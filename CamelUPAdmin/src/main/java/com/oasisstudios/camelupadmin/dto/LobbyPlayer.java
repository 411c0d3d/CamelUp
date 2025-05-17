package com.oasisstudios.camelupadmin.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import javax.annotation.processing.Generated;

/**
 * player
 * <p>
 * A list of player objects that have joined the lobby.
 */
@Generated("jsonschema2pojo")
@Data
public class LobbyPlayer {

    /**
     * Unique identifier for a player.
     * (Required)
     */
    @SerializedName("playerId")
    @Expose
    @NotNull
    private Integer playerId;
    /**
     * Display name of the player.
     * (Required)
     */
    @SerializedName("name")
    @Expose
    @NotNull
    private String name;

    public static LobbyPlayerBuilderBase builder() {
        return new lobbyPlayerBuilder();
    }

    /**
     * Unique identifier for a player.
     * (Required)
     */
    public Integer getPlayerId() {
        return playerId;
    }

    /**
     * Display name of the player.
     * (Required)
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(LobbyPlayer.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("playerId");
        sb.append('=');
        sb.append(((this.playerId == null) ? "<null>" : this.playerId));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null) ? "<null>" : this.name));
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
        result = ((result * 31) + ((this.name == null) ? 0 : this.name.hashCode()));
        result = ((result * 31) + ((this.playerId == null) ? 0 : this.playerId.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LobbyPlayer) == false) {
            return false;
        }
        LobbyPlayer rhs = ((LobbyPlayer) other);
        return (((this.name == rhs.name) || ((this.name != null) && this.name.equals(rhs.name))) && ((this.playerId == rhs.playerId) || ((this.playerId != null) && this.playerId.equals(rhs.playerId))));
    }

    public static class lobbyPlayerBuilder
        extends LobbyPlayerBuilderBase<LobbyPlayer> {

        public lobbyPlayerBuilder() {
            super();
        }

    }

    public static abstract class LobbyPlayerBuilderBase<T extends LobbyPlayer> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public LobbyPlayerBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(lobbyPlayerBuilder.class)) {
                this.instance = ((T) new LobbyPlayer());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        public LobbyPlayerBuilderBase withPlayerId(Integer playerId) {
            ((LobbyPlayer) this.instance).playerId = playerId;
            return this;
        }

        public LobbyPlayerBuilderBase withName(String name) {
            ((LobbyPlayer) this.instance).name = name;
            return this;
        }

    }

}
