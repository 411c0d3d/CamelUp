package com.oasisstudios.camelupserver.domain.model.domainclasses;

import java.util.ArrayList;

public class GameConfigDOM {

    // Enum for illegal move penalties
    public enum Penalty {
        FORFEIT_CURRENT_STAGE("currentStageForfeit"), // The player is excluded from the next stage's ranking
        FORFEIT_GAME("gameForfeit");          // The player is excluded from the game

        private final String value;

        Penalty(String value) {
            this.value = value;
        }

        // Method to get the string value of the enum
        public String getAsString() {
            return this.value;
        }
    }

    private final int playerCount;
    private final int numberOfSpaces;
    private final ArrayList<CamelDOM> camels;
    private final int diceFaces;
    private final int numberOfBettingCards;
    private final int thinkingTime;
    private final int visualizationTime;
    private final Penalty illegalMovePenalty; // Changed to use enum Penalty
    private final int maxGameDuration;
    private final int maxTurns;

    // Constructor with the given restrictions
    public GameConfigDOM(int playerCount, int numberOfSpaces, ArrayList<CamelDOM> camels, int diceFaces,
                         int numberOfBettingCards, int thinkingTime, int visualizationTime,
                         Penalty illegalMovePenalty, int maxGameDuration, int maxTurns) {

        this.playerCount = playerCount;
        this.numberOfSpaces = numberOfSpaces;
        this.camels = camels;
        this.diceFaces = diceFaces;
        this.numberOfBettingCards = numberOfBettingCards;
        this.thinkingTime = thinkingTime;
        this.visualizationTime = visualizationTime;
        this.illegalMovePenalty = illegalMovePenalty;
        this.maxGameDuration = maxGameDuration;
        this.maxTurns = maxTurns;
    }

    public int getPlayerCount() {
        return this.playerCount;
    }

    public int getNumberOfSpaces() {
        return this.numberOfSpaces;
    }

    public ArrayList<CamelDOM> getCamels() {
        return this.camels;
    }

    public CamelDOM getCamelById(int camelId) {
        return this.camels.stream().filter(camel -> camel.getId() == camelId).findFirst().orElse(null); // Return null if camel is not found
    }

    public int getDiceFaces() {
        return this.diceFaces;
    }

    public int getNumberOfBettingCards() {
        return this.numberOfBettingCards;
    }

    public int getThinkingTime() {
        return this.thinkingTime;
    }

    public int getVisualizationTime() {
        return this.visualizationTime;
    }

    // Changed to return the enum value as a string
    public Penalty getIllegalMovePenalty() {
        return this.illegalMovePenalty;
    }

    public int getMaxGameDuration() {
        return this.maxGameDuration;
    }

    public int getMaxTurns() {
        return this.maxTurns;
    }


}