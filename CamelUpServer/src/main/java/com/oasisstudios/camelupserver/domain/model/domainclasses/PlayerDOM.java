package com.oasisstudios.camelupserver.domain.model.domainclasses;

import java.util.ArrayList;
import java.util.List;

public class PlayerDOM {
    private int playerId;
    private String name;
    private int money;
    private ArrayList<BettingCardDOM> bettingCards;
    private int numberOfPlacedBettingCardsInGame;
    private PlayerState state;  // PlayerDOM state as an enum

    // Enum for PlayerState
    public enum PlayerState {
        PLAYING("playing"),              // The player is actively participating in the game
        CURRENT_STAGE_FORFEIT("currentStageForfeit"), // The player is excluded from the next stage's ranking
        GAME_FORFEIT("gameForfeit"),          // The player is excluded from the game
        DISCONNECTED("disconnected");            // The player has left the game, e.g. due to losing connection

        private final String value;

        PlayerState(String value) {
            this.value = value;
        }

        // Method to get the string value of the enum
        public String getAsString() {
            return this.value;
        }
    }

    // Constructor
    public PlayerDOM(int playerId, String name, int money, PlayerState state) {
        this.playerId = playerId;
        this.name = name;
        this.money = money;
        this.bettingCards = new ArrayList<>();
        this.numberOfPlacedBettingCardsInGame = 0; // keeping track of placed BettingCardsDOM per Stage for possible winner tie-breaker
        this.state = state;
    }

    // Getters and setters
    public int getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return this.money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void addToMoney(int money) {
        this.money += money;
    }

    public List<BettingCardDOM> getBettingCards() {
        return this.bettingCards;
    }

    public void setBettingCards(ArrayList<BettingCardDOM> bettingCards) {
        this.bettingCards = bettingCards;
    }

    public void addBettingCard(BettingCardDOM bettingCardDOM) {
        this.bettingCards.add(bettingCardDOM);
    }

    // Getter and setter for state (now using PlayerState enum)
    public PlayerState getState() {
        return this.state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    // keeping track of placed BettingCardsDOM per Stage for possible winner tie-breaker
    public void incrementNumberOfPlacedBettingCardsInGameBy1() {
        this.numberOfPlacedBettingCardsInGame += 1;
    }

    public void resetNumberOfPlacedBettingCardsInGame() {
        this.numberOfPlacedBettingCardsInGame = 0;
    }

    public int getNumberOfPlacedBettingCardsInGame() {
        return this.numberOfPlacedBettingCardsInGame;
    }

    // Method to get the state as a string via the enum's getAsString method
    public String getStateAsString() {
        return this.state.getAsString();  // Get the string representation from the enum
    }

}
