package uob.cs.teamproject.sabrewulf.components;

import uob.cs.teamproject.sabrewulf.GameComponent;
import uob.cs.teamproject.sabrewulf.map.GameMapWrapper;
import uob.cs.teamproject.sabrewulf.util.Transform;

/**
 * This component allows character behaviour classes (e.g. PlayerBehaviour, EnemyBehaviour) to re-use common movement
 * code, such as 'walk left' or 'set speed to 10'
 */
public class CharacterMovement extends GameComponent {
    public enum Direction {LEFT, RIGHT, UP, DOWN, NONE}
    private Boolean isMoving = false;
    private int speed = 3;
    private int speedBoost = 0;
    private Direction directionX = Direction.NONE;
    private Direction directionY = Direction.DOWN;
    private Direction facing = Direction.DOWN;
    private CharacterAnimator animator;
    private Transform transform;
    private GameMapWrapper gameMapWrapper;

    /**
     * Constructor for a CharacterMovement component.
     *
     * @param animator:       The CharacterAnimator component belonging to this.
     * @param transform:      The Transform component belonging to this.
     * @param gameMapWrapper: The GameMapWrapper component belonging to this.
     */
    public CharacterMovement(CharacterAnimator animator, Transform transform, GameMapWrapper gameMapWrapper) {
        this.animator = animator;
        this.transform = transform;
        this.gameMapWrapper = gameMapWrapper;
        animator.setDirection(facing);
    }

    /**
     * @return the CharacterAnimator belonging to this class.
     */
    public CharacterAnimator getCharacterAnimator() {
        return animator;
    }

    /**
     * @param facing: The direction the character is facing (Direction.Left, Direction.Right, Direction.Up, Direction
     *              .Down).
     */
    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    /**
     * Toggles whether or not the character is moving.
     *
     * @param b: isMoving (true or false).
     */
    public void setIsMoving(Boolean b) {
        try {
            this.isMoving = b;
            animator.setIsMoving(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter method.
     *
     * @return is moving.
     */
    public Boolean getIsMoving() {
        return this.isMoving;
    }

    /**
     * Getter Method.
     *
     * @return new Direction[]{this.directionX, this.directionY};.
     */
    public Direction[] getDirection() {
        return new Direction[]{this.directionX, this.directionY};
    }

    /**
     * Sets the speed the character is moving.
     *
     * @param s: speed.
     */
    public void setSpeed(int s) {
        this.speed = s;
    }

    /**
     * Getter method.
     *
     * @return x coordinate of the character.
     */
    double getPosX() {
        return transform.position.x;
    }

    /**
     * Getter method.
     *
     * @return y coordinate of the character.
     */
    double getPosY() {
        return transform.position.y;
    }

    /**
     * Getter method primarily used for testing values.
     * @return the CharacterMovement's x direction.
     */
    public Direction getDirX() {
        return directionX;
    }

    /**
     * Getter method primarily used for testing values.
     * @return the CharacterMovement's y direction.
     */
    public Direction getDirY() {
        return directionY;
    }

    /**
     * Getter method primarily used for testing values.
     * @return the CharacterMovement's facing direction.
     */
    public Direction getFacing() {
        return facing;
    }

    /**
     * @return the GameMapWrapper belonging to this class.
     */
    GameMapWrapper getGameMapWrapper() {
        return gameMapWrapper;
    }

    /**
     * The player moves with totalSpeed = speed + speedBoost
     *
     * @param speedBoost: speed boost of the player.
     */
    void setSpeedBoost(int speedBoost) {
        this.speedBoost = speedBoost;
    }

    /**
     * Changes the direction the character is facing based on the x and y directions.
     * Tells the CharacterAnimator which way the character is facing so the animated
     * sprites are facing the correct direction.
     *
     * @param directionX: The x direction the character is moving in.
     * @param directionY: The y direction the character is moving in.
     */
    public void changeDirection(Direction directionX, Direction directionY) {
        this.directionX = directionX;
        this.directionY = directionY;
        this.isMoving = true;

        animator.setDirection(facing);
    }

    /**
     * Calls the move method.
     *
     * @param t: The current time in nanoseconds.
     */
    @Override
    public void update(long t) {
        move();
    }

    /**
     * Enables the character to move given its current x and y directions.
     * Communicates with the CharacterAnimator so the matching sprite
     * animation is carried out.
     */
    private void move() {
        int xCoord = (int) transform.position.x;
        int yCoord = (int) transform.position.y;
        int totalSpeed = speed + speedBoost;

        if (isMoving) {
            if (directionX == Direction.LEFT) {
                xCoord = xCoord - totalSpeed;
            } else if (directionX == Direction.RIGHT) {
                xCoord = xCoord + totalSpeed;
            }

            if (directionY == Direction.UP) {
                yCoord = yCoord - totalSpeed;
            } else if (directionY == Direction.DOWN) {
                yCoord = yCoord + totalSpeed;
            }
            animator.setCoords(xCoord, yCoord);
            animator.setDirection(facing);
        }
    }

}
