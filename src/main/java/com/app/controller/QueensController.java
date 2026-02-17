package com.app.controller;

import java.io.File;
import java.io.IOException;

import com.app.model.IOfiles;
import com.app.model.QueensBrute;
import com.app.model.QueensOptimation;
import com.app.view.QueensView;

import javafx.concurrent.Task;
import javafx.stage.FileChooser;

public class QueensController {
    public QueensBrute bruteModel;
    public QueensOptimation optimationModel;
    public QueensView view;
    public File currentFile;
    public char[][] currentBoard;
    
    public QueensController(QueensBrute bruteModel, QueensOptimation optimationModel, QueensView view) {
        this.bruteModel = bruteModel;
        this.optimationModel = optimationModel;
        this.view = view;
        
        initEventHandlers();
    }

    public void initEventHandlers() {
        view.getHomeButton().setOnAction(e -> handleGoHome());
        view.getLoadButton().setOnAction(e -> handleLoadFile());
        view.getSolveButton().setOnAction(e -> handleSolve());
        view.getSaveButton().setOnAction(e -> handleSaveFile());
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
                view.getBoardDisplay().setText(IOfiles.getInitialBoardString(board, size));
                view.getResultDisplay().setText("");
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
        String input = view.getBoardDisplay().getText();
        if (input == null || input.trim().isEmpty()) {
            view.showError("No Board", "Please load a file or type a board first!");
            return;
        }
        try {
            char[][] board = IOfiles.parseBoard(input);
            currentBoard = board;
            
            int size = board.length;
            view.getBoardDisplay().setText(IOfiles.getInitialBoardString(board, size));
        } catch (IllegalArgumentException ex) {
            view.showError("Invalid Board", ex.getMessage());
            return;
        }
        boolean useOptimation = view.getOptimationCheckBox().isSelected();
        Object activeModel;
        if (useOptimation) {
            optimationModel.setBoard(currentBoard);
            activeModel = optimationModel;
        } else {
            bruteModel.setBoard(currentBoard);
            activeModel = bruteModel;
        }
        view.getSolveButton().setDisable(true);
        view.getLoadButton().setDisable(true);
        view.getSaveButton().setDisable(true);
        view.getVisualizeCheckBox().setDisable(true);
        view.getOptimationCheckBox().setDisable(true);
        view.getResultDisplay().setText("Solving...\n\nPlease wait...");
        view.getProgressBar().setVisible(true);
        view.getProgressBar().setProgress(-1);
        boolean visualize = view.getVisualizeCheckBox().isSelected();
        int sliderValue = (int) view.getVisualizationSlider().getValue();
        long vizInterval = sliderController(sliderValue);
        if (useOptimation) {
            optimationModel.setVisualize(visualize);
            optimationModel.setVisualizationInterval(vizInterval);
            if (visualize) {
                view.getResultDisplay().textProperty().bind(optimationModel.currentStateProperty());
            } else {
                view.getResultDisplay().setText("Solving with Optimation...\n\nVisualization disabled.");
            }
        } else {
            bruteModel.setVisualize(visualize);
            bruteModel.setVisualizationInterval(vizInterval);
            if (visualize) {
                view.getResultDisplay().textProperty().bind(bruteModel.currentStateProperty());
            } else {
                view.getResultDisplay().setText("Solving...\n\nVisualization disabled.");
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
                view.getResultDisplay().textProperty().unbind();
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
                view.getResultDisplay().setText(IOfiles.getSolutionString(currentBoard, solution, currentBoard.length));
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
                
                view.getResultDisplay().setText("No solution found!");
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

    public void handleSaveFile() {
        boolean useOptimation = view.getOptimationCheckBox().isSelected();
        boolean[][] solution = useOptimation ? optimationModel.getSolution() : bruteModel.getSolution();
        
        if (solution == null) {
            view.showWarning("No Solution", "No solution to save!");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Solution");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        if (currentFile != null) {
            String baseName = currentFile.getName().replaceFirst("[.][^.]+$", "");
            fileChooser.setInitialFileName(baseName + "_solution.txt");
        } else {
            fileChooser.setInitialFileName("solution.txt");
        }
        File file = fileChooser.showSaveDialog(view.getPrimaryStage());
        if (file != null) {
            try {
                String solutionStr = IOfiles.getSolutionString(currentBoard, solution, currentBoard.length);
                IOfiles.saveFile(file.getAbsolutePath(), solutionStr);
                
                view.showInfo("Success", "Solution saved to:\n" + file.getAbsolutePath());
                
            } catch (IOException ex) {
                view.showError("Error", "Failed to save file:\n" + ex.getMessage());
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
