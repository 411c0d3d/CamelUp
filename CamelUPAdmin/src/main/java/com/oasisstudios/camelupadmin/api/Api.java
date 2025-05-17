package com.oasisstudios.camelupadmin.api;

import com.google.gson.Gson;
import com.oasisstudios.camelupadmin.dto.*;
import com.oasisstudios.camelupadmin.model.GameState;
import com.oasisstudios.camelupadmin.service.IMessageListener;
import com.oasisstudios.camelupadmin.service.IPendingPlayersListener;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

import static com.oasisstudios.camelupadmin.dto.Packet.Type.*;
import static java.lang.System.out;

/**
 * Typesafe Api which delegates packets to the {@link Client}
 */
public class Api implements IMessageListener{
    public boolean isConnected = false; 
    private static final Logger logger = LoggerFactory.getLogger(Api.class);
    public String playerName;
    @Setter
    public IPendingPlayersListener playersListener;
    @Setter
    public IMessageListener messageListener;
    private Client client;
    private Gson gson;
    private final ConcurrentMap<Class<?>, CompletableFuture<Object>> pendingRequests = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, CompletableFuture<Object>> pendingFeedbackRequests = new ConcurrentHashMap<>();
    private Consumer<GameState> gameStateEventHandler;
    private Consumer<GameEnd> gameEndEventHandler;
    private Consumer<PendingPlayerList> pendingPlayersEventHandler;

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
    @Override
    public void onPacketReceived(final Packet packet) {
        messageListener.onMessageReceived("connected");
        switch (packet.getType()) {
            case LOBBY_LIST:
                completePendingRequest(packet, LobbyList.class);
                break;
            case RECENT_GAMES:
                completePendingRequest(packet, RecentGames.class);
                break;
            case SUCCESS_FEEDBACK:
                completePendingRequest(packet, SuccessFeedback.class);
                break;
            case PENDING_PLAYER_LIST:
                completePendingRequest(packet, PendingPlayerList.class);
                break;
                
////                completePendingRequest(packet, PendingPlayerList.class);
//                processPendingPlayers(packet);
//                PendingPlayerList pendingPlayerList = this.gson.fromJson(packet.getContent().toString(), PendingPlayerList.class);
//                playersListener.onPendingPlayersReceived(pendingPlayerList);
////                playersListener.onPendingPlayersReceived(packet.getContent() == null ? new PendingPlayerList(new ArrayList<>()) : toContent(packet, PendingPlayerList.class));
////                notifyEventHandler(this.pendingPlayersEventHandler, toContent(packet, PendingPlayerList.class));
            case ADMIN_CHANNEL:
                completePendingRequest(packet, AdminChannel.class);
                logger.info("connected");
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
//            out.println(content);
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
    public CompletableFuture<AdminChannel> connect(final String host, final int port) throws IOException {
        logger.trace("--> Connecting to \"{}:{}\" ...", host == null ? "localhost" : host, port);
        var future = new CompletableFuture<AdminChannel>();
        addPendingObjectRequest(AdminChannel.class, future);

        try {
            client.setMessageListener(this);
            client.connect(host, port);
            isConnected = true;

        } catch (IOException e) {
            pendingRequests.remove(String.class);
            isConnected = false;
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
                logger.warn("A pending request is already present for " + responseClass.getName());
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
                logger.warn("A pending request is already present for " + responseType);
            }
            this.pendingFeedbackRequests.put(responseType.value(), (CompletableFuture<Object>) value);
        }
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


    public CompletableFuture<PendingPlayerList> getPendingPlayersList() {
        logger.trace("--> Requesting pending player list...");
        var future = new CompletableFuture<PendingPlayerList>();
        addPendingObjectRequest(PendingPlayerList.class, future);

        // Send the request packet to the server
        client.sendMessage(Packet.builder()
                .withType(PENDING_PLAYER_LIST)
                .withContent(PendingPlayerList.builder()
                        .build())
                .build());

        return future;
    }

    private void processPendingPlayers(final Packet packet) {
        try {
            PendingPlayerList pendingPlayerList = gson.fromJson(packet.getContent().toString(), PendingPlayerList.class);

            if (pendingPlayerList == null) {
                logger.warn("Empty PendingPlayerList received");
                return;
            }

            CompletableFuture.runAsync(() -> {
                if (playersListener != null) {
                    playersListener.onPendingPlayersReceived(pendingPlayerList);
                }
            }).exceptionally(ex -> {
                logger.error("Error handling PendingPlayerList: {}", ex.getMessage());
                return null;
            });
        } catch (Exception e) {
            logger.error("Error processing PendingPlayerList packet: ", e);
        }
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


    public void close() {
        client.close();
    }

    public void sendPacket(Packet outgoingPacket) throws IOException {
        client.sendMessage(outgoingPacket);
    }

    /**
     * On message received.
     *
     * @param message the message
     */
    @Override
    public void onMessageReceived(String message) {
        this.messageListener.onMessageReceived(message);
    }

    /**
     * On connect.
     */
    @Override
    public void onConnect() {
        isConnected = true;
        this.messageListener.onConnect();
    }

    /**
     * On disconnect.
     */
    @Override
    public void onDisconnect() {
        isConnected = false;
        this.messageListener.onDisconnect();
    }
}
