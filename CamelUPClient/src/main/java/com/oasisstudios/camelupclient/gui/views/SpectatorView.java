package com.oasisstudios.camelupclient.gui.views;

import com.oasisstudios.camelupclient.api.Api;
import com.oasisstudios.camelupclient.dto.*;
import com.oasisstudios.camelupclient.dto.GameState.GamePhase;
import com.oasisstudios.camelupclient.gui.components.PlayerBoxGenerator;
import com.oasisstudios.camelupclient.model.CamelModel;
import com.oasisstudios.camelupclient.service.ContextProvider;
import com.oasisstudios.camelupclient.service.UserType;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javafx.util.Duration;
import com.oasisstudios.camelupclient.gui.components.GameField;

import javax.inject.Inject;

import static com.oasisstudios.camelupclient.service.UserType.SPECTATOR;

public class SpectatorView extends AbstractSceneBasedView {

    private BorderPane mainLayout = new BorderPane();
    private ScheduledExecutorService executor;
    private Api api;
    private VBox leftElements = new VBox();
    private HBox playerBox = new HBox();
    private Circle playerCircle = new Circle(40);
    private Label playerName = new Label("");
    private StackPane playerStack = new StackPane();
    private GridPane scoreBoard = new GridPane();
    private VBox scoreBox = new VBox();
    private Label scoreBoardText = new Label("SCOREBOARD");
    private HBox scoreTextBox = new HBox();
    private HBox timerBox = new HBox();
    private StackPane timerStack = new StackPane();
    private Label playTimeText = new Label("REMAINING TIME");
    private Label roundText = new Label("ROUND");
    private Label time = new Label("00:00:00");
    private Label round = new Label("00");
    private VBox playTimeBox = new VBox();
    private VBox roundBox = new VBox();
    private VBox rightUpperBox = new VBox();
    private int secondsElapsed = 0;
    private HBox titlePastMovesBox = new HBox();
    private StackPane pastMoveStack = new StackPane();
    private VBox pastMovesBox = new VBox();
    private VBox pastElementsBox = new VBox();
    private ScrollPane pastMovesScroll = new ScrollPane();
    private Label titleOfPastMovesBox = new Label("MOVED CAMELS");
    private StackPane gameFieldStack = new StackPane();
    private GameField gameField = new GameField(2);
    public Label pastMoveEntry;
    List<String> allowedPhases = Arrays.asList("created", "playing", "visualizing", "paused");
    public Label winner = new Label("WINNER");
    public Label looser = new Label("LOOSER");
    public GridPane winLosePane = new GridPane();
    public VBox winLoBox = new VBox();
    public ScrollPane winLoBoxScroll = new ScrollPane();
    public StackPane winLoStack = new StackPane();
    public StackPane headerWLBox = new StackPane();
    public Label bedWorL = new Label("PLAYER BETS");

    Button mooove = new Button("TEST");
    Button moverius = new Button("Update");

    public SpectatorView(Stage stage, Lobby inputLobby) {
        api = ContextProvider.getBean(Api.class);
        this.primaryStage = stage;

        rightUpperBox.setSpacing(75);
        Font customFont = Font.loadFont(
                getClass().getResourceAsStream("/static/images/Fonts/LilitaOne-Regular.ttf"), 36
        );

        setupPlayerBox();
        setupScoreboard();
        setupPastMoves();
        setupTimerAndRound();
        setupLooserWinnerBox();
        api.removeOnGameStateEventHandler();
        Consumer<GameState> consumer = new GameStateConsumer();
        api.addOnGameStateEventHandler(consumer);
        // Apply background image to the game field stack (removing the beige box)
        gameFieldStack.setStyle(
                "-fx-background-image: url('/static/images/logos/background_image.png');" +
                        "-fx-background-size: cover;" +
                        "-fx-background-position: center;" +
                        "-fx-background-repeat: no-repeat;"
        );

        // Add the game field on top of the transparent background
        gameField.setStyle("-fx-background-color: transparent;");
        gameFieldStack.getChildren().addAll(gameField);
        gameFieldStack.setPadding(new Insets(20, 20, 20, 20));
        mainLayout.setCenter(gameFieldStack);

        leftElements.setSpacing(20);
        mainLayout.setLeft(leftElements);
        mainLayout.setRight(rightUpperBox);

        this.stageTitle = "Camel-UP Spectator";
        this.userType = UserType.SPECTATOR;
        this.setLayout(mainLayout);
        setStage(stage);
    }

    private void setupPlayerBox() {
        playerName.setStyle("-fx-text-fill: #664343");
        playerName.setFont(Font.font("Lilita One", FontWeight.BOLD, 20));
        playerCircle.setFill(Color.web("#F2A649"));
        playerBox.setAlignment(Pos.CENTER_LEFT);
        playerBox.setPadding(new Insets(20, 20, 20, 20));
        playerBox.setSpacing(20);
        playerBox.setStyle("-fx-background-color: #F5EFE5;-fx-background-radius: 16");
        playerBox.setPrefSize(360, 130);
        playerBox.setMaxSize(360, 130);
        playerBox.getChildren().addAll(playerCircle, playerName);
        playerStack.getChildren().addAll(playerBox);
        playerStack.setAlignment(Pos.CENTER);
        leftElements.getChildren().add(playerStack);
    }

    private void setupScoreboard() {
        scoreBoardText.setFont(Font.font("Lilita One", FontWeight.BOLD, 20));
        scoreBoardText.setStyle("-fx-text-fill: #FAB848");
        scoreTextBox.getChildren().add(scoreBoardText);
        scoreTextBox.setAlignment(Pos.CENTER_LEFT);
        scoreTextBox.setStyle("-fx-background-radius: 16 16 0 0;-fx-background-color: #664343");
        scoreTextBox.setPadding(new Insets(20, 20, 20, 20));

        scoreBox.setStyle("-fx-background-radius: 0 0 16 16;-fx-background-color: #F5EEE4");
        scoreBox.setPadding(new Insets(0, 0, 20, 0));
        scoreBox.setMaxSize(360, 275);
        scoreBox.getChildren().addAll(scoreTextBox, scoreBoard);
        leftElements.getChildren().add(scoreBox);
    }

    private void setupPastMoves() {
        titleOfPastMovesBox.setFont(Font.font("Lilita One", FontWeight.BOLD, 20));
        titleOfPastMovesBox.setStyle("-fx-text-fill: #FAB848;");
        titlePastMovesBox.setAlignment(Pos.CENTER_LEFT);
        titlePastMovesBox.setPadding(new Insets(10));
        titlePastMovesBox.setStyle("-fx-background-color: #664343; -fx-background-radius: 16 16 0 0;");
        titlePastMovesBox.getChildren().add(titleOfPastMovesBox);

        pastElementsBox.setSpacing(10);
        pastElementsBox.setPadding(new Insets(10));
        pastElementsBox.setStyle("-fx-background-color: #F5EFE5;");

        pastMovesScroll.setContent(pastElementsBox);
        pastMovesScroll.setFitToWidth(true);
        pastMovesScroll.setStyle("-fx-background-color: transparent; -fx-background-radius: 0 0 16 16;");
        pastMovesScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pastMovesScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        pastMovesBox.getChildren().addAll(titlePastMovesBox, pastMovesScroll);
        pastMovesBox.setSpacing(0);
        pastMovesBox.setStyle("-fx-background-color: #F5EFE5; -fx-background-radius: 16;");
        pastMovesBox.setPrefHeight(300);
        pastMovesBox.setPrefWidth(360);
        pastMovesBox.setMaxWidth(360);
        pastMovesBox.setAlignment(Pos.CENTER);

        pastMoveStack.getChildren().add(pastMovesBox);
        pastMoveStack.setPadding(new Insets(20, 0, 0, 0));
        leftElements.getChildren().add(pastMoveStack);
    }
    private void onGameStatesChanged(GameState gameState, List<Camel> camelModels) {
        if (gameState == null || camelModels == null || gameState.getGameConfig().getCamels() == null ) {
            return;
        }
        // Clear the existing camel models (if needed)
        camelModels.clear();
        for (Camel camel : gameState.getGameConfig().getCamels()) {
            System.out.println("Board Item moved!");
            System.out.println(camel.toString());
            camelModels.add(camel);
        }
        addCamelsToGameField(gameState);
    }
    private void setupTimerAndRound() {
        playTimeText.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
        playTimeText.setStyle("-fx-text-fill: #A08C8A");
        time.setStyle("-fx-text-fill: #FAB848");
        time.setFont(Font.font("Lilita One", FontWeight.BOLD, 20));
        playTimeBox.getChildren().addAll(playTimeText, time);
        playTimeBox.setSpacing(10);

        roundText.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
        roundText.setStyle("-fx-text-fill: #A08C8A");
        round.setFont(Font.font("Lilita One", FontWeight.BOLD, 20));
        round.setStyle("-fx-text-fill: #FAB848");
        roundBox.getChildren().addAll(roundText, round);
        roundBox.setSpacing(10);
        roundBox.setAlignment(Pos.CENTER);

        timerBox.setSpacing(20);
        timerBox.setPadding(new Insets(20, 20, 20, 20));
        timerBox.setStyle("-fx-background-color: #664343;-fx-background-radius: 16");
        timerBox.setPrefSize(280, 80);
        timerBox.setMaxSize(280, 80);
        timerBox.getChildren().addAll(playTimeBox, roundBox);
        timerBox.setAlignment(Pos.CENTER);
        timerStack.getChildren().add(timerBox);
        rightUpperBox.getChildren().add(timerStack);
        rightUpperBox.setPadding(new Insets(20, 20, 20, 20));
    }

    public void setupLooserWinnerBox() {
        looser.setFont(Font.font("Lilita One", FontWeight.BOLD, 20));
        looser.setStyle("-fx-text-fill: #664343");
        winner.setFont(Font.font("Lilita One", FontWeight.BOLD, 20));
        winner.setStyle("-fx-text-fill: #664343");

        winLosePane.setVgap(10);
        winLosePane.setHgap(50);
        winLosePane.setStyle("-fx-background-color: #F5EFE5; -fx-padding: 10; -fx-background-radius: 16; -fx-border-radius: 16;");
        winLosePane.add(winner, 0, 0);
        winLosePane.add(looser, 1, 0);

        winLoBoxScroll.setContent(winLosePane);
        winLoBoxScroll.setFitToWidth(true);
        winLoBoxScroll.setStyle("-fx-background-color: transparent; -fx-background-radius: 16; -fx-border-radius: 16;");
        winLoBoxScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        winLoBoxScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        headerWLBox.getChildren().add(bedWorL);
        headerWLBox.setAlignment(Pos.CENTER_LEFT);
        headerWLBox.setStyle("-fx-background-color: #664343; -fx-background-radius: 16 16 0 0; -fx-border-radius: 16 16 0 0;");
        headerWLBox.setMaxSize(360, 50);
        bedWorL.setFont(Font.font("Lilita One", FontWeight.BOLD, 20));
        bedWorL.setStyle("-fx-text-fill: #FBB849");
        headerWLBox.setPadding(new Insets(20, 20, 20, 20));

        winLoBox.getChildren().addAll(headerWLBox, winLoBoxScroll);
        winLoBox.setMaxSize(360, 600);
        winLoBox.setPrefSize(360, 600);
        winLoBox.setStyle("-fx-background-color: #F5EFE5; -fx-background-radius: 16; -fx-border-radius: 16;");

        winLoStack.getChildren().add(winLoBox);
        rightUpperBox.getChildren().add(winLoStack);
    }
     // Formats the time for the timer display
     private String formatTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * //--------------------------UPDATE THE SPECTATOR UI----------------------------------------------------//
     * Here are the Methods to Update the UI
     **/

    //--------------------------SCOREBOARD----------------------------------------------------//
    // Adds a new Entry to the Score Board
    public void addEntryToScoreBoard(String playerName, String playerID) {
        int newRow = scoreBoard.getRowCount();
        Label nameLabel = new Label(playerName);
        nameLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 14;");
        Label scoreLabel = new Label("0");
        scoreLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 14;");
        scoreBoard.add(nameLabel, 0, newRow);
        scoreBoard.add(scoreLabel, 1, newRow);
    }


    private void updateScoreboard(List<Player> players) {

        if (players == null || players.isEmpty()) {
            return;
        }
        scoreBoard.getChildren().clear(); // Alte Einträge entfernen

        // Setze Padding für die gesamte Box
        scoreBoard.setPadding(new Insets(20));

        // Überschriften hinzufügen
        Label nameHeader = new Label("Name (ID)");
        Label scoreHeader = new Label("Score");
        nameHeader.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #664343;"); // Braun wie gewünscht
        scoreHeader.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #664343;"); // Braun wie gewünscht

        // Überschriften links und rechts ausrichten mit Containern
        HBox nameHeaderBox = new HBox(nameHeader);
        nameHeaderBox.setAlignment(Pos.CENTER_LEFT);

        HBox scoreHeaderBox = new HBox(scoreHeader);
        scoreHeaderBox.setAlignment(Pos.CENTER_RIGHT);

        scoreBoard.add(nameHeaderBox, 0, 0);
        scoreBoard.add(scoreHeaderBox, 1, 0);

        // Spieler hinzufügen
        int row = 1;
        for (Player player : players) {
            // Name und ID in einer Spalte anzeigen (links ausgerichtet)
            Label nameLabel = new Label(player.getName() + " (" + player.getPlayerId() + ")");
            nameLabel.setStyle("-fx-text-fill: #664343; -fx-font-size: 16px; -fx-font-weight: bold;"); // Braun und fett
            HBox nameBox = new HBox(nameLabel);
            nameBox.setAlignment(Pos.CENTER_LEFT);

            // Score in einer Spalte anzeigen (rechts ausgerichtet)
            Label scoreLabel = new Label(String.valueOf(player.getMoney()));
            scoreLabel.setStyle("-fx-text-fill: #664343; -fx-font-size: 16px; -fx-font-weight: bold;"); // Braun und fett
            HBox scoreBox = new HBox(scoreLabel);
            scoreBox.setAlignment(Pos.CENTER_RIGHT);

            // Füge Abstand zwischen Name und Score hinzu, um gesamte Breite zu nutzen
            GridPane.setHgrow(nameBox, Priority.ALWAYS);

            scoreBoard.add(nameBox, 0, row);
            scoreBoard.add(scoreBox, 1, row);
            row++;
        }
    }


    //--------------------------REMAINING-CAMELS----------------------------------------------------//
    // Adds the Camels to the Moved Camels Window
    public void addPastMoveEntry(String camelId, int diceValue) {

        pastMoveEntry = new Label(camelId + " rolled a " + diceValue);
        pastMoveEntry.setStyle("-fx-font-size: 16px; -fx-text-fill: #664343; -fx-font-weight: bold; -fx-padding: 5;"); // Schriftgröße und Stil angepasst
        pastMoveEntry.setWrapText(true);

        pastElementsBox.getChildren().add(pastMoveEntry);
        pastMovesScroll.layout();
        pastMovesScroll.setVvalue(1.0);
    }

    // Deletes the Remaining Camel List
    public void deleteRemCamelsList() {
        pastElementsBox.getChildren().clear();
        pastMovesScroll.setVvalue(0.0);
    }

    public void adjustMovedCamelsBox() {
        // Setze Padding und Stil der braunen Box (Moved Camels Header)
        titlePastMovesBox.setPadding(new Insets(20)); // Einheitliches Padding wie bei der Scoreboard-Box
        titleOfPastMovesBox.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #FBB849;"); // Text wie vorheriger Header

        // Setze Stil für den Inhalt
        pastElementsBox.setPadding(new Insets(20)); // Innenabstand für den Boxinhalt
        pastElementsBox.setStyle("-fx-background-color: #F5EFE5; -fx-background-radius: 16;"); // Beige und abgerundete Ecken

        pastMovesBox.setPadding(new Insets(20)); // Padding der gesamten Box identisch mit Scoreboard-Box
        pastMovesBox.setStyle("-fx-background-color: #F5EFE5; -fx-background-radius: 16;"); // Stil der Box
    }

    //--------------------------Player-Stage-Cards----------------------------------------------------//
    // PlayerCards Section
    // Adds a PlayerCard Box for his beds.
    private PlayerBoxGenerator playerBoxGenerator = new PlayerBoxGenerator();

    public void playerCardsSetter(Player player) {
        for (Node node : rightUpperBox.getChildren()) {
            if (node instanceof GridPane) {
                GridPane gridPane = (GridPane) node;
                for (Node child : gridPane.getChildren()) {
                    if (child instanceof VBox) {
                        VBox playerBox = (VBox) child;

                        if (playerBox.getUserData() != null && (int) playerBox.getUserData() == player.getPlayerId()) {
                            updatePlayerCards(player);
                            return;
                        }
                    }
                }
            }
        }

        GridPane updatedContainer = playerBoxGenerator.addPlayerBox(player.getName(), player.getPlayerId());
        if (!rightUpperBox.getChildren().contains(updatedContainer)) {
            rightUpperBox.getChildren().add(updatedContainer);
        }

        // Aktualisiere den Inhalt der neuen Box
        updatePlayerCards(player);
    }

    // This Method updates the Player Cards based on PlayerDTP.
    // Aktualisiert die Karten für einen bestimmten Spieler basierend auf PlayerDTO
    public void updatePlayerCards(Player player) {
        for (Node node : rightUpperBox.getChildren()) {
            if (node instanceof GridPane) {
                GridPane gridPane = (GridPane) node;

                // Setze gleichmäßige Spaltenbreiten
                gridPane.getColumnConstraints().clear();
                int columnCount = gridPane.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    ColumnConstraints columnConstraints = new ColumnConstraints();
                    columnConstraints.setHgrow(Priority.ALWAYS); // Spalte soll sich ausdehnen
                    columnConstraints.setFillWidth(true);
                    gridPane.getColumnConstraints().add(columnConstraints);
                }

                // Setze Abstände und Padding
                gridPane.setHgap(10); // Horizontaler Abstand zwischen Boxen
                gridPane.setPadding(new Insets(10, 10, 10, 10)); // Abstand um das GridPane

                for (Node child : gridPane.getChildren()) {
                    if (child instanceof VBox) {
                        VBox playerBox = (VBox) child;

                        // Überprüfen, ob die UserData mit der PlayerId übereinstimmt
                        if (playerBox.getUserData() != null && (int) playerBox.getUserData() == player.getPlayerId()) {
                            ScrollPane scrollPane = (ScrollPane) playerBox.getChildren().get(0);
                            VBox contentBox = (VBox) scrollPane.getContent();

                            // Entferne alte Inhalte
                            contentBox.getChildren().clear();

                            // Spielername und ID in großer Schrift
                            Label playerLabel = new Label(player.getName() + " (ID: " + player.getPlayerId() + ")");
                            playerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #664343;");
                            contentBox.getChildren().add(playerLabel);

                            // Camel-Karten (Camel ID + Worth) hinzufügen
                            for (BettingCard card : player.getBettingCards()) {
                                Label cardLabel = new Label(
                                        "Camel: " + card.getCamelId() + "\nWorth: " + card.getWorth() + "p"
                                );
                                cardLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #664343;");
                                contentBox.getChildren().add(cardLabel);
                            }

                            // Strecken des ScrollPane
                            scrollPane.setMaxWidth(Double.MAX_VALUE);
                            scrollPane.setFitToWidth(true); // Inhalt an die Breite anpassen
                            scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

                            // Strecken des Inhalts
                            contentBox.setMaxWidth(Double.MAX_VALUE);
                            contentBox.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

                            // Symmetrisches Padding für den Inhalt
                            scrollPane.setPadding(new Insets(10, 10, 10, 10)); // Gleicher Abstand links und rechts

                            // Aktualisiere das ScrollPane
                            scrollPane.setContent(contentBox);
                            return; // Beende die Methode nach dem Update
                        }
                    }
                }
            }
        }
    }



    //updates the betting cards for the Players for the current "Etappe"
    public void updateStageBettingCards(List<Player> Players) {
        for (Player player : Players) {
            playerCardsSetter(player);
        }
    }

    //--------------------------Winner-Looser-Bet----------------------------------------------------//

    public void addEntryToLoserWinnerBox(int spielerID, int camelID, String wORL) {
        Label entryLabel = new Label("Spieler: " + spielerID + " -> Camel " + " (" + camelID + ")");
        entryLabel.setFont(Font.font("Lilita One", FontWeight.NORMAL, 14));
        entryLabel.setStyle("-fx-text-fill: #000000;");

        if (wORL.equalsIgnoreCase("winner")) {
            int row = (int) winLosePane.getChildren().stream()
                    .filter(node -> GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == 0)
                    .count() + 1;
            winLosePane.add(entryLabel, 0, row);
        } else if (wORL.equalsIgnoreCase("looser")) {
            int row = (int) winLosePane.getChildren().stream()
                    .filter(node -> GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == 1)
                    .count() + 1;
            winLosePane.add(entryLabel, 1, row);
        } else {
            //
        }
    }

    //--------------------------OTHER----------------------------------------------------//

    /**
     * @param name Name des aktuellen Spielers als String
     *             kann genutzt werden um den aktuellen Spieler zu updaten
     */
    public void changeCurrentPlayerDisplay(String name) {
        playerName.setText(name);
    }

    /**
     * @param initialTimeInSeconds ThinkingTime eines Players der gerade am Zug ist in Sekunden
     *                             kann genutzt werden um den Timer des Spielers bei neuem Spieler zurückzuseten usw.
     */
    private void startCountdown(int initialTimeInSeconds) {

        secondsElapsed = initialTimeInSeconds;

        Timeline countdownTimeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), e -> {
            if (secondsElapsed > 0) {
                secondsElapsed--;
                time.setText(formatTime(secondsElapsed));
            } else {
                countdownTimeline.stop(); // Use the `Timeline` instance directly
                System.out.println("Countdown finished!");
            }
        });

// Add the KeyFrame to the Timeline
        countdownTimeline.getKeyFrames().add(keyFrame);

// Set the cycle count and play the timeline
        countdownTimeline.setCycleCount(initialTimeInSeconds);
        countdownTimeline.play();
    }

    public void changeRound(Integer Round) {
        round.setText(Round.toString());
    }

    // this method shows the Winner Screen
    public void endGame(List<String> winners) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("GAME FINISHED!");
        alert.setHeaderText("AND THE WINNER IS:");

        StringBuilder content = new StringBuilder();
        for (int i = 0; i < winners.size(); i++) {
            content.append((i + 1)).append(". PLACE: ").append(winners.get(i)).append("\n");
        }

        alert.setContentText(content.toString());
        alert.showAndWait();
    }

    //sorts Players based on Money
    public List<String> generateScoreboard(List<Player> players) {
        // Sort players by money in descending order
        players.sort((p1, p2) -> Integer.compare(p2.getMoney(), p1.getMoney()));

        // Prepare a scoreboard with player names
        List<String> scoreboard = new ArrayList<>();
        for (Player player : players) {
            scoreboard.add(player.getName());
        }
        return scoreboard;
    }

    //update Method for final ranking
    public void updateFinalScoreboard(List<Player> players) {
        List<String> scoreboard = generateScoreboard(players);
        System.out.println(scoreboard);
        endGame(scoreboard);
    }


    public void onGameStateReceived(GameState gameState) {
        Platform.runLater(() -> {
            try {
                if (gameState.getGamePhase() == GamePhase.FINISHED){
                   updateFinalScoreboard(gameState.getPlayers());
               }


                //initializeGameField(gameState);
                initializeGameField(gameState);

                updatePlayersAndScoreboard(gameState.getPlayers());
                updateScoreboard(gameState.getPlayers());
                //To convert a Set to a List
                List<RolledDice> list = new ArrayList<>();
                list.addAll(gameState.getRolledDice());
                updateRolledDice(list);
                updateStageBettingCards(gameState.getPlayers());
                // TEST was faster remove
                var testValue = gameState.getMoveTimeRemaining() + 4;
                updateRoundAndTime(gameState.getTurns(), testValue);
                if (gameState.getFinalBets() != null) {
                    updateFinalBets(gameState.getFinalBets());
                }
                updateBoardSpacesWithPlayerCards(gameState.getBoardSpaces());
                onGameStatesChanged(gameState, new ArrayList<>());
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Fehler beim Verarbeiten des GameStates: " + e.getMessage());
            }
        });
    }


    private void updatePlayersAndScoreboard(List<Player> players) {
        if (players != null && !players.isEmpty()) {
            Player currentPlayer = players.get(0);
            changeCurrentPlayerDisplay(currentPlayer.getName());
        }

        updateScoreboard(players);
    }

    private void updateRolledDice(List<RolledDice> rolledDice) {
        if (rolledDice == null || rolledDice.isEmpty()) {
            return;
        }
        deleteRemCamelsList(); // Vorherige Würfelergebnisse löschen
        for (RolledDice dice : rolledDice) {
            addPastMoveEntry("Camel " + dice.getCamelId(), dice.getNumber());
        }
    }

    private void updateRoundAndTime(int turns, int moveTimeRemaining) {
        changeRound(turns);
        startCountdown(moveTimeRemaining / 1000);
    }


    private void updateFinalBets(FinalBets finalBets) {
        winLosePane.getChildren().clear();
        winLosePane.add(winner, 0, 0);
        winLosePane.add(looser, 1, 0);

        if (finalBets.getFirstCamel() != null) {
            int winnerRow = 1;
            for (PlayerFinalBet winnerBet : finalBets.getFirstCamel()) {
                Label winnerLabel = new Label("Player: " + winnerBet.getPlayerId() + " -> Camel (" + winnerBet.getCamelId() + ")");
                winnerLabel.setFont(Font.font("Lilita One", FontWeight.NORMAL, 14));
                winnerLabel.setStyle("-fx-text-fill: #000000;");
                winLosePane.add(winnerLabel, 0, winnerRow);
                winnerRow++;
            }
        }

        if (finalBets.getLastCamel() != null) {
            int loserRow = 1;
            for (PlayerFinalBet loserBet : finalBets.getLastCamel()) {
                Label loserLabel = new Label("Player: " + loserBet.getPlayerId() + " -> Camel (" + loserBet.getCamelId() + ")");
                loserLabel.setFont(Font.font("Lilita One", FontWeight.NORMAL, 14));
                loserLabel.setStyle("-fx-text-fill: #000000;");
                winLosePane.add(loserLabel, 1, loserRow);
                loserRow++;
            }
        }
    }

    private void initializeGameField(GameState gameState) {
        GameField newGameField = new GameField(gameState.getBoardSpaces().size());

        gameFieldStack.getChildren().clear();
        gameFieldStack.getChildren().add(newGameField);

        this.gameField = newGameField;
    }


    private void updateBoardSpacesWithPlayerCards(List<BoardSpace> boardSpaces) {
        if (boardSpaces == null || boardSpaces.isEmpty()) {
            return;
        }

        for (int spaceId : gameField.fieldStacks.keySet()) {
            StackPane fieldStack = gameField.fieldStacks.get(spaceId);
            Rectangle background = (Rectangle) fieldStack.getChildren().get(0);

            if (spaceId == 0) {
                background.setFill(Color.web("#FBB948")); //start field yellow
            } else if (spaceId == gameField.fieldStacks.size() - 1) {
                background.setFill(Color.web("#A18C8B")); // grey fields
            } else {
                background.setFill(Color.web("#F5EEE4")); //normal fields
            }

            fieldStack.getChildren().removeIf(node -> node instanceof Label);
        }

        for (BoardSpace boardSpace : boardSpaces) {
            if (boardSpace.getPlayerCard() != null) {
                int spaceId = boardSpace.getSpaceId();
                StackPane fieldStack = gameField.fieldStacks.get(spaceId);

                if (fieldStack != null) {
                    Rectangle background = (Rectangle) fieldStack.getChildren().get(0);
                    PlayerCard playerCard = boardSpace.getPlayerCard();

                    if (playerCard.getSpacesMoved() > 0) {
                        background.setFill(Color.LIGHTBLUE);
                    } else if (playerCard.getSpacesMoved() < 0) {
                        background.setFill(Color.ORANGE);
                    } else {
                        background.setFill(Color.MAGENTA);
                    }

                    Label playerIdLabel = new Label("ID" + playerCard.getPlayerId());
                    playerIdLabel.setStyle("-fx-text-fill: white; -fx-font-size: 8px; -fx-font-weight: bold;");
                    fieldStack.getChildren().add(playerIdLabel);
                    StackPane.setAlignment(playerIdLabel, Pos.CENTER);
                }
            }
        }
    }

    private void addCamelsToGameField(GameState gameState) {
        List<Camel> camelInGame = new ArrayList<>();
        camelInGame.addAll(gameState.getGameConfig().getCamels());
        camelInGame.sort(new CamelComparator());

        gameField.clearGameField();
        List<BoardSpace> boardSpaces = gameState.getBoardSpaces();
        for (int i = 0; i< boardSpaces.size(); i++){
            List<Integer> camelIds = new ArrayList<>();
            camelIds.addAll(boardSpaces.get(i).getCamelIds());
            List<CamelModel> camels = new ArrayList<>();
            for( int j =0; j< camelIds.size(); j++) {
                camels.add(new CamelModel(camelInGame.get(camelIds.get(j)+2)));
            }
            gameField.updateStackOnField(boardSpaces.get(i).getSpaceId().intValue(), camels);
        }
    }
    private class CamelComparator implements Comparator<Camel> {
        @Override
        public int compare(Camel o1, Camel o2) {
            if (o1.getId() < o2.getId()){
                return -1;
            } else if( o1.getId() == o2.getId()) {
                return 0;
            }
            return 1;
        }
    }

    private class GameStateConsumer implements Consumer<GameState> {

        @Override
        public void accept(GameState t) {
            onGameStateReceived(t);
        }
        
    }
}
