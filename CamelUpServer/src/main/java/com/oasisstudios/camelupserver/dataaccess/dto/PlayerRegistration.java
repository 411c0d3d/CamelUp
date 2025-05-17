package com.oasisstudios.camelupserver.dataaccess.dto;

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
     * Instantiates a new Player registration.
     *
     * @param playerName the player name
     */
    public PlayerRegistration(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Instantiates a new Player registration.
     */
    public PlayerRegistration(){}
    /**
     * The name of the player.
     * (Required)
     */
    @SerializedName("playerName")
    @Expose
    @NotNull
    private String playerName;

    /**
     * Builder player registration . player registration builder base.
     *
     * @return the player registration . player registration builder base
     */
    public static PlayerRegistration.PlayerRegistrationBuilderBase builder() {
        return new PlayerRegistration.PlayerRegistrationBuilder();
    }

    /**
     * The name of the player.
     * (Required)
     *
     * @return the player name
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

    /**
     * The type Player registration builder.
     */
    public static class PlayerRegistrationBuilder
        extends PlayerRegistration.PlayerRegistrationBuilderBase<PlayerRegistration> {


        /**
         * Instantiates a new Player registration builder.
         */
        public PlayerRegistrationBuilder() {
            super();
        }

    }

    /**
     * The type Player registration builder base.
     *
     * @param <T> the type parameter
     */
    public static abstract class PlayerRegistrationBuilderBase<T extends PlayerRegistration> {

        /**
         * The Instance.
         */
        protected T instance;

        /**
         * Instantiates a new Player registration builder base.
         */
        @SuppressWarnings("unchecked")
        public PlayerRegistrationBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(PlayerRegistration.PlayerRegistrationBuilder.class)) {
                this.instance = ((T) new PlayerRegistration());
            }
        }

        /**
         * Build t.
         *
         * @return the t
         */
        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        /**
         * With player name player registration . player registration builder base.
         *
         * @param playerName the player name
         * @return the player registration . player registration builder base
         */
        public PlayerRegistration.PlayerRegistrationBuilderBase withPlayerName(String playerName) {
            ((PlayerRegistration) this.instance).playerName = playerName;
            return this;
        }

    }

}
