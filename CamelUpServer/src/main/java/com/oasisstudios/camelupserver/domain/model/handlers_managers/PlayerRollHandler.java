package com.oasisstudios.camelupserver.domain.model.handlers_managers;

import com.oasisstudios.camelupserver.domain.model.domainclasses.*;

// PlayerRollHandler is responsible for all dice rolling related actions like initializing the gameboard with camels
// and handling a dice roll by a player. In this context also delegates rewarding the roll as well as stepping on player card.
// call handleCamelLineupRoll if you want to initialize the gameboard with camels at start.
// call handlePlayerRoll for regular rolls within turns.
// Is injected with GameStateDOM, BoardSpacesCourseHelper, CamelMovementHelper and PlayerHandler.


public class PlayerRollHandler {
    GameStateDOM gameStateDOM;
    BoardSpacesCourseHelper boardSpacesCourseHelper;
    CamelMovementHelper camelMovementHelper;
    PlayerHandler playerHandler;

    public PlayerRollHandler(GameStateDOM gameStateDOM, BoardSpacesCourseHelper boardSpacesCourseHelper,
                             CamelMovementHelper camelMovementHelper, PlayerHandler playerHandler) {
        this.gameStateDOM = gameStateDOM;
        this.boardSpacesCourseHelper = boardSpacesCourseHelper;
        this.camelMovementHelper = camelMovementHelper;
        this.playerHandler = playerHandler;
    }

    // Method for realizing the initial camel lineup 
    public void handleCamelLineupRoll(RolledDiceDOM rolledDiceDOM) {
        // Calculate Ids of startBoardSpaceDOM and destinationBoardSpaceDOM and
        int startBoardSpaceId = 0;
        int destinationBoardSpaceOffset = rolledDiceDOM.getRolledNumber()
                * (rolledDiceDOM.getCamel().getDirection().getModifier());
        // request boardSpacesCourseManager to provide the corresponding objects
        BoardSpaceDOM startBoardSpaceDOM = boardSpacesCourseHelper.getBoardSpaceById(startBoardSpaceId);
        BoardSpaceDOM destinationBoardSpaceDOM = boardSpacesCourseHelper.getDestinationBoardspace(startBoardSpaceDOM, destinationBoardSpaceOffset);
        // No player card possible at initial lineup, hence set variable spacesMoved to null
        PlayerCardDOM.SpacesMoved spacesMoved = null;
        // place the camel on the StartBoardSpace
        startBoardSpaceDOM.getCamelPack().addCamelOnTop(rolledDiceDOM.getCamel());
        // Capsule camelPacks from startBoardSpaceDOM and destinationBoardSpaceDOM into a
        // CamelPackPairDOM object
        CamelPackPairDOM camelPackPairDOM = new CamelPackPairDOM(startBoardSpaceDOM.getCamelPack(),
                destinationBoardSpaceDOM.getCamelPack());
        // Call the camelMovementHandler to move the camels from one pack to the other.
        CamelPackPairDOM updatedCamelPackPairDOM = camelMovementHelper.moveCamels(camelPackPairDOM, rolledDiceDOM.getCamel(), spacesMoved);
        // After moving update the startBoardSpaceDOM and destinationBoardSpaceDOM camelPacks
        startBoardSpaceDOM.setCamelPack(updatedCamelPackPairDOM.getStartCamelPack());
        // In lineup Roll the startBoardSpaceDOM is always the finish line Boardspace with id = 0 during lineup. Check if it is empty after moving camels.
        if (!startBoardSpaceDOM.getCamelPack().getCamels().isEmpty()) {
            throw new IllegalStateException("BoardSpaceDOM " + startBoardSpaceDOM.getSpaceId() + " not empty while performing handleCamelLineupRoll");
        }
        destinationBoardSpaceDOM.setCamelPack(camelPackPairDOM.getDestinationCamelPack());
        // Tell boardSpacesCourseManager to update hashmap entries, assigning moved camels to their destinationBoardSpaceDOM
        boardSpacesCourseHelper.updateCamelToSpacesMapEntries(destinationBoardSpaceDOM);
    }

    private boolean isWinningRoll(RolledDiceDOM rolledDiceDOM) {
        BoardSpaceDOM startBoardSpaceDOM = boardSpacesCourseHelper.getRolledCamelBoardSpace(rolledDiceDOM.getCamel());
        int destinationBoardSpaceOffset = rolledDiceDOM.getRolledNumber()
                * (rolledDiceDOM.getCamel().getDirection().getModifier());
        BoardSpaceDOM destinationBoardSpaceDOM = boardSpacesCourseHelper.getDestinationBoardspace(startBoardSpaceDOM, destinationBoardSpaceOffset);
        // initialize a spacesMoved variable in case boardSpace has a player card placed.
        PlayerCardDOM.SpacesMoved spacesMoved;
        if (destinationBoardSpaceDOM.hasPlayerCard()) {
            spacesMoved = destinationBoardSpaceDOM.getPlayerCard().getSpacesMoved();
            // calculate new destination caused by player card spacesMoved value.
            int newDestinationBoardSpaceOffset = destinationBoardSpaceDOM.getSpaceId() + spacesMoved.getValue();
            // get the destinationBoardSpaceDOM considering the spacesMoved
            destinationBoardSpaceDOM = boardSpacesCourseHelper.getDestinationBoardspace(startBoardSpaceDOM, newDestinationBoardSpaceOffset);
        }
        return destinationBoardSpaceDOM == boardSpacesCourseHelper.getFinishlineBoardspace();
    }


    public void handlePlayerRoll(RolledDiceDOM rolledDiceDOM, int playerId) {
        BoardSpaceDOM startBoardSpaceDOM = boardSpacesCourseHelper.getRolledCamelBoardSpace(rolledDiceDOM.getCamel());
        int destinationBoardSpaceOffset = rolledDiceDOM.getRolledNumber()
                * (rolledDiceDOM.getCamel().getDirection().getModifier());
        BoardSpaceDOM destinationBoardSpaceDOM = boardSpacesCourseHelper.getDestinationBoardspace(startBoardSpaceDOM, destinationBoardSpaceOffset);
        // initialize a spacesMoved variable in case a player card has to be handled on the destination boardspace.
        PlayerCardDOM.SpacesMoved spacesMoved = null;
        // if DestinationBoardSpace has playerCard, handling the playercard and updating the
        // destinationBoardSpaceDOM variable based upon playercard is required.
        if (destinationBoardSpaceDOM.hasPlayerCard()) {
            spacesMoved = destinationBoardSpaceDOM.getPlayerCard().getSpacesMoved();
            // calculate new destination by adding playercard spacesMoved value and then applying camel direction.
            int newDestinationBoardSpaceOffset = (rolledDiceDOM.getRolledNumber() + spacesMoved.getValue()) * rolledDiceDOM.getCamel().getDirection().getModifier();
            // give playerCard to playerCardHandler to handle and remove playerCard from BoardSpaceDOM
            playerHandler.rewardSteppedPlayerCardToPlayer(destinationBoardSpaceDOM.getPlayerCard().getPlayerId());
            // get the destinationBoardSpaceDOM considering the spacesMoved
            destinationBoardSpaceDOM = boardSpacesCourseHelper.getDestinationBoardspace(startBoardSpaceDOM, newDestinationBoardSpaceOffset);
        }
        // Capsule camelPacks from startBoardSpaceDOM and destinationBoardSpaceDOM into a
        // CamelPackPairDOM object
        CamelPackPairDOM camelPackPairDOM = new CamelPackPairDOM(startBoardSpaceDOM.getCamelPack(),
                destinationBoardSpaceDOM.getCamelPack());
        // Call the camelMovementHandler to move the camels from one pack to the other.
        CamelPackPairDOM updatedCamelPackPairDOM = camelMovementHelper.moveCamels(camelPackPairDOM, rolledDiceDOM.getCamel(), spacesMoved);
        // update the start and destination Boardspaces with the moved camel packs
        destinationBoardSpaceDOM.setCamelPack(updatedCamelPackPairDOM.getDestinationCamelPack());
        startBoardSpaceDOM.setCamelPack(updatedCamelPackPairDOM.getStartCamelPack());
        // Tell boardSpacesCourseManager to update hashmap entries, assigning moved camels to their destinationBoardSpaceDOM
        boardSpacesCourseHelper.updateCamelToSpacesMapEntries(destinationBoardSpaceDOM);
        // add rolled dice to rolled dice manager's list
        gameStateDOM.addRolledDice(rolledDiceDOM);
        // reward money to player for the roll
        playerHandler.rewardStageRollToPlayer(playerId);
    }



//    public static void main(String[] args) {
//        // Start the timer
//        long startTime = System.nanoTime();
//
//        // Step 1: Initialize the minimum of camels
//        ArrayList<CamelDOM> camels = new ArrayList<>();
//        camels.add(new CamelDOM(-2, "#000000", CamelDOM.Direction.BACKWARDS));
//        camels.add(new CamelDOM(-1, "#FFFFFF", CamelDOM.Direction.BACKWARDS));
//        camels.add(new CamelDOM(0, "#333333", CamelDOM.Direction.FORWARDS));
//        camels.add(new CamelDOM(1, "#444444", CamelDOM.Direction.FORWARDS));
//        int additionalForwardCamels = 0;
//        // Step 2: Add more camels with positive integer IDs (incremented by +1) and direction FORWARDS
//        for (int i = 2; i <= additionalForwardCamels; i++) {
//            camels.add(new CamelDOM(i, "#"+String.format("%06x", (int)(Math.random() * 0xFFFFFF)), CamelDOM.Direction.FORWARDS));
//        }
//
//        // Step 3: Create GameConfigDOM with camels and number of spaces, and diceFaces, players
//        PlayerDOM player0 = new PlayerDOM(0,"Alf",0, PlayerDOM.PlayerState.PLAYING);
//        int numberOfSpaces = 5;
//        int diceFaces = 1;  // Set the number of dice faces
//        GameConfigDOM gameConfig = new GameConfigDOM(0, numberOfSpaces, camels, diceFaces, 0, 0, 0, null, 0, 0);
//        GameStateDOM gameStateDOM = new GameStateDOM(null, null, gameConfig,0,0,0);
//        gameStateDOM.getPlayers().add(player0);
//        DicePyramidDOM pyramid = new DicePyramidDOM(gameStateDOM);
//        // Step 4: Create BoardSpacesCourseDOM with number of spaces
//        BoardSpacesCourseDOM course = new BoardSpacesCourseDOM(numberOfSpaces);
//        System.out.println("BoardSpacesCourseDOM: " + Arrays.toString(course.getCourse()));
//
//        // Step 5: Initialize helpers
//        BoardSpacesCourseHelper boardSpacesCourseHelper = new BoardSpacesCourseHelper(course);
//        CamelMovementHelper camelMovementHelper = new CamelMovementHelper();
//        PlayerHandler playerHandler = new PlayerHandler(gameStateDOM, null, null);
//
//        // Step 6: Create PlayerRollHandler
//        PlayerRollHandler playerRollHandler = new PlayerRollHandler(gameStateDOM, boardSpacesCourseHelper, camelMovementHelper, playerHandler);
//
//        // Step 7: Simulate the camel lineup rolls by using the dice pyramid to generate dices
//        pyramid.initialLineupRefill();
//        while (!pyramid.isEmpty()) {
//            RolledDiceDOM rolledDice = pyramid.roll();
//            CamelDOM camel = rolledDice.getCamel();
//            int rolledNumber = rolledDice.getRolledNumber();
//            System.out.println("Rolling for CamelDOM " + camel.getId() + " with color " + camel.getColor());
//            System.out.println("Rolled Number: " + rolledNumber);
//
//            // Step 8: Handle the camel lineup roll
//            playerRollHandler.handleCamelLineupRoll(rolledDice);
//        }
//
//        PlacePlayerCardMove card = new PlacePlayerCardMove(2,-1);
//        PlayerCardHandler cardHandler = new PlayerCardHandler(boardSpacesCourseHelper);
//        cardHandler.placePlayerCard(0, card);
//
//        // Step 9: Loop through the BoardSpacesCourseDOM and print camel IDs
//
//            System.out.println("\nFinal positions of camels:");
//            for (int i = 0; i < course.getCourse().length; i++) {
//                BoardSpaceDOM boardSpace = course.getBoardSpaceById(i);
//                CamelPackDOM camelPack = boardSpace.getCamelPack();
//
//                // Check if camelPack and camels list are not null
//                if (camelPack != null && camelPack.getCamels() != null) {
//                    System.out.print("CamelDOM IDs at BoardSpaceDOM " + boardSpace.getSpaceId() + ": ");
//                    // Iterate through the list of camels in camelPack and print their IDs
//                    for (CamelDOM camel : camelPack.getCamels()) {
//                        System.out.print(" | " + camel.getId() + " | ");
//                    }
//                    if (boardSpace.hasPlayerCard()) {
//                        System.out.print("PlayerCardDOM SpacesMoved is " + boardSpace.getPlayerCard().getSpacesMoved().getValue());
//                    } else {
//                        System.out.print("No PlayerCardDOM");
//                    }
//                    System.out.println();  // Move to the next line after printing the camel IDs
//                }
//            }
//
//
//        pyramid.regularStageRefill();
//        RolledDiceDOM rolledDice = pyramid.roll();
//        CamelDOM camel = rolledDice.getCamel();
//        int rolledNumber = rolledDice.getRolledNumber();
//        System.out.println("Rolling for CamelDOM " + camel.getId() + " with color " + camel.getColor());
//        System.out.println("Rolled Number: " + rolledNumber);
//        // Handle the camel lineup roll
//        playerRollHandler.handleCamelLineupRoll(rolledDice);
//
//
//        System.out.println("\nFinal positions of camels:");
//        for (int i = 0; i < course.getCourse().length; i++) {
//            BoardSpaceDOM boardSpace = course.getBoardSpaceById(i);
//            CamelPackDOM camelPack = boardSpace.getCamelPack();
//
//            // Check if camelPack and camels list are not null
//            if (camelPack != null && camelPack.getCamels() != null) {
//                System.out.print("CamelDOM IDs at BoardSpaceDOM " + boardSpace.getSpaceId() + ": ");
//                // Iterate through the list of camels in camelPack and print their IDs
//                for (CamelDOM camel1 : camelPack.getCamels()) {
//                    System.out.print(" | " + camel1.getId() + " | ");
//                }
//                if (boardSpace.hasPlayerCard()) {
//                    System.out.print("PlayerCardDOM SpacesMoved is " + boardSpace.getPlayerCard().getSpacesMoved().getValue());
//                } else {
//                    System.out.print("No PlayerCardDOM");
//                }
//                System.out.println();  // Move to the next line after printing the camel IDs
//            }
//        }
//
//        // End the timer and calculate the elapsed time
//        long endTime = System.nanoTime();
//        long duration = endTime - startTime;
//
//        // Convert duration from nanoseconds to milliseconds for easier reading
//        double durationInMillis = duration / 1_000_000.0;
//        System.out.println("\nTime taken for the main method: " + durationInMillis + " milliseconds");
//    }
}

