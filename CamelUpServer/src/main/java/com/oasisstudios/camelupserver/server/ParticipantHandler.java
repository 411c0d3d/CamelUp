package com.oasisstudios.camelupserver.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.oasisstudios.camelupserver.dataaccess.dto.*;
import com.oasisstudios.camelupserver.dataaccess.jsonstorage.LobbyService;
import com.oasisstudios.camelupserver.domain.repository.ClientHandlersRepository;
import com.oasisstudios.camelupserver.domain.repository.LobbyRepository;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import static com.oasisstudios.camelupserver.dataaccess.dto.Packet.Type.*;

public class ParticipantHandler extends Thread {
    @Setter
    private IPacketListener moveListener;
    private Gson gson;
    private Socket clientChannel;
    private JsonWriter out;
    private JsonReader in;
    @Getter
    private final ClientAck clientAck;
    private int joinedLobbyId = -1;
    private String playerName;
    private final LobbyService lobbyService = new LobbyService();
    private final Logger logger = LogManager.getLogger(ParticipantHandler.class);
    private final ClientHandlersRepository clientHandlersRepository;

    public ParticipantHandler(Socket channel, int userId) {
        this.clientChannel = channel;
        this.clientAck = new ClientAck(userId);
        this.gson = new Gson();
        clientHandlersRepository = ClientHandlersRepository.getClientHandlersRepository();
    }

    public void clearMoveListener() {
        this.moveListener = null;
    }

    public void run() {
        try {
             out = new JsonWriter(new OutputStreamWriter(clientChannel.getOutputStream()));
             in = new JsonReader(new InputStreamReader(clientChannel.getInputStream()));
            //Thread.sleep(1000);
            Packet packet = Packet.builder().withType(CLIENT_ACK).withContent(clientAck).build();
            sendPacket(packet);
            // Set up the I/O for the client

            while (true) {
                JsonObject jsonObject = gson.fromJson(in, JsonObject.class);
                if (jsonObject == null) {
                    break;
                }
                packet = gson.fromJson(jsonObject, Packet.class);
                //logger.info(packet.toString());
                onPacketReceived(packet);
            }
        } catch (IOException e) {
            logger.error("Error handling Participant client", e);
        } catch (Exception e) {
            logger.error("Interruption Error Participant client", e);
        } finally {
            closeResources();
        }
    }

    /**
     * Processes an incoming packet and dispatches it based on its type.
     *
     * @param packet the incoming {@link Packet}.
     */
    public void onPacketReceived(final Packet packet) throws Exception {
        if (packet == null || packet.getType() == null) {
            logger.warn("Participant handler received invalid packet: {}", packet);
            return;
        }
        //logger.info("Received packet from {}", this.clientAck.getClientId());
        switch (packet.getType()) {
            case REQUEST_LOBBY_LIST:
                sendRunningLobbyList();
                break;
            case REQUEST_RECENT_GAMES:
                sendRecentGames(packet);
                break;
            case FINAL_BET:
                finalBet(packet);
                break;
            case MOVE_VISUALIZED:
                visualised();
                break;
            case PLACE_PLAYER_CARD:
                placePlayerCard(packet);
                break;
            case PLAYER_REGISTRATION:
                setPlayerName(packet);
                break;
            case ROLL_DICE:
                rollDice(packet);
                break;
            case STAGE_BET:
                stageBet(packet);
                break;
            case JOIN_LOBBY:
                joinLobby(packet);
                break;
            default:
                sendFailure(packet, "Received packet of type" + packet.getType().toString());
                break;
        }
    }

    private void setPlayerName(Packet packet) throws IOException {
        PlayerRegistration playerReg = gson.fromJson(packet.getContent().toString(), PlayerRegistration.class);
        if (playerName == null) {
            playerName = playerReg.getPlayerName();
            clientAck.setClientName(playerName);
            logger.info(playerName+ "Registered");
            Player player = new Player();
            player.setPlayerId(this.clientAck.getClientId());
            player.setName(playerName);
            //AdminHandler.getInstance(this.clientChannel).sendPendingPlayers(player);
            sendSuccess(packet);
        } else {
            sendFailure(packet, "Player name already set.");
        }
    }

    private void joinLobby(Packet packet) throws IOException {
        JoinLobby lobbyToJoin = gson.fromJson(packet.getContent().toString(), JoinLobby.class);
        Lobby lobby;
        Packet lobbyPacket = null;
        try {
            lobby = lobbyService.readLobbyByFileName(lobbyToJoin.getLobbyId());
        } catch (Exception e) {
            logger.error("Error reading lobby", e);
        }
        if (lobbyToJoin.getLobbyId().intValue() == -1) {
            if (lobbyService.leaveLobbyAsSpectator(joinedLobbyId, clientAck.getClientId())) {
                sendSuccess(packet);
            } else {
                sendFailure(packet, "Already left lobby");
            }
        } else {
            if (lobbyToJoin.getJoinAsPlayer()) {
                try {
                    Player player = new Player();
                    player.setPlayerId(this.clientAck.getClientId());
                    player.setName(playerName);
                    lobbyService.joinLobbyAsPlayer(lobbyToJoin.getLobbyId(), player);
                    this.clientHandlersRepository.addLobbyParticipant(lobbyToJoin.getLobbyId(), this);
                } catch (Exception e) {
                    logger.error("Could not join Lobby as Player", e);
                }
            } else {
                try {
                    if (lobbyService.joinLobbyAsSpectator(lobbyToJoin.getLobbyId(), clientAck.getClientId())) {
                        joinedLobbyId = lobbyToJoin.getLobbyId();
                        this.clientHandlersRepository.addLobbyParticipant(joinedLobbyId, this);
                        sendSuccess(packet);
                    } else {
                        sendFailure(packet, "Could not join Lobby as Spectator");
                    }
                } catch (Exception e) {
                    sendFailure(packet, e.getMessage());
                }
            }
        }
    }

    private void sendRecentGames(final Packet packet) throws IOException {
        Packet outgoingPacket;
        RequestRecentGames recentGames = gson.fromJson(packet.getContent().toString(), RequestRecentGames.class);
        outgoingPacket = Packet.builder()
                .withType(RECENT_GAMES)
                .withContent(lobbyService.readFinishedLobbies(recentGames.getNumGames()))
                .build();
        sendPacket(outgoingPacket);
    }

    private void sendRunningLobbyList() throws IOException {
        Packet outgoingPacket = Packet.builder().withType(LOBBY_LIST)
                .withContent(lobbyService.readRunningLobbies(30)).build();
        sendPacket(outgoingPacket);
    }

    public void sendPacket(Packet outgoingPacket) throws IOException {
        gson.toJson(outgoingPacket, outgoingPacket.getClass(), out);
        out.flush();
    }

    public void closeResources() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientChannel != null && clientChannel.isConnected()) clientChannel.close();
            logger.info("Closed connection for client ID: {}", clientAck.getClientId());
        } catch (IOException e) {
            logger.error("Error closing resources: {}", e.getMessage(), e);
        }
    }

    public void sendSuccess(Packet packet) throws IOException {
        SuccessFeedback successFeedback = new SuccessFeedback(true, packet, "");
        Packet outgoingPacket = Packet.builder().withType(SUCCESS_FEEDBACK).withContent(successFeedback)
                .build();
        sendPacket(outgoingPacket);
    }

    public void sendFailure(Packet packet, String errorString) throws IOException {
        SuccessFeedback successFeedback = new SuccessFeedback(true, packet, errorString);
        Packet outgoingPacket = Packet.builder().withType(SUCCESS_FEEDBACK).withContent(successFeedback)
                .build();
        sendPacket(outgoingPacket);
    }
    private void stageBet(Packet packet) {
        if (moveListener != null) {
            moveListener.onPacketReceived(packet);
        } else {
            logger.warn("No moveListener set for packet: {}", packet);
        }
    }

    private void rollDice(Packet packet) {
        if (moveListener != null) {
            moveListener.onPacketReceived(packet);
        } else {
            logger.warn("No moveListener set for packet: {}", packet);
        }
    }

    private void finalBet(Packet packet) {
        if (moveListener != null) {
            moveListener.onPacketReceived(packet);
        } else {
            logger.warn("No moveListener set for packet: {}", packet);
        }
    }

    private void visualised() {
        // implement functionality
    }

    private void placePlayerCard(Packet packet) {
        if (moveListener != null) {
            moveListener.onPacketReceived(packet);
        } else {
            logger.warn("No moveListener set for packet: {}", packet);
        }
    }
}
