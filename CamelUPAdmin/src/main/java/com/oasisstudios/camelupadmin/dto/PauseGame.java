package com.oasisstudios.camelupadmin.dto;
import lombok.Data;

/**
 * The type Pause game.
 */
@Data
public class PauseGame {
    private int lobbyId;

    /**
     * Instantiates a new Pause game.
     *
     * @param lobbyId the lobby id
     */
    public PauseGame(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    /**
     * Instantiates a new Pause game.
     */
    PauseGame() {}

    /**
     * Builder pause game . pause game builder base.
     *
     * @return the pause game . pause game builder base
     */
    public static PauseGameBuilderBase builder() {
        return new PauseGameBuilder();
    }


    /**
     * The type Pause game builder.
     */
    public static class PauseGameBuilder
            extends PauseGameBuilderBase<PauseGame> {

        /**
         * Instantiates a new Pause game builder.
         */
        public PauseGameBuilder() {
            super();
        }

    }

    /**
     * The type Pause game builder base.
     *
     * @param <T>  the type parameter
     */
    public static abstract class PauseGameBuilderBase<T extends PauseGame> {

        
        protected T instance;


        /**
         * Instantiates a new Pause game builder base.
         */
        @SuppressWarnings("unchecked")
        public PauseGameBuilderBase() {
            // Skip initialization when called from subclass
            if (this.getClass().equals(PauseGameBuilder.class)) {
                this.instance = ((T) new PauseGame());
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
    
