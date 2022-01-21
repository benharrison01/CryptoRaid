package uob.cs.teamproject.sabrewulf.ui.templates;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import uob.cs.teamproject.sabrewulf.ResourceManager;

/** Window Label class extends {@link Label}. This class defines the formatting of a text label when it is displayed
 *  on the screen
 */
public class WindowLabel extends Label {

    /** Constructor - Creates a new instance of Window Label
     * @param label - Text to be displayed
     * @param size - Font size of the text
     */
    public WindowLabel(String label, int size) {
        setButtonFont(size);
        setText(label);
    }

    private void setButtonFont(int size) {
        setFont(ResourceManager.getFont("fonts/kenpixel_square.ttf", size));
        setTextFill(Color.web("#334E58"));
    }
}

