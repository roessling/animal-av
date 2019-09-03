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
import algoanim.util.Offset;

public class TFlipflop implements Generator {
  private VHDLLanguage         lang;
  private SourceCode           sc;
  private SourceCodeProperties sourceCodeProps;
  private TextProperties       txtProp;

  public void init() {
    lang = new AnimalScript("T-FLIPFLOP", "Golsa Arashloozadeh", 640, 480);
    lang.setStepMode(true);

  }

  public VHDLElement TOperator(char[] Pinarray) {

    char Qm = Pinarray[5];
    setTitle();
    showSourceCode1();
    Vector<VHDLPin> pins = new Vector<VHDLPin>(7);
    VHDLPin T = new VHDLPin(VHDLPinType.INPUT, " T ", VHDLPin.VALUE_NOT_DEFINED);
    pins.add(T);
    VHDLPin AReset = new VHDLPin(VHDLPinType.SET, " AReset ",
        VHDLPin.VALUE_NOT_DEFINED);
    pins.add(AReset);
    VHDLPin ASet = new VHDLPin(VHDLPinType.RESET, " ASet ",
        VHDLPin.VALUE_NOT_DEFINED);
    pins.add(ASet);
    VHDLPin Clk = new VHDLPin(VHDLPinType.CLOCK, " Clk ",
        VHDLPin.VALUE_NOT_DEFINED);
    pins.add(Clk);
    VHDLPin EnClk = new VHDLPin(VHDLPinType.CLOCK_ENABLE, " EnClk ",
        VHDLPin.VALUE_NOT_DEFINED);
    pins.add(EnClk);
    VHDLPin Q = new VHDLPin(VHDLPinType.OUTPUT, " Q ",
        VHDLPin.VALUE_NOT_DEFINED);
    pins.add(Q);
    VHDLElement elem = lang.newTFlipflop(new Coordinates(20, 100), 250, 500,
        "myT", pins, null);
    lang.nextStep();
    for (int i = 0; i < Pinarray.length; i++) {
      if (Pinarray[i] != '0' && Pinarray[i] != '1')
        Pinarray[i] = 'x';
    }
    T.setValue(Pinarray[0]);
    AReset.setValue(Pinarray[1]);
    ASet.setValue(Pinarray[2]);
    Clk.setValue(Pinarray[3]);
    EnClk.setValue(Pinarray[4]);
    elem = lang.newTFlipflop(new Coordinates(20, 100), 250, 500, "myT", pins,
        null);

    lang.nextStep();
    String ClkEnClk = String.valueOf(Clk.getValue()).concat(
        String.valueOf(EnClk.getValue()));

    if (AReset.getValue() == 'x' || ASet.getValue() == 'x') {
      Q.setValue('x');
      sc.highlight(18);
      sc.highlight(19);
      elem = lang.newTFlipflop(new Coordinates(20, 100), 250, 500, "myT", pins,
          null);
      lang.nextStep();
      sc.unhighlight(18);
      sc.unhighlight(19);
      return elem;
    }

    else if (AReset.getValue() == '1') { // ARESET
      if (ASet.getValue() == '1') {
        Q.setValue('x');
        sc.highlight(18);
        sc.highlight(19);

      } else {
        sc.highlight(10);
        sc.highlight(11);
        Q.setValue('0');

      }

      elem = lang.newTFlipflop(new Coordinates(20, 100), 250, 500, "myT", pins,
          null);
      lang.nextStep();
      sc.unhighlight(10);
      sc.unhighlight(11);
      sc.unhighlight(18);
      sc.unhighlight(19);

      return elem;

    } else if (ASet.getValue() == '1') {// ASET
      sc.highlight(12);
      sc.highlight(13);
      Q.setValue('1');
      elem = lang.newTFlipflop(new Coordinates(20, 100), 250, 500, "myT", pins,
          null);
      lang.nextStep();
      sc.unhighlight(12);
      sc.unhighlight(13);
      return elem;

    } else if (ClkEnClk.equals("11")) {// ansteigende Flanke
      sc.highlight(14);
      sc.highlight(15);
      if (T.getValue() == '1') {

        sc.highlight(16);
        sc.highlight(17);
        Q.setValue((Qm == '0') ? '1' : '0');// togglen
        setText(new Offset(30, 95, elem, AnimalScript.DIRECTION_NE), "=not Qm",
            "=not Qm", 12);
      } else if (T.getValue() == '0') {

        Q.setValue(Qm);
        setText(new Offset(30, 95, elem, AnimalScript.DIRECTION_NE), "=Qm",
            "Qm", 12);

      }

      else
        Q.setValue('x');
    }

    else {
      Q.setValue('x');
      sc.highlight(18);
      sc.highlight(19);

    }

    elem = lang.newTFlipflop(new Coordinates(20, 100), 250, 500, "myT", pins,
        null);
    lang.nextStep();
    sc.unhighlight(14);
    sc.unhighlight(15);
    sc.unhighlight(16);
    sc.unhighlight(17);
    sc.unhighlight(18);
    sc.unhighlight(19);
    sc.unhighlight(20);

    return elem;
  }

  public void setTitle() {
    txtProp = new TextProperties();
    txtProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
    txtProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 24));
    lang.newText(new Coordinates(130, 30), "T-Flipflop", "title", null, txtProp);
  }

  private void setText(Offset Offset, String text, String name, int Groesse) {
    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, Groesse));
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    lang.newText(Offset, text, name, null, textProperties);
  }

  private void showSourceCode1() {
    sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
        Color.BLUE);
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 12));
    sc = lang.newSourceCode(new Coordinates(340, 50), "sourceCode", null,
        sourceCodeProps);

    sc.addCodeLine("LIBRARY IEEE;", null, 0, null);
    sc.addCodeLine("USE IEEE.STD_LOGIC_1164.ALL;", null, 0, null);
    sc.addCodeLine("entity  T-Flipflop is", null, 0, null);
    sc.addCodeLine(
        "port( CLK,T,AReset,ASet,Enable : in std_logic;Q : buffer std_logic);",
        null, 1, null);
    sc.addCodeLine("end T-Flipflop;", null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("architecture Verhalten of T-Flipflop is", null, 0, null);
    sc.addCodeLine("begin", null, 0, null);
    sc.addCodeLine("P1: process( CLK,AReset,ASet,Enable)", null, 1, null);
    sc.addCodeLine("begin", null, 1, null);
    sc.addCodeLine("if (AReset='1') then   //asynchroner Reset", null, 2, null);
    sc.addCodeLine("Q <= '0';", null, 3, null);
    sc.addCodeLine("elsif (ASet= '1' ) then   //asynchroner Preeset ", null, 2,
        null);
    sc.addCodeLine("Q <= '1';", null, 3, null);
    sc.addCodeLine(
        "elsif(CLK'event and CLK = '1') then   //ansteigende Flanke  ", null,
        2, null);
    sc.addCodeLine("if(Enable='1') then   //Freigabe", null, 3, null);
    sc.addCodeLine("if( T='1') then", null, 4, null);
    sc.addCodeLine("Q <= not Q;   //Toggeln", null, 5, null);
    sc.addCodeLine("else ", null, 2, null);
    sc.addCodeLine("Q <= 'X';", null, 3, null);
    sc.addCodeLine("end if", null, 4, null);
    sc.addCodeLine("end if;", null, 3, null);
    sc.addCodeLine("end if;", null, 2, null);
    sc.addCodeLine("end process;", null, 1, null);
    sc.addCodeLine("end Verhalten;", null, 0, null);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> arg) {
    TFlipflop t = new TFlipflop();
    t.init();
    char[] Pinarray = new char[6];
    Pinarray[0] = ((String) arg.get("T")).charAt(0);
    Pinarray[1] = ((String) arg.get("AReset")).charAt(0);
    Pinarray[2] = ((String) arg.get("ASet")).charAt(0);
    Pinarray[3] = ((String) arg.get("Clk")).charAt(0);
    Pinarray[4] = ((String) arg.get("EnClk")).charAt(0);
    Pinarray[5] = ((String) arg.get("Qm")).charAt(0);
    TOperator(Pinarray);
    return lang.toString();

  }

  public String getAlgorithmName() {
    return "TFlipflop";
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
    return "TFlipflop";
  }

  public String getOutputLanguage() {
    return "VHDL";
  }

  public static void main(String[] args) {
    TFlipflop t = new TFlipflop();
    t.init();
    Hashtable<String, Object> ht = new Hashtable<String, Object>(7);
    ht.put("T", "1");
    ht.put("Qm", "0");
    ht.put("AReset", "3");
    ht.put("ASet", "0");
    ht.put("Clk", "1");
    ht.put("EnClk", "1");

    System.err.println(t.generate(null, ht));
  }
}
