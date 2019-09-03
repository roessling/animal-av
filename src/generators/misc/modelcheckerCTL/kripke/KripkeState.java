/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.misc.modelcheckerCTL.kripke;

import generators.misc.modelcheckerCTL.token.Token;

import java.util.ArrayList;
import java.util.List;

public class KripkeState {
    private final Token token;
    private List<Token> terminals;
    private final int id;

    public KripkeState(Token token, int id) {
        this.token = token;
        this.id = id;
        this.terminals = new ArrayList<>();
    }

    public String getName() {
        return token.getFormula();
    }

    public List<Token> getTerminals() {
        return terminals;
    }

    public int getId() {
        return id;
    }

    public Token getToken() {
        return token;
    }

    public void setTerminals(List<Token> terminals) {
        this.terminals = terminals;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof KripkeState) &&
                (this.getName().equals(((KripkeState) o).getName())&&this.getId()==((KripkeState) o).getId());
    }



    @Override
    public String toString() {
        return "("+this.getName()+", "+terminals.toString().replace('[','{').replace(']','}')+")";
    }

}
