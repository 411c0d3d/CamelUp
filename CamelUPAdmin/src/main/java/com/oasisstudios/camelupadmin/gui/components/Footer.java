package com.oasisstudios.camelupadmin.gui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class Footer extends HBox{
    private static Footer instance;

    private Footer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        this.setPadding(new Insets(5, 10, 0, 10)); // Top 5px, Right 10px, Bottom 15px, Left 20px
        this.getStylesheets().add(getClass().getResource("/Styling/gameSecStyling.css").toExternalForm());
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(new Clickable(new BackButton()), spacer, new ConnectionStatusWidget().widget);
    }
    
    public static Footer getInstance() {
        if (instance == null) {
            instance = new Footer();
        }
        return instance;
    }
}
