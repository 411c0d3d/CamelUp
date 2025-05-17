package com.oasisstudios.camelupserver.domain.model.domainclasses;

public class RolledDiceDOM {
    private int rolledNumber;
    private CamelDOM camelDOM;

    public RolledDiceDOM(int rolledNumber, CamelDOM camelDOM) {
        this.rolledNumber = rolledNumber;
        this.camelDOM = camelDOM;
    }

    public int getRolledNumber() {
        return this.rolledNumber;
    }

    public void setRolledNumber(int rolledNumber) {
        this.rolledNumber = rolledNumber;
    }

    public CamelDOM getCamel() {
        return this.camelDOM;
    }

    public void setCamel(CamelDOM camelDOM) {
        this.camelDOM = camelDOM;
    }
}

