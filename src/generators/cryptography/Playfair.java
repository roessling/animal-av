package generators.cryptography;

import generators.cryptography.helpers.PlayfairCode;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;

public class Playfair implements Generator {
  private Language             lang;
  private String               Key;
  private String               Plaintext;
  private ArrayProperties      arrayProps;
  private SourceCodeProperties sourceCodeProps;

  public void init() {
    lang = new AnimalScript("Playfair-Chiffre",
        "Daniel Tanneberg, Nadine Trüschler", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    Key = (String) primitives.get("Key");
    Plaintext = (String) primitives.get("Plaintext");
    arrayProps = (ArrayProperties) props.getPropertiesByName("arrayProps");
    sourceCodeProps = (SourceCodeProperties) props
        .getPropertiesByName("sourceCodeProps");

    if (!Plaintext.isEmpty()) {
      PlayfairCode play = new PlayfairCode(lang, arrayProps, sourceCodeProps);

      String cipher = play.encrypt(Plaintext, Key);
      play.decrypt(cipher);
      play.endPage();

      lang.nextStep("End");
    } else
      System.out.println("Plaintext was empty!");
    System.out.println(lang);

    return lang.toString();
  }

  public String getName() {
    return "Playfair-Chiffre";
  }

  public String getAlgorithmName() {
    return "Playfair";
  }

  public String getAnimationAuthor() {
    return "Daniel Tanneberg, Nadine Tr&uuml;schler";
  }

  public String getDescription() {
    return "Der Algorithmus basiert auf der Verschl&uuml;sselung von Gruppen von je<br>"
        + "\n"
        + "zwei Buchstaben. Jedes Buchstabenpaar des Klartextes wird durch ein <br>"
        + "\n"
        + "anderes Buchstabenpaar ersetzt. Die Playfair-Verschl&uuml;sselung ist somit <br>"
        + "\n"
        + "ein bigraphisches Verfahren, womit die Verteilung der Buchstabenh&auml;ufigkeiten <br>"
        + "\n"
        + "verschleiert wird. Da zur Ver- und Entschl&uuml;sselung der gleiche Schl&uuml;ssel <br>"
        + "\n"
        + "genutzt wird, ist das Verfahren symmetrisch. Ausgangspunkt ist eine 5*5 Matrix,<br>"
        + "\n"
        + "in die ein Schl&uuml;sselwort, ohne bereits eingetragene Zeichen zu wiederholen, <br>"
        + "\n"
        + "zeilenweise eingetragen wird. Dann werden die &uuml;brigens Buchstaben des <br>"
        + "\n"
        + "Alphabetes eingetragen. Die Buchstaben I und J belegen gemeinsam ein Element <br>"
        + "\n"
        + "der Matrix. Sind im Klartext zwei gleiche Buchstaben hintereinander, wird ein<br>"
        + "\n"
        + "X dazwischen eingef&uuml;gt. Der Klartext wird ohne Leer- und Satzzeichnen in jeweils<br>"
        + "\n"
        + "zwei Buchstaben zerlegt. Steht am Ende ein einzelner Buchstabe, <br>"
        + "\n"
        + "wird der F&uuml;llbuchstabe X hinzugef&uuml;gt.<br>"
        + "\n"
        + "<br><br>"
        + "\n"
        + "Der entstehende Chiffretext weist die normalen H&auml;ufigkeiten der nat&uuml;rlichen <br>"
        + "\n"
        + "Sprache nicht mehr auf, da die Verteilung der Buchstabenpaare gleichm&auml;&szlig;iger ist  <br>"
        + "\n"
        + "als die der Einzelbuchstaben. Dennoch kann auch dieses Verfahren gebrochen werden,  <br>"
        + "\n"
        + "wenn gen&uuml;gend auf gleiche Weise verschl&uuml;sselter Text zur Verf&uuml;gung steht oder<br>"
        + "\n"
        + "Wissen &uuml;ber die Art und Herkunft des Textes vorliegt. Der besondere Vorteil  <br>"
        + "\n"
        + "des Verfahrens liegt aber darin, dass mit einer Entschl&uuml;ssselung eines Teiles der <br>"
        + "\n"
        + "Chiffre noch nicht auf den ganzen Klartext geschlossen werden kann. Da der Algo-<br>"
        + "\n"
        + "rithmus aus der Mitte des 19. Jahrhunderts stammt, wurde er f&uumlr die Verwendung per <br>"
        + "\n"
        + "Hand entwickelt. Das f&auml;llt vorallem beim Entschl&uuml;ssen auf, da das Aussortieren von  <br>"
        + "\n"
        + "&uuml;berfl&uuml;ssigen X's sowie die Zerlegung in die einzelne W&ouml;rter mit einem Rechner <br>"
        + "\n"
        + "einen zus&auml;tzlichen Algorithmus ben&ouml;tigt. W&ouml;rter mit doppeltem X k&ouml;nnen so nicht <br>"
        + "\n"
        + "verschl&uuml;sset werden, kommen jedoch auch praktisch nicht vor und werden im Notfall<br>"
        + "\n"
        + "einfach durch Streichen von einem X verschl&uuml;sselt. Von der Komplexit&auml;t ist der <br>"
        + "\n"
        + "Algorithmus sehr gut und effizient, da er keine gro&szlig;en  Rechenschritte beinhaltet,<br>"
        + "\n"
        + "sondern nur &uuml;ber einfache Array- und Matrixzugriffe arbeitet.";
  }

  public String getCodeExample() {
    return "prepare Plaintext(text)"
        + "\n"
        + "    remove spaces"
        + "\n"
        + "    set J to I"
        + "\n"
        + "    set X between double-letters"
        + "\n"
        + "    if text.length is odd set X at the end"
        + "\n"
        + "		"
        + "\n"
        + "createMatrix(key)"
        + "\n"
        + "    create 5x5 Matrix"
        + "\n"
        + "    fill in Keyword"
        + "\n"
        + "    fill up with remaining letters"
        + "\n"
        + "		"
        + "\n"
        + "encrypt(text,matrix)"
        + "\n"
        + "    for all digraphs"
        + "\n"
        + "        look up position of the letters"
        + "\n"
        + "        if in same row"
        + "\n"
        + "            encrypt with right neighbour"
        + "\n"
        + "        if in same column"
        + "\n"
        + "            encrypt with below neighbour"
        + "\n"
        + "        if in different rows and columns"
        + "\n"
        + "            encrypt 1st letter with letter in it's row and column of the 2nd"
        + "\n"
        + "            encrypt 2nd letter with letter in it's row and column of the 1st"
        + "\n"
        + "				"
        + "\n"
        + "decrypt(text,matrix)"
        + "\n"
        + "    for all digraphs"
        + "\n"
        + "        look up position of the letters"
        + "\n"
        + "        if in same row"
        + "\n"
        + "            encrypt with left neighbour"
        + "\n"
        + "        if in same column"
        + "\n"
        + "            encrypt with above neighbour"
        + "\n"
        + "        if in different rows and columns "
        + "\n"
        + "            encrypt 1st letter with letter in it's row and column of the 2nd"
        + "\n"
        + "            encrypt 2nd letter with letter in it's row and column of the 1st";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}