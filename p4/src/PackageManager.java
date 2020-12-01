import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Filename: PackageManager.java Project: p4 Authors: Elijah Asher
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

public class PackageManager {
  private Graph graph;

  /*
   * Package Manager default no-argument constructor.
   */
  public PackageManager() {
    graph = new Graph();
  }

  /**
   * Takes in a file path for a json file and builds the package dependency graph from it.
   * 
   * @param jsonFilepath the name of json data file with package dependency information
   * @throws FileNotFoundException if file path is incorrect
   * @throws IOException           if the give file cannot be read
   * @throws ParseException        if the given json cannot be parsed
   */
  public void constructGraph(String jsonFilepath)
      throws FileNotFoundException, IOException, ParseException {
    // create an object that holds the json elements
    Object obj = new JSONParser().parse(new FileReader(jsonFilepath));
    // cast object to a json object
    JSONObject jo = (JSONObject) obj;
    JSONArray packages = (JSONArray) jo.get("packages");
    // iterate through packages and make graph
    for (int i = 0; i < packages.size(); i++) {
      JSONObject jsonPackages = (JSONObject) packages.get(i);
      String name = (String) jsonPackages.get("name");
      JSONArray dep = (JSONArray) jsonPackages.get("dependencies");
      graph.addVertex(name);
      if (dep.size() > 0) {
        for (int j = 0; j < dep.size(); j++) {
          graph.addEdge(name, (String) dep.get(j));
        }
      }
    }
  }

  /**
   * Helper method to get all packages in the graph.
   * 
   * @return Set<String> of all the packages
   */
  public Set<String> getAllPackages() {
    return graph.getAllVertices();
  }

  /**
   * Given a package name, returns a list of packages in a valid installation order.
   * 
   * Valid installation order means that each package is listed before any packages that depend upon
   * that package.
   * 
   * @return List<String>, order in which the packages have to be installed
   * 
   * @throws CycleException           if you encounter a cycle in the graph while finding the
   *                                  installation order for a particular package. Tip: Cycles in
   *                                  some other part of the graph that do not affect the
   *                                  installation order for the specified package, should not throw
   *                                  this exception.
   * 
   * @throws PackageNotFoundException if the package passed does not exist in the dependency graph.
   */
  public List<String> getInstallationOrder(String pkg)
      throws CycleException, PackageNotFoundException {
    // check if package is present
    if (!graph.getAllVertices().contains(pkg))
      throw new PackageNotFoundException();
    // create list of verteces
    LinkedList<String> verteces = new LinkedList<String>();
    verteces.add(pkg);
    // initalize 2nd list for installation order
    LinkedList<String> order = new LinkedList<String>();
    order.add(pkg);
    while (!verteces.isEmpty()) {
      String temp = verteces.remove(0);// store and remove 1st element in list
      List<String> dependencyList = graph.getAdjacentVerticesOf(temp);
      // traverse through dependencies
      for (String depend : dependencyList) {
        if (depend.equals(pkg))
          throw new CycleException();
        order.addFirst(depend);
        verteces.add(depend);// add dependent vertex to lists
      }
    }
    for (int i = 0; i < order.size(); i++) {
      String temp = order.remove(i);
      while (order.contains(temp))
        order.remove(temp);
      order.add(i, temp);
    }
    return order;
  }
  /////// unused code
  /**
   * Private recursive helper for getInstallationOrder
   * 
   * @param str - finds all dependent verteces
   * @return - list of dependent verteces
   */
  // private ArrayList<String> installationOrderHelper(String str, String[][] adjList) {
  // // TODO: check for cyclic error
  // ArrayList<String> toReturn = new ArrayList<String>();
  // // iterate through adjList
  // for (int i = 0; i < adjList.length; i++) {
  // // check if this row has been visited
  // if ((adjList[i][0] != null)) {
  // // go through and mark nodes depending on str as visited (null)
  // for (int k = 1; k <= graph.getAdjacentVerticesOf(adjList[i][0]).size(); k++) {
  // // node at i is dependent on str
  // if (adjList[i][k].equals(str)) {// pkg was found in the dependency list of another vertex
  // String recurString = adjList[i][0];
  // adjList[i][0] = null;// mark visited
  // toReturn = installationOrderHelper(recurString, adjList);// make recursive call
  // toReturn.add(str);// add to the end
  // }
  // }
  // }
  // }
  // return toReturn;
  // }

  /**
   * Given two packages - one to be installed and the other installed, return a List of the packages
   * that need to be newly installed.
   * 
   * For example, refer to shared_dependecies.json - toInstall("A","B") If package A needs to be
   * installed and packageB is already installed, return the list ["A", "C"] since D will have been
   * installed when B was previously installed.
   * 
   * @return List<String>, packages that need to be newly installed.
   * 
   * @throws CycleException           if you encounter a cycle in the graph while finding the
   *                                  dependencies of the given packages. If there is a cycle in
   *                                  some other part of the graph that doesn't affect the parsing
   *                                  of these dependencies, cycle exception should not be thrown.
   * 
   * @throws PackageNotFoundException if any of the packages passed do not exist in the dependency
   *                                  graph.
   */
  public List<String> toInstall(String newPkg, String installedPkg)
      throws CycleException, PackageNotFoundException {
    // see if packages are in the graph
    if (!(graph.getAllVertices().contains(newPkg) || graph.getAllVertices().contains(installedPkg)))
      throw new PackageNotFoundException();
    // create list of installed dependencies and needed dependencies
    List<String> installed = getInstallationOrder(installedPkg);
    List<String> needed = getInstallationOrder(newPkg);
    for (String i : installed) {
      if (needed.contains(i))
        needed.remove(i);
    }
    return needed;
  }

  /**
   * Return a valid global installation order of all the packages in the dependency graph.
   * 
   * assumes: no package has been installed and you are required to install all the packages
   * 
   * returns a valid installation order that will not violate any dependencies
   * 
   * @return List<String>, order in which all the packages have to be installed
   * @throws CycleException           if you encounter a cycle in the graph
   * @throws PackageNotFoundException
   */
  public List<String> getInstallationOrderForAllPackages()
      throws CycleException, PackageNotFoundException {
    // collects all verteces into an object arr
    Object[] toReturn = graph.getAllVertices().toArray();
    boolean[] visited = new boolean[toReturn.length];
    List<String> installationOrder = new ArrayList<String>();
    // traverse through all verteces
    for (int k = 0; k < toReturn.length; k++) {
      // check if visited
      if (!visited[k]) {
        List<String> order = getInstallationOrder((String) toReturn[k]);
        Collections.reverse(order); // corrects installation order
        for (int w = 0; w < order.size(); w++) {
          if (!installationOrder.contains(order.get(w)) && order.get(w) != null) {
            installationOrder.add(w, order.get(w));
          }
        }
      }
    }
    Collections.reverse(installationOrder);
    return installationOrder;

  }

  /**
   * Find and return the name of the package with the maximum number of dependencies.
   * 
   * Tip: it's not just the number of dependencies given in the json file. The number of
   * dependencies includes the dependencies of its dependencies. But, if a package is listed in
   * multiple places, it is only counted once.
   * 
   * Example: if A depends on B and C, and B depends on C, and C depends on D. Then, A has 3
   * dependencies - B,C and D.
   * 
   * @return String, name of the package with most dependencies.
   * @throws CycleException if you encounter a cycle in the graph
   */
  public String getPackageWithMaxDependencies() throws CycleException {
    // Initializes an iterator for all verteces
    Iterator<String> verteces = graph.getAllVertices().iterator();
    String toReturn = "";
    int max = Integer.MIN_VALUE;
    while (verteces.hasNext()) {
      String temp = verteces.next(); // holds string in temp
      List<String> adjList = graph.getAdjacentVerticesOf(temp);
      // check if max needs to be modified
      if (adjList.size() > max) {
        max = adjList.size();
        toReturn = temp;
      }
    }
    return toReturn;
  }

  public static void main(String[] args) {
    System.out.println("PackageManager.main()");
  }

}
