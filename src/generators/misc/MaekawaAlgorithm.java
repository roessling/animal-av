package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Font;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;
import java.util.Stack;

import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.helpers.MaekawaAnimation;
import generators.misc.helpers.MaekawaThread;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.ArrayProperties;

public class MaekawaAlgorithm implements ValidatingGenerator {
  private Language                              lang;
  private int[]                                 processes;
  private int[][]                               uVotingSets;
  private int[]                                 processDelay;
  private TextProperties                        headerProps;
  private TextProperties                        processQueuesProps;
  private TextProperties                        algoSitDetailProps;
  private SourceCodeProperties                  algoCodeProps;
  private ArrayProperties                       processQueueProps;
  private TextProperties                        algoQuestProps;
  private TextProperties                        processQueueLabelProps;
  private TextProperties                        infoProps;
  private TextProperties                        circleTextProps;
  private TextProperties                        actionProps;
  private TextProperties                        processInfoProps;

  private MaekawaAnimation                      animation;
  private Integer                               finished;
  private Integer                               nProcesses;

  private HashMap<String, Integer>              threads;
  private HashMap<Integer, Integer[]>           processVariables;
  private HashMap<Integer, LinkedList<Integer>> processQueues;
  private HashMap<String, Object>               properties;
  private HashMap<Integer, Stack<Integer>>      votingSets;

  public void init() {
    lang = new AnimalScript("MaekawaAlgorithm", "Marco Torsello, Arvid Lange",
        1024, 768);
    properties = new HashMap<String, Object>();
    threads = new HashMap<String, Integer>();
    processVariables = new HashMap<Integer, Integer[]>();
    processQueues = new HashMap<Integer, LinkedList<Integer>>();
    votingSets = new HashMap<Integer, Stack<Integer>>();

    finished = 0;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    processes = (int[]) primitives.get("processes");
    nProcesses = (Integer) primitives.get("nProcesses");
    uVotingSets = (int[][]) primitives.get("votingSets");
    processDelay = (int[]) primitives.get("processDelay");
    headerProps = (TextProperties) props.getPropertiesByName("headerProps");
    processQueuesProps = (TextProperties) props
        .getPropertiesByName("processQueuesProps");
    algoSitDetailProps = (TextProperties) props
        .getPropertiesByName("algoSitDetailProps");
    algoCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("algoCodeProps");
    processQueueProps = (ArrayProperties) props
        .getPropertiesByName("processQueueProps");
    algoQuestProps = (TextProperties) props
        .getPropertiesByName("algoQuestProps");
    processQueueLabelProps = (TextProperties) props
        .getPropertiesByName("processQueueLabelProps");
    infoProps = (TextProperties) props.getPropertiesByName("infoProps");
    circleTextProps = (TextProperties) props
        .getPropertiesByName("circleTextProps");
    actionProps = (TextProperties) props.getPropertiesByName("actionProps");
    processInfoProps = (TextProperties) props
        .getPropertiesByName("processInfoProps");

    // header properties
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 24));

    // info properties
    infoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 18));

    // algorithm question properties
    algoQuestProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 22));

    // algorithm details properties
    algoSitDetailProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 16));

    // circle text properties
    circleTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 18));

    // process queue -> Prozesswarteschlange:
    processQueuesProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 18));

    // process queue labels -> 0 1 2 3 4
    processQueueLabelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 18));

    // process information properties
    processInfoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));

    // action properties
    actionProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 16));

    properties.put("headerProps", headerProps);
    properties.put("processQueuesProps", processQueuesProps);
    properties.put("algoSitDetailProps", algoSitDetailProps);
    properties.put("algoCodeProps", algoCodeProps);
    properties.put("processQueueProps", processQueueProps);
    properties.put("algoQuestProps", algoQuestProps);
    properties.put("processQueueLabelProps", processQueueLabelProps);
    properties.put("infoProps", infoProps);
    properties.put("circleTextProps", circleTextProps);
    properties.put("actionProps", actionProps);
    properties.put("processInfoProps", processInfoProps);

    this.setVotingSets();

    for (int i = 0; i < this.processes.length; i++) {
      if (!this.threads.containsKey(String.valueOf(this.processes[i]))) {
        threads.put(String.valueOf(this.processes[i]), 1);
      } else {
        int temp = threads.get(String.valueOf(this.processes[i]));
        temp++;
        threads.put(String.valueOf(this.processes[i]), temp);
      }
    }

    this.animation = new MaekawaAnimation(this, lang, properties, nProcesses,
        threads, uVotingSets);

    this.animation.intro();

    maekawa();

    while (this.finished != this.threads.size()) {
      try {
        Thread.sleep(5);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    this.animation.end();

    return lang.toString();
  }

  /**
   * Starts the Maekawa-Algorithm with threads
   */
  private void maekawa() {

    // initialize variables
    initialize();

    Set<String> temp = threads.keySet();
    Iterator<String> it = temp.iterator();

    // create a thread for each process and start
    while (it.hasNext()) {
      String next = it.next();
      Thread t = new Thread(new MaekawaThread(this,
          processDelay[Integer.valueOf(next)], threads.get(next),
          votingSets.get(Integer.valueOf(next))), next);
      t.start();
    }
  }

  /**
   * Initializes the variables for each process
   * 
   * Position 0 = STATE Position 1 = VOTED Position 2 = Reply counter Position 3
   * = Request received Position 4 = Release received
   * 
   * RELEASED = 0 WANTED = 1 HELD = 2
   * 
   * VOTED FALSE = 0 VOTED TRUE = 1
   * 
   * Request received FALSE = 0 Request received TRUE = 1
   * 
   * Release received FALSE = 0 Release received TRUE = 1
   */
  private void initialize() {
    for (int i = 0; i < nProcesses; i++) {
      Integer[] processVariable = { 0, 0, 0, 0, 0 };
      LinkedList<Integer> processQueue = new LinkedList<Integer>();
      this.processVariables.put(i, processVariable);
      this.processQueues.put(i, processQueue);
    }
  }

  /**
   * Sets the votingset for each process
   */
  private void setVotingSets() {
    for (int i = 0; i < nProcesses; i++) {
      Stack<Integer> votingSet = new Stack<Integer>();
      boolean toggle = false;
      for (int j = 0; j < uVotingSets.length; j++) {
        for (int n = 0; n < uVotingSets[j].length; n++) {
          if (uVotingSets[j][n] == i && !toggle) {
            toggle = true;
            n = -1;
          } else if (uVotingSets[j][n] != i && toggle
              && !votingSet.contains(uVotingSets[j][n])) {
            votingSet.add(uVotingSets[j][n]);
          }
        }
        toggle = false;
      }
      votingSets.put(i, votingSet);
    }
  }

  /**
   * Access the critical section and creates Animal animation of access
   * 
   * @param process
   *          the process who gets access
   * @param votingSet
   *          the votingSet of the given process
   */
  public void access(Integer process, Stack<Integer> votingSet) {
    if (this.processVariables.get(process)[2] >= votingSet.size() - 1) { // if
                                                                         // K-1
                                                                         // replies
                                                                         // received
                                                                         // grant
                                                                         // access
      animation.accessAnimation(process); // else look again if enough replies
                                          // received
      changeState(process, 2);
    } else {
      try {
        Thread.sleep(50);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      access(process, votingSet);
    }
  }

  /**
   * Requests access to the critical section and creates Animal animation of
   * request
   * 
   * @param process
   *          the process who requests access
   * @param votingSet
   *          the votingset of the given process
   */
  public void request(Integer process, Stack<Integer> votingSet) {
    animation.requestAnimation(process, votingSet); // first animate the request

    changeState(process, 1); // change state to WANTED
    for (int i = 0; i < votingSet.size(); i++) { // tell every process in the
                                                 // votingset
      // that he received a request
      Integer[] processVariables = this.processVariables.get(votingSet.get(i));
      processVariables[3] = 1;
      this.processVariables.put(votingSet.get(i), processVariables);
      receiveRequest(process, votingSet.get(i));
    }
    access(process, votingSet);
  }

  /**
   * Receives request to get access to the critical section and creates Animal
   * animation of receiving a request
   * 
   * @param requestingProcess
   *          the process who requests the access
   * @param receivingProcess
   *          the process who receives the request
   */
  public void receiveRequest(Integer requestingProcess, Integer receivingProcess) {
    animation.receiveRequestAnimation(requestingProcess, receivingProcess); // first
                                                                            // animate
                                                                            // the
                                                                            // receiving

    Integer[] receivingProcessVariables = this.processVariables
        .get(receivingProcess);
    Integer[] requestingProcessVariables = this.processVariables
        .get(requestingProcess);
    LinkedList<Integer> receivingProcessQueue = this.processQueues
        .get(receivingProcess);

    if (receivingProcessVariables[0] == 2 || receivingProcessVariables[1] == 1) { // if
                                                                                  // STATE
                                                                                  // =
                                                                                  // HELD
                                                                                  // or
                                                                                  // VOTED
                                                                                  // =
                                                                                  // TRUE
      receivingProcessQueue.add(requestingProcess); // add request to queue
      this.processQueues.put(receivingProcess, receivingProcessQueue);
    } else { // else
      requestingProcessVariables[2] = requestingProcessVariables[2] + 1; // reply
      this.processVariables.put(requestingProcess, requestingProcessVariables);
      changeVoted(receivingProcess, 1); // set VOTED = TRUE
    }
    receivingProcessVariables[3] = 0;
    this.processVariables.put(receivingProcess, receivingProcessVariables);
  }

  /**
   * Releases the critical section and creates Animal animation of a release
   * 
   * @param process
   *          the process who leaves the critical section
   * @param votingSet
   *          the votingset of the given process
   */
  public void release(Integer process, Stack<Integer> votingSet) {
    animation.releaseAnimation(process, votingSet); // first animate release

    changeState(process, 0); // set STATE = RELEASED

    for (int i = 0; i < votingSet.size(); i++) { // tell every process in the
                                                 // votingset
      Integer[] processVariables = this.processVariables.get(votingSet.get(i)); // that
                                                                                // he
                                                                                // received
                                                                                // a
                                                                                // release
      processVariables[4] = 1;
      this.processVariables.put(votingSet.get(i), processVariables);
      receiveRelease(process, votingSet.get(i));
    }

    Integer[] processVariables = this.processVariables.get(process);
    processVariables[2] = 0;
    this.processVariables.put(process, processVariables);
  }

  /**
   * Receives the release of the critical section and creates Animal animation
   * of receiving a release
   * 
   * @param releasingProcess
   *          the process who leaves the critical section
   * @param receivingProcess
   *          the process who receives the release
   */
  public void receiveRelease(Integer releasingProcess, Integer receivingProcess) {
    animation.receiveReleaseAnimation(releasingProcess, receivingProcess); // first
                                                                           // animate
                                                                           // receiving
                                                                           // a
                                                                           // release

    Integer[] receivingProcessVariables = this.processVariables
        .get(receivingProcess);
    LinkedList<Integer> receivingProcessQueue = this.processQueues
        .get(receivingProcess);

    if (receivingProcessQueue.isEmpty())
      changeVoted(receivingProcess, 0); // if queue is empty, set VOTED = FAlSE
    else { // else
      Integer next = receivingProcessQueue.pop(); // get first process in the
                                                  // queue and remove it
      this.processQueues.put(receivingProcess, receivingProcessQueue);

      Integer[] nextProcessVariables = this.processVariables.get(next);
      if (nextProcessVariables[0] == 1) {
        nextProcessVariables[2] = nextProcessVariables[2] + 1; // reply
        this.processVariables.put(next, nextProcessVariables);
      }
      changeVoted(receivingProcess, 1); // set VOTED = TRUE
    }

    receivingProcessVariables[4] = 0;
    this.processVariables.put(receivingProcess, receivingProcessVariables);
  }

  /**
   * Changes the state of a process
   * 
   * @param process
   *          the process whom to change the state
   * @param state
   *          the new state of the given process
   */
  public void changeState(Integer process, Integer state) {
    Integer[] processVariables = this.processVariables.get(process);
    processVariables[0] = state;
    this.processVariables.put(process, processVariables);
  }

  /**
   * Changes if a process has voted
   * 
   * @param process
   *          the process whom to change if it has voted
   * @param voted
   *          true if it has voted, else false
   */
  public void changeVoted(Integer process, Integer voted) {
    Integer[] processVariables = this.processVariables.get(process);
    processVariables[1] = voted;
    this.processVariables.put(process, processVariables);
  }

  public HashMap<Integer, Integer[]> getProcessVariables() {
    return processVariables;
  }

  public void setProcessVariables(HashMap<Integer, Integer[]> processVariables) {
    this.processVariables = processVariables;
  }

  public HashMap<Integer, LinkedList<Integer>> getProcessQueues() {
    return processQueues;
  }

  public void setProcessQueues(
      HashMap<Integer, LinkedList<Integer>> processQueues) {
    this.processQueues = processQueues;
  }

  public MaekawaAnimation getAnimation() {
    return animation;
  }

  public void setAnimation(MaekawaAnimation animation) {
    this.animation = animation;
  }

  public Integer getFinished() {
    return finished;
  }

  public void setFinished(Integer finished) {
    this.finished = finished;
  }

  public Integer getnProcesses() {
    return nProcesses;
  }

  public void setnProcesses(Integer nProcesses) {
    this.nProcesses = nProcesses;
  }

  public HashMap<Integer, Stack<Integer>> getVotingSets() {
    return votingSets;
  }

  public void setVotingSets(HashMap<Integer, Stack<Integer>> votingSets) {
    this.votingSets = votingSets;
  }

  public String getName() {
    return "MaekawaAlgorithm";
  }

  public String getAlgorithmName() {
    return "Maekawa";
  }

  public String getAnimationAuthor() {
    return "Marco Torsello, Arvid Lange";
  }

  public String getDescription() {
    return "Der Maekawa-Algorithmus dient zur Zugriffssteuerung auf einen kritischen Bereich unter mehreren Prozessen."
        + "\n"
        + "Es soll garantiert werden, dass es ein wechselseitiger Ausschluss statt findet, ohne dabei alle Prozesse zu"
        + "\n"
        + "fragen, sondern nur eine Teilmenge aller Prozesse."
        + "\n"
        + "Hierzu werden die Prozesse in Votingssets eingeteilt, wobei ein Prozess in mindestens zwei Votingssets"
        + "\n"
        + "vorhanden sein muss und zwei Votingssets mindestens einen gemeinsamen Prozess haben m&uuml;ssen."
        + "\n"
        + "Diese Votingssetzs werden eingesetzt um die Kommunikation zu vereinfachen, indem Anfragen eines Prozesses"
        + "\n"
        + "an andere Prozesse nur innerhalb eines Votingsets verschickt werden m&uuml;ssen.";
  }

  public String getCodeExample() {
    return "Jeder Prozess hat einen Zustand (STATE), der folgende Werte annehmen kann:"
        + "\n"
        + "RELEASED - der Prozess ist in keinem kritischen Abschnitt, gleichzeitig der Startwert"
        + "\n"
        + "WANTED - der Prozess m&ouml;chte den kritischen Abschnitt betreten"
        + "\n"
        + "HELD - der Prozess befindet sich im kritischen Abschnitt"
        + "\n"
        + "VOTED - ein anderer Prozess darf den kritischen Abschnitt betreten"
        + "\n"
        + "\n"
        + "Der Prozess m&ouml;chte den kritischen Abschnitt betreten:"
        + "\n"
        + "\n"
        + "Setze den aktuellen Zustand auf WANTED"
        + "\n"
        + "Sende eine Anfrage an alle Prozesse im Votingset"
        + "\n"
        + "Warte auf K-1 Antworten"
        + "\n"
        + "Setze den aktuellen Zustand auf HELD"
        + "\n"
        + "\n"
        + "Der Prozess m&ouml;chte den kritischen Abschnitt verlassen:"
        + "\n"
        + "\n"
        + "Setze den aktuellen Zustand auf RELEASED"
        + "\n"
        + "Sende eine Freigabe an alle Prozesse im Votingset"
        + "\n"
        + "\n"
        + "Der Prozess erh&auml;lt eine Anfrage von einem anderen Prozess:"
        + "\n"
        + "\n"
        + "Wenn der aktuelle Zustand auf HELD oder VOTED steht"
        + "\n"
        + "     dann sortiere die Anfrage in die Warteschlange ein"
        + "\n"
        + "ansonsten"
        + "\n"
        + "     sende eine Antwort"
        + "\n"
        + "     und setze VOTED auf \"true"
        + "\n"
        + "\n"
        + "Der Prozess erh&auml;lt eine Freigabe von einem anderen Prozess:"
        + "\n"
        + "\n"
        + "Falls die Warteschlange leer ist"
        + "\n"
        + "     setze VOTED auf \"false\""
        + "\n"
        + "ansonsten"
        + "\n"
        + "     sende eine Antwort an den ersten Prozess in der Warteschlange und entferne ihn daraus"
        + "\n" + "     und setze VOTED auf \"true\"";
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
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) throws IllegalArgumentException {

    int[] givenProcesses = (int[]) arg1.get("processes");
    Integer nProcesses = (Integer) arg1.get("nProcesses");
    int[][] uVotingSets = (int[][]) arg1.get("votingSets");
    int[] processDelay = (int[]) arg1.get("processDelay");

    // check if enough processes are given to use the algorithm
    if (nProcesses < 3)
      return false;

    // check if processes that want access are existent
    for (int i = 0; i < givenProcesses.length; i++) {
      if (givenProcesses[i] > nProcesses - 1 || givenProcesses[i] < 0)
        return false;
    }

    // check if for every process was given a delay
    if (processDelay.length != nProcesses)
      return false;

    int[][] found = new int[nProcesses][uVotingSets.length];
    for (int i = 0; i < nProcesses; i++) {
      found[i][0] = 0;
      for (int j = 0; j < uVotingSets.length; j++) {
        for (int n = 0; n < uVotingSets[j].length; n++) {
          if (i == uVotingSets[j][n]) {
            found[i][j] = 1;
          }
        }
      }
    }

    // test if every process is in minimum 2 votingsets
    for (int i = 0; i < found.length; i++) {
      int temp = 0;
      for (int j = 0; j < found[i].length; j++) {
        if (found[i][j] == 1)
          temp++;
      }
      if (temp < 2)
        return false;
    }

    return true;
  }

}