package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.DoubleArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class Erwartungswert implements Generator {
  // private Text header;
  private Text                  header2;
  private Text                  header3;
  private Text                  header4;
  private Text                  t1;
  private Text                  t2;
  private Text                  t3;
  private Text                  t4;
  private Text                  t5;
  private Text                  t6;
  private Text                  t7;
  private Text                  t8;
  private Text                  t9;
  private Text                  t10;
  private Text                  t11;
  private Text                  t12;

  // private Rect hRect;
  private ArrayProperties       arrayProps;
  private TextProperties        textProps;
  private ArrayMarkerProperties ami;

  private Language              lang;
  private SourceCodeProperties  sourceCode;

  private double[]              Werte;
  private double[]              Wahrscheinlichkeiten;
  private ArrayProperties       arrayWerte;
  private ArrayProperties       arrayWahrscheinlichkeiten;

  public void init() {
    // Generate a new Language instance for content creation
    // Parameter: Animation title, author, width, height
    lang = new AnimalScript("Erwartungswert berechnen",
        "Fatima Isufaj, Jasmin Diehl", 640, 480);
    // Activate step control
    lang.setStepMode(true);
    // create array properties with default values
    arrayProps = new ArrayProperties();
    // Redefine properties: border red, filled with gray
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.RED); // color
                                                                              // red
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // filled
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY); // fill
                                                                       // color
                                                                       // gray
    // marker for i: black with label "i"
    ami = new ArrayMarkerProperties();
    ami.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    ami.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    setArrayProps(arrayProps);
  }

  public Language getLang() {
    return lang;
  }

  public void setLang(Language lang) {
    this.lang = lang;
  }

  public ArrayProperties getArrayProps() {
    return arrayProps;
  }

  public void setArrayProps(ArrayProperties arrayProps) {
    this.arrayProps = arrayProps;
  }

  public void showSourceCode() {
    // first, set the visual properties for the source code
    // SourceCodeProperties scProps = new SourceCodeProperties();
    // scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    // scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
    // new Font("Monospaced", Font.PLAIN, 12));
    // scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    // scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    // now, create the source code entity
    SourceCode sc = lang.newSourceCode(new Coordinates(40, 400), "sc", null,
        sourceCode);
    // add a code line
    // parameters: code itself; name (can be null); indentation level; display
    // options
    sc.addCodeLine(
        "public double erwartungswert(double[] werte, double[] wahrscheinlichkeit){",
        null, 0, null);
    sc.addCodeLine("double erwartungswert=0;", null, 1, null);
    sc.addCodeLine("for (int i = 0; i< werte.length; i++){", null, 1, null);
    sc.addCodeLine(
        "erwartungswert = erwartungswert + werte[i]*wahrscheinlichkeit[i];",
        null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("return erwartungswert;", null, 1, null);
    sc.addCodeLine("}", null, 0, null);
    setSc(sc);

  }

  private SourceCode sc; // to ensure it is visible outside the method...

  public SourceCode getSc() {
    return sc;
  }

  public void setSc(SourceCode sc) {
    this.sc = sc;
  }

  public void erwartungswert(double[] werte, double[] wahrscheinlichkeit) {

    // Ueberschrift in rechteck wird erzeugt:
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    lang.newText(new Coordinates(20, 30), "Erwartungswert berechnen", "header",
        null, headerProps);
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", "SE"), "hRect", null, rectProps);

    if (werte.length != wahrscheinlichkeit.length) {
      textProps = new TextProperties();
      textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          Font.SANS_SERIF, Font.PLAIN, 16));
      lang.newText(new Coordinates(10, 100),
          "Geben Sie bitte zwei Arrays ein, die die selbe Länge haben.",
          "description1", null, textProps);
      lang.newText(
          new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW),
          "Das ist notwendig, da jedem Wert eine Wahrscheinlichkeit zugeordnet werden muss.",
          "description2", null, textProps);

    }

    else {
      // der Text auf der ersten folie wir nach und nach aufgebaut
      lang.nextStep();
      textProps = new TextProperties();
      textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          Font.SANS_SERIF, Font.PLAIN, 16));
      t1 = lang.newText(new Coordinates(10, 100),
          "Der Erwartungswert ist ein Grundbegriff der Stochastik.",
          "description1", null, textProps);
      t2 = lang.newText(new Offset(0, 25, "description1",
          AnimalScript.DIRECTION_NW),
          "Er ist das mit den entsprechenden Wahrscheinlichkeiten ",
          "description2", null, textProps);
      t3 = lang.newText(new Offset(0, 25, "description2",
          AnimalScript.DIRECTION_NW), "gewichtete arithmetische Mittel.",
          "description3", null, textProps);

      lang.nextStep();
      t5 = lang.newText(new Offset(0, 50, "description3",
          AnimalScript.DIRECTION_NW),
          " •  Sei X eine endliche Zufallsgröße, welche genau", "algo11", null,
          textProps);
      t6 = lang.newText(
          new Offset(25, 25, "algo11", AnimalScript.DIRECTION_NW),
          "die Werte Xi annehmen kann.", "algo12", null, textProps);
      t7 = lang.newText(new Offset(0, 25, "algo12", AnimalScript.DIRECTION_NW),
          "Diese Werte stehen im ersten Array. ", "algo13", null, textProps);

      lang.nextStep();
      t8 = lang.newText(
          new Offset(-25, 25, "algo13", AnimalScript.DIRECTION_NW),
          " •  Dabei hat der Wert Xi die Wahrscheinlichkeit P ( X = xi ).",
          "algo21", null, textProps);
      t9 = lang.newText(
          new Offset(25, 25, "algo21", AnimalScript.DIRECTION_NW),
          "Diese Wahrscheinlichkeiten stehen im zweiten Array ", "algo22",
          null, textProps);
      t10 = lang.newText(
          new Offset(0, 25, "algo22", AnimalScript.DIRECTION_NW),
          "und da es sich um Wahrscheinlichkeiten handelt, ", "algo23", null,
          textProps);
      t12 = lang.newText(
          new Offset(0, 25, "algo23", AnimalScript.DIRECTION_NW),
          "müssen die Einträge zwischen 0 und 1 liegen.", "algo24", null,
          textProps);

      lang.nextStep();
      t11 = lang.newText(new Offset(-25, 25, "algo24",
          AnimalScript.DIRECTION_NW),
          " •  Dann berechnet sich der Erwartungswert nach folgender Formel:",
          "algo31", null, textProps);
      t4 = lang.newText(
          new Offset(25, 25, "algo31", AnimalScript.DIRECTION_NW),
          "E(X) = x1 · P(X = X1 ) + x2 · P(X = x2 ) + ... + Xn · P(X = Xn ) ",
          "algo32", null, textProps);

      lang.nextStep();

      // der text auf der ersten folie verschwindet, nur die Ueberschrift bleibt
      t1.hide();
      t2.hide();
      t3.hide();
      t4.hide();
      t5.hide();
      t6.hide();
      t7.hide();
      t8.hide();
      t9.hide();
      t11.hide();
      t10.hide();
      t12.hide();

      // gleichzeitig werden die arrays und der code erzeugt

      TextProperties headerProps2 = new TextProperties();
      headerProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          Font.SANS_SERIF, Font.BOLD, 16));
      header2 = lang.newText(new Coordinates(20, 130), "Werte:", "header2",
          null, headerProps2);

      TextProperties headerProps3 = new TextProperties();
      headerProps3.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          Font.SANS_SERIF, Font.BOLD, 16));
      header3 = lang.newText(new Coordinates(20, 230), "Wahrscheinlichkeiten:",
          "header3", null, headerProps3);

      TextProperties headerProps4 = new TextProperties();
      headerProps4.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          Font.SANS_SERIF, Font.BOLD, 16));
      header4 = lang.newText(new Coordinates(20, 330), "Erwartungswert:",
          "header4", null, headerProps3);

      double erwartungswert = 0;

      DoubleArray arrayWE = lang.newDoubleArray(new Coordinates(200, 130),
          werte, "arrayWE", null, arrayWerte);
      DoubleArray arrayWA = lang.newDoubleArray(new Coordinates(200, 230),
          wahrscheinlichkeit, "arrayWA", null, arrayWahrscheinlichkeiten);
      Timing defaultTiming = new TicksTiming(15);
      showSourceCode(); // der code wird erzeugt
      lang.nextStep("Code");
      sc.highlight(0); // method head
      lang.nextStep(); // to show array without markers
      sc.toggleHighlight(0, 1); // jump from header to int...
      DoubleArray arrayEW = lang.newDoubleArray(new Coordinates(200, 330),
          new double[] { erwartungswert }, "arrayEW", null, arrayProps);
      lang.nextStep(); // to show k and i marker
      ArrayMarker i = lang.newArrayMarker(arrayWE, 0, "i", null, ami);
      ArrayMarker k = lang.newArrayMarker(arrayWA, 0, "i", null, ami);

      for (; i.getPosition() < arrayWE.getLength(); i.increment(null,
          defaultTiming), k.increment(null, defaultTiming)) {
        erwartungswert = erwartungswert + werte[i.getPosition()]
            * wahrscheinlichkeit[i.getPosition()];
        sc.toggleHighlight(1, 2);
        // sc.highlight(2); // now at line 2 (for loop)
        lang.nextStep(i.getPosition() + 1 + ". Schritt");
        sc.toggleHighlight(2, 3);
        arrayEW.put(0, erwartungswert, null, null);
        lang.nextStep();
        sc.unhighlight(3);
      }
      sc.highlight(5);
      i.hide();
      k.hide();
      lang.nextStep();
      sc.unhighlight(5);
      lang.nextStep();

      // alles wird versteckt, nur die Ueberschrift bleibt
      header2.hide();
      header3.hide();
      header4.hide();
      arrayEW.hide();
      arrayWE.hide();
      arrayWA.hide();
      sc.hide();

      // der Text auf der abschlussfolie wird erzeugt
      lang.nextStep();
      lang.newText(new Coordinates(10, 100),
          "Der Erwartungswert eines Spiels:", "text", null, textProps);
      lang.nextStep();
      lang.newText(new Offset(0, 50, "text", AnimalScript.DIRECTION_NW),
          "Wir betrachten die Werte als mögliche Gewinne bei ", "text2", null,
          textProps);
      lang.newText(new Offset(0, 25, "text2", AnimalScript.DIRECTION_NW),
          "einem Spiel, die ein Spieler mit einer bestimmten,", "text3", null,
          textProps);
      lang.newText(new Offset(0, 25, "text3", AnimalScript.DIRECTION_NW),
          "gegebenen Wahrscheinlichkeit erhält.", "text4", null, textProps);
      lang.newText(new Offset(0, 25, "text4", AnimalScript.DIRECTION_NW),
          "Dann gibt der Erwartungswert den durchschnittlichen Gewinn an.",
          "text5", null, textProps);
      lang.nextStep();
      lang.newText(new Offset(0, 50, "text5", AnimalScript.DIRECTION_NW),
          "Ist der Erwartungswert null, so ist das Spiel fair.", "text6", null,
          textProps);

    }

    return;

  }

  // work around some Animal bugs
  private static String workAround(String input) {

    // input = input.replaceAll(" refresh", "");
    // input = input.replaceAll(" row 0", "");

    String[] temp = input.split("}");
    String newOut = "";

    for (int i = 0; i < temp.length - 1; i++) {
      if (temp[i]
          .contains("Dann berechnet sich der Erwartungswert nach folgender Formel:")) {
        temp[i] = temp[i] + "}" + System.getProperty("line.separator")
            + "Label \"Einleitung\"";
      } else {
        temp[i] = temp[i] + "}";
      }
      newOut = newOut + temp[i];
    }
    return newOut + System.getProperty("line.separator")
        + "label \"Der Erwartungswert eines Spiels\"";
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");

    final String[] werte = (String[]) primitives.get("Werte");
    Werte = new double[werte.length];
    for (int i = 0; i < Werte.length; i++) {
      Werte[i] = Double.parseDouble(werte[i]);
    }

    final String[] wahrscheinlichkeiten = (String[]) primitives
        .get("Wahrscheinlichkeiten");
    Wahrscheinlichkeiten = new double[wahrscheinlichkeiten.length];
    for (int i = 0; i < Wahrscheinlichkeiten.length; i++) {
      Wahrscheinlichkeiten[i] = Double.parseDouble(wahrscheinlichkeiten[i]);
    }

    arrayWerte = (ArrayProperties) props.getPropertiesByName("arrayWerte");
    arrayWahrscheinlichkeiten = (ArrayProperties) props
        .getPropertiesByName("arrayWahrscheinlichkeiten");
    erwartungswert(Werte, Wahrscheinlichkeiten);

    return workAround(lang.toString());
  }

  public String getName() {
    return "Erwartungswert berechnen";
  }

  public String getAlgorithmName() {
    return "Erwartungswert";
  }

  public String getAnimationAuthor() {
    return "Fatima Isufaj, Jasmin Diehl";
  }

  public String getDescription() {
    return "Der Algorithmus \"erwartungswert\" berechnet aus gegebenen Werten und den zugehörigen Wahrscheinlichkeiten den Erwartungswert."
        + "\n"
        + "Dazu wird die folgende Formel verwendet:  E(X) = x1 · P(X = X1 ) + x2 · P(X = x2 ) + ... + Xn · P(X = Xn ).";
  }

  public String getCodeExample() {
    return "public double erwartungswert(double[] werte, double[] wahrscheinlichkeit){"
        + "\n"
        + "	double erwartungswert=0;"
        + "\n"
        + "	for (int i = 0; i< werte.length; i++){"
        + "\n"
        + "	          erwartungswert = erwartungswert + werte[i]*wahrscheinlichkeit[i];"
        + "\n"
        + "	}"
        + "\n"
        + "	return erwartungswert;"
        + "\n"
        + "                             }";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}
