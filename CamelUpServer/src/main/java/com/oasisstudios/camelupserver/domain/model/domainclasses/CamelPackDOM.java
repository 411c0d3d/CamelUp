package com.oasisstudios.camelupserver.domain.model.domainclasses;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CamelPackDOM {
    private ArrayList<CamelDOM> camels;

    // Constructor
    public CamelPackDOM() {
        this.camels = new ArrayList<>();
    }

    // Getter for camels
    public ArrayList<CamelDOM> getCamels() {
        return this.camels;
    }

    // Setter for camels
    public void setCamels(ArrayList<CamelDOM> camels) {
        this.camels = camels;
    }

    // Method to add a camelDOM on top of the pack
    public void addCamelOnTop(CamelDOM camelDOM) {
        this.camels.add(camelDOM);
    }

    // Method to add a camelDOM on top of the pack
    public void addCamelToBottom(CamelDOM camelDOM) {
        this.camels.addFirst(camelDOM);
    }

    // Method to add multiple camels from a list to the top of the pack
    public void addCamelsFromListOnTop(List<CamelDOM> camelsList) {
        this.camels.addAll(camelsList);
    }

    // Method to add multiple camels from a list to the bottom of the pack
    public void addCamelsFromListToBottom(List<CamelDOM> camelsList) {
        this.camels.addAll(0, camelsList);
    }

    // Method to extract bottom camels as a list, up to and including the given camel
    // Extracting means removing camels from original camelPack and returning as list.
    public List<CamelDOM> extractBottomCamelsAsListToIncl(CamelDOM upToAndIncludingCamelDOM) {
        List<CamelDOM> bottomPack = new ArrayList<>();
        Iterator<CamelDOM> iterator = this.camels.iterator();

        while (iterator.hasNext()) {
            CamelDOM current = iterator.next();
            bottomPack.add(current); // Add the current camel to the bottom pack
            iterator.remove();       // Remove it from the original list

            if (current.equals(upToAndIncludingCamelDOM)) {
                break; // Break after adding the specified camel
            }
        }

        // Validate if the specified camel was found and added
        if (bottomPack.isEmpty() || !bottomPack.contains(upToAndIncludingCamelDOM)) {
            throw new IllegalArgumentException("The specified camel is not in the list.");
        }

        return bottomPack;
    }

    // Method to extract top camels as a list, from the given camel downwards.
    // Extracting means removing camels from original camelPack and returning as list.
    public List<CamelDOM> extractTopCamelsAsListFrom(CamelDOM downToAndIncludingCamelDOM) {
        List<CamelDOM> topPack = new ArrayList<>();
        ListIterator<CamelDOM> iterator = this.camels.listIterator(this.camels.size());

        while (iterator.hasPrevious()) {
            CamelDOM current = iterator.previous();
            topPack.addFirst(current); // Add the current camel to the beginning of the topPack list
            iterator.remove();       // Remove it from the original list

            if (current.equals(downToAndIncludingCamelDOM)) {
                break; // Break after adding the specified camel
            }
        }

        // Validate if the specified camel was found and added
        if (topPack.isEmpty() || !topPack.contains(downToAndIncludingCamelDOM)) {
            throw new IllegalArgumentException("The specified camel is not in the list.");
        }

        return topPack;
    }

    // Method to check if the camel pack contains a camel with the given id
    public boolean containsCamelId(int camelId) {
        return this.camels.stream().anyMatch(camel -> camel.getId() == camelId);
    }

    // Method to check if the camelDOM pack contains a camelDOM with the given id
    public boolean containsCamel(CamelDOM camelDOM) {
        return this.camels.contains(camelDOM);
    }

    // The method to check if the special rule is matched
// The method to check if the special rule is matched
    public boolean isMatchingSpecialRule33() {
        return this.camels.stream().anyMatch(camel -> camel.getDirection() == CamelDOM.Direction.BACKWARDS) && this.camels.stream().anyMatch(camel -> camel.getDirection() == CamelDOM.Direction.FORWARDS);
    }


    // Method to apply Special Rule 33.
    // Extracting means removing camels from original camelPack and returning as list.
    public List<CamelDOM> extractSpecialRule33Pack() {
        // Iterate the camels list from top down to find the first camel with BACKWARDS direction
        ListIterator<CamelDOM> iterator = this.camels.listIterator(this.camels.size());
        while (iterator.hasPrevious()) {
            CamelDOM currentCamelDOM = iterator.previous();
            if (currentCamelDOM.getDirection() == CamelDOM.Direction.BACKWARDS) {
                // Call extractTopCamelsAsList with the found camel and return the result
                return extractTopCamelsAsListFrom(currentCamelDOM);
            }
        }

        // If no camel with BACKWARDS direction is found, throw an exception or return an empty list
        throw new IllegalStateException("No camel with BACKWARDS direction found.");
    }

    // Helper method to find camel index by id
    public int findCamelIndexById(int camelId) {
        for (int i = 0; i < this.camels.size(); i++) {
            if (this.camels.get(i).getId() == camelId) {
                return i;
            }
        }
        return -1; // If camelId is not found
    }

    // Helper method to find camel by id
    public CamelDOM getCamelById(int camelId) {
        return this.camels.stream().filter(camel -> camel.getId() == camelId).findFirst().orElse(null); // Return null if camel is not found
    }

    public List<CamelDOM> getForwardsCamelsInOrder() {
        List<CamelDOM> forwardsCamels = new ArrayList<>();
        // Iterate through the camel pack from bottom up
        for (CamelDOM camelDOM : camels) {
            // Check if the camelDOM is moving FORWARDS and add it to the list
            if (camelDOM.isCamelType(CamelDOM.Direction.FORWARDS)) {
                // Forward camels will still be in same order as in camelDOM pack, last camelDOM is top camelDOM.
                forwardsCamels.add(camelDOM);
            }
        }
        return forwardsCamels; // Return the list of camels with FORWARDS direction
    }

//    public static void main(String[] args) {
//
//        // TESTING ADDING AND EXTRACTING METHODS
//
//        // Create some test BoardSpaceDTO and PlayerCardDTO objects
//        BoardSpaceDOM boardSpace1 = new BoardSpaceDOM(1);
//        System.out.println("boardSpace1 after Initializing: " + boardSpace1);
//        BoardSpaceDOM boardSpace2 = new BoardSpaceDOM(2);
//        System.out.println("boardSpace1 after Initializing: " + boardSpace2);
//
//        // Create a list camels1 of camels and add them to a CamelPackDOM, then add the camelPack1 to a BoardSpaceDOM boardSpace1
//        ArrayList<CamelDOM> camels1 = new ArrayList<>();
//        camels1.add(new CamelDOM(-2, "#000000", CamelDOM.Direction.BACKWARDS));
//        camels1.add(new CamelDOM(-1, "#FFFFFF", CamelDOM.Direction.BACKWARDS));
//        camels1.add(new CamelDOM(3, "#333333", CamelDOM.Direction.FORWARDS));
//        camels1.add(new CamelDOM(4, "#444444", CamelDOM.Direction.FORWARDS));
//        camels1.add(new CamelDOM(5, "#555555", CamelDOM.Direction.FORWARDS));
//        CamelPackDOM camelPack1 = new CamelPackDOM();
//        camelPack1.setCamels(camels1);
//        boardSpace1.setCamelPack(camelPack1);
//        System.out.print("boardSpace1 CamelDOM IDs after setting camelPack1 on boardSpace1: ");
//        for (CamelDOM camel : boardSpace1.getCamelPack().getCamels()) {
//            System.out.print(camel.getId() + " ");  // Print each camel's ID followed by a space
//        }
//        System.out.println();
//
//        // Create a list camels2 of camels and add them to a CamelPackDOM, then add the camelPack2 to a BoardSpaceDOM boardSpace2
//        ArrayList<CamelDOM> camels2 = new ArrayList<>();
//        camels2.add(new CamelDOM(6, "#666666", CamelDOM.Direction.FORWARDS));
//        camels2.add(new CamelDOM(7, "#777777", CamelDOM.Direction.FORWARDS));
//        CamelPackDOM camelPack2 = new CamelPackDOM();
//        camelPack2.setCamels(camels2);
//        boardSpace2.setCamelPack(camelPack2);
//        System.out.print("boardSpace2 CamelDOM IDs after setting camelPack2 on boardSpace2: ");
//        for (CamelDOM camel : boardSpace2.getCamelPack().getCamels()) {
//            System.out.print(camel.getId() + " ");  // Print each camel's ID followed by a space
//        }
//        System.out.println();
//
//        // Create a list camels3
//        ArrayList<CamelDOM> camels3 = new ArrayList<>();
//        camels3.add(new CamelDOM(8, "#888888", CamelDOM.Direction.FORWARDS));
//        camels3.add(new CamelDOM(9, "#999999", CamelDOM.Direction.FORWARDS));
//        camels3.add(new CamelDOM(10, "#101010", CamelDOM.Direction.FORWARDS));
//        // test the addCamelsFromListOnTop method to add to camelpack to the top of boardSpace1
//        boardSpace1.getCamelPack().addCamelsFromListOnTop(camels3);
//        System.out.print("boardSpace1 CamelDOM IDs after adding new camels to top: ");
//        for (CamelDOM camel : boardSpace1.getCamelPack().getCamels()) {
//            System.out.print(camel.getId() + " ");  // Print each camel's ID followed by a space
//        }
//        System.out.println();
//        // test the extractTopCamelsAsListFrom of the previously added camels to of boardSpace1
//        List<CamelDOM> extractedCamels3 = boardSpace1.getCamelPack().extractTopCamelsAsListFrom(camels3.getFirst());
//        System.out.print("boardSpace1 CamelDOM IDs after extracting added camels3 from top again: ");
//        for (CamelDOM camel : boardSpace1.getCamelPack().getCamels()) {
//            System.out.print(camel.getId() + " ");  // Print each camel's ID followed by a space
//        }
//        System.out.println();
//        // test the addCamelsFromListOnBottom to add to the extractedCamels3 again but to bottom to camelpack of boardSpace1
//        boardSpace1.getCamelPack().addCamelsFromListToBottom(extractedCamels3);
//        System.out.print("boardSpace1 CamelDOM IDs after adding extractedCamels3 to bottom: ");
//        for (CamelDOM camel : boardSpace1.getCamelPack().getCamels()) {
//            System.out.print(camel.getId() + " ");  // Print each camel's ID followed by a space
//        }
//        System.out.println();
//
//        // NOW TESTING SPECIAL RULE 33 METHODS
//
//        // Create DiceDTO with camelId -2 (rolled with -2)
//        RolledDiceDOM dice = new RolledDiceDOM(1, new CamelDOM(-2, "#000000", CamelDOM.Direction.BACKWARDS)); // Dice rolled for camel ID -2
//        // Test the isMatchingSpecialRule33 method
//        boolean isSpecialRule33 = boardSpace1.getCamelPack().isMatchingSpecialRule33();
//        System.out.println("Does boardSpace1 match Special Rule 33? Answer is: " + isSpecialRule33);
//        // extract the SpecialRule33Pack
//        List<CamelDOM> resultAfterRule33 = boardSpace1.getCamelPack().extractSpecialRule33Pack();
//        System.out.print("Applying Special Rule 33. " + "Dice rolled camelId is " + dice.getCamel().getDirection() + " " + dice.getCamel().getId() + ". BoardSpace1 CamelDOM IDs after extracting extractSpecialRule33Pack: ");
//        for (CamelDOM camel : boardSpace1.getCamelPack().getCamels()) {
//            System.out.print(camel.getId() + " ");  // Print each camel's ID followed by a space
//        }
//        System.out.println();
//        System.out.print("extractSpecialRule33Pack is: ");
//        for (CamelDOM camel : resultAfterRule33) {
//            System.out.print(camel.getId() + " ");  // Print each camel's ID followed by a space
//        }
//        System.out.println();
//        boardSpace1.getCamelPack().addCamelsFromListToBottom(resultAfterRule33);
//        System.out.print("boardSpace1 CamelDOM IDs after randomly adding 'Special Rule 33 pack' to bottom ");
//        for (CamelDOM camel : boardSpace1.getCamelPack().getCamels()) {
//            System.out.print(camel.getId() + " ");  // Print each camel's ID followed by a space
//        }
//        System.out.println();
//
//        // extracting remaining camels from boardSpace2 and add to bottom of boardSpace1
//        CamelDOM firstCamel = boardSpace2.getCamelPack().getCamels().getLast();
//        List<CamelDOM> boardSpace2Camels = boardSpace2.getCamelPack().extractBottomCamelsAsListToIncl(firstCamel);
//        System.out.print("boardSpace2 CamelDOM IDs after extractBottomCamelsAsListToIncl: is boardSpace2 camelPack empty? ");
//        System.out.print(boardSpace2.getCamelPack().getCamels().isEmpty());  // Print each camel's ID followed by a space
//
//        System.out.println();
//        boardSpace1.getCamelPack().addCamelsFromListToBottom(boardSpace2Camels);
//        System.out.print("boardSpace1 CamelDOM IDs after randomly adding boardSpace2Camels to bottom ");
//        for (CamelDOM camel : boardSpace1.getCamelPack().getCamels()) {
//            System.out.print(camel.getId() + " ");  // Print each camel's ID followed by a space
//        }
//        System.out.println();
//
//    }
}

