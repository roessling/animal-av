package generators.cryptography.caesarcipher;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import interactionsupport.models.HtmlDocumentationModel;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.parser.InteractionFactory;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class CaesarChiffre2 implements Generator {

  // private static final String AUTHOR = "Alper,Bilal,Gökhan";
  private final Coordinates         COORDINATES_HEADER          = new Coordinates(
                                                                    100, 20);
  private final Coordinates         COORDINATES_ALGODESCRIPTION = new Coordinates(
                                                                    10, 50);
  private final Coordinates         COORDINATES_AR_PLAIN        = new Coordinates(
                                                                    110, 100);
  private final Coordinates         COORDINATES_AR_ALPHABET     = new Coordinates(
                                                                    10, 200);
  private final Coordinates         COORDINATES_AR_CHIFFRE      = new Coordinates(
                                                                    110, 290);

  private final Coordinates         COORDINATES_DESC_SHIFT      = new Coordinates(
                                                                    100, 170);
  private final Coordinates         COORDINATES_DESC_ALPHABET   = new Coordinates(
                                                                    100, 220);
  private final Coordinates         COORDINATES_DESC_PLAIN      = new Coordinates(
                                                                    100, 120);

  private final Coordinates         COORDINATES_SHIFTTEXT       = new Coordinates(
                                                                    10, 150);
  private final Coordinates         COORDINATES_SHIFTTEXT_MOVE  = new Coordinates(
                                                                    10, 250);

  private final Coordinates         COORDINATES_SOURCECODE      = new Coordinates(
                                                                    40, 320);
  private final Coordinates         COORDINATES_DESCRIPTION     = new Coordinates(
                                                                    10, 600);
  private final Coordinates         COORDINATES_TB_PLAIN        = new Coordinates(
                                                                    10,
                                                                    COORDINATES_AR_PLAIN
                                                                        .getY());
  private final Coordinates         COORDINATES_TB_CHIFFRE      = new Coordinates(
                                                                    10,
                                                                    COORDINATES_AR_CHIFFRE
                                                                        .getY());

  AnimalScript                      lang;
  SourceCodeProperties              scProps;
  SourceCode                        sc;
  SourceCode                        desc_algorithm;
  ArrayProperties                   ar_plain_p;
  StringArray                       ar_plain;
  ArrayProperties                   ar_chiffre_p;
  StringArray                       ar_chiffre;
  ArrayProperties                   ar_alphabet_p;
  StringArray                       ar_alphabet;
  ArrayMarker                       i;
  ArrayMarker                       am_alphabet;
  StringArray                       ar_alphabet_zahl;

  boolean                           mod26                       = false;
  int                               countCodeLine;
  Timing                            defaultTiming;
  /* Beschreibungen */
  Text                              description;
  private String[]                  descriptionText;
  private Hashtable<String, String> descriptionVars;

  private ArrayMarkerProperties     am_i_p;
  private ArrayMarkerProperties     am_alphabet_p;
  private Text                      tb_plain;
  private Text                      tb_chiffre;
  private SourceCodeProperties      descriptionProps;
  private int                       shift;

  public CaesarChiffre2() {
    countCodeLine = 0;
    this.lang = new AnimalScript("CaesarChiffre Animation", "Us", 640, 480);
    lang.setStepMode(true);
    init_CaesarChiffre();
  }

  public AnimalScript getLang() {
    return lang;
  }

  public void init_CaesarChiffre() {
    defaultTiming = new TicksTiming(15);
    descriptionText = new String[13];
    descriptionText[0] = "Die Methode bekommt als Parameter den Klartext und Verschiebewert.";
    descriptionText[1] = "Das Klartextalphabet wird initialisiert.";
    descriptionText[2] = "Der Klartext wird in Großbuchstaben umgewandelt.";
    descriptionText[3] = "Als Ausgabe erhalten wir ein Chiffretext. ";
    descriptionText[4] = "Der Zeiger i wird um eine Stelle nach rechts verschoben. ";
    descriptionText[5] = "Das %i'te Element im Klartext wird ausgelesen.";
    descriptionText[6] = "Ist das aktuelle Element ein Buchstabe?";
    descriptionText[7] = "Berechne die Position vom Buchstaben im Alphabet.";
    descriptionText[8] = "Verschlüssele den Buchstaben %k durch Verschiebung um %s Buchstaben.";
    descriptionText[9] = " Der verschlüsselte Buchstabe %c wird in die Chiffre übertragen.";
    descriptionText[10] = "Ausgabe des Geheimtextes.";
    // Der Rest vom description
    descriptionText[11] = "%k  um %s Buchstaben veschoben ergibt %alpha";
    descriptionText[12] = "Der Zeiger i wird initialisiert.";
    descriptionVars = new Hashtable<String, String>();

    /* Propeties am_i */
    am_i_p = new ArrayMarkerProperties();
    am_i_p.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    am_i_p.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    /* Propeties am_alphabet */
    am_alphabet_p = new ArrayMarkerProperties();
    am_alphabet_p.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    /* Description */

    // description.
  }

  /*
   * private void init_properties() { / Properties source code scProps = new
   * SourceCodeProperties();
   * scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
   * scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font( "Monospaced",
   * Font.PLAIN, 12));
   * scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
   * scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK); //
   * Properties ar_plain_p ar_plain_p = new ArrayProperties();
   * ar_plain_p.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
   * ar_plain_p.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
   * ar_plain_p.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.red);
   * ar_plain_p.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
   * Color.green); // Properties ar_chiffre_p ar_chiffre_p = new
   * ArrayProperties(); ar_chiffre_p.set(AnimationPropertiesKeys.COLOR_PROPERTY,
   * Color.BLACK); ar_chiffre_p.set(AnimationPropertiesKeys.FILL_PROPERTY,
   * Color.white);
   * ar_chiffre_p.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
   * Color.red);
   * ar_chiffre_p.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
   * Color.green); // Properties ar_alphabet_p ar_alphabet_p = new
   * ArrayProperties();
   * ar_alphabet_p.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
   * ar_alphabet_p.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
   * ar_alphabet_p.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
   * Color.red);
   * ar_alphabet_p.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
   * Color.green);
   * 
   * 
   * // Properties Algorithm Description descriptionProps = new
   * SourceCodeProperties();
   * descriptionProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
   * Color.BLUE); descriptionProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
   * new Font( "Serif", Font.PLAIN, 16));
   * descriptionProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
   * Color.RED); descriptionProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
   * Color.BLACK);
   * 
   * }
   */

  public void caesarChiffre(String plaintext, int shift)
      throws IllegalDirectionException {
    /* header */
    Text header = lang.newText(COORDINATES_HEADER, "Cäsar-Chiffre", "header",
        defaultTiming);
    header.setFont(new Font(Font.SERIF, Font.BOLD, 40), null, defaultTiming);

    this.shift = shift;
    lang.addLabel("Einführung");
    desc_algorithm = createDescription(COORDINATES_ALGODESCRIPTION, "desc_algo");
    String[] str_desc = {
        "Die Cäsar-Chiffre ist eines der einfachsten Verfahren",
        "zum Verschlüsseln von Nachrichten. Der Name kommt ",
        "vom römischen Kaiser Julius Caesar (100-44 v. Chr.),",
        "der dieses Verfahren während seiner Regentschaft",
        "im Römischen Reich verwendet hat.", "",
        "Dabei wird jedem Buchstaben eines Klartextes ein",
        "anderer eindeutiger Buchstabe zugewiesen.",
        "Man spricht von monoalphabetischer Substitution.", "",
        "Die Zuordnung basiert auf der zyklischen Rotation",
        "des Alphabets um x Zeichen, dabei folgt auf",
        "'Z' wieder 'A'. Das x ist der Schlüssel, mit dem",
        "verschlüsselt oder entschlüsselt wird. ",
        "siehe: http://home.arcor.de/wemmzi/downloads/vigenere.pdf" };
    for (String tmpstr : str_desc) {
      addDescriptionLine(tmpstr, desc_algorithm);
    }
    lang.nextStep();

    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    // Frage nach dem Namen des Algorithmus
    FillInBlanksQuestionModel algoName = new FillInBlanksQuestionModel(
        "algoName");
    algoName
        .setPrompt("Auf welchen Kaiser geht der Name dieses Algorithmus zur&uuml;ck?");
    algoName.setGroupID("Einf&uuml;hrung");
    algoName.addAnswer("Julius Caesar", 1,
        "Julius Caesar (100-44 v. Chr.) verwendete diesen Algorithmus");
    lang.addFIBQuestion(algoName);
    lang.nextStep();

    desc_algorithm.hide();
    /* Begin */

    ar_plain = lang.newStringArray(COORDINATES_AR_PLAIN,
        convertToStringArray(plaintext), "ar_plain", null, ar_plain_p);
    tb_plain = lang.newText(COORDINATES_TB_PLAIN, "Klartext:", "tb_plain",
        defaultTiming);
    tb_plain.setFont(new Font(Font.SERIF, Font.PLAIN, 20), null, defaultTiming);
    /* Beschreibung Klartext */
    String plainText = plaintext.toUpperCase();
    ar_plain.hide();
    ar_plain = lang.newStringArray(COORDINATES_AR_PLAIN,
        convertToStringArray(plainText), "ar_plain", null, ar_plain_p);
    SourceCode description_Plain = createDescription(COORDINATES_DESC_PLAIN,
        "desc_plaintext");
    String str_desc_plain[] = {
        "Der Klartext wird in den folgenden Schritten verschlüsselt.",
        "In dieser Animation werden nur Grossbuchstaben verschlüsselt." };
    for (String tmpstr : str_desc_plain) {
      addDescriptionLine(tmpstr, description_Plain);
    }
    lang.nextStep();
    description_Plain.hide();
    Text shiftText = lang.newText(COORDINATES_SHIFTTEXT, "Schlüssel: " + shift,
        "shiftText", defaultTiming);
    shiftText
        .setFont(new Font(Font.SERIF, Font.PLAIN, 20), null, defaultTiming);
    /* Beschreibung Verschiebung */
    SourceCode description_shift = createDescription(COORDINATES_DESC_SHIFT,
        "desc_shift");
    String str_desc_shift[] = { "Der Schlüssel gibt den Wert an, um den",
        "die Klartextbuchstaben nach rechts verschoben werden.",
        "Der Klartext und der Schlüssel werden als Parameter übergeben." };
    for (String tmpstr : str_desc_shift) {
      addDescriptionLine(tmpstr, description_shift);
    }

    lang.nextStep();

    description_shift.hide();
    // sc.toggleHighlight(0, 1);
    String Alphabet[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
        "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
        "Y", "Z" };

    // showDescription(1);
    ar_alphabet = lang.newStringArray(COORDINATES_AR_ALPHABET, Alphabet,
        "ar_alphabet", null, ar_alphabet_p);
    SourceCode description_alphabet = createDescription(
        COORDINATES_DESC_ALPHABET, "desc_alphabet");
    String str_desc_alphabet[] = { "Die Verschlüsselung wird anhand des Alphabets (oben) erklärt.", };
    for (String tmpstr : str_desc_alphabet) {
      addDescriptionLine(tmpstr, description_alphabet);
    }
    lang.nextStep();
    description_alphabet.hide();
    shiftText.moveTo(null, null, COORDINATES_SHIFTTEXT_MOVE,
        new TicksTiming(10), new TicksTiming(10));
    StringBuffer chiffre = new StringBuffer();
    ar_chiffre = lang.newStringArray(COORDINATES_AR_CHIFFRE,
        convertToEmptyStringArray(plainText), "ar_chiffre", null, ar_chiffre_p);
    tb_chiffre = lang.newText(COORDINATES_TB_CHIFFRE, "Geheimtext:",
        "tb_chiffre", defaultTiming);
    tb_chiffre.setFont(new Font(Font.SERIF, Font.PLAIN, 20), null,
        defaultTiming);
    lang.addLabel("Der Algorithmus");
    showSourceCode();
    sc.highlight(0);
    sc.highlight(1);
    sc.highlight(2);
    sc.highlight(3);

    /* Beschreibung */
    description = lang.newText(COORDINATES_DESCRIPTION,
        "Die ersten drei Zeilen im Code dienen der Initzialisierung",
        "description", defaultTiming);
    description.setFont(new Font(Font.SERIF, Font.PLAIN, 20), null,
        defaultTiming);
    lang.nextStep();
    sc.unhighlight(0);
    sc.unhighlight(1);
    sc.unhighlight(2);
    sc.unhighlight(3);
    i = lang.newArrayMarker(ar_plain, 0, "i", null, am_i_p);

    InteractionFactory f = new InteractionFactory(lang,
        "InteractionPatterns.xml");
    lang.addQuestionGroup(new QuestionGroupModel("chiffre", 3));
    MultipleChoiceQuestionModel chiff;

    for (; i.getPosition() < plainText.length(); i.increment(null,
        defaultTiming)) {

      // Wie wird das Zeichen chiffriert?
      chiff = f.generateMCQuestion("caesarChiffreLoop",
          "chiff" + i.getPosition(), "" + plainText.charAt(i.getPosition()));
      sc.highlight(4);
      showDescription(4);
      lang.nextStep();

      sc.toggleHighlight(4, 5);
      showDescription(5);
      int position = ((int) plainText.charAt(i.getPosition()));

      // 65 = A
      chiff.addAnswer("" + (char) (((position + shift - 65) % 26) + 65), 2,
          "Das ist korrekt");
      chiff.addAnswer("" + (char) (((position + shift + 3 - 65) % 26) + 65),
          -1, "Nein. Die richtige Antwort lautet: "
              + (char) ((position + shift) % 26));
      chiff.addAnswer("" + (char) (((position - shift - 65) % 26) + 65), -1,
          "Nein. Die richtige Antwort lautet: "
              + (char) ((position + shift) % 26));

      ar_plain.highlightCell(i.getPosition(), null, defaultTiming);
      lang.nextStep();

      sc.toggleHighlight(5, 6);
      showDescription(6);

      if (position >= 'A' && position <= 'Z') {
        lang.nextStep();

        sc.toggleHighlight(6, 7);
        showDescription(7);
        position = position - 65;
        int oldPosition = position;
        ar_alphabet.highlightCell(oldPosition, null, defaultTiming);
        if (i.getPosition() == 0)
          lang.addLabel("Verschlüsselung des Klartextes (erster Buchstabe)");
        if (i.getPosition() == plainText.length() - 1)
          lang.addLabel("Verschlüsselung des Klartextes (letzter Buchstabe)");
        if ((position + shift) > 26 && !mod26) {
          lang.addLabel("Verschlüsselung des Klartextes (mod 26)");
          mod26 = true;
        }
        lang.nextStep();

        sc.toggleHighlight(7, 8);
        showDescription(8);
        position = (position + shift) % 26;
        am_alphabet_p.set(AnimationPropertiesKeys.LABEL_PROPERTY, "shift + "
            + String.valueOf(shift));
        am_alphabet = lang.newArrayMarker(ar_alphabet, oldPosition, "shift",
            defaultTiming, am_alphabet_p);
        lang.nextStep();

        am_alphabet.move(position, null, defaultTiming);
        showDescription(11);
        lang.nextStep();

        ar_alphabet.highlightCell(am_alphabet.getPosition(), null,
            defaultTiming);

        lang.nextStep();
        ar_alphabet.unhighlightCell(oldPosition, null, defaultTiming);
        ar_alphabet.unhighlightCell(am_alphabet.getPosition(), null,
            defaultTiming);
        am_alphabet.hide();

        sc.toggleHighlight(8, 9);
        chiffre.append(Alphabet[position]);
        ar_chiffre.put(i.getPosition(), " " + Alphabet[position] + " ", null,
            defaultTiming);
        ar_chiffre.highlightCell(i.getPosition(), null, defaultTiming);
        showDescription(9);
        lang.nextStep();
        ar_chiffre.unhighlightCell(i.getPosition(), null, defaultTiming);
        sc.unhighlight(9);
      } else {
        sc.unhighlight(6);
      }
      sc.unhighlight(3);
      ar_plain.unhighlightCell(i.getPosition(), null, defaultTiming);
      lang.nextStep();
    }
    i.hide();
    showDescription(10);

    // Verweis auf Wikipedia
    HtmlDocumentationModel link = new HtmlDocumentationModel("link");
    link.setLinkAddress("http://de.wikipedia.org/wiki/Verschiebechiffre");
    lang.addDocumentationLink(link);
    lang.nextStep();
  }

  private SourceCode createDescription(Coordinates descCoordinate, String name) {
    return lang.newSourceCode(descCoordinate, name, defaultTiming,
        descriptionProps);
  }

  /* Methoden zur Anzeige der Beschreibungen */
  private void updateDescription() {

    if (i != null && i.getPosition() >= 0 && i.getPosition() < 8) {
      System.out.println(i.getPosition());
      descriptionVars.put("%k", ar_plain.getData(i.getPosition()));
      if (am_alphabet != null)
        descriptionVars.put("%alpha",
            ar_alphabet.getData(am_alphabet.getPosition()));
      descriptionVars.put("%c", ar_chiffre.getData(i.getPosition()));
      descriptionVars.put("%s", String.valueOf(shift));
      descriptionVars.put("%i", String.valueOf(i.getPosition()));
    }
  }

  private void showDescription(int pos) {
    if (pos != 0)
      updateDescription();
    String descriptionNewText = descriptionText[pos];
    for (String key : descriptionVars.keySet()) {
      if (descriptionNewText.contains(key)) {
        descriptionNewText = descriptionNewText.replace(key,
            descriptionVars.get(key));
      }
    }
    description.setText(descriptionNewText, null, defaultTiming);
  }

  private void showSourceCode() {
    this.sc = lang.newSourceCode(COORDINATES_SOURCECODE, "sourceCode", null,
        scProps);
    /* 0 */addCodeLine(
        "public String caesarChiffre(String plain, int shift) {", 0);
    /* 1 */addCodeLine("char Alphabet[] = { 'A', 'B', 'C', ... ,'Z' };", 1);
    /* 2 */addCodeLine("plain=plain.toUpperCase();", 1);
    /* 3 */addCodeLine("StringBuffer chiffre = new StringBuffer();", 1);
    /* 4 */addCodeLine("for (int i = 0; i < plain.length(); i++) {", 1);
    /* 5 */addCodeLine("int position = ((int) plain.charAt(i));", 2);
    /* 6 */addCodeLine("if (position >= 'A' && position <= 'Z') {", 2);
    /* 7 */addCodeLine("position = position - 'A';", 3);
    /* 8 */addCodeLine("position = (position + shift) % 26;", 3);
    /* 9 */addCodeLine("chiffre.append(Alphabet[position]);", 3);
    /* 10 */addCodeLine("}", 2);
    /* 11 */addCodeLine("}", 1);
    /* 12 */addCodeLine("return chiffre.toString();", 1);
    /* 13 */addCodeLine("}", 0);
  }

  private void addCodeLine(String codeLine, int space) {
    sc.addCodeLine(codeLine, null, space, null);
  }

  private void addDescriptionLine(String codeLine, SourceCode sc_description) {
    sc_description.addCodeLine(codeLine, null, 0, null);
  }

  private String[] convertToStringArray(String text) {
    String[] result = new String[text.length()];
    for (int i = 0; i < text.length(); i++) {
      result[i] = " " + text.charAt(i) + " ";
    }
    return result;
  }

  private String[] convertToEmptyStringArray(String text) {
    String[] result = new String[text.length()];
    for (int i = 0; i < text.length(); i++) {
      result[i] = "  ";
    }
    return result;
  }

  /* Methoden für den Generator */

  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    ar_plain_p = (ArrayProperties) arg0.get(0);
    ar_alphabet_p = (ArrayProperties) arg0.get(1);
    ar_chiffre_p = (ArrayProperties) arg0.get(2);
    scProps = (SourceCodeProperties) arg0.get(3);
    descriptionProps = (SourceCodeProperties) arg0.get(4);
    try {
      caesarChiffre((String) arg1.get("plaintext"), (Integer) arg1.get("shift"));
    } catch (IllegalDirectionException e) {
      e.printStackTrace();
    }

    lang.finalizeGeneration();
    return lang.toString();
  }

  public String getAlgorithmName() {
    return "Caesar-Verschl\u00fcsselung";
  }

  public String getAnimationAuthor() {
    return "Alper Özdemir, Bilal Balci, Gökhan Simsek";
  }

  public String getCodeExample() {
    return "public String caesarChiffre(String plain, int shift) {\n"
        + "char Alphabet[] = { 'A', 'B', 'C', ... ,'Z' };\n"
        + "plain=plain.toUpperCase();\n"
        + "StringBuffer chiffre = new StringBuffer();\n"
        + "for (int i = 0; i < plain.length(); i++) {\n"
        + "int position = ((int) plain.charAt(i));\n"
        + "if (position >= 'A' && position <= 'Z') {\n"
        + "position = position - 'A';\n"
        + "position = (position + shift) % 26;\n"
        + "chiffre.append(Alphabet[position]);\n" + "}\n" + "}\n"
        + "return chiffre.toString();\n" + "}\n";
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getDescription() {
    String[] str_desc = {
        "Die Cäsar-Chiffre ist eines der einfachsten Verfahren",
        "zum Verschlüsseln von Nachrichten. Der Name kommt ",
        "vom römischen Kaiser Julius Caesar (100-44 v. Chr.),",
        "der dieses Verfahren während seiner Regentschaft",
        "im Römischen Reich verwendet hat.", "siehe www.wikipedia.de" };
    String desc_generator = "";
    for (String tmpstr : str_desc) {
      desc_generator += tmpstr + "\n";
    }
    return desc_generator;
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  public String getName() {
    return "Caesar-Chiffre";
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public void init() {
    // nothing to be done here...
  }

}
