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
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.VHDLWireProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class Volladdierer implements Generator {

  VHDLLanguage                 lang;
  VHDLPin                      CoutPin = new VHDLPin(VHDLPinType.OUTPUT,
                                           " Cout ", VHDLPin.VALUE_NOT_DEFINED);
  VHDLPin                      APin    = new VHDLPin(VHDLPinType.INPUT, " A ",
                                           VHDLPin.VALUE_NOT_DEFINED);
  VHDLPin                      BPin    = new VHDLPin(VHDLPinType.INPUT, "B ",
                                           VHDLPin.VALUE_NOT_DEFINED);
  Vector<VHDLPin>              pinsOr  = new Vector<VHDLPin>(3);
  private SourceCode           sc;
  char                         Cout    = 0;
  private TextProperties       txtProp;
  private SourceCodeProperties sourceCodeProps;
  VHDLWire                     wire1, wire2, wire3, wire4, wire5, wire6, wire7,
      wire8, wire9, wire10, wire11, wire12, wire13, wire14, wire15, wire16;

  public VHDLElement orErzeuger1(char[] pinArray) {

    pinsOr.add(APin);
    pinsOr.add(BPin);
    pinsOr.add(CoutPin);

    VHDLElement elem = lang.newOrGate(new Coordinates(460, 271), 100, 100,
        "myOr", pinsOr, null);

    return elem;
  }

  public void orErzeuger2(char[] pinArray) {

    APin.setValue(pinArray[0]);
    BPin.setValue(pinArray[1]);

    lang.newOrGate(new Coordinates(460, 271), 100, 100, "myOr", pinsOr, null);

  }

  public VHDLElement orErzeuger3(char[] pinArray) {

    for (int j = 0; j < 2; j++) {// Ausgang berechnen
      if (pinArray[j] == '1')
        Cout = '1';
    }

    CoutPin.setValue(Cout);
    VHDLElement elem = lang.newOrGate(new Coordinates(460, 271), 100, 100,
        "myOr", pinsOr, null);
    return elem;
  }

  public Rect rechteckErzeuger(Coordinates Coordinates1,
      Coordinates Coordinates2, String name, Color color) {
    RectProperties RectProps = new RectProperties();
    RectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
    Rect rect = lang.newRect(Coordinates1, Coordinates2, "Rect", null,
        RectProps);
    setText(new Offset(5, -15, rect, AnimalScript.DIRECTION_NW), name, "name",
        12, color, new Font("SansSerif", Font.PLAIN, 12));

    return rect;

  }

  private void setText(Offset Offset, String text, String name, int Groesse,
      Color color, Font Font) {
    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, Font);
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
    lang.newText(Offset, text, name, null, textProperties);
  }

  public String VA(char[] pinArray) {
    setTitle();
    showSourceCode();

    Rect entityRect = rechteckErzeuger(new Coordinates(30, 70),
        new Coordinates(605, 400), "Volladdierer", Color.BLACK);
    wireErzeugerEntity(entityRect);// Entity

    lang.nextStep();
    changeColorWire(new VHDLWire[] { wire12, wire13, wire14, wire15, wire16 },
        Color.BLACK);
    Halbaddierer HA1 = new Halbaddierer();
    HA1.init(lang);
    Rect rect1 = rechteckErzeuger(new Coordinates(55, 190), new Coordinates(
        190, 390), "HA1", Color.BLACK);
    HA1.xorandErzeuger1(new Coordinates(80, 200), new Coordinates(80, 300));
    portmapHA1(rect1);
    lang.nextStep();
    changeColorWire(new VHDLWire[] { wire1, wire2, wire3, wire4 }, Color.BLACK);

    Rect rect2 = rechteckErzeuger(new Coordinates(250, 90), new Coordinates(
        385, 290), "HA2", Color.BLACK);
    Halbaddierer HA2 = new Halbaddierer();
    HA2.init(lang);
    HA2.xorandErzeuger1(new Coordinates(275, 100), new Coordinates(275, 200));
    portmapHA2(rect1, rect2);
    lang.nextStep();
    changeColorWire(new VHDLWire[] { wire5, wire6, wire7, wire8 }, Color.BLACK);

    VHDLElement or = orErzeuger1(pinArray);
    portmapor(rect1, or, rect2, entityRect);
    lang.nextStep();
    sc.unhighlight(22);
    changeColorWire(new VHDLWire[] { wire9, wire10, wire11 }, Color.BLACK);
    HA1.xorandErzeuger2(pinArray, new Coordinates(80, 200), new Coordinates(80,
        300));

    lang.nextStep();
    HA1.xorandErzeuger3(pinArray, new Coordinates(80, 200), new Coordinates(80,
        300));
    lang.nextStep();
    char[] pinArray2 = new char[2];
    pinArray2[1] = pinArray[2];// /Cin
    pinArray2[0] = HA1.SUMPin.getValue();

    HA2.xorandErzeuger2(pinArray2, new Coordinates(275, 100), new Coordinates(
        275, 200));
    lang.nextStep();
    HA2.xorandErzeuger3(pinArray2, new Coordinates(275, 100), new Coordinates(
        275, 200));
    char Sumgesamt = HA2.SUMPin.getValue();
    setText(new Offset(35, 73, entityRect, AnimalScript.DIRECTION_NE), " = "
        + Sumgesamt, "SUM", 12, Color.RED,
        new Font("SansSerif", Font.PLAIN, 12));

    char[] pinArray3 = new char[2];
    pinArray3[0] = HA2.Cout;
    pinArray3[1] = HA1.Cout;
    orErzeuger2(pinArray3);
    lang.nextStep();
    orErzeuger3(pinArray3);
    setText(new Offset(35, -79, entityRect, AnimalScript.DIRECTION_SE), " ="
        + Cout, "Cout", 12, Color.RED, new Font("SansSerif", Font.PLAIN, 12));
    return lang.toString();
  }

  public void setTitle() {
    txtProp = new TextProperties();
    txtProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
    txtProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 24));
    lang.newText(new Coordinates(450, 30), "Volladdierer", "title", null,
        txtProp);

    lang.nextStep();

  }

  private void showSourceCode() {
    sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLUE);
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 13));
    sc = lang.newSourceCode(new Coordinates(675, 60), "sourceCode", null,
        sourceCodeProps);

    sc.addCodeLine("LIBRARY IEEE;", null, 0, null);
    sc.addCodeLine("USE IEEE.STD_LOGIC_1164.ALL;", null, 0, null);
    sc.addCodeLine("entity Volladdierer is", null, 0, null);
    sc.addCodeLine(
        "port (Ain, Bin,Cin : in std_logic; Cout, SUM : out std_logic);", null,
        1, null);
    sc.addCodeLine("end  Volladdierer", null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("architecture Verhalten of Volladdierer is", null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("component Halbddierer", null, 0, null);
    sc.addCodeLine("port (A, B : in  std_logic; Cout, SUM : out std_logic);",
        null, 0, null);
    sc.addCodeLine("end component;", null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine(
        "signal  TEMP_CARRY1,TEMP_CARRY2,TEMP_SUM:std_logic;  //Lokale Signale",
        null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("begin", null, 0, null);
    sc.addCodeLine("HA1: Halbaddierer", null, 0, null);
    sc.addCodeLine(
        "port map (A => Ain, B => Bin, Cout => TEMP_CARRY1 ,SUM => TEMP_SUM)",
        null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("HA2: Halbaddierer", null, 0, null);
    sc.addCodeLine(
        "port map (A => Cin,B => TEMP_SUM, Cout => TEMP_CARRY2,SUM => SUM)",
        null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("Cout = TEMP_CARRY1  or  TEMP_CARRY2", null, 0, null);
    sc.addCodeLine("end Verhalten ;", null, 0, null);

  }

  public void changeColorWire(VHDLWire[] wire, Color color) {
    for (int i = 0; i < wire.length; i++) {

      wire[i].changeColor("wire", color, null, null);
    }

  }

  public void portmapHA1(Rect rect1) {
    sc.unhighlight(0);
    sc.unhighlight(1);
    sc.unhighlight(2);
    sc.unhighlight(3);
    sc.unhighlight(4);
    sc.highlight(16);
    sc.highlight(17);

    VHDLWireProperties wireProps = new VHDLWireProperties();
    wireProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

    List<Node> nodes1 = new Vector<Node>(0, 29);
    nodes1.add(new Offset(-30, 50, rect1, AnimalScript.DIRECTION_NE));
    nodes1.add(new Offset(20, 50, rect1, AnimalScript.DIRECTION_NE));
    wire1 = lang.newWire(nodes1, 0, "Wire1", null, wireProps);

    List<Node> nodes2 = new Vector<Node>(0, 29);
    nodes2.add(new Offset(-30, -50, rect1, AnimalScript.DIRECTION_SE));
    nodes2.add(new Offset(160, -50, rect1, AnimalScript.DIRECTION_SE));
    wire2 = lang.newWire(nodes2, 0, "Wire2", null, wireProps);

    List<Node> nodes3 = new Vector<Node>(0, 29);
    nodes3.add(new Offset(0, 39, rect1, AnimalScript.DIRECTION_NW));
    nodes3.add(new Offset(-18, 39, rect1, AnimalScript.DIRECTION_NW));
    wire3 = lang.newWire(nodes3, 0, "Wire3", null, wireProps);

    List<Node> nodes4 = new Vector<Node>(0, 29);
    nodes4.add(new Offset(0, 65, rect1, AnimalScript.DIRECTION_NW));
    nodes4.add(new Offset(-18, 65, rect1, AnimalScript.DIRECTION_NW));
    wire4 = lang.newWire(nodes4, 0, "Wire4", null, wireProps);
    setText(new Offset(110, -50, rect1, AnimalScript.DIRECTION_SE),
        "TEMP_CARRY1", "TEMP_CARRY1", 10, Color.RED, new Font("SansSerif",
            Font.BOLD, 10));
    setText(new Offset(15, 50, rect1, AnimalScript.DIRECTION_NE), "TEMP_SUM",
        "TEMP_SUM", 10, Color.RED, new Font("SansSerif", Font.BOLD, 10));

  }

  public void portmapHA2(Rect rect1, Rect rect2) {// portmaoHA2
    sc.unhighlight(16);
    sc.unhighlight(17);
    sc.highlight(19);
    sc.highlight(20);
    VHDLWireProperties wireProps = new VHDLWireProperties();
    wireProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

    List<Node> nodes1 = new Vector<Node>(0, 29);
    nodes1.add(new Offset(20, 50, rect1, AnimalScript.DIRECTION_NE));
    nodes1.add(new Offset(20, -35, rect1, AnimalScript.DIRECTION_NE));
    nodes1.add(new Offset(70, -35, rect1, AnimalScript.DIRECTION_NE));
    wire5 = lang.newWire(nodes1, 0, "Wire5", null, wireProps);

    List<Node> nodes2 = new Vector<Node>(0, 55);
    nodes2.add(new Offset(-220, 39, rect2, AnimalScript.DIRECTION_NW));
    nodes2.add(new Offset(5, 39, rect2, AnimalScript.DIRECTION_NW));
    wire6 = lang.newWire(nodes2, 0, "Wire6", null, wireProps);

    List<Node> nodes3 = new Vector<Node>(0, 55);
    nodes3.add(new Offset(-30, 50, rect2, AnimalScript.DIRECTION_NE));
    nodes3.add(new Offset(200, 50, rect2, AnimalScript.DIRECTION_NE));
    wire7 = lang.newWire(nodes3, 0, "Wire7", null, wireProps);

    List<Node> nodes4 = new Vector<Node>(0, 55);
    nodes4.add(new Offset(-30, -50, rect2, AnimalScript.DIRECTION_SE));
    nodes4.add(new Offset(55, -50, rect2, AnimalScript.DIRECTION_SE));
    wire8 = lang.newWire(nodes4, 0, "Wire8", null, wireProps);
    setText(new Offset(20, -50, rect2, AnimalScript.DIRECTION_SE),
        "TEMP_CARRY2", "TEMP_CARRY2", 10, Color.RED, new Font("SansSerif",
            Font.BOLD, 10));
    setText(new Offset(15, -35, rect1, AnimalScript.DIRECTION_NE), "TEMP_SUM",
        "TEMP_SUM", 10, Color.RED, new Font("SansSerif", Font.BOLD, 10));
  }

  public void portmapor(Rect rect1, VHDLElement or, Rect rect2, Rect rect) {// portmapor
    sc.unhighlight(19);
    sc.unhighlight(20);
    sc.highlight(22);
    VHDLWireProperties wireProps = new VHDLWireProperties();
    wireProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    List<Node> nodes1 = new Vector<Node>(0, 29);
    nodes1.add(new Offset(160, -50, rect1, AnimalScript.DIRECTION_SE));
    nodes1.add(new Offset(0, -31, or, AnimalScript.DIRECTION_SW));
    wire9 = lang.newWire(nodes1, 0, "Wire9", null, wireProps);

    List<Node> nodes2 = new Vector<Node>(0, 55);
    nodes2.add(new Offset(55, -50, rect2, AnimalScript.DIRECTION_SE));
    nodes2.add(new Offset(55, 17, rect2, AnimalScript.DIRECTION_SE));
    nodes2.add(new Offset(0, 36, or, AnimalScript.DIRECTION_NW));
    wire10 = lang.newWire(nodes2, 0, "Wire10", null, wireProps);

    List<Node> nodes3 = new Vector<Node>(0, 55);
    nodes3.add(new Offset(-20, -79, rect, AnimalScript.DIRECTION_SE));
    nodes3.add(new Offset(-45, -79, rect, AnimalScript.DIRECTION_SE));
    wire11 = lang.newWire(nodes3, 0, "Wire11", null, wireProps);
    setText(new Offset(0, 17, rect2, AnimalScript.DIRECTION_SE), "TEMP_CARRY2",
        "TEMP_CARRY2", 10, Color.RED, new Font("SansSerif", Font.BOLD, 10));
  }

  public void wireErzeugerEntity(Rect rect) {
    sc.highlight(0);
    sc.highlight(1);
    sc.highlight(2);
    sc.highlight(3);
    sc.highlight(4);
    VHDLWireProperties wireProps = new VHDLWireProperties();
    wireProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    List<Node> nodes1 = new Vector<Node>(0, 55);
    List<Node> nodes2 = new Vector<Node>(0, 55);
    List<Node> nodes3 = new Vector<Node>(0, 55);
    List<Node> nodes4 = new Vector<Node>(0, 55);
    List<Node> nodes5 = new Vector<Node>(0, 55);

    nodes1.add(new Offset(-20, 59, rect, AnimalScript.DIRECTION_NW));
    nodes1.add(new Offset(10, 59, rect, AnimalScript.DIRECTION_NW));
    wire12 = lang.newWire(nodes1, 0, "WireCin", null, wireProps);
    setText(new Offset(-25, 59, rect, AnimalScript.DIRECTION_NW), "Cin", "Cin",
        12, Color.RED, new Font("SansSerif", Font.PLAIN, 12));

    nodes2.add(new Offset(-20, 159, rect, AnimalScript.DIRECTION_NW));
    nodes2.add(new Offset(10, 159, rect, AnimalScript.DIRECTION_NW));
    wire13 = lang.newWire(nodes2, 0, "WireA", null, wireProps);
    setText(new Offset(-25, 159, rect, AnimalScript.DIRECTION_NW), "Ain",
        "Ain", 12, Color.RED, new Font("SansSerif", Font.PLAIN, 12));

    nodes3.add(new Offset(-20, 185, rect, AnimalScript.DIRECTION_NW));
    nodes3.add(new Offset(10, 185, rect, AnimalScript.DIRECTION_NW));
    wire14 = lang.newWire(nodes3, 0, "WireB", null, wireProps);
    setText(new Offset(-25, 185, rect, AnimalScript.DIRECTION_NW), "Bin",
        "Bin", 12, Color.RED, new Font("SansSerif", Font.PLAIN, 12));

    nodes4.add(new Offset(-20, 70, rect, AnimalScript.DIRECTION_NE));
    nodes4.add(new Offset(10, 70, rect, AnimalScript.DIRECTION_NE));
    wire15 = lang.newWire(nodes4, 0, "WireSUM", null, wireProps);
    setText(new Offset(10, 70, rect, AnimalScript.DIRECTION_NE), "SUM", "SUM",
        12, Color.RED, new Font("SansSerif", Font.PLAIN, 12));

    nodes5.add(new Offset(-20, -79, rect, AnimalScript.DIRECTION_SE));
    nodes5.add(new Offset(10, -79, rect, AnimalScript.DIRECTION_SE));
    wire16 = lang.newWire(nodes5, 0, "WireCout", null, wireProps);
    setText(new Offset(10, -79, rect, AnimalScript.DIRECTION_SE), "Cout",
        "Cout", 12, Color.RED, new Font("SansSerif", Font.PLAIN, 12));

  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> arg) {

    char[] pinArray = new char[3];

    pinArray[0] = ((String) arg.get("A")).charAt(0);

    pinArray[1] = ((String) arg.get("B")).charAt(0);
    pinArray[2] = ((String) arg.get("Cin")).charAt(0);

    VA(pinArray);

    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Volladdierer";
  }

  @Override
  public String getAnimationAuthor() {
    return "Golsa Arashloozadeh";
  }

  @Override
  public String getCodeExample() {
    return null;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "Illustrates how a fulladder gate works";
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_HARDWARE);
  }

  @Override
  public String getName() {
    return "Volladdierer";
  }

  @Override
  public String getOutputLanguage() {
    return "VHDL";
  }

  @Override
  public void init() {
    lang = new AnimalScript("Volladdierer", "Golsa Arashloozadeh", 640, 480);
    lang.setStepMode(true);

  }

  public static void main(String[] args) {
    Volladdierer va = new Volladdierer();
    va.init();
    Hashtable<String, Object> ht = new Hashtable<String, Object>(7);
    ht.put("A", "1");
    ht.put("B", "1");
    ht.put("Cin", "1");

    System.err.println(va.generate(null, ht));
  }

}
