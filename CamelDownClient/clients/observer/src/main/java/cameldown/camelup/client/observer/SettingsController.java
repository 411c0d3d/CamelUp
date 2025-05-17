package cameldown.camelup.client.observer;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.FloatControl;

/**
 * Controller for the settings popup.
 */
public class SettingsController {

    @FXML
    private StackPane slideButton1;
    @FXML
    private Rectangle background1;
    @FXML
    private Circle slider1;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider settingsFieldCount;

    @FXML
    private GridPane rootGridPane;
    @FXML
    private StackPane rootStackPane;
    @FXML
    private ImageView muteIcon;

    private boolean isSlider1Active;
    private boolean isMuted = false;

    /**
     * Initializes settings UI and loads current values.
     */
    public void initialize() {
        isMuted = GameSpectateController.isSoundMuted;
        Platform.runLater(() -> {
            Scene scene = rootStackPane.getScene();
            if (scene != null) {
                scene.widthProperty().addListener((obs, oldVal, newVal) -> adjustScaling());
                scene.heightProperty().addListener((obs, oldVal, newVal) -> adjustScaling());
                adjustScaling();
                registerEscHotkey(scene);
            }
        });

        GameSpectateController gameSpectateController = GameSpectateController.getInstance();
        FloatControl volumeControl = gameSpectateController.getVolumeControl();

        isSlider1Active = GameSpectateController.showID;
        updateSliderUI();

        volumeSlider.setMin(-15.0);
        volumeSlider.setMax(5.0);
        if (volumeControl != null) {
            volumeSlider.setValue(volumeControl.getValue());
        }
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            float volume = newValue.floatValue();
            gameSpectateController.unmuteSound(volume);
            isMuted = false;
            updateMuteIcon();
        });

        settingsFieldCount.valueProperty().addListener((observable, oldValue, newValue) -> {
            gameSpectateController.displayedFields = newValue.intValue();
            gameSpectateController.generateFields();
        });
        settingsFieldCount.setValue(gameSpectateController.displayedFields);

        updateMuteIcon();
    }

    /**
     * Adjusts scaling of the UI elements.
     */
    private void adjustScaling() {
        Scene scene = rootStackPane.getScene();
        if (scene == null) {
            return;
        }
        double sceneWidth = scene.getWidth();
        double sceneHeight = scene.getHeight();
        double internalWidth = 500;
        double internalHeight = 450;
        double scaleX = sceneWidth / internalWidth;
        double scaleY = sceneHeight / internalHeight;
        double scale = Math.min(scaleX, scaleY);
        rootGridPane.setScaleX(scale);
        rootGridPane.setScaleY(scale);
        rootGridPane.setPrefWidth(internalWidth);
        rootGridPane.setPrefHeight(internalHeight);
    }

    /**
     * Toggles a slider that shows or hides camel IDs.
     *
     * @param event mouse event on the slider
     */
    @FXML
    private void toggleSlide(MouseEvent event) {
        if (event.getSource() == slideButton1) {
            isSlider1Active = !isSlider1Active;
            GameSpectateController.showID = isSlider1Active;
            updateSliderUI();
            GameSpectateController.getInstance().generateFields();
        }
    }

    /**
     * Updates the UI appearance of the slider.
     */
    private void updateSliderUI() {
        TranslateTransition transition1 = new TranslateTransition(Duration.millis(200), slider1);
        if (isSlider1Active) {
            transition1.setToX(20);
            background1.setFill(Color.web("#007AFF"));
        } else {
            transition1.setToX(0);
            background1.setFill(Color.web("#ddd"));
        }
        transition1.play();
    }

    /**
     * Closes the settings window.
     *
     * @param event triggered event
     */
    public void handleClose(ActionEvent event) {
        GameSpectateController.isSoundMuted = isMuted;
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
     * Toggles sound mute/unmute.
     *
     * @param event triggered event
     */
    public void settingsMute(ActionEvent event) {
        GameSpectateController gameSpectateController = GameSpectateController.getInstance();
        if (isMuted) {
            float volume = (float) volumeSlider.getValue();
            gameSpectateController.unmuteSound(volume);
            isMuted = false;
        } else {
            gameSpectateController.muteSound();
            isMuted = true;
        }
        updateMuteIcon();
    }

    public void updateMuteIcon() {
        String iconPath = isMuted
            ? "/cameldown/camelup/client/observer/assets/GameSpectate/Mute.png"
            : "/cameldown/camelup/client/observer/assets/GameSpectate/Unmute.png";
        try {
            Image image = new Image(getClass().getResource(iconPath).toExternalForm());
            muteIcon.setImage(image);
        } catch (NullPointerException e) {
            System.err.println("Resource not found: " + iconPath);
        }
    }

}
