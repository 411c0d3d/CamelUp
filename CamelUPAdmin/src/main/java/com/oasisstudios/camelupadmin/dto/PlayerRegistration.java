package com.oasisstudios.camelupadmin.dto;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * playerRegistration
 * <p>
 * This packet is sent to the server when a player wants to register as a game participant. This can happen as long as the client is not in any lobby. It can also happen when switching from an observer to a participant.
 */
@Generated("jsonschema2pojo")
@Data
public class PlayerRegistration {

    /**
     * The name of the player.
     * (Required)
     */
    @SerializedName("playerName")
    @Expose
    @NotNull
    private String playerName;

    public static PlayerRegistration.PlayerRegistrationBuilderBase builder() {
        return new PlayerRegistration.PlayerRegistrationBuilder();
    }

    /**
     * The name of the player.
     * (Required)
     */
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PlayerRegistration.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("playerName");
        sb.append('=');
        sb.append(((this.playerName == null) ? "<null>" : this.playerName));
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
        result = ((result * 31) + ((this.playerName == null) ? 0 : this.playerName.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PlayerRegistration) == false) {
            return false;
        }
        PlayerRegistration rhs = ((PlayerRegistration) other);
        return ((this.playerName == rhs.playerName) || ((this.playerName != null) && this.playerName.equals(rhs.playerName)));
    }

    public static class PlayerRegistrationBuilder
        extends PlayerRegistration.PlayerRegistrationBuilderBase<PlayerRegistration> {


        public PlayerRegistrationBuilder() {
            super();
        }

    }

    public static abstract class PlayerRegistrationBuilderBase<T extends PlayerRegistration> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public PlayerRegistrationBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(PlayerRegistration.PlayerRegistrationBuilder.class)) {
                this.instance = ((T) new PlayerRegistration());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        public PlayerRegistration.PlayerRegistrationBuilderBase withPlayerName(String playerName) {
            ((PlayerRegistration) this.instance).playerName = playerName;
            return this;
        }

    }

}
