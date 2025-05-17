package com.oasisstudios.camelupserver.domain.model.mappers;

import com.oasisstudios.camelupserver.domain.model.domainclasses.PlayerCardDOM;
import com.oasisstudios.camelupserver.dataaccess.dto.PlayerCard;

public class PlayerCardMapper {

    // Convert PlayerCard to PlayerCardDTO
    public static PlayerCard toDTO(PlayerCardDOM playerCardDOM) {
        if (playerCardDOM == null) {
            return null;
        }

        return new PlayerCard(playerCardDOM.getPlayerId(), playerCardDOM.getSpacesMoved().getValue());
    }

    // Convert PlayerCardDTO to PlayerCard
    public static PlayerCardDOM toDomain(PlayerCard playerCard) {
        if (playerCard == null) {
            return null;
        }

        // Get the int value from the SpacesMoved enum in DTO
        int spacesMovedValue = playerCard.getSpacesMoved();
        PlayerCardDOM.SpacesMoved spacesMoved = PlayerCardDOM.SpacesMoved.fromValue(spacesMovedValue);

        // Return the domain class instance
        return new PlayerCardDOM(playerCard.getPlayerId(), spacesMoved);
    }

    public static void main(String[] args){
        // Beispielinstanz von PlayerCardDTO erstellen
        PlayerCard playerCardB = new PlayerCard(2, -1);

        PlayerCard playerCardF = new PlayerCard(3, 1);

        // Konvertierung von PlayerCard zu PlayerCardDOM, Beispiel BACKWARDS
        PlayerCardDOM playerCardDOMB = PlayerCardMapper.toDomain(playerCardB);
        System.out.println("PlayerCard:");
        System.out.println("Player ID: " + playerCardDOMB.getPlayerId());
        System.out.println("Spaces Moved: " + playerCardDOMB.getSpacesMoved()); // Sollte SpacesMoved.BACKWARDS sein

        // Konvertierung von PlayerCard zu PlayerCardDOM, Beispiel FORWARDS
        PlayerCardDOM playerCardDOMF = PlayerCardMapper.toDomain(playerCardF);
        System.out.println("PlayerCard:");
        System.out.println("Player ID: " + playerCardDOMF.getPlayerId());
        System.out.println("Spaces Moved: " + playerCardDOMF.getSpacesMoved()); // Sollte SpacesMoved.FORWARDS sein

        // Konvertierung von PlayerCardDOM zurück zu PlayerCard, Beispiel BACKWARDS
        PlayerCard convertedPlayerCardB = PlayerCardMapper.toDTO(playerCardDOMB);
        System.out.println("\nConverted PlayerCardDTO:");
        System.out.println("Player ID: " + convertedPlayerCardB.getPlayerId());
        System.out.println("Spaces Moved: " + convertedPlayerCardB.getSpacesMoved());

        // Konvertierung von PlayerCardDOM zurück zu PlayerCard, Beispiel BACKWARDS
        PlayerCard convertedPlayerCardF = PlayerCardMapper.toDTO(playerCardDOMF);
        System.out.println("\nConverted PlayerCardDTO:");
        System.out.println("Player ID: " + convertedPlayerCardF.getPlayerId());
        System.out.println("Spaces Moved: " + convertedPlayerCardF.getSpacesMoved());
    }


}
