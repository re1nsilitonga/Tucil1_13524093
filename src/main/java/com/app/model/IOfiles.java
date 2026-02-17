package com.app.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IOfiles {
    
    public static char[][] parseBoard(String input) throws IllegalArgumentException {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Board kosong!");
        }
        String[] lines = input.trim().split("\\n");
        List<String> validLines = new ArrayList<>();
        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                validLines.add(trimmed);
            }
        }
        if (validLines.isEmpty()) {
            throw new IllegalArgumentException("Board kosong!");
        }
        int size = validLines.size();
        char[][] board = new char[size][size];
        for (int i = 0; i < size; i++) {
            String currentLine = validLines.get(i);
            if (currentLine.length() != size) {
                throw new IllegalArgumentException("Papan harus berbentuk persegi (N x N)!");
            }
            for (int j = 0; j < size; j++) {
                char ch = currentLine.charAt(j);
                if (ch < 'A' || ch > 'Z') {
                    throw new IllegalArgumentException("Hanya boleh karakter A-Z!");
                }
                board[i][j] = ch;
            }
        }
        return board;
    }
    
    public static char[][] loadTxt(String filename) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new FileReader(filename))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = buffer.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    lines.add(trimmed);
                }
            }
            if (lines.isEmpty()) {
                throw new IllegalArgumentException("File kosong!");
            }
            int size = lines.size();
            char[][] board = new char[size][size];
            
            for (int i = 0; i < size; i++) {
                String currentLine = lines.get(i);
                
                if (currentLine.length() != size) {
                    throw new IllegalArgumentException("Papan harus berbentuk persegi (N x N)!");
                }
                for (int j = 0; j < size; j++) {
                    char ch = currentLine.charAt(j);
                    if (ch < 'A' || ch > 'Z') {
                        throw new IllegalArgumentException("Hanya boleh karakter A-Z!");
                    }
                    board[i][j] = ch;
                }
            }
            return board;
        }
    }
    
    public static void saveFile(String filename, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
        }
    }
    
    public static String getBoardString(char[][] board, boolean[][] queens, int size) {
        if (board == null) return "";
        StringBuilder boardString = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (queens != null && queens[i][j]) {
                    boardString.append("#");
                } else {
                    boardString.append(board[i][j]);
                }
            }
            boardString.append("\n");
        }
        return boardString.toString();
    }
    
    public static String getInitialBoardString(char[][] board, int size) {
        return getBoardString(board, null, size);
    }

    public static String getSolutionString(char[][] board, boolean[][] solution, int size) {
        return getBoardString(board, solution, size);
    }
}
