package algoanim.counter.examples.sorting;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class InsertionSortGdI2 //extends SortingAlgorithm 
implements Generator {
  private Language lang;
  ArrayProperties arrayProps;
  SourceCode code;
  ArrayMarkerProperties iMarkerProps, jMarkerProps;
  RectProperties rectProps;
  SourceCodeProperties sourceProps;
  TextProperties txtProps = new TextProperties();
  TicksTiming defaultTiming = new TicksTiming(30);
  TicksTiming atOnce = new TicksTiming(0);
  Rect assRect;
  Rect cmpRect;
  int nrAssigns = 0, nrComp = 0;

  public InsertionSortGdI2() {
    rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
  }
  private static final String DESCRIPTION = 
    "Dieser Generator sortiert das vom Nutzer eingegebene Array mittels Insertion Sort."
    +"Der Algorithmus entnimmt der unsortierten Eingabemenge ein beliebiges "
    +"(z.B. das erste) Element und fügt es an richtiger Stelle in die (anfangs leere) "
    +"Ausgabemenge ein. Das Verfahren arbeitet also in-place. Geht man in der "
    +"Reihenfolge der ursprünglichen Menge vor, so ist es jedoch (etwa im Gegensatz "
    +"zu Selection Sort) stabil. Wird auf einem Array gearbeitet, so müssen die "
    +"Elemente nach dem neu eingefügten Element verschoben werden. Dies ist die "
    +"eigentlich teure Operation von Insertionsort, da das Finden der richtigen "
    +"Einfügeposition über eine binäre Suche vergleichsweise effizient erfolgen kann.";

//  private static final String MY_FOOTER_TEXT =
//    "hideAll\n"
//    +"{\n"
//    +"text \"eoa\" \"Ende der Animation\" at (120,50) color black font SansSerif size 32\n"
//    +"  text \"comps\" \"Es wurden \" +$\"nrComparisons\" asInt"
//    +" +\" Vergleiche\" und \" +$\"nrAssignments\" asInt +\" Zuweisungen durchgeführt.\""
//    +" at (20,100) color black font SansSerif size 24\n";

  private static final String HEADER_TEXT = 
    "text \"f1-01\" \"Insertion Sort\" at (120,50) color black font SansSerif size 32 bold\n"
    + "{\n"
    +"  text \"f1-02a\" \"Sortieren durch Einfügen, auch als Insertion Sort bezeichnet, fuegt\" at (20,100) color black font SansSerif size 24\n"
    +"  text \"f1-02b\" \"der Reihe nach Elemente in eine bereits sortierte (Teil-)Liste ein,\" at (20,130)\n"
    +"  text \"f1-02c\" \"die anfangs leer ist.\" at (20,160)\n"
    +"}\n"
    +"{\n"
    +"  text \"f1-03a\" \"Damit ist das Vorgehen dem Sortieren von Spielkarten ähnlich: in\" at (20,200)\n"
    +"  text \"f1-03b\" \"jedem Schritt wird eine neue Spielkarte zwischen die bereits\" at (20,230)\n"
    +"  text \"f1-03c\" \"sortierten Karten einfügt.\" at (20,260)\n"
    +"}\n"
    +"label \"Insertion Sort Übersicht\"\n"
    +"hideAll\n"
    +"text \"f2-01\" \"Der Algorithmus in Worten\" at (120,50) color black font SansSerif size 32 bold\n"
    +"text \"f2-02\" \"1. Setze i=1\" at (20,100) color black font SansSerif size 24\n"
    +"text \"f2-03\" \"2. Setze j=i und speichere a[i] in einer Variablen temp\" at (20,140)\n"
    +"{\n"
    +"  text \"f2-04a\" \"3. Solange j>0 und v kleiner als a[j-1] ist,\" at (20,180)\n"
    +"  text \"f2-04b\" \"   kopiere a[j-1] an Position a[j] und setze j = j - 1\" at (20,210)\n"
    +"}\n"
    +"text \"f2-05\" \"4. Füge Element temp an die Position j ein\" at (20,250)\n"
    +"{\n"
    +"  text \"f2-06a\" \"5. Falls i kleiner als n ist, erhöhe i um eins und fahre\" at (20,290)\n"
    +"  text \"f2-06b\" \"    fort mit Schritt 2\" at (20,320)\n"
    +"}\n"
    +"label \"Insertion Sort Pseudocode\"\n";

  private static final String SOURCE_CODE =
    "public void insertionSort(int[] array){ // sort by Insertion Sort"
    +"\n  int i, j, temp;"
    +"\n  for (i=1; i<array.length; i++) {"
    +"\n    j = i;"
    +"\n    temp = array[i]; // store current element"
    +"\n    while (j > 0 && array[j - 1] > temp) {"
    +"\n      array[j] = array[j - 1]; // copy smaller value over current"
    +"\n      j = j - 1; // step to next element"
    +"\n    }"
    +"\n    array[j] = temp; // re-insert current value in proper position"
    +"\n  }" 
    +"\n}";
  
  private void generateHeader() {
    lang.addLine(HEADER_TEXT);
  }
  
  private void generateCodeExample() {
    code = lang.newSourceCode(new Coordinates(20, 200),
        "code", null, sourceProps);
    code.addCodeLine("public void insertionSort(int[] array){ // sort by Insertion Sort", 
        "", 0, null);
    
    code.addCodeLine("int i, j, temp;", "" , 1, null);
    code.addCodeLine("for (i = 1; i < array.length; i++) {", "" , 1, null);
    code.addCodeLine("j = i;", "" , 2, null);
    code.addCodeLine("temp = array[i]; // store current element", "" , 2, null);
    code.addCodeLine("while (j > 0 && array[j - 1] > temp) {", "" , 2, null);
    code.addCodeLine("array[j] = array[j - 1]; // copy smaller value over current",
        "" , 3, null);
    code.addCodeLine("j = j - 1; // step to next element", "" , 3, null);
    code.addCodeLine("}", "" , 2, null);
    code.addCodeLine("array[j] = temp; // re-insert current value in proper position",
        "" , 2, null);
    code.addCodeLine("}", "" , 1, null);
    code.addCodeLine("}", "" , 0, null);
  }
  
  private void updateComparisons(int byHowMuch) {
    nrComp += byHowMuch;
    cmpRect.moveBy("translate #2", 2 * byHowMuch, 0, atOnce, defaultTiming);
    }
  private void updateAssignments(int byHowMuch) {
    nrAssigns += byHowMuch;
    assRect.moveBy("translate #2", 2 * byHowMuch, 0, atOnce, defaultTiming);
  }

  public void sort(int[] arrayValues) {
    generateHeader();
    lang.addLine("hideAll");
    
    // show array and code
    IntArray array = lang.newIntArray(new Coordinates(20, 140),
        arrayValues, "array", null, arrayProps);
    lang.nextStep();
    generateCodeExample();
    lang.nextStep("Initialisierung");
    code.highlight(0);
    lang.nextStep();
    code.toggleHighlight(0, 1);
    int i = 0, j = 0, temp = -1;
    
    lang.newText(new Offset(100, 0, "array",
        AnimalScript.DIRECTION_NE), "#Assign", "ass", null, txtProps);
    lang.newText(new Offset(100, 40, "array",
        AnimalScript.DIRECTION_NE), "#Comp", "comp", null, txtProps);
    assRect = lang.newRect(new Offset(20, 0, "ass", AnimalScript.DIRECTION_NE),
        new Offset(20, 0, "ass", AnimalScript.DIRECTION_SE), 
        "assRect", null, rectProps);
    cmpRect = lang.newRect(new Offset(20, 0, "comp", AnimalScript.DIRECTION_NE),
        new Offset(20, 0, "comp", AnimalScript.DIRECTION_SE), 
        "cmpRect", null, rectProps);

    // visual rendition of the declaration of i, j, temp
    ArrayMarker iMarker = lang.newArrayMarker(array, 0, "i", null, iMarkerProps);
    ArrayMarker jMarker = lang.newArrayMarker(array, 0, "j", null, jMarkerProps);
    lang.newText(new Offset(0, 20, array, AnimalScript.DIRECTION_SW), 
        "temp:", "temp", null, txtProps);
    lang.nextStep();
    code.unhighlight(1);
    Text tmpTxt = null;
    int count = 0;/////////////////////////////
    for (i = 1; i < arrayValues.length; i++) {
      // previous line must be unhighlighted - 1 if i==1, else 9
      code.toggleHighlight((i != 1) ? 9 : 1, 2);
      updateAssignments(1);
      updateComparisons(1);
      
      
      iMarker.move(i, defaultTiming, null);
      array.highlightCell(i - 1, null, null);
      lang.nextStep("Sortieren bis Position " +i);
      code.highlight(2, 0, true);
      code.highlight(3);
      
      // move j
      jMarker.move(i, atOnce, defaultTiming);
      j = i;
      updateAssignments(1);
      lang.nextStep();
      
      code.toggleHighlight(3, 4);
      temp = arrayValues[i];
      updateAssignments(1);
      if (i == 1)
        tmpTxt = lang.newText(new Offset(10, 0, "temp", 
            AnimalScript.DIRECTION_BASELINE_END), String.valueOf(temp),
            "tmp", null, txtProps);
      else
        tmpTxt.setText(String.valueOf(temp), atOnce, defaultTiming);
      lang.nextStep();
      // next step: enter while loop
      code.toggleHighlight(4, 5);
      while (j > 0 && arrayValues[j - 1] > temp) {
        // count 2 comparisons
        lang.nextStep();
        updateComparisons(2);

        // next step: set loop to context, copy values
        // note: the internal while loop has already have been unhighlighted,
        // if this was not the first iteration
        array.highlightElem(j - 1, atOnce, defaultTiming);
        code.highlight(5, 0, true);
        code.highlight(6);
        array.put(j, arrayValues[j - 1], atOnce, defaultTiming);
        count++;///////////
        CheckpointUtils.checkpointEvent(this,"austausch",new Variable("index",j),new Variable("nextone",arrayValues[j - 1]));//////////////////////
        updateAssignments(1);
        lang.nextStep();
        
        code.toggleHighlight(6, 7);
        jMarker.move(j - 1, atOnce, defaultTiming);
        j--;
        updateAssignments(1);
        array.unhighlightCell(j - 1, j, atOnce, atOnce);
        
        lang.nextStep();
        
        code.toggleHighlight(7, 5);
      }
      // inc comparisons
      updateComparisons(1);
      lang.nextStep();
      code.toggleHighlight(5, 9);
      array.put(j, temp, atOnce, atOnce);
      CheckpointUtils.checkpointEvent(this,"replace",new Variable("index",j),new Variable("value",temp));///////////////////
      updateAssignments(1);
      tmpTxt.changeColor("color", Color.RED, atOnce, atOnce);
      array.highlightCell(0, i, atOnce, atOnce);
      lang.nextStep("Insert Element in Array [0, " + i + "]");
    }
    CheckpointUtils.checkpointEvent(this, "countAustausch", new Variable("count",count));////////////////////////////////
    
    // end for loop
    // inc comp, inc assign

    // next step: make sure no code is highlighted. Last must be line 9.
    code.unhighlight(2);
    code.unhighlight(9);
    
//    nrAssigns = targetArray.getNrAssignments();
//    nrComparisons = targetArray.getNrComparisons();
    lang.nextStep();
    StringBuilder sb = new StringBuilder(80);
    sb.append("  variable \"nrComparisons\"\n");
    sb.append("  assign \"nrComparisons\" = " + nrComp + "\n");
    sb.append("  variable \"nrAssignments\"\n");
    sb.append("  assign \"nrAssignments\" = " + nrAssigns + "\n");
    lang.addLine(sb);
    lang.nextStep("Aufwand");
    
    StringBuilder stringBuilder = new StringBuilder(512);
    stringBuilder.append("{\n  hideAll");
    stringBuilder.append("\n  text \"eoa\" \"End of the Animation\" at (120,50)");
    stringBuilder.append(" color black font SansSerif size 32");
    stringBuilder.append("\n  text \"comps\" \"A total of ");
    stringBuilder.append(nrComp).append(" comparisons and ");
    stringBuilder.append(nrAssigns).append(" assignments were performed.\"");
    stringBuilder.append(" at (20,130) color black font SansSerif size 24\n}");
    lang.addLine(stringBuilder);
//    startStep();
//    endAnimGeneration();
//    endStep();
//    addLabel(sb, "Aufwand");
//    sb.append(generateEnglishSummary(targetArray.getNrComparisons(), 
//    		targetArray.getNrAssignments()));
//    		MY_FOOTER_TEXT);
  }
  
  protected String getAlgorithmDescription() {
    return DESCRIPTION;
  }

  protected String getAlgorithmCode() {
    return SOURCE_CODE;
  }

	public String getName() {
		return "Insertion Sort (GdI 2)";
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getCodeExample() {
		return SOURCE_CODE;
	}
  
  
	/**
	 * getContentLocale returns the target Locale of the generated output
	 * Use e.g. Locale.US for English content, Locale.GERMANY for German, etc.
	 * 
	 * @return a Locale instance that describes the content type of the output
	 */
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
     
    int[] theArray = (int[])primitives.get("array");
    arrayProps = (ArrayProperties)props.getPropertiesByName("array");
    sourceProps = (SourceCodeProperties)props.getPropertiesByName("code");
    iMarkerProps = (ArrayMarkerProperties)props.getPropertiesByName("iMarker");
    jMarkerProps = (ArrayMarkerProperties)props.getPropertiesByName("jMarker");
    sort(theArray);
    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Insertion Sort";
  }

  @Override
  public String getAnimationAuthor() {
    return "Guido R\u00F6\u00DFling";
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
  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  @Override
  public void init() {
    lang = new AnimalScript("Insertion Sort (GdI 2)",
        "Guido Roessling (roessling@acm.org)", 800, 600);
    lang.setStepMode(true);
  }
}
