package generators.maths;

import interactionsupport.models.FillInBlanksQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Group;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class NewtonMethod {

  private Language              lang;
  private JexlEngine            jexl          = new JexlEngine();
  private JexlContext           jc;
  private SourceCodeProperties  scProps;
  private SourceCode            startText;
  private SourceCode            inputData;
  private SourceCode            pseudoCode;
  private SourceCode            endText;
  private final Coordinates     startTextPos  = new Coordinates(50, 50);
  private final Coordinates     inputDataPos  = new Coordinates(500, 30);
  private final Coordinates     pseudoCodePos = new Coordinates(500, 180);
  private final Coordinates     endTextPos    = new Coordinates(50, 50);
  private final Coordinates     formalPos     = new Coordinates(500, 150);
  private final Coordinates     tablePos      = new Coordinates(850, 60);
  private final Coordinates     plotPos       = new Coordinates(35, 35);

  private final String          start[]       = {
      "Die folgende Animation zeigt, wie mit dem Newton-Verfahren",
      "iterativ eine Nullstelle einer Funktion grafisch und rechnerisch",
      "angenaehert werden kann. Die Idee des Newton-Verfahrens besteht",
      "darin, dass Funktionen in kleinen Bereichen gut durch ihre Tangenten",
      "angenaehert werden.",
      "",
      "Dabei wird durch die Steigung an der Stelle x die naechste Stelle x' berechnet",
      "und sich so an die Nullstelle der Funktion angenaehert. Bei der Wahl des ",
      "Startwertes kann es jedoch passieren, dass man sich von der gesuchten ",
      "Nullstelle entfernt und das Verfahren divergiert.",
      "",
      "Das Verfahren konvergiert nur bei der Wahl eines geeigneten Startwertes. ",
      "Der Algorithmus terminiert nach Erreichen der maximalen Iterationsanzahl oder",
      "der gewaehlten Genauigkeit."          };
  private String                input[];
  private final String          code[]        = { "Der Algorithmus:",
      "	1. Setze x in die Vorschrift ein und", "			berechne f(x) und f'(x)",
      "	2. Berechne x'", "	3. Pruefe, ob f(x') <= d ist",
      "		a) Wenn ja, dann weiter mit Schritt 5",
      "		b) Sonst weiter mit Schritt 4 aus", "	4. Pruefe, ob n+1 < m ist",
      "		a) Wenn ja, dann erhoehe n um 1 und weiter mit Schritt 1",
      "		b) Sonst weiter mit Schritt 5 aus",
      "	5. Abbruch: Naeherungsloesung x'"    };
  private String                end[];
  private IterationList         iterations;
  private StringMatrix          matrix;
  private final int             plot_size     = 400;
  private double                plot_x_l;
  private double                plot_x_r;
  private double                plot_y_o;
  private double                plot_y_u;
  private double                factor_x_w;
  private double                factor_y_h;
  private Group                 plot;
  private LinkedList<Primitive> primitives    = new LinkedList<Primitive>();
  private Group                 table;
  private Group                 formel;
  private int                   width;
  private TextProperties        textProps;
  private MatrixProperties      matrixProps;
//  private Color                 plotColor;
//  private Color                 helpColor;
  private PolylineProperties    graphProps;
  private PolylineProperties    helpProps;

  public NewtonMethod(Language lang) {
    this.width = -50 + Toolkit.getDefaultToolkit().getScreenSize().width;
    this.lang = lang;
    lang.setStepMode(true);
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 12));
    lang.newText(new Coordinates(width / 2 - 80, 15), "Das Newton-Verfahren",
        "title", null, textProps);
    lang.nextStep();
    initProperties();
  }

  public String newton(String funexpr, String dfunexpr, double x0,
      double delta, int maxiter, PolylineProperties graph,
      PolylineProperties hilfslinien,
      SourceCodeProperties pseudoCodeEinstellungen) {
    this.graphProps = graph;
    this.helpProps = hilfslinien;
    this.scProps = pseudoCodeEinstellungen;
    initJexl();
    Expression expr_f = jexl.createExpression("1.0*(" + funexpr + ")");
    Expression expr_df = jexl.createExpression("1.0*(" + dfunexpr + ")");
    calculateResults(expr_f, expr_df, x0, delta, maxiter);
    setStartText();
    lang.nextStep("Start");
    startText.hide();
    showInputData(funexpr, dfunexpr, x0, delta, maxiter);
    lang.nextStep("Eingabewerte anzeigen");
    plotGraph(expr_f);
    lang.nextStep("Graph anzeigen");
    setPseudoCode();
    lang.nextStep("Pseudocode anzeigen");
    initTable();
    lang.nextStep("Tabelle initialisieren");
    FillInBlanksQuestionModel converge = new FillInBlanksQuestionModel("year");
    converge
        .setPrompt("Konvergiert das Newton-Verfahren immer? Antworte mit Ja oder Nein");
    converge.addAnswer("Nein", 1,
        "Richtig! Es konvergiert nicht immer; nur bei geeignetem Startwert.");
    lang.addFIBQuestion(converge);
    double x = 0;
    int i = 1;
    // TwoValueCounter counter = lang.newCounter(matrix);
    // CounterProperties cp = new CounterProperties();
    // cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    // cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
    // lang.newCounterView(counter, new Coordinates(width - 200, 0), cp,
    // true,
    // false);
    // RectProperties rectProps = new RectProperties();
    // rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    // rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    // rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    // lang.newRect(new Coordinates(width - 200, 27), new Coordinates(width,
    // 60), "coverRect", null, rectProps);
    matrix.put(i, 1, "" + 1.0 * Math.round(x * 100000000) / 100000000, null,
        null);
    matrix.put(i, 0, "             " + i, null, null);
    FillInBlanksQuestionModel questions[] = new FillInBlanksQuestionModel[iterations
        .size() * 10];
    for (Iteration r : iterations) {

      unhighlight(-1);
      highlight(1);
      highlight(2);
      x = r.getX();
      double next_x = r.getNEXTX();
      double fx = r.getFX();
      Coordinates points[] = new Coordinates[] {
          new Coordinates((int) Math.round(Math.abs(x - plot_x_l) * factor_x_w
              + plotPos.getX()), (int) Math.round((plot_y_o - 0) * factor_y_h
              + plotPos.getY())),
          new Coordinates((int) Math.round(Math.abs(x - plot_x_l) * factor_x_w
              + plotPos.getX()), (int) Math.round((plot_y_o - fx) * factor_y_h
              + plotPos.getY())) };
      Polyline line1 = lang.newPolyline(points, "line1", null, helpProps);
      primitives.add(line1);
      matrix.put(i, 2, "" + 1.0 * Math.round(fx * 100000000) / 100000000, null,
          null);
      lang.nextStep();
      ++i;
      matrix.put(i, 0, "             " + i, null, null);
      matrix.put(i, 1, "" + 1.0 * Math.round(next_x * 100000000) / 100000000,
          null, null);
      unhighlight(-1);
      highlight(3);
      Coordinates points2[] = new Coordinates[] {
          new Coordinates((int) Math.round(Math.abs(x - plot_x_l) * factor_x_w
              + plotPos.getX()), (int) Math.round((plot_y_o - fx) * factor_y_h
              + plotPos.getY())),
          new Coordinates((int) Math.round(Math.abs(next_x - plot_x_l)
              * factor_x_w + plotPos.getX()), (int) Math.round((plot_y_o - 0)
              * factor_y_h + plotPos.getY())) };
      Polyline line2 = lang.newPolyline(points2, "line2", null, helpProps);
      primitives.add(line2);
      lang.nextStep("" + (i - 1) + ". Iteration");
      unhighlight(-1);
      highlight(4);
      questions[2 * iterations.size() - i] = new FillInBlanksQuestionModel(
          "questions" + (2 * iterations.size() - i) + "");
      questions[2 * iterations.size() - i]
          .setPrompt("Konvergiert das Verfahren jetzt? Antworte mit Ja oder Nein");
      questions[2 * iterations.size() - i]
          .addAnswer(
              (Math.abs(calc(expr_f, next_x)) <= delta) ? "Ja" : "Nein",
              1,
              (Math.abs(calc(expr_f, next_x)) <= delta) ? "Richtig! Es terminiert, weil die Genauigkeit erreicht ist!"
                  : "Richtig! Es terminiert nicht, weil die Genauigkeit noch nicht erreicht ist!");
      lang.addFIBQuestion(questions[2 * iterations.size() - i]);

      if (Math.abs(calc(expr_f, next_x)) <= delta) {
        lang.nextStep("Abbruch durch Genauigkeit");
        matrix.put(i, 2, "" + 1.0 * Math.round(fx * 100000000) / 100000000,
            null, null);
        unhighlight(-1);
        highlight(4);
        highlight(5);
      } else {
        lang.nextStep();
        unhighlight(-1);
        highlight(4);
        highlight(6);
        lang.nextStep();
        unhighlight(-1);
        highlight(7);
        questions[i] = new FillInBlanksQuestionModel("questions" + i + "");
        questions[i]
            .setPrompt("Konvergiert das Verfahren jetzt? Antworte mit Ja oder Nein");
        questions[i]
            .addAnswer(
                (r.getI() >= maxiter) ? "Ja" : "Nein",
                1,
                (r.getI() >= maxiter) ? "Richtig! Es terminiert, weil die maximale Anzahl an Iterationen erreicht ist!"
                    : "Richtig! Es terminiert nicht, weil die maximale Anzahl an Iterationen noch nicht erreicht ist!");
        lang.addFIBQuestion(questions[i]);

        if (r.getI() >= maxiter) {
          lang.nextStep("Abbruch durch max. Anzahl an Iterationen");
          unhighlight(-1);
          highlight(7);
          highlight(9);
        }
        lang.nextStep();
        unhighlight(-1);
        highlight(7);
        highlight(8);
        lang.nextStep();
      }
    }
    lang.nextStep("Terminierung des Algorithmus");
    unhighlight(-1);
    highlight(10);
    lang.nextStep();
    matrix.hide();
    plot = lang.newGroup(primitives, "plot");
    plot.hide();
    table.hide();
    pseudoCode.hide();
    inputData.hide();
    formel.hide();
    setEndText(x);
    lang.nextStep("Ende");

    return lang.getAnimationCode();

  }

  public static void main(String[] args) {
    AnimalScript lang = new AnimalScript("Das Newton-Verfahren",
        "Rene Roepke, Daniel Thul", 800, 600);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    NewtonMethod newton = new NewtonMethod(lang);
    String function = "2*Math.exp(x)*x+x-2";
    String dfunction = "6*x*x+1";
    PolylineProperties props = new PolylineProperties("graph");
    PolylineProperties hprops = new PolylineProperties("graph");
    if (newton.validate(function) && newton.validate(dfunction)) {
      SourceCodeProperties scprops = new SourceCodeProperties();
      newton.newton(function, dfunction, 1, 0.000000001, 10, props, hprops,
          scprops);
      lang.finalizeGeneration();
      String code = lang.toString();
      try {
        FileOutputStream fos = new FileOutputStream("newton.asu");
        Writer out = new OutputStreamWriter(fos, "UTF8");
        out.write(code);
        out.close();
        System.out.println("A file called \"newton.asu\" was created.");
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      JOptionPane.showMessageDialog(null,
          "Achtung! Ihre Eingaben stimmen nicht.", "Achtung",
          JOptionPane.WARNING_MESSAGE);
    }
  }

  public boolean validate(String funktion) {
    initJexl();
    String funktion2 = funktion;
    funktion2 = "1.0*(" + funktion2 + ")";
    if (noOtherVariable(funktion2)) {
      Expression expr_f = jexl.createExpression(funktion2);
      return expr_f.toString().equals(funktion2);
    } else {
      return false;
    }
  }

  private boolean noOtherVariable(String funktion) {
    // (x *x +x -x /x ->
    int i = 0;
    while (i < funktion.length()) {
      String rest = funktion.substring(i);
      String rchar = rest.substring(0, 1);
      if ("x()+-*/,0123456789.".contains(rchar)) {
        i++;
      } else if ("M".contains(rchar)) {
        String math = rest.substring(0, 4);
        if (math.equals("Math")) {
          i += rest.indexOf('(') + 1;
        } else
          return false;
      } else {
        return false;
      }
    }
    String surrounded = funktion;
    i = 0;
    while (i < funktion.length()) {
      surrounded = surrounded.substring(surrounded.indexOf("x") - 1);
      if (surrounded.substring(0, 3).matches("[{(+-/,*}]x[{+-/,*)}]")) {
        surrounded = surrounded.substring(surrounded.indexOf("x") + 1);
        if (surrounded.indexOf("x") <= 0) {
          i = funktion.length();
        }
      } else if (funktion.indexOf("Math.exp(") >= 0
          && surrounded.charAt(0) == 'e' && surrounded.charAt(2) == 'p') {
        surrounded = surrounded.substring(surrounded.indexOf("x") + 1);
      } else {
        return false;
      }
    }
    return true;
  }

  private void initJexl() {
    jc = new MapContext();
    @SuppressWarnings("rawtypes")
    Constructor[] cons = Math.class.getDeclaredConstructors();
    cons[0].setAccessible(true);
    Math math;
    try {
      math = (Math) cons[0].newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
      return;
    } catch (IllegalAccessException e) {
      e.printStackTrace();
      return;
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      return;
    } catch (InvocationTargetException e) {
      e.printStackTrace();
      return;
    }
    jc.set("Math", math);
  }

  private void initProperties() {
    // SourceCodeProperties
    scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.INDENTATION_PROPERTY, 30);
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    // TextProperties
    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));
    // MatrixProperties
    matrixProps = new MatrixProperties();
    matrixProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 15));
    matrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLUE);
    matrixProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);

  }

  private void initTable() {
    LinkedList<Primitive> primitives = new LinkedList<Primitive>();
    Text t0 = lang.newText(
        new Coordinates((tablePos.getX() + 55), (tablePos.getY() - 2)), "n",
        "n", null, textProps);

    primitives.add(t0);
    Text t1 = lang.newText(
        new Coordinates((tablePos.getX() + 180), (tablePos.getY() - 2)), "x",
        "x", null, textProps);

    primitives.add(t1);
    Text t2 = lang.newText(
        new Coordinates((tablePos.getX() + 300), (tablePos.getY() - 2)),
        "f(x)", "f(x)", null, textProps);
    primitives.add(t2);
    Polyline tableline1 = lang.newPolyline(new Coordinates[] {
        new Coordinates(tablePos.getX(), tablePos.getY() + 19),
        new Coordinates(tablePos.getX() + 400, tablePos.getY() + 19) },
        "tableline1", null);
    primitives.add(tableline1);
    Polyline tableline2 = lang.newPolyline(new Coordinates[] {
        new Coordinates(tablePos.getX() + 115, tablePos.getY()),
        new Coordinates(tablePos.getX() + 115, tablePos.getY() + 50
            + iterations.size() * 30) }, "tableline2", null);
    primitives.add(tableline2);
    Polyline tableline3 = lang.newPolyline(new Coordinates[] {
        new Coordinates(tablePos.getX() + 255, tablePos.getY()),
        new Coordinates(tablePos.getX() + 255, tablePos.getY() + 50
            + iterations.size() * 30) }, "tableline3", null);
    primitives.add(tableline3);

    String[][] data = new String[iterations.size() + 1 + 2][3];
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[i].length; j++) {
        data[i][j] = "";
      }
    }
    matrix = lang.newStringMatrix(tablePos, data, "name", null, matrixProps);
    matrix.put(0, 0, "                                   ", null, null);
    matrix.put(0, 1, "                                   ", null, null);
    matrix.put(0, 2, "                                   ", null, null);
    primitives.add(matrix);
    table = lang.newGroup(primitives, "table");

  }

  private double calc(Expression funExpr, Double x) {
    jc.set("x", x);
    double erg;
    try {
      erg = (Double) funExpr.evaluate(jc);
    } catch (Exception e) {
      throw new IllegalArgumentException(
          "Die Funktion oder Ableitung ist nicht im geforderten Format");
    }
    return erg;
  }

  private void calculateResults(Expression expr_f, Expression expr_df,
      Double x, double delta, int maxIterations) {

    iterations = new IterationList();
    Double x2 = x;
    Double next_x = x2;
    int i = 0;
    do {
      x2 = next_x;
      Double fx = calc(expr_f, next_x);
      Double dfx = calc(expr_df, next_x);
      next_x = x2 - fx / dfx;
      ++i;
      iterations.add(new Iteration(x2, next_x, i, fx));
    } while (i < maxIterations && Math.abs(calc(expr_f, next_x)) > delta);
  }

  private void setStartText() {
    startText = lang.newSourceCode(startTextPos, "startText", null, scProps);
    for (String line : start) {
      startText.addCodeLine(line, null, 0, null);
    }
  }

  private void showInputData(String funexpr, String dfunexpr, double x0,
      double delta, int maxiter) {
    input = new String[] { "Funktion:   f(x)=".concat(funexpr),
        "Ableitung: f'(x)=".concat(dfunexpr), "Startwert: " + x0,
        "Genauigkeit d: " + delta, "max. Anzahl an Iterationen m: " + maxiter };
    inputData = lang.newSourceCode(new Coordinates(inputDataPos.getX(),
        inputDataPos.getY()), "sourceCode", null, scProps);
    for (String line : input) {
      inputData.addCodeLine(line, null, 0, null);
    }
  }

  private void setPseudoCode() {
    LinkedList<Primitive> primitives = new LinkedList<Primitive>();
    Text t1 = lang.newText(new Coordinates(formalPos.getX(), formalPos.getY()),
        "x'", "lsdg", null, textProps);
    primitives.add(t1);
    Text t2 = lang.newText(
        new Coordinates(formalPos.getX() + 30, formalPos.getY()), "=",
        "gleich1", null, textProps);
    primitives.add(t2);
    Text t3 = lang.newText(
        new Coordinates(formalPos.getX() + 50, formalPos.getY()), "x -",
        "rsdg", null, textProps);
    primitives.add(t3);
    Text t4 = lang.newText(
        new Coordinates(formalPos.getX() + 95, formalPos.getY() - 9), "f(x)",
        "rsdg2o", null, textProps);
    primitives.add(t4);
    Text t5 = lang.newText(
        new Coordinates(formalPos.getX() + 92, formalPos.getY() + 11), "f'(x)",
        "rsdg2u", null, textProps);
    primitives.add(t5);
    Polyline p = lang.newPolyline(
        new Coordinates[] {
            new Coordinates(formalPos.getX() + 80, formalPos.getY() + 10),
            new Coordinates(formalPos.getX() + 120, formalPos.getY() + 10) },
        "bruch", null);
    primitives.add(p);
    formel = lang.newGroup(primitives, "formel");

    pseudoCode = lang.newSourceCode(new Coordinates(pseudoCodePos.getX(),
        pseudoCodePos.getY()), "sourceCode", null, scProps);
    Pattern linePattern = Pattern.compile("(?<depth>\\t*)(?<code>.*)");
    for (String line : code) {
      Matcher matcher = linePattern.matcher(line);
      if (matcher.matches()) {
        int depth = matcher.group("depth").length();
        String code = matcher.group("code").trim();
        pseudoCode.addCodeLine(code, null, depth, null);
      }
    }
  }

  private void setEndText(double x) {
    end = new String[] {
        "Das Newton-Verfahren terminierte mit",
        "folgendem Ergebnis: "
            + iterations.get(iterations.size() - 1).getNEXTX(), "",
        "Die Interpretation des Ergebnisses bleibt den Anwender ueberlassen",
        "da dieses Verfahren nicht immer zu einer Nullstelle konvergiert." };
    endText = lang.newSourceCode(endTextPos, "endText", null, scProps);
    for (String line : end) {
      endText.addCodeLine(line, null, 0, null);
    }
  }

  private void unhighlight(int i) {
    if (i == -1) {
      for (int j = 0; j < code.length; j++) {
        pseudoCode.unhighlight(j);
      }
    } else {
      pseudoCode.unhighlight(i);
    }

  }

  private void highlight(int i) {
    if (i == -1) {
      for (int j = 0; j < code.length; j++) {
        pseudoCode.highlight(j);
      }
    } else {
      pseudoCode.highlight(i);
    }

  }

  private void plotGraph(Expression expr_f) {
    plot_x_l = iterations.findLowestX();
    plot_x_r = iterations.findHighestX();
    plot_y_o = iterations.findHighestY();
    plot_y_u = iterations.findLowestY();
    factor_x_w = (plot_size / Math.abs(plot_x_l - plot_x_r));
    factor_y_h = (plot_size / Math.abs(plot_y_o - plot_y_u));
    // position of x_axis
    int y_axis_pos_x;
    if (plot_x_l <= 0 && plot_x_r >= 0) {
      y_axis_pos_x = (int) Math.round(plotPos.getX() + factor_x_w
          * Math.abs(plot_x_l));
    } else {
      y_axis_pos_x = plotPos.getX() - 5;
    }
    // position of y_axis
    int x_axis_pos_y;
    if (plot_y_u <= 0 && plot_y_o >= 0) {
      x_axis_pos_y = (int) Math.round(plotPos.getY() + plot_y_o * factor_y_h);
    } else {
      x_axis_pos_y = plotPos.getY() + plot_size + 5;
    }
    Polyline axis_x = lang.newPolyline(new Coordinates[] {
        new Coordinates(plotPos.getX() - 3, x_axis_pos_y),
        new Coordinates(plotPos.getX() - 3 + plot_size + 10, x_axis_pos_y) },
        "axis_x", null);
    primitives.add(axis_x);
    Polyline axis_x_1 = lang.newPolyline(new Coordinates[] {
        new Coordinates(plotPos.getX() - 5 + plot_size + 10 - 5,
            x_axis_pos_y - 3),
        new Coordinates(plotPos.getX() - 5 + plot_size + 10, x_axis_pos_y) },
        "axis_x_1", null);
    primitives.add(axis_x_1);
    Polyline axis_x_2 = lang.newPolyline(new Coordinates[] {
        new Coordinates(plotPos.getX() - 5 + plot_size + 10 - 5,
            x_axis_pos_y + 3),
        new Coordinates(plotPos.getX() - 5 + plot_size + 10, x_axis_pos_y) },
        "axis_x_2", null);
    primitives.add(axis_x_2);
    Polyline axis_y = lang.newPolyline(new Coordinates[] {
        new Coordinates(y_axis_pos_x, plotPos.getY() - 5),
        new Coordinates(y_axis_pos_x, plotPos.getY() - 5 + plot_size + 10) },
        "axis_y", null);
    primitives.add(axis_y);
    Polyline axis_y_1 = lang.newPolyline(new Coordinates[] {
        new Coordinates(y_axis_pos_x + 3, plotPos.getY() - 5 + 5),
        new Coordinates(y_axis_pos_x, plotPos.getY() - 5) }, "axis_y_1", null);
    primitives.add(axis_y_1);
    Polyline axis_y_2 = lang.newPolyline(new Coordinates[] {
        new Coordinates(y_axis_pos_x - 3, plotPos.getY() - 5 + 5),
        new Coordinates(y_axis_pos_x, plotPos.getY() - 5) }, "axis_y_2", null);
    primitives.add(axis_y_2);
    int x_draw = plotPos.getX();
    int y_draw = (int) Math.round(plotPos.getY() + factor_y_h
        * (plot_y_o - calc(expr_f, plot_x_l)));
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 12));
    Text tx1 = lang.newText(new Coordinates(x_draw, x_axis_pos_y + 5), ""
        + plot_x_l, "tx1", null, textProps);
    primitives.add(tx1);
    Polyline x1 = lang.newPolyline(new Coordinates[] {
        new Coordinates(x_draw, x_axis_pos_y - 3),
        new Coordinates(x_draw, x_axis_pos_y + 3) }, "x1", null);
    primitives.add(x1);
    if (y_draw <= plotPos.getY() + plot_size) {
      Polyline y1 = lang.newPolyline(new Coordinates[] {
          new Coordinates(y_axis_pos_x - 3, y_draw),
          new Coordinates(y_axis_pos_x + 3, y_draw) }, "y1", null);
      Text ty1 = lang.newText(new Coordinates(y_axis_pos_x - 25, y_draw - 8),
          "" + calc(expr_f, plot_x_l), "ty1", null, textProps);
      primitives.add(y1);
      primitives.add(ty1);
    }
    x_draw = plotPos.getX() + plot_size;
    Text tx2 = lang.newText(new Coordinates(x_draw, x_axis_pos_y + 5), ""
        + plot_x_r, "tx2", null, textProps);
    primitives.add(tx2);
    y_draw = (int) Math.round(plotPos.getY() + factor_y_h
        * (plot_y_o - calc(expr_f, plot_x_r)));
    Polyline x2 = lang.newPolyline(new Coordinates[] {
        new Coordinates(x_draw, x_axis_pos_y - 3),
        new Coordinates(x_draw, x_axis_pos_y + 3) }, "x2", null);
    primitives.add(x2);
    if (y_draw >= plotPos.getY()) {
      Polyline y2 = lang.newPolyline(new Coordinates[] {
          new Coordinates(y_axis_pos_x - 3, y_draw),
          new Coordinates(y_axis_pos_x + 3, y_draw) }, "y2", null);
      Text ty2 = lang.newText(new Coordinates(y_axis_pos_x - 25, y_draw - 8),
          "" + calc(expr_f, plot_x_r), "ty2", null, textProps);
      primitives.add(ty2);
      primitives.add(y2);
    }
    Coordinates points[] = new Coordinates[plot_size + 1];
    for (int x = plotPos.getX(); x <= plotPos.getX() + plot_size; x++) {
      int yy = (int) Math.round(plotPos.getY()
          + factor_y_h
          * (plot_y_o - calc(expr_f, plot_x_l + (x - plotPos.getX())
              / factor_x_w)));
      if (yy >= plotPos.getY() && yy <= plotPos.getY() - 5 + plot_size + 10) {
        points[x - plotPos.getX()] = new Coordinates(x, yy);
      }

    }
    Polyline line = lang.newPolyline(points, "line", null, graphProps);
    primitives.add(line);
  }

  public class Iteration {
    private Double x;
    private Double next_x;
    private int    iter;
    private Double fx;

    public Iteration(Double x, Double next_x, int iter, Double fx) {
      this.x = x;
      this.next_x = next_x;
      this.iter = iter;
      this.fx = fx;
    }

    @Override
    public String toString() {
      return x + " " + next_x + " " + iter + " " + fx;
    }

    public Double getX() {
      return x;
    }

    public Double getFX() {
      return fx;
    }

    public Double getNEXTX() {
      return next_x;
    }

    public int getI() {
      return iter;
    }
  }

  public class IterationList extends ArrayList<Iteration> {

    private static final long serialVersionUID = 1L;

    public Double findHighestX() {
      Double value = this.get(0).getX();
      if (this.size() == 1 && (this.get(0).getX() != this.get(0).getFX())) {
        return (this.get(0).getX() > this.get(0).getNEXTX()) ? this.get(0)
            .getX() : this.get(0).getNEXTX();
      } else if (this.size() == 1 && this.get(0).getX() == this.get(0).getFX()) {
        return this.get(0).getX() + 1;
      }
      for (Iteration r : this) {
        if (r.getX() > value)
          value = r.getX();
      }
      return Math.ceil(value);
    }

    public Double findHighestY() {
      Double value = this.get(0).getFX();
      if (this.size() == 1) {
        if (this.get(0).getFX() < 0) {
          return 0.0;
        }
        return this.get(0).getFX() + 1;
      }
      for (Iteration r : this) {
        if (r.getFX() > value)
          value = r.getFX();
      }
      return Math.ceil(value);
    }

    public Double findLowestX() {
      Double value = this.get(0).getX();
      if (this.size() == 1 && (this.get(0).getX() != this.get(0).getFX())) {
        return (this.get(0).getX() < this.get(0).getNEXTX()) ? this.get(0)
            .getX() : this.get(0).getNEXTX();
      } else if (this.size() == 1 && this.get(0).getX() == this.get(0).getFX()) {
        return this.get(0).getX() - 1;
      }
      for (Iteration r : this) {
        if (r.getX() < value)
          value = r.getX();
      }
      return Math.floor(value);
    }

    public Double findLowestY() {
      Double value = this.get(0).getX();
      if (this.size() == 1) {
        if (this.get(0).getFX() > 0) {
          return 0.0;
        }
        return this.get(0).getFX() - 1;
      }
      for (Iteration r : this) {
        if (r.getFX() < value)
          value = r.getFX();
      }
      return Math.floor(value);
    }

  }

}
