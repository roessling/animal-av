package interactionsupport.parser;

import interactionsupport.controllers.InteractionController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

/**
 * The main parser routines are encapsulated in this class. Use it for
 * comfortably parsing a file structure using a given StreamTokenizer.
 * 
 * @author Gina Haeussge, huge(at)rbg.informatik.tu-darmstadt.de
 */
public class Parser2 implements ParserInterface {

	/** The StreamTokenizer */
	protected StreamTokenizer stok;

	/** The filename of the file to be parsed */
	protected String filename;

	/** Use case-insensitive parsing or not */
	private boolean ignoreCase;

	/** The character used for line-comments */
	private char commentChar;

	/** The character used for quoting strings */
	private char quoteChar;

	// ~ Constructors --------------------------------------------

	/**
	 * Constructor
	 * 
	 * @param s
	 *          The StreamTokenizer to use
	 */
	public Parser2(StreamTokenizer s) {
		this(s, "");
	}

	/**
	 * Constructor
	 * 
	 * @param s
	 *          The StreamTokenizer to use
	 * @param fname
	 *          the filename of the underlying stream
	 */
	public Parser2(StreamTokenizer s, String fname) {
		stok = s;
		quoteChar = '"';
		commentChar = '#';
		filename = fname;
		resetParserSettings();
	}

	/**
	 * Looks for a character in the tokenizer. If none is found, an exception is
	 * thrown.
	 * 
	 * @return The found character.
	 * 
	 * @throws IOException
	 *           Thrown if something goes wrong with the Tokenizer.
	 * @throws BadSyntaxException
	 *           Thrown if something different than a single character is found.
	 */
	public char getChar() throws IOException, BadSyntaxException {
		stok.nextToken();

		if (stok.ttype == StreamTokenizer.TT_WORD) {
			throw generateException("wrongTypeFound", new String[] {
					InteractionController.translateMessage("charType"),
					InteractionController.translateMessage("wordType") });
		} else if (stok.ttype == StreamTokenizer.TT_NUMBER) {
			throw generateException("wrongTypeFound", new String[] {
					InteractionController.translateMessage("charType"),
					InteractionController.translateMessage("numericType") });
		} else if (stok.ttype == StreamTokenizer.TT_EOL) {
			throw generateException("wrongTypeFound", new String[] {
					InteractionController.translateMessage("charType"),
					InteractionController.translateMessage("eolType") });
		} else if (stok.ttype == StreamTokenizer.TT_EOF) {
			throw generateException("wrongTypeFound", new String[] {
					InteractionController.translateMessage("charType"),
					InteractionController.translateMessage("eofType") });
		}

		return (char) stok.ttype;
	}

	/**
	 * Sets the char used for quoting strings.
	 * 
	 * @param comment
	 *          The char that should indicate one-line-comments.
	 */
	public void setCommentChar(char comment) {
		commentChar = comment;
		resetParserSettings();
	}

	/**
	 * Looks for an EOL in the tokenizer. If none is found, an exception is
	 * thrown.
	 * 
	 * @return A boolean value being true if an EOL was found.
	 * 
	 * @throws IOException
	 *           Thrown if something goes wrong with the Tokenizer.
	 * @throws BadSyntaxException
	 *           Thrown if no eol is found.
	 */
	public boolean getEOL() throws IOException, BadSyntaxException {
		stok.nextToken();

		if ((stok.ttype == 13) && (stok.ttype != StreamTokenizer.TT_EOL)) {
			stok.nextToken(); // remove double coding of Windows systems (two chars)
		}

		if ((stok.ttype == StreamTokenizer.TT_EOL) || (stok.ttype == 13)) {
			return true;
		}

		throw generateException("eolExpected");
	}

	/**
	 * Looks for an EOL or an EOF in the tokenizer. If neither is found, an
	 * exception is thrown. If an EOF is found, it is pushed back on the
	 * tokenizer.
	 * 
	 * @return A boolean value being true if an EOL or EOF was found.
	 * 
	 * @throws IOException
	 *           Thrown if something goes wrong with the Tokenizer.
	 * @throws BadSyntaxException
	 *           Thrown if neither eol nor eof is found.
	 */
	public boolean getEOX() throws IOException, BadSyntaxException {
		stok.nextToken();

		if ((stok.ttype == 13) && (stok.ttype != StreamTokenizer.TT_EOL)) {
			stok.nextToken();
		}

		if ((stok.ttype == StreamTokenizer.TT_EOL) || (stok.ttype == 13)) {
			return true;
		} else if (stok.ttype == StreamTokenizer.TT_EOF) {
			stok.pushBack();

			return true;
		} else {
			throw generateException("eolExpected");
		}
	}

	/**
	 * Sets whether to do case-sensitive matching (false) or not (true)
	 * 
	 * @param tf
	 *          Switches case-insensitive parsing on and off
	 */
	public void setIgnoreCase(boolean tf) {
		ignoreCase = tf;
	}

	/**
	 * Looks up a given keyword in the tokenizer. If it is NOT there, an exception
	 * will be thrown to indicate wrong syntax.
	 * 
	 * @param keyword
	 *          The keyword that should be matched against.
	 * 
	 * @return true If keyword was found, otherwise false. Quite needless with
	 *         this method throwing an exception if the keyword is not found, but
	 *         it was implemented nevertheless to keep this method synchron with
	 *         its getOptionalKeyword- twin.
	 * 
	 * @throws IOException
	 *           Thrown if something goes wrong with the Tokenizer.
	 * @throws BadSyntaxException
	 *           Thrown if the given keyword is not found.
	 */
	public boolean getKeyword(String keyword) throws IOException,
			BadSyntaxException {
		stok.nextToken();

		if (stok.ttype == StreamTokenizer.TT_WORD) {
			if ((stok.sval).equals(keyword)) {
				return (true);
			} else if ((stok.sval).equalsIgnoreCase(keyword) && ignoreCase) {
				return (true);
			} else {
				throw generateException("keywordError", new String[] { keyword,
						stok.sval });
			}
		}
		throw generateException("keywordError2");
	}

	/**
	 * Checks for an integer in the tokenizer. Throws an exception if something
	 * different than a number is found.
	 * 
	 * @return The found integer value.
	 * 
	 * @throws IOException
	 *           Thrown if something goes wrong with the Tokenizer.
	 * @throws BadSyntaxException
	 *           Thrown if something different than a number was found.
	 */
	public int getNumber() throws IOException, BadSyntaxException {
		stok.nextToken();

		if (stok.ttype == StreamTokenizer.TT_NUMBER) {
			return ((int) stok.nval);
		}
		throw generateException("numberExpected");
	}

	/**
	 * Looks for an optional character in the tokenizer.
	 * 
	 * @return The found character.
	 * 
	 * @throws IOException
	 *           Thrown if something goes wrong with the Tokenizer.
	 */
	public char getOptionalChar() throws IOException {
		stok.nextToken();

		int token = stok.ttype;

		if ((token == StreamTokenizer.TT_WORD)
				|| (token == StreamTokenizer.TT_NUMBER)
				|| (token == StreamTokenizer.TT_EOL)
				|| (token == StreamTokenizer.TT_EOF)) {
			stok.pushBack();

			return (char) 0;
		}

		return (char) stok.ttype;
	}

	/**
	 * Looks for an optional EOL in the tokenizer.
	 * 
	 * @return A boolean value being true if an EOL was found, false if otherwise.
	 * 
	 * @throws IOException
	 *           Thrown if something goes wrong with the Tokenizer.
	 */
	public boolean getOptionalEOL() throws IOException {
		stok.nextToken();

		if ((stok.ttype == 13) && (stok.ttype != StreamTokenizer.TT_EOL)) {
			stok.nextToken();
		}

		if ((stok.ttype == StreamTokenizer.TT_EOL) || (stok.ttype == 13)) {
			return true;
		}
		stok.pushBack();

		return false;
	}

	/**
	 * Looks for an optional EOL or EOF in the tokenizer. If an EOF is found, it
	 * is pushed back on the tokenizer.
	 * 
	 * @return A boolean value being true if an EOL or EOF was found.
	 * 
	 * @throws IOException
	 *           Thrown if something goes wrong with the Tokenizer.
	 */
	public boolean getOptionalEOX() throws IOException {
		stok.nextToken();

		if ((stok.ttype == 13) && (stok.ttype != StreamTokenizer.TT_EOL)) {
			stok.nextToken();
		}

		if ((stok.ttype == StreamTokenizer.TT_EOF) || (stok.ttype == 13)) {
			stok.pushBack();

			return true;
		}
		stok.pushBack();

		return false;
	}

	/**
	 * Looks up a given optional keyword in the Tokenizer.
	 * 
	 * @param keyword
	 *          The keyword to match against
	 * 
	 * @return True if the keyword was found, false otherwise.
	 * 
	 * @throws IOException
	 *           Thrown if something mad goes on with the Tokenizer.
	 */
	public boolean getOptionalKeyword(String keyword) throws IOException {
		stok.nextToken();

		if (stok.ttype == StreamTokenizer.TT_WORD) {
			if ((stok.sval).equals(keyword)) {
				return true;
			} else if ((stok.sval).equalsIgnoreCase(keyword) && ignoreCase) {
				return true;
			} else {
				stok.pushBack();

				return false;
			}
		}
		stok.pushBack();

		return false;
	}

	/**
	 * Checks for an optional integer in the tokenizer. Because this method
	 * returns only -1 if no intVal was found, use it WITH CAUTION!
	 * 
	 * @return The found integer value if any, or -1.
	 * 
	 * @throws IOException
	 *           Thrown if something goes wrong with the Tokenizer.
	 */
	public int getOptionalNumber() throws IOException {
		stok.nextToken();

		if (stok.ttype == StreamTokenizer.TT_NUMBER) {
			return ((int) stok.nval);
		}
		return -1;
	}

	/**
	 * Checks for an optional quoted string in the tokenizer.
	 * 
	 * @return The content of the quoted string.
	 * 
	 * @throws IOException
	 *           Thrown if something goes wrong with the Tokenizer.
	 * @throws BadSyntaxException
	 *           Thrown if no closing quotes are found before eof.
	 */
	public String getOptionalQuoted() throws IOException, BadSyntaxException {
		String theString = "";

		stok.nextToken();

		if (stok.ttype == quoteChar) {
			setQuotedSettings();
			stok.nextToken();

			while (stok.ttype != quoteChar) {
				if (stok.ttype == StreamTokenizer.TT_WORD) {
					theString += stok.sval;
				} else if (stok.ttype == StreamTokenizer.TT_NUMBER) {
					theString += (int) stok.nval;
				} else if (stok.ttype == StreamTokenizer.TT_EOF) {
					throw generateException("endOfQuote");
				} else {
					theString += (char) stok.ttype;
				}

				stok.nextToken();
			}

			resetParserSettings();

			return theString;
		}
		stok.pushBack();

		return "";
	}

	/**
	 * Looks for optional whitespace in the tokenizer. Whitespace is defined as ' '
	 * or '\t'
	 * 
	 * @return A String consisting of the found whitespace.
	 * 
	 * @throws IOException
	 *           Thrown if something goes wrong with the Tokenizer.
	 */
	public String getOptionalWhitespace() throws IOException {
		String whitespace = "";

		stok.nextToken();

		if ((stok.ttype == ' ') || (stok.ttype == '\t')) {
			while ((stok.ttype == ' ') || (stok.ttype == '\t')) {
				whitespace += (char) stok.ttype;
				stok.nextToken();
			}

			stok.pushBack();

			return whitespace;
		}
		stok.pushBack();

		return "";
	}

	/**
	 * Checks for an optional word in the tokenizer.
	 * 
	 * @return The word found.
	 * 
	 * @throws IOException
	 *           Thrown if something goes wrong with the Tokenizer.
	 */
	public String getOptionalWord() throws IOException {
		stok.nextToken();

		if (stok.ttype == StreamTokenizer.TT_WORD) {
			return (stok.sval);
		}
		stok.pushBack();

		return "";
	}

	// ~ Methods -------------------------------------------------

	/**
	 * Sets the char used for quoting strings.
	 * 
	 * @param quote
	 *          The char that should indicate quoted strings.
	 */
	public void setQuoteChar(char quote) {
		quoteChar = quote;
	}

	/**
	 * Checks for a quoted string in the tokenizer. If there is none, an execption
	 * is thrown.
	 * 
	 * @return The content of the quoted string.
	 * 
	 * @throws IOException
	 *           Thrown if something goes wrong with the Tokenizer.
	 * @throws BadSyntaxException
	 *           Thrown if something differeent than a quoted string is found, or
	 *           there are no closing quotes before eof.
	 */
	public String getQuoted() throws IOException, BadSyntaxException {
		String theString = "";

		stok.nextToken();

		if (stok.ttype == quoteChar) {
			setQuotedSettings();
			stok.nextToken();

			while (stok.ttype != quoteChar) {
				if (stok.ttype == StreamTokenizer.TT_WORD) {
					theString += stok.sval;
				} else if (stok.ttype == StreamTokenizer.TT_NUMBER) {
				  String s = String.valueOf(stok.nval);
				  if (s.endsWith(".0"))
				    s = s.substring(0, s.length() - 2);
				  theString += s;
//					theString += (int) stok.nval;
				} else if (stok.ttype == StreamTokenizer.TT_EOF) {
					generateException("endOfQuote");
				} else {
					theString += (char) stok.ttype;
				}

				stok.nextToken();
			}

			resetParserSettings();

			return theString;
		}
		throw generateException("quotedString");
	}

	/**
	 * Switch tokenizer to recognizing numbers as parts of strings.
	 */
	public void setQuotedSettings() {
		resetParserSettings();
		stok.wordChars('0', '9');
	}

	/**
	 * Looks for whitespace in the tokenizer. If none is found, an exception is
	 * thrown. Whitespace is defined as ' ' or '\t'
	 * 
	 * @return A String consisting of the found whitespace.
	 * 
	 * @throws IOException
	 *           Thrown if something goes wrong with the Tokenizer.
	 * @throws BadSyntaxException
	 *           Thrown if no whitespace is found.
	 */
	public String getWhitespace() throws IOException, BadSyntaxException {
		String whitespace = "";

		stok.nextToken();

		if ((stok.ttype == ' ') || (stok.ttype == '\t')) {
			while ((stok.ttype == ' ') || (stok.ttype == '\t')) {
				whitespace += (char) stok.ttype;
				stok.nextToken();
			}

			stok.pushBack();

			return whitespace;
		}
		stok.pushBack();
		throw generateException("whitespaceExpected");
	}

	/**
	 * Checks for a word in the tokenizer. If none is found, an exception is
	 * thrown.
	 * 
	 * @return The word found.
	 * 
	 * @throws IOException
	 *           Thrown if something goes wrong with the Tokenizer.
	 * @throws BadSyntaxException
	 *           Thrown if no word but something different was found.
	 */
	public String getWord() throws IOException, BadSyntaxException {
		stok.nextToken();

		if (stok.ttype == StreamTokenizer.TT_WORD) {
			return (stok.sval);
		}
		throw generateException("numberExpected");
	}

	/**
	 * Switch tokenizer to parsing numbers in its normal way.
	 */
	public void resetParserSettings() {
		stok.resetSyntax();
		stok.wordChars('a', 'z');
		stok.wordChars('A', 'Z');
		stok.parseNumbers();
		stok.eolIsSignificant(true);
//		stok.ordinaryChar(quoteChar);
		stok.commentChar(commentChar);
	}

	protected BadSyntaxException generateException(String key, Object[] params) {
		return generateException(key, params, true);
	}

	protected BadSyntaxException generateException(String key, Object[] params,
			boolean addLineNo) {
		if (!addLineNo || (params == null)) {
			return new BadSyntaxException(InteractionController.translateMessage(key,
					params));
		}
		int nrElems = params.length;
		Object[] elements = new Object[nrElems + 2];
		System.arraycopy(params, 0, elements, 0, nrElems);
		elements[nrElems] = filename;
		elements[nrElems + 1] = String.valueOf(stok.lineno());

		return new BadSyntaxException(InteractionController.translateMessage(key,
				elements));

	}

	protected BadSyntaxException generateException(String key) {
		return generateException(key, new String[] { filename,
				String.valueOf(stok.lineno()) });
	}

	protected BadSyntaxException generateException(String key, String singleParam) {
		return generateException(key, new String[] { singleParam, filename,
				String.valueOf(stok.lineno()) }, false);
	}
	
	public static void main(String[] args) {
	  try {
	    BufferedReader br = new BufferedReader(new FileReader("/Users/roessling/Desktop/AirPort/demo.txt"));
	    StreamTokenizer s = new StreamTokenizer(br);
	    s.quoteChar('\"');
//	    Parser p = new Parser(s, "demo.txt");
	    int token;
	    while ((token = s.nextToken()) != StreamTokenizer.TT_EOF) {
	      if (token == StreamTokenizer.TT_WORD || token == '"')
	        System.out.println("w: " +s.sval);
	      else if (token == StreamTokenizer.TT_NUMBER)
	        System.out.println("nr: " +s.nval);
	      else
	        System.out.println("x: " +((char)s.ttype));
	    }
	    br.close();
	  } catch(Exception e) {
	    System.err.println(e.getMessage());
	  }
	}
}
