/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.misc.modelcheckerCTL;

import generators.misc.modelcheckerCTL.kripke.KripkeState;
import generators.misc.modelcheckerCTL.token.Token;
import generators.misc.modelcheckerCTL.token.Type;

import java.util.List;

public class Util {


    private static int getClosingIndex(Token[] tokens, int start, Type openingBracket, Type closingBracket) {

        int counter = 0;
        int index;

        for (index = start; index < tokens.length; index++) {

            if (tokens[index].getType() == openingBracket) counter++;
            else if (tokens[index].getType() == closingBracket) counter--;

            if (counter == 0) {
                break;
            }
        }
        if (counter != 0) {
            throw new IllegalArgumentException(""+counter);
        }

//        System.out.println("Searching for "+closingBracket+" in "+Arrays.toString(tokens)+" found at "+index+"="+tokens[index]);
        return index;

    }

    public static int indexOfClosingBracket(Token[] tokens, int start) {
        int result;
        switch (tokens[start].getType()) {
            case BRACKETS_R_OPEN:
                result = getClosingIndex(tokens, start, Type.BRACKETS_R_OPEN, Type.BRACKETS_R_CLOSED);
                break;
            case BRACKETS_C_OPEN:
                result = getClosingIndex(tokens, start, Type.BRACKETS_C_OPEN, Type.BRACKETS_C_CLOSED);
                break;
            case BRACKETS_S_OPEN:
                result = getClosingIndex(tokens, start, Type.BRACKETS_S_OPEN, Type.BRACKETS_S_CLOSED);
                break;
            default:
                throw new IllegalArgumentException(tokens[start].getType().name());

        }
        return result;
    }


    public static boolean isUnaryOP(char c) {
        return (c == '&' || c == '|' || c == '!' || isABracket(c));
    }

    public static boolean isABracket(Token token) {
        return isABracket(token.getType().toString().charAt(0));
    }

    public static boolean isABracket(char c) {
        return c == '(' || c == ')' || c == '{' || c == '}' || c == '[' || c == ']';
    }

    public static boolean isPossibleOperator(char c) {
        return c == 'E' || c == 'A' || c == '&' || c == '!' ||
                c == 'X' || c == 'G' || c == 'F' ||
                c == '|' || c == '-' || c == '>' || c == 'U'
                || c == ',' ||
                isABracket(c);
    }

    public static int getAffixOfOperator(Type type) {
        int result = 0;

        int NO_OP = 0;
        int PREFIX = 1;
        int INFIX = 2;
        int UNTIL = 3;

        if (type == Type.EF || type == Type.EG || type == Type.EX ||
                type == Type.AF || type == Type.AG || type == Type.AX || type == Type.NOT) {
            result = 1;
        } else if (type == Type.AND || type == Type.OR || type == Type.IMPL || type == Type.COMMA) {
            result = 2;
        }else if (type == Type.AU_START || type == Type.AU_END || type == Type.EU_START || type == Type.EU_END|| type == Type.AU|| type == Type.EU) {
            result = 3;
        }
        return result;
    }

    public static String printSimpleStates(List<KripkeState> states){
        String result="";
        for (KripkeState state : states) {
            result +=state.getName()+",";
        }
        if(result.length()>2)result = result.substring(0, result.length()-1);
        if(result.equals(""))result="-";
        return  result;
    }


    //    public static is

}
