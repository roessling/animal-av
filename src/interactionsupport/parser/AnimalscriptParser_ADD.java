package interactionsupport.parser;

import interactionsupport.controllers.InteractionController;
import interactionsupport.models.AnswerModel;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.HtmlDocumentationModel;
import interactionsupport.models.InteractionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.QuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.net.URL;
import java.util.Hashtable;

/**
 * Parses interaction definition files written in animal script.
 * 
 * @author Gina Haeussge, Simon Sprankel
 *         <sprankel@rbg.informatik.tu-darmstadt.de>
 */
public class AnimalscriptParser_ADD implements LanguageParserInterface {

  /** name of the language this parser parses */
  private final String                          LANGUAGE       = "AnimalScript";

  /** Hashtable holding all {@link QuestionGroupModel} objects, identifying them by their ID */
  private Hashtable<String, QuestionGroupModel> questionGroups = new Hashtable<String, QuestionGroupModel>();

  /** An instance of the Parser class, used for easy parsing */
  private Parser                                parser;

  /** The StreamTokenizer used for parsing the file */
  private StreamTokenizer                       stok;

  public AnimalscriptParser_ADD() {
    // do nothing
  }

  /**
   * Returns a Hash containing the information about question groups
   * 
   * @return A Hash with the group information
   */
  public Hashtable<String, QuestionGroupModel> getQuestionGroups() {
    return questionGroups;
  }

  /**
   * The main parser routine.
   * 
   * @param filename
   *          The filename of the file that should be parsed
   * 
   * @return A hash containing all found objects.
   */
  public Hashtable<String, InteractionModel> parse(String filename) {
    // create storage object for all interaction models
    Hashtable<String, InteractionModel> interactionModels = new Hashtable<String, InteractionModel>();

    URL theURL;
    String word = "";
    BufferedReader theReader;
    int token = -42;
    try {
      word = "";
      // if we have a valid URL
      if (filename.startsWith("http://") || filename.startsWith("ftp://")
          || filename.startsWith("file://")) {
        theURL = new URL(filename);
        theReader = new BufferedReader(new InputStreamReader(
            theURL.openStream()));

        // else we got a local file
      } else {
        theReader = new BufferedReader(new FileReader(filename));
      }

      // create our Tokenizer
      stok = new StreamTokenizer(theReader);
      stok.quoteChar('"');
/*      
      // create an instance of the parser
      parser = new Parser(stok, filename);
      parser.setIgnoreCase(true);
*/
      // parse the whole file
      while (stok.ttype != StreamTokenizer.TT_EOF) {
        while (getOptionalEOL()) {
          // do nothing, just skip the empty lines
        }

        stok.pushBack();
        // break if the end of the file is reached
        if (stok.ttype == StreamTokenizer.TT_EOF)
          break;
        token = stok.nextToken();
        if (token == StreamTokenizer.TT_WORD || token == '"')
          word = stok.sval;//parser.getWord();

        if ("documentation".equalsIgnoreCase(word)) {
          InteractionModel interactionModel = parseDocumentation();
          interactionModels.put(interactionModel.getID(), interactionModel);
        } else if ("fillInBlanksQuestion".equalsIgnoreCase(word)) {
          InteractionModel interactionModel = parseFIBQuestion();
          interactionModels.put(interactionModel.getID(), interactionModel);
        } else if ("multipleChoiceQuestion".equalsIgnoreCase(word)) {
          InteractionModel interactionModel = parseMCQuestion();
          interactionModels.put(interactionModel.getID(), interactionModel);
        } else if ("multipleSelectionQuestion".equalsIgnoreCase(word)) {
          InteractionModel interactionModel = parseMSQuestion();
          interactionModels.put(interactionModel.getID(), interactionModel);
        } else if ("trueFalseQuestion".equalsIgnoreCase(word)) {
          InteractionModel interactionModel = parseTFQuestion();
          interactionModels.put(interactionModel.getID(), interactionModel);
        } else if ("questionGroup".equalsIgnoreCase(word)) {
          QuestionGroupModel questionGroup = parseQuestionGroup();
          questionGroups.put(questionGroup.getID(), questionGroup);
        } else {
          throw new IOException("Keyword expected, got " +word);//parser.generateException("keywordExpected", word);
        }
      }
    } catch (BadSyntaxException bse) {
      System.out.println(InteractionController.translateMessage(
          "syntaxError",
          new String[] { bse.getMessage(), filename,
              String.valueOf(stok.lineno()) }));
    } catch (IOException ioe) {
      System.out.println(InteractionController.translateMessage(
          "genericException",
          new String[] { getClass().getName(), ioe.getMessage(), filename,
              String.valueOf(stok.lineno()) }));
    }

    return interactionModels;
  }

  /**
   * returns the name of the language
   * 
   * @return a String containing the languages name
   */
  public String toString() {
    return LANGUAGE;
  }

  /**
   * Does an EOL lookup using the parser instance and calls the lines of the
   * parsed file by doing so.
   * 
   * @return A boolean value indicating whether there was an eol or not.
   * 
   * @throws IOException
   * @throws BadSyntaxException
   */
  private boolean getEOL() throws IOException, BadSyntaxException {
    stok.nextToken();
    if (stok.ttype == StreamTokenizer.TT_EOL)
      return true;
    return false;
//    parser.getOptionalWhitespace();
//
//    if (parser.getEOL()) {
//      parser.getOptionalWhitespace();
//
//      return true;
//    }
//
//    return false;
  }

  /**
   * Does an EOL lookup using the parser instance and calls the lines of the
   * parsed file by doing so. The eol is optional.
   * 
   * @return A boolean value indicating whether there was an eol or not.
   * 
   * @throws IOException
   */
  private boolean getOptionalEOL() throws IOException {
    stok.nextToken();
    if (stok.ttype == StreamTokenizer.TT_EOL) {
      return true;
    }
    stok.pushBack();
    return false;
//    parser.getOptionalWhitespace();
//
//    if (parser.getOptionalEOL()) {
//      parser.getOptionalWhitespace();
//
//      return true;
//    }
//
//    return false;
  }

  /**
   * Parses the documentation keyword.
   * 
   * @throws IOException
   * @throws BadSyntaxException
   */
  private HtmlDocumentationModel parseDocumentation() throws IOException,
      BadSyntaxException {
    String docID = null;
    int token = stok.nextToken();
    if (token == StreamTokenizer.TT_WORD || token == '"')
      docID = stok.sval;
    HtmlDocumentationModel doc = new HtmlDocumentationModel(docID);
    getEOL();
    token = stok.nextToken();
    if (token == StreamTokenizer.TT_WORD && "url".equalsIgnoreCase(stok.sval))
      return null;
    
    // get the documentation identifier
    parser.getWhitespace();
    String doc2ID = parser.getQuoted();
    //HtmlDocumentationModel
    HtmlDocumentationModel doc2 = new HtmlDocumentationModel(docID);
    getEOL();
    parser.getOptionalWhitespace();

    // get the URL
    parser.getKeyword("url");
    parser.getWhitespace();
    String url = parser.getQuoted();
    doc.setLinkAddress(url);
    getEOL();
    parser.getOptionalWhitespace();

    parser.getKeyword("endDocumentation");
    getEOL();
    parser.getOptionalWhitespace();

    return doc;
  }

  /**
   * Parses the question ID and saves it in the given question instance.<br />
   * Note: This method should be called <i>after</i> the question ID keyword has
   * been parsed.
   * 
   * @param question
   *          the question in which the parsed ID should be saved
   * @return the parsed question ID
   * @throws IOException
   * @throws BadSyntaxException
   */
  private String parseQuestionID(QuestionModel question) throws IOException,
      BadSyntaxException {
    // get the interactionID
    parser.getWhitespace();
    String id = parser.getQuoted();
    question.setID(id);
    getEOL();
    parser.getOptionalWhitespace();
    return id;
  }

  /**
   * Parses the question prompt and saves it in the given question instance.<br />
   * Note: This method should be called <i>after</i> the question prompt keyword
   * has been parsed.
   * 
   * @param question
   *          the question in which the parsed prompt should be saved
   * @return the parsed question prompt
   * @throws IOException
   * @throws BadSyntaxException
   */
  private String parseQuestionPrompt(QuestionModel question)
      throws IOException, BadSyntaxException {
    parser.getWhitespace();
    String prompt = parser.getQuoted();
    question.setPrompt(prompt);
    getEOL();
    parser.getOptionalWhitespace();
    return prompt;
  }

  /**
   * Parses the question group and saves it in the given question inctance.<br />
   * Note: This method should be called <i>after</i> the question group keyword
   * has been parsed.
   * 
   * @param question
   *          the question in which the parsed group should be saved
   * @return the parsed question group ID
   * @throws IOException
   * @throws BadSyntaxException
   */
  private String parseQuestionGroupID(QuestionModel question)
      throws IOException, BadSyntaxException {
    parser.getWhitespace();
    String groupID = parser.getQuoted();
    question.setGroupID(groupID);
    getEOL();
    parser.getOptionalWhitespace();
    return groupID;
  }

  /**
   * Parses the number of tries and saves them in the given question instance.<br />
   * Note: This method should be called <i>after</i> the number of tries keyword
   * has been parsed.
   * 
   * @param question
   *          the question in which the number of tries should be saved
   * @return the parsed number of tries
   * @throws IOException
   * @throws BadSyntaxException
   */
  private int parseNumberOfTries(QuestionModel question) throws IOException,
      BadSyntaxException {
    parser.getWhitespace();
    int numberOfTries = parser.getNumber();
    question.setNumberOfTries(numberOfTries);
    getEOL();
    parser.getOptionalWhitespace();
    return numberOfTries;
  }

  /**
   * Parses an answer and saves it in the given question instance.<br />
   * Note: This method should be called <i>after</i> the answer keyword has been
   * parsed.
   * 
   * @param question
   *          the question in which the parsed answer should be saved
   * @throws IOException
   * @throws BadSyntaxException
   */
  private AnswerModel parseAnswer(QuestionModel question) throws IOException,
      BadSyntaxException {
    // create a new answer object
    AnswerModel answer = new AnswerModel();

    parser.getWhitespace();
    // parse the answer ID
    String id = parser.getQuoted();
    answer.setID(id);
    getEOL();
    parser.getOptionalWhitespace();

    // the order of the keywords does not matter
    String keyword = parser.getWord();
    while (!keyword.equalsIgnoreCase("endAnswer")) {
      if (keyword.equalsIgnoreCase("points")) {
        answer.setPoints(parseAnswerPoints());
      } else if (keyword.equalsIgnoreCase("feedback")) {
        answer.setFeedback(parseAnswerFeedback());
      } else if (keyword.equalsIgnoreCase("text")) {
        answer.setText(parseAnswerText());
      }
      // get the next keyword
      keyword = parser.getWord();
    }
    // add the answer to the given question
    question.addAnswer(answer);

    parser.getEOL();
    parser.getOptionalWhitespace();

    return answer;
  }

  /**
   * Parses the text for an answer.
   * 
   * @return the parsed answer text
   * @throws IOException
   * @throws BadSyntaxException
   */
  private String parseAnswerText() throws IOException, BadSyntaxException {
    parser.getWhitespace();
    String text = parser.getQuoted();
    getEOL();
    parser.getOptionalWhitespace();
    return text;
  }

  /**
   * Parses the points for an answer.
   * 
   * @return the parsed number of points
   * @throws IOException
   * @throws BadSyntaxException
   */
  private int parseAnswerPoints() throws IOException, BadSyntaxException {
    parser.getWhitespace();
    int points = parser.getNumber();
    getEOL();
    parser.getOptionalWhitespace();
    return points;
  }

  /**
   * Parses the comment for an answer.
   * 
   * @return the parsed comment
   * @throws IOException
   * @throws BadSyntaxException
   */
  private String parseAnswerFeedback() throws IOException, BadSyntaxException {
    parser.getWhitespace();
    String comment = parser.getQuoted();
    getEOL();
    parser.getOptionalWhitespace();
    return comment;
  }

  /**
   * Parses the trueFalseQuestion keyword.
   * 
   * @return the parsed {@link TrueFalseQuestionModel}
   * @throws IOException
   * @throws BadSyntaxException
   */
  private TrueFalseQuestionModel parseTFQuestion() throws IOException,
      BadSyntaxException {
    // create the question model
    TrueFalseQuestionModel question = new TrueFalseQuestionModel(null);

    // parse the question ID first
    parseQuestionID(question);

    // the order of the other keywords does not matter
    String keyword = parser.getWord();

    // parse all keywords until endQuestion is parsed
    boolean correctAnswer = false;
    int pointsPossible = -1;
    while (!keyword.equalsIgnoreCase("endQuestion")) {
      if (keyword.equalsIgnoreCase("prompt")) {
        parseQuestionPrompt(question);
      } else if (keyword.equalsIgnoreCase("questionGroup")) {
        parseQuestionGroupID(question);
      } else if (keyword.equalsIgnoreCase("numberOfTries")) {
        parseNumberOfTries(question);
      } else if (keyword.equalsIgnoreCase("answer")) {
        AnswerModel answer = parseAnswer(question);
        if (answer.getPoints() > 0) {
          correctAnswer = Boolean.parseBoolean(answer.getID());
          pointsPossible = answer.getPoints();
        }
      }
      // get the next keyword
      keyword = parser.getWord();
    }
    if (pointsPossible > 0) {
      question.setCorrectAnswer(correctAnswer);
      question.setPointsPossible(pointsPossible);
    }

    return question;
  }

  /**
   * Parses the fillInBlanksQuestion keyword.
   * 
   * @return the parsed {@link TrueFalseQuestionModel}
   * @throws IOException
   * @throws BadSyntaxException
   */
  private FillInBlanksQuestionModel parseFIBQuestion() throws IOException,
      BadSyntaxException {
    // create the question model
    FillInBlanksQuestionModel question = new FillInBlanksQuestionModel(null);

    // parse the question ID first
    parseQuestionID(question);

    // the order of the other keywords does not matter
    String keyword = parser.getWord();

    // parse all keywords until endQuestion is parsed
    while (!keyword.equalsIgnoreCase("endQuestion")) {
      if (keyword.equalsIgnoreCase("prompt")) {
        parseQuestionPrompt(question);
      } else if (keyword.equalsIgnoreCase("questionGroup")) {
        parseQuestionGroupID(question);
      } else if (keyword.equalsIgnoreCase("numberOfTries")) {
        parseNumberOfTries(question);
      } else if (keyword.equalsIgnoreCase("answer")) {
        parseAnswer(question);
      }
      // get the next keyword
      keyword = parser.getWord();
    }

    return question;
  }

  /**
   * Parses the questionGroup keyword.
   * 
   * @return the parsed {@link QuestionGroupModel}
   * @throws IOException
   * @throws BadSyntaxException
   */
  private QuestionGroupModel parseQuestionGroup() throws IOException,
      BadSyntaxException {
    // get the group identifier
    parser.getWhitespace();
    String groupID = parser.getQuoted();
    QuestionGroupModel group = new QuestionGroupModel(groupID);
    getEOL();
    parser.getOptionalWhitespace();

    // get the number of repeats
    parser.getKeyword("numberOfRepeats");
    parser.getWhitespace();
    int repeats = parser.getNumber();
    group.setNumberOfRepeats(repeats);
    getEOL();
    parser.getOptionalWhitespace();

    parser.getKeyword("endQuestionGroup");
    getEOL();
    parser.getOptionalWhitespace();

    return group;
  }

  /**
   * Parses the multipleChoiceQuestion keyword.
   * 
   * @return the parsed {@link MultipleChoiceQuestionModel}
   * @throws IOException
   * @throws BadSyntaxException
   */
  private MultipleChoiceQuestionModel parseMCQuestion() throws IOException,
      BadSyntaxException {
    // create the question model
    MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel(null);

    // parse the question ID first
    parseQuestionID(question);

    // the order of the other keywords does not matter
    String keyword = parser.getWord();

    // parse all keywords until endQuestion is parsed
    while (!keyword.equalsIgnoreCase("endQuestion")) {
      if (keyword.equalsIgnoreCase("prompt")) {
        parseQuestionPrompt(question);
      } else if (keyword.equalsIgnoreCase("questionGroup")) {
        parseQuestionGroupID(question);
      } else if (keyword.equalsIgnoreCase("numberOfTries")) {
        parseNumberOfTries(question);
      } else if (keyword.equalsIgnoreCase("answer")) {
        parseAnswer(question);
      }
      // get the next keyword
      keyword = parser.getWord();
    }

    return question;
  }

  /**
   * Parses the multipleSelectionQuestion keyword.
   * 
   * @return the parsed {@link MultipleSelectionQuestionModel}
   * @throws IOException
   * @throws BadSyntaxException
   */
  private MultipleSelectionQuestionModel parseMSQuestion() throws IOException,
      BadSyntaxException {
    // create the question model
    MultipleSelectionQuestionModel question = new MultipleSelectionQuestionModel(
        null);

    // parse the question ID first
    parseQuestionID(question);

    // the order of the other keywords does not matter
    String keyword = parser.getWord();

    // parse all keywords until endQuestion is parsed
    while (!keyword.equalsIgnoreCase("endQuestion")) {
      if (keyword.equalsIgnoreCase("prompt")) {
        parseQuestionPrompt(question);
      } else if (keyword.equalsIgnoreCase("questionGroup")) {
        parseQuestionGroupID(question);
      } else if (keyword.equalsIgnoreCase("numberOfTries")) {
        parseNumberOfTries(question);
      } else if (keyword.equalsIgnoreCase("answer")) {
        parseAnswer(question);
      }
      // get the next keyword
      keyword = parser.getWord();
    }

    return question;
  }

}
