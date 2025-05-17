package com.oasisstudios.camelupserver.dataaccess.dto;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * A spectator tile that is on this field
 */
@Generated("jsonschema2pojo")
@Data
public class PlayerCard {

    /**
     * Instantiates a new Player card.
     *
     * @param playerId    the player id
     * @param spacesMoved the spaces moved
     */
    public PlayerCard(Integer playerId, Integer spacesMoved) {
        this.playerId = playerId;
        this.spacesMoved = spacesMoved;
    }

    /**
     * Instantiates a new Player card.
     */
    public PlayerCard() {}
    /**
     * The Id of the player who placed this tile
     * (Required)
     */
    @SerializedName("playerId")
    @Expose
    @NotNull
    private Integer playerId;
    /**
     * The number of spaces camels move when they land on this field
     * (Required)
     */
    @SerializedName("spacesMoved")
    @Expose
    @NotNull
    private Integer spacesMoved;

    /**
     * Builder player card . player card builder base.
     *
     * @return the player card . player card builder base
     */
    public static PlayerCard.PlayerCardBuilderBase builder() {
        return new PlayerCard.PlayerCardBuilder();
    }

    /**
     * The Id of the player who placed this tile
     * (Required)
     *
     * @return the player id
     */
    public Integer getPlayerId() {
        return playerId;
    }

    /**
     * The number of spaces camels move when they land on this field
     * (Required)
     *
     * @return the spaces moved
     */
    public Integer getSpacesMoved() {
        return spacesMoved;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PlayerCard.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("playerId");
        sb.append('=');
        sb.append(((this.playerId == null) ? "<null>" : this.playerId));
        sb.append(',');
        sb.append("spacesMoved");
        sb.append('=');
        sb.append(((this.spacesMoved == null) ? "<null>" : this.spacesMoved));
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
        result = ((result * 31) + ((this.spacesMoved == null) ? 0 : this.spacesMoved.hashCode()));
        result = ((result * 31) + ((this.playerId == null) ? 0 : this.playerId.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PlayerCard) == false) {
            return false;
        }
        PlayerCard rhs = ((PlayerCard) other);
        return (((this.spacesMoved == rhs.spacesMoved) || ((this.spacesMoved != null) && this.spacesMoved.equals(rhs.spacesMoved))) && ((this.playerId == rhs.playerId) || ((this.playerId != null) && this.playerId.equals(rhs.playerId))));
    }

    /**
     * The type Player card builder.
     */
    public static class PlayerCardBuilder
        extends PlayerCard.PlayerCardBuilderBase<PlayerCard> {

        /**
         * Instantiates a new Player card builder.
         */
        public PlayerCardBuilder() {
            super();
        }

    }

    /**
     * The type Player card builder base.
     *
     * @param <T> the type parameter
     */
    public static abstract class PlayerCardBuilderBase<T extends PlayerCard> {

        /**
         * The Instance.
         */
        protected T instance;

        /**
         * Instantiates a new Player card builder base.
         */
        @SuppressWarnings("unchecked")
        public PlayerCardBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(PlayerCard.PlayerCardBuilder.class)) {
                this.instance = ((T) new PlayerCard());
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
         * With player id player card . player card builder base.
         *
         * @param playerId the player id
         * @return the player card . player card builder base
         */
        public PlayerCard.PlayerCardBuilderBase withPlayerId(Integer playerId) {
            ((PlayerCard) this.instance).playerId = playerId;
            return this;
        }

        /**
         * With spaces moved player card . player card builder base.
         *
         * @param spacesMoved the spaces moved
         * @return the player card . player card builder base
         */
        public PlayerCard.PlayerCardBuilderBase withSpacesMoved(int spacesMoved) {
            if (spacesMoved != 1 && spacesMoved != -1) {
                throw new IllegalArgumentException("spacesMoved is only valid for 1 or -1");
            }
            ((PlayerCard) this.instance).spacesMoved = spacesMoved;
            return this;
        }

    }



}
