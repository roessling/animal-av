package generators.misc.helpers;


import java.awt.Font;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class Minimierung {

  int                         blubb  = 0;
  private Language            lang;
  private int                 textX  = 270;
  private int                 textY  = 320;
  private SourceCode          code;
  private List<List<Integer>> result = new ArrayList<List<Integer>>();
  DFA                         dfa;
  SourceCodeProperties        SourceCodeProperties;

  // Text der gehidet werden soll

  /**
   * Default constructor
   * 
   * @param l
   *          the conrete language object used for creating output
   * @throws generators.misc.helpers.Minimierung.MyException
   */
  public Minimierung(Language l, int[] accepts, String[][] matrix,
      SourceCodeProperties SourceCodeProps)
      throws generators.misc.helpers.Minimierung.MyException {
    this.SourceCodeProperties = SourceCodeProps;
    lang = l;
    lang.setStepMode(true);

    HashSet<Character> alph = new HashSet<Character>();
    int states = matrix.length;

    for (int x = 0; x < matrix.length; x++) {

      for (int y = 0; y < matrix[x].length; y++) {

        if (matrix[x].length != states) {
          throw new MyException(
              "Die Matrix muss eine höhe und breite gleich der Anzahl der Zustände besitzen");
        }

        String[] values = matrix[x][y].split(",");
        for (int k = 0; k < values.length; k++) {
          if (values[k].length() > 1) {
            throw new MyException(
                "Die Übergangsrelation besitzt eine Transition die kein Buchstabe ist");
          }
          if (values[k].length() == 1 && !alph.contains(values[k])
              && !values[k].equals(" ")) {
            alph.add(values[k].charAt(0));
          }
        }

      }
    }

    char[] alphabet = new char[alph.size()];

    int i = 0;
    for (Character c : alph) {
      alphabet[i] = c;
      i++;
    }

    if (!isDeterministic(matrix, alphabet)) {
      throw new MyException(
          "Der Eingegebenen Automat ist nicht deterministisch");
    }

    // akzeptierender Zustand muss Zustand sein
    for (int j = 0; j < accepts.length; j++) {
      if (accepts[j] >= states) {
        throw new MyException(
            "Ein akzeptierender Zustand darf keinen Wert haben, der größer als der Größte Zustand ist");
      }
    }

    // input
    int start = 0;
    dfa = new DFA(states, start, alphabet, matrix, accepts);
  }

  /**
   * Die Matrix repräsentiert einen deterministischen Automaten gdw. in jeder
   * Zeile jeder Buchstabe des Alphabets genau ein mal vorkommt
   * 
   * @param matrix
   * @param alphabet
   * @return true if successful
   */
  public boolean isDeterministic(String[][] matrix, char[] alphabet) {

    HashSet<String> counter = new HashSet<String>();

    for (int x = 0; x < matrix.length; x++) {

      for (int y = 0; y < matrix[x].length; y++) {

        // für jeden Buchstaben versuchen, ob eine Transition von x nach y
        // existiert
        for (int i = 0; i < alphabet.length; i++) {
          if (matrix[x][y].contains("" + alphabet[i])) {
            if (counter.contains("" + alphabet[i])) {
              return false;
            } else {
              counter.add("" + alphabet[i]);
            }
          }
        }
      }

      // Am Ende müssen alle Buchstaben enthalten sein
      if (counter.size() != alphabet.length) {
        return false;
      }

      counter.clear();
    }

    return true;
  }

  /**
   * Das Skript wird ausgeführt
   */
  public void run() {
    // Headline
    TextProperties y = new TextProperties();
    y.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 19));
    lang.newText(new Coordinates(400, 10), "Minimierung eines DFA", "name",
        null, y);
    // Algorithmenbeschreibung anzeigen
    showDescription();
    lang.nextStep();

    // graph anzeigen
    lang.addLine(dfa.printGraph(20, 130));

    printInfos(dfa, 20, 20);
    lang.nextStep();

    minimierung(dfa);

  }

  public void showEnd() {

    TextProperties x = new TextProperties();
    x.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 15));

    lang.newText(
        new Coordinates(10, 90),
        "Da der Algorithmus terminiert, wenn in einem Durchlauf keine neuen Klassen gefunden wurden, ",
        "name", null, x);
    lang.newText(new Coordinates(10, 120),
        "ist die Laufzeit durch die Anzahl der Zuständer gebunden.", "name",
        null, x);
    lang.newText(
        new Coordinates(10, 150),
        "Im worst case existiert nach x Durchläufen für jeden Ausgangszustand genau eine Klasse, ",
        "name", null, x);
    lang.newText(new Coordinates(10, 180),
        "wobei x die Anzahl der Zustände ist.", "name", null, x);
    lang.newText(new Coordinates(10, 210),
        "In diesem Fall ist die Lösung nach " + blubb
            + " Durchläufen gefunden worden.", "name", null, x);

  }

  public void printInfos(DFA dfa, int x, int y) {
    TextProperties prop = new TextProperties();
    prop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 12));
    lang.newText(new Coordinates(x, y), "Startzustand: " + dfa.getStart(),
        "dfa1", null, prop);
    String transition = dfa.printMatrix();
    System.out.println(transition.length());
    lang.newText(new Coordinates(x, y + 20),
        "Akzeptierende ZustÃ¤nde: " + dfa.printAccepts(), "dfa2", null, prop);
    if (transition.length() < 138) {
      lang.newText(new Coordinates(x, y + 40),
          "Übergangsfunktion: " + dfa.printMatrix(), "dfa3", null, prop);
    } else {
      String transition1 = transition.substring(137, transition.length() - 1);
      String transition2 = transition.substring(0, 137);
      lang.newText(new Coordinates(x, y + 40), "Übergangsfunktion: "
          + transition2, "dfa4", null, prop);
      lang.newText(new Coordinates(x, y + 60), transition1, "dfa5", null, prop);
    }

  }

  /**
   * Der eigentliche Algorithmus
   */
  public void minimierung(DFA dfa) {
    code = lang.newSourceCode(new Coordinates(270, 120), "pseudocode", null,
        this.SourceCodeProperties);

    code.addCodeLine("PseudoCode", "code0", 0, null);
    code.addCodeLine(
        "Teile die akzeptierenden und nicht akzeptierenden jeweils einer Klasse zu",
        "code1", 0, null);
    code.addCodeLine("Solange bis keine neuen Klassen gefunden wurden",
        "code2", 0, null);
    code.addCodeLine("Für jede Klasse", "code3", 1, null);
    code.addCodeLine("Für jedes Element", "code4", 2, null);
    code.addCodeLine("Wenn das Element noch nicht zugeteilt ist", "code5", 3,
        null);
    code.addCodeLine("->Klasse mit dem Element erstellen", "code6", 4, null);
    code.addCodeLine("->Für alle anderen noch nicht zugeteilten Elemente x",
        "code7", 4, null);
    code.addCodeLine("Ist x äquivalent zum Element ", "code8", 5, null);
    code.addCodeLine("->x in die Klasse mit einfügen", "code9", 6, null);

    lang.nextStep();

    List<Integer> klasse1 = help(dfa.getAccepts());
    List<Integer> klasse2 = new ArrayList<Integer>();

    for (int i = 0; i < dfa.getStates(); i++) {
      if (!klasse1.contains(i)) {
        klasse2.add(i);
      }
    }

    List<List<Integer>> klassen = new ArrayList<List<Integer>>();
    klassen.add(klasse1);
    klassen.add(klasse2);
    List<List<Integer>> newklassen;

    int size = 2;
    int newsize = 0;
    int tablecount = 0;
    Set<Integer> added;

    lang.newText(getTextCoordinates(true), klasse1 + "" + klasse2, "table"
        + tablecount, null);
    code.highlight(1);
    lang.nextStep();
    code.unhighlight(1);
    blubb++;

    while (size != newsize) {
      blubb++;
      tablecount++;
      Text text = lang.newText(getTextCoordinates(true), "", "table"
          + tablecount, null);

      code.highlight(2);
      lang.nextStep();
      code.unhighlight(2);

      newklassen = new ArrayList<List<Integer>>();
      // Jede Klasse für sich betrachten, Zustände aus unterschiedlichen Klassen
      // sind nicht äquvivalent
      for (int k = 0; k < klassen.size(); k++) {

        List<Integer> klasse = klassen.get(k);
        added = new HashSet<Integer>();

        code.highlight(3);
        text.setText(printKlassen(newklassen) + " Klasse: " + k, null, null);
        lang.nextStep();
        code.unhighlight(3);

        // für jeden Zustand eine Klasse erstellen mit den Zuständen die zu ihm
        // äquivalent sind
        for (int i = 0; i < klasse.size(); i++) {

          int state = klasse.get(i);

          code.highlight(4);
          text.setText(printKlassen(newklassen) + " Klasse: " + k
              + " Element: " + state, null, null);
          lang.nextStep();
          code.unhighlight(4);

          code.highlight(5);
          lang.nextStep();
          code.unhighlight(5);

          if (!added.contains(state)) {

            List<Integer> newklasse = new ArrayList<Integer>();
            newklasse.add(state);
            added.add(state);
            newklassen.add(newklasse);

            text.setText(printKlassen(newklassen) + " Klasse: " + k
                + " Element: " + state, null, null);
            code.highlight(6);
            lang.nextStep();
            code.unhighlight(6);

            // alle von 0 bis i wurden schon betrachtet
            for (int j = i + 1; j < klasse.size(); j++) {

              code.highlight(7);
              lang.nextStep();
              code.unhighlight(7);

              int nextstate = klasse.get(j);

              if (!added.contains(nextstate)) {

                text.setText(printKlassen(newklassen) + " Klasse: " + k
                    + " Element: " + state + " x: " + nextstate, null, null);

                code.highlight(8);
                lang.nextStep();
                code.unhighlight(8);

                if (aequivalent(state, nextstate, klassen, dfa)) {
                  newklasse.add(nextstate);
                  added.add(nextstate);

                  text.setText(printKlassen(newklassen) + " Klasse: " + k
                      + " Element: " + state + " x: " + nextstate, null, null);

                  code.highlight(9);
                  lang.nextStep();
                  code.unhighlight(9);
                }
              }
            }
            // newklassen.add(newklasse);
          }
        }
      }
      size = klassen.size();
      newsize = newklassen.size();
      klassen = new ArrayList<List<Integer>>(newklassen);

      text.setText(printKlassen(klassen), null, null);
      result = new ArrayList<List<Integer>>(klassen);
    }

  }

  public void hideAll() {
    lang.addLine("hideAll");
  }

  public void showResult() {
    lang.newText(getTextCoordinates(true),
        "Die Zustände können wie folgt zusammengefasst werden: "
            + printKlassen(result), "result1", null);
  }

  public String printKlassen(List<List<Integer>> klassen) {
    String result = "";
    for (int i = 0; i < klassen.size(); i++) {
      result += klassen.get(i).toString();
    }
    return result;
  }

  public Coordinates getTextCoordinates(boolean newline) {
    Coordinates c = new Coordinates(textX, textY);
    if (newline) {
      textY = textY + 20;
    }
    return c;
  }

  public boolean aequivalent(int one, int two, List<List<Integer>> klassen,
      DFA dfa) {

    for (char sign : dfa.getAlphabet()) {
      int oneNext = dfa.getNext(one, sign);
      int twoNext = dfa.getNext(two, sign);
      if (!sameClasses(klassen, oneNext, twoNext)) {
        return false;
      }
    }

    return true;
  }

  /**
     * 
     */
  public boolean sameClasses(List<List<Integer>> klassen, int state,
      int otherState) {

    for (List<Integer> klasse : klassen) {
      if (klasse.contains(state)) {
        if (klasse.contains(otherState)) {
          return true;
        } else {
          return false;
        }
      }
    }
    return false;
  }

  public List<Integer> help(int[] accepts) {
    List<Integer> list = new ArrayList<Integer>();
    for (int i : accepts) {
      list.add(i);
    }
    return list;
  }

  /**
   * Initialisiert jedes Feld einer StringMatrix mit ""
   * 
   * @param matrix
   * @return matrix
   */
  public static String[][] initMatrix(String[][] matrix) {
    for (int x = 0; x < matrix.length; x++) {
      for (int y = 0; y < matrix[x].length; y++) {
        matrix[x][y] = "";
      }
    }
    return matrix;
  }

  public void showDescription() {
    TextProperties x = new TextProperties();
    x.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.BOLD, 16));
    Text beschreibung_0 = lang.newText(new Coordinates(10, 60), "Grundidee",
        "beschreibung0", null, x);
    lang.nextStep();
    Text beschreibung_1 = lang
        .newText(
            new Coordinates(10, 90),
            "1. Man startet mit zwei Klassen: Die der akzeptierenden und nicht akzeptierenden Zusände",
            "beschreibung1", null, x);
    lang.nextStep();
    Text beschreibung_2 = lang.newText(new Coordinates(10, 120),
        "2. In jedem Schritt werden die Elemente neu auf die Klassen verteilt",
        "beschreibung2", null, x);
    lang.nextStep();
    Text beschreibung_3 = lang.newText(new Coordinates(10, 150),
        "3. Solange sich in einem Schritt die Anzahl der Klassen nicht erhöht",
        "beschreibung3", null, x);
    lang.nextStep();
    Text beschreibung_4 = lang.newText(new Coordinates(10, 180),
        "4. Zwei Elemente werden in die selbe Klasse eingetragen gdw. sie ",
        "beschreibung4", null, x);
    lang.nextStep();
    Text beschreibung_5 = lang
        .newText(new Coordinates(10, 210),
            "      - schon in der selben Klasse sind und", "beschreibung5",
            null, x);
    lang.nextStep();
    Text beschreibung_6 = lang.newText(new Coordinates(10, 240),
        "      - zueinander äquivalent sind", "beschreibung6", null, x);
    lang.nextStep();
    Text beschreibung_7 = lang
        .newText(
            new Coordinates(10, 270),
            "      (Zwei Zustände sind zueinander äquivalent, wenn sie für jede Eingabe in Nachfolgezuständen landen, die in der selben Klasse liegen)",
            "beschreibung7", null, x);
    lang.nextStep();
    Text beschreibung_8 = lang
        .newText(
            new Coordinates(10, 300),
            "Nachdem die endgültigen Klassen bekannt sind, kann man die in einer Klasse enthaltenen Zustände zusammenfassen",
            "beschreibung8", null, x);
    lang.nextStep();

    beschreibung_0.hide();
    beschreibung_1.hide();
    beschreibung_2.hide();
    beschreibung_3.hide();
    beschreibung_4.hide();
    beschreibung_5.hide();
    beschreibung_6.hide();
    beschreibung_7.hide();
    beschreibung_8.hide();
  }

  public class MyException extends Exception {

    private static final long serialVersionUID = 1L;
    private String            message;

    public MyException(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }
  }

}
