package uob.cs.teamproject.sabrewulf.enemyai;

import uob.cs.teamproject.sabrewulf.map.Cell;
import uob.cs.teamproject.sabrewulf.map.Divider;
import uob.cs.teamproject.sabrewulf.map.GameMapWrapper;

import java.util.ArrayList;

/**
 * This class converts the representation of the game map into
 * a graph of nodes to help find a path between a player and an AI enemy in the game.
 */
public class MazeSolver {

    private Graph graph;
    private GameMapWrapper actualGameMap;

    /**
     * The constructor for the MazeSolver.
     * @param actualGameMap: The GameMapWrapper for the game which will be used to find a path through.
     */
    public MazeSolver(GameMapWrapper actualGameMap) {
        this.actualGameMap = actualGameMap;
        graph = new Graph();
        createNodes();
        connectNodesInGraph();
    }

    /**
     * Finds a path between a start and goal node.
     * @param aiCurrentCell: The current cell of the AI (used as the start node).
     * @param playerCurrentCell: The current cell of the player to be chased (used as the goal node).
     * @return an ArrayList of nodes to be traversed through to reach the goal
     * node from the start node.
     */
    public ArrayList<Node> solvePath(Cell aiCurrentCell, Cell playerCurrentCell) {
        Node start;
        Node goal;

        //add start and goal nodes to graph
        if (graph.findNodeCalled("cell("+aiCurrentCell.getCoordX()+", "+aiCurrentCell.getCoordY()+")") == null) {
            //node isn't already in graph so add it
            start = new Node("cell("+aiCurrentCell.getCoordX()+", "+aiCurrentCell.getCoordY()+")");
            graph.addNode(start);
        }
        else {
            //node already exists so label it start
            start = graph.findNodeCalled("cell("+aiCurrentCell.getCoordX()+", "+aiCurrentCell.getCoordY()+")");
        }

        if (graph.findNodeCalled("cell("+playerCurrentCell.getCoordX()+", "+playerCurrentCell.getCoordY()+")") == null) {
            //node isn't already in graph so add it
            goal = new Node("cell("+playerCurrentCell.getCoordX()+", "+playerCurrentCell.getCoordY()+")");
            graph.addNode(goal);
        }
        else {
            //node already exists so label it goal
            goal = graph.findNodeCalled("cell("+playerCurrentCell.getCoordX()+", "+playerCurrentCell.getCoordY()+")");
        }

        //connect start node
        Cell[][] cellGrid = actualGameMap.getCellGrid();
        int startX = start.getX();
        int startY = start.getY();
        int thisX = startX;
        int thisY = startY;

        //add connection to any right nodes
        while (cellGrid[thisX][thisY].getDividerArray()[2] == Divider.DividerType.EMPTY) {
            thisX += 1; //move to next cell
            Node potentialNode = graph.findNodeCalled("cell(" + thisX +", " + thisY + ")");
            if (potentialNode != null) {
                graph.addBiDirection(start, potentialNode);
                break;
            }
        }

        //add connection to any left nodes
        thisX = startX;
        thisY = startY;
        while (cellGrid[thisX][thisY].getDividerArray()[3] == Divider.DividerType.EMPTY) {
            thisX -= 1; //move to next cell
            Node potentialNode = graph.findNodeCalled("cell(" + thisX +", " + thisY + ")");
            if (potentialNode != null) {
                graph.addBiDirection(start, potentialNode);
                break;
            }
        }

        //add connection to any upper nodes
        thisX = startX;
        thisY = startY;
        while (cellGrid[thisX][thisY].getDividerArray()[0] == Divider.DividerType.EMPTY) {
            thisY -= 1; //move to next cell
            Node potentialNode = graph.findNodeCalled("cell(" + thisX +", " + thisY + ")");
            if (potentialNode != null) {
                graph.addBiDirection(start, potentialNode);
                break;
            }
        }

        //add connection to any lower nodes
        thisX = startX;
        thisY = startY;
        while (cellGrid[thisX][thisY].getDividerArray()[1] == Divider.DividerType.EMPTY) {
            thisY += 1; //move to next cell
            Node potentialNode = graph.findNodeCalled("cell(" + thisX +", " + thisY + ")");
            if (potentialNode != null) {
                graph.addBiDirection(start, potentialNode);
                break;
            }
        }

        ////connect goal node
        startX = goal.getX();
        startY = goal.getY();
        thisX = startX;
        thisY = startY;

        //add connection to any right nodes
        while (cellGrid[thisX][thisY].getDividerArray()[2] == Divider.DividerType.EMPTY) {
            thisX += 1; //move to next cell
            Node potentialNode = graph.findNodeCalled("cell(" + thisX +", " + thisY + ")");
            if (potentialNode != null) {
                graph.addBiDirection(goal, potentialNode);
                break;
            }
        }

        //add connection to any left nodes
        thisX = startX;
        thisY = startY;
        while (cellGrid[thisX][thisY].getDividerArray()[3] == Divider.DividerType.EMPTY) {
            thisX -= 1; //move to next cell
            Node potentialNode = graph.findNodeCalled("cell(" + thisX +", " + thisY + ")");
            if (potentialNode != null) {
                graph.addBiDirection(goal, potentialNode);
                break;
            }
        }

        //add connection to any upper nodes
        thisX = startX;
        thisY = startY;
        while (cellGrid[thisX][thisY].getDividerArray()[0] == Divider.DividerType.EMPTY) {
            thisY -= 1; //move to next cell
            Node potentialNode = graph.findNodeCalled("cell(" + thisX +", " + thisY + ")");
            if (potentialNode != null) {
                graph.addBiDirection(goal, potentialNode);
                break;
            }
        }

        //add connection to any lower nodes
        thisX = startX;
        thisY = startY;
        while (cellGrid[thisX][thisY].getDividerArray()[1] == Divider.DividerType.EMPTY) {
            thisY += 1; //move to next cell
            Node potentialNode = graph.findNodeCalled("cell(" + thisX +", " + thisY + ")");
            if (potentialNode != null) {
                graph.addBiDirection(goal, potentialNode);
                break;
            }
        }

        //about to perform search
        Search s = new Search();

        return s.performAStar(graph, start, goal);
    }

    /**
     * Finds a path between a start and goal node.
     * @param aiCurrentCell: The current cell of the AI (used as the start node).
     * @param playerCurrentCell: The current cell of the player to be chased (used as the goal node).
     * @return an int[][] of node coordinates to be traversed through to reach the goal
     * node from the start node.
     */
    public int[][] solvePathAsCells(Cell aiCurrentCell, Cell playerCurrentCell) {
        graph = new Graph();
        createNodes();
        connectNodesInGraph();
        ArrayList<Node> nodesPath = solvePath(aiCurrentCell, playerCurrentCell);
        int[][] nodeCoordinates = new int[nodesPath.size()][2];

        for (int i = 0; i < nodesPath.size(); i++) {
            //convert to cell
            nodeCoordinates[i][0] = nodesPath.get(i).getX();
            nodeCoordinates[i][1] = nodesPath.get(i).getY();
        }

        return nodeCoordinates;
    }

    /**
     * For all nodes in the graph, this method connections those which have a direct path between them.
     * A node is defined as a corner or intersection (i.e. anywhere in which the AI can or must change direction).
     */
    private void connectNodesInGraph() {
        Cell[][] cellGrid = actualGameMap.getCellGrid();

        /*
        First connect nodes in the x direction by finding two nodes that
        are not interrupted by a wall.
        */
        ArrayList<Node> nodesOrderedByX = new ArrayList<>();
        for (int j=0; j<cellGrid.length; j++) {
            for (int i=0; i<cellGrid[0].length; i++) {
                Node node = graph.findNodeCalled("cell("+i+", "+j+")");
                if (node != null) {
                    nodesOrderedByX.add(node);
                }
            }
        }

        for (int i=0; i<nodesOrderedByX.size() -1; i++) {
            //get cell at first node
            int x1 = nodesOrderedByX.get(i).getX();
            int y1 = nodesOrderedByX.get(i).getY();

            int x2 = nodesOrderedByX.get(i+1).getX();
            int y2 = nodesOrderedByX.get(i+1).getY();
            Cell cellAtNode2 = cellGrid[x2][y2];
            if (y2 == y1) {
                int thisX = x1;
                int thisY = y1;

                //while not at cell of second node,
                while (cellGrid[thisX][thisY] != cellAtNode2) {

                    //if not right wall on current cell
                    if (cellGrid[thisX][thisY].getDividerArray()[2] == Divider.DividerType.EMPTY) {
                        thisX += 1;
                        //move onto next cell
                    }
                    else {
                        break;
                    }
                }
                if (cellGrid[thisX][thisY] == cellAtNode2) {
                    Node node1 = graph.findNodeCalled("cell("+x1+", "+y1+")");
                    Node node2 = graph.findNodeCalled("cell("+thisX+", "+thisY+")");
                    graph.addBiDirection(node1, node2, Math.abs(x1-thisX));
                }
            }
        }

        ///////////////////////////////////////////////////////////////////////////////
        /////////////////////////     NEXT SECTION     ////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////

        /*
        Then connect nodes in the y direction by finding two nodes that
        are not interrupted by a wall.
        */
        ArrayList<Node> nodesOrderedByY = new ArrayList<>();
        for (int i=0; i<cellGrid.length; i++) {
            for (int j=0; j<cellGrid[0].length; j++) {
                Node node = graph.findNodeCalled("cell(" +i+", "+j+")");
                if (node != null) {
                    nodesOrderedByY.add(node);
                }
            }
        }

        for (int i=0; i<nodesOrderedByY.size() -1; i++) {
            //get cell at first node
            int x1 = nodesOrderedByY.get(i).getX();
            int y1 = nodesOrderedByY.get(i).getY();

            int x2 = nodesOrderedByY.get(i+1).getX();
            int y2 = nodesOrderedByY.get(i+1).getY();
            Cell cellAtNode2 = cellGrid[x2][y2];
            if (x2 == x1) {
                int thisX = x1;
                int thisY = y1;

                //while not at cell of second node,
                while (cellGrid[thisX][thisY] != cellAtNode2) {

                    //if not right wall on current cell
                    if (cellGrid[thisX][thisY].getDividerArray()[1] == Divider.DividerType.EMPTY) {
                        thisY += 1;
                        //move onto next cell
                    }
                    else {
                        break;
                    }
                }
                if (cellGrid[thisX][thisY] == cellAtNode2) {
                    Node node1 = graph.findNodeCalled("cell("+x1+", "+y1+")");
                    Node node2 = graph.findNodeCalled("cell("+thisX+", "+thisY+")");
                    graph.addBiDirection(node1, node2, Math.abs(y1-thisY));
                }
            }
        }

    }

    /**
     * Analyses the map and creates a node at each corner or intersection
     * (i.e. anywhere in which the AI can or must change direction).
     */
    private void createNodes() {
        Cell[][] cellGrid = actualGameMap.getCellGrid();
        for (int j=0; j<cellGrid.length; j++) {
            for (int i=0; i<cellGrid.length; i++) {
                int moveableXDirections = 0;
                int moveableYDirections = 0;
                if (cellGrid[i][j].getDividerArray()[0] == Divider.DividerType.EMPTY) {
                    moveableYDirections += 1;
                }
                if (cellGrid[i][j].getDividerArray()[1] == Divider.DividerType.EMPTY) {
                    moveableYDirections += 1;
                }
                if (cellGrid[i][j].getDividerArray()[2] == Divider.DividerType.EMPTY) {
                    moveableXDirections += 1;
                }
                if (cellGrid[i][j].getDividerArray()[3] == Divider.DividerType.EMPTY) {
                    moveableXDirections += 1;
                }
                if (moveableXDirections * moveableYDirections != 0) {
                    Node node = new Node("cell(" + i+", "+j + ")");
                    graph.addNode(node);
                }
            }
        }
    }

}
