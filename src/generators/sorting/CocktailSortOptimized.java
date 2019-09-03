package generators.sorting;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.Square;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.updater.ArrayMarkerUpdater;
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

public class CocktailSortOptimized extends AnnotatedAlgorithm implements Generator {
	private int					arr_len	= 0;
	private Text				header;
	private Rect				hbox;
	private Timing			time;
	private StringArray	sArray;
	private ArrayMarker	iMarker, jMarker, endMarker, nBackMarker, nFrontMarker;

	public CocktailSortOptimized() {
		init();
	}

	public void sort(int[] arr) {
// "CocktailSort(int[] array) {"
		exec("header");
		show(sArray);
		hide(iMarker, jMarker, endMarker, nBackMarker, nFrontMarker);
		lang.nextStep();

//"  boolean swapped = true;"
		exec("v_swp");
		Square swp_check = buildSwappedChkBox();
		lang.nextStep();

//"  int newEndFront = 0, newEndBack = array.length - 1;"
		exec("v_nend");
		show(nFrontMarker, nBackMarker);
		int newEndFront = 0, newEndBack = arr.length - 1;
		lang.nextStep();

//"  for(int end = newEndBack; swapped && (newEndFront < end); end = newEndBack) {"
		exec("oForInit");
		show(endMarker);
		lang.nextStep();

		exec("oForCond");
		lang.nextStep();
		for(int end = newEndBack; "true".equals(vars.get("swapped")) && (end > newEndFront); end = newEndBack) {
//"    swapped = false;"
			exec("v_swp_f1");
			setSwap(swp_check, false);
			lang.nextStep();

//"    for(int i = newEndFront, j = i + 1; i < end; i++, j++) {"
			exec("iFor1Init");
			show(iMarker, jMarker);
			lang.nextStep();

			exec("iFor1Cond");
			lang.nextStep();
			for(int i = newEndFront, j = i + 1; i < end; i++, j++) {
//"      if(array[i] > array[j]) {"
				exec("if1");
				lang.nextStep();

				if(arr[i] > arr[j]) {
//"        swap(array[i], array[j]);"
					exec("swp1");
					int tmp = arr[i];
					arr[i] = arr[j];
					arr[j] = tmp;
					sArray.swap(i + 1, j + 1, null, time);
					lang.nextStep();
					CheckpointUtils.checkpointEvent(this, "swapEvent", new Variable("frontele1",arr[i]),new Variable("frontele2",arr[j]));////////////
//"        swapped = true;"
					exec("v_swp_t1");
					setSwap(swp_check, true);
					lang.nextStep();

//"        newEndBack = i;"
					exec("v_nendb");
					newEndBack = i;
					lang.nextStep();
				}
				//CheckpointUtils.checkpointEvent(this, "suchenNewEnd", new Variable("endback",newEndBack));////////////
//"    for(int i = newEndFront, j = i + 1; i < end; i++, j++) {" // looped
				exec("iFor1Next");
				lang.nextStep();

				exec("iFor1Cond");
				lang.nextStep();
			}

//"    if(!swapped || newEndBack <= newEndFront) break;"
			exec("break");
			hide(iMarker, jMarker);
			sArray.highlightCell(newEndBack + 2, end + 1, null, null);
			sArray.highlightElem(newEndBack + 2, end + 1, null, null);
			lang.nextStep();
			if("false".equals(vars.get("swapped")) || newEndBack <= newEndFront) {
				break;
			}
			CheckpointUtils.checkpointEvent(this, "suchenNewEnd", new Variable("endback",newEndBack),new Variable("endfront",newEndFront));////////////
//"    swapped = false;"
			exec("v_swp_f2");
			setSwap(swp_check, false);
			lang.nextStep();

//"    end = newEndFront;"
			exec("v_end");
			end = newEndFront;
			lang.nextStep();
			//CheckpointUtils.checkpointEvent(this, "suchEnd", new Variable("newEnd",end));////////////
//"    for(int j = newEndBack, i = j - 1; j > end; i--, j--) {"
			exec("iFor2Init");
			show(iMarker, jMarker);
			lang.nextStep();

			exec("iFor2Cond");
			lang.nextStep();
			for(int j = newEndBack, i = j - 1; j > end; i--, j--) {
//"      if(array[i] > array[j]) {"
				exec("if2");
				lang.nextStep();

				if(arr[i] > arr[j]) {
//"        swap(array[i], array[j]);"
					exec("swp2");
					int tmp = arr[i];
					arr[i] = arr[j];
					arr[j] = tmp;
					sArray.swap(i + 1, j + 1, null, time);
					lang.nextStep();
				  CheckpointUtils.checkpointEvent(this, "swapEvent", new Variable("backele1",arr[i]),new Variable("backele2",arr[j]));////////////
//"        swapped = true;"
					exec("v_swp_t2");
					setSwap(swp_check, true);
					lang.nextStep();

//"        newEndFront = j;"
					exec("v_nendf");
					newEndFront = j;
					lang.nextStep();
					//CheckpointUtils.checkpointEvent(this, "suchenNewEnd", new Variable("endfront",newEndFront));////////////
				}

//"    for(int j = newEndBack, i = j - 1; j > end; i--, j--) {" // looped
				exec("iFor2Next");
				lang.nextStep();

				exec("iFor2Cond");
				lang.nextStep();
			}

//"  for(int end = newEndBack; swapped && (newEndFront < end); end = newEndBack) {" // looped
			exec("oForNext");
			hide(iMarker, jMarker);
			sArray.highlightCell(end + 1, newEndFront, null, null);
			sArray.highlightElem(end + 1, newEndFront, null, null);
			lang.nextStep();
			
			exec("oForCond");
			lang.nextStep();
		}

//"// sort complete"
		exec("end");
		hbox.changeColor("color", new Color(0x00B000), null, time);
		header.changeColor("color", new Color(0x00B000), null, time);
		sArray.highlightCell(1, arr.length, null, null);
		sArray.highlightElem(1, arr.length, null, null);
		hide(endMarker, nBackMarker, nFrontMarker);
	}

	private void hide(Primitive... prims) {
		for(Primitive prim : prims) {
			prim.hide();
		}
	}

	private void show(Primitive... prims) {
		for(Primitive prim : prims) {
			prim.show();
		}
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

	private void setSwap(Square chkBox, boolean checked) {
		if(checked) {
			chkBox.changeColor("color", new Color(0xD00000), null, time);
			chkBox.changeColor("fillColor", Color.RED, null, time);
		} else {
			chkBox.changeColor("color", new Color(0x009000), null, time);
			chkBox.changeColor("fillColor", Color.WHITE, null, time);
		}
	}

	private ArrayMarker buildArrayMarker(String var, ArrayMarkerProperties aprops) {
		vars.declare("int", var);
		ArrayMarker aM = lang.newArrayMarker(sArray, 1, (String)aprops.get(AnimationPropertiesKeys.LABEL_PROPERTY), null, aprops);
		ArrayMarkerUpdater aMU = new ArrayMarkerUpdater(aM, null, time, arr_len + 1);
		aMU.setVariable(vars.getVariable(var));
		aM.hide();
		return aM;
	}

	private void buildArray(int[] arr, ArrayProperties aprops) {
		String[] v_arr = buildFixedWidthArray(arr);
		sArray = lang.newStringArray(new Coordinates(20, 140), v_arr, "array", null, aprops);
		sArray.hide();
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

	private void buildSource(SourceCodeProperties sprops) {
		sourceCode = lang.newSourceCode(new Coordinates(20, 200), "source", null, sprops);
	}

	private void buildHeader() {
		TextProperties tprops = new TextProperties();
		tprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		header = lang.newText(new Coordinates(20, 20), "Cocktail Sort Optimized", "header", null, tprops);
		RectProperties rprops = new RectProperties();
		rprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		hbox = lang.newRect(new Coordinates(17, 11), new Coordinates(294, 48), "hbox", null, rprops);
	}

	@Override
	public String generate(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1) {
		init();
		int[] arr = (int[])arg1.get("Array Werte");
		arr_len = arr.length;
		time = new TicksTiming(100);
		buildHeader();
		buildSource((SourceCodeProperties)arg0.getPropertiesByName("QuellText Darstellung"));
		buildArray(arr, (ArrayProperties)arg0.getPropertiesByName("Array Darstellung"));
		nBackMarker = buildArrayMarker("newEndBack", (ArrayMarkerProperties)arg0.getPropertiesByName("newEndBack Marker"));
		nFrontMarker = buildArrayMarker("newEndFront", (ArrayMarkerProperties)arg0.getPropertiesByName("newEndFront Marker"));
		endMarker = buildArrayMarker("end", (ArrayMarkerProperties)arg0.getPropertiesByName("end Marker"));
		iMarker = buildArrayMarker("i", (ArrayMarkerProperties)arg0.getPropertiesByName("i Marker"));
		jMarker = buildArrayMarker("j", (ArrayMarkerProperties)arg0.getPropertiesByName("j Marker"));
		vars.declare("String", "swapped");
		parse();
		lang.nextStep();
		sort(arr);
		return lang.toString();
	}

	private final static String	AUTHOR			= "Wilhelm 'sOph' Retz";
	private final static String	NAME				= "Cocktail Sort Optimized";
	private final static String	DESCRIPTION	= "Bei dem Cocktailsort handelt es sich um einen Bidirektionalen Bubblesort "
																					+ "Algorithmus, d.h. es erfolgt abwechselnd ein Durchlauf in Richtung und "
																					+ "Gegenrichtung des zu sortierenden Arrays.<br\\>"
																					+ "Ein Durchlauf sieht so aus, dass immer zwei benachbarte Elemente mit "
																					+ "einander verglichen werden, ist das linke Element im Array größer, werden "
																					+ "die Elemente vertauscht. Nach jedem Vergleich geht es im Array eine "
																					+ "Position weiter (entweder Hin- oder Gegenrichtung).<br\\>"
																					+ "Die Optimierung liegt darin, dass ein Durchlauf bei der Position des "
																					+ "letzten Umtausches der Gegenrichtung beginnt und der Position des letzten "
																					+ "Umtausches (des vorherigen Durchlaufes) der selben Richtung endet.";

	@Override
	public String getAnnotatedSrc() {
		return "public void cocktailSort(int[] arr) {@label(\"header\")\n"
		+ " boolean swapped = true;	@label(\"v_swp\")@set(\"swapped\", \"true\")\n"
		+ " int newEndFront = 0, newEndBack = array.length - 1;@label(\"v_nend\")@set(\"newEndFront\", \"1\")"
		+ "@set(\"newEndBack\", \"" + arr_len + "\")\n"
		+ " for(int end = newEndBack;	@label(\"oForInit\")@eval(\"end\", \"newEndBack\")\n"
		+ "swapped && (newEndFront < end);@label(\"oForCond\")@continue\n"
		+ "end = newEndBack) {@label(\"oForNext\")@continue@eval(\"end\", \"newEndBack\")\n"
		+ "  swapped = false;@label(\"v_swp_f1\")@set(\"swapped\", \"false\")\n"
		+ "  for(int i = newEndFront, j = i + 1;@label(\"iFor1Init\")@eval(\"i\", \"newEndFront\")@eval(\"j\", \"i + 1\")\n"
		+ "i < end;@label(\"iFor1Cond\")@continue\n"
		+ "i++, j++) {@label(\"iFor1Next\")@continue@inc(\"i\")@inc(\"j\")\n"
		+ "   if(array[i] > array[j]) {@label(\"if1\")\n"
		+ "    swap(array[i], array[j]);@label(\"swp1\")\n"
		+ "    swapped = true;@label(\"v_swp_t1\")@set(\"swapped\", \"true\")\n"
		+ "    newEndBack = i;@label(\"v_nendb\")@eval(\"newEndBack\", \"i\")\n"
		+ "   }@label(\"none1\")\n"
		+ "  }@label(\"none2\")\n\n"
		+ "  if(!swapped || newEndBack <= newEndFront) break;@label(\"break\")\n\n"
		+ "  swapped = false;@label(\"v_swp_f2\")@set(\"swapped\", \"false\")\n"
		+ "  end = newEndFront;@label(\"v_end\")@eval(\"end\", \"newEndFront\")\n"
		+ "  for(int j = newEndBack, i = j - 1;@label(\"iFor2Init\")@eval(\"j\", \"newEndBack\")@eval(\"i\", \"j - 1\")\n"
		+ "j > end;@label(\"iFor2Cond\")@continue\n"
		+ "i--, j--) {@label(\"iFor2Next\")@continue@dec(\"i\")@dec(\"j\")\n"
		+ "   if(array[i] > array[j]) {@label(\"if2\")\n"
		+ "    swap(array[i], array[j]);@label(\"swp2\")\n"
		+ "    swapped = true;@label(\"v_swp_t2\")@set(\"swapped\", \"true\")\n"
		+ "    newEndFront = j;@label(\"v_nendf\")@eval(\"newEndFront\", \"j\")\n"
		+ "   }@label(\"none3\")\n"
		+ "  }@label(\"none4\")\n"
		+ " }@label(\"none5\")\n"
		+ "}@label(\"none6\")\n"
		+ "// sort complete@label(\"end\")";

	}

	@Override
	public String getAlgorithmName() {
		return "Cocktail Sort";
	}

	@Override
	public String getAnimationAuthor() {
		return AUTHOR;
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}
}