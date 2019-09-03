/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.misc.modelcheckerCTL.token;

public class Token {
    String formula;
    Type type;

    public Token(Type type, String str) {
        this.formula = str;
        this.type = type;
    }

    public Token(Type type) {
        this.formula = type.toString();
        this.type = type;
    }

    public String getFormula() {
        return formula;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return formula;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Token) &&
                (this.formula.equals(((Token) o).getFormula()) && this.type == ((Token) o).getType());
    }
}
