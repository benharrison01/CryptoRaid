package test;

import org.junit.Test;
import uob.cs.teamproject.sabrewulf.enemyai.Graph;
import uob.cs.teamproject.sabrewulf.enemyai.Node;
import uob.cs.teamproject.sabrewulf.enemyai.Search;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * This collection of JUnit tests tests Connection, Graph, Node, Search
 */
public class GraphTest {

    @Test
    public void test1() {
        //count nodes
        //count connections
        //check path with: no loops, no costs
        Graph graph = new Graph();

        Node A = new Node("A");
        Node B = new Node("B");
        Node C = new Node("C");
        Node D = new Node("D");
        Node E = new Node("E");
        Node F = new Node("F");
        Node G = new Node("G");

        graph.addNode(A);
        graph.addNode(B);
        graph.addNode(C);
        graph.addNode(D);
        graph.addNode(E);
        graph.addNode(F);
        graph.addNode(G);

        assertEquals(7, graph.getNodes().size());

        graph.addBiDirection(A,B);
        graph.addBiDirection(B,D);
        graph.addBiDirection(D,G);
        graph.addBiDirection(A,C);
        graph.addBiDirection(C,E);
        graph.addBiDirection(E,F);
        graph.addBiDirection(F,G);

        assertEquals(14, graph.getNumberOfEdges());

        Search s = new Search();
        ArrayList<Node> actualSolution = s.performAStar(graph, A, G);
        ArrayList<Node> expectedSolution = new ArrayList<Node>(Arrays.asList(A,B,D,G));
        assertEquals(expectedSolution, actualSolution);
    }

    @Test
    public void test2() {
        //count nodes
        //count connections
        //check path with: loops, no costs
        Graph graph = new Graph();

        Node A = new Node("A");
        Node B = new Node("B");
        Node C = new Node("C");
        Node D = new Node("D");
        Node E = new Node("E");
        Node F = new Node("F");
        Node G = new Node("G");

        graph.addNode(A);
        graph.addNode(B);
        graph.addNode(C);
        graph.addNode(D);
        graph.addNode(E);
        graph.addNode(F);
        graph.addNode(G);

        assertEquals(7, graph.getNodes().size());

        graph.addBiDirection(A,B);
        graph.addBiDirection(B,D);
        graph.addBiDirection(D,G);
        graph.addBiDirection(A,C);
        graph.addBiDirection(C,E);
        graph.addBiDirection(E,F);
        graph.addBiDirection(F,G);
        graph.addBiDirection(C,F);

        assertEquals(16, graph.getNumberOfEdges());

        Search s = new Search();
        ArrayList<Node> actualSolution = s.performAStar(graph, A, G);
        ArrayList<Node> expectedSolution = new ArrayList<Node>(Arrays.asList(A,B,D,G));
        assertEquals(expectedSolution, actualSolution);
    }

    @Test
    public void test3() {
        //count nodes
        //count connections
        //check path with: loops, equal costs
        Graph graph = new Graph();

        Node A = new Node("A");
        Node B = new Node("B");
        Node C = new Node("C");
        Node D = new Node("D");
        Node E = new Node("E");
        Node F = new Node("F");
        Node G = new Node("G");

        graph.addNode(A);
        graph.addNode(B);
        graph.addNode(C);
        graph.addNode(D);
        graph.addNode(E);
        graph.addNode(F);
        graph.addNode(G);

        assertEquals(7, graph.getNodes().size());

        graph.addBiDirection(A,B,1);
        graph.addBiDirection(B,D,1);
        graph.addBiDirection(D,G,1);
        graph.addBiDirection(A,C,1);
        graph.addBiDirection(C,E,1);
        graph.addBiDirection(E,F,1);
        graph.addBiDirection(F,G,1);
        graph.addBiDirection(C,F,1);

        assertEquals(16, graph.getNumberOfEdges());

        Search s = new Search();
        ArrayList<Node> actualSolution = s.performAStar(graph, A, G);
        ArrayList<Node> expectedSolution = new ArrayList<Node>(Arrays.asList(A,B,D,G));
        assertEquals(expectedSolution, actualSolution);
    }

    @Test
    public void test4() {
        //count nodes
        //count connections
        //check path with: no loops, unequal costs
        Graph graph = new Graph();

        Node A = new Node("A");
        Node B = new Node("B");
        Node C = new Node("C");
        Node D = new Node("D");
        Node E = new Node("E");
        Node F = new Node("F");
        Node G = new Node("G");

        graph.addNode(A);
        graph.addNode(B);
        graph.addNode(C);
        graph.addNode(D);
        graph.addNode(E);
        graph.addNode(F);
        graph.addNode(G);

        assertEquals(7, graph.getNodes().size());

        graph.addBiDirection(A,B,3);
        graph.addBiDirection(B,D,1);
        graph.addBiDirection(D,G,1);
        graph.addBiDirection(A,C,1);
        graph.addBiDirection(C,E,1);
        graph.addBiDirection(E,F,1);
        graph.addBiDirection(F,G,1);

        assertEquals(14, graph.getNumberOfEdges());

        Search s = new Search();
        ArrayList<Node> actualSolution = s.performAStar(graph, A, G);
        ArrayList<Node> expectedSolution = new ArrayList<Node>(Arrays.asList(A,C,E,F,G));
        assertEquals(expectedSolution, actualSolution);
    }

    @Test
    public void test5() {
        //count nodes
        //count connections
        //check path with: loops, unequal costs
        Graph graph = new Graph();

        Node A = new Node("A");
        Node B = new Node("B");
        Node C = new Node("C");
        Node D = new Node("D");
        Node E = new Node("E");
        Node F = new Node("F");
        Node G = new Node("G");

        graph.addNode(A);
        graph.addNode(B);
        graph.addNode(C);
        graph.addNode(D);
        graph.addNode(E);
        graph.addNode(F);
        graph.addNode(G);

        assertEquals(7, graph.getNodes().size());

        graph.addBiDirection(A,B,3);
        graph.addBiDirection(B,D,1);
        graph.addBiDirection(D,G,1);
        graph.addBiDirection(A,C,1);
        graph.addBiDirection(C,E,1);
        graph.addBiDirection(E,F,1);
        graph.addBiDirection(F,G,1);
        graph.addBiDirection(C,F,1);

        assertEquals(16, graph.getNumberOfEdges());

        Search s = new Search();
        ArrayList<Node> actualSolution = s.performAStar(graph, A, G);
        ArrayList<Node> expectedSolution = new ArrayList<Node>(Arrays.asList(A,C,F,G));
        assertEquals(expectedSolution, actualSolution);
    }

    @Test
    public void test6() {
        //count nodes
        //count connections
        //check path with: loops, unequal costs, loop is cheaper than reaching goal
        Graph graph = new Graph();

        Node A = new Node("A");
        Node B = new Node("B");
        Node C = new Node("C");
        Node D = new Node("D");
        Node E = new Node("E");
        Node F = new Node("F");
        Node G = new Node("G");

        graph.addNode(A);
        graph.addNode(B);
        graph.addNode(C);
        graph.addNode(D);
        graph.addNode(E);
        graph.addNode(F);
        graph.addNode(G);

        assertEquals(7, graph.getNodes().size());

        graph.addBiDirection(A,B,1);
        graph.addBiDirection(B,D,1);
        graph.addBiDirection(A,C,1);
        graph.addBiDirection(C,E,1);
        graph.addBiDirection(E,F,1);
        graph.addBiDirection(F,G,20);
        graph.addBiDirection(C,F,1);

        assertEquals(14, graph.getNumberOfEdges());

        Search s = new Search();
        ArrayList<Node> actualSolution = s.performAStar(graph, A, G);
        ArrayList<Node> expectedSolution = new ArrayList<Node>(Arrays.asList(A,C,F,G));
        assertEquals(expectedSolution, actualSolution);
    }

    @Test
    public void test7() {
        //count nodes
        //count connections
        //check path when goal not in graph
        Graph graph = new Graph();

        Node A = new Node("A");
        Node B = new Node("B");
        Node C = new Node("C");
        Node D = new Node("D");
        Node E = new Node("E");
        Node F = new Node("F");
        Node G = new Node("G");

        graph.addNode(A);
        graph.addNode(B);
        graph.addNode(C);
        graph.addNode(D);
        graph.addNode(E);
        graph.addNode(F);
        //graph.addNode(G);

        assertEquals(6, graph.getNodes().size());

        graph.addBiDirection(A,B,1);
        graph.addBiDirection(B,D,1);
        graph.addBiDirection(A,C,1);
        graph.addBiDirection(C,E,1);
        graph.addBiDirection(E,F,1);
        graph.addBiDirection(C,F,1);

        assertEquals(12, graph.getNumberOfEdges());

        Search s = new Search();
        ArrayList<Node> actualSolution = s.performAStar(graph, A, G);
        assertNull(actualSolution);
    }

    @Test
    public void test8() {
        //count nodes
        //count connections
        //check path when start not in graph
        Graph graph = new Graph();

        Node A = new Node("A");
        Node B = new Node("B");
        Node C = new Node("C");
        Node D = new Node("D");
        Node E = new Node("E");
        Node F = new Node("F");
        Node G = new Node("G");

        graph.addNode(A);
        graph.addNode(B);
        graph.addNode(C);
        graph.addNode(D);
        graph.addNode(E);
        graph.addNode(F);
        //graph.addNode(G);

        assertEquals(6, graph.getNodes().size());

        graph.addBiDirection(A,B,1);
        graph.addBiDirection(B,D,1);
        graph.addBiDirection(A,C,1);
        graph.addBiDirection(C,E,1);
        graph.addBiDirection(E,F,1);
        graph.addBiDirection(C,F,1);

        assertEquals(12, graph.getNumberOfEdges());

        Search s = new Search();
        ArrayList<Node> actualSolution = s.performAStar(graph, G, A);
        assertNull(actualSolution);
    }
}
