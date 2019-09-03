package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class ModiMethod implements Generator {

  private Language             lang;
  // Quantity of supply.
  private int[]                supply = { 10, 8, 7 };
  // Quantity of demand.
  private int[]                demand = { 6, 5, 8, 6 };
  // Matrix with costs for each supplier demander combination.
  private int[][]              costs  = { { 7, 2, 4, 7 }, { 9, 5, 3, 3 },
      { 7, 7, 6, 4 }                 };
  // View Elements
  private Text                 costHeading;
  private Text                 eqnHeading;
  private Text                 rcHeading;
  private Text                 totalCost;
  private Text                 deltaLabel;
  private Text[]               equations;
  private Text[]               rcEquations;
  private SourceCode           sc;
  private SourceCode           explanation;
  private SourceCode           conclusion;
  private SourceCode           description;
  private StringMatrix         viewTableau;
  private IntMatrix            costMatrix;
  private Rect                 descrRect;
  // View Properties
  private SourceCodeProperties sourceCodeProps;
  private SourceCodeProperties textProps;
  private MatrixProperties     tableauProps;
  private TextProperties       labelProps;

  /**
   * ####################################################################
   * Initialisation
   * ####################################################################
   */
  @Override
  public void init() {
    lang = new AnimalScript("MODI-Method",
        "Daniel Burgmann, Matthias Horn, Torben Stoffer", 800, 600);
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    costHeading = null;
    eqnHeading = null;
    rcHeading = null;
    totalCost = null;
    deltaLabel = null;
    equations = null;
    rcEquations = null;
    sc = null;
    explanation = null;
    conclusion = null;
    description = null;
    viewTableau = null;
    costMatrix = null;
    descrRect = null;
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    if (props != null) { // needed vor mainMethod to work
      sourceCodeProps = (SourceCodeProperties) props
          .getPropertiesByName("sourceCodeProps");
      textProps = (SourceCodeProperties) props.getPropertiesByName("textProps");
      tableauProps = (MatrixProperties) props
          .getPropertiesByName("tableauProps");
      labelProps = (TextProperties) props.getPropertiesByName("labelProps");
      supply = (int[]) primitives.get("supply");
      costs = (int[][]) primitives.get("costs");
      demand = (int[]) primitives.get("demand");
    }

    displayHeader();

    // check input
    String error = checkInput();

    if (error != null) {
      // display error and stop run
      displayError(error);

      return lang.toString();
    }

    displayDescription();
    lang.nextStep();
    explanation.hide();
    run();
    displayConclusion();
    lang.nextStep();

    lang.finalizeGeneration();
    return lang.toString()
        .replaceAll("style table", "style table cellWidth 40 cellHeight 25")
        .replaceAll("refresh", "");
  }

  /**
   * ####################################################################
   * Algorithm functions
   * ####################################################################
   */
  private void run() {
    int iteration = 0;

    // variable for basis
    List<Point> basis = new LinkedList<Point>();

    // northwest corner rule
    int[][] tableau = northWestCornerRule(basis);

    displayTableau(tableau, null, null, null);
    displayCode();
    displayVarDescription();

    sc.highlight(0);
    lang.nextStep();

    // get non basis & basis variables
    List<Point> nonBasis = getNonBasis(basis);

    // ask about bv
    FillInBlanksQuestionModel bvQ = new FillInBlanksQuestionModel("bv");
    bvQ.setPrompt("How many basis variables do we have?");
    bvQ.addAnswer(String.valueOf(basis.size()), 1,
        "Correct! All values greater 0 in the tableau are basis variables.");
    lang.addFIBQuestion(bvQ);
    lang.nextStep();

    displayTableau(tableau, basis, null, null); // show inital tableau
    displayTotalCost(calcCosts(tableau, basis));// show cost of inital
    // tableau
    sc.unhighlight(0);
    sc.highlight(1);
    sc.highlight(2);
    lang.nextStep();

    while (true) {
      // calculate us and vs
      List<Integer> us = new LinkedList<Integer>();
      List<Integer> vs = new LinkedList<Integer>();

      calcDualVariables(basis, us, vs);
      displayVarBasedEquationSystem(basis);
      displayCostMatrix();
      sc.unhighlight(1);
      sc.unhighlight(2);
      sc.highlight(3);
      lang.nextStep("Start iteration " + ++iteration);
      displaySolvedEquationSystem(basis, us, vs);
      displayTableau(tableau, basis, us, vs);
      sc.unhighlight(3);
      sc.highlight(4);
      lang.nextStep();

      // calculate reduced costs
      int[][] oldTableau = tableau.clone();
      tableau = calcReducedCosts(tableau, basis, nonBasis, us, vs);
      displayVarBasedRcCalculation(nonBasis);

      sc.unhighlight(4);
      sc.highlight(5);
      displayVarBasedRcCalculation(nonBasis);
      lang.nextStep();
      displayRcCalculation(oldTableau, tableau, nonBasis, us, vs);
      displayTableau(tableau, basis, us, vs);
      lang.nextStep();

      // get minimal non basis variable
      Point min = getMinReducedCosts(tableau, nonBasis);

      sc.unhighlight(5);
      sc.highlight(6);
      lang.nextStep("Decide if optimal solution is found in iteration "
          + iteration);

      if (tableau[min.getRow()][min.getCol()] >= 0) {
        FillInBlanksQuestionModel finishedQ = new FillInBlanksQuestionModel(
            "finished");
        finishedQ.setPrompt("To which step do we need to go now?");
        finishedQ.addAnswer(String.valueOf(7), 1,
            "Correct! All reduced costs are greater than zero.");
        lang.addFIBQuestion(finishedQ);
        lang.nextStep();

        sc.unhighlight(6);
        sc.highlight(7);
        lang.nextStep();
        break;
      }

      // highlight next step
      sc.unhighlight(6);
      sc.highlight(8);
      lang.nextStep("Find new basis variable in iteration " + iteration);

      // Highlight minimal reduced Costs (new Basis var)
      sc.unhighlight(8);
      sc.highlight(9);
      viewTableau.highlightCell(min.getRow() + 1, min.getCol() + 1,
          Timing.INSTANTEOUS, Timing.INSTANTEOUS);
      lang.nextStep();

      // get circle
      List<Point> circle = findCircle(min, basis);
      sc.unhighlight(9);
      sc.highlight(10);
      displayCircle(circle);
      lang.nextStep();

      sc.unhighlight(10);
      sc.highlight(11);
      displayCircleIncDec(circle);
      lang.nextStep();

      // get maximal increase
      Point max = getMaxIncrease(tableau, circle);
      int delta = tableau[max.getRow()][max.getCol()];

      if (iteration == 2) { // Ask Question about Delta in second
        // iteration
        FillInBlanksQuestionModel deltaQ = new FillInBlanksQuestionModel(
            "delta");
        deltaQ.setPrompt("What is the value of delta?");
        deltaQ
            .addAnswer(
                String.valueOf(delta),
                1,
                "Correct - Delta is always the smallest value that has a minus in the tableau. ");
        lang.addFIBQuestion(deltaQ);
        lang.nextStep();
      }

      // Mark Delta
      String elem = viewTableau.getElement(max.getRow() + 1, max.getCol() + 1);
      viewTableau.put(max.getRow() + 1, max.getCol() + 1,
          ">" + elem.substring(0, elem.length() - 1) + "<", Timing.INSTANTEOUS,
          Timing.INSTANTEOUS);

      sc.unhighlight(11);
      sc.highlight(12);
      displayDelta(delta);
      lang.nextStep();
      this.deltaLabel.hide();

      // calculate new tableau
      calcNewTableau(tableau, circle, nonBasis, delta);
      sc.unhighlight(12);
      sc.highlight(13);
      displayTableau(tableau, basis, us, vs);
      // Mark leaving element
      viewTableau.put(max.getRow() + 1, max.getCol() + 1,
          ">" + viewTableau.getElement(max.getRow() + 1, max.getCol() + 1)
              + "<", Timing.INSTANTEOUS, Timing.INSTANTEOUS);

      viewTableau.highlightCell(min.getRow() + 1, min.getCol() + 1,
          Timing.INSTANTEOUS, Timing.INSTANTEOUS); // rehighlight min
      lang.nextStep();

      // change basis and non basis
      basis.remove(max);
      nonBasis.add(max);
      nonBasis.remove(min);
      basis.add(min);
      sc.unhighlight(13);
      sc.highlight(14);

      displayTableau(tableau, basis, us, vs);
      // highlighting
      // von variable
      // leaving basis
      lang.nextStep();

      // calculate costs
      int cost = calcCosts(tableau, basis);
      // highlight last step
      sc.unhighlight(14);
      sc.highlight(15);
      displayTotalCost(cost);
      lang.nextStep();

      // remove highlighting
      sc.unhighlight(15);
    }
  }

  /**
   * Perform the North-West-Corner-Rule to get a start tableau for the
   * MODI-method.
   * 
   * @param basis
   *          list to add basis to
   * @return start tableau after use of North-West-Corner-Rule
   */
  private int[][] northWestCornerRule(List<Point> basis) {
    // create tableau array filled with zeros
    int[][] tableau = new int[supply.length][demand.length];

    // create arrays with left supply and demand
    int[] supplyLeft = supply.clone();
    int[] demandLeft = demand.clone();

    // iterate over tableau till end of tableau is reached
    for (int row = 0, col = 0; row < supply.length && col < demand.length;) {
      int quantity = 0;

      // check if left quantity of supply or demand is smaller
      if (supplyLeft[row] <= demandLeft[col]) {
        quantity = supplyLeft[row];
      } else {
        quantity = demandLeft[col];
      }

      // add quantity to tableau and subtract from supply and demand
      tableau[row][col] += quantity;
      supplyLeft[row] -= quantity;
      demandLeft[col] -= quantity;

      // add point to basis
      basis.add(new Point(row, col));

      // increase row if supply is satisfied
      if (supplyLeft[row] == 0) {
        row++;
      } // increase column if supply is satisfied
      else if (demandLeft[col] == 0) {
        col++;
      }
    }

    return tableau;
  }

  /**
   * Get non basis variables.
   * 
   * @param basis
   *          list with basis points
   * @return list with non basis points
   */
  private List<Point> getNonBasis(List<Point> basis) {
    // variable for non basis
    List<Point> nonBasis = new LinkedList<Point>();

    // iterate over whole tableau
    for (int row = 0; row < supply.length; row++) {
      for (int col = 0; col < demand.length; col++) {
        Point p = new Point(row, col);

        // check if point is not in basis
        if (!basis.contains(p)) // add to non basis variables
        {
          nonBasis.add(p);
        }
      }
    }

    return nonBasis;
  }

  /**
   * Calculate tableau with reduced costs.
   * 
   * @param tableau
   *          current tableau matrix
   * @param basis
   *          current basis variables
   * @param nonBasis
   *          current non-basis variables
   * @param us
   *          dual variables for supply
   * @param vs
   *          dual variables for demand
   * @return tableau containing the calculated reduced costs
   */
  private int[][] calcReducedCosts(int[][] tableau, List<Point> basis,
      List<Point> nonBasis, List<Integer> us, List<Integer> vs) {
    // iterate over non basis to calculate reduced costs
    for (Point p : nonBasis) {
      int row = p.getRow();
      int col = p.getCol();

      // calculate reduced costs
      tableau[row][col] = costs[row][col] - us.get(row) - vs.get(col);
    }

    // System.out.println("Tableau containing reduced costs:");
    // printMatrix(tableau, basis, null);

    return tableau;
  }

  /**
   * Calculate dual variables.
   * 
   * @param basis
   *          current basis variables
   * @param us
   *          list to add dual variables for supply to
   * @param vs
   *          list to add dual variables for demand to
   */
  private void calcDualVariables(List<Point> basis, List<Integer> us,
      List<Integer> vs) {
    int size = basis.size();

    // create matrix for coefficients without u1 (is set to zero)
    double[][] arrCoefficients = new double[size][demand.length + supply.length
        - 1];

    // create array for constants (costs)
    double[] arrConstants = new double[size];

    // iterate over basis
    for (int i = 0; i < basis.size(); i++) {
      // get point and corresponding row and column
      Point p = basis.get(i);
      int row = p.getRow();
      int col = p.getCol();

      // skip u1 (is set to zero)
      if (row > 0) // set coefficient in corresponding column to 1
      {
        arrCoefficients[i][row - 1] = 1;
      }

      // set coefficient in corresponding column to 1
      arrCoefficients[i][supply.length - 1 + col] = 1;

      // set costs as constant
      arrConstants[i] = costs[row][col];
    }

    // System.out.println("System of linear equations:");
    // printMatrix(arrCoefficients, null, null);

    // create and solve system of linear equations
    RealMatrix objCoefficients = new Array2DRowRealMatrix(arrCoefficients,
        false);
    DecompositionSolver solver = new LUDecomposition(objCoefficients)
        .getSolver();
    RealVector objConstants = new ArrayRealVector(arrConstants, false);
    RealVector solution = solver.solve(objConstants);

    // add u1 (is set to zero)
    us.add(0);

    // get u2 till uN
    for (int i = 0; i < supply.length - 1; i++) {
      us.add((int) solution.getEntry(i));
    }

    // get v1 till vN
    for (int i = supply.length - 1; i < basis.size(); i++) {
      vs.add((int) solution.getEntry(i));
    }

    // System.out.println("u:"+ us);
    // System.out.println();
    //
    // System.out.println("v:"+ vs);
    // System.out.println();
  }

  /**
   * Get smallest reduced costs.
   * 
   * @param tableau
   *          current tableau matrix
   * @param nonBasis
   *          non-basis variables to use for search
   * @return point with smallest reduced costs
   */
  private Point getMinReducedCosts(int[][] tableau, List<Point> nonBasis) {
    Point min = nonBasis.get(0);

    // iterate over non basis
    for (Point p : nonBasis) // check if new minimum is found
    {
      if (tableau[p.getRow()][p.getCol()] < tableau[min.getRow()][min.getCol()]) {
        min = p;
      }
    }

    return min;
  }

  /**
   * Start finding of circle in new and current basis variables.
   * 
   * @param startPoint
   *          new basis variable and first point of circle
   * @param basis
   *          current basis
   * @return list with points in circle
   */
  private List<Point> findCircle(Point startPoint, List<Point> basis) {
    // try to find circle horizontally first
    List<Point> circle = findCircle(startPoint, startPoint, basis,
        Direction.HORIZONTAL);

    if (circle != null) {
      return circle;
    }

    // otherwise find circle vertically
    circle = findCircle(startPoint, startPoint, basis, Direction.VERTICAL);

    return circle;
  }

  /**
   * Find Circle by use of backtracking.
   * 
   * @param endPoint
   *          new basis point
   * @param currentPoint
   *          current point to continue search from
   * @param oldBasis
   *          old basis variables
   * @param direction
   *          direction to continue search
   * @return list with points to end point or null if no circle exists
   */
  private List<Point> findCircle(Point endPoint, Point currentPoint,
      List<Point> oldBasis, Direction direction) {
    List<Point> circle = null;

    // check if end point is in same row/column and finish search
    if (!endPoint.equals(currentPoint)
        && ((direction == Direction.HORIZONTAL && endPoint.getRow() == currentPoint
            .getRow()) || (direction == Direction.VERTICAL && endPoint.getCol() == currentPoint
            .getCol()))) {
      circle = new LinkedList<Point>();
      circle.add(currentPoint);

      return circle;
    }

    int pos = 0;

    do {
      // iterate over remaining basis and search for points in same
      // row/column
      for (; pos < oldBasis.size()
          && ((direction == Direction.HORIZONTAL && oldBasis.get(pos).getRow() != currentPoint
              .getRow()) || (direction == Direction.VERTICAL && oldBasis.get(
              pos).getCol() != currentPoint.getCol())); pos++)
        ;

      // return null if all points are processed and no one lead to a
      // solution
      if (pos >= oldBasis.size()) {
        return null;
      }

      Point nextPoint = oldBasis.get(pos);

      // remove next point from basis
      List<Point> newBasis = new LinkedList<Point>(oldBasis);
      newBasis.remove(nextPoint);

      // try to find circle starting at next point
      circle = findCircle(endPoint, nextPoint, newBasis,
          direction == Direction.HORIZONTAL ? Direction.VERTICAL
              : Direction.HORIZONTAL);

      // repeat till circle is found
    } while (circle == null);

    // add current point as first point to circle
    circle.add(0, currentPoint);

    return circle;
  }

  /**
   * Find point with the smallest amount of increase which has to leave the
   * basis.
   * 
   * @param tableau
   *          current tableau matrix
   * @param circle
   *          list with circle points
   * @return point which has to leave the basis
   */
  private Point getMaxIncrease(int[][] tableau, List<Point> circle) {
    Point max = circle.get(1);

    // iterate over every second point in circle (with negative sign)
    for (int i = 1; i < circle.size(); i += 2) {
      Point p = circle.get(i);

      // check if new maximum is found
      if (tableau[p.getRow()][p.getCol()] < tableau[max.getRow()][max.getCol()]) {
        max = p;
      }
    }

    return max;
  }

  /**
   * Calculate new tableau.
   * 
   * @param tableau
   *          current tableau matrix
   * @param circle
   *          list with circle points to recalculate amounts of
   * @param nonBasis
   *          non-basis variables to set amounts to zero
   * @param delta
   *          amount of increase
   */
  private void calcNewTableau(int[][] tableau, List<Point> circle,
      List<Point> nonBasis, int delta) {
    // set values of non basis to zero
    for (Point p : nonBasis) {
      tableau[p.getRow()][p.getCol()] = 0;
    }

    // iterate over whole circle
    for (int i = 0; i < circle.size(); i++) {
      Point p = circle.get(i);
      int row = p.getRow();
      int col = p.getCol();

      // add/subtract delta
      if (i % 2 == 0) {
        tableau[row][col] += delta;
      } else {
        tableau[row][col] -= delta;
      }
    }
  }

  /**
   * Calculate costs.
   * 
   * @param tableau
   *          current tableau matrix
   * @param basis
   *          basis variables to consider for calculation
   * @return calculated costs
   */
  private int calcCosts(int[][] tableau, List<Point> basis) {
    int cost = 0;

    // iterate over whole basis
    for (Point p : basis) {
      int row = p.getRow();
      int col = p.getCol();

      // multiply costs by quantity
      cost += costs[row][col] * tableau[row][col];
    }

    return cost;
  }

  /**
   * ####################################################################
   * Display Functions
   * ####################################################################
   */
  /**
   * Display given error message.
   * 
   * @param message
   *          error message to display
   */
  private void displayError(String message) {
    SourceCode error = lang.newSourceCode(new Offset(0, 30, "header",
        AnimalScript.DIRECTION_SW), "error", null, textProps);
    error.addCodeLine(message, null, 0, null);
  }

  /**
   * Display explanation on first page.
   */
  private void displayDescription() {
    explanation = lang.newSourceCode(new Offset(0, 30, "header",
        AnimalScript.DIRECTION_SW), "explanation", null, textProps);
    explanation.addCodeLine(
        "The MODI-method (Modified Distribution method) is an", null, 0, null);
    explanation.addCodeLine(
        "iterative, numerical algorithm to solve the standard transportation",
        null, 0, null);
    explanation.addCodeLine("problem.", null, 0, null);
    explanation
        .addCodeLine(
            "At this problem a specific number of suppliers and demanders all with",
            null, 0, null);
    explanation.addCodeLine(
        "a specific amount of supply respectively demand for a specific", null,
        0, null);
    explanation.addCodeLine(
        "product and costs for the transportation between every supplier and",
        null, 0, null);
    explanation.addCodeLine(
        "demander are given. The goal is to find the solution with minimal",
        null, 0, null);
    explanation.addCodeLine(
        "transportation costs on condition that every demand is satisfied.",
        null, 0, null);
    explanation.addCodeLine(
        "To use the MODI-method a valid start tableau is necessary which is",
        null, 0, null);
    explanation
        .addCodeLine(
            "generated by use of a heuristic. In this case the North-West-Corner-Rule is used.",
            null, 0, null);
    explanation.addCodeLine(
        "This animation focusses on the MODI-Method itself, ", null, 0, null);
    explanation.addCodeLine(
        "the application of the North-West-Corner-Rule is not shown.", null, 0,
        null);

  }

  /**
   * Display the header title.
   */
  private void displayHeader() {
    // adding the heading
    TextProperties headerProperties = new TextProperties();
    headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    lang.newText(new Coordinates(20, 30), "The MODI-Method", "header", null,
        headerProperties);

    // adding the rectangle around the heading
    RectProperties headerRectProps = new RectProperties();
    headerRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    headerRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    headerRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", "SE"), "headerRect", null, headerRectProps);
  }

  /**
   * Display the pseudo code
   */
  private void displayCode() {
    sc = lang.newSourceCode(new Offset(50, 0, "tableau",
        AnimalScript.DIRECTION_NE), "pseudocode", null, sourceCodeProps);
    sc.addCodeLine("1) Start with valid tableau.", null, 0, null);

    sc.addCodeLine(
        "2) Consider every field in the main tableau with a value larger "
            + "than zero as a basis variable (highlighted) and all other fields ",
        null, 0, null);
    sc.addCodeLine("  as non-basis variables (not highlighted).", null, 0, null);

    sc.addCodeLine("3) Create a system of linear equations "
        + "with the following equation for every basis "
        + "variable (Xij): Ui + Vj = Cij", null, 0, null);

    sc.addCodeLine("4) Solve system of linear equations by setting "
        + "U0 to 0.", null, 0, null);

    sc.addCodeLine("5) Calculate reduced costs (RC) for all "
        + "non-basis variables: RCij = Cij - Ui - Vj", null, 0, null);

    sc.addCodeLine("6) Check if all reduced costs are larger than zero.", null,
        0, null);

    sc.addCodeLine(
        "7) All RC > 0 => Algorithm Finished: Optimal solution (= solution with min cost) is found.",
        null, 0, null);

    sc.addCodeLine("8) Any RC < 0 => Basis variables have to be changed.",
        null, 0, null);

    sc.addCodeLine("8.1) Non-basis variable with most negative value "
        + "becomes new basis variable ", null, 1, null);

    sc.addCodeLine("8.2) Emergence of a circle in tableau.", null, 1, null);

    sc.addCodeLine(
        "8.3) Redirection of values along the circle - Alternating increasing and decreasing",
        null, 1, null);

    sc.addCodeLine("8.3.1) Use smallest decreasing value as delta.", null, 2,
        null);

    sc.addCodeLine("8.3.2) Increase / Decrease "
        + "variables in circle by delta.", null, 2, null);

    sc.addCodeLine("8.3.3) At least one basis variable becomes zero "
        + "and leaves the basis (becomes a non-basis variable).", null, 2, null);
    sc.addCodeLine("10) Go to step 3.", null, 0, null);
  }

  /**
   * Displays variable description
   */
  private void displayVarDescription() {
    // show conclusion
    description = lang.newSourceCode(new Offset(5, 30, "pseudocode",
        AnimalScript.DIRECTION_SW), "varDescription", null, textProps);
    description.addCodeLine("Si  = Supply of the i-th supplier", null, 0, null);
    description.addCodeLine("Dj  = Demand of the j-th demander", null, 0, null);
    description.addCodeLine(
        "Ui  = Dualvariable corresponding to the i-th supplier", null, 0, null);
    description
        .addCodeLine("Vj  = Dualvariable corresponding to the j-th demmander",
            null, 0, null);
    description.addCodeLine(
        "Cij = transportation costs between i-th supplier and j-th demander",
        null, 0, null);

    // adding the rectangle around the heading
    RectProperties descrRectProps = new RectProperties();
    descrRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    descrRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    descrRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    descrRect = lang.newRect(new Offset(-5, -5, "varDescription",
        AnimalScript.DIRECTION_NW), new Offset(5, 5, "varDescription", "SE"),
        "descrRect", null, descrRectProps);
  }

  /**
   * Displays the Costmatrix
   */
  private void displayCostMatrix() {
    if (costMatrix == null) {
      costHeading = lang
          .newText(new Offset(60, 0, "eqnHeading", AnimalScript.DIRECTION_NE),
              "Costs:", "costHeading", null, labelProps);
      costMatrix = lang.newIntMatrix(new Offset(0, 10, "costHeading",
          AnimalScript.DIRECTION_SW), costs, "costMatrix", null, tableauProps);

    }
  }

  /**
   * Displays the equationsystem showing the variables
   * 
   * @param basis
   * @param us
   * @param vs
   */
  private void displayVarBasedEquationSystem(List<Point> basis) {
    // init on first call
    if (equations == null) {
      eqnHeading = lang.newText(new Offset(0, 30, "tableau",
          AnimalScript.DIRECTION_SW), "Equation System:", "eqnHeading", null,
          labelProps);
      equations = new Text[basis.size()];
      for (int i = 0; i < basis.size(); i++) {
        equations[i] = lang.newText(new Offset(0, 10 + i * 15, "eqnHeading",
            AnimalScript.DIRECTION_SW), "", "eq" + i, null, labelProps);
      }
    }

    // Show Variables
    int j = 0;
    StringBuilder text;
    for (Point basisVar : basis) {
      text = new StringBuilder();
      text.append("U").append(basisVar.row + 1).append("+ ").append("V")
          .append(basisVar.col + 1).append("= ").append("C")
          .append(basisVar.row + 1).append(basisVar.col + 1);

      equations[j].setText(text.toString(), Timing.INSTANTEOUS,
          Timing.INSTANTEOUS);
      j++;
    }

  }

  /**
   * Displays the solved linear equation system
   * 
   * @param basis
   *          list of basis variables
   * @param us
   *          list of us dual variables
   * @param vs
   *          list of vs dual variables
   */
  private void displaySolvedEquationSystem(List<Point> basis, List<Integer> us,
      List<Integer> vs) {
    StringBuilder text;

    // Show Values
    int j = 0;
    for (Point basisVar : basis) {
      text = new StringBuilder();
      text.append(us.get(basisVar.row)).append("+ ")
          .append(vs.get(basisVar.col)).append("= ")
          .append(costs[basisVar.row][basisVar.col]);

      equations[j].setText(text.toString(), Timing.INSTANTEOUS,
          Timing.INSTANTEOUS);
      j++;
    }
  }

  /**
   * Displays the calcution of Rc
   * 
   * @param basis
   * @param us
   * @param vs
   */
  private void displayVarBasedRcCalculation(List<Point> nonbasis) {
    // init on first call
    if (rcEquations == null) {
      rcHeading = lang.newText(new Offset(0, 30, "eq" + (equations.length - 1),
          AnimalScript.DIRECTION_SW), "Reduced Costs:", "rcHeading", null,
          labelProps);
      rcEquations = new Text[nonbasis.size()];
      for (int i = 0; i < nonbasis.size(); i++) {
        rcEquations[i] = lang.newText(new Offset(0, 10 + i * 15, "rcHeading",
            AnimalScript.DIRECTION_SW), "", "rc" + i, null, labelProps);
      }
    }

    // Show Variables
    int j = 0;
    StringBuilder text;
    for (Point var : nonbasis) {
      text = new StringBuilder();
      text.append("RC").append(var.row + 1).append(var.col + 1).append("- ")
          .append("U").append(var.row + 1).append("- ").append("V")
          .append(var.col + 1).append("= ").append("*RC").append(var.row + 1)
          .append(var.col + 1);

      rcEquations[j].setText(text.toString(), Timing.INSTANTEOUS,
          Timing.INSTANTEOUS);
      j++;
    }

  }

  /**
   * Displays the solved Rc calculation
   * 
   * @param basis
   *          list of basis variables
   * @param us
   *          list of us dual variables
   * @param vs
   *          list of vs dual variables
   */
  private void displayRcCalculation(int[][] oldTableau, int[][] newTableau,
      List<Point> nonbasis, List<Integer> us, List<Integer> vs) {
    StringBuilder text;
    // Show Values
    int j = 0;
    for (Point var : nonbasis) {
      text = new StringBuilder();
      text.append(oldTableau[var.row][var.col]).append("- ")
          .append(us.get(var.row)).append("- ").append(vs.get(var.col))
          .append("= ").append(newTableau[var.row][var.col]).append("=> ")
          .append("RC").append(var.row + 1).append(var.col + 1);

      rcEquations[j].setText(text.toString(), Timing.INSTANTEOUS,
          Timing.INSTANTEOUS);
      j++;
    }
  }

  /**
   * Displays the tableau
   * 
   * @param tableau
   *          the tableau
   * @param basis
   *          list of basis variables
   * @param us
   *          list of us dual variables
   * @param vs
   *          list of vs dual variables
   */
  private void displayTableau(int[][] tableau, List<Point> basis,
      List<Integer> us, List<Integer> vs) {

    // convert ints to strings
    String[][] strMatrix = intMatrixToStringMatrix(tableau);

    // check if tableau needs to be created first
    if (viewTableau == null) {
      // determine size -> size is bigger because of u's, v's, supply,
      // demand and header
      // rows/cols
      int sizex = strMatrix.length + 3;
      int sizey = strMatrix[0].length + 3;

      // create tableu
      viewTableau = lang.newStringMatrix(new Offset(0, 30, "header",
          AnimalScript.DIRECTION_SW), new String[sizex][sizey], "tableau",
          null, tableauProps);

      // write descriptions to tableau

      // set static description
      viewTableau.put(0, 0, "i/j", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
      viewTableau.put(0, viewTableau.getNrCols() - 2, "Ui", Timing.INSTANTEOUS,
          Timing.INSTANTEOUS);
      viewTableau.put(viewTableau.getNrRows() - 2, 0, "Vj", Timing.INSTANTEOUS,
          Timing.INSTANTEOUS);
      viewTableau.put(0, viewTableau.getNrCols() - 1, "Si", Timing.INSTANTEOUS,
          Timing.INSTANTEOUS);
      viewTableau.put(viewTableau.getNrRows() - 1, 0, "Dj", Timing.INSTANTEOUS,
          Timing.INSTANTEOUS);
      viewTableau.put(viewTableau.getNrRows() - 1, viewTableau.getNrCols() - 1,
          "", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
      viewTableau.put(viewTableau.getNrRows() - 2, viewTableau.getNrCols() - 2,
          "", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
      viewTableau.put(viewTableau.getNrRows() - 1, viewTableau.getNrCols() - 2,
          "", Timing.INSTANTEOUS, Timing.INSTANTEOUS);
      viewTableau.put(viewTableau.getNrRows() - 2, viewTableau.getNrCols() - 1,
          "", Timing.INSTANTEOUS, Timing.INSTANTEOUS);

      // add col counters
      for (int j = 1; j < viewTableau.getNrCols() - 2; j++) {
        viewTableau.put(0, j, String.valueOf(j), Timing.INSTANTEOUS,
            Timing.INSTANTEOUS);
      }

      // add row counters
      for (int i = 1; i < viewTableau.getNrRows() - 2; i++) {
        viewTableau.put(i, 0, String.valueOf(i), Timing.INSTANTEOUS,
            Timing.INSTANTEOUS);
      }

      // add demand values
      for (int j = 1; j < viewTableau.getNrCols() - 2; j++) {
        viewTableau.put(viewTableau.getNrRows() - 1, j,
            String.valueOf(demand[j - 1]), Timing.INSTANTEOUS,
            Timing.INSTANTEOUS);
      }

      // add supply values
      for (int i = 1; i < viewTableau.getNrRows() - 2; i++) {
        viewTableau.put(i, viewTableau.getNrCols() - 1,
            String.valueOf(supply[i - 1]), Timing.INSTANTEOUS,
            Timing.INSTANTEOUS);
      }
    }

    // update tableau values
    for (int i = 0; i < strMatrix.length; i++) {
      for (int j = 0; j < strMatrix[i].length; j++) {
        viewTableau.put(i + 1, j + 1, strMatrix[i][j], Timing.INSTANTEOUS,
            Timing.INSTANTEOUS);

        // Highlight Basiselements & unhighlight non basiselements
        if (basis != null && basis.contains(new Point(i, j))) {
          viewTableau.highlightElem(i + 1, j + 1, Timing.INSTANTEOUS,
              Timing.INSTANTEOUS);
        } else {
          viewTableau.unhighlightElem(i + 1, j + 1, Timing.INSTANTEOUS,
              Timing.INSTANTEOUS);
        }
      }
    }

    // update usif set otherwise fill with dashes
    if (us != null) {
      for (int i = 1; i < viewTableau.getNrRows() - 2; i++) {
        viewTableau.put(i, viewTableau.getNrCols() - 2,
            String.valueOf(us.get(i - 1)), Timing.INSTANTEOUS,
            Timing.INSTANTEOUS);
      }
    } else {
      for (int i = 1; i < viewTableau.getNrRows() - 2; i++) {
        viewTableau.put(i, viewTableau.getNrCols() - 2, "-",
            Timing.INSTANTEOUS, Timing.INSTANTEOUS);
      }
    }

    // update vs if set otherwise fill with dashes
    if (vs != null) {
      for (int j = 1; j < viewTableau.getNrCols() - 2; j++) {
        viewTableau.put(viewTableau.getNrRows() - 2, j,
            String.valueOf(vs.get(j - 1)), Timing.INSTANTEOUS,
            Timing.INSTANTEOUS);
      }
    } else {
      for (int j = 1; j < viewTableau.getNrCols() - 2; j++) {
        viewTableau.put(viewTableau.getNrRows() - 2, j, "-",
            Timing.INSTANTEOUS, Timing.INSTANTEOUS);
      }
    }
  }

  /**
   * Displays the circle in the view tableau
   * 
   * @param circle
   */
  private void displayCircle(List<Point> circle) {
    for (Point p : circle) {
      viewTableau.put(p.getRow() + 1, p.getCol() + 1,
          "[" + viewTableau.getElement(p.getRow() + 1, p.getCol() + 1) + "]",
          Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    }
  }

  /**
   * Displays the increasing and decreasing marks in the tableau circle
   * 
   * @param circle
   */
  private void displayCircleIncDec(List<Point> circle) {
    String sign;
    for (int i = 0; i < circle.size(); i++) {
      Point p = circle.get(i);
      if (i % 2 == 0) {
        sign = "+";
      } else {
        sign = "-";
      }
      viewTableau.put(p.getRow() + 1, p.getCol() + 1,
          viewTableau.getElement(p.getRow() + 1, p.getCol() + 1) + sign,
          Timing.INSTANTEOUS, Timing.INSTANTEOUS);
    }
  }

  /**
   * Displays delta value
   * 
   * @param delta
   */
  private void displayDelta(int delta) {
    if (this.deltaLabel == null) {
      this.deltaLabel = lang.newText(new Offset(0, 30, "totalCost",
          AnimalScript.DIRECTION_SW), "Delta: " + delta, "delta", null,
          labelProps);
    } else {
      this.deltaLabel.setText("Delta: " + delta, Timing.INSTANTEOUS,
          Timing.INSTANTEOUS);
      this.deltaLabel.show();
    }
  }

  /**
   * Displays current total cost value
   * 
   * @param cost
   */
  private void displayTotalCost(int cost) {
    if (this.totalCost == null) {
      this.totalCost = lang.newText(new Offset(0, 30, "varDescription",
          AnimalScript.DIRECTION_SW), "Current total cost: " + cost,
          "totalCost", null, labelProps);
    } else {
      this.totalCost.setText("Current total cost: " + cost, Timing.INSTANTEOUS,
          Timing.INSTANTEOUS);
    }
  }

  /**
   * Displays a conclusion in the last step
   */
  private void displayConclusion() {
    // hide Algorithm elements
    viewTableau.hide();
    sc.hide();
    description.hide();
    descrRect.hide();
    costMatrix.hide();
    costHeading.hide();
    eqnHeading.hide();
    rcHeading.hide();
    deltaLabel.hide();
    totalCost.hide();

    for (Text eq : equations) {
      eq.hide();
    }

    for (Text rc : rcEquations) {
      rc.hide();
    }

    // show conclusion
    conclusion = lang.newSourceCode(new Offset(0, 30, "header",
        AnimalScript.DIRECTION_SW), "conclusion", null, textProps);
    conclusion.addCodeLine("You saw the application of the MODI-Method.", null,
        0, null);
    conclusion
        .addCodeLine(
            "The initial valid tableau which is needed to execute the MODI-Method was determined by using the",
            null, 0, null);
    conclusion
        .addCodeLine(
            "North-West-Corner-Rule. There are several other methods to create this initial solution,",
            null, 0, null);
    conclusion
        .addCodeLine(
            "based on which method is used, possibly a different, equally optimal, solution will be found.",
            null, 0, null);
    conclusion
        .addCodeLine(
            "The quality of the inital tableau also influences the number of iterations which are needed to find",
            null, 0, null);
    conclusion.addCodeLine("the optimal solution.", null, 0, null);
  }

  /**
   * #################################################################### Getter
   * & Setter
   * ####################################################################
   */
  @Override
  public String getName() {
    return "MODI-Method";
  }

  @Override
  public String getAlgorithmName() {
    return "MODI-Method";
  }

  @Override
  public String getAnimationAuthor() {
    return "Daniel Burgmann, Matthias Horn, Torben Stoffer";
  }

  @Override
  public String getDescription() {
    return "The MODI-method (<strong>Mo</strong>dified <strong>Di</strong>stribution method) is an iterative, numerical "
        + "algorithm to solve the standard transportation problem."
        + "At this problem a specific number of suppliers and demanders all with a specific amount of supply respectively "
        + "demand for a specific product and costs for the transportation between every supplier and demander is given. "
        + "The goal is to find the solution with minimal transportation costs on condition that every demand is satisfied."
        + ""
        + "To use the MODI-method a valid start tableau is necessary which is, in this case, generated by use of the "
        + "North-West-Corner-Rule in this case.\n"
        + "This animation focusses on the MODI-Method itself, the application of the North-West-Corner-Rule is not shown, "
        + "because it is not part of the MODI-Method itself and can be replaced by another method.";

  }

  @Override
  public String getCodeExample() {
    return "<h3>MODI-method</h3>"
        + "1) Start with valid tableau. <br>"
        + "2) Consider every field in the main tableau with a value larger than zero as a "
        + "basis variable (highlighted) and all other fields "
        + "as non-basis variables (not highlighted). <br>"
        + "3) Create a system of linear equations "
        + "with the following equation for every basis "
        + "variable (Xij): Ui + Vj = Cij <br>"
        + "4) Solve system of linear equations by setting "
        + "U0 to 0.<br>"
        + "5) Calculate reduced costs (RC) for all "
        + "non-basis variables: RCij = Cij - Ui - Vj <br>"
        + "6) Check if all reduced costs are larger than zero. <br>"
        + "7) All RC > 0 => Algorithm Finished: Optimal solution (= solution with min cost) is found. <br>"
        + "8) Any RC < 0 => Basis variables have to be changed. <br>"
        + "8.1) Non-basis variable with most negative value "
        + "becomes new basis variable  <br>"
        + "8.2) Emergence of a circle in tableau. <br>"
        + "8.3) Redirection of values along the circle - Alternating increasing and decreasing <br>"
        + "8.3.1) Use smallest decreasing value as delta. <br>"
        + "8.3.2) Increase / Decrease " + "variables in circle by delta. <br>"
        + "8.3.3) At least one basis variable becomes zero "
        + "and leaves the basis (becomes a non-basis variable). <br>"
        + "10) Go to step 3. ";
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.US;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  /**
   * ####################################################################
   * Miscellaneous
   * ####################################################################
   */
  private String checkInput() {
    // check if sum of demand and supply are equal
    if (sumArray(supply) != sumArray(demand))
      return "Sum of amount of supply has to equals sum of amount of demand.";

    // check if dimensions of cost array fit
    if (supply.length != costs.length)
      return "The cost matrix needs as many rows as number of available suppliers.";

    if (demand.length != costs[0].length)
      return "The cost matrix needs as many columns as number of available demander";

    return null;
  }

  /**
   * Sum all elements of an array.
   * 
   * @param array
   *          array to calculate sum of
   * @return sum of all elements
   */
  private int sumArray(int[] array) {
    int sum = 0;

    for (int value : array)
      sum += value;

    return sum;
  }

  /**
   * converst given int Matrix to a string matrix
   * 
   * @param intMatrix
   *          input int matrix
   * @return
   */
  private String[][] intMatrixToStringMatrix(int[][] intMatrix) {
    int sizex = intMatrix[0].length;
    int sizey = intMatrix.length;
    String[][] result = new String[sizey][sizex];

    for (int i = 0; i < sizey; i++) {
      for (int j = 0; j < sizex; j++) {
        result[i][j] = String.valueOf(intMatrix[i][j]);
      }
    }
    return result;
  }

  /**
   * #################################################################### Debug
   * ####################################################################
   */
  public static void main(String[] args) {
    ModiMethod mm = new ModiMethod();
    mm.mainInit();
    mm.init();

    System.out.print(mm.generate(null, null).replaceAll("refresh", ""));
  }

  /**
   * Additional init function when not called from animal
   */
  public void mainInit() {
    tableauProps = new MatrixProperties();
    tableauProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    tableauProps
        .set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    tableauProps
        .set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.CYAN);
    tableauProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.ORANGE);
    tableauProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
    sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLUE);
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 12));
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    textProps = new SourceCodeProperties();
    textProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    textProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    labelProps = new TextProperties();
    labelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 12));
  }

  /**
   * #################################################################### Enums
   * & inner classes
   * ####################################################################
   */
  /**
   * Direction to continue search to find circle.
   * 
   * @author Daniel Burgmann, Matthias Horn, Torben Stoffer
   * @version 1.0
   */
  private enum Direction {

    HORIZONTAL, VERTICAL
  }

  /**
   * Represents one point in tableau matrix.
   * 
   * @author Daniel Burgmann, Matthias Horn, Torben Stoffer
   * @version 1.0
   */
  private class Point {

    /**
     * Row.
     */
    int row;
    /**
     * Column.
     */
    int col;

    /**
     * Constructor to set attributes
     * 
     * @param row
     *          row to set
     * @param col
     *          column to set
     */
    Point(int row, int col) {
      this.row = row;
      this.col = col;
    }

    /**
     * Getter for row.
     * 
     * @return row
     */
    int getRow() {
      return row;
    }

    /**
     * Getter for column.
     * 
     * @return column
     */
    int getCol() {
      return col;
    }

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Point)) {
        return false;
      }

      Point p = (Point) o;

      return row == p.row && col == p.col;
    }
  }
}