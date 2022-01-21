package uob.cs.teamproject.sabrewulf.ui.scene;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import uob.cs.teamproject.sabrewulf.audio.Audio;
import uob.cs.teamproject.sabrewulf.ResourceManager;
import uob.cs.teamproject.sabrewulf.ui.templates.TextButton;
import uob.cs.teamproject.sabrewulf.ui.templates.TextSection;
import uob.cs.teamproject.sabrewulf.ui.templates.WindowLabel;
import static javafx.geometry.Pos.*;

/** Instructions Scene creates a {@link VBox} which lists the game instructions on the user interface. It displays
 *  a container with the scene title, return button and a content container which lists instructions on
 *  how the user should play the game
 */
public class InstructionsScene extends VBox {

    private final Audio audio;
    private final StackPane menuRoot;

    /** Constructor - Creates a new instance of Instructions scene
     * @param audio - {@link Audio} system for button click FX
     * @param menuRoot - the {@link StackPane} which contains the menu controls
     */
    public InstructionsScene(Audio audio, StackPane menuRoot) {
        this.audio = audio;
        this.menuRoot = menuRoot;
        this.setSpacing(5);
        this.setAlignment(CENTER);
        this.setPrefWidth(700);
        this.setPrefHeight(500);
        this.setMaxWidth(700);
        this.setMaxHeight(500);
        createBackground();
        this.getChildren().addAll(sceneHeader(), instructionsContainer());
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
        WindowLabel gameTitle = new WindowLabel("How to Play", 20);
        HBox.setMargin(gameTitle, new Insets(0, 0, 0, 75));
        hBox.getChildren().add(gameTitle);
        return hBox;
    }

    private VBox instructionsContainer(){
        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.setAlignment(CENTER);
        vBox.setPrefWidth(700);
        vBox.setPrefHeight(425);
        TextButton controlsButton = new TextButton("Controls", 20, audio);
        controlsButton.setOnAction(actionEvent -> menuRoot.getChildren().add(new ControlsScene(audio, menuRoot)));
        ScrollPane instructionPane = new ScrollPane();
        instructionPane.setContent(gameInstructions());
        instructionPane.getStylesheets().add("uob/cs/teamproject/sabrewulf/ui/style/instructions.css");
        instructionPane.setFitToWidth(true);
        instructionPane.setPrefHeight(270);
        VBox.setMargin(instructionPane, new Insets(0,10,0,10));
        vBox.getChildren().addAll(instructionPane, controlsButton);
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

    private VBox gameInstructions() {
        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.setAlignment(CENTER);
        TextSection partOne = new TextSection("Objective: collect all the cryptocurrency " +
                "across the maze, whilst avoiding the antiviruses (AI)", 16);
        TextSection partTwo = new TextSection("Singleplayer mode: Collecting cryptocurrency will " +
                "increase your score but colliding with an antivirus will decrease your score", 16);
        TextSection partThree = new TextSection("You can collect lives, however you will lose a life " +
                "if you come into contact with an antivirus. If you run out of lives, the game is over", 16);
        TextSection partFour = new TextSection("Multiplayer mode: Your aim is to collect more cryptocurrency " +
                "than your opponents and have the highest score", 16);
        TextSection partFive = new TextSection("In both modes you can use powerups to help you, and see " +
                "your final score in the game leaderboard", 16);
        TextSection partSix = new TextSection("There are two types of powerup: invisibility (hides your " +
                "location from the antiviruses temporarily) and speed boost (increases your speed " +
                "temporarily)", 16);
        TextSection partSeven = new TextSection("To use a powerup from your inventory, you must activate it " +
                "using the correct control (see controls page)", 16);
        TextSection partEight = new TextSection("You can also collect keys, which allow you to use " +
                "coloured doors on the map. The antiviruses cannot use these doors. The key colour matches " +
                "the colour of the door", 16);
        TextSection partNine = new TextSection("You can also gain achievements for collecting powerups, " +
                "completing the game quickly and completing the game without using powerups", 16);
        vBox.getChildren().addAll(partOne, partTwo, partThree, partFour, partFive, partSix);
        vBox.getChildren().addAll(partSeven, partEight, partNine);
        return vBox;
    }
}
