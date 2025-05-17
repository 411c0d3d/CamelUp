package cameldown.camelup.client.core.api;

import cameldown.camelup.interfacedefinition.game.v3.*;
import cameldown.camelup.interfacedefinition.game.v3.PlacePlayerCard;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

import static cameldown.camelup.interfacedefinition.game.v3.Packet.Type.*;

/**
 * Typesafe Api which delegates packets to the {@link Client}
 */
public class Api {
    private static final Logger logger = LoggerFactory.getLogger(Api.class);

    private final Client client;
    @Getter @Setter
    private int clientId;
    private final Gson gson;
    private final ConcurrentMap<Class<?>, CompletableFuture<Object>> pendingRequests = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, CompletableFuture<Object>> pendingFeedbackRequests = new ConcurrentHashMap<>();
    private Consumer<GameState> gameStateEventHandler;
    private Consumer<GameEnd> gameEndEventHandler;
    private Consumer<JoinLobby> assignedToLobbyEventHandler;

    /**
     * Constructs an API instance.
     *
     * @param gson the {@link Gson} instance used for JSON serialization/deserialization.
     */
    public Api(final Gson gson) {
        this.gson = gson;
        this.client = new Client(this::onPacketReceived, gson);
    }

    /**
     * Notifies the specified event handler with the given object if the handler is set.
     *
     * @param eventHandler the event handler to notify.
     * @param obj          the object to pass to the handler.
     * @param <T>          the type of the object.
     */
    private static <T> void notifyEventHandler(final Consumer<T> eventHandler, final T obj) {
        if (eventHandler != null) {
            eventHandler.accept(obj);
        }
    }

    /**
     * Processes an incoming packet and dispatches it based on its type.
     *
     * @param packet the incoming {@link Packet}.
     */
    private void onPacketReceived(final Packet packet) {
        switch (packet.getType()) {
            case LOBBY_LIST:
                completePendingRequest(packet, LobbyList.class);
                break;
            case CLIENT_ACK:
                completePendingRequest(packet, ClientAck.class);
                break;
            case JOIN_LOBBY:
                notifyEventHandler(this.assignedToLobbyEventHandler, toContent(packet, JoinLobby.class));
                break;
            case RECENT_GAMES:
                completePendingRequest(packet, RecentGames.class);
                break;
            case SUCCESS_FEEDBACK:
                completePendingRequest(packet, SuccessFeedback.class);
                break;
            case GAME_STATE:
                notifyEventHandler(this.gameStateEventHandler, toContent(packet, GameState.class));
                break;
            case GAME_END:
                notifyEventHandler(this.gameEndEventHandler, toContent(packet, GameEnd.class));
                break;

            default:
                throw new IllegalArgumentException("Unknown PacketType: " + packet.getType());
        }
    }

    /**
     * Completes a pending request by associating a received packet's content with its corresponding future.
     *
     * @param packet        the received {@link Packet}.
     * @param responseClass the expected response class type.
     * @param <T>           the type of the response.
     */
    private <T> void completePendingRequest(final Packet packet, final Class<T> responseClass) {
        var content = toContent(packet, responseClass);

        CompletableFuture<Object> request;
        if (content instanceof final SuccessFeedback feedback) {
            logger.trace("<-- Received feedback response for request \"{}\" with content: {}", feedback.getRequest(), content);
            request = pendingFeedbackRequests.remove(feedback.getRequest().getType().value());
            if (!feedback.getSuccess()) {
                throw new IllegalStateException("Request " + feedback.getRequest() + " was not successfully. Error: " + feedback.getError());
            }
        } else {
            logger.trace("<-- Received response of type {} for enum {} with content: {}", content.getClass().getSimpleName(), packet.getType(), content);
            request = pendingRequests.remove(responseClass);
        }

        if (request == null) {
            logger.warn("Found no pending request future for type {} with content: {}", content.getClass().getSimpleName(), content);
            return;
        }

        request.complete(content);
    }

    /**
     * Converts a packet's content to the specified class type.
     *
     * @param packet the {@link Packet} containing the content.
     * @param clazz  the target class type.
     * @param <T>    the type of the result.
     * @return the deserialized content.
     */
    private <T> T toContent(final Packet packet, final Class<T> clazz) {
        return gson.fromJson(gson.toJson(packet.getContent()), clazz);
    }

    /**
     * Initiates a connection to the specified IP and port.
     *
     * @param host the domain name or the ip address of the host to connect to, null for the loopback
     * @param port the port to connect to.
     * @return a {@link CompletableFuture} representing the connection acknowledgment.
     */
    public CompletableFuture<ClientAck> connect(final String host, final int port) throws IOException {
        logger.trace("--> Connecting to \"{}:{}\" ...", host == null ? "localhost" : host, port);
        var future = new CompletableFuture<ClientAck>();
        addPendingObjectRequest(ClientAck.class, future);
        try {
            client.connect(host, port);
        } catch (IOException e) {
            pendingRequests.remove(ClientAck.class);
            throw e;
        }

        return future;
    }

    /**
     * Adds a pending request for a response of the specified type.
     *
     * @param responseClass the response class type.
     * @param value         the {@link CompletableFuture} associated with the request.
     * @param <T>           the type of the response.
     */
    private <T> void addPendingObjectRequest(final Class<T> responseClass, final CompletableFuture<T> value) {
        synchronized (this.pendingRequests) {
            if (this.pendingRequests.get(responseClass) != null) {
                throw new IllegalStateException("A pending request is already present for " + responseClass.getName());
            }
            this.pendingRequests.put(responseClass, (CompletableFuture<Object>) value);
        }
    }

    /**
     * Adds a pending feedback request for a specific packet type.
     *
     * @param responseType the packet type.
     * @param value        the {@link CompletableFuture} associated with the feedback.
     */
    private void addPendingFeedbackRequest(final Packet.Type responseType, final CompletableFuture<?> value) {
        synchronized (this.pendingFeedbackRequests) {
            if (this.pendingFeedbackRequests.get(responseType.value()) != null) {
                throw new IllegalStateException("A pending request is already present for " + responseType);
            }
            this.pendingFeedbackRequests.put(responseType.value(), (CompletableFuture<Object>) value);
        }
    }

    /**
     * Sends a request to register a player.
     *
     * @param name the player name
     * @return a {@link CompletableFuture} containing the {@link SuccessFeedback}.
     */
    public CompletableFuture<SuccessFeedback> registerPlayer(final String name) {
        logger.trace("--> Registering player as \"{}\" ...", name);
        var future = new CompletableFuture<SuccessFeedback>();
        addPendingFeedbackRequest(PLAYER_REGISTRATION, future);
        
        client.sendMessage(Packet.builder()
            .withType(PLAYER_REGISTRATION)
            .withContent(PlayerRegistration.builder()
                .withPlayerName(name)
                .build())
            .build());

        return future;
    }

    /**
     * Sends a request to place a final bet.
     *
     * @param camelId the camel to bet on
     * @param isFirst if the camel will be first or last
     * @return a {@link CompletableFuture} containing the {@link SuccessFeedback}.
     */
    public CompletableFuture<SuccessFeedback> placeFinalBet(final int camelId, final boolean isFirst) {
        logger.trace("--> Placing final bet on camel {}, is first {} ...", camelId, isFirst);
        var future = new CompletableFuture<SuccessFeedback>();
        addPendingFeedbackRequest(FINAL_BET, future);

        client.sendMessage(Packet.builder()
            .withType(FINAL_BET)
            .withContent(FinalBet.builder()
                .withId(camelId)
                .withIsFirst(isFirst)
                .build())
            .build());

        return future;
    }

    /**
     * Sends a request to roll a dice.
     *
     * @return a {@link CompletableFuture} containing the {@link SuccessFeedback}.
     */
    public CompletableFuture<SuccessFeedback> rollDice() {
        logger.trace("--> Rolling dice ...");
        var future = new CompletableFuture<SuccessFeedback>();
        addPendingFeedbackRequest(ROLL_DICE, future);

        client.sendMessage(Packet.builder()
            .withType(ROLL_DICE)
            .withContent(RollDice.builder().build())
            .build());

        return future;
    }

    /**
     * Sends a request to bet on stage.
     *
     * @param camelId the camel to bet on
     * @return a {@link CompletableFuture} containing the {@link SuccessFeedback}.
     */
    public CompletableFuture<SuccessFeedback> betOnStage(final int camelId) {
        logger.trace("--> Betting on stage on camel {} ...", camelId);
        var future = new CompletableFuture<SuccessFeedback>();
        addPendingFeedbackRequest(STAGE_BET, future);

        client.sendMessage(Packet.builder()
            .withType(STAGE_BET)
            .withContent(StageBet.builder()
                .withCamelId(camelId)
                .build())
            .build());

        return future;
    }

    /**
     * Sends a request to leave a lobby.
     */
    public CompletableFuture<SuccessFeedback> leaveLobby() {
        logger.trace("--> Leaving lobby...");
        return this.joinLobby(-1, false);
    }

    /**
     * Sends a request to join a lobby.
     *
     * @param lobbyId      the ID of the lobby to join.
     * @param joinAsPlayer if the lobby should be joined as player.
     * @return a {@link CompletableFuture} containing the {@link JoinLobby} details.
     */
    public CompletableFuture<SuccessFeedback> joinLobby(int lobbyId, boolean joinAsPlayer) {
        logger.trace("--> Joining lobby {} as player: {} ...", lobbyId, joinAsPlayer);
        var future = new CompletableFuture<SuccessFeedback>();
        addPendingFeedbackRequest(JOIN_LOBBY, future);

        client.sendMessage(Packet.builder()
            .withType(JOIN_LOBBY)
            .withContent(JoinLobby.builder()
                .withLobbyId(lobbyId)
                .withJoinAsPlayer(joinAsPlayer)
                .build())
            .build());

        return future;
    }

    /**
     * Sends the move was visualized.
     *
     * @return a {@link CompletableFuture} containing the {@link SuccessFeedback}.
     */
    public CompletableFuture<SuccessFeedback> moveVisualized() {
        logger.trace("--> Visualized move ...");
        var future = new CompletableFuture<SuccessFeedback>();
        addPendingFeedbackRequest(MOVE_VISUALIZED, future);

        client.sendMessage(Packet.builder()
            .withType(MOVE_VISUALIZED)
            .withContent(MoveVisualized.builder()
                .build())
            .build());

        return future;
    }

    /**
     * Sends a request to place a player card.
     *
     * @param spaceId         the space to place the card on
     * @param movingDirection th direction of the card
     * @return a {@link CompletableFuture} containing the {@link SuccessFeedback}.
     */
    public CompletableFuture<SuccessFeedback> placePlayerCard(final int spaceId, final int movingDirection) {
        logger.trace("--> Placing player card on {} ...", spaceId);
        var future = new CompletableFuture<SuccessFeedback>();
        addPendingFeedbackRequest(PLACE_PLAYER_CARD, future);

        client.sendMessage(Packet.builder()
            .withType(PLACE_PLAYER_CARD)
            .withContent(PlacePlayerCard.builder()
                .withSpaceId(spaceId)
                .withMovingDirection(movingDirection)
                .build())
            .build());

        return future;
    }

    /**
     * Sends a request to fetch the lobby list.
     *
     * @return a {@link CompletableFuture} containing the {@link LobbyList}.
     */
    public CompletableFuture<LobbyList> listLobbies() {
        logger.trace("--> Listing lobbies...");
        var future = new CompletableFuture<LobbyList>();
        addPendingObjectRequest(LobbyList.class, future);

        client.sendMessage(Packet.builder()
            .withType(REQUEST_LOBBY_LIST)
            .withContent(RequestLobbyList.builder()
                .build())
            .build());

        return future;
    }

    /**
     * Sends a request to list recent games
     *
     * @param numGames number of games to list
     * @return a {@link CompletableFuture} containing the {@link RecentGames}.
     */
    public CompletableFuture<RecentGames> listRecentGames(int numGames) {
        logger.trace("--> Listing recent {} games ...", numGames);
        var future = new CompletableFuture<RecentGames>();
        addPendingObjectRequest(RecentGames.class, future);

        client.sendMessage(Packet.builder()
            .withType(REQUEST_RECENT_GAMES)
            .withContent(RequestRecentGames.builder()
                .withNumGames(numGames)
                .build())
            .build());

        return future;
    }

    /**
     * Adds an event handler for game state changes.
     *
     * @param handler the game state changed event handler
     */
    public void addOnGameStateEventHandler(final Consumer<GameState> handler) {
        if (this.gameStateEventHandler != null) {
            throw new EventHandlerAlreadySetException();
        }
        this.gameStateEventHandler = handler;
    }

    /**
     * Removes the current game state event handler.
     */
    public void removeOnGameStateEventHandler() {
        this.gameStateEventHandler = null;
    }

    /**
     * Adds an event handler for the game end event.
     *
     * @param handler the game end event handler
     */
    public void addOnGameEndEventHandler(Consumer<GameEnd> handler) {
        if (this.gameEndEventHandler != null) {
            throw new EventHandlerAlreadySetException();
        }
        this.gameEndEventHandler = handler;
    }

    /**
     * Removes the current on game end event handler.
     */
    public void removeOnGameEndEventHandler() {
        this.gameEndEventHandler = null;
    }

    /**
     * Adds an event handler for the assigned to lobby event.
     *
     * @param handler the assigned to lobby event handler
     */
    public void addOnAssignedToLobbyEventHandler(final Consumer<JoinLobby> handler) {
        if (this.assignedToLobbyEventHandler != null) {
            throw new EventHandlerAlreadySetException();
        }
        this.assignedToLobbyEventHandler = handler;
    }

    /**
     * Removes the assigned to lobby event handler.
     */
    public void removeAssignedToLobbyEventHandler() {
        this.assignedToLobbyEventHandler = null;
    }

    public void close() {
        client.close();
    }
}
