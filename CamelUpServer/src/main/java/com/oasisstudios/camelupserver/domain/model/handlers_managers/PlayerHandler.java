package com.oasisstudios.camelupserver.domain.model.handlers_managers;

import com.oasisstudios.camelupserver.domain.model.domainclasses.GameStateDOM;
import com.oasisstudios.camelupserver.domain.model.domainclasses.PlayerDOM;

// PlayerHandler handles the retrieval, checking and modification of a PlayerDOM object.
// is injected with GameStateDOM to access and keep the Players updated.
// is injected with PlayerPenaltyHelper and PlayerRewardHelper.
public class PlayerHandler {
    GameStateDOM gameStateDOM;
    PlayerPenaltyHelper playerPenaltyHelper;
    TurnRewardHelper turnRewardHelper;

    public PlayerHandler(GameStateDOM gameStateDOM, PlayerPenaltyHelper playerPenaltyHelper, TurnRewardHelper turnRewardHelper) {
        this.gameStateDOM = gameStateDOM;
        this.playerPenaltyHelper = playerPenaltyHelper;
        this.turnRewardHelper = turnRewardHelper;
    }

    // check if a player has status "playing"
    private boolean checkIfPlaying(int playerId) {
        PlayerDOM thisPlayerDOM = gameStateDOM.getPlayerById(playerId);
        return thisPlayerDOM.getState() == PlayerDOM.PlayerState.PLAYING;
    }

    public void clearStageBettingCardsOfPlayer(int playerId) {
        PlayerDOM playerDOM = gameStateDOM.getPlayerById(playerId);
        playerDOM.getBettingCards().clear();
    }

    public void endCurrentPlayersTurn(int playerId) {
        if (!checkIfThisPlayerHasTurn(playerId)) {
            throw new IllegalStateException("No  players " + playerId + "turn because either not 'playing' state or not first player in players.");
        } else {
            moveFirstPlayerToEnd();
        }
    }

    private void moveFirstPlayerToEnd() {
        // remove first player
        PlayerDOM firstPlayerDOM = gameStateDOM.getPlayers().removeFirst();
        // Add the player to the end of the list
        gameStateDOM.getPlayers().add(firstPlayerDOM);
    }

    // determines the current legal player for the coming turn. Only executes, if sufficient players with State PlAYING.
    public PlayerDOM determineCurrentPlayerForTurn() {
        if (hasGameSufficientPlayingPlayers()) {
            for (PlayerDOM playerDOM : gameStateDOM.getPlayers()) {
                if (playerDOM.getState() == PlayerDOM.PlayerState.PLAYING) {
                    return playerDOM;
                } else {
                    moveFirstPlayerToEnd();
                }
            }
        } 
            return null;
//            throw new IllegalStateException("Check hasGameSufficientPlayingPlayers beforehand! hasGameSufficientPlayingPlayers is " + hasGameSufficientPlayingPlayers() + " -> no active player with state PLAYING available!");
    }

    // return false when the count of players with state playing is less than 2 and true otherwise. Useful to check
    // if the game meets minimal count of players.
    public boolean hasGameSufficientPlayingPlayers() {
        long playingCount = gameStateDOM.getPlayers().stream()
                .filter(player -> player.getState() == PlayerDOM.PlayerState.PLAYING)
                .count();
        return playingCount >= 1;
    }

    // compares the given playerId with the playerId of the current playing player.
    public boolean checkIfThisPlayerHasTurn(int playerId) {
        PlayerDOM thisPlayerDOM = gameStateDOM.getPlayerById(playerId);
        boolean isFirst = thisPlayerDOM == gameStateDOM.getFirstPlayer();
        return checkIfPlaying(playerId) && isFirst;
    }

    public void rewardStageRollToPlayer(int playerId) {
        PlayerDOM playerDOM = gameStateDOM.getPlayerById(playerId);
        turnRewardHelper.rewardStageRollToPlayer(playerDOM);
    }

    public void rewardSteppedPlayerCardToPlayer(int playerId) {
        PlayerDOM playerDOM = gameStateDOM.getPlayerById(playerId);
        turnRewardHelper.rewardSteppedPlayerCardToPlayer(playerDOM);
    }

    public void penalizePlayerDisconnect(int playerId) {
        PlayerDOM thisPlayerDOM = gameStateDOM.getPlayerById(playerId);
        this.playerPenaltyHelper.penalizePlayerDisconnect(thisPlayerDOM);
    }

    public void penalizeIllegalMove(int playerId) {
        PlayerDOM thisPlayerDOM = gameStateDOM.getPlayerById(playerId);
        this.playerPenaltyHelper.penalizeIllegalMove(thisPlayerDOM);
    }

    public void releasePlayerStageForfeit(int playerId) {
        PlayerDOM thisPlayerDOM = gameStateDOM.getPlayerById(playerId);
        this.playerPenaltyHelper.releasePlayerStageForfeit(thisPlayerDOM);
    }

    public void setPlayerStateToPlaying(int playerId) {
        PlayerDOM thisPlayerDOM = gameStateDOM.getPlayerById(playerId);
        thisPlayerDOM.setState(PlayerDOM.PlayerState.PLAYING);
    }
}

