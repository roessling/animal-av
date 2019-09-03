package generators.sorting;

import algoanim.primitives.*;
import algoanim.properties.*;
import algoanim.util.*;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import interactionsupport.models.MultipleChoiceQuestionModel;

@SuppressWarnings("unused")
public class SLPGenerator implements ValidatingGenerator {
    Language lang;
    private Color _farbHighlight;
    private int[] _threads;
    private Locale loc;


    public void init() {
        lang = new AnimalScript("Sleepsort", "Michael Piepel", 800, 600);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        lang.setStepMode(true);
    }

    public boolean validateInput(AnimationPropertiesContainer var1, Hashtable<String, Object> var2) {
        int[] inputArray = (int[]) var2.get("Threads");
        for (int i = 0; i < inputArray.length; i++) {
            if (inputArray[i] < 0) {
                if (loc == Locale.GERMANY) {
                    throw new IllegalArgumentException("Fehler: Die Array Elemente duerfen keine negativen Zahlen beinhalten.");
                }
                else{
                    throw new IllegalArgumentException("Error: The array elements should not contain negative numbers.");
                }
            }
        }
        return true;
    }


    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        lang.setStepMode(true);
        _threads = (int[]) primitives.get("Threads");
        _farbHighlight = (Color) primitives.get("farbHighlight");

        TextProperties textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
        Text header = lang.newText(new Coordinates(20, 30), "Sleepsort", "header", null, textProps);

        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        Rect hRect = lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect", null, rectProps);

        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 18));
        SourceCodeProperties sourceCodeProps = new SourceCodeProperties();
        sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
        sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, _farbHighlight);
        //Einleitung
        lang.nextStep();
        if (loc == Locale.GERMANY) {
            SourceCode einleitung = lang.newSourceCode(new Coordinates(20, 75), "Einleitung", null, sourceCodeProps);
            einleitung.addCodeLine("Einleitung", null, 0, null);
            einleitung.addCodeLine("Beim Sleepsort wird fuer jedes Element in dem Array ein seperater Thread gestartet. Zunaechst werden alle Threads", null, 0, null);
            einleitung.addCodeLine("gestartet und direkt schlafen gelegt. Das Element in dem Array wird mal der definierten Zeit im Sleepsort multipliziert. ", null, 0, null);
            einleitung.addCodeLine("In diesem Beispiel wird das Element des Arrays mal einer Sekunde multipliziert. Des Weiteren wird hier CountDownLatch verwendet.", null, 0, null);
            einleitung.addCodeLine("Das bedeutet, ein oder mehrere Threads warten auf eine Bedingung, wenn sie erfuellt ist, koennen die Threads fortfahren. ", null, 0, null);
            einleitung.addCodeLine("Die Funktion .await() blockiert Threads und .countDown() ermoeglicht einzelnd Threads laufen zu lassen. ", null, 0, null);
            lang.nextStep("Einleitung");
            einleitung.hide();
        } else{
            SourceCode einleitung = lang.newSourceCode(new Coordinates(20, 75), "Introduction", null, sourceCodeProps);
            einleitung.addCodeLine("Introduction", null, 0, null);
            einleitung.addCodeLine("The sleepsort algorithm starts with initializing an thread for each element of the array. After starting the threads,", null, 0, null);
            einleitung.addCodeLine(" they are immediately sent asleep. The element in the array is multiplied by the defined time in the sleepsort.", null, 0, null);
            einleitung.addCodeLine("In this example, the element of the array is multiplied by one second. Furthermore, CountDownLatch is used,", null, 0, null);
            einleitung.addCodeLine("which contains certain conditions. The .await () function blocks threads and .countDown () allows", null, 0, null);
            einleitung.addCodeLine("threads to run individually. ", null, 0, null);
            lang.nextStep("Introduction");
            einleitung.hide();
        }

        //TextProperties textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 18));
        Text sekunden = lang.newText(new Coordinates(660, 100), (loc == Locale.GERMANY) ? "Zeit in Sekunden" : "Time in Seconds", "header", null, textProps);
        Text threads = lang.newText(new Coordinates(660, 150), "Threads", "header", null, textProps);
        Text konsole = lang.newText(new Coordinates(660, 250), (loc == Locale.GERMANY) ? "Konsole" : "Console","header", null, textProps);
        //Initialisierung
        ArrayProperties arrayPropsZugriffe = new ArrayProperties();
        arrayPropsZugriffe.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        arrayPropsZugriffe.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        arrayPropsZugriffe.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        arrayPropsZugriffe.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        arrayPropsZugriffe.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        arrayPropsZugriffe.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, _farbHighlight);
        IntArray ia = lang.newIntArray(new Coordinates(950, 150), _threads, "intArray", null, arrayPropsZugriffe);

        TextProperties textProps2 = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 18));

        Coordinates a = new Coordinates(650, 75);
        Coordinates b = new Coordinates(650, 650);
        Node[] nodes = new Node[2];
        nodes[0] = a;
        nodes[1] = b;
        Polyline pl = lang.newPolyline(nodes, "trennlinie", null);

        Text sourcecode = lang.newText(new Coordinates(40, 100), "Sourcecode", "header", null, textProps);

        sourceCodeProps = new SourceCodeProperties();
        sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, _farbHighlight);

        SourceCode src = lang.newSourceCode(new Coordinates(40, 140), "sourceCode", null, sourceCodeProps);
        // Add the lines to the SourceCode object.
        // Line, name, indentation, display dealy
        src.addCodeLine("public void SleepSort(int[] array)", null, 0, null); // 0
        src.addCodeLine("{", null, 0, null);
        src.addCodeLine("final CountDownLatch doneSignal = new CountDownLatch(array.length);", null, 1, null);
        src.addCodeLine("for (final int num : array)", null, 1, null); // 3
        src.addCodeLine("{", null, 1, null); // 4
        src.addCodeLine("new Thread(new Runnable()", null, 2, null); // 5
        src.addCodeLine("{", null, 2, null); // 6
        src.addCodeLine("public void run()", null, 2, null); // 7
        src.addCodeLine("{", null, 3, null); // 8
        src.addCodeLine("doneSignal.countDown();", null, 4, null); // 9
        src.addCodeLine("try", null, 3, null); // 10
        src.addCodeLine("{", null, 4, null); // 11
        src.addCodeLine("doneSignal.await();", null, 3, null); // 12
        src.addCodeLine("Thread.sleep(num * 1000);", null, 4, null); // 13
        src.addCodeLine("System.out.println(num);", null, 4, null); // 13
        src.addCodeLine("} catch (InterruptedException e) ", null, 2, null); // 14
        src.addCodeLine("{", null, 2, null); // 15
        src.addCodeLine("e.printStackTrace();", null, 3, null); // 16
        src.addCodeLine("}", null, 2, null); // 17
        src.addCodeLine("}", null, 3, null); // 18
        src.addCodeLine("}).start();", null, 2, null); // 19
        src.addCodeLine("}", null, 2, null); // 20
        src.addCodeLine("}", null, 2, null); // 21

        ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
        arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "");
        arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        arrayIMProps.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);

// Beginn der Animation
        lang.nextStep("Initalisierung");
        src.highlight(0, 0, false);
        lang.nextStep();
        src.toggleHighlight(0, 0, false, 2, 0);
        lang.nextStep();

        src.unhighlight(2, 0, false);
        src.highlight(3, 0, false);
        lang.nextStep();
        src.unhighlight(3, 0, false);

        TextProperties textPropsa = new TextProperties();
        textPropsa.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));

        for (int j = 1; j <= ia.getLength(); j++) {
            src.highlight(5, 0, false);
            ia.highlightCell(j - 1, (Timing) null, new TicksTiming(10));
            Text t1 = lang.newText(new Coordinates(660, 200), (loc == Locale.GERMANY) ? "" + j + ". Thread wird gestartet und  legt sich " + ia.getData(j - 1) + "  Sekunden schlafen..." : "" + j + ". Thread starts and lay down " + ia.getData(j - 1) + " Seconds to sleep...", "header", null, textPropsa);
            lang.nextStep();
            src.unhighlight(5, 0, false);
            src.highlight(7, 0, false);
            lang.nextStep();
            src.unhighlight(7, 0, false);
            src.highlight(9, 0, false);
            lang.nextStep();
            src.unhighlight(9, 0, false);
            src.highlight(10, 0, false);
            lang.nextStep();
            src.unhighlight(10, 0, false);
            src.highlight(12, 0, false);
            lang.nextStep();
            src.unhighlight(12, 0, false);
            src.highlight(13, 0, false);
            lang.nextStep();
            src.unhighlight(13, 0, false);
            src.highlight(20, 0, false);
            lang.nextStep();
            src.unhighlight(20, 0, false);
            src.highlight(5, 0, false);
            t1.hide();
        }
        src.unhighlight(5, 0, false);
        if (loc == Locale.GERMANY) {
            MultipleChoiceQuestionModel effizient = new MultipleChoiceQuestionModel("einsatz");
            effizient.setPrompt("Wann wird ein Thread mit dem Wert 2 gestartet. Wenn ein Thread Wert*1000ms schlaeft?");
            effizient.addAnswer("1 Sekunde ", 0, "Falsch! Der Array Wert wird mit der Zeit multipliziert -> 2*1000ms");
            effizient.addAnswer("2 Sekunden ", 1, "Richtig!");
            effizient.addAnswer("3 Sekunden ", 0, "Falsch! Der Array Wert wird mit der Zeit multipliziert -> 2*1000ms");
            lang.addMCQuestion(effizient);
        }else{
            MultipleChoiceQuestionModel effizient = new MultipleChoiceQuestionModel("einsatz");
            effizient.setPrompt("When will a thread with the value 2 be started? If a thread sleeps value * 1000ms?");
            effizient.addAnswer("1 Seconds ", 0, "Wrong! The array value is multiplied by the time -> 2*1000ms");
            effizient.addAnswer("2 Seconds ", 1, "Right!");
            effizient.addAnswer("3 Seconds ", 0, "Wrong! The array value is multiplied by the time -> 2*1000ms");
            lang.addMCQuestion(effizient);
        }
        lang.nextStep();

        int[] q = new int[ia.getLength()];
        for (int i = 0; i < q.length; i++) {
            q[i] = ia.getData(i);
        }
        for (int i = 0; i < q.length - 1; i++) {
            for (int j = i + 1; j < q.length; j++) {
                if (q[i] > q[j]) {
                    int temp = q[i];
                    q[i] = q[j];
                    q[j] = temp;
                }
            }
        }
        int sek = 0;

        Text kk = lang.newText(new Coordinates(660, 270), " >>", "header", null, textPropsa);


        for (int i = 0; i < ia.getLength(); i++) {
            src.highlight(13, 0, false);
            Text k2 = lang.newText(new Coordinates(950, 100), " " + sek + " ", "header", null, textProps);


            if (q[i] == sek) {
                src.unhighlight(13, 0, false);
                String s = kk.getText();
                s = s + " " + q[i] + " ";
                kk.setText(s, null, null);
                src.highlight(14, 0, false);


            } else {
                i--;
            }
            sek++;

            lang.nextStep();
            src.unhighlight(14, 0, false);
            k2.hide();
        }
        src.unhighlight(13, 0, false);


        src.unhighlight(14);

        lang.nextStep();              // alles verstecken -> Fazit
        pl.hide();
        src.hide();
        sourcecode.hide();
        ia.hide();
        threads.hide();
        konsole.hide();
        sekunden.hide();
        kk.hide();

        if (loc == Locale.GERMANY) {
            textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 22));
            Text fazit = lang.newText(new Coordinates(20, 100), "Fazit", "header", null, textProps);
            textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 18));

            sourceCodeProps = new SourceCodeProperties();
            sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
            sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, _farbHighlight);

            SourceCode fazitSrc = lang.newSourceCode(new Coordinates(20, 175), "Einleitung", null, sourceCodeProps);
            fazitSrc.addCodeLine("Die Laufzeit haengt von der hoechsten Zahl im Array ab und ist daher nicht Effizent. Des Weiteren erlaubt der Algorithmus keine negativen Integer Zahlen. ", null, 0, null);
            lang.nextStep("Fazit");
        } else{
            textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 22));
            Text fazit = lang.newText(new Coordinates(20, 100), "Conclusion", "header", null, textProps);
            textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 18));

            sourceCodeProps = new SourceCodeProperties();
            sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
            sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, _farbHighlight);

            SourceCode fazitSrc = lang.newSourceCode(new Coordinates(20, 175), "Einleitung", null, sourceCodeProps);
            fazitSrc.addCodeLine("The runtime depends on the highest number in the array and is therefore not Efficient. Furthermore, the algorithm does not allow negative integer numbers. ", null, 0, null);
            lang.nextStep("Conclusion");
        }
        lang.finalizeGeneration();
        return lang.toString();
    }

    public String getName() {
        return "Sleepsort";
    }

    public String getAlgorithmName() {
        return "Sleepsort";
    }

    public String getAnimationAuthor() {
        return "Michael Piepel";
    }

    public String getDescription() {
        if (loc == Locale.GERMANY) {
            return "Der Sleepsort Algorithmus ist ein Algorithmus der Threads verwendet.\n" + "In diesem Algorithmus erstellen wir Threads fuer jedes der Elemente im Eingabe-Array und dann schlaeft jeder \n" + "Thread fuer eine Zeitspanne, die proportional zum Wert des entsprechenden Array-Elements ist.\n" + "Daher wacht der Thread mit der geringsten Schlafzeit zuerst auf \n" + "Das groesste Element wacht nach einer langen Zeit auf und dann wird das Element zuletzt ausgegeben. Daher ist die Ausgabe eine sortierte Ausgabe.\n" + "All dieser Multithreading-Prozess findet im Hintergrund und im Kern des Betriebssystems statt.\n" + "\n" + "Wir erfahren nichts darueber, was im Hintergrund passiert, daher ist dies ein \"mysterioeser\" Sortieralgorithmus";
        } else {
            return "In general the sleep sort algorithm is an algorithm used by threads. In this algorithm we create threads for each element in the input array and then every thread sleeps. The sleeping time of the array depends value of the element in the array. Corresponding to that, the thread with the shortest value wakes up as first. Therefore, the output is sorted. The whole multithreading process takes place in the background and at the core of the operating system.\n\n We do not know what is going on in the background, so this is a mysterious sorting algorithm.";
        }
    }


    public String getCodeExample() {
        return "public void sleepSortAndPrint(int[] array)\n" + "   final CountDownLatch doneSignal = new CountDownLatch(array.length)\"\n" + "   for (final int num : array)\"\n" + "       new Thread(new Runnable();\"\n" + "           public void run()\n" + "               doneSignal.countDown();\n" + "               try\n" + "                   doneSignal.await();\n" + "                   Thread.sleep(num * 1000); \n" + "                   System.out.println(num) \n" + "               catch (InterruptedException e)\n" + "                   e.printStackTrace()\n" + "       .start()\n";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return loc;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    public SLPGenerator(Locale locale) {
        loc = locale;
    }
    public SLPGenerator() {
        loc = Locale.GERMANY;
    }

}