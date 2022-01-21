package uob.cs.teamproject.sabrewulf.ui.scene;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import uob.cs.teamproject.sabrewulf.audio.Audio;
import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.ResourceManager;
import uob.cs.teamproject.sabrewulf.input.Input;
import uob.cs.teamproject.sabrewulf.ui.LobbyConfiguration;
import uob.cs.teamproject.sabrewulf.ui.templates.TextButton;
import uob.cs.teamproject.sabrewulf.ui.templates.WindowLabel;
import uob.cs.teamproject.sabrewulf.network.NetworkSystem;

import static javafx.geometry.Pos.*;

/** Lobby Scene creates a {@link VBox} which displays the Multiplayer lobby scene on the user interface. It displays
 *  a container with the scene title, return button and a content container which displays the
 *  {@link LobbyConfiguration}
 */
public class LobbyScene extends VBox {

    private final Audio audio;
    private final NetworkSystem networkSystem;
    private final Input inputSystem;
    private HBox returnButton;
    private final StackPane menuRoot;

    /** Constructor - Creates a new instance of the lobby scene
     * @param audio - {@link Audio} system for button click FX
     * @param networkSystem - {@link NetworkSystem}
     * @param inputSystem - {@link Input} system
     * @param menuRoot - the {@link StackPane} which contains the menu controls
     */
    public LobbyScene(Audio audio, NetworkSystem networkSystem, Input inputSystem, StackPane menuRoot) {
        this.audio = audio;
        this.networkSystem = networkSystem;
        this.inputSystem = inputSystem;
        this.menuRoot = menuRoot;
        this.setSpacing(5);
        this.setAlignment(CENTER);
        this.setPrefWidth(700);
        this.setPrefHeight(500);
        this.setMaxWidth(700);
        this.setMaxHeight(500);
        createBackground();
        this.getChildren().addAll(sceneHeader(), multiplayerContainer());
    }

    /** Remove the return button from the scene
     */
    public void removeBackButton() {
        returnButton.setDisable(true);
        returnButton.setVisible(false);
    }
    
    private HBox sceneHeader() {
        HBox hBox = new HBox();
        hBox.setAlignment(CENTER);
        hBox.setPrefWidth(700);
        hBox.setPrefHeight(75);
        returnButton = returnButton();
        hBox.getChildren().addAll(lobbyTitle(), returnButton);
        return hBox;
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

    private HBox lobbyTitle() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(CENTER_LEFT);
        hBox.setPrefWidth(350);
        hBox.setPrefHeight(75);
        WindowLabel gameTitle = new WindowLabel("Multiplayer Lobby", 20);
        gameTitle.setTextFill(Color.web("#334E58"));
        HBox.setMargin(gameTitle, new Insets(0, 0, 0, 25));
        hBox.getChildren().add(gameTitle);
        return hBox;
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
            GameSettings.setNumPlayers(1);
            inputSystem.setMuteKeyEnabled(true);
        });
        return hBox;
    }

    private VBox multiplayerContainer(){
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setAlignment(CENTER);
        vBox.setPrefWidth(700);
        vBox.setPrefHeight(425);
        vBox.getChildren().addAll(new LobbyConfiguration(this,audio, networkSystem));
        return vBox;
    }
}
