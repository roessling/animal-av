/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.misc.modelcheckerCTL;


import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import animal.main.Animal;
import generators.graph.helpers.CoordinatesPatternBuilder;
import generators.misc.modelcheckerCTL.kripke.AnimalKripkeStructure;
import generators.misc.modelcheckerCTL.kripke.KripkeState;
import generators.misc.modelcheckerCTL.token.Type;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class GUI {
    StringMatrix solutionTable;
    Text infoText;
    HashMap<Type,SourceCode> formulaFunctionTexts;
    HashMap<Type,SourceCode> formulaInfoTexts;
    HashMap<Type,Rect> formulaFunctionBgs;
    HashMap<Type,Rect> formulaInfoBgs;
    Coordinates leftInfoBoxCorner, rightInfoboxCorner;
    Type activeFormulaText;
    int entryCounter;
    Language lang;
    TextDatabase textDatabase;
    AnimalKripkeStructure animalKripkeStructure;
    HashMap<String, Rect> backgroundShapes;
    Text formulaFunctionTitle;
    final static int FONTSIZE = 18;

    public GUI(Language lang, int solutionSize){
        this.lang = lang;

        leftInfoBoxCorner = new Coordinates(10,60);
        rightInfoboxCorner = new Coordinates(240,60);
        backgroundShapes = new HashMap<>();
        Coordinates tmpCoord;



        textDatabase = new TextDatabaseDE();
        formulaFunctionTexts = new HashMap<>();
        formulaInfoTexts = new HashMap<>();

        SourceCode tmp;
        List<String> tmpSTR;
        String str;
        SourceCodeProperties sourceCodeProperties = new SourceCodeProperties();
        RectProperties rectProperties = new RectProperties();
        rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        Color rectBG = Color.LIGHT_GRAY;

        rectProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, rectBG);
        rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, rectBG);
        rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 15);
        TextProperties textProperties = new TextProperties();

        formulaFunctionBgs = new HashMap<>();
        formulaInfoBgs = new HashMap<>();
        int lines;

        Coordinates formulaFunctionRoot = new Coordinates(leftInfoBoxCorner.getX()-5,leftInfoBoxCorner.getY());
//        Rect tmpBG = lang.newRect(tmpCoord, new Coordinates(tmpCoord.getX()+365, tmpCoord.getY()+205), "formulaFunctionBG", null, rectProperties);
        Rect tmpBG;
//        backgroundShapes.put("formulaFunction", tmpBG);
        formulaFunctionTitle = lang.newText(formulaFunctionRoot, "function SAT( "+TextDatabase.phi+" ) :", "formulaFunctionTitle", null, textProperties);
        formulaFunctionRoot = new Coordinates(formulaFunctionRoot.getX(),formulaFunctionRoot.getY()+18);

        for(Type type: textDatabase.formulaFunctionTexts.keySet()){
            int tabCounter=0;
            tmpSTR = textDatabase.getFormulaFunctionTexts(type);
            tmp = lang.newSourceCode(new Coordinates(leftInfoBoxCorner.getX(),leftInfoBoxCorner.getY()+10), "formulaFunction"+type, null, sourceCodeProperties);
            for (int i = 0; i<tmpSTR.size(); i++) {
                str = tmpSTR.get(i);
                if(str.contains("end")){
                    tabCounter--;
                }
                tmp.addCodeLine(str, "formulaFunction"+type+i, tabCounter,null);
                if(str.contains("begin")||str.contains("repeat until")){
                    tabCounter++; }

            }
            tmp.hide();
            lines = tmp.length();

            formulaFunctionTexts.put(type, tmp);
            tmpBG = lang.newRect(formulaFunctionRoot, new Coordinates(rightInfoboxCorner.getX(), formulaFunctionRoot.getY()+(lines*FONTSIZE)+5), "formulaFunctionBG"+type, null, rectProperties);
            formulaFunctionBgs.put(type, tmpBG);
            tmpBG.hide();

            tmpCoord = (Coordinates)tmpBG.getLowerRight();

            tmpSTR = textDatabase.getFormulaInfoTexts(type);
            tmp = lang.newSourceCode(new Coordinates(leftInfoBoxCorner.getX(),tmpCoord.getY()-5), "formulaInfo"+type, null, sourceCodeProperties);
            tmp.addCodeLine("Vorgehen:", "formulaInfo"+type+"title", 0,null);
            for (int i = 0; i<tmpSTR.size(); i++) {
                str = tmpSTR.get(i);
                tmp.addCodeLine(str, "formulaInfo"+type+i, 0,null);
            }
            tmp.hide();
            lines = tmp.length();
            tmpBG = lang.newRect(new Coordinates(leftInfoBoxCorner.getX()-5,tmpCoord.getY()+23), new Coordinates(rightInfoboxCorner.getX(), tmpCoord.getY()+(lines*FONTSIZE)+5), "formulaInfoBG"+type, null, rectProperties);
            tmpBG.hide();
            formulaInfoTexts.put(type, tmp);
            formulaInfoBgs.put(type, tmpBG);

        }


        MatrixProperties solutionTableProbs = new MatrixProperties();
        solutionTableProbs.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.LIGHT_GRAY);
        String[][] matr1 = new String[solutionSize+1][2];
        matr1[0][0] = "Teilformel:";
        matr1[0][1] = "Gültig in:";
        for (int i = 1; i <= solutionSize; i++) {
            matr1[i][0] = "";
            matr1[i][1] = "";
        }
        solutionTable = lang.newStringMatrix(new Coordinates(rightInfoboxCorner.getX()+400, leftInfoBoxCorner.getY()+30), matr1, "solutionTable", null, solutionTableProbs);
        solutionTable.highlightCell(0,0,null,null);
        solutionTable.highlightCell(0,1,null,null);
        entryCounter = 0;

    }

    public void putTheFormula(String str){
        SourceCodeProperties sourceCodeProperties = new SourceCodeProperties();
        Coordinates tempCoord = (Coordinates) solutionTable.getUpperLeft();
        SourceCode tmp = lang.newSourceCode(new Coordinates(tempCoord.getX(), tempCoord.getY() - 46), "asrg", null, sourceCodeProperties);
        tmp.addCodeLine("Gegebene Formel: ", "abcCode", 0,null);
        tmp.addCodeLine(str, "abcCode", 0,null);

    }


    public boolean putSolutionFormula(String str){
        if(entryCounter < solutionTable.getNrRows()){
            solutionTable.put(entryCounter+1,0,str,null,null);
            return true;
        }else return false;
    }

    public boolean putSolutionStates(String str){
        if(entryCounter < solutionTable.getNrRows()){
            solutionTable.put(entryCounter+1,1,str,null,null);
            entryCounter++;
            return true;
        }else return false;
    }


    public void showFormulaText(Type type){
        if(activeFormulaText!=null)hideFormulaText(activeFormulaText);
        formulaFunctionTexts.get(type).show();
        formulaInfoTexts.get(type).show();
        formulaFunctionBgs.get(type).show();
        formulaInfoBgs.get(type).show();
        activeFormulaText = type;
    }

    public void hideFormulaText(Type type){
        formulaFunctionTexts.get(type).hide();
        formulaInfoTexts.get(type).hide();
        formulaFunctionBgs.get(type).hide();
        formulaInfoBgs.get(type).hide();
    }

    public void setInfoText(String str){
        this.infoText.setText(str, null, null);
    }

    public void setAnimalKripkeStructure(AnimalKripkeStructure animalKripkeStructure) {
        this.animalKripkeStructure = animalKripkeStructure;
    }


    public void showResults(List<KripkeState> resultStates, SourceCodeProperties scTextProperties){
        if(activeFormulaText!=null)hideFormulaText(activeFormulaText);
        formulaFunctionTitle.hide();
        Coordinates tmpCoord = new Coordinates(leftInfoBoxCorner.getX(), leftInfoBoxCorner.getY());
        SourceCode tmp = lang.newSourceCode(tmpCoord, "outTXT", null, scTextProperties);
        RectProperties rectProperties = new RectProperties();
        rectProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
        rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 15);
        lang.newRect(new Coordinates(tmpCoord.getX()-5, tmpCoord.getY()), new Coordinates(tmpCoord.getX()+300, tmpCoord.getY()+80), "txt", null, rectProperties);
        tmp.addCodeLine("Die Formel ist erfüllt in den Zuständen:", "outTXT", 0,null);
        tmp.addCodeLine(""+Util.printSimpleStates(resultStates), "outTXT", 0,null);
        if(resultStates.contains(animalKripkeStructure.getStartState())) {
            tmp.addCodeLine("Da der Startzustand " + animalKripkeStructure.getStartState().getName() + " enthalten ist,", "outTXT", 0, null);
            tmp.addCodeLine("ist die Formel auf der Kripke-Struktur erfüllt.", "outTXT", 0, null);
        }
        else {
            tmp.addCodeLine("Da der Startzustand " + animalKripkeStructure.getStartState().getName() + " nicht enthalten ist,", "outTXT", 0, null);
            tmp.addCodeLine("ist die Formel hiermit auf der Kripke-Struktur", "outTXT", 0, null);
            tmp.addCodeLine("nicht erfüllt.", "outTXT", 0, null);
        }
        lang.nextStep("Ergebnis");
    }

    public Coordinates[] getStructureCoordinates(int size) {
        Coordinates[] result;
        Coordinates tmp;
        result = CoordinatesPatternBuilder.getCircularPattern(size, rightInfoboxCorner.getX()+190, leftInfoBoxCorner.getY() + 190, 150);
        return result;
    }

    public static void main(String[] args) {
        Language lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "hallo", "Timm Welt", 400, 400);
        lang.setStepMode(true);
        GUI gui = new GUI(lang, 10);
//        for (int i = 0; i < 6; i++) {
//            gui.putMinimizedFormula("timmwelz"+i);
//        }
        for (int i = 0; i < 10; i++) {
            gui.infoText.setText(i+ "hallo ich bin der Jonas", null, null);
            gui.putSolutionFormula("form"+i);

            gui.putSolutionStates("states"+i);
        }
        lang.nextStep();
        for (Type type: gui.formulaInfoTexts.keySet()) {
            gui.showFormulaText(type);
            lang.nextStep();
        }
        Animal.startAnimationFromAnimalScriptCode(lang.toString());
    }
}
