package com.oasisstudios.camelupadmin.dto;

import lombok.Data;

import javax.annotation.processing.Generated;


/**
 * rollDice
 * <p>
 * This packet is sent from the client to the server when a player decides to roll the dice during their turn. It triggers the server-side dice roll event.
 */
@Generated("jsonschema2pojo")
@Data
public class RollDice {


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

    public static class RollDiceBuilder
        extends RollDice.RollDiceBuilderBase<RollDice> {


        public RollDiceBuilder() {
            super();
        }

    }

    public static abstract class RollDiceBuilderBase<T extends RollDice> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public RollDiceBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(RollDice.RollDiceBuilder.class)) {
                this.instance = ((T) new RollDice());
            }
        }

        public T build() {
            T result;
            result = this.instance;
            this.instance = null;
            return result;
        }

    }

}
