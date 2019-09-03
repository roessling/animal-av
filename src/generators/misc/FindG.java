package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;

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
public class FindG implements ValidatingGenerator {
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

  public boolean                finished;
  public boolean                noHypoPossible;
  public boolean                alreadyHighlighted;

  private int                   lastStop;
  private boolean               notSet;
  private int                   indexPositiveExp;
  private boolean               hadThisNegative;
  public ArrayMarker            hypoMarker;

  public String[][]             psoExpNew;
  public String[][]             posExp;
  public StringArray            oldHypothesis;

  public void init() {
    lang = new AnimalScript("Find-G", "Dominik Unzicker, Torben Unzicker", 800,
        600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    init();
    validateInput(props, primitives);

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

    finished = false;
    noHypoPossible = false;

    lastStop = 0;
    notSet = true;
    indexPositiveExp = -1;
    hadThisNegative = false;

    // Initialize trainingData Matrix
    StringMatrix trainingDataMtx = lang.newStringMatrix(new Coordinates(500,
        100), trainingData, "trainingDataMtx", null, trainingDataProp);

    psoExpNew = new String[countElements(trainingDataMtx,
        trainingDataMtx.getNrCols() - 1, "yes")][trainingDataMtx.getNrCols() - 1];
    posExp = new String[countElements(trainingDataMtx,
        trainingDataMtx.getNrCols() - 1, "yes")][trainingDataMtx.getNrCols() - 1];

    // Set Variables
    variables = lang.newVariables();

    // Create required Text output
    lang.addLine("text " + '"' + "pseudeCodeTxt" + '"' + " " + '"'
        + "Pseudo Code" + '"' + " " + "at (10,40) font Serif size 16 bold");
    lang.addLine("text " + '"' + "trainingDataTxt" + '"' + " " + '"'
        + "Training Data" + '"' + " " + "at (500,15) font Serif size 16 bold");
    lang.addLine("text " + '"' + "hypothesisTxt" + '"' + " " + '"'
        + "Hypothesis" + '"' + " " + "at (10,250) font Serif size 16 bold");
    lang.addLine("text " + '"' + "explanationTxt" + '"' + " " + '"'
        + "Explanation" + '"' + " " + "at (500,260) font Serif size 16 bold");

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
    sc.addCodeLine("h = most general hypothesis in H (covering no examples)",
        null, 0, null);
    sc.addCodeLine(" 2. for each training example e", null, 1, null);
    sc.addCodeLine("   a. if e is positive", null, 2, null);
    sc.addCodeLine("     - do nothing", null, 3, null);
    sc.addCodeLine("   b. if e is negative", null, 4, null);
    sc.addCodeLine("     - for some condition c in e", null, 5, null);
    sc.addCodeLine("       ~ if c is not part of h", null, 6, null);
    sc.addCodeLine("         + add a condition that negates c", null, 7, null);
    sc.addCodeLine("           and covers all previous", null, 8, null);
    sc.addCodeLine("           positive examples to h", null, 9, null);

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
        + "Find-G Algorithm (Machine Learning)" + '"' + " "
        + "at (10,10) font Serif size 20 bold");
    lang.addLine("text " + '"' + "trainingDataTxt" + '"' + " " + '"'
        + "Training Data" + '"' + " " + "at (500,15) font Serif size 16 bold");
    lang.addLine("text " + '"' + "hypothesisTxt" + '"' + " " + '"'
        + "Hypothesis" + '"' + " " + "at (10,250) font Serif size 16 bold");
    lang.addLine("text " + '"' + "explanationTxt" + '"' + " " + '"'
        + "Explanation" + '"' + " " + "at (500,260) font Serif size 16 bold");

    lang.addLine("text " + '"' + "descriptionTxt1" + '"' + " " + '"'
        + "Find-G is an algorithm used in machine learning." + '"' + " "
        + "at (500,290) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt2"
        + '"'
        + " "
        + '"'
        + "Thereby Find-G creates an inital hypothesis which is the most general and covers all training examples. "
        + '"' + " " + "at (500,320) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt3"
        + '"'
        + " "
        + '"'
        + "This hypothesis is adapted to the training examples which are classified as negative examples. "
        + '"' + " " + "at (500,340) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt4"
        + '"'
        + " "
        + '"'
        + "Thereby some conditions are removed from the hypothesis in a way to ensure that no negative example gets classified anymore. "
        + '"' + " " + "at (500,360) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt5"
        + '"'
        + " "
        + '"'
        + "Thus Find-G will only stop if alo negative example is covered anymore.  "
        + '"' + " " + "at (500,380) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt6"
        + '"'
        + " "
        + '"'
        + "The final hypothesis which is created by Find-G is a classfier that classifies new information either as positive or negative. "
        + '"' + " " + "at (500,400) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt7"
        + '"'
        + " "
        + '"'
        + "Thereby it can still happen, that even negative information may be classified as positive while being the most general hypothesis to classify informatik."
        + '"' + " " + "at (500,420) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt8"
        + '"'
        + " "
        + '"'
        + "- in this case you have either to apply your training examples (use more examples, use more different examples) or change the learning algorithm."
        + '"' + " " + "at (500,440) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "descriptionTxt9"
        + '"'
        + " "
        + '"'
        + "The complexity of Find-G is based on the number of training examples and the number of attributes."
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

    performFindG(trainingDataMtx, sc, newHypoProp, initalString);
    lastIteration();
    lang.finalizeGeneration();

    return lang.toString();

  }

  /**
   * performFind-G: Creates an hypothesis for classifying elements
   * 
   * @param trainingData
   *          containing the training data
   * @param codeSupport
   *          the underlying code instance
   * @param hypoProps
   *          including the properties of the hypotheses
   */
  @SuppressWarnings("unused")
  private void performFindG(StringMatrix trainingData, SourceCode codeSupport,
      ArrayProperties hypoProps, String[][] oldHypo)
      throws LineNotExistsException {

    if (firstIteration) {
      firstIteration = false;
      // Contains data only
      String[][] initOldHypo = new String[trainingData.getNrRows() + 1][trainingData
          .getNrCols() - 1];

      StringArray h0 = lang.newStringArray(new Coordinates(30, 290),
          new String[] { "true", "true", "true", "true", "true", "true" }, "",
          null, hypoProps);
      lang.addLine("text " + '"' + "expl1" + '"' + " " + '"' + "h0:" + '"'
          + " " + "at (10,290) font Serif size 14");
      for (int j = 0; j < trainingData.getNrCols() - 1; j++) {
        h0.put(j, "true", null, null);
        initOldHypo[0][j] = h0.getData(j);
      }
      // h=most specific hypothesis in H (covering no example)
      lang.addLine("text " + '"' + "expl1" + '"' + " " + '"'
          + "h0 covers all training examples." + '"' + " "
          + "at (500,290) font Serif size 14");
      lang.addLine("text " + '"' + "expl2" + '"' + " " + '"'
          + "Thus it is the most general hypothesis." + '"' + " "
          + "at (500,310) font Serif size 14");
      codeSupport.highlight(0);
      h0.highlightElem(0, h0.getLength() - 1, null, null);
      lang.nextStep("Training begins");
      lang.addLine("hide" + " " + '"' + "expl1" + '"' + "");
      lang.addLine("hide" + " " + '"' + "expl2" + '"' + "");
      h0.hide();
      performFindG(trainingData, codeSupport, hypoProps, initOldHypo);

    } else {

      for (int i = 0; i < trainingData.getNrRows(); i++) { // Wähle jedes
                                                           // Example

        if (trainingData.getElement(i, trainingData.getNrCols() - 1)
            .equalsIgnoreCase("no") && secondQuestion) {
          // Wird dieses Beispiel von Find-G berücksichtigt? Warum bzw. Warum
          // nicht?
          FillInBlanksQuestionModel qNextExample = new FillInBlanksQuestionModel(
              "qNextExample");
          qNextExample
              .setPrompt("Wird das nächste Beispiel von Find-G berücksichtigt? Warum bzw. warum nicht?");
          qNextExample
              .addAnswer(
                  "Ja",
                  1,
                  "Es wird berücksichtigt, da es sich um ein negatives Beispiel handelt. Find-G berücksichtigt bei der Hypothesenerstellung die Eigenschaften negativer Beispiele und versucht diese auszuschließen.");
          lang.addFIBQuestion(qNextExample);
          secondQuestion = false;
        }

        // HIER: oldHypo in StringArray konvertieren
        int l = 0;
        l = i * 40 + 290;
        String[] oldHypothesisString = new String[trainingData.getNrCols() - 1];

        for (int p = 0; p < trainingData.getNrCols() - 1; p++) {
          oldHypothesisString[p] = oldHypo[i][p];
        }

        oldHypothesis = lang.newStringArray(new Coordinates(30, l),
            oldHypothesisString, "", null, oldHypoProp);

        int k = 0;
        k = i * 40 + 330;
        lang.addLine("text " + '"' + "expl1" + '"' + " " + '"' + "h" + (i + 1)
            + ":" + '"' + " " + "at (10," + k + ") font Serif size 14");
        StringArray newHypo = lang.newStringArray(new Coordinates(30, k),
            new String[] { "true", "true", "true", "true", "true", "true" },
            "", null, hypoProps);
        newHypo.hide();
        // for each training example e
        codeSupport.toggleHighlight(0, 1);

        trainingData.highlightCellColumnRange(i, 0,
            trainingData.getNrCols() - 1, null, null);
        lang.addLine("text " + '"' + "expl1" + '"' + " " + '"'
            + "Pick training example e" + '"' + " "
            + "at (500,290) font Serif size 14");
        lang.nextStep("Decide about classification of example e");

        lang.addLine("text " + '"' + "expl2" + '"' + " " + '"'
            + "Check if e is a positive training example." + '"' + " "
            + "at (500,290) font Serif size 14");
        lang.addLine("text "
            + '"'
            + "expl3"
            + '"'
            + " "
            + '"'
            + "A training example is positive, if it is classified as a positive example ('Yes' as class attribute value)."
            + '"' + " " + "at (500,310) font Serif size 14");
        lang.addLine("hide" + " " + '"' + "expl1" + '"' + "");
        codeSupport.highlight(2, 0, false);

        lang.nextStep("Checked if e is a positive example");
        lang.addLine("hide" + " " + '"' + "expl2" + '"' + "");
        lang.addLine("hide" + " " + '"' + "expl3" + '"' + "");

        // if e is positive
        if (trainingData.getElement(i, trainingData.getNrCols() - 1)
            .equalsIgnoreCase("yes")) {

          lang.addLine("text " + '"' + "expl1" + '"' + " " + '"'
              + "e is a positive training example." + '"' + " "
              + "at (500,290) font Serif size 14");
          lang.addLine("text "
              + '"'
              + "expl2"
              + '"'
              + " "
              + '"'
              + "Thus we do nothing and the hypothesis remains the same as the one before."
              + '"' + " " + "at (500,310) font Serif size 14");

          codeSupport.highlight(3, 0, false);

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

          newHypo.highlightElem(0, newHypo.getLength() - 1, null, null);

          lang.nextStep("Created new hypothesis");
          lang.addLine("hide" + " " + '"' + "expl1" + '"' + "");
          lang.addLine("hide" + " " + '"' + "expl2" + '"' + "");
          codeSupport.unhighlight(2, 0, false);
          codeSupport.unhighlight(3, 0, false);

        } else { // close if trainingExp is positive

          lang.addLine("text " + '"' + "expl1" + '"' + " " + '"'
              + "Check if e is a negative training example." + '"' + " "
              + "at (500,290) font Serif size 14");
          lang.addLine("text "
              + '"'
              + "expl2"
              + '"'
              + " "
              + '"'
              + "A training example is negative, if it is classified as a negative example ('No' as class attribute value)."
              + '"' + " " + "at (500,310) font Serif size 14");
          lang.addLine("text "
              + '"'
              + "expl3"
              + '"'
              + " "
              + '"'
              + "As e is negative, the current hypothesis has to be applied to fit the positive example."
              + '"' + " " + "at (500,330) font Serif size 14");
          codeSupport.toggleHighlight(2, 4);
          lang.nextStep("Check if es is a negative example");
          lang.addLine("hide" + " " + '"' + "expl1" + '"' + "");
          lang.addLine("hide" + " " + '"' + "expl2" + '"' + "");
          lang.addLine("hide" + " " + '"' + "expl3" + '"' + "");

          // if e is negative
          if (trainingData.getElement(i, trainingData.getNrCols() - 1)
              .equalsIgnoreCase("no")) {

            // System.out.println("NEGATIVES BEISPIEL ENTDECKT: "+
            // trainingData.getElement(i, trainingData.getNrCols() - 1));

            for (int j = 0; j < trainingData.getNrCols() - 1; j++) { // Durchlaufe
                                                                     // jeden
                                                                     // Example
                                                                     // Wert

              if (notSet) {
                lastStop = 0;
              }

              // Wenn das Beispiel sich in einem Wert von den bisherigen
              // positiven Beispielen unterscheidet
              // lastStop = ist das Beispiel bei dem zuletzt die Hypothese
              // funktioniert hat
              // bis i, dem aktuellen Beispiel
              for (int u = lastStop; u < i; u++) {

                // System.out.println("Betrachetes Beispiel: "+u);

                // Betrachte alle bisherigen positiven Beispiele
                if (trainingData.getElement(u, trainingData.getNrCols() - 1)
                    .equalsIgnoreCase("yes")) {
                  for (int o = 0; o < trainingData.getNrCols() - 1; o++) {
                    // Unterscheidet sich das aktuelle Element mit Klasse "no"
                    // von dern anderen positiven Beispielen?
                    if (!trainingData.getElement(i, o).equalsIgnoreCase(
                        trainingData.getElement(u, o))
                        && !finished) {

                      // System.out.println("trainingsElement Negativ: "+trainingData.getElement(i,
                      // j));
                      // System.out.println("Letztes positives: "+trainingData.getElement(u,o));

                      // Organize data
                      oldHypo[i + 1][o] = trainingData.getElement(u, o); // Setze
                                                                         // Wert
                                                                         // des
                                                                         // positiven
                                                                         // Beispiels
                      newHypo.put(o, oldHypo[i + 1][o], null, null);
                      notSet = false;
                      lastStop = u + 1;
                      finished = true;

                      // Fülle den Rest der neuen Hypothese mit "true"
                      for (int z = o + 1; z < trainingData.getNrCols() - 1; z++) {
                        oldHypo[i + 1][z] = oldHypo[i][z];
                        newHypo.put(z, oldHypo[i + 1][z], null, null);
                        newHypo.highlightCell(z, null, null);
                      }
                    } else {
                      // Fülle den Rest der neuen Hypothese mit "true"
                      for (int z = o; z < trainingData.getNrCols() - 1; z++) {
                        oldHypo[i + 1][z] = oldHypo[i][z];
                        newHypo.put(z, oldHypo[i + 1][z], null, null);
                        newHypo.highlightCell(z, null, null);
                      }
                    }

                    // Print data
                    hypoMarker = lang.newArrayMarker(oldHypothesis, o,
                        "hypoMarker", null, hypoMarkerProp);
                    hypoMarker.move(o, null, null);
                    break;
                  }

                } else if (trainingData.getElement(u,
                    trainingData.getNrCols() - 1).equalsIgnoreCase("no")
                    && !hadThisNegative) {
                  if (!noHypoPossible) {
                    for (int t = 0; t < i; t++) {
                      // Lege Zwischenspeicher fuer alle bisher positiven
                      // Beispiele an
                      // Schreibe "wert" für alle pos. mit gleichem wert,
                      // ansonsten "fail"
                      // System.out.println("Zwischenspeicher - positive Beispiele:");
                      if (trainingData.getElement(t,
                          trainingData.getNrCols() - 1).equalsIgnoreCase("yes")) {
                        indexPositiveExp++;
                        for (int gh = 0; gh < trainingData.getNrCols() - 1; gh++) {
                          posExp[indexPositiveExp][gh] = trainingData
                              .getElement(t, gh);
                          // System.out.print(posExp[indexPositiveExp][gh]+" ");
                        }
                      }

                      indexPositiveExp = 0;

                      // Array mit allen positiven Elementen wobei das letzte
                      // Element
                      // fail = Werte sind unterschiedlich
                      // 'wert' = alle positiven haben den gleichen Wert für das
                      // Attribut
                      // System.out.println("posExpNew");
                      for (int iu = 0; iu < posExp.length; iu++) {
                        for (int ou = 0; ou < trainingData.getNrCols() - 1; ou++) {
                          if (iu > 0) {
                            if (posExp[iu][ou]
                                .equalsIgnoreCase(psoExpNew[iu - 1][ou])) {
                              psoExpNew[iu][ou] = posExp[iu][ou];
                            } else if (!posExp[iu][ou]
                                .equalsIgnoreCase(posExp[iu - 1][ou])) {
                              psoExpNew[iu][ou] = "fail";
                            }
                          } else {
                            psoExpNew[0][ou] = posExp[0][ou];
                          }
                          // System.out.print(psoExpNew[iu][ou]+" ");
                        }
                      }

                      // Vergleichen, ob es positive mit gemeinsamem
                      // Attributwert gibt
                      // und diese noch nicht in der hypothese sind
                      System.out.println("Laenge des positiven Arrays: "
                          + posExp[0].length);
                      for (int jl = 0; jl < trainingData.getNrCols() - 1; jl++) {
                        // System.out.println("i: "+i);
                        // System.out.println("o: "+o);
                        // System.out.println("jl: "+jl);
                        // System.out.println("psoExpNew.length: "+psoExpNew.length);
                        if (!psoExpNew[psoExpNew.length - 1][jl]
                            .equalsIgnoreCase("fail")
                            && oldHypo[i][jl].equalsIgnoreCase("true")) {
                          if (i + 1 < trainingData.getNrRows()) {
                            oldHypo[i + 1][jl] = psoExpNew[psoExpNew.length - 1][jl];
                            newHypo.put(jl, oldHypo[i + 1][jl], null, null);
                          } else {
                            oldHypo[i][jl] = psoExpNew[psoExpNew.length - 1][jl];
                            newHypo.put(jl, oldHypo[i][jl], null, null);
                          }
                        }
                      }
                    }
                  } // close noHypoPosible
                } // close equals("no")

                if (trainingData.getElement(i, trainingData.getNrCols() - 1)
                    .equalsIgnoreCase("yes") && i > 0) {
                  // System.out.println("Wir sind drin.");
                  for (int o = 0; o < trainingData.getNrCols() - 1; o++) {
                    if (!oldHypo[i][o].equalsIgnoreCase(trainingData
                        .getElement(u, o))
                        && !oldHypo[i][o].equalsIgnoreCase("true")) {

                      for (int op = 0; op < trainingData.getNrCols() - 1; op++) {
                        oldHypo[i + 1][op] = "?";
                      }
                      // System.out.println("KEINE HYPOTHESE MOEGLICH! Verwende GSet oder wähle andere Condition.");
                      noHypoPossible = true;
                    }
                  }
                }// Close if FlaseHypo->("yes)&&i>0
              } // Close for(u<lastStop)

              // System.out.print("BREAK3");
              // if(finished) break;
              hadThisNegative = true;

              lastStop = i - 1;
              finished = false;

              trainingData.highlightElem(i, j, null, null);
              lang.addLine("hide" + " " + '"' + "expl15" + '"' + "");

            }// Durchlaufe jeden Wert des Examples
            codeSupport.highlight(5, 0, false);
            lang.addLine("text "
                + '"'
                + "expl2"
                + '"'
                + " "
                + '"'
                + "Here we mark the condition c in h to show the relevant attribute (as StringMatrix is not working correctly)"
                + '"' + " " + "at (500,290) font Serif size 14");
            lang.nextStep("Get relevant attribute of c to change");

            lang.addLine("hide" + " " + '"' + "expl2" + '"' + "");
            codeSupport.highlight(6, 0, false);

            lang.addLine("text " + '"' + "expl2" + '"' + " " + '"'
                + "c is not a part of h." + '"' + " "
                + "at (500,290) font Serif size 14");

            lang.nextStep("delete c from h");
            lang.addLine("text "
                + '"'
                + "expl3"
                + '"'
                + " "
                + '"'
                + "Thus we have to add a condition that negates c and still fulfills the positive examples."
                + '"' + " " + "at (500,290) font Serif size 14");

            // Stelle hypohthese dar
            newHypo.highlightElem(0, newHypo.getLength() - 1, null, null);
            codeSupport.highlight(7, 0, false);
            codeSupport.highlight(8, 0, false);
            codeSupport.highlight(9, 0, false);

            lang.addLine("hide" + " " + '"' + "hypoMarker" + '"' + "");
            lang.addLine("hide" + " " + '"' + "expl2" + '"' + "");
            lang.nextStep("Found new hypothesis");
            lang.addLine("hide" + " " + '"' + "expl3" + '"' + "");
            newHypo.hide();

            lang.addLine("hide" + " " + '"' + "expl2" + '"' + "");
            lang.addLine("hide" + " " + '"' + "expl3" + '"' + "");
            lang.addLine("hide" + " " + '"' + "expl1" + '"' + "");
            lang.addLine("hide" + " " + '"' + "expl15" + '"' + "");
            codeSupport.highlight(7, 0, false);
            codeSupport.highlight(8, 0, false);
            codeSupport.highlight(9, 0, false);

            // newHypo.highlightElem(0, newHypo.getLength() - 1, null,null);

          }// Ende else negative Example
        } // Ende Durchlaufe jeden Wert des Examples

        trainingData.unhighlightCellColumnRange(i, 0,
            trainingData.getNrCols() - 1, null, null);

        codeSupport.unhighlight(4);
        codeSupport.unhighlight(5);
        codeSupport.unhighlight(6);
        codeSupport.unhighlight(7);
        codeSupport.unhighlight(8);
        codeSupport.unhighlight(9);
        // lang.nextStep();

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

  } // Close FindG

  public String getName() {
    return "Find-G";
  }

  public String getAlgorithmName() {
    return "Find-G";
  }

  public String getAnimationAuthor() {
    return "Dominik Unzicker, Torben Unzicker";
  }

  public String getDescription() {
    return "Find-G is an algorithm that is used in machine learning."
        + "\n"
        + "Based on a given number of training examples, covering positive and negative examples, Find-G creates a hypothesis for classifying data."
        + "\n"
        + "The final hypothesis that is created by Find-G is as general as possible to cover no negative examples.";
  }

  public String getCodeExample() {

    return "h = most general hypothesis in H (covering no examples)" + "\n"
        + " 2. for each training example e" + "\n" + "   a. if e is positive"
        + "\n" + "     - do nothing" + "\n" + "   b. if e is negative" + "\n"
        + "     - for some condition c in e" + "\n"
        + "       ~ if c is not part of h" + "\n"
        + "           add a condition that negates c" + "\n"
        + "           and covers all previous" + "\n"
        + "           positive examples to h" + "\n"
        + "               delete c from h";
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

  private void lastIteration() {
    lang.addLine("text "
        + '"'
        + "expl1"
        + '"'
        + " "
        + '"'
        + "We have now created a hypothesis that is the most general hypothesis for all training examples to classify new information."
        + '"' + " " + "at (500,290) font Serif size 14");
    lang.addLine("text "
        + '"'
        + "expl2"
        + '"'
        + " "
        + '"'
        + "The final hypothesis created by Find-G is just not excluding any positive example in the training data."
        + '"' + " " + "at (500,310) font Serif size 14");
  }

  public int countElements(StringMatrix array, int column, String searched) {

    int returnValue = 0;

    for (int i = 0; i < array.getNrRows(); i++) {
      if (array.getElement(i, column).equalsIgnoreCase(searched))
        returnValue++;
    }

    return returnValue;
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
