package com.oasisstudios.camelupengine.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import javax.annotation.processing.Generated;


/**
 * betCard
 * <p>
 * Ein spezieller Wettschein
 */
@Generated("jsonschema2pojo")
@Data
public class PlayerBettingCard {

    /**
     * Die Id des Kamels für die der Wettscheine gilt
     * (Required)
     */
    @SerializedName("camelId")
    @Expose
    @NotNull
    private Integer camelId;
    /**
     * Der Wert des Wettscheines
     * (Required)
     */
    @SerializedName("worth")
    @Expose
    @DecimalMin("2")
    @NotNull
    private Integer worth;

    public static PlayerBettingCardBuilderBase builder() {
        return new PlayerBettingCardBuilder();
    }

    /**
     * Die Id des Kamels für die der Wettscheine gilt
     * (Required)
     */
    public Integer getCamelId() {
        return camelId;
    }

    /**
     * Der Wert des Wettscheines
     * (Required)
     */
    public Integer getWorth() {
        return worth;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PlayerBettingCard.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        result = ((result * 31) + ((this.camelId == null) ? 0 : this.camelId.hashCode()));
        result = ((result * 31) + ((this.worth == null) ? 0 : this.worth.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PlayerBettingCard) == false) {
            return false;
        }
        PlayerBettingCard rhs = ((PlayerBettingCard) other);
        return (((this.camelId == rhs.camelId) || ((this.camelId != null) && this.camelId.equals(rhs.camelId))) && ((this.worth == rhs.worth) || ((this.worth != null) && this.worth.equals(rhs.worth))));
    }

    public static class PlayerBettingCardBuilder
        extends PlayerBettingCardBuilderBase<PlayerBettingCard> {


        public PlayerBettingCardBuilder() {
            super();
        }

    }

    public static abstract class PlayerBettingCardBuilderBase<T extends PlayerBettingCard> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public PlayerBettingCardBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(PlayerBettingCardBuilder.class)) {
                this.instance = ((T) new PlayerBettingCard());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

        public PlayerBettingCardBuilderBase withCamelId(Integer camelId) {
            ((PlayerBettingCard) this.instance).camelId = camelId;
            return this;
        }

        public PlayerBettingCardBuilderBase withWorth(Integer worth) {
            ((PlayerBettingCard) this.instance).worth = worth;
            return this;
        }

    }

}
