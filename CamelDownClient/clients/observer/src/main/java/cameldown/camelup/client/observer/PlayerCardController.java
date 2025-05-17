package cameldown.camelup.client.observer;

import java.net.URL;
import java.util.ResourceBundle;

import cameldown.camelup.client.core.api.Api;
import cameldown.camelup.interfacedefinition.game.v3.GameState;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PlayerCardController implements Controller {

    private GameState lastGameState;

    private Api api;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private StackPane rootStackPane;

    @FXML
    public ChoiceBox<String> ChoiceBoxPlayerCard;

    @FXML
    public TextField fieldInput;

    @FXML
    private Button placePlayerCardButton;

    @FXML
    private Button handleClose;

    @FXML
    void handleClosePlayerCardWindow(ActionEvent event) {
        // Logik zum Schließen des Fensters (falls erforderlich)
    }

    @FXML
    void handlePlacePlayerCard(ActionEvent event) {
        String inputText = fieldInput.getText();
        String value = ChoiceBoxPlayerCard.getValue(); // +1 oder -1
        Integer spaceID = Integer.parseInt(inputText)-1;;

        if (inputText.trim().isEmpty()) {
            fieldInput.setText("BITTE EINE FELD ID ANGEBEN!");
            return;
        }

        if (!inputText.matches("\\d+")) {
            fieldInput.setText("Es muss eine Zahl sein!");
            return;
        }

        int direction;
        if ("+1".equals(value)) {
            direction = 1;
        } else if ("-1".equals(value)) {
            direction = -1;
        } else {
            fieldInput.setText("Ungültige Richtung in der Auswahlbox!");
            return;
        }

        try {
            this.api.placePlayerCard(spaceID, direction);
            System.out.println("Spielerkarte erfolgreich platziert auf Feld " + spaceID);

        } catch (Exception e) {
            System.err.println("Fehler beim Platzieren der Spielerkarte: " + e.getMessage());
            e.printStackTrace();
            fieldInput.setText("Fehler beim Platzieren der Spielerkarte!");
        }
    }

    public void handleClose(ActionEvent event) {
        Stage stage = (Stage) rootStackPane.getScene().getWindow();
        stage.close();
    }


    /**
     * Initializes the controller by setting up initial content and starting a periodic update check.
     */
    public void initialize() {
        ChoiceBoxPlayerCard.setValue("+1");

        // Verhindere doppelte Einträge
        if (ChoiceBoxPlayerCard.getItems().isEmpty()) {
            ChoiceBoxPlayerCard.getItems().addAll("-1", "+1");
        }

        lastGameState = GameSpectateController.getInstance().getGameState();

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
        }
    }


    @Override
    public void loadApi(final Api api) {
        this.api = api;
        initialize();
    }
}
