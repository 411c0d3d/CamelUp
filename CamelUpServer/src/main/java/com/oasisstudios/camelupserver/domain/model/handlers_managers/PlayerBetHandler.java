package com.oasisstudios.camelupserver.domain.model.handlers_managers;

import com.oasisstudios.camelupserver.dataaccess.dto.*;
import com.oasisstudios.camelupserver.domain.model.domainclasses.*;

import java.util.NoSuchElementException;

// BettingCardHandler handles the stageBet action sent by a player client.
// Is injected with GameStateDOM.
// BettingCardHandler checks, calculates and creates the requested BettingCardDOM
// In GameStateDOM it updates the BettingCardsDOM amount and insert the BettingCardDOM into the acting players bettingCards List.
public class PlayerBetHandler {
    private GameStateDOM gameStateDOM;

    public PlayerBetHandler(GameStateDOM gameStateDOM) {
        this.gameStateDOM = gameStateDOM;
    }

    public boolean hasBetAmountAvailable(StageBet stageBet) {
        return getCurrentAmountOfBettingCards(stageBet) > 0;
    }

    // Method to get the current number of betting cards for a camel from BettingCardsManager.
    // Checks the amount value if creation is allowed.
    // Decreases and gets the necessary amount value to calculate worth if new BettingCardDOM gets created.
    private int getCurrentAmountOfBettingCards(StageBet stageBetMove) {
        for (BettingCardsDOM bettingCardDeck : this.gameStateDOM.getBettingCards()) {
            if (bettingCardDeck.getCamelId() == stageBetMove.getCamelId()) {
                if (bettingCardDeck.getAmount() > 0) {
                    // Decrease amount in the BettingCardsManager as a new betting card is being created

                    return bettingCardDeck.getAmount();
                } else {
                    throw new IllegalArgumentException(
                            "All possible BettingCardDOM objects have already been created for this camelId: " + stageBetMove.getCamelId());
                }
            }
        }
        throw new NoSuchElementException("No camelId found matching: " + stageBetMove.getCamelId());
    }

    // Method to calculate the worth of the betting card being created
    private int calculateWorth(int currentAmountOfBettingCards) {
        int worth;
        // The first bettingCard gets the highest worth of numberOfBettingCards + 1
        if (currentAmountOfBettingCards == this.gameStateDOM.getGameConfigDOM().getNumberOfBettingCards()) {
            worth = this.gameStateDOM.getGameConfigDOM().getNumberOfBettingCards() + 1;
        }
        // The last bettingCard gets the lowest worth of 2
        else if (currentAmountOfBettingCards == 1) {
            worth = 2;
        }
        // Other cards get a worth of currentAmountOfBettingCards
        else {
            worth = currentAmountOfBettingCards;
        }
        return worth;
    }

    // Method to create a new betting card for a player.
    public BettingCardDOM handleStageBet(StageBet stageBet, int playerId) {
        // Step 1: Get current number of betting cards for this camel
        int currentAmountOfBettingCards = getCurrentAmountOfBettingCards(stageBet);
        // Step 2: Calculate the worth of the new betting card
        int worth = calculateWorth(currentAmountOfBettingCards);
        // Step 3: Create a new BettingCardMove object
        BettingCardDOM newBettingCardDOM = new BettingCardDOM(stageBet.getCamelId(), worth);
        for (BettingCardsDOM bettingCardDeck : this.gameStateDOM.getBettingCards()) {
            if (bettingCardDeck.getCamelId() == stageBet.getCamelId()) {
                bettingCardDeck.setAmount(bettingCardDeck.getAmount() - 1);
            }
        }
        // Step 4: Add the new betting card to the player's list
        PlayerDOM bettingPlayerDOM = this.gameStateDOM.getPlayerById(playerId); // NEUE ÄNDERUNG: --> Rufe player Handler auf? players
        bettingPlayerDOM.getBettingCards().add(newBettingCardDOM);
        // increment numberOfPlacedBettingCardsInGame by 1
        bettingPlayerDOM.incrementNumberOfPlacedBettingCardsInGameBy1();
        // return statement not necessary. Only added for Testing now.
        return newBettingCardDOM;
    }

//    // Main Method for testing and example usage:
//    public static void main(String[] args) {
//
//        PlayerDOM bettingPlayer1 = new PlayerDOM(69696969, "Hans", 9, PlayerDOM.PlayerState.PLAYING);
//        PlayerDOM bettingPlayer2 = new PlayerDOM(42424242, "Fritz", 1, PlayerDOM.PlayerState.CURRENT_STAGE_FORFEIT);
//        ArrayList<PlayerDOM> players = new ArrayList<>();
//        players.add(bettingPlayer1);
//        players.add(bettingPlayer2);
//
//        StageBetMove stageBet1 = new StageBetMove(1);
//        StageBetMove stageBet2 = new StageBetMove(2);
//
//        GameConfigDOM gameConfig = new GameConfigDOM(0, 0, null, 0, 5, 0, 0, null, 0, 0);
//        GameStateDOM gameStateDOM = new GameStateDOM(null, null ,gameConfig, 0,0,0);
//
//        int numberOfBettingCards = 5;
//        // Create two BettingCardsDOM objects and add them to the ArrayList
//        gameStateDOM.getBettingCards().add(new BettingCardsDOM(1, numberOfBettingCards)); // First object with camelId 1
//        gameStateDOM.getBettingCards().add(new BettingCardsDOM(2, numberOfBettingCards)); // Second object with camelId 2
//
//        // Erstellen des BettingCardManagers
//        PlayerBetHandler bettingCardHandler = new PlayerBetHandler(gameStateDOM);
//
//        // Anfordern des ersten Wettscheins für das erste Kamel
//        BettingCardDOM bettingCard1 = bettingCardHandler.handleStageBet(stageBet1, bettingPlayer1.getPlayerId());
//        System.out.println("Erzeuge Kamel 1 - Wettschein 1: CamelDOM ID = " + bettingCard1.getCamelId() + ", Wertigkeit = "
//                + bettingCard1.getWorth());
//
//        // Anfordern des zweiten Wettscheins für das erste Kamel
//        BettingCardDOM bettingCard2 = bettingCardHandler.handleStageBet(stageBet1, bettingPlayer1.getPlayerId());
//        System.out.println("Erzeuge Kamel 1 - Wettschein 2: CamelDOM ID = " + bettingCard2.getCamelId() + ", Wertigkeit = "
//                + bettingCard2.getWorth());
//
//        // Anfordern des dritten Wettscheins für das erste Kamel
//        BettingCardDOM bettingCard3 = bettingCardHandler.handleStageBet(stageBet1, bettingPlayer1.getPlayerId());
//        System.out.println("Erzeuge Kamel 1 - Wettschein 3: CamelDOM ID = " + bettingCard3.getCamelId() + ", Wertigkeit = "
//                + bettingCard3.getWorth());
//
//        // Anfordern des vierten Wettscheins für das erste Kamel
//        BettingCardDOM bettingCard4 = bettingCardHandler.handleStageBet(stageBet1, bettingPlayer1.getPlayerId());
//        System.out.println("Erzeuge Kamel 1 - Wettschein 4: CamelDOM ID = " + bettingCard4.getCamelId() + ", Wertigkeit = "
//                + bettingCard4.getWorth());
//
//        // Anfordern des letzten Wettscheins für das erste Kamel
//        BettingCardDOM bettingCard5 = bettingCardHandler.handleStageBet(stageBet1, bettingPlayer1.getPlayerId());
//        System.out.println("Erzeuge Kamel 1 - Wettschein 5: CamelDOM ID = " + bettingCard5.getCamelId() + ", Wertigkeit = "
//                + bettingCard5.getWorth());
//
//        // System.out.println("Alle Wettscheine von Kamel 1: ");
//        // for (BettingCardMove card : bettingCardManager.createBettingCard(stageBet, bettingPlayer )) {
//        //     System.out.println("CamelDOM ID: " + card.getCamelId() + " Wertigkeit: " + card.getWorth());
//        // }
//
//        // Unerlaubtes Anfordern eines weiteren Wettscheins für das erste Kamel --> Löst
//        // Exception aus.
//        try {
//            BettingCardDOM bettingCard6 = bettingCardHandler.handleStageBet(stageBet1, bettingPlayer2.getPlayerId());
//            System.out.println("Kamel 1 - Wettschein 6: CamelDOM ID = " + bettingCard6.getCamelId() + ", Wertigkeit = "
//                    + bettingCard6.getWorth());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // Anfordern des ersten Wettscheins für das erste Kamel
//        BettingCardDOM bettingCard11 = bettingCardHandler.handleStageBet(stageBet2, bettingPlayer2.getPlayerId());
//        System.out.println("Erzeuge Kamel 2 - Wettschein 1: CamelDOM ID = " + bettingCard11.getCamelId()
//                + ", Wertigkeit = " + bettingCard11.getWorth());
//
//        // Anfordern des zweiten Wettscheins für das erste Kamel
//        BettingCardDOM bettingCard22 = bettingCardHandler.handleStageBet(stageBet2, bettingPlayer2.getPlayerId());
//        System.out.println("Erzeuge Kamel 2 - Wettschein 2: CamelDOM ID = " + bettingCard22.getCamelId()
//                + ", Wertigkeit = " + bettingCard22.getWorth());
//
//        // Anfordern des dritten Wettscheins für das erste Kamel
//        BettingCardDOM bettingCard33 = bettingCardHandler.handleStageBet(stageBet2, bettingPlayer2.getPlayerId());
//        System.out.println("Erzeuge Kamel 2 - Wettschein 3: CamelDOM ID = " + bettingCard33.getCamelId()
//                + ", Wertigkeit = " + bettingCard33.getWorth());
//
//        // Anfordern des vierten Wettscheins für das erste Kamel
//        BettingCardDOM bettingCard44 = bettingCardHandler.handleStageBet(stageBet2, bettingPlayer2.getPlayerId());
//        System.out.println("Erzeuge Kamel 2 - Wettschein 4: CamelDOM ID = " + bettingCard44.getCamelId()
//                + ", Wertigkeit = " + bettingCard44.getWorth());
//
//        // Anfordern des letzten Wettscheins für das erste Kamel
//        BettingCardDOM bettingCard55 = bettingCardHandler.handleStageBet(stageBet2, bettingPlayer2.getPlayerId());
//        System.out.println("Erzeuge Kamel 2 - Wettschein 5: CamelDOM ID = " + bettingCard55.getCamelId()
//                + ", Wertigkeit = " + bettingCard55.getWorth());
//
//
//    }
}