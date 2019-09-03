/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.misc.modelcheckerCTL;

import generators.misc.modelcheckerCTL.token.Type;

import java.util.HashMap;
import java.util.List;

public abstract class TextDatabase {
    public final static String phi = "x";
    public final static String phi1 = phi+"1";
    public final static String phi2 = phi+"2";
    public final static String psi = "y";
    public final static String stateSet = "S";
    public final static String state = "s";
    public final static String state0 = "s0";
    public final static String Xset = "X";
    public final static String Yset = "Y";

    public HashMap<Type, List<String>> formulaFunctionTexts;
    public HashMap<Type, List<String>> formulaInfoTexts;
    public HashMap<String, List<String>> introTexts;

    public TextDatabase(){
        initFormulaFunctionTexts();
        initFormulaInfoTexts();
        initIntroTexts();
    }

    abstract void initFormulaFunctionTexts();
    abstract void initFormulaInfoTexts();
    abstract void initIntroTexts();

    public List<String> getFormulaFunctionTexts(Type type){
        return formulaFunctionTexts.get(type);
    }


    public List<String> getFormulaInfoTexts(Type type) {
        return formulaInfoTexts.get(type);
    }


    public List<String> getIntroPage(String page) {
        return introTexts.get(page);
    }
}
