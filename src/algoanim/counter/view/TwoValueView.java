package algoanim.counter.view;

import java.awt.Color;

import algoanim.counter.enumeration.ControllerEnum;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;

/**
 * Class to visualize the values (Accesses and Assignments) of a
 * <code>AccessAndAssignmentCounter</code>.
 * 
 * @author Axel Heimann
 */

public class TwoValueView {

  protected static final int INCREMENT_LENGTH = 5;
  protected int              assignments, accesses;
  protected DisplayOptions   disOp;
  protected Text             assignmentsText, accessesText, assignmentsNumber,
      accessesNumber;
  protected int              upperValue;
  protected Rect             assignmentsBar, accessesBar;
  protected TextProperties   textProps;
  protected RectProperties   rectProps;
  protected Color            highlightColor;
  protected Color            standardColor;
  protected Language         lang;

  protected TwoValueView() {

  }

  /**
   * Instantiates an <code>TwoValueView</code>
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

  public TwoValueView(Language lang, Node coord, boolean number, boolean bar,
      CounterProperties counterProperties, String[] valueNames) {
    assignments = accesses = 0;
    this.lang = lang;
    rectProps = initRectProperties(counterProperties);
    textProps = initTextProperties(counterProperties);
    highlightColor = (Color) counterProperties.get("highlightColor");
    standardColor = (Color) counterProperties.get("color");
    if (highlightColor.equals(standardColor)
        && !(highlightColor.equals(Color.RED))) {
      highlightColor = Color.RED;
    }
    String[] names = initValueNames(valueNames);

    if (names[0].length() >= names[1].length()) {
      initAssignments(names[0], names[1], coord);
      upperValue = 0;
    } else {
      initAccesses(names[1], names[0], coord);
      upperValue = 1;
    }
    assignmentsBar = lang.newRect(new Offset(20, 0, assignmentsNumber, "NE"),
        new Offset(20, 0, assignmentsNumber, "SE"), "AssignmentsBar", disOp,
        rectProps);
    accessesBar = lang.newRect(new Offset(20, 0, accessesNumber, "NE"),
        new Offset(20, 0, accessesNumber, "SE"), "AccessesBar", disOp,
        rectProps);

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
   * Moves this <code>TwoValueView</code> along another one into a specific
   * direction.
   * 
   * @param direction
   *          the direction to move the <code>TwoValueView</code>.
   * @param via
   *          the <code>Arc</code>, along which the <code>TwoValueView</code> is
   *          moved.
   * @param delay
   *          the delay, before the operation is performed.
   * @param duration
   *          the duration of the operation.
   * @throws IllegalDirectionException
   */
  public void moveVia(String direction, Primitive via, Timing delay,
      Timing duration) throws IllegalDirectionException {
    assignmentsText.moveVia(direction, "translate", via, delay, duration);
    accessesText.moveVia(direction, "translate", via, delay, duration);
    assignmentsNumber.moveVia(direction, "translate", via, delay, duration);
    accessesNumber.moveVia(direction, "translate", via, delay, duration);
    assignmentsBar.moveVia(direction, "translate", via, delay, duration);
    accessesBar.moveVia(direction, "translate", via, delay, duration);
  }

  /**
   * Moves this <code>TwoValueView</code> to a <code>Node</code>.
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
  public void moveBy(int dx, int dy, Timing delay, Timing duration) {
    assignmentsText.moveBy("translate", dx, dy, delay, duration);
    accessesText.moveBy("translate", dx, dy, delay, duration);
    assignmentsNumber.moveBy("translate", dx, dy, delay, duration);
    accessesNumber.moveBy("translate", dx, dy, delay, duration);
    assignmentsBar.moveBy("translate", dx, dy, delay, duration);
    accessesBar.moveBy("translate", dx, dy, delay, duration);
  }

  /**
   * Moves this <code>TwoValueView</code> to a <code>Node</code>.
   * 
   * @param direction
   *          the direction to move the <code>TwoValueView</code>.
   * @param moveType
   *          the type of the movement.
   * @param target
   *          the point where the <code>TwoValueView</code> is moved to.
   * @param delay
   *          the delay, before the operation is performed.
   * @param duration
   *          the duration of the operation.
   * @throws IllegalDirectionException
   */
  /**
   * public void moveTo(String direction, Node target, Timing delay, Timing
   * duration) throws IllegalDirectionException { if (upperValue == 0) {
   * assignmentsText.moveTo(direction, "translate", target, delay, duration);
   * accessesText.moveTo(direction, "translate", new Offset(0, 5,
   * assignmentsText, "SW"), delay, duration);
   * assignmentsNumber.moveTo(direction, "translate", new Offset(5, 0,
   * assignmentsText, "NE"), delay, duration); accessesNumber.moveTo(direction,
   * "translate", new Offset(0, 5, assignmentsNumber, "SW"), delay, duration); }
   * else { accessesText.moveTo(direction, "translate", target, delay,
   * duration); assignmentsText.moveTo(direction, "translate", new Offset(0, 5,
   * accessesText, "SW"), delay, duration); accessesNumber.moveTo(direction,
   * "translate", new Offset(5, 0, accessesText, "NE"), delay, duration);
   * assignmentsNumber.moveTo(direction, "translate", new Offset(0, 5,
   * accessesNumber, "SW"), delay, duration);
   * 
   * } assignmentsBar.moveTo(direction, "translate", new Offset(20, 0,
   * assignmentsNumber, "NE"), delay, duration); accessesBar.moveTo(direction,
   * "translate", new Offset(20, 0, accessesNumber, "NE"), delay, duration); }
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
      default:
        try {
          throw new IllegalArgumentException("ValuetypeNotVisualized");
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
        }
    }
  }

  /**
   * Hides the visualization as number after the given time.
   * 
   * @param t
   *          the offset until the operation shall be performed.
   */
  public void hideNumber(Timing t) {
    assignmentsNumber.hide(t);
    accessesNumber.hide(t);
  }

  /**
   * Hides the visualization as number.
   */

  public void hideNumber() {
    assignmentsNumber.hide();
    accessesNumber.hide();
  }

  /**
   * Hides the visualization as bar after the given time.
   * 
   * @param t
   *          the offset until the operation shall be performed.
   */
  public void hideBar(Timing t) {
    assignmentsBar.hide(t);
    accessesBar.hide(t);
  }

  /**
   * Hides the visualization as bar.
   */

  public void hideBar() {
    assignmentsBar.hide();
    accessesBar.hide();
  }

  /**
   * Shows the visualization as number after the given time.
   * 
   * @param t
   *          the offset until the operation shall be performed.
   */
  public void showNumber(Timing t) {
    assignmentsNumber.show(t);
    accessesNumber.show(t);
  }

  /**
   * Shows the visualization as number.
   */

  public void showNumber() {
    assignmentsNumber.show();
    accessesNumber.show();
  }

  /**
   * Shows the visualization as bar after the given time.
   * 
   * @param t
   *          the offset until the operation shall be performed.
   */
  public void showBar(Timing t) {
    assignmentsBar.show(t);
    accessesBar.show(t);
  }

  /**
   * Shows the visualization as bar.
   */

  public void showBar() {
    assignmentsBar.show();
    accessesBar.show();
  }

  /**
   * Hides the text in front of the number/bar after the given time.
   * 
   * @param t
   *          the offset until the operation shall be performed.
   */
  public void hideText(Timing t) {
    assignmentsText.hide(t);
    accessesText.hide(t);
  }

  /**
   * Hides the text in front of the number/bar.
   */

  public void hideText() {
    assignmentsText.hide();
    accessesText.hide();
  }

  /**
   * Shows the text in front of the number/bar after the given time.
   * 
   * @param t
   *          the offset until the operation shall be performed.
   */
  public void showText(Timing t) {
    assignmentsText.show(t);
    accessesText.show(t);
  }

  /**
   * Shows the text in front of the number/bar.
   */
  public void showText() {
    assignmentsText.show();
    accessesText.show();
  }

  /**
   * Hides the complete Counter visualization (text, number and bar) after the
   * given time.
   * 
   * @param t
   *          the offset until the operation shall be performed.
   */
  public void hide(Timing t) {
    hideText(t);
    hideNumber(t);
    hideBar(t);
  }

  /**
   * Hides the complete Counter visualization (text, number and bar).
   */
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
  public void show(Timing t) {
    showText(t);
    showNumber(t);
    showBar(t);
  }

  /**
   * Shows the complete Counter visualization (text, number and bar).
   */
  public void show() {
    showText();
    showNumber();
    showBar();
  }

  /**
   * Highlights the complete visualization in the highlightColor. Default color
   * is black.
   */

  public void highlight() {
    highlightAssignments();
    highlightAccess();
  }

  /**
   * Unhighlights the complete visualization in the highlightColor. Default
   * color is black.
   */

  public void unhighlight() {
    unhighlightAssignments();
    unhighlightAccess();
  }

  /**
   * Highlights the assignments in the highlightColor. Default color is black.
   */

  public void highlightAssignments() {
    assignmentsNumber.changeColor("color", highlightColor, null, null);
    assignmentsText.changeColor("color", highlightColor, null, null);
    assignmentsBar.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
        highlightColor, null, null);
  }

  /**
   * Highlights the accesses in the highlightColor. Default color is black.
   */

  public void highlightAccess() {
    accessesNumber.changeColor("color", highlightColor, null, null);
    accessesText.changeColor("color", highlightColor, null, null);
    accessesBar.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
        highlightColor, null, null);
  }

  /**
   * Unhighlights the assignments in the standardColor. Default color is black.
   */

  public void unhighlightAssignments() {
    assignmentsNumber.changeColor("color", standardColor, null, null);
    assignmentsText.changeColor("color", standardColor, null, null);
    assignmentsBar.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
        standardColor, null, null);
  }

  /**
   * Highlights the accesses in the standardColor. Default color is black.
   */

  public void unhighlightAccess() {
    accessesNumber.changeColor("color", standardColor, null, null);
    accessesText.changeColor("color", standardColor, null, null);
    accessesBar.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
        standardColor, null, null);
  }

  /**
   * Checks the assigned valueNames. If the String Array is invalid standard
   * valueNames are used
   * 
   * @param valueNames
   *          StringArray which is checked
   */

  private String[] initValueNames(String[] valueNames) {
    String[] names = null;
    if (valueNames == null || valueNames.length != 2)
      names = new String[] { "Assignments", "Accesses" };
    else
      names = valueNames;
    return names;
  }

  /**
   * Extracts the properties which are relevant for the bar view from the
   * CounterProperties.
   */

  protected RectProperties initRectProperties(CounterProperties props) {
    RectProperties rp = new RectProperties();
    rp.set(AnimationPropertiesKeys.FILL_PROPERTY, props.get("fillColor"));
    rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, props.get("filled"));
    rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, props.get("color"));
    rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, props.get("depth"));
    rp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, props.get("hidden"));
    return rp;
  }

  /**
   * Extracts the properties which are relevant for the text view from the
   * CounterProperties.
   */

  protected TextProperties initTextProperties(CounterProperties props) {
    TextProperties tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, props.get("color"));
    tp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, props.get("depth"));
    tp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, props.get("hidden"));
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, props.get("font"));
    return tp;
  }

  private void initAssignments(String name1, String name2, Node coord) {
    assignmentsText = lang.newText(coord, name1, "AssignmentsText", disOp,
        textProps);
    accessesText = lang.newText(new Offset(0, 5, assignmentsText, "SW"), name2,
        "AccessesText", disOp, textProps);

    assignmentsNumber = lang.newText(new Offset(5, 0, assignmentsText, "NE"),
        "0", "AssignmentsNumber", disOp);
    accessesNumber = lang.newText(new Offset(0, 5, assignmentsNumber, "SW"),
        "0", "AccessesNumber", disOp);
  }

  private void initAccesses(String name1, String name2, Node coord) {
    accessesText = lang.newText(coord, name1, "AccessesText", disOp, textProps);
    assignmentsText = lang.newText(new Offset(0, 5, accessesText, "SW"), name2,
        "AssignmentsText", disOp, textProps);

    accessesNumber = lang.newText(new Offset(5, 0, accessesText, "NE"), "0",
        "AssignmentsNumber", disOp);
    assignmentsNumber = lang.newText(new Offset(0, 5, accessesNumber, "SW"),
        "0", "AccessesNumber", disOp);
  }
}
