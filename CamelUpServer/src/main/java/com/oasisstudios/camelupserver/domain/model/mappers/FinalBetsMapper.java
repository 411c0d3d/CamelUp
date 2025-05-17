package com.oasisstudios.camelupserver.domain.model.mappers;

import com.oasisstudios.camelupserver.dataaccess.dto.*;
import com.oasisstudios.camelupserver.domain.model.domainclasses.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FinalBetsMapper {

    // Convert FinalBetsDOM to FinaBets
    public static FinalBets toDTO(FinalBetsDOM finalBetsDOM) {
        if (finalBetsDOM == null) {
            return null;
        }

        FinalBets finalBets = new FinalBets();

        // Convert the lists
        finalBets.setFirstCamel(finalBetsDOM.getFirstCamelBets() != null ? finalBetsDOM.getFirstCamelBets().stream()
                        .map(FinalBetMapper::toDTO).collect(Collectors.toList()) : null);

        finalBets.setLastCamel(finalBetsDOM.getLastCamelBets() != null ? finalBetsDOM.getLastCamelBets().stream()
                        .map(FinalBetMapper::toDTO).collect(Collectors.toList()) : null);

        return finalBets;
    }

    // Convert FinalBetsDTO to FinalBets
    public static FinalBetsDOM toDomain(FinalBets finalBets) {
        if (finalBets == null) {
            return null;
        }

        // Convert the lists
        ArrayList<FinalBetDOM> firstCamel = finalBets.getFirstCamel() != null ? finalBets.getFirstCamel().stream()
                .map(FinalBetMapper::toDomain).collect(Collectors.toCollection(ArrayList::new)) : null;

        ArrayList<FinalBetDOM> lastCamel = finalBets.getLastCamel() != null ? finalBets.getLastCamel().stream()
                .map(FinalBetMapper::toDomain).collect(Collectors.toCollection(ArrayList::new)) : null;

        FinalBetsDOM finalBetsDOM = new FinalBetsDOM();
        finalBetsDOM.setFirstCamel(firstCamel);
        finalBetsDOM.setLastCamel(lastCamel);

        return finalBetsDOM;
    }

    public static void main(String[] args){

        // 1. Erstelle Beispielinstanzen von PlayerFinalBet und FinalBets
        PlayerFinalBet finalBetDTO1 = new PlayerFinalBet(1, 0);

        PlayerFinalBet finalBetDTO2 = new PlayerFinalBet(2, 2);

        PlayerFinalBet finalBetDTO3 = new PlayerFinalBet(3, 6);

        PlayerFinalBet finalBetDTO4 = new PlayerFinalBet(4, 6);

        FinalBets finalBetsDTO = new FinalBets();
        finalBetsDTO.setFirstCamel(List.of(finalBetDTO1, finalBetDTO2, finalBetDTO3)); // Wetten auf das erste Kamel
        finalBetsDTO.setLastCamel(List.of(finalBetDTO4)); // Wetten auf das letzte Kamel

        // 2. Konvertiere FinalBetsDTO zu FinalBets (Domain Objekt)
        FinalBetsDOM finalBetsFromDTO = FinalBetsMapper.toDomain(finalBetsDTO);
        System.out.println("FinalBets from DTO:");

        // Ausgabe der playerId und camelId für das erste Kamel
        System.out.println("Wetten auf das erste Kamel (Gewinner):");
        for (FinalBetDOM bet : finalBetsFromDTO.getFirstCamelBets()) {
            System.out.println("playerId: " + bet.getPlayerId() + ", camelId: " + bet.getCamelId());
        }

        // Ausgabe der playerId und camelId für das letzte Kamel
        System.out.println("Wetten auf das letzte Kamel (Verlierer):");
        for (FinalBetDOM bet : finalBetsFromDTO.getLastCamelBets()) {
            System.out.println("playerId: " + bet.getPlayerId() + ", camelId: " + bet.getCamelId());
        }

        // 3. Erstelle Beispielinstanzen von FinalBetDOM
        FinalBetDOM finalBet1 = new FinalBetDOM(6, 5);
        FinalBetDOM finalBet2 = new FinalBetDOM(7, 6);
        FinalBetDOM finalBet3 = new FinalBetDOM(7, 8);
        FinalBetDOM finalBet4 = new FinalBetDOM(7, 9);

        ArrayList<FinalBetDOM> firstCamelBets = new ArrayList<>(List.of(finalBet1));
        ArrayList<FinalBetDOM> lastCamelBets = new ArrayList<>(List.of(finalBet2, finalBet3, finalBet4));

        FinalBetsDOM finalBets = new FinalBetsDOM();
        finalBets.setFirstCamel(firstCamelBets);
        finalBets.setLastCamel(lastCamelBets);

        // 4. Konvertiere FinalBets zu FinalBetsDTO (DTO)
        FinalBets finalBetsDTOFromDomain = FinalBetsMapper.toDTO(finalBets);
        System.out.println();
        System.out.println();
        System.out.println("FinalBetsDTO from Domain:");

        // Ausgabe der playerId und camelId für das erste Kamel
        System.out.println("Wetten auf das erste Kamel (Gewinner) aus DTO:");
        for (PlayerFinalBet betDTO : finalBetsDTOFromDomain.getFirstCamel()) {
            System.out.println("playerId: " + betDTO.getPlayerId() + ", camelId: " + betDTO.getCamelId());
        }

        // Ausgabe der playerId und camelId für das letzte Kamel
        System.out.println("Wetten auf das letzte Kamel (Verlierer) aus DTO:");
        for (PlayerFinalBet betDTO : finalBetsDTOFromDomain.getLastCamel()) {
            System.out.println("playerId: " + betDTO.getPlayerId() + ", camelId: " + betDTO.getCamelId());
        }
    }
}
