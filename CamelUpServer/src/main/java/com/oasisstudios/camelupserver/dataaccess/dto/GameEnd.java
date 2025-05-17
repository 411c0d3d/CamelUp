package com.oasisstudios.camelupserver.dataaccess.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

import java.util.LinkedHashSet;

/**
 * The type Game end.
 */
@Builder
@Data
public class GameEnd {

    /**
     * Instantiates a new Game end.
     *
     * @param lobby       the lobby
     * @param leaderboard the leaderboard
     */
    public GameEnd(Lobby lobby, LinkedHashSet<Integer> leaderboard) {
        this.lobby = lobby;
        this.leaderboard = leaderboard;
    }

    /**
     * The last state of the lobby
     */
    @SerializedName("lobby")
    @Expose
    @NotNull
    private Lobby lobby;

    /**
     * List of player IDs.
     * Lowest index has the highest score.
     */
    @SerializedName("leaderboard")
    @Expose
    @NotNull
    private LinkedHashSet<Integer> leaderboard;
}
