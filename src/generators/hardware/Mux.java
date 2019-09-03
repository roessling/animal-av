package generators.hardware;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.VHDLLanguage;
import algoanim.primitives.vhdl.VHDLElement;
import algoanim.primitives.vhdl.VHDLPin;
import algoanim.primitives.vhdl.VHDLPinType;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class Mux implements Generator {

  private VHDLLanguage         lang;

  private SourceCode           sc;
  private TextProperties       txtProp;
  private SourceCodeProperties sourceCodeProps;
  boolean                      tester = false;

  public void init() {
    lang = new AnimalScript("Mux", "Golsa Arashloozadeh", 640, 480);
    lang.setStepMode(true);

  }

  public VHDLElement MuxOperator(char[] CharSteuerArray, char[] CharDatenArray) {
    Vector<VHDLPin> pins = new Vector<VHDLPin>(100);

    int[] SteuerArrayint = new int[CharSteuerArray.length];
    VHDLPin[] DatenArray = new VHDLPin[(int) (Math.pow(2,
        CharSteuerArray.length))];
    VHDLPin[] SteuerArray = new VHDLPin[CharSteuerArray.length];
    VHDLPin Y = new VHDLPin(VHDLPinType.OUTPUT, " Y", VHDLPin.VALUE_NOT_DEFINED);
    int DataIndex = 0;
    setTitle();
    showSourceCode();
    // / Highlighting wird hier gemacht
    for (int i = 0; i < SteuerArray.length; i++) {
      SteuerArray[i] = new VHDLPin(VHDLPinType.CONTROL, "  S" + i + " ",
          VHDLPin.VALUE_NOT_DEFINED);
      pins.add(SteuerArray[i]);
    }
    for (int i = 0; i < DatenArray.length; i++) {
      DatenArray[i] = new VHDLPin(VHDLPinType.INPUT, "  e" + i + " ",
          VHDLPin.VALUE_NOT_DEFINED);
      pins.add(DatenArray[i]);
    }
    pins.add(Y);

    VHDLElement elem = lang.newMultiplexer(new Coordinates(20, 100), 200, 400,
        "mymux", pins, null);
    lang.nextStep();
    for (int i = 0; i < SteuerArray.length; i++) {
      if (CharSteuerArray[i] != '0' && CharSteuerArray[i] != '1') {
        SteuerArray[i].setValue('x');
        tester = true;

      } else
        SteuerArray[i].setValue(CharSteuerArray[i]);
    }
    for (int i = 0; i < DatenArray.length; i++) {
      if (CharDatenArray[i] != '0' && CharDatenArray[i] != '1') {
        DatenArray[i].setValue('x');
        tester = true;
      } else
        DatenArray[i].setValue(CharDatenArray[i]);
    }
    if (tester == true) {
      Y.setValue('x');
      elem = lang.newMultiplexer(new Coordinates(20, 100), 200, 400, "mymux",
          pins, null);
      sc.highlight(15);
      lang.nextStep();
      sc.unhighlight(15);
      return elem;
    }

    elem = lang.newMultiplexer(new Coordinates(20, 100), 200, 400, "mymux",
        pins, null);
    lang.nextStep();

    for (int i = 0; i < SteuerArray.length; i++) {

      if (CharSteuerArray[i] == '0')
        SteuerArrayint[i] = 0;

      else if (CharSteuerArray[i] == '1')
        SteuerArrayint[i] = 1;
    }

    for (int j = 0; j < SteuerArrayint.length; j++)
      DataIndex += (int) (SteuerArrayint[j] * Math.pow(2, j));

    elem = lang.newMultiplexer(new Coordinates(20, 100), 200, 400, "mymux",
        pins, null);
    setText(new Offset(26, 99, elem, AnimalScript.DIRECTION_NE), "= e"
        + DataIndex, "ei");

    sc.highlight(10);
    sc.highlight(11);
    sc.highlight(12);
    sc.highlight(13);
    sc.highlight(14);
    sc.highlight(15);
    sc.highlight(16);

    lang.nextStep();
    sc.unhighlight(10);
    sc.unhighlight(11);
    sc.unhighlight(12);
    sc.unhighlight(13);
    sc.unhighlight(14);
    sc.unhighlight(15);
    sc.unhighlight(16);
    return elem;
  }

  private void setText(Offset Offset, String text, String name) {
    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 12));
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    lang.newText(Offset, text, name, null, textProperties);
  }

  public void setTitle() {
    txtProp = new TextProperties();
    txtProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
    txtProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 24));
    lang.newText(new Coordinates(260, 20), "Mux", "title", null, txtProp);
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
    sc = lang.newSourceCode(new Coordinates(340, 70), "sourceCode", null,
        sourceCodeProps);
    sc.addCodeLine("LIBRARY IEEE;", null, 0, null);
    sc.addCodeLine("USE IEEE.STD_LOGIC_1164.ALL;", null, 0, null);
    sc.addCodeLine("entity 4:1-Mux is", null, 0, null);
    sc.addCodeLine(
        "port (e0,e1,e2,e3 : in std_logic; Y : out std_logic;SEL:in std_logic _vector (1 downto 0));  // Steuersignale al 2-Bit-Vektor",
        null, 1, null);
    sc.addCodeLine("end  4:1-Mux;", null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("architecture Verhalten of 4:1-Mux is", null, 0, null);
    sc.addCodeLine("begin", null, 0, null);
    sc.addCodeLine("SELECT_PROCESS: process (SEL,e1,e2,e3,e4) ", null, 0, null);
    sc.addCodeLine("begin", null, 1, null);
    sc.addCodeLine("case SEL is", null, 2, null);
    sc.addCodeLine("when '00'  => Y <= e0;", null, 3, null);// /??????""
    sc.addCodeLine("when '01'  => Y <= e1;", null, 3, null);
    sc.addCodeLine("when '10'  => Y <= e2;", null, 3, null);
    sc.addCodeLine("when '11'  => Y <= e3;", null, 3, null);
    sc.addCodeLine("when others => Y <= 'x';", null, 3, null);
    sc.addCodeLine("end  case;  ", null, 2, null);
    sc.addCodeLine("end  process; ", null, 0, null);
    sc.addCodeLine("end Verhalten;", null, 0, null);

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> arg) {
    Mux mux = new Mux();
    mux.init();
    int n = (Integer) arg.get("n");
    char[] DatenArray = new char[(int) (Math.pow(2, n))];
    char[] CharSteuerArray = new char[n];
    for (int i = 0; i < DatenArray.length; i++) {
      DatenArray[i] = ((String) arg.get("e" + i)).charAt(0);
    }
    for (int i = 0; i < n; i++) {
      CharSteuerArray[i] = ((String) arg.get("S" + i)).charAt(0);
    }

    MuxOperator(CharSteuerArray, DatenArray);
    return lang.toString();

  }

  public String getAlgorithmName() {
    return "Multiplexer";
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
    return null;
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_HARDWARE);
  }

  public String getName() {
    return "Multiplexer";
  }

  public String getOutputLanguage() {
    return "VHDL";
  }

  public static void main(String[] args) {
    Mux mux = new Mux();
    mux.init();
    Hashtable<String, Object> ht = new Hashtable<String, Object>(20);
    ht.put("n", 3);
    ht.put("S0", "p");
    ht.put("S1", "1");
    ht.put("S2", "1");
    ht.put("S3", "0");
    ht.put("e0", "0");
    ht.put("e1", "0");
    ht.put("e2", "0");
    ht.put("e3", "1");
    ht.put("e4", "1");
    ht.put("e5", "1");
    ht.put("e6", "1");
    ht.put("e7", "1");
    ht.put("e8", "1");
    ht.put("e9", "1");
    ht.put("e10", "1");
    ht.put("e11", "1");
    ht.put("e12", "1");
    ht.put("e13", "1");
    ht.put("e14", "1");
    ht.put("e15", "1");

    System.err.println(mux.generate(null, ht));
  }
}
