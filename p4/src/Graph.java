import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Filename: Graph.java Project: p4 Authors: Elijah Asher
 * 
 * Directed and unweighted graph implementation
 */

public class Graph implements GraphADT {
  private ArrayList<GraphNode> adjList;// adjacency list of verteces (GraphNodes)

  /*
   * Default no-argument constructor
   */
  public Graph() {
    adjList = new ArrayList<GraphNode>();
  }

  /**
   * Inner Class GraphNode stores the dependecy List of each vertex and the name of each vertex
   */
  private class GraphNode {
    private ArrayList<String> dependencyList;// list of verteces that this node has edges to
    private String nodeName;// name of this node

    /**
     * Constuctor
     * 
     * @param name - name of GraphNode
     */
    private GraphNode(String name) {
      nodeName = name;
      dependencyList = new ArrayList<String>();
    }

    private String getName() {// name getter
      return nodeName;
    }

    private ArrayList<String> getDependencyList() {
      return dependencyList;
    }

    public String toString() {
      return nodeName;
    }

    private void addToDependencyList(ArrayList<String> list) {
      dependencyList = list;
    }
  }

  /**
   * Add new vertex to the graph.
   *
   * If vertex is null or already exists, method ends without adding a vertex or throwing an
   * exception.
   * 
   * Valid argument conditions: 1. vertex is non-null 2. vertex is not already in the graph
   * 
   * @param vertex the vertex to be added
   */
  public void addVertex(String vertex) {
    if (vertex == null) {
      return;
    }
    // check if vertex is already in graph
    for (int i = 0; i < adjList.size(); i++) {
      if (adjList.get(i).getName().equals(vertex))
        return;
    }
    // adds new vertex to graph
    adjList.add(new GraphNode(vertex));
  }

  /**
   * Remove a vertex and all associated edges from the graph.
   * 
   * If vertex is null or does not exist, method ends without removing a vertex, edges, or throwing
   * an exception.
   * 
   * Valid argument conditions: 1. vertex is non-null 2. vertex is not already in the graph
   * 
   * @param vertex the vertex to be removed
   */
  public void removeVertex(String vertex) {
    if (vertex == null)
      return;

    for (int i = 0; i < adjList.size(); i++) {
      if (adjList.get(i).getName().equals(vertex)) {
        adjList.remove(i);// removed vertex from adjLists
        for (int k = 0; k < adjList.size(); k++) {
          adjList.get(k).dependencyList.remove(vertex);
        }
        break;
      }
    }
    // removed vertex and all instances of vertex in other graphnodes dependency lists

  }

  /**
   * Add the edge from vertex1 to vertex2 to this graph. (edge is directed and unweighted)
   * 
   * If either vertex does not exist, VERTEX IS ADDED and then edge is created. No exception is
   * thrown.
   *
   * If the edge exists in the graph, no edge is added and no exception is thrown.
   * 
   * Valid argument conditions: 1. neither vertex is null 2. both vertices are in the graph 3. the
   * edge is not in the graph
   * 
   * @param vertex1 the first vertex (src)
   * @param vertex2 the second vertex (dst)
   */
  public void addEdge(String vertex1, String vertex2) {
    if (vertex1 == null || vertex2 == null)
      return;
    else if (vertex1.equals(vertex2)) {// checks for self edge
      return;
    }
    // check if verteces are in graph
    ArrayList<String> nameHolder = new ArrayList<String>();
    for (int i = 0; i < adjList.size(); i++) {
      nameHolder.add(adjList.get(i).getName());
    }
    // if the list doesn't contain either v1 or v2, return
    if (!nameHolder.contains(vertex1) || !nameHolder.contains(vertex2))
      return;

    for (int i = 0; i < adjList.size(); i++) {
      // if v1 is found in the adjList, store its index
      if (adjList.get(i).getName().equals(vertex1)) {
        // check if edge already exists from v1 --> v2
        if (adjList.get(i).getDependencyList().contains(vertex2)) {
          return; // edge already exists
        } else {
          // edge does not exist, create edge from v1 --> v2
          ArrayList<String> temp = adjList.get(i).dependencyList;
          temp.add(vertex2);
          GraphNode tempNode = new GraphNode(vertex1);
          tempNode.addToDependencyList(temp);
          adjList.set(i, tempNode);
        }
      }
    }

  }

  /**
   * Remove the edge from vertex1 to vertex2 from this graph. (edge is directed and unweighted) If
   * either vertex does not exist, or if an edge from vertex1 to vertex2 does not exist, no edge is
   * removed and no exception is thrown.
   * 
   * Valid argument conditions: 1. neither vertex is null 2. both vertices are in the graph 3. the
   * edge from vertex1 to vertex2 is in the graph
   * 
   * @param vertex1 the first vertex
   * @param vertex2 the second vertex
   */
  public void removeEdge(String vertex1, String vertex2) {
    // checks that verteces are not null
    if (vertex1 == null || vertex2 == null)
      return;
    // check if verteces are in graph
    ArrayList<String> nameHolder = new ArrayList<String>();
    for (int i = 0; i < adjList.size(); i++) {
      nameHolder.add(adjList.get(i).getName());
    }
    // if the list doesn't contain either v1 or v2, return
    if (!nameHolder.contains(vertex1) || !nameHolder.contains(vertex2))
      return;
    for (int i = 0; i < adjList.size(); i++) {
      // if v1 is found in the adjList, store its index
      if (adjList.get(i).getName().equals(vertex1)) {
        // check if edge already exists from v1 --> v2
        if (adjList.get(i).getDependencyList().contains(vertex2)) {
          adjList.get(i).dependencyList.remove(vertex2);// removes v2 from dependency list of v1
        }
      }
    }

  }

  /**
   * Returns a Set that contains all the vertices
   * 
   * @return a Set<String> which contains all the vertices in the graph
   */
  public Set<String> getAllVertices() {
    Set<String> toReturn = new HashSet<>();
    // iterate through adjList
    for (int i = 0; i < adjList.size(); i++) {
      toReturn.add(adjList.get(i).getName());
    }
    return toReturn;
  }

  /**
   * Get all the neighbor (adjacent-dependencies) of a vertex
   * 
   * For the example graph, A->[B, C], D->[A, B] getAdjacentVerticesOf(A) should return [B, C].
   * 
   * In terms of packages, this list contains the immediate dependencies of A and depending on your
   * graph structure, this could be either the predecessors or successors of A.
   * 
   * @param vertex the specified vertex
   * @return an List<String> of all the adjacent vertices for specified vertex
   */
  public List<String> getAdjacentVerticesOf(String vertex) {
    for (int i = 0; i < adjList.size(); i++) {
      if (adjList.get(i).getName().equals(vertex)) {
        return adjList.get(i).getDependencyList();
      }
    }
    return null;
  }

  /**
   * Returns the number of edges in this graph.
   * 
   * @return number of edges in the graph.
   */
  public int size() {
    int toReturn = 0;
    for (int i = 0; i < adjList.size(); i++) {
      toReturn += adjList.get(i).getDependencyList().size();
    }
    return toReturn;
  }

  /**
   * Returns the number of vertices in this graph.
   * 
   * @return number of vertices in graph.
   */
  public int order() {
    return adjList.size();
  }
}
