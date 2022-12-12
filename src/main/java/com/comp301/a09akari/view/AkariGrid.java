package com.comp301.a09akari.view;

import com.comp301.a09akari.model.CellType;
import com.comp301.a09akari.model.Model;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AkariGrid extends GridPane {
    private Model model;

    public AkariGrid(Model model) {

        this.model = model;
        for (int row = 0; row < model.getActivePuzzle().getHeight(); ++row) {
            for (int col = 0; col < model.getActivePuzzle().getWidth(); ++col) {
                this.add(nodeOf(row, col), row, col);
            }
        }
        this.setCenterShape(true);
        this.setAlignment(Pos.CENTER);
        this.setGridLinesVisible(true);
    }

    private Node nodeOf(int row, int col) {
        CellType type = model.getActivePuzzle().getCellType(row, col);
        StackPane sp = new StackPane();
        Rectangle rect = new Rectangle(75, 75);
        sp.getChildren().add(rect);

        rect.setFill(Color.WHITE);
        if (type == CellType.CLUE) {
            Label label = new Label("" + model.getActivePuzzle().getClue(row, col));
            if (model.isClueSatisfied(row, col)) {
                label.setTextFill(Color.GREEN);
            }
            else {
                label.setTextFill(Color.RED);
            }

            label.setFont(Font.font(30));
            sp.getChildren().add(label);
            rect.setFill(Color.BLACK);
        }
        else if (type == CellType.CORRIDOR) {
            if (model.isLamp(row, col)) {
                rect.setFill(Color.ORANGE);
                rect.setOnMouseClicked(ignored -> model.removeLamp(row, col));
            }
            else if (model.isLit(row, col)) {
                rect.setFill(Color.YELLOW);
                rect.setOnMouseClicked(ignored -> model.addLamp(row, col));
            }
            else {
                rect.setOnMouseClicked(ignored -> model.addLamp(row, col));
            }

        }
        else if (type == CellType.WALL) {
            rect.setFill(Color.BLACK);
        }

        return sp;
    }
}
