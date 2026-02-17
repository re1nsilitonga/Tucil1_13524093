package com.app.controller;

import com.app.model.QueensBrute;
import com.app.model.QueensOptimation;
import com.app.model.IOfiles;
import com.app.model.ImageProcessor;
import com.app.view.QueensImage;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;

public class QueensImageController {
    public QueensBrute bruteModel;
    public QueensOptimation optimationModel;
    public QueensImage view;
    public File currentFile;
    public char[][] currentBoard;
    public WritableImage inputImage;
    public WritableImage outputImage;
    
    public QueensImageController(QueensBrute bruteModel, QueensOptimation optimationModel, QueensImage view) {
        this.bruteModel = bruteModel;
        this.optimationModel = optimationModel;
        this.view = view;
        
        initEventHandlers();
    }

    public void initEventHandlers() {
        view.getHomeButton().setOnAction(e -> handleGoHome());
        view.getLoadButton().setOnAction(e -> handleLoadFile());
        view.getSolveButton().setOnAction(e -> handleSolve());
        view.getSaveButton().setOnAction(e -> handleSaveImage());
    }
    
    public void handleGoHome() {
        new com.app.view.QueensMain(view.getPrimaryStage());
    }
    
    public void handleLoadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Board File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        File file = fileChooser.showOpenDialog(view.getPrimaryStage());
        if (file != null) {
            try {
                char[][] board = IOfiles.loadTxt(file.getAbsolutePath());
                currentBoard = board;
                currentFile = file;
                int size = board.length;
                inputImage = ImageProcessor.boardToImage(board);
                view.getInputImageView().setImage(inputImage);
                view.getOutputImageView().setImage(null);
                view.getStatsLabel().setText("");
                view.getSolveButton().setDisable(false);
                view.getSaveButton().setDisable(true);
                view.showInfo("Success", "Board loaded successfully!\nSize: " + size + "×" + size);
            } catch (IOException ex) {
                view.showError("Error", "Failed to load file:\n" + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                view.showError("Invalid Input", ex.getMessage());
            }
        }
    }
    
    public void handleSolve() {
        if (currentBoard == null) {
            view.showError("No Board", "Please load an image first!");
            return;
        }
        boolean useOptimation = view.getOptimationCheckBox().isSelected();
        if (useOptimation) {
            optimationModel.setBoard(currentBoard);
        } else {
            bruteModel.setBoard(currentBoard);
        }
        view.getSolveButton().setDisable(true);
        view.getLoadButton().setDisable(true);
        view.getSaveButton().setDisable(true);
        view.getVisualizeCheckBox().setDisable(true);
        view.getOptimationCheckBox().setDisable(true);
        view.getSolutionTextDisplay().setVisible(true);
        view.getSolutionTextDisplay().setManaged(true);
        view.getOutputImageView().setVisible(false);
        view.getOutputImageView().setManaged(false);
        view.getSolutionTextDisplay().setText("Solving...\n\nPlease wait...");
        view.getProgressBar().setVisible(true);
        view.getProgressBar().setProgress(-1);
        boolean visualize = view.getVisualizeCheckBox().isSelected();
        int sliderValue = (int) view.getVisualizationSlider().getValue();
        long vizInterval = sliderController(sliderValue);
        if (useOptimation) {
            optimationModel.setVisualize(visualize);
            optimationModel.setVisualizationInterval(vizInterval);
            if (visualize) {
                view.getSolutionTextDisplay().textProperty().bind(optimationModel.currentStateProperty());
            } else {
                view.getSolutionTextDisplay().setText("Solving with Optimation...\n\nVisualization disabled.");
            }
        } else {
            bruteModel.setVisualize(visualize);
            bruteModel.setVisualizationInterval(vizInterval);
            if (visualize) {
                view.getSolutionTextDisplay().textProperty().bind(bruteModel.currentStateProperty());
            } else {
                view.getSolutionTextDisplay().setText("Solving...\n\nVisualization disabled.");
            }
        }
        Task<Boolean> solveTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                if (useOptimation) {
                    return optimationModel.solve();
                } else {
                    return bruteModel.solve();
                }
            }
        };
        solveTask.setOnSucceeded(event -> {
            boolean solved = solveTask.getValue();
            if (visualize) {
                view.getSolutionTextDisplay().textProperty().unbind();
            }
            view.getProgressBar().setVisible(false);
            view.getSolveButton().setDisable(false);
            view.getLoadButton().setDisable(false);
            view.getVisualizeCheckBox().setDisable(false);
            view.getOptimationCheckBox().setDisable(false);
            if (solved) {
                boolean[][] solution;
                long execTime;
                long iterations;
                if (useOptimation) {
                    solution = optimationModel.getSolution();
                    execTime = optimationModel.getExecutionTime();
                    iterations = optimationModel.getIterationCount();
                } else {
                    solution = bruteModel.getSolution();
                    execTime = bruteModel.getExecutionTime();
                    iterations = bruteModel.getIterationCount();
                }
                outputImage = ImageProcessor.boardToImage(currentBoard, solution);
                view.getSolutionTextDisplay().setVisible(false);
                view.getSolutionTextDisplay().setManaged(false);
                view.getOutputImageView().setVisible(true);
                view.getOutputImageView().setManaged(true);
                view.getOutputImageView().setImage(outputImage);
                String algorithm = useOptimation ? "Optimized Brute Force" : "Brute Force";
                view.getStatsLabel().setText(
                    "Time execution: " + execTime + " ms\n" +
                    "Iteration: " + iterations + " cases"
                );
                
                view.getSaveButton().setDisable(false);
                view.showInfo("Success", "Solution found! \n\nAlgorithm: " + algorithm + "\nTime: " + execTime + " ms");
            } else {
                long execTime = useOptimation ? optimationModel.getExecutionTime() : bruteModel.getExecutionTime();
                long iterations = useOptimation ? optimationModel.getIterationCount() : bruteModel.getIterationCount();
                String algorithm = useOptimation ? "Optimized Brute Force" : "Brute Force";
                view.getSolutionTextDisplay().setVisible(true);
                view.getSolutionTextDisplay().setManaged(true);
                view.getOutputImageView().setVisible(false);
                view.getOutputImageView().setManaged(false);
                view.getSolutionTextDisplay().setText("No solution found!");
                view.getStatsLabel().setText(
                    "No solution found\n"
                );
                view.showWarning("No Solution", "No valid solution exists for this board.");
            }
        });
        solveTask.setOnFailed(event -> {
            view.getProgressBar().setVisible(false);
            view.getSolveButton().setDisable(false);
            view.getLoadButton().setDisable(false);
            view.getVisualizeCheckBox().setDisable(false);
            view.getOptimationCheckBox().setDisable(false);
            Throwable ex = solveTask.getException();
            view.showError("Error", "Error during solving:\n" + ex.getMessage());
        });
        Thread thread = new Thread(solveTask);
        thread.setDaemon(true);
        thread.start();
    }

    public void handleSaveImage() {
        if (outputImage == null) {
            view.showWarning("No Solution", "No solution to save!");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Solution Image");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PNG Image", "*.png")
        );
        if (currentFile != null) {
            String baseName = currentFile.getName().replaceFirst("[.][^.]+$", "");
            fileChooser.setInitialFileName(baseName + "_solution.png");
        } else {
            fileChooser.setInitialFileName("solution.png");
        }
        File file = fileChooser.showSaveDialog(view.getPrimaryStage());
        if (file != null) {
            try {
                ImageProcessor.saveImage(outputImage, file.getAbsolutePath());
                view.showInfo("Success", "Solution image saved to:\n" + file.getAbsolutePath());
            } catch (IOException ex) {
                view.showError("Error", "Failed to save image:\n" + ex.getMessage());
            }
        }
    }
    
    public long sliderController(int level) {
        return switch(level) {
            case 0 -> 1L;
            case 1 -> 10L;
            case 2 -> 100L;
            case 3 -> 1000L;
            case 4 -> 10000L;
            default -> 100L;
        };
    }
}
