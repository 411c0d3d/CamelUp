package com.oasisstudios.camelupserver.domain.model.handlers_managers;
import com.oasisstudios.camelupserver.dataaccess.dto.PlacePlayerCard;
import com.oasisstudios.camelupserver.dataaccess.dto.StageBet;

// ActionValidationService methods take a player action and a playerId and check
// if the conditions are legit to perform that action.
public class ActionValidationService {
    PlayerHandler playerHandler;
    FinalBetHandler finalBetHandler;
    PlayerBetHandler bettingCardHandler;
    PlayerCardHandler playerCardHandler;


    public ActionValidationService(PlayerHandler playerHandler, FinalBetHandler finalBetHandler, PlayerBetHandler playerBetHandler, PlayerCardHandler playerCardHandler) {
        this.playerHandler = playerHandler;
        this.finalBetHandler = finalBetHandler;
        this.bettingCardHandler = playerBetHandler;
        this.playerCardHandler = playerCardHandler;
    }

    public boolean isPlacingFinalBetValid(int playerId, int camelId) {
        return isPlayersTurn(playerId) && !finalBetHandler.hasPlayerAlreadyFinalBetOnCamelId(playerId, camelId);
    }

    public boolean isPlacingBettingCardValid(StageBet stageBet, int playerId) {
        return isPlayersTurn(playerId) && bettingCardHandler.hasBetAmountAvailable(stageBet);
    }
    public boolean isRollingValid(int playerId) {
        return isPlayersTurn(playerId);
    }

    public boolean isPlacingPlayerCardValid(PlacePlayerCard placePlayerCard, int playerId) {
        return isPlayersTurn(playerId) && playerCardHandler.isPlayerCardPlaceable(placePlayerCard, playerId);
    }

    public boolean isPlayersTurn(int playerId) {
        return playerHandler.checkIfThisPlayerHasTurn(playerId);
    }
}
