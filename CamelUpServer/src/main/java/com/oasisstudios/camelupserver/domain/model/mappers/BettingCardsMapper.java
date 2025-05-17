package com.oasisstudios.camelupserver.domain.model.mappers;

import com.oasisstudios.camelupserver.domain.model.domainclasses.BettingCardsDOM;
import com.oasisstudios.camelupserver.dataaccess.dto.BettingCards;

public class BettingCardsMapper {

    public static BettingCards toDTO(BettingCardsDOM bettingCardsDOM) {
        if (bettingCardsDOM == null) {
            return null;
        }

        BettingCards bettingCards = new BettingCards();
        bettingCards.setCamelId(bettingCardsDOM.getCamelId());
        bettingCards.setAmount(bettingCardsDOM.getAmount());

        return bettingCards;
    }

    public static BettingCardsDOM toDomain(BettingCards bettingCards) {
        if (bettingCards == null) {
            return null;
        }

        return new BettingCardsDOM(bettingCards.getCamelId(), bettingCards.getAmount());
    }
}
