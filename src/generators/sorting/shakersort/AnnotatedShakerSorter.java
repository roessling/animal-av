package generators.sorting.shakersort;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
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

public class AnnotatedShakerSorter extends AnnotatedAlgorithm implements
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
		return "public int [] sortieren(int [] liste) 							@label(\"header\"){\n"
				+ "	int i;														@label(\"vars_marker\") @declare(\"int\", \"i\")\n"
				+ " boolean sortiert = false;									@label(\"vars_sortiert\") @declare(\"int\", \"sortiert\", \"0\") @inc(\""+assi+"\")\n"
				+ " boolean vorwaerts = true;									@label(\"vars_vorwaerts\") @declare(\"int\", \"vorwaerts\", \"1\") @inc(\""+assi+"\")\n" 
				+ " int start = 0;												@label(\"vars_start\") @declare(\"int\", \"start\", \"0\") @inc(\""+assi+"\")\n"
				+ " int ende = liste.length-1;									@label(\"vars_ende\") @declare(\"int\", \"ende\", \"4\") @inc(\""+assi+"\")\n"
				+ " while (start<ende 											@label(\"oWhileComp1\") @inc(\""+comp+"\")\n"
				+ "					&& !sortiert) {								@label(\"oWhileComp2\") @continue @inc(\""+comp+"\")\n"
				+ "   sortiert = true;											@label(\"setsortiert\") @set(\"sortiert\", \"1\") @inc(\""+assi+"\")\n" 
				+ "   if (vorwaerts) {											@label(\"if\") @inc(\""+comp+"\")\n"
				+ "     for (int i=start;										@label(\"iForInit1\") @inc(\""+assi+"\")\n"
				+ " 					i<ende;									@label(\"iForComp1\") @continue @inc(\""+comp+"\")\n"
				+ " 							i++) {							@label(\"iForInc\") @continue @inc(\"i\") @inc(\""+assi+"\")\n"
				+ "       if (liste[i] > liste[i+1]){							@label(\"if2\") @inc(\""+comp+"\")\n"
				+ "         swap(liste[i],liste[i+1]);							@label(\"swap1\") @inc(\""+assi+"\") @inc(\""+assi+"\") @inc(\""+assi+"\")\n"
				+ "         sortiert = false;									@label(\"setsortiert2\") @set(\"sortiert\", \"0\") @inc(\""+assi+"\")\n"
				+ "       }														@label(\"if2End\")\n" 
				+ "      }														@label(\"iForEnd1\")\n"
				+ "      ende--;												@label(\"oEndeDec\") @dec(\"ende\") @inc(\""+assi+"\")\n" 
				+ "    }														@label(\"if1End\")\n" 
				+ "    else {													@label(\"Else\")\n"
				+ "      for (int i=ende;										@label(\"iForInit2\") @inc(\""+assi+"\")\n"
				+ " 						i>start;							@label(\"iForComp2\") @continue @inc(\""+comp+"\")\n"
				+ " 								i--) {						@label(\"iFordec\") @continue @dec(\"i\") @inc(\""+assi+"\")\n"
				+ "        if (liste[i] < liste[i-1]) {							@label(\"if3\") @inc(\""+comp+"\")\n"
				+ "          swap(liste[i],liste[i-1]);							@label(\"swap2\") @inc(\""+assi+"\") @inc(\""+assi+"\") @inc(\""+assi+"\")\n"
				+ "          sortiert = false;									@label(\"setsortiert3\") @set(\"sortiert\", \"0\") @inc(\""+assi+"\")\n" 
				+ "         }													@label(\"if3End\")\n" 
				+ "      }														@label(\"iForEnd1\")\n"
				+ "      start++;												@label(\"oStartInc\") @inc(\"start\") @inc(\""+assi+"\")\n"  
				+ "    }														@label(\"elseEnd\")\n"
				+ "    vorwaerts = !vorwaerts;									@label(\"setvorwaertsF\") @set(\"vorwaerts\", \"0\") @inc(\""+assi+"\")\n"
				+ "  }  															@label(\"setvorwaertsT\") @set(\"vorwaerts\", \"1\") @inc(\""+assi+"\")\n"
				//+ "  }															@label(\"elseWhile\") @dec(\"ende\")\n"
				+ " return liste;												@label(\"return\")\n"
				+ "}															@label(\"end\")\n";

	}

	@Override
	public void init() {
		super.init();
	}

    public void localInit() {
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
		ampI.set(AnimationPropertiesKeys.LABEL_PROPERTY, "start");
		iMarker = lang.newArrayMarker(array, 0, "iMarker", null, ampI);
		amuI = new ArrayMarkerUpdater(iMarker, null, defaultTiming, array
				.getLength() - 1);

		ArrayMarkerProperties ampJ = new ArrayMarkerProperties();
		ampJ.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		ampJ.set(AnimationPropertiesKeys.LABEL_PROPERTY, "ende");
		jMarker = lang.newArrayMarker(array, array.getLength()-1, "jMarker", null, ampJ);
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

		localInit();
		sort();

		return lang.toString();
	}

	private void sort() {
		exec("header");
		lang.nextStep();

		exec("vars_marker");
		lang.nextStep();
		
		exec("vars_sortiert");
		lang.nextStep();
		boolean vorwaerts=true;
		exec("vars_vorwaerts");
		lang.nextStep();
		
		exec("vars_start");
		
		amuI.setVariable(vars.getVariable("start"));
		lang.nextStep();
		
		exec("vars_ende");
		vars.set("ende", String.valueOf((array.getLength()-1)));
		//exec("elseWhile");
		//System.out.println(vars.getVariable("ende"));
		lang.nextStep();
		amuJ.setVariable(vars.getVariable("ende"));
		lang.nextStep();
		/////////////////////////////////////////////////////
				System.out.println("ende="+vars.get("ende")+"  start="+vars.get("start"));
//		exec("oForInit");
//		vars.set("i", String.valueOf(array.getLength()));
//		lang.nextStep();
		exec("oWhileComp1");
		lang.nextStep();

		exec("oWhileComp2");
		lang.nextStep();
//		if (Integer.parseInt(vars.get("i")) > 0) {
		int countVorwaerts = 0;
		int countRueckwaerts = 0;
			while ("0".equals(vars.get("sortiert"))
					&& Integer.parseInt(vars.get("ende")) > Integer.parseInt(vars.get("start"))) {
				exec("setsortiert");
				lang.nextStep();
//////////////////////////////
				exec("if");
				lang.nextStep();
				if("1".equals(vars.get("vorwaerts")))
				{
					exec("iForInit1");
					vars.set("i", vars.get("start"));
					//System.out.println("i="+vars.get("i"));
					lang.nextStep();
					exec("iForComp1");
					lang.nextStep();
					while (Integer.parseInt(vars.get("i")) < (Integer.parseInt(vars.get("ende")))) {
					exec("if2");
					lang.nextStep();
					int i = Integer.parseInt(vars.get("i"));
					if (array.getData(i) > array.getData(i+1)) {
						exec("swap1");
						CheckpointUtils.checkpointEvent(this, "swap", new Variable("thisone",array.getData(i)),
                                                           new Variable("nextone",array.getData(i+1)));/////////////////
						array.swap(i, i+1, null, defaultTiming);
						lang.nextStep();
						countVorwaerts++;						
						exec("setsortiert2");
						lang.nextStep();
						
					}
				
					exec("iForInc");
					lang.nextStep();
					}
					exec("oEndeDec");
					lang.nextStep();     
				}
			 
				else {
					
					exec("iForInit2");
					vars.set("i", vars.get("ende"));
					//System.out.println("i="+vars.get("i"));
					lang.nextStep();
					exec("iForComp2");
					lang.nextStep();
					while (Integer.parseInt(vars.get("i")) > Integer.parseInt(vars.get("start"))) {
					exec("if3");
					lang.nextStep();
					int i = Integer.parseInt(vars.get("i"));
					if (array.getData(i) < array.getData(i-1)) {
						
						exec("swap2");
						CheckpointUtils.checkpointEvent(this, "swap", new Variable("thisone",array.getData(i)),
                                                           new Variable("nextone",array.getData(i-1)));/////////////////
                
						array.swap(i, i-1, null, defaultTiming);
						lang.nextStep();
						countRueckwaerts++;
						
						exec("setsortiert3");
						lang.nextStep();
					}
					
					exec("iFordec");
					lang.nextStep();
					}
					exec("oStartInc");
					lang.nextStep();
				}
				vorwaerts=!vorwaerts;
				if(vorwaerts)
				{
					exec("setvorwaertsT");
					lang.nextStep();
				}
				else
				{
					exec("setvorwaertsF");
					lang.nextStep();
				}
				
				
			}
			CheckpointUtils.checkpointEvent(this, "swap", new Variable("countVorwärts",countVorwaerts));///////////////////////
			CheckpointUtils.checkpointEvent(this, "swap", new Variable("countRückwärts",countRueckwaerts));/////////////////////
		exec("return");
		iMarker.hide();
		jMarker.hide();
		// highlight all cells on end if not
		for (int i = 0; i < array.getLength(); i++)
			array.highlightCell(i, null, null);
	}

	@Override
	public String getAlgorithmName() {
		return "Shaker Sort";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return "Das zu sortierende Feld wird abwechselnd nach oben und nach unten durchlaufen.\n"+
		"Dabei werden jeweils zwei benachbarte Elemente verglichen und gegebenenfalls vertauscht.\n"+
		"Durch diese Bidirektionalit&auml;t kommt es zu einem schnellerem Absetzen von gro&szlig;en bzw. kleinen Elementen."; 
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	@Override
	public String getName() {
		return "Shaker Sort [annotated]";
	}
}
