package uob.cs.teamproject.sabrewulf.ui.templates;

import javafx.scene.control.Button;
import javafx.scene.layout.*;
import uob.cs.teamproject.sabrewulf.ResourceManager;

/** Generic button class extends {@link Button} to create a button which has both a pressed state and
 *  a free state. The button style changes to the pressed state when clicked with the mouse and then to
 *  the free state when released
 */
public class GenericButton extends Button {

    private final String buttonFreeStyle;
    private final String buttonPressedStyle;

    /** Constructor for a generic button
     * @param buttonPath - Path for 'free' button image
     * @param buttonPressedPath - Path for 'pressed' button image
     */
    public GenericButton(String buttonPath, String buttonPressedPath) {
        this.buttonFreeStyle = buttonPath;
        this.buttonPressedStyle = buttonPressedPath;
        setButtonFreeStyle();
        setPrefWidth(30);
        setPrefHeight(30);
    }

    /** Set style of button to 'pressed' style
     */
    public void setButtonPressedStyle() {
        BackgroundImage buttonImage = new BackgroundImage(ResourceManager.getImage(buttonPressedStyle),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background background = new Background(buttonImage);
        this.setBackground(background);
    }

    /** Set style of button to 'free' style
     */
    public void setButtonFreeStyle() {
        BackgroundImage buttonImage = new BackgroundImage(ResourceManager.getImage(buttonFreeStyle),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background background = new Background(buttonImage);
        this.setBackground(background);
    }
}
