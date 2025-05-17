package com.oasisstudios.camelupadmin.gui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CircularIconWidget extends StackPane {
    
    private Circle roleCircle;
    private ImageView roleIcon;
    private Label roleLabel;

    public CircularIconWidget(Label label, Image iconImage, double radius, String circleColor) {
        setStyle(circleColor + "-fx-border-radius: 10; -fx-padding: 10;");

        roleCircle = new Circle(radius);
        roleCircle.setFill(Color.web(circleColor));

        roleIcon = new ImageView(iconImage);
        roleIcon.setPreserveRatio(true);
        roleIcon.setFitWidth(radius * 1.3);
        roleIcon.setFitHeight(radius * 1.3);

        Circle clipCircle = new Circle(radius);
        clipCircle.setCenterX(radius * 0.8);
        clipCircle.setCenterY(radius * 0.8);
        roleIcon.setClip(clipCircle);

        roleLabel = label;

        this.getChildren().addAll(roleCircle, roleIcon, roleLabel);
        this.setAlignment(Pos.CENTER);

    }

    public void setLabelText(String text) {
        roleLabel.setText(text);
    }

    public void setCircleColor(String color) {
        roleCircle.setFill(Color.web(color));
    }

    public void setImage(String imagePath) {
        roleIcon.setImage(new Image(imagePath));
    }
}
