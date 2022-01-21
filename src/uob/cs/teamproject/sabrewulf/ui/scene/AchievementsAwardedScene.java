package uob.cs.teamproject.sabrewulf.ui.scene;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import uob.cs.teamproject.sabrewulf.ResourceManager;
import uob.cs.teamproject.sabrewulf.achievements.AchievementElement;
import uob.cs.teamproject.sabrewulf.achievements.StatisticsTracker;
import uob.cs.teamproject.sabrewulf.audio.Audio;
import uob.cs.teamproject.sabrewulf.network.NetworkSystem;
import uob.cs.teamproject.sabrewulf.ui.templates.TextButton;
import uob.cs.teamproject.sabrewulf.ui.templates.WindowLabel;

import java.util.ArrayList;
import java.util.Set;

import static javafx.geometry.Pos.CENTER;
import static javafx.geometry.Pos.TOP_CENTER;

/**
 *  AchievementsAwardedScene creates a new {@link VBox} which displays the achievements which a user has been awarded
 *   whilst playing a single player game. It is displayed once a game terminates and displays a container with the list
 *  of achievements awarded to a player during the game. AchievementsAwardedScene may not be displayed if a player does
 *  not get awarded any achievements whilst playing a game.
 */
public class AchievementsAwardedScene extends VBox {

    /* game systems */
    private final Audio audioSystem;
    private final NetworkSystem networkSystem;
    private final StatisticsTracker statisticsTracker;

    /* the application window, needed here so that it can be passed to the FinalScoreScene */
    private final Stage mainStage;
    /* JavaFX container which contains the menu scenes */
    private final StackPane menuRoot;

    private TableView<AchievementElement> table;
    private final FinalScoreScene.COMPLETIONTYPE completionType;
    private final ArrayList<AchievementElement> achievements;

    /**
     * @param audioSystem {@link Audio} used to play button press FX
     * @param networkSystem {@link NetworkSystem} passed to {@link FinalScoreScene} which successes this scene
     * @param statisticsTracker {@link StatisticsTracker} used to track game progress
     * @param mainStage the main application window. Required here so that 'close' buttons can close the window
     * @param menuRoot the JavaFX container which contains the menu scenes
     * @param gainedAchievements list of achievements which a player has gained during a game
     * @param completionType {@link FinalScoreScene.COMPLETIONTYPE} way in which game ended eg. play quit, player
     *                                                             ran out of lives
     */
    public AchievementsAwardedScene(Audio audioSystem, NetworkSystem networkSystem, StatisticsTracker statisticsTracker,
                                    Stage mainStage, StackPane menuRoot,
                                    Set<String> gainedAchievements, FinalScoreScene.COMPLETIONTYPE completionType) {
        this.audioSystem = audioSystem;
        this.networkSystem = networkSystem;
        this.statisticsTracker = statisticsTracker;
        this.menuRoot = menuRoot;
        this.mainStage = mainStage;
        this.completionType = completionType;
        achievements = new ArrayList<AchievementElement>();
        for(String achievement : gainedAchievements) {
            AchievementElement achievementElement = new AchievementElement(achievement, true);
            achievements.add(achievementElement);
        }
        this.setSpacing(20);
        this.setAlignment(TOP_CENTER);
        this.setPrefWidth(700);
        this.setPrefHeight(500);
        this.setMaxWidth(700);
        this.setMaxHeight(500);
        Image backgroundImage = ResourceManager.getImage("images/uielements/container_fullheader.png",
                700,
                500,
                true,
                true);
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false));
        this.setBackground(new Background(background));
        HBox hBox = new HBox();
        hBox.setAlignment(TOP_CENTER);
        hBox.setPrefWidth(700);
        hBox.setPrefHeight(75);
        hBox.getChildren().add(screenTitle());
        this.getChildren().addAll(hBox, achievementsAwardedDisplay(), buttonOptions());
    }

    /** Displays the title of the scene using a {@link HBox}. */
    private HBox screenTitle() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(CENTER);
        hBox.setPrefWidth(700);
        hBox.setPrefHeight(75);
        WindowLabel titleText = new WindowLabel("New Achievements", 25);
        hBox.getChildren().add(titleText);
        return hBox;
    }

    /** Displays the achievements awarded during a game using a {@link VBox}. */
    private VBox achievementsAwardedDisplay() {
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setAlignment(CENTER);
        table = new TableView<>();
        table.getStylesheets().add("uob/cs/teamproject/sabrewulf/ui/style/achievements.css");
        table.setMaxSize(600, 240);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<AchievementElement, String> achievementColumn = new TableColumn<>("Achievement");
        achievementColumn.setCellValueFactory(new PropertyValueFactory<>("achievement"));
        table.getColumns().addAll(achievementColumn);
        for (int i = 0; i< achievements.size(); i++) {
            if(achievements.get(i).getAchieved()) {
                table.getItems().add(achievements.get(i));
            }
        }
        vBox.getChildren().addAll(table);
        return vBox;
    }

    /** Displays the next button at the bottom of the scene using a {@link HBox}. */
    private VBox buttonOptions() {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(CENTER);
        TextButton nextButton = new TextButton("Next", 15, audioSystem);
        nextButton.setOnAction(actionEvent -> {
            menuRoot.getChildren().remove(this);
            menuRoot.getChildren().add(new FinalScoreScene(audioSystem, networkSystem, statisticsTracker,
                    mainStage, menuRoot, completionType));
        });
        hBox.getChildren().add(nextButton);
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setAlignment(CENTER);
        vBox.getChildren().add(hBox);
        return vBox;
    }
}
