package uob.cs.teamproject.sabrewulf.ui.templates;

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import uob.cs.teamproject.sabrewulf.audio.Audio;
import uob.cs.teamproject.sabrewulf.ResourceManager;

/** Text Button class extends {@link GenericButton}, to create a button which has text displayed. This
 *  class sets the text label, font, font size and listeners which define actions when the button is
 *  clicked
 */
public class TextButton extends GenericButton {

    private boolean selected = false;
    private final Audio audio;

    /** Constructor to create a Text Button
     * @param label - Text to be displayed on button
     * @param size - Font size for text
     * @param audio - {@link Audio} system instance
     */
    public TextButton(String label, int size, Audio audio) {
        super("images/uielements/button.png", "images/uielements/button_pressed.png");
        this.audio = audio;
        setPrefWidth(160);
        setPrefHeight(55);
        setMinWidth(160);
        setMinHeight(55);
        setButtonFont(size);
        setText(label);
        setButtonFreeStyle();
        buttonListeners();
    }

    /** Change button from 'pressed' state to a 'free' state
     */
    public void setButtonFree() {
        setButtonFreeStyle();
        selected = false;
        setTextFill(Color.web("#334E58"));
    }

    /** Change button from 'free' state to a 'pressed' state
     */
    public void setButtonPressed() {
        setButtonPressedStyle();
        selected = true;
        setTextFill(Color.web("#FFFFFF"));
    }

    private void setButtonFont(int size) {
        setFont(ResourceManager.getFont("fonts/kenpixel_square.ttf", size));
        setTextFill(Color.web("#334E58"));
    }

    private void buttonListeners() {
        setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                audio.playButtonAudio();
                setButtonPressedStyle();
                setTextFill(Color.web("#FFFFFF"));
            }
        });
        setOnMouseReleased(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                setButtonFreeStyle();
                if (!selected) {
                    setTextFill(Color.web("#334E58"));
                }
            }
        });
    }
}
