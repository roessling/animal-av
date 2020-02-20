package generators.misc.ershov.parser;

class Token {
    private TokenType type;
    private String sequence;

    Token(TokenType type, String sequence) {
        this.type = type;
        this.sequence = sequence;
    }

    TokenType getTokenType() {
        return type;
    }

    String getSequence() {
        return sequence;
    }
}
