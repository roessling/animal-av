import java.awt.Color;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.util.ArrayDisplayOptions;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class LineareSuche {

	private Language lang;
	private DisplayOptions disOp = new TicksTiming(0);
	private IntArray intArray;
	private ArrayMarker arrayMarker;
	private TwoValueCounter counter;
//	private TwoValueView view;

	public static void main(String[] args) {
		Language l = new AnimalScript("Lineare Suche Animation",
				"Axel Heimann", 1280, 720);
		LineareSuche ls = new LineareSuche(l);

		ls.init();
		int i = ls.search(3, ls.intArray);
		ls.showResult(i);
		System.out.println(ls.lang.toString());
	}

	public LineareSuche(Language l) {
		lang = l;
		lang.setStepMode(true);
	}

	public void init() {
		lang = new AnimalScript("Lineare Suche Animation", "Axel Heimann",
				1280, 720);
		lang.setStepMode(true);

		ArrayDisplayOptions arrayDisOp = new ArrayDisplayOptions(null, null,
				false);
		int[] content = { 1, 2, 3, 4 };
		intArray = lang.newIntArray(new Coordinates(100, 100), content,
				"intArray", arrayDisOp);
		counter = lang.newCounter(intArray);
		CounterProperties cp = new CounterProperties();
		String[] valueNames = { "abc", "lalalala" };
//		view =
		lang.newCounterView(counter, new Coordinates(10, 10), cp, true, true, valueNames);
		lang.nextStep();
		int[][] mx = new int[][]{{1,2},{3,4}};
    
		MatrixProperties mp = new MatrixProperties();
		mp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		mp.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
		mp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
		IntMatrix srcPic = lang.newIntMatrix(new Coordinates(0, 0), mx, "srcPic", null, mp);

		lang.nextStep();
		srcPic.setGridFillColor(0, 0, Color.PINK, Timing.INSTANTEOUS,
		    Timing.INSTANTEOUS);
		lang.nextStep();

	}

	public void showResult(int i) {
		lang.newText(new Coordinates(200, 200), "Element gefunden an Position "
				+ (i + 1), "Result", disOp);
	}

	public int search(int searched, IntArray data) {
		arrayMarker = lang.newArrayMarker(intArray, 0, "ArrayMarker", disOp);
		for (int i = 0; i < data.getLength(); i++) {
			lang.nextStep();
			arrayMarker.increment(null, null);
			if (data.getData(i) == searched) {
				lang.newText(new Coordinates(100, 200),
						"" + counter.getAccess(), "Access", disOp);
				return i;
			}
		}
		return -1;
	}
}
