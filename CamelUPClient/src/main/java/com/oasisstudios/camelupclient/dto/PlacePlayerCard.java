package com.oasisstudios.camelupclient.dto;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * placePlayerCard
 * <p>
 * The packet that is sent from the player client to the server to place a spectator tile.
 */
@Generated("jsonschema2pojo")
@Data
public class PlacePlayerCard {

    /**
     * This is the ID of the field where the spectator tile should be placed.
     * (Required)
     */
    @SerializedName("spaceId")
    @Expose
    @NotNull
    private Integer spaceId;
    /**
     * This specifies whether the camel (herd) should move one space forward (+1) or one space backward (-1) when entering the field.
     * (Required)
     */
    @SerializedName("movingDirection")
    @Expose
    @NotNull
    private int movingDirection;

    public static PlacePlayerCard.PlacePlayerCardBuilderBase builder() {
        return new PlacePlayerCard.PlacePlayerCardBuilder();
    }

    /**
     * This is the ID of the field where the spectator tile should be placed.
     * (Required)
     */
    public Integer getSpaceId() {
        return spaceId;
    }

    /**
     * This specifies whether the camel (herd) should move one space forward (+1) or one space backward (-1) when entering the field.
     * (Required)
     */
    public int getMovingDirection() {
        return movingDirection;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PlacePlayerCard.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("spaceId");
        sb.append('=');
        sb.append(((this.spaceId == null) ? "<null>" : this.spaceId));
        sb.append(',');
        sb.append("movingDirection");
        sb.append('=');
        sb.append(this.movingDirection);
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
        result = ((result * 31) + ((this.spaceId == null) ? 0 : this.spaceId.hashCode()));
        result = ((result * 31) + ((Integer)this.movingDirection).hashCode());
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PlacePlayerCard) == false) {
            return false;
        }
        PlacePlayerCard rhs = ((PlacePlayerCard) other);
        return (((this.spaceId == rhs.spaceId) || ((this.spaceId != null) && this.spaceId.equals(rhs.spaceId))) && (this.movingDirection == rhs.movingDirection));
    }


    public static class PlacePlayerCardBuilder
        extends PlacePlayerCard.PlacePlayerCardBuilderBase<PlacePlayerCard> {


        public PlacePlayerCardBuilder() {
            super();
        }

    }

    public static abstract class PlacePlayerCardBuilderBase<T extends PlacePlayerCard> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public PlacePlayerCardBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(PlacePlayerCard.PlacePlayerCardBuilder.class)) {
                this.instance = ((T) new PlacePlayerCard());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        public PlacePlayerCard.PlacePlayerCardBuilderBase withSpaceId(Integer spaceId) {
            ((PlacePlayerCard) this.instance).spaceId = spaceId;
            return this;
        }

        public PlacePlayerCard.PlacePlayerCardBuilderBase withMovingDirection(int movingDirection) {
            if (movingDirection != 1 && movingDirection != -1) {
                throw new IllegalArgumentException("movingDirection is only valid for 1 or -1");
            }
            ((PlacePlayerCard) this.instance).movingDirection = movingDirection;
            return this;
        }

    }

}
