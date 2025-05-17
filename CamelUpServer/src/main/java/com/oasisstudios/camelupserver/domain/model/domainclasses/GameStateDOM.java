package com.oasisstudios.camelupserver.domain.model.domainclasses;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class GameStateDOM {

    // Enum for GamePhase
    public enum GamePhase {
        CREATED("created"),
        PLAYING("playing"),
        VISUALIZING("visualizing"),
        PAUSED("paused"),
        FINISHED("finished");

        private final String phase;

        GamePhase(String phase) {
            this.phase = phase;
        }

        public String getPhaseAsString() {
            return this.phase;
        }
    }

    private GamePhase gamePhase;  // Changed from String to GamePhase

    private BoardSpacesCourseDOM boardSpacesCourseDOM;

    private GameConfigDOM gameConfigDOM;

    private ArrayList<RolledDiceDOM> rolledDice;

    private ArrayList<BettingCardsDOM> bettingCards;

    private ArrayList<PlayerDOM> players;

    private int turns;

    private int gameDuration;

    private int moveTimeRemaining;

    private FinalBetsDOM finalBetsDOM;

    public GameStateDOM(GamePhase gamePhase, GameConfigDOM gameConfigDOM,
                        int turns, int gameDuration, int moveTimeRemaining) {
        this.gamePhase = gamePhase;
        this.boardSpacesCourseDOM = new BoardSpacesCourseDOM(gameConfigDOM.getNumberOfSpaces());
        this.gameConfigDOM = gameConfigDOM;
        this.rolledDice = new ArrayList<>();
        this.bettingCards = new ArrayList<>();
        this.players = new ArrayList<>();
        this.turns = turns;
        this.gameDuration = gameDuration;
        this.moveTimeRemaining = moveTimeRemaining;
        this.finalBetsDOM = new FinalBetsDOM();
    }
    
    public GamePhase getGamePhase() {
        return this.gamePhase;
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    public BoardSpacesCourseDOM getBoardSpacesCourse() {
        return this.boardSpacesCourseDOM;
    }

    public void setBoardSpacesCourse(BoardSpacesCourseDOM boardSpacesCourseDOM) {
        this.boardSpacesCourseDOM = boardSpacesCourseDOM;
    }

    public GameConfigDOM getGameConfigDOM() {
        return this.gameConfigDOM;
    }

    public void setGameConfig(GameConfigDOM gameConfigDOM) {
        this.gameConfigDOM = gameConfigDOM;
    }

    public ArrayList<RolledDiceDOM> getRolledDice() {
        return this.rolledDice;
    }

    public void setRolledDice(ArrayList<RolledDiceDOM> rolledDiceDOM) {
        this.rolledDice = rolledDiceDOM;
    }

    public boolean isGameWithinMaxGameDuration() {
        return this.gameDuration <= this.getGameConfigDOM().getMaxGameDuration();
        
    }


    public ArrayList<BettingCardsDOM> getBettingCards() {
        return this.bettingCards;
    }

    public void setBettingCards(ArrayList<BettingCardsDOM> bettingCards) {
        this.bettingCards = bettingCards;
    }

    public void resetBettingCardsToAmount() {
        for (BettingCardsDOM bettingCard : bettingCards) {
            bettingCard.setAmount(gameConfigDOM.getNumberOfBettingCards());
        }
    }

    public void shufflePlayers() {
        Collections.shuffle(this.getPlayers());
    }

    public ArrayList<PlayerDOM> getPlayers() {
        return this.players;
    }

    public void setPlayers(ArrayList<PlayerDOM> playerDOMS) {
        this.players = playerDOMS;
    }

    public void addPlayer(PlayerDOM playerDOM) {
        players.add(playerDOM);
    }

    public void removePlayerById(int playerId) {
        players.removeIf(player -> player.getPlayerId() == playerId);
    }

    public PlayerDOM getPlayerById(int playerId) {
        for (PlayerDOM playerDOM : players) {
            if (playerDOM.getPlayerId() == playerId) {
                return playerDOM;
            }
        }
        throw new IllegalStateException("PlayerDOM with playerId " + playerId + " cannot be found in playerDOMS!");
    }

    public List<PlayerDOM> getAllPlayers() {
        return players;
    }

    public boolean isGameWithinTurnsLimit() {
        return this.turns <= gameConfigDOM.getMaxTurns();
    }

    public void incrementTurnCount() {
        this.turns += 1;
    }

    public PlayerDOM getFirstPlayer() {
        return this.players.getFirst();
    }

    public int getTurns() {
        return this.turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }

    public int getGameDuration() {
        return this.gameDuration;
    }

    public void setGameDuration(int gameDuration) {
        this.gameDuration = gameDuration;
    }

    public int getMoveTimeRemaining() {
        return this.moveTimeRemaining;
    }

    public void setMoveTimeRemaining(int moveTimeRemaining) {
        this.moveTimeRemaining = moveTimeRemaining;
    }

    public FinalBetsDOM getFinalBets() {
        return this.finalBetsDOM;
    }

    public void setFinalBets(FinalBetsDOM finalBetsDOM) {
        this.finalBetsDOM = finalBetsDOM;
    }

    public ArrayList<RolledDiceDOM> getRolledDiceList() {
        return this.rolledDice;
    }

    public void setRolledDices(ArrayList<RolledDiceDOM> rolledDiceDOMS) {
        this.rolledDice = rolledDiceDOMS;
    }

    public void addRolledDice(RolledDiceDOM rolledDiceDOM) {
        this.rolledDice.add(rolledDiceDOM);
    }

    public void clearRolledDices() {
        this.rolledDice.clear();
    }


}