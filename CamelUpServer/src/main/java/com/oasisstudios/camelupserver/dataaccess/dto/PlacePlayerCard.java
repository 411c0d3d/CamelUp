package com.oasisstudios.camelupserver.dataaccess.dto;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oasisstudios.camelupserver.dataaccess.IPlayerMove;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * placePlayerCard
 * <p>
 * The packet that is sent from the player client to the server to place a spectator tile.
 */
@Generated("jsonschema2pojo")
@Data
public class PlacePlayerCard implements IPlayerMove {
    /**
     * Instantiates a new Place player card.
     *
     * @param spaceId         the space id
     * @param movingDirection the moving direction
     */
    public PlacePlayerCard(Integer spaceId, int movingDirection) {
        this.spaceId = spaceId;
        this.movingDirection = movingDirection;
    }
    
    /**
     * Instantiates a new Place player card.
     */
    public PlacePlayerCard(){}
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

    /**
     * Builder place player card . place player card builder base.
     *
     * @return the place player card . place player card builder base
     */
    public static PlacePlayerCard.PlacePlayerCardBuilderBase builder() {
        return new PlacePlayerCard.PlacePlayerCardBuilder();
    }

    /**
     * This is the ID of the field where the spectator tile should be placed.
     * (Required)
     *
     * @return the space id
     */
    public Integer getSpaceId() {
        return spaceId;
    }

    /**
     * This specifies whether the camel (herd) should move one space forward (+1) or one space backward (-1) when entering the field.
     * (Required)
     *
     * @return the moving direction
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


    /**
     * The type Place player card builder.
     */
    public static class PlacePlayerCardBuilder
        extends PlacePlayerCard.PlacePlayerCardBuilderBase<PlacePlayerCard> {


        /**
         * Instantiates a new Place player card builder.
         */
        public PlacePlayerCardBuilder() {
            super();
        }

    }

    /**
     * The type Place player card builder base.
     *
     * @param <T> the type parameter
     */
    public static abstract class PlacePlayerCardBuilderBase<T extends PlacePlayerCard> {

        /**
         * The Instance.
         */
        protected T instance;

        /**
         * Instantiates a new Place player card builder base.
         */
        @SuppressWarnings("unchecked")
        public PlacePlayerCardBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(PlacePlayerCard.PlacePlayerCardBuilder.class)) {
                this.instance = ((T) new PlacePlayerCard());
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
         * With space id place player card . place player card builder base.
         *
         * @param spaceId the space id
         * @return the place player card . place player card builder base
         */
        public PlacePlayerCard.PlacePlayerCardBuilderBase withSpaceId(Integer spaceId) {
            ((PlacePlayerCard) this.instance).spaceId = spaceId;
            return this;
        }

        /**
         * With moving direction place player card . place player card builder base.
         *
         * @param movingDirection the moving direction
         * @return the place player card . place player card builder base
         */
        public PlacePlayerCard.PlacePlayerCardBuilderBase withMovingDirection(int movingDirection) {
            if (movingDirection != 1 && movingDirection != -1) {
                throw new IllegalArgumentException("movingDirection is only valid for 1 or -1");
            }
            ((PlacePlayerCard) this.instance).movingDirection = movingDirection;
            return this;
        }

    }

}
