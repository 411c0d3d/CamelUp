package com.oasisstudios.camelupclient.dto;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Generated("jsonschema2pojo")
@Data
public class RolledDice {

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

    public static RolledDice.RolledDiceBuilderBase builder() {
        return new RolledDice.RolledDiceBuilder();
    }

    /**
     * The Id of the camel for which the dice was rolled.
     * (Required)
     */
    public Integer getCamelId() {
        return camelId;
    }

    /**
     * The result of the roll
     * (Required)
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

    public static class RolledDiceBuilder
        extends RolledDice.RolledDiceBuilderBase<RolledDice> {

        public RolledDiceBuilder() {
            super();
        }

    }

    public static abstract class RolledDiceBuilderBase<T extends RolledDice> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public RolledDiceBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(RolledDice.RolledDiceBuilder.class)) {
                this.instance = ((T) new RolledDice());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        public RolledDice.RolledDiceBuilderBase withCamelId(Integer camelId) {
            ((RolledDice) this.instance).camelId = camelId;
            return this;
        }

        public RolledDice.RolledDiceBuilderBase withNumber(Integer number) {
            ((RolledDice) this.instance).number = number;
            return this;
        }

    }

}
