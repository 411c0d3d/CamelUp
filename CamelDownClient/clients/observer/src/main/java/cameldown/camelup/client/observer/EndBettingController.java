package cameldown.camelup.client.observer;
import cameldown.camelup.client.core.api.Api;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import cameldown.camelup.interfacedefinition.game.v3.FinalBets;
import cameldown.camelup.interfacedefinition.game.v3.GameState;
import cameldown.camelup.interfacedefinition.game.v3.Player;
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

import static cameldown.camelup.client.observer.GameSpectateController.gameState;

public class EndBettingController implements Controller{

    @FXML
    private URL location;

    @FXML
    private ResourceBundle resources;

    @FXML
    private VBox firstStack;

    @FXML
    private VBox lastStack;

    @FXML
    private Button looserButton;

    @FXML
    private TextField looserInput;

    @FXML
    private GridPane rootGridPane;

    @FXML
    private StackPane rootStackPane;

    @FXML
    private Button winnerButton;

    @FXML
    private TextField winnerInput;

    private Api api;
    private GameState lastGameState;
    /**
     * Initializes the view and starts periodic updates.
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
     * Checks for changes in the game state and updates if needed.
     */
    private void checkForUpdates() {
        GameState currentState = GameSpectateController.getInstance().getGameState();
        if (currentState != null && !currentState.equals(lastGameState)) {
            lastGameState = currentState;
            buildContent(currentState);
        }
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
     * Builds the UI content based on the current game state.
     *
     * @param state the current game state
     */
    private void buildContent(GameState state) {
        firstStack.getChildren().clear();
        lastStack.getChildren().clear();

        if (gameState != null && gameState.getFinalBets() != null) {
            FinalBets finalBets = gameState.getFinalBets();
            List<Player> playerList = gameState.getPlayers();
            Map<Integer, String> playerMap = new HashMap<>();
            for (Player player : playerList) {
                playerMap.put(player.getPlayerId(), player.getName());
            }

            if (finalBets.getFirstCamel() != null && !finalBets.getFirstCamel().isEmpty()) {
                for (int i = 0; i < finalBets.getFirstCamel().size(); i++) {
                    int playerId = finalBets.getFirstCamel().get(i).getPlayerId();
                    String playerName = getPlayerNameById(playerList, playerId, playerMap);
                    String betText = (i + 1) + ". " + playerName;
                    TextFlow textFlow = new TextFlow();
                    textFlow.setTextAlignment(TextAlignment.LEFT);

                    Text text = new Text(betText);
                    text.setStyle("-fx-font-size: 16px;");
                    textFlow.getChildren().add(text);

                    firstStack.setAlignment(Pos.TOP_LEFT);
                    VBox.setMargin(textFlow, new Insets(0, 0, 8, 0));
                    firstStack.getChildren().add(textFlow);
                }
            }

            if (finalBets.getLastCamel() != null && !finalBets.getLastCamel().isEmpty()) {
                for (int i = 0; i < finalBets.getLastCamel().size(); i++) {
                    int playerId = finalBets.getLastCamel().get(i).getPlayerId();
                    String playerName = getPlayerNameById(playerList, playerId, playerMap);

                    String betText = (i + 1) + ". " + playerName;
                    TextFlow textFlow = new TextFlow();
                    textFlow.setTextAlignment(TextAlignment.LEFT);

                    Text text = new Text(betText);
                    text.setStyle("-fx-font-size: 16px;");
                    textFlow.getChildren().add(text);

                    lastStack.setAlignment(Pos.TOP_LEFT);
                    VBox.setMargin(textFlow, new Insets(0, 0, 8, 0));
                    lastStack.getChildren().add(textFlow);
                }
            }
        }

        firstStack.setAlignment(Pos.TOP_CENTER);
        lastStack.setAlignment(Pos.TOP_CENTER);

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

    /**
     * Retrieves a player's name by ID.
     *
     * @param playerList list of players
     * @param playerId   the player's ID
     * @param playerMap  map of player IDs to names
     * @return the player's name or "Unbekannter Spieler"
     */
    private String getPlayerNameById(List<Player> playerList, int playerId, Map<Integer, String> playerMap) {
        return playerMap.getOrDefault(playerId, "Unbekannter Spieler");
    }

    /**
     * Adjusts UI scaling based on the current window size.
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
     * Closes this window.
     *
     * @param event ActionEvent
     */
    public void handleClose(ActionEvent event) {
        Stage stage = (Stage) rootStackPane.getScene().getWindow();
        stage.close();
    }


    /**
     * places bet with CamelId from textfield on looser
     *
     * @param event
     */
    public void handleLooserButton(ActionEvent event) {
        int camelId;

        try {
            if((looserInput != null) && !looserInput.getText().isEmpty()) {
                camelId = Integer.parseInt(looserInput.getText());
                this.api.placeFinalBet(camelId, false);
                System.out.println(camelId);
            }
        }catch (Exception e) {
            if(looserInput.getText().trim().isEmpty()){
                looserInput.setText("BITTE EINE KAMEL ID ANGEBEN!");
            }
            System.out.println(e.getMessage());
        }
    }

    /**
     * places bet with CamelId from textfield on winner
     *
     * @param event
     */
    public void handleWinnerButton (ActionEvent event){
        int camelId;
        try {
            if((winnerInput != null) && !winnerInput.getText().isEmpty()) {
                camelId = Integer.parseInt(winnerInput.getText());
                this.api.placeFinalBet(camelId, true);
                System.out.println(camelId);
            }
        }catch (Exception e) {
            if(winnerInput.getText().trim().isEmpty()){
                winnerInput.setText("BITTE EINE KAMEL ID ANGEBEN!");
            }
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void loadApi(final Api api) {
        this.api = api;
        initialize();
    }

}