package uob.cs.teamproject.sabrewulf;

import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import uob.cs.teamproject.sabrewulf.ui.templates.WindowLabel;
import java.awt.*;

/** Game preloader extends the class preloader and displays the splash screen which is displayed whilst the
 *  game is started up.
 */
public class GamePreloader extends Preloader {
    private Stage stage;
    private StackPane stackPane;

    /** Start method is called by the preloader when it is launched. This method displays the preloader on the
     *  screen and sets the dimensions of the screen size in the game settings.
     * @param stage - Main stage
     * @throws Exception - Throw exception if error occurs
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("Game Loading...");
        stage.getIcons().add(ResourceManager.getImage("images/mapItems/bitcoin_logo.png"));
        stackPane = new StackPane();
        setBackground();
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        GameSettings.setScreenWidth((int) (screenSize.width/1.5));
        GameSettings.setScreenHeight((int) (screenSize.height/1.25));
        stage.setScene(new Scene(stackPane,
                GameSettings.getScreenWidth()/2.5,
                GameSettings.getScreenHeight()/1.75));
        stage.show();
    }

    private void setBackground() {
        Image backgroundImage = ResourceManager.getImage("images/uielements/splashscreen.png");
        BackgroundImage background = new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true, true, false, false));
        stackPane.setBackground(new Background(background));
        VBox loader = new VBox();
        loader.setSpacing(25);
        loader.setAlignment(Pos.CENTER);
        ImageView logo = new ImageView(ResourceManager.getImage("images/uielements/logo.png", 500, 1000, true, false));
        WindowLabel loadingText = new WindowLabel("Loading...", 25);
        loader.getChildren().addAll(logo, loadingText);
        stackPane.getChildren().add(loader);
    }

    /** Check whether an event has been fired to say that the game is ready to load. If it has been fired, wait
     *  1.5 seconds before hiding the game preloader
     * @param event - Event which has been detected
     */
    @Override
    public void handleStateChangeNotification(StateChangeNotification event) {
        if (event.getType() == StateChangeNotification.Type.BEFORE_START) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            stage.hide();
        }
    }
}
