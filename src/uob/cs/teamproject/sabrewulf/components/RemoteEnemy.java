package uob.cs.teamproject.sabrewulf.components;

import javafx.scene.image.Image;
import uob.cs.teamproject.sabrewulf.GameComponent;
import uob.cs.teamproject.sabrewulf.ResourceManager;
import uob.cs.teamproject.sabrewulf.network.DataStorage;
import uob.cs.teamproject.sabrewulf.util.XYPair;

/**
 * The RemoteEnemy component is responsible for replicating the enemy (MovementLogicAI) behaviour to clients
 */
public class RemoteEnemy extends GameComponent {
    private final CharacterMovement characterMovement;
    private final CharacterAnimator characterAnimator;
    private final EnemyCollider enemyCollider;
    private final DataStorage dataStorage;
    private final int num;

    /**
     * The constructor for the RemoteEnemy class.
     * @param enemyCollider The EnemyCollider system belonging to the AI
     * @param characterAnimator The CharacterAnimator component belonging to the AI
     * @param characterMovement The CharacterMovement component belonging to the AI
     * @param dataStorage The DataStorage object where character directions are stored to be sent over the network
     * @param num The number of the enemy character
     */
    public RemoteEnemy(EnemyCollider enemyCollider, CharacterAnimator characterAnimator, CharacterMovement characterMovement, DataStorage dataStorage, int num) {
        this.dataStorage = dataStorage;
        this.num = num;
        this.characterMovement = characterMovement;
        this.enemyCollider = enemyCollider;
        this.characterAnimator = characterAnimator;
        characterMovement.setIsMoving(false);
        setSprites();
        setSecondarySprites();
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
     * The update method for the RemoteEnemy. Sets characterAnimator to use secondary sprites if the enemy is
     * detecting a player. Makes changes to the characterMovement according to the changes received from the server.
     * @param now a timestamp for the current step of the game loop, given in nanoseconds
     */
    @Override
    public void update(long now){
        XYPair playerXY = enemyCollider.getTargetPlayerPos();
        characterAnimator.setUseSecondaryStrips(playerXY != null);

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
}