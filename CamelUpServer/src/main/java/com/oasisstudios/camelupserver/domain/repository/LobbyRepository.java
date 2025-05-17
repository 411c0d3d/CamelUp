package com.oasisstudios.camelupserver.domain.repository;

import com.oasisstudios.camelupserver.dataaccess.dto.Lobby;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class LobbyRepository {
    private final ConcurrentHashMap<String, Lobby> activeLobbies;

    public LobbyRepository() {
        this.activeLobbies = new ConcurrentHashMap<>();
    }

    public Lobby getLobby(String lobbyId) {
        return activeLobbies.get(lobbyId);
    }

    public void addLobby(Lobby lobby) {
        activeLobbies.put(String.valueOf(lobby.getLobbyId()), lobby);
    }

    public void removeLobby(String lobbyId) {
        activeLobbies.remove(lobbyId);
    }

    // Can add methods to load and persist game states from/to JSON files
}
