package uob.cs.teamproject.sabrewulf.ui.fields;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import uob.cs.teamproject.sabrewulf.ResourceManager;
import uob.cs.teamproject.sabrewulf.ui.templates.WindowLabel;
import static javafx.geometry.Pos.CENTER;

/** IP Address Field creates a {@link HBox} which contains a text field to allow a player to enter their IP address
 *  & port number and a label to identify the field. The field only allows numbers full stops (.) and colons (:),
 *  and has a length check but does not check if it is a legal IP address & port number (i.e. in the correct format)
 */
public class IPAddressField extends HBox {

    private String ip;
    private TextField ipField;

    /** Constructor - Creates a new instance of the IP address field
     */
    public IPAddressField() {
        this.setSpacing(20);
        this.setAlignment(CENTER);
        createField();
        this.getChildren().addAll(createHeader(), ipField);
    }

    /** @return Returns the IP address and port number
     */
    public String getIp() {
        return ip;
    }

    /**
     * @return Returns the IP address text field
     */
    public TextField getIpField() {
        return ipField;
    }

    private WindowLabel createHeader() {
        WindowLabel ipLabel = new WindowLabel("IP + Port:", 15);
        ipLabel.setMinWidth(120);
        return ipLabel;
    }

    private void createField() {
        ipField = new TextField("127.0.0.1:50000");
        ipField.setStyle("-fx-text-inner-color: #334E58; -fx-border-color: #8D91C7;");
        ipField.setFont(ResourceManager.getFont("fonts/kenpixel_square.ttf", 15));
        ip = ipField.getText();
        ipField.textProperty().addListener((ObservableValue<? extends String> observableValue, String s, String t1) -> {
            if (!t1.matches("[0-9.:]+")) {
                ipField.setText(t1.replaceAll("[^[0-9.:]]", ""));
            }
                    if (ipField.getText().length() > 21) {
                        ipField.setText(ipField.getText().substring(0, 21));
                    }
                    if (ipField.getText().isEmpty()) {
                        ip = "";
                    } else {
                        ip = ipField.getText();
                    }
                }
        );
    }
}