package br.com.vinidiefen.pong.input.handlers;

import java.awt.event.KeyEvent;
import br.com.vinidiefen.pong.constants.InputConstants;
import br.com.vinidiefen.pong.input.InputObserver;

public class GameShortcuts implements InputObserver {

    @Override
    public void onInputChanged(KeyEvent event) {
        if (event.getID() == KeyEvent.KEY_PRESSED) {
            if (event.getKeyCode() == InputConstants.EXIT_KEY) {
                System.exit(0);
            }
        }
    }

}
