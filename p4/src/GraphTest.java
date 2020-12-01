

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Filename: GraphTest.java Project: p4 Author: Elijah Asher
 * 
 * PackageManager is used to process json package dependency files and provide function that make
 * that information available to other users.
 * 
 * Each package that depends upon other packages has its own entry in the json file.
 * 
 * Package dependencies are important when building software, as you must install packages in an
 * order such that each package is installed after all of the packages that it depends on have been
 * installed.
 * 
 * For example: package A depends upon package B, then package B must be installed before package A.
 * 
 * This program will read package information and provide information about the packages that must
 * be installed before any given package can be installed. all of the packages in
 * 
 * You may add a main method, but we will test all methods with our own Test classes.
 */
class GraphTest {
  static Graph graph = new Graph();
  int verteces = 6;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {
    graph = new Graph();
  }

  @BeforeEach
  void setUp() throws Exception {
    graph = new Graph();
    // populates graph with verteces
    for (int i = 0; i < verteces; i++) {
      graph.addVertex("" + i);
    }
  }

  @AfterEach
  public void tearDown() throws Exception {
    graph = new Graph();
  }

  @Test
  void test_AddingNullInstance() {

    graph.addVertex(null);

    if (graph.order() != 0)
      fail("Null instance was added");
  }

  @Test
  void test_Creating_Edges() {
    // adds 3 edges to vertex 1
    graph.addEdge("1", "2");
    graph.addEdge("1", "3");
    graph.addEdge("1", "4");
    // stores the length of 1s dependency list
    int edges = graph.getAdjacentVerticesOf("1").size();
    if (edges != 3)// checks that edges match with the edges created
      fail("Incorrect number of edges added");
  }

  @Test
  void test_Creating_Self_Edge() {
    // tries to add an edge between the same vertex
    graph.addEdge("1", "1");
    System.out.println(graph.getAdjacentVerticesOf("1").size());
    if (graph.getAdjacentVerticesOf("1").size() == 0)
      fail("Added a self edge");
  }

  @Test
  void test_GetAllVerteces() {
    if (graph.getAllVertices().size() == verteces)
      ;// expected
    else
      fail("GetAllVerteces does not return the correct value");
  }

}
