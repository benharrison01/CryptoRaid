package uob.cs.teamproject.sabrewulf;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import uob.cs.teamproject.sabrewulf.achievements.StatisticsTracker;
import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.audio.Audio;
import uob.cs.teamproject.sabrewulf.map.Divider;
import uob.cs.teamproject.sabrewulf.map.GameMap;
import uob.cs.teamproject.sabrewulf.collisions.ColliderTag;
import uob.cs.teamproject.sabrewulf.collisions.CollisionSystem;
import uob.cs.teamproject.sabrewulf.map.GameMapWrapper;
import uob.cs.teamproject.sabrewulf.components.*;
import uob.cs.teamproject.sabrewulf.enemyai.MovementLogicAI;
import uob.cs.teamproject.sabrewulf.input.Input;
import uob.cs.teamproject.sabrewulf.ui.selectors.DIFFICULTY;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODE;
import uob.cs.teamproject.sabrewulf.ui.selectors.MODEL;
import uob.cs.teamproject.sabrewulf.network.NetworkSystem;
import uob.cs.teamproject.sabrewulf.util.Subscriber;
import uob.cs.teamproject.sabrewulf.util.Subscription;
import uob.cs.teamproject.sabrewulf.util.Transform;
import uob.cs.teamproject.sabrewulf.rendering.Renderer;
import uob.cs.teamproject.sabrewulf.util.XYPair;

/**
 * A GameWorld instance creates, stores, updates and eventually removes the GameComponents required for a single run
 * of the game.
 */
public class GameWorld {

    /* game systems */
    private final Audio audioSystem;
    private final CollisionSystem collisionSystem;
    private final Input inputSystem;
    private final NetworkSystem networkSystem;
    private final StatisticsTracker statisticsTracker;
    private final Renderer renderer;

    /* collection of GameComponents */
    private final Subscription<GameComponent> gameComponents = new Subscription<>();

    /* map generation parameters */
    private final int mapWidth = 1080;
    private final int mapHeight = 1080;
    private final int cellCountX = 14;
    private final int cellCountY = 14;
    private final int wallThickness = 5;

    /* width and height of players and enemies */
    private final double PLAYER_WIDTH = 28;
    private final double PLAYER_HEIGHT = 36;
    private final double ENEMY_WIDTH = 30;
    private final double ENEMY_HEIGHT = 46;

    /* the number of players in this game */
    private final int numOfPlayers;

    /* the number of coins currently in the game */
    private int numCoins = 0;

    /**
     * After initialising, create all of the GameComponents which are required at the start of the game.
     * @param audioSystem the {@link Audio} system to use
     * @param collisionSystem the {@link CollisionSystem} to use
     * @param inputSystem the {@link Input} system to use
     * @param networkSystem the {@link NetworkSystem} to use
     * @param statisticsTracker the {@link StatisticsTracker} to use
     * @param renderer the {@link Renderer} to use
     */
    public GameWorld(Audio audioSystem, CollisionSystem collisionSystem, Input inputSystem,
                     NetworkSystem networkSystem, Renderer renderer, StatisticsTracker statisticsTracker) {

        this.audioSystem = audioSystem;
        this.collisionSystem = collisionSystem;
        this.inputSystem = inputSystem;
        this.networkSystem = networkSystem;
        this.statisticsTracker = statisticsTracker;
        this.renderer = renderer;
        this.numOfPlayers = GameSettings.getNumPlayers();

        buildWorld();
    }

    /** call {@link GameComponent#start()} on every stored {@link GameComponent} */
    public void start() {
        for (GameComponent gc : gameComponents) {
            gc.start();
        }
    }

    /** call {@link GameComponent#update(long)} on every stored {@link GameComponent} */
    public void update(long now) {
        for (GameComponent gc : gameComponents) {
            gc.update(now);
        }
    }

    /** call {@link GameComponent#remove()} on every stored {@link GameComponent} */
    public void remove() {
        for (GameComponent gc : gameComponents) {
            gc.remove();
        }
    }

    /* add a GameComponent to the collection of GameComponents */
    private void addComponentToWorld(GameComponent component) {
        gameComponents.addSubscriber(new Subscriber<>(component));
    }

    /* add a GameComponent to a GameEntity and to the collection of GameComponents */
    private void addComponentToEntity(GameComponent component, GameEntity entity) {
        Subscriber<GameComponent> componentSub = new Subscriber<>(component);
        gameComponents.addSubscriber(componentSub);
        entity.addComponent(componentSub);
    }

    /* create all of the GameComponents which are required at the start of the game */
    private void buildWorld() {

        renderer.setCanvasColor(Color.BLACK);

        Cell[][] cellGrid = networkSystem.getClient().getDataStorage().getCellGrid();
        GameMapWrapper mapData = new GameMap(cellCountX, cellCountY, mapWidth/cellCountX,
                mapHeight/cellCountY, cellGrid).getGameMapWrapper();

        XYPair[] spawnAt = networkSystem.getClient().getDataStorage().getSpawnAt();
        createInventory();

        placeWalls(mapData);
        placeItems(mapData, spawnAt);
        placeEnemies(mapData,spawnAt);
        placePlayers(mapData, spawnAt);
        addFinishChecker();
    }

    /* add the walls into the world */
    private void placeWalls(GameMapWrapper mapData) {

        int cellWidth = mapData.getCellDimensions()[0];
        int cellHeight = mapData.getCellDimensions()[1];

        for(int gridX = 0; gridX < cellCountX; gridX++) {
            for(int gridY = 0; gridY < cellCountY; gridY++) {

                Cell current = mapData.getCellGrid()[gridX][gridY];

                double x = (gridX * cellWidth) - (mapWidth*0.5f) + cellWidth*0.5f;
                double y = (gridY * cellHeight) - (mapHeight*0.5f) + cellHeight*0.5f;

                Divider[] walls = current.getCellDividers();

                for (int i = 0; i < 4; i++) {
                    if (walls[i].isActive()) {
                        boolean wallAlreadyAdded = false;
                        double wallPosX=0, wallPosY=0;      // these default values get overwritten where necessary,
                        double wallWidth=0, wallHeight=0;   // they're just to appease the compiler
                        switch (i) {
                            case 0: /* wall on -y side (above) */
                                if (gridY == 0) {
                                    wallPosX = x;
                                    wallPosY = y - cellWidth*0.5f;
                                    wallWidth = cellWidth;
                                    wallHeight = wallThickness;
                                } else {
                                    wallAlreadyAdded = true;
                                }
                                break;
                            case 1: /* wall on +y side (below) */
                                wallPosX = x;
                                wallPosY = y + cellWidth*0.5f;
                                wallWidth = cellWidth;
                                wallHeight = wallThickness;
                                break;
                            case 2: /* wall on +x side (right) */
                                wallPosX = x + cellWidth*0.5f;
                                wallPosY = y;
                                wallWidth = wallThickness;
                                wallHeight = cellHeight;
                                break;
                            case 3: /* wall on -x side (left) */
                                if (gridX == 0) {
                                    wallPosX = x - cellWidth*0.5f;
                                    wallPosY = y;
                                    wallWidth = wallThickness;
                                    wallHeight = cellHeight;
                                } else {
                                    wallAlreadyAdded = true;
                                }
                                break;
                            default:
                                assert false; /* switch statement is missing cases */
                                return;
                        }
                        if (!wallAlreadyAdded) {
                            createWall(wallPosX, wallPosY, wallWidth, wallHeight, walls[i].getDividerType());
                        }
                    }
                }
            }
        }
    }

    /* add the items into the world */
    private void placeItems(GameMapWrapper mapData, XYPair[] spawnAt) {
        for(int gridX = 0; gridX < cellCountX; gridX++) {
            for(int gridY = 0; gridY < cellCountY; gridY++) {
                Cell cell = mapData.getCellGrid()[gridX][gridY];
                if(cell.hasKey()) {
                    createKey(cell.getX(), cell.getY(), cell.getKeyType());
                }
                else if(cell.hasPowerUp()) {
                    createPowerUp(cell.getX(), cell.getY(), cell.getPowerUpType());
                }
                else {
                    /* only spawn in a coin if there is no player in that cell */
                    /* (players cannot spawn in cells with powerups or keys)   */
                    boolean containsPlayer = false;
                    for (int i = 0; i < numOfPlayers; i++) {
                        XYPair playerCoords = spawnAt[i];
                        if (playerCoords.x == cell.getX() && playerCoords.y == cell.getY()) {
                            containsPlayer = true;
                            break;
                        }
                    }
                    if (!containsPlayer) {
                        createCoin(cell.getX(), cell.getY());
                    }
                }
            }
        }
    }

    /* add the enemies into the world */
    private void placeEnemies(GameMapWrapper mapData, XYPair[] spawnAt) {
        // each of these can eventually have different numbers of enemies and varying Intelligence.SMART and
        // Intelligence.NORMAL
        // State.CHASE isn't needed but I'm keeping it until I know the collisions works.
        if(GameSettings.getModel() == MODEL.SERVER || GameSettings.getGameMode() == MODE.SINGLEPLAYER){
            if (GameSettings.getDifficulty() == DIFFICULTY.EASY) {
                createEnemy(mapData, 300, 2, spawnAt,0);
                createEnemy(mapData, 300, 2, spawnAt, 1);
                createEnemy(mapData, 300, 2, spawnAt, 2);
            }
            else if (GameSettings.getDifficulty() == DIFFICULTY.MEDIUM) {
                createEnemy(mapData, 300, 2, spawnAt,0);
                createEnemy(mapData, 300, 3, spawnAt,1);
                createEnemy(mapData, 500, 2, spawnAt,2);
                createEnemy(mapData, 500, 3, spawnAt,3);
            }
            else if (GameSettings.getDifficulty() == DIFFICULTY.HARD) {
                createEnemy(mapData, 600, 3, spawnAt,0);
                createEnemy(mapData, 600, 3, spawnAt,1);
                createEnemy(mapData, 600, 3, spawnAt,2);
                createEnemy(mapData, 600, 3, spawnAt,3);
            }
        } else {
            if (GameSettings.getDifficulty() == DIFFICULTY.EASY) {
                createRemoteEnemy(mapData, 300, 2,spawnAt, 0);
                createRemoteEnemy(mapData, 300, 2, spawnAt, 1);
                createRemoteEnemy(mapData, 300, 2, spawnAt,2);
            }
            else if (GameSettings.getDifficulty() == DIFFICULTY.MEDIUM) {
                createRemoteEnemy(mapData, 300, 2, spawnAt,0);
                createRemoteEnemy(mapData, 300, 3, spawnAt,1);
                createRemoteEnemy(mapData, 500, 2, spawnAt,2);
                createRemoteEnemy(mapData, 500, 3, spawnAt,3);
            }
            else if (GameSettings.getDifficulty() == DIFFICULTY.HARD) {
                createRemoteEnemy(mapData, 600, 3,spawnAt,0);
                createRemoteEnemy(mapData, 600, 3,spawnAt,1);
                createRemoteEnemy(mapData, 600, 3, spawnAt,2);
                createRemoteEnemy(mapData, 600, 3, spawnAt,3);
            }
        }
    }

    /* add the player(s) into the world */
    private void placePlayers(GameMapWrapper mapData, XYPair[] spawnAt) {
        createThisPlayer(mapData, spawnAt);
        for(int i = 1; i < numOfPlayers; i++){
            createOtherPlayer(mapData, spawnAt, i);
        }
    }

    /* create a player character controlled by this game's player (rather than a networked player) */
    private void createThisPlayer(GameMapWrapper mapData, XYPair[] spawnAt) {
        Transform transform = new Transform(spawnAt[0], PLAYER_WIDTH, PLAYER_HEIGHT);

        CharacterAnimator characterAnimator = new CharacterAnimator(renderer, transform);
        CharacterMovement characterMovement = new CharacterMovement(characterAnimator, transform, mapData);
        PlayerCollider playerCollider = new PlayerCollider(collisionSystem, transform);
        Player player = new Player(transform,
                characterMovement,
                inputSystem,
                playerCollider,
                statisticsTracker,
                audioSystem,
                networkSystem.getClient(),
                mapData,
                networkSystem.getClient().getDataStorage());

        addComponentToWorld(characterMovement);
        addComponentToWorld(characterAnimator);
        addComponentToWorld(playerCollider);
        addComponentToWorld(player);
    }

    /* create a player character controlled over a network */
    private void createOtherPlayer(GameMapWrapper mapData, XYPair[] spawnAt, int num /* sprites */) {
        GameEntity entity = new GameEntity();

        Transform transform = new Transform(spawnAt[num], PLAYER_WIDTH, PLAYER_HEIGHT);
        CharacterAnimator characterAnimator = new CharacterAnimator(renderer, transform/* sprites */);
        CharacterMovement characterMovement = new CharacterMovement(characterAnimator, transform, mapData);
        PlayerCollider playerCollider = new PlayerCollider(collisionSystem, transform);
        RemotePlayer remotePlayer = new RemotePlayer(transform,characterAnimator, playerCollider, characterMovement,
                num, entity, networkSystem.getClient().getDataStorage());

        addComponentToEntity(characterAnimator,entity);
        addComponentToEntity(characterMovement,entity);
        addComponentToEntity(playerCollider,entity);
        addComponentToEntity(remotePlayer,entity);
    }

    /* create an AI-controlled enemy character if the host of the game is hosting a server*/
    private void createEnemy(GameMapWrapper mapData, int detectionRadius, int speed, XYPair[] spawnAt,
                             int num) {
        Transform transform = new Transform(spawnAt[(numOfPlayers+num)], ENEMY_WIDTH, ENEMY_HEIGHT);

        CharacterAnimator characterAnimator = new CharacterAnimator(renderer, transform);
        CharacterMovement characterMovement = new CharacterMovement(characterAnimator, transform, mapData);
        characterMovement.setSpeed(speed);
        EnemyCollider enemyCollider = new EnemyCollider(collisionSystem, transform, detectionRadius);
        MovementLogicAI movementLogicAI = new MovementLogicAI(transform, characterAnimator, mapData,
                enemyCollider, characterMovement, networkSystem.getClient().getDataStorage(), num);

        addComponentToWorld(characterAnimator);
        addComponentToWorld(characterMovement);
        addComponentToWorld(movementLogicAI);
        addComponentToWorld(enemyCollider);
    }

    /* create an enemy character which replicates the behaviour of the enemy controlled by AI in the server host */
    private void createRemoteEnemy(GameMapWrapper mapData, int detectionRadius, int speed, XYPair[] spawnAt,
                                   int num) {
        Transform transform = new Transform(spawnAt[(numOfPlayers+num)], ENEMY_WIDTH, ENEMY_HEIGHT);

        CharacterAnimator characterAnimator = new CharacterAnimator(renderer, transform);
        CharacterMovement characterMovement = new CharacterMovement(characterAnimator, transform, mapData);
        characterMovement.setSpeed(speed);
        EnemyCollider enemyCollider = new EnemyCollider(collisionSystem, transform, detectionRadius);
        RemoteEnemy remoteEnemy = new RemoteEnemy(enemyCollider, characterAnimator, characterMovement,
                networkSystem.getClient().getDataStorage(), num);

        addComponentToWorld(characterAnimator);
        addComponentToWorld(characterMovement);
        addComponentToWorld(remoteEnemy);
        addComponentToWorld(enemyCollider);
    }

    /* create a coin for players to pick up */
    private void createCoin(double posX, double posY) {
        final double COIN_WIDTH = 32, COIN_HEIGHT = 32;
        final Image COIN_IMAGE = ResourceManager.getImage("images/mapItems/bitcoin_logo.png");
        GameEntity entity = createItem(posX, posY, COIN_WIDTH, COIN_HEIGHT, ColliderTag.COIN, 0, COIN_IMAGE);

        addComponentToEntity(new GameComponent() {
            @Override
            public void remove() {
                numCoins--;
            }
        }, entity);
        numCoins++;
    }

    /* add inventory to store the players score and other game data */
    private void createInventory() {
        Inventory inventory = new Inventory();
        addComponentToWorld(inventory);
        GameSettings.setInventory(inventory);
    }

    /* create a key for players to pick up */
    private void createKey(double posX, double posY, Cell.KeyType keyType) {
        final double KEY_WIDTH = 60, KEY_HEIGHT = 50;

        Image image;
        switch (keyType) {
            case BLUEKEY:
                image = ResourceManager.getImage("images/mapItems/blue_key.png");
                break;
            case GREENKEY:
                image = ResourceManager.getImage("images/mapItems/green_key.png");
                break;
            case YELLOWKEY:
                image =  ResourceManager.getImage("images/mapItems/yellow_key.png");
                break;
            default:
                assert false; /* switch statement is missing cases */
                return;
        }
        createItem(posX, posY, KEY_WIDTH, KEY_HEIGHT, ColliderTag.KEY, keyType.ordinal(), image);
    }

    /* create a power-up for players to pick up */
    private void createPowerUp(double posX, double posY, Cell.PowerUpType powerUpType) {
        final double POWERUP_WIDTH = 32, POWERUP_HEIGHT = 32;
        Image image;
        switch (powerUpType) {
            case ADDLIFE:
                image = ResourceManager.getImage("images/mapItems/heart_powerup.png");
                break;
            case INVISIBILITY:
                image = ResourceManager.getImage("images/mapItems/ghost_powerup.png");
                break;
            case SPEEDUP:
                image = ResourceManager.getImage("images/mapItems/lightning_powerup.png");
                break;
            default:
                assert false; /* switch statement is missing cases */
                return;
        }
        createItem(posX, posY, POWERUP_WIDTH, POWERUP_HEIGHT, ColliderTag.POWERUP, powerUpType.ordinal(), image);
    }

    /* create an item for players to pick up */
    private GameEntity createItem(double posX, double posY, double width, double height, ColliderTag colliderTag,
                                  int colliderData, Image image) {
        Transform transform = new Transform(posX, posY, width, height);
        GameEntity entity = new GameEntity();

        Sprite sprite = new Sprite(renderer, transform, image);
        ItemCollider collider = new ItemCollider(collisionSystem, transform, colliderTag, colliderData, entity);

        addComponentToEntity(sprite, entity);
        addComponentToEntity(collider, entity);

        return entity;
    }

    /* creates a section of wall whose color depends on 'dividerType' */
    private void createWall(double posX, double posY, double width, double height, Divider.DividerType dividerType) {
        Color wallColor;
        int wallType;
        switch (dividerType) {
            case WALL:
                wallColor = Color.rgb(200,200,200);
                wallType = 0;
                break;
            case BLUEDOOR:
                wallColor = Color.BLUE;
                wallType = Cell.KeyType.BLUEKEY.ordinal() + 1;
                break;
            case GREENDOOR:
                wallColor = Color.GREEN;
                wallType = Cell.KeyType.GREENKEY.ordinal() + 1;
                break;
            case YELLOWDOOR:
                wallColor = Color.YELLOW;
                wallType = Cell.KeyType.YELLOWKEY.ordinal() + 1;
                break;
            default:
                assert false; /* switch statement is missing cases */
                return;
        }
        Transform transform = new Transform(posX, posY, width, height);

        WallCollider collider = new WallCollider(collisionSystem, transform, wallType);
        WallGraphic graphic = new WallGraphic(renderer, transform, wallColor);

        addComponentToWorld(collider);
        addComponentToWorld(graphic);
    }

    /* creates a new finish checker to end the game when set criteria reached */
    private void addFinishChecker() {
        FinishChecker finishChecker = new FinishChecker(this, networkSystem);
        addComponentToWorld(finishChecker);
    }

    /* get the number of coins in the game */
    public int getNumCoins() {
        return numCoins;
    }
}
