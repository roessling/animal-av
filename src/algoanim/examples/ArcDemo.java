package algoanim.examples;

import java.awt.Color;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Arc;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArcProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class ArcDemo {
  
  public String runTest() {
    Language l = new AnimalScript("ArcDemo", "Guido", 640, 480);
    l.setStepMode(true);
    ArcProperties arcP = new ArcProperties();
    arcP.set(AnimationPropertiesKeys.ANGLE_PROPERTY, 30);
    arcP.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, 180);
    Arc arc = l.newArc(new Coordinates(20, 30), new Coordinates(30, 40), "arc1", null, arcP);
    
    Arc arc1 = l.newArc(new Coordinates(120, 30), new Coordinates(20, 20), "arc2", null, arcP);
    Arc arc2 = l.newArc(new Coordinates(220, 30), new Coordinates(30, 30), "arc3", null, arcP);
    Arc arc3 = l.newArc(new Coordinates(320, 30), new Coordinates(40, 40), "arc4", null, arcP);
    Timing defaultTiming = new TicksTiming(20);
    l.nextStep();
    
    arc.changeColor("color", Color.RED, null, null);
    arc.moveBy("translate", 30, 80, null, defaultTiming);
    arc1.changeColor("color", Color.BLUE, null, null);
    l.nextStep();
    arc2.changeColor("color", Color.GREEN, null, null);
    l.nextStep();
    arc3.changeColor("color", Color.YELLOW, null, null);
//    arc.moveTo("translate", moveType, target, delay, duration)
    return l.toString();
  }
  
  public static void main(String[] args) {
    ArcDemo demo = new ArcDemo();
    System.err.println(demo.runTest());
  }
}
