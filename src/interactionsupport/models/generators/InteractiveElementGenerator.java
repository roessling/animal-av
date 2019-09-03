package interactionsupport.models.generators;

import interactionsupport.models.InteractionModel;
import algoanim.primitives.generators.GeneratorInterface;

/**
 * <code>InteractiveElementGenerator</code> offers methods to request the
 * included Language object to append interactive elements to the output.
 * 
 * @author Guido R&ouml;&szlig;ling <roessling@acm.org>
 * @version 1.0 2008-08-25
 */
public interface InteractiveElementGenerator extends GeneratorInterface {

  /**
   * creates the actual code for representing an interactive element
   * 
   * @param element
   *          the element to be generated
   */
  public void createInteractiveElementCode(InteractionModel element);

  /**
   * Creates the script code for a given {@link InteractionModel}.
   * 
   * @param interaction
   *          the <em>InteractionModel</em> for which the code is to be
   *          generated
   */
  public void createInteraction(InteractionModel interaction);

  /**
   * finalize the writing of the interaction components
   */
  public void finalizeInteractiveElements();

}
