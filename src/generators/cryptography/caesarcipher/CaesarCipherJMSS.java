package generators.cryptography.caesarcipher;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class CaesarCipherJMSS implements Generator {

  /**
   * das konkrete Language Objekt zur Erzeugung der Ausgabe
   */
  private Language              lang;
  private String[]              alphabet = new String[] { "A", "B", "C", "D",
      "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
      "S", "T", "U", "V", "W", "X", "Y", "Z" };
  private Timing                tickTime = new TicksTiming(10);

  private TextProperties        hProps;
  private RectProperties        rhProps;
  private RectProperties        rsProps;
  private SourceCodeProperties  scProps;
  private TextProperties        t1Props;
  private ArrayProperties       aProps;
  private ArrayMarkerProperties amProps1;
  private ArrayMarkerProperties amProps2;

  /**
   * konstruktor
   */
  public CaesarCipherJMSS() {
  }

  /**
   * konstruktor
   * 
   * @param l
   *          das konkrete Language Objekt zur Erzeugung der Ausgabe
   */
  public CaesarCipherJMSS(Language l) {
    lang = l;
    lang.setStepMode(true);
  }

  public void caesar(String klartext, int verschiebewert) {
    // //////////////////////////////////////////////////////////////////////////////////////////
    // SourceCode block
    SourceCode sc = lang.newSourceCode(new Coordinates(250, 300), "sourceCode",
        null, scProps);
    sc.addCodeLine(
        "public void caesarChiffre(String klartext, int verschiebewert){",
        null, 0, null);
    sc.addCodeLine("init();", null, 1, null);
    sc.addCodeLine(
        "StringBuilder chiffrat = new StringBuilder(klartext.length());", null,
        1, null);
    sc.addCodeLine("", null, 1, null);
    sc.addCodeLine("for (int i = 0; i < klartext.length(); i++){", null, 1,
        null);
    sc.addCodeLine("for (int j = 0; j < schluesselRaum.length(); j++){", null,
        2, null);
    sc.addCodeLine(
        "if (schluessRaum.charAt(j).equalsIgnoreCase(klartext.charAt(i)){",
        null, 3, null);
    sc.addCodeLine(
        "chiffrat.append(schluesselRaum.charAt(i + "
            + String.valueOf(verschiebewert) + ");", null, 4, null);
    sc.addCodeLine("} else if (j == schluesselRaum.length() - 1){", null, 3,
        null);
    sc.addCodeLine("chiffrat.append(schluesselRaum.charAt(i);", null, 4, null);
    sc.addCodeLine("}", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);
    sc.highlight(0);
    // //////////////////////////////////////////////////////////////////////////////////////////
    // //////////////////////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////////////////////
    // header
    lang.newRect(new Offset(-10, -10, "sourceCode", AnimalScript.DIRECTION_NW),
        new Offset(10, 10, "sourceCode", AnimalScript.DIRECTION_SE), "scRect",
        null, rsProps);
    lang.newText(new Coordinates(250, 25), "Caesar-Chiffre", "header", null,
        hProps);
    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", AnimalScript.DIRECTION_SE), "hRect", null,
        rhProps);
    lang.nextStep();
    // //////////////////////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////////////////////
    // messages
    sc.toggleHighlight(0, 0, false, 1, 0, tickTime, tickTime);
    sc.highlight(2);
    lang.newText(new Coordinates(140, 120), "Schluesselraum: ", "PText", null,
        t1Props);
    StringArray schluesselraum = lang.newStringArray(new Offset(5, 0, "PText",
        AnimalScript.DIRECTION_NE), alphabet, "stringArray", null, aProps);
    lang.newText(new Offset(0, 30, "PText", AnimalScript.DIRECTION_SW),
        "Klartext:", "message1", null, t1Props);
    Text m2Text = lang.newText(new Offset(55, 0, "message1",
        AnimalScript.DIRECTION_NE), klartext, "message2", null, t1Props);
    lang.newText(new Offset(0, 30, "message1", AnimalScript.DIRECTION_SW),
        "Chiffrat:", "chiffrat1", null, t1Props);
    Text c2Text = lang.newText(new Offset(55, 0, "chiffrat1",
        AnimalScript.DIRECTION_NE), "", "chiffrat2", null, t1Props);
    lang.newText(new Offset(0, 30, "chiffrat1", AnimalScript.DIRECTION_SW),
        "Verschiebewert:", "verschiebung1", null, t1Props);
    lang.newText(new Offset(10, 0, "verschiebung1", AnimalScript.DIRECTION_NE),
        String.valueOf(verschiebewert), "verschiebung2", null, t1Props);
    lang.nextStep();
    sc.toggleHighlight(1, 0, false, 4, 0, tickTime, tickTime);
    sc.unhighlight(2);
    // //////////////////////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////////////////////
    // akt char
    Text aktKlarChar = lang.newText(new Offset(50, 0, m2Text,
        AnimalScript.DIRECTION_NE), "betrachteter Buchstabe == ", "klarChar",
        null, t1Props);
    lang.newRect(new Offset(-5, -5, "klarChar", AnimalScript.DIRECTION_NW),
        new Offset(75, 5, "klarChar", AnimalScript.DIRECTION_SE), "klarRect",
        null, rsProps);
    aktKlarChar.setText(
        "betrachteter Buchstabe == " + String.valueOf(klartext.charAt(0)),
        null, null);
    lang.nextStep();
    // //////////////////////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////////////////////
    // arraymarker
    ArrayMarker searchMarker = lang.newArrayMarker(schluesselraum, 0,
        "aMarker", null, amProps1); // suchen
    ArrayMarker pushMarker = lang.newArrayMarker(schluesselraum, 0, "aMarker2",
        null, amProps2); // verschieben
    searchMarker.show(); // added show "suchen"
    pushMarker.hide(); // hide verschieben
    // //////////////////////////////////////////////////////////////////////////////////////////

    String chiffrat = c2Text.getText().toString();

    // ///////////////////////
    // BEGIN //
    // ///////////////////////
    for (int i = 0; i < klartext.length(); i++) { // scanne klartext
      if (String.valueOf(klartext.charAt(i)).contentEquals(" ")) {
        aktKlarChar.setText("betrachteter Buchstabe == LEERTASTE", null, null);
      } else {
        aktKlarChar.setText(
            "betrachteter Buchstabe == "
                + String.valueOf(klartext.charAt(i)).toUpperCase(), null, null);
      }
      lang.nextStep();
      sc.toggleHighlight(4, 5);
      lang.nextStep();

      for (int j = 0; j < alphabet.length; j++) { // scan alphabet

        searchMarker.move(j, null, null);
        schluesselraum.highlightCell(j, null, null);
        sc.toggleHighlight(5, 6);
        String klartextString = String.valueOf(klartext.charAt(i));
        lang.nextStep();

        if (alphabet[j].equals(klartextString.toUpperCase())) { // alphabet[j]
                                                                // == klartext
                                                                // -> beginne
                                                                // verschiebung
          searchMarker.hide();
          sc.toggleHighlight(6, 7);
          lang.nextStep();
          schluesselraum.unhighlightCell(j, null, null);

          int newCharDest = verschiebewert + j;

          if (newCharDest >= alphabet.length) { // overflow alphabet
            j = newCharDest - alphabet.length;
            searchMarker.hide();
            pushMarker.move(j, null, null);
            pushMarker.show();
            schluesselraum.highlightCell(j, null, null);
            chiffrat += alphabet[j];
            lang.nextStep();
            schluesselraum.unhighlightCell(j, null, null);

          } else { // without overflow in alphabet
            searchMarker.hide();
            pushMarker.move(j + verschiebewert, null, null);
            pushMarker.show();
            schluesselraum.highlightCell(j + verschiebewert, null, null);
            chiffrat += alphabet[j + verschiebewert];
            lang.nextStep();
            schluesselraum.unhighlightCell(j + verschiebewert, null, null);
          }
          c2Text.setText(chiffrat, null, null);

          j = schluesselraum.getLength(); // nach verschiebung

          pushMarker.hide();
          searchMarker.show();
          searchMarker.move(0, null, null);
          searchMarker.show();
          sc.toggleHighlight(7, 4);
          lang.nextStep();
        } else if (j != alphabet.length - 1) {
          // wenn array eintrag ungleich klartextbuchstabe
          sc.toggleHighlight(6, 8);
          lang.nextStep();
          schluesselraum.unhighlightCell(j, null, null);
          sc.toggleHighlight(8, 5);
          lang.nextStep();
        } else if (j == alphabet.length - 1) {
          // wenn der Buchstabe nicht im Schluesselraum vorhanden ist
          sc.toggleHighlight(6, 8);
          lang.nextStep();
          sc.toggleHighlight(8, 9);
          chiffrat += String.valueOf(klartext.charAt(i));
          c2Text.setText(chiffrat, null, null);
          lang.nextStep();
          schluesselraum.unhighlightCell(j, null, null);
          sc.toggleHighlight(9, 4);
        }
      }
    }
    aktKlarChar.setText("alle Buchstaben betrachtet", null, null);
    sc.unhighlight(4);
    searchMarker.hide();
    lang.nextStep();
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    init();

    this.hProps = (TextProperties) props.get(0);
    this.rhProps = (RectProperties) props.get(1);
    this.rsProps = (RectProperties) props.get(2);
    this.scProps = (SourceCodeProperties) props.get(3);
    this.t1Props = (TextProperties) props.get(4);
    this.aProps = (ArrayProperties) props.get(5);
    this.amProps1 = (ArrayMarkerProperties) props.get(6);
    this.amProps2 = (ArrayMarkerProperties) props.get(7);

    caesar(primitives.get("klartext").toString(),
        Integer.valueOf(primitives.get("verschiebewert").toString()));
    return lang.toString();
  }

  public String getAlgorithmName() {
    return "Caesar Cipher";
  }

  public String getAnimationAuthor() {
    return "Jerome Möckel, Stefan Schmitz";
  }

  public String getCodeExample() {
    String code = "public void caesarChiffre(String klartext, int verschiebewert){\n"
        + "  init();\n"
        + "  StringBuilder chiffrat = new StringBuilder(klartext.length());\n\n"
        + "  for(int i = 0; i &lt; klartext.length(); i++){\n"
        + "    for(int j = 0; j &lt; schluesselRaum.length(); j++){\n"
        + "	   if (schluessRaum.charAt(j).equals(klartext.charAt(i)){\n"
        + "         chiffrat.append(schluesselRaum.charAt(i + verschiebewert);\n"
        + "      }else if (j == schluesselRaum.length() - 1){\n"
        + "         chiffrat.append(schluesselRaum.charAt(i);\n"
        + "      }\n"
        + "    }\n" + "  }\n" + "}";
    return code;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public String getDescription() {
    return "Chiffrierung eines Wortes mittels Caesarchiffre";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  public void init() {
    lang = new AnimalScript("Caesar", "Jerome Moeckel, Stefan Schmitz", 800,
        600);
    lang.setStepMode(true);
  }

  public String getName() {
    return "Caesar";
  }
}
