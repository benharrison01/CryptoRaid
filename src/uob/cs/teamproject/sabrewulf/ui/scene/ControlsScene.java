package uob.cs.teamproject.sabrewulf.ui.scene;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import uob.cs.teamproject.sabrewulf.audio.Audio;
import uob.cs.teamproject.sabrewulf.ResourceManager;
import uob.cs.teamproject.sabrewulf.ui.templates.TextButton;
import uob.cs.teamproject.sabrewulf.ui.templates.WindowLabel;

import static javafx.geometry.Pos.*;

/** Controls scene creates a {@link VBox} which displays the 'Controls' page on the user interface. It displays a
 *  container with the scene title, return button and content container which displays the details of the
 *  game controls
 */
public class ControlsScene extends VBox {

    private final Audio audio;
    private final StackPane menuRoot;
    private ImageView controls;

    /** Constructor - Creates a new instance of Controls Scene
     * @param audio - {@link Audio} system for button click FX
     * @param menuRoot - the {@link StackPane} which contains the menu controls
     */
    public ControlsScene(Audio audio, StackPane menuRoot) {
        this.audio = audio;
        this.menuRoot = menuRoot;
        this.setSpacing(5);
        this.setAlignment(CENTER);
        this.setPrefWidth(700);
        this.setPrefHeight(500);
        this.setMaxWidth(700);
        this.setMaxHeight(500);
        createBackground();
        this.getChildren().addAll(sceneHeader(), contentContainer());
    }

    private void createBackground() {
        String path = "images/uielements/container_shortheader.png";
        Image backgroundImage = ResourceManager.getImage(path, 700, 500, true, true);
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false));
        this.setBackground(new Background(background));
    }

    private HBox sceneHeader() {
        HBox hBox = new HBox();
        hBox.setAlignment(CENTER);
        hBox.setPrefWidth(700);
        hBox.setPrefHeight(75);
        hBox.getChildren().addAll(instructionsTitle(), returnButton());
        return hBox;
    }

    private HBox instructionsTitle() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(CENTER_LEFT);
        hBox.setPrefWidth(350);
        hBox.setPrefHeight(75);
        WindowLabel sceneTitle = new WindowLabel("Controls", 20);
        HBox.setMargin(sceneTitle, new Insets(0, 0, 0, 90));
        hBox.getChildren().add(sceneTitle);
        return hBox;
    }

    private VBox contentContainer(){
        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.setAlignment(CENTER);
        vBox.setPrefWidth(700);
        vBox.setPrefHeight(425);
        controls = new ImageView(ResourceManager.getImage("images/uielements/controls_option1.png"));
        controls.setFitHeight(300);
        controls.setFitWidth(500);
        vBox.getChildren().addAll(selectorButtons(), controls);
        return vBox;
    }

    private HBox returnButton() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(CENTER_RIGHT);
        hBox.setPrefWidth(350);
        hBox.setPrefHeight(75);
        TextButton returnButton = new TextButton("Back", 20, audio);
        returnButton.setTextFill(Color.web("#334E58"));
        HBox.setMargin(returnButton, new Insets(0, 25, 0, 0));
        hBox.getChildren().add(returnButton);
        returnButton.setOnAction(actionEvent -> {
            menuRoot.getChildren().remove(this);
        });
        return hBox;
    }

    private HBox selectorButtons() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(CENTER);
        TextButton option1Button = new TextButton("Option 1", 20, audio);
        TextButton option2Button = new TextButton("Option 2", 20, audio);
        option1Button.setButtonPressed();
        option1Button.setOnAction(actionEvent -> {
            option1Button.setButtonPressed();
            option2Button.setButtonFree();
            controls.setImage(ResourceManager.getImage("images/uielements/controls_option1.png"));
        });
        option2Button.setOnAction(actionEvent -> {
            option2Button.setButtonPressed();
            option1Button.setButtonFree();
            controls.setImage(ResourceManager.getImage("images/uielements/controls_option2.png"));
        });
        hBox.getChildren().addAll(option1Button, option2Button);
        return hBox;
    }
}
