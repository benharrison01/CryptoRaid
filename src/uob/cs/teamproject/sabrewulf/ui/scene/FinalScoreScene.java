package uob.cs.teamproject.sabrewulf.ui.scene;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import uob.cs.teamproject.sabrewulf.achievements.StatisticsTracker;
import javafx.stage.Stage;
import uob.cs.teamproject.sabrewulf.audio.Audio;
import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.ResourceManager;
import uob.cs.teamproject.sabrewulf.leaderboards.LeaderboardElement;
import uob.cs.teamproject.sabrewulf.leaderboards.StoredLeaderboard;
import uob.cs.teamproject.sabrewulf.network.NetworkSystem;
import uob.cs.teamproject.sabrewulf.ui.fields.UsernameField;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODE;
import uob.cs.teamproject.sabrewulf.ui.templates.TextButton;
import uob.cs.teamproject.sabrewulf.ui.templates.WindowLabel;
import uob.cs.teamproject.sabrewulf.components.Inventory;
import java.time.LocalDateTime;
import java.util.Map;
import static javafx.geometry.Pos.CENTER;

/** Final score screen creates a new {@link VBox} which contains all of the contents to be displayed on the screen
 *  when the player completes the game, either by winning/losing, running out of lives or quitting the game
 */
public class FinalScoreScene extends VBox {

    public enum COMPLETIONTYPE {SUCCESS, WIN, LOSE, OUTOFLIVES, QUIT}
    private enum BUTTONTYPE {ALL, REDUCED}
    private final COMPLETIONTYPE completionType;
    private final Audio audioSystem;
    private final NetworkSystem networkSystem;
    private final StatisticsTracker statisticsTracker;
    private final StoredLeaderboard leaderboard = new StoredLeaderboard(GameSettings.getDifficulty());
    private LeaderboardElement entry;
    private UsernameField field;
    private final Inventory inventory;
    private final Stage mainStage;
    private final StackPane menuRoot;
    private TextButton submitName;

    /** Constructor creates a new instance of the final score screen and sets the layout of the screen and
     *  adds content to the screen
     * @param audioSystem - {@link Audio} system
     * @param networkSystem - {@link NetworkSystem}
     * @param statisticsTracker - Statistics about single player game
     * @param completiontype - Completion type to be displayed on screen
     * @param menuRoot - the {@link StackPane} which contains the menu controls
     * @param mainStage the main application window. This will be closed when this scene's 'close' button is clicked.
     */
    public FinalScoreScene(Audio audioSystem, NetworkSystem networkSystem, StatisticsTracker statisticsTracker,
                           Stage mainStage, StackPane menuRoot,
                           COMPLETIONTYPE completiontype) {
        this.audioSystem = audioSystem;
        this.networkSystem = networkSystem;
        this.statisticsTracker = statisticsTracker;
        this.mainStage = mainStage;
        this.menuRoot = menuRoot;
        this.completionType = completiontype;
        this.inventory = GameSettings.getInventory();
        this.setSpacing(5);
        this.setAlignment(CENTER);
        this.setPrefWidth(700);
        this.setPrefHeight(500);
        this.setMaxWidth(700);
        this.setMaxHeight(500);
        String path = "images/uielements/container_fullheader.png";
        Image backgroundImage = ResourceManager.getImage(path, 700, 500, true, true);
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false));
        this.setBackground(new Background(background));
        HBox hBox = new HBox();
        hBox.setAlignment(CENTER);
        hBox.setPrefWidth(700);
        hBox.setPrefHeight(75);
        hBox.getChildren().addAll(screenTitle());
        this.getChildren().addAll(hBox, contentContainer());
    }

    private HBox screenTitle() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(CENTER);
        hBox.setPrefWidth(700);
        hBox.setPrefHeight(75);
        WindowLabel titleText = new WindowLabel("Summary", 25);
        hBox.getChildren().add(titleText);
        return hBox;
    }

    private VBox contentContainer(){
        VBox vBox = new VBox();
        vBox.setSpacing(30);
        vBox.setAlignment(CENTER);
        vBox.setPrefWidth(700);
        vBox.setPrefHeight(425);
        if (GameSettings.getGameMode() == MODE.SINGLEPLAYER && completionType != COMPLETIONTYPE.QUIT) {
            HBox singleplayerUsername = singleplayerUsername();
            VBox playerScore = playerScore();
            VBox buttonOptions = buttonOptions(BUTTONTYPE.REDUCED);
            HBox finishConfirmation = finishConfirmation();
            vBox.getChildren().addAll(finishConfirmation, playerScore, singleplayerUsername, buttonOptions);
            field.getField().setOnMouseClicked(mouseEvent -> field.getField().setEditable(true));
            submitName.setOnAction(actionEvent -> {
                if (!field.getField().getText().isEmpty()) {
                    GameSettings.setUsername(field.getUsername());
                    addToLeaderboard();
                    vBox.getChildren().removeAll(finishConfirmation, singleplayerUsername, playerScore, buttonOptions);
                    vBox.getChildren().addAll(finishConfirmation(), playerScore());
                    vBox.getChildren().addAll(leaderboardPosition(), buttonOptions(BUTTONTYPE.ALL));
                }
                });
        } else {
            vBox.getChildren().addAll(finishConfirmation(), playerScore(), buttonOptions(BUTTONTYPE.ALL));
        }
        return vBox;
    }

    private HBox singleplayerUsername() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(CENTER);
        field = new UsernameField();
        field.getField().setEditable(false);
        hBox.getChildren().addAll(field);
        return hBox;
    }

    private HBox finishConfirmation() {
        HBox hBox = new HBox();
        hBox.setSpacing(25);
        hBox.setAlignment(CENTER);
        WindowLabel completionReason;
        if (completionType == COMPLETIONTYPE.SUCCESS) {
            completionReason = new WindowLabel("Nice work, game complete", 25);
        } else if (completionType == COMPLETIONTYPE.OUTOFLIVES) {
            completionReason = new WindowLabel("Unlucky, you ran out of lives", 25);
        } else if (completionType == COMPLETIONTYPE.WIN) {
            addToLeaderboard();
            completionReason = new WindowLabel("Well done, you won the game", 25);
        } else if (completionType == COMPLETIONTYPE.LOSE) {
            addToLeaderboard();
            completionReason = new WindowLabel("Unlucky, you didn't win this time", 25);
        } else {
            if (GameSettings.getGameMode() == MODE.SINGLEPLAYER) {
                completionReason = new WindowLabel("Game over, you quit", 25);
            } else {
                completionReason = new WindowLabel("Game over, somebody quit", 25);
            }
        }
        if (completionType == COMPLETIONTYPE.SUCCESS || completionType == COMPLETIONTYPE.WIN) {
            ImageView balloons1 = new ImageView(ResourceManager.getImage("images/uielements/balloons1.png", 60, 90, true, true));
            ImageView balloons2 = new ImageView(ResourceManager.getImage("images/uielements/balloons2.png", 60, 90, true, true));
            hBox.getChildren().addAll(balloons1, completionReason, balloons2);
        } else if (completionType == COMPLETIONTYPE.OUTOFLIVES) {
            ImageView skull = new ImageView(ResourceManager.getImage("images/uielements/skull.png", 40, 70, true, true));
            ImageView skull2 = new ImageView(ResourceManager.getImage("images/uielements/skull.png", 40, 70, true, true));
            hBox.getChildren().addAll(skull, completionReason, skull2);
        }
        else {
            hBox.getChildren().addAll(completionReason);
        }
        return hBox;
    }

    private void addToLeaderboard() {
        if (GameSettings.getGameMode() == MODE.SINGLEPLAYER) {
            entry = new LeaderboardElement(GameSettings.getUsername(), inventory.getScore(), LocalDateTime.now());
            leaderboard.insertScore(entry);
        } else {
            for (Map.Entry<String, Integer> i : networkSystem.getScores().entrySet()) {
                entry = new LeaderboardElement(i.getKey(), i.getValue(), LocalDateTime.now());
                leaderboard.insertScore(entry);
            }
        }
    }

    private VBox playerScore() {
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setAlignment(CENTER);
        WindowLabel scoreLabel = new WindowLabel("You scored " + inventory.getScore() + " points", 15);
        vBox.getChildren().addAll(scoreLabel);
        if (GameSettings.getGameMode() == MODE.MULTIPLAYER && completionType != COMPLETIONTYPE.QUIT) {
            for (Map.Entry<String, Integer> i : networkSystem.getScores().entrySet()) {
                if (!i.getKey().equals(GameSettings.getUsername())) {
                    WindowLabel otherScore = new WindowLabel(i.getKey() + " scored " + i.getValue() + " points", 15);
                    vBox.getChildren().add(otherScore);
                }
            }
        }
        return vBox;
    }

    private HBox leaderboardPosition() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(CENTER);
        WindowLabel leaderboardLabel;
        if (leaderboard.getElements().contains(entry)) {
            int position = leaderboard.getElements().indexOf(entry) + 1;
            int lastDigit = Math.abs(position) % 10;
            String postfix;
            if (lastDigit == 1 && position != 11) {
                postfix = "st";
            } else if (lastDigit == 2 && position != 12) {
                postfix = "nd";
            } else if (lastDigit == 3 && position != 13) {
                postfix = "rd";
            } else {
                postfix = "th";
            }
            leaderboardLabel = new WindowLabel("You've placed " +position+postfix+ " on the leaderboard", 15);
        } else {
            leaderboardLabel = new WindowLabel("You haven't made it onto the leaderboard", 15);
        }
        hBox.getChildren().addAll(leaderboardLabel);
        return hBox;
    }

    private VBox buttonOptions(BUTTONTYPE buttontype) {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(CENTER);
        TextButton homeButton = new TextButton("Main menu", 15, audioSystem);
        homeButton.setOnAction(actionEvent -> {
            menuRoot.getChildren().remove(this);
            GameSettings.getGameStateOwner().returnToMenu();
        });
        TextButton playAgainButton = new TextButton("Play again", 15, audioSystem);
        playAgainButton.setOnAction(actionEvent -> {
            menuRoot.getChildren().remove(this);
            GameSettings.getGameStateOwner().restartGame();
        });
        TextButton leaderboardButton = new TextButton("Leaderboard", 15, audioSystem);
        leaderboardButton.setOnAction(actionEvent -> {
            LeaderboardScene leaderboardScene = new LeaderboardScene(audioSystem, menuRoot);
            leaderboardScene.removeSelectorButtons();
            menuRoot.getChildren().add(leaderboardScene);
        });
        if (buttontype == BUTTONTYPE.ALL) {
            if (GameSettings.getGameMode() == MODE.SINGLEPLAYER) {
                hBox.getChildren().addAll(homeButton, playAgainButton, leaderboardButton);
            } else {
                hBox.getChildren().addAll(homeButton, leaderboardButton);
            }
        } else {
            submitName = new TextButton("Submit", 15, audioSystem);
            hBox.getChildren().add(submitName);
        }
        HBox hBox2 = new HBox();
        hBox2.setSpacing(10);
        hBox2.setAlignment(CENTER);
        TextButton achievementsButton = new TextButton("Achievements", 15, audioSystem);
        achievementsButton.setOnAction(actionEvent -> {
            menuRoot.getChildren().add(
                    new AchievementsScene(audioSystem, menuRoot, statisticsTracker.getAchievementElements())
            );
        });
        TextButton creditsButton = new TextButton("Credits", 15, audioSystem);
        creditsButton.setOnAction(actionEvent -> menuRoot.getChildren().add(new CreditsScene(audioSystem, menuRoot)));
        TextButton closeButton = new TextButton("Close", 15, audioSystem);
        closeButton.setOnAction(actionEvent -> mainStage.close());
        if (buttontype == BUTTONTYPE.ALL) {
            if (GameSettings.getGameMode() == MODE.SINGLEPLAYER) {
                hBox2.getChildren().addAll(achievementsButton, creditsButton, closeButton);
            } else {
                hBox2.getChildren().addAll(creditsButton, closeButton);
            }
        } else {
            hBox2.getChildren().addAll(homeButton, playAgainButton, closeButton);
        }
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setAlignment(CENTER);
        vBox.getChildren().addAll(hBox, hBox2);
        return vBox;
    }
}
