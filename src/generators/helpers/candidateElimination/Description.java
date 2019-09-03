package generators.helpers.candidateElimination;

import java.awt.Font;

import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Offset;

/**
 * @author mateusz
 */
public class Description {

    algoanim.primitives.SourceCode algoDesc;

    public Description(Language lang) {
	SourceCodeProperties algoDescProps = new SourceCodeProperties(
		"algo_description");
	algoDescProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		"SansSerif", Font.BOLD, 12));

	algoDesc = lang.newSourceCode(new Offset(0, 20, "title", "SW"),
		"description", null, algoDescProps);

	algoDesc.addCodeLine(
		"Der Candidate Elimination Algorithmus sucht mit Hilfe ", null,
		0, null);
	algoDesc.addCodeLine(
		"einer Reihe von Trainingsbeispielen die Grenzen ", null, 0,
		null);
	algoDesc.addCodeLine("des sogenannten Version Space. Dieser bildet ",
		null, 0, null);
	algoDesc.addCodeLine(
		"den Bereich aller Regeln ab, die die Trainingsbeispiele ",
		null, 0, null);
	algoDesc.addCodeLine(
		"korrekt mit Hilfe einer einzigen Regel klassifizieren ", null,
		0, null);
	algoDesc.addCodeLine(
		"koennen. Eine Regel ausserhalb des Version Space ", null, 0,
		null);
	algoDesc.addCodeLine(
		"sagt also fuer einen Teil der Trainingsbeispiele ", null, 0,
		null);
	algoDesc.addCodeLine("nicht die richtige Klasse vorraus. Sind untere ",
		null, 0, null);
	algoDesc.addCodeLine("und obere Schranke gleich, so existiert genau ",
		null, 0, null);
	algoDesc.addCodeLine(
		"eine Regel, die die Bespiele korrekt klassifiziert. ", null,
		0, null);
	algoDesc.addCodeLine("Sind beide Regelmengen leer, so gibt es keine ",
		null, 0, null);
	algoDesc.addCodeLine("einzelne Regel die alle Trainingsbeispiele ",
		null, 0, null);
	algoDesc.addCodeLine("korrekt klassifiert. Die untere Schranke des ",
		null, 0, null);
	algoDesc.addCodeLine(
		"Version Space, genannt S-Set, wird des weiteren ", null, 0,
		null);
	algoDesc.addCodeLine("als 'S' angegeben, die obere Schranke, genannt ",
		null, 0, null);
	algoDesc.addCodeLine("G-Set, als 'G'.", null, 0, null);
    }

    public void hide() {
	algoDesc.hide();
    }

}
