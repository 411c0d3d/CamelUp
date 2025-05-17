package com.oasisstudios.camelupadmin.model;

import com.oasisstudios.camelupadmin.dto.Camel;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class CamelModel {
    private final int id;
    private final String color;
    private Integer position;
    private final StackPane camelWithId; // Kombination aus SVG und ID-Text

    public CamelModel(Camel camelDTO) {
        this.id = camelDTO.getId();
        this.color = camelDTO.getColor();
        this.camelWithId = createCamelWithId(color, id);
    }

    private StackPane createCamelWithId(String colorHex, int id) {
        SVGPath svgPath = createCamelSvg(colorHex);
        Text idText = new Text(String.valueOf(id));

        // Stil für die ID-Zahl
        idText.setFont(Font.font("Arial", 16));
        idText.setFill(Color.BLACK);
        idText.setStroke(Color.WHITE); // Umrandung für bessere Sichtbarkeit
        idText.setStrokeWidth(0.5);

        // StackPane, um SVG und ID übereinander zu legen
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(svgPath, idText);

        return stackPane;
    }

    private SVGPath createCamelSvg(String colorHex) {
        SVGPath svgPath = new SVGPath();
        svgPath.setContent("M60.5546 51.0213C48.7891 50.5305 47.7952 76.335 37.5546 70.5213C31.4795 67.0723 33.426 60.3364 29.5546 54.5213C26.3264 49.6723 22.8706 48.1206 20.0546 43.0213C17.4576 38.3183 20.3871 32.3685 15.5546 30.0213C12.5687 28.571 10.1281 30.2752 7.05461 29.0213C2.49159 27.1595 -0.441909 24.4244 0.0546086 19.5213C0.487856 15.2429 3.60952 13.5948 7.05461 11.0213C12.2547 7.13672 17.7674 12.7926 21.5547 7.52126C23.3561 5.01393 19.5265 0.419815 22.5547 1.02126C30 2.5 32 11.0001 32.5 13.0001C33.625 17.5001 35 20.5791 38 22.0001C47.5 26.5001 49 2.00014 60.5 2.00014C70 2.00014 71.5 21 79 21C91.7482 21 88.5 2.00014 100.055 5.02127C107.176 6.88323 106.403 15.1896 107.055 22.5213C107.785 30.7293 105.834 35.6988 102.055 43.0213C99.493 47.9838 96.9469 50.085 93.5546 54.5213C88.9978 60.4804 88.7652 67.4515 81.5546 69.5213C71.0494 72.5367 71.4746 51.4767 60.5546 51.0213Z");
        svgPath.setFill(Color.web(colorHex));
        svgPath.setScaleX(-0.5);
        svgPath.setScaleY(0.5);
        svgPath.setStroke(Color.BLACK);
        svgPath.setStrokeWidth(2);
        return svgPath;
    }

    public StackPane getCamelWithId() {
        return camelWithId;
    }

    public int getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
