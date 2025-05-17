package com.oasisstudios.camelupserver.domain.model.handlers_managers;

import com.oasisstudios.camelupserver.domain.model.domainclasses.*;

import java.util.ArrayList;
import java.util.Collections;

public class EndGameRatingService {
    public boolean isRated;
    public enum PrizeMoney {
        WINNER_FIRSTBET(8),
        WINNER_SECONDBET(5),
        WINNER_THIRDBET(3),
        WINNER_REMAININGBETS(1),

        LOSER_FIRSTBET(8),
        LOSER_SECONDBET(5),
        LOSER_THIRDBET(3),
        LOSER_REMAININGBETS(1),

        NON_WINNER_NON_LOSER(-1);

        private final int value;

        PrizeMoney(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    private BoardSpacesCourseHelper boardSpacesCourseHelper;
    private GameStateDOM gameStateDOM;

    public EndGameRatingService(BoardSpacesCourseHelper boardSpacesCourseHelper, GameStateDOM gameStateDOM) {
        this.gameStateDOM = gameStateDOM;
        this.boardSpacesCourseHelper = boardSpacesCourseHelper;
    }

    public void performEndGameRatingForAllPlayers() {
        this.isRated = true;
        CamelDOM winnerCamelDOM = boardSpacesCourseHelper.getLeadingCamel();
        CamelDOM loserCamelDOM = boardSpacesCourseHelper.getLosingCamel();

        boolean winner_firstbet_found = false;
        boolean winner_secondbet_found = false;
        boolean winner_thirdbet_found = false;
        // iterate through all winner FinalBetDOM objects and assign prize according to PV (38)
        for (FinalBetDOM bet : gameStateDOM.getFinalBets().getFirstCamelBets()) {
            int playerId = bet.getPlayerId();
            PlayerDOM playerDOM = gameStateDOM.getPlayerById(playerId);
            if (bet.getCamelId() == winnerCamelDOM.getId()) {
                if (!winner_firstbet_found) {
                    winner_firstbet_found = true;
                    playerDOM.addToMoney(PrizeMoney.WINNER_FIRSTBET.getValue());
                } else if (!winner_secondbet_found) {
                    winner_secondbet_found = true;
                    playerDOM.addToMoney(PrizeMoney.WINNER_SECONDBET.getValue());
                } else if (!winner_thirdbet_found) {
                    winner_thirdbet_found = true;
                    playerDOM.addToMoney(PrizeMoney.WINNER_THIRDBET.getValue());
                } else {
                    playerDOM.addToMoney(PrizeMoney.WINNER_REMAININGBETS.getValue());
                }
            } else {
                playerDOM.addToMoney(PrizeMoney.NON_WINNER_NON_LOSER.getValue());
            }
            // adjust for negative money: set to 0, otherwise keep amount.
            playerDOM.setMoney(Math.max(0, playerDOM.getMoney()));
        }

        boolean loser_firstbet_found = false;
        boolean loser_secondbet_found = false;
        boolean loser_thirdbet_found = false;
        // iterate through all loser FinalBetDOM objects and assign prize according to PV (38)
        for (FinalBetDOM bet : gameStateDOM.getFinalBets().getLastCamelBets()) {
            int playerId = bet.getPlayerId();
            PlayerDOM playerDOM = gameStateDOM.getPlayerById(playerId);
            if (bet.getCamelId() == loserCamelDOM.getId()) {
                if (!loser_firstbet_found) {
                    loser_firstbet_found = true;
                    playerDOM.addToMoney(PrizeMoney.LOSER_FIRSTBET.getValue());
                } else if (!loser_secondbet_found) {
                    loser_secondbet_found = true;
                    playerDOM.addToMoney(PrizeMoney.LOSER_SECONDBET.getValue());
                } else if (!loser_thirdbet_found) {
                    loser_thirdbet_found = true;
                    playerDOM.addToMoney(PrizeMoney.LOSER_THIRDBET.getValue());
                } else {
                    playerDOM.addToMoney(PrizeMoney.LOSER_REMAININGBETS.getValue());
                }
            } else {
                playerDOM.addToMoney(PrizeMoney.NON_WINNER_NON_LOSER.getValue());
            }
            // adjust for negative money: set to 0, otherwise keep amount.
            playerDOM.setMoney(Math.max(0, playerDOM.getMoney()));
        }

        // order all playerDOMS in descending order by money, if equal by getNumberOfPlacedBettingCardsInGame, otherwise randomize
        ArrayList<PlayerDOM> playerDOMS = gameStateDOM.getPlayers();
        // Check if all playerDOMS have the same money and number of placed betting cards
        boolean allEqual = playerDOMS.stream()
                .allMatch(p -> p.getMoney() == playerDOMS.getFirst().getMoney() &&
                        p.getNumberOfPlacedBettingCardsInGame() == playerDOMS.getFirst().getNumberOfPlacedBettingCardsInGame());
        if (allEqual) {
            // If all playerDOMS have the same money and betting cards, shuffle the list
            System.out.println("All playerDOMS have the same money and betting cards. Shuffling the list...");
            Collections.shuffle(playerDOMS);
        } else {
            // Sort playerDOMS by money first, then by number of placed betting cards as a tiebreaker
            System.out.println("Sorting playerDOMS based on money and betting cards...");
            playerDOMS.sort((p1, p2) -> {
                // Primary sort by money in descending order
                int moneyComparison = Integer.compare(p2.getMoney(), p1.getMoney());
                if (moneyComparison != 0) {
                    return moneyComparison;
                }
                // if money equal, sort by numberOfPlacedBettingCardsInGame in descending order
                return Integer.compare(p2.getNumberOfPlacedBettingCardsInGame(), p1.getNumberOfPlacedBettingCardsInGame());
            });
        }
    }

}
