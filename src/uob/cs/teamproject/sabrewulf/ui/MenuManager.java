package uob.cs.teamproject.sabrewulf.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import uob.cs.teamproject.sabrewulf.achievements.StatisticsTracker;
import javafx.stage.Stage;
import uob.cs.teamproject.sabrewulf.audio.Audio;
import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.ResourceManager;
import uob.cs.teamproject.sabrewulf.exceptions.UsernameUnavailableException;
import uob.cs.teamproject.sabrewulf.input.Input;
import uob.cs.teamproject.sabrewulf.rendering.Renderer;
import uob.cs.teamproject.sabrewulf.ui.scene.*;
import uob.cs.teamproject.sabrewulf.ui.selectors.*;
import uob.cs.teamproject.sabrewulf.ui.templates.TextButton;
import uob.cs.teamproject.sabrewulf.ui.templates.WindowLabel;
import uob.cs.teamproject.sabrewulf.network.NetworkSystem;

import java.io.IOException;
import java.net.PortUnreachableException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import static javafx.geometry.Pos.*;

/** Menu manager creates all new menu's (both main menu's and in-game menu's) which are used in the game and
 *  contains functionality which controls the contents and appearance of these two menu's
 */
public class MenuManager {
    /** Data type for specifying the menu type
     */
    public enum MENUTYPE {MAIN, INGAME}
    private MENUTYPE type;
    private VBox menu;

    /* game systems */
    private final Audio audio;
    private final Input inputSystem;
    private final NetworkSystem networkSystem;
    private final Renderer renderer;
    private final StatisticsTracker statisticsTracker;

    /* the Stage representing the application window */
    private final Stage mainStage;
    /* the root of the application window */
    private final StackPane menuRoot;

    List<GameModeSelector> modes = new ArrayList<>();
    List<DifficultySelector> difficulties = new ArrayList<>();
    List<WindowSelector> windowSelectors = new ArrayList<>();

    /**
     * Creates a new MenuManager instance
     * @param audio the {@link Audio} system
     * @param renderer the {@link Renderer}
     * @param networkSystem the {@link NetworkSystem}
     * @param inputSystem the {@link Input} system
     * @param statisticsTracker the {@link StatisticsTracker} system
     * @param mainStage the {@link Stage} representing the application window
     * @param menuRoot the {@link StackPane} at the root of the application window
     */
    public MenuManager(Audio audio, Input inputSystem, NetworkSystem networkSystem, Renderer renderer,
                       StatisticsTracker statisticsTracker,
                       Stage mainStage, StackPane menuRoot) {
        this.audio = audio;
        this.inputSystem = inputSystem;
        this.networkSystem = networkSystem;
        this.renderer = renderer;
        this.statisticsTracker = statisticsTracker;
        this.mainStage = mainStage;
        this.menuRoot = menuRoot;
    }

    /** Creates a new menu when a new game is launched
     * @param type - Type of menu (main or in-game)
     */
    public void createStartMenu(MENUTYPE type) {
        this.type = type;
        createBackground();
        menuRoot.getChildren().add(menu());
        mainStage.setFullScreen(GameSettings.getWindowSize() == WINDOWSIZE.FULLSCREEN);
        mainStage.show();
    }

    /** Creates a new menu whilst a current game is already underway
     * @param type - Type of menu
     */
    public void createNewMenu(MENUTYPE type) {
        this.type = type;
        menu = menu();
        menuRoot.getChildren().add(menu);
        mainStage.getScene().setRoot(menuRoot);
        mainStage.setFullScreen(GameSettings.getWindowSize() == WINDOWSIZE.FULLSCREEN);
    }

    private void createBackground() {
        Image backgroundImage = ResourceManager.getImage("images/uielements/background.png", 800, 600,false,true);
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false));
        menuRoot.setBackground(new Background(background));
    }

    private VBox menu(){
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setAlignment(CENTER);
        vBox.setPrefWidth(700);
        vBox.setPrefHeight(500);
        vBox.setMaxWidth(700);
        vBox.setMaxHeight(500);
        String path = "images/uielements/container_fullheader.png";
        Image backgroundImage = ResourceManager.getImage(path, 700, 500, true, true);
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false));
        vBox.setBackground(new Background(background));
        vBox.getChildren().addAll(titlePane(), menuConfig());
        return vBox;
    }

    /**
     * @return Returns game title, used in Main Menu
     */
    private HBox titlePane(){
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(CENTER);
        hBox.setPrefWidth(700);
        hBox.setPrefHeight(75);
        WindowLabel gameTitle = new WindowLabel("CryptoRaid", 30);
        hBox.getChildren().add(gameTitle);
        return hBox;
    }

    /**
     * @return Returns structure for Main Menu and In Game menu (left column and right column)
     */
    private HBox menuConfig(){
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        hBox.setAlignment(BOTTOM_CENTER);
        hBox.setPrefWidth(700);
        hBox.setPrefHeight(425);
        hBox.getChildren().addAll(leftColumn(), rightColumn());
        return hBox;
    }

    /**
     * @return Returns the left column for menu's, content varies depending on Menu Mode (Main or Settings menu)
     */
    private VBox leftColumn() {
        VBox leftBox = new VBox();
        leftBox.setPrefWidth(300);
        leftBox.setPrefHeight(300);
        leftBox.setSpacing(10);
        leftBox.setAlignment(CENTER);
        if (type == MENUTYPE.MAIN) {
            VBox selectorColumn = new VBox();
            selectorColumn.setPrefWidth(300);
            selectorColumn.setPrefHeight(300);
            selectorColumn.setSpacing(10);
            selectorColumn.setAlignment(CENTER);
            TextButton leaderboardButton = new TextButton("Leaderboard", 15, audio);
            leaderboardButton.setOnAction(actionEvent -> {
                menuRoot.getChildren().add(new LeaderboardScene(audio, menuRoot));
            });
            TextButton achievementsButton = new TextButton("Achievements", 15, audio);
            achievementsButton.setOnAction(actionEvent ->
                    menuRoot.getChildren().add(new AchievementsScene(audio, menuRoot,
                            statisticsTracker.getAchievementElements()))
            );
            VBox.setMargin(achievementsButton, new Insets(0, 0, 4, 0));
            selectorColumn.getChildren().addAll(difficultySelector(), modeSelector(), startButton());
            selectorColumn.getChildren().addAll(leaderboardButton, achievementsButton);
            leftBox.getChildren().add(selectorColumn);
        }
        if (type == MENUTYPE.INGAME) {
            VBox inGameColumn = new VBox();
            inGameColumn.setPrefWidth(300);
            inGameColumn.setPrefHeight(300);
            inGameColumn.setSpacing(20);
            inGameColumn.setAlignment(CENTER);
            WindowLabel gameDifficultyText = new WindowLabel("Difficulty: " + GameSettings.getDifficulty(),
                    15);
            if (GameSettings.getGameMode() == MODE.SINGLEPLAYER) {
                inGameColumn.getChildren().addAll(gameDifficultyText, resumeGameButton());
                inGameColumn.getChildren().addAll(restartGameButton(), quitGameButton());
            } else {
                inGameColumn.getChildren().addAll(gameDifficultyText, resumeGameButton(), quitGameButton());
            }
            leftBox.getChildren().add(inGameColumn);
        }
        return leftBox;
    }

    /**
     * @return Returns container with difficulty selector (easy, medium or hard)
     */
    private VBox difficultySelector() {
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        VBox.setMargin(vBox, new Insets(5, 10, 5, 10));
        for (DIFFICULTY difficulty : DIFFICULTY.values()) {
            DifficultySelector selectDifficulty = new DifficultySelector(difficulty);
            difficulties.add(selectDifficulty);
            selectDifficulty.setAlignment(CENTER);
            difficulties.get(0).setSelectorCircle(true);
            GameSettings.setDifficulty(difficulties.get(0).getDifficulty());
            vBox.getChildren().add(selectDifficulty);
            selectDifficulty.setOnMouseClicked(mouseEvent -> {
                for (DifficultySelector difficultySelector : difficulties) {
                    difficultySelector.setSelectorCircle(false);
                }
                selectDifficulty.setSelectorCircle(true);
                GameSettings.setDifficulty(selectDifficulty.getDifficulty());
            });
        }
        vBox.setAlignment(CENTER);
        return vBox;
    }

    /**
     * @return Returns container with game mode selector (single player or multiplayer game modes)
     */
    private VBox modeSelector() {
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        VBox.setMargin(vBox, new Insets(5, 10, 5, 10));
        for (MODE mode : MODE.values()) {
            GameModeSelector selectMode = new GameModeSelector(mode);
            modes.add(selectMode);
            selectMode.setAlignment(CENTER);
            modes.get(0).setSelectorCircle(true);
            GameSettings.setGameMode(modes.get(0).getMode());
            vBox.getChildren().add(selectMode);
            selectMode.setOnMouseClicked(mouseEvent -> {
                for (GameModeSelector mode1 : modes) {
                    mode1.setSelectorCircle(false);
                }
                selectMode.setSelectorCircle(true);
                GameSettings.setGameMode(selectMode.getMode());
                if (selectMode.getMode() == MODE.MULTIPLAYER) {
                    GameSettings.setGameMode(MODE.MULTIPLAYER);
                }
                if (selectMode.getMode() == MODE.SINGLEPLAYER) {
                    GameSettings.setGameMode(MODE.SINGLEPLAYER);
                    GameSettings.setModel(null);
                }
            });
        }
        vBox.setAlignment(CENTER);
        return vBox;
    }

    /**
     * @return Returns the game start button for Main Menu
     */
    private HBox startButton() {
        HBox hBox = new HBox();
        TextButton startButton = new TextButton("Start", 20, audio);
        hBox.getChildren().add(startButton);
        hBox.setAlignment(CENTER);
        startButton.setOnAction(actionEvent -> {
            if ((GameSettings.getGameMode()) == MODE.SINGLEPLAYER && (GameSettings.getDifficulty()) != null) {
                try {
                    networkSystem.initiateNetworkSystem(GameSettings.getNumPlayers(),
                            "guest",
                            "127.0.0.1",
                            "50000");
                } catch (Exception e) {
                    //shouldn't occur
                    e.printStackTrace();
                }
                try {
                    networkSystem.start();
                } catch (ClassNotFoundException | IOException | InterruptedException | UsernameUnavailableException e) {
                    //shouldn't occur
                    e.printStackTrace();
                }
                GameSettings.getGameStateOwner().startGame();
            }
            if ((GameSettings.getGameMode()) == MODE.MULTIPLAYER && (GameSettings.getDifficulty()) != null) {
                GameSettings.setNumPlayers(2);
                inputSystem.setMuteKeyEnabled(false);
                menuRoot.getChildren().add(new LobbyScene(audio, networkSystem, inputSystem, menuRoot));
            }
        });
        return hBox;
    }

    /**
     * @return Returns the Resume button for the In-game Menu
     */
    private HBox resumeGameButton() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        TextButton startButton = new TextButton("Resume", 20, audio);
        hBox.getChildren().add(startButton);
        hBox.setAlignment(CENTER);
        startButton.setOnAction(actionEvent -> {
            menuRoot.getChildren().remove(menu);
            GameSettings.getGameStateOwner().resumeGame();
        });
        return hBox;
    }

    /**
     * @return Returns the Restart button for the In-Game Menu
     */
    private HBox restartGameButton() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        TextButton restartButton = new TextButton("Restart", 20, audio);
        hBox.getChildren().add(restartButton);
        hBox.setAlignment(CENTER);
        restartButton.setOnAction(actionEvent -> {
            menuRoot.getChildren().remove(menu);
            GameSettings.getGameStateOwner().restartGame();
        });
        return hBox;
    }

    /**
     * @return Returns the Quit game button for the In-Game Menu
     */
    private HBox quitGameButton() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);
        TextButton leaveGameButton = new TextButton("Leave", 20, audio);
        hBox.getChildren().add(leaveGameButton);
        hBox.setAlignment(CENTER);
        leaveGameButton.setOnAction(actionEvent -> {
            try {
                networkSystem.handleQuit();
            } catch (SocketTimeoutException | PortUnreachableException e) {
                e.printStackTrace();
            }
            menuRoot.getChildren().remove(menu);
            GameSettings.getGameStateOwner().completeGame(FinalScoreScene.COMPLETIONTYPE.QUIT);
        });
        return hBox;
    }

    /**
     * @return Returns the container for the right column, used in both Main Menu and In-Game Menu
     */
    private VBox rightColumn() {
        VBox audioBox = new VBox();
        audioBox.setPrefWidth(300);
        audioBox.setPrefHeight(300);
        audioBox.setSpacing(10);
        audioBox.setAlignment(CENTER);
        audioBox.getChildren().addAll(audioControls(), createInstructionsButton());
        if (type == MENUTYPE.MAIN) {
            HBox hBox = new HBox();
            TextButton closeButton = new TextButton("Close", 20, audio);
            HBox.setMargin(closeButton, new Insets(0, 0, 4, 0));
            hBox.getChildren().add(closeButton);
            hBox.setAlignment(CENTER);
            closeButton.setOnAction(actionEvent -> mainStage.close());
            audioBox.getChildren().addAll(createCreditsButton(), hBox);
        }
        return audioBox;
    }

    /**
     * @return Creates audio components to be added to menu's (Volume slider, FX slider and window size selector)
     */
    private VBox audioControls() {
        VBox audioControls = new VBox();
        audioControls.setPrefWidth(300);
        audioControls.setPrefHeight(300);
        audioControls.setSpacing(10);
        audioControls.getChildren().addAll(createVolumeSlider(), createFXSlider(), createWindowSelector());
        audioControls.setAlignment(CENTER);
        return audioControls;
    }

    /**
     * @return Creates volume slider and label
     */
    private HBox createVolumeSlider() {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        WindowLabel title = new WindowLabel("Volume: ", 15);
        Slider volumeSliderMusic = new Slider(0,100,GameSettings.getMusicVolume()*100);
        volumeSliderMusic.setMajorTickUnit(20.0);
        volumeSliderMusic.setMinorTickCount(5);
        volumeSliderMusic.setSnapToTicks(false);
        volumeSliderMusic.setShowTickMarks(false);
        volumeSliderMusic.setShowTickLabels(false);
        volumeSliderMusic.setMinWidth(100);
        WindowLabel volumeLabel = new WindowLabel(Integer.toString((int)volumeSliderMusic.getValue()), 15);
        volumeLabel.setMinWidth(30);
        hBox.getChildren().addAll(title, volumeSliderMusic, volumeLabel);
        hBox.setAlignment(CENTER_RIGHT);
        GameSettings.volumeProperty().addListener((o, oldVal, newVal) -> {
            if (GameSettings.getMusicVolume() == 0) {
                volumeLabel.setText(Integer.toString((int) GameSettings.getMusicVolume()));
                volumeSliderMusic.setValue(GameSettings.getMusicVolume());
            }
        });
        volumeSliderMusic.valueProperty().addListener((observableValue, number, t1) -> {
            int vol = ((int) volumeSliderMusic.getValue());
            volumeLabel.setText(Integer.toString(vol));
            audio.setVolumeMusic((double) vol/100);
        });
        return hBox;
    }

    /**
     * @return Creates FX volume slider and label
     */
    private HBox createFXSlider() {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        WindowLabel title = new WindowLabel(" FX Volume: ", 15);
        Slider volumeSliderFX = new Slider(0,100, GameSettings.getFxVolume()*100);
        volumeSliderFX.setMajorTickUnit(20.0);
        volumeSliderFX.setMinorTickCount(5);
        volumeSliderFX.setSnapToTicks(false);
        volumeSliderFX.setShowTickMarks(false);
        volumeSliderFX.setShowTickLabels(false);
        volumeSliderFX.setMinWidth(100);
        WindowLabel fxLabel = new WindowLabel(Integer.toString((int)volumeSliderFX.getValue()), 15);
        fxLabel.setMinWidth(30);
        hBox.getChildren().addAll(title, volumeSliderFX, fxLabel);
        hBox.setAlignment(CENTER_RIGHT);
        GameSettings.fxVolumeProperty().addListener((o, oldVal, newVal) -> {
            if (GameSettings.getFxVolume() == 0) {
                fxLabel.setText(Integer.toString((int) GameSettings.getFxVolume()));
                volumeSliderFX.setValue(GameSettings.getFxVolume());
            }
        });
        volumeSliderFX.valueProperty().addListener((observableValue, number, t1) -> {
            int vol = ((int) volumeSliderFX.getValue());
            fxLabel.setText(Integer.toString(vol));
            audio.setVolumeFX((double) vol/100);
        });
        return hBox;
    }

    /**
     * @return Creates window size selector (full screen or windowed view)
     */
    private VBox createWindowSelector() {
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(20,0,0,0));
        for (WINDOWSIZE window : WINDOWSIZE.values()) {
            WindowSelector selectWindow = new WindowSelector(window);
            windowSelectors.add(selectWindow);
            selectWindow.setAlignment(CENTER);
            if (!mainStage.isFullScreen()) {
                windowSelectors.get(0).setSelectorCircle(true);
                GameSettings.setWindowSize(windowSelectors.get(0).getWindowSize());
                mainStage.setFullScreen(false);
            } else {
                if (windowSelectors.size() > 1) {
                    windowSelectors.get(1).setSelectorCircle(true);
                    GameSettings.setWindowSize(windowSelectors.get(1).getWindowSize());
                    mainStage.setFullScreen(true);
                }
            }
            vBox.getChildren().add(selectWindow);
            selectWindow.setOnMouseClicked(mouseEvent -> {
                for (WindowSelector windowSelector : windowSelectors) {
                    windowSelector.setSelectorCircle(false);
                }
                selectWindow.setSelectorCircle(true);
                GameSettings.setWindowSize(selectWindow.getWindowSize());
                mainStage.setFullScreen(GameSettings.getWindowSize() == WINDOWSIZE.FULLSCREEN);
                renderer.update();

            });
        }
        return vBox;
    }

    /**
     * @return Creates Instructions button for Main Menu and In-Game Menu
     */
    private HBox createInstructionsButton() {
        HBox hBox = new HBox();
        TextButton instructionsButton = new TextButton("How to Play", 17, audio);
        hBox.getChildren().add(instructionsButton);
        hBox.setAlignment(CENTER);
        instructionsButton.setOnAction(
                actionEvent -> menuRoot.getChildren().add(new InstructionsScene(audio, menuRoot))
        );
        return hBox;
    }

    private HBox createCreditsButton() {
        HBox hBox = new HBox();
        TextButton creditsButton = new TextButton("Credits", 17, audio);
        hBox.getChildren().add(creditsButton);
        hBox.setAlignment(CENTER);
        creditsButton.setOnAction(actionEvent -> menuRoot.getChildren().add(new CreditsScene(audio, menuRoot)));
        return hBox;
    }
}
