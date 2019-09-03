package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Stack;
import java.util.StringTokenizer;

import algoanim.animalscript.AnimalGroupGenerator;
import algoanim.animalscript.AnimalPolylineGenerator;
import algoanim.animalscript.AnimalRectGenerator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.AnimalTextGenerator;
import algoanim.animalscript.AnimalTriangleGenerator;
import algoanim.primitives.Group;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Triangle;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;

public class ShuntingYard implements ValidatingGenerator {
  /* list of available operators */
  private final String               OPERATORS           = "+-*/";
  /* temporary stack that holds operators, functions and brackets */
  private Stack<String>              operatorStack;
  /* String Array for holding expression converted to reversed polish notation */
  private Stack<String>              outputStack;
  /* String Array for holding input expression */
  private String[]                   inputArray;

  private Language                   lang;
  private static String              expression          = "";
  private Color                      queueHighlightColor = new Color(0, 0, 0);
  private Color                      arrowColor          = new Color(0, 0, 0);
  private Color                      queueColor          = new Color(0, 0, 0);
  private Color                      codeHighlight       = new Color(0, 0, 0);
  private Color                      explanationColor    = new Color(0, 0, 0);
  private SourceCodeProperties       codeProperties;
  private PolylineProperties         arrowProperties;
  private RectProperties             rectProperties;
  private TextProperties             textProperties;
  private TextProperties             explanationProperties;
  private SourceCodeProperties       introOutroProperties;
  private Coordinates                pOutput;

  private int                        fromStackCounter;
  private HashMap<String, Primitive> pMap;
  private String                     outputFormatted;
  private String                     input;

  public void init() {
    operatorStack = new Stack<String>();
    outputStack = new Stack<String>();
    pOutput = new Coordinates(60, 350);
    fromStackCounter = 0;
    outputFormatted = "";
    input = "";
    pMap = new HashMap<String, Primitive>();
    lang = new AnimalScript("Shunting-yard algorithm",
        "Janine Hoelscher, Johannes Wagener", 800, 600);
    lang.setStepMode(true);
    codeProperties = new SourceCodeProperties();
    codeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 12));
    codeProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        codeHighlight);
    arrowProperties = new PolylineProperties();
    arrowProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, arrowColor);
    arrowProperties.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    rectProperties = new RectProperties();
    rectProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, queueColor);
    rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 12));
    explanationProperties = new TextProperties();
    explanationProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
        explanationColor);
    explanationProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 14));
    introOutroProperties = new SourceCodeProperties();
    introOutroProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Serif", Font.PLAIN, 15));
    parseExpression(expression);
  }

  protected void introduction() {
    // Titel
    TextProperties titleProperties = new TextProperties();
    titleProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 24));
    Text title = lang.newText(new Coordinates(25, 25),
        "Shunting-yard algorithm", "header", null, titleProperties);

    // Box
    RectProperties rectProperties = new RectProperties();
    rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
    rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    Rect badge = lang.newRect(new Coordinates(20, 15),
        new Coordinates(355, 50), "hRect", null, rectProperties);

    // Group titel and box
    LinkedList<Primitive> titelPrimitives = new LinkedList<Primitive>();
    titelPrimitives.add(title);
    titelPrimitives.add(badge);
    Group titelGroup = new Group(new AnimalGroupGenerator(lang),
        titelPrimitives, "titel");
    pMap.put("titelGroup", titelGroup);

    // Intro
    SourceCode intro = lang.newSourceCode(new Coordinates(30, 60), "intro",
        null, introOutroProperties);
    intro
        .addCodeLine(
            "The shunting-yard algorithm is a method for parsing mathematical expressions",
            "intro", 0, null);
    intro
        .addCodeLine(
            "specified in infix notation to Reverse Polish notation (RPN) or to an abstract syntax tree (AST).",
            "intro", 0, null);
    intro.addCodeLine("", "intro", 0, null);
    intro.addCodeLine("The following functions are assumed:", "intro", 0, null);
    intro.addCodeLine("- recognition of integer numbers", "intro", 0, null);
    intro.addCodeLine("- recognition of operators", "intro", 0, null);
    intro.addCodeLine("- determination of operator associativity", "intro", 0,
        null);
    intro.addCodeLine("- determination of operator precedence", "intro", 0,
        null);
    intro.addCodeLine("", "intro", 0, null);
    intro
        .addCodeLine(
            "The algorithm reads each symbol of the given expression from left to right.",
            "intro", 0, null);
    intro.addCodeLine(
        "If the read symbol is an integer, it is added to the output.",
        "intro", 0, null);
    intro.addCodeLine(
        "If it is an operator, it is pushed onto the operator stack.", "intro",
        0, null);
    intro
        .addCodeLine(
            "If the stack is not empty, the algorithm decides whether the operator is pushed on the",
            "intro", 0, null);
    intro
        .addCodeLine(
            "stack immediately or whether the stack content is added to the output first based on operator",
            "intro", 0, null);
    intro.addCodeLine("associativity and precedence.", "intro", 0, null);
    intro
        .addCodeLine(
            "Since the algorithm outputs Reverse Polish notation, parentheses are not added",
            "intro", 0, null);
    intro.addCodeLine("to the output.", "intro", 0, null);
    intro.addCodeLine("", "intro", 0, null);
    intro
        .addCodeLine(
            "A conflict of right and left associative operators with the same precedence is not handled",
            "intro", 0, null);
    intro.addCodeLine("by the algorithm.", "intro", 0, null);
    lang.nextStep("Introduction");
    intro.hide();
  }

  protected void algo(String expression) {
    AnimalRectGenerator animalRectGenerator = new AnimalRectGenerator(lang);
    AnimalTextGenerator animalTextGenerator = new AnimalTextGenerator(lang);
    AnimalGroupGenerator animalGroupGenerator = new AnimalGroupGenerator(lang);
    AnimalPolylineGenerator animalPolylineGenerator = new AnimalPolylineGenerator(
        lang);

    int totalInputLength = -3;
    for (int i = 0; i < inputArray.length; i++) {
      totalInputLength += 5 + inputArray[i].length() * 7 + 3;
    }

    int radius = 50;
    int upperLine = 50;
    int upperGap = 40;

    Coordinates p1 = new Coordinates(60, 335);
    Coordinates p2 = new Coordinates(60, 385);
    Coordinates p3 = new Coordinates(p1.getX() + totalInputLength, p1.getY());
    Coordinates p4 = new Coordinates(p3.getX() + radius, p3.getY() - radius);
    Coordinates p5 = new Coordinates(p4.getX(), p4.getY() - upperLine);
    Coordinates p6 = new Coordinates(p5.getX() + upperGap, p5.getY());
    Coordinates p7 = new Coordinates(p6.getX(), p4.getY());
    Coordinates p8 = new Coordinates(p7.getX() + radius, p1.getY());
    Coordinates p9 = new Coordinates(p8.getX() + totalInputLength, p1.getY());
    Coordinates p10 = new Coordinates(p9.getX(), p2.getY());
    Coordinates c1 = new Coordinates(p3.getX(), p4.getY());
    Coordinates c2 = new Coordinates(p8.getX(), p7.getY());
    Coordinates pInput = new Coordinates(p8.getX(), 350);

    Coordinates[] c1QuarterCirclePoints = new Coordinates[91];
    for (int i = 0; i <= 90; i++) {
      c1QuarterCirclePoints[i] = getCirclePoint(50, c1.getX(), c1.getY(), (i
          * Math.PI / 180));
    }

    Coordinates[] c2QuarterCirclePoints = new Coordinates[91];
    for (int i = 0; i <= 90; i++) {
      int angle = i + 90;
      c2QuarterCirclePoints[i] = getCirclePoint(50, c2.getX(), c2.getY(),
          (angle * Math.PI / 180));
    }

    // create explanation texts
    Text exp1 = new Text(animalTextGenerator, new Coordinates(120, 450),
        "Condition evaluates to true.", "evalTrue", null, explanationProperties);
    exp1.hide();
    pMap.put("evalTrue", exp1);
    Text exp2 = new Text(animalTextGenerator, new Coordinates(120, 450),
        "Condition evaluates to false.", "evalfalse", null,
        explanationProperties);
    exp2.hide();
    pMap.put("evalFalse", exp2);
    Text exp3 = new Text(animalTextGenerator, new Coordinates(120, 450),
        "All input tokens read.", "allRead", null, explanationProperties);
    exp3.hide();
    pMap.put("allRead", exp3);
    Text exp4 = new Text(animalTextGenerator, new Coordinates(120, 450),
        "Algorithm terminated.", "terminated", null, explanationProperties);
    exp4.hide();
    pMap.put("terminated", exp4);

    int lastUpperX = pInput.getX() - 3;
    int lastElementLength = 0;
    int[] myUpperX = new int[inputArray.length];
    int[] myLength = new int[inputArray.length];
    for (int i = 0; i < inputArray.length; i++) {
      String rectName = "inputRect" + i;
      String textName = "inputText" + i;
      String groupName = "inputGroup" + i;
      myUpperX[i] = lastUpperX + lastElementLength + 3;
      myLength[i] = 5 + inputArray[i].length() * 7;
      LinkedList<Primitive> group = new LinkedList<Primitive>();
      Rect rect = new Rect(animalRectGenerator, new Coordinates(myUpperX[i],
          350), new Coordinates(myUpperX[i] + myLength[i], 370), rectName,
          null, rectProperties);
      pMap.put(rectName, rect);
      group.add(rect);
      group.add(new Text(animalTextGenerator, new Coordinates(myUpperX[i] + 3,
          350), inputArray[i], textName, null, textProperties));
      pMap.put(groupName, new Group(animalGroupGenerator, group, groupName));
      lastUpperX = myUpperX[i];
      lastElementLength = myLength[i];
    }

    Coordinates[] toStackPolylinePoints = new Coordinates[93];
    toStackPolylinePoints[0] = p9;
    toStackPolylinePoints[92] = p6;
    for (int i = 1; i < 92; i++) {
      toStackPolylinePoints[i] = c2QuarterCirclePoints[i - 1];
    }

    Coordinates[] fromStackPolylinePoints = new Coordinates[93];
    fromStackPolylinePoints[0] = p5;
    fromStackPolylinePoints[92] = p1;
    for (int i = 1; i < 92; i++) {
      fromStackPolylinePoints[i] = c1QuarterCirclePoints[i - 1];
    }

    Coordinates[] toOutputPoints = { p10, p2 };

    pMap.put("toStackPolyline", new Polyline(new AnimalPolylineGenerator(lang),
        toStackPolylinePoints, "toStackPolyline", null, arrowProperties));
    pMap.put("fromStackPolyline", new Polyline(
        new AnimalPolylineGenerator(lang), fromStackPolylinePoints,
        "fromStackPolyline", null, arrowProperties));
    pMap.put("toOutput", new Polyline(new AnimalPolylineGenerator(lang),
        toOutputPoints, "toOutput", null, arrowProperties));

    // show source code
    Coordinates sourceCodeCoordinate;
    if (p9.getX() < 355) {
      sourceCodeCoordinate = new Coordinates(400, 30);
    } else {
      sourceCodeCoordinate = new Coordinates(p9.getX() + 60, 30);
    }
    SourceCode code = lang.newSourceCode(sourceCodeCoordinate, "code", null,
        codeProperties);
    code.addCodeLine("WHILE there are tokens to be read:", "code0", 0, null);
    code.addCodeLine("   Read a token.", "code1", 0, null);
    code.addCodeLine("   IF token is number:", "code2", 0, null);
    code.addCodeLine("      add it to output queue.", "code3", 0, null);
    code.addCodeLine("   IF token is operator, o1:", "code4", 0, null);
    code.addCodeLine(
        "      WHILE there is an operator token, o2, at top of stack, and",
        "code5", 0, null);
    code.addCodeLine(
        "       either o1 is left-associative and its precedence is equal to that of o2,",
        "code6", 0, null);
    code.addCodeLine("       or o1 has precedence less than that of o2:",
        "code7", 0, null);
    code.addCodeLine("         pop o2 off stack, onto the output queue.",
        "code8", 0, null);
    code.addCodeLine("      push o1 onto stack.", "code9", 0, null);
    code.addCodeLine("   IF token is left parenthesis:", "code10", 0, null);
    code.addCodeLine("      push it onto stack.", "code11", 0, null);
    code.addCodeLine("   IF token is right parenthesis:", "code12", 0, null);
    code.addCodeLine(
        "      WHILE token at top of stack is no left parenthesis:", "code13",
        0, null);
    code.addCodeLine("         pop operators off stack onto output queue.",
        "code14", 0, null);
    code.addCodeLine("         IF stack is empty:", "code15", 0, null);
    code.addCodeLine("            ERROR: mismatched parentheses.", "code16", 0,
        null);
    code.addCodeLine(
        "      pop left parenthesis from stack, but not onto output queue.",
        "code17", 0, null);
    code.addCodeLine("When there are no more tokens to read:", "code18", 0,
        null);
    code.addCodeLine("   WHILE stack is not empty:", "code19", 0, null);
    code.addCodeLine("      IF operator token on top of stack is parenthesis:",
        "code20", 0, null);
    code.addCodeLine("         ERROR: mismatched parentheses.", "code21", 0,
        null);
    code.addCodeLine("      Pop operator onto output queue.", "code22", 0, null);
    code.addCodeLine("Exit.", "code23", 0, null);

    TextProperties stackTextProperties = new TextProperties();
    stackTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 13));
    new Text(animalTextGenerator, new Coordinates(p5.getX() - 115,
        p5.getY() - 25), "Operator Stack:", "opStack", null,
        stackTextProperties);

    PolylineProperties pp = new PolylineProperties();
    Coordinates[] ca = { new Coordinates(p5.getX() + 21, p5.getY() - 40),
        new Coordinates(p5.getX() + 21, p5.getY() - 70) };
    Polyline stackMarkerLine = new Polyline(animalPolylineGenerator, ca,
        "stackMarkerLine", null, pp);

    TriangleProperties tp = new TriangleProperties();
    tp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    tp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
    Triangle stackMarkerTriangle = new Triangle(new AnimalTriangleGenerator(
        lang), new Coordinates(p5.getX() + 21, p5.getY() - 30),
        new Coordinates(p5.getX() + 15, p5.getY() - 40), new Coordinates(
            p5.getX() + 27, p5.getY() - 40), "stackMarkerTriangle", null, tp);

    TextProperties stackMarkerTextProperties = new TextProperties();
    stackMarkerTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("Monospaced", Font.ITALIC, 12));
    Text stackMarkerText = new Text(animalTextGenerator, new Coordinates(
        p5.getX() + 11, p5.getY() - 90), "top", "stackMarkerText", null,
        stackMarkerTextProperties);

    LinkedList<Primitive> stackMarkerPrimitives = new LinkedList<Primitive>();
    stackMarkerPrimitives.add(stackMarkerText);
    stackMarkerPrimitives.add(stackMarkerTriangle);
    stackMarkerPrimitives.add(stackMarkerLine);
    Group stackMarker = new Group(animalGroupGenerator, stackMarkerPrimitives,
        "stackMarker");
    stackMarker.hide();

    lang.nextStep("Algorithm");

    LinkedList<Primitive> operatorsOnStack = new LinkedList<Primitive>();
    int polyCounter = 0;
    int operatorGroupCounter = 0;
    boolean stackMarkerVisible = false;
    boolean executeLastWhile = true;

    readInput: for (int i = 0; i < inputArray.length; i++) {
      String token = inputArray[i];

      code.highlight(0);
      pMap.get("evalTrue").show();
      lang.nextStep();
      pMap.get("evalTrue").hide();
      code.unhighlight(0);
      code.highlight(1);
      pMap.get("inputRect" + i).changeColor(
          AnimationPropertiesKeys.FILL_PROPERTY, queueHighlightColor, null,
          null);
      lang.nextStep();
      code.unhighlight(1);
      code.highlight(2);
      if (isNumber(token)) {
        pMap.get("evalTrue").show();
      } else {
        pMap.get("evalFalse").show();
      }
      lang.nextStep();

      if (isNumber(token)) {
        outputStack.push(token + " ");

        pMap.get("evalTrue").hide();
        code.unhighlight(2);
        code.highlight(3);
        Polyline p = getPolyline(i, 3, myUpperX, myLength,
            c1QuarterCirclePoints, c2QuarterCirclePoints, p5, p6);
        lang.addLine("hide \"toOutput" + i + "\"");
        pMap.get("inputGroup" + i).moveVia(null, null, p, new MsTiming(0),
            new MsTiming(1000));
        lang.addLine("hide \"toOutput" + i + "\"");
        pMap.get("inputRect" + i).changeColor(
            AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE,
            new MsTiming(1000), null);
        lang.nextStep();
        code.unhighlight(3);
        continue;
      } else {
        code.unhighlight(2);
        code.highlight(4);
        if (isOperator(token)) {
          pMap.get("evalFalse").hide();
          pMap.get("evalTrue").show();
        }
        lang.nextStep();
      }
      if (isOperator(token)) {
        if (!(!operatorStack.empty() && isOperator(operatorStack.peek()) && ((isLeftAssociative(token) && isPrecedenceEqual(
            token, operatorStack.peek())) || isPrecedenceLess(token,
            operatorStack.peek())))) {
          pMap.get("evalTrue").hide();
          pMap.get("evalFalse").show();
        }
        code.unhighlight(4);
        code.highlight(5);
        code.highlight(6);
        code.highlight(7);
        lang.nextStep();
        while (!operatorStack.empty()
            && isOperator(operatorStack.peek())
            && ((isLeftAssociative(token) && isPrecedenceEqual(token,
                operatorStack.peek())) || isPrecedenceLess(token,
                operatorStack.peek()))) {
          outputStack.push(operatorStack.pop() + " ");

          Group stackTop = (Group) operatorsOnStack.removeFirst();
          int indexOfstackTop = Integer.parseInt(stackTop.getName().substring(
              10));
          Polyline p = getPolyline(indexOfstackTop, 2, myUpperX, myLength,
              c1QuarterCirclePoints, c2QuarterCirclePoints, p5, p6);
          lang.addLine("hide \"fromStack" + fromStackCounter + "\"");
          stackTop.moveVia(null, null, p, new MsTiming(0), new MsTiming(1000));
          lang.addLine("hide \"fromStack" + fromStackCounter + "\"");
          if (!operatorsOnStack.isEmpty()) {
            Group operatorGroup = new Group(animalGroupGenerator,
                operatorsOnStack, "operatorGroup" + operatorGroupCounter);
            Coordinates[] co = { new Coordinates(50, 10),
                new Coordinates(50 - myLength[indexOfstackTop] - 3, 10) };
            Polyline s = new Polyline(animalPolylineGenerator, co, "poly"
                + polyCounter, null, arrowProperties);
            lang.addLine("hide \"poly" + polyCounter + "\"");
            operatorGroup.moveVia(null, null, s, new MsTiming(200),
                new MsTiming(800));
            lang.addLine("hide \"poly" + polyCounter + "\"");
            polyCounter++;
            operatorGroupCounter++;
          } else {
            stackMarker.hide(new MsTiming(1000));
            stackMarkerVisible = false;
          }
          fromStackCounter++;
          code.unhighlight(5);
          code.unhighlight(6);
          code.unhighlight(7);
          code.highlight(8);
          pMap.get("evalTrue").hide();
          pMap.get("inputRect" + indexOfstackTop).changeColor(
              AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE,
              new MsTiming(1000), null);
          lang.nextStep();
          code.unhighlight(8);
          code.highlight(5);
          code.highlight(6);
          code.highlight(7);
          if (!(!operatorStack.empty() && isOperator(operatorStack.peek()) && ((isLeftAssociative(token) && isPrecedenceEqual(
              token, operatorStack.peek())) || isPrecedenceLess(token,
              operatorStack.peek())))) {
            pMap.get("evalFalse").show();
          }
          lang.nextStep();
        }
        operatorStack.push(token);

        code.unhighlight(5);
        code.unhighlight(6);
        code.unhighlight(7);
        pMap.get("evalFalse").hide();

        if (!stackMarkerVisible) {
          stackMarker.show(new MsTiming(1000));
          stackMarkerVisible = true;
        }
        Polyline p = getPolyline(i, 1, myUpperX, myLength,
            c1QuarterCirclePoints, c2QuarterCirclePoints, p5, p6);
        lang.addLine("hide \"toStack" + i + "\"");
        pMap.get("inputGroup" + i).moveVia(null, null, p, new MsTiming(0),
            new MsTiming(1000));
        lang.addLine("hide \"toStack" + i + "\"");
        if (!operatorsOnStack.isEmpty()) {
          Group operatorGroup = new Group(animalGroupGenerator,
              operatorsOnStack, "operatorGroup" + operatorGroupCounter);
          Coordinates[] co = { new Coordinates(10, 10),
              new Coordinates(10 + myLength[i] + 3, 10) };
          Polyline s = new Polyline(animalPolylineGenerator, co, "poly"
              + polyCounter, null, arrowProperties);
          lang.addLine("hide \"poly" + polyCounter + "\"");
          operatorGroup.moveVia(null, null, s, new MsTiming(0), new MsTiming(
              800));
          lang.addLine("hide \"poly" + polyCounter + "\"");
          polyCounter++;
          operatorGroupCounter++;
        }
        operatorsOnStack.addFirst(pMap.get("inputGroup" + i));
        code.highlight(9);
        pMap.get("inputRect" + i).changeColor(
            AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE,
            new MsTiming(1000), null);
        lang.nextStep();
        code.unhighlight(9);
        continue;
      } else {
        code.unhighlight(4);
        code.highlight(10);
        if (token.equals("(")) {
          pMap.get("evalFalse").hide();
          pMap.get("evalTrue").show();
        }
        lang.nextStep();
      }
      if (token.equals("(")) {
        operatorStack.push(token);

        pMap.get("evalTrue").hide();
        code.unhighlight(10);
        code.highlight(11);
        if (!stackMarkerVisible) {
          stackMarker.show(new MsTiming(1000));
          stackMarkerVisible = true;
        }
        Polyline p = getPolyline(i, 1, myUpperX, myLength,
            c1QuarterCirclePoints, c2QuarterCirclePoints, p5, p6);
        lang.addLine("hide \"toStack" + i + "\"");
        pMap.get("inputGroup" + i).moveVia(null, null, p, new MsTiming(0),
            new MsTiming(1000));
        lang.addLine("hide \"toStack" + i + "\"");
        if (!operatorsOnStack.isEmpty()) {
          Group operatorGroup = new Group(animalGroupGenerator,
              operatorsOnStack, "operatorGroup" + operatorGroupCounter);
          Coordinates[] co = { new Coordinates(10, 10),
              new Coordinates(10 + myLength[i] + 3, 10) };
          Polyline s = new Polyline(animalPolylineGenerator, co, "poly"
              + polyCounter, null, arrowProperties);
          lang.addLine("hide \"poly" + polyCounter + "\"");
          operatorGroup.moveVia(null, null, s, new MsTiming(0), new MsTiming(
              800));
          lang.addLine("hide \"poly" + polyCounter + "\"");
          operatorGroupCounter++;
          polyCounter++;
        }
        operatorsOnStack.addFirst(pMap.get("inputGroup" + i));
        pMap.get("inputRect" + i).changeColor(
            AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE,
            new MsTiming(1000), null);
        lang.nextStep();
        code.unhighlight(11);
        continue;
      } else {
        code.unhighlight(10);
        code.highlight(12);
        pMap.get("evalFalse").hide();
        pMap.get("evalTrue").show();
        lang.nextStep();
      }
      if (token.equals(")")) {
        code.unhighlight(12);
        code.highlight(13);
        if (operatorStack.peek().equals("(")) {
          pMap.get("evalFalse").show();
          pMap.get("evalTrue").hide();
        }
        lang.nextStep();
        while (!operatorStack.peek().equals("(")) {
          outputStack.push(operatorStack.pop() + " ");

          Group stackTop = (Group) operatorsOnStack.removeFirst();
          int indexOfstackTop = Integer.parseInt(stackTop.getName().substring(
              10));
          Polyline p = getPolyline(indexOfstackTop, 2, myUpperX, myLength,
              c1QuarterCirclePoints, c2QuarterCirclePoints, p5, p6);
          lang.addLine("hide \"fromStack" + fromStackCounter + "\"");
          stackTop.moveVia(null, null, p, new MsTiming(0), new MsTiming(1000));
          lang.addLine("hide \"fromStack" + fromStackCounter + "\"");
          if (!operatorsOnStack.isEmpty()) {
            Group operatorGroup = new Group(animalGroupGenerator,
                operatorsOnStack, "operatorGroup" + operatorGroupCounter);
            Coordinates[] co = { new Coordinates(50, 10),
                new Coordinates(50 - myLength[indexOfstackTop] - 3, 10) };
            Polyline s = new Polyline(animalPolylineGenerator, co, "poly"
                + polyCounter, null, arrowProperties);
            lang.addLine("hide \"poly" + polyCounter + "\"");
            operatorGroup.moveVia(null, null, s, new MsTiming(200),
                new MsTiming(800));
            lang.addLine("hide \"poly" + polyCounter + "\"");
            polyCounter++;
            operatorGroupCounter++;
          } else {
            stackMarker.hide(new MsTiming(1000));
            stackMarkerVisible = false;
          }
          fromStackCounter++;
          code.unhighlight(13);
          pMap.get("evalTrue").hide();
          code.highlight(14);
          lang.nextStep();
          code.unhighlight(14);
          code.highlight(15);
          if (operatorStack.empty()) {
            pMap.get("evalTrue").show();
          } else {
            pMap.get("evalFalse").show();
          }
          lang.nextStep();

          if (operatorStack.empty()) {
            code.unhighlight(15);
            pMap.get("evalTrue").hide();
            code.highlight(16);
            lang.nextStep();
            code.unhighlight(16);
            executeLastWhile = false;
            break readInput;
          } else {
            code.unhighlight(15);
            if (!operatorStack.peek().equals("(")) {
              pMap.get("evalFalse").hide();
              pMap.get("evalTrue").show();
            }
            code.highlight(13);
            lang.nextStep();
          }
        }
        operatorStack.pop(); // pop the left parenthesis from the stack,
        // but not onto the output queue

        code.unhighlight(13);
        pMap.get("evalFalse").hide();
        code.highlight(17);

        Group stackTop = (Group) operatorsOnStack.removeFirst();
        int indexOfstackTop = Integer
            .parseInt(stackTop.getName().substring(10));
        stackTop.hide(new MsTiming(200));
        if (!operatorsOnStack.isEmpty()) {
          Group operatorGroup = new Group(animalGroupGenerator,
              operatorsOnStack, "operatorGroup" + operatorGroupCounter);
          Coordinates[] co = { new Coordinates(50, 10),
              new Coordinates(50 - myLength[indexOfstackTop] - 3, 10) };
          Polyline s = new Polyline(animalPolylineGenerator, co, "poly"
              + polyCounter, null, arrowProperties);
          lang.addLine("hide \"poly" + polyCounter + "\"");
          operatorGroup.moveVia(null, null, s, new MsTiming(200), new MsTiming(
              800));
          lang.addLine("hide \"poly" + polyCounter + "\"");
          polyCounter++;
          operatorGroupCounter++;
        } else {
          stackMarker.hide(new MsTiming(600));
          stackMarkerVisible = false;
        }
        // hide ")" also
        pMap.get("inputGroup" + i).hide(new MsTiming(200));
        lang.nextStep();
        code.unhighlight(17);
        continue;
      } else {
        code.unhighlight(12);
      }
    }
    // all tokens of input read at this Point
    if (executeLastWhile) {
      code.highlight(0);
      pMap.get("evalFalse").show();
      lang.nextStep();
      pMap.get("evalFalse").hide();
      code.unhighlight(0);
      code.highlight(18);
      pMap.get("allRead").show();
      lang.nextStep();
      code.unhighlight(18);
      pMap.get("allRead").hide();
      code.highlight(19);
      if (!operatorStack.empty()) {
        pMap.get("evalTrue").show();
      } else {
        pMap.get("evalFalse").show();
      }
      lang.nextStep();
      code.unhighlight(19);
      while (!operatorStack.empty()) {
        code.highlight(20);
        if (!(operatorStack.peek().equals("(") || operatorStack.peek().equals(
            ")"))) {
          pMap.get("evalTrue").hide();
          pMap.get("evalFalse").show();
        }
        lang.nextStep();
        if (operatorStack.peek().equals("(")
            || operatorStack.peek().equals(")")) {
          pMap.get("evalTrue").hide();
          code.unhighlight(20);
          code.highlight(21);
          lang.nextStep();
          code.unhighlight(21);
          break;
        } else {
          outputStack.push(operatorStack.pop() + " ");

          pMap.get("evalFalse").hide();
          code.unhighlight(20);
          code.highlight(22);
          Group stackTop = (Group) operatorsOnStack.removeFirst();
          int indexOfstackTop = Integer.parseInt(stackTop.getName().substring(
              10));
          Polyline p = getPolyline(indexOfstackTop, 2, myUpperX, myLength,
              c1QuarterCirclePoints, c2QuarterCirclePoints, p5, p6);
          lang.addLine("hide \"fromStack" + fromStackCounter + "\"");
          stackTop.moveVia(null, null, p, new MsTiming(0), new MsTiming(1000));
          lang.addLine("hide \"fromStack" + fromStackCounter + "\"");
          if (!operatorsOnStack.isEmpty()) {
            Group operatorGroup = new Group(animalGroupGenerator,
                operatorsOnStack, "operatorGroup" + operatorGroupCounter);
            Coordinates[] co = { new Coordinates(50, 10),
                new Coordinates(50 - myLength[indexOfstackTop] - 3, 10) };
            Polyline s = new Polyline(animalPolylineGenerator, co, "poly"
                + polyCounter, null, arrowProperties);
            lang.addLine("hide \"poly" + polyCounter + "\"");
            operatorGroup.moveVia(null, null, s, new MsTiming(200),
                new MsTiming(800));
            lang.addLine("hide \"poly" + polyCounter + "\"");
            polyCounter++;
            operatorGroupCounter++;
          } else {
            stackMarker.hide(new MsTiming(1000));
            stackMarkerVisible = false;
          }
          fromStackCounter++;
          lang.nextStep();
          code.unhighlight(22);
          code.highlight(19);
          if (!operatorStack.empty()) {
            pMap.get("evalTrue").show();
          } else {
            pMap.get("evalFalse").show();
          }
          lang.nextStep();
          code.unhighlight(19);
        }
      }
    }
    pMap.get("evalFalse").hide();
    code.highlight(23);
    pMap.get("terminated").show();
    lang.nextStep();
    lang.hideAllPrimitives();
    pMap.get("titelGroup").show();

    // convert outputStack to output String
    Collections.reverse(outputStack);
    while (!outputStack.empty()) {
      outputFormatted += outputStack.pop();
    }
  }

  private void conclusion() {
    SourceCode conclusion = lang.newSourceCode(new Coordinates(30, 60),
        "conclusion", null, introOutroProperties);
    conclusion.addCodeLine("The input expression was:", "conclusion", 0, null);
    conclusion.addCodeLine("	" + input, "conclusion", 0, null);
    conclusion.addCodeLine("", "conclusion", 0, null);
    conclusion.addCodeLine("The output of the Shunting-Yard algorithm is:",
        "conclusion", 0, null);
    conclusion.addCodeLine("	" + outputFormatted, "conclusion", 0, null);
    conclusion.addCodeLine("", "conclusion", 0, null);
    conclusion.addCodeLine("", "conclusion", 0, null);
    conclusion.addCodeLine(
        "The running time complexity of this algorithm is O(n).", "conclusion",
        0, null);
    conclusion
        .addCodeLine(
            "That is because each token will be read once, each number, or operator will be printed once,",
            "conclusion", 0, null);
    conclusion
        .addCodeLine(
            "and each operator, or parenthesis will be pushed onto the stack and popped off the stack once.",
            "conclusion", 0, null);
    conclusion
        .addCodeLine(
            "Therefore, there are at most a constant number of operations executed per token, so the running time is",
            "conclusion", 0, null);
    conclusion.addCodeLine("linear to the the size of the input.",
        "conclusion", 0, null);
    conclusion.addCodeLine("", "conclusion", 0, null);
    conclusion
        .addCodeLine(
            "The shunting yard algorithm can also be applied to produce prefix notation (also known as polish notation).",
            "conclusion", 0, null);
    conclusion
        .addCodeLine(
            "To do this one would simply start from the end of the tokenized string and work backwards,",
            "conclusion", 0, null);
    conclusion
        .addCodeLine(
            "reverse the output queue (therefore making the output queue an output stack)",
            "conclusion", 0, null);
    conclusion.addCodeLine("and flip the left and right parenthesis behavior.",
        "conclusion", 0, null);
    lang.nextStep("Conclusion");
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    validateInput(props, primitives);
    expression = (String) primitives.get("expression");
    queueHighlightColor = (Color) primitives.get("queueHighlightColor");
    arrowColor = (Color) primitives.get("arrowColor");
    queueColor = (Color) primitives.get("queueColor");
    codeHighlight = (Color) primitives.get("codeHighlight");
    explanationColor = (Color) primitives.get("explanationColor");
    init();
    introduction();
    algo(expression);
    conclusion();
    return lang.toString();
  }

  public String getName() {
    return "Shunting-yard algorithm";
  }

  public String getAlgorithmName() {
    return "Shunting-yard algorithm";
  }

  public String getAnimationAuthor() {
    return "Janine Hölscher, Johannes Wagener";
  }

  public String getDescription() {
    return "The shunting&#45;yard algorithm is a method for parsing mathematical expressions specified in"
        + "\n"
        + "infix notation&#46; It can be used to produce output in Reverse Polish notation &#40;RPN&#41; or as an abstract syntax tree &#40;AST&#41;&#46;"
        + "\n"
        + "The algorithm was invented by Edsger Dijkstra and published in 1961&#46;"
        + "\n"
        + "It was named 'shunting&#45;yard' algorithm because Dijkstra stated in his publication that 'the translation process shows"
        + "\n"
        + "much resemblance to shunting at a three way railroad junction'&#46;"
        + "\n";
  }

  public String getCodeExample() {
    return ("IF the token is an operator&#44; o1&#44; then&#58;"
        + "\n"
        + "   WHILE there is an operator token&#44; o2&#44; at the top of the operator stack&#44; and"
        + "\n"
        + "    either o1 is left&#45;associative and its precedence is equal to that of o2&#44;"
        + "\n" + "    or o1 has precedence less than that of o2&#58;" + "\n"
        + "      pop o2 off the operator stack&#44; onto the output queue&#46;"
        + "\n" + "   push o1 onto the operator stack&#46;" + "\n" + "\n");

  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  private Coordinates getCirclePoint(int radius, int originX, int originY,
      double angle) {
    long x = Math.round(originX + radius * Math.cos(angle));
    long y = Math.round(originY + radius * Math.sin(angle));
    return new Coordinates((int) x, (int) y);
  }

  private Polyline getPolyline(int elementIndex, int action, int[] myUpperX,
      int[] myLength, Coordinates[] c1QuarterCirclePoints,
      Coordinates[] c2QuarterCirclePoints, Coordinates p5, Coordinates p6) {
    switch (action) {
      case 1: {
        int distance = myLength[elementIndex] + 13;
        Coordinates[] toStack = new Coordinates[93];
        toStack[0] = new Coordinates(myUpperX[elementIndex], 335);
        toStack[92] = new Coordinates(p6.getX() - distance, p6.getY() - 40);
        for (int j = 1; j < 92; j++) {
          toStack[j] = new Coordinates(c2QuarterCirclePoints[j - 1].getX()
              - distance, c2QuarterCirclePoints[j - 1].getY());
        }
        Polyline poly = new Polyline(new AnimalPolylineGenerator(lang),
            toStack, "toStack" + elementIndex, null, arrowProperties);
        return poly;
      }
      case 2: {
        int distance = 40 - myLength[elementIndex] - 13;
        Coordinates[] toStack = new Coordinates[93];
        toStack[0] = new Coordinates(p5.getX() + distance, p5.getY() - 40);
        toStack[92] = new Coordinates(pOutput.getX(), 335);
        for (int j = 1; j < 92; j++) {
          toStack[j] = new Coordinates(c1QuarterCirclePoints[j - 1].getX()
              + distance, c1QuarterCirclePoints[j - 1].getY());
        }
        Polyline poly = new Polyline(new AnimalPolylineGenerator(lang),
            toStack, "fromStack" + fromStackCounter, null, arrowProperties);
        pOutput = new Coordinates(pOutput.getX() + myLength[elementIndex] + 3,
            pOutput.getY());
        return poly;
      }
      case 3: {
        Coordinates[] toStack = { new Coordinates(myUpperX[elementIndex], 350),
            pOutput };
        Polyline poly = new Polyline(new AnimalPolylineGenerator(lang),
            toStack, "toOutput" + elementIndex, null, arrowProperties);
        pOutput = new Coordinates(pOutput.getX() + myLength[elementIndex] + 3,
            pOutput.getY());
        return poly;
      }
      default:
        return null;
    }
  }

  private void parseExpression(String expression) {
    // remove possible spaces
    String exp = expression.replace(" ", "");
    StringTokenizer tempStore = new StringTokenizer(exp, OPERATORS + "()", true);
    inputArray = new String[tempStore.countTokens()];
    int counter = 0;
    while (tempStore.hasMoreTokens()) {
      String nextToken = tempStore.nextToken();
      inputArray[counter] = nextToken;
      input += nextToken;
      counter++;
    }
  }

  private boolean isNumber(String token) {
    try {
      Integer.parseInt(token);
    } catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  private boolean isOperator(String token) {
    return OPERATORS.contains(token);
  }

  private boolean isLeftAssociative(String token) {
    // was: switch (token) {
    switch (token.charAt(0)) {
      case '/':
        return true;
      case '*':
        return true;
      case '+':
        return true;
      case '-':
        return true;
      default:
        return false;
    }
  }

  private boolean isPrecedenceEqual(String token1, String token2) {
    if ((token1.equals("/") && token2.equals("*"))
        || (token1.equals("*") && token2.equals("/"))
        || (token1.equals("+") && token2.equals("-"))
        || (token1.equals("-") && token2.equals("+"))) {
      return true;
    } else {
      return false;
    }
  }

  private boolean isPrecedenceLess(String token1, String token2) {
    if ((token1.equals("+") || token1.equals("-"))
        && (token2.equals("/") || token2.equals("*"))) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) throws IllegalArgumentException {
    String the_expression = (String) arg1.get("expression");
    if (the_expression.isEmpty()) {
      throw new IllegalArgumentException("The expression can not be empty.");
    } else {
      the_expression.replace(" ", "");
      String[] exp = the_expression.split("");
      for (int i = 1; i < exp.length; i++) {
        if (!exp[i].matches("[0-9\\(\\)\\+\\-\\*\\/]")) {
          throw new IllegalArgumentException(
              "The expression can only contain the following characters: 0-9, (, ), +, -, *, /");
        }
      }
    }
    return true;
  }
}