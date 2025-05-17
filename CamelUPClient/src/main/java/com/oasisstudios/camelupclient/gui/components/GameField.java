package com.oasisstudios.camelupclient.gui.components;

import com.oasisstudios.camelupclient.model.CamelModel;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameField extends ScrollPane {
    public final GridPane gridPane;
    public final Map<Integer, StackPane> fieldStacks = new HashMap<>();
    private final int cellSize = 150;
    private double zoomLevel = 1.0;

    public GameField(int fieldSize) {
        gridPane = new GridPane();
        createGameField(fieldSize + 1);

        setContent(gridPane);
        setFitToWidth(true);
        setFitToHeight(true);
        setStyle("-fx-background: #F2ECE4; -fx-background-color: #F2ECE4;");

        setupZooming();
    }

    private void createGameField(int fieldSize) {
        gridPane.getChildren().clear();
        fieldStacks.clear();

        int rows = (int) Math.ceil(Math.sqrt(fieldSize));
        int cols = (int) Math.ceil((double) fieldSize / rows);

        while (2 * rows + 2 * cols - 4 < fieldSize) {
            if (cols <= rows) {
                cols++;
            } else {
                rows++;
            }
        }

        int totalCells = 2 * rows + 2 * cols - 4;
        int fieldCount = 0;

        for (int col = 0; col < cols && fieldCount < totalCells; col++) {
            addCell(0, col, fieldCount++, fieldSize);
        }

        for (int row = 1; row < rows && fieldCount < totalCells; row++) {
            addCell(row, cols - 1, fieldCount++, fieldSize);
        }

        for (int col = cols - 2; col >= 0 && fieldCount < totalCells; col--) {
            addCell(rows - 1, col, fieldCount++, fieldSize);
        }

        for (int row = rows - 2; row > 0 && fieldCount < totalCells; row--) {
            addCell(row, 0, fieldCount++, fieldSize);
        }

        gridPane.setAlignment(Pos.CENTER);
        applyZoom();
    }

    private void addCell(int row, int col, int index, int fieldSize) {
        StackPane fieldStack = new StackPane();
        Rectangle cell = new Rectangle(cellSize, cellSize);

        if (index < fieldSize) {
            if (index == 0) {
                cell.setFill(Color.web("#FBB948")); // Starting field (orange)
            } else if (index == fieldSize - 1) {
                cell.setFill(Color.web("#A18C8B")); // goal (grau)
            } else {
                cell.setFill(Color.web("#F5EEE4")); // normal fields (beige)
            }
        } else {
            cell.setFill(Color.web("#A18C8B")); // additional fields (grau)
        }

        cell.setStroke(Color.BLACK);
        fieldStack.getChildren().add(cell);
        fieldStacks.put(index, fieldStack);
        gridPane.add(fieldStack, col, row);
    }

    public void updateStackOnField(int position, List<CamelModel> orderedCamels) {
        StackPane targetStack = fieldStacks.get(position);

        if (targetStack == null) {
            throw new IllegalArgumentException("Invalid position: " + position);
        }

        Rectangle background = (Rectangle) targetStack.getChildren().get(0);
        targetStack.getChildren().clear();
        targetStack.getChildren().add(background);

        double offsetY = 0;

        // show "+" in case of more than 3 camels stacking on one field
        if (orderedCamels.size() > 3) {
            Label plusLabel = new Label("+");
            plusLabel.setStyle("-fx-font-size: 30; -fx-text-fill: black; -fx-font-weight: bold;");
            StackPane.setAlignment(plusLabel, Pos.TOP_RIGHT);
            StackPane.setMargin(plusLabel, new javafx.geometry.Insets(10, 10, 0, 0));
            targetStack.getChildren().add(plusLabel);

            targetStack.setOnMouseEntered(event -> showHoverDisplay(targetStack, orderedCamels));
            targetStack.setOnMouseExited(event -> hideHoverDisplay(targetStack));
        } else {
            targetStack.setOnMouseEntered(null);
            targetStack.setOnMouseExited(null);
        }

        int startIndex = Math.max(0, orderedCamels.size() - 3);
        for (int i = startIndex; i < orderedCamels.size(); i++) {
            CamelModel camel = orderedCamels.get(i);
            StackPane camelWithId = camel.getCamelWithId();
            camelWithId.setTranslateY(-offsetY);
            targetStack.getChildren().add(camelWithId);
            StackPane.setAlignment(camelWithId, Pos.BOTTOM_CENTER);

            offsetY += 20;
        }
    }

    private void showHoverDisplay(StackPane targetStack, List<CamelModel> orderedCamels) {
        ScrollPane hoverScrollPane = new ScrollPane();
        VBox contentBox = new VBox();
        contentBox.setStyle("-fx-background-color: #F5EEE4; -fx-padding: 10; -fx-border-color: #000000;");
        contentBox.setAlignment(Pos.CENTER);

        for (int i = orderedCamels.size() - 1; i >= 0; i--) {
            CamelModel camel = orderedCamels.get(i);
            Label camelLabel = new Label("Kamel " + camel.getId());
            camelLabel.setStyle("-fx-font-size: 14; -fx-text-fill: black;");
            contentBox.getChildren().add(camelLabel);
        }

        hoverScrollPane.setContent(contentBox);
        hoverScrollPane.setFitToWidth(true);
        hoverScrollPane.setMaxHeight(cellSize);
        hoverScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        targetStack.getChildren().add(hoverScrollPane);
        StackPane.setAlignment(hoverScrollPane, Pos.TOP_CENTER);
    }

    private void hideHoverDisplay(StackPane targetStack) {
        targetStack.getChildren().removeIf(node -> node instanceof ScrollPane);
    }

    private void setupZooming() {
        this.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.isControlDown()) {
                double delta = event.getDeltaY();
                if (delta > 0) {
                    zoomLevel *= 1.1;
                } else {
                    zoomLevel /= 1.1;
                }
                applyZoom();
                event.consume();
            }
        });
    }

    private void applyZoom() {
        gridPane.setScaleX(zoomLevel);
        gridPane.setScaleY(zoomLevel);
    }

    public void clearGameField() {
        for (StackPane stack : fieldStacks.values()) {
            if (!stack.getChildren().isEmpty() && stack.getChildren().get(0) instanceof Rectangle) {
                Rectangle background = (Rectangle) stack.getChildren().get(0);
                stack.getChildren().clear();
                stack.getChildren().add(background);
            } else {
                stack.getChildren().clear();
            }
        }
    }
}
