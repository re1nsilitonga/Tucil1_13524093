package com.app.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageProcessor {
    
    private static Image crownImage;
    
    static {
        try {
            InputStream is = ImageProcessor.class.getResourceAsStream("crown.png");
            if (is != null) {
                crownImage = new Image(is);
            } else {
                System.err.println("Warning: crown.png not found in util package");
            }
        } catch (Exception e) {
            System.err.println("Error loading crown.png: " + e.getMessage());
        }
    }
    
    private static final Color[] LETTER_COLORS = {
        Color.web("#FF6B6B"),
        Color.web("#4ECDC4"),
        Color.web("#45B7D1"),
        Color.web("#FFA07A"),
        Color.web("#98D8C8"),
        Color.web("#F7DC6F"),
        Color.web("#BB8FCE"),
        Color.web("#85C1E2"),
        Color.web("#F8B88B"),
        Color.web("#FAD7A0"),
        Color.web("#A9DFBF"),
        Color.web("#F9E79F"),
        Color.web("#D7BDE2"),
        Color.web("#A3E4D7"),
        Color.web("#FAD7A0"),
        Color.web("#E8DAEF"),
        Color.web("#D5F4E6"),
        Color.web("#FADBD8"),
        Color.web("#D6EAF8"),
        Color.web("#FCF3CF"),
        Color.web("#EBDEF0"),
        Color.web("#D1F2EB"),
        Color.web("#F5EEF8"),
        Color.web("#FEF9E7"),
        Color.web("#F4ECF7"),
        Color.web("#E8F8F5")
    };
    
    private static Color getColorForLetter(char letter) {
        if (letter >= 'A' && letter <= 'Z') {
            return LETTER_COLORS[letter - 'A'];
        }
        return Color.WHITE;
    }
    
    public static WritableImage boardToImage(char[][] board) {
        return boardToImage(board, null);
    }
    
    public static WritableImage boardToImage(char[][] board, boolean[][] queens) {
        if (board == null || board.length == 0) return null;
        
        int size = board.length;
        int cellSize = 100;
        int imageSize = size * cellSize;
        
        Canvas canvas = new Canvas(imageSize, imageSize);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                char letter = board[row][col];
                
                gc.setFill(getColorForLetter(letter));
                gc.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(2);
                gc.strokeRect(col * cellSize, row * cellSize, cellSize, cellSize);
                
                if (queens != null && queens[row][col]) {
                    drawCrown(gc, col * cellSize, row * cellSize, cellSize);
                }
            }
        }
        
        WritableImage image = new WritableImage(imageSize, imageSize);
        canvas.snapshot(null, image);
        return image;
    }
    
    private static void drawCrown(GraphicsContext gc, double x, double y, double cellSize) {
        if (crownImage == null) {
            gc.setFill(Color.DARKRED);
            gc.setFont(new javafx.scene.text.Font(cellSize * 0.6));
            gc.fillText("👑", x + cellSize/4, y + cellSize * 0.75);
            return;
        }
        
        double crownSize = cellSize;
        double crownX = x;
        double crownY = y;
        
        gc.drawImage(crownImage, crownX, crownY, crownSize, crownSize);
    }
    
    public static void saveImage(WritableImage image, String filename) throws IOException {
        File file = new File(filename);
        
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        PixelReader pixelReader = image.getPixelReader();
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = pixelReader.getArgb(x, y);
                bufferedImage.setRGB(x, y, argb);
            }
        }
        
        ImageIO.write(bufferedImage, "png", file);
    }
}
