/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.misc.modelcheckerCTL.token;

import java.util.Comparator;

public class TokenComparator implements Comparator<Token> {


    @Override
    public int compare(Token o1, Token o2) {
        return o1.getFormula().compareTo(o2.getFormula());
    }
}
