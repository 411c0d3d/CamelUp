package com.oasisstudios.camelupadmin.gui.views;

import com.oasisstudios.camelupadmin.api.Api;
import com.oasisstudios.camelupadmin.dto.*;
import com.oasisstudios.camelupadmin.gui.components.Clickable;
import com.oasisstudios.camelupadmin.service.ContextProvider;
import com.oasisstudios.camelupadmin.service.CurrentLobbyService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;

public class PlayerSelectionView extends AbstractSceneBasedView {
    @Inject
    private Api api;

    @Inject
    private CurrentLobbyService currentLobbyService;

    private BorderPane mainLayout;
    private TableView<PlayerInGame> startGameTable = new TableView<>();
    private VBox middleFrame = new VBox();
    private HBox buttons = new HBox();
    private StackPane middleStack = new StackPane();
    private Integer activePlayer = 0;
    private Button startGame = new Button("START GAME");
    private ObservableList<PlayerInGame> tablePlayers = FXCollections.observableArrayList();
    private Integer playerAmount = 0;
    private String currentPlayerAmount;
    private ArrayList<Player> orderedPlayers = new ArrayList<>();
    private Lobby lobby;
    private PendingPlayerList pendingPlayerList;
    private final HBox playersAddedBox = new HBox();
    public final Label addedPlayersLabel = new Label();
    private Button createLobby = new Button("CREATE LOBBY");
    private Button cancelButton = new Button("CANCEL");

    public PlayerSelectionView(Stage stage) {
        currentLobbyService = ContextProvider.getContext().getBean(CurrentLobbyService.class);
        api = ContextProvider.getContext().getBean(Api.class);
        lobby = currentLobbyService.getCurrentLobby();
        this.pendingPlayerList = new PendingPlayerList(new ArrayList<>());
        this.primaryStage = stage;
        this.stageTitle = "Camel-UP Admin - Lobby Creation";
        mainLayout = new BorderPane();

        startGame.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
        startGame.setStyle("-fx-background-radius: 16; -fx-background-color: #F2A649; -fx-text-fill: #674342;");
        startGame.setMaxSize(150, 40);
        startGame.setPrefSize(150, 40);

        createLobby.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
        createLobby.setStyle("-fx-background-radius: 16; -fx-background-color: #F2A649; -fx-text-fill: #674342;");
        createLobby.setMaxSize(150, 40);
        createLobby.setPrefSize(150, 40);

        cancelButton.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
        cancelButton.setStyle("-fx-background-radius: 16; -fx-background-color: #F2A649; -fx-text-fill: #674342;");
        cancelButton.setMaxSize(150, 40);
        cancelButton.setPrefSize(150, 40);

        TableColumn<PlayerInGame, String> nameColumn = new TableColumn<>("SELECT PLAYER");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().playerNameProperty());

        TableColumn<PlayerInGame, Button> addColumn = new TableColumn<>(currentPlayerAmount);
        addColumn.setCellValueFactory(cellData -> cellData.getValue().buttonProperty());

        TableColumn<PlayerInGame, String> orderColumn = new TableColumn<>("ORDER");
        orderColumn.setCellValueFactory(cellData -> cellData.getValue().playerOrderProperty());

        startGameTable.getColumns().addAll(nameColumn, addColumn, orderColumn);
        nameColumn.prefWidthProperty().bind(startGameTable.widthProperty().multiply(0.33));
        addColumn.prefWidthProperty().bind(startGameTable.widthProperty().multiply(0.33));
        orderColumn.prefWidthProperty().bind(startGameTable.widthProperty().multiply(0.33));

        middleFrame.setPrefSize(this.primaryStage.getWidth() * 0.50, 370);
        middleStack.setPrefSize(this.primaryStage.getWidth() * 0.50, 370);
        middleFrame.setAlignment(Pos.CENTER);
        middleStack.setAlignment(Pos.CENTER);

        buttons.getChildren().addAll(new Clickable(cancelButton), new Clickable(createLobby), new Clickable(startGame));
        buttons.setSpacing(20);
        buttons.setAlignment(Pos.CENTER);

        playersAddedBox.setMinHeight(40);
        playersAddedBox.setMaxWidth(this.primaryStage.getWidth() * 0.50);
        playersAddedBox.getChildren().add(addedPlayersLabel);
        playersAddedBox.setAlignment(Pos.CENTER);

        startGameTable.setItems(tablePlayers);
        startGameTable.setPrefSize(this.primaryStage.getWidth() * 0.65, 370);
        startGameTable.setMaxSize(this.primaryStage.getWidth() * 0.65, 370);
        middleFrame.getChildren().addAll(startGameTable, playersAddedBox, buttons);

        middleStack.getChildren().add(middleFrame);
        playerAmount = pendingPlayerList.getPlayers().size();
        currentPlayerAmount = activePlayer + "/" + playerAmount;
        mainLayout.setCenter(middleStack);

        startAutoUpdate();
        this.setLayout(mainLayout);
        this.primaryStage.setScene(this.getScene());
        this.mainLayout.getStylesheets().add(getClass().getResource("/Styling/gameSecStyling.css").toExternalForm());

        createLobby.setOnAction(event -> {
            try {
                extractPlayersFromSelection();
                lobby.getGameState().setGamePhase(GameState.GamePhase.CREATED);
                createLobby(lobby.getGameState().getGameConfig(), lobby.getName(), lobby.getLobbyId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        cancelButton.setOnAction(event -> {});
        startGame.setOnAction(event -> sendGameStart());
    }

    public class PlayerInGame {
        private final StringProperty playerName;
        private final StringProperty playerOrder;
        private final Button addToGame = new Button("ADD TO GAME");
        @Getter
        private Player currentPlayer;

        public PlayerInGame(Player player, PlayerSelectionView playerSelectionView) {
            currentPlayer = player;
            playerName = new SimpleStringProperty(player.getName());
            playerOrder = new SimpleStringProperty();
            addToGame.setText("ADD");
            addToGame.setOnAction(event -> {
                if (playerSelectionView.orderedPlayers.contains(player)) {
                    playerSelectionView.orderedPlayers.remove(player);
                    addToGame.setText("ADD");
                    this.playerOrder.set("");
                } else {
                    playerSelectionView.addPlayer(player);
                    this.playerOrder.set(String.valueOf(playerSelectionView.orderedPlayers.size()));
                    addToGame.setText("REMOVE");
                }
                ArrayList<String> playerNames = new ArrayList<>();
                playerSelectionView.orderedPlayers.forEach(p -> playerNames.add(p.getName()));
                playerSelectionView.addedPlayersLabel.setText(playerNames.toString());
            });
        }

        public StringProperty playerNameProperty() {
            return playerName;
        }

        public ObjectProperty<Button> buttonProperty() {
            return new SimpleObjectProperty<>(addToGame);
        }

        public StringProperty playerOrderProperty() {
            return playerOrder;
        }
    }

    private void extractPlayersFromSelection() throws IOException {
        lobby.getGameState().setPlayers(orderedPlayers);
    }

    private void createLobby(GameConfig gameConfig, String name, int lobbyId) throws IOException {
        GameState.GamePhase gamephase = (lobbyId != -1) ? GameState.GamePhase.CREATED : null;

        var lobby = this.currentLobbyService.getCurrentLobby();
        if (lobby != null) {
            lobby.setName(name);
            lobby.getGameState().setGameConfig(gameConfig);
            lobby.getGameState().setGamePhase(gamephase);
            lobby.setLobbyId(lobbyId);
            this.currentLobbyService.updateCurrentLobby(lobby);
            sendLobby(new CreateLobby(lobby));
        } else {
            System.err.println("Unexpected null Lobby object");
        }
    }

    private void sendLobby(CreateLobby createLobby) throws IOException {
        api.sendPacket(Packet.builder().withType(Packet.Type.CREATE_LOBBY).withContent(createLobby).build());
    }

    private void sendGameStart() {
        StartGame startGame = new StartGame(this.currentLobbyService.getCurrentLobby().getLobbyId());
        try {
            api.sendPacket(Packet.builder().withType(Packet.Type.START_GAME).withContent(startGame).build());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addPlayer(Player selectedPlayer) {
        if (!orderedPlayers.contains(selectedPlayer)) {
            orderedPlayers.add(selectedPlayer);
        }
    }

    private void startAutoUpdate() {
        // Create a Timeline to fetch PendingPlayersList periodically
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), event -> updatePendingPlayersList())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);  // Runs indefinitely
        timeline.play();  // Start the timeline
    }

    private void updatePendingPlayersList() {
        api.getPendingPlayersList().thenAccept(pendingPlayerList -> {
            Platform.runLater(() -> {
                // Update the table with new pending players
                tablePlayers.setAll(pendingPlayerList.getPlayers().stream()
                        .map(player -> new PlayerInGame(player, this))
                        .toList());

                // Update the added players label
                addedPlayersLabel.setText(pendingPlayerList.getPlayers().toString());
            });
        });
    }
}