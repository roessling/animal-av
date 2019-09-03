package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;
import algoanim.util.Timing;

/**
 * 
 * @author Thomas Schulz, Benjamin Lueck
 */
public class Josephus implements Generator {
  private Language              lang;
  private TextProperties        normalText;
  private SourceCodeProperties  sourceCode;
  private RectProperties        rect;
  private int                   Schrittweite;
  private int                   Soldaten;
  private ArrayProperties       array;
  private ArrayMarkerProperties arrayMarker;

  private TextProperties        titleProperties;
  // private Rect hRect;
  private static final String   CURRENT = "Aktuell: ";
  private static final String   I       = "i=";
  private static final String   STEP    = "step=";
  private static final String   DEAD    = "dead=";
  private static final String   EXIT    = "Abbruch der Schleife";
  private SourceCode            sc;
  private SourceCode            desc;
  private SourceCode            finish;
  private TextProperties        varProperties;

  public void init() {
    lang = new AnimalScript("", "", 800, 600);

    lang.setStepMode(true);
    titleProperties = new TextProperties();
    titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Serif", Font.BOLD, 24));
    titleProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    varProperties = new TextProperties();
    varProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",
        Font.BOLD, 16));
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    normalText = (TextProperties) props.getPropertiesByName("normalText");
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    rect = (RectProperties) props.getPropertiesByName("rect");
    Schrittweite = (Integer) primitives.get("Schrittweite");
    Soldaten = (Integer) primitives.get("Soldaten");
    array = (ArrayProperties) props.getPropertiesByName("array");
    arrayMarker = (ArrayMarkerProperties) props
        .getPropertiesByName("arrayMarker");

    josephus(Soldaten, Schrittweite);

    return lang.toString();
  }

  public String getName() {
    return "Josephus Problem [DE]";
  }

  public String getAlgorithmName() {
    return "Josephus Problem";
  }

  public String getAnimationAuthor() {
    return "Benjamin Lück, Thomas Schulz";
  }

  public String getDescription() {
    return "Das Josephus-Problem oder die Josephus-Permutation ist ein theoretisches Problem "
        + "\n"
        + "aus der Informatik oder Mathematik. Es werden n nummerierte Objekte im Kreis angeordnet, "
        + "\n"
        + "dann wird beginnend mit der Nummer s, jedes s-te Objekt entfernt, wobei der Kreis immer "
        + "\n"
        + "wieder geschlossen wird. Die Reihenfolge der entfernten Objekte wird als Josephus-Permutation "
        + "\n"
        + "bezeichnet. Ziel dieses Problems ist es, bei gegebenem n und s, das letzte Objekt der Permutation "
        + "\n"
        + "zu bestimmen."
        + "<br><br>\n"
        + "Geschichtlicher Hintergrund:<br>"
        + "Das Problem wurde nach dem j&ouml;dischen Historiker Flavius Josephus benannt, welcher sich 67 n. Chr. "
        + "beim Kampf um die galil&auml;ische Stadt Jotapata mit 40 weiteren M&auml;nnern in einer H&ouml;hle vor den R&ouml;mern"
        + "\n"
        + "versteckt hielt. Als das Versteck verraten wurde, sicherten die R&ouml;mer Josephus freies Geleit zu, "
        + "\n"
        + "wenn er das Versteck verl&auml;sst. Seine Gefolgsleute drohten allerdings ihn umzubringen und wollten "
        + "\n"
        + "lieber sterben, als den R&ouml;mern in die H&auml;nde zu fallen. Daraufhin machte Josephus den Vorschlag eines"
        + "\n"
        + "kollektiven Suizids, in dem sich alle im Kreis aufstellen und jeder 3. durch seinen rechten Nachbarn"
        + "\n"
        + "get&ouml;tet werden sollte. Er stellte sich an die 16. Stelle und blieb damit als Vorletzter &uuml;brig, "
        + "\n"
        + "&uuml;berw&auml;ltigte den schw&auml;cheren Mann an der 31. Stelle. Beide ergaben sich den R&ouml;mern und &uuml;berlebten."
        + "<br><br>"
        + "Quelle: Wikipedia (http://de.wikipedia.org/wiki/Josephus-Problem)";
  }

  public String getCodeExample() {
    return "	private static int josephus(int n, int k) {" + "\n"
        + "		int dead = 0; " + "\n" + "		int i = 0;" + "\n"
        + "		boolean[] array = new boolean[n];" + "\n" + "		while (dead < n) {"
        + "\n" + "			int step = 0;" + "\n" + "			while (step < k) {" + "\n"
        + "				step++;" + "\n" + "				do {						" + "\n" + "					i++;" + "\n"
        + "					i = i % n;" + "\n" + "				} while (array[i] == true);" + "\n"
        + "			}" + "\n" + "			dead++;							" + "\n" + "			array[i] = true;"
        + "\n" + "		}" + "\n" + "		return i;							" + "\n" + "	}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  private int josephus(int n, int k) {
    // displaying texts etc.
    lang.newText(new Coordinates(20, 30), "Josephus Problem", "title", null,
        titleProperties);

    // rectangle around the title
    Offset rectNW = new Offset(-5, -5, "title", AnimalScript.DIRECTION_NW);
    Offset rectSE = new Offset(5, 5, "title", AnimalScript.DIRECTION_SE);
    // RectProperties rectProps = new RectProperties();
    // rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    // rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    // rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    lang.newRect(rectNW, rectSE, "hRect", null, rect);

    showDescription();
    desc.hide();
    // showSourceCode();

    // show variables
    Text variables = lang.newText(new Offset(0, 5, "hRect",
        AnimalScript.DIRECTION_SW), "Variablen", "var", null, varProperties);
    Text txtn = lang.newText(
        new Offset(10, 0, "var", AnimalScript.DIRECTION_SW), "n=" + n, "txtn",
        null, normalText);
    Text txtk = lang.newText(new Offset(10, 0, "txtn",
        AnimalScript.DIRECTION_NE), "k=" + k, "txtk", null, normalText);
    Text txtdead = lang.newText(new Offset(10, 0, "txtk",
        AnimalScript.DIRECTION_NE), DEAD + "=?", "txtdead", null, normalText);
    Text txti = lang.newText(new Offset(10, 0, "txtdead",
        AnimalScript.DIRECTION_NE), I + "=?", "txti", null, normalText);
    Text txtstep = lang.newText(new Offset(10, 0, "txti",
        AnimalScript.DIRECTION_NE), STEP + "?", "txtstep", null, normalText);
    Text txtcurrent = lang.newText(new Offset(30, 0, "txtstep",
        AnimalScript.DIRECTION_NE), CURRENT, "txtcurrent", null, normalText);
    lang.nextStep("Quelltext");

    // start of the algo
    showSourceCode();
    lang.nextStep();
    sc.highlight(0);
    lang.nextStep("Initialisierung");

    sc.toggleHighlight(0, 1);
    int dead = 0;
    txtdead.setText("dead=" + dead, null, null);

    lang.nextStep();
    int i = 0;
    sc.toggleHighlight(1, 2); // i = 0
    txti.setText("i=" + i, null, null);

    lang.nextStep();
    // show array t=tot f=lebendig
    Text soldiers = lang
        .newText(new Offset(-10, 5, "txtn", AnimalScript.DIRECTION_SW),
            "Soldaten", "soldiers", null, varProperties);
    String[] solArr = new String[n];

    Arrays.fill(solArr, "f");
    StringArray soldierArr = lang.newStringArray(new Offset(10, 55, "soldiers",
        AnimalScript.DIRECTION_SW), solArr, "solArr", null, array);
    Timing defTime = new MsTiming(250);
    ArrayMarker iMark = lang.newArrayMarker(soldierArr, i, "i", null,
        arrayMarker);
    boolean[] array = new boolean[n]; // 5
    sc.toggleHighlight(2, 3);
    Text txtDesc = lang.newText(new Offset(40, 45, "solArr",
        AnimalScript.DIRECTION_NE), "t=tot", "txtDesc", null);
    Text txtDesc2 = lang.newText(new Offset(0, 2, "txtDesc",
        AnimalScript.DIRECTION_SW), "f=lebendig", "txtDesc2", null);

    while (dead < n) {
      lang.nextStep("while (" + dead + " < " + n + ")");
      if (dead < n) {
        sc.unhighlight(15);
        sc.toggleHighlight(3, 4);
        txtcurrent.setText(CURRENT + dead + "<" + n, null, null);
        txtcurrent.changeColor("color", Color.GREEN, null, null);
      } else {
        sc.unhighlight(15);
        sc.toggleHighlight(3, 4);
        txtcurrent.setText(CURRENT + dead + "<" + n, null, null);
        txtcurrent.changeColor("color", Color.RED, null, null);
      }

      lang.nextStep();
      int step = 0;
      txtcurrent.setText(CURRENT, null, null);
      txtcurrent.changeColor("color", Color.BLACK, null, null);
      sc.toggleHighlight(4, 5);
      txtstep.setText(STEP + step, null, null);

      // lang.nextStep();
      // sc.toggleHighlight(5, 6);
      // txtcurrent.setText(CURRENT + step + "<" + k, null, null); // noch grün
      // machen wenn es stimmt / rot wenn nicht
      while (step < k) {
        lang.nextStep();
        sc.toggleHighlight(5, 6);
        sc.unhighlight(11);
        if (step < k)
          txtcurrent.changeColor("color", Color.GREEN, null, null);
        else
          txtcurrent.changeColor("color", Color.RED, null, null);
        txtcurrent.setText(CURRENT + step + "<" + k, null, null); // noch grün
                                                                  // machen wenn
                                                                  // es stimmt /
                                                                  // rot wenn
                                                                  // nicht

        lang.nextStep();
        step++;
        sc.toggleHighlight(6, 7);
        txtstep.setText(STEP + step, null, null);
        txtcurrent.setText(CURRENT, null, null);
        txtcurrent.changeColor("color", Color.BLACK, null, null);

        lang.nextStep();
        sc.toggleHighlight(7, 8);
        do {

          lang.nextStep();
          sc.toggleHighlight(8, 9);
          sc.unhighlight(11);
          sc.highlight(10);
          i++;
          i = i % n;
          txti.setText(I + i, null, null);
          iMark.move(i, null, null);

          lang.nextStep();
          sc.unhighlight(9);
          sc.toggleHighlight(10, 11);
          txtcurrent.setText(CURRENT + array[i] + "==true", null, null); // noch
                                                                         // grün
                                                                         // machen
                                                                         // wenn
                                                                         // es
                                                                         // stimmt
                                                                         // /
                                                                         // rot
                                                                         // wenn
                                                                         // nicht
          if (array[i] == true)
            txtcurrent.changeColor("color", Color.GREEN, null, null);
          else
            txtcurrent.changeColor("color", Color.RED, null, null);

          // nur der schönere ausgabe wegen
          if (step >= k && array[i] == false) {
            lang.nextStep();
            sc.highlight(6);
            sc.unhighlight(11);
            txtcurrent.setText(CURRENT + step + "<" + k + "  --> " + EXIT,
                null, null); // noch grün machen wenn es stimmt / rot wenn nicht
            if (step < k)
              txtcurrent.changeColor("color", Color.GREEN, null, null);
            else
              txtcurrent.changeColor("color", Color.RED, null, null);
          }

        } while (array[i] == true);

      }

      // nur der schönere ausgabe wegen
      if (dead >= n) {
        lang.nextStep();
        sc.highlight(4);
        sc.unhighlight(6);
        txtcurrent.setText(CURRENT + dead + "<" + n + "  --> Abbruch", null,
            null); // noch grün machen wenn es stimmt / rot wenn nicht
        txtcurrent.changeColor("color", Color.RED, null, null);
      } else {
        lang.nextStep();
        sc.highlight(4);
        sc.unhighlight(6);
        txtcurrent.setText(CURRENT + dead + "<" + n, null, null); // noch grün
                                                                  // machen wenn
                                                                  // es stimmt /
                                                                  // rot wenn
                                                                  // nicht
        txtcurrent.changeColor("color", Color.GREEN, null, null);
      }

      lang.nextStep();
      sc.unhighlight(4); // while dead<n
      sc.unhighlight(6); // while step<k
      sc.highlight(12);
      txtcurrent.setText(CURRENT, null, null); // noch grün machen wenn es
                                               // stimmt / rot wenn nicht
      txtcurrent.changeColor("color", Color.BLACK, null, null);

      lang.nextStep();
      sc.toggleHighlight(12, 13);
      dead++;
      txtdead.setText(DEAD + dead, null, null);

      // System.out.println("dying " + i);
      lang.nextStep();
      sc.toggleHighlight(13, 14);
      array[i] = true;
      solArr[i] = "t";
      soldierArr.highlightCell(i, null, defTime);
      soldierArr.put(i, "t", null, null);

      lang.nextStep("Soldat " + (i + 1) + " stirbt.");
      sc.toggleHighlight(14, 15);
      soldierArr.unhighlightCell(i, null, defTime);
    }

    lang.nextStep();
    sc.toggleHighlight(15, 16);

    lang.nextStep();
    sc.unhighlight(16);
    Text lastManTxt = lang
        .newText(
            new Coordinates(120, 110),
            "Der Soldat, welcher durch den Marker >> i << markiert ist, ist der letzte ueberlebende.",
            "lastManTxt", null, normalText);

    lang.nextStep();
    Text lastManTxt2 = lang.newText(new Offset(0, 5, "lastManTxt",
        AnimalScript.DIRECTION_SW), "In diesem Fall der Soldat Nummer "
        + (i + 1) + ".", "lastManTxt2", null, normalText);

    lang.nextStep();
    // lang.hideAllPrimitivesExcept(title);
    soldierArr.hide();
    txtDesc.hide();
    txtDesc2.hide();
    lastManTxt2.hide();
    lastManTxt.hide();
    soldiers.hide();
    variables.hide();
    txtn.hide();
    txtk.hide();
    txtdead.hide();
    txti.hide();
    txtstep.hide();
    txtcurrent.hide();
    sc.hide();

    lang.nextStep();
    showFinish();

    return i;
  }

  public void showSourceCode() {
    sc = lang.newSourceCode(new Coordinates(20, 200), "sc", null, sourceCode);
    sc.addCodeLine("private int josephus(int n, int k) {", null, 0, null);
    sc.addCodeLine("int dead = 0; // dead soldiers", null, 1, null);
    sc.addCodeLine("int i = 0;", null, 1, null);
    sc.addCodeLine("boolean[] array = new boolean[n];", null, 1, null);
    sc.addCodeLine("while (dead < n) {", null, 1, null);
    sc.addCodeLine("int step = 0;", null, 2, null);
    sc.addCodeLine("while (step < k) {", null, 2, null);
    sc.addCodeLine("step++;", null, 3, null);
    sc.addCodeLine("do {", null, 3, null);
    sc.addCodeLine("i++;", null, 4, null);
    sc.addCodeLine("i = i % n;", null, 4, null);
    sc.addCodeLine("} while (array[i] == true);", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("dead++;", null, 2, null);
    sc.addCodeLine("array[i] = true;", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);
  }

  public void showDescription() {
    desc = lang.newSourceCode(new Offset(0, 0, "hRect",
        AnimalScript.DIRECTION_SW), "description", null, sourceCode);
    desc.addCodeLine("Das Josephus-Problem", null, 0, null);
    desc.addCodeLine(
        "oder die Josephus-Permutation ist ein theoretisches Problem aus der Informatik oder Mathematik. ",
        null, 2, null);
    desc.addCodeLine(
        "Es werden n nummerierte Objekte im Kreis angeordnet, dann wird beginnend ",
        null, 2, null);
    desc.addCodeLine(
        "mit der Nummer s, jedes s-te Objekt entfernt, wobei der Kreis immer wieder ",
        null, 2, null);
    desc.addCodeLine(
        "geschlossen wird. Die Reihenfolge der entfernten Objekte wird als Josephus-Permutation ",
        null, 2, null);
    desc.addCodeLine(
        "bezeichnet. Ziel dieses Problems ist es, bei gegebenem n und s, das letzte Objekt der ",
        null, 2, null);
    desc.addCodeLine("Permutation zu bestimmen.", null, 2, null);
    lang.nextStep("Beschreibung");
  }

  public void showFinish() {
    finish = lang.newSourceCode(new Offset(0, 0, "hRect",
        AnimalScript.DIRECTION_SW), "finish", null, sourceCode);
    finish
        .addCodeLine(
            "Der Algorithmus löst das Josephus-Problem, d.h. es wird der Soldat bestimmt ",
            null, 0, null);
    finish.addCodeLine(
        "der als letztes erschossen werden würde. Es wird also die", null, 0,
        null);
    finish
        .addCodeLine(
            "Stelle >> i << bestimmt, wo sich Flavius Josephus hinstellen muesste, ",
            null, 0, null);
    finish
        .addCodeLine(
            "um als letzter ueberlebender sich den Roemern ergeben zu koennen, ohne dass",
            null, 0, null);
    finish.addCodeLine("einer seiner Kameraden noch lebt.", null, 0, null);
    finish
        .addCodeLine(
            "Der Abbruch des Algorithmus ist weniger effektiv, da das Array noch k-mal ",
            null, 0, null);
    finish.addCodeLine("durchlaufen werden muss und erst dann abbricht, ",
        null, 0, null);
    finish.addCodeLine(
        "wenn auch der letzte Soldat als tot markiert worden ist.", null, 0,
        null);
    lang.nextStep();
    lang.newText(new Offset(0, 10, "finish", AnimalScript.DIRECTION_SW),
        "Angelehnt an Wikipedia", "wiki", null, normalText);
    lang.newText(new Offset(0, 3, "wiki", AnimalScript.DIRECTION_SW),
        "http://de.wikipedia.org/wiki/Josephus-Problem", "wiki2", null,
        normalText);
    lang.nextStep("Zusammenfassung");
  }

}