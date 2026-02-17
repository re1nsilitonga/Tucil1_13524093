package com.app;

import com.app.view.QueensMain;

import javafx.application.Application;
import javafx.stage.Stage;

public class QueensSolverApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        new QueensMain(primaryStage);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}