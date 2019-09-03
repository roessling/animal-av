package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

/**
 * @author Dominik Unzicker, Torben Unzicker
 * @version 1.0 2013-08-30
 */
public class FindS implements ValidatingGenerator {
  private Language              lang;
  private String[][]            trainingData;
  private String[][]            trainingDataValidate;
  private SourceCodeProperties  scProp;
  private ArrayProperties       oldHypoProp;
  private ArrayProperties       newHypoProp;
  private MatrixProperties      trainingDataProp;
  private ArrayMarkerProperties hypoMarkerProp;

  boolean                       firstIteration;
  // Booleans for Questions
  boolean                       firstQuestion;
  boolean                       secondQuestion;

  private Variables             variables;
  private String                oldHypoString;

  public void init() {
    lang = new AnimalScript("Find-S", "Dominik Unzicker, Torben Unzicker", 800,
        600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    validateInput(props, primitives);
    init();

    firstIteration = true;
    // Booleans for Questions
    firstQuestion = true;
    secondQuestion = true;
    oldHypoString = "";

    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    trainingData = (String[][]) primitives.get("trainingData");
    scProp = (SourceCodeProperties) props.getPropertiesByName("scProp");
    oldHypoProp = (ArrayProperties) props.getPropertiesByName("oldHypoProp");
    newHypoProp = (ArrayProperties) props.getPropertiesByName("newHypoProp");
    trainingDataProp = (MatrixProperties) props
        .getPropertiesByName("trainingDataProp");
    hypoMarkerProp = (ArrayMarkerProperties) props
        .getPropertiesByName("hypoMarkerProp");
    String[][] initalString = new String[trainingData.length][trainingData[0].length];

    // Initialize trainingData Matrix
    StringMatrix trainingDataMtx = lang.newStringMatrix(new Coordinates(500,
        100), trainingData, "trainingDataMtx", null, trainingDataProp);

    // Set Variables
    variables = lang.newVariables();

    // Create required Text output
    lang.addLine("text " + '"' + "trainingDataTxt" + '"' + " " + '"'
        + "Training Data" + '"' + " " + "at (500,15) font Serif size 16 bold");
    lang.addLine("text " + '"' + "hypothesisTxt" + '"' + " " + '"'
        + "Hypothesis" + '"' + " " + "at (10,250) font Serif size 16 bold");
    lang.addLine("text " + '"' + "explanationTxt" + '"' + " " + '"'
        + "Explanation" + '"' + " " + "at (500,260) font Serif size 16 bold");

    lang.addLine("text " + '"' + "descriptionTxt2" + '"' + " " + '"'
        + "Find-S is an algorithm used in machine learning." + '"' + " "
        + "at (500,290) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt3"
        + '"'
        + " "
        + '"'
        + "Thereby Find-S creates an inital hypothesis h0 which is the most specific and does not cover any training examples. "
        + '"' + " " + "at (500,310) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt4"
        + '"'
        + " "
        + '"'
        + "This hypothesis is adapted to the training examples which are classified as positive examples. (Here: Sport? = Yes) "
        + '"' + " " + "at (500,330) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt5"
        + '"'
        + " "
        + '"'
        + "Positive examples are the information you want to learn with Find-S. "
        + '"' + " " + "at (500,350) font Serif size 14");
    lang.addLine("text " + '"' + "descriptionTxt6" + '"' + " " + '"'
        + "Thus Find-S will only stop if all positive examples are covered.  "
        + '"' + " " + "at (500,380) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt7"
        + '"'
        + " "
        + '"'
        + "The final hypothesis which is created by Find-S is a classfier that classifies new information either as positive or negative. "
        + '"' + " " + "at (500,400) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt8"
        + '"'
        + " "
        + '"'
        + "Thereby it can still happen, that even negative information may be classified as positive."
        + '"' + " " + "at (500,420) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt9"
        + '"'
        + " "
        + '"'
        + "In this case you have either to apply your training examples (use more examples, use more different examples) or change the learning algorithm."
        + '"' + " " + "at (500,440) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt1"
        + '"'
        + " "
        + '"'
        + "The complexity of Find-S is based on the number of training examples and the number of attributes."
        + '"' + " " + "at (500,480) font Serif size 14");

    lang.addLine("hide" + " " + '"' + "descriptionTxt1" + '"' + "");
    lang.addLine("hide" + " " + '"' + "descriptionTxt2" + '"' + "");
    lang.addLine("hide" + " " + '"' + "descriptionTxt3" + '"' + "");
    lang.addLine("hide" + " " + '"' + "descriptionTxt4" + '"' + "");
    lang.addLine("hide" + " " + '"' + "descriptionTxt5" + '"' + "");
    lang.addLine("hide" + " " + '"' + "descriptionTxt6" + '"' + "");
    lang.addLine("hide" + " " + '"' + "descriptionTxt7" + '"' + "");
    lang.addLine("hide" + " " + '"' + "descriptionTxt8" + '"' + "");
    lang.addLine("hide" + " " + '"' + "descriptionTxt9" + '"' + "");

    // Create Source Code Object
    SourceCode sc = lang.newSourceCode(new Coordinates(10, 80), "sourceCode",
        null, scProp);
    sc.addCodeLine(
        "1. h = most specific hypothesis in H (covering no examples)", null, 0,
        null); // 0
    sc.addCodeLine("2. for each training example e", null, 0, null);
    sc.addCodeLine("a. if e is negative", null, 1, null);
    sc.addCodeLine(" do nothing", null, 2, null); // 3
    sc.addCodeLine("b. if e is positive", null, 1, null); // 4
    sc.addCodeLine("for each condition c in h", null, 2, null); // 5
    sc.addCodeLine("if c does not cover e", null, 3, null); // 6
    sc.addCodeLine("delete c from h", null, 4, null); // 7

    // Create Counter Object
    lang.addLine("text " + '"' + "complexityTxt" + '"' + " " + '"'
        + "Complexity due to accessing Training Data" + '"' + " "
        + "at (850,25) font Serif size 16 bold");
    TwoValueCounter counterExp = lang.newCounter(trainingDataMtx); // Zaehler
                                                                   // anlegen

    CounterProperties cpExp = new CounterProperties(); // Zaehler-Properties
                                                       // anlegen
    cpExp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // gefuellt...
    cpExp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE); // ...mit Blau
    lang.newCounterView(counterExp, new Coordinates(850, 60), cpExp, true, true);

    lang.addLine("text " + '"' + "headlineTxt" + '"' + " " + '"'
        + "Find-S Algorithm (Machine Learning)" + '"' + " "
        + "at (10,10) font Serif size 20 bold");
    lang.addLine("text " + '"' + "pseudeCodeTxt" + '"' + " " + '"'
        + "Pseudo Code" + '"' + " " + "at (10,40) font Serif size 16 bold");
    lang.addLine("text " + '"' + "trainingDataTxt" + '"' + " " + '"'
        + "Training Data" + '"' + " " + "at (500,15) font Serif size 16 bold");
    lang.addLine("text " + '"' + "hypothesisTxt" + '"' + " " + '"'
        + "Hypothesis" + '"' + " " + "at (10,250) font Serif size 16 bold");
    lang.addLine("text " + '"' + "explanationTxt" + '"' + " " + '"'
        + "Explanation" + '"' + " " + "at (500,260) font Serif size 16 bold");
    // lang.addLine("text " + '"' + "attributesTxt" + '"' + " " + '"'
    // + "  Sky Temp Humidity Windy Water Forecast Sport?" + '"' + " "
    // + "at (500,55) font Serif size 14");

    lang.addLine("text " + '"' + "descriptionTxt2" + '"' + " " + '"'
        + "Find-S is an algorithm used in machine learning." + '"' + " "
        + "at (500,290) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt3"
        + '"'
        + " "
        + '"'
        + "Thereby Find-S creates an inital hypothesis h0 which is the most specific and does not cover any training examples. "
        + '"' + " " + "at (500,310) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt4"
        + '"'
        + " "
        + '"'
        + "This hypothesis is adapted to the training examples which are classified as positive examples. (Here: Sport? = Yes) "
        + '"' + " " + "at (500,330) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt5"
        + '"'
        + " "
        + '"'
        + "Positive examples are the information you want to learn with Find-S. "
        + '"' + " " + "at (500,350) font Serif size 14");
    lang.addLine("text " + '"' + "descriptionTxt6" + '"' + " " + '"'
        + "Thus Find-S will only stop if all positive examples are covered.  "
        + '"' + " " + "at (500,380) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt7"
        + '"'
        + " "
        + '"'
        + "The final hypothesis which is created by Find-S is a classfier that classifies new information either as positive or negative. "
        + '"' + " " + "at (500,400) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt8"
        + '"'
        + " "
        + '"'
        + "Thereby it can still happen, that even negative information may be classified as positive."
        + '"' + " " + "at (500,420) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt9"
        + '"'
        + " "
        + '"'
        + "In this case you have either to apply your training examples (use more examples, use more different examples) or change the learning algorithm."
        + '"' + " " + "at (500,440) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt1"
        + '"'
        + " "
        + '"'
        + "The complexity of Find-S is based on the number of training examples and the number of attributes."
        + '"' + " " + "at (500,480) font Serif size 14");
    lang.nextStep("Start der Animation");
    lang.addLine("hide" + " " + '"' + "descriptionTxt1" + '"' + "");
    lang.addLine("hide" + " " + '"' + "descriptionTxt2" + '"' + "");
    lang.addLine("hide" + " " + '"' + "descriptionTxt3" + '"' + "");
    lang.addLine("hide" + " " + '"' + "descriptionTxt4" + '"' + "");
    lang.addLine("hide" + " " + '"' + "descriptionTxt5" + '"' + "");
    lang.addLine("hide" + " " + '"' + "descriptionTxt6" + '"' + "");
    lang.addLine("hide" + " " + '"' + "descriptionTxt7" + '"' + "");
    lang.addLine("hide" + " " + '"' + "descriptionTxt8" + '"' + "");
    lang.addLine("hide" + " " + '"' + "descriptionTxt9" + '"' + "");

    performFindS(trainingDataMtx, sc, newHypoProp, initalString);
    lastIteration();
    lang.finalizeGeneration();

    return lang.toString();

  }

  public String getName() {
    return "Find-S";
  }

  public String getAlgorithmName() {
    return "Find-S";
  }

  public String getAnimationAuthor() {
    return "Dominik Unzicker, Torben Unzicker";
  }

  public String getDescription() {
    return "Find-S is an algorithm that is used in machine learning."
        + "\n"
        + "Based on a given number of training examples, covering positive and negative examples, FInd-S creates an hypothesis for classifying data."
        + "\n"
        + "The final hypothesis that is created by Find-S is that specific that it just covers all positive training examples.";
  }

  public String getCodeExample() {
    return "h = most specific hypothesis in H (covering no examples)" + "\n"
        + "for each training example e" + "\n" + "   a. if e is negative"
        + "\n" + "       - do nothing" + "\n" + "   b. if e is positive" + "\n"
        + "       - for earch condition c in h" + "\n"
        + "              if c does not cover e" + "\n"
        + "                     delete c from h";
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

  /**
   * performFind-S: Creates an hypothesis for classifying elements
   * 
   * @param trainingData
   *          containing the training data
   * @param codeSupport
   *          the underlying code instance
   * @param hypoProps
   *          including the properties of the hypotheses
   */
  private void performFindS(StringMatrix trainingData, SourceCode codeSupport,
      ArrayProperties hypoProps, String[][] oldHypo)
      throws LineNotExistsException {

    /*
     * for(int i =0;i<trainingData.getNrRows()-1;i++){ for(int
     * j=0;j<trainingData.getNrCols()-1;j++){ trainingData.highlightCell(i, j,
     * null, null); lang.nextStep(); } }
     */

    if (firstIteration) {
      firstIteration = false;
      // Contains data only
      String[][] initOldHypo = new String[trainingData.getNrRows() + 1][trainingData
          .getNrCols() - 1];

      StringArray h0 = lang
          .newStringArray(new Coordinates(30, 290), new String[] { "false",
              "false", "false", "false", "false", "false" }, "", null,
              hypoProps);
      lang.addLine("text " + '"' + "expl1" + '"' + " " + '"' + "h0:" + '"'
          + " " + "at (10,290) font Serif size 14");
      for (int j = 0; j < trainingData.getNrCols() - 1; j++) {
        h0.put(j, "false", null, null);
        initOldHypo[0][j] = h0.getData(j);
      }
      // h=most specific hypothesis in H (covering no example)
      lang.addLine("text " + '"' + "expl1" + '"' + " " + '"'
          + "h0 covers no training example." + '"' + " "
          + "at (500,290) font Serif size 14");
      lang.addLine("text " + '"' + "expl2" + '"' + " " + '"'
          + "Thus it is the most specific hypothesis." + '"' + " "
          + "at (500,310) font Serif size 14");
      codeSupport.highlight(0);
      h0.highlightElem(0, h0.getLength() - 1, null, null);
      lang.nextStep("h0 initialisiert. Training beginnt.");
      lang.addLine("hide" + " " + '"' + "expl1" + '"' + "");
      lang.addLine("hide" + " " + '"' + "expl2" + '"' + "");
      h0.hide();

      performFindS(trainingData, codeSupport, hypoProps, initOldHypo);

    } else {

      for (int i = 0; i < trainingData.getNrRows(); i++) { // Waehle jedes
        // Example

        // HIER: oldHypo in StringArray konvertieren
        int l = 0;
        l = i * 40 + 290;
        String[] oldHypothesisString = new String[trainingData.getNrCols() - 1];
        // System.out.println("OLD NEWHYPO");
        for (int p = 0; p < trainingData.getNrCols() - 1; p++) {
          oldHypothesisString[p] = oldHypo[i][p];
          // System.out.print(oldHypothesisString[p]);
        }

        StringArray oldHypothesis = lang.newStringArray(new Coordinates(30, l),
            oldHypothesisString, "", null, oldHypoProp);

        int k = 0;
        k = i * 40 + 330;
        lang.addLine("text " + '"' + "expl1" + '"' + " " + '"' + "h" + (i + 1)
            + ":" + '"' + " " + "at (10," + k + ") font Serif size 14");
        StringArray newHypo = lang
            .newStringArray(new Coordinates(30, k), new String[] { "false",
                "false", "false", "false", "false", "false" }, "", null,
                hypoProps);

        // for each training example e
        codeSupport.toggleHighlight(0, 1);

        trainingData.highlightCellColumnRange(i, 0,
            trainingData.getNrCols() - 1, null, null);
        lang.addLine("text " + '"' + "expl1" + '"' + " " + '"'
            + "Pick training example e" + '"' + " "
            + "at (500,290) font Serif size 14");
        lang.nextStep("Waehle Trainingsbeispiel " + i);

        lang.addLine("text " + '"' + "expl2" + '"' + " " + '"'
            + "Check if e is a negative training example." + '"' + " "
            + "at (500,290) font Serif size 14");
        lang.addLine("text "
            + '"'
            + "expl3"
            + '"'
            + " "
            + '"'
            + "A training example is negative, if it is classified as a negative example ('No' as class attribute value)."
            + '"' + " " + "at (500,310) font Serif size 14");
        lang.addLine("hide" + " " + '"' + "expl1" + '"' + "");
        codeSupport.highlight(2, 0, false);
        lang.nextStep("Teste ob Beispiel negativ ist.");
        lang.addLine("hide" + " " + '"' + "expl2" + '"' + "");
        lang.addLine("hide" + " " + '"' + "expl3" + '"' + "");

        if (trainingData.getElement(i, trainingData.getNrCols() - 1)
            .equalsIgnoreCase("no") && secondQuestion) {
          // Wird dieses Beispiel von Find-S berücksichtigt? Warum bzw. Warum
          // nicht?
          FillInBlanksQuestionModel qNextExample = new FillInBlanksQuestionModel(
              "qNextExample");
          qNextExample
              .setPrompt("Wird das naechste Beispiel von Find-S beruecksichtigt? Warum bzw. warum nicht?");
          qNextExample
              .addAnswer(
                  "Nein",
                  1,
                  "Es wird nicht beruecksichtigt, da es sich um ein negatives Beispiel handelt. Find-S beruecksichtigt bei der Hypothesenerstellung allerdings nur die Eigenschaften positiver Beispiele.");
          lang.addFIBQuestion(qNextExample);
          secondQuestion = false;
        }
        lang.nextStep("1. Frage: Wird das naechste Beispiel von Find-S beruecksichtigt? Warum bzw. warum nicht?");

        // if e is negative
        if (trainingData.getElement(i, trainingData.getNrCols() - 1)
            .equalsIgnoreCase("no")) {
          lang.addLine("text " + '"' + "expl1" + '"' + " " + '"'
              + "e is a negative training example." + '"' + " "
              + "at (500,290) font Serif size 14");
          lang.addLine("text "
              + '"'
              + "expl2"
              + '"'
              + " "
              + '"'
              + "Thus we do nothing and the hypothesis remains the same as the one before."
              + '"' + " " + "at (500,310) font Serif size 14");
          // Array zwischenspeichern, um es dann ausgeben zu können
          if (i + 1 <= trainingData.getNrRows()) {
            for (int m = 0; m < oldHypo[i].length; m++) {
              oldHypo[i + 1][m] = oldHypo[i][m];
              newHypo.put(m, oldHypo[i][m], null, null);
            }
          } else {
            for (int m = 0; m < oldHypo[i].length; m++) {
              oldHypo[i][m] = oldHypo[i][m];
              newHypo.put(m, oldHypo[i][m], null, null);
            }
          }
          codeSupport.highlight(3, 0, false);
          newHypo.highlightElem(0, newHypo.getLength() - 1, null, null);
          // @TODO: Textausgabe, dass Beispiel negativ ist und deshalb
          // die Hypotheses gleichbleibt
          lang.nextStep();
          lang.addLine("hide" + " " + '"' + "expl1" + '"' + "");
          lang.addLine("hide" + " " + '"' + "expl2" + '"' + "");
          newHypo.hide();
          codeSupport.unhighlight(2, 0, false);
          codeSupport.unhighlight(3, 0, false);
        } else { // close if trainingExp is negative

          lang.addLine("text " + '"' + "expl1" + '"' + " " + '"'
              + "Check if e is a positive training example." + '"' + " "
              + "at (500,290) font Serif size 14");
          lang.addLine("text "
              + '"'
              + "expl2"
              + '"'
              + " "
              + '"'
              + "A training example is positive, if it is classified as a positive example ('Yes' as class attribute value)."
              + '"' + " " + "at (500,310) font Serif size 14");
          lang.addLine("text "
              + '"'
              + "expl3"
              + '"'
              + " "
              + '"'
              + "As e is positive, the current hypothesis has to be applied to fit the positive example."
              + '"' + " " + "at (500,330) font Serif size 14");
          codeSupport.toggleHighlight(2, 4);
          lang.nextStep("Teste ob Beispiel positiv ist.");
          lang.addLine("hide" + " " + '"' + "expl1" + '"' + "");
          lang.addLine("hide" + " " + '"' + "expl2" + '"' + "");
          lang.addLine("hide" + " " + '"' + "expl3" + '"' + "");

          // if e is positive
          if (trainingData.getElement(i, trainingData.getNrCols() - 1)
              .equalsIgnoreCase("yes")) {

            for (int j = 0; j < trainingData.getNrCols() - 1; j++) { // Durchlaufe
                                                                     // jeden
                                                                     // Wert des
                                                                     // Examples

              if (i == 0 && j == 2 && firstQuestion) {
                // Welchen Wert nimmt die Hypothese an der nächsten Stellen an?
                MultipleChoiceQuestionModel qNextValue = new MultipleChoiceQuestionModel(
                    "farbeAbfrage");
                qNextValue
                    .setPrompt("Welchen Wert nimmt die neue Hypothese for dieses Attribut an?");
                qNextValue.addAnswer(
                    "0",
                    "true",
                    0,
                    "Falsch. Die richtige Antwort lautet"
                        + trainingData.getElement(i, j));
                qNextValue.addAnswer(
                    "1",
                    "false",
                    0,
                    "Falsch. Das Beispiel ist positiv, weshalb "
                        + trainingData.getElement(i, j)
                        + " die richtige Antwort ist.");
                qNextValue.addAnswer("2", trainingData.getElement(i, j), 1,
                    "Korrekt. " + trainingData.getElement(i, j)
                        + " ist die richtige Antwort.");
                lang.addMCQuestion(qNextValue);
                firstQuestion = false;
              }

              codeSupport.highlight(5, 0, false);
              lang.addLine("text "
                  + '"'
                  + "expl1"
                  + '"'
                  + " "
                  + '"'
                  + "We take a look at the covered attribute, the condition c, of the hypothesis"
                  + '"' + " " + "at (500,290) font Serif size 14");
              // Set hypoMarker visible
              ArrayMarker hypoMarker = lang.newArrayMarker(oldHypothesis, j,
                  "hypoMarker", null, hypoMarkerProp);
              hypoMarker.move(j, null, null);
              lang.nextStep();

              // HIGHLIGHTEN der StringMatrix
              trainingData.highlightElem(i, j, null, null);
              codeSupport.highlight(6, 0, false);
              lang.addLine("text " + '"' + "expl2" + '"' + " " + '"'
                  + "We compare it with the attribute value of example e."
                  + '"' + " " + "at (500,310) font Serif size 14");

              lang.nextStep("Vergleich von c und e");
              lang.addLine("hide" + " " + '"' + "expl1" + '"' + "");
              lang.addLine("hide" + " " + '"' + "expl2" + '"' + "");

              // In der Hypothese steht false -> Setze Wert des
              // Trainingsbeispiels
              if (oldHypo[i][j].equalsIgnoreCase("false")) {
                // Organize data
                newHypo.put(j, trainingData.getElement(i, j), null, null);
                oldHypo[i + 1][j] = trainingData.getElement(i, j);

                // Print
                lang.addLine("text "
                    + '"'
                    + "expl1"
                    + '"'
                    + " "
                    + '"'
                    + "As the condition c in h does not cover the attribute value of the positive example, we have to delete c from h."
                    + '"' + " " + "at (500,290) font Serif size 14");
                newHypo.highlightElem(j, null, null);
                codeSupport.highlight(7);
                lang.nextStep("Setze den Wert fuer false auf positives Trainingsbeispiel um.");
                lang.addLine("hide" + " " + '"' + "expl1" + '"' + "");
                codeSupport.unhighlight(6);
                codeSupport.unhighlight(7);
              }

              // In der Hypothese steht ein anderer Wert als im
              // Trainingsbeispiel -> Setze True
              if (!oldHypo[i][j].equalsIgnoreCase("false")
                  && !oldHypo[i][j].equalsIgnoreCase(trainingData.getElement(i,
                      j)) && !oldHypo[i][j].equalsIgnoreCase("true")) {
                // Organize data
                newHypo.put(j, "true", null, null);
                oldHypo[i + 1][j] = "true";

                // Print
                lang.addLine("text "
                    + '"'
                    + "expl4"
                    + '"'
                    + " "
                    + '"'
                    + "If h contains a attribute value which is too specific, it needs to be generalized to cover the current training example."
                    + '"' + " " + "at (500,290) font Serif size 14");
                lang.addLine("text " + '"' + "expl5" + '"' + " " + '"'
                    + "The attribute value is generalized by setting it 'true'"
                    + '"' + " " + "at (500,310) font Serif size 14");
                newHypo.highlightElem(j, null, null);
                codeSupport.highlight(7);
                lang.nextStep("Generalisierung des Attributs durch Setzen von 'true'");
                lang.addLine("hide" + " " + '"' + "expl4" + '"' + "");
                lang.addLine("hide" + " " + '"' + "expl5" + '"' + "");
                codeSupport.unhighlight(6);
                codeSupport.unhighlight(7);
              }

              // In der Hypothese steht bereits true
              if (oldHypo[i][j].equalsIgnoreCase("true")) {
                // Organize data
                newHypo.put(j, "true", null, null);
                oldHypo[i + 1][j] = "true";

                // Print
                lang.addLine("hide" + " " + '"' + "expl1" + '"' + "");
                lang.addLine("text "
                    + '"'
                    + "expl4"
                    + '"'
                    + " "
                    + '"'
                    + "h still contains 'true' for this attribute. Thus we adapt it to our new hypothesis as well to cover the example."
                    + '"' + " " + "at (500,290) font Serif size 14");
                lang.addLine("text " + '"' + "expl5" + '"' + " " + '"'
                    + "The attribute value is generalized by setting it 'true'"
                    + '"' + " " + "at (500,310) font Serif size 14");
                newHypo.highlightElem(j, null, null);
                lang.nextStep("Attributwert bleibt 'true'");
                lang.addLine("hide" + " " + '"' + "expl1" + '"' + "");
                lang.addLine("hide" + " " + '"' + "expl4" + '"' + "");
                lang.addLine("hide" + " " + '"' + "expl5" + '"' + "");
              }

              // Wert der Hypothese ist gleich dem Trainingsbeispiel
              // -> übernehme alten Hypothesenwert
              if (oldHypo[i][j].equalsIgnoreCase(trainingData.getElement(i, j))) {
                // Organize data
                newHypo.put(j, oldHypo[i][j], null, null);
                oldHypo[i + 1][j] = oldHypo[i][j];

                // Print
                lang.addLine("text "
                    + '"'
                    + "expl3"
                    + '"'
                    + " "
                    + '"'
                    + "The attribute value of the training example is still covered by the old hyptothesis. Thus we adopt this to our new hypothesis."
                    + '"' + " " + "at (500,290) font Serif size 14");
                newHypo.highlightElem(j, null, null);
                codeSupport.highlight(7);
                lang.nextStep("Der Attributwert bleibt unveraendert.");
                codeSupport.unhighlight(6);
                codeSupport.unhighlight(7);
                lang.addLine("hide" + " " + '"' + "expl3" + '"' + "");
              }
              // trainingData.unhighlightCell(i, j, null, null);
              hypoMarker.hide();
            }// Durchlaufe jeden Wert des Examples

            if (i < trainingData.getNrRows() - 1) {
              newHypo.hide();
            }

          }// Ende else negative Example
        } // Ende Durchlaufe jeden Wert des Examples

        trainingData.unhighlightCellColumnRange(i, 0,
            trainingData.getNrCols() - 1, null, null);

        codeSupport.unhighlight(4);
        codeSupport.unhighlight(5);

        // Variable für Old Hypothesis
        oldHypoString = oldHypoString + " " + i + " ";
        for (int b = 0; b < trainingData.getNrCols() - 1; b++) {
          oldHypoString = oldHypoString + " " + oldHypo[i][b];
        }

        variables.declare("String", "OldHypothesis", oldHypoString);

      }// Ende for: Durchlaufe ein Example

      codeSupport.unhighlight(1, 0, false);
      codeSupport.unhighlight(2, 0, false);
      codeSupport.unhighlight(3, 0, false);

      // System.out.println("oldHypo-Array");
      // for (int a = 0; a < oldHypo.length; a++) {
      // System.out.print(a + ": ");
      // for (int b = 0; b < oldHypo[a].length; b++) {
      // System.out.print(oldHypo[a][b] + " ");
      // }
      // System.out.println("");
      // }
      //
      // System.out.println("trainingData");
      // for (int a = 0; a < trainingData.getNrRows(); a++) {
      // System.out.print(a + ": ");
      // for (int b = 0; b < trainingData.getNrCols() - 1; b++) {
      // System.out.print(trainingData.getElement(a, b) + " ");
      // }
      // System.out.println("");
      // }
    } // Close else firstIteration

  } // Close FindS

  private void lastIteration() {
    lang.addLine("text "
        + '"'
        + "expl1"
        + '"'
        + " "
        + '"'
        + "We have now created a hypothesis that is the most specific hypothesis for all training examples."
        + '"' + " " + "at (500,290) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "expl2"
        + '"'
        + " "
        + '"'
        + "The final hypothesis created by Find-S is just not excluding any positive example in the training data."
        + '"' + " " + "at (500,310) font Serif size 14");// TODO
    // Auto-generated
    // method
    // stub
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {

    trainingDataValidate = (String[][]) primitives.get("trainingData");

    for (int i = 0; i < trainingDataValidate.length; i++) {
      // modified by Dominik Fischer, fixing tautology. Original:
      // if
      // (!trainingDataValidate[i][trainingDataValidate.length-1].equalsIgnoreCase("no")){
      // throw new
      // IllegalArgumentException("The last attribute classifies the training example. Please specifiy the class of each example by adding 'yes' or 'no' to the class attribute (the last element of a training example).");
      // }
      // if(!trainingDataValidate[i][trainingDataValidate.length-1].equalsIgnoreCase("yes")){
      // throw new
      // IllegalArgumentException("The last attribute classifies the training example. Please specifiy the class of each example by adding 'yes' or 'no' to the class attribute (the last element of a training example).");
      // }
      final String[] row = trainingDataValidate[i];
      final String lastCell = row[row.length - 1];
      if (!lastCell.equalsIgnoreCase("yes") && !lastCell.equalsIgnoreCase("no")) {
        throw new IllegalArgumentException(
            "The last attribute classifies the training example. Please specifiy the class of each example by adding 'yes' or 'no' to the class attribute (the last element of a training example).");
      }
    }
    return true;
  }

}