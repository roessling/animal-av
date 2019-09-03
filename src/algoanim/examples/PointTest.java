package algoanim.examples;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PointProperties;
import algoanim.util.Coordinates;


public class PointTest {
  Language lang = null;
  String header = "%Animal 2\ntitle \"PointTest\"\nauthor \"Guido\"";
  PointProperties pProps;
  
  @Before
  public void setup() {
    lang = new AnimalScript("PointTest", "Guido", 640, 480);
    pProps = new PointProperties();
    pProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
    pProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
  }
  
  @Test
  public void pointTest() {
    lang.newPoint(new Coordinates(20, 30), "p1", null, pProps);
    System.err.println(lang.toString());
    assertEquals(header + "\npoint \"p1\" (20, 30) color (255, 0, 0) depth 10", lang.toString().trim());
  }
}
