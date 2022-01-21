package uob.cs.teamproject.sabrewulf.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import uob.cs.teamproject.sabrewulf.audio.Audio;
import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.ResourceManager;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODE;
import uob.cs.teamproject.sabrewulf.ui.templates.WindowLabel;
import uob.cs.teamproject.sabrewulf.components.Inventory;

/** In game bar creates a {@link HBox} which contains visual representation of all the score and power up related
 *  features which are stored in the {@link Inventory}. It also includes a button which links to the in game menu
 */
public class InGameBar extends HBox {

    private final Audio audio;
    private Inventory inventory;
    private Button menuButton;
    private WindowLabel scoreLabel;
    private WindowLabel livesLabel;
    private WindowLabel chasedLabel;
    private ImageView invisibilityIcon;
    private WindowLabel invisibilityNum;
    private ImageView boostIcon;
    private WindowLabel boostNum;
    private ImageView blueKey;
    private ImageView greenKey;
    private ImageView yellowKey;

    /** Creates a new instance of the in game bar
     * @param audio - {@link Audio} system for button click FX
     */
    public InGameBar(Audio audio) {
        this.audio = audio;
        this.setPadding(new Insets(2, 5, 2, 5));
        this.setSpacing(5);
        this.setBackground(new Background(new BackgroundFill(Color.web("#000000"), null, null)));
        this.setAlignment(Pos.CENTER);
    }

    /** Adds the {@link Inventory} to the {@link GameSettings} class and creates instance of the elements to be
     *  displayed in the in-game bar
     */
    public void setInventory() {
        this.inventory = GameSettings.getInventory();
        menuButton = menuButton();
        scoreLabel = scoreLabel();
        livesLabel = livesLabel();
        chasedLabel = chasedLabel();
        invisibilityIcon = invisibilityIcon();
        invisibilityNum = invisibilityNum();
        boostIcon = boostIcon();
        boostNum = boostNum();
        blueKey = blueKey();
        greenKey = greenKey();
        yellowKey = yellowKey();
    }

    /** Add all the elements defined by this class to the menu bar, depending on whether single player
     *  or multiplayer game
     */
    public void addChildren() {
        if (GameSettings.getGameMode() == MODE.SINGLEPLAYER) {
            this.getChildren().addAll(menuButton, scoreLabel, livesLabel, chasedLabel, invisibilityIcon);
            this.getChildren().addAll(invisibilityNum, boostIcon, boostNum, blueKey, greenKey, yellowKey);
        } else {
            this.getChildren().addAll(menuButton, scoreLabel, chasedLabel, invisibilityIcon, invisibilityNum);
            this.getChildren().addAll(boostIcon, boostNum, blueKey, greenKey, yellowKey);
        }
    }

    /** Remove all the elements defined by this class to the menu bar, depending on whether single player
     *  or multiplayer game
     */
    public void removeChildren() {
        if (GameSettings.getGameMode() == MODE.SINGLEPLAYER) {
            this.getChildren().removeAll(menuButton, scoreLabel, livesLabel, chasedLabel, invisibilityIcon);
            this.getChildren().removeAll(invisibilityNum, boostIcon, boostNum, blueKey, greenKey, yellowKey);
        } else {
            this.getChildren().removeAll(menuButton, scoreLabel, chasedLabel, invisibilityIcon, invisibilityNum);
            this.getChildren().removeAll(boostIcon, boostNum, blueKey, greenKey, yellowKey);
        }
    }

    private Button menuButton() {
        ImageView icon = new ImageView(ResourceManager.getImage("images/uielements/menu_icon.png"));
        Button menuButton = new Button("", icon);
        menuButton.setStyle("-fx-background-color: #000000;");
        menuButton.setOnAction(actionEvent -> {
            audio.playButtonAudio();
            GameSettings.getGameStateOwner().pauseGame();
        });
        return menuButton;
    }

    private WindowLabel scoreLabel() {
        WindowLabel scoreLabel = new WindowLabel("Score: " + inventory.getScore(), 15);
        scoreLabel.setMinWidth(150);
        scoreLabel.setAlignment(Pos.CENTER);
        scoreLabel.setTextFill(Color.web("#FFFFFF"));
        inventory.scoreProperty().addListener((o, oldVal, newVal) -> {
            scoreLabel.setText("Score: " + inventory.getScore());
        });
        return scoreLabel;
    }

    private WindowLabel livesLabel() {
        WindowLabel livesLabel = new WindowLabel("Lives remaining: " + inventory.getNumberOfLives(), 15);
        livesLabel.setMinWidth(200);
        livesLabel.setAlignment(Pos.CENTER);
        livesLabel.setTextFill(Color.web("#FFFFFF"));
        inventory.livesProperty().addListener((o, oldVal, newVal) -> {
            livesLabel.setText("Lives remaining: " + inventory.getNumberOfLives());
        });
        return livesLabel;
    }

    private WindowLabel chasedLabel() {
        WindowLabel chasedLabel = new WindowLabel(inventory.getEnemiesChasingPlayer() + " chasing you", 15);
        chasedLabel.setMinWidth(170);
        chasedLabel.setAlignment(Pos.CENTER);
        chasedLabel.setTextFill(Color.web("#FFFFFF"));
        inventory.enemiesChasingPlayerProperty().addListener((o, oldVal, newVal) -> {
            chasedLabel.setText(inventory.getEnemiesChasingPlayer() + " chasing you");
        });
        return chasedLabel;
    }

    private ImageView invisibilityIcon() {
        String path = "images/mapItems/ghost_powerup.png";
        ImageView invisibilityIcon = new ImageView(ResourceManager.getImage(path, 30, 30, true, true));
        invisibilityIcon.setOpacity(0.4);
        inventory.invisibilityProperty().addListener((o, oldVal, newVal) -> {
            if (inventory.getInvisible()&&!inventory.isInactive()) {
                invisibilityIcon.setOpacity(1.0);
            } else {
                invisibilityIcon.setOpacity(0.4);
            }
        });
        return invisibilityIcon;
    }

    private WindowLabel invisibilityNum() {
        WindowLabel invisibilityLabel = new WindowLabel(String.valueOf(inventory.getNumInvisibilitiesWaiting()),
                15);
        invisibilityLabel.setMinWidth(20);
        invisibilityLabel.setAlignment(Pos.CENTER);
        invisibilityLabel.setTextFill(Color.web("#FFFFFF"));
        inventory.numInvisibilitiesWaitingProperty().addListener((o, oldVal, newVal) -> {
            invisibilityLabel.setText(String.valueOf(inventory.getNumInvisibilitiesWaiting()));
        });
        return invisibilityLabel;
    }

    private ImageView boostIcon() {
        String path = "images/mapItems/lightning_powerup.png";
        ImageView boostIcon = new ImageView(ResourceManager.getImage(path, 30, 30, true, true));
        boostIcon.setOpacity(0.4);
        inventory.boostProperty().addListener((o, oldVal, newVal) -> {
            if (inventory.getBoostProperty()) {
                boostIcon.setOpacity(1.0);
            } else {
                boostIcon.setOpacity(0.4);
            }
        });
        return boostIcon;
    }

    private WindowLabel boostNum() {
        WindowLabel boostLabel = new WindowLabel(String.valueOf(inventory.getNumSpeedBoostsWaiting()), 15);
        boostLabel.setMinWidth(20);
        boostLabel.setAlignment(Pos.CENTER);
        boostLabel.setTextFill(Color.web("#FFFFFF"));
        inventory.numSpeedBoostsWaitingProperty().addListener((o, oldVal, newVal) -> {
            boostLabel.setText(String.valueOf(inventory.getNumSpeedBoostsWaiting()));
        });
        return boostLabel;
    }

    private ImageView blueKey() {
        String path = "images/mapItems/blue_key.png";
        ImageView blueKey = new ImageView(ResourceManager.getImage(path, 30, 30, true, true));
        blueKey.setOpacity(0.4);
        inventory.blueProperty().addListener((o, oldVal, newVal) -> {
            if (inventory.getBlueKey()) {
                blueKey.setOpacity(1.0);
            }
        });
        return blueKey;
    }

    private ImageView greenKey() {
        String path = "images/mapItems/green_key.png";
        ImageView greenKey = new ImageView(ResourceManager.getImage(path, 30, 30, true, true));
        greenKey.setOpacity(0.4);
        inventory.greenProperty().addListener((o, oldVal, newVal) -> {
            if (inventory.getGreenKey()) {
                greenKey.setOpacity(1.0);
            }
        });
        return greenKey;
    }

    private ImageView yellowKey() {
        String path = "images/mapItems/yellow_key.png";
        ImageView yellowKey = new ImageView(ResourceManager.getImage(path, 30, 30, true, true));
        yellowKey.setOpacity(0.4);
        inventory.yellowProperty().addListener((o, oldVal, newVal) -> {
            if (inventory.getYellowKey()) {
                yellowKey.setOpacity(1.0);
            }
        });
        return yellowKey;
    }
}
