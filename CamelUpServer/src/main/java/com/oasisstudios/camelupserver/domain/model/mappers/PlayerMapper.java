package com.oasisstudios.camelupserver.domain.model.mappers;

import com.oasisstudios.camelupserver.dataaccess.dto.BettingCard;
import com.oasisstudios.camelupserver.domain.model.domainclasses.BettingCardDOM;
import com.oasisstudios.camelupserver.domain.model.domainclasses.PlayerDOM;
import com.oasisstudios.camelupserver.dataaccess.dto.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerMapper {

    // Convert PlayerDOM to Player
    public static Player toDTO(PlayerDOM playerDOM) {
        if (playerDOM == null) {
            return null;
        }

        Player player = new Player();
        player.setPlayerId(playerDOM.getPlayerId());
        player.setName(playerDOM.getName());
        player.setMoney(playerDOM.getMoney());

        // Convert list of BettingCardDOM to list of BettingCard using BettingCardMapper
        List<BettingCard> bettingCards = playerDOM.getBettingCards().stream()
                .map(BettingCardMapper::toDTO)
                .collect(Collectors.toList());
        player.setBettingCards(bettingCards);

        // Convert PlayerState to lowercase string
        player.setState(Player.State.fromValue(playerDOM.getState().getAsString()));

        return player;
    }

    // Convert Player to PlayerDOM
    public static PlayerDOM toDomain(Player player) {
        if (player == null) {
            return null;
        }

        // Convert PlayerState string to PlayerState enum
        PlayerDOM.PlayerState playerStateDOM = PlayerDOM.PlayerState.valueOf(player
                .getState()
                .toString()
                .toUpperCase());

        // Convert list of BettingCard to list of BettingCardDOM using BettingCardMapper
        ArrayList<BettingCardDOM> bettingCardsDOM = player.getBettingCards().stream()
                .map(BettingCardMapper::toDomain)
                .collect(Collectors.toCollection(ArrayList::new));

        // Calculate numberOfPlacedBettingCardsInGame as the size of bettingCards list
//        int numberOfPlacedBettingCardsInGame = player.getBettingCards() != null
//                ? player.getBettingCards().size()
//                : 0;

        PlayerDOM playerDOM = new PlayerDOM(
                player.getPlayerId(),
                player.getName(),
                player.getMoney(),
                playerStateDOM
        );

//        for(int i = 0; i < numberOfPlacedBettingCardsInGame; i++) {}
//            playerDOM.incrementNumberOfPlacedBettingCardsInGameBy1();

        playerDOM.setBettingCards(bettingCardsDOM);

        return playerDOM;

    }

    public static void main(String[] args){
        // Erstellen von BettingCards
        BettingCard bettingCard1 = new BettingCard();
        bettingCard1.setCamelId(1);
        bettingCard1.setWorth(100);  // Wert der Karte

        BettingCard bettingCard2 = new BettingCard();
        bettingCard2.setCamelId(2);
        bettingCard2.setWorth(200);  // Wert der Karte

        // Erstellen des Players mit einer Liste von BettingCardDTOs
        List<BettingCard> bettingCards = List.of(bettingCard1, bettingCard2);

        Player player = new Player();
        player.setPlayerId(1);
        player.setName("Hans");
        player.setMoney(1000);
        player.setBettingCards(bettingCards);
        player.setState(Player.State.PLAYING);

        // Erstellen von BettingCardDOM-Objekten
        BettingCardDOM bettingCardDOM1 = new BettingCardDOM(1, 100);  // Camel 1, Wert 100
        BettingCardDOM bettingCardDOM2 = new BettingCardDOM(2, 200);  // Camel 2, Wert 200

        // Erstellen des Player-Objekts mit einer Liste von BettingCards
        ArrayList<BettingCardDOM> bettingCardsDOM = new ArrayList<>();
        bettingCardsDOM.add(bettingCardDOM1);
        bettingCardsDOM.add(bettingCardDOM2);

        // Erstellen des Player-Objekts
        PlayerDOM playerDOM = new PlayerDOM(1, "Hans", 1000, PlayerDOM.PlayerState.PLAYING);

        playerDOM.setBettingCards(bettingCardsDOM);

        // Konvertiere Player zu PlayerDOM
        PlayerDOM playerFromDTO = PlayerMapper.toDomain(player);
        System.out.println("Player from DTO: " + "Id: " + playerFromDTO.getPlayerId() + playerFromDTO.getName() + ", Money: " + playerFromDTO.getMoney());
        System.out.println("BettingCards: ");
        for(BettingCardDOM bettingCardDOM : playerFromDTO.getBettingCards()) {
            System.out.println("CamelID: " + bettingCardDOM.getCamelId());
            System.out.println("Worth: " + bettingCardDOM.getWorth());
        }
        System.out.println("Number of placed BettingCards: " + playerFromDTO.getNumberOfPlacedBettingCardsInGame());
        System.out.println("State: " + playerFromDTO.getState());

        // Konvertiere Player zu PlayerDTO
        System.out.println();
        Player playerDTOFromDomain = PlayerMapper.toDTO(playerDOM);
        System.out.println("PlayerDTO from domain: " + "Id: " + playerDTOFromDomain.getPlayerId() + playerDTOFromDomain.getName() + ", Money: " + playerDTOFromDomain.getMoney());
        for(BettingCard bettingCard : playerDTOFromDomain.getBettingCards()) {
            System.out.println("CamelID: " + bettingCard.getCamelId());
            System.out.println("Worth: " + bettingCard.getWorth());
        }
        System.out.println("State: " + playerDTOFromDomain.getState());

    }

}
