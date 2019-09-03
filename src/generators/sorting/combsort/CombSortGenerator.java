package generators.sorting.combsort;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;


public class CombSortGenerator extends AnnotatedAlgorithm implements generators.framework.Generator {
	public Language lang;
//	private SourceCode sc;
	private ArrayProperties arrayProps;
	private TextProperties textProps;
	private ArrayMarkerProperties arrayMarkerProps;
	private SourceCodeProperties sourceProps;
	
	private String comp = "Vergleiche";
	private String divid = "Divisionen";
	private String assi = "Zuweisungen";
	
	private IntArray sortArray;
	
	
	public CombSortGenerator() {
		init();		
	}
	
	@Override
	public String getAnnotatedSrc() {
		String code = 
		"schritt := Laenge (A)				 							@label(\"line0\") @inc(\""+assi+"\")\n"+
		"wiederhole solange (vertauscht == true oder schritt > 1) {  	@label(\"line1\") @inc(\""+comp+"\") @inc(\""+comp+"\")  \n"+
		"	vertauscht = false												@label(\"line2\") @inc(\""+assi+"\")\n"+
		"	falls (schritt > 1) dann schritt := Math.floor ( schritt/1.3 ) @label(\"line3\") @inc(\""+comp+"\") @inc(\""+assi+"\") @inc(\""+divid+"\")\n"+
		"	fuer jedes i von 0 bis (Laenge ( A ) - schritt) wiederhole { 	@label(\"line4\") @inc(\""+comp+"\") @inc(\""+assi+"\")\n"+
		"		falls ( A[i] > A[i + schritt]) dann { 							@label(\"line5\") @inc(\""+comp+"\")\n"+
		"			vertausche ( A [i], A [i + schritt] ) 							@label(\"line6\") @inc(\""+assi+"\") @inc(\""+assi+"\") @inc(\""+assi+"\")\n"+
		"			vertauscht := true @label(\"line7\") 							@inc(\""+assi+"\") \n"+
		"		}																@label(\"line8\")\n"+
		"	}																@label(\"line9\")\n"+
		"}																@label(\"line10\")\n";
		return code;
	}
	
	public void startsort(int[] sortme) {
//		Text headline = 
	  lang.newText(new Coordinates(20,20), "CombSort Sortieralgorithmus ", "headline",null, textProps);
		sortArray = lang.newIntArray(new Coordinates(20, 100), sortme, "sortme", null, arrayProps);
	    
		sourceCode = lang.newSourceCode(new Coordinates(20, 220), "code", null, sourceProps);
		/*sc.addCodeLine("schritt := Laenge (A)", null, 0, null);
		sc.addCodeLine("wiederhole solange (vertauscht == true oder schritt > 1) {", null, 0, null);
		sc.addCodeLine("vertauscht = false", "line2", 2, null);
		sc.addCodeLine("falls (schritt > 1) dann schritt := Math.floor ( schritt/1.3 )", "line3", 2, null);
		sc.addCodeLine("fuer jedes i von 0 bis (Laenge ( A ) - schritt) wiederhole {", "line4", 2, null);
		sc.addCodeLine("falls ( A[i] > A[i + schritt]) dann {", null, 4, null);
		sc.addCodeLine("vertausche ( A [i], A [i + schritt] )", null, 6, null);
		sc.addCodeLine("vertauscht := true", null, 6, null);
		sc.addCodeLine("}", null, 4, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("}", null, 0, null);
		*/
		
		// setup complexity
		vars.declare("int", comp); vars.setGlobal(comp);
		vars.declare("int", assi); vars.setGlobal(assi);
		vars.declare("int", divid); vars.setGlobal(divid);
		
		Text text = lang.newText(new Coordinates(300, 20), "...", "complexity", null);
		TextUpdater tu = new TextUpdater(text);
		tu.addToken("Vergleiche: ");
		tu.addToken(vars.getVariable(comp));
		tu.addToken(" - Zuweisungen: ");
		tu.addToken(vars.getVariable(assi));
		tu.addToken(" - Divisionen: ");
		tu.addToken(vars.getVariable(divid));
		tu.update();
		
		// parsing anwerfen
		parse();
		
		combsort(sortArray);
	}
	
	public void combsort(IntArray sortArray) {

		 Timing changeTime = new TicksTiming(20);
		 Timing preOne = new TicksTiming(50);
		 
		ArrayMarker jMarker = lang.newArrayMarker(sortArray, 0, "j",  null, arrayMarkerProps);
		arrayMarkerProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		ArrayMarker iMarker = lang.newArrayMarker(sortArray, 0, "i",  null, arrayMarkerProps);
		 
		
		int step =  sortArray.getLength();
		//sc.highlight(0);
		exec("line0");
		boolean swapped = false;
		Text steptext = lang.newText(new Coordinates(20,150), "Schrittgroesse: "+step, "step",null, textProps);
		Text swappedtext = lang.newText(new Coordinates(20,170), "vertauscht: "+swapped, "step",null, textProps);
		lang.nextStep();
		
		//sc.unhighlight(0);
		while (swapped || step>1) {
			//sc.highlight(1);
			exec("line1");
			lang.nextStep();
			
			swapped = false;
			//sc.toggleHighlight(1,2);
			exec("line2");
			swappedtext.setText("swapped: "+swapped, null, changeTime);
			lang.nextStep();
			
			if(step>1) step = (int) Math.floor(step/1.3);
			steptext.setText("Schrittgroesse: "+step, null, changeTime);
			//sc.toggleHighlight(2, 3);
			exec("line3");
			lang.nextStep();
			
			for(int i=0; i<sortArray.getLength()-step; i++) {
				//sc.toggleHighlight(3,4);
				exec("line4");
				lang.nextStep();
				
				iMarker.move(i, null, changeTime);
				sortArray.highlightCell(i, null, changeTime);
				lang.nextStep();
				jMarker.move(i+step, null, preOne);
				sortArray.highlightCell(i+step, preOne, changeTime);
				//sc.toggleHighlight(4, 5);
				exec("line5");
				lang.nextStep();
				
				if(sortArray.getData(i)>sortArray.getData(i+step)) {
					int tmp = sortArray.getData(i);
					sortArray.put(i, sortArray.getData(i+step), null, null);
					//sc.toggleHighlight(5, 6);
					exec("line6");
					lang.nextStep();
					sortArray.put(i+step, tmp, null, null);
					swapped = true;
					swappedtext.setText("vertauscht: "+swapped, null, changeTime);
					//sc.toggleHighlight(6,7);
					exec("line7");
					lang.nextStep();
					
				}
				sortArray.unhighlightCell(i, null, null);
				sortArray.unhighlightCell(i+step, null, null);
				lang.nextStep();
				//sc.unhighlight(7);
				//sc.unhighlight(5);
			}
		}
	}
	
	public void printarray(int[] a) {
		for(int i=0; i<a.length; i++) {
			System.out.print(a[i]+"  ");
		}
		System.out.println();
	}
	
	/*
	 * 				################################################## GENERATE ######################
	 * (non-Javadoc)
	 * @see generator.Generator#generate(generator.properties.AnimationPropertiesContainer, java.util.Hashtable)
	 */
	@Override
	public String generate(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1) {
		int sortme[] = {38, 5, 18, 42, 6};
		
		if(arg1.containsKey("ArrayProperties")) this.arrayProps = (ArrayProperties) arg1.get("ArrayProperties");
		if(arg1.containsKey("ArrayMarkerProperties")) this.arrayMarkerProps = (ArrayMarkerProperties) arg1.get("ArrayMarkerProperties");
		if(arg1.containsKey("SourceCodeProperties")) this.sourceProps = (SourceCodeProperties) arg1.get("SourceCodeProperties");
		if(arg1.containsKey("TextProperties")) this.textProps = (TextProperties) arg1.get("TextProperties");		
		if(arg1.containsKey("intArray")) sortme = (int[]) arg1.get("intArray");
		
		this.startsort(sortme);
		return(lang.toString());

	}

	@Override
	public String getAlgorithmName() {
		return "Comb Sort";
	}

	@Override
	public String getAnimationAuthor() {
		return "Sebastian Kauschke, Manuel Pistner";
	}

	@Override
	public String getCodeExample() {
		return "";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	@Override
	public String getDescription() {
		return "Demonstriert den Combsort Algorithmus.";
	}

	@Override
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	@Override
	public String getName() {
		return "Comb Sort Generator";
	}

	@Override
	public String getOutputLanguage() {
		return PSEUDO_CODE_OUTPUT;
	}

	@Override
	public void init() {
		super.init();
		lang = new AnimalScript("CombSortGenerator", "Sebastian Kauschke, Manuel Pistner", 800, 500);
		lang.setStepMode(true);
		
		arrayProps = new ArrayProperties();
	    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	    arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);   
	    arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, 
	        Color.BLACK);
	    arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, 
	        Color.RED);
	    arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, 
	        Color.YELLOW);
	    
	    arrayMarkerProps = new ArrayMarkerProperties();
		arrayMarkerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		textProps = new TextProperties();
		textProps.set("color", Color.BLACK);
		
		sourceProps = new SourceCodeProperties();
		sourceProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		sourceProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 
	        Font.PLAIN, 12));

		sourceProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, 
	        Color.RED);   
		sourceProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
	
		
	
		
	}
}