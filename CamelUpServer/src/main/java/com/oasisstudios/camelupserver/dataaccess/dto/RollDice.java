package com.oasisstudios.camelupserver.dataaccess.dto;

import com.oasisstudios.camelupserver.dataaccess.IPlayerMove;
import lombok.Data;

import javax.annotation.processing.Generated;


/**
 * rollDice
 * <p>
 * This packet is sent from the client to the server when a player decides to roll the dice during their turn. It triggers the server-side dice roll event.
 */
@Generated("jsonschema2pojo")
@Data
public class RollDice implements IPlayerMove {

    /**
     * Instantiates a new Roll dice.
     */
    public RollDice() {}

    /**
     * Builder roll dice . roll dice builder base.
     *
     * @return the roll dice . roll dice builder base
     */
    public static RollDice.RollDiceBuilderBase builder() {
        return new RollDice.RollDiceBuilder();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(RollDice.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RollDice) == false) {
            return false;
        }
        RollDice rhs = ((RollDice) other);
        return true;
    }

    /**
     * The type Roll dice builder.
     */
    public static class RollDiceBuilder
        extends RollDice.RollDiceBuilderBase<RollDice> {


        /**
         * Instantiates a new Roll dice builder.
         */
        public RollDiceBuilder() {
            super();
        }

    }

    /**
     * The type Roll dice builder base.
     *
     * @param <T> the type parameter
     */
    public static abstract class RollDiceBuilderBase<T extends RollDice> {

        /**
         * The Instance.
         */
        protected T instance;

        /**
         * Instantiates a new Roll dice builder base.
         */
        @SuppressWarnings("unchecked")
        public RollDiceBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(RollDice.RollDiceBuilder.class)) {
                this.instance = ((T) new RollDice());
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
    }
}
