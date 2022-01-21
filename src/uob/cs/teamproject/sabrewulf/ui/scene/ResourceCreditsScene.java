package uob.cs.teamproject.sabrewulf.ui.scene;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.ResourceManager;
import uob.cs.teamproject.sabrewulf.audio.Audio;
import uob.cs.teamproject.sabrewulf.ui.templates.TextButton;
import uob.cs.teamproject.sabrewulf.ui.templates.TextSection;
import uob.cs.teamproject.sabrewulf.ui.templates.WindowLabel;
import static javafx.geometry.Pos.*;
import static javafx.geometry.Pos.CENTER;

/** Resource credits scene creates a {@link VBox} which contains all of the content to be displayed on the screen
 *  with details of resources we have used for the project, along with licensing info for those resources
 *  which required accreditation in the actual game
 */
public class ResourceCreditsScene extends VBox {

    private final Audio audio;
    private final StackPane menuRoot;

    /** Constructor for the resource credits scene, which creates a new instance and configures the layout
     *  and content of the scene
     * @param audio - {@link Audio} system
     * @param menuRoot - the {@link StackPane} which contains the menu controls
     */
    public ResourceCreditsScene(Audio audio, StackPane menuRoot) {
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
        WindowLabel sceneTitle = new WindowLabel("Resource Credits", 20);
        HBox.setMargin(sceneTitle, new Insets(0, 0, 0, 35));
        hBox.getChildren().add(sceneTitle);
        return hBox;
    }

    private VBox contentContainer(){
        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.setAlignment(CENTER);
        vBox.setPrefWidth(700);
        vBox.setPrefHeight(425);
        vBox.getChildren().addAll(creditsText());
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
        TextSection partOneA = new TextSection("Life powerup FX: Pomodoro 320655_rhodesmas_level-up-01.mp3" +
                " by shinephoenixstormcrow", 15);
        partOneA.setTextFill(Color.web("#334E58"));
        TextSection partOneB = new TextSection("This work is licensed under the Creative Commons Attribution " +
                "3.0 Unported License. To view a copy of this license, " +
                "visit http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative Commons, " +
                "PO Box 1866, Mountain View, CA 94042, USA.", 15);
        TextSection partOneC = new TextSection("Link to media: https://freesound.org/people/shinep" +
                "hoenixstormcrow/sounds/337049/ -- No changes were made to this media", 15);
        vBox.getChildren().addAll(partOneA, partOneB, partOneC);
        TextSection partTwoA = new TextSection("Speed boost FX: Synth power change" +
                " by qubodup", 15);
        partTwoA.setTextFill(Color.web("#334E58"));
        TextSection partTwoB = new TextSection("This work is licensed under the Creative Commons Attribution " +
                "3.0 Unported License. To view a copy of this license, " +
                "visit http://creativecommons.org/licenses/by/3.0/ or send a letter to Creative Commons, " +
                "PO Box 1866, Mountain View, CA 94042, USA.", 15);
        TextSection partTwoC = new TextSection("Link to media: " +
                "https://freesound.org/people/qubodup/sounds/211997/" +
                " -- The length of this media has been cropped and volume reduced", 15);
        vBox.getChildren().addAll(partTwoA, partTwoB, partTwoC);
        return vBox;
    }
}

