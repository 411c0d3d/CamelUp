package com.oasisstudios.camelupserver.domain.model.domainclasses;

public class FinalBetDOM {
    private int camelId;
    private int playerId;

    // Constructor
    public FinalBetDOM(int camelId, int playerId) {
        this.camelId = camelId;
        this.playerId = playerId;
    }

    // Getter for camelId
    public int getCamelId() {
        return this.camelId;
    }

    // Setter for camelId
    public void setCamelId(int camelId) {
        this.camelId = camelId;
    }

    // Getter for playerId
    public int getPlayerId() {
        return this.playerId;
    }

    // Setter for playerId
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}


