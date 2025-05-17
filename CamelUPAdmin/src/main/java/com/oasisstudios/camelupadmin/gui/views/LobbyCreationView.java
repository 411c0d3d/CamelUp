//package com.oasisstudios.camelupclient.gui.views;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import java.util.concurrent.Future;
//
//import javax.inject.Inject;
//
//import com.oasisstudios.camelupclient.api.Api;
//import com.oasisstudios.camelupclient.dto.*;
//
//import com.oasisstudios.camelupclient.service.ContextProvider;
//
//import com.oasisstudios.camelupclient.service.UserType;
//import javafx.beans.property.ObjectProperty;
//import javafx.beans.property.SimpleObjectProperty;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.beans.property.StringProperty;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.geometry.Pos;
//
//import javafx.scene.control.*;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.StackPane;
//import javafx.scene.layout.VBox;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import javafx.stage.Stage;
//
//public class LobbyCreationView extends AbstractSceneBasedView {
//
//    @Inject
//    Api api;
//    private BorderPane mainLayout;
//    private TableView<PlayerInGame> startGameTable = new TableView<>();
//    private VBox middleFrame = new VBox();
//    private StackPane middleStack = new StackPane();
//    private Integer activePlayer = 0;
//    private String currentPlayerAmount;
//    private Button startGame = new Button("START GAME");
//    private Button createGame = new Button("CREATE GAME");
//    //    private Button playerButton = new Button("REFRESH PLAYER");
//    private ObservableList<PlayerInGame> players = FXCollections.observableArrayList();
//    private ArrayList<Player> playerOrder;
//    Lobby lobby;
//
//    public LobbyCreationView(Stage stage, Lobby lobby) {
//        this.primaryStage = stage;
//        this.stageTitle = "Camel-UP Admin - Lobby Creation";
//        this.userType = UserType.ADMIN;
//        this.api = ContextProvider.getContext().getBean(Api.class);
//        this.lobby = lobby;
//        mainLayout = new BorderPane(); // Neues Layout für jede Instanz
//        currentPlayerAmount = activePlayer + "/" + maxPlayerCount(lobby);
//        playerOrder = new ArrayList<Player>(Integer.parseInt(maxPlayerCount(lobby)));
//        for (int i = 0; i < Integer.parseInt(maxPlayerCount(lobby)); i++) {
//            playerOrder.add(null);
//        }
////        Font font = Font.loadFont(getClass().getResourceAsStream("LilitaOne-Regular.ttf"), 18);
//
//        startGame.setFont(Font.font("Lilita One", FontWeight.BOLD, 15));
//        startGame.setStyle("-fx-background-radius: 16; -fx-background-color: #F2A649; -fx-text-fill: #674342;");
//        startGame.setMaxSize(150, 40);
//        startGame.setPrefSize(150, 40);
//
//        TableColumn<PlayerInGame, String> nameColumn = new TableColumn<>("SELECT PLAYER");
//        nameColumn.setCellValueFactory(cellData -> cellData.getValue().playerNameProperty());
//
//        TableColumn<PlayerInGame, ComboBox<Integer>> turnColumn = new TableColumn<>("TURN ORDER");
//        turnColumn.setCellValueFactory(cellData -> cellData.getValue().comboBoxProperty());
//
//        TableColumn<PlayerInGame, Button> addColumn = new TableColumn<>(currentPlayerAmount);
//        addColumn.setCellValueFactory(cellData -> cellData.getValue().buttonProperty());
//
//        startGameTable.getColumns().addAll(nameColumn, turnColumn, addColumn);
//
//        nameColumn.prefWidthProperty().bind(startGameTable.widthProperty().multiply(0.4));
//        turnColumn.prefWidthProperty().bind(startGameTable.widthProperty().multiply(0.3));
//        addColumn.prefWidthProperty().bind(startGameTable.widthProperty().multiply(0.3));
//
//        middleFrame.setPrefSize(670, 370);
//        middleStack.setPrefSize(670, 370);
//        middleFrame.setAlignment(Pos.CENTER);
//        middleStack.setAlignment(Pos.CENTER);
//
//        startGameTable.setItems(players);
//        startGameTable.setPrefSize(670, 370);
//        startGameTable.setMaxSize(670, 370);
//        middleFrame.getChildren().addAll(startGameTable, startGame);
//
//        middleStack.getChildren().add(middleFrame);
//
////        refreshPlayers();
//
//        mainLayout.setCenter(middleStack);
//
//        this.setLayout(mainLayout);
//        this.primaryStage.setScene(this.getScene());
//        this.mainLayout.getStylesheets().add(getClass().getResource("/Styling/gameSecStyling.css").toExternalForm());
//
//        createGame.setOnAction(event -> {
//            List<Player> chosenPlayers = new ArrayList<Player>();
//            System.err.println(playerOrder);
//            for (int i = 0; i < playerOrder.size(); i++) {
//                if (playerOrder.get(i) != null) {
//                    Player chosen = playerOrder.get(i);
//                    chosenPlayers.add(Player.builder().withName(chosen.getName()).withPlayerId(chosen.getPlayerId()).build());
//                }
//            }
//
//            GameState gameState = lobby.getGameState();
//            gameState.setPlayers(chosenPlayers);
//            lobby.setGameState(gameState);
//            try {
//                api.sendPacket(Packet.builder().withType(Packet.Type.CREATE_LOBBY).withContent(lobby).build());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//
//        startGame.setOnAction(event -> {
////            refreshPlayers();
//
//        });
//    }
//
//    private String maxPlayerCount(Lobby lobby) {
//        return Integer.toString(lobby.getGameState().getGameConfig().getPlayerCount());
//    }
//
////    private void refreshPlayers(){
////        Future<PendingPlayerList> future = api.listPlayers();
////        try {
////            PendingPlayerList res = future.get();
////            players.clear();
////            playerOrder.clear();
////            activePlayer = 0;
////            currentPlayerAmount = activePlayer + "/"+maxPlayerCount(lobby);
////            for(int i =0; i<Integer.parseInt(maxPlayerCount(lobby)); i++){
////                playerOrder.add(null);
////            }
////            ArrayList<RegisteredPlayer> prospectivePlayers = res.getPlayers();
////            while(!prospectivePlayers.isEmpty()){
////                players.add(new PlayerInGame(prospectivePlayers.removeFirst()));
////            }
////        } catch (Exception e) {
////            System.err.println(e.getMessage());
////        }
////    }
//
//    public class PlayerInGame {
//        private final StringProperty playerName;
//        private final Player prospectivePlayer;
//        private final ComboBox<Integer> turnOrderComboBox = new ComboBox<>();
//        private final Button addToGame = new Button("ADD TO GAME");
//        private boolean isAdded = false;
//
//        public PlayerInGame(Player newPlayer) {
//            this.prospectivePlayer = newPlayer;
//            playerName = new SimpleStringProperty(prospectivePlayer.getName());
//
//            turnOrderComboBox.getItems().addAll(1, 2, 3, 4, 5, 6);
//            turnOrderComboBox.setPromptText("TURN ORDER");
//            turnOrderComboBox.getStyleClass().add("combo-box");
//
//            turnOrderComboBox.setOnAction(event -> {
//                Integer selectedValue = turnOrderComboBox.getValue();
//                if (selectedValue != null) {
//                    turnOrderComboBox.setStyle("-fx-background-color: #FAB848; -fx-text-fill: #593B3B;");
//
//                    players.forEach(player -> {
//                        if (player != this) {
//                            player.turnOrderComboBox.getItems().remove(selectedValue);
//                        }
//                    });
//                }
//            });
//
//            addToGame.getStyleClass().add("add-button");
//            addToGame.setOnAction(event -> {
//                if (!isAdded && turnOrderComboBox.getValue() != null) {
//                    isAdded = true;
//                    Integer selectedValue = turnOrderComboBox.getValue();
//                    activePlayer = (activePlayer + 1) % Integer.parseInt(maxPlayerCount(lobby));
//                    currentPlayerAmount = activePlayer + "/" + maxPlayerCount(lobby);
//                    startGameTable.getColumns().get(2).setText(currentPlayerAmount);
//                    addToGame.getStyleClass().add("added-button");
//                    addToGame.setText("ADDED");
//
//                    players.forEach(player -> {
//                        if (player != this) {
//                            player.turnOrderComboBox.getItems().remove(selectedValue);
//                        }
//                    });
//                    playerOrder.add(selectedValue, this.prospectivePlayer);
//                    System.out.println("Player Added: " + playerName.get());
//                }
//            });
//        }
//
//        public StringProperty playerNameProperty() {
//            return playerName;
//        }
//
//        public ObjectProperty<ComboBox<Integer>> comboBoxProperty() {
//            return new SimpleObjectProperty<>(turnOrderComboBox);
//        }
//
//        public ObjectProperty<Button> buttonProperty() {
//            return new SimpleObjectProperty<>(addToGame);
//        }
//    }
//}
