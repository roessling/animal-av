package algoanim.examples;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.FourValueCounter;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.FourValueView;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.ConceptualStack;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.StackProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class CounterDemo {

  private static final String            HEADER1                = "Counter Demo: Stack -> Array";
  private static final String            DESCRIPTION1           = "Diese Animation soll die Funktionsweise der Zähler-Objekte für ANIMALveranschaulichen. ";
  private static final String            DESCRIPTION2           = "Dafür werden zwei Datenstrukturen verwendet. Ein IntArray und ein ConceptualStack.";
  private static final String            DESCRIPTION3           = "Für beide Datenstrukturen werden Zähler und entsprechende Visualisierungen erstellt.";
  private static final String            DESCRIPTION4           = "Anschließend werden die Elemente des Stacks ausgelesen und in das Array gespeichert. ";
  private static final String            DESCRIPTION5           = "Bei beiden Datenstrukturen werden die entstehenden Zuweisungen und Zugriffe automatisch ";
  private static final String            DESCRIPTION6           = "mitgezählt. Für den Stack werden zusätzlich Einreihungen und Entnahmen gezählt.";

  private final String                   HEADER2                = "Counter Demo: Insertion Sort";
  private static final String            DESCRIPTION7           = "Insertion Sort entnimmt der unsortierten Eingabemenge ein beliebiges (z.B. das erste) Element und ";
  private static final String            DESCRIPTION8           = "fügt es an richtiger Stelle in die (anfangs leere) Ausgabemenge ein. Das Verfahren arbeitet also ";
  private static final String            DESCRIPTION9           = "in-place. Geht man in der Reihenfolge der ursprünglichen Menge vor, so ist es jedoch (etwa im Gegensatz ";
  private static final String            DESCRIPTION10          = "zu Selection Sort) stabil. Wird auf einem Array gearbeitet, so müssen die Elemente nach dem neu ";
  private static final String            DESCRIPTION11          = "eingefügten Element verschoben werden.Dies ist die eigentlich teure Operation von Insertionsort, da";
  private static final String            DESCRIPTION12          = "das Finden der richtigen Einfügeposition über eine binäre Suche vergleichsweise effizient erfolgen kann.";

  private final IntArray                 array;
  private final TwoValueCounter          arrayCounter;
  private final TwoValueView             arrayView;
  private final ConceptualStack<Integer> stack;
  private final FourValueCounter         stackCounter;
  private final FourValueView            stackView;
  private final ArrayProperties          arrayProps             = new ArrayProperties();
  private final Language                 lang;
  private final StackProperties          stackProps             = new StackProperties();
  private final TextProperties           HeaderTP               = new TextProperties();
  private final TextProperties           DesciptionTP           = new TextProperties();
  private final CounterProperties        arrayCounterProperties = new CounterProperties();
  private Text                           header;
  private Text                           description1;
  private Text                           description2;
  private Text                           description3;
  private Text                           description4;
  private Text                           description5;
  private Text                           description6;

  private SourceCode                     code;
  private final ArrayMarkerProperties    jMarkerProps           = new ArrayMarkerProperties();
  private final ArrayMarkerProperties    iMarkerProps           = new ArrayMarkerProperties();
  private final SourceCodeProperties     sourceProps            = new SourceCodeProperties();

  public CounterDemo(Language l) {
    lang = l;
    l.setStepMode(true);
    // Init Header and Description Text
    HeaderTP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 30));
    DesciptionTP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        "SansSerif", Font.PLAIN, 18));
    header = lang.newText(new Coordinates(100, 50), HEADER1, "Header", null,
        HeaderTP);
    description1 = lang.newText(new Offset(0, 5, header, "SW"), DESCRIPTION1,
        "Description1", null);
    description2 = lang.newText(new Offset(0, 5, description1, "SW"),
        DESCRIPTION2, "Description2", null);
    description3 = lang.newText(new Offset(0, 5, description2, "SW"),
        DESCRIPTION3, "Description3", null);
    description4 = lang.newText(new Offset(0, 5, description3, "SW"),
        DESCRIPTION4, "Description4", null);
    description5 = lang.newText(new Offset(0, 5, description4, "SW"),
        DESCRIPTION5, "Description5", null);
    description6 = lang.newText(new Offset(0, 5, description5, "SW"),
        DESCRIPTION6, "Description6", null);
    // Init Stack
    ArrayList<Integer> stackData = new ArrayList<Integer>() {
      {
        add(7);
        add(4);
        add(9);
        add(5);
        add(8);
        add(6);
      }
    };
    stackProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    stackProps.set(AnimationPropertiesKeys.DIVIDINGLINE_COLOR_PROPERTY,
        Color.ORANGE);
    stackProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
    stackProps.set(AnimationPropertiesKeys.ALTERNATE_FILLED_PROPERTY, true);
    stackProps
        .set(AnimationPropertiesKeys.ALTERNATE_FILL_PROPERTY, Color.GREEN);
    stackProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    stack = lang.newConceptualStack(new Coordinates(300, 240), stackData,
        "Stack", null, stackProps);
    // Init stackCounter + stackView
    stackCounter = lang.newCounter(stack);
    stackView = lang.newCounterView(stackCounter, new Coordinates(300, 370));
    // Init Array
    int[] arrayData;
    arrayData = new int[9];
    arrayData[0] = 2;
    arrayData[1] = 1;
    arrayData[2] = 3;
    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.RED);
    array = lang.newIntArray(new Coordinates(100, 300), arrayData, "Array",
        null, arrayProps);
    // Init arrayCounter + arrayView
    arrayCounter = lang.newCounter(array);
    String[] names = { "Zuweisungen: ", "Zugriffe: " };
    arrayCounterProperties.set(AnimationPropertiesKeys.FILL_PROPERTY,
        Color.BLUE);
    arrayCounterProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    arrayCounterProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
        Color.RED);
    arrayView = lang.newCounterView(arrayCounter, new Coordinates(100, 370),
        arrayCounterProperties, true, true, names);
    lang.nextStep();
    // Transfer of stackData to Array
    int value = 0;
    int pos = 3;
    while (!stack.isEmpty()) {
      value = stack.top();
      stack.pop();
      stack.highlightTopCell(null, null);
      array.put(pos, value, null, null);
      arrayData[pos] = value;
      array.highlightElem(pos, null, null);
      array.unhighlightElem(pos - 1, null, null);
      pos++;
      lang.nextStep();
    }
    array.unhighlightElem(pos - 1, null, null);
    arrayView.highlight();
    lang.nextStep();
    lang.hideAllPrimitives();
    lang.nextStep();
    header = lang.newText(new Coordinates(100, 50), HEADER2, "Header", null,
        HeaderTP);
    description1 = lang.newText(new Offset(0, 5, header, "SW"), DESCRIPTION7,
        "Description1", null);
    description2 = lang.newText(new Offset(0, 5, description1, "SW"),
        DESCRIPTION8, "Description2", null);
    description3 = lang.newText(new Offset(0, 5, description2, "SW"),
        DESCRIPTION9, "Description3", null);
    description4 = lang.newText(new Offset(0, 5, description3, "SW"),
        DESCRIPTION10, "Description4", null);
    description5 = lang.newText(new Offset(0, 5, description4, "SW"),
        DESCRIPTION11, "Description5", null);
    description6 = lang.newText(new Offset(0, 5, description5, "SW"),
        DESCRIPTION12, "Description6", null);
    array.show();
    arrayView.show();
    arrayView.unhighlight();
    header.show();
    description1.show();
    description2.show();
    description3.show();
    description4.show();
    description5.show();
    description6.show();
    sort();
    stackView.hide();
  }

  public static void main(String[] args) {
    Language lang = new AnimalScript("CounterDemo", "Axel Heimann", 800, 600);
//    CounterDemo cd = 
    new CounterDemo(lang);
    System.out.println(lang);

  }

  private void generateCodeExample() {
    code = lang.newSourceCode(new Coordinates(420, 250), "code", null,
        sourceProps);
    code.addCodeLine("public void insertionSort(int[] array){", "", 0, null);
    code.addCodeLine("int i, j, temp;", "", 1, null);
    code.addCodeLine("for (i = 1; i < array.length; i++) {", "", 1, null);
    code.addCodeLine("j = i;", "", 2, null);
    code.addCodeLine("temp = array[i];", "", 2, null);
    code.addCodeLine("while (j > 0 && array[j - 1] > temp) {", "", 2, null);
    code.addCodeLine("array[j] = array[j - 1];", "", 3, null);
    code.addCodeLine("j = j - 1;", "", 3, null);
    code.addCodeLine("}", "", 2, null);
    code.addCodeLine("array[j] = temp;", "", 2, null);
    code.addCodeLine("}", "", 1, null);
    code.addCodeLine("}", "", 0, null);
  }

  public void sort() {
    // show array and code
    lang.nextStep();
    sourceProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    generateCodeExample();
    lang.nextStep();
    code.highlight(0);
    lang.nextStep();
    code.toggleHighlight(0, 1);
    int i = 0, j = 0, temp = -1;
    // visual rendition of the declaration of i, j, temp
    ArrayMarker iMarker = lang
        .newArrayMarker(array, 0, "i", null, iMarkerProps);
    ArrayMarker jMarker = lang
        .newArrayMarker(array, 0, "j", null, jMarkerProps);
    lang.newText(new Offset(0, 20, array, AnimalScript.DIRECTION_SW), "temp:",
        "temp", null, DesciptionTP);
    lang.nextStep();
    code.unhighlight(1);
    Text tmpTxt = null;
    for (i = 1; i < array.getLength(); i++) {
      // previous line must be unhighlighted - 1 if i==1, else 9
      code.toggleHighlight((i != 1) ? 9 : 1, 2);
      iMarker.move(i, null, null);
      array.highlightElem(i - 1, null, null);
      lang.nextStep();
      code.highlight(2, 0, true);
      code.highlight(3);
      // move j
      jMarker.move(i, null, null);
      j = i;
      lang.nextStep();
      code.toggleHighlight(3, 4);
      temp = array.getData(i);
      if (i == 1)
        tmpTxt = lang.newText(new Offset(10, -6, "temp",
            AnimalScript.DIRECTION_BASELINE_END), String.valueOf(temp), "tmp",
            null, DesciptionTP);
      else
        tmpTxt.setText(String.valueOf(temp), null, null);
      lang.nextStep();
      // next step: enter while loop
      code.toggleHighlight(4, 5);
      while (j > 0 && array.getData(j - 1) > temp) {
        // count 2 comparisons
        lang.nextStep();
        // next step: set loop to context, copy values
        // note: the internal while loop has already have been unhighlighted,
        // if this was not the first iteration
        array.highlightElem(j - 1, null, null);
        code.highlight(5, 0, true);
        code.highlight(6);
        array.put(j, array.getData(j - 1), null, null);
        lang.nextStep();
        code.toggleHighlight(6, 7);
        jMarker.move(j - 1, null, null);
        j--;
        array.unhighlightElem(j - 1, j, null, null);
        lang.nextStep();
        code.toggleHighlight(7, 5);
      }
      lang.nextStep();
      code.toggleHighlight(5, 9);
      array.put(j, temp, null, null);
      tmpTxt.changeColor("color", Color.RED, null, null);
      array.highlightElem(0, i, null, null);
      lang.nextStep();
    }
    // end for loop
    // next step: make sure no code is highlighted. Last must be line 9.
    code.unhighlight(2);
    code.unhighlight(9);
  }
}
