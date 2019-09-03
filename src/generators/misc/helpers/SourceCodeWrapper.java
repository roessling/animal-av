package generators.misc.helpers;
import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

/**
 * Encapsulates the functionality for sourcecode-like outputs in animal.
 * 
 * @author chollubetz
 * 
 */
public class SourceCodeWrapper {

	private SourceCode sourceCode1, sourceCode2;
	private Set<Integer> markedLines1, markedLines2;

	/**
	 * Creates a new source code wrapper
	 * 
	 * @param position1 position of the first source code
	 * @param position2 position of the second source code
	 * @param lang the language to draw to
	 * @param sourceLines1 lines of the first source code
	 * @param sourceLines2 lines of the second source code
	 */
	public SourceCodeWrapper(Coordinates position1, Coordinates position2,
			Language lang, List<String> sourceLines1, List<String> sourceLines2, SourceCodeProperties scProps) {

		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);

		this.sourceCode1 = lang.newSourceCode(position1, "sourceCode1", null,
				scProps);
		for (String currentLine : sourceLines1)
			this.sourceCode1.addCodeLine(currentLine, null, 0, null);

		this.sourceCode2 = lang.newSourceCode(position2, "sourceCode2", null,
				scProps);
		for (String currentLine : sourceLines2)
			this.sourceCode2.addCodeLine(currentLine, null, 0, null);

		this.markedLines1 = new HashSet<Integer>();
		this.markedLines2 = new HashSet<Integer>();
	}

	/**
	 * Marks the given line numbers in the first sourcecode.
	 * 
	 * @param lineNumber
	 *            the lines to mark
	 */
	public void markSourceCode1Lines(Integer... lineNumber) {
		for (Integer currentLine : markedLines1)
			sourceCode1.unhighlight(currentLine);
		markedLines1.clear();
		for (Integer currentLine : lineNumber) {
			sourceCode1.highlight(currentLine);
			markedLines1.add(currentLine);
		}
	}

	/**
	 * Marks the given line numbers in the second sourcecode.
	 * 
	 * @param lineNumber
	 *            the lines to mark
	 */
	public void markSourceCode2Lines(Integer... lineNumber) {
		for (Integer currentLine : markedLines2)
			sourceCode2.unhighlight(currentLine);
		markedLines2.clear();
		for (Integer currentLine : lineNumber) {
			sourceCode2.highlight(currentLine);
			markedLines2.add(currentLine);
		}
	}

	/**
	 * Hides the sourcecodes.
	 */
	public void hide() {
		sourceCode1.hide();
		sourceCode2.hide();
	}
}