package com.oasisstudios.camelupadmin.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Pending player list.
 */
@Data
public class PendingPlayerList {

    @SerializedName("lobbyId")
    private int lobbyId;

    @SerializedName("players")
    private List<Player> players;

    PendingPlayerList() {
    }

    /**
     * Instantiates a new Pending player list.
     *
     * @param players the players
     */
    public PendingPlayerList(ArrayList<Player> players) {
        this.players = players;
    }

    public static PendingPlayerListBuilderBase builder() {
        return new PendingPlayerListBuilder();
    }

    public static class PendingPlayerListBuilder extends PendingPlayerListBuilderBase<PendingPlayerList> {

        public PendingPlayerListBuilder() {
            super();
        }
    }

    public static abstract class PendingPlayerListBuilderBase<T extends PendingPlayerList> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public PendingPlayerListBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(PendingPlayerList.PendingPlayerListBuilder.class)) {
                this.instance = ((T) new PendingPlayerList());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        public PendingPlayerListBuilderBase withPlayers(List<Player> players) {
            ((PendingPlayerList) this.instance).players = players;
            return this;
        }
    }

}
