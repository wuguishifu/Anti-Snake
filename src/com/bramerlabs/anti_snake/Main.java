package com.bramerlabs.anti_snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.prefs.Preferences;

public class Main {

    // the frame and panel used to display the game
    private JFrame frame;
    private JPanel panel;

    // the state of the game
    int state = 0;
    public static final int
        title = 0,
        game = 1,
        gameOver = 2;

    // the screen size
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    // the snake
    private Snake snake;
    private int maxLength = 15;

    // the player
    private Player player;
    private boolean playerMoving = false;
    private int playerDirection = 0;

    // if the game is running
    private boolean running;
    private boolean update;
    private boolean drawPlusOne = false;
    private int timer = 0;

    // the score
    private int score = 0;
    private int highScore = 0;

    /**
     * main runnable
     * @param args - jvm arguments
     */
    public static void main(String[] args) {
        new Main().init();
    }

    /**
     * paints the scene
     * @param g - the graphics component handed down by the paint() call
     */
    public void paintScene(Graphics g) {
        switch (state) {
            case 0:
                frame.setTitle("Anti-Snake");
                g.drawRect(100, 100, frame.getWidth() - 200, 200);
                drawCenteredText(g, 100, 100, frame.getWidth() - 100, 166,
                        "Anti Snake");
                drawCenteredText(g, 100, 166, frame.getWidth() - 100, 233,
                        "Get the snake to collide with itself");
                drawCenteredText(g, 100, 233, frame.getWidth() - 100, 300,
                        "Press 'enter' to Start or Press 'esc' to Quit");
                g.drawRect(100, 400, frame.getWidth() - 200, 100);
                drawCenteredText(g, 100, 400, frame.getWidth() - 100, 450,
                        "High Score: " + highScore);
                drawCenteredText(g, 100, 450, frame.getWidth() - 100, 500,
                        "Press 'R' to Reset high score");
                break;
            case 1:
                snake.paint(g);
                player.paint(g);
                Grid.paint(g);
                g.setColor(Color.BLACK);
                frame.setTitle("Score: " + score);
                if (timer > 50) {
                    drawPlusOne = false;
                }
                timer++;
                if (drawPlusOne) {
                    drawCenteredText(g, 0, 0, frame.getWidth(), frame.getHeight(), "+1");
                }
                break;
            case 2:
                frame.setTitle("Game Over!");
                g.drawRect(100, 100, frame.getWidth() - 200, frame.getHeight() - 200);
                g.setColor(Color.RED);
                drawCenteredText(g, 100, 100, frame.getWidth() - 100, 300,
                        "Game Over! Score: " + score);
                drawCenteredText(g, 100, 300, frame.getWidth() - 100, frame.getHeight() - 100,
                        "Press 'esc' to Return to Menu or 'enter' to restart");
        }
    }

    /**
     * draws centered text
     * @param g - the graphics object handed down by panel.repaint()
     * @param minX - the min x value to draw in
     * @param minY - the min y value to draw in
     * @param maxX - the max x value to draw in
     * @param maxY - the max y value to draw in
     * @param string - the string to draw
     */
    private static void drawCenteredText(Graphics g, int minX, int minY, int maxX, int maxY, String string) {
        Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(string, g2d);
        int x = minX + ((maxX - minX) - (int) r.getWidth()) / 2;
        int y = minY + ((maxY - minY) - (int) r.getHeight()) / 2 + fm.getAscent();
        g.drawString(string, x, y);
    }

    /**
     * initialize a new game
     */
    public void gameInit() {
        snake = new Snake(this, maxLength);
        maxLength -= 1;
        player = new Player(0, 0, this);
    }

    /**
     * sets the game state
     * @param gameState - the new game state
     */
    public void setGameState(int gameState) {
        this.state = gameState;
    }

    /**
     * increments the score
     */
    public void incScore() {
        score++;
        drawPlusOne = true;
        timer = 0;
        if (score > highScore) {
            highScore = score;
        }
        gameInit();
    }

    /**
     * initializes the game
     */
    @SuppressWarnings("deprecation")
    private void init() {
        // read the high score
        highScore = readHighScore();

        // initialize the game
        gameInit();

        // make the window occupy one quarter of the screen
        Dimension windowSize = new Dimension(Grid.gridWidth * Grid.gridSize,
                Grid.gridHeight * Grid.gridSize);
        Point windowPos = new Point(screenSize.width/2 - windowSize.width/2,
                screenSize.height/2 - windowSize.height/2);

        // initialize the frame
        frame = new JFrame();
        frame.setSize(windowSize);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // initialize the panel
        panel = new JPanel(true) {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                paintScene(g);
            }
        };
        panel.setPreferredSize(windowSize);

        // initialize the keyboard listener
        KeyListener keyListener = new KeyListener() {
            /**
             * runs when a key is pressed and then released
             * @param keyEvent - the key event
             */
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            /**
             * runs when a key is pressed
             * @param keyEvent - the key event
             */
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        if (state == gameOver) {
                            state = title;
                        } else {
                            stop();
                        }
                        break;
                    case KeyEvent.VK_SPACE:
                        update = true;
                        break;
                    case KeyEvent.VK_R:
                        if (state == title) {
                            setHighScore(0);
                        }
                        break;
                    case KeyEvent.VK_ENTER:
                        if (state == title || state == gameOver) {
                            state = game;
                            gameInit();
                        }
                    case KeyEvent.VK_W:
                        playerMoving = true;
                        playerDirection = Player.up;
                        break;
                    case KeyEvent.VK_A:
                        playerMoving = true;
                        playerDirection = Player.left;
                        break;
                    case KeyEvent.VK_S:
                        playerMoving = true;
                        playerDirection = Player.down;
                        break;
                    case KeyEvent.VK_D:
                        playerMoving = true;
                        playerDirection = Player.right;
                        break;
                }
            }

            /**
             * runs when a key is released
             * @param keyEvent - the key event
             */
            @Override
            public void keyReleased(KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_W:
                        if (playerDirection == Player.up) {
                            playerMoving = false;
                        }
                        break;
                    case KeyEvent.VK_A:
                        if (playerDirection == Player.left) {
                            playerMoving = false;
                        }
                        break;
                    case KeyEvent.VK_S:
                        if (playerDirection == Player.down) {
                            playerMoving = false;
                        }
                        break;
                    case KeyEvent.VK_D:
                        if (playerDirection == Player.right) {
                            playerMoving = false;
                        }
                        break;
                }
            }
        };

        // add the components to the frame
        frame.addKeyListener(keyListener);
        frame.add(panel);

        // pack and display the window
        frame.pack();
        frame.move(windowPos.x, windowPos.y);
        frame.setVisible(true);
        panel.repaint();

        this.run();
    }

    /**
     * stops the application
     */
    public void stop() {
        this.running = false;
    }

    /**
     * runs the game loop
     */
    @SuppressWarnings("BusyWait")
    private void run() {
        running = true;
        int snakeCount = 0;
        int playerCount = 0;
//        int timeCount = 0;
        while (running) {

            if (state == game) {
                snakeCount++;
                if (snakeCount >= Snake.snakeSpeed) {
                    snakeCount = 0;
                    snake.move(player);
                }

                playerCount++;
                if (playerCount >= Player.playerSpeed && playerMoving) {
                    playerCount = 0;
                    player.move(playerDirection);
                }

//            timeCount += 1;
//            if (timeCount >= 50) {
//                timeCount = 0;
//                System.out.println(Snake.snakeSpeed);
//                Snake.snakeSpeed -= 1;
//            }
//            if (update) {
//                snake.move(player);
//                move = false;
//            }
                player.update(snake);
            }


            panel.repaint();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

        setHighScore(highScore);
        frame.dispose();

    }

    public void setHighScore(int newHighScore) {
        highScore = newHighScore;
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        prefs.put("high_score", String.valueOf(highScore));
    }

    public int readHighScore() {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        return Integer.parseInt(prefs.get("high_score", "0"));
    }

}
