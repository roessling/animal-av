package algoanim.examples;

import java.awt.Color;
import java.util.LinkedList;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayBasedStack;
import algoanim.primitives.ConceptualStack;
import algoanim.primitives.ListBasedStack;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.StackProperties;
import algoanim.util.Coordinates;

public class StacksDemo {

  public static void main(String[] args) {
    // TODO Auto-generated method stub

    StackProperties sp = new StackProperties();
    sp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    sp.set(AnimationPropertiesKeys.DIVIDINGLINE_COLOR_PROPERTY, Color.ORANGE);
    sp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
    sp.set(AnimationPropertiesKeys.ALTERNATE_FILLED_PROPERTY, true);
    sp.set(AnimationPropertiesKeys.ALTERNATE_FILL_PROPERTY, Color.GREEN);
    sp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    Language l = new AnimalScript("Stacks Animation", "Dima Vronskyi"
        + " vronskyi@googlemail.com", 640, 480);
    l.setStepMode(true);
    LinkedList<Integer> content = new LinkedList<Integer>();
    content.add(10);
    content.add(20);
    content.add(30);

    
    ConceptualStack<Integer> cs = l.newConceptualStack(new Coordinates(40, 70),
        content, "Cons", null, sp);
    l.nextStep();
    cs.moveBy("translate", 30, 20, null, null);

    l.nextStep();    
    ArrayBasedStack<Integer> abs = l.newArrayBasedStack(
        new Coordinates(140, 70), content, "AB", null, sp, 5);
    l.nextStep();
    abs.moveBy("translate", 30, 20, null, null);    l.nextStep();
    
    l.nextStep();
    ListBasedStack<Integer> lbs = l.newListBasedStack(new Coordinates(270, 70),
        content, "LB", null, sp);
    l.nextStep();
    lbs.moveBy("translate", 30, 20, null, null);
    
    System.out.println(l);
  }
}
