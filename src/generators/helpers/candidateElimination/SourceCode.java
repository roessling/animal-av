package generators.helpers.candidateElimination;

import java.awt.Color;
import java.awt.Font;

import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Offset;

/**
 * @author mateusz
 */
public class SourceCode {

    algoanim.primitives.SourceCode sc;

    public SourceCode(Language lang) {

	SourceCodeProperties scProps = new SourceCodeProperties();
	scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
	scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		"Monospaced", Font.BOLD, 12));
	scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);

	sc = lang.newSourceCode(new Offset(200, -10, "description", "NE"),
		"sourceCode", null, scProps);

	sc.addCodeLine("G = set of maximally general " + "hypotheses", null, 0,
		null); // 0
	sc.addCodeLine("S = set of maximally specific hypotheses", null, 0,
		null); // 1
	sc.addCodeLine("For each training example e", null, 0, null); // 2
	sc.addCodeLine("if e is positive", null, 1, null); // 3
	sc.addCodeLine("For each hypothesis g in G that does not cover e",
		null, 2, null); // 4
	sc.addCodeLine("remove g from G", null, 3, null); // 5
	sc.addCodeLine("For each hypothesis s in S that does not cover e",
		null, 2, null); // 6
	sc.addCodeLine("remove s from S", null, 3, null); // 7
	sc.addCodeLine("S = S united with all hypotheses h such that", null, 3,
		null); // 8
	sc.addCodeLine("h is a minimal generalization of s", null, 4, null); // 5
	sc.addCodeLine("h covers e", null, 4, null); // 6
	sc.addCodeLine("some hypothesis in G is more general than h", null, 4,
		null); // 7
	sc.addCodeLine("remove from S any hypothesis that is more general ",
		null, 3, null);
	sc.addCodeLine("than another hypothesis in S", null, 5, null);
	sc.addCodeLine("if e is negative", null, 1, null); // 3
	sc.addCodeLine("For each hypothesis s in S that does cover e", null, 2,
		null); // 4
	sc.addCodeLine("remove s from S", null, 3, null); // 5
	sc.addCodeLine("For each hypothesis g in G that does cover e", null, 2,
		null); // 6
	sc.addCodeLine("remove g from G", null, 3, null); // 7
	sc.addCodeLine("G = G united with all hypotheses h such that", null, 3,
		null); // 8
	sc.addCodeLine("h is a minimal specialization of g", null, 4, null); // 5
	sc.addCodeLine("h covers e", null, 4, null); // 6
	sc.addCodeLine("some hypothesis in S is more special than h", null, 4,
		null); // 7
	sc.addCodeLine("remove from G any hypothesis that is more special ",
		null, 3, null);
	sc.addCodeLine("than another hypothesis in G", null, 5, null);
    }

    public void hide() {
	sc.hide();
    }

    public void show() {
	sc.show();
    }

    public void toggleHighlight(int oldLine, int newLine) {
	sc.toggleHighlight(oldLine, newLine);
    }

    public void highlight(int i) {
	sc.highlight(i);
    }

    public void unhighlight(int i) {
	sc.unhighlight(i);
    }
}