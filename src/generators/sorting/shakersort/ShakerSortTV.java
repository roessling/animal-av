package generators.sorting.shakersort; 
import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.updater.TextUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;

/**
 * @author Tsvetoslava Vateva 
 *
 */
public class ShakerSortTV extends AnnotatedAlgorithm implements generators.framework.Generator {
	
	private String comp = "Compares";
	private String assi = "Assignments";

	private int[] arrToSort;						//the array to be sorted
	private AnimationPropertiesContainer container;
	private Hashtable<String, Object> primitives;
	private ArrayProperties arrProperties;
	private IntArray arrayToSort;							//the visualisation of the array to be sorted
	private ArrayMarkerProperties arrayFirstProps;
	private ArrayMarkerProperties arrayLastProps;
	private ArrayMarkerProperties arrayIProps;
	private ArrayMarkerProperties arrayJProps;
	private ArrayMarker firstMarker;
	private ArrayMarker lastMarker;
	private ArrayMarker iMarker;
	private ArrayMarker jMarker;
	private SourceCodeProperties titelProperties;
	private SourceCodeProperties shakerSortProperties;
	private SourceCodeProperties endProperties;
	private SourceCode end;
	private SourceCode titel;

	
	
	private void setProperties(){
		titelProperties = (SourceCodeProperties) container.getPropertiesByName("titel");
		shakerSortProperties = (SourceCodeProperties)container.getPropertiesByName("source");
		arrayIProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		arrayIProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
		arrayJProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
		arrayJProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
		arrayFirstProps = (ArrayMarkerProperties)container.getPropertiesByName("markerF");
		arrayLastProps = (ArrayMarkerProperties)container.getPropertiesByName("markerL");
		arrProperties = (ArrayProperties)container.getPropertiesByName("array");
	}
	
	
	
	
	private void markAllAsFinished(IntArray arrayToSort){
		for(int i=0; i<=arrToSort.length-1; i++)
			arrayToSort.highlightElem(i, null, null);
		firstMarker.hide();
		lastMarker.hide();
		if(iMarker != null)
			iMarker.hide();
		if(jMarker != null)
			jMarker.hide();
		end = lang.newSourceCode(new Coordinates(400,250),"end" , null,endProperties);
		end.addCodeLine("The array is sorted", "sorted", 0, null);
		
	}
	
	
	
	public String getAlgorithmName() {
		return "Shaker Sort";
	}


	
	public String getAnimationAuthor() {
		return "Tsvetoslava Vateva";
	}


	public String  getAnnotatedSrc() {
		
		String buffer = new String();
		buffer = buffer + "ShakerSort(int []arr) 				@label(\"header\") \n";
		buffer = buffer + " int f = 0; 							@label(\"f0\") @inc(\""+assi+"\")\n";
		buffer = buffer + " int l = arr.length-1; 				@label(\"l\")  @inc(\""+assi+"\")\n";
		buffer = buffer + " boolean swapped = false;			@label(\"resetSwapped0\") @inc(\""+assi+"\")\n";
		buffer = buffer + "  do 								@label(\"do\") \n";
		buffer = buffer + "    for (int i=f; i<l; i++) 			@label(\"for1\") @inc(\""+assi+"\") @inc(\""+comp+"\") \n";
		buffer = buffer + "      if (arr[i] > arr[i+1]) 		@label(\"if1\")  @inc(\""+comp+"\") \n";
		buffer = buffer + "        swap(i,(i+1)); 				@label(\"swap1\")  \n";
		buffer = buffer + "        swapped = true; 				@label(\"setSwapped1\") @inc(\""+assi+"\")  \n";
		buffer = buffer + "    if (swapped == false) 			@label(\"if2\")  @inc(\""+comp+"\") \n";
		buffer = buffer + "      return; 						@label(\"return1\")\n";
		buffer = buffer + "    else 							@label(\"else2\")\n";
		buffer = buffer + "      l--; 							@label(\"decL\") @inc(\""+assi+"\")\n";	
		buffer = buffer + "      swapped = false; 				@label(\"resetSwapped1\") @inc(\""+assi+"\")\n";	
		buffer = buffer + "    for (int j=l; j>f; j--) 			@label(\"for2\") @inc(\""+assi+"\") @inc(\""+comp+"\") \n";
		buffer = buffer + "      if (arr[j-1] > arr[j]) 		@label(\"if3\")  @inc(\""+comp+"\") \n";
		buffer = buffer + "         swap((j-1),j); 				@label(\"swap2\")  \n";  							
		buffer = buffer + "         swapped = true; 			@label(\"setSwapped2\") @inc(\""+assi+"\")  \n"; 
		buffer = buffer + "    if (swapped == false) 			@label(\"if4\")  @inc(\""+comp+"\") \n";				
		buffer = buffer + "      return; 						@label(\"return2\")\n";  									
		buffer = buffer + "    else 	  						@label(\"else4\")\n";				
		buffer = buffer + "      f++; 							@label(\"incF\") @inc(\""+assi+"\")\n"; 										
		buffer = buffer + "      swapped = false; 				@label(\"resetSwapped2\") @inc(\""+assi+"\")\n";				
		buffer = buffer + "   while(f<l); 						@label(\"while\") @inc(\""+comp+"\")\n";	
		return buffer;
	}
	 
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}


	
	public String getDescription() {
		return "Implementierung des Sortierverfahrens Shaker Sort. Zwei Zeiger sind verwendet um die sortierten" +
				"Blöcke zu markieren. In jeder Interation wird das größte Element von den nicht sortierten " +
				"Elementen nach rechts geschoben und das kleinste - nach links und somit werden die beiden " +
				"Zeiger auch verschoben bis sie auf dem gleichen Element zeigen. Dann ist das Array sortiert."  ;
	}


	
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}


	
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
	}


	
	public String getName() {
		return "ShakerSort";
	}


	
	/*
	 * the sorting algorithm and the animation details
	 * @param selectionSourceCode - the Source Code of the function selectionSort that should be animated
	 * @param minSourceCode - the Source Code of the function min that should be animated
	 * @param arrayToSort- the array that is to be sorted
	 */
	private void shakerSort(IntArray arrayToSort){
				 
		//highlight the name of the function in the PseudoCode
		exec("header");
		
		int first = 0;
		lang.nextStep();
		exec("f0");
		//adding a pointer to the array
		firstMarker = lang.newArrayMarker(arrayToSort, first, "f", null, arrayFirstProps);

		
		int last = arrToSort.length - 1;
		lang.nextStep();
		lastMarker = lang.newArrayMarker(arrayToSort, last , "l", null, arrayLastProps);
		exec("l");
		
		boolean swapped = false;
		lang.nextStep();
		exec("resetSwapped0");	
		lang.nextStep();							
		sourceCode.unhighlight(3);					
		do{
			exec("do");	
			
			lang.nextStep();
			exec("for1");		
			firstMarker.hide();
			//arrayIProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i=f");//for-loop
			iMarker = lang.newArrayMarker(arrayToSort, first, "i", null, arrayIProps);
			
			for(int i=first; i<last; i++){
				if(first!=i){
					iMarker.move(i,new MsTiming(100), null);
					firstMarker.show();	
				}

				lang.nextStep();
				exec("if1"); 
				arrayToSort.highlightCell(i, null, null);
				arrayToSort.highlightCell((i+1), null, null);
				lang.nextStep();
				if(arrToSort[i]>arrToSort[i+1]){
					exec("swap1"); 
					arrayToSort.swap(i, (i+1), null, new MsTiming(450));
					lang.nextStep();
					exec("setSwapped1"); 								
					swapped = true;
					lang.nextStep();
					exec("for1"); 
					arrayToSort.unhighlightCell(i, null, null);
					arrayToSort.unhighlightCell((i+1), null, null);
					lang.nextStep();
				}
				else{
					lang.nextStep();
					arrayToSort.unhighlightCell(i, null, null);
					arrayToSort.unhighlightCell((i+1), null, null);
					exec("for1");
					lang.nextStep();
				}
			}
			iMarker.hide();
			
			
			lang.nextStep();
			exec("if2");										//if
			if(swapped == false){
				lang.nextStep();
				markAllAsFinished(arrayToSort);
				exec("return1");
				lang.nextStep();
				return;
			}
			
			else{
				lang.nextStep();
				exec("else2");
				lang.nextStep();
				exec("decL");  
				arrayToSort.highlightElem(last, null, null);
				last --;
					if(last == first){
						lastMarker.hide();
						arrayFirstProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "f=l");
					}
					else
						lastMarker.move(last, new MsTiming(100), null);
				lang.nextStep();
				exec("resetSwapped1"); 
				lang.nextStep();
				swapped = false;
				
			}
			
			lang.nextStep();
			exec("for2"); 
			lang.nextStep();
			
			lastMarker.hide();
			//arrayJProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j=l");//for-loop
			jMarker = lang.newArrayMarker(arrayToSort, last, "j", null, arrayJProps);
			
			for(int j=last; j>first; j--){
				if(last!=j){
					lastMarker.show();
					jMarker.move(j,new MsTiming(100), null);
				}
					
				exec("if3");
				arrayToSort.highlightCell(j, null, null);
				arrayToSort.highlightCell((j-1), null, null);
				if(arrToSort[j-1]>arrToSort[j]){
					lang.nextStep();
					exec("swap2");				//if - true
					arrayToSort.swap(j, (j-1), null, new MsTiming(450));		//swap j and j-1
					lang.nextStep();
					exec("setSwapped2");
					swapped = true;
					lang.nextStep();
					exec("for2");
					arrayToSort.unhighlightCell(j, null, null);
					arrayToSort.unhighlightCell((j-1), null, null);
					lang.nextStep();
				}
				else{
					lang.nextStep();
					exec("for2");
					arrayToSort.unhighlightCell(j, null, null);
					arrayToSort.unhighlightCell((j-1), null, null);
					lang.nextStep();
				}
				
				
			}
			jMarker.hide();
			
			lang.nextStep();
			exec("if4");
			sourceCode.toggleHighlight(14, 18);							//if
			if(swapped == false){
				lang.nextStep();
				markAllAsFinished(arrayToSort);
				exec("return2");
				lang.nextStep();
				sourceCode.unhighlight(19);
				return;
			}
			else{
				lang.nextStep();
				exec("else4");
				lang.nextStep();
				exec("incF");
				arrayToSort.highlightElem(first, null, null);
				first++;	
					if(first==last){
						firstMarker.hide();
						arrayLastProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "l=f");
						}
					else
						firstMarker.move(first,new MsTiming(100), null);
				lang.nextStep();
				exec("resetSwapped2");
				swapped = false;
				lang.nextStep();
			}
			lang.nextStep();
			exec("while");
			lang.nextStep();
		}while(first<last);
		
	}
	
	/**
	 * This function defines the properties of the animation and 
	 * calls the sorting algorithm
	 */
	public void initLocal(){
		
		super.init();
		
		titelProperties= new SourceCodeProperties();
		shakerSortProperties = new SourceCodeProperties();
		endProperties= new SourceCodeProperties();
		arrayFirstProps = new ArrayMarkerProperties();
		arrayLastProps = new ArrayMarkerProperties();
		arrayIProps = new ArrayMarkerProperties();
		arrayJProps = new ArrayMarkerProperties();
		arrProperties = new ArrayProperties();
			
		
		titelProperties = (SourceCodeProperties) container.getPropertiesByName("titel");
		shakerSortProperties = (SourceCodeProperties)container.getPropertiesByName("source");
		arrayIProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		arrayIProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
		arrayJProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
		arrayJProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);
		arrayFirstProps = (ArrayMarkerProperties)container.getPropertiesByName("markerF");
		arrayLastProps = (ArrayMarkerProperties)container.getPropertiesByName("markerL");
		arrProperties = (ArrayProperties)container.getPropertiesByName("array");
		arrToSort = (int[])(primitives.get("intArray"));
		

		
		setProperties(); 
		
		
		
		lang.nextStep();
		titel = lang.newSourceCode(new Coordinates(400,25),"titel" , null,titelProperties);
		titel.addCodeLine("ShakerSort", "shakerSort", 0, null);
		sourceCode = lang.newSourceCode(new Coordinates(5,5), "pseudoCodeSort", null, shakerSortProperties);
		
		
		
		//Fill in the values from the given array to the structure, we just created
		lang.nextStep();
		arrayToSort = lang.newIntArray(new Coordinates(370, 150), arrToSort, "arrayToSort", null, arrProperties);
		
//		 setup complexity
		vars.declare("int", comp); vars.setGlobal(comp);
		vars.declare("int", assi); vars.setGlobal(assi);
		
		Text text = lang.newText(new Coordinates(300, 20), "...", "complexity", null);
		TextUpdater tu = new TextUpdater(text);
		tu.addToken("Compares: ");
		tu.addToken(vars.getVariable(comp));
		tu.addToken(" - Assignments: ");
		tu.addToken(vars.getVariable(assi));
		tu.update();
		
		
		parse();
		
		lang.nextStep();
		shakerSort(arrayToSort);
		
		
	}
	

	
	public String generate(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) {
		//creating the object 
		
//		ShakerSortTV sort = 
//		    new ShakerSortTV();
		
		primitives = arg1;
		container = arg0;
		
			
		initLocal();					//creates the visual structures and calls the sorting function	
		//call selectionSort-function in a new animation step
		return lang.toString();
	}

	
	public String getOutputLanguage() {
	    return Generator.JAVA_OUTPUT;
	}


}
