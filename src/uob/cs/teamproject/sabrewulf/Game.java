package uob.cs.teamproject.sabrewulf;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import uob.cs.teamproject.sabrewulf.achievements.StatisticsTracker;
import uob.cs.teamproject.sabrewulf.audio.Audio;
import uob.cs.teamproject.sabrewulf.collisions.CollisionSystem;
import uob.cs.teamproject.sabrewulf.input.Input;
import uob.cs.teamproject.sabrewulf.network.NetworkSystem;
import uob.cs.teamproject.sabrewulf.rendering.Renderer;
import uob.cs.teamproject.sabrewulf.rendering.ResizeableCanvas;
import uob.cs.teamproject.sabrewulf.ui.InGameBar;
import uob.cs.teamproject.sabrewulf.ui.MenuManager;
import uob.cs.teamproject.sabrewulf.ui.scene.AchievementsAwardedScene;
import uob.cs.teamproject.sabrewulf.ui.scene.FinalScoreScene;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODE;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODEL;
import uob.cs.teamproject.sabrewulf.ui.selectors.WINDOWSIZE;

import java.util.Set;

/**
 * This class is the entry point of the program.
 * It is responsible for:
 *  - managing the top-level JavaFX containers
 *  - creating the game systems
 *  - launching the start menu
 *  - the main game update loop
 *  - transitions between different states of the application
 *    (e.g. 'start menu' to 'in-game', 'in-game' to 'paused', 'in-game' to 'completed', etc.)
 */
public class Game extends Application implements GameStateOwner {

        /* the program is a state machine, this stores the current state */
        private GameState gameState;

        /* game systems */
        private Audio audioSystem;
        private CollisionSystem collisionSystem;
        private Input inputSystem;
        private NetworkSystem networkSystem;
        private Renderer renderer;
        private StatisticsTracker statisticsTracker;

        /* manages the entities in the game world */
        private GameWorld world;

        /* the main game loop */
        private AnimationTimer gameLoop;

        /* top-level JavaFX containers */
        private Stage mainStage;
        private Scene mainScene;
        private StackPane menuRoot;
        private StackPane gameRoot;
        private BorderPane gameLayoutPane;
        private StackPane canvasContainer;

        /* JavaFX controls for displaying the game */
        private ResizeableCanvas canvas;
        private InGameBar inGameBar;

        /* stores the time when a game starts, so that the duration of the game can be determined */
        private long gameStartTime;


        /**
         * The entry point of the program. Launches the JavaFX preloader and the main application.
         * @param args command line arguments
         */
        public static void main(String[] args) {
                /* enable the 'preloader' splash screen */
                System.setProperty("javafx.preloader", GamePreloader.class.getCanonicalName());

                /* launch the JavaFX application */
                launch(args);
        }

        /**
         * The entry point of the main application.
         * Creates the game systems, sets up the JavaFX controls and starts the menu system.
         * @param mainStage the {@link Stage} for the main application window.
         */
        @Override
        public void start(Stage mainStage) {
                this.mainStage = mainStage;

                /* initialise the state machine */
                gameState = GameState.START_MENU;

                /* initialise the 'settings' values */
                GameSettings.initialise();

                /* get the screen width and height, which were already set by the preloader */
                double screenWidth = GameSettings.getScreenWidth();
                double screenHeight = GameSettings.getScreenHeight();

                /* create some of the controls */
                menuRoot        = new StackPane();
                gameRoot        = new StackPane();
                mainScene       = new Scene(menuRoot, screenWidth, screenHeight);
                gameLayoutPane  = new BorderPane();
                canvasContainer = new StackPane();
                canvas          = new ResizeableCanvas(screenWidth, screenHeight);

                /* create the game systems */
                audioSystem     = new Audio();
                collisionSystem = new CollisionSystem();
                inputSystem     = new Input(mainScene, audioSystem);
                networkSystem   = new NetworkSystem();
                renderer        = new Renderer(canvas);
                statisticsTracker = new StatisticsTracker(
                        "json/achievements_challenges.json");

                /* create the rest of the controls */
                inGameBar       = new InGameBar(audioSystem);

                /* lay out and configure the controls */
                initialiseControls();

                /* enable the 'mute' hotkey */
                inputSystem.setMuteKeyEnabled(true);

                /* expose this object's GameStateOwner interface for managing the game state */
                GameSettings.setGameStateOwner(this);

                /* show the start menu */
                MenuManager startMenu = new MenuManager(
                        audioSystem, inputSystem, networkSystem, renderer, statisticsTracker,
                        mainStage, menuRoot);
                startMenu.createStartMenu(MenuManager.MENUTYPE.MAIN);
        }


        /** Transition from the start menu to the game. */
        @Override
        public void startGame() {
                assert gameState == GameState.START_MENU;

                /* create the game world */
                world = new GameWorld(audioSystem, collisionSystem, inputSystem, networkSystem, renderer,
                        statisticsTracker);

                /* create the main game loop */
                gameLoop = new AnimationTimer() {
                        @Override
                        public void handle(long now) {
                            world.update(now);
                            collisionSystem.update();
                            networkSystem.update();
                            renderer.update();
                        }
                };

                /* initialise the in-game UI */
                inGameBar.setInventory();
                inGameBar.addChildren();

                /* show the game */
                mainScene.setRoot(gameRoot);

                /* enable the opening the pause menu */
                GameSettings.setPauseMenuDisabled(false);

                /* set the window to fullscreen if the user chose to */
                if (GameSettings.getWindowSize() == WINDOWSIZE.FULLSCREEN) {
                        mainStage.setFullScreen(true);
                }

                /* if this is a singleplayer game, record the current time and notify the achievements system */
                if(GameSettings.getGameMode() == MODE.SINGLEPLAYER) {
                        gameStartTime = System.nanoTime();
                        statisticsTracker.noteGameStarted();
                }

                /* game world startup */
                world.start();

                /* start the main game loop */
                gameLoop.start();

                gameState = GameState.IN_GAME;
        }

        /** Transition from the game to the pause menu. */
        @Override
        public void pauseGame() {
                assert gameState == GameState.IN_GAME;

                /* if it's a singleplayer game, stop the main game loop */
                if (GameSettings.getGameMode() == MODE.SINGLEPLAYER){
                        gameLoop.stop();
                }

                /* switch from showing the game to showing the menu */
                mainScene.setRoot(menuRoot);

                /* create and display the pause menu */
                MenuManager pauseMenu = new MenuManager(
                        audioSystem, inputSystem, networkSystem, renderer, statisticsTracker,
                        mainStage, menuRoot);
                pauseMenu.createNewMenu(MenuManager.MENUTYPE.INGAME);

                gameState = GameState.PAUSED;
        }

        /** Transition from the pause menu to the game. */
        @Override
        public void resumeGame() {
                assert gameState == GameState.PAUSED;

                /* if it's a singleplayer game, restart the main game loop */
                if (GameSettings.getGameMode() == MODE.SINGLEPLAYER) {
                        gameLoop.start();
                }

                /* switch from showing the menu to showing the game */
                mainScene.setRoot(gameRoot);

                gameState = GameState.IN_GAME;
        }

        /** Transition from the game or the pause menu to the final score page. */
        @Override
        public void completeGame(FinalScoreScene.COMPLETIONTYPE finishType) {
                assert gameState == GameState.IN_GAME || gameState == GameState.PAUSED;

                /* stop the game */
                gameLoop.stop();

                /* we either go to the 'Final Score' page or the 'Achievements Awarded' page */
                boolean goToAchievementsPage = false;

                if (finishType == FinalScoreScene.COMPLETIONTYPE.SUCCESS) {
                        /* the game lasted through to completion */

                        if (GameSettings.getGameMode() == MODE.SINGLEPLAYER) {
                                /* the game lasted through to completion and was a singleplayer game */

                                /* play success audio if the player successfully completes the game */
                                audioSystem.playSuccessAudio();

                                /* work out the duration of the game */
                                long gameTimeMillis = (System.nanoTime() - gameStartTime) / 1000000L;

                                /* notify the statistics tracker that the game has completed */
                                statisticsTracker.noteGameFinished(GameSettings.getDifficulty(), gameTimeMillis);

                                /* get the list of achievements we had earned before this game */
                                Set<String> achievementsBeforeGame = statisticsTracker.getAwardedAchievements();

                                /* update the achievements system with the newly earned achievements */
                                statisticsTracker.writeStatsToFile(
                                        "json/achievements_challenges.json"
                                );
                                statisticsTracker.updateAchievements();

                                /* get the updated list of achievements we've earned */
                                Set<String> newAchievements = statisticsTracker.getAwardedAchievements();
                                newAchievements.removeAll(achievementsBeforeGame);

                                /* if we have gained any new achievements */
                                if (!newAchievements.isEmpty()) {
                                        /* go to the achievements page */
                                        goToAchievementsPage = true;
                                        menuRoot.getChildren().add(new AchievementsAwardedScene(
                                                audioSystem, networkSystem, statisticsTracker,
                                                mainStage, menuRoot,
                                                newAchievements, finishType
                                        ));
                                }

                        } else {
                                /* the game lasted through to completion and was a multiplayer game */

                                /* find the highest score across all the players */
                                int thisPlayerScore = networkSystem.getScores().get(GameSettings.getUsername());
                                int maxScore = -1;
                                for (int score : networkSystem.getScores().values())
                                        if (score >= maxScore)
                                                maxScore = score;

                                /* figure out whether we won the game */
                                if (thisPlayerScore == maxScore) {
                                        finishType = FinalScoreScene.COMPLETIONTYPE.WIN;
                                        audioSystem.playSuccessAudio();
                                }
                                else {
                                        finishType = FinalScoreScene.COMPLETIONTYPE.LOSE;
                                }
                        }
                }

                if (!goToAchievementsPage) {
                        /* go to the 'Final Score' page */
                        menuRoot.getChildren().add(new FinalScoreScene(
                                        audioSystem, networkSystem, statisticsTracker,
                                        mainStage, menuRoot,
                                        finishType
                                ));
                }

                /* disable hotkeys which should only be active during the game */
                inputSystem.setMuteKeyEnabled(false);
                GameSettings.setPauseMenuDisabled(true);

                /* switch from showing the game to showing the menu */
                mainScene.setRoot(menuRoot);

                gameState = GameState.COMPLETED;
        }

        /** Transition from the pause menu or the final score page to a new run of the game. */
        @Override
        public void restartGame() {
                assert gameState == GameState.PAUSED || gameState == GameState.COMPLETED;

                world.remove();
                inGameBar.removeChildren();

                inputSystem.setMuteKeyEnabled(true); // enable the 'mute' hotkey

                networkSystem.restart();

                gameState = GameState.START_MENU;
                startGame(); // changes state to IN_GAME
        }

        /** Transition from the pause menu or the final score page to the start menu. */
        @Override
        public void returnToMenu() {
                assert gameState == GameState.PAUSED || gameState == GameState.COMPLETED;

                world.remove();
                inGameBar.removeChildren();

                if(GameSettings.getModel() == MODEL.SERVER || GameSettings.getGameMode() == MODE.SINGLEPLAYER){
                        networkSystem.closeServerSocket();
                }

                GameSettings.onReturnToMenu();

                MenuManager menuManager = new MenuManager(
                        audioSystem, inputSystem, networkSystem, renderer, statisticsTracker,
                        mainStage, menuRoot);

                inputSystem.setMuteKeyEnabled(true); // enable the 'mute' hotkey

                /* show the main menu */
                menuManager.createStartMenu(MenuManager.MENUTYPE.MAIN);

                gameState = GameState.START_MENU;
        }


        /* set up the main JavaFX containers and controls */
        private void initialiseControls() {
                /* hierarchy of JavaFX controls:

                [during the main menu + pause menu]
                mainStage
                  mainScene
                    menuRoot
                      (menu controls...)

                [during the game]
                mainStage
                  mainScene
                    gameRoot
                      gameLayoutPane
                        inGameBar       (top)
                        canvasContainer (center)
                          canvas
                */

                /* set window title and icon */
                mainStage.setTitle("CryptoRaid");
                mainStage.getIcons().add(ResourceManager.getImage("images/mapItems/bitcoin_logo.png"));
                mainStage.setScene(mainScene);

                /* arrange the controls */
                gameRoot.getChildren().add(gameLayoutPane);
                gameLayoutPane.setTop(inGameBar);
                gameLayoutPane.setCenter(canvasContainer);
                canvasContainer.getChildren().add(canvas);

                /* configure the controls */
                menuRoot.prefWidthProperty().bind(mainScene.widthProperty());
                menuRoot.prefHeightProperty().bind(mainScene.heightProperty());
        }
}