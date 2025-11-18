package br.com.vinidiefen.pong.input.handlers;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import br.com.vinidiefen.pong.input.InputObserver;

/**
 * Handles keyboard input and notifies observers (Subject in Observer pattern)
 */
public class KeyboardHandler extends KeyAdapter {

    // Observers
    private final List<InputObserver> inputObservers = new CopyOnWriteArrayList<>();

    /**
     * Register an observer for left paddle input
     */
    public void addInputObserver(InputObserver observer) {
        if (observer == null || inputObservers.contains(observer)) {
            return;
        }
        inputObservers.add(observer);
    }

    public void removeInputObserver(InputObserver observer) {
        inputObservers.remove(observer);
    }

    public void clearObservers() {
        inputObservers.clear();
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
