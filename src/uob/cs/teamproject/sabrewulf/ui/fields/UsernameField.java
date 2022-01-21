package uob.cs.teamproject.sabrewulf.ui.fields;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import uob.cs.teamproject.sabrewulf.ResourceManager;
import uob.cs.teamproject.sabrewulf.ui.templates.WindowLabel;
import static javafx.geometry.Pos.CENTER;

/** Username Field creates a {@link HBox} which contains a text field to allow a player to enter their username
 *  and a label to identify the field. The username is validated to ensure that it meets a set of rules:
 *   - Only contains alphabetical characters (a-z,A-Z) or numbers
 *   - Length is not greater than 12
 */
public class UsernameField extends HBox {

    private String username;
    private TextField field;

    /** Constructor - Creates a new instance of username field
     */
    public UsernameField() {
        this.setSpacing(20);
        this.setAlignment(CENTER);
        this.username = "guest";
        createField();
        this.getChildren().addAll(createHeader(), field);
    }

    /** @return Returns the username entered by the player
     */
    public String getUsername() {
        return username;
    }

    /** @return Returns the username field
     */
    public TextField getField() {
        return field;
    }

    private WindowLabel createHeader() {
        WindowLabel usernameLabel = new WindowLabel("Username:", 15);
        usernameLabel.setMinWidth(120);
        return usernameLabel;
    }

    private void createField() {
        field = new TextField();
        field.setStyle("-fx-text-inner-color: #334E58; -fx-border-color: #8D91C7;");
        field.setFont(ResourceManager.getFont("fonts/kenpixel_square.ttf", 15));
        field.textProperty().addListener((ObservableValue<? extends String> observableValue, String s, String t1) -> {
                    if (!t1.matches("[a-zA-Z0-9]+")) {
                        field.setText(t1.replaceAll("[^[a-zA-Z0-9]]", ""));
                    }
                    if (field.getText().length() > 12) {
                        field.setText(field.getText().substring(0, 12));
                    }
                    if (field.getText().isEmpty()) {
                        username = "guest";
                    } else {
                        username = field.getText();
                    }
                }
        );
    }
}
