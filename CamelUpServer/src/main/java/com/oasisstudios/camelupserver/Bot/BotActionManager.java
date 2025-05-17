package com.oasisstudios.camelupserver.Bot;


import com.oasisstudios.camelupserver.dataaccess.dto.CamelDTO;
import com.oasisstudios.camelupserver.dataaccess.dto.GameConfigDTO;
import com.oasisstudios.camelupserver.domain.model.domainclasses.PlacePlayerCard;
import java.util.List;
import java.util.Random;

/**
 * BotActionManager is responsible for executing game actions such as placing bets,
 * rolling dice, and positioning player cards. It centralizes game logic to keep
 * the main bot logic clean and structured.
 */
public class BotActionManager
{
    private GameConfigDTO gameConfig;
    private List<CamelDTO> camels;
    private Random random = new Random(); // Random instance for decision-making

    /**
     * Initializes the action manager with required game data.
     */
    public BotActionManager(GameConfigDTO gameConfig, List<CamelDTO> camels)
    {
        this.gameConfig = gameConfig;
        this.camels = camels;
    }

    /**
     * Rolls the dice and returns a value between 1 and 3.
     */
    public int rollDice()
    {
        return random.nextInt(3) + 1;
    }

    /**
     * Checks if a bet is available for a given camel.
     * TODO: Implement actual bet availability logic.
     */
    public boolean isBetAvailableForCamel(CamelDTO camel)
    {
        return true; // Placeholder return value
    }

    /**
     * Places a stage bet based on the bot's strategy.
     * TODO: Implement stage bet logic.
     */
    public void placeStageBet()
    {
        // Placeholder for stage bet logic
    }

    /**
     * Places a final bet on the overall game winner.
     * TODO: Implement final bet logic.
     */
    public void placeFinalBet(CamelDTO camel)
    {
        // Placeholder for final bet logic
    }

    /**
     * Determines if a PlayerCard should be placed based on probability.
     * TODO: Remove probability logic from here if it should be handled in BotLogic.
     */
    public boolean shouldPlacePlayerCard()
    {
        return random.nextInt(10) < 9; // 90% chance to place a PlayerCard
    }

    /**
     * Places a PlayerCard in the game.
     * TODO: Ensure placement logic correctly follows game rules.
     */
    public void placePlayerCard()
    {
        CamelDTO leadingCamel = getLeadingCamel();
        if (leadingCamel == null)
        {
            return; // No camel available for reference
        }

        int spaceId = Math.max(1, leadingCamel.getPosition() - 3);
        int movingDirection = 1; // Default value, adjust if needed

        PlacePlayerCard playerCard = new PlacePlayerCard(spaceId, movingDirection);
        // TODO: Implement logic to send PlayerCard action to the game.
    }

    /**
     * Finds the leading camel in the game.
     * TODO: Verify that this correctly determines the leading camel.
     */
    private CamelDTO getLeadingCamel()
    {
        CamelDTO leadingCamel = null;
        int minDistance = Integer.MAX_VALUE;

        for (CamelDTO camel : camels)
        {
            int distance = gameConfig.getNumberOfSpaces() - camel.getPosition();
            if (distance < minDistance)
            {
                minDistance = distance;
                leadingCamel = camel;
            }
        }
        return leadingCamel;
    }
}
