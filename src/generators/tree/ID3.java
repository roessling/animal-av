/*
 * ID3, 
 * Anja Kirchhöfer, Ben Kohr, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.tree;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.tree.id3.ID3Generator;

public class ID3 {

	private Language lang;

	private int probSpecificQuestion;

	private int exampleNumber;

	private int probRandomQuestion;

	private Color nodeColor;

	private Color yesColor;

	private Color noColor;

	private Color tableHighlightColor;

	private Color tableBorderColor;

	private Color tableElementColor;

	private Font tableFont;

	private Color explanationBoxFillColor;

	private Color explanationBoxColor;

	private Color codeHighlightColor;

	private Color codeColor;

	private int codeSize;

	private boolean codeBold;

	private boolean codeItalic;

	private Font codeFont;

	private boolean language;

	private boolean codeLanguage;

	private String[][] codeTable;

	private boolean informationGainOrGiniIndex;



	public ID3(boolean language, boolean codeLanguage) {
		this.language = language;
		this.codeLanguage = codeLanguage;
	}


	public void init() {
		lang = new AnimalScript("ID3", "Anja Kirchhöfer, Ben Kohr", 800, 600);
	}


	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		if (language) {

			informationGainOrGiniIndex = (Boolean) primitives.get("Information measure");
			exampleNumber = (Integer) primitives.get("Example number");
			probRandomQuestion = (int) primitives.get("Probability for general questions");
			probSpecificQuestion = (int) primitives.get("Probability for specific questions");
			codeTable = (String[][]) primitives.get("Custom input table");

			nodeColor = (Color) (((CircleProperties) props.getPropertiesByName("Style of nodes")).get(AnimationPropertiesKeys.FILL_PROPERTY));

			yesColor = (Color) (((CircleProperties) props.getPropertiesByName("Style of yes-leaves")).get(AnimationPropertiesKeys.FILL_PROPERTY));

			noColor = (Color) (((CircleProperties) props.getPropertiesByName("Style of no-leaves")).get(AnimationPropertiesKeys.FILL_PROPERTY));

			codeHighlightColor = (Color) ((SourceCodeProperties) props.getPropertiesByName("Code style"))
					.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY);
			codeColor = (Color) ((SourceCodeProperties) props.getPropertiesByName("Code style")).get(AnimationPropertiesKeys.COLOR_PROPERTY);
			codeSize = (int) ((SourceCodeProperties) props.getPropertiesByName("Code style")).get(AnimationPropertiesKeys.SIZE_PROPERTY);
			codeBold = (boolean) ((SourceCodeProperties) props.getPropertiesByName("Code style")).get(AnimationPropertiesKeys.BOLD_PROPERTY);
			codeItalic = (boolean) ((SourceCodeProperties) props.getPropertiesByName("Code style")).get(AnimationPropertiesKeys.ITALIC_PROPERTY);
			codeFont = (Font) ((SourceCodeProperties) props.getPropertiesByName("Code style")).get(AnimationPropertiesKeys.FONT_PROPERTY);

			tableHighlightColor = (Color) ((MatrixProperties) props.getPropertiesByName("Example table style"))
					.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY);
			tableBorderColor = (Color) ((MatrixProperties) props.getPropertiesByName("Example table style"))
					.get(AnimationPropertiesKeys.GRID_BORDER_COLOR_PROPERTY);
			tableElementColor = (Color) ((MatrixProperties) props.getPropertiesByName("Example table style"))
					.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY);
			tableFont = (Font) ((MatrixProperties) props.getPropertiesByName("Example table style")).get(AnimationPropertiesKeys.FONT_PROPERTY);

			explanationBoxFillColor = (Color) ((RectProperties) props.getPropertiesByName("Explanation box style"))
					.get(AnimationPropertiesKeys.FILL_PROPERTY);
			explanationBoxColor = (Color) ((RectProperties) props.getPropertiesByName("Explanation box style"))
					.get(AnimationPropertiesKeys.COLOR_PROPERTY);

		} else {

			informationGainOrGiniIndex = (Boolean) primitives.get("Maß für Informationsgehalt");
			exampleNumber = (Integer) primitives.get("Beispielnummer");
			probRandomQuestion = (int) primitives.get("Wahrscheinlichkeit für allgemeine Fragen");
			probSpecificQuestion = (int) primitives.get("Wahrscheinlichkeit für spezifische Fragen");
			codeTable = (String[][]) primitives.get("Eigene Tabelle");

			nodeColor = (Color) (((CircleProperties) props.getPropertiesByName("Stil der Knoten")).get(AnimationPropertiesKeys.FILL_PROPERTY));

			yesColor = (Color) (((CircleProperties) props.getPropertiesByName("Stil der Ja-Blätter")).get(AnimationPropertiesKeys.FILL_PROPERTY));

			noColor = (Color) (((CircleProperties) props.getPropertiesByName("Stil der Nein-Blätter")).get(AnimationPropertiesKeys.FILL_PROPERTY));

			codeHighlightColor = (Color) ((SourceCodeProperties) props.getPropertiesByName("Stil des Codes"))
					.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY);
			codeColor = (Color) ((SourceCodeProperties) props.getPropertiesByName("Stil des Codes")).get(AnimationPropertiesKeys.COLOR_PROPERTY);
			codeSize = (int) ((SourceCodeProperties) props.getPropertiesByName("Stil des Codes")).get(AnimationPropertiesKeys.SIZE_PROPERTY);
			codeBold = (boolean) ((SourceCodeProperties) props.getPropertiesByName("Stil des Codes")).get(AnimationPropertiesKeys.BOLD_PROPERTY);
			codeItalic = (boolean) ((SourceCodeProperties) props.getPropertiesByName("Stil des Codes")).get(AnimationPropertiesKeys.ITALIC_PROPERTY);
			codeFont = (Font) ((SourceCodeProperties) props.getPropertiesByName("Stil des Codes")).get(AnimationPropertiesKeys.FONT_PROPERTY);

			tableHighlightColor = (Color) ((MatrixProperties) props.getPropertiesByName("Tabellenstil"))
					.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY);
			tableBorderColor = (Color) ((MatrixProperties) props.getPropertiesByName("Tabellenstil"))
					.get(AnimationPropertiesKeys.GRID_BORDER_COLOR_PROPERTY);
			tableElementColor = (Color) ((MatrixProperties) props.getPropertiesByName("Tabellenstil"))
					.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY);
			tableFont = (Font) ((MatrixProperties) props.getPropertiesByName("Tabellenstil")).get(AnimationPropertiesKeys.FONT_PROPERTY);

			explanationBoxFillColor = (Color) ((RectProperties) props.getPropertiesByName("Stil der Erklärungsboxen"))
					.get(AnimationPropertiesKeys.FILL_PROPERTY);
			explanationBoxColor = (Color) ((RectProperties) props.getPropertiesByName("Stil der Erklärungsboxen"))
					.get(AnimationPropertiesKeys.COLOR_PROPERTY);

		}


		try {

			ID3Generator id3 = new ID3Generator(informationGainOrGiniIndex, exampleNumber, probRandomQuestion, probSpecificQuestion, codeTable,
					nodeColor, yesColor, noColor, codeHighlightColor, codeColor, codeSize, codeBold, codeItalic, codeFont, tableHighlightColor,
					tableBorderColor, tableElementColor, tableFont, explanationBoxFillColor, explanationBoxColor, language, codeLanguage);

			id3.executeGenerator(lang);

		} catch (Exception e) {
			// TODO
		}

		return lang.toString();
	}


	public String getName() {
		return "ID3";
	}


	public String getAlgorithmName() {
		return "ID3";
	}


	public String getAnimationAuthor() {
		return "Anja Kirchhöfer, Ben Kohr";
	}


	public String getDescription() {

		if (language) {
			return "The ID3 algorithm constructs a decision tree based on a table of observed examples described by a list " + "\n"
					+ "of features and a class label. ID3 uses a function to measure purity of nodes and leaves and aims at " + "\n"
					+ "splitting the class labels of the examples on every tree level as good as possible. The resulting tree" + "\n"
					+ "can be used for class predictions of new examples." + "\n" + "" + "\n"
					+ "Please note: This generated animations are adjusted for a resolution of 1366 x 768 pixels. If the animations" + "\n"
					+ "are very small or too large to be completely displayed on the screen and your PC has a different resolution," + "\n"
					+ "consider to temporarily switch to the above resolution. Or instead, you can adjust the scaling in the" + "\n"
					+ "Animal animation window. If your PC has a Full HD resolution (1920 x 1080), a scaling of 150% should" + "\n"
					+ "yield good results.";
		} else {
			return "Der Algorithmus ID3 konstruiert einen Entscheidungsbaum, basierend auf einer Tabelle von Beispielen," + "\n"
					+ "welche durch eine Liste von Eigenschaften und eine Klasse beschrieben werden. ID3 nutzt eine" + "\n"
					+ "Funktion zum Messen der Reinheit von Knoten und Blättern und zielt darauf ab, die Klassen der Beispiele" + "\n"
					+ "auf jeder Ebene des Baumes möglichst gut zu trennen. Der resultierende Baum kann zur Vorhersage" + "\n"
					+ "von Klassen für neue Beispiele genutzt werden." + "\n" + "" + "\n"
					+ "Anmerkung: Die generierten Animationen sind für eine Auflösung von 1366 x 768 Pixeln zugeschnitten. Falls" + "\n"
					+ "sie sehr klein oder zu groß für die Darstellung auf dem Bildschirm sind und dein PC eine andere" + "\n"
					+ "Auflösung hat, kann es helfen, vorübergehend zu der obigen Auflösung zu wechseln. Stattdessen kannst du" + "\n"
					+ "auch die Skalierung im Animationsfenster von Animal anpassen. Wenn dein PC eine Full-HD-Auflösung" + "\n"
					+ "(1920 x 1080) hat, sollte eine Skalierung von 150% gute Ergebnisse liefern.";
		}
	}


	public String getCodeExample() {

		if (language) {

			if (codeLanguage) {
				return "procedure ID3" + "\n" + "\n" + "    if ( all examples belong to one class c )" + "\n"
						+ "        newLeaf = construct new leaf;" + "\n" + "        label newLeaf with c;" + "\n"
						+ "        return newLeaf as the result tree;" + "\n" + "\n"
						+ "    bestFeature = choose most informative feature with information measure;" + "\n" + "\n"
						+ "    newNode = construct new node;" + "\n" + "    label newNode with the feature's name;" + "\n" + "\n"
						+ "    for ( each value of the bestFeature )" + "\n"
						+ "        call procedure ID3 for all examples with this value for bestFeature;" + "\n" + "\n"
						+ "    newTree = construct a tree by attaching all constructed child trees to newNode;" + "\n" + "    return newTree;" + "\n"
						+ " ";
			} else {
				return "public Tree id3(List<Example> examples) {" + "\n" + "\n" + "    if ( examples.classCount() == 1 )" + "\n"
						+ "        Leaf newLeaf = new Leaf();" + "\n" + "        newLeaf.setLabel( examples.getClassOfAllExamples() );" + "\n"
						+ "        return new Tree( newLeaf );" + "\n" + "\n" + "    String bestFeature = chooseMostInformativeFeature();" + "\n"
						+ "\n" + "    Node newNode = new Node();" + "\n" + "    newNode.setLabel( bestFeature );" + "\n" + "\n"
						+ "    for ( String value : getValues(bestFeature) )" + "\n" + "        id3( examples.getReducedExampleTableFor(value) );"
						+ "\n" + "\n" + "    Tree newTree = new Tree( newNode.attach(getAllCreatedSubtrees()) );" + "\n" + "    return newTree;"
						+ "\n" + "} ";
			}

		} else {

			if (codeLanguage) {
				return "Prozedur ID3" + "\n" + "\n" + "    falls ( alle Beispiele gehören einer Klasse c an )" + "\n"
						+ "        blatt = erstelle ein neues Blatt;" + "\n" + "        beschrifte blatt mit c;" + "\n"
						+ "        gib blatt als resultierenden Baum zurück;" + "\n" + "\n"
						+ "    besteEigenschaft = wähle die informativste Eigenschaft mithilfe der gewählten Maßes aus;" + "\n" + "\n"
						+ "    knoten = erstelle einen neuen Knoten;" + "\n" + "    beschrifte knoten mit dem Namen der Eigenschaft;" + "\n" + "\n"
						+ "    für ( jeden Wert von besteEigenschaft )" + "\n"
						+ "        rufe die Prozedur ID3 mit allen Beispielen dieses Wertes für besteEigenschaft auf;" + "\n" + "\n"
						+ "    neuerBaum = erstelle neuen Baum, indem erzeugte Subbäume an knoten befestigt werden;" + "\n"
						+ "    gib neuerBaum zurück;" + "\n" + " ";
			} else {
				return "public Baum id3(List<Beispiel> beispiele) {" + "\n" + "\n" + "    if ( beispiele.anzahlKlassen() == 1 )" + "\n"
						+ "        Blatt blatt = new Blatt();" + "\n" + "        blatt.setzeBezeichnung( beispiele.bestimmeKlasseAllerBeispiele() );"
						+ "\n" + "        return new Baum( blatt );" + "\n" + "\n" + "    String besteEigenschaft = bestimmeBesteEigenschaft();"
						+ "\n" + "\n" + "    Knoten knoten = new Knoten();" + "\n" + "    knoten.setzeBezeichnung( besteEigenschaft );" + "\n" + "\n"
						+ "    for ( String wert : werteVon(besteEigenschaft) )" + "\n"
						+ "        id3( beispiele.konstruiereReduzierteTabelleFuer(wert) );" + "\n" + "\n"
						+ "    Baum neuerBaum = new Baum( knoten.haengeAn(holeAlleErzeugtenSubbaeume()) );" + "\n" + "    return neuerBaum;" + "\n"
						+ "} ";
			}


		}


	}


	public String getFileExtension() {
		return "asu";
	}


	public Locale getContentLocale() {

		if (language) {
			return Locale.ENGLISH;
		} else {
			return Locale.GERMAN;
		}

	}


	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
	}


	public String getOutputLanguage() {

		if (codeLanguage) {
			return Generator.PSEUDO_CODE_OUTPUT;
		} else {
			return Generator.JAVA_OUTPUT;
		}

	}


	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {

		int probSpecificQuestion = (int) primitives
				.get((language) ? "Probability for specific questions" : "Wahrscheinlichkeit für spezifische Fragen");
		if (probSpecificQuestion < 0 || probSpecificQuestion > 100) {
			return false;
		}

		int exampleNumber = (Integer) primitives.get((language) ? "Example number" : "Beispielnummer");
		if (exampleNumber < 1 || exampleNumber > 9) {
			return false;
		}

		int probRandomQuestion = (int) primitives.get((language) ? "Probability for general questions" : "Wahrscheinlichkeit für allgemeine Fragen");
		if (probRandomQuestion < 0 || probRandomQuestion > 100) {
			return false;
		}

		/*
		 * nodeColor = (Color) primitives.get((language) ? "Color of nodes" :
		 * "Knotenfarbe"); yesColor = (Color) primitives.get((language) ?
		 * "Color of yes-leaves" : "Farbe der Ja-Blätter"); noColor = (Color)
		 * primitives.get((language) ? "Color of no-leaves" :
		 * "Farbe der Nein-Blätter"); tableHighlightColor = (Color)
		 * primitives.get((language) ? "Color of table highlights" :
		 * "Farbe der Tabellenhervorhebungen");
		 * 
		 * if (nodeColor.equals(yesColor) || nodeColor.equals(noColor) ||
		 * noColor.equals(tableHighlightColor) || yesColor.equals(noColor) ||
		 * yesColor.equals(tableHighlightColor) ||
		 * noColor.equals(tableHighlightColor)) { return false; }
		 * 
		 * codeHighlightColor = (Color) primitives.get((language) ?
		 * "Color of code highlights" : "Farbe der Codehervorhebungen");
		 * 
		 * if (yesColor.equals(Color.BLACK) || noColor.equals(Color.BLACK) ||
		 * nodeColor.equals(Color.BLACK) ||
		 * tableHighlightColor.equals(Color.BLACK) ||
		 * codeHighlightColor.equals(Color.BLACK)) { return false; }
		 */

		if (exampleNumber == 9) {

			String[][] codeTable = (String[][]) primitives.get((language) ? "Custom input table" : "Eigene Tabelle");
			if (codeTable.length < 2 || codeTable.length > 7 || codeTable[0].length != 4) {
				return false;
			}

			if (codeTable[0][0].equals(codeTable[0][1]) || codeTable[0][0].equals(codeTable[0][2]) || codeTable[0][0].equals(codeTable[0][3])
					|| codeTable[0][1].equals(codeTable[0][2]) || codeTable[0][1].equals(codeTable[0][3])
					|| codeTable[0][2].equals(codeTable[0][3])) {
				return false;
			}

			for (int i = 0; i < codeTable.length; i++) {
				for (int j = 0; j < codeTable[0].length; j++) {
					if (codeTable[i][j].isEmpty() || codeTable[i][j].matches(".*[0-9]+.*")) {
						return false;
					}
				}
			}

			for (int i = 1; i < codeTable.length; i++) {
				if (!codeTable[i][3].equals((language) ? "yes" : "ja") && !codeTable[i][3].equals((language) ? "no" : "nein")) {
					return false;
				}
			}


			try {

				boolean informationGainOrGiniIndex = (Boolean) primitives.get((language) ? "Information measure" : "Maß für Informationsgehalt");

				ID3Generator id3 = new ID3Generator(informationGainOrGiniIndex, exampleNumber, probRandomQuestion, probSpecificQuestion, codeTable,
						nodeColor, yesColor, noColor, codeHighlightColor, codeColor, codeSize, codeBold, codeItalic, codeFont, tableHighlightColor,
						tableBorderColor, tableElementColor, tableFont, explanationBoxFillColor, explanationBoxColor, language, codeLanguage);

				boolean okay = id3.validate();
				if (!okay) {
					return false;
				}
			} catch (Exception e) {
				// TODO
			}
		}

		return true;
	}


}