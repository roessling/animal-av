package generators.sorting;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.updater.ArrayMarkerUpdater;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class SimpleSortMAKT extends AnnotatedAlgorithm implements Generator {

  protected Language            lang;
  private ArrayProperties       arrayProps;
  private ArrayMarkerProperties ami, amj;
  private SourceCode            sc;
  private IntArray              array;
  private SourceCodeProperties  scProps;
  int[]                         original      = new int[] { 11, 5, 2, 0, 4, 98,
      1, 3, 99, 34                           };
  private ArrayMarker           i, j;
  private Timing                defaultTiming = new TicksTiming(30);
  private boolean               initSort      = false;
  private String                comp          = "Compares";
  private String                assi          = "Assignments";
  private ArrayMarkerUpdater    amuI, amuJ;

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    localInit();
    // parse();
    if (primitives.get("UserArray") != null) {
      arrayProps = (ArrayProperties) props.getPropertiesByName("arrayPro");
      ami = (ArrayMarkerProperties) props.getPropertiesByName("iMarker");
      amj = (ArrayMarkerProperties) props.getPropertiesByName("jMarker");
      original = (int[]) primitives.get("UserArray");
      scProps = (SourceCodeProperties) props
          .getPropertiesByName("sourceCodeProp");

    } else {

      arrayProps = new ArrayProperties();
      arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.RED);
      arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.CYAN);

      ami = new ArrayMarkerProperties();
      ami.setName("iMarker");
      ami.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
      ami.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");

      amj = new ArrayMarkerProperties();
      amj.setName("jMarker");
      amj.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
      amj.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");

      scProps = new SourceCodeProperties();
      scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
      scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
          Font.PLAIN, 12));
      scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
      scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    }

    // Ueberschrift

    this.array = lang.newIntArray(new Coordinates(20, 100), original, "array",
        null, arrayProps);

    algoanim.primitives.Text header = lang.newText(new Coordinates(20, 10),
        "Simple Sort", "Simple Sort", null);
    header.setFont(new Font("SansSerif", 1, 22), null, null);

    sc = lang.newSourceCode(new Coordinates(40, 140), "sourceCode", null,
        scProps);
    sc.addCodeLine("public void simpleSort(int[] data) {", null, 0, null);
    sc.addCodeLine("int i, j;", null, 1, null);
    sc.addCodeLine("for ( i = 0; i <= data.length ; i++) {", null, 1, null);
    sc.addCodeLine("for ( j = i + 1; j < data.length; j++) {", null, 2, null);
    sc.addCodeLine("if (data[i] > data[j])", null, 3, null);
    sc.addCodeLine("swap(data, i, j);", null, 4, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);

    i = lang.newArrayMarker(array, 0, "i", new algoanim.util.Hidden(), ami);
    j = lang.newArrayMarker(array, i.getPosition() + 1, "j",
        new algoanim.util.Hidden(), amj);

    simpleSort(original);

    return lang.toString();
  }

  public SimpleSortMAKT(Language l) {

    lang = l;
    lang.setStepMode(true);
  }

  public SimpleSortMAKT() {
  }

  public void init() {
    super.init();
    lang = new AnimalScript("SimpleSort", "Muhammed Arour und Katja Tenenbaum",
        640, 480);
    lang.setStepMode(true);
    // parse();
  }

  public void localInit() {
    vars = lang.newVariables();
    if (!this.initSort) {
      this.initSort = true;
      // vars.declare("int", "i", String.valueOf(i));
      // vars.declare("int", "j", String.valueOf(j));
      vars.declare("int", "m", String.valueOf(0));
      vars.declare("int", "Rekursion", "0");
      vars.declare("int", comp, String.valueOf(0));
      vars.declare("int", assi, String.valueOf(0));
      vars.declare("int", "compareOperation", String.valueOf(0));
    }
    // ------------------

    // annotations = new HashMap<String, Vector<Annotation>>();

    Text text = lang.newText(new Coordinates(300, 20), "...", "complexity",
        null);
    TextUpdater tu = new TextUpdater(text);
    tu.addToken("Compares: ");
    tu.addToken(vars.getVariable(comp));
    tu.addToken(" - Assignments: ");
    tu.addToken(vars.getVariable(assi));
    tu.update();

    parse();
  }

  public void simpleSort(int[] inputArray) {

    exec("header");
    sc.highlight(0);
    lang.nextStep();
    // -------------------
    exec("vars_marker");
    ArrayMarker amuzi = lang.newArrayMarker(array, 0, "amui", null, ami);
    amuI = new ArrayMarkerUpdater(amuzi, null, null, 0);
    amuI.setVariable(vars.getVariable("i"));
    ArrayMarker amuzj = lang.newArrayMarker(array, 0, "amuj", null, ami);
    amuJ = new ArrayMarkerUpdater(amuzj, null, null, 0);
    amuJ.setVariable(vars.getVariable("j"));
    sc.toggleHighlight(0, 1);
    i.show();
    j.show();
    lang.nextStep();
    // -------------------
    System.err.println("exec iForInit");
    exec("iForInit");
    System.err.println("done exec iForInit");
    // vars.set("i", "0");
    sc.toggleHighlight(1, 2);

    // for (i.move(0, null, null); i.getPosition() < array.getLength(); i
    // .increment(null, defaultTiming)) {

    while (Integer.parseInt(vars.get("i")) < array.getLength()) {
      exec("oForComp");
      vars.set("i", String.valueOf(array.getLength()));
      lang.nextStep();
      j.show();
      exec("jForInit");
      // hier brauche ich j=i+1 und ich dachte das geht einfach so dass ich den
      // wert von i+1 in einer Variable m speichere und
      // dann setze ich mit vars.set....j = m wobei m=i+1 .. ich weiß es nicht
      // ob das funktioniert
      // ich kanns leider nicht testen
      // ich habe die AnnotatedBubbleSort klasse und die XML-Datei mit
      // Animal-2.3.. gebunden und auch
      // das nötige in DummyGenerator gemacht und trotzdem funktioniert es nicht

      int m = Integer.parseInt(vars.get("i")) + 1;
      vars.set("j", String.valueOf(m));
      sc.toggleHighlight(2, 3);
      exec("jForComp");
      // for (j.move(i.getPosition() + 1, null, null); j.getPosition() < array
      // .getLength(); j.increment(null, defaultTiming)) {
      while (Integer.parseInt(vars.get("j")) < array.getLength()) {
        lang.nextStep();
        sc.toggleHighlight(3, 4);

        lang.nextStep();
        exec("if");
        if (array.getData(i.getPosition()) > array.getData(j.getPosition())) {
          sc.toggleHighlight(4, 5);
          exec("swap");
          lang.nextStep();

          array.swap(i.getPosition(), j.getPosition(), null, defaultTiming);
          lang.nextStep();

        }
        sc.unhighlight(4);
        sc.highlight(3);
        sc.unhighlight(5);
        exec("jForInc");
        lang.nextStep();

      }
      exec("iForInc");
      j.hide();
      array.highlightCell(i.getPosition(), null, null);
      array.highlightCell(Integer.parseInt(vars.get("i")), null, null);
    }
  }

  @Override
  public String getAlgorithmName() {
    return "Simple Sort";
  }

  @Override
  public String getAnimationAuthor() {
    return "Muhammed Arour, Katja Tenenbaum";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    StringBuffer desc = new StringBuffer();
    desc.append("Simplesort ist ein stabiles in-place Sortierverfahren")
        .append("\n");
    desc.append("In seiner einfachsten Form hat Simplesort für ein Array der Länge (n) in der Landau-Notation einen Zeit-Aufwand von O(n^2) \n");
    desc.append("Simplesort zeichnet sich durch einen besonders einfachen Algorithmus aus. \n");
    desc.append("Die intuitive Idee hinter Simplesort ist, dass man die Positionen im zu sortierenden Arrays nacheinander betrachtet und das jeweils passende Element einsortiert.");
    return desc.toString();
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  @Override
  public String getName() {
    return "SimpleSort";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  // @Override
  // public String getCodeExample() {
  // StringBuffer sb = new StringBuffer();
  // sb.append("  public void simpleSort(int[] data) { \n");
  // sb.append("   int i, j; \n");
  // sb.append("    for (i = 0; i &lt; data.length ; i++) { \n");
  // sb.append("      for (j = i + 1; j &ge; data.length; j++) { \n");
  // sb.append("        if (data[i] &gt; data[j]) \n");
  // sb.append("          swap(data, i, j); \n");
  // sb.append("      } \n");
  // sb.append("    }");
  // sb.append("  }");
  // return sb.toString();
  // }

  @Override
  public String getAnnotatedSrc() {
    return "int[] sort(int[] arr) {				@label(\"header\")\n"
        + "	int i, j;						@label(\"vars_marker\") @declare(\"int\", \"i\") @declare(\"int\", \"j\")\n"
        + "	for(i = 0;						@label(\"iForInit\") @set(\"i\" , \"0\") @inc(\""
        + assi
        + "\")\n"
        + "           i < data.length;		@label(\"iForComp\") @continue @inc(\""
        + comp
        + "\")\n"
        + "                    i++) {		@label(\"iForInc\") @continue @inc(\"i\") @inc(\""
        + assi
        + "\")\n"
        // ich weiß es nicht ob das hier mit @set(\"j\", \"i+1\") geht oder
        // sollte ich das oben im kode.
        // oder sollte ich das lieber mit @eval machen.
        + "		for(j = i + 1;				@label(\"jForInit\") @set(\"j\", \"i+1\") @inc(\""
        + assi
        + "\")\n"
        + "       j < data.length;			@label(\"jForComp\") @continue @inc(\""
        + comp
        + "\")\n"
        + "         j++) {					@label(\"jForInc\") @continue @inc(\"j\") @inc(\""
        + assi
        + "\")\n"
        + "			if(data[i] > data[j]) 	@label(\"if\") @inc(\""
        + comp
        + "\")\n"
        + "				swap(data, i, j);	@label(\"swap\") @inc(\""
        + assi
        + "\") @inc(\""
        + assi
        + "\") @inc(\""
        + assi
        + "\")\n"
        + "		         }					@label(\"iForEnd\")\n"
        + "		}							@label(\"jForEnd\")\n" + "}								@label(\"end\")\n";
  }

}