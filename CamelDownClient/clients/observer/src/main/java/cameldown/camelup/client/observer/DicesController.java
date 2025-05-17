package cameldown.camelup.client.observer;

import cameldown.camelup.interfacedefinition.game.v3.BoardSpace;
import cameldown.camelup.interfacedefinition.game.v3.Camel;
import cameldown.camelup.interfacedefinition.game.v3.GameState;
import cameldown.camelup.interfacedefinition.game.v3.RolledDice;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

/**
 * Controls the UI display and updates of rolled dice and related information.
 */
public class DicesController{

    @FXML
    private HBox diceRolledDice;

    @FXML
    private Text diceLast;

    @FXML
    private Text diceFirstRolled;

    @FXML
    private Text diceFirstUnrolled;

    @FXML
    private Text diceCountRolled;

    @FXML
    private Text diceCountUnrolled;

    @FXML
    private Text diceBack2;

    @FXML
    private Text diceBack1;

    @FXML
    private TextField diceIDSearchField;

    @FXML
    private Text diceIDSearchValue;

    @FXML
    private GridPane rootGridPane;

    @FXML
    private StackPane rootStackPane;

    private List<RolledDice> rolledDiceList;
    private List<Camel> camels;
    private GameState lastGameState;

    /**
     * Initializes the controller, sets up initial UI state and starts periodic updates.
     */
    public void initialize() {
        GameState lastGameState = GameSpectateController.getInstance().getGameState();
        buildContent(lastGameState);
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(50), e -> checkForUpdates())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Periodically checks for changes in the game state and updates the UI if needed.
     */
    private void checkForUpdates() {
        GameState currentState = GameSpectateController.getInstance().getGameState();
        if (currentState != null && !currentState.equals(lastGameState)) {
            lastGameState = currentState;
            buildContent(currentState);
        }
    }

    /**
     * Rebuilds the UI elements based on the current game state.
     *
     * @param state the current game state
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

        int numRows = 5;
        int numColumns = 7;
        int totalBoxes = numRows * numColumns;

        GameState gameState = GameSpectateController.getInstance().getGameState();

        camels = new ArrayList<>(gameState.getGameConfig().getCamels());
        int totalCamels = camels.size();

        rolledDiceList = new ArrayList<>(gameState.getRolledDice());
        Collections.reverse(rolledDiceList);

        List<Node> diceBoxes = new ArrayList<>();

        // Create boxes for rolled dice
        for (RolledDice rolledDice : rolledDiceList) {
            Camel camel = findCamelById(rolledDice.getCamelId(), camels);
            HBox diceBox = createColoredDiceBox(rolledDice, camel);
            diceBoxes.add(diceBox);
        }

        // Create grey boxes for unrolled dice
        int remainingCamels = totalCamels - rolledDiceList.size();
        for (int i = 0; i < remainingCamels - 1; i++) {
            HBox diceBox = createGreyDiceBox();
            diceBoxes.add(diceBox);
        }

        // Create invisible boxes for spacing
        int remainingBoxes = totalBoxes - diceBoxes.size();
        for (int i = 0; i < remainingBoxes; i++) {
            HBox diceBox = createInvisibleDiceBox();
            diceBoxes.add(diceBox);
        }

        VBox diceRows = new VBox(5);
        diceRows.setAlignment(Pos.CENTER);

        int index = 0;
        for (int i = 0; i < numRows; i++) {
            HBox row = new HBox(5);
            row.setAlignment(Pos.CENTER);
            for (int j = 0; j < numColumns; j++) {
                if (index < diceBoxes.size()) {
                    row.getChildren().add(diceBoxes.get(index));
                    index++;
                }
            }
            diceRows.getChildren().add(row);
        }

        diceRolledDice.getChildren().clear();
        diceRolledDice.setAlignment(Pos.CENTER);
        diceRolledDice.getChildren().add(diceRows);

        updateTexts();
    }

    /**
     * Adjusts the scaling of the UI depending on the scene size.
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
     * Creates an invisible dice box for layout purposes.
     *
     * @return the invisible dice box
     */
    private HBox createInvisibleDiceBox() {
        HBox diceBox = new HBox();
        diceBox.setAlignment(Pos.CENTER);
        diceBox.setPrefSize(53, 53);
        diceBox.setVisible(false);
        return diceBox;
    }

    /**
     * Creates a dice box for a rolled dice, colored according to the camel color.
     *
     * @param rolledDice the rolled dice data
     * @param camel      the camel associated with this dice
     * @return the colored dice box
     */
    private HBox createColoredDiceBox(RolledDice rolledDice, Camel camel) {
        HBox diceBox = new HBox();
        diceBox.setAlignment(Pos.CENTER);
        diceBox.setPrefSize(53, 53);

        String camelColorHex = camel != null ? camel.getColor() : "#FFFFFF";
        diceBox.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0.5, 2, 0); " +
            "-fx-background-color: " + camelColorHex + "; -fx-background-radius: 10;");

        Color camelColor = Color.web(camelColorHex);
        double brightness = camelColor.getBrightness();
        Color textColor = (brightness > 0.9) ? Color.BLACK : Color.WHITE;

        Text numberText = new Text(String.valueOf(rolledDice.getNumber()));
        numberText.setFont(Font.font("System", FontWeight.BOLD, 18));
        numberText.setFill(textColor);

        Text camelText = new Text(System.lineSeparator() + "# " + rolledDice.getCamelId());
        camelText.setFont(Font.font("System", 16));
        camelText.setFill(textColor);

        TextFlow textFlow = new TextFlow(numberText, camelText);
        textFlow.setTextAlignment(TextAlignment.CENTER);

        diceBox.getChildren().add(textFlow);
        return diceBox;
    }

    /**
     * Creates a grey dice box representing an unrolled dice.
     *
     * @return the grey dice box
     */
    private HBox createGreyDiceBox() {
        HBox diceBox = new HBox();
        diceBox.setAlignment(Pos.CENTER);
        diceBox.setPrefSize(53, 53);
        diceBox.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0.5, 2, 0); " +
            "-fx-background-color: #E8E8E8; -fx-background-radius: 10;");
        Label numberLabel = new Label();
        numberLabel.setVisible(false);
        diceBox.getChildren().add(numberLabel);
        return diceBox;
    }

    /**
     * Finds a camel by its ID.
     *
     * @param camelId the camel ID
     * @param camels  the list of camels
     * @return the camel with the given ID, or null if not found
     */
    private Camel findCamelById(Integer camelId, List<Camel> camels) {
        for (Camel camel : camels) {
            if (camel.getId().equals(camelId)) {
                return camel;
            }
        }
        return null;
    }

    /**
     * Closes the current stage.
     *
     * @param event the action event
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
     * Updates displayed texts based on the current game state and dice rolls.
     */
    public void updateTexts() {
        GameState gameState = GameSpectateController.getInstance().getGameState();

        if (rolledDiceList.isEmpty()) {
            diceLast.setText("Noch nicht gewürfelt");
        } else {
            RolledDice lastRolledDice = rolledDiceList.getFirst();
            diceLast.setText("Kamel ID " + lastRolledDice.getCamelId());
        }

        List<BoardSpace> boardSpaces = gameState.getBoardSpaces();
        List<Integer> camelIdsInOrder = new ArrayList<>();
        for (int i = boardSpaces.size() - 1; i >= 0; i--) {
            BoardSpace space = boardSpaces.get(i);
            Set<Integer> camelIdsOnSpace = space.getCamelIds();
            for (Integer camelId : camelIdsOnSpace) {
                if (!camelIdsInOrder.contains(camelId)) {
                    camelIdsInOrder.add(camelId);
                }
            }
        }

        Set<Integer> rolledCamelIds = new HashSet<>();
        for (RolledDice rd : rolledDiceList) {
            rolledCamelIds.add(rd.getCamelId());
        }

        Integer firstUnrolledCamelId = null;
        Integer firstRolledCamelId = null;

        for (Integer camelId : camelIdsInOrder) {
            if (!rolledCamelIds.contains(camelId) && firstUnrolledCamelId == null && camelId >= 0) {
                firstUnrolledCamelId = camelId;
            }
            if (rolledCamelIds.contains(camelId) && firstRolledCamelId == null && camelId >= 0) {
                firstRolledCamelId = camelId;
            }
            if (firstUnrolledCamelId != null && firstRolledCamelId != null) {
                break;
            }
        }

        if (firstUnrolledCamelId != null) {
            diceFirstUnrolled.setText("Kamel ID " + firstUnrolledCamelId);
        } else {
            diceFirstUnrolled.setText("Alle Kamele gewürfelt");
        }

        if (firstRolledCamelId != null) {
            diceFirstRolled.setText("Kamel ID " + firstRolledCamelId);
        } else {
            diceFirstRolled.setText("Noch nicht gewürfelt");
        }

        diceCountRolled.setText(String.valueOf(rolledDiceList.size()));
        int diceCountUnrolledValue = camels.size() - rolledDiceList.size();
        diceCountUnrolled.setText(String.valueOf(diceCountUnrolledValue - 1));

        Integer camelIdMinus2 = -2;
        Integer camelIdMinus1 = -1;

        RolledDice diceForMinus2 = null;
        RolledDice diceForMinus1 = null;

        for (RolledDice rd : rolledDiceList) {
            if (rd.getCamelId().equals(camelIdMinus2)) {
                diceForMinus2 = rd;
            }
            if (rd.getCamelId().equals(camelIdMinus1)) {
                diceForMinus1 = rd;
            }
        }

        if (diceForMinus2 != null) {
            diceBack2.setText("Mit Wert " + diceForMinus2.getNumber() + " gewürfelt");
        } else {
            diceBack2.setText("Noch nicht gewürfelt");
        }

        if (diceForMinus1 != null) {
            diceBack1.setText("Mit Wert " + diceForMinus1.getNumber() + " gewürfelt");
        } else {
            diceBack1.setText("Noch nicht gewürfelt");
        }
    }

    /**
     * Searches for a dice result by camel ID and updates the display.
     */
    public void diceIDSearch() {
        String inputText = diceIDSearchField.getText().trim();
        try {
            Integer camelId = Integer.parseInt(inputText);
            Camel camel = findCamelById(camelId, camels);
            if (camel == null) {
                diceIDSearchValue.setText("ID nicht vorhanden");
                return;
            }

            RolledDice rolledDice = null;
            for (RolledDice rd : rolledDiceList) {
                if (rd.getCamelId().equals(camelId)) {
                    rolledDice = rd;
                    break;
                }
            }

            if (rolledDice != null) {
                diceIDSearchValue.setText("Mit Wert " + rolledDice.getNumber() + " gewürfelt");
            } else {
                diceIDSearchValue.setText("Kamel " + camelId + " noch nicht gewürfelt");
            }
        } catch (NumberFormatException e) {
            diceIDSearchValue.setText("ID nicht vorhanden");
        }
    }
}
