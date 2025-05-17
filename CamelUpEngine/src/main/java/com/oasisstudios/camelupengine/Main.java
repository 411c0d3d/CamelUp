package com.oasisstudios.camelupengine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oasisstudios.camelupengine.api.Api;
import com.oasisstudios.camelupengine.dto.ClientAck;
import com.oasisstudios.camelupengine.dto.SuccessFeedback;
import com.oasisstudios.camelupengine.engineteilnehmer.EngineParticipant;
import com.oasisstudios.camelupengine.engineteilnehmer.EngineParticipantTest;
import com.oasisstudios.camelupengine.engineteilnehmer.GameStateConsumer;

import java.util.Arrays;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Main {
    private static int id;
    public static void main(String[] args) {
        //EngineParticipantTest test = new EngineParticipantTest();
        //test.runTests();

        String host = "localhost";
        // int port = 33100;
        int port = 62263;
        if (args.length > 0) {
            List<String> arguments = Arrays.asList(args);
            if (arguments.indexOf("-port") > 0) {
                port = Integer.parseInt(arguments.get(arguments.indexOf("-port")));
            }
            if (arguments.indexOf("-ip") > 0) {
                host = arguments.get(arguments.indexOf("-ip"));
            }
        }

        // Initialisiere Gson und API
        Gson gson = new GsonBuilder().create();
        Api api = new Api(gson);
        try {
            // Verbinde zum Server
            
            CompletableFuture<ClientAck> connectionFuture = api.connect(host, port);
            ClientAck client = connectionFuture.get();
            id = client.getClientId();
            EngineParticipant engine = new EngineParticipant(id, api, true);
            System.out.println(id);
            // Registriere den Bot als Spieler
            CompletableFuture<Void> registrationFuture = api.registerPlayer("AI" + id)
                    .thenAccept(feedback -> handleFeedback(feedback, "Registrierung"))
                    .exceptionally(throwable -> {
                        System.err.println("Registrierung fehlgeschlagen: " + throwable.getMessage());
                        return null;
                    });

            api.addOnGameStateEventHandler(new GameStateConsumer(engine));

            while( !false) {
                //To stop the engine from stopping immediately
            }
            
        } catch (IOException | InterruptedException | RuntimeException e) {
            System.err.println("Fehler: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Beende die API-Verbindung
            api.close();
        }
    }

    /**
     * Verarbeitet das SuccessFeedback und gibt entsprechende Meldungen aus.
     *
     * @param feedback   Das SuccessFeedback-Objekt
     * @param actionName Name der Aktion (z. B. "Registrierung", "Beitritt zur
     *                   Lobby")
     */
    private static void handleFeedback(SuccessFeedback feedback, String actionName) {
        if (feedback.getSuccess()) {
            System.out.println(actionName + " erfolgreich abgeschlossen: " + feedback.getRequest());
        } else {
            System.err.println(actionName + " fehlgeschlagen: " + feedback.getError());
        }
    }
}
