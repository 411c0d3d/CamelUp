package com.oasisstudios.camelupserver.domain.model.domainclasses;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

public class DicePyramidDOM {
    private final int diceFaces;
    private int currentIndex;
    private boolean isShuffled;
    private ArrayList<CamelDOM> shuffledCamelsArrList;
    private CamelDOM suspendedCamelDOM;
    private GameStateDOM gameStateDOM;

    // Constructor to initialize the Dice
    public DicePyramidDOM(GameStateDOM gameStateDOM) {
        this.shuffledCamelsArrList = gameStateDOM.getGameConfigDOM().getCamels(); // camels from the gameconfig
        this.diceFaces = gameStateDOM.getGameConfigDOM().getDiceFaces();
        this.currentIndex = 0;
        this.isShuffled = false;
        this.suspendedCamelDOM = null;
    }

    public CamelDOM getRandomCamel() {
        if (this.isShuffled == true) {
            if (this.currentIndex < shuffledCamelsArrList.size()) {
                CamelDOM camelDOM = shuffledCamelsArrList.get(this.currentIndex);
                this.currentIndex++; // Increment Index to next item in camelIDList
                return camelDOM;

            } else { // Exception has to be catched by Caller
                this.isShuffled = false;
                throw new IndexOutOfBoundsException(
                        "Last index of DicePyramidDOM's shuffledCamelIDsArList exceeded. Furthermore, attribute 'shuffled' is now set to 'false'");
            }
        } else {
            throw new Error("'shuffled' attribute of DicePyramidDOM instance is 'false'.");
        }
    }

    public boolean isEmpty() {
        return this.currentIndex >= this.shuffledCamelsArrList.size();
    }

    public int generateDiceFace() {
        SecureRandom randInt = new SecureRandom(); // Generate a SecureRandom Instance.
        int dots = randInt.nextInt(this.diceFaces) + 1; // Generates random int from 1 inclusive to maxDots
        // inclusive
        return dots;
    }

    public void initialLineupRefill() {
        Collections.shuffle(this.shuffledCamelsArrList);
        this.isShuffled = true;
        this.currentIndex = 0;
    }

    public void regularStageRefill() {
        if (this.suspendedCamelDOM != null) {
            this.shuffledCamelsArrList.add(this.suspendedCamelDOM);
            this.suspendedCamelDOM = null;
        }

        SecureRandom random = new SecureRandom();
        int suspendedBackwardsCamelId = (random.nextInt(2) == 0) ? -1 : -2;
        System.out.println("suspendedBackwardsCamelId " + suspendedBackwardsCamelId);
        for (CamelDOM camelDOM : this.shuffledCamelsArrList) {
            if (camelDOM.getId() == suspendedBackwardsCamelId) {
                System.out.println("FOUND CAMEL TO REMOVE : " + camelDOM.getId());
                this.suspendedCamelDOM = camelDOM;
                this.shuffledCamelsArrList.remove(camelDOM);
                break;
            }
        }
        Collections.shuffle(this.shuffledCamelsArrList);
        this.isShuffled = true;
        this.currentIndex = 0;
    }

    public RolledDiceDOM roll() {
        // get the camelID at currentIndex of shuffledCamelIDsArList
        CamelDOM camelDOM = getRandomCamel();
        // get a random number within admin configured range
        int rolledNumber = generateDiceFace();
        RolledDiceDOM newDice = new RolledDiceDOM(rolledNumber, camelDOM);
        return newDice;
    }

//    // DicePyramidDOM Class-Methods Testing.
//    public static void main(String[] args) {
//        ArrayList<CamelDOM> camels = new ArrayList<>();
//        // Add CamelDOM objects to the ArrayList
//        camels.add(new CamelDOM(-1, "#000000", Direction.BACKWARDS));
//        camels.add(new CamelDOM(0, "#4900ff", Direction.FORWARDS));
//        camels.add(new CamelDOM(-2, "#FFFFFF", Direction.BACKWARDS));
//        camels.add(new CamelDOM(1, "#ff5733", Direction.FORWARDS));
//        GameConfigDOM gameconfig = new GameConfigDOM(3, 5, camels, 6, 7, 4, 8, "forfeitCurrentStage", 11, 9);
//        GameStateDOM gameStateDOM = new GameStateDOM(null,null,gameconfig,null,null,null,0,0,0,null);
//        // instantiate a dice
//        DicePyramidDOM pyramid = new DicePyramidDOM(gameStateDOM);
//        // test Dice class method generateDots()
//        System.out.println("generateDiceFace: " + pyramid.generateDiceFace());
//
//        // test refilling and rolling. Note that initual refill rolls ALL camels.
//        // Susubsequent rolls for actual Etappen will suspend one of the backwards
//        // camels because they share a dice (Product Vision).
//        System.out.println("refillPyramid");
//        pyramid.initialLineupRefill();
//        System.out.println("shuffledCamelIDsArList: " + pyramid.shuffledCamelsArrList);
//        // Testing of Creating a Dice
//        RolledDiceDOM dice1 = pyramid.roll();
//        System.out.println(
//                "Rolled dice is: " + "camelID: " + dice1.getCamel().getId() + " RolledNumber: "
//                        + dice1.getRolledNumber());
//        RolledDiceDOM dice2 = pyramid.roll();
//        System.out.println(
//                "Rolled dice is: " + "camelID: " + dice2.getCamel().getId() + " RolledNumber: "
//                        + dice2.getRolledNumber());
//        RolledDiceDOM dice3 = pyramid.roll();
//        System.out.println(
//                "Rolled dice is: " + "camelID: " + dice3.getCamel().getId() + " RolledNumber: "
//                        + dice3.getRolledNumber());
//        RolledDiceDOM dice4 = pyramid.roll();
//        System.out.println(
//                "Rolled dice is: " + "camelID: " + dice4.getCamel().getId() + " RolledNumber: "
//                        + dice4.getRolledNumber());
//        // dice5 must fail, because CamelMap was instantiated with only 3 Entries and
//        // dice5 exceeds shuffledCamelIDsArList.
//        // This mimics the "Etappenende", because for an "Etappenende", all dices
//        // must have been roled and thus pyramid emptied.
//        // DicePyramidDOM set shuffled is then set to false and an Error is thrown. To
//        // solve this, DicePyramidDOM must be refilled refillPyramid().
//        // refillPyramid() automatically shuffles the shuffledCamelIDsArList to a new
//        // random sequence of Map Entries.
//        try {
//            pyramid.roll();
//        } catch (Exception e) {
//            System.out.println("There has been an Excption: " + e);
//        }
//        // after dice5 failed, refill the DicePyramidDOM with pyramid.refillPyramid();
//        // i.e. refill Dice Pyramid for next "Etappe".
//        // Then get a new dice6
//        System.out.println("refillPyramid AGAIN");
//        pyramid.regularStageRefill();
//        // refillPyramid() automatically shuffles the shuffledCamelIDsArList to a new
//        // random sequence of Map Entries.
//        RolledDiceDOM dice6 = pyramid.roll();
//        System.out.println(
//                "Rolled dice is: " + "camelID: " + dice6.getCamel().getId() + " RolledNumber: "
//                        + dice6.getRolledNumber());
//        RolledDiceDOM dice7 = pyramid.roll();
//        System.out.println(
//                "Rolled dice is: " + "camelID: " + dice7.getCamel().getId() + " RolledNumber: "
//                        + dice7.getRolledNumber());
//        RolledDiceDOM dice8 = pyramid.roll();
//        System.out.println(
//                "Rolled dice is: " + "camelID: " + dice8.getCamel().getId() + " RolledNumber: "
//                        + dice8.getRolledNumber());
//        // dice 9 must fail, because initially 4 camels, but now only 3 allowed, since
//        // both backwards camels share a dice
//        RolledDiceDOM dice9 = pyramid.roll();
//        System.out.println(
//                "Rolled dice is: " + "camelID: " + dice9.getCamel().getId() + " RolledNumber: "
//                        + dice9.getRolledNumber());
//    }
}
