package com.oasisstudios.camelupserver.dataaccess.dto;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


/**
 * boardSpace
 * <p>
 * A specific space on the board or the finish line
 */
@Generated("jsonschema2pojo")
@Data
public class BoardSpace {
    /**
     * Instantiates a new Board space.
     *
     * @param spaceId    the space id
     * @param camelIds   the camel ids
     * @param playerCard the player card
     */
    public BoardSpace(Integer spaceId, List<Integer> camelIds, PlayerCard playerCard) {
        this.spaceId = spaceId;
        this.camelIds = camelIds;
        this.playerCard = playerCard;
    }
    public BoardSpace(){}
    /**
     * The unique identifier of the space. These start at 0 and are numbered consecutively
     * (Required)
     */
    @SerializedName("spaceId")
    @Expose
    @NotNull
    private Integer spaceId;
    /**
     * The ids of the camels that are on the space. The first camel in the list is the bottommost camel and the last one is the topmost.
     */
    @SerializedName("camelIds")
    @Expose
    @Size(min = 0)
    @Valid
    private List<Integer> camelIds = new ArrayList<>();
    /**
     * A spectator card that is on this space
     */
    @SerializedName("playerCard")
    @Expose
    @Valid
    private PlayerCard playerCard;

    /**
     * Builder board space . board space builder base.
     *
     * @return the board space . board space builder base
     */
    public static BoardSpace.BoardSpaceBuilderBase builder() {
        return new BoardSpace.BoardSpaceBuilder();
    }

    /**
     * The unique identifier of the space. These start at 0 and are numbered consecutively
     * (Required)
     *
     * @return the space id
     */
    public Integer getSpaceId() {
        return spaceId;
    }

    /**
     * The ids of the camels that are on the space. The first camel in the list is the bottommost camel and the last one is the topmost.
     *
     * @return the camel ids
     */
    public List<Integer> getCamelIds() {
        return camelIds;
    }

    /**
     * A spectator card that is on this space
     *
     * @return the player card
     */
    public PlayerCard getPlayerCard() {
        return playerCard;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(BoardSpace.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("spaceId");
        sb.append('=');
        sb.append(((this.spaceId == null) ? "<null>" : this.spaceId));
        sb.append(',');
        sb.append("camelIds");
        sb.append('=');
        sb.append(((this.camelIds == null) ? "<null>" : this.camelIds));
        sb.append(',');
        sb.append("playerCard");
        sb.append('=');
        sb.append(((this.playerCard == null) ? "<null>" : this.playerCard));
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
        result = ((result * 31) + ((this.camelIds == null) ? 0 : this.camelIds.hashCode()));
        result = ((result * 31) + ((this.spaceId == null) ? 0 : this.spaceId.hashCode()));
        result = ((result * 31) + ((this.playerCard == null) ? 0 : this.playerCard.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BoardSpace) == false) {
            return false;
        }
        BoardSpace rhs = ((BoardSpace) other);
        return ((((this.camelIds == rhs.camelIds) || ((this.camelIds != null) && this.camelIds.equals(rhs.camelIds))) && ((this.spaceId == rhs.spaceId) || ((this.spaceId != null) && this.spaceId.equals(rhs.spaceId)))) && ((this.playerCard == rhs.playerCard) || ((this.playerCard != null) && this.playerCard.equals(rhs.playerCard))));
    }

    /**
     * The type Board space builder.
     */
    public static class BoardSpaceBuilder
        extends BoardSpace.BoardSpaceBuilderBase<BoardSpace> {


        /**
         * Instantiates a new Board space builder.
         */
        public BoardSpaceBuilder() {
            super();
        }

    }

    /**
     * The type Board space builder base.
     *
     * @param <T> the type parameter
     */
    public static abstract class BoardSpaceBuilderBase<T extends BoardSpace> {

        /**
         * The Instance.
         */
        protected T instance;

        /**
         * Instantiates a new Board space builder base.
         */
        @SuppressWarnings("unchecked")
        public BoardSpaceBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(BoardSpace.BoardSpaceBuilder.class)) {
                this.instance = ((T) new BoardSpace());
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
         * With space id board space . board space builder base.
         *
         * @param spaceId the space id
         * @return the board space . board space builder base
         */
        public BoardSpace.BoardSpaceBuilderBase withSpaceId(Integer spaceId) {
            ((BoardSpace) this.instance).spaceId = spaceId;
            return this;
        }

        /**
         * With camel ids board space . board space builder base.
         *
         * @param camelIds the camel ids
         * @return the board space . board space builder base
         */
        public BoardSpace.BoardSpaceBuilderBase withCamelIds(List<Integer> camelIds) {
            ((BoardSpace) this.instance).camelIds = camelIds;
            return this;
        }

        /**
         * With player card board space . board space builder base.
         *
         * @param playerCard the player card
         * @return the board space . board space builder base
         */
        public BoardSpace.BoardSpaceBuilderBase withPlayerCard(PlayerCard playerCard) {
            ((BoardSpace) this.instance).playerCard = playerCard;
            return this;
        }

    }

}
