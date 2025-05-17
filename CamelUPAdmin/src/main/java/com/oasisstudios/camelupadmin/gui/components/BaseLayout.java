package com.oasisstudios.camelupadmin.gui.components;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

/**
 * The Base layout.
 */
public class BaseLayout extends BorderPane {
    /**
     * Instantiates a new Base layout.
     *
     * @param content the content
     * @param footer  the footer
     */
    public BaseLayout(Node content, Footer footer) {
        this.setCenter(content);
        this.setBottom(footer);
//        this.setStyle("-fx-background-color: #f9f9f9;");
    }
}