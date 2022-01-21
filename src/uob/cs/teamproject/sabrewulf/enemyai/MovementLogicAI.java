package uob.cs.teamproject.sabrewulf.enemyai;

import javafx.scene.image.Image;
import uob.cs.teamproject.sabrewulf.GameComponent;
import uob.cs.teamproject.sabrewulf.GameSettings;
import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.map.GameMapWrapper;
import uob.cs.teamproject.sabrewulf.ResourceManager;
import uob.cs.teamproject.sabrewulf.components.*;
import uob.cs.teamproject.sabrewulf.network.DataStorage;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODE;
import uob.cs.teamproject.sabrewulf.util.Transform;
import uob.cs.teamproject.sabrewulf.util.XYPair;

public class MovementLogicAI extends GameComponent {

    private Transform transform;
    private CharacterAnimator characterAnimator;
    private CharacterMovement.Direction directionX;
    private CharacterMovement.Direction directionY;
    private CharacterMovement.Direction facing = CharacterMovement.Direction.UP;
    private static GameMapWrapper mapWrapper;
    private CharacterMovement characterMovement;
    private Cell randomCellToReach;
    private Cell theCurrentCell;
    private boolean isChasing = false;
    private DataStorage dataStorage;
    private int num;
    private MazeSolver m;
    private Boolean firstTime = true;
    private int[][] coordsToReach;
    private EnemyCollider enemyCollider;

    /**
     * The constructor for the AI's logic/ decision making class.
     * The constructor spawns the AI on a cell around the top of the map.
     * @param transform: The Transform component belonging to the AI.
     * @param characterAnimator: The CharacterAnimator component belonging to the AI.
     * @param map: The GameMap of the current game so the AI can make appropriate decisions.
     * @param enemyCollider: The EnemyCollider system belonging to the AI.
     * @param characterMovement: The CharacterMovement component belonging to the AI.
     * @param dataStorage The DataStorage object where character directions are stored to be sent over the network
     * @param num The number of the enemy character
     */
    public MovementLogicAI(Transform transform, CharacterAnimator characterAnimator, GameMapWrapper map, EnemyCollider enemyCollider, CharacterMovement characterMovement, DataStorage dataStorage, int num) {
        super();
        this.mapWrapper = map;
        this.transform = transform;
        this.characterAnimator = characterAnimator;
        this.enemyCollider = enemyCollider;
        this.dataStorage = dataStorage;
        this.characterMovement = characterMovement;
        this.num = num;
        setSprites();
        setSecondarySprites();
        setInitialDirection();

        randomCellToReach = mapWrapper.getRandomCell();

        try{
            characterMovement.setIsMoving(false);
        }
        catch(Exception ignored){

        }

        m = new MazeSolver(mapWrapper);
    }

    /**
     * Sets the sprites as images from the predefined file location and passes them
     * to the CharacterAnimator component.
     */
    private void setSprites() {
        final String FILESTR = "images/enemy/enemy_%s.png";

        Image[] rightStrips = new Image[4];
        rightStrips[0] = ResourceManager.getImage(String.format(FILESTR, "E_1and3"));
        rightStrips[1] = ResourceManager.getImage(String.format(FILESTR, "E_2"));
        rightStrips[2] = ResourceManager.getImage(String.format(FILESTR, "E_1and3"));
        rightStrips[3] = ResourceManager.getImage(String.format(FILESTR, "E_4"));

        Image[] leftStrips = new Image[4];
        leftStrips[0] = ResourceManager.getImage(String.format(FILESTR, "W_1and3"));
        leftStrips[1] = ResourceManager.getImage(String.format(FILESTR, "W_2"));
        leftStrips[2] = ResourceManager.getImage(String.format(FILESTR, "W_1and3"));
        leftStrips[3] = ResourceManager.getImage(String.format(FILESTR, "W_4"));

        Image[] upStrips = new Image[4];
        upStrips[0] = ResourceManager.getImage(String.format(FILESTR, "N_1and3"));
        upStrips[1] = ResourceManager.getImage(String.format(FILESTR, "N_2"));
        upStrips[2] = ResourceManager.getImage(String.format(FILESTR, "N_1and3"));
        upStrips[3] = ResourceManager.getImage(String.format(FILESTR, "N_4"));

        Image[] downStrips = new Image[4];
        downStrips[0] = ResourceManager.getImage(String.format(FILESTR, "S_1and3"));
        downStrips[1] = ResourceManager.getImage(String.format(FILESTR, "S_2"));
        downStrips[2] = ResourceManager.getImage(String.format(FILESTR, "S_1and3"));
        downStrips[3] = ResourceManager.getImage(String.format(FILESTR, "S_4"));

        SpriteSets spriteSets = new SpriteSets();
        spriteSets.setUpStrips(upStrips);
        spriteSets.setDownStrips(downStrips);
        spriteSets.setLeftStrips(leftStrips);
        spriteSets.setRightStrips(rightStrips);

        characterMovement.getCharacterAnimator().setSpriteSets(spriteSets);
    }

    /**
     * Sets the secondary sprites as images from the predefined file location and passes them
     * to the CharacterAnimator component.
     */
    private void setSecondarySprites() {
        final String FILESTR = "images/enemy_alerted/enemy_alerted_%s.png";

        Image[] rightStrips = new Image[4];
        rightStrips[0] = ResourceManager.getImage(String.format(FILESTR, "E_1and3"));
        rightStrips[1] = ResourceManager.getImage(String.format(FILESTR, "E_2"));
        rightStrips[2] = ResourceManager.getImage(String.format(FILESTR, "E_1and3"));
        rightStrips[3] = ResourceManager.getImage(String.format(FILESTR, "E_4"));

        Image[] leftStrips = new Image[4];
        leftStrips[0] = ResourceManager.getImage(String.format(FILESTR, "W_1and3"));
        leftStrips[1] = ResourceManager.getImage(String.format(FILESTR, "W_2"));
        leftStrips[2] = ResourceManager.getImage(String.format(FILESTR, "W_1and3"));
        leftStrips[3] = ResourceManager.getImage(String.format(FILESTR, "W_4"));

        Image[] upStrips = new Image[4];
        upStrips[0] = ResourceManager.getImage(String.format(FILESTR, "N_1and3"));
        upStrips[1] = ResourceManager.getImage(String.format(FILESTR, "N_2"));
        upStrips[2] = ResourceManager.getImage(String.format(FILESTR, "N_1and3"));
        upStrips[3] = ResourceManager.getImage(String.format(FILESTR, "N_4"));

        Image[] downStrips = new Image[4];
        downStrips[0] = ResourceManager.getImage(String.format(FILESTR, "S_1and3"));
        downStrips[1] = ResourceManager.getImage(String.format(FILESTR, "S_2"));
        downStrips[2] = ResourceManager.getImage(String.format(FILESTR, "S_1and3"));
        downStrips[3] = ResourceManager.getImage(String.format(FILESTR, "S_4"));

        SpriteSets spriteSets = new SpriteSets();
        spriteSets.setUpStrips(upStrips);
        spriteSets.setDownStrips(downStrips);
        spriteSets.setLeftStrips(leftStrips);
        spriteSets.setRightStrips(rightStrips);

        characterMovement.getCharacterAnimator().setSecondarySpriteSets(spriteSets);
    }

    /**
     * Sets the initial direction of the AI.
     */
    private void setInitialDirection() {
        this.directionX = CharacterMovement.Direction.NONE;
        this.directionY = CharacterMovement.Direction.DOWN;
    }

    /**
     * The update method which calls updateAstarApproach to find a path.
     * @param t: The current time in nanoseconds.
     */
    public void update(long t) {
        updateAstarApproach();
    }

    /**
     * Checks if the AI has passed the middle of the cell to prevent
     * them from reaching a new cell and turning into a new direction instantly.
     * @return a boolean of whether the AI is around the middle of the current cell.
     */
    private Boolean aroundMiddleOfCell() {
        int x = (int) transform.position.x;
        int y = (int) transform.position.y;
        Cell AIcurrentCell = mapWrapper.coordsToCell(x, y);
        int midpointX = mapWrapper.getCellCoordinates(AIcurrentCell)[0];
        int midpointY = mapWrapper.getCellCoordinates(AIcurrentCell)[1];

        if (directionX == CharacterMovement.Direction.LEFT) {
            if (x <= midpointX) {
                return true;
            }
        }
        else if (directionX == CharacterMovement.Direction.RIGHT) {
            if (x >= midpointX) {
                return true;
            }
        }
        else if (directionY == CharacterMovement.Direction.UP) {
            if (y <= midpointY) {
                return true;
            }
        }
        else if (directionY == CharacterMovement.Direction.DOWN) {
            if (y >= midpointY) {
                return true;
            }
        }
        return false;
    }

    /**
     * Uses the MazeSolver class to perform an A* search from the AI to the closest player.
     * If there is no player within the detection radius, the AI traverses the maze randomly.
     */
    private void updateAstarApproach() {
        Cell AIcurrentCell;
        Cell playerCurrentCell;

        AIcurrentCell = mapWrapper.coordsToCell((int) transform.position.x, (int) transform.position.y);

        XYPair playerXY = enemyCollider.getTargetPlayerPos();

        if (playerXY == null) {
            isChasing = false;
            characterAnimator.setUseSecondaryStrips(false);
            //traverse randomly
            if (AIcurrentCell == randomCellToReach || !characterMovement.getIsMoving()) {

                playerCurrentCell = mapWrapper.getRandomCell();
                randomCellToReach = playerCurrentCell;
            }
            else {
                playerCurrentCell = randomCellToReach;
            }
        }
        else {
            if (!isChasing) {
                isChasing = true;
            }

            //move towards player
            randomCellToReach = mapWrapper.getRandomCell(); //prevents ai stuck in loops
            playerCurrentCell = mapWrapper.coordsToCell((int) playerXY.x, (int) playerXY.y);
            characterAnimator.setUseSecondaryStrips(true);
        }


            //only get a new direction if in the middle of the cell
            if (aroundMiddleOfCell() || firstTime) {
                //only update if on a fresh cell, makes it more efficient and stops ai running into walls
                if (theCurrentCell == AIcurrentCell) {
                    //do nothing new, we have already made a decision once on this cell
                }
                else {
                    theCurrentCell = AIcurrentCell;
                    coordsToReach = m.solvePathAsCells(AIcurrentCell, playerCurrentCell);
                    firstTime = false;
                }

            }



        int[] nextCoords;

        if (coordsToReach == null) {
            //do nothing, to prevent crash, although this should not occur anyway
        }
        else {
            if (coordsToReach.length >= 2) {
                nextCoords = coordsToReach[1];
                int[] currentCoords = new int[]{AIcurrentCell.getCoordX(), AIcurrentCell.getCoordY()};
                int curX = currentCoords[0];
                int curY = currentCoords[1];
                int nodeX = nextCoords[0];
                int nodeY = nextCoords[1];
                if (curX > nodeX) {
                    directionY = CharacterMovement.Direction.NONE;
                    directionX = CharacterMovement.Direction.LEFT;
                    facing = CharacterMovement.Direction.LEFT;
                } else if (curX < nodeX) {
                    directionY = CharacterMovement.Direction.NONE;
                    directionX = CharacterMovement.Direction.RIGHT;
                    facing = CharacterMovement.Direction.RIGHT;
                } else if (curY > nodeY) {
                    directionY = CharacterMovement.Direction.UP;
                    directionX = CharacterMovement.Direction.NONE;
                    facing = CharacterMovement.Direction.UP;
                } else if (curY < nodeY) {
                    directionY = CharacterMovement.Direction.DOWN;
                    directionX = CharacterMovement.Direction.NONE;
                    facing = CharacterMovement.Direction.DOWN;
                }

                if(GameSettings.getGameMode() == MODE.SINGLEPLAYER){
                    dataStorage.setEnemyDirections(num, directionX, directionY, facing);
                    dataStorage.setEnemyIsMoving(num, true);
                    characterMovement.setIsMoving(true);
                    characterMovement.changeDirection(directionX, directionY);
                    characterMovement.setFacing(facing);
                }
                else {
                    dataStorage.setEnemyDirections(num, directionX, directionY, facing);
                    dataStorage.setEnemyIsMoving(num, true);
                    CharacterMovement.Direction[] directions = this.dataStorage.getEnemyDirections();
                    boolean[] isMoving = this.dataStorage.getEnemyIsMoving();
                    CharacterMovement.Direction[] isFacing = dataStorage.getEnemyIsFacing();
                    if(directions!=null && isMoving != null && isFacing != null){
                        if(directions[num*2] != null && directions[num*2+1] != null) {
                            characterMovement.changeDirection(directions[num*2], directions[num*2+1]);
                            characterMovement.setFacing(isFacing[num]);
                            characterMovement.setIsMoving(isMoving[num]);
                        }
                    }
                }

            } else {
                characterMovement.setIsMoving(false);
                directionY = CharacterMovement.Direction.NONE;
                directionX = CharacterMovement.Direction.NONE;
                dataStorage.setEnemyDirections(num, directionX, directionY, facing);
                dataStorage.setEnemyIsMoving(num, false);
                characterMovement.changeDirection(directionX, directionY);
                firstTime = true;
                playerCurrentCell = mapWrapper.getRandomCell();
                coordsToReach = m.solvePathAsCells(AIcurrentCell, playerCurrentCell);

            }
        }
    }


}
