package uob.cs.teamproject.sabrewulf.ui.scene;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import uob.cs.teamproject.sabrewulf.audio.Audio;
import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.ResourceManager;
import uob.cs.teamproject.sabrewulf.ui.selectors.DIFFICULTY;
import uob.cs.teamproject.sabrewulf.ui.selectors.DifficultySelector;
import uob.cs.teamproject.sabrewulf.ui.templates.TextButton;
import uob.cs.teamproject.sabrewulf.ui.templates.WindowLabel;
import uob.cs.teamproject.sabrewulf.leaderboards.LeaderboardElement;
import uob.cs.teamproject.sabrewulf.leaderboards.StoredLeaderboard;
import static javafx.geometry.Pos.*;

/** Leaderboard Display creates a new {@link VBox} which displays the leaderboard for the game on the user
 *  interface. It displays a container with the scene title, return button and a content container which
 *  contains the leaderboard, formatted as a table in numerical order
 */
public class LeaderboardScene extends VBox {

    private final Audio audio;
    private final StoredLeaderboard leaderboard;
    private TableView<LeaderboardElement> table;
    private HBox selectorButtons;
    private final VBox contentContainer;
    private final StackPane menuRoot;

    /** Constructor - Creates a new instance of the leaderboard display
     * @param audio - {@link Audio} system for button click FX
     * @param menuRoot - the {@link StackPane} which contains the menu controls
     */
    public LeaderboardScene(Audio audio, StackPane menuRoot) {
        this.audio = audio;
        this.menuRoot = menuRoot;
        if (GameSettings.getDifficulty() == null) {
            this.leaderboard = new StoredLeaderboard(DIFFICULTY.EASY);
        } else {
            this.leaderboard = new StoredLeaderboard(GameSettings.getDifficulty());
        }
        this.setSpacing(5);
        this.setAlignment(CENTER);
        this.setPrefWidth(700);
        this.setPrefHeight(500);
        this.setMaxWidth(700);
        this.setMaxHeight(500);
        String path = "images/uielements/container_shortheader.png";
        Image backgroundImage = ResourceManager.getImage(path, 700, 500, true, true);
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false));
        this.setBackground(new Background(background));
        contentContainer = contentContainer();
        this.getChildren().addAll(header(), contentContainer);
    }

    /** Removes the {@link DifficultySelector} buttons from the leaderboard scene so that they are no longer
     *  displayed
     */
    public void removeSelectorButtons() {
        this.contentContainer.getChildren().remove(selectorButtons);
    }

    private HBox header() {
        HBox hBox = new HBox();
        hBox.setAlignment(CENTER);
        hBox.setPrefWidth(700);
        hBox.setPrefHeight(75);
        hBox.getChildren().addAll(screenTitle(), returnButton());
        return hBox;
    }

    private HBox screenTitle() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(CENTER_LEFT);
        hBox.setPrefWidth(350);
        hBox.setPrefHeight(75);
        WindowLabel titleText = new WindowLabel("Leaderboard", 25);
        HBox.setMargin(titleText, new Insets(0, 0, 0, 50));
        hBox.getChildren().addAll(titleText);
        return hBox;
    }

    private VBox contentContainer(){
        VBox contentContainer = new VBox();
        contentContainer.setSpacing(10);
        contentContainer.setAlignment(CENTER);
        contentContainer.setPrefWidth(700);
        contentContainer.setPrefHeight(425);
        selectorButtons = selectorButtons();
        contentContainer.getChildren().addAll(selectorButtons, leaderboardDisplay());
        return contentContainer;
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
        returnButton.setOnAction(actionEvent -> menuRoot.getChildren().remove(this));
        return hBox;
    }

    private HBox selectorButtons() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(CENTER);
        TextButton easyButton = new TextButton("Easy", 20, audio);
        TextButton mediumButton = new TextButton("Medium", 20, audio);
        TextButton hardButton = new TextButton("Hard", 20, audio);
        if (GameSettings.getDifficulty() == null || GameSettings.getDifficulty() == DIFFICULTY.EASY) {
            easyButton.setButtonPressed();
        } else if (GameSettings.getDifficulty() == DIFFICULTY.MEDIUM) {
            mediumButton.setButtonPressed();
        } else {
            hardButton.setButtonPressed();
        }
        easyButton.setOnAction(actionEvent -> {
            easyButton.setButtonPressed();
            mediumButton.setButtonFree();
            hardButton.setButtonFree();
            leaderboard.switchLeaderboard(leaderboard, DIFFICULTY.EASY);
            updateTable();
        });
        mediumButton.setOnAction(actionEvent -> {
            mediumButton.setButtonPressed();
            easyButton.setButtonFree();
            hardButton.setButtonFree();
            leaderboard.switchLeaderboard(leaderboard, DIFFICULTY.MEDIUM);
            updateTable();
        });
        hardButton.setOnAction(actionEvent -> {
            hardButton.setButtonPressed();
            easyButton.setButtonFree();
            mediumButton.setButtonFree();
            leaderboard.switchLeaderboard(leaderboard, DIFFICULTY.HARD);
            updateTable();
        });
        hBox.getChildren().addAll(easyButton, mediumButton, hardButton);
        return hBox;
    }

    private VBox leaderboardDisplay() {
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setAlignment(CENTER);
        table = new TableView<>();
        table.getStylesheets().add("uob/cs/teamproject/sabrewulf/ui/style/leaderboard.css");
        table.setMaxSize(600, 300);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<LeaderboardElement, Integer> rankingColumn = new TableColumn<>("Ranking");
        rankingColumn.setCellValueFactory(data -> {
            return new ReadOnlyObjectWrapper<>(table.getItems().indexOf(data.getValue()) + 1);
        });
        rankingColumn.setSortable(false);
        TableColumn<LeaderboardElement, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setSortable(false);
        TableColumn<LeaderboardElement, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        scoreColumn.setSortable(false);
        TableColumn<LeaderboardElement, String> dtfColumn = new TableColumn<>("Time");
        dtfColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        dtfColumn.setSortable(false);
        table.getColumns().addAll(rankingColumn, nameColumn, scoreColumn, dtfColumn);
        for (int i=0; i<leaderboard.getElements().size(); i++) {
            table.getItems().add(leaderboard.getElements().get(i));
        }
        vBox.getChildren().addAll(table);
        return vBox;
    }

    private void updateTable() {
        table.getItems().clear();
        for (int i=0; i<leaderboard.getElements().size(); i++) {
            table.getItems().add(leaderboard.getElements().get(i));
        }
    }
}
