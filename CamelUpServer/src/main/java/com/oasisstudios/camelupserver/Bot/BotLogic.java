package com.oasisstudios.camelupserver.Bot;


import com.oasisstudios.camelupserver.dataaccess.dto.GameConfigDTO;
import com.oasisstudios.camelupserver.dataaccess.dto.CamelDTO;
import java.util.List;
import java.util.Random;

/**
 * BotLogic controls the bot's decisions based on the current game state.
 * It determines whether to place bets, roll dice, or take other actions.
 */
public class BotLogic
{
    private final CurrentPhase currentPhase; // Tracks the overall game progress
    private final CurrentStage currentStage; // Tracks the progress of the current stage
    private final BotEvaluation botEvaluation; // Evaluates which camels are leading or trailing
    private final List<CamelDTO> camels; // List of all camels in the game
    private final GameConfigDTO gameConfig; // Stores configuration data such as max rounds
    private final BotActionManager botActionManager; // Executes actions like betting or rolling dice
    private Random random = new Random(); // Random generator for probability checks

    /**
     * Initializes the bot logic and passes the relevant game data.
     */
    public BotLogic(CurrentPhase currentPhase, CurrentStage currentStage, List<CamelDTO> camels, GameConfigDTO gameConfig, BotActionManager botActionManager)
    {
        this.currentPhase = currentPhase;
        this.currentStage = currentStage;
        this.botEvaluation = new BotEvaluation();
        this.camels = camels;
        this.gameConfig = gameConfig;
        this.botActionManager = botActionManager;
    }

    /**
     * Determines the next action based on the current game state.
     */
    public void makeDecision()
    {
        // Identify the leading and trailing camel
        CamelDTO winningCamel = botEvaluation.getWinningCamel(camels, gameConfig);
        CamelDTO losingCamel = botEvaluation.getLosingCamel(camels, gameConfig);

        // TODO: Handle cases where winningCamel or losingCamel is null
        if (winningCamel == null || losingCamel == null)
        {
            return;
        }

        // Check if we are in the final phase of the game
        if (currentPhase.hasReachedThreshold())
        {
            // TODO: Ensure isBetAvailableForCamel() correctly checks for available bets
            if (!botEvaluation.areFinalBetsPlaced(winningCamel, losingCamel))
            {
                if (botActionManager.isBetAvailableForCamel(winningCamel))
                {
                    botActionManager.placeFinalBet(winningCamel);
                }
                else if (botActionManager.isBetAvailableForCamel(losingCamel))
                {
                    botActionManager.placeFinalBet(losingCamel);
                }
            }
        }
        else
        {
            // TODO: Validate currentStage.isEndOfStage() logic
            if (currentStage.isEndOfStage())
            {
                botActionManager.placeStageBet();
            }
            else
            {
                // TODO: Decide where probability check for placing PlayerCard should be handled
                if (botActionManager.shouldPlacePlayerCard())
                {
                    botActionManager.placePlayerCard();
                }
                else
                {
                    botActionManager.rollDice();
                }
            }
        }
    }
}
