package generators.datastructures;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;

public class RingBufferGenerator implements Generator {

	private static final String INTRO = "Der Ringpuffer ist eine konkrete Umsetzung der Warteschlangen. Die Besonderheit beim Ringpuffer \n"
			+ "sind einerseits die 2 Zeiger, naemlich der Schreibe-Zeiger und der Lese-Zeiger, und \n"
			+ "andererseits eine feste Groesse an Plaetzen im Puffer. Dabei zeigt der Schreibe-Zeiger (writePos) auf das erste freie Element \n"
			+ "im Array, das den Ringpuffer repraesentiert, und der Lese-Zeiger (readPos) auf das erste belegte Element. \n"
			+ "Lauuft der Puffer voll, gibt es verschiedene Strategien wie weiter verfahren wird: \n"
			+ "\t 1. Die aeltesten Inhalte werden ueberschrieben, wenn der Puffer voll ist und weitere Elemente \n"
			+ "\t \t in den Ringpuffer abgelegt werden. \n"
			+ "\t \t Doch Vorsicht: Diese Implementierung fuehrt zu Datenverlust! \n"
			+ "\t 2. Die einzufuegenden Elemente werden verworfen bis wieder Platz im Puffer ist, \n"
			+ "\t \t also nach einem read-Aufruf. Hierbei kann es nat�rlich auch zu Datenverlust kommen, \n"
			+ "\t \t sollte es kein externes Handling geben, falls der Puffer voll ist. \n"
			+ "\t \t Das NICHT-Einfuegen, also der sogenannte Pufferueberlauf sollte dann allerdings durch eine \n"
			+ "\t \t entsprechende Rueckmeldung angezeigt werden. \n \n"
			+ "Diese Implementierung betracht den 2. Fall \n \n"
			+ "Quelle: vgl. http://de.wikipedia.org/wiki/Warteschlange_%28Datenstruktur%29";

	private static final String DESCRIPTION = "Der Ringpuffer ist eine konkrete Umsetzung der Warteschlangen. Die Besonderheit beim Ringpuffer"
			+ "sind einerseits die 2 Zeiger, naemlich der Schreibe-Zeiger und der Lese-Zeiger, und "
			+ "andererseits eine feste Groesse an Plaetzen im Puffer. Dabei zeigt der Schreibe-Zeiger (writePos) auf das erste freie Element"
			+ "im Array, das den Ringpuffer repraesentiert, und der Lese-Zeiger (readPos) auf das erste belegte Element."
			+ "Lauuft der Puffer voll, gibt es verschiedene Strategien wie weiter verfahren wird, des weiteren sollte dieser sogenannte "
			+ "Pufferueberlauf durch irgendeine fest definierte Meldung angezeigt werden. Durch die feste Groesse jedoch kann es leicht "
			+ "zu Datenverlust kommen. Hier sollten dann externe Behandlungen durchgefuehrt werden, um dieses Verhalten abzufangen. "
			+ "Anwendungsgebiete f�r den Ringpuffer sind zum Beispiel: Druckerwarteschlangen, asynchrone Prozesse, Netzwerkkommunikation, u.v.m."
			+ "<p>In dieser Animation weden positive Werte in den Puffer <b>geschrieben</b>, und '-1' bedeutet <b>lesen</b> aus dem Puffer.</p> ";

	private static final String END = "Der Ringpuffer ist, wie auch eingangs gesagt, eine konkrete Umsetzung der Warteschlange. \n"
			+ "Besonderheit ist vor allem die feste Groesse, welche allerdings zum Pufferueberlauf fuehren kann. \n"
			+ "Die Komplexitaetsklasse der beiden Methode (read/write) ist O(1), da sie unabhaengig von den Elementen im Puffer sind. \n \n"
			+ "Verwandte Datenstrukturen sind alle Umsetzungen von Warteschlangen, also z.B. Stacks oder Queues. \n \n"
			+ "Wir hoffen diese Animation konnte Ihnen helfen!";

	private static final String CODE_WRITE = "public boolean write(T value) { \n"
			+ "\t if (isFull) { \n"
			+ "\t \t	return false; \n"
			+ "\t } \n"
			+ "\t ringBuffer.add(writePos, value);\n"
			+ "\t writePos++;\n"
			+ "\t writePos = writePos % size;\n"
			+ "\t isEmpty = false;\n"
			+ "\t if (writePos == readPos) { \n"
			+ "\t \t isFull = true; \n"
			+ "\t } \n" + "\t return true; \n" + "}";

	private static final String CODE_READ = "public T read() { \n"
			+ "\t if (isEmpty) { \n" + "\t \t return null; \n" + "\t } \n"
			+ "\t T temp = ringBuffer.get(readPos); \n" + "\t readPos++; \n"
			+ "\t readPos = readPos % size; \n" + "\t isFull = false; \n"
			+ "\t if (writePos == readPos) { \n" + "\t \t isEmpty = true; \n"
			+ "\t } \n" + "\t return temp; \n" + "} \n";

	private Language lang;

	private Polyline[] ringBufferArrows;
	private BufferElement[] ringBufferElements;

	private boolean isWrite;
	private int[] ringBufferValues;

	private RingBuffer<Integer> ringBufferModel;

	// style
	private TextProperties headerStyle;
	private TextProperties chapterStyle;
	private TextProperties textStyle;

	private SourceCodeProperties descriptionProp;
	private SourceCodeProperties sourceCodeProp;

	private RectProperties headerProp;

	private CircleProperties circleProp;

	private PolylineProperties arrowProp;
	private PolylineProperties pointerWriteProp;
	private PolylineProperties pointerReadProp;

	/**
	 * CONSTRUCTOR
	 */
	public RingBufferGenerator() {
		// nix
	}

	public RingBufferGenerator(Language lang) {
		this.lang = lang;
	}

	public void init() {
		lang = new AnimalScript("Ring Buffer [DE]",
				"Ferdinand Pyttel, Michael Ries, Florian Platzer", 800, 700);
		lang.setStepMode(true);
	}

	private int radiusOfRingBuffer;
	private int centerYofRingBuffer;
	private int centerXofRingBuffer;
	private float radiansOfRingBuffer;

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		// #########################
		// Get Parameter from Configuration-Wizard
		// #########################
		ringBufferValues = (int[]) primitives.get("ringBufferValues");
		int size = (int) primitives.get("size");
		// .....

		// #########################
		// Init Style
		// #########################
		descriptionProp = (SourceCodeProperties) props
				.getPropertiesByName("text");
		
		int textSize = (int) descriptionProp
				.get(AnimationPropertiesKeys.SIZE_PROPERTY);
		int fontType = ( (boolean) descriptionProp
				.get(AnimationPropertiesKeys.BOLD_PROPERTY) ) ? Font.BOLD : Font.PLAIN;
		Font fontRead = (Font) descriptionProp
				.get(AnimationPropertiesKeys.FONT_PROPERTY);
		String font = fontRead.getName();
		
		// TEXT
		headerStyle = new TextProperties();
		headerStyle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font,
				fontType, textSize + 8));

		chapterStyle = new TextProperties();
		chapterStyle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font,
				fontType, textSize + 4));

		textStyle = new TextProperties();
		textStyle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font,
				fontType, textSize));

		descriptionProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font,
				fontType, textSize));
		
		
		sourceCodeProp = (SourceCodeProperties) props
				.getPropertiesByName("sourceCode");
		int textSize2 = (int) sourceCodeProp
				.get(AnimationPropertiesKeys.SIZE_PROPERTY);
		int fontType2 = ( (boolean) sourceCodeProp
				.get(AnimationPropertiesKeys.BOLD_PROPERTY) ) ? Font.BOLD : Font.PLAIN;
		Font fontRead2 = (Font) sourceCodeProp
				.get(AnimationPropertiesKeys.FONT_PROPERTY);
		String font2 = fontRead2.getName();
		sourceCodeProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font2,
				fontType2, textSize2));
		
		arrowProp = (PolylineProperties) props.getPropertiesByName("arrow");
		pointerWriteProp = (PolylineProperties) props
				.getPropertiesByName("pointerWrite");
		pointerReadProp = (PolylineProperties) props
				.getPropertiesByName("pointerRead");
		circleProp = (CircleProperties) props.getPropertiesByName("elements");
		circleProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		headerProp = (RectProperties) props.getPropertiesByName("header");
		headerProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		

		// GENERATE RINGBUFFER VISUALIZATION
		ringBufferModel = new RingBuffer<Integer>(size);

		startAnimRingBuffer();

		return lang.toString();
	}

	public String getName() {
		return "Ring Buffer [DE]";
	}

	public String getAlgorithmName() {
		return "Ring Buffer";
	}

	public String getAnimationAuthor() {
		return "Ferdinand Pyttel, Michael Ries, Florian Platzer";
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getCodeExample() {
		return "1. Schreiben: \n \n" + CODE_WRITE + " \n \n 2. Lesen: \n \n "
				+ CODE_READ;
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

	private Text title;
	private Rect rectBackGroundTitle;

	// INTRO FULLSCREEN
	private SourceCode description;

	// unten
	private SourceCode descText;
	private int seperatorPosX;
	private int seperatorPosY;

	// COORDS
	private int legendLeft;
	private Coordinates lowerRight;

	private void startAnimRingBuffer() {
		// Anfangsfolie
		// ///////////////////////////////////////////////
		title = lang.newText(new Coordinates(30, 30), "Ring Puffer", "title",
				null, headerStyle);
		rectBackGroundTitle = lang.newRect(new Offset(-10, -10, title,
				AnimalScript.DIRECTION_NW), new Offset(10, 10, title,
				AnimalScript.DIRECTION_SE), "rectBackGroundTitle", null,
				headerProp);

		// LOWER RIGHT OF
		FontMetrics metrics = new Canvas().getFontMetrics((Font) headerStyle
				.get("font"));
		lowerRight = new Coordinates(30 + metrics.stringWidth("Ring Puffer"),
				30 + metrics.getMaxAscent());
		legendLeft = lowerRight.getX() * 3;

		description = lang.newSourceCode(new Coordinates(30,
				lowerRight.getY() + 50), "description", null, descriptionProp);
		setTextToSourceCode(description, INTRO);

		// //////////////////////////////////////////
		lang.nextStep();

		// cleanup
		description.hide();

		// #########################
		// Init Size
		// #########################
		radiusOfRingBuffer = (int) (ringBufferModel.getSize() * 100 / 2 / Math.PI);
		centerYofRingBuffer = lowerRight.getY() + 150 + radiusOfRingBuffer;
		centerXofRingBuffer = lowerRight.getX() + 50 + radiusOfRingBuffer;
		radiansOfRingBuffer = 360 / ringBufferModel.getSize();

		// ######################
		// create ringBuffer
		// ######################
		ringBufferArrows = new Polyline[ringBufferModel.getSize()];
		ringBufferElements = new BufferElement[ringBufferModel.getSize()];

		BufferElement firstBox = null;
		BufferElement lastBox = null;

		for (int i = 0; i < ringBufferModel.getSize(); i++) {
			BufferElement box = createBufferElement(
					computeCoordinateOnRadius(i, 0, 0), " ");
			ringBufferElements[i] = box;

			if (firstBox == null && lastBox == null) {
				firstBox = box;
				lastBox = box;
			} else {
				ringBufferArrows[i - 1] = createArrow(lastBox, box);
				lastBox = box;
			}
		}
		// close Cycle
		ringBufferArrows[ringBufferModel.getSize() - 1] = createArrow(lastBox,
				firstBox);

		metrics = new Canvas().getFontMetrics((Font) sourceCodeProp
				.get("font"));
		seperatorPosX = metrics
				.stringWidth("T temp = ringBuffer.get(readPos); plusSpace");
		seperatorPosY = metrics.getMaxAscent() * 16;

		// set seperator
		lang.newPolyline(new Coordinates[] {
				new Coordinates(seperatorPosX, centerYofRingBuffer
						+ radiusOfRingBuffer + 70),
				new Coordinates(seperatorPosX, centerYofRingBuffer
						+ radiusOfRingBuffer + 70 + seperatorPosY) },
				"seperator", null);

		// ////////////////////////////////////////////////////////////////////
		// INIT POINTER AND FLAGS
		// ////////////////////////////////////////////////////////////////////
		lang.nextStep("Start Initialisierung");

		metrics = new Canvas().getFontMetrics((Font) textStyle.get("font"));
		int textStyleDistance = (int) (metrics.getMaxAscent() * 1.5);

		// add pointer to legend
		Polyline legendWrite = createArrowForward(legendLeft, 30,
				legendLeft + 30, 30, pointerWriteProp);
		Text legendWriteText = lang.newText(
				new Coordinates(legendLeft + 40, 15), "writePos = 0",
				"legendWriteText", null, textStyle);

		Polyline pointerWrite = this.createArrowPointerWrite(0);

		// descText.hide();
		descText = createDescription("Schreibe-Zeiger (writePos) auf das erste Element \n aus dem Ring Puffer initialisieren.");

		// ////////////////////////////////////////////////////////////////////
		lang.nextStep();

		// add pointer to legend
		Polyline legendRead = createArrowForward(legendLeft,
				30 + textStyleDistance, legendLeft + 30,
				30 + textStyleDistance, pointerReadProp);
		Text legendReadText = lang.newText(new Coordinates(legendLeft + 40,
				25 + textStyleDistance), "readPos = 0", "legendReadText", null,
				textStyle);

		Polyline pointerRead = this.createArrowPointerRead(0);

		descText.hide();
		descText = createDescription("Lese-Zeiger (readPos) auf das erste Element \n aus dem Ring Puffer initialisieren.");

		// ////////////////////////////////////////////////////////////////////
		lang.nextStep();

		Text legendEmptyText = lang.newText(new Coordinates(legendLeft,
				30 + (int) (textStyleDistance * 2)), "isEmpty:    'true'",
				"legendReadText", null, textStyle);

		descText.hide();
		descText = createDescription("Die Flag (isEmpty) auf 'true' initialisieren.");

		// ////////////////////////////////////////////////////////////////////
		lang.nextStep();

		Text legendFullText = lang.newText(new Coordinates(legendLeft,
				30 + (int) (textStyleDistance * 3)), "isFull:         'false'",
				"legendReadText", null, textStyle);

		descText.hide();
		descText = createDescription("Die Flag (isFull) auf 'false' initialisieren.");

		// ////////////////////////////////////////////////////////////////////
		lang.nextStep("Ende Initialisierung");

		SourceCode action = createAction("");

		SourceCode code = lang.newSourceCode(new Coordinates(30,
				centerYofRingBuffer + radiusOfRingBuffer + 50), "code", null,
				descriptionProp);

		for (int j = 0; j < ringBufferValues.length; j++) {

			// Check if next element is reading or writing
			int content = ringBufferValues[j];
			if (content < 0) {
				isWrite = false;
			} else {
				isWrite = true;
			}

			title.hide();
			rectBackGroundTitle.hide();
			code.hide();
			action.hide();

			if (isWrite) {
				// #################################
				// TITLE
				// #################################
				title = lang.newText(new Coordinates(30, 30),
						"Ring Puffer - Schreiben", "title", null,
						headerStyle);
				rectBackGroundTitle = lang.newRect(new Offset(-10, -10, title,
						AnimalScript.DIRECTION_NW), new Offset(10, 10, title,
						AnimalScript.DIRECTION_SE), "rectBackGroundTitle",
						null, headerProp);
				// #################################
				// SOURCECODE
				// #################################
				code = lang.newSourceCode(new Coordinates(30,
						centerYofRingBuffer + radiusOfRingBuffer + 50), "code",
						null, sourceCodeProp);
				setTextToSourceCode(code, CODE_WRITE);
				// #################################
				// ACTION
				// #################################
				action = createAction("Wert zum Schreiben: " + content);
				// #################################

				descText.hide();

				// ////////////////////////////////////////////////////////////////////
				lang.nextStep("Aufruf der Write-Methode");
				code.highlight(0);
				descText.hide();
				descText = createDescription("Aufruf der Write-Methode.");

				// ////////////////////////////////////////////////////////////////////
				lang.nextStep();
				code.unhighlight(0);
				code.highlight(1);
				descText.hide();
				descText = createDescription("Pruefen, ob der Puffer voll ist. \n Angezeigt durch die Flag (isFull).");

				int writePos = ringBufferModel.getWritePos();
				if (ringBufferModel.write(content)) {
					// ////////////////////////////////////////////////////////////////////
					lang.nextStep();
					code.unhighlight(1);
					code.highlight(3);
					descText.hide();
					descText = createDescription("Ueberspringen, \n falls der Puffer nicht voll ist.");

					// ////////////////////////////////////////////////////////////////////
					lang.nextStep("    Wert schreiben.");
					code.unhighlight(3);
					code.highlight(4);
					descText.hide();
					descText = createDescription("Wert (value) an dem naechsten \n freien Platz (writePos) einfuegen.");

					updateRingBuffer(writePos, "" + content);

					if (ringBufferModel.getWritePos() == 0) {
						// ////////////////////////////////////////////////////////////////////
						lang.nextStep();
						code.unhighlight(4);
						code.highlight(5);
						descText.hide();
						descText = createDescription("Schreibe-Zeiger (writePos) erhoehen.");

						// ////////////////////////////////////////////////////////////////////
						lang.nextStep("    Modulo - Sprung vom Ende des Puffers auf Anfang");
						code.unhighlight(5);
						code.highlight(6);
						descText.hide();
						descText = createDescription("Modulorechnung hat Auswirkung, \n da wir am Ende des Puffer sind und \n den Zeiger wieder auf Anfang setzen.");

						pointerWrite.hide();
						// get updated!!!! write-Position
						pointerWrite = this
								.createArrowPointerWrite(ringBufferModel
										.getWritePos());
					} else {
						// ////////////////////////////////////////////////////////////////////
						lang.nextStep();
						code.unhighlight(4);
						code.highlight(5);
						code.highlight(6);
						descText.hide();
						descText = createDescription("Schreibe-Zeiger (writePos) erhoehen. \n Modulorechnung hat KEINE Auswirkung, \n da wir mitten im Puffer sind.");
						pointerWrite.hide();
						// get updated!!!! write-Position
						pointerWrite = this
								.createArrowPointerWrite(ringBufferModel
										.getWritePos());
					}

					legendWriteText.setText(
							"writePos = " + ringBufferModel.getWritePos(),
							new MsTiming(0), new MsTiming(0));

					// ////////////////////////////////////////////////////////////////////
					lang.nextStep();
					code.unhighlight(5);
					code.unhighlight(6);
					code.highlight(7);
					descText.hide();
					descText = createDescription("Da ein Wert geschrieben wurden \n ist der Buffer keinesfalls leer.");

					legendEmptyText.setText("isEmpty:    'false'",
							new MsTiming(0), new MsTiming(0));

					// ////////////////////////////////////////////////////////////////////
					lang.nextStep();
					code.unhighlight(7);
					code.highlight(8);
					descText.hide();
					descText = createDescription("Pruefen, ob Schreibe- und Lese-Zeiger \n auf der gleichen Stelle sind.");

					if (ringBufferModel.isFull()) {
						// ////////////////////////////////////////////////////////////////////
						lang.nextStep("    Flag 'isFull' setzen.");
						code.unhighlight(8);
						code.highlight(9);
						code.highlight(10);

						descText.hide();
						descText = createDescription("Falls ja, Flag (isFull) setzen.");

						legendFullText.setText("isFull:         'true'",
								new MsTiming(0), new MsTiming(0));

					} else {
						// ////////////////////////////////////////////////////////////////////
						lang.nextStep();
						code.unhighlight(8);
						code.highlight(10);
						descText.hide();
						descText = createDescription("Falls nein, ueberspringen.");
					}
					// ////////////////////////////////////////////////////////////////////
					lang.nextStep("    Rueckgabe");
					code.unhighlight(9);
					code.unhighlight(10);
					code.highlight(11);
					descText.hide();
					descText = createDescription("'True' zurueck geben, \n weil das Schreiben erfolgreich war.");

					// ////////////////////////////////////////////////////////////////////
					lang.nextStep();
					code.unhighlight(11);
					descText.hide();
					descText = createDescription("Methode beendet. \n Auf naechste Aktion warten.");
					action.hide();
					action = createAction("Wert '" + content
							+ "' erfolgreich geschrieben.");

				} else {
					// ////////////////////////////////////////////////////////////////////
					lang.nextStep("    Puffer ist voll - Pufferueberlauf");
					code.unhighlight(1);
					code.highlight(2);
					descText.hide();
					descText = createDescription("'False' zurueckgeben, \n falls der Puffer voll ist \n um anzuzeigen, dass das Schreiben \n NICHT erfolgreich war.");

					// ////////////////////////////////////////////////////////////////////
					lang.nextStep();
					code.unhighlight(2);
					action.hide();
					action = createAction("Buffer ist voll!  \n" + "Wert: '"
							+ content + "'  kann nicht eingefuegt werden. \n");
				}
			} else {

				// #################################
				// TITLE
				// #################################
				title = lang
						.newText(new Coordinates(30, 30),
								"Ring Puffer - Lesen", "title", null,
								headerStyle);
				rectBackGroundTitle = lang.newRect(new Offset(-10, -10, title,
						AnimalScript.DIRECTION_NW), new Offset(10, 10, title,
						AnimalScript.DIRECTION_SE), "rectBackGroundTitle",
						null, headerProp);
				// #################################
				// SOURCECODE
				// #################################
				code = lang.newSourceCode(new Coordinates(30,
						centerYofRingBuffer + radiusOfRingBuffer + 50), "code",
						null, sourceCodeProp);
				setTextToSourceCode(code, CODE_READ);
				// #################################
				// ACTION
				// #################################
				action = createAction("Wert lesen");
				// #################################

				descText.hide();

				// ////////////////////////////////////////////////////////////////////
				lang.nextStep("Aufruf der Read-Methode");
				code.highlight(0);
				descText.hide();
				descText = createDescription("Aufruf der Read-Methode.");

				// ////////////////////////////////////////////////////////////////////
				lang.nextStep();
				code.unhighlight(0);
				code.highlight(1);
				descText.hide();
				descText = createDescription("Pruefen, ob der Puffer leer ist. \n Angezeigt durch die Flag (isEmpty).");

				int readPos = ringBufferModel.getReadPos();
				Integer temp = ringBufferModel.read();
				if (temp != null) {
					// ////////////////////////////////////////////////////////////////////
					lang.nextStep();
					code.unhighlight(1);
					code.highlight(3);
					descText.hide();
					descText = createDescription("Ueberspringen, \n falls der Puffer nicht leer ist.");

					// ////////////////////////////////////////////////////////////////////
					lang.nextStep("    Wert lesen.");
					code.unhighlight(3);
					code.highlight(4);
					descText.hide();
					descText = createDescription("Wert an der Stelle (readPos) auslesen \n und zwischenspeichern in 'temp'.");

					updateRingBuffer(readPos, "");

					if (ringBufferModel.getReadPos() == 0) {
						// ////////////////////////////////////////////////////////////////////
						lang.nextStep();
						code.unhighlight(4);
						code.highlight(5);
						descText.hide();
						descText = createDescription("Lese-Zeiger (readPos) erhoehen.");

						// ////////////////////////////////////////////////////////////////////
						lang.nextStep("    Modulo - Sprung vom Ende des Puffers auf Anfang");
						code.unhighlight(5);
						code.highlight(6);
						descText.hide();
						descText = createDescription("Modulorechnung hat Auswirkung, \n da wir am Ende des Puffer sind und \n den Zeiger wieder auf Anfang setzen.");

						pointerRead.hide();
						// get updated!!!! read-Position
						pointerRead = this
								.createArrowPointerRead(ringBufferModel
										.getReadPos());
					} else {
						// ////////////////////////////////////////////////////////////////////
						lang.nextStep();
						code.unhighlight(4);
						code.highlight(5);
						code.highlight(6);
						descText.hide();
						descText = createDescription("Lese-Zeiger (readPos) erhoehen. \n Modulorechnung hat KEINE Auswirkung, \n da wir mitten im Puffer sind.");

						pointerRead.hide();
						// get updated!!!! read-Position
						pointerRead = this
								.createArrowPointerRead(ringBufferModel
										.getReadPos());
					}

					legendReadText.setText(
							"readPos = " + ringBufferModel.getReadPos(),
							new MsTiming(0), new MsTiming(0));

					// ////////////////////////////////////////////////////////////////////
					lang.nextStep();
					code.unhighlight(5);
					code.unhighlight(6);
					code.highlight(7);
					descText.hide();
					descText = createDescription("Da ein Wert gelesen wurden \n ist der Buffer keinesfalls voll.");

					legendFullText.setText("isFull:         'false'",
							new MsTiming(0), new MsTiming(0));

					// ////////////////////////////////////////////////////////////////////
					lang.nextStep();
					code.unhighlight(7);
					code.highlight(8);
					descText.hide();
					descText = createDescription("Pruefen, ob Schreibe- und Lese-Zeiger \n auf der gleichen Stelle sind.");

					if (ringBufferModel.isEmpty()) {
						// ////////////////////////////////////////////////////////////////////
						lang.nextStep("    Flag 'isEmpty' setzen.");
						code.unhighlight(8);
						code.highlight(9);
						code.highlight(10);
						descText.hide();
						descText = createDescription("Falls ja, Flag (isEmpty) setzen.");

						legendEmptyText.setText("isEmpty:    'true'",
								new MsTiming(0), new MsTiming(0));
					} else {
						// ////////////////////////////////////////////////////////////////////
						lang.nextStep();
						code.unhighlight(8);
						code.highlight(10);
						descText.hide();
						descText = createDescription("Falls nein, ueberspringen.");
					}
					// ////////////////////////////////////////////////////////////////////
					lang.nextStep("    Rueckgabe");
					code.unhighlight(9);
					code.unhighlight(10);
					code.highlight(11);
					descText.hide();
					descText = createDescription("'temp' zurueck geben, \n also der gelesene Wert.");

					// ////////////////////////////////////////////////////////////////////
					lang.nextStep();
					code.unhighlight(11);
					descText.hide();
					descText = createDescription("Methode beendet. \n Auf naechste Aktion warten.");
					action.hide();
					action = createAction("Wert: '" + temp
							+ "' erfolgreich gelesen.");

				} else {
					// ////////////////////////////////////////////////////////////////////
					lang.nextStep("    Leerer Puffer -  Kein Lesen moeglich.");
					code.unhighlight(1);
					code.highlight(2);
					descText.hide();
					descText = createDescription("'null' zurueckgeben, \n falls der Puffer leer ist \n um anzuzeigen, dass das Lesen \n NICHT erfolgreich war.");

					// ////////////////////////////////////////////////////////////////////
					lang.nextStep();
					code.unhighlight(2);
					action.hide();
					action = createAction("Buffer ist leer!  \n"
							+ "Kein Lesen moeglich \n");
				}
			}
			// ////////////////////////////////////////////////////////////////////
			lang.nextStep();
		}

		lang.hideAllPrimitives();
		// #################################
		// TITLE
		// #################################
		title = lang.newText(new Coordinates(30, 30),
				"Ring Puffer", "title", null, headerStyle);
		rectBackGroundTitle = lang.newRect(new Offset(-10, -10, title,
				AnimalScript.DIRECTION_NW), new Offset(10, 10, title,
				AnimalScript.DIRECTION_SE), "rectBackGroundTitle", null,
				headerProp);

		description = lang.newSourceCode(new Coordinates(30,
				lowerRight.getY() + 50), "description", null, descriptionProp);
		setTextToSourceCode(description, END);

	}

	private SourceCode createAction(String message) {
		SourceCode temp = lang.newSourceCode(
				new Coordinates(30, lowerRight.getY() + 30), "action", null,
				descriptionProp);
		setTextToSourceCode(temp, message);
		return temp;
	}

	// ##########################################################
	// ##########################################################
	// HELPER METHODES
	// ##########################################################

	// Counter just needed here
	private int arrowCounter = 0;

	private Polyline createArrowForward(int x1, int y1, int x2, int y2,
			PolylineProperties prop) {
		Coordinates[] co = { new Coordinates(x1, y1), new Coordinates(x2, y2) };
		prop.set("fwArrow", true);
		prop.set("bwArrow", false);
		return lang.newPolyline(co, "arrow-" + (++arrowCounter), null,
				prop);
	}

	private Polyline createArrowBackward(int x1, int y1, int x2, int y2,
			PolylineProperties prop) {
		Coordinates[] co = { new Coordinates(x1, y1), new Coordinates(x2, y2) };
		prop.set("fwArrow", false);
		prop.set("bwArrow", true);
		return lang.newPolyline(co, "arrow-" + (++arrowCounter), null,
				prop);
	}

	private Polyline createArrowPointerWrite(int index) {
		return createArrowPointer(index, -2, pointerWriteProp);
	}

	private Polyline createArrowPointerRead(int index) {
		return createArrowPointer(index, 2, pointerReadProp);
	}

	private Polyline createArrowPointer(int index, int offset, PolylineProperties prop) {
		int rad = ringBufferElements[index].getRadius();
		Coordinates first = computeCoordinateOnRadius(index, offset, rad + 20);
		Coordinates second = computeCoordinateOnRadius(index, offset, rad + 50);

		return createArrowBackward(first.getX(), first.getY(), second.getX(),
				second.getY() , prop);
	}

	private Coordinates computeCoordinateOnRadius(int index,
			int offsetOnRadius, int offsetFromCenter) {
		return new Coordinates(
				centerXofRingBuffer
						+ (int) ((radiusOfRingBuffer + offsetFromCenter) * Math.cos(Math
								.toRadians((radiansOfRingBuffer * index)
										- (90 + offsetOnRadius)))),
				centerYofRingBuffer
						+ (int) ((radiusOfRingBuffer + offsetFromCenter) * Math.sin(Math
								.toRadians((radiansOfRingBuffer * index)
										- (90 + offsetOnRadius)))));
	}

	private Polyline createArrow(BufferElement from, BufferElement to) {

		int offFrom = from.getRadius();
		int offTo = to.getRadius();

		int xFrom = from.getCenterX();
		int yFrom = from.getCenterY();
		int xTo = to.getCenterX();
		int yTo = to.getCenterY();

		if (xFrom != xTo) {
			if (xFrom < xTo) {
				if (xFrom + offFrom + offTo < xTo) {
					xFrom = xFrom + offFrom;
					xTo = xTo - offTo;
				}
			} else {
				if (xFrom - offFrom - offTo > xTo) {
					xFrom = xFrom - offFrom;
					xTo = xTo + offTo;
				}
			}
		}

		if (yFrom != yTo) {
			if (yFrom < yTo) {
				if (yFrom + offFrom + offTo < yTo) {
					yFrom = yFrom + offFrom;
					yTo = yTo - offTo;
				}
			} else {
				if (yFrom - offFrom - offTo > yTo) {
					yFrom = yFrom - offFrom;
					yTo = yTo + offTo;
				}
			}
		}

		return createArrowForward(xFrom, yFrom, xTo, yTo, arrowProp);
	}

	// Counter just needed here
	private int boxCounter = 0;

	private BufferElement createBufferElement(Coordinates coord, String value) {
		return createBufferElement(coord.getX(), coord.getY(), value);
	}

	private BufferElement createBufferElement(int x, int y, String value) {
		FontMetrics metrics = new Canvas().getFontMetrics((Font) chapterStyle
				.get("font"));
		int textHeight = metrics.getMaxAscent();
		int textWidth = metrics.stringWidth(value);

		Text textValue = lang.newText(new Coordinates(x - textWidth / 2, y
				- textHeight / 2), value, "box-" + (++boxCounter), null,
				chapterStyle);

		int radius = Math.max((textHeight / 2) + 2, (textWidth / 2) + 2);

		Circle circle = lang.newCircle(new Offset(0, 0, textValue,
				AnimalScript.DIRECTION_C), radius, "rec-" + boxCounter, null,
				circleProp);

		return new BufferElement(circle, textValue, x, y, radius);
	}

	private void updateRingBuffer(int pos, String text) {
		BufferElement temp = createBufferElement(
				ringBufferElements[pos].getCenterX(),
				ringBufferElements[pos].getCenterY(), text);
		// cleanup
		ringBufferElements[pos].getCircle().hide();
		ringBufferElements[pos].getText().hide();
		ringBufferArrows[pos].hide();
		int preReadPos = (pos == 0) ? (ringBufferModel.getSize() - 1)
				: (pos - 1);
		ringBufferArrows[preReadPos].hide();

		// refill
		ringBufferElements[pos] = temp;
		int postReadPos = (pos == (ringBufferModel.getSize() - 1)) ? 0
				: (pos + 1);
		ringBufferArrows[pos] = createArrow(ringBufferElements[pos],
				ringBufferElements[postReadPos]);
		ringBufferArrows[preReadPos] = createArrow(
				ringBufferElements[preReadPos], ringBufferElements[pos]);
	}

	// Counter just needed here
	private int codeCounter = 0;

	private SourceCode createSourceCode(int x, int y, String code) {
		SourceCode scode = lang.newSourceCode(new Coordinates(x, y),
				"sourcecode-" + (++codeCounter), null, descriptionProp);
		setTextToSourceCode(scode, code);
		return scode;
	}

	private SourceCode createDescription(String code) {
		return createSourceCode(seperatorPosX + 30, centerYofRingBuffer
				+ radiusOfRingBuffer + 50, code);
	}

	private void setTextToSourceCode(SourceCode sourceCode, String text) {
		String[] lines = text.split("\n");
		for (String line : lines) {
			int tabs = line.split("\t").length - 1;
			sourceCode.addCodeLine(line, null, tabs, null);
		}
	}

	// #################################################
	// inner class for buffer visualization
	// #################################################

	private class BufferElement {
		private Circle circle;
		private Text text;
		private int centerX;
		private int centerY;
		private int radius;

		public BufferElement(Circle circle, Text text, int centerX,
				int centerY, int radius) {
			super();
			this.circle = circle;
			this.text = text;
			this.centerX = centerX;
			this.centerY = centerY;
			this.radius = radius;
		}

		public Circle getCircle() {
			return circle;
		}

		public Text getText() {
			return text;
		}

		public int getCenterX() {
			return centerX;
		}

		public int getCenterY() {
			return centerY;
		}

		public int getRadius() {
			return radius;
		}
	}

	// #################################################
	// inner class for RingBufferModel
	// #################################################
	private class RingBuffer<T> {

		private ArrayList<T> ringBuffer;
		private int readPos = 0;
		private int writePos = 0;
		private int size = 0;
		private boolean isFull = false;
		private boolean isEmpty = true;

		public RingBuffer(int size) {
			this.size = size;
			this.ringBuffer = new ArrayList<T>(size);
		}

		/**
		 * @param value
		 *            to insert
		 * @return true if writing in Buffer is done, false if Buffer is full
		 *         and value isn`t added to buffer
		 */
		public boolean write(T value) {
			if (isFull) {
				return false;
			}
			ringBuffer.add(writePos, value);
			writePos++;

			// take care of size from Buffer
			writePos = writePos % size;

			// If I write something the buffer is never empty
			isEmpty = false;

			// buffer is full
			if (writePos == readPos) {
				isFull = true;
			}

			return true;
		}

		/**
		 * @return null, if the buffer is empty or the read element
		 */
		public T read() {
			if (isEmpty) {
				return null;
			}
			T temp = ringBuffer.get(readPos);
			readPos++;

			// If I write something the buffer is never empty
			isFull = false;

			// take care of size from Buffer
			readPos = readPos % size;

			// buffer is empty
			if (writePos == readPos) {
				isEmpty = true;
			}

			return temp;
		}

		public int getReadPos() {
			return readPos;
		}

		public int getWritePos() {
			return writePos;
		}

		public int getSize() {
			return size;
		}

		public boolean isFull() {
			return isFull;
		}

		public boolean isEmpty() {
			return isEmpty;
		}

		public String toString() {
			String str = "";
			for (int i = 0; i < this.size; i++) {
				str += read() + ", ";
			}
			return str;
		}
	}

}