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

public class JKFlipflop implements Generator {

  private VHDLLanguage         lang;
  private SourceCode           sc;
  private SourceCodeProperties sourceCodeProps;
  private TextProperties       txtProp;

  public void init() {
    lang = new AnimalScript("JK-FLIPFLOP", "Golsa Arashloozadeh", 640, 480);
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

  public VHDLElement JKOperator(char[] Pinarray) {
    setTitle();
    showSourceCode1();
    char Qm = Pinarray[6];// vorheriger Zustand
    Vector<VHDLPin> pins = new Vector<VHDLPin>(10);
    VHDLPin J = new VHDLPin(VHDLPinType.INPUT, " J ", VHDLPin.VALUE_NOT_DEFINED);
    pins.add(J);
    VHDLPin K = new VHDLPin(VHDLPinType.INPUT, " K ", VHDLPin.VALUE_NOT_DEFINED);
    pins.add(K);
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

    VHDLElement elem = lang.newJKFlipflop(new Coordinates(20, 100), 250, 500,
        "myJK", pins, null);

    lang.nextStep();
    for (int i = 0; i < Pinarray.length; i++) {
      if (Pinarray[i] != '0' && Pinarray[i] != '1')
        Pinarray[i] = 'x';
    }
    J.setValue(Pinarray[0]);
    K.setValue(Pinarray[1]);
    AReset.setValue(Pinarray[2]);
    ASet.setValue(Pinarray[3]);
    Clk.setValue(Pinarray[4]);
    EnClk.setValue(Pinarray[5]);
    elem = lang.newJKFlipflop(new Coordinates(20, 100), 250, 500, "myJK", pins,
        null);

    lang.nextStep();
    String JK = String.valueOf(J.getValue()).concat(
        String.valueOf(K.getValue()));

    String ClkEnClk = String.valueOf(Clk.getValue()).concat(
        String.valueOf(EnClk.getValue()));

    if ((AReset.getValue() == 'x') || (ASet.getValue() == 'x')) {// ungültige
                                                                 // Werte
                                                                 // für AReset
                                                                 // und
                                                                 // ASet
      Q.setValue('x');
      Qbar.setValue('x');
      HighLightZeilen(new int[] { 30, 31, 32 });
      elem = lang.newJKFlipflop(new Coordinates(20, 100), 250, 500, "myJK",
          pins, null);
      lang.nextStep();
      unHighLightZeilen(new int[] { 30, 31, 32 });
      return elem;
    } else if (AReset.getValue() == '1') { // ARESET
      if (ASet.getValue() == '1') {
        HighLightZeilen(new int[] { 29, 30, 31 });
        Q.setValue('x');
        Qbar.setValue('x');
      } else {
        HighLightZeilen(new int[] { 9, 10, 11 });
        Q.setValue('0');
        Qbar.setValue('0');
      }

      elem = lang.newJKFlipflop(new Coordinates(20, 100), 250, 500, "myJK",
          pins, null);
      lang.nextStep();
      unHighLightZeilen(new int[] { 9, 10, 11, 29, 30, 31 });
      return elem;

    } else if (ASet.getValue() == '1') {// ASET
      HighLightZeilen(new int[] { 12, 13, 14 });

      Q.setValue('1');
      Qbar.setValue('1');
      elem = lang.newJKFlipflop(new Coordinates(20, 100), 250, 500, "myJK",
          pins, null);
      lang.nextStep();
      unHighLightZeilen(new int[] { 12, 13, 14 });
      return elem;

    } else if (ClkEnClk.equals("11")) {// ansteigende Flanke
      HighLightZeilen(new int[] { 15, 16 });

      if (JK.equals("00")) {// Speichern der Vorherigen Zustand Qm
        HighLightZeilen(new int[] { 17, 18, 19 });
        if (Qm != '0' && Qm != '1') {// ungültige Werte für Qm
          Q.setValue('x');
          Qbar.setValue('x');
        } else {
          Q.setValue(Qm);
          Qbar.setValue((Qm == '0') ? '1' : '0');
          setText(new Offset(30, 95, elem, AnimalScript.DIRECTION_NE), "=Qm",
              "Qm", 12, Color.BLACK);
          setText(new Offset(47, -100, elem, AnimalScript.DIRECTION_SE),
              "=not Qm", "=not Qm", 12, Color.BLACK);
        }

        elem = lang.newJKFlipflop(new Coordinates(20, 100), 250, 500, "myJK",
            pins, null);

        lang.nextStep();
        unHighLightZeilen(new int[] { 15, 16, 17, 18, 19 });
        return elem;

      } else if (JK.equals("01")) {
        HighLightZeilen(new int[] { 20, 21, 22 });

        Q.setValue('0');// Löschen
        Qbar.setValue('1');

      } else if (JK.equals("10")) {
        HighLightZeilen(new int[] { 23, 24, 25 });

        Q.setValue('1');// Setzen
        Qbar.setValue('0');

      } else if (JK.equals("11")) {// toggeln der Vorherigen Zustand Qm
        HighLightZeilen(new int[] { 26, 27, 28 });

        if (Qm != '0' && Qm != '1') {// ungültige Werte für Qm
          Q.setValue('x');
          Qbar.setValue('x');
        } else {
          Qbar.setValue(Qm);
          Q.setValue((Qm == '0') ? '1' : '0');
          setText(new Offset(30, 95, elem, AnimalScript.DIRECTION_NE),
              "=not Qm", "Qm", 12, Color.BLACK);
          setText(new Offset(47, -100, elem, AnimalScript.DIRECTION_SE),
              "= Qm", "=not Qm", 12, Color.BLACK);

        }
      } else {

        Q.setValue('x');
        Qbar.setValue('x');
      }
      elem = lang.newJKFlipflop(new Coordinates(20, 100), 250, 500, "myJK",
          pins, null);

      lang.nextStep();
      unHighLightZeilen(new int[] { 15, 16, 20, 21, 22, 23, 24, 25, 26, 27, 28 });
      return elem;

    } else {

      HighLightZeilen(new int[] { 30, 31, 32 });

      Q.setValue('x');
      Qbar.setValue('x');

      elem = lang.newJKFlipflop(new Coordinates(20, 100), 250, 500, "myJK",
          pins, null);
    }

    lang.nextStep();
    unHighLightZeilen(new int[] { 30, 31, 32 });
    return elem;

  }

  public void setTitle() {
    txtProp = new TextProperties();
    txtProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
    txtProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 24));
    lang.newText(new Coordinates(130, 30), "JK-Flipflop", "title", null,
        txtProp);
  }

  private void setText(Offset Offset, String text, String name, int Groesse,
      Color color) {
    TextProperties textProperties = new TextProperties();
    textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, Groesse));
    textProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
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
    sc = lang.newSourceCode(new Coordinates(590, 0), "sourceCode", null,
        sourceCodeProps);

    sc.addCodeLine("LIBRARY IEEE;", null, 0, null);
    sc.addCodeLine("USE IEEE.STD_LOGIC_1164.ALL;", null, 0, null);
    sc.addCodeLine("entity JK-Flipflop is", null, 0, null);
    sc.addCodeLine(
        "port (J, K,CLK,AReset,ASet: in std_logic;Q, Qbar: buffer std_logic);",
        null, 0, null);
    sc.addCodeLine("end JK-Flipflop;", null, 0, null);
    sc.addCodeLine("architecture Verhalten of JK-Flipflop is", null, 0, null);
    sc.addCodeLine("begin", null, 0, null);
    sc.addCodeLine("P1: process( CLK,AReset,ASet,Enable )", null, 0, null);
    sc.addCodeLine("begin", null, 0, null);
    sc.addCodeLine("if (AReset='1') then", null, 1, null);
    sc.addCodeLine("Q <= '0'; ", null, 2, null);
    sc.addCodeLine("Qbar<='0';", null, 2, null);
    sc.addCodeLine("elsif (ASet='1') then", null, 1, null);
    sc.addCodeLine("Q <= '1'; ", null, 2, null);
    sc.addCodeLine("Qbar<='1';", null, 2, null);
    sc.addCodeLine("elsif ( CLK'event and CLK = '1' ) then ", null, 1, null);
    sc.addCodeLine("if(Enable='1') then ", null, 2, null);
    sc.addCodeLine(
        "if (J='0' and k='0')  then //Speichern der Vorherigen Zustand Qm",
        null, 3, null);
    sc.addCodeLine("Q <= Q;", null, 4, null);
    sc.addCodeLine("Q bar <= not Q", null, 4, null);
    sc.addCodeLine("elsif (J='0' and k='1')  then//Loeschen", null, 3, null);
    sc.addCodeLine("Q <= '0';", null, 4, null);
    sc.addCodeLine("Qbar='1';", null, 4, null);
    sc.addCodeLine("elsif (J='1' and k='0')  then//Setzen", null, 3, null);
    sc.addCodeLine("Q <= 1;", null, 4, null);
    sc.addCodeLine("Qbar='0';", null, 4, null);
    sc.addCodeLine(
        "elsif (J='1' and k='1')  then //Toggeln der Vorherigen Zustand Qm",
        null, 3, null);
    sc.addCodeLine("Q <= not Q;", null, 4, null);
    sc.addCodeLine("Qbar= Q;", null, 4, null);
    sc.addCodeLine("end if ;", null, 3, null);
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
    char[] Pinarray = new char[8];
    Pinarray[0] = ((String) arg.get("J")).charAt(0);
    Pinarray[1] = ((String) arg.get("K")).charAt(0);
    Pinarray[2] = ((String) arg.get("AReset")).charAt(0);
    Pinarray[3] = ((String) arg.get("ASet")).charAt(0);
    Pinarray[4] = ((String) arg.get("Clk")).charAt(0);
    Pinarray[5] = ((String) arg.get("EnClk")).charAt(0);
    Pinarray[6] = ((String) arg.get("Qm")).charAt(0);
    JKOperator(Pinarray);
    return lang.toString();
  }

  public String getAlgorithmName() {
    return "JKFlipflop";
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
    return "JKFlipflop";
  }

  public String getOutputLanguage() {
    return "VHDL";
  }

  public static void main(String[] args) {
    JKFlipflop jk = new JKFlipflop();
    jk.init();
    Hashtable<String, Object> ht = new Hashtable<String, Object>(9);
    ht.put("J", "1");
    ht.put("K", "1");
    ht.put("AReset", "5");
    ht.put("ASet", "0");
    ht.put("Clk", "0");
    ht.put("EnClk", "1");
    ht.put("Qm", "p");

    System.err.println(jk.generate(null, ht));
  }
}
