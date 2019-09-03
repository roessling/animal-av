package generators.cryptography.helpers;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Group;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class PlayfairCode {

  private TextProperties       varProps, trueProps, falseProps;
  private MatrixProperties     matrixProps;
  private SourceCodeProperties codeProps;
  private ArrayProperties      arrayProps;
  private Language             lang;
  private List<Character>      alphabet;
  private char[][]             matrix;
  private Set<Character>       usedChars;
  private SourceCode           sc, sc1, sc2, sc3, sc4;
  private StringArray          plain, key, cipher, matrix0, matrix1, matrix2,
      matrix3, matrix4;
  private Text                 plaint, keyt, ciphert, true1, true2, true3,
      false1, false2, false3;
  private Vector<Integer>      marks;
  private Text                 algo_code;

  // description text
  private static final String  desc0  = "Der Algorithmus basiert auf der Verschlüsselung von Gruppen von je";
  private static final String  desc1  = "zwei Buchstaben. Jedes Buchstabenpaar des Klartextes wird durch ein";
  private static final String  desc2  = "anderes Buchstabenpaar ersetzt. Die Playfair-Verschlüsselung ist somit";
  private static final String  desc3  = "ein bigraphisches Verfahren, womit die Verteilung der Buchstabenhäufigkeiten";
  private static final String  desc4  = "verschleiert wird. Da zur Ver- und Entschlüsselung der gleiche Schlüssel ";
  private static final String  desc5  = "genutzt wird, ist das Verfahren symmetrisch. Ausgangspunkt ist eine 5*5 Matrix,";
  private static final String  desc6  = "in die ein Schlüsselwort, ohne bereits eingetragene Zeichen zu wiederholen, ";
  private static final String  desc7  = "zeilenweise eingetragen wird. Dann werden die übrigens Buchstaben des ";
  private static final String  desc8  = "Alphabetes eingetragen. Die Buchstaben I und J belegen gemeinsam ein Element";
  private static final String  desc9  = "der Matrix. Sind im Klartext zwei gleiche Buchstaben hintereinander, wird ein";
  private static final String  desc10 = "X dazwischen eingefügt. Der Klartext wird ohne Leer- und Satzzeichnen in jeweils";
  private static final String  desc11 = "zwei Buchstaben zerlegt. Steht am Ende ein einzelner Buchstabe, ";
  private static final String  desc12 = "wird der Füllbuchstabe X hinzugefügt.";

  // end text
  private static final String  fin0   = "Der entstehende Chiffretext weist die normalen Häufigkeiten der natürlichen ";
  private static final String  fin1   = "Sprache nicht mehr auf, da die Verteilung der Buchstabenpaare gleichmäßiger ist ";
  private static final String  fin2   = "als die der Einzelbuchstaben. Dennoch kann auch dieses Verfahren gebrochen werden, ";
  private static final String  fin3   = "wenn genügend auf gleiche Weise verschlüsselter Text zur Verfügung steht oder";
  private static final String  fin4   = "Wissen über die Art und Herkunft des Textes vorliegt. Der besondere Vorteil  ";
  private static final String  fin5   = "des Verfahrens liegt aber darin, dass mit einer Entschlüssselung eines Teiles der ";
  private static final String  fin6   = "Chiffre noch nicht auf den ganzen Klartext geschlossen werden kann. Da der Algo-";
  private static final String  fin7   = "rithmus aus der Mitte des 19. Jahrhunderts stammt, wurde er für die Verwendung per ";
  private static final String  fin8   = "Hand entwickelt. Das fällt vor allem beim Entschlüssen auf, da das Aussortieren von ";
  private static final String  fin9   = "überflüssigen X's sowie die Zerlegung in die einzelne Wörter mit einem Rechner ";
  private static final String  fin10  = "einen zusätzlichen Algorithmus benötigt. Wörter mit doppeltem X können so nicht ";
  private static final String  fin11  = "verschlüsset werden, kommen jedoch auch praktisch nicht vor und werden im Notfall";
  private static final String  fin12  = "einfach durch Streichen von einem X verschlüsselt. Von der Komplexität ist der ";
  private static final String  fin13  = "Algorithmus sehr gut und effizient, da er keine großen  Rechenschritte beinhaltet,";
  private static final String  fin14  = "sondern nur über einfache Array- und Matrixzugriffe arbeitet.";

  // constructor
  public PlayfairCode(Language l, ArrayProperties arrayP,
      SourceCodeProperties sourceP) {
    alphabet = new ArrayList<Character>();

    lang = l;
    lang.setStepMode(true);

    alphabet.add('A');
    alphabet.add('B');
    alphabet.add('C');
    alphabet.add('D');
    alphabet.add('E');
    alphabet.add('F');
    alphabet.add('G');
    alphabet.add('H');
    alphabet.add('I');
    alphabet.add('K');
    alphabet.add('L');
    alphabet.add('M');
    alphabet.add('N');
    alphabet.add('O');
    alphabet.add('P');
    alphabet.add('Q');
    alphabet.add('R');
    alphabet.add('S');
    alphabet.add('T');
    alphabet.add('U');
    alphabet.add('V');
    alphabet.add('W');
    alphabet.add('X');
    alphabet.add('Y');
    alphabet.add('Z');

    // Properties
    // ARRAY
    arrayProps = arrayP;
    /*
     * arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
     * arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
     * Color.BLACK); arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
     * Color.WHITE);
     * arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
     * Color.GREEN);
     * arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
     * Color.RED);
     */
    arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 18));

    // SOURCECODE
    codeProps = sourceP;
    /*
     * codeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
     * codeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
     * Color.BLUE); codeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY,
     * Color.RED);
     */
    codeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 16));

    // MATRIX
    matrixProps = new MatrixProperties();
    matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    matrixProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    matrixProps
        .set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
    matrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.RED);
    matrixProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 18));
    matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");

    // true false texts
    trueProps = new TextProperties();
    falseProps = new TextProperties();
    trueProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
    trueProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 15));
    falseProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    falseProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 15));

    // Startpage

    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 24));
    headerProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    lang.newText(new Coordinates(20, 30), "Playfair-Chiffre", "header", null,
        headerProps);

    RectProperties boxProps = new RectProperties();
    boxProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
    boxProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    boxProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    lang.newRect(new Offset(-5, -5, "header",
        AnimalScript.DIRECTION_NW), new Offset(5, 5, "header",
        AnimalScript.DIRECTION_SE), "box", null, boxProps);

    TextProperties authorProps = new TextProperties();
    authorProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.ITALIC, 13));
    authorProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    lang.newText(new Offset(10, -10, "header", AnimalScript.DIRECTION_E),
        "by Nadine Trüschler & Daniel Tanneberg", "authors", null, authorProps);

    TextProperties descProps = new TextProperties();
    descProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 15));
    Text des0 = lang.newText(new Coordinates(20, 75), desc0, "desc0", null,
        descProps);
    Text des1 = lang.newText(new Offset(0, 16, "desc0",
        AnimalScript.DIRECTION_W), desc1, "desc1", null, descProps);
    Text des2 = lang.newText(new Offset(0, 16, "desc1",
        AnimalScript.DIRECTION_W), desc2, "desc2", null, descProps);
    Text des3 = lang.newText(new Offset(0, 16, "desc2",
        AnimalScript.DIRECTION_W), desc3, "desc3", null, descProps);
    Text des4 = lang.newText(new Offset(0, 16, "desc3",
        AnimalScript.DIRECTION_W), desc4, "desc4", null, descProps);
    Text des5 = lang.newText(new Offset(0, 16, "desc4",
        AnimalScript.DIRECTION_W), desc5, "desc5", null, descProps);
    Text des6 = lang.newText(new Offset(0, 16, "desc5",
        AnimalScript.DIRECTION_W), desc6, "desc6", null, descProps);
    Text des7 = lang.newText(new Offset(0, 16, "desc6",
        AnimalScript.DIRECTION_W), desc7, "desc7", null, descProps);
    Text des8 = lang.newText(new Offset(0, 16, "desc7",
        AnimalScript.DIRECTION_W), desc8, "desc8", null, descProps);
    Text des9 = lang.newText(new Offset(0, 16, "desc8",
        AnimalScript.DIRECTION_W), desc9, "desc9", null, descProps);
    Text des10 = lang.newText(new Offset(0, 16, "desc9",
        AnimalScript.DIRECTION_W), desc10, "desc10", null, descProps);
    Text des11 = lang.newText(new Offset(0, 16, "desc10",
        AnimalScript.DIRECTION_W), desc11, "desc11", null, descProps);
    Text des12 = lang.newText(new Offset(0, 16, "desc11",
        AnimalScript.DIRECTION_W), desc12, "desc12", null, descProps);

    LinkedList<Primitive> desc = new LinkedList<Primitive>();
    desc.add(des0);
    desc.add(des1);
    desc.add(des2);
    desc.add(des3);
    desc.add(des4);
    desc.add(des5);
    desc.add(des6);
    desc.add(des7);
    desc.add(des8);
    desc.add(des9);
    desc.add(des10);
    desc.add(des11);
    desc.add(des12);
    Group des = lang.newGroup(desc, "des");

    lang.nextStep("Start");
    des.hide();
  } // end constructor

  // HELPER-FUNCTIONS
  // *************************************************************************************
  class pair {
    int x;
    int y;

    pair(int xx, int yy) {
      x = xx;
      y = yy;
    }
  } // end pair

  private pair getPos(char c) {
    for (int i = 0; i < matrix.length; i++)
      for (int j = 0; j < matrix[i].length; j++)
        if (matrix[j][i] == c)
          return new pair(j, i);
    return null;
  } // end getPos

  private int right(int x) {
    return (x == 4) ? 0 : x + 1;
  }

  private int left(int x) {
    return (x == 0) ? 4 : x - 1;
  }

  private int below(int y) {
    return (y == 4) ? 0 : y + 1;
  }

  private int above(int y) {
    return (y == 0) ? 4 : y - 1;
  }

  private String[] makeArray(String s) {
    String[] textArray = new String[s.length()];
    for (int i = 0; i < s.length(); i++)
      textArray[i] = String.valueOf(s.charAt(i));
    return textArray;
  }

  private StringArray getMatrix(int m) {
    switch (m) {
      case 0:
        return matrix0;
      case 1:
        return matrix1;
      case 2:
        return matrix2;
      case 3:
        return matrix3;
      case 4:
        return matrix4;
      default:
        return matrix4;
    }
  }

  private String removeSpaces(String s) {
    String s2 = s;
    while (s2.indexOf(" ") != -1) {
      int index = s2.indexOf(" ");
      if (index != -1) {
        String sleft = s2.substring(0, index);
        String sright = s2.substring(index + 1, s2.length());
        s2 = sleft.concat(sright);
      }
    }
    // highlight space cells
    for (int i = 0; i < plain.getLength(); i++)
      if (plain.getData(i).equals(" "))
        plain.highlightCell(i, null, null);

    return s2;
  } // end removeSpaces

  private String doubleLetters(String s) {

    String s2 = s;
    for (int i = 0; i < s2.length() - 1; i++) {
      if (s2.charAt(i) == s2.charAt(i + 1)) {
        plain.highlightCell(i, i + 1, null, null);
        plain.highlightElem(i, i + 1, null, null);
      }
    }

    for (int i = 0; i < s2.length() - 1; i++) {
      if (s2.charAt(i) == s2.charAt(i + 1)) {
        String sleft = s2.substring(0, i + 1);
        String sright = s2.substring(i + 1, s2.length());
        s2 = sleft.concat("X").concat(sright);
        marks.add(i + 1);
      }
    }
    return s2;
  } // end doubleLetters

  private String removeWrongChars(String s) {

    String s2 = s;
    for (int i = 0; i < s2.length(); i++) {
      if ((s2.charAt(i) < 65 || s2.charAt(i) > 90) && s2.charAt(i) != 32) {
        plain.highlightCell(i, null, null);
        plain.highlightElem(i, null, null);
      }
    }

    for (int i = 0; i < s2.length(); i++) {
      if ((s2.charAt(i) < 65 || s2.charAt(i) > 90) && s2.charAt(i) != 32) {
        s2 = s2.replace(s2.charAt(i), ' ');
        plain.highlightCell(i, null, null);
        plain.highlightElem(i, null, null);
      }
    }

    return s2;
  } // end removeWrongChars

  // *************************************************************************************

  /*
   * prepare Plaintext(text) make all UpperCase remove illegal Characters remove
   * spaces set J to I set X between double-letters if text.length is odd set X
   * at the end createMatrix(key) encrypt(text,matrix) decrypt(text,matrix)
   */

  // prepare the plaintext
  private String preparePlaintext(String text) {
    String text2 = text;
    String[] textArray = makeArray(text2);

    varProps = new TextProperties();
    varProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 18));
    plaint = lang.newText(new Coordinates(20, 90), "Plaintext", "plaint", null,
        varProps);
    plain = lang.newStringArray(new Coordinates(130, 85), textArray, "plain",
        null, arrayProps);

    // show sourcecode
    lang.nextStep();

    TextProperties algoProps = new TextProperties();
    algoProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.DARK_GRAY);
    algoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.ITALIC, 16));
    algo_code = lang.newText(new Coordinates(20, 205), "Algorithmus-Code",
        "algo_code", null, algoProps);

    sc = lang.newSourceCode(new Coordinates(20, 220), "sc", null, codeProps);
    sc.addCodeLine("preparePlaintext(text)", null, 0, null);
    sc.addCodeLine("createMatrix(key)", null, 0, null);
    sc.addCodeLine("encrypt(plaintext, matrix)", null, 0, null);
    sc.addCodeLine("decrypt(cipher, matrix)", null, 0, null);

    // show expanded sc1
    lang.nextStep();
    sc.hide();

    sc1 = lang.newSourceCode(new Coordinates(20, 220), "sc1", null, codeProps);
    sc1.addCodeLine("preparePlaintext(text)", "0", 0, null);
    sc1.addCodeLine("make all UpperCase", "1", 1, null);
    sc1.addCodeLine("remove illegal Characters", "2", 1, null);
    sc1.addCodeLine("remove spaces", "3", 1, null);
    sc1.addCodeLine("set J to I", "4", 1, null);
    sc1.addCodeLine("set X between double-letters", "5", 1, null);
    sc1.addCodeLine("if text.length is odd set X at the end", "6", 1, null);
    sc1.addCodeLine("createMatrix(key)", "7", 0, null);
    sc1.addCodeLine("encrypt(plaintext, matrix)", "8", 0, null);
    sc1.addCodeLine("decrypt(cipher, matrix)", "9", 0, null);

    sc1.highlight("0");

    // toUpperCase
    lang.nextStep("preparePlaintext");
    sc1.highlight("0", true);
    sc1.highlight("1");
    text2 = text2.toUpperCase();
    textArray = makeArray(text2);
    plain.hide();
    plain = lang.newStringArray(new Coordinates(130, 85), textArray, "plain",
        null, arrayProps);

    // removeWrongChars
    lang.nextStep();
    sc1.unhighlight("1");
    sc1.highlight("2");
    text2 = text2.replaceAll("Ä", "AE");
    text2 = text2.replaceAll("Ü", "UE");
    text2 = text2.replaceAll("Ö", "OE");
    text2 = text2.replaceAll("ß", "SS");
    lang.nextStep();
    textArray = makeArray(text2);
    plain.hide();
    plain = lang.newStringArray(new Coordinates(130, 85), textArray, "plain",
        null, arrayProps);
    text2 = removeWrongChars(text2);
    lang.nextStep();
    textArray = makeArray(text2);
    plain.hide();
    plain = lang.newStringArray(new Coordinates(130, 85), textArray, "plain",
        null, arrayProps);
    plain.unhighlightCell(0, plain.getLength() - 1, null, null);
    plain.unhighlightElem(0, plain.getLength() - 1, null, null);

    // remove Spaces
    lang.nextStep();
    sc1.unhighlight("2");
    sc1.highlight("3");
    text2 = removeSpaces(text2);
    lang.nextStep();
    textArray = makeArray(text2);
    plain.hide();
    plain = lang.newStringArray(new Coordinates(130, 85), textArray, "plain",
        null, arrayProps);
    plain.unhighlightCell(0, plain.getLength() - 1, null, null);
    plain.unhighlightElem(0, plain.getLength() - 1, null, null);

    // set J to I
    lang.nextStep();
    sc1.unhighlight("3");
    sc1.highlight("4");
    text2 = text2.replace("J", "I");
    marks = new Vector<Integer>();
    for (int i = 0; i < plain.getLength() - 1; i++)
      if (plain.getData(i).equals("J")) {
        plain.highlightCell(i, null, null);
        plain.highlightElem(i, null, null);
        marks.add(i);
      }
    lang.nextStep();
    textArray = makeArray(text2);
    plain.hide();
    plain = lang.newStringArray(new Coordinates(130, 85), textArray, "plain",
        null, arrayProps);

    for (Integer i : marks) {
      plain.unhighlightCell(i, null, null);
      plain.highlightElem(i, null, null);
    }
    marks.clear();

    // doubleLetters
    lang.nextStep();
    plain.unhighlightCell(0, plain.getLength() - 1, null, null);
    plain.unhighlightElem(0, plain.getLength() - 1, null, null);
    sc1.unhighlight("4");
    sc1.highlight("5");
    text2 = doubleLetters(text2);
    lang.nextStep();
    textArray = makeArray(text2);
    plain.hide();
    plain = lang.newStringArray(new Coordinates(130, 85), textArray, "plain",
        null, arrayProps);

    for (Integer i : marks) {
      plain.unhighlightCell(i, null, null);
      plain.highlightElem(i, null, null);
    }
    marks.clear();

    // text length odd?
    lang.nextStep();
    plain.unhighlightCell(0, plain.getLength() - 1, null, null);
    plain.unhighlightElem(0, plain.getLength() - 1, null, null);
    sc1.unhighlight("5");
    sc1.highlight("6");

    TextProperties lengthProps = new TextProperties();
    if ((text2.length() % 2) != 0)
      lengthProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    else
      lengthProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
    lengthProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.BOLD, 16));

    String lengthText = "Lenght: " + String.valueOf(text2.length());
    Text length = lang.newText(new Offset(10, -9, plain,
        AnimalScript.DIRECTION_E), lengthText, "length", null, lengthProps);

    if ((text2.length() % 2) != 0) {
      lang.nextStep();
      text2 = text2.concat("X");

      lengthProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
      textArray = makeArray(text2);
      plain.hide();
      plain = lang.newStringArray(new Coordinates(130, 85), textArray, "plain",
          null, arrayProps);
      plain.highlightElem(text2.length() - 1, null, null);
      length.hide();
      lengthText = "Lenght: " + String.valueOf(text2.length());
      length = lang.newText(
          new Offset(10, -9, plain, AnimalScript.DIRECTION_E), lengthText,
          "length", null, lengthProps);
    }

    lang.nextStep();
    length.hide();
    lengthText = "OK";
    length = lang.newText(new Offset(10, -9, plain, AnimalScript.DIRECTION_E),
        lengthText, "length", null, lengthProps);
    plain.unhighlightElem(plain.getLength() - 1, null, null);

    lang.nextStep();
    length.hide();
    sc1.hide();

    return text2;
  } // end preparePlaintext

  /*
   * createMatrix(key) create 5x5 Matrix fill in Keyword fill up with remaining
   * letters
   */

  // create the matrix
  private void createMatrix(String keys) {
    usedChars = new HashSet<Character>();
    matrix = new char[5][5];
    // prepare
    String keys2 = keys;
    keys2 = keys2.toUpperCase();
    keys2 = keys2.replaceAll("Ä", "AE");
    keys2 = keys2.replaceAll("Ü", "UE");
    keys2 = keys2.replaceAll("Ö", "OE");
    keys2 = keys2.replaceAll("ß", "SS");
    keys2 = keys2.replace('J', 'I');
    keys2 = removeWrongChars(keys2);
    keys2 = removeSpaces(keys2);
    String[] keyArray = makeArray(keys2);

    keyt = lang
        .newText(new Coordinates(20, 125), "Key", "keyt", null, varProps);
    key = lang.newStringArray(new Coordinates(130, 120), keyArray, "key", null,
        arrayProps);

    sc2 = lang.newSourceCode(new Coordinates(20, 220), "sc2", null, codeProps);
    sc2.addCodeLine("preparePlaintext(text)", null, 0, null);
    sc2.addCodeLine("createMatrix(key)", "1", 0, null);
    sc2.addCodeLine("create 5x5 Matrix", "2", 1, null);
    sc2.addCodeLine("fill in Keyword", "3", 1, null);
    sc2.addCodeLine("fill up with remaining letters", "4", 1, null);
    sc2.addCodeLine("encrypt(plaintext, matrix)", "5", 0, null);
    sc2.addCodeLine("decrypt(cipher, matrix)", "6", 0, null);
    sc2.highlight(1);

    // create 5x5 matrix
    lang.nextStep("createMatrix");
    sc2.highlight("1", true);
    sc2.highlight(2);

    /*
     * da matrix table style nicht funktioniert, kein style, highlights gehn
     * nicht, wie auch schon im AS aus aufgabe2, nehmen wir wieder 5 array
     * untereinander.
     * 
     * String empty = "12345"; String[] empty1 = makeArray(empty); String[][]
     * empty2 = {empty1,empty1,empty1,empty1,empty1};
     * 
     * smatrix = lang.newStringMatrix(new
     * Offset(50,-12,plain,AnimalScript.DIRECTION_E), empty2, "matrix", null,
     * matrixProps); smatrix.highlightCell(2, 2, null, null);
     * smatrix.highlightElem(2, 2, null, null);
     */
    String empty = "     ";
    String[] empty1 = makeArray(empty);
    matrix0 = lang.newStringArray(new Offset(80, -12, plain,
        AnimalScript.DIRECTION_E), empty1, "matrix0", null, arrayProps);
    matrix1 = lang.newStringArray(new Offset(0, 12, matrix0,
        AnimalScript.DIRECTION_W), empty1, "matrix1", null, arrayProps);
    matrix2 = lang.newStringArray(new Offset(0, 12, matrix1,
        AnimalScript.DIRECTION_W), empty1, "matrix2", null, arrayProps);
    matrix3 = lang.newStringArray(new Offset(0, 12, matrix2,
        AnimalScript.DIRECTION_W), empty1, "matrix3", null, arrayProps);
    matrix4 = lang.newStringArray(new Offset(0, 12, matrix3,
        AnimalScript.DIRECTION_W), empty1, "matrix4", null, arrayProps);

    // insert key
    lang.nextStep();
    sc2.unhighlight(2);
    sc2.highlight(3);

    Node[] points1 = { new Offset(0, 0, key, AnimalScript.DIRECTION_E),
        new Offset(40, 35, plain, AnimalScript.DIRECTION_E),
        new Offset(40, 0, plain, AnimalScript.DIRECTION_E),
        new Offset(0, 0, matrix0, AnimalScript.DIRECTION_W) };
    PolylineProperties lineProps = new PolylineProperties();
    lineProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    Polyline line1 = lang.newPolyline(points1, "line1", null, lineProps);
    if (keys2.isEmpty())
      line1.hide();

    // put key in
    lang.nextStep();
    int x = 0;
    int y = 0;
    for (int i = 0; i < keys2.length(); i++) {
      char c = keys2.charAt(i);
      if (!usedChars.contains(c)) {
        matrix[x][y] = c;
        usedChars.add(c);

        if (usedChars.size() <= 2) {
          getMatrix(y).put(x, String.valueOf(c), null, null);
          key.highlightElem(i, null, null);
          matrix0.highlightElem(i, null, null);
          if (i == 1) {
            key.unhighlightElem(i - 1, null, null);
            matrix0.unhighlightElem(i - 1, null, null);
          }
          lang.nextStep();
        } else
          getMatrix(y).put(x, String.valueOf(c), null, null);

        if (i == 2) {
          key.unhighlightElem(i - 1, null, null);
          matrix0.unhighlightElem(i - 1, null, null);
        }

        if (x == 4) {
          x = 0;
          y++;
        } else
          x++;
      }
    }

    // insert remaining chars
    lang.nextStep();
    line1.hide();
    sc2.unhighlight(3);
    sc2.highlight(4);

    Node[] points2 = { new Coordinates(350, 325),
        new Offset(40, 228, plain, AnimalScript.DIRECTION_E),
        new Offset(-40, 0, getMatrix(y), AnimalScript.DIRECTION_W),
        new Offset(0, 0, getMatrix(y), AnimalScript.DIRECTION_W) };
    Polyline line2 = lang.newPolyline(points2, "line2", null, lineProps);

    if (usedChars.size() < 25) {
      int oldsize = usedChars.size();
      lang.nextStep();
      for (int i = 0; i < alphabet.size(); i++) {
        char c = alphabet.get(i);
        if (!usedChars.contains(c)) {
          matrix[x][y] = c;
          usedChars.add(c);

          if ((usedChars.size() - oldsize) <= 2) {
            getMatrix(y).put(x, String.valueOf(c), null, null);
            if ((usedChars.size() - oldsize) == 2) {
              matrix0.unhighlightElem(0, 4, null, null);
              matrix1.unhighlightElem(0, 4, null, null);
              matrix2.unhighlightElem(0, 4, null, null);
              matrix3.unhighlightElem(0, 4, null, null);
              matrix4.unhighlightElem(0, 4, null, null);
            }
            getMatrix(y).highlightElem(x, null, null);
            lang.nextStep();
          } else
            getMatrix(y).put(x, String.valueOf(c), null, null);

          if ((usedChars.size() - oldsize) == 3) {
            matrix0.unhighlightElem(0, 4, null, null);
            matrix1.unhighlightElem(0, 4, null, null);
            matrix2.unhighlightElem(0, 4, null, null);
            matrix3.unhighlightElem(0, 4, null, null);
            matrix4.unhighlightElem(0, 4, null, null);
          }

          if (x == 4) {
            x = 0;
            y++;
          } else
            x++;
        }
      }
    }
    lang.nextStep();
    line2.hide();
    sc2.hide();
    key.hide();
    keyt.hide();

    // test output

    /*
     * for(int i = 0; i < matrix.length; i++) { for(int j = 0; j <
     * matrix[i].length; j++) System.out.print(matrix[j][i]);
     * System.out.print("\n"); }
     */

  } // end createMatrix

  /*
   * encrypt(text,matrix) for all digraphs look up position of the letters if in
   * same row encrypt with right neighbour if in same column encrypt with below
   * neighbour if in different rows and columns encrypt 1st letter with letter
   * in it's row and column of the 2nd encrypt 2nd letter with letter in it's
   * row and column of the 1st
   */

  public String encrypt(String text, String keys) {

    true1 = lang.newText(new Coordinates(7, 336), "TRUE", "true1", null,
        trueProps);
    true2 = lang.newText(new Offset(0, 40, true1, AnimalScript.DIRECTION_NW),
        "TRUE", "true2", null, trueProps);
    true3 = lang.newText(new Offset(0, 40, true2, AnimalScript.DIRECTION_NW),
        "TRUE", "true3", null, trueProps);
    false1 = lang.newText(new Coordinates(5, 336), "FALSE", "false1", null,
        falseProps);
    false2 = lang.newText(new Offset(0, 40, false1, AnimalScript.DIRECTION_NW),
        "FALSE", "false2", null, falseProps);
    false3 = lang.newText(new Offset(0, 40, false2, AnimalScript.DIRECTION_NW),
        "FALSE", "false3", null, falseProps);
    true1.hide();
    true2.hide();
    true3.hide();
    false1.hide();
    false2.hide();
    false3.hide();

    String plains = preparePlaintext(text);
    createMatrix(keys);
    String ciphers = "";

    String empty = "";
    for (int i = 0; i < plains.length(); i++)
      empty += " ";
    String[] cipherArray = makeArray(empty);

    ciphert = lang.newText(new Coordinates(20, 160), "Cipher", "ciphert", null,
        varProps);
    cipher = lang.newStringArray(new Coordinates(130, 155), cipherArray,
        "cipher", null, arrayProps);

    sc3 = lang.newSourceCode(new Coordinates(20, 220), "sc3", null, codeProps);
    sc3.addCodeLine("preparePlaintext(text)", null, 0, null);
    sc3.addCodeLine("createMatrix(key)", null, 0, null);
    sc3.addCodeLine("encrypt(plaintext, matrix)", "0", 0, null);
    sc3.addCodeLine("for all digraphs", "1", 1, null);
    sc3.addCodeLine("look up position of the letters", null, 2, null);
    sc3.addCodeLine("if in same row", "2", 2, null);
    sc3.addCodeLine("encrypt with right neighbour", null, 3, null);
    sc3.addCodeLine("if in same column", "3", 2, null);
    sc3.addCodeLine("encrypt with below neighbour", null, 3, null);
    sc3.addCodeLine("if in different rows and columns", "4", 2, null);
    sc3.addCodeLine(
        "encrypt 1st letter with letter in it's row and column of the 2nd",
        null, 3, null);
    sc3.addCodeLine(
        "encrypt 2nd letter with letter in it's row and column of the 1st",
        null, 3, null);
    sc3.addCodeLine("decrypt(cipher, matrix)", null, 0, null);
    sc3.highlight(2);

    // all bigraphs
    lang.nextStep("encrypt");
    sc3.highlight(3);
    sc3.highlight("0", true);

    for (int i = 0; i < plains.length(); i += 2) {
      // all bigraphs
      plain.highlightCell(i, i + 1, null, null);

      // look position
      lang.nextStep();
      pair first = getPos(plains.charAt(i));
      pair second = getPos(plains.charAt(i + 1));
      sc3.highlight("1", true);
      sc3.highlight(4);

      getMatrix(first.y).highlightCell(first.x, null, null);
      getMatrix(second.y).highlightCell(second.x, null, null);

      // same row
      if (first.y == second.y) {
        lang.nextStep();
        sc3.highlight(5);
        sc3.unhighlight(4);
        true1.show();
        false2.show();
        false3.show();

        lang.nextStep();
        sc3.highlight("2", true);
        sc3.highlight(6);
        ciphers += matrix[right(first.x)][first.y];
        ciphers += matrix[right(second.x)][first.y];

        cipher.put(i, String.valueOf(ciphers.charAt(i)), null, null);
        cipher.put(i + 1, String.valueOf(ciphers.charAt(i + 1)), null, null);
        cipher.highlightCell(i, null, null);
        cipher.highlightCell(i + 1, null, null);
        cipher.highlightElem(i, null, null);
        cipher.highlightElem(i + 1, null, null);
        getMatrix(first.y).highlightCell(right(first.x), null, null);
        getMatrix(second.y).highlightCell(right(second.x), null, null);
        getMatrix(first.y).highlightElem(right(first.x), null, null);
        getMatrix(second.y).highlightElem(right(second.x), null, null);

        lang.nextStep();
        sc3.unhighlight("2");
        sc3.unhighlight(6);
        cipher.unhighlightCell(i, null, null);
        cipher.unhighlightCell(i + 1, null, null);
        cipher.unhighlightElem(i, null, null);
        cipher.unhighlightElem(i + 1, null, null);
        getMatrix(first.y).unhighlightCell(right(first.x), null, null);
        getMatrix(second.y).unhighlightCell(right(second.x), null, null);
        getMatrix(first.y).unhighlightElem(right(first.x), null, null);
        getMatrix(second.y).unhighlightElem(right(second.x), null, null);
        true1.hide();
        false2.hide();
        false3.hide();
      }
      // same column
      else if (first.x == second.x) {
        lang.nextStep();
        sc3.highlight(7);
        sc3.unhighlight(4);
        true2.show();
        false1.show();
        false3.show();

        lang.nextStep();
        sc3.highlight("3", true);
        sc3.highlight(8);
        ciphers += matrix[first.x][below(first.y)];
        ciphers += matrix[first.x][below(second.y)];

        cipher.put(i, String.valueOf(ciphers.charAt(i)), null, null);
        cipher.put(i + 1, String.valueOf(ciphers.charAt(i + 1)), null, null);
        cipher.highlightCell(i, null, null);
        cipher.highlightCell(i + 1, null, null);
        cipher.highlightElem(i, null, null);
        cipher.highlightElem(i + 1, null, null);
        getMatrix(below(first.y)).highlightCell(first.x, null, null);
        getMatrix(below(second.y)).highlightCell(second.x, null, null);
        getMatrix(below(first.y)).highlightElem(first.x, null, null);
        getMatrix(below(second.y)).highlightElem(second.x, null, null);

        lang.nextStep();
        sc3.unhighlight("3");
        sc3.unhighlight(8);
        cipher.unhighlightCell(i, null, null);
        cipher.unhighlightCell(i + 1, null, null);
        cipher.unhighlightElem(i, null, null);
        cipher.unhighlightElem(i + 1, null, null);
        getMatrix(below(first.y)).unhighlightCell(first.x, null, null);
        getMatrix(below(second.y)).unhighlightCell(second.x, null, null);
        getMatrix(below(first.y)).unhighlightElem(first.x, null, null);
        getMatrix(below(second.y)).unhighlightElem(second.x, null, null);
        true2.hide();
        false1.hide();
        false3.hide();
      }
      // both different
      else {
        lang.nextStep();
        sc3.highlight(9);
        sc3.unhighlight(4);
        true3.show();
        false2.show();
        false1.show();

        lang.nextStep();
        sc3.highlight("4", true);
        sc3.highlight(10);
        ciphers += matrix[second.x][first.y];
        cipher.put(i, String.valueOf(ciphers.charAt(i)), null, null);
        cipher.highlightCell(i, null, null);
        cipher.highlightElem(i, null, null);
        getMatrix(first.y).highlightCell(second.x, null, null);
        getMatrix(first.y).highlightElem(second.x, null, null);

        lang.nextStep();
        sc3.highlight(11);
        sc3.unhighlight(10);
        ciphers += matrix[first.x][second.y];
        cipher.put(i + 1, String.valueOf(ciphers.charAt(i + 1)), null, null);
        cipher.highlightCell(i + 1, null, null);
        cipher.highlightElem(i + 1, null, null);
        getMatrix(second.y).highlightElem(first.x, null, null);
        getMatrix(second.y).highlightCell(first.x, null, null);

        lang.nextStep();
        sc3.unhighlight("4");
        sc3.unhighlight(11);
        cipher.unhighlightCell(i + 1, null, null);
        cipher.unhighlightElem(i + 1, null, null);
        cipher.unhighlightCell(i, null, null);
        cipher.unhighlightElem(i, null, null);
        getMatrix(second.y).unhighlightElem(first.x, null, null);
        getMatrix(second.y).unhighlightCell(first.x, null, null);
        getMatrix(first.y).unhighlightCell(second.x, null, null);
        getMatrix(first.y).unhighlightElem(second.x, null, null);
        true3.hide();
        false2.hide();
        false1.hide();

      }

      plain.unhighlightCell(i, null, null);
      plain.unhighlightCell(i + 1, null, null);
      getMatrix(first.y).unhighlightCell(first.x, null, null);
      getMatrix(second.y).unhighlightCell(second.x, null, null);

    }

    sc3.hide();
    sc.show();
    key.show();
    keyt.show();
    cipher.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW,
        null, null);

    // matrix = null;
    // usedChars.clear();
    return ciphers;

  } // end encrypt

  /*
   * decrypt(text,matrix) for all digraphs look up position of the letters if in
   * same row encrypt with left neighbour if in same column encrypt with above
   * neighbour if in different rows and columns encrypt 1st letter with letter
   * in it's row and column of the 2nd encrypt 2nd letter with letter in it's
   * row and column of the 1st
   */

  // decrypt
  public String decrypt(String ciphers) {

    true1 = lang.newText(new Coordinates(7, 356), "TRUE", "true1", null,
        trueProps);
    true2 = lang.newText(new Offset(0, 40, true1, AnimalScript.DIRECTION_NW),
        "TRUE", "true2", null, trueProps);
    true3 = lang.newText(new Offset(0, 40, true2, AnimalScript.DIRECTION_NW),
        "TRUE", "true3", null, trueProps);
    false1 = lang.newText(new Coordinates(5, 356), "FALSE", "false1", null,
        falseProps);
    false2 = lang.newText(new Offset(0, 40, false1, AnimalScript.DIRECTION_NW),
        "FALSE", "false2", null, falseProps);
    false3 = lang.newText(new Offset(0, 40, false2, AnimalScript.DIRECTION_NW),
        "FALSE", "false3", null, falseProps);
    true1.hide();
    true2.hide();
    true3.hide();
    false1.hide();
    false2.hide();
    false3.hide();

    // createMatrix(key);
    String plains = "";

    lang.nextStep();
    sc.hide();
    sc4 = lang.newSourceCode(new Coordinates(20, 220), "sc4", null, codeProps);
    sc4.addCodeLine("preparePlaintext(text)", null, 0, null);
    sc4.addCodeLine("createMatrix(key)", null, 0, null);
    sc4.addCodeLine("encrypt(plaintext, matrix)", null, 0, null);
    sc4.addCodeLine("decrypt(cipher, matrix)", "0", 0, null);
    sc4.addCodeLine("for all digraphs", "1", 1, null);
    sc4.addCodeLine("look up position of the letters", null, 2, null);
    sc4.addCodeLine("if in same row", "2", 2, null);
    sc4.addCodeLine("encrypt with left neighbour", null, 3, null);
    sc4.addCodeLine("if in same column", "3", 2, null);
    sc4.addCodeLine("encrypt with above neighbour", null, 3, null);
    sc4.addCodeLine("if in different rows and columns", "4", 2, null);
    sc4.addCodeLine(
        "encrypt 1st letter with letter in it's row and column of the 2nd",
        null, 3, null);
    sc4.addCodeLine(
        "encrypt 2nd letter with letter in it's row and column of the 1st",
        null, 3, null);
    sc4.highlight(3);
    cipher.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE,
        null, null);

    for (int i = 0; i < plain.getLength(); i++)
      plain.put(i, " ", null, null);
    key.hide();
    keyt.hide();

    // all bigraphs
    lang.nextStep("decrypt");
    sc4.highlight(4);
    sc4.highlight("0", true);

    for (int i = 0; i < ciphers.length(); i += 2) {
      // all bigraphs
      cipher.highlightCell(i, i + 1, null, null);

      // look position
      lang.nextStep();
      pair first = getPos(ciphers.charAt(i));
      pair second = getPos(ciphers.charAt(i + 1));
      sc4.highlight("1", true);
      sc4.highlight(5);

      getMatrix(first.y).highlightCell(first.x, null, null);
      getMatrix(second.y).highlightCell(second.x, null, null);

      // same row
      if (first.y == second.y) {
        lang.nextStep();
        sc4.highlight(6);
        sc4.unhighlight(5);
        true1.show();
        false2.show();
        false3.show();

        lang.nextStep();
        sc4.highlight("2", true);
        sc4.highlight(7);
        plains += matrix[left(first.x)][first.y];
        plains += matrix[left(second.x)][first.y];

        plain.put(i, String.valueOf(plains.charAt(i)), null, null);
        plain.put(i + 1, String.valueOf(plains.charAt(i + 1)), null, null);
        plain.highlightCell(i, null, null);
        plain.highlightCell(i + 1, null, null);
        plain.highlightElem(i, null, null);
        plain.highlightElem(i + 1, null, null);
        getMatrix(first.y).highlightCell(left(first.x), null, null);
        getMatrix(second.y).highlightCell(left(second.x), null, null);
        getMatrix(first.y).highlightElem(left(first.x), null, null);
        getMatrix(second.y).highlightElem(left(second.x), null, null);

        lang.nextStep();
        sc4.unhighlight("2");
        sc4.unhighlight(7);
        plain.unhighlightCell(i, null, null);
        plain.unhighlightCell(i + 1, null, null);
        plain.unhighlightElem(i, null, null);
        plain.unhighlightElem(i + 1, null, null);
        getMatrix(first.y).unhighlightCell(left(first.x), null, null);
        getMatrix(second.y).unhighlightCell(left(second.x), null, null);
        getMatrix(first.y).unhighlightElem(left(first.x), null, null);
        getMatrix(second.y).unhighlightElem(left(second.x), null, null);
        true1.hide();
        false2.hide();
        false3.hide();
      }
      // same column
      else if (first.x == second.x) {
        lang.nextStep();
        sc4.highlight(8);
        sc4.unhighlight(5);
        true2.show();
        false1.show();
        false3.show();

        lang.nextStep();
        sc4.highlight("3", true);
        sc4.highlight(9);
        plains += matrix[first.x][above(first.y)];
        plains += matrix[first.x][above(second.y)];

        plain.put(i, String.valueOf(plains.charAt(i)), null, null);
        plain.put(i + 1, String.valueOf(plains.charAt(i + 1)), null, null);
        plain.highlightCell(i, null, null);
        plain.highlightCell(i + 1, null, null);
        plain.highlightElem(i, null, null);
        plain.highlightElem(i + 1, null, null);
        getMatrix(above(first.y)).highlightCell(first.x, null, null);
        getMatrix(above(second.y)).highlightCell(second.x, null, null);
        getMatrix(above(first.y)).highlightElem(first.x, null, null);
        getMatrix(above(second.y)).highlightElem(second.x, null, null);

        lang.nextStep();
        sc4.unhighlight("3");
        sc4.unhighlight(9);
        plain.unhighlightCell(i, null, null);
        plain.unhighlightCell(i + 1, null, null);
        plain.unhighlightElem(i, null, null);
        plain.unhighlightElem(i + 1, null, null);
        getMatrix(above(first.y)).unhighlightCell(first.x, null, null);
        getMatrix(above(second.y)).unhighlightCell(second.x, null, null);
        getMatrix(above(first.y)).unhighlightElem(first.x, null, null);
        getMatrix(above(second.y)).unhighlightElem(second.x, null, null);
        true2.hide();
        false1.hide();
        false3.hide();
      } else {
        lang.nextStep();
        sc4.highlight(10);
        sc4.unhighlight(5);
        true3.show();
        false2.show();
        false1.show();

        lang.nextStep();
        sc4.highlight("4", true);
        sc4.highlight(11);
        plains += matrix[second.x][first.y];
        plain.put(i, String.valueOf(plains.charAt(i)), null, null);
        plain.highlightCell(i, null, null);
        plain.highlightElem(i, null, null);
        getMatrix(first.y).highlightCell(second.x, null, null);
        getMatrix(first.y).highlightElem(second.x, null, null);

        lang.nextStep();
        sc4.highlight(12);
        sc4.unhighlight(11);
        plains += matrix[first.x][second.y];
        plain.put(i + 1, String.valueOf(plains.charAt(i + 1)), null, null);
        plain.highlightCell(i + 1, null, null);
        plain.highlightElem(i + 1, null, null);
        getMatrix(second.y).highlightElem(first.x, null, null);
        getMatrix(second.y).highlightCell(first.x, null, null);

        lang.nextStep();
        sc4.unhighlight("4");
        sc4.unhighlight(12);
        plain.unhighlightCell(i + 1, null, null);
        plain.unhighlightElem(i + 1, null, null);
        plain.unhighlightCell(i, null, null);
        plain.unhighlightElem(i, null, null);
        getMatrix(second.y).unhighlightElem(first.x, null, null);
        getMatrix(second.y).unhighlightCell(first.x, null, null);
        getMatrix(first.y).unhighlightCell(second.x, null, null);
        getMatrix(first.y).unhighlightElem(second.x, null, null);
        true3.hide();
        false2.hide();
        false1.hide();
      }

      cipher.unhighlightCell(i, null, null);
      cipher.unhighlightCell(i + 1, null, null);
      getMatrix(first.y).unhighlightCell(first.x, null, null);
      getMatrix(second.y).unhighlightCell(second.x, null, null);
    }

    sc4.hide();
    sc.show();
    key.show();
    keyt.show();
    cipher.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW,
        null, null);
    plain.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW,
        null, null);

    // matrix = null;
    // usedChars.clear();
    return plains;
  } // end decrypt

  public void endPage() {
    lang.nextStep("Algorithm finished");
    key.hide();
    keyt.hide();
    plain.hide();
    plaint.hide();
    cipher.hide();
    ciphert.hide();
    matrix0.hide();
    matrix1.hide();
    matrix2.hide();
    matrix3.hide();
    matrix4.hide();
    algo_code.hide();
    sc.hide();

    TextProperties finProps = new TextProperties();
    finProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 15));
    lang.newText(new Coordinates(20, 75), fin0, "fin0", null,
        finProps);
    lang.newText(
        new Offset(0, 16, "fin0", AnimalScript.DIRECTION_W), fin1, "fin1",
        null, finProps);
    lang.newText(
        new Offset(0, 16, "fin1", AnimalScript.DIRECTION_W), fin2, "fin2",
        null, finProps);
    lang.newText(
        new Offset(0, 16, "fin2", AnimalScript.DIRECTION_W), fin3, "fin3",
        null, finProps);
    lang.newText(
        new Offset(0, 16, "fin3", AnimalScript.DIRECTION_W), fin4, "fin4",
        null, finProps);
    lang.newText(
        new Offset(0, 16, "fin4", AnimalScript.DIRECTION_W), fin5, "fin5",
        null, finProps);
    lang.newText(
        new Offset(0, 16, "fin5", AnimalScript.DIRECTION_W), fin6, "fin6",
        null, finProps);
    lang.newText(
        new Offset(0, 16, "fin6", AnimalScript.DIRECTION_W), fin7, "fin7",
        null, finProps);
    lang.newText(
        new Offset(0, 16, "fin7", AnimalScript.DIRECTION_W), fin8, "fin8",
        null, finProps);
    lang.newText(
        new Offset(0, 16, "fin8", AnimalScript.DIRECTION_W), fin9, "fin9",
        null, finProps);
    lang.newText(
        new Offset(0, 16, "fin9", AnimalScript.DIRECTION_W), fin10, "fin10",
        null, finProps);
    lang.newText(new Offset(0, 16, "fin10",
        AnimalScript.DIRECTION_W), fin11, "fin11", null, finProps);
    lang.newText(new Offset(0, 16, "fin11",
        AnimalScript.DIRECTION_W), fin12, "fin12", null, finProps);
    lang.newText(new Offset(0, 16, "fin12",
        AnimalScript.DIRECTION_W), fin13, "fin13", null, finProps);
    lang.newText(new Offset(0, 16, "fin13",
        AnimalScript.DIRECTION_W), fin14, "fin14", null, finProps);

  }
  /*
   * prepare Plaintext(text) remove spaces set J to I set X between
   * double-letters if text.length is odd set X at the end
   * 
   * createMatrix(key) create 5x5 Matrix fill in Keyword fill up with remaining
   * letters
   * 
   * encrypt(text,matrix) for all digraphs look up position of the letters if in
   * same row encrypt with right neighbour if in same column encrypt with below
   * neighbour if in different rows and columns encrypt 1st letter with letter
   * in it's row and column of the 2nd encrypt 2nd letter with letter in it's
   * row and column of the 1st
   * 
   * decrypt(text,matrix) for all digraphs look up position of the letters if in
   * same row encrypt with left neighbour if in same column encrypt with above
   * neighbour if in different rows and columns encrypt 1st letter with letter
   * in it's row and column of the 2nd encrypt 2nd letter with letter in it's
   * row and column of the 1st
   */

}
