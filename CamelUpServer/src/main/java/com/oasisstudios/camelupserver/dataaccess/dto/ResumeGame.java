package com.oasisstudios.camelupserver.dataaccess.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

/**
 * The type Resume game.
 */
@Data
public class ResumeGame implements Serializable {

    @SerializedName("lobbyId")
    private int lobbyId;

    /**
     * Instantiates a new Create Game.
     *
     * @param lobbyId the lobby id
     */
    public ResumeGame(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    /**
     * Instantiates a new Start game.
     */
    ResumeGame(){}

    /**
     * Builder start game . start game builder base.
     *
     * @return the start game . start game builder base
     */
    public static ResumeGame.ResumeGameBuilderBase builder() {
        return new ResumeGame.ResumeGameBuilder();
    }

    /**
     * The type Start game builder.
     */
    public static class ResumeGameBuilder
            extends ResumeGame.ResumeGameBuilderBase<ResumeGame> {
        /**
         * Instantiates a new Start game builder.
         */
        public ResumeGameBuilder() {
            super();
        }

    }

    /**
     * The type Start game builder base.
     *
     * @param <T>  the type parameter
     */
    public static abstract class ResumeGameBuilderBase<T extends ResumeGame> {

        /**
         * The Instance.
         */
        protected T instance;

        /**
         * Instantiates a new Start game builder base.
         */
        @SuppressWarnings("unchecked")
        public ResumeGameBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(ResumeGame.ResumeGameBuilder.class)) {
                this.instance = ((T) new ResumeGame());
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
