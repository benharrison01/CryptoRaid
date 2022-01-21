package uob.cs.teamproject.sabrewulf.network;

import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.exceptions.UsernameUnavailableException;
import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.components.CharacterMovement;
import uob.cs.teamproject.sabrewulf.ui.scene.FinalScoreScene;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODEL;
import uob.cs.teamproject.sabrewulf.util.XYPair;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Integer.parseInt;

/**
 * The Client class is used to create a connection with a server socket
 * and contains methods to send data to the server and receive data from it.
 * It talks to some of the game components with the help of {@link DataStorage}
 */
public class Client {
    protected DatagramSocket datagramSocket;
    private final String hostIp;
    private final int port;
    private boolean isConnected;
    private int playersConnected;
    private final int clientNumber;
    private final ArrayList<Integer> portsOfUsersConnected;
    private final HashMap<Integer,Integer> userNumByPort;

    private int numberOfPlayers;
    private final int numberOfEnemies;

    private DataStorage dataStorage;

    /**'The client class constructor initiates a {@link DatagramSocket} and
     * sends a username to the server to connect to it
     * @param numberOfPlayers a number of players
     * @param numberOfEnemies a number of enemies
     * @param username a username
     * @param hostIp an IP address of the user hosting the server
     * @param port a port the server is listening on
     * @throws SocketException if the client could not initiate a socket or a server socket is not reachable
     */
    public Client(int numberOfPlayers, int numberOfEnemies, String username, String hostIp, int port)
            throws SocketException {
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfEnemies = numberOfEnemies;
        this.hostIp = hostIp;
        this.port = port;
        this.dataStorage = new DataStorage(numberOfPlayers,numberOfEnemies);
        dataStorage.setUsername(username);
        portsOfUsersConnected = new ArrayList<>();
        userNumByPort = new HashMap<>();
        isConnected = false;
        playersConnected = 0;

        SocketAddress address= null;
        try {
            address = new InetSocketAddress(InetAddress.getByName(hostIp),port);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }

        datagramSocket = new DatagramSocket();
        datagramSocket.connect(address);
        datagramSocket.setSoTimeout(300000);

        clientNumber = datagramSocket.getLocalPort();
        sendData(username);
    }

    /**
     * Sends character's information to the server
     * @param dirX the x direction of the character
     * @param dirY the y direction of the character
     * @param isMoving true if the character is moving, false otherwise
     * @param facing the direction the character is facing
     * @param x the x position of character transform object
     * @param y the y position of character transform object
     * @param invisibility true if character is using the invisibility boost
     * @param speed true if character is using the speed boost
     */
    public void sendCoordinates(CharacterMovement.Direction dirX, CharacterMovement.Direction dirY,
                                boolean isMoving, CharacterMovement.Direction facing,
                                double x, double y, boolean invisibility, boolean speed){
        StringBuilder str = new StringBuilder();
        str.append(clientNumber).append("#");
        str.append(dirX).append("#");
        str.append(dirY).append("#");
        str.append(isMoving).append("#");
        str.append(facing).append("#");
        str.append(invisibility).append("#");
        str.append(speed).append("#");
        str.append(x).append("#");
        str.append(y).append("#");
        if(GameSettings.getModel() == MODEL.SERVER){
            for(int i = 0; i < numberOfEnemies; i++){
                str.append("e#");
                str.append(dataStorage.enemyDirections[i*2]).append("#");
                str.append(dataStorage.enemyDirections[i*2+1]).append("#");
                str.append(dataStorage.enemyIsMoving[i]).append("#");
                str.append(dataStorage.enemyIsFacing[i]).append("#");
            }
        }
        sendData(str.toString());
    }

    /**
     * @return true if the game should start, false otherwise
     */
    public boolean getStart(){
        DatagramPacket dtPacket = null;
        try {
            dtPacket = getData(32);
        }
        catch (SocketTimeoutException | PortUnreachableException e) {
            e.printStackTrace();
        }
        if(dtPacket != null){
            String str = new String(dtPacket.getData());
            String[] s = str.split("#",-2);
            return s[0].trim().equals("start");
        }
        else {
            return false;
        }
    }

    public boolean isConnected(){
        return isConnected;
    }


    public DataStorage getDataStorage() {
        return dataStorage;
    }

    /**
     * The start method receives usernames and ports of all clients and
     * assigns a number each client will be distinguished by
     * @throws SocketTimeoutException if the socket does not receive any packets in time
     * @throws PortUnreachableException if the port of the server is not reachable
     * @throws UsernameUnavailableException if the username is unavailable
     */
    protected void start() throws SocketTimeoutException, PortUnreachableException, UsernameUnavailableException {
        receiveUsernames();

        while(numberOfPlayers != playersConnected){
            receiveUsernames();
        }

        userNumByPort.put(clientNumber,0);
        int num = 1;
        for(int p : portsOfUsersConnected){
            if(p != clientNumber){
                userNumByPort.put(p,num);
                num++;
            }
        }
    }

    /**
     * The update method for the client
     */
    protected void update(){
        receiveDirections();
    }

    /**
     * Gets a map from the server and stores it in {@link DataStorage}
     * @throws IOException if an IO error occurs
     * @throws ClassNotFoundException if a class of a serialized object cannot be found
     */
    protected void getMap() throws IOException, ClassNotFoundException {
        DatagramPacket dtPacket = getData(40000);
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(dtPacket.getData());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
        Cell[][] cellGrid = (Cell[][]) objectInputStream.readObject();
        if(cellGrid != null){
            isConnected = true;
            dataStorage.setCellGrid(cellGrid);
        }
    }

    /**
     * Gets positions all players and enemies spawn on from the server and stores them in {@link DataStorage}
     * @throws PortUnreachableException if the port of the server is not reachable
     * @throws SocketTimeoutException if the socket does not receive any packets in time
     */
    protected void getRandomCoordinates() throws PortUnreachableException, SocketTimeoutException {
        DatagramPacket dtPacket = getData(164);
        String coordinates = new String(dtPacket.getData());
        String[] randomCoordinates = coordinates.trim().split("#",-2);
        int characters = 0;
        for(int i = 0; i < randomCoordinates.length; i = i + 2){
            if(characters < numberOfPlayers && !(randomCoordinates[i].trim().equals(""))
                    && !(randomCoordinates[i+1].trim().equals(""))){
                int j = userNumByPort.get(parseInt(randomCoordinates[i].trim()));
                XYPair pair = new XYPair(Double.parseDouble(randomCoordinates[i+1]),
                        Double.parseDouble(randomCoordinates[i+2]));
                dataStorage.setSpawnAt(j,pair);
                characters++;
            }
            else if(!(randomCoordinates[i].trim().equals("")) && !(randomCoordinates[i+1].trim().equals(""))){
                XYPair pair = new XYPair(Double.parseDouble(randomCoordinates[i]),
                        Double.parseDouble(randomCoordinates[i+1]));
                dataStorage.setSpawnAt(characters,pair);
                characters++;
            }
        }
    }

    /**
     * Send messages to the server
     * @param data a message to be sent
     */
    protected void sendData(String data){
        try {
            DatagramPacket dtPacket =
                    new DatagramPacket(data.getBytes(),data.getBytes().length, InetAddress.getByName(hostIp), port);
            datagramSocket.send(dtPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a message when a player quits
     */
    protected void sendQuit(){
        String str;
        if(GameSettings.getModel() == MODEL.SERVER){
            str = "end#";
        }
        else {
            str = "quit#";
        }
        sendData(str);
    }

    /**
     * Send a username and a score
     * @param score a score of a player
     */
    protected void sendScore(int score){
        String str = "score#" + dataStorage.getUsername() + "#" + score + "#";
        sendData(str);
    }

    /**
     * Receives a {@link DatagramPacket} of given length
     * @param size the length of byte array to receive
     * @return a {@link DatagramPacket}
     * @throws SocketTimeoutException if the socket does not receive any packets in time
     * @throws PortUnreachableException if the port of the server is not reachable
     */
    protected DatagramPacket getData(int size) throws SocketTimeoutException, PortUnreachableException {
        byte[] data = new byte [size];
        DatagramPacket dtPacket = new DatagramPacket(data,data.length);
        try {
            datagramSocket.receive(dtPacket);
        }
        catch (SocketTimeoutException e) {
            throw new SocketTimeoutException("Client not connected");
        }
        catch (PortUnreachableException e) {
            throw new PortUnreachableException("not");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return dtPacket;
    }

    /**
     * Get scores of all clients
     * @return a {@link HashMap<>} that contains a {@link String} username as a key and
     * an {@link Integer} score as a value
     */
    protected HashMap<String, Integer> getScores(){
        HashMap<String,Integer> scores = new HashMap<>();
        DatagramPacket packet = null;
        try {
            packet = getData(400);
        }
        catch (SocketTimeoutException | PortUnreachableException e) {
            e.printStackTrace();
        }
        assert packet != null;
        String[] data = (new String(packet.getData())).trim().split("#",-2);
        int i = 0;
        if(data[i].trim().equals("scores")) {
            i = 1;
            while (i < data.length) {
                String username = data[i];
                int score;
                if (!data[i].equals("")) {
                    try {
                        score = parseInt(data[i + 1]);
                    }
                    catch (NumberFormatException e) {
                        score = 0;
                    }
                    scores.put(username, score);
                }
                i = i + 2;
            }
        }
        else {
            return null;
        }
        dataStorage.setScores(scores);
        return scores;
    }

    /**
     * Receive usernames of all clients and store them in {@link DataStorage}
     * @throws SocketTimeoutException if the socket does not receive any packets in time
     * @throws PortUnreachableException if the port of the server is not reachable
     * @throws UsernameUnavailableException if the username is unavailable
     */
    private void receiveUsernames()
            throws SocketTimeoutException, PortUnreachableException, UsernameUnavailableException {
        DatagramPacket dtPacket = getData(560);
        String allUsernames = new String(dtPacket.getData());
        String[] user = allUsernames.split("#",-2);
        if(user[0].trim().equals("unavailable")){
            throw new UsernameUnavailableException("Username already in use");
        }
        playersConnected = parseInt(user[0]);
        for(int i = 1; i < user.length; i=i+2){
            if(!user[i].trim().equals("") && !(dataStorage.getUsernames().contains(user[i].trim()))
                    && !user[i+1].trim().equals("")){
                dataStorage.addUsername(user[i].trim());
                portsOfUsersConnected.add(parseInt(user[i+1].trim()));
            }
        }
    }

    /**
     * Receive information of all clients and store them in {@link DataStorage}
     */
    private void receiveDirections(){
        DatagramPacket dtPacket = null;
        try {
            dtPacket = getData(1024);
        } catch (SocketTimeoutException | PortUnreachableException e) {
            e.printStackTrace();
        }
        int i = 0;
        int en = 0;
        assert dtPacket != null;
        String directions = new String(dtPacket.getData());
        String[] direction = directions.split("#",-2);
        label:
        while(i < direction.length - 1){
            if(!(direction[i].trim().equals(""))){
                switch (direction[i].trim()) {
                    case "end":
                        if(direction[i+1].trim().equals("score")){
                            GameSettings.getGameStateOwner().completeGame(FinalScoreScene.COMPLETIONTYPE.SUCCESS);
                        }
                        else {
                            GameSettings.getGameStateOwner().completeGame(FinalScoreScene.COMPLETIONTYPE.QUIT);
                        }
                        break label;
                    case "e":
                        dataStorage.setReceivedEnemyDirections(en,direction[i+1],direction[i+2],
                                direction[i+3],direction[i+4]);
                        en++;
                        i = i + 5;
                        break;
                    case "remove":
                        try{
                            dataStorage.setRemove(parseInt(direction[i+1].trim()),
                                    userNumByPort.get(parseInt(direction[i + 1].trim())));
                            GameSettings.setNumPlayers(--numberOfPlayers);
                        }
                        catch (NumberFormatException e){
                            //Ignore if not valid, should not occur
                        }
                        i = i + 2;
                        break;
                    default:
                        try{
                            int j = userNumByPort.get(parseInt(direction[i].trim()));
                            dataStorage.setReceivedPlayerInfo(j,direction[i+1],direction[i+2],direction[i+3],
                                    direction[i+4],direction[i+5],direction[i+6],direction[i+7],direction[i+8]);
                        }
                        catch (NumberFormatException e){
                            //Ignore if not valid, should not occur
                        }
                        i = i + 9;
                        break;
                }
            }
        }
    }
}
