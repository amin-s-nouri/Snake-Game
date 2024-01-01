package com.mycompany.snakegame; 

import java.awt.*; // Importing classes for graphics and colors for GUI components
import java.awt.event.*; // Importing AWT event listeners (classes) for handling events like key presses
import javax.swing.JPanel; // Importing JPanel class for creating a panel
import javax.swing.Timer; // Importing Timer class for scheduling tasks (creating a game loop)
import javax.swing.JOptionPane; // Importing JOptionPane for displaying dialog boxes, such as the difficulty selection menu
import java.util.Random; // Importing Random class for generating random numbers

public final class GamePanel extends JPanel implements ActionListener { // GamePanel class extending JPanel and implementing ActionListener
    // Game constants
    static final int SCREEN_WIDTH = 600; // Constant for Width of the game screen
    static final int SCREEN_HEIGHT = 600; // Constant for Height of the game screen
    static final int UNIT_SIZE = 13; // Size of the game objects (like the snake and apple)
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE; // Total number of possible units on the screen
    static int DELAY; // Delay between game updates (milliseconds)

    // Arrays to hold the coordinates of the snake's parts
    final int[] x = new int[GAME_UNITS]; // Array to store x-coordinates of the snake's body parts
    final int[] y = new int[GAME_UNITS]; // Array to store y-coordinates of the snake's body parts

    // Game variables
    int bodyParts = 6; // Initial number of body parts of the snake
    int applesEaten; // Count of the apples eaten by the snake
    int appleX; // X-coordinate of the apple
    int appleY; // Y-coordinate of the apple
    char direction = 'R'; // Initial direction of the snake (R = Right, L = Left, U = Up, D = Down)
    boolean running = false; // Indicates if the game is running
    Timer timer; // Timer object to for game loop
    Random random; // Random number generator for placing the apple

    // Constructor
    GamePanel() {
        random = new Random(); // Initializing the random object
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); // Setting the size of the panel
        this.setBackground(Color.black); // Setting the background color of the panel
        this.setFocusable(true); // Making sure the panel can be focused to receive key events
        this.addKeyListener(new MyKeyAdapter()); // Adding a key listener to capture key presses
        selectDifficulty(); // Method to select game difficulty
        timer = new Timer(DELAY, this); // Setting up the timer with delay and action listener
        timer.start(); // Starting the timer
        startGame(); // Calling the method to start the game
    }

    // Method to select game difficulty and set DELAY
    private void selectDifficulty() {
        String[] options = {"Easy", "Medium", "Hard"};
        int choice = JOptionPane.showOptionDialog(null, "Select Difficulty", 
                "Difficulty Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
                null, options, options[0]);
        

        switch(choice) {
            case 0: // Easy
                DELAY = 250;
                break;
            case 1: // Medium
                DELAY = 180;
                break;
            case 2: // Hard
                DELAY = 100;
                break;
            default: // Default to Exit if no selection
                System.exit(0);
                break;
        }
    }

// Method to start the game
    public void startGame() {
        newApple(); // Method to place a new apple(the first apple)
        running = true; // Set the game as running
        timer = new Timer(DELAY, this); // Setting up the timer with delay and action listener (call actionPerformed method) 
        timer.start(); // Starting the timer
    }

// Method to paint components on the screen
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Calls the superclass method for painting
        draw(g); // Calling the draw method to draw game components
    }
    
// Method to draw components on the screen
    public void draw(Graphics g) { // Drawing logic (grid, snake, apple, score)
        if (running) { // Check if the game is currently running

//Drawing logic grid

            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) { // Loop to draw grid lines vertically and horizontally
                 g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); // Draw vertical lines of the grid
                 g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE); // Draw horizontal lines of the grid
       }

// Drawing the apple
        g.setColor(Color.red); // Set the color for drawing the apple
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); // Draw the apple at its position

        for (int i = 0; i < bodyParts; i++) { // Loop through each part of the snake's body
           
// Drawing the snake
            if (i == 0) { // Check if it's the head of the snake
                g.setColor(Color.green); // Set color for the snake's head
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); // Draw the snake's head
            } else { // If it's not the head, it's the body
                g.setColor(new Color(45, 180, 0)); // Set color for the snake's body
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); // Draw part of the snake's body
            }
        }
// Drawing the score
        g.setColor(Color.red); // Set color for drawing the score
        g.setFont(new Font("Ink Free", Font.BOLD, 40)); // Set the font for the score
        FontMetrics metrics = getFontMetrics(g.getFont()); // Get font metrics for aligning text
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize()); // Draw the score on the screen
    } else { // If the game is not running
        gameOver(g); // Call the gameOver method to display the game over screen
    }
}

// Method to place a new apple
    public void newApple() { // Logic to place a new apple at a random position
    appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE; // Randomly sets the X-coordinate for the apple
    appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE; // Randomly sets the Y-coordinate for the apple
}
// Method to move the snake
    public void move() {// Logic to move the snake

// Shifting the coordinates of snake parts
        for (int i = bodyParts; i > 0; i--) {
          x[i] = x[i - 1]; // Moves the body parts of the snake forward in the array
          y[i] = y[i - 1]; // Moves the body parts of the snake forward in the array
    }

// Changing the direction of the snake's head
    switch (direction) {
        case 'U' -> y[0] = y[0] - UNIT_SIZE; // Moves the head of the snake up
        case 'D' -> y[0] = y[0] + UNIT_SIZE; // Moves the head of the snake down
        case 'L' -> x[0] = x[0] - UNIT_SIZE; // Moves the head of the snake left
        case 'R' -> x[0] = x[0] + UNIT_SIZE; // Moves the head of the snake right
    }
}
// Method to check if the apple is eaten
    public void checkApple() { // Logic to check if the apple is eaten
        if ((x[0] == appleX) && (y[0] == appleY)) {
             bodyParts++; // Increases the size of the snake
             applesEaten++; // Increments the number of apples eaten
             newApple(); // Generates a new apple
             
            if (DELAY > 70) { // Check to prevent delay from becoming too small
            DELAY -= 10; // Decrease delay to increase speed
            timer.stop(); // Stop current timer
            timer = new Timer(DELAY, this); // Create new timer with decreased delay
            timer.start(); // Start new timer
        }
    }
}
// Method to check for collisions
    public void checkCollisions() { // Logic to check for collisions       
    // Check if the head collides with the body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                 running = false; // Checks if the head collides with the body
        }
    }

// Check if the head touches left, right, top, or bottom borders
    if (x[0] < 0) {
        running = false; // Checks if the snake hits the left wall
    }
    if (x[0] >= SCREEN_WIDTH) {
        running = false; // Checks if the snake hits the right wall
    }
    if (y[0] < 0) {
        running = false; // Checks if the snake hits the top wall
    }
    if (y[0] >= SCREEN_HEIGHT) {
        running = false; // Checks if the snake hits the bottom wall
    }

// Stop the timer if the game is not running
    if (!running) {
        timer.stop(); // Stops the timer if the game is not running
        
    }
}
    

// Method to display the game over screen
    public void gameOver(Graphics g) { // Logic for game over state

// Display score
        g.setColor(Color.red); // Sets the color for the text
        g.setFont(new Font("Ink Free", Font.BOLD, 40)); // Sets the font for the score text
        FontMetrics metrics1 = getFontMetrics(g.getFont()); // Gets font metrics for the score text
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize()); // Draws the score

// Display Game Over text
        g.setColor(Color.red); // Sets the color for the text
        g.setFont(new Font("Ink Free", Font.BOLD, 75)); // Sets the font for the game over text
        FontMetrics metrics2 = getFontMetrics(g.getFont()); // Gets font metrics for the game over text
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2); // Draws the game over message
}
// Method called by the timer at fixed intervals

@Override
public void actionPerformed(ActionEvent e) {
    if (running) {
        move(); // Moves the snake
        checkApple(); // Checks for apple collision
        checkCollisions(); // Checks for collisions with self or wall
    }
    repaint(); // Repaints the panel
    
}

// Inner class to handle key events
    public class MyKeyAdapter extends KeyAdapter { // Custom key adapter class extending KeyAdapter
     @Override
        public void keyPressed(KeyEvent e) { // Overriding the keyPressed method to handle key events
// Change the direction of the snake based on the key pressed
          switch (e.getKeyCode()) { // Switch case based on the key code of the pressed key
            
              case KeyEvent.VK_LEFT: // Case for left arrow key
                    if (direction != 'R') { // Check if current direction is not right
                        direction = 'L'; // Change direction to left
                }
                break; // Break out of the switch case
            
              case KeyEvent.VK_RIGHT: // Case for right arrow key
                if (direction != 'L') { // Check if current direction is not left
                    direction = 'R'; // Change direction to right
                }
                break; // Break out of the switch case
            
              case KeyEvent.VK_UP: // Case for up arrow key
                if (direction != 'D') { // Check if current direction is not down
                    direction = 'U'; // Change direction to up
                }
                break; // Break out of the switch case
            
              case KeyEvent.VK_DOWN: // Case for down arrow key
                if (direction != 'U') { // Check if current direction is not up
                    direction = 'D'; // Change direction to down
                }
                break; // Break out of the switch case
            }
        }
    }
}
