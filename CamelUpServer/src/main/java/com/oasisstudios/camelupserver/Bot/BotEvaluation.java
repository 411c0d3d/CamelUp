package com.oasisstudios.camelupserver.Bot;

import com.oasisstudios.camelupserver.dataaccess.dto.GameConfigDTO;
import com.oasisstudios.camelupserver.dataaccess.dto.CamelDTO;

/**
 * BotEvaluation is responsible for analyzing the game state and determining
 * relevant information such as leading and trailing camels, and whether final bets are placed.
 */
class BotEvaluation
{
    /**
     * Checks if final bets have already been placed on the leading or losing camel.
     * @param winningCamel The camel currently leading the race.
     * @param losingCamel The camel currently in last place.
     * @return true if bets have already been placed, otherwise false.
     * TODO: Ensure FinalBets.contains() correctly checks for bet placement.
     */
    public boolean areFinalBetsPlaced(CamelDTO winningCamel, CamelDTO losingCamel)
    {
        return FinalBets.contains(winningCamel.getGoalCard()) || FinalBets.contains(losingCamel.getGoalCard());
    }

    /**
     * Determines the camel closest to the finish line.
     * @param camels List of all camels in the game.
     * @param gameConfig The game configuration containing board details.
     * @return The leading camel.
     * TODO: Verify that getWinningCamel() properly accounts for stacked camels.
     */
    public CamelDTO getWinningCamel(List<CamelDTO> camels, GameConfigDTO gameConfig)
    {
        CamelDTO winningCamel = null;
        int minDistance = Integer.MAX_VALUE;

        for (CamelDTO camel : camels)
        {
            int distance = gameConfig.getSpaces() - camel.getSpaceId(); // Calculate distance to finish line
            if (distance < minDistance)
            {
                minDistance = distance;
                winningCamel = camel; // Store the current leading camel
            }
        }
        return winningCamel;
    }

    /**
     * Determines the camel farthest from the finish line.
     * @param camels List of all camels in the game.
     * @param gameConfig The game configuration containing board details.
     * @return The trailing camel.
     * TODO: Confirm that getLosingCamel() correctly handles tied last-place camels.
     */
    public CamelDTO getLosingCamel(List<CamelDTO> camels, GameConfigDTO gameConfig)
    {
        CamelDTO losingCamel = null;
        int maxDistance = Integer.MIN_VALUE;

        for (CamelDTO camel : camels)
        {
            int distance = gameConfig.getSpaces() - camel.getSpaceId(); // Calculate distance to finish line
            if (distance > maxDistance)
            {
                maxDistance = distance;
                losingCamel = camel; // Store the current last-place camel
            }
        }
        return losingCamel;
    }
}
