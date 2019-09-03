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

public class DoublyLinkedListGenerator implements Generator {

	private static final String DESCRIPTION = "Eine DoublyLinkedList ist eine verkettete Datenstruktur die eine Menge von verketteten Datensaetze beinhaltet. \n"
			+ "Diese Datensaetze, also einzelne Elemente dieser Liste, besitzen jeweils zwei Referenzen (sogenannte 'Pointer' oder 'Zeiger')  "
			+ "zu anderen Datensaetze dieser Liste. \nDer eine Pointer zeigt auf das vorherige Element der Liste, der zweite Pointer "
			+ "auf das naechste Element der Liste.\n"
			+ "Der Vorgaenger-Zeiger des ersten und der Nachfolger-Zeiger des letzten Elementes zeigen auf den Wert NULL.\n"
			+ "Weiterhin existieren bei einer DoublyLinkedList noch ein Pointer auf das erste und ein Pointer aus das letzte Element der Liste. \n"
			+ "Somit kann die Liste sowohl von vorne als auch von hinten iteriert werden. Dies ist insbesondere fuer das Einfuegen von grosser Bedeutung. \n"
			+ "Diese implementierte DoublyLinkedList ist eine unsortierte Liste. \n"
			+ "Demzufolge kann beim Hinzufuegen eines Elements mithilfe des 'last'-Pointers ein Element sehr schnell am Ende der Liste hinzugefuegt werden. \n"
			+ "Fuer das Loeschen eines Elements muss allerdings die Liste weiterhin von vorne durchiteriert werden (da es eine unsortierte Liste ist).";

	private static final String CONCLUSION = "Hinzufuegen: Komplexitaetsklasse ist immer O(1), da das neue Element hinten hinzugefuegt wird und diese Stelle bekannt ist.\n"
			+ "Entfernen: Im schlimmsten Fall ist die Komplexitaetsklasse linear zur Laenge der Sequenz: O(n).\n"
			+ "Aehnliche Datenstrukturen sind Baumstrukturen. Im Gegensatz zu diesen Baeumen sind Listen jedoch linear (wie man an den Komplexitaetsklassen erkennen kann)"
			+ ", \ndas heisst, dass ein Element genau einen Nachfolger bzw. einen Vorgaenger besitzt.";

	private static final String CODE_ADD = "public void add(T value) {\n"
			+ "\tif (last == null) {\n"
			+ "\t\tlast = new DoublyLinkedListElement<T>(value);\n"
			+ "\t\treturn;\n" + "\t}\n"
			+ "\tDoublyLinkedListElement<T> act = last;\n"
			+ "\tnewElem = new DoublyLinkedListElement<T>(value);\n"
			+ "\tnewElem.last = act;\n" + "\tact.next = newElem;\n" + "}\n";

	private static final String CODE_REMOVE = "public boolean remove(T value) {\n"
			+ "\tif (first == null) {\n"
			+ "\t\treturn false;\n"
			+ "\t}\n"
			+ "\tDoublyLinkedListElement<T> act = first;\n"
			+ "\twhile (act != null && act.value != value) {\n"
			+ "\t\tact = act.next;\n\t}\n"
			+ "\tif (act == null) {\n"
			+ "\t\treturn false;\n"
			+ "\t}\n"
			+ "\tif(act.prev == null)\n"
			+ "\t\tfirst  = act.next\n" + "\telse\n" + "\t...\n";

	private static final String CODE_REMOVE2 = "...\n"
			+ "\tif(act.prev == null)\n" + "\t\tfirst  = act.next\n"
			+ "\telse\n" + "\t\tact.prev.next = act.next\n"
			+ "\tif(act.next != null)\n" + "\t\tact.next.prev = act.prev\n"
			+ "\tdestroy(act);\n" + "\treturn true;\n}";

	private Language lang;
	private int[] doublyLinkedListValues;
	private Polyline[] doublyLinkedListArrowsForward;
	private Polyline[] doublyLinkedListArrowsBackward;
	private Rect[] doublyLinkedListBoxes;
	private int value;
	private boolean isAdd = true;

	private DoublyLinkedList<Integer> linkedList;

	// style
	private TextProperties headerStyle;
	private TextProperties chapterStyle;
	private TextProperties textStyle;
	private SourceCodeProperties descriptionStyle;
	private SourceCodeProperties sourceCodeStyle;

	private RectProperties recProp1;
	private RectProperties recProp2;

	// ####################################
	// ############ Properties ############
	// ####################################
	Color Color_first_last_legendBox = Color.RED;
	Color Color_nextPointer = Color.BLUE;
	Color Color_actPointer = Color.GREEN;
	Color Color_forwardPointer = Color.BLACK;
	Color Color_backwardPointer = Color.BLACK;
	Color Color_defaultBox = Color.BLACK;

	
	private boolean preNull = false;
	private boolean postNull = false;
	/**
	 * CONSTRUCTOR
	 */
	public DoublyLinkedListGenerator() {
		// nix
	}

	public DoublyLinkedListGenerator(Language lang) {
		this.lang = lang;
	}

	public void init() {
		lang = new AnimalScript("Doubly Linked List [DE]",
				"Ferdinand Pyttel, Michael Ries, Florian Platzer", 800, 600);
		lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		// Get Parameter from Configuration-Wizard
		doublyLinkedListValues = (int[]) primitives
				.get("doublyLinkedListValues");
		value = (int) primitives.get("value");
		// .....

		// ###########################
		// Init Style
		// ###########################

		// TEXT
		headerStyle = (TextProperties) props
				.getPropertiesByName("header");
		Font headerFont = (Font)headerStyle.get(AnimationPropertiesKeys.FONT_PROPERTY);
		headerStyle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				headerFont.getName(), Font.BOLD, 24));

		chapterStyle = (TextProperties) props
				.getPropertiesByName("values");
		Font valuesFont = (Font)chapterStyle.get(AnimationPropertiesKeys.FONT_PROPERTY);
		chapterStyle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				valuesFont.getName(), Font.PLAIN, 20));

		textStyle = (TextProperties) props
				.getPropertiesByName("legend");
		Font legendFont = (Font)textStyle.get(AnimationPropertiesKeys.FONT_PROPERTY);
		textStyle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				legendFont.getName(), Font.PLAIN, 16));

		descriptionStyle = (SourceCodeProperties) props
				.getPropertiesByName("description");

		sourceCodeStyle = (SourceCodeProperties) props
				.getPropertiesByName("sourceCode");

		// ARROWS & POINTER
		PolylineProperties nextPointerStyle = (PolylineProperties) props
				.getPropertiesByName("nextPointer");
		Color_nextPointer = (Color)nextPointerStyle.get("color");
		PolylineProperties actPointerStyle = (PolylineProperties) props
				.getPropertiesByName("actPointer");
		Color_actPointer = (Color)actPointerStyle.get("color");
		PolylineProperties forwardPointerStyle = (PolylineProperties) props
				.getPropertiesByName("forwardPointer");
		Color_forwardPointer = (Color)forwardPointerStyle.get("color");
		PolylineProperties backwardPointerStyle = (PolylineProperties) props
				.getPropertiesByName("backwardPointer");
		Color_backwardPointer = (Color)backwardPointerStyle.get("color");
		Color_defaultBox = (Color)chapterStyle.get("color");
		
		RectProperties firstLastElementStyle = (RectProperties) props
				.getPropertiesByName("firstLastElement");
		Color_first_last_legendBox = (Color)firstLastElementStyle.get("color");

		// GRAPHICS
		recProp1 = new RectProperties();
		recProp1.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		recProp1.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		recProp1.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);

		recProp2 = new RectProperties();
		recProp2.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		// GENERATE DOUBLYLINKEDLIST
		Integer[] tmp = new Integer[doublyLinkedListValues.length];
		for (int i = 0; i < doublyLinkedListValues.length; i++) {
			tmp[i] = doublyLinkedListValues[i];
		}
		linkedList = new DoublyLinkedList<Integer>(tmp);
		if (value < 0) {
			isAdd = false;
			value = Math.abs(value);
		}

		startAnimLinkedList();

		return lang.toString();
	}

	public String getName() {
		return "Doubly Linked List [DE]";
	}

	public String getAlgorithmName() {
		return "Doubly Linked List";
	}

	public String getAnimationAuthor() {
		return "Ferdinand Pyttel, Michael Ries, Florian Platzer";
	}

	public String getDescription() {
		return "Eine DoublyLinkedList ist eine verkettete Datenstruktur die eine Menge von verketteten Datens&auml;tze beinhaltet. Diese "
				+ "Datens&auml;tze, also einzelne Elemente dieser Liste, besitzen jeweils zwei Referenzen (sogenannte 'Pointer')  "
				+ "zu anderen Datens&auml;tze dieser Liste. Der eine Pointer zeigt auf das vorherige Element der Liste, der zweite Pointer "
				+ "auf das n&auml;chste Element der Liste. <br>"
				+ " Der gro&szlig;e Vorteil einer DoublyLinkedList ist, dass diese Liste (im Vergleich zu einer einfachen LinkedList) zus&auml;tzlich zu einem Pointer auf das erste Element der Liste, einen weiteren Pointer auf das letzte Element dieser Liste besitzt."
				+ " Dadurch kann die DoublyLinkedList sowohl von vorne als auch von hinten durchiteriert werden.<br>"
				+ "<p>Bei dieser Animation gilt: <br> <b>Positive Werte</b> werden der Liste hinzugef&uuml;gt,<br> <b>negative Werte</b> werden aus der Liste gel&ouml;scht.</p>";
	}

	public String getCodeExample() {
		return "Code Beispiel f&uuml;r das L&ouml;schen eines Elements:\n\n"
				+ CODE_REMOVE.substring(0, CODE_REMOVE.length() - 53)
				+ CODE_REMOVE2.substring(3, CODE_REMOVE2.length());
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
		Text title = lang.newText(new Coordinates(30, 30),
				"Doubly Linked List", "title", null, headerStyle);
		Rect rectBackGroundTitle = lang.newRect(new Offset(-10, -10, title,
				AnimalScript.DIRECTION_NW), new Offset(10, 10, title,
				AnimalScript.DIRECTION_SE), "rectBackGroundTitle", null,
				recProp1);

		SourceCode description = lang.newSourceCode(new Coordinates(30, 100),
				"description", null, descriptionStyle);

		setTextToSourceCode(description, DESCRIPTION);

		Rect rectBackGroundDescription = lang.newRect(new Offset(-10, -10,
				description, AnimalScript.DIRECTION_NW), new Offset(10, 10,
				description, AnimalScript.DIRECTION_SE),
				"rectBackGroundDescription", null, recProp2);

		// cleanup
		lang.hideAllPrimitives();

		if (isAdd) {
			// STEP 2.Add
			// ///////////////////////////////////////////////

			// set title
			Text titleAdd = lang.newText(new Coordinates(30, 30),
					"Doubly Linked List - Hinzufuegen", "titleAdd", null,
					headerStyle);
			Rect rectBackGroundTitleAdd = lang.newRect(new Offset(-10, -10,
					titleAdd, AnimalScript.DIRECTION_NW), new Offset(10, 10,
					titleAdd, AnimalScript.DIRECTION_SE),
					"rectBackGroundTitle", null, recProp1);

			// set source code
			SourceCode codeAdd = lang.newSourceCode(new Coordinates(30, 230),
					"codeAdd", null, sourceCodeStyle);
			setTextToSourceCode(codeAdd, CODE_ADD);

			// set description text
			SourceCode descText = lang.newSourceCode(new Coordinates(440, 230),
					"descAdd", null, descriptionStyle);

			// set seperator
			lang.newPolyline(new Coordinates[] { new Coordinates(420, 230),
					new Coordinates(420, 550) }, "seperator", null);

			// create linked list graphic
			doublyLinkedListArrowsForward = new Polyline[doublyLinkedListValues.length];
			doublyLinkedListArrowsBackward = new Polyline[doublyLinkedListValues.length];
			doublyLinkedListBoxes = new Rect[doublyLinkedListValues.length + 2];
			int offset = 100;
			int y = 100;
			int x = 30;
			Rect lastBox = null;

			// null-box at beginning
			doublyLinkedListBoxes[0] = createBox(x + (offset * 0), y, "null");
			doublyLinkedListBoxes[0].changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color_defaultBox,
					null, null);
			for (int i = 0; i < doublyLinkedListValues.length; i++) {
				Rect box = createBox(x + (offset * (i + 1)), y,
						doublyLinkedListValues[i]);
				doublyLinkedListBoxes[i + 1] = box;
				box.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
						Color_defaultBox, null, null);

				if (lastBox == null) {
					lastBox = box;
				} else {
					doublyLinkedListArrowsForward[i - 1] = createArrow(lastBox,
							box, -10, Color_forwardPointer);
					doublyLinkedListArrowsBackward[i] = createArrowBack(box,
							lastBox, 10, Color_backwardPointer);
					lastBox = box;
				}
			}
			// add null pointer
			doublyLinkedListBoxes[doublyLinkedListValues.length + 1] = createBox(
					x + (offset * (doublyLinkedListValues.length + 1)), y,
					"null");
			doublyLinkedListBoxes[doublyLinkedListValues.length + 1]
					.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
							Color_defaultBox, null, null);
			doublyLinkedListArrowsForward[doublyLinkedListValues.length - 1] = createArrow(
					lastBox,
					doublyLinkedListBoxes[doublyLinkedListValues.length + 1],
					-10, Color_forwardPointer);
			doublyLinkedListArrowsBackward[0] = createArrowBack(
					doublyLinkedListBoxes[1], doublyLinkedListBoxes[0], 10,
					Color_backwardPointer);
			lang.nextStep("Start Initialisierung");

			// STEP 3.Add
			// ///////////////////////////////////////////////

			// select first code line
			codeAdd.highlight(1);
			// select first element
			doublyLinkedListBoxes[doublyLinkedListBoxes.length - 2]
					.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
							Color_first_last_legendBox, null, null);
			// add firstBox to legend
			createBox(700, 25, "  ").changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY,
					Color_first_last_legendBox, null, null);
			Text legendLastBox1 = lang.newText(new Coordinates(740, 15),
					"last", "legendFirstBox1", null, textStyle);
			Text legendLastBox2 = lang.newText(new Coordinates(740, 40),
					"element", "legendFirstBox2", null, textStyle);

			// set description
			setTextToSourceCode(descText,
					"Das letzte Element der DoublyLinkedList 'last' ist ungleich null.");

			lang.nextStep();

			// STEP 4.Add
			// ///////////////////////////////////////////////
			codeAdd.unhighlight(1);

			codeAdd.highlight(5);
			descText.hide();
			descText = createSourceCode(
					440,
					230,
					"Aktuellen Zeiger (act) mit dem letzten Element (last)\naus der DoublyLinkedList initialisieren.");
			Polyline pointerAct = createArrow(
					doublyLinkedListBoxes[doublyLinkedListBoxes.length - 2],
					40, -10, Color_actPointer);

			// add pointer to legend
			Polyline legendAct = createArrow(620, 30, 650, 30, Color_actPointer);
			Text legendActText = lang.newText(new Coordinates(550, 20), "act",
					"legendActText", null, textStyle);

			// add pointer to legend
			Polyline legendLast = createArrow(620, 50, 650, 50,
					Color_nextPointer);
			Text legendActLast = lang.newText(new Coordinates(550, 40),
					"act.next", "legendActLast", null, textStyle);

			doublyLinkedListArrowsForward[doublyLinkedListArrowsForward.length - 1]
					.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
							Color_nextPointer, null, null);

			lang.nextStep("Ende Initialisierung");

			// STEP 5.Add
			// ///////////////////////////////////////////////
			codeAdd.unhighlight(5);
			codeAdd.highlight(6);

			descText.hide();
			descText = createSourceCode(440, 230, "Neues Element wird erzeugt.");
			// pointerAct.hide();

			Rect valueBox = createBox(x
					+ (offset * (doublyLinkedListValues.length + 1)), y + 60,
					value);
			valueBox.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
					Color_defaultBox, null, null);
			Rect nullBox = createBox(x
					+ (offset * (doublyLinkedListValues.length + 2)), y + 60,
					"null");
			nullBox.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
					Color_defaultBox, null, null);
			Polyline newPointer = createArrow(valueBox, nullBox, -10,
					Color_forwardPointer);
			Rect nullBox2 = createBox(x
					+ (offset * doublyLinkedListValues.length), y + 60, "null");
			nullBox2.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
					Color_defaultBox, null, null);
			Polyline newPointer2 = createArrowBack(valueBox, nullBox2, 10,
					Color_backwardPointer);

			lang.nextStep("Neues Element erstellen");

			// STEP 6.Add
			// ///////////////////////////////////////////////

			codeAdd.unhighlight(6);
			codeAdd.highlight(7);
			descText.hide();
			descText = createSourceCode(
					440,
					230,
					"Setzte das aktuelle Element (act) als Vorgaenger des neuen Elementes (newElem.last).");

			((Offset) nullBox2.getUpperLeft()).getRef().hide();
			nullBox2.hide();
			newPointer2.hide();
			Polyline newElementArrowBack = createArrowBack(valueBox,
					doublyLinkedListBoxes[doublyLinkedListValues.length], 10,
					Color_backwardPointer);

			lang.nextStep("Neues Element der Liste hinzufuegen");

			// STEP 7.Add
			// ///////////////////////////////////////////////

			codeAdd.unhighlight(7);
			codeAdd.highlight(8);
			descText.hide();
			descText = createSourceCode(440, 230,
					"Setzte das naechste Element des aktuellen (act.next) auf das neue Element.");

			doublyLinkedListArrowsForward[doublyLinkedListArrowsForward.length - 1]
					.hide();
			Polyline newElementArrowFor = createArrow(
					doublyLinkedListBoxes[doublyLinkedListValues.length],
					valueBox, -10, Color_forwardPointer);

			newElementArrowFor.changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color_nextPointer,
					null, null);

			((Offset) (doublyLinkedListBoxes[doublyLinkedListValues.length + 1]
					.getUpperLeft())).getRef().hide();
			doublyLinkedListBoxes[doublyLinkedListValues.length + 1].hide();

			lang.nextStep();

			// STEP 8.Add
			// ///////////////////////////////////////////////
			valueBox.hide();
			((Offset) (valueBox.getUpperLeft())).getRef().hide();
			nullBox.hide();
			((Offset) (nullBox.getUpperLeft())).getRef().hide();
			newPointer.hide();

			newElementArrowFor.hide();
			newElementArrowBack.hide();

			doublyLinkedListArrowsForward[doublyLinkedListArrowsForward.length - 1]
					.hide();

			valueBox = createBox(x
					+ (offset * (doublyLinkedListValues.length + 1)), y, value);
			valueBox.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
					Color_defaultBox, null, null);
			nullBox = createBox(x
					+ (offset * (doublyLinkedListValues.length + 2)), y, "null");
			nullBox.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
					Color_defaultBox, null, null);
			newPointer = createArrow(valueBox, nullBox, -10,
					Color_forwardPointer);
			doublyLinkedListArrowsForward[doublyLinkedListArrowsForward.length - 1] = createArrow(
					doublyLinkedListBoxes[doublyLinkedListValues.length],
					valueBox, -10, Color_forwardPointer);
			doublyLinkedListArrowsForward[doublyLinkedListArrowsForward.length - 1]
					.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
							Color_nextPointer, null, null);
			doublyLinkedListArrowsBackward[doublyLinkedListArrowsBackward.length - 1] = createArrowBack(
					valueBox,
					doublyLinkedListBoxes[doublyLinkedListValues.length], 10,
					Color_backwardPointer);

			codeAdd.unhighlight(8);
			descText.hide();
			descText = createSourceCode(440, 230,
					"Das neue Element wurde erfolgreich der Liste hinzugefuegt.");

			lang.nextStep("Neues Element der Liste hinzugefuegt");

		} else {
			// STEP 2.Remove
			// ///////////////////////////////////////////////
			// set title
			Text titleRemove = lang.newText(new Coordinates(30, 30),
					"Doubly Linked List - Entfernen", "titleRemove", null,
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
			SourceCode descText = lang.newSourceCode(new Coordinates(440, 230),
					"descRemove", null, descriptionStyle);

			// set seperator
			lang.newPolyline(new Coordinates[] { new Coordinates(400, 230),
					new Coordinates(400, 520) }, "seperator", null);

			// create linked list graphic
			doublyLinkedListArrowsForward = new Polyline[doublyLinkedListValues.length];
			doublyLinkedListArrowsBackward = new Polyline[doublyLinkedListValues.length];
			doublyLinkedListBoxes = new Rect[doublyLinkedListValues.length + 2];
			int offset = 100;
			int y = 100;
			int x = 30;
			Rect lastBox = null;

			// null-box at beginning
			doublyLinkedListBoxes[0] = createBox(x + (offset * 0), y, "null");
			doublyLinkedListBoxes[0].changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY, Color_defaultBox,
					null, null);
			for (int i = 0; i < doublyLinkedListValues.length; i++) {
				Rect box = createBox(x + (offset * (i + 1)), y,
						doublyLinkedListValues[i]);
				doublyLinkedListBoxes[i + 1] = box;
				box.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
						Color_defaultBox, null, null);
				if (lastBox == null) {
					lastBox = box;
				} else {
					doublyLinkedListArrowsForward[i - 1] = createArrow(lastBox,
							box, -10, Color_forwardPointer);
					doublyLinkedListArrowsBackward[i] = createArrowBack(box,
							lastBox, 10, Color_backwardPointer);
					lastBox = box;
				}
			}
			// add null pointer
			doublyLinkedListBoxes[doublyLinkedListValues.length + 1] = createBox(
					x + (offset * (doublyLinkedListValues.length + 1)), y,
					"null");
			doublyLinkedListBoxes[doublyLinkedListValues.length + 1]
					.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
							Color_defaultBox, null, null);
			doublyLinkedListArrowsForward[doublyLinkedListValues.length - 1] = createArrow(
					lastBox,
					doublyLinkedListBoxes[doublyLinkedListValues.length + 1],
					-10, Color_forwardPointer);

			doublyLinkedListArrowsBackward[0] = createArrowBack(
					doublyLinkedListBoxes[1], doublyLinkedListBoxes[0], 10,
					Color_backwardPointer);
			lang.nextStep("Start Initialisierung");

			// STEP 3.Remove
			// ///////////////////////////////////////////////
			// select first code line
			codeRemove.highlight(1);
			// select first element
			doublyLinkedListBoxes[1].changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY,
					Color_first_last_legendBox, null, null);
			// add firstBox to legend
			createBox(700, 25, "  ").changeColor(
					AnimationPropertiesKeys.COLOR_PROPERTY,
					Color_first_last_legendBox, null, null);
			Text legendFirstBox1 = lang.newText(new Coordinates(740, 15),
					"first", "legendFirstBox1", null, textStyle);
			Text legendFirstBox2 = lang.newText(new Coordinates(740, 40),
					"element", "legendFirstBox2", null, textStyle);

			// set description
			setTextToSourceCode(descText,
					"Das erste Element der DoublyLinkedList 'first' ist ungleich null.");

			lang.nextStep();

			// STEP 4.Remove
			// ///////////////////////////////////////////////
			codeRemove.unhighlight(1);
			codeRemove.highlight(4);
			descText.hide();
			descText = createSourceCode(
					440,
					230,
					"Aktuellen Zeiger (act) mit dem ersten Element (first)\naus der DoublyLinkedList initialisieren.");
			Polyline pointerAct = createArrow(doublyLinkedListBoxes[1], 40,
					-10, Color_actPointer);

			// add pointer to legend
			Polyline legendAct = createArrow(620, 30, 650, 30, Color_actPointer);
			Text legendActText = lang.newText(new Coordinates(550, 20), "act",
					"legendActText", null, textStyle);
			lang.nextStep("Ende Initialisierung");

			// STEP 5.Remove
			// ///////////////////////////////////////////////
			boolean isValueMatch = false;
			int listIndex = 1;
			for (int i = 1; i < doublyLinkedListValues.length + 1; i++) {
				if (doublyLinkedListValues[i - 1] == value) {
					isValueMatch = true;
					break;
				}

				codeRemove.unhighlight(4);
				codeRemove.unhighlight(6);
				codeRemove.highlight(5);
				descText.hide();
				descText = createSourceCode(
						440,
						230,
						"Solange aktuelles Element (act) ungleich null ist und\nder Wert des aktuellen Elementes ungleich dem zu loeschenden Wertes ist, wird fortgefahren.");

				lang.nextStep();

				codeRemove.unhighlight(5);
				codeRemove.highlight(6);
				descText.hide();
				descText = createSourceCode(440, 230,
						"Das neue aktuelle Element ist der Nachfolger des alten aktuellen Elements.");
				pointerAct.hide();
				pointerAct = createArrow(doublyLinkedListBoxes[i + 1], 40, -10,
						Color_actPointer);

				listIndex++;
				lang.nextStep();
			}

			// STEP 6.Remove
			// ///////////////////////////////////////////////
			codeRemove.unhighlight(4);
			codeRemove.unhighlight(6);
			codeRemove.highlight(5);
			descText.hide();
			if (isValueMatch) {
				// found value to remove
				descText = createSourceCode(
						440,
						230,
						"Der Wert des aktuellen Elementes entspricht dem zu loeschenden Wert.\nSchleife wird beendet.");

				lang.nextStep("Wert in Liste gefunden");

				// STEP 7.Remove
				// ///////////////////////////////////////////////
				codeRemove.unhighlight(5);
				codeRemove.highlight(8);
				descText.hide();
				// found value to remove
				descText = createSourceCode(440, 230,
						"Aktuelles Element ist ungleich Null.");

				doublyLinkedListBoxes[listIndex].hide();
				((Offset) doublyLinkedListBoxes[listIndex].getUpperLeft())
						.getRef().hide();

				// move down the box which will be delete
				Rect actBox = createBox(x + (offset * (listIndex)), y + 60,
						((Text) ((Offset) doublyLinkedListBoxes[listIndex]
								.getUpperLeft()).getRef()).getText());
				doublyLinkedListBoxes[listIndex] = actBox;
				actBox.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
						Color_defaultBox, null, null);
				if (listIndex == 1) {
					actBox.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
							Color_first_last_legendBox, null, null);
				}

				if (listIndex > 1) {
					doublyLinkedListArrowsForward[listIndex - 2].hide();
					doublyLinkedListArrowsForward[listIndex - 2] = createArrow(
							doublyLinkedListBoxes[listIndex - 1], actBox, -10,
							Color_forwardPointer);
				}
				doublyLinkedListArrowsBackward[listIndex - 1].hide();
				doublyLinkedListArrowsBackward[listIndex - 1] = createArrowBack(
						actBox, doublyLinkedListBoxes[listIndex - 1], 10,
						Color_backwardPointer);

				doublyLinkedListArrowsForward[listIndex - 1].hide();
				doublyLinkedListArrowsForward[listIndex - 1] = createArrow(
						actBox, doublyLinkedListBoxes[listIndex + 1], -10,
						Color_forwardPointer);

				if (listIndex != doublyLinkedListBoxes.length - 2) {
					doublyLinkedListArrowsBackward[listIndex].hide();
					doublyLinkedListArrowsBackward[listIndex] = createArrowBack(
							doublyLinkedListBoxes[listIndex + 1], actBox, 10,
							Color_backwardPointer);
				}

				pointerAct.hide();
				pointerAct = createArrow(doublyLinkedListBoxes[listIndex], 40,
						-10, Color_actPointer);

				lang.nextStep("Wert aus Liste loeschen");

				// STEP 8.Remove
				// ///////////////////////////////////////////////

				codeRemove.unhighlight(8);
				codeRemove.highlight(11);
				// set source code
				codeRemove.hide();
				SourceCode codeRemove2 = lang.newSourceCode(new Coordinates(30,
						230), "codeRemove2", null, sourceCodeStyle);
				setTextToSourceCode(codeRemove2, CODE_REMOVE2);
				codeRemove2.highlight(1);

				if (listIndex > 1) {
					descText.hide();
					descText = createSourceCode(440, 230,
							"Der Vogaenger ist ungleich Null.");

					lang.nextStep();
					// STEP 9.Remove
					// ///////////////////////////////////////////////
					codeRemove2.unhighlight(1);
					codeRemove2.highlight(4);
					descText.hide();
					// found value to remove
					descText = createSourceCode(
							440,
							230,
							"Setze als naechstes Element des Vorgaengers den Nachfolger des aktuellen Elements.");

					doublyLinkedListArrowsForward[listIndex - 2].hide();

					Polyline newArrowNext = createArrow(
							doublyLinkedListBoxes[listIndex - 1],
							doublyLinkedListBoxes[listIndex + 1], -10,
							Color_forwardPointer);
					lang.nextStep();

				} else {
					descText.hide();
					descText = createSourceCode(440, 230,
							"Der Vogaenger ist Null.");
					
					preNull = true;
					
					lang.nextStep();
					// STEP 9.Remove
					// ///////////////////////////////////////////////
					codeRemove2.unhighlight(1);
					codeRemove2.highlight(2);
					descText.hide();
					// found value to remove
					descText = createSourceCode(440, 230,
							"Setze als erstes Element 'first' den Nachfolger des aktuellen Elements.");

					actBox.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY,
							Color.BLACK, null, null);
					doublyLinkedListBoxes[2].changeColor(
							AnimationPropertiesKeys.COLOR_PROPERTY,
							Color_first_last_legendBox, null, null);
					lang.nextStep();

				}

				// STEP 10.Remove
				// ///////////////////////////////////////////////
				codeRemove2.unhighlight(2);
				codeRemove2.unhighlight(4);
				codeRemove2.highlight(5);

				if (listIndex != doublyLinkedListBoxes.length - 2) {
					descText.hide();
					descText = createSourceCode(440, 230,
							"Der Nachfolger ist ungleich Null.");

					lang.nextStep();

					// STEP 11.Remove
					// ///////////////////////////////////////////////
					codeRemove2.unhighlight(5);
					codeRemove2.highlight(6);
					descText.hide();
					// found value to remove
					descText = createSourceCode(
							440,
							230,
							"Setze als vorheriges Element des Nachgaengers den Vorgaenger des Aktuellen Elements.");

					// doublyLinkedListArrowsForward[listIndex - 2].hide();
					doublyLinkedListArrowsBackward[listIndex].hide();

					Polyline newArrowBack = createArrowBack(
							doublyLinkedListBoxes[listIndex + 1],
							doublyLinkedListBoxes[listIndex - 1], +10,
							Color_backwardPointer);
					lang.nextStep();

				} else {
					descText.hide();
					descText = createSourceCode(440, 230,
							"Der Nachfolger ist Null.");

					postNull = true;
					
					lang.nextStep();
				}

				// STEP 12.Remove
				// ///////////////////////////////////////////////
				codeRemove2.unhighlight(5);
				codeRemove2.unhighlight(6);
				codeRemove2.highlight(7);
				descText.hide();
				// found value to remove
				descText = createSourceCode(
						440,
						230,
						"Kein Pointer der Liste zeigt mehr auf das zu loeschende Element. Das Element kann geloescht werden.");

				// delete the element from display
				doublyLinkedListBoxes[listIndex].hide();
				((Offset) doublyLinkedListBoxes[listIndex].getUpperLeft())
						.getRef().hide();
				doublyLinkedListArrowsForward[listIndex - 1].hide();
				doublyLinkedListArrowsBackward[listIndex - 1].hide();
				pointerAct.hide();

				if (listIndex == 1 && postNull) {
					doublyLinkedListBoxes[listIndex - 1].hide();
					((Offset) doublyLinkedListBoxes[listIndex - 1]
							.getUpperLeft()).getRef().hide();
				}

				lang.nextStep();

				// STEP 13.Remove
				// ///////////////////////////////////////////////
				codeRemove2.unhighlight(7);
				codeRemove2.highlight(8);
				descText.hide();
				if (listIndex == 1 && postNull) {
					descText = createSourceCode(
							440,
							230,
							"Das zu loeschende Element wurde erfolgreich aus der Liste geloescht.\nDies war das einzige Element der DoublyLinkedList.\nDie Liste ist nun leer.");
				} else {
					descText = createSourceCode(440, 230,
							"Das zu loeschende Element wurde erfolgreich aus der Liste geloescht.");
				}

				lang.nextStep("Wert aus der Liste geloescht");
			} else {
				// value not found
				descText.hide();
				descText = createSourceCode(
						440,
						230,
						"Die DoublyLinkedList besitzt keine weiteres Elemente mehr und somit wird die Schleife beendet.");

				lang.nextStep();

				// STEP 7.Remove
				// ///////////////////////////////////////////////
				codeRemove.unhighlight(5);
				codeRemove.highlight(8);
				descText.hide();
				descText = createSourceCode(
						440,
						230,
						"Das aktuelle Element ist null, d.h., dass die DoublyLinkedList \ndas gesuchte Element nicht enthaelt.");
				lang.nextStep("Wert nicht in der Liste gefunden");

				// STEP 8.Remove
				// ///////////////////////////////////////////////
				codeRemove.unhighlight(8);
				codeRemove.highlight(9);
				descText.hide();
				descText = createSourceCode(440, 230,
						"Der Algorithmus wird beendet und es wird 'false' zurueckgegeben.");
			}

		}

		// Last STEP
		// ///////////////////////////////////////////////
		lang.hideAllPrimitives();
		Text titleLast = lang.newText(new Coordinates(30, 30),
				"Doubly Linked List", "title", null, headerStyle);

		lang.newRect(
				new Offset(-10, -10, titleLast, AnimalScript.DIRECTION_NW),
				new Offset(10, 10, title, AnimalScript.DIRECTION_SE),
				"rectBackGroundTitle", null, recProp1);

		description = lang.newSourceCode(new Coordinates(30, 100),
				"description", null, descriptionStyle);

		setTextToSourceCode(description, CONCLUSION);

		rectBackGroundDescription = lang.newRect(new Offset(-10, -10,
				description, AnimalScript.DIRECTION_NW), new Offset(10, 10,
				description, AnimalScript.DIRECTION_SE),
				"rectBackGroundDescription", null, recProp2);

		lang.nextStep();
	}

	// just needed here
	private int arrowCounter = 0;

	private Polyline createArrow(int x1, int y1, int x2, int y2, Color color) {
		Coordinates[] co = { new Coordinates(x1, y1), new Coordinates(x2, y2) };
		PolylineProperties arrowProp = new PolylineProperties();
		arrowProp.set("fwArrow", true);
		arrowProp.set("color", color);
		return lang.newPolyline(co, "arrow-" + (++arrowCounter), null,
				arrowProp);
	}

	private Polyline createArrow(Rect from, int x2, int y2, int yOffsetFrom,
			boolean back, Color color) {
		Offset offsetUpperLeft = (Offset) from.getUpperLeft();
		Offset offsetLowerRight = (Offset) from.getLowerRight();
		Coordinates upperLeft = (Coordinates) ((Text) offsetUpperLeft.getRef())
				.getUpperLeft();
		FontMetrics metrics = new Canvas().getFontMetrics((Font) chapterStyle
				.get("font"));
		int textWidth = metrics.stringWidth(((Text) offsetUpperLeft.getRef())
				.getText());
		int textHeight = metrics.getMaxAscent();
		int x;
		if (!back) {
			x = Math.abs(offsetLowerRight.getX()) + upperLeft.getX()
					+ textWidth;
		} else {
			x = upperLeft.getX() - Math.abs(offsetLowerRight.getX());
		}
		int y = upperLeft.getY() + (textHeight / 2) + yOffsetFrom;
		return createArrow(x, y, x2, y2, color);
	}

	private Polyline createArrow(Rect from, Rect to, Color color) {
		return createArrow(from, to, 0, color);
	}

	private Polyline createArrow(Rect from, Rect to, int yOffset, Color color) {
		Offset offsetUpperLeft = (Offset) to.getUpperLeft();
		Offset offsetLowerRight = (Offset) to.getLowerRight();
		Coordinates upperLeft = (Coordinates) ((Text) offsetUpperLeft.getRef())
				.getUpperLeft();
		FontMetrics metrics = new Canvas().getFontMetrics((Font) chapterStyle
				.get("font"));
		int textHeight = metrics.getMaxAscent();
		int x2 = upperLeft.getX() - Math.abs(offsetLowerRight.getX());
		int y2 = upperLeft.getY() + (textHeight / 2) + yOffset;
		return createArrow(from, x2, y2, yOffset, false, color);
	}

	private Polyline createArrowBack(Rect from, Rect to, int offset, Color color) {
		Offset offsetUpperLeft = (Offset) to.getUpperLeft();
		Offset offsetLowerRight = (Offset) to.getLowerRight();
		Coordinates upperLeft = (Coordinates) ((Text) offsetUpperLeft.getRef())
				.getUpperLeft();
		FontMetrics metrics = new Canvas().getFontMetrics((Font) chapterStyle
				.get("font"));
		int textHeight = metrics.getMaxAscent();
		int textWidth = metrics.stringWidth(((Text) offsetUpperLeft.getRef())
				.getText());
		// int x2 = upperLeft.getX() - Math.abs(offsetLowerRight.getX());
		int x2 = Math.abs(offsetLowerRight.getX()) + upperLeft.getX()
				+ textWidth;
		int y2 = upperLeft.getY() + (textHeight / 2) + offset;
		return createArrow(from, x2, y2, offset, true, color);
	}

	private Polyline createArrow(Rect toBottom, int length, int offsetX,
			Color color) {
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
		return createArrow(x, y, x2, y2, color);
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
		((Offset) (doublyLinkedListBoxes[index].getUpperLeft())).getRef()
				.hide();
		doublyLinkedListBoxes[index].hide();
		// TODO cleanup array
	}

	private void removeBoxWithArrow(int index) {
		removeBox(index);
		doublyLinkedListArrowsForward[index].hide();
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
	public class DoublyLinkedList<T> {
		private LinkedListElement<T> first;

		public DoublyLinkedList() {/* nix */
		}

		public DoublyLinkedList(T[] initValues) {
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
				// latest = act;
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
			while (act != null && act.value != value) {
				act = act.next;
			}
			if (act == null) {
				return false;
			}

			if (act.prev == null)
				first = act.next;
			else
				act.prev.next = act.next;
			if (act.next != null)
				act.next.prev = act.prev;

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
			private LinkedListElement<T> prev;

			public LinkedListElement(T value) {
				this.value = value;
			}
		}
	}

}