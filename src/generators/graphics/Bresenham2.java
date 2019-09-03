package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PointProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class Bresenham2 implements Generator {
  private Language lang;
  private Color    linie;
  private int      xend;
  private Color    pixel;
  private int      ystart;
  private int      yend;
  private int      xstart;
  private Color    highlight;

  public void init() {
    lang = new AnimalScript("Bresenham [DE]",
        "Michael Rodenberg (Michael.Rodenberg@d120.de)", 1024, 740);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    linie = (Color) primitives.get("linie");
    xend = (Integer) primitives.get("xend");
    pixel = (Color) primitives.get("pixel");
    ystart = (Integer) primitives.get("ystart");
    yend = (Integer) primitives.get("yend");
    xstart = (Integer) primitives.get("xstart");
    highlight = (Color) primitives.get("highlight");

    Color colors[] = { linie, highlight, pixel };

    try {
      bresenham(xstart, ystart, xend, yend, colors);
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }

    return lang.toString();
  }

  public String getName() {
    return "Bresenham [DE]";
  }

  public String getAlgorithmName() {
    return "Bresenham-Algorithmus";
  }

  public String getAnimationAuthor() {
    return "Michael Rodenberg";
  }

  public String getDescription() {
    return "Der Algorithmus von Bresenham ist ein sehr einfacher (und damit effizient zu implementierender) Algorithmus aus der Computergrafik, welcher in einem Raster mit gegebenen Start- und Endpunkt die 'Pixel' einer Linie berechnet.";
  }

  public String getCodeExample() {
    return "dx = xend - xstart" + "\n" + "dy = yend - ystart" + "\n"
        + "x = xstart" + "\n" + "y = ystart" + "\n" + "setPixel(x, y)" + "\n"
        + "if(dy < = dx){" + "\n" + "   // Steigung <= 1" + "\n"
        + "   int fehler = dx/2;" + "\n" + "   while(x < xend){" + "\n"
        + "      x = x+1;" + "\n" + "      fehler = fehler - dy;" + "\n"
        + "      if(fehler < 0){" + "\n" + "         y = y+1;" + "\n"
        + "         fehler = fehler + dx;" + "\n" + "      }" + "\n"
        + "      setPixel(x, y);" + "\n" + "   }" + "\n" + "}" + "\n" + "else{"
        + "\n" + "   // Steigung > 1" + "\n" + "   int fehler = dy/2;" + "\n"
        + "   while(y < yend){" + "\n" + "      y = y+1;" + "\n"
        + "      fehler = fehler - dx;" + "\n" + "      if(fehler < 0){" + "\n"
        + "         x = x+1;" + "\n" + "         fehler = fehler + dy;" + "\n"
        + "      }" + "\n" + "      setPixel(x, y);" + "\n" + "   }" + "\n"
        + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  // Eigener Code
  private int zeit       = 20;
  private int zeitfaktor = 0;
  private int wartezeit  = 4;

  public void bresenham(int xstart, int ystart, int xend, int yend,
      Color colors[]) throws IllegalDirectionException {
    // Einleitung
    zeigeTitel();
    // SourceCode
    SourceCodeProperties sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 12));
    sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLUE);

    SourceCode sourceCode = lang.newSourceCode(new Coordinates(50, 1),
        "sourceCode", null, sourceCodeProps);

    sourceCode.addCodeLine("dx = xend - xstart", null, 0, null); // 0
    sourceCode.addCodeLine("dy = yend - ystart", null, 0, null); // 1
    sourceCode.addCodeLine("x = xstart", null, 0, null); // 2
    sourceCode.addCodeLine("y = ystart", null, 0, null); // 3
    sourceCode.addCodeLine("setPixel(x, y)", null, 0, null); // 4
    sourceCode.addCodeLine("if(dy < = dx){", null, 0, null); // 5
    sourceCode.addCodeLine("// Steigung <= 1", null, 0, null); // 6
    sourceCode.addCodeLine("int fehler = dx/2;", null, 1, null); // 7
    sourceCode.addCodeLine("while(x < xend){", null, 1, null); // 8
    sourceCode.addCodeLine("x = x+1;", null, 2, null); // 9
    sourceCode.addCodeLine("fehler = fehler - dy;", null, 2, null); // 10
    sourceCode.addCodeLine("if(fehler < 0){", null, 2, null); // 11
    sourceCode.addCodeLine("y = y+1;", null, 3, null); // 12
    sourceCode.addCodeLine("fehler = fehler + dx;", null, 3, null); // 13
    sourceCode.addCodeLine("}", null, 2, null); // 14
    sourceCode.addCodeLine("setPixel(x, y);", null, 2, null); // 15
    sourceCode.addCodeLine("}", null, 1, null); // 16
    sourceCode.addCodeLine("}", null, 0, null); // 17
    sourceCode.addCodeLine("else{", null, 0, null); // 18
    sourceCode.addCodeLine("// Steigung > 1", null, 0, null); // 19
    sourceCode.addCodeLine("int fehler = dy/2;", null, 1, null); // 20
    sourceCode.addCodeLine("while(y < yend){", null, 1, null); // 21
    sourceCode.addCodeLine("y = y+1;", null, 2, null); // 22
    sourceCode.addCodeLine("fehler = fehler - dx;", null, 2, null); // 23
    sourceCode.addCodeLine("if(fehler < 0){", null, 2, null); // 24
    sourceCode.addCodeLine("x = x+1;", null, 3, null); // 25
    sourceCode.addCodeLine("fehler = fehler + dy;", null, 3, null); // 26
    sourceCode.addCodeLine("}", null, 2, null); // 27
    sourceCode.addCodeLine("setPixel(x, y);", null, 2, null); // 28
    sourceCode.addCodeLine("}", null, 1, null); // 29
    sourceCode.addCodeLine("}", null, 0, null); // 30

    // Infotext
    SourceCodeProperties einleitungProps = new SourceCodeProperties();
    einleitungProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SanSerif", Font.PLAIN, 20));
    einleitungProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    einleitungProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLUE);

    SourceCode einleitung = lang.newSourceCode(new Coordinates(300, 200),
        "sourceCode", null, einleitungProps);

    einleitung.addCodeLine(
        "Der Algorithmus von Bresenham ist ein sehr einfacher", null, 0, null);
    einleitung.addCodeLine("(und damit effizient zu implementierender)", null,
        0, null);
    einleitung
        .addCodeLine("Algorithmus aus der Computergrafik,", null, 0, null);
    einleitung.addCodeLine(
        "welcher in einem Raster mit gegebenen Start- und Endpunkt", null, 0,
        null);
    einleitung.addCodeLine("die 'Pixel' einer Linie berechnet.", null, 0, null);

    lang.nextStep();
    einleitung.hide();

    int maxFelder = 14;
    int size = 30;

    // Gitter
    PointProperties pointProps = new PointProperties();
    pointProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    lang.newPoint(new Coordinates(300, 470), "gitter_benchmark", null,
        pointProps);

    RectProperties felderProps = new RectProperties();
    felderProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    felderProps.set(AnimationPropertiesKeys.FILL_PROPERTY, colors[2]);
    felderProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    felderProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
    felderProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);

    TextProperties beschriftungProps = new TextProperties();
    beschriftungProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 16));
    beschriftungProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    beschriftungProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    Rect felder[][] = new Rect[maxFelder][maxFelder];
    for (int i = 0; i < (maxFelder); i++) {
      for (int j = 0; j < (maxFelder); j++) {
        felder[i][j] = lang
            .newRect(new Offset(i * size, -j * size, "gitter_benchmark",
                AnimalScript.DIRECTION_E),
                new Offset(((i + 1) * size), -((j + 1) * size),
                    "gitter_benchmark", AnimalScript.DIRECTION_E), "feld_" + i
                    + "_" + j, null, felderProps);
      }
      lang.newText(new Offset((i * size) + 8, 4, "gitter_benchmark",
          AnimalScript.DIRECTION_N), "" + i, "x_beschriftung_" + i, null,
          beschriftungProps);
      lang.newText(new Offset(-20, -(i * size) - 26, "gitter_benchmark",
          AnimalScript.DIRECTION_N), "" + i, "y_beschriftung_" + i, null,
          beschriftungProps);
    }

    // Gitter Linien
    PolylineProperties linieSchwarzProps = new PolylineProperties();
    linieSchwarzProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    linieSchwarzProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    PolylineProperties linieGrauProps = new PolylineProperties();
    linieGrauProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    linieGrauProps
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);

    Node[] grundlinie_horizontal_Node = {
        new Offset(-size, 0, "gitter_benchmark", AnimalScript.DIRECTION_N),
        new Offset(maxFelder * size, 0, "gitter_benchmark",
            AnimalScript.DIRECTION_N) };
    lang.newPolyline(grundlinie_horizontal_Node, "grundlinie_horizontal", null,
        linieSchwarzProps);

    Node[] grundlinie_vertikal_Node = {
        new Offset(0, size, "gitter_benchmark", AnimalScript.DIRECTION_N),
        new Offset(0, -maxFelder * size, "gitter_benchmark",
            AnimalScript.DIRECTION_N) };
    lang.newPolyline(grundlinie_vertikal_Node, "grundlinie_vertikal", null,
        linieSchwarzProps);

    for (int i = 1; i <= maxFelder; i++) {
      Node[] linie_horizontal_Node = {
          new Offset(-size, -i * size, "gitter_benchmark",
              AnimalScript.DIRECTION_N),
          new Offset(maxFelder * size, -i * size, "gitter_benchmark",
              AnimalScript.DIRECTION_N) };
      Node[] linie_vertikal_Node = {
          new Offset(i * size, size, "gitter_benchmark",
              AnimalScript.DIRECTION_N),
          new Offset(i * size, -maxFelder * size, "gitter_benchmark",
              AnimalScript.DIRECTION_N) };
      lang.newPolyline(linie_horizontal_Node, "linie_horizontal", null,
          linieGrauProps);
      lang.newPolyline(linie_vertikal_Node, "linie_vertikal", null,
          linieGrauProps);
    }

    // Zeilen- und Spaltenmarkierungen
    RectProperties markierungProps = new RectProperties();
    markierungProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    markierungProps.set(AnimationPropertiesKeys.FILL_PROPERTY, colors[1]);
    markierungProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[1]);
    markierungProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 20);
    markierungProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);

    Rect zeilen[] = new Rect[maxFelder];
    Rect spalten[] = new Rect[maxFelder];

    for (int i = 0; i < (maxFelder); i++) {
      zeilen[i] = lang.newRect(new Offset(-size, -(i + 1) * size,
          "gitter_benchmark", AnimalScript.DIRECTION_E), new Offset(maxFelder
          * size, -i * size, "gitter_benchmark", AnimalScript.DIRECTION_E),
          "zeile_" + i, null, markierungProps);
      spalten[i] = lang.newRect(new Offset(i * size, -(maxFelder * size),
          "gitter_benchmark", AnimalScript.DIRECTION_E), new Offset((i + 1)
          * size, size, "gitter_benchmark", AnimalScript.DIRECTION_E),
          "spalte_" + i, null, markierungProps);
    }

    TextProperties infoTextProps = new TextProperties();
    infoTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 16));
    infoTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    infoTextProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    RectProperties infoRectProps = new RectProperties();
    infoRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    infoRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
    infoRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    infoRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    TextProperties infoTitelProps = new TextProperties();
    infoTitelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 18));
    infoTitelProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    infoTitelProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    lang.newPoint(new Coordinates(750, 80), "linieInfo_benchmark", null,
        pointProps);
    lang.newRect(new Offset(0, 0, "linieInfo_benchmark",
        AnimalScript.DIRECTION_N), new Offset(300, 80, "linieInfo_benchmark",
        AnimalScript.DIRECTION_N), "variablenRect", null, infoRectProps);
    lang.newText(new Offset(30, 8, "linieInfo_benchmark",
        AnimalScript.DIRECTION_N), "gewünschte Linie", "linieInfoTitel", null,
        infoTitelProps);
    Text linien_startpunkt = lang.newText(new Offset(10, 30,
        "linieInfo_benchmark", AnimalScript.DIRECTION_N), "Startpunkt: ("
        + xstart + ", " + ystart + ")", "linien_startpunkt", null,
        infoTextProps);
    Text linien_endpunkt = lang.newText(new Offset(10, 50,
        "linieInfo_benchmark", AnimalScript.DIRECTION_N), "Endpunkt: (" + xend
        + ", " + yend + ")", "linien_endpunkt", null, infoTextProps);

    // Variablen
    lang.newPoint(new Coordinates(750, 200), "variablen_benchmark", null,
        pointProps);
    lang.newRect(new Offset(0, 0, "variablen_benchmark",
        AnimalScript.DIRECTION_N), new Offset(300, 230, "variablen_benchmark",
        AnimalScript.DIRECTION_N), "variablenRect", null, infoRectProps);
    lang.newText(new Offset(30, 8, "variablen_benchmark",
        AnimalScript.DIRECTION_N), "Variablen", "variablen", null,
        infoTitelProps);
    lang.newText(new Offset(10, 30, "variablen_benchmark",
        AnimalScript.DIRECTION_N), "xstart = " + xstart, "variablen_xstart",
        null, infoTextProps);
    lang.newText(new Offset(10, 50, "variablen_benchmark",
        AnimalScript.DIRECTION_N), "ystart = " + ystart, "variablen_ystart",
        null, infoTextProps);
    lang.newText(new Offset(10, 70, "variablen_benchmark",
        AnimalScript.DIRECTION_N), "xend = " + xend, "variablen_xend", null,
        infoTextProps);
    lang.newText(new Offset(10, 90, "variablen_benchmark",
        AnimalScript.DIRECTION_N), "yend = " + yend, "variablen_yend", null,
        infoTextProps);
    Text variablen_dx = lang
        .newText(new Offset(10, 110, "variablen_benchmark",
            AnimalScript.DIRECTION_N), "dx = ", "variablen_dx", null,
            infoTextProps);
    Text variablen_dy = lang
        .newText(new Offset(10, 130, "variablen_benchmark",
            AnimalScript.DIRECTION_N), "dy = ", "variablen_dy", null,
            infoTextProps);
    Text variablen_x = lang.newText(new Offset(10, 150, "variablen_benchmark",
        AnimalScript.DIRECTION_N), "x = ", "variablen_x", null, infoTextProps);
    Text variablen_y = lang.newText(new Offset(10, 170, "variablen_benchmark",
        AnimalScript.DIRECTION_N), "y = ", "variablen_y", null, infoTextProps);
    Text variablen_fehler = lang.newText(new Offset(10, 190,
        "variablen_benchmark", AnimalScript.DIRECTION_N), "fehler = ",
        "variablen_fehler", null, infoTextProps);

    // Linie
    PolylineProperties linieProps = new PolylineProperties();
    linieProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    linieProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, colors[0]);
    Node[] linienPunkte = {
        new Offset((xstart * size) + (size / 2),
            -((ystart * size) + (size / 2)), "gitter_benchmark",
            AnimalScript.DIRECTION_N),
        new Offset((xstart * size) + (size / 2),
            -((ystart * size) + (size / 2)), "gitter_benchmark",
            AnimalScript.DIRECTION_N) };

    // Linie einzeichnen
    linien_startpunkt.changeColor("color", Color.RED, new TicksTiming(
        zeitfaktor * zeit), null);
    spalten[xstart].show(new TicksTiming(zeitfaktor * zeit));
    zeilen[ystart].show(new TicksTiming(zeitfaktor * zeit));
    zeitfaktor++;
    setPixel(xstart, ystart, felder);
    spalten[xstart].hide(new TicksTiming(zeitfaktor * zeit));
    zeilen[ystart].hide(new TicksTiming(zeitfaktor * zeit));
    linien_startpunkt.changeColor("color", Color.BLACK, new TicksTiming(
        zeitfaktor * zeit), null);
    zeitfaktor++;
    linien_endpunkt.changeColor("color", Color.RED, new TicksTiming(zeitfaktor
        * zeit), null);
    spalten[xend].show(new TicksTiming(zeitfaktor * zeit));
    zeilen[yend].show(new TicksTiming(zeitfaktor * zeit));
    zeitfaktor++;
    setPixel(xend, yend, felder);
    spalten[xend].hide(new TicksTiming(zeitfaktor * zeit));
    zeilen[yend].hide(new TicksTiming(zeitfaktor * zeit));
    linien_endpunkt.changeColor("color", Color.BLACK, new TicksTiming(
        zeitfaktor * zeit), null);
    zeitfaktor++;
    Polyline linie = lang.newPolyline(linienPunkte, "linie", new TicksTiming(
        zeitfaktor * zeit), linieProps);
    linie.moveTo(AnimalScript.DIRECTION_E, "translate #2", new Offset(
        (xend * size) + (size / 2), -((yend * size) + (size / 2)),
        "gitter_benchmark", AnimalScript.DIRECTION_N), new TicksTiming(
        zeitfaktor * zeit), new TicksTiming(3 * zeit));
    zeitfaktor = zeitfaktor + wartezeit;
    felder[xstart][ystart].hide(new TicksTiming(zeitfaktor * zeit));
    felder[xend][yend].hide(new TicksTiming(zeitfaktor * zeit));
    zeitfaktor++;

    lang.nextStep();
    zeitfaktor = 0;

    // ***Algorithmus initialisierung***

    // dx
    int dx = xend - xstart;
    sourceCode.highlight(0, 0, false, new TicksTiming(zeitfaktor * zeit), null);
    variablen_dx.changeColor("color", Color.RED, new TicksTiming(zeitfaktor
        * zeit), null);
    zeitfaktor++;
    variablen_dx.setText("dx = xend - xstart = " + dx, new TicksTiming(
        zeitfaktor * zeit), null);
    zeitfaktor = zeitfaktor + wartezeit;
    variablen_dx
        .setText("dx = " + dx, new TicksTiming(zeitfaktor * zeit), null);
    zeitfaktor++;
    variablen_dx.changeColor("color", Color.BLACK, new TicksTiming(zeitfaktor
        * zeit), null);
    sourceCode.unhighlight(0, 0, false, new TicksTiming(zeitfaktor * zeit),
        null);

    zeitfaktor = zeitfaktor + wartezeit;

    // dy
    int dy = yend - ystart;
    sourceCode.highlight(1, 0, false, new TicksTiming(zeitfaktor * zeit), null);
    variablen_dy.changeColor("color", Color.RED, new TicksTiming(zeitfaktor
        * zeit), null);
    zeitfaktor++;
    variablen_dy.setText("dy = yend - ystart = " + dy, new TicksTiming(
        zeitfaktor * zeit), null);
    zeitfaktor = zeitfaktor + wartezeit;
    variablen_dy
        .setText("dy = " + dy, new TicksTiming(zeitfaktor * zeit), null);
    zeitfaktor++;
    variablen_dy.changeColor("color", Color.BLACK, new TicksTiming(zeitfaktor
        * zeit), null);
    sourceCode.unhighlight(1, 0, false, new TicksTiming(zeitfaktor * zeit),
        null);

    zeitfaktor = zeitfaktor + wartezeit;

    // x
    int x = xstart;
    sourceCode.highlight(2, 0, false, new TicksTiming(zeitfaktor * zeit), null);
    variablen_x.changeColor("color", Color.RED, new TicksTiming(zeitfaktor
        * zeit), null);
    zeitfaktor++;
    variablen_x.setText("x = xstart = " + x,
        new TicksTiming(zeitfaktor * zeit), null);
    spalten[x].show(new TicksTiming(zeitfaktor * zeit));
    zeitfaktor = zeitfaktor + wartezeit;
    variablen_x.setText("x = " + x, new TicksTiming(zeitfaktor * zeit), null);
    zeitfaktor++;
    variablen_x.changeColor("color", Color.BLACK, new TicksTiming(zeitfaktor
        * zeit), null);
    sourceCode.unhighlight(2, 0, false, new TicksTiming(zeitfaktor * zeit),
        null);

    zeitfaktor = zeitfaktor + wartezeit;

    // y
    int y = ystart;
    sourceCode.highlight(3, 0, false, new TicksTiming(zeitfaktor * zeit), null);
    variablen_y.changeColor("color", Color.RED, new TicksTiming(zeitfaktor
        * zeit), null);
    zeitfaktor++;
    variablen_y.setText("y = ystart = " + y,
        new TicksTiming(zeitfaktor * zeit), null);
    zeilen[y].show(new TicksTiming(zeitfaktor * zeit));
    zeitfaktor = zeitfaktor + wartezeit;
    variablen_y.setText("y = " + y, new TicksTiming(zeitfaktor * zeit), null);
    zeitfaktor++;
    variablen_y.changeColor("color", Color.BLACK, new TicksTiming(zeitfaktor
        * zeit), null);
    sourceCode.unhighlight(3, 0, false, new TicksTiming(zeitfaktor * zeit),
        null);

    zeitfaktor = zeitfaktor + wartezeit;

    // Startpixel
    lang.nextStep();
    zeitfaktor = 0;
    sourceCode.highlight(4, 0, false, new TicksTiming(zeitfaktor * zeit), null);
    setPixel(x, y, felder);
    sourceCode.unhighlight(4, 0, false, new TicksTiming(zeitfaktor * zeit),
        null);

    // Bestimmen der Steigung
    lang.nextStep();
    zeitfaktor = 0;
    sourceCode.highlight(5, 0, false, new TicksTiming(zeitfaktor * zeit), null);
    zeitfaktor++;
    if (dy <= dx) { // Steigung <= 1
      sourceCode
          .highlight(6, 0, true, new TicksTiming(zeitfaktor * zeit), null);
      zeitfaktor = zeitfaktor + wartezeit;
      sourceCode.unhighlight(5, 0, false, new TicksTiming(zeitfaktor * zeit),
          null);

      // fehler = dx72
      lang.nextStep();
      zeitfaktor = 0;

      sourceCode.highlight(7, 0, false, new TicksTiming(zeitfaktor * zeit),
          null);
      int fehler = dx / 2;
      variablen_fehler.changeColor("color", Color.RED, new TicksTiming(
          zeitfaktor * zeit), null);
      zeitfaktor++;
      variablen_fehler.setText("fehler = dx/2 = " + fehler, new TicksTiming(
          zeitfaktor * zeit), null);
      zeitfaktor = zeitfaktor + wartezeit;
      variablen_fehler.setText("fehler = " + fehler, new TicksTiming(zeitfaktor
          * zeit), null);
      zeitfaktor++;
      variablen_fehler.changeColor("color", Color.BLACK, new TicksTiming(
          zeitfaktor * zeit), null);
      zeitfaktor++;
      sourceCode.unhighlight(7, 0, false, new TicksTiming(zeitfaktor * zeit),
          null);
      zeitfaktor++;

      while (x < xend) {
        sourceCode.highlight(8, 0, false, new TicksTiming(zeitfaktor * zeit),
            null);
        zeitfaktor = zeitfaktor + wartezeit;
        sourceCode.unhighlight(8, 0, false, new TicksTiming(zeitfaktor * zeit),
            null);

        x++;
        sourceCode.highlight(9, 0, false, new TicksTiming(zeitfaktor * zeit),
            null);
        variablen_x.changeColor("color", Color.RED, new TicksTiming(zeitfaktor
            * zeit), null);
        zeitfaktor++;
        spalten[x - 1].hide(new TicksTiming(zeitfaktor * zeit));
        spalten[x].show(new TicksTiming(zeitfaktor * zeit));
        variablen_x.setText("x = x+1 = " + x,
            new TicksTiming(zeitfaktor * zeit), null);
        zeitfaktor = zeitfaktor + wartezeit;
        variablen_x.setText("x = " + x, new TicksTiming(zeitfaktor * zeit),
            null);
        zeitfaktor++;
        variablen_x.changeColor("color", Color.BLACK, new TicksTiming(
            zeitfaktor * zeit), null);
        zeitfaktor++;
        sourceCode.unhighlight(9, 0, false, new TicksTiming(zeitfaktor * zeit),
            null);
        zeitfaktor++;

        fehler = fehler - dy;
        sourceCode.highlight(10, 0, false, new TicksTiming(zeitfaktor * zeit),
            null);
        variablen_fehler.changeColor("color", Color.RED, new TicksTiming(
            zeitfaktor * zeit), null);
        zeitfaktor++;
        variablen_fehler.setText("fehler = fehler - dy = " + fehler,
            new TicksTiming(zeitfaktor * zeit), null);
        zeitfaktor = zeitfaktor + wartezeit;
        variablen_fehler.setText("fehler = " + fehler, new TicksTiming(
            zeitfaktor * zeit), null);
        zeitfaktor++;
        variablen_fehler.changeColor("color", Color.BLACK, new TicksTiming(
            zeitfaktor * zeit), null);
        zeitfaktor++;
        sourceCode.unhighlight(10, 0, false,
            new TicksTiming(zeitfaktor * zeit), null);
        zeitfaktor++;

        sourceCode.highlight(11, 0, false, new TicksTiming(zeitfaktor * zeit),
            null);
        zeitfaktor = zeitfaktor + wartezeit;
        if (fehler < 0) {

          y++;
          sourceCode.highlight(12, 0, false,
              new TicksTiming(zeitfaktor * zeit), null);
          variablen_y.changeColor("color", Color.RED, new TicksTiming(
              zeitfaktor * zeit), null);
          zeitfaktor++;
          zeilen[y - 1].hide(new TicksTiming(zeitfaktor * zeit));
          zeilen[y].show(new TicksTiming(zeitfaktor * zeit));
          variablen_y.setText("y = y + 1 = " + y, new TicksTiming(zeitfaktor
              * zeit), null);
          zeitfaktor = zeitfaktor + wartezeit;
          variablen_y.setText("y = " + y, new TicksTiming(zeitfaktor * zeit),
              null);
          zeitfaktor++;
          variablen_y.changeColor("color", Color.BLACK, new TicksTiming(
              zeitfaktor * zeit), null);
          zeitfaktor++;
          sourceCode.unhighlight(12, 0, false, new TicksTiming(zeitfaktor
              * zeit), null);
          zeitfaktor++;

          fehler = fehler + dx;
          sourceCode.highlight(13, 0, false,
              new TicksTiming(zeitfaktor * zeit), null);
          variablen_fehler.changeColor("color", Color.RED, new TicksTiming(
              zeitfaktor * zeit), null);
          zeitfaktor++;
          variablen_fehler.setText("fehler = fehler + dx = " + fehler,
              new TicksTiming(zeitfaktor * zeit), null);
          zeitfaktor = zeitfaktor + wartezeit;
          variablen_fehler.setText("fehler = " + fehler, new TicksTiming(
              zeitfaktor * zeit), null);
          zeitfaktor++;
          variablen_fehler.changeColor("color", Color.BLACK, new TicksTiming(
              zeitfaktor * zeit), null);
          zeitfaktor++;
          sourceCode.unhighlight(13, 0, false, new TicksTiming(zeitfaktor
              * zeit), null);
          zeitfaktor++;

        }
        sourceCode.unhighlight(11, 0, false,
            new TicksTiming(zeitfaktor * zeit), null);

        sourceCode.highlight(15, 0, false, new TicksTiming(zeitfaktor * zeit),
            null);
        zeitfaktor++;
        setPixel(x, y, felder);
        sourceCode.unhighlight(15, 0, false,
            new TicksTiming(zeitfaktor * zeit), null);

        lang.nextStep();
        zeitfaktor = 0;
      }
    } else { // Steigung > 1
      sourceCode.highlight(18, 0, false, new TicksTiming(zeitfaktor * zeit),
          null);
      sourceCode.highlight(19, 0, true, new TicksTiming(zeitfaktor * zeit),
          null);
      zeitfaktor = zeitfaktor + wartezeit;
      sourceCode.unhighlight(5, 0, false, new TicksTiming(zeitfaktor * zeit),
          null);
      sourceCode.unhighlight(18, 0, false, new TicksTiming(zeitfaktor * zeit),
          null);

      // fehler = dy/2
      lang.nextStep();
      zeitfaktor = 0;

      sourceCode.highlight(20, 0, false, new TicksTiming(zeitfaktor * zeit),
          null);
      int fehler = dy / 2;
      variablen_fehler.changeColor("color", Color.RED, new TicksTiming(
          zeitfaktor * zeit), null);
      zeitfaktor++;
      variablen_fehler.setText("fehler = dy/2 = " + fehler, new TicksTiming(
          zeitfaktor * zeit), null);
      zeitfaktor = zeitfaktor + wartezeit;
      variablen_fehler.setText("fehler = " + fehler, new TicksTiming(zeitfaktor
          * zeit), null);
      zeitfaktor++;
      variablen_fehler.changeColor("color", Color.BLACK, new TicksTiming(
          zeitfaktor * zeit), null);
      zeitfaktor++;
      sourceCode.unhighlight(20, 0, false, new TicksTiming(zeitfaktor * zeit),
          null);

      while (y < yend) {
        sourceCode.highlight(21, 0, false, new TicksTiming(zeitfaktor * zeit),
            null);
        zeitfaktor = zeitfaktor + wartezeit;
        sourceCode.unhighlight(21, 0, false,
            new TicksTiming(zeitfaktor * zeit), null);

        y++;
        sourceCode.highlight(22, 0, false, new TicksTiming(zeitfaktor * zeit),
            null);
        variablen_y.changeColor("color", Color.RED, new TicksTiming(zeitfaktor
            * zeit), null);
        zeitfaktor++;
        zeilen[y - 1].hide(new TicksTiming(zeitfaktor * zeit));
        zeilen[y].show(new TicksTiming(zeitfaktor * zeit));
        variablen_y.setText("y = y+1 = " + y,
            new TicksTiming(zeitfaktor * zeit), null);
        zeitfaktor = zeitfaktor + wartezeit;
        variablen_y.setText("y = " + y, new TicksTiming(zeitfaktor * zeit),
            null);
        zeitfaktor++;
        variablen_y.changeColor("color", Color.BLACK, new TicksTiming(
            zeitfaktor * zeit), null);
        zeitfaktor++;
        sourceCode.unhighlight(22, 0, false,
            new TicksTiming(zeitfaktor * zeit), null);
        zeitfaktor++;

        fehler = fehler - dx;
        sourceCode.highlight(23, 0, false, new TicksTiming(zeitfaktor * zeit),
            null);
        variablen_fehler.changeColor("color", Color.RED, new TicksTiming(
            zeitfaktor * zeit), null);
        zeitfaktor++;
        variablen_fehler.setText("fehler = fehler-dx = " + fehler,
            new TicksTiming(zeitfaktor * zeit), null);
        zeitfaktor = zeitfaktor + wartezeit;
        variablen_fehler.setText("fehler = " + fehler, new TicksTiming(
            zeitfaktor * zeit), null);
        zeitfaktor++;
        variablen_fehler.changeColor("color", Color.BLACK, new TicksTiming(
            zeitfaktor * zeit), null);
        zeitfaktor++;
        sourceCode.unhighlight(23, 0, false,
            new TicksTiming(zeitfaktor * zeit), null);
        zeitfaktor++;

        sourceCode.highlight(24, 0, false, new TicksTiming(zeitfaktor * zeit),
            null);
        zeitfaktor = zeitfaktor + wartezeit;
        if (fehler < 0) {

          x++;
          sourceCode.highlight(25, 0, false,
              new TicksTiming(zeitfaktor * zeit), null);
          variablen_x.changeColor("color", Color.RED, new TicksTiming(
              zeitfaktor * zeit), null);
          zeitfaktor++;
          spalten[x - 1].hide(new TicksTiming(zeitfaktor * zeit));
          spalten[x].show(new TicksTiming(zeitfaktor * zeit));
          variablen_x.setText("x = x + 1 = " + x, new TicksTiming(zeitfaktor
              * zeit), null);
          zeitfaktor = zeitfaktor + wartezeit;
          variablen_x.setText("x = " + x, new TicksTiming(zeitfaktor * zeit),
              null);
          zeitfaktor++;
          variablen_x.changeColor("color", Color.BLACK, new TicksTiming(
              zeitfaktor * zeit), null);
          zeitfaktor++;
          sourceCode.unhighlight(25, 0, false, new TicksTiming(zeitfaktor
              * zeit), null);
          zeitfaktor++;

          fehler = fehler + dy;
          sourceCode.highlight(26, 0, false,
              new TicksTiming(zeitfaktor * zeit), null);
          variablen_fehler.changeColor("color", Color.RED, new TicksTiming(
              zeitfaktor * zeit), null);
          zeitfaktor++;
          variablen_fehler.setText("fehler = fehler + dy = " + fehler,
              new TicksTiming(zeitfaktor * zeit), null);
          zeitfaktor = zeitfaktor + wartezeit;
          variablen_fehler.setText("fehler = " + fehler, new TicksTiming(
              zeitfaktor * zeit), null);
          zeitfaktor++;
          variablen_fehler.changeColor("color", Color.BLACK, new TicksTiming(
              zeitfaktor * zeit), null);
          zeitfaktor++;
          sourceCode.unhighlight(26, 0, false, new TicksTiming(zeitfaktor
              * zeit), null);
          zeitfaktor++;

        }
        sourceCode.unhighlight(24, 0, false,
            new TicksTiming(zeitfaktor * zeit), null);

        sourceCode.highlight(28, 0, false, new TicksTiming(zeitfaktor * zeit),
            null);
        zeitfaktor++;
        setPixel(x, y, felder);
        sourceCode.unhighlight(28, 0, false,
            new TicksTiming(zeitfaktor * zeit), null);

        lang.nextStep();
        zeitfaktor = 0;
      }
    }
    zeilen[y].hide(new TicksTiming(zeitfaktor * zeit));
    spalten[x].hide(new TicksTiming(zeitfaktor * zeit));

    // Abschluss
    lang.nextStep();
    lang.hideAllPrimitives();
    zeigeTitel();

    // Infotext
    SourceCodeProperties abschlussProps = new SourceCodeProperties();
    abschlussProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SanSerif", Font.PLAIN, 20));
    abschlussProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    abschlussProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLUE);

    SourceCode abschluss = lang.newSourceCode(new Coordinates(300, 200),
        "sourceCode", null, abschlussProps);

    abschluss.addCodeLine(
        "Es gibt verschiedene Variationen des Bresenham Algorithmus.", null, 0,
        null);
    abschluss
        .addCodeLine(
            "Der hier gezeigte Code ist eine recht einfache Variante, welche aber die Idee des Algorithmus",
            null, 0, null);
    abschluss
        .addCodeLine(
            "sehr anschaulich darstellt. Beschränkt wird diese dadurch, dass Start- und Endpunkt",
            null, 0, null);
    abschluss.addCodeLine(
        "im ersten Quadranten liegen und die Steigung positiv sein muss.",
        null, 0, null);
    abschluss.addCodeLine("Weitere Varianten des Algorithmus können unter",
        null, 0, null);
    abschluss.addCodeLine("http://de.wikipedia.org/wiki/Bresenham-Algorithmus",
        null, 0, null);
    abschluss.addCodeLine("nachgelesen werden.", null, 0, null);

    abschluss.highlight(5, 0, true, null, null);

  }

  private void setPixel(int x, int y, Rect felder[][]) {
    for (int i = 0; i < 3; i++) {
      felder[x][y].hide(new TicksTiming(zeitfaktor * zeit));
      zeitfaktor++;
      felder[x][y].show(new TicksTiming(zeitfaktor * zeit));
      zeitfaktor++;
    }
  }

  /**
   * Erstellt den Titel der Animation
   */
  private void zeigeTitel() {
    TextProperties titelProps = new TextProperties();
    titelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 18));
    titelProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    titelProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    RectProperties hrectProps = new RectProperties();
    hrectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    hrectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
    hrectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    hrectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    lang.newText(
        new Coordinates(280, 10),
        "Bresenham Algorithmus für eine Linie im ersten Quadranten mit positiver Steigung",
        "titel", null, titelProps);
    lang.newRect(new Offset(-2, -2, "titel", AnimalScript.DIRECTION_NW),
        new Offset(2, 2, "titel", AnimalScript.DIRECTION_SE), "hRect", null,
        hrectProps);
  }

}
