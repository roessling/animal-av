package generators.misc.machineLearning;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import translator.Translator;

public class RelieF implements ValidatingGenerator {

  public Language             lang;
  public SourceCode           explanation;
  public SourceCodeProperties expProps;
  public SourceCode           sc;
  public SourceCodeProperties scProps;
  public StringMatrix         matrix;
  public MatrixProperties     matrixProps;
  public TextProperties       titleProps;
  public TextProperties       textProps;
  public StringMatrix         datasetMatrix;
  public Variables            vars;
  public LinkedList<Double>   weights;
  public LinkedList<Integer>  hits;
  public LinkedList<Integer>  misses;
  public Translator           translator;
  public int                  r;

  /*
   * new RelieF("generators/misc/machineLearning/helperRelief/Relief", Locale.US)
   * new RelieF("generators/misc/machineLearning/helperRelief/Relief", Locale.GERMANY)
   */
  
  public RelieF(String resourceName, Locale locale) {
    lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT,
        "getAlgorithmName()", "getAnimationAuthor()", 1000, 1000);
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    translator = new Translator(resourceName, locale);
  }

  public void updateWeights(StringMatrix m, int e, int hit, int miss) {

    int distanceHit = 0;
    int distanceMiss = 0;

    sc.unhighlight(3);
    sc.unhighlight(5);
    sc.highlight(6);
    sc.highlight(7);

    String s = "";

    for (int i = 1; i < m.getNrCols() - 1; i++) {

      m.setGridFont(hit, i, new Font("SansSerif", Font.BOLD, 12), null, null);
      m.setGridFont(miss, i, new Font("SansSerif", Font.BOLD, 12), null, null);
      m.setGridFont(e, i, new Font("SansSerif", Font.BOLD, 12), null, null);

      distanceHit = m.getElement(e, i).equalsIgnoreCase(m.getElement(hit, i))
          ? 0
          : 1;
      distanceMiss = m.getElement(e, i).equalsIgnoreCase(m.getElement(miss, i))
          ? 0
          : 1;

      double w = weights.get(i - 1);

      s = translator.translateMessage("updateWeight") + "w_"
          + m.getElement(0, i) + " = w_" + m.getElement(0, i) + "  + 1/r * ( d_"
          + m.getElement(0, i) + "(ID" + miss + ", ID" + e + ") - " + "d_"
          + m.getElement(0, i) + "(ID" + hit + ", ID" + e + ") )";

      double newW = round(w + (double) 1 / r * (distanceMiss - distanceHit), 3);

      setExplanationText(s);

      weights.set(i - 1, newW);

      lang.nextStep();

      s = s + "\nw_" + m.getElement(0, i) + " = " + w + " + 1/" + r + "* ( "
          + distanceMiss + " - " + distanceHit + ") = " + newW;
      setExplanationText(s);

      vars.declare("double", "\"w_"+m.getElement(0, i)+"\"",
          String.valueOf(weights.get(i - 1)));

      lang.nextStep();

      m.setGridFont(hit, i, new Font("SansSerif", Font.PLAIN, 12), null, null);
      m.setGridFont(miss, i, new Font("SansSerif", Font.PLAIN, 12), null, null);
      m.setGridFont(e, i, new Font("SansSerif", Font.PLAIN, 12), null, null);
    }
  }

  public int findNearestHit(StringMatrix dataset, StringMatrix distanceTable,
      int rand) {

    int nearestD = Integer.MAX_VALUE;
    int nearestE = 0;

    for (int i = 1; i < distanceTable.getNrRows(); i++) {

      if (hits.contains(i)
          && Integer.valueOf(distanceTable.getElement(i, 1)) < nearestD) {
        nearestE = Integer.parseInt(distanceTable.getElement(i, 0));
        nearestD = Integer.parseInt(distanceTable.getElement(i, 1));
      }

    }

    unhighlightAllSourceCodeLines(sc, 8);
    sc.highlight(3);
    sc.highlight(4);
    setExplanationText(
        translator.translateMessage("chooseNearestHit", String.valueOf(rand)));

    for (int i = 1; i < distanceTable.getNrRows(); i++) {
      if (distanceTable.getElement(i, 0)
          .equalsIgnoreCase(String.valueOf(nearestE))) {
        changeRowColor(distanceTable, i, Color.GREEN);
      }
    }

    for (int j = 1; j < dataset.getNrRows(); j++) {
      if (dataset.getElement(j, 0).equalsIgnoreCase(String.valueOf(nearestE))) {
        changeRowColor(dataset, j, Color.GREEN);
      }
    }

    return nearestE;
  }

  public int findNearestMiss(StringMatrix dataset, StringMatrix distanceTable,
      int rand) {

    int nearestD = Integer.MAX_VALUE;
    int nearestE = 0;

    for (int i = 1; i < distanceTable.getNrRows(); i++) {

      if (misses.contains(i)
          && Integer.valueOf(distanceTable.getElement(i, 1)) < nearestD) {
        nearestE = Integer.parseInt(distanceTable.getElement(i, 0));
        nearestD = Integer.parseInt(distanceTable.getElement(i, 1));
      }

    }

    sc.toggleHighlight(4, 5);
    setExplanationText(
        translator.translateMessage("chooseNearestMiss", String.valueOf(rand)));

    for (int i = 1; i < distanceTable.getNrRows(); i++) {
      if (distanceTable.getElement(i, 0)
          .equalsIgnoreCase(String.valueOf(nearestE))) {
        changeRowColor(distanceTable, i, Color.RED);
      }
    }

    for (int j = 1; j < dataset.getNrRows(); j++) {
      if (dataset.getElement(j, 0).equalsIgnoreCase(String.valueOf(nearestE))) {
        changeRowColor(dataset, j, Color.RED);
      }
    }

    return nearestE;
  }

  public StringMatrix createDistanceTable(StringMatrix m, int row) {

    String[][] distanceTableArr = createEmptyMatrix(m.getNrRows() - 1, 2);
    StringMatrix distanceTable = lang.newStringMatrix(
        new Offset(0, 10, "interSteps", AnimalScript.DIRECTION_SW),
        distanceTableArr, "distanceTable", null, matrixProps);

    distanceTable.put(0, 0, "ID", null, null);
    distanceTable.put(0, 1, translator.translateMessage("distance"), null,
        null);

    changeRowColor(m, row, Color.LIGHT_GRAY);

    int j = 1;
    for (int i = 1; i < m.getNrRows(); i++) {

      changeRowColor(m, i, Color.LIGHT_GRAY);

      if (i != row) {
        int d = calcDistance(m, row, i);

        distanceTable.put(j, 0, String.valueOf(i), null, null);
        distanceTable.put(j, 1, String.valueOf(d), null, null);
        j++;
      }

    }

    return distanceTable;
  }

  public int calcDistance(StringMatrix m, int ex1, int ex2) {

    int distance = 0;

    String s = translator.translateMessage("calcDistBetween",
        String.valueOf(ex1));
    setExplanationText(s);
    s = s + translator.translateMessage("distance") + "(ID" + ex1 + ", ID" + ex2
        + ") = ";

    setExplanationText(s);
    lang.nextStep();

    for (int i = 1; i < m.getNrCols() - 1; i++) {

      m.setGridFont(ex1, i, new Font("SansSerif", Font.BOLD, 12), null, null);
      m.setGridFont(ex2, i, new Font("SansSerif", Font.BOLD, 12), null, null);

      if (!m.getElement(ex1, i).equalsIgnoreCase(m.getElement(ex2, i))) {
        distance++;
        s = s + "1 + ";
      } else {
        s = s + "0 + ";
      }

      if (i == m.getNrCols() - 2) {
        s = s.substring(0, s.length() - 2);
      }
      setExplanationText(s);
      lang.nextStep();
      m.setGridFont(ex1, i, new Font("SansSerif", Font.PLAIN, 12), null, null);
      m.setGridFont(ex2, i, new Font("SansSerif", Font.PLAIN, 12), null, null);
    }

    s = s + " = " + distance;
    setExplanationText(s);

    lang.nextStep();
    m.unhighlightCellColumnRange(ex2, 0, m.getNrCols() - 1, null, null);

    return distance;
  }

  public void createIntroWithoutDesc(String headline, Coordinates uL,
      Coordinates lR, Coordinates tUL) {

    RectProperties headlinebgProps = new RectProperties();
    RectProperties shadowProps = new RectProperties();

    // shadow properties (back rectangle in the background)
    shadowProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    shadowProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
    shadowProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    shadowProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

    // headline rectangle properties
    headlinebgProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    headlinebgProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    headlinebgProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
    headlinebgProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

    // shadow rectangle
    lang.newRect(uL, lR, "headlinebg", null, headlinebgProps);
    Coordinates shadowUpperLeft = new Coordinates(uL.getX() + 10,
        uL.getY() + 10);
    Coordinates shadowLowerRight = new Coordinates(lR.getX() + 10,
        lR.getY() + 10);

    // headline rectangle
    lang.newRect(shadowUpperLeft, shadowLowerRight, "shadow", null,
        shadowProps);
    TextProperties headlineProps = new TextProperties();
    headlineProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.BOLD, 30));

    // headline text
    lang.newText(tUL, headline, "headline", null, headlineProps);

    SourceCodeProperties descProps = new SourceCodeProperties();
    descProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.PLAIN, 12));
  }

  public void createIntro(String headline, String description, Coordinates uL,
      Coordinates lR, Coordinates tUL, Coordinates dC) {

    RectProperties headlinebgProps = new RectProperties();
    RectProperties shadowProps = new RectProperties();

    // shadow properties (back rectangle in the background)
    shadowProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    shadowProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
    shadowProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    shadowProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

    // headline rectangle properties
    headlinebgProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    headlinebgProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    headlinebgProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
    headlinebgProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);

    // shadow rectangle
    lang.newRect(uL, lR, "headlinebg", null, headlinebgProps);
    Coordinates shadowUpperLeft = new Coordinates(uL.getX() + 10,
        uL.getY() + 10);
    Coordinates shadowLowerRight = new Coordinates(lR.getX() + 10,
        lR.getY() + 10);

    // headline rectangle
    lang.newRect(shadowUpperLeft, shadowLowerRight, "shadow", null,
        shadowProps);
    TextProperties headlineProps = new TextProperties();
    headlineProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.BOLD, 30));

    // headline text
    lang.newText(tUL, headline, "headline", null, headlineProps);

    SourceCodeProperties descProps = new SourceCodeProperties();
    descProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.PLAIN, 12));

    SourceCode desc = lang.newSourceCode(dC, "desc", null, descProps);
    desc.addMultilineCode(description, "desc", null);
  }

  public void setExplanationText(String s) {
    if (explanation != null) {
      explanation.hide();
    }
    explanation = lang.newSourceCode(
        new Offset(0, 0, "titleExplanation", AnimalScript.DIRECTION_SW),
        "explanation", null, expProps);
    explanation.addMultilineCode(s, null, null);
  }

  public double round(double v, int d) {
    return Math.round(v * Math.pow(10.0, d)) / Math.pow(10.0, d);
  }

  public String[][] createEmptyMatrix(int row, int col) {

    String[][] m = new String[row][col];

    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        m[i][j] = " ";
      }
    }

    return m;
  }

  public void setHeadLine(StringMatrix m) {
    for (int i = 0; i < m.getNrCols(); i++) {
      m.setGridFont(0, i, new Font("SansSerif", Font.BOLD, 12), null, null);
    }
  }

  public String[][] createID(String[][] data) {

    String[][] d = new String[data.length][data[0].length + 1];

    d[0][0] = "ID";

    for (int i = 0; i < d.length; i++) {

      if (i != 0) {
        d[i][0] = String.valueOf(i);
      }

      for (int j = 1; j < d[0].length; j++) {

        d[i][j] = data[i][j - 1];

      }
    }

    return d;

  }

  public void highlightSameClass(StringMatrix m, int row) {

    String classification = m.getElement(row, m.getNrCols() - 1);

    for (int i = 1; i < m.getNrRows(); i++) {

      if (i != row) {

        if (m.getElement(i, m.getNrCols() - 1)
            .equalsIgnoreCase(classification)) {
          changeRowColor(m, i, Color.GREEN);
          hits.add(i);
        } else {
          changeRowColor(m, i, Color.RED);
          misses.add(i);
        }

      }
    }

  }

  public void changeRowColor(StringMatrix m, int row, Color c) {
    for (int i = 0; i < m.getNrCols(); i++) {
      m.setGridHighlightFillColor(row, i, c, null, null);
      m.highlightCellColumnRange(row, 0, m.getNrCols() - 1, null, null);
    }
  }

  public void changeRowColorDefault(StringMatrix m) {

    for (int i = 0; i < m.getNrRows(); i++) {
      for (int j = 0; j < m.getNrCols(); j++) {
        m.setGridHighlightFillColor(i, j, Color.LIGHT_GRAY, null, null);
      }
    }
  }

  public void unhighlightAll(StringMatrix m) {

    for (int i = 0; i < m.getNrRows(); i++) {
      m.unhighlightCellColumnRange(i, 0, m.getNrCols() - 2, null, null);
      m.unhighlightCell(i, m.getNrCols() - 1, null, null);

      m.unhighlightElemColumnRange(i, 0, m.getNrCols() - 1, null, null);
      // m.unhighlightElem(i, m.getNrCols() - 1, null, null);
    }
  }

  public void unhighlightAllSourceCodeLines(SourceCode s, int numOfLines) {
    for (int i = 0; i < numOfLines; i++) {
      s.unhighlight(i);
    }
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    titleProps = (TextProperties) props.getPropertiesByName("titleProps");
    titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.BOLD, 18));

    textProps = (TextProperties) props.getPropertiesByName("textProps");
    
    matrixProps = (MatrixProperties) props.getPropertiesByName("matrixProps");
    scProps = (SourceCodeProperties) props.getPropertiesByName("scProps");
    expProps = (SourceCodeProperties) props.getPropertiesByName("expProps");

    String[][] d = (String[][]) primitives.get("dataset");
    r = (int) primitives.get("r");

    weights = new LinkedList<Double>();
    hits = new LinkedList<Integer>();
    misses = new LinkedList<Integer>();                                                

    createIntro("RelieF", translator.translateMessage("intro"),
        new Coordinates(10, 10), new Coordinates(200, 60),
        new Coordinates(60, 15), new Coordinates(10, 100));

    MultipleChoiceQuestionModel quest1 = new MultipleChoiceQuestionModel("q1");
    quest1.setPrompt(translator.translateMessage("q1"));
    quest1.addAnswer("0", translator.translateMessage("q1a1"), 0,
        translator.translateMessage("qFeedbackC"));
    quest1.addAnswer("1", translator.translateMessage("q1a2"), 0,
        translator.translateMessage("qFeedbackW"));
    quest1.addAnswer("2", translator.translateMessage("q1a3"), 0,
        translator.translateMessage("qFeedbackW"));
    lang.addMCQuestion(quest1);

    MultipleChoiceQuestionModel quest2 = new MultipleChoiceQuestionModel("q2");
    quest2.setPrompt(translator.translateMessage("q2"));
    quest2.addAnswer("4", translator.translateMessage("q2a1"), 0,
        translator.translateMessage("qFeedbackW"));
    quest2.addAnswer("5", translator.translateMessage("q2a2"), 0,
        translator.translateMessage("qFeedbackC"));
    quest2.addAnswer("6", translator.translateMessage("q2a3"), 0,
        translator.translateMessage("qFeedbackW"));

    lang.nextStep("intro");
    lang.hideAllPrimitives();

    lang.addMCQuestion(quest1);

    createIntroWithoutDesc("RelieF", new Coordinates(10, 10),
        new Coordinates(200, 60), new Coordinates(60, 15));

    lang.newText(new Coordinates(10, 130),
        translator.translateMessage("dataset"), "titleTrainingData", null,
        titleProps);

    datasetMatrix = lang.newStringMatrix(
        new Offset(0, 10, "titleTrainingData", AnimalScript.DIRECTION_SW),
        createID(d), "dataset", null, matrixProps);

    setHeadLine(datasetMatrix);

    lang.newText(new Offset(200, -30, "dataset", AnimalScript.DIRECTION_NE),
        translator.translateMessage("interSteps"), "interSteps", null,
        titleProps);

    lang.newText(new Offset(200, 0, "interSteps", AnimalScript.DIRECTION_NE),
        translator.translateMessage("sc"), "scTitle", null, titleProps);

    sc = lang.newSourceCode(
        new Offset(0, 0, "scTitle", AnimalScript.DIRECTION_SW), "sc", null,
        scProps);

    sc.addCodeLine("1.) set all attribute weigths w_A = 0.0", null, 0, null);
    sc.addCodeLine("2.) for i = 1 to r", null, 0, null);
    sc.addCodeLine("    select a random example x", null, 1, null);
    sc.addCodeLine("    find:", null, 1, null);
    sc.addCodeLine("    h: nearest neighbor of same class (nearest hit)", null,
        2, null);
    sc.addCodeLine("    m: nearest neighbor of different class (nearest miss)",
        null, 2, null);
    sc.addCodeLine("    for each attribute A:", null, 1, null);
    sc.addCodeLine("    w_A = w_A + 1/r * (d_A(m,x) - d_A(h,x))", null, 2,
        null);

    lang.newText(new Offset(0, 30, "sc", AnimalScript.DIRECTION_SW),
        translator.translateMessage("explanation"), "titleExplanation", null,
        titleProps);

    lang.nextStep();

    sc.highlight(0);
    setExplanationText(translator.translateMessage("initWeights"));

    vars = lang.newVariables();
    vars.declare("int", "r", String.valueOf(r));

    // init weights
    for (int i = 0; i < d[0].length - 1; i++) {
      vars.declare("double", "\"w_" + d[0][i] + "\"", "0.0");
      weights.add(0.0);
    }

    lang.nextStep("init weights");

    sc.unhighlight(0);
    Random random = new Random();

    for (int i = 0; i < r; i++) {

      sc.highlight(1);
      sc.highlight(2);
      int rand = random.nextInt(d.length - 1) + 1;

      vars.declare("int", "iteration", String.valueOf(i + 1));
      vars.declare("int", "random", String.valueOf(rand));

      datasetMatrix.highlightCellColumnRange(rand, 0,
          datasetMatrix.getNrCols() - 1, null, null);

      setExplanationText(
          translator.translateMessage("randomValue") + " " + rand);

      lang.nextStep("iteration" + (i + 1));

      setExplanationText(translator.translateMessage("highlightSameClass",
          String.valueOf(rand)));
      highlightSameClass(datasetMatrix, rand);

      lang.nextStep();

      changeRowColorDefault(datasetMatrix);
      unhighlightAll(datasetMatrix);

      StringMatrix dT = createDistanceTable(datasetMatrix, rand);

      lang.nextStep();
      int nearestHitID = findNearestHit(datasetMatrix, dT, rand);
      sc.highlight(1);

      lang.nextStep();
      int nearestMissID = findNearestMiss(datasetMatrix, dT, rand);
      sc.highlight(1);

      lang.nextStep();
      updateWeights(datasetMatrix, rand, nearestHitID, nearestMissID);

      if (i != r - 1) {
        dT.hide();
      }

      while (!hits.isEmpty()) {
        hits.removeFirst();
      }

      while (!misses.isEmpty()) {
        misses.removeFirst();
      }

      unhighlightAllSourceCodeLines(sc, 8);
      unhighlightAll(datasetMatrix);
    }

    lang.hideAllPrimitives();

    String outro = translator.translateMessage("outro", String.valueOf(r));

    for (int i = 0; i < weights.size(); i++) {
      outro = outro + "w_" + datasetMatrix.getElement(0, i + 1) + ": " + weights.get(i)
          + "\n";
    }

    createIntro("RelieF", outro, new Coordinates(10, 10),
        new Coordinates(200, 60), new Coordinates(60, 15),
        new Coordinates(10, 100));

    lang.nextStep("outro");

    lang.addMCQuestion(quest2);

    lang.finalizeGeneration();

    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "RelieF";
  }

  @Override
  public String getAnimationAuthor() {
    return "Dominik Rödszus, Stanislaw Kin";

  }

  @Override
  public String getCodeExample() {
    return "1.) set all attribute weigths w_A = 0.0" + "\n"
        + "2.) for i = 1 to r" + "\n" + "    select a random example x" + "\n"
        + "    find:" + "\n"
        + "       h: nearest neighbor of same class (nearest hit)" + "\n"
        + "       m: nearest neighbor of different class (nearest miss)" + "\n"
        + "    for each attribute A:" + "\n"
        + "       w_A = w_A + 1/r * (d_A(m,x) - d_A(h,x))";
  }

  @Override
  public Locale getContentLocale() {
    return translator.getCurrentLocale();
  }

  @Override
  public String getDescription() {
    return translator.translateMessage("intro");

  }

  @Override
  public String getFileExtension() {
    return "asu";

  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);

  }

  @Override
  public String getName() {
    return "RelieF";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;

  }

  @Override
  public void init() {
    lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT,
        getAlgorithmName(), getAnimationAuthor(), 1000, 1000);
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {
   

    String[][] trainingDataValidate = (String[][]) primitives.get("dataset");
    int r = (int) primitives.get("r");

    
    for(int i = 1; i < trainingDataValidate.length; i++) {
      
      for(int j = 1; j < trainingDataValidate[0].length; j++) {
        
        if(isNumericAttribute(trainingDataValidate[i][j])) {
          throw new IllegalArgumentException(
              "Only nominal attributes are allowed.");
        }
        
        if(trainingDataValidate[i][j].equals(" ") || trainingDataValidate[i][j] == null) {
          throw new IllegalArgumentException(
              "Values are missing.");
        }
        
        if (!trainingDataValidate[i][trainingDataValidate[0].length - 1]
            .equalsIgnoreCase("no")
            && (!trainingDataValidate[i][trainingDataValidate[0].length - 1]
                .equalsIgnoreCase("yes"))) {
          throw new IllegalArgumentException(
              "The last attribute classifies the training example. Please specifiy the class of each example by adding 'yes' or 'no' to the class attribute (the last element of a training example).");
        }
      }
      
    }
    
    if(r < 1) {
      throw new IllegalArgumentException(
          "r must be greater than 0");
    }
    
    
    
    return true;
    
  }
  

  public boolean isNumericAttribute(String s) {
    return s != null && s.matches("[-+]?\\d*\\.?\\d+");
  }

}
