package generators.hardware;/*
 * generators.hardware.NRUGenerator.java
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

@SuppressWarnings("unused")
public class NRUGenerator  implements ValidatingGenerator {
    private Language lang;
    private Color highlight;
    private int[][] speicherSequenz;
    private int anzahlSeiten;

    public boolean validateInput(AnimationPropertiesContainer var1, Hashtable<String, Object> var2){
        speicherSequenz = (int[][]) var2.get("speicherSequenz");
        anzahlSeiten = (Integer) var2.get("anzahlSeiten");

        if(anzahlSeiten <1){
            throw new IllegalArgumentException("Fehler: die Anzahl an Seiten muss mindestens 1 betragen.");
        }

        for(int i =0; i <speicherSequenz[1].length;i++){
            if(speicherSequenz[1][i]!=1&&speicherSequenz[1][i]!=0){
                System.out.println("Fail_1");
                throw new IllegalArgumentException("Fehler: Seitenzugriffe kann entweder lesend oder nicht lesend sein." +
                        "Der Wert muss 1 oder 0 sein.");
            }
        }
        for(int i =0; i <speicherSequenz[2].length;i++) {
            if (speicherSequenz[2][i] != 1 && speicherSequenz[2][i] != 0) {
                System.out.println("Fail_2");
                throw new IllegalArgumentException("Fehler: Seitenzugriffe kann entweder schreibend oder nicht schreibend sein." +
                        "Der Wert muss 1 oder 0 sein.");
            }
        }
        return true;
        }

    public void init() {
        lang = new AnimalScript("The Not Recently Used Page Replacement Algorithm", "Paul Vincent Dawadi", 800, 600);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

        highlight = (Color) primitives.get("highlight");
        speicherSequenz = (int[][]) primitives.get("speicherSequenz");
        anzahlSeiten = (Integer) primitives.get("anzahlSeiten");
// Überschrifft
        lang.setStepMode(true);
        TextProperties textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 24));
        Text header = lang.newText(new Coordinates(20, 30), "Not Recently Used Page Replacement Algorithmus",
                "header", null, textProps);
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        Rect hRect = lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
                null, rectProps);
        lang.nextStep();
//Einleitung
        SourceCodeProperties sourceCodeProps = new SourceCodeProperties();
        sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 16));
        sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlight);
        SourceCode einleitung = lang.newSourceCode(new Coordinates(20, 75), "Einleitung",
                null, sourceCodeProps);
        einleitung.addCodeLine("Einleitende Worte", null, 0,
                null);
        einleitung.addCodeLine("Um den Algorithmus zu animieren werden olgende Visualisierungen verwendet:", null, 0, null);
        einleitung.addCodeLine("Eine Zugriffstabelle, in der nacheinander die Seitenzugriffe aufgelistet werden. Zusätzlich wird angezeigt ob der Zugriff schreibend oder lesend erfolgt.", null, 0, null);
        einleitung.addCodeLine("Ein Cache-Array indem die Seiten angezeigt werden, die sich im Cache befinden.", null, 0, null);
        einleitung.addCodeLine("Eine Verwaltungtabelle, in der die Prioritäten der Seiten im Cache verwaltet werden.", null, 0, null);
        einleitung.addCodeLine("Zugriffe beinhalten neben der Seitennummer bei diesem Verfahren auch die Information," +
                " ob diese Zugriffe schreibend oder lesend oder weder noch auf dem Speicher erfolgen.", null, 0, null);
        einleitung.addCodeLine("", null, 0, null);
        einleitung.addCodeLine("Die Zugriffstabelle gliedert sich fogendermaßen:", null, 0, null);
        einleitung.addCodeLine("    In der ersten Zeile befindet sich die Seitennummer.", null, 0, null);
        einleitung.addCodeLine("    In der zweiten Zeile steht, ob ein Wert lesend oder nicht lesend ist, dabei gilt: 1 = lesend & 0 = nicht lesend", null, 0, null);
        einleitung.addCodeLine("    In der dritten Zeile stegt, ob ein Wert schreibend oder nicht schreibend ist, dabei gilt: 1 = schreibend & 0 = nicht schreibend", null, 0, null);
        einleitung.addCodeLine("", null, 0, null);
        einleitung.addCodeLine("Prioritäten der Zugriffsarten:", null, 0, null);
        einleitung.addCodeLine("1: lesend = 0; schreibend = 0 ", null, 0, null);
        einleitung.addCodeLine("2: lesend = 0; schreibend = 1 ", null, 0, null);
        einleitung.addCodeLine("3: lesend = 1; schreibend = 0 ", null, 0, null);
        einleitung.addCodeLine("4: lesend = 1; schreibend = 1 ", null, 0, null);
        einleitung.addCodeLine("", null, 0, null);
        einleitung.addCodeLine("Seiten mit geringen Priroritäten werden ersetzt, um Platz für neue Seiten zu machen.", null, 0, null);
        einleitung.addCodeLine("", null, 0, null);




// Initalisierung der visuellen Elemente
        lang.nextStep("Einleitung");
        einleitung.hide();

        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 18));
        Text speicherzugriffe = lang.newText(new Coordinates(20, 100), "Speicherzugriffe",
                "header", null, textProps);
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 18));
        Text speicherzugriffelesend = lang.newText(new Coordinates(20, 125), "lesend",
                "header", null, textProps);
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 18));
        Text speicherzugriffeschreibend = lang.newText(new Coordinates(20, 150), "schreibend",
                "header", null, textProps);

        MatrixProperties mp = new MatrixProperties();
        mp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        IntMatrix zugriffsMatrix = lang.newIntMatrix(new Coordinates(300, 100), speicherSequenz, "intArrayZugriffe", null, mp);


        TextProperties textProps2 = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 18));
        Text cacheText = lang.newText(new Coordinates(20, 250), "Cache",
                "header", null, textProps);

        int[] cache = new int[anzahlSeiten];
        ArrayProperties arrayPropsChache = new ArrayProperties();
        arrayPropsChache.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        arrayPropsChache.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        arrayPropsChache.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        arrayPropsChache.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        arrayPropsChache.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, highlight);
        arrayPropsChache.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
                Color.YELLOW);
        IntArray iaCache = lang.newIntArray(new Coordinates(300, 250), cache, "intArray",
                null, arrayPropsChache);


        TextProperties textProps3 = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 18));
        Text seitennummer = lang.newText(new Coordinates(20, 350), "Seitennummer",
                "header", null, textProps);
        TextProperties textProps4 = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 18));
        Text priorität = lang.newText(new Coordinates(20, 375), "Priorität",
                "header", null, textProps);

        int[][] verwaltungsArray = new int[2][anzahlSeiten];
        MatrixProperties mpV = new MatrixProperties();
        mpV.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        IntMatrix verwaltungsMatrix = lang.newIntMatrix(new Coordinates(300, 350), verwaltungsArray, "intArray", null, mpV);

        String offsetArray = speicherSequenz[0].length>=anzahlSeiten ? "intArrayZugriffe" : "intArray";
        int offsetHigh = speicherSequenz[0].length>=anzahlSeiten ? 0 : -100;

        Coordinates a = new Coordinates(650, 75);
        Coordinates b = new Coordinates(650, 500);
        Offset ofa = new Offset(100, -35+offsetHigh, offsetArray, AnimalScript.DIRECTION_E);
        Offset ofb = new Offset(100, 300+offsetHigh, offsetArray, AnimalScript.DIRECTION_SE);
        Node[] nodes = new Node[2];
        nodes[0] = ofa;
        nodes[1] = ofb;
        Polyline trennlinie = lang.newPolyline(nodes, "trennlinie", null);

        Offset ofSH = new Offset(105, -40+offsetHigh, offsetArray, AnimalScript.DIRECTION_E);
        Text sourcecode = lang.newText(ofSH, "Sourcecode",
                "header", null, textProps);

        Offset ofSC = new Offset(115, -15+offsetHigh, offsetArray, AnimalScript.DIRECTION_E);
        sourceCodeProps = new SourceCodeProperties();
        sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 16));
        sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlight);

        SourceCode src = lang.newSourceCode(ofSC, "sourceCode",
                null, sourceCodeProps);
        src.addCodeLine("bis alle Speicherzugriffe abgearbeitet sind", null, 0,
                null); // 0
        src.addCodeLine("Zugriff auf nächste Speicheradresse", null, 1, null);
        src.addCodeLine("if(Speicheradresse bereits im Cache)", null, 2, null);
        src.addCodeLine("Priorität aktualisieren", null, 3, null);
        src.addCodeLine("else ", null, 2, null);
        src.addCodeLine("if(Ist noch Platz im Cache) ", null, 3, null);
        src.addCodeLine("Füge Speicheradresse in Cache ein", null, 4, null);
        src.addCodeLine("Setze den Priorität für die Seite", null, 4, null);
        src.addCodeLine("else ", null, 3, null);
        src.addCodeLine("if(Seite mit Priorität == 1 enthalten )", null, 4, null);
        src.addCodeLine("Ersetze beliebige Seite mit dem Priorität 1", null, 5, null);
        src.addCodeLine("else if(Seite mit Priorität == 2 enthalten )", null, 4, null);
        src.addCodeLine("Ersetze beliebige Seite mit dem Priorität 2", null, 5, null);
        src.addCodeLine("else if(Seite mit Priorität == 3 enthalten )", null, 4, null);
        src.addCodeLine("Ersetze beliebige Seite mit dem Priorität 3", null, 5, null);
        src.addCodeLine("else ", null, 4, null);
        src.addCodeLine("Ersetze beliebige Seite ", null, 5, null);
        src.addCodeLine("Alle Zugriffe abgearbeitet", null, 0, null);

        Text zugriffe1 = lang.newText(new Coordinates(20, 475), "Zugriffe auf",
                "header", null, textProps);

        Text zugriffe2 = lang.newText(new Coordinates(20, 500), "den Cache",
                "header", null, textProps);
        TwoValueCounter counter = lang.newCounter(iaCache); // Zaehler anlegen
        CounterProperties cp = new CounterProperties(); // Zaehler-Properties anlegen
        cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // gefuellt...
        cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE); // ...mit Blau
        TwoValueView view = lang.newCounterView(counter,
                new Coordinates(300, 475), cp, true, true);

        lang.nextStep("Initialisierung");


        int chacheStelle = 0;
        int neueZugriffe = 0;
        boolean speicherFrei = false;
        boolean adresseinCache = false;
        int pointerCounter = 0;

        for (pointerCounter = 0; pointerCounter < speicherSequenz[0].length; pointerCounter++) {
            if (pointerCounter == 0) {
                zugriffsMatrix.highlightCell(0, 0, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                zugriffsMatrix.highlightCell(1, 0, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                zugriffsMatrix.highlightCell(2, 0, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
            } else {
                zugriffsMatrix.highlightCell(0, pointerCounter, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                zugriffsMatrix.highlightCell(1, pointerCounter, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                zugriffsMatrix.highlightCell(2, pointerCounter, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                zugriffsMatrix.unhighlightCell(0, pointerCounter - 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                zugriffsMatrix.unhighlightCell(1, pointerCounter - 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                zugriffsMatrix.unhighlightCell(2, pointerCounter - 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
            }

            src.highlight(0);
            src.unhighlight(3);
            src.unhighlight(4);
            src.unhighlight(7);
            if(pointerCounter==0){
                lang.nextStep("Start der Animation");
            }else {
                lang.nextStep();
            }
            src.unhighlight(0);
            src.highlight(1);
            lang.nextStep();
            src.unhighlight(1);
            src.highlight(2);

            boolean imCache = enthält(cache, speicherSequenz[0][pointerCounter]);
// Wenn der Wert bereits im Cache ist
            if (imCache) {
                lang.nextStep();
                src.unhighlight(2);
                src.highlight(3);
                int stelle = getStelle(cache, speicherSequenz[0][pointerCounter]);
                int alterZustand = verwaltungsMatrix.getElement(1, stelle);
                if (alterZustand == 1) {
                    if (speicherSequenz[1][pointerCounter] == 1 && speicherSequenz[2][pointerCounter] == 1) {
                        verwaltungsArray[1][stelle] = 4;
                        verwaltungsMatrix.put(1, stelle, 4, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(0, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(1, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                    }
                    if (speicherSequenz[1][pointerCounter] == 1 && speicherSequenz[2][pointerCounter] == 0) {
                        verwaltungsArray[1][stelle] = 3;
                        verwaltungsMatrix.put(1, stelle, 3, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(0, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(1, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                    }
                    if (speicherSequenz[1][pointerCounter] == 1 && speicherSequenz[2][pointerCounter] == 1) {
                        verwaltungsArray[1][stelle] = 2;
                        verwaltungsMatrix.put(1, stelle, 2, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(0, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(1, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                    }
                } else if (alterZustand == 2) {
                    if (speicherSequenz[1][pointerCounter] == 1 && speicherSequenz[2][pointerCounter] == 1) {
                        verwaltungsArray[1][stelle] = 4;
                        verwaltungsMatrix.put(1, stelle, 4, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(0, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(1, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                    }
                    if (speicherSequenz[1][pointerCounter] == 1 && speicherSequenz[2][pointerCounter] == 0) {

                        MultipleChoiceQuestionModel aktualisieren = new MultipleChoiceQuestionModel("aktualisieren");
                        aktualisieren.setPrompt("Wie verändert sich die Priorität?");
                        aktualisieren.addAnswer("Sie erhöht sich auf 3", 0, "Falsch! Der MFU-Algorithmus ist dann effizient wenn auf Seiten mit höheren Prioritäten öfter zugegriffen wird");
                        aktualisieren.addAnswer("Sie erhöht sich auf 4", 1, "Richtig!");
                        aktualisieren.addAnswer("Sie bleibt unverändert", 0, "Falsch! Der MFU-Algorithmus ist dann effizient wenn auf Seiten mit höheren Prioritäten öfter zugegriffen wird");
                        lang.addMCQuestion(aktualisieren);
                        lang.nextStep();
                        verwaltungsArray[1][stelle] = 4;
                        verwaltungsMatrix.put(1, stelle, 4, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(0, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(1, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                    } else {
                        verwaltungsArray[1][stelle] = 2;
                        verwaltungsMatrix.put(1, stelle, 2, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(0, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(1, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                    }
                } else if (alterZustand == 3) {
                    if (speicherSequenz[1][pointerCounter] == 1 && speicherSequenz[2][pointerCounter] == 1) {
                        verwaltungsArray[1][stelle] = 4;
                        verwaltungsMatrix.put(1, stelle, 4, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(0, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(1, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                    }
                    if (speicherSequenz[1][pointerCounter] == 0 && speicherSequenz[2][pointerCounter] == 1) {
                        MultipleChoiceQuestionModel aktualisieren = new MultipleChoiceQuestionModel("aktualisieren");
                        aktualisieren.setPrompt("Wie verändert sich die Priorität?");
                        aktualisieren.addAnswer("Sie erhöht sich auf 3", 0, "Falsch! Der MFU-Algorithmus ist dann effizient wenn auf Seiten mit höheren Prioritäten öfter zugegriffen wird");
                        aktualisieren.addAnswer("Sie erhöht sich auf 4", 1, "Richtig!");
                        aktualisieren.addAnswer("Sie bleibt unverändert", 0, "Falsch! Der MFU-Algorithmus ist dann effizient wenn auf Seiten mit höheren Prioritäten öfter zugegriffen wird");
                        lang.addMCQuestion(aktualisieren);
                        lang.nextStep();
                        verwaltungsArray[1][stelle] = 4;
                        verwaltungsMatrix.put(1, stelle, 4, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(0, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(1, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                    } else {
                        verwaltungsArray[1][stelle] = 3;
                        verwaltungsMatrix.put(1, stelle, 3, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(0, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(1, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                    }
                }
                lang.nextStep();
                verwaltungsMatrix.unhighlightCell(0, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                verwaltungsMatrix.unhighlightCell(1, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
            }

// Wenn der Wert noch nicht im Cache ist
            else {
                lang.nextStep();
                src.unhighlight(2);
                src.unhighlight(3);
                src.highlight(4);
                lang.nextStep();
                src.unhighlight(4);
                src.highlight(5);
                boolean cacheFrei = false;
                lang.nextStep();
                src.unhighlight(5);

                for (int i = 0; i < anzahlSeiten; i++) {
                    src.unhighlight(4);
                    src.highlight(5);
                    if (cache[i] == 0) {

                        cache[i] = speicherSequenz[0][pointerCounter];
                        iaCache.highlightCell(i, i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        iaCache.put(i, speicherSequenz[0][pointerCounter], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        cacheFrei = true;
                        src.unhighlight(5);
                        src.highlight(6);
                        lang.nextStep();
                        verwaltungsArray[0][i] = speicherSequenz[0][pointerCounter];
                        verwaltungsArray[1][i] = getZustand(speicherSequenz[1][pointerCounter], speicherSequenz[2][pointerCounter]);
                        verwaltungsMatrix.put(0, i, speicherSequenz[0][pointerCounter], Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.put(1, i, getZustand(speicherSequenz[1][pointerCounter], speicherSequenz[2][pointerCounter]), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(0, i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(1, i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        src.unhighlight(6);
                        src.highlight(7);
                        lang.nextStep();
                        iaCache.unhighlightCell(i, i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.unhighlightCell(0, i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.unhighlightCell(1, i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        break;
                    }

                }
//Wenn im Cache kein Platz mehr frei ist
                if (!cacheFrei) {
                    src.unhighlight(5);
                    src.highlight(8);
                    lang.nextStep();
                    src.unhighlight(8);
                    src.highlight(9);
                    if (enthält(verwaltungsArray[1], 1)) {

                        lang.nextStep();
                        verwaltungsMatrix.highlightCell(0, getStelle(verwaltungsArray[1], 1), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(1, getStelle(verwaltungsArray[1], 1), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        lang.nextStep();
                        src.unhighlight(9);
                        src.highlight(10);
                        int stelle = getStelle(verwaltungsArray[1], 1);
                        verwaltungsMatrix.highlightCell(0, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(1, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        lang.nextStep();
                        iaCache.highlightCell(stelle, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        iaCache.put(stelle, speicherSequenz[0][pointerCounter], Timing.INSTANTEOUS, Timing.MEDIUM);
                        cache[stelle] = speicherSequenz[0][pointerCounter];
                        verwaltungsMatrix.put(0, stelle, speicherSequenz[0][pointerCounter], Timing.INSTANTEOUS, Timing.MEDIUM);
                        verwaltungsMatrix.put(1, stelle, getZustand(speicherSequenz[1][pointerCounter], speicherSequenz[2][pointerCounter]), Timing.INSTANTEOUS, Timing.MEDIUM);
                        verwaltungsArray[0][stelle] = speicherSequenz[0][pointerCounter];
                        verwaltungsArray[1][stelle] = getZustand(speicherSequenz[1][pointerCounter], speicherSequenz[2][pointerCounter]);
                        lang.nextStep();
                        src.unhighlight(10);
                        verwaltungsMatrix.unhighlightCell(0, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.unhighlightCell(1, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        iaCache.unhighlightCell(stelle, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

                    } else if (enthält(verwaltungsArray[1], 2)) {
                        lang.nextStep();
                        src.unhighlight(9);
                        src.highlight(11);
                        int stelle = getStelle(verwaltungsArray[1], 2);
                        verwaltungsMatrix.highlightCell(0, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(1, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        lang.nextStep();
                        src.unhighlight(11);
                        src.highlight(12);
                        iaCache.highlightCell(stelle, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        iaCache.put(stelle, speicherSequenz[0][pointerCounter], Timing.INSTANTEOUS, Timing.MEDIUM);
                        cache[stelle] = speicherSequenz[0][pointerCounter];
                        verwaltungsMatrix.put(0, stelle, speicherSequenz[0][pointerCounter], Timing.INSTANTEOUS, Timing.MEDIUM);
                        verwaltungsMatrix.put(1, stelle, getZustand(speicherSequenz[1][pointerCounter], speicherSequenz[2][pointerCounter]), Timing.INSTANTEOUS, Timing.MEDIUM);
                        verwaltungsArray[0][stelle] = speicherSequenz[0][pointerCounter];
                        verwaltungsArray[1][stelle] = getZustand(speicherSequenz[1][pointerCounter], speicherSequenz[2][pointerCounter]);
                        lang.nextStep();
                        src.unhighlight(12);
                        verwaltungsMatrix.unhighlightCell(0, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.unhighlightCell(1, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        iaCache.unhighlightCell(stelle, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);


                    } else if (enthält(verwaltungsArray[1], 3)) {
                        lang.nextStep();
                        src.unhighlight(9);
                        src.highlight(13);
                        int stelle = getStelle(verwaltungsArray[1], 3);
                        verwaltungsMatrix.highlightCell(0, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(1, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        lang.nextStep();
                        src.unhighlight(13);
                        src.highlight(14);
                        iaCache.highlightCell(stelle, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        iaCache.put(stelle, speicherSequenz[0][pointerCounter], Timing.INSTANTEOUS, Timing.MEDIUM);
                        cache[stelle] = speicherSequenz[0][pointerCounter];
                        verwaltungsMatrix.put(0, stelle, speicherSequenz[0][pointerCounter], Timing.INSTANTEOUS, Timing.MEDIUM);
                        verwaltungsMatrix.put(1, stelle, getZustand(speicherSequenz[1][pointerCounter], speicherSequenz[2][pointerCounter]), Timing.INSTANTEOUS, Timing.MEDIUM);
                        verwaltungsArray[0][stelle] = speicherSequenz[0][pointerCounter];
                        verwaltungsArray[1][stelle] = getZustand(speicherSequenz[1][pointerCounter], speicherSequenz[2][pointerCounter]);
                        lang.nextStep();
                        src.unhighlight(14);
                        verwaltungsMatrix.unhighlightCell(0, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.unhighlightCell(1, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        iaCache.unhighlightCell(stelle, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

                    } else {
                        lang.nextStep();
                        src.unhighlight(9);
                        src.unhighlight(15);
                        int stelle = getStelle(verwaltungsArray[1], 4);
                        verwaltungsMatrix.highlightCell(0, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.highlightCell(1, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        lang.nextStep();

                        src.unhighlight(15);
                        src.highlight(16);
                        iaCache.highlightCell(stelle, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        iaCache.put(stelle, speicherSequenz[0][pointerCounter], Timing.INSTANTEOUS, Timing.MEDIUM);
                        cache[stelle] = speicherSequenz[0][pointerCounter];
                        verwaltungsMatrix.put(0, stelle, speicherSequenz[0][pointerCounter], Timing.INSTANTEOUS, Timing.MEDIUM);
                        verwaltungsMatrix.put(1, stelle, getZustand(speicherSequenz[1][pointerCounter], speicherSequenz[2][pointerCounter]), Timing.INSTANTEOUS, Timing.MEDIUM);
                        verwaltungsArray[0][stelle] = speicherSequenz[0][pointerCounter];
                        verwaltungsArray[1][stelle] = getZustand(speicherSequenz[1][pointerCounter], speicherSequenz[2][pointerCounter]);

                        lang.nextStep();
                        src.unhighlight(16);
                        verwaltungsMatrix.unhighlightCell(0, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        verwaltungsMatrix.unhighlightCell(1, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                        iaCache.unhighlightCell(stelle, stelle, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
                    }
                }
            }
        }
        src.highlight(0);
        zugriffsMatrix.unhighlightCell(0, pointerCounter - 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        zugriffsMatrix.unhighlightCell(1, pointerCounter - 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
        zugriffsMatrix.unhighlightCell(2, pointerCounter - 1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);

        lang.nextStep();
        src.unhighlight(0);
        src.highlight(17);

        MultipleChoiceQuestionModel effizient = new MultipleChoiceQuestionModel("einsatz");
        effizient.setPrompt("Wann ist das NRU Verfahren effizient?");
        effizient.addAnswer("Wenn auf Seiten mit niedriegen Prioritäten öfter zugegriffen ", 0, "Falsch! Der NRU-Algorithmus ist dann effizient wenn auf Seiten mit höheren Prioritäten öfter zugegriffen wird");
        effizient.addAnswer("Wenn auf Seiten mit höheren Prioritäten öfter zugegriffen wird ", 1, "Richtig!");
        effizient.addAnswer("Wenn die Priorität einer Seite keine Rolle über zukünftige Zugriffe spielt ", 0, "Falsch! Der NRU-Algorithmus ist dann effizient wenn auf Seiten mit höheren Prioritäten öfter zugegriffen wird");
        lang.addMCQuestion(effizient);
        lang.nextStep();
        zugriffsMatrix.hide();
        speicherzugriffe.hide();
        speicherzugriffelesend.hide();
        speicherzugriffeschreibend.hide();
        iaCache.hide();
        cacheText.hide();
        seitennummer.hide();
        priorität.hide();
        verwaltungsMatrix.hide();
        sourcecode.hide();
        src.hide();
        trennlinie.hide();
        view.hide();
        zugriffe1.hide();
        zugriffe2.hide();

        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 22));
        Text fazitText = lang.newText(new Coordinates(20, 100), "Fazit",
                "header", null, textProps);
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 18));
        Text ergbniss = lang.newText(new Coordinates(20, 150), "Anzahl an Durchläufe:" + counter.getAssigments(),
                "header", null, textProps);

        sourceCodeProps = new SourceCodeProperties();
        sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 16));
        sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlight);

        SourceCode fazit = lang.newSourceCode(new Coordinates(20, 175), "Einleitung",
                null, sourceCodeProps);
        fazit.addCodeLine("Bei diesem Durchgang mussten " + counter.getAssigments() + " mal Seiten nachgeladen werden, da diese sich nicht im Cache befanden.", null, 0, null);
        fazit.addCodeLine("Da die Effizienz eines Page-Replacement sehr davon abhängt in welcher Reihenfolge die Seitenzugriffe abfolgen,", null, 0, null);
        fazit.addCodeLine("sollte versucht werden hierdrüber eine Annahme zu erstellen.", null, 0, null);
        fazit.addCodeLine("Je nach Annahme sollte der gewünschte Page-Replacement-Algorithmus gewählt werden.", null, 0, null);
        fazit.addCodeLine("", null, 0, null);
        fazit.addCodeLine("Der NRU-Algorithmus eignet sich gut, wenn die Annahme eintrifft, dass Seiten mit einer geringen Priorität in Zukunft  seltener benötigt werden.  ", null, 0, null);
        lang.nextStep("Fazit");

        lang.finalizeGeneration();
        return lang.toString();

    }

    public String getName() {
        return "The Not Recently Used Page Replacement Algorithm";
    }

    public String getAlgorithmName() {
        return "The Not Recently Used Page Replacement Algorithm";
    }

    public String getAnimationAuthor() {
        return "Paul Vincent Dawadi";
    }

    public String getDescription() {
        return "Der NRU-Algorithmus ist ein Page-Replacement-Algorithmus.\n" +
                "Das Betriebssystem speichert im Arbeitsspeicher Seiten des Adressbereiches.\n" +
                "Häufig befinden sich die benötigten Seiten aber nicht im Arbeitsspeicher, dann kommt es zu einem Seitenfehler.\n" +
                "Die benötigten Seite muss dann in dem Arbeitsspeicher geladen werden.\n" +
                "Ist der Arbeitsspeicher aber voll, muss eine andere Seite aus dem Arbeitsspeicher überschrieben werden.\n" +
                "Ein Page-Replacement-Algorithmus versucht hier nun algorithmisch zu entscheiden welche Seite überschrieben, werden soll.\n" +
                "\n" +
                "Zugriffe beinhalten neben der Seitennummer bei diesem Verfahren auch die Information, ob diese Zugriffe schreibend oder lesend oder weder noch auf dem Speicher erfolgen.\n" +
                "Die Zugriffstabelle gliedert sich fogendermaßen:\n" +
                "\tIn der ersten Zeile befindet sich die Seitennummer.\n" +
                "\tIn der zweiten Zeile steht, ob ein Wert lesend oder nicht lesend ist, dabei gilt: 1 = lesend & 0 = nicht lesend\n" +
                "\tIn der dritten Zeile stegt, ob ein Wert schreibend oder nicht schreibend ist, dabei gilt: 1 = schreibend & 0 = nicht schreibend"+
                "\n" +
                "\n" +
                "Der NRU-Algorithmus basiert auf der Idee, dass eine Seite mit einer geringen Priorität zukünftig seltener benötigt werden\" \n" +
                "die Priorität ergibt sich aus Lese- und Schreibvorgänge auf der jeweiligen Seite \n" +
                "Die Prioritäten ergeben sich Folgendermaßen:\n" +
                "\t1: lesend = 0; schreibend = 0\n" +
                "\t2: lesend = 0; schreibend = 1\n" +
                "\t3: lesend = 1; schreibend = 0\n" +
                "\t4: lesend = 1; schreibend = 1\n" +
                "Wenn nun aus dem Cache eine Seite ersetzt werden muss, wird eine Seite mit der niedrigsten Priorität ersetzt.\n";    }

    public String getCodeExample() {
        return "bis alle Speicherzugriffe abgearbeitet sind\n" +
                "\tZugriff auf nächste Speicheradresse\n" +
                "\t\tif(Speicheradresse bereits im Cache)\n" +
                "\t\t\tPriorität aktualisieren\n" +
                "\t\telse\n" +
                "\t\t\tif(Ist noch Platz im Cache)\n" +
                "\t\t\t\tFüge Speicheradresse in Cache ein\n" +
                "\t\t\t\tSetze die Priorität für die Seite\n" +
                "\t\t\telse\n" +
                "\t\t\t\tif(Seite mit Priorität == 1 enthalten )\n" +
                "\t\t\t\t\tErsetze beliebige Seite mit dem Priorität 1\n" +
                "\t\t\t\telse if(Seite mit Priorität == 2 enthalten )\n" +
                "\t\t\t\t\tErsetze beliebige Seite mit dem Priorität 2\n" +
                "\t\t\t\telse if(Seite mit Priorität == 3 enthalten )\n" +
                "\t\t\t\t\tErsetze beliebige Seite mit dem Priorität 3\n" +
                "\t\t\t\telse\n" +
                "\t\t\t\t\tErsetze beliebige Seite\n" +
                "Alle Zugriffe abgearbeitet";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_HARDWARE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    private boolean enthält(int[] ar, int wert) {
        boolean enhält = false;
        for (int i = 0; i < ar.length; i++) {
            if (ar[i] == wert) {
                enhält = true;
                break;
            }
        }
        return enhält;
    }

    private int getStelle(int[] ar, int wert) {
        int stelle = -1;
        for (int i = 0; i < ar.length; i++) {
            if (ar[i] == wert) {
                stelle = i;
                break;
            }
        }
        return stelle;
    }

    private int getZustand(int a, int m) {
        int zustand = 0;
        if (a == 0 && m == 0) {
            zustand = 1;
        } else if (a == 0 && m == 1) {
            zustand = 2;
        } else if (a == 1 && m == 0) {
            zustand = 3;
        } else if (a == 1 && m == 1) {
            zustand = 4;
        }

        return zustand;
    }
}