package com.oasisstudios.camelupengine.engineteilnehmer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.oasisstudios.camelupengine.dto.*;

public class EngineParticipantTest {

    public void runTests() {
        testCalculatePossibleWinners();
        testCalculateCamelToBetOn();
        testCalculateAllMovedCamels();
        testCalculateAllCamelIds();
        testCalculateCamelsAbleToMove();
        testFindCurrentFirstPlaceField();
        testCalculatePossibleLooser();
        testPlaceFinalBet();
        testPlaceStageBet();
    }

    public void testCalculatePossibleWinners() {
        System.out.println("Running testCalculatePossibleWinners...");
        EngineParticipant engineParticipant = getEngine();
        GameState gameState = createGameState();

        // Spielsituation simulieren
        gameState.getBoardSpaces().get(9).getCamelIds().add(1); // Feld 9: Kamel 1
        gameState.getBoardSpaces().get(8).getCamelIds().add(2); // Feld 8: Kamel 2

        List<BoardSpace> winners = engineParticipant.calculatePossibleWinners(gameState);

        if (winners.size() == 2 && winners.get(0).getSpaceId() == 9 && winners.get(1).getSpaceId() == 8) {
            System.out.println("testCalculatePossibleWinners PASSED");
        } else {
            System.out.println("testCalculatePossibleWinners FAILED");
            System.out.println("Expected: [9, 8], Found: " + winners.stream().map(BoardSpace::getSpaceId).toList());
        }
    }

    private EngineParticipant getEngine() {
        return new EngineParticipant(1, null, false);
    }



    public void testCalculateCamelToBetOn() {
        System.out.println("Running testCalculateCamelToBetOn...");
        EngineParticipant engineParticipant = getEngine();
        GameState gameState = createGameState();

        // Spielsituation simulieren
        gameState.getBoardSpaces().get(9).getCamelIds().add(1); // Feld 9: Kamel 1
        gameState.getBoardSpaces().get(8).getCamelIds().add(3); // Feld 8: Kamel 3 (unter Kamel 1)

        int camelId = engineParticipant.calculateCamelToBetOn(gameState);

        if (camelId == 1) {
            System.out.println("testCalculateCamelToBetOn PASSED");
        } else {
            System.out.println("testCalculateCamelToBetOn FAILED");
            System.out.println("Expected: 1, Found: " + camelId);
        }
    }



    public void testCalculateAllMovedCamels() {
        System.out.println("Running testCalculateAllMovedCamels...");
        EngineParticipant engineParticipant = getEngine();
        GameState gameState = createGameState();

        List<Integer> movedCamels = engineParticipant.calculateAllMovedCamels(gameState);

        if (movedCamels.isEmpty()) {
            System.out.println("testCalculateAllMovedCamels PASSED");
        } else {
            System.out.println("testCalculateAllMovedCamels FAILED");
        }
    }

    public void testCalculateAllCamelIds() {
        System.out.println("Running testCalculateAllCamelIds...");
        EngineParticipant engineParticipant = getEngine();
        GameState gameState = createGameState();

        List<Integer> camelIds = engineParticipant.calculateAllCamelIds(gameState);
            System.out.println(camelIds);
        if (camelIds.equals(Arrays.asList(-2,-1,0, 1, 2))) {
            System.out.println("testCalculateAllCamelIds PASSED");
        } else {
            System.out.println("testCalculateAllCamelIds FAILED");
        }
    }

    public void testCalculateCamelsAbleToMove() {
        System.out.println("Running testCalculateCamelsAbleToMove...");
        EngineParticipant engineParticipant = getEngine();
        GameState gameState = createGameState();

        List<Integer> camelsAbleToMove = engineParticipant.calculateCamelsAbleToMove(gameState);
        System.out.println(camelsAbleToMove);
        if (camelsAbleToMove.equals(Arrays.asList(-2,-1,0, 1, 2))) {
            System.out.println("testCalculateCamelsAbleToMove PASSED");
        } else {
            System.out.println("testCalculateCamelsAbleToMove FAILED");
        }
    }

    public void testFindCurrentFirstPlaceField() {
        System.out.println("Running testFindCurrentFirstPlaceField...");
        EngineParticipant engineParticipant = getEngine();
        GameState gameState = createGameState();
        gameState.getBoardSpaces().get(9).getCamelIds().add(1);

        int firstPlaceField = engineParticipant.findCurrentFirstPlaceField(gameState);

        if (firstPlaceField == 9) {
            System.out.println("testFindCurrentFirstPlaceField PASSED");
        } else {
            System.out.println("testFindCurrentFirstPlaceField FAILED");
        }
    }

    public void testCalculatePossibleLooser() {
        System.out.println("Running testCalculatePossibleLooser...");
        EngineParticipant engineParticipant = getEngine();
        GameState gameState = createGameState();
        gameState.getBoardSpaces().get(0).getCamelIds().add(4);

        int looserId = engineParticipant.calculatePossibleLooser(gameState);

        if (looserId == 4) {
            System.out.println("testCalculatePossibleLooser PASSED");
        } else {
            System.out.println("testCalculatePossibleLooser FAILED");
        }
    }

    public void testPlaceFinalBet() {
        System.out.println("Running testPlaceFinalBet...");
        EngineParticipant engineParticipant = getEngine();

        FinalBet finalBet = engineParticipant.placeFinalBet(true, 1);

        if (finalBet != null && finalBet.getId() == 1) { 
            // && finalBet.getPlayerId() == 1234 FinalBet has no PlayerID 
            System.out.println("testPlaceFinalBet PASSED");
        } else {
            System.out.println("testPlaceFinalBet FAILED");
        }
    }

    public void testPlaceStageBet() {
        System.out.println("Running testPlaceStageBet...");
        EngineParticipant engineParticipant = getEngine();

        StageBet stageBet = engineParticipant.placeStageBet(2);

        if (stageBet != null && stageBet.getCamelId() == 2) {
            System.out.println("testPlaceStageBet PASSED");
        } else {
            System.out.println("testPlaceStageBet FAILED");
        }
    }

    private GameState createGameState() {
        GameState gameState = new GameState();
        GameConfig gameConfig = new GameConfig();
        List<Camel> camels = new ArrayList<>();
        camels.add(Camel.builder().withColor("000000").withId(-2).build());
        camels.add(Camel.builder().withColor("000000").withId(-1).build());
        camels.add(Camel.builder().withColor("000000").withId(0).build());
        camels.add(Camel.builder().withColor("000000").withId(1).build());
        camels.add(Camel.builder().withColor("000000").withId(2).build());
        gameConfig.setCamels(camels);;
        gameConfig.setDiceFaces(3);
        gameState.setGameConfig(gameConfig);

        List<BoardSpace> boardSpaces = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BoardSpace space = new BoardSpace();
            space.setSpaceId(i);
            space.setCamelIds(new ArrayList<>());
            boardSpaces.add(space);
        }
        gameState.setBoardSpaces(boardSpaces);

        // Initialisiere eine leere Liste für RolledDice
        gameState.setRolledDice(new ArrayList<>());

        return gameState;
    }

}
