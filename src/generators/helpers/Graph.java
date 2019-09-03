package generators.helpers;

import algoanim.primitives.generators.Language;

public class Graph {
  Language l;
  int[]    pos;
  Knoten[] n;
  Kante[]  line;

  public Graph(int[] array, Language lang) {
    l = lang;
    n = new Knoten[array.length];
    for (int i = 0; i < array.length; i++) {
      n[i] = new Knoten(array[i], 400 + i * 50, 450, l);
    }

    line = new Kante[n.length + 1];

    // int stufe=0;
    int pot = 1;
    int summe = 0;
    pos = new int[n.length];
    for (int i = 0; i < pos.length; i++) {
      if ((summe + pot) == i) {
        summe = (summe + pot);
//        stufe++;
        pot = 2 * pot;
      }

      pos[i] = (i - summe + 1) * 400 / (pot + 1);
      // System.out.println(pos[i]+" summe "+ summe+" pot "+pot);
    }

  }

  public void moveStart(int i) {
    n[i].moveBy(0, -pos[i]);
    if ((2 * (i + 1) - 1) < n.length) {
      line[2 * (i + 1) - 1] = new Kante(n[i], n[2 * (i + 1) - 1], l);
    }
    if ((2 * (i + 1)) < n.length) {
      line[2 * (i + 1)] = new Kante(n[i], n[2 * (i + 1)], l);
    }

  }

  public void swap(int i, int j) {
    int xdiff = n[i].co.getX() - n[j].co.getX();
    int ydiff = n[i].co.getY() - n[j].co.getY();
    n[i].moveBy(-xdiff, -ydiff);
    n[j].moveBy(xdiff, ydiff);
    Knoten z = n[i];
    n[i] = n[j];
    n[j] = z;
    // System.out.println((n[i].co.getX()-n[j].co.getX())
    // +";"+(n[i].co.getY()-n[j].co.getY()));
    // System.out.println(xdiff+" "+ydiff);
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
