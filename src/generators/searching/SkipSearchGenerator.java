package generators.searching;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class SkipSearchGenerator implements Generator {
  // properties
  private Language                    lang;
  private String                      Text;
  private boolean                     zeige_Counter;
  private ArrayProperties             next_array;
  private String                      Suchtext;
  private TextProperties              beschreibung;
  private MatrixProperties            occ_map;
  private ArrayProperties             text_Einstellung;
  private boolean                     zeige_Fragen;
  private boolean                     zeige_lineare_Suche;
  private ArrayProperties             suchtext_Einstellung;
  private SourceCodeProperties        quelltext;

  private TextProperties              beschreibungHeading;

  // primitives
  Text                                label_Header;
  private SourceCode                  sc_skipSearch;
  private SourceCode                  sc_matchesAt;
  private SourceCode                  sc_init;
  private Text                        text_beschreibung;
  private Text                        text_var_k;
  private Text                        text_var_i;
  private StringArray                 stringArray_text;
  private StringArray                 stringArray_suchText;
  private StringArray                 stringArray_next;
  private StringArray                 stringArray_result;
  private StringMatrix                stringMatrix_occ;
  private ArrayMarker                 arrayMarker_Text_i;
  private ArrayMarker                 arrayMarker_Text_i_k;
  private Rect                        rect_Header;
  private Rect                        rect_sourceCode;
  private Rect                        rect_beschreibung;
  private Rect                        rect_Eingabe;
  private Rect                        rect_ErgebnisseVorlauf;

  // counter
  private TwoValueCounter             tvcounter_occ;                                                          // notwendig,
                                                                                                               // da
                                                                                                               // die
                                                                                                               // Matrix
                                                                                                               // zweckentfremdet
                                                                                                               // wird
  private TwoValueCounter             tvcounter_text;
  private TwoValueCounter             tvcounter_suchtext;
  private TwoValueCounter             tvcounter_next;

  // layout
  private int                         rectWidth                           = 500;

  // intern
  private HashMap<Character, Integer> algo_occ;
  private Integer[]                   algo_next;
  private HashMap<Character, Integer> algo_occ_indices                    = new HashMap<Character, Integer>();
  private Integer                     algo_k;
  private int                         unique_Question_Nr                  = 0;
  private boolean                     first_lineareSuche                  = true;
  private boolean                     first_nextZuweisung                 = true;

  // maximale abfragen, weil Gruppierung nicht zu funktionieren scheint (kann
  // entfernt werden, wenn Gruppierung laeuft)
  int                                 questionCount_Initialisierung       = 0;
  int                                 questionCount_Variablen_Deklaration = 0;
  int                                 questionCount_for_Schleife          = 0;
  int                                 questionCount_while_Schleife        = 0;
  int                                 questionCount_lineare_Suche         = 0;

  private void skip_search() {
    beschreibungHeading = new TextProperties();
    beschreibungHeading.set("font", ((Font) beschreibung.get("font"))
        .deriveFont(Font.BOLD).deriveFont(14.0f));

    lang.setStepMode(true);
    if (zeige_Fragen) {
      lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
      lang.addQuestionGroup(new QuestionGroupModel("Initialisierung", 2));
      lang.addQuestionGroup(new QuestionGroupModel("Variablen_Deklaration", 2));
      lang.addQuestionGroup(new QuestionGroupModel("for_Schleife", 2));
      lang.addQuestionGroup(new QuestionGroupModel("while_Schleife", 2));
      if (zeige_lineare_Suche)
        lang.addQuestionGroup(new QuestionGroupModel("lineare_Suche", 2));
    }

    show_Vorschautext();

    show_SourceCode();

    show_Heading_Beschreibung();

    show_skipSearch();

    show_Zusammenfassung();

    lang.finalizeGeneration();
  }

  private void show_skipSearch() {
    show_EingabeVariablen();
    show_VariablenDeklaration_occ_next();
    show_VariablenDeklaration_m_n_k_result();
    show_forSchleife();
    show_return();
  }

  private void show_Vorschautext() {

    // Überschrift und Box drumrum
    TextProperties headerprobs = new TextProperties();
    headerprobs.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 24));
    Text header = lang.newText(new Coordinates(20, 30), "Skip Search",
        "header", null, headerprobs);
    Rect rect = lang.newRect(new Offset(-5, -5, header,
        AnimalScript.DIRECTION_NW), new Offset(5, 5, header,
        AnimalScript.DIRECTION_SE), "hRect", null);
    headerprobs.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.BOLD, 20));
    Text descrHd = lang.newText(new Coordinates(20, 80),
        "Beschreibung des Algorithmus", "descrHd", null, headerprobs);

    // Beschreibung
    SourceCode scHeader = lang.newSourceCode(new Offset(0, 30, descrHd,
        AnimalScript.DIRECTION_SW), "descr", null, quelltext);
    scHeader
        .addCodeLine(
            "Skip Search ist ein Algorithmus zum Auffinden eines Suchtextes in einem Text.",
            null, 0, null);
    scHeader
        .addCodeLine(
            "Dafür wird immer an den Textstellen, die ein vielfaches der Suchtextlänge",
            null, 0, null);
    scHeader
        .addCodeLine(
            "darstellen, geprüft, ob sich der Buchstabe an dieser Stelle im Suchtext befindet.",
            null, 0, null);
    scHeader
        .addCodeLine(
            "Erst wenn das der Fall ist, wird der folgende Text mit dem Suchtext per lineare Suche verglichen.",
            null, 0, null);
    scHeader
        .addCodeLine(
            "Um die Abfrage, ob sich der jeweilige Buchstabe im Suchtext befindet, zu beschleunigen,",
            null, 0, null);
    scHeader.addCodeLine(
        "werden in der Initialisierung zwei Datenstrukturen angelegt.", null,
        0, null);
    scHeader
        .addCodeLine(
            "Diese sind der next-Array, indem zu dem jedem Buchstaben des Suchtext der ",
            null, 0, null);
    scHeader
        .addCodeLine(
            "Index des vorherigen Auftretens des Buchstabens im Suchtext hinterlegt ist, sowie die ",
            null, 0, null);
    scHeader
        .addCodeLine(
            "occ-Map in der zu jedem Buchstaben des Suchtextes der Index des jeweils letzten Auftretens hinterlegt ist.",
            null, 0, null);
    scHeader.addCodeLine("", null, 0, null);

    // nach einem Schritt wieder ausblenden
    lang.nextStep();
    descrHd.hide();
    header.hide();
    scHeader.hide();
    rect.hide();

  }

  private void show_SourceCode() {

    sc_skipSearch = lang.newSourceCode(new Coordinates(5, 0), "sourceCode",
        null, quelltext);
    /* 00 */sc_skipSearch.addCodeLine(
        "Map<Character, Integer> occ = new HashMap<Character, Integer>();",
        null, 0, null);
    /* 01 */sc_skipSearch.addCodeLine("Integer[] next;", null, 0, null);
    /* 02 */sc_skipSearch.addCodeLine("", null, 0, null);
    /* 03 */sc_skipSearch.addCodeLine(
        "Vector<Integer> skipSearch(char[] text, char[] suchtext){", null, 0,
        null);
    /* 04 */sc_skipSearch.addCodeLine("init(suchtext);", null, 1, null);
    /* 05 */sc_skipSearch.addCodeLine("", null, 1, null);
    /* 06 */sc_skipSearch
        .addCodeLine("int m = suchtext.length;", null, 1, null);
    /* 07 */sc_skipSearch.addCodeLine("int n = text.length;", null, 1, null);
    /* 08 */sc_skipSearch.addCodeLine("Integer k;", null, 1, null);
    /* 09 */sc_skipSearch.addCodeLine(
        "Vector<Integer> result = new Vector<Integer>();", null, 1, null);
    /* 10 */sc_skipSearch.addCodeLine("", null, 1, null);
    /* 11 */sc_skipSearch.addCodeLine("for (int i=m-1; i<n; i+=m){", null, 1,
        null);
    /* 12 */sc_skipSearch.addCodeLine("if((k = occ.get(text[i])) == null)",
        null, 2, null);
    /* 13 */sc_skipSearch.addCodeLine("continue;", null, 3, null);
    /* 14 */sc_skipSearch.addCodeLine("", null, 2, null);
    /* 15 */sc_skipSearch.addCodeLine("while(k != null && i-k <= n-m){", null,
        2, null);
    /* 16 */sc_skipSearch.addCodeLine("if (matchesAt(text,suchtext,i-k))",
        null, 3, null);
    /* 17 */sc_skipSearch.addCodeLine("result.add(i-k);", null, 4, null);
    /* 18 */sc_skipSearch.addCodeLine("k=next[k];", null, 3, null);
    /* 19 */sc_skipSearch.addCodeLine("}", null, 2, null);
    /* 20 */sc_skipSearch.addCodeLine("}", null, 1, null);
    /* 21 */sc_skipSearch.addCodeLine("return result;", null, 1, null);
    /* 22 */sc_skipSearch.addCodeLine("}", null, 0, null);

    sc_init = lang.newSourceCode(new Offset(0, 5, sc_skipSearch,
        AnimalScript.DIRECTION_SW), "sourceCode_init", null, quelltext);
    /* 00 */sc_init.addCodeLine("void init(char[] suchtext){", null, 0, null);
    /* 01 */sc_init.addCodeLine("next = new Integer[suchtext.length];", null,
        1, null);
    /* 02 */sc_init.addCodeLine("for (int j=0; j< suchtext.length; j++){",
        null, 1, null);
    /* 03 */sc_init.addCodeLine("char curChar = suchtext[j];", null, 2, null);
    /* 04 */sc_init.addCodeLine("next[j] = occ.get(curChar);", null, 2, null);
    /* 05 */sc_init.addCodeLine("occ.put(curChar, j);", null, 2, null);
    /* 06 */sc_init.addCodeLine("}", null, 1, null);
    /* 07 */sc_init.addCodeLine("}", null, 0, null);
    /* 08 */sc_init.addCodeLine("", null, 0, null);
    sc_init.hide();

    sc_matchesAt = lang.newSourceCode(new Offset(0, 5, sc_skipSearch,
        AnimalScript.DIRECTION_SW), "sourceCode_matchesAt", null, quelltext);
    /* 00 */sc_matchesAt.addCodeLine(
        "boolean matchesAt(char[] text, char[] suchtext, int index){", null, 0,
        null);
    /* 01 */sc_matchesAt.addCodeLine(
        "for(int j = index; index + suchtext.length > j; j++)", null, 1, null);
    /* 02 */sc_matchesAt.addCodeLine("if(text[j] != suchtext[j-index])", null,
        2, null);
    /* 03 */sc_matchesAt.addCodeLine("return false;", null, 3, null);
    /* 04 */sc_matchesAt.addCodeLine("return true;	", null, 1, null);
    /* 05 */sc_matchesAt.addCodeLine("}", null, 0, null);
    /* 06 */sc_matchesAt.addCodeLine("", null, 0, null);
    sc_matchesAt.hide();

    rect_sourceCode = lang.newRect(new Offset(-5, -5, sc_skipSearch,
        AnimalScript.DIRECTION_NW), new Offset(5, 220, sc_skipSearch,
        AnimalScript.DIRECTION_SE), "rect_sourceCode", null);

  }

  private void show_Heading_Beschreibung() {

    TextProperties tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, ((Font) beschreibungHeading
        .get(AnimationPropertiesKeys.FONT_PROPERTY)).deriveFont(20.00f));
    label_Header = lang.newText(new Offset(10, 13, rect_sourceCode,
        AnimalScript.DIRECTION_NE), "Skip Search", "label_header", null, tp);
    rect_Header = lang.newRect(new Offset(-5, -5, label_Header,
        AnimalScript.DIRECTION_NW), new Offset(5, 5, label_Header,
        AnimalScript.DIRECTION_SE), "rect_header", null);
    rect_Header.changeColor("fillcolor", Color.yellow, null, null);

    Text label_beschreibung = lang.newText(new Offset(5, 5, rect_Header,
        AnimalScript.DIRECTION_SW), "Beschreibung:", "label_Beschreibung",
        null, beschreibungHeading);
    text_beschreibung = lang.newText(new Offset(5, 0, label_beschreibung,
        AnimalScript.DIRECTION_SW),
        "In jedem Schritt ist hier eine Beschreibung zu lesen.", "Text", null,
        beschreibung);

    rect_beschreibung = lang.newRect(new Offset(-5, -5, label_beschreibung,
        AnimalScript.DIRECTION_NW), new Offset(rectWidth, 5, text_beschreibung,
        AnimalScript.DIRECTION_SW), "rect_beschreibung", null);

  }

  private void show_EingabeVariablen() {
    Text label_eingabe = lang.newText(new Offset(5, 10, rect_beschreibung,
        AnimalScript.DIRECTION_SW), "Eingaben:", "label_Eingaben", null,
        beschreibungHeading);

    Text text = lang.newText(new Offset(5, 20, label_eingabe,
        AnimalScript.DIRECTION_SW), "Text:", "label_text", null, beschreibung);
    String[] temp = new String[Text.length()];
    for (int i = 0; i < Text.length(); i++)
      temp[i] = String.valueOf(Text.charAt(i));
    stringArray_text = lang.newStringArray(new Offset(45, 0, text,
        AnimalScript.DIRECTION_NE), temp, "text_chars", null, text_Einstellung);
    arrayMarker_Text_i = lang.newArrayMarker(stringArray_text,
        Suchtext.length() - 1, "ArrayMarker_Text_i", null,
        getArrayMarkerProperties("i"));
    arrayMarker_Text_i.hide();
    arrayMarker_Text_i_k = lang.newArrayMarker(stringArray_text, 0,
        "ArrayMarker_Text_i_k", null, getArrayMarkerProperties("i-k"));
    arrayMarker_Text_i_k.hide();
    tvcounter_text = lang.newCounter(stringArray_text);
    TwoValueView counter_text = lang.newCounterView(tvcounter_text, new Offset(
        15, 10, text, AnimalScript.DIRECTION_SW), getCounterproperties(), true,
        true);
    if (!zeige_Counter)
      counter_text.hide();

    Text suchtext = lang.newText(new Offset(0, zeige_Counter ? 55 : 10, text,
        AnimalScript.DIRECTION_SW), "Suchtext:", "label_suchtext", null,
        beschreibung);
    temp = new String[Suchtext.length()];
    for (int i = 0; i < Suchtext.length(); i++)
      temp[i] = String.valueOf(Suchtext.charAt(i));
    stringArray_suchText = lang.newStringArray(new Offset(21, 0, suchtext,
        AnimalScript.DIRECTION_NE), temp, "suchtext_chars", null,
        suchtext_Einstellung);
    tvcounter_suchtext = lang.newCounter(stringArray_suchText);
    TwoValueView counter_suchtext = lang.newCounterView(tvcounter_suchtext,
        new Offset(15, 10, suchtext, AnimalScript.DIRECTION_SW),
        getCounterproperties(), true, true);
    if (!zeige_Counter)
      counter_suchtext.hide();

    rect_Eingabe = lang.newRect(new Offset(-5, -5, label_eingabe,
        AnimalScript.DIRECTION_NW), new Offset(rectWidth, zeige_Counter ? 50
        : 10, suchtext, AnimalScript.DIRECTION_SW), "rect_Eingaben", null);

    algo_occ = new HashMap<Character, Integer>();
    algo_next = new Integer[Suchtext.length()];

    lang.nextStep();

    sc_skipSearch.unhighlight(21);
  }

  private void show_VariablenDeklaration_occ_next() {
    int zeile_init = 4;

    Text label_Variablen = lang.newText(new Offset(5, 10, rect_Eingabe,
        AnimalScript.DIRECTION_SW), "Ergebnisse der Initialisierung:",
        "label_Variablen", null, beschreibungHeading);
    label_Variablen.hide();
    text_beschreibung
        .setText(
            "Die occ-Map speichert das letzte Vorkommen eines Zeichens im Suchtext.",
            null, null);
    sc_skipSearch.highlight(0);
    lang.nextStep();

    sc_skipSearch.unhighlight(0);
    sc_skipSearch.highlight(1);
    text_beschreibung
        .setText(
            "Im next-Array ist das vorherige Auftauchen des jeweiligen Zeichens im Suchtext hinterlegt.",
            null, null);

    Text label_occ = lang.newText(new Offset(5, 5, label_Variablen,
        AnimalScript.DIRECTION_SW), "ooc:", "label_ooc", null, beschreibung);
    label_occ.hide();
    Text label_next = lang.newText(new Offset(0, zeige_Counter ? 80 : 42,
        label_occ, AnimalScript.DIRECTION_SW), "next:", "label_next", null,
        beschreibung);
    label_next.hide();
    String[] next = new String[Suchtext.length()];
    stringArray_next = lang.newStringArray(new Offset(5, 0, label_next,
        AnimalScript.DIRECTION_NE), next, "next", null, next_array);
    stringArray_next.hide();

    TwoValueView counter_next = null;
    tvcounter_next = lang.newCounter(stringArray_next);
    if (zeige_Counter) {
      counter_next = lang.newCounterView(tvcounter_next, new Offset(15, 10,
          label_next, AnimalScript.DIRECTION_SW), getCounterproperties(), true,
          true);
      counter_next.hide();
    }
    lang.nextStep();
    sc_skipSearch.unhighlight(1);

    rect_ErgebnisseVorlauf = lang.newRect(new Offset(-5, -5, label_Variablen,
        AnimalScript.DIRECTION_NW), new Offset(rectWidth, zeige_Counter ? 50
        : 10, label_next, AnimalScript.DIRECTION_SW), "rect_variablen", null);
    label_Variablen.show();
    text_beschreibung
        .setText(
            "Der Algorithmus beginnt mit der Initialisierung, die occ und next füllt.",
            null, null);
    sc_skipSearch.unhighlight(0);
    sc_skipSearch.unhighlight(1);
    sc_skipSearch.highlight(zeile_init);
    if (zeige_Counter)
      counter_next.show();
    label_occ.show();
    stringMatrix_occ = lang.newStringMatrix(new Offset(5, -3, label_occ,
        AnimalScript.DIRECTION_NE), new String[2][1], "occ_map", null, occ_map);

    tvcounter_occ = new TwoValueCounter();
    TwoValueView counter_occ = lang.newCounterView(tvcounter_occ, new Offset(
        15, 27, label_occ, AnimalScript.DIRECTION_SW), getCounterproperties(),
        true, true);
    label_next.show();
    if (!zeige_Counter)
      counter_occ.hide();
    lang.nextStep("Initialisierung");

    show_Initialisierung();

    sc_skipSearch.unhighlight(zeile_init);
  }

  private void show_Initialisierung() {
    int zeile_init_header = 0;
    sc_init.show();
    sc_init.highlight(zeile_init_header);
    text_beschreibung.setText("Durchführung der Initialisierung.", null, null);

    lang.nextStep();
    sc_init.unhighlight(zeile_init_header);
    sc_init.highlight(zeile_init_header + 1);
    text_beschreibung.setText(
        "Den next-Array anlegen mit ausreichender Größe.", null, null);
    stringArray_next.show();

    lang.nextStep();
    sc_init.unhighlight(zeile_init_header + 1);
    sc_init.highlight(zeile_init_header + 2);
    text_beschreibung.setText("Iteriere über alle Zeichen des Suchtextes.",
        null, null);

    lang.nextStep();
    sc_init.unhighlight(zeile_init_header + 2);

    for (int i = 0; i < Suchtext.length(); i++) {
      sc_init.highlight(zeile_init_header + 3);
      sc_init.unhighlight(zeile_init_header + 5);
      if (i > 0)
        stringArray_suchText.unhighlightCell(i - 1, null, null);
      stringArray_suchText.highlightCell(i, null, null);
      text_beschreibung.setText("Das " + i
          + ". Zeichen aus dem Suchtext hinterlegen.", null, null);
      char curChar = stringArray_suchText.getData(i).charAt(0);

      if (zeige_Fragen && !first_nextZuweisung) {
        if (questionCount_Initialisierung < 2) {
          questionCount_Initialisierung++;
          FillInBlanksQuestionModel fibq = new FillInBlanksQuestionModel(
              "next_Zuweisung_Abfrage_" + unique_Question_Nr++);
          fibq.setPrompt("Welcher Wert wird im nächsten Schritt dem next-Array zugwiesen? (-1 für null nutzen)");
          // richtige Antwort ermitteln
          String antwort = algo_occ.containsKey(curChar) ? ""
              + algo_occ.get(curChar) : "-1";
          fibq.addAnswer(antwort, 1, "Korrekt.");
          fibq.addAnswer(null, 0, "Falsch, es wird " + antwort
              + " zugewiesen, wie man am Inhalt der occ-Map entnehmen kann.");
          fibq.setGroupID("Initialisierung");
          lang.addFIBQuestion(fibq);

        }
      }
      first_nextZuweisung = false;
      lang.nextStep();

      sc_init.unhighlight(zeile_init_header + 3);
      sc_init.highlight(zeile_init_header + 4);
      tvcounter_occ.accessInc(1);
      if (algo_occ.containsKey(curChar)) {
        text_beschreibung.setText(
            "Da '" + curChar
                + "' in occ vorkommt, wird das letzte Erscheinen (bei "
                + algo_occ.get(curChar) + ") in next[" + i + "] hintelegt.",
            null, null);
        algo_next[i] = algo_occ.get(curChar);
      } else {
        text_beschreibung.setText("Da '" + curChar
            + "' noch nicht in occ vorkommt, wird null in next[" + i
            + "] hintelegt.", null, null);
        algo_next[i] = null;
      }
      stringArray_next.put(i,
          algo_next[i] == null ? null : algo_next[i].toString(), null, null);
      stringArray_next.highlightElem(i, null, null);
      lang.nextStep();

      sc_init.unhighlight(zeile_init_header + 4);
      sc_init.highlight(zeile_init_header + 5);
      stringArray_next.unhighlightElem(i, null, null);
      text_beschreibung.setText("Das letzte Erscheinen von '" + curChar
          + "' bei " + i + " wird in occ hinterlegt.", null, null);
      tvcounter_occ.assignmentsInc(1);
      if (!algo_occ_indices.containsKey(curChar)) {
        stringMatrix_occ = addItemToMap(stringMatrix_occ, "" + i, "" + curChar);
        algo_occ_indices.put(curChar, algo_occ_indices.size());
      } else {
        stringMatrix_occ.highlightElem(1, algo_occ_indices.get(curChar), null,
            null);
        stringMatrix_occ.put(1, algo_occ_indices.get(curChar), "" + i, null,
            null);
        stringMatrix_occ.put(0, algo_occ_indices.get(curChar),
            "" + String.valueOf(curChar), null, null);
      }
      algo_occ.put(curChar, i);

      lang.nextStep();
      stringMatrix_occ.unhighlightElem(1, algo_occ_indices.get(curChar), null,
          null);
    }

    sc_init.unhighlight(zeile_init_header + 5);
    stringArray_suchText.unhighlightCell(Suchtext.length() - 1, null, null);
    text_beschreibung
        .setText(
            "Nun ist der Vorlauf abegschloßen und die eigentliche Suche kann gestartet werden.",
            null, null);

    lang.nextStep("Eigentliche Suche");
    sc_init.hide();
  }

  private void show_VariablenDeklaration_m_n_k_result() {
    int zeile_int_m = 6;
    text_beschreibung
        .setText(
            "Zunächst werden die Längen des Suchtextes und Textes, sowie ein Integer k angelegt.",
            null, null);
    sc_skipSearch.highlight(zeile_int_m);
    sc_skipSearch.highlight(zeile_int_m + 1);
    sc_skipSearch.highlight(zeile_int_m + 2);
    Text heading = lang.newText(new Offset(5, 10, rect_ErgebnisseVorlauf,
        AnimalScript.DIRECTION_SW), "Variablen:", "label_Variablen", null,
        beschreibungHeading);
    Text m = lang.newText(new Offset(5, 5, heading, AnimalScript.DIRECTION_SW),
        "m=" + Suchtext.length() + "  (Suchtextlänge)", "label_m", null,
        beschreibung);
    Text n = lang.newText(new Offset(0, 5, m, AnimalScript.DIRECTION_SW), "n="
        + Text.length() + "   (Textlänge)", "label_n", null, beschreibung);
    text_var_k = lang.newText(new Offset(0, 5, n, AnimalScript.DIRECTION_SW),
        "k=?", "label_k", null);
    text_var_i = lang.newText(new Offset(0, 5, text_var_k,
        AnimalScript.DIRECTION_SW), "i", "label_i", null);
    Text label_result = lang.newText(new Offset(0, 5, text_var_i,
        AnimalScript.DIRECTION_SW), "result=", "label_result", null,
        beschreibung);
    String[] results = new String[1];
    stringArray_result = lang.newStringArray(new Offset(5, 0, label_result,
        AnimalScript.DIRECTION_NE), results, "stringArray_result", null,
        text_Einstellung);
    text_var_i.hide();
    stringArray_result.hide();
    label_result.hide();
    lang.newRect(new Offset(-5, -5, heading, AnimalScript.DIRECTION_NW),
        new Offset(rectWidth, 10, label_result, AnimalScript.DIRECTION_SW),
        "rect_variablen", null);
    lang.nextStep();

    sc_skipSearch.unhighlight(zeile_int_m);
    sc_skipSearch.unhighlight(zeile_int_m + 1);
    sc_skipSearch.unhighlight(zeile_int_m + 2);
    sc_skipSearch.highlight(zeile_int_m + 3);
    stringArray_result.show();
    label_result.show();
    text_beschreibung
        .setText(
            "Anlegen des Ergebnisvektors, der die Indizes des Suchtextes im Text enthält.",
            null, null);

    if (zeige_Fragen) {
      if (questionCount_Variablen_Deklaration < 2) {
        questionCount_Variablen_Deklaration++;
        MultipleChoiceQuestionModel mCQM = new MultipleChoiceQuestionModel(
            "occ_Abfrage" + unique_Question_Nr++);
        mCQM.setPrompt("Mit welcher Variable kann mit nur einem Zugriff bestimmt werden, ob sich ein Zeichen im Suchtext befindet?");
        mCQM.addAnswer(
            "Der next-Array",
            0,
            "Falsch, der next-Array zeigt nur das nächste Vorkommen eines Zeichens.Die richtige Antwort wäre die occ-Map gewesen.");
        mCQM.addAnswer(
            "Die occ-Map",
            1,
            "Korrekt, eine Abfrage gibt null zurück, wenn sich ein Zeichen nicht im Suchtext befindet.");
        mCQM.addAnswer(
            "Der result-Vektor",
            0,
            "Falsch, der result Array enthält nur die Indizes, an denen der Suchtext im Text vorkommt. Die richtige Antwort wäre die occ-Map gewesen.");
        mCQM.setGroupID("Variablen_Deklaration");
        lang.addMCQuestion(mCQM);
      }
    }

    lang.nextStep();
    sc_skipSearch.unhighlight(zeile_int_m + 3);
  }

  private void show_forSchleife() {
    int zeile_for_Schleife = 11;

    arrayMarker_Text_i.show();

    text_var_i.show();
    boolean firstRun = true;
    text_beschreibung
        .setText(
            "Über Text in Abständen der Suchtextlänge iterieren (Pivotelement ist im Text markiert).",
            null, null);

    int m = Suchtext.length(), n = Text.length();
    for (int i = m - 1; i < n; i += m) {
      arrayMarker_Text_i.move(i, null, null);
      if (firstRun)
        firstRun = false;
      else
        text_beschreibung.setText("Pivotelement veschieben um m=" + m
            + " nach rechts auf i=" + i + ".", null, null);

      sc_skipSearch.highlight(zeile_for_Schleife);
      text_var_i.setText("i=" + i, null, null);
      text_var_i.changeColor(null, Color.RED, null, null);
      lang.nextStep();

      text_var_i.changeColor(null, Color.BLACK, null, null);
      sc_skipSearch.unhighlight(zeile_for_Schleife);
      sc_skipSearch.highlight(zeile_for_Schleife + 1);
      text_beschreibung
          .setText(
              "Prüfen, ob '"
                  + Text.charAt(i)
                  + "' (in 'Text'-markiert) im Suchtext vorkommt und in k hinterlegen.",
              null, null);
      tvcounter_occ.accessInc(1);
      algo_k = algo_occ.get(Text.charAt(i));

      if (zeige_Fragen) {
        if (questionCount_for_Schleife < 2) {
          questionCount_for_Schleife++;
          FillInBlanksQuestionModel fibq = new FillInBlanksQuestionModel(
              "Zeichen_Abfrage_" + unique_Question_Nr++);
          fibq.setPrompt("An welcher Stelle des Suchtextes befindet sich das aktuelle Zeichen '"
              + Text.charAt(i) + "' (-1 für nicht vorhanden)?");
          if (algo_k == null) {
            fibq.addAnswer("-1", 1, "Korrekt, das Zeichen '" + Text.charAt(i)
                + "' ist nicht im Suchtext vorhanden.");
          } else {
            fibq.addAnswer(algo_k + "", 1,
                "Korrekt, das Zeichen '" + Text.charAt(i)
                    + "' befindet sich an der Stelle " + algo_k + ".");
          }
          fibq.addAnswer(null, 0, "Falsch, das Zeichen '" + Text.charAt(i)
              + "' befindet sich an der Stelle " + algo_k
              + ", was direkt aus der occ-Map gelesen werden kann.");
          fibq.setGroupID("for_Schleife");
          lang.addFIBQuestion(fibq);
        }
      }

      text_var_k.setText("k=" + algo_k, null, null);
      text_var_k.changeColor(null, Color.RED, null, null);
      if (algo_k != null) {
        stringMatrix_occ.highlightCell(0, algo_occ_indices.get(Text.charAt(i)),
            null, null);
        stringMatrix_occ.highlightCell(1, algo_occ_indices.get(Text.charAt(i)),
            null, null);
      }
      char buchstabe = stringArray_text.getData(i).charAt(0);
      lang.nextStep();

      if (!algo_occ.containsKey(buchstabe)) {
        sc_skipSearch.unhighlight(zeile_for_Schleife + 1);
        sc_skipSearch.highlight(zeile_for_Schleife + 2);
        text_beschreibung.setText("Da '" + Text.charAt(i)
            + "' nicht im Suchtext vorkommt, wird Pivotelement verschoben.",
            null, null);
        text_var_k.changeColor(null, Color.BLACK, null, null);
        lang.nextStep();

        sc_skipSearch.unhighlight(zeile_for_Schleife + 2);
        continue;
      }

      sc_skipSearch.unhighlight(zeile_for_Schleife + 1);
      text_var_k.changeColor(null, Color.BLACK, null, null);
      stringMatrix_occ.unhighlightCellRowRange(0, 1,
          algo_occ_indices.get(Text.charAt(i)), null, null);

      show_whileSchleife(i, m, n);

      if (arrayMarker_Text_i.getPosition() != arrayMarker_Text_i_k
          .getPosition())
        arrayMarker_Text_i.hide();
      sc_skipSearch.unhighlight(zeile_for_Schleife + 8);
      sc_skipSearch.unhighlight(zeile_for_Schleife + 5);

    }

  }

  private void show_whileSchleife(int i, int m, int n) {
    int zeile_while = 15;

    sc_skipSearch.highlight(zeile_while);

    boolean firstrun = true;

    while (algo_k != null && i - algo_k <= n - m) {
      arrayMarker_Text_i_k.move(i - algo_k, null, null);
      if (firstrun) {
        text_beschreibung
            .setText(
                "Über Suchtext iterieren, solange das Pivotelement darin vorkommt und Text nicht endet.",
                null, null);
        firstrun = false;
      } else {
        text_beschreibung
            .setText(
                "Da k == "
                    + algo_k
                    + " >= 0, also das Pivotelement noch im Suchtext ist, nächste Iteration.",
                null, null);
        sc_skipSearch.highlight(zeile_while);
      }
      lang.nextStep();

      arrayMarker_Text_i_k.show();
      for (int temp = i - algo_k; temp < i - algo_k + Suchtext.length(); temp++)
        stringArray_text.highlightCell(temp, null, null);
      if (arrayMarker_Text_i_k.getPosition() == arrayMarker_Text_i
          .getPosition()) {
        arrayMarker_Text_i.hide();
        arrayMarker_Text_i_k.hide();
        arrayMarker_Text_i_k = lang.newArrayMarker(stringArray_text,
            arrayMarker_Text_i_k.getPosition(), "new_ArrayMarker_i_k", null,
            getArrayMarkerProperties("i-k==i"));

      }
      sc_skipSearch.unhighlight(zeile_while);
      sc_skipSearch.highlight(zeile_while + 1);
      text_beschreibung.setText(
          "Per lineare Suche ermitteln, ob sich ab Position i-k=="
              + (i - algo_k) + " im Text das Suchwort befindet.", null, null);
      if (zeige_Fragen && !first_lineareSuche && zeige_lineare_Suche) {
        if (questionCount_lineare_Suche < 2) {
          questionCount_lineare_Suche++;
          FillInBlanksQuestionModel fibq = new FillInBlanksQuestionModel(
              "Anzahl_Vergleiche_Abfrage_" + unique_Question_Nr++);
          fibq.setPrompt("Wie viele Vergleiche benötigt die lineare Suche, bis ein Ergebnis bekannt ist?");
          // richtige Antwort ermitteln
          int anzahl_Vergleiche = 0;
          for (; anzahl_Vergleiche < Suchtext.length(); anzahl_Vergleiche++) {
            if (Text.charAt(i - algo_k + anzahl_Vergleiche) != Suchtext
                .charAt(anzahl_Vergleiche))
              break;
          }

          fibq.addAnswer(null, 1, "Korrekt.");
          fibq.addAnswer(null, 0, "Falsch, es werden " + anzahl_Vergleiche
              + " Vergleiche benötigt, wie der folgende Ablauf zeigt.");
          fibq.setGroupID("lineare_Suche");
          lang.addFIBQuestion(fibq);
        }
      }
      first_lineareSuche = false;
      lang.nextStep();

      boolean match = show_lineareSuche(i - algo_k);
      sc_skipSearch.unhighlight(zeile_while + 1);

      if (match) {
        sc_skipSearch.highlight(zeile_while + 2);
        text_beschreibung.setText("Da sich bei i-k==" + (i - algo_k)
            + " im Text das Suchwort befindet, wird " + (i - algo_k)
            + " in result gespeichert.", null, null);

        stringArray_result = addItemToStringArray(stringArray_result, ""
            + (i - algo_k));

        lang.nextStep();
        sc_skipSearch.unhighlight(zeile_while + 2);
      }
      sc_skipSearch.highlight(zeile_while + 3);
      stringArray_next.highlightElem(algo_k, null, null);
      String temp = stringArray_next.getData(algo_k);
      algo_k = temp == null ? null : new Integer(temp);
      text_var_k.changeColor(null, Color.RED, null, null);
      text_var_k.setText("k=" + algo_k, null, null);

      if (algo_k == null)
        text_beschreibung
            .setText(
                "Da '"
                    + Text.charAt(i)
                    + "' nicht mehr im Suchtext vorkommt, wird die Schleife verlassen.",
                null, null);
      else
        text_beschreibung.setText("Da '" + Text.charAt(i) + "' bei " + algo_k
            + " nochmal im Suchtext vorkommt, weitersuchen.", null, null);
      lang.nextStep();

      stringArray_next.unhighlightElem(0, stringArray_next.getLength() - 1,
          null, null);
      text_var_k.changeColor(null, Color.BLACK, null, null);
      sc_skipSearch.unhighlight(zeile_while + 3);
      if (!arrayMarker_Text_i_k.getProperties().get("label").equals("i-k")) {
        arrayMarker_Text_i_k.hide();
        arrayMarker_Text_i_k = lang.newArrayMarker(stringArray_text,
            arrayMarker_Text_i_k.getPosition(), "new_ArrayMarker_i_k", null,
            getArrayMarkerProperties("i-k"));
      }
      arrayMarker_Text_i_k.hide();
      for (int tempi = 0; tempi < stringArray_text.getLength(); tempi++)
        stringArray_text.unhighlightCell(tempi, null, null);
    }

    sc_skipSearch.unhighlight(zeile_while);
    sc_skipSearch.unhighlight(zeile_while + 1);
    sc_skipSearch.unhighlight(zeile_while + 2);
    sc_skipSearch.unhighlight(zeile_while + 3);
    sc_skipSearch.unhighlight(zeile_while + 4);
    arrayMarker_Text_i.show();

  }

  private boolean show_lineareSuche(int index) {
    boolean ret = true;
    if (zeige_lineare_Suche)
      sc_matchesAt.show();
    int zeile_matchesAt = 0;
    text_beschreibung
        .setText(
            "Bei der linearen Suche wird Suchtext zeichenweise mit dem Text verglichen.",
            null, null);
    sc_matchesAt.highlight(zeile_matchesAt);

    if (zeige_lineare_Suche)
      lang.nextStep();

    boolean firstrun = true;

    for (int i = index; index + Suchtext.length() > i; i++) {
      sc_matchesAt.unhighlight(zeile_matchesAt);
      sc_matchesAt.unhighlight(zeile_matchesAt + 2);
      sc_matchesAt.highlight(zeile_matchesAt + 1);
      if (firstrun) {
        text_beschreibung.setText("Ab dem übergebenen Index (" + index
            + ") über den Text iterieren.", null, null);
        firstrun = false;
      } else
        text_beschreibung.setText("Weiter an der nächsten Stelle (i==" + i
            + ").", null, null);
      if (zeige_lineare_Suche)
        lang.nextStep();

      sc_matchesAt.unhighlight(zeile_matchesAt + 1);
      sc_matchesAt.highlight(zeile_matchesAt + 2);
      text_beschreibung.setText("Testen ob text[" + i + "] != suchtext["
          + (i - index) + "] gilt (verglichene Elemente sind hervorgehoben).",
          null, null);
      stringArray_text.highlightElem(i, null, null);
      stringArray_suchText.highlightElem(i - index, null, null);
      char temp_text = stringArray_text.getData(i).charAt(0);
      char temp_suchtext = stringArray_suchText.getData(i - index).charAt(0) /*
                                                                              * Suchtext
                                                                              * .
                                                                              * charAt
                                                                              * (
                                                                              * i
                                                                              * -
                                                                              * index
                                                                              * )
                                                                              */;
      if (zeige_lineare_Suche)
        lang.nextStep();

      if (temp_text != temp_suchtext) {
        sc_matchesAt.unhighlight(zeile_matchesAt + 2);
        sc_matchesAt.highlight(zeile_matchesAt + 3);
        text_beschreibung.setText(
            "Da '" + Text.charAt(i) + "'!='" + Suchtext.charAt(i - index)
                + "' gilt, ist der Suchtext nich beim Index im Text.", null,
            null);
        ret = false;
        break;
      } else {
        ret = true;
      }
    }

    if (ret) {
      sc_matchesAt.unhighlight(zeile_matchesAt + 2);
      sc_matchesAt.highlight(zeile_matchesAt + 4);
      text_beschreibung
          .setText(
              "Da der Suchtext beim gegebenen Index im Text vorhanden ist, wird true zurückgegeben.",
              null, null);
    }
    if (zeige_lineare_Suche)
      lang.nextStep();

    sc_matchesAt.unhighlight(zeile_matchesAt + 3);
    sc_matchesAt.unhighlight(zeile_matchesAt + 4);
    stringArray_text.unhighlightElem(0, Text.length() - 1, null, null);
    stringArray_suchText.unhighlightElem(0, Suchtext.length() - 1, null, null);

    if (zeige_lineare_Suche)
      sc_matchesAt.hide();
    return ret;
  }

  private void show_return() {
    arrayMarker_Text_i.hide();
    arrayMarker_Text_i_k.hide();
    int zeile_return = 21;
    sc_skipSearch.highlight(zeile_return);
    text_beschreibung
        .setText(
            "Der Text wurde komplett durchsucht und result enthält alle Fundorte des Suchtextes.",
            null, null);
    lang.nextStep("Zusammenfassung");
    sc_skipSearch.unhighlight(zeile_return);
  }

  private void show_Zusammenfassung() {
    sc_skipSearch.hide();

    text_beschreibung.setText(
        "Die Vorkommen des Suchtextes sind im Text markiert.", null, null);
    int anzahlErgebnisse = stringArray_result.getLength();
    if (anzahlErgebnisse == 1)
      if (stringArray_result.getData(0) == null)
        anzahlErgebnisse = 0;

    for (int i = 0; i < anzahlErgebnisse; i++) {
      stringArray_text.highlightElem(
          new Integer(stringArray_result.getData(i)), null, null);
      stringArray_text.highlightCell(
          new Integer(stringArray_result.getData(i)), new Integer(
              stringArray_result.getData(i)) + Suchtext.length() - 1, null,
          null);
    }
    Text label_header_zusammefassung = lang.newText(new Offset(5, 5,
        rect_sourceCode, AnimalScript.DIRECTION_NW), "Zusammenfassung:",
        "Heading_Zusammenfassung", null, beschreibungHeading);

    // Anzahl der Speicherzugriffe bei linearer Suche ermitteln
    int speicherzugriffe_lineareSuche = 0;
    for (int index = 0; index < Text.length(); index++) {
      for (int j = 0; j < Suchtext.length() && index + j < Text.length(); j++) {
        speicherzugriffe_lineareSuche += 2;
        if (Suchtext.charAt(j) != Text.charAt(index + j))
          break;
      }
    }

    int speicherzugriffe_insgesamt_skipSearch = 0;
    speicherzugriffe_insgesamt_skipSearch += tvcounter_next.getAccess();
    speicherzugriffe_insgesamt_skipSearch += tvcounter_next.getAssigments();
    speicherzugriffe_insgesamt_skipSearch += tvcounter_occ.getAccess();
    speicherzugriffe_insgesamt_skipSearch += tvcounter_occ.getAssigments();
    speicherzugriffe_insgesamt_skipSearch += tvcounter_text.getAccess();
    speicherzugriffe_insgesamt_skipSearch += tvcounter_text.getAssigments();
    speicherzugriffe_insgesamt_skipSearch += tvcounter_suchtext.getAccess();
    speicherzugriffe_insgesamt_skipSearch += tvcounter_suchtext.getAssigments();

    int speicherzugriffe_eingaben_skipSearch = 0;
    speicherzugriffe_eingaben_skipSearch += tvcounter_text.getAccess();
    speicherzugriffe_eingaben_skipSearch += tvcounter_text.getAssigments();
    speicherzugriffe_eingaben_skipSearch += tvcounter_suchtext.getAccess();
    speicherzugriffe_eingaben_skipSearch += tvcounter_suchtext.getAssigments();

    Text zf_0 = lang
        .newText(new Offset(5, 30, label_header_zusammefassung,
            AnimalScript.DIRECTION_SW), "Zum Auffinden aller "
            + anzahlErgebnisse + " Vorkommen des Suchtextes im Text waren",
            "zf_0", null, beschreibung);
    Text zf_1 = lang.newText(new Offset(0, 5, zf_0, AnimalScript.DIRECTION_SW),
        "insgesamt " + speicherzugriffe_insgesamt_skipSearch
            + " Speicherzugriffe nötig. Davon entfallen "
            + speicherzugriffe_eingaben_skipSearch + " auf Text ", "zf_1",
        null, beschreibung);
    Text zf_2 = lang.newText(new Offset(0, 5, zf_1, AnimalScript.DIRECTION_SW),
        "und Suchtext. Die lineare Suche hingegen hätte "
            + speicherzugriffe_lineareSuche + " benötigt. ", "zf_2", null,
        beschreibung);
    Text zf_3;
    Text zf_4;
    Text zf_5;
    Text zf_6;
    if (speicherzugriffe_insgesamt_skipSearch >= speicherzugriffe_lineareSuche) {
      zf_3 = lang.newText(new Offset(0, 5, zf_2, AnimalScript.DIRECTION_SW),
          "Die hohe Anzahl von Speicherzugriffen erklärt sich durch die",
          "zf_3", null, beschreibung);
      zf_4 = lang.newText(new Offset(0, 5, zf_3, AnimalScript.DIRECTION_SW),
          "kurze Suchtextlänge, wegen der immer wieder die occ-Map", "zf_4",
          null, beschreibung);
      zf_5 = lang.newText(new Offset(0, 5, zf_4, AnimalScript.DIRECTION_SW),
          "abgefragt werden muss. Zudem amotisieren sich noch nicht die ",
          "zf_5", null, beschreibung);
      zf_6 = lang.newText(new Offset(0, 5, zf_5, AnimalScript.DIRECTION_SW),
          "Speicherzugriffe, die bei der Initialisierung anfallen. ", "zf_6",
          null, beschreibung);
    } else {
      zf_3 = lang.newText(new Offset(0, 0, zf_2, AnimalScript.DIRECTION_SW),
          "", "zf_3", null, beschreibung);
      zf_4 = lang.newText(new Offset(0, 0, zf_2, AnimalScript.DIRECTION_SW),
          "", "zf_4", null, beschreibung);
      zf_5 = lang.newText(new Offset(0, 0, zf_2, AnimalScript.DIRECTION_SW),
          "", "zf_5", null, beschreibung);
      zf_6 = lang.newText(new Offset(0, 0, zf_2, AnimalScript.DIRECTION_SW),
          "", "zf_6", null, beschreibung);
    }
    Text zf_7 = lang.newText(
        new Offset(0, 15, zf_6, AnimalScript.DIRECTION_SW),
        "Wie aus dem Verlauf des Algorithmus ersichtlich, wird die ", "zf_7",
        null, beschreibung);
    Text zf_8 = lang.newText(new Offset(0, 5, zf_7, AnimalScript.DIRECTION_SW),
        "optimale Komplexität von O(n/m) nur erreicht wird, wenn die ", "zf_8",
        null, beschreibung);
    Text zf_9 = lang.newText(new Offset(0, 5, zf_8, AnimalScript.DIRECTION_SW),
        "Pivotelemente nie im Suchtext vorkommen. Andererseits wird die ",
        "zf_9", null, beschreibung);
    Text zf_10 = lang.newText(
        new Offset(0, 5, zf_9, AnimalScript.DIRECTION_SW),
        "schlechteste Komplexität von O(m*n) nur erreicht, wenn sich jedes ",
        "zf_10", null, beschreibung);
    Text zf_11 = lang.newText(
        new Offset(0, 5, zf_10, AnimalScript.DIRECTION_SW),
        "Pivotelement im Suchtext befindet, weshalb immer eine lineare ",
        "zf_11", null, beschreibung);
    lang.newText(new Offset(0, 5, zf_11, AnimalScript.DIRECTION_SW),
        "Suche ausgeführt werden muss.", "zf_11", null, beschreibung);

  }

  private StringArray addItemToStringArray(StringArray sa, String newElement) {
    String[] temp;
    if (sa.getLength() == 1)
      if (sa.getData(0) == null) {
        sa.put(0, newElement, null, null);
        return sa;
      }
    temp = new String[sa.getLength() + 1];

    for (int j = 0; j < sa.getLength(); j++)
      temp[j] = sa.getData(j);
    temp[temp.length - 1] = newElement;

    // nich so gut wie exchange, aber exchange wirft Fehler beim parsen
    sa.hide();
    return lang.newStringArray(sa.getUpperLeft(), temp, sa.getName(), null,
        text_Einstellung);
  }

  CounterProperties getCounterproperties() {
    CounterProperties cp = new CounterProperties();
    cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);

    return cp;

  }

  ArrayMarkerProperties getArrayMarkerProperties(String text) {
    ArrayMarkerProperties ret = new ArrayMarkerProperties();
    ret.set("short", true);
    ret.set("label", text);
    return ret;
  }

  private StringMatrix addItemToMap(StringMatrix sa, String newElement1,
      String newElement2) {
    String[][] oldElements;
    if (sa.getNrCols() == 1)
      if (sa.getElement(0, 0) == null) {
        sa.put(1, 0, newElement1, null, null);
        sa.put(0, 0, newElement2, null, null);
        return sa;
      }

    oldElements = new String[2][sa.getNrCols() + 1];

    for (int j = 0; j < sa.getNrCols(); j++) {
      oldElements[1][j] = sa.getElement(1, j);
      oldElements[0][j] = sa.getElement(0, j);
    }
    oldElements[1][sa.getNrCols()] = newElement1;
    oldElements[0][sa.getNrCols()] = newElement2;

    // nich so gut wie exchange, aber exchange wirft Fehler beim parsen
    sa.hide();
    return lang.newStringMatrix(sa.getUpperLeft(), oldElements, sa.getName(),
        null, occ_map);
  }

  int anzahlVerschiedenerZeichen(String text) {
    Set<Character> set = new HashSet<Character>();
    for (char c : text.toCharArray())
      set.add(c);

    return set.size();
  }

  public void init() {
    lang = new AnimalScript("Skip Search [DE]", "Felix Hammacher", 1000, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    Text = (String) primitives.get("Text");
    zeige_Counter = (Boolean) primitives.get("zeige_Counter");
    next_array = (ArrayProperties) props.getPropertiesByName("next_array");
    Suchtext = (String) primitives.get("Suchtext");
    beschreibung = (TextProperties) props.getPropertiesByName("beschreibung");
    occ_map = (MatrixProperties) props.getPropertiesByName("occ_map");
    text_Einstellung = (ArrayProperties) props
        .getPropertiesByName("text_Einstellung");
    zeige_Fragen = (Boolean) primitives.get("zeige_Fragen");
    zeige_lineare_Suche = (Boolean) primitives.get("zeige_lineare_Suche");
    suchtext_Einstellung = (ArrayProperties) props
        .getPropertiesByName("suchtext_Einstellung");
    quelltext = (SourceCodeProperties) props.getPropertiesByName("quelltext");

    skip_search();

    return lang.toString();
  }

  public String getName() {
    return "Skip Search [DE]";
  }

  public String getAlgorithmName() {
    return "Skip Search";
  }

  public String getAnimationAuthor() {
    return "Felix Hammacher, Patrick Lerch";
  }

  public String getDescription() {
    return "Skip Search ist ein Algorithmus zum Auffinden eines Suchtextes in einem Text. "
        + "\n"
        + "Daf&#252;r wird immer an den Textstellen, die ein vielfaches der Suchtextl&#228;nge darstellen, gepr&#252;ft, ob sich der "
        + "\n"
        + "Buchstabe an dieser Stelle im Suchtext befindet. Erst, wenn dies der Fall ist, wird der folgende Text mit "
        + "\n"
        + "dem Suchtext per lineare Suche verglichen. Um die Abfrage, ob sich der jeweilige Buchstabe im "
        + "\n"
        + "Suchtext befindet, zu beschleunigen, werden in einer Initialisierung zwei Datenstrukturen angelegt."
        + "\n"
        + "\n"
        + "F&#252;r einen Text der L&#228;nge n und einen Suchtext der L&#228;nge m, besitzt der der Algorithmus im besten Fall"
        + "\n"
        + "eine Komplexit&#228;t von O(n/m), im schlechtesten von O(n*m).";
  }

  public String getCodeExample() {
    return "Map<Character, Integer> occ = new HashMap<Character, Integer>();"
        + "\n" + "Integer[] next;" + "\n" + "\n"
        + "void init(char[] suchtext){				" + "\n"
        + "   next = new Integer[suchtext.length];" + "\n"
        + "   for (int i=0; i< suchtext.length; i++){" + "\n"
        + "      char curChar = suchtext[i];" + "\n"
        + "      next[i]=occ.get(curChar);" + "\n"
        + "      occ.put(curChar, i);" + "\n" + "   }    " + "\n" + "}" + "\n"
        + "	" + "\n"
        + "boolean matchesAt(char[] text, char[] suchtext, int index){" + "\n"
        + "   for(int i = index; index + suchtext.length > i; i++)" + "\n"
        + "      if(text[i] != suchtext[i-index])" + "\n"
        + "         return false;		" + "\n" + "   return true;	    " + "\n"
        + "}" + "\n" + "	" + "\n"
        + "Vector<Integer> skipSearch(char[] text, char[] suchtext){	" + "\n"
        + "   init(suchtext);" + "\n" + "		" + "\n"
        + "   int m = suchtext.length;" + "\n" + "   int n = text.length;"
        + "\n" + "   int k;" + "\n"
        + "   Vector<Integer> result = new Vector<Integer>();" + "\n" + "	    "
        + "\n" + "   for (int i=m-1; i<n; i+=m){	    	" + "\n"
        + "      if((k = occ.get(text[i])) == null)" + "\n"
        + "         continue;" + "\n" + "\n"
        + "      while(k != null && i-k <= n-m){" + "\n"
        + "        if (matchesAt(text,suchtext,i-k))" + "\n"
        + "          result.add(i-k);" + "\n" + "        k=next[k];" + "\n"
        + "      }" + "\n" + "   }" + "\n" + "   return result;" + "\n" + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

}