import java.awt.Color;
import java.awt.Font;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.ArrayPrimitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class CocktailSortOptimized {
	private Language	lang;
	private Text			header;
	private Rect			hbox;

	public CocktailSortOptimized(Language l) {
		lang = l;
		lang.setStepMode(true);
	}

	public void sort(int[] arr) {
		buildHeader();
		lang.nextStep();

		SourceCode scode = buildSource();
		lang.nextStep();

// "CocktailSort(int[] array) {"
		scode.highlight(0);
		StringArray vis_arr = buildArray(arr);
		lang.nextStep();
		Timing time = new TicksTiming(100);

		ArrayMarker amNewEndFront = buildArrayMarker(vis_arr, "newEndFront", new Color(0xFFA0A0));
		ArrayMarker amNewEndBack = buildArrayMarker(vis_arr, "newEndBack", new Color(0xFFA0A0));
		ArrayMarker amEnd = buildArrayMarker(vis_arr, "end", new Color(0xB00000));
		amEnd.hide();
		ArrayMarker amI = buildArrayMarker(vis_arr, "i", new Color(0x00D000));
		amI.hide();
		ArrayMarker amJ = buildArrayMarker(vis_arr, "j", new Color(0x00D0D0));
		amJ.hide();

//"  boolean swapped = true;"
//"  int newEndFront = 0, newEndBack = array.length - 1;"
		scode.toggleHighlight(0, 1);
		scode.highlight(2);
		boolean swapped = true;
		Square swp_check = buildSwappedChkBox();
		int newEndFront = 0, newEndBack = arr.length - 1;
		amNewEndBack.move(arr.length, null, null);
		lang.nextStep();

//"  for(int end = newEndBack; swapped && (newEndFront < end); end = newEndBack) {"
		scode.unhighlight(1);
		scode.toggleHighlight(2, 3);
		amEnd.move(arr.length, null, null);
		amEnd.show();
		lang.nextStep();

		scode.unhighlight(3);
		for(int end = newEndBack; swapped && (end > newEndFront); end = newEndBack) {
//"    swapped = false;"
			scode.highlight(4);
			swapped = false;
			swp_check.changeColor("color", new Color(0x009000), null, time);
			swp_check.changeColor("fillColor", Color.WHITE, null, time);
			lang.nextStep();

//"    for(int i = newEndFront, j = i + 1; i < end; i++, j++) {"
			scode.toggleHighlight(4, 5);
			amI.move(newEndFront + 1, null, null);
			amI.show();
			amJ.move(newEndFront + 2, null, null);
			amJ.show();
			lang.nextStep();

			for(int i = newEndFront, j = i + 1; i < end; i++, j++) {
//"      if(array[i] > array[j]) {"
				scode.toggleHighlight(5, 6);
				lang.nextStep();

				scode.unhighlight(6);
				if(arr[i] > arr[j]) {
//"        swap(array[i], array[j]);"
					scode.highlight(7);
					int tmp = arr[i];
					arr[i] = arr[j];
					arr[j] = tmp;
					vis_arr.swap(i + 1, j + 1, null, time);
					lang.nextStep();

//"        swapped = true;"
					scode.toggleHighlight(7, 8);
					swapped = true;
					swp_check.changeColor("color", new Color(0xD00000), null, time);
					swp_check.changeColor("fillColor", Color.RED, null, time);
					lang.nextStep();

//"        newEndBack = i;"
					scode.toggleHighlight(8, 9);
					newEndBack = i;
					amNewEndBack.move(i + 1, null, time);
					lang.nextStep();

					scode.unhighlight(9);
				}

//"    for(int i = newEndFront, j = i + 1; i < end; i++, j++) {" // looped
				scode.highlight(5);
				amI.move(i + 2, null, time);
				amJ.move(j + 2, null, time);
				lang.nextStep();
			}

//"    if(!swapped || newEndBack <= newEndFront) break;"
			scode.toggleHighlight(5, 13);
			amI.hide();
			amJ.hide();
			vis_arr.highlightCell(newEndBack + 2, end + 1, null, null);
			vis_arr.highlightElem(newEndBack + 2, end + 1, null, null);
			lang.nextStep();
			if(!swapped || newEndBack <= newEndFront) {
				scode.unhighlight(13);
				break;
			}

//"    swapped = false;"
//"    end = newEndFront;"
			scode.toggleHighlight(13, 15);
			scode.highlight(16);
			swapped = false;
			swp_check.changeColor("color", new Color(0x009000), null, time);
			swp_check.changeColor("fillColor", Color.WHITE, null, time);
			end = newEndFront;
			amEnd.move(newEndFront + 1, null, time);
			lang.nextStep();

//"    for(int j = newEndBack, i = j - 1; j > end; i--, j--) {"
			scode.unhighlight(15);
			scode.toggleHighlight(16, 17);
			amI.move(newEndBack, null, null);
			amI.show();
			amJ.move(newEndBack + 1, null, null);
			amJ.show();
			lang.nextStep();

			for(int j = newEndBack, i = j - 1; j > end; i--, j--) {
//"      if(array[i] > array[j]) {"
				scode.toggleHighlight(17, 18);
				lang.nextStep();

				scode.unhighlight(18);
				if(arr[i] > arr[j]) {
//"        swap(array[i], array[j]);"
					scode.highlight(19);
					int tmp = arr[i];
					arr[i] = arr[j];
					arr[j] = tmp;
					vis_arr.swap(i + 1, j + 1, null, time);
					lang.nextStep();

//"        swapped = true;"
					scode.toggleHighlight(19, 20);
					swapped = true;
					swp_check.changeColor("color", new Color(0xD00000), null, time);
					swp_check.changeColor("fillColor", Color.RED, null, time);
					lang.nextStep();

//"        newEndFront = j;"
					scode.toggleHighlight(20, 21);
					newEndFront = j;
					amNewEndFront.move(j + 1, null, time);
					lang.nextStep();

					scode.unhighlight(21);
				}

//"    for(int j = newEndBack, i = j - 1; j > end; i--, j--) {" // looped
				scode.highlight(17);
				amI.move(i, null, time);
				amJ.move(j, null, time);
				lang.nextStep();
			}

//"  for(int end = newEndBack; swapped && (newEndFront < end); end = newEndBack) {" // looped
			scode.toggleHighlight(17, 3);
			amI.hide();
			amJ.hide();
			amEnd.move(newEndBack + 1, null, time);
			vis_arr.highlightCell(end + 1, newEndFront, null, null);
			vis_arr.highlightElem(end + 1, newEndFront, null, null);
			lang.nextStep();
			scode.unhighlight(3);
		}
//"// sort complete"
		scode.highlight(26);
		hbox.changeColor("color", new Color(0x00B000), null, time);
		header.changeColor("color", new Color(0x00B000), null, time);
		vis_arr.highlightCell(1, arr.length, null, null);
		vis_arr.highlightElem(1, arr.length, null, null);
		amEnd.hide();
		amNewEndBack.hide();
		amNewEndFront.hide();
	}

	private ArrayMarker buildArrayMarker(ArrayPrimitive arr, String name, Color color) {
		ArrayMarkerProperties apropse = new ArrayMarkerProperties();
		apropse.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
		apropse.set(AnimationPropertiesKeys.LABEL_PROPERTY, name);
		return lang.newArrayMarker(arr, 1, name, null, apropse);
	}

	private Square buildSwappedChkBox() {
		TextProperties tprops = new TextProperties();
		tprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 18));
		lang.newText(new Coordinates(50, 180), "swapped", "swptxt", null, tprops);
		SquareProperties sprops = new SquareProperties();
		sprops.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(0xD00000));
		sprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		sprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
		return lang.newSquare(new Coordinates(20, 183), 20, "swp", null, sprops);
	}

	private StringArray buildArray(int[] arr) {
		ArrayProperties aprops = new ArrayProperties();
		aprops.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		aprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		aprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		aprops.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, new Color(0xD00000));
		aprops.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, new Color(0x009000));
		aprops.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.GREEN);
//		aprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 18)); // does not work

		String[] v_arr = buildFixedWidthArray(arr);
		return lang.newStringArray(new Coordinates(20, 140), v_arr, "array", null, aprops);
	}

	private String[] buildFixedWidthArray(int[] arr) {
		String[] ret = new String[arr.length + 2];
		ret[0] = ret[arr.length + 1] = " ";
		int maxW = 2;
		for(int i = 0; i < arr.length; i++) {
			String t = "" + arr[i];
			maxW = ((t.length() > maxW) ? t.length() : maxW);
		}
		for(int i = 0; i < arr.length;) {
			String t = "" + arr[i];
			int d = maxW - t.length();
			for(int j = 0; j < d; j++) {
				t = " " + t + " ";
			}
			ret[++i] = t;
		}
		return ret;
	}

	private SourceCode buildSource() {
		SourceCodeProperties sprops = new SourceCodeProperties();
		sprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 16));
		sprops.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, new Color(0xD00000));
		sprops.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		SourceCode scode = lang.newSourceCode(new Coordinates(20, 200), "source", null, sprops);
		scode.addCodeLine("CocktailSort(int[] array) {", null, 0, null);
		scode.addCodeLine("boolean swapped = true;", null, 1, null);
		scode.addCodeLine("int newEndFront = 0, newEndBack = array.length - 1;", null, 1, null);
		scode.addCodeLine("for(int end = newEndBack; swapped && (newEndFront < end); end = newEndBack) {", null, 1, null);
		scode.addCodeLine("swapped = false;", null, 2, null);
		scode.addCodeLine("for(int i = newEndFront, j = i + 1; i < end; i++, j++) {", null, 2, null);
		scode.addCodeLine("if(array[i] > array[j]) {", null, 3, null);
		scode.addCodeLine("swap(array[i], array[j]);", null, 4, null);
		scode.addCodeLine("swapped = true;", null, 4, null);
		scode.addCodeLine("newEndBack = i;", null, 4, null);
		scode.addCodeLine("}", null, 3, null);
		scode.addCodeLine("}", null, 2, null);
		scode.addCodeLine("", null, 2, null);
		scode.addCodeLine("if(!swapped || newEndBack <= newEndFront) break;", null, 2, null);
		scode.addCodeLine("", null, 2, null);
		scode.addCodeLine("swapped = false;", null, 2, null);
		scode.addCodeLine("end = newEndFront;", null, 2, null);
		scode.addCodeLine("for(int j = newEndBack, i = j - 1; j > end; i--, j--) {", null, 2, null);
		scode.addCodeLine("if(array[i] > array[j]) {", null, 3, null);
		scode.addCodeLine("swap(array[i], array[j]);", null, 4, null);
		scode.addCodeLine("swapped = true;", null, 4, null);
		scode.addCodeLine("newEndFront = j;", null, 4, null);
		scode.addCodeLine("}", null, 3, null);
		scode.addCodeLine("}", null, 2, null);
		scode.addCodeLine("}", null, 1, null);
		scode.addCodeLine("}", null, 0, null);
		scode.addCodeLine("// sort complete", null, 0, null);

		return scode;
	}

	private void buildHeader() {
		TextProperties tprops = new TextProperties();
		tprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		header = lang.newText(new Coordinates(20, 20), "Cocktail Sort Optimized", "header", null, tprops);
		RectProperties rprops = new RectProperties();
		rprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		hbox = lang.newRect(new Coordinates(17, 11), new Coordinates(294, 48), "hbox", null, rprops);
	}

}