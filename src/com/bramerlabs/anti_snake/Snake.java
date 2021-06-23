package com.bramerlabs.anti_snake;

import java.awt.*;
import java.util.ArrayList;

public class Snake {

    // the body of the snake
    private final ArrayList<int[]> occupied;
    private int[] headPosition;
    private int curLength;
    private int maxLength;

    // the direction the snake is moving
    private static final int
        right = 0,
        up = 1,
        left = 2,
        down = 3;

    // the speed of the snake
    public static int snakeSpeed = 7;

    private final Main main;

    /**
     * main constructor
     * @param main - the main game class
     */
    public Snake(Main main, int maxLength) {
        this.main = main;
        this.maxLength = maxLength;
        occupied = new ArrayList<>();
        headPosition = new int[]{Grid.gridWidth / 2, Grid.gridHeight / 2};
        occupied.add(headPosition);
        curLength = 1;
    }

    /**
     * moves the snake by 1 square
     */
    public void move(Player player) {
        // determine the next head position
        int[] newHeadPosition = new int[]{headPosition[0], headPosition[1]};
        int dx = Math.abs(player.getX() - headPosition[0]);
        int dy = Math.abs(player.getY() - headPosition[1]);
        if (dx > dy) {
            if (player.getX() < headPosition[0]) {
                newHeadPosition[0] -= 1;
            } else if (player.getX() > headPosition[0]) {
                newHeadPosition[0] += 1;
            }
        } else {
            if (player.getY() < headPosition[1]) {
                newHeadPosition[1] -= 1;
            } else if (player.getY() > headPosition[1]) {
                newHeadPosition[1] += 1;
            }
        }

        // check if it is inside the bounds of the box - should never be the case but maybe lmao
        if (newHeadPosition[0] < 0 || newHeadPosition[0] >= Grid.gridWidth ||
                newHeadPosition[1] < 0 || newHeadPosition[1] > Grid.gridHeight) {
            main.setGameState(Main.title);
        }

        // check if the head collides with the body
        if (checkCollision(newHeadPosition)) {
            main.incScore();
        }

        // add the new head position to the snake
        occupied.add(0, new int[]{newHeadPosition[0], newHeadPosition[1]});
        headPosition = new int[]{newHeadPosition[0], newHeadPosition[1]};

        if (curLength >= maxLength) {
            // remove the tail of the snake
            occupied.remove(occupied.size() - 1);
        } else {
            curLength += 1;
        }
    }

    /**
     * paints each component of the snake
     * @param g - the graphics object handed down by the paint() call
     */
    public void paint(Graphics g) {
        g.setColor(new Color(162, 219, 76));
        for (int[] body : occupied) {
            int x = body[0];
            int y = body[1];
            g.fillRect(x * Grid.gridSize, y * Grid.gridSize, Grid.gridSize, Grid.gridSize);
        }
    }

    /**
     * checks if a position collides with the snake
     * @param test - the test position
     * @return - if it collides
     */
    public boolean checkCollision(int[] test) {
        for (int[] body : occupied) {
            if (test[0] == body[0] && test[1] == body[1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * getter method
     * @return - the list of body components in the snake
     */
    public ArrayList<int[]> getOccupied() {
        return this.occupied;
    }

}
