package com.oasisstudios.camelupserver.domain.model.handlers_managers;

import com.oasisstudios.camelupserver.domain.model.domainclasses.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// BoardSpacesCourseManager maintains and provides the BoardSpacesCourseDOM with all the Boardspaces
public class BoardSpacesCourseHelper {
    private BoardSpacesCourseDOM boardSpacesCourseDOM;
    private Map<CamelDOM, BoardSpaceDOM> camelToSpacesLookupMap;
    private ArrayList<BoardSpaceDOM> playerCardBoardSpacesLookupList;

    // Constructor
    public BoardSpacesCourseHelper(BoardSpacesCourseDOM boardSpacesCourseDOM) {
        this.boardSpacesCourseDOM = boardSpacesCourseDOM;
        this.camelToSpacesLookupMap = new HashMap<>();
        this.playerCardBoardSpacesLookupList = new ArrayList<>();
    }

    public void removePlayersPlayerCardFromBoardSpace(int playerId) {
        BoardSpaceDOM playerCardBoardSpace = null;
        for (BoardSpaceDOM boardSpaceDOM : playerCardBoardSpacesLookupList) {
            if (boardSpaceDOM.getPlayerCard().getPlayerId() == playerId) {
                playerCardBoardSpace = boardSpaceDOM;
                // remove playerCard of player from BoardSpaceDOM
                boardSpaceDOM.removePlayerCard();
            }
        }
        if (boardSpacesCourseDOM != null) {
            // remove players entry from playerCardBoardSpacesLookupList after removing all playerCard from BoardSpaceDOM
            playerCardBoardSpacesLookupList.remove(playerCardBoardSpace);
        }

    }

    public boolean hasPlayerAlreadyPlacedCard(int playerId) {
        for (BoardSpaceDOM boardSpaceDOM : playerCardBoardSpacesLookupList) {
            if (boardSpaceDOM.getPlayerCard().getPlayerId() == playerId) {
                return true;
            }
        }
        return false;
    }

    public BoardSpaceDOM getLastBoardSpace() {
        return boardSpacesCourseDOM.getBoardSpaceById(boardSpacesCourseDOM.getCourse().length - 1);
    }

    public BoardSpaceDOM getStartBoardSpace() {
        return boardSpacesCourseDOM.getStartBoardSpace();
    }

    public void placePlayerCardOnBoardSpace(int boardSpaceId, PlayerCardDOM playerCardDOM) {
        BoardSpaceDOM boardSpaceDOM = this.boardSpacesCourseDOM.getBoardSpaceById(boardSpaceId);
        boardSpaceDOM.setPlayerCard(playerCardDOM);
        this.playerCardBoardSpacesLookupList.add(boardSpaceDOM);
        // GameStateDOM update
    }

    public boolean isFinishLineReached() {
        return this.boardSpacesCourseDOM.hasFinishLineCamels();
    }

    public void removeAllPlayerCardsFromBoardSpaces() {
        for (BoardSpaceDOM boardSpaceDOM : playerCardBoardSpacesLookupList) {
            if (boardSpaceDOM.hasPlayerCard()) {
                boardSpaceDOM.removePlayerCard();
                ;
            } else {
                throw new IllegalStateException("boardSpaceDOM " + boardSpaceDOM.getSpaceId() + " in playerCardBoardSpaces has no playercard!");
            }
        }
    }

    public BoardSpaceDOM getBoardSpaceById(int startBoardSpaceId) {
        return boardSpacesCourseDOM.getBoardSpaceById(startBoardSpaceId);
    }

    public BoardSpaceDOM getRolledCamelBoardSpace(CamelDOM rolledCamelDOM) {
        if (!camelToSpacesLookupMap.containsKey(rolledCamelDOM)) {
            throw new IllegalArgumentException("CamelDOM not initialized on a board space!");
        }
        return camelToSpacesLookupMap.get(rolledCamelDOM);
    }

    public void updateCamelToSpacesMapEntries(BoardSpaceDOM boardSpaceDOM) {
        for (CamelDOM camelDOM : boardSpaceDOM.getCamelPack().getCamels()) {
            camelToSpacesLookupMap.put(camelDOM, boardSpaceDOM);
        }
    }

    public void removeCamelMapEntry(CamelDOM camelDOM) {
        camelToSpacesLookupMap.remove(camelDOM);
    }

    public BoardSpaceDOM getDestinationBoardspace(BoardSpaceDOM startBoardSpaceDOM, int offset) {
        return boardSpacesCourseDOM.getDestinationBoardspace(startBoardSpaceDOM, offset);
    }

    public BoardSpaceDOM getFinishlineBoardspace() {
        return boardSpacesCourseDOM.getFinishlineBoardSpace();
    }


    public CamelDOM getLeadingCamel() {
        // Iterate the array in reverse (start from the last index
        for (int i = boardSpacesCourseDOM.getCourse().length - 1; i >= 0; i--) {
            BoardSpaceDOM boardSpaceDOM = boardSpacesCourseDOM.getBoardSpaceById(i);

            // Check if the CamelPackDOM is not empty
            if (boardSpaceDOM.getCamelPack().getCamels() != null && !boardSpaceDOM.getCamelPack().getCamels().isEmpty()) {
                List<CamelDOM> forwardCamels = boardSpaceDOM.getCamelPack().getForwardsCamelsInOrder();
                if (!forwardCamels.isEmpty()) {
                    // Get the first CamelDOM and return it
                    return forwardCamels.getLast();
                }
            }
        }
        throw new IllegalStateException("No BoardSpaceDOM has been initialized with forwards camels! Could not find a leading camel!");
    }

    // needs testing and maybe some rework because ugly
    public CamelDOM getSecondPlaceCamel() {
        boolean foundWinner = false;
        // Iterate the array in reverse (start from the last index
        for (int i = boardSpacesCourseDOM.getCourse().length - 1; i >= 0; i--) {
            BoardSpaceDOM boardSpaceDOM = boardSpacesCourseDOM.getBoardSpaceById(i);
            // Check if the CamelPackDOM exists and is not empty
            if (boardSpaceDOM.getCamelPack().getCamels() != null && !boardSpaceDOM.getCamelPack().getCamels().isEmpty()) {
                // Only then get a list of the forwards camels of the camel pack. winning camels can only be forwards camels
                List<CamelDOM> forwardCamels = boardSpaceDOM.getCamelPack().getForwardsCamelsInOrder();
                // if the winning camel has not been seen yet to determine second place, there are two possible cases:
                if (!foundWinner) {
                    // if forwardCamels List has at least two camels, the second place winner is the second camel from end of Array List
                    if (forwardCamels.size() > 1) {
                        // Get the second first CamelDOM and return it
                        return forwardCamels.get(forwardCamels.size() - 2);
                        // there is only one forward camel, that must be the winning camel, set foundWinner to true
                    } else if (forwardCamels.size() == 1) {
                        foundWinner = true;
                        // foundWinner has not size > 1 camels, not size == 1 camels, so must be empty -> ignore it
                    } else {
                        continue;
                    }
                } // the winning camel has been seen, the next camel on top of a camel pack must be the second winner
                else {
                    if (!forwardCamels.isEmpty()) {
                        return forwardCamels.getLast();
                    }
                }
            }
        }

        throw new IllegalStateException("No BoardSpaceDOM has been initialized with forwards camels! Could not find a leading camel!");
    }


    public CamelDOM getLosingCamel() {
        // Iterate the boardSpacesCourse from the start (first index)
        for (int i = 0; i < boardSpacesCourseDOM.getCourse().length; i++) {
            BoardSpaceDOM boardSpaceDOM = boardSpacesCourseDOM.getBoardSpaceById(i);
            // Check if the CamelPackDOM is not empty
            if (boardSpaceDOM.getCamelPack().getCamels() != null && !boardSpaceDOM.getCamelPack().getCamels().isEmpty()) {
                List<CamelDOM> forwardCamels = boardSpaceDOM.getCamelPack().getForwardsCamelsInOrder();
                if (!forwardCamels.isEmpty()) {
                    // Get the first CamelDOM and return it
                    return boardSpaceDOM.getCamelPack().getCamels().getFirst();
                }
            }
        }
        throw new IllegalStateException("No BoardSpaceDOM has been initialized with forwards camels! Could not find a leading camel!");
    }

}