package com.diefen.vini.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles keyboard input and notifies observers (Subject in Observer pattern)
 */
public class KeyboardHandler extends KeyAdapter {

    // Observers
    private List<InputObserver> inputObservers = new ArrayList<>();

    /**
     * Register an observer for left paddle input
     */
    public void addInputObserver(InputObserver observer) {
        inputObservers.add(observer);
    }

    public void removeInputObserver(InputObserver observer) {
        inputObservers.remove(observer);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        notifyObservers(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        notifyObservers(e);
    }

    /**
     * Notify all observers about input changes
     */
    private void notifyObservers(KeyEvent e) {
        for (InputObserver observer : inputObservers) {
            observer.onInputChanged(e);
        }
    }

}
