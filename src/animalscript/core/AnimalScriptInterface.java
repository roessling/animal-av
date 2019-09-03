package animalscript.core;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.Enumeration;

import animal.misc.XProperties;

/**
 * This interface must be implemented by <em>all</em> AnimalScript parsers!
 * It contains functions for processing the commands, retrieving the keywords
 * locally supported, and a function deciding whether a new step must be inserted
 * for a given operation.
 *
 * @author Guido R&ouml;&szlig;ling 
 * (EMail: <a href="mailto:roessling@acm.org">roessling@acm.org</a>)
 */
public interface AnimalScriptInterface
{
  // =================================================================
  //                             CONSTANTS
  // =================================================================

  // ========================= Element types =========================


  /**
   * Type information for 'animator' object definitions
   */
  public static final int ANIMATOR = -1;


  /**
   * Type information for 'link' object definitions
   */
  public static final int LINK = -2;


  /**
   * Type information for 'graphic object' object definitions
   */
  public static final int OBJECT = -4;


  /**
   * Determine if a new step is needed, depending on the command passed.
   * Keep in mind that we might be in a grouped step using the {...} form.
   * Usually, every command not inside such a grouped step is contained in a
   * new step.  However, this is not the case for operations without visible
   * effect - mostly maintenance or declaration entries.
   *
   * @param currentCommand the command used for the decision.
   * @return true if a new step must be generated
   */
  public boolean generateNewStep(String currentCommand);


  /**
   * Retrieve the keywords handled by this parser
   *
   * @return an enumeration of keywords that this parser handles.
   */
  public Enumeration<String> getHandledKeywords();


  /**
   * Retrieve the keyword properties handled by this parser
   *
   * @return a XProperties of keywords that this parser handles.
   */
//  public Hashtable getHandledKeywordsProps();


  /**
   * Retrieve the rules handled by this parser
   *
   * @return an Enumeration containing the keywords that this parser handles.
   */
  public Enumeration<Object> getHandledRules();

  
  /**
   * Retrieve the extensions required by this parser.
   * This does <em>not</em> include the <code>core</code> packages.
   *
   * @return a String array of keywords that this parser handles.
   */
  public String[] getRequiredExtensions();


  /**
   * Retrieve the rule for a given keyword handled by this parser
   *
   * @return a String containing the requested rule, if exists
   */
  public String getRuleFor(String key);


  /**
   * Process the current command and perform appropriate actions.
   * Depending on the keyword found, usually some objects are inserted into
   * the graphic objects vector, the animator vector, or both.
   *
   * @param stok the StreamTokenizer used for parsing.
   * @param currentCommand the parsed command
   * @throws IOException if any parsing error occurred.
   */
  public XProperties process(StreamTokenizer stok, String currentCommand) 
    throws IOException;
}
