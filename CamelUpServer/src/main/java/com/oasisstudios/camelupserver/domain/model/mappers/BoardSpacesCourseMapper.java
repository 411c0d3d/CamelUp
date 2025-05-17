package com.oasisstudios.camelupserver.domain.model.mappers;

import com.oasisstudios.camelupserver.dataaccess.dto.*;
import com.oasisstudios.camelupserver.domain.model.domainclasses.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BoardSpacesCourseMapper {

    public static List<BoardSpace> toDTO(BoardSpacesCourseDOM course) {
        List<BoardSpace> dtoList = new ArrayList<>();

        for(BoardSpaceDOM boardSpaceDOM : course.getCourse()) {
            BoardSpace dto = new BoardSpace();

            // Map spaceIds
            dto.setSpaceId(boardSpaceDOM.getSpaceId());

            // Map camelIds
            dto.setCamelIds(boardSpaceDOM.getCamelPack().getCamels().stream().map(CamelDOM::getId).collect(Collectors.toList()));

            // Map PlayerCard using PlayerCardMapper
            if(boardSpaceDOM.getSpaceId() > 1) {
                if(boardSpaceDOM.getPlayerCard() != null) {
                    dto.setPlayerCard(PlayerCardMapper.toDTO(boardSpaceDOM.getPlayerCard()));
                }
            }

            dtoList.add(dto);
        }

        return dtoList;
    }

//    public static BoardSpacesCourseDOM toDomain(List<BoardSpace> boardSpaces, GameConfig gameConfig) {
//        if(boardSpaces == null || boardSpaces.isEmpty()) {
//            return null;
//        }
//
////         Logge die Eingabedaten
////        for (BoardSpaceDTO dto : boardSpacesDTO) {
////            System.out.println("Converting BoardSpaceDTO with spaceId: " + dto.getSpaceId());
////        }
//
//        // Create a new BoardSpacesCourse with the size of the largest spaceId value
//        int maxSpaceId = boardSpaces.stream()
//                .mapToInt(BoardSpace::getSpaceId)
//                .max()
//                .orElseThrow(() -> new IllegalArgumentException("No spaceId found"));
//
//        // Create a new BoardSpacesCourse with the size of the provided BoardSpaceDTO list
//        BoardSpacesCourseDOM boardSpacesCourseDOM = new BoardSpacesCourseDOM(maxSpaceId + 1);
//
//        // Convert each BoardSpaceDTO to BoardSpace using BoardSpaceMapper
//        for(BoardSpace boardSpace : boardSpaces) {
//            // Convert each BoardSpaceDTO to BoardSpace
//            BoardSpaceDOM boardSpaceDOM = BoardSpaceMapper.toDomain(boardSpace, gameConfig);
////            System.out.println("Converted BoardSpace with spaceId: " + boardSpaceDOM.getSpaceId());
//
//            int index = boardSpaceDOM.getSpaceId();
//            if(index < 0 || index >= boardSpacesCourseDOM.getCourse().length) {
//                throw new IllegalArgumentException("No matching found for: " + boardSpaceDOM.getSpaceId());
//            }
//
//            // Update the course with the newly mapped BoardSpace
//            boardSpacesCourseDOM.getCourse()[index] = boardSpaceDOM;
//        }
//
//        return boardSpacesCourseDOM;
//    }



    public static void main(String[] args) {

        BoardSpacesCourseDOM boardSpacesCourse = new BoardSpacesCourseDOM(10);

        BoardSpaceDOM space0 = new BoardSpaceDOM(0);
        BoardSpaceDOM space1 = new BoardSpaceDOM(1);
        BoardSpaceDOM space2 = new BoardSpaceDOM(2);
        BoardSpaceDOM space3 = new BoardSpaceDOM(3);
        BoardSpaceDOM space4 = new BoardSpaceDOM(4);
        BoardSpaceDOM space5 = new BoardSpaceDOM(5);
        BoardSpaceDOM space6 = new BoardSpaceDOM(6);
        BoardSpaceDOM space7 = new BoardSpaceDOM(7);
        BoardSpaceDOM space8 = new BoardSpaceDOM(8);
        BoardSpaceDOM space9 = new BoardSpaceDOM(9);

        ArrayList<CamelDOM> camels1 = new ArrayList<>();
        camels1.add(new CamelDOM(-2, "#000000", CamelDOM.Direction.BACKWARDS));
        camels1.add(new CamelDOM(-1, "#FFFFFF", CamelDOM.Direction.BACKWARDS));
        camels1.add(new CamelDOM(1, "#333333", CamelDOM.Direction.FORWARDS));
        camels1.add(new CamelDOM(2, "#444444", CamelDOM.Direction.FORWARDS));
        camels1.add(new CamelDOM(3, "#555555", CamelDOM.Direction.FORWARDS));
        CamelPackDOM camelPack1 = new CamelPackDOM();
        camelPack1.setCamels(camels1);
        space0.setCamelPack(camelPack1);

        ArrayList<CamelDOM> camels2 = new ArrayList<>();
        camels2.add(new CamelDOM(4, "#111111", CamelDOM.Direction.FORWARDS));
        camels2.add(new CamelDOM(5, "#DDDDDD", CamelDOM.Direction.FORWARDS));
        camels2.add(new CamelDOM(6, "#222222", CamelDOM.Direction.FORWARDS));
        CamelPackDOM camelPack2 = new CamelPackDOM();
        camelPack2.setCamels(camels2);
        space1.setCamelPack(camelPack2);

        ArrayList<CamelDOM> camels3 = new ArrayList<>();
        camels3.add(new CamelDOM(7, "#424242", CamelDOM.Direction.FORWARDS));
        camels3.add(new CamelDOM(8, "#232323", CamelDOM.Direction.FORWARDS));
        camels3.add(new CamelDOM(9, "#121212", CamelDOM.Direction.FORWARDS));
        CamelPackDOM camelPack3 = new CamelPackDOM();
        camelPack3.setCamels(camels3);
        space2.setCamelPack(camelPack3);

        ArrayList<CamelDOM> camels4 = new ArrayList<>();
        camels4.add(new CamelDOM(10, "#878787", CamelDOM.Direction.FORWARDS));
        camels4.add(new CamelDOM(11, "#090909", CamelDOM.Direction.FORWARDS));
        CamelPackDOM camelPack4 = new CamelPackDOM();
        camelPack4.setCamels(camels4);
        space3.setCamelPack(camelPack4);

        ArrayList<CamelDOM> camels5 = new ArrayList<>();
        camels5.add(new CamelDOM(12, "#696969", CamelDOM.Direction.FORWARDS));
        camels5.add(new CamelDOM(13, "#424242", CamelDOM.Direction.FORWARDS));
        CamelPackDOM camelPack5 = new CamelPackDOM();
        camelPack5.setCamels(camels5);
        space4.setCamelPack(camelPack5);

        PlayerCardDOM playerCard1 = new PlayerCardDOM(5, PlayerCardDOM.SpacesMoved.FORWARDS);
        space5.setPlayerCard(playerCard1);
        PlayerCardDOM playerCard2 = new PlayerCardDOM(6, PlayerCardDOM.SpacesMoved.FORWARDS);
        space6.setPlayerCard(playerCard2);
        PlayerCardDOM playerCard3 = new PlayerCardDOM(7, PlayerCardDOM.SpacesMoved.FORWARDS);
        space7.setPlayerCard(playerCard3);

        boardSpacesCourse.getCourse()[0] = space0;
        boardSpacesCourse.getCourse()[1] = space1;
        boardSpacesCourse.getCourse()[2] = space2;
        boardSpacesCourse.getCourse()[3] = space3;
        boardSpacesCourse.getCourse()[4] = space4;
        boardSpacesCourse.getCourse()[5] = space5;
        boardSpacesCourse.getCourse()[6] = space6;
        boardSpacesCourse.getCourse()[7] = space7;
        boardSpacesCourse.getCourse()[8] = space8;
        boardSpacesCourse.getCourse()[9] = space9;

        var boardSpaceDTOList = toDTO(boardSpacesCourse);

        System.out.println("Converting BoardSpacesCourseDOM to a List of BoardSpace objects...");
        System.out.println();
        for (BoardSpace boardSpaceDTO : boardSpaceDTOList) {
            System.out.println("spaceId: " + boardSpaceDTO.getSpaceId());

            // Ausgabe der camelIds
            System.out.print("camelIds: ");
            for (Integer camelId : boardSpaceDTO.getCamelIds()) {
                System.out.print(camelId + " ");
            }
            System.out.println();  // Zeilenumbruch nach den camelIds

            // Ausgabe der PlayerCard, wenn vorhanden
            if (boardSpaceDTO.getPlayerCard() != null) {
                PlayerCard playerCardDTO = boardSpaceDTO.getPlayerCard();
                System.out.println("playerCard - PlayerId: " + playerCardDTO.getPlayerId() + ", SpacesMoved: " + playerCardDTO.getSpacesMoved());
            } else {
                System.out.println("playerCard: none");
            }

        }
        System.out.println();
        System.out.println();

        // Erstelle PlayerCardDTO
        PlayerCard playerCardDTO = new PlayerCard();
        playerCardDTO.setPlayerId(1);
        playerCardDTO.setSpacesMoved(1);

        // Erstelle CamelDTOs
        Camel camel1 = new Camel();
        camel1.setId(1);
        camel1.setColor("FF0000");
        Camel camel2 = new Camel();
        camel2.setId(2);
        camel2.setColor("00FF00");
        Camel camel3 = new Camel();
        camel3.setId(-1);
        camel3.setColor("0000FF");

        List<Camel> camels = new ArrayList<>();

        camels.add(camel1);
        camels.add(camel2);
        camels.add(camel3);

        // Erstelle GameConfigDTO
        GameConfig gameConfig = new GameConfig();
        gameConfig.setPlayerCount(6);
        gameConfig.setNumberOfSpaces(10);
        gameConfig.setCamels(camels);
        gameConfig.setDiceFaces(4);
        gameConfig.setNumberOfBettingCards(4);
        gameConfig.setThinkingTime(500);
        gameConfig.setVisualizationTime(60000);
        gameConfig.setIllegalMovePenalty(GameConfig.IllegalMovePenalty.FORFEIT_GAME);
        gameConfig.setMaxGameDuration(360000);
        gameConfig.setMaxTurns(100);

        // Erstelle BoardSpaceDTOs
        List<BoardSpace> boardSpaceDTOList2 = new ArrayList<>();

        BoardSpace space00 = new BoardSpace();
        space00.setSpaceId(0);
        space00.setCamelIds(List.of(1, 2)); // Kamel 1 und 2
        boardSpaceDTOList2.add(space00);

        BoardSpace space11 = new BoardSpace();
        space11.setSpaceId(1);
        space11.setCamelIds(List.of(-1)); // Kamel 3 (rückwärts)
        boardSpaceDTOList2.add(space11);

        BoardSpace space22 = new BoardSpace();
        space22.setSpaceId(2);
        space22.setPlayerCard(playerCardDTO);
        boardSpaceDTOList2.add(space22);

        // Führe die Umwandlung durch
//        BoardSpacesCourseDOM boardSpacesCourse1 = BoardSpacesCourseMapper.toDomain(boardSpaceDTOList2, gameConfig);
//
//        System.out.println("Converting a List of BoardSpace objects to BoardSpacesCourseDOM...");
//        System.out.println();
//        // Teste die korrekte Zuordnung der BoardSpaces
//        for (int i = 0; i < boardSpacesCourse1.getCourse().length; i++) {
//            BoardSpaceDOM boardSpace = boardSpacesCourse1.getCourse()[i];
//
//            System.out.println("BoardSpace " + boardSpace.getSpaceId());
//
//            // Überprüfe die Camel-Packs
//            System.out.println("Camels on BoardSpace " + boardSpace.getSpaceId() + ":");
//            for (CamelDOM camel : boardSpace.getCamelPack().getCamels()) {
//                System.out.println("Camel ID: " + camel.getId() + ", Color: " + camel.getColor() + ", Direction: " + camel.getDirection());
//            }
//
//            // Überprüfe PlayerCard (wenn vorhanden)
//            if (boardSpace.getSpaceId() > 1 && boardSpace.getPlayerCard() != null) {
//                PlayerCardDOM playerCard = boardSpace.getPlayerCard();
//                System.out.println("PlayerCard PlayerID: " + playerCard.getPlayerId() + ", SpacesMoved: " + playerCard.getSpacesMoved());
//            }
//        }

    }

}
