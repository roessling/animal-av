package generators.cryptography.caesarcipher;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Polyline;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class CaesarImp implements Generator {

//  private String             author      = "Malek Boulakbech";
  private String             s           = "CAESAR-CHIFFRE ANIMATION";
  public final String        DESCRIPTION = "Die Caesar-Chiffre ist eines der einfachsten Verfahren zum Verschlüsseln von Nachrichten.";
  public final String        SOURCE_CODE = "public static String encode(String s, int schluessel){\n"

                                             + "\n  int myschluessel = schluessel % 26 ; "
                                             + "\n  StringBuffer ergebnis = new StringBuffer();"
                                             + "\n  char a = ' ';"
                                             + "\n  for (int i=0; i<s.length(); i++)"
                                             + "\n  {"
                                             + "\n     a = s.charAt(i);"
                                             + "\n     if (a >= 'A' && a <= 'Z')"
                                             + "\n	 {"
                                             + "\n        a += myschluessel;"
                                             + "\n        if (a > 'Z')a -= 26;"
                                             + "\n	 }"
                                             + "\n	 ergebnis.append( a );"
                                             + "\n   }"
                                             + "\nreturn ergebnis.toString();"
                                             + "\n\n}";

  private int                line        = 150;
  private int                line_       = 250;
  private Language           lang;
  private TextProperties     textP1;
  private TextProperties     textP2;
  private TextProperties     plainTextProps;
  private TextProperties     cipherTextProps;
  private PolylineProperties p;

  public CaesarImp() {

  }

  public void init() {

    lang = new AnimalScript("Caesar-Chiffre Animation", "Malek Boulakbech", 640, 480);
    lang.setStepMode(true);

    textP2 = new TextProperties();
    textP2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 15));
    textP2.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(139, 0, 139));

    textP1 = new TextProperties();
    textP1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 15));
    textP1
        .set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(130, 130, 130));
    textP1.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);

    p = new PolylineProperties();
    p.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    p.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
    p.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(139, 0, 0));

    plainTextProps = new TextProperties();
    plainTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 15));
    plainTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

    cipherTextProps = new TextProperties();
    cipherTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 15));
    cipherTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
  }

  public void CaesarChiffre(int myschluessel) throws IllegalDirectionException {

    Timing t = new TicksTiming(15);
    Timing delay = new TicksTiming(0);
    int key = myschluessel % 26;
    if (myschluessel >= 26)
      lang.newText(new Coordinates(750, 30), "Key = " + myschluessel + " ---> "
          + key, "key", null, textP2);
    else
      lang.newText(new Coordinates(750, 30), "Key = " + myschluessel, "key",
          null, textP2);
    lang.nextStep();

    lang.newText(new Coordinates(600, line), "Plaintext alphabet", "text1",
        null, textP2);
    lang.newText(new Coordinates(20, line), "A", "1", null, textP1);
    lang.newText(new Coordinates(40, line), "B", "2", null, textP1);
    lang.newText(new Coordinates(60, line), "C", "3", null, textP1);
    lang.newText(new Coordinates(80, line), "D", "4", null, textP1);
    lang.newText(new Coordinates(100, line), "E", "5", null, textP1);
    lang.newText(new Coordinates(120, line), "F", "6", null, textP1);
    lang.newText(new Coordinates(140, line), "G", "7", null, textP1);
    lang.newText(new Coordinates(160, line), "H", "8", null, textP1);
    lang.newText(new Coordinates(180, line), "I", "9", null, textP1);
    lang.newText(new Coordinates(200, line), "J", "10", null, textP1);
    lang.newText(new Coordinates(220, line), "K", "11", null, textP1);
    lang.newText(new Coordinates(240, line), "L", "12", null, textP1);
    lang.newText(new Coordinates(260, line), "M", "13", null, textP1);
    lang.newText(new Coordinates(280, line), "N", "14", null, textP1);
    lang.newText(new Coordinates(300, line), "O", "15", null, textP1);
    lang.newText(new Coordinates(320, line), "P", "16", null, textP1);
    lang.newText(new Coordinates(340, line), "Q", "17", null, textP1);
    lang.newText(new Coordinates(360, line), "R", "18", null, textP1);
    lang.newText(new Coordinates(380, line), "S", "19", null, textP1);
    lang.newText(new Coordinates(400, line), "T", "20", null, textP1);
    lang.newText(new Coordinates(420, line), "U", "21", null, textP1);
    lang.newText(new Coordinates(440, line), "V", "22", null, textP1);
    lang.newText(new Coordinates(460, line), "W", "23", null, textP1);
    lang.newText(new Coordinates(480, line), "X", "24", null, textP1);
    lang.newText(new Coordinates(500, line), "Y", "25", null, textP1);
    lang.newText(new Coordinates(520, line), "Z", "26", null, textP1);

    lang.nextStep();

    t1_ = lang.newText(new Coordinates(20, line), "A", "1_", null, textP1);
    t2_ = lang.newText(new Coordinates(40, line), "B", "2_", null, textP1);
    t3_ = lang.newText(new Coordinates(60, line), "C", "3_", null, textP1);
    t4_ = lang.newText(new Coordinates(80, line), "D", "4_", null, textP1);
    t5_ = lang.newText(new Coordinates(100, line), "E", "5_", null, textP1);
    t6_ = lang.newText(new Coordinates(120, line), "F", "6_", null, textP1);
    t7_ = lang.newText(new Coordinates(140, line), "G", "7_", null, textP1);
    t8_ = lang.newText(new Coordinates(160, line), "H", "8_", null, textP1);
    t9_ = lang.newText(new Coordinates(180, line), "I", "9_", null, textP1);
    t10_ = lang.newText(new Coordinates(200, line), "J", "10_", null, textP1);
    t11_ = lang.newText(new Coordinates(220, line), "K", "11_", null, textP1);
    t12_ = lang.newText(new Coordinates(240, line), "L", "12_", null, textP1);
    t13_ = lang.newText(new Coordinates(260, line), "M", "13_", null, textP1);
    t14_ = lang.newText(new Coordinates(280, line), "N", "14_", null, textP1);
    t15_ = lang.newText(new Coordinates(300, line), "O", "15_", null, textP1);
    t16_ = lang.newText(new Coordinates(320, line), "P", "16_", null, textP1);
    t17_ = lang.newText(new Coordinates(340, line), "Q", "17_", null, textP1);
    t18_ = lang.newText(new Coordinates(360, line), "R", "18_", null, textP1);
    t19_ = lang.newText(new Coordinates(380, line), "S", "19_", null, textP1);
    t20_ = lang.newText(new Coordinates(400, line), "T", "20_", null, textP1);
    t21_ = lang.newText(new Coordinates(420, line), "U", "21_", null, textP1);
    t22_ = lang.newText(new Coordinates(440, line), "V", "22_", null, textP1);
    t23_ = lang.newText(new Coordinates(460, line), "W", "23_", null, textP1);
    t24_ = lang.newText(new Coordinates(480, line), "X", "24_", null, textP1);
    t25_ = lang.newText(new Coordinates(500, line), "Y", "25_", null, textP1);
    t26_ = lang.newText(new Coordinates(520, line), "Z", "26_", null, textP1);

    lang.newText(new Coordinates(600, line_), "Ciphertext alphabet", "text2",
        null, textP2);

    char point = 'A';
    Text T = gettext(point);
    for (int j = 0; j < key; j++) {
      T.changeColor(null, Color.LIGHT_GRAY, null, delay);
      T = gettext((char) (point + 1));
      point += 1;
    }

    lang.nextStep();
    point = (char) (setKeyToAnim(key) - 1);
    T = gettext((char) (setKeyToAnim(key) - 1));
    int k = 20;
    int Ticks = 0;
    for (int j = key; j < 26; j++) {

      T.moveTo(null, null, new Coordinates(k, line_), delay, t);
      T = gettext((char) (point + 1));
      k += 20;
      point += 1;
      Ticks += 5;
      delay = new TicksTiming(Ticks);

    }
    lang.nextStep();

    point = 'A';
    T = gettext(point);
    Ticks = 0;
    delay = new TicksTiming(0);
    for (int j = 0; j < key; j++) {

      T.moveTo(null, null, new Coordinates(k, line_), delay, t);
      T = gettext((char) (point + 1));
      k += 20;
      point += 1;
      Ticks += 5;
      delay = new TicksTiming(Ticks);
    }
    lang.nextStep();

    String mytext = "";
    int i = 0;
    k = 20;

    lang.newText(new Coordinates(600, 90), "Plaintext", "text3", null,
        plainTextProps);

    while (i < s.length()) {
      mytext += s.charAt(i);
      lang.newText(new Coordinates(k, 90), mytext, "100" + i, null, textP1)
          .changeColor(null, Color.BLACK, null, null);
      k += 20;
      i++;
      mytext = "";
    }
    lang.nextStep();
    k = 20;
    StringBuffer ergebnis = new StringBuffer();
    char aa = ' ';
    Text textresult = null;
    String resText = "";

    textP2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
    lang.newText(new Coordinates(600, 300), "Ciphertext", "text4", null,
        cipherTextProps);

    for (i = 0; i < s.length(); i++) {
      aa = s.charAt(i);
      resText += aa;
      if (aa >= 'A' && aa <= 'Z') {
        aa += key;
        resText = "";
        resText += aa;
        if (aa > 'Z') {
          aa -= 26;
          resText = "";
          resText += aa;
        }
        mytext += s.charAt(i);
        Text textmove = lang.newText(new Coordinates(k, 90), mytext, "100" + i,
            null, textP1);
        textmove.changeColor(null, Color.BLUE, null, null);
        lang.nextStep();

        textmove.moveTo(null, null, new Coordinates(
            (s.charAt(i) - 65 + 1) * 20, line), null, t);
        lang.nextStep();

        Node x = new Coordinates((s.charAt(i) - 65 + 1) * 20 + 5, line + 20);
        Node y = new Coordinates((s.charAt(i) - 65 + 1) * 20 + 5, line_ - 3);
        Node[] n = { x, y };
        Polyline P = lang.newPolyline(n, "n" + i, null, p);
        mytext = "";
        lang.nextStep();
        textmove.hide();
        P.hide();
      }
      textresult = lang.newText(new Coordinates((s.charAt(i) - 65 + 1) * 20,
          250), resText, "res" + i, null, textP1);
      textresult.changeColor(null, Color.RED, null, t);

      if (aa >= 'A' && aa <= 'Z')
        textresult.moveTo(null, null, new Coordinates(k, 300), null, t);
      else
        textresult.moveTo(null, null, new Coordinates(k, 300), null, null);

      lang.nextStep();
      k += 20;
      ergebnis.append(aa);
      resText = "";
    }
  }

  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {

    init();
    cipherTextProps.set("color", arg0.get("Cipher", "color"));
    plainTextProps.set("color", arg0.get("Plain", "color"));
    int Value = Integer.parseInt(arg1.get("Key").toString());
    try {
      CaesarChiffre(Value);
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }
    lang.finalizeGeneration();
    return lang.getAnimationCode();
  }

  public String getAlgorithmName() {
    return "Caesar Cipher";
  }

  public String getAnimationAuthor() {
    return "Malek Boulakbech";
  }

  public String getCodeExample() {
    return SOURCE_CODE;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getDescription() {
    return DESCRIPTION;
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  public String getName() {
    return "CaesarImp";
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  private Text t1_, t2_, t3_, t4_, t5_, t6_, t7_, t8_, t9_, t10_, t11_, t12_,
      t13_, t14_, t15_, t16_, t17_, t18_, t19_, t20_, t21_, t22_, t23_, t24_,
      t25_, t26_;

  public Text gettext(char s) {

    switch (s) {
      case 'A':
        return t1_;
      case 'B':
        return t2_;
      case 'C':
        return t3_;
      case 'D':
        return t4_;
      case 'E':
        return t5_;
      case 'F':
        return t6_;
      case 'G':
        return t7_;
      case 'H':
        return t8_;
      case 'I':
        return t9_;
      case 'J':
        return t10_;
      case 'K':
        return t11_;
      case 'L':
        return t12_;
      case 'M':
        return t13_;
      case 'N':
        return t14_;
      case 'O':
        return t15_;
      case 'P':
        return t16_;
      case 'Q':
        return t17_;
      case 'R':
        return t18_;
      case 'S':
        return t19_;
      case 'T':
        return t20_;
      case 'U':
        return t21_;
      case 'V':
        return t22_;
      case 'W':
        return t23_;
      case 'X':
        return t24_;
      case 'Y':
        return t25_;
      case 'Z':
        return t26_;

    }
    return null;
  }

  public char setKeyToAnim(int k) {
    return (char) (k + 65 + 1);
  }
}
