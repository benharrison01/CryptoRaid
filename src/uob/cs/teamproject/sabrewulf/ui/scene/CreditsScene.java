package uob.cs.teamproject.sabrewulf.ui.scene;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import uob.cs.teamproject.sabrewulf.audio.Audio;
import uob.cs.teamproject.sabrewulf.ResourceManager;
import uob.cs.teamproject.sabrewulf.ui.templates.TextButton;
import uob.cs.teamproject.sabrewulf.ui.templates.TextSection;
import uob.cs.teamproject.sabrewulf.ui.templates.WindowLabel;

import static javafx.geometry.Pos.*;

/** Credits Scene creates a {@link VBox} which displays the 'Credits' scene on the user interface. It displays a
 *  container with the scene title, return button and content container which lists the credits for the
 *  game
 */
public class CreditsScene extends VBox {

    private final Audio audio;
    private final StackPane menuRoot;

    /** Constructor - Creates a new instance of the Credits scene
     * @param audio - {@link Audio} system for button click FX
     * @param menuRoot - the {@link StackPane} which contains the menu controls
     */
    public CreditsScene(Audio audio, StackPane menuRoot) {
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
        WindowLabel sceneTitle = new WindowLabel("Credits", 20);
        HBox.setMargin(sceneTitle, new Insets(0, 0, 0, 100));
        hBox.getChildren().add(sceneTitle);
        return hBox;
    }

    private VBox contentContainer(){
        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.setAlignment(CENTER);
        vBox.setPrefWidth(700);
        vBox.setPrefHeight(425);
        vBox.getChildren().addAll(creditsText(), resourcesButton());
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

    private VBox creditsText() {
        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.setAlignment(CENTER);
        WindowLabel partOne = new WindowLabel("Game Developers:", 30);
        TextSection partTwo = new TextSection("Benjamin Harrison", 20);
        TextSection partThree = new TextSection("Bernadeta Urvikyte", 20);
        TextSection partFour = new TextSection("Matthew Clutterbuck", 20);
        TextSection partFive = new TextSection("Miles Courtie", 20);
        TextSection partSix = new TextSection("Kushal Patel", 20);
        TextSection partSeven = new TextSection("William Tabbron", 20);
        vBox.getChildren().addAll(partOne, partTwo, partThree, partFour, partFive, partSix, partSeven);
        return vBox;
    }

    private HBox resourcesButton() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(CENTER);
        TextButton resourcesButton = new TextButton("Resources", 18, audio);
        resourcesButton.setOnAction(actionEvent -> {
            menuRoot.getChildren().add(new ResourceCreditsScene(audio, menuRoot));
        });
        hBox.getChildren().add(resourcesButton);
        return hBox;
    }
}
