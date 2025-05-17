package com.oasisstudios.camelupadmin.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

import java.util.LinkedHashSet;

@Builder
@Data
public class GameEnd {

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
