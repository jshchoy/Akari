package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.AkariController;
import com.comp301.a09akari.model.Model;
import com.comp301.a09akari.model.ModelImpl;
import com.comp301.a09akari.model.PuzzleLibrary;
import com.comp301.a09akari.model.PuzzleLibraryFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AppLauncher extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        PuzzleLibrary puzzleLibrary = PuzzleLibraryFactory.ofDefault();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui.fxml"));
        Model model = new ModelImpl(puzzleLibrary);
        AkariController akariController = new AkariController(model);
        model.addObserver(akariController);
        fxmlLoader.setController(akariController);
        Scene scene = new Scene(fxmlLoader.load());

        stage.setScene(scene);
        stage.setTitle("Akari Game");
        stage.show();
    }
}
