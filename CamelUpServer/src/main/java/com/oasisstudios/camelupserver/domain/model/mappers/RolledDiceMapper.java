package com.oasisstudios.camelupserver.domain.model.mappers;

import com.oasisstudios.camelupserver.dataaccess.dto.Camel;
import com.oasisstudios.camelupserver.domain.model.domainclasses.CamelDOM;
import com.oasisstudios.camelupserver.domain.model.domainclasses.RolledDiceDOM;
import com.oasisstudios.camelupserver.dataaccess.dto.RolledDice;

public class RolledDiceMapper {

    // Convert RolledDiceDOM to RolledDice
    public static RolledDice toDTO(RolledDiceDOM rolledDiceDOM) {
        if (rolledDiceDOM == null) {
            return null;
        }

        RolledDice rolledDice = new RolledDice();
        rolledDice.setCamelId(rolledDiceDOM.getCamel().getId());
        rolledDice.setNumber(rolledDiceDOM.getRolledNumber());
        return rolledDice;
    }

    // Convert RolledDice to RolledDiceDOM
    public static RolledDiceDOM toDomain(RolledDice rolledDice, Camel camel) {
        if (rolledDice == null || camel == null) {
            return null;
        }

        // Convert Camel to CamelDOM using CamelMapper
        CamelDOM camelDOM = CamelMapper.toDomain(camel);

        return new RolledDiceDOM(rolledDice.getNumber(), camelDOM);
    }
}
