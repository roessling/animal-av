package generators;

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
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class SelectionSortDemo implements Generator {
  private Language lang;
  private ArrayProperties arrayProps;
  public void init() {
    // Generate a new Language instance for content creation
    // Parameter: Animation title, author, width, height
  	lang = new AnimalScript("Quicksort Animation",
  			"Dr. Guido Roessling", 640, 480);
    // Activate step control
  	lang.setStepMode(true);

    // create array properties with default values
  	arrayProps = new ArrayProperties();
    // Redefine properties: border red, filled with gray
  	arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
  			Color.RED); // Base color red (border)
  	arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,
  			true); // filled
  	arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, 
  				Color.GRAY); // fill color gray
  	ami = new ArrayMarkerProperties();
  	ami.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  	ami.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
  	amj = new ArrayMarkerProperties();
  	amj.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
  	amj.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
  	amMin = new ArrayMarkerProperties();
  	amMin.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
  	amMin.set(AnimationPropertiesKeys.LABEL_PROPERTY, "min");
  }
  private ArrayMarkerProperties ami, amj, amMin;
  private SourceCode sc;

  
  public static void main(String[] args) {
    SelectionSortDemo ssd = new SelectionSortDemo();
    ssd.init();
    int[] original = new int[] { 1, 13, 7, 2, 11};
    ssd.selectionSort(original);
    ssd.lang.finalizeGeneration();
    System.err.println(ssd.lang.getAnimationCode());
  }
  
  public void selectionSort1(int[] arrayContents) {
    IntArray array = lang.newIntArray(new Coordinates(10, 100), 
        arrayContents, "array", null, arrayProps);
    Timing defaultTiming = new TicksTiming(15);
    int i, j, minIndex;

    for (i=0; i<array.getLength() - 1; i++) {
      minIndex = i;
      for (j=i+1; j<array.getLength(); j++)
        if (array.getData(j) < array.getData(minIndex))
          minIndex = j;
      array.swap(i, minIndex, null, defaultTiming);
      lang.nextStep(); // change step
    }
  }
  
  public void showSourceCode() {
    // first, set the visual properties for the source code
     SourceCodeProperties scProps = new SourceCodeProperties();
     scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
     scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, 
        new Font("Monospaced", Font.PLAIN, 12));
     scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);   
     scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

     // now, create the source code entity
     sc = lang.newSourceCode(new Coordinates(10, 140), "sourceCode",
         null, scProps);
     // add a code line
     // parameters: code itself; name (can be null); indentation level; display options
     sc.addCodeLine("public void selectionSort(int[] data) {", null, 0, null);
     sc.addCodeLine("int i, j, minIndex;", null, 1, null);
     sc.addCodeLine("for (i = 0; i < inputArray.length - 1; i++) {", null, 1, null);
     sc.addCodeLine("minIndex = i;", null, 2, null);
     sc.addCodeLine("for (j = i + 1; j < inputArray.length; j++)", null, 2, null);
     sc.addCodeLine("if (inputArray[j] < inputArray[minIndex])", null, 3, null);
     sc.addCodeLine("minIndex = j;", null, 4, null);
     sc.addCodeLine("swap(inputArray, i, j); // swap", null, 2, null);
     sc.addCodeLine("}", null, 1, null);
     sc.addCodeLine("}", null, 0, null);
  }
  
  public void selectionSort2(int[] arrayContents) {
    IntArray array = lang.newIntArray(new Coordinates(10, 100), 
        arrayContents, "array", null, arrayProps);
    Timing defaultTiming = new TicksTiming(15);
    lang.nextStep(); // to show array without markers
    
    ArrayMarker i = lang.newArrayMarker(array, 0, "i", null, ami);
    lang.nextStep(); // to show i marker
    ArrayMarker j = lang.newArrayMarker(array, 0, "j", null, amj);
    lang.nextStep(); // to show j marker
    ArrayMarker min = lang.newArrayMarker(array, 0, "min", null, amMin);

    for (; i.getPosition() < array.getLength() - 1; 
    	i.increment(null, defaultTiming)) {
    	lang.nextStep(); // for increment 
      min.move(i.getPosition(), null,	defaultTiming);
      lang.nextStep(); // move min
      for (j.move(i.getPosition() + 1, null, defaultTiming); 
      	j.getPosition() < array.getLength();
      	j.increment(null, defaultTiming)) {
      	lang.nextStep(); // wait for the update of j
      	if (array.getData(j.getPosition()) < array.getData(min.getPosition())) {
      		min.move(j.getPosition(), null, defaultTiming);
      		lang.nextStep(); // wait for update of j
      	}
      }
      array.swap(i.getPosition(), min.getPosition(), null, defaultTiming);
      lang.nextStep(); // change step
    }
  }
  
  public void selectionSort3(int[] arrayContents) {
    IntArray array = lang.newIntArray(new Coordinates(10, 100), 
        arrayContents, "array", null, arrayProps);
    Timing defaultTiming = new TicksTiming(15);
    showSourceCode();
    lang.nextStep(); // to show array without markers
    
    ArrayMarker i = lang.newArrayMarker(array, 0, "i", null, ami);
    lang.nextStep(); // to show i marker
    ArrayMarker j = lang.newArrayMarker(array, 0, "j", null, amj);
    lang.nextStep(); // to show j marker
    ArrayMarker min = lang.newArrayMarker(array, 0, "min", null, amMin);

    for (; i.getPosition() < array.getLength() - 1; 
    	i.increment(null, defaultTiming)) {
    	lang.nextStep(); // for increment 
      min.move(i.getPosition(), null,	defaultTiming);
      lang.nextStep(); // move min
      for (j.move(i.getPosition() + 1, null, defaultTiming); 
      	j.getPosition() < array.getLength();
      	j.increment(null, defaultTiming)) {
      	lang.nextStep(); // wait for the update of j
      	if (array.getData(j.getPosition()) < array.getData(min.getPosition())) {
      		min.move(j.getPosition(), null, defaultTiming);
      		lang.nextStep(); // wait for update of j
      	}
      }
      array.swap(i.getPosition(), min.getPosition(), null, defaultTiming);
      lang.nextStep(); // change step
    }
  }
  
  public void selectionSort4(int[] arrayContents) {
    IntArray array = lang.newIntArray(new Coordinates(10, 100), 
        arrayContents, "array", null, arrayProps);
    Timing defaultTiming = new TicksTiming(15);
    showSourceCode();
    lang.nextStep(); // to show array without markers
    sc.highlight(0); // method head
    lang.nextStep(); // to show array without markers
    sc.toggleHighlight(0, 1); // jump from header to int...
    ArrayMarker i = lang.newArrayMarker(array, 0, "i", null, ami);
    lang.nextStep(); // to show i marker
    ArrayMarker j = lang.newArrayMarker(array, 0, "j", null, amj);
    lang.nextStep(); // to show j marker
    ArrayMarker min = lang.newArrayMarker(array, 0, "min", null, amMin);

    sc.unhighlight(1);
    for (; i.getPosition() < array.getLength() - 1; 
    	i.increment(null, defaultTiming)) {
    	sc.highlight(2); // now at line 2 (for loop)
    	lang.nextStep(); // for increment 
    	sc.toggleHighlight(2, 3); // for --> min =
      min.move(i.getPosition(), null,	defaultTiming);
      lang.nextStep(); // move min
      sc.unhighlight(3); // min= done
      for (j.move(i.getPosition() + 1, null, defaultTiming); 
      	j.getPosition() < array.getLength();
      	j.increment(null, defaultTiming)) {
      	sc.highlight(4); // for j...
      	lang.nextStep(); // wait for the update of j
      	sc.toggleHighlight(4, 5);
      	if (array.getData(j.getPosition()) < array.getData(min.getPosition())) {
      		lang.nextStep();
      		sc.toggleHighlight(5, 6);
      		min.move(j.getPosition(), null, defaultTiming);
      		lang.nextStep(); // wait for update of j
      		sc.unhighlight(6);
      	} else {
      		lang.nextStep(); 
      		sc.unhighlight(5); // needed for source highlighting
      	}
      }
      sc.highlight(7);
      array.swap(i.getPosition(), min.getPosition(), null, defaultTiming);
      lang.nextStep(); // change step
      sc.unhighlight(7);
    }
  }

  
  public void selectionSort(int[] data) {
  	selectionSort4(data);
  }

  public String getCodeExample() {
    return "Straightforward SelectionSort Algorithm"; // to give readers an impression
  }
  public Locale getContentLocale() {
    return Locale.US; // US-English
  }
  public String getDescription() {
    return "Animates SelectionSort with Source Code + Highlighting"; // description
  }
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION; // file extension for "AnimalScript, uncompressed"
  }
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT); // this is about sorting!
  }
  public String getName() {
    return "SelectionSortDemo"; // the title to be displayed
  }

  public String getAnimationAuthor() {
    return "Guido Rößling";
  }
	
	public String generate(AnimationPropertiesContainer props, 
			Hashtable<String, Object> primitives) {
		init(); // ensure all properties are set up :-)
		int[] arrayData = (int[])primitives.get("array");
		// adapt the COLOR to whatever the user chose
    // you could do this for all properties if you wanted to...
    arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, 
    		props.get("array", AnimationPropertiesKeys.COLOR_PROPERTY));

    // call the selection sort method
		selectionSort(arrayData);
    lang.finalizeGeneration();
    return lang.getAnimationCode();
	}
    
	public String getOutputLanguage() {
	  return Generator.JAVA_OUTPUT;
	}
    
    public String getAlgorithmName() {
      return "Selection Sort";
    }

}
