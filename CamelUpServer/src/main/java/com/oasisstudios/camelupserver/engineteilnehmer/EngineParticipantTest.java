package com.oasisstudios.camelupserver.engineteilnehmer;

import com.oasisstudios.camelupserver.dataaccess.dto.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EngineParticipantTest {

    public static void main(String[] args) {
        EngineParticipantTest test = new EngineParticipantTest();
        test.runTests();
    }

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
        EngineParticipant engineParticipant = new EngineParticipant(1);
        GameStateDTO gameState = createGameState();

        // Spielsituation simulieren
        gameState.getBoardSpaces().get(9).getCamelIds().add(1); // Feld 9: Kamel 1
        gameState.getBoardSpaces().get(8).getCamelIds().add(2); // Feld 8: Kamel 2

        List<BoardSpaceDTO> winners = engineParticipant.calculatePossibleWinners(gameState);

        if (winners.size() == 2 && winners.get(0).getSpaceId() == 9 && winners.get(1).getSpaceId() == 8) {
            System.out.println("testCalculatePossibleWinners PASSED");
        } else {
            System.out.println("testCalculatePossibleWinners FAILED");
            System.out.println("Expected: [9, 8], Found: " + winners.stream().map(BoardSpaceDTO::getSpaceId).toList());
        }
    }



    public void testCalculateCamelToBetOn() {
        System.out.println("Running testCalculateCamelToBetOn...");
        EngineParticipant engineParticipant = new EngineParticipant(1);
        GameStateDTO gameState = createGameState();

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
        EngineParticipant engineParticipant = new EngineParticipant(1);
        GameStateDTO gameState = createGameState();

        List<Integer> movedCamels = engineParticipant.calculateAllMovedCamels(gameState);

        if (movedCamels.isEmpty()) {
            System.out.println("testCalculateAllMovedCamels PASSED");
        } else {
            System.out.println("testCalculateAllMovedCamels FAILED");
        }
    }

    public void testCalculateAllCamelIds() {
        System.out.println("Running testCalculateAllCamelIds...");
        EngineParticipant engineParticipant = new EngineParticipant(1);
        GameStateDTO gameState = createGameState();

        List<Integer> camelIds = engineParticipant.calculateAllCamelIds(gameState);

        if (camelIds.equals(Arrays.asList(0, 1, 2, 3, 4))) {
            System.out.println("testCalculateAllCamelIds PASSED");
        } else {
            System.out.println("testCalculateAllCamelIds FAILED");
        }
    }

    public void testCalculateCamelsAbleToMove() {
        System.out.println("Running testCalculateCamelsAbleToMove...");
        EngineParticipant engineParticipant = new EngineParticipant(1);
        GameStateDTO gameState = createGameState();

        List<Integer> camelsAbleToMove = engineParticipant.calculateCamelsAbleToMove(gameState);

        if (camelsAbleToMove.equals(Arrays.asList(0, 1, 2, 3, 4))) {
            System.out.println("testCalculateCamelsAbleToMove PASSED");
        } else {
            System.out.println("testCalculateCamelsAbleToMove FAILED");
        }
    }

    public void testFindCurrentFirstPlaceField() {
        System.out.println("Running testFindCurrentFirstPlaceField...");
        EngineParticipant engineParticipant = new EngineParticipant(1);
        GameStateDTO gameState = createGameState();
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
        EngineParticipant engineParticipant = new EngineParticipant(1);
        GameStateDTO gameState = createGameState();
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
        EngineParticipant engineParticipant = new EngineParticipant(1);

        FinalBetDTO finalBet = engineParticipant.placeFinalBet(true, 1);

        if (finalBet != null && finalBet.getCamelId() == 1 && finalBet.getPlayerId() == 1234) {
            System.out.println("testPlaceFinalBet PASSED");
        } else {
            System.out.println("testPlaceFinalBet FAILED");
        }
    }

    public void testPlaceStageBet() {
        System.out.println("Running testPlaceStageBet...");
        EngineParticipant engineParticipant = new EngineParticipant(1);

        StageBetDTO stageBet = engineParticipant.placeStageBet(2);

        if (stageBet != null && stageBet.getCamelId() == 2) {
            System.out.println("testPlaceStageBet PASSED");
        } else {
            System.out.println("testPlaceStageBet FAILED");
        }
    }

    private GameStateDTO createGameState() {
        GameStateDTO gameState = new GameStateDTO();
        GameConfigDTO gameConfig = new GameConfigDTO();
        gameConfig.setCamelCount(5);
        gameConfig.setDiceFacesCount(3);
        gameState.setGameConfig(gameConfig);

        List<BoardSpaceDTO> boardSpaces = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BoardSpaceDTO space = new BoardSpaceDTO();
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
