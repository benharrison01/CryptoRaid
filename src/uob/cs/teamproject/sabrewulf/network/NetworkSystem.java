package uob.cs.teamproject.sabrewulf.network;

import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.exceptions.UsernameUnavailableException;
import uob.cs.teamproject.sabrewulf.ui.selectors.DIFFICULTY;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODE;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODEL;

import java.io.IOException;
import java.net.PortUnreachableException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;


/**
 * The network system is responsible for a client and a server initiation
 * and the way they work during the whole game
 */
public class NetworkSystem{
    private Server server;
    private Client client;

    public NetworkSystem(){}

    /**
     * Initiates a client and a server if the user is hosting it
     * @param numOfPlayers a number of players
     * @param username a username of the host of the game
     * @param ip an IP address of the host of the server
     * @param portNumber a port the server is listening on
     * @throws SocketException if port is not available
     */
    public void initiateNetworkSystem(int numOfPlayers, String username, String ip, String portNumber)
            throws SocketException {
        int numberOfEnemies = 3;
        if(GameSettings.getDifficulty() != DIFFICULTY.EASY){
            numberOfEnemies = 4;
        }
        int port  = Integer.parseInt(portNumber);
        if(GameSettings.getGameMode().equals(MODE.SINGLEPLAYER)) {
            //Choose a random port until the chosen one is available
            while(true){
                port = (int) ((Math.random() * 16383) + 49152);
                try {
                    server = new Server(numOfPlayers, numberOfEnemies,port);
                    break;
                } catch (SocketException e) {
                    System.out.println("Port is not available. Trying another port");
                }
            }
        }
        else if(GameSettings.getModel().equals(MODEL.SERVER)){
            server = new Server(numOfPlayers, numberOfEnemies,port);
        }
        client = new Client(numOfPlayers, numberOfEnemies, username, ip,port);
    }

    /**
     * The start method for the network system which loads the game for all clients
     * @throws IOException if an IO error occurs
     * @throws ClassNotFoundException if a class of a serialized object cannot be found
     * @throws InterruptedException if a thread is interrupted
     * @throws UsernameUnavailableException if the username is already used by another player.
     */
    public void start() throws IOException, ClassNotFoundException, InterruptedException, UsernameUnavailableException {
        if(server != null) {
            Thread thread = new Thread(() -> {
                try {
                    server.start();
                }
                catch (IOException e) {
                    Thread.currentThread().interrupt();
                }
            });
            thread.start();
            client.start();
            thread.join();

        }
        else {
            client.start();
        }
        client.getMap();
        Thread thread3 = new Thread(() -> {
            if(server != null){
                while(server.playerSpawnsAt == null){
                    try {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    server.sendRandomCoordinates();
                }
                catch (IOException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        thread3.start();
        thread3.join();
        client.getRandomCoordinates();
    }

    /**
     * The update method for the network system.
     */
    public void update(){
        if (server != null) {
            if (server.isConnected) {
                server.update();
                client.update();
            }
        }
        else {
            if(client.isConnected()){
                client.update();
            }
        }
    }

    /**
     * The restart method for the network system
     * prepares the server and the client for the start of a new game.
     */
    public void restart(){
        server.setInitialValues();
        server.createMap();
        client.sendData(client.getDataStorage().getUsername());
        try {
            start();
        }
        catch (IOException | ClassNotFoundException | InterruptedException | UsernameUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the way the client and the server act if a player quits a game.
     * @throws SocketTimeoutException if a timeout has occurred on a socket read or accept
     * @throws PortUnreachableException if an ICMP Port Unreachable message has been received on a connected datagram
     */
    public void handleQuit() throws SocketTimeoutException, PortUnreachableException {
        if(server != null) {
            server.sendEnd(false);
            client.getData(5);
        }
        else if(client != null){
            client.sendQuit();
        }
    }

    /**
     * Handles the way the client and the server act when the game ends.
     */
    public void handleFinish(){
        if(server != null) {
            server.sendEnd(true);
        }
        else if(client != null){
            client.sendData("end#");
        }
    }

    /**
     * Closes a server socket
     */
    public void closeServerSocket(){
        if(server != null){
            server.dtSocket.close();
            server = null;
        }
    }

    /**
     * Closes a client socket
     */
    public void closeClientSocket(){
        client.datagramSocket.close();
        client = null;
    }

    /**
     * @return scores of players connected
     */
    public HashMap<String, Integer> getScores(){
        if(client.getDataStorage().getScores() == null){
            Thread thread = new Thread(() -> {
                if(server != null){
                    server.getScore();
                }
            });
            thread.start();
            client.sendScore(GameSettings.getInventory().getScore());
            HashMap<String,Integer> scores = client.getScores();
            while(scores == null){
                client.sendScore(GameSettings.getInventory().getScore());
                scores = client.getScores();
            }
            try {
                thread.join();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            return scores;
        }
        return client.getDataStorage().getScores();
    }

    /**
     * @return the client object
     */
    public Client getClient(){
        return client;
    }

    /**
     * @return the server object
     */
    public Server getServer(){
        return server;
    }
}
