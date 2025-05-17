package com.oasisstudios.camelupclient.dto;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * The type Betting card.
 */
@Generated("jsonschema2pojo")
@Data
public class BettingCard {
    /**
     * Instantiates a new Betting card.
     *
     * @param camelId the camel id
     * @param worth   the worth
     */
    public BettingCard(Integer camelId, Integer worth) {
        this.camelId = camelId;
        this.worth = worth;
    }
    public BettingCard(){}
    /**
     * The camelId is the id of the camel for which the betting cards are.
     * (Required)
     */
    @SerializedName("camelId")
    @Expose
    @NotNull
    private Integer camelId;
    /**
     * The worth of a betting card.
     * (Required)
     */
    @SerializedName("worth")
    @Expose
    @DecimalMin("0")
    @NotNull
    private Integer worth;

    /**
     * Builder betting card . betting card builder base.
     *
     * @return the betting card . betting card builder base
     */
    public static BettingCard.BettingCardBuilderBase builder() {
        return new BettingCard.BettingCardBuilder();
    }

    /**
     * The camelId is the id of the camel for which the betting cards are.
     * (Required)
     *
     * @return the camel id
     */
    public Integer getCamelId() {
        return camelId;
    }

    /**
     * The worth of betting cards that are left.
     * (Required)
     *
     * @return the worth
     */
    public Integer getWorth() {
        return worth;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(BettingCard.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("camelId");
        sb.append('=');
        sb.append(((this.camelId == null) ? "<null>" : this.camelId));
        sb.append(',');
        sb.append("worth");
        sb.append('=');
        sb.append(((this.worth == null) ? "<null>" : this.worth));
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
        result = ((result * 31) + ((this.worth == null) ? 0 : this.worth.hashCode()));
        result = ((result * 31) + ((this.camelId == null) ? 0 : this.camelId.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BettingCard) == false) {
            return false;
        }
        BettingCard rhs = ((BettingCard) other);
        return (((this.worth == rhs.worth) || ((this.worth != null) && this.worth.equals(rhs.worth))) && ((this.camelId == rhs.camelId) || ((this.camelId != null) && this.camelId.equals(rhs.camelId))));
    }

    /**
     * The type Betting card builder.
     */
    public static class BettingCardBuilder
            extends BettingCard.BettingCardBuilderBase<BettingCard> {


        /**
         * Instantiates a new Betting card builder.
         */
        public BettingCardBuilder() {
            super();
        }

    }

    /**
     * The type Betting card builder base.
     *
     * @param <T> the type parameter
     */
    public static abstract class BettingCardBuilderBase<T extends BettingCard> {

        /**
         * The Instance.
         */
        protected T instance;

        /**
         * Instantiates a new Betting card builder base.
         */
        @SuppressWarnings("unchecked")
        public BettingCardBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(BettingCard.BettingCardBuilder.class)) {
                this.instance = ((T) new BettingCard());
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
         * With camel id betting card . betting card builder base.
         *
         * @param camelId the camel id
         * @return the betting card . betting card builder base
         */
        public BettingCard.BettingCardBuilderBase withCamelId(Integer camelId) {
            ((BettingCard) this.instance).camelId = camelId;
            return this;
        }

        /**
         * Withworth betting card . betting card builder base.
         *
         * @param worth the worth
         * @return the betting card . betting card builder base
         */
        public BettingCard.BettingCardBuilderBase withworth(Integer worth) {
            ((BettingCard) this.instance).worth = worth;
            return this;
        }

    }

}
