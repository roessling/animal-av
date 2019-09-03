package algoanim.examples;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.IntArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.generators.Language;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class CounterMoveDemo {

  public static void main(String[] args) {
    ArrayDisplayOptions arrayDisOp = null;
    IntArray intArray;
    TwoValueCounter counter;
    TwoValueView view;
    Polyline line1;
    Language lang = new AnimalScript("CounterMoveDemo", "Axel Heimann", 640,
        480);
    lang.setStepMode(true);
    int[] data = { 1, 2, 3 };
    intArray = lang.newIntArray(new Coordinates(100, 100), data, "Array",
        arrayDisOp);
    counter = lang.newCounter(intArray);
    String[] names = { "longerString", "shortString" };
    view = lang.newCounterView(counter, new Coordinates(200, 200), null, true,
        true, names);
    lang.nextStep();
    view.moveBy(50, 50, null, null);
    lang.nextStep();
    Node pos1 = new Coordinates(300, 300);
    Node pos2 = new Coordinates(350, 350);
    Node[] nodes = { pos1, pos2 };
    line1 = lang.newPolyline(nodes, "line1", null);
    line1.hide();
    try {
      view.moveVia("NE", line1, null, null);
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }
    /**
     * lang.nextStep(); try { view.moveTo("SE", new Coordinates(300, 300), null,
     * null); } catch (IllegalDirectionException e1) { e1.printStackTrace(); }
     **/
    System.out.println(lang);

  }

}
