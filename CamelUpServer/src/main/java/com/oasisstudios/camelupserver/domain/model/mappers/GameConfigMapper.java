package com.oasisstudios.camelupserver.domain.model.mappers;

import com.oasisstudios.camelupserver.dataaccess.dto.Camel;
import com.oasisstudios.camelupserver.domain.model.domainclasses.CamelDOM;
import com.oasisstudios.camelupserver.domain.model.domainclasses.GameConfigDOM;
import com.oasisstudios.camelupserver.dataaccess.dto.GameConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameConfigMapper {

    // Convert GameConfigDOM to GameConfig
    public static GameConfig toDTO(GameConfigDOM gameConfigDOM) {
        if (gameConfigDOM == null) {
            return null;
        }

        GameConfig gameConfig = new GameConfig();
        gameConfig.setPlayerCount(gameConfigDOM.getPlayerCount());
        gameConfig.setNumberOfSpaces(gameConfigDOM.getNumberOfSpaces());
        gameConfig.setDiceFaces(gameConfigDOM.getDiceFaces());
        gameConfig.setNumberOfBettingCards(gameConfigDOM.getNumberOfBettingCards());
        gameConfig.setThinkingTime(gameConfigDOM.getThinkingTime());
        gameConfig.setVisualizationTime(gameConfigDOM.getVisualizationTime());

        // Map GameConfigDOM.Penalty to GameConfig.IllegalMovePenalty
        GameConfig.IllegalMovePenalty illegalMovePenalty = null;
        if (gameConfigDOM.getIllegalMovePenalty() != null) {
            illegalMovePenalty = switch (gameConfigDOM.getIllegalMovePenalty()) {
                case FORFEIT_CURRENT_STAGE -> GameConfig.IllegalMovePenalty.FORFEIT_CURRENT_STAGE;
                case FORFEIT_GAME -> GameConfig.IllegalMovePenalty.FORFEIT_GAME;
            };
        }

        gameConfig.setIllegalMovePenalty(illegalMovePenalty);
        gameConfig.setMaxGameDuration(gameConfigDOM.getMaxGameDuration());
        gameConfig.setMaxTurns(gameConfigDOM.getMaxTurns());

        // Convert Camels with CamelMapper
        List<Camel> camels = gameConfigDOM.getCamels().stream().map(CamelMapper::toDTO).collect(Collectors.toList());
        gameConfig.setCamels(camels);

        return gameConfig;
    }

    // Convert GameConfig to GameConfigDOM
    public static GameConfigDOM toDomain(GameConfig gameConfig) {
        if (gameConfig == null) {
            return null;
        }

        // Convert Camels with CamelMapper
        ArrayList<CamelDOM> camelsDOM = gameConfig.getCamels().stream()
                .map(CamelMapper::toDomain)
                .collect(Collectors.toCollection(ArrayList::new));

        // Map GameConfig.IllegalMovePenalty to GameConfigDOM.Penalty
        GameConfigDOM.Penalty penalty = null;
        if (gameConfig.getIllegalMovePenalty() != null) {
            penalty = switch (gameConfig.getIllegalMovePenalty()) {
                case FORFEIT_CURRENT_STAGE -> GameConfigDOM.Penalty.FORFEIT_CURRENT_STAGE;
                case FORFEIT_GAME -> GameConfigDOM.Penalty.FORFEIT_GAME;
            };
        }

        return new GameConfigDOM(
                gameConfig.getPlayerCount(),
                gameConfig.getNumberOfSpaces(),
                camelsDOM,
                gameConfig.getDiceFaces(),
                gameConfig.getNumberOfBettingCards(),
                gameConfig.getThinkingTime(),
                gameConfig.getVisualizationTime(),
                penalty,
                gameConfig.getMaxGameDuration(),
                gameConfig.getMaxTurns()
        );
    }

    public static void main(String[] args) {

        // Test toDTO

        CamelDOM camelDOM1 = new CamelDOM(1, "FF0000", CamelDOM.Direction.FORWARDS);
        CamelDOM camelDOM2 = new CamelDOM(2, "0000FF", CamelDOM.Direction.FORWARDS);
        CamelDOM camelDOM3 = new CamelDOM(-1, "00FF00", CamelDOM.Direction.BACKWARDS);
        ArrayList<CamelDOM> camelsDOM = new ArrayList<>();
        camelsDOM.add(camelDOM1);
        camelsDOM.add(camelDOM2);
        camelsDOM.add(camelDOM3);

        GameConfigDOM gameConfigDOM = new GameConfigDOM(6, 10, camelsDOM, 4, 4, 500, 60000, GameConfigDOM.Penalty.FORFEIT_GAME, 360000, 100);

        GameConfig gameConfigFromDOM = toDTO(gameConfigDOM);

        System.out.println("Von GameConfigDOM zu GameConfig");
        System.out.println("Playercount (6): " + gameConfigFromDOM.getPlayerCount());
        System.out.println("Number of Spaces (10): " + gameConfigFromDOM.getNumberOfSpaces());
        System.out.println("Camels");
        for(Camel camel : gameConfigFromDOM.getCamels()) {
            System.out.println("Id: " + camel.getId() + ", Color: " + camel.getColor());
        }
        System.out.println("Dice faces (4): " + gameConfigFromDOM.getDiceFaces());
        System.out.println("Number of Bettingcards (4): " + gameConfigFromDOM.getNumberOfBettingCards());
        System.out.println("Thinking time (500): " + gameConfigFromDOM.getThinkingTime());
        System.out.println("Visualization time (60000): " + gameConfigFromDOM.getVisualizationTime());
        System.out.println("Illegal move penalty (forfeitgame): " + gameConfigFromDOM.getIllegalMovePenalty());
        System.out.println("Max game duration (360000): " + gameConfigFromDOM.getMaxGameDuration());
        System.out.println("Max turns (100): " + gameConfigFromDOM.getMaxTurns());
        System.out.println();

        // Test toDomain

        Camel camel1 = new Camel();
        camel1.setId(1);
        camel1.setColor("424242");
        Camel camel2 = new Camel();
        camel2.setId(2);
        camel2.setColor("818181");
        Camel camel3 = new Camel();
        camel3.setId(-1);
        camel3.setColor("696969");

        List<Camel> camels = new ArrayList<>();
        camels.add(camel1);
        camels.add(camel2);
        camels.add(camel3);

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

        GameConfigDOM gameConfigFromDTO = toDomain(gameConfig);

        System.out.println("Von GameConfig zu GameConfigDOM");
        System.out.println("Playercount (6): " + gameConfigFromDTO.getPlayerCount());
        System.out.println("Number of Spaces (10): " + gameConfigFromDTO.getNumberOfSpaces());
        System.out.println("Camels");
        for(CamelDOM camelDOM : gameConfigFromDTO.getCamels()) {
            System.out.println("Id: " + camelDOM.getId() + ", Color: " + camelDOM.getColor() + ", Direction: " + camelDOM.getDirection());
        }
        System.out.println("Dice faces (4): " + gameConfigFromDTO.getDiceFaces());
        System.out.println("Number of Bettingcards (4): " + gameConfigFromDTO.getNumberOfBettingCards());
        System.out.println("Thinking time (500): " + gameConfigFromDTO.getThinkingTime());
        System.out.println("Visualization time (60000): " + gameConfigFromDTO.getVisualizationTime());
        System.out.println("Illegal move penalty (GAME_FORFEIT): " + gameConfigFromDTO.getIllegalMovePenalty());
        System.out.println("Max game duration (360000): " + gameConfigFromDTO.getMaxGameDuration());
        System.out.println("Max turns (100): " + gameConfigFromDTO.getMaxTurns());


    }

}
