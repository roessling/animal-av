package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graph.helpers.ScriptWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.generators.Language;

public class PathSearch implements Generator {

  private Language     lang;

  private String       start;
  private int[][]      matrix;
  private String       target;
  private List<String> nodesList;
  private ScriptWriter scriptWriter;
  private int[][]      coordinates;

  public void init() {
    lang = new AnimalScript("Path Search", "Sheip Darugtev", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    scriptWriter = new ScriptWriter(lang, props, primitives);
    coordinates = (int[][]) primitives.get("coordinates");
    matrix = (int[][]) primitives.get("matrix");
    target = (String) primitives.get("target");
    start = (String) primitives.get("start");
    String[] nodeLabels = (String[]) primitives.get("nodeLabels");
    nodesList = Arrays.asList(nodeLabels);
    start();
    return lang.toString();
  }

  private void start() {
    if (!validMatrix()) {
      scriptWriter.writeMatrixError();
      return;
    }
    if (!validCoordinates()) {
      return;
    }
    if (duplicatedLabels()) {
      scriptWriter.writeDuplicatesError();
      return;
    }
    scriptWriter.writeInitialization();

    try {
      areConnected(start, target);
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }
  }

  private boolean validCoordinates() {
    if (coordinates.length == matrix.length && coordinates[0].length == 2)
      return true;
    return false;
  }

  private boolean duplicatedLabels() {
    for (String label : nodesList) {
      int count = 0;
      for (String label2 : nodesList) {
        if (label.equals(label2))
          count++;
      }
      if (count > 1)
        return true;
    }
    return false;
  }

  private boolean validMatrix() {
    if (matrix[0].length != matrix.length || nodesList.size() != matrix.length) {
      return false;
    }
    return true;
  }

  /**
   * 
   * @param start
   *          node
   * @param target
   *          node
   * @return true if there is path between <code>start</code> and
   *         <code>target</code>
   * @throws IllegalDirectionException
   */
  private boolean areConnected(String start, String target)
      throws IllegalDirectionException {
    Vector<String> toDo = new Vector<String>();
    Vector<String> visited = new Vector<String>();
    toDo.add(start);
    scriptWriter.writeInitials(toDo);

    while (!toDo.isEmpty()) {
      String current = toDo.get(0);
      visited.add(current);
      toDo.remove(current);
      List<String> neighbors = getNeighbors(current);
      scriptWriter.writeNextNode(current, visited, toDo);

      for (int i = 0; i < neighbors.size(); i++) {
        String neighbor = neighbors.get(i);
        scriptWriter.writeDefaultForNeighbor(current, neighbor);
        if (neighbor.equals(target)) {
          scriptWriter.writeSuccess();
          return true;
        }
        if (!toDo.contains(neighbor) && !visited.contains(neighbor)) {
          toDo.add(neighbor);
          scriptWriter.writeAddNeighborToTODO(toDo, neighbor);
        } else {
          scriptWriter.writeNextNeighborIteration();
        }
      }
    }
    scriptWriter.writeFailed();
    return false;
  }

  /**
   * 
   * @param node
   * @return list with neighbors for the node
   */
  private List<String> getNeighbors(String node) {
    List<String> result = new ArrayList<String>();
    int index = nodesList.indexOf(node);
    for (int i = 0; i < matrix[index].length; i++) {
      if (matrix[index][i] != 0 || matrix[i][index] != 0)
        result.add(nodesList.get(i));
    }
    return result;
  }

  public String getName() {
    return "Path Search";
  }

  public String getAlgorithmName() {
    return "Path Search";
  }

  public String getAnimationAuthor() {
    return "Sheip Darugtev";
  }

  public String getDescription() {
    return "	The algorithm calculates if 2 given Nodes in a graph are connected. "
        + "\n"
        + "It works by starting with the given start node and working its way forward"
        + "\n"
        + "by traversing the neighbors of each visited node, until either the target"
        + "\n"
        + "node is reached or until it traverses all nodes to which the start node has a connection."
        + "\n"
        + "<p><b>Input information:</b></p>"
        + "<ul> "
        + "<li><i>nodeLabels</i> - The labels inside the graph nodes. The node labels should be unique(no duplicates).</li> "
        + "<li><i>matrix</i> - The adjacency matrix for the graph.</li> "
        + "<li><i>coordinates</i> - The coordinates of each node. It must be an array with as many rows as there are nodes<br />"
        + "and 2 columns representing x-coordinate and y-coordinate of each node.</li>"
        + "<li><i>start</i> - The start node.</li>"
        + "<li><i>target</i> - The target node.</li>" + "<ul>";
  }

  public String getCodeExample() {
    return "public void areConnected(Node start, Node target) {" + "\n"
        + "	List<Node> toDoList=new ArrayList<Node>();" + "\n"
        + "	List<Node> visited=new ArrayList<Node>();" + "\n"
        + "	toDoList.add(start);" + "\n" + "	while(!toDoList.isEmpty()) {"
        + "\n" + "		Node current=toDoList.get(0);" + "\n"
        + "		visited.add(current);" + "\n" + "		toDoList.remove(current);"
        + "\n" + "		for(Node neighbor : current.neighbors()) {" + "\n"
        + "			if(neighbor.equals(target))" + "\n" + "				return true;" + "\n"
        + "			if(!toDoList.contains(neighbor) && !visited.contains(neighbor))"
        + "\n" + "				toDoList.add(neighbor);" + "\n" + "		}" + "\n" + "	}"
        + "\n" + "	return false;" + "\n" + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}