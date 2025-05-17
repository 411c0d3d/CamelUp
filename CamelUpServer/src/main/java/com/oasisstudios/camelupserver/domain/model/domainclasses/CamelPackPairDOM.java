package com.oasisstudios.camelupserver.domain.model.domainclasses;

public class CamelPackPairDOM {

    private final CamelPackDOM startCamelPackDOM;
    private final CamelPackDOM destinationCamelPackDOM;

    // Constructor with explicit validation
    public CamelPackPairDOM(CamelPackDOM startCamelPackDOM, CamelPackDOM destinationCamelPackDOM) {
        this.startCamelPackDOM = startCamelPackDOM;
        this.destinationCamelPackDOM = destinationCamelPackDOM;

        if (startCamelPackDOM == null || destinationCamelPackDOM == null) {
            throw new IllegalArgumentException("CamelDOM packs must not be null.");
        }
    }

    public CamelPackDOM getStartCamelPack() {
        return this.startCamelPackDOM;
    }

    public CamelPackDOM getDestinationCamelPack() {
        return this.destinationCamelPackDOM;
    }
}