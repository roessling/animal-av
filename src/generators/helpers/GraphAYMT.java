package generators.helpers;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.util.TicksTiming;

/**
 * @author Atilla Yalcin(1240034) , Mustafa Tuerkmen(1094578)
 * @version 1.0 2008-07-28
 * 
 */

public class GraphAYMT {
  Language     l;
  KnotenAYMT[] n;
  KanteAYMT[]  line;
  int[]        grapharray;
  int          traverse;

  /**
   * Graph
   * 
   * @param array
   *          the IntArray to be sorted
   * @param lang the Language object used for creating visualizations
   */
  public GraphAYMT(int[] array, Language lang) {
    // wird zur späteren Traversierung benötigt
    traverse = 0;
    // language object wird übernommen
    l = lang;

    // es werden für das Array alle möglichen Knotenpositionen berechnet
    int power = 1;
    for (int i = 0; i < array.length; i++) {
      power = power * 2;
    }
    grapharray = new int[power - 1];
    for (int i = 0; i < power - 1; i++) {
      grapharray[i] = -1;
    }

    n = new KnotenAYMT[power - 1];
    line = new KanteAYMT[n.length + 1];
    int stufe = 0;
    int pot = 1;
    int summe = 0;
    for (int i = 0; i < power - 1; i++) {
      if ((summe + pot) == i) {
        summe = (summe + pot);
        stufe++;
        pot = 2 * pot;
      }

      n[i] = new KnotenAYMT(500 + (i - summe + 1) * 400 / (pot + 1),
          100 + 50 * stufe);
    }
  }

  /**
   * Einf&auml;geopertion in den Graphen
   * 
   */
  public void einfuegen(int i, SourceCode codeSupport) {

    boolean gefunden = false;
    int pos = 0;
    int vorgaenger = -1;

    // wurzel nicht leer
    if (grapharray[0] >= 0) {

      while (gefunden == false) {
        // falls aktuelle position nicht leer
        if (grapharray[pos] >= 0) {
          codeSupport.highlight(12, 0, false);
          l.nextStep();
          codeSupport.unhighlight(12, 0, false);
          n[pos].hightlight();
          vorgaenger = pos;

          // auswahl der richten Unterknotens
          if (grapharray[pos] > i) {

            pos = 2 * pos + 1;
            codeSupport.highlight(13, 0, false);
            l.nextStep();
            codeSupport.unhighlight(13, 0, false);

          } else {
            pos = 2 * pos + 2;
            codeSupport.highlight(14, 0, false);
            l.nextStep();
            codeSupport.unhighlight(14, 0, false);
          }
          n[vorgaenger].unhightlight();
        } else {
          gefunden = true;
          n[pos].einfuegen(i, l);
          if (vorgaenger >= 0) {
            line[pos] = new KanteAYMT(n[vorgaenger], n[pos], l);
          }
          grapharray[pos] = i;
          codeSupport.highlight(15, 0, false);
          l.nextStep();
          codeSupport.unhighlight(15, 0, false);
        }
      }
    } else {
      n[pos].einfuegen(i, l);
      grapharray[pos] = i;
      codeSupport.highlight(15, 0, false);
      l.nextStep();
      codeSupport.unhighlight(15, 0, false);
    }
  }

  /**
   * preorder Traversierung des Graphen und einlesen in das Array
   * 
   */
  public void traversiereBauminArray(IntArray ia, ArrayMarker k, int pos,
      SourceCode codeSupport) {

    codeSupport.highlight(19, 0, false);
    l.nextStep();
    codeSupport.unhighlight(19, 0, false);
    // linker Teil
    if ((2 * pos + 1) < grapharray.length && grapharray[2 * pos + 1] >= 0)
      traversiereBauminArray(ia, k, 2 * pos + 1, codeSupport);
    codeSupport.highlight(20, 0, false);
    l.nextStep();
    codeSupport.unhighlight(20, 0, false);

    // knoten selbst
    ia.put(traverse, grapharray[pos], null, null);
    codeSupport.highlight(21, 0, false);
    l.nextStep();
    codeSupport.unhighlight(21, 0, false);
    traverse++;
    n[pos].markout();

    codeSupport.highlight(22, 0, false);
    l.nextStep();
    codeSupport.unhighlight(22, 0, false);
    if (k.getPosition() < ia.getLength() - 1)
      k.increment(null, new TicksTiming(15));

    // rechter Teil
    if ((2 * pos + 2) < grapharray.length && grapharray[2 * pos + 2] >= 0)
      traversiereBauminArray(ia, k, 2 * pos + 2, codeSupport);

  }

  public void hightlight(int i) {
    n[i].hightlight();
  }

  public void unhightlight(int i) {
    n[i].unhightlight();
  }

  public void markout(int i) {
    n[i].markout();
  }

}
