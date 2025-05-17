package com.oasisstudios.camelupclient.dto;


import javax.annotation.processing.Generated;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import javax.annotation.processing.Generated;

/**
 * The type Betting cards.
 */
@Generated("jsonschema2pojo")
@Data
public class BettingCards {
    /**
     * Instantiates a new Betting cards.
     *
     * @param camelId the camel id
     * @param amount  the amount
     */
    public BettingCards(Integer camelId, Integer amount) {
        this.camelId = camelId;
        this.amount = amount;
    }
    public BettingCards(){}
    /**
     * The camelId is the id of the camel for which the betting cards are.
     * (Required)
     */
    @SerializedName("camelId")
    @Expose
    @NotNull
    private Integer camelId;
    /**
     * The amount of betting cards that are left.
     * (Required)
     */
    @SerializedName("amount")
    @Expose
    @DecimalMin("0")
    @NotNull
    private Integer amount;

    /**
     * Builder betting card builder base.
     *
     * @return the betting card builder base
     */
    public static BettingCardBuilderBase builder() {
        return new BettingCardBuilder();
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
     * The amount of betting cards that are left.
     * (Required)
     *
     * @return the amount
     */
    public Integer getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(BettingCards.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("camelId");
        sb.append('=');
        sb.append(((this.camelId == null) ? "<null>" : this.camelId));
        sb.append(',');
        sb.append("amount");
        sb.append('=');
        sb.append(((this.amount == null) ? "<null>" : this.amount));
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
        result = ((result * 31) + ((this.amount == null) ? 0 : this.amount.hashCode()));
        result = ((result * 31) + ((this.camelId == null) ? 0 : this.camelId.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof BettingCards) == false) {
            return false;
        }
        BettingCards rhs = ((BettingCards) other);
        return (((this.amount == rhs.amount) || ((this.amount != null) && this.amount.equals(rhs.amount))) && ((this.camelId == rhs.camelId) || ((this.camelId != null) && this.camelId.equals(rhs.camelId))));
    }

    /**
     * The type Betting card builder.
     */
    public static class BettingCardBuilder
            extends BettingCardBuilderBase<BettingCards> {


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
    public static abstract class BettingCardBuilderBase<T extends BettingCards> {

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
            if (this.getClass().equals(BettingCardBuilder.class)) {
                this.instance = ((T) new BettingCards());
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
         * With camel id betting card builder base.
         *
         * @param camelId the camel id
         * @return the betting card builder base
         */
        public BettingCardBuilderBase withCamelId(Integer camelId) {
            ((BettingCards) this.instance).camelId = camelId;
            return this;
        }

        /**
         * With amount betting card builder base.
         *
         * @param amount the amount
         * @return the betting card builder base
         */
        public BettingCardBuilderBase withAmount(Integer amount) {
            ((BettingCards) this.instance).amount = amount;
            return this;
        }

    }

}
