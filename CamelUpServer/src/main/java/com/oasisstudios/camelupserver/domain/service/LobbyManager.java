package com.oasisstudios.camelupserver.domain.service;

import com.oasisstudios.camelupserver.dataaccess.dto.*;
import com.oasisstudios.camelupserver.domain.repository.ClientHandlersRepository;
import com.oasisstudios.camelupserver.domain.repository.LobbyRepository;
import com.oasisstudios.camelupserver.server.ParticipantHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Service
public class LobbyManager {
    private final LobbyRepository lobbyRepository;
    private final ConcurrentHashMap<String, GameLoop> gameLoops = new ConcurrentHashMap<>();
    private CopyOnWriteArraySet<ParticipantHandler> lobbyParticipantClientHandlers;

    @Autowired
    public LobbyManager(LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    public void startGameLoop(Lobby lobby) {
        ClientHandlersRepository clientHandlersRepository = ClientHandlersRepository.getClientHandlersRepository();
        ArrayList<Integer> participantIds = new ArrayList<>();
//        ArrayList<Integer> playerIds = lobby.getGameState().getPlayers().stream().map(Player::getPlayerId).collect(Collectors.toCollection(ArrayList::new));
//        ArrayList<Integer> spectatorIds = (ArrayList<Integer>) lobby.getObserverIds();
//        participantIds.addAll(playerIds);
//        participantIds.addAll(spectatorIds);
        this.lobbyParticipantClientHandlers = clientHandlersRepository.getLobbyParticipants().get(lobby.getLobbyId());
        
//        CopyOnWriteArraySet<ParticipantHandler> currentParticipantClientHandlers = lobbyParticipantClientHandlers.stream()
//                .filter(participantHandler -> participantIds.stream()
//                .anyMatch(participantId -> Objects.equals(participantId, participantHandler.getClientAck().getClientId())))
//                .collect(Collectors.toCollection(CopyOnWriteArraySet::new));

        int lobbyId = lobby.getLobbyId();
        if (lobbyId != -1) {
            lobbyRepository.addLobby(lobby);
            GameLoop gameLoop = new GameLoop(lobby, lobbyParticipantClientHandlers);
            gameLoops.put(String.valueOf(lobbyId), gameLoop);
            gameLoop.start();
        }
    }

    // now idl until it's resumed and must be notified to resume the Game loop via .notify
    public void pauseGameLoop(Lobby lobby) throws InterruptedException {
        int lobbyId = lobby.getLobbyId();
        var gameLoop = gameLoops.get(String.valueOf(lobbyId));
        gameLoop.wait();
    }

    // now awake again and continues
    public void resumeGameLoop(Lobby lobby) throws InterruptedException {
        int lobbyId = lobby.getLobbyId();
        var gameLoop = gameLoops.get(String.valueOf(lobbyId));
        gameLoop.notify();
    }
}
