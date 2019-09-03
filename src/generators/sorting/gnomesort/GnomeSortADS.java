package generators.sorting.gnomesort;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class GnomeSortADS implements Generator {
  public Language              lang;
  public ArrayProperties       arrayDesign   = new ArrayProperties();
  public ArrayMarkerProperties pointerDesign = new ArrayMarkerProperties();
  public SourceCodeProperties  scProps       = new SourceCodeProperties();
  public ArrayProperties       arrayDesign2  = new ArrayProperties();
  public RectProperties        rp            = new RectProperties();
  public int                   language;

  public GnomeSortADS() {
    Language l = new AnimalScript("niko tijani", "arif", 640, 480);
    lang = l;
  }

  public void execute(int a[]) {
    lang.setStepMode(true);

    // showing the main text
    // ********************************************************
    TextProperties textProp = new TextProperties();
    textProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Bold",
        Font.BOLD, 40));
    lang
        .newText(new Coordinates(500, 30), "Gnome Sort", "text", null, textProp);
    lang.nextStep();

    if (language % 2 == 0) {
      showEnglishText();
    }

    if (language % 2 == 1) {
      showGermanText();
    }

    // showing the source code
    SourceCode sc = showSourceCode();
    lang.nextStep();

    int i = 0;
    sc.highlight(0);

    // showing the array
    IntArray sortArray = lang.newIntArray(new Coordinates(100, 90), a,
        "mainArray", null, arrayDesign);
    IntArray oldArray = lang.newIntArray(new Coordinates(100, 90), a,
        "mainArray", null, arrayDesign);
    oldArray.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE,
        new TicksTiming(10), new TicksTiming(10));
    oldArray.hide();

    lang.nextStep();
    sc.unhighlight(0);
    sc.highlight(1);

    // adding a pointer
    ArrayMarker marker = lang.newArrayMarker(sortArray, 0, "i", null,
        pointerDesign); // creating the pointer

    showString("i = 0", 1);
    sc.unhighlight(1);
    sc.highlight(2);

    if (language % 2 == 0) {
      showString("i = 0,  ar.length = " + sortArray.getLength() + ", (0 < "
          + sortArray.getLength() + ") =>  TRUE!", 2);
    }
    if (language % 2 == 1) {
      showString("i = 0,  ar.length = " + sortArray.getLength() + ", (0 < "
          + sortArray.getLength() + ") =>  WAHR!", 2);
    }

    while (i < sortArray.getLength()) {
      sortArray.unhighlightCell(0, sortArray.getLength() - 1, null, null);
      sc.unhighlight(2);
      sc.highlight(3);
      sortArray.unhighlightCell(0, sortArray.getLength(), null, null);
      sortArray.highlightCell(i - 1, i, null, null);
      sortArray.highlightCell(i, i, null, null);

      if (i == 0) {
        if (language % 2 == 0) {
          showString("this time i is equals 0", 3);
        }

        if (language % 2 == 1) {
          showString("jetzt ist i = 0", 3);
        }
      }

      else if (sortArray.getData(i - 1) < sortArray.getData(i)) {
        if (language % 2 == 0) {
          showString(sortArray.getData(i - 1) + " is smaller than "
              + sortArray.getData(i) + " so they are sorted", 3);
        }

        if (language % 2 == 1) {
          showString(sortArray.getData(i - 1) + " ist kleiner als "
              + sortArray.getData(i) + " also sind sie sortiert", 3);
        }

      }

      if (i == 0 || sortArray.getData(i - 1) <= sortArray.getData(i)) {
        sortArray.unhighlightCell(0, sortArray.getLength(), null, null);
        sortArray.highlightCell(i - 1, i, null, null);
        sc.unhighlight(2);
        sc.unhighlight(3);
        sc.highlight(4);
        if (language % 2 == 0) {
          showString("we can move to the next position", 4);
        }
        if (language % 2 == 1) {
          showString("Wir gehen über zur naechsten Position", 4);
        }
        i++;

        if (i < sortArray.getLength())
          marker.move(i, null, null);
      } else {
        sc.unhighlight(2);
        sc.unhighlight(3);
        sc.unhighlight(4);
        sc.highlight(5);

        if (language % 2 == 0) {
          showString(sortArray.getData(i - 1) + " is bigger than "
              + sortArray.getData(i), 5);
        }
        if (language % 2 == 1) {
          showString(sortArray.getData(i - 1) + " ist groesser als "
              + sortArray.getData(i), 5);
        }

        sc.unhighlight(5);
        sc.highlight(6);

        if (language % 2 == 0) {
          showString("so we swap them!", 6);
        }

        if (language % 2 == 1) {
          showString("Also wird getauscht!", 6);
        }

        CheckpointUtils.checkpointEvent(this, "Swap", 
            new Variable("index1", i),
            new Variable("val1", sortArray.getData(i)),            
            new Variable("index0", i-1),
            new Variable("val0", sortArray.getData(i-1)));
        sortArray.swap(i, i - 1, null, new MsTiming(900));
        lang.nextStep();

        sc.unhighlight(6);
        sc.highlight(7);
        i--;

        if (language % 2 == 0) {
          showString("go one position backwords", 7);
        }

        if (language % 2 == 1) {
          showString("Gehe eine Position zurueck", 7);
        }

        sc.unhighlight(7);
        marker.move(i, null, null);
        lang.nextStep();
      }

      sortArray.unhighlightCell(0, sortArray.getLength() - 1, null, null);
      sc.unhighlight(3);
      sc.unhighlight(4);
      sc.highlight(2);

      if (i < sortArray.getLength()) {
        if (language % 2 == 0) {
          showString("i = " + i + ",  ar.length = " + sortArray.getLength()
              + ", (" + i + " < " + sortArray.getLength() + ") still TRUE!", 2);
        }

        if (language % 2 == 1) {
          showString("i = " + i + ",  ar.length = " + sortArray.getLength()
              + ", (" + i + " < " + sortArray.getLength() + ") Das ist WAHR!",
              2);
        }
      } else {
        if (language % 2 == 0) {
          showString("i = " + i + ",  ar.length = " + sortArray.getLength()
              + ", (" + i + " = " + sortArray.getLength() + ") =>  FALSE!", 2);
        }
        if (language % 2 == 1) {
          showString("i = " + i + ",  ar.length = " + sortArray.getLength()
              + ", (" + i + " = " + sortArray.getLength() + ") =>  FALSCH!", 2);
        }

      }
    }

    if (language % 2 == 0) {
      showString("Sorted!", 10);
    }
    if (language % 2 == 1) {
      showString("Sortiert!", 10);
    }

    lang.nextStep();

    marker.hide();
    sortArray.moveBy(null, 0, 40, new TicksTiming(10), new TicksTiming(10));

    lang.nextStep();

    oldArray.show();

    if (language % 2 == 0) {
      lang.newText(new Offset(10, 10, sortArray, AnimalScript.DIRECTION_E),
          "Array before sort", "text", null);
      lang.newText(new Offset(10, 50, sortArray, AnimalScript.DIRECTION_E),
          "Array after sort", "text", null);
    }

    if (language % 2 == 1) {
      lang.newText(new Offset(10, 10, sortArray, AnimalScript.DIRECTION_E),
          "Array vor dem sortieren", "text", null);
      lang.newText(new Offset(10, 50, sortArray, AnimalScript.DIRECTION_E),
          "Array nach dem sortieren", "text", null);
    }

  }

  public void unhighlight(IntArray x) {
    for (int i = 0; i < x.getLength(); i++) {

    }
  }

  public SourceCode showSourceCode() {
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
        Font.PLAIN, 12));
    SourceCode sc = lang.newSourceCode(new Coordinates(40, 200), "sourceCode",
        null, scProps);
    sc.addCodeLine("public void gnomeSort(int [ ] array){", null, 0, null);
    sc.addCodeLine("int i = 0;", null, 1, null);
    sc.addCodeLine("while (i < ar.length){", null, 1, null);
    sc.addCodeLine("if(i == 0 || array[i-1] <= array[i])", null, 2, null);
    sc.addCodeLine("i++;", null, 3, null);
    sc.addCodeLine("else{", null, 2, null);
    sc.addCodeLine("swap(array[i],array[i-1]);", null, 3, null);
    sc.addCodeLine("i--;", null, 3, null);
    sc.addCodeLine("}", null, 2, null);
    sc.addCodeLine("}", null, 1, null);
    sc.addCodeLine("}", null, 0, null);

    return sc;
  }

  public void showString(String text, int i) {
    String t = "// " + text;
    String[] x = new String[1];
    x[0] = t;
    // showing the array
    StringArray x1 = lang.newStringArray(new Coordinates(320, 205 + i * 17), x,
        "x", null, arrayDesign2);
    lang.nextStep();
    x1.hide(null);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    int[] a = (int[]) primitives.get("array");
    // adapt the COLOR to whatever the user chose
    // you could do this for all properties if you wanted to...
    arrayDesign = (ArrayProperties) props.get(0);
    pointerDesign = (ArrayMarkerProperties) props.get(1);
    scProps = (SourceCodeProperties) props.get(2);
    arrayDesign2 = (ArrayProperties) props.get(3);
    rp = (RectProperties) props.get(4);
    language = (Integer) primitives.get("Language/Sprache");

    execute(a);
    return lang.toString();
  }

  public void showGermanText() { // ****************************************************************************************************

    TextProperties textProp = new TextProperties();
    textProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Bold",
        Font.PLAIN, 20));
    Text descTitle = lang.newText(new Coordinates(40, 165),
        "Beschreibung des Algorithmus", "text", null, textProp);

    SourceCode germanText = lang.newSourceCode(new Coordinates(40, 200),
        "description", null);
    germanText.addCodeLine(
        "Man stelle sich einen Gartenzwerg (garden gnome) vor, welcher", null,
        0, null);
    germanText.addCodeLine(
        "vor n Blumentöpfen steht, die unterschiedliche Größen haben dürfen.",
        null, 0, null);
    germanText.addCodeLine(
        "Die Blumentöpfe sind in einer von links nach rechts", null, 0, null);
    germanText.addCodeLine(
        "verlaufenden Reihe aufgestellt. Ganz links steht der Gartenzwerg",
        null, 0, null);
    germanText.addCodeLine(
        "und möchte die Blumentöpfe von links nach rechts der Größe nach",
        null, 0, null);
    germanText.addCodeLine("aufsteigend sortieren.", null, 0, null);
    germanText.addCodeLine("", null, 1, null);

    lang.nextStep();

    germanText.addCodeLine(
        "Dazu vergleicht er die beiden Blumentöpfe, vor denen er grade steht.",
        null, 0, null);
    germanText.addCodeLine(
        "Stellt er fest, dass sie in der richtigen Reihenfolge sind,", null, 0,
        null);
    germanText.addCodeLine(
        "so macht er einen Schritt nach rechts. Stellt er hingegen fest,",
        null, 0, null);
    germanText.addCodeLine(
        "dass die Reihenfolge nicht stimmt, so vertauscht er die", null, 0,
        null);
    germanText
        .addCodeLine("beiden Blumentöpfe und macht einen Schritt nach links.",
            null, 0, null);
    germanText.addCodeLine("", null, 1, null);

    lang.nextStep();

    germanText.addCodeLine(
        "Dies wiederholt er ständig. Fertig ist er, wenn er", null, 0, null);
    germanText.addCodeLine(
        "am ganz rechts stehenden Blumentopf ankommt und feststellt,", null, 0,
        null);
    germanText.addCodeLine("dass dieser in der richtigen Reihenfolge steht.",
        null, 0, null);
    germanText.addCodeLine("", null, 1, null);
    germanText.addCodeLine("entnommen aus: ", null, 0, null);
    germanText.addCodeLine("http://de.wikipedia.org/wiki/Gnomesort", null, 0,
        null);

    drawRect(descTitle, germanText);

    lang.nextStep();
  }

  private void drawRect(Text descTitle, SourceCode descText) {
    Rect inner = lang.newRect(new Offset(-5, -35, descText,
        AnimalScript.DIRECTION_NW), new Offset(5, 5, descText,
        AnimalScript.DIRECTION_SE), "rect", null, rp);
    Rect outer = lang.newRect(new Offset(-3, -33, descText,
        AnimalScript.DIRECTION_NW), new Offset(7, 7, descText,
        AnimalScript.DIRECTION_SE), "rect", null, rp);
    Rect outer1 = lang.newRect(new Offset(-7, -37, descText,
        AnimalScript.DIRECTION_NW), new Offset(3, 3, descText,
        AnimalScript.DIRECTION_SE), "rect", null, rp);

    lang.nextStep();

    descTitle.moveBy(null, 600, 0, new TicksTiming(10), new TicksTiming(10));
    descText.moveBy(null, 600, 0, new TicksTiming(10), new TicksTiming(10));
    inner.moveBy(null, 600, 0, new TicksTiming(10), new TicksTiming(10));
    outer.moveBy(null, 600, 0, new TicksTiming(10), new TicksTiming(10));
    outer1.moveBy(null, 600, 0, new TicksTiming(10), new TicksTiming(10));
  }

  public void showEnglishText() {
    TextProperties textProp = new TextProperties();
    textProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Bold",
        Font.PLAIN, 20));
    Text descTitle = lang.newText(new Coordinates(40, 165),
        "Description of algorithm", "text", null, textProp);

    SourceCode englishText = lang.newSourceCode(new Coordinates(40, 200),
        "description", null);
    englishText
        .addCodeLine("Gnome sort is a sorting algorithm which is similar to ",
            null, 0, null);
    englishText.addCodeLine(
        "insertion sort, except that moving an element to its ", null, 0, null);
    englishText
        .addCodeLine("proper place is accomplished by a series of swaps, as ",
            null, 0, null);
    englishText.addCodeLine(
        "in bubble sort. The name comes from the supposed ", null, 0, null);
    englishText.addCodeLine(
        "behavior of the Dutch garden gnome in sorting a line ", null, 0, null);
    englishText.addCodeLine(
        "of flowerpots and is described on Dick Grune's Gnome ", null, 0, null);
    englishText.addCodeLine("sort page", null, 0, null);
    englishText.addCodeLine("", null, 0, null);

    lang.nextStep();

    englishText.addCodeLine(
        "It is conceptually simple, requiring no nested loops.", null, 0, null);
    englishText.addCodeLine("The running time is O(n*n), but in practice the ",
        null, 0, null);
    englishText.addCodeLine("algorithm can run as fast as Insertion sort.",
        null, 0, null);
    englishText.addCodeLine("", null, 0, null);

    lang.nextStep();

    englishText.addCodeLine(
        "The algorithm always finds the first place where two ", null, 0, null);
    englishText.addCodeLine(
        "adjacent elements are in the wrong order, and swaps ", null, 0, null);
    englishText.addCodeLine(
        "them. It takes advantage of the fact that performing ", null, 0, null);
    englishText
        .addCodeLine("a swap can introduce a new out-of-order adjacent pair ",
            null, 0, null);
    englishText.addCodeLine(
        "only right before or after the two swapped elements. ", null, 0, null);
    englishText.addCodeLine("It does not assume that elements forward of the ",
        null, 0, null);
    englishText.addCodeLine(
        "current position are sorted, so it only needs to ", null, 0, null);
    englishText.addCodeLine("check the position directly before the swapped ",
        null, 0, null);
    englishText.addCodeLine("elements.", null, 0, null);
    englishText.addCodeLine("", null, 0, null);

    lang.nextStep();

    englishText.addCodeLine(
        "taken from http://en.wikipedia.org/wiki/Gnome_sort", null, 0, null);
    drawRect(descTitle, englishText);

  }

  // ****************************************************************************************************

  public String getAlgorithmName() {
    return "Gnome Sort";
  }

  public String getAnimationAuthor() {
    return "Tijani Ahmedou, Nikola Dyundev, Arif Sami";
  }

  public String getCodeExample() {

    return "public void gnomeSort(int [ ] array){\n" + "  int i = 0;\n"
        + "  while (i < ar.length){\n"
        + "    if(i == 0 || array[i-1] <= array[i])\n" + "      i++;\n"
        + "    else{\n" + "      swap(array[i],array[i-1]);\n" + "      i--;\n"
        + "    }\n" + "  }\n" + "}";
  }

  public Locale getContentLocale() {
    // TODO Auto-generated method stub
    return Locale.US;
  }

  public String getDescription() {
    // TODO Auto-generated method stub
    return "Ein einfacher und sehr stabiler Sortieralgorithmus\n"
        + "A simple and stable sorting algorithm\n" + "\n" + "Language: \n"
        + "0 - English\n" + "1 - Deutsch (default)\n" + "\n";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public GeneratorType getGeneratorType() {
    // TODO Auto-generated method stub
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getName() {
    // TODO Auto-generated method stub
    return "Gnome Sort";
  }

  public String getOutputLanguage() {
    // TODO Auto-generated method stub
    return Generator.JAVA_OUTPUT;
  }

  public void init() {
    // nothing to be done here
  }
}
