package animal.misc;

public interface InteractionInterface
{
	/**
	 * the constant value for "fill in the blank" question types
	 */
  public static final int FILL_IN_BLANKS = 0;
  
  
  /**
   * the constant value for "multiple choice " question types
   */
  public static final int MULTIPLE_CHOICE = 1;

  
  /**
   * the constant value for "true / false" question types
   */
  public static final int TRUE_FALSE = 2;

  
  /**
   * the constant value for documentation links
   */
  public static final int DOCUMENTATION = 4;

  
  /**
   * retrieves the handler for a given interaction
   * 
   * @param interactionType the type of interaction. Can be FILL_IN_BLANKS,
   * MULTIPLE_CHOICE, TRUE_FALSE, or DOCUMENTATION.
   * @param questionID the ID of the actual question
   * @return the handler for this question
   */
  QuestionInterface getHandlerFor(int interactionType, String questionID);
  
  
  /**
   * initializes the question handler
   * 
   * @param question the question handler obect
   * @param isInitialized if true, perform the question!
   * @param initText the text for the initialization
   */
  void initialize(QuestionInterface question, boolean isInitialized,
                  String initText);
  
  
  /**
   * performs (executes) the given interaction
   * 
   * @param question the handler object
   */
  void performQuestionOperation(QuestionInterface question);
  
  
  /**
   * returns the web root for handling elements and especially documentation
   * 
   * @return the Web root as a String object 
   */
  String getWebRoot();
}
