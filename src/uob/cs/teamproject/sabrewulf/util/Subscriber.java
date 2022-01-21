package uob.cs.teamproject.sabrewulf.util;

/**
 * A member of a {@link Subscription} collection. Each item in the collection is associated with an object of this type,
 * which is used to access the item.
 */
public class Subscriber<T> {

    private final T value;
    private Subscriber<T> prev, next;

    /**
     * Creates a new {@link Subscriber} instance which points to the given object.
     * @param value the object which is to be stored in the {@link Subscription}
     */
    public Subscriber(T value) {
        this.value = value;
        this.prev = null;
        this.next = null;
    }

    /**
     * @return the value associated with this subscriber
     */
    public T getValue() {
        return value;
    }

    /**
     * Add another {@link Subscriber} instance into the {@link Subscription}'s list of subscribers, immediately after
     * this instance.
     * @param newSub the new {@link Subscriber} instance to add
     */
    public void insertAfter(Subscriber<T> newSub) {
        newSub.next = this.next;
        newSub.prev = this;
        if (this.next != null) {
            this.next.prev = newSub;
        }
        this.next = newSub;
    }

    /**
     * @return false iff this object is at the end of the {@link Subscription}'s list of subscribers.
     */
    public boolean hasNext() {
        return next != null;
    }

    /**
     * @return the next subscriber in the {@link Subscription}'s list of subscribers.
     */
    public Subscriber<T> getNext() {
        return next;
    }

    /**
     * Remove this object from the collection.
     */
    public void unsubscribe() {
        if (this.prev != null) {
            this.prev.next = this.next;
        }
        if (this.next != null) {
            this.next.prev = this.prev;
        }
    }
}