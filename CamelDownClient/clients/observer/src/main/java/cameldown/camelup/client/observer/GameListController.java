package cameldown.camelup.client.observer;

import cameldown.camelup.client.core.api.Api;
import cameldown.camelup.interfacedefinition.game.v3.LobbyList;
import cameldown.camelup.interfacedefinition.game.v3.*;
import com.google.gson.Gson;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Controls and displays a list of available lobbies, allowing the user to join or spectate games.
 * This view also allows filtering by game phase (searching, running, finished) and searching by lobby name.
 */
public class GameListController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(GameListController.class);

    /**
     * Holds a reference to the currently selected game state for spectating.
     */
    public static GameState gameStateGameList;

    /**
     * Holds the current lobby list retrieved from the server.
     */
    public LobbyList lobbyList;

    /**
     * Holds a list of recent games retrieved from the server, used when displaying finished games.
     */
    public RecentGames recentGames;

    private Api api;
    private GameState.GamePhase currentGamePhase;
    private Timeline refreshTimeline;

    @FXML
    private GridPane rootGridPane;
    @FXML
    private StackPane rootStackPane;
    @FXML
    private VBox lobbyListVBox;
    @FXML
    private Button searchingGamesTab;
    @FXML
    private Button runningGameTab;
    @FXML
    private Button finishedGameTab;
    @FXML
    private TextField searchField;

    private Image clockIconImage;
    private Image sandClockIconImage;
    private Image playerCountIconImage;
    private Image camelIconImage;
    private Image fieldCountIconImage;

    /**
     * Initializes this controller after the API is loaded. Retrieves the lobby and recent games lists,
     * sets the default tab to 'searching games', and sets up scaling adjustments.
     */
    public void initializeController() {

        loadImages();

        Platform.runLater(() -> {
            // Default tab: searching (CREATED)
            searchingGamesTab.setStyle("-fx-background-color: rgb(221,221,221);");
            runningGameTab.setStyle("-fx-background-color: rgb(255,255,255);");
            finishedGameTab.setStyle("-fx-background-color: rgb(255,255,255);");
            currentGamePhase = GameState.GamePhase.CREATED;
            loadLobbies();

            refreshTimeline = new Timeline(
                new KeyFrame(Duration.seconds(2.5), event -> {
                    try {
                        loadLobbies();} catch (RuntimeException e) {
                        logger.warn("Exception during lobby request", e);
                    }
                })
            );
            refreshTimeline.setCycleCount(Timeline.INDEFINITE);
            refreshTimeline.play();


            Scene scene = rootStackPane.getScene();
            if (scene != null) {
                scene.widthProperty().addListener((obs, oldVal, newVal) -> adjustScaling());
                scene.heightProperty().addListener((obs, oldVal, newVal) -> adjustScaling());
                adjustScaling();
            }
        });
    }

    private void loadImages() {
        clockIconImage = new Image(getClass().getResourceAsStream(
            "/cameldown/camelup/client/observer/assets/GameSpectate/ClockIcon.png"));
        sandClockIconImage = new Image(getClass().getResourceAsStream(
            "/cameldown/camelup/client/observer/assets/GameSpectate/SandClockIcon.png"));
        playerCountIconImage = new Image(getClass().getResourceAsStream(
            "/cameldown/camelup/client/observer/assets/GameSpectate/PlayerCountIcon.png"));
        camelIconImage = new Image(getClass().getResourceAsStream(
            "/cameldown/camelup/client/observer/assets/GameSpectate/CamelLobbyIcon.png"));
        fieldCountIconImage = new Image(getClass().getResourceAsStream(
            "/cameldown/camelup/client/observer/assets/GameSpectate/FieldCountIcon.png"));
    }

    /**
     * Adjusts the scaling of the UI elements based on the current scene size, ensuring that the layout
     * scales proportionally to maintain visual consistency at different window sizes.
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
     * Loads and displays the lobbies based on the current game phase. If the phase is 'finished',
     * recent games are displayed instead of ongoing lobbies. The UI is cleared before new lobbies are added.
     */
    private void loadLobbies() {
        try {
            lobbyList = api.listLobbies().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        try {
            recentGames = api.listRecentGames(50).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        lobbyListVBox.getChildren().clear();

        int itemsPerRow = 3;
        HBox row = null;
        int itemCount = 0;

        if (currentGamePhase != GameState.GamePhase.FINISHED) {
            for (Lobby lobby : lobbyList.getLobbies()) {
                if (lobby.getGameState().getGamePhase() != currentGamePhase) {
                    if (currentGamePhase == GameState.GamePhase.PLAYING) {
                        if (lobby.getGameState().getGamePhase() != GameState.GamePhase.VISUALIZING &&
                            lobby.getGameState().getGamePhase() != GameState.GamePhase.PAUSED) {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }

                if (itemCount % itemsPerRow == 0) {
                    row = new HBox(20);
                    row.setAlignment(Pos.CENTER);
                    lobbyListVBox.getChildren().add(row);
                }

                VBox lobbyItem = createLobbyItem(lobby);
                row.getChildren().add(lobbyItem);
                itemCount++;
            }
        } else {
            // FINISHED: display recent games
            for (RecentGame lobby : recentGames.getLobbies()) {
                if (itemCount % itemsPerRow == 0) {
                    row = new HBox(20);
                    row.setAlignment(Pos.CENTER);
                    lobbyListVBox.getChildren().add(row);
                }

                VBox lobbyItem = createLobbyItem(lobby.getLobby());
                row.getChildren().add(lobbyItem);
                itemCount++;
            }
        }
    }

    /**
     * Creates a UI element representing a single, currently active or searching lobby.
     * Adds visuals and a join button allowing the user to join the selected lobby.
     *
     * @param lobby The lobby data from which to build the UI element
     * @return a VBox representing the lobby
     */
    private VBox createLobbyItem(Lobby lobby) {
        String lobbyName = lobby.getName();
        GameConfig gameConfig = lobby.getGameState().getGameConfig();

        String backgroundColor;
        GameState.GamePhase gamePhase = lobby.getGameState().getGamePhase();
        if (gamePhase == GameState.GamePhase.PLAYING || gamePhase == GameState.GamePhase.VISUALIZING) {
            backgroundColor = "#BED7B6";
        } else if (gamePhase == GameState.GamePhase.PAUSED) {
            backgroundColor = "#DECD1D";
        } else if (gamePhase == GameState.GamePhase.CREATED) {
            backgroundColor = "#D7C59A";
        } else if (gamePhase == GameState.GamePhase.FINISHED) {
            backgroundColor = "#A19383";
        } else {
            backgroundColor = "#FFFFFF";
        }
        int currentPlayers = lobby.getGameState().getPlayers().size();
        int maxPlayers = gameConfig.getPlayerCount();
        int camelCount = gameConfig.getCamels().size();
        int fieldCount = gameConfig.getNumberOfSpaces();
        String moveTime = (gameConfig.getThinkingTime() / 1000) + " s";
        String roundTime = (gameConfig.getMaxGameDuration() / 60000) + " min";

        VBox lobbyItem = new VBox();
        lobbyItem.setSpacing(5.0);
        lobbyItem.setPadding(new Insets(0));
        lobbyItem.setAlignment(Pos.CENTER);
        lobbyItem.setMaxWidth(240.0);
        lobbyItem.setPrefWidth(240.0);

        VBox gameBoxTemplate = new VBox();
        gameBoxTemplate.setStyle("-fx-background-color: " + backgroundColor + "; -fx-border-radius: 10; -fx-background-radius: 10; "
            + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0.5, 3, 0);");
        gameBoxTemplate.setMaxWidth(Double.MAX_VALUE);
        gameBoxTemplate.setAlignment(Pos.TOP_CENTER);

        HBox gameBoxTemplate3 = new HBox();
        gameBoxTemplate3.setStyle("-fx-background-color: white; -fx-border-radius: 10 10 0 0; -fx-background-radius: 10 10 0 0;");
        gameBoxTemplate3.setPrefHeight(40.0);
        gameBoxTemplate3.setMaxWidth(Double.MAX_VALUE);
        gameBoxTemplate3.setAlignment(Pos.CENTER);
        gameBoxTemplate3.setSpacing(10);
        gameBoxTemplate3.setPadding(new Insets(5, 10, 5, 10));

        ImageView clockIcon = new ImageView(clockIconImage);
        clockIcon.setFitWidth(25.0);
        clockIcon.setFitHeight(22.0);

        Label moveTimeLabel = new Label(moveTime);
        moveTimeLabel.setTextFill(Color.web("#aeaeae"));
        moveTimeLabel.setFont(Font.font(16));

        ImageView sandClockIcon = new ImageView(sandClockIconImage);
        sandClockIcon.setFitWidth(25.0);
        sandClockIcon.setFitHeight(22.0);

        Label roundTimeLabel = new Label(roundTime);
        roundTimeLabel.setTextFill(Color.web("#aeaeae"));
        roundTimeLabel.setFont(Font.font(16));

        gameBoxTemplate3.getChildren().addAll(clockIcon, moveTimeLabel, sandClockIcon, roundTimeLabel);

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(10));
        contentBox.setMaxWidth(Double.MAX_VALUE);

        HBox iconLabelBox = new HBox(20);
        iconLabelBox.setAlignment(Pos.CENTER);
        iconLabelBox.setMaxWidth(Double.MAX_VALUE);

        VBox playerBox = new VBox(5);
        playerBox.setAlignment(Pos.CENTER);
        ImageView playerCountIcon = new ImageView(playerCountIconImage);
        playerCountIcon.setFitWidth(25.0);
        playerCountIcon.setFitHeight(22.0);
        Label playerCountLabel = new Label(currentPlayers + "/" + maxPlayers);
        playerCountLabel.setTextFill(Color.WHITE);
        playerCountLabel.setFont(Font.font(16));
        playerCountLabel.setAlignment(Pos.CENTER);
        playerBox.getChildren().addAll(playerCountIcon, playerCountLabel);

        VBox camelBox = new VBox(5);
        camelBox.setAlignment(Pos.CENTER);
        ImageView camelIcon = new ImageView(camelIconImage);
        camelIcon.setFitWidth(25.0);
        camelIcon.setFitHeight(22.0);
        Label camelCountLabel = new Label(String.valueOf(camelCount));
        camelCountLabel.setTextFill(Color.WHITE);
        camelCountLabel.setFont(Font.font(16));
        camelCountLabel.setAlignment(Pos.CENTER);
        camelBox.getChildren().addAll(camelIcon, camelCountLabel);

        VBox fieldBox = new VBox(5);
        fieldBox.setAlignment(Pos.CENTER);
        ImageView fieldCountIcon = new ImageView(fieldCountIconImage);
        fieldCountIcon.setFitWidth(27.0);
        fieldCountIcon.setFitHeight(22.0);
        Label fieldCountLabel = new Label(String.valueOf(fieldCount));
        fieldCountLabel.setTextFill(Color.WHITE);
        fieldCountLabel.setFont(Font.font(16));
        fieldCountLabel.setAlignment(Pos.CENTER);
        fieldBox.getChildren().addAll(fieldCountIcon, fieldCountLabel);

        iconLabelBox.getChildren().addAll(playerBox, camelBox, fieldBox);

        Button joinButton = new Button("Beitreten");
        joinButton.setMinSize(75.0, 20.0);
        joinButton.setPrefSize(75.0, 20.0);
        joinButton.setStyle("-fx-background-radius: 15; "
            + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 15, 0.5, 3, 0);");
        joinButton.setTextFill(Color.web("#747474"));
        joinButton.setFont(Font.font("System Bold", 11));
        joinButton.setOnAction(event -> {
            try {
                playGame(event, lobby.getGameState(), lobby.getLobbyId());
            } catch (Exception e) {
                logger.error("Error while joining lobby: {}", e.getMessage());
            }
        });
        
        contentBox.getChildren().addAll(iconLabelBox, joinButton);
        gameBoxTemplate.getChildren().addAll(gameBoxTemplate3, contentBox);

        Label lobbyNameLabel = new Label(lobbyName);
        lobbyNameLabel.setTextFill(Color.web("#747474"));
        lobbyNameLabel.setFont(Font.font(16));
        lobbyNameLabel.setAlignment(Pos.CENTER);
        lobbyNameLabel.setMaxWidth(Double.MAX_VALUE);

        lobbyItem.getChildren().addAll(gameBoxTemplate, lobbyNameLabel);

        return lobbyItem;
    }
    private void showLoadingPopUp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Joining Lobby");
        alert.setHeaderText("Please wait...");
        alert.setContentText("Attempting to join the lobby. Please wait.");
        alert.setGraphic(new ProgressIndicator()); // You can add a progress bar or indicator here
        alert.showAndWait();
    }

    private void showErrorPopUp(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Failed to Join Lobby");
        alert.setContentText(message);
        alert.showAndWait();
    }
    /**
     * Attempts to join and spectate a game identified by lobbyId. If successful, transitions to the GameSpectateView.
     *
     * @param event     The action event
     * @param gameState The game state to spectate
     * @param lobbyId   The ID of the lobby
     * @throws IOException if changing the root view fails
     */
    private void playGame(ActionEvent event, GameState gameState, int lobbyId) throws IOException, InterruptedException, ExecutionException {
        gameStateGameList = gameState;
        SuccessFeedback spectateGameSuccessFeedback = api.joinLobby(lobbyId, true).get();
        if (spectateGameSuccessFeedback.getSuccess()) {
            App.setRoot("GameSpectateView");
            if (refreshTimeline != null) {
                refreshTimeline.stop();
                refreshTimeline = null;
            }
            if (gameState.getGamePhase() == GameState.GamePhase.FINISHED) {
                for (RecentGame rg : recentGames.getLobbies()) {
                    if (rg.getLobby().getLobbyId() == lobbyId) {
                        List<Integer> leaderboard = new ArrayList<>(rg.getLeaderboard());
                        GameSpectateController.getInstance().updateScoreboard(leaderboard);
                        break;
                    }
                }
            }
        } else {
            logger.error("Could not join game because {}", spectateGameSuccessFeedback.getError());
        }
    }

    /**
     * Filters lobbies to show those in the CREATED (searching) phase. Updates tab styles accordingly.
     *
     * @param event triggered event
     */
    @FXML
    private void searchingGamesTab(ActionEvent event) {
        currentGamePhase = GameState.GamePhase.CREATED;
        searchingGamesTab.setStyle("-fx-background-color: rgb(221,221,221);");
        runningGameTab.setStyle("-fx-background-color: rgb(255,255,255);");
        finishedGameTab.setStyle("-fx-background-color: rgb(255,255,255);");
        loadLobbies();
    }

    /**
     * Filters lobbies to show those in the PLAYING phase (including VISUALIZING and PAUSED). Updates tab styles.
     *
     * @param event triggered event
     */
    @FXML
    private void runningGameTab(ActionEvent event) {
        currentGamePhase = GameState.GamePhase.PLAYING;
        runningGameTab.setStyle("-fx-background-color: rgb(221,221,221);");
        searchingGamesTab.setStyle("-fx-background-color: rgb(255,255,255);");
        finishedGameTab.setStyle("-fx-background-color: rgb(255,255,255);");
        loadLobbies();
    }

    /**
     * Filters lobbies to show finished games by displaying recent games instead of active lobbies. Updates tab styles.
     *
     * @param event triggered event
     */
    @FXML
    private void finishedGameTab(ActionEvent event) {
        currentGamePhase = GameState.GamePhase.FINISHED;
        finishedGameTab.setStyle("-fx-background-color: rgb(221,221,221);");
        runningGameTab.setStyle("-fx-background-color: rgb(255,255,255);");
        searchingGamesTab.setStyle("-fx-background-color: rgb(255,255,255);");
        loadLobbies();
    }

    /**
     * Triggered when the user presses enter in the searchField. Filters lobbies by exact name match
     * and displays only those matching. Clears tab highlighting since this is a separate search action.
     *
     * @param event triggered event
     */
    public void searchField(ActionEvent event) {
        try {
            lobbyList = api.listLobbies().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        lobbyListVBox.getChildren().clear();

        int itemsPerRow = 3;
        HBox row = null;
        int itemCount = 0;

        // Simple exact name search
        for (Lobby lobby : lobbyList.getLobbies()) {
            System.out.println(lobby.getName());
            if (!lobby.getName().contains(searchField.getText())) {
                continue;
            }

            if (itemCount % itemsPerRow == 0) {
                row = new HBox(20);
                row.setAlignment(Pos.CENTER);
                lobbyListVBox.getChildren().add(row);
            }

            VBox lobbyItem = createLobbyItem(lobby);
            row.getChildren().add(lobbyItem);
            itemCount++;
            searchField.setText(String.valueOf(api.getClientId()));
        }

        runningGameTab.setStyle("-fx-background-color: rgb(255,255,255);");
        searchingGamesTab.setStyle("-fx-background-color: rgb(255,255,255);");
        finishedGameTab.setStyle("-fx-background-color: rgb(255,255,255);");
    }

    /**
     * Called once the API instance is available. Initializes the controller and loads the initial lobby list.
     *
     * @param api The API instance for server communication
     */
    @Override
    public void loadApi(final Api api) {
        this.api = api;
        initializeController();
    }
}
