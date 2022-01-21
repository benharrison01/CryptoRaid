package uob.cs.teamproject.sabrewulf;

import uob.cs.teamproject.sabrewulf.ui.scene.FinalScoreScene;

/**
 * The application as a whole acts like a state machine, transitioning from the start menu to the game, from the game to
 * the pause menu, etc. This interface exposes methods for transitioning between those states.
 */
public interface GameStateOwner {

    /** Transition from 'start menu' to 'in-game'. */
    void startGame();

    /** Transition from 'in-game' to 'paused'. */
    void pauseGame();

    /** Transition from 'paused' to 'in-game'. */
    void resumeGame();

    /**
     * Transition from 'in-game' to 'completed' or from 'paused' to 'completed'.
     * @param type describes the manner in which the game was exited (e.g. win, lose, quit, etc.)
     */
    void completeGame(FinalScoreScene.COMPLETIONTYPE type);

    /** Transition from 'paused' to 'in-game' (restarting the game) or from 'completed' to 'in-game'. */
    void restartGame();

    /** Transition from 'paused' to 'start menu' or from 'completed' to 'start menu'. */
    void returnToMenu();
}