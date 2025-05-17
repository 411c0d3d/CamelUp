package com.oasisstudios.camelupserver.Bot;

import com.oasisstudios.camelupserver.dataaccess.dto.GameConfigDTO;
import com.oasisstudios.camelupserver.dataaccess.dto.GameStateDTO;
import lombok.Getter;

/**
 * Tracks the current phase of the game and checks how far we are into it.
 * The threshold for "late game" can be adjusted dynamically.
 */
public class CurrentPhase
{
    private GameStateDTO gameState; // Holds the current game state
    private GameConfigDTO gameConfig; // Holds game settings like max rounds
    /**
     * -- GETTER --
     *  Returns the current threshold for determining late-game status.
     *
     * @return A decimal value (e.g., 0.7 means "last 30%").
     */
    @Getter
    private double progressThreshold = 0.7; // Default: last 30% of the game

    /**
     * Creates an instance that keeps track of game progress.
     * @param gameState The current state of the game (rounds played, etc.).
     * @param gameConfig The game’s configuration (total rounds, etc.).
     */
    public CurrentPhase(GameStateDTO gameState, GameConfigDTO gameConfig)
    {
        this.gameState = gameState;
        this.gameConfig = gameConfig;
    }

    /**
     * Adjusts the threshold that defines when we consider the game to be in its final phase.
     * @param threshold A value between 0 and 1 (where 1 means the game is fully completed).
     */
    public void setProgressThreshold(double threshold)
    {
        if (threshold >= 0 && threshold <= 1)
        {
            this.progressThreshold = threshold;
        }
    }

    /**
     * Checks if we've reached the set threshold for late-game.
     * @return true if the game has progressed past the defined percentage, false otherwise.
     *
     * TODO: Prevent division by zero if maxTurns is 0.
     */
    public boolean hasReachedThreshold()
    {
        double phaseProgress = (double) gameState.getTurns() / gameConfig.getMaxTurns(); // Get the current game progress
        return phaseProgress >= progressThreshold; // Compare with the set threshold
    }

}
