package com.oasisstudios.camelupserver.domain.model.mappers;

import com.oasisstudios.camelupserver.domain.model.domainclasses.*;
import com.oasisstudios.camelupserver.dataaccess.dto.*;

import java.util.*;
import java.util.stream.Collectors;

public class GameStateMapper {

    // Convert GameState to GameStateDOM
    public static GameStateDOM initializeGameStateDOM(GameState dto) {

        // Convert Camels into a map of id -> Camel
        Map<Integer, Camel> camelMap = dto.getGameConfig().getCamels().stream()
                .collect(Collectors.toMap(Camel::getId, camel -> camel));

        // Map RolledDiceDTOs to RolledDice objects
        ArrayList<RolledDiceDOM> rolledDice = new ArrayList<>();
        if(dto.getRolledDice() != null) {
             rolledDice = dto.getRolledDice().stream()
                    .map(rolledDiceDTO -> {
                        Camel camel = camelMap.get(rolledDiceDTO.getCamelId());
                        if (camel == null) {
                            throw new IllegalArgumentException("No matching Camel found for camelId: " + rolledDiceDTO.getCamelId());
                        }
                        return RolledDiceMapper.toDomain(rolledDiceDTO, camel);
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        // Convert a list of BettingCards to ArrayList of BettingCardsDOM
        ArrayList<BettingCardsDOM> bettingCards = dto.getBettingCards().stream()
                .map(BettingCardsMapper::toDomain)  // Jeder BettingCardsDTO wird in BettingCards konvertiert
                .collect(Collectors.toCollection(ArrayList::new)); // Sammle die konvertierten Objekte in eine ArrayList

        // Convert gamePhase
        GameStateDOM.GamePhase gamePhase = null;
        if (dto.getGamePhase() != null) {
            gamePhase = switch (dto.getGamePhase()) {
                case CREATED -> GameStateDOM.GamePhase.CREATED;
                case PLAYING -> GameStateDOM.GamePhase.PLAYING;
                case VISUALIZING -> GameStateDOM.GamePhase.VISUALIZING;
                case PAUSED -> GameStateDOM.GamePhase.PAUSED;
                case FINISHED -> GameStateDOM.GamePhase.FINISHED;
            };
        }

        // Convert Players
        ArrayList<PlayerDOM> playerDOMs = dto.getPlayers().stream()
                .map(PlayerMapper::toDomain)
                .collect(Collectors.toCollection(ArrayList::new));

        GameStateDOM gameStateDOM = new GameStateDOM(
                gamePhase,
                GameConfigMapper.toDomain(dto.getGameConfig()),
                dto.getTurns(),
                dto.getGameDuration(),
                dto.getMoveTimeRemaining()
        );

        gameStateDOM.setRolledDice(rolledDice);
        gameStateDOM.setBettingCards(bettingCards);
        gameStateDOM.setPlayers(playerDOMs);
        gameStateDOM.setFinalBets(FinalBetsMapper.toDomain(dto.getFinalBets()));

        return gameStateDOM;
    }

    // Convert GameState to GameStateDTO
    public static GameState toDTO(GameStateDOM domain) {
        GameState dto = new GameState();

        // Convert gamePhase
        GameState.GamePhase gamePhase = null;
        if (domain.getGamePhase() != null) {
            gamePhase = switch (domain.getGamePhase()) {
                case CREATED -> GameState.GamePhase.CREATED;
                case PLAYING -> GameState.GamePhase.PLAYING;
                case VISUALIZING -> GameState.GamePhase.VISUALIZING;
                case PAUSED -> GameState.GamePhase.PAUSED;
                case FINISHED -> GameState.GamePhase.FINISHED;
            };
        }

        dto.setGamePhase(gamePhase);
        dto.setBoardSpaces(BoardSpacesCourseMapper.toDTO(domain.getBoardSpacesCourse()));
        dto.setGameConfig(GameConfigMapper.toDTO(domain.getGameConfigDOM()));
        dto.setRolledDice(domain.getRolledDice().stream()
                        .map(RolledDiceMapper::toDTO)
                        .collect(Collectors.toList()));
        dto.setBettingCards(domain.getBettingCards().stream()
                        .map(BettingCardsMapper::toDTO)
                        .collect(Collectors.toList()));
        dto.setPlayers(domain.getPlayers().stream()
                        .map(PlayerMapper::toDTO)
                        .collect(Collectors.toList()));
        dto.setTurns(domain.getTurns());
        dto.setGameDuration(domain.getGameDuration());
        dto.setMoveTimeRemaining(domain.getMoveTimeRemaining());
        dto.setFinalBets(FinalBetsMapper.toDTO(domain.getFinalBets()));

        return dto;
    }

    public static void main(String[] args) {
        // Beispiel für Domain-Objekte



        // Step 1: Initialize the minimum of camels
        ArrayList<CamelDOM> camels = new ArrayList<>();
        camels.add(new CamelDOM(-2, "#000000", CamelDOM.Direction.BACKWARDS));
        camels.add(new CamelDOM(-1, "#FFFFFF", CamelDOM.Direction.BACKWARDS));
        camels.add(new CamelDOM(0, "#333333", CamelDOM.Direction.FORWARDS));
        camels.add(new CamelDOM(1, "#444444", CamelDOM.Direction.FORWARDS));
        int additionalForwardCamels = 32000;
        // Step 2: Add more camels with positive integer IDs (incremented by +1) and direction FORWARDS
        for (int i = 2; i <= additionalForwardCamels; i++) {
            camels.add(new CamelDOM(i, "#"+String.format("%06x", (int)(Math.random() * 0xFFFFFF)), CamelDOM.Direction.FORWARDS));
        }

        int numberOfSpaces = 32000;
        GameConfigDOM gameConfigDOM = new GameConfigDOM(6,numberOfSpaces,camels,300,10000,0, 0, GameConfigDOM.Penalty.FORFEIT_GAME, 36000, 1000);
        GameStateDOM gameStateDOM = new GameStateDOM(GameStateDOM.GamePhase.PLAYING,gameConfigDOM,0,0,0);

        // Erstelle BettingCards of GameState
        ArrayList<BettingCardsDOM> bettingCards = new ArrayList<>();
        int numberOfBettingCards = camels.size();
        int amount = 5;
        // Step 2: Add more camels with positive integer IDs (incremented by +1) and direction FORWARDS
        for (int i = 0; i < numberOfBettingCards; i++) {
            bettingCards.add(new BettingCardsDOM(i, amount));
        }

        // Füge jedem Player BettingCard hinzu
        ArrayList<PlayerDOM> players = new ArrayList<>();
        for (PlayerDOM player : gameStateDOM.getPlayers()) {
            for (int i = 0; i < numberOfBettingCards; i++) {
            player.getBettingCards().add(new BettingCardDOM(0,0));
        }
        }

        DicePyramidDOM pyramidDom = new DicePyramidDOM(gameStateDOM);
        pyramidDom.initialLineupRefill();
        while (!pyramidDom.isEmpty()) {
            gameStateDOM.getRolledDice().add(pyramidDom.roll());
        }

        // method to distribute pairs of camels on boardspaces
        int numberOfCamels = gameStateDOM.getGameConfigDOM().getCamels().size()-1;
        for (BoardSpaceDOM boardSpaceDOM : gameStateDOM.getBoardSpacesCourse().getCourse()) {
            if (numberOfCamels-2 < 1) {
                break;
            } else {
            boardSpaceDOM.getCamelPack().getCamels().add(camels.get(numberOfCamels)); // Add the pair to the pairsLis
                boardSpaceDOM.getCamelPack().getCamels().add(camels.get(numberOfCamels-1)); // Add the pair to the pairsLis
            numberOfCamels -= 2;
        }
        }

        // add maximum amount of possible final bets
        for (PlayerDOM player : gameStateDOM.getPlayers()) {
            for (CamelDOM camel : gameStateDOM.getGameConfigDOM().getCamels()) {
                gameStateDOM.getFinalBets().getFirstCamelBets().add(new FinalBetDOM(camel.getId(), player.getPlayerId()));
            }
        }



        // Start the timer after initializing the whole GameStateDOM
        long startTime = System.nanoTime();

        // Konvertiere GameState zu GameStateDTO
        GameState gameStateDTO = GameStateMapper.toDTO(gameStateDOM);

        // Ausgabe der DTO-Werte zur Kontrolle
//        System.out.println("Converted GameState to GameStateDTO");
//        System.out.println("GameStateDTO:");
//        System.out.println("Game Phase: " + gameStateDTO.getGamePhase());
//        System.out.println("Turns: " + gameStateDTO.getTurns());
//        System.out.println("Game Duration: " + gameStateDTO.getGameDuration());
//        System.out.println("Move Time Remaining: " + gameStateDTO.getMoveTimeRemaining());
//        System.out.println("");
//        // BoardSpaces anzeigen
//        System.out.println("BoardSpaces");
//        for (BoardSpace boardSpaceDTO : gameStateDTO.getBoardSpaces()) {
//            System.out.println("BoardSpace ID: " + boardSpaceDTO.getSpaceId());
//            System.out.println("Camels: " + boardSpaceDTO.getCamelIds());
//        }
//
//        // BettingCards anzeigen
//        System.out.println("BettingCards:");
//        for (BettingCards bettingCardsDTO : gameStateDTO.getBettingCards()) {
//            System.out.println("CamelId: " + bettingCardsDTO.getCamelId() + ", Amount: " + bettingCardsDTO.getAmount());
//        }
//
//        // GameConfig anzeigen
//        System.out.println("");
//        System.out.println("GameConfig:");
//        System.out.println("PlayerCount: " + gameStateDTO.getGameConfig().getPlayerCount());
//        System.out.println("Number of spaces: " + gameStateDTO.getGameConfig().getNumberOfSpaces());
//        System.out.println("Dice faces: " + gameStateDTO.getGameConfig().getDiceFaces());
//        System.out.println("Number of bettingcards: " + gameStateDTO.getGameConfig().getNumberOfBettingCards());
//        System.out.println("Thinking time: " + gameStateDTO.getGameConfig().getThinkingTime());
//        System.out.println("visualizationTime: " + gameStateDTO.getGameConfig().getVisualizationTime());
//        System.out.println("Illegal move penalty: " + gameStateDTO.getGameConfig().getIllegalMovePenalty());
//        System.out.println("Camels:");
//        for(Camel camelDTO : gameStateDTO.getGameConfig().getCamels()) {
//            System.out.println("CamelID: " + camelDTO.getId());
//            System.out.println("Color: " + camelDTO.getColor());
//        }
//        System.out.println("Max Game Duration: " + gameStateDTO.getGameConfig().getMaxGameDuration());
//        System.out.println("Max turns: " + gameStateDTO.getGameConfig().getMaxTurns());

        // Beispiel für DTO Objekte
//
//        // Erstelle CamelDTOs
//        Camel camel4 = new Camel();
//        camel4.setId(4);
//        camel4.setColor("FF0000");
//
//        Camel camel5 = new Camel();
//        camel5.setId(5);
//        camel5.setColor("00FF00");
//
//        Camel camel6 = new Camel();
//        camel6.setId(6);
//        camel6.setColor("0000FF");
//
//        List<Camel> camelsDTO = new ArrayList<>();
//        camelsDTO.add(camel4);
//        camelsDTO.add(camel5);
//        camelsDTO.add(camel6);
//
//        // Erstelle BettingCard Objekte
//        BettingCard bettingCard4 = new BettingCard();
//        bettingCard4.setCamelId(4);
//        bettingCard4.setWorth(100);
//
//        BettingCard bettingCard5 = new BettingCard();
//        bettingCard5.setCamelId(5);
//        bettingCard5.setWorth(200);
//
//        BettingCard bettingCard6 = new BettingCard();
//        bettingCard6.setCamelId(4);
//        bettingCard6.setWorth(100);
//
//        // Erstelle bettingCards mit einer Liste von BettingCards
//        BettingCards bettingCards4 = new BettingCards(4, 3);
//        BettingCards bettingCards5 = new BettingCards(5, 4);
//        List<BettingCards> bettingCardsDTO = new ArrayList<>();
//        bettingCardsDTO.add(bettingCards4);
//        bettingCardsDTO.add(bettingCards5);
//
//        // Erstelle PlayerCard
//        PlayerCard playerCard = new PlayerCard();
//        playerCard.setPlayerId(2);
//        playerCard.setSpacesMoved(1);
//
//        // Erstelle BoardSpaces
//        BoardSpace boardSpace1 = new BoardSpace();
//        boardSpace1.setSpaceId(0);
//        boardSpace1.setCamelIds(List.of(4, 5));
//
//        BoardSpace boardSpace2 = new BoardSpace();
//        boardSpace2.setSpaceId(1);
//        boardSpace2.setCamelIds(List.of(6));
//
//        BoardSpace boardSpace3 = new BoardSpace();
//        boardSpace3.setSpaceId(2);
//        boardSpace3.setPlayerCard(playerCard);
//
//        List<BoardSpace> boardSpaces = new ArrayList<>();
//        boardSpaces.add(boardSpace1);
//        boardSpaces.add(boardSpace2);
//        boardSpaces.add(boardSpace3);
//
//        // Erstelle GameConfigDTO
//        GameConfig gameConfigDTO = new GameConfig();
//        gameConfigDTO.setPlayerCount(2);
//        gameConfigDTO.setNumberOfSpaces(10);
//        gameConfigDTO.setDiceFaces(4);
//        gameConfigDTO.setNumberOfBettingCards(4);
//        gameConfigDTO.setThinkingTime(600);
//        gameConfigDTO.setVisualizationTime(30);
//        gameConfigDTO.setIllegalMovePenalty(GameConfig.IllegalMovePenalty.FORFEIT_CURRENT_STAGE);
//        gameConfigDTO.setMaxGameDuration(360000);
//        gameConfigDTO.setMaxTurns(10);
//        gameConfigDTO.setCamels(camelsDTO);
//
//        // Erstelle RolledDiceDTO
//        RolledDice rolledDice2 = new RolledDice();
//        rolledDice2.setCamelId(4);
//        rolledDice2.setNumber(3);
//
//        List<RolledDice> rolledDiceListDTO = new ArrayList<>();
//        rolledDiceListDTO.add(rolledDice2);
//
//        // Erstelle PlayerDTO
//        Player player1DTO = new Player();
//        player1DTO.setPlayerId(1);
//        player1DTO.setName("Hans");
//        player1DTO.setMoney(1000);
//        player1DTO.setState(Player.State.PLAYING);
//        player1DTO.setBettingCards(List.of(bettingCard4, bettingCard5));
//
//        Player player2DTO = new Player();
//        player2DTO.setPlayerId(2);
//        player2DTO.setName("Fritz");
//        player2DTO.setMoney(1250);
//        player2DTO.setState(Player.State.PLAYING);
//        player2DTO.setBettingCards(List.of(bettingCard6));
//
//        List<Player> playersDTO = new ArrayList<>();
//        playersDTO.add(player1DTO);
//        playersDTO.add(player2DTO);
//
//        // Erstelle FinalBetsDTO
//        FinalBets finalBetsDTO = new FinalBets();
//
//        // Erstelle die Liste für das erste Kamel
//        List<PlayerFinalBet> firstCamelBetsDTO = new ArrayList<>();
//        PlayerFinalBet firstBetDTO = new PlayerFinalBet(1, 4);
//        firstCamelBetsDTO.add(firstBetDTO); // Beispielwert
//        finalBetsDTO.setFirstCamel(firstCamelBetsDTO);
//
//        // Erstelle die Liste für das letzte Kamel
//        List<PlayerFinalBet> lastCamelBetsDTO = new ArrayList<>();
//        PlayerFinalBet lastBetDTO = new PlayerFinalBet(2, 5);
//        lastCamelBetsDTO.add(lastBetDTO);  // Beispielwert
//        finalBetsDTO.setLastCamel(lastCamelBetsDTO);
//
//
//        // Erstelle GameStateDTO
//        GameState gameStateDTO1 = new GameState();
//        gameStateDTO1.setGamePhase(GameState.GamePhase.PLAYING);
//        gameStateDTO1.setBoardSpaces(boardSpaces);
//        gameStateDTO1.setGameConfig(gameConfigDTO);
//        gameStateDTO1.setRolledDice(rolledDiceListDTO);
//        gameStateDTO1.setBettingCards(bettingCardsDTO);
//        gameStateDTO1.setPlayers(playersDTO);
//        gameStateDTO1.setTurns(1);
//        gameStateDTO1.setGameDuration(3600000);
//        gameStateDTO1.setMoveTimeRemaining(30000);
//        gameStateDTO1.setFinalBets(finalBetsDTO);
//
//        GameStateDOM gameState1 = GameStateMapper.initializeGameStateDOM(gameStateDTO1);
//
//        // Ausgabe zur Kontrolle
//        System.out.println("---------------------------------");
//        System.out.println("Converted GameStateDTO to GameState");
//        System.out.println("GameState:");
//        System.out.println("BoardSpacesCourse: ");
//        for(BoardSpaceDOM boardSpace123 : gameState1.getBoardSpacesCourse().getCourse()) {
//            System.out.println("");
//            System.out.println("Spaceid: " + boardSpace123.getSpaceId());
//            for(CamelDOM camel321 : boardSpace123.getCamelPack().getCamels()) {
//                System.out.println("CamelId: " + camel321.getId());
//                System.out.println("Color: " + camel321.getColor());
//                System.out.println("Direction: " + camel321.getDirection());
//            }
////            if(boardSpace123.getSpaceId()>1) {
////                System.out.println("PlayerCard");
////                System.out.println("PlayerId: " + boardSpace123.getPlayerCard().getPlayerId());
////                System.out.println("Spaces moved: " + boardSpace123.getPlayerCard().getSpacesMoved());
////            }
//        }
//        System.out.println("");
//        System.out.println("Gameconfig: ");
//        System.out.println("Playercount: " + gameState1.getGameConfigDOM().getPlayerCount());
//        System.out.println("Number of Spaces: " + gameState1.getGameConfigDOM().getNumberOfSpaces());
//        System.out.println("");
//        System.out.println("Camels: ");
//        for(CamelDOM camel123 : gameState1.getGameConfigDOM().getCamels()) {
//            System.out.println("CamelId: " + camel123.getId());
//            System.out.println("Color: " + camel123.getColor());
//            System.out.println("Direction: " + camel123.getDirection());
//        }
//        System.out.println("");
//        System.out.println("diceFaces: " + gameState1.getGameConfigDOM().getDiceFaces());
//        System.out.println("Number of Betting Cards: " + gameState1.getGameConfigDOM().getNumberOfBettingCards());
//        System.out.println("Thinking time: " + gameState1.getGameConfigDOM().getThinkingTime());
//        System.out.println("Visualization time: " + gameState1.getGameConfigDOM().getThinkingTime());
//        System.out.println("Illegal move penalty: " + gameState1.getGameConfigDOM().getIllegalMovePenalty());
//        System.out.println("Max game duration: " + gameState1.getGameConfigDOM().getMaxGameDuration());
//        System.out.println("Max Turns: " + gameState1.getGameConfigDOM().getMaxTurns());
//        System.out.println("");
//        System.out.println("Rolled Dice: ");
//        for(RolledDiceDOM rolledDice : gameState1.getRolledDice()) {
//            System.out.println("Rolled number: " + rolledDice.getRolledNumber());
//            System.out.println("Camel: " + rolledDice.getCamel().getId());
//        }
//        System.out.println("");
//        System.out.println("Bettingcards: " );
//        for(BettingCardsDOM bettingCards123 : gameState1.getBettingCards()) {
//            System.out.println("CamelId: " + bettingCards123.getCamelId());
//            System.out.println("Amount: " + bettingCards123.getAmount());
//        }
//        System.out.println("");
//        System.out.println("Players: ");
//        for(PlayerDOM player123 : gameState1.getPlayers()) {
//            System.out.println("PlayerId: " + player123.getPlayerId());
//            System.out.println("Name: " + player123.getName());
//            System.out.println("Money: " + player123.getMoney());
//            for(BettingCardDOM bettingCard123 : player123.getBettingCards()) {
//                System.out.println("BettingCard: ");
//                System.out.println("CamelId: " + bettingCard123.getCamelId());
//                System.out.println("Worth: " + bettingCard123.getWorth());
//            }
//            System.out.println("Number of placed BettingCards: " + player123.getNumberOfPlacedBettingCardsInGame());
//            System.out.println("State: " + player123.getState());
//        }
//        System.out.println("Turns: " + gameState1.getTurns());
//        System.out.println("gameDuration: " + gameState1.getGameDuration());
//        System.out.println("moveTimeRemaining: " + gameState1.getMoveTimeRemaining());
//        System.out.println("");
//        System.out.println("First camel Bets: ");
//        for(FinalBetDOM finalBet123 : gameState1.getFinalBets().getFirstCamelBets()) {
//            System.out.println("CamelId: " + finalBet123.getCamelId());
//            System.out.println("PlayerId: " + finalBet123.getPlayerId());
//        }
//        System.out.println("");
//        System.out.println("Last Camel Bets: ");
//        for(FinalBetDOM finalBet321 : gameState1.getFinalBets().getLastCamelBets()) {
//            System.out.println("CamelId: " + finalBet321.getCamelId());
//            System.out.println("PlayerId: " + finalBet321.getPlayerId());
//        }

        // End the timer and calculate the elapsed time
        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        // Convert duration from nanoseconds to milliseconds for easier reading
        double durationInMillis = duration / 1_000_000.0;
        System.out.println("\nTime taken for the main method: " + durationInMillis + " milliseconds");

    }

}
