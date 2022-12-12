package com.comp301.a09akari.controller;

import com.comp301.a09akari.model.Model;
import com.comp301.a09akari.model.ModelObserver;
import com.comp301.a09akari.view.AkariGrid;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AkariController implements Initializable, ModelObserver {

    private Model model;

    @FXML
    private Label puzzleIndexLabel;

    @FXML
    private Button nextButton;

    @FXML
    private Button backButton;

    @FXML
    private Button resetButton;

    @FXML
    private BorderPane root;

    @FXML
    private Label winLabel;

    public AkariController(Model model) {
        this.model = model;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.setCenter(new AkariGrid(model));
        root.getChildren().forEach(System.out::println);
        puzzleIndexLabel.setText("Puzzle 1 / " + model.getPuzzleLibrarySize());
        initEventHandlers();
    }

    private void initEventHandlers() {
        this.nextButton.setOnMouseClicked(ignored -> tryIncrement());
        this.backButton.setOnMouseClicked(ignored -> tryDecrement());
        this.resetButton.setOnMouseClicked(ignored -> tryReset());
    }



    private void tryIncrement() {
        System.out.println(model.getActivePuzzleIndex());
        System.out.println(model.getPuzzleLibrarySize());
        if (model.getActivePuzzleIndex() + 1 < model.getPuzzleLibrarySize()) {
            model.setActivePuzzleIndex(model.getActivePuzzleIndex() + 1);
        }
    }

    private void tryDecrement() {
        if (model.getActivePuzzleIndex() != 0) {
            model.setActivePuzzleIndex(model.getActivePuzzleIndex() - 1);
        }
    }

    private void tryReset() {
        model.resetPuzzle();
    }

    @Override
    public void update(Model model) {
        updateLabel();
        updateGraph();
        checkWin();
    }

    private void checkWin() {
        if (model.isSolved()) {
            winLabel.setText("You won!!!");
        }
        else {
            winLabel.setText("");
        }
    }

    private void updateLabel() {
        puzzleIndexLabel.setText("Puzzle " + (model.getActivePuzzleIndex()+1) + " / " + model.getPuzzleLibrarySize());
    }

    private void updateGraph() {
        root.setCenter(new AkariGrid(model));
    }
}
