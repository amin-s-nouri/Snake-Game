package com.mycompany.snakegame; 

import javax.swing.JFrame; // Import statement. Brings in the JFrame class from the javax.swing package for GUI creation.

public class GameFrame extends JFrame { // Class declaration. Defines a new class named GameFrame that inherits from JFrame.

    public GameFrame() { // Constructor. Special method called when an instance of GameFrame is created.
        this.add(new GamePanel()); // Adds a new GamePanel to the GameFrame. GamePanel is likely another class that manages the game's content.
        this.setTitle("Snake Game"); // Sets the title of the window to "Snake Game".
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensures the application exits when the window is closed.
        this.setResizable(false); // Disables resizing of the game window.
        this.pack(); // Causes the window to be sized to fit the preferred size and layouts of its subcomponents.
        this.setVisible(true); // Makes the game window visible.
        this.setLocationRelativeTo(null); // Centers the game window on the screen.
    }
}
