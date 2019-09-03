/*
 * quicksort.java
 * Christoph Schüßler, Marcel Frank, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
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
import algoanim.util.Node;

public class Quicksort implements Generator {

	public static final int VISIBLE_ELEMENTS = 5;
	public static final int ARRAY_GAP = 20;
	public static final int ARRAY_MOVE_BY = 65;
	public static final int WHITESPACE_X = 5;
	public static final int WHITESPACE_Y = 7;
	
    private Language lang;
    private ArrayMarkerProperties arrayMarkerJProperties;
    private SourceCodeProperties sourceCodeProperties;
    private TextProperties textProperties;
    private PolylineProperties arrowProperties;
    private ArrayMarkerProperties arrayMarkerPivotProperties;
    private ArrayProperties arrayProperties;
    private ArrayProperties workArrayProperties;
    private int[] array;
    private ArrayMarkerProperties arrayMarkerIProperties;
    private TextProperties headerProperties;
    private boolean infoBoolean;
	private SourceCode sourceCode;
	
	private Text[] descriptionText;
	private Text[] statisticText;
    
	private ArrayList<ArrayList<IntArray>> masterList;
	private ArrayList<Polyline> arrowList;
	private ArrayList<Integer> arrowcount;
	private ArrayList<ArrayMarker> markerList;
	private ArrayList<Integer> pivotArray;
	
	private Coordinates sourceCodeCoordinates = new Coordinates(40, 50);
	private Coordinates headerCoordinates = new Coordinates(30, 30);
	private Coordinates arrayCoordinates = new Coordinates(680, 100);
	private Coordinates statisticCoordiantes = new Coordinates(400, 300);
	private Coordinates infoCoordiantes = new Coordinates(400, 480);
	private Coordinates descriptionCoordiantes = new Coordinates(40, 100);
	private Coordinates endCoordiantes = new Coordinates(40, 100);
	
	private Font headerFont = new Font("Monospaced", Font.BOLD, 26);
	private Font arrayFont = new Font("Monospaced", Font.PLAIN, 16);
	private Font statisticFont = new Font("Monospaced", Font.PLAIN, 18);
	private Font textFont = new Font("Monospaced", Font.PLAIN, 18);
	
	private Rect statisticRect;
	private Rect infoRect;
	private int arrayIndex;
	private int compareCounter;
	private int swapCounter;
	private int recursionDepth;
	private int tempIndexPivot;
	private int[] resultArray;
	private FontRenderContext frc;
	
	private String[] description = {
			"",
			"Quicksort ist ein rekursiver Algorithmus nach dem „Teile und Herrsche“ Prinzip. Zuerst",
			"trennt der Algorithmus die zu sortierende Liste in 2 Teillisten. Hierfür wird aus den Elementen",
			"der Liste ein Pivot-Element ausgewählt. Nun kommen alle Elemente, welche kleiner als das",
			"Pivot-Element sind, in die linke Teilliste und alle größeren Elemente werden in die rechte",
			"Liste verschoben. Die Elemente, die genau so groß sind wie das Pivot, können in eine beliebige",
			"Liste verschoben werden.",
			"",
			"Die Sortierung erfolgt durch den Aufruf des Algorithmus auf jede Teilliste. Somit ist sichergestellt,",
			"dass sich das jeweilige Pivot-Element nach jedem Durchlauf an der richtigen Stellen befindet und links",
			"nur kleinere oder gleiche bzw. rechts nur größere oder gleiche Elemente befinden. Sobald eine Teilliste",
			"nur noch ein Element besitzt, ist diese sortiert und muss nicht mehr geteilt werden.",
			"",
			"Im Folgenden wird der Algorithmus Quicksort mit jeweils 5 Schritten des Tauschbaumes dargestellt.",
			"Am Ende der Animation findet sich dann der vollständige Tauschbaum."

	};

	private String[] code = { "public void quickSort(int[] array, int links, int rechts) {", // 0
			"#if (links < rechts){", // 1
			"#int i = links, j = rechts;", // 2
			"int pivot = (links + rechts) / 2;", // 3
			"while (i < j) {", // 4
			"#while (array[i] < array[pivot])", // 5
			"#i++;", // 6
			"~while (array[j] > array[pivot])", // 7
			"#j--;", // 8
			"~if (i < j) {", // 9
			"#if (pivot == i)", // 10
			"#pivot = j;", // 11
			"~else if (pivot == j)", // 12
			"#pivot = i;", // 13
			"~int temp = array[i];", // 14
			"array[i] = array[j];", // 15
			"array[j] = temp;", // 16
			"if (i < pivot)", // 17
			"#i++;", // 18
			"~if (j > pivot)", // 19
			"#j--;", // 20
			"~~}", // 21
			"~}", // 22
			"if (links < j)", // 23
			"#quickSort(array, links, pivot-1);", // 24
			"~if (rechts > i)", // 25
			"#quickSort(array, pivot+1, rechts);", // 26
			"~~}", // 27
			"~}" }; // 28

    public void init(){
        lang = new AnimalScript("Quicksort mit Tauschbaum[DE]", "Christoph Schüßler, Marcel Frank", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        arrayMarkerJProperties = (ArrayMarkerProperties)props.getPropertiesByName("arrayMarkerJProperties");
        sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProperties");
        textProperties = (TextProperties)props.getPropertiesByName("textProperties");
        arrowProperties = (PolylineProperties)props.getPropertiesByName("arrowProperties");
        arrayMarkerPivotProperties = (ArrayMarkerProperties)props.getPropertiesByName("arrayMarkerPivotProperties");
        arrayProperties = (ArrayProperties)props.getPropertiesByName("arrayProperties");
        workArrayProperties = (ArrayProperties)props.getPropertiesByName("workArrayProperties");
        array = (int[])primitives.get("array");
        arrayMarkerIProperties = (ArrayMarkerProperties)props.getPropertiesByName("arrayMarkerIProperties");
        infoBoolean = (Boolean)primitives.get("Zusatzinformationen");
        arrayProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(((Font) arrayProperties.get(AnimationPropertiesKeys.FONT_PROPERTY)).getName(), arrayFont.getStyle(), arrayFont.getSize()));
        workArrayProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(((Font) arrayProperties.get(AnimationPropertiesKeys.FONT_PROPERTY)).getName(), arrayFont.getStyle(), arrayFont.getSize()));
        textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(((Font) textProperties.get(AnimationPropertiesKeys.FONT_PROPERTY)).getName(), textFont.getStyle(), textFont.getSize()));
        headerProperties = new TextProperties();
		headerProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, headerFont);
		arrowProperties = new PolylineProperties();
		arrowProperties.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

		lang.setStepMode(true);
       
		arrayIndex = 0;
    	compareCounter = 0;
    	swapCounter = 0;
    	recursionDepth = 0;
    	tempIndexPivot = 0;
    	masterList = new ArrayList<ArrayList<IntArray>>();
    	pivotArray = new ArrayList<Integer>();
    	arrowList = new ArrayList<Polyline>();
    	arrowcount = new ArrayList<Integer>();
    	markerList = new ArrayList<ArrayMarker>();
    	frc = new FontRenderContext(new AffineTransform(), true, false);
        
        initQuicksort();
        return lang.toString();
    }

    public String getName() {
        return "Quicksort mit Tauschbaum[DE]";
    }

    public String getAlgorithmName() {
        return "Quicksort";
    }

    public String getAnimationAuthor() {
        return "Christoph Schüßler, Marcel Frank";
    }

    public String getDescription(){
        return "Quicksort ist ein rekursiver Algorithmus nach dem „Teile und Herrsche“ Prinzip. Zuerst"
 +"\n"
 +"trennt der Algorithmus die zu sortierende Liste in 2 Teillisten. Hierf&uuml;r wird aus den Elementen"
 +"\n"
 +"der Liste ein Pivot-Element ausgew&auml;hlt. Nun kommen alle Elemente, welche kleiner als das"
 +"\n"
 +"Pivot-Element sind, in die linke Teilliste und alle gr&ouml;&szlig;eren Elemente werden in die rechte"
 +"\n"
 +"Liste verschoben. Die Elemente, die genau so gro&szlig; sind wie das Pivot, k&ouml;nnen in eine beliebige"
 +"\n"
 +"Liste verschoben werden."
 +"\n"
 +"\n"
 +"Die Sortierung erfolgt durch den Aufruf des Algorithmus auf jede Teilliste. Somit ist sichergestellt,"
 +"\n"
 +"dass sich das jeweilige Pivot-Element nach jedem Durchlauf an der richtigen Stellen befindet und links"
 +"\n"
 +"nur kleinere oder gleiche bzw. rechts nur gr&ouml;&szlig;ere oder gleiche Elemente befinden. Sobald eine Teilliste"
 +"\n"
 +"nur noch ein Element besitzt, ist diese sortiert und muss nicht mehr geteilt werden."
 +"\n"
 +"\n"
 +"Im Folgenden wird der Algorithmus Quicksort mit jeweils 5 Schritten des Tauschbaumes dargestellt."
 +"\n"
 +"Am Ende der Animation findet sich dann der vollst&auml;ndige Tauschbaum.";
    }

    public String getCodeExample(){
        return "public void quickSort(int[] array, int links, int rechts) {"
 +"\n"
 +"     if (links < rechts){"
 +"\n"
 +"          int i = links, j = rechts;"
 +"\n"
 +"          int pivot = (links + rechts) / 2;"
 +"\n"
 +"          while (i < j) {"
 +"\n"
 +"               while (array[i] < array[pivot])"
 +"\n"
 +"                    i++;"
 +"\n"
 +"               while (array[j] > array[pivot])"
 +"\n"
 +"                    j--;"
 +"\n"
 +"               if (i < j) {"
 +"\n"
 +"                    if (pivot == i)"
 +"\n"
 +"                         pivot = j;"
 +"\n"
 +"                    else if (pivot == j)"
 +"\n"
 +"                         pivot = i;"
 +"\n"
 +"                    int temp = array[i];"
 +"\n"
 +"                    array[i] = array[j];"
 +"\n"
 +"                    array[j] = temp;"
 +"\n"
 +"                    if (i < pivot)"
 +"\n"
 +"                         i++;"
 +"\n"
 +"                    if (j > pivot)"
 +"\n"
 +"                         j--;"
 +"\n"
 +"               }"
 +"\n"
 +"          }"
 +"\n"
 +"          if (links < j)"
 +"\n"
 +"               quickSort(array, links, pivot-1);"
 +"\n"
 +"          if (rechts > i)"
 +"\n"
 +"               quickSort(array, pivot+1, rechts);"
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
	 * initialize the sorting
	 */
    public void initQuicksort() {
		sourceCode = lang.newSourceCode(sourceCodeCoordinates, "SourceCode", null, sourceCodeProperties);
		// header
		lang.newText(headerCoordinates, "Quicksort mit Tauschdiagramm", "title", null, headerProperties);
		Coordinates[] line = { new Coordinates(headerCoordinates.getX(), headerCoordinates.getY() + headerFont.getSize()), new Coordinates(headerCoordinates.getX() + 465, headerCoordinates.getY() + headerFont.getSize()) };
		lang.newPolyline(line, "line", null);
		descriptionText = getText(description, descriptionCoordiantes, textProperties, textFont.getSize());
		lang.nextStep("Beschreibung");
		//hide description
		for (int i = 0; i < descriptionText.length; i++) {
			descriptionText[i].hide();
		}
		IntArray intarray = lang.newIntArray(arrayCoordinates, array, "array", null, arrayProperties);
		ArrayList<IntArray> temp = new ArrayList<IntArray>();
		temp.add(intarray);
		masterList.add(temp);
		addCode(sourceCode, code);
		updateStatistic(0, 0, 0, 0, 0);
		int longest = 0;
		for (int i = 0; i < statisticText.length; i++) {
			int temp1 = (int) (statisticFont.getStringBounds(statisticText[i].getText(), frc).getWidth() + WHITESPACE_X + 30);
			if (temp1 > longest) {
				longest = temp1;
			}
		}
		statisticRect = lang.newRect(statisticCoordiantes, new Coordinates(statisticCoordiantes.getX() + longest,statisticCoordiantes.getY() + 160), "Rectangle", null);
		
		quicksort("Initial");
		for (int i = 5; i < masterList.size(); i++) {
			for (IntArray array : masterList.get(i)) {
				array.show();
			}
		}
		int count = 0;
		if (arrowcount.size() > 3) {
			for (int i = 0; i < 4; i++) {
				count += arrowcount.get(i);
			}
		}
		for (int i = count; i < arrowList.size(); i++) {
			arrowList.get(i).show();
		}
		for (ArrayMarker marker : markerList) {
			marker.hide();
		}
		statisticRect.hide();
		for (int i = 0; i < statisticText.length; i++) {
			statisticText[i].hide();
		}
		lang.nextStep("kompletter Tauschbaum");
		// Ende
		for (ArrayList<IntArray> list : masterList) {
			for (IntArray array : list) {
				array.hide();
			}
		}
		for (Polyline polyline : arrowList) {
			polyline.hide();
		}
		resultArray = new int[array.length];
		for (int i = 0; i < masterList.get(0).size(); i++){
			resultArray[i] = masterList.get(0).get(i).getData(0);
		}
		sourceCode.hide();
		getText(endString(compareCounter, swapCounter), endCoordiantes, textProperties, textFont.getSize());
		lang.newIntArray(new Coordinates(endCoordiantes.getX(), endCoordiantes.getY()+textFont.getSize()+10), array, "array", null, arrayProperties);
		lang.newIntArray(new Coordinates(endCoordiantes.getX(), endCoordiantes.getY()+textFont.getSize()*7+10), resultArray, "resultArray", null, arrayProperties);
		lang.nextStep("Zusammenfassung");
    }

    /**
     * sorts the given array
     * @param indicates which recursive call its currently in
     */
	public void quicksort(String label) {
		int links = 0;
		int rechts = 0;
		sourceCode.highlight(0);
		sourceCode.highlight(28);
		// zu sortierendes array suchen
		IntArray array = null;
		for (int i = 0; i < masterList.get(0).size(); i++) {
			if (!pivotArray.contains(i)) {
				array = masterList.get(0).get(i);
				arrayIndex = i;
				break;
			}
		}
		// abbruch
		if (array == null) {
			return;
		}
		array.hide();
		array = colorArray(array, array.getUpperLeft());
		masterList.get(0).remove(arrayIndex);
		masterList.get(0).add(arrayIndex, array);
		int statisticLinks = 0+ arrayIndex;
		int statisticRechts = array.getLength()-1 + arrayIndex;
		updateStatistic(links, rechts, 0, statisticLinks, statisticRechts);
		lang.nextStep("quickSort (Rekursionstiefe: " + recursionDepth + " " + label+ ")");
		sourceCode.toggleHighlight(0, 1);
		sourceCode.toggleHighlight(28, 27);
		info(1, statisticLinks, statisticRechts, 0, 0);
		compareCounter++;
		updateStatistic(links, rechts, 0, statisticLinks, statisticRechts);
		lang.nextStep();
		if (array.getLength() == 1) {
			pivotArray.add(arrayIndex);
			sourceCode.unhighlight(1);
			sourceCode.unhighlight(27);
			array.hide();
			array = copyArray(array, array.getUpperLeft());
			masterList.get(0).remove(arrayIndex);
			masterList.get(0).add(arrayIndex, array);
			array.highlightCell(0, null, null);
			lang.nextStep();
			return;
		}
		// bounds
		sourceCode.toggleHighlight(1, 2);
		sourceCode.unhighlight(27);
		links = 0;
		rechts = array.getLength() - 1;
		markerList.add(0, lang.newArrayMarker(array, links, "i", null, arrayMarkerIProperties));
		markerList.add(0, lang.newArrayMarker(array, rechts, "j", null, arrayMarkerJProperties));
		updateStatistic(links, rechts, 0, statisticLinks, statisticRechts);
		lang.nextStep();
		sourceCode.toggleHighlight(2, 3);
		int indexPivot = (links + rechts) / 2;
		info(3, links, rechts, 0, 0);
		updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
		markerList.add(0, lang.newArrayMarker(array, indexPivot, "pivot", null, arrayMarkerPivotProperties));
		array.highlightCell(indexPivot, null, null);
		lang.nextStep();
		// begin sorting
		while (links < rechts) {
			compareCounter++;
			updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
			sourceCode.toggleHighlight(3, 4);
			info(4, links, rechts, 0, 0);
			sourceCode.highlight(22);
			lang.nextStep();
			sourceCode.unhighlight(4);
			sourceCode.unhighlight(22);
			while (array.getData(links) < array.getData(indexPivot)) {
				compareCounter++;
				updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
				sourceCode.highlight(5);
				info(5, links, indexPivot, array.getData(links), array.getData(indexPivot));
				lang.nextStep();
				sourceCode.toggleHighlight(5, 6);
				links++;
				updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
				markerList.get(2).increment(null, null);
				lang.nextStep();
				sourceCode.unhighlight(6);
			}
			sourceCode.highlight(5);
			info(5, links, indexPivot, array.getData(links), array.getData(indexPivot));
			compareCounter++;
			updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
			lang.nextStep();
			sourceCode.unhighlight(5);
			while (array.getData(rechts) > array.getData(indexPivot)) {
				compareCounter++;
				updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
				sourceCode.highlight(7);
				info(7, rechts, indexPivot, array.getData(rechts), array.getData(indexPivot));
				lang.nextStep();
				sourceCode.toggleHighlight(7, 8);
				rechts--;
				updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
				markerList.get(1).decrement(null, null);
				lang.nextStep();
				sourceCode.unhighlight(8);
			}
			sourceCode.highlight(7);
			info(7, rechts, indexPivot, array.getData(rechts), array.getData(indexPivot));
			compareCounter++;
			updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
			lang.nextStep();
			sourceCode.unhighlight(7);
			sourceCode.highlight(9);
			sourceCode.highlight(21);
			info(9, links, rechts, 0, 0);
			compareCounter++;
			updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
			lang.nextStep();
			if (links < rechts) {
				sourceCode.unhighlight(9);
				sourceCode.unhighlight(21);
				ArrayList<IntArray> temp = new ArrayList<IntArray>();
				int[] copy;
				for (int j = 0; j < masterList.get(0).size();j++) {
					copy = new int[masterList.get(0).get(j).getLength()];
					for (int i = 0; i < copy.length; i++) {
						copy[i] = masterList.get(0).get(j).getData(i);
					}
					if (j!=arrayIndex){
					temp.add(lang.newIntArray(masterList.get(0).get(j).getUpperLeft(), copy, "array", null, arrayProperties));
					}else{
					temp.add(lang.newIntArray(masterList.get(0).get(j).getUpperLeft(), copy, "array", null, workArrayProperties));	
					}
				}
				moveDown();
				masterList.add(0, temp);
				pivotHighlight();
				masterList.get(0).get(arrayIndex).highlightCell(indexPivot, null, null);
				markerList.get(2).hide();
				markerList.get(1).hide();
				markerList.get(0).hide();
				markerList.add(0, lang.newArrayMarker(masterList.get(0).get(arrayIndex), markerList.get(2).getPosition(), "i", null, arrayMarkerIProperties));
				markerList.add(0, lang.newArrayMarker(masterList.get(0).get(arrayIndex), markerList.get(2).getPosition(), "j", null, arrayMarkerJProperties));	
				markerList.add(0, lang.newArrayMarker(masterList.get(0).get(arrayIndex), markerList.get(2).getPosition(), "pivot", null, arrayMarkerPivotProperties));
				// swap pivot
				compareCounter++;
				updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
				sourceCode.highlight(10);
				info(10, indexPivot, links, 0, 0);
				lang.nextStep();
				if (links == indexPivot) {
					sourceCode.toggleHighlight(10, 11);
					indexPivot = rechts;
					markerList.get(0).move(indexPivot, null, null);
					updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
					lang.nextStep();
					sourceCode.unhighlight(11);
				} else {
					sourceCode.toggleHighlight(10, 12);
					info(12, indexPivot, rechts, 0, 0);
					compareCounter++;
					updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
					lang.nextStep();
					if (rechts == indexPivot) {
						sourceCode.toggleHighlight(12, 13);
						indexPivot = links;
						markerList.get(0).move(indexPivot, null, null);
						updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
						lang.nextStep();
						sourceCode.unhighlight(13);
					}
					sourceCode.unhighlight(12);
				}
				sourceCode.unhighlight(10);
				sourceCode.highlight(14);
				sourceCode.highlight(15);
				sourceCode.highlight(16);
				// swap array
				swapCounter++;
				updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
				array = masterList.get(0).get(arrayIndex);
				array.swap(links, rechts, null, null);
				swapArrow(arrayIndex, links, rechts);
				lang.nextStep();
				sourceCode.unhighlight(14);
				sourceCode.unhighlight(15);
				sourceCode.unhighlight(16);
				sourceCode.highlight(17);
				info(17, links, indexPivot, 0, 0);
				compareCounter++;
				updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
				lang.nextStep();
				sourceCode.unhighlight(17);
				if (links != indexPivot) {
					sourceCode.highlight(18);
					links++;
					updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
					markerList.get(2).increment(null, null);
					lang.nextStep();
					sourceCode.unhighlight(18);
				}
				sourceCode.highlight(19);
				info(19, rechts, indexPivot, 0, 0);
				compareCounter++;
				updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
				lang.nextStep();
				sourceCode.unhighlight(19);
				if (rechts != indexPivot) {
					sourceCode.highlight(20);
					rechts--;
					updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
					markerList.get(1).decrement(null, null);
					lang.nextStep();
					sourceCode.unhighlight(20);
				}
			}
		}
		sourceCode.unhighlight(9);
		sourceCode.unhighlight(21);
		sourceCode.highlight(4);
		sourceCode.highlight(22);
		info(4, links, rechts, 0, 0);
		lang.nextStep();
		sourceCode.unhighlight(4);
		sourceCode.unhighlight(22);
		// create new list
		tempIndexPivot = 0;
		moveDown();
		ArrayList<IntArray> slaveList = new ArrayList<IntArray>();
		// add arrays to arrayIndex-1
		int offset = 0;
		for (int i = 0; i < arrayIndex; i++) {
			offset += getArrayLength(masterList.get(0).get(i)) + ARRAY_GAP;
			slaveList.add(copyArray(masterList.get(0).get(i), masterList.get(0).get(i).getUpperLeft()));
		}
		// split array
		int[] temp;
		// first part
		if (indexPivot > 0) {
			temp = new int[indexPivot];
			for (int i = 0; i < temp.length; i++) {
				temp[i] = array.getData(i);
				tempIndexPivot++;
			}
			slaveList.add(lang.newIntArray(new Coordinates(arrayCoordinates.getX() + offset, arrayCoordinates.getY()), temp, "array", null, arrayProperties));
			offset += ARRAY_GAP + getArrayLength(slaveList.get(slaveList.size()-1));
		}
		// pivot part
		pivotArray.add(tempIndexPivot);
		temp = new int[1];
		temp[0] = array.getData(indexPivot);
		IntArray pivot = lang.newIntArray(new Coordinates(arrayCoordinates.getX() + offset, arrayCoordinates.getY()), temp, "array", null, arrayProperties);
		pivot.highlightCell(0, null, null);
		slaveList.add(pivot);
		offset += getArrayLength(slaveList.get(slaveList.size()-1)) + ARRAY_GAP;
		// second part
		if (indexPivot < array.getLength() - 1) {
			temp = new int[array.getLength() - 1 - indexPivot];
			for (int i = 0; i < temp.length; i++) {
				temp[i] = array.getData(indexPivot + 1 + i);
			}
			slaveList.add(lang.newIntArray(new Coordinates(arrayCoordinates.getX() + offset, arrayCoordinates.getY()), temp, "array", null, arrayProperties));
			offset += ARRAY_GAP + getArrayLength(slaveList.get(slaveList.size()-1));
		}
		// add array from arrayIndex+1
		for (int i = arrayIndex + 1; i < masterList.get(0).size(); i++) {
			temp = new int[masterList.get(0).get(i).getLength()];
			for (int j = 0; j < temp.length; j++) {
				temp[j] = masterList.get(0).get(i).getData(j);
			}
			slaveList.add(copyArray(masterList.get(0).get(i), new Coordinates(arrayCoordinates.getX() + offset, arrayCoordinates.getY())));
			offset += ARRAY_GAP + getArrayLength(slaveList.get(slaveList.size()-1));;
		}
		masterList.add(0, slaveList);
		pivotHighlight();
		markerList.get(0).hide();
		markerList.get(1).hide();
		markerList.get(2).hide();
		splitArrow(arrayIndex, indexPivot);
		// rekursion
		sourceCode.highlight(23);
		info(23, statisticLinks, rechts, 0, 0);
		compareCounter++;
		updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
		lang.nextStep();
		sourceCode.unhighlight(23);
		if (0 < rechts) {
			sourceCode.highlight(24);
			info(24, statisticLinks, indexPivot, 0, 0);
			recursionDepth++;
			updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
			lang.nextStep();
			sourceCode.unhighlight(24);
			quicksort("Links");
			recursionDepth--;
			updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
		}
		sourceCode.highlight(25);
		info(25, statisticRechts, links, 0, 0);
		compareCounter++;
		updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
		lang.nextStep();
		sourceCode.unhighlight(25);
		if (array.getLength() - 1 > links) {
			sourceCode.highlight(26);
			info(26, indexPivot, statisticRechts, 0, 0);
			recursionDepth++;
			updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
			lang.nextStep();
			sourceCode.unhighlight(26);
			quicksort("Rechts");
			recursionDepth--;
			updateStatistic(links, rechts, indexPivot, statisticLinks, statisticRechts);
		}
		sourceCode.unhighlight(0);
		sourceCode.unhighlight(28);
		lang.nextStep();
	}

	/**
	 * moves the diagram down
	 */
	private void moveDown() {
		if (masterList.size() > VISIBLE_ELEMENTS-1) {
			for (IntArray i : masterList.get(VISIBLE_ELEMENTS-1)) {
				i.hide();
			}	
			int temp = 0;
			for (int i = 0; i < VISIBLE_ELEMENTS-2; i++) {
				temp += arrowcount.get(i);
			}
			for (int i = 0; i < arrowcount.get(VISIBLE_ELEMENTS-2); i++) {
				arrowList.get(temp+i).hide();
			}
		}
		

		for (ArrayList<IntArray> list : masterList) {
			for (IntArray array : list) {
				array.moveBy(null, 0, ARRAY_MOVE_BY, null, new MsTiming(125));
			}
		}
		for (Polyline arrow : arrowList) {
			arrow.moveBy(null, 0, ARRAY_MOVE_BY, null, new MsTiming(125));
		}
	}

	/**
	 * Copies the array to the given location
	 * @param array array to copy
	 * @param node position of new array
	 * @return copied array
	 */
	public IntArray copyArray(IntArray array, Node node) {
		int[] copy = new int[array.getLength()];
		for (int i = 0; i < array.getLength(); i++) {
			copy[i] = array.getData(i);
			tempIndexPivot++;
		}
		return lang.newIntArray(node, copy, "array", null, arrayProperties);
	}
	
	/**
	 * Copys and colors the array
	 * @param array array to color
	 * @param node position of new array
	 * @return colored array
	 */
	public IntArray colorArray(IntArray array, Node node) {
		int[] copy = new int[array.getLength()];
		for (int i = 0; i < array.getLength(); i++) {
			copy[i] = array.getData(i);
		}
		return lang.newIntArray(node, copy, "array", null,
				workArrayProperties);
	}

	/**
	 * Adds text to ScourceCode
	 * @param sourceCode Where to add the code
	 * @param code text to add
	 */
	public void addCode(SourceCode sourceCode, String[] code) {
		int indentation = 0;
		for (int i = 0; i < code.length; i++) {
			if (code[i].startsWith("#")) {
				sourceCode.addCodeLine(code[i].replace("#", ""), null, ++indentation, null);
			} else if (code[i].startsWith("~~")) {
				sourceCode.addCodeLine(code[i].replace("~~", ""), null, indentation -= 2, null);
			} else if (code[i].startsWith("~")) {
				sourceCode.addCodeLine(code[i].replace("~", ""), null, --indentation, null);
			} else {
				sourceCode.addCodeLine(code[i], null, indentation, null);
			}
		}
	}

	/**
	 * Draws arrows to the parts of the splitted array
	 * @param listIndex index of the splitted array
	 * @param pivotIndex index of the pivot element
	 */
	private void splitArrow(int listIndex, int pivotIndex) {
		int offset = arrayCoordinates.getX();
		int yOben = arrayCoordinates.getY() + (int) ((Font) arrayProperties.get(AnimationPropertiesKeys.FONT_PROPERTY)).getStringBounds(String.valueOf(masterList.get(0).get(0).getData(0)), frc).getHeight() + WHITESPACE_Y;
		int yUnten = arrayCoordinates.getY() + ARRAY_MOVE_BY;
		int firstExists = 0;
		for (int i = 0; i < listIndex; i++) {
			offset += getArrayLength(masterList.get(0).get(i)) + ARRAY_GAP;
		}
		// links
		int untenlinks = 0;
		int obenlinks = 0;
		int arrownumber = 0;
		if (pivotIndex > 0) {
			firstExists++;
			for (int i = 0; i < pivotIndex; i++) {
				untenlinks += ((Font) arrayProperties.get(AnimationPropertiesKeys.FONT_PROPERTY)).getStringBounds(String.valueOf(masterList.get(1).get(listIndex).getData(i)), frc).getWidth() + WHITESPACE_X;
			}
			obenlinks += getArrayLength(masterList.get(0).get(listIndex));	
			arrowList.add(0, lang.newPolyline(new Coordinates[]{new Coordinates(offset + untenlinks / 2, yUnten), new Coordinates(offset + obenlinks / 2, yOben)}, "arrow", null, arrowProperties));
			arrownumber++;
		}
		// pivot
		int untenpivot = 0;
		int obenpivot = 0;
		untenpivot += ((Font) arrayProperties.get(AnimationPropertiesKeys.FONT_PROPERTY)).getStringBounds(String.valueOf(masterList.get(1).get(listIndex).getData(pivotIndex)), frc).getWidth() + WHITESPACE_X;
		obenpivot += getArrayLength(masterList.get(0).get(listIndex + firstExists));
		arrowList.add(0, lang.newPolyline(new Coordinates[]{new Coordinates(offset + untenlinks + untenpivot / 2, yUnten), new Coordinates(offset + obenlinks + ARRAY_GAP * firstExists + obenpivot / 2, yOben)}, "arrow", null, arrowProperties));
		arrownumber++;
		// rechts
		int untenrechts = 0;
		int obenrechts = 0;
		if (pivotIndex < masterList.get(1).get(listIndex).getLength() - 1) {
			obenrechts += getArrayLength(masterList.get(0).get(listIndex + 1 + firstExists));
			for (int i = pivotIndex + 1; i < masterList.get(1).get(listIndex).getLength(); i++) {
				untenrechts += ((Font) arrayProperties.get(AnimationPropertiesKeys.FONT_PROPERTY)).getStringBounds(String.valueOf(masterList.get(1).get(listIndex).getData(i)), frc).getWidth() + WHITESPACE_X;
			}
			arrowList.add(0, lang.newPolyline(new Coordinates[]{new Coordinates(offset + untenlinks + untenpivot + untenrechts / 2, yUnten), new Coordinates(offset + obenlinks + ARRAY_GAP * firstExists + obenpivot + ARRAY_GAP + obenrechts / 2, yOben)}, "arrow", null, arrowProperties));
			arrownumber++;
		}
		arrowcount.add(0, arrownumber);
	}

	/**
	 * Draws arrows to show elements have swapped
	 * @param listIndex index of the array with swapped elements
	 * @param arrayIndex1 element1 
	 * @param arrayIndex2 element2
	 */
	public void swapArrow(int listIndex, int arrayIndex1, int arrayIndex2) {
		int yOben = arrayCoordinates.getY() + (int) ((Font) arrayProperties.get(AnimationPropertiesKeys.FONT_PROPERTY)).getStringBounds(String.valueOf(masterList.get(0).get(0).getData(0)), frc).getHeight() + WHITESPACE_Y;
		int yUnten = arrayCoordinates.getY() + ARRAY_MOVE_BY;
		
		// obenlinks
		int obenlinks = arrayCoordinates.getX() + getOffset(0, listIndex, arrayIndex1);
		
		// obenrechts
		int obenrechts = arrayCoordinates.getX() + getOffset(0, listIndex, arrayIndex2);
		
		// untenlinks
		int untenlinks = arrayCoordinates.getX() + getOffset(1, listIndex, arrayIndex1);
		
		// untenrechts
		int untenrechts = arrayCoordinates.getX() + getOffset(1, listIndex, arrayIndex2);
		
		// create line		
		arrowList.add(0, lang.newPolyline(new Coordinates[]{new Coordinates(untenlinks, yUnten), new Coordinates(obenrechts, yOben)}, "arrow", null, arrowProperties));
		arrowList.add(0, lang.newPolyline(new Coordinates[]{new Coordinates(untenrechts, yUnten), new Coordinates(obenlinks, yOben)}, "arrow", null, arrowProperties));
		arrowcount.add(0, 2);
	}
	
	/**
	 * Calculates offset to index in given array 
	 * @param outerListIindex index in masterlist
	 * @param innerListIndex index in slavelist
	 * @param arrayIndex index in array
	 * @return offset in pixel
	 */
	private int getOffset(int outerListIindex, int innerListIndex, int arrayIndex) {
		int offset = 0;
		for (int i = 0; i < innerListIndex; i++) {			
			offset += getArrayLength(masterList.get(outerListIindex).get(i)) + ARRAY_GAP;
		}
		for (int i = 0; i < arrayIndex; i++) {
			offset += (arrayFont.getStringBounds(String.valueOf(masterList.get(outerListIindex).get(innerListIndex).getData(i)), frc).getWidth() + WHITESPACE_X);
		}
		offset += (arrayFont.getStringBounds(String.valueOf(masterList.get(outerListIindex).get(innerListIndex).getData(arrayIndex)), frc).getWidth() + WHITESPACE_X)/2;
		return offset;
	}
	
	/**
	 * Calculates length of array in pixel
	 * @param array array to get length
	 * @return length in pixel
	 */
	private int getArrayLength(IntArray array) {
		int offset = 0;
		for (int i = 0; i < array.getLength(); i++) {
			offset += ((Font) arrayProperties.get(AnimationPropertiesKeys.FONT_PROPERTY)).getStringBounds(String.valueOf(array.getData(i)), frc).getWidth() + WHITESPACE_X;
		}
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
	public Text[] getText(String[] lines, Coordinates coords, TextProperties props, int fontSize) {
		Text[] text = new Text[lines.length];
		for (int i = 0; i < lines.length; i++) {
			text[i] = lang.newText(new Coordinates(coords.getX(), coords.getY() + fontSize * i), lines[i], "text", null, props);
		}
		return text;
	}
	/**
	 * updates the statistics
	 * @param links position of i pointer
	 * @param rechts position of j pointer
	 * @param indexPivot index of pivot element
	 * @param statisticLinks left bound of array
	 * @param statisticRechts right bound of array
	 */
	public void updateStatistic(int links, int rechts, int indexPivot, int statisticLinks, int statisticRechts) {
		String[] statistic = {	"", 	
								"Vergleiche: " + compareCounter,
								"Vertauschungen: " + swapCounter,
								"Rekursionstiefe: " + recursionDepth,
								"i: " + links,
								"j: " + rechts,
								"pivot: " + indexPivot,
								"links: " + statisticLinks, 
								"rechts: " + statisticRechts, 
								 };
		if (statisticText != null) {
			for (int i = 0; i < statisticText.length; i++) {
				statisticText[i].hide();
			}
		}
		statisticText = getText(statistic, new Coordinates(statisticCoordiantes.getX() + WHITESPACE_X, statisticCoordiantes.getY() - 17), textProperties, statisticFont.getSize());

	}

	/**
	 * highlights the pivot element
	 */
	public void pivotHighlight() {
		int pivotHighlighter = 0;
		for (int i = 0; i < masterList.get(0).size(); i++) {
			for (int j = 0; j < masterList.get(0).get(i).getLength(); j++) {
				if (pivotArray.contains(pivotHighlighter)) {
					masterList.get(0).get(i).highlightCell(j, null, null);
				}
				pivotHighlighter++;
			}
		}
	}
	
	/**
	 * Displays the evaluation of the current codeline
	 * @param lineNumber
	 * @param a values to display
	 * @param b values to display
	 * @param c values to display
	 * @param d values to display
	 */
	public void info(int lineNumber, int a, int b, int c, int d){
		if (infoBoolean){
			LinkedList<String> infoText = new LinkedList<String>();
			Text tempText[];
			String array = "[";
			for(int i = 0; i < masterList.get(0).size(); i++){
				for (int j = 0; j < masterList.get(0).get(i).getLength(); j++){
					array = array.concat(Integer.toString(masterList.get(0).get(i).getData(j)));
					array = array.concat("|");
				}
			}
			array = array.substring(0, array.length()-1);
			array = array.concat("]");
			switch (lineNumber) {
			case 1: 
				infoText.add("if (links < rechts)");
				infoText.add("if ("+ a + " < " + b +")");
				infoText.add("if ("+Boolean.toString(a < b)+")");
				break;
			case 3:
				infoText.add("int pivot = (links + rechts) / 2");
				infoText.add("int pivot = (" + a + " + " + b +") / 2");
				infoText.add("int pivot = " + ((a+b)/2));
				break;
			case 4:
				infoText.add("while (i < j)");
				infoText.add("while ("+a+" < "+b+")");
				infoText.add("while ("+Boolean.toString(a < b)+")");
				break;
			case 5:
				infoText.add("while (array[i] < array[pivot])");
				infoText.add("while (array["+a+"] < array["+b+"])");
				infoText.add("while ("+c+" < "+d+")");
				infoText.add("while ("+Boolean.toString(c < d)+")");
				break;
			case 7:
				infoText.add("while (array[j] > array[pivot])");
				infoText.add("while (array["+a+"] > array["+b+"])");
				infoText.add("while ("+c+" > "+d+")");
				infoText.add("while ("+Boolean.toString(c > d)+")");
				break;
			case 9:
				infoText.add("if (i < j)");
				infoText.add("if ("+ a + " < " + b +")");
				infoText.add("if ("+Boolean.toString(a < b)+")");
				break;
			case 10:
				infoText.add("if (pivot == i)");
				infoText.add("if ("+ a + " == " + b +")");
				infoText.add("if ("+Boolean.toString(a == b)+")");
				break;
			case 12:
				infoText.add("else if (pivot == j)");
				infoText.add("else if ("+ a + " == " + b +")");
				infoText.add("else if ("+Boolean.toString(a == b)+")");
				break;
			case 17:
				infoText.add("if (i < pivot)");
				infoText.add("if ("+ a + " < " + b +")");
				infoText.add("if ("+Boolean.toString(a < b)+")");
				break;
			case 19:
				infoText.add("if (j > pivot)");
				infoText.add("if ("+ a + " > " + b +")");
				infoText.add("if ("+Boolean.toString(a > b)+")");
				break;
			case 23:
				infoText.add("if (links < j)");
				infoText.add("if ("+ a + " < " + b +")");
				infoText.add("if ("+Boolean.toString(a < b)+")");
				break;
			case 24:
				infoText.add("quickSort(array, links, pivot-1)");
				infoText.add("quickSort("+array+", " +a+", ("+b+"-1))");
				infoText.add("quickSort("+array+", " +a+", "+(b-1)+")");
				break;
			case 25:
				infoText.add("if (rechts > i)");
				infoText.add("if ("+ a + " > " + b +")");
				infoText.add("if ("+Boolean.toString(a > b)+")");
				break;
			case 26:
				infoText.add("quickSort(array, pivot+1, rechts)");
				infoText.add("quickSort("+array+", (" + a +"+1), "+b+")");
				infoText.add("quickSort("+array+", " + (a+1)+", "+b+")");
				break;
			default:
				return;
			}
			int longest = 0;
			for (int i = 0; i < infoText.size(); i++){
				int temp = (int) textFont.getStringBounds(infoText.get(i), frc).getWidth();
				if (temp > longest) {
					longest = temp;
				}
			}
			infoRect = lang.newRect(infoCoordiantes, new Coordinates(infoCoordiantes.getX() + 2 * WHITESPACE_X + longest,infoCoordiantes.getY() + 20 + WHITESPACE_Y), "InfoRectangle", null);
			
			for (int i = 0; i < infoText.size(); i++){
				String[] temp = {"", infoText.get(i)};
				tempText = getText(temp, new Coordinates(infoCoordiantes.getX() + WHITESPACE_X, infoCoordiantes.getY() - 17), textProperties, statisticFont.getSize());
				lang.nextStep();
				tempText[0].hide();
				tempText[1].hide();
			}
			infoRect.hide();
		}
	}
	
	/**
	 * Shows the final evaluation
	 * @param compareCounter compare the algorithm needed
	 * @param swapCounter swaps the algorithm did
	 * @return text to display
	 */
	public String[] endString (int compareCounter, int swapCounter){
		String[] end = 	{"Der Quicksort Algorithmus hat die ursprüngliche Liste",
						"",
						"",
						"mit " + compareCounter + " Vergleichen sortiert.",
						"Dabei wurden " + swapCounter + " Vertauschungen durchgeführt.",
						"",
						"Die resultierende Liste lautet:"};
		return end;
	}
}
