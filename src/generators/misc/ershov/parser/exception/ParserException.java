package generators.misc.ershov.parser.exception;

public class ParserException extends RuntimeException {

    public ParserException(String message) {
        super("Error while parsing: " + message);
    }
}
