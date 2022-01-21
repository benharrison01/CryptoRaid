package uob.cs.teamproject.sabrewulf.ui.scene;

import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import uob.cs.teamproject.sabrewulf.ResourceManager;
import uob.cs.teamproject.sabrewulf.achievements.AchievementElement;
import uob.cs.teamproject.sabrewulf.audio.Audio;
import uob.cs.teamproject.sabrewulf.ui.templates.TextButton;
import uob.cs.teamproject.sabrewulf.ui.templates.TextSection;
import uob.cs.teamproject.sabrewulf.ui.templates.WindowLabel;

import java.util.ArrayList;

import static javafx.geometry.Pos.*;

/** Achievement Display creates a new {@link VBox} which displays the list of achievements which a user has been
 *  awarded on the user interface. It displays a container with the scene title, return button and a content container
 *  which contains the list of achievements, formatted as a table. The order of the list of achievements can be adjusted
 *  as all Columns are able to be ordered in ascending or descending order.
 */
public class AchievementsScene extends VBox {

    private final Audio audio;
    Integer sumAchievements;
    private TableView<AchievementElement> table;
    private HBox selectorButtons;
    private final VBox contentContainer;
    private final StackPane menuRoot;
    private ArrayList<AchievementElement> achievementElements;

    /**
     * @param audio - {@link Audio} system for button click FX
     * @param achievementElements - list of achievements which a player has been awarded
     * @param menuRoot - the {@link StackPane} which contains the menu controls
     */
    public AchievementsScene(Audio audio, StackPane menuRoot, ArrayList<AchievementElement> achievementElements) {
        this.audio = audio;
        this.menuRoot = menuRoot;
        this.achievementElements = achievementElements;
        this.setSpacing(5);
        this.setAlignment(CENTER);
        this.setPrefWidth(700);
        this.setPrefHeight(500);
        this.setMaxWidth(700);
        this.setMaxHeight(500);
        this.sumAchievements = 0;
        String path = "images/uielements/container_shortheader.png";
        Image backgroundImage = ResourceManager.getImage(path, 700, 500, true, true);
        BackgroundSize size = new BackgroundSize(1.0, 1.0, true, true, false, false);
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                size);
        this.setBackground(new Background(background));
        contentContainer = contentContainer();
        this.getChildren().addAll(header(), contentContainer);
    }

    /** Displays the title and return button at the top of the scene using a {@link HBox}. */
    private HBox header() {
        HBox hBox = new HBox();
        hBox.setAlignment(CENTER);
        hBox.setPrefWidth(700);
        hBox.setPrefHeight(75);
        hBox.getChildren().addAll(screenTitle(), returnButton());
        return hBox;
    }

    /** Displays the title of the scene using a {@link HBox}. */
    private HBox screenTitle() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(CENTER_LEFT);
        hBox.setPrefWidth(350);
        hBox.setPrefHeight(75);
        WindowLabel titleText = new WindowLabel("Achievements", 25);
        HBox.setMargin(titleText, new Insets(0, 0, 0, 40));
        hBox.getChildren().addAll(titleText);
        return hBox;
    }


    /** Displays a table of all the achievements which the player has been awarded, how many of these achievements
     * there are and the player's subsequent rank.
     * embedded in a {@link HBox}. */
    private VBox contentContainer(){
        VBox contentContainer = new VBox();
        contentContainer.setSpacing(15);
        contentContainer.setAlignment(CENTER);
        contentContainer.setPrefWidth(700);
        contentContainer.setPrefHeight(425);
        selectorButtons = selectorButtons();
        contentContainer.getChildren().addAll(selectorButtons,
                achievementDisplay(),
                numberOfAchievements(),
                playerRank());
        VBox.setMargin(selectorButtons, new Insets(10,0,0,0));
        return contentContainer;
    }

    /** Displays the return {@link TextButton} using a {@link HBox}. */
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
        returnButton.setOnAction(actionEvent -> menuRoot.getChildren().remove(this));
        return hBox;
    }

    /** Selector {@link TextButton}s to select which type of achievements are displayed in the {@link TableView} using
     * a {@link HBox}. */
    private HBox selectorButtons() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(CENTER);
        TextButton achievedButton = new TextButton("Achieved", 15, audio);
        TextButton incompleteButton = new TextButton("Incomplete", 15, audio);
        achievedButton.setButtonPressed();

        achievedButton.setOnAction(actionEvent -> {
            achievedButton.setButtonPressed();
            incompleteButton.setButtonFree();
            showAchieved();
        });
        incompleteButton.setOnAction(actionEvent -> {
            incompleteButton.setButtonPressed();
            achievedButton.setButtonFree();
            showIncomplete();
        });
        hBox.getChildren().addAll(achievedButton, incompleteButton);
        return hBox;
    }

    /** Displays all the achievements awarded to a player using a {@link TableView} embedded within a {@link VBox}. */
    private VBox achievementDisplay() {
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setAlignment(CENTER);
        table = new TableView<>();
        table.getStylesheets().add("uob/cs/teamproject/sabrewulf/ui/style/achievements.css");
        table.setMaxSize(600, 240);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<AchievementElement, String> achievementColumn = new TableColumn<>("Achievement");
        achievementColumn.setCellValueFactory(new PropertyValueFactory<>("achievement"));
        TableColumn<AchievementElement, Integer> ordinalColumn = new TableColumn<>("Ordinal");
        ordinalColumn.setCellValueFactory(new PropertyValueFactory<>("ordinal"));
        table.getColumns().addAll(achievementColumn,ordinalColumn);
        ordinalColumn.setVisible(false);
        for (AchievementElement achievementElement : achievementElements) {
            if (achievementElement.getAchieved()) {
                table.getItems().add(achievementElement);
            }
        }
        vBox.getChildren().addAll(table);
        return vBox;
    }

    /** Calculates and displays the number of achievements which a player has been awarded using a {@link HBox}. */
    private HBox numberOfAchievements() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(CENTER);
        hBox.setPrefWidth(700);
        for(AchievementElement achievement : achievementElements) {
            if(achievement.getAchieved()) { sumAchievements++; }
        }
        TextSection sumLabel = new TextSection(sumAchievements + "/45 achievements awarded ", 15);
        hBox.getChildren().addAll(sumLabel);
        return hBox;
    }

    /** Calculates and displays the rank of the player, based on the number of achievements which they have been awarded.
     * This is displayed using a {@link HBox}. */
    private HBox playerRank() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(CENTER);
        hBox.setPrefWidth(700);
        int rankNumber;
        String rank;
        String rankMessage;
        if (sumAchievements < 15) {
            rankNumber = 1;
            rank = "WANNABE H4X0R";
            rankMessage = "GET RAIDING KID!!";
        } else if (sumAchievements < 35) {
            rankNumber = 2;
            rank = "EDWARD SNOWDON JR";
            rankMessage = "YOU'RE GETTING GOOD AT THIS..";
        } else {
            rankNumber = 3;
            rank = "JULIAN ASSANGE";
            rankMessage = "WHAT A PRO!";
        }
        TextSection rankLabel = new TextSection("You are rank " + rankNumber + "/3 : " + rank + ". " + rankMessage,
                15);
        hBox.getChildren().addAll(rankLabel);
        HBox.setMargin(rankLabel, new Insets(0,0,20,0));
        return hBox;
    }

    /** Compiles a list of achievements which a player has been awarded. */
    private void showAchieved() {
        table.getItems().clear();
        for (AchievementElement achievementElement : achievementElements) {
            if (achievementElement.getAchieved()) {
                table.getItems().add(achievementElement);
            }
        }
    }

    /** Compiles a list of achievements which a player has yet to be awarded. */
    private void showIncomplete() {
        table.getItems().clear();
        for (AchievementElement achievementElement : achievementElements) {
            if (!achievementElement.getAchieved()) {
                table.getItems().add(achievementElement);
            }
        }
    }
}
