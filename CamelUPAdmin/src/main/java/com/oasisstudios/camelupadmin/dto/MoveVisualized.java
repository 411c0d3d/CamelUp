package com.oasisstudios.camelupadmin.dto;

import lombok.Data;

import javax.annotation.processing.Generated;


/**
 * moveVisualized
 * <p>
 * This package is sent from the client to the server as soon as the client has finished visualizing a move. This happens, for example, after a player has rolled the dice, followed by the dice animation, and finally the movement of the affected camels from field A to field B is completed. After all players have sent a moveVisualized package to the server, or after the maximum visualization time has been exceeded, the next player in the game order is granted their turn.
 */
@Generated("jsonschema2pojo")
@Data
public class MoveVisualized {


    public static MoveVisualized.MoveVisualizedBuilderBase builder() {
        return new MoveVisualized.MoveVisualizedBuilder();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MoveVisualized.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        if ((other instanceof MoveVisualized) == false) {
            return false;
        }
        MoveVisualized rhs = ((MoveVisualized) other);
        return true;
    }

    public static class MoveVisualizedBuilder
        extends MoveVisualized.MoveVisualizedBuilderBase<MoveVisualized> {


        public MoveVisualizedBuilder() {
            super();
        }

    }

    public static abstract class MoveVisualizedBuilderBase<T extends MoveVisualized> {

        protected T instance;

        @SuppressWarnings("unchecked")
        public MoveVisualizedBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(MoveVisualized.MoveVisualizedBuilder.class)) {
                this.instance = ((T) new MoveVisualized());
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
