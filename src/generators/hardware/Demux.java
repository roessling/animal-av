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

public class Demux implements Generator {

  private VHDLLanguage         lang;
  private SourceCode           sc;
  private SourceCodeProperties sourceCodeProps;
  private TextProperties       txtProp;
  boolean                      tester = false;

  public void init() {
    lang = new AnimalScript("Demux", "Golsa Arashloozadeh", 640, 480);
    lang.setStepMode(true);

  }

  public VHDLElement DemuxOperator(char[] CharSteuerArray, char Data) {
    Vector<VHDLPin> pins = new Vector<VHDLPin>(100);
    int[] SteuerArrayint = new int[CharSteuerArray.length];
    int yIndex = 0;
    VHDLPin[] AusgangArray = new VHDLPin[(int) (Math.pow(2,
        CharSteuerArray.length))];
    VHDLPin[] SteuerArray = new VHDLPin[CharSteuerArray.length];
    setTitle();
    showSourceCode();
    VHDLPin e = new VHDLPin(VHDLPinType.INPUT, " e ", VHDLPin.VALUE_NOT_DEFINED);
    pins.add(e);
    for (int i = 0; i < SteuerArray.length; i++) {
      SteuerArray[i] = new VHDLPin(VHDLPinType.CONTROL, " S" + i + " ",
          VHDLPin.VALUE_NOT_DEFINED);
      pins.add(SteuerArray[i]);

    }
    for (int i = 0; i < AusgangArray.length; i++) {
      AusgangArray[i] = new VHDLPin(VHDLPinType.OUTPUT, " Y" + i + " ",
          VHDLPin.VALUE_NOT_DEFINED);
      pins.add(AusgangArray[i]);
    }
    VHDLElement elem = lang.newDemultiplexer(new Coordinates(20, 100), 150,
        500, "myDemux", pins, null);
    lang.nextStep();
    if (Data != '0' && Data != '1') {
      e.setValue('x');
      tester = true;

    } else
      e.setValue(Data);
    for (int i = 0; i < SteuerArray.length; i++) {

      if (CharSteuerArray[i] != '0' && CharSteuerArray[i] != '1') {
        SteuerArray[i].setValue('x');
        tester = true;
      } else
        SteuerArray[i].setValue(CharSteuerArray[i]);
    }
    elem = lang.newDemultiplexer(new Coordinates(20, 100), 150, 500, "myDemux",
        pins, null);

    lang.nextStep();
    if (tester == true) {
      for (int i = 0; i < AusgangArray.length; i++)
        AusgangArray[i].setValue('x');

      elem = lang.newDemultiplexer(new Coordinates(20, 100), 150, 500,
          "mydmux", pins, null);
      sc.highlight(15);
      lang.nextStep();
      sc.unhighlight(15);
      return elem;
    }
    for (int i = 0; i < CharSteuerArray.length; i++) {
      if (CharSteuerArray[i] == '0') {
        SteuerArrayint[i] = 0;

      } else if (CharSteuerArray[i] == '1')
        SteuerArrayint[i] = 1;
    }

    for (int j = 0; j < SteuerArrayint.length; j++)
      yIndex += (int) (SteuerArrayint[j] * Math.pow(2, j));

    for (int i = 0; i < AusgangArray.length; i++) {

      if (i != yIndex) {
        AusgangArray[i].setValue('x');

      } else {
        AusgangArray[i].setValue(Data);

      }

    }
    sc.highlight(10);
    sc.highlight(11);
    sc.highlight(12);
    sc.highlight(13);
    sc.highlight(14);
    sc.highlight(15);
    sc.highlight(16);

    elem = lang.newDemultiplexer(new Coordinates(20, 100), 150, 500, "myDemux",
        pins, null);
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

  public void setTitle() {
    txtProp = new TextProperties();
    txtProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
    txtProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 24));
    lang.newText(new Coordinates(260, 20), "Demux", "title", null, txtProp);
    // lang.newRect(new Offset(-65, 0, title, AnimalScript.DIRECTION_NW),
    // new Offset(65, 0, title, AnimalScript.DIRECTION_SE), "recht", null);
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
    sc.addCodeLine("entity 1:4-Demux is", null, 0, null);
    sc.addCodeLine(
        "port (e: in std_logic; Y0,Y1,Y2,Y3 : out std_logic;SEL:in std_logic _vector (1 downto 0));  // Steuersignale al 2-Bit-Vektor",
        null, 1, null);
    sc.addCodeLine("end  1:4-Demux;", null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("architecture  Verhalten of 1:4-Demux is", null, 0, null);
    sc.addCodeLine("begin", null, 0, null);
    sc.addCodeLine("SELECT_PROCESS: process  (SEL,e) ", null, 0, null);
    sc.addCodeLine("begin", null, 1, null);
    sc.addCodeLine("case SEL is", null, 2, null);
    sc.addCodeLine(
        "when '00'  =>  Y0 <= e ; Y1 <= 'x' ; Y2 <= 'x' ; Y3 <= 'x';", null, 3,
        null);
    sc.addCodeLine(
        "when '01'  =>  Y0 <= 'x' ; Y1 <= e ; Y2 <= 'x' ; Y3 <= 'x';", null, 3,
        null);
    sc.addCodeLine(
        "when '10'  =>  Y0 <= 'x' ; Y1 <= 'x' ; Y2 <= e ; Y3 <= 'x';", null, 3,
        null);
    sc.addCodeLine(
        "when '11'  =>  Y0 <= 'x' ; Y1 <= 'x'; Y2 <= 'x' ; Y3 <= e;", null, 3,
        null);
    sc.addCodeLine(
        "when others =>  Y0 <= 'x' ; Y1 <= 'x'; Y2 <= 'x' ; Y3 <= 'x';", null,
        3, null);
    sc.addCodeLine("end case;", null, 2, null);
    sc.addCodeLine("end process;", null, 0, null);
    sc.addCodeLine("end Verhalten;", null, 0, null);

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> arg) {
    Demux Demux = new Demux();
    Demux.init();
    int n = (Integer) arg.get("n");

    char[] CharSteuerArray = new char[n];

    for (int i = 0; i < n; i++) {
      CharSteuerArray[i] = ((String) arg.get("S" + i)).charAt(0);
    }

    char Data = ((String) arg.get("e")).charAt(0);

    DemuxOperator(CharSteuerArray, Data);
    return lang.toString();

  }

  public String getAlgorithmName() {
    return "Demultiplexer";
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
    return "Demultiplexer";
  }

  public String getOutputLanguage() {
    return "VHDL";
  }

  public static void main(String[] args) {
    Demux Demux = new Demux();
    Demux.init();
    Hashtable<String, Object> ht = new Hashtable<String, Object>(7);
    ht.put("n", 2);
    ht.put("e", "0");
    ht.put("S0", "1");
    ht.put("S1", "0");
    // ht.put("S2", "1");

    System.err.println(Demux.generate(null, ht));
  }
}
