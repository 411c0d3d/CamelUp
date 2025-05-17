package com.oasisstudios.camelupserver.domain.model.handlers_managers;

import com.oasisstudios.camelupserver.domain.model.domainclasses.GameConfigDOM;
import com.oasisstudios.camelupserver.domain.model.domainclasses.GameStateDOM;
import com.oasisstudios.camelupserver.domain.model.domainclasses.PlayerDOM;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;


// gets used by PlayerDOM Handler.
// is injected with gameStateDOM and BoardSpacesCourseHelper.
// Gets called if a player's action was not legit.
// sets the kind of penalty state to player
// maybe needs more penalty logic, for instance if a penalty includes removing money or cards etc. --> check product vision!
public class PlayerPenaltyHelper {
    GameStateDOM gameStateDOM;
    BoardSpacesCourseHelper boardSpacesCourseHelper;
    private Logger logger = LogManager.getLogger(PlayerPenaltyHelper.class);


    public PlayerPenaltyHelper(GameStateDOM gameStateDOM, BoardSpacesCourseHelper boardSpacesCourseHelper) {
        
        this.gameStateDOM = gameStateDOM;
        this.boardSpacesCourseHelper = boardSpacesCourseHelper;
    }

    // Penalizes the playerDOM by disconnecting them (sets the state to DISCONNECTED)
    public void penalizePlayerDisconnect(PlayerDOM playerDOM) {
        if (playerDOM != null) {
            // set penalty state
            playerDOM.setState(PlayerDOM.PlayerState.DISCONNECTED);
            // remove all the players playerCards
            removePlayersPlayerCardFromBoardSpace(playerDOM.getPlayerId());
            // remove all final bets of this playerDOM
            gameStateDOM.getFinalBets().removeAllFinalBetsFromPlayer(playerDOM.getPlayerId());
        } else {
            logger.error("playerDOM to be penalized is null");
        }
    }

    public void penalizeIllegalMove(PlayerDOM playerDOM) {
        if (Objects.equals(gameStateDOM.getGameConfigDOM().getIllegalMovePenalty(), GameConfigDOM.Penalty.FORFEIT_CURRENT_STAGE)) {
            penalizePlayerStageForfeit(playerDOM);
        } else if (Objects.equals(gameStateDOM.getGameConfigDOM().getIllegalMovePenalty(), GameConfigDOM.Penalty.FORFEIT_GAME)) {
            penalizePlayerGameForfeit(playerDOM);
        }
    }

    // Penalizes the playerDOM by forfeiting them from the current stage (sets the state to CURRENT_STAGE_FORFEIT)
    private void penalizePlayerStageForfeit(PlayerDOM playerDOM) {
        if (playerDOM != null) {
            // set penalty state
            playerDOM.setState(PlayerDOM.PlayerState.CURRENT_STAGE_FORFEIT);
            // remove all the players playerCards
            removePlayersPlayerCardFromBoardSpace(playerDOM.getPlayerId());
        } else {
            logger.error("playerDOM to be penalized is null");
        }
    }


    // Penalizes the playerDOM by forfeiting them from the entire game (sets the state to GAME_FORFEIT)
    private void penalizePlayerGameForfeit(PlayerDOM playerDOM) {
        if (playerDOM != null) {
            // set penalty state
            playerDOM.setState(PlayerDOM.PlayerState.GAME_FORFEIT);
            // remove all the players playerCards
            removePlayersPlayerCardFromBoardSpace(playerDOM.getPlayerId());
            // remove all final bets of this playerDOM
            gameStateDOM.getFinalBets().removeAllFinalBetsFromPlayer(playerDOM.getPlayerId());
        } else {
            logger.error("playerDOM to be penalized is null");
        }
    }

    private void removePlayersPlayerCardFromBoardSpace(int playerId) {
        this.boardSpacesCourseHelper.removePlayersPlayerCardFromBoardSpace(playerId);
    }

    // Releases the playerDOM from the StageForfeit (sets the state to PLAYING)
    public void releasePlayerStageForfeit(PlayerDOM playerDOM) {
        if (playerDOM != null) {
            playerDOM.setState(PlayerDOM.PlayerState.PLAYING);
        } else {
            logger.error("playerDOM to be penalized is null");
        }
    }
}