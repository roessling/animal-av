package generators.maths.resolutionCalculus.parser;

import java.util.Deque;
import java.util.LinkedList;

public final class Token {

	public static final byte LBRACKET = 0;
	public static final byte RBRACKET = 1;
	public static final byte COMMA = 2;
	public static final byte LITERAL = 3;

	public final String source;
	public final int col;
	public final byte type;

	public Token(String source, int col, byte type) {
		this.source = source;
		this.col = col;
		this.type = type;
	}

	public static Deque<Token> toTokens(String s) {
		char[] input = s.replace(" ", "").toCharArray();
		LinkedList<Token> tokens = new LinkedList<>();

		for (int i = 0; i < input.length; ++i) {
			if (input[i] == ',')
				tokens.add(new Token(",", i, Token.COMMA));
			else if (input[i] == '{')
				tokens.add(new Token("{", i, Token.LBRACKET));
			else if (input[i] == '}')
				tokens.add(new Token("}", i, Token.RBRACKET));
			else {
				StringBuilder builder = new StringBuilder();
				for (; i < input.length && input[i] != '{' && input[i] != '}' && input[i] != ','; ++i)
					builder.append(input[i]);
				tokens.add(new Token(builder.toString(), i, Token.LITERAL));
				// Alternative: Loop skewing
				i--;
			}
		}

		return tokens;
	}

}
