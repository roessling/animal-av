import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.VHDLLanguage;
import algoanim.primitives.vhdl.VHDLElement;
import algoanim.primitives.vhdl.VHDLPin;
import algoanim.primitives.vhdl.VHDLPinType;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

public class RSFlipflop implements Generator {
  private VHDLLanguage             lang;
//  private SourceCode           sc;
  private SourceCodeProperties sourceCodeProps;
  private char                 Q = 'x', Qbar = 'x';

  public void init() {
    lang = new AnimalScript("RS-FLIPFLOP", "Golsa Arashloozadeh", 640, 480);
    lang.setStepMode(true);

  }

  public VHDLElement RSOperator(char[] Pinarray) {

    showSourceCode();
    // / Highlighting wird hier gemacht

    Vector<VHDLPin> pins = new Vector<VHDLPin>(8);
    VHDLPin R = new VHDLPin(VHDLPinType.INPUT, "R", Pinarray[0]);
    pins.add(R);
    VHDLPin S = new VHDLPin(VHDLPinType.INPUT, "S", Pinarray[1]);
    pins.add(S);
    VHDLPin Set = new VHDLPin(VHDLPinType.SET, "Set", Pinarray[2]);
    pins.add(Set);
    VHDLPin Reset = new VHDLPin(VHDLPinType.RESET, "Reset", Pinarray[3]);
    pins.add(Reset);
    VHDLPin Clk = new VHDLPin(VHDLPinType.CLOCK, "Clk", Pinarray[4]);
    pins.add(Clk);
    VHDLPin EnClk = new VHDLPin(VHDLPinType.CLOCK_ENABLE, "EnClk", Pinarray[5]);
    pins.add(EnClk);

    String RS = String.valueOf(Pinarray[0]).concat(String.valueOf(Pinarray[1]));
    String SetReset = String.valueOf(Pinarray[2]).concat(String.valueOf(Pinarray[3]));
    String ClkEnClk = String.valueOf(Pinarray[4]).concat(String.valueOf(Pinarray[5]));
    

    if (SetReset.equals("01")) { // RESET

      Q = '0';
      Qbar = '0';
    } else if (SetReset.equals("10")) {// SET

      Q = '1';
      Qbar = '1';

    } else if (ClkEnClk.equals("11")) {// ansteigende Flanke

      if (RS.equals("00")) {
        if (Q != 'x')
          Qbar = (Q == '0') ? '1' : '0';

      } else if (RS.equals("01")) {
        Q = '1';// Setzen
        Qbar = (Q == '0') ? '1' : '0';

      } else if (RS.equals("10")) {
        Q = '0';// Loeschen
        Qbar = (Q == '0') ? '1' : '0';

      } else if (RS.equals("11")) {
        Qbar = 'x'; // instabil
        Q = 'x';

      }
    }

    pins.add(new VHDLPin(VHDLPinType.OUTPUT, "Q", Q));
    pins.add(new VHDLPin(VHDLPinType.OUTPUT, "Qbar", Qbar));
    VHDLElement elem = lang.newRSFlipflop(new Coordinates(20, 100), 280, 360,
        "myRS", pins, null);
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
    char[] Pinarray = new char[6];
    String R = (String) arg.get("R");
    Pinarray[0] = R.charAt(0);
    String S = (String) arg.get("S");
    Pinarray[1] = S.charAt(0);
    String Set = (String) arg.get("Set");
    Pinarray[2] = Set.charAt(0);
    String Reset = (String) arg.get("Reset");
    Pinarray[3] = Reset.charAt(0);
    String Clk = (String) arg.get("Clk");
    Pinarray[5] = Clk.charAt(0);
    String EnClk = (String) arg.get("EnClk");
    Pinarray[4] = EnClk.charAt(0);
    RSOperator(Pinarray);
    return lang.toString();
  }

  public String getAlgorithmName() {
    return "RSFlipflop";
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
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getName() {
    return "RSFlipflop";
  }

  public String getOutputLanguage() {
    return "AnimalScript";
  }

  public static void main(String[] args) {
    RSFlipflop rs = new RSFlipflop();
    rs.init();
    Hashtable<String, Object> ht = new Hashtable<String, Object>(7);
    ht.put("R", "0");
    ht.put("S", "0");
    ht.put("Set", "0");
    ht.put("Reset", "0");
    ht.put("Clk", "1");
    ht.put("EnClk", "1");
    System.err.println(rs.generate(null, ht));
    ht.put("R", "1");
    ht.put("S", "0");
    System.err.println(rs.generate(null, ht));
    ht.put("R", "0");
    ht.put("S", "0");
    System.err.println(rs.generate(null, ht));
    // System.out.println(and.getCodeExample());
  }
}
