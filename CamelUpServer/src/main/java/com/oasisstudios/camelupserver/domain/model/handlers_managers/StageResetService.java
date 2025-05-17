package com.oasisstudios.camelupserver.domain.model.handlers_managers;

import com.oasisstudios.camelupserver.domain.model.domainclasses.GameStateDOM;
import com.oasisstudios.camelupserver.domain.model.domainclasses.PlayerDOM;

import java.util.ArrayList;

// Call StageResetService after StageRatingService is done.
// resets BettingCardDOM amount to GameConfigDOM numberOfBettingCards specs
// clears BettingCardsDOM of every PlayerDOM
// clear playerCard of every PlayerDOM from boardspace
// releases player's stage forfeit penalty if so
public class StageResetService {
    GameStateDOM gameStateDOM;
    BoardSpacesCourseHelper boardSpacesCourseHelper;
    PlayerHandler playerHandler;

    public StageResetService(GameStateDOM gameStateDOM, BoardSpacesCourseHelper boardSpacesCourseHelper, PlayerHandler playerHandler) {
        this.gameStateDOM = gameStateDOM;
        this.boardSpacesCourseHelper = boardSpacesCourseHelper;
        this.playerHandler = playerHandler;
    }

    public void resetStage() {
        gameStateDOM.resetBettingCardsToAmount(); // general reset amount to GameConfigDOM numberOfBettingCards specs
        ArrayList<PlayerDOM> playerDOMS = gameStateDOM.getPlayers();
        for (PlayerDOM playerDOM : playerDOMS) {
            playerHandler.clearStageBettingCardsOfPlayer(playerDOM.getPlayerId()); // clear BettingCardsDOM of every PlayerDOM
            boardSpacesCourseHelper.removePlayersPlayerCardFromBoardSpace(playerDOM.getPlayerId()); // clear playerCard of every PlayerDOM from boardspace
            if (playerDOM.getState() == PlayerDOM.PlayerState.CURRENT_STAGE_FORFEIT) { // releases playerDOM's stage forfeit penalty if so
                playerHandler.releasePlayerStageForfeit(playerDOM.getPlayerId());
            }
        }
        gameStateDOM.getRolledDice().clear();
    }

}
