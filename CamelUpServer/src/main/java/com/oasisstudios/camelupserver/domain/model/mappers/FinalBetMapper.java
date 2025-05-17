package com.oasisstudios.camelupserver.domain.model.mappers;

import com.oasisstudios.camelupserver.domain.model.domainclasses.FinalBetDOM;
import com.oasisstudios.camelupserver.dataaccess.dto.PlayerFinalBet;

public class FinalBetMapper {

    public static PlayerFinalBet toDTO(FinalBetDOM finalBetDOM) {
        if (finalBetDOM == null) {
            return null;
        }

        return new PlayerFinalBet(finalBetDOM.getPlayerId(), finalBetDOM.getCamelId());
    }

    public static FinalBetDOM toDomain(PlayerFinalBet playerFinalBet) {
        if (playerFinalBet == null) {
            return null;
        }
        return new FinalBetDOM(playerFinalBet.getCamelId(), playerFinalBet.getPlayerId());
    }
}
