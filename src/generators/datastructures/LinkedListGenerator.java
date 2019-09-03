package generators.datastructures;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class LinkedListGenerator implements Generator {

	private static final String DESCRIPTION = "Eine LinkedList ist eine verkettete Datenstruktur die eine Menge von verketteten Datensaetze beinhaltet. \n"
			+ "Diese Datensaetze, also einzelne Elemente dieser Liste, besitzen jeweils eine Referenz (ein sogenannter 'Pointer' oder 'Zeiger')  "
			+ "zu anderen Datensaetze dieser Liste. \nDieser Pointer zeigt auf das nochfolgende Element der Liste, wobei der Nachfolger-Zeiger\n"
			+ "des letzten Elementes auf den Wert NULL zeigt.\n"
			+ "Weiterhin existieren bei einer LinkedList noch ein Pointer auf das erste Element. \n"
			+ "Eine LinkedList kann demzufolge nur von vorne durchiteriert werden. \n"
			+ "Diese implementierte LinkedList ist eine unsortierte Liste. \n"
			+ "Demzufolge muss beim Hinzufuegen eines Elements die komplette Liste durchiteriert werden. \n";

	private static final String CONCLUSION = "Hinzufuegen: Im besten und schlimmsten Fall ist die Komplexitaetsklasse linear zur Laenge der Sequenz: O(n).\n"
			+ "Entfernen: Im schlimmsten Fall ist die Komplexitaetsklasse linear zur Laenge der Sequenz: O(n).\n"
			+ "Aehnliche Datenstrukturen sind Baumstrukturen. Im Gegensatz zu diesen Baeumen sind Listen jedoch linear (wie man an den Komplexitaetsklassen erkennen kann),\n"
			+ "das heisst, dass ein Element genau einen Nachfolger besitzt.";

	private static final String CODE_ADD = "public void add(T value) {\n"
			+ "\tif (first == null) {\n"
			+ "\t\tfirst = new LinkedListElement<T>(value);\n"
			+ "\t\treturn;\n" + "\t}\n" + " \n"
			+ "\tLinkedListElement<T> act = first;\n"
			+ "\tLinkedListElement<T> last = act;\n"
			+ "\twhile (act != null) {\n" + "\t\tlast = act;\n"
			+ "\t\tact = act.next;\n" + "\t}\n"
			+ "\tnewElem = new LinkedListElement<T>(value);\n"
			+ "\tlast.next = newElem\n" + "}";

	private static final String CODE_REMOVE = "public boolean remove(T value) {\n"
			+ "\tLinkedListElement<T> act = first;\n"
			+ "\tLinkedListElement<T> last = act;\n"
			+ "\twhile (act != null && act.value != value) {\n"
			+ "\t\tlast = act;\n\t\tact = act.next;\n\t}\n"
			+ "\tif (act == null) {\n\t\treturn false;\n\t}\n"
			+ "\tif (last.next == null){\n"
			+ "\t\tfirst = act.next;\n"
			+ "\t}else{\n"
			+ "\t\tlast.next = act.next;\n"
			+ "\t}\n"
			+ "\tdestroy(act);\n" + "\treturn true;\n" + "}";

	private Language lang;
	private int[] linkedListValues;
	private Polyline[] linkedListArrows;
	private Rect[] linkedListBoxes;
	private int value;
	private boolean isAdd = true;

	private LinkedList<Integer> linkedList;

	// style
	private TextProperties headerStyle;
	private TextProperties chapterStyle;
	private TextProperties textStyle;
	private SourceCodeProperties descriptionStyle;
	private SourceCodeProperties sourceCodeStyle;

	private RectProperties recProp1;
	private RectProperties recProp2;

	private PolylineProperties arrow;
	private PolylineProperties pointer1;
	private PolylineProperties pointer2;

	private Color firstElementColor = Color.RED;

	/**
	 * CONSTRUCTOR
	 */
	public LinkedListGenerator() {
		// nix
	}

	public LinkedListGenerator(Language lang) {
		this.lang = lang;
	}

	public void init() {
		lang = new AnimalScript("Linked List [DE]",
				"Ferdinand Pyttel, Michael Ries, Florian Platzer", 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		// Get Parameter from Configuration-Wizard
		linkedListValues = (int[]) primitives.get("linkedListValues");
		value = (int) primitives.get("value");
		// .....

		// #########################
		// Init Style
		// ###########################

		SourceCodeProperties descriptionSty = (SourceCodeProperties) props
				.getPropertiesByName("description");
		SourceCodeProperties sourceCode = (SourceCodeProperties) props
				.getPropertiesByName("sourceCode");
		arrow = (PolylineProperties) props.getPropertiesByName("arrow");
		pointer1 = (PolylineProperties) props.getPropertiesByName("pointerAct");
		pointer2 = (PolylineProperties) props
				.getPropertiesByName("pointerLast");

		// TEXT
		headerStyle = (TextProperties) props.getPropertiesByName("header");
		Font headerFont = (Font) headerStyle
				.get(AnimationPropertiesKeys.FONT_PROPERTY);
		headerStyle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				headerFont.getName(), Font.BOLD, 24));

		chapterStyle = (TextProperties) props.getPropertiesByName("values");
		Font valuesFont = (Font) chapterStyle
				.get(AnimationPropertiesKeys.FONT_PROPERTY);
		chapterStyle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				valuesFont.getName(), Font.PLAIN, 20));

		textStyle = (TextProperties) props.getPropertiesByName("legend");
		Font legendFont = (Font) textStyle
				.get(AnimationPropertiesKeys.FONT_PROPERTY);
		textStyle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				legendFont.getName(), Font.PLAIN, 16));

		descriptionStyle = descriptionSty;

		sourceCodeStyle = sourceCode;

		// GRAPHICS
		recProp1 = new RectProperties();
		recProp1.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		recProp1.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		recProp1.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);

		recProp2 = new RectProperties();
		recProp2.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		recProp2.set("color", chapterStyle.get("color"));

		RectProperties firstElementStyle = (RectProperties) props
				.getPropertiesByName("firstElement");
		firstElementColor = (Color)firstElementStyle.get("color");

		// GENERATE LINKEDLIST
		Integer[] tmp = new Integer[linkedListValues.length];
		for (int i = 0; i < linkedListValues.length; i++) {
			tmp[i] = linkedListValues[i];
		}
		linkedList = new LinkedList<Integer>(tmp);
		if (value < 0) {
			isAdd = false;
			value = Math.abs(value);
		}

		startAnimLinkedList();

		return lang.toString();
	}

	public String getName() {
		return "Linked List [DE]";
	}

	public String getAlgorithmName() {
		return "Linked List";
	}

	public String getAnimationAuthor() {
		return "Ferdinand Pyttel, Michael Ries, Florian Platzer";
	}

	public String getDescription() {
		return DESCRIPTION
				+ "<br><p>Bei dieser Animation gilt: <br> <b>Positive Werte</b> werden der Liste hinzugef&uuml;gt,<br> <b>negative Werte</b> werden aus der Liste gel&ouml;scht.</p>";
	}

	public String getCodeExample() {
		return "Code Beispiel f&uuml;r das Einf&uuml;gen eines Elements:\n\n"
				+ CODE_ADD;
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_DATA_STRUCTURE);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	// #################################################
	// STEP by STEP
	// #################################################

	private void startAnimLinkedList() {
		// STEP 1
		// ///////////////////////////////////////////////
		Text title = lang.newText(new Coordinates(30, 30), "Linked List",
				"title", null, headerStyle);
		Rect rectBackGroundTitle = lang.newRect(new Offset(-10, -10, title,
				AnimalScript.DIRECTION_NW), new Offset(10, 10, title,
				AnimalScript.DIRECTION_SE), "rectBackGroundTitle", null,
				recProp1);

		SourceCode description = lang.newSourceCode(new Coordinates(30, 100),
				"description", null, descriptionStyle);

		setTextToSourceCode(description, DESCRIPTION);

		lang.nextStep();

		// cleanup
		lang.hideAllPrimitives();

		if (isAdd) {
			// STEP 2.Add
			// ///////////////////////////////////////////////

			// set title
			Text titleAdd = lang.newText(new Coordinates(30, 30),
					"Linked List - Hinzufuegen", "titleAdd", null, headerStyle);
			Rect rectBackGroundTitleAdd = lang.newRect(new Offset(-10, -10,
					titleAdd, AnimalScript.DIRECTION_NW), new Offset(10, 10,
					titleAdd, AnimalScript.DIRECTION_SE),
					"rectBackGroundTitle", null, recProp1);

			// set source code
			SourceCode codeAdd = lang.newSourceCode(new Coordinates(30, 230),
					"codeAdd", null, sourceCodeStyle);
			setTextToSourceCode(codeAdd, CODE_ADD);

			// set description text
			SourceCode descText = lang.newSourceCode(new Coordinates(400, 230),
					"descAdd", null, descriptionStyle);

			// set seperator
			lang.newPolyline(new Coordinates[] { new Coordinates(380, 230),
					new Coordinates(380, 550) }, "seperator", null);

			// create linked list graphic
			linkedListArrows = new Polyline[linkedListValues.length];
			linkedListBoxes = new Rect[linkedListValues.length + 1];
			int offset = 100;
			int y = 100;
			int x = 30;
			Rect lastBox = null;
			for (int i = 0; i < linkedListValues.length; i++) {
				Rect box = createBox(x + (offset * i), y, linkedListValues[i]);
				linkedListBoxes[i] = box;
				if (lastBox == null) {
					lastBox = box;
				} else {
					linkedListArrows[i - 1] = createArrow(lastBox, box, arrow);
					lastBox = box;
				}
			}
			// add null pointer
			linkedListBoxes[linkedListValues.length] = createBox(x
					+ (offset * linkedListValues.length), y, "null");
			linkedListArrows[linkedListValues.length - 1] = createArrow(
					lastBox, linkedListBoxes[linkedListValues.length], arrow);

			lang.nextStep("Start Initialisierung");

			// STEP 3.Add
			// ///////////////////////////////////////////////

			// select first code line
			codeAdd.highlight(1);
			// select first element
			linkedListBoxes[0].changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, firstElementColor,
					null, null);

			// add firstBox to legend
			createBox(600, 25, "  ").changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, firstElementColor,
					null, null);
			Text legendLastBox1 = lang.newText(new Coordinates(640, 15),
					"fist", "legendFirstBox1", null, textStyle);
			Text legendLastBox2 = lang.newText(new Coordinates(640, 40),
					"element", "legendFirstBox2", null, textStyle);

			// set description
			setTextToSourceCode(descText,
					"Das erste Element der LinkedList 'fist' ist ungleich null.");

			lang.nextStep();

			// STEP 4.Add
			// ///////////////////////////////////////////////
			codeAdd.unhighlight(1);

			codeAdd.highlight(6);
			descText.hide();
			descText = createSourceCode(
					400,
					230,
					"Aktuellen Zeiger (act) mit dem ersten Element (first)\naus der LinkedList initialisieren.");
			Polyline pointerAct = createArrowTo(linkedListBoxes[0], 40, -10,
					pointer1);

			// add pointer to legend
			Polyline legendAct = createArrow(500, 30, 530, 30, pointer1);
			Text legendActText = lang.newText(new Coordinates(470, 20), "act",
					"legendActText", null, textStyle);

			lang.nextStep();

			// STEP 5.Add
			// ///////////////////////////////////////////////
			codeAdd.unhighlight(6);
			codeAdd.highlight(7);
			descText.hide();
			descText = createSourceCode(
					400,
					230,
					"Zeiger auf vorheriges Element (last) mit dem aktuellen Element (act) initialisieren.");
			Polyline pointerLast = createArrowTo(linkedListBoxes[0], 40, 10,
					pointer2);

			// add pointer to legend
			Polyline legendLast = createArrow(500, 50, 530, 50, pointer2);
			Text legendActLast = lang.newText(new Coordinates(470, 40), "last",
					"legendActLast", null, textStyle);

			lang.nextStep("Ende Initialisierung");

			// STEP 6.Add
			// ///////////////////////////////////////////////
			int listIndex = 0;
			for (int i = 0; i < linkedListValues.length; i++) {
				codeAdd.unhighlight(10);
				codeAdd.unhighlight(7);
				codeAdd.highlight(8);
				descText.hide();
				descText = createSourceCode(400, 230,
						"Solange aktuelles Element (act) ungleich null ist, wird fortgefahren.");

				lang.nextStep();

				codeAdd.unhighlight(8);
				codeAdd.highlight(9);
				descText.hide();
				descText = createSourceCode(400, 230,
						"Das aktuelle Element wird als vorheriges Element gespeichert.");
				pointerLast.hide();
				pointerLast = createArrowTo(linkedListBoxes[i], 40, 10,
						pointer2);

				lang.nextStep();

				codeAdd.unhighlight(9);
				codeAdd.highlight(10);
				descText.hide();
				descText = createSourceCode(400, 230,
						"Das neue aktuelle Element ist der Nachfolger des alten aktuellen Elements.");
				pointerAct.hide();
				pointerAct = createArrowTo(linkedListBoxes[i + 1], 40, -10,
						pointer1);
				listIndex++;
				lang.nextStep();
			}

			// STEP 7.Add
			// ///////////////////////////////////////////////
			codeAdd.unhighlight(10);
			codeAdd.highlight(8);
			descText.hide();
			descText = createSourceCode(400, 230,
					"Aktuelles Element (act) ist null. Abbruch der Schleife.");

			lang.nextStep();

			// STEP 8.Add
			// ///////////////////////////////////////////////
			codeAdd.unhighlight(8);
			codeAdd.highlight(12);
			descText.hide();
			descText = createSourceCode(400, 230, "Neues Element wird erzeugt.");
			pointerAct.hide();

			Rect valueBox = createBox(x + (offset * linkedListValues.length),
					y + 60, value);
			Rect nullBox = createBox(x
					+ (offset * (linkedListValues.length + 1)), y + 60, "null");
			Polyline newPointer = createArrow(valueBox, nullBox, arrow);

			lang.nextStep("Neues Element erstellen");

			// STEP 9.Add
			// ///////////////////////////////////////////////
			codeAdd.unhighlight(12);
			codeAdd.highlight(13);
			descText.hide();
			descText = createSourceCode(400, 230,
					"Setze das neue Element als Nachfolger des 'last-Elements'.");

			linkedListArrows[listIndex - 1].hide();
			linkedListArrows[listIndex - 1] = createArrow(
					linkedListBoxes[listIndex - 1], valueBox, arrow);

			lang.nextStep("Neues Element der Liste hinzufuegen");
			// STEP 9.Add
			// ///////////////////////////////////////////////

			codeAdd.unhighlight(13);
			descText.hide();
			descText = createSourceCode(400, 230,
					"Das neue Element wurde erfolgreich der Liste hinzugefuegt.");

			((Offset) (linkedListBoxes[linkedListValues.length].getUpperLeft()))
					.getRef().hide();
			linkedListBoxes[linkedListValues.length].hide();
			valueBox.hide();
			((Offset) (valueBox.getUpperLeft())).getRef().hide();
			nullBox.hide();
			((Offset) (nullBox.getUpperLeft())).getRef().hide();
			newPointer.hide();

			valueBox = createBox(x + (offset * linkedListValues.length), y,
					value);
			nullBox = createBox(x + (offset * (linkedListValues.length + 1)),
					y, "null");
			newPointer = createArrow(valueBox, nullBox, arrow);

			linkedListArrows[listIndex - 1].hide();
			linkedListArrows[listIndex - 1] = createArrow(
					linkedListBoxes[listIndex - 1], valueBox, arrow);

			lang.nextStep("Neues Element der Liste hinzugefuegt");

		} else {
			// STEP 2.Remove
			// ///////////////////////////////////////////////
			// set title
			Text titleRemove = lang
					.newText(new Coordinates(30, 30),
							"Linked List - Entfernen", "titleRemove", null,
							headerStyle);
			Rect rectBackGroundTitleRemove = lang.newRect(new Offset(-10, -10,
					titleRemove, AnimalScript.DIRECTION_NW), new Offset(10, 10,
					titleRemove, AnimalScript.DIRECTION_SE),
					"rectBackGroundTitle", null, recProp1);

			// set source code
			SourceCode codeRemove = lang.newSourceCode(
					new Coordinates(30, 230), "codeRemove", null,
					sourceCodeStyle);
			setTextToSourceCode(codeRemove, CODE_REMOVE);

			// set description text
			SourceCode descText = lang.newSourceCode(new Coordinates(400, 230),
					"descRemove", null, descriptionStyle);

			// set seperator
			lang.newPolyline(new Coordinates[] { new Coordinates(380, 230),
					new Coordinates(380, 520) }, "seperator", null);

			// create linked list graphic
			linkedListArrows = new Polyline[linkedListValues.length];
			linkedListBoxes = new Rect[linkedListValues.length + 1];
			int offset = 100;
			int y = 100;
			int x = 30;
			Rect lastBox = null;
			for (int i = 0; i < linkedListValues.length; i++) {
				Rect box = createBox(x + (offset * i), y, linkedListValues[i]);
				linkedListBoxes[i] = box;
				if (lastBox == null) {
					lastBox = box;
				} else {
					linkedListArrows[i - 1] = createArrow(lastBox, box, arrow);
					lastBox = box;
				}
			}
			// add null pointer
			linkedListBoxes[linkedListValues.length] = createBox(x
					+ (offset * linkedListValues.length), y, "null");
			linkedListArrows[linkedListValues.length - 1] = createArrow(
					lastBox, linkedListBoxes[linkedListValues.length], arrow);

			lang.nextStep("Start Initialisierung");

			// STEP 3.Remove
			// ///////////////////////////////////////////////

			// add firstBox to legend
			createBox(600, 25, "  ").changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, firstElementColor,
					null, null);
			Text legendFirstBox1 = lang.newText(new Coordinates(640, 15),
					"first", "legendFirstBox1", null, textStyle);
			Text legendFirstBox2 = lang.newText(new Coordinates(640, 40),
					"element", "legendFirstBox2", null, textStyle);

			linkedListBoxes[0].changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, firstElementColor,
					null, null);

			codeRemove.highlight(1);
			descText.hide();
			descText = createSourceCode(
					400,
					230,
					"Aktuellen Zeiger (act) mit dem ersten Element (first)\naus der LinkedList initialisieren.");
			Polyline pointerAct = createArrowTo(linkedListBoxes[0], 40, -10,
					pointer1);

			// add pointer to legend
			Polyline legendAct = createArrow(500, 30, 530, 30, pointer1);
			Text legendActText = lang.newText(new Coordinates(470, 20), "act",
					"legendActText", null, textStyle);

			lang.nextStep();

			// STEP 4.Remove
			// ///////////////////////////////////////////////
			codeRemove.unhighlight(1);
			codeRemove.highlight(2);
			descText.hide();
			descText = createSourceCode(
					400,
					230,
					"Zeiger auf vorheriges Element (last) mit dem aktuellen Element (act) initialisieren.");
			Polyline pointerLast = createArrowTo(linkedListBoxes[0], 40, 10,
					pointer2);

			// add pointer to legend
			Polyline legendLast = createArrow(500, 50, 530, 50, pointer2);
			Text legendActLast = lang.newText(new Coordinates(470, 40), "last",
					"legendActLast", null, textStyle);

			lang.nextStep("Ende Initialisierung");

			// STEP 5.Remove
			// ///////////////////////////////////////////////
			boolean isValueMatch = false;
			int listIndex = 0;
			for (int i = 0; i < linkedListValues.length; i++) {
				if (linkedListValues[i] == value) {
					isValueMatch = true;
					break;
				}

				codeRemove.unhighlight(2);
				codeRemove.unhighlight(5);
				codeRemove.highlight(3);
				descText.hide();
				descText = createSourceCode(
						400,
						230,
						"Solange aktuelles Element (act) ungleich null ist und\nder Wert des aktuellen Elementes ungleich dem zu loeschenden Wertes ist, wird fortgefahren.");

				lang.nextStep();

				codeRemove.unhighlight(3);
				codeRemove.highlight(4);
				descText.hide();
				descText = createSourceCode(400, 230,
						"Das aktuelle Element wird als vorheriges Element gespeichert.");
				pointerLast.hide();
				pointerLast = createArrowTo(linkedListBoxes[i], 40, 10,
						pointer2);

				lang.nextStep();

				codeRemove.unhighlight(4);
				codeRemove.highlight(5);
				descText.hide();
				descText = createSourceCode(400, 230,
						"Das neue aktuelle Element ist der Nachfolger des alten aktuellen Elements.");
				pointerAct.hide();
				pointerAct = createArrowTo(linkedListBoxes[i + 1], 40, -10,
						pointer1);

				listIndex++;
				lang.nextStep();
			}

			// STEP 6.Remove
			// ///////////////////////////////////////////////
			codeRemove.unhighlight(2);
			codeRemove.unhighlight(5);
			codeRemove.highlight(3);
			descText.hide();
			if (isValueMatch) {
				// found value to remove
				descText = createSourceCode(
						400,
						230,
						"Der Wert des aktuellen Elementes entspricht dem zu loeschenden Wert.\nSchleife wird beendet.");

				lang.nextStep("Wert in Liste gefunden");

				// STEP 7.Remove
				// ///////////////////////////////////////////////

				codeRemove.unhighlight(3);
				codeRemove.highlight(7);
				descText.hide();
				// found value to remove
				descText = createSourceCode(440, 230,
						"Aktuelles Element ist ungleich Null.");

				linkedListBoxes[listIndex].hide();
				((Offset) linkedListBoxes[listIndex].getUpperLeft()).getRef()
						.hide();

				// move down the box which will be delete
				Rect actBox = createBox(x + (offset * (listIndex)), y + 60,
						((Text) ((Offset) linkedListBoxes[listIndex]
								.getUpperLeft()).getRef()).getText());
				linkedListBoxes[listIndex] = actBox;

				if (listIndex != 0) {
					linkedListArrows[listIndex - 1].hide();
					linkedListArrows[listIndex - 1] = createArrow(
							linkedListBoxes[listIndex - 1], actBox, arrow);
				} else {
					pointerLast.hide();
					pointerLast = createArrowTo(linkedListBoxes[listIndex], 40,
							10, pointer2);
					actBox.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
							firstElementColor, null, null);
				}
				linkedListArrows[listIndex].hide();
				linkedListArrows[listIndex] = createArrow(actBox,
						linkedListBoxes[listIndex + 1], arrow);

				pointerAct.hide();
				pointerAct = createArrowTo(linkedListBoxes[listIndex], 40, -10,
						pointer1);

				lang.nextStep("Wert aus Liste loeschen");

				if (listIndex != 0) {
					// STEP 8.Remove
					// ///////////////////////////////////////////////

					codeRemove.unhighlight(7);
					codeRemove.highlight(10);
					descText.hide();
					descText = createSourceCode(
							440,
							230,
							"last.next ist ungleich Null. D.h., dass die Liste hat mehr als ein Element besitzt.");
					lang.nextStep();

					// STEP 9.Remove
					// ///////////////////////////////////////////////

					codeRemove.unhighlight(7);
					codeRemove.highlight(13);
					descText.hide();
					descText = createSourceCode(
							440,
							230,
							"Setzte als Nachfolger des last-Pointers den Nachfolger des aktuellen Elements.");

					linkedListArrows[listIndex - 1].hide();
					linkedListArrows[listIndex - 1] = createArrow(
							linkedListBoxes[listIndex - 1],
							linkedListBoxes[listIndex + 1], arrow);
					lang.nextStep();
				} else {
					// STEP 8.Remove
					// ///////////////////////////////////////////////

					codeRemove.unhighlight(7);
					codeRemove.highlight(10);
					descText.hide();
					descText = createSourceCode(440, 230,
							"last.next ist Null. D.h., dass die Liste nur das eine Element besitzt.");
					lang.nextStep();

					// STEP 9.Remove
					// ///////////////////////////////////////////////

					codeRemove.unhighlight(10);
					codeRemove.highlight(11);
					descText.hide();
					descText = createSourceCode(440, 230,
							"Setze als erstes Element 'first' den Nachfolger des aktuellen Elements.");

					linkedListBoxes[0].changeColor(
							AnimationPropertiesKeys.COLOR_PROPERTY,
							Color.BLACK, null, null);
					linkedListBoxes[1].changeColor(
							AnimationPropertiesKeys.COLOR_PROPERTY, firstElementColor,
							null, null);
					lang.nextStep();

				}
				// STEP 10.Remove
				// ///////////////////////////////////////////////
				codeRemove.unhighlight(11);
				codeRemove.unhighlight(13);
				codeRemove.highlight(15);
				descText.hide();
				descText = createSourceCode(
						440,
						230,
						"Kein Pointer der Liste zeigt mehr auf das zu loeschende Element. Das Element kann geloescht werden.");

				// delete the element from display
				linkedListBoxes[listIndex].hide();
				((Offset) linkedListBoxes[listIndex].getUpperLeft()).getRef()
						.hide();
				linkedListArrows[listIndex].hide();
				pointerAct.hide();

				if (listIndex == 0) {
					pointerLast.hide();
				}

				lang.nextStep();

				// STEP 11.Remove
				// ///////////////////////////////////////////////

				codeRemove.unhighlight(15);
				codeRemove.highlight(16);
				descText.hide();
				if (listIndex == 0) {
					descText = createSourceCode(
							440,
							230,
							"Das zu loeschende Element wurde erfolgreich aus der Liste geloescht.\nDies war das einzige Element der LinkedList.\nDie Liste ist nun leer.");
				} else {
					descText = createSourceCode(440, 230,
							"Das zu loeschende Element wurde erfolgreich aus der Liste geloescht.");
				}
				// delete the element from display
				linkedListBoxes[listIndex].hide();
				((Offset) linkedListBoxes[listIndex].getUpperLeft()).getRef()
						.hide();
				linkedListArrows[listIndex].hide();
				pointerAct.hide();
				lang.nextStep("Wert aus der Liste geloescht");

			} else {
				// found no value
				descText.hide();
				descText = createSourceCode(400, 230,
						"Die LinkedList hat keine Elemente mehr und somit wird die Schleife beendet.");

				lang.nextStep();

				// STEP 7.Remove
				// ///////////////////////////////////////////////
				codeRemove.unhighlight(3);
				codeRemove.highlight(7);
				codeRemove.highlight(8);
				codeRemove.highlight(9);
				descText.hide();
				descText = createSourceCode(
						400,
						230,
						"Das aktuelle Element ist null. D.h., dass die LinkedList \ndas gesuchte Element nicht enthaelt.");
				lang.nextStep("Wert nicht in der Liste gefunden");
			}
		}
		// Last STEP
		// ///////////////////////////////////////////////
		lang.hideAllPrimitives();
		Text titleLast = lang.newText(new Coordinates(30, 30), "Linked List",
				"title", null, headerStyle);

		lang.newRect(
				new Offset(-10, -10, titleLast, AnimalScript.DIRECTION_NW),
				new Offset(10, 10, title, AnimalScript.DIRECTION_SE),
				"rectBackGroundTitle", null, recProp1);

		description = lang.newSourceCode(new Coordinates(30, 100),
				"description", null, descriptionStyle);

		setTextToSourceCode(description, CONCLUSION);

		lang.nextStep();
	}

	// just needed here
	private int arrowCounter = 0;

	private Polyline createArrow(int x1, int y1, int x2, int y2,
			PolylineProperties prop) {
		Coordinates[] co = { new Coordinates(x1, y1), new Coordinates(x2, y2) };
		PolylineProperties arrowProp = prop;
		arrowProp.set("fwArrow", true);
		return lang.newPolyline(co, "arrow-" + (++arrowCounter), null,
				arrowProp);
	}

	private Polyline createArrow(Rect from, int x2, int y2,
			PolylineProperties prop) {
		Offset offsetUpperLeft = (Offset) from.getUpperLeft();
		Offset offsetLowerRight = (Offset) from.getLowerRight();
		Coordinates upperLeft = (Coordinates) ((Text) offsetUpperLeft.getRef())
				.getUpperLeft();
		FontMetrics metrics = new Canvas().getFontMetrics((Font) chapterStyle
				.get("font"));
		int textWidth = metrics.stringWidth(((Text) offsetUpperLeft.getRef())
				.getText());
		int textHeight = metrics.getMaxAscent();
		int x = Math.abs(offsetLowerRight.getX()) + upperLeft.getX()
				+ textWidth;
		int y = upperLeft.getY() + (textHeight / 2);
		return createArrow(x, y, x2, y2, prop);
	}

	private Polyline createArrow(Rect from, Rect to, PolylineProperties prop) {
		return createArrow(from, to, 0, prop);
	}

	private Polyline createArrow(Rect from, Rect to, int offset,
			PolylineProperties prop) {
		Offset offsetUpperLeft = (Offset) to.getUpperLeft();
		Offset offsetLowerRight = (Offset) to.getLowerRight();
		Coordinates upperLeft = (Coordinates) ((Text) offsetUpperLeft.getRef())
				.getUpperLeft();
		FontMetrics metrics = new Canvas().getFontMetrics((Font) chapterStyle
				.get("font"));
		int textHeight = metrics.getMaxAscent();
		int x2 = upperLeft.getX() - Math.abs(offsetLowerRight.getX());
		int y2 = upperLeft.getY() + (textHeight / 2) + offset;
		return createArrow(from, x2, y2, prop);
	}

	private Polyline createArrowTo(Rect toBottom, int length, int offsetX,
			PolylineProperties prop) {
		Offset offsetUpperLeft = (Offset) toBottom.getUpperLeft();
		Offset offsetLowerRight = (Offset) toBottom.getLowerRight();
		Coordinates upperLeft = (Coordinates) ((Text) offsetUpperLeft.getRef())
				.getUpperLeft();
		FontMetrics metrics = new Canvas().getFontMetrics((Font) chapterStyle
				.get("font"));
		int textWidth = metrics.stringWidth(((Text) offsetUpperLeft.getRef())
				.getText());
		int textHeight = metrics.getMaxAscent();
		int x2 = upperLeft.getX() + (textWidth / 2) + offsetX;
		int y2 = upperLeft.getY() + textHeight
				+ Math.abs(offsetLowerRight.getY());
		int x = x2;
		int y = y2 + length;
		return createArrow(x, y, x2, y2, prop);
	}

	// just needed here
	private int boxCounter = 0;

	private Rect createBox(int x, int y, String value) {
		Text textValue = lang.newText(new Coordinates(x, y), value, "box-"
				+ (++boxCounter), null, chapterStyle);
		return lang
				.newRect(new Offset(-10, -10, textValue,
						AnimalScript.DIRECTION_NW), new Offset(10, 10,
						textValue, AnimalScript.DIRECTION_SE), "rec-"
						+ boxCounter, null, recProp2);
	}

	private Rect createBox(int x, int y, int value) {
		return createBox(x, y, Integer.toString(value));
	}

	private void removeBox(int index) {
		((Offset) (linkedListBoxes[index].getUpperLeft())).getRef().hide();
		linkedListBoxes[index].hide();
		// TODO cleanup array
	}

	private void removeBoxWithArrow(int index) {
		removeBox(index);
		linkedListArrows[index].hide();
		// TODO cleanup array
	}

	// just needed here
	private int codeCounter = 0;

	private SourceCode createSourceCode(int x, int y, String code) {
		SourceCode scode = lang.newSourceCode(new Coordinates(x, y),
				"sourcecode-" + (++codeCounter), null, descriptionStyle);
		setTextToSourceCode(scode, code);
		return scode;
	}

	private void setTextToSourceCode(SourceCode sourceCode, String text) {
		String[] lines = text.split("\n");
		for (String line : lines) {
			int tabs = line.split("\t").length - 1;
			sourceCode.addCodeLine(line, null, tabs, null);
		}
	}

	// #################################################
	// inner class
	// #################################################
	public class LinkedList<T> {
		private LinkedListElement<T> first;

		public LinkedList() {/* nix */
		}

		public LinkedList(T[] initValues) {
			for (T t : initValues) {
				add(t);
			}
		}

		public void add(T value) {
			if (first == null) {
				first = new LinkedListElement<T>(value);
				return;
			}

			LinkedListElement<T> act = first;
			LinkedListElement<T> latest = act;
			while (act != null) {
				latest = act;
				act = act.next;
			}
			latest.next = new LinkedListElement<T>(value);
		}

		public T get(int index) {
			int i = 0;
			LinkedListElement<T> act = first;
			while (act != null && i != index) {
				act = act.next;
				i++;
			}
			return act == null ? null : act.value;
		}

		public int size() {
			int i = 0;
			LinkedListElement<T> act = first;
			while (act != null) {
				act = act.next;
				i++;
			}
			return i;
		}

		public boolean remove(int index) {
			int i = 0;
			LinkedListElement<T> act = first;
			LinkedListElement<T> latest = act;
			while (act != null && i != index) {
				latest = act;
				act = act.next;
				i++;
			}
			if (act == null) {
				return false;
			}
			if (first == act) {
				first = act.next;
				return true;
			}
			latest.next = act.next;
			return true;
		}

		// delete object by reference
		public boolean remove(T value) {
			LinkedListElement<T> act = first;
			LinkedListElement<T> latest = act;
			while (act != null && act.value != value) {
				latest = act;
				act = act.next;
			}
			if (act == null) {
				return false;
			}
			latest.next = act.next;
			return true;
		}

		public String toString() {
			String str = "";
			for (int i = 0; i < size(); i++) {
				str += get(i) + ", ";
			}
			return str;
		}

		// inner inner class
		private class LinkedListElement<T> {
			private T value;
			private LinkedListElement<T> next;

			public LinkedListElement(T value) {
				this.value = value;
			}
		}
	}

}