package br.com.vinidiefen.pong;

import javax.swing.JFrame;

public class GameFrame extends JFrame {

    public GameFrame() {
        super("Pong Game");

        // Window size and state
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // For better performance
        setResizable(true);

        // Focus settings for keyboard input
        setFocusable(true);
        requestFocusInWindow();
    }
}