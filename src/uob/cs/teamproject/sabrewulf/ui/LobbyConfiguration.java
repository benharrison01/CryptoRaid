package uob.cs.teamproject.sabrewulf.ui;

import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import uob.cs.teamproject.sabrewulf.audio.Audio;
import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.exceptions.UsernameUnavailableException;
import uob.cs.teamproject.sabrewulf.ui.fields.IPAddressField;
import uob.cs.teamproject.sabrewulf.ui.fields.UsernameField;
import uob.cs.teamproject.sabrewulf.ui.scene.LobbyScene;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODEL;
import uob.cs.teamproject.sabrewulf.ui.templates.TextButton;
import uob.cs.teamproject.sabrewulf.ui.templates.WindowLabel;
import uob.cs.teamproject.sabrewulf.network.NetworkSystem;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.geometry.Pos.CENTER;

/** Lobby Configuration class extends {@link VBox} and contains all the content which is displayed in the multiplayer
 *  lobby scene. This includes the number of players selection, network model, IP address/port number and username
 *  input fields.
 */
public class LobbyConfiguration extends VBox {

    private final LobbyScene lobbyScene;
    private final NetworkSystem networkSystem;
    private final Audio audio;
    private List<String> usernames;
    private HBox selectNumPlayers;
    private HBox modelSelectionButtons;
    private HBox usernameInput;
    private HBox ipInput;
    private TextButton continueButton;
    private UsernameField field;
    private IPAddressField ipField;
    private boolean warningActive = false;


    /** Constructor creates a new instance of Lobby Configuration
     * @param lobbyScene - {@link LobbyScene} where these features will be added
     * @param audio - {@link Audio} system
     * @param networkSystem - {@link NetworkSystem}
     */
    public LobbyConfiguration(LobbyScene lobbyScene, Audio audio, NetworkSystem networkSystem) {
        this.lobbyScene = lobbyScene;
        this.audio = audio;
        this.networkSystem = networkSystem;
        this.setSpacing(20);
        this.setAlignment(CENTER);
        this.setPrefWidth(500);
        this.setPrefHeight(200);
        usernames = new CopyOnWriteArrayList<>();
        selectNumPlayers();
        modelSelection();
        ipBox();
        usernameBox();
        continueButton();
        this.getChildren().addAll(selectNumPlayers, modelSelectionButtons, ipInput, usernameInput, continueButton);
    }

    private void selectNumPlayers() {
        HBox numPlayers = new HBox();
        numPlayers.setSpacing(20);
        numPlayers.setAlignment(CENTER);
        TextButton twoPlayers = new TextButton("2 Players", 20, audio);
        TextButton threePlayers = new TextButton("3 Players", 20, audio);
        TextButton fourPlayers = new TextButton("4 Players", 20, audio);
        twoPlayers.setButtonPressed();
        twoPlayers.setOnAction(actionEvent -> {
            GameSettings.setNumPlayers(2);
            twoPlayers.setButtonPressed();
            threePlayers.setButtonFree();
            fourPlayers.setButtonFree();
        });
        threePlayers.setOnAction(actionEvent -> {
            GameSettings.setNumPlayers(3);
            threePlayers.setButtonPressed();
            twoPlayers.setButtonFree();
            fourPlayers.setButtonFree();
        });
        fourPlayers.setOnAction(actionEvent -> {
            GameSettings.setNumPlayers(4);
            fourPlayers.setButtonPressed();
            twoPlayers.setButtonFree();
            threePlayers.setButtonFree();
        });
        numPlayers.getChildren().addAll(twoPlayers, threePlayers, fourPlayers);
        selectNumPlayers = numPlayers;
    }

    private void modelSelection() {
        HBox modelSelection = new HBox();
        modelSelection.setSpacing(20);
        modelSelection.setAlignment(CENTER);
        modelSelection.setPrefWidth(500);
        modelSelection.setPrefHeight(200);
        TextButton hostButton = new TextButton("New Game", 20, audio);
        TextButton joinButton = new TextButton("Join Game", 20, audio);
        hostButton.setOnAction(actionEvent -> {
            GameSettings.setModel(MODEL.SERVER);
            hostButton.setButtonPressed();
            joinButton.setButtonFree();
        });
        joinButton.setOnAction(actionEvent -> {
            GameSettings.setModel(MODEL.CLIENT);
            hostButton.setButtonFree();
            joinButton.setButtonPressed();
        });
        modelSelection.getChildren().addAll(hostButton, joinButton);
        modelSelectionButtons = modelSelection;
    }

    private void usernameBox() {
        field = new UsernameField();
        usernameInput = field;
    }

    private void ipBox() {
        ipField = new IPAddressField();
        ipInput = ipField;
    }

    private void continueButton() {
        TextButton button = new TextButton("Continue", 20, audio);
        button.setOnAction(actionEvent -> {
            String reg = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.)" +
                    "{3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]):[0-9]{5}+$";
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(ipField.getIp());
            int inputLength = ipField.getIp().length();
            String portNumber = "";
            if (matcher.matches()) {
                portNumber = ipField.getIp().substring(inputLength-5,inputLength);
            }
            WindowLabel invalidIp = new WindowLabel("Invalid IP/port format or value(s)", 15);
            if(!matcher.matches() || Integer.parseInt(portNumber) < 49152 || Integer.parseInt(portNumber) > 65535) {
                if (!warningActive) {
                    this.getChildren().add(invalidIp);
                    warningActive = true;
                }
            } else {
                if (GameSettings.getModel() != null && GameSettings.getNumPlayers() > 1) {
                    if (!field.getField().getText().isEmpty() && !ipField.getIpField().getText().isEmpty()) {
                        GameSettings.setUsername(field.getUsername());
                        if (this.getChildren().contains(invalidIp)) {
                            this.getChildren().remove(invalidIp);
                            warningActive = false;
                        }
                        this.getChildren().removeAll(selectNumPlayers, modelSelectionButtons, ipInput);
                        this.getChildren().removeAll(usernameInput, continueButton);

                        try {
                            String ip = ipField.getIp().substring(0, inputLength-6);
                            networkSystem.initiateNetworkSystem(GameSettings.getNumPlayers(),
                                    field.getUsername(),
                                    ip, portNumber);
                        } catch (SocketException e) {
                            WindowLabel portErrorLabel = new WindowLabel("Please enter a different port",
                                    15);
                            this.getChildren().add(portErrorLabel);
                            e.printStackTrace();
                            return;
                        }

                        try {
                            networkSystem.start();
                        } catch (IOException | ClassNotFoundException | InterruptedException e) {
                            /* Occurs if a host has not started a server
                             * or anything goes wrong while loading the game
                             * (couldn't create or get map, coordinates, etc) */
                            networkSystem.closeClientSocket();
                            if(networkSystem.getServer() != null){
                                networkSystem.closeServerSocket();
                            }
                            WindowLabel gameErrorLabel = new WindowLabel("Could not load",
                                    15);
                            this.getChildren().add(gameErrorLabel);
                            return;
                        } catch (UsernameUnavailableException e) {
                            WindowLabel usernameErrorLabel = new WindowLabel("Please enter a new username",
                                    15);
                            networkSystem.closeClientSocket();
                            this.getChildren().addAll(usernameErrorLabel,usernameInput, continueButton);
                            return;
                        }

                        lobbyScene.removeBackButton();
                        WindowLabel numPlayersText = new WindowLabel(GameSettings.getNumPlayers() + " player game",
                                15);
                        this.getChildren().addAll(numPlayersText);

                        usernames = networkSystem.getClient().getDataStorage().getUsernames();
                        for (String s : usernames) {
                            WindowLabel username = new WindowLabel(s, 10);
                            username.setTextFill(Color.web("#FFFFFF"));
                            this.getChildren().add(username);
                        }

                        if (GameSettings.getModel() == MODEL.SERVER) {
                            TextButton startMultiButton = new TextButton("Start Game", 15, audio);
                            startMultiButton.setOnAction(actionEvent2 -> {
                                networkSystem.getServer().sendStart();
                                networkSystem.getClient().getStart();
                                GameSettings.getGameStateOwner().startGame();
                            });
                            this.getChildren().addAll(startMultiButton);
                        } else {
                            WindowLabel waitingLabel = new WindowLabel("Waiting for host to start the game",
                                    15);
                            this.getChildren().add(waitingLabel);
                            if(GameSettings.getModel() != MODEL.SERVER){
                                Thread thread = new Thread(() -> {
                                    while(true){
                                        if(networkSystem.getClient().getStart()){
                                            break;
                                        }
                                    }
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            GameSettings.getGameStateOwner().startGame();
                                        }
                                    });
                                });
                                thread.start();
                            }

                        }
                    }
                }
            }
        });
        continueButton = button;
    }
}
