import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

/**
 * @author Patrick Fongue, Matthias Prager, Daniel Staesche
 * @version 0.1 2008-04-30
 * 
 */
public class WarshallP {

	/**
	 * The concrete language object used for creating output
	 */
	private Language	lang;
	Text							param0;
	Text							param11;
	Text							param22;
	Text							param33;
	Text							param44;
	Text							param55;

	/**
	 * Default constructor
	 * 
	 * @param l
	 *          the conrete language object used for creating output
	 */
	public WarshallP(Language l) {
		// Store the language object
		lang = l;
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		lang.setStepMode(true);
	}

	private static final String	DESCRIPTION	= "Warshall";

	private static final String	SOURCE_CODE	= "Warshall";

	public void calcReachability(int[][] w) {
		MatrixProperties adjMatrixProperties = new MatrixProperties();
		adjMatrixProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		adjMatrixProperties.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.RED);
		adjMatrixProperties.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.BLACK);

		Coordinates upperLeftIntMatrix = new Coordinates(10, 10);

		IntMatrix adjMatrix = lang.newIntMatrix(upperLeftIntMatrix, w,
				"Erreichbarkeitsmatrix", null, adjMatrixProperties);
		lang.nextStep();
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
				Font.PLAIN, 12));

		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		// now, create the source code entity
		SourceCode sc = lang.newSourceCode(new Coordinates(40, 140), "sourceCode",
				null, scProps);

		sc.addCodeLine("public void warshall(int[][]a) {", null, 0, null);
		sc.addCodeLine("for(int k=0; k<a.length; k++) {", null, 1, null);
		sc.addCodeLine("for(int i= 0; i<a.length; i++) {", null, 2, null);
		sc.addCodeLine("if(a[i][k]==1){", null, 3, null);
		sc.addCodeLine("for(int j=0; j<a.length; j++){", null, 4, null);
		sc.addCodeLine("if(a[k][j]==1) {", null, 5, null);
		sc.addCodeLine("a[i][j]=1 }", null, 5, null);
		sc.addCodeLine(" }", null, 4, null);
		sc.addCodeLine("}", null, 3, null);
		sc.addCodeLine(" }", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine(" }", null, 0, null);

		lang.nextStep();
		lang.newText(new Coordinates(350, 200), "k = ", "textk", null);
		param0 = lang.newText(new Coordinates(380, 200), "", "text0", null);
		lang.newText(new Coordinates(350, 225), "j = ", "textj", null);
		param11 = lang.newText(new Coordinates(380, 200), "", "text11", null);
		lang.newText(new Coordinates(350, 250), "i = ", "texti", null);
		param22 = lang.newText(new Coordinates(380, 200), "", "text22", null);
		lang.newText(new Coordinates(500, 200), "a[i][k] = ", "textik", null);
		param33 = lang.newText(new Coordinates(545, 200), "", "text33", null);
		lang.newText(new Coordinates(500, 225), "a[k][j] = ", "textkj", null);
		param44 = lang.newText(new Coordinates(545, 200), "", "text44", null);
		lang.newText(new Coordinates(500, 250), "a[i][j] = ", "textij", null);
		param55 = lang.newText(new Coordinates(545, 200), "", "text55", null);

		// adjMatrix.highlightElemColumnRange(0, 0, 3, null, null);
		// adjMatrix.highlightElemRowRange(0, 0, 3, null, null);
		lang.nextStep();
		warshall(adjMatrix, sc);
	}

	/**
	 * counter for the number of pointers
	 * 
	 */
//	private int	pointerCounter	= 0;

	/**
	 * Warshall: Calculate the transitive closure of a graph
	 * 
	 */
	private void warshall(IntMatrix w, SourceCode source) {
		// Highlight first line
		// Line, Column, use context colour?, display options, duration
		source.highlight(0, 0, false);
		lang.nextStep();

		// Highlight next line
		source.toggleHighlight(0, 0, false, 1, 0);

		int numberOfNodes = w.getNrCols();
		for (int transitnode = 0; transitnode < numberOfNodes; transitnode++) {
			param0.setText(Integer.toString(transitnode), null, null);
			source.highlight(2);
			lang.nextStep();
			for (int sourcenode = 0; sourcenode < numberOfNodes; sourcenode++) {
				param11.setText(Integer.toString(sourcenode), null, null);
				source.toggleHighlight(2, 3);
				param33
						.setText(Integer.toString(w.getElement(sourcenode, transitnode)),
								null, null);
				w.highlightElem(sourcenode, transitnode, null, null);
				source.highlight(4);
				lang.nextStep();
				if (w.getElement(sourcenode, transitnode) == 1) {
					source.unhighlight(3);
					source.unhighlight(4);
					w.highlightElemRowRange(transitnode, transitnode, 3, null, null);
					source.highlight(5);
					for (int targetnode = 0; targetnode < numberOfNodes; targetnode++) {
						param22.setText(Integer.toString(targetnode), null, null);
						param44.setText(Integer.toString(w.getElement(transitnode,
								targetnode)), null, null);
						w.highlightElem(transitnode, targetnode, null, null);
						lang.nextStep();
						if (w.getElement(transitnode, targetnode) == 1) {
							param55.setText(Integer.toString(w.getElement(sourcenode,
									targetnode)), null, null);
							w.put(sourcenode, targetnode, 1, null, null);

							lang.nextStep();
						}
					}
					source.unhighlight(5);
				}
			}
		}
	}

	protected String getAlgorithmDescription() {
		return DESCRIPTION;
	}

	protected String getAlgorithmCode() {
		return SOURCE_CODE;
	}

	public String getName() {
		return "Warshall";
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getCodeExample() {
		return SOURCE_CODE;
	}

	public static void main(String[] args) {
		// Create a new animation
		// name, author, screen width, screen height
		Language l = new AnimalScript("Warshall Animation",
				"Patrick Fongue, Matthias Prager, Daniel Staesche", 640, 480);
		WarshallP s = new WarshallP(l);
		int[][] w = { { 0, 0, 1, 1 }, { 1, 0, 0, 0 }, { 0, 0, 0, 1 },
				{ 0, 0, 0, 0 } };
		s.calcReachability(w);
		System.out.println(l);
	}
}