package com.oasisstudios.camelupserver.domain.model.domainclasses;

public class BettingCardsDOM {
    private int camelId;
    private int amount;

    // Constructor
    public BettingCardsDOM(int camelId, int amount) {
        this.camelId = camelId;
        this.amount = amount;
    }

    // Getter for camelId
    public int getCamelId() {
        return this.camelId;
    }

    // Setter for camelId
    public void setCamelId(int camelId) {
        this.camelId = camelId;
    }

    // Getter for amount
    public int getAmount() {
        return this.amount;
    }

    // Setter for amount
    public void setAmount(int amount) {
        this.amount = amount;
    }

}

