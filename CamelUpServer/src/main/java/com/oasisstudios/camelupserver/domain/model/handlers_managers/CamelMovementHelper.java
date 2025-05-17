package com.oasisstudios.camelupserver.domain.model.handlers_managers;

import com.oasisstudios.camelupserver.domain.model.domainclasses.CamelDOM;
import com.oasisstudios.camelupserver.domain.model.domainclasses.CamelDOM.Direction;
import com.oasisstudios.camelupserver.domain.model.domainclasses.CamelPackDOM;
import com.oasisstudios.camelupserver.domain.model.domainclasses.CamelPackPairDOM;
import com.oasisstudios.camelupserver.domain.model.domainclasses.PlayerCardDOM;

import java.util.List;

// CamelMovementHelper is a helper class for the RolledDiceHandler.
// it gets use by RolledDiceHandler to insert aka move the part of the camelpack of 
// the rolled camel from a given boardspace to a
// destination boarspace that is determined by the RolledDiceHandler.
// This pair of BoardSpaces is capsuled and provided as a CamelPackPairDOM to the CamelMovementHelper and also returned.
// CamelMovementHelper takes care of insertion order of special rule 32.2 if the player card spacesMoved is -1
// CamelMovementHelper takes care of insertion order of special rule 33 if in the rolled camel pack are two backwards camels.
public class CamelMovementHelper {
    // method for moving a part of camelpack from starting boardspace to the destinationboardspaces camelpack.
    public CamelPackPairDOM moveCamels(CamelPackPairDOM camelPackPairDOM, CamelDOM rolledCamelDOM,
                                       PlayerCardDOM.SpacesMoved spacesMoved) {
        if (camelPackPairDOM == null || rolledCamelDOM == null) {
            throw new IllegalArgumentException("Start pack, destination pack, and rolled camel must not be null.");
        }
        // get startCamelPackDOM and destinationCamelPackDOM from the CamelPackPairDOM.
        CamelPackDOM startCamelPackDOM = camelPackPairDOM.getStartCamelPack();
        CamelPackDOM destinationCamelPackDOM = camelPackPairDOM.getDestinationCamelPack();
        // Determine the list of camels to move based on special rules and rolled camel.
        List<CamelDOM> camelsToMove = determineCamelsToMove(startCamelPackDOM, rolledCamelDOM);
        // insert the List of camels to move into the destination CamelPackDOM, considering
        // a provided PlayerCardDOM modifier of spaces moved if a player card was placed.
        insertCamels(camelsToMove, destinationCamelPackDOM, spacesMoved);
        // Return the changed CamelPackPairDOM after the startCamelPackDOM and destinationCamelPackDOM
        // have been moved
        return camelPackPairDOM;
    }

    // Method to determine which camels to move based on SpacesMoved
    private List<CamelDOM> determineCamelsToMove(CamelPackDOM startPack, CamelDOM rolledCamelDOM) {
        // special case: check if Rule 33 is applicable and get the Camels accordingly
        if (rolledCamelDOM.isCamelType(Direction.BACKWARDS) && startPack.isMatchingSpecialRule33()) {
            return startPack.extractSpecialRule33Pack();
            // regular case: extract the camels from rolledCamelDOM to top
        } else {
            return startPack.extractTopCamelsAsListFrom(rolledCamelDOM);
        }
    }

    private void insertCamels(List<CamelDOM> camelsToMove, CamelPackDOM destinationCamelPackDOM,
                              PlayerCardDOM.SpacesMoved spacesMoved) {
        // Adjust the inserting order of camels based on SpacesMoved
        if (spacesMoved == PlayerCardDOM.SpacesMoved.BACKWARDS) {
            // insert camel pack at bottom of destinationPack
            destinationCamelPackDOM.addCamelsFromListToBottom(camelsToMove);
        } else if (spacesMoved == PlayerCardDOM.SpacesMoved.FORWARDS) {
            // Add the camels to the destination pack
            destinationCamelPackDOM.addCamelsFromListOnTop(camelsToMove);
        } else {
            // Add the camels to the destination pack
            destinationCamelPackDOM.addCamelsFromListOnTop(camelsToMove);
        }
    }
}
