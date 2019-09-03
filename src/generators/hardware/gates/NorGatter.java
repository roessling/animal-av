package generators.hardware.gates;

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

public class NorGatter implements Generator {
  private VHDLLanguage         lang;
  private SourceCode           sc;
  private TextProperties       txtProp;
  private SourceCodeProperties sourceCodeProps;

  public void init() {
    lang = new AnimalScript("NOR-Gatter", "Golsa Arashloozadeh", 640, 480);
    lang.setStepMode(true);

  }

  public VHDLElement NorOperator(char[] pinArray) {

    VHDLPin pinA = null;
    VHDLPin pinY = null;
    char outValue = '1';
    setTitle();
    showSourceCode();
    Vector<VHDLPin> pins = new Vector<VHDLPin>(pinArray.length + 1);

    if (pinArray.length < 2) {
      System.err
          .println("Die Anzahl der Eingaben dürfen nicht weniger als 2 sein");
      return null;
    }
    for (int i = 0; i < pinArray.length; i++) {
      pinA = new VHDLPin(VHDLPinType.INPUT, " in" + i + " ",
          VHDLPin.VALUE_NOT_DEFINED);
      pins.add(pinA);
    }
    pinY = new VHDLPin(VHDLPinType.OUTPUT, " Y ", VHDLPin.VALUE_NOT_DEFINED);
    pins.add(pinY);
    VHDLElement elem = lang.newNorGate(new Coordinates(20, 100), 100, 400,
        "myNOr", pins, null);
    lang.nextStep();

    for (int k = 0; k < pinArray.length; k++) {
      if (pinArray[k] != '0' && pinArray[k] != '1')
        pins.get(k).setValue('x');
      else
        pins.get(k).setValue(pinArray[k]);
    }

    elem = lang.newNorGate(new Coordinates(20, 100), 100, 400, "myNOr", pins,
        null);
    lang.nextStep();

    for (int j = 0; j < pinArray.length; j++) {
      if (pins.get(j).getValue() == 'x') {
        pinY.setValue('x');
        elem = lang.newNorGate(new Coordinates(20, 100), 100, 400, "myNor",
            pins, null);
        return elem;
      } else if (pins.get(j).getValue() == '1')
        outValue = '0';

    }
    sc.highlight(8);
    pinY.setValue(outValue);
    pins.add(new VHDLPin(VHDLPinType.OUTPUT, "Y", outValue));
    elem = lang.newNorGate(new Coordinates(20, 100), 100, 400, "myNOr", pins,
        null);
    lang.nextStep();
    sc.unhighlight(8);
    return elem;
  }

  public void setTitle() {
    txtProp = new TextProperties();
    txtProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
    txtProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 24));
    lang.newText(new Coordinates(130, 30), "NOR-Gatter", "title", null, txtProp);
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
    sc = lang.newSourceCode(new Coordinates(260, 70), "sourceCode", null,
        sourceCodeProps);

    sc.addCodeLine("LIBRARY IEEE;", null, 0, null);
    sc.addCodeLine("USE IEEE.STD_LOGIC_1164.ALL;", null, 0, null);
    sc.addCodeLine("entity  NOR-Gatter is", null, 0, null);
    sc.addCodeLine("port (in0,in1: in std_logic; Y : out std_logic);", null, 1,
        null);
    sc.addCodeLine("end NOR-Gatter;", null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("architecture Verhalten of NOR-Gatter is", null, 0, null);
    sc.addCodeLine("begin", null, 0, null);
    sc.addCodeLine("Y <= in0 nor in1;", null, 1, null);
    sc.addCodeLine("end Verhalten", null, 0, null);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> arg) {

    NorGatter nor = new NorGatter();
    nor.init();

    int n = (Integer) arg.get("n");
    char[] pinArray1 = new char[n];
    for (int i = 0; i < pinArray1.length; i++) {
      pinArray1[i] = ((String) arg.get("in" + i)).charAt(0);

    }

    NorOperator(pinArray1);
    return lang.toString();
  }

  public String getAlgorithmName() {
    return "NOR-Gatter";
  }

  public String getAnimationAuthor() {
    return "Golsa Arashloozadeh";
  }

  public String getCodeExample() {
    return "Example code to follow";
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getDescription() {
    return "Illustrates how an NOR gate works";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_HARDWARE);
  }

  public String getName() {
    return "NOR-Gatter";
  }

  public String getOutputLanguage() {
    return "VHDL";
  }

  public static void main(String[] args) {
    NorGatter nor = new NorGatter();
    nor.init();
    Hashtable<String, Object> ht = new Hashtable<String, Object>(10);
    ht.put("n", 8);
    ht.put("in0", "1");
    ht.put("in1", "0");
    ht.put("in2", "0");
    ht.put("in3", "0");
    ht.put("in4", "0");
    ht.put("in5", "0");
    ht.put("in6", "0");
    ht.put("in7", "1");
    ht.put("in8", "9");
    System.err.println(nor.generate(null, ht));
  }
}
