package com.oasisstudios.camelupclient.gui.views;

import com.oasisstudios.camelupclient.GUIApplication;
import com.oasisstudios.camelupclient.api.Api;
import com.oasisstudios.camelupclient.dto.Lobby;
import com.oasisstudios.camelupclient.dto.LobbyList;
import com.oasisstudios.camelupclient.gui.components.JoinLobbyButtonCell;
import com.oasisstudios.camelupclient.service.ContextProvider;
import com.oasisstudios.camelupclient.service.UserType;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class GamesSelectionView extends AbstractSceneBasedView {

    @Inject 
    private Api api;
    private final GUIApplication guiApplication;
    private ScheduledExecutorService executor;
    private final BorderPane mainLayout = new BorderPane();
    private final TableView<Lobby> tableView = new TableView<>();
    private final ObservableList<Lobby> lobbyList = FXCollections.observableArrayList();

    public GamesSelectionView(Stage stage) {
        this.primaryStage = stage;
        this.api = ContextProvider.getContext().getBean(Api.class);
        this.guiApplication = ContextProvider.getContext().getBean(GUIApplication.class);
        this.stageTitle = "Camel-UP Spectator";
        this.userType = UserType.SPECTATOR;

        configureTableView();
        mainLayout.setCenter(tableView);
        setupContinuousExecution();
        
        // For Testing UI 
//        addExampleLobbies();

        this.setLayout(mainLayout);
        this.primaryStage.setScene(this.getScene());
        this.mainLayout.getStylesheets().add(getClass().getResource("/Styling/gameSecStyling.css").toExternalForm());
    }

    private void configureTableView() {
        TableColumn<Lobby, Integer> lobbyIdColumn = new TableColumn<>("Lobby ID");
        lobbyIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getLobbyId()).asObject());
        lobbyIdColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Lobby, Integer> playersColumn = new TableColumn<>("Player Count");
        playersColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getGameState().getPlayers().size()).asObject());
        playersColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Lobby, Integer> spectatorsColumn = new TableColumn<>("Spectator Count");
        spectatorsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getObserverIds().size()).asObject());
        spectatorsColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Lobby, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGameState().getGamePhase().toString()));
        statusColumn.setStyle("-fx-alignment: CENTER;");
        TableColumn<Lobby, Void> actionColumn = new TableColumn<>("Join");
        actionColumn.setCellFactory(col -> new JoinLobbyButtonCell(guiApplication, this));
        actionColumn.setStyle("-fx-alignment: CENTER;");
        tableView.setRowFactory(tv -> {
            TableRow<Lobby> row = new TableRow<>();
            row.setOnMouseEntered(event -> row.setStyle("-fx-cursor: default;"));
            return row;
        });
        tableView.getColumns().addAll(lobbyIdColumn, playersColumn, spectatorsColumn, statusColumn, actionColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setItems(lobbyList);
    }

    private void displayLobbies(LobbyList lobbyList) {
        this.lobbyList.clear();
        if (lobbyList.getLobbies() != null) {
            this.lobbyList.addAll(lobbyList.getLobbies());
        }
    }

//    private void addExampleLobbies() {
//        // Example players
//        PlayerDTO player1 = new PlayerDTO();
//        player1.setName("Alice");
//        player1.setPlayerId(1);
//        player1.setMoney(100);
//
//        PlayerDTO player2 = new PlayerDTO();
//        player2.setName("Bob");
//        player2.setPlayerId(2);
//        player2.setMoney(50);
//
//        PlayerDTO player3 = new PlayerDTO();
//        player3.setName("Charlie");
//        player3.setPlayerId(3);
//        player3.setMoney(75);
//
//        // Example lobbies
//        LobbyDTO lobby1 = createLobby(1, "Lobby1", "running", Arrays.asList(player1, player2), Arrays.asList(1, 2));
//        LobbyDTO lobby2 = createLobby(2, "Lobby2", "waiting", Arrays.asList(player1, player3), Arrays.asList(1, 2));
//        LobbyDTO lobby3 = createLobby(3, "Lobby3", "finished", Arrays.asList(player2, player3), Arrays.asList(1, 2));
//        LobbyDTO lobby4 = createLobby(4, "Lobby4", "running", Arrays.asList(player1), Arrays.asList(1, 2));
//        LobbyDTO lobby5 = createLobby(5, "Lobby5", "waiting", Arrays.asList(player2), Arrays.asList(1, 2));
//
//        LobbyListDTO lobbyListDTO = new LobbyListDTO();
//        lobbyListDTO.setLobbyList(Arrays.asList(lobby1, lobby2, lobby3, lobby4, lobby5));
//
//        displayLobbies(lobbyListDTO);
//    }

    /*private LobbyDTO createLobby(int lobbyId, String name, String gameStatus, List<PlayerDTO> players, List<Integer> observerIds) {
        LobbyDTO lobbyDTO = new LobbyDTO();
        lobbyDTO.setLobbyId(lobbyId);
        lobbyDTO.setName(name);
        var gameState = new GameStateDTO();
        gameState.setGamePhase(gameStatus);
        lobbyDTO.setGameState(gameState);
        lobbyDTO.setPlayers(players);
        lobbyDTO.setObserverIds(observerIds);
        return lobbyDTO;
    }*/

    private void loadLobbiesAsync() {
        Platform.runLater(() -> {
            CompletableFuture<LobbyList> future = api.listLobbies();

            future.thenAccept(response -> Platform.runLater(() -> {
                if (response.getLobbies() != null) {
                    displayLobbies(response);
                }
            })).exceptionally(ex -> {
                System.err.println("Error loading lobbies: " + ex.getMessage());
                return null;
            });
        });
    }

    private void setupContinuousExecution() {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::loadLobbiesAsync, 0, 3, TimeUnit.SECONDS);
    }
    
    // Shutdown executor when no longer needed
    public void stop() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}
