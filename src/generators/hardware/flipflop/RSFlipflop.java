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

public class RSFlipflop implements Generator {
  private VHDLLanguage         lang;
  private SourceCode           sc;
  private SourceCodeProperties sourceCodeProps;
  private TextProperties       txtProp;

  public void init() {
    lang = new AnimalScript("RS-FLIPFLOP", "Golsa Arashloozadeh", 640, 480);
    lang.setStepMode(true);
  }

  public void unHighLightZeilen(int[] Zeile) {
    for (int i = 0; i < Zeile.length; i++)
      sc.unhighlight(Zeile[i]);

  }

  public void HighLightZeilen(int[] Zeile) {
    for (int i = 0; i < Zeile.length; i++)
      sc.highlight(Zeile[i]);

  }

  public VHDLElement RSOperator(char[] Pinarray) {
    setTitle();
    showSourceCode1();
    char Qm = Pinarray[6];
    Vector<VHDLPin> pins = new Vector<VHDLPin>(8);
    VHDLPin S = new VHDLPin(VHDLPinType.INPUT, " S ", VHDLPin.VALUE_NOT_DEFINED);
    pins.add(S);
    VHDLPin R = new VHDLPin(VHDLPinType.INPUT, " R ", VHDLPin.VALUE_NOT_DEFINED);
    pins.add(R);
    VHDLPin AReset = new VHDLPin(VHDLPinType.RESET, " AReset ",
        VHDLPin.VALUE_NOT_DEFINED);
    pins.add(AReset);
    VHDLPin ASet = new VHDLPin(VHDLPinType.SET, " ASet ",
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
    VHDLPin Qbar = new VHDLPin(VHDLPinType.OUTPUT, " Qbar ",
        VHDLPin.VALUE_NOT_DEFINED);
    pins.add(Qbar);

    VHDLElement elem = lang.newRSFlipflop(new Coordinates(20, 100), 250, 500,
        "myRS", pins, null);
    lang.nextStep();
    for (int i = 0; i < Pinarray.length; i++) {
      if (Pinarray[i] != '0' && Pinarray[i] != '1')
        Pinarray[i] = 'x';
    }
    S.setValue(Pinarray[0]);
    R.setValue(Pinarray[1]);
    AReset.setValue(Pinarray[2]);
    ASet.setValue(Pinarray[3]);
    Clk.setValue(Pinarray[4]);
    EnClk.setValue(Pinarray[5]);

    elem = lang.newRSFlipflop(new Coordinates(20, 100), 250, 500, "myRS", pins,
        null);

    lang.nextStep();

    String SR = String.valueOf(S.getValue()).concat(
        String.valueOf(R.getValue()));
    String ClkEnClk = String.valueOf(Clk.getValue()).concat(
        String.valueOf(EnClk.getValue()));

  if ((AReset.getValue() == 'x')
      ||( ASet.getValue()== 'x')) {
      Q.setValue('x');
      Qbar.setValue('x');
      HighLightZeilen(new int[] { 31, 32,33 });
      System.out.println("hallo-1");
      elem = lang.newRSFlipflop(new Coordinates(20, 100), 250, 500, "myrs",
          pins, null);
      lang.nextStep();
      unHighLightZeilen(new int[] { 31, 32,33 });
      return elem;
    } else if (AReset.getValue() == '1') { // ARESET
      if (ASet.getValue() == '1') {
        HighLightZeilen(new int[] { 28, 29, 30 });
        Q.setValue('x');
        Qbar.setValue('x');
      } else {
        HighLightZeilen(new int[] { 10, 11, 12 });
        Q.setValue('0');
        Qbar.setValue('0');
      }

      elem = lang.newRSFlipflop(new Coordinates(20, 100), 250, 500, "myRS",
          pins, null);
      lang.nextStep();
      unHighLightZeilen(new int[] { 28, 29, 30, 10, 11, 12 });
      return elem;

    } else if (ASet.getValue() == '1') {// ASET
      HighLightZeilen(new int[] { 13, 14, 15 });
      Q.setValue('1');
      Qbar.setValue('1');

      elem = lang.newRSFlipflop(new Coordinates(20, 100), 250, 500, "myRS",
          pins, null);
      lang.nextStep();
      unHighLightZeilen(new int[] { 13, 14, 15 });
      return elem;

    } else if (ClkEnClk.equals("11")) {// ansteigende Flanke
      HighLightZeilen(new int[] { 16, 17 });
      if (SR.equals("00")) {// Speichern
        HighLightZeilen(new int[] { 18, 19, 20 });
        if (Qm != '0' && Qm != '1') {// ungültige Werte für Qm
          Q.setValue('x');
          Qbar.setValue('x');
        } else {
     
        Q.setValue(Qm);
        Qbar.setValue((Qm == '0') ? '1' : '0');
        }

        elem = lang.newRSFlipflop(new Coordinates(20, 100), 250, 500, "myRS",
            pins, null);
        setText(new Offset(30, 95, elem, AnimalScript.DIRECTION_NE), "=Qm",
            "Qm", 12);
        setText(new Offset(47, -100, elem, AnimalScript.DIRECTION_SE), "=not Qm",
            "=not Qm", 12);
        lang.nextStep();
        unHighLightZeilen(new int[] { 16, 17, 18, 19, 20 });
        return elem;
      }

      else if (SR.equals("01")) {
        HighLightZeilen(new int[] { 21, 22, 23 });
        Q.setValue('0');// Löschen
        Qbar.setValue('1');
        ;

      } else if (SR.equals("10")) {
        HighLightZeilen(new int[] { 24, 25, 26 });
        Q.setValue('1');// Setzen
        Qbar.setValue('0');
}
      else if(SR.equals("11")){
      Q.setValue('x');
      Qbar.setValue('x');
      HighLightZeilen(new int[] { 27, 28, 29 });
      
    }
      else{
        Q.setValue('x');
        Qbar.setValue('x');
      }
    }
      else {
      HighLightZeilen(new int[] { 31, 32,33 });
      Q.setValue('x');
      Qbar.setValue('x');

    }

    elem = lang.newRSFlipflop(new Coordinates(20, 100), 250, 500, "myRS", pins,
        null);
    lang.nextStep();
    unHighLightZeilen(new int[] { 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
        28, 29, 31 ,32,33});
    return elem;
  }

  public void setTitle() {
    txtProp = new TextProperties();
    txtProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
    txtProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 24));
    lang.newText(new Coordinates(130, 30), "RS-Flipflop", "title",
        null, txtProp);
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
    sc = lang.newSourceCode(new Coordinates(490, 0), "sourceCode", null,
        sourceCodeProps);

    sc.addCodeLine("LIBRARY IEEE;", null, 0, null);
    sc.addCodeLine("USE IEEE.STD_LOGIC_1164.ALL;", null, 0, null);
    sc.addCodeLine("entity RS-Flipflop is", null, 0, null);
    sc.addCodeLine(
        "port (R, S,CLK,AReset,ASet: in std_logic;Q, Qbar: buffer std_logic);",
        null, 0, null);
    sc.addCodeLine("end RS-Flipflop;", null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("architecture Verhalten of RS-Flipflop is", null, 0, null);
    sc.addCodeLine("begin", null, 0, null);
    sc.addCodeLine("P1: process( CLK,AReset,ASet,Enable )", null, 0, null);
    sc.addCodeLine("begin", null, 0, null);
    sc.addCodeLine("if (AReset='1') then", null, 1, null);//10
    sc.addCodeLine("Q <= '0'; ", null, 2, null);
    sc.addCodeLine("Qbar<='0';", null, 2, null);
    sc.addCodeLine("elsif (ASet='1') then", null, 1, null);
    sc.addCodeLine("Q <= '1'; ", null, 2, null);
    sc.addCodeLine("Qbar<='1';", null, 2, null);
    sc.addCodeLine("elsif ( CLK'event and CLK = '1' ) then ", null, 1, null);
    sc.addCodeLine("if(Enable='1') then ", null, 2, null);
    sc.addCodeLine("if (S='0' and R='0')  then //Speichern", null, 3, null);
    sc.addCodeLine("Q <= Q;", null, 4, null);
    sc.addCodeLine("Q bar <= not Q", null, 4, null);//20
    sc.addCodeLine("elsif (S='0' and R='1')  then//Loeschen", null, 3, null);
    sc.addCodeLine("Q <= '0';", null, 4, null);
    sc.addCodeLine("Qbar='1';", null, 4, null);
    sc.addCodeLine("elsif (S='1' and R='0')  then//Setzen", null, 3, null);
    sc.addCodeLine("Q <= '1';", null, 4, null);
    sc.addCodeLine("Qbar='0';", null, 4, null);
    sc.addCodeLine("elsif (S='1' and R='1')  then//unstabil", null, 3, null);
    sc.addCodeLine("Q <= 'X';", null, 4, null);
    sc.addCodeLine("Qbar='X';", null, 4, null);
    sc.addCodeLine("end if ;", null, 3, null);//30
    sc.addCodeLine("else ", null, 1, null);
    sc.addCodeLine("Q <= 'X';", null, 2, null);
    sc.addCodeLine("Qbar= 'X';", null, 2, null);

    sc.addCodeLine("end if ;", null, 1, null);
    sc.addCodeLine("end if ;", null, 0, null);
    sc.addCodeLine("end process ;", null, 0, null);
    sc.addCodeLine("end Verhalten;", null, 0, null);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> arg) {
    RSFlipflop rs = new RSFlipflop();
    rs.init();
    char[] Pinarray = new char[8];
    Pinarray[0] = ((String) arg.get("S")).charAt(0);
    Pinarray[1] = ((String) arg.get("R")).charAt(0);
    Pinarray[2] = ((String) arg.get("AReset")).charAt(0);
    Pinarray[3] = ((String) arg.get("ASet")).charAt(0);
    Pinarray[4] = ((String) arg.get("Clk")).charAt(0);
    Pinarray[5] = ((String) arg.get("EnClk")).charAt(0);
    Pinarray[6] = ((String) arg.get("Qm")).charAt(0);

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
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_HARDWARE);
  }

  public String getName() {
    return "RSFlipflop";
  }

  public String getOutputLanguage() {
    return "VHDL";
  }

  public static void main(String[] args) {
    RSFlipflop rs = new RSFlipflop();
    rs.init();
    Hashtable<String, Object> ht = new Hashtable<String, Object>(7);
    ht.put("S", "0");
    ht.put("R", "1");
    ht.put("AReset", "0");
    ht.put("ASet", "0");
    ht.put("Clk", "1");
    ht.put("EnClk", "1");
    
    
    ht.put("Qm", "0");

    rs.generate(null, ht);

    System.err.println(rs.lang.toString());

  }
}
