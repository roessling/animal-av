package generators.graph.boruvka;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Graph;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

/**
 * 
 * @author Ahmed CHarfi und Jihed Ouni
 * 
 */
public class Boruvka implements Generator {
  private Language        lang;
  // private MatrixProperties matrixProps;
  private TextProperties  text;
  private GraphProperties gprops;
  private int[][]         adjMatrix;

  /**
   * Constructor
   */
  public Boruvka() {
    lang = new AnimalScript("Boruvka [DE]", "Ahmed Charfi , Jihed Ouni", 800,
        600);

  }

  /**
   * initialzation
   */
  public void init() {
    lang = new AnimalScript("Boruvka [DE]", "Ahmed Charfi , Jihed Ouni", 800,
        600);
  }

  /**
 * 
 */
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    // matrixProps =
    // (MatrixProperties)props.getPropertiesByName("matrixProps");

    text = (TextProperties) props.getPropertiesByName("text");
    gprops = (GraphProperties) props.getPropertiesByName("graph");

    if (text == null)
      text = getDefaultTextProperties();
    if (gprops == null)
      gprops = getDefaultGraphProperties();

    text.set("font", new Font("SansSerif", Font.PLAIN, 12));
    gprops.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);

    /*
     * Iterator it = gprops.getAllPropertyNames().iterator();
     * 
     * String propName;
     * 
     * while(it !=null && it.hasNext()) { propName = (String) it.next();
     * //sc1.addCodeLine(propName + " " + text.get(propName), null, 0, null);
     * System.out.println(propName + " " + gprops.get(propName)); }
     * 
     * it = text.getAllPropertyNames().iterator();
     * 
     * while(it !=null && it.hasNext()) { propName = (String) it.next();
     * //sc1.addCodeLine(propName + " " + text.get(propName), null, 0, null);
     * System.out.println(propName + " " + text.get(propName)); }
     */

    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    // text = (TextProperties) props.getPropertiesByName("text");
    adjMatrix = (int[][]) primitives.get("adjMatrix");

    if (adjMatrix == null) {
      System.out.println("adj matrix is null");
      adjMatrix = getDefaultAdjMatrix(1);
    }

    start();
    findmst(props, primitives);

    return lang.toString();
  }

  /**
   * Name
   */
  public String getName() {
    return "Boruvka [DE]";
  }

  /**
   * return Name Algorithm
   */
  public String getAlgorithmName() {
    return "Boruvka Algorithmus";
  }

  /**
   * Authornamen
   */
  public String getAnimationAuthor() {
    return "Ahmed Charfi, Jihed Ouni";
  }

  /**
   * Description of Algorithm
   */
  public String getDescription() {
    return "Der Algorithmus von Boruvka ist ein  Algorithmus zur Ermittlung eines minimalen Spannbaums"
        + "\n"
        + "eines gewichteten, ungerichteten Graphen erwähnt, der Algorithmus von Boruvka."
        + "\n"
        + "Dieser stammt aus dem Jahre 1926 und ist aus Kruskals und Prims Algorithmus hervorgegangen."
        + "\n"
        + "Er behandelt den Spezialfall, dass die Kantengewichte paarweise verschieden sind."
        + "\n"
        + "\n"
        + "Der Algorithmus betrachtet anfangs jeden Knoten als Baum bzw. isolierte  Komponente"
        + "\n"
        + "In jeder Iteration sucht sich jeder Knoten die Kante mit dem niedrigsten Wert,"
        + "\n"
        + "welche die aktuelle Komponente mit einer anderen Komponente verbindet. Diese"
        + "\n"
        + "Kante wird dann in den minimalen Spannbaum aufgenommen"
        + "\n"
        + "Dabei werden Kanten so hinzugenommen, dass stets zwei Komponenten immer nur"
        + "\n"
        + "durch eine Kante verbunden werden und auftretende Kreise aufgelöst werden. Dieser"
        + "\n"
        + "Schritt wird solange wiederholt, bis nur noch eine Komponente existiert, die dann"
        + "\n" + "einen minimalen Spannbaum des Ausgangsgraphen bildet.";
  }

  /**
   * code Example
   */

  public String getCodeExample() {
    return "Gegeben sei ein zusammenhengender, ungerichteter gewichteter Graph G "
        + "\n"
        + "fuer jeden Knoten i"
        + "\n"
        + "\n"
        + "	finde günstigste ki Kante, die i enthält"
        + "\n"
        + "füge ki zum minimalen Spannbaum M hinzu."
        + "\n"
        + "vereinige alle Komponenten von M "
        + "\n"
        + "end für "
        + "\n"
        + "\n"
        + " Solange (Anzahl Komponenten von M > 1)"
        + "\n"
        + "	wähle 2 Komponenten von M: K1 und K2 "
        + "\n"
        + "finde die günstigste Kante k zwischen K1 und K2"
        + "\n"
        + " so dass k nicht bereits in M ist. "
        + "\n"
        + "vereinige K1 und K2"
        + "\n"
        + "aktualisiere Anzahl der Komponenten"
        + "\n"
        + "ende Solange"
        + "\n" + "\n";
  }

  /**
   * FileExtension
   */
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  /**
   * ContentLocale
   */
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  /**
   * Generator Type
   */
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
  }

  /**
   * OutPut Language
   */
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  /**
   * start the algorithm
   */
  private void start() {

    lang.setStepMode(true);

    SourceCodeProperties scTitle = new SourceCodeProperties();
    scTitle.set(AnimationPropertiesKeys.FONT_PROPERTY, this.text.get("font"));

    // new Font( "Monospaced", Font.PLAIN, 18));

    scTitle.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    SourceCode sc = lang.newSourceCode(new Coordinates(90, 40), "title", null,
        scTitle);

    SourceCodeProperties scPresen1 = new SourceCodeProperties();
    scPresen1.set(AnimationPropertiesKeys.FONT_PROPERTY, this.text.get("font"));
    // new Font("Monospaced", Font.PLAIN, 16));

    scPresen1.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scPresen1.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        this.text.get("color"));// Color.BLACK);
    SourceCode sc1 = lang.newSourceCode(new Coordinates(50, 70),
        "Presnetation1", null, scPresen1);

    sc.addCodeLine("Der Algorithmus von Boruvka:", null, 0, null);
    sc1.addCodeLine("", null, 0, null);

    sc1.addCodeLine(
        "Der Algorithmus von Boruvka ermittelt einen minimalen Spannbaum in",
        null, 0, null);
    sc1.addCodeLine("einem gewichteten ungerichteten Graphen.", null, 0, null);
    sc1.addCodeLine("", null, 0, null);
    sc1.addCodeLine(
        "Der Algorithmus stammt aus dem Jahre 1926 und ist aus Kruskals und Prims Algorithmus hervorgegangen.",
        null, 0, null);
    sc1.addCodeLine(
        "Er behandelt den Spezialfall, dass die Kantengewichte paarweise verschieden sind.",
        null, 0, null); // 0

    // lang.nextStep();
    lang.nextStep("1.Einleitung");
    // lang.nextStep(lang.getStep(), "1.Einleitung");

    sc1.hide();
    // ///////////////////////////////////////////////////////////////////////

    SourceCode sc2 = lang.newSourceCode(new Coordinates(50, 70), "sourceCode",
        null, scPresen1);

    sc2.addCodeLine(" 		", null, 0, null);

    sc2.addCodeLine(
        "Der Algorithmus betrachtet anfangs jeden Knoten als Baum bzw. isolierte Komponente.",
        null, 0, null);
    sc2.addCodeLine(" ", null, 0, null);
    sc2.addCodeLine(
        "In jeder Iteration sucht der Algorithmus für jeden Knoten die Kante mit der niedrigsten",
        null, 0, null);

    sc2.addCodeLine(
        "Gewichtung, welche die aktuelle Komponente mit einer anderen Komponente verbindet.",
        null, 0, null);
    sc2.addCodeLine(
        "Diese Kante wird dann in den minimalen Spannbaum aufgenommen.", null,
        0, null); // 0
    sc2.addCodeLine(
        "Dabei werden Kanten so hinzugenommen, dass stets zwei Komponenten immer nur",
        null, 0, null); // 0
    sc2.addCodeLine(
        "durch eine Kante verbunden werden und auftretende Kreise aufgelöst werden. ",
        null, 0, null); // 0
    sc2.addCodeLine(
        "Dieser Schritt wird solange wiederholt, bis nur noch eine Komponente existiert,",
        null, 0, null); // 0
    sc2.addCodeLine(
        "die dann einen minimalen Spannbaum des Ausgangsgraphen bildet.				",
        null, 0, null); // 0
    lang.nextStep("2.Beschreibung");
    // lang.nextStep(lang.getStep(),"2.Beschreibung");
    sc2.hide();
    // ///////////////////////////////////////////////////////////////////////
    // Pseudo Code

    getPseudoCode();

  }

  private void getPseudoCode() {
    SourceCodeProperties scPseudCo = new SourceCodeProperties();
    // scPseudCo.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scPseudCo.set(AnimationPropertiesKeys.FONT_PROPERTY, this.text.get("font"));
    // new Font("Monospaced", Font.PLAIN, 16));

    scPseudCo.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scPseudCo.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        this.text.get("color"));
    SourceCode sc3 = lang.newSourceCode(new Coordinates(90, 70), "sourceCode",
        null, scPseudCo);

    // Add the lines to the SourceCode object.
    // Line, name, indentation, display dealy

    sc3.addCodeLine("PSEUDO_CODE", null, 0, null);
    sc3.addCodeLine("", null, 0, null);

    sc3.addCodeLine("Gegeben sei ein zusammenhängender, ungerichteter ", null,
        0, null);
    sc3.addCodeLine("und gewichteter Graph G. ", null, 0, null);

    sc3.addCodeLine("", null, 0, null);
    sc3.addCodeLine("für jeden Knoten i", null, 1, null);
    sc3.addCodeLine("finde günstigste ki Kante, die i enthaelt", null, 2, null);

    sc3.addCodeLine("füge ki zum minimalen Spannbaum M hinzu", null, 2, null);
    sc3.addCodeLine("vereinige alle Komponenten von M ", null, 2, null);
    sc3.addCodeLine("end für", null, 1, null);
    sc3.addCodeLine("", null, 0, null); // 0
    sc3.addCodeLine("Solange (Anzahl Komponenten von M > 1)", null, 1, null); // 0

    sc3.addCodeLine("wähle 2 Komponenten von M: K1 und K2 ", null, 2, null); // 0

    sc3.addCodeLine("finde günstigste Kante k zwischen K1 und K2", null, 2,
        null); // 0

    sc3.addCodeLine("so dass k nicht bereits in M ist. ", null, 2, null); // 0
    sc3.addCodeLine("vereinige K1 und K2", null, 2, null); // 0
    sc3.addCodeLine("aktualisiere Anzahl der Komponenten", null, 2, null); // 0
    sc3.addCodeLine("ende Solange", null, 1, null); // 0

    lang.nextStep("3.Pseudo-Code");
    sc3.hide();
  }

  /**
   * fin minmal spannbaum
   * 
   * @param props
   * @param primitives
   */
  private void findmst(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    int[][] matrix = (int[][]) primitives.get("adjMatrix");

    if (matrix == null) {
      System.out.print("Matrix ist null");
      matrix = getDefaultAdjMatrix();

    }
    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[0].length; j++) {

        // System.out.print(matrix[i][j] + " " );

      }
      // System.out.println();
    }

    boolean isNull = true;
    for (int i = 0; i < matrix.length; i++)
      for (int j = 0; j < matrix[0].length; j++)
        if (matrix[i][j] != 0)
          isNull = false;

    if (isNull) {
      // System.out.println("Hallo");
      matrix = getDefaultAdjMatrix();
    }
    // create the graph again in order to be able to set the graph
    // properties

    int size = matrix.length;

    Node[] graphNodes = new Node[size];
    String[] nodeLabels = new String[size];

    int startX = 100;
    int startY = 200;
    int x = startX;
    int y = startY;
    int xmax = startX;
    int delta = 80;
    if (size > 10)
      delta = 50;

    for (int i = 0; i < size; i++) {
      graphNodes[i] = new Coordinates(x, y);

      if (i < Math.round(size / 2) - 1) {
        x = x + delta;
        xmax = x;

      }

      if (i == Math.round(size / 2) - 1) {

        y = y + 200;

      }

      if (i > Math.round(size / 2) - 1) {
        x = x - delta;
      }

    }
    // allow user to modify this via props

    // define the names of the nodes
    char startChar = 65;

    for (int i = 0; i < size; i++) {
      nodeLabels[i] = startChar + "";
      startChar++;
    }

    gprops.setName("Boruvka Algorithm");

    Graph g = lang.newGraph(this.getName(), matrix, graphNodes, nodeLabels,
        null, gprops);

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if ((i != j) && (matrix[i][j] != 0)
            && (matrix[i][j] != Integer.MAX_VALUE))
          g.setEdgeWeight(i, j, matrix[i][j], null, null);
      }
    }

    lang.nextStep("4.Graph");
    // ////////

    // sort adjazenzmatrix
    ArrayList<Integer> myListe = new ArrayList<Integer>();

    // iterate over all nodes
    int min;
    int jmin = 0;

    for (int i = 0; i < size; i++) {

      // find cheapest edge
      min = Integer.MAX_VALUE;

      for (int j = 0; j < size; j++) {

        if (j == i)
          matrix[i][j] = Integer.MAX_VALUE;

        if (matrix[i][j] < min && matrix[i][j] != 0) {
          min = matrix[i][j];
          jmin = j;
        }
      }
      // we found cheapest edge
      myListe.add(jmin);

    }

    // Take edge with minimum weight

    SourceCodeProperties scGraph1 = new SourceCodeProperties();
    scGraph1.set(AnimationPropertiesKeys.FONT_PROPERTY, this.text.get("font"));

    scGraph1.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scGraph1
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, this.text.get("color"));
    SourceCode scG1;
    SourceCode scPseu = scG1 = lang.newSourceCode(
        new Coordinates(xmax + 35, 50), "sourceCode", null, scGraph1);
    ;

    int counter = 0;
    int sum = 0;

    scPseu.addCodeLine("", null, 0, null);
    scPseu.addCodeLine("für  jeden Knoten i", "1", 1, null);
    scPseu
        .addCodeLine("finde günstigste ki Kante, die i enthält", "2", 2, null);

    scPseu
        .addCodeLine(" füge ki zum minimalen Spannbaum M hinzu", "3", 2, null);
    scPseu.addCodeLine("vereinige alle Komponenten von M ", "4", 2, null);
    scPseu.addCodeLine("end fuer", "5", 1, null);
    scPseu.addCodeLine("", "6", 0, null); // 0
    scPseu.addCodeLine("Solange (Anzahl Komponenten von M > 1)", "7", 1, null); // 0

    scPseu.addCodeLine("wähle 2 Komponenten von M: K1 und K2 ", "8", 2, null); // 0

    scPseu.addCodeLine("finde die günstigste Kante k zwischen K1 und K2", "9",
        2, null); // 0

    scPseu.addCodeLine("so dass k nicht bereits in M ist. ", "10", 2, null); // 0
    scPseu.addCodeLine("vereinige K1 und K2", "11", 2, null); // 0
    scPseu.addCodeLine("aktualisiere Anzahl der Komponenten", "12", 2, null); // 0
    scPseu.addCodeLine("ende Solange", "13", 1, null); // 0

    ArrayList<Integer> listsum = new ArrayList<Integer>();
    ArrayList<Index> mylistsum = new ArrayList<Index>();

    scG1 = lang.newSourceCode(new Coordinates(xmax + 485, 50), "sourceCode",
        null, scGraph1);

    scG1.addCodeLine("", null, 0, null);

    int mycounter = counter + 1;
    //

    scG1.addCodeLine("Step" + mycounter + ":", null, 0, null);

    scG1.addCodeLine(" Der Algorithmus nimmt  jeden Knoten ", null, 0, null);
    scG1.addCodeLine(" unter die Lupe und ermittelt die günstigste Kante.",
        null, 0, null);

    Index myIndex;

    for (int i = 0; i < size; i++) {
      scG1.addCodeLine(
          " Die günstigste Kante " + getAsChar(i) + ","
              + getAsChar(myListe.get(i)) + " hat das Gewicht "
              + matrix[i][myListe.get(i)], "Zeile3Step1", 0, null);

      myIndex = new Index(matrix[i][myListe.get(i)], i, myListe.get(i));

      scG1.highlight("Zeile3Step1");
      scPseu.highlight("1");
      scPseu.highlight("2");
      scPseu.highlight("3");

      g.highlightEdge(i, myListe.get(i), null, null);
      g.highlightNode(i, null, null);
      g.highlightNode(myListe.get(i), null, null);

      boolean found = false;
      // check that element is not alredy contained
      for (int k = 0; k < mylistsum.size(); k++) {
        if (mylistsum.get(k).val == myIndex.val
            && mylistsum.get(k).x == myIndex.y
            && mylistsum.get(k).y == myIndex.x) {
          found = true;
          break;
        }
      }

      if (!found) {
        sum = sum + matrix[i][myListe.get(i)];
        listsum.add(matrix[i][myListe.get(i)]);
        mylistsum.add(myIndex);
      }
      lang.nextStep();
      scPseu.unhighlight("1");
      scPseu.unhighlight("2");
      scPseu.unhighlight("3");

    }
    scG1.addCodeLine(
        " Die günstigste Kanten werden in dem Spannbaum aufgenommen",
        "Zeile4Step1", 0, null);
    scPseu.highlight("4");
    scPseu.highlight("5");

    List<ArrayList<Integer>> components = getInitialComponents(myListe);

    lang.nextStep();
    scPseu.unhighlight("4");
    scPseu.highlight("7");
    scPseu.unhighlight("5");

    scG1.addCodeLine(" Die Anzahl der Komponenten ist " + components.size(),
        "Zeile5Step1", 0, null);

    scG1.highlight("Zeile5Step1");

    lang.nextStep();
    scPseu.unhighlight("7");

    scG1.hide();

    counter++;
    // lang.nextStep();

    scG1 = lang.newSourceCode(new Coordinates(xmax + 485, 50), "sourceCode",
        null, scGraph1);

    scG1.addCodeLine("", null, 0, null);

    while (components.size() > 1) {

      mycounter = counter + 1;

      scG1.addCodeLine("Step" + mycounter + ":", null, 0, null);

      scG1.addCodeLine(" Die Anzahl der Komponenten ist " + components.size()
          + ".", null, 0, null);
      scPseu.highlight("7");

      scG1.addCodeLine(" Der Algorithmus versucht je zwei Komponenten  ", null,
          0, null);
      scG1.addCodeLine(" mit der günstigsten Kante zu finden. ", null, 0, null);

      lang.nextStep();
      scPseu.highlight("7");
      scPseu.highlight("8");

      scPseu.highlight("9");
      scPseu.highlight("10");
      scPseu.highlight("11");

      // iterate over all components, try to find cheapest edge to other
      // components.
      ArrayList<Integer> comp1, comp2;

      ArrayList<Index> connections = new ArrayList<Index>();

      // for (int i = 0; i < components.size(); i++) {
      // comp1 = components.get(i);
      comp1 = components.get(0);
      Index ind = new Index(0, 0, 0);

      for (int j = 1; j < components.size(); j++) {
        comp2 = components.get(j);
        // for each node in comp1, find chepeast edge to nodes
        // of comp2.
        int curr1, curr2;

        for (int k = 0; k < comp2.size(); k++) {
          curr2 = comp2.get(k);

          for (int l = 0; l < comp1.size(); l++) {
            curr1 = comp1.get(l);
            // check if there is an edge connecting curr 1
            // and curr 2
            if (matrix[curr1][curr2] != 0) {
              ind = new Index(matrix[curr1][curr2], curr1, curr2);
              connections.add(ind);
            }
          }
        }
      }

      Collections.sort(connections, ind);
      // get minimal connection
      scG1.addCodeLine(
          " Die günstigste Kante " + getAsChar(connections.get(0).x) + ","
              + getAsChar(connections.get(0).y) + " mit dem Gewicht "
              + connections.get(0).val, "mefteh1", 0, null);
      scG1.addCodeLine(

      " wird in dem minimalen Spannbaum aufgenommen.", "mefteh", 0, null);

      scG1.highlight("mefteh");
      scG1.highlight("mefteh1");
      g.highlightEdge(connections.get(0).x, connections.get(0).y, null, null);
      g.highlightNode(connections.get(0).x, null, null);
      g.highlightNode(connections.get(0).y, null, null);

      lang.nextStep();
      scPseu.unhighlight("8");
      scPseu.unhighlight("9");
      scPseu.unhighlight("10");
      scPseu.unhighlight("11");

      sum = sum + connections.get(0).val;
      listsum.add(connections.get(0).val);
      mylistsum.add(connections.get(0));

      // unite components.
      this.MergeComponents(connections.get(0).x, connections.get(0).y,
          components);
      counter++;

      // }
      // }

      // }

    }
    g.hide();

    scG1.hide();

    SourceCode scy = lang.newSourceCode(new Coordinates(50, 70),
        "Presnetationsum", null, scGraph1);
    scPseu.hide();
    scy.addCodeLine("                        ", null, 0, null);

    scy.addCodeLine(" Wir haben nur noch eine Komponente. ", null, 0, null);
    scy.addCodeLine(
        " Der Algorithmus ist somit fertig.                       ", null, 0,
        null);
    scy.addCodeLine("                        ", null, 0, null);

    int j = 0;
    for (int i = 0; i < listsum.size(); i++) {
      j = i + 1;
      scy.addCodeLine("                " + j + ". Kante   "
          + getAsChar(mylistsum.get(i).x) + "," + getAsChar(mylistsum.get(i).y)
          + " : " + mylistsum.get(i).val + " +", null, 0, null);
    }
    scy.addCodeLine("                                ----- ", null, 0, null);
    scy.addCodeLine("Die Länge des minimalen Spannbaum = " + sum, null, 0, null);
    lang.nextStep("5.minimale Spannbaum");

    scy.hide();
    SourceCode sc4 = lang.newSourceCode(new Coordinates(50, 70),
        "Presnetation1", null, scGraph1);

    sc4.addCodeLine("", null, 0, null);
    sc4.addCodeLine("", null, 0, null);

    sc4.addCodeLine(
        "Der Algorithmus von Boruvka bestimmt einen minimal Spannbaum mit",
        null, 0, null);
    sc4.addCodeLine("der Zeitkomplexität O(|V*V| log|V|).", null, 0, null);
    sc4.addCodeLine(
        "Die Suche nach der Kante mit dem geringsten Gewicht, die mit jeder Komponente",
        null, 0, null);
    sc4.addCodeLine(
        "inzident ist, benötigt O(V*V) Vergleiche. Die Anzahl der Komponenten reduziert sich",
        null, 0, null);
    sc4.addCodeLine(
        "dabei in jeder Iteration um den Faktor zwei, die Anzahl der Zusammenhangskomponenten",
        null, 0, null); // 0

    sc4.addCodeLine(
        "wird also mindestens halbiert. Folglich sind O(log |V |) Iterationen nötig, um",
        null, 0, null); // 0

    sc4.addCodeLine("den minimalen Spannbaum zu ermitteln.", null, 0, null); // 0
    sc4.addCodeLine("Somit ergibt sich eine Laufzeit von O(|V*V| log |V |)",
        null, 0, null);
    // lang.nextStep(lang.getStep(),"6.Complexity");
    lang.nextStep("6.Complexity");

  }

  /**
   * Initial components
   * 
   * @param myListe
   * @return
   */
  private List<ArrayList<Integer>> getInitialComponents(
      ArrayList<Integer> myListe) {
    // int numComp = myListe.size();

    List<ArrayList<Integer>> components = new ArrayList<ArrayList<Integer>>();

    // put each minimum edge in a separate component

    for (int i = 0; i < myListe.size(); i++) {

      ArrayList<Integer> comp = new ArrayList<Integer>();

      int tmp = myListe.get(i);
      comp.add(i);
      comp.add(tmp);

      components.add(comp);
    }

    // eliminate duplicates

    int size = components.size();

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {

        if ((j != i) && (components.get(i).containsAll(components.get(j)))) {

          components.remove(j);
          size = components.size();
        }
      }
    }

    // eliminate duplicates from myListe

    for (int i = 0; i < myListe.size(); i++) {
      if (myListe.get(myListe.get(i)) == i) {
        myListe.set(i, -1);
      }
    }

    MergeComponents(components);

    return components;

  }

  /**
   * Merge components
   * 
   * @param x
   * @param y
   * @param components
   */
  private void MergeComponents(int x, int y, List<ArrayList<Integer>> components) {

    // check for common nodes between minimum edges and unite components
    ArrayList<Integer> comp1, comp2;

    for (int l = 0; l < components.size(); l++) {
      comp1 = components.get(l);

      // search for comp1 which contains x
      if (!comp1.contains(x))
        continue;

      // search for comp2 which contains y
      for (int j = 0; j < components.size(); j++) {
        comp2 = components.get(j);

        // search for comp2 which contains y
        if (!comp2.contains(y) || j == l)
          continue;

        // if we found the min edge
        if (comp1.contains(x) && comp2.contains(y)) {
          // add all elements of comp2 to comp1
          for (int m = 0; m < comp2.size(); m++) {
            if (!comp1.contains(comp2.get(m)))
              comp1.add(comp2.get(m));
          }

          // remove comp2 from components
          components.remove(j);

          return;
        }
      }
    }

  }

  /**
   * Merge componenets
   * 
   * @param components
   */
  private void MergeComponents(List<ArrayList<Integer>> components) {

    // check for common nodes between minimum edges and unite components
    ArrayList<Integer> comp1, comp2;
    int curr;

    for (int l = 0; l < components.size(); l++) {
      comp1 = components.get(l);

      // check if we find a node of comp1 in another component
      for (int k = 0; k < comp1.size(); k++) {
        curr = comp1.get(k);

        for (int j = 0; j < components.size(); j++) {

          comp2 = components.get(j);

          if ((j == l) || !comp2.contains(curr)) {
            continue;
          } else {
            // we need to merge
            comp1.addAll(comp2);
            components.remove(j);
            // return;
          }
        }
      }
    }
  }

  /**
   * get char
   * 
   * @param ind
   * @return retChar
   */
  public char getAsChar(int ind) {
    char retChar = (char) (65 + ind);
    return retChar;
  }

  /**
   * 
   * @param mat
   * @param val
   * @return the index
   */
  public Index getIndex(int[][] mat, int val) {
    Index ind = new Index(-1, -1);

    for (int i = 0; i < mat.length; i++)
      for (int j = 0; j < mat.length; j++) {
        if (mat[i][j] == val) {
          ind = new Index(i, j);
          return ind;
        }
      }
    return ind;
  }

  /**
   * main method
   * 
   * @param args
   */
  public static void main(String[] args) {

    Boruvka b = new Boruvka();
    if (b.text == null)
      b.text = getDefaultTextProperties();
    if (b.gprops == null)
      b.gprops = getDefaultGraphProperties();

    b.start();

    Hashtable<String, Object> primitives = new Hashtable<String, Object>();
    primitives.put("adjMatrix", getDefaultAdjMatrix());

    AnimationPropertiesContainer props = new AnimationPropertiesContainer();

    b.findmst(props, primitives);

    System.out.println(b.lang);
  }

  public static GraphProperties getDefaultGraphProperties() {

    GraphProperties graphProps = new GraphProperties();
    graphProps.set(AnimationPropertiesKeys.EDGECOLOR_PROPERTY, Color.black);
    graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.red);
    graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
    graphProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.red);

    graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
    graphProps.set(AnimationPropertiesKeys.WEIGHTED_PROPERTY, true);

    return graphProps;

  }

  public static TextProperties getDefaultTextProperties() {

    TextProperties tprops = new TextProperties();
    tprops.set("font", new Font("SansSerif", 1, 16));
    tprops.set("color", Color.black);
    return tprops;

  }

  /**
   * 
   * @param matrix
   * @return the number of edges
   */
  public int getNumEdges(int[][] matrix) {
    int counter = 0;

    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix.length; j++) {
        if (i != j && matrix[i][j] != 0 && i < j) {
          counter++;
        }

      }
    }
    return counter;
  }

  /**
   * 
   * @return the defaultMatrix
   */
  public static int[][] getDefaultAdjMatrix() {
    return getDefaultAdjMatrix(3);
  }

  /**
   * some tests
   * 
   * @param index
   * @return the default adjacency matrix
   */
  public static int[][] getDefaultAdjMatrix(int index) {
    int[][] AdjMatrix;
    if (index == 0) {
      AdjMatrix = new int[5][5];
      for (int i = 0; i < AdjMatrix.length; i++) {
        for (int j = 0; j < AdjMatrix.length; j++) {
          if (i == j) {
            AdjMatrix[i][j] = 0;
          }

        }
      }
      AdjMatrix[0][1] = 35;
      AdjMatrix[1][0] = 35;
      AdjMatrix[0][3] = 40;
      AdjMatrix[3][0] = 40;
      AdjMatrix[1][3] = 25;
      AdjMatrix[3][1] = 25;
      AdjMatrix[1][2] = 10;
      AdjMatrix[2][1] = 10;
      AdjMatrix[2][3] = 20;
      AdjMatrix[3][2] = 20;
      AdjMatrix[2][4] = 30;
      AdjMatrix[4][2] = 30;
      AdjMatrix[3][4] = 15;
      AdjMatrix[4][3] = 15;
      //
    } else if (index == 1) {
      AdjMatrix = new int[6][6];
      for (int i = 0; i < AdjMatrix.length; i++) {
        for (int j = 0; j < AdjMatrix.length; j++) {
          if (i == j) {
            AdjMatrix[i][j] = 0;
          }

        }
      }
      AdjMatrix[0][1] = 5;
      AdjMatrix[1][0] = 5;
      AdjMatrix[1][2] = 4;
      AdjMatrix[2][1] = 4;
      AdjMatrix[2][3] = 5;
      AdjMatrix[3][2] = 5;
      AdjMatrix[3][4] = 8;
      AdjMatrix[4][3] = 8;
      AdjMatrix[4][5] = 4;
      AdjMatrix[5][4] = 4;
      AdjMatrix[5][0] = 9;
      AdjMatrix[0][5] = 9;
      //

    }

    else if (index == 2) {
      AdjMatrix = new int[4][4];
      for (int i = 0; i < AdjMatrix.length; i++) {
        for (int j = 0; j < AdjMatrix.length; j++) {
          if (i == j) {
            AdjMatrix[i][j] = 0;
          }

        }
      }
      AdjMatrix[0][1] = 1;
      AdjMatrix[1][0] = 1;
      AdjMatrix[1][2] = 4;
      AdjMatrix[2][1] = 4;
      AdjMatrix[1][3] = 3;
      AdjMatrix[3][1] = 3;
      AdjMatrix[2][3] = 2;
      AdjMatrix[3][2] = 2;
      //
    } else if (index == 3) {
      AdjMatrix = new int[8][8];
      for (int i = 0; i < AdjMatrix.length; i++) {
        for (int j = 0; j < AdjMatrix.length; j++) {
          if (i == j) {
            AdjMatrix[i][j] = 0;
          }

        }
      }
      AdjMatrix[0][1] = 4;
      AdjMatrix[1][0] = 4;
      AdjMatrix[1][2] = 11;
      AdjMatrix[2][1] = 11;
      AdjMatrix[2][3] = 3;
      AdjMatrix[3][2] = 3;
      AdjMatrix[3][4] = 12;
      AdjMatrix[4][3] = 12;
      AdjMatrix[4][5] = 2;
      AdjMatrix[5][4] = 2;
      AdjMatrix[5][6] = 10;
      AdjMatrix[6][5] = 10;
      AdjMatrix[6][7] = 1;
      AdjMatrix[7][6] = 1;
      AdjMatrix[7][0] = 9;
      AdjMatrix[0][7] = 9;

      AdjMatrix[2][6] = 7;
      AdjMatrix[6][2] = 7;

      //
    } else if (index == 4) {
      AdjMatrix = new int[14][14];
      for (int i = 0; i < AdjMatrix.length; i++) {
        for (int j = 0; j < AdjMatrix.length; j++) {
          if (i == j) {
            AdjMatrix[i][j] = 0;
          }

        }
      }
      AdjMatrix[0][1] = 4;
      AdjMatrix[1][0] = 4;
      AdjMatrix[1][2] = 11;
      AdjMatrix[2][1] = 11;
      AdjMatrix[2][3] = 3;
      AdjMatrix[3][2] = 3;
      AdjMatrix[3][4] = 132;
      AdjMatrix[4][3] = 132;
      AdjMatrix[4][5] = 27;
      AdjMatrix[5][4] = 27;
      AdjMatrix[5][6] = 10;
      AdjMatrix[6][5] = 10;
      AdjMatrix[6][7] = 1;
      AdjMatrix[7][6] = 1;
      AdjMatrix[7][0] = 9;
      AdjMatrix[0][7] = 9;
      AdjMatrix[7][8] = 6;
      AdjMatrix[8][7] = 6;
      AdjMatrix[9][0] = 2;
      AdjMatrix[0][9] = 2;
      AdjMatrix[10][9] = 12;
      AdjMatrix[9][10] = 45;
      AdjMatrix[10][11] = 45;
      AdjMatrix[11][10] = 12;
      AdjMatrix[11][12] = 62;
      AdjMatrix[12][11] = 62;
      AdjMatrix[12][13] = 19;
      AdjMatrix[13][12] = 19;
      AdjMatrix[2][6] = 7;
      AdjMatrix[6][2] = 7;

      //
    } else if (index == 5) {
      AdjMatrix = new int[4][4];
      for (int i = 0; i < AdjMatrix.length; i++) {
        for (int j = 0; j < AdjMatrix.length; j++) {
          if (i == j) {
            AdjMatrix[i][j] = 0;
          }

        }
      }
      AdjMatrix[0][1] = 25;
      AdjMatrix[1][0] = 25;
      AdjMatrix[1][2] = 1;
      AdjMatrix[2][1] = 1;
      AdjMatrix[0][2] = 3;
      AdjMatrix[2][0] = 3;
      AdjMatrix[0][3] = 20;
      AdjMatrix[3][0] = 20;
      //
    }

    else if (index == 6) {
      AdjMatrix = new int[3][3];
      for (int i = 0; i < AdjMatrix.length; i++) {
        for (int j = 0; j < AdjMatrix.length; j++) {
          if (i == j) {
            AdjMatrix[i][j] = 0;
          }

        }
      }
      AdjMatrix[0][1] = 24;
      AdjMatrix[1][0] = 24;
      AdjMatrix[0][2] = 3;
      AdjMatrix[2][0] = 3;
      AdjMatrix[1][2] = 14;
      AdjMatrix[2][1] = 14;
      //
    } else {
      AdjMatrix = new int[4][4];
      for (int i = 0; i < AdjMatrix.length; i++) {
        for (int j = 0; j < AdjMatrix.length; j++) {
          if (i == j) {
            AdjMatrix[i][j] = 0;
          }

        }
      }
      AdjMatrix[0][1] = 25;
      AdjMatrix[1][0] = 25;
      AdjMatrix[1][2] = 1;
      AdjMatrix[2][1] = 1;
      AdjMatrix[0][2] = 3;
      AdjMatrix[2][0] = 3;
      AdjMatrix[0][3] = 20;
      AdjMatrix[3][0] = 20;
      //
    }

    return AdjMatrix;
  }

}

/**
 * class Index
 * 
 * @author ahmed
 * 
 */
class Index implements Comparator<Index> {
  public int val;
  public int x;
  public int y;

  /**
   * 
   * @param a
   * @param b
   */
  public Index(int a, int b) {
    this.x = a;
    this.y = b;
    this.val = 0;
  }

  /**
   * 
   * @param val
   * @param a
   * @param b
   */
  public Index(int val, int a, int b) {
    this.val = val;
    this.x = a;
    this.y = b;
  }

  @Override
  /**
   * 
   */
  public int compare(Index o1, Index o2) {

    if (o1.val < o2.val)
      return -1;
    else if (o1.val == o2.val)
      return 0;
    else
      return 1;
  }
}
