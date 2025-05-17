package com.oasisstudios.camelupserver.domain.model.handlers_managers;

import com.oasisstudios.camelupserver.dataaccess.dto.*;
import com.oasisstudios.camelupserver.domain.model.domainclasses.FinalBetDOM;
import com.oasisstudios.camelupserver.domain.model.domainclasses.GameStateDOM;

// FinalBetHandler handles the placement of a FinalBetDOM by a player.
// Also checks if a player has already placed a finalBet on a certain camelId
// is injected with FinalBetsManager to keep the FinalBetsDOM updated.
public class FinalBetHandler {
    private GameStateDOM gameStateDOM;

    // Constructor to initialize the ArrayLists
    public FinalBetHandler(GameStateDOM gameStateDOM) {
        this.gameStateDOM = gameStateDOM;
    }

    public void handleFinalBet(FinalBet finalBet,int playerId) {
        boolean isCamelFirst = finalBet.getIsFirst();
        int camelId = finalBet.getId();
        // check if player already has a final bet on this camel placed
        if (hasPlayerAlreadyFinalBetOnCamelId(camelId, playerId)) {
            throw new IllegalArgumentException("player" + playerId + "already has finalbet on camelId " + camelId);
        }
        // final bets are only allowed for forward camels, which have id's >= 0
        else if (camelId == -1 || camelId == -2) {
            throw new IllegalArgumentException("Invalid camelId: " + camelId);
        }

        // else create a FinalBetDOM and add it to the FinalBetsDOM List, as well to the dedicated hashmap for faster lookup / checking
        else {
            FinalBetDOM finalBetDOM = new FinalBetDOM(camelId, playerId);
            if (isCamelFirst) {
                this.gameStateDOM.getFinalBets().addFirstCamelFinalBet(finalBetDOM);
            } else {
                this.gameStateDOM.getFinalBets().addLastCamelFinalBet(finalBetDOM);
            }
        }
    }

    //
    public boolean hasPlayerAlreadyFinalBetOnCamelId(int camelId, int playerId) {
        return gameStateDOM.getFinalBets().hasFinalBetOnCamelId(playerId, camelId);
    }


}