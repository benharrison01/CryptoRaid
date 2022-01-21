package uob.cs.teamproject.sabrewulf.components;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import uob.cs.teamproject.sabrewulf.GameComponent;
import uob.cs.teamproject.sabrewulf.rendering.Graphic;
import uob.cs.teamproject.sabrewulf.rendering.Renderer;
import uob.cs.teamproject.sabrewulf.util.Subscriber;
import uob.cs.teamproject.sabrewulf.util.Transform;

/**
 * This component is used to animate a collection of sprites belonging to a player.
 */
public class CharacterAnimator extends GameComponent {

    private Image[] rightStrips = new Image[4];
    private Image[] leftStrips = new Image[4];
    private Image[] upStrips = new Image[4];
    private Image[] downStrips = new Image[4];
    private Image[] rightStrips2 = new Image[4];
    private Image[] leftStrips2 = new Image[4];
    private Image[] upStrips2 = new Image[4];
    private Image[] downStrips2 = new Image[4];
    private Image[] spriteStrips;
    private double frameRate;
    private CharacterMovement.Direction facing;
    private Transform transform;
    private final long startNanoTime;
    private Subscriber<Graphic> graphicElemSub;
    private Image currentImg;
    private PlayerBehaviour player;
    private RemotePlayer remotePlayer;
    private boolean useSecondaryStrips = false;
    private boolean isMoving = false;
    private int flashRate = 24;
    private int currentlyNoImageTimer = flashRate;

    /**
     * The constructor for a CharacterAnimator.
     * Contains the overridden draw method which draws the player's current sprite to the screen
     * in it's current state, e.g. normal, ghost, flashing.
     *
     * @param renderer:  The renderer which shall draw the animated sprites.
     * @param transform: The Transform component belonging to this CharacterAnimator.
     */
    public CharacterAnimator(Renderer renderer, Transform transform) {
        super();

        this.transform = transform;
        this.startNanoTime = System.nanoTime();
        this.frameRate = 0.15;

        final double[] wh = {500}; //has to be an array so draw can change it even though it is final
        this.graphicElemSub = renderer.addForegroundElem(brush -> {
            final int SIZE_DECREASE_RATE = 7; //trial and error number which looks smooth enough
            if ((player != null) && wh[0] > 0) {
                brush.drawOval(transform.position.x, transform.position.y, wh[0], wh[0], new Color(0.9, 0.9, 0.9, 0.5));
                wh[0] = wh[0] - SIZE_DECREASE_RATE;
            }
            try {
                if ((player != null && player.isUndetectable()) || (remotePlayer != null && remotePlayer.isUndetectable())) {
                    if (currentlyNoImageTimer > flashRate / 2) {
                        SemiTransparentImage st = new SemiTransparentImage(currentImg);
                        Image currentImgTransp = st.applyTransparency(0.7);
                        brush.drawImage(transform.position.x, transform.position.y, transform.width, transform.height
                                , currentImgTransp);
                        currentlyNoImageTimer -= 1;
                    } else {
                        brush.drawImage(transform.position.x, transform.position.y, transform.width, transform.height
                                , null);
                        currentlyNoImageTimer -= 1;
                    }
                } else if ((player != null && player.getInventory().hasInvisibility()) ||
                        (remotePlayer != null && remotePlayer.getInventory().hasInvisibility())) {
                    SemiTransparentImage st = new SemiTransparentImage(currentImg);
                    Image currentImgTransp = st.applyTransparency(0.4);
                    brush.drawImage(transform.position.x, transform.position.y, transform.width, transform.height,
                            currentImgTransp);
                } else {
                    brush.drawImage(transform.position.x, transform.position.y, transform.width, transform.height,
                            currentImg);
                }
            } catch (Exception e) {
                brush.drawImage(transform.position.x, transform.position.y, transform.width, transform.height,
                        currentImg);
            }

            if (currentlyNoImageTimer == 0) {
                currentlyNoImageTimer = flashRate;
            }
        });
    }

    /**
     * @return the x coordinate of the midpoint of the object, where the midpoint of the screen is (0,0)
     * and the rightmost pixels are positive values.
     */
    public int getX() {
        return (int) transform.position.x;
    }

    /**
     * @return the y coordinate of the midpoint of the object, where the midpoint of the screen is (0,0)
     * and the above pixels are negative values.
     */
    public int getY() {
        return (int) transform.position.y;
    }

    /**
     * Assigns the primary sprite strips for the CharacterAnimator.
     *
     * @param s: A collection of up, down, left, right facing sprite strips.
     */
    public void setSpriteSets(SpriteSets s) {
        this.downStrips = s.getDownStrips();
        this.rightStrips = s.getRightStrips();
        this.upStrips = s.getUpStrips();
        this.leftStrips = s.getLeftStrips();
        this.spriteStrips = downStrips;
    }

    /**
     * Assigns the secondary sprite strips for the CharacterAnimator.
     *
     * @param s: A collection of up, down, left, right facing sprite strips.
     */
    public void setSecondarySpriteSets(SpriteSets s) {
        this.downStrips2 = s.getDownStrips();
        this.rightStrips2 = s.getRightStrips();
        this.upStrips2 = s.getUpStrips();
        this.leftStrips2 = s.getLeftStrips();
    }

    /**
     * For a PlayerBehaviour component owning this CharacterAnimator, setPlayer
     * assigns this PlayerBehaviour component to this CharacterAnimator.
     *
     * @param p: The PlayerBehaviour component.
     */
    public void setPlayer(PlayerBehaviour p) {
        this.player = p;
    }

    /**
     * Takes the direction of the character and assigns the current sprite strips accordingly
     * such that the character is animated to match the direction of movement.
     *
     * @param facing: The direction the character is facing.
     */
    public void setDirection(CharacterMovement.Direction facing) {
        this.facing = facing;
        changeSpriteStrips();
    }

    /**
     * Setter method.
     *
     * @param b: True or false to allow or prevent use of CharacterAnimator's secondary strips.
     */
    public void setUseSecondaryStrips(boolean b) {
        useSecondaryStrips = b;
    }

    /**
     * For a RemotePlayer component owning this CharacterAnimator, setRemotePlayer
     * assigns this RemotePlayer component to this CharacterAnimator.
     *
     * @param p: The Remote PLayer component.
     */
    void setRemotePlayer(RemotePlayer p) {
        this.remotePlayer = p;
    }

    /**
     * Updates the character's coordinates.
     *
     * @param x: The x coordinate.
     * @param y: The y coordinate.
     */
    void setCoords(int x, int y) {
        transform.position.x = x;
        transform.position.y = y;
    }

    /**
     * Calls the animate function such that the character changes image.
     *
     * @param currentNanoTime: The current time in nanoseconds.
     */
    @Override
    public void update(long currentNanoTime) {
        double t = (currentNanoTime - startNanoTime) / 1000000000.0;
        animate(t);
    }

    /**
     * Unsubscribe this component from any game systems it is using.
     */
    @Override
    public void remove() {
        graphicElemSub.unsubscribe();
    }

    /**
     * Used to prevent or allow character animation.
     *
     * @param b: b==true if the character is moving, b==false if the character is not moving.
     */
    void setIsMoving(Boolean b) {
        this.isMoving = b;
    }

    /**
     * Changes the sprite strips which are animated
     * based on where the character is facing.
     */
    private void changeSpriteStrips() {
        try {
            if (!useSecondaryStrips) {
                if (facing == CharacterMovement.Direction.UP) {
                    spriteStrips = upStrips;
                } else if (facing == CharacterMovement.Direction.DOWN) {
                    spriteStrips = downStrips;
                } else if (facing == CharacterMovement.Direction.LEFT) {
                    spriteStrips = leftStrips;
                } else if (facing == CharacterMovement.Direction.RIGHT) {
                    spriteStrips = rightStrips;
                }
            } else {
                if (facing == CharacterMovement.Direction.UP) {
                    spriteStrips = upStrips2;
                } else if (facing == CharacterMovement.Direction.DOWN) {
                    spriteStrips = downStrips2;
                } else if (facing == CharacterMovement.Direction.LEFT) {
                    spriteStrips = leftStrips2;
                } else if (facing == CharacterMovement.Direction.RIGHT) {
                    spriteStrips = rightStrips2;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Sets the current image to be one from the current strips.
     *
     * @param time: the current time - start time in seconds.
     */
    private void animate(double time) {
        try {
            int index = (int) ((time % (spriteStrips.length * frameRate)) / frameRate);
            if (!isMoving) {
                currentImg = spriteStrips[0];
            } else {
                currentImg = spriteStrips[index];
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

