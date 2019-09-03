package algoanim.animalscript.addons.bbcode;

import java.util.regex.Pattern;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 * Create a multiline BBCode element.
 */
public abstract class MultilineBBCode extends BBCode {
	
	protected String[] brSplit(String input) {
		return Pattern.compile("\\[br\\]", Pattern.CASE_INSENSITIVE).split(input);
	}
}