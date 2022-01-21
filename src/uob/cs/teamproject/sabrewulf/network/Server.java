package uob.cs.teamproject.sabrewulf.network;

import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.map.GameMap;
import uob.cs.teamproject.sabrewulf.map.GameMapWrapper;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The server class is responsible for the distribution of information from each {@link Client} to other clients.
 * It creates a {@link DatagramSocket} which is used to receive and send {@link DatagramPacket} to clients.
 */
public class Server {
    protected DatagramSocket dtSocket;
    protected boolean isConnected;
    private int numberOfPlayers;
    private final int numberOfEnemies;
    private int playersConnected;
    private List<Integer> ports;
    private HashMap<Integer,InetAddress> addresses;
    private List<Integer> unavailablePorts;
    private List<String> usernames;

    protected HashMap<Integer,String> playerSpawnsAt;
    private String enemiesSpawnAt;

    private GameMapWrapper gameMapWrapper;
    private Cell[][] cellGrid;

    /**
     * The server class constructor initiates a {@link DatagramSocket} and creates data important to run the game
     * @param numberOfPlayers a number of players
     * @param numberOfEnemies a number of enemies
     * @param port a port the server is listening on
     * @throws SocketException if the port is unavailable
     */
    protected Server(int numberOfPlayers, int numberOfEnemies, int port) throws SocketException {
        this.isConnected = false;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfEnemies = numberOfEnemies;
        playersConnected = 0;
        ports = new ArrayList<>();
        addresses= new HashMap<>();
        unavailablePorts = new ArrayList<>();
        usernames = new ArrayList<>();
        playerSpawnsAt = new HashMap<>();
        setInitialValues();

        dtSocket = new DatagramSocket(port);
        dtSocket.setSoTimeout(300000);

        createMap();
    }

    /**
     * Sends a message to all clients to notify each of them to start the game
     */
    public void sendStart(){
        try {
            for(int p : ports){
                byte[] data = ("start#"+p+"#").getBytes();
                DatagramPacket dtPacket = new DatagramPacket(data,data.length, addresses.get(p),p);
                dtSocket.send(dtPacket);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The start method for the server.
     * The {@link DatagramSocket} waits for the same number of {@link DatagramPacket}
     * as there are players in the game and saves the information it needs to continue the game
     * @throws IOException if an IO error occurs
     */
    protected void start() throws IOException {
        int k;
        for(int i = 0; i < numberOfPlayers; i++){
            do{
                k = -1;
                DatagramPacket dp = getData(16);
                String username = new String(dp.getData());
                int p = dp.getPort();
                InetAddress address = dp.getAddress();

                /* Check if the socket has not already received a datagram packet from this port */
                if(ports.contains(p)){
                    k++;
                }

                if (k == -1) {
                    /* Check if the username is not used by another player */
                    if(usernames.contains(username.trim())){
                        getNewUsername(address,p);
                        k++;
                    }
                    else {
                        ports.add(p);
                        addresses.put(p,address);
                        usernames.add(username.trim());
                        StringBuilder str = new StringBuilder();
                        boolean validCellFound = false;
                        Cell randomCell;
                        while(!validCellFound) {
                            /* find a random cell to spawn player on */
                            randomCell = gameMapWrapper.getRandomEmptyCellOnRows(10, 14);
                            /* cell returned from getRandomEmptyCellOnRows will not have another player in it */
                            /* so just need to check cell does not already contain a power-up or key */
                            /* if cell passes these checks, OK to add player to it */
                            if((!randomCell.hasPowerUp()) && (!randomCell.hasKey())) {
                                validCellFound = true;
                                str.append(randomCell.getX());
                                str.append("#");
                                str.append(randomCell.getY());
                                str.append("#");
                                playerSpawnsAt.put(p,str.toString());
                            }
                        }
                        playersConnected++;
                        /* Send usernames to each client that is connected
                         * to notify about the new player
                         */
                        for(int j = 0; j < playersConnected; j++){
                            sendUsernames(ports.get(j));
                        }
                    }

                }
            } while (k != -1);
        }
        if(playersConnected == numberOfPlayers){
            this.isConnected = true;
            for (int i = 0; i < numberOfPlayers; i++) {
                sendMap(ports.get(i));
            }
        }

        try {
            dtSocket.setSoTimeout(200000);
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * The update method for the server waits for {@link DatagramPacket}
     * from all clients, checks if anyone has quit the game or if the game has ended
     * and notifies the clients accordingly
     */
    protected void update(){
        List<DatagramPacket> packets = new ArrayList<>();
        List<DatagramPacket> tobeRemoved = new ArrayList<>();
        int remove = 0;

        for(int i = 0; i < numberOfPlayers; i++){
            try {
                packets.add(i,getData(256));
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String[] data = (new String(packets.get(i).getData())).split("#",-2);
            if(data[0].trim().equals("quit")){
                remove++;
                unavailablePorts.add(packets.get(i).getPort());
                tobeRemoved.add(packets.get(i));
            }
            else if(data[0].trim().equals("end")){
                sendEnd(true);
                return;
            }
        }

        numberOfPlayers = numberOfPlayers - remove;
        /* End game if a player has quit and only one player has left
         * or the player who quit hosted the server
         */
        if(GameSettings.getGameMode() != MODE.SINGLEPLAYER && numberOfPlayers == 1) {
            sendEnd(false);
        }
        /* Remove a player from the game if one has quit the game */
        else if(remove != 0){
            for(DatagramPacket r : tobeRemoved){
                packets.remove(r);
            }
            buildPacket(packets,true);
        }
        /* Distribute data from all clients to each client */
        else {
            buildPacket(packets,false);
        }

    }

    /**
     * Sets the initial values of the variables
     */
    protected void setInitialValues(){
        isConnected = false;
        playersConnected = 0;
        ports = new ArrayList<>();
        addresses= new HashMap<>();
        unavailablePorts = new ArrayList<>();
        usernames = new ArrayList<>();
        playerSpawnsAt = new HashMap<>();
    }

    /**
     * Creates a new {@link GameMap} and gets the positions for enemies to spawn on to be shared with the clients
     */
    protected void createMap(){
        int mapWidth = 1080;
        int mapHeight = 1080;
        int cellCountX = 14;
        int cellCountY = 14;
        GameMap map = new GameMap(cellCountX, cellCountY, mapWidth / cellCountX,
                mapHeight / cellCountY);
        this.cellGrid = map.getCellGrid();
        this.gameMapWrapper = map.getGameMapWrapper();

        StringBuilder str = new StringBuilder();
        for(int i = 0; i < numberOfEnemies; i++){
            Cell randomCell = gameMapWrapper.getRandomEmptyCellOnRows(0,4);
            randomCell.setEmpty(false);
            str.append(randomCell.getX());
            str.append("#");
            str.append(randomCell.getY());
            str.append("#");
        }
        enemiesSpawnAt = str.toString();
    }

    /**
     * Sends positions all players and enemies spawn at to each client
     * @throws IOException if an IO error occurs
     */
    protected void sendRandomCoordinates() throws IOException {
        DatagramPacket dp;
        StringBuilder str = new StringBuilder();
        for(int p : ports) {
            str.append(p);
            str.append("#");
            str.append(playerSpawnsAt.get(p));
            str.append("#");
        }
        str.append(enemiesSpawnAt);
        for(int p : ports){
            dp = new DatagramPacket(str.toString().getBytes(),str.toString().getBytes().length, addresses.get(p),p);
            dtSocket.send(dp);
        }
    }

    /**
     * Notifies all clients to end the game according to the reason it ended
     * @param bool true if the game has finished, false if the game ends because someone has quit
     */
    protected void sendEnd(boolean bool){
        String send;
        if(bool){
            send="end#score#";
        }
        else {
            send = "end#";
        }
        try {
            for(int p : ports){
                byte[] data = send.getBytes();
                DatagramPacket dtPacket = new DatagramPacket(data,data.length,addresses.get(p),p);
                dtSocket.send(dtPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Builds a message containing each player's username and score
     */
    protected void getScore(){
        StringBuilder scores = new StringBuilder();
        scores.append("scores#");
        ArrayList<Integer> ports = new ArrayList<>();
        for(int i = 0; i < numberOfPlayers; i++){
            boolean score = false;
            while(!score){
                DatagramPacket packet = null;
                try {
                    packet = getData(100);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                String[] data;
                if (packet != null) {
                    data = (new String(packet.getData())).trim().split("#");
                    if(data[0].trim().equals("score") && !ports.contains(packet.getPort())){
                        ports.add(packet.getPort());
                        scores.append(data[1]);
                        scores.append("#");
                        scores.append(data[2]);
                        scores.append("#");
                        score = true;
                    }
                }
            }
        }
        sendScores(scores.toString());
    }

    /**
     * Send message containing number of players connected, their usernames and ports
     * @param port a port to send the message to
     * @throws IOException if an IO error occurs
     */
    private void sendUsernames(int port) throws IOException {
        StringBuilder un = new StringBuilder();
        un.append(playersConnected);
        un.append("#");
        for(int i = 0; i < playersConnected; i++){
            un.append(usernames.get(i));
            un.append("#");
            un.append(ports.get(i));
            un.append("#");
        }
        byte[] data = un.toString().getBytes();
        DatagramPacket dtPacket = new DatagramPacket(data,data.length, addresses.get(port),port);
        dtSocket.send(dtPacket);
    }

    /**
     * Sends map to each client
     * @param port a port to send the map to
     * @throws IOException if an IO error occurs
     */
    private void sendMap(int port) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(cellGrid);
        byte[] data = byteArrayOutputStream.toByteArray();
        DatagramPacket dtPacket = new DatagramPacket(data,data.length,addresses.get(port),port);
        dtSocket.send(dtPacket);
    }

    /**
     * Distributes data of packets got from all clients to each client
     * @param datagramPackets a list of {@link DatagramPacket}
     * @param r true if any player has quit, false otherwise
     */
    private void buildPacket(List<DatagramPacket> datagramPackets, boolean r){
        DatagramPacket dp;
        int dataLength = 0;
        int offset = 0;
        for (DatagramPacket datagramPacket : datagramPackets) {
            try {
                dataLength = dataLength + datagramPacket.getData().length;
            }
            catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        StringBuilder remove = new StringBuilder();
        for (Integer unavailablePort : unavailablePorts) {
            remove.append("remove#");
            remove.append(unavailablePort);
            remove.append("#");
        }
        byte[] data;
        if (r){
            data = new byte[dataLength+remove.toString().length()];
        } else {
            data = new byte[dataLength];
        }

        for (DatagramPacket datagramPacket : datagramPackets) {
            System.arraycopy(datagramPacket.getData(), 0, data, offset, datagramPacket.getData().length);
            offset = offset + datagramPacket.getData().length;
        }
        if (r){
            System.arraycopy(remove.toString().getBytes(), 0, data, offset, remove.toString().getBytes().length);
        }

        for (DatagramPacket datagramPacket : datagramPackets) {
            dp = new DatagramPacket(data, data.length, datagramPacket.getAddress(), datagramPacket.getPort());
            try {
                dtSocket.send(dp);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> getUsernames(){
        return usernames;
    }

    /**
     * Notifies a particular client in case the username is unavailable
     * @param address an {@link InetAddress} of the client
     * @param port a port the client socket is listening on
     */
    private void getNewUsername(InetAddress address, int port){
        byte[] data = "unavailable".getBytes();
        DatagramPacket dtPacket = new DatagramPacket(data,data.length,address,port);
        try {
            dtSocket.send(dtPacket);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the message containing scores to each client
     * @param scores a message containing scores
     */
    private void sendScores(String scores){
        for(int port : ports){
            if(!unavailablePorts.contains(port)){
                DatagramPacket dp = new DatagramPacket(scores.getBytes(),scores.getBytes().length,
                        addresses.get(port), port);
                try {
                    dtSocket.send(dp);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Receives a {@link DatagramPacket} of given length
     * @param size the length of byte array to receive
     * @return a received {@link DatagramPacket}
     * @throws IOException if an IO error occurs
     */
    private DatagramPacket getData(int size) throws IOException {
        byte[] data = new byte [size];
        DatagramPacket dtPacket = new DatagramPacket(data,data.length);
        dtSocket.receive(dtPacket);
        return dtPacket;
    }
}
