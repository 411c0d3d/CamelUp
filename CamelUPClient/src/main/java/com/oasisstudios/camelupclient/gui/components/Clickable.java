package com.oasisstudios.camelupclient.gui.components;

import com.oasisstudios.camelupclient.gui.ColorConverter;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.lang.reflect.Method;

public class Clickable extends StackPane {

    private EventHandler<MouseEvent> clickHandler;
    private String defaultColor;
    private Node content;
    
    public Clickable(Node content){
        this.content = content;
        this.getChildren().add(content);
        initializeBehavior();
    }

    private void initializeBehavior() {
        this.defaultColor = getDefaultColor(content);
        setOnMouseEntered(event -> applyHoverEffect());
        setOnMouseExited(event -> removeHoverEffect());
        setOnMouseClicked(event -> {
            if (clickHandler != null) {
                clickHandler.handle(event);
            }
        });
    }
    
    public void setOnClick(EventHandler<MouseEvent> handler) {
        this.clickHandler = handler;
    }

    private void applyHoverEffect() {
        setStyle(defaultColor +
                "-fx-border-radius: 10; -fx-padding: 0; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, silver, 20, 0, 0, 0);");
    }
    
    private void removeHoverEffect() {
        setStyle(defaultColor + "-fx-border-radius: 10; -fx-padding: 0;");
    }

    public static String getDefaultColor(Node node) {
        try {
            Method getFillMethod = node.getClass().getMethod("getFill");
            Object fill = getFillMethod.invoke(node);
            if (fill instanceof Color) {
                return ColorConverter.ColorToHex((Color) fill);
            }
        } catch (Exception e) {
            // Method not found or invocation failed; return null
            return null;
        }
        return null;
    }
}
