
package generators.maths.resolutionCalculus;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.List;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.graphics.PTStringArray;

public class AnimationDecoratedClause implements IClause {

	protected final Clause clause;
	protected final StringArray array;

	protected final Rectangle dimensions;
	
	/**
	 * Has to exist once {@link AnimationDecoratedClause#setPosition(Node)} has been called once (see, the corresponding forum thread:
	 * https://moodle.informatik.tu-darmstadt.de/mod/moodleoverflow/discussion.php?d=12
	 */
	protected Node currentUpperLeft;

	public AnimationDecoratedClause(Clause clause, Language lang, Node coords) {
		this.clause = clause;

		ArrayProperties ap = new ArrayProperties();
		ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		ap.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		ap.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
		String[] representation = clause.toRepresentation();
		array = lang.newStringArray(coords, representation, "null", null, ap);
		array.showIndices(false, null, null);

		// Hacky, but at least works, for the dimensions I have to jump down to the
		// actual render code
		PTStringArray dimensionGrabber = new PTStringArray(representation);
		dimensions = dimensionGrabber.getBoundingBox();
		currentUpperLeft = coords;
	}

	public void setPosition(Node coords) {
		array.moveTo(AnimalScript.DIRECTION_N, null, coords, null, Timing.MEDIUM);
		currentUpperLeft = coords;
	}

	public Node getPosition() {
		return currentUpperLeft;
	}

	public void highlight(Color c) {
		// Workaround for missing functionality (Can only set for one cell even though
		// the script allows omitting it)
		ResolutionGeneratorMain.lang.addLine(String.format("setFillColor on \"%s\" with color (%d,%d,%d)", array.getName(), c.getRed(),
				c.getGreen(), c.getBlue()));
	}

	public void highlightCell(Color c, String literal) {
		for (int i = 0; i < array.getLength(); i++) {
			if (array.getData(i).equals(literal)) {
				array.setFillColor(i, c, null, null);
				break;
			}
		}
	}

	public void dehighlight() {
		// Same workaround
		ResolutionGeneratorMain.lang.addLine(String.format("setFillColor on \"%s\" with color (255,255,255)", array.getName()));
	}

	public void destroy() {
		array.hide();
	}

	public StringArray getPrimitive() {
		return array;
	}

	public int getWidth() {
		return dimensions.width;
	}

	public int getHeight() {
		return dimensions.height;
	}

	@Override
	public void addLiteral(String literal) {
		clause.addLiteral(literal);
	}

	@Override
	public boolean isUnsatisfiable() {
		return clause.isUnsatisfiable();
	}

	@Override
	public List<Clause> resolveWith(Clause other, IResolveListener listener) {
		return clause.resolveWith(other, listener);
	}

	@Override
	public Clause getEmbeddedClause() {
		return clause.getEmbeddedClause();
	}

	@Override
	public int literalCount() {
		return clause.literalCount();
	}

	@Override
	public String[] toRepresentation() {
		return clause.toRepresentation();
	}

	@Override
	public String toString() {
		return clause.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof AnimationDecoratedClause) {
			return clause.equals(((AnimationDecoratedClause) obj).clause);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return clause.hashCode();
	}

}
