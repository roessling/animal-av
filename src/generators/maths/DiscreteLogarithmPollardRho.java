package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class DiscreteLogarithmPollardRho implements ValidatingGenerator {
  private Language             lang;
  private int                  alpha;
  private int                  gamma;
  private int                  m;
  private int                  x1;
  private int                  y1;

  private TextProperties       titleProps;
  private SourceCodeProperties continuousTextProps;
  private TextProperties       tableTextProps;
  private SourceCodeProperties algorithmTextProps;
  private TextProperties       graphLabelProps;
  private CircleProperties     graphNodeProps;
  private CircleProperties     graphCurrentNodeProps;
  private CircleProperties     graphMemoryNodeProps;

  public void init() {
    lang = new AnimalScript(
        "Pollard-ρ-Algorithmus zur Berechnung des diskreten Logarithmus",
        "Pascal Weisenburger", 800, 600);
    lang.setStepMode(true);
  }

  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {
    alpha = (Integer) primitives.get("alpha");
    gamma = (Integer) primitives.get("gamma");
    m = (Integer) primitives.get("m");
    x1 = (Integer) primitives.get("x[1]");
    y1 = (Integer) primitives.get("y[1]");

    return m >= 1 && m < 1000 && alpha >= 1 && alpha < 1000 && gamma >= 0
        && gamma < 1000 && x1 >= 0 && x1 < 1000 && y1 >= 0 && y1 < 1000;
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    alpha = (Integer) primitives.get("alpha");
    gamma = (Integer) primitives.get("gamma");
    m = (Integer) primitives.get("m");
    x1 = (Integer) primitives.get("x[1]");
    y1 = (Integer) primitives.get("y[1]");

    titleProps = (TextProperties) props.getPropertiesByName("title");
    continuousTextProps = (SourceCodeProperties) props
        .getPropertiesByName("continuousText");
    tableTextProps = (TextProperties) props.getPropertiesByName("tableText");
    algorithmTextProps = (SourceCodeProperties) props
        .getPropertiesByName("algorithmText");
    graphLabelProps = (TextProperties) props.getPropertiesByName("graphLabel");
    graphNodeProps = (CircleProperties) props.getPropertiesByName("graphNode");
    graphCurrentNodeProps = (CircleProperties) props
        .getPropertiesByName("graphCurrentNode");
    graphMemoryNodeProps = (CircleProperties) props
        .getPropertiesByName("graphMemoryNode");

    if (m < 1)
      m = 1;
    if (m > 999)
      m = 999;
    if (alpha < 1)
      alpha = 1;
    if (alpha > 999)
      alpha = 999;
    if (gamma < 0)
      gamma = 0;
    if (gamma > 999)
      gamma = 999;
    if (x1 < 0)
      x1 = 0;
    if (x1 > 999)
      x1 = 999;
    if (y1 < 0)
      y1 = 0;
    if (y1 > 999)
      y1 = 999;

    log(alpha, gamma, m, x1, y1);

    return lang.toString();
  }

  private String[]      congruence = new String[3];
  private List<Integer> beta;
  private List<Integer> x;
  private List<Integer> y;
  private int           cycleBegin, cycleEnd;

  /**
   * Calculates the discrete logarithm <em>x</em> for <em>γ</em>^<em>x</em> ≡
   * <em>α</em> mod <em>m</em> with the given start values <em>x[1]</em> and
   * <em>y[1]</em>
   * 
   * @param alpha
   *          α
   * @param gamma
   *          γ
   * @param m
   *          modulus
   * @param x1
   *          <em>x[1]</em> (start value)
   * @param y1
   *          <em>y[1]</em> (start value)
   */
  public void log(final int alpha, final int gamma, final int m, final int x1,
      final int y1) {
    final int n = phi(m);

    /*
     * create title
     */
    TextProperties headerText1Props = new TextProperties();
    headerText1Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 32));
    headerText1Props.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        titleProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    Primitive headerText1 = lang.newText(new Coordinates(20, 20),
        "Pollard-ρ-Algorithmus", "headerText1", null, headerText1Props);

    TextProperties headerText2Props = new TextProperties();
    headerText2Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 16));
    headerText2Props.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        titleProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    Primitive headerText2 = lang.newText(new Offset(0, -20, headerText1, "SW"),
        "Berechnung des diskreten Logarithmus", "headerText2", null,
        headerText2Props);

    PolylineProperties headerProps = new PolylineProperties();
    headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        titleProps.get(AnimationPropertiesKeys.COLOR_PROPERTY));
    Primitive header = lang.newPolyline(new Node[] {
        new Offset(-5, 5, headerText2, "SW"),
        new Offset(200, 5, headerText2, "SE") }, "header", null, headerProps);

    /*
     * description
     */
    SourceCode desc = lang.newSourceCode(new Offset(5, 15, header, "SW"),
        "description", null, continuousTextProps);
    desc.addCodeLine(
        "Der Pollard-ρ-Algorithmus zur Berechnung des diskreten Logarithmus",
        null, 0, null);
    desc.addCodeLine(
        "ist ein Algorithmus zum schnellen Lösen des Diskreten-Logarithmus-Problems.",
        null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine(
        "Pollard-ρ-Methoden sind im Allgemeinen Algorithmen zur Bestimmung der",
        null, 0, null);
    desc.addCodeLine(
        "Periodenlänge einer Zahlenfolge, die mit einer mathematischen Funktion berechnet wird.",
        null, 0, null);
    desc.addCodeLine("Hierbei werden Folgen von Teilergebnissen berechnet.",
        null, 0, null);
    desc.addCodeLine(
        "Ab einem bestimmten Punkt wiederholt sich ein Teil dieser Teilergebnisse nur noch.",
        null, 0, null);
    desc.addCodeLine(
        "Man kann die Teilergebnisse grafisch so anordnen, dass sich die Gestalt des Buchstaben",
        null, 0, null);
    desc.addCodeLine(
        "ρ (Rho) erkennen lässt. Daraus leitet sich die Bezeichnung der Methoden ab.",
        null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine("http://de.wikipedia.org/wiki/Pollard-Rho-Methode", null,
        0, null);
    desc.addCodeLine(
        "http://en.wikipedia.org/wiki/Pollard's_rho_algorithm_for_logarithms",
        null, 0, null);

    lang.nextStep("Beschreibung");
    desc.hide();

    /*
     * run algorithm
     */
    beta = new ArrayList<Integer>();
    x = new ArrayList<Integer>();
    y = new ArrayList<Integer>();

    int result = pollardRho(alpha, gamma, m, n, x1, y1);

    // find cycle
    cycleEnd = beta.size() - 1;
    for (int i = cycleEnd - 1;; i--)
      if (i < 0) {
        cycleBegin = cycleEnd = beta.size();
        break;
      } else if (beta.get(i).equals(beta.get(cycleEnd))) {
        cycleBegin = i;
        for (i = 1; cycleBegin - i >= 0; i++)
          if (!beta.get(cycleBegin - i).equals(beta.get(cycleEnd - i)))
            break;
          else if (cycleEnd - i == cycleBegin) {
            cycleBegin -= i;
            cycleEnd -= i;
            i = 1;
          }
        break;
      }

    /*
     * create table and text elements
     */
    tableTextProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    Text iText = lang.newText(new Offset(5, 15, header, "SW"), "i", "step",
        null, tableTextProps);
    Text betaText = lang.newText(new Offset(15, 0, iText, "NE"), "β[i]",
        "beta", null, tableTextProps);
    Text xText = lang.newText(new Offset(15, 0, betaText, "NE"), "x[i]", "x",
        null, tableTextProps);
    Text yText = lang.newText(new Offset(15, 0, xText, "NE"), "y[i]", "y",
        null, tableTextProps);
    Text memoryText = lang.newText(new Offset(10, 0, yText, "NE"),
        "im Speicher", "memory", null, tableTextProps);
    Polyline memoryMarker = null;
    tableTextProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);

    SourceCode specification = lang.newSourceCode(new Offset(30, -12,
        memoryText, "NE"), "specification", null, algorithmTextProps);
    specification
        .addCodeLine("berechne diskreten Logarithmus x", null, 0, null);
    specification.addCodeLine("für γ^x ≡ α  mod m", null, 0, null);
    specification.addCodeLine("mit", null, 0, null);
    specification.addCodeLine("γ = " + gamma, null, 1, null);
    specification.addCodeLine("α = " + alpha, null, 1, null);
    specification.addCodeLine("m = " + m, null, 1, null);

    /*
     * create graph
     */
    graphNodeProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    graphCurrentNodeProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
    graphMemoryNodeProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);

    PolylineProperties lineProps = new PolylineProperties();
    lineProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lineProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);

    graphLabelProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    graphLabelProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);

    final int stepX = 5, stepY = 50;
    double stepRadius = 2 * Math.PI / (cycleEnd - cycleBegin);
    int radius = 8 * (cycleEnd - cycleBegin);
    Offset center = new Offset(stepX * cycleBegin + radius + 130, radius + 20,
        specification, "NE");
    Offset position = null, previousPosition = null;

    // linear graph part
    for (int i = 0; i < cycleBegin + 1; i++) {
      int step = cycleBegin - i;
      previousPosition = position;
      position = new Offset(center.getX() - step * stepX - radius,
          center.getY() + step * stepY, center.getRef(), center.getDirection());

      if (i < cycleBegin) {
        lang.newCircle(position, 15, "node" + i, null, graphNodeProps);
        lang.newCircle(position, 15, "memoryNode" + i, null,
            graphMemoryNodeProps);
        lang.newCircle(position, 15, "currentNode" + i, null,
            graphCurrentNodeProps);
        Offset textPosition = new Offset(position.getX(), position.getY() - 8,
            position.getRef(), position.getDirection());
        lang.newText(textPosition, beta.get(i).toString(), "label" + i, null,
            graphLabelProps);
      }
      if (i > 0) {
        lang.newPolyline(new Node[] { previousPosition, position }, "line" + i,
            null, lineProps);
      }
    }

    // circular graph part
    for (int i = cycleBegin; i < cycleEnd + 1; i++) {
      double angle = Math.PI + (i - cycleBegin) * stepRadius;
      if (i < cycleEnd) {
        position = new Offset(center.getX() + (int) (radius * Math.cos(angle)),
            center.getY() + (int) (radius * Math.sin(angle)), center.getRef(),
            center.getDirection());
        lang.newCircle(position, 15, "node" + i, null, graphNodeProps);
        lang.newCircle(position, 15, "memoryNode" + i, null,
            graphMemoryNodeProps);
        lang.newCircle(position, 15, "currentNode" + i, null,
            graphCurrentNodeProps);
        position = new Offset(position.getX(), position.getY() - 8,
            position.getRef(), position.getDirection());
        lang.newText(position, beta.get(i).toString(), "label" + i, null,
            graphLabelProps);
      }
      if (i > cycleBegin) {
        // workaround since neither newCircleSeg nor newArc work as expected
        lang.addLine("circleSeg \"line" + i + "\" offset (" + center.getX()
            + ", " + center.getY() + ") from \"" + center.getRef().getName()
            + "\" " + center.getDirection() + " radius (" + radius + ", "
            + radius + ") angle " + (int) (stepRadius * 180 / Math.PI)
            + " starts " + (int) (360 - angle * 180 / Math.PI)
            + " color (0, 0, 0) depth 2 hidden");
      }
    }

    /*
     * show algorithm steps
     */
    lang.nextStep("Problembeschreibung");
    specification.addCodeLine("n = φ(m) = " + n + " (Gruppenordnung)", null, 1,
        null);
    lang.nextStep();

    SourceCode searchPair = lang.newSourceCode(new Offset(0, 0, specification,
        "SW"), "searchPair", null, algorithmTextProps);
    searchPair.addCodeLine("wähle drei paarweise disjunkte Teilmengen", null,
        0, null);
    searchPair.addCodeLine("G[1], G[2] und G[3] von G:", null, 0, null);
    lang.nextStep();
    if (m / 3 == 0)
      searchPair.addCodeLine("G[1] = ∅", null, 1, null);
    else
      searchPair.addCodeLine("G[1] = {" + 0 + ", ..., " + (m / 3 - 1) + "}",
          null, 1, null);
    if (2 * m / 3 == 0)
      searchPair.addCodeLine("G[2] = ∅", null, 1, null);
    else
      searchPair.addCodeLine("G[2] = {" + (m / 3) + ", ..., " + (2 * m / 3 - 1)
          + "}", null, 1, null);
    searchPair.addCodeLine(
        "G[3] = {" + (2 * m / 3) + ", ..., " + (m - 1) + "}", null, 1, null);
    lang.nextStep();
    searchPair.addCodeLine("", null, 0, null);
    searchPair.addCodeLine("wähle Startwert: (x[1], y[1]) = (" + x1 + ", " + y1
        + ")", null, 0, null);
    lang.nextStep("Berechnungs-Vorbereitungen");
    searchPair.addCodeLine("", null, 0, null);
    searchPair.addCodeLine("berechne:", null, 0, null);
    searchPair.addCodeLine("", null, 0, null);
    searchPair.addCodeLine("β[i] = γ^x[i] * α^y[i]  mod m", null, 1, null);

    lang.nextStep();
    iText.show();
    betaText.show();
    xText.show();
    yText.show();
    memoryText.show();

    // positioning workaround for missing
    // right text alignment animation property key
    String string;
    Rectangle2D rect = null;
    FontRenderContext fontRenderContext = new FontRenderContext(null, false,
        false);
    Font font = (Font) tableTextProps
        .get(AnimationPropertiesKeys.FONT_PROPERTY);

    int memoryIndex = 2;
    for (int i = 0; i < beta.size(); i++) {
      setGraphCurrentNode(i);

      if (i == 1) {
        PolylineProperties memoryProps = new PolylineProperties();
        memoryProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
        memoryMarker = lang
            .newPolyline(
                new Node[] {
                    new Offset(-10, (int) rect.getHeight() / 2 + 2, memoryText,
                        "SE"),
                    new Offset(10, (int) rect.getHeight() / 2 + 2, memoryText,
                        "SW") }, "memory", null, memoryProps);

        setGraphMemoryNode(i - 1);
      } else if (i == memoryIndex) {
        memoryMarker.moveBy("translate", 0, (i - (memoryIndex >> 1))
            * ((int) rect.getHeight() + 3), null, null);
        memoryIndex <<= 1;

        setGraphMemoryNode(i - 1);
      }

      string = String.valueOf(i + 1);
      rect = font.getStringBounds(string, fontRenderContext);
      iText = lang.newText(new Offset(-(int) rect.getWidth(), 2, iText, "SE"),
          string, "step" + i, null, tableTextProps);

      string = beta.get(i).toString();
      rect = font.getStringBounds(string, fontRenderContext);
      betaText = lang.newText(new Offset(-(int) rect.getWidth(), 2, betaText,
          "SE"), string, "beta" + i, null, tableTextProps);

      string = x.get(i).toString();
      rect = font.getStringBounds(string, fontRenderContext);
      xText = lang.newText(new Offset(-(int) rect.getWidth(), 2, xText, "SE"),
          string, "x" + i, null, tableTextProps);

      string = y.get(i).toString();
      rect = font.getStringBounds(string, fontRenderContext);
      yText = lang.newText(new Offset(-(int) rect.getWidth(), 2, yText, "SE"),
          string, "y" + i, null, tableTextProps);

      lang.nextStep((i + 1) + ". Iteration");

      if (i == 0) {
        searchPair.addCodeLine("", null, 0, null);
        searchPair.addCodeLine("x[i+1] = x[i] + 1  mod n  falls β[i] ∈ G[1]",
            null, 1, null);
        searchPair.addCodeLine("x[i+1] = 2 * x[i]  mod n  falls β[i] ∈ G[2]",
            null, 1, null);
        searchPair.addCodeLine("x[i+1] = x[i]             falls β[i] ∈ G[3]",
            null, 1, null);
        searchPair.addCodeLine("", null, 0, null);
        searchPair.addCodeLine("y[i+1] = y[i]             falls β[i] ∈ G[1]",
            null, 1, null);
        searchPair.addCodeLine("y[i+1] = 2 * y[i]  mod n  falls β[i] ∈ G[2]",
            null, 1, null);
        searchPair.addCodeLine("y[i+1] = y[i] + 1  mod n  falls β[i] ∈ G[3]",
            null, 1, null);
        lang.nextStep();
        searchPair.addCodeLine("", null, 0, null);
        searchPair.addCodeLine("speichere jeweils β[i], x[i] und y[i]", null,
            0, null);
        searchPair.addCodeLine("für alle i = 2^N = 1, 2, 4, 8, 16, ...", null,
            0, null);
        searchPair.addCodeLine("", null, 0, null);
        searchPair.addCodeLine("vergleiche das aktuelle β[i] mit dem", null, 0,
            null);
        searchPair.addCodeLine(
            "gespeicherten bis zwei gleiche β gefunden werden", null, 0, null);
      }
    }

    int index1 = memoryIndex >> 1, index2 = beta.size();
    searchPair.hide();
    SourceCode calculation = lang.newSourceCode(new Offset(0, 0, specification,
        "SW"), "calculation", null, algorithmTextProps);
    calculation.addCodeLine("zwei gleiche β gefunden:", null, 0, null);
    calculation.addCodeLine("β[" + index1 + "] = β[" + index2 + "]", null, 1,
        null);
    lang.nextStep("Match gefunden");
    calculation.addCodeLine("", null, 0, null);
    calculation.addCodeLine("löse Kongruenz:", null, 0, null);
    calculation.addCodeLine("x[" + index1 + "] - x[" + index2 + "] ≡ x * (y["
        + index2 + "] - y[" + index1 + "])  mod n", null, 1, null);
    lang.nextStep();
    if (result == -1) {
      calculation.addCodeLine(congruence[0], null, 1, null);
      calculation.addCodeLine("", null, 0, null);
      calculation.addCodeLine("da diese Kongruenz nicht lösbar ist,", null, 0,
          null);
      calculation.addCodeLine(
          "existiert der gesuchte diskrete Logarithmus nicht.", null, 0, null);
    } else {
      calculation.addCodeLine(congruence[0], null, 1, null);
      if (!congruence[0].equals(congruence[1]))
        calculation.addCodeLine(congruence[1], null, 1, null);
      calculation.addCodeLine("mögliche Lösungen: " + congruence[2], null, 1,
          null);
      lang.nextStep();
      calculation.addCodeLine("", null, 0, null);
      calculation.addCodeLine("suche ein x, das auch die Kongruenzgleichung",
          null, 0, null);
      calculation.addCodeLine(gamma + "^x ≡ " + alpha + "  mod " + m
          + " erfüllt", null, 0, null);
      lang.nextStep();
      if (result == -2) {
        calculation.addCodeLine("", null, 0, null);
        calculation.addCodeLine("so ein x kann nicht gefunden werden,", null,
            0, null);
        calculation
            .addCodeLine("der gesuchte diskrete Logarithmus existiert nicht.",
                null, 0, null);
      } else {
        calculation.addCodeLine("", null, 0, null);
        calculation.addCodeLine("x = " + result
            + " ist der gesuchte diskrete Logarithmus", null, 0, null);
      }
    }
    lang.nextStep("Lösung der Kongruenzen");

    /*
     * conclusion
     */
    lang.addLine("hideAll"); // lang.hideAllPrimitives creates a new step
    header.show();
    headerText1.show();
    headerText2.show();
    SourceCode conclusion = lang.newSourceCode(new Offset(5, 15, header, "SW"),
        "conclusion", null, continuousTextProps);
    conclusion.addCodeLine(
        "Der Algorithmus benötigt O(√(|G|)) viele Gruppenoperationen", null, 0,
        null);
    conclusion.addCodeLine(
        "und ist damit wesentlich effitienter als ein naiver Ansatz,", null, 0,
        null);
    conclusion.addCodeLine(
        "lässt sich allerdings für sehr große Zahlen in der Praxis", null, 0,
        null);
    conclusion.addCodeLine("dennoch nicht einsetzen.", null, 0, null);
    conclusion.addCodeLine("", null, 0, null);
    conclusion.addCodeLine(
        "Der Algorithmus benötigt nur konstant viele Speicherplätze,", null, 0,
        null);
    conclusion.addCodeLine("der Speicherplatzbedarf liegt also in O(1).", null,
        0, null);
    lang.nextStep("Schlussbemerkung");
  }

  /**
   * highlights the <em>i</em>-th value in the graph as being the currently
   * calculated value making the appropriate graph node visible if necessary
   */
  private void setGraphCurrentNode(int index) {
    if (index > 0)
      lang.addLine("hide \"currentNode" + getGraphIndex(index - 1) + "\"");

    if (index < cycleEnd) {
      lang.addLine("show \"node" + index + "\"");
      lang.addLine("show \"label" + index + "\"");
    }
    lang.addLine("show \"currentNode" + getGraphIndex(index) + "\"");

    if (index > 0 && index <= cycleEnd)
      lang.addLine("show \"line" + index + "\"");
  }

  /**
   * highlights the <em>i</em>-th value in the graph as being in memory
   */
  private void setGraphMemoryNode(int index) {
    if (index > 0)
      lang.addLine("hide \"memoryNode" + getGraphIndex(index >> 1) + "\"");
    lang.addLine("show \"memoryNode" + getGraphIndex(index) + "\"");
  }

  /**
   * Calculates the graph node index for a given <em>i</em> considering cycles
   */
  private int getGraphIndex(int index) {
    return index < cycleEnd ? index : (index - cycleBegin)
        % (cycleEnd - cycleBegin) + cycleBegin;
  }

  /**
   * Solves the discrete logarithm problem using <a href=
   * "http://en.wikipedia.org/wiki/Pollard's_rho_algorithm_for_logarithms"
   * >Pollard's ρ algorithm</a>. calculating <em>x</em> for <em>γ</em>^
   * <em>x</em> ≡ <em>α</em> mod <em>m</em> with the given start values
   * <em>x[1]</em> and <em>y[1]</em> and <em>n</em> = φ(<em>m</em>)
   * 
   * @param alpha
   *          α
   * @param gamma
   *          γ
   * @param m
   *          modulus
   * @param n
   *          φ(<em>m</em>) (group order of <em>m</em>)
   * @param x1
   *          <em>x[1]</em> (start value)
   * @param y1
   *          <em>y[1]</em> (start value)
   * @return
   */
  private int pollardRho(final int alpha, final int gamma, final int m,
      final int n, final int x1, final int y1) {
    final int m1 = m / 3, m2 = 2 * m / 3;

    int xi = x1, yi = y1, beta_k = -1;
    int beta_index = 1;

    for (int i = 0; i < 200; i++) {
      x.add(xi);
      y.add(yi);

      int beta_i = (modPow(gamma, xi, m) * modPow(alpha, yi, m)) % m;
      beta.add(beta_i);

      if (beta_i < m1)
        xi = (xi + 1) % n;
      else if (beta_i < m2) {
        xi = (2 * xi) % n;
        yi = (2 * yi) % n;
      } else
        yi = (yi + 1) % n;

      if (beta_k == beta_i)
        break;

      if (i == beta_index - 1) {
        beta_index <<= 1;
        beta_k = beta_i;
      }
    }

    // x_k - x_i ≡ x * (y_i - y_k) mod n
    // a ≡ x * b mod n
    beta_index >>= 1;
    int a = (x.get(beta_index - 1) - x.get(x.size() - 1)) % n;
    if (a < 0)
      a += n;

    int b = (y.get(y.size() - 1) - y.get(beta_index - 1)) % n;
    if (b < 0)
      b += n;

    // calculate x with
    // a ≡ x * b mod n
    // gamma^x ≡ alpha mod m
    congruence[0] = a + " ≡ x * " + b + "  mod " + n;

    if (b % n == 0)
      return -1;

    int[] euclidean = extendedEuclidean(b, n);
    int d = euclidean[0], s = euclidean[1];

    int start = (a / d * s) % n; // start value
    int value = n / d;

    congruence[1] = (a / d) + " ≡ x * " + (b / d) + "  mod " + (n / d);
    congruence[2] = "x = " + (a / d) + " + k * " + (n / d);

    // solution is one of: start + i * value
    for (int i = 0; i < d; i++) {
      int result = (start + i * value) % n;
      if (result < 0)
        result += n;

      if ((modPow(gamma, result, m) - alpha) % m == 0)
        return result;
    }

    return -2;
  }

  /**
   * Calculates the <a href="http://en.wikipedia.org/wiki/Euler_function">Euler
   * φ function</a> for a given number
   */
  static private int phi(int input) {
    int result = 1;

    int input2 = input;
    for (int i = 2; i * i <= input2; i++) {
      int p = 1;
      while (input2 % i == 0) {
        input2 /= i;
        p *= i;
      }
      p /= i;
      if (p != 0)
        result *= p * (i - 1);
    }
    int n = input2 - 1;
    return n == 0 ? result : n * result;
  }

  /**
   * Performs a modular exponentiation, e.g. <code>modPow</code>(<em>b</em>,
   * <em>e</em>, <em>m</em>) = <em>b</em>^<em>e</em> mod <em>m</em>
   * 
   * @param base
   *          non-negative base
   * @param exponent
   *          non-negative exponent
   * @param modulus
   *          positive modulus
   */
  static private int modPow(int base, int exponent, int modulus) {
    int base2 = base;
    if (base2 < 0)
      throw new IllegalArgumentException(
          "base for modular exponentiation may not be negative");
    int exponent2 = exponent;
    if (exponent2 < 0)
      throw new IllegalArgumentException(
          "exponent for modular exponentiation may not be negative");
    if (modulus <= 0)
      throw new IllegalArgumentException(
          "modulus for modular exponentiation may not be zero or negative");

    int result = 1;
    while (exponent2 > 0) {
      if ((exponent2 & 1) == 1)
        result = (result * base2) % modulus;
      exponent2 = exponent2 >> 1;
      base2 = (base2 * base2) % modulus;
    }
    return result;
  }

  /**
   * Calculates the greatest common divisor <em>d</em> of two numbers <em>a</em>
   * and <em>b</em> and two coefficients <em>s</em> and <em>t</em> so that <br>
   * <em>d</em> = gcd(<em>a</em>, <em>b</em>) = <em>s</em> * <em>a</em> +
   * <em>t</em> * <em>b</em>
   * 
   * @return an integer array <code>int[] { d, s, t }</code>
   */
  static private int[] extendedEuclidean(int a, int b) {
    if (b == 0)
      return new int[] { a, 1, 0 };

    int[] result = extendedEuclidean(b, a % b);
    int s = result[1], t = result[2];
    result[1] = t;
    result[2] = s - (a / b) * t;
    return result;
  }

  public String getName() {
    return "Pollard-Rho-Algorithmus zur Berechnung des diskreten Logarithmus";
  }

  public String getAlgorithmName() {
    return "Pollard-Rho-Methode";
  }

  public String getAnimationAuthor() {
    return "Pascal Weisenburger";
  }

  public String getDescription() {
    return "Der Pollard-&rho;-Algorithmus zur Berechnung des diskreten Logarithmus\n"
        + "ist ein Algorithmus zum schnellen L&ouml;sen des Diskreten-Logarithmus-Problems.<br>\n"
        + "<br>\n"
        + "Pollard-&rho;-Methoden sind im Allgemeinen Algorithmen zur Bestimmung der\n"
        + "Periodenl&auml;nge einer Zahlenfolge, die mit einer mathematischen Funktion berechnet wird.\n"
        + "Hierbei werden Folgen von Teilergebnissen berechnet.\n"
        + "Ab einem bestimmten Punkt wiederholt sich ein Teil dieser Teilergebnisse nur noch.\n"
        + "Man kann die Teilergebnisse grafisch so anordnen, dass sich die Gestalt des Buchstaben\n"
        + "&rho; (Rho) erkennen l&auml;sst. Daraus leitet sich die Bezeichnung der Methoden ab.<br>\n"
        + "<br>\n"
        + "<br>\n"
        + "<a href=\"http://de.wikipedia.org/wiki/Pollard-Rho-Methode\">http://de.wikipedia.org/wiki/Pollard-Rho-Methode</a><br>\n"
        + "<a href=\"http://en.wikipedia.org/wiki/Pollard's_rho_algorithm_for_logarithms\">http://en.wikipedia.org/wiki/Pollard's_rho_algorithm_for_logarithms</a>";
  }

  public String getCodeExample() {
    return "   berechne diskreten Logarithmus x f&uuml;r &gamma;^x &equiv; &alpha;  mod m\n"
        + "\n"
        + "1. w&auml;hle drei paarweise disjunkte Teilmengen G[1], G[2] und G[3] von G,\n"
        + "   deren Vereinigung die ganze Gruppe G ist\n"
        + "\n"
        + "2. w&auml;hle Startwert: (x[1], y[1])\n"
        + "\n"
        + "3. berechne:\n"
        + "   \n"
        + "   &beta;[i] = &gamma;^x[i] * &alpha;^y[i]  mod m\n"
        + "   \n"
        + "   x[i+1] = x[i] + 1  mod n  falls &beta;[i] &isin; G[1]\n"
        + "   x[i+1] = 2 * x[i]  mod n  falls &beta;[i] &isin; G[2]\n"
        + "   x[i+1] = x[i]             falls &beta;[i] &isin; G[3]\n"
        + "   \n"
        + "   y[i+1] = y[i]             falls &beta;[i] &isin; G[1]\n"
        + "   y[i+1] = 2 * y[i]  mod n  falls &beta;[i] &isin; G[2]\n"
        + "   y[i+1] = y[i] + 1  mod n  falls &beta;[i] &isin; G[3]\n"
        + "   \n"
        + "   speichere jeweils &beta;[i], x[i] und y[i]\n"
        + "   f&uuml;r alle i = 2^N = 1, 2, 4, 8, 16, ...\n"
        + "   \n"
        + "   vergleiche das aktuelle &beta;[i] mit dem\n"
        + "   gespeicherten bis zwei gleiche &beta; (&beta;[i] und &beta;[k]) gefunden werden\n"
        + "\n"
        + "4. l&ouml;se Kongruenz:\n"
        + "   x[k] - x[i] &equiv; x * (y[i] - y[k])  mod n\n"
        + "\n"
        + "5. suche ein x, das auch die Kongruenzgleichung\n"
        + "   &gamma;^x &equiv; &alpha;  mod m" + "   erf&uuml;llt";
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
    return Generator.PSEUDO_CODE_OUTPUT;
  }
}