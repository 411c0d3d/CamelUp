package com.oasisstudios.camelupengine.engineteilnehmer;

import com.oasisstudios.camelupengine.api.Api;
import com.oasisstudios.camelupengine.dto.*;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class EngineParticipant {

    int engineId;
    Api api;
    boolean real;

    public EngineParticipant(int engineId, Api api, boolean real) {
        this.engineId = engineId;
        this.api = api;
        this.real = real;
    }

    /**
     * engineTurnDecision decides which move the Engine Participant is going to do,
     * by splitting the game into two phases.
     * The later phase of the game is determined by the turns that have been made
     * divided by the max turns set in gameConfig,
     * which starts at 70% of maxTurns. The winnerBet/looserBet will be chosen by
     * helping methods and handed over to another
     * method that places the determined FinalBet, if not been placed yet (checks
     * current gameState).
     * otherwise a different play will be decided by a different method,
     * based on current gameState like rolling the dice in 90% of the cases or
     * placing a player card in 10%
     * 
     * @param gameState: GameStateDTO
     */
    public void engineTurnDecision(GameState gameState) {
        // TODO FOR ENIGNE TO THINK WHAT HE DOES
        int currentPhase = gameState.getTurns() / gameState.getGameConfig().getMaxTurns();
        if (currentPhase >= 0.7) {
            int winnerBet = calculateCamelToBetOn(gameState);
            int looserBet = calculatePossibleLooser(gameState);
            List<Integer> myBets = calculateYourPlacedBets(gameState);
            if (!myBets.contains(winnerBet) && myBets.contains(looserBet)) {
                boolean bet = decideFinalBet(gameState);
                if (bet) {
                    placeFinalBet(bet, winnerBet);
                } else {
                    placeFinalBet(bet, looserBet);
                }
            } else {
                if (myBets.contains(winnerBet)) {
                    if (myBets.contains(looserBet)) {
                        calculateEtappenPlay(gameState);
                    } else {
                        placeFinalBet(false, looserBet);
                    }
                } else {
                    placeFinalBet(true, winnerBet);
                }
            }
        } else {
            calculateEtappenPlay(gameState);
        }
    }

    /// +++++++++++++++++++++++++
    /// EngineStageBets++++++++++++++++++++++++++++++++++++++++++++++++++///

    // habe ich die schon benutzt, warum steht das nicht im Gamsestate aber alle
    // Kamele mit ID AAAAAAAAAAHHHHHH

    /**
     * checks if a player card is allowed to be placed on specific board space
     * 
     * @param gameState GameStateDTO
     * @return boolean
     */
    public boolean canPlacePlayerCard(GameState gameState) {
        List<BoardSpace> fields = gameState.getBoardSpaces();
        for (BoardSpace field : fields) {
            if (field.getPlayerCard() != null) {
                if (field.getPlayerCard().getPlayerId() == engineId)
                    return false;
            }

        }
        return true;
    }

    /**
     * Finds out the closest camel or Rudel towards the finish line, by calculating
     * the distance difference of the
     * current leading camel or Rudel and possible other camels or Rudels, being
     * possibly able to catch up with
     * their maximum dice power. If it's less than the difference the leading camel
     * or Rudel need to win, consider them
     * as the possible winner of the Game. If it's distance is bigger, choose
     * leading camel or Rudel to bet on.
     * 
     * @param gameState GameStateDTO
     * @return possibleWinners a list of all possible winners
     */
    public List<BoardSpace> calculatePossibleWinners(GameState gameState) {
        List<BoardSpace> possibleWinners = new ArrayList<>();
        int distance = 0;
        int powerOfRudel = 0;

        int firstPlaceField = findCurrentFirstPlaceField(gameState);

        for (int i = firstPlaceField - 1; i >= 0; i--) {
            int place = i + 1;
            BoardSpace boardSpace = gameState.getBoardSpaces().get(place);
            if (boardSpace.getCamelIds().isEmpty()) {
                distance++;
                continue;
            }
            if (distance <= powerOfRudel) {
                List<Integer> movableCamels = calculateCamelsAbleToMove(gameState);
                for (int camel : boardSpace.getCamelIds()) {
                    if (movableCamels.contains(camel)) {
                        powerOfRudel += gameState.getGameConfig().getDiceFaces();
                    }
                }
                possibleWinners.add(boardSpace);
                distance++;
            } else {
                break;
            }
        }

        return possibleWinners;
    }

    /**
     * calculates the winner of an "etappe" and for the final bet -> Miro contains
     * exact documentation on the logic
     * 
     * @param gameState GameStateDTO
     * @return winnerID: winner out of list of possible winners
     */
    public int calculateCamelToBetOn(GameState gameState) {
        int winnerId = 0;
        List<BoardSpace> possibleWinners = calculatePossibleWinners(gameState);
        if (possibleWinners.size() == 1 && possibleWinners.get(0).getCamelIds().size() == 1) {
            winnerId = possibleWinners.get(0).getCamelIds().get(0);
        } else if (possibleWinners.size() == 1 && possibleWinners.get(0).getCamelIds().size() > 1) {
            int sizeOfStack = possibleWinners.get(0).getCamelIds().size();
            winnerId = possibleWinners.get(0).getCamelIds().get(sizeOfStack - 1);
        } else if (possibleWinners.size() > 1) {
            List<Integer> movedCamels = new ArrayList<>(
                    gameState.getRolledDice().stream().map(RolledDice::getCamelId).toList());
            movedCamels.add(-1); // the backwards camels are treated like moved
            movedCamels.add(-2); // so they don't mess up stuff
            int biggestStack = 0;
            int winnerIdOfBiggestStack = 0;
            for (BoardSpace boardSpace : possibleWinners) {
                List<Integer> CamelIds = boardSpace.getCamelIds();
                CamelIds.removeAll(movedCamels);
                int powerOfRudel = CamelIds.size() * gameState.getGameConfig().getDiceFaces();
                if (powerOfRudel > biggestStack) {
                    int sizeOfStack = boardSpace.getCamelIds().size() - 1;
                    winnerIdOfBiggestStack = boardSpace.getCamelIds().get(sizeOfStack);
                    biggestStack = powerOfRudel;
                }
            }
            winnerId = winnerIdOfBiggestStack;
        }

        return winnerId;
    }

    /**
     * checks if the betting for the "Etappe" are still available and
     * places the bet or decides to go for another play if not and references the
     * methods
     * 
     * @param gameState
     */
    public void calculateBettingOrRolling(GameState gameState) {
        int bettingCamelID = calculateCamelToBetOn(gameState);
        for (BettingCards bettingCard : gameState.getBettingCards()) {
            if (bettingCard.getCamelId() == bettingCamelID && bettingCard.getAmount() > 0) {
                placeStageBet(bettingCamelID);
            } else {
                rollTheDice(gameState);
            }
        }
    }

    /**
     * finds the boardSpace with a non backwards camel on, which is closest to the
     * goal
     * 
     * @param gameState BoardSpaceDTO
     * @return boardSpaceId in question
     */
    public int findCurrentFirstPlaceField(GameState gameState) {
        boolean isNotBackwards = false; // make sure no backwards camel is used
        int boardSpaceId = 0;
        for (int i = gameState.getBoardSpaces().size() - 1; i >= 0; i--) {
            BoardSpace boardSpace = gameState.getBoardSpaces().get(i);
            if (boardSpace.getCamelIds().isEmpty()) {
                continue;
            }
            for (int camelId : boardSpace.getCamelIds()) {
                if (camelId >= 0) {
                    isNotBackwards = true;
                    break;
                }
            }
            if (isNotBackwards) {
                boardSpaceId = boardSpace.getSpaceId();
                // System.out.println("First place field found: " + boardSpaceId);
                break;
            }
        }
        return boardSpaceId;
    }

    /**
     * calculates all camels that have moved in the "Etappe"
     * 
     * @param gameState GameStateDTO
     * @return allMovedCamels List of Integer CamelIds as list
     */
    public List<Integer> calculateAllMovedCamels(GameState gameState) {
        List<Integer> allMovedCamelIds = new ArrayList<>();
        if (gameState.getRolledDice() != null) {
            for (RolledDice roll : gameState.getRolledDice()) {
                allMovedCamelIds.add(roll.getCamelId());
            }
        }
        return allMovedCamelIds;
    }

    /**
     * calculates all camels participating in the game
     * 
     * @param gameState GameStateDTO
     * @return list of Integer -> the Ids of the camels
     */
    public List<Integer> calculateAllCamelIds(GameState gameState) {
        List<Integer> allCamelIds = new ArrayList<>();
        int camelCount = gameState.getGameConfig().getCamels().size()-2;// Don't forget the backwards camels
        for (int id = -2; id < camelCount; id++) {
            allCamelIds.add(id);
        }
        return allCamelIds;
    }

    /**
     * calculates the camels still able to make a move in this "etappe"
     * 
     * @param gameState GameStateDTO
     * @return List of Integer CamelIds of the camels in question
     */
    public List<Integer> calculateCamelsAbleToMove(GameState gameState) {
        List<Integer> camelsMoved = calculateAllMovedCamels(gameState);
        List<Integer> camels = new ArrayList<>(calculateAllCamelIds(gameState));
        // List<Integer> camelsAbleToMove = List.of();
        camels.removeAll(camelsMoved);
        return camels;
    }

    /**
     * calculates a candidate to place the FinalBet isFirst=false on.
     * Looks at the board and the distance to the other camels
     * 
     * @param gameState GameSTateDTO
     * @return looserId Integer Id of the Camel to place final bet on
     */
    public int calculatePossibleLooser(GameState gameState) {
        int looserId = -1;
        int distanceTracker = 0;
        for (BoardSpace boardSpace : gameState.getBoardSpaces()) {
            if (distanceTracker > gameState.getGameConfig().getDiceFaces()) {
                break;
            }
            List<Integer> backwardsCamels = Arrays.asList(-1, -2);
            List<Integer> camels = boardSpace.getCamelIds();
            camels.removeAll(backwardsCamels); // avoid the backwards camels
            if (boardSpace.getCamelIds().isEmpty()) {
                if (looserId == -1)
                    continue;
                distanceTracker++;
            } else if (!boardSpace.getCamelIds().isEmpty()) {
                looserId = boardSpace.getCamelIds().get(0);
            }
        }

        return looserId;
    }

    /// ++++++++++++++++++++++++++++++EngineEndBet++++++++++++++++++++++++++++++++++++++++++///

    /**
     * engine logic to decide on the type of final bet/first/last).
     * Calculates the respective distance to the goal or the next camel
     * and compares it.
     * 
     * @param gameState GameStateDTO
     * @return bet boolean -> bool indictaing the type of bet
     */
    public boolean decideFinalBet(GameState gameState) {
        int winnerBet = calculateCamelToBetOn(gameState);
        int looserBet = calculatePossibleLooser(gameState);
        int distanceToWin = gameState.getGameConfig().getNumberOfSpaces() -
                (findCurrentFirstPlaceField(gameState) + 1);
        int looserField = 0;
        for (BoardSpace boardSpace : gameState.getBoardSpaces()) {
            if (boardSpace.getCamelIds().contains(looserBet)) {
                looserField = boardSpace.getSpaceId();
                break;
            }
        }
        int distanceToNextLooser = 0;
        for (int i = looserField + 1; i < gameState.getBoardSpaces().size(); i++) {
            BoardSpace field = gameState.getBoardSpaces().get(i);
            distanceToNextLooser++;
            if (!field.getCamelIds().isEmpty()) {
                break;
            }
        }
        boolean bet = true;
        if (distanceToNextLooser > distanceToWin) {
            bet = false;
        }
        return bet;

    }

    /**
     * calculates the final bets already placed by the player to avoid the penalty
     * 
     * @param gameState: GameStateDTO
     * @return placedBets: List of Integer containing the CamelIds with a placed bet
     */
    public List<Integer> calculateYourPlacedBets(GameState gameState) {
        List<Integer> placedBets = new ArrayList<>();
        List<PlayerFinalBet> winnerBets = gameState.getFinalBets().getFirstCamel();
        List<PlayerFinalBet> looserBets = gameState.getFinalBets().getLastCamel();
        winnerBets.addAll(looserBets);
        for (PlayerFinalBet finalBet : winnerBets) {
            if (finalBet.getPlayerId() == engineId) {
                placedBets.add(finalBet.getPlayerId());
            }
        }
        return placedBets;
    }

    /**
     * sends a rollDiceDTO to the Server
     * 
     * @param gameState GameStateDTO
     */
    public void rollTheDice(GameState gameState) {
        if (real) {
            api.rollDice();
        }
    }

    /**
     * sends placePlayerCardDTO to the server to place the player c
     * ard using the spot infront of the winning camel
     * 
     * @param gameState GameStateDTO
     */
    public void placePlayerCard(GameState gameState) {
        int firstCamel = findCurrentFirstPlaceField(gameState);
        int boardSpaceId = firstCamel + 2;
        // PlacePlayerCard placePlayerCard =
        // PlacePlayerCard.builder().withSpaceId(boardSpaceId).build();
        if (real) {
            api.placePlayerCard(boardSpaceId, 1);
        }

    }

    /**
     * sends FinalBetDTO to the server to place a "Endwette"
     * 
     * @param isFirst referencing first or last camel
     * @param id      the id of the camel
     * @return
     */
    public FinalBet placeFinalBet(Boolean isFirst, int id) {
        FinalBet finalBet = FinalBet.builder().withId(id).withIsFirst(isFirst).build();
        // finalBet.setIsFirst(isFirst)
        if (real) {
            api.placeFinalBet(id, isFirst);
        }
        return finalBet; // for test
    }

    /**
     * sends a StageBetDTO to the Server Interface def.
     * 
     * @param camelId
     * @return
     */
    public StageBet placeStageBet(int camelId) {
        StageBet stageBet = StageBet.builder().withCamelId(camelId).build();
        if (real) {
            api.betOnStage(camelId);
        }
        return stageBet; // for test
    }

    /**
     * Decides if the Engine is rolling or placing a player card at 70% of the
     * Etappe
     * 
     * @param gameState GameStateDTO
     * @return
     */
    private void calculateEtappenPlay(GameState gameState) {
        int camelAmount = gameState.getGameConfig().getCamels().size();
        int amountOfMovedCamels = gameState.getRolledDice().size();
        if (camelAmount * 0.7 <= amountOfMovedCamels) {
            calculateBettingOrRolling(gameState);
        } else {
            if (canPlacePlayerCard(gameState)) {
                rollTheDice(gameState);
            } else {
                double beLucky = Math.random();
                if (beLucky < 0.1) {
                    placePlayerCard(gameState);
                } else {
                    rollTheDice(gameState);
                }
            }
        }
    }

}
