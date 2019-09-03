/*
 * ConvertFatToInode.java
 * Thomas Schmeiduch, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.hardware;

import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.*;

import algoanim.primitives.generators.Language;

import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.QuestionModel;

public class ConvertFatToInode implements Generator, ValidatingGenerator {
    private Language lang;
    private String[] fatBlocks;
    private MatrixProperties inodeMatrixProps;
    private MatrixProperties directoryMatrixProps;
    private String[][] directory;
    private MatrixProperties fatMatrixProps;
    private SourceCodeProperties procedureTextProps;

    //Computation
    private ArrayList<ArrayList<Integer>> allInodes;
    private ArrayList<String[]> iNodeDir;
    //
    private StringMatrix stringMatrixFat, stringMatrixDir, stringMatrixInodeDir;
    private ArrayList<StringMatrix> stringMatricesInodes;
    private SourceCode procedureText;
    private int line;
    private Map<String, QuestionModel> questions;

    public void init(){
        lang = new AnimalScript("Konvertierung FAT zu i-Node Dateisystem", "Thomas Schmeiduch", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

        initVariables(props, primitives);
        lang.setStepMode(true);

        iNodeDir = new ArrayList<>();
        allInodes = new ArrayList<>();
        stringMatricesInodes = new ArrayList<>();
        line = 0;

        initQuestions();

        showTitle();

        showIntroduction(true);
        showIntroduction(false);

        showSubTitles();
        showFat();
        showFatDir();
        showInodeDir();
        showProcedureText();
        lang.nextStep("Visualisierung des Algorithmus");
        showAnimation();
        procedureText.unhighlight(4);

        lang.nextStep();

        showFinish();
        lang.nextStep();

        lang.finalizeGeneration();
        return lang.toString();
    }

    private void initVariables(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        fatBlocks = (String[])primitives.get("fatBlocks");
        inodeMatrixProps = (MatrixProperties)props.getPropertiesByName("inodeMatrixProps");
        directoryMatrixProps = (MatrixProperties)props.getPropertiesByName("directoryMatrixProps");
        directory = (String[][])primitives.get("directory");
        fatMatrixProps = (MatrixProperties)props.getPropertiesByName("fatMatrixProps");
        procedureTextProps = (SourceCodeProperties) props.getPropertiesByName("procedureTextProps");
    }

    private void initQuestions() {
        questions = new HashMap<>();
        MultipleChoiceQuestionModel block0 = new MultipleChoiceQuestionModel("block0");
        block0.setPrompt("Was passiert als nächstes?");
        block0.addAnswer("Die Iteration fuer diese Datei ist fertig, da eine 0 im Block steht.",
                0,
                "Denken wir noch mal nach... PCs fangen bei 0 an zu zählen. Wie soll man dann auf den 0. Block des Dateisystems zugreifen?");
        block0.addAnswer("Im nächsten Schritt springt die Iteration an Block 0 im Dateisystem.", 1, "Richtig!");
        questions.put("block0", block0);

        MultipleChoiceQuestionModel fileStart = new MultipleChoiceQuestionModel("fileStart");
        fileStart.setPrompt("In welchem Block faengt diese Datei an?");
        questions.put("fileStart", fileStart);

        FillInBlanksQuestionModel blockEof = new FillInBlanksQuestionModel("blockEof");
        blockEof.setPrompt("In welchem Block befindet sich das Ende der Datei, die gerade bearbeitet wird (eof-Tag)?");
        questions.put("blockEof", blockEof);
    }

    //-----------------

    private void showTitle() {
        TextProperties txtProps = new TextProperties();
        txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font(Font.SANS_SERIF,Font.BOLD,24));
        lang.newText(new Coordinates(10,20), getAlgorithmName(),"header",null,txtProps);

        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        lang.newRect(new Offset(-5, -5, "header",
                AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect", null, rectProps);
    }

    private void showSubTitles() {
        TextProperties subTxtProps = new TextProperties();
        subTxtProps.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font(Font.SANS_SERIF,Font.BOLD,14));
        lang.newText(new Coordinates(10,60), "FAT Dateisystem","headerFAT",null,subTxtProps);

        lang.newText(new Coordinates(280,60), "I-Node Dateisystem","headerInode",null,subTxtProps);
    }

    private void showIntroduction(boolean explanation) {
        SourceCode t = lang.newSourceCode(new Coordinates(15,65),"intro",null);
        t.addMultilineCode(explanation ? getDescription() : getCodeExample(),
                "intro",null);
        lang.nextStep(explanation ? "Beschreibung" : "Vorgehen");
        t.hide();
    }

    private void showProcedureText() {
        procedureText = lang.newSourceCode(new Coordinates(550,5),"vorgehen",null, procedureTextProps);
        procedureText.addMultilineCode(getCodeExample(),
                "vorgehen",null);
    }

    private void showFat() {
        String[][] matrix = new String[fatBlocks.length+1][2];
        matrix[0][0] = "";
        matrix[0][1] = "FAT";
        for(int i = 0; i < fatBlocks.length; i++) {
            matrix[i+1][0] = Integer.toString(i);
            matrix[i+1][1] = fatBlocks[i];
        }
        stringMatrixFat = lang.newStringMatrix(new Coordinates(10, 80), matrix, "gridPageFrames", null, fatMatrixProps);

    }

    private void showFatDir() {
        lang.newText(new Coordinates(80,80), "Verzeichnis","header",null);

        String[][] matrix = new String[directory.length+1][2];
        matrix[0][0] = "Name";
        matrix[0][1] = "Block";
        for(int i = 0; i < directory.length; i++)
            System.arraycopy(directory[i], 0, matrix[i + 1], 0, 2);
        stringMatrixDir = lang.newStringMatrix(new Coordinates(80, 95), matrix, "gridPageFrames", null, directoryMatrixProps);
    }

    private void showInodeDir() {
        if(stringMatrixInodeDir != null)
            stringMatrixInodeDir.hide();
        lang.newText(new Coordinates(280,110), "Verzeichnis","header",null);

        String[][] matrix = new String[iNodeDir.size()+1][2];
        matrix[0][0] = "Name";
        matrix[0][1] = "I-Node";
        for(int i = 0; i < iNodeDir.size(); i++) {
            for(int z = 0; z < 2; z++) {
                matrix[i+1][z] = iNodeDir.get(i)[z] == null ? "" : iNodeDir.get(i)[z];
            }
        }
        stringMatrixInodeDir = lang.newStringMatrix(new Coordinates(280, 125), matrix, "gridPageFrames", null, directoryMatrixProps);

    }

    private void showFinish() {
        lang.hideAllPrimitives();
        showTitle();
        showInodeDir();
        for(int i = 0; i < directory.length; i++)
            showInode(i);

        TextProperties txtProps = new TextProperties();
        txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font(Font.SANS_SERIF,Font.BOLD,24));
        txtProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
        lang.newText(new Coordinates(10,60), "Fertig!","headerFinished",null,txtProps);

        lang.newText(new Coordinates(10,90), "Das ist nun unser i-Node Dateisystem.","headerFinished",null);

        SourceCode t = lang.newSourceCode(new Coordinates(10,105),"inodesFinished",null);
        t.addCodeLine("I-Node Verzeichnis enthält " + directory.length + " Einträge.", "inodesFinished", 1, null);

        for(int i = 0; i < allInodes.size(); i++) {
            String s = "I-Node " + i + " besitzt " + allInodes.get(i).size() + " Blöcke.";

            t.addCodeLine(s, "inodesFinished", 1, null);
        }

    }

    //-----

    private void showAnimation() {
        for (int i = 0; i < directory.length; i++) {
            highlightNextDir(i);
            if(directory.length > 1 && i == 1) { //question second file in dir - where it starts
                questions.get("fileStart").addAnswer("Diese Datei startet in Zeile " + directory[i][1] + " des FAT Dateisystems. Block 0 entspricht hierbei die Zeile 1.",
                        0, "Falsch! Ein PC orientiert sich mit Adressen, der PC wird keine Zeilennummern in Blocknummern umrechnen.");
                questions.get("fileStart").addAnswer("Diese Datei startet in Blocknummer " + directory[i][1] + ".", 1, "Richtig!");
                lang.addMCQuestion((MultipleChoiceQuestionModel) questions.get("fileStart"));
            }
            lang.nextStep();
            convert(i);
        }
        stringMatrixDir.unhighlightCell(directory.length, 0, null, null);
    }

    private void highlightNextDir(int i) {
        if(i != 0)
            stringMatrixDir.unhighlightCell(i, 0, null, null);
        stringMatrixDir.highlightCell(i+1, 0, null, null);
        highlightNextProcedure();
        iNodeDir.add(new String[]{directory[i][0], Integer.toString(iNodeDir.size())});
        lang.nextStep();
        highlightNextProcedure();
        showInodeDir();
    }

    private void highlightNextProcedure() {
        procedureText.unhighlight(line);
        if(line > 3)
            line = 0;
        procedureText.highlight(++line);
    }

    private void showInode(int i) {
        if(allInodes.get(i) == null)
            return;
        if(stringMatricesInodes.size() <= i)
            stringMatricesInodes.add(i, null);
        else
            stringMatricesInodes.get(i).hide();


        String[][] matrix = new String[allInodes.get(i).size()+1][2];
        matrix[0][0] = "Block:";
        matrix[0][1] = "I-Node " + i;
        for(int z = 0; z < allInodes.get(i).size(); z++) {
            matrix[z+1][0] = Integer.toString(z);
            matrix[z+1][1] = Integer.toString(allInodes.get(i).get(z));
        }
        stringMatricesInodes.set(i, lang.newStringMatrix(new Coordinates(420 + (i*140), 125), matrix, "gridPageFrames", null, inodeMatrixProps));
    }

    //---
    //Computation
    private void convert(int i) {
        allInodes.add(i, new ArrayList<>());
        showInode(i);
        lang.nextStep();

        ArrayList<Integer> iNode = allInodes.get(i);

        int element = Integer.parseInt(directory[i][1]);
        iNode.add(element);
        showInode(i);
        highlightNextProcedure();
        do {
            stringMatrixFat.highlightCell(element+1, 1, null, null);
            iNode.add(Integer.parseInt(fatBlocks[element]));
            if(Integer.parseInt(fatBlocks[element]) == 0) //question block 0
                lang.addMCQuestion((MultipleChoiceQuestionModel)questions.get("block0"));
            lang.nextStep();

            stringMatrixFat.unhighlightCell(element+1, 1, null, null);
            showInode(i);
            element = Integer.parseInt(fatBlocks[element]);

            if (!fatBlocks[element].equals("eof") && directory.length / 2 == i) { //question where file ends (in middle of dir procedure)
                questions.get("blockEof").addAnswer(Integer.toString(determineEof(i)), 1, "Richtig!");
                lang.addFIBQuestion((FillInBlanksQuestionModel) questions.get("blockEof"));
            }

        } while (!fatBlocks[element].equals("eof"));
        stringMatrixFat.highlightCell(element+1, 1, null, null);
        lang.nextStep();
        stringMatrixFat.unhighlightCell(element+1, 1, null, null);
        highlightNextProcedure();
        lang.nextStep();
    }

    //---
    //helper methods
    private boolean checkFat() {
        //only digits, eof & -
        for (String s : fatBlocks) {
            if (s == null || (!s.matches("[-+]?\\d*\\.?\\d+") && !s.equals("-") && !s.equals("eof")))
                return false;
        }
        return true;
    }

    private boolean checkDirLst() {
        //only digits, startblock smaller then whole FAT system, file in dir does not point to empty block
        for (String[] s : directory) {
            if (s == null ||
                    !s[1].matches("[-+]?\\d*\\.?\\d+") ||
                    Integer.parseInt(s[1]) >= fatBlocks.length ||
                    fatBlocks[Integer.parseInt(s[1])].equals("-"))
                return false;
        }
        return true;
    }

    private int determineEof(int i) {
        int element = Integer.parseInt(directory[i][1]);
        do
            element = Integer.parseInt(fatBlocks[element]);
        while (!fatBlocks[element].equals("eof"));
        return element;
    }

    //-----------------

    public String getName() {
        return "Konvertierung FAT zu i-Node Dateisystem";
    }

    public String getAlgorithmName() {
        return "Konvertierung FAT zu i-Node Dateisystem";
    }

    public String getAnimationAuthor() {
        return "Thomas Schmeiduch";
    }

    public String getDescription(){
        return "Es gibt viele verschiedene Arten, wie Dateien auf einem Speichermedium verwaltet wird. Für diese Verwaltung benötigt es ein Dateisystem.\n" +
                "Es gibt verschiedene Dateisystem-Arten. Zwei davon sind das FAT und i-Node Dateisystem.\n" +
                "In dieser Animation wird dargestellt, wie ein FAT Dateisystem in ein i-Node basiertes Dateisystem konvertiert wird.\n" +
                "Ein FAT Dateisystem besteht aus zwei Listen: Die eine Liste stellt alle Datenblöcke, die auf der Festplatte liegen (Festplattenspuren werden in Blöcke aufgeteilt), dar. " +
                "Die zweite Liste beinhaltet alle Dateinamen und die Anfangsblocknummer, wo die Datei auf der Festplatte anfängt.\n" +
                "Am Ende der Konvertierung erhalten wir ein i-Node basiertes Dateisystem. I-Node basierte Dateisysteme enthalten eine Verzeichnisliste, " +
                "die den Dateinamen enthält und den Verweis auf die i-Node Nummer, und pro Datei einen i-Node.";
    }

    public String getCodeExample(){
        return  "Vorgehen:\n" +
                "1. Lese (erstes) Element aus der FAT Directory aus und ermittle den Startblock dieser Datei.\n" +
                "2. Trage den Dateinamen in die i-Node directory, erstelle einen neuen i-Node mit eindeutigem Namen und trage den Namen des i-Nodes in die i-Node directory.\n" +
                "3. Gehe zu diesem Startblock im FAT Dateisystem, gehe nacheinander alle Blöcke durch, die zu dieser Datei gehören (eine Art verkettete Liste), und schreibe dies in den neuen i-Node.\n" +
                "4. Gehe zum nächsten Element im FAT Directory, bis alle Dateien auf das neue Dateisystem konvertiert wurden.";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_HARDWARE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> hashtable) throws IllegalArgumentException {
        initVariables(animationPropertiesContainer, hashtable);
        if(!checkFat())
            throw new IllegalArgumentException("Das FAT Dateisystem darf nur aus Blocknummern, Strichen (unbelegter Block) und end-of-file Tags bestehen (Zahlen, -, eof)");
        if(!checkDirLst())
            throw new IllegalArgumentException("Die Dateien im Verzeichnis dürfen nur auf Blocknummern verweisen, die nicht leer sind und auch nicht außerhalb der FAT Liste liegen!");
        if((directory.length != 0 && directory[0].length != 2))
            throw new IllegalArgumentException("Die Spalten des Verzeichnisses sind unplausibel (Eine Spalte Dateiname, zweite Spalte Verweis auf FAT Blocknummer)!");
        return true;
    }
}