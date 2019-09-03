
/*
 * ChomskyNormalform.java
 * Sarah Fischer, Anke Unger, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Offset;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;

public class ChomskyNormalform implements ValidatingGenerator {
	private Language lang;
	private String[][] a;
	private int n;
	private StringArray helpArray, hString;
	private int xkoord = 20;
	private int ykoord = 40;
	TextProperties headerProps, schrittProps, teilschrittProps, schrittPropsFett, teilschrittPropsFett;
	RectProperties recProps;
	ArrayProperties ap, helpArrayProps, helpArrayProps2;
	TextProperties introProps;
	MatrixProperties aProp;
	SourceCodeProperties srcProp;
	ArrayMarkerProperties markerStringProp;
	ArrayMarkerProperties markerCharProp;
	Text cn;
	Text schritt;
	Font nine, ten, eleven, twelve;

	ArrayMarker stringMarker = null;
	private int free = 65 + n;
	private boolean noChom = false;

	public void init() {
		lang = new AnimalScript("Chomsky Normalform", "Sarah Fischer, Anke Unger", 800, 600);

	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		eleven = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
		nine = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

		twelve = new Font(Font.SANS_SERIF, Font.BOLD, 22);
		ten = new Font(Font.SANS_SERIF, Font.BOLD, 18);
		
		headerProps = (TextProperties) props.getPropertiesByName("headerProperties");
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		schrittProps = (TextProperties) props.getPropertiesByName("schritt");
		schrittProps.set(AnimationPropertiesKeys.FONT_PROPERTY, eleven);
		teilschrittProps = (TextProperties) props.getPropertiesByName("teilschritt");
		teilschrittProps.set(AnimationPropertiesKeys.FONT_PROPERTY, nine);
		recProps = (RectProperties) props.getPropertiesByName("headerRectProperties");
		aProp = (MatrixProperties) props.getPropertiesByName("aProperties");
		srcProp = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProperties");
		markerStringProp = (ArrayMarkerProperties) props.getPropertiesByName("arrayMarkerCharProperties");
		markerCharProp = (ArrayMarkerProperties) props.getPropertiesByName("arrayMarkerStringProperties");
		markerStringProp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "Char");
		markerCharProp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "String");
		introProps = (TextProperties) props.getPropertiesByName("IntroEnding");
		helpArrayProps = (ArrayProperties) props.getPropertiesByName("helpArray");
		helpArrayProps2 = (ArrayProperties) props.getPropertiesByName("helpArray2");
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		lang.setStepMode(true);

		cn = lang.newText(new Coordinates(xkoord, ykoord), "Chomsky Normalform", "Text", null, headerProps);
		Rect rect = lang.newRect(new Offset(-5, -5, cn, AnimalScript.DIRECTION_NW),
				new Offset(5, 5, cn, AnimalScript.DIRECTION_SE), "Rect", null, recProps);

		Text intro, intro1, intro2, intro3, intro4, intro5, intro6, intro7, intro8, intro9, intro10, intro11, intro12,
				end, end2, end3, end4, end5, zyk, zyk1, zyk2;
		introProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		intro = lang.newText(new Offset(0, 100, cn, AnimalScript.DIRECTION_SW),
				"Die Chomsky Normalform besteht aus Terminalen und Nicht-Terminalen. Terminale sind atomare Einheiten, während Nicht-Terminale",
				"Introduction1", null, introProps);
		intro1 = lang.newText(new Offset(0, 10, intro, AnimalScript.DIRECTION_SW),
				"aufgelöst werden müssen. Entweder zu Terminalen oder zu weiteren Nicht-Terminalen, die wieder aufgelöst werden, bis ein Terminal erreicht wird.",
				"Introduction2", null, introProps);
		intro2 = lang.newText(new Offset(0, 10, intro1, AnimalScript.DIRECTION_SW),
				"Die Normalform kann aus jeder kontextfreien Sprache erzeugt werden. Ein Beispiel für eine kontextfreie Sprache wäre:",
				"Introduction3", null, introProps);
		intro3 = lang.newText(new Offset(0, 10, intro2, AnimalScript.DIRECTION_SW),
				"A -> B|a und B -> b mit Großbuchstaben als Nicht-Terminale und Kleinbuchstaben als Terminale. Der | bedeutet, dass A entweder als B oder als a aufgelöst werden kann.",
				"Introduction4", null, introProps);
		intro4 = lang.newText(new Offset(0, 10, intro3, AnimalScript.DIRECTION_SW),
				"Als Chomsky Normalform gilt eine Grammatik, wenn nur die folgenden Formen existieren:",
				"Introduction5", null, introProps);
		intro5 = lang.newText(new Offset(5, 10, intro4, AnimalScript.DIRECTION_SW),
				"A -> AB - Ein Nicht-Terminal zeigt auf genau zwei Nicht-Terminale", "Introduction6", null, introProps);
		intro6 = lang.newText(new Offset(0, 10, intro5, AnimalScript.DIRECTION_SW),
				"A -> a - Ein Nicht-Terminal zeigt auf genau ein Terminal", "Introduction7", null, introProps);
		intro7 = lang.newText(new Offset(-5, 10, intro6, AnimalScript.DIRECTION_SW), "Unzulässig sind:",
				"Introduction8", null, introProps);
		intro8 = lang.newText(new Offset(5, 10, intro7, AnimalScript.DIRECTION_SW),
				"- das leere Wort, im Folgenden als 3 bezeichnet (A -> B|3)", "Introduction9", null, introProps);
		intro9 = lang.newText(new Offset(0, 10, intro8, AnimalScript.DIRECTION_SW), "- Ketten (A -> B, B -> CD)",
				"Introduction10", null, introProps);
		intro10 = lang.newText(new Offset(0, 10, intro9, AnimalScript.DIRECTION_SW),
				"- Nicht alleinstehende Terminale (A -> aB)", "Introduction11", null, introProps);
		intro11 = lang.newText(new Offset(0, 10, intro10, AnimalScript.DIRECTION_SW),
				"- Mehr oder weniger als 2 Nicht-Terminale (A -> ABC)", "Introduction12", null, introProps);
		intro12 = lang.newText(new Offset(-5, 40, intro11, AnimalScript.DIRECTION_SW),
				"Hinweis: Im Folgenden wird der Teil links neben -> als linke Seite und der Teil rechts neben -> als rechte Seite bezeichnet.",
				"Introduction12", null, introProps);


		lang.nextStep();
		intro.hide();
		intro1.hide();
		intro2.hide();
		intro3.hide();
		intro4.hide();
		intro5.hide();
		intro6.hide();
		intro7.hide();
		intro8.hide();
		intro9.hide();
		intro10.hide();
		intro11.hide();
		intro12.hide();

		lang.nextStep();

		a = (String[][]) primitives.get("a");
		n = a.length;
		epsilon(a);
		if (noChom) {
			zyk = lang.newText(new Offset(100, 100, cn, AnimalScript.DIRECTION_SW),
					"Bei der Eingabe der Kontextfreien Sprache wurde ein Zyklus eingegeben.", "EndFolie1", null,
					introProps);
			zyk1 = lang.newText(new Offset(0, 10, zyk, AnimalScript.DIRECTION_SW),
					"Dadurch zeigen mindestens zwei Nicht-Terminale aufeinander ohne die Möglichkeit sich jemals zu einem anderen (Nicht-)Terminal auflösen zu können.",
					"EndFolie1", null, introProps);
			zyk2 = lang.newText(new Offset(0, 10, zyk1, AnimalScript.DIRECTION_SW),
					"Es konnte somit keine korrekte Chomsky Normalform erzeugt werden. Bitte achten Sie daher darauf, dass die Eingabe bereits keine Zyklen beinhaltet.",
					"EndFolie1", null, introProps);
			end = lang.newText(new Offset(0, 10, zyk2, AnimalScript.DIRECTION_SW),
					"Abgesehen von dem Zyklus befindet sich die kontextfreie Grammatik nun in der Chomsky Normalform.",
					"Introduction1", null, introProps);

		} else {
			end = lang.newText(new Offset(100, 100, cn, AnimalScript.DIRECTION_SW),
					"Die kontextfreie Grammatik befindet sich nun in der Chomsky Normalform.", "Introduction1", null,
					introProps);
		}
		end2 = lang.newText(new Offset(0, 10, end, AnimalScript.DIRECTION_SW),
				"Jedes leere Wort wurde entfernt und alle Verweise auf dieses wurden umgangen.", "Introduction2",
				null, introProps);
		if (noChom) {
			end3 = lang.newText(new Offset(0, 10, end2, AnimalScript.DIRECTION_SW),
					"Verkettungen wurden soweit wie möglich entfernt.", "Introduction3", null, introProps);
		} else {
			end3 = lang.newText(new Offset(0, 10, end2, AnimalScript.DIRECTION_SW),
					"Alle Verkettungen wurden entfernt.", "Introduction3", null, introProps);
		}
		end4 = lang.newText(new Offset(0, 10, end3, AnimalScript.DIRECTION_SW), "Alle Terminale stehen alleine.",
				"Introduction4", null, introProps);
		if (noChom) {
			end5 = lang.newText(new Offset(0, 10, end4, AnimalScript.DIRECTION_SW),
					"Und jedes Nicht-Terminal, welches keinen Zyklus bildet, steht auf der rechten Seite mit genau einem weiteren Nicht-Terminal da.",
					"Introduction3", null, introProps);
		} else {
			end5 = lang.newText(new Offset(0, 10, end4, AnimalScript.DIRECTION_SW),
					"Und jedes Nicht-Terminal auf der rechten Seite steht mit genau einem weiteren Nicht-Terminal da.",
					"Introduction4", null, introProps);
		}

		lang.finalizeGeneration();
		return lang.toString();
	}

	public String getName() {
		return "Chomsky Normalform";
	}

	public String getAlgorithmName() {
		return "Chomsky Normalform";
	}

	public String getAnimationAuthor() {
		return "Sarah Fischer, Anke Unger";
	}

	public String getDescription() {
		return "Die Chomsky Normalform ist eine kontextfreie Grammatik, für die gilt, dass " + System.lineSeparator()
				+ "für jede kontextfreie Sprache eine Chomsky Normalform erzeugt werden kann." + System.lineSeparator()
				+ "Damit diese Visualisierung funktioniert, gelten die folgenden Eingaberegeln: "
				+ System.lineSeparator()
				+ "Kontextfrei bedeutet, dass links nur ein Nicht-Terminal stehen darf. Dahinter kommt ein -> und anschließend beliebig viele (Nicht-Terminale), welche mit einem | getrennt werden können. "
				+ System.lineSeparator()
				+ "Das leere Wort wird mit einer 3 dargestellt und kann genauso eingegeben werden wie (Nicht-)Terminale."
				+ System.lineSeparator() + "Zum Beispiel:" + System.lineSeparator() + "A -> B|CBC|ab|a"
				+ System.lineSeparator() + "B -> C|bc" + System.lineSeparator() + "C -> a|3";
	}

	public String getCodeExample() {
		return "Entfernen von Epsilon:\n\n"
				+ "Enthält der aktuelle String ein leeres Wort?\n"
				+ "   Ja: Dann füge die linke Seite dem Array M hinzu\n"
				+ "      und lösche das leere Wort aus dem Term\n"
				+ "\n\n"
				+ "Gehe im Term die rechte Seite durch\n"
				+ "   Gehe in jedem String alle einzelnen Zeichen durch\n"
				+ "      Existiert das aktuelle Zeichen in M und ist der String 1 Zeichen lang?\n"
				+ "         Ja: Existiert die linke Seite von dem Zeichen noch nicht in M?\n"
				+ "            Ja: Füge die linke Seite in M ein\n"
				+ "\n\n"
				+ "Gehe im Term die rechte Seite durch\n"
				+ "   Gehe in jedem String alle einzelnen Zeichen durch\n"
				+ "      Existiert das aktuelle Zeichen in M und ist der String länger als 1?\n"
				+ "         Ja: Füge den String ohne das aktuellen Zeichen ans Ende des Terms";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}
	int count = 0;
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

		a = (String[][]) primitives.get("a");
		// return checkInput(a);
		String b = null;

		if (a[0].length > 1) {
			for (int i = 0; i < a.length; i++) {
				for (int j = 1; j < a[i].length; j++) {
					a[i][j] = "";
				}
			}
		}

		for (int i = 0; i < a.length; i++) {
			if (a[i][0].equals("")) {
				return false;
			}
		}

		for (int i = 0; i < a.length; i++) {
			b = a[i][0];
			if (!String.valueOf(b.charAt(0)).matches("[A-Z]")) {
				if (String.valueOf(b.charAt(0)).matches("[a-z]")) {
					a[i][0] = String.valueOf(b.charAt(0)).toUpperCase() + a[i][0].substring(1, b.length());

				} else {
					return false;
				}
			}
		}
		for(int i = 0; i < a.length; i++){
			b = a[i][0];
			if (!b.substring(1, 5).equals(" -> ")) {
				return false;
			}
			if (!String.valueOf(b.charAt(5)).matches("[a-zA-Z]")) {
				return false;
			}
			if (!((String.valueOf(b.charAt(b.length() - 1))).matches("[a-zA-Z]") || b.charAt(b.length() - 1) == '3')) {
				return false;
			}
			boolean bool = false;
			for (int j = 6; j < b.length() - 1; j++) {
				if (b.charAt(j) == '|') {

					if (bool) {
						return false;
					}
					bool = true;
				} else if (String.valueOf(b.charAt(j)).matches("[a-zA-Z]") || b.charAt(j) == '3') {
					bool = false;
				} else {
					return false;
				}
			}
			boolean bool2 = true;
			for (int j = 6; j < b.length(); j++) {
				bool2 = true;
				if (String.valueOf(b.charAt(j)).matches("[A-Z]")) {
					for (int k = 0; k < a.length; k++) {
						if (b.charAt(j) == a[k][0].charAt(0)) {
							bool2 = false;

							if (i == k) {

								if (j == b.length() - 1) {
									if (b.charAt(j - 1) == '|') {
										a[i][0] = a[i][0].substring(0, j) + String.valueOf(b.charAt(j)).toLowerCase();
									}
								} else {
									if (b.charAt(j - 1) == '|' && b.charAt(j + 1) == '|') {
										a[i][0] = a[i][0].substring(0, j) + String.valueOf(b.charAt(j)).toLowerCase()
												+ a[i][0].substring(j + 1, b.length());
									}
								}
							}
						}
					}
				} else {
					bool2 = false;
				}
				if (bool2) {
					return false;
				}
			}

		}
		return true;

	}

	public void epsilon(String[][] a) {
		lang.setStepMode(true);
		ap = new ArrayProperties();
		ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		StringMatrix array = lang.newStringMatrix(new Offset(0, 40, cn, AnimalScript.DIRECTION_SW), a, "array",
				new ArrayDisplayOptions(null, null, false), aProp);
		lang.nextStep();

		StringArray mArray = null;
		MultipleChoiceQuestionModel reihenfolge = new MultipleChoiceQuestionModel("reihenfolge");
		reihenfolge.setPrompt("Mit welchen Schritten kommt man zur Chomsky Normalform?");
		reihenfolge.addAnswer(
				"Epsilon entfernen, Ketten entfernen, nicht alleinstehende Nicht-Terminale ersetzen, alle Terme auf 2 Terminale reduzieren",
				0,
				"Leider falsch, es müssen alle nicht alleinstehenden Terminale ersetzt werden und alle Terme auf 2 Nicht-Terminale reduiert werden.");
		reihenfolge.addAnswer(
				"Epsilon entfernen, Ketten entfernen, nicht alleinstehende Nicht-Terminale ersetzen, alle Terme auf 2 Nicht-Terminale reduzieren",
				0, "Leider falsch, es müssen alle nicht alleinstehenden Terminale ersetzt werden.");
		reihenfolge.addAnswer(
				"Epsilon entfernen, Ketten entfernen, nicht alleinstehende Terminale ersetzen, alle Terme auf 2 Nicht-Terminale reduzieren",
				1, "Richtig.");
		reihenfolge.addAnswer(
				"Epsilon entfernen, Ketten entfernen, nicht alleinstehende Terminale ersetzen, alle Terme auf 2 Terminale reduzieren",
				0, "Leider falsch, es müssen alle Terme auf 2 Nicht-Terminale reduziert werden.");
		lang.addMCQuestion(reihenfolge);

		Text schritt, schritt2, schritt3, schritt4, teilschritt, teilschritt2, teilschritt3, mArrayT;
		TextProperties mArrayProp = new TextProperties();

		mArrayT = lang.newText(new Offset(0, 50, array, AnimalScript.DIRECTION_SW), "M:", "m", null, mArrayProp);

		ArrayMarker helpMarker = null;
		mArrayProp.set(AnimationPropertiesKeys.FONT_PROPERTY, eleven);

		SourceCode src1, src2, src3 = null;
		schritt = lang.newText(new Offset(40, 120, cn, AnimalScript.DIRECTION_SE),
				"1. Finde alle leeren Wörter (3) und entferne diese. Bsp.: A -> B|3 wird zu A -> B", "Schritte", null,
				schrittProps);
		teilschritt = lang.newText(new Offset(10, 5, schritt, AnimalScript.DIRECTION_SW),
				"I. Prüfe, ob der aktuelle String ein leeres Wort (3) ist.", "Teilschritte", null, teilschrittProps);

		src1 = lang.newSourceCode(new Offset(10, 5, teilschritt, AnimalScript.DIRECTION_SW), "src", null, srcProp);
		src1.addCodeLine("Enthält der aktuelle String ein leeres Wort?", "1", 0, null);
		src1.addCodeLine("Ja: Dann füge die linke Seite M hinzu", "2", 1, null);
		src1.addCodeLine("und lösche das leere Wort aus dem Term", "3", 1, null);

		teilschritt2 = lang.newText(new Offset(-10, 10, src1, AnimalScript.DIRECTION_SW),
				"II. Prüfe, ob der aktuelle String aus einem Nicht-Terminal besteht, welches auf ein Nicht-Terminal aus M zeigt.",
				"Teilschritt", null, teilschrittProps);

		src2 = lang.newSourceCode(new Offset(10, 5, teilschritt2, AnimalScript.DIRECTION_SW), "src", null, srcProp);
		src2.addCodeLine("Gehe im Term die rechte Seite durch", "5", 0, null);
		src2.addCodeLine("Gehe in jedem String alle einzelnen Zeichen durch", "6", 1, null);
		src2.addCodeLine("Existiert das aktuelle Zeichen in M und ist der String 1 Zeichen lang?", "7", 2, null);
		src2.addCodeLine("Ja: Existiert die linke Seite von dem Zeichen noch nicht in M?", "8", 3, null);
		src2.addCodeLine("Ja: Füge die linke Seite in M ein", "9", 4, null);

		teilschritt3 = lang.newText(new Offset(-10, 10, src2, AnimalScript.DIRECTION_SW),
				"III. Prüfe, ob der aktuelle String aus mehr als einem (Nicht-)Terminal besteht und ein Nicht-Terminal aus M enthält.",
				"Todo", null, teilschrittProps);

		src3 = lang.newSourceCode(new Offset(10, 5, teilschritt3, AnimalScript.DIRECTION_SW), "src", null, srcProp);
		src3.addCodeLine("Gehe im Term die rechte Seite durch", "10", 0, null);
		src3.addCodeLine("Gehe in jedem String alle einzelnen Zeichen durch", "11", 1, null);
		src3.addCodeLine("Existiert das aktuelle Zeichen in M und ist der String länger als 1?", "12", 2, null);
		src3.addCodeLine("Ja: Füge den String ohne das aktuellen Zeichen ans Ende des Terms", "13", 3, null);

		schritt2 = lang.newText(new Offset(-20, 10, src3, AnimalScript.DIRECTION_SW),
				"2. Finde alle Verkettungen und entferne diese. Bsp.: A -> CD, B -> A, dann wird B -> A zu B -> CD",
				"Schritte", null, schrittProps);
		schritt3 = lang.newText(new Offset(0, 5, schritt2, AnimalScript.DIRECTION_SW),
				"3. Finde alle Terminale, die nicht alleine stehen und weise ihnen ein Nicht-Terminal zu. Bsp.: A -> Ba wird zu A -> BC und C -> a",
				"Schritte", null, schrittProps);
		schritt4 = lang.newText(new Offset(0, 5, schritt3, AnimalScript.DIRECTION_SW),
				"4. Finde Terme, die aus mehr als 2 Nicht-Terminalen bestehen und reduziere diese. Bsp.: A -> BCD, wird zu A -> BE und E -> CD",
				"Schritte", null, schrittProps);

		schritt.setFont(twelve, null, null);
		int count = 0;
		String[] m = new String[0];
		String[] n = new String[0];
		// arrayMarker = lang.newArrayMarker(array, 0, "arrayMarker", null,
		// arrayMarkerProp);
		for (int i = 0; i < array.getNrRows(); i++) {
			array.highlightCell(i, 0, null, null);
			if(i==0){
				lang.nextStep("Epsilon entfernen");
			}else lang.nextStep();
			String[] help = splitter(array.getElement(i, 0));
			// Suche nach Epsilons und speichere die linke Seite in m
			if (helpArray != null) {
				helpArray.hide();
			}

			helpArray = lang.newStringArray(new Offset(40, 50, cn, AnimalScript.DIRECTION_SE), help, "array", null, helpArrayProps);
			if (helpMarker != null) {
				helpMarker.hide();
			}
			helpMarker = lang.newArrayMarker(helpArray, 1, "array", null, markerCharProp);

			lang.nextStep();

			for (int j = 1; j < helpArray.getLength(); j++) {
				teilschritt.setFont(ten, null, null);
				lang.nextStep();
				helpMarker.move(j, null, null);
				src1.highlight(0);
				lang.nextStep();
				if (helpArray.getData(j).contains("3")) {
					if (count != 0) {
						n = new String[count + 1];
						System.arraycopy(m, 0, n, 0, count - 1);
					}
					m = new String[count + 1];
					System.arraycopy(n, 0, m, 0, n.length);
					m[count] = String.valueOf(array.getElement(i, 0).charAt(0));
					count += 1;
					String[] help2 = new String[helpArray.getLength() - 1];
					src1.unhighlight(0);
					src1.highlight(1);
					lang.nextStep();
					// Lösche das Epsilon
					if (j == 0) {
						for (int l = 0; l < helpArray.getLength() - 1; l++) {
							help2[l] = helpArray.getData(l + 1);
						}
					} else if (j == helpArray.getLength() - 1) {
						for (int l = 0; l < helpArray.getLength() - 1; l++) {
							help2[l] = helpArray.getData(l);
						}
					} else {
						for (int l = 0; l < helpArray.getLength() - 1; l++) {
							if (l >= j) {
								help2[l] = helpArray.getData(l + 1);
							} else
								help2[l] = helpArray.getData(l);
						}
					}
					if (!(mArray == null)) {
						mArray.hide();
					}

					mArray = lang.newStringArray(new Offset(0, 5, mArrayT, AnimalScript.DIRECTION_SW), m, "mArray",
							null, ap);
					lang.nextStep();
					helpArray.hide();
					helpArray = lang.newStringArray(new Offset(40, 50, cn, AnimalScript.DIRECTION_SE), help2, "array",
							null, helpArrayProps);
					src1.unhighlight(1);
					src1.highlight(2);
					helpMarker.hide();

					array.put(i, 0, append(help2), null, null);
					lang.nextStep();

				}
				src1.unhighlight(0);
				src1.unhighlight(1);
				src1.unhighlight(2);

			}
			array.unhighlightCell(i, 0, null, null);
		}
		teilschritt.setFont(nine, null, null);
		// Suche nach nicht-Terminalen die ein Nicht-Terminal aus m besitzen
		if (count > 0) {
			// int hsize, zaehler;

			teilschritt2.setFont(ten, null, null);
			for (int i = 0; i < array.getNrRows(); i++) {
				array.highlightCell(i, 0, null, null);
				lang.nextStep();
				String[] help = splitter(array.getElement(i, 0));
				helpArray.hide();
				helpArray = lang.newStringArray(new Offset(40, 50, cn, AnimalScript.DIRECTION_SE), help, "array", null,
						helpArrayProps);
				if (helpMarker != null) {
					helpMarker.hide();
				}
				helpMarker = lang.newArrayMarker(helpArray, 1, "array", null, markerCharProp);
				// Einzelnstehende Nicht-Terminale
				boolean bool = true;
				for (int j = 1; j < helpArray.getLength(); j++) {
					src2.highlight(0);

					helpMarker.move(j, null, null);
					lang.nextStep();

					for (int k = 0; k < helpArray.getData(j).length(); k++) {
						if (hString != null) {
							hString.hide();
						}
						hString = lang.newStringArray(new Offset(40, 0, helpArray, AnimalScript.DIRECTION_NE),
								helpArray.getData(j).split(""), "array", null, helpArrayProps);
						if (stringMarker != null) {
							stringMarker.hide();
						}
						stringMarker = lang.newArrayMarker(hString, 0, "hString", null, markerStringProp);
						stringMarker.move(k, null, null);

						src2.unhighlight(0);
						src2.highlight(1);
						lang.nextStep();
						// Wenn das Nicht-Terminal links bereits vorhanden ist,
						// setze bool auf false
						MultipleChoiceQuestionModel leerewort = new MultipleChoiceQuestionModel("leerewort");
						leerewort.setPrompt(
								"Ergänze die fehlenden Wörter: Bei dem Term A -> a steht links ein ___ und rechts ein ___");
						leerewort.addAnswer("leeres Wort und Terminal", 0,
								"Leider falsch, A ist ein Nicht-Terminal und a ein Terminal.");
						leerewort.addAnswer("Terminal und Nicht-Terminal", 0,
								"Leider falsch, A ist ein Nicht-Terminal und a ein Terminal.");
						leerewort.addAnswer("Nicht-Terminal und Terminal", 1,
								"Richtig, A ist ein Nicht-Terminal und a ein Terminal");
						leerewort.addAnswer("Nicht-Terminal und leeres Wort", 0,
								"Leider falsch, A ist ein Nicht-Terminal und a ein Terminal.");
						lang.addMCQuestion(leerewort);
						for (int l = 0; l < m.length; l++) {
							bool = true;
							src2.unhighlight(1);
							src2.highlight(2);
							lang.nextStep();
							if ((helpArray.getData(j).charAt(k) == m[l].charAt(0))
									&& (helpArray.getData(j).length() == 1)) {
								for (int o = 0; o < m.length; o++) {
									if (m[l].charAt(o) == array.getElement(j, 0).charAt(0)) {
										bool = false;
									}
								}

								// Ist das Nicht-Terminal links noch nicht
								// enthalten
								// füge es ein.
								src2.unhighlight(2);
								src2.highlight(3);
								lang.nextStep();
								if (bool) { // falls bool = false, ist das
											// Nicht-Terminal schon in m und
											// muss nicht mehr abgespeichert
											// werden
									src2.unhighlight(3);
									src2.highlight(4);
									n = new String[count + 1];
									System.arraycopy(m, 0, n, 0, m.length);
									m = new String[n.length];
									System.arraycopy(n, 0, m, 0, n.length);
									m[count] = String.valueOf(array.getElement(i, 0).charAt(0));
									count++;
									// Setze j wieder auf 1 und gehe erneut von
									// oben
									// durch
									j = 1;
									mArray.hide();
									mArray = lang.newStringArray(new Offset(0, 50, array, AnimalScript.DIRECTION_SW), m,
											"mArray", null, ap);
									lang.nextStep();
								}
								src2.unhighlight(4);
								lang.nextStep();
							}
							src2.unhighlight(3);
							hString.hide();
							stringMarker.hide();
						}
						src2.unhighlight(2);
					}
					src2.unhighlight(1);
				}
				src2.unhighlight(0);

				lang.nextStep();
				array.unhighlightCell(i, 0, null, null);
			}
			teilschritt2.setFont(nine, null, null);

			for (int i = 0; i < array.getNrRows(); i++) {
				array.highlightCell(i, 0, null, null);
				lang.nextStep();
				String[] help = splitter(array.getElement(i, 0));
				helpArray.hide();
				helpArray = lang.newStringArray(new Offset(40, 50, cn, AnimalScript.DIRECTION_SE), help, "array", null,
						helpArrayProps);

				teilschritt3.setFont(ten, null, null);
				for (int j = 1; j < helpArray.getLength(); j++) {

					if (helpMarker != null) {
						helpMarker.hide();
					}

					helpMarker = lang.newArrayMarker(helpArray, 0, "array", null, markerCharProp);
					helpMarker.move(j, null, null);
					lang.nextStep();
					src3.highlight(0);
					lang.nextStep();
					for (int k = 0; k < helpArray.getData(j).length(); k++) {
						src3.unhighlight(0);
						src3.highlight(1);
						lang.nextStep();

						if (hString != null) {
							hString.hide();
						}
						hString = lang.newStringArray(new Offset(40, 0, helpArray, AnimalScript.DIRECTION_NE),
								helpArray.getData(j).split(""), "array", null, helpArrayProps);
						if (stringMarker != null) {
							stringMarker.hide();
						}
						stringMarker = lang.newArrayMarker(hString, 0, "hString", null, markerStringProp);
						stringMarker.move(k, null, null);

						// Falls das aktuelle char gleich einem nicht-Terminal
						// aus m ist und der aktuelle String größer als 1 ist,
						// füge den String ohne dem nicht-Terminal nochmal
						// hinten an.
						for (int l = 0; l < m.length; l++) {
							src3.unhighlight(1);
							src3.highlight(2);
							lang.nextStep();
							if ((helpArray.getData(j).charAt(k) == m[l].charAt(0))
									&& (helpArray.getData(j).length() > 1)) {
								src3.unhighlight(2);
								src3.highlight(3);
								String[] help2 = new String[helpArray.getLength() + 1];
								for (int o = 0; o < helpArray.getLength(); o++) {
									help2[o] = helpArray.getData(o);
								}
								if (k < 1) {
									help2[help2.length - 1] = helpArray.getData(j).substring(k + 1,
											helpArray.getData(j).length());
								} else if (k == helpArray.getData(j).length() - 1) {
									help2[help2.length - 1] = helpArray.getData(j).substring(0, k);
								} else {
									help2[help2.length - 1] = helpArray.getData(j).substring(0, k)
											+ helpArray.getData(j).substring(k + 1, helpArray.getData(j).length());
								}
								// Wenn Nicht-Terminal aus M enthalten, lösche
								// dieses aus dem String und füge ihn hinten an
								helpArray.hide();
								helpArray = lang.newStringArray(new Offset(40, 50, cn, AnimalScript.DIRECTION_SE),
										help2, "array", null, helpArrayProps);
								helpArray.highlightCell(helpArray.getLength(), null, null);
								hString.hide();
								hString = lang.newStringArray(new Offset(40, 0, helpArray, AnimalScript.DIRECTION_NE),
										helpArray.getData(j).split(""), "array", null, helpArrayProps);
								stringMarker.hide();
								stringMarker = lang.newArrayMarker(hString, 0, "hString", null, markerStringProp);
								stringMarker.move(k, null, null);
								lang.nextStep();
								helpMarker.move(j, null, null);

								array.put(i, 0, append(help2), null, null);

								lang.nextStep();
							}
							src3.unhighlight(3);

						}

						src3.unhighlight(2);
					}
					hString.hide();
					stringMarker.hide();
					src3.unhighlight(1);
				}
				src3.unhighlight(0);
				array.unhighlightCell(i, 0, null, null);
			}
			teilschritt3.setFont(nine, null, null);
			mArray.hide();
			if (helpArray != null) {
				helpArray.hide();
			}
			if (helpMarker != null) {
				helpMarker.hide();
			}
		}
		lang.nextStep();
		teilschritt.hide();
		teilschritt2.hide();
		teilschritt3.hide();
		src1.hide();
		src2.hide();
		src3.hide();
		schritt2.hide();
		schritt3.hide();
		schritt4.hide();
		schritt2 = lang.newText(new Offset(0, 10, schritt, AnimalScript.DIRECTION_SW),
				"2. Finde alle Verkettungen und entferne diese. Bsp.: A -> CD, B -> A, dann wird B -> A zu B -> CD",
				"Schritte", null, schrittProps);
		schritt3 = lang.newText(new Offset(0, 5, schritt2, AnimalScript.DIRECTION_SW),
				"3. Finde alle Terminale, die nicht alleine stehen und weise ihnen ein Nicht-Terminal zu. Bsp.: A -> Ba wird zu A -> BC und C -> a",
				"Schritte", null, schrittProps);
		schritt4 = lang.newText(new Offset(0, 5, schritt3, AnimalScript.DIRECTION_SW),
				"4. Finde Terme, die aus mehr als 2 Nicht-Terminalen bestehen und reduziere diese. Bsp.: A -> BCD, wird zu A -> BE und E -> CD",
				"Schritte", null, schrittProps);
		lang.nextStep();
		schritt.hide();
		schritt2.hide();
		schritt3.hide();
		schritt4.hide();
		helpMarker.hide();
		mArrayT.hide();
		helpArray.hide();

		chain(array, helpMarker);
	}

	// Entferne alle Verkettungen
	public void chain(StringMatrix array, ArrayMarker am) {
		Text schritt, schritt2, schritt3, schritt4, zykl;
		SourceCode src = null;

		schritt = lang.newText(new Offset(40, 120, cn, AnimalScript.DIRECTION_SE),
				"1. Finde alle leeren Wörter (3) und entferne diese. Bsp.: A -> B|3 wird zu A -> B", "Schritte", null,
				schrittProps);
		schritt2 = lang.newText(new Offset(0, 5, schritt, AnimalScript.DIRECTION_SW),
				"2. Finde alle Verkettungen und entferne diese. Bsp.: A -> CD, B -> A, dann wird B -> A zu B -> CD",
				"Schritte", null, schrittProps);
		src = lang.newSourceCode(new Offset(10, 5, schritt2, AnimalScript.DIRECTION_SW), "src", null, srcProp);

		// src.addCodeLine("Gehe im Term die rechte Seite durch", "1", 0, null);
		src.addCodeLine("Besteht der String aus nur einem Zeichen?", "1", 0, null);
		src.addCodeLine("Ja: Ist das Zeichen ein Nicht-Terminal (Großbuchstabe)?", "2", 1, null);
		src.addCodeLine("Ja: Ersetze das Zeichen durch die rechte Seite von dem aktuellen Nicht-Terminal", "3", 2,
				null);
		schritt3 = lang.newText(new Offset(-10, 10, src, AnimalScript.DIRECTION_SW),
				"3. Finde alle Terminale, die nicht alleine stehen und weise ihnen ein Nicht-Terminal zu. Bsp.: A -> Ba wird zu A -> BC und C -> a",
				"Schritte", null, schrittProps);
		schritt4 = lang.newText(new Offset(0, 5, schritt3, AnimalScript.DIRECTION_SW),
				"4. Finde Terme, die aus mehr als 2 Nicht-Terminalen bestehen und reduziere diese. Bsp.: A -> BCD, wird zu A -> BE und E -> CD",
				"Schritte", null, schrittProps);

		schritt2.setFont(twelve, null, null);

		lang.nextStep("Kettenregel entfernen");

		int zyklus = 0;

		for (int i = 0; i < array.getNrRows(); i++) {
			array.highlightCell(i, 0, null, null);
			lang.nextStep();
			String[] help = splitter(array.getElement(i, 0));
			helpArray.hide();
			helpArray = lang.newStringArray(new Offset(40, 50, cn, AnimalScript.DIRECTION_SE), help, "array", null, helpArrayProps);
			for (int j = 1; j < helpArray.getLength(); j++) {
				zyklus = 0;

				// teilschritt.setFont(ten, null, null);
				if (am != null) {
					am.hide();
				}
				am = lang.newArrayMarker(helpArray, 0, "array", null, am.getProperties());
				am.move(j, null, null);
				src.highlight(0);
				lang.nextStep();
				char[] kreis = new char[10];
				// Wenn help[j] nur ein nicht-Terminal besitzt
				while (helpArray.getData(j).length() < 2) {
					char old = 0;
					src.unhighlight(0);
					src.highlight(1);
					lang.nextStep();
					if (!((int) helpArray.getData(j).charAt(0) >= 97 && (int) helpArray.getData(j).charAt(0) <= 122)) {
						src.unhighlight(1);
						src.highlight(2);
						lang.nextStep();

						int links = 0;
						// finde alleinstehendes nicht-Terminal auf der linken
						// Seite
						for (int k = 0; k < array.getNrRows(); k++) {

							if (array.getElement(k, 0).charAt(0) == helpArray.getData(j).charAt(0)) {
								links = k;
							}
						}
						String[] add = splitter(array.getElement(links, 0));
						String[] add2 = new String[add.length - 1];
						System.arraycopy(add, 1, add2, 0, add2.length);
						// System.arraycopy(add, 1, add, 0, add.length-1);
						int laenge = helpArray.getLength() + add2.length - 1;

						// Erstelle neuen String, mit ersetzem nicht-Terminal,
						// das vorher eine Kettenregel gebildet hat
						String[] help2 = new String[laenge];
						int save = 0;
						for (int k = 0; k < help2.length; k++) {

							if (k < j) {
								help2[k] = helpArray.getData(k);
							} else if (k > j) {
								help2[k] = helpArray.getData(save);
								save++;
							} else if (k == j) {
								save = k + 1;
								for (int l = 0; l < add2.length; l++) {
									help2[k] = add2[l];
									if (l != (add2.length - 1)) {
										k++;
									}
								}
							}
							// Speichere rechte Seite von nicht-Terminal ans
							// Ende

						}
						old = helpArray.getData(j).charAt(0);
						helpArray.hide();
						helpArray = lang.newStringArray(new Offset(40, 50, cn, AnimalScript.DIRECTION_SE), help2,
								"array", null, helpArrayProps);
						am.move(j, null, null);
						// old = helpArray.getData(j).charAt(0);
						// //array.getElement(i, 0).charAt(0);
						array.put(i, 0, append(help2), null, null);
						lang.nextStep();
					}
					src.unhighlight(2);
					zyklus = zyklus + 1;
					for (int k = 0; k < kreis.length; k++) {
						if (kreis[k] == old) {
							noChom = true;
							zykl = lang.newText(new Offset(40, 0, helpArray, AnimalScript.DIRECTION_NE),
									"Zyklus wurde eingegeben, auflösen nicht möglich.", "zykl", null, schrittProps);
							lang.nextStep();
							zykl.hide();
							break;
						}

					}
					if (noChom) {
						break;
					}
					if (zyklus == 10) {
						noChom = true;
						zykl = lang.newText(new Offset(40, 0, helpArray, AnimalScript.DIRECTION_NE),
								"Zyklus wurde eingegeben, auflösen nicht möglich.", "zykl", null, schrittProps);
						lang.nextStep();
						zykl.hide();
						break;
					}
					kreis[zyklus - 1] = old;
				}
				src.unhighlight(1);
			}
			if (helpArray != null) {
				helpArray.hide();
			}
			if (am != null) {
				am.hide();
			}
			src.unhighlight(0);
			array.unhighlightCell(i, 0, null, null);
		}
		lang.nextStep();
		schritt3.hide();
		schritt4.hide();
		src.hide();
		schritt3 = lang.newText(new Offset(0, 10, schritt2, AnimalScript.DIRECTION_SW),
				"3. Finde alle Terminale, die nicht alleine stehen und weise ihnen ein Nicht-Terminal zu. Bsp.: A -> Ba wird zu A -> BC und C -> a",
				"Schritte", null, schrittProps);
		schritt4 = lang.newText(new Offset(0, 5, schritt3, AnimalScript.DIRECTION_SW),
				"4. Finde Terme, die aus mehr als 2 Nicht-Terminalen bestehen und reduziere diese. Bsp.: A -> BCD, wird zu A -> BE und E -> CD",
				"Schritte", null, schrittProps);
		lang.nextStep();
		schritt.hide();
		schritt2.hide();
		schritt3.hide();
		schritt4.hide();

		replace(array, am);

	}

	public void replace(StringMatrix array, ArrayMarker am) {
		Text schritt, schritt2, schritt3, schritt4;
		SourceCode src = null;

		MultipleChoiceQuestionModel verteilung = new MultipleChoiceQuestionModel("verteilung");
		verteilung.setPrompt("Welche Terme sind nach der Chomsky Normalform zulässig?");
		verteilung.addAnswer("A -> BC, B -> b, C -> CC|a", 1,
				"Richtig, alle Nicht-Terminale stehen zu Zweit und lassen sich zu einem Terminal auflösen und alle Terminale stehen alleine.");
		verteilung.addAnswer("A -> BC, B -> b, C -> CC|aB", 0, "Leider falsch, Terminale dürfen nur alleine stehen.");
		verteilung.addAnswer("A -> BB, B -> c, C -> D", 0,
				"Leider falsch, D lässt sich nicht zu einem Terminal auflösen, da es links nie vorkommt und darf nicht alleine stehen.");
		lang.addMCQuestion(verteilung);
		schritt = lang.newText(new Offset(40, 120, cn, AnimalScript.DIRECTION_SE),
				"1. Finde alle leeren Wörter (3) und entferne diese. Bsp.: A -> B|3 wird zu A -> B", "Schritte", null,
				schrittProps);
		schritt2 = lang.newText(new Offset(0, 5, schritt, AnimalScript.DIRECTION_SW),
				"2. Finde alle Verkettungen und entferne diese. Bsp.: A -> CD, B -> A, dann wird B -> A zu B -> CD",
				"Schritte", null, schrittProps);
		schritt3 = lang.newText(new Offset(0, 5, schritt2, AnimalScript.DIRECTION_SW),
				"3. Finde alle Terminale, die nicht alleine stehen und weise ihnen ein Nicht-Terminal zu. Bsp.: A -> Ba wird zu A -> BC und C -> a",
				"Schritte", null, schrittProps);

		src = lang.newSourceCode(new Offset(10, 5, schritt3, AnimalScript.DIRECTION_SW), "src", null, srcProp);
		// src.addCodeLine("Gehe im Term die rechte Seite durch", "1", 0, null);
		src.addCodeLine("Ist der aktuelle String länger als 1?", "1", 0, null);
		src.addCodeLine("Ja: Ist es ein Terminal?", "2", 1, null);
		src.addCodeLine("Ja: Kommt es in einem Term bereits einzeln vor?", "3", 2, null);
		src.addCodeLine("Ja: Ersetze das Terminal durch das Nicht-Terminal, in dem es bereits einzeln vorkommt", "4", 3,
				null);
		src.addCodeLine("Sonst", "5", 2, null);
		src.addCodeLine("Ersetze das Terminal durch das nächste freie Nicht-Terminal", "6", 3, null);

		schritt4 = lang.newText(new Offset(-10, 10, src, AnimalScript.DIRECTION_SW),
				"4. Finde Terme, die aus mehr als 2 Nicht-Terminalen bestehen und reduziere diese. Bsp.:A -> BCD, wird zu A -> BE und E -> CD",
				"Schritte", null, schrittProps);

		schritt3.setFont(twelve, null, null);

		for (int i = 0; i < array.getNrRows(); i++) {
			array.highlightCell(i, 0, null, null);
			if(i == 0){
			lang.nextStep("Nicht alleinstehende Terminale ersetzen");
			}else lang.nextStep();
			String[] help = splitter(array.getElement(i, 0));

			helpArray.hide();
			helpArray = lang.newStringArray(new Offset(40, 50, cn, AnimalScript.DIRECTION_SE), help, "array", null, helpArrayProps);

			for (int j = 1; j < helpArray.getLength(); j++) {
				if (am != null) {
					am.hide();
				}
				am = lang.newArrayMarker(helpArray, 0, "array", null, am.getProperties());
				am.move(j, null, null);
				lang.nextStep();
				src.highlight(0);
				lang.nextStep();
				if (helpArray.getData(j).length() > 1) {

					for (int k = 0; k < helpArray.getData(j).length(); k++) {
						src.unhighlight(0);
						src.highlight(1);
						if (hString != null) {
							hString.hide();
						}
						hString = lang.newStringArray(new Offset(40, 0, helpArray, AnimalScript.DIRECTION_NE),
								helpArray.getData(j).split(""), "array", null, helpArrayProps);
						if (stringMarker != null) {
							stringMarker.hide();
						}
						stringMarker = lang.newArrayMarker(hString, 0, "hString", null, markerStringProp);
						stringMarker.move(k, null, null);
						lang.nextStep();

						if ((int) helpArray.getData(j).charAt(k) >= 97 && (int) helpArray.getData(j).charAt(k) <= 122) {

							char alt = helpArray.getData(j).charAt(k); // Altes
																		// Terminal
							// sichern
							char[] x = helpArray.getData(j).toCharArray(); // String
																			// in
							// Char-Array
							// umwandeln

							char num = ' ';
							// Prüfe ob Terminal bereits auf rechter (linker?)
							// Seite
							// vorhanden
							boolean existing = false;
							for (int l = 0; l < array.getNrRows(); l++) {

								if (array.getElement(l, 0).charAt(5) == alt) {
									existing = true;

									// Falls vorhanden, setze num (zugehöriges
									// nicht-Terminal) auf Arrayplatz (+65)
									num = (char) array.getElement(l, 0).charAt(0);
									break;
								} else {

									// Sonst an die nächste freie Stelle
									free = nextFree(array); // (int)
															// array.getElement(array.getNrRows()
															// - 1, 0).charAt(0)
															// + 1;
									num = (char) free;
								}
								src.unhighlight(5);
								src.unhighlight(4);
							}

							src.unhighlight(1);
							src.highlight(2);
							lang.nextStep();
							if (existing) {
								src.unhighlight(2);
								src.highlight(3);
								lang.nextStep();
							} else {
								src.unhighlight(2);
								src.highlight(4);
								src.highlight(5);
								lang.nextStep();
							}
							src.unhighlight(3);
							src.unhighlight(4);
							src.unhighlight(5);
							x[k] = num; // Terminal durch nicht-Terminal
										// ersetzen
							String h = new String(x); // nicht-Terminal in
							// String umwandeln
							helpArray.put(j, h, null, null); // String in
																// Array
																// einfügen
							hString.hide();
							hString = lang.newStringArray(new Offset(40, 0, helpArray, AnimalScript.DIRECTION_NE),
									helpArray.getData(j).split(""), "array", null, helpArrayProps2);
							help[j] = helpArray.getData(j);
							// Wenn nicht-Terminal noch nicht vorhanden, hänge
							// hinten an
							array.put(i, 0, append(help), null, null);
							lang.nextStep();
							if (num == (char) free) {

								a = toExpand(array, alt, num);
								help = splitter(array.getElement(i, 0));
								array.hide();
								array = lang.newStringMatrix(new Coordinates(20, 120), a, "array", null, aProp);
								array.highlightCell(i, 0, null, null);
								array.highlightCell(array.getNrRows() - 1, 0, null, null);
								lang.nextStep();
								array.unhighlightCell(array.getNrRows() - 1, 0, null, null);

								// an die nächste
								// Position (rechte
								// Seite free) das
								// alte Terminal
								// einfügen
								free += 1;
								n += 1;
							}
						}
						src.unhighlight(2);
						hString.hide();
						stringMarker.hide();
					}
				}
				src.unhighlight(1);
			}
			if (helpArray != null) {
				helpArray.hide();
			}
			if (am != null) {
				am.hide();
			}
			src.unhighlight(0);
			array.put(i, 0, append(help), null, null);
			lang.nextStep();
			array.unhighlightCell(i, 0, null, null);
		}
		
		lang.nextStep();
		schritt4.hide();
		src.hide();
		schritt4 = lang.newText(new Offset(0, 10, schritt3, AnimalScript.DIRECTION_SW),
				"4. Finde Terme, die aus mehr als 2 Nicht-Terminalen bestehen und reduziere diese. Bsp.:A -> BCD, wird zu A -> BE und E -> CD",
				"Schritte", null, schrittProps);
		schritt.hide();
		schritt2.hide();
		schritt3.hide();
		schritt4.hide();
		am.hide();
		src.hide();
		helpArray.hide();
		hString.hide();

		reduce(array, am);
	}

	public int nextFree(StringMatrix a) {
		char[] aList = new char[a.getNrRows()];
		int frei = (int) a.getElement(a.getNrRows() - 1, 0).charAt(0) + 1;
		if (frei == 91) {
			frei = 65;
		}
		for (int i = 0; i < a.getNrRows(); i++) {
			aList[i] = a.getElement(i, 0).charAt(0);
		}
		for (int i = 0; i < a.getNrRows(); i++) {
			if (frei == (int) aList[i]) {
				// if(frei == 90){
				// frei = 65;
				// i = 0;
				// }else{
				frei = frei + 1;
				i = 0;
				// }
			}
		}
		return frei;
	}

	public String[] splitter(String a) {
		String[] rest = a.substring(5).split("\\|");
		String[] help = new String[rest.length + 1];
		help[0] = a.substring(0, 5);
		for (int j = 0; j < rest.length; j++) {
			help[j + 1] = rest[j];
		}

		return help;
	}

	public String[][] toExpand(StringMatrix array, char alt, char num) {
		String[][] b = new String[array.getNrRows() + 1][1];
		for (int i = 0; i < array.getNrRows(); i++) {
			b[i][0] = array.getElement(i, 0);
		}
		b[b.length - 1][0] = num + " -> " + String.valueOf(alt);
		return b;

	}

	// Verkürze alle Ausdrücke die rechts mehr als 2 nicht-Terminale besitzen
	public void reduce(StringMatrix array, ArrayMarker am) {
		Text schritt, schritt2, schritt3, schritt4 = null;

		SourceCode src = null;

		schritt = lang.newText(new Offset(40, 120, cn, AnimalScript.DIRECTION_SE),
				"1. Finde alle leeren Wörter (3) und entferne diese. Bsp.: A -> B|3 wird zu A -> B", "Schritte", null,
				schrittProps);
		schritt2 = lang.newText(new Offset(0, 5, schritt, AnimalScript.DIRECTION_SW),
				"2. Finde alle Verkettungen und entferne diese. Bsp.: A -> CD, B -> A, dann wird B -> A zu B -> CD",
				"Schritte", null, schrittProps);
		schritt3 = lang.newText(new Offset(0, 5, schritt2, AnimalScript.DIRECTION_SW),
				"3. Finde alle Terminale, die nicht alleine stehen und weise ihnen ein Nicht-Terminal zu. Bsp.: A -> Ba wird zu A -> BC und C -> a",
				"Schritte", null, schrittProps);
		schritt4 = lang.newText(new Offset(0, 5, schritt3, AnimalScript.DIRECTION_SW),
				"4. Finde Terme, die aus mehr als 2 Nicht-Terminalen bestehen und reduziere diese. Bsp.: A -> BCD, wird zu A -> BE und E -> CD",
				"Schritte", null, schrittProps);

		src = lang.newSourceCode(new Offset(10, 5, schritt4, AnimalScript.DIRECTION_SW), "src", null, srcProp);

		src.addCodeLine("Gehe im Term die rechte Seite durch", "1", 0, null);
		src.addCodeLine("Ist der aktuelle String länger als 2 Zeichen?", "2", 1, null);
		src.addCodeLine("Ja: Ersetze alle Nicht-Terminale bis auf das erste durch das nächste freie Nicht-Terminal",
				"3", 2, null);
		src.addCodeLine("       und füge diese zusammen mit dem Rest neu ins Array ein.", "3", 3, null);

		schritt4.setFont(twelve, null, null);

		for (int i = 0; i < array.getNrRows(); i++) {
			array.highlightCell(i, 0, null, null);
			String[] help = splitter(array.getElement(i, 0));

			helpArray.hide();
			helpArray = lang.newStringArray(new Offset(40, 50, cn, AnimalScript.DIRECTION_SE), help, "array", null, helpArrayProps);
			src.highlight(0);

			for (int j = 1; j < helpArray.getLength(); j++) {
				if (am != null) {
					am.hide();
				}
				am = lang.newArrayMarker(helpArray, 0, "array", null, am.getProperties());
				am.move(j, null, null);
				if(i==0 && j == 1){
				lang.nextStep("Nicht-Terminale reduzieren, die mit mehr als einem weiteren zusammenstehen");
				}else lang.nextStep();
				src.unhighlight(0);
				src.highlight(1);
				lang.nextStep();
				while (helpArray.getData(j).length() > 2) {
					src.unhighlight(1);
					src.highlight(2);
					src.highlight(3);
					lang.nextStep();
					// Speichere den Reststring
					free = nextFree(array); // (int)
											// array.getElement(array.getNrRows()
											// - 1, 0).charAt(0) + 1;
					String neu = (char) free + " -> "; // +
														// String.valueOf(help[j].charAt(1));
					for (int k = 1; k < helpArray.getData(j).length(); k++) {
						neu = neu + helpArray.getData(j).charAt(k);
					}
					// Ersetze den Reststring durch die nächste freie Variable

					helpArray.put(j, help[j].substring(0, 1) + (char) free, null, null);
					help[j] = helpArray.getData(j);

					// Erstelle einen neuen Arrayplatz mit dem Reststring
					String a[][] = new String[array.getNrRows() + 1][1];
					for (int l = 0; l < array.getNrRows(); l++) {
						a[l][0] = array.getElement(l, 0);
					}
					a[i][0] = append(help);
					help = splitter(a[i][0]);

					helpArray.hide();
					helpArray = lang.newStringArray(new Offset(40, 50, cn, AnimalScript.DIRECTION_SE), help, "array",
							null, helpArrayProps);

					a[a.length - 1][0] = neu.toString();
					array.hide();
					array = lang.newStringMatrix(new Coordinates(20, 120), a, "array", null, aProp);
					array.highlightCell(i, 0, null, null);
					lang.nextStep();

					free += 1;
					n += 1;
				}
				src.unhighlight(2);
				src.unhighlight(3);
			}
			src.unhighlight(1);
			array.put(i, 0, append(help), null, null);
			lang.nextStep();
			array.unhighlightCell(i, 0, null, null);
		}
		src.unhighlight(0);
		schritt.hide();
		schritt2.hide();
		schritt3.hide();
		schritt4.hide();
		am.hide();
		src.hide();
		helpArray.hide();
		hString.hide();
		array.hide();

	}

	public String[] delete(String[] help, int j) {
		String[] help2 = new String[help.length - 1];
		for (int i = 0; i < help2.length; i++) {
			if (i < j) {
				help2[i] = help[i];
			} else if (i != j) {
				help2[i] = help[i + 1];
			}
		}
		return help2;
	}

	public String append(String[] help) {
		StringBuilder sb = new StringBuilder();
		sb.append(help[0]);
		for (int j = 1; j < help.length; j++) {
			sb.append(help[j]);
			if (j < help.length - 1)
				sb.append("|");
		}

		return sb.toString();
	}

}
