package com.app.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class QueensOptimation {
    public char[][] board;
    public int size;
    public ColorMap colorPosition;
    public long iterationCount;
    public long startTime;
    public boolean[][] solution;
    public boolean visualize;
    public long visualizationInterval;
    
    public StringProperty currentStateProperty;

    public QueensOptimation() {
        this.colorPosition = new ColorMap();
        this.iterationCount = 0;
        this.visualize = false;
        this.visualizationInterval = 1;
        this.solution = null;
        this.currentStateProperty = new SimpleStringProperty("");
    }
    
    public void setBoard(char[][] board) {
        this.size = board.length;
        this.board = java.util.Arrays.copyOf(board, board.length);
        for (int i = 0; i < size; i++) {
            this.board[i] = java.util.Arrays.copyOf(board[i], board[i].length);
        }
        mapColorPosition();
    }
    
    public void mapColorPosition() {
        colorPosition.clear();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                char color = board[i][j];
                PositionList positions = colorPosition.getOrCreate(color);
                positions.add(i, j);
            }
        }
    }
    
    public StringProperty currentStateProperty() {
        return currentStateProperty;
    }
    
    public void setVisualize(boolean visualize) {
        this.visualize = visualize;
    }

    public void setVisualizationInterval(long interval) {
        this.visualizationInterval = interval;
    }
    
    public boolean solve() {
        startTime = System.currentTimeMillis();
        iterationCount = 0;
        solution = null;
        
        int[] perm = new int[size];
        for (int i = 0; i < size; i++) {
            perm[i] = i;
        }
        
        while (true) {
            iterationCount++;
            
            boolean[][] queens = new boolean[size][size];
            for (int row = 0; row < size; row++) {
                int col = perm[row];
                queens[row][col] = true;
            }
            
            if (visualize && iterationCount % visualizationInterval == 0) {
                String boardStr = IOfiles.getBoardString(board, queens, size);
                String message = "Iteration " + iterationCount + "\n\n" + boardStr;
                javafx.application.Platform.runLater(() -> {
                    currentStateProperty.set(message);
                });
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            if (isValidSolution(queens)) {
                solution = copyBoard(queens);
                return true;
            }
            
            if (!nextPermutation(perm)) {
                break;
            }
        }
        
        return false;
    }
    
    public boolean nextPermutation(int[] perm) {
        int i = perm.length - 2;
        while (i >= 0 && perm[i] >= perm[i + 1]) {
            i--;
        }
        
        if (i < 0) {
            return false;
        }
        
        int j = perm.length - 1;
        while (perm[j] <= perm[i]) {
            j--;
        }
        
        int temp = perm[i];
        perm[i] = perm[j];
        perm[j] = temp;
        
        int left = i + 1;
        int right = perm.length - 1;
        while (left < right) {
            temp = perm[left];
            perm[left] = perm[right];
            perm[right] = temp;
            left++;
            right--;
        }
        
        return true;
    }
    
    public boolean[][] copyBoard(boolean[][] queens) {
        boolean[][] copy = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                copy[i][j] = queens[i][j];
            }
        }
        return copy;
    }
    
    public boolean isValidSolution(boolean[][] queens) {
        char[] colors = colorPosition.keySet();
        for (int c = 0; c < colors.length; c++) {
            char color = colors[c];
            PositionList positions = colorPosition.get(color);
            int count = 0;
            for (int p = 0; p < positions.size(); p++) {
                int[] pos = positions.get(p);
                if (queens[pos[0]][pos[1]]) count++;
            }
            if (count > 1) return false;
        }
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (queens[i][j]) {
                    int[] dx = {-1, 1, 0, 0};
                    int[] dy = {0, 0, -1, 1};
                    for (int d = 0; d < 4; d++) {
                        int nr = i + dx[d];
                        int nc = j + dy[d];
                        if (nr >= 0 && nr < size && nc >= 0 && nc < size) {
                            if (queens[nr][nc]) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        
        return true;
    }

    public char[][] getBoard() {
        return board;
    }
    
    public int getSize() {
        return size;
    }
    
    public boolean[][] getSolution() {
        return solution;
    }
    
    public long getIterationCount() {
        return iterationCount;
    }
    
    public long getExecutionTime() {
        return System.currentTimeMillis() - startTime;
    }
}
