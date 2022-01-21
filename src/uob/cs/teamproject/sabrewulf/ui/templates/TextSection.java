package uob.cs.teamproject.sabrewulf.ui.templates;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/** Text Section class extends the {@link Label} class, and defines the formatting for a paragraph of text to be
 *  displayed
 */
public class TextSection extends Label {

    /** Constructor - Creates a new instance of Text Section
     * @param label - Text to be displayed
     * @param size - Size of text to be displayed
     */
    public TextSection(String label, int size) {
        setTextFont(size);
        setText(label.toUpperCase());
        this.setTextAlignment(TextAlignment.CENTER);
        this.setWrapText(true);
        VBox.setMargin(this, new Insets(0, 10, 0, 10));
    }

    private void setTextFont(int size) {
        setFont(Font.font("Courier New", size));
        setTextFill(Color.web("#FFFAFA"));
    }
}
