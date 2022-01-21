package uob.cs.teamproject.sabrewulf.enemyai;

/**
 * This class represents a connection between two nodes in a graph. It is used to help find a path
 * between an AI enemy and a player.
 */
public class Connection {

    private int cost;
    private Node node; //connecting node

    /**
     * The constructor for this class.
     * A connection connects the node A it belongs to to another node B
     * in the single direction A to B with default cost 1.
     * @param node: The node this connection comes from.
     */
    public Connection(Node node){
        this.node = node;
        this.cost = 1; //default cost value
    }

    /**
     * The constructor for this class.
     * A connection connects the node A it belongs to to another node B
     * in the single direction A to B.
     * @param node: The node this connection comes from.
     * @param cost: The cost/ weight of the connection.
     */
    public Connection(Node node, int cost){
        this.node = node;
        this.cost = cost;
    }

    /**
     * Getter method.
     * @return the cost/ weight of this connection.
     */
    public int getCost() {
        return cost;
    }

    /**
     * Getter method.
     * @return for a connection A to B, returns node A.
     */
    public Node getNode() {
        return node;
    }
}
