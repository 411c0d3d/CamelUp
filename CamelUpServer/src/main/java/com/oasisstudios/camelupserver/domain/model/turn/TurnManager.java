package com.oasisstudios.camelupserver.domain.model.turn;

import com.oasisstudios.camelupserver.dataaccess.IPlayerMove;
import com.oasisstudios.camelupserver.dataaccess.dto.*;
import com.oasisstudios.camelupserver.domain.model.domainclasses.DicePyramidDOM;
import com.oasisstudios.camelupserver.domain.model.domainclasses.GameStateDOM;
import com.oasisstudios.camelupserver.domain.model.domainclasses.RolledDiceDOM;
import com.oasisstudios.camelupserver.domain.model.handlers_managers.*;

public class TurnManager {
    private final GameStateDOM gameStateDOM;
    private final ActionValidationService validationService;
    private final PlayerRollHandler playerRollHandler;
    private final PlayerCardHandler playerCardHandler;
    private final FinalBetHandler finalBetHandler;
    private final PlayerBetHandler playerBetHandler;
    private final PlayerHandler playerHandler;
    private final DicePyramidDOM dicePyramidDOM;
    public TurnManager(GameStateDOM gameStateDOM,
                       ActionValidationService actionValidationService,
                       PlayerRollHandler playerRollHandler,
                       PlayerCardHandler playerCardHandler,
                       FinalBetHandler finalBetHandler,
                       PlayerBetHandler playerBetHandler,
                       PlayerHandler playerHandler,
                       DicePyramidDOM dicePyramidDOM) {
        this.validationService = actionValidationService;
        this.playerRollHandler = playerRollHandler;
        this.playerCardHandler = playerCardHandler;
        this.finalBetHandler = finalBetHandler;
        this.playerBetHandler = playerBetHandler;
        this.playerHandler = playerHandler;
        this.gameStateDOM = gameStateDOM;
        this.dicePyramidDOM = dicePyramidDOM;
    }

    // Sollte vom Server am Anfang des Zuges aufgerufen werden
    public boolean action(IPlayerMove playerMove, int playerId) {
        //Ich würde gerne die action/ das Warten darauf in einen anderen Threat auslagern und diesen die Zeit
        //seines Timers schlafen lassen. Kinder abzuschießen scheint leichter zu sein, als auf Zuruf eines
        //Kindes zurückzuspringen. Problem: run() Methode
        //Thread activity = new Thread();
        boolean isMoveMade = false;

        // if this is a valid player
        if(validationService.isPlayersTurn(playerId)) {
            switch (playerMove) {
                case FinalBet finalBet -> {
                    if (validationService.isPlacingFinalBetValid(playerId, finalBet.getId())) {
                        finalBetHandler.handleFinalBet(finalBet, playerId);
                        isMoveMade = true;
                    } else {
                        playerHandler.penalizeIllegalMove(playerId);
                    }
                }
                case RollDice rollDice -> {
                    if (validationService.isRollingValid(playerId)) {
                        RolledDiceDOM dice = dicePyramidDOM.roll();
                        playerRollHandler.handlePlayerRoll(dice, playerId);
                        isMoveMade = true;
                    } else {
                        playerHandler.penalizeIllegalMove(playerId);
                    }
                }
                case PlacePlayerCard placePlayerCard -> {
                    if (validationService.isPlacingPlayerCardValid(placePlayerCard, playerId)) {
                        playerCardHandler.placePlayerCard(placePlayerCard, playerId);
                        isMoveMade = true;
                    } else {
                        playerHandler.penalizeIllegalMove(playerId);
                    }
                }
                case StageBet stageBet -> {
                    if (validationService.isPlacingBettingCardValid(stageBet, playerId)) {
                        playerBetHandler.handleStageBet(stageBet, playerId);
                        isMoveMade = true;
                    } else {
                        playerHandler.penalizeIllegalMove(playerId);
                    }
                }
                default -> {
                    isMoveMade = false;
                    playerHandler.penalizeIllegalMove(playerId);
                    throw new IllegalArgumentException("Unknown move type: " + playerMove.getClass().getName());
                }
            }
            gameStateDOM.incrementTurnCount();
        }else {
            playerHandler.penalizeIllegalMove(playerId);
        }

        return isMoveMade;
    }
}