package com.oasisstudios.camelupserver.domain.model.mappers;

import com.oasisstudios.camelupserver.domain.model.domainclasses.*;
import com.oasisstudios.camelupserver.dataaccess.dto.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class BoardSpaceMapper {


    // Convert BoardSpaceDOM to BoardSpace
    public static BoardSpace toDTO(BoardSpaceDOM boardSpaceDOM) {

        if (boardSpaceDOM == null) {
            return null;
        }

        BoardSpace boardSpace = new BoardSpace();

        // Set spaceId
        boardSpace.setSpaceId(boardSpaceDOM.getSpaceId());

        // Convert camelPack to camelIds
        if (boardSpaceDOM.getCamelPack() != null && boardSpaceDOM.getCamelPack().getCamels() != null) {
            List<Integer> camelIds = boardSpaceDOM.getCamelPack().getCamels().stream()
                    .map(CamelDOM::getId)
                    .collect(Collectors.toList());
            boardSpace.setCamelIds(camelIds);
        }

        // Convert PlayerCard using PlayerCardMapper
        if (boardSpaceDOM.getSpaceId() > 1 && boardSpaceDOM.getPlayerCard() != null) {
            PlayerCard playerCard = PlayerCardMapper.toDTO(boardSpaceDOM.getPlayerCard());
            boardSpace.setPlayerCard(playerCard);
        }

        return boardSpace;
    }

    // Needs GameConfig to convert camelIds to camelPack
    public static BoardSpaceDOM toDomain(BoardSpace boardSpace, GameConfig gameConfig) {

        if (boardSpace == null) {
            return null;
        }

        BoardSpaceDOM boardSpaceDOM = new BoardSpaceDOM(boardSpace.getSpaceId());

        // Convert camelIds to camelPack using gameConfig
        if (boardSpace.getCamelIds() != null && !boardSpace.getCamelIds().isEmpty()) {
            CamelPackDOM camelPackDOM = new CamelPackDOM();
            List<Camel> camels = gameConfig.getCamels();

            for (Integer camelId : boardSpace.getCamelIds()) {
                for (Camel camel : camels) {
                    if (camel.getId().equals(camelId)) {

                        CamelDOM.Direction direction;
                        if (camelId == -1 || camelId == -2) {
                            direction = CamelDOM.Direction.BACKWARDS;
                        } else {
                            direction = CamelDOM.Direction.FORWARDS;
                        }

                        CamelDOM camelDOM = new CamelDOM(camel.getId(), camel.getColor(), direction);
                        camelPackDOM.getCamels().add(camelDOM);
                    }
                }
            }

            boardSpaceDOM.setCamelPack(camelPackDOM);

        }

        // Convert PlayerCardDTO using PlayerCardMapper
        if (boardSpace.getSpaceId() > 1 && boardSpace.getPlayerCard() != null) {
            PlayerCardDOM playerCardDOM = PlayerCardMapper.toDomain(boardSpace.getPlayerCard());
            boardSpaceDOM.setPlayerCard(playerCardDOM);
        }

        return boardSpaceDOM;

    }

    public static void main (String[] args) {

        testToDomain();

        // Teste BoardSpaceMapper.toDTO
        testToDTO();
    }

    private static void testToDomain() {

        PlayerCard playerCard = PlayerCard.builder()
                .withPlayerId(2)
                .withSpacesMoved(-1) // Beispiel: BACKWARDS
                .build();

        // Erstelle Camels
//        Camel camel1 = Camel.builder().withId(1).withColor("FF0000").build();
//        Camel camel2 = Camel.builder().withId(2).withColor("00FF00").build();
//        Camel camel3 = Camel.builder().withId(-1).withColor("0000FF").build();
        
        Camel camel1 = new Camel(1,"FF0000");
        Camel camel2 = new Camel(2,"00FF00");
        Camel camel3 = new Camel(3,"0000FF");
        List<Camel> camels = new ArrayList<>();
        camels.add(camel1);
        camels.add(camel2);
        camels.add(camel3);
        GameConfig gameConfig = new GameConfig();
//        // Erstelle GameConfig
//        GameConfig gameConfig = GameConfig.builder()
//                .withPlayerCount(4)
//                .withNumberOfSpaces(10)
//                .withCamels(camels)
//                .withDiceFaces(3)
//                .withNumberOfBettingsCards(3)
//                .withThinkingTime(10)
//                .withVisualizationTime(12)
//                .withIllegalMovePenalty(GameConfig.IllegalMovePenalty.FORFEIT_GAME)
//                .withMaxGameDuration(2222)
//                .withMaxRounds(12)
//                .build();

        // Erstelle BoardSpaceDTO
        BoardSpace boardSpace = BoardSpace.builder()
                .withSpaceId(5)
                .withCamelIds(List.of(1, 2, -1))
                .withPlayerCard(playerCard)
                .build();

        // Wende den Mapper an
        BoardSpaceDOM boardSpaceDOM = BoardSpaceMapper.toDomain(boardSpace, gameConfig);

        // Ausgabe überprüfen
        System.out.println("Von BoardSpace zu BoardSpaceDOM: ");
        System.out.println("SpaceId: " + boardSpaceDOM.getSpaceId());
        System.out.println("Camels: ");
        for(CamelDOM camelDOM : boardSpaceDOM.getCamelPack().getCamels()) {
            System.out.println("Id: " + camelDOM.getId() + ", Color: " + camelDOM.getColor() + ", direction: " + camelDOM.getDirection());
        }
        System.out.print("PlayerCard: ");
        System.out.println("PlayerId: " + boardSpaceDOM.getPlayerCard().getPlayerId());
        System.out.println("Spaces Moved: " + boardSpaceDOM.getPlayerCard().getSpacesMoved());
        System.out.println();
    }

    private static void testToDTO() {

        // Erstelle ein CamelPack mit Camels
        CamelDOM camel1 = new CamelDOM(1, "#FF0000", CamelDOM.Direction.FORWARDS);
        CamelDOM camel2 = new CamelDOM(-1, "#0000FF", CamelDOM.Direction.BACKWARDS);
        CamelPackDOM camelPackDOM = new CamelPackDOM();
        camelPackDOM.addCamelOnTop(camel1);
        camelPackDOM.addCamelOnTop(camel2);

        // Erstelle eine PlayerCard
        PlayerCardDOM playerCardDOM = new PlayerCardDOM(1, PlayerCardDOM.SpacesMoved.FORWARDS);

        // Erstelle ein BoardSpace
        BoardSpaceDOM boardSpaceDOM = new BoardSpaceDOM(5); // spaceId = 5
        boardSpaceDOM.setCamelPack(camelPackDOM);
        boardSpaceDOM.setPlayerCard(playerCardDOM);

        // Wende den Mapper an
        BoardSpace boardSpace = BoardSpaceMapper.toDTO(boardSpaceDOM);

        // Ausgabe überprüfen
        System.out.println("Von BoardSpaceDOM zu BoardSpace: ");
        System.out.println("SpaceId: " + boardSpace.getSpaceId());
        System.out.println("Camels: ");
        for(int id : boardSpace.getCamelIds()) {
            System.out.println("Id: " + id);
        }
        System.out.println("PlayerCard: ");
        System.out.println("PlayerId: " + boardSpace.getPlayerCard().getPlayerId());
        System.out.println("Spaces Moved: " + boardSpace.getPlayerCard().getSpacesMoved());
    }
}
