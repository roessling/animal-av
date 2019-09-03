package generators.misc.roundrobin;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;

public class Drafter {

	private static int maxTime;

	private static final int baseX = 460;

	private static final int baseY = 280;

	private static final int legendBaseX = 1000;

	private static final int legendBaseY = 375;

	private static int counter = 0;

	private static List<Primitive> processComponents = new LinkedList<Primitive>();

	private static Rect frame;

	private static int k;

	private static int colorEffectDuration;

	private static LinkedList<Primitive> rotateBoxComponents = new LinkedList<Primitive>();

	private static LinkedList<Primitive> finishedBoxComponents = new LinkedList<Primitive>();

	private static LinkedList<Primitive> waitingBoxComponents = new LinkedList<Primitive>();

	private static Rect grayRect;

	private static Text sliceText1;

	private static Text sliceText2;

	private static Text processorText;

	private static boolean textHidden = false;

	/*
	 * private static Color[] colors = new Color[] { new Color(152, 209, 121),
	 * new Color(183, 183, 25), new Color(85, 214, 201), new Color(237, 173,
	 * 71), new Color(185, 153, 219), new Color(25, 183, 30), new Color(183, 25,
	 * 107) };
	 */



	public static void setup(Language lang, LinkedList<Process> processes, int k, int colorEffectDuration) {

		maxTime = processes.stream().map((Process p) -> {
			return p.getRemainingTime();
		}).max((Integer i, Integer j) -> {
			return i - j;
		}).get();


		// createProcessorAndQueue(lang);

		createBoxes(lang);
		textHidden = false;
		Drafter.k = k;
		Drafter.colorEffectDuration = colorEffectDuration;
	}


	private static void createBoxes(Language lang) {
		RectProperties properties = new RectProperties();
		properties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		properties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
		Rect rect = lang.newRect(new Coordinates(baseX - 10, baseY - 55), new Coordinates(baseX + 100, baseY - 25), "rotate_box", null, properties);
		rect.hide();

		TextProperties rotateBoxProperties = new TextProperties();
		rotateBoxProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 18));
		Text progressText = lang.newText(new Coordinates(baseX + 8, baseY - 52), "ROTATE", "rotateboxtext", null, rotateBoxProperties);
		progressText.hide();

		Text finishedText = lang.newText(new Coordinates(baseX + 6, baseY - 52), "REMOVE", "finishedtext", null, rotateBoxProperties);
		finishedText.hide();

		Text waitingText = lang.newText(new Coordinates(baseX + 7, baseY - 52), "WAITING", "waitingText", null, rotateBoxProperties);
		waitingText.hide();

		rotateBoxComponents.add(rect);
		rotateBoxComponents.add(progressText);

		finishedBoxComponents.add(rect);
		finishedBoxComponents.add(finishedText);

		waitingBoxComponents.add(rect);
		waitingBoxComponents.add(waitingText);

		RectProperties grayRectProperties = new RectProperties();
		grayRectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		grayRectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(200, 200, 200));
		grayRectProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(200, 200, 200));
		grayRectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		grayRect = lang.newRect(new Coordinates(baseX - 6, baseY - 6), new Coordinates(baseX + 94, baseY + 46), "processor_gray_rect", null,
				grayRectProperties);
		grayRect.hide();
	}


	private static void createProcessorAndQueue(Language lang) {

		RectProperties properties = new RectProperties();
		properties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		properties.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(200, 200, 200));
		properties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
		lang.newRect(new Coordinates(baseX - 10, baseY - 10), new Coordinates(baseX + 100, baseY + 50), "processor_middle", null, properties);

		RectProperties frameProperties = new RectProperties();
		frameProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		frameProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
		frameProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 4);
		frame = lang.newRect(new Coordinates(baseX - 15, baseY - 15), new Coordinates(baseX + 105, baseY + 55), "processor_frame" + counter, null,
				frameProperties);
		counter++;

		RectProperties surroundFrameProperties = new RectProperties();
		surroundFrameProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		surroundFrameProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.DARK_GRAY);
		surroundFrameProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 5);
		lang.newRect(new Coordinates(baseX - 60, baseY - 20), new Coordinates(baseX + 115, baseY + 60), "processor_surround", null,
				surroundFrameProperties);


		RectProperties queueItemProperties = new RectProperties();
		queueItemProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		queueItemProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(200, 200, 200));
		queueItemProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);

		RectProperties queueItemFrameProperties = new RectProperties();
		queueItemFrameProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		queueItemFrameProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
		queueItemFrameProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 4);

		for (int i = 1; i < 6; i++) {

			lang.newRect(new Coordinates(baseX + 20 + 120 * i - 15, baseY - 10), new Coordinates(baseX + 20 + 120 * i + 90 + 20, baseY + 50),
					"queueitem" + counter, null, queueItemProperties);

			lang.newRect(new Coordinates(baseX + 20 + 120 * i - 15 - 3, baseY - 10 - 3),
					new Coordinates(baseX + 20 + 120 * i + 90 + 20 + 3, baseY + 50 + 3), "queueitemframe" + counter, null, queueItemFrameProperties);
			counter++;
		}


		TextProperties processorTextProperties = new TextProperties();
		processorTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 15));
		processorText = lang.newText(new Coordinates(baseX - 58, baseY - 40), "Processor:", "processortext", null, processorTextProperties);

		TextProperties queueTextProperties = new TextProperties();
		queueTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 11));

		lang.newText(new Coordinates(baseX + 43, baseY + 65), "^", "queuetext_1_", null, queueTextProperties);
		lang.newText(new Coordinates(baseX + 3, baseY + 75), "Queue item 1", "queuetext_1", null, queueTextProperties);
		for (int i = 2; i <= 6; i++) {
			lang.newText(new Coordinates(baseX + i * 120 - 57, baseY + 57), "^", "queuetext_" + i + "_", null, queueTextProperties);
			lang.newText(new Coordinates(baseX + i * 120 - 97, baseY + 67), "Queue item " + i, "queuetext_" + i, null, queueTextProperties);
		}

		TextProperties processorSliceTextProperties = new TextProperties();
		processorSliceTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 10));
		sliceText1 = lang.newText(new Coordinates(baseX - 58, baseY + 62), "slice:", "slicetext1", null, processorSliceTextProperties);
		sliceText2 = lang.newText(new Coordinates(baseX - 58, baseY + 72), "", "slicetext2", null, processorSliceTextProperties);
		sliceText1.hide();
		sliceText2.hide();

	}


	public static void createProcess(Language lang, int position, Process p) {
		counter++;

		int x = (position == 0) ? baseX : (baseX + 20 + 120 * position);

		RectProperties properties = new RectProperties();
		properties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		properties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		properties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		properties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		// title
		Rect titlebox = lang.newRect(new Coordinates(x, baseY), new Coordinates(x + 90, baseY + 20), "p_titlebox_" + counter, null, properties);
		int xOffset = (int) (45 - p.getName().length() * 3.3);
		TextProperties textProperties = new TextProperties();
		textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
		Text title = lang.newText(new Coordinates(x + xOffset, baseY + 2), p.getName(), "p_title_" + counter, null, textProperties);


		RectProperties barproperties = new RectProperties();
		barproperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		barproperties.set(AnimationPropertiesKeys.FILL_PROPERTY, p.getColor());
		Rect progress = lang.newRect(new Coordinates(x, baseY + 20),
				new Coordinates(x + (int) (90 * (p.getRemainingTime() / ((double) maxTime))), baseY + 40), "progress_bar_" + counter, null,
				barproperties);


		RectProperties spaceWithoutProgressProperties = new RectProperties();
		spaceWithoutProgressProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		spaceWithoutProgressProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, p.getColor().darker());
		spaceWithoutProgressProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		spaceWithoutProgressProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		Rect container = lang.newRect(new Coordinates(x, baseY + 20), new Coordinates(x + 90, baseY + 40), "progress_bar_container_" + counter, null,
				spaceWithoutProgressProperties);
		int xOffsetProgress = 45 - ((p.getRemainingTime() >= 10) ? 39 : 36);
		TextProperties textBelowProperties = new TextProperties();
		textBelowProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 10));
		Text progresstext = lang.newText(new Coordinates(x + xOffsetProgress, baseY + 22), p.getRemainingTime() + " units left",
				"p_progress_text_" + counter, null, textBelowProperties);

		processComponents.add(titlebox);
		processComponents.add(title);
		processComponents.add(progress);
		processComponents.add(container);
		processComponents.add(progresstext);

	}


	public static void flush() {
		for (Primitive p : processComponents) {
			p.hide();
		}

		processComponents.clear();
	}


	public static void animateProcessor(Language lang, ProcessorType type) {

		switch (type) {
		case INACTIVE:
			frame.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK, null, new TicksTiming(0));
			break;
		case ACTIVE:
			frame.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, new Color(66, 134, 244), null, new TicksTiming(0));
			break;
		case WORKING:
			frame.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, new Color(66, 134, 244), null, new TicksTiming(colorEffectDuration));
			break;
		}

	}


	public static void updateProcessorRemainingTime(Language lang, int helpK, boolean displayText) {

		RectProperties barproperties = new RectProperties();
		barproperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		barproperties.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(66, 134, 244));
		Rect progress = lang.newRect(new Coordinates(baseX - 50, baseY + 55 - (int) ((70 / ((double) k)) * helpK)),
				new Coordinates(baseX - 25, baseY + 55), "progress_bar_processor_" + counter, null, barproperties);
		counter++;
		RectProperties spaceWithoutProgressProperties = new RectProperties();
		spaceWithoutProgressProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		spaceWithoutProgressProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		// spaceWithoutProgressProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
		// Color.BLACK);
		spaceWithoutProgressProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		Rect container = lang.newRect(new Coordinates(baseX - 50, baseY - 15), new Coordinates(baseX - 25, baseY + 55),
				"progress_bar_container_processor_" + counter, null, spaceWithoutProgressProperties);


		if (displayText) {
			sliceText1.show();
			sliceText2.show();
		}
		sliceText2.setText(helpK + " of " + k, null, null);

		processComponents.add(progress);
		processComponents.add(container);

		counter++;
	}


	public static void initProcessor(Language lang) {

		createProcessorAndQueue(lang);
		updateProcessorRemainingTime(lang, 0, false);

	}


	public static void createLegend(Language lang) {

		ArrayProperties arrayProperties = new ArrayProperties();
		arrayProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		arrayProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.CYAN);
		arrayProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 9));

		ArrayDisplayOptions processArrayOptions = new ArrayDisplayOptions(null, null, false);

		TextProperties textProperties = new TextProperties();
		textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 11));

		int distanceX = 35;
		int distanceY = 26;
		int offsetTexts = 3;

		lang.newText(new Coordinates(legendBaseX, legendBaseY + 5), "Legend for the process arrays:", "legend_text", null, textProperties);

		textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 10));
		StringArray stringArray = lang.newStringArray(new Coordinates(legendBaseX, legendBaseY + distanceY), new String[] { "   " }, "active_legend",
				processArrayOptions, arrayProperties);
		stringArray.highlightCell(0, null, null);
		stringArray.showIndices(false, null, null);
		lang.newText(new Coordinates(legendBaseX + distanceX, legendBaseY + distanceY + offsetTexts), "Process (with resp. color) is executed.",
				"active_legend_text", null, textProperties);

		arrayProperties.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.LIGHT_GRAY);
		stringArray = lang.newStringArray(new Coordinates(legendBaseX, legendBaseY + distanceY * 2), new String[] { " A " }, "arrival_legend",
				processArrayOptions, arrayProperties);
		stringArray.highlightCell(0, null, null);
		stringArray.showIndices(false, null, null);
		lang.newText(new Coordinates(legendBaseX + distanceX, legendBaseY + distanceY * 2 + offsetTexts), "Process will arrive at that time point.",
				"arrival_legend_text", null, textProperties);

		stringArray = lang.newStringArray(new Coordinates(legendBaseX, legendBaseY + distanceY * 3), new String[] { " w " }, "waiting_legend",
				processArrayOptions, arrayProperties);
		stringArray.showIndices(false, null, null);
		lang.newText(new Coordinates(legendBaseX + distanceX, legendBaseY + +distanceY * 3 + offsetTexts), "Process is waiting for execution.",
				"waiting_legend_text", null, textProperties);

		stringArray = lang.newStringArray(new Coordinates(legendBaseX, legendBaseY + distanceY * 4), new String[] { " - " }, "notpresent_legend",
				processArrayOptions, arrayProperties);
		stringArray.showIndices(false, null, null);
		lang.newText(new Coordinates(legendBaseX + distanceX, legendBaseY + +distanceY * 4 + offsetTexts), "Process is not present in the queue.",
				"notpresent_legend_text", null, textProperties);

		lang.newRect(new Coordinates(legendBaseX - 8, legendBaseY - 2),
				new Coordinates(legendBaseX + distanceX + 230, legendBaseY + distanceY * 4 + 30), "legend_box", processArrayOptions,
				new RectProperties());

	}


	public static void setRotateMode(boolean active) {

		if (!textHidden) {
			processorText.hide();
			textHidden = true;
		}

		if (active) {
			for (Primitive p : rotateBoxComponents) {
				p.show();
			}
		} else {
			for (Primitive p : rotateBoxComponents) {
				p.hide();
			}
		}
	}


	public static void setFinishedMode(boolean active) {

		if (!textHidden) {
			processorText.hide();
			textHidden = true;
		}

		if (active) {
			for (Primitive p : finishedBoxComponents) {
				p.show();
			}
		} else {
			for (Primitive p : finishedBoxComponents) {
				p.hide();
			}
		}
	}


	public static void setWaitingMode(boolean active) {

		if (!textHidden) {
			processorText.hide();
			textHidden = true;
		}

		if (active) {
			for (Primitive p : waitingBoxComponents) {
				p.show();
			}
		} else {
			for (Primitive p : waitingBoxComponents) {
				p.hide();
			}
		}
	}


	public static void setNoProcessMode(boolean active) {

		if (active) {
			grayRect.show();
		} else {
			grayRect.hide();
		}
	}


}

