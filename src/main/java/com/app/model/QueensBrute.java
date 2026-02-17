package com.app.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class QueensBrute {
    public char[][] board;
    public int size;
    public ColorMap colorPosition;
    public long iterationCount;
    public long startTime;
    public boolean[][] solution;
    public boolean visualize;
    public long visualizationInterval;
    
    public StringProperty currentStateProperty;

    public QueensBrute() {
        this.colorPosition = new ColorMap();
        this.iterationCount = 0;
        this.visualize = false;
        this.visualizationInterval = 1;
        this.solution = null;
        this.currentStateProperty = new SimpleStringProperty("");
    }
    
    public void setBoard(char[][] board) {
        this.size = board.length;
        this.board = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.board[i][j] = board[i][j];
            }
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
        
        int totalCells = size * size;
        int[] comb = new int[size];
        for (int i = 0; i < size; i++) {
            comb[i] = i;
        }
        while (true) {
            iterationCount++;
            boolean[][] queens = new boolean[size][size];
            for (int i = 0; i < size; i++) {
                int pos = comb[i];
                int row = pos / size;
                int col = pos % size;
                queens[row][col] = true;
            }
            
            // buat visualisasi
            if (visualize && iterationCount % visualizationInterval == 0) {
                String boardStr = IOfiles.getBoardString(board, queens, size);
                String message = "Iteration " + iterationCount + "\n\n" + boardStr;
                javafx.application.Platform.runLater(() -> {
                    currentStateProperty.set(message);
                });
                try {
                    Thread.sleep(50); // 50ms delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (isValidSolution(queens)) {
                solution = copyBoard(queens);
                return true; // solusi ketemu langsung break
            }
            
            // generate kombinasi berikutnya
            if (!bruteCombination(comb, totalCells, size)) {
                break;
            }
        }
        return false; // tidak ada solusi
    }

    public boolean bruteCombination(int[] comb, int n, int k) {
        int i = k - 1;
        while (i >= 0 && comb[i] == n - k + i) {
            i--;
        }
        if (i < 0) {
            return false;
        }
        
        comb[i]++;
        for (int j = i + 1; j < k; j++) {
            comb[j] = comb[j - 1] + 1;
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
        int totalQueens = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (queens[i][j]) totalQueens++;
            }
        }
        if (totalQueens != size) return false;
        
        // check row
        for (int i = 0; i < size; i++) {
            int count = 0;
            for (int j = 0; j < size; j++) {
                if (queens[i][j]) count++;
            }
            if (count != 1) return false;
        }
        
        // check column
        for (int j = 0; j < size; j++) {
            int count = 0;
            for (int i = 0; i < size; i++) {
                if (queens[i][j]) count++;
            }
            if (count != 1) return false;
        }
        
        // check color
        char[] colors = colorPosition.keySet();
        for (int c = 0; c < colors.length; c++) {
            char color = colors[c];
            PositionList positions = colorPosition.get(color);
            int count = 0;
            for (int p = 0; p < positions.size(); p++) {
                int[] pos = positions.get(p);
                if (queens[pos[0]][pos[1]]) count++;
            }
            if (count != 1) return false;
        }
        
        // check neighbor
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (queens[i][j]) {
                    for (int dr = -1; dr <= 1; dr++) {
                        for (int dc = -1; dc <= 1; dc++) {
                            if (dr == 0 && dc == 0) continue;
                            int nr = i + dr;
                            int nc = j + dc;
                            if (nr >= 0 && nr < size && nc >= 0 && nc < size) {
                                if (queens[nr][nc]) {
                                    return false;
                                }
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
