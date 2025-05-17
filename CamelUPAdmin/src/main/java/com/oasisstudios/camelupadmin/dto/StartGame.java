package com.oasisstudios.camelupadmin.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

/**
 * The type Create game.
 */
@Data
public class  StartGame implements Serializable {
    
    @SerializedName("lobbyId")
    private int lobbyId;

    /**
     * Instantiates a new Create Game.
     *
     * @param lobbyId the lobby id
     */
    public StartGame(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    /**
     * Instantiates a new Start game.
     */
    StartGame(){}

    /**
     * Builder start game . start game builder base.
     *
     * @return the start game . start game builder base
     */
    public static StartGameBuilderBase builder() {
        return new StartGameBuilder();
    }

    /**
     * The type Start game builder.
     */
    public static class StartGameBuilder
            extends StartGameBuilderBase<StartGame> {
        /**
         * Instantiates a new Start game builder.
         */
        public StartGameBuilder() {
            super();
        }

    }

    /**
     * The type Start game builder base.
     *
     * @param <T> the type parameter
     */
    public static abstract class StartGameBuilderBase<T extends StartGame> {

        /**
         * The Instance.
         */
        protected T instance;

        /**
         * Instantiates a new Start game builder base.
         */
        @SuppressWarnings("unchecked")
        public StartGameBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(StartGameBuilder.class)) {
                this.instance = ((T) new StartGame());
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
