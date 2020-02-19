package generators.misc;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.main.Animal;

/*
 * PumpingLemma.java
 * Frederik Röper, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
// package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

public class PumpingLemma implements Generator {
  private Language lang;

  public static void main(String[] args) {
    Generator generator = new PumpingLemma(); // Generator erzeugen
    Animal.startGeneratorWindow(generator); // Animal mit Generator starten
  }

  public void init() {
    lang = new AnimalScript("Pumping Lemma", "Frederik Röper", 800, 600);
    lang.setStepMode(true);
  }

  public final static Timing defaultDuration = new TicksTiming(30);
  public int graph_x = 20;
  public int graph_y = 180;
  public int step_length = 28;
  public int step_height = 15;
  public int height_offset = 26;
  public int row_height = 17;

  public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
    // ArcProperties arc = (ArcProperties) props.getPropertiesByName("arc");

    paint_description();

    return lang.toString();
  }

  public void paint_description() {
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
    Text textHeader = lang.newText(new Coordinates(20, 20), "Pumping Lemma für reguläre Sprachen", "Text", null,
        headerProps);

    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 12));
    String[] description = new String[] {
        "Durch das Pumping Lemma lässt sich nachweisen, dass eine formale Sprache nicht regulär ist. Eine",
        "reguläre Sprache kann durch einen endlichen Automaten erkannt und von regulären Ausdrücken beschrieben",
        "werden. Sollte eine formale Sprache das Pumping Lemma nicht bestehen ist klar, dass die Sprache nicht",
        "regulär ist. Umgekehrt kann man allerdings nicht sagen, dass die Sprache dann regulär ist." };

    for (int i = 0; i < description.length; i++) {
      Text textDescripton = lang.newText(new Coordinates(20, 50 + (this.row_height * i)), description[i], "Text", null,
          textProps);
    }

    lang.nextStep();

    RectProperties whiteRectProps = new RectProperties();
    whiteRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    whiteRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);

    lang.newRect(new Coordinates(10, 140), new Coordinates(700, 280), "Rect", null, whiteRectProps);

    String[] definition = new String[] { "Definition Pumping Lemma:",
        "Für jede reguläre Sprache L gibt es eine natürliche Zahl n, sodass gilt: Jedes Wort z in L mit ",
        "Mindestlänge n hat eine Zerlegung z = uvw mit den folgenden drei Eigenschaften:",
        "1. Die beiden Wörter u und v haben zusammen höchstens die Länge n", "2. Das Wort v ist nicht leer",
        "3. Für jede natürliche Zahl (mit 0), ist das Wort uvⁱw in der Sprache L, d.h. die Wörter uw, uvw,",
        "    uvvw, uvvvw, uvvvvw usw sind alle ind er Sprache L." };
    for (int i = 0; i < definition.length; i++) {
      Text textDescripton = lang.newText(new Coordinates(20, 150 + (this.row_height * i)), definition[i], "Text", null,
          textProps);
    }

    lang.nextStep();

    lang.newText(new Coordinates(20, 300), "Beispiel:", "Text", null, textProps);
    lang.newText(new Coordinates(20, 317), "Nehmen wir folgenden Automaten", "Text", null, textProps);

    Node[] nodes = new Node[2];
    nodes[0] = new Coordinates(this.graph_x, this.graph_y);
    nodes[1] = new Coordinates(this.graph_x + 20, this.graph_y + 20);

    setCircle(50, 355, "s1", false);
    setArrow(65, 355, 130, 355, "a", -5);
    setCircle(145, 355, "s2", true);
    setArrow(134, 366, 106, 414, "b", 3);
    setCircle(95, 425, "s3", false);
    setArrow(110, 425, 170, 425, "a", -5);
    setCircle(185, 425, "s4", false);
    setArrow(174, 414, 156, 366, "b", -8);

    lang.newText(new Coordinates(90, 445), "a(bab)ⁱ", "Text", null, textProps);

    lang.nextStep();

    String[] exercise = new String[] { "Wir suchen jetzt eine Zerlegung z in u, v und w, sodass alle",
        "Bedingungen erfüllt werden:", "Wir wählen u=a v=bab w=Ø",
        "1. Die beiden Wörter u und v haben zusammen höchstens die Länge n:", "2. Das Wort v ist nicht leer",
        "3. Für jede natürliche Zahl (mit 0), ist das Wort uvⁱw in der Sprache L, d.h. die Wörter a, abab,",
        "    ababbab, ababbabbab, ababbabbabbab usw sind alle in der Sprache L." };

    int[] number = { 2, 3, 4 };
    for (int i = 0; i < exercise.length; i++) {
      Text textDescripton = lang.newText(new Coordinates(300, 317 + (this.row_height * i)), exercise[i], "Text", null,
          textProps);
      if (contains(number, i)) {
        lang.nextStep();
      }
    }

    lang.nextStep();

    lang.newRect(new Coordinates(10, 470), new Coordinates(700, 495), "Rect", null, whiteRectProps);

    String[] conclusion = new String[] { "=> Der Automat besteht das Pumping Lemma" };
    for (int i = 0; i < conclusion.length; i++) {
      lang.newText(new Coordinates(20, 475 + (this.row_height * i)), conclusion[i], "Text", null, textProps);
    }
  }

  public void setCircle(int x, int y, String text, boolean accepting) {
    CircleProperties circleProperties = new CircleProperties();
    // circleProperties.set(AnimationPropertiesKeys.BWARROW_PROPERTY, true);
    lang.newCircle(new Coordinates(x, y), 15, "", null, circleProperties);
    if (accepting) {
      lang.newCircle(new Coordinates(x, y), 20, "", null, circleProperties);
    }
    lang.newText(new Coordinates(x - 6, y - 8), text, "Text", null);
  }

  public void setArrow(int begin_x, int begin_y, int end_x, int end_y, String arrow_desc, int offset) {
    Node[] nodes = new Node[2];
    nodes[1] = new Coordinates(begin_x, begin_y);
    nodes[0] = new Coordinates(end_x, end_y);

    PolylineProperties polylineProperties = new PolylineProperties();
    polylineProperties.set(AnimationPropertiesKeys.BWARROW_PROPERTY, true);
    lang.newPolyline(nodes, "Arrow", null, polylineProperties);

    int first_x, secound_x, first_y, second_x, second_y;
    if (begin_x <= end_x) {
      first_x = begin_x;
      second_x = end_x;
    } else {
      first_x = end_x;
      second_x = begin_x;
    }
    if (begin_y <= end_y) {
      first_y = begin_y;
      second_y = end_y;
    } else {
      first_y = end_y;
      second_y = begin_y;
    }

    lang.newText(new Coordinates(first_x + Math.abs(first_x - second_x) / 2 + offset,
        first_y + Math.abs(first_y - second_y) / 2), arrow_desc, "Text", null);

  }

  public boolean contains(int[] array, int v) {

    boolean result = false;

    for (int i : array) {
      if (i == v) {
        result = true;
        break;
      }
    }

    return result;
  }

  public String getName() {
    return "Pumping Lemma";
  }

  public String getAlgorithmName() {
    return "Pumping Lemma";
  }

  public String getAnimationAuthor() {
    return "Frederik Röper";
  }

  public String getDescription() {
    return "Durch das Pumping Lemma lässt sich nachweisen, dass eine formale Sprache nicht regulär ist. Eine "
        + "reguläre Sprache kann durch einen endlichen Automaten erkannt und von regulären Ausdrücken beschrieben"
        + "werden. Sollte eine formale Sprache das Pumping Lemma nicht bestehen ist klar, dass die Sprache nicht"
        + "regulär ist. Umgekehrt kann man allerdings nicht sagen, dass die Sprache dann regulär ist.";
  }

  public String getCodeExample() {
    return "";
  }

  public String getFileExtension() {
    return "asu";
  }

  public Locale getContentLocale() {
    return Locale.GERMAN;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}