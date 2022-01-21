package uob.cs.teamproject.sabrewulf;

import uob.cs.teamproject.sabrewulf.util.Subscriber;

import java.util.LinkedList;

/**
 * A collection of {@link GameComponent}s which are associated with one another. It handles the graceful removal of each
 * {@link GameComponent} it contains.
 */
public class GameEntity {

    private LinkedList<Subscriber<GameComponent>> gameComponentSubs;

    public GameEntity() {
        gameComponentSubs = new LinkedList<>();
    }

    /**
     * Add a {@link GameComponent} to the entity.
     * @param sub a {@link Subscriber<GameComponent>} for the {@link GameComponent}.
     */
    public void addComponent(Subscriber<GameComponent> sub) {
        gameComponentSubs.add(sub);
    }

    /**
     * Remove each of the {@link GameComponent}s which make up the entity,
     * by calling its {@link GameComponent#remove()}
     * followed by {@link Subscriber<GameComponent>#unsubcribe()}.
     */
    public void remove() {
        for (Subscriber<GameComponent> sub : gameComponentSubs) {
            sub.getValue().remove();
            sub.unsubscribe();
        }
    }
}