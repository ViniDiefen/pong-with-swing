package br.com.vinidiefen.pong.input;

import java.awt.event.KeyEvent;

public class GameShortcuts implements InputObserver {
    
    public static final int EXIT_KEY = java.awt.event.KeyEvent.VK_ESCAPE;

    @Override
    public void onInputChanged(KeyEvent event) {
        if (event.getID() == KeyEvent.KEY_PRESSED) {
            if (event.getKeyCode() == EXIT_KEY) {
                System.exit(0);
            }
        }
    }

}
