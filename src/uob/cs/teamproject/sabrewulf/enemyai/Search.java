package uob.cs.teamproject.sabrewulf.enemyai;

import java.util.ArrayList;

/**
 * This class implements the A* algorithm on a graph.
 * It is used to find a path between a player and an AI enemy in the game.
 */
public class Search {

    /**
     * The constructor for the Search class.
     */
    public Search(){
    }

    /**
     * A method to perform an A* Search Algorithm on a graph, given a start node
     * and a goal node. This algorithm is the same as Dijkstra's Algorithm when
     * the heuristic costs for all nodes are equal.
     * @param graph: The graph to be traversed for a solution.
     * @param start: The starting node in the path.
     * @param goal: The goal node in the path.
     * @return the solution as an ArrayList of Nodes representing the path/ intermediate steps to
     * reach the goal node from the start node in the graph.
     */
    public ArrayList<Node> performAStar(Graph graph, Node start, Node goal) {
        try {
            ArrayList<Node> unvistited = new ArrayList<Node>();
            ArrayList<Node> visited = new ArrayList<Node>();
            Boolean finished = false;

            //initialisation
            start.setF(start.getHeuristic());
            start.setG(0);
            unvistited.addAll(graph.getNodes());

            while (!finished) {
                //pick node with lowest f as current node
                Node currentNode = unvistited.get(0);
                for (Node node : unvistited) {
                    if (node.getF() < currentNode.getF()) {
                        currentNode = node;
                    }
                }

                if (currentNode == goal) {
                    finished = true;
                    visited.add(currentNode);
                } else {
                    //examine unvisited neighbours of this node
                    ArrayList<Node> neighbours = currentNode.getConnectedNodes();
                    for (Node neighbour : neighbours) {
                        int newGScore = currentNode.getCostTo(neighbour) + currentNode.getG();
                        if (newGScore < neighbour.getG()) {
                            neighbour.setG(newGScore);
                            neighbour.setF(newGScore + neighbour.getHeuristic());
                            neighbour.setPrevious(currentNode);
                        }
                    }
                    unvistited.remove(currentNode);
                    visited.add(currentNode);
                }
            }

            ArrayList<Node> solution = new ArrayList<>();
            Node backtrackNode = visited.get(visited.size() - 1);
            while (backtrackNode != start) {
                solution.add(0, backtrackNode);
                backtrackNode = backtrackNode.getPrevious();
            }
            solution.add(0, start);
            return solution;
        }
        catch (Exception e) {
            return null;
        }

    }


}
