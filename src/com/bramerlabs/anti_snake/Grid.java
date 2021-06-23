package com.bramerlabs.anti_snake;

import java.awt.*;

public class Grid {

    public static final int gridWidth = 16;
    public static final int gridHeight = 12;

    public static int gridSize = 50;

    /**
     * paints the grid
     * @param g - the graphics component handed down by the paint() call
     */
    public static void paint(Graphics g) {
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                g.setColor(new Color(167, 167, 167));
                g.drawRect(i * gridSize, j * gridSize, gridSize, gridSize);
            }
        }
    }

}