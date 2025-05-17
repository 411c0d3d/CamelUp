package cameldown.camelup.client.observer;

import cameldown.camelup.client.core.api.Api;
import cameldown.camelup.interfacedefinition.game.v3.ClientAck;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for the start screen.
 */
public class StartScreenController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    @FXML
    TextField serverAddressTextField;
    @FXML
    TextField serverPortTextField;
    @FXML
    private GridPane rootGridPane;
    @FXML
    private StackPane rootStackPane;
    @FXML
    private Text startScreenError;
    @FXML
    private TextField usernameInput;

    private Api api;

    /**
     * Initializes the controller after loading.
     */
    public void initializeController() {
        Platform.runLater(() -> {
            Scene scene = rootStackPane.getScene();
            if (scene != null) {
                Stage stage = (Stage) scene.getWindow();
                stage.setTitle("CamelDown");
                stage.getIcons().add(new Image("/cameldown/camelup/client/observer/assets/GameSpectate/icon.png"));

                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                double desiredWidth = screenBounds.getWidth() * 0.95;
                double desiredHeight = screenBounds.getHeight() * 0.95;
                if (desiredWidth / 16 >= desiredHeight / 9) {
                    desiredWidth = desiredHeight / 9 * 16;
                } else {
                    desiredHeight = desiredWidth / 16 * 9;
                }
                stage.setWidth(desiredWidth);
                stage.setHeight(desiredHeight);
                stage.centerOnScreen();

                scene.widthProperty().addListener((obs, oldVal, newVal) -> adjustScaling());
                scene.heightProperty().addListener((obs, oldVal, newVal) -> adjustScaling());
                adjustScaling();
            }
        });
    }

    /**
     * Adjusts the scaling of UI elements based on scene size.
     */
    private void adjustScaling() {
        Scene scene = rootStackPane.getScene();
        if (scene == null) {
            return;
        }
        double sceneWidth = scene.getWidth();
        double sceneHeight = scene.getHeight();
        double internalWidth = 1920;
        double internalHeight = 1080;
        double scaleX = sceneWidth / internalWidth;
        double scaleY = sceneHeight / internalHeight;
        double scale = Math.min(scaleX, scaleY);
        rootGridPane.setScaleX(scale);
        rootGridPane.setScaleY(scale);
        rootGridPane.setPrefWidth(internalWidth);
        rootGridPane.setPrefHeight(internalHeight);
    }

    /**
     * Connects to the server and switches to the game list view.
     *
     * @param event triggered event
     * @throws IOException if loading fails
     */
    @FXML
    private void switchToSecondary(Event event) throws IOException {
        String ipAddress = serverAddressTextField.getText();
        if (GameSpectateController.isInteger(serverPortTextField.getText())) {
            int port = Integer.parseInt(serverPortTextField.getText());
            try {
                CompletableFuture<ClientAck> future = api.connect(ipAddress, port);
                try {
                    var clientAck = future.get();
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\t" + clientAck.getClientId());
                    api.setClientId(clientAck.getClientId());
                    this.api.registerPlayer(usernameInput.getText()); // getting Username from InputField
                }catch (Exception e) {
                    e.printStackTrace();
                }
            } catch(IOException e){
                logger.error("Failed to connect to server.");
                startScreenError.setVisible(true);
                startScreenError.setText("Server unter " + serverAddressTextField.getText() + ":" + serverPortTextField.getText() + " nicht erreichbar.");
                return;
            }
        }
         else {
            logger.error("Invalid port.");
            startScreenError.setVisible(true);
            startScreenError.setText("Ungültiger Port.");
            return;
        }
        api.addOnGameStateEventHandler(newGameState -> {
            Platform.runLater(() -> {
                GameSpectateController.refreshGameState(newGameState);
            });
        });
        api.addOnGameEndEventHandler(newGameEnd -> {
            Platform.runLater(() -> {
                GameSpectateController.refreshGameEnd(newGameEnd);
            });
        });
        App.setRoot("GameList");
    }

    @Override
    public void loadApi(final Api api) {
        this.api = api;
        initializeController();
    }
}
