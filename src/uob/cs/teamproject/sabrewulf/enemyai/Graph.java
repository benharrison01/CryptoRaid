package uob.cs.teamproject.sabrewulf.enemyai;

import java.util.ArrayList;

/**
 * This class represents a graph of nodes.
 * In the game, a node is defined as either a corner or a junction (ie.
 * somewhere in which the AI can or must change direction), with the
 * exception of the player's current location which must always be considered
 * to also be a node.
 * It is used to help find a path between an AI enemy and a player.
 */
public class Graph {

    private ArrayList<Node> nodes = new ArrayList<Node>();

    /**
     * The constructor for a new Graph.
     */
    public Graph() {
    }

    /**
     * Getter method.
     * @return an ArrayList of all nodes in the graph.
     */
    public ArrayList<Node> getNodes() {
        return nodes;
    }

    /**
     * Getter method.
     * @return the number of edges in the graph. A bidirectional connection will
     * be counted as 2 connections/ edges.
     */
    public int getNumberOfEdges() {
        int noEdges = 0;

        for(Node node: nodes){
            noEdges += node.getConnections().size();
        }

        return noEdges;
    }

    /**
     * Adds a connection both A to B and B to A with default cost 1.
     * @param node1: Node A.
     * @param node2: Node B.
     */
    public void addBiDirection(Node node1, Node node2){
        node1.addConnection(node2);
        node2.addConnection(node1);
    }

    /**
     * Adds a connection both A to B and B to A with the given cost.
     * @param node1: Node A.
     * @param node2: Node B.
     * @param cost: The cost/ weight of the connection.
     */
    public void addBiDirection(Node node1, Node node2, int cost){
        node1.addConnection(node2, cost);
        node2.addConnection(node1, cost);
    }

    /**
     * Adds a node to the graph.
     * @param nodeToAdd: The node to be added to the graph.
     */
    public void addNode(Node nodeToAdd){
            nodes.add(nodeToAdd);
    }

    /**
     *
     * @param s: The name of node to be found.
     * @return the node which has the name given as a parameter.
     */
    public Node findNodeCalled(String s) {
        for (Node node : nodes){
            if (node.getName().equals(s)) {
                return node;
            }
        }
        return null;
    }

    /**
     * Overridden toString() method.
     * @return the graph in string format:
     * "Node ___ has connections: ___" for all nodes.
     */
    @Override
    public String toString() {
        String output = "";
        for (Node node : nodes) {
            ArrayList<Connection> connections = node.getConnections();
            String cons = "";
            for (Connection connection : connections) {
                cons = cons + connection.getNode().toString() + ", ";
            }
            output = output + "Node: " + node + " has connections: " + cons + "\n";
        }
        return output;
    }
}
