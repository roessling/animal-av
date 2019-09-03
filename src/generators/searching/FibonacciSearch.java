/*
 * FibonacciSearch.java
 * Mai Ly Tran, 2017 for the Animal project at TU Darmstadt.
 */
package generators.searching;

import algoanim.primitives.*;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.Arrays;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import interactionsupport.models.MultipleChoiceQuestionModel;

public class FibonacciSearch implements Generator {
    private Language lang;
    private Text header;
    private TextProperties textProps;
    private ArrayMarkerProperties arrayIMProps;
    private SourceCodeProperties scProps;
    private int[] array;
    private int searchedElement;
    private Variables varTable;

    public static void main(String[] args) {
        // Create a new language object for generating animation code
        // this requires type, name, author, screen width, screen height
        FibonacciSearch s = new FibonacciSearch();
        s.init();
        int[] array = {40};
        int x = 40;
        Hashtable<String, Object> prims = new Hashtable<String, Object>();
        prims.put("array", array);
        prims.put("searchedElement", x);
        AnimationPropertiesContainer t = new AnimationPropertiesContainer();
        System.out.print(s.generate(t, prims));
    }

    public void init(){
        lang = new AnimalScript("Fibonacci Suche", "Mai Ly Tran", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        array = (int[])primitives.get("array");
        searchedElement = (Integer)primitives.get("searchedElement");
        fibSearch(array, searchedElement);
        lang.finalizeGeneration();
        return lang.toString();
    }

    public boolean validateInput(AnimationPropertiesContainer
                                         props, Hashtable<String, Object>
                                         primitives) {
        array = (int[])primitives.get("array");
        searchedElement = (Integer)primitives.get("searchedElement");

        int[] arrayCopy = array.clone();
        Arrays.sort(arrayCopy);

        return Arrays.equals(array, arrayCopy) && array.length != 0 ;
    }

    public void fibSearch(int[] arr, int x){

        // Header
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 24));
        header = lang.newText(new Coordinates(20, 30), "Fibonacci-Suche",
                "header", null, headerProps);

        // Source Code Properties
        scProps = new SourceCodeProperties();
        scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("Monospaced", Font.PLAIN, 12));

        scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        // Array Properties
        ArrayProperties arrayProps = new ArrayProperties();
        arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
                Color.YELLOW);

        // Text Properties
        textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.MONOSPACED, Font.PLAIN, 14));

        // Array Marker Properties
        arrayIMProps = new ArrayMarkerProperties();
        arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
        arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        // Vartable declaration
        varTable = lang.newVariables();
        varTable.declare("int", "fib");
        varTable.declare("int", "fibM1");
        varTable.declare("int", "fibM2");
        varTable.declare("int", "offset", "-1");
        varTable.declare("int", "i");

        // Desc Text
        SourceCode desc = lang.newSourceCode(new Coordinates(25, 80), "sourceCode",
                null, scProps);
        desc.addCodeLine("In der Fibonacci-Suche wird ein Element x in einem ein sortiertes Array mithilfe von Fibonacci-Zahlen ", null, 0, null);
        desc.addCodeLine("gesucht, indem es anhand derer geteilt wird. Der Algorithmus funktioniert wie folgend:", null, 0, null);
        desc.addCodeLine("1. Es wird mit 3 aufeinanderfolgenden Fibonacchi Zahlen (m-2, m-1, m) gestartet, ", null, 0, null);
        desc.addCodeLine("  wobei die größte (m) davon nächstgrößer oder gleich der Arraylänge ist.",  null, 0, null);
        desc.addCodeLine("2a. Wenn x der Zahl dem Element an der Position m-2 + Anzahl eliminierter Elemente gleicht,", null, 0, null);
        desc.addCodeLine("  wird diese Position zurückgegeben.", null, 0, null);
        desc.addCodeLine("2b. Wenn x kleiner ist als das Element an der Position m-2 + Anzahl eliminierter Elemente ist,", null, 0, null);
        desc.addCodeLine("  werden alle Fibonacci-Zahlen um zwei Stufen verkleinert, was einer Eliminierung von 2/3 am Ende", null, 0, null);
        desc.addCodeLine("des verbleibenden Arrays entspricht.", null, 0, null);
        desc.addCodeLine("2c. Wenn x größer als das Element an der Position m-2 + Anzahl eliminierter Elemente ist, werden alle", null, 0, null);
        desc.addCodeLine("  Fibonacci Zahlen um eine Stufe verkleinert und der Offset wird auf diese Position gesetzt, was einer", null, 0, null);
        desc.addCodeLine("  Eliminierung von 1/3 am Anfang des verbleibenden Arrays entspricht.", null, 0, null);
        desc.addCodeLine("3. Wenn ein Element übrig bleibt, überprüfe, ob m-1 gleich 1 ist und vergleiche x mit diesem. Wenn sie gleich", null, 0, null);
        desc.addCodeLine("  sind, wird die Position dieses Elementes zurückgegeben.", null, 0, null);
        desc.addCodeLine("4. Wurde die Zahl nicht gefunden, wird -1 zurückgegeben.", null, 0, null);

        lang.nextStep("Initialisierung");
        desc.hide();

        SourceCode sc = lang.newSourceCode(new Coordinates(25, 80), "sourceCode",
                null, scProps);

        sc.addCodeLine("public int fibSearch(int[] arr, int x){", null, 0, null);
        sc.addCodeLine("int fibM2 = 0;", null, 1, null);
        sc.addCodeLine("int fibM1 = 1;", null, 1, null);
        sc.addCodeLine("int fib = fibM1 + fibM2;", null, 1, null);
        sc.addCodeLine("while(fib < arr.length){", null, 1, null); // 4
        sc.addCodeLine("fibM2 = fibM1;", null, 2, null);
        sc.addCodeLine("fibM1 = fib;", null, 2, null);
        sc.addCodeLine("fib = fibM1 + fibM2;", null, 2, null);
        sc.addCodeLine("}", null, 1, null);
        sc.addCodeLine("int offset = -1;", null, 1, null);
        sc.addCodeLine("while (fib > 1){", null, 1, null); // 10
        sc.addCodeLine("int i = (offset + fibM2) <= (arr.length - 1)? (offset + fibM2) : (arr.length - 1);",
                null, 2, null);
        sc.addCodeLine("if (arr[i] < x){", null, 2, null); // 12
        sc.addCodeLine("fib  = fibM1;", null, 3, null);
        sc.addCodeLine("fibM1 = fibM2;", null, 3, null);
        sc.addCodeLine("fibM2 = fib - fibM1;", null, 3, null);
        sc.addCodeLine("offset = i;", null, 3, null);
        sc.addCodeLine("}", null, 2, null);
        sc.addCodeLine("else if (arr[i] > x) {", null, 2, null); // 18
        sc.addCodeLine("fib  = fibM2;", null, 3, null);
        sc.addCodeLine("fibM1 = fibM1 - fibM2;", null, 3, null);
        sc.addCodeLine("fibM2 = fib - fibM1;", null, 3, null);
        sc.addCodeLine("}", null, 2, null);
        sc.addCodeLine("else return i;", null, 2, null);
        sc.addCodeLine("}", null, 1, null);
        sc.addCodeLine("if(fibM1 == 1 && offset+1 < arr.length && arr[offset+1] == x)", null, 1, null); // 25
        sc.addCodeLine("return offset + 1;", null, 2, null);
        sc.addCodeLine("return -1;", null, 1, null);
        sc.addCodeLine("}", null, 0, null);

        IntArray ia = lang.newIntArray(new Coordinates(680, 100), arr, "intArray",
                null, arrayProps);
        // Highlight all cells
        ia.highlightCell(0, ia.getLength() - 1, null, null);

        lang.newText(new Coordinates(680, 280),
                "gesucht: " + x, "gesucht", null, textProps);

        lang.nextStep();
        MultipleChoiceQuestionModel mc = new MultipleChoiceQuestionModel("Choice2");
        mc.setPrompt("Welche Komplexität hat die Fibonacci-Suche?");
        mc.addAnswer("O(log(n))", 1,
                "Korrekt");
        mc.addAnswer("O(n)", 0,
                "Falsch.");
        mc.addAnswer("O(1)", 0,
                "Falsch.");
        mc.addAnswer("O(n^2)", 0,
                "Falsch.");
        lang.addMCQuestion(mc);
        fibSearch(ia, x, sc);
    }

    private void fibSearch(IntArray ia, int x, SourceCode sc){
        int fm2 = 0;
        int fm1 = 1;
        int fm = fm2 + fm1;
        int offset = -1;
        int elemCount = ia.getLength() - 1;
        int pos = 0;
        Boolean found = false;
        Boolean asked = false;
        ArrayMarker marker = lang.newArrayMarker(ia, 0, "i",
                null, arrayIMProps);
        marker.hide();
        sc.highlight(0, 0, false);
        lang.nextStep();

        sc.toggleHighlight(0, 1);
        Text fibM2 = lang.newText(new Coordinates(680, 320),
                "fibM2: " + fm2, "m2", null, textProps);
        fibM2.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red, null, null);
        varTable.set("fibM2", Integer.toString(fm2));
        lang.nextStep();

        sc.toggleHighlight(1, 2);
        Text fibM1 = lang.newText(new Coordinates(680, 340),
                "fibM1: " + fm1, "m1", null, textProps);
        fibM2.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);
        fibM1.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red, null, null);
        varTable.set("fibM1", Integer.toString(fm1));
        lang.nextStep();

        sc.toggleHighlight(2, 3);
        Text fib = lang.newText(new Coordinates(680, 360),
                "fib: " + fm, "fib", null, textProps);
        fibM1.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);
        fib.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red, null, null);
        varTable.set("fib", Integer.toString(fm));
        lang.nextStep("Hochzählen der Fibonaccizahlen");

        // while (fib < arr.Length)
        fib.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);
        Text desc = lang.newText(new Coordinates(680, 200),
                "Die Fibonaccizahlen werden hochgezählt, bis fib >= der Arraylänge ist.", "desc", null, textProps);
        sc.toggleHighlight(3, 4);
        if (fm >= ia.getLength()) lang.nextStep();
        while (fm < ia.getLength()) {
            sc.toggleHighlight(7, 4);
            lang.nextStep();

            sc.toggleHighlight(4, 5);
            fm2 = fm1;
            fibM2.setText("fibM2:" + fm2, null, null);
            fibM2.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red, null, null);
            varTable.set("fibM2", Integer.toString(fm2));
            lang.nextStep();

            sc.toggleHighlight(5, 6);
            fm1 = fm;
            fibM1.setText("fibM1: " + fm1, null, null);
            fibM2.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);
            fibM1.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red, null, null);
            varTable.set("fibM1", Integer.toString(fm1));
            lang.nextStep();

            sc.toggleHighlight(6, 7);
            fm = fm1 + fm2;
            fib.setText("fib: " + fm, null, null);
            fibM1.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);
            fib.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red, null, null);
            varTable.set("fib", Integer.toString(fm));
            lang.nextStep();

            fib.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);
        }
        sc.toggleHighlight(4,9); // offset
        sc.toggleHighlight(7, 9);
        desc.hide();
        Text offText = lang.newText(new Coordinates(680, 400),
                "offset: " + offset, "off", null, textProps);
        offText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red, null, null);
        varTable.set("offset", Integer.toString(offset));
        lang.nextStep();

        // while (fib > 1)
        offText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);
        sc.toggleHighlight(9, 10);
        if (fm <= 1) lang.nextStep();
        while (fm > 1){
            desc.hide();
            sc.toggleHighlight(16, 10);
            sc.toggleHighlight(21, 10);
            lang.nextStep();
            // int i
            sc.toggleHighlight(10, 11);
            desc.setText("x wird mit dem letzten Element, das von fibM2 gedeckt wird, verglichen.",null, null);
            desc.show();
            int i = (offset + fm2) <= elemCount? (offset + fm2) : elemCount;
            varTable.set("i", Integer.toString(i));
            marker.move(i, null, null);
            marker.show();
            lang.nextStep();

            //if (arr[i] < x)
            sc.toggleHighlight(11, 12);
            lang.nextStep("Vergleich, ob x größer ist");
            if (!asked) {
                MultipleChoiceQuestionModel mc = new MultipleChoiceQuestionModel("Choice1");
                mc.setPrompt("Was wird vom Array weggeworfen, wenn x größer als das zurzeit betrachtete Element ist?");
                mc.addAnswer("Alles von offset bis i", 1,
                        "Korrekt");
                mc.addAnswer("Alles nach i+1", 0,
                        "Falsch, dies ist nur im Falle, falls x kleiner ist, gültig");
                lang.addMCQuestion(mc);
                asked = true;
            }
            if (ia.getData(i) < x) {
                desc.setText("x ist größer, also wird alles von offset bis i nicht mehr betrachtet.", null, null);
                ia.unhighlightCell(offset == -1? 0 : offset, i, null, null);
                sc.toggleHighlight(12, 13);
                fm = fm1;
                fib.setText("fib: " + fm, null, null);
                fib.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red, null, null);
                varTable.set("fib", Integer.toString(fm));
                lang.nextStep();

                sc.toggleHighlight(13, 14);
                fm1 = fm2;
                fibM1.setText("fibM1: " + fm1, null, null);
                fib.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);
                fibM1.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red, null, null);
                varTable.set("fibM1", Integer.toString(fm1));
                lang.nextStep();

                sc.toggleHighlight(14, 15);
                fm2 = fm - fm1;
                fibM2.setText("fibM2:" + fm2, null, null);
                fibM1.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);
                fibM2.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red, null, null);
                varTable.set("fibM2", Integer.toString(fm2));
                lang.nextStep();

                fibM2.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);
                sc.toggleHighlight(15, 16);
                offset = i;
                offText.setText("offset: " + offset, null, null);
                offText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red, null, null);
                varTable.set("offset", Integer.toString(offset));
                lang.nextStep();
                offText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);
                continue;
            }
            // else if arr[i] > x
            sc.toggleHighlight(12, 18);
            lang.nextStep("Vergleich, ob x kleiner ist");
            if (ia.getData(i) > x) {
                desc.setText("x ist kleiner, also wird alles nach i+1 nicht mehr betrachtet.", null, null);
                ia.unhighlightCell(i+1, elemCount, null, null);
                sc.toggleHighlight(18, 19);
                fm = fm2;
                fib.setText("fib: " + fm, null, null);
                fib.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red, null, null);
                varTable.set("fib", Integer.toString(fm));
                sc.toggleHighlight(19, 20);
                lang.nextStep();
                fm1 -= fm2;
                fibM1.setText("fibM1: " + fm1, null, null);
                fib.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);
                fibM1.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red, null, null);
                varTable.set("fibM1", Integer.toString(fm1));
                lang.nextStep();
                sc.toggleHighlight(20, 21);
                fm2 = fm - fm1;
                fibM2.setText("fibM2:" + fm2, null, null);
                fibM1.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);
                fibM2.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red, null, null);
                varTable.set("fibM2", Integer.toString(fm2));
                lang.nextStep();
                fibM2.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black, null, null);
                continue;
            }
            // else return i
            sc.toggleHighlight(18, 23);
            desc.setText("x wurde gefunden.", null, null);
            pos = i;
            found = true;
            lang.nextStep("Abschluss");
            break;
        }
        // last if
        if (!found) {
            sc.toggleHighlight(21, 25);
            sc.toggleHighlight(10, 25);
            desc.setText("Vergleiche das letzte verbleibende Element mit x.", null, null);
            lang.nextStep();
            if (fm1 == 1 && offset + 1 < ia.getLength() && ia.getData(offset + 1) == x) {
                sc.toggleHighlight(25, 26);
                pos = offset + 1;
                lang.nextStep("Abschluss");
            } else {
                sc.toggleHighlight(25, 27);
                ia.unhighlightCell(offset + 1,null, null);
                desc.setText("x wurde nicht gefunden.", null, null);
                pos = -1;
                lang.nextStep("Abschluss");
            }
        }

        //Abschlussfolie
        lang.hideAllPrimitives();
        marker.hide();
        header.show();
        if (pos == -1)
            lang.newText(new Coordinates(25, 80),
                    "Das gesuchte Element " + x + " ist nicht im Array enthalten.",
                    "notFound", null, textProps);
        else
            lang.newText(new Coordinates(25, 80),
                    "Das gesuchte Element " + x + " wurde an der Stelle " + pos + " gefunden.",
                    "found", null, textProps);

        SourceCode info = lang.newSourceCode(new Coordinates(30, 80), "info",
                null, scProps);
        info.addCodeLine("Die Fibonacci-Suche hat folgendes mit der binären Suche gemeinsam:", null, 0, null);
        info.addCodeLine("1. Beide haben die Komplexität O(log(n))", null, 1, null);
        info.addCodeLine("2. Beide funktionieren arbeiten mit sortierten Arrays", null, 1, null);
        info.addCodeLine("3. Beide nutzen 'Teile-und-Herrsche'", null, 1, null);
        info.addCodeLine("", null, 0, null);
        info.addCodeLine("Und dies sind ihre Unterschiede:", null, 0, null);
        info.addCodeLine("1. Die Fibonacci-Suche teilt das Array in ungleiche Größen", null, 1, null);
        info.addCodeLine("2. Im Gegensatz zur binären Suche didiviert die Fibonacci-Suche nicht, sondern addiert und subtrahiert", null, 1, null);
        info.addCodeLine("3. Die Fibonacci-Suche hat ggf. geringere Zugriffszeiten", null, 1, null);

    }


    public String getName() {
        return "Fibonacci Suche";
    }

    public String getAlgorithmName() {
        return "Fibonacci Suche";
    }

    public String getAnimationAuthor() {
        return "Mai Ly Tran";
    }

    public String getDescription(){
        return "In der Fibonacci Suche wird ein Element x in einem ein sortiertes Array mithilfe von Fibonacci-Zahlen "
 +"\n"
 +"gesucht, indem es anhand derer geteilt wird. Der Algorithmus funktioniert wie folgend:"
 +"\n"
 +"1. Es wird mit 3 aufeinanderfolgenden Fibonacchi Zahlen (m-2, m-1, m) gestartet, "
 +"\n"
 +"wobei die größte (m) davon nächstgrößer oder gleich der Arraylänge ist."
 +"\n"
 +"2a. Wenn x der Zahl dem Element an der Position m-2 + Anzahl eliminierter Elemente gleicht,"
 +"\n"
 +"      wird diese Position zurückgegeben."
                +"\n"
 +"2b. Wenn x kleiner ist als das Element an der Position m-2 + Anzahl eliminierter Elemente ist,"
 +"\n"
 +"    werden alle Fibonacci-Zahlen um zwei Stufen verkleinert, was einer Eliminierung von 2/3 am Ende "
 +"\n"
 +"des   verbleibenden Arrays entspricht."
 +"\n"
 +"2c. Wenn x größer als das Element an der Position m-2 + Anzahl eliminierter Elemente ist, werden alle"
 +"\n"
 +"      Fibonacci Zahlen um eine Stufe verkleinert und der Offset wird auf diese Position gesetzt, was einer "
 +"\n"
 +"     Eliminierung von 1/3 am Anfang des verbleibenden Arrays entspricht."
 +"\n"
 +"3. Wenn ein Element übrig bleibt, überprüfe, ob m-1 1 ist und vergleiche x mit diesem. Wenn sie gleich"
 +"\n"
 +"sind, wird die Position dieses Elementes zurückgegeben."
 +"\n"
 +"4. Wurde die Zahl nicht gefunden, wird -1 zurückgegeben. ";
    }

    public String getCodeExample(){
        return "    public int fibSearch(int[] arr, int x){"
 +"\n"
 +"        int fibM2 = 0;"
 +"\n"
 +"        int fibM1 = 1;"
 +"\n"
 +"        int fib = fibM1 + fibM2;"
 +"\n"
 +"\n"
 +"        while(fib < arr.length){"
 +"\n"
 +"            fibM2 = fibM1;"
 +"\n"
 +"            fibM1 = fib;"
 +"\n"
 +"            fib = fibM1 + fibM2;"
 +"\n"
 +"        }"
 +"\n"
 +"        int offset = -1;"
 +"\n"
 +"        while (fib > 1){"
 +"\n"
 +"            int i = (offset + fibM2) <= (arr.length - 1)? (offset + fibM2) : (arr.length - 1);"
 +"\n"
 +"            if (arr[i] < x){"
 +"\n"
 +"                fib  = fibM1;"
 +"\n"
 +"                fibM1 = fibM2;"
 +"\n"
 +"                fibM2 = fib - fibM1;"
 +"\n"
 +"                offset = i;"
 +"\n"
 +"            }"
 +"\n"
 +"            else if (arr[i] > x) {"
 +"\n"
 +"                fib  = fibM2;"
 +"\n"
 +"                fibM1 = fibM1 - fibM2;"
 +"\n"
 +"                fibM2 = fib - fibM1;"
 +"\n"
 +"            }"
 +"\n"
 +"            else return i;"
 +"\n"
 +"        }"
 +"\n"
 +"        if(fibM1 == 1 && offset+1 < arr.length && arr[offset+1] == x)"
 +"\n"
 +"            return offset + 1;"
 +"\n"
 +"\n"
 +"        return -1;"
 +"\n"
 +"    }";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}