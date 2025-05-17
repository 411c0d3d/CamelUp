package cameldown.camelup.client.observer;

import cameldown.camelup.client.core.api.Api;
import cameldown.camelup.interfacedefinition.game.v3.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cameldown.camelup.client.observer.GameSpectateController.gameState;
import static cameldown.camelup.client.observer.GameSpectateController.isInteger;
import static java.lang.Integer.parseInt;

/**
 * Controller for handling and displaying betting cards and related information.
 */

public class BettingsController implements Controller {
    @FXML
    private TextField InputBettingID;

    @FXML
    private Button PlaceEtappenwette;

    @FXML
    private GridPane rootGridPane;

    @FXML
    private StackPane rootStackPane;

    @FXML
    private Text camelBetText;
    @FXML
    private Text camelBetText2;

    @FXML
    private TextField camelBetSearch;
    @FXML
    private TextField playerBetSearch;

    @FXML
    private VBox bettingsStack;
    @FXML
    private VBox playerBets;

    private GameState lastGameState;

    private Api api;

    /**
     * Initializes the controller by setting up initial content and starting a periodic update check.
     */
    public void initialize() {
        lastGameState = GameSpectateController.getInstance().getGameState();
        buildContent(lastGameState);

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(50), e -> checkForUpdates())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Checks for updates in the game state and rebuilds the UI content if changes are detected.
     */
    private void checkForUpdates() {
        GameState currentState = GameSpectateController.getInstance().getGameState();
        if (currentState != null && !currentState.equals(lastGameState)) {
            lastGameState = currentState;
            buildContent(currentState);
        }
    }

    /**
     * Rebuilds the displayed betting card information based on the given game state.
     *
     * @param state The current game state.
     */
    private void buildContent(GameState state) {
        bettingsStack.getChildren().clear();
        GameState gameState = GameSpectateController.getInstance().getGameState();

        if (gameState != null) {
            List<BettingCards> bettingCardDeck = gameState.getBettingCards();
            for (BettingCards bettingCard : bettingCardDeck) {
                Text text = new Text("ID " + bettingCard.getCamelId() + ": " + bettingCard.getAmount() + " Wettscheine verfügbar");
                text.setStyle("-fx-font-size: 16px;");
                VBox.setMargin(text, new Insets(0, 0, 8, 0));
                bettingsStack.getChildren().add(text);

                TextFlow textFlow = new TextFlow();
                textFlow.setTextAlignment(TextAlignment.LEFT);

                text.setStyle("-fx-font-size: 16px;");
                textFlow.getChildren().add(text);

                VBox.setMargin(textFlow, new Insets(0, 0, 8, 0));
                bettingsStack.getChildren().add(textFlow);
            }
        }

        bettingsStack.setAlignment(Pos.TOP_CENTER);

        Platform.runLater(() -> {
            Scene scene = rootStackPane.getScene();
            if (scene != null) {
                scene.widthProperty().addListener((obs, oldVal, newVal) -> adjustScaling());
                scene.heightProperty().addListener((obs, oldVal, newVal) -> adjustScaling());
                adjustScaling();
                registerEscHotkey(scene);
            }
        });
    }

    private void registerEscHotkey(Scene scene) {
        scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                handleClose(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Handles the search action for a specific camel ID and updates the displayed betting card information.
     *
     * @param e Action event triggered by the search button.
     */
    public void camelBetSearch(ActionEvent e) {
        List<BettingCards> bettingCardDeck = gameState.getBettingCards();
        if (isInteger(camelBetSearch.getText()) &&
            parseInt(camelBetSearch.getText()) <= gameState.getGameConfig().getCamels().size() &&
            parseInt(camelBetSearch.getText()) >= 0) {

            int camelId = parseInt(camelBetSearch.getText());
            String newCamelBetText = "Kamel mit ID: " + camelId + " hat noch " +
                    bettingCardDeck.get(camelId).getAmount() + " Wettscheine.";
            camelBetText.setText(newCamelBetText);
            camelBetSearch.setText("");

            List<Player> playerList = gameState.getPlayers();
            Map<Integer, String> playerMap = new HashMap<>();
            for (Player player : playerList) {
                playerMap.put(player.getPlayerId(), player.getName());
            }

            StringBuilder sb = new StringBuilder("Wettscheine gekauft von: ");
            boolean foundAny = false;

            for (Player player : playerList) {
                for (PlayerBettingCard pbc : player.getBettingCards()) {
                    if (pbc.getCamelId().equals(camelId)) {
                        String playerName = playerMap.get(player.getPlayerId());
                        sb.append(playerName).append(", ");
                        foundAny = true;
                    }
                }
            }

            if (foundAny) {
                sb.setLength(sb.length() - 2);
                camelBetText2.setText(sb.toString());
            } else {
                camelBetText2.setText("");
            }

        } else {
            camelBetText.setText("Ungültige ID.");
            camelBetText2.setText("");
        }
    }

    /**
     * Handles the search action for a specific player name and displays the camels (with ID and worth)
     * for which this player has bought betting cards in the playerBets VBox.
     *
     * @param e Action event triggered by the search button associated with playerBetSearch.
     */
    public void playerBetSearch(ActionEvent e) {
        playerBets.getChildren().clear();
        GameState gameState = GameSpectateController.getInstance().getGameState();

        if (gameState == null) {
            return;
        }

        String searchName = playerBetSearch.getText().trim();
        if (searchName.isEmpty()) {
            Text infoText = new Text("Bitte einen Spielernamen eingeben.");
            infoText.setStyle("-fx-font-size: 16px;");
            VBox.setMargin(infoText, new Insets(0, 0, 8, 0));
            playerBets.getChildren().add(infoText);
            return;
        }

        List<Player> playerList = gameState.getPlayers();
        Map<Integer, String> playerMap = new HashMap<>();
        for (Player player : playerList) {
            playerMap.put(player.getPlayerId(), player.getName());
        }

        Player foundPlayer = null;
        for (Player player : playerList) {
            if (player.getName().equalsIgnoreCase(searchName)) {
                foundPlayer = player;
                break;
            }
        }

        if (foundPlayer == null) {
            Text infoText = new Text("Spielernamen nicht gefunden.");
            infoText.setStyle("-fx-font-size: 16px;");
            VBox.setMargin(infoText, new Insets(0, 0, 8, 0));
            playerBets.getChildren().add(infoText);
            return;
        }

        if (foundPlayer.getBettingCards().isEmpty()) {
            Text infoText = new Text("Dieser Spieler hat keine Wettscheine gekauft.");
            infoText.setStyle("-fx-font-size: 16px;");
            VBox.setMargin(infoText, new Insets(0, 0, 8, 0));
            playerBets.getChildren().add(infoText);
            return;
        }

        for (PlayerBettingCard pbc : foundPlayer.getBettingCards()) {
            String betInfo = "Kamel-ID: " + pbc.getCamelId() + ", mit Wert: " + pbc.getWorth() + ".";
            Text text = new Text(betInfo);
            text.setStyle("-fx-font-size: 16px;");
            VBox.setMargin(text, new Insets(0, 0, 8, 0));
            playerBets.getChildren().add(text);
        }

        playerBets.setAlignment(Pos.TOP_CENTER);
    }

    /**
     * Adjusts scaling of the UI elements based on the current scene size.
     */
    private void adjustScaling() {
        Scene scene = rootStackPane.getScene();
        if (scene == null) {
            return;
        }

        double sceneWidth = scene.getWidth();
        double sceneHeight = scene.getHeight();
        double internalWidth = 960;
        double internalHeight = 540;
        double scaleX = sceneWidth / internalWidth;
        double scaleY = sceneHeight / internalHeight;
        double scale = Math.min(scaleX, scaleY);

        rootGridPane.setScaleX(scale);
        rootGridPane.setScaleY(scale);
        rootGridPane.setPrefWidth(internalWidth);
        rootGridPane.setPrefHeight(internalHeight);
    }

    /**
     * Handles the closure of the current stage.
     *
     * @param event Action event triggered by the close button.
     */
    public void handleClose(ActionEvent event) {
        Stage stage = (Stage) rootStackPane.getScene().getWindow();
        stage.close();
    }

    public void handlePlaceEtappenwette(ActionEvent event) {
        Integer camelID = Integer.parseInt(InputBettingID.getText());
        this.api.betOnStage(camelID);
    }

    @Override
    public void loadApi(final Api api) {
        this.api = api;
        initialize();
    }
}

