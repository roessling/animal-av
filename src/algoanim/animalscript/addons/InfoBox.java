package algoanim.animalscript.addons;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.UUID;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * Displays a borderless box with a headline and multiple lines of text.
 *
 */
public class InfoBox extends MultiPrimitiveAnim {
	// the headline
	private Text hl;
	
	// the variable value
	private Object[] myVariables = null;
	
	/**
	 * Create a new InfoBox to display.
	 * 
	 * @param lang The Language object to add the box to
	 * @param position The position of the box within the animation
	 * @param size The number of lines of normal text the box can display 
	 * @param headline The content of the headline
	 */
	public InfoBox(Language lang, Node position, int size, String headline) {
	  this(lang, position, size, headline, (Object[])null);
//		super(lang);
//
//		TextProperties boxHeadProps = new TextProperties();
//		boxHeadProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
//		boxHeadProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
//
//		TextProperties boxProps = new TextProperties();
//		boxProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
//		boxProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 12));
//		hl = l.newText(position, headline, UUID.randomUUID().toString(), null, boxHeadProps);
//		
//		Text t = l.newText(new Offset(4, 0, hl, AnimalScript.DIRECTION_SW), "", UUID.randomUUID().toString(), null, boxProps);
//		p.add(t);
//		for (int i = 1; i < size; i++) {
//			t = l.newText(new Offset(0, 0, t, AnimalScript.DIRECTION_SW), "", UUID.randomUUID().toString(), null, boxProps);
//			p.add(t);
//		}
	}
	
	 /**
   * Create a new InfoBox to display.
   * 
   * @param lang The Language object to add the box to
   * @param position The position of the box within the animation
   * @param size The number of lines of normal text the box can display 
   * @param headline The content of the headline
   * @param variables values for the quoted ({0}, {1}, ...) variable place holders
   */
  public InfoBox(Language lang, Node position, int size, String headline, Object... variables) {
    super(lang);
    
    myVariables = variables;
    TextProperties boxHeadProps = new TextProperties();
    boxHeadProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    boxHeadProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));

    TextProperties boxProps = new TextProperties();
    boxProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
    boxProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 12));

    String headLineText = headline;
    if (variables != null && variables.length > 0)
      headLineText = replaceVars(headline);

    hl = l.newText(position, headLineText, UUID.randomUUID().toString(), null, boxHeadProps);
    
    Text t = l.newText(new Offset(4, 0, hl, AnimalScript.DIRECTION_SW), "", UUID.randomUUID().toString(), null, boxProps);
    p.add(t);
    for (int i = 1; i < size; i++) {
      t = l.newText(new Offset(0, 0, t, AnimalScript.DIRECTION_SW), "", UUID.randomUUID().toString(), null, boxProps);
      p.add(t);
    }
  }
	
	/**
	 * Change the headline
	 * 
	 * @param text The new headline
	 */
	public void setHeadline(String text) {
		hl.setText(text, null, null);
	}
	
	/**
	 * Change the text
	 * 
	 * @param text The new text elements.
	 */
	public void setText(List<String> text) {
		// find the minimum size of the two ArrayLists to iterate
		int size = Math.min(p.size(), text.size());
		for (int i = 0; i < size; i++) {
			Text t = (Text)p.get(i);
			t.setText(replaceVars(text.get(i)), null, null);
			p.set(i, t);
		}
		
		// empty all remaining list entries
		for (int i = size; i < p.size(); i++) {
			Text t = (Text)p.get(i);
			t.setText("", null, null);
			p.set(i, t);			
		}
	}
	
	 /**
   * Replace any variables with their content. Variables are created by using {}
   * with a index number between the brackets.
   * 
   * @param text
   *          Text to be replaced
   * @param vars
   *          An array with the contents of the variables
   * @return A String where the variables have been replaced with their
   *         contents.
   */
  private String replaceVars(String text) {
    if (myVariables == null)
      return text;
    String result = new String(text);
    for (int i = 0; i < myVariables.length; i++) {
      result = result.replace("{" + i + "}", myVariables[i].toString());
    }

    return result;
  }

	
	@Override
	public void show() {
		hl.show();
		super.show();
	}
	
	@Override
	public void hide() {
		hl.hide();
		super.hide();
	}
}
