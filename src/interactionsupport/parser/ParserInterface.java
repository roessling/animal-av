package interactionsupport.parser;

import java.io.IOException;

/**
 * The Parser Interface
 */
public interface ParserInterface {
  /**
   * Get a single character
   *
   * @return the single character read in
   *
   * @throws IOException Thrown if something goes wrong with the Tokenizer.
   * @throws BadSyntaxException Thrown if something different than a char was
   *         found.
   */
  public char getChar() throws IOException, BadSyntaxException;

  /**
   * Get an EOL
   *
   * @return true if EOL was found, else false
   *
   * @throws IOException Thrown if something goes wrong with the Tokenizer.
   * @throws BadSyntaxException Thrown if something different than an EOL was
   *         found.
   */
  public boolean getEOL() throws IOException, BadSyntaxException;

  /**
   * Get an EOL or an EOF
   *
   * @return true if a EOF or EOL was found, else false
   *
   * @throws IOException Thrown if something goes wrong with the Tokenizer.
   * @throws BadSyntaxException Thrown if something different than a number was
   *         found.
   */
  public boolean getEOX() throws IOException, BadSyntaxException;

  /**
   * Get a keyword
   *
   * @param keyword the keyword that the parser is looking for
   *
   * @return true if the keyword was found, else false
   *
   * @throws IOException Thrown if something goes wrong with the Tokenizer.
   * @throws BadSyntaxException Thrown if something different than a number was
   *         found.
   */
  public boolean getKeyword(String keyword)
    throws IOException, BadSyntaxException;

  /**
   * Get a number
   *
   * @return the number read in (if any)
   *
   * @throws IOException Thrown if something goes wrong with the Tokenizer.
   * @throws BadSyntaxException Thrown if something different than a number was
   *         found.
   */
  public int getNumber() throws IOException, BadSyntaxException;

  /**
   * Get an optional single character
   *
   * @return the optional character, else '0'
   *
   * @throws IOException Thrown if something goes wrong with the Tokenizer.
   */
  public char getOptionalChar() throws IOException;

  /**
   * Get an optional EOL
   *
   * @return true if an EOL was found, else false
   *
   * @throws IOException Thrown if something goes wrong with the Tokenizer.
   */
  public boolean getOptionalEOL() throws IOException;

  /**
   * Get an optional EOL or an optional EOF
   *
   * @return true if an EOL or EOF was read, else false
   *
   * @throws IOException Thrown if something goes wrong with the Tokenizer.
   */
  public boolean getOptionalEOX() throws IOException;

  /**
   * Get an optional keyword
   *
   * @param keyword the optional expected keyword
   *
   * @return true if the keyword was found, else false
   *
   * @throws IOException Thrown if something goes wrong with the Tokenizer.
   */
  public boolean getOptionalKeyword(String keyword) throws IOException;

  /**
   * Get an optional number
   *
   * @return the number read in, if any - else, it will be -1
   *
   * @throws IOException Thrown if something goes wrong with the Tokenizer.
   */
  public int getOptionalNumber() throws IOException;

  /**
   * Get an optional quoted String
   *
   * @return the value of the optional String, if any is present, else  an
   *         empty (but non-null!) String
   *
   * @throws IOException Thrown if something goes wrong with the Tokenizer.
   * @throws BadSyntaxException if the syntax of the file is incorrect
   */
  public String getOptionalQuoted() throws IOException, BadSyntaxException;

  /**
   * Get optional whitespace
   *
   * @return the content of the whitespace, if any is present
   *
   * @throws IOException Thrown if something goes wrong with the Tokenizer.
   */
  public String getOptionalWhitespace() throws IOException;

  /**
   * Get an optional word
   *
   * @return the optional word read in, if any - else, the method will return
   *         an empty String (Parser.EMPTY_STRING)
   *
   * @throws IOException Thrown if something goes wrong with the Tokenizer.
   */
  public String getOptionalWord() throws IOException;

  /**
   * Get a quoted String
   *
   * @return the value of the quoted String
   *
   * @throws IOException Thrown if something goes wrong with the Tokenizer.
   * @throws BadSyntaxException if the syntax of the file is incorrect
   */
  public String getQuoted() throws IOException, BadSyntaxException;

  /**
   * Get whitespace
   *
   * @return the whitespace read in, if any; else, the method will return
   *         Parser.EMPTY_STRING
   *
   * @throws IOException Thrown if something goes wrong with the Tokenizer.
   * @throws BadSyntaxException if the syntax of the file is incorrect
   */
  public String getWhitespace() throws IOException, BadSyntaxException;

  /**
   * Get a word
   *
   * @return the word read in
   *
   * @throws IOException Thrown if something goes wrong with the Tokenizer.
   * @throws BadSyntaxException if the syntax of the file is incorrect
   */
  public String getWord() throws IOException, BadSyntaxException;
}
