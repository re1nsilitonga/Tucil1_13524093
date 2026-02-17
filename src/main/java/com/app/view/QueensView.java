package com.app.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class QueensView {
    public Stage primaryStage;
    
    @FXML public Button homeButton;
    @FXML public Button loadButton;
    @FXML public Button solveButton;
    @FXML public Button saveButton;
    @FXML public CheckBox visualizeCheckBox;
    @FXML public CheckBox visualizeCheckBox1;
    @FXML public Slider visualizationSlider;
    @FXML public Label visualizationLabel;
    @FXML public TextArea boardDisplay;
    @FXML public TextArea resultDisplay;
    @FXML public Label statsLabel;
    @FXML public ProgressBar progressBar;
    @FXML public VBox root;
    
    public QueensView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initUI();
    }
    
    public void initUI() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("QueensView.fxml"));
            loader.setController(this);
            VBox root = loader.load();
            root.prefWidthProperty().bind(primaryStage.widthProperty());
            root.prefHeightProperty().bind(primaryStage.heightProperty());
            
            visualizationSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                int level = newVal.intValue();
                String text = switch(level) {
                    case 0 -> "Every 1 iteration (very slow)";
                    case 1 -> "Every 10 iterations";
                    case 2 -> "Every 100 iterations";
                    case 3 -> "Every 1,000 iterations";
                    case 4 -> "Every 10,000 iterations";
                    default -> "Every 100 iterations";
                };
                visualizationLabel.setText(text);
            });
            
            boolean wasMaximized = primaryStage.isMaximized();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Queens Puzzle Solver - Text Mode");
            primaryStage.setMaximized(wasMaximized);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load FXML: " + e.getMessage());
        }
    }

    public Button getHomeButton() {
        return homeButton;
    }
    public Button getLoadButton() {
        return loadButton;
    }
    public Button getSolveButton() {
        return solveButton;
    }
    public Button getSaveButton() {
        return saveButton;
    }
    public CheckBox getVisualizeCheckBox() {
        return visualizeCheckBox;
    }
    public CheckBox getOptimationCheckBox() {
        return visualizeCheckBox1;
    }
    public Slider getVisualizationSlider() {
        return visualizationSlider;
    }
    public TextArea getBoardDisplay() {
        return boardDisplay;
    }
    public TextArea getResultDisplay() {
        return resultDisplay;
    }
    public Label getStatsLabel() {
        return statsLabel;
    }
    public ProgressBar getProgressBar() {
        return progressBar;
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
