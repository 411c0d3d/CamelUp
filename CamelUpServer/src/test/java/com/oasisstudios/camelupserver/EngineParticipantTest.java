package com.oasisstudios.camelupserver;

import static org.junit.jupiter.api.Assertions.*;


import com.oasisstudios.camelupserver.dataaccess.dto.*;
import com.oasisstudios.camelupserver.engineteilnehmer.EngineParticipant;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EngineParticipantTest {

    @Test
    public void testCalculatePossibleWinners() {
        EngineParticipant engineParticipant = new EngineParticipant(1);
        GameStateDTO gameState = createGameState();

        // Spielsituation simulieren
        gameState.getBoardSpaces().get(9).getCamelIds().add(1); // Feld 9: Kamel 1
        gameState.getBoardSpaces().get(8).getCamelIds().add(2); // Feld 8: Kamel 2

        List<BoardSpaceDTO> winners = engineParticipant.calculatePossibleWinners(gameState);

        assertEquals(2, winners.size());
        assertEquals(9, winners.get(0).getSpaceId());
        assertEquals(8, winners.get(1).getSpaceId());
    }

    @Test
    public void testCalculatePossibleWinnersWithEmptyFields() {
        EngineParticipant engineParticipant = new EngineParticipant(1);
        GameStateDTO gameState = createGameState();

        gameState.getBoardSpaces().get(9).getCamelIds().add(1); // Feld 9: Kamel 1
        gameState.getBoardSpaces().get(7).getCamelIds().add(3); // Feld 7: Kamel 3 (weiter hinten, sollte nicht als Gewinner gezählt werden)

        List<BoardSpaceDTO> winners = engineParticipant.calculatePossibleWinners(gameState);

        assertEquals(1, winners.size());
        assertEquals(9, winners.get(0).getSpaceId());
    }

    @Test
    public void testCanPlacePlayerCardWithOccupiedField() {
        System.out.println("Running testCanPlacePlayerCardWithOccupiedField...");
        EngineParticipant engineParticipant = new EngineParticipant(1);
        GameStateDTO gameState = createGameState();

        // Spielerkarte auf Feld 5 platzieren
        PlayerCardDTO playerCard = new PlayerCardDTO();
        playerCard.setPlayerId(1);
        gameState.getBoardSpaces().get(5).setPlayerCard(playerCard);

        boolean canPlace = engineParticipant.canPlacePlayerCard(gameState);

        if (!canPlace) {
            System.out.println("testCanPlacePlayerCardWithOccupiedField PASSED");
        } else {
            System.out.println("testCanPlacePlayerCardWithOccupiedField FAILED");
            System.out.println("Expected: false, Found: " + canPlace);
        }
    }


    @Test
    public void testCalculateCamelToBetOn() {
        EngineParticipant engineParticipant = new EngineParticipant(1);
        GameStateDTO gameState = createGameState();

        // Spielsituation simulieren
        gameState.getBoardSpaces().get(9).getCamelIds().add(1); // Feld 9: Kamel 1
        gameState.getBoardSpaces().get(8).getCamelIds().add(3); // Feld 8: Kamel 3 (unter Kamel 1)

        int camelId = engineParticipant.calculateCamelToBetOn(gameState);

        assertEquals(1, camelId);
    }

    @Test
    public void testCalculateCamelToBetOnWithStack() {
        EngineParticipant engineParticipant = new EngineParticipant(1);
        GameStateDTO gameState = createGameState();

        gameState.getBoardSpaces().get(9).getCamelIds().add(2); // Feld 9: Kamel 2 (unten)
        gameState.getBoardSpaces().get(9).getCamelIds().add(1); // Feld 9: Kamel 1 (oben)

        int camelId = engineParticipant.calculateCamelToBetOn(gameState);

        assertEquals(1, camelId); // Oberstes Kamel sollte ausgewählt werden
    }


    @Test
    public void testCalculateAllMovedCamels() {
        EngineParticipant engineParticipant = new EngineParticipant(1);
        GameStateDTO gameState = createGameState();

        List<Integer> movedCamels = engineParticipant.calculateAllMovedCamels(gameState);

        assertTrue(movedCamels.isEmpty());
    }



    @Test
    public void testCalculateAllCamelIds() {
        EngineParticipant engineParticipant = new EngineParticipant(1);
        GameStateDTO gameState = createGameState();

        List<Integer> camelIds = engineParticipant.calculateAllCamelIds(gameState);

        assertEquals(Arrays.asList(0, 1, 2, 3, 4), camelIds);
    }

    @Test
    public void testCalculateCamelsAbleToMove() {
        EngineParticipant engineParticipant = new EngineParticipant(1);
        GameStateDTO gameState = createGameState();

        List<Integer> camelsAbleToMove = engineParticipant.calculateCamelsAbleToMove(gameState);

        assertEquals(Arrays.asList(0, 1, 2, 3, 4), camelsAbleToMove);
    }

    @Test
    public void testFindCurrentFirstPlaceField() {
        EngineParticipant engineParticipant = new EngineParticipant(1);
        GameStateDTO gameState = createGameState();
        gameState.getBoardSpaces().get(9).getCamelIds().add(1);

        int firstPlaceField = engineParticipant.findCurrentFirstPlaceField(gameState);

        assertEquals(9, firstPlaceField);
    }

    @Test
    public void testFindCurrentFirstPlaceFieldWithBackwardCamels() {
        EngineParticipant engineParticipant = new EngineParticipant(1);
        GameStateDTO gameState = createGameState();

        gameState.getBoardSpaces().get(9).getCamelIds().add(-1); // Rückwärts laufendes Kamel
        gameState.getBoardSpaces().get(8).getCamelIds().add(1); // Normales Kamel

        int firstPlaceField = engineParticipant.findCurrentFirstPlaceField(gameState);

        assertEquals(8, firstPlaceField); // Rückwärts Kamele sollten ignoriert werden
    }


    @Test
    public void testCalculatePossibleLooser() {
        EngineParticipant engineParticipant = new EngineParticipant(1);
        GameStateDTO gameState = createGameState();
        gameState.getBoardSpaces().get(0).getCamelIds().add(4);

        int looserId = engineParticipant.calculatePossibleLooser(gameState);

        assertEquals(4, looserId);
    }

    @Test
    public void testCalculatePossibleLooserWithStack() {
        EngineParticipant engineParticipant = new EngineParticipant(1);
        GameStateDTO gameState = createGameState();

        gameState.getBoardSpaces().get(0).getCamelIds().add(4); // Feld 0: Kamel 4 (unten)
        gameState.getBoardSpaces().get(0).getCamelIds().add(3); // Feld 0: Kamel 3 (oben)

        int looserId = engineParticipant.calculatePossibleLooser(gameState);

        assertEquals(4, looserId); // Unterstes Kamel sollte als Looser erkannt werden
    }


//    @Test
//    public void testPlaceFinalBet() {
//        EngineParticipant engineParticipant = new EngineParticipant(1);
//
//        FinalBetDTO finalBet = engineParticipant.placeFinalBet(true, 1);
//
//        assertNotNull(finalBet);
//        assertEquals(1, finalBet.getCamelId());
//        assertEquals(1234, finalBet.getPlayerId());
//    }

    @Test
    public void testPlaceStageBet() {
        EngineParticipant engineParticipant = new EngineParticipant(1);

        StageBetDTO stageBet = engineParticipant.placeStageBet(2);

        assertNotNull(stageBet);
        assertEquals(2, stageBet.getCamelId());
    }

    private GameStateDTO createGameState() {
        GameStateDTO gameState = new GameStateDTO();
        GameConfigDTO gameConfig = new GameConfigDTO();
        gameConfig.setCamelCount(5);
        gameConfig.setDiceFacesCount(1);
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

    @Test
    public void testEngineTurnDecisionWithComplexGameState() {
        System.out.println("Running testEngineTurnDecisionWithComplexGameState...");

        // EngineParticipant erstellen
        EngineParticipant engineParticipant = new EngineParticipant(1);

        // Komplexen GameState erstellen
        GameStateDTO gameState = new GameStateDTO();

        // GameConfig mit Werten initialisieren
        GameConfigDTO gameConfig = new GameConfigDTO();
        gameConfig.setCamelCount(5);
        gameConfig.setDiceFacesCount(3);
        gameConfig.setMaxTurns(20);
        gameState.setGameConfig(gameConfig);
        gameState.setRolledDice(new ArrayList<>());
        // GameState initialisieren
        gameState.setTurns(14); // 70% des Spiels ist abgeschlossen

        // Spielfelder erstellen
        List<BoardSpaceDTO> boardSpaces = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            BoardSpaceDTO space = new BoardSpaceDTO();
            space.setSpaceId(i);
            space.setCamelIds(new ArrayList<>());
            boardSpaces.add(space);
        }

        // Kamele auf Spielfelder verteilen
        boardSpaces.get(15).getCamelIds().add(0); // Kamel 0 führt
        boardSpaces.get(14).getCamelIds().add(1); // Kamel 1 folgt
        boardSpaces.get(10).getCamelIds().add(2); // Kamel 2 ist mittendrin
        boardSpaces.get(5).getCamelIds().add(3); // Kamel 3 ist hinten
        boardSpaces.get(1).getCamelIds().add(4); // Kamel 4 ist am weitesten zurück

        gameState.setBoardSpaces(boardSpaces);

        // Final Bets initialisieren
        FinalBetsDTO finalBets = new FinalBetsDTO();
        finalBets.setFirstCamel(new ArrayList<>()); // Noch keine Wetten auf Sieger
        finalBets.setLastCamel(new ArrayList<>());  // Noch keine Wetten auf Verlierer
        gameState.setFinalBets(finalBets);

        // Engine-Aktion ausführen
        engineParticipant.engineTurnDecision(gameState);

        // Ausgabe prüfen
        System.out.println("Engine finished its turn decision.");
        // Du kannst hier weitere Assertions hinzufügen, um zu prüfen, ob die Engine das erwartete Verhalten zeigt.
        //kann später ausgebaut werden
    }

}
