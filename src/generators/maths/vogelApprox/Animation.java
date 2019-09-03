package generators.maths.vogelApprox;

import generators.maths.vogelApprox.views.CodeView;
import generators.maths.vogelApprox.views.GridView;

import java.awt.Color;
import java.awt.Font;

import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class Animation {

  protected Language        myAnimationScript;
  private DrawingUtils      myDrawingUtils;

  private MatrixElement[]   supply;                                                                                                  // Angebot
  private MatrixElement[]   demand;                                                                                                  // Nachfrage

  private MatrixElement[][] tableau;                                                                                                 // Transporttableau
  private MatrixElement[][] cost;                                                                                                    // Kostenmatrix
  private MatrixElement[]   costDifColumn;                                                                                           // Kosten-Differenz-Spalte
                                                                                                                                      // (unten)
  private MatrixElement[]   costDifRow;                                                                                              // Kosten-Differenz-Zeile
                                                                                                                                      // (rechts)

  private CodeView          myCodeViewHandler;
  private GridView          myGridViewHandler;

  Text                      header;
  Rect                      hRect;

  private static String     TXT_ANIMATION_TITLE = "Vogelsche Approximationsmethode";
  private static String     TXT_INTRO           = "Die Vogelsche Approximationsmethode ist ein heuristisches Verfahren aus dem Bereich<br>"
                                                    + "des Operations Research zur Loesung eines Transportproblems.<br>"
                                                    + "Diese Methode zeichnet sich dadurch aus, dass sie dem Optimum schon sehr nahe kommt.<br>"
                                                    + "Der Aufwand ist allerdings gegenueber anderen Methoden, wie z. B. dem<br>"
                                                    + "Nord-West-Ecken-Verfahren oder dem Matrixminimumverfahren, vergleichsweise hoch.<br><br>"
                                                    + "Quelle: http://de.wikipedia.org/wiki/Vogelsche_Approximationsmethode<br><br>";

  // private static String TXT_LASTFRAME =
  // "Durch die Vogelsche Approximationsmethode wurde eine Basislösung erzeugt. <br>"
  // +
  // "Aufbauend auf dieser Lösung kann z.B. der Simplex-Algorithmus angewendet werden.";

  public Animation(MatrixElement[] supply, MatrixElement[] demand,
      MatrixElement[][] tableau, MatrixElement[][] cost,
      MatrixElement[] costDifColumn, MatrixElement[] costDifRow,
      Language animationScript) {

    // Store the language object
    myAnimationScript = animationScript;
    // This initializes the step mode. Each pair of subsequent steps has to be
    // divided by a call of lang.nextStep();
    myAnimationScript.setStepMode(true);
    myAnimationScript
        .setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    this.supply = supply;
    this.demand = demand;
    this.tableau = tableau;
    this.cost = cost;
    this.costDifColumn = costDifColumn;
    this.costDifRow = costDifRow;

    // Initialize drawing utilities.
    myDrawingUtils = new DrawingUtils(myAnimationScript);

    // Initialize all view handlers for the animation
    myCodeViewHandler = new CodeView(myAnimationScript, myDrawingUtils);
    myGridViewHandler = new GridView(myAnimationScript, myDrawingUtils);

    buildIntroFrame();
  }

  public void buildIntroFrame() {

    myAnimationScript.nextStep("Einleitung");
    // Build Title and introduction text
    myDrawingUtils.drawHeader(new Coordinates(5, 20), TXT_ANIMATION_TITLE);
    myDrawingUtils.buildText(new Coordinates(100, 100), TXT_INTRO);

  }

  public void buildDefaultViews() {
    myAnimationScript.nextStep("Initialisierung");
    myAnimationScript.hideAllPrimitives();

    myDrawingUtils.drawHeader(new Coordinates(5, 20), TXT_ANIMATION_TITLE);
    myCodeViewHandler.setupView();
    myGridViewHandler.setupView(supply, demand, tableau, cost);

  }

  public void refreshValues() {
    myGridViewHandler.refreshValues();
    myAnimationScript.nextStep();
  }

  public void buildExceptionFrame(String message) {

    // setup the error page with the description
    myAnimationScript.nextStep();
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    myAnimationScript.newText(new Coordinates(10, 100), message,
        "description1", null, textProps);
  }

  public Language getMyAnimationScript() {
    return myAnimationScript;
  }

  /**
   * Cost-Differenz Animation
   */
  int counter = 1;

  public void animationLine0(int x, int y, int y2) {
    myGridViewHandler.getCostGrid().highlightCell(x, y, Color.BLUE, 0);
    myGridViewHandler.getCostGrid().highlightCell(x, y2, Color.MAGENTA, 0);
    myGridViewHandler.getCostDifColumnGrid()
        .highlightCell(x, 0, Color.GREEN, 0);
    myGridViewHandler.getCostDifColumnGrid().setLabel(x, 0,
        Integer.toString(costDifColumn[x].getValue()));
    myAnimationScript.nextStep(counter + ". Iteration");
    counter++;
    myGridViewHandler.getCostGrid().unhighlightCell(x, y, 0);
    myGridViewHandler.getCostGrid().unhighlightCell(x, y2, 0);
    myGridViewHandler.getCostDifColumnGrid().unhighlightCell(x, 0, 0);

  }

  /**
   * Cost-Differenz Animation
   */
  public void animationLine1(int y, int x, int x2) {
    myGridViewHandler.getCostGrid().highlightCell(x, y, Color.BLUE, 0);
    myGridViewHandler.getCostGrid().highlightCell(x2, y, Color.MAGENTA, 0);
    myGridViewHandler.getCostDifRowGrid().highlightCell(0, y, Color.GREEN, 0);
    myGridViewHandler.getCostDifRowGrid().setLabel(0, y,
        Integer.toString(costDifRow[y].getValue()));
    myAnimationScript.nextStep();
    myGridViewHandler.getCostGrid().unhighlightCell(x, y, 0);
    myGridViewHandler.getCostGrid().unhighlightCell(x2, y, 0);
    myGridViewHandler.getCostDifRowGrid().unhighlightCell(0, y, 0);
  }

  public void animationLine2(int y, int column, int row) {
    myGridViewHandler.getCostDifColumnGrid().blinkCell(y, 0, Color.BLUE, 0);
    myAnimationScript.nextStep();
    for (int i = 0; i < cost.length; i++) {
      if (cost[i][y].isActive()) {
        myGridViewHandler.getCostGrid().highlightCell(y, i, Color.GREEN, 0);
      }
    }
    myAnimationScript.nextStep();
    myGridViewHandler.getCostGrid().blinkCell(column, row, Color.BLUE, 0);
    myGridViewHandler.getSupplyGrid().blinkCell(0, row, Color.BLUE, 0);
    myGridViewHandler.getDemandGrid().blinkCell(column, 0, Color.BLUE, 0);
    codeLine2();
    myAnimationScript.nextStep();
    myGridViewHandler.getSupplyGrid().highlightCell(0, row, Color.BLUE, 0);
    myGridViewHandler.getDemandGrid().highlightCell(column, 0, Color.BLUE, 0);
    for (int i = 0; i < cost.length; i++) {
      if (cost[i][y].isActive()) {
        myGridViewHandler.getCostGrid().unhighlightCell(y, i, 0);
      }
    }
  }

  public void animationLine3(int x, int column, int row) {
    myGridViewHandler.getCostDifRowGrid().blinkCell(0, x, Color.BLUE, 0);
    myAnimationScript.nextStep();
    for (int i = 0; i < cost[x].length; i++) {
      if (cost[x][i].isActive()) {
        myGridViewHandler.getCostGrid().highlightCell(i, x, Color.GREEN, 0);
      }
    }
    myAnimationScript.nextStep();
    myGridViewHandler.getCostGrid().blinkCell(column, row, Color.BLUE, 0);
    myGridViewHandler.getSupplyGrid().blinkCell(0, row, Color.BLUE, 0);
    myGridViewHandler.getDemandGrid().blinkCell(column, 0, Color.BLUE, 0);
    codeLine2();
    myAnimationScript.nextStep();
    myGridViewHandler.getSupplyGrid().highlightCell(0, row, Color.BLUE, 0);
    myGridViewHandler.getDemandGrid().highlightCell(column, 0, Color.BLUE, 0);
    for (int i = 0; i < cost[x].length; i++) {
      if (cost[x][i].isActive()) {
        myGridViewHandler.getCostGrid().unhighlightCell(i, x, 0);
      }
    }
  }

  public void animationLine6(int x, int y) {
    myGridViewHandler.getTableauGrid().setLabel(y, x,
        Integer.toString(tableau[x][y].getValue()));
    myAnimationScript.nextStep();
    myGridViewHandler.getSupplyGrid().blinkCell(0, x, Color.YELLOW, 0);
    myGridViewHandler.getDemandGrid().blinkCell(y, 0, Color.YELLOW, 0);
    codeLine4();
    myAnimationScript.nextStep();
    myGridViewHandler.refreshDemand();
    myGridViewHandler.refreshSupply();
    myAnimationScript.nextStep();
  }

  public void animationLine4(int y) {
    myGridViewHandler.getCostGrid().highlightColumn(y, Color.RED, 0);
    myGridViewHandler.getSupplyGrid().unhighlightAll(0);
    myGridViewHandler.getDemandGrid().unhighlightAll(0);
    myGridViewHandler.getDemandGrid().highlightCell(y, 0, Color.RED, 0);
    myAnimationScript.nextStep();
    myGridViewHandler.getDemandGrid().unhighlightCell(y, 0, 0);
  }

  public void animationLine5(int x) {
    myGridViewHandler.getCostGrid().highlightRow(x, Color.RED, 0);
    myGridViewHandler.getSupplyGrid().unhighlightAll(0);
    myGridViewHandler.getDemandGrid().unhighlightAll(0);
    myGridViewHandler.getSupplyGrid().highlightCell(0, x, Color.RED, 0);
    myAnimationScript.nextStep();
    myGridViewHandler.getSupplyGrid().unhighlightCell(0, x, 0);
  }

  public void animationLine7(int x, int y) {
    myGridViewHandler.getSupplyGrid().blinkCell(0, x, Color.YELLOW, 0);
    myGridViewHandler.getDemandGrid().blinkCell(y, 0, Color.YELLOW, 0);
  }

  public void animationLine8() {
    myAnimationScript.nextStep();
    myGridViewHandler.refreshValues();
    myAnimationScript.nextStep();
    myGridViewHandler.getSupplyGrid().unhighlightAll(0);
    myGridViewHandler.getDemandGrid().unhighlightAll(0);

  }

  public void codeLine0() {
    myCodeViewHandler.highlight(0, 2);
  }

  public void codeLine1() {
    myCodeViewHandler.highlight(3, 4);
  }

  public void codeLine2() {
    myCodeViewHandler.highlight(5, 7);
  }

  public void codeLine3() {
    myCodeViewHandler.highlight(8, 9);
  }

  public void codeLine4() {
    myCodeViewHandler.highlight(10, 11);
  }

  public void codeLine5() {
    myCodeViewHandler.highlight(12, 15);
  }

  public void codeLine6() {
    myCodeViewHandler.highlight(16, 17);
    myAnimationScript.nextStep();
  }

  public void codeLine7() {
    myCodeViewHandler.highlight(18, 19);
  }
}
