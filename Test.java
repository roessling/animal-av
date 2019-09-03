import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.LinkedList;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ConceptualStack;
import algoanim.primitives.ListBasedQueue;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.QueueProperties;
import algoanim.properties.StackProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.main.Animal;

public class Test {

  /*
   * public static <T> ConceptualStack<T> newConceptualStack(T dummy,
   * Coordinates coords, String name, Timing timing, StackProperties sp) { //
   * ConceptualStack ConceptualStackGenerator<T> csg = new
   * AnimalConceptualStackGenerator<T>(l); ConceptualStack<T> result = new
   * ConceptualStack<T>(csg, coords, name, timing, sp); return result; } public
   * static <T> ConceptualStack<T>
   * newConceptualStack(ConceptualStackGenerator<T> csg, Coordinates coords,
   * String name, Timing timing, StackProperties sp) { ConceptualStack<T> result
   * = new ConceptualStack<T>(csg, coords, name, timing, sp); return result; }
   */

  public static int getStringWidth(String text, Font f) {
    int width = 0;
    FontMetrics fm = getConcreteFontMetrics(f);

    if ((fm != null) && (text != null)) {
      width = fm.stringWidth(text);
    }

    return width;
  }

  public static FontMetrics getConcreteFontMetrics(Font f) {
    Graphics g = Animal.get().getGraphics();
    return g.getFontMetrics(f);
    // return f.getLineMetrics("Hello world", g);
    // Graphics graphics = null; //
    // Animal.get().getGraphics();
    // problem: need an Animal instance running before!
    // if (graphics != null)
    // return graphics.getFontMetrics(f);
    // return Toolkit.getDefaultToolkit().getFontMetrics(f);
  }

  /**
   * @param args
   */
  public static void main(String[] args) {

    int i = Test.getStringWidth("5", new Font("SansSerif", Font.PLAIN, 12));
    System.err.println(i);
    // TODO Auto-generated method stub
    Language l = new AnimalScript("ConceptualStack Animation", "Dima Vronskyi"
        + " vronskyi@googlemail.com", 640, 480);
    l.setStepMode(true);
    StackProperties sp = new StackProperties();
    LinkedList<String> content = null;

    sp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    sp.set(AnimationPropertiesKeys.DIVIDINGLINE_COLOR_PROPERTY, Color.ORANGE);
    sp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
    sp.set(AnimationPropertiesKeys.ALTERNATE_FILLED_PROPERTY, true);
    sp.set(AnimationPropertiesKeys.ALTERNATE_FILL_PROPERTY, Color.GREEN);
    sp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    sp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
    // sp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    // sp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
    // sp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
    // ConceptualStack<Integer> cs = new ConceptualStack<Integer>(csg, new
    // Coordinates(10,20),"CStack",
    // null, sp);
    content = new LinkedList<String>();
    content.add("10");
    content.add("20");
    content.add("30");

    Timing defaultTiming = new TicksTiming(500);

    // ConceptualStack
    ConceptualStack<String> cs = l.newConceptualStack(new Coordinates(10, 20),
        content, "CStack", null, sp);
    // ConceptualStack<Integer> cs2 = newConceptualStack(csg, new
    // Coordinates(10, 20), "CStack",
    // null, sp);
    l.nextStep();
    cs.push("1l", null, null);
    l.nextStep();
    cs.top(null, null);
    l.nextStep();
    cs.push("j2Q", defaultTiming, defaultTiming);
    l.nextStep();
    cs.highlightTopCell(defaultTiming, defaultTiming);
    l.nextStep();
    cs.push("32gh", null, null);
    l.nextStep();
    cs.push("1563756745", null, null);
    l.nextStep();
    cs.push("21", null, null);
    l.nextStep();
    cs.top(defaultTiming, new TicksTiming(100));
    l.nextStep();
    cs.push("32bQqIj", null, null);
    l.nextStep();
    cs.pop(defaultTiming, defaultTiming);
    l.nextStep();
    cs.pop(null, null);
    l.nextStep();
    cs.push("7", null, null);
    l.nextStep();
    cs.top(null, null);
    l.nextStep();
    /*
     * cs.pop(null, null); l.nextStep(); cs.pop(null, null); l.nextStep();
     * cs.pop(null, null); l.nextStep(); cs.pop(null, null); l.nextStep();
     * cs.pop(null, null); l.nextStep(); cs.push("7", null, null); l.nextStep();
     * cs.top(null, null); l.nextStep(); cs.push("17", null, null);
     */
    // l.nextStep();
    // cs.pop(null, null);
    /*
     * VisualStack cs1 = new ConceptualStack<Integer>(csg, new
     * Coordinates(250,20),"CStack", null, sp);
     */

    // ArrayBasedStack
    // sp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    // sp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    /*
     * ArrayDisplayOptions ado = new ArrayDisplayOptions(defaultTiming, null,
     * false); ArrayBasedStack<String> abs = l.newArrayBasedStack(new
     * Coordinates(10,70), content, "AbStack", null, sp, 5); l.nextStep();
     * abs.pop(null, null); l.nextStep(); abs.pop(null, null); l.nextStep();
     * abs.pop(null, null); l.nextStep();
     * 
     * abs.push("1n,c1", null, null); l.nextStep(); abs.push("12päihuhgizf",
     * defaultTiming, defaultTiming); l.nextStep(); abs.highlightTopElem(null,
     * null); l.nextStep(); abs.push("1rar3", null, null); l.nextStep();
     * abs.top(null, null); l.nextStep(); abs.pop(defaultTiming, defaultTiming);
     * l.nextStep(); abs.push("1r3", null, null); l.nextStep();
     * abs.highlightTopCell(null, null);; l.nextStep(); abs.pop(null, null);
     * l.nextStep(); abs.top(null, null); l.nextStep(); abs.pop(null, null);
     */

    // ListBasedStack
    /*
     * ListBasedStack<String> lbs = l.newListBasedStack(new Coordinates(10,70),
     * content, "LbStack", null, sp); l.nextStep(); lbs.top(null, null);
     * l.nextStep(); lbs.push("1", null, null); l.nextStep();
     * lbs.highlightTopElem(null, null); l.nextStep(); lbs.push("658742", null,
     * null); l.nextStep(); lbs.highlightTopCell(null, null); l.nextStep();
     * lbs.push("378765937", null, null); l.nextStep(); lbs.pop(null, null);
     * l.nextStep(); lbs.top(null, null); l.nextStep(); lbs.pop(null, null);
     * l.nextStep(); lbs.pop(null, null);
     */

    // ListElement le = l.newListElement(new Coordinates(10, 10), 1, null, null,
    // "l", null);

    QueueProperties qp = new QueueProperties();
    qp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    qp.set(AnimationPropertiesKeys.ALTERNATE_FILLED_PROPERTY, true);
    qp.set(AnimationPropertiesKeys.ALTERNATE_FILL_PROPERTY, Color.GREEN);
    qp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
    qp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    // ConceptualQueue
    /*
     * ConceptualQueue<String> cq = l.newConceptualQueue(new Coordinates(30,70),
     * content, "", null, qp); l.nextStep(); cq.enqueue("ghd6", null, null);
     * l.nextStep(); cq.tail(null, null); l.nextStep(); cq.enqueue("6", null,
     * null); l.nextStep(); cq.front(null, null); l.nextStep(); cq.dequeue(null,
     * null); l.nextStep(); cq.tail(null, null); l.nextStep();
     * cq.enqueue("ghjgfdkdhd6", null, null); l.nextStep(); cq.enqueue("1qayx",
     * null, null); l.nextStep(); cq.front(null, null); l.nextStep();
     * cq.enqueue("pögv65%%", null, null); l.nextStep(); cq.tail(null, null);
     * l.nextStep(); cq.dequeue(null, null); l.nextStep();
     * cq.enqueue("ghkjk0d6", null, null); l.nextStep(); cq.dequeue(null, null);
     * l.nextStep(); cq.tail(null, null); l.nextStep(); cq.front(null, null);
     * l.nextStep(); cq.dequeue(null, null); l.nextStep();
     * cq.enqueue("Hhgallo!!", null, null);
     */

    // ArrayBasedQueue
    /*
     * ArrayBasedQueue<String> abq = l.newArrayBasedQueue(new
     * Coordinates(30,70), content, "", null, qp, 4); l.nextStep();
     * abq.dequeue(null, null); l.nextStep(); abq.dequeue(null, null);
     * l.nextStep(); abq.dequeue(null, null); l.nextStep(); abq.enqueue("ghd6",
     * null, null); l.nextStep(); abq.tail(null, null); l.nextStep();
     * abq.enqueue("6", null, null); l.nextStep(); abq.front(null, null);
     * l.nextStep(); abq.dequeue(null, null); l.nextStep(); abq.tail(null,
     * null); l.nextStep(); abq.enqueue("ghjgf", null, null); l.nextStep();
     * abq.enqueue("1qayx", null, null); l.nextStep(); abq.front(null, null);
     * l.nextStep(); abq.enqueue("pögv65%%", null, null); l.nextStep();
     * abq.tail(null, null); l.nextStep(); abq.dequeue(null, null);
     * l.nextStep(); abq.enqueue("ghkjk0d6", null, null); l.nextStep();
     * abq.dequeue(null, null); l.nextStep(); abq.tail(null, null);
     * l.nextStep(); abq.front(null, null); l.nextStep(); abq.dequeue(null,
     * null); l.nextStep(); abq.enqueue("Hhgallo!!", null, null);
     */

    // ListBasedQueue
    ListBasedQueue<String> lbq = l.newListBasedQueue(new Coordinates(30, 70),
        content, "", null, qp);
    l.nextStep();
    lbq.highlightFrontCell(null, null);
    l.nextStep();
    lbq.moveBy("translate", 100, 10, null, null);
    l.nextStep();
    lbq.enqueue("ghd6", null, null);
    l.nextStep();
    lbq.tail(null, null);
    l.nextStep();
    lbq.enqueue("6", null, null);
    l.nextStep();
    lbq.front(null, null);
    l.nextStep();
    lbq.dequeue(null, null);
    l.nextStep();
    lbq.tail(null, null);
    l.nextStep();
    lbq.enqueue("ghjgfdkdhd6", null, null);
    l.nextStep();
    lbq.enqueue("1qayx", null, null);
    l.nextStep();
    lbq.front(null, null);
    l.nextStep();
    lbq.enqueue("pögv65%%", null, null);
    l.nextStep();
    lbq.tail(null, null);
    l.nextStep();
    lbq.dequeue(null, null);
    l.nextStep();
    lbq.enqueue("ghkjk0d6", null, null);
    l.nextStep();
    lbq.dequeue(null, null);
    l.nextStep();
    lbq.tail(null, null);
    l.nextStep();
    lbq.front(null, null);
    l.nextStep();
    lbq.dequeue(null, null);
    l.nextStep();
    lbq.enqueue("Hhgallo!!", null, null);

    System.out.println(l);

  }
  /*
   * %Animal 2 640*480 title "ConceptualStack Animation" author "Dima Vronskyi"
   * { array "StringArray1" (10,70) length 6 "xx" "xx" "xx" "xx" "xx" "xx" depth
   * 1 arrayMarker "top" on "StringArray1" atIndex 0 label "top" color (0,0,255)
   * depth 1 hide "top" } arrayPut "10" on "StringArray1" position 0 delay 0 ms
   * { moveArrayMarker "top" to position 0 show "top" } arrayPut "20000" on
   * "StringArray1" position 1 delay 0 ms moveArrayMarker "top" to position 1
   * arrayPut "30" on "StringArray1" position 2 delay 0 ms moveArrayMarker "top"
   * to position 2 arrayPut "11" on "StringArray1" position 3 delay 0 ms {
   * moveArrayMarker "top" to position 3 } { arrayPut "1200" on "StringArray1"
   * position 4 after 50 ticks within 50 ticks } delay 0 ms { moveArrayMarker
   * "top" to position 4 } { highlightArrayElem on "StringArray1" position 4 } {
   * unhighlightArrayElem on "StringArray1" position 4 arrayPut "1003" on
   * "StringArray1" position 5 } delay 0 ms { moveArrayMarker "top" to position
   * 5 } { arrayPut "xx" on "StringArray1" position 5 after 50 ticks within 50
   * ticks } delay 0 ms { moveArrayMarker "top" to position 4 } {
   * highlightArrayCell on "StringArray1" position 4 } { unhighlightArrayCell on
   * "StringArray1" position 4 arrayPut "xx" on "StringArray1" position 4 }
   * delay 0 ms { moveArrayMarker "top" to position 3 } { highlightArrayCell on
   * "StringArray1" position 3 highlightArrayElem on "StringArray1" position 3 }
   * { unhighlightArrayCell on "StringArray1" position 3 unhighlightArrayElem on
   * "StringArray1" position 3 arrayPut "xx" on "StringArray1" position 3 }
   * delay 0 ms { moveArrayMarker "top" to position 2 }
   */
}