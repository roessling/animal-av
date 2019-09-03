package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class NearestCentroidClassifier implements Generator {
  private Language             lang;
  private int[][]              pointsToBeCategorized_x_y;
  private SourceCodeProperties sourceCode;
  private CircleProperties     circleBlue;
  private CircleProperties     circleRed;
  private TextProperties       caption;
  private CircleProperties     circleGreen;
  private Variables            vars;

  public void init() {
    lang = new AnimalScript("Nearest Centroid Classifier", "Theo Kischka", 800,
        600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    pointsToBeCategorized_x_y = (int[][]) primitives
        .get("pointsToBeCategorized_x_y");
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    circleBlue = (CircleProperties) props.getPropertiesByName("circleBlue");
    circleRed = (CircleProperties) props.getPropertiesByName("circleRed");
    caption = (TextProperties) props.getPropertiesByName("caption");
    circleGreen = (CircleProperties) props.getPropertiesByName("circleGreen");

    classify();
    return lang.toString();
  }

  public String getName() {
    return "Nearest Centroid Classifier";
  }

  public String getAlgorithmName() {
    return "Nearest Centroid Classifier";
  }

  public String getAnimationAuthor() {
    return "Theo Kischka";
  }

  public String getDescription() {
    return "Nearest centroid or nearest prototype classifier is a classification model that assigns"
        + "\n"
        + "to observations the label of the class of training samples whose mean (centroid) is closest"
        + "\n" + "to the observation.";
  }

  public String getCodeExample() {
    return "Given:A training set of categorized two-dimensional points"
        + "\n"
        + "1.Calculate the centroid of each category (sum of each point in the"
        + "\n"
        + "category divivded by the number of points within the category"
        + "\n"
        + "2.The category which has the shortest (euclidean) distance from its centroid to an uncategorized point will be chosen";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  // Definitionen fuer den Klassifizierer
  public class CategoryPoint {
    public Coordinates c;
    public categories  category;

    public CategoryPoint(Coordinates c, categories category) {
      this.c = c;
      this.category = category;
    }

    public String toString() {
      return "x:" + c.getX() + ";y+" + c.getY() + ";category:" + category;
    }
  }

  public enum categories {
    empty, red, Green, Blue;
  }

  private ArrayList<CategoryPoint> trainingData;
  private ArrayList<CategoryPoint> centroids;
  private ArrayList<Coordinates>   testingData;
  private ArrayList<CategoryPoint> results;

  // Animation
  private final static int         SCALE_X = 5;
  private final static int         SCALE_Y = 3;
  private final Coordinates        CENTER  = new Coordinates(500, 250);
  private Text                     status;
  private SourceCode               pseudoCode;

  public NearestCentroidClassifier() {
    init();
  }

  // ---------------------Hauptmethode
  public void classify() {
    createHeader();
    createHeaderBox();
    SourceCode descr = createDescription();

    lang.nextStep("Introduction");
    Text descrCaption = createCaptionTextAt("Algorithm", new Coordinates(20,
        180));
    createDescriptionSteps();

    descr.hide();
    descrCaption.setText("Pseudocode", null, null);
    pseudoCode = createPseudoCode();

    lang.nextStep("Algorithm in action (Animation)");
    train();
    lang.nextStep();
    test();

    lang.nextStep("Results");
    pseudoCode.hide();
    status.hide();
    descrCaption.show();
    descrCaption.setText("Time Complexity", null, null);
    createTimeComplexityDescription();
    // showResults();
  }

  public void train() {
    createTrainingData();
    status = createCaptionTextAt("Drawing points", new Coordinates(20, 400));
    pseudoCode.highlight(0);
    drawCircles(trainingData);

    lang.nextStep();
    centroids = new ArrayList<CategoryPoint>();
    vars = lang.newVariables();
    vars.openContext();
    for (categories c : categories.values()) {
      centroids.add(new CategoryPoint(calculateCentroid(c), c));
    }
    status.setText("Drawing centroids (rectangles)", null, null);
    drawRects(centroids);
  }

  public void test() {
    results = new ArrayList<CategoryPoint>();
    categories cat = categories.empty;
    createTestingData();
    status.setText("Uncategorized points (Black circles)", null, null);
    pseudoCode.unhighlight(0);
    pseudoCode.highlight(1);
    ArrayList<CategoryPoint> temp = new ArrayList<CategoryPoint>();
    for (Coordinates c : testingData) {
      temp.add(new CategoryPoint(c, categories.empty));
      vars.declare("String", new CategoryPoint(c, categories.empty).toString());
    }
    drawCircles(temp);
    lang.nextStep();
    double min = Double.MAX_VALUE;
    for (Coordinates c : testingData) {
      for (CategoryPoint centroid : centroids) {
        double distance = euclideanDistance(c, centroid.c);
        lang.nextStep();
        status.setText("Point: " + c.getX() + ";" + c.getY() + " distance to "
            + centroid.category + " cluster:" + Math.round(distance), null,
            null);
        vars.declare("String", "distance" + c.getX() + ";" + c.getY()
            + ";toCategory" + centroid.category + ":" + distance);
        if (distance < min) {
          min = distance;
          cat = centroid.category;
        }
      }
      lang.nextStep();
      status.setText("Point: " + c.getX() + ";" + c.getY() + " -> " + cat,
          null, null);
      results.add(new CategoryPoint(c, cat));
    }
  }

  private void createTrainingData() {
    trainingData = new ArrayList<CategoryPoint>();
    trainingData
        .add(new CategoryPoint(new Coordinates(10, 20), categories.red));
    trainingData.add(new CategoryPoint(new Coordinates(8, 15), categories.red));
    trainingData
        .add(new CategoryPoint(new Coordinates(12, 12), categories.red));
    trainingData
        .add(new CategoryPoint(new Coordinates(10, 10), categories.red));
    trainingData
        .add(new CategoryPoint(new Coordinates(20, 20), categories.red));
    trainingData.add(new CategoryPoint(new Coordinates(14, 9), categories.red));
    trainingData.add(new CategoryPoint(new Coordinates(35, 10),
        categories.Green));
    trainingData
        .add(new CategoryPoint(new Coordinates(40, 1), categories.Green));
    trainingData
        .add(new CategoryPoint(new Coordinates(50, 5), categories.Green));
    trainingData.add(new CategoryPoint(new Coordinates(45, 11),
        categories.Green));
    trainingData
        .add(new CategoryPoint(new Coordinates(37, 8), categories.Green));
    trainingData
        .add(new CategoryPoint(new Coordinates(42, 9), categories.Green));
    trainingData
        .add(new CategoryPoint(new Coordinates(10, 90), categories.Blue));
    trainingData
        .add(new CategoryPoint(new Coordinates(8, 87), categories.Blue));
    trainingData
        .add(new CategoryPoint(new Coordinates(15, 78), categories.Blue));
    trainingData
        .add(new CategoryPoint(new Coordinates(12, 85), categories.Blue));
    trainingData
        .add(new CategoryPoint(new Coordinates(13, 91), categories.Blue));
    trainingData
        .add(new CategoryPoint(new Coordinates(15, 82), categories.Blue));
  }

  private void createTestingData() {
    testingData = new ArrayList<Coordinates>();
    // testingData.add(new Coordinates(15, 99)); // cat3
    // testingData.add(new Coordinates(50, 9)); // cat2
    // testingData.add(new Coordinates(11, 11)); // cat1
    for (int i = 0; i < pointsToBeCategorized_x_y.length; i++) {
      int x = pointsToBeCategorized_x_y[i][0];
      int y = pointsToBeCategorized_x_y[i][1];
      if (x != 0 && y != 0) {
        testingData.add(new Coordinates(x, y));
      }
    }
    if (testingData.size() == 0) {
      testingData.add(new Coordinates(0, 0));
    }
  }

  private Coordinates calculateCentroid(categories cat) {
    Coordinates centroid = new Coordinates(0, 0);
    int counter = 1; // um Division durch 0 zu verhindern
    // (Laplace-Korrektur)
    for (CategoryPoint p : trainingData) {
      if (p.category.equals(cat)) {
        centroid = new Coordinates(centroid.getX() + p.c.getX(),
            centroid.getY() + p.c.getY());
        counter++;
      }
    }
    return new Coordinates(centroid.getX() / counter, centroid.getY() / counter); // ungenau
  }

  public double euclideanDistance(Coordinates from, Coordinates to) {
    return Math.sqrt(Math.pow(Math.abs(from.getX() - to.getX()), 2)
        + Math.pow(Math.abs(from.getY() - to.getY()), 2));
  }

  public void showResults() {
    for (CategoryPoint c : results) {
      System.out.println(c.c + " -> " + c.category);
    }
  }

  // ----------Animationszeugs

  private CircleProperties getStandardCircleProperties(categories c) {
    CircleProperties props = new CircleProperties();
    switch (c) {
      case empty:
        props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        break;
      case red:
        // props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
        return this.circleRed;
        // break;
      case Green:
        // props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
        // break;
        return this.circleGreen;
      case Blue:
        // props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        // break;
        return this.circleBlue;
      default:
        props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    }
    return props;
  }

  private RectProperties getStandardRectProperties(categories c) {
    RectProperties props = new RectProperties();
    switch (c) {
      case empty:
        props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
        break;
      case red:
        props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
        break;
      case Green:
        props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
        break;
      case Blue:
        props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        break;
      default:
        props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    }
    return props;
  }

  private SourceCodeProperties getStandardSourceCodeProperties() {
    // SourceCodeProperties scProps = new SourceCodeProperties();
    // scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    // scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // "Monospaced", Font.PLAIN, 12));
    //
    // scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    // scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    // return scProps;
    return this.sourceCode;
  }

  private void drawCircles(ArrayList<CategoryPoint> points) {
    for (CategoryPoint p : points) {
      lang.newCircle(new Coordinates(CENTER.getX() + p.c.getX() * SCALE_X,
          CENTER.getY() + p.c.getY() * SCALE_Y), 3, p.c.toString() + ";"
          + p.category, null, getStandardCircleProperties(p.category));
    }
  }

  private void drawRects(ArrayList<CategoryPoint> points) {
    for (CategoryPoint p : points) {
      lang.newRect(
          new Coordinates(CENTER.getX() + p.c.getX() * SCALE_X, CENTER.getY()
              + p.c.getY() * SCALE_Y),
          new Coordinates(CENTER.getX() + p.c.getX() * SCALE_X + 3, CENTER
              .getY() + p.c.getY() * SCALE_Y + 3), p.c.toString() + ";"
              + p.category, null, getStandardRectProperties(p.category));
    }
  }

  private void createHeader() {
    Font newFont = new Font("SansSerif", Font.BOLD, 20);
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, newFont);
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    lang.newText(new Coordinates(20, 31), "Nearest Centroid Classifier",
        "header", null, textProps);
  }

  private void createHeaderBox() {
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.green);
    lang.newRect(new Coordinates(15, 26), new Coordinates(300, 62),
        "headerRect", null, rectProps);
  }

  private SourceCode createDescription() {
    SourceCodeProperties scProps = getStandardSourceCodeProperties();
    SourceCode descr = lang.newSourceCode(new Coordinates(20, 100),
        "description", null, scProps);
    descr
        .addCodeLine(
            "Nearest centroid or nearest prototype classifier is a classification model that assigns",
            null, 0, null);
    descr
        .addCodeLine(
            "to observations the label of the class of training samples whose mean (centroid) is closest to the observation.",
            null, 0, null);
    return descr;
  }

  private Text createCaptionTextAt(String text, Coordinates coords) {
    // Font newFont = new Font("SansSerif", Font.BOLD, 20);
    // TextProperties textProps = new TextProperties();
    // textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, newFont);
    // textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    // return lang.newText(coords, text, text, null, textProps);
    return lang.newText(coords, text, text, null, this.caption);
  }

  private void createDescriptionSteps() {
    SourceCodeProperties scProps = getStandardSourceCodeProperties();
    SourceCode descr = lang.newSourceCode(new Coordinates(20, 200), "steps",
        null, scProps);
    descr.addCodeLine(
        "Given:A training set of categorized two-dimensional points", null, 0,
        null);
    descr.addCodeLine(
        "1.Calculate the centroid of each category (sum of each point in the",
        null, 0, null);
    descr.addCodeLine(
        "category divivded by the number of points within the category", null,
        0, null);
    lang.nextStep();
    descr
        .addCodeLine(
            "2.The category which has the shortest (euclidean) distance from its centroid to an uncategorized point will be chosen",
            null, 0, null);
    lang.nextStep("Algorithm explained");
    descr.hide();
  }

  private SourceCode createPseudoCode() {
    SourceCodeProperties scProps = getStandardSourceCodeProperties();
    SourceCode descr = lang.newSourceCode(new Coordinates(20, 200),
        "description", null, scProps);
    descr.addCodeLine("1.Training: Calculate centroid for each category", null,
        0, null);
    descr
        .addCodeLine(
            "2.Testing: Find category with shortest distance to uncategorized point",
            null, 0, null);
    descr.addCodeLine(
        "3.Repeat steps 1 and 2 if any uncategorized points are left", null, 0,
        null);
    return descr;
  }

  private void createTimeComplexityDescription() {
    SourceCodeProperties scProps = getStandardSourceCodeProperties();
    SourceCode descr = lang.newSourceCode(new Coordinates(20, 200),
        "time complexity", null, scProps);
    descr.addCodeLine("Training time has linear complexity.", null, 0, null);
  }

  // test

  private static SourceCodeProperties getOLDStandardSourceCodeProperties() {
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));

    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    return scProps;
  }

  private static TextProperties getOLDTextProps() {
    Font newFont = new Font("SansSerif", Font.BOLD, 20);
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, newFont);
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    return textProps;
    // return lang.newText(coords, text, text, null, this.caption);
  }

  private static CircleProperties getOLDStandardCircleProperties(categories c) {
    CircleProperties props = new CircleProperties();
    switch (c) {
      case empty:
        props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        break;
      case red:
        props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
        // return this.circleRed;
        break;
      case Green:
        props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
        break;
      // return this.circleGreen;
      case Blue:
        props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        break;
      // return this.circleBlue;
      default:
        props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    }
    return props;
  }

  public static void main(String[] args) {
    NearestCentroidClassifier rocchio = new NearestCentroidClassifier();
    rocchio.init();

    int[][] points = new int[10][2];
    points[0][0] = 15;
    points[0][1] = 99;
    points[1][0] = 50;
    points[1][1] = 9;
    points[2][0] = 11;
    points[2][1] = 11;

    rocchio.pointsToBeCategorized_x_y = points;

    rocchio.sourceCode = NearestCentroidClassifier
        .getOLDStandardSourceCodeProperties();
    rocchio.caption = NearestCentroidClassifier.getOLDTextProps();
    rocchio.circleBlue = NearestCentroidClassifier
        .getOLDStandardCircleProperties(categories.Blue);
    rocchio.circleGreen = NearestCentroidClassifier
        .getOLDStandardCircleProperties(categories.Green);
    rocchio.circleRed = NearestCentroidClassifier
        .getOLDStandardCircleProperties(categories.red);

    rocchio.classify();

    System.out.println(rocchio.lang);
  }

}