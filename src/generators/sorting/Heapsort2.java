package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.TicksTiming;

public class Heapsort2 implements Generator {

  private static Language lang;

  private final String    SOURCE_CODE = "public static void sort(int[] a0) \n"
                                          + "{\n"
                                          + "\ta=a0;\n"
                                          + "\tn=a.length;\n"
                                          + "\theapsort();\n"
                                          + "}\n\n"
                                          + "private static void heapsort()\n"
                                          + "{\n"
                                          + "\tbuildheap();\n"
                                          + "\twhile (n>1)\n"
                                          + "\t{\n"
                                          + "\t\tn--;\n"
                                          + "\t\texchange (0, n);\n"
                                          + "\t\tdownheap (0);\n"
                                          + "\t}\n"
                                          + "}\n\n"
                                          + "private static void buildheap()\n"
                                          + "{\n"
                                          + "\tfor (int v=n/2-1; v>=0; v--)\n"
                                          + "\t\tdownheap (v);\n"
                                          + "}\n\n"
                                          + "private static void downheap(int v)\n"
                                          + "{\n"
                                          + "\tint w=2*v+1;    // first descendant of v\n"
                                          + "\twhile (w<n)\n"
                                          + "\t{\n"
                                          + "\t\tif (w+1<n)    // is there a second descendant?\n"
                                          + "\t\t\tif (a[w+1]>a[w]) w++;  // w is the descendant of v with maximum label\n"
                                          + "\t\tif (a[v]>=a[w]) return;  // v has heap property\n"
                                          + "\t\t// otherwise\n"
                                          + "\t\texchange(v, w);  // exchange labels of v and w\n"
                                          + "\t\tv=w;        // continue\n"
                                          + "\t\tw=2*v+1;\n"
                                          + "\t}\n"
                                          + "}\n\n"
                                          + "private static void exchange(int i, int j)\n"
                                          + "{\n" + "\tint t=a[i];\n"
                                          + "\ta[i]=a[j];\n" + "\ta[j]=t;\n"
                                          + "}\n\n";

  private final String    DESCRIPTION = "Heapsort (method) is a comparison-based sorting algorithm, and "
                                          + "is part of the selection sort family. Although somewhat slower in practice on most machines "
                                          + "than a good implementation of quicksort, it has the advantage of a worst-case O(n log n) "
                                          + "runtime. Heapsort inserts the input list elements into a heap data structure. The largest "
                                          + "value (in a max-heap) or the smallest value (in a min-heap) are extracted until none remain, "
                                          + "the values having been extracted in sorted order. The heap's invariant is preserved after "
                                          + "each extraction, so the only cost is that of extraction During extraction, the only space "
                                          + "required is that needed to store the heap. In order to achieve constant space overhead, the "
                                          + "heap is stored in the part of the input array that has not yet been sorted. (The structure of "
                                          + "this heap is described at Binary heap: Heap implementation.) Heapsort uses two heap "
                                          + "operations: insertion and root deletion. Each extraction places an element in the last "
                                          + "empty location of the array. The remaining prefix of the array stores the unsorted elements. (Source: Wikipedia)";

  private final String    desc1       = "Heapsort (method) is a comparison-based sorting algorithm, and is part of the";
  private final String    desc2       = "selection sort family. Although somewhat slower in practice on most machines than";
  private final String    desc3       = "a good implementation of quicksort, it has the advantage of a worst-case O(n log n)";
  private final String    desc4       = "runtime. Heapsort inserts the input list elements into a heap data structure. The largest";
  private final String    desc5       = "value (in a max-heap) or the smallest value (in a min-heap) are extracted until none remain,";
  private final String    desc6       = "the values having been extracted in sorted order. The heap's invariant is preserved after";
  private final String    desc7       = "each extraction, so the only cost is that of extraction During extraction, the only space";
  private final String    desc8       = "required is that needed to store the heap. In order to achieve constant space overhead, the";
  private final String    desc9       = "heap is stored in the part of the input array that has not yet been sorted. Heapsort uses two";
  private final String    desc10      = "heap operations: insertion and root deletion. Each extraction places an element in the last";
  private final String    desc11      = "empty location of the array. The remaining prefix of the array stores the unsorted elements.";
  private final String    desc12      = "";
  private final String    desc13      = "                                                    Source: Wikipedia";

  private static SourceCode scSort, scBuildheap, scDownheap, scExchange;

  private static IntArray   intArray;

  private static int        n;

  private static int        arrayN;

  private static Text       nIndex, vIndex, wIndex, vLocalIndex;

  private static ArrayMarker nMarker, vMarker, wMarker, vLocalMarker;

  private static Text[]      tree;

  private static int         originalXcoordTree = 500;

  private static int         originalYcoordTree = 420;

  /**
   * Class constructor. Takes a {@link Language} object as input in order to
   * create the different <br>
   * primitives in the animation.
   * 
   * @param lang
   */
  private Heapsort2(Language lang) {
    Heapsort2.lang = lang;
  }

  public Heapsort2() {
    lang = new AnimalScript("Caesar-Chiffre",
        "Henrik Schröder, Nedislav Nedyalkov, Latinka Pavlova", 1024, 768);
  }

  @Override
  public String generate(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> arg1) {

    // create the new language
    Language lang = new AnimalScript("Heapsort",
        "Henrik Schröder, Nedislav Nedyalkov, Latinka Pavlova", 1024, 768);
    new Heapsort2(lang);

    lang.setStepMode(true);

    // create the heapsort header with the rectangle around it.
    RectProperties rp = new RectProperties();
    rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
    rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    lang.newRect(new Coordinates(120, 5), new Coordinates(260, 36),
        "HeaderRect", null, rp);
    TextProperties tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 20));
    lang.newText(new Coordinates(140, 10), "Heapsort", "Header", null, tp);
    lang.nextStep();

    RectProperties rpd = new RectProperties();
    rpd.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.CYAN);
    Rect r1 = lang.newRect(new Coordinates(40, 140), new Coordinates(552, 372),
        "descr", null);
    Rect r2 = lang.newRect(new Coordinates(42, 142), new Coordinates(550, 370),
        "descr", null);
    Rect r3 = lang.newRect(new Coordinates(41, 141), new Coordinates(551, 371),
        "descr", null, rpd);

    // create the description object
    SourceCodeProperties scProperties = new SourceCodeProperties();
    scProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    SourceCode s = lang.newSourceCode(new Coordinates(50, 150), "desc", null);
    s.addCodeLine(desc1, null, 0, new TicksTiming(25));
    s.addCodeLine(desc2, null, 0, new TicksTiming(20));
    s.addCodeLine(desc3, null, 0, new TicksTiming(15));
    s.addCodeLine(desc4, null, 0, new TicksTiming(10));
    s.addCodeLine(desc5, null, 0, new TicksTiming(5));
    s.addCodeLine(desc6, null, 0, new TicksTiming(0));
    s.addCodeLine(desc7, null, 0, new TicksTiming(5));
    s.addCodeLine(desc8, null, 0, new TicksTiming(10));
    s.addCodeLine(desc9, null, 0, new TicksTiming(15));
    s.addCodeLine(desc10, null, 0, new TicksTiming(20));
    s.addCodeLine(desc11, null, 0, new TicksTiming(25));
    s.addCodeLine(desc12, null, 0, new TicksTiming(30));
    s.addCodeLine(desc13, null, 0, new TicksTiming(35));

    lang.nextStep();

    // hide the description object
    s.hide(new TicksTiming(500));
    r1.hide(new TicksTiming(500));
    r2.hide(new TicksTiming(500));
    r3.hide(new TicksTiming(500));

    lang.nextStep();
    // create the source code objects and put them into the animation
    lang.newRect(new Coordinates(60, 125), new Coordinates(300, 125), "line",
        null, rp);
    lang.newRect(new Coordinates(390, 5), new Coordinates(390, 400),
        "vertLine", null, rp);

    scSort = lang.newSourceCode(new Coordinates(80, 120), "sourceCode", null,
        (SourceCodeProperties) arg0.getPropertiesByName("main_sourceCode"));
    scSort.addCodeLine("private static void sort()", null, 0, null);
    scSort.addCodeLine("{", null, 0, null);
    scSort.addCodeLine("    buildheap();", null, 0, null);
    scSort.addCodeLine("    while (n>1)", null, 0, null);
    scSort.addCodeLine("    {", null, 0, null);
    scSort.addCodeLine("        n--;", null, 0, null);
    scSort.addCodeLine("        exchange (0, n);", null, 0, null);
    scSort.addCodeLine("        downheap (0);", null, 0, null);
    scSort.addCodeLine("    }", null, 0, null);
    scSort.addCodeLine("}", null, 0, null);

    scBuildheap = lang.newSourceCode(new Coordinates(400, 0), "sourceCode2",
        null,
        (SourceCodeProperties) arg0.getPropertiesByName("methods_sourceCode"));
    scBuildheap.addCodeLine(" -> private static void buildheap() {", null, 0,
        null);
    scBuildheap.addCodeLine("        for (int v=n/2-1; v>=0; v--)", null, 0,
        null);
    scBuildheap.addCodeLine("            downheap (v);", null, 0, null);
    scBuildheap.addCodeLine("    }", null, 0, null);

    rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    rp.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(224, 224, 255));
    lang.newRect(new Coordinates(835, 30), new Coordinates(950, 55), "vrect",
        null, rp);
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    vIndex = lang.newText(new Coordinates(840, 30), "current v = _", "v", null,
        tp);

    scDownheap = lang.newSourceCode(new Coordinates(400, 75), "sourceCode3",
        null,
        (SourceCodeProperties) arg0.getPropertiesByName("methods_sourceCode"));
    scDownheap.addCodeLine(" -> private static void downheap(int v') {", null,
        0, null);
    scDownheap.addCodeLine(
        "        int w=2*v'+1;    // first descendant of v'", null, 0, null);
    scDownheap.addCodeLine("        while (w<n)", null, 0, null);
    scDownheap.addCodeLine("        {", null, 0, null);
    scDownheap.addCodeLine(
        "            if (w+1<n)    // is there a second descendant?", null, 0,
        null);
    scDownheap
        .addCodeLine(
            "                if (a[w+1] > a[w]) w++;              // w-the descendant of v' with max label",
            null, 0, null);
    scDownheap.addCodeLine("            if (a[v'] >= a[w])", null, 0, null);
    scDownheap.addCodeLine(
        "                return;      // v' has heap property", null, 0, null);
    scDownheap.addCodeLine(
        "            exchange(v', w);  // exchange labels of v' and w", null,
        0, null);
    scDownheap.addCodeLine("            v'=w;        // continue", null, 0,
        null);
    scDownheap.addCodeLine("            w=2*v'+1;", null, 0, null);
    scDownheap.addCodeLine("        }", null, 0, null);
    scDownheap.addCodeLine("    }", null, 0, null);

    lang.newRect(new Coordinates(835, 110), new Coordinates(960, 150), "vrect",
        null, rp);
    wIndex = lang.newText(new Coordinates(840, 110), "current w = _", "w",
        null, tp);
    vLocalIndex = lang.newText(new Coordinates(840, 130), "v' = _", "vLocal",
        null, tp);

    scExchange = lang.newSourceCode(new Coordinates(400, 310), "sourceCode4",
        null,
        (SourceCodeProperties) arg0.getPropertiesByName("methods_sourceCode"));
    scExchange.addCodeLine(" -> private void exchange(int i, int j) {", null,
        0, null);
    scExchange.addCodeLine("       int t=a[i];    // first descendant of v",
        null, 0, null);
    scExchange.addCodeLine("       a[i] = a[j];", null, 0, null);
    scExchange.addCodeLine("       a[j] = t;", null, 0, null);
    scExchange.addCodeLine("    }", null, 0, null);

    int[] array = (int[]) arg1
        .get("Unsorted_Numbers(maxValue=100,maxCount=15)");

    ArrayList<Integer> alNumbers = new ArrayList<Integer>(array.length);
    for (int i = 0; i < array.length; i++) {
      if (array[i] < 100)
        alNumbers.add(array[i]);
      if (alNumbers.size() >= 15)
        break;
    }

    array = new int[alNumbers.size()];
    for (int i = 0; i < alNumbers.size(); i++) {
      array[i] = alNumbers.get(i);
    }
    heapsort(array, (ArrayProperties) arg0.getPropertiesByName("array"));

    String st = lang.toString();

    return st;
  }

  private static void heapsort(int[] array, ArrayProperties arProp) {

    intArray = lang.newIntArray(new Coordinates(80, 410), array, "SortArray",
        null, arProp);
    n = array.length;
    arrayN = n - 1;

    // the current n in the view
    RectProperties rp = new RectProperties();
    rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    rp.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(224, 224, 255));
    rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    lang.newRect(new Coordinates(265, 185), new Coordinates(370, 210),
        "curnrect", null, rp);
    TextProperties tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    nIndex = lang.newText(new Coordinates(270, 185), "current n = " + n,
        "text1", null, tp);

    // ************************ The Tree for the Heap *********************** //
    tree = new Text[n];

    // create the nodes (with text) //
    for (int i = 0; i < tree.length; i++) {
      double p = 0;
      if (i == 0) {
        tree[i] = lang.newText(new Coordinates(originalXcoordTree,
            originalYcoordTree), new Integer(intArray.getData(i)).toString(),
            new Integer(intArray.getData(i)).toString(), null);
      } else if (i == 1 || i == 2) {
        p = 1.5;
        p = p - i;
        double x = originalXcoordTree - 160 * p;
        tree[i] = lang.newText(
            new Coordinates((int) x, originalYcoordTree + 30), new Integer(
                intArray.getData(i)).toString(),
            new Integer(intArray.getData(i)).toString(), null);
      } else if (i >= 3 && i <= 6) {
        p = 4.5;
        p = p - i;
        double x = originalXcoordTree - 80 * p;
        tree[i] = lang.newText(
            new Coordinates((int) x, originalYcoordTree + 60), new Integer(
                intArray.getData(i)).toString(),
            new Integer(intArray.getData(i)).toString(), null);
      } else if (i >= 7 && i <= 14) {
        p = 10.5;
        p = p - i;
        double x = originalXcoordTree - 40 * p;
        tree[i] = lang.newText(
            new Coordinates((int) x, originalYcoordTree + 90), new Integer(
                intArray.getData(i)).toString(),
            new Integer(intArray.getData(i)).toString(), null);
      } else if (i >= 15 && i <= 30) {
        p = 22.5;
        p = p - i;
        double x = originalXcoordTree - 20 * p;
        tree[i] = lang.newText(new Coordinates((int) x,
            originalYcoordTree + 120), new Integer(intArray.getData(i))
            .toString(), new Integer(intArray.getData(i)).toString(), null);
      }
    }

    // create the edges with polylines
    PolylineProperties pp = new PolylineProperties();
    pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);

    for (int i = 0; i < tree.length; i++) {
      if (2 * i + 1 < tree.length) {
        lang.newPolyline(
            new Node[] { tree[i].getUpperLeft(), tree[2 * i + 1].getUpperLeft() },
            "line1: " + i, null, pp);
      }
      if (2 * i + 2 < tree.length) {
        lang.newPolyline(
            new Node[] { tree[i].getUpperLeft(), tree[2 * i + 2].getUpperLeft() },
            "line2: " + i, null, pp);
      }
    }
    // ***************************************************************************************************
    // //

    lang.nextStep();

    // create the marker and hide them initially
    ArrayMarkerProperties amn = new ArrayMarkerProperties();
    amn.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    amn.set(AnimationPropertiesKeys.LABEL_PROPERTY, "n");
    nMarker = lang.newArrayMarker(intArray, n - 1, "n", null, amn);
    nMarker.hide();
    amn.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    amn.set(AnimationPropertiesKeys.LABEL_PROPERTY, "v");
    vMarker = lang.newArrayMarker(intArray, n / 2 - 1, "vMarker", null, amn);
    vMarker.hide();
    amn.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    amn.set(AnimationPropertiesKeys.LABEL_PROPERTY, "w");
    wMarker = lang.newArrayMarker(intArray, n / 2 - 1, "wMarker", null, amn);
    wMarker.hide();
    amn.set(AnimationPropertiesKeys.LABEL_PROPERTY, "v'");
    vLocalMarker = lang.newArrayMarker(intArray, n / 2 - 1, "vLocalMarker",
        null, amn);
    vLocalMarker.hide();

    // sort
    sort();

    lang.setStepMode(true);
    scSort.unhighlight(0);
    scSort.unhighlight(1);
    scSort.unhighlight(9);
    intArray.highlightCell(0, null, null);

    rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.FALSE);
    lang.newRect(new Coordinates(originalXcoordTree - 5, originalYcoordTree),
        new Coordinates(originalXcoordTree + 17, originalYcoordTree + 17),
        "root", null, rp);
    tree[0]
        .setFont(new Font("Serif", Font.BOLD, 13), null, new TicksTiming(50));

    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.BOLD, 16));
    tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

    lang.newText(new Coordinates(80, 370), "SORTED", "sorted", null, tp);
    lang.nextStep();
    lang.setStepMode(false);
  }

  private static void sort() {

    // highlight code
    scSort.highlight(0);
    scSort.highlight(1);
    scSort.highlight(9);
    scSort.highlight(2);
    scBuildheap.highlight(0);
    lang.nextStep();
    lang.setStepMode(false);

    // build the heap
    buildheap();

    // un-/ highlight code
    lang.setStepMode(true);
    scBuildheap.unhighlight(0);
    scSort.unhighlight(2);
    scSort.highlight(3);
    scSort.highlight(4);
    scSort.highlight(8);
    lang.nextStep();
    lang.setStepMode(false);

    while (n > 1) {
      lang.setStepMode(true);
      scSort.highlight(5);

      n--; // decrease the length of the array which remains to be sorted
      nIndex.setText("current n = " + n, null, null);

      if (arrayN == n) {
        nMarker.show();
      } else {
        nMarker.move(n, null, new TicksTiming(50));
      }
      lang.nextStep();

      scSort.unhighlight(5);
      scSort.highlight(6);
      scExchange.highlight(0);
      scExchange.highlight(1);
      scExchange.highlight(2);
      scExchange.highlight(3);
      scExchange.highlight(4);// scExchange.highlight(5);
      lang.nextStep();

      // exchange elements in the array and the heap
      exchange(0, n);

      // unhighlight code
      scSort.unhighlight(6);
      scExchange.unhighlight(0);
      scExchange.unhighlight(1);
      scExchange.unhighlight(2);
      scExchange.unhighlight(3);
      scExchange.unhighlight(4);// scExchange.unhighlight(5);

      intArray.highlightCell(n, null, new TicksTiming(50));
      tree[n].setFont(new Font("Serif", Font.BOLD, 13), null, new TicksTiming(
          50));

      // ***************** create a rectangle around a sorted element in the
      // heap *************** //
      double p = 0;
      double x = 0;
      double y = 0;
      if (n == 0) {
        x = originalXcoordTree;
        y = originalYcoordTree;
      } else if (n == 1 || n == 2) {
        p = 1.5;
        p = p - n;
        x = originalXcoordTree - 160 * p;
        y = originalYcoordTree + 30;
      } else if (n >= 3 && n <= 6) {
        p = 4.5;
        p = p - n;
        x = originalXcoordTree - 80 * p;
        y = originalYcoordTree + 60;
      } else if (n >= 7 && n <= 14) {
        p = 10.5;
        p = p - n;
        x = originalXcoordTree - 40 * p;
        y = originalYcoordTree + 90;
      } else if (n >= 15 && n <= 30) {
        p = 22.5;
        p = p - n;
        x = originalXcoordTree - 20 * p;
        y = originalYcoordTree + 120;
      }
      RectProperties rp = new RectProperties();
      rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
      lang.newRect(new Coordinates((int) x - 5, (int) y), new Coordinates(
          (int) x + 17, (int) y + 17), "rec", null, rp);
      lang.nextStep();
      // ********************************************************************************************
      // //

      // un-/ highlight code
      scSort.highlight(7);
      scDownheap.highlight(0);
      scDownheap.highlight(12);
      lang.nextStep();
      lang.setStepMode(false);

      // move the element down the heap if necessary
      downheap(0);

      // un-/ highlight code
      lang.setStepMode(true);
      scDownheap.unhighlight(0);
      scDownheap.unhighlight(12);
      scSort.unhighlight(7);
      lang.nextStep();
      lang.setStepMode(false);
    }
    lang.setStepMode(true);
    scSort.unhighlight(3);
    scSort.unhighlight(4);
    scSort.unhighlight(8);
    nMarker.hide();
    lang.nextStep();
    lang.setStepMode(false);
  }

  private static void buildheap() {
    lang.setStepMode(true);
    scBuildheap.highlight(0);
    scBuildheap.highlight(1);
    scBuildheap.highlight(3);// scBuildheap.highlight(4);
    vMarker.show();
    lang.nextStep();
    lang.setStepMode(false);
    for (int v = n / 2 - 1; v >= 0; v--) {
      lang.setStepMode(true);
      scBuildheap.highlight(2);
      scDownheap.highlight(0);
      scDownheap.highlight(12);
      vIndex.setText("current v = " + v, null, null);
      vMarker.move(v, null, new TicksTiming(50));
      lang.nextStep();
      lang.setStepMode(false);
      downheap(v);
    }
    lang.setStepMode(true);
    vIndex.setText("v = _", null, null);
    scDownheap.unhighlight(0);
    scDownheap.unhighlight(12);
    scBuildheap.unhighlight(0);
    scBuildheap.unhighlight(1);
    scBuildheap.unhighlight(2);
    scBuildheap.unhighlight(3);// scBuildheap.unhighlight(4);
    vMarker.hide();
    lang.nextStep();
    lang.setStepMode(false);
  }

  private static void downheap(int theV) {
    int v = theV;
    lang.setStepMode(true);

    scDownheap.highlight(1);
    int w = 2 * v + 1; // first descendant of v

    if (!(w >= n))
      wMarker.move(w, null, null);

    wIndex.setText("current w = " + w, null, null);
    lang.nextStep();
    vLocalIndex.setText("v' = " + v, null, null);
    vLocalMarker.move(v, null, null);
    scDownheap.unhighlight(1);
    scDownheap.highlight(2);
    scDownheap.highlight(3);
    scDownheap.highlight(11);
    vLocalMarker.show();
    wMarker.show();
    lang.nextStep();

    while (w < n) {
      lang.setStepMode(false);
      scDownheap.highlight(4);
      if (w + 1 < n) { // is there a second descendant?
        scDownheap.highlight(5);
        if (intArray.getData(w + 1) > intArray.getData(w)) {
          w++; // w is the descendant of v with maximum label
          wIndex.setText("current w = " + w, null, null);
          wMarker.move(w, null, new TicksTiming(50));

        }
      }

      lang.setStepMode(true);
      scDownheap.unhighlight(4);
      scDownheap.unhighlight(5);
      scDownheap.highlight(6);
      lang.nextStep();

      if (intArray.getData(v) >= intArray.getData(w)) {
        scDownheap.highlight(6);
        scDownheap.highlight(7);
        lang.nextStep();
        scDownheap.unhighlight(2);
        scDownheap.unhighlight(3);
        scDownheap.unhighlight(6);
        scDownheap.unhighlight(7);
        scDownheap.unhighlight(11);
        wMarker.hide();
        vLocalMarker.hide();
        lang.nextStep();
        return; // v has heap property
      }
      // otherwise

      scDownheap.unhighlight(6);
      scDownheap.highlight(8);
      scExchange.highlight(0);
      scExchange.highlight(1);
      scExchange.highlight(2);
      scExchange.highlight(3);
      scExchange.highlight(4);// scExchange.highlight(5);
      exchange(v, w); // exchange labels of v and w
      scDownheap.unhighlight(8);
      scExchange.unhighlight(0);
      scExchange.unhighlight(1);
      scExchange.unhighlight(2);
      scExchange.unhighlight(3);
      scExchange.unhighlight(4);// scExchange.unhighlight(5);
      scDownheap.highlight(9);
      lang.nextStep();
      v = w; // continue
      vLocalMarker.move(v, null, new TicksTiming(50));
      vLocalIndex.setText("v' = " + v, null, null);
      lang.nextStep();
      scDownheap.unhighlight(9);
      scDownheap.highlight(10);
      w = 2 * v + 1;
      wIndex.setText("current w = " + w, null, null);
      if (!(w >= n)) {
        wMarker.move(w, null, new TicksTiming(50));
      }
      lang.nextStep();

      scDownheap.highlight(10);
      scDownheap.unhighlight(10);
      lang.nextStep();
    }
    wIndex.setText("current w = _", null, null);
    vLocalIndex.setText("v' = _", null, null);
    scDownheap.unhighlight(2);
    scDownheap.unhighlight(3);
    scDownheap.unhighlight(11);
    wMarker.hide();
    vLocalMarker.hide();
    lang.nextStep();
    lang.setStepMode(false);
  }

  private static void exchange(int i, int j) {
    intArray.highlightElem(i, null, null);
    tree[i].setFont(new Font("SansSerif", Font.BOLD, 13), null, null);
    tree[j].setFont(new Font("SansSerif", Font.BOLD, 13), null, null);

    intArray.highlightElem(j, null, null);

    PolylineProperties pp = new PolylineProperties();
    pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    pp.set(AnimationPropertiesKeys.BWARROW_PROPERTY, Boolean.TRUE);
    Polyline p = lang.newPolyline(
        new Node[] { tree[i].getUpperLeft(), tree[j].getUpperLeft() }, "linie",
        null, pp);
    p.hide(new TicksTiming(350));
    tree[i].setText(new Integer(intArray.getData(j)).toString(),
        new TicksTiming(100), new TicksTiming(200));
    tree[j].setText(new Integer(intArray.getData(i)).toString(),
        new TicksTiming(100), new TicksTiming(200));
    intArray.swap(i, j, new TicksTiming(100), new TicksTiming(200));
    tree[i].setFont(new Font("Monospaced", Font.PLAIN, 12),
        new TicksTiming(150), null);
    tree[j].setFont(new Font("Monospaced", Font.PLAIN, 12),
        new TicksTiming(150), null);
    intArray.unhighlightElem(j, new TicksTiming(100), null);
    intArray.unhighlightElem(i, new TicksTiming(100), null);
    lang.nextStep();
  }

  @Override
  public String getAlgorithmName() {
    return "Heap Sort";
  }

  @Override
  public String getAnimationAuthor() {
    return "Henrik Schröder, Nedislav Nedyalkov, Latinka Pavlova";
  }

  @Override
  public String getCodeExample() {
    return SOURCE_CODE;
  }

  @Override
  public Locale getContentLocale() {
    return Locale.US;
  }

  @Override
  public String getDescription() {
    return DESCRIPTION;
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  @Override
  public String getName() {
    return "Heapsort";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public void init() {
    // TODO Auto-generated method stub

  }

}