package uob.cs.teamproject.sabrewulf.collisions;

/**
 * Each collider is assigned a tag representing the type of game object it is attached to, so that objects which
 * collide with it can tell what they've collided with. Each collider is also assigned a 'data' field, which is an
 * integer code used to send additional information to the colliding object, e.g. for a collider with the POWERUP tag,
 * the data field would indicate what type of powerup it is.
 */
public enum ColliderTag {
    PLAYER,     // data: 0 = detectable, 1 = undetectable
    ENEMY,      // data: unused
    ENEMY_VIEW, // data: unused
    WALL,       // data: int representation of DividerType enum
    COIN,       // data: unused
    KEY,        // data: int representation of KeyType enum
    POWERUP     // data: int representation of PowerUpType enum
}