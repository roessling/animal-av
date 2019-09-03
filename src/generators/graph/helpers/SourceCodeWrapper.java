package generators.graph.helpers;

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

  private SourceCode   sourceCode;
  private Set<Integer> markedLines;

  /**
   * Creates a new source code wrapper
   * 
   * @param position
   *          position of the source code
   * @param lang
   *          the language to draw to
   * @param sourceLines
   *          lines of the source code
   * @param scProps
   *          the properties for the source code
   */
  public SourceCodeWrapper(Coordinates position, Language lang,
      List<String> sourceLines, SourceCodeProperties scProps) {

    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);

    this.sourceCode = lang.newSourceCode(position, "sourceCode", null, scProps);
    for (String currentLine : sourceLines)
      this.sourceCode.addCodeLine(currentLine, null, 0, null);

    this.markedLines = new HashSet<Integer>();
  }

  /**
   * Marks the given line numbers in the first sourcecode.
   * 
   * @param lineNumber
   *          the lines to mark
   */
  public void markSourceCodeLines(Integer... lineNumber) {
    for (Integer currentLine : markedLines)
      sourceCode.unhighlight(currentLine);
    markedLines.clear();
    for (Integer currentLine : lineNumber) {
      sourceCode.highlight(currentLine);
      markedLines.add(currentLine);
    }
  }

  /**
   * Hides the sourcecodes.
   */
  public void hide() {
    sourceCode.hide();
  }
}
