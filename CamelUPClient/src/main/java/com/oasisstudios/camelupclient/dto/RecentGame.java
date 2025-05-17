package com.oasisstudios.camelupclient.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;


/**
 * recentGame
 * <p>
 * A specific game that has ended.
 */
@Generated("jsonschema2pojo")
@Data
public class RecentGame {

    /**
     * gameState
     * <p>
     * The current state of the game, which is only sent by the server during an active game when updates occur.
     * (Required)
     */
    @SerializedName("lobby")
    @Expose
    @Valid
    @NotNull
    private Lobby lobby;
    /**
     * A list of player IDs representing the order of the leaderboard. The first player in the list is the winner, and so on.
     * (Required)
     */
    @SerializedName("leaderboard")
    @Expose
    @Size(min = 0)
    @Valid
    @NotNull
    private List<Integer> leaderboard = new ArrayList<Integer>();

    public static RecentGame.LobbyBuilderBase builder() {
        return new RecentGame.LobbyBuilder();
    }

    /**
     * gameState
     * <p>
     * The current state of the game, which is only sent by the server during an active game when updates occur.
     * (Required)
     */
    public Lobby getLobby() {
        return lobby;
    }

    /**
     * A list of player IDs representing the order of the leaderboard. The first player in the list is the winner, and so on.
     * (Required)
     */
    public List<Integer> getLeaderboard() {
        return leaderboard;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(RecentGame.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("gameState");
        sb.append('=');
        sb.append(((this.lobby == null) ? "<null>" : this.lobby));
        sb.append(',');
        sb.append("leaderboard");
        sb.append('=');
        sb.append(((this.leaderboard == null) ? "<null>" : this.leaderboard));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result * 31) + ((this.lobby == null) ? 0 : this.lobby.hashCode()));
        result = ((result * 31) + ((this.leaderboard == null) ? 0 : this.leaderboard.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RecentGame) == false) {
            return false;
        }
        RecentGame rhs = ((RecentGame) other);
        return (((this.lobby == rhs.lobby) || ((this.lobby != null) && this.lobby.equals(rhs.lobby))) && ((this.leaderboard == rhs.leaderboard) || ((this.leaderboard != null) && this.leaderboard.equals(rhs.leaderboard))));
    }

    public static class LobbyBuilder
        extends RecentGame.LobbyBuilderBase<RecentGame> {


        public LobbyBuilder() {
            super();
        }

    }

    public static abstract class LobbyBuilderBase<T extends RecentGame> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public LobbyBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(RecentGame.LobbyBuilder.class)) {
                this.instance = ((T) new RecentGame());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        public RecentGame.LobbyBuilderBase withLobby(Lobby lobby) {
            ((RecentGame) this.instance).lobby = lobby;
            return this;
        }

        public RecentGame.LobbyBuilderBase withLeaderboard(List<Integer> leaderboard) {
            ((RecentGame) this.instance).leaderboard = leaderboard;
            return this;
        }

    }
}
