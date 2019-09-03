import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.primitives.vhdl.VHDLElement;
import algoanim.primitives.vhdl.VHDLPin;
import algoanim.primitives.vhdl.VHDLPinType;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

public class AndGatter {
  private Language             lang;
//  private SourceCode           sc;
  private SourceCodeProperties sourceCodeProps;

  public void init() {
    lang = new AnimalScript("AND-Gatter", "Golsa Arashloozadeh", 640, 480);
    lang.setStepMode(true);
    // ////alle properties

  }

  public VHDLElement andOperator(char[] pinArray) {

    showSourceCode();
    // / Highlighting wird hier gemacht
    char outValue = '1';
    Vector<VHDLPin> pins = new Vector<VHDLPin>(pinArray.length + 1);
    for (int i = 0; i < pinArray.length; i++) {
      VHDLPin pinA = new VHDLPin(VHDLPinType.INPUT, "in" + i, pinArray[i]);
      pins.add(pinA);
      if (pins.get(i).getValue() == '0')
        outValue = '0';
    }

    pins.add(new VHDLPin(VHDLPinType.OUTPUT, "Y", outValue));
    VHDLElement elem = lang.newAndGate(new Coordinates(20, 100), 80, 360,
        "myAnd", pins, null);
    lang.nextStep();
    return elem;
  }

  private void showSourceCode() {
    sourceCodeProps = new SourceCodeProperties();
    sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.MONOSPACED, Font.PLAIN, 20));
//    SourceCode sc = 
    lang.newSourceCode(new Coordinates(40, 140), "sourceCode",
        null, sourceCodeProps);
    // sc.addCodeLine(null,null,0,null);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> arg) {

    Set<String> key = arg.keySet();
    char[] Pinarray = new char[key.size()];
    for (Iterator<String> it = key.iterator(); it.hasNext();) {//arg iterieren durch Keys
      for (int i = 0; i < Pinarray.length; i++) {//abspeichern die Inputwerte in array
        String inValue = (String) arg.get(it.next());
        Pinarray[i] = inValue.charAt(0);
      }
    }

    andOperator(Pinarray);
    return lang.toString();
  }

  // ensure all properties are set up
  // ////This will extract the input the user has generated and pass it to
  // NOT-Gatter
  // / It also adapts one color setting to show how this is done.

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
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getName() {
    return "AND-Gatter";
  }

  public String getOutputLanguage() {
    return "VHDL";
  }

//  public void writeToFile() {
//    try {
//      File file = new File("AND-Gatter.asu");
//      FileWriter output = new FileWriter(file);
//      output.write(lang.toString());
//      output.flush();
//      output.close();
//    } catch (IOException ex) {
//      System.err.print("Can't write the file! \n" + ex.getMessage());
//    }
//  }

  public static void main(String[] args) {
    AndGatter and = new AndGatter();
    and.init();
    Hashtable<String, Object> ht = new Hashtable<String, Object>(7);
    ht.put("inputA", "1");
    ht.put("inputB", "0");
    ht.put("inputC", "0");
    ht.put("inputD", "0");
    ht.put("inputE", "0");
    System.err.println(and.generate(null, ht));

    // System.out.println(and.getCodeExample());
  }
}
