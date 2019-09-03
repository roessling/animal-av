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

public class NotGatter implements Generator {
  private VHDLLanguage         lang;
  private SourceCode           sc;
  private SourceCodeProperties sourceCodeProps;
  private TextProperties       txtProp;

  public void init() {
    lang = new AnimalScript("NOT-Gatter", "Golsa Arashloozadeh", 640, 480);
    lang.setStepMode(true);

  }

  public VHDLElement NotOperator(char inValue) {
    setTitle();
    showSourceCode();

    Vector<VHDLPin> pins = new Vector<VHDLPin>(2);
    VHDLPin pinA = new VHDLPin(VHDLPinType.INPUT, " in ",
        VHDLPin.VALUE_NOT_DEFINED);
    pins.add(pinA);
    VHDLPin pinY = new VHDLPin(VHDLPinType.OUTPUT, " Y ",
        VHDLPin.VALUE_NOT_DEFINED);
    pins.add(pinY);
    VHDLElement elem = lang.newNotGate(new Coordinates(40, 100), 100, 100,
        "myNot", pins, null);
    lang.nextStep();
    if (inValue != '0' && inValue != '1')

      pinA.setValue('x');
    else
      pinA.setValue(inValue);
    elem = lang.newNotGate(new Coordinates(40, 100), 100, 100, "myNot", pins,
        null);
    lang.nextStep();

    if (inValue != '0' && inValue != '1') {// ungültige Eingabe
      pinY.setValue('x');
      elem = lang.newNotGate(new Coordinates(40, 100), 100, 100, "myNot", pins,
          null);
      return elem;
    } else {
      pinY.setValue((inValue == '0') ? '1' : '0');
      sc.highlight(8);

      elem = lang.newNotGate(new Coordinates(40, 100), 100, 100, "myNot", pins,
          null);
      lang.nextStep();
      sc.unhighlight(8);
    }
    return elem;

  }

  public void setTitle() {
    txtProp = new TextProperties();
    txtProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
    txtProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 24));
    lang.newText(new Coordinates(130, 30), "NOT-Gatter", "title", null, txtProp);
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
    sc.addCodeLine("entity  NOT-Gatter is", null, 0, null);
    sc.addCodeLine("port (in: in std_logic; Y : out std_logic);", null, 1, null);
    sc.addCodeLine("end NOT-Gatter;", null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("architecture Verhalten of NOT-Gatter is", null, 0, null);
    sc.addCodeLine("begin", null, 0, null);
    sc.addCodeLine("Y <= not in ", null, 1, null);
    sc.addCodeLine("end Verhalten", null, 0, null);

  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> arg) {
    NotGatter not = new NotGatter();
    not.init();
    String inValue = (String) arg.get("in");
    NotOperator(inValue.charAt(0));
    return lang.toString();

  }

  public String getAlgorithmName() {
    return "NOT-Gatter";
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
    return "NOT-Gatter";
  }

  public String getOutputLanguage() {
    return "VHDL";
  }

  public static void main(String[] args) {
    NotGatter not = new NotGatter();
    not.init();
    Hashtable<String, Object> ht = new Hashtable<String, Object>(7);
    ht.put("in", "1");

    System.err.println(not.generate(null, ht));

  }
}
