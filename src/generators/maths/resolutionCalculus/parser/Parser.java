package generators.maths.resolutionCalculus.parser;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import generators.maths.resolutionCalculus.Clause;

/**
 * LL1 Parser for the following grammar:
 * <p>
 * Start non-terminal S / non-terminals S, Clause, Literal / terminals '{', '}',
 * ',', Unicode
 * <p>
 * S -> Clause (',' Clause)*
 * <p>
 * Clause -> '{' Literal (',' Literal)* '}'
 * <p>
 * Literal -> Unicode*
 * <p>
 * Example: {p, q}, {!s, !p}, {!q}
 * 
 * 
 * @author Fabian Bauer
 *
 */
public class Parser {

	private Token currentToken;
	private Deque<Token> tokenStream;
	private ArrayList<Clause> clauses;

	public Parser(Deque<Token> tokenStream) {
		this.tokenStream = tokenStream;
		this.currentToken = tokenStream.poll();
		this.clauses = new ArrayList<>();
		parse();
	}

	public List<Clause> getClauses() {
		return clauses;
	}

	private void accept(byte tokenType) {
		if (currentToken == null)
			throw new IllegalArgumentException("Expected " + tokenType + " at the end of the input");
		if (currentToken.type != tokenType)
			throw new IllegalArgumentException(
					"Expected " + tokenType + " at " + currentToken.source + "@" + currentToken.col);
		currentToken = tokenStream.poll();
	}

	private void acceptIt() {
		currentToken = tokenStream.poll();
	}

	private void parse() {
		parseClause();

		while (currentToken != null) {
			accept(Token.COMMA);
			parseClause();
		}
	}

	private void parseClause() {
		accept(Token.LBRACKET);

		Clause c = new Clause();
		parseLiteral(c);
		while (currentToken != null && currentToken.type == Token.COMMA) {
			acceptIt();
			parseLiteral(c);
		}
		clauses.add(c);

		accept(Token.RBRACKET);
	}

	private void parseLiteral(Clause c) {
		Token current = currentToken;
		accept(Token.LITERAL);
		c.addLiteral(current.source);
	}

}
