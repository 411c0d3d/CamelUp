package com.oasisstudios.camelupserver.dataaccess.dto;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * The type Rolled dice.
 */
@Generated("jsonschema2pojo")
@Data
public class RolledDice {
    /**
     * Instantiates a new Rolled dice.
     *
     * @param camelId the camel id
     * @param number  the number
     */
    public RolledDice(Integer camelId, Integer number) {
        this.camelId = camelId;
        this.number = number;
    }

    /**
     * Instantiates a new Rolled dice.
     */
    public RolledDice(){}
    /**
     * The Id of the camel for which the dice was rolled.
     * (Required)
     */
    @SerializedName("camelId")
    @Expose
    @NotNull
    private Integer camelId;
    /**
     * The result of the roll
     * (Required)
     */
    @SerializedName("number")
    @Expose
    @DecimalMin("1")
    @NotNull
    private Integer number;

    /**
     * Builder rolled dice . rolled dice builder base.
     *
     * @return the rolled dice . rolled dice builder base
     */
    public static RolledDice.RolledDiceBuilderBase builder() {
        return new RolledDice.RolledDiceBuilder();
    }

    /**
     * The Id of the camel for which the dice was rolled.
     * (Required)
     *
     * @return the camel id
     */
    public Integer getCamelId() {
        return camelId;
    }

    /**
     * The result of the roll
     * (Required)
     *
     * @return the number
     */
    public Integer getNumber() {
        return number;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(RolledDice.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("camelId");
        sb.append('=');
        sb.append(((this.camelId == null) ? "<null>" : this.camelId));
        sb.append(',');
        sb.append("number");
        sb.append('=');
        sb.append(((this.number == null) ? "<null>" : this.number));
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
        result = ((result * 31) + ((this.number == null) ? 0 : this.number.hashCode()));
        result = ((result * 31) + ((this.camelId == null) ? 0 : this.camelId.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RolledDice) == false) {
            return false;
        }
        RolledDice rhs = ((RolledDice) other);
        return (((this.number == rhs.number) || ((this.number != null) && this.number.equals(rhs.number))) && ((this.camelId == rhs.camelId) || ((this.camelId != null) && this.camelId.equals(rhs.camelId))));
    }

    /**
     * The type Rolled dice builder.
     */
    public static class RolledDiceBuilder
        extends RolledDice.RolledDiceBuilderBase<RolledDice> {

        /**
         * Instantiates a new Rolled dice builder.
         */
        public RolledDiceBuilder() {
            super();
        }

    }

    /**
     * The type Rolled dice builder base.
     *
     * @param <T> the type parameter
     */
    public static abstract class RolledDiceBuilderBase<T extends RolledDice> {

        /**
         * The Instance.
         */
        protected T instance;

        /**
         * Instantiates a new Rolled dice builder base.
         */
        @SuppressWarnings("unchecked")
        public RolledDiceBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(RolledDice.RolledDiceBuilder.class)) {
                this.instance = ((T) new RolledDice());
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
         * With camel id rolled dice . rolled dice builder base.
         *
         * @param camelId the camel id
         * @return the rolled dice . rolled dice builder base
         */
        public RolledDice.RolledDiceBuilderBase withCamelId(Integer camelId) {
            ((RolledDice) this.instance).camelId = camelId;
            return this;
        }

        /**
         * With number rolled dice . rolled dice builder base.
         *
         * @param number the number
         * @return the rolled dice . rolled dice builder base
         */
        public RolledDice.RolledDiceBuilderBase withNumber(Integer number) {
            ((RolledDice) this.instance).number = number;
            return this;
        }

    }

}
