package com.oasisstudios.camelupserver.dataaccess.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.ArrayList;

/**
 * The type Pending player list.
 */
@Data
public class PendingPlayerList {
    
    @SerializedName("lobbyId")
    private ArrayList<Player> players;

    /**
     * Instantiates a new Pending player list.
     *
     * @param players the players
     */
    public PendingPlayerList(ArrayList<Player> players) {
        this.players = players;
    }
}
