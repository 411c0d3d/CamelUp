package com.oasisstudios.camelupadmin.model;

import com.oasisstudios.camelupadmin.dto.Lobby;
import com.oasisstudios.camelupadmin.dto.Player;

import javafx.beans.property.*;

import lombok.Data;
import lombok.Getter;

import java.util.List;

/**
 * The type Lobby.
 */
@Data
public class LobbyModel {

    @Getter
    private IntegerProperty lobbyId = new SimpleIntegerProperty();
    @Getter
    private StringProperty name = new SimpleStringProperty();
    @Getter
    private StringProperty status = new SimpleStringProperty();
    @Getter
    private IntegerProperty playerCount = new SimpleIntegerProperty();
    @Getter
    private IntegerProperty spectatorCount = new SimpleIntegerProperty();

    /**
     * Instantiates a new Lobby.
     *
     * @param lobbyDTO the dto
     */
    public LobbyModel(Lobby lobbyDTO) {
        if (lobbyDTO != null) {
            List<Player> player = lobbyDTO.getGameState().getPlayers();
            this.lobbyId.set(lobbyDTO.getLobbyId());
            this.name.set(lobbyDTO.getName());
            this.status.set(lobbyDTO.getGameState().getGamePhase().toString());
            this.playerCount.set(player.size());
            this.spectatorCount.set(lobbyDTO.getObserverIds().size());
        }

    }
}

