package com.oasisstudios.camelupadmin.gui.views;

import com.oasisstudios.camelupadmin.gui.components.BaseLayout;
import com.oasisstudios.camelupadmin.gui.components.Footer;

import com.oasisstudios.camelupadmin.service.UserType;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;


@Getter
public abstract class AbstractSceneBasedView {
    Stage primaryStage;
    private Scene scene = null;
    private Pane layout = null;
    protected String stageTitle = null;
    protected UserType userType;
    private final Footer footer = Footer.getInstance();
    
    public void setLayout(Pane pane) {
        layout = new BaseLayout(pane, footer);
        var isViewMaxed = this.getPrimaryStage().isMaximized();
        if (isViewMaxed) {
            ConfigureMaxedStage(layout);
            scene  = new Scene(layout);
        }else {
            scene = new Scene(layout, 1280, 720);        
        }
    }

    public void ConfigureMaxedStage(Pane layout) {
        this.layout = layout;
        this.layout.prefWidthProperty().bind(this.getPrimaryStage().widthProperty());
        this.layout.prefHeightProperty().bind(this.getPrimaryStage().heightProperty());
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(screenBounds.getMinX());
        primaryStage.setY(screenBounds.getMinY());
        primaryStage.setWidth(screenBounds.getWidth());
        primaryStage.setHeight(screenBounds.getHeight());
    }

    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }
}
