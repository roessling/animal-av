package generators.hardware;/*
 * MFUWizard.java
 * Paul Vincent Dawadi, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.*;
import algoanim.primitives.generators.AnimationType;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;
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
import interactionsupport.models.TrueFalseQuestionModel;

@SuppressWarnings("unused")
public class MFUGenerator implements ValidatingGenerator {
    Language lang;
    private Color _farbHighlight;
    private int[] _speicherSequenz;
    private int _anzahlSeitentabellen;
    private int[] _speicher;


    public void init() {
        lang = new AnimalScript("Most frequently Used Page Replacement Algorithm", "Paul Vincent Dawadi", 800, 600);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        lang.setStepMode(true);
    }


    public boolean validateInput(AnimationPropertiesContainer var1, Hashtable<String, Object> var2){
        int anzahlSeitentabellen = (int) var2.get("anzahlSeitentabellen");
        if(anzahlSeitentabellen <1){
            throw new IllegalArgumentException("Fehler: die Anzahl an Seiten muss mindestens 1 betragen.");
        }
       return true;
    }
    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        int anzahlSeitentabellen = (int) primitives.get("anzahlSeitentabellen");
        lang.setStepMode(true);
        _speicherSequenz = (int[]) primitives.get("speicherSequenz");
        _farbHighlight = (Color) primitives.get("farbHighlight");
        _anzahlSeitentabellen = (Integer) primitives.get("anzahlSeitentabellen");
        TextProperties textProps = new TextProperties();


        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 24));
        Text header = lang.newText(new Coordinates(20, 30), "Most frequently Used Page Replacement Algorithmus",
                "header", null, textProps);


        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        Rect hRect = lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
                null, rectProps);

        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 18));
        SourceCodeProperties sourceCodeProps = new SourceCodeProperties();
        sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 16));
        sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, _farbHighlight);
//Einleitung
        lang.nextStep();
        SourceCode einleitung = lang.newSourceCode(new Coordinates(20, 75), "Einleitung",
                null, sourceCodeProps);
        einleitung.addCodeLine("Einleitende Worte", null, 0,
                null);
        einleitung.addCodeLine("Um den Algorithmus zu animieren wird im Folgenden ein Array für die Speicherzugriffe und ein Array um den Cache zur Visualisierung verwendet.", null, 0, null);
        einleitung.addCodeLine("Des Weiteren kommt eine Verwaltungstabelle zum Einsatz. In ihr wird protokolliert wie viele Zugriffe auf die einzelnen Seiten erfolgt wurden.", null, 0, null);
        einleitung.addCodeLine("So kann ermittelt werden welche Seite bei einem vollem Cache überschrieben werden sollen", null, 0, null);
        lang.nextStep("Einleitung");
        einleitung.hide();
        //TextProperties textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 18));
        Text speicherzugriffe = lang.newText(new Coordinates(20, 100), "Speicherzugriffe",
                "header", null, textProps);
//Initialisierung
        ArrayProperties arrayPropsZugriffe = new ArrayProperties();
        arrayPropsZugriffe.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        arrayPropsZugriffe.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        arrayPropsZugriffe.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        arrayPropsZugriffe.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        arrayPropsZugriffe.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        arrayPropsZugriffe.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, _farbHighlight);
        IntArray iaZugriffe = lang.newIntArray(new Coordinates(300, 100), _speicherSequenz, "intArrayZugriffe",
                null, arrayPropsZugriffe);


        TextProperties textProps2 = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 18));
        Text cache = lang.newText(new Coordinates(20, 200), "Cache",
                "header", null, textProps);


        _speicher = new int[_anzahlSeitentabellen];
        ArrayProperties arrayPropsSpeicher = new ArrayProperties();
        arrayPropsSpeicher.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        arrayPropsSpeicher.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        arrayPropsSpeicher.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        arrayPropsSpeicher.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        arrayPropsSpeicher.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        arrayPropsSpeicher.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
                Color.YELLOW);
        IntArray iaSpeicher = lang.newIntArray(new Coordinates(300, 200), _speicher, "intArraySpeicher",
                null, arrayPropsZugriffe);


        TextProperties textProps3 = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 18));
        Text seitennummer = lang.newText(new Coordinates(20, 300), "Seitennummer",
                "header", null, textProps);
        TextProperties textProps4 = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 18));
        Text zugriffe = lang.newText(new Coordinates(20, 325), "Zugriffe",
                "header", null, textProps);

        int[] anzahlZugriffe = new int[_anzahlSeitentabellen];
        int[][] ar = new int[2][_anzahlSeitentabellen];
        MatrixProperties mp = new MatrixProperties();
        mp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        IntMatrix verwaltungsMatrix = lang.newIntMatrix(new Coordinates(300, 300), ar, "intArrayVerwaltung", null, mp);
        
        String offsetArray = _speicherSequenz.length>=_anzahlSeitentabellen ? "intArrayZugriffe" : "intArraySpeicher";
        int offsetHigh = _speicherSequenz.length>=_anzahlSeitentabellen ? 0 : -80;

        Offset ofa = new Offset(100, -15+offsetHigh, offsetArray, AnimalScript.DIRECTION_E);
        Offset ofb = new Offset(100, 300+offsetHigh, offsetArray, AnimalScript.DIRECTION_SE);
        Node[] nodes = new Node[2];
        nodes[0] = ofa;
        nodes[1] = ofb;
        Polyline pl = lang.newPolyline(nodes, "trennlinie", null);

        Offset ofSH = new Offset(105, -20+offsetHigh, offsetArray, AnimalScript.DIRECTION_E);
        Text sourcecode = lang.newText(ofSH, "Sourcecode",
                "headerSC", null, textProps);


        sourceCodeProps = new SourceCodeProperties();
        sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 16));
        sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, _farbHighlight);


        Offset ofSCA = new Offset(115, 0+offsetHigh, offsetArray, AnimalScript.DIRECTION_E);

      //  SourceCode src = lang.newSourceCode(new Coordinates(675, 100), "sourceCode",
        SourceCode src = lang.newSourceCode(ofSCA, "sourceCode",
                null, sourceCodeProps);
        src.addCodeLine("bis alle Speicherzugriffe abgearbeitet sind", null, 0,
                null); // 0
        src.addCodeLine("Zugriff auf nächste Speicheradresse", null, 1, null);
        src.addCodeLine("if(Speicheradresse bereits im Cache)", null, 1, null);
        src.addCodeLine("Erhöhe Counter der Speicheradresse", null, 2, null);
        src.addCodeLine("else ", null, 1, null);
        src.addCodeLine("if(Ist noch Platz im Cache)", null, 2, null);
        src.addCodeLine("Füge Speicheradresse in Cache ein", null, 3, null);
        src.addCodeLine("Setze den Counter der Speicheradresse auf 1", null, 3, null);
        src.addCodeLine("else ", null, 2, null);
        src.addCodeLine("Ermittel die Wert mit den meisten Zuriffen", null, 3, null);
        src.addCodeLine("Ersetze den Wert in der Verwaltungstabelle", null, 3, null);
        src.addCodeLine("Setze den neuen Wert in Speichertabelle ", null, 3, null);
        src.addCodeLine("Alle Zugriffe abgearbeitet", null, 0, null);


        ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
        arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "");
        arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        arrayIMProps.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);



        Text schreibvorgänge1 = lang.newText(new Coordinates(20, 400), "Schreibvorgänge auf",
                "headerSA", null, textProps);

        Text schreibvorgänge2 = lang.newText(new Coordinates(20, 425), "den Cache",
                "headerDC", null, textProps);

        TwoValueCounter counter = lang.newCounter(iaSpeicher); // Zaehler anlegen
        CounterProperties cp = new CounterProperties(); // Zaehler-Properties anlegen
        cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // gefuellt...
        cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE); // ...mit Blau
        TwoValueView view = lang.newCounterView(counter,
                new Coordinates(300, 400), cp, true, true);


// Beginn der Animation
        lang.nextStep("Initalisierung");

// Schritt 1 Solange noch nicht alle Speicherzugriffe abgearbeitet wurden

// Variablen
        ArrayMarker iMarker = null;
        int chacheStelle = 0;
        int neueZugriffe = 0;
        int pointerCounter = 0;
        boolean speicherFrei = false;
        boolean adresseinCache = false;

        for (pointerCounter = 0; pointerCounter < _speicherSequenz.length; pointerCounter++) {
            src.unhighlight(7);
            src.highlight(0);
            if(pointerCounter==0){
                lang.nextStep("Start der Animation");
            }else {
                lang.nextStep();
            }
            src.unhighlight(0);
            src.highlight(1);
            if (pointerCounter == 0) {
                iMarker = lang.newArrayMarker(iaZugriffe, pointerCounter, "i" + pointerCounter,
                        null, arrayIMProps);
            }
            iMarker.move(pointerCounter, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
           lang.nextStep();
            src.unhighlight(1);
            src.highlight(2);
            lang.nextStep();
            adresseinCache = false;
            for (int i = 0; i < _anzahlSeitentabellen; i++) {
                if (_speicher[i] == _speicherSequenz[pointerCounter]) {
                    adresseinCache = true;
                    chacheStelle = i;
                }
            }
            if (adresseinCache) {
                src.unhighlight(2);
                src.highlight(3);
                neueZugriffe = anzahlZugriffe[chacheStelle] + 1;
                anzahlZugriffe[chacheStelle] = anzahlZugriffe[chacheStelle] + 1;
                verwaltungsMatrix.highlightCell(0, chacheStelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                verwaltungsMatrix.highlightCell(1, chacheStelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                verwaltungsMatrix.put(1, chacheStelle, neueZugriffe, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

                lang.nextStep();
                verwaltungsMatrix.unhighlightCell(0, chacheStelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                verwaltungsMatrix.unhighlightCell(1, chacheStelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                src.unhighlight(3);
            } else {
                src.unhighlight(2);
                src.highlight(4);
                lang.nextStep();
                src.unhighlight(4);
                src.highlight(5);
                lang.nextStep();
                speicherFrei = false;
                for (int i = 0; i < _anzahlSeitentabellen; i++) {
                    if (_speicher[i] == 0) {
                        _speicher[i] = _speicherSequenz[pointerCounter];
                        iaSpeicher.highlightCell(i, i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        iaSpeicher.put(i, _speicherSequenz[pointerCounter], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        speicherFrei = true;
                        src.unhighlight(5);
                        src.highlight(6);
                        lang.nextStep();
                        verwaltungsMatrix.put(0, i, _speicherSequenz[pointerCounter], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.put(1, i, 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(0, i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(1, i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        anzahlZugriffe[i] = 1;
                        src.unhighlight(6);
                        src.highlight(7);
                        lang.nextStep();
                        iaSpeicher.unhighlightCell(i, i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.unhighlightCell(0, i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.unhighlightCell(1, i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        break;
                    }

                }
                if (!speicherFrei) {

                    TrueFalseQuestionModel seitenfehler = new TrueFalseQuestionModel("seitenfehler", true, 1);
                    seitenfehler.setPrompt("Kommt es zu einem Konflikt, sodass ein Wert aus dem Cache überschrieben werden soll?");
                    seitenfehler.setCorrectAnswer(true);
                    lang.addTFQuestion(seitenfehler);

                    src.unhighlight(5);
                    src.highlight(8);

                    int hoechsteStelle = 0;
                    int hoechsterWert = 0;


                    lang.nextStep();
                    src.unhighlight(8);
                    src.highlight(9);
                    src.highlight(10);
                    for (int i = 0; i < anzahlZugriffe.length; i++) {
                        if (anzahlZugriffe[i] > hoechsterWert) {
                            hoechsterWert = anzahlZugriffe[i];
                            hoechsteStelle = i;
                        }
                    }
                    MultipleChoiceQuestionModel ersetzen = new MultipleChoiceQuestionModel("ersetzen");
                    ersetzen.setPrompt("Welche der folgenden Seiten im Cache wird überschrieben?");
                    ersetzen.addAnswer("Die Seite mit den wenigsten Zugriffen ", 0, "Falsch! Der MFU-Algorithmus ersetzt die Seiten mit dem meistem Zugriffen");
                    ersetzen.addAnswer("Die Seite mit den meisten Zugriffen  ", 1, "Richtig!");
                    ersetzen.addAnswer("Es wird zufällig eine Seite gewählt ", 0, "Falsch! Der MFU-Algorithmus ersetzt die Seiten mit dem meistem Zugriffen");
                    lang.addMCQuestion(ersetzen);

                    anzahlZugriffe[hoechsteStelle] = 1;
                    verwaltungsMatrix.put(0, hoechsteStelle, _speicherSequenz[pointerCounter], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                    verwaltungsMatrix.put(1, hoechsteStelle, 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

                    verwaltungsMatrix.highlightCell(0, hoechsteStelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                    verwaltungsMatrix.highlightCell(1, hoechsteStelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

                    lang.nextStep();

                    src.unhighlight(9);
                    src.unhighlight(10);
                    src.highlight(11);
                    iaSpeicher.highlightCell(hoechsteStelle, hoechsteStelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                    iaSpeicher.put(hoechsteStelle, _speicherSequenz[pointerCounter], Timing.INSTANTEOUS, Timing.INSTANTEOUS);

                    _speicher[hoechsteStelle] = _speicherSequenz[pointerCounter];
                    lang.nextStep();
                    src.highlight(11);
                    src.unhighlight(11);
                    iaSpeicher.unhighlightCell(hoechsteStelle, hoechsteStelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                    verwaltungsMatrix.unhighlightCell(0, hoechsteStelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                    verwaltungsMatrix.unhighlightCell(1, hoechsteStelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                }
            }
        }
        lang.nextStep();
        src.highlight(12);
        src.unhighlight(0);
        src.unhighlight(1);
        src.unhighlight(2);
        src.unhighlight(3);
        src.unhighlight(4);
        src.unhighlight(5);
        src.unhighlight(6);
        src.unhighlight(7);
        src.unhighlight(8);
        src.unhighlight(9);
        src.unhighlight(10);
        src.unhighlight(11);

        MultipleChoiceQuestionModel effizient = new MultipleChoiceQuestionModel("einsatz");
        effizient.setPrompt("Wann ist der MFU-Algorithmus effizient?");
        effizient.addAnswer("Wenn die Speicherzugriffe über die Seiten gleich verteilt sind zB. 123 456 789 ", 0, "Falsch! Der MFU-Algorithmus ist dann effizient wenn die Zugriffe blockweise erfolgen");
        effizient.addAnswer("Wenn die Speicherzugriffe auf Seiten Blockweise sind zB. 111 222 333  ", 1, "Richtig!");
        effizient.addAnswer("Speicherzugriffe auf bestimmte Seiten erscheinen immer wieder z.B 111 223 111 ", 0, "Falsch! Der MFU-Algorithmus ist dann effizient wenn die Zugriffe blockweise erfolgen");
        lang.addMCQuestion(effizient);

        lang.nextStep();
        src.unhighlight(12);
        iaSpeicher.hide();
        iaZugriffe.hide();
        verwaltungsMatrix.hide();
        pl.hide();
        src.hide();
        iMarker.hide();
        view.hide();
        speicherzugriffe.hide();
        cache.hide();
        schreibvorgänge1.hide();
        schreibvorgänge2.hide();
        sourcecode.hide();
        seitennummer.hide();
        zugriffe.hide();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 22));
        Text fazit = lang.newText(new Coordinates(20, 100), "Fazit",
                "header", null, textProps);
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 18));
        Text ergbniss = lang.newText(new Coordinates(20, 150), "Anzahl an Durchläufe:" + counter.getAssigments(),
                "header", null, textProps);

        sourceCodeProps = new SourceCodeProperties();
        sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 16));
        sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, _farbHighlight);

        SourceCode fazitSrc = lang.newSourceCode(new Coordinates(20, 175), "Einleitung",
                null, sourceCodeProps);
        fazitSrc.addCodeLine("Bei diesem Durchgang musste " + counter.getAssigments() + " mal eine Seite nachgeladen werden, da diese sich nicht im Cache befindet.", null, 0, null);
        fazitSrc.addCodeLine("Da die Effizienz eines Page-Replacement sehr davon abhängt in welcher Reihenfolge die Seitenzugriffe abfolgen, sollte versucht werden hierdrüber eine Annahme zu erstellen.", null, 0, null);
        fazitSrc.addCodeLine("Je nach Annahme sollte der gewünschte Page-Replacement-Algorithmus gewählt werden.", null, 0, null);
        fazitSrc.addCodeLine("", null, 0, null);
        fazitSrc.addCodeLine("Der MFU-Algorithmus ist dann effizient wenn die Zugriffe blockweise erfolgen. ", null, 0, null);
        fazitSrc.addCodeLine("", null, 0, null);
        fazitSrc.addCodeLine("Bei diesem Beispiel ist der MFU-Algorithmus optimal: ", null, 0, null);
        fazitSrc.addCodeLine("Sequenz: 111112344444244344523  ", null, 0, null);
        fazitSrc.addCodeLine("Platz im Chache: 3 ", null, 0, null);
        fazitSrc.addCodeLine("Ergbnis: es müssen nur 5 mal Werte nachgeladen werden. ", null, 0, null);
        lang.nextStep("Fazit");
        lang.finalizeGeneration();
        return lang.toString();
    }

    public String getName() {
        return "Most frequently Used Page Replacement Algorithm";
    }

    public String getAlgorithmName() {
        return "Most frequently Used Page Replacement Algorithm";
    }

    public String getAnimationAuthor() {
        return "Paul Vincent Dawadi";
    }

    public String getDescription() {
        return
                "Der MFU-Algorithmus ist ein Page-Replacement-Algorithmus.\n" +
                        "Das Betrienssystem speichert im Arbeitsspeicher Seiten des Adressbereiches.\n" +
                        "Häufig befinden sich die benötigten Seiten aber nicht in Frames, dann kommt es zu einem Seitenfehler.\n" +
                        "Die benötigten Seite muss dann in dem Speicher geladen werden.\n" +
                        "Ist der Arbeitsspeicher aber voll dann muss eine andere Seite aus dem Arbeitsspeicher überschrieben werden.\n" +
                        "Ein Page-Replacement-Algorithmus versucht hier nun algorithmisch zu entscheden welche Seite überschrieben, werden soll.\n" +
                        "\n" +
                        "Der MFU-Algorithmus basiert auf der Idee, dass die Seite mit wenigen Zugriffen wahrscheinlich gerade erst eingeführt wurde" +
                        "und vor der Beendigung noch mehrmals verwendet wird. \n" +
                        "Er löscht dementsprechend immer den Eintrag mit den meisten Zugriffen, da hier die Wahrscheinlichkeit am größten ist,\n" +
                        "dass der Zugriff bereits beendet wurde";
    }

    public String getCodeExample() {
        return "bis alle Speicherzugriffe abgearbeitet sind\n" +
                "   Zugriff auf nächste Speicheradresse\"\n" +
                "   if(Speicheradresse bereits im Cache)\"\n" +

                "       Erhöhe Counter der Speicheradresse\"\n" +
                "   else\n" +
                "       if(Ist noch Platz im Cache)\n" +
                "           Füge Speicheradresse in Cache ein\n" +
                "           Setze den Counter der Speicheradresse auf 1\n" +
                "       else \n" +
                "           Ermittel die Wert mit den meisten Zuriffen\n" +
                "           Ersetze den Wert in der Verwaltungstabelle\n" +
                "           Setze den neuen Wert in Speichertabelle\n" +
                "Alle Zugriffe abgearbeitet";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    public void doMFU() {
        int[][] a = new int[10][10];
        MatrixProperties mp = new MatrixProperties();
        IntMatrix im = lang.newIntMatrix(new Coordinates(20, 100), a, "intArray", null, mp);

        lang.nextStep();
    }

}