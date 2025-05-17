package com.oasisstudios.camelupadmin.gui.views;


import com.oasisstudios.camelupadmin.dto.Lobby;
import com.oasisstudios.camelupadmin.dto.LobbyList;
import com.oasisstudios.camelupadmin.model.LobbyModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AdminDashboardView extends AbstractSceneBasedView {
    private final BorderPane mainLayout = new BorderPane();
    private final HBox middleElements = new HBox();
    private final VBox mainElemnVBOx = new VBox();
    private final TableView<LobbyModel> currentGames = new TableView<>();
    private final StackPane middleMainBox = new StackPane();
    private final StackPane gameConfigBox = new StackPane();
    private final StackPane quickStartBox = new StackPane();
    private final Label gameConfigText = new Label("CREATE GAMECONFIG");


    private final ObservableList<LobbyModel> lobbyList = FXCollections.observableArrayList();

    public AdminDashboardView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.stageTitle = "Admin - Game Management";

        initializeUI();
        this.setLayout(mainLayout);
        this.primaryStage.setScene(this.getScene());
    }

    private void initializeUI() {
        // Text Styling
        gameConfigText.setFont(Font.font("Lilita One", FontWeight.BOLD, 20));
        gameConfigText.setTextFill(Color.web("#F2A649"));

        // Box 1: Game Config Box
        gameConfigBox.getChildren().add(gameConfigText);
        gameConfigBox.setStyle("-fx-background-color: #593B3B;-fx-background-radius: 16;");
        gameConfigBox.setMaxSize(200, 100);
        gameConfigBox.setPrefSize(200, 100);
        gameConfigBox.setOnMouseClicked(event -> openGameConfigView());

        // styling for centered box
        middleMainBox.setStyle("-fx-background-color: #F4EFE4;-fx-background-radius: 16;");
        middleMainBox.setMaxSize(1000, 200);

        // Table config
        currentGames.setPrefSize(615, 300);
        currentGames.setMaxSize(615, 300);
        configureTableView();

        middleElements.getChildren().addAll(gameConfigBox, quickStartBox);
        middleMainBox.getChildren().add(middleElements);
        middleMainBox.setAlignment(Pos.CENTER);

        mainElemnVBOx.getChildren().addAll(middleElements, currentGames);
        mainElemnVBOx.setSpacing(20);
        mainElemnVBOx.setAlignment(Pos.CENTER);

        mainLayout.setCenter(mainElemnVBOx);
        middleElements.setAlignment(Pos.CENTER);
        middleElements.setSpacing(20);

        //addExampleLobbies();
    }

    private void configureTableView() {
        TableColumn<LobbyModel, Integer> lobbyIdColumn = new TableColumn<>("LOBBY-ID");
        lobbyIdColumn.setCellValueFactory(cellData -> cellData.getValue().getLobbyId().asObject());

        TableColumn<LobbyModel, String> statusColumn = new TableColumn<>("STATUS");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().getStatus());

        TableColumn<LobbyModel, Void> cancelColumn = new TableColumn<>("CANCEL");
        cancelColumn.setCellFactory(col -> new TableCell<>() {
            private final Button cancelButton = new Button("CANCEL");

            {
                cancelButton.setOnAction(event -> {
                    LobbyModel lobby = getTableView().getItems().get(getIndex());
                    System.out.println("Cancel button clicked for lobby: " + lobby.getLobbyId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(cancelButton);
                }
            }
        });

        TableColumn<LobbyModel, Void> pauseColumn = new TableColumn<>("PAUSE");
        pauseColumn.setCellFactory(col -> new TableCell<>() {
            private final Button pauseButton = new Button("PAUSE");

            {
                pauseButton.setOnAction(event -> {
                    LobbyModel lobby = getTableView().getItems().get(getIndex());
                    System.out.println("Pause button clicked for lobby: " + lobby.getLobbyId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pauseButton);
                }
            }
        });

        currentGames.getColumns().addAll(lobbyIdColumn, statusColumn, cancelColumn, pauseColumn);
        currentGames.setItems(lobbyList);
        currentGames.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    /* 
    private Lobby createLobby(int lobbyId, String name, String gameStatus, List<Player> players, List<Integer> observerIds) {
        Lobby lobbyDTO = new LobbyDTO();
        lobbyDTO.setLobbyId(lobbyId);
        lobbyDTO.setName(name);
        var gameState = new GameStateDTO();
        gameState.setGamePhase(gameStatus);
        lobbyDTO.setGameState(gameState);
        lobbyDTO.setPlayers(players);
        lobbyDTO.setObserverIds(observerIds);
        return lobbyDTO;
    }*/

    private void displayLobbies(LobbyList lobbyListDTO) {
        lobbyList.clear();
        if (lobbyListDTO.getLobbies() != null) {
            for (Lobby lobbyDTO : lobbyListDTO.getLobbies()) {
                lobbyList.add(new LobbyModel(lobbyDTO));
            }
        }
    }
    /* Example lobbies with player
    private void addExampleLobbies() {

        PlayerDTO player1 = new PlayerDTO();
        player1.setName("Alice");
        player1.setPlayerId(1);
        player1.setMoney(100);

        PlayerDTO player2 = new PlayerDTO();
        player2.setName("Bob");
        player2.setPlayerId(2);
        player2.setMoney(50);

        PlayerDTO player3 = new PlayerDTO();
        player3.setName("Charlie");
        player3.setPlayerId(3);
        player3.setMoney(75);

        LobbyDTO lobby1 = createLobby(1, "Lobby1", "running", Arrays.asList(player1, player2), Arrays.asList(1, 2));
        LobbyDTO lobby2 = createLobby(2, "Lobby2", "waiting", Arrays.asList(player1, player3), Arrays.asList(1, 2));
        LobbyDTO lobby3 = createLobby(3, "Lobby3", "finished", Arrays.asList(player2, player3), Arrays.asList(1, 2));
        LobbyDTO lobby4 = createLobby(4, "Lobby4", "running", Arrays.asList(player1), Arrays.asList(1, 2));
        LobbyDTO lobby5 = createLobby(5, "Lobby5", "waiting", Arrays.asList(player2), Arrays.asList(1, 2));

        LobbyListDTO lobbyListDTO = new LobbyListDTO();
        lobbyListDTO.setLobbyList(Arrays.asList(lobby1, lobby2, lobby3, lobby4, lobby5));

        displayLobbies(lobbyListDTO);
    }*/

    private void openGameConfigView() {
        GameConfigView gameConfigView = new GameConfigView(primaryStage);
        primaryStage.setScene(gameConfigView.getScene());
    }
}
