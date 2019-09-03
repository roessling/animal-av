package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;

public class Bubblesort implements Generator {
	
	public static final int WHITESPACE_X = 5;
	public static final int WHITESPACE_Y = 7;
	public static final int DIAGRAM_MOVE_BY = 65;
	
	public static final Coordinates diagramCoordinates = new Coordinates(640, 100);
	public static final Coordinates sourceCodeCoordinates = new Coordinates(40, 80);
	public static final Coordinates headerCoordinates = new Coordinates(30, 30);
	public static final Coordinates descriptionCoordiantes = new Coordinates(40, 100);
	public static final Coordinates endCoordiantes = new Coordinates(40, 100);
	public static final Coordinates statisticCoordiantes = new Coordinates(40, 400);

	public static final Font headerFont = new Font("Monospaced", Font.BOLD, 26);
	public static final Font textFont = new Font("Monospaced", Font.PLAIN, 18);
	public static final Font sourceCodeFont = new Font("Monospaced", Font.PLAIN, 18);
	public static final Font arrayFont = new Font("Monospaced", Font.PLAIN, 16);
	
    private Language lang;
    private int[] intArray;
    private ArrayProperties arrayProperties;
    private TextProperties textProperties;
    private TextProperties headerProperties;
    private SourceCodeProperties sourceCodeProperties;
    private ArrayMarkerProperties arrayMarkerIProperties;
    private ArrayMarkerProperties arrayMarkerJProperties;
    private PolylineProperties arrowProperties;
    
    private ArrayList<IntArray> arrayList;
    private ArrayList<Polyline> arrowList;
    private ArrayList<Text> textList;
    private SourceCode sourceCode;
    private Text[] descriptionText;
    private Text[] statisticText;
    private ArrayList<ArrayMarker> markerI;
    private ArrayList<ArrayMarker> markerJ;
	
	private int iterationCounter;
	private int swapCounter;
	private int compareCounter;
	
	private FontRenderContext frc;
	
	private String[] description = {
			"",
			"Bubblesort ist ein Algorithmus, der eine Liste von Elementen durch Vergleichen sortiert.", 
			"Dieses Sortierverfahren arbeitet in-place, das bedeutet, dass der Algorithmus die Eingabe",
			"mit der Ausgabe überschreibt.",
			"",
			"Die Sortierung erfolgt durch das Durchlaufen der Liste von Links nach Rechts. Hierbei wird",
			"jeweils das aktuelle Element mit dem rechten Nachbarn verglichen. Ist das aktuelle Element",
			"größer als der Nachbar so findet ein Tausch statt. Anschließend wird mit dem rechten Element",
			"fortgefahren. Da das größte Element am Ende eines jeden Listendurchlaufes ganz rechts steht,",
			"muss das letzte Element beim nächsten Durchgang nicht mehr beachtet werden.",
			"Somit ist die Sortierung nach n Durchläufen korrekt abgeschlossen,",
			"wobei n der Anzahl der Elemente in der Liste -1 entspricht.",
			"",
			"Bubblesort wird in der Praxis kaum eingesetzt,da er ein sehr schlechtes Laufzeitverhalten aufweist.",
			"In der Lehre erfreut er sich dagegen sehr großer Beliebtheit, da er sehr leicht verständlich ist.",
			"",
			"Im Folgenden wird der Algorithmus Bubblesort mit jeweils 5 Schritten des Tauschbaumes dargestellt.",
			"Am Ende der Animation findet sich dann der vollständige Tauschbaum."};
	
	private String[] code = 	   {"public void bubbleSort(int[] array) {",	//1
				"for (int i = array.length - 1; i > 0; i--) {",					//2
					"for (int j = 0; j < i; j++) {",							//3
						"if (array[j] > array[j + 1]) {",						//4
							"int temp = array[j];",								//5						
							"array[j] = array[j + 1];",							//6
							"array[j + 1] = temp;",								//7
						"}",													//8
					"}",														//9
				"}",															//10
			"}"};																//11
	
    public void init(){
        lang = new AnimalScript("Bubble Sort[DE]", "Christoph Schüßler, Marcel Frank", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	intArray = (int[])primitives.get("intArray");
    	arrayProperties = (ArrayProperties)props.getPropertiesByName("arrayProperties");
    	textProperties = (TextProperties)props.getPropertiesByName("textProperties");
    	sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProperties");
    	arrayMarkerIProperties = (ArrayMarkerProperties)props.getPropertiesByName("arrayMarkerIProperties");
        arrayMarkerJProperties = (ArrayMarkerProperties)props.getPropertiesByName("arrayMarkerJProperties");
        arrowProperties = (PolylineProperties)props.getPropertiesByName("arrowProperties");
        
        textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(((Font) textProperties.get(AnimationPropertiesKeys.FONT_PROPERTY)).getName(), textFont.getStyle(), textFont.getSize()));
        arrayProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(((Font) arrayProperties.get(AnimationPropertiesKeys.FONT_PROPERTY)).getName(), arrayFont.getStyle(), arrayFont.getSize()));
        sourceCodeProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(((Font) sourceCodeProperties.get(AnimationPropertiesKeys.FONT_PROPERTY)).getName(), sourceCodeFont.getStyle(), sourceCodeFont.getSize()));
        headerProperties = new TextProperties();
        headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(((Font) headerProperties.get(AnimationPropertiesKeys.FONT_PROPERTY)).getName(), headerFont.getStyle(), headerFont.getSize()));

        swapCounter = 0;
        iterationCounter = 0;
        compareCounter = 0;
        markerI = new ArrayList<ArrayMarker>();
    	markerJ = new ArrayList<ArrayMarker>();
    	arrayList = new ArrayList<IntArray>();
    	arrowList = new ArrayList<Polyline>();
    	textList = new ArrayList<Text>();
    	frc = new FontRenderContext(new AffineTransform(),true,false);
    	lang.setStepMode(true);
    	
    	//header
    	lang.newText(headerCoordinates, "Bubblesort mit Tauschdiagramm", "title", null, headerProperties);
    	Coordinates[] line = {new Coordinates(headerCoordinates.getX(), headerCoordinates.getY() + headerFont.getSize()), new Coordinates(headerCoordinates.getX()+465,headerCoordinates.getY() + headerFont.getSize())};
    	lang.newPolyline(line, "line", null);	
    	//description
    	descriptionText = getText(description, descriptionCoordiantes, textProperties, textFont.getSize());
    			
    	lang.nextStep("Beschreibung");
    			
    	//hide description
    	for (int i = 0; i < descriptionText.length; i++) {
    		descriptionText[i].hide();
    	}
        bubblesort();
        return lang.toString();
    }

    public String getName() {
        return "Bubble Sort  mit Tauschbaum [DE]";
    }

    public String getAlgorithmName() {
        return "Bubble Sort";
    }

    public String getAnimationAuthor() {
        return "Christoph Schüßler, Marcel Frank";
    }

    public String getDescription(){
        return "Bubblesort ist ein Algorithmus, der eine Liste von Elementen durch Vergleichen sortiert. "
 +"\n"
 +"Dieses Sortierverfahren arbeitet in-place, das bedeutet, dass der Algorithmus die Eingabe"
 +"\n"
 +"mit der Ausgabe &uuml;berschreibt. Die Sortierung erfolgt durch das Durchlaufen der Liste von "
 +"\n"
 +"Links nach Rechts. Hierbei wird jeweils das aktuelle Element mit dem rechten Nachbarn verglichen. "
 +"\n"
 +"Ist das aktuelle Element gr&ouml;&szlig;er als der Nachbar, so findet ein Tausch statt. Anschlie&szlig;end wird mit"
 +"\n"
 +"dem rechten Element fortgefahren. Da das gr&ouml;&szlig;te Element am Ende eines jeden Listendurchlaufes "
 +"\n"
 +"ganz rechts steht, muss das letzte Element beim n&auml;chsten Durchgang nicht mehr beachtet werden. "
 +"\n"
 +"Somit ist die Sortierung nach n Durchl&auml;ufen korrekt abgeschlossen, wobei n der Anzahl der Elemente "
 +"\n"
 +"in der Liste -1 entspricht. Bubblesort wird in der Praxis kaum eingesetzt, da er ein sehr schlechtes "
 +"\n"
 +"Laufzeitverhalten aufweist. In der Lehre erfreut er sich dagegen sehr gro&szlig;er Beliebtheit, da er "
 +"\n"
 +"sehr leicht verst&auml;ndlich ist. Im Folgenden wird der Algorithmus Bubblesort mit jeweils 5 Schritten "
 +"\n"
 +"des Tauschbaumes dargestellt. Am Ende der Animation findet sich dann der vollst&auml;ndige Tauschbaum. ";
    }

    public String getCodeExample(){
        return "public void bubbleSort(int[] array) {"
 +"\n"
 +"     for (int i = array.length - 1; i > 0; i--) {"
 +"\n"
 +"          for (int j = 0; j < i; j++) {"
 +"\n"
 +"               if (array[j] > array[j + 1]) {"
 +"\n"
 +"                    int temp = array[j];"
 +"\n"
 +"                    array[j] = array[j + 1];"
 +"\n"
 +"                    array[j + 1] = temp;"
 +"\n"
 +"               }"
 +"\n"
 +"          }"
 +"\n"
 +"     }"
 +"\n"
 +"}";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }
    
    /**
     * Main method. Starts the sorting animation.
     */
    public void bubblesort() {	
		//source code
		sourceCode = lang.newSourceCode(sourceCodeCoordinates, "sourceCode", null, sourceCodeProperties);
		addCode(sourceCode, code);
		
		//init lists
		arrayList.add(0, lang.newIntArray(diagramCoordinates, intArray, "array", null, arrayProperties));
		textList.add(0, lang.newText(new Coordinates(diagramCoordinates.getX()-20, diagramCoordinates.getY()), "0", "text", null));
				
		markerJ.add(lang.newArrayMarker(arrayList.get(0), 0, "j", null, arrayMarkerJProperties));
		markerJ.get(0).hide();
		
		//begin sort
		sourceCode.highlight(0);
		updateStatistic();
		lang.nextStep();
		markerI.add(0,lang.newArrayMarker(arrayList.get(0), intArray.length-1, "i", null, arrayMarkerIProperties));
		for (int i = 1; i < arrayList.get(0).getLength(); i++) {
			compareCounter++;
			updateStatistic();
			markerI.get(0).move(intArray.length-i, null, null);
			sourceCode.toggleHighlight(0, 1);
			arrayList.get(0).highlightCell(intArray.length - i + 1, intArray.length-1, null, null);
			lang.nextStep(i + ". Durchlauf");
			markerJ.add(0, lang.newArrayMarker(arrayList.get(0), markerJ.get(0).getPosition(), "j", null, arrayMarkerJProperties));
			for (int j = 0; j < arrayList.get(0).getLength() - i; j++) {
				boolean swap = false;
				compareCounter++;
				updateStatistic();
				markerJ.get(0).move(j, null, null);;
				arrayList.get(0).unhighlightElem(j-1, j, null, null);
				sourceCode.toggleHighlight(1, 2);
				lang.nextStep();
				sourceCode.toggleHighlight(2, 3);
				arrayList.get(0).highlightElem(j, null, null);
				arrayList.get(0).highlightElem(j+1, null, null);
				iterationCounter++;
				compareCounter++;
				updateStatistic();
				lang.nextStep();
				arrayList.add(0, copyArray());
				markerI.get(0).hide();
				markerJ.get(0).hide();
				markerI.add(0, lang.newArrayMarker(arrayList.get(0), markerI.get(0).getPosition(), "i", null, arrayMarkerIProperties));
				markerJ.add(0, lang.newArrayMarker(arrayList.get(0), markerJ.get(0).getPosition(), "j", null, arrayMarkerJProperties));
				if (arrayList.get(0).getData(j) > arrayList.get(0).getData(j+1)) {
					swap = true;
				}
				sourceCode.unhighlight(3);			
				textList.add(0,lang.newText(new Coordinates(diagramCoordinates.getX()-20, diagramCoordinates.getY()-DIAGRAM_MOVE_BY), iterationCounter + "", "text", null));	
				arrayList.get(0).highlightElem(j, null, null);
				arrayList.get(0).highlightElem(j+1, null, null);
				arrayList.get(0).highlightCell(arrayList.get(0).getLength()-i+1, arrayList.get(0).getLength()-1, null, null);
				moveDown();
				arrow(j, swap);			
				if(swap){
					arrayList.get(0).swap(j, j+1, new MsTiming(250), new MsTiming(300));
					arrayList.get(0).hide();
					IntArray temp = copyArray();
					arrayList.remove(0);
					arrayList.add(0, temp);
					markerI.get(0).hide();
					markerJ.get(0).hide();
					markerI.add(0, lang.newArrayMarker(arrayList.get(0), markerI.get(0).getPosition(), "i", null, arrayMarkerIProperties));
					markerJ.add(0, lang.newArrayMarker(arrayList.get(0), markerJ.get(0).getPosition(), "j", null, arrayMarkerJProperties));
					arrayList.get(0).highlightElem(j, null, null);
					arrayList.get(0).highlightElem(j+1, null, null);
					arrayList.get(0).highlightCell(arrayList.get(0).getLength()-i+1, arrayList.get(0).getLength()-1, null, null);				
					swapCounter++;
					updateStatistic();
					sourceCode.highlight(4);
					sourceCode.highlight(5);
					sourceCode.highlight(6);
					sourceCode.unhighlight(3);
					lang.nextStep();
				}
				sourceCode.unhighlight(4);
				sourceCode.unhighlight(5);
				sourceCode.unhighlight(6);
				sourceCode.highlight(2);
			}
			compareCounter++;
			updateStatistic();
			lang.nextStep();
			markerJ.get(0).hide();
			sourceCode.unhighlight(2);
			arrayList.get(0).unhighlightElem(arrayList.get(0).getLength()-1-i, arrayList.get(0).getLength()-i, null, null);
		}
		compareCounter++;
		updateStatistic();
		sourceCode.highlight(1);
		lang.nextStep();
		sourceCode.unhighlight(1);
		markerI.get(0).hide();
		arrayList.get(0).highlightCell(1, null, null);
		lang.nextStep();
		//arraymarker
		for (int i = 0; i < markerI.size(); i++) {
			markerI.get(i).hide();
		}
		for (int i = 0; i < markerJ.size(); i++) {
			markerJ.get(i).hide();
		}
		if (arrayList.size() > 5) {
			moveEnd();
			lang.nextStep("kompletter Tauschbaum");
		}	
		sourceCode.hide();
		for (int i = 0; i < statisticText.length; i++) {
			statisticText[i].hide();
		}
		//array
		for (int i = 0; i < arrayList.size(); i++) {
			arrayList.get(i).hide();	
		}
		//arrow
		for (int i = 0; i < arrowList.size(); i++) {
			arrowList.get(i).hide();
		}
		//text
		for (int i = 0; i < textList.size(); i++) {
			textList.get(i).hide();
		}
		getText(endString(compareCounter, swapCounter), endCoordiantes, textProperties, textFont.getSize());
		lang.newIntArray(new Coordinates(endCoordiantes.getX(), endCoordiantes.getY()+textFont.getSize()+10), intArray, "array", null, arrayProperties);
		arrayList.add(0, copyArray());
		arrayList.get(0).moveTo(null, null, new Coordinates(endCoordiantes.getX(), endCoordiantes.getY()+textFont.getSize()*7+10), null, null);
		markerJ.get(0).hide();
		markerI.get(0).hide();
		lang.nextStep("Zusammenfassung");
	}
	
    /**
     * Copies the first array in the list
     * @return copied array
     */
	public IntArray copyArray(){	
		int[] array = new int[arrayList.get(0).getLength()];
			for (int i = 0; i < arrayList.get(0).getLength(); i++) {
				array[i] = arrayList.get(0).getData(i);
			}
		return lang.newIntArray(diagramCoordinates, array, "array", null, arrayProperties);
	}
	
	/**
	 * Moves the diagram down
	 */
	public void moveDown(){
		//array
		for (int i = 1; i < arrayList.size(); i++) {
			arrayList.get(i).moveBy(null, 0, DIAGRAM_MOVE_BY, null, new MsTiming(125));
			if (i == 5) {
				arrayList.get(i).hide();
				break;
			}
		}
		//arrow
		for (int i = 0; i < arrowList.size(); i++) {
			arrowList.get(i).moveBy(null, 0, DIAGRAM_MOVE_BY, null, new MsTiming(125));
			if (i == 7) {
				arrowList.get(i).hide();
				arrowList.get(i-1).hide();
				break;
			}
		}
		//text
		for (int i = 0; i < textList.size(); i++) {
			textList.get(i).moveBy(null, 0, DIAGRAM_MOVE_BY, null, new MsTiming(125));
			if (i == 5) {
				textList.get(i).hide();
				break;
			}
		}
	}
	
	/**
	 * Moves and shows the complete diagram
	 */
	public void moveEnd() {
		for (int i = 5; i < arrayList.size(); i++) {
			arrayList.get(i).moveBy(null, 0, (i-5)*DIAGRAM_MOVE_BY, null, null);
			arrayList.get(i).show();
		}
		for (int i = 8; i < arrowList.size(); i++) {
			arrowList.get(i).moveBy(null, 0, (i-8)/2*DIAGRAM_MOVE_BY, null, null);
			arrowList.get(i).show();
		}
		for (int i = 5; i < textList.size(); i++) {
			textList.get(i).moveBy(null, 0, (i-5)*DIAGRAM_MOVE_BY, null, null);
			textList.get(i).show();
		}
	}
	
	/**
	 * Draws 2 arrows
	 * @param i index of first arrow
	 * @param swap true if elements are swapped
	 */
	public void arrow(int i, boolean swap) {		
		int botY = diagramCoordinates.getY() + DIAGRAM_MOVE_BY;
		int topY = diagramCoordinates.getY() + ((int)arrayFont.getStringBounds(String.valueOf(arrayList.get(0).getData(i)),frc).getHeight()) + WHITESPACE_Y;
		
		int botLeftX = diagramCoordinates.getX() + getOffset(i, arrayList.get(0));
		int botRightX = diagramCoordinates.getX() + getOffset(i+1, arrayList.get(0));
		
		int topLeftX, topRightX;
		if (swap) {
			IntArray swappedArray = copyArray();
			swappedArray.swap(i, i + 1, null, null);
			swappedArray.hide();
			
			topLeftX = diagramCoordinates.getX() + getOffset(i+1, swappedArray);	
			topRightX = diagramCoordinates.getX() + getOffset(i, swappedArray);
		} else {
			topLeftX = botLeftX;
			topRightX = botRightX;
		}
			arrowList.add(0,lang.newPolyline(new Coordinates[] {new Coordinates(botLeftX, botY), new Coordinates(topLeftX, topY)}, "arrow", null, arrowProperties));
			arrowList.add(0,lang.newPolyline(new Coordinates[] {new Coordinates(botRightX, botY), new Coordinates(topRightX, topY)}, "arrow", null, arrowProperties));
			
	}	
	
	/**
	 * Calculate offset array[0] -> array[index] in pixel
	 * @param index 
	 * @param array 
	 * @return offset pixel
	 */
	private int getOffset(int index, IntArray array) {
		int offset = 0;
		for (int j = 0; j < index; j++) {
			offset += (arrayFont.getStringBounds(String.valueOf(array.getData(j)), frc).getWidth() + WHITESPACE_X);
		}
		offset += (arrayFont.getStringBounds(String.valueOf(array.getData(index)), frc).getWidth() + WHITESPACE_X)/2;
		return offset;
	}
	
	/**
	 * Convert String[] to Text[]
	 * @param lines text to display
	 * @param coords position
	 * @param props TextProperties
	 * @param fontSize 
	 * @return displayable text
	 */
	public Text[] getText(String[] lines, Coordinates coords,TextProperties props, int fontSize){
		Text[] text = new Text[lines.length];
		for (int i = 0; i < lines.length; i++) {
			text[i] = lang.newText(new Coordinates(coords.getX(), coords.getY()+fontSize*i), lines[i], "text", null, props);
		}
		return text;
	}
	
	/**
	 * Adds text to ScourceCode
	 * @param sourceCode Where to add the code
	 * @param code text to add
	 */
	public void addCode(SourceCode sourceCode, String[] code) {
		int indentation = 0;
		for (int i = 0; i < code.length; i++) {			
			if(code[i].endsWith("{")) {
				sourceCode.addCodeLine(code[i], null, indentation++, null);
			} else if(code[i].endsWith("}")) {
				sourceCode.addCodeLine(code[i], null, --indentation, null);
			} else {
				sourceCode.addCodeLine(code[i], null, indentation, null);
			}
		}
	}
	/**
	 * Shows the final evaluation
	 * @param compareCounter compare the algorithm needed
	 * @param swapCounter swaps the algorithm did
	 * @return text to display
	 */
	public String[] endString (int compareCounter, int swapCounter){
		String[] end = 	{"Der Bubblesort Algorithmus hat die ursprüngliche Liste",
						"",
						"",
						"in " + compareCounter + " Schritten sortiert.",
						"Dabei wurden " + swapCounter + " Vertauschungen durchgeführt.",
						"",
						"Die resultierende Liste lautet:"};
		return end;
	}
	/**
	 * updates the statistics
	 */
	public void updateStatistic() {
		String[] statistic = 	{"",
				"Vergleiche: " + compareCounter,
								"Vertauschungen: " + swapCounter};
		if (statisticText != null) {
			for (int i = 0; i < statisticText.length; i++) {
				statisticText[i].hide();
			}
		}	
		statisticText = getText(statistic, statisticCoordiantes, textProperties, textFont.getSize());
	}  

}
