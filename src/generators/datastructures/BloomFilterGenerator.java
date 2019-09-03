/*
 * BloomFilterGenerator.java
 * Sandra Sch�fer, Jonas Schatz, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.datastructures;

import java.awt.Color;
import java.awt.Font;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Group;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

public class BloomFilterGenerator implements ValidatingGenerator {
    private Language lang;
    private Locale loc;
    private SourceCodeProperties sourceCodeStyle;
    private int[] numbersToInsert;
    private int bloomFilterSize;
    private int[] numbersToCheck;
    private int[] primesForHash;
    private int bloomFilterSquareSize;
    
    private SquareProperties bloomFilterIsOneXML;
    private SquareProperties bloomFilterIsZeroXML;
    private PolylineProperties arrowStyle;

    public void init(){
        lang = new AnimalScript("Bloom-Filter", "Sandra Sch�fer, Jonas Schatz", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }
    
    public static String arrayListToString(List<Integer> arraylist) {
    	String result = "";
    	for (int i=0; i<arraylist.size(); i++) {
    		result += arraylist.get(i);
    		if (i != arraylist.size()-1) result += ", ";
    	}
    	return result;
    }
    
	public static int simpleHash(int number, int prime, int modulo) {
		return (number*prime) % modulo;
	}
    
    public static <T extends Primitive> void hideArrayOfPrimitives(T[] myArray) {
    	for(T i : myArray) if (i != null) i.hide();
    }
    
    public static <T extends Primitive> void showArrayOfPrimitives(T[] myArray) {
    	for(T i : myArray) if (i != null) i.show();
    }
    
    public static void eraseTextArray(Text[] myArray) {
    	for(Text i : myArray) if (i != null) i.setText("", null, null);
    }
    
    public static void askQuestion(boolean accepted) {

    }
    
    
    // Creates the "fancy marker", looking like this:
    //  _   _
    // |     |
    // 
    // |_   _|
    // 
    public static Group createFancyMarker(String squareIdentifier, int size, Language lang, String name) {
    	    	
    	int offset = size/8;
    	int length = size/5;
    	
    	// Nodes making up the fancy marker. From the top left corner, clockwise
    	Node node0  = new Offset(offset, offset, squareIdentifier, AnimalScript.DIRECTION_NW);
    	Node node1  = new Offset(offset + length, offset, squareIdentifier, AnimalScript.DIRECTION_NW);
    	Node node2  = new Offset(size-offset-length, offset, squareIdentifier, AnimalScript.DIRECTION_NW);
    	Node node3  = new Offset(size-offset, offset, squareIdentifier, AnimalScript.DIRECTION_NW);
    	Node node4  = new Offset(size-offset, offset+length, squareIdentifier, AnimalScript.DIRECTION_NW);
    	Node node5  = new Offset(size-offset, size-offset-length, squareIdentifier, AnimalScript.DIRECTION_NW);
    	Node node6  = new Offset(size-offset, size-offset, squareIdentifier, AnimalScript.DIRECTION_NW);
    	Node node7  = new Offset(size-offset-length, size-offset, squareIdentifier, AnimalScript.DIRECTION_NW);
    	Node node8  = new Offset(offset + length, size-offset, squareIdentifier, AnimalScript.DIRECTION_NW);
    	Node node9  = new Offset(offset, size-offset, squareIdentifier, AnimalScript.DIRECTION_NW);
    	Node node10 = new Offset(offset, size-offset-length,squareIdentifier, AnimalScript.DIRECTION_NW);
    	Node node11 = new Offset(offset, offset+length, squareIdentifier, AnimalScript.DIRECTION_NW);
    	
    	// Method returns a group, which has to be constructed from a list of primitives
    	LinkedList<Primitive> list = new LinkedList<>();
    	list.add(lang.newPolyline(new Node[] {node0, node1}, name + "0", null));
    	list.add(lang.newPolyline(new Node[] {node2, node3}, name + "1", null));
    	list.add(lang.newPolyline(new Node[] {node3, node4}, name + "2", null));
    	list.add(lang.newPolyline(new Node[] {node5, node6}, name + "3", null));
    	list.add(lang.newPolyline(new Node[] {node6, node7}, name + "4", null));
    	list.add(lang.newPolyline(new Node[] {node8, node9}, name + "5", null));
    	list.add(lang.newPolyline(new Node[] {node9, node10}, name + "6", null));
    	list.add(lang.newPolyline(new Node[] {node11, node0}, name + "7", null));
    	
    	return lang.newGroup(list, name);
    }
    
    
    
    
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	
    	// Automatic import of the XML file
        sourceCodeStyle = (SourceCodeProperties)props.getPropertiesByName("sourceCodeStyle");
        bloomFilterIsOneXML = (SquareProperties)props.getPropertiesByName("bloomFilterIsOne");
        bloomFilterIsZeroXML = (SquareProperties)props.getPropertiesByName("bloomFilterIsZero");
        arrowStyle = (PolylineProperties)props.getPropertiesByName("arrowStyle");
        bloomFilterSize = (Integer)primitives.get("bloomFilterSize");
        numbersToCheck = (int[])primitives.get("numbersToCheck");
        primesForHash = (int[])primitives.get("primesForHash");
        numbersToInsert = (int[])primitives.get("numbersToInsert");
        bloomFilterSquareSize = (Integer)primitives.get("bloomFilterSquareSize");
        bloomFilterSize = (Integer)primitives.get("bloomFilterSize");
        
        
        // +++++++++++++++++++++
        // Derive parameters from the XML properties
        // +++++++++++++++++++++
        
        Color bloomFilterIsOne = (Color)(bloomFilterIsOneXML.getItem("fillColor").get());
        Color bloomFilterIsZero = (Color)(bloomFilterIsZeroXML.getItem("fillColor").get());
        
        // List of elements that still have to be inserted
        List<Integer> notYetInserted = new ArrayList<>(numbersToInsert.length);
        for (int i : numbersToInsert) notYetInserted.add(Integer.valueOf(i));
        
        // List of elements that still have to be checked
        List<Integer> notYetChecked =  new ArrayList<>(numbersToCheck.length);
        for (int i : numbersToCheck) notYetChecked.add(Integer.valueOf(i));
        
        // List of elements that are already inserted
        @SuppressWarnings({ "rawtypes", "unchecked" })
        List<Integer> alreadyInserted = new ArrayList();
        
        // The actual content of the bloom filter
        int[] bloomFilter = new int[bloomFilterSize];
        
        // Other variables
        int currentNumber;
        int numberOfHashFunctions = primesForHash.length;
        
        
        
        // +++++++++++++++++++++
        // Define my own parameters
        // +++++++++++++++++++++
        
        Color acceptedColor = Color.GREEN;
        Color deniedColor = Color.RED;
        
        int captionFontSize = 20;
        TextProperties captionStyle = new TextProperties();
        captionStyle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 0, captionFontSize));
        
        int largeFontSize = 15;
        TextProperties largeTextStyle = new TextProperties();
        largeTextStyle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 0, largeFontSize)); 
        
        int standardFontSize = 12;
        TextProperties standardTextStyle = new TextProperties();
        standardTextStyle.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", 0, standardFontSize)); 
        
        
        RectProperties descriptionRectProperty = new RectProperties();
        descriptionRectProperty.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255, 206, 161));
        descriptionRectProperty.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);     
 
        
        RectProperties ShadowProperties = new RectProperties();
        ShadowProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        ShadowProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
        ShadowProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);

        
        RectProperties BackgroundBoxProperties = new RectProperties();
        BackgroundBoxProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        BackgroundBoxProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(253, 253, 150));
        
        
        
        
        
        
        // +++++++++++++++++++++
        //  Create Descriptions
        // +++++++++++++++++++++
        
        // Caption
    	Rect captionShadow = lang.newRect(new Coordinates(23, 23), new Coordinates(263, 53), "descriptionCaptionShadow", null, ShadowProperties);
    	Rect captionBox = lang.newRect(new Coordinates(17, 17), new Coordinates(257, 47), "descriptionShadowBackground", null, BackgroundBoxProperties);
    	Text descriptionCaption = lang.newText(new Coordinates(20, 20), 
    			(loc == Locale.GERMANY) ? "Bloom Filter - Einfuehrung" : "Bloom Filter - Introduction", 
    			"descriptionCaption", null, captionStyle);
        
    	lang.newText(new Offset(0, 40, "descriptionCaption", AnimalScript.DIRECTION_NW), 
    			(loc == Locale.GERMANY) ? "Wie eingangs erwaehnt, ist der Bloom-Filter eine probabilistische Datenstruktur, mit dessen Hilfe das Enthalten eines" : "As mentioned previously, the Bloom Filter is a probabilistic data structure, used to check if an element is contained", 
    			"description1", null);
    	
    	lang.newText(new Offset(0, 25, "description1", AnimalScript.DIRECTION_NW), 
    			(loc == Locale.GERMANY) ? "Elements in einer Menge ueberprueft werden kann. " : "in a set.", 
    			"description2", null);
    	
    	lang.newText(new Offset(0, 40, "description2", AnimalScript.DIRECTION_NW), 
    			(loc == Locale.GERMANY) ? "Dazu wird eine Signatur jedes Elements erstellt. Diese besteht aus den Ergebnissen mehrerer Hash-Funktionen, in " : "For this, a signature of every element is calculated. It is combined from the results of multiple hash functions. ", 
    			"description3", null);
    	
    	lang.newText(new Offset(0, 25, "description3", AnimalScript.DIRECTION_NW), 
    			(loc == Locale.GERMANY) ? "unserem Fall handelt es sich um:" : "In our case, these are: ", 
    			"description4", null);
    	
    	
    	Text[] hashFunctionsDescription = new Text[numberOfHashFunctions]; 
        for(int i = 0; i < numberOfHashFunctions; i++) {
        	hashFunctionsDescription[i] = lang.newText(
        			new Offset(40, 25 + i*25, "description4", AnimalScript.DIRECTION_NW), 
        			"f(x) = (x*" + primesForHash[i] + ") mod " + bloomFilterSize, 
        			"hashFunctionsDescription_" + i, 
        			null);
        }
        
        
        lang.nextStep((loc == Locale.GERMANY) ? "Beschreibung" : "Description");
        
        eraseTextArray(hashFunctionsDescription);
        
        
        for(int i = 0; i < numberOfHashFunctions; i++) {
        	hashFunctionsDescription[i] = lang.newText(
        			new Offset(40, 25 + i*25, "description4", AnimalScript.DIRECTION_NW), 
        			"f(4) = (4*" + primesForHash[i] + ") mod " + bloomFilterSize + " = " + simpleHash(4, primesForHash[i], bloomFilterSize), 
        			"hashFunctionsDescription_" + i, 
        			null);
        }
    	
    	
    	
    	lang.nextStep();
    	lang.newText(new Offset(0, 40 + 25*numberOfHashFunctions, "description4", AnimalScript.DIRECTION_NW), 
    			(loc == Locale.GERMANY) ? "Die Werte eines binaeren Arrays werden an den Index-Positionen, die durch die Signatur festgelegt sind, auf 1 " : "The values of a binary array are set to 1 at index positions given by the signature. The check whether an element", 
    			"description5", null);
    	
    	lang.newText(new Offset(0, 25, "description5", AnimalScript.DIRECTION_NW), 
    			(loc == Locale.GERMANY) ? "gesetzt. Das Ueberpruefen auf Mitgliedschaft erfolgt analog. Beide Operationen werden in der folgenden Animation " : "is a member is carried out accordingly. Both operations will be shown in the following animation. ", 
    			"description6", null);
    	
    	lang.newText(new Offset(0, 25, "description6", AnimalScript.DIRECTION_NW), (loc == Locale.GERMANY) ? "dargestellt." : "", "description7", null);
    	
    	lang.nextStep();
    	lang.hideAllPrimitives();
    	
    	captionShadow = lang.newRect(new Coordinates(23, 23), new Coordinates(213, 53), "descriptionCaptionShadow", null, ShadowProperties);
    	captionBox = lang.newRect(new Coordinates(17, 17), new Coordinates(207, 47), "descriptionShadowBackground", null, BackgroundBoxProperties);
    	descriptionCaption = lang.newText(new Coordinates(20, 20), "Bloom Filter - Setup", "descriptionCaption", null, captionStyle);
    	
        
        // +++++++++++++++++++++
        // Create Bloom Filter
        // +++++++++++++++++++++
        
    	

        
        // Create Bloom Filter Data Structure
        Text bloomfiltertext = lang.newText(new Offset(0, 300, "descriptionCaption", AnimalScript.DIRECTION_NW), "Bloom Filter", "bloomfiltertext", null, largeTextStyle);
        
        Square[] bloomFilterCells = new Square[bloomFilterSize];        // The squares making up the filter
        Text[] bloomFilterIndicesAsText = new Text[bloomFilterSize];    // The indices 
        
        for(int i = 0; i < bloomFilterSize; i++) {
        	
        	// Create the actual squares
        	SquareProperties sp = new SquareProperties();
        	sp.set(AnimationPropertiesKeys.FILL_PROPERTY, bloomFilterIsZero);
        	sp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        	bloomFilterCells[i] = lang.newSquare(
        			new Offset(150 + bloomFilterSquareSize*i, 0, "bloomfiltertext", AnimalScript.DIRECTION_NW), 
        			bloomFilterSquareSize, "bloomFilterCell_" + i, null, sp);
        	
        	// Write the actual indices
        	bloomFilterIndicesAsText[i] = lang.newText(
        			new Offset(0, 5, "bloomFilterCell_" + i, AnimalScript.DIRECTION_S), 
        			Integer.toString(i), "bloomFilterIndicesAsText_" + i, null, standardTextStyle);
        }
        
        

        
        
        
        
        // +++++++++++++++++++++
        //  Setup Animation
        // +++++++++++++++++++++    
        
        // Create description for the bloom filter
        lang.nextStep("Animation Setup");
        Rect bloomfilterdescriptionbox = lang.newRect(
        		new Offset(150, 100, "bloomFilterCell_0", AnimalScript.DIRECTION_NW) , 
        		new Offset(550, 125, "bloomFilterCell_0", AnimalScript.DIRECTION_SE) , "bloomfilterdescriptionbox", null, descriptionRectProperty);
        Text bloomfilterdescription1 = lang.newText(new Offset(10, 10, "bloomfilterdescriptionbox", AnimalScript.DIRECTION_NW), 
        		(loc == Locale.GERMANY) ? "Der Bloom-Filter selbst ist ein binaeres Array, in unserem Fall mit Laenge " + bloomFilterSize + "." : "The Bloom filter itself is a binary array, in our case of length " + bloomFilterSize + ".", 
        		"bloomfilterdescription1", null);
        
        Text bloomfilterdescription2 = lang.newText(new Offset(0, 5, "bloomfilterdescription1", AnimalScript.DIRECTION_SW), 
        		(loc == Locale.GERMANY) ? "Array-Elemente mit Wert 1 werden farblich hervorgehoben:" : "Array elements with value 1 are highlighted.", 
        		"bloomfilterdescription2", null);
       
        
        // Temporarily highlight one cell in the bloom filter
        lang.nextStep();
        bloomFilterCells[1].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, bloomFilterIsOne, null, null);
        bloomFilterCells[1].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, bloomFilterIsZero, new MsTiming(500), null);
        lang.nextStep();
        
        bloomfilterdescription1.hide();
        bloomfilterdescription2.hide();
        bloomfilterdescriptionbox.hide();
        
       
        
        
        
        // Write down the numbers to be inserted
        Text elements = lang.newText(new Offset(0, 50, "descriptionCaption", AnimalScript.DIRECTION_NW), 
        		(loc == Locale.GERMANY) ? "Elemente:" : "Elements:", 
        		"elements", null, largeTextStyle);
        
        Text toBeProcessed = lang.newText(new Offset(150, -5, "elements", AnimalScript.DIRECTION_NW), 
        		(loc == Locale.GERMANY) ? "Noch einzufuegen:" : "To be inserted:", 
        		"numbersToInsertCaption", null, standardTextStyle);
        
        lang.newText(new Offset(350, -5, "elements", AnimalScript.DIRECTION_NW), 
        		(loc == Locale.GERMANY) ? "Aktuell:" : "Current:", 
        		"currentCaption", null, standardTextStyle);
        
        lang.newText(new Offset(450, -5, "elements", AnimalScript.DIRECTION_NW), 
        		(loc == Locale.GERMANY) ? "Schon eingefuegt:" : "Already inserted:", 
        		"alreadyInsertedCaption", null, standardTextStyle);
        
        Text numbersToInsertAsText = lang.newText(new Offset(0, -5, "numbersToInsertCaption", AnimalScript.DIRECTION_SW), "", "numbersToInsertAsText", null, standardTextStyle);
        Text currentNumberAsText = lang.newText(new Offset(0, -5, "currentCaption", AnimalScript.DIRECTION_SW), "", "currentNumberAsText", null, standardTextStyle);
        Text alreadyInsertedAsText = lang.newText(new Offset(0, -5, "alreadyInsertedCaption", AnimalScript.DIRECTION_SW), "", "alreadyInsertedAsText", null, standardTextStyle);
        
        
        // Create a description for these numbers
        Rect numbersdescriptionbox = lang.newRect(
        		new Offset(0, 50, "elements", AnimalScript.DIRECTION_SW), 
        		new Offset(865, 25, "elements", AnimalScript.DIRECTION_SW), "numbersdescriptionbox", null, descriptionRectProperty);
        
        Text numbersdescriptiontext = lang.newText(new Offset(5, 5, "numbersdescriptionbox", AnimalScript.DIRECTION_NW), 
        		(loc == Locale.GERMANY) ? "Hier werden die Elemente angezeigt, die noch einzufuegen sind, das aktuell betrachtete Element und die Elemente, die schon im Bloom-Filter enthalten sind." : "In the top row we can see: (1) Elements to be inserted (2) the currently considered element and (3) the already inserted elements.", 
        		"numbersdescriptiontext", null);
        Rect numbersHighlightBox1 = lang.newRect(new Offset(0, 5, "numbersToInsertAsText", AnimalScript.DIRECTION_NW), new Offset(80, 25, "numbersToInsertAsText", AnimalScript.DIRECTION_NW), "numbersHighlightBox1", null, ShadowProperties);
        Rect numbersHighlightBox2 = lang.newRect(new Offset(0, 5, "currentNumberAsText", AnimalScript.DIRECTION_NW), new Offset(80, 25, "currentNumberAsText", AnimalScript.DIRECTION_NW), "numbersHighlightBox2", null, ShadowProperties);
        Rect numbersHighlightBox3 = lang.newRect(new Offset(0, 5, "alreadyInsertedAsText", AnimalScript.DIRECTION_NW), new Offset(80, 25, "alreadyInsertedAsText", AnimalScript.DIRECTION_NW), "numbersHighlightBox3", null, ShadowProperties);
        
        lang.nextStep();
        numbersdescriptionbox.hide();
        numbersdescriptiontext.hide();
        numbersHighlightBox1.hide();
        numbersHighlightBox2.hide();
        numbersHighlightBox3.hide();
        
        
        
        
        
        // Write down the hash functions we are using
        lang.newText(new Offset(0, 100, "elements", AnimalScript.DIRECTION_NW), 
        		(loc == Locale.GERMANY) ? "Hash Funktionen:" : "Hash Functions:", "hashfunctions", null, largeTextStyle);
        
        Text[] hashFunctionsAsText = new Text[numberOfHashFunctions]; // contains the top row, for example: f(x) = (x*31) mod 20
        Text[] hashResultsAsText = new Text[numberOfHashFunctions];   // contains the bottom row, for example: f(x) = 17
        
        for(int i = 0; i < numberOfHashFunctions; i++) {
        	
        	// Fill up the "hashFunctionsAsText" with actual text
        	hashFunctionsAsText[i] = lang.newText(
        			new Offset(150 + i*150, 0, "hashfunctions", AnimalScript.DIRECTION_NW), 
        			"f(x) = (x*" + primesForHash[i] + ") mod " + bloomFilterSize, 
        			"hashFunctionAsText_" + i, 
        			null);
        	
        	// Fill up the "hashResultsAsText" with actual text
        	hashResultsAsText[i] = lang.newText(
        			new Offset(0, -5, "hashFunctionAsText_" + i, AnimalScript.DIRECTION_SW), 
        			"", "hashResultsAsText_" + i, null);
        }
        
        Rect hasdescriptionbox = lang.newRect(
        		new Offset(0, 50, "hashFunctionAsText_0", AnimalScript.DIRECTION_SW), 
        		new Offset(325, 25, "hashFunctionAsText_0", AnimalScript.DIRECTION_SW), "hasdescriptionbox", null, descriptionRectProperty);
        Text hashdescriptiontext = lang.newText(new Offset(5, 5, "hasdescriptionbox", AnimalScript.DIRECTION_NW), 
        		(loc == Locale.GERMANY) ? "Hier werden die verwendeten Hash-Funktionen angezeigt." : "The used Hash Functions are shown in the second row.", "numbersdescriptiontext", null);
        
        
        lang.nextStep();
        hasdescriptionbox.hide();
        hashdescriptiontext.hide();
        
        
        
        
        
        // +++++++++++++++++++++
        // Setup Source Code
        // +++++++++++++++++++++
        
        Text sourcecodeText = lang.newText(																// Dynamic positioning of the source code
        		new Coordinates(
        				((Offset) bloomFilterCells[bloomFilterSize-1].getUpperLeft()).getX() + 150,  	// directly to the right of the bloom filter
        				((Offset) elements.getUpperLeft()).getY()), 								    // at the height of the first caption
        		"Source Code:", "sourcecodetext", null, largeTextStyle);
        
        
        SourceCode src1 = lang.newSourceCode(										// First source code: For insert
        		new Offset(0, 50, sourcecodeText, AnimalScript.DIRECTION_NW), 
        		"sourceCode1", null, sourceCodeStyle);
        src1.addCodeLine("// Simple Hash-Function                                                               ", null, 0, null);
        src1.addCodeLine("private static int simpleHash(int number, int prime, int modulo) {                    ", null, 0, null);
        src1.addCodeLine("    return (number*prime) % modulo;                                                   ", null, 0, null);
        src1.addCodeLine("}                                                                                     ", null, 0, null);
        src1.addCodeLine("                                                                                     ", null, 0, null);
        src1.addCodeLine("// Method to insert a number into a bloom filter                                      ", null, 0, null);
        src1.addCodeLine("private static void put(int[] bloomFilter, int[] primes, int key) {                   ", null, 0, null);
        src1.addCodeLine("    for(int i = 0; i < primes.length; i++) {                                          ", null, 0, null);
        src1.addCodeLine("        int hash = simpleHash(key, primes[i], bloomFilter.length);                    ", null, 0, null);
        src1.addCodeLine("        bloomFilter[hash] = 1;                                                        ", null, 0, null);
        src1.addCodeLine("    }                                                                                 ", null, 0, null);
        src1.addCodeLine("}                                                                                     ", null, 0, null);
        src1.hide();
        
        
        
        captionShadow.hide();
        captionBox.hide();
        captionShadow = lang.newRect(new Coordinates(23, 23), new Coordinates(193, 53), "descriptionCaptionShadow", null, ShadowProperties);
        captionShadow.hide();
    	captionBox = lang.newRect(new Coordinates(17, 17), new Coordinates(187, 47), "descriptionShadowBackground", null, BackgroundBoxProperties);
    	captionBox.hide();
    	
        Text animationCaption = lang.newText(new Coordinates(20, 20), "Bloom Filter - Put", "animationCaption", null, captionStyle);
        animationCaption.hide();
        
        
        
        // If there are no numbers to insert, skip the Step where we setup the page for the insertion
    	if (numbersToInsert.length != 0) {
    		src1.show();
    		

	        captionShadow.show();
	        captionBox.show();
	    	animationCaption.show();
    		
	        lang.nextStep((loc == Locale.GERMANY) ? "Beginn der Animation" : "Start of Animation");
	        descriptionCaption.hide();
	        //captionShadow.hide();
	        //captionBox.hide();
	        
	        
	        // Display the numbers we will insert
	        numbersToInsertAsText.setText(arrayListToString(notYetInserted), null, null);
	        lang.nextStep();
    	}
        
    	
    	// +++++++++++++++++++++
        // Animation: Put
        // +++++++++++++++++++++
    	
        // Go through the numbers we want to insert in the bloom filter
        for(int i = 0; i < numbersToInsert.length; i++) {
        	
        	// Make some data structures we need during the loop
        	Polyline[] arrowsNumberToHash = new Polyline[numberOfHashFunctions];
        	Polyline[] arrowsHashToBloom = new Polyline[numberOfHashFunctions];
        	int[] hashResults = new int[numberOfHashFunctions];
        	Group[] fancyMarkers = new Group[numberOfHashFunctions];
        	
        	Rect insertDescriptionBox = lang.newRect(
	        		new Offset(175, 50, "bloomFilterCell_0", AnimalScript.DIRECTION_SW), 
	        		new Offset(525, 100, "bloomFilterCell_0", AnimalScript.DIRECTION_SW), "insertDescriptionBox", null, descriptionRectProperty);
        	
        	Text insertDescriptionText1 = lang.newText(new Offset(5, 5, "insertDescriptionBox", AnimalScript.DIRECTION_NW), 
	        		(loc == Locale.GERMANY) ? "Jede Zahl durchlaeuft jede Hash-Funktion." : "Every number is plugged into every hash function", 
	        		"insertDescriptionText1", null);
	        
	        Text insertDescriptionText2 = lang.newText(new Offset(0, 5, "insertDescriptionText1", AnimalScript.DIRECTION_SW), 
	        		(loc == Locale.GERMANY) ? "Der Wert des Bloom-Filters an diesem Index wird auf 1 gesetzt." : "The value of the Bloom filter at this position is set to 1.", 
	        		"insertDescriptionText2", null);
	        
	        insertDescriptionBox.hide();
	        insertDescriptionText1.hide();
	        insertDescriptionText2.hide();
	        
        	// Take the first number of "notYetInserted" and make it the number we're working with this loop
        	currentNumber = notYetInserted.remove(0);
        	
        	// Update the display: What numbers do we still have to insert? What is the current number
        	numbersToInsertAsText.setText(arrayListToString(notYetInserted), null, null);
        	currentNumberAsText.setText(Integer.toString(currentNumber), null, null);
    		lang.nextStep((loc == Locale.GERMANY) ? "Einfuegen: " + currentNumber : "Insert: " + currentNumber);   	
        	
        	// Apply the Hash Functions to the current element
        	for(int j = 0; j < numberOfHashFunctions; j++) {
        		
        		// Beschreibung (nur beim allerersten Durchlauf)
        		if(i==0 && j==0) {
        			
        			insertDescriptionBox.show();
        			insertDescriptionText1.show();
        	        insertDescriptionText2.show();
        	        
 
        		}
        		
        		// Step 1: Create arrow from the current number to the currently used hash function
        		Node nodeUnderCurrentNumber = new Offset(0, 0, "currentNumberAsText", AnimalScript.DIRECTION_S);
        		Node nodeOverHash = new Offset(0, 0, "hashFunctionAsText_" + j, AnimalScript.DIRECTION_N);
        		arrowsNumberToHash[j] = lang.newPolyline(new Node[]{nodeUnderCurrentNumber, nodeOverHash}, "arrowNumberToHash_" + j, null, arrowStyle);
        		src1.highlight(8);
        		
        		
        		// Step 2: Calculate the Hash
        		lang.nextStep();
        		hashResults[j] = simpleHash(currentNumber, primesForHash[j], bloomFilterSize);
        		hashResultsAsText[j].setText("f(" + currentNumber + ") = " + hashResults[j], null, null);
        		src1.unhighlight(8);
        		src1.highlight(2);
        		
        		// Step 3: Create arrow from the hash result to the cell in the bloom filter
        		lang.nextStep();
        		Node nodeUnderHash = new Offset(0, 15, "hashFunctionAsText_" + j, AnimalScript.DIRECTION_S);
        		Node nodeOverBloom = new Offset(0, 0, "bloomFilterCell_" + hashResults[j], AnimalScript.DIRECTION_N);
        		arrowsHashToBloom[j] = lang.newPolyline(new Node[]{nodeUnderHash, nodeOverBloom}, "arrowNumberToHash_" + j, null, arrowStyle);
        		fancyMarkers[j] = createFancyMarker("bloomFilterCell_" + hashResults[j], bloomFilterSquareSize, lang, "fancyMarker_" + j);
        		src1.unhighlight(2);
        		src1.highlight(9);
        		
        		
        		// Finally, update the content of the bloom filter
        		bloomFilter[hashResults[j]] = 1;
        		
        		// Beschreibung (nur beim allerersten Durchlauf)
        		lang.nextStep();
        		src1.unhighlight(9);
        		if(i==0 && j==0) {
        		
        	        insertDescriptionBox.hide();
        	        insertDescriptionText1.hide();
        	        insertDescriptionText2.hide();
        			
        		}
        		
        		
        	}
        	
        	
        	lang.nextStep();
        	
        	// Move the number we are currently working on to the "already inserted" -  section
        	alreadyInserted.add(currentNumber);
        	alreadyInsertedAsText.setText(arrayListToString(alreadyInserted), null, null);
        	
        	// Remove temporary Objects (it's easier to delete than to hide, move and unhide for the next round)
        	hideArrayOfPrimitives(arrowsNumberToHash);
        	hideArrayOfPrimitives(arrowsHashToBloom);
        	eraseTextArray(hashResultsAsText);
        	hideArrayOfPrimitives(fancyMarkers);
        	currentNumberAsText.setText("", null, null);
        	
        	// Mark the cells we changed to 1 for this number
        	for(int j = 0; j < numberOfHashFunctions; j++) {
        		bloomFilterCells[hashResults[j]].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, bloomFilterIsOne, null, null);
        	}
        	
        	
        }
        
        
        // +++++++++++++++++++
        // Animation: Check
        // +++++++++++++++++++
        
        
        
        
        // Show the changes: New source code and some text
        SourceCode src2 = lang.newSourceCode(										// Second source code: For check								
        		new Offset(0, 50, sourcecodeText, AnimalScript.DIRECTION_NW), 
        		"sourceCode2", null, sourceCodeStyle);
        src2.addCodeLine("// Simple Hash-Function                                                               ", null, 0, null); // 0
        src2.addCodeLine("private static int simpleHash(int number, int prime, int modulo) {                    ", null, 0, null);
        src2.addCodeLine("    return (number*prime) % modulo;                                                   ", null, 0, null);
        src2.addCodeLine("}                                                                                     ", null, 0, null);
        src2.addCodeLine("                                                                                     ", null, 0, null);
        src2.addCodeLine("// Method to check if a number is in a bloom filter                                   ", null, 0, null);
        src2.addCodeLine("private static boolean check(int[] bloomFilter, int[] primes, int key) {              ", null, 0, null);
        src2.addCodeLine("    for(int i = 0; i < primes.length; i++) {                                          ", null, 0, null);
        src2.addCodeLine("        int hash = simpleHash(key, primes[i], bloomFilter.length);                    ", null, 0, null);
        src2.addCodeLine("        if (bloomFilter[hash] != 1) return false;                                      ", null, 0, null); // 9
        src2.addCodeLine("    }                                                                                 ", null, 0, null);
        src2.addCodeLine("    return true;                                                                     ", null, 0, null); // 11
        src2.addCodeLine("}                                                                                     ", null, 0, null);
        src2.hide();
        
        Text numbersToCheckAsText = lang.newText(new Offset(0, -5, "numbersToInsertCaption", AnimalScript.DIRECTION_SW), "", "numbersToCheckAsText", null, standardTextStyle);
        numbersToCheckAsText.hide();
        

        
        Text resultText = lang.newText(new Offset(0, 100, "bloomfiltertext", AnimalScript.DIRECTION_NW), 
        		(loc == Locale.GERMANY) ? "Ergebnis:" : "Result: ", "resulttext", 
        		null, largeTextStyle);
        
        resultText.hide();
        
        Text acceptedOrNotText = lang.newText(new Offset(150, 3, "resulttext", AnimalScript.DIRECTION_NW), "", "acceptedOrNotText", null, standardTextStyle);
        acceptedOrNotText.hide();
        
        if (numbersToCheck.length != 0) {
        	
        	src2.show();
        	numbersToCheckAsText.show();
        	
	        // First, set-up the animation page
	        RectProperties coverProperties = new RectProperties();
	        coverProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
	        coverProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
	        coverProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);        
	        Rect temporaryCover = lang.newRect(
	        		new Coordinates(0,0), 
	        		new Offset(0, 1000, src1, AnimalScript.DIRECTION_SE), 
	        		"temporaryCover", null, coverProperties);
	        
	        
	        
	        captionShadow.hide();
	        captionBox.hide();
	        
	        
	        
	        
	        captionShadow = lang.newRect(new Coordinates(23, 23), new Coordinates(223, 53), "descriptionCaptionShadow", null, ShadowProperties);
	    	captionBox = lang.newRect(new Coordinates(17, 17), new Coordinates(217, 47), "descriptionShadowBackground", null, BackgroundBoxProperties);
	        lang.newText(new Coordinates(20, 20), "Bloom Filter - Check", "checkCaption", null, captionStyle);
	        lang.nextStep((loc == Locale.GERMANY) ? "Testen, ob Elemente im Filter enthalten sind" : "Check for membership");
	        
	        
	        
	        
	       
	        
	        
	        // Hide what we don't need anymore: Old source code and text
	        temporaryCover.hide();
	        src1.hide();
	        numbersToInsertAsText.hide();
	        animationCaption.hide();
	        
	       
	
	        toBeProcessed.setText((loc == Locale.GERMANY) ? "Noch zu ueberpruefen:" : "To be checked:", null, null);
	        
	        
	        // Result
	        resultText.show();
	        acceptedOrNotText.show();
	        
	        
	        
	        // Create a description for the new numbers
	        Rect checkDescriptionBox = lang.newRect(
	        		new Offset(0, 50, "elements", AnimalScript.DIRECTION_SW), 
	        		new Offset(530, 25, "elements", AnimalScript.DIRECTION_SW), "numbersdescriptionbox", null, descriptionRectProperty);
	        Text checkDescriptionText = lang.newText(new Offset(5, 5, "numbersdescriptionbox", AnimalScript.DIRECTION_NW), 
	        		(loc == Locale.GERMANY) ? "Hier werden jetzt die Elemente angezeigt, die auf Mitgliedschaft im Bloom-Filter getestet werden." : "Now the elements that are tested for membership are shown in the topmost row.", 
	        		"numbersdescriptiontext", null);
	        
	        Rect checkHighlightBox1 = lang.newRect(new Offset(0, 5, "numbersToInsertAsText", AnimalScript.DIRECTION_NW), new Offset(80, 25, "numbersToInsertAsText", AnimalScript.DIRECTION_NW), "numbersHighlightBox1", null, ShadowProperties);
	        Rect checkHighlightBox2 = lang.newRect(new Offset(0, 5, "currentNumberAsText", AnimalScript.DIRECTION_NW), new Offset(80, 25, "currentNumberAsText", AnimalScript.DIRECTION_NW), "numbersHighlightBox2", null, ShadowProperties);
	        
	        lang.nextStep();
	        checkDescriptionBox.hide();
	        checkDescriptionText.hide();
	        checkHighlightBox1.hide();
	        checkHighlightBox2.hide();
	        
	        
	        // Display the numbers we want to check
	        numbersToCheckAsText.setText(arrayListToString(notYetChecked), null, null);
	        lang.nextStep();
        
        }
        
        
        // Go through the number we want to check against the bloom filter
        for(int i = 0; i < numbersToCheck.length; i++) {
        	
        	// Make some data structures we need during the loop
        	Polyline[] arrowsNumberToHash = new Polyline[numberOfHashFunctions];
        	Polyline[] arrowsHashToBloom = new Polyline[numberOfHashFunctions];
        	int[] hashResults = new int[numberOfHashFunctions];
        	Group[] fancyMarkers = new Group[numberOfHashFunctions];
        	boolean accepted = true;
        	
        	// Take the first number of "notYetChecked" and make it the number we are checking this turn
        	currentNumber = notYetChecked.remove(0);
        	
        	// Update the display
        	numbersToCheckAsText.setText(arrayListToString(notYetChecked), null, null);
        	currentNumberAsText.setText(Integer.toString(currentNumber), null, null);
        	
        	
        	// Apply the Hash Functions to the current element
        	for(int j = 0; j < numberOfHashFunctions; j++) {
        		
        		// Step 1: Create arrow from current number to the currently used hash function
        		if(j == 0) {
        			lang.nextStep("Check: " + currentNumber);
        		} else {
        			lang.nextStep();
        		}
        		
        		Node nodeUnderCurrentNumber = new Offset(0, 0, "currentNumberAsText", AnimalScript.DIRECTION_S);
        		Node nodeOverHash = new Offset(0, 0, "hashFunctionAsText_" + j, AnimalScript.DIRECTION_N);
        		arrowsNumberToHash[j] = lang.newPolyline(new Node[]{nodeUnderCurrentNumber, nodeOverHash}, "arrowNumberToHash_" + j, null, arrowStyle);
        		src2.highlight(8);
        		
        		// Step 2: Calculate Hash
        		lang.nextStep();
        		hashResults[j] = simpleHash(currentNumber, primesForHash[j], bloomFilterSize);
        		hashResultsAsText[j].setText("f(" + currentNumber + ") = " + hashResults[j], null, null);
        		src2.unhighlight(8);
        		src2.highlight(2);
        		
        		// Step 3: Create arrow from the hash result to the cell in the bloom filter
        		lang.nextStep();
        		Node nodeUnderHash = new Offset(0, 15, "hashFunctionAsText_" + j, AnimalScript.DIRECTION_S);
        		Node nodeOverBloom = new Offset(0, 0, "bloomFilterCell_" + hashResults[j], AnimalScript.DIRECTION_N);
        		arrowsHashToBloom[j] = lang.newPolyline(new Node[]{nodeUnderHash, nodeOverBloom}, "arrowNumberToHash_" + j, null);
        		fancyMarkers[j] = createFancyMarker("bloomFilterCell_" + hashResults[j], bloomFilterSquareSize, lang, "fancyMarker_" + j);
        		src2.unhighlight(2);
        		
        		
        		// Step 4: Depending on the result of the check, you might exit the loop
        		if (bloomFilter[hashResults[j]] == 0) {
        			fancyMarkers[j].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, deniedColor, null, null);
        			accepted = false;
        		} else {
        			fancyMarkers[j].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, acceptedColor, null, null);
        		} 
        		
        		
        		// Ask Question
        		if (true) {
        			
        			if (loc == Locale.GERMANY) {
            			MultipleChoiceQuestionModel isAcceptedQuestion = new MultipleChoiceQuestionModel("isAcceptedQuestion_" + i + "_" + j);
                    	isAcceptedQuestion.setPrompt("Mit den aktuell berechneten Hash-Werten fuer die Zahl " + currentNumber + ", welche Aussage k�nnen wir treffen?");
                    	
                    	if (accepted) {
                    		isAcceptedQuestion.addAnswer("Die Zahl ist moeglicherweise enthalten", 1, "Richtig - nach aktuellem Stand koennte die Zahl im Bloom Filter enthalten sein.");
                    		isAcceptedQuestion.addAnswer("Die Zahl ist nicht enthalten", 0, "Falsch - an den Stellen im Bloom Filter, die die Hash-Werte indizieren, ist eine '1' enthalten.");
                    		
                    	} else {
                    		isAcceptedQuestion.addAnswer("Die Zahl ist moeglicherweise enthalten", 0, "Falsch - an der Position des letzten Hash-Wertes steht keine 1, die Zahl kann unmoeglich im Bloom Filter enthalten sein.");
                    		isAcceptedQuestion.addAnswer("Die Zahl ist nicht enthalten", 1, "Richtig - an der Position des letzten Hash-Wertes steht keine 1, die Zahl kann unmoeglich im Bloom Filter enthalten sein.");
                    	}
                		isAcceptedQuestion.addAnswer("Die Zahl ist sicher enthalten", 0, "Falsch - wir koennen NIE sicher sein, dass eine Zahl enthalten ist.");
                		lang.addMCQuestion(isAcceptedQuestion);
                		
        			} else {
            			MultipleChoiceQuestionModel isAcceptedQuestion = new MultipleChoiceQuestionModel("isAcceptedQuestion_" + i + "_" + j);
                    	isAcceptedQuestion.setPrompt("Considering the already calculated hash values for the number " + currentNumber + ", which statement is true?");
                    	
                    	if (accepted) {
                    		isAcceptedQuestion.addAnswer("The number might be contained in the set", 1, "Correct - Considering the hash values calculated to far, the number could be a member of the set.");
                    		isAcceptedQuestion.addAnswer("The number is not contained in the set", 0, "Wrong - there is a '1' at a position in the bloom filter indexed by one of the hash values.");
                    		
                    	} else {
                    		isAcceptedQuestion.addAnswer("The number might be a member of the set", 0, "Wrong - There is no '1' at the position of the last hash value, it is impossible that this number is a member of the set and therefore contained in the Bloom filter.");
                    		isAcceptedQuestion.addAnswer("The number is not contained in the set", 1, "Correct - There is no '1' at the position of the last hash value, it is impossible that this number is a member of the set and therefore contained in the Bloom filter.");
                    	}
                		isAcceptedQuestion.addAnswer("The number is certainly a member of the set", 0, "Wrong - we can NEVER be sure that a number is certainly a member of the set.");
                		lang.addMCQuestion(isAcceptedQuestion);
        			}
        			
        			

        		}
        		
        		
        		// If the element is not in the filter, we don't have to check the remaining hashes
        		if (!accepted) break;
        		
        		
        	}
        	

        	


        	
        	
 
        	
        	// Do something if accepted / denied
        	lang.nextStep();
        	if (accepted) {
        		src2.highlight(11, 0, true); // uses context color, which is conveniently the same as the accepted color
        		acceptedOrNotText.setText((loc == Locale.GERMANY) ? "Das Element " + currentNumber + " ist WAHRSCHEINLICH im Bloom Filter enthalten" : "The element " + currentNumber + " is PROBABLY a member of the set.", null, null);
        		
        	} else {
        		src2.highlight(9);
        		acceptedOrNotText.setText((loc == Locale.GERMANY) ? "Das Element " + currentNumber + " ist SICHER NICHT im Bloom Filter enthalten" : "The element" + currentNumber + " is CERTAINLY not a member of the set.",  null, null);
        		
        	}
        	
        	
        	lang.nextStep();
        	
        	// Remove Objects temporary objects
        	hideArrayOfPrimitives(arrowsNumberToHash);
        	hideArrayOfPrimitives(arrowsHashToBloom);
        	eraseTextArray(hashResultsAsText);
        	hideArrayOfPrimitives(fancyMarkers);
        	currentNumberAsText.setText("", null, null);
        	acceptedOrNotText.setText("", null, null);
        	
        	src2.unhighlight(9);
        	src2.unhighlight(11);
        }
        
        
    	// +++++++++++++++++++++
        // Summary Page
        // +++++++++++++++++++++
        
        
        // Need the values we do calculations with as doubles to avoid integer rounding!
        double m = bloomFilterSize;
        double k = numberOfHashFunctions;
        double n = numbersToInsert.length;
        
        
        lang.hideAllPrimitives();
        src2.hide();
        src1.hide();
        
        // Caption
        captionShadow = lang.newRect(new Coordinates(23, 23), new Coordinates(313, 53), "descriptionCaptionShadow", null, ShadowProperties);
    	captionBox = lang.newRect(new Coordinates(17, 17), new Coordinates(307, 47), "descriptionShadowBackground", null, BackgroundBoxProperties);
        lang.newText(new Coordinates(20, 20), 
        		(loc == Locale.GERMANY) ? "Bloom Filter - Zusammenfassung" : "Bloom Filter - Summary", 
        		"summaryCaption", null, captionStyle);
        
        showArrayOfPrimitives(bloomFilterCells);
        bloomfiltertext.show();
        
        lang.newText(new Offset(0, 50, "animationCaption", AnimalScript.DIRECTION_NW), 
        		(loc == Locale.GERMANY) ? "In der gerade gezeigten Animation wurde erklaert, wie Elemente in einen Bloom-Filter eingefuegt werden und wie geprueft wird, ob Elemente enthalten sind." : "In the previously shown animation you have seen how elements are inserted in a Bloom filter and how to check for membership. ", 
        		"summary1", null, standardTextStyle);
        
        lang.newText(new Offset(0, 25, "summary1", AnimalScript.DIRECTION_NW), 
        		(loc == Locale.GERMANY) ? "Zum Abschluss soll noch kurz auf falsch positive Ergebnisse eingegangen werden." : "Finally, we briefly want to consider false-positive results. ", 
        		"summary2", null, standardTextStyle);
        
        lang.newText(new Offset(0, 40, "summary2", AnimalScript.DIRECTION_NW), 
        		(loc == Locale.GERMANY) ? "Allgemein ist die Chance, dass in einem Bloom Filter mit Laenge m nach Einfuegen von n Elementen ueber k Hash-Funktionen ein Bit gleich 1 ist:" : "In general, the chance that a bit is 1 in a Bloom filter of length m after inserting n elements using k hash functions, is given by: ", 
        		"summary3", null);
        
        lang.newText(new Offset(100, 25, "summary3", AnimalScript.DIRECTION_NW), "p = 1 - (1 - 1/m)^kn", "summary4", null);
        
        lang.newText(new Offset(0, 75, "summary3", AnimalScript.DIRECTION_NW), 
        		(loc == Locale.GERMANY) ? "In unserem Fall erwarten wir mit m = " + bloomFilterSize + ", n = " + numbersToInsert.length + " und k = " + numberOfHashFunctions + " einen Wert von" : "In our case with m = " + bloomFilterSize + ", n = " + numbersToInsert.length + " and k = " + numberOfHashFunctions + " we expect a value of", 
        		"summary5", null);
        
        double pExpected = 1 - (Math.pow(1 - 1/m, n * k));
        lang.newText(new Offset(100, 25, "summary5", AnimalScript.DIRECTION_NW), "p = " + pExpected, "summary6", null);
        
        
        double sum = 0.0;
        for(int i : bloomFilter) sum += i;
        double pActual = sum / m;
        lang.newText(new Offset(0, 225, "summary5", AnimalScript.DIRECTION_NW), 
        		(loc == Locale.GERMANY) ? "Der tatsaechliche Wert, da " + (int)sum + " der insgesamt " + bloomFilterSize + " Zellen einen Wert von 1 haben, betraegt: " : "The actual value, since " + (int)sum + " of " + bloomFilterSize + " bits have a value of 1, is: ", 
        		"summary7", null);
        
        lang.newText(new Offset(100, 25, "summary7", AnimalScript.DIRECTION_NW), "p' = " + pActual, "summary8", null);
        
        
        lang.newText(new Offset(0, 75, "summary7", AnimalScript.DIRECTION_NW), 
        		(loc == Locale.GERMANY) ? "Dies ist somit die Wahrscheinlichkeit, dass das Ergebnis einer Hash-Funktion auf ein auf 1 gesetztes Bit zeigt." : "Consequently, this is the probability that the result of a hash function points to a bit with a value of 1.", 
        		"summary9", null);
        
        lang.newText(new Offset(0, 25, "summary9", AnimalScript.DIRECTION_NW), 
        		(loc == Locale.GERMANY) ? "Die Chance, dass eine Zahl bei k Hash-Funktionen falsch-positiv getestet wird, betraegt somit:  " : "It follows that the chance for a false-positive result using k hash functions simply is: ", 
        		"summary10", null);
        
        double fp = Math.pow(pActual, k);
        lang.newText(new Offset(100, 25, "summary10", AnimalScript.DIRECTION_NW), "fp = p' ^ k = " + fp, "summary11", null);
        
        lang.nextStep((loc == Locale.GERMANY) ? "Zusammenfassung" : "Summary");
        lang.finalizeGeneration();
        return lang.toString();
    }

    public String getName() {
        return "Bloom-Filter";
    }

    public String getAlgorithmName() {
        return "Bloom-Filter";
    }

    public String getAnimationAuthor() {
        return "Sandra Sch�fer, Jonas Schatz";
    }
    
    
    public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
    	

        bloomFilterSize = (Integer)primitives.get("bloomFilterSize");
        numbersToCheck = (int[])primitives.get("numbersToCheck");
        primesForHash = (int[])primitives.get("primesForHash");
        numbersToInsert = (int[])primitives.get("numbersToInsert");
        bloomFilterSquareSize = (Integer)primitives.get("bloomFilterSquareSize");
        
        if (bloomFilterSize <2  || bloomFilterSize > 50) {
        	throw new IllegalArgumentException((loc == Locale.GERMANY) ? "Verwende eine Zahl zwischen 2 und 50 fuer die Laenge des Bloom Filter Arrays" : "Use a number between 2 and 50 for the length of the Bloom filter array.");
        }
        
        if (primesForHash.length == 0) {
        	throw new IllegalArgumentException((loc == Locale.GERMANY) ? "Es wird mindestens eine Primzahl fuer die Hash Funktionen benoetigt" : "You have to use at least one hash function.");
        }
        
        for (int prime : primesForHash) {
        	if (!BigInteger.valueOf(prime).isProbablePrime(10000)) {
        		throw new IllegalArgumentException((loc == Locale.GERMANY) ? "Fuer die Hash-Funktionen muessen Primzahlen verwendet werden. " + prime + " ist keine Primzahl." : "You have to provide prime numbers for the Hash Functions. " + prime + " is not a prime.");
        	}
        }
        
        if (numbersToCheck.length == 0 && numbersToInsert.length == 0) {
        	throw new IllegalArgumentException((loc == Locale.GERMANY) ? "Mindestens eine Zahl muss eingefuegt oder auf Enthaltensein in der Menge ueberprueft werden." : "Provide at least one number to insert or one number to check for membership.");
        }
        
        if (numbersToCheck.length > 20 && numbersToInsert.length > 20) {
        	throw new IllegalArgumentException((loc == Locale.GERMANY) ? "Verwende 20 oder weniger Zahlen beim Einfuegen oder Ueberpruefen, um die Animation nicht zu lange werden zu lassen" : "Use 20 or less numbers to insert or check to keep the animation at a reasonable length.");
        }
    	
        if (bloomFilterSquareSize <= 5) {
        	System.out.println(bloomFilterSquareSize);
        	throw new IllegalArgumentException((loc == Locale.GERMANY) ? "bloomFilterSquareSize muss positiv und >5 sein, um die Kaestchen anzeigen zu koennen." : "bloomFilterSquareSize has to be positive and >5 to actually show the content of the squares");
        }
        
    	return true;
    }

    public String getDescription(){
    	if(loc == Locale.GERMANY) {
	        return "Der Bloom-Filter ist eine probabilistische Datenstruktur. Er �berpr�ft, ob ein Element in einer Menge \n" 
	        		+"an Elementen vorhanden ist, indem mit mehreren Hash-Funktionen eine Signatur der Elemente in \n"
	        		+"einer Hashtabelle gespeichert wird. \n"
	        		+"\n"
	        		+"Das Einf�gen und �berpr�fen von Werten erfolgt mit konstanter Geschwindigkeit. Jedoch gibt es \n"
	        		+"falsch-positive Ergebnisse, d.h. es kann nicht mit Sicherheit gesagt werden, ob ein Element schon \n"
	        		+"in der Menge enthalten ist. Wenn ein Element nicht in der Menge enthalten ist, wird dies jedoch mit \n"
	        		+"Sicherheit erkannt. \n"
	        		+"\n"
	        		+"Die Genauigkeit des Bloom-Filters kann durch Verwendung einer l�ngeren Hashtabelle gesteigert \n"
	        		+"werden. \n"
	        		+"\n"
	        		+"Quelle: https://de.wikipedia.org/wiki/Bloomfilter";
        } else {
        	return "The Bloom filter is a probabilistic data structure. It is used to check whether an element is \n"
        			+ "contained in a set, by calculating a signature of the elements already in the set using hash \n"
        			+ "functions and saving these signatures as a hash table. \n"
        			+ "\n"
        			+ "The cost of inserting and checking for membership is constant. However, when checking \n"
        			+ "for membership, there might be false-positive results: We can't be certain if an element is \n" 
        			+ "contained in a set. We can only be certain if it is not contained. \n"
        			+ "\n"
        			+ "The rate of false-positive results and with it the precision of the Bloom filter can be \n"
        			+ "improved using a longer hash table. \n"
        			+ "\n"
        			+ "Source: https://en.wikipedia.org/wiki/Bloom_filter";
        }
	        			
    }

    public String getCodeExample(){
        return "public class BloomFilterSourceCode{"
			 +"\n"
			 +"		"
			 +"\n"
			 +"	// Simple Hash-Function"
			 +"\n"
			 +"	private static int simpleHash(int number, int prime, int modulo) {"
			 +"\n"
			 +"		return (number*prime) % modulo;"
			 +"\n"
			 +"	}"
			 +"\n"
			 +"\n"
			 +"	// Method to insert a number into a bloom filter"
			 +"\n"
			 +"	private static void put(int[] bloomFilter, int[] primes, int key) {"
			 +"\n"
			 +"		for(int i = 0; i < primes.length; i++) {"
			 +"\n"
			 +"			int hash = simpleHash(key, primes[i], bloomFilter.length);"
			 +"\n"
			 +"			bloomFilter[hash] = 1;"
			 +"\n"
			 +"		}"
			 +"\n"
			 +"	}"
			 +"\n"
			 +"	"
			 +"\n"
			 +"	// Method to check if a number is in a bloom filter"
			 +"\n"
			 +"	private static boolean check(int[] bloomFilter, int[] primes, int key) {"
			 +"\n"
			 +"		for(int i = 0; i < primes.length; i++) {"
			 +"\n"
			 +"			int hash = simpleHash(key, primes[i], bloomFilter.length);"
			 +"\n"
			 +"			if (bloomFilter[hash] != 1) return false;"
			 +"\n"
			 +"		}"
			 +"\n"
			 +"		return true;"
			 +"\n"
			 +"	}"
			 +"\n"
			 +"\n"
			 +"	"
			 +"\n"
			 +"	public static void main(String[] args) {"
			 +"\n"
			 +"		//Parameters"
			 +"\n"
			 +"		int[] insertedKeys = {11, 42};"
			 +"\n"
			 +"		int[] testedKeys = {10, 11};"
			 +"\n"
			 +"		int[] primes = {7, 11, 13};"
			 +"\n"
			 +"		int[] bloomFilter = new int[20];"
			 +"\n"
			 +"		"
			 +"\n"
			 +"		// Insert the keys	"
			 +"\n"
			 +"		for(int i = 0; i < insertedKeys.length; i++) {"
			 +"\n"
			 +"			put(bloomFilter, primes, insertedKeys[i]);"
			 +"\n"
			 +"		}"
			 +"\n"
			 +"		"
			 +"\n"
			 +"		// Check the keys"
			 +"\n"
			 +"		for(int i = 0; i < testedKeys.length; i++) {"
			 +"\n"
			 +"			boolean isInFilter = check(bloomFilter, primes, testedKeys[i]);"
			 +"\n"
			 +"			System.out.println(\"Is \" + testedKeys[i] + \" in the filter? \" + isInFilter);"
			 +"\n"
			 +"		}"
			 +"\n"
			 +"	}"
			 +"\n"
			 +"}";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return loc;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_DATA_STRUCTURE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }
    
    public BloomFilterGenerator(Locale locale) {
    	loc = locale;
    }
    
    public BloomFilterGenerator() {
    	loc = Locale.GERMAN;
    }
    //public static void main(String[] args) {
    //	Generator generator = new BloomFilterGenerator();
    //	Animal.startGeneratorWindow(generator);
    //}

}