package generators.misc.ershov.parser.exception;

public class TokenizerException extends RuntimeException {

    public TokenizerException(String expr) {
        super("Error while tokenizing: " + expr);
    }
}
