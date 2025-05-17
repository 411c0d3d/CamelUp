package cameldown.camelup.client.observer;

import cameldown.camelup.interfacedefinition.game.v3.BoardSpace;
import cameldown.camelup.interfacedefinition.game.v3.GameState;
import cameldown.camelup.interfacedefinition.game.v3.Player;
import cameldown.camelup.interfacedefinition.game.v3.PlayerCard;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

/**
 * Displays detailed information about a single field, including camels and player cards.
 */
public class FieldInformationController {

    @FXML
    private Text currentInfoField;
    @FXML
    private Text fieldCountCamel;
    @FXML
    private Text fieldFirstCamel;
    @FXML
    private Text fieldBack2;
    @FXML
    private Text fieldBack1;
    @FXML
    private Text fieldPlayercard;

    @FXML
    private GridPane rootGridPane;
    @FXML
    private StackPane rootStackPane;
    @FXML
    private VBox fieldCamelsStack;

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

    /**
     * Builds the UI components based on the current game state.
     *
     * @param state current game state
     */
    private void buildContent(GameState state) {
        Platform.runLater(() -> {
            Scene scene = rootStackPane.getScene();
            if (scene != null) {
                scene.widthProperty().addListener((obs, oldVal, newVal) -> adjustScaling());
                scene.heightProperty().addListener((obs, oldVal, newVal) -> adjustScaling());
                adjustScaling();
                registerEscHotkey(scene);
            }
        });

        updateTexts();
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
     * Adjusts UI scaling based on current window size.
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
     * Updates the displayed texts for the currently selected field.
     */
    public void updateTexts() {
        int currentFieldIndex = GameSpectateController.infoField;
        int currentFieldNumber = currentFieldIndex + 1;
        currentInfoField.setText("Angezeigtes Feld: " + currentFieldNumber);

        GameState gameState = GameSpectateController.gameState;
        List<BoardSpace> boardSpaces = gameState.getBoardSpaces();
        BoardSpace currentBoardSpace = boardSpaces.get(currentFieldIndex);

        List<Integer> camelIdList = new ArrayList<>(currentBoardSpace.getCamelIds());
        Collections.reverse(camelIdList);

        fieldCountCamel.setText("" + camelIdList.size());


        fieldCamelsStack.getChildren().clear();


        HBox row = null;
        for (int i = 0; i < camelIdList.size(); i++) {
            if (i % 3 == 0) {
                row = new HBox(20);
                fieldCamelsStack.getChildren().add(row);
            }

            int camelId = camelIdList.get(i);
            int position = i + 1;
            Text t = new Text((position + ". Pos.: ID " + camelId));
            t.setStyle("-fx-font-size: 16px;");
            row.getChildren().add(t);
        }

        if (!camelIdList.isEmpty()) {
            int firstCamelId = camelIdList.getFirst();
            fieldFirstCamel.setText("ID " + firstCamelId);
        } else {
            fieldFirstCamel.setText("Keine Kamele auf dem Feld");
        }

        int camelIdToFind = -2;
        int position = camelIdList.indexOf(camelIdToFind);
        if (position != -1) {
            position += 1;
            fieldBack2.setText("an Position " + position);
        } else {
            fieldBack2.setText("Nicht auf Feld.");
        }

        camelIdToFind = -1;
        position = camelIdList.indexOf(camelIdToFind);
        if (position != -1) {
            position += 1;
            fieldBack1.setText("an Position " + position);
        } else {
            fieldBack1.setText("Nicht auf Feld.");
        }

        PlayerCard playerCard = currentBoardSpace.getPlayerCard();
        if (playerCard == null) {
            fieldPlayercard.setText("Keine");
        } else {
            List<Player> playerList = gameState.getPlayers();
            Map<Integer, String> playerMap = new HashMap<>();
            for (Player player : playerList) {
                playerMap.put(player.getPlayerId(), player.getName());
            }
            Integer spacesMoved = playerCard.getSpacesMoved();
            int playerId = playerCard.getPlayerId();
            String playerName = getPlayerNameById(playerList, playerId, playerMap);

            if (spacesMoved == null) {
                fieldPlayercard.setText("Unbekannte PlayerCard");
            } else if (spacesMoved.equals(1)) {
                fieldPlayercard.setText("+1 von " + playerName);
            } else if (spacesMoved.equals(-1)) {
                fieldPlayercard.setText("-1 von " + playerName);
            } else {
                fieldPlayercard.setText("Unbekannte PlayerCard");
            }
        }
    }

    /**
     * Retrieves a player's name by their ID.
     *
     * @param playerList list of players
     * @param playerId   player's ID
     * @param playerMap  map of IDs to names
     * @return player's name or "Unbekannter Spieler"
     */
    private String getPlayerNameById(List<Player> playerList, int playerId, Map<Integer, String> playerMap) {
        return playerMap.getOrDefault(playerId, "Unbekannter Spieler");
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

}
