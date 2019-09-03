package generators.helpers;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class Aufgabe_4_1_CaesarChiffre {

  public Language animalScript;

  public Aufgabe_4_1_CaesarChiffre(String title, String author, int width,
      int heigth) {
    this.animalScript = new AnimalScript(title, author, width, heigth);
    this.animalScript.setStepMode(true);
  }

  public void execute(String string, int offset) {

    int offset2 = offset;
    while (offset2 < 0) {
      offset2 += 26;
    }

    // Code-Matrix erstellen
    String[][] encode = new String[2][27];
    for (int i = 0; i < 26; i++) {
      encode[0][i] = String.valueOf((char) (65 + i));
      encode[1][i] = "";
      // encode[1][i] = String.valueOf(encode((char) (65 + i), offset));
    }

    // Sonstige Zeichen einfügen
    encode[0][26] = "~";
    encode[1][26] = "~";

    // Text-Matrix erstellen
    String[][] matrix = new String[2][string.length()];
    for (int i = 0; i < string.length(); i++) {
      matrix[0][i] = String.valueOf(string.charAt(i));
      matrix[1][i] = "";
    }

    // AnimalScript erzeugen
    createTitle("Aufgabe 4.1: Caesar-Chiffre");
    SourceCode sc = createSourceCode();
    createOffsetField(offset2);
    animalScript.nextStep();
    StringMatrix ed = createStringMatrix(sc, encode, 350, 0);
    encryptAlphabet(sc, ed, offset2);
    StringMatrix sm = createStringMatrix(sc, matrix, 450, 2);
    encryptMessage(sc, sm, ed, offset2);
    completed(sc);
  }

  private void encryptMessage(SourceCode sc, StringMatrix sm, StringMatrix ed,
      int offset) {

    for (int i = 0; hasMoreElements(sc, sm, i); i++) {
      encryptElementAt(sc, sm, ed, i, offset);
    }
  }

  private void encryptAlphabet(SourceCode sc, StringMatrix ed, int offset) {

    // Markiere Zeile
    sc.toggleHighlight(0, 1);

    // Ersten Verschiebewert schreiben
    ed.highlightCell(0, 0, null, null);
    ed.put(1, 0, new String("0"), null, null);

    for (int i = 1; i <= offset; i++) {
      animalScript.nextStep();
      // Nächsten Verschiebewert schreiben
      ed.highlightCell(0, i % 26, null, null);
      ed.put(1, i % 26, new String("" + i), null, null);
      // Vorherige Markierung entfernen
      ed.unhighlightCell(0, (i - 1) % 26, null, null);
      ed.put(1, (i - 1) % 26, "", null, null);
    }

    animalScript.nextStep();
    // Letzten Verschiebewert entfernen
    ed.put(1, offset % 26, "", null, null);

    for (int i = 0; i < 26; i++) {
      // Aktuelles Zeichen verschlüsseln
      ed.highlightCell(0, (i + offset) % 26, null, null);
      ed.highlightCell(1, i % 26, null, null);
      ed.put(1, i % 26, encrypt(ed.getElement(0, i % 26), offset), null, null);
      animalScript.nextStep();
    }

    ed.unhighlightCellColumnRange(0, 0, 25, null, null);
    ed.unhighlightCellColumnRange(1, 0, 25, null, null);
  }

  private boolean hasMoreElements(SourceCode sc, StringMatrix sm, int i) {

    // Markiere Zeile
    sc.toggleHighlight(2, 3);
    sc.toggleHighlight(4, 3);

    animalScript.nextStep();
    return i < sm.getNrCols();
  }

  private int getIndex(char c) {
    int i = c - 65;
    if (i > 25) {
      i -= 32;
    }
    if (i > 25 || i < 0) {
      i = 26; // sonstige Zeichen
    }
    return i;
  }

//  private char getChar(int i) {
//    return (char) (i + 65);
//  }

  private void encryptElementAt(SourceCode sc, StringMatrix sm,
      StringMatrix ed, int pos, int offset) {

    // Markiere Zeile
    sc.toggleHighlight(3, 4);

    // Zeichen verschlüsseln
    sm.put(1, pos, encrypt(sm.getElement(0, pos), offset), null, null);
    sm.highlightCell(0, pos, null, null);
    sm.highlightCell(1, pos, null, null);

    // Alphabet-Position markieren
    ed.highlightCellRowRange(0, 1, getIndex(sm.getElement(0, pos).charAt(0)),
        null, null);
    animalScript.nextStep();
    ed.unhighlightCellRowRange(0, 1, getIndex(sm.getElement(0, pos).charAt(0)),
        null, null);
  }

  private String encrypt(String s, int i) {
    return String.valueOf(encrypt(s.charAt(0), i));
  }

  private char encrypt(char c, int i) {
    int i2 = i;
    while (i2 < 0) {
      i2 += 26;
    }
    char[] alphabetUC = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z' };
    for (int k = 0; k < 26; k++) {
      if (alphabetUC[k] == c) {
        i2 = (i2 + k) % 26;
        return alphabetUC[i2];
      }
    }
    char[] alphabetLC = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
        'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
        'y', 'z' };
    for (int k = 0; k < 26; k++) {
      if (alphabetLC[k] == c) {
        i2 = (i2 + k) % 26;
        return alphabetLC[i2];
      }
    }
    return c;
  }

//  private char decrypt(char c, int i) {
//    return encrypt(c, -1 * i);
//  }

  private Text createOffsetField(int offset) {
    TextProperties props = new TextProperties();
    props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("sans Serif",
        Font.BOLD, 20));
    return animalScript.newText(new Coordinates(20, 300), "Verschiebewert = "
        + offset, "offsetField", null, props);
  }

  private void completed(SourceCode sc) {

    // Markiere Zeile
    sc.toggleHighlight(3, 5);
    sc.toggleHighlight(4, 5);

    sc.unhighlight(1);
    TextProperties props = new TextProperties();
    props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("sans Serif",
        Font.BOLD, 20));
    animalScript.newText(new Coordinates(20, 600),
        "Verschlüsselung mit Caesar-Chiffre erfolgreich abgeschlossen.",
        "complete", null, props);
    animalScript.nextStep();
  }

  private void createTitle(String title) {
    TextProperties tProps = new TextProperties();
    tProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    tProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("sans Serif",
        Font.BOLD, 20));
    animalScript.newText(new Coordinates(20, 30), title, "title", null, tProps);
    animalScript.newRect(new Coordinates(15, 25), new Coordinates(300, 65),
        "titlebox", null);
    animalScript.nextStep();
  }

  private SourceCode createSourceCode() {

    // Source-Code erstellen
    SourceCodeProperties scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 16));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    SourceCode sc = animalScript.newSourceCode(new Coordinates(20, 100),
        "codeSelectionSort", null, scProps);
    sc.addCodeLine("0 - Alphabet erstellen", null, 0, null);
    sc.addCodeLine("1 - Alphabet übersetzen", null, 0, null);
    sc.addCodeLine("2 - Nachricht erstellen", null, 0, null);
    sc.addCodeLine("3 - Wiederholen, solange weiterer Buchstabe vorhanden ist",
        null, 0, null);
    sc.addCodeLine("4    - Aktuellen Buchstabe verschlüsseln", null, 0, null);
    sc.addCodeLine("5 - ENDE -", null, 0, null);

    animalScript.nextStep();

    return sc;
  }

  private StringMatrix createStringMatrix(SourceCode sc, String[][] string,
      int y, int codeline) {

    // Markiere Zeile
    sc.toggleHighlight(Math.max(0, codeline - 1), codeline);

    // Array erstellen
    MatrixProperties smProps = new MatrixProperties();
    // smProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // "Monospaced", Font.PLAIN, 16));
    // smProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    smProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    // smProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    // smProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
    // Color.BLACK);
    // smProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
    // Color.RED);
    // smProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
    // Color.YELLOW);
    StringMatrix sm = animalScript.newStringMatrix(new Coordinates(20, y),
        string, "StringMatrix" + y, null, smProps);
    animalScript.nextStep();
    return sm;
  }
}