package com.app.view;

import java.io.IOException;

import com.app.controller.QueensController;
import com.app.controller.QueensImageController;
import com.app.model.QueensBrute;
import com.app.model.QueensOptimation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class QueensMain {
    public Stage primaryStage;
    
    @FXML public Button textInputButton;
    @FXML public Button imageInputButton;
    @FXML public VBox root;
    
    public QueensMain(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initUI();
    }
    
    public void initUI() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("QueensMain.fxml"));
            loader.setController(this);
            VBox root = loader.load();
            root.prefWidthProperty().bind(primaryStage.widthProperty());
            root.prefHeightProperty().bind(primaryStage.heightProperty());
            textInputButton.setOnAction(e -> openTextMode());
            imageInputButton.setOnAction(e -> openImageMode());
            boolean wasMaximized = primaryStage.isMaximized();
            boolean isFirstTime = !primaryStage.isShowing();
            
            javafx.scene.Scene scene;
            if (isFirstTime) {
                scene = new javafx.scene.Scene(root, 900, 600);
            } else {
                scene = new javafx.scene.Scene(root);
            }
            
            primaryStage.setScene(scene);
            primaryStage.setTitle("Queens Puzzle Solver");
            
            primaryStage.setMaximized(wasMaximized);
            
            if (isFirstTime) {
                primaryStage.show();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load FXML: " + e.getMessage());
        }
    }
    
    private void openTextMode() {
        QueensBrute bruteModel = new QueensBrute();
        QueensOptimation optimationModel = new QueensOptimation();
        QueensView view = new QueensView(primaryStage);
        new QueensController(bruteModel, optimationModel, view);
    }
    
    private void openImageMode() {
        QueensBrute bruteModel = new QueensBrute();
        QueensOptimation optimationModel = new QueensOptimation();
        QueensImage view = new QueensImage(primaryStage);
        new QueensImageController(bruteModel, optimationModel, view);
    }
}
