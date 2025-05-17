package com.oasisstudios.camelupserver.Bot;

import com.oasisstudios.camelupserver.dataaccess.dto.GameConfigDTO;
import com.oasisstudios.camelupserver.dataaccess.dto.GameStateDTO;

/**
 * CurrentStage handles checking whether the stage is ending.
 */
public class CurrentStage
{
    private GameStateDTO gameState; // Holds the current game state
    private GameConfigDTO gameConfig; // Stores game configuration details

    /**
     * Initializes the stage tracking with game state and configuration.
     */
    public CurrentStage(GameStateDTO gameState, GameConfigDTO gameConfig)
    {
        this.gameState = gameState;
        this.gameConfig = gameConfig;
    }

    /**
     * Checks if the current stage is ending based on how many camels have rolled.
     * A stage is considered near the end if at least 70% of the camels have rolled.
     *
     * @return true if the stage is ending, false otherwise.
     */
    public boolean isEndOfStage()
    {
        return gameConfig.getCamelCount() * 0.7 <= (double) gameState.getRolledDice().size();
    }
}
