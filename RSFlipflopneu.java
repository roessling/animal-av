import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.VHDLLanguage;
import algoanim.primitives.vhdl.VHDLElement;
import algoanim.primitives.vhdl.VHDLPin;
import algoanim.primitives.vhdl.VHDLPinType;
import algoanim.util.Coordinates;

public class RSFlipflopneu implements Generator {
  private VHDLLanguage   lang;
  // private SourceCode sc;
  // private SourceCodeProperties sourceCodeProps;
  private char       Q = 'x', Qbar = 'x';
  static VHDLElement elemG;
  VHDLElement rsFlipflop = null;
  
  public void init() {
    lang = new AnimalScript("RS-FLIPFLOP", "Golsa Arashloozadeh", 640, 480);
    lang.setStepMode(true);
  }

  public VHDLElement RSOperator(char[] Pinarray) {

    // showSourceCode();
    // Highlighting wird hier gemacht.

    Vector<VHDLPin> pins = new Vector<VHDLPin>(8);

    pins.add(new VHDLPin(VHDLPinType.INPUT, "S", Pinarray[0]));
    pins.add(new VHDLPin(VHDLPinType.INPUT, "R", Pinarray[1]));
    pins.add(new VHDLPin(VHDLPinType.SET, "Set", Pinarray[2]));
    pins.add(new VHDLPin(VHDLPinType.RESET, "Reset", Pinarray[3]));
    pins.add(new VHDLPin(VHDLPinType.CLOCK, "Clk", Pinarray[4]));
    pins.add(new VHDLPin(VHDLPinType.CLOCK_ENABLE, "EnClk", Pinarray[5]));

    String SR = String.valueOf(Pinarray[0]).concat(String.valueOf(Pinarray[1]));
    String SetReset = String.valueOf(Pinarray[2]).concat(
        String.valueOf(Pinarray[3]));
    String ClkEnClk = String.valueOf(Pinarray[4]).concat(
        String.valueOf(Pinarray[5]));

    if (SetReset.equals("01")) { // RESET

      Q = '0';
      Qbar = '0';
    } else if (SetReset.equals("10")) {// SET

      Q = '1';
      Qbar = '1';

    } else if (ClkEnClk.equals("11")) {// ansteigende Flanke

      if (SR.equals("00")) {// Speichern
        if (Q != 'x')
          Qbar = (Q == '0') ? '1' : '0';

      } else if (SR.equals("10")) {
        Q = '1';// Setzen
        Qbar = (Q == '0') ? '1' : '0';

      } else if (SR.equals("01")) {
        Q = '0';// Loeschen
        Qbar = (Q == '0') ? '1' : '0';

      } else if (SR.equals("11")) {
        Qbar = 'x'; // instabil
        Q = 'x';

      }
    }

    pins.add(new VHDLPin(VHDLPinType.OUTPUT, "Q", Q));
    pins.add(new VHDLPin(VHDLPinType.OUTPUT, "Qbar", Qbar));
    VHDLElement elem = lang.newRSFlipflop(new Coordinates(20, 100), 100, 400,
        "myRS", pins, null);
//    if (rsFlipflop != null)
//      rsFlipflop.hide();
    rsFlipflop = elem;
//    lang.nextStep();
//    elemG = elem;
    return elem;
  }

  /*
   * private void showSourceCode() { sourceCodeProps = new
   * SourceCodeProperties();
   * sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
   * Color.RED); sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new
   * Font( Font.MONOSPACED, Font.PLAIN, 20)); SourceCode sc =
   * lang.newSourceCode(new Coordinates(40, 140), "sourceCode", null,
   * sourceCodeProps); sc.addCodeLine(null,null,0,null); }
   */

  public void createContents(Hashtable<String, Object> arg) {
    char[] Pinarray = new char[6];
    Pinarray[0] = ((String) arg.get("S")).charAt(0);
    Pinarray[1] = ((String) arg.get("R")).charAt(0);
    Pinarray[2] = ((String) arg.get("Set")).charAt(0);
    Pinarray[3] = ((String) arg.get("Reset")).charAt(0);
    Pinarray[4] = ((String) arg.get("Clk")).charAt(0);
    Pinarray[5] = ((String) arg.get("EnClk")).charAt(0);
//    rsFlipflop = 
    if (rsFlipflop != null)
      rsFlipflop.hide();
    rsFlipflop = RSOperator(Pinarray);
    lang.nextStep();
  }
  
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> arg) {
    createContents(arg);
    
    // modify values
    arg.put("R", "1");
    arg.put("S", "0");
    createContents(arg);
    
    arg.put("R", "0");
    arg.put("S", "0");
    createContents(arg);

    return lang.toString();
//    char[] Pinarray = new char[6];
//    Pinarray[0] = ((String) arg.get("S")).charAt(0);
//    Pinarray[1] = ((String) arg.get("R")).charAt(0);
//    Pinarray[2] = ((String) arg.get("Set")).charAt(0);
//    Pinarray[3] = ((String) arg.get("Reset")).charAt(0);
//    Pinarray[4] = ((String) arg.get("Clk")).charAt(0);
//    Pinarray[5] = ((String) arg.get("EnClk")).charAt(0);
////    rsFlipflop = 
//    if (rsFlipflop != null)
//      rsFlipflop.hide();
//    rsFlipflop = RSOperator(Pinarray);
//    lang.nextStep();
//    return lang.toString();
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
    RSFlipflopneu rs = new RSFlipflopneu();
    rs.init();
    Hashtable<String, Object> ht = new Hashtable<String, Object>(7);
    ht.put("S", "1");
    ht.put("R", "0");
    ht.put("Set", "0");
    ht.put("Reset", "0");
    ht.put("Clk", "1");
    ht.put("EnClk", "1");
//    if (rs.rsFlipflop != null)
//      rs.rsFlipflop.hide();
//    System.out.println(
    rs.generate(null, ht);
    

    // elemG.hide();
    // ht.clear();

    // RSFlipflop rs1 = new RSFlipflop();
    // ht.put("S", "0");
    // ht.put("R", "1");
    // ht.put("Set", "0");
    // ht.put("Reset", "0");
    // ht.put("Clk", "1");
    // ht.put("EnClk", "1");
    // System.out.println(rs1.generate(null, ht));

    // System.out.println(and.getCodeExample());
    
//    lang.nextStep();
//    ht.put("R", "1");
//    ht.put("S", "0");
//    if (rs.rsFlipflop != null)
//      rs.rsFlipflop.hide();
//    rs.generate(null, ht);
//    System.err.println(rs.generate(null, ht));
//    ht.put("R", "0");
//    ht.put("S", "0");
//    if (rs.rsFlipflop != null)
//      rs.rsFlipflop.hide();
//    rs.generate(null, ht);
    System.err.println(rs.lang.toString());
//    System.err.println(rs.generate(null, ht));

    // System.out.println(and.getCodeExample());
  }
}
