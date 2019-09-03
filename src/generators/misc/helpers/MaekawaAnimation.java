package generators.misc.helpers;

import generators.misc.MaekawaAlgorithm;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class MaekawaAnimation {

  private MaekawaAlgorithm                algo;
  private Language                        lang;
  private final Lock                      langLocked;
  private Integer                         nProcesses;
  private HashMap<String, Integer>        threads;
  private HashMap<String, Stack<Integer>> sets;
  private HashMap<Integer, Color>         setColors;
  private int[][]                         uVotingSets;

  private HashMap<String, Text>           textElements;
  private HashMap<String, SourceCode>     codeElements;
  private HashMap<String, Circle>         circleElements;
  private HashMap<String, StringArray>    stringArrayElements;
  private HashMap<String, Square>         squareElements;
  private HashMap<String, Polyline>       lineElements;
  private HashMap<String, Object>         properties;
  private boolean                         isHeld;

  /**
   * Creates an instance with given language and main process
   * 
   * @param algo
   *          the main process
   * @param l
   *          the language
   */
  public MaekawaAnimation(MaekawaAlgorithm algo, Language l,
      HashMap<String, Object> properties, Integer nProcesses,
      HashMap<String, Integer> threads, int[][] uVotingSets) {
    this.algo = algo;

    this.lang = l;
    this.lang.setStepMode(true);

    this.langLocked = new ReentrantLock(true);

    this.nProcesses = nProcesses;
    this.threads = threads;
    this.sets = new HashMap<String, Stack<Integer>>();
    this.setColors = new HashMap<Integer, Color>();

    this.properties = properties;
    this.uVotingSets = uVotingSets;

    this.textElements = new HashMap<String, Text>();
    this.codeElements = new HashMap<String, SourceCode>();
    this.circleElements = new HashMap<String, Circle>();
    this.stringArrayElements = new HashMap<String, StringArray>();
    this.squareElements = new HashMap<String, Square>();
    this.lineElements = new HashMap<String, Polyline>();

    this.isHeld = false;
  }

  /**
   * Creates the static intro part of the animation (definitions, algorithm
   * description etc.)
   */
  public void intro() {

    // defines all properties used in this animation
    defineProperties();
    defineVotingSets();
    defineSquareAndColorProperties();

    // defines all elements used in this animation
    defineCircleElements();
    defineStringArrayElements();
    defineSquareElements();
    defineLineElements();
    defineTextElements();
    defineVotingSetExp();
    defineVotingSetsLabel();
    defineProcessDependantTextElements();
    defineCircleText();
    defineQueueLabels();
    defineCodeElements();

    // Generate steps and animations

    // 001
    getTextElements().get("header").show();
    lang.nextStep();

    // 002
    getTextElements().get("intro0").show();
    getTextElements().get("intro1").show();
    getTextElements().get("intro2").show();
    getTextElements().get("intro3").show();
    getTextElements().get("intro4").show();
    lang.nextStep();

    // 003
    getTextElements().get("intro0").hide();
    getTextElements().get("intro1").hide();
    getTextElements().get("intro2").hide();
    getTextElements().get("intro3").hide();
    getTextElements().get("intro4").hide();
    getTextElements().get("info0").show();
    lang.nextStep();

    // 004
    getTextElements().get("info1").show();
    lang.nextStep();

    // 005
    getTextElements().get("info2").show();
    lang.nextStep();

    // 006
    getTextElements().get("info3").show();
    lang.nextStep();

    // 007
    getTextElements().get("info4").show();
    lang.nextStep();

    // 008
    getTextElements().get("info5").show();
    getTextElements().get("info6").show();
    lang.nextStep();

    // 009
    getTextElements().get("info7").show();
    lang.nextStep();

    // 010
    getTextElements().get("info0").hide();
    getTextElements().get("info1").hide();
    getTextElements().get("info2").hide();
    getTextElements().get("info3").hide();
    getTextElements().get("info4").hide();
    getTextElements().get("info5").hide();
    getTextElements().get("info6").hide();
    getTextElements().get("info7").hide();
    getTextElements().get("algoQuest").show();
    lang.nextStep();

    // 011
    getTextElements().get("algoQuest").hide();
    getTextElements().get("algoAnswer").show();
    lang.nextStep();

    // 012
    getTextElements().get("algoSit0").show();
    lang.nextStep();

    // 013
    getTextElements().get("algoSit1").show();
    lang.nextStep();

    // 014
    getTextElements().get("algoSit2").show();
    lang.nextStep();

    // 015
    getTextElements().get("algoSit3").show();
    lang.nextStep();

    // 016
    getTextElements().get("algoAnswer").hide();
    getTextElements().get("algoSit0").hide();
    getTextElements().get("algoSit1").hide();
    getTextElements().get("algoSit2").hide();
    getTextElements().get("algoSit3").hide();

    getTextElements().get("algoSit0Detail").show();
    lang.nextStep();

    // 017
    getCodeElements().get("algoSit0Code").show();
    lang.nextStep();

    // 018
    getTextElements().get("algoSit0Detail").hide();
    getCodeElements().get("algoSit0Code").hide();

    getTextElements().get("algoSit1Detail").show();
    lang.nextStep();

    // 019
    getCodeElements().get("algoSit1Code").show();
    lang.nextStep();

    // 020
    getTextElements().get("algoSit1Detail").hide();
    getCodeElements().get("algoSit1Code").hide();

    getTextElements().get("algoSit2Detail").show();
    lang.nextStep();

    // 021
    getCodeElements().get("algoSit2Code").show();
    lang.nextStep();

    // 022
    getTextElements().get("algoSit2Detail").hide();
    getCodeElements().get("algoSit2Code").hide();

    getTextElements().get("algoSit3Detail").show();
    lang.nextStep();

    // 023
    getCodeElements().get("algoSit3Code").show();
    lang.nextStep();

    // 024
    getTextElements().get("algoSit3Detail").hide();
    getCodeElements().get("algoSit3Code").hide();

    showCircles();

    showCircleText();

    lang.nextStep();

    // 025
    showVariables();

    lang.nextStep();

    // 026
    showQueues();

    // 027
    getTextElements().get("votingSet0").show();

    lang.nextStep();

    // 028
    getTextElements().get("votingSet1").show();
    getTextElements().get("votingSet2").show();
    getTextElements().get("votingSet3").show();

    lang.nextStep();

    // 029
    getTextElements().get("votingSet4").show();
    getTextElements().get("votingSet5").show();

    lang.nextStep();

    votingSetAnimation();

    getTextElements().get("votingSet0").hide();
    getTextElements().get("votingSet1").hide();
    getTextElements().get("votingSet2").hide();
    getTextElements().get("votingSet3").hide();
    getTextElements().get("votingSet4").hide();
    getTextElements().get("votingSet5").hide();

    lang.nextStep();
  }

  /**
   * Shows the votingset animation
   */
  public void votingSetAnimation() {
    for (int i = 0; i < sets.size(); i++) {
      Stack<Integer> votingSet = sets.get("set" + i);
      for (int j = 0; j < votingSet.size(); j++) {
        getCircleElements().get("process" + votingSet.get(j)).changeColor(
            "fillColor", setColors.get(i), null, null);
      }

      lang.nextStep();

      getSquareElements().get("set" + i).show();
      getTextElements().get("set" + i + "Label").show();

      lang.nextStep();

      for (int j = 0; j < votingSet.size(); j++) {
        getCircleElements().get("process" + votingSet.get(j)).changeColor(
            "fillColor", Color.white, null, null);
      }

      lang.nextStep();
    }
  }

  /**
   * Creates the static end part of this animation
   */
  public void end() {
    getTextElements().get("end0").show();
    getTextElements().get("end1").show();
    getTextElements().get("end2").show();
  }

  /**
   * Creates the access animation for the given process
   * 
   * @param process
   *          the process who gets access to the critical section
   */
  public void accessAnimation(Integer process) {
    if (!isHeld) { // if actually the critical section is held in the animation
                   // then try later again
      if (langLocked.tryLock()) { // try to get exlusive access to animate, if
                                  // true animate, else try later again
        isHeld = true; // set isHeld = true
        Text text = getTextElements().get("algoSit0Detail");
        SourceCode code = getCodeElements().get("algoSit0Code");

        String infoName = "processInfo" + process + "A"; // tell which process
                                                         // gets access to the
                                                         // critical section
        Text info = getTextElements().get(infoName);

        text.show();
        code.show();
        info.show();
        code.highlight(2);
        lang.nextStep();
        code.unhighlight(2);
        code.highlight(3);
        lang.nextStep();
        changeStateAnimation(process, 2);
        code.unhighlight(3);
        text.hide();
        code.hide();
        info.hide();
        lang.nextStep();
        langLocked.unlock();
      } else {
        try {
          Thread.sleep(50);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        accessAnimation(process);
      }
    } else {
      try {
        Thread.sleep(5);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      accessAnimation(process);
    }
  }

  /**
   * Creates the request animation for the given process
   * 
   * @param process
   *          the process who requests access to the critical section
   * @param votingSet
   *          the votingset of the given process
   */
  public void requestAnimation(Integer process, Stack<Integer> votingSet) {
    if (langLocked.tryLock()) { // try to get exlusive access to animate, if
                                // true animate, else try later again
      Text text = getTextElements().get("algoSit0Detail");
      SourceCode code = getCodeElements().get("algoSit0Code");

      String infoName = "processInfo" + process + "Req"; // tell which process
                                                         // requests access to
                                                         // the critical section
      Text info = getTextElements().get(infoName);

      text.show();
      code.show();
      info.show();
      code.highlight(0);
      lang.nextStep();
      changeStateAnimation(process, 1);
      code.unhighlight(0);
      code.highlight(1);

      for (int i = 0; i < votingSet.size(); i++) {
        String line = "line" + process + votingSet.get(i); // show who gets the
                                                           // request
        getLineElements().get(line).show();
        getTextElements().get("processReq").show();
        lang.nextStep();
        getLineElements().get(line).hide();
        getTextElements().get("processReq").hide();
      }

      code.unhighlight(1);
      code.highlight(2);
      lang.nextStep();
      code.unhighlight(2);
      text.hide();
      code.hide();
      info.hide();
      lang.nextStep();
      langLocked.unlock(); // release exclusive access to animate
    } else {
      try {
        Thread.sleep(5);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      requestAnimation(process, votingSet);
    }
  }

  /**
   * Creates the receive request animation
   * 
   * @param requestingProcess
   *          the process who requests access to the critical section
   * @param receivingProcess
   *          the process who receives the request
   */
  public void receiveRequestAnimation(Integer requestingProcess,
      Integer receivingProcess) {
    if (langLocked.tryLock()) { // try to get exlusive access to animate, if
                                // true animate, else try later again
      Text text = getTextElements().get("algoSit2Detail");
      SourceCode code = getCodeElements().get("algoSit2Code");

      String infoName = "processInfo" + receivingProcess + "RReq"; // tell which
                                                                   // process
                                                                   // receives a
                                                                   // request
      Text info = getTextElements().get(infoName);

      text.show();
      code.show();
      info.show();
      code.highlight(0);
      lang.nextStep();
      code.unhighlight(0);

      String queue = "process" + receivingProcess + "Queue";
      LinkedList<Integer> processQueue = algo.getProcessQueues().get(
          receivingProcess);
      Integer[] processVariables = algo.getProcessVariables().get(
          receivingProcess);

      if (processVariables[0] == 2 || processVariables[1] == 1) { // if STATE =
                                                                  // HELD or
                                                                  // VOTED =
                                                                  // TRUE
        code.highlight(1); // show enqueuing animation
        lang.nextStep();

        StringArray arr = getStringArrayElements().get(queue);
        Integer pos = processQueue.size();
        arr.highlightCell(pos, null, null);
        lang.nextStep();
        arr.put(pos, String.valueOf(requestingProcess), null, null);
        arr.highlightElem(pos, null, null);
        lang.nextStep();
        arr.unhighlightCell(pos, null, null);
        arr.unhighlightElem(pos, null, null);

        lang.nextStep();
        code.unhighlight(1);
      } else {
        code.highlight(2); // show reply animation
        lang.nextStep();
        code.unhighlight(2);
        code.highlight(3);
        lang.nextStep();

        String line = "line" + receivingProcess + requestingProcess;
        getLineElements().get(line).show();
        getTextElements().get("processOk").show();
        lang.nextStep();
        getLineElements().get(line).hide();
        getTextElements().get("processOk").hide();

        lang.nextStep();
        code.unhighlight(3);
        code.highlight(4);
        lang.nextStep();
        changeVotedAnimation(receivingProcess, 1);
        lang.nextStep();
        code.unhighlight(4);
      }

      code.hide();
      text.hide();
      info.hide();
      lang.nextStep();
      langLocked.unlock(); // release exclusive access to animate
    } else {
      try {
        Thread.sleep(5);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      receiveRequestAnimation(requestingProcess, receivingProcess);
    }
  }

  /**
   * Creates the change state animation for the given process
   * 
   * @param process
   *          the process who changes its state
   * @param state
   *          the new state of the process
   */
  public void changeStateAnimation(Integer process, Integer state) {
    if (state == 0) {
      String stateNameA = "process" + process + "StateH";
      String stateNameB = "process" + process + "StateR";

      getTextElements().get(stateNameA).hide(); // hide old state
      Text released = getTextElements().get(stateNameB);
      released.show(); // show new state
      released.changeColor("Color", Color.red, null, null); // and colored it
                                                            // red
      lang.nextStep();
      released.changeColor("Color", Color.black, null, null); // change back to
                                                              // black
    } else if (state == 1) {
      String stateNameA = "process" + process + "StateR";
      String stateNameB = "process" + process + "StateW";

      getTextElements().get(stateNameA).hide(); // hide old state
      Text released = getTextElements().get(stateNameB);
      released.show(); // show new state
      released.changeColor("Color", Color.red, null, null); // and colored it
                                                            // red
      lang.nextStep();
      released.changeColor("Color", Color.black, null, null); // change back to
                                                              // black
    } else {
      String stateNameA = "process" + process + "StateW";
      String stateNameB = "process" + process + "StateH";

      getTextElements().get(stateNameA).hide(); // hide old state
      Text released = getTextElements().get(stateNameB);
      released.show(); // show new state
      released.changeColor("Color", Color.red, null, null); // and colored it
                                                            // red
      lang.nextStep();
      released.changeColor("Color", Color.black, null, null); // change back to
                                                              // black
    }
  }

  /**
   * Creates the change voted animation for the given process
   * 
   * @param process
   *          the process who changes its voted state
   * @param voted
   *          the new voted state, true if the process has voted, else false
   */
  public void changeVotedAnimation(Integer process, Integer voted) {
    if (voted == 0) {
      String stateNameA;
      if (algo.getProcessVariables().get(process)[1] == 1) {
        stateNameA = "process" + process + "VotedT";
      } else {
        stateNameA = "process" + process + "VotedF";
      }
      String stateNameB = "process" + process + "VotedF";

      getTextElements().get(stateNameA).hide(); // hide old voted state
      Text released = getTextElements().get(stateNameB);
      released.show(); // show new voted state
      released.changeColor("Color", Color.red, null, null); // and colored it
                                                            // red
      lang.nextStep();
      released.changeColor("Color", Color.black, null, null); // change back to
                                                              // black
    } else {
      String stateNameA;
      if (algo.getProcessVariables().get(process)[1] == 1) {
        stateNameA = "process" + process + "VotedT";
      } else {
        stateNameA = "process" + process + "VotedF";
      }
      String stateNameB = "process" + process + "VotedT";

      getTextElements().get(stateNameA).hide(); // hide old voted state
      Text released = getTextElements().get(stateNameB);
      released.show(); // show new voted state
      released.changeColor("Color", Color.red, null, null); // and colored it
                                                            // red
      lang.nextStep();
      released.changeColor("Color", Color.black, null, null); // change back to
                                                              // black
    }
  }

  /**
   * Creates the release animation for the given process
   * 
   * @param process
   *          the process who leaves the critical section
   * @param votingSet
   *          the votingset of the given process
   */
  public void releaseAnimation(Integer process, Stack<Integer> votingSet) {
    if (langLocked.tryLock()) { // try to get exlusive access to animate, if
                                // true animate, else try later again
      isHeld = false; // tell the animation nobody holds the access anymore
      Text text = getTextElements().get("algoSit1Detail");
      SourceCode code = getCodeElements().get("algoSit1Code");

      String infoName = "processInfo" + process + "Rel"; // tell which process
                                                         // leaves the critical
                                                         // section
      Text info = getTextElements().get(infoName);

      text.show();
      code.show();
      info.show();
      code.highlight(0);
      lang.nextStep();
      changeStateAnimation(process, 0);
      code.unhighlight(0);
      code.highlight(1);
      lang.nextStep();

      for (int i = 0; i < votingSet.size(); i++) {
        String line = "line" + process + votingSet.get(i); // show who gets the
                                                           // release
        getLineElements().get(line).show();
        getTextElements().get("processRel").show();
        lang.nextStep();
        getLineElements().get(line).hide();
        getTextElements().get("processRel").hide();
      }

      lang.nextStep();
      code.unhighlight(1);
      text.hide();
      code.hide();
      info.hide();
      lang.nextStep();
      langLocked.unlock();
    } else {
      try {
        Thread.sleep(5);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      releaseAnimation(process, votingSet);
    }
  }

  /**
   * Creates the receive release animation
   * 
   * @param releasingProcess
   *          the process who leaves the critical section
   * @param receivingProcess
   *          the process who receives the release
   */
  public void receiveReleaseAnimation(Integer releasingProcess,
      Integer receivingProcess) {
    if (langLocked.tryLock()) { // try to get exlusive access to animate, if
                                // true animate, else try later again
      Text text = getTextElements().get("algoSit3Detail");
      SourceCode code = getCodeElements().get("algoSit3Code");

      String infoName = "processInfo" + receivingProcess + "RRel"; // tell which
                                                                   // process
                                                                   // receives a
                                                                   // release
      Text info = getTextElements().get(infoName);

      text.show();
      code.show();
      info.show();
      code.highlight(0);
      lang.nextStep();
      code.unhighlight(0);

      String queue = "process" + receivingProcess + "Queue";

      if (algo.getProcessQueues().get(receivingProcess).isEmpty()) { // if queue
                                                                     // is empty
        code.highlight(1);
        lang.nextStep();
        changeVotedAnimation(receivingProcess, 0); // animate VOTED = FALSE
        lang.nextStep();
        code.unhighlight(1);

      } else { // else
        LinkedList<Integer> processQueue = algo.getProcessQueues().get(
            receivingProcess);
        StringArray arr = getStringArrayElements().get(queue);
        arr.highlightCell(0, null, null);
        lang.nextStep();
        if (processQueue.size() > 1) {
          for (int i = 0; i < processQueue.size() - 1; i++) {
            arr.put(i, String.valueOf(processQueue.get(i + 1)), null, null);
            arr.put(i + 1, "null", null, null);
          }
        } else {
          arr.put(0, "null", null, null);
        }

        String line = "line" + receivingProcess + processQueue.getFirst();
        arr.highlightElem(0, null, null);
        getLineElements().get(line).show(); // animate reply to process
        getTextElements().get("processOk").show();
        lang.nextStep();
        arr.unhighlightCell(0, null, null);
        arr.unhighlightElem(0, null, null);
        getLineElements().get(line).hide();
        getTextElements().get("processOk").hide();

        lang.nextStep();
        code.unhighlight(3);
        code.highlight(4);
        lang.nextStep();
        changeVotedAnimation(receivingProcess, 1);
        lang.nextStep();
        code.unhighlight(4);
      }

      code.hide();
      text.hide();
      info.hide();
      lang.nextStep();
      langLocked.unlock();
    } else {
      try {
        Thread.sleep(5);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      receiveReleaseAnimation(releasingProcess, receivingProcess);
    }
  }

  /**
   * Defines all properties used in this animation
   */
  public void defineProperties() {
    // info-red properties
    TextProperties infoRedProps = new TextProperties();
    infoRedProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 18));
    infoRedProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
    infoRedProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    properties.put("infoRedProps", infoRedProps);

    // circle properties
    CircleProperties circleProps = new CircleProperties();
    circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
    circleProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    properties.put("circleProps", circleProps);

    // process variables
    TextProperties variablesTextProps = new TextProperties();
    variablesTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));
    variablesTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    variablesTextProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    properties.put("variablesTextProps", variablesTextProps);

    // votingSetDescription properties
    TextProperties votingSetProps = new TextProperties();
    votingSetProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));
    votingSetProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    properties.put("votingSetProps", votingSetProps);

    // square properties
    SquareProperties squareProps0 = new SquareProperties();
    squareProps0.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    squareProps0.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.blue);
    squareProps0.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    SquareProperties squareProps1 = new SquareProperties();
    squareProps1.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    squareProps1.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.pink);
    squareProps1.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    SquareProperties squareProps2 = new SquareProperties();
    squareProps2.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    squareProps2.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.cyan);
    squareProps2.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    SquareProperties squareProps3 = new SquareProperties();
    squareProps3.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    squareProps3.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.green);
    squareProps3.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    properties.put("squareProps0", squareProps0);
    properties.put("squareProps1", squareProps1);
    properties.put("squareProps2", squareProps2);
    properties.put("squareProps3", squareProps3);

    // votingset labels properties
    TextProperties setLabelProps = new TextProperties();
    setLabelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 14));
    setLabelProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    properties.put("setLabelProps", setLabelProps);

    // line properties
    PolylineProperties lineProps = new PolylineProperties();
    lineProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    lineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    lineProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    properties.put("lineProps", lineProps);
  }

  /**
   * Defines square and colorProperties for Votingsets
   */
  public void defineSquareAndColorProperties() {
    Random random = new Random();

    Set<String> keySet = sets.keySet();
    Iterator<String> it = keySet.iterator();
    while (it.hasNext()) {
      String next = it.next();
      String setNumber = next.substring(3, next.length());

      Integer iSetNumber = Integer.valueOf(setNumber);

      Color color = new Color(random.nextInt(256), random.nextInt(256),
          random.nextInt(256));
      setColors.put(iSetNumber, color);

      // square properties
      SquareProperties squareProps = new SquareProperties();
      squareProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      squareProps.set(AnimationPropertiesKeys.FILL_PROPERTY, color);
      squareProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
      properties.put("squareProps" + iSetNumber, squareProps);
    }
  }

  /**
   * Defines text fields with content
   */
  public void defineTextElements() {
    // header
    Text header = lang.newText(new Coordinates(20, 20), "Maekawa Algorithmus",
        "header", null, (TextProperties) properties.get("headerProps"));
    textElements.put("header", header);

    // algorithm description
    Text intro0 = lang
        .newText(
            new Offset(0, 50, "header", AnimalScript.DIRECTION_NW),
            "Der Maekawa-Algorithmus dient zur Zugriffssteuerung auf einen kritischen Bereich unter Prozessen.",
            "intro0", null, (TextProperties) properties.get("infoProps"));
    Text intro1 = lang
        .newText(
            new Offset(0, 30, "intro0", AnimalScript.DIRECTION_NW),
            "Dabei werden die Prozesse in Votingsets (K Mitglieder) eingeteilt, wobei zwei unterschiedliche Votingsets mindestens",
            "intro1", null, (TextProperties) properties.get("infoProps"));
    Text intro2 = lang
        .newText(
            new Offset(0, 30, "intro1", AnimalScript.DIRECTION_NW),
            "einen gemeinsamen Prozess beinhalten müssen und jeder Prozess muss mindestens in zwei Votingsets vertreten sein.",
            "intro2", null, (TextProperties) properties.get("infoProps"));
    Text intro3 = lang
        .newText(
            new Offset(0, 30, "intro2", AnimalScript.DIRECTION_NW),
            "Votingsets werden eingesetzt um die Kommunikation unter den Prozessen zu vereinfachen, so müssen Anfragen",
            "intro3", null, (TextProperties) properties.get("infoProps"));
    Text intro4 = lang
        .newText(
            new Offset(0, 30, "intro3", AnimalScript.DIRECTION_NW),
            "nicht an alle gesendet werden, sondern nur an die Prozesse im eigenen Votingset.",
            "intro4", null, (TextProperties) properties.get("infoProps"));
    textElements.put("intro0", intro0);
    textElements.put("intro1", intro1);
    textElements.put("intro2", intro2);
    textElements.put("intro3", intro3);
    textElements.put("intro4", intro4);

    // algorithm explanation
    Text info0 = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW),
        "Jeder Prozess besitzt einen internen Status mit einem der Werte:",
        "info0", null, (TextProperties) properties.get("infoProps"));

    Text info1 = lang.newText(new Offset(10, 40, "info0",
        AnimalScript.DIRECTION_NW), "- RELEASED", "info1", null,
        (TextProperties) properties.get("infoRedProps"));

    Text info2 = lang.newText(new Offset(0, 30, "info1",
        AnimalScript.DIRECTION_NW), "- WANTED", "info2", null,
        (TextProperties) properties.get("infoRedProps"));

    Text info3 = lang.newText(new Offset(0, 30, "info2",
        AnimalScript.DIRECTION_NW), "- HELD", "info3", null,
        (TextProperties) properties.get("infoRedProps"));

    Text info4 = lang.newText(new Offset(-10, 40, "info3",
        AnimalScript.DIRECTION_NW), "Sowie eine boolsche Variable:", "info4",
        null, (TextProperties) properties.get("infoProps"));

    Text info5 = lang.newText(new Offset(10, 40, "info4",
        AnimalScript.DIRECTION_NW), "- VOTED", "info5", null,
        (TextProperties) properties.get("infoRedProps"));

    Text info6 = lang.newText(new Offset(-10, 40, "info5",
        AnimalScript.DIRECTION_NW), "Mit den Werten TRUE oder FALSE.", "info6",
        null, (TextProperties) properties.get("infoProps"));

    Text info7 = lang.newText(new Offset(0, 40, "info6",
        AnimalScript.DIRECTION_NW), "Und eine Warteschlange für Prozesse.",
        "info7", null, (TextProperties) properties.get("infoProps"));

    textElements.put("info0", info0);
    textElements.put("info1", info1);
    textElements.put("info2", info2);
    textElements.put("info3", info3);
    textElements.put("info4", info4);
    textElements.put("info5", info5);
    textElements.put("info6", info6);
    textElements.put("info7", info7);

    // text "algoQuestion" "Wie funktioniert der Algorithmus?" offset (0, 100)
    // from "header" NW color blue font SansSerif size 22
    Text algoQuest = lang
        .newText(new Offset(0, 100, "header", AnimalScript.DIRECTION_NW),
            "Wie funktioniert der Algorithmus?", "algoQuestion", null,
            (TextProperties) properties.get("algoQuestProps"));
    textElements.put("algoQuest", algoQuest);

    // text "algoAnswer" "Wir unterscheiden 4 Mögliche Situationen!" offset (0,
    // 50) from "header" NW color black font SansSerif size 18
    Text algoAnswer = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW),
        "Wir unterscheiden 4 Mögliche Situationen!", "algoAnswer", null,
        (TextProperties) properties.get("infoProps"));
    textElements.put("algoAnswer", algoAnswer);

    // text "algoSituation0"
    // "- Der Prozess möchte Zugang zum kritischen Bereich" offset (10, 40) from
    // "algoAnswer" NW color red font SansSerif size 18
    Text algoSit0 = lang.newText(new Offset(10, 40, "algoAnswer",
        AnimalScript.DIRECTION_NW),
        "- Der Prozess möchte Zugang zum kritischen Bereich", "algoSit0", null,
        (TextProperties) properties.get("infoRedProps"));
    textElements.put("algoSit0", algoSit0);

    // text "algoSituation1" "- Der Prozess verlässt den kritischen Bereich"
    // offset (0, 30) from "algoSituation0" NW font SansSerif size 18
    Text algoSit1 = lang.newText(new Offset(0, 30, "algoSit0",
        AnimalScript.DIRECTION_NW),
        "- Der Prozess verlässt den kritischen Bereich", "algoSit1", null,
        (TextProperties) properties.get("infoRedProps"));
    textElements.put("algoSit1", algoSit1);

    // text "algoSituation2"
    // "- Der Prozess erhält eine Anfrage von einem anderen Prozess" offset (0,
    // 30) from "algoSituation1" NW font SansSerif size 18
    Text algoSit2 = lang.newText(new Offset(0, 30, "algoSit1",
        AnimalScript.DIRECTION_NW),
        "- Der Prozess erhält eine Anfrage von einem anderen Prozess",
        "algoSit2", null, (TextProperties) properties.get("infoRedProps"));
    textElements.put("algoSit2", algoSit2);

    // text "algoSituation3"
    // "- Der Prozess erhält eine Freigabe von einem anderen Prozess" offset (0,
    // 30) from "algoSituation2" NW font SansSerif size 18
    Text algoSit3 = lang.newText(new Offset(0, 30, "algoSit2",
        AnimalScript.DIRECTION_NW),
        "- Der Prozess erhält eine Freigabe von einem anderen Prozess",
        "algoSit3", null, (TextProperties) properties.get("infoRedProps"));
    textElements.put("algoSit3", algoSit3);

    // text "algoSituation0Detail"
    // "Der Prozess möchte Zugang zum kritischen Bereich" offset (0, 50) from
    // "header" NW color black font SansSerif size 16 bold
    Text algoSit0Detail = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW),
        "Der Prozess möchte Zugang zum kritischen Bereich", "algoSit0Detail",
        null, (TextProperties) properties.get("algoSitDetailProps"));
    textElements.put("algoSit0Detail", algoSit0Detail);

    // text "algoSituation1Detail" "Der Prozess verlässt den kritischen Bereich"
    // offset (0, 50) from "header" NW color black font SansSerif size 16 bold
    Text algoSit1Detail = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW),
        "Der Prozess verlässt den kritischen Bereich", "algoSit1Detail", null,
        (TextProperties) properties.get("algoSitDetailProps"));
    textElements.put("algoSit1Detail", algoSit1Detail);

    // text "algoSituation2Detail"
    // "Der Prozess erhält eine Anfrage von einem anderen Prozess" offset (0,
    // 50) from "header" NW color black font SansSerif size 16 bold
    Text algoSit2Detail = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW),
        "Der Prozess erhält eine Anfrage von einem anderen Prozess",
        "algoSit2Detail", null, (TextProperties) properties
            .get("algoSitDetailProps"));
    textElements.put("algoSit2Detail", algoSit2Detail);

    // text "algoSituation3Detail"
    // "Der Prozess erhält eine Freigabe von einem anderen Prozess" offset (0,
    // 50) from "header" NW color black font SansSerif size 16 bold
    Text algoSit3Detail = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW),
        "Der Prozess erhält eine Freigabe von einem anderen Prozess",
        "algoSit3Detail", null, (TextProperties) properties
            .get("algoSitDetailProps"));
    textElements.put("algoSit3Detail", algoSit3Detail);

    Text processQueues = lang.newText(new Coordinates(1000, 220),
        "Prozesswarteschlangen:", "processQueues", null,
        (TextProperties) properties.get("processQueuesProps"));
    textElements.put("processQueues", processQueues);

    // actions (Request!, Release!, Ok!)
    Text processReq = lang.newText(new Coordinates(670, 370), "Request!",
        "processReq", null, (TextProperties) properties.get("actionProps"));
    Text processRel = lang.newText(new Coordinates(670, 370), "Release!",
        "processRel", null, (TextProperties) properties.get("actionProps"));
    Text processOk = lang.newText(new Coordinates(690, 370), "Ok!",
        "processOk", null, (TextProperties) properties.get("actionProps"));
    textElements.put("processReq", processReq);
    textElements.put("processRel", processRel);
    textElements.put("processOk", processOk);

    // end of animation
    Text end0 = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW), "Das war der Maekawa-Algorithmus", "end0",
        null, (TextProperties) properties.get("algoSitDetailProps"));
    Text end1 = lang.newText(new Offset(0, 30, "end0",
        AnimalScript.DIRECTION_NW),
        "Antworten an einen Prozess, der bereits im kritischen Bereich war",
        "end1", null, (TextProperties) properties.get("votingSetProps"));
    Text end2 = lang.newText(new Offset(0, 20, "end1",
        AnimalScript.DIRECTION_NW), "(STATE != WANTED), wurden verworfen.",
        "end2", null, (TextProperties) properties.get("votingSetProps"));
    textElements.put("end0", end0);
    textElements.put("end1", end1);
    textElements.put("end2", end2);
  }

  /**
   * Defines Votingsets
   */
  public void defineVotingSets() {
    for (int i = 0; i < uVotingSets.length; i++) {
      Stack<Integer> set = new Stack<Integer>();
      String setName = "set" + i;
      for (int j = 0; j < uVotingSets[i].length; j++) {
        set.add(uVotingSets[i][j]);
      }
      sets.put(setName, set);
    }
  }

  /**
   * Defines the votingset explanation
   */
  public void defineVotingSetExp() {

    // votingset explanation
    Text votingSet0 = lang.newText(new Offset(0, 50, "header",
        AnimalScript.DIRECTION_NW), "Wir teilen nun in Votingsets ein",
        "votingSet0", null, (TextProperties) properties
            .get("algoSitDetailProps"));
    Text votingSet1 = lang.newText(new Offset(0, 30, "votingSet0",
        AnimalScript.DIRECTION_NW),
        "In einem Votingset befinden sich K Prozesse.", "votingSet1", null,
        (TextProperties) properties.get("votingSetProps"));
    Text votingSet2 = lang.newText(new Offset(0, 20, "votingSet1",
        AnimalScript.DIRECTION_NW),
        "Um ein Möglichst kleines K zu erhalten muss K = 2*sqrt(N)-1 gelten.",
        "votingSet2", null, (TextProperties) properties.get("votingSetProps"));
    Text votingSet3 = lang.newText(new Offset(0, 20, "votingSet2",
        AnimalScript.DIRECTION_NW), "Wobei N die Anzahl an Prozessen ist.",
        "votingSet3", null, (TextProperties) properties.get("votingSetProps"));

    String votingSet4Text = "In unserem Fall ist N = " + nProcesses + ".";
    Text votingSet4 = lang.newText(new Offset(0, 40, "votingSet3",
        AnimalScript.DIRECTION_NW), votingSet4Text, "votingSet4", null,
        (TextProperties) properties.get("votingSetProps"));

    long k = Math.round(2 * Math.sqrt(Double.valueOf(nProcesses)) - 1);
    String votingSet5Text = "Somit sollte K = 2*sqrt(" + nProcesses + ")-1 = "
        + k + " sein.";
    Text votingSet5 = lang.newText(new Offset(0, 20, "votingSet4",
        AnimalScript.DIRECTION_NW), votingSet5Text, "votingSet5", null,
        (TextProperties) properties.get("votingSetProps"));
    textElements.put("votingSet0", votingSet0);
    textElements.put("votingSet1", votingSet1);
    textElements.put("votingSet2", votingSet2);
    textElements.put("votingSet3", votingSet3);
    textElements.put("votingSet4", votingSet4);
    textElements.put("votingSet5", votingSet5);
  }

  /**
   * Defines votingset labels
   */
  public void defineVotingSetsLabel() {
    // votingset labels
    Set<String> keySet = sets.keySet();
    Iterator<String> it = keySet.iterator();
    while (it.hasNext()) {
      String next = it.next();
      String setNumber = next.substring(3, next.length());

      String setName = "set" + setNumber + "Label";
      String setText = "Votingset " + setNumber + "(" + sets.get(next).get(0);
      for (int i = 1; i < sets.get(next).size(); i++) {
        setText += "," + sets.get(next).get(i);
      }
      setText += ")";
      Text setLabel = lang.newText(new Offset(10, 0, "set" + setNumber,
          AnimalScript.DIRECTION_NE), setText, setName, null,
          (TextProperties) properties.get("setLabelProps"));
      textElements.put(setName, setLabel);
    }
  }

  /**
   * Defines process dependant text elements
   */
  public void defineProcessDependantTextElements() {
    // VOTED = TRUE

    Text process0VotedTElement = lang.newText(new Offset(-30, -20, "process0",
        AnimalScript.DIRECTION_NW), "VOTED = TRUE", "process0VotedT", null,
        (TextProperties) properties.get("variablesTextProps"));
    textElements.put("process0VotedT", process0VotedTElement);

    Integer vXT = 0;
    Integer vYT = 0;
    String vTDir = "";
    boolean toggleVXT = false;

    Integer limitVT = 0;
    if (nProcesses % 2 == 0)
      limitVT = nProcesses - 1;
    else
      limitVT = nProcesses;

    for (int i = 1; i < limitVT; i += 2) {
      for (int j = 0; j < 2; j++) {

        if (toggleVXT) {
          vXT = 20;
          vTDir = AnimalScript.DIRECTION_E;
          toggleVXT = false;
        } else {
          vXT = -140;
          vTDir = AnimalScript.DIRECTION_W;
          toggleVXT = true;
        }

        String processVotedT = "process" + (i + j);
        String votedTName = "process" + (i + j) + "VotedT";

        Text processVotedTElement = lang.newText(new Offset(vXT, vYT,
            processVotedT, vTDir), "VOTED = TRUE", votedTName, null,
            (TextProperties) properties.get("variablesTextProps"));
        textElements.put(votedTName, processVotedTElement);
      }
    }

    if (nProcesses % 2 == 0) {
      String processVotedT = "process" + (nProcesses - 1);
      String votedTName = "process" + (nProcesses - 1) + "VotedT";

      Text processVotedTElement = lang.newText(new Offset(-30, 20,
          processVotedT, AnimalScript.DIRECTION_SW), "VOTED = TRUE",
          votedTName, null, (TextProperties) properties
              .get("variablesTextProps"));
      textElements.put(votedTName, processVotedTElement);
    }

    // VOTED = FALSE

    Text process0VotedFElement = lang.newText(new Offset(-30, -20, "process0",
        AnimalScript.DIRECTION_NW), "VOTED = FALSE", "process0VotedF", null,
        (TextProperties) properties.get("variablesTextProps"));
    textElements.put("process0VotedF", process0VotedFElement);

    Integer vXF = 0;
    Integer vYF = 0;
    String vFDir = "";
    boolean toggleVXF = false;

    Integer limitVF = 0;
    if (nProcesses % 2 == 0)
      limitVF = nProcesses - 1;
    else
      limitVF = nProcesses;

    for (int i = 1; i < limitVF; i += 2) {
      for (int j = 0; j < 2; j++) {

        if (toggleVXF) {
          vXF = 20;
          vFDir = AnimalScript.DIRECTION_E;
          toggleVXF = false;
        } else {
          vXF = -140;
          vFDir = AnimalScript.DIRECTION_W;
          toggleVXF = true;
        }

        String processVotedF = "process" + (i + j);
        String votedFName = "process" + (i + j) + "VotedF";

        Text processVotedFElement = lang.newText(new Offset(vXF, vYF,
            processVotedF, vFDir), "VOTED = FALSE", votedFName, null,
            (TextProperties) properties.get("variablesTextProps"));
        textElements.put(votedFName, processVotedFElement);
      }
    }

    if (nProcesses % 2 == 0) {
      String processVotedF = "process" + (nProcesses - 1);
      String votedFName = "process" + (nProcesses - 1) + "VotedF";

      Text processVotedFElement = lang.newText(new Offset(-30, 20,
          processVotedF, AnimalScript.DIRECTION_SW), "VOTED = FALSE",
          votedFName, null, (TextProperties) properties
              .get("variablesTextProps"));
      textElements.put(votedFName, processVotedFElement);
    }

    // STATE = RELEASED

    Text process0StateRElement = lang.newText(new Offset(-30, -40, "process0",
        AnimalScript.DIRECTION_NW), "STATE = RELEASED", "process0StateR", null,
        (TextProperties) properties.get("variablesTextProps"));
    textElements.put("process0StateR", process0StateRElement);

    Integer sXR = 0;
    Integer sYR = -20;
    String sRDir = "";
    boolean toggleSXR = false;

    Integer limitSR = 0;
    if (nProcesses % 2 == 0)
      limitSR = nProcesses - 1;
    else
      limitSR = nProcesses;

    for (int i = 1; i < limitSR; i += 2) {
      for (int j = 0; j < 2; j++) {

        if (toggleSXR) {
          sXR = 20;
          sRDir = AnimalScript.DIRECTION_E;
          toggleSXR = false;
        } else {
          sXR = -140;
          sRDir = AnimalScript.DIRECTION_W;
          toggleSXR = true;
        }

        String processStateR = "process" + (i + j);
        String stateRName = "process" + (i + j) + "StateR";

        Text processStateRElement = lang.newText(new Offset(sXR, sYR,
            processStateR, sRDir), "STATE = RELEASED", stateRName, null,
            (TextProperties) properties.get("variablesTextProps"));
        textElements.put(stateRName, processStateRElement);
      }
    }

    if (nProcesses % 2 == 0) {
      String processStateR = "process" + (nProcesses - 1);
      String stateRName = "process" + (nProcesses - 1) + "StateR";

      Text processStateRElement = lang.newText(new Offset(-30, 40,
          processStateR, AnimalScript.DIRECTION_SW), "STATE = RELEASED",
          stateRName, null, (TextProperties) properties
              .get("variablesTextProps"));
      textElements.put(stateRName, processStateRElement);
    }

    // STATE = WANTED

    Text process0StateWElement = lang.newText(new Offset(-30, -40, "process0",
        AnimalScript.DIRECTION_NW), "STATE = WANTED", "process0StateW", null,
        (TextProperties) properties.get("variablesTextProps"));
    textElements.put("process0StateW", process0StateWElement);

    Integer sXW = 0;
    Integer sYW = -20;
    String sWDir = "";
    boolean toggleSXW = false;

    Integer limitSW = 0;
    if (nProcesses % 2 == 0)
      limitSW = nProcesses - 1;
    else
      limitSW = nProcesses;

    for (int i = 1; i < limitSW; i += 2) {
      for (int j = 0; j < 2; j++) {

        if (toggleSXW) {
          sXW = 20;
          sWDir = AnimalScript.DIRECTION_E;
          toggleSXW = false;
        } else {
          sXW = -140;
          sWDir = AnimalScript.DIRECTION_W;
          toggleSXW = true;
        }

        String processStateW = "process" + (i + j);
        String stateWName = "process" + (i + j) + "StateW";

        Text processStateWElement = lang.newText(new Offset(sXW, sYW,
            processStateW, sWDir), "STATE = WANTED", stateWName, null,
            (TextProperties) properties.get("variablesTextProps"));
        textElements.put(stateWName, processStateWElement);
      }
    }

    if (nProcesses % 2 == 0) {
      String processStateW = "process" + (nProcesses - 1);
      String stateWName = "process" + (nProcesses - 1) + "StateW";

      Text processStateWElement = lang.newText(new Offset(-30, 40,
          processStateW, AnimalScript.DIRECTION_SW), "STATE = WANTED",
          stateWName, null, (TextProperties) properties
              .get("variablesTextProps"));
      textElements.put(stateWName, processStateWElement);
    }

    // STATE = HELD

    Text process0StateHElement = lang.newText(new Offset(-30, -40, "process0",
        AnimalScript.DIRECTION_NW), "STATE = HELD", "process0StateH", null,
        (TextProperties) properties.get("variablesTextProps"));
    textElements.put("process0StateH", process0StateHElement);

    Integer sXH = 0;
    Integer sYH = -20;
    String sHDir = "";
    boolean toggleSXH = false;

    Integer limitSH = 0;
    if (nProcesses % 2 == 0)
      limitSH = nProcesses - 1;
    else
      limitSH = nProcesses;

    for (int i = 1; i < limitSH; i += 2) {
      for (int j = 0; j < 2; j++) {

        if (toggleSXH) {
          sXH = 20;
          sHDir = AnimalScript.DIRECTION_E;
          toggleSXH = false;
        } else {
          sXH = -140;
          sHDir = AnimalScript.DIRECTION_W;
          toggleSXH = true;
        }

        String processStateH = "process" + (i + j);
        String stateHName = "process" + (i + j) + "StateH";

        Text processStateHElement = lang.newText(new Offset(sXH, sYH,
            processStateH, sHDir), "STATE = HELD", stateHName, null,
            (TextProperties) properties.get("variablesTextProps"));
        textElements.put(stateHName, processStateHElement);
      }
    }

    if (nProcesses % 2 == 0) {
      String processStateH = "process" + (nProcesses - 1);
      String stateHName = "process" + (nProcesses - 1) + "StateH";

      Text processStateHElement = lang.newText(new Offset(-30, 40,
          processStateH, AnimalScript.DIRECTION_SW), "STATE = HELD",
          stateHName, null, (TextProperties) properties
              .get("variablesTextProps"));
      textElements.put(stateHName, processStateHElement);
    }

    // information which process wants access to the critical section
    for (int i = 0; i < nProcesses; i++) {
      String text = "Prozess " + i + " möchte Zugang zum kritischen Bereich!";
      String name = "processInfo" + i + "Req";
      Text processInfoReq = lang.newText(new Coordinates(20, 355), text, name,
          null, (TextProperties) properties.get("processInfoProps"));
      textElements.put(name, processInfoReq);
    }

    // information which process enters the critical section
    for (int i = 0; i < nProcesses; i++) {
      String text = "Prozess " + i + " betritt den kritischen Bereich!";
      String name = "processInfo" + i + "A";
      Text processInfoA = lang.newText(new Coordinates(20, 355), text, name,
          null, (TextProperties) properties.get("processInfoProps"));
      textElements.put(name, processInfoA);
    }

    // information which process receives a request
    for (int i = 0; i < nProcesses; i++) {
      String text = "Prozess " + i + " hat eine Anfrage erhalten!";
      String name = "processInfo" + i + "RReq";
      Text processInfoRReq = lang.newText(new Coordinates(20, 355), text, name,
          null, (TextProperties) properties.get("processInfoProps"));
      textElements.put(name, processInfoRReq);
    }

    // information which process leaves the critical section
    for (int i = 0; i < nProcesses; i++) {
      String text = "Prozess " + i + " verlässt den kritischen Bereich!";
      String name = "processInfo" + i + "Rel";
      Text processInfoRel = lang.newText(new Coordinates(20, 355), text, name,
          null, (TextProperties) properties.get("processInfoProps"));
      textElements.put(name, processInfoRel);
    }

    // information which process receives a release
    for (int i = 0; i < nProcesses; i++) {
      String text = "Prozess " + i + " hat eine Freigabe erhalten!";
      String name = "processInfo" + i + "RRel";
      Text processInfoRRel = lang.newText(new Coordinates(20, 355), text, name,
          null, (TextProperties) properties.get("processInfoProps"));
      textElements.put(name, processInfoRRel);
    }

  }

  /**
   * Defines code elements with content
   */
  public void defineCodeElements() {

    // codeGroup "algoSituation0Code" offset (0, 30) from "algoSituation0Detail"
    // color black highlightColor red font SansSerif size 14
    SourceCode algoSit0Code = lang.newSourceCode(new Offset(0, 30,
        "algoSit0Detail", AnimalScript.DIRECTION_NW), "algoSit0Code", null,
        (SourceCodeProperties) properties.get("algoCodeProps"));
    // addCodeLine "- Setze den aktuellen Zustand auf WANTED" to
    // "algoSituation0Code"
    algoSit0Code.addCodeLine("- Setze den aktuellen Zustand auf WANTED", null,
        0, null);
    // addCodeLine "- Sende eine Anfrage an alle Prozesse im Votingset" to
    // "algoSituation0Code"
    algoSit0Code.addCodeLine(
        "- Sende eine Anfrage an alle Prozesse im Votingset", null, 0, null);
    // addCodeLine "- Warte auf K-1 Antworten" to "algoSituation0Code"
    algoSit0Code.addCodeLine("- Warte auf K-1 Antworten", null, 0, null);
    // addCodeLine "- Setze den aktuellen Zustand auf HELD" to
    // "algoSituation0Code"
    algoSit0Code.addCodeLine("- Setze den aktuellen Zustand auf HELD", null, 0,
        null);
    algoSit0Code.hide(); // hidden Attribut funktioniert nicht da Code
                         // hinzugefügt wird nach Definition von Codegroup
    codeElements.put("algoSit0Code", algoSit0Code);

    // codeGroup "algoSituation1Code" offset (0, 30) from "algoSituation1Detail"
    // color black highlightColor red font SansSerif size 14
    SourceCode algoSit1Code = lang.newSourceCode(new Offset(0, 30,
        "algoSit1Detail", AnimalScript.DIRECTION_NW), "algoSit1Code", null,
        (SourceCodeProperties) properties.get("algoCodeProps"));
    // addCodeLine "- Setze den aktuellen Zustand auf RELEASED" to
    // "algoSituation1Code"
    algoSit1Code.addCodeLine("- Setze den aktuellen Zustand auf RELEASED",
        null, 0, null);
    // addCodeLine "- Sende eine Freigabe an alle Prozesse im Votingset" to
    // "algoSituation1Code"
    algoSit1Code.addCodeLine(
        "- Sende eine Freigabe an alle Prozesse im Votingset", null, 0, null);
    algoSit1Code.hide(); // hidden Attribut funktioniert nicht da Code
                         // hinzugefügt wird nach Definition von Codegroup
    codeElements.put("algoSit1Code", algoSit1Code);

    // codeGroup "algoSituation2Code" offset (0, 30) from "algoSituation2Detail"
    // color black highlightColor red font SansSerif size 14
    SourceCode algoSit2Code = lang.newSourceCode(new Offset(0, 30,
        "algoSit2Detail", AnimalScript.DIRECTION_NW), "algoSit2Code", null,
        (SourceCodeProperties) properties.get("algoCodeProps"));
    // addCodeLine "- Wenn STATE = HELD oder VOTED = TRUE" to
    // "algoSituation2Code"
    algoSit2Code.addCodeLine("- Wenn STATE = HELD oder VOTED = TRUE", null, 0,
        null);
    // addCodeLine "- Dann Anfrage in die Warteschlange einsortieren" to
    // "algoSituation2Code"
    algoSit2Code.addCodeLine(
        "- Dann Anfrage in die Warteschlange einsortieren", null, 0, null);
    // addCodeLine "- Sonst" to "algoSituation2Code"
    algoSit2Code.addCodeLine("- Sonst", null, 0, null);
    // addCodeLine "- Sende Antwort" to "algoSituation2Code"
    algoSit2Code.addCodeLine("- Sende Antwort", null, 0, null);
    // addCodeLine "- Setze VOTED auf TRUE" to "algoSituation2Code"
    algoSit2Code.addCodeLine("- Setze VOTED auf TRUE", null, 0, null);
    algoSit2Code.hide(); // hidden Attribut funktioniert nicht da Code
                         // hinzugefügt wird nach Definition von Codegroup
    codeElements.put("algoSit2Code", algoSit2Code);

    // codeGroup "algoSituation3Code" offset (0, 30) from "algoSituation3Detail"
    // color black highlightColor red font SansSerif size 14
    SourceCode algoSit3Code = lang.newSourceCode(new Offset(0, 30,
        "algoSit3Detail", AnimalScript.DIRECTION_NW), "algoSit3Code", null,
        (SourceCodeProperties) properties.get("algoCodeProps"));
    // addCodeLine "- Wenn Warteschlange leer ist" to "algoSituation3Code"
    algoSit3Code.addCodeLine("- Wenn Warteschlange leer ist", null, 0, null);
    // addCodeLine "- Setze VOTED auf FALSE" to "algoSituation3Code"
    algoSit3Code.addCodeLine("- Setze VOTED auf FALSE", null, 0, null);
    // addCodeLine "- Sonst" to "algoSituation3Code"
    algoSit3Code.addCodeLine("- Sonst", null, 0, null);
    // addCodeLine
    // "- Sende Antwort an ersten Prozess aus Warteschlange und entferne den Prozess"
    // to "algoSituation3Code"
    algoSit3Code
        .addCodeLine(
            "- Sende Antwort an ersten Prozess aus Warteschlange und entferne den Prozess",
            null, 0, null);
    // addCodeLine "- Setze VOTED auf TRUE" to "algoSituation3Code"
    algoSit3Code.addCodeLine("- Setze VOTED auf TRUE", null, 0, null);
    algoSit3Code.hide(); // hidden Attribut funktioniert nicht da Code
                         // hinzugefügt wird nach Definition von Codegroup
    codeElements.put("algoSit3Code", algoSit3Code);
  }

  /**
   * Defines circle elements
   */
  public void defineCircleElements() {
    Circle process0 = lang.newCircle(new Coordinates(700, 150), 20, "process0",
        null, (CircleProperties) properties.get("circleProps"));
    circleElements.put("process0", process0);

    Integer x = 0;
    Integer y = 250;
    boolean toggleX = false;

    Integer limit = 0;
    if (nProcesses % 2 == 0)
      limit = nProcesses - 1;
    else
      limit = nProcesses;

    for (int i = 1; i < limit; i += 2) {

      for (int j = 0; j < 2; j++) {

        if (toggleX) {
          x = 775;
          toggleX = false;
        } else {
          x = 625;
          toggleX = true;
        }

        String name = "process" + (i + j);
        Circle process = lang.newCircle(new Coordinates(x, y), 20, name, null,
            (CircleProperties) properties.get("circleProps"));
        circleElements.put(name, process);
      }

      y += 100;
    }

    if (nProcesses % 2 == 0) {
      String name = "process" + (nProcesses - 1);
      Circle process = lang.newCircle(new Coordinates(700, y), 20, name, null,
          (CircleProperties) properties.get("circleProps"));
      circleElements.put(name, process);
    }
  }

  /**
   * Defines stringarray elements with content
   */
  public void defineStringArrayElements() {

    String[] a = new String[threads.size()];

    for (int i = 0; i < a.length; i++) {
      a[i] = "null";
    }

    StringArray process0Queue = lang.newStringArray(new Coordinates(1000, 270),
        a, "process0Queue", null,
        (ArrayProperties) properties.get("processQueueProps"));
    stringArrayElements.put("process0Queue", process0Queue);

    for (int i = 1; i < nProcesses; i++) {
      String name = "process" + i + "Queue";
      String predecessor = "process" + (i - 1) + "Queue";
      StringArray processQueue = lang.newStringArray(new Offset(20, 0,
          predecessor, AnimalScript.DIRECTION_NE), a, name, null,
          (ArrayProperties) properties.get("processQueueProps"));
      processQueue.hide();
      stringArrayElements.put(name, processQueue);
    }

  }

  /**
   * Defines the labels for the processqueues
   */
  public void defineQueueLabels() {
    Text process0QueueLabel = lang.newText(new Coordinates(1010, 250), "0",
        "process0QueueLabel", null,
        (TextProperties) properties.get("processQueueLabelProps"));
    textElements.put("process0QueueLabel", process0QueueLabel);

    for (int i = 1; i < nProcesses; i++) {
      String name = "process" + i + "QueueLabel";
      String predecessor = "process" + (i - 1) + "QueueLabel";
      Text processQueueLabel = lang.newText(new Offset(40, 0, predecessor,
          AnimalScript.DIRECTION_NE), String.valueOf(i), name, null,
          (TextProperties) properties.get("processQueueLabelProps"));
      textElements.put(name, processQueueLabel);
    }
  }

  /**
   * Defines the processnames in the circles
   */
  public void defineCircleText() {
    Text process0Name = lang.newText(new Coordinates(695, 140), "0",
        "process0Name", null,
        (TextProperties) properties.get("circleTextProps"));
    textElements.put("process0Name", process0Name);

    Integer x = 0;
    Integer y = 240;
    boolean toggleX = false;

    Integer limit = 0;
    if (nProcesses % 2 == 0)
      limit = nProcesses - 1;
    else
      limit = nProcesses;

    for (int i = 1; i < limit; i += 2) {

      for (int j = 0; j < 2; j++) {

        if (toggleX) {
          x = 770;
          toggleX = false;
        } else {
          x = 620;
          toggleX = true;
        }

        String name = "process" + (i + j) + "Name";
        Text processName = lang.newText(new Coordinates(x, y),
            String.valueOf((i + j)), name, null,
            (TextProperties) properties.get("circleTextProps"));
        textElements.put(name, processName);
      }

      y += 100;
    }

    if (nProcesses % 2 == 0) {
      String name = "process" + (nProcesses - 1) + "Name";
      Text processName = lang.newText(new Coordinates(695, y),
          String.valueOf((nProcesses - 1)), name, null,
          (TextProperties) properties.get("circleTextProps"));
      textElements.put(name, processName);
    }
  }

  /**
   * Defines square elements
   */
  public void defineSquareElements() {
    Set<String> keySet = sets.keySet();
    Iterator<String> it = keySet.iterator();
    Integer y;
    while (it.hasNext()) {
      String next = it.next();
      String setNumber = next.substring(3, next.length());

      Integer iSetNumber = Integer.valueOf(setNumber);

      y = 50 + (iSetNumber * 20);

      // square elements
      Square set = lang.newSquare(new Coordinates(800, y), 20, next, null,
          (SquareProperties) properties.get("squareProps" + iSetNumber));
      squareElements.put(next, set);
    }
  }

  /**
   * Defines line elements between processes (circles)
   * 
   * nodeAB: A = from process (circle) B = to process (circle)
   */
  public void defineLineElements() {

    Integer limit;
    if (nProcesses % 2 == 0)
      limit = nProcesses - 1;
    else
      limit = nProcesses;

    boolean toggle0 = false;
    String dirA;
    String dirB;
    for (int i = 1; i < limit; i++) {
      if (!toggle0) {
        dirA = AnimalScript.DIRECTION_SW;
        dirB = AnimalScript.DIRECTION_NE;
        toggle0 = true;
      } else {
        dirA = AnimalScript.DIRECTION_SE;
        dirB = AnimalScript.DIRECTION_NW;
        toggle0 = false;
      }

      String processA = "process0";
      String processB = "process" + i;
      Node[] node = { new Offset(0, 0, processA, dirA),
          new Offset(0, 0, processB, dirB) };

      String lineName = "line0" + i;
      Polyline line = lang.newPolyline(node, lineName, null,
          (PolylineProperties) properties.get("lineProps"));
      lineElements.put(lineName, line);
    }

    if (nProcesses % 2 == 0) {
      String processA = "process0";
      String processB = "process" + (nProcesses - 1);
      Node[] node = { new Offset(0, 0, processA, AnimalScript.DIRECTION_S),
          new Offset(0, 0, processB, AnimalScript.DIRECTION_N) };

      String lineName = "line0" + (nProcesses - 1);
      Polyline line = lang.newPolyline(node, lineName, null,
          (PolylineProperties) properties.get("lineProps"));
      lineElements.put(lineName, line);
    }

    for (int i = 1; i < limit; i++) {
      for (int j = 1; j < limit; j++) {
        if (i != j) {
          String processA = "process" + i;
          String processB = "process" + j;

          if (i % 2 == 0) {
            if (i < j) {
              if (j % 2 == 0) {
                dirA = AnimalScript.DIRECTION_S;
                dirB = AnimalScript.DIRECTION_N;
              } else {
                dirA = AnimalScript.DIRECTION_SW;
                dirB = AnimalScript.DIRECTION_NE;
              }
            } else {
              if (i - 1 == j) {
                dirA = AnimalScript.DIRECTION_W;
                dirB = AnimalScript.DIRECTION_E;
              } else if (j % 2 == 0) {
                dirA = AnimalScript.DIRECTION_N;
                dirB = AnimalScript.DIRECTION_S;
              } else {
                dirA = AnimalScript.DIRECTION_NW;
                dirB = AnimalScript.DIRECTION_SE;
              }
            }
          } else {
            if (i < j) {
              if (i + 1 == j) {
                dirA = AnimalScript.DIRECTION_E;
                dirB = AnimalScript.DIRECTION_W;
              } else if (j % 2 != 0) {
                dirA = AnimalScript.DIRECTION_S;
                dirB = AnimalScript.DIRECTION_N;
              } else {
                dirA = AnimalScript.DIRECTION_SE;
                dirB = AnimalScript.DIRECTION_NW;
              }
            } else {
              if (j % 2 != 0) {
                dirA = AnimalScript.DIRECTION_N;
                dirB = AnimalScript.DIRECTION_S;
              } else {
                dirA = AnimalScript.DIRECTION_NE;
                dirB = AnimalScript.DIRECTION_SW;
              }
            }
          }

          Node[] node = { new Offset(0, 0, processA, dirA),
              new Offset(0, 0, processB, dirB) };

          String lineName = "line" + i + j;
          Polyline line = lang.newPolyline(node, lineName, null,
              (PolylineProperties) properties.get("lineProps"));
          lineElements.put(lineName, line);
        }
      }
    }

    boolean toggle1 = false;
    for (int i = 1; i < limit; i++) {
      String processA = "process" + i;
      String processB = "process0";

      if (!toggle1) {
        dirA = AnimalScript.DIRECTION_NE;
        dirB = AnimalScript.DIRECTION_SW;
        toggle1 = true;
      } else {
        dirA = AnimalScript.DIRECTION_NW;
        dirB = AnimalScript.DIRECTION_SE;
        toggle1 = false;
      }

      Node[] node = { new Offset(0, 0, processA, dirA),
          new Offset(0, 0, processB, dirB) };

      String lineName = "line" + i + "0";
      Polyline line = lang.newPolyline(node, lineName, null,
          (PolylineProperties) properties.get("lineProps"));
      lineElements.put(lineName, line);
    }

    if (nProcesses % 2 == 0) {
      boolean toggle2 = false;
      for (int i = 1; i < limit; i++) {
        String processA = "process" + i;
        String processB = "process" + (nProcesses - 1);

        if (!toggle2) {
          dirA = AnimalScript.DIRECTION_SE;
          dirB = AnimalScript.DIRECTION_NW;
          toggle2 = true;
        } else {
          dirA = AnimalScript.DIRECTION_SW;
          dirB = AnimalScript.DIRECTION_NE;
        }

        Node[] node = { new Offset(0, 0, processA, dirA),
            new Offset(0, 0, processB, dirB) };

        String lineName = "line" + i + (nProcesses - 1);
        Polyline line = lang.newPolyline(node, lineName, null,
            (PolylineProperties) properties.get("lineProps"));
        lineElements.put(lineName, line);
      }

      for (int i = 1; i < limit; i++) {
        String processA = "process" + (nProcesses - 1);
        String processB = "process" + i;

        if (!toggle2) {
          dirA = AnimalScript.DIRECTION_NW;
          dirB = AnimalScript.DIRECTION_SE;
          toggle2 = true;
        } else {
          dirA = AnimalScript.DIRECTION_NE;
          dirB = AnimalScript.DIRECTION_SW;
        }

        Node[] node = { new Offset(0, 0, processA, dirA),
            new Offset(0, 0, processB, dirB) };

        String lineName = "line" + (nProcesses - 1) + i;
        Polyline line = lang.newPolyline(node, lineName, null,
            (PolylineProperties) properties.get("lineProps"));
        lineElements.put(lineName, line);
      }

      String processA = "process" + (nProcesses - 1);
      String processB = "process0";

      Node[] node = { new Offset(0, 0, processA, AnimalScript.DIRECTION_N),
          new Offset(0, 0, processB, AnimalScript.DIRECTION_S) };

      String lineName = "line" + (nProcesses - 1) + "0";
      Polyline line = lang.newPolyline(node, lineName, null,
          (PolylineProperties) properties.get("lineProps"));
      lineElements.put(lineName, line);
    }

  }

  /**
   * Shows the circles for the processes
   */
  public void showCircles() {
    for (int i = 0; i < nProcesses; i++) {
      String name = "process" + i;
      circleElements.get(name).show();
    }
  }

  /**
   * Shows the process names in the circles
   */
  public void showCircleText() {
    for (int i = 0; i < nProcesses; i++) {
      String name = "process" + i + "Name";
      textElements.get(name).show();
    }
  }

  /**
   * Shows initial process variables
   */
  public void showVariables() {
    for (int i = 0; i < nProcesses; i++) {
      String votedName = "process" + i + "VotedF";
      textElements.get(votedName).show();

      String nameState = "process" + i + "StateR";
      textElements.get(nameState).show();
    }
  }

  /**
   * Shows the processqueues
   */
  public void showQueues() {
    textElements.get("processQueues").show();

    lang.nextStep();

    for (int i = 0; i < nProcesses; i++) {
      String processQueueLabelname = "process" + i + "QueueLabel";
      textElements.get(processQueueLabelname).show();
      String processQueueName = "process" + i + "Queue";
      stringArrayElements.get(processQueueName).show();
    }

    lang.nextStep();
  }

  public MaekawaAlgorithm getAlgo() {
    return algo;
  }

  public void setAlgo(MaekawaAlgorithm algo) {
    this.algo = algo;
  }

  public HashMap<String, Text> getTextElements() {
    return textElements;
  }

  public void setTextElements(HashMap<String, Text> textElements) {
    this.textElements = textElements;
  }

  public HashMap<String, SourceCode> getCodeElements() {
    return codeElements;
  }

  public void setCodeElements(HashMap<String, SourceCode> codeElements) {
    this.codeElements = codeElements;
  }

  public HashMap<String, Circle> getCircleElements() {
    return circleElements;
  }

  public void setCircleElements(HashMap<String, Circle> circleElements) {
    this.circleElements = circleElements;
  }

  public HashMap<String, StringArray> getStringArrayElements() {
    return stringArrayElements;
  }

  public void setStringArrayElements(
      HashMap<String, StringArray> stringArrayElements) {
    this.stringArrayElements = stringArrayElements;
  }

  public HashMap<String, Square> getSquareElements() {
    return squareElements;
  }

  public void setSquareElements(HashMap<String, Square> squareElements) {
    this.squareElements = squareElements;
  }

  public HashMap<String, Polyline> getLineElements() {
    return lineElements;
  }

  public void setLineElements(HashMap<String, Polyline> lineElements) {
    this.lineElements = lineElements;
  }

  public HashMap<String, Object> getProperties() {
    return properties;
  }

  public void setProperties(HashMap<String, Object> properties) {
    this.properties = properties;
  }

  public Language getLang() {
    return lang;
  }

  public void setLang(Language lang) {
    this.lang = lang;
  }
}
