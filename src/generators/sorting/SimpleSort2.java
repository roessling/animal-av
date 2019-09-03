package generators.sorting;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class SimpleSort2 implements Generator {
  /* Konstanten */
  private static final String        AUTHOR                  = "Alper Özdemir, Bilal Balci, Gökhan Simsek";
  private static final Node          COORDINATES_HEADER      = new Coordinates(
                                                                 10, 20);
  private static final Node          COORDINATES_ARRAY       = new Coordinates(
                                                                 10, 100);
  private static final Node          COORDINATES_SOURCECODE  = new Coordinates(
                                                                 40, 140);
  private static final Coordinates   COORDINATES_DESCRIPTION = new Coordinates(
                                                                 10, 350);

  /* Atribute */
  IntArray                           array;
  ArrayMarker                        i;
  ArrayMarker                        j;
  AnimalScript                       lang;
  ArrayProperties                    arp;
  ArrayMarkerProperties              ami;
  ArrayMarkerProperties              amj;
  SourceCodeProperties               scProps;
  SourceCode                         sc;
  boolean                            negativeExample         = false;
  boolean                            positiveExample         = false;

  /* Beschreibungen */
  Text                               description;
  Timing                             defaultTiming;
  private String[]                   descriptionText;
  private Hashtable<String, Integer> descriptionVars;

  /* Konstruktor */
  public SimpleSort2() {
    this.lang = new AnimalScript("SimpleSort Animation", AUTHOR, 640, 480);
    lang.setStepMode(true);
    initSimpleSort();
  }

  private void initSimpleSort() {
    defaultTiming = new TicksTiming(15);
    /* Description and header */
    Text header = lang.newText(COORDINATES_HEADER, "Simplesort", "header",
        defaultTiming);
    header.setFont(new Font(Font.SERIF, Font.BOLD, 40), null, defaultTiming);
    description = lang.newText(COORDINATES_DESCRIPTION,
        "Beschreibung des Simplesort", "description", defaultTiming);
    description.setFont(new Font(Font.SERIF, Font.PLAIN, 20), null,
        defaultTiming);
    descriptionText = new String[9];
    descriptionText[0] = "Die Methode erhölt als Eingabe einen Array mit Zahlen.";
    descriptionText[1] = "Die benötigten Zeiger i und j werden initialisiert.";
    descriptionText[2] = "Der Zeiger i ist an der Position %pos.";
    descriptionText[3] = "Der Zeiger j geht um eine Position weiter nach rechts. ";
    descriptionText[4] = "Ist die Bedingung %i > %j  erfüllt? ";
    descriptionText[5] = "%i ist größer als %j, also erfolgt hier eine Vertauschung.";
    descriptionText[6] = "Die Bedingung %i > %j ist nicht erfüllt, daher erfolgt keine Vertauschung.";
    descriptionText[7] = "Der Zeiger j positioniert sich an der Stelle i+1.";
    descriptionText[8] = "Die sortierte Liste";
    descriptionVars = new Hashtable<String, Integer>();
    lang.newRect(new Coordinates(COORDINATES_DESCRIPTION.getX() - 5,
        COORDINATES_DESCRIPTION.getY() - 5), new Coordinates(
        COORDINATES_DESCRIPTION.getX() + 560,
        COORDINATES_DESCRIPTION.getY() + 20), "rect_description", defaultTiming);
    // initProperties();
  }

  /*
   * private void initProperties() { // Properties arp = new ArrayProperties();
   * arp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
   * arp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
   * arp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.red);
   * arp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.green); //
   * marker for i: black with label 'i' ami = new ArrayMarkerProperties();
   * ami.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
   * ami.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i"); // marker for j: blue
   * with label 'j' amj = new ArrayMarkerProperties();
   * amj.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
   * amj.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
   * 
   * scProps = new SourceCodeProperties();
   * scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
   * scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font( "Monospaced",
   * Font.PLAIN, 16));
   * scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
   * scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK); }
   */

  /* Der Algorithmus */
  public void simpleSort(int[] arrayContents) {
    array = lang.newIntArray(COORDINATES_ARRAY, arrayContents, "array", null,
        arp);
    showSourceCode();
    sc.highlight(0);
    showDescription(0);
    lang.addLabel("Einführung");
    lang.nextStep();
    i = lang.newArrayMarker(array, 0, "i", null, ami);
    j = lang.newArrayMarker(array, 1, "j", null, amj);
    sc.toggleHighlight(0, 1);
    showDescription(1);
    i.show();
    array.highlightCell(0, null, defaultTiming);
    lang.addLabel("Initialisierung der Variablen");
    lang.nextStep(); // to show i marker
    sc.unhighlight(1);
    int count = 0;
    for (; i.getPosition() < arrayContents.length - 1; i.increment(null,
        defaultTiming)) {
      array.highlightCell(i.getPosition(), null, defaultTiming);
      sc.highlight(2);
      showDescription(2);
      lang.nextStep();
      sc.unhighlight(2);
      for (j.move(i.getPosition() + 1, null, defaultTiming); j.getPosition() < arrayContents.length; j
          .increment(null, defaultTiming)) {
        sc.highlight(3);
        if (j.getPosition() == i.getPosition() + 1) {
          showDescription(7);
        } else {
          showDescription(3);
        }
        lang.nextStep();
        sc.toggleHighlight(3, 4);
        showDescription(4);
        if (!negativeExample || !positiveExample) {
          if (arrayContents[i.getPosition()] > arrayContents[j.getPosition()]) {
            if (!positiveExample) {
              lang.addLabel("Vergleich der Elemente (positiv)");
              positiveExample = true;
            }

          } else {
            if (!negativeExample) {
              lang.addLabel("Vergleich der Elemente (negativ)");
              negativeExample = true;
            }
          }
        }

        if (arrayContents[i.getPosition()] > arrayContents[j.getPosition()]) {
          lang.nextStep(); // wait for update of j
          array.highlightCell(j.getPosition(), null, defaultTiming);
          lang.nextStep();
          sc.toggleHighlight(4, 5);
          showDescription(5);
          array.swap(i.getPosition(), j.getPosition(), null, defaultTiming);
          count++;
          CheckpointUtils.checkpointEvent(this, "countSwap", new Variable(
              "swaps", count),
              new Variable("thisone", arrayContents[i.getPosition()]),
              new Variable("nextone", arrayContents[j.getPosition()]));
          array.unhighlightCell(j.getPosition(), null, defaultTiming);
          lang.nextStep();
          sc.unhighlight(5);
        } else {
          lang.nextStep();
          showDescription(6);
          array.highlightElem(j.getPosition(), null, defaultTiming);
          lang.nextStep();
          array.unhighlightElem(j.getPosition(), null, defaultTiming);
          sc.unhighlight(4);
        }

        lang.nextStep();
      }
    }

    array.highlightCell(array.getLength() - 1, null, defaultTiming);
    lang.nextStep();
    lang.addLabel("Die sortierte Liste");
    array.hide();
    i.hide();
    j.hide();
    array = lang.newIntArray(COORDINATES_ARRAY, arrayContents, "array", null,
        arp);
    showDescription(8);
    lang.nextStep();

  }

  /* Methoden zur Anzeige der Beschreibungen */
  private void updateDescription() {
    descriptionVars.put("%i", array.getData(i.getPosition()));
    if (j.getPosition() < array.getLength())
      descriptionVars.put("%j", array.getData(j.getPosition()));
    descriptionVars.put("%pos", i.getPosition());
  }

  private void showDescription(int pos) {
    if (pos != 0)
      updateDescription();
    String descriptionNewText = descriptionText[pos];
    for (String key : descriptionVars.keySet()) {
      if (descriptionNewText.contains(key)) {
        descriptionNewText = descriptionNewText.replace(key,
            String.valueOf(descriptionVars.get(key)));
      }
    }
    description.setText(descriptionNewText, null, defaultTiming);
  }

  /* Methode zur Anzeige des SourceCodes */
  private String showSourceCode() {
    this.sc = lang.newSourceCode(COORDINATES_SOURCECODE, "sourceCode", null,
        scProps);
    /* 0 */addCodeLine("public void simpleSort(int[] arrayContents) {", 0);
    /* 1 */addCodeLine("int i, j;", 1);
    /* 2 */addCodeLine("for(i=0; i<arrayContents.length; i++){", 1);
    /* 3 */addCodeLine("for (j=i+1; j<arrayContents.length;j++){", 2);
    /* 4 */addCodeLine("if(arrayContents[i]>arrayContents[j])", 3);
    /* 5 */addCodeLine("swap(arrayContents, i, j);", 4);
    /* 7 */addCodeLine("}", 3);
    /* 8 */addCodeLine("}", 2);
    /* 9 */addCodeLine("}", 1);
    return sc.toString();
  }

  /* Methoden für den Generator */
  public AnimalScript getLang() {
    return lang;
  }

  private void addCodeLine(String codeLine, int space) {
    sc.addCodeLine(codeLine, null, space, null);
  }

  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    arp = (ArrayProperties) arg0.get(0);
    ami = (ArrayMarkerProperties) arg0.get(1);
    amj = (ArrayMarkerProperties) arg0.get(2);
    scProps = (SourceCodeProperties) arg0.get(3);
    simpleSort((int[]) arg1.get("intArray"));
    return lang.toString();
  }

  public String getAlgorithmName() {
    return "Simple Sort";
  }

  public String getAnimationAuthor() {
    return AUTHOR;
  }

  public String getCodeExample() {
    return "public void simpleSort(int[] arrayContents) {\n" + "int i, j;\n"
        + "for(i=0; i<arrayContents.length; i++){\n"
        + "for (j=i+1; j<arrayContents.length;j++){\n"
        + "if(arrayContents[i]>arrayContents[j])\n"
        + "swap(arrayContents, i, j);\n" + "}\n" + "}\n" + "}";

  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getDescription() {
    return "Simplesort ist ein stabiles in-place Sortierverfahren.\n"
        + "In seiner einfachsten Form hat Simplesort \n"
        + "für ein Array der Länge n n der Landau-Notation\n"
        + "einen Zeit-Aufwand von O(n*n). \n"
        + "Simplesort zeichnet sich durch einen besonders \n"
        + "einfachen Algorithmus aus.Die intuitive Idee \n"
        + "hinter Simplesort ist, dass man die Positionen \n"
        + "im zu sortierenden Arrays nacheinander betrachtet\n"
        + "und das jeweils passende Element einsortiert.\n"
        + "siehe www.wikipedia.de";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getName() {
    return "Simple Sort";
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public void init() {
    // TODO Auto-generated method stub

  }

}
