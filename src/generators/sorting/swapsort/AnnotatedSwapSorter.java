package generators.sorting.swapsort;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Text;
import algoanim.primitives.updater.ArrayMarkerUpdater;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class AnnotatedSwapSorter extends AnnotatedAlgorithm implements
		Generator {

	private String comp = "Compares";
	private String assi = "Assignments";

	private int[] arrayData;
	private IntArray array = null;
	private ArrayMarker iMarker, jMarker;
	private Timing defaultTiming = new TicksTiming(100);

	private ArrayMarkerUpdater amuI, amuJ;

	@Override
	public String getAnimationAuthor() {
		return "Sami Graja, Hasan Tercan";
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public String getAnnotatedSrc() {
		return    "public class SwapSorter {															@label(\"header\"){\n"
				+ "  public void sort(int[] sortMe){													@label(\"functionSort\")\n"
				+ "    int startwert = 0;																@label(\"vars_startwert\") @declare(\"int\", \"startwert\", \"0\") @inc(\""+assi+"\")\n"
				+ "    while (startwert < sortMe.length - 1) {											@label(\"oWhileComp\") @inc(\""+comp+"\")\n"
				+ "       int kleinere = countSmallerOnes(sortMe, startwert);							@label(\"vars_kleinere\") @declare(\"int\", \"kleinere\", \"0\") @inc(\""+assi+"\")\n"
				+ "       if (kleinere > 0) {															@label(\"if1\") @inc(\""+comp+"\")\n"
				+ "         swap(startwert,startwert + kleinere);										@label(\"swap\") @inc(\""+assi+"\") @inc(\""+assi+"\") @inc(\""+assi+"\")\n"
				+ "       }																				@label(\"if1End\")\n"
				+ "       else {																		@label(\"Else\")\n"
				+ "         startwert++;																@label(\"oStartInc\") @inc(\"startwert\") @inc(\""+assi+"\")\n" 
				+ "       }																				@label(\"elseEnd\")\n"
				+ "    }																				@label(\"elseWhile\")\n"
				+ "  }																					@label(\"functionSortEnd\")\n"
				+ " 																					@label(\"leer1\") @declare(\"int\", \"counter\", \"0\") @declare(\"int\", \"i\")\n"
				+ " 																					@label(\"leer2\")\n"
				+ "  private int countSmallerOnes(final int[] countHere, final int index) {			    @label(\"functionCountSmallerOnes\")\n"
				+ "     int counter = 0;																@label(\"counter\") @set(\"counter\", \"0\") @inc(\""+assi+"\")\n"
				+ "     for (int i = index + 1;															@label(\"iForInit\") @inc(\""+assi+"\")\n"
				+ "							i < countHere.length;										@label(\"iForComp\") @continue @inc(\""+comp+"\")\n"
				+ " 											i++) {									@label(\"iForInc\") @continue @inc(\"i\") @inc(\""+assi+"\")\n"
				+ "       if (countHere[index] > countHere[i]) {										@label(\"if2\") @inc(\""+comp+"\")\n"
				+ "          counter++;																	@label(\"counterInc\") @inc(\"counter\") @inc(\""+assi+"\")\n"
				+ "       }																				@label(\"if2End\")\n"
				+ "     }																				@label(\"iForEnd\")\n"
				+ "     return counter;																	@label(\"return\")\n"
				+ "   }																					@label(\"functionCountSmallerOnesEnd\")\n" 
				+ "}																					@label(\"end\")\n";
	}

	@Override
	public void init() {
		super.init();

		SourceCodeProperties props = new SourceCodeProperties();
		props.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
		props.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);

		// instantiate source code primitive to work with
		sourceCode = lang.newSourceCode(new Coordinates(20, 100), "sumupCode",
				null, props);
		// System.out.println(annotatedSrc);

		// setup array
		ArrayProperties iap = new ArrayProperties();
		iap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		iap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.LIGHT_GRAY);
		array = lang.newIntArray(new Coordinates(20, 70), arrayData, "array",
				null, iap);

		ArrayMarkerProperties ampI = new ArrayMarkerProperties();
		ampI.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		ampI.set(AnimationPropertiesKeys.LABEL_PROPERTY, "startwert");
		iMarker = lang.newArrayMarker(array, 0, "iMarker", null, ampI);
		amuI = new ArrayMarkerUpdater(iMarker, null, defaultTiming, array
				.getLength() - 1);

		ArrayMarkerProperties ampJ = new ArrayMarkerProperties();
		ampJ.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		ampJ.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		jMarker = lang.newArrayMarker(array, 0, "jMarker",
				null, ampJ);
		amuJ = new ArrayMarkerUpdater(jMarker, null, defaultTiming, array
				.getLength() - 1);

		// setup complexity
		vars.declare("int", comp);
		vars.setGlobal(comp);
		vars.declare("int", assi);
		vars.setGlobal(assi);

		Text text = lang.newText(new Coordinates(300, 20), "...", "complexity",
				null);
		TextUpdater tu = new TextUpdater(text);
		tu.addToken("Compares: ");
		tu.addToken(vars.getVariable(comp));
		tu.addToken(" - Assignments: ");
		tu.addToken(vars.getVariable(assi));
		tu.update();

		// parsing anwerfen
		parse();
	}

	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		// fetch Array Data from Framework
		arrayData = (int[]) primitives.get("Array Data");

		init();
		sort();

		return lang.toString();
	}

	private void sort() {
		exec("header");
		lang.nextStep();

		exec("functionSort");
		lang.nextStep();

		exec("vars_startwert");
		exec("leer1");
		amuI.setVariable(vars.getVariable("startwert"));
		amuJ.setVariable(vars.getVariable("i"));
		jMarker.hide();
		lang.nextStep();

		while (Integer.parseInt(vars.get("startwert"))<(array.getLength() - 1))
		{
			exec("vars_kleinere");
			exec("functionCountSmallerOnes");
			lang.nextStep();
			
			exec("counter");
			lang.nextStep();
			
			exec("iForInit");
			int n=Integer.parseInt(vars.get("startwert"))+1;
			vars.set("i", String.valueOf(n));
			jMarker.show();
			lang.nextStep();
			
			exec("iForComp");
			lang.nextStep();
			while (Integer.parseInt(vars.get("i")) < array.getLength()) 
			{
			int index = Integer.parseInt(vars.get("startwert"));
			int i = Integer.parseInt(vars.get("i"));
			exec("if2");
			lang.nextStep();
				if (array.getData(index) > array.getData(i)) {
				
						exec("counterInc");
						lang.nextStep();
				}
				exec("iForInc");
				lang.nextStep();
			}
			
			exec("return");
			vars.set("kleinere", vars.get("counter"));
			jMarker.hide();
			lang.nextStep();
			
			exec("if1");
			lang.nextStep();
			
			int kleinere = Integer.parseInt(vars.get("kleinere"));
			int startwert = Integer.parseInt(vars.get("startwert"));
			if(kleinere>0)
			{
				exec("swap");
				array.swap(startwert, startwert+kleinere, null, defaultTiming);
				lang.nextStep();
				lang.nextStep();
			}
			else
			{
				exec("oStartInc");
				lang.nextStep();
			}
		}
		
		iMarker.hide();
		jMarker.hide();
		// highlight all cells on end if not
		for (int i = 0; i < array.getLength(); i++)
			array.highlightCell(i, null, null);
	}

	@Override
	public String getAlgorithmName() {
		return "Swap Sort";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return "Das zu sortierende Feld wird abwechselnd nach oben und nach unten durchlaufen.\n"
				+ "Dabei werden jeweils zwei benachbarte Elemente verglichen und gegebenenfalls vertauscht.\n"
				+ "Durch diese Bidirektionalit&auml;t kommt es zu einem schnellerem Absetzen von gro&szlig;en bzw. kleinen Elementen.";
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	@Override
	public String getName() {
		return "SwapSorter [annotation]";
	}
}
