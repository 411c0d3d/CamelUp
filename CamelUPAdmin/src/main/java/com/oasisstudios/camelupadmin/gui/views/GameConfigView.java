package com.oasisstudios.camelupadmin.gui.views;

import com.oasisstudios.camelupadmin.GUIApplication;
import com.oasisstudios.camelupadmin.dto.*;
import com.oasisstudios.camelupadmin.dto.GameState.GamePhase;
import com.oasisstudios.camelupadmin.gui.components.Clickable;
//import com.oasisstudios.camelupadmin.model.GenerateUniqueCamels;
import com.oasisstudios.camelupadmin.service.ContextProvider;
import com.oasisstudios.camelupadmin.service.CurrentLobbyService;
import com.oasisstudios.camelupadmin.service.CurrentUserInfoService;
import com.oasisstudios.camelupadmin.service.NavigationManager;
import com.oasisstudios.camelupadmin.api.Api;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Inject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * The Game config View.
 */
public class GameConfigView extends AbstractSceneBasedView {
    /**
     * The Client rest service.
     */

    @Inject
    private Api api;

    private final CurrentLobbyService currentLobbyService;
    /**
     * The Current user info service.
     */
    @Inject
    CurrentUserInfoService currentUserInfoService;
    private final GUIApplication guiApplication;
    private final NavigationManager navigationManager;
    private BorderPane mainLayout = new BorderPane();
    private StackPane mainMiddleBox = new StackPane();
    private HBox firstRow = new HBox();
    private HBox secondRow = new HBox();
    private HBox thirdRow = new HBox();
    private VBox fourthRow = new VBox();
    private VBox playerAmount = new VBox();
    private VBox gameFieldSize = new VBox();
    private VBox bidCardsAmount = new VBox();
    private VBox thinkTime = new VBox();
    private VBox camelAmount = new VBox();
    private VBox diceEyeNumber = new VBox();
    private VBox maxGameDuration = new VBox();
    private VBox timeVisualision = new VBox();
    private VBox MaxTurnsAmount = new VBox();
    private HBox importBox = new HBox();
    private HBox exportBox = new HBox();
    private HBox gamestartAndConfig = new HBox();

    private VBox mainPlacement = new VBox();

    private Label headerText = new Label("GAME CONFIGURATION");
    private Label playerAmountText = new Label("\n MAXIMUM PLAYER:");
    private Label gameFieldSizeText = new Label("\n PLAYING FIELD SIZE:");
    private Label bidCardsAmountText = new Label("AMOUNT OF BET SLIPS \n(PER CAMEL):");
    private Label thinkTimeText = new Label("\n TIME PER MOVE (SEC):");
    private Label camelAmountText = new Label("\n AMOUNT OF CAMELS:");
    private Label diceEyeNumberText = new Label("MAXIMUM NUMBER OF  \nEYES(PER DICE):");
    private Label maxGameDurationText = new Label("\n MAXIMUM PLAYING TIME:");
    private Label timeVisualisationText = new Label("MAXIMUM TIME FOR \nVISUALIZE A MOVE:");
    private Label maxAmTurnsText = new Label("\n MAXIMUM TOTAL TURNS:");
    private Label lobbyNameText = new Label("\n NAME OF LOBBY:");
    private TextField playerAmountTextField = new TextField();
    private TextField gameFieldSizeTextField = new TextField();
    private TextField bidCardsAmountTextField = new TextField();
    private TextField thinkTimeTextField = new TextField();
    private TextField camelAmountTextField = new TextField();
    private TextField diceEyeNumberTextField = new TextField();
    private TextField maxGameDurationTextField = new TextField();
    private TextField timeVisualizationTextField = new TextField();
    private TextField maxTurnsAmountTextField = new TextField();
    private TextField lobbyNameTextField = new TextField();

    /**
     * The Consequences.
     */
    ComboBox<String> consequences = new ComboBox<>();

    /**
     * The Create game.
     */
    Button createInitialLobby = new Button("EXPORT GAMECONFIG");
    /**
     * The Export text.
     */
    Button exportText = new Button("EXPORT GAME");
    /**
     * The Import button.
     */
    Button importButton = new Button("name.file");
    /**
     * The Export button.
     */
    //Button exportButton = new Button();

    // Mapping zwischen Enum-Werten und beschreibenden Texten
    Map<String, GameConfig.IllegalMovePenalty> penaltyDescriptions = Map.of(
            "EXCLUDE PLAYER FROM CURRENT STAGE", GameConfig.IllegalMovePenalty.FORFEIT_CURRENT_STAGE,
            "EXCLUDE PLAYER FROM THE GAME", GameConfig.IllegalMovePenalty.FORFEIT_GAME
    );

    /**
     * Instantiates a new Game config form.
     *
     * @param stage the stage
     */
    public GameConfigView(Stage stage) {
        this.primaryStage = stage; // Speichert die Stage-Referenz
        currentUserInfoService = ContextProvider.getContext().getBean(CurrentUserInfoService.class);
        currentLobbyService = ContextProvider.getContext().getBean(CurrentLobbyService.class);
        api = ContextProvider.getContext().getBean(Api.class);
        // initialize Current Lobby and initialize Game State
        currentLobbyService.initializeCurrentLobby();
        this.currentLobbyService.getCurrentLobby().setGameState(new GameState());

        this.guiApplication = ContextProvider.getContext().getBean(GUIApplication.class);
        this.navigationManager = ContextProvider.getContext().getBean(NavigationManager.class);
        this.api = ContextProvider.getContext().getBean(Api.class);

        this.stageTitle = "Admin - Game Configuration | Camel-UP";
        this.userType = currentUserInfoService.getUserType();

        //exportButton.setOnAction(event -> handleExport(this.primaryStage));
        exportText.setOnAction(event -> handleExport(this.primaryStage));

        // Werte zur ComboBox hinzufügen
        consequences.getItems().addAll(penaltyDescriptions.keySet());
        consequences.setValue("EXCLUDE PLAYER FROM CURRENT STAGE");
        consequences.setPromptText("PENALTY FOR ILLEGAL MOVES");
        consequences.setStyle("-fx-background-color: #FFFEFE; -fx-background-radius: 16; -fx-padding: 0 10 0 10;-fx-text-fill: #F2A649");
        consequences.setMaxSize(500, 50);


        // Font
        Font customFont = Font.loadFont(
                getClass().getResourceAsStream("/static/images/Fonts/LilitaOne-Regular.ttf"), 36
        );


        // Text Styling
        headerText.setFont(Font.font("Lilita One", FontWeight.BOLD, 30));
        headerText.setTextFill(Color.web("#F2A649"));
        playerAmountText.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
        playerAmountText.setTextFill(Color.web("#593B3B"));
        gameFieldSizeText.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
        gameFieldSizeText.setTextFill(Color.web("#593B3B"));
        bidCardsAmountText.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
        bidCardsAmountText.setTextFill(Color.web("#593B3B"));
        thinkTimeText.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
        thinkTimeText.setTextFill(Color.web("#593B3B"));
        camelAmountText.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
        camelAmountText.setTextFill(Color.web("#593B3B"));
        diceEyeNumberText.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
        diceEyeNumberText.setTextFill(Color.web("#593B3B"));
        maxGameDurationText.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
        maxGameDurationText.setTextFill(Color.web("#593B3B"));
        timeVisualisationText.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
        timeVisualisationText.setTextFill(Color.web("#593B3B"));
        maxAmTurnsText.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
        maxAmTurnsText.setTextFill(Color.web("#593B3B"));
        lobbyNameText.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
        lobbyNameText.setTextFill(Color.web("#593B3B"));

        // TextField Styling
        playerAmountTextField.setStyle("-fx-background-color: #A18C8B;-fx-background-radius: 16;-fx-prompt-text-fill: #927978;-fx-padding: 0 10 0 10");
        playerAmountTextField.setPromptText("2 TO 6");
        playerAmountTextField.setMaxSize(220, 50);
        playerAmountTextField.setPrefSize(220, 50);
        gameFieldSizeTextField.setStyle("-fx-background-color: #A18C8B;-fx-background-radius: 16;-fx-prompt-text-fill: #927978;-fx-padding: 0 10 0 10");
        gameFieldSizeTextField.setPromptText("MINIMUM 3");
        gameFieldSizeTextField.setMaxSize(220, 50);
        gameFieldSizeTextField.setPrefSize(220, 50);
        bidCardsAmountTextField.setStyle("-fx-background-color: #A18C8B;-fx-background-radius: 16;-fx-prompt-text-fill: #927978;-fx-padding: 0 10 0 10");
        bidCardsAmountTextField.setPromptText("MINIMUM 3");
        bidCardsAmountTextField.setMaxSize(220, 50);
        bidCardsAmountTextField.setPrefSize(220, 50);
        thinkTimeTextField.setStyle("-fx-background-color: #A18C8B;-fx-background-radius: 16;-fx-prompt-text-fill: #927978;-fx-padding: 0 10 0 10");
        thinkTimeTextField.setPromptText("60 E.G.");
        thinkTimeTextField.setMaxSize(220, 50);
        thinkTimeTextField.setPrefSize(220, 50);
        camelAmountTextField.setStyle("-fx-background-color: #A18C8B;-fx-background-radius: 16;-fx-prompt-text-fill: #927978;-fx-padding: 0 10 0 10");
        camelAmountTextField.setPromptText("MINIMUM 2");
        camelAmountTextField.setMaxSize(220, 50);
        camelAmountTextField.setPrefSize(220, 50);
        diceEyeNumberTextField.setStyle("-fx-background-color: #A18C8B;-fx-background-radius: 16;-fx-prompt-text-fill: #927978;-fx-padding: 0 10 0 10");
        diceEyeNumberTextField.setPromptText("FIELD SIZE DIVIDED BY 3");
        diceEyeNumberTextField.setMaxSize(220, 50);
        diceEyeNumberTextField.setPrefSize(220, 50);
        maxGameDurationTextField.setStyle("-fx-background-color: #A18C8B;-fx-background-radius: 16;-fx-prompt-text-fill: #927978;-fx-padding: 0 10 0 10");
        maxGameDurationTextField.setPromptText("NO LIMIT");
        maxGameDurationTextField.setMaxSize(220, 50);
        maxGameDurationTextField.setPrefSize(220, 50);
        timeVisualizationTextField.setStyle("-fx-background-color: #A18C8B;-fx-background-radius: 16;-fx-prompt-text-fill: #927978;-fx-padding: 0 10 0 10");
        timeVisualizationTextField.setPromptText("NO LIMIT");
        timeVisualizationTextField.setMaxSize(220, 50);
        timeVisualizationTextField.setPrefSize(220, 50);
        maxTurnsAmountTextField.setStyle("-fx-background-color: #A18C8B;-fx-background-radius: 16;-fx-prompt-text-fill: #927978;-fx-padding: 0 10 0 10");
        maxTurnsAmountTextField.setPromptText("NO LIMIT");
        maxTurnsAmountTextField.setMaxSize(220, 50);
        maxTurnsAmountTextField.setPrefSize(220, 50);
        lobbyNameTextField.setStyle("-fx-background-color: #A18C8B;-fx-background-radius: 16;-fx-prompt-text-fill: #927978;-fx-padding: 0 10 0 10");
        lobbyNameTextField.setPromptText("NO LIMIT");
        lobbyNameTextField.setMaxSize(220, 50);
        lobbyNameTextField.setPrefSize(220, 50);

        // Button Styling
        exportText.setStyle("-fx-background-color: #664343;-fx-background-radius: 16 0 0 16;-fx-text-fill: #F2A649;-fx-padding: 0 10 0 10");
        importButton.setStyle("-fx-background-color: #A18C8B;-fx-background-radius: 0 16 16 0;-fx-text-fill: #593B3B;-fx-padding: 0 10 0 10");
        //exportButton.setStyle("-fx-background-color: #A18C8B;-fx-background-radius: 0 16 16 0;-fx-text-fill: #593B3B;-fx-padding: 0 10 0 10");
        exportText.setMaxSize(175, 50);
        exportText.setPrefSize(175, 50);
        exportText.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
        importButton.setMaxSize(175, 50);
        importButton.setPrefSize(175, 50);
        //exportButton.setMaxSize(175, 50);
        //exportButton.setPrefSize(175, 50);
        //exportButton.setPromptText("Enter File name");
        createInitialLobby.setStyle("-fx-background-color: #F2A649;-fx-background-radius: 16;-fx-text-fill: #593B3B");
        createInitialLobby.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
        createInitialLobby.setMaxSize(175, 50);
        createInitialLobby.setPrefSize(175, 50);

        // First Row
        playerAmount.getChildren().addAll(playerAmountText, playerAmountTextField);
        gameFieldSize.getChildren().addAll(gameFieldSizeText, gameFieldSizeTextField);
        bidCardsAmount.getChildren().addAll(bidCardsAmountText, bidCardsAmountTextField);
        thinkTime.getChildren().addAll(thinkTimeText, thinkTimeTextField);
        firstRow.setSpacing(20);
        firstRow.getChildren().addAll(playerAmount, gameFieldSize, bidCardsAmount, thinkTime);
        firstRow.setAlignment(Pos.BOTTOM_LEFT);

        // Second Row
        camelAmount.getChildren().addAll(camelAmountText, camelAmountTextField);
        diceEyeNumber.getChildren().addAll(diceEyeNumberText, diceEyeNumberTextField);
        maxGameDuration.getChildren().addAll(maxGameDurationText, maxGameDurationTextField);
        timeVisualision.getChildren().addAll(timeVisualisationText, timeVisualizationTextField);
        secondRow.setSpacing(20);
        secondRow.getChildren().addAll(camelAmount, diceEyeNumber, maxGameDuration, timeVisualision);

        // Third Row
        Label maxTurnsLabel = new Label("\n MAXIMUM TOTAL TURNS:");
        maxTurnsLabel.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
        maxTurnsLabel.setTextFill(Color.web("#593B3B"));

        VBox maxTurnsBox = new VBox();
        maxTurnsBox.getChildren().addAll(maxTurnsLabel, maxTurnsAmountTextField);

        VBox consequencesBox = new VBox();
        Label consequencesLabel = new Label("\n PENALTY FOR ILLEGAL MOVES:");
        consequencesLabel.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
        consequencesLabel.setTextFill(Color.web("#593B3B"));
        consequences.setPrefSize(320, 50); // Setze gleiche Höhe und Breite wie die Textfelder
        consequences.setMaxSize(320, 50);
        consequencesBox.getChildren().addAll(consequencesLabel, consequences);

        HBox maxTurnsLobbyAndConsequences = new HBox();
        maxTurnsLobbyAndConsequences.setSpacing(20);
        maxTurnsLobbyAndConsequences.setAlignment(Pos.CENTER_LEFT);
        maxTurnsLobbyAndConsequences.setPadding(new Insets(10, 0, 0, 0));
        maxTurnsLobbyAndConsequences.getChildren().addAll(maxTurnsBox, consequencesBox);

        // Aktualisiere thirdRow
        thirdRow.setSpacing(20);
        thirdRow.setAlignment(Pos.BOTTOM_LEFT);
        thirdRow.getChildren().addAll(maxTurnsLobbyAndConsequences);


        // Fourth Row
        // exportBox.getChildren().addAll(exportText, exportButton);
        gamestartAndConfig.getChildren().addAll(new Clickable(exportBox), new Clickable(createInitialLobby));
        gamestartAndConfig.setSpacing(415);
        fourthRow.setSpacing(20);
        fourthRow.getChildren().addAll(new Clickable(importBox), gamestartAndConfig);
        fourthRow.setPadding(new Insets(50, 0, 0, 0));

        // Create Gameconfig Button
        createInitialLobby.setOnAction(event -> {handleExport(this.primaryStage);
            // Spielkonfiguration abrufen
//            GameConfig gameConfig = getGameConfig();
//
//            try {
//                // Lobby erstellen und Spielkonfiguration speichern
//                prepareLobby(gameConfig, lobbyNameTextField.getText());
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//            // PlayerSelectionView öffnen
//            PlayerSelectionView playerSelectionView = new PlayerSelectionView(primaryStage);
//            navigationManager.switchScene(playerSelectionView);
        });


        // Main Styling
        mainMiddleBox.setStyle("-fx-background-color: #F4EFE4;-fx-background-radius: 16");
        mainMiddleBox.setAlignment(Pos.CENTER_LEFT);
        mainMiddleBox.setMaxSize(1060, 650); // Mittlere Box größe
        mainPlacement.getChildren().addAll(headerText, firstRow, secondRow, thirdRow, fourthRow);
        mainPlacement.setSpacing(20);
        mainMiddleBox.getChildren().add(mainPlacement);

        mainMiddleBox.setPadding(new Insets(20, 20, 20, 20));
        mainLayout.setCenter(mainMiddleBox);

        this.setLayout(mainLayout);
        this.primaryStage.setScene(this.getScene());
        this.mainLayout.getStylesheets().add(getClass().getResource("/Styling/gameSecStyling.css").toExternalForm());
    }

    private void handleExport(Stage stage) {
        try {
            // Befüllen des GameConfigDTO aus den Textfeldern
            GameConfig gameConfig = getGameConfig();
            String fileName = "game_config_" + currentUserInfoService.getUserName() + "_" + readableTimestamp() + ".json";
            //gameConfig.setIllegalMovePenalty(penaltyDescriptions.get(consequences.getValue()));

            // JSON erstellen
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(gameConfig);

            System.out.println("Generated JSON: " + json);

            if (json == null || json.isEmpty()) {
                showAlert("Error", "Generated JSON is empty. Please check your input fields.", Alert.AlertType.ERROR);
                return;
            }

            // Datei speichern
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Configuration File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
            fileChooser.setInitialFileName(fileName);
            File selectedFile = fileChooser.showSaveDialog(stage);

            if (selectedFile != null) {
                saveConfigToFile(gameConfig, selectedFile);
//                showAlert("Success", "Configuration exported successfully!", Alert.AlertType.INFORMATION);
            } else {
//                showAlert("Error", "No file selected. Export cancelled.", Alert.AlertType.ERROR);
            }

        } catch (NumberFormatException e) {
            showAlert("Error", "Please ensure all numeric fields are filled correctly!", Alert.AlertType.ERROR);
        } catch (IOException e) {
            showAlert("Error", "An error occurred while saving the file: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private GameConfig getGameConfig() {

        GameConfig gameConfig = new GameConfig(parseIntWithDefault(playerAmountTextField.getText()),
                parseIntWithDefault(gameFieldSizeTextField.getText()),
                new ArrayList<>(),
                parseIntWithDefault(diceEyeNumberTextField.getText()),
                parseIntWithDefault(bidCardsAmountTextField.getText()),
                parseIntWithDefault(thinkTimeTextField.getText()),
                parseIntWithDefault(timeVisualizationTextField.getText()),
                penaltyDescriptions.get(consequences.getValue()),
                parseIntWithDefault(maxGameDurationTextField.getText()),
                parseIntWithDefault(maxTurnsAmountTextField.getText()));
                gameConfig.setCamelCount(parseIntWithDefault(camelAmountTextField.getText()));
        this.currentLobbyService.getCurrentLobby().getGameState().setGameConfig(gameConfig);
        return gameConfig;
    }

    private void sendLobby(CreateLobby createLobby) throws IOException {
        api.sendPacket(Packet.builder().withType(Packet.Type.CREATE_LOBBY).withContent(createLobby).build());
    }

    //   /**
    //    * Submit game config.
    //    *
    //    * @param gameConfig the game config
    //    */
//
    void prepareLobby(GameConfig gameConfig, String name) throws IOException {

        var lobby = this.currentLobbyService.getCurrentLobby();
        if (lobby != null) {
            lobby.setName(name);
            lobby.getGameState().setGameConfig(gameConfig);
            lobby.getGameState().setGamePhase(GamePhase.CREATED);
            lobby.setLobbyId(0);
            sendLobby(new CreateLobby(lobby));
            this.currentLobbyService.updateCurrentLobby(lobby);
        } else {
            System.err.println("Unexpected null Lobby object");
        }
    }

    private void handleImport(Stage stage) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Configuration File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile == null) {
            System.err.println(this.getClass().getSimpleName() + ": No file was selected!");
            return;
        }
        importButton.setText(selectedFile.getName());

        try {
            Gson gson = new Gson();
            GameConfig gameConfig = gson.fromJson(new FileReader(selectedFile), GameConfig.class);

            if (gameConfig != null) {
                playerAmountTextField.setText(String.valueOf(gameConfig.getPlayerCount()));
                gameFieldSizeTextField.setText(String.valueOf(gameConfig.getNumberOfSpaces()));
                bidCardsAmountTextField.setText(String.valueOf(gameConfig.getNumberOfBettingCards()));
                thinkTimeTextField.setText(String.valueOf(gameConfig.getThinkingTime()));
//                camelAmountTextField.setText(String.valueOf(gameConfig.getCamels().size()));
                camelAmountTextField.setText(String.valueOf(gameConfig.getCamelCount()));
                diceEyeNumberTextField.setText(String.valueOf(gameConfig.getDiceFaces()));
                maxGameDurationTextField.setText(String.valueOf(gameConfig.getMaxGameDuration()));
                timeVisualizationTextField.setText(String.valueOf(gameConfig.getVisualizationTime()));
                maxTurnsAmountTextField.setText(String.valueOf(gameConfig.getMaxGameDuration()));
                String consequenceVal = gameConfig.getIllegalMovePenalty().getValue();
                String consequenceKey = penaltyDescriptions.entrySet().stream().filter(e -> e.getValue().getValue().equals(consequenceVal)).findFirst().get().getKey();
                consequences.setValue(consequenceKey);

//                showAlert("Success", "Configuration imported successfully!", Alert.AlertType.INFORMATION);
            } else {
//                showAlert("Error", "Failed to parse the configuration file. Please check the file format.", Alert.AlertType.ERROR);
            }

        } catch (IOException e) {
            showAlert("Error", "An error occurred while reading the file: " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private int parseIntWithDefault(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveConfigToFile(GameConfig config, File file) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(config);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
        }
    }

    public static String readableTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return LocalDateTime.now().format(formatter);
    }
}
