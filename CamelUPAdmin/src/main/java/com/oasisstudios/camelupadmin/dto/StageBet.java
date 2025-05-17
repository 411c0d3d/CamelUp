package com.oasisstudios.camelupadmin.dto;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * stageBet
 * <p>
 * During a round or stage, a player can place a bet on the round victory of camels using a betting slip. The JSON packet stageBet is used by the player client to place a round bet on a specific camel.
 */
@Generated("jsonschema2pojo")
@Data
public class StageBet {

    /**
     * The ID of the camel on which the bet is to be placed.
     * (Required)
     */
    @SerializedName("camelId")
    @Expose
    @NotNull
    private Integer camelId;

    public static StageBet.StageBetBuilderBase builder() {
        return new StageBet.StageBetBuilder();
    }

    /**
     * The ID of the camel on which the bet is to be placed.
     * (Required)
     */
    public Integer getCamelId() {
        return camelId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StageBet.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("camelId");
        sb.append('=');
        sb.append(((this.camelId == null) ? "<null>" : this.camelId));
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
        result = ((result * 31) + ((this.camelId == null) ? 0 : this.camelId.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof StageBet) == false) {
            return false;
        }
        StageBet rhs = ((StageBet) other);
        return ((this.camelId == rhs.camelId) || ((this.camelId != null) && this.camelId.equals(rhs.camelId)));
    }

    public static class StageBetBuilder
        extends StageBet.StageBetBuilderBase<StageBet> {


        public StageBetBuilder() {
            super();
        }

    }

    public static abstract class StageBetBuilderBase<T extends StageBet> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public StageBetBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(StageBet.StageBetBuilder.class)) {
                this.instance = ((T) new StageBet());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        public StageBet.StageBetBuilderBase withCamelId(Integer camelId) {
            ((StageBet) this.instance).camelId = camelId;
            return this;
        }

    }

}
