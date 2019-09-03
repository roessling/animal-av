package algoanim.primitives.generators;

import algoanim.primitives.SourceCode;
import algoanim.util.Timing;

/**
 * <code>SourceCodeGenerator</code> offers methods to request the 
 * included Language object to
 * append sourcecode related script code lines to the output.
 * It is designed to be included by a <code>SourceCode</code> primitive, 
 * which just redirects action calls to the generator.
 * Each script language offering a <code>SourceCode</code> primitive has 
 * to implement its own
 * <code>SourceCodeGenerator</code>, which is then responsible to 
 * create proper script code.  
 * 
 * @author Stephan Mehlhase 
 */
public interface SourceCodeGenerator extends GeneratorInterface {
	/**
	 * Creates the originating script code for a given 
	 * <code>SourceCode</code>, due to the fact that before a primitive 
	 * can be worked with it has to be defined and made known to the script 
	 * language.
	 * 
	 * @param sc the <code>SourceCode</code> for which the initiate 
	 * script code shall be created. 
	 */
	public void create(SourceCode sc);
	
	/**
	 * Adds a new code line to the <code>SourceCode</code>.
	 * 
	 * @param code the <code>SourceCode</code> which the line shall 
	 * belong to.
	 * @param codeline the actual code.
	 * @param name a distinct name for the line.
	 * @param indentation the indentation to apply to this line.
	 * @param t the delay after which this operation shall be performed.
	 */
	public void addCodeLine(SourceCode code, String codeline, 
							String name, int indentation, Timing t);
	
	/**
	 * Adds a new code element to the <code>SourceCode</code>.
	 * 
	 * @param code the <code>SourceCode</code> which the element 
	 * shall belong to.
	 * @param codeline the actual code.
	 * @param name a distinct name for the element.
	 * @param indentation the indentation to apply to this line.
	 * @param row specifies which entry of the current line this element 
	 * should be.
	 * @param t the delay after which this operation shall be performed.
	 */
	public void addCodeElement(SourceCode code, String codeline, 
								String name, int indentation, int row, Timing t);	
	
	 /**
   * Adds a new code element to the <code>SourceCode</code>.
   * 
   * @param code the <code>SourceCode</code> which the element 
   * shall belong to.
   * @param codeline the actual code.
   * @param name a distinct name for the element.
   * @param indentation the indentation to apply to this line.
   * @param row specifies which entry of the current line this element 
   * should be.
   * @param t the delay after which this operation shall be performed.
   */
  public void addCodeElement(SourceCode code, String codeline, 
                String name, int indentation, boolean noSpace, int row, Timing t); 

	/**
	 * Highlights a line in a certain <code>SourceCode</code> element.
	 * 
	 * @param code the <code>SourceCode</code> which the line 
	 * belongs to.
	 * @param line the line to highlight.
	 * @param row the code element to highlight.
	 * @param context use the code context colour instead of the
	 * code highlight colour.
	 * @param delay the delay to apply to this operation.
	 * @param duration the duration of the action.
	 */
	public void highlight(SourceCode code, int line, int row, 
			boolean context, Timing delay, Timing duration);
	
//	/**
//	 * Highlights a line in a certain <code>SourceCode</code> element.
//	 * 
//	 * @param code the <code>SourceCode</code> which the line 
//	 * belongs to.
//	 * @param line the name of the line to highlight.
//	 * @param row the code element to highlight.
//	 * @param context use the code context colour instead of the
//	 * code highlight colour.
//	 * @param delay the delay to apply to this operation.
//	 * @param duration the duration of the action.
//	 */
//	public void highlight(SourceCode code, String lineName, int row, 
//			boolean context, Timing delay, Timing duration);

	
	/**
	 * Unhighlights a line in a certain <code>SourceCode</code> element.
	 * 
	 * @param code the <code>SourceCode</code> which the line 
	 * belongs to.
	 * @param line the line to unhighlight.
	 * @param row the code element to unhighlight.
	 * @param context use the code context colour instead of the
	 * code highlight colour.
	 * @param delay the delay to apply to this operation.
	 * @param duration the duration of the action.
	 */
	public void unhighlight(SourceCode code, int line, int row, 
			boolean context, Timing delay, Timing duration);
	
//	/**
//	 * Unhighlights a line in a certain <code>SourceCode</code> element.
//	 * 
//	 * @param code the <code>SourceCode</code> which the line 
//	 * belongs to.
//	 * @param line the name of the line to unhighlight.
//	 * @param row the code element to unhighlight.
//	 * @param context use the code context colour instead of the
//	 * code highlight colour.
//	 * @param delay the delay to apply to this operation.
//	 * @param duration the duration of the action.
//	 */
//	public void unhighlight(SourceCode code, String lineName, int row, 
//			boolean context, Timing delay, Timing duration);

	
	/**
	 * Hides the given <code>SourceCode</code> element.
	 * 
	 * @param code the <code>SourceCode</code> to hide.
	 * @param delay the delay to apply to this operation.
	 */
	public void hide(SourceCode code, Timing delay);
}
