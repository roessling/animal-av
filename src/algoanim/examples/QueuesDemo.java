package algoanim.examples;

import java.awt.Color;
import java.util.LinkedList;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayBasedQueue;
import algoanim.primitives.ConceptualQueue;
import algoanim.primitives.ListBasedQueue;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.QueueProperties;
import algoanim.util.Coordinates;

public class QueuesDemo {

  public static void main(String[] args) {
    // TODO Auto-generated method stub

    QueueProperties qp = new QueueProperties();
    qp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    qp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
    qp.set(AnimationPropertiesKeys.ALTERNATE_FILLED_PROPERTY, true);
    qp.set(AnimationPropertiesKeys.ALTERNATE_FILL_PROPERTY, Color.GREEN);
    qp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    Language l = new AnimalScript("Queues Animation", "Dima Vronskyi"
        + " vronskyi@googlemail.com", 640, 480);
    l.setStepMode(true);
    LinkedList<Integer> content = new LinkedList<Integer>();
    content.add(10);
    content.add(20);
    content.add(30);

    
    ConceptualQueue<Integer> cq = l.newConceptualQueue(new Coordinates(40, 70),
        content, "Cons", null, qp);
    l.nextStep();
    cq.moveBy("translate", 30, 20, null, null);
    l.nextStep();
    
    ArrayBasedQueue<Integer> abq = l.newArrayBasedQueue(
        new Coordinates(190, 70), content, "AB", null, qp, 5);
    l.nextStep();
    abq.moveBy("translate", 30, 20, null, null);
    l.nextStep();
    
    ListBasedQueue<Integer> lbq = l.newListBasedQueue(new Coordinates(300, 70),
        content, "LB", null, qp);
    l.nextStep();
    lbq.moveBy("translate", 30, 20, null, null);

    System.out.println(l);
  }
}
