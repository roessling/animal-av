package generators.misc.roundrobin;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import algoanim.animalscript.addons.InfoBox;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import translator.ResourceLocator;

public class MessageOrganizer {

	private static HashMap<String, String> dictionary = new HashMap<String, String>();

	private static HashMap<String, List<String>> messages = new HashMap<String, List<String>>();
	// TODO Explanation texts (up to three lines):

	private static Text currentText1;

	private static Text currentText2;

	private static Text currentText3;

	private static Text currentText4;

	private static Text currentText5;


	private static Rect box;

	private static final int baseX = 420;

	private static final int baseY = 380;

	// TODO Start and end texts (up to approximately six):
	private static List<String> startText;

	private static List<String> endTextInf;

	private static List<String> endTextIncomplete;

	/* Extra für Beispiel 3 (Raketenstart) */
	private static List<String> endTextComplete;

	private static List<String> startTextSim;

	private static InfoBox i;



	public static void initInfoBox(Language lang) {
		i = new InfoBox(lang, new Coordinates(baseX - 30, baseY), 10, "");
	}


	public static void showInfoBox(Language lang) {
		currentText1.hide();
		currentText2.hide();
		currentText3.hide();
		currentText4.hide();
		currentText5.hide();
		box.hide();
		i.show();
	}


	public static void hideInfoBox(Language lang) {
		i.hide();
	}


	public static void showStartText(Language lang, boolean infinity) {
		if (infinity) {
			i.setText(startTextSim);
		} else {
			i.setText(startText);
		}
		lang.nextStep();
	}


	public static void showEndText(Language lang, boolean infinity, boolean completed) {
		if (infinity) {
			i.setText(endTextInf);
			lang.nextStep("The animation ends. All processes have been completely executed.");
		} else {
			if (completed) {
				i.setText(endTextComplete);
			} else {
				i.setText(endTextIncomplete);
			}

			lang.nextStep("The animation ends. The preset time interval is over.");
		}
	}


	public static void initBox(Language lang) {
		RectProperties rP = new RectProperties();
		rP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rP.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(230, 230, 230));
		box = lang.newRect(new Coordinates(baseX + 20, baseY + 30), new Coordinates(baseX + 475, baseY + 95), "explanationbox", null, rP);


		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 13));
		currentText1 = lang.newText(new Coordinates(baseX + 30, baseY + 35), "", "explanationtext1", null, tp);
		currentText2 = lang.newText(new Coordinates(baseX + 30, baseY + 55), "", "explanationtext2", null, tp);
		currentText3 = lang.newText(new Coordinates(baseX + 30, baseY + 75), "", "explanationtext3", null, tp);
		currentText4 = lang.newText(new Coordinates(baseX + 30, baseY + 75), "", "explanationtext3", null, tp);
		currentText5 = lang.newText(new Coordinates(baseX + 30, baseY + 75), "", "explanationtext3", null, tp);
		// currentText3 = lang.newText(new Coordinates(baseX + 30, baseY + 75),
		// "", "explanationtext3", null, tp);
	}


	public static void showText(Language lang, String key) {

		if (!messages.containsKey(key)) {
			return;
		}

		List<String> message = messages.get(key);

		switch (message.size()) {
		case 1:
			currentText1.setText(message.get(0), null, null);
			currentText2.setText("", null, null);
			currentText3.setText("", null, null);
			break;
		case 2:
			currentText1.setText(message.get(0), null, null);
			currentText2.setText(message.get(1), null, null);
			currentText3.setText("", null, null);
			break;
		case 3:
			currentText1.setText(message.get(0), null, null);
			currentText2.setText(message.get(1), null, null);
			currentText3.setText(message.get(2), null, null);
			break;
		}

		messages.remove(key);

		if (messages.isEmpty()) {
			lang.nextStep();
			box.hide();
			currentText1.hide();
			currentText2.hide();
			currentText3.hide();
		}

	}


	public static void setupTexts(Lang language, boolean infinity, int exampleID) throws Exception {

		BufferedReader reader = null;

		if (language == Lang.ENGLISH) {
      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/roundrobin/en_US");
//			reader = new BufferedReader(new FileReader("resources/roundrobin/en_US"));
		} else {
      reader = ResourceLocator.getResourceLocator().getBufferedReader("resources/roundrobin/de_DE");
//			reader = new BufferedReader(new FileReader("resources/roundrobin/de_DE"));
		}

		reader.lines().forEach((String line) -> {
			dictionary.put(line.split("#")[0], line.split("#")[1]);
		});

		messages.put("mRemove", Arrays.asList(dictionary.get("mRemove1"), dictionary.get("mRemove2")));
		messages.put("mRotate", Arrays.asList(dictionary.get("mRotate1"), dictionary.get("mRotate2"), dictionary.get("mRotate3")));
		messages.put("mWait", Arrays.asList(dictionary.get("mWait")));
		messages.put("mWork", Arrays.asList(dictionary.get("mWork1"), dictionary.get("mWork2")));
		messages.put("mNewProcess", Arrays.asList(dictionary.get("mNewProcess1"), dictionary.get("mNewProcess2")));

		startText = Arrays.asList(dictionary.get("startText1" + "-id" + exampleID), dictionary.get("startText2" + "-id" + exampleID),
				dictionary.get("startText3" + "-id" + exampleID), dictionary.get("startText4" + "-id" + exampleID),
				dictionary.get("startText5" + "-id" + exampleID), dictionary.get("startText6" + "-id" + exampleID));

		endTextInf = Arrays.asList(dictionary.get("endText1" + "-id" + exampleID + "-inf"), dictionary.get("endText2" + "-id" + exampleID + "-inf"),
				dictionary.get("endText3" + "-id" + exampleID + "-inf"), dictionary.get("endText4" + "-id" + exampleID + "-inf"),
				dictionary.get("endText5" + "-id" + exampleID + "-inf"), dictionary.get("endText6" + "-id" + exampleID + "-inf"));

		endTextIncomplete = Arrays.asList(dictionary.get("endText1" + "-id" + exampleID + "-inc"),
				dictionary.get("endText2" + "-id" + exampleID + "-inc"), dictionary.get("endText3" + "-id" + exampleID + "-inc"),
				dictionary.get("endText4" + "-id" + exampleID + "-inc"), dictionary.get("endText5" + "-id" + exampleID + "-inc"),
				dictionary.get("endText6" + "-id" + exampleID + "-inc"));
		if (exampleID == 3) {
			endTextComplete = Arrays.asList(dictionary.get("endText1" + "-id" + exampleID + "-comp"),
					dictionary.get("endText2" + "-id" + exampleID + "-comp"), dictionary.get("endText3" + "-id" + exampleID + "-comp"),
					dictionary.get("endText4" + "-id" + exampleID + "-comp"), dictionary.get("endText5" + "-id" + exampleID + "-comp"),
					dictionary.get("endText6" + "-id" + exampleID + "-comp"));

			startTextSim = Arrays.asList(dictionary.get("startText1" + "-id" + exampleID + "-sim"),
					dictionary.get("startText2" + "-id" + exampleID + "-sim"), dictionary.get("startText3" + "-id" + exampleID + "-sim"),
					dictionary.get("startText4" + "-id" + exampleID + "-sim"), dictionary.get("startText5" + "-id" + exampleID + "-sim"),
					dictionary.get("startText6" + "-id" + exampleID + "-sim"));
		} else {
			endTextComplete = endTextInf;
			startTextSim = startText;
		}
	}


	public static void hideTexts() {
		currentText1.setText("", null, null);
		currentText2.setText("", null, null);
		currentText3.setText("", null, null);
	}


	public static String get(String string) {
		return dictionary.get(string);
	}

}
