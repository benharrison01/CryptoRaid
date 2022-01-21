package uob.cs.teamproject.sabrewulf;

/** The base class for the components which make up entities in the game world. */
public abstract class GameComponent {

    /**
     * This method can be overridden by extending classes. It is going to be called when the game (not the application)
     * starts.
     */
    public void start() {}

    /**
     * This method can be overridden by extending classes. It is going to be called once in each step of the game loop.
     * @param now a timestamp for the current step of the game loop, given in nanoseconds
     */
    public void update(long now) {}

    /**
     * This method can be overridden by extending classes. It is going to be called when the entity which the component
     * is part of is removed from the game world.
     */
    public void remove() {}
}