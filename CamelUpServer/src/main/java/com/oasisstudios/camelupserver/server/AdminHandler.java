package com.oasisstudios.camelupserver.server;

import java.io.*;
import java.net.Socket;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.oasisstudios.camelupserver.dataaccess.dto.*;
import com.oasisstudios.camelupserver.dataaccess.jsonstorage.LobbyService;
import com.oasisstudios.camelupserver.domain.repository.ClientHandlersRepository;
import com.oasisstudios.camelupserver.domain.service.GenerateUniqueCamels;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;

import com.oasisstudios.camelupserver.domain.repository.LobbyRepository;
import com.oasisstudios.camelupserver.domain.service.LobbyManager;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.oasisstudios.camelupserver.dataaccess.dto.Packet.Type.*;

public class AdminHandler extends Thread {
    private static AdminHandler instance;
    private final Gson gson;
    public final HashSet<Player> pendingPlayers = new HashSet<>();
    private final Socket clientChannel;
    private final Logger logger = LogManager.getLogger(AdminHandler.class);
    private final LobbyManager lobbyManager;
    @Getter
    public final LobbyRepository lobbyRepository;
    @Getter
    public final ClientHandlersRepository clientHandlersRepository;
    private AtomicInteger lobbyId = new AtomicInteger(1);
    JsonReader in;
    JsonWriter out;

    // Constructor
    private AdminHandler(Socket socketChannel) {
        this.clientChannel = socketChannel;
        this.gson = new Gson();
        this.lobbyManager = new LobbyManager(new LobbyRepository());
        this.lobbyRepository = new LobbyRepository();
        clientHandlersRepository = ClientHandlersRepository.getClientHandlersRepository();
    }

    // Singleton pattern
    public static synchronized AdminHandler getInstance(Socket clientChannel) {
        if (instance == null) {
            instance = new AdminHandler(clientChannel);
        }
        return instance;
    }    
    
    public static synchronized AdminHandler getInstance() {
        return instance;
    }

    @Override
    public void run() {
        try {
            out = new JsonWriter(new OutputStreamWriter(clientChannel.getOutputStream()));
            in = new JsonReader(new InputStreamReader(clientChannel.getInputStream()));
            sendPacket(Packet.builder().withType(ADMIN_CHANNEL).withContent(new AdminChannel()).build());
            // Read data until the full JSON object is received
            while (true) {
                in.setStrictness(Strictness.LENIENT);
                Packet packet = parsePacket(in);
                try {
                    onPacketReceived(packet);
                } catch (InterruptedException e) {
                    logger.error("Admin onPacketReceived Interrupted Exception: {}", e.getMessage());
                    logger.error("Admin broken Packet: {}", packet);
                }
            }
        } catch (IOException | JsonSyntaxException e) {
            logger.error("Admin IOException Interrupted Exception: {}", e.getMessage());
            e.printStackTrace();
        } finally {
            closeClientConnection();
        }
    }

    // Process incoming packet based on its type
    public void onPacketReceived(Packet packet) throws IOException, InterruptedException {
        System.out.println("Admin sent: " + packet);
        if (packet == null || packet.getType() == null) {
            logger.warn("Admin handler received invalid packet: {}", packet);
            return;
        }

        logger.info("Admin sent stuff : {}", packet.toString());
        switch (packet.getType()) {
            case CREATE_LOBBY:
                createGameLobby(packet);
                break;
            case START_GAME:
                startGame(packet);
                break;
            case PAUSE_GAME:
                pauseGame(packet);
                break;
            case RESUME_GAME:
                resumeGame(packet);
                break;
            case ADMIN_CHANNEL:
                connectAdmin();
                break;
            default:
                sendFailure(packet, "Received packet of type: " + packet.getType().toString());
                break;
        }
    }

    // Handle specific admin actions
    private void createGameLobby(Packet packet) throws IOException {
        CreateLobby createLobby = this.gson.fromJson(packet.getContent().toString(), CreateLobby.class);
        Lobby lobby = createLobby.getLobby();
        if (lobby != null) {
            try {
                // create lobby now
                if (lobby.getLobbyId() == 0) {
                    lobby.getGameState().setGamePhase(GameState.GamePhase.CREATED);
                    List<Camel> camels = GenerateUniqueCamels.createCamels(lobby.getGameState().getGameConfig().getCamelCount());
                    lobby.getGameState().getGameConfig().setCamels(camels);
                } else {
                    lobby.setLobbyId(lobbyId.getAndIncrement());
                    // Store json
                    if (!lobby.getGameState().getPlayers().isEmpty()) {
                        var lobbyPlayerIds = lobby.getGameState().getPlayers().stream().map(Player::getPlayerId).collect(Collectors.toSet());

                        var playersHandlers = this.clientHandlersRepository.getParticipantClientHandlers().stream()
                                .filter(participantHandler -> lobbyPlayerIds.contains(participantHandler.getClientAck().getClientId()))
                                .collect(Collectors.toSet());

                        for (var playersHandler : playersHandlers) {
                            this.clientHandlersRepository.addLobbyParticipant(lobby.getLobbyId(), playersHandler);
                        }
                    }
                }
                logger.info("Raw:\t" + lobby);
                logger.info("toString" + lobby);
                updateSpectatorsByLobby(lobby);
                LobbyService.storeLobby(lobby);
                sendSuccess(Packet.builder().withType(SUCCESS_FEEDBACK).withContent(new SuccessFeedback()).build());
            } catch (IOException e) {
                sendFailure(packet, "Lobby Creation Failed");
            }
        } else {
            sendFailure(packet, "Invalid Lobby Data");
        }
    }
    
    
    public void createLobbyFromConfig(String gameConfigJsonFileName, String lobbyName) {
        GameConfig gameConfig = null;
        try {
            gameConfig = LobbyService.readGameConfigByFileName(gameConfigJsonFileName);
        } catch (Exception e) {
            logger.error("Game Config mapping failed: {}", e.getMessage());
        }
        Lobby lobby = new Lobby();
        GameState gameState = new GameState();
        gameState.setTurns(0);
        gameState.setMoveTimeRemaining(-1);
        gameState.setFinalBets(new FinalBets(new ArrayList<>(), new ArrayList<>()));
        gameState.setGameDuration(gameConfig.getMaxGameDuration());
        lobby.setGameState(gameState);
        lobby.setName(lobbyName);
        lobby.getGameState().setGameConfig(gameConfig);
        try {
            // create lobby now
            lobby.getGameState().setGamePhase(GameState.GamePhase.CREATED);
            List<Camel> camels = GenerateUniqueCamels.createCamels(lobby.getGameState().getGameConfig().getCamelCount());
            lobby.getGameState().getGameConfig().setCamels(camels);
            lobby.setLobbyId(lobbyId.getAndIncrement());
            LobbyService.storeLobby(lobby);
        } catch (IOException e) {
            logger.error("Lobby Creation Failed", e.getMessage());
        }
    }
        // Called after the join is over
        public void updatePlayersByLobby(Lobby lobby) {
        if (!lobby.getGameState().getPlayers().isEmpty()) {
            var lobbyPlayerIds = lobby.getGameState().getPlayers().stream().map(Player::getPlayerId).collect(Collectors.toSet());

            var playersHandlers = this.clientHandlersRepository.getParticipantClientHandlers().stream()
                    .filter(participantHandler -> lobbyPlayerIds.contains(participantHandler.getClientAck().getClientId()))
                    .collect(Collectors.toSet());

            for (var playersHandler : playersHandlers) {
                this.clientHandlersRepository.addLobbyParticipant(lobby.getLobbyId(), playersHandler);
            }
        }    
    }
    public void updateSpectatorsByLobby(Lobby lobby) {
        if (!lobby.getObserverIds().isEmpty()) {
            var lobbySpectatorIds = lobby.getObserverIds();
            var spectatorHandlers = this.clientHandlersRepository.getParticipantClientHandlers().stream()
                    .filter(participantHandler -> lobbySpectatorIds.contains(participantHandler.getClientAck().getClientId()))
                    .collect(Collectors.toSet());

            for (var spectatorHandler : spectatorHandlers) {
                this.clientHandlersRepository.addLobbyParticipant(lobby.getLobbyId(), spectatorHandler);
            }
        }
    }

    public void startGame(Packet packet) throws IOException {
        StartGame startGame = gson.fromJson(packet.getContent().toString(), StartGame.class);
        Lobby lobby = lobbyRepository.getLobby(String.valueOf(startGame.getLobbyId()));
        if (lobby != null && lobby.getGameState().getPlayers().size() >= 2 && lobby.getGameState().getGamePhase().equals(GameState.GamePhase.CREATED)) {
            lobbyManager.startGameLoop(lobby);
            sendSuccess(packet);
        } else {
            sendFailure(packet, "Game cannot start. Lobby not ready or insufficient players.");
        }
    }

    public void startGame(Integer lobbyID) throws IOException {
        Lobby lobby = lobbyRepository.getLobby(String.valueOf(lobbyID));
        if (lobby != null && lobby.getGameState().getPlayers().size() >= 2 && lobby.getGameState().getGamePhase().equals(GameState.GamePhase.CREATED)) {
            lobbyManager.startGameLoop(lobby);
        } else {
            logger.error("Game cannot start. Lobby not ready or insufficient players.");
        }
    }

    private Packet parsePacket(JsonReader reader) throws IOException {
        try {
            reader.setStrictness(Strictness.LENIENT);
            if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                return gson.fromJson(reader, Packet.class);
            } else {
                logger.error("Expected a JSON object, but got something else.");
                return null; // Or handle it differently
            }
        } catch (JsonSyntaxException | EOFException e) {
            logger.error("Failed to parse packet due to malformed JSON or incomplete data.", e);
            return null;
        }
    }

    private void pauseGame(Packet packet) throws IOException, InterruptedException {
        PauseGame pauseGame = gson.fromJson(packet.getContent().toString(), PauseGame.class);
        Lobby lobby = lobbyRepository.getLobby(String.valueOf(pauseGame.getLobbyId()));
        if (lobby != null && lobby.getGameState().getGamePhase() == GameState.GamePhase.PLAYING) {
            lobbyManager.pauseGameLoop(lobby);
            sendSuccess(packet);
        } else {
            sendFailure(packet, "Game cannot be paused. Invalid state.");
        }
    }

    private void resumeGame(Packet packet) throws IOException, InterruptedException {
        ResumeGame resumeGame = gson.fromJson(packet.getContent().toString(), ResumeGame.class);
        Lobby lobby = lobbyRepository.getLobby(String.valueOf(resumeGame.getLobbyId()));
        if (lobby != null && lobby.getGameState().getGamePhase() == GameState.GamePhase.PAUSED) {
            lobbyManager.resumeGameLoop(lobby);
            sendSuccess(packet);
        } else {
            sendFailure(packet, "Game cannot be resumed. Invalid state.");
        }
    }

    // Send a packet to the client (admin)
    public void sendPacket(Packet outgoingPacket) throws IOException {
        gson.toJson(outgoingPacket, outgoingPacket.getClass(), out);
        out.flush();
    }

    public void sendSuccess(Packet packet) throws IOException {
        SuccessFeedback successFeedback = new SuccessFeedback(true, packet, "");
        Packet outgoingPacket = Packet.builder().withType(SUCCESS_FEEDBACK).withContent(successFeedback).build();
        sendPacket(outgoingPacket);
    }

    public void sendFailure(Packet packet, String errorString) throws IOException {
        SuccessFeedback successFeedback = new SuccessFeedback(false, packet, errorString);
        Packet outgoingPacket = Packet.builder().withType(SUCCESS_FEEDBACK).withContent(successFeedback).build();
        sendPacket(outgoingPacket);
    }

    public void connectAdmin() {
        AdminChannel AdminChannel = new AdminChannel();
        try {
            this.sendSuccess(Packet.builder().withType(Packet.Type.ADMIN_CHANNEL).withContent(AdminChannel).build());
            logger.info("Admin Success feedback");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPendingPlayers(Player player) throws IOException {
        if (pendingPlayers.stream().noneMatch(p-> Objects.equals(p.getPlayerId(), player.getPlayerId()))) {
            pendingPlayers.add(player);
        }
        PendingPlayerList pendingPlayerList = new PendingPlayerList((ArrayList<Player>) pendingPlayers.stream().toList());
        Packet outgoingPacket = Packet.builder().withType(PENDING_PLAYERS).withContent(pendingPlayerList)
                .build();
        sendPacket(outgoingPacket);
    }

    private void closeClientConnection() {
        try {
            if (clientChannel.isConnected()) {
                clientChannel.close();
                System.out.println("Admin client disconnected.");
            }
        } catch (IOException ignored) {
        }
    }

}
