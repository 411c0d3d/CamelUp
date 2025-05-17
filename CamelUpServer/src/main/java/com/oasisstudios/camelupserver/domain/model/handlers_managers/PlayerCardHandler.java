package com.oasisstudios.camelupserver.domain.model.handlers_managers;

import com.oasisstudios.camelupserver.dataaccess.dto.PlacePlayerCard;
import com.oasisstudios.camelupserver.domain.model.domainclasses.BoardSpaceDOM;
import com.oasisstudios.camelupserver.domain.model.domainclasses.PlayerCardDOM;
import com.oasisstudios.camelupserver.domain.model.domainclasses.PlayerCardDOM.SpacesMoved;

// PlayerCardHandler is used for placing a playerCard
// is injected with boardSpacesCourseHelper to keep the boardspaces updated in case
// a playercard is placed or removed etc.
public class PlayerCardHandler {
    BoardSpacesCourseHelper boardSpacesCourseHelper;

    public PlayerCardHandler(BoardSpacesCourseHelper boardSpacesCourseHelper) {
        this.boardSpacesCourseHelper = boardSpacesCourseHelper;
    }

    public void placePlayerCard(PlacePlayerCard placePlayerCard, int playerId) {
        if (!isPlayerCardPlaceable(placePlayerCard, playerId)) {
            throw new IllegalArgumentException("Placing playerCardDOM is not allowed on BoardSpaceDOM Id: " + placePlayerCard.getSpaceId());
        }
        // create playerCardDOM object to insert into defined BoardSpaceDOM
        PlayerCardDOM playerCardDOM;
        if (placePlayerCard.getMovingDirection() == -1) {
            playerCardDOM = new PlayerCardDOM(playerId, SpacesMoved.BACKWARDS);

        } else if (placePlayerCard.getMovingDirection() == 1) {
            playerCardDOM = new PlayerCardDOM(playerId, SpacesMoved.FORWARDS);

        } else {
            throw new IllegalArgumentException("placeCard has no moving direction initialized.");
        }
        // insert player card object into defined BoardSpaceDOM, update BoardSpaceDOM on the
        // course
        int boardSpaceId = placePlayerCard.getSpaceId();
        boardSpacesCourseHelper.placePlayerCardOnBoardSpace(boardSpaceId, playerCardDOM);
    }

    // checks rule (26): "Auf einem Feld kann höchstens ein Zuschauerplättchen platziert werden. Auf den beiden
    // angrenzenden Feldern darf sich kein Zuschauerplättchen befinden. Auf dem Startfeld
    // darf kein Zuschauerplättchen platziert werden."
    public boolean isPlayerCardPlaceable(PlacePlayerCard placePlayerCard, int playerId) {
        BoardSpaceDOM cardSpace =  boardSpacesCourseHelper.getBoardSpaceById(placePlayerCard.getSpaceId());
        BoardSpaceDOM prevSpaceNeighbour = boardSpacesCourseHelper.getBoardSpaceById(placePlayerCard.getSpaceId() -1);
        BoardSpaceDOM nextSpaceNeighbour;
        if (placePlayerCard.getSpaceId() < boardSpacesCourseHelper.getLastBoardSpace().getSpaceId()) {
            nextSpaceNeighbour = boardSpacesCourseHelper.getBoardSpaceById(placePlayerCard.getSpaceId() +1);
        } else {
            nextSpaceNeighbour = boardSpacesCourseHelper.getStartBoardSpace();
        }
        // check if finishline 0 or startBoardSpace 1 --> not allowed to place here
        if (cardSpace.getSpaceId() == 0 || cardSpace.getSpaceId() == 1) {
            return false;
        }
        // check whether current BoardSpaceDOM nor adjacent BoardSpaces already have a playerCard placed on them
        else if (cardSpace.hasPlayerCard() || prevSpaceNeighbour.hasPlayerCard() || nextSpaceNeighbour.hasPlayerCard()) {
            return false;
        }
        else if (boardSpacesCourseHelper.hasPlayerAlreadyPlacedCard(playerId)) {
            return false;
        } else {
            return true;
        }
    }
}