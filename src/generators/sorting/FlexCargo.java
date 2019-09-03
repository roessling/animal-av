package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntArray;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class FlexCargo implements Generator {
  private Language             lang;
  private int                  maxLen;
  private int[]                zug;
  private int[]                standardZug = { 4, 2, 5, 1, 6, 3 };
  private IntArray             arZug;
  private SourceCode           source;
  private SourceCodeProperties sourceProp;
  // private Text header;
  private TextProperties       headerProp;
  private Text                 explain;
  private TextProperties       explainProp;
  private IntMatrix            gleisMatrix;
  private MatrixProperties     matrixProps;
  private ArrayProperties      arrayProps;
  private RectProperties       backRectProps;
  private Rect                 backRect;
  private SourceCodeProperties introProp;
  private SourceCode           intro, fadeout;

  public void init() {
    lang = new AnimalScript("FlexCargoRail", "Patrick Hörmann, Jonas Kellert",
        800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    maxLen = (Integer) primitives
        .get("maximale Gleislänge (Anzahl Elemente pro temp. Speicher, 0 für unbegrenzt)");
    zug = (int[]) primitives.get("Wagenfolge (unsortierte Liste)");
    if (maxLen < 0)
      maxLen = 0;
    explainProp = new TextProperties();
    explainProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(null,
        Font.PLAIN, 14));
    explainProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        new Color(0, 0, 128));
    explain = lang.newText(new Coordinates(400, 100), "", "explain", null,
        explainProp);
    for (int i = 0; i < zug.length; i++)
      if (zug[i] <= 0) {
        zug = standardZug;
        explain.setText(
            "Verwende Standardzug wegen ungültiger Wagen (kleiner 1)", null,
            null);
        break;
      }
    setAnimation();
    sort(zug, maxLen);
    // nachdem Ausführen des Algorithmus beende mit letzter Folie
    lang.nextStep("Schlussbemerkung");
    source.hide();
    explain.setText("", null, null);
    fadeout = lang.newSourceCode(new Coordinates(50, 100), "fadeout", null,
        introProp);
    fadeout.addCodeLine(
        "Im Gegensatz zur herkömlichen Waggonsortierung mithilfe von", null, 0,
        null);
    fadeout.addCodeLine(
        "Ablaufberganlagen ist dies ein sehr einfaches Verfahren,", null, 0,
        null);
    fadeout.addCodeLine(
        "wobei in der Realität natürlich die Kapazität auf dem", null, 0, null);
    fadeout.addCodeLine("Gleisfeld beschränkt ist.", null, 0, null);
    fadeout.addCodeLine("", null, 0, null);
    fadeout.addCodeLine(
        "Auch wenn Speicher nicht unbegrenzt verfügbar ist, ist diese", null,
        0, null);
    fadeout.addCodeLine(
        "Darstellung eher an Einfachheit als an Speichereffizienz", null, 0,
        null);
    fadeout.addCodeLine("orientiert, was sich aber mit Listen anstelle von",
        null, 0, null);
    fadeout.addCodeLine("Arrays leicht ändern lässt.", null, 0, null);
    fadeout.addCodeLine("", null, 0, null);
    fadeout.addCodeLine("Anders sieht es mit der Laufzeit aus:", null, 0, null);
    fadeout.addCodeLine(
        "Diese Variante liegt im worst case in O(n^2), womit der", null, 0,
        null);
    fadeout.addCodeLine("normale MergeSort mit O(n*log n) effizienter ist.",
        null, 0, null);
    fadeout.addCodeLine("In der Anwendung des Schienengüterverkehrs dient",
        null, 0, null);
    fadeout.addCodeLine(
        "er jedoch eher der Sortierung überschaubar langer Züge,", null, 0,
        null);
    fadeout.addCodeLine("zumal er - und nur darauf kommt es dabei an -", null,
        0, null);
    fadeout.addCodeLine("in polynomieller Laufzeit liegt.", null, 0, null);
    fadeout.addCodeLine("", null, 0, null);
    fadeout.addCodeLine("Nicht zuletzt ist diese praxisbezogene Darstellung",
        null, 0, null);
    fadeout.addCodeLine("sicher auch eine didaktisch schöne zur Erlernung",
        null, 0, null);
    fadeout.addCodeLine("eines Sortieralgorithmus, oder?", null, 0, null);

    return lang.toString();
  }

  private void setAnimation() {
    headerProp = new TextProperties();
    headerProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(null,
        Font.BOLD, 24));
    lang.newText(new Coordinates(20, 30), "FlexCargoRail", "header", null,
        headerProp);
    backRectProps = new RectProperties();
    backRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    backRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);

    backRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    backRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    backRect = lang.newRect(new Offset(-5, -5, "header", "NW"), new Offset(5,
        5, "header", "SE"), "orange", null, backRectProps);
    backRect.show();

    introProp = new SourceCodeProperties();
    introProp.set(AnimationPropertiesKeys.SIZE_PROPERTY, 14);
    intro = lang.newSourceCode(new Coordinates(20, 100), "intro", null,
        introProp);
    intro.addCodeLine("Ständig kommen Güterzüge mit unsortierten Wagenfolgen",
        null, 0, null);
    intro.addCodeLine(
        "in Rangierbahnhöfen an und müssen dort vor ihrer Weiterfahrt", null,
        0, null);
    intro.addCodeLine(
        "sortiert werden. Dabei steht ein Gleisfeld, auf dem die", null, 0,
        null);
    intro.addCodeLine(
        "einzelnen Waggons zwischengelagert werden, zur Verfügung.", null, 0,
        null);
    intro.addCodeLine(
        "Anschließend wird der Zug wieder sortiert zusammengesetzt.", null, 0,
        null);
    intro.addCodeLine("Können die Waggons - wie beim Modell FlexCargoRail - ",
        null, 0, null);
    intro.addCodeLine("selbstständig fahren, ist dies eine leichtere Aufgabe.",
        null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro.addCodeLine("Gezeigt wird nun an diesem Szenario eine Variante",
        null, 0, null);
    intro.addCodeLine("von MergeSort.", null, 0, null);
    intro.addCodeLine("", null, 0, null);
    lang.nextStep("Initialisierung");

    arrayProps = new ArrayProperties();
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLUE);
    arrayProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.WHITE);
    arZug = lang.newIntArray(new Coordinates(400, 50), zug, "Zug", null,
        arrayProps);
    intro.addCodeLine("Hier fährt nun der unsortierte Zug ein.", null, 0, null);
    intro.addCodeLine("", null, 0, null);

    lang.nextStep();
    intro.addCodeLine("Sortiert wird über dieses Gleisfeld, wobei jede", null,
        0, null);
    if (maxLen == 0) {
      intro.addCodeLine("0 für das Ende eines Gleises steht, das von rechts",
          null, 0, null);
      intro
          .addCodeLine("beliebig viele Waggons aufnehmen kann.", null, 0, null);
    } else {
      intro.addCodeLine("0 für das Ende eines Gleises steht, das von rechts",
          null, 0, null);
      intro.addCodeLine("bis zu " + maxLen + " Waggons aufnehmen kann", null,
          0, null);
    }
    intro.addCodeLine("Auf einem Gleis wird dabei stets aufsteigend", null, 0,
        null);
    intro.addCodeLine("eingefügt, eizufügende Wagen kleinerer Nummern", null,
        0, null);
    intro.addCodeLine("müssen ggf. ein neues Gleis belegen.", null, 0, null);
    intro.addCodeLine("", null, 0, null);
    intro.addCodeLine("Jetzt geht's los mit dem Algorithmus...", null, 0, null);
    matrixProps = new MatrixProperties();
    matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "matrix");
    matrixProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.WHITE);
    matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    matrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.WHITE);
    matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.BLUE);
    matrixProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    matrixProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
    int[][] temp = new int[zug.length][zug.length];
    gleisMatrix = lang.newIntMatrix(new Coordinates(400, 150), temp,
        "gleisMatrix", null, matrixProps);
    gleisMatrix.highlightElemRowRange(0, zug.length - 1, 0, null, null);

    lang.nextStep();
    gleisMatrix.unhighlightElemRowRange(0, zug.length - 1, 0, null, null);
    intro.hide();

    sourceProp = new SourceCodeProperties();
    sourceProp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    sourceProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    sourceProp.set(AnimationPropertiesKeys.SIZE_PROPERTY, 9);
    source = lang.newSourceCode(new Coordinates(10, 80), "SourceCode", null,
        sourceProp);
    source.addCodeLine("public int[] sort(int[] zug, int maxLen){", null, 0,
        null); // 0
    source.addCodeLine("int laenge = zug.length;", null, 1, null);
    source.addCodeLine("int[][] stack;", null, 1, null);
    source.addCodeLine("if(maxLen == 0) stack = new int[laenge][laenge+1];",
        null, 1, null);
    source
        .addCodeLine("else stack = new int[laenge][maxLen+1];", null, 1, null);
    source.addCodeLine("for(int i=0; i<laenge; i++) {", null, 1, null); // 5
    source.addCodeLine("for(int j=0; j<laenge; j++) {", null, 2, null);
    if (maxLen == 0)
      source
          .addCodeLine("if(stack[j][stack[j][0]] <= zug[i]) {", null, 3, null);
    else
      source.addCodeLine(
          "if(stack[j][stack[j][0]] <= zug[i] && frei(maxLen, stack[j][0])) {",
          null, 3, null);
    source.addCodeLine("stack[j][0]++;", null, 4, null);
    source.addCodeLine("stack[j][stack[j][0]] = zug [i];", null, 4, null);
    source.addCodeLine("break;", null, 4, null); // 10
    source.addCodeLine("}", null, 3, null);
    source.addCodeLine("}", null, 2, null);
    source.addCodeLine("}", null, 1, null);
    source.addCodeLine("for(int i=laenge-1; i>=0; i--) {", null, 1, null);
    source.addCodeLine("int pos = 0, max = 0;", null, 2, null); // 15
    source.addCodeLine("for(int j=0; j<laenge; j++) {", null, 2, null);
    source.addCodeLine("if(stack[j][stack[j][0]] > max) {", null, 3, null);
    source.addCodeLine("max = stack[j][stack[j][0]];", null, 4, null);
    source.addCodeLine("pos = j;", null, 4, null);
    source.addCodeLine("}", null, 3, null); // 20
    source.addCodeLine("}", null, 2, null);
    source.addCodeLine("zug[i] = max;", null, 2, null);
    source.addCodeLine("stack[pos][stack[pos][0]] = 0;", null, 2, null);
    source.addCodeLine("stack[pos][0]--;", null, 2, null);
    source.addCodeLine("}", null, 1, null); // 25
    source.addCodeLine("return zug;", null, 1, null);
    source.addCodeLine("}", null, 0, null);
    // lang.nextStep();
  }

  public int[] sort(int[] zug, int maxLen) {
    int laenge = zug.length;
    int[][] stack;
    if (maxLen == 0) {
      stack = new int[laenge][laenge + 1];
      source.highlight(3);
      source.highlight(2);
      explain.setText("Setze Rangiergleise unbegrenzter Länge", null, null);
    } else {
      stack = new int[laenge][maxLen + 1];
      source.highlight(2);
      source.highlight(4);
      explain.setText("Setze Rangiergleise der Länge maxLen = " + maxLen, null,
          null);
    }
    lang.nextStep("1.Zerlegung");
    source.unhighlight(3);
    source.unhighlight(2);
    source.unhighlight(4);
    // Ankommenden Zug zerlegen
    for (int i = 0; i < laenge; i++) {
      arZug.highlightCell(i, null, null);
      explain.setText("Nimm Waggon " + zug[i] + " (i=" + i + ")", null, null);
      source.highlight(5);
      lang.nextStep();
      source.unhighlight(5);
      // if(Math.random() > 0.85) {
      //
      // }
      for (int j = 0; j < laenge; j++) {
        if (stack[j][stack[j][0]] <= zug[i]) {
          if (frei(maxLen, stack[j][0])) {
            source.highlight(8);
            source.highlight(9);
            explain.setText("Sortiere Waggon " + zug[i] + " auf Gleis j=" + j
                + " an Position " + stack[j][0] + " ein.", null, null);
            gleisMatrix.put(j, stack[j][0], zug[i], null, null);
            gleisMatrix.highlightCell(j, stack[j][0], null, null);
            arZug.highlightElem(i, null, null);
            lang.nextStep();
            source.unhighlight(8);
            source.unhighlight(9);
            stack[j][0]++;
            stack[j][stack[j][0]] = zug[i];
            break;
          } else {
            explain.setText("Auf Gleis j=" + j + " ist kein Platz (max. "
                + maxLen + ") mehr frei.", null, null);
            source.highlight(7);
            lang.nextStep();
            source.unhighlight(7);
          }
        }
      }
      arZug.unhighlightCell(i, null, null);
      arZug.put(i, 0, null, null);
      arZug.highlightElem(i, null, null);
    }
    source.highlight(13);
    explain.setText("Der Zug ist zerlegt und wird wieder zusammengefügt.",
        null, null);
    lang.nextStep("2. Zusammenfügung");
    source.unhighlight(13);
    // Zerlegten Zug neu zusammensetzen
    for (int i = laenge - 1; i >= 0; i--) {
      arZug.highlightCell(i, null, null);
      explain.setText("Wähle den Waggon für Platz i=" + i + "", null, null);
      source.highlight(14);
      source.highlight(15);
      int pos = 0, max = 0;
      for (int j = 0; j < laenge; j++) {
        if (stack[j][stack[j][0]] > max) {
          max = stack[j][stack[j][0]];
          pos = j;
        }
      }
      zug[i] = max;
      stack[pos][stack[pos][0]] = 0;
      stack[pos][0]--;
      arZug.unhighlightElem(i, null, null);
      arZug.put(i, max, null, null);
      arZug.highlightElem(i, null, null);
      lang.nextStep();
      source.unhighlight(14);
      source.unhighlight(15);
      source.highlight(18);
      source.highlight(19);
      source.highlight(22);
      explain.setText("Das ist Waggon max=" + max + " von Gleis pos=" + pos,
          null, null);
      gleisMatrix.put(pos, stack[pos][0], 0, null, null);
      gleisMatrix.unhighlightCell(pos, stack[pos][0], null, null);
      // arZug.put(i, max, null, null);
      arZug.unhighlightElem(i, null, null);
      lang.nextStep();
      source.unhighlight(18);
      source.unhighlight(19);
      source.unhighlight(22);
      arZug.unhighlightCell(i, null, null);
    }
    explain.setText("Fertig ist der sortierte Zug ✔", null, null);
    source.highlight(26);
    return zug;
  }

  private boolean frei(int maxLen, int len) {
    if (maxLen == 0)
      return true;
    if (len < maxLen)
      return true;
    return false;
  }

  public String getName() {
    return "FlexCargoRail";
  }

  public String getAlgorithmName() {
    return "Merge Sort";
  }

  public String getAnimationAuthor() {
    return "Patrick Hörmann, Jonas Kellert";
  }

  public String getDescription() {
    return "Dieser Algorithmus ist sehr einfache eine Variante von Merge-Sort"
        + "\n"
        + "und anhand eines praktischen Beispiels erklärt:"
        + "\n"
        + "Ein Zug aus selbstständig fahrenden Güterwaggons kann hiermit zerlegt"
        + "\n" + "und in richtiger Wagenfolge wieder zusammengesetzt werden.";
  }

  public String getCodeExample() {
    return "public int[] sort(int[] zug, int maxLen){"
        + "\n"
        + "    	int laenge = zug.length;"
        + "\n"
        + "    	int[][] stack;"
        + "\n"
        + "    	if(maxLen == 0) stack = new int[laenge][laenge+1];"
        + "\n"
        + "    	else stack = new int[laenge][maxLen+1];"
        + "\n"
        + "//    	Ankommenden Zug zerlegen"
        + "\n"
        + "    	for(int i=0; i<laenge; i++) {"
        + "\n"
        + "    		for(int j=0; j<laenge; j++){"
        + "\n"
        + "    			if(stack[j][stack[j][0]] <= zug[i] && frei(maxLen, stack[j][0])){"
        + "\n" + "    				stack[j][0]++;" + "\n"
        + "    				stack[j][stack[j][0]] = zug [i];" + "\n" + "    				break;"
        + "\n" + "    			}" + "\n" + "    		}" + "\n" + "    	}" + "\n"
        + "//    	Zerlegten Zug neu zusammensetzen" + "\n"
        + "    	for(int i=laenge-1; i>=0; i--){" + "\n"
        + "    		int pos = 0, max = 0;" + "\n"
        + "    		for(int j=0; j<laenge; j++){" + "\n"
        + "    			if(stack[j][stack[j][0]] > max){" + "\n"
        + "    				max = stack[j][stack[j][0]];" + "\n" + "    				pos = j;"
        + "\n" + "    			}" + "\n" + "    		}" + "\n" + "    		zug[i] = max;"
        + "\n" + "    		stack[pos][stack[pos][0]] = 0;" + "\n"
        + "    		stack[pos][0]--;" + "\n" + "    	}" + "\n"
        + "    	return zug;" + "\n" + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public static void main(String[] args) {
    Hashtable<String, Object> primitives = new Hashtable<String, Object>();
    FlexCargo testzug = new FlexCargo();
    int maxLen = 0;
    int[] zug = { 4, 2, 5, 1, 6, 3 };
    primitives
        .put(
            "maximale Gleislänge (Anzahl Elemente pro temp. Speicher, 0 für unbegrenzt)",
            maxLen);
    primitives.put("Wagenfolge (unsortierte Liste)", zug);
    testzug.init();
    String output = testzug.generate(null, primitives);
    System.out.println(output);
  }

}