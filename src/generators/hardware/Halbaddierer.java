package generators.hardware;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.VHDLLanguage;
import algoanim.primitives.vhdl.VHDLElement;
import algoanim.primitives.vhdl.VHDLPin;
import algoanim.primitives.vhdl.VHDLPinType;
import algoanim.primitives.vhdl.VHDLWire;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.VHDLWireProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class Halbaddierer implements Generator {
  private VHDLLanguage         lang;
  private SourceCode           sc;
  private SourceCodeProperties sourceCodeProps;

  private TextProperties       txtProp;

  int                          Anzahl  = 0;
  char                         SUM     = '1';
  char                         Cout    = '1';
  VHDLElement                  elem1, elem2;
  VHDLWire                     wire1, wire2, wire3, wire4, wire5, wire6, wire7,
      wire8, wire9, wire10;

  Vector<VHDLPin>              pinsXor = new Vector<VHDLPin>(3);
  Vector<VHDLPin>              pinsAnd = new Vector<VHDLPin>(3);
  VHDLPin                      SUMPin  = new VHDLPin(VHDLPinType.OUTPUT,
                                           " SUM ", VHDLPin.VALUE_NOT_DEFINED);
  VHDLPin                      CoutPin = new VHDLPin(VHDLPinType.OUTPUT,
                                           " Cout ", VHDLPin.VALUE_NOT_DEFINED);
  VHDLPin                      APin    = new VHDLPin(VHDLPinType.INPUT, " A ",
                                           VHDLPin.VALUE_NOT_DEFINED);
  VHDLPin                      BPin    = new VHDLPin(VHDLPinType.INPUT, "B ",
                                           VHDLPin.VALUE_NOT_DEFINED);

  public void init() {
    lang = new AnimalScript("Halbaddierer", "Golsa Arashloozadeh", 640, 480);
    lang.setStepMode(true);

  }

  public void init(VHDLLanguage lang) {
    this.lang = lang;
  }

  public void HauptxorandErzeuger(char[] pinArray,
      Coordinates Coordinates,// Eingänge und Ausgnge ohne Wert
      Coordinates Coordinates2, Coordinates xCoordEntity,
      Coordinates yCoordEntity, String name) {
    showSourceCode();
    setTitle();
    entityRechteck(xCoordEntity, yCoordEntity, "Halbaddierer");
    sc.highlight(2);
    sc.highlight(3);
    sc.highlight(4);
    lang.nextStep();
    sc.unhighlight(2);
    sc.unhighlight(3);
    sc.unhighlight(4);
    xorandErzeuger1(Coordinates,// Eingänge und Ausgnge ohne Wert
        Coordinates2);
    Buendelung(xCoordEntity, yCoordEntity);
    lang.nextStep();
    xorandErzeuger2(pinArray, Coordinates,// Eingänge mit dem Wert aber Ausgänge
                                          // ohne
        Coordinates2);

    lang.nextStep();

    xorandErzeuger3(pinArray, Coordinates, // Eingänge mit dem Wert aber
                                           // Ausgänge ohne
        Coordinates2);
    sc.highlight(8);
    sc.highlight(9);

    lang.nextStep();
    sc.unhighlight(8);
    sc.unhighlight(9);

  }

  public void xorandErzeuger1(Coordinates Coordinates,// Eingänge und Ausgnge
                                                      // ohne Wert
      Coordinates Coordinates2) {

    pinsXor.add(APin);
    pinsXor.add(BPin);
    pinsXor.add(SUMPin);
    VHDLElement elem1 = lang.newXOrGate(Coordinates, 80, 0, "myXOr", pinsXor,
        null);
    pinsAnd.add(APin);
    pinsAnd.add(BPin);
    pinsAnd.add(CoutPin);
    VHDLElement elem2 = lang.newAndGate(Coordinates2, 80, 0, "myAnd", pinsAnd,
        null);
    wireErzeuger(elem1, elem2);
  }

  public void xorandErzeuger2(char[] pinArray, Coordinates Coordinates,
  // Eingänge mit dem Wert aber Ausgänge ohne
      Coordinates Coordinates2) {
    if (pinArray[0] != '0' && pinArray[0] != '1') {
      APin.setValue('x');
      System.out.println("haha");
      if (pinArray[1] != '0' && pinArray[1] != '1')
        BPin.setValue('x');

      else
        BPin.setValue(pinArray[1]);
    } else {
      APin.setValue(pinArray[0]);
      BPin.setValue(pinArray[1]);
    }
    elem1 = lang.newXOrGate(Coordinates, 80, 0, "myXOr", pinsXor, null);
    elem2 = lang.newAndGate(Coordinates2, 80, 0, "myAnd", pinsAnd, null);

  }

  // /Eingänge und Ausgänge mit dem Wert

  public void xorandErzeuger3(char[] pinArray, Coordinates Coordinates,// Eingänge
                                                                       // und
                                                                       // Ausgnge
                                                                       // ohne
                                                                       // Wert
      Coordinates Coordinates2) {
    // VHDLElement[] VHDLArray;
    if ((APin.getValue() == 'x' || BPin.getValue() == 'x')) {
      SUMPin.setValue('x');
      CoutPin.setValue('x');
      elem1 = lang.newXOrGate(Coordinates, 80, 0, "myXOr", pinsXor, null);
      elem2 = lang.newAndGate(Coordinates2, 80, 0, "myAnd", pinsAnd, null);
      // return;
      // VHDLArray= new VHDLElement[]{elem1,elem2};

      // return VHDLArray;
    }

    else {
      if ((APin.getValue() == '0' && BPin.getValue() == '0')
          || (APin.getValue() == '1' && BPin.getValue() == '1'))

        SUM = '0';

      SUMPin.setValue(SUM);// XorAusgang gesetzt
      if (APin.getValue() == '0' || BPin.getValue() == '0')
        Cout = '0';

      CoutPin.setValue(Cout);// And Ausgang gesetzt

      elem1 = lang.newXOrGate(Coordinates, 80, 0, "myXOr", pinsXor, null);
      elem2 = lang.newAndGate(Coordinates2, 80, 0, "myAnd", pinsAnd, null);
      // VHDLArray= new VHDLElement[]{elem1,elem2};

      // return VHDLArray;
    }
  }

  private void setText(Offset Offset, String text, String name, int Groesse,
      Color color) {
    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, Groesse));
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
    lang.newText(Offset, text, name, null, textProperties);
  }

  public void entityRechteck(Coordinates xCoordEntity,
      Coordinates yCoordEntity, String name) {

    Rect rect = lang.newRect(xCoordEntity, yCoordEntity, "entity", null);
    setText(new Offset(0, -40, rect, AnimalScript.DIRECTION_NW), name,
        " name ", 12, Color.GRAY);

    VHDLWireProperties wireProps = new VHDLWireProperties();
    wireProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
    List<Node> nodes1 = new Vector<Node>(0, 29);// ?????????????
    List<Node> nodes2 = new Vector<Node>(0, 55);
    List<Node> nodes3 = new Vector<Node>(0, 55);
    List<Node> nodes4 = new Vector<Node>(0, 55);

    nodes1.add(new Offset(-32, 49, rect, AnimalScript.DIRECTION_NW));
    nodes1.add(new Offset(32, 49, rect, AnimalScript.DIRECTION_NW));
    wire1 = lang.newWire(nodes1, 0, "WireA", null, wireProps);
    setText(new Offset(-12, 49, rect, AnimalScript.DIRECTION_NW), "A", "1 ",
        12, Color.BLACK);

    nodes2.add(new Offset(-32, 75, rect, AnimalScript.DIRECTION_NW));
    nodes2.add(new Offset(32, 75, rect, AnimalScript.DIRECTION_NW));
    wire2 = lang.newWire(nodes2, 0, "WireB", null, wireProps);
    setText(new Offset(-12, 75, rect, AnimalScript.DIRECTION_NW), "B", "2 ",
        12, Color.BLACK);

    nodes3.add(new Offset(-32, 60, rect, AnimalScript.DIRECTION_NE));
    nodes3.add(new Offset(32, 60, rect, AnimalScript.DIRECTION_NE));
    wire3 = lang.newWire(nodes3, 0, "WireSUM", null, wireProps);
    setText(new Offset(12, 60, rect, AnimalScript.DIRECTION_NE), " SUM", "3 ",
        12, Color.BLACK);

    nodes4.add(new Offset(-32, -80, rect, AnimalScript.DIRECTION_SE));
    nodes4.add(new Offset(32, -80, rect, AnimalScript.DIRECTION_SE));
    wire4 = lang.newWire(nodes4, 0, "WireCout", null, wireProps);
    setText(new Offset(12, -80, rect, AnimalScript.DIRECTION_SE), " Cout",
        "3 ", 12, Color.BLACK);

  }

  public void Buendelung(Coordinates xCoordEntity, Coordinates yCoordEntity) {
    Rect rect = lang.newRect(xCoordEntity, yCoordEntity, "entity", null);
    VHDLWireProperties wireProps = new VHDLWireProperties();
    wireProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
    List<Node> nodes1 = new Vector<Node>(0, 29);// ?????????????
    List<Node> nodes2 = new Vector<Node>(0, 55);
    nodes1.add(new Offset(-47, 60, rect, AnimalScript.DIRECTION_NE));
    nodes1.add(new Offset(0, 60, rect, AnimalScript.DIRECTION_NE));
    wire5 = lang.newWire(nodes1, 0, "WireCout", null, wireProps);
    nodes2.add(new Offset(-47, -80, rect, AnimalScript.DIRECTION_SE));
    nodes2.add(new Offset(0, -80, rect, AnimalScript.DIRECTION_SE));
    wire6 = lang.newWire(nodes2, 0, "WireCout", null, wireProps);
  }

  public void wireErzeuger(VHDLElement xor, VHDLElement and) {

    VHDLWireProperties wireProps = new VHDLWireProperties();
    wireProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    List<Node> nodes1 = new Vector<Node>(0, 29);// ?????????????
    List<Node> nodes2 = new Vector<Node>(0, 55);
    List<Node> nodes3 = new Vector<Node>(0, 29);
    List<Node> nodes4 = new Vector<Node>(0, 55);

    nodes1.add(new Offset(-32, 29, xor.getName(), AnimalScript.DIRECTION_NW));
    nodes1.add(new Offset(0, 29, xor.getName(), AnimalScript.DIRECTION_NW));
    wire7 = lang.newWire(nodes1, 0, "WireA", null, wireProps);

    nodes2.add(new Offset(0, 55, xor.getName(), AnimalScript.DIRECTION_NW));
    nodes2.add(new Offset(-32, 55, xor.getName(), AnimalScript.DIRECTION_NW));
    wire8 = lang.newWire(nodes2, 0, "WireB", null, wireProps);

    nodes3.add(new Offset(-10, 29, xor.getName(), AnimalScript.DIRECTION_NW));
    nodes3.add(new Offset(-10, 29, and.getName(), AnimalScript.DIRECTION_NW));
    nodes3.add(new Offset(0, 29, and.getName(), AnimalScript.DIRECTION_NW));
    wire9 = lang.newWire(nodes3, 0, "WireC", null, wireProps);

    nodes4.add(new Offset(-18, 55, xor.getName(), AnimalScript.DIRECTION_NW));
    nodes4.add(new Offset(-18, 55, and.getName(), AnimalScript.DIRECTION_NW));
    nodes4.add(new Offset(0, 55, and.getName(), AnimalScript.DIRECTION_NW));
    wire10 = lang.newWire(nodes4, 0, "WireD", null, wireProps);

    CircleProperties circleProps = new CircleProperties();
    circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
    lang.newCircle(
        new Offset(-10, 29, xor.getName(), AnimalScript.DIRECTION_NW), 2,
        "Knoten1", null, circleProps);
    lang.newCircle(
        new Offset(-18, 55, xor.getName(), AnimalScript.DIRECTION_NW), 2,
        "Knoten2", null, circleProps);

  }

  public void setTitle() {
    txtProp = new TextProperties();
    txtProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
    txtProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 24));
    lang.newText(new Coordinates(130, 30), "Halbaddierer", "title", null,
        txtProp);

  }

  private void showSourceCode() {
    sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLUE);
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 12));
    sc = lang.newSourceCode(new Coordinates(260, 100), "sourceCode", null,
        sourceCodeProps);

    sc.addCodeLine("LIBRARY IEEE;", null, 0, null);
    sc.addCodeLine("USE IEEE.STD_LOGIC_1164.ALL;", null, 0, null);
    sc.addCodeLine("entity Halbaddierer is", null, 0, null);
    sc.addCodeLine("port (A, B : in std_logic; Cout, SUM : out std_logic);",
        null, 1, null);
    sc.addCodeLine("end Halbaddierer;", null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("architecture Verhalten of Halbddierer is", null, 0, null);
    sc.addCodeLine("begin", null, 0, null);
    sc.addCodeLine("SUM <= A xor B;", null, 0, null);
    sc.addCodeLine("Cout <= A and B;", null, 0, null);
    sc.addCodeLine("end Verhalten;", null, 0, null);

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> arg) {

    char[] pinArray = new char[2];

    pinArray[0] = ((String) arg.get("A")).charAt(0);

    pinArray[1] = ((String) arg.get("B")).charAt(0);

    HauptxorandErzeuger(pinArray, new Coordinates(75, 100), new Coordinates(75,
        200), new Coordinates(20, 80), new Coordinates(200, 320),
        "Halbaddierer");

    return lang.toString();
  }

  public String getAlgorithmName() {
    return "Halbaddierer";
  }

  public String getAnimationAuthor() {
    return "Golsa Arashloozadeh";
  }

  public String getCodeExample() {
    return null;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getDescription() {
    return "Illustrates how a Halfadder gate works";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_HARDWARE);
  }

  public String getName() {
    return "Halbaddierer";
  }

  public String getOutputLanguage() {
    return "VHDL";
  }

  public static void main(String[] args) {
    Halbaddierer ha = new Halbaddierer();
    ha.init();
    Hashtable<String, Object> ht = new Hashtable<String, Object>(7);
    ht.put("A", "o");
    ht.put("B", "0");

    System.err.println(ha.generate(null, ht));
  }
}
