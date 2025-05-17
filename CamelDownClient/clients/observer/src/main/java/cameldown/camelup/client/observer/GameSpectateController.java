package cameldown.camelup.client.observer;

import cameldown.camelup.client.core.api.Api;
import cameldown.camelup.interfacedefinition.game.v3.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import javax.sound.sampled.*;


/**
 * Controller for spectating a running game. Provides UI updates, timers, notifications,
 * and methods to center the view on camels, fields, or player actions.
 */
public class GameSpectateController implements Controller {
    private static final Logger logger = LoggerFactory.getLogger(GameSpectateController.class);

    /**
     * Current game state.
     */
    public static GameState gameState;

    /**
     * Field ID for info popup usage.
     */
    public static int infoField;

    /**
     * If true, camel IDs are shown on top of their images.
     */
    public static boolean showID = true;

    /**
     * Automatically center on the front camel when the game is first initialized.
     */
    public static boolean autoCenterFirst = true;

    /**
     * Automatically center on the most recently moved camel on every update.
     */
    public static boolean autoCenterUpdate = false;

    /**
     * Indicates if sound is currently muted.
     */
    public static boolean isSoundMuted = false;

    private static GameSpectateController instance;

    /**
     * Number of fields displayed.
     */
    public int displayedFields = 19;

    public ImageView btnSettings;
    public int currentPlayerID = -1;
    public String currentPlayerName;
    public int currentDiceSize = 0;
    public int currentFinalBetsSizeFirst = 0;
    public int currentFinalBetsSizeLast = 0;
    public int currentPlayerCardCount = 0;
    public int currentPlayerCardField;
    public List<BoardSpace> currentBoardSpaces;
    public int currentBettingCardCount = 0;
    public int currentDiceCamelID;
    public int currentDiceValue;
    public int centerBackCamel = -1;

    @FXML
    private Label GameTimer;
    private Timeline gameTimerTimeline;
    private long moveTimeRemaining;

    @FXML
    private Label playerTimer;
    private Timeline playerTimerTimeline;
    private long playerTimeRemaining;

    private Api api;

    @FXML
    private Button btnDicePyramide;
    @FXML
    private Button btnEndPlayerCard;
    @FXML
    private Label playerIDPlayer0;
    @FXML
    private Label playerIDPlayer1;
    @FXML
    private Label playerIDPlayer2;
    @FXML
    private Label playerIDPlayer3;
    @FXML
    private Label playerIDPlayer4;
    @FXML
    private Label playerIDPlayer5;
    @FXML
    private Label coinCountPlayer0;
    @FXML
    private Label coinCountPlayer1;
    @FXML
    private Label coinCountPlayer2;
    @FXML
    private Label coinCountPlayer3;
    @FXML
    private Label coinCountPlayer4;
    @FXML
    private Label coinCountPlayer5;
    @FXML
    private HBox player0;
    @FXML
    private HBox player1;
    @FXML
    private HBox player2;
    @FXML
    private HBox player3;
    @FXML
    private HBox player4;
    @FXML
    private HBox player5;
    @FXML
    private HBox rolledDiceBox;
    @FXML
    private Pane fieldContainer;
    @FXML
    private Pane notificationBox;
    @FXML
    private Pane gameTextbox;

    /**
     * Total number of fields in the game.
     */
    private int fieldCount;

    /**
     * Current centered field index.
     */
    private int fieldCentered;

    @FXML
    private TextField centerTextField0;
    @FXML
    private TextField centerTextField1;
    private Clip clip;

    @FXML
    private Label turnCount;

    @FXML
    private ImageView playerPicture0;
    @FXML
    private ImageView playerPicture1;
    @FXML
    private ImageView playerPicture2;
    @FXML
    private ImageView playerPicture3;
    @FXML
    private ImageView playerPicture4;
    @FXML
    private ImageView playerPicture5;
    @FXML
    private GridPane rootGridPane;
    @FXML
    private StackPane rootStackPane;

    @FXML
    private Text gameTextboxHeadline;
    @FXML
    private Text gameTextboxLine0;
    @FXML
    private Text gameTextboxLine1;
    @FXML
    private Text gameTextboxLine2;
    @FXML
    private Text gameTextboxLine3;
    @FXML
    private Text gameTextboxLine4;
    @FXML
    private Text gameTextboxLine5;
    @FXML
    private Text gameTextboxLine6;

    private Image fieldBorder;
    private Image goalLine;
    private double centerX;
    private double centerY;
    private double radius;

    @FXML
    private StackPane slideButton1;
    @FXML
    private Rectangle background1;
    @FXML
    private Circle slider1;
    @FXML
    private StackPane slideButton2;
    @FXML
    private Rectangle background2;
    @FXML
    private Circle slider2;
    private boolean isSlider1Active;
    private boolean isSlider2Active;

    /**
     * Creates a new controller instance (singleton pattern).
     */
    public GameSpectateController() {
        instance = this;
    }

    /**
     * Returns the singleton instance.
     *
     * @return GameSpectateController instance
     */
    public static GameSpectateController getInstance() {
        return instance;
    }

    /**
     * Checks if a given string can be parsed to an integer.
     *
     * @param text the string to check
     * @return true if it's an integer, false otherwise
     */
    public static boolean isInteger(String text) {
        if (text.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Refreshes the game state by updating this controller with new data.
     *
     * @param newGameState the new game state
     */
    public static void refreshGameState(GameState newGameState) {
        instance.updateGameSpectateController(newGameState);
    }

    /**
     * Refreshes the scoreboard at the end of a game.
     *
     * @param newGameEnd final game results
     */
    public static void refreshGameEnd(GameEnd newGameEnd) {
        List<Integer> leaderboardAsList = new ArrayList<>(newGameEnd.getLeaderboard());
        GameSpectateController.getInstance().updateScoreboard(leaderboardAsList);
    }

    /**
     * Updates the final scoreboard with endgame information.
     *
     * @param leaderboardSet Set von Spieler-IDs, in Reihenfolge ihrer Platzierung
     */
    public void updateScoreboard(List<Integer> leaderboardSet) {
        int playerCount = gameState.getGameConfig().getPlayerCount();
        List<Integer> leaderboard = new ArrayList<>(leaderboardSet);
        Text[] lines = new Text[]{
            gameTextboxLine1,
            gameTextboxLine2,
            gameTextboxLine3,
            gameTextboxLine4,
            gameTextboxLine5,
            gameTextboxLine6
        };
        gameTextboxLine0.setVisible(true);
        for (int i = 0; i < lines.length; i++) {
            if (i < playerCount && i < leaderboard.size()) {
                int currentPlayerId = leaderboard.get(i);
                String playerName = null;
                String playerMoney = null;
                for (Player p : gameState.getPlayers()) {
                    if (p.getPlayerId() == currentPlayerId) {
                        playerName = p.getName();
                        playerMoney = p.getMoney().toString();
                        break;
                    }
                }
                lines[i].setText((i + 1) + ". " + playerName + " mit " + playerMoney + " Münzen.");
                lines[i].setVisible(true);
            } else {
                lines[i].setVisible(false);
            }
        }
    }

    /**
     * Displays a notification message for a short time.
     *
     * @param message the message to display
     */
    public void showNotification(String message) {
        Label notification = new Label(message);
        notification.setPrefWidth(500);
        notification.setPrefHeight(36);
        notification.setAlignment(Pos.CENTER_LEFT);
        notification.setFont(new Font("Arial", 16));
        notification.setTextFill(Color.BLACK);
        notification.setStyle("-fx-background-color: rgba(255,255,255,0.7); -fx-background-radius: 12;");
        notification.setPadding(new Insets(0, 0, 0, 10));
        notificationBox.getChildren().add(0, notification);
        VBox.setMargin(notification, new Insets(10, 0, 0, 0));

        Timeline timeline = new Timeline(new KeyFrame(
            Duration.seconds(8),
            evt -> {
                Platform.runLater(() -> {
                    notificationBox.getChildren().remove(notification);
                });
            }
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }

    /**
     * Initializes the controller after the API is loaded.
     */
    public void initializeController() {
        gameState = GameListController.gameStateGameList;
        if (gameState.getGameConfig().getNumberOfSpaces() <= displayedFields) {
            autoCenterFirst = false;
        }
        fieldCount = gameState.getGameConfig().getNumberOfSpaces();
        currentBoardSpaces = gameState.getBoardSpaces();
        if (autoCenterFirst) {
            List<BoardSpace> boardSpaces = gameState.getBoardSpaces();
            int lastFieldWithCamel = -1;
            for (int i = boardSpaces.size() - 1; i >= 0; i--) {
                BoardSpace boardSpace = boardSpaces.get(i);
                if (!boardSpace.getCamelIds().isEmpty() && boardSpace.getCamelIds().stream().anyMatch(id -> id >= 0)) {
                    lastFieldWithCamel = i;
                    break;
                }
            }
            fieldCentered = lastFieldWithCamel;
        } else {
            fieldCentered = 0;
        }

        String soundFilePath = "/cameldown/camelup/client/observer/assets/GameSpectate/CamelUPSoundtrackGameBoard_converted.wav";
        InputStream audioStream = getClass().getResourceAsStream(soundFilePath);
//        InputStream audioStream = getClass().getResourceAsStream("soundFilePath");
        if (audioStream != null && !isSoundMuted) {
//            playSound(audioStream);
        }

        GameState.GamePhase gamePhase = gameState.getGamePhase();
        moveTimeRemaining = gameState.getMoveTimeRemaining();
        playerTimeRemaining = gameState.getGameConfig().getMaxGameDuration() - gameState.getGameDuration();
        if (gamePhase == GameState.GamePhase.PLAYING || gamePhase == GameState.GamePhase.VISUALIZING) {
            startGameTimer();
            startPlayerTimer();
        } else {
            long moveTimer = moveTimeRemaining / 1000;
            updateGameTimerLabel(moveTimer);
            long playerTimer = playerTimeRemaining / 1000;
            updatePlayerTimerLabel(playerTimer);
        }
        isSlider2Active = autoCenterFirst;
        isSlider1Active = autoCenterUpdate;
        updatePlayerLabels();
        updatePlayerPictures();
        generateFields();
        slideButton1.setCursor(Cursor.HAND);
        slideButton2.setCursor(Cursor.HAND);
        updateSliderUI();

        Platform.runLater(() -> {
            Scene scene = rootStackPane.getScene();
            if (scene != null) {
                scene.widthProperty().addListener((obs, oldVal, newVal) -> adjustScaling());
                scene.heightProperty().addListener((obs, oldVal, newVal) -> adjustScaling());
                adjustScaling();
                registerHotkeys(scene);
                rootStackPane.requestFocus();
            }
            if (gameState.getGamePhase() == GameState.GamePhase.VISUALIZING) {
                try {
                    SuccessFeedback moveVisualizedSuccessFeedback = api.moveVisualized().get();
                    if (!moveVisualizedSuccessFeedback.getSuccess()) {
                        logger.error("Could not notify server of completed move visualization because {}",
                            moveVisualizedSuccessFeedback.getError());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Handles new game state events and updates the spectate UI.
     *
     * @param newGameState the new game state
     */
    public void updateGameSpectateController(GameState newGameState) {
        Platform.runLater(() -> {
            gameState = newGameState;
            GameState.GamePhase gamePhase = gameState.getGamePhase();
            moveTimeRemaining = gameState.getMoveTimeRemaining();
            playerTimeRemaining = gameState.getGameConfig().getMaxGameDuration() - gameState.getGameDuration();
            if (gamePhase == GameState.GamePhase.PLAYING) {
                startGameTimer();
                startPlayerTimer();
            } else if (gamePhase == GameState.GamePhase.PAUSED || gamePhase == GameState.GamePhase.FINISHED) {
                if (playerTimerTimeline != null) {
                    playerTimerTimeline.stop();
                    gameTimerTimeline.stop();
                }
            }

            if (currentDiceSize != gameState.getRolledDice().size() && !gameState.getRolledDice().isEmpty()) {
                currentDiceCamelID = gameState.getRolledDice().stream().toList().getLast().getCamelId();
                currentDiceValue = gameState.getRolledDice().stream().toList().getLast().getNumber();
            }
            if (currentBoardSpaces.isEmpty()) {
                currentBoardSpaces = gameState.getBoardSpaces();
            }

            autoCenter();

            updatePlayerLabels();
            updatePlayerPictures();
            generateFields();

            if (gameState.getGamePhase() == GameState.GamePhase.VISUALIZING) {
                try {
                    SuccessFeedback moveVisualizedSuccessFeedback = api.moveVisualized().get();
                    if (!moveVisualizedSuccessFeedback.getSuccess()) {
                        logger.error("Could not notify server of completed move visualization because {}",
                            moveVisualizedSuccessFeedback.getError());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

            // **German text in the game UI**:
            if (!gameState.getPlayers().isEmpty()
                && currentPlayerID != gameState.getPlayers().get(0).getPlayerId()
                && gameState.getGamePhase() == GameState.GamePhase.PLAYING
            ) {
                generateNotification();
            }
        });
    }

    /**
     * Registers keyboard shortcuts for various actions.
     *
     * @param scene the main scene
     */
    public void registerHotkeys(Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();

            if (event.isControlDown()) {
                rootStackPane.requestFocus();
            }

            if (code == KeyCode.LEFT && event.isControlDown() && !event.isShiftDown()) {
                left(new ActionEvent());
            } else if (code == KeyCode.RIGHT && event.isControlDown() && !event.isShiftDown()) {
                right(new ActionEvent());
            } else if (code == KeyCode.LEFT && event.isControlDown() && event.isShiftDown()) {
                leftNotEmpty(new ActionEvent());
            } else if (code == KeyCode.RIGHT && event.isControlDown() && event.isShiftDown()) {
                rightNotEmpty(new ActionEvent());
            } else if (code == KeyCode.G && event.isControlDown()) {
                centerTextField0.requestFocus();
            } else if (code == KeyCode.F && event.isControlDown()) {
                centerTextField1.requestFocus();
            } else if (code == KeyCode.I && event.isControlDown()) {
                handleOpenInfo(new ActionEvent());
            } else if (code == KeyCode.E && event.isControlDown()) {
                handleOpenSettings(new ActionEvent());
            } else if (code == KeyCode.W && event.isControlDown()) {
                handleOpenEndBetting(new ActionEvent());
            } else if (code == KeyCode.Q && event.isControlDown()) {
                handleOpenBetting(new ActionEvent());
            } else if (code == KeyCode.A && event.isControlDown()) {
                firstCamel(new ActionEvent());
            } else if (code == KeyCode.D && event.isControlDown()) {
                autoCenterUpdate = true;
                isSlider1Active = true;
                autoCenterFirst = false;
                isSlider2Active = false;
                updateSliderUI();
                autoCenter();
            } else if (code == KeyCode.S && event.isControlDown()) {
                backCamel(new ActionEvent());
            } else if (code == KeyCode.R && event.isControlDown()) {
                autoCenterFirst = true;
                isSlider2Active = true;
                autoCenterUpdate = false;
                isSlider1Active = false;
                updateSliderUI();
                autoCenter();
            } else if (code == KeyCode.T && event.isControlDown()) {
                handleOpenDice(new ActionEvent());
            } else if (code == KeyCode.H && event.isControlDown()) {
                handleOpenHotkey(new ActionEvent());
            } else if (code == KeyCode.M && event.isControlDown()) {
                if (!isSoundMuted) {
                    stopSound();
                } else {
                    String soundFilePath = "/cameldown/camelup/client/observer/assets/GameSpectate/CamelUPSoundtrackGameBoard_converted.wav";
                    InputStream audioStream = getClass().getResourceAsStream(soundFilePath);
                    if (audioStream != null) {
//                        playSound(audioStream);
                    }
                }
            }
        });
    }

    /**
     * Centers the view on camels or fields automatically depending on settings.
     */
    public void autoCenter() {
        if (autoCenterFirst) {
            List<BoardSpace> boardSpaces = gameState.getBoardSpaces();
            int lastFieldWithCamel = -1;
            for (int i = boardSpaces.size() - 1; i >= 0; i--) {
                BoardSpace boardSpace = boardSpaces.get(i);
                if (!boardSpace.getCamelIds().isEmpty() && boardSpace.getCamelIds().stream().anyMatch(id -> id >= 0)) {
                    lastFieldWithCamel = i;
                    break;
                }
            }
            fieldCentered = lastFieldWithCamel;
        } else if (autoCenterUpdate) {
            if (currentDiceSize != gameState.getRolledDice().size()) {
                List<BoardSpace> boardSpaces = gameState.getBoardSpaces();
                int lastField = -1;
                for (int i = 0; i >= 0; i++) {
                    if (i < boardSpaces.size()) {
                        BoardSpace boardSpace = boardSpaces.get(i);
                        if (!boardSpace.getCamelIds().isEmpty()
                            && boardSpace.getCamelIds().stream().anyMatch(id -> id == currentDiceCamelID)) {
                            lastField = i;
                            break;
                        }
                    }
                }
                fieldCentered = lastField;
            } else if (currentPlayerCardCount != newPlayerCardCount()) {
                fieldCentered = lastPlayerCardField(false);
            }
        }
    }

    /**
     * Toggles the slider states for auto-centering.
     *
     * @param event mouse event
     */
    @FXML
    private void toggleSlide(MouseEvent event) {
        if (event.getSource() == slideButton1) {
            isSlider1Active = !isSlider1Active;
            if (isSlider1Active) {
                isSlider2Active = false;
                autoCenterFirst = false;
            }

            autoCenterUpdate = isSlider1Active;

            updateSliderUI();
            autoCenter();
            generateFields();
        }
        if (event.getSource() == slideButton2) {
            isSlider2Active = !isSlider2Active;
            if (isSlider2Active) {
                isSlider1Active = false;
                autoCenterUpdate = false;
            }

            autoCenterFirst = isSlider2Active;

            updateSliderUI();
            autoCenter();
            generateFields();
        }
    }

    /**
     * Updates the UI of the sliders.
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
        TranslateTransition transition2 = new TranslateTransition(Duration.millis(200), slider2);
        if (isSlider2Active) {
            transition2.setToX(20);
            background2.setFill(Color.web("#007AFF"));
        } else {
            transition2.setToX(0);
            background2.setFill(Color.web("#ddd"));
        }
        transition1.play();
        transition2.play();
    }

    /**
     * Generates a notification about the last player action (German text in UI).
     */
    public void generateNotification() {
        if (currentPlayerName == null) {
            currentPlayerName = gameState.getPlayers().get(gameState.getPlayers().size() - 1).getName();
        }

        String lastAction = "";
        // **German text in the game UI**:
        if (currentDiceSize != gameState.getRolledDice().size()) {
            lastAction = "für Kamel " + currentDiceCamelID + " die Zahl " + currentDiceValue + " gewürfelt.";
        } else if (currentFinalBetsSizeFirst != gameState.getFinalBets().getFirstCamel().size()) {
            lastAction = "auf den Gewinner des Spiels gewettet.";
        } else if (currentFinalBetsSizeLast != gameState.getFinalBets().getLastCamel().size()) {
            lastAction = "auf den Verlierer des Spiels gewettet.";
        } else if (currentPlayerCardCount != newPlayerCardCount()) {
            currentPlayerCardField = lastPlayerCardField(true);
            lastAction = "ein Zuschauerplättchen auf Feld " + (currentPlayerCardField + 1) + " platziert.";
        } else if (currentBettingCardCount != newBettingCardCount()) {
            lastAction = "auf den Etappensieger gewettet.";
        }

        if (!lastAction.isBlank()) {
            String notification = currentPlayerName + " hat " + lastAction;
            showNotification(notification);
        }

        currentPlayerName = gameState.getPlayers().get(0).getName();
        currentPlayerID = gameState.getPlayers().get(0).getPlayerId();
        currentDiceSize = gameState.getRolledDice().size();
        currentFinalBetsSizeFirst = gameState.getFinalBets().getFirstCamel().size();
        currentFinalBetsSizeLast = gameState.getFinalBets().getLastCamel().size();
        currentPlayerCardCount = newPlayerCardCount();
        currentPlayerCardField = lastPlayerCardField(true);
        currentBettingCardCount = newBettingCardCount();
    }

    /**
     * Counts how many player cards are currently on the board.
     *
     * @return count of player cards
     */
    public int newPlayerCardCount() {
        int count = 0;
        List<BoardSpace> boardSpaces = gameState.getBoardSpaces();
        if (boardSpaces != null) {
            for (BoardSpace space : boardSpaces) {
                if (space.getPlayerCard() != null) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Finds the field index of the last placed player card.
     *
     * @param setBoardSpaces if true, updates current board spaces
     * @return the field index of the last placed player card
     */
    public int lastPlayerCardField(boolean setBoardSpaces) {
        List<BoardSpace> newBoardSpaces = gameState.getBoardSpaces();
        int newField = 0;
        for (int i = 0; i < newBoardSpaces.size(); i++) {
            if (newBoardSpaces.get(i).getPlayerCard() != null) {
                if (currentBoardSpaces.get(i).getPlayerCard() == null) {
                    newField = i;
                    if (setBoardSpaces) {
                        currentBoardSpaces = gameState.getBoardSpaces();
                    }
                    break;
                }
            }
        }
        return newField;
    }

    /**
     * Counts how many betting cards have been taken so far.
     *
     * @return count of taken betting cards
     */
    public int newBettingCardCount() {
        int maxPlayerCards = (gameState.getGameConfig().getCamels().size() - 2)
            * gameState.getGameConfig().getNumberOfBettingCards();
        int count = gameState.getBettingCards().stream()
            .mapToInt(BettingCards::getAmount)
            .sum();
        return maxPlayerCards - count;
    }

    /**
     * Redraws the entire field view based on current states and positions.
     */
    void generateFields() {
        fieldContainer.getChildren().clear();
        populateRolledDiceBox();
        updateTurnCount();
        drawBoardBackground();

        int N = (displayedFields - 1) / 2;

        if (fieldCount > displayedFields) {
            double angleStep = 360.0 / (displayedFields
                + ((displayedFields >= 17) ? 5 : (displayedFields >= 15) ? 4 : (displayedFields >= 9) ? 3 : 2));
            double extraAngle = 90 - (angleStep * displayedFields / 2);
            ImageView extraImageView = createFieldImageView(-1, fieldBorder, goalLine);
            positionFieldImageView(extraImageView, extraAngle, centerX, centerY, radius);
            fieldContainer.getChildren().add(extraImageView);
        }

        for (int i = 0; i < fieldCount; i++) {
            int distance = (i - fieldCentered + fieldCount) % fieldCount;
            if (distance > fieldCount / 2) {
                distance = fieldCount - distance;
            }

            if (distance > N) {
                continue;
            }

            ImageView imageView = createFieldImageView(i, fieldBorder, goalLine);
            double[] angles = calculateAngles(i);
            double angle = angles[0];
            double angleNumber = angles[1];
            positionFieldImageView(imageView, angle, centerX, centerY, radius);
            fieldContainer.getChildren().add(imageView);
            drawNumberCircle(i, angleNumber, centerX, centerY, radius);
            placeCamelsOnField(i, angleNumber, centerX, centerY, radius);
            drawPlayerCard(i, angleNumber, centerX, centerY, radius);
        }

        updateGameTextbox();
    }

    /**
     * Updates the game textbox UI depending on the current game phase.
     */
    public void updateGameTextbox() {
        // **German text in the game UI**:
        if (gameState.getGamePhase() == GameState.GamePhase.PLAYING
            || gameState.getGamePhase() == GameState.GamePhase.VISUALIZING) {
            gameTextbox.setMouseTransparent(true);
            gameTextbox.setVisible(false);
        } else if (gameState.getGamePhase() == GameState.GamePhase.PAUSED) {
            gameTextbox.setMouseTransparent(false);
            gameTextbox.setVisible(true);
            gameTextboxHeadline.setText("Spiel pausiert.");
            gameTextboxLine0.setVisible(false);
            gameTextboxLine1.setVisible(false);
            gameTextboxLine2.setVisible(false);
            gameTextboxLine3.setVisible(false);
            gameTextboxLine4.setVisible(false);
            gameTextboxLine5.setVisible(false);
            gameTextboxLine6.setVisible(false);
        } else if (gameState.getGamePhase() == GameState.GamePhase.CREATED) {
            gameTextbox.setMouseTransparent(false);
            gameTextbox.setVisible(true);
            gameTextboxHeadline.setText("Warte auf Spielstart.");
            gameTextboxLine0.setVisible(true);
            gameTextboxLine0.setText("Warte auf Spieler " + gameState.getPlayers().size()
                + " / " + gameState.getGameConfig().getPlayerCount());
            gameTextboxLine1.setVisible(false);
            gameTextboxLine2.setVisible(false);
            gameTextboxLine3.setVisible(false);
            gameTextboxLine4.setVisible(false);
            gameTextboxLine5.setVisible(false);
            gameTextboxLine6.setVisible(false);
        } else if (gameState.getGamePhase() == GameState.GamePhase.FINISHED) {
            gameTextbox.setMouseTransparent(false);
            gameTextbox.setVisible(true);
            gameTextboxHeadline.setText("Spiel beendet");
            gameTextboxLine0.setText("Endplatzierung der Spieler:");
        }
    }

    /**
     * Plays background sound in a loop.
     *
     * @param audioStream audio input stream
     */
    public void playSound(InputStream audioStream) {
        isSoundMuted = false;
        try (BufferedInputStream bis = new BufferedInputStream(audioStream);
             AudioInputStream ais = AudioSystem.getAudioInputStream(bis)) {

            clip = AudioSystem.getClip();
            clip.open(ais);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-8.0f);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the currently playing sound.
     */
    public void stopSound() {
        isSoundMuted = true;
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    /**
     * Mutes the currently playing sound.
     */
    public void muteSound() {
        isSoundMuted = true;
        if (clip != null && clip.isOpen()) {
            clip.stop();
        }
    }

    /**
     * Unmutes sound and resets it to a given volume.
     *
     * @param volume the target volume
     */
    public void unmuteSound(float volume) {
        if (clip != null && clip.isOpen()) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume);
            clip.setFramePosition(0);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            String soundFilePath = "/cameldown/camelup/client/observer/assets/GameSpectate/CamelUPSoundtrackGameBoard_converted.wav";
            InputStream audioStream = getClass().getResourceAsStream(soundFilePath);
            if (audioStream != null) {
//                playSound(audioStream);
            }
        }
    }

    /**
     * Retrieves the volume control of the audio clip.
     *
     * @return FloatControl instance or null if unavailable
     */
    public FloatControl getVolumeControl() {
        if (clip != null && clip.isOpen()) {
            return (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        }
        return null;
    }

    /**
     * Leaves the lobby and returns to the game list.
     *
     * @param event the event triggering this action
     * @throws IOException if loading the scene fails
     */
    @FXML
    private void leaveLobby(Event event) throws IOException {
        if (!isSoundMuted) {
            stopSound();
            isSoundMuted = false;
        }
        api.leaveLobby();
        gameState = null;
        App.setRoot("GameList");
    }

    /**
     * Adjusts the scaling of the UI to fit the scene size.
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
     * Starts the game timer countdown.
     */
    private void startGameTimer() {
        if (gameTimerTimeline != null) {
            gameTimerTimeline.stop();
        }

        long initialTimeInSeconds = moveTimeRemaining / 1000;
        updateGameTimerLabel(initialTimeInSeconds);

        gameTimerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            moveTimeRemaining -= 1000;
            long timeInSeconds = moveTimeRemaining / 1000;

            if (moveTimeRemaining <= 0) {
                gameTimerTimeline.stop();
                GameTimer.setText("00:00");
            } else {
                updateGameTimerLabel(timeInSeconds);
            }
        }));
        gameTimerTimeline.setCycleCount(Timeline.INDEFINITE);
        gameTimerTimeline.play();
    }

    /**
     * Starts the player timer countdown.
     */
    private void startPlayerTimer() {
        if (playerTimerTimeline != null) {
            playerTimerTimeline.stop();
        }

        long initialTimeInSeconds = playerTimeRemaining / 1000;
        updatePlayerTimerLabel(initialTimeInSeconds);

        playerTimerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            playerTimeRemaining -= 1000;
            long timeInSeconds = playerTimeRemaining / 1000;

            if (playerTimeRemaining <= 0) {
                playerTimerTimeline.stop();
                playerTimer.setText("00:00");
            } else {
                updatePlayerTimerLabel(timeInSeconds);
            }
        }));
        playerTimerTimeline.setCycleCount(Timeline.INDEFINITE);
        playerTimerTimeline.play();
    }

    /**
     * Updates the game timer label.
     *
     * @param seconds remaining seconds
     */
    private void updateGameTimerLabel(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        String timeString = String.format("%02d:%02d", minutes, remainingSeconds);
        GameTimer.setText(timeString);
        if (seconds <= 10) {
            GameTimer.setTextFill(Color.web("#eb0b0b"));
        } else {
            GameTimer.setTextFill(Color.web("#2cf536"));
        }
    }

    /**
     * Updates the player timer label.
     *
     * @param seconds remaining seconds
     */
    private void updatePlayerTimerLabel(long seconds) {
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        String timeString = String.format("%02d:%02d", minutes, remainingSeconds);
        playerTimer.setText(timeString);
    }

    /**
     * Updates the player pictures with a color-coded image.
     */
    private void updatePlayerPictures() {
        ImageView[] playerPictures = new ImageView[]{
            playerPicture0, playerPicture1, playerPicture2,
            playerPicture3, playerPicture4, playerPicture5
        };
        for (int i = 0; i < playerPictures.length; i++) {
            ImageView playerPicture = playerPictures[i];
            if (playerPicture != null) {
                Color playerColor = getPlayerColor(i);
                String resourcePath = "/cameldown/camelup/client/observer/assets/GameSpectate/PicturePlayer.png";
                InputStream is = getClass().getResourceAsStream(resourcePath);
                if (is == null) {
                    continue;
                }
                Image baseImage = new Image(is);
                Image coloredImage = replaceColor(baseImage, playerColor);
                playerPicture.setImage(coloredImage);
            }
        }
    }

    /**
     * Determines player color by ID.
     *
     * @param playerId the player ID
     * @return the associated Color
     */
    private Color getPlayerColor(int playerId) {
        switch (playerId) {
            case 0:
                return Color.BLUE;
            case 1:
                return Color.RED;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.YELLOW;
            case 4:
                return Color.ORANGE;
            case 5:
                return Color.PURPLE;
            default:
                return Color.BLACK;
        }
    }

    /**
     * Updates player labels and coin counts.
     */
    public void updatePlayerLabels() {
        List<Player> players = new ArrayList<>(gameState.getPlayers());
        int currentPlayerId = -1;
        if (!players.isEmpty()) {
            currentPlayerId = players.get(0).getPlayerId();
        }
        players.sort(Comparator.comparingInt(Player::getPlayerId)); // Sortiere Spieler nach ID

        Label[] playerLabels = new Label[]{
            playerIDPlayer0, playerIDPlayer1, playerIDPlayer2,
            playerIDPlayer3, playerIDPlayer4, playerIDPlayer5
        };
        Label[] coinLabels = new Label[]{
            coinCountPlayer0, coinCountPlayer1, coinCountPlayer2,
            coinCountPlayer3, coinCountPlayer4, coinCountPlayer5
        };
        HBox[] playerBoxes = new HBox[]{
            player0, player1, player2, player3, player4, player5
        };

        // Verstecke nicht benötigte Spielerboxen
        for (int i = gameState.getGameConfig().getPlayerCount(); i < 6; i++) {
            playerBoxes[i].setVisible(false);
        }

        // Aktualisiere die Spielerlabels und Münzlabels
        for (int i = 0; i < players.size() && i < 6; i++) {
            Player player = players.get(i);
            playerLabels[i].setText(player.getName());
            coinLabels[i].setText(String.valueOf(player.getMoney()));
            playerBoxes[i].setVisible(true);

            // Markiere den Namen und die Münzen des eigenen Clients fett
            if (player.getPlayerId() == api.getClientId()) {
                playerLabels[i].setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
                coinLabels[i].setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
            } else {
                playerLabels[i].setStyle("-fx-font-size: 18px; -fx-font-weight: normal;");
                coinLabels[i].setStyle("-fx-font-size: 18px; -fx-font-weight: normal;");
            }

            // Passe die Boxgröße für den aktuellen Spieler an
            if (player.getPlayerId() == currentPlayerId) {
                playerBoxes[i].setPrefHeight(80.0);
                playerBoxes[i].setPrefWidth(500.0);
            } else {
                playerBoxes[i].setPrefHeight(75.0);
                playerBoxes[i].setPrefWidth(450.0);
            }
        }
    }

    /**
     * Moves the view to the next field to the left.
     *
     * @param e action event
     */
    public void left(ActionEvent e) {
        if (fieldCentered < (fieldCount - 1)) {
            fieldCentered++;
        } else {
            fieldCentered = 0;
        }
        autoCenterUpdate = false;
        autoCenterFirst = false;
        isSlider1Active = false;
        isSlider2Active = false;
        updateSliderUI();
        generateFields();
    }

    /**
     * Moves the view to the next field on the left that has a camel or a player card.
     *
     * @param e action event
     */
    public void leftNotEmpty(ActionEvent e) {
        List<BoardSpace> boardSpaces = gameState.getBoardSpaces();
        int i = fieldCentered + 1;
        while (i < boardSpaces.size()) {
            BoardSpace boardSpace = boardSpaces.get(i);
            boolean hasCamel = !boardSpace.getCamelIds().isEmpty()
                && boardSpace.getCamelIds().stream().anyMatch(id -> id >= -2);
            boolean hasPlayerCard = boardSpace.getPlayerCard() != null;
            if (hasCamel || hasPlayerCard) {
                fieldCentered = i;
                break;
            }
            i++;
        }
        if (i >= boardSpaces.size()) {
            fieldCentered = boardSpaces.size() - 1;
        }
        autoCenterUpdate = false;
        autoCenterFirst = false;
        isSlider1Active = false;
        isSlider2Active = false;
        updateSliderUI();
        generateFields();
    }

    /**
     * Moves the view to the next field on the right that has a camel or a player card.
     *
     * @param e action event
     */
    public void rightNotEmpty(ActionEvent e) {
        List<BoardSpace> boardSpaces = gameState.getBoardSpaces();
        int i = fieldCentered - 1;
        while (i >= 0) {
            BoardSpace boardSpace = boardSpaces.get(i);
            boolean hasCamel = !boardSpace.getCamelIds().isEmpty()
                && boardSpace.getCamelIds().stream().anyMatch(id -> id >= -2);
            boolean hasPlayerCard = boardSpace.getPlayerCard() != null;
            if (hasCamel || hasPlayerCard) {
                fieldCentered = i;
                break;
            }
            i--;
        }
        if (i < 0) {
            fieldCentered = 0;
        }
        autoCenterUpdate = false;
        autoCenterFirst = false;
        isSlider1Active = false;
        isSlider2Active = false;
        updateSliderUI();
        generateFields();
    }

    /**
     * Moves the view to the next field to the right.
     *
     * @param e action event
     */
    public void right(ActionEvent e) {
        if (fieldCentered == 0) {
            fieldCentered = (fieldCount - 1);
        } else {
            fieldCentered--;
        }
        autoCenterUpdate = false;
        autoCenterFirst = false;
        isSlider1Active = false;
        isSlider2Active = false;
        updateSliderUI();
        generateFields();
    }

    /**
     * Centers the view on the first (leading) camel in the race.
     *
     * @param e action event
     */
    public void firstCamel(ActionEvent e) {
        List<BoardSpace> boardSpaces = gameState.getBoardSpaces();
        int lastFieldWithCamel = -1;
        for (int i = boardSpaces.size() - 1; i >= 0; i--) {
            BoardSpace boardSpace = boardSpaces.get(i);
            if (!boardSpace.getCamelIds().isEmpty() && boardSpace.getCamelIds().stream().anyMatch(id -> id >= 0)) {
                lastFieldWithCamel = i;
                break;
            }
        }
        fieldCentered = lastFieldWithCamel;
        autoCenterUpdate = false;
        autoCenterFirst = false;
        isSlider1Active = false;
        isSlider2Active = false;
        updateSliderUI();
        generateFields();
    }

    /**
     * Centers on the back camel, toggling between IDs -1 and -2.
     *
     * @param e action event
     */
    public void backCamel(ActionEvent e) {
        centerOnCamelById(centerBackCamel);
        if (centerBackCamel == -1) {
            centerBackCamel = -2;
        } else {
            centerBackCamel = -1;
        }
        autoCenterUpdate = false;
        autoCenterFirst = false;
        isSlider1Active = false;
        isSlider2Active = false;
        updateSliderUI();
        generateFields();
    }

    /**
     * Centers on a specified field.
     *
     * @param e action event
     */
    public void centerOnField(ActionEvent e) {
        if (isInteger(centerTextField0.getText())
            && Integer.parseInt(centerTextField0.getText()) <= fieldCount) {
            fieldCentered = Integer.parseInt(centerTextField0.getText()) - 1;
            centerTextField0.setText("");
            autoCenterUpdate = false;
            autoCenterFirst = false;
            isSlider1Active = false;
            isSlider2Active = false;
            updateSliderUI();
            generateFields();
        }
    }

    /**
     * Centers on a specified camel by ID.
     *
     * @param e action event
     */
    public void centerOnCamel(ActionEvent e) {
        String text = centerTextField1.getText();
        if (isInteger(text)) {
            int camelId = Integer.parseInt(text);
            centerOnCamelById(camelId);
            centerTextField1.setText("");
        }
    }

    /**
     * Centers the view on a given camel ID.
     *
     * @param camelId the camel ID
     */
    public void centerOnCamelById(int camelId) {
        List<BoardSpace> boardSpaces = gameState.getBoardSpaces();
        for (int i = 0; i < boardSpaces.size(); i++) {
            BoardSpace boardSpace = boardSpaces.get(i);
            Set<Integer> camelIds = boardSpace.getCamelIds();

            if (camelIds.contains(camelId)) {
                fieldCentered = i;
                autoCenterUpdate = false;
                autoCenterFirst = false;
                isSlider1Active = false;
                isSlider2Active = false;
                updateSliderUI();
                generateFields();
                break;
            }
        }
    }

    /**
     * Draws the board background image depending on the board size.
     */
    private void drawBoardBackground() {
        boolean splitBoard = fieldCount > displayedFields;

        fieldBorder = new Image(getClass().getResourceAsStream(
            "/cameldown/camelup/client/observer/assets/GameSpectate/field_border3.png"));
        goalLine = new Image(getClass().getResourceAsStream(
            "/cameldown/camelup/client/observer/assets/GameSpectate/goal_line.png"));

        centerX = 700;
        centerY = 450;
        radius = 315;

        ImageView circle = new ImageView(new Image(getClass().getResourceAsStream(
            "/cameldown/camelup/client/observer/assets/GameSpectate/Circle_closed2.png")));
        if (splitBoard) {
            circle.setImage(new Image(getClass().getResourceAsStream(
                "/cameldown/camelup/client/observer/assets/GameSpectate/Circle_open2.png")));
        }
        circle.setFitWidth(1400);
        circle.setFitHeight(900);
        fieldContainer.getChildren().add(circle);
        circle.setMouseTransparent(true);
    }

    /**
     * Opens a popup window.
     *
     * @param fxmlFile   the FXML file to load
     * @param popupTitle the popup title
     * @param event      the triggering event
     * @param width      popup width
     * @param height     popup height
     */
    private void openPopup(String fxmlFile, String popupTitle, ActionEvent event, double width, double height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            StackPane popupRoot = loader.load();
            var controller = loader.getController();
            if(controller instanceof Controller) {
                ((Controller) controller).loadApi(this.api);
            }
            Scene popupScene = new Scene(popupRoot);
            popupScene.setFill(Color.TRANSPARENT);
            popupScene.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("stylesPopup.css")).toExternalForm()
            );

            Stage popupStage = new Stage();
            popupStage.setTitle(popupTitle);
            popupStage.setScene(popupScene);
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.initStyle(StageStyle.TRANSPARENT);

            Stage mainStage = (Stage) fieldContainer.getScene().getWindow();
            popupStage.setFullScreen(false);
            popupStage.setResizable(false);
            popupStage.setWidth(width / 1920 * mainStage.getWidth());
            popupStage.setHeight(height / 1080 * mainStage.getHeight());

            double popupX = mainStage.getX() + (mainStage.getWidth() - popupStage.getWidth()) / 2;
            double popupY = mainStage.getY() + (mainStage.getHeight() - popupStage.getHeight()) / 2;
            popupStage.setX(popupX);
            popupStage.setY(popupY);

            popupStage.initOwner(mainStage);
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the settings popup.
     *
     * @param event action event
     */
    public void handleOpenSettings(ActionEvent event) {
        double widthPopup = 500.0;
        double heightPopup = 450.0;
        openPopup("Settings.fxml", "Settings", event, widthPopup, heightPopup);
    }

    /**
     * Opens the info popup.
     *
     * @param event action event
     */
    public void handleOpenInfo(ActionEvent event) {
        double widthPopup = 820.0;
        double heightPopup = 450.0;
        openPopup("Info.fxml", "Info", event, widthPopup, heightPopup);
    }

    /**
     * Opens the hotkey popup.
     *
     * @param event action event
     */
    public void handleOpenHotkey(ActionEvent event) {
        double widthPopup = 700.0;
        double heightPopup = 500.0;
        openPopup("Hotkeys.fxml", "Hotkeys", event, widthPopup, heightPopup);
    }

    /**
     * Opens the betting popup.
     *
     * @param event action event
     */
    public void handleOpenBetting(ActionEvent event) {
        double widthPopup = 960.0;
        double heightPopup = 540.0;
        openPopup("Betting.fxml", "Wetten", event, widthPopup, heightPopup);
    }

    /**
     * Opens the dice popup.
     *
     * @param event action event
     */
    public void handleOpenDice(ActionEvent event) {
        double widthPopup = 960.0;
        double heightPopup = 540.0;
        openPopup("Dice.fxml", "Würfel", event, widthPopup, heightPopup);
    }

    /**
     * Opens the end betting popup.
     *
     * @param event action event
     */
    public void handleOpenEndBetting(ActionEvent event) {
        double widthPopup = 960.0;
        double heightPopup = 540.0;
        openPopup("EndBetting.fxml", "Endwetten", event, widthPopup, heightPopup);
    }

    /**
     * Opens the field information popup.
     *
     * @param event action event
     */
    public void handleOpenFieldInformation(ActionEvent event) {
        double widthPopup = 960.0;
        double heightPopup = 540.0;
        openPopup("FieldInformation.fxml", "Feld Informationen", event, widthPopup, heightPopup);
    }

    /**
     * Creates a field ImageView.
     *
     * @param i           the field index
     * @param fieldBorder the field border image
     * @param goalLine    the goal line image
     * @return the ImageView for the field
     */
    private ImageView createFieldImageView(int i, Image fieldBorder, Image goalLine) {
        ImageView imageView = new ImageView(fieldBorder);
        if ((i - fieldCentered) == (fieldCount - 1 - fieldCentered)) {
            imageView = new ImageView(goalLine);
        }
        imageView.setFitWidth(25);
        imageView.setFitHeight(120);
        imageView.setMouseTransparent(true);
        return imageView;
    }

    /**
     * Calculates the angles used to position fields and their labels.
     *
     * @param i the field index
     * @return an array containing [angle, angleNumber]
     */
    private double[] calculateAngles(int i) {
        double angle;
        double angleNumber;

        int hiddenFields;
        if (displayedFields >= 17) {
            hiddenFields = 5;
        } else if (displayedFields >= 15) {
            hiddenFields = 4;
        } else if (displayedFields >= 9) {
            hiddenFields = 3;
        } else {
            hiddenFields = 2;
        }

        int totalFieldsForAngle = displayedFields + hiddenFields;
        int relativeIndex = (i - fieldCentered + fieldCount) % fieldCount;

        if (relativeIndex > fieldCount / 2) {
            relativeIndex -= fieldCount;
        }

        if (fieldCount <= displayedFields) {
            angle = 90 + (360.0 / fieldCount) * relativeIndex
                + (360.0 / fieldCount) / 2;
            angleNumber = angle - (360.0 / fieldCount) / 2;
        } else {
            angle = 90 + (360.0 / totalFieldsForAngle) * relativeIndex
                + (360.0 / totalFieldsForAngle) / 2;
            angleNumber = angle - (360.0 / totalFieldsForAngle) / 2;
        }

        return new double[]{angle, angleNumber};
    }

    /**
     * Positions a field ImageView on the circle.
     *
     * @param imageView the field ImageView
     * @param angle     the angle in degrees
     * @param centerX   the circle center X
     * @param centerY   the circle center Y
     * @param radius    the circle radius
     */
    private void positionFieldImageView(ImageView imageView, double angle,
                                        double centerX, double centerY, double radius) {
        double angleRad = Math.toRadians(angle);
        double x = centerX + radius * Math.cos(angleRad) - imageView.getFitWidth() / 2;
        double y = centerY + radius * Math.sin(angleRad) - imageView.getFitHeight() / 2;
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        imageView.setRotate(angle - 90);
    }

    /**
     * Draws a clickable circle for field numbering.
     *
     * @param fieldIndex  the field index
     * @param angleNumber the angle for positioning
     * @param centerX     center X
     * @param centerY     center Y
     * @param radius      circle radius
     */
    private void drawNumberCircle(int fieldIndex, double angleNumber,
                                  double centerX, double centerY, double radius) {
        radius = radius + 10;
        double angleNumberRad = Math.toRadians(angleNumber);
        double xNumber = centerX + radius * Math.cos(angleNumberRad) - 27.5;
        double yNumber = centerY + radius * Math.sin(angleNumberRad) - 27.5;
        Button numberButton = new Button(String.valueOf(fieldIndex + 1));
        numberButton.setPrefSize(55, 55);
        numberButton.setStyle("-fx-opacity: 0.4; -fx-background-radius: 25; -fx-font-size: 16px; -fx-text-fill: black;");
        if (fieldIndex >= 999) {
            numberButton.setStyle("-fx-opacity: 0.4; -fx-background-radius: 25; -fx-font-size: 13px; -fx-text-fill: black;");
        }
        numberButton.setLayoutX(xNumber);
        numberButton.setLayoutY(yNumber);
        numberButton.setOnMouseClicked(event -> {
            try {
                infoField = fieldIndex;
                handleOpenFieldInformation(new ActionEvent(event.getSource(), event.getTarget()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        fieldContainer.getChildren().add(numberButton);
    }

    /**
     * Places camel images on the given field.
     *
     * @param fieldIndex  the field index
     * @param angleNumber the angle for positioning
     * @param centerX     center X
     * @param centerY     center Y
     * @param radius      circle radius
     */
    private void placeCamelsOnField(int fieldIndex, double angleNumber,
                                    double centerX, double centerY, double radius) {

        radius = radius - 20;
        int camelHeight;
        int minFields = displayedFields;
        if (displayedFields > fieldCount) {
            minFields = fieldCount;
        }
        if (minFields >= 17) {
            camelHeight = 70;
        } else if (minFields >= 15) {
            camelHeight = 80;
        } else if (minFields >= 9) {
            camelHeight = 95;
        } else {
            camelHeight = 110;
        }

        List<BoardSpace> boardSpaces = gameState.getBoardSpaces();
        if (fieldIndex < boardSpaces.size()) {
            BoardSpace boardSpace = boardSpaces.get(fieldIndex);
            List<Integer> camelIds = new ArrayList<>(boardSpace.getCamelIds());
            if (!camelIds.isEmpty()) {
                double camelRadius = radius - 30;
                if (camelIds.size() > 4) {
                    Image camelImage = new Image(getClass().getResourceAsStream(
                        "/cameldown/camelup/client/observer/assets/GameSpectate/camelPack.png"));
                    Button camelButton = new Button();
                    ImageView camelImageView = new ImageView(camelImage);
                    camelImageView.setFitWidth(camelHeight * 1.25);
                    camelImageView.setFitHeight(camelHeight);
                    camelButton.setGraphic(camelImageView);
                    camelButton.setStyle("-fx-background-color: transparent;");
                    camelButton.setPrefSize(camelImageView.getFitWidth(), camelImageView.getFitHeight());

                    double angleRad = Math.toRadians(angleNumber);
                    double xCamel = centerX + camelRadius * Math.cos(angleRad) - camelButton.getPrefWidth() / 2;
                    double yCamel = centerY + camelRadius * Math.sin(angleRad) - camelButton.getPrefHeight() / 2;
                    camelButton.setLayoutX(xCamel);
                    camelButton.setLayoutY(yCamel);
                    camelButton.setRotate(angleNumber - 90);

                    camelButton.setOnAction(event -> {
                        infoField = fieldIndex;
                        handleOpenFieldInformation(event);
                    });

                    fieldContainer.getChildren().add(camelButton);

                    int x = 0;
                    int y = 0;
                    for (Integer camelId : camelIds) {
                        if (camelId >= 0) {
                            x++;
                        } else {
                            y++;
                        }
                    }

                    Circle numberCircle = new Circle(20, Color.WHITE);
                    numberCircle.setOpacity(0.8);

                    Text textX = new Text(String.valueOf(x));
                    textX.setFont(Font.font("Arial", 16));
                    textX.setFill(Color.GREEN);

                    Text textSlash = new Text("/");
                    textSlash.setFont(Font.font("Arial", 20));
                    textSlash.setFill(Color.BLACK);

                    Text textY = new Text(String.valueOf(y));
                    textY.setFont(Font.font("Arial", 16));
                    textY.setFill(Color.RED);

                    HBox camelCountHBox = new HBox(2);
                    camelCountHBox.setAlignment(Pos.CENTER);
                    camelCountHBox.getChildren().addAll(textX, textSlash, textY);

                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().addAll(numberCircle, camelCountHBox);
                    stackPane.setAlignment(Pos.CENTER);

                    Button circleButton = new Button();
                    circleButton.setGraphic(stackPane);
                    circleButton.setStyle("-fx-background-color: transparent;");
                    circleButton.setPrefSize(numberCircle.getRadius() * 2, numberCircle.getRadius() * 2);

                    double textOffsetX = 0;
                    double textOffsetY = -70;
                    double rotationRad = Math.toRadians(angleNumber - 90);
                    double rotatedOffsetX = textOffsetX * Math.cos(rotationRad)
                        - textOffsetY * Math.sin(rotationRad);
                    double rotatedOffsetY = textOffsetX * Math.sin(rotationRad)
                        + textOffsetY * Math.cos(rotationRad);

                    double circleButtonX = xCamel + camelButton.getPrefWidth() / 2
                        + rotatedOffsetX - circleButton.getPrefWidth() / 2;
                    double circleButtonY = yCamel + camelButton.getPrefHeight() / 2
                        + rotatedOffsetY - circleButton.getPrefHeight() / 2;
                    circleButton.setLayoutX(circleButtonX);
                    circleButton.setLayoutY(circleButtonY);

                    circleButton.setOnAction(event -> {
                        infoField = fieldIndex;
                        handleOpenFieldInformation(event);
                    });

                    fieldContainer.getChildren().add(circleButton);

                } else {
                    for (int j = 0; j < camelIds.size(); j++) {
                        Integer camelId = camelIds.get(j);
                        double adjustedRadius = camelRadius - (j * (camelHeight / 2));
                        double angleRad = Math.toRadians(angleNumber);
                        double xCamel = centerX + adjustedRadius * Math.cos(angleRad)
                            - (camelHeight * 1.25 / 2);
                        double yCamel = centerY + adjustedRadius * Math.sin(angleRad)
                            - (camelHeight * 0.9 / 2);
                        Image camelImage = new Image(getClass().getResourceAsStream(
                            "/cameldown/camelup/client/observer/assets/GameSpectate/camel.png"));
                        Camel camel = findCamelById(camelId);
                        if (camel != null) {
                            String camelColorHex = camel.getColor();
                            Color camelColor = Color.web(camelColorHex);
                            camelImage = replaceColor(camelImage, camelColor);

                            ImageView camelImageView = new ImageView(camelImage);
                            camelImageView.setFitWidth(camelHeight * 1.25);
                            camelImageView.setFitHeight(camelHeight * 0.9);

                            if (camel.getId() < 0) {
                                camelImageView.setScaleX(-1);
                            }

                            double brightness = camelColor.getBrightness();
                            Color textColor = (brightness > 0.9) ? Color.BLACK : Color.WHITE;

                            StackPane camelStack = new StackPane();
                            if (showID) {
                                Label idLabel = new Label("# " + camel.getId());
                                idLabel.setTextFill(textColor);
                                idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                                idLabel.setAlignment(Pos.CENTER);

                                camelStack.getChildren().addAll(camelImageView, idLabel);
                            } else {
                                camelStack.getChildren().addAll(camelImageView);
                            }
                            Button camelButton = new Button();
                            camelButton.setGraphic(camelStack);
                            camelButton.setStyle("-fx-background-color: transparent;");
                            camelButton.setPrefSize(
                                camelImageView.getFitWidth(),
                                camelImageView.getFitHeight()
                            );

                            camelButton.setLayoutX(xCamel);
                            camelButton.setLayoutY(yCamel);
                            camelButton.setRotate(angleNumber - 90);

                            camelButton.setOnAction(event -> {
                                infoField = fieldIndex;
                                handleOpenFieldInformation(event);
                            });

                            fieldContainer.getChildren().add(camelButton);
                        }
                    }
                }
            }
        }
    }

    /**
     * Draws a player card on a given field if present.
     *
     * @param fieldIndex  the field index
     * @param angleNumber the angle for positioning
     * @param centerX     center X
     * @param centerY     center Y
     * @param radius      circle radius
     */
    private void drawPlayerCard(int fieldIndex, double angleNumber,
                                double centerX, double centerY, double radius) {
        int cardHeight;
        int minFields = displayedFields;
        if (displayedFields > fieldCount) {
            minFields = fieldCount;
        }
        if (minFields >= 17) {
            cardHeight = 60;
        } else if (minFields >= 15) {
            cardHeight = 70;
        } else if (minFields >= 9) {
            cardHeight = 80;
        } else {
            cardHeight = 100;
        }

        if (fieldIndex < gameState.getBoardSpaces().size()) {
            BoardSpace boardSpace = gameState.getBoardSpaces().get(fieldIndex);
            if (boardSpace.getPlayerCard() == null) {
                return;
            }
            PlayerCard playerCard = boardSpace.getPlayerCard();
            String imageName = "";
            if(playerCard.getSpacesMoved() == null) {
                return;
            } else if (playerCard.getSpacesMoved().equals(1)) {
                imageName = "playerCardForward.png";
            } else if (playerCard.getSpacesMoved().equals(-1)) {
                imageName = "playerCardBackward.png";
            } else {
                return;
            }

            Image playerCardImage = new Image(getClass().getResourceAsStream(
                "/cameldown/camelup/client/observer/assets/GameSpectate/" + imageName));
            List<Player> players = new ArrayList<>(gameState.getPlayers());
            players.sort(Comparator.comparingInt(Player::getPlayerId));
            int playerCardPlayerID = gameState.getBoardSpaces().get(fieldIndex).getPlayerCard().getPlayerId();
            int playerColorIndex = -1;
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getPlayerId() == playerCardPlayerID) {
                    playerColorIndex = i;
                    break;
                }
            }
            Color playerColor = getPlayerColor(playerColorIndex);
            playerCardImage = replaceColor(playerCardImage, playerColor);

            Button playerCardButton = new Button();
            ImageView playerCardImageView = new ImageView(playerCardImage);
            playerCardImageView.setFitWidth(cardHeight);
            playerCardImageView.setFitHeight(cardHeight);
            playerCardButton.setGraphic(playerCardImageView);
            playerCardButton.setStyle("-fx-background-color: transparent;");
            playerCardButton.setPrefSize(
                playerCardImageView.getFitWidth(),
                playerCardImageView.getFitHeight()
            );

            double playerCardRadius = radius - 50;
            double angleRad = Math.toRadians(angleNumber);
            double x = centerX + playerCardRadius * Math.cos(angleRad) - playerCardButton.getPrefWidth() / 2;
            double y = centerY + playerCardRadius * Math.sin(angleRad) - playerCardButton.getPrefHeight() / 2;
            playerCardButton.setLayoutX(x);
            playerCardButton.setLayoutY(y);
            playerCardButton.setRotate(angleNumber - 90);

            playerCardButton.setOnAction(event -> {
                infoField = fieldIndex;
                handleOpenFieldInformation(event);
            });

            fieldContainer.getChildren().add(playerCardButton);
        }
    }

    /**
     * Finds a camel by its ID.
     *
     * @param camelId the camel ID
     * @return the Camel instance if found, or null otherwise
     */
    private Camel findCamelById(Integer camelId) {
        for (Camel c : gameState.getGameConfig().getCamels()) {
            if (c.getId().equals(camelId)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Replaces a specific color in the image with another color.
     *
     * @param image      the source image
     * @param camelColor the target color
     * @return the new image with replaced color
     */
    private Image replaceColor(Image image, Color camelColor) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage newImage = new WritableImage(width, height);
        PixelReader reader = image.getPixelReader();
        PixelWriter writer = newImage.getPixelWriter();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                if (color.getOpacity() == 1.0 && color.getGreen() == 1.0 && color.getRed() == 0.0) {
                    writer.setColor(x, y, camelColor);
                } else {
                    writer.setColor(x, y, color);
                }
            }
        }
        return newImage;
    }

    /**
     * Populates the rolled dice box with recently rolled dice.
     */
    private void populateRolledDiceBox() {
        rolledDiceBox.getChildren().clear();
        int maxDiceDisplay = 12;
        List<RolledDice> rolledDiceList = new ArrayList<>(gameState.getRolledDice());
        Collections.reverse(rolledDiceList);

        int totalCamels = gameState.getGameConfig().getCamels().size();
        int diceToDisplay = Math.min(totalCamels, maxDiceDisplay);

        List<Node> diceBoxes = new ArrayList<>();
        for (int i = 0; i < rolledDiceList.size() && i < diceToDisplay; i++) {
            RolledDice rolledDice = rolledDiceList.get(i);
            HBox diceBox = createColoredDiceBox(rolledDice);
            diceBoxes.add(diceBox);
        }

        int remainingDice = diceToDisplay - rolledDiceList.size();
        for (int i = 0; i < remainingDice; i++) {
            HBox diceBox = createGreyDiceBox();
            diceBoxes.add(diceBox);
        }

        Button btnDice = new Button();
        btnDice.setPrefSize(53, 53);
        btnDice.setStyle("-fx-background-radius: 15;-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0.5, 2, 0);");
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(
            "/cameldown/camelup/client/observer/assets/GameSpectate/GameCubeIcon.png")));
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        btnDice.setGraphic(imageView);
        btnDice.setOnAction(this::handleOpenDice);

        if (!diceBoxes.isEmpty()) {
            diceBoxes.remove(diceBoxes.size() - 1);
        }
        diceBoxes.add(btnDice);

        HBox firstRow = new HBox(5);
        HBox secondRow = new HBox(5);
        firstRow.setAlignment(Pos.CENTER);
        secondRow.setAlignment(Pos.CENTER);

        for (int i = 0; i < diceBoxes.size(); i++) {
            if (i < 6) {
                firstRow.getChildren().add(diceBoxes.get(i));
            } else {
                secondRow.getChildren().add(diceBoxes.get(i));
            }
        }

        VBox diceRows = new VBox(5);
        diceRows.setAlignment(Pos.CENTER);
        diceRows.getChildren().add(firstRow);
        if (!secondRow.getChildren().isEmpty()) {
            diceRows.getChildren().add(secondRow);
        }

        rolledDiceBox.getChildren().add(diceRows);
    }

    /**
     * Creates a colored dice box based on a rolled dice.
     *
     * @param rolledDice the rolled dice data
     * @return a styled HBox representing the dice
     */
    private HBox createColoredDiceBox(RolledDice rolledDice) {
        HBox diceBox = new HBox();
        diceBox.setAlignment(Pos.CENTER);
        diceBox.setPrefSize(53, 53);
        diceBox.setMaxSize(53, 53);

        Camel camel = findCamelById(rolledDice.getCamelId());
        String camelColorHex = camel != null ? camel.getColor() : "#FFFFFF";
        diceBox.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0.5, 2, 0); "
            + "-fx-background-color: " + camelColorHex + "; -fx-background-radius: 10;");

        Color camelColor = Color.web(camelColorHex);
        double brightness = camelColor.getBrightness();
        Color textColor = (brightness > 0.7) ? Color.BLACK : Color.WHITE;

        // **German text is replaced by dynamic data, so keep the numeric label only**:
        Text numberText = new Text(String.valueOf(rolledDice.getNumber()));
        numberText.setFont(Font.font("System", FontWeight.BOLD, 18));
        numberText.setFill(textColor);

        Text camelText = new Text(System.lineSeparator() + "# " + rolledDice.getCamelId());
        camelText.setFont(Font.font("System", 16));
        if (rolledDice.getCamelId() >= 9999) {
            camelText.setFont(Font.font("System", 14));
        }
        camelText.setFill(textColor);

        TextFlow textFlow = new TextFlow(numberText, camelText);
        textFlow.setTextAlignment(TextAlignment.CENTER);

        diceBox.getChildren().add(textFlow);

        diceBox.setOnMouseClicked(event -> {
            int camelId = rolledDice.getCamelId();
            centerOnCamelById(camelId);
        });
        diceBox.setOnMouseEntered(event -> diceBox.setCursor(Cursor.HAND));
        diceBox.setOnMouseExited(event -> diceBox.setCursor(Cursor.DEFAULT));

        return diceBox;
    }

    /**
     * Creates a grey dice box representing an unrolled dice.
     *
     * @return a styled HBox representing the grey dice
     */
    private HBox createGreyDiceBox() {
        HBox diceBox = new HBox();
        diceBox.setAlignment(Pos.CENTER);
        diceBox.setPrefSize(53, 53);
        diceBox.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0.5, 2, 0); "
            + "-fx-background-color: #E8E8E8; -fx-background-radius: 10;");
        Label numberLabel = new Label();
        numberLabel.setVisible(false);
        diceBox.getChildren().add(numberLabel);
        return diceBox;
    }

    /**
     * Updates the Turn count label (German text in UI).
     */
    private void updateTurnCount() {
        turnCount.setText((gameState.getTurns() + 1) + " von " + gameState.getGameConfig().getMaxTurns());
        if (gameState.getGameConfig().getMaxTurns() <= 99) {
            turnCount.setFont(Font.font("System", 26));
        } else if (gameState.getGameConfig().getMaxTurns() < 999) {
            turnCount.setFont(Font.font("System", 24));
        } else if (gameState.getGameConfig().getMaxTurns() < 9999) {
            turnCount.setFont(Font.font("System", 21));
        } else {
            turnCount.setFont(Font.font("System", 18));
        }
    }

    /**
     * Gets the current game state.
     *
     * @return the current game state
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Loads the API instance and initializes the controller.
     *
     * @param api the API instance
     */
    @Override
    public void loadApi(final Api api) {
        this.api = api;
        initializeController();
    }


    public void handleOpenPlayerCard(ActionEvent event) {
        double widthPopup = 600;
        double heightPopup = 400;
        openPopup("/cameldown/camelup/client/observer/PlayerCard.fxml", "PlayerCard", event, widthPopup, heightPopup);
    }


    @FXML
    public void handleDicePyramide(ActionEvent event) {
        this.api.rollDice();
    }
}