package cameldown.camelup.interfacedefinition.game.v3;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


/**
 * boardSpace
 * <p>
 * A specific space on the board or the finish line
 */
@Generated("jsonschema2pojo")
public class BoardSpace {

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
    private Set<Integer> camelIds = new LinkedHashSet<Integer>();
    /**
     * A spectator card that is on this space
     */
    @SerializedName("playerCard")
    @Expose
    @Valid
    private PlayerCard playerCard;

    public static BoardSpace.BoardSpaceBuilderBase builder() {
        return new BoardSpace.BoardSpaceBuilder();
    }

    /**
     * The unique identifier of the space. These start at 0 and are numbered consecutively
     * (Required)
     */
    public Integer getSpaceId() {
        return spaceId;
    }

    /**
     * The ids of the camels that are on the space. The first camel in the list is the bottommost camel and the last one is the topmost.
     */
    public Set<Integer> getCamelIds() {
        return camelIds;
    }

    /**
     * A spectator card that is on this space
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

    public static class BoardSpaceBuilder
        extends BoardSpace.BoardSpaceBuilderBase<BoardSpace> {


        public BoardSpaceBuilder() {
            super();
        }

    }

    public static abstract class BoardSpaceBuilderBase<T extends BoardSpace> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public BoardSpaceBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(BoardSpace.BoardSpaceBuilder.class)) {
                this.instance = ((T) new BoardSpace());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        public BoardSpace.BoardSpaceBuilderBase withSpaceId(Integer spaceId) {
            ((BoardSpace) this.instance).spaceId = spaceId;
            return this;
        }

        public BoardSpace.BoardSpaceBuilderBase withCamelIds(Set<Integer> camelIds) {
            ((BoardSpace) this.instance).camelIds = camelIds;
            return this;
        }

        public BoardSpace.BoardSpaceBuilderBase withPlayerCard(PlayerCard playerCard) {
            ((BoardSpace) this.instance).playerCard = playerCard;
            return this;
        }

    }

}
