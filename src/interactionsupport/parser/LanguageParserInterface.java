package interactionsupport.parser;

import interactionsupport.models.InteractionModel;
import interactionsupport.models.QuestionGroupModel;

import java.util.Hashtable;

/**
 * Interface of parser usable with AVInteraction.
 */
public interface LanguageParserInterface {

  /**
   * Retrieve the information about questionGroups defined in the
   * definitionFile.
   * 
   * @return A Hash containg the groups-information.
   */
  public Hashtable<String, QuestionGroupModel> getQuestionGroups();

  /**
   * Parse the interactionDefinition and return a Hash containing the
   * interaction objects.
   * 
   * @param filename
   *          The filename of the definitionFile
   * 
   * @return A Hash containing the interaction objects.
   */
  public Hashtable<String, InteractionModel> parse(String filename);

  /**
   * Return a string representation of the object, normally a name for the
   * parser.
   * 
   * @return A string representation of the parser-object.
   */
  public String toString();

}
