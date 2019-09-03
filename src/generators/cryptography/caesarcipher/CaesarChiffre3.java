package generators.cryptography.caesarcipher;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class CaesarChiffre3 implements generators.framework.Generator {

  protected Language      lang;
  private TextProperties  txtProb, txt1Prob;
  private ArrayProperties arrayProps;
  private SourceCode      sc;

  public void verschluessle(int Shift, byte[] plaintext) {
    Timing time = new TicksTiming(10);
    Text txt = lang.newText(new Coordinates(30, 30), "CaesarChiffre", "title",
        null, txtProb);
    // Rect recht =
    lang.newRect(new Offset(-5, -5, txt, AnimalScript.DIRECTION_NW),
        new Offset(5, 5, txt, AnimalScript.DIRECTION_SE), "recht", null);
    showSourceCode();
    lang.nextStep();
    sc.highlight(0);
    // Text ShiftText =
    lang.newText(new Coordinates(30, 80), "Shift = " + Integer.toString(Shift),
        "shift", null, txt1Prob);
    lang.nextStep();

    char[] tem1 = new String(plaintext).toCharArray();
    String[] tem2 = new String[plaintext.length];
    for (int i = 0; i < plaintext.length; i++)
      tem2[i] = String.valueOf(tem1[i]);
    StringArray Klartext = lang.newStringArray(new Coordinates(30, 150), tem2,
        "KlarText", null, arrayProps);
    // Text txt1 =
    lang.newText(new Offset(0, -20, Klartext, AnimalScript.DIRECTION_NW),
        "KlarText", "klartext", null, txt1Prob);
    lang.nextStep();
    sc.toggleHighlight(0, 2);
    sc.highlight(3);
    String[] plainAlphabet = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
        "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
        "X", "Y", "Z" };
    StringArray KlarAlphabet = lang.newStringArray(new Coordinates(30, 250),
        plainAlphabet, "KlarAlphabet", null, arrayProps);
    Text txt2 = lang.newText(new Offset(0, -20, KlarAlphabet,
        AnimalScript.DIRECTION_NW), "KlarAlphabet", "klaralphabet", null,
        txt1Prob);

    lang.nextStep();
    sc.unhighlight(2);
    sc.unhighlight(3);
    sc.highlight(4);
    String[] cipherAlphabet = new String[26];
    for (int i = 0; i < plainAlphabet.length; i++) {
      cipherAlphabet[i] = "  ";
    }

    StringArray ChiffreAlphabet = lang.newStringArray(new Coordinates(30, 350),
        cipherAlphabet, "ChiffreAlphabet", null, arrayProps);
    // Text txt3 =
    lang.newText(
        new Offset(0, -20, ChiffreAlphabet, AnimalScript.DIRECTION_NW),
        "ChiffreAlphabet", "chiffrealphabet", null, txt1Prob);
    Text Shift2 = lang.newText(new Offset(40, -20, txt2,
        AnimalScript.DIRECTION_E), "shift: " + String.valueOf(Shift), "sh",
        null, txt1Prob);
    lang.nextStep();
    sc.unhighlight(4);
    for (int i = 0; i < Shift; i++) {
      KlarAlphabet.highlightCell(i, null, time);
      Shift2.setText("shift: " + String.valueOf(Shift - i), null, null);
      lang.nextStep();
      KlarAlphabet.unhighlightCell(i, null, time);
    }
    Shift2.setText("shift: 0", null, null);
    KlarAlphabet.highlightCell(Shift, null, time);
    lang.nextStep();
    Shift2.hide();

    for (int i = 0; i < plainAlphabet.length; i++) {
      sc.highlight(6);
      ChiffreAlphabet.highlightCell(i, null, time);
      lang.nextStep();
      sc.toggleHighlight(6, 7);
      KlarAlphabet.highlightCell((i + Shift) % 26, null, time);
      ChiffreAlphabet.highlightCell(i, null, time);
      ChiffreAlphabet
          .put(i, KlarAlphabet.getData((i + Shift) % 26), null, null);
      lang.nextStep();
      sc.unhighlight(7);
      KlarAlphabet.unhighlightCell((i + Shift) % 26, null, time);
      ChiffreAlphabet.unhighlightCell(i, null, time);
    }

    lang.nextStep();
    String[] a = new String[plaintext.length];
    for (int i = 0; i < plaintext.length; i++)
      a[i] = "  ";
    StringArray ver = lang.newStringArray(new Coordinates(30, 450), a,
        "ChAlphabet", null, arrayProps);
    // Text txt4 =
    lang.newText(new Offset(0, -20, ver, AnimalScript.DIRECTION_NW),
        "ChiffreText", "chiffretext", null, txt1Prob);

    for (int i = 0; i < plaintext.length; i++) {
      sc.highlight(12);
      Klartext.highlightCell(i, null, time);
      lang.nextStep();
      KlarAlphabet.highlightCell(Byte.valueOf(plaintext[i]) - 65, null, time);
      lang.nextStep();
      ChiffreAlphabet
          .highlightCell(Byte.valueOf(plaintext[i]) - 65, null, time);
      lang.nextStep();
      sc.toggleHighlight(12, 13);
      ver.highlightCell(i, null, time);
      ver.put(i, ChiffreAlphabet.getData(Byte.valueOf(plaintext[i]) - 65),
          null, null);
      lang.nextStep();
      sc.unhighlight(13);
      Klartext.unhighlightCell(i, null, time);
      KlarAlphabet.unhighlightCell(Byte.valueOf(plaintext[i]) - 65, null, time);
      ChiffreAlphabet.unhighlightCell(Byte.valueOf(plaintext[i]) - 65, null,
          time);
      ver.unhighlightCell(i, null, time);
    }
    sc.highlight(16);
//    Text Ende =
      lang.newText(new Coordinates(550, 500), "ENDE", "ende", null,
        txtProb);
    lang.nextStep();
    sc.unhighlight(16);

  }

  public void init() {
    lang = new AnimalScript("CaesarChiffre-Demo",
        "iaad zabar und abdulghani alshadadi", 640, 480);
    lang.setStepMode(true);

    txtProb = new TextProperties();
    txtProb.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
    txtProb.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 24));
    txt1Prob = new TextProperties();
    txt1Prob.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    txt1Prob.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 14));

    arrayProps = new ArrayProperties();
    /*
     * arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,Color.red);
     * arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,true);
     * arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY,Color.gray);
     * arrayProps
     * .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,Color.yellow);
     * arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
     * Color.black);
     */

  }

  public void showSourceCode() {
//    Rect ln = 
      lang.newRect(new Coordinates(500, 10), new Coordinates(500, 500),
        "line", null);
    SourceCodeProperties scProb = new SourceCodeProperties();
    scProb.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.blue);
    scProb.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 14));
    scProb.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.red);
    scProb.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);

    sc = lang.newSourceCode(new Coordinates(550, 40), "sourceCode", null,
        scProb);

    sc.addCodeLine(
        "public static String encrypt(int shiftValue,byte[] plainText) {",
        null, 0, null);
    sc.addCodeLine("", null, 0, null);
    sc
        .addCodeLine(
            "Char[] plainAlphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M',",
            null, 1, null);
    sc.addCodeLine("'N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};",
        null, 3, null);
    sc.addCodeLine("Char[] cipherAlphabet = new String[26];", null, 1, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("for (int i = 0; i < plainAlphabet.length; i++) {", null, 1,
        null);
    sc.addCodeLine("cipherAlphabet[i] = plainAlphabet[(i+shiftValue)%26];",
        null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine(
        "StringBuilder cipherText = new StringBuilder(plainText.length);",
        null, 1, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("for (int i = 0; i < plainText.length; i++) {", null, 1,
        null);
    sc.addCodeLine(
        "cipherText.append(cipherAlphabet[Byte.valueOf(plainText[i])-65]);",
        null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("return cipherText.toString();", null, 1, null);
    sc.addCodeLine("", null, 0, null);
    sc.addCodeLine("}", null, 0, null);
  }

  public byte[] convert(String[] a) {
    byte[] b = new byte[a.length];
    for (int i = 0; i < a.length; i++)
      b[i] = a[i].getBytes()[0];
    return b;
  }

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    CaesarChiffre3 probe = new CaesarChiffre3();
    probe.init();
    probe.arrayProps = (ArrayProperties) arg0.getPropertiesByName("arrayProps");
    int sh = ((Integer) arg1.get("shift"));
    String[] klar = (String[]) arg1.get("klartext");
    byte[] iaad = probe.convert(klar);
    probe.verschluessle(sh, iaad);
    return probe.lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Caesar Cipher";
  }

  @Override
  public String getAnimationAuthor() {
    return "Iaad Zabar, Abdulghani Alshadadi";
  }

  @Override
  public String getCodeExample() {

    return "public static String encrypt(int shiftValue,byte[] plainText) {"
        + "\n"
        + ""
        + "\n"
        + "Char[] plainAlphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M',"
        + "\n" + "'N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};" + "\n"
        + "Char[] cipherAlphabet = new String[26];" + "\n" + "" + "\n"
        + "for (int i = 0; i < plainAlphabet.length; i++) {" + "\n"
        + "cipherAlphabet[i] = plainAlphabet[(i+shiftValue)%26];" + "\n" + "}"
        + "\n" + "" + "\n"
        + "StringBuilder cipherText = new StringBuilder(plainText.length);"
        + "\n" + "" + "\n" + "for (int i = 0; i < plainText.length; i++) {"
        + "\n"
        + "cipherText.append(cipherAlphabet[Byte.valueOf(plainText[i])-65]);"
        + "\n" + "}" + "\n" + "" + "\n" + "return cipherText.toString();"
        + "\n" + "" + "\n" + "}";
  }

  @Override
  public Locale getContentLocale() {

    return Locale.GERMANY;

  }

  @Override
  public String getDescription() {

    return "Dieses Kodierungsverfahren basiert auf der Gegenüberstellung"
        + "\n"
        + "eines Klartextalphabetes und eines Geheimtextalphabetes."
        + "\n"
        + "Dabei wird jedem Buchstaben eines Klartextes ein anderer,"
        + "\n"
        + "eindeutiger Buchstabe des Geheimtextes zugewiesen."
        + "\n"
        + "Die Zuordnung basiert auf der zyklischen Rotation des Alphabets um x Zeichen, dabei"
        + "\n"
        + "folgt auf Z wieder A. Das x ist der Schlüssel, mit dem verschlüsselt bzw. entschlüsselt wird.";
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  @Override
  public String getName() {

    return "CaesarChiffre";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}
