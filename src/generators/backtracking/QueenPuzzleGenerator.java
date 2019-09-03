package generators.backtracking;

import generators.backtracking.helpers.QueensPuzzle;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;

/**
 * Generator for the Queen Puzzle.
 */
public class QueenPuzzleGenerator implements Generator {

	public static final String animationAuthor = "Roman Uhlig, Michael Rau";

	// the AnimalScript language
	private Language lang;

	private int Anzahl_der_Damen = 4;

	// various colors
	private Color Farbe_Damen = Color.black;
	private Color Pseudocode_Hervorhebungsfarbe = Color.black;
	private Color Head_Textfarbe = Color.black;
	private Color Pseudocode_Hintergrundfarbe = Color.black;
	private Color Farbe_schlagende_Dame = Color.black;
	private Color Farbe_geschlagene_Dame = Color.black;
	private Color Kastenfarbe_2 = Color.black;
	private Color Kastenfarbe_1 = Color.black;
	private Color Pseudocode_Textfarbe = Color.black;

	/**
	 * Sets the AnimalScript language.
	 */
	public void init() {
		lang = new AnimalScript("Damenproblem", animationAuthor, 800, 600);
	}

	/**
	 * Generates the animation.
	 * 
	 * @param props
	 *            the AnimationProperties
	 * @param primitives
	 *            the primitive settings
	 */
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		Anzahl_der_Damen = (Integer) primitives.get("Anzahl_der_Damen");
		Farbe_Damen = (Color) primitives.get("Farbe_Damen");
		Pseudocode_Hervorhebungsfarbe = (Color) primitives
				.get("Pseudocode_Hervorhebungsfarbe");
		Head_Textfarbe = (Color) primitives.get("Head_Textfarbe");
		Pseudocode_Hintergrundfarbe = (Color) primitives
				.get("Pseudocode_Hintergrundfarbe");
		Farbe_schlagende_Dame = (Color) primitives.get("Farbe_schlagende_Dame");
		Farbe_geschlagene_Dame = (Color) primitives
				.get("Farbe_geschlagene_Dame");
		Kastenfarbe_2 = (Color) primitives.get("Kastenfarbe_2");
		Kastenfarbe_1 = (Color) primitives.get("Kastenfarbe_1");
		Pseudocode_Textfarbe = (Color) primitives.get("Pseudocode_Textfarbe");

		new QueensPuzzle(Anzahl_der_Damen, lang, Farbe_Damen, Kastenfarbe_1,
				Kastenfarbe_2, Farbe_geschlagene_Dame, Farbe_schlagende_Dame,
				Pseudocode_Hervorhebungsfarbe, Pseudocode_Hintergrundfarbe,
				Pseudocode_Textfarbe, Head_Textfarbe);

		lang.finalizeGeneration();
		
		// remove every occurrence of the "refresh" command from the script
		// because of major layouting problems with refresh
		String finalStringWithoutRefresh = lang.toString().replaceAll(
				"refresh", "");
		
		return finalStringWithoutRefresh;
	}

	public String getName() {
		return "Damenproblem";
	}

	public String getAlgorithmName() {
		return "Damenproblem";
	}

	public String getAnimationAuthor() {
		return animationAuthor;
	}

	public String getDescription() {
		return "Das Damenproblem ist eine schachmathematische Aufgabe. Es sollen jeweils n Damen auf einem Schachbrett so aufgestellt werden, dass keine zwei Damen einander nach den Schachregeln schlagen können. Die Figurenfarbe wird dabei ignoriert, und es wird angenommen, dass jede Figur jede andere angreifen könnte. Oder anders ausgedrückt: Es sollen sich keine zwei Damen die gleiche Reihe, Linie oder Diagonale teilen. Im Mittelpunkt steht die Frage nach der Anzahl der möglichen Lösungen.<br><br>  Im Folgenden wird diese Aufgabe mittels Backtracking gelöst. Dabei werden stets so viele Damen wie möglich Zeile für Zeile gesetzt. Lässt sich für eine Dame keine mögliche Position mehr finden, so springt der Algorithmus Zeile um Zeile zurück, um die bisher platzierten Damen auf eine womöglich bessere Ausgangsposition zu verschieben.";
	}

	public String getCodeExample() {
		return "Prozedur L&ouml;seDamenproblem() gibt Wahrheitswert zur&uuml;ck\n    Falls Damenproblem(1) ist Wahr\n            gib Wahr zur&uuml;ck\n        Sonst\n            gib Falsch zur&uuml;ck\n\nProzedur Damenproblem (Zeile) gibt Wahrheitswert zur&uuml;ck\n    Falls Zeile ungerade\n            setze neue Dame auf linkestes Feld von Zeile\n        Sonst\n            setze neue Dame auf rechtestes Feld von Zeile\n\n    Solange Dame innerhalb des Spielfeldes\n        Falls Dame ist nicht bedroht\n            Falls Zeile ist letzte Zeile\n                    gib Wahr zur&uuml;ck\n                Sonst Falls Damenproblem(Zeile+1) ist Wahr\n                    gib Wahr zur&uuml;ck\n        setze Dame eine Spalte weiter\n\n    entferne Dame von Spielfeld\n    gib Falsch zur&uuml;ck";
	}

	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_BACKTRACKING);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

}
