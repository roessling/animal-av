package generators.maths;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Rect;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
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

public class ErweiterterEuklid2 extends AnnotatedAlgorithm implements Generator {

  // Properties
  private ArrayProperties       arrayProperties;
  private ArrayMarkerProperties ama;
  private ArrayMarkerProperties amb;
  private ArrayMarkerProperties amx0;
  private ArrayMarkerProperties amx1;
  private ArrayMarkerProperties amy0;
  private ArrayMarkerProperties amy1;
  private ArrayMarkerProperties amgh;

  // Marker
  private ArrayMarker           ar;
  private ArrayMarker           br;
  private ArrayMarker           x0;
  private ArrayMarker           x1;
  private ArrayMarker           y0;
  private ArrayMarker           y1;
  private ArrayMarker           gh;

  // Arrays
  private StringArray           r;
  private StringArray           q;
  private StringArray           x;
  private StringArray           y;

  private Timing                defaultTiming;
  private Text                  header;

  // private Rect headerBackground;

  public ErweiterterEuklid2() {
    ama = new ArrayMarkerProperties();
    ama.set(AnimationPropertiesKeys.LABEL_PROPERTY, "a");
    amb = new ArrayMarkerProperties();
    amb.set(AnimationPropertiesKeys.LABEL_PROPERTY, "b");
    amx0 = new ArrayMarkerProperties();
    amx0.set(AnimationPropertiesKeys.LABEL_PROPERTY, "x0");
    amx1 = new ArrayMarkerProperties();
    amx1.set(AnimationPropertiesKeys.LABEL_PROPERTY, "x1");
    amy0 = new ArrayMarkerProperties();
    amy0.set(AnimationPropertiesKeys.LABEL_PROPERTY, "y0");
    amy1 = new ArrayMarkerProperties();
    amy1.set(AnimationPropertiesKeys.LABEL_PROPERTY, "y1");
    amgh = new ArrayMarkerProperties();
    amgh.set(AnimationPropertiesKeys.LABEL_PROPERTY, "q");

    arrayProperties = new ArrayProperties();
    arrayProperties.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
        Color.RED);
    arrayProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    arrayProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.YELLOW);
    defaultTiming = new TicksTiming(100);
  }

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    init();
    int a = (Integer) arg1.get("a");
    int b = (Integer) arg1.get("b");
    initMatrix(a, b);
    initArrayMarker();
    xeuclid(a, b);
    return lang.toString();
  }

  public void init() {
    super.init();
    header = generateHeader();
    generateHeaderBackground();
    SourceCodeProperties props = new SourceCodeProperties();
    props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
    props.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);
    sourceCode = lang.newSourceCode(new Offset(30, 0, header,
        AnimalScript.DIRECTION_SW), "sumupCode", null, props);
    parse();
  }

  /**
   * Generiert die Ueberschrift fuer die Darstellung des Erweiterten Euklid
   * 
   * @return Text header;
   */
  private Text generateHeader() {
    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    textProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    return lang
        .newText(
            new Coordinates(20, 30),
            "Erweiterter Euklidischer Algorithmus von Tuba Goezel und Johannes Born",
            "header", null, textProperties);
  }

  /**
   * Generiert den Hintergrund fuer die Ueberschrift
   * 
   * @return headerBackground
   */
  private Rect generateHeaderBackground() {
    RectProperties rectProperties = new RectProperties();
    rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
    rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    return lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, header, AnimalScript.DIRECTION_SE),
        "HeaderBackground", null, rectProperties);
  }

  private void initArrayMarker() {
    ar = lang.newArrayMarker(r, 1, "a", null, ama);
    ar.hide();
    br = lang.newArrayMarker(r, 2, "b", null, amb);
    br.hide();
    x0 = lang.newArrayMarker(x, 1, "x0", null, amx0);
    x0.hide();
    x1 = lang.newArrayMarker(x, 2, "x1", null, amx1);
    x1.hide();
    y0 = lang.newArrayMarker(y, 1, "y0", null, amy0);
    y0.hide();
    y1 = lang.newArrayMarker(y, 2, "y1", null, amy1);
    y1.hide();
    gh = lang.newArrayMarker(q, 2, "gh", null, amgh);
    gh.hide();
  }

  private void initMatrix(int a, int b) {
    int length = calculateLength(a, b) + 2;

    // r initialisieren
    String[] rData = new String[length];
    rData[0] = "r";
    for (int i = 1; i < length; i++) {
      rData[i] = "\t";
    }
    r = lang.newStringArray(new Offset(50, 80, sourceCode,
        AnimalScript.DIRECTION_NE), rData, "r", null, arrayProperties);

    // q initialisieren
    String[] qData = new String[length];
    qData[0] = "q";
    for (int i = 1; i < length; i++) {
      qData[i] = "\t";
    }
    q = lang.newStringArray(new Offset(0, 80, r, AnimalScript.DIRECTION_SW),
        qData, "r", null, arrayProperties);

    // x initialisieren
    String[] xData = new String[length];
    xData[0] = "x";
    for (int i = 1; i < length; i++) {
      xData[i] = "\t";
    }
    x = lang.newStringArray(new Offset(0, 80, q, AnimalScript.DIRECTION_SW),
        xData, "r", null, arrayProperties);

    // y initialisieren
    String[] yData = new String[length];
    yData[0] = "y";
    for (int i = 1; i < length; i++) {
      yData[i] = "";
    }
    y = lang.newStringArray(new Offset(0, 80, x, AnimalScript.DIRECTION_SW),
        yData, "r", null, arrayProperties);
  }

  /**
   * Calculates the length of the Matrix to display the algorithm
   * 
   * @param a
   *          - scalar a from ax + by = gcd(a, b)
   * @param b
   *          - scalar a from ax + by = gcd(a, b)
   * @return length of the matrix
   */
  public int calculateLength(int a, int b) {
    int length = 1;
    int a2 = a, b2 = b;
    while (b2 != 0) {
      int h = a2 % b2;
      a2 = b2;
      b2 = h;
      length++;
    }
    return length;
  }

  /**
   * Main Algorithm to calculate x, y and gcd whith the scalar a and b for the
   * equation ax+by=gcd(a,b)
   * 
   * @param a
   *          - scalar
   * @param b
   *          - scalar
   */
  public void xeuclid(int a, int b) {
    exec("header");
    lang.nextStep();
    int a2 = a;
    vars.set("a", String.valueOf(a2));
    ar.show(defaultTiming);
    r.put(1, String.valueOf(a2), null, defaultTiming);
    r.swap(1, 1, null, null);
    lang.nextStep();
    ar.hide(defaultTiming);

    int b2 = b;
    r.put(2, String.valueOf(b2), null, defaultTiming);
    vars.set("b", String.valueOf(b2));
    br.show(defaultTiming);
    lang.nextStep();
    br.hide(defaultTiming);

    int q, r, xtmp, ytmp;
    exec("init");
    lang.nextStep();
    int sign = 1;
    exec("init_sign");
    lang.nextStep();
    int x0 = 1;
    this.x.put(1, String.valueOf(x0), null, defaultTiming);
    this.x.highlightCell(1, null, defaultTiming);
    this.x0.show(defaultTiming);
    exec("init_x0");
    lang.nextStep();
    this.x0.hide(defaultTiming);

    int x1 = 0;
    this.x.unhighlightCell(1, null, defaultTiming);
    this.x.put(2, String.valueOf(x1), null, defaultTiming);
    this.x.highlightCell(2, null, defaultTiming);
    exec("init_x1");
    this.x1.show(defaultTiming);
    lang.nextStep();
    this.x1.hide(defaultTiming);

    this.x.unhighlightCell(2, null, defaultTiming);
    int y0 = 0;
    this.y.put(1, String.valueOf(y0), null, defaultTiming);
    this.y.highlightCell(1, null, defaultTiming);
    this.y0.show(defaultTiming);
    exec("init_y0");
    lang.nextStep();
    this.y0.hide(defaultTiming);

    int y1 = 1;
    this.y.unhighlightCell(1, null, defaultTiming);
    this.y.put(2, String.valueOf(y1), null, defaultTiming);
    this.y.highlightCell(2, null, defaultTiming);
    this.y1.show(defaultTiming);
    exec("init_y1");
    lang.nextStep();
    this.y1.hide(defaultTiming);

    this.y.unhighlightCell(2, null, defaultTiming);
    int counter = 2;
    while (b2 != 0) {
      exec("while");
      lang.nextStep();
      this.r.highlightCell(counter + 1, null, defaultTiming);
      this.ar.show(defaultTiming);
      this.br.show(defaultTiming);
      r = a2 % b2;
      this.r.put(counter + 1, String.valueOf(r), defaultTiming, defaultTiming);
      vars.set("r", String.valueOf(r));
      exec("set_r");
      lang.nextStep();
      this.r.unhighlightCell(counter + 1, null, defaultTiming);

      this.q.highlightCell(counter, null, defaultTiming);
      q = a2 / b2;
      this.q.put(counter, String.valueOf(q), defaultTiming, defaultTiming);
      exec("set_q");
      lang.nextStep();
      this.q.unhighlightCell(counter, null, defaultTiming);

      ar.move(counter, null, defaultTiming);
      a2 = b2;
      exec("set_a");
      lang.nextStep();

      b2 = r;
      br.move(counter + 1, null, defaultTiming);
      exec("set_b");
      lang.nextStep();
      this.ar.hide(defaultTiming);
      this.br.hide(defaultTiming);

      this.x1.show(defaultTiming);
      xtmp = x1;
      exec("set_xtmp");
      lang.nextStep();
      this.x1.hide(defaultTiming);

      this.y1.show(defaultTiming);
      ytmp = y1;
      exec("set_ytmp");
      lang.nextStep();
      this.y1.hide(defaultTiming);

      this.gh.show(defaultTiming);
      this.x1.show(defaultTiming);
      this.x0.show(defaultTiming);
      this.x.highlightCell(counter + 1, null, defaultTiming);
      x1 = q * x1 + x0;
      this.x.put(counter + 1, String.valueOf(x1), defaultTiming, defaultTiming);
      exec("set_x1");
      lang.nextStep();
      this.x.unhighlightCell(counter + 1, null, defaultTiming);
      this.x1.hide(defaultTiming);
      this.x0.hide(defaultTiming);
      this.x1.move(counter + 1, defaultTiming, null);

      this.y1.show(defaultTiming);
      this.y0.show(defaultTiming);
      this.y.highlightCell(counter + 1, null, defaultTiming);
      y1 = q * y1 + y0;
      this.y.put(counter + 1, String.valueOf(y1), defaultTiming, defaultTiming);
      exec("set_y1");
      lang.nextStep();
      this.y.unhighlightCell(counter + 1, null, defaultTiming);
      this.y1.hide(defaultTiming);
      this.y0.hide(defaultTiming);
      this.y1.move(counter + 1, defaultTiming, null);
      this.gh.hide(defaultTiming);
      this.gh.move(counter + 1, defaultTiming, null);

      this.x0.show(defaultTiming);
      this.x.highlightCell(counter, null, defaultTiming);
      x0 = xtmp;
      this.x.put(counter, String.valueOf(x0), defaultTiming, defaultTiming);
      exec("set_x0");
      lang.nextStep();
      this.x.unhighlightCell(counter, null, defaultTiming);
      this.x0.move(counter, null, defaultTiming);

      lang.nextStep();
      this.x0.hide(defaultTiming);

      this.y0.show(defaultTiming);
      this.y.highlightCell(counter, null, defaultTiming);
      y0 = ytmp;
      this.y.put(counter, String.valueOf(y0), defaultTiming, defaultTiming);
      exec("set_y0");
      lang.nextStep();
      this.y.unhighlightCell(counter, null, defaultTiming);
      this.y0.move(counter, null, defaultTiming);

      lang.nextStep();
      this.y0.hide(defaultTiming);

      sign = -sign;
      exec("set_sign");
      lang.nextStep();
      counter++;
    }
    exec("endWhile");
    lang.nextStep();
    // int x = sign * x0;
    exec("set_x");
    lang.nextStep();
    // int y = -sign * y0;
    exec("set_y");
    lang.nextStep();
    // int gcd = a2;
    exec("set_gcd");
    lang.nextStep();
  }

  /**
   * Getter und Setter
   */
  @Override
  public String getAnnotatedSrc() {
    return "void xeuclid(int a, int b) { @label(\"header\") @declare(\"int\", \"a\") @declare(\"int\", \"b\")\n"
        + " int q, r, xtmp, ytmp;	@label(\"init\") @declare(\"int\", \"q\")@declare(\"int\", \"r\") @declare(\"int\", \"xtmp\") @declare(\"int\", \"ytmp\")\n"
        + " int sign = 1;			@label(\"init_sign\") @declare(\"int\", \"sign\", \"1\")\n"
        + " int x0 = 1;			@label(\"init_x0\") @declare(\"int\", \"x0\", \"1\")\n"
        + " int x1 = 0;			@label(\"init_x1\") @declare(\"int\", \"x1\", \"0\")\n"
        + " int y0 = 0;			@label(\"init_y0\") @declare(\"int\", \"y0\", \"0\")\n"
        + " int y1 = 1;			@label(\"init_y1\") @declare(\"int\", \"y1\", \"1\")\n"
        + " while(b != 0) {		@label(\"while\")\n"
        + "  r = a % b;			@label(\"set_r\")\n"
        + "  q = a / b;			@label(\"set_q\") @eval(\"q\", \"a/b\")\n"
        + "  a = b;				@label(\"set_a\") @eval(\"a\", \"b\")\n"
        + "  b = r;				@label(\"set_b\") @eval(\"b\", \"r\")\n"
        + "  xtmp = x1;			@label(\"set_xtmp\") @eval(\"xtmp\", \"x1\")\n"
        + "  ytmp = y1;			@label(\"set_ytmp\") @eval(\"ytmp\", \"y1\")\n"
        + "  x1 = q * x1 + x0;	@label(\"set_x1\") @eval(\"x1\", \"(q*x1)+x0\")\n"
        + "  y1 = q * y1 + y0;	@label(\"set_y1\") @eval(\"y1\", \"(q*y1)+y0\")\n"
        + "  x0 = xtmp;			@label(\"set_x0\") @eval(\"x0\", \"xtmp\")\n"
        + "  y0 = ytmp;			@label(\"set_y0\") @eval(\"y0\", \"ytmp\")\n"
        + "  sign = -sign;		@label(\"set_sign\") @eval(\"sign\", \"sign * (0 - 1)\")\n"
        +

        " }						@label(\"endWhile\")\n"
        + " int x = sign * x0; 	@label(\"set_x\") @declare(\"int\", \"x\") @eval(\"x\", \"sign*x0\")\n"
        + " int y = -sign * y0;		@label(\"set_y\") @declare(\"int\", \"y\") @eval(\"y\", \"sign*y0 *(0-1)\")\n"
        + " int gcd = a;				@label(\"set_gcd\") @declare(\"int\", \"gcd\") @eval(\"gcd\", \"a\")\n"
        + "}						@label(\"endFunktion\")\n";
  }

  @Override
  public String getAlgorithmName() {
    return "Erweiterter Euklidscher Algorithmus";
  }

  @Override
  public String getAnimationAuthor() {
    return "Tuba Gözel, Johannes Born";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "Mit dem erweiterten euklidischen Algorithmus berechnet man ein "
        + "x und ein y, sodass die Gleichung ax+by = gcd(a, b) geloest werden kann. Verwendet "
        + "wird er zum Beispiel bei der Schluesselberechnung des RSA-Verschluesselungs-"
        + "Verfahrens. <br />Eingabedaten:" + "<br />- a:" + "<br />- b:";
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
  }

  @Override
  public String getName() {
    return "Erweiterter Euklidscher Algorithmus (Annotation Based)";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }
}