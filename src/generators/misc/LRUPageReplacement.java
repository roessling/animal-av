/*
 * LRUPageReplacement.java
 * Thomas Schmeiduch, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import algoanim.primitives.*;
import algoanim.properties.*;
import algoanim.util.*;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.util.*;

import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.stream.IntStream;

import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionModel;

public class LRUPageReplacement implements Generator, ValidatingGenerator {
    private Language lang;
    private int[] pageAccess;
    private int amountPageFrames;
    private MatrixProperties matrixProps;
    private ListElementProperties listElementProps;

    private StringMatrix stringMatrix;
    private String[][] transposedPageReplHist;

    private int[][] pageReplacementHistory;
    private LinkedList<LinkedList<Integer>> allLinkedListsHistory;

    private SourceCode procedureText;
    private SourceCodeProperties procedureTextProps;
    private int highlight;
    private Map<String, QuestionModel> questions;
    private boolean ask1, ask2;


    public void init(){
        lang = new AnimalScript("LRU Seitenersetzung im RAM", "Thomas Schmeiduch", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

        initVariables(props, primitives);

        pageReplacementHistory = new int[pageAccess.length][amountPageFrames];
        allLinkedListsHistory = new LinkedList<>();
        highlight = 0;
        ask1 = ask2 = true;

        lang.setStepMode(true);

        initQuestions();

        showTitle();
        showIntroduction(true);
        showIntroduction(false);

        lruAlgorithm();

        showGridFrame();
        showListFrame();
        showProcedureText();
        lang.nextStep("Visualisierung des Algorithmus");
        showVisualization();

        showFinish();
        lang.nextStep();

        lang.finalizeGeneration();
        return lang.toString();
    }

    private void initVariables(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        pageAccess = (int[])primitives.get("pageAccess");
        amountPageFrames = (Integer)primitives.get("amountPageFrames");
        matrixProps = (MatrixProperties)props.getPropertiesByName("matrixProps");
        listElementProps = (ListElementProperties) props.getPropertiesByName("listElementProps");
        procedureTextProps = (SourceCodeProperties) props.getPropertiesByName("procedureTextProps");
    }

    private void initQuestions() {
        questions = new HashMap<>();
        MultipleChoiceQuestionModel replaceNotEmpty = new MultipleChoiceQuestionModel("replaceNotEmpty");
        replaceNotEmpty.setPrompt("Welche Seite wird ersetzt?");
        replaceNotEmpty.addAnswer("Seitennummer 1 wird ersetzt.",
                0,
                "Wieso? Es stehen uns noch freie Seitenrahmen zur Verfuegung.");
        replaceNotEmpty.addAnswer("Der naechste freie Seitenrahmen wird beansprucht.", 1, "Richtig!");
        questions.put("replaceNotEmpty", replaceNotEmpty);

        FillInBlanksQuestionModel replaceSite = new FillInBlanksQuestionModel("replaceSite");
        replaceSite.setPrompt("Welche Seitenrahmen-Nummer wird im naechsten Schritt ersetzt?");
        questions.put("replaceSite", replaceSite);
    }

    //-------------------

    private void showTitle() {
        TextProperties txtProps = new TextProperties();
        txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font(Font.SANS_SERIF,Font.BOLD,24));
        lang.newText(new Coordinates(10,20),"LRU Seitenersetzung","header",null,txtProps);

        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        lang.newRect(new Offset(-5, -5, "header",
                        AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect", null, rectProps);
    }

    private void showIntroduction(boolean explanation) {
        SourceCode t = lang.newSourceCode(new Coordinates(15,65),"intro",null);
        t.addMultilineCode(explanation ? getDescription() : getCodeExample(),
                "intro",null);
        lang.nextStep(explanation ? "Beschreibung" : "Vorgehen");
        t.hide();
    }

    private void showGridFrame() {
        transposedPageReplHist = new String[amountPageFrames+1][pageAccess.length+1];
        transposedPageReplHist[0][0] = "";

        for(int x = 0; x < amountPageFrames; x++) //Add items
            for(int y = 0; y < pageAccess.length; y++)
                transposedPageReplHist[x+1][y+1] = "";
        for(int i = 1; i <= amountPageFrames; i++) //Add description on the left
            transposedPageReplHist[i][0] = "Seitennummer " + i;
        for(int i = 0; i < pageAccess.length; i++) //Add pageAccess
            transposedPageReplHist[0][i+1] = Integer.toString(pageAccess[i]);
        transposedPageReplHist[0][0] = "Seitenzugriffe:";

        stringMatrix = lang.newStringMatrix(new Coordinates(10, 160), transposedPageReplHist, "gridPageFrames", null, matrixProps);
    }

    private void showListFrame() {
        //Show frame and title
        RectProperties rectPropsFrame = new RectProperties();
        rectPropsFrame.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectPropsFrame.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        rectPropsFrame.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        lang.newRect(new Coordinates(10, 60),
                new Coordinates(amountPageFrames * 80, 150), "hRectFrame", null, rectPropsFrame);
        lang.newText(new Coordinates(15, 65), "Verkettete Liste:", "frameHeader", null);

        //Show line
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        lang.newRect(new Coordinates((amountPageFrames * 80) - 60 - 10, 80),
                new Coordinates((amountPageFrames * 80) - 60 - 9, 140),
                "hRectFrameLine", null, rectProps);

        lang.newText(new Coordinates(15, 100), "Anfang", "frameHeader", null);
    }

    private void showProcedureText() {
        SourceCode p = lang.newSourceCode(new Coordinates(555,5),"vorgehen1",null, procedureTextProps);
        p.addMultilineCode(codeExample1, "vorgehen1",null);

        procedureText = lang.newSourceCode(new Coordinates(555,53),"vorgehen2",null, procedureTextProps);
        procedureText.addMultilineCode(codeExample2,"vorgehen2",null);
    }

    private void highlightProcedure(int i) {
        procedureText.unhighlight(highlight);
        highlight = i;
        procedureText.highlight(i);
    }

    private void showVisualization() {
        for(int i = 0; i < pageAccess.length; i++) {
            showHighlightGridHeader(i);
            highlightProcedure(0);
            lang.nextStep();

            if(i != 0 && allLinkedListsHistory.get(i).size() < amountPageFrames && ask1) {
                lang.addMCQuestion((MultipleChoiceQuestionModel) questions.get("replaceNotEmpty"));
                lang.nextStep();
                ask1 = false;
            }

            showHighlightGridCellPageRepl(i);
            lang.nextStep();
            showGrid(i);
            lang.nextStep();

            showListElements(i);
            showUnHighlightGridCellRow(i);
            lang.nextStep();
        }
        //showGrid - end
        stringMatrix.unhighlightCell(0, pageAccess.length, null, null);
        procedureText.unhighlight(highlight);
    }

    private void showFinish() {
        lang.hideAllPrimitives();
        showTitle();
        showGrid(amountPageFrames-1);


        TextProperties txtProps = new TextProperties();
        txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font(Font.SANS_SERIF,Font.BOLD,24));
        txtProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
        lang.newText(new Coordinates(10,60), "Fertig!","headerFinished",null,txtProps);

        StringBuilder s = new StringBuilder("Nach den Zugriffen enthält ");
        for(int i = 0; i < amountPageFrames; i++)
            s.append("Seitenrahmen ").append(i + 1).append(": ").append(pageReplacementHistory[pageReplacementHistory.length - 1][i]).append(", ");
        s = new StringBuilder(s.substring(0, s.length() - 2));
        s.append(".");

        lang.newText(new Coordinates(10,90), s.toString(),"headerFinished",null);
    }

    //----
    // Helper methods

    private void showHighlightGridHeader(int i) {
        stringMatrix.unhighlightCell(0, i, null, null);
        stringMatrix.highlightCell(0, i+1, null, null);
    }

    private void showUnHighlightGridCellRow(int i) {
        stringMatrix.unhighlightCellRowRange(1, amountPageFrames, i+1,null, null);
    }

    private void showHighlightGridCellPageRepl(int i) {

        Integer[] actualArrayHistory = IntStream.of( pageReplacementHistory[i] ).boxed().toArray( Integer[]::new );

        //if item already in a page - highlight to this again
        if(i != 0 && allLinkedListsHistory.get(i-1).contains(pageAccess[i])) {
            int pointer = Arrays.asList(actualArrayHistory).indexOf(pageAccess[i]);
            stringMatrix.highlightCell(pointer+1, i+1, null, null);
            highlightProcedure(2);
            return;
        }
        //if table not full - highlight next frame
        if(allLinkedListsHistory.get(i).size() < amountPageFrames ||
                (i != 0 && allLinkedListsHistory.get(i-1).size() < amountPageFrames && allLinkedListsHistory.get(i).size() == amountPageFrames))
            stringMatrix.highlightCell(allLinkedListsHistory.get(i).size(), i+1, null, null);
        //Highlight which one will be removed
        else {
            int pointer = Arrays.asList(actualArrayHistory).indexOf(allLinkedListsHistory.get(i).getLast());

            if(ask2) {
                questions.get("replaceSite").addAnswer(Integer.toString(pointer), 1, "Richtig!");
                lang.addFIBQuestion((FillInBlanksQuestionModel) questions.get("replaceSite"));
                ask2 = false;
                lang.nextStep();
            }

            stringMatrix.highlightCell(pointer, i+1, null, null);
        }
        highlightProcedure(1);
    }

    /**
     * Show plain Grid on every update
     * @param z actual index
     */
    private void showGrid(int z) {
        z++;

        for(int x = 0; x < amountPageFrames; x++)
            transposedPageReplHist[x+1][z] = pageReplacementHistory[z-1][x] == 0 ? "" : Integer.toString(pageReplacementHistory[z-1][x]);
        lang.newStringMatrix(new Coordinates(10, 160), transposedPageReplHist, "gridPageFrames", null, matrixProps);
    }

    /**
     * Generate LinkedList Elements View
     * @param i actual history index
     */
    private void showListElements(int i) {

        if(allLinkedListsHistory.get(i).isEmpty())
            return;

        listElementProps.set(AnimationPropertiesKeys.TEXT_PROPERTY, Integer.toString(allLinkedListsHistory.get(i).get(allLinkedListsHistory.get(i).size()-1)));

        ListElement le = lang.newListElement(new Coordinates((amountPageFrames * 80) - 60, 100),
                1,
                null,
                null,
                "LE" + i + "0",
                new ArrayDisplayOptions(new MsTiming(0), new MsTiming(0),true),
                listElementProps);

        LinkedList<Object> lst = new LinkedList<>();
        int y = 1;

        for(int z = allLinkedListsHistory.get(i).size()-2; z >= 0; z--) {

            listElementProps.set(AnimationPropertiesKeys.TEXT_PROPERTY, Integer.toString(allLinkedListsHistory.get(i).get(z)));
            lst.add(le);
            ListElement le2 = lang.newListElement(
                    new Coordinates((amountPageFrames * 80) - ((y+1)*60), 100), 1,
                    lst,
                    null,
                    "LE" + i + z,
                    new ArrayDisplayOptions(new MsTiming(0),new MsTiming(0),true),
                    listElementProps);

            lst.clear();
            lst.add(le2);
            y++;
        }
    }

    //-------------------
    //Computation
    /**
     * Compute the LRU algorithm on given page access
     */
    private void lruAlgorithm() {
        int pointer = -1;
        LinkedList<Integer> lstPageRepl = new LinkedList<>();
        for (int i = 0; i < pageAccess.length; i++) {
            if(i != 0)
                lruCopyHistory(i-1, i);

            if(lstPageRepl.contains(pageAccess[i])) {
                lstPageRepl.remove(lstPageRepl.indexOf(pageAccess[i]));
                lstPageRepl.addFirst(pageAccess[i]);
                allLinkedListsHistory.add(new LinkedList<>(lstPageRepl));
                continue;
            }

            if(lstPageRepl.size() < amountPageFrames) {
                pointer++;
            }
            else {
                int toReplace = lstPageRepl.getLast();
                Integer[] actualArrayHistory = IntStream.of( pageReplacementHistory[i] ).boxed().toArray( Integer[]::new );
                pointer = Arrays.asList(actualArrayHistory).indexOf(toReplace);
                lstPageRepl.removeLast();
            }
            lstPageRepl.addFirst(pageAccess[i]);
            pageReplacementHistory[i][pointer] = pageAccess[i];
            allLinkedListsHistory.add(new LinkedList<>(lstPageRepl));
        }
    }

    private void lruCopyHistory(int src, int dest) {
        System.arraycopy(pageReplacementHistory[src], 0, pageReplacementHistory[dest], 0, amountPageFrames);
    }

    //-------------------

    public String getName() {
        return "LRU Seitenersetzung im RAM";
    }

    public String getAlgorithmName() {
        return "LRU Seitenersetzung im RAM";
    }

    public String getAnimationAuthor() {
        return "Thomas Schmeiduch";
    }

    public String getDescription(){
        return "Mithilfe von virtuellem Speicher gelingt es uns mehrere Prozesse auszuführen, obwohl dieser Prozess mehr Speicherplatz braucht, als der Hauptspeicher groß ist.\n" +
                "Dabei muss das Betriebssystem mithilfe eines Algorithmes entscheiden, welche Seite im Speicher ersetzt werden muss. In diesem Falle wird das LRU (Least Recently Used) Verfahren dargestellt.\n" +
                "Es wird dargestellt, welche Seite in welchem Seitenrahmen nach jedem Zugriff eingelagert wird.\n" +
                "Dabei wird bei einem Seitenfehler die Seite ersetzt, die am längsten nicht mehr benutzt wird.";
    }

    private String codeExample1 = "Verfahren der Seitenersetzung:\n" +
            "Seiten werden als verkettete Liste verwaltet.\n" +
            "Am Anfang der Liste steht die neueste Seite, am Ende die am längsten nicht mehr benutzte Seite\n";

    private String codeExample2 = "- Bei Zugriff auf eine Seite wird diese Seite in der Liste gesucht, wenn Seitenfehler auftritt:\n" +
            "    - ja:    Das Element am Ende der Liste wird ersetzt (oder Element wird in die nicht volle Liste eingefügt)\n" +
            "    - nein: Die gesuchte Seite wird an den Anfang verschoben";

    public String getCodeExample(){
        return codeExample1 + codeExample2;
    }

    public String getFileExtension(){
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

    @Override
    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> hashtable) throws IllegalArgumentException {
        initVariables(animationPropertiesContainer, hashtable);
        if(amountPageFrames < 1)
            throw new IllegalArgumentException("Die Anzahl der Seitenrahmen muss min. 1 betragen!");
        if(pageAccess.length < amountPageFrames)
            throw new IllegalArgumentException("Es müssen min. " + amountPageFrames + " Zugriffe auf Seiten erfolgen!");
        return true;
    }

}