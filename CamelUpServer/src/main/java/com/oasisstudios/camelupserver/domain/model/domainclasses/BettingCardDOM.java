package com.oasisstudios.camelupserver.domain.model.domainclasses;

public class BettingCardDOM {
    private int camelId;
    private int worth;

    // Constructor
    public BettingCardDOM(int camelId, int worth) {
        this.camelId = camelId;
        this.worth = worth;
    }

    // Getter for camelId
    public int getCamelId() {
        return this.camelId;
    }

    // Setter for camelId
    public void setCamelId(int camelId) {
        this.camelId = camelId;
    }

    // Getter for worth
    public int getWorth() {
        return this.worth;
    }

    // Setter for worth
    public void setWorth(int worth) {
        this.worth = worth;
    }
}

