package com.oasisstudios.camelupserver.domain.model.domainclasses;

import java.util.ArrayList;

public class FinalBetsDOM {
    private ArrayList<FinalBetDOM> firstCamel;
    private ArrayList<FinalBetDOM> lastCamel;

    public FinalBetsDOM() {
        this.firstCamel = new ArrayList<>();
        this.lastCamel = new ArrayList<>();
    }

    public ArrayList<FinalBetDOM> getFirstCamelBets() {
        return firstCamel;
    }

    public void setFirstCamel(ArrayList<FinalBetDOM> firstCamel) {
        this.firstCamel = firstCamel;
    }

    public ArrayList<FinalBetDOM> getLastCamelBets() {
        return lastCamel;
    }

    public void setLastCamel(ArrayList<FinalBetDOM> lastCamel) {
        this.lastCamel = lastCamel;
    }
    // add a FinalBetDOM on the first camel to the FinalBetsDOM firstcamel list.
    public void addFirstCamelFinalBet(FinalBetDOM firstCamel) {
        this.firstCamel.add(firstCamel);
    }

    // add a FinalBetDOM on the last camel to the FinalBetsDOM lastcamel list.
    public void addLastCamelFinalBet(FinalBetDOM lastCamel) {
        this.lastCamel.add(lastCamel);
    }

    // Method to get all camelIds for a given playerId from both lists
    public ArrayList<Integer> getAllPlayersFinalBetsCamelIds(int playerId) {
        ArrayList<Integer> camelIds = new ArrayList<>();
        // Check in the firstCamel list
        for (FinalBetDOM bet : this.firstCamel) {
            if (bet.getPlayerId() == playerId) {
                camelIds.add(bet.getCamelId());
            }
        }
        // Check in the lastCamel list
        for (FinalBetDOM bet : this.lastCamel) {
            if (bet.getPlayerId() == playerId) {
                camelIds.add(bet.getCamelId());
            }
        }
        return camelIds;
    }

    public boolean hasFinalBetOnCamelId(int playerId, int camelId) {
        ArrayList<FinalBetDOM> firstCamelBets = getAllPlayersFirstCamelFinalBets(playerId);
        ArrayList<FinalBetDOM> lastCamelBets = getAllPlayersLastCamelFinalBets(playerId);
        boolean found = false;
        for (FinalBetDOM bet : firstCamelBets) {
            if (bet.getCamelId() == camelId) {
                found = true;
                break;
            }
        }
        for (FinalBetDOM bet : lastCamelBets) {
            if (bet.getCamelId() == camelId) {
                found = true;
                break;
            }
        }
        return found;
    }

    public void removeAllFinalBetsFromPlayer(int playerId) {
        // Remove from firstCamel list
        removeBetsFromList(this.firstCamel, playerId);

        // Remove from lastCamel list
        removeBetsFromList(this.lastCamel, playerId);
    }

    private void removeBetsFromList(ArrayList<FinalBetDOM> betsList, int playerId) {
        // Use Iterator to safely remove elements from the list while iterating
        // Remove the bet if playerId matches
        betsList.removeIf(bet -> bet.getPlayerId() == playerId);
    }

    public ArrayList<FinalBetDOM> getAllPlayersFirstCamelFinalBets(int playerId) {
        ArrayList<FinalBetDOM> firstCamelBets = new ArrayList<>();
        // Check in the firstCamel list
        for (FinalBetDOM bet : this.firstCamel) {
            if (bet.getPlayerId() == playerId) {
                firstCamelBets.add(bet);
            }
        }
        return firstCamelBets;
    }

    public ArrayList<FinalBetDOM> getAllPlayersLastCamelFinalBets(int playerId) {
        ArrayList<FinalBetDOM> lastCamelBets = new ArrayList<>();
        // Check in the firstCamel list
        for (FinalBetDOM bet : this.lastCamel) {
            if (bet.getPlayerId() == playerId) {
                lastCamelBets.add(bet);
            }
        }
        return lastCamelBets;
    }

}

