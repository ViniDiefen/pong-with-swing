package br.com.vinidiefen.pong;

/**
 * Represents the current state of the game
 */
public enum GameState {
    MENU(true),
    PLAYING(true),
    PAUSED(true),
    GAME_OVER(true),
    STOPPED(false);

    private final boolean isGameLoopActive;

    GameState(boolean isGameLoopActive) {
        this.isGameLoopActive = isGameLoopActive;
    }

    public boolean isGameLoopActive() {
        return isGameLoopActive;
    }
}
