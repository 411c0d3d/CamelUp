package com.oasisstudios.camelupserver.domain.model.mappers;


import com.oasisstudios.camelupserver.domain.model.domainclasses.BettingCardDOM;
import com.oasisstudios.camelupserver.dataaccess.dto.BettingCard;

public class BettingCardMapper {

    // Convert BettingCardDOM to BettingCard
    public static BettingCard toDTO(BettingCardDOM bettingCardDOM) {
        if (bettingCardDOM == null) {
            return null;
        }

        return new BettingCard(bettingCardDOM.getCamelId(), bettingCardDOM.getWorth());
    }

    // Convert BettingCard to BettingCardDOM
    public static BettingCardDOM toDomain(BettingCard bettingCard) {
        if (bettingCard == null) {
            return null;
        }

        return new BettingCardDOM(bettingCard.getCamelId(), bettingCard.getWorth());
    }

}
