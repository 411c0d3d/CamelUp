package com.oasisstudios.camelupserver.dataaccess.dto;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.oasisstudios.camelupserver.dataaccess.IPlayerMove;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * finalBet
 * <p>
 * The package sent from the client to the server is transmitted as soon as a player is up and decides to place a bet on which camel is going to win or lose the game.
 */
@Generated("jsonschema2pojo")
@Data
public class FinalBet implements IPlayerMove {
    /**
     * Instantiates a new Final bet.
     *
     * @param isFirst the is first
     * @param id      the id
     */
    public FinalBet(Boolean isFirst, Integer id) {
        this.isFirst = isFirst;
        this.id = id;
    }

    /**
     * Instantiates a new Final bet.
     */
    public FinalBet(){}
    /**
     * Indicates whether the bet was placed on the first or last position. A bet on the 'first place' (true) is a bet that the camel will win the game. A bet on the 'last place' (false) is a bet that the camel will lose the game.
     * (Required)
     */
    @SerializedName("isFirst")
    @Expose
    @NotNull
    private Boolean isFirst;
    /**
     * Indicates which camel the bet was placed on. The value is the ID of the camel that was bet on.
     * (Required)
     */
    @SerializedName("id")
    @Expose
    @NotNull
    private Integer id;

    /**
     * Builder final bet . final bet builder base.
     *
     * @return the final bet . final bet builder base
     */
    public static FinalBet.FinalBetBuilderBase builder() {
        return new FinalBet.FinalBetBuilder();
    }

    /**
     * Indicates whether the bet was placed on the first or last position. A bet on the 'first place' (true) is a bet that the camel will win the game. A bet on the 'last place' (false) is a bet that the camel will lose the game.
     * (Required)
     *
     * @return the is first
     */
    public Boolean getIsFirst() {
        return isFirst;
    }

    /**
     * Indicates which camel the bet was placed on. The value is the ID of the camel that was bet on.
     * (Required)
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(FinalBet.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("isFirst");
        sb.append('=');
        sb.append(((this.isFirst == null) ? "<null>" : this.isFirst));
        sb.append(',');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null) ? "<null>" : this.id));
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
        result = ((result * 31) + ((this.isFirst == null) ? 0 : this.isFirst.hashCode()));
        result = ((result * 31) + ((this.id == null) ? 0 : this.id.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FinalBet) == false) {
            return false;
        }
        FinalBet rhs = ((FinalBet) other);
        return (((this.isFirst == rhs.isFirst) || ((this.isFirst != null) && this.isFirst.equals(rhs.isFirst))) && ((this.id == rhs.id) || ((this.id != null) && this.id.equals(rhs.id))));
    }

    /**
     * The type Final bet builder.
     */
    public static class FinalBetBuilder
        extends FinalBet.FinalBetBuilderBase<FinalBet> {


        /**
         * Instantiates a new Final bet builder.
         */
        public FinalBetBuilder() {
            super();
        }

    }

    /**
     * The type Final bet builder base.
     *
     * @param <T> the type parameter
     */
    public static abstract class FinalBetBuilderBase<T extends FinalBet> {

        /**
         * The Instance.
         */
        protected T instance;

        /**
         * Instantiates a new Final bet builder base.
         */
        @SuppressWarnings("unchecked")
        public FinalBetBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(FinalBet.FinalBetBuilder.class)) {
                this.instance = ((T) new FinalBet());
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
         * With is first final bet . final bet builder base.
         *
         * @param isFirst the is first
         * @return the final bet . final bet builder base
         */
        public FinalBet.FinalBetBuilderBase withIsFirst(Boolean isFirst) {
            ((FinalBet) this.instance).isFirst = isFirst;
            return this;
        }

        /**
         * With id final bet . final bet builder base.
         *
         * @param id the id
         * @return the final bet . final bet builder base
         */
        public FinalBet.FinalBetBuilderBase withId(Integer id) {
            ((FinalBet) this.instance).id = id;
            return this;
        }

    }

}
