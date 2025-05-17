package cameldown.camelup.client.observer;

import cameldown.camelup.interfacedefinition.game.v3.GameConfig;
import cameldown.camelup.interfacedefinition.game.v3.GameState;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Displays information about the current game configuration and state.
 */
public class InfoController {

    public GameConfig gameConfig;
    public GameState gameState;

    @FXML
    private Text infoPlayerCount;

    @FXML
    private Text infoNumberOfSpaces;

    @FXML
    private Text infoCamels;

    @FXML
    private Text infoDiceFaces;

    @FXML
    private Text infoNumberOfBettingsCards;

    @FXML
    private Text infoThinkingTime;

    @FXML
    private Text infoIllegalMovePenalty;

    @FXML
    private Text infoMaxGameDuration;

    @FXML
    private Text infoMaxRounds;

    @FXML
    private GridPane rootGridPane;
    @FXML
    private StackPane rootStackPane;

    /**
     * Closes the info window.
     *
     * @param event triggered event
     */
    public void handleClose(ActionEvent event) {
        Stage stage = (Stage) rootStackPane.getScene().getWindow();
        stage.close();
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
     * Initializes the info view with data.
     */
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            Scene scene = rootStackPane.getScene();
            if (scene != null) {
                scene.widthProperty().addListener((obs, oldVal, newVal) -> adjustScaling());
                scene.heightProperty().addListener((obs, oldVal, newVal) -> adjustScaling());
                adjustScaling();
                registerEscHotkey(scene);
            }
        });

        gameState = GameSpectateController.gameState;
        gameConfig = gameState.getGameConfig();

        infoPlayerCount.setText(gameState.getPlayers().size() + " / " + gameConfig.getPlayerCount());
        infoNumberOfSpaces.setText("" + gameConfig.getNumberOfSpaces());
        infoCamels.setText("" + gameConfig.getCamels().size());
        infoDiceFaces.setText("" + gameConfig.getDiceFaces());
        infoNumberOfBettingsCards.setText("" + gameConfig.getNumberOfBettingCards());
        infoThinkingTime.setText((gameConfig.getThinkingTime() / 1000) + " Sekunden");
        infoMaxGameDuration.setText((gameConfig.getMaxGameDuration() / 60000) + " Minuten");
        infoMaxRounds.setText("" + gameConfig.getMaxTurns());
        if (gameConfig.getIllegalMovePenalty() == GameConfig.IllegalMovePenalty.FORFEIT_CURRENT_STAGE) {
            infoIllegalMovePenalty.setText("Ausschluss des Spielers von der aktuellen Etappe");
        } else {
            infoIllegalMovePenalty.setText("Ausschluss aus der aktuellen Etappe");
        }
    }

    /**
     * Adjusts UI scaling based on current window size.
     */
    private void adjustScaling() {
        Scene scene = rootStackPane.getScene();
        if (scene == null) {
            return;
        }
        double sceneWidth = scene.getWidth();
        double sceneHeight = scene.getHeight();
        double internalWidth = 820;
        double internalHeight = 450;
        double scaleX = sceneWidth / internalWidth;
        double scaleY = sceneHeight / internalHeight;
        double scale = Math.min(scaleX, scaleY);
        rootGridPane.setScaleX(scale);
        rootGridPane.setScaleY(scale);
        rootGridPane.setPrefWidth(internalWidth);
        rootGridPane.setPrefHeight(internalHeight);
    }

}
