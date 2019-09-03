/**
 * 
 */
package generators.misc;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Point;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PointProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;

/**
 * This class implements an AnimalScript animation of Conway's Game of Life
 * 
 * @author Tobias Raffel
 * @version 0.1
 */
public class GameOfLive implements Generator {

  // Helpful names
  enum Keys {

    POPULATED, EMPTY, UNHIGHLIGHT, HIGHLIGHT, PROSPERITY, REPRODUCTION, OVERCROWDING_OR_UNDERPOPULATION, UNDERPOPULATION

  }

  // Modular variables
  private int                  sizeX, sizeY;
  private int                  iterations;
  int[][]                      pattern;
  Color                        colorAlive;
  Color                        colorDead;
  Color                        colorNotCalculated;

  // Non-modular variables
  Language                     lang;

  private RectProperties       cpOriginal, cpDuplicate, cpAnalysis;
  private SourceCodeProperties scProps;

  Font                         fontContent;
  Font                         fontCode;
  private Font                 fontHeadline;
  private Font                 fontTitle;

  /**
   * Starts the animation
   */
  public void start() {

    // The global headline
    Text textHeadline = lang.newText(new Offset(0, 0, "O",
        AnimalScript.DIRECTION_NW), "Conway's Game of Life", "HGL", null);
    textHeadline.setFont(fontHeadline, null, null);

    // The slide-local title
    Text textTitle = lang.newText(new Offset(10, 20, "O",
        AnimalScript.DIRECTION_NW), "NULL", "HSL", null);
    textTitle.setFont(fontTitle, null, null);

    // First Slide: Introduction
    textTitle.setText("-> Einleitung", null, null);
    showIntro();

    // Second Slide: Algorithm animation
    textTitle.setText("-> Algorithmus im Detail - eine Generation", null, null);
    showAnimationInDetail();

    // Second Slide: Algorithm animation
    textTitle.setText("-> Ein weiteres Beispiel - mehrere Generationen", null,
        null);
    showAnimationExamples();

    // Third Slide: Summary
    textTitle.setText("-> Bemerkungen", null, null);
    showSummary();
  }

  /**
   * Shows the introduction
   */
  private void showIntro() {

    Vector<Primitive> hideList = new Vector<Primitive>(14);

    final String s1 = "John Horton Conway entwarf das Game of Life im Jahre 1970. Es handelt sich dabei ";
    final String s2 = "um einen zellulaeren Automaten, welcher als zweidimensionales Gitter dargestellt wird.";
    final String s3 = "Das Spielfeld ist dabei in einzelne Zellen aufgeteilt, von denen sich jede Zelle stets";
    final String s4 = "in genau einem von zwei Zustaenden befindet - lebendig oder tot.";
    final String s5 = "Als Initialisierung werden einige lebendige Zellen auf dem Spielfeld verteilt,";
    final String s6 = "danach ist keine weitere Benutzerinteraktion moeglich.";

    final String s7 = "Das Spiel beschraenkt sich auf die algorithmische Ausfuehrung der folgenden vier Regeln:";
    final String s8 = "1. Eine lebendige Zelle mit mehr als drei lebendigen Nachbarzellen stirbt.";
    final String s9 = "2. Eine lebendige Zelle mit weniger als zwei lebendigen Nachbarzellen stirbt.";
    final String s10 = "3. Eine lebendige Zelle mit zwei oder drei lebendigen Nachbarzellen ueberlebt bis zur naechsten Generation.";
    final String s11 = "4. Eine tote Zelle mit genau drei lebendigen Nachbarzellen wird in der naechsten Generation lebendig sein.";

    final String s12 = "Beim Iterieren ueber die Zellen, darf man das Ergebnis einer Regelanwendung nicht unmittelbar uebernehmen,";
    final String s13 = "sonst wuerden bei spaeter iterierten Zellen falsche Informationen verarbeitet und potenziell falsche Regeln";
    final String s14 = "angewendet.";

    final String[] from1To6 = { s1, s2, s3, s4, s5, s6 };
    final String[] from7To11 = { s7, s8, s9, s10, s11 };
    final String[] from12To14 = { s12, s13, s14 };

    hideList.addAll(showText(from1To6, 0, 80, 20, "O", "I", fontContent, null,
        null, null));

    lang.nextStep("Einleitung");

    hideList.addAll(showText(from7To11, 0, 240, 40, "O", "I", fontContent, 1,
        null, null));

    lang.nextStep();

    hideList.addAll(showText(from12To14, 0, 460, 20, "O", "I", fontContent,
        null, null, null));

    hide(hideList);
  }

  /**
   * Shows the algorithm visualization
   */
  private void showAnimationInDetail() {

    int[][] pattern = { { 0, 0, 0, 0, 0 }, { 0, 1, 1, 1, 0 },
        { 0, 1, 0, 1, 0 }, { 0, 1, 1, 1, 0 }, { 0, 0, 0, 0, 0 } };

    // Declare all variables
    Keys k;
    World present, future;
    Analyser analysis;
    SourceCode code;
    Offset offset;
    Vector<Primitive> hideList;
    Legend legend;

    // Initialize all variables
    hideList = new Vector<Primitive>(45);

    legend = new Legend();
    legend.init(520, 300, "O");

    future = new World();
    int[][] dummy = new int[0][0];
    future.init(900, 100, 5, 5, 20, cpDuplicate, colorAlive,
        colorNotCalculated, Keys.EMPTY, dummy);

    present = new World();
    present.init(520, 100, 5, 5, 20, cpOriginal, colorAlive, colorDead,
        Keys.POPULATED, pattern);

    analysis = new Analyser();
    analysis.init(660, 120, 20, cpAnalysis, colorAlive, colorDead, present);

    offset = new Offset(0, 80, "O", AnimalScript.DIRECTION_NW);
    code = lang.newSourceCode(offset, "SCP", null, scProps);

    code.addCodeLine("...", null, 0, null); // 0
    code.addCodeLine("int neighbors;", null, 0, null); // 1
    code.addCodeLine("// Fuer alle Gebiete in der Welt...", null, 0, null); // 2
    code.addCodeLine("for(int y = 0; y < world.height(); y++)", null, 0, null); // 3
    code.addCodeLine("for(int x = 0; x < world.length(); x++) {", null, 1, null); // 4
    code.addCodeLine("// ...bestimme die Anzahl der direkten Nachbarn", null,
        2, null); // 5
    code.addCodeLine("neighbors = countNeighbors(x, y);", null, 2, null); // 6
    code.addCodeLine("// Ist Gebiet bewohnt...", null, 2, null); // 7
    code.addCodeLine("if(world[y][x].isPopulated()) {", null, 2, null); // 8
    code.addCodeLine("// Ausreichend starke Bevoelkerung", null, 3, null); // 9
    code.addCodeLine("if(neighbors == 2  || neighbors == 3)", null, 3, null); // 10
    code.addCodeLine("populate(x,y);", null, 4, null); // 11
    code.addCodeLine("// Unter-/Ueberbevoelkerung", null, 3, null); // 12
    code.addCodeLine("else", null, 3, null); // 13
    code.addCodeLine("dieOut(x,y);", null, 4, null); // 14
    code.addCodeLine("// ... oder unbewohnt", null, 2, null); // 15
    code.addCodeLine("// Reproduktion", null, 2, null); // 16
    code.addCodeLine("else if(neighbors == 3)", null, 2, null); // 17
    code.addCodeLine("populate(x,y);", null, 4, null); // 18
    code.addCodeLine("}", null, 1, null); // 19
    code.addCodeLine("...", null, 0, null); // 20

    // Start Animation
    future.changeColors(Keys.UNHIGHLIGHT, colorDead);

    code.highlight(0);
    lang.nextStep("Algorithmus im Detail");

    code.toggleHighlight(0, 1);
    lang.nextStep();

    // For each cell apply rules
    for (int y = 0; y < 5; y++) {

      code.toggleHighlight(1, 3);
      lang.nextStep();

      for (int x = 0; x < 5; x++) {

        code.toggleHighlight(3, 4);
        lang.nextStep();

        k = analysis.focus(x, y, present);

        code.toggleHighlight(4, 6);
        lang.nextStep();

        code.toggleHighlight(6, 8);
        lang.nextStep();

        switch (k) {
        // Alive
          case PROSPERITY: {

            code.toggleHighlight(8, 10);
            analysis.showDecision(k);

            lang.nextStep();

            code.toggleHighlight(10, 11);
            future.status[y][x] = future.populate(x, y);

            lang.nextStep();

            analysis.resetDecision();
            code.unhighlight(11);

            break;
          }
          // Alive
          case OVERCROWDING_OR_UNDERPOPULATION: {

            code.toggleHighlight(8, 10);
            analysis.showDecision(k);

            lang.nextStep();

            code.toggleHighlight(10, 14);
            future.status[y][x] = future.dieOut(x, y);

            lang.nextStep();

            analysis.resetDecision();
            code.unhighlight(14);

            break;
          }
          // Dead
          case REPRODUCTION: {

            code.toggleHighlight(8, 17);
            analysis.showDecision(k);

            lang.nextStep();

            code.toggleHighlight(17, 18);
            future.status[y][x] = future.populate(x, y);

            lang.nextStep();

            analysis.resetDecision();
            code.unhighlight(18);

            break;
          }
          // Dead
          case UNDERPOPULATION: {

            code.toggleHighlight(8, 17);
            analysis.showDecision(k);

            lang.nextStep();

            analysis.resetDecision();

            future.status[y][x] = future.dieOut(x, y);
            code.unhighlight(17);
            break;
          }
          case EMPTY:
          case HIGHLIGHT:
          case POPULATED:
          case UNHIGHLIGHT:
          default:
            break;
        }

      }
    }

    // The future is now...
    analysis.resetAll();
    future.changeColors(Keys.UNHIGHLIGHT, colorNotCalculated);
    future.resetTo(Keys.EMPTY, null, null);
    present.evolute();
    code.highlight(20);
    lang.nextStep();

    code.unhighlight(20);

    // Gather all hide-lists and hide
    hideList.add(code);
    hideList.addAll(present.hideList);
    hideList.addAll(future.hideList);
    hideList.addAll(analysis.hideList);
    hideList.addAll(legend.hideList);
    hide(hideList);
  }

  /**
   * Shows an example
   */
  private void showAnimationExamples() {

    Vector<Primitive> hideList = new Vector<Primitive>(sizeY * sizeX);

    World example = new World();

    example.init(0, 80, sizeX, sizeY, 20, cpOriginal, colorAlive, colorDead,
        Keys.POPULATED, pattern);

    lang.nextStep("Sandkasten");
    if (iterations > 0)
      example.evolute();

    for (int i = 1; i < iterations; i++) {

      lang.nextStep();
      example.evolute();

    }

    hideList.addAll(example.hideList);
    hide(hideList);
  }

  /**
   * Shows the summary
   */
  private void showSummary() {

    Vector<Primitive> hideList = new Vector<Primitive>(21);

    final String s1 = "Die Muster werden anhand ihres Verhaltens kategorisiert: ";
    final String s2 = "Still-Leben - Die Form des Mustern aendert sich auch bei beliebig vielen Iterationen nicht.";
    final String s3 = "Oszillatoren - Diese Muster haben ein periodisches Verhalten.";
    final String s4 = "Raumschiffe - Dabei handelt es sich um bewegliche Muster.";

    final String s5 = "Kombiniert man geschickt verschiedene Muster lassen sich daraus komplexe Szenarien entwerfen,";
    final String s6 = "welche wiederum ein besonderes Verhalten vorweisen. Man kann so beispielsweise Fabriken entwickeln,";
    final String s7 = "die beliebig viele Muster der Kategorie Raumschiff erzeugen.";

    final String s8 = "Unter der Annahme, dass das Spielfeld eine unendliche Ausdehnung besitzt, kann so ein lineares,";
    final String s9 = "aber unbegrenztes Wachstum modelliert werden.";
    final String s10 = "Ein komplexes Muster, welches solch ein Szenario modelliert, nennt sich Glider Gun (siehe bspw. wikipedia).";

    final String[] from1To4 = { s1, s2, s3, s4 };
    final String[] from5To7 = { s5, s6, s7 };
    final String[] from8To10 = { s8, s9, s10 };

    lang.nextStep("Schlussbemerkungen");

    hideList.addAll(showText(from1To4, 0, 80, 40, "O", "I", fontContent, 1,
        null, null));

    lang.nextStep();

    hideList.addAll(showText(from5To7, 0, 300, 20, "O", "I", fontContent, null,
        null, null));

    lang.nextStep();

    hideList.addAll(showText(from8To10, 0, 400, 20, "O", "I", fontContent,
        null, null, null));

    hide(hideList);
  }

  /**
   * This class represents the world
   * 
   * @author Tobias Raffel
   * @version 1.0
   */
  private class World {

    // Class attributes
    private Rect[][]  world;
    private Color     colorHighlighted, colorUnhighlighted;
    Vector<Primitive> hideList;
    /**
     * If a status[x][y] is true, the associated cell is populated
     */
    boolean[][]       status;
    private int       refCnt;
    private int       sizeX, sizeY;

    // Constructor
    public World() {

      // Prevents default references
      refCnt = 0;
      while (lang.isNameUsed("C" + refCnt)) {

        refCnt++;
      }
    }

    /**
     * Initializes the world
     * 
     * @param xPos
     *          - the upperLeft x-position of the first rectangle
     * @param yPos
     *          - the upperLeft y-position of the first rectangle
     * @param cellSize
     *          - the x-y-dimension of a cell
     * @param rp
     *          - a reference to a Rectangle-Properties object
     * @param s
     *          - a key determining whether the world is populated or not: </br>
     *          </br>Keys.POPULATED the world becomes initialized with the input
     *          pattern </br>Keys.EMPTY the world will be empty
     * 
     */
    public void init(int xPos, int yPos, int _sizeX, int _sizeY, int cellSize,
        RectProperties rp, Color highlighted, Color unhighlighted, Keys s,
        int[][] pattern) {

      sizeX = _sizeX;
      sizeY = _sizeY;

      world = new Rect[sizeY][sizeX];
      status = new boolean[sizeY][sizeX];

      hideList = new Vector<Primitive>(sizeY * sizeX);

      // Temporary cell reference, offsets,...
      Rect cell;
      Offset upperLeft, lowerRight;
      int x, y;

      // Set colors
      colorHighlighted = highlighted;
      colorUnhighlighted = unhighlighted;

      // Set origin - relative to "O"
      x = xPos;
      y = yPos;

      // For each position in the world...
      for (int i = 0; i < sizeY; i++, y += 20) {

        for (int j = 0; j < sizeX; j++, x += 20) {

          // ...prepare objects...
          upperLeft = new Offset(x, y, "O", AnimalScript.DIRECTION_NW);
          lowerRight = new Offset(x + cellSize, y + cellSize, "O",
              AnimalScript.DIRECTION_SE);
          cell = lang.newRect(upperLeft, lowerRight, "C" + refCnt++, null, rp);

          // ...and initialize
          world[i][j] = cell;
          status[i][j] = false;

          // ...and add to hide-list
          hideList.add(cell);

        }
        x = xPos;
      }
      // If the world shall initialized populated, the initial pattern has to be
      // read
      if (s == Keys.POPULATED) {
        for (y = 0; y < pattern.length; y++) {
          for (x = 0; x < pattern[0].length; x++) {

            if (pattern[y][x] == 1) {
              status[y][x] = populate(x, y);
            }
          }
        }
      }

    }

    /**
     * Resets the world, that means it completely un-populated </br>NOTE: Has
     * only an effect if this world is initialized
     * 
     * @param s
     *          - a key determining whether the world is populated or not: </br>
     *          </br>Keys.POPULATED the world becomes initialized with the input
     *          pattern </br>Keys.EMPTY the world will be empty
     */
    public void resetTo(Keys s, Integer[] xPattern, Integer[] yPattern) {

      // Kill all population
      for (int y = 0; y < sizeY; y++) {

        for (int x = 0; x < sizeX; x++) {

          status[y][x] = dieOut(x, y);
        }
      }

      // Resets world with initial pattern
      if (s == Keys.POPULATED) {

        // Set initial pattern
        for (int y = 0; y < pattern.length; y++) {
          for (int x = 0; x < pattern[0].length; x++) {

            if (pattern[y][x] == 1) {
              status[y][x] = populate(x, y);
            }
          }
        }
      }
    }

    /**
     * Changes the fill-colors
     * 
     * @param k
     *          - HIGHLIGHT, or UNHIGHLIGHT
     * @param color
     *          - a Color
     */
    public void changeColors(Keys k, Color color) {

      switch (k) {

        case HIGHLIGHT: {

          colorHighlighted = color;
          return;
        }
        case UNHIGHLIGHT: {

          colorUnhighlighted = color;
          return;
        }
        default:
          return;
      }
    }

    /**
     * Determines whether a focused cell is alive
     * 
     * @param x
     *          - the x-position of the focused cell
     * @param y
     *          - the y-position of the focused cell
     * @return - true if the neighbor is alive, else false
     */
    public boolean neighborAlive(int x, int y) {

      // Considering the border of the world
      try {
        // If he/she is alive -> return 1
        if (status[y][x]) {

          return true;
        }
        // Else the neighbor is dead -> return 0
        else {

          return false;
        }
      }
      // We re beyond the border - phew, something caught us
      catch (IndexOutOfBoundsException e) {

        return false;
      }
    }

    /**
     * Computes the next evolutionary status of this world
     */
    public void evolute() {

      boolean[][] tmp;

      // For each cell compute the next (evolutionary) status
      tmp = new boolean[sizeY][sizeX];
      for (int y = 0; y < sizeY; y++) {

        for (int x = 0; x < sizeX; x++) {

          if (status(x, y)) {

            tmp[y][x] = populate(x, y);
          } else {

            tmp[y][x] = dieOut(x, y);
          }
        }
      }

      // Set next (evolutionary) status
      for (int y = 0; y < sizeY; y++) {

        System.arraycopy(tmp[y], 0, status[y], 0, sizeX);
      }
    }

    /**
     * Populates a cell - highlight cell
     * 
     * @param x
     *          - the x-Coordinate
     * @param y
     *          - the y-Coordinate
     */
    boolean populate(int x, int y) {

      world[y][x].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
          colorHighlighted, null, null);

      return true;
    }

    /**
     * A cell dies out - un-highlight cell
     * 
     * @param x
     *          - the x-Coordinate
     * @param y
     *          - the y-Coordinate
     */
    boolean dieOut(int x, int y) {

      world[y][x].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
          colorUnhighlighted, null, null);

      return false;
    }

    /**
     * Checks the neighborhood and determines whether the focused cell will die
     * or not
     * 
     * @param x
     *          - the x-position of the focused cell
     * @param y
     *          - the y-position of the focused cell
     * @param alive
     *          - true if focused cell is alive, false if dead
     * @return - true if it should be populated, false if it will die out
     */
    private boolean status(int x, int y) {

      int lives = 0;
      boolean alive = status[y][x];

      // Upper neighbors
      lives += addNeighbor(x - 1, y - 1);
      lives += addNeighbor(x, y - 1);
      lives += addNeighbor(x + 1, y - 1);

      // Neighbors at the same level
      lives += addNeighbor(x - 1, y);
      lives += addNeighbor(x + 1, y);

      // Lower neighbors
      lives += addNeighbor(x - 1, y + 1);
      lives += addNeighbor(x, y + 1);
      lives += addNeighbor(x + 1, y + 1);

      // Rules
      if (alive) {

        // Well populated
        if (lives == 2 || lives == 3) {

          return true;
        }
        // Overcrowded or under-population
        else {

          return false;
        }
      } else {

        // Reproduction
        if (lives == 3) {

          return true;
        } else {

          return false;
        }
      }
    }

    /**
     * Determines whether a focused cell is alive
     * 
     * @param x
     *          - the x-position of the focused cell
     * @param y
     *          - the y-position of the focused cell
     * @return - 1 if the neighbor is alive, else 0
     */
    private int addNeighbor(int x, int y) {

      // Considering the border of the world
      try {
        // If he/she is alive -> return 1
        if (status[y][x]) {

          return 1;
        }
        // Else the neighbor is dead -> return 0
        else {

          return 0;
        }
      }
      // We re beyond the border - phew, something caught us
      catch (IndexOutOfBoundsException e) {

        return 0;
      }
    }
  }

  /**
   * Represents an peephole, focusing a 3x3 area of a world
   * 
   * @author Tobias Raffel
   * @version 1.0
   */
  private class Analyser {

    // Class variables
    private Rect[][]  environment;
    private Text      textFocused, textCount, textRule;
    private Color     colorHighlighted, colorUnhighlighted;
    private int       refCnt;

    Vector<Primitive> hideList;

    // Constructor
    public Analyser() {

      environment = new Rect[3][3];
      hideList = new Vector<Primitive>(10);

      // Prevents default references
      refCnt = 0;
      while (lang.isNameUsed("P" + refCnt)) {

        refCnt++;
      }
    }

    /**
     * Initializes the peephole
     * 
     * @param xPos
     *          - the upperLeft x-position of the first rectangle
     * @param yPos
     *          - the upperLeft y-position of the first rectangle
     * @param cellSize
     *          - the x-y-dimension of a cell
     * @param rp
     *          - a reference to a Rectangle-Properties object
     * @param highlighted
     *          - a color for highlighted cells
     * @param unhighlighted
     *          - a color for un-highlighted cells
     * @param referenceTo
     *          - a reference to a World-object
     */
    public void init(int xPos, int yPos, int cellSize, RectProperties rp,
        Color highlighted, Color unhighlighted, World referenceTo) {

      // Temporary cell reference, offsets,...
      Rect cell;
      Offset upperLeft, lowerRight;
      int x, y;

      // Set colors
      colorHighlighted = highlighted;
      colorUnhighlighted = unhighlighted;

      // Set origin - relative to "O"
      x = xPos;
      y = yPos;

      // For each position in the world...
      for (int i = 0; i < 3; i++, y += 20) {

        for (int j = 0; j < 3; j++, x += 20) {

          // ...prepare objects...
          upperLeft = new Offset(x, y, "O", AnimalScript.DIRECTION_NW);
          lowerRight = new Offset(x + cellSize, y + cellSize, "O",
              AnimalScript.DIRECTION_SE);
          cell = lang.newRect(upperLeft, lowerRight, "P" + refCnt++, null, rp);

          // ...and initialize
          environment[i][j] = cell;

          // ...and add to hide-list
          hideList.add(cell);

        }
        x = xPos;
      }

      upperLeft = new Offset(xPos + 70, yPos, "O", AnimalScript.DIRECTION_NW);
      textFocused = lang.newText(upperLeft, "(x, y)", "tP" + refCnt, null);
      textFocused.setFont(fontContent, null, null);

      upperLeft = new Offset(xPos + 70, yPos + 20, "O",
          AnimalScript.DIRECTION_NW);
      textCount = lang
          .newText(upperLeft, "|Nachbarn| = 0", "tP" + refCnt, null);
      textCount.setFont(fontContent, null, null);

      upperLeft = new Offset(xPos + 70, yPos + 40, "O",
          AnimalScript.DIRECTION_NW);
      textRule = lang.newText(upperLeft, "=>", "tP" + refCnt, null);
      textRule.setFont(fontContent, null, null);

      hideList.add(textFocused);
      hideList.add(textCount);
      hideList.add(textRule);
    }

    /**
     * Focuses a 3x3 environment around atX, atY
     * 
     * @param atX
     *          - an x-Coordinate
     * @param atY
     *          - an y-Coordinate
     */
    Keys focus(int atX, int atY, World world) {

      boolean alive = false;
      int lives = 0;
      Keys k;
      String s;

      alive = world.status[atY][atX];

      // For each cell around the focused cell, lookup the world-status in that
      // area
      for (int i = 0, y = atY - 1; i < 3; i++, y++) {

        for (int j = 0, x = atX - 1; j < 3; j++, x++) {

          if (world.neighborAlive(x, y)) {

            highlight(j, i);

            if (i != 1 || j != 1) {
              lives++;
            }

          } else {

            unhighlight(j, i);
          }
        }
      }

      // Rules
      if (alive) {

        s = "lebendig";
        // Well populated
        if (lives == 2 || lives == 3) {

          k = Keys.PROSPERITY;
        }
        // Overcrowded or under-population
        else {

          k = Keys.OVERCROWDING_OR_UNDERPOPULATION;
        }
      } else {

        s = "tot";
        // Reproduction
        if (lives == 3) {

          k = Keys.REPRODUCTION;
        } else {

          k = Keys.UNDERPOPULATION;
        }
      }

      // Set text
      textFocused.setText("(" + atX + ", " + atY + ") - " + s, null, null);
      textCount.setText("|Nachbarn| = " + lives, null, null);

      return k;
    }

    /**
     * Changes Text to a new decision-text
     * 
     * @param k
     *          - a key, this is the decision criteria
     */
    void showDecision(Keys k) {

      String s;

      if (k == Keys.PROSPERITY || k == Keys.REPRODUCTION) {

        s = "=> wird leben";
      } else {
        s = "=> wird sterben";
      }

      textRule.setText(s, null, null);
    }

    /**
     * Resets decision-text on default value
     */
    void resetDecision() {

      textRule.setText("=>", null, null);
    }

    /**
     * Resets all elements to default value
     */
    void resetAll() {

      // Reset peephole
      for (int y = 0; y < 3; y++) {

        for (int x = 0; x < 3; x++) {

          unhighlight(x, y);
        }
      }

      // Reset texts
      textFocused.setText("(x, y)", null, null);
      textCount.setText("|Nachbarn| = 0", null, null);
      textRule.setText("=>", null, null);
    }

    /**
     * Highlights a cell
     * 
     * @param x
     *          - the x-Coordinate
     * @param y
     *          - the y-Coordinate
     */
    private void highlight(int x, int y) {

      environment[y][x].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
          colorHighlighted, null, null);
    }

    /**
     * Un-highlights a cell
     * 
     * @param x
     *          - the x-Coordinate
     * @param y
     *          - the y-Coordinate
     */
    private void unhighlight(int x, int y) {

      environment[y][x].changeColor(AnimationPropertiesKeys.FILL_PROPERTY,
          colorUnhighlighted, null, null);
    }
  }

  /**
   * This class represents a new complex Animal-primitive - a legend
   * 
   * @author Tobias Raffel
   * @version 1.0
   */
  private class Legend {

    Vector<Primitive> hideList;

    /**
     * Creates a new instance of Legend NOTE: PLEASE ONLY ONE INSTANCE
     */
    public Legend() {

    }

    /**
     * initializes the Legend with default values NOTE PLEASE ONLY ONCE
     * INITIALIZE
     */
    public void init(int xPos, int yPos, String upperLeft) {

      hideList = new Vector<Primitive>(7);

      Rect frame, boxAlive, boxDead, boxNIter;
      Text textAlive, textDead, textNIter;

      RectProperties frameProps;
      RectProperties box1Props;
      RectProperties box2Props;
      RectProperties box3Props;

      frameProps = new RectProperties();
      frameProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
      frameProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
      frameProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

      box1Props = new RectProperties();
      box1Props.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      box1Props.set(AnimationPropertiesKeys.FILL_PROPERTY, colorAlive);
      box1Props.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

      box2Props = new RectProperties();
      box2Props.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      box2Props.set(AnimationPropertiesKeys.FILL_PROPERTY, colorDead);
      box2Props.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

      box3Props = new RectProperties();
      box3Props.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      box3Props.set(AnimationPropertiesKeys.FILL_PROPERTY, colorNotCalculated);
      box3Props.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

      // RecTangles
      Offset off1;
      Offset off2;

      off1 = new Offset(xPos, yPos, upperLeft, AnimalScript.DIRECTION_NW);
      off2 = new Offset(xPos + 155, yPos + 90, upperLeft,
          AnimalScript.DIRECTION_NW);
      frame = lang.newRect(off1, off2, "LF", null, frameProps);

      off1 = new Offset(xPos + 5, yPos + 5, upperLeft,
          AnimalScript.DIRECTION_NW);
      off2 = new Offset(xPos + 25, yPos + 25, upperLeft,
          AnimalScript.DIRECTION_NW);
      boxAlive = lang.newRect(off1, off2, "BA", null, box1Props);

      off1 = new Offset(xPos + 5, yPos + 35, upperLeft,
          AnimalScript.DIRECTION_NW);
      off2 = new Offset(xPos + 25, yPos + 55, upperLeft,
          AnimalScript.DIRECTION_NW);
      boxDead = lang.newRect(off1, off2, "BIA", null, box2Props);

      off1 = new Offset(xPos + 5, yPos + 65, upperLeft,
          AnimalScript.DIRECTION_NW);
      off2 = new Offset(xPos + 25, yPos + 85, upperLeft,
          AnimalScript.DIRECTION_NW);
      boxNIter = lang.newRect(off1, off2, "BF", null, box3Props);

      // Texts
      off1 = new Offset(30, 10, "LF", AnimalScript.DIRECTION_NW);
      textAlive = lang.newText(off1, "lebendig", "TA", null);
      textAlive.setFont(fontCode, null, null);

      off1 = new Offset(30, 40, "LF", AnimalScript.DIRECTION_NW);
      textDead = lang.newText(off1, "tot", "TI", null);
      textDead.setFont(fontCode, null, null);

      off1 = new Offset(30, 70, "LF", AnimalScript.DIRECTION_NW);
      textNIter = lang.newText(off1, "nicht berechnet", "TF", null);
      textNIter.setFont(fontCode, null, null);

      hideList.add(frame);
      hideList.add(boxAlive);
      hideList.add(boxDead);
      hideList.add(boxNIter);
      hideList.add(textAlive);
      hideList.add(textDead);
      hideList.add(textNIter);
    }
  }

  /**
   * Creates and initializes a new SourceProperties Object
   * 
   * @param font
   *          - the text font
   * @param highlight
   *          - the text highlight property
   * @param colorproperty
   *          - the general font color property
   * @return new object of SourceProperties
   */
  private SourceCodeProperties createSourceCodeProperties(Font font,
      Color highlight, Color colorproperty) {

    SourceCodeProperties scProps = new SourceCodeProperties();

    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlight);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorproperty);

    return scProps;
  }

  /**
   * Shows Text objects and adds them to a hide-list, so they can easily hidden
   * after their presentation
   * 
   * @param content
   *          - a String array containing the text for the content of a slide
   * @param xInit
   *          - The initial x-coordinate
   * @param yInit
   *          - The initial y-coordinate
   * @param yOffset
   *          - The y-value for distance between text-objects
   * @param upperLeft
   *          - Reference of an primitive contained in Language
   * @param newReferenceName
   *          - the name of the new text objects
   * @param fontContent
   *          - the font of the content
   * @param interval
   *          - every interval iterations a nextStep() will occur
   * @param suppressed
   *          - an array containing iterations where a nextStep() shall not
   *          occur
   * @param modifier
   *          - for every suppressed iteration you may modify the distance
   *          between the current iterated text-objects
   * @return - a vector containing candidates for hiding
   */
  private Vector<Primitive> showText(String[] content, int xInit, int yInit,
      int yOffset, String upperLeft, String newReferenceName, Font fontContent,
      Integer interval, Integer[] suppressed, Integer modifier) {

    Vector<Primitive> hideList = new Vector<Primitive>(content.length);

    int i = 0; // counts iterations
    int rc = 0; // reference modifier
    int x = xInit;
    int y = yInit;
    boolean modified = false;

    // Check existing objects and determine new name
    while (lang.isNameUsed(newReferenceName + rc)) {

      rc++;
    }

    // Show animation
    Text text;
    Offset offset;
    for (String s : content) {

      offset = new Offset(x, y, upperLeft, AnimalScript.DIRECTION_NW);
      text = lang.newText(offset, s, newReferenceName + rc++, null);
      text.setFont(fontContent, null, null);

      if (nextStepExpected(i, interval, suppressed) && i != content.length - 1) {
        lang.nextStep();
      }

      // Build hide-list
      hideList.add(text);

      if (modified) {
        y = y + modifier;
        modified = false;
      }

      if (suppressed != null) {
        for (Integer sup : suppressed) {
          if (sup.equals(i)) {
            modified = true;
            y = y + modifier;
            break;
          }
        }
      }
      i++;
      y = y + yOffset;
    }

    return hideList;
  }

  /**
   * Determines if a nextStep() shall occur
   * 
   * @param i
   *          - the actual iteration
   * @param interval
   *          - every interval iterations a nextStep() shall occur...
   * @param suppressed
   *          - ...if there is no Exception
   * @return true if a nextStep shall occur
   */
  private boolean nextStepExpected(Integer i, Integer interval,
      Integer[] suppressed) {

    boolean next = false;

    // Check whether a nextStep() shall be executed or not
    if (interval != null) {

      // If there are iterations where an nextStep() shall not occur...
      if (suppressed != null) {

        // ... check if this iteration hits
        for (Integer sup : suppressed) {
          if (sup.equals(i)) {
            break;
          }
          // ... else check whether a nextStep() shall occur
          else {
            if (i % interval == 0) {
              next = true;
            }
          }
        }
      }
      // Else check if a nextStep() shall occur
      else {
        if (i % interval == 0) {
          next = true;
        }
      }
    }
    return next;
  }

  /**
   * Hides a given list of primitives
   * 
   * @param primitives
   *          - a list of Primitive elements
   */
  private void hide(List<Primitive> primitives) {

    lang.nextStep();

    for (Primitive p : primitives) {
      p.hide();
    }
  }

  // ONLY FOR FRAMEWORK
  public GameOfLive() {
  }

  // ONLY FOR FRAMEWORK
  public void init() {
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    lang = new AnimalScript("Conway's Game of Life", "Tobias Raffel", 800, 600);

    // Sort all input data
    Font fontTmp1 = (Font) primitives.get("Schriftart");
    String s1 = fontTmp1.getFontName();

    colorAlive = (Color) primitives.get("Farbe: lebendig");
    colorDead = (Color) primitives.get("Farbe: tot");
    colorNotCalculated = (Color) primitives.get("Farbe: nicht berechnet");
    Color codeHighlight = (Color) primitives.get("Farbe: Code (hervorgehoben)");

    sizeX = (Integer) primitives.get("Spielfeld Dim. x");
    sizeY = (Integer) primitives.get("Spielfeld Dim. y");

    iterations = (Integer) primitives.get("Iterationen");

    pattern = (int[][]) primitives.get("Muster");

    // Set stepMode
    lang.setStepMode(true);

    // Animation offset-anchor
    PointProperties pp = new PointProperties();
    Point p = lang.newPoint(new Coordinates(40, 40), "O", null, pp);
    p.hide();

    // The fonts
    fontContent = new Font(s1, Font.BOLD, 16);
    fontHeadline = new Font(s1, Font.BOLD, 30);
    fontTitle = new Font(s1, Font.BOLD, 20);
    fontCode = new Font("Monospaced", Font.PLAIN, 14);

    // The property-objects
    scProps = createSourceCodeProperties(fontCode, codeHighlight, Color.BLACK);

    cpOriginal = new RectProperties();
    cpOriginal.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cpOriginal.set(AnimationPropertiesKeys.FILL_PROPERTY, colorDead);
    cpOriginal.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    cpAnalysis = new RectProperties();
    cpAnalysis.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cpAnalysis.set(AnimationPropertiesKeys.FILL_PROPERTY, colorDead);
    cpAnalysis.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    cpDuplicate = new RectProperties();
    cpDuplicate.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cpDuplicate.set(AnimationPropertiesKeys.FILL_PROPERTY, colorNotCalculated);
    cpDuplicate.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    start();

    return lang.toString();
  }

  public String getName() {
    return "Conway's Game of Life";
  }

  public String getAlgorithmName() {
    return "Game of Life";
  }

  public String getAnimationAuthor() {
    return "Tobias Raffel";
  }

  public String getDescription() {
    return "Im Folgenden wird eine Implementierung des bekannten 'Game of Life' vorgestellt. "
        + "\n"
        + "\n"
        + "Dabei wird sich im Detail darauf beschraenkt zu veranschaulichen, "
        + "\n"
        + "wie ausgehend von einem Anfangsszenario, die direkt darauf folgende Generation berechnet wird."
        + "\n"
        + "\n"
        + "Sie koennen nun eine Animation betrachten/vorfuehren, die sich ueber mehrere Generationen erstreckt."
        + "\n"
        + "Das Anfangsszenario koennen sie durch setzen der Parameter 'Muster' manipulieren. "
        + "\n"
        + "Vergessen Sie auch nicht, die Groesse des Spielfeldes entsprechend zu setzen.";
  }

  public String getCodeExample() {
    return "";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  /**
   * Creates a new Instance of Conway's Game of Life
   * 
   * @param l
   *          - an AnimalScript reference
   */
  public GameOfLive(Language l, int[][] pattern, int iterations, int sizeX,
      int sizeY) {

    lang = l;
    this.pattern = pattern;
    this.sizeX = sizeX;
    this.sizeY = sizeY;
    this.iterations = iterations;

    myInit();
  }

  /**
   * For testing
   */
  private void myInit() {

    // Set stepMode
    lang.setStepMode(true);

    // Animation offset-anchor
    PointProperties pp = new PointProperties();
    Point p = lang.newPoint(new Coordinates(40, 40), "O", null, pp);
    p.hide();

    // The fonts
    fontContent = new Font("Monospaced", Font.BOLD, 16);
    fontCode = new Font("Monospaced", Font.PLAIN, 14);
    fontHeadline = new Font("Monospaced", Font.BOLD, 30);
    fontTitle = new Font("Monospaced", Font.BOLD, 20);

    colorAlive = Color.BLACK;
    colorDead = Color.WHITE;
    colorNotCalculated = Color.YELLOW;

    // The property-objects
    scProps = createSourceCodeProperties(fontCode, Color.RED, Color.BLACK);

    cpOriginal = new RectProperties();
    cpOriginal.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cpOriginal.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    cpOriginal.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    cpAnalysis = new RectProperties();
    cpAnalysis.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cpAnalysis.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    cpAnalysis.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    cpDuplicate = new RectProperties();
    cpDuplicate.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cpDuplicate.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    cpDuplicate.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

  }

  /**
   * For testing
   * 
   * @param args
   *          - blank
   */
  public static void main(String[] args) {

    Language l = new AnimalScript("Conway's Game of Life", "Tobias Raffel",
        800, 600);

    int[][] pattern = { { 0, 1, 0, 0, 0, 0 }, { 0, 0, 1, 0, 0, 0 },
        { 1, 1, 1, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0 } };
    GameOfLive game = new GameOfLive(l, pattern, 6, 6, 6);
    game.start();

    // Write file
    l.writeFile("GameOfLife.asu");
  }
}
