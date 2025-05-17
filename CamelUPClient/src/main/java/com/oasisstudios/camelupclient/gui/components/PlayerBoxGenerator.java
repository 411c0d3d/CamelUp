package com.oasisstudios.camelupclient.gui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class PlayerBoxGenerator {

    @Getter
    private final GridPane playerBoxContainer;
    private int currentPlayerCount;
    private int playerID;

    public PlayerBoxGenerator() {
        this.playerBoxContainer = new GridPane();
        this.playerBoxContainer.setPrefSize(287, 585);
        this.playerBoxContainer.setPadding(new Insets(20)); // 20px padding around the grid
        this.playerBoxContainer.setHgap(10); // 10px horizontal gap between boxes
        this.playerBoxContainer.setVgap(10); // 10px vertical gap between boxes
        this.playerBoxContainer.setStyle("-fx-background-color: #593B3B;-fx-background-radius: 16;"); // Brown background
        this.currentPlayerCount = 0;
    }

    /**
     * Creates a new player box and adds it to the main container.
     *
     * @param playerName The name of the player.
     * @return The updated GridPane containing all player boxes.
     */
    public GridPane addPlayerBox(String playerName, int id) {
        int columns = 2; // Fixed 2 columns
        int row = currentPlayerCount / columns;
        int col = currentPlayerCount % columns;

        VBox contentBox = new VBox();
        contentBox.setAlignment(Pos.TOP_LEFT);
        contentBox.setSpacing(5);
        contentBox.setPadding(new Insets(5));
        contentBox.setStyle("-fx-background-color: #F2ECE4;");

        Label playerTitle = new Label(playerName.toUpperCase());
        playerTitle.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #664343;");
        contentBox.getChildren().add(playerTitle);

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #F2ECE4; -fx-border-color: transparent;");
        scrollPane.setPrefSize(120, 175);

        VBox playerBox = new VBox(scrollPane);
        playerBox.setAlignment(Pos.TOP_CENTER);
        playerBox.setStyle("-fx-background-color: #F5EFE5; -fx-background-radius: 16;");
        playerBox.setPrefSize(120, 175);

        playerBox.setUserData(id);

        playerBoxContainer.add(playerBox, col, row);

        currentPlayerCount++;

        return playerBoxContainer;
    }
}
