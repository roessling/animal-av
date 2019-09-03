package generators.hardware.flipflop;

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

public class DFlipflop implements Generator {

  private VHDLLanguage         lang;
  private SourceCode           sc;
  private SourceCodeProperties sourceCodeProps;
  private TextProperties       txtProp;
  char                         Q = VHDLPin.VALUE_NOT_DEFINED;

  public void init() {
    lang = new AnimalScript("D-FLIPFLOP", "Golsa Arashloozadeh", 640, 480);
    lang.setStepMode(true);

  }

  public VHDLElement DOperator(char[] Pinarray) {
    setTitle();
    showSourceCode();
    Vector<VHDLPin> pins = new Vector<VHDLPin>(7);
    VHDLPin D = new VHDLPin(VHDLPinType.INPUT, " D ", VHDLPin.VALUE_NOT_DEFINED);
    pins.add(D);
    VHDLPin AReset = new VHDLPin(VHDLPinType.RESET, " AReset ",
        VHDLPin.VALUE_NOT_DEFINED);
    pins.add(AReset);
    VHDLPin Clk = new VHDLPin(VHDLPinType.CLOCK, " Clk ",
        VHDLPin.VALUE_NOT_DEFINED);
    pins.add(Clk);
    VHDLPin EnClk = new VHDLPin(VHDLPinType.CLOCK_ENABLE, " EnClk ",
        VHDLPin.VALUE_NOT_DEFINED);
    pins.add(EnClk);
    VHDLPin Q = new VHDLPin(VHDLPinType.OUTPUT, " Q ",
        VHDLPin.VALUE_NOT_DEFINED);
    pins.add(Q);
    VHDLElement elem = lang.newDFlipflop(new Coordinates(20, 100), 250, 500,
        "myD", pins, null);

    lang.nextStep();
    for (int i = 0; i < Pinarray.length; i++) {
      if (Pinarray[i] != '0' && Pinarray[i] != '1')
        Pinarray[i] = 'x';
    }
    D.setValue(Pinarray[0]);
    AReset.setValue(Pinarray[1]);
    Clk.setValue(Pinarray[2]);
    EnClk.setValue(Pinarray[3]);
    String ClkEnClk = String.valueOf(Clk.getValue()).concat(
        String.valueOf(EnClk.getValue()));
    elem = lang.newDFlipflop(new Coordinates(20, 100), 250, 500, "myD", pins,
        null);

    lang.nextStep();

    if (AReset.getValue() == 'x') {// ungültige AReset-Eingabe
      Q.setValue('x');
      elem = lang.newDFlipflop(new Coordinates(20, 100), 250, 500, "myD", pins,
          null);
      return elem;
    } else if (AReset.getValue() == '1') { // ARESET
      sc.highlight(10);
      sc.highlight(11);
      Q.setValue('0');

      elem = lang.newDFlipflop(new Coordinates(20, 100), 250, 500, "myD", pins,
          null);
      lang.nextStep();
      sc.unhighlight(10);
      sc.unhighlight(11);
      return elem;

    } else if (ClkEnClk.equals("11")) {// ansteigende Flanke
      sc.highlight(12);

      if (D.getValue() == '0') {
        sc.highlight(13);

        Q.setValue('0');// Löschen

      } else if (D.getValue() == '1') {
        sc.highlight(13);

        Q.setValue('1');// Setzen

      } else if (D.getValue() == 'x')

        Q.setValue('x');// ungültige D-Eingabe

    }

    else {

      Q.setValue('x');// ungültige ClkEnClk-Eingaben
    }

    elem = lang.newDFlipflop(new Coordinates(20, 100), 250, 500, "myD", pins,
        null);
    lang.nextStep();
    sc.unhighlight(12);
    sc.unhighlight(13);
    return elem;
  }

  public void setTitle() {
    txtProp = new TextProperties();
    txtProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
    txtProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 24));
    lang.newText(new Coordinates(130, 30), "D-Flipflop", "title", null, txtProp);
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
    sc = lang.newSourceCode(new Coordinates(360, 70), "sourceCode", null,
        sourceCodeProps);

    sc.addCodeLine("LIBRARY IEEE;", null, 0, null);
    sc.addCodeLine("USE IEEE.STD_LOGIC_1164.ALL;", null, 0, null);
    sc.addCodeLine("entity  D-Flipflop is", null, 0, null);
    sc.addCodeLine("port( CLK,D,AReset: in std_logic;Q : out std_logic);",
        null, 1, null);
    sc.addCodeLine("end D-Flipflop;", null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("architecture Verhalten of D-Flipflop is", null, 0, null);
    sc.addCodeLine("begin", null, 0, null);
    sc.addCodeLine("P1: process( CLK,AReset)", null, 1, null);
    sc.addCodeLine("begin", null, 1, null);
    sc.addCodeLine("if (AReset='1') then   //asynchroner Reset", null, 2, null);
    sc.addCodeLine("Q <= '0';", null, 3, null);
    sc.addCodeLine(
        "elsif(CLK'event and CLK = '1') then   //ansteigende Flanke  ", null,
        2, null);
    sc.addCodeLine("Q <= D;", null, 3, null);
    sc.addCodeLine("end if;", null, 3, null);
    sc.addCodeLine("end process;", null, 1, null);
    sc.addCodeLine("end Verhalten;", null, 0, null);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> arg) {
    DFlipflop d = new DFlipflop();
    d.init();
    char[] Pinarray = new char[6];

    Pinarray[0] = ((String) arg.get("D")).charAt(0);
    Pinarray[1] = ((String) arg.get("AReset")).charAt(0);
    Pinarray[2] = ((String) arg.get("Clk")).charAt(0);
    Pinarray[3] = ((String) arg.get("EnClk")).charAt(0);
    DOperator(Pinarray);

    return lang.toString();

  }

  public String getAlgorithmName() {
    return "DFlipflop";
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
    return "DFlipflop";
  }

  public String getOutputLanguage() {
    return "VHDL";
  }

  public static void main(String[] args) {
    DFlipflop d = new DFlipflop();
    d.init();
    Hashtable<String, Object> ht = new Hashtable<String, Object>(7);
    ht.put("D", "0");
    ht.put("AReset", "0");
    ht.put("Clk", "1");
    ht.put("EnClk", "1");

    System.err.println(d.generate(null, ht));
  }
}
