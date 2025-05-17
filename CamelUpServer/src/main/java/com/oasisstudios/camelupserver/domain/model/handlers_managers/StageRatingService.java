package com.oasisstudios.camelupserver.domain.model.handlers_managers;

import com.oasisstudios.camelupserver.domain.model.domainclasses.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

//Eine Etappe endet, sobald kein Würfel mehr zur Verfügung steht. Am Ende einer Etappe
//wird eine Etappenwertung durchgeführt. Falls das Spiel nicht beendet wurde, beginnt
//danach eine neue Etappe.
// (37) Eine Etappenwertung wird wie folgt durchgeführt:
// (37.1) Das oberste Kamel des am weitesten vorne liegenden Kamel-Rudels ist der Etappensieger.
//Falls das entsprechende Kamel-Rudel aus mindestens zwei Kamelen
//besteht, so ist das Kamel, welches sich im Stapel unmittelbar unter dem Etappensieger
//befindet, der Etappenzweite. Ansonsten ist das oberste Kamel des am zweit
//weitesten vorne liegenden Kamel-Rudels der Etappenzweite. Das unterste Kamel
//des am weitesten hinten liegenden Kamel-Rudels ist der Etappenverlierer. Die beiden
//rückwärtslaufenden Kamele werden bei der Bestimmung des Etappensiegers,
//des Etappenzweiten und des Etappenverlierers ignoriert.
// (37.2) Für jeden Spieler wird ein Gesamtbetrag an Münzen wie folgt bestimmt: Für jeden
// Wettschein, welcher zum Etappensieger gehört, erhält der Spieler genau w
// viele Münzen, wobei w derWertigkeit desWettscheins entspricht. Für jedenWettschein,
//welcher zum Etappenzweiten gehört, erhält der Spieler genau eine Münze.
//Für jeden anderen Wettschein verliert der Spieler genau eine Münze. Für die Berechnungen
//werden nur die Wettscheine berücksichtigt, die der Spieler während
//der Etappe genommen hat.
//(37.3) Ist der Gesamtbetrag positiv, so erhält der Spieler entsprechend viele Münzen.
//(37.4) Ist der Gesamtbetrag negativ, so verliert der Spieler entsprechend viele Münzen.

// (39) Verliert ein Spieler mehr Münzen als er besitzt, so verliert er lediglich die Münzen, die
//er besitzt.

public class StageRatingService {

    private GameStateDOM gameStateDOM;
    private BoardSpacesCourseHelper boardSpacesCourseHelper;


    public StageRatingService(GameStateDOM gameStateDOM, BoardSpacesCourseHelper boardSpacesCourseHelper) {
        this.gameStateDOM = gameStateDOM;
        this.boardSpacesCourseHelper = boardSpacesCourseHelper;
    }


    public void performStageRatingForAllPlayers() {
        ArrayList<PlayerDOM> playerDOMS = gameStateDOM.getPlayers();
        CamelDOM winner = boardSpacesCourseHelper.getLeadingCamel();
        CamelDOM second = boardSpacesCourseHelper.getSecondPlaceCamel();

        for (PlayerDOM playerDOM : playerDOMS) {
            if ((playerDOM.getState() == PlayerDOM.PlayerState.PLAYING) && !playerDOM.getBettingCards().isEmpty()) {
                int money = 0;
                for (BettingCardDOM bet : playerDOM.getBettingCards()) {
                    if (bet.getCamelId() == winner.getId()) {
                        money += bet.getWorth();
                    } else if (bet.getCamelId() == second.getId()) {
                        money += 1;
                    } else {
                        money -= 1;
                    }

                }
                // Update playerDOM's money by total money from bets. If playerDOMS money drops below 0: set money to 0.
                playerDOM.setMoney(Math.max(0, playerDOM.getMoney() + money));

            }
        }

    }

}


