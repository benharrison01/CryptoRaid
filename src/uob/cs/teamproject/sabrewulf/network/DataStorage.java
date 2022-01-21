package uob.cs.teamproject.sabrewulf.network;

import uob.cs.teamproject.sabrewulf.components.CharacterMovement;
import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.util.XYPair;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The DataStorage class is used to store data that is received by the client or could be send by the client to
 * the server. It allows other game components to get the information they need that has been stored by the client.
 */
public class DataStorage {

    private String username;
    private final List<String> usernames;

    private HashMap<String,Integer> scores;

    private final int numberOfEnemies;

    private HashMap<Integer,Integer> remove;

    private Cell[][] cellGrid;
    private final XYPair[] spawnAt;

    private XYPair[] transformCheck;

    private CharacterMovement.Direction[] receivedPlayerDirections;
    private CharacterMovement.Direction[] receivedPlayerIsFacing;
    private boolean[] receivedPlayerIsMoving;
    private boolean[] receivedPlayerInvisibilityBoost;
    private boolean[] receivedPlayerSpeedBoost;

    private CharacterMovement.Direction[] receivedEnemyDirections;
    private CharacterMovement.Direction[] receivedEnemyIsFacing;
    private boolean[] receivedEnemyIsMoving;

    public CharacterMovement.Direction[] enemyDirections;
    public CharacterMovement.Direction[] enemyIsFacing;
    public boolean[] enemyIsMoving;

    /**
     * The constructor of the DataStorage class
     * @param numOfPlayers a number of players
     * @param numberOfEnemies a number of enemies
     */
    public DataStorage(int numOfPlayers, int numberOfEnemies){
        this.numberOfEnemies = numberOfEnemies;
        usernames  = new CopyOnWriteArrayList<>();
        remove = new HashMap<>();
        receivedPlayerDirections = new CharacterMovement.Direction[numOfPlayers*2];
        receivedPlayerIsFacing = new CharacterMovement.Direction[numOfPlayers];
        receivedPlayerIsMoving = new boolean[numOfPlayers];
        receivedPlayerInvisibilityBoost = new boolean[numOfPlayers];
        receivedPlayerSpeedBoost = new boolean[numOfPlayers];
        receivedEnemyDirections = new CharacterMovement.Direction[numberOfEnemies*2];
        receivedEnemyIsFacing = new CharacterMovement.Direction[numberOfEnemies];
        receivedEnemyIsMoving = new boolean[numberOfEnemies];
        enemyDirections = new CharacterMovement.Direction[numberOfEnemies*2];
        enemyIsFacing = new CharacterMovement.Direction[numberOfEnemies];
        enemyIsMoving = new boolean[numberOfEnemies];
        spawnAt = new XYPair[numOfPlayers+numberOfEnemies];
        transformCheck = new XYPair[numOfPlayers];
        setInitialEnemyInfo();
    }

    /**
     * Store received enemy directions by the number of the enemy
     * @param num the number of the enemy
     * @param dirX the x direction of the enemy character
     * @param dirY the y direction of the enemy character
     * @param isMoving true if the enemy character is moving, false otherwise
     * @param facing the direction the enemy character is facing
     */
    protected void setReceivedEnemyDirections(int num, String dirX, String dirY, String isMoving, String facing){
        try{
            receivedEnemyDirections[num*2] = CharacterMovement.Direction.valueOf(dirX.trim());
        } catch (IllegalArgumentException | NullPointerException e){
            receivedEnemyDirections[num*2] = CharacterMovement.Direction.NONE;
        }

        try{
            receivedEnemyDirections[num*2+1] = CharacterMovement.Direction.valueOf(dirY.trim());
        } catch (IllegalArgumentException | NullPointerException e){
            receivedEnemyDirections[num*2+1] = CharacterMovement.Direction.DOWN;
        }

        receivedEnemyIsMoving[num] = isMoving.equals("true");

        try{
            receivedEnemyIsFacing[num] = CharacterMovement.Direction.valueOf(facing.trim());
        } catch (IllegalArgumentException | NullPointerException e){
            receivedEnemyIsFacing[num] = CharacterMovement.Direction.DOWN;
        }
    }

    /**
     * Store received player directions by the number of the player
     * @param num the number of the player
     * @param dirX the x direction of the character
     * @param dirY the y direction of the character
     * @param isMoving true if the character is moving, false otherwise
     * @param facing the direction the character is facing
     * @param invisibility true if the character is using an invisibility boost, false otherwise
     * @param speed true if the character is using a speed boost, false otherwise
     * @param transformX the x position of character transform object
     * @param transformY the y position of character transform object
     */
    protected void setReceivedPlayerInfo(int num, String dirX, String dirY, String isMoving, String facing,
                                      String invisibility, String speed, String transformX, String transformY){
        try{
            receivedPlayerDirections[num*2] = CharacterMovement.Direction.valueOf(dirX.trim());
        }
        catch (IllegalArgumentException | NullPointerException e){
            receivedPlayerDirections[num*2] = CharacterMovement.Direction.NONE;
        }

        try{
            receivedPlayerDirections[num*2+1] = CharacterMovement.Direction.valueOf(dirY.trim());
        }
        catch (IllegalArgumentException | NullPointerException e){
            receivedPlayerDirections[num*2+1] = CharacterMovement.Direction.DOWN;
        }

        receivedPlayerIsMoving[num] = isMoving.equals("true");

        try{
            receivedPlayerIsFacing[num] = CharacterMovement.Direction.valueOf(facing.trim());
        }
        catch (IllegalArgumentException | NullPointerException e){
            receivedPlayerIsFacing[num] = CharacterMovement.Direction.DOWN;
        }

        receivedPlayerInvisibilityBoost[num] = invisibility.equals("true");

        receivedPlayerSpeedBoost[num] = speed.equals("true");

        try{
            transformCheck[num] = new XYPair(Double.parseDouble(transformX), Double.parseDouble(transformY));
        }
        catch (NullPointerException | NumberFormatException e) {
            //Do not make any changes
        }
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setCellGrid(Cell[][] grid){
        this.cellGrid = grid;
    }

    public void setSpawnAt(int num, XYPair pair){
        spawnAt[num] = pair;
    }

    public void setEnemyDirections(int num, CharacterMovement.Direction dirX, CharacterMovement.Direction dirY,
                                   CharacterMovement.Direction facing){
        enemyDirections[num*2] = dirX;
        enemyDirections[num*2+1] = dirY;
        enemyIsFacing[num] = facing;
    }

    public void setEnemyIsMoving(int num, boolean isMoving){
        enemyIsMoving[num] = isMoving;
    }

    protected void setRemove(int port, int num){
        remove.put(port,num);
    }

    protected void setInitialEnemyInfo() {
        for(int i = 0; i < numberOfEnemies; i++){
            enemyDirections[i] = CharacterMovement.Direction.NONE;
            receivedEnemyDirections[i] = CharacterMovement.Direction.NONE;
            enemyDirections[i+1] = CharacterMovement.Direction.DOWN;
            receivedEnemyDirections[i+1] = CharacterMovement.Direction.DOWN;
            enemyIsMoving[i] = false;
            enemyIsFacing[i] = CharacterMovement.Direction.DOWN;
            receivedEnemyIsMoving[i] = false;
            receivedEnemyIsFacing[i] = CharacterMovement.Direction.DOWN;
        }
    }

    /**
     * Add username to the list of all usernames
     * @param username a username to be added
     */
    public void addUsername(String username){
        usernames.add(username);
    }

    public Cell[][] getCellGrid(){
        return cellGrid;
    }

    public String getUsername() {
        return username;
    }

    public XYPair[] getSpawnAt(){
        return spawnAt;
    }

    public XYPair getTransformCheck(int num){
        return transformCheck[num];
    }

    public CharacterMovement.Direction[] getPlayerDirections(){
        return receivedPlayerDirections;
    }

    public CharacterMovement.Direction getPlayerIsFacing(int num){
        return receivedPlayerIsFacing[num];
    }

    public boolean getPlayerIsMoving(int num){
        return receivedPlayerIsMoving[num];
    }

    public boolean getInvisibilityBoost(int num){
        return receivedPlayerInvisibilityBoost[num];
    }

    public boolean getSpeedBoost(int num) {
        return receivedPlayerSpeedBoost[num];
    }

    public CharacterMovement.Direction[] getEnemyDirections(){
        return receivedEnemyDirections;
    }

    public CharacterMovement.Direction[] getEnemyIsFacing(){
        return receivedEnemyIsFacing;
    }

    public boolean[] getEnemyIsMoving(){
        return receivedEnemyIsMoving;
    }

    public HashMap<Integer,Integer> getRemove(){
        return remove;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public HashMap<String, Integer> getScores() {
        return scores;
    }

    public void setScores(HashMap<String, Integer> scores) {
        this.scores = scores;
    }
}
