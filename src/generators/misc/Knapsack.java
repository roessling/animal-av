package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.hashing.Fletcher;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntArray;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class Knapsack implements ValidatingGenerator {
  protected Language lang;

  private int        r, cur_vol, i, k; // Hilfsvariablen
  private int        access = 0;      // Gesamtzahl Zugriffe
  private int        assign = 0;      // Gesamtzahl Zuweisungen

  private IntMatrix  item_matrix, comp_matrix; // Visuelle Darstellung der
                                               // Gegenstandsliste und der
                                               // Berechnungs-/Ergebnissmatrix
  private MatrixProperties item_props, comp_props;

  private IntArray         selection;             // Repräsentiert am Ende die
                                                   // auszuwählenden Gegenstände
  private ArrayProperties  selection_props;

  private SourceCode       sc_comp, sc_back, expl, info_comp, info_back; // Quellcode-Felder,
                                                                         // sowie
                                                                         // Info-Textfelder,
                                                                         // die
                                                                         // per
                                                                         // SourceCode
                                                                         // realisiert
                                                                         // werden
  private SourceCodeProperties sc_props, expl_props;

  private Text                 i_txt, k_txt, r_txt, cur_vol_txt; // diverse
                                                                 // Info-Textfelder

  private Text                 headline;                        // Überschrift

  private Node                 sc_coords, selection_coords;

  private Timing               duration = new TicksTiming(1);

  @Override
  public void init() {
    lang = new AnimalScript("Rucksack-Problem per Dynamischer Programmierung",
        "Erich Wittenbeck", 640, 480);
    lang.setStepMode(true);

    selection_coords = new Coordinates(20, 400);

    item_props = new MatrixProperties();
    comp_props = new MatrixProperties();
    sc_props = new SourceCodeProperties();
    selection_props = new ArrayProperties();
    expl_props = new SourceCodeProperties();

    item_props.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.GREEN);
    item_props.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    item_props.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
    item_props.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GRAY);

    comp_props.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    comp_props.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    comp_props.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);

    sc_props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    sc_props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    sc_props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));

    selection_props.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
        Color.RED);
    selection_props.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    selection_props.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);

    expl_props.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    expl_props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "Monospaced", Font.PLAIN, 15));
  }

  public void knapsack(int[][] elem_data, int maxV) {

    headline = lang.newText(new Coordinates(20, 20),
        "Das Rucksackproblem, dynamisch geloest - Animation", "", null);
    headline.setFont(new Font("Monospaced", Font.PLAIN, 20), null, duration);
    Rect head_rect = lang.newRect(new Offset(-2, -2, headline,
        AnimalScript.DIRECTION_NW), new Coordinates(620, 40), "", null);
    lang.nextStep("Einführung");

    dispExp(0); // Einleitung

    expl.hide();
    Text item_marking = lang.newText(new Offset(0, 15, headline, "SW"),
        "v[i]  w[i]", "", null);
    item_matrix = lang.newIntMatrix(new Offset(0, 7, item_marking, "SW"),
        elem_data, "", duration, item_props);
    sc_coords = new Offset(0, 20, item_matrix, "SW"); // Quellcode abhaengig von
                                                      // elem_matrix
    item_matrix.hide();
    item_marking.hide();
    showSourceCode_comp();
    lang.nextStep();

    // 'results' berechnen (hier : comp_matrix )

    sc_comp.highlight(0);
    item_matrix.show();
    item_marking.show();
    lang.nextStep();

    int[][] comp_data = new int[elem_data.length + 1][maxV + 1];
    cur_vol = 0; // haelt am Ende das tatsaechliche, verbrauchte Volumen

    sc_comp.unhighlight(0);
    dispExp(1); // Erklaerung von comp_matrix

    expl.hide();
    sc_comp.highlight(1);
    comp_matrix = lang.newIntMatrix(new Offset(80, 5, item_matrix, "NW"),
        comp_data, "", duration, comp_props);
    Text results = lang.newText(new Offset(80, 0, item_marking, "NW"),
        "results", "", null);
    lang.nextStep();

    sc_comp.toggleHighlight(1, 2);
    for (i = item_matrix.getNrRows() - 1; i >= 0; i--) {

      if (i_txt != null)
        i_txt.hide();
      i_txt = lang.newText(new Offset(10, 0, comp_matrix, "NE"), "i = " + i
          + "", "", null);

      item_matrix.highlightCellColumnRange(i, 0, 1, null, duration);
      lang.nextStep();

      sc_comp.toggleHighlight(2, 3);
      for (k = item_matrix.getElement(i, 0); k < comp_matrix.getNrCols(); k++) {

        if (k_txt != null)
          k_txt.hide();
        k_txt = lang.newText(new Offset(0, 0, i_txt, "SW"), "k = " + k + "",
            "", null);
        comp_matrix.highlightCell(i, k, null, duration);
        lang.nextStep();

        sc_comp.toggleHighlight(3, 4);
        showInfo_comp(i, k);

        info_comp.hide();
        comp_matrix.put(
            i,
            k,
            Math.max(
                item_matrix.getElement(i, 1)
                    + comp_matrix.getElement(i + 1,
                        k - item_matrix.getElement(i, 0)),
                comp_matrix.getElement(i + 1, k)), null, duration);
        comp_matrix.unhighlightCell(i + 1, k - item_matrix.getElement(i, 0),
            null, duration);
        comp_matrix.unhighlightCell(i + 1, k, null, duration);
        lang.nextStep();

        sc_comp.toggleHighlight(4, 3);
        comp_matrix.unhighlightCell(i, k, null, duration);

        assign++;
        access = access + 4;
      }
      sc_comp.toggleHighlight(3, 2);
      item_matrix.unhighlightCellColumnRange(i, 0, 1, null, duration);
    }
    sc_comp.hide();

    if (i_txt != null)
      i_txt.hide();

    if (k_txt != null)
      k_txt.hide();

    lang.nextStep("Die Ergebnisse stehen fest!");

    comp_matrix.hide();
    results.hide();
    dispExp(2); // Erlaeutern von Backtracking

    // Konkrete Auswahl der Gegenstände ( Backtracking )
    expl.hide();
    comp_matrix.show();
    results.show();
    selection_coords = new Offset(10, 5, comp_matrix, "NE");
    selection = lang.newIntArray(selection_coords, new int[0], "", null,
        selection_props);

    showSourceCode_back();
    lang.nextStep();

    cur_vol = maxV; // Aktuell betrachtetes Volumen ( Spalte in
                    // results/comp_matrix )
    r = comp_data[0][maxV]; // Ergebniss : Hoechstes Volumen bei maximalen
                            // Gesamtwert

    if (r == 0) { // Falls kein einziger Gegenstand reingepasst hat ...

      lang.hideAllPrimitives();
      headline.show();
      head_rect.show();
      dispExp(3);
      return;
    }

    sc_back.highlight(0);
    introduceRandCurVol();

    sc_back.toggleHighlight(0, 1);
    for (i = 0; i < comp_matrix.getNrRows() - 1; i++) {

      int old_vol = cur_vol;

      if (i_txt != null)
        i_txt.hide();
      i_txt = lang.newText(new Offset(0, 0, cur_vol_txt, "SW"),
          "i = " + i + "", "", null);

      comp_matrix.highlightCell(i, cur_vol, null, duration);
      lang.nextStep();

      sc_back.toggleHighlight(1, 2);
      Text info_break = lang.newText(new Offset(10, -25, comp_matrix, "SE"), ""
          + cur_vol + " - " + item_matrix.getElement(i, 0) + " < 0 -->", "",
          null);
      if (cur_vol - item_matrix.getElement(i, 0) < 0) {
        info_break.setText("" + cur_vol + " - " + item_matrix.getElement(i, 0)
            + " < 0 -->" + "true", null, duration);
        lang.nextStep();

        info_break.hide();
        sc_back.toggleHighlight(2, 3);
        lang.nextStep();

        sc_back.toggleHighlight(3, 9);
        break;

      }
      info_break.setText("" + cur_vol + " - " + item_matrix.getElement(i, 0)
          + " < 0 -->" + "false", null, duration);
      lang.nextStep();

      info_break.hide();
      sc_back.toggleHighlight(2, 4);

      showInfo_back(i);
      if (r - item_matrix.getElement(i, 1) == comp_matrix.getElement(i + 1,
          cur_vol - item_matrix.getElement(i, 0))) {

        selection.hide();
        selection = lang.newIntArray(selection_coords,
            appendToArray(Fletcher.toArray(selection), i), "", null,
            selection_props);
        selection.highlightCell(selection.getLength() - 1, null, duration);
        sc_back.toggleHighlight(4, 5);
        lang.nextStep();

        r = r - item_matrix.getElement(i, 1);
        sc_back.toggleHighlight(5, 6);
        updateR();

        cur_vol = cur_vol - item_matrix.getElement(i, 0);
        sc_back.toggleHighlight(6, 7);
        updateCurVol();

        sc_back.toggleHighlight(7, 1);

        access = access + 2;
      } else {
        comp_matrix.unhighlightCell(i + 1,
            cur_vol - item_matrix.getElement(i, 0), null, duration);
        sc_back.toggleHighlight(4, 1);
      }
      info_back.hide();
      item_matrix.unhighlightCell(i, 1, null, duration);
      comp_matrix.unhighlightCell(i, old_vol, null, duration);
    }
    selection.unhighlightCell(selection.getLength() - 1, null, duration);
    comp_matrix.unhighlightCell(elem_data.length - 1, 0, null, duration);
    sc_back.toggleHighlight(1, 9);
    lang.nextStep("Die zu waehlenden Gegenstaende stehen fest!");

    item_matrix.hide();
    item_marking.hide();
    comp_matrix.hide();
    results.hide();
    sc_back.hide();
    i_txt.hide();
    r_txt.hide();
    cur_vol_txt.hide();
    selection.hide();
    dispExp(3); // Schlusserlaeuterung
  }

  /**
   * Zeigt den Quellcode für den ersten Algorithmus ( Erstellen von 'results' )
   * an
   */
  private void showSourceCode_comp() {
    sc_comp = lang.newSourceCode(sc_coords, "", null, sc_props);

    sc_comp.addCodeLine("int knapsack(int[][] items, int V){", null, 0, null);
    sc_comp
        .addCodeLine(
            "int[][] results = new int[N + 1][V + 1] // zur korrekten Berechnung eine Zeile 'extra' ",
            null, 1, null);
    sc_comp.addCodeLine("for(int i = N; i >= 0; i--) ", null, 1, null);
    sc_comp.addCodeLine("for(int k = v[i]; k <= V; j++) // Ein Zugriff ", null,
        2, null);
    sc_comp
        .addCodeLine(
            "results[i][k] = max(w[i] + results[i+1][k - v[i]], results[i+1][k]) // Eine Zuweisung und 4 Zugriffe",
            null, 3, null);
    sc_comp.addCodeLine("return results[0][V]", null, 1, null);
    sc_comp.addCodeLine("}", null, 0, null);

  }

  /**
   * Zeigt den Code für das Backtracking ( Feststellen, welche konkreten
   * Gegenstände zu nehmen sind ) ausgehend von 'results'
   */
  private void showSourceCode_back() {
    sc_back = lang.newSourceCode(sc_coords, "", null, sc_props);

    sc_back.addCodeLine("backtrack(int[][] results){", null, 0, null);
    sc_back.addCodeLine("for(int i = 0; i < N; i++)", null, 1, null);
    sc_back.addCodeLine("if(currentVolume - v[i] < 0)", null, 2, null);
    sc_back
        .addCodeLine(
            "break // Weder dieser noch die nachfolgenden Gegenstaende passen in den Rucksack",
            null, 3, null);
    sc_back.addCodeLine(
        "if(r - w[i] == results[i+1][currentVolume - v[i]]){ // Zwei Zugriffe",
        null, 2, null);
    sc_back.addCodeLine("select(i)", null, 3, null);
    sc_back.addCodeLine("r = r - w[i] // Ein Zugriff", null, 3, null);
    sc_back.addCodeLine("currentVolume = currentVolume - v[i] // Ein Zugriff",
        null, 3, null);
    sc_back.addCodeLine("}", null, 2, null);
    sc_back.addCodeLine("}", null, 0, null);
  }

  /**
   * Veranschaulicht die Berechnung von max(a+b,c) visuell und dynamisch für den
   * Zuschauer
   */
  private void showInfo_comp(int i, int k) {
    info_comp = lang.newSourceCode(new Offset(10, -25, comp_matrix, "SE"), "",
        null, sc_props);
    info_comp.addCodeLine(
        "max(w[i] + results[i+1][k - v[i]], results[i+1][k])", null, 0, null);
    lang.nextStep();

    info_comp.hide();
    item_matrix.unhighlightCell(i, 0, null, duration);
    info_comp = lang.newSourceCode(new Offset(10, -25, comp_matrix, "SE"), "",
        null, sc_props);
    info_comp.addCodeLine("max(" + item_matrix.getElement(i, 1)
        + " + results[i+1][k - v[i]], results[i+1][k])", null, 0, null);
    lang.nextStep();

    info_comp.hide();
    comp_matrix.highlightCell(i + 1, k - item_matrix.getElement(i, 0), null,
        duration);
    info_comp = lang.newSourceCode(new Offset(10, -25, comp_matrix, "SE"), "",
        null, sc_props);
    info_comp.addCodeLine("max(" + item_matrix.getElement(i, 1) + " + "
        + comp_matrix.getElement(i + 1, k - item_matrix.getElement(i, 0))
        + ", results[i+1][k])", null, 0, null);
    lang.nextStep();

    info_comp.hide();
    comp_matrix.highlightCell(i + 1, k, null, duration);
    info_comp = lang.newSourceCode(new Offset(10, -25, comp_matrix, "SE"), "",
        null, sc_props);
    info_comp.addCodeLine(
        "max("
            + item_matrix.getElement(i, 1)
            + " + "
            + comp_matrix.getElement(i + 1, k - item_matrix.getElement(i, 0))
            + ", "
            + comp_matrix.getElement(i + 1, k)
            + ") = "
            + "max("
            + (item_matrix.getElement(i, 1) + comp_matrix.getElement(i + 1, k
                - item_matrix.getElement(i, 0))) + ", "
            + comp_matrix.getElement(i + 1, k) + ")", null, 0, null);
    lang.nextStep();

    info_comp.hide();
    info_comp = lang.newSourceCode(new Offset(10, -25, comp_matrix, "SE"), "",
        null, sc_props);
    info_comp.addCodeLine(
        ""
            + Math.max(
                (item_matrix.getElement(i, 1) + comp_matrix.getElement(i + 1, k
                    - item_matrix.getElement(i, 0))),
                comp_matrix.getElement(i + 1, k)), null, 0, null);
    info_comp.highlight(0);
    lang.nextStep();
  }

  /**
   * Aktuallisiert das Info-Textfeld r
   */
  private void updateR() {
    r_txt.hide();
    r_txt = lang.newText(new Offset(10, 25, comp_matrix, "NE"),
        "r = " + r + "", "", null);
    lang.nextStep();
  }

  /**
   * Aktuallisiert das Info-Textfeld cur_vol
   */
  private void updateCurVol() {
    cur_vol_txt.hide();
    cur_vol_txt = lang.newText(new Offset(0, 0, r_txt, "SW"),
        "currentVolume = " + cur_vol + "", "", null);
    lang.nextStep();
  }

  /**
   * Führt die Info-Textfelder r und cur_vol iniial ein
   */
  private void introduceRandCurVol() {
    r_txt = lang.newText(new Offset(10, 25, comp_matrix, "NE"),
        "r = " + r + "", "", null);

    cur_vol_txt = lang.newText(new Offset(0, 0, r_txt, "SW"),
        "currentVolume = " + cur_vol + "", "", null);
    lang.nextStep();
  }

  /**
   * Veranschaulicht die Auswertung des bool'schen Wertes (i+j == k)
   */
  private void showInfo_back(int i) {
    info_back = lang.newSourceCode(new Offset(10, -25, comp_matrix, "SE"), "",
        null, sc_props);
    info_back.addCodeLine("r - w[i] == results[i+1][currentVolume - v[i]]",
        null, 0, null);
    lang.nextStep();

    info_back.hide();
    info_back = lang.newSourceCode(new Offset(10, -25, comp_matrix, "SE"), "",
        null, sc_props);
    info_back.addCodeLine("" + r
        + " - w[i] == results[i+1][currentVolume - v[i]]", null, 0, null);
    lang.nextStep();

    info_back.hide();
    item_matrix.highlightCell(i, 1, null, duration);
    info_back = lang.newSourceCode(new Offset(10, -25, comp_matrix, "SE"), "",
        null, sc_props);
    info_back.addCodeLine("" + r + " - " + item_matrix.getElement(i, 1)
        + " == results[i+1][currentVolume - v[i]]", null, 0, null);
    lang.nextStep();

    info_back.hide();
    comp_matrix.highlightCell(i + 1, cur_vol - item_matrix.getElement(i, 0),
        null, duration);
    info_back = lang.newSourceCode(new Offset(10, -25, comp_matrix, "SE"), "",
        null, sc_props);
    info_back.addCodeLine(
        ""
            + r
            + " - "
            + item_matrix.getElement(i, 1)
            + " == "
            + comp_matrix.getElement(i + 1,
                cur_vol - item_matrix.getElement(i, 0)), null, 0, null);
    lang.nextStep();

    if (r - item_matrix.getElement(i, 1) == comp_matrix.getElement(i + 1,
        cur_vol - item_matrix.getElement(i, 0))) {
      info_back.hide();
      info_back = lang.newSourceCode(new Offset(10, -25, comp_matrix, "SE"),
          "", null, sc_props);
      info_back.addCodeLine(
          ""
              + (r - item_matrix.getElement(i, 1))
              + " == "
              + comp_matrix.getElement(i + 1,
                  cur_vol - item_matrix.getElement(i, 0)) + " --> true", null,
          0, null);
    } else {
      info_back.hide();
      info_back = lang.newSourceCode(new Offset(10, -25, comp_matrix, "SE"),
          "", null, sc_props);
      info_back.addCodeLine(
          ""
              + (r - item_matrix.getElement(i, 1))
              + " == "
              + comp_matrix.getElement(i + 1,
                  cur_vol - item_matrix.getElement(i, 0)) + " --> false", null,
          0, null);
    }
    lang.nextStep();
  }

  /**
   * @return Ein natürlich-sprachlicher Satz darüber, welche Gegenstände beim
   *         Backtracking ausgewählt wurden.
   */
  private String showSelection() {
    String res = "Einzupacken sind also die Gegenstaende #";
    if (selection.getLength() == 0)
      return "Es haben leider keine Gegenstände in den Rucksack gepasst!";
    else {
      for (int k = 0; k < selection.getLength() - 1; k++) {
        res = res.concat("" + selection.getData(k) + ", #");
      }
      res = res.concat("" + selection.getData(selection.getLength() - 1) + "");
    }

    return res;
  }

  /**
   * Zeigt diverse Erläuterungen während der Animation an
   * 
   * @param disp_nr
   *          die 'ID' der gewünschten Erläuterung
   */
  private void dispExp(int disp_nr) {
    switch (disp_nr) {
      case 0:
        expl = lang.newSourceCode(new Coordinates(100, 200), "", null,
            expl_props);
        expl.addCodeLine(
            "Beim Rucksackproblem geht es darum, eine (indizierte) Menge von N Gegenständen, die alle ein Volumen und einen Wert haben,",
            null, 0, null);
        expl.addCodeLine(
            "in einen Rucksack mit einem bestimmten Fassungsvermögen V zu packen.",
            null, 0, null);
        expl.addCodeLine(
            "Es soll die Teilmenge gefunden werden, die in den Sack passt und maximalen Gesamtwert hat.",
            null, 0, null);
        expl.addCodeLine("", null, 0, null);
        expl.addCodeLine(
            "Der in dieser Animation präsentierte Algorithmus basiert darauf, dass : ",
            null, 0, null);
        expl.addCodeLine("", null, 0, null);
        expl.addCodeLine(
            "a) Die Volumina der Gegenstände und des Rucksacks ganzahlig sind",
            null, 0, null);
        expl.addCodeLine(
            "b) Die Gegenstände aufsteigend nach ihrem Volumen sortiert vorliegen",
            null, 0, null);
        expl.addCodeLine("", null, 0, null);
        expl.addCodeLine(
            "Die N-elementige Liste der Gegenstände liegt als Nx2-Array vor,",
            null, 0, null);
        expl.addCodeLine(
            "wobei v[i] = items[i][0] dem Volumen und w[i] = items[i][1] dem Wert vom Gegenstand mit Index i entspricht",
            null, 0, null);
        break;
      case 1:
        expl = lang.newSourceCode(new Offset(80, 5, item_matrix, "NW"), "",
            null, expl_props);
        expl.addCodeLine(
            "Die (N+1)x(V+1) - Matrix results entspricht den verschieden Lösungen der kleineren Teilprobleme,",
            null, 0, null);
        expl.addCodeLine(
            "die im Rahmen der 'Dynamischen Programmierung' zu einer optimalen Lösung zusammengesetzt werden.",
            null, 0, null);
        break;
      case 2:
        expl = lang.newSourceCode(comp_matrix.getUpperLeft(), "", null,
            expl_props);
        expl.addCodeLine(
            "Jetzt liegt in der Zelle results[i][j] der maximale Gesamtwert, den man aus den Gegenständen i - (N-1)",
            null, 0, null);
        expl.addCodeLine(
            "mit der Rucksack-Kapazität j, erhalten kann. Folglich ist in der Zelle results[0][maxV] die gesuchte Lösung.",
            null, 0, null);
        expl.addCodeLine(
            "Um die Inidzies der eingepackten Gegenstände zu erhalten, gehen wir nu den 'Rückweg' (Backtracking) : ",
            null, 0, null);
        expl.addCodeLine("", null, 0, null);
        expl.addCodeLine(
            "Wir entfernen - beginnend mit dem Kleinsten - einen Gegenstand nach dem anderen,",
            null, 0, null);
        expl.addCodeLine(
            "und wenn der neu erhaltene Wert r mit dem Gesamtwert der nachfolgenden Gegenstände überein stimmt,",
            null, 0, null);
        expl.addCodeLine("wissen wir, dass der Gegenstand im Rucksack war",
            null, 0, null);
        break;
      case 3:
        expl = lang.newSourceCode(new Coordinates(100, 200), "", null,
            expl_props);
        expl.addCodeLine(showSelection(), null, 0, null);
        expl.addCodeLine("", null, 0, null);
        if (selection.getLength() != 0) {
          expl.addCodeLine("Um auf diese optimale Lösung zu kommen wurden "
              + access + " Lese- und " + assign
              + " Schreibzugriffe auf die Matrizen gemacht", null, 0, null);
          expl.addCodeLine(
              "woran man deutlich die 'NP-Härte' des Problems erkennen kann.",
              null, 0, null);
          expl.addCodeLine("", null, 0, null);
        }
        expl.addCodeLine(
            "Vielen Dank fürs Zusehen, ich hoffe diese Animation war aufschlussreich!",
            null, 0, null);
        break;
      default:
        break;
    }
    lang.nextStep();
  }

  /**
   * Ergaenzt Arrays um ein angegebenes Element
   * 
   * @param toBeAppendedTo
   *          das Array an das man was ranhaengen will
   * @param x
   *          das anzuhaengende Datum
   * @return Das um x erweiterte Ursprungsarray
   */
  private int[] appendToArray(int[] toBeAppendedTo, int x) {
    int[] res = new int[toBeAppendedTo.length + 1];
    for (int i = 0; i < toBeAppendedTo.length; i++) {
      res[i] = toBeAppendedTo[i];
    }
    res[toBeAppendedTo.length] = x;
    return res;
  }

  private int[][] fuseArrays(int[] a, int[] b) {
    int[][] res = new int[a.length][2];
    for (int i = 0; i < a.length; i++) {
      res[i][0] = a[i];
      res[i][1] = b[i];
    }
    return res;
  }

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {
    validateInput(arg0, arg1);

    item_props = (MatrixProperties) arg0
        .getPropertiesByName("items_properties");
    comp_props = (MatrixProperties) arg0
        .getPropertiesByName("results_properties");
    sc_props = (SourceCodeProperties) arg0
        .getPropertiesByName("sourcecode_properties");
    expl_props = (SourceCodeProperties) arg0
        .getPropertiesByName("explanation_properties");

    int[][] items = fuseArrays((int[]) arg1.get("item_volumes"),
        (int[]) arg1.get("item_values"));
    int maxVolume = ((Integer) arg1.get("maxVolume")).intValue();

    knapsack(items, maxVolume);
    return lang.toString().replaceAll("refresh", "");
  }

  @Override
  public String getAlgorithmName() {
    return "Rucksackproblem per Dynamischer Programmierung";
  }

  @Override
  public String getAnimationAuthor() {
    return "Erich Wittenbeck";
  }

  @Override
  public String getCodeExample() {
    return "int knapsack(int[][] items, int V){\n"
        + "\tint[][] results = new int[N + 1][V + 1]\n"
        + "\tfor(int i = N; i >= 0; i--) \n"
        + "\t\tfor(int j = v[i]; j <= V; j++)\n"
        + "\t\t\tresults[i][j] = max(w[i] + results[i+1][j - v[i]], results[i+1][j])\n"
        + "\treturn results[0][V]\n" + "}";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "Animation der L&ouml;sung des Rucksackproblems mittels Dynamischer Programmierung<br>"
        + "<br>"
        + "Im folgenden sind 2 N-lange Listen jeweils mit den Volumina und den Werten der N Gegenst&auml;nde, die man einpacken will, anzugeben<br>"
        + "sowie das maximale Fassungsverm&ouml;gen des zu f&uuml;llenden Rucksacks.<br>"
        + "<br>"
        + "Dar&uuml;ber hinaus noch Einstellungen f&uuml;r die optische Darstellung der Gegenstandsliste, der Berechnungs-/Ergebnissmatrix,<br>"
        + "des eingeblendeten Quellcodes und der diversen Erl&auml;uterungen, w&auml;hrend der Animation.";
  }

  @Override
  public String getFileExtension() {
    return ".asu";
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  @Override
  public String getName() {
    return "Rucksack-Problem, dynamisch gelöst";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) throws IllegalArgumentException {
    if (((int[]) arg1.get("item_volumes")).length != ((int[]) arg1
        .get("item_values")).length)
      throw new IllegalArgumentException(
          "Die Listen der Volumina und Werte sind unterschiedlich gro&szlig;!");

    for (int i = 0; i < ((int[]) arg1.get("item_volumes")).length; i++)
      if (((int[]) arg1.get("item_volumes"))[i] < 0
          || ((int[]) arg1.get("item_values"))[i] < 0)
        throw new IllegalArgumentException(
            "Negative Werte sind weder für die Gewichte noch für die Wertigkeiten zul&auml;ssig!");
    return true;
  }
}
