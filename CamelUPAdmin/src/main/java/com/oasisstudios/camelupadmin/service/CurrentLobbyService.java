package com.oasisstudios.camelupadmin.service;

import com.oasisstudios.camelupadmin.dto.Lobby;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class CurrentLobbyService {
    @Getter
    private Lobby currentLobby;

    public void initializeCurrentLobby() {
        this.currentLobby = new Lobby();
    }
    
    public void updateCurrentLobby(Lobby lobby) {
        this.currentLobby = lobby;
    }
    
}