package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import algoanim.primitives.IntArray;
import algoanim.primitives.Point;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

import java.util.Hashtable;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;

/**
 * This class implements the animation
 * "parallel summation of an array using reduction".
 * 
 * @author Tobias Raffel
 * @version 0.9
 */
public class LoopParallelizationReduction implements Generator {

  // Algorithm-specific variables
  CyclicBarrier        barrier;
  private Worker[]             threads;
  int                  threadc;

  // Animation-specific variables
  int[]                data;
  Language             lang;

  private Font                 fontContent;
  Font                 fontCode;
  private Font                 fontHeadline;
  Font                 fontTitle;
  private Font                 fontData;

  Color                        colorActive, colorInActive, colorProcessed,
      colorCodeHighlight;

  private SourceCodeProperties scProps;
  private ArrayProperties      inputProps, threadProps;
  private IntArray             inputData;
  Legend               legend;

  // ONLY FOR FRAMEWORK
  public LoopParallelizationReduction() {
  }

  // EMPTY BECAUSE OF INITIALIZATION ISSUES AT inputData
  public void init() {
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    threadc = (Integer) primitives.get("Anzahl CPUs");
    data = (int[]) primitives.get("Eingabe");
    colorActive = (Color) primitives.get("Farbe: aktiver Thread");
    colorInActive = (Color) primitives.get("Farbe: inaktiver Thread");
    colorProcessed = (Color) primitives.get("Farbe: verarbeiteter Wert");
    colorCodeHighlight = (Color) primitives.get("Farbe: Code (hervorgehoben)");
    Font font = (Font) primitives.get("Schriftart");
    String s = font.getFontName();

    lang = new AnimalScript("Schleifenparallelisierung", "Tobias Raffel", 800,
        600);

    // Language-stepMode
    lang.setStepMode(true);

    // Animation offset-anchor
    PointProperties pp = new PointProperties();
    Point p = lang.newPoint(new Coordinates(20, 40), "O", null, pp);
    p.hide();

    // The fonts
    fontContent = new Font(s, Font.BOLD, 16);
    fontCode = new Font("Monospaced", Font.PLAIN, 12);
    fontHeadline = new Font(s, Font.BOLD, 30);
    fontTitle = new Font(s, Font.BOLD, 20);
    fontData = new Font("Monospaced", Font.BOLD, 12);

    // The property-objects
    scProps = createSourceCodeProperties(fontCode, colorCodeHighlight,
        Color.BLACK);
    inputProps = createArrayProperties(Color.BLACK, Color.WHITE, Boolean.TRUE,
        Color.BLACK, colorInActive, colorProcessed, fontData);
    threadProps = createArrayProperties(Color.BLACK, colorActive, Boolean.TRUE,
        Color.BLACK, colorProcessed, colorInActive, fontData);

    // Initialize the inputData with data
    inputData = lang.newIntArray(new Offset(0, 500, "O",
        AnimalScript.DIRECTION_NW), data, "ID", null, inputProps);
    inputData.hide();

    startSlideshow();

    return lang.toString();
  }

  public String getName() {
    return "Reduktion";
  }

  public String getAlgorithmName() {
    return "Schleifenparallelisierung";
  }

  public String getAnimationAuthor() {
    return "Tobias Raffel";
  }

  public String getDescription() {

    String description = "Im folgenden wird vorgestellt, wie man vorgehen kann, wenn man ohne Hilfsmittel, wie OpenMP,\n"
        + "Schleifen parallelisieren moechte, bei denen die zu verarbeitenden Daten nicht unabhaengig\n"
        + "Schleifen parallelisieren moechte, bei denen die zu verarbeitenden Daten nicht unabhaengig\n"
        + "voneinander sind.";

    return description;
  }

  public String getCodeExample() {

    String codeExample = "// Parallele Faltung eines Arrays mit OpenMP -- in Java so nicht moeglich \n"
        + "int result = 0; \n#pragma omp parallel for reduction (+ : result) \n"
        + "for(int i = 0; i < arr.length; i++)\n"
        + "{\n"
        + "    result += arr[i];\n" + "}\n";

    return codeExample;

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
   * Starts the whole animation
   */
  public void startSlideshow() {

    // The global headline
    final Text textHeadline = lang.newText(new Offset(0, 0, "O",
        AnimalScript.DIRECTION_NW), "Schleifenparallelisierung - Reduktion",
        "HGL", null);
    textHeadline.setFont(fontHeadline, null, null);

    // The slide-local title
    Text textTitle = lang.newText(new Offset(10, 20, "O",
        AnimalScript.DIRECTION_NW), "NULL", "HSL", null);
    textTitle.setFont(fontTitle, null, null);

    // First slide: Introduction
    textTitle.setText("-> Einleitung", null, null);
    showIntroduction();

    // Second slide: Hints
    textTitle.setText("-> Hinweise - Darauf muss man achten", null, null);
    showHints();

    // Third slide: Sequential animation
    textTitle.setText(
        "-> Visualisierung und Java-Pseudocode(ausschnitt) - sequenziell",
        null, null);
    showSequentialAnimation();

    // Fourth slide: Parallel animation
    textTitle.setText(
        "-> Visualisierung und Java-Pseudocode(ausschnitt) - parallel", null,
        null);
    showParallelAnimation();

    // Sixth slide: Summary
    textTitle.setText("-> Vergleich & Bemerkungen", null, null);
    showSummary();
  }

  /**
   * Shows the introduction-slide
   */
  private void showIntroduction() {

    // The whole content...
    final String stringContent0 = "Der Trend shared-memory-Systeme mit immer mehr identischen Recheneinheiten auszustatten,";
    final String stringContent1 = "stellt an Programmierer die Forderung Programcode parallel ausfuehrbar zu modellieren.";
    final String stringContent2 = "Dabei gelangt man schnell an Grenzen. Daten muessen moeglichst unabhaengig voneinander sein ";
    final String stringContent3 = "und das Ergebnis sollte nicht von einer festen Reihenfolge abhaengen.";
    final String stringContent4 = "Im Folgenden soll am Beispiel einer einfachen Faltung von Arraywerten mittels Addition illustriert werden,";
    final String stringContent5 = "dass man einigen Aufwand betreiben muss um dieses vergleichsweise einfache Verfahren zu parallelisieren.";
    final String stringContent6 = "Es wird auch veranschaulicht, dass der Aufwand trotzdem lohnt.";

    // ...gathered in String arrays
    final String[] content1 = { stringContent0, stringContent1, stringContent2,
        stringContent3 };
    final String[] content2 = { stringContent4, stringContent5, stringContent6 };

    Vector<Primitive> content = new Vector<Primitive>(content1.length
        + content2.length);

    // Show
    content.addAll(showText(content1, 0, 100, 20, "O", "I_", fontContent, null,
        null, null));

    lang.nextStep("Einleitung");

    content.addAll(showText(content2, 0, 200, 20, "O", "I_", fontContent, null,
        null, null));

    hide(content);

  }

  /**
   * Shows the hint-slide
   */
  private void showHints() {

    // The whole content...
    final String stringContent1 = "Problem:";
    final String stringContent2 = "Loesung:";
    final String stringContent3 = "Implementierung:";

    final String stringContent4 = "Jedes Zwischenergebnis muss genau einmal verarbeitet werden.";
    final String stringContent5 = "Jedem Thread wird nur eine Teilmenge von Indizes zugaenglich gemacht.";
    final String stringContent6 = "Eindeutige Thread-Ids, Operanden-Distanzen";

    final String stringContent7 = "Jedes Zwischenergebnis muss in einer bestimmten Reihenfolge verarbeitet werden.";
    final String stringContent8 = "Es werden Checkpoints festgelegt, den alle Threads gemeinsam passieren muessen";
    final String stringContent9_1 = "Alle Threads fuehren an bestimmten Punkten sync() aus und warten solange";
    final String stringContent9_2 = "bis alle diesen Punkt erreicht haben.";

    final String stringContent10_1 = "Bei der Anwendung eines binaeren Faltungsoperators, koennen bei einer Problemgroessee";
    final String stringContent10_2 = "von n, maximal n/2 Threads sinnvoll arbeiten. Diese Anzahl dieser Threads wird schrittweise halbiert.";
    final String stringContent11 = "- ";

    // ...gathered in few String array
    final String[] content1To3 = { stringContent1, stringContent2,
        stringContent3 };
    final String[] content4To6 = { stringContent4, stringContent5,
        stringContent6 };
    final String[] content7To9 = { stringContent7, stringContent8,
        stringContent9_1, stringContent9_2 };
    final String[] content10To11 = { stringContent10_1, stringContent10_2,
        stringContent11, stringContent11 };

    Vector<Primitive> content1, content2;

    // Show
    content1 = new Vector<Primitive>(content1To3.length);
    content1.addAll(showText(content1To3, 0, 100, 40, "O", "H_", fontContent,
        null, null, null));

    lang.nextStep("Hinweise");

    content2 = new Vector<Primitive>(content4To6.length);
    content2.addAll(showText(content4To6, 170, 100, 40, "O", "H_", fontContent,
        1, null, null));
    hide(content2);

    lang.nextStep();

    Integer[] suppressed1 = { 2 };
    content2 = new Vector<Primitive>(content7To9.length);
    content2.addAll(showText(content7To9, 170, 100, 40, "O", "H_", fontContent,
        1, suppressed1, -20));
    hide(content2);

    lang.nextStep();

    Integer[] suppressed2 = { 0 };
    content2 = new Vector<Primitive>(content7To9.length);
    content2.addAll(showText(content10To11, 170, 100, 40, "O", "H_",
        fontContent, 1, suppressed2, -20));

    content2.addAll(content1);
    hide(content2);
  }

  /**
   * Shows the sequential-computation-slide
   */
  private void showSequentialAnimation() {

    Offset offset;
    Vector<Primitive> hideList;

    // Initialize offset and hideList
    offset = new Offset(0, 100, "O", AnimalScript.DIRECTION_NW);
    hideList = new Vector<Primitive>(3);

    // Initialize the Legend
    legend = new Legend();
    legend.init(800, 100, "O");

    // Initialize the sequential source-code
    SourceCode codeSequential = lang
        .newSourceCode(offset, "SCS", null, scProps);
    codeSequential.addCodeLine("...", null, 0, null); // 0
    codeSequential.addCodeLine(
        "// Addiere der Reihe nach jeden Wert zur Ergebnisvariable result",
        null, 0, null); // 1
    codeSequential.addCodeLine("...", null, 0, null); // 2
    codeSequential.addCodeLine("for(int i = 0; i < data.length; i += 1) {",
        null, 0, null); // 3
    codeSequential.addCodeLine("result += data[i];", null, 1, null); // 4
    codeSequential.addCodeLine("}", null, 0, null); // 5
    codeSequential.addCodeLine("...", null, 0, null); // 6

    // Simulate thread-private result memory
    offset = new Offset(0, 540, "O", AnimalScript.DIRECTION_NW);

    int[] singleThread = { 0 };
    IntArray threadResults = lang.newIntArray(offset, singleThread, "TRS",
        null, threadProps);

    // Show inputData
    inputData.show();

    // Start animation
    codeSequential.highlight(0);
    lang.nextStep("Visualisierung - sequenziell");

    codeSequential.unhighlight(0);
    codeSequential.highlight(2);
    lang.nextStep();

    codeSequential.unhighlight(2);
    codeSequential.highlight(3);
    codeSequential.highlight(5);

    // Sum up all values form inputData
    for (int i = 0; i < inputData.getLength(); i++) {
      legend.showOp(0, "data[" + i + "]");
      threadResults.put(0, threadResults.getData(0) + inputData.getData(i),
          null, null);
      inputData.highlightCell(i, null, null);
      codeSequential.unhighlight(3);
      codeSequential.unhighlight(5);
      codeSequential.highlight(4);
      lang.nextStep();
      legend.hideOps();
      codeSequential.unhighlight(4);
      codeSequential.highlight(3);
      codeSequential.highlight(5);
      lang.nextStep();
    }

    codeSequential.unhighlight(3);
    codeSequential.unhighlight(4);
    codeSequential.unhighlight(5);
    codeSequential.highlight(6);
    lang.nextStep();

    codeSequential.unhighlight(6);

    // Hide all slide-elements
    hideList.add(inputData);
    hideList.add(codeSequential);
    hideList.add(threadResults);
    hide(hideList);

    // un-highlight all inputData cells
    for (int i = 0; i < inputData.getLength(); i++) {
      inputData.unhighlightCell(i, null, null);
    }
  }

  /**
   * Shows the parallel-computation-slide
   */
  private void showParallelAnimation() {

    legend.showGlobalContext();

    Offset offset;
    Vector<Primitive> hideList;

    // Initialize offset and hideList
    offset = new Offset(0, 100, "O", AnimalScript.DIRECTION_NW);
    hideList = new Vector<Primitive>(3);

    SourceCode codeParallel = lang.newSourceCode(offset, "SCP", null, scProps);
    codeParallel.addCodeLine(
        "numThreads = min(availableThreads, inputData.length >> 1);", null, 0,
        null); // 0
    codeParallel.addCodeLine("...", null, 0, null); // 1
    codeParallel
        .addCodeLine(
            "// Verteile alle Werte des Feldes im gemeinsamen Speicher an max. n / 2 Threads",
            null, 0, null); // 2
    codeParallel.addCodeLine(
        "for(int i = myId; i < inputData.length; i += numThreads) {", null, 0,
        null); // 3
    codeParallel.addCodeLine("myResult += inputData[i];", null, 1, null); // 4
    codeParallel.addCodeLine("}", null, 0, null); // 5
    codeParallel.addCodeLine("synchronize();", null, 0, null); // 6
    codeParallel
        .addCodeLine(
            "// Reduziere alle thread-privaten Werte zu einem Ergebniswert (dieser liegt dann in thread 0 vor)",
            null, 0, null); // 7
    codeParallel.addCodeLine("int lastStep = numThreads;", null, 0, null); // 8
    codeParallel
        .addCodeLine("int currentStep = half(lastStep);", null, 0, null); // 9
    codeParallel.addCodeLine("while(currentstep > 0) {", null, 0, null); // 10
    codeParallel.addCodeLine("if(myId + currentStep < lastStep) {", null, 1,
        null); // 11
    codeParallel.addCodeLine("myResult += getResultOf(myId + currentStep);",
        null, 2, null); // 12
    codeParallel.addCodeLine("}", null, 1, null); // 13
    codeParallel.addCodeLine("lastStep = currentStep;", null, 1, null); // 14
    codeParallel.addCodeLine("currentStep = half(lastStep);", null, 1, null); // 15
    codeParallel.addCodeLine("synchronize();", null, 1, null); // 16
    codeParallel.addCodeLine("}", null, 0, null); // 17
    codeParallel.addCodeLine("...", null, 0, null);

    // Simulate thread-private result memory
    int[] initWorkers = new int[threadc];
    for (int i = 0; i < threadc; i++) {
      initWorkers[i] = 0;
    }

    offset = new Offset(0, 540, "O", AnimalScript.DIRECTION_NW);

    IntArray threadResults = lang.newIntArray(
        offset,
        initWorkers,
        "TRP",
        null,
        createArrayProperties(Color.BLACK, colorActive, Boolean.TRUE,
            Color.BLACK, colorProcessed, colorInActive, fontData));
    inputData.show();

    // Start the reduction-animation

    // If there never was any work, were done
    if (inputData.getLength() == 0) {
      return;
    }

    final int maxThreadCount = inputData.getLength() >> 1;
    final int threadCount = maxThreadCount > threadResults.getLength() ? threadResults
        .getLength() : maxThreadCount;

    // Mark all threads being too much
    threadResults.highlightCell(maxThreadCount, threadResults.getLength() - 1,
        null, null);
    codeParallel.highlight(0);
    lang.nextStep("Visualisierung - parallel");

    // There are at least 2 pieces of work
    barrier = new CyclicBarrier(threadCount);
    threads = new Worker[threadCount];
    codeParallel.unhighlight(0);
    codeParallel.highlight(1);
    lang.nextStep();

    // Create new threads
    for (int i = 0; i < threadCount; i++) {

      threads[i] = new Worker(inputData, threadResults, codeParallel, i,
          threadCount);
    }

    // Threads ready for computation
    for (int i = 0; i < threadCount; i++) {
      threads[i].start();
    }

    // Gather
    for (int i = 0; i < threadCount; i++) {
      try {
        threads[i].join();
      } catch (InterruptedException e) {
        System.err.println("Error while joining");
      }
    }

    codeParallel.unhighlight(18);

    // Hide all slide-elements

    hideList.add(inputData);
    hideList.add(codeParallel);
    hideList.add(threadResults);
    hideList.addAll(legend.hideList);

    hide(hideList);

  }

  private void showSummary() {

    // The whole content...
    final String stringContent0 = "Trotz der fallenden Anzahl arbeitender Threads erhaelt man eine Laufzeitverbesserung";
    final String stringContent1 = "von (n-1) Berechnungschritten, fuer einen sequenziellen Durchlauf, zu log(n) Berechnungschritten, bei";
    final String stringContent2 = "paralleler Ausfuehrung.";

    // ...gathered in String arrays
    String[] content0 = { stringContent0, stringContent1, stringContent2 };

    Vector<Primitive> content = new Vector<Primitive>(content0.length);

    lang.nextStep("Bemerkungen & Vergleich");

    // Show
    content.addAll(showText(content0, 0, 100, 20, "O", "S_", fontContent, null,
        null, null));

    hide(content);

  }

  // ---------------------------------------------------------------------
  private class Worker extends Thread {

    private int        myid, threadCount;
    private IntArray   inputData;
    private SourceCode code;
    private IntArray   myresult;

    public Worker(IntArray input, IntArray threadresults,
        SourceCode sourcecode, int id, int threadc) {
      myid = id;
      inputData = input;
      code = sourcecode;
      myresult = threadresults;
      threadCount = threadc;
    }

    public void run() throws LineNotExistsException {

      int currentStep, lastStep;
      int tmp;

      // before for
      if (myid == 0) {
        code.unhighlight(1);
        code.highlight(3);
        code.highlight(5);
        lang.nextStep();
      }

      // Because of animation issues the loop has to be synchronized
      // All threads have to run threadCount times through the loop
      // --------------
      // Distribute all input-values to the threads
      for (int i = myid, sync = 0; sync < inputData.getLength(); i += threadCount, sync += threadCount) {

        // For animation
        sync();

        if (i < inputData.getLength()) {

          updateResult(myresult, inputData, myid, i);
        }

        sync();

        // duplicated because of Animation issues
        if (i < inputData.getLength()) {

          legend.showOp(myid, "data[" + i + "]");
        }

        // For animation
        sync();

        if (myid == 0) {
          code.unhighlight(3);
          code.unhighlight(5);
          code.highlight(4);
          lang.nextStep();
        }

        if (myid == 0) {
          legend.hideOps();
          code.unhighlight(4);
          code.highlight(3);
          code.highlight(5);
          lang.nextStep();
        }
      }

      // synchronize
      sync(); // necessary without animation
      if (myid == 0) {
        code.unhighlight(3);
        code.unhighlight(5);
        code.highlight(6);
        lang.nextStep();
      }

      // the last-step corresponds to the threadCount
      lastStep = threadCount;
      if (myid == 0) {
        code.unhighlight(6);
        code.highlight(8);
        lang.nextStep();
      }

      // the current-step is the half of the last-step
      currentStep = half(lastStep);
      if (myid == 0) {
        code.unhighlight(8);
        code.highlight(9);
        lang.nextStep();
      }

      // the current-step is the half of the last-step
      currentStep = half(lastStep);

      // before while
      if (myid == 0) {
        code.unhighlight(9);
        code.highlight(10);
        code.highlight(17);
        lang.nextStep();
      }

      // Reduce
      while (currentStep > 0) {

        // if...
        if (myid == 0) {
          code.unhighlight(10);
          code.unhighlight(17);
          code.highlight(11);
          code.highlight(13);
          lang.nextStep();
        }

        // For animation
        sync();

        if (myid + currentStep < lastStep) {

          updateResult(myresult, myid, currentStep);
        }

        // For animation
        sync();

        // duplicated because of animation issues
        if (myid + currentStep < lastStep) {

          tmp = myid + currentStep;
          legend.showOp(myid, "thread" + tmp + ".result");
        }
        // For animation
        sync();

        // update...
        if (myid == 0) {
          code.unhighlight(11);
          code.unhighlight(13);
          code.highlight(12);
          lang.nextStep();
        }

        // lastStep...
        lastStep = currentStep;
        if (myid == 0) {
          legend.hideOps();
          code.unhighlight(12);
          code.highlight(14);
          lang.nextStep();
        }

        // currentStep...
        currentStep = half(currentStep);
        if (myid == 0) {
          code.unhighlight(14);
          code.highlight(15);
          lang.nextStep();
        }

        // synchronize...
        // sync(); // necessary without animation
        if (myid == 0) {
          code.unhighlight(15);
          code.highlight(16);
          lang.nextStep();
        }

        // next while...
        if (myid == 0) {
          code.unhighlight(16);
          code.highlight(10);
          code.highlight(17);
          lang.nextStep();
        }
      }

      // Final code-highlight
      if (myid == 0) {
        code.unhighlight(10);
        code.unhighlight(17);
        code.unhighlight(16);
        code.highlight(18);
        lang.nextStep();
      }
    }

    /**
     * Cyclic Barrier to synchronize Reduction-steps
     */
    private void sync() {
      try {
        barrier.await();
      } catch (InterruptedException e) {
        System.err.println("Error while waiting phase 1");
      } catch (BrokenBarrierException e) {
        System.err.println("Barrier broken at phase 1");
        e.printStackTrace();
      }
    }

    /**
     * Halfs the step-size. If step is odd the result gets ceiled. If step is
     * one the result is zero.
     * 
     * @param step
     *          - an integer
     * @return
     */
    private int half(int step) {
      return step == 1 ? 0 : step % 2 == 0 ? step >> 1 : (step >> 1) + 1;
    }
  }

  // ---------------------------------------------------------------------

  /**
   * Helper-method to increase thread-local results and highlight the operands
   * 
   * @param myResult
   *          - thread-local summation variable
   * @param inputData
   *          - the source of values
   * @param myId
   *          - identifies the thread-local variable
   * @param i
   *          - identifies the position the operand
   */
  synchronized void updateResult(IntArray myResult, IntArray inputData,
      int myId, int i) {
    myResult.put(myId, myResult.getData(myId) + inputData.getData(i), null,
        null);
    inputData.highlightCell(i, null, null);
  }

  /**
   * Helper-method to increase thread-local results and highlight the operands
   * 
   * @param myResult
   *          - thread-local summation variable
   * @param myId
   *          - identifies the thread-local variable
   * @param i
   *          - identifies the position the operand
   */
  synchronized void updateResult(IntArray myResult, int myId, int i) {
    myResult.put(myId, myResult.getData(myId) + myResult.getData(myId + i),
        null, null);
    myResult.highlightCell(myId + i, null, null);
    myResult.highlightElem(myId + i, null, null);
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
   * Creates and initializes a new SourceProperties Object
   * 
   * @param contextcolor
   *          - dunno rly!
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
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    return scProps;
  }

  /**
   * Create and initializes a new array-property object
   * 
   * @param colorproperty
   *          - the color property
   * @param fillproperty
   *          - the fill property
   * @param filledproperty
   *          - the filled property
   * @param elementcolor
   *          - the element color property
   * @param elementhighlight
   *          - the element highlight property
   * @param cellhighlight
   *          - the cell highlight property
   * @return new object of ArrayProperties
   */
  private ArrayProperties createArrayProperties(Color colorproperty,
      Color fillproperty, Boolean filledproperty, Color elementcolor,
      Color elementhighlight, Color cellhighlight, Font font) {

    ArrayProperties arrayProps = new ArrayProperties();

    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, colorproperty);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, fillproperty);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, filledproperty);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, elementcolor);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
        elementhighlight);
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        cellhighlight);
    arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, font);

    return arrayProps;
  }

  /**
   * This class represents a new complex Animal-primitive - a legend
   * 
   * @author Tobias Raffel
   * @version 0.9
   */
  private class Legend {

    Vector<Primitive> hideList;
    private List<Text>        workingThreads;
    private Text              textThreadId;
    private int               refCnt;

    /**
     * Creates a new instance of Legend
     */
    public Legend() {

      refCnt = 0;
      if (lang.isNameUsed("L" + refCnt)) {

        refCnt++;
      }
    }

    /**
     * initializes the Legend with default values
     */
    public void init(int xPos, int yPos, String upperLeft) {

      Offset off1;
      Offset off2;
      int count;
      Text text;

      RectProperties frameProps, contextProps, activeProps, inActiveProps, fetchedProps;
      Rect frame, frameContext, frameScheduler, boxActive, boxInActive, boxFetched;
      Text textActive, textInActive, textFetched, textContext;

      hideList = new Vector<Primitive>(100);

      frameProps = new RectProperties();
      frameProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
      frameProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
      frameProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

      contextProps = new RectProperties();
      contextProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
      contextProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
      contextProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

      activeProps = new RectProperties();
      activeProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      activeProps.set(AnimationPropertiesKeys.FILL_PROPERTY, colorActive);
      activeProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

      inActiveProps = new RectProperties();
      inActiveProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      inActiveProps.set(AnimationPropertiesKeys.FILL_PROPERTY, colorInActive);
      inActiveProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

      fetchedProps = new RectProperties();
      fetchedProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      fetchedProps.set(AnimationPropertiesKeys.FILL_PROPERTY, colorProcessed);
      fetchedProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

      // Create scheduler texts
      workingThreads = new ArrayList<Text>();

      final int half = data.length >> 1;
      final int threadCount = (half > threadc) ? threadc : half;

      for (count = 0; count < threadCount; count++) {

        off1 = new Offset(xPos + 10, yPos + 155 + count * 20, upperLeft,
            AnimalScript.DIRECTION_NW);
        text = lang.newText(off1, "thread" + count + " += " + "thread" + count,
            "L" + refCnt, null);
        text.setFont(fontCode, null, null);
        text.hide();

        workingThreads.add(text);
        hideList.add(text);
      }

      // Rectangles
      off1 = new Offset(xPos, yPos, upperLeft, AnimalScript.DIRECTION_NW);
      off2 = new Offset(xPos + 200, yPos + 150 + count * 20 + 10, upperLeft,
          AnimalScript.DIRECTION_NW);
      frame = lang.newRect(off1, off2, "L" + refCnt, null, frameProps);

      off1 = new Offset(xPos + 5, yPos + 10, upperLeft,
          AnimalScript.DIRECTION_NW);
      off2 = new Offset(xPos + 25, yPos + 30, upperLeft,
          AnimalScript.DIRECTION_NW);
      boxActive = lang.newRect(off1, off2, "L" + refCnt, null, activeProps);

      off1 = new Offset(xPos + 5, yPos + 40, upperLeft,
          AnimalScript.DIRECTION_NW);
      off2 = new Offset(xPos + 25, yPos + 60, upperLeft,
          AnimalScript.DIRECTION_NW);
      boxInActive = lang.newRect(off1, off2, "L" + refCnt, null, inActiveProps);

      off1 = new Offset(xPos + 5, yPos + 70, upperLeft,
          AnimalScript.DIRECTION_NW);
      off2 = new Offset(xPos + 25, yPos + 90, upperLeft,
          AnimalScript.DIRECTION_NW);
      boxFetched = lang.newRect(off1, off2, "L" + refCnt, null, fetchedProps);

      off1 = new Offset(xPos + 5, yPos + 100, upperLeft,
          AnimalScript.DIRECTION_NW);
      off2 = new Offset(xPos + 195, yPos + 145, upperLeft,
          AnimalScript.DIRECTION_NW);
      frameContext = lang.newRect(off1, off2, "L" + refCnt, null, contextProps);

      off1 = new Offset(xPos + 5, yPos + 150, upperLeft,
          AnimalScript.DIRECTION_NW);
      off2 = new Offset(xPos + 195, yPos + 150 + count * 20 + 5, upperLeft,
          AnimalScript.DIRECTION_NW);
      frameScheduler = lang.newRect(off1, off2, "L" + refCnt, null,
          contextProps);

      // Texts
      off1 = new Offset(xPos + 30, yPos + 10, upperLeft,
          AnimalScript.DIRECTION_NW);
      textActive = lang.newText(off1, "aktiver Thread", "L" + refCnt, null);
      textActive.setFont(fontCode, null, null);

      off1 = new Offset(xPos + 30, yPos + 40, upperLeft,
          AnimalScript.DIRECTION_NW);
      textInActive = lang.newText(off1, "inaktiver Thread", "L" + refCnt, null);
      textInActive.setFont(fontCode, null, null);

      off1 = new Offset(xPos + 30, yPos + 70, upperLeft,
          AnimalScript.DIRECTION_NW);
      textFetched = lang
          .newText(off1, "verarbeiteter Wert", "L" + refCnt, null);
      textFetched.setFont(fontCode, null, null);

      off1 = new Offset(xPos + 10, yPos + 100, upperLeft,
          AnimalScript.DIRECTION_NW);
      textContext = lang.newText(off1, "Kontext:", "L" + refCnt, null);
      textContext.setFont(fontCode, null, null);

      off1 = new Offset(xPos + 10, yPos + 120, upperLeft,
          AnimalScript.DIRECTION_NW);
      textThreadId = lang.newText(off1, "Thread 0", "L" + refCnt, null);
      textThreadId.setFont(fontTitle, null, null);

      hideList.add(frame);
      hideList.add(frameContext);
      hideList.add(frameScheduler);
      hideList.add(boxActive);
      hideList.add(boxInActive);
      hideList.add(boxFetched);
      hideList.add(textActive);
      hideList.add(textInActive);
      hideList.add(textFetched);
      hideList.add(textContext);
      hideList.add(textThreadId);

    }

    /**
     * Shows an expression
     * 
     * @param dest
     *          - the destination index
     * @param opString
     *          - an expression (only LH part)
     */
    public synchronized void showOp(int dest, String opString) {

      Text t = workingThreads.get(dest);
      t.setText("thread" + dest + " += " + opString, null, null);
      t.show();
    }

    /**
     * Hides an expression
     */
    public void hideOps() {

      for (Text t : workingThreads) {
        t.hide();
      }
    }

    /**
     * Changes the legend to highlight the actual focused thread
     * 
     * @param id
     *          - new context
     */
    @SuppressWarnings("unused")
    public void switchContextTo(int id) {

      textThreadId.setText("Thread " + id, null, null);
    }

    /**
     * Changes the legend to highlight the actual focused thread
     */
    public void showGlobalContext() {

      textThreadId.setText("Alle Threads", null, null);
    }

  }

  /**
   * Creates an new reduction-algorithm animation
   * 
   * @param l
   *          - A new Language reference
   * @param input
   */
  public LoopParallelizationReduction(Language l, int[] input, int threadCount) {

    lang = l;
    data = input;
    threadc = threadCount;

    myInit();
  }

  /**
   * Initializes all relevant animation-primitives
   */
  public void myInit() {

    // Language-stepMode
    lang.setStepMode(true);

    // Animation offset-anchor
    PointProperties pp = new PointProperties();
    Point p = lang.newPoint(new Coordinates(20, 40), "O", null, pp);
    p.hide();

    // The colors
    colorActive = Color.GREEN;
    colorInActive = Color.RED;
    colorProcessed = Color.YELLOW;

    // The fonts
    fontContent = new Font("Monospaced", Font.BOLD, 16);
    fontCode = new Font("Monospaced", Font.PLAIN, 12);
    fontHeadline = new Font("Monospaced", Font.BOLD, 30);
    fontTitle = new Font("Monospaced", Font.BOLD, 20);
    fontData = new Font("Monospaced", Font.BOLD, 12);

    // The property-objects
    scProps = createSourceCodeProperties(fontCode, Color.RED, Color.BLACK);
    inputProps = createArrayProperties(Color.BLACK, Color.WHITE, Boolean.TRUE,
        Color.BLACK, colorInActive, colorProcessed, fontData);
    threadProps = createArrayProperties(Color.BLACK, colorActive, Boolean.TRUE,
        Color.BLACK, colorProcessed, colorInActive, fontData);

    // Initialize the inputData with data
    inputData = lang.newIntArray(new Offset(0, 500, "O",
        AnimalScript.DIRECTION_NW), data, "ID", null, inputProps);
    inputData.hide();
  }

  public static void main(String[] args) {

    Language lang = new AnimalScript("Schleifenparallelisierung - Reduktion",
        "Tobias Raffel", 800, 600);

    int[] input = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
    int threadCount = 6;

    LoopParallelizationReduction reduction = new LoopParallelizationReduction(
        lang, input, threadCount);
    reduction.startSlideshow();

    lang.writeFile("Reduction.asu");
  }

}