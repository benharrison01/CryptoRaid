package uob.cs.teamproject.sabrewulf.input;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import uob.cs.teamproject.sabrewulf.audio.Audio;
import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.ui.selectors.WINDOWSIZE;
import java.util.ArrayList;

public class Input {
    Scene scene;
    Audio audio;
    private Boolean gameActive = true;
    private ArrayList<String> input = new ArrayList<String>();

    /**
     * A basic constructor, used for testing.
     */
    public Input() {
    }

    /**
     * The constructor for this class. Contains KeyEvent handlers which adds newly pressed
     * keys to an ArrayList of their corresponding strings. Since the game uses the WASD system,
     * opposite keys pressed shall removed the previously pressed opposite, for example
     * if a W is pressed, S will be removed from the ArrayList.
     * When a key is released, its value is also removed from the ArrayList.
     * @param scene: The JavaFx scene in which this Input class reads inputs from.
     * @param audio: The audio component in which this Input class controls.
     */
    public Input(Scene scene, Audio audio) {
        this.scene = scene;
        this.audio = audio;

        try {
            scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    KeyEvent e = (KeyEvent) event;
                    String code = e.getCode().toString();
                    if (!input.contains(code)) {
                        input.add(code);
                    }
                    if (oppositeOf(code) != null) {
                        if (input.contains(oppositeOf(code)[0]) || input.contains(oppositeOf(code)[1])) {
                            input.remove(oppositeOf(code)[0]);
                            input.remove(oppositeOf(code)[1]);
                        }
                    }
                    if (e.getCode().equals(KeyCode.SPACE) || e.getCode().equals(KeyCode.ENTER)) {
                        e.consume();
                    }
                    if (e.getCode().equals(KeyCode.ESCAPE)) {
                        if (GameSettings.getWindowSize() == WINDOWSIZE.FULLSCREEN) {
                            GameSettings.setWindowSize(WINDOWSIZE.WINDOWED);
                        }
                    }
                    if (e.getCode().equals(KeyCode.M) && gameActive) {
                        audio.setVolumeMusic(0);
                        audio.setVolumeFX(0);
                    }
                    if (e.getCode().equals(KeyCode.TAB)) {
                        if (!GameSettings.getPauseMenuDisabled()) {
                            GameSettings.getGameStateOwner().pauseGame();
                        }
                    }
                }
            });

            scene.setOnKeyReleased(
                    new EventHandler<KeyEvent>() {
                        public void handle(KeyEvent e) {
                            String code = e.getCode().toString();
                            input.remove(code);

                        }
                    });
        }
        catch(Exception ignored) {
            //for junit testing where no scene is set up
        }
    }

    /**
     * Getter method.
     * @return the most recent key pressed.
     */
    public String getCurrentKeyDown() {
        try {
            return input.get(input.size()-1);
        }
        catch (Exception e) {
            return "-1";
        }

    }

    /**
     * Helper function which maps W/UP to S/DOWN, S/DOWN to W/UP, D/RIGHT to A/LEFT and A/LEFT to D/RIGHT
     * @param s: The WASD key to find the opposite of.
     * @return the opposite key(s) as per the above mapping.
     */
    public String[] oppositeOf(String s) {
        if (s=="W"||s=="UP") {
            return new String[]{"S", "DOWN"};
        }
        else if (s=="D"||s=="RIGHT") {
            return new String[]{"A", "LEFT"};
        }
        else if (s=="S"||s=="DOWN") {
            return new String[]{"W","UP"};
        }
        else if (s=="A"||s=="LEFT") {
            return new String[]{"D","RIGHT"};
        }
        return null;
    }

    /**
     * Getter method.
     * @return the list of all keys currently being pressed.
     */
    public ArrayList<String> getInputList() {
        return input;
    }

    /**
     * Controls whether the keyboard shortcut for muting the audio should be enabled.
     * @param enabled: true to enable the mute key, false to disable it
     */
    public void setMuteKeyEnabled(boolean enabled) { this.gameActive = enabled; }


}