package com.bramerlabs.anti_snake;

import java.awt.*;

public class Player {

    private int x, y;
    private final Color color = new Color(199, 97, 97);
    private final Main main;

    public static int playerSpeed = 3;

    public static final int
            right = 0,
            up = 1,
            left = 2,
            down = 3;

    /**
     * default constructor
     * @param x - the x position of the player in the grid
     * @param y - the y position of the player in the grid
     */
    public Player(int x, int y, Main main) {
        this.x = x;
        this.y = y;
        this.main = main;
    }

    /**
     * moves the player in a specific direction
     * @param direction - the direction to move in
     */
    public void move(int direction) {
        switch (direction) {
            case left:
                this.move(-1, 0);
                break;
            case right:
                this.move(1, 0);
                break;
            case up:
                this.move(0, -1);
                break;
            case down:
                this.move(0, 1);
                break;
        }
    }

    /**
     * moves the player
     * @param dx - the change in x location
     * @param dy - the change in y location
     */
    public void move(int dx, int dy) {
        if (!(this.x + dx < 0 || this.x + dx >= Grid.gridWidth)) {
            this.x += dx;
        }
        if (!(this.y + dy < 0 || this.y + dy >= Grid.gridHeight)) {
            this.y += dy;
        }
    }

    /**
     * updates the player
     * checks if the snake intersects with the player
     * @param snake - the snake
     */
    public void update(Snake snake) {
        for (int[] body : snake.getOccupied()) {
            if (this.x == body[0] && this.y == body[1]) {
                main.setGameState(Main.gameOver);
            }
        }
    }

    /**
     * paints the player
     * @param g - the graphics component handed down by the paint() call
     */
    public void paint(Graphics g) {
        g.setColor(color);
        g.fillRect(x * Grid.gridSize, y * Grid.gridSize, Grid.gridSize, Grid.gridSize);
    }

    /**
     * getter method
     * @return - the x position
     */
    public int getX() {
        return this.x;
    }

    /**
     * getter method
     * @return - the y position
     */
    public int getY() {
        return this.y;
    }

}
