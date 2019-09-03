/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.misc.modelcheckerCTL.token;

import generators.misc.modelcheckerCTL.Util;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private int waitingForU = 0;
    private final int WAITING_E = 1;
    private final int WAITING_A = 0;
    private final String formula;
    private final List<Token> tokens;

    public Tokenizer(String formula) {
        this.formula = formula;
        this.tokens = tokenize();
    }

    private List<Token> tokenize() {


        List<Token> tokens = new ArrayList<>();
        List<String> workingList = new ArrayList<>();
        Type tmpType = null;
        String[] strings = formula.split(" ");
        String currentSubstring = "";
        char tempChar;

        boolean lastWasOP = false;
        for (int i = 0; i < strings.length; i++) {
            for (int j = 0; j < strings[i].length(); j++) {

                tempChar = strings[i].charAt(j);

                if (Util.isPossibleOperator(tempChar)) {
                    if (currentSubstring.length() > 0) workingList.add("" + currentSubstring);
                    workingList.add("" + tempChar);
                    currentSubstring = "";

                } else {
                    currentSubstring += tempChar;
                }


            }
            if (currentSubstring.length() > 0) {
                workingList.add("" + currentSubstring);
            }


            currentSubstring = "";
        }
        for (int j = 0; j < workingList.size(); j++) {
            if (j < workingList.size() - 1) {
                currentSubstring = workingList.get(j) + workingList.get(j + 1);

                tmpType = getUntilTypeForString(currentSubstring);
                if (tmpType != null) {
                    tokens.add(new Token(tmpType));
                    continue;
                }
                tmpType = getTypeForString(currentSubstring);
                if (tmpType != null) {
                    tokens.add(new Token(tmpType));
                    j++;
                    continue;
                }
            }
            tmpType = getTypeForString(workingList.get(j));
            if (tmpType != null) {
                tokens.add(new Token(tmpType));
                continue;
            }

            tokens.add(new Token(Type.TERMINAL, workingList.get(j)));


        }
        //System.out.println(workingList.toString());
        return tokens;
    }

    private Type getTypeForString(String str) {
        Type result = null;
        Type[] types = Type.values();

        for (int i = 0; i < types.length; i++) {
            if (str.equals(types[i].toString())) {
                result = types[i];
                break;
            }

        }
        return result;
    }


    private Type getUntilTypeForString(String str) {
        Type result = null;
        String exclude = "[^XFG]";
        if (str.matches("A" + exclude)) {
            result = Type.AU_START;
            waitingForU = WAITING_A;
        } else if (str.matches("E" + exclude)) {
            result = Type.EU_START;
            waitingForU = WAITING_E;
        } else if (str.matches("U" + exclude)) {
            if (waitingForU == WAITING_E) {
                result = Type.EU_END;
            } else if (waitingForU == WAITING_A) {
                result = Type.AU_END;
            } else throw new IllegalArgumentException();

            waitingForU = 0;
        }
        return result;
    }

    public String getFormula() {
        return formula;
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
