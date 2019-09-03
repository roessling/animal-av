package algoanim.counter.view;

import java.awt.Color;

import algoanim.counter.enumeration.ControllerEnum;
import algoanim.counter.model.FourValueCounter;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.RectProperties;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;

/**
 * Class to visualize the values (Accesses, Assignments, Queueings and
 * Unqueueings) of a <code>FourValueCounter</code>.
 * 
 * @author Axel Heimann
 */

public class FourValueView extends TwoValueView {

  private Text                 enqueueText;
  private Text                 dequeueText;
  private Text                 enqueueNumber;
  private Text                 dequeueNumber;
  private final Rect           enqueueBar;
  private final Rect           dequeueBar;
  private final RectProperties rpQueue, rpUnqueue;
  private String[]             valueNames = { "0", "0", "0", "0" };
  private int                  enqueueings, dequeueings;

  // private int longestString;

  /**
   * Instantiates an <code>FourValueView</code>
   * 
   * @param lang
   *          the <code>Language</code> used to create visualizing Primitives
   * @param coord
   *          Node Object representing the upper left as Coordinates or Offset
   * @param number
   *          true -> Values visualized as number false -> Values not visualized
   *          as number
   * @param bar
   *          true -> Values visualized as bar false -> Values not visualized as
   *          bar
   * @param counterProperties
   *          the <code>CounterProperties</code> which define colors etc. of the
   *          visualization
   * @param valueNames
   *          String Array to customize the names of the counted values (e.g.
   *          "Zuweisungen" instead of "assignments"). The Array must have 2 or
   *          4 entries dependent on the number of visualized values
   */

  public FourValueView(Language lang, Node coord, boolean number, boolean bar,
      CounterProperties counterProperties, String[] valueNames) {
    super();
    this.lang = lang;
    assignments = accesses = enqueueings = dequeueings = 0;
    rectProps = initRectProperties(counterProperties);
    textProps = initTextProperties(counterProperties);
    highlightColor = (Color) counterProperties.get("highlightColor");
    standardColor = (Color) counterProperties.get("color");
    if (highlightColor.equals(standardColor)
        && !(highlightColor.equals(Color.RED))) {
      highlightColor = Color.RED;
    }
    this.valueNames = initValueNames(valueNames);
    rpQueue = rpUnqueue = initRectProperties(counterProperties);
    int[] StringLength = { 0, 0, 0, 0 };
    int longestString;
    longestString = 0;

    StringLength[0] = valueNames[0].length();
    StringLength[1] = valueNames[1].length();
    StringLength[2] = valueNames[2].length();
    StringLength[3] = valueNames[3].length();
    longestString = getLongestStringPos(StringLength);

    switch (longestString) {
      case 1:
        initAssignments(coord);
        break;
      case 2:
        initAccesses(coord);
        break;
      case 3:
        initEnqueue(coord);
        break;
      case 4:
        initDequeue(coord);
        break;
      default:
        throw new IllegalArgumentException();
    }

    assignmentsBar = lang.newRect(new Offset(20, 0, assignmentsNumber, "NE"),
        new Offset(20, 0, assignmentsNumber, "SE"), "AssignmentsBar", disOp,
        rectProps);
    accessesBar = lang.newRect(new Offset(20, 0, accessesNumber, "NE"),
        new Offset(20, 0, accessesNumber, "SE"), "AccessesBar", disOp,
        rectProps);
    enqueueBar = lang.newRect(new Offset(20, 0, enqueueNumber, "NE"),
        new Offset(20, 0, enqueueNumber, "SE"), "AssignmentsBar", disOp,
        rectProps);
    dequeueBar = lang
        .newRect(new Offset(20, 0, dequeueNumber, "NE"), new Offset(20, 0,
            dequeueNumber, "SE"), "AccessesBar", disOp, rectProps);

    if (!number) {
      hideNumber();
    }
    if (!bar) {
      hideBar();
    }
    if (!bar && !number) {
      hide();
    }
  }

  /**
   * Moves this <code>FourValueView</code> along another one into a specific
   * direction.
   * 
   * @param direction
   *          the direction to move the <code>FourValueView</code>.
   * @param via
   *          the <code>Arc</code>, along which the <code>FourValueView</code>
   *          is moved.
   * @param delay
   *          the delay, before the operation is performed.
   * @param duration
   *          the duration of the operation.
   * @throws IllegalDirectionException
   */
  @Override
  public void moveVia(String direction, Primitive via, Timing delay,
      Timing duration) throws IllegalDirectionException {
    assignmentsText.moveVia(direction, "translate", via, delay, duration);
    accessesText.moveVia(direction, "translate", via, delay, duration);
    enqueueText.moveVia(direction, "translate", via, delay, duration);
    dequeueText.moveVia(direction, "translate", via, delay, duration);
    assignmentsNumber.moveVia(direction, "translate", via, delay, duration);
    accessesNumber.moveVia(direction, "translate", via, delay, duration);
    enqueueNumber.moveVia(direction, "translate", via, delay, duration);
    dequeueNumber.moveVia(direction, "translate", via, delay, duration);
    assignmentsBar.moveVia(direction, "translate", via, delay, duration);
    accessesBar.moveVia(direction, "translate", via, delay, duration);
    enqueueBar.moveVia(direction, "translate", via, delay, duration);
    dequeueBar.moveVia(direction, "translate", via, delay, duration);
  }

  /**
   * Moves this <code>FourValueView</code> to a <code>Node</code>.
   * 
   * @param dx
   *          the x offset to move
   * @param dy
   *          the y offset to move
   * @param delay
   *          the delay, before the operation is performed.
   * @param duration
   *          the duration of the operation.
   */
  @Override
  public void moveBy(int dx, int dy, Timing delay, Timing duration) {
    assignmentsText.moveBy("translate", dx, dy, delay, duration);
    accessesText.moveBy("translate", dx, dy, delay, duration);
    enqueueText.moveBy("translate", dx, dy, delay, duration);
    dequeueText.moveBy("translate", dx, dy, delay, duration);
    assignmentsNumber.moveBy("translate", dx, dy, delay, duration);
    accessesNumber.moveBy("translate", dx, dy, delay, duration);
    enqueueNumber.moveBy("translate", dx, dy, delay, duration);
    dequeueNumber.moveBy("translate", dx, dy, delay, duration);
    assignmentsBar.moveBy("translate", dx, dy, delay, duration);
    accessesBar.moveBy("translate", dx, dy, delay, duration);
    enqueueBar.moveBy("translate", dx, dy, delay, duration);
    dequeueBar.moveBy("translate", dx, dy, delay, duration);
  }

  /**
   * Moves this <code>FourValueView</code> to a <code>Node</code>.
   * 
   * @param direction
   *          the direction to move the <code>FourValueView</code>.
   * @param target
   *          the point where the <code>FourValueView</code> is moved to.
   * @param delay
   *          the delay, before the operation is performed.
   * @param duration
   *          the duration of the operation.
   * @throws IllegalDirectionException
   */
  /**
   * @Override public void moveTo(String direction, Node target, Timing delay,
   *           Timing duration) throws IllegalDirectionException { switch
   *           (longestString) { case 1: assignmentsText.moveTo(direction,
   *           "translate", target, delay, duration);
   *           accessesText.moveTo(direction, "translate", new Offset(0, 5,
   *           assignmentsText, "SW"), delay, duration);
   *           assignmentsNumber.moveTo(direction, "translate", new Offset(5, 0,
   *           assignmentsText, "NE"), delay, duration);
   *           accessesNumber.moveTo(direction, "translate", new Offset(0, 5,
   *           assignmentsNumber, "SW"), delay, duration); break; case 2:
   *           accessesText.moveTo(direction, "translate", target, delay,
   *           duration); assignmentsText.moveTo(direction, "translate", new
   *           Offset(0, 5, accessesText, "SW"), delay, duration);
   *           assignmentsNumber.moveTo(direction, "translate", new Offset(5, 0,
   *           assignmentsText, "NE"), delay, duration);
   *           accessesNumber.moveTo(direction, "translate", new Offset(0, 5,
   *           accessesText, "SW"), delay, duration);
   *           assignmentsBar.moveTo(direction, "translate", new Offset(20, 0,
   *           assignmentsNumber, "SE"), delay, duration);
   *           accessesBar.moveTo(direction, "translate", new Offset(20, 0,
   *           accessesNumber, "SE"), delay, duration); break; case 3:
   * 
   *           break; case 4:
   * 
   *           break; default: throw new IllegalArgumentException(); } }
   **/

  /**
   * Updates the view.
   * 
   * @param valueType
   *          the value which should be updated
   * 
   * @param value
   *          the increase of the value
   */

  @Override
  public void update(ControllerEnum valueType, int value) {
    switch (valueType) {
      case assignments:
        assignments = assignments + value;
        assignmentsNumber.setText(String.valueOf(assignments), null, null);
        assignmentsBar.moveBy("translate #2", INCREMENT_LENGTH * value, 0,
            null, null);
        break;
      case access:
        accesses = accesses + value;
        accessesNumber.setText(String.valueOf(accesses), null, null);
        accessesBar.moveBy("translate #2", INCREMENT_LENGTH * value, 0, null,
            null);
        break;
      case queueings:
        enqueueings = enqueueings + value;
        enqueueNumber.setText(String.valueOf(enqueueings), null, null);
        enqueueBar.moveBy("translate #2", INCREMENT_LENGTH * value, 0, null,
            null);
        break;
      case unqueueings:
        dequeueings = dequeueings + value;
        dequeueNumber.setText(String.valueOf(dequeueings), null, null);
        dequeueBar.moveBy("translate #2", INCREMENT_LENGTH * value, 0, null,
            null);
        break;
      default:
        try {
          throw new IllegalArgumentException("ValuetypeNotVisualized");
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
        }
    }
  }

  public void addCounter(FourValueCounter fvc) {

  }

  /**
   * Hides the visualization as number after the given time.
   * 
   * @param t
   *          the offset until the operation shall be performed.
   */
  @Override
  public void hideNumber(Timing t) {
    super.hideNumber(t);
    enqueueNumber.hide(t);
    dequeueNumber.hide(t);
  }

  /**
   * Hides the visualization as number.
   */

  @Override
  public void hideNumber() {
    super.hideNumber();
    enqueueNumber.hide();
    dequeueNumber.hide();
  }

  /**
   * Hides the visualization as bar after the given time.
   * 
   * @param t
   *          the offset until the operation shall be performed.
   */
  @Override
  public void hideBar(Timing t) {
    super.hideBar(t);
    enqueueBar.hide(t);
    dequeueBar.hide(t);
  }

  /**
   * Hides the visualization as bar.
   */

  @Override
  public void hideBar() {
    super.hideBar();
    enqueueBar.hide();
    dequeueBar.hide();
  }

  /**
   * Shows the visualization as number after the given time.
   * 
   * @param t
   *          the offset until the operation shall be performed.
   */
  @Override
  public void showNumber(Timing t) {
    super.showNumber(t);
    enqueueNumber.show(t);
    dequeueNumber.show(t);
  }

  /**
   * Shows the visualization as number.
   */

  @Override
  public void showNumber() {
    super.showNumber();
    enqueueNumber.show();
    dequeueNumber.show();
  }

  /**
   * Shows the visualization as bar after the given time.
   * 
   * @param t
   *          the offset until the operation shall be performed.
   */
  @Override
  public void showBar(Timing t) {
    super.showBar(t);
    enqueueBar.show(t);
    dequeueBar.show(t);
  }

  /**
   * Shows the visualization as bar.
   */

  @Override
  public void showBar() {
    super.showBar();
    enqueueBar.show();
    dequeueBar.show();
  }

  /**
   * Hides the text in front of the number/bar after the given time.
   * 
   * @param t
   *          the offset until the operation shall be performed.
   */
  @Override
  public void hideText(Timing t) {
    super.hideText(t);
    enqueueText.hide(t);
    dequeueText.hide(t);
  }

  /**
   * Hides the text in front of the number/bar.
   */

  @Override
  public void hideText() {
    super.hideText();
    enqueueText.hide();
    dequeueText.hide();
  }

  /**
   * Shows the text in front of the number/bar after the given time.
   * 
   * @param t
   *          the offset until the operation shall be performed.
   */
  @Override
  public void showText(Timing t) {
    super.showText(t);
    enqueueText.show(t);
    dequeueText.show(t);
  }

  /**
   * Shows the text in front of the number/bar.
   */
  @Override
  public void showText() {
    super.showText();
    enqueueText.show();
    dequeueText.show();
  }

  /**
   * Hides the complete Counter visualization (text, number and bar) after the
   * given time.
   * 
   * @param t
   *          the offset until the operation shall be performed.
   */
  @Override
  public void hide(Timing t) {
    hideText(t);
    hideNumber(t);
    hideBar(t);
  }

  /**
   * Hides the complete Counter visualization (text, number and bar).
   */
  @Override
  public void hide() {
    hideText();
    hideNumber();
    hideBar();
  }

  /**
   * Shows the complete Counter visualization (text, number and bar) after the
   * given time.
   * 
   * @param t
   *          the offset until the operation shall be performed.
   */
  @Override
  public void show(Timing t) {
    showText(t);
    showNumber(t);
    showBar(t);
  }

  /**
   * Shows the complete Counter visualization (text, number and bar).
   */
  @Override
  public void show() {
    showText();
    showNumber();
    showBar();
  }

  /**
   * Highlights the complete visualization in the highlightColor. Default color
   * is black.
   */

  @Override
  public void highlight() {
    highlightAssignments();
    highlightAccess();
    highlightDequeueings();
    highlightEnqueueings();
  }

  /**
   * Unhighlights the complete visualization in the highlightColor. Default
   * color is black.
   */

  @Override
  public void unhighlight() {
    unhighlightAssignments();
    unhighlightAccess();
    unhighlightDequeueings();
    unhighlightEnqueueings();
  }

  /**
   * Highlights the queueings in the highlightColor. Default color is black.
   */

  public void highlightEnqueueings() {
    enqueueNumber.changeColor("color", highlightColor, null, null);
    enqueueText.changeColor("color", highlightColor, null, null);
    rpQueue.set(AnimationPropertiesKeys.COLOR_PROPERTY, highlightColor);
  }

  /**
   * Highlights the dequeueings in the highlightColor. Default color is black.
   */

  public void highlightDequeueings() {
    dequeueNumber.changeColor("color", highlightColor, null, null);
    dequeueText.changeColor("color", highlightColor, null, null);
    rpUnqueue.set(AnimationPropertiesKeys.COLOR_PROPERTY, highlightColor);
  }

  /**
   * Unhighlights the enqueueings in the highlightColor. Default color is black.
   */

  public void unhighlightEnqueueings() {
    enqueueNumber.changeColor("color", standardColor, null, null);
    enqueueText.changeColor("color", standardColor, null, null);
    rpQueue.set(AnimationPropertiesKeys.COLOR_PROPERTY, standardColor);
  }

  /**
   * Unhighlights the dequeueings in the highlightColor. Default color is black.
   */

  public void unhighlightDequeueings() {
    dequeueNumber.changeColor("color", standardColor, null, null);
    dequeueText.changeColor("color", standardColor, null, null);
    rpUnqueue.set(AnimationPropertiesKeys.COLOR_PROPERTY, standardColor);
  }

  /**
   * Checks the assigned valueNames. If the String Array is invalid standard
   * valueNames are used
   * 
   * @param valueNames
   *          StringArray which is checked
   */

  private String[] initValueNames(String[] valueNames) {
    String[] names = new String[4];
    if (valueNames != null && valueNames.length == 4)
      System.arraycopy(valueNames, 0, names, 0, 4);
    else {
      names[0] = "Assignments";
      names[1] = "Accesses";
      names[2] = "Enqueueings";
      names[3] = "Dequeueings";
    }
    return names;
  }

  /**
   * Extracts the properties which are relevant for the bar view from the
   * CounterProperties.
   */

  private int getLongestStringPos(int[] x) {
    int maxX = 0;
    int pos = 0;
    for (int i = 0; i < 4; i++) {
      if (x[i] > maxX) {
        maxX = x[i];
        pos = i + 1;
      }
    }
    return pos;
  }

  private void initAssignments(Node coord) {
    assignmentsText = lang.newText(coord, valueNames[0], "assignmentsText",
        disOp, textProps);
    accessesText = lang.newText(new Offset(0, 5, assignmentsText, "SW"),
        valueNames[1], "accessesText", disOp, textProps);
    enqueueText = lang.newText(new Offset(0, 5, accessesText, "SW"),
        valueNames[2], "enqueueText", disOp, textProps);
    dequeueText = lang.newText(new Offset(0, 5, enqueueText, "SW"),
        valueNames[3], "dequeueText", disOp, textProps);

    assignmentsNumber = lang.newText(new Offset(5, 0, assignmentsText, "NE"),
        "0", "AssignmentsNumber", disOp);
    accessesNumber = lang.newText(new Offset(0, 5, assignmentsNumber, "SW"),
        "0", "AccessesNumber", disOp);
    enqueueNumber = lang.newText(new Offset(0, 5, accessesNumber, "SW"), "0",
        "AssignmentsNumber", disOp);
    dequeueNumber = lang.newText(new Offset(0, 5, enqueueNumber, "SW"), "0",
        "AccessesNumber", disOp);
  }

  private void initAccesses(Node coord) {
    accessesText = lang
        .newText(coord, valueNames[1], "Text1", disOp, textProps);
    assignmentsText = lang.newText(new Offset(0, 5, accessesText, "SW"),
        valueNames[0], "Text2", disOp, textProps);
    enqueueText = lang.newText(new Offset(0, 5, assignmentsText, "SW"),
        valueNames[2], "enqueueText", disOp, textProps);
    dequeueText = lang.newText(new Offset(0, 5, enqueueText, "SW"),
        valueNames[3], "dequeueText", disOp, textProps);

    accessesNumber = lang.newText(new Offset(5, 0, accessesText, "NE"), "0",
        "AssignmentsNumber", disOp);
    assignmentsNumber = lang.newText(new Offset(0, 5, accessesNumber, "SW"),
        "0", "AccessesNumber", disOp);
    enqueueNumber = lang.newText(new Offset(0, 5, assignmentsNumber, "SW"),
        "0", "AssignmentsNumber", disOp);
    dequeueNumber = lang.newText(new Offset(0, 5, enqueueNumber, "SW"), "0",
        "AccessesNumber", disOp);
  }

  private void initEnqueue(Node coord) {
    enqueueText = lang.newText(coord, valueNames[2], "enqueueText", disOp,
        textProps);
    dequeueText = lang.newText(new Offset(0, 5, enqueueText, "SW"),
        valueNames[3], "dequeueText", disOp, textProps);
    accessesText = lang.newText(new Offset(0, 5, dequeueText, "SW"),
        valueNames[1], "Text1", disOp, textProps);
    assignmentsText = lang.newText(new Offset(0, 5, accessesText, "SW"),
        valueNames[0], "Text2", disOp, textProps);

    enqueueNumber = lang.newText(new Offset(5, 0, enqueueText, "NE"), "0",
        "enqueueNumber", disOp);
    dequeueNumber = lang.newText(new Offset(0, 5, enqueueNumber, "SW"), "0",
        "AccessesNumber", disOp);
    accessesNumber = lang.newText(new Offset(0, 5, dequeueNumber, "SW"), "0",
        "AssignmentsNumber", disOp);
    assignmentsNumber = lang.newText(new Offset(0, 5, accessesNumber, "SW"),
        "0", "AccessesNumber", disOp);
  }

  private void initDequeue(Node coord) {
    dequeueText = lang.newText(coord, valueNames[3], "dequeueText", disOp,
        textProps);
    enqueueText = lang.newText(new Offset(0, 5, dequeueText, "SW"),
        valueNames[2], "enqueueText", disOp, textProps);
    accessesText = lang.newText(new Offset(0, 5, enqueueText, "SW"),
        valueNames[1], "Text1", disOp, textProps);
    assignmentsText = lang.newText(new Offset(0, 5, accessesText, "SW"),
        valueNames[0], "Text2", disOp, textProps);

    dequeueNumber = lang.newText(new Offset(5, 0, dequeueText, "NE"), "0",
        "AccessesNumber", disOp);
    enqueueNumber = lang.newText(new Offset(0, 5, dequeueNumber, "SW"), "0",
        "enqueueNumber", disOp);
    accessesNumber = lang.newText(new Offset(0, 5, enqueueNumber, "SW"), "0",
        "AssignmentsNumber", disOp);
    assignmentsNumber = lang.newText(new Offset(0, 5, accessesNumber, "SW"),
        "0", "AccessesNumber", disOp);
  }
}
