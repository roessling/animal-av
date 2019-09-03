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

public class AndGatter implements Generator {

  private VHDLLanguage         lang;
  private SourceCode           sc;
  private SourceCodeProperties sourceCodeProps;
  private TextProperties       txtProp;

  public void init() {
    lang = new AnimalScript("AND-Gatter", "Golsa Arashloozadeh", 640, 480);
    lang.setStepMode(true);

  }

  public VHDLElement andOperator(char[] pinArray) {
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
    for (int i = 0; i < pinArray.length; i++) {// Eingänge und Augang ohne Wert
                                               // präsentieret
      pinA = new VHDLPin(VHDLPinType.INPUT, " in" + i + " ",
          VHDLPin.VALUE_NOT_DEFINED);
      pins.add(pinA);
    }
    pinY = new VHDLPin(VHDLPinType.OUTPUT, " Y ", VHDLPin.VALUE_NOT_DEFINED);
    pins.add(pinY);
    VHDLElement elem = lang.newAndGate(new Coordinates(40, 100), 100, 400,
        "myAnd", pins, null);
    lang.nextStep();

    for (int k = 0; k < pinArray.length; k++) {// Eingänge mit dem Wert und
      if (pinArray[k] != '0' && pinArray[k] != '1')
        pins.get(k).setValue('x'); // Augang ohne Wert präsentieret
      else
        pins.get(k).setValue(pinArray[k]);
    }

    elem = lang.newAndGate(new Coordinates(40, 100), 100, 400, "myAnd", pins,
        null);
    lang.nextStep();

    for (int j = 0; j < pinArray.length; j++) {
      if (pins.get(j).getValue() == 'x') {
        pinY.setValue('x');
        elem = lang.newAndGate(new Coordinates(40, 100), 100, 400, "myAnd",
            pins, null);
        return elem;
      } else if (pins.get(j).getValue() == '0')
        outValue = '0';

    }
    sc.highlight(8);
    pinY.setValue(outValue);
    elem = lang.newAndGate(new Coordinates(40, 100), 100, 400, "myAnd", pins,
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
    lang.newText(new Coordinates(180, 30), "AND-Gatter", "title", null, txtProp);
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
    sc.addCodeLine("entity  AND-Gatter is", null, 0, null);
    sc.addCodeLine("port (in0,in1: in std_logic; Y : out std_logic);", null, 1,
        null);
    sc.addCodeLine("end AND-Gatter;", null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("architecture Verhalten of AND-Gatter is", null, 0, null);
    sc.addCodeLine("begin", null, 0, null);
    sc.addCodeLine(
        "Y <= in0 and in1;   ///AND-Operator ist für mehr als zwei Eingängen erweiterbar: Y <= ((in0 and in1) and in2) and in3) and ...  ",
        null, 1, null);

    sc.addCodeLine("end  Verhalten;", null, 0, null);

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> arg) {
    AndGatter and = new AndGatter();
    and.init();

    int n = (Integer) arg.get("n");
    char[] pinArray1 = new char[n];

    for (int i = 0; i < pinArray1.length; i++) {
      pinArray1[i] = ((String) arg.get("in" + i)).charAt(0);

    }
    andOperator(pinArray1);
    return lang.toString();
  }

  public String getAlgorithmName() {
    return "AND-Gatter";
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
    return "Illustrates how an AND gate works";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_HARDWARE);
  }

  public String getName() {
    return "AND-Gatter";
  }

  public String getOutputLanguage() {
    return "VHDL";
  }

  public static void main(String[] args) {
    AndGatter and = new AndGatter();
    and.init();
    Hashtable<String, Object> ht = new Hashtable<String, Object>(15);
    ht.put("n", 5);
    ht.put("in0", "1");
    ht.put("in1", "1");
    ht.put("in2", "1");
    ht.put("in3", "x");
    ht.put("in4", "z");
    ht.put("in5", "1");
    ht.put("in6", "1");
    ht.put("in7", "z");
    ht.put("in8", "1");

    System.err.println(and.generate(null, ht));

  }
}
