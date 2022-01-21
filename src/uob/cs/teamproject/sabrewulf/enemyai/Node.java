package uob.cs.teamproject.sabrewulf.enemyai;

import java.util.ArrayList;

/**
 * This class represents a node.
 * In the game, a node is defined as either a corner or a junction (ie.
 * somewhere in which the AI can or must change direction), with the
 * exception of the player's current location which must always be considered
 * to also be a node.
 * It is used to help find a path between an AI enemy and a player.
 */
public class Node {

    private int heuristic = 0; //when defaulted to 0, the A* algorithm degenerates to Dijkstra's
    private int g = (int) Double.POSITIVE_INFINITY;
    private int f = (int) Double.POSITIVE_INFINITY;
    private Node previous;
    private ArrayList<Connection> connections = new ArrayList<Connection>();
    private String name = "Unnamed-Node";

    /**
     * A constructor for a Node.
     * @param name: The name of the Node.
     */
    public Node(String name){
        this.name = name;
    }

    /**
     * Getter method.
     * @return the heuristic of this node.
     */
    public int getHeuristic() {
        return heuristic;
    }

    /**
     * Getter method.
     * @return an ArrayList of all connections this node has.
     */
    public ArrayList<Connection> getConnections() {
        return connections;
    }

    /**
     * Getter method.
     * @return an ArrayList of all nodes this node connects to.
     */
    public ArrayList<Node> getConnectedNodes() {
        ArrayList<Connection> connections = getConnections();
        ArrayList<Node> connectedNodes = new ArrayList<>();
        for (Connection connection : connections) {
            connectedNodes.add(connection.getNode());
        }
        return connectedNodes;
    }

    /**
     * For this node A and a connecting node B, such that there is a connection
     * A to B with cost c, this method returns the cost c.
     * @param connectingNode: The node B connecting to this node.
     * @return the cost c of the connection. If there is no connection, -1 is returned.
     */
    public int getCostTo(Node connectingNode) {
        for (Connection connection : connections) {
            if (connection.getNode() == connectingNode) {
                return connection.getCost();
            }
        }
        return -1;
    }

    /**
     * Getter method.
     * @return this node's g value for an A* search.
     */
    public int getG() {
        return g;
    }

    /**
     * Setter method.
     * @param g: This node's g value for an A* search.
     */
    public void setG(int g) {
        this.g = g;
    }

    /**
     * Getter method.
     * @return this node's f value for an A* search.
     */
    public int getF() {
        return f;
    }

    /**
     * Setter method.
     * @param f: This node's f value for an A* search.
     */
    public void setF(int f) {
        this.f = f;
    }

    /**
     * For an A* search:
     * @return previous node for the best solution so far.
     */
    public Node getPrevious() {
        return previous;
    }

    /**
     * For an A* search:
     * @param previous: previous node for the best solution so far.
     */
    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    /**
     * Getter method.
     * @return the name of the node.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method. Extracts the x coordinate from the name of the node.
     * @return the x coordinate of the node.
     */
    public int getX() {
        return Integer.parseInt(toString().split("\\(")[1].split(",")[0]);
    }

    /**
     * Getter method. Extracts the y coordinate from the name of the node.
     * @return the y coordinate of the node.
     */
    public int getY() {
        return Integer.parseInt(toString().split(", ")[1].split("\\)")[0]);
    }

    /**
     * Overridden toString() method for a node.
     * @return the name of the node.
     */
    public String toString() {
        return name;
    }

    /**
     * For this node A, and node B as the parameter, this method adds a connection A to B.
     * @param node: The node to connect to, B.
     */
    public void addConnection(Node node){
        removePreexistingConnection(node);
        Connection newConnection = new Connection(node);
        connections.add(newConnection);
    }

    /**
     * For this node A, and node B as the parameter, this method adds a connection A to B.
     * @param node: The node to connect to, B.
     * @param cost: The cost/ weight of the connection.
     */
    public void addConnection(Node node, int cost){
        removePreexistingConnection(node);
        Connection newConnection = new Connection(node, cost);
        connections.add(newConnection);
    }

    /**
     * For this node A, and node B as the parameter, this method checks if there is a connection
     * between them and removes it.
     * @param node: The candidate connected node, B.
     */
    private void removePreexistingConnection(Node node) {
        ArrayList<Connection> connectionsToRemove = new ArrayList<Connection>();

        for (Connection connection : connections) {
            if (connection.getNode() == node) {
                connectionsToRemove.add(connection);
            }
        }

        for (Connection connectionToRemove : connectionsToRemove) {
            connections.remove(connectionToRemove);
        }
    }
}
