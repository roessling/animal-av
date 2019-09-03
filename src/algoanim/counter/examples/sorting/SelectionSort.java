package algoanim.counter.examples.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;

/**
 * 
 * @author Enkh-Amgalan Ganbaatar & Martin Tjokrodiredjo
 *
 */
public class SelectionSort implements generators.framework.Generator{
	
	
	
	private Language lang;
	
	private static final String DESCRIPTION = "Selection sort is a sorting algorithm, " +
												"specifically an in-place comparison sort. " +
												"It has O(n2) complexity, making it inefficient on large lists, " +
												"and generally performs worse than the similar insertion sort. " +
												"Selection sort is noted for its simplicity, " +
												"and also has performance advantages over more " +
												"complicated algorithms in certain situations."+
												" source: www.en.wikipedia.org";
	
	private static final String SOURCE_CODE = 
	   "public void selectionSort(int[] a) {\n"			
	+  "  for (int i = 0; i < a.length-1; i++) {\n"
	+  "    int min = i;\n"
	+  "    for (int j = i + 1; j < a.length; j++) {\n"
	+  "      if (a[j] < a[min]) {\n"
	+  "        min = j;\n"
	+  "      }\n"
	+  "    }\n"
	+  "    swap(i,min);\n"
	+  "  }\n"
	+  "}";
	
	public SelectionSort(){
		lang = new AnimalScript(getAlgorithmName(),getAnimationAuthor(),720,480);
		lang.setStepMode(true);
	}

	
	/**
	 * Selection Sort : sorting algorithm on the parameter IntArray array
	 * @param array
	 * @param code
	 * @throws LineNotExistsException
	 */
	private void selectionSort(IntArray array,SourceCode code, ArrayMarker iMarker , ArrayMarker jMarker) throws LineNotExistsException {
		// highlight first line.
		code.highlight(0);
		lang.nextStep();
		// toggle highlight to next line.
		code.toggleHighlight(0, 1);
		// set arrayMarker i on position 0.
		
		lang.nextStep();
		
		jMarker.hide();
	
		for(int i = 0 ;i < array.getLength() - 1; i++ ){
			lang.nextStep();
			//move iMarker to position.
			iMarker.move(i, null, new TicksTiming(10));
			// toggle to next line.
			code.toggleHighlight(1, 2);
					
			int min = i;
			
			lang.nextStep();
			code.toggleHighlight(2, 3);
			
			// set arrayMarker j on position i+1.
						
			lang.nextStep();
			
			// begin inner loop.
			for(int j=i+1; j < array.getLength(); j++){
				// move jMarker to next position
				jMarker.show();
				jMarker.move(j, null, new TicksTiming(10));
				
				lang.nextStep();
				
				code.toggleHighlight(3,4);
				
				lang.nextStep();
				// highlighting compared array cells.
				array.highlightCell(j, null, new TicksTiming(10));
				array.highlightCell(min, null, new TicksTiming(10));
				
				if(array.getData(j) < array.getData(min)){
					
					lang.nextStep();
					array.unhighlightCell(min, null, new TicksTiming(10));
					// new minimal element on index j.
					min = j;
					code.toggleHighlight(4,5);
					
					lang.nextStep();
					
					code.toggleHighlight(5,6);
				}
				else {
					lang.nextStep();
					code.toggleHighlight(4,6);
					array.unhighlightCell(j, null, new TicksTiming(10));
				}
				lang.nextStep();
				code.toggleHighlight(6,7);
				lang.nextStep();
				code.toggleHighlight(7,3);
			}
			// minimal element founded.Now starting to swap index i with min.
			code.toggleHighlight(3,8);
			
			lang.nextStep();
			// swapping array elements.
			array.swap(i, min,null, new TicksTiming(10));
			array.unhighlightCell(i, null, new TicksTiming(10));
			array.highlightElem(i, null, new TicksTiming(10));
			
			lang.nextStep();
			code.toggleHighlight(8,1);
			
		}
		code.toggleHighlight(1,9);
		lang.nextStep();
		code.toggleHighlight(9,10);
		
			
	}

	/**
	 * Here generated an animal script as String Object.User can configure Array- and SourceCode 
	 * properties as well through input parameter props.
	 * 
	 *  at index 0 : SourceCode properties
	 *  at index 1 : ArrayProperties
	 *  
	 *  @param props User defined properties.
	 *  @param primitives given primitives, arrays etc.
	 *
	 *  @return generated animalScript 
	 */
	@Override
	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		
		init();
	
		// creating a source code
		SourceCode sc = lang.newSourceCode(new Coordinates(40 , 140),"sourceCode",null,(SourceCodeProperties) props.getPropertiesByName("sourceCode"));
		
		// adding lines
		addLines(sc);
		
		// now, create the IntArray object, linked to the properties
		IntArray ia = lang.newIntArray(new Coordinates(20, 100),  (int[]) primitives.get("IntArray"), "IntArray", 
				null, (ArrayProperties) props.getPropertiesByName("IntArray"));
		
		ArrayMarker marker1 = lang.newArrayMarker(ia, 0, "i", null,(ArrayMarkerProperties) props.getPropertiesByName("iMarker"));
		
		ArrayMarker marker2 = lang.newArrayMarker(ia, 0, "j", null , (ArrayMarkerProperties) props.getPropertiesByName("jMarker"));
		
		lang.nextStep();
		try {
			
			ia.show();
			
			selectionSort(ia, sc ,marker1 , marker2);
		} catch (LineNotExistsException e) {
			e.printStackTrace();
		}
		
		return lang.toString();
	}
	
	
	private void addLines(SourceCode sc){
		sc.addCodeLine("public void selectionSort(int[] a)", null, 0, null);
		sc.addCodeLine("for(int i=0; i < a.length-1; i++) {", null, 1, null);
		sc.addCodeLine("int min=i;", null, 2, null);
		sc.addCodeLine("for(int j=i+1; j < a.length; j++) {", null, 2, null);
		sc.addCodeLine("if (a[j] < a[min]) {", null, 3, null);
		sc.addCodeLine("min = j;", null, 4, null);
		sc.addCodeLine("}", null, 3, null);
		sc.addCodeLine("}", null, 2, null);
		sc.addCodeLine("swap(a[i],a[min]);", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("}", null, 0, null);
	}
	
	public void setLang(Language aLang) {
		lang = aLang;
	}
	
	public static String getDESCRIPTION() {
		return DESCRIPTION;
	}
	
	@Override
	public String getAlgorithmName() {
		return "Selection Sort";
	}

	@Override
	public String getCodeExample() {
		return SOURCE_CODE;
	}
	
	public String getAnimationAuthor(){
		return "Enkh-Amgalan Ganbaatar, Martin Tjokrodiredjo";
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
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}

	@Override
	public String getName() {
		return "Selection Sort";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	public void init(){
		
		lang = new AnimalScript(getAlgorithmName(),getAnimationAuthor(),720,480);
		
		lang.setStepMode(true);
	}
	public Language getLang(){
		return lang;
	}


	
}

