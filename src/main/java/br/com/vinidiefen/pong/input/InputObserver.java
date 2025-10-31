package br.com.vinidiefen.pong.input;

import java.awt.event.KeyEvent;

/**
 * Observer interface for input events
 */
public interface InputObserver {

    /**
     * Called when input state changes
     * 
     * @param event The input event containing key states
     */
    void onInputChanged(KeyEvent event);

}
