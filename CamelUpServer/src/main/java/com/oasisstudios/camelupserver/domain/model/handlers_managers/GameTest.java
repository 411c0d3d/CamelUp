package com.oasisstudios.camelupserver.domain.model.handlers_managers;

import com.oasisstudios.camelupserver.dataaccess.dto.FinalBet;
import com.oasisstudios.camelupserver.dataaccess.dto.PlacePlayerCard;
import com.oasisstudios.camelupserver.dataaccess.dto.StageBet;
import com.oasisstudios.camelupserver.domain.model.domainclasses.*;

import java.util.ArrayList;
import java.util.Arrays;

public class GameTest {

    public static void printGameStatus(BoardSpacesCourseDOM course, ArrayList<PlayerDOM> players, GameStateDOM gameState) {
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println();
        System.out.println("BoardSpace");
        System.out.println();
        for (int i = 0; i < course.getCourse().length; i++) {
            BoardSpaceDOM boardSpace = course.getBoardSpaceById(i);
            CamelPackDOM camelPack = boardSpace.getCamelPack();

            // Check if camelPack and camels list are not null
            if (camelPack != null && camelPack.getCamels() != null) {
                System.out.print("Camel IDs at BoardSpace " + boardSpace.getSpaceId() + ": ");
                // Iterate through the list of camels in camelPack and print their IDs
                for (CamelDOM camel : camelPack.getCamels()) {
                    System.out.print(camel.getId() + " ");
                }
                if (boardSpace.hasPlayerCard()) {
                    System.out.print("- PlayerCard (" + boardSpace.getPlayerCard().getSpacesMoved() + ") by Player with id " + boardSpace.getPlayerCard().getPlayerId());
                }
                System.out.println();  // Move to the next line after printing the camel IDs
            }
        }

        System.out.println();
        System.out.println();
        System.out.println("Players");
        System.out.println();
        for(PlayerDOM player : players) {
            System.out.println("PlayerId: " + player.getPlayerId());
            System.out.println("Name: " + player.getName());
            System.out.println("Money: " + player.getMoney());
            if(player.getBettingCards() != null) {
                System.out.println("BettingCards: ");
                for(BettingCardDOM bettingCard : player.getBettingCards()) {
                    System.out.println("[CamelId: " + bettingCard.getCamelId() + ", worth: " + bettingCard.getWorth() + "]");
                }
            }
            else {
                System.out.println("BettingCards: -");
            }
            System.out.println("Number of placed BettingCards in Game: " + player.getNumberOfPlacedBettingCardsInGame());
            System.out.println("Playerstate: " + player.getState());
            System.out.println();
        }

        if(gameState.getBettingCards() != null) {
            System.out.println();
            System.out.println("Remaining BettingCards");
            System.out.println();
            for (BettingCardsDOM bettingCards : gameState.getBettingCards()) {
                System.out.println("CamelId: " + bettingCards.getCamelId() + ", amount: " + bettingCards.getAmount());
            }
            System.out.println();
        }
        System.out.println("------------------------------------------------------------------------------------");
    }

    public static void main(String[] args) {

        // Initialize 10 camels
        ArrayList<CamelDOM> camels = new ArrayList<>(Arrays.asList(
                new CamelDOM(-2, "#000000", CamelDOM.Direction.BACKWARDS),
                new CamelDOM(-1, "#FFFFFF", CamelDOM.Direction.BACKWARDS),
                new CamelDOM(0, "#333333", CamelDOM.Direction.FORWARDS),
                new CamelDOM(1, "#444444", CamelDOM.Direction.FORWARDS),
                new CamelDOM(2, "#555555", CamelDOM.Direction.FORWARDS),
                new CamelDOM(3, "#666666", CamelDOM.Direction.FORWARDS),
                new CamelDOM(4, "#777777", CamelDOM.Direction.FORWARDS),
                new CamelDOM(5, "#888888", CamelDOM.Direction.FORWARDS),
                new CamelDOM(6, "#999999", CamelDOM.Direction.FORWARDS),
                new CamelDOM(7, "#424242", CamelDOM.Direction.FORWARDS)
        ));

        // Initialize 6 Player
        ArrayList<PlayerDOM> players = new ArrayList<>(Arrays.asList(
                new PlayerDOM(1, "Anton", 0, PlayerDOM.PlayerState.PLAYING),
                new PlayerDOM(2, "Berta", 0, PlayerDOM.PlayerState.PLAYING),
                new PlayerDOM(3, "Cäsar", 0, PlayerDOM.PlayerState.PLAYING),
                new PlayerDOM(4, "Dora", 0, PlayerDOM.PlayerState.PLAYING),
                new PlayerDOM(5, "Emil", 0, PlayerDOM.PlayerState.PLAYING),
                new PlayerDOM(6, "Friedrich", 0, PlayerDOM.PlayerState.PLAYING)
        ));

        // Initialize GameConfig with numberOfSpaces
        int numberOfSpaces = 30;
        GameConfigDOM gameConfig = new GameConfigDOM(6, numberOfSpaces, camels, 4, 4, 500, 60000, null, 360000, 100);

        // Initialize BoardSpacesCourse with numberOfSpaces
        BoardSpacesCourseDOM boardSpacesCourse = new BoardSpacesCourseDOM(numberOfSpaces);

        // Initialize BettingCards for Camels
        ArrayList<BettingCardsDOM> bettingCards = new ArrayList<>(Arrays.asList(
                new BettingCardsDOM(0, 4),
                new BettingCardsDOM(1, 4),
                new BettingCardsDOM(2, 4),
                new BettingCardsDOM(3, 4),
                new BettingCardsDOM(4, 4),
                new BettingCardsDOM(5, 4),
                new BettingCardsDOM(6, 4),
                new BettingCardsDOM(7, 4)
                ));

        // Initialize GameState and DicePyramid
        GameStateDOM gameState = new GameStateDOM(GameStateDOM.GamePhase.PLAYING, gameConfig, 0, 0, 0);
        gameState.setBettingCards(bettingCards);
        gameState.setPlayers(players);
        DicePyramidDOM dicePyramid = new DicePyramidDOM(gameState);

        // Initialize helpers
        BoardSpacesCourseHelper boardSpacesCourseHelper = new BoardSpacesCourseHelper(boardSpacesCourse);
        CamelMovementHelper camelMovementHelper = new CamelMovementHelper();
        PlayerPenaltyHelper playerPenaltyHelper = new PlayerPenaltyHelper(gameState, boardSpacesCourseHelper);
        TurnRewardHelper turnRewardHelper = new TurnRewardHelper();
        PlayerHandler playerHandler = new PlayerHandler(gameState, playerPenaltyHelper, turnRewardHelper);

        // PlayerRollHandler
        PlayerRollHandler playerRollHandler = new PlayerRollHandler(gameState, boardSpacesCourseHelper, camelMovementHelper, playerHandler);

        // Simulate the camel lineup rolls by using the DicePyramid to generate dices
        dicePyramid.initialLineupRefill();
        while (!dicePyramid.isEmpty()) {
            RolledDiceDOM rolledDice = dicePyramid.roll();
            CamelDOM camel = rolledDice.getCamel();
            int rolledNumber = rolledDice.getRolledNumber();
//            System.out.println("Rolling for Camel " + camel.getId() + " with color " + camel.getColor());
//            System.out.println("Rolled Number: " + rolledNumber);

            // Handle the camel lineup roll
            playerRollHandler.handleCamelLineupRoll(rolledDice);
        }

        printGameStatus(boardSpacesCourse, players, gameState);

        // Place PlayerCards
        System.out.println("PLACING PLAYERCARDS");
        PlacePlayerCard card1 = new PlacePlayerCard(2,1);
        PlacePlayerCard card2 = new PlacePlayerCard(8,1);
        PlacePlayerCard card3 = new PlacePlayerCard(12,1);
        PlacePlayerCard card4 = new PlacePlayerCard(16,-1);
        PlacePlayerCard card5 = new PlacePlayerCard(25,-1);
        PlayerCardHandler cardHandler = new PlayerCardHandler(boardSpacesCourseHelper);
        System.out.println("PlayerCard placeable on spaceid 2: " + cardHandler.isPlayerCardPlaceable(card1, 1));
        System.out.println("PlayerCard placeable on spaceid 8: " + cardHandler.isPlayerCardPlaceable(card2, 2));
        System.out.println("PlayerCard placeable on spaceid 12: " + cardHandler.isPlayerCardPlaceable(card3, 3));
        System.out.println("PlayerCard placeable on spaceid 16: " + cardHandler.isPlayerCardPlaceable(card4, 4));
        System.out.println("PlayerCard placeable on spaceid 25: " + cardHandler.isPlayerCardPlaceable(card5, 5));
        cardHandler.placePlayerCard(card1, 1);
        cardHandler.placePlayerCard(card2, 2);
        cardHandler.placePlayerCard(card3, 3);
        cardHandler.placePlayerCard(card4, 4);
        cardHandler.placePlayerCard(card5, 5);

        printGameStatus(boardSpacesCourse, players, gameState);

        // Place StageBets
        System.out.println("PLACING STAGEBETS");
        StageBet stageBetMove1 = new StageBet(0);
        StageBet stageBetMove2 = new StageBet(2);
        StageBet stageBetMove3 = new StageBet(3);
        StageBet stageBetMove4 = new StageBet(5);
        StageBet stageBetMove5 = new StageBet(7);

        PlayerBetHandler betHandler = new PlayerBetHandler(gameState);
        betHandler.handleStageBet(stageBetMove1, 1);
        betHandler.handleStageBet(stageBetMove2, 2);
        betHandler.handleStageBet(stageBetMove3, 1);
        betHandler.handleStageBet(stageBetMove4, 2);
        betHandler.handleStageBet(stageBetMove5, 1);

        printGameStatus(boardSpacesCourse, players, gameState);

        // Place FinalBets
        System.out.println("PLACING FINALBETS");
        FinalBet finalBetMove1 = new FinalBet(true, 2);
        FinalBet finalBetMove2 = new FinalBet(false, 3);
        FinalBet finalBetMove3 = new FinalBet(false, 4);
        FinalBet finalBetMove4 = new FinalBet(true, 6);
        FinalBet finalBetMove5 = new FinalBet(true, 1);

        FinalBetHandler finalBetHandler = new FinalBetHandler(gameState);

        finalBetHandler.handleFinalBet(finalBetMove1, 1);
        finalBetHandler.handleFinalBet(finalBetMove2, 1);
        finalBetHandler.handleFinalBet(finalBetMove3, 2);
        finalBetHandler.handleFinalBet(finalBetMove4, 2);
        finalBetHandler.handleFinalBet(finalBetMove5, 3);

        System.out.println();
        System.out.println("First Camel Bets");
        for(FinalBetDOM firstCamelBet : gameState.getFinalBets().getFirstCamelBets()) {
            System.out.println("CamelId: " + firstCamelBet.getCamelId() + ", PlayerId: " + firstCamelBet.getPlayerId());
        }
        System.out.println();
        System.out.println("Last Camel Bets");
        for(FinalBetDOM lastCamelBet : gameState.getFinalBets().getLastCamelBets()) {
            System.out.println("CamelId: " + lastCamelBet.getCamelId() + ", PlayerId: " + lastCamelBet.getPlayerId());
        }

        printGameStatus(boardSpacesCourse, players, gameState);

        // Player with id 1 rolling dice
        System.out.println("ROLL DICE");
        dicePyramid.regularStageRefill();

        RolledDiceDOM newDice1 = dicePyramid.roll();

        CamelDOM rolledCamel = newDice1.getCamel();

        int spaceIdOfRolledCamel = boardSpacesCourseHelper.getRolledCamelBoardSpace(rolledCamel).getSpaceId();

        System.out.println();
        System.out.println("Player with id 1 rolling...");
        System.out.println();
        System.out.println("Camel with id " + rolledCamel.getId() + " is currently on BoardSpace with id " + spaceIdOfRolledCamel + " and rolled a " + newDice1.getRolledNumber());
        System.out.println("Camels on BoardSpace: ");
        System.out.print("[");
        for(CamelDOM camel : boardSpacesCourseHelper.getRolledCamelBoardSpace(rolledCamel).getCamelPack().getCamels()) {
            System.out.print(camel.getId() + ", ");
        }
        System.out.print("]");
        System.out.println();
        System.out.println();
        System.out.println("moving...");

        playerRollHandler.handlePlayerRoll(newDice1, 1);

        int newBS = 0;
        for(BoardSpaceDOM boardSpace : boardSpacesCourse.getCourse()) {
            for(CamelDOM camel : boardSpace.getCamelPack().getCamels()) {
                if(camel.getId() == rolledCamel.getId()) {
                    System.out.println();
                    newBS = boardSpace.getSpaceId();
                    if(boardSpace.hasPlayerCard()) {
                        if (boardSpace.getSpaceId() > 1 && boardSpace.getPlayerCard().getSpacesMoved().getValue() == 1) {
                            newBS++;
                            System.out.println("Camel is now on BoardSpace with id " + newBS);
                        } else {
                            newBS--;
                            System.out.println("Camel is now on BoardSpace with id " + newBS);
                        }
                    }
                    else {
                        System.out.println("Camel is now on BoardSpace with id " + newBS);
                    }
                }
            }
        }
        System.out.println("Camels on BoardSpace: ");
        System.out.print("[");
        for(CamelDOM camel : boardSpacesCourse.getCourse()[newBS].getCamelPack().getCamels()) {
            System.out.print(camel.getId() + ", ");
        }
        System.out.println("]");

        printGameStatus(boardSpacesCourse, players, gameState);

        // Player with id 2 rolling dice
        System.out.println("ROLL DICE");

        RolledDiceDOM newDice2 = dicePyramid.roll();

        CamelDOM rolledCamel2 = newDice2.getCamel();

        int spaceIdOfRolledCamel2 = boardSpacesCourseHelper.getRolledCamelBoardSpace(rolledCamel2).getSpaceId();

        System.out.println();
        System.out.println("Player with id 2 rolling...");
        System.out.println();
        System.out.println("Camel with id " + rolledCamel2.getId() + " is currently on BoardSpace with id " + spaceIdOfRolledCamel2 + " and rolled a " + newDice2.getRolledNumber());
        System.out.println("Camels on BoardSpace: ");
        System.out.print("[");
        for(CamelDOM camel : boardSpacesCourseHelper.getRolledCamelBoardSpace(rolledCamel2).getCamelPack().getCamels()) {
            System.out.print(camel.getId() + ", ");
        }
        System.out.print("]");
        System.out.println();
        System.out.println();
        System.out.println("moving...");

        playerRollHandler.handlePlayerRoll(newDice2, 2);

        int newBS2 = 0;
        for(BoardSpaceDOM boardSpace : boardSpacesCourse.getCourse()) {
            for(CamelDOM camel : boardSpace.getCamelPack().getCamels()) {
                if(camel.getId() == rolledCamel2.getId()) {
                    System.out.println();
                    newBS2 = boardSpace.getSpaceId();
                    if(boardSpace.hasPlayerCard()) {
                        if (boardSpace.getSpaceId() > 1 && boardSpace.getPlayerCard().getSpacesMoved().getValue() == 1) {
                            newBS2++;
                            System.out.println("Camel is now on BoardSpace with id " + newBS2);
                        } else {
                            newBS2--;
                            System.out.println("Camel is now on BoardSpace with id " + newBS2);
                        }
                    }
                    else {
                        System.out.println("Camel is now on BoardSpace with id " + newBS2);
                    }
                }
            }
        }
        System.out.println("Camels on BoardSpace: ");
        System.out.print("[");
        for(CamelDOM camel : boardSpacesCourse.getCourse()[newBS2].getCamelPack().getCamels()) {
            System.out.print(camel.getId() + ", ");
        }
        System.out.println("]");

        printGameStatus(boardSpacesCourse, players, gameState);

        // Player with id 3 rolling dice
        System.out.println("ROLL DICE");

        RolledDiceDOM newDice3 = dicePyramid.roll();

        CamelDOM rolledCamel3 = newDice3.getCamel();

        int spaceIdOfRolledCamel3 = boardSpacesCourseHelper.getRolledCamelBoardSpace(rolledCamel3).getSpaceId();

        System.out.println();
        System.out.println("Player with id 3 rolling...");
        System.out.println();
        System.out.println("Camel with id " + rolledCamel3.getId() + " is currently on BoardSpace with id " + spaceIdOfRolledCamel3 + " and rolled a " + newDice3.getRolledNumber());
        System.out.println("Camels on BoardSpace: ");
        System.out.print("[");
        for(CamelDOM camel : boardSpacesCourseHelper.getRolledCamelBoardSpace(rolledCamel3).getCamelPack().getCamels()) {
            System.out.print(camel.getId() + ", ");
        }
        System.out.print("]");
        System.out.println();
        System.out.println();
        System.out.println("moving...");

        playerRollHandler.handlePlayerRoll(newDice3, 3);

        int newBS3 = 0;
        for(BoardSpaceDOM boardSpace : boardSpacesCourse.getCourse()) {
            for(CamelDOM camel : boardSpace.getCamelPack().getCamels()) {
                if(camel.getId() == rolledCamel3.getId()) {
                    System.out.println();
                    newBS3 = boardSpace.getSpaceId();
                    if(boardSpace.hasPlayerCard()) {
                        if (boardSpace.getSpaceId() > 1 && boardSpace.getPlayerCard().getSpacesMoved().getValue() == 1) {
                            newBS3++;
                            System.out.println("Camel is now on BoardSpace with id " + newBS3);
                        } else {
                            newBS3--;
                            System.out.println("Camel is now on BoardSpace with id " + newBS3);
                        }
                    }
                    else {
                        System.out.println("Camel is now on BoardSpace with id " + newBS3);
                    }
                }
            }
        }
        System.out.println("Camels on BoardSpace: ");
        System.out.print("[");
        for(CamelDOM camel : boardSpacesCourse.getCourse()[newBS3].getCamelPack().getCamels()) {
            System.out.print(camel.getId() + ", ");
        }
        System.out.println("]");

        printGameStatus(boardSpacesCourse, players, gameState);

        // Player with id 4 rolling dice
        System.out.println("ROLL DICE");

        RolledDiceDOM newDice4 = dicePyramid.roll();

        CamelDOM rolledCamel4 = newDice4.getCamel();

        int spaceIdOfRolledCamel4 = boardSpacesCourseHelper.getRolledCamelBoardSpace(rolledCamel4).getSpaceId();

        System.out.println();
        System.out.println("Player with id 4 rolling...");
        System.out.println();
        System.out.println("Camel with id " + rolledCamel4.getId() + " is currently on BoardSpace with id " + spaceIdOfRolledCamel4 + " and rolled a " + newDice4.getRolledNumber());
        System.out.println("Camels on BoardSpace: ");
        System.out.print("[");
        for(CamelDOM camel : boardSpacesCourseHelper.getRolledCamelBoardSpace(rolledCamel4).getCamelPack().getCamels()) {
            System.out.print(camel.getId() + ", ");
        }
        System.out.print("]");
        System.out.println();
        System.out.println();
        System.out.println("moving...");

        playerRollHandler.handlePlayerRoll(newDice4, 4);

        int newBS4 = 0;
        for(BoardSpaceDOM boardSpace : boardSpacesCourse.getCourse()) {
            for(CamelDOM camel : boardSpace.getCamelPack().getCamels()) {
                if(camel.getId() == rolledCamel4.getId()) {
                    System.out.println();
                    newBS4 = boardSpace.getSpaceId();
                    if(boardSpace.hasPlayerCard()) {
                        if (boardSpace.getSpaceId() > 1 && boardSpace.getPlayerCard().getSpacesMoved().getValue() == 1) {
                            newBS4++;
                            System.out.println("Camel is now on BoardSpace with id " + newBS4);
                        } else {
                            newBS4--;
                            System.out.println("Camel is now on BoardSpace with id " + newBS4);
                        }
                    }
                    else {
                        System.out.println("Camel is now on BoardSpace with id " + newBS4);
                    }
                }
            }
        }
        System.out.println("Camels on BoardSpace: ");
        System.out.print("[");
        for(CamelDOM camel : boardSpacesCourse.getCourse()[newBS4].getCamelPack().getCamels()) {
            System.out.print(camel.getId() + ", ");
        }
        System.out.println("]");

        printGameStatus(boardSpacesCourse, players, gameState);

    }
}
