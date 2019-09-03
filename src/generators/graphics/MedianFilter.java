
/*
 * MedianFilter.java
 * Igor Braun, Vladimir Bolgov, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import interactionsupport.models.FillInBlanksQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Circle;
import algoanim.primitives.IntArray;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.Timing;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import animal.variables.VariableRoles;

public class MedianFilter implements Generator {
	private Language lang;

	private TextProperties h2TextProperties;
	private TextProperties pTextProperties;
	
	private IntMatrix destPic;
	private IntMatrix srcPic;
	private IntArray medianArrayElem;
	
	private ArrayMarker medianArrayMarkerJ;
	private ArrayMarker medianArrayMarkerJp1;
	private ArrayMarker medianArrayMarkerPivot;
	private SourceCode sourceCode;
	
	private MatrixProperties srcPicProps;
	
	private int lesendeZugrSrc;
	private int lesendeMedianArray;
	private int schreibendeMedianArray;
	private int schreibendeDest;
	
	private Text medianArrayTitle;
	
	private ArrayList<Integer> questionIndizes;
	
	private int countMedianArraySorting;
	private boolean showedBlackBoxesDesc;
	private boolean showedMedianDesc;
	private boolean showedSortDescription;
	
	Variables v;
	
	
	private int neighbourRange;
	private int[][] srcPicArray;
	public void init() {
		lang = new AnimalScript("Median Filter für Bildverarbeitung",
				"Igor Braun, Vladimir Bolgov", 800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(
				 Language.INTERACTION_TYPE_AVINTERACTION);
	}
	
	public int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		
		v = lang.newVariables();
		
		countMedianArraySorting = 0;
		showedBlackBoxesDesc = false;
		showedMedianDesc = false;
		showedSortDescription = false;
		
		lesendeZugrSrc = 0;
		v.declare("int", "lesendeZugrSrc", ""+lesendeZugrSrc, VariableRoles.FOLLOWER.name());
		lesendeMedianArray = 0;
		v.declare("int", "lesendeMedianArray", ""+lesendeMedianArray, VariableRoles.FOLLOWER.name());
		schreibendeMedianArray = 0;
		v.declare("int", "schreibendeMedianArray", ""+schreibendeMedianArray, VariableRoles.FOLLOWER.name());
		schreibendeDest = 0;
		v.declare("int", "schreibendeDest", ""+schreibendeDest, VariableRoles.FOLLOWER.name());
		
		v.declare("int", "arrayCounter", "-", VariableRoles.FOLLOWER.name());
		srcPicArray = (int[][]) primitives.get("srcPic");
		neighbourRange = (int) primitives.get("neighbourhoodRange");
		v.declare("int", "neighbourRange", ""+neighbourRange, VariableRoles.FIXED_VALUE.name());

		questionIndizes = new ArrayList<Integer>();
		
		for(int a=0; a<3; a++){
			questionIndizes.add(randInt(1,srcPicArray[0].length*srcPicArray.length));
		}

		int i = 0;
		int j = 0;
		
		// ## HEADER ##---
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 24));
		Text header = lang.newText(new Coordinates(20, 30), "Median Filter für Bildverarbeitung",
				"header", null, headerProps);
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.getHSBColor(38.09f, 37.0f, 82.75f));
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		Rect hRect = lang.newRect(new Offset(-5, -5, "header",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"),
				"hRect", null, rectProps);
		header.show();
		hRect.show();
		// ---###
		
		// ## TEXT PROPERTIES ##---
		h2TextProperties = new TextProperties();
		h2TextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 24));
		
		pTextProperties = (TextProperties) props.getPropertiesByName("descrTextProps");
		pTextProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 18));
		
		CircleProperties cp1 = (CircleProperties) props.getPropertiesByName("statistikCircles");
		
		// ---###
		
		//
		Text mainDescription1 = lang.newText(new Offset(0, 20, "hRect", AnimalScript.DIRECTION_SW), 
				"Der Algorithmus wird jeweils für jeden einzelnen Pixel des Ursprungsbildes angewandt.", 
				"mainDescription1", null, pTextProperties);
		Text mainDescription2 = lang.newText(new Offset(0, 50, "hRect", AnimalScript.DIRECTION_SW), 
				"Dabei lässt sich der Algorithmus in drei Schritte aufteilen: ", 
				"mainDescription2", null, pTextProperties);
		
		Circle c1 = lang.newCircle(new Offset(7, 90, "hRect", AnimalScript.DIRECTION_SW), 5, "d1", null, cp1);
		Text mainDescription3 = lang.newText(new Offset(20, 80, "hRect", AnimalScript.DIRECTION_SW), 
				"Für einen bestimmten Pixel werden alle Pixel aus einer festgelegten Umgebung in ein Array geschrieben.", 
				"mainDescription3", null, pTextProperties);
		Text mainDescription4 = lang.newText(new Offset(20, 100, "hRect", AnimalScript.DIRECTION_SW), 
				"Dabei wird die Umgebung meistens als eine quadratische Matrix ungerader Seitenlänge mit dem untersuchten Pixel in der Mitte definiert. ", 
				"mainDescription4", null, pTextProperties);
		
		Circle c2 = lang.newCircle(new Offset(7, 140, "hRect", AnimalScript.DIRECTION_SW), 5, "d2", null, cp1);
		Text mainDescription5 = lang.newText(new Offset(20, 130, "hRect", AnimalScript.DIRECTION_SW), 
				"Das im letzten Schritt erzeugte Array wird aufsteigen sortiert. ", 
				"mainDescription5", null, pTextProperties);
		
		Circle c3 = lang.newCircle(new Offset(7, 170, "hRect", AnimalScript.DIRECTION_SW), 5, "d3", null, cp1);
		Text mainDescription6 = lang.newText(new Offset(20, 160, "hRect", AnimalScript.DIRECTION_SW), 
				"Der untersuchende Pixel wird durch den Medianwert des Arrays ersetzt.", 
				"mainDescription6", null, pTextProperties);
		
		
		Text mainDescription7 = lang.newText(new Offset(0, 190, "hRect", AnimalScript.DIRECTION_SW), 
				"Nach dem Anwenden des Medianfilters werden das Rauschen und kleine Störungen entfernt. Um ein optimales Ausgabebild zu", 
				"mainDescription7", null, pTextProperties);
		Text mainDescription8 = lang.newText(new Offset(0, 210, "hRect", AnimalScript.DIRECTION_SW), 
				"bekommen sind aber oft mehrere Versuche mit der Auswahl des Parameters für die Umgebungsdistanz nötig.", 
				"mainDescription8", null, pTextProperties);
		
		lang.nextStep("Initialisierung");
		mainDescription1.hide();
		mainDescription2.hide();
		mainDescription3.hide();
		mainDescription4.hide();
		mainDescription5.hide();
		mainDescription6.hide();
		mainDescription7.hide();
		mainDescription8.hide();
		
		c1.hide();
		c2.hide();
		c3.hide();
		
		
		// ## SOURCE PICTURE ##---
		
		srcPicProps = (MatrixProperties) props.getPropertiesByName("srcPicProps");
		srcPicProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		srcPicProps.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
		
		srcPic = lang.newIntMatrix(new Offset(0, 80, "hRect",
				AnimalScript.DIRECTION_SW), srcPicArray, "srcPic", null, srcPicProps);
		
		lang.newText(new Offset(0, -70, "srcPic",
				AnimalScript.DIRECTION_NW), "Source Picture", "srcPicText",
				null, h2TextProperties);
		
		Text srcPicDescription = lang.newText(new Offset(0, 20, "srcPic", AnimalScript.DIRECTION_SW), 
				"Die Matrix 'Source Picture' wird entsprechend der Eingabematrix für die Grauwerte des "
				+ "zu bearbeitenden Bildes initialisiert. ", 
				"srcPicDescription", null, pTextProperties);
		
		lang.nextStep();
		srcPicDescription.hide();
		// ---###
		
		
		// ## MEDIAN ARRAY ##---
		int dimension = neighbourRange * 2 + 1;
		int medianArray[] = new int[dimension * dimension];
		for (i = 0; i < dimension * dimension; i++) {
			medianArray[i] = 0;
		}
		
		ArrayProperties ap = (ArrayProperties) props.getPropertiesByName("medianArrayProps");
		medianArrayElem = lang.newIntArray(new Offset(60, 0, "srcPic",
				AnimalScript.DIRECTION_NE), medianArray, "medianArray", null,
				ap);
		
		medianArrayTitle = lang.newText(new Offset(0, -70, "medianArray",
				AnimalScript.DIRECTION_NW), "Median Array", "medianArrayText",
				null, h2TextProperties);

		
		ArrayMarkerProperties medianArrayMarkerJProp = new ArrayMarkerProperties();
		medianArrayMarkerJProp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "");
		medianArrayMarkerJProp.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
		ArrayMarkerProperties medianArrayMarkerPivotProp = new ArrayMarkerProperties();
		medianArrayMarkerPivotProp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "Median");
		medianArrayMarkerPivotProp.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);

		medianArrayMarkerJ = lang.newArrayMarker(medianArrayElem, 0, "markerJ",
				null, medianArrayMarkerJProp);
		medianArrayMarkerJp1 = lang.newArrayMarker(medianArrayElem, 0, "markerJp1",
				null, medianArrayMarkerJProp);
		medianArrayMarkerPivot = lang.newArrayMarker(medianArrayElem, 0,
				"markerJ", null, medianArrayMarkerPivotProp);

		medianArrayMarkerJ.hide();
		medianArrayMarkerPivot.hide();
		medianArrayMarkerJp1.hide();

		Text medianArrayDescription = lang.newText(new Offset(0, 20, "medianArray", AnimalScript.DIRECTION_SW), 
				"Das Median-Array enthält Grauwerte, die sich um das gerade betrachtete Pixel befinden.", 
				"medianArrayDescription", null, pTextProperties);
		
		lang.nextStep();
		medianArrayDescription.hide();
		// ---###
		
		
		// ## DESTINATION PICTURE ##---
		int destPicArray[][] = new int[srcPic.getNrRows()][srcPic.getNrCols()];
		for (i = 0; i < srcPic.getNrRows(); i++) {
			for (j = 0; j < srcPic.getNrCols(); j++) {
				destPicArray[i][j] = 0;
			}
		}
		MatrixProperties destPicProps = (MatrixProperties) props.getPropertiesByName("destPicProps");
		destPicProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		destPicProps.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
		
		destPic = lang.newIntMatrix(new Offset(60, 0, "medianArray",
				AnimalScript.DIRECTION_NE), destPicArray, "destPic", null, destPicProps);
		
		lang.newText(new Offset(0, -70, "destPic",
				AnimalScript.DIRECTION_NW), "Destination Picture",
				"destPicText", null, h2TextProperties);
		
		Text destPicDescription = lang.newText(new Offset(0, 20, "destPic", AnimalScript.DIRECTION_SW), 
				"Die Matrix 'Destination Picture' wird am Ende die Ausgabe des Algorithmus sein. "
				+ "Am Anfang wird diese mit Nullen initialisiert.", 
				"destPicDescription", null, pTextProperties);
		
		lang.nextStep();
		destPicDescription.hide();
		
		// ---###


		// ## SOURCE CODE ##---
		
		SourceCodeProperties srp = (SourceCodeProperties) props.getPropertiesByName("sourceCodeProps");
		srp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 16));
		
		sourceCode = lang.newSourceCode(new Offset(0, 50, "srcPic",
				AnimalScript.DIRECTION_SW), "pseudoCode", null, srp);
		sourceCode.addMultilineCode(getCodeExampleShorted(), "Code", Timing.INSTANTEOUS);
		// ---###
		
		
		sourceCode.highlight(0);
		sourceCode.highlight(1, 0, true);
		sourceCode.highlight(3);
		sourceCode.highlight(2, 0, true);
		sourceCode.highlight(4, 0, true);
		sourceCode.highlight(5, 0, true);
		
		srcPic.setGridHighlightFillColor (0, 0, Color.RED, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		srcPic.highlightCell(0, 0, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		
		Text neighborhoodSearchDescr = lang.newText(new Offset(0, 30, "srcPic", AnimalScript.DIRECTION_SW), 
				"Alle Grauwerte um den zu durchsuchenden Wert, die maximal " + neighbourRange +
				" Wert(e) entfernt in der Ursprungsmatrix in horizale/vertikale/diagonale Richtung liegen "
				+ " werden in das MedianArray hinzugefügt.", 
				"blackBoxDescription", null, pTextProperties);
				
		lang.nextStep();
		neighborhoodSearchDescr.hide();
		srcPic.unhighlightCell(0, 0, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		
		v.declare("int", "currentRow", "0", VariableRoles.FOLLOWER.name());
		v.declare("int", "currentColumn", "0", VariableRoles.FOLLOWER.name());
		
		for (i = 0; i < srcPic.getNrRows(); i++) {
			v.set("currentRow", ""+i);
			for (j = 0; j < srcPic.getNrCols(); j++) {
				v.set("currentColumn", ""+j);
				calcMedian(i, j);				
			}
		}
		
		medianArrayMarkerJ.hide();
		medianArrayMarkerPivot.hide();
		sourceCode.hide();
		medianArrayTitle.hide();
		medianArrayElem.hide();
		
		
		lang.newText(new Offset(0, 20, "srcPic", AnimalScript.DIRECTION_SW), 
				"Die resultierende Matrix, nach dem Anwenden des Median-Filters, "
				+ "ist rechts zu sehen.", "finishText", null, pTextProperties);
		
		
		
		cp1.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		
		lang.newCircle(new Offset(3, 73, "srcPic", AnimalScript.DIRECTION_SW), 5, "c1", null, cp1);
		lang.newText(new Offset(15, 60, "srcPic", AnimalScript.DIRECTION_SW), 
				"Lesende Zugriffe auf Source Picture: " + lesendeZugrSrc, 
				"srcPicLesen", null, pTextProperties);
		
		lang.newCircle(new Offset(3, 93, "srcPic", AnimalScript.DIRECTION_SW), 5, "c2", null, cp1);
		lang.newText(new Offset(15, 80, "srcPic", AnimalScript.DIRECTION_SW), 
				"Lesende Zugriffe auf Median Array: " + lesendeMedianArray, 
				"srcPicLesen", null, pTextProperties);
		
		lang.newCircle(new Offset(3, 113, "srcPic", AnimalScript.DIRECTION_SW), 5, "c3", null, cp1);
		lang.newText(new Offset(15, 100, "srcPic", AnimalScript.DIRECTION_SW), 
				"Schreibende Zugriffe auf Median Array: " + schreibendeMedianArray, 
				"srcPicLesen", null, pTextProperties);
		
		lang.newCircle(new Offset(3, 133, "srcPic", AnimalScript.DIRECTION_SW), 5, "c4", null, cp1);
		lang.newText(new Offset(15, 120, "srcPic", AnimalScript.DIRECTION_SW), 
				"Schreibende Zugriffe auf Destination Picture: " + schreibendeDest, 
				"srcPicLesen", null, pTextProperties);
		
		
		
		
		lang.nextStep("Ende");
		
		lang.finalizeGeneration();
		String rt = lang.toString();
		return rt;
		
	}

	private void calcMedian(int row, int column) {

		int dimension = neighbourRange * 2 + 1;
		int countMatrixLength = dimension * dimension;

		// highlight current row/column
		srcPic.setGridHighlightFillColor(row, column, Color.RED, Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);
		srcPic.highlightCell(row, column, Timing.INSTANTEOUS,Timing.INSTANTEOUS);
		
		medianArrayElem.setFillColor(0, countMatrixLength - 1, Color.WHITE,
				Timing.INSTANTEOUS, Timing.INSTANTEOUS);
	
		medianArrayMarkerJ.hide();
		medianArrayMarkerPivot.hide();
	

		// build median array
		int i = 0;
		int j = 0;

		for (j = 0; j < countMatrixLength; j++) {
			medianArrayElem.put(j, 0, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			medianArrayElem.unhighlightCell(j, Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);
		}
		

		int medianArray[] = new int[countMatrixLength];
		int arrayCounter = 0;
		int skipedConter = 0;
		
		v.set("arrayCounter", arrayCounter + "");
		
		sourceCode.highlight(11, 0, true);
		sourceCode.highlight(12, 0, true);
		sourceCode.highlight(16, 0, true);
		sourceCode.highlight(17, 0, true);
		
		int counter = row*srcPic.getNrCols()+column + 1;
		lang.nextStep("Itaration "+counter);
		
		for (i = (row - neighbourRange); i <= (row + neighbourRange); i++) {
			for (j = (column - neighbourRange); j <= (column + neighbourRange); j++) {
				int currentRowIndex = i;
				int currentColumnIndex = j;
				if (currentRowIndex < 0 || currentColumnIndex < 0 || currentRowIndex > srcPic.getNrRows() - 1
					|| currentColumnIndex > srcPic.getNrCols() - 1){
					
					sourceCode.highlight(13);
					medianArrayElem.setFillColor(countMatrixLength - skipedConter - 1,
							Color.BLACK, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					skipedConter++;
					
					if(!showedBlackBoxesDesc) {
						Text blackBox = lang.newText(new Offset(0, 30, "srcPic", AnimalScript.DIRECTION_SW), 
								"Wenn die Umgebung aufgrund von den Raendern nicht komplett aufgebaut werden kann, "
								+ "werden die fehlenden Werte im Median-Array schwarz markiert", 
								"blackBoxDescription", null, pTextProperties);
						showedBlackBoxesDesc = true;
						lang.nextStep();
						blackBox.hide();
					} else {
						lang.nextStep();	
					}
					
					
					continue;
				}
				lesendeZugrSrc++;
				v.set("lesendeZugrSrc", ""+lesendeZugrSrc);
				sourceCode.unhighlight(13);

				srcPic.setGridHighlightFillColor(row, column, Color.RED, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				srcPic.highlightCell(row, column, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				
				srcPic.setGridHighlightFillColor(currentRowIndex, currentColumnIndex, (Color)srcPicProps.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY),
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				srcPic.highlightCell(currentRowIndex, currentColumnIndex,
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);

				medianArrayElem.highlightCell(arrayCounter, Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);

				medianArray[arrayCounter] = srcPicArray[currentRowIndex][currentColumnIndex];
				schreibendeMedianArray++;
				v.set("schreibendeMedianArray", ""+schreibendeMedianArray);
				medianArrayElem.put(arrayCounter, medianArray[arrayCounter],
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				sourceCode.highlight(14);
				lang.nextStep();
				sourceCode.unhighlight(14);
				srcPic.unhighlightCell(currentRowIndex, currentColumnIndex,
						Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				medianArrayElem.unhighlightCell(arrayCounter, Timing.INSTANTEOUS,
						Timing.INSTANTEOUS);
				arrayCounter++;
				v.set("arrayCounter", arrayCounter + "");
			}
		}
		srcPic.highlightCell(row, column, Timing.INSTANTEOUS,Timing.INSTANTEOUS);
		
		sourceCode.unhighlight(11, 0, true);
		sourceCode.unhighlight(12, 0, true);
		sourceCode.unhighlight(16, 0, true);
		sourceCode.unhighlight(17, 0, true);
		sourceCode.unhighlight(13);
		
		

		medianArrayMarkerJ.show();
		medianArrayMarkerJp1.move(1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		medianArrayMarkerJp1.show();
		sourceCode.highlight(18);
		int temp = 0;
		if(!showedSortDescription){
			Text sortArrayDescription = lang.newText(new Offset(0, 30, "srcPic", AnimalScript.DIRECTION_SW), 
					"Das Array wird mit BubbleSort beispielhaft sortiert. In den nächsten "
					+ "Schritten wird die Sortierung nicht mehr explizit angezeigt, sondern in "
					+ "einem Schritt ausgeführt.", 
					"sortArrayDescription", null, pTextProperties);
			showedSortDescription = true;
			lang.nextStep();
			sortArrayDescription.hide();
		}
		for (i = 1; i < arrayCounter; i++) {
			for (j = 0; j < arrayCounter - 1; j++) {
				// medianArrayMarkerI.move(i, Timing.INSTANTEOUS,
				// Timing.INSTANTEOUS);
				medianArrayMarkerJ.move(j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				medianArrayMarkerJp1.move(j+1, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				if (medianArray[j] > medianArray[j + 1]) {
					if(countMedianArraySorting < 1){
						lang.nextStep();
					}
					temp = medianArray[j];
					lesendeMedianArray++;
					medianArray[j] = medianArray[j + 1];
					schreibendeMedianArray++;
					lesendeMedianArray++;
					medianArray[j + 1] = temp;
					schreibendeMedianArray++;
					medianArrayElem.swap(j, j + 1, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					if(countMedianArraySorting < 1){
						lang.nextStep();
					}
					v.set("schreibendeMedianArray", ""+schreibendeMedianArray);
				}
				lesendeMedianArray = lesendeMedianArray + 2;
				v.set("lesendeMedianArray", ""+lesendeMedianArray);
			}
			medianArrayElem.highlightCell(arrayCounter - i, Timing.INSTANTEOUS,
					Timing.INSTANTEOUS);
		}
		countMedianArraySorting++;
		medianArrayElem.highlightCell(0, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
		medianArrayMarkerJ.hide();
		medianArrayMarkerJp1.hide();
		
		lang.nextStep();
		sourceCode.unhighlight(18);
		sourceCode.highlight(20);
		int pivotMedian = (arrayCounter / 2) - 1 + (arrayCounter % 2);

		if(questionIndizes.contains(counter)){
			FillInBlanksQuestionModel quest = new FillInBlanksQuestionModel("question" + counter);
			quest.setPrompt("Was ist das Median des MedianArray?");
			quest.addAnswer(new Integer(medianArray[pivotMedian]).toString(), 1, "Right");
			lang.addFIBQuestion(quest);
		};
		
		srcPic.unhighlightElem(row, column, Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);
		medianArrayMarkerPivot.show();
		medianArrayMarkerPivot.move(pivotMedian, Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);
		medianArrayElem.highlightCell(pivotMedian, Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);

		destPic.put(row, column, medianArray[pivotMedian], Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);
		schreibendeDest++;
		v.set("schreibendeDest", ""+schreibendeDest);
		destPic.highlightCell(row, column, Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);
		
		
		
		
		if(!showedMedianDesc) {
			Text medianDesc = lang.newText(new Offset(0, 30, "srcPic", AnimalScript.DIRECTION_SW), 
					"Aus den verfügbaren Grauwerten wird der Median ausgewählt.", 
					"medianSelectDescription", null, pTextProperties);
			showedMedianDesc = true;
			lang.nextStep();
			medianDesc.hide();
		} else {
			lang.nextStep();	
		}
		
		srcPic.setGridBorderColor(row, column, Color.BLACK, Timing.INSTANTEOUS,
				Timing.INSTANTEOUS);
		sourceCode.unhighlight(20);
		srcPic.unhighlightCell(row, column, Timing.INSTANTEOUS,Timing.INSTANTEOUS);
	}

	public String getName() {
		return "Median Filter für Bildverarbeitung";
	}

	public String getAlgorithmName() {
		return "Median Filter für Bildverarbeitung";
	}

	public String getAnimationAuthor() {
		return "Igor Braun, Vladimir Bolgov";
	}

	public String getDescription() {
		return "Der Medianfilter gehört zur Klasse der nichtlinearen Filter in der digitalen Bildverarbeitung, dieser kann nicht durch eine Faltung beschrieben werden können. Hier wird dieser am Beispiel der Grauwerte eine Bildes (in Form einer Matrix) dargestellt. "
				+ "\n"
				+ "\n"
				+ "Der Algorithmus wird jeweils für jeden einzelnen Pixel angewandt. Dabei  lässt sich der Algorithmus in drei Schritte aufteilen: "
				+ "\n"
				+ "\n"
				+ "1. Für einen bestimmten Pixel werden alle Pixel aus einer festgelegten Umgebung in ein Array geschrieben. Dabei wird die Umgebung meistens als eine quadratische Matrix ungerader Seitenlänge mit dem untersuchten Pixel in der Mitte definiert. "
				+ "\n"
				+ "\n"
				+ "2. Das im letzten Schritt erzeugte Array wird aufsteigen sortiert. "
				+ "\n"
				+ "\n"
				+ "3. Der untersuchende Pixel wird durch den Medianwert des Arrays ersetzt.";
	}

	public String getCodeExample() {
		return "Eingegeben wird eine Matrix srcPic mit Grauwerten."
				+ "\n"
				+ "destPicture ist eine Matrix, die am Ende des Algorithmus ausgegeben wird."
				+ "\n"
				+ "Nachbarschaft neightbourRange wird durch die Anzahl der Pixel definiert, die in jede Richtung vom aktuellen Pixel untersucht werden."
				+ "\n"
				+ "MedianFilter(srcPic):"
				+ "\n"
				+ "    for(i = 0; i < srcPic.getNrRows(); i++) {"
				+ "\n"
				+ "        for(j = 0; j < srcPic.getNrCols(); j++) {"
				+ "\n"
				+ "            CalcMedianInNeighbourhood(i, j)"
				+ "\n"
				+ "        }"
				+ "\n"
				+ "    }"
				+ "\n"
				+ "CalcMedianInNeighbourhood(row, column):"
				+ "\n"
				+ "    dimension = neighbourRange * 2 + 1"
				+ "\n"
				+ "    countMatrixLength = dimension * dimension"
				+ "\n"
				+ "    medianArray[countMatrixLength]"
				+ "\n"
				+ "    arrayCounter = 0"
				+ "\n"
				+ "    for(i = (row - neighbourRange); i <= (row + neighbourRange); i++) {"
				+ "\n"
				+ "        for(j = (column - neighbourRange); j <= (column + neighbourRange); j++) {"
				+ "\n"
				+ "            if(i < 0) continue"
				+ "\n"
				+ "            if(j < 0) continue"
				+ "\n"
				+ "            if(i > srcPic.getNrRows() - 1) continue"
				+ "\n"
				+ "            if(j > srcPic.getNrCols() - 1) continue"
				+ "\n"
				+ "            medianArray[arrayCounter] = srcPicArray[i][j]"
				+ "\n"
				+ "            arrayCounter++"
				+ "\n"
				+ "        }"
				+ "\n"
				+ "    }"
				+ "\n"
				+ "    medianArray.sort()"
				+ "\n"
				+ "    pivotMedian = (arrayCounter / 2) - 1 + (arrayCounter % 2)"
				+ "\n" + "    destPicture[i][j] = medianArray[pivotMedian]"
				+ "\n";
	}
	
	public String getCodeExampleShorted() {
		return "MedianFilter(srcPic):"
				+ "\n"
				+ "    for(i = 0; i < srcPic.getNrRows(); i++) {"
				+ "\n"
				+ "        for(j = 0; j < srcPic.getNrCols(); j++) {"
				+ "\n"
				+ "            CalcMedianInNeighbourhood(i, j)"
				+ "\n"
				+ "        }"
				+ "\n"
				+ "    }"
				+ "\n"
				+ "CalcMedianInNeighbourhood(row, column):"
				+ "\n"
				+ "    dimension = neighbourRange * 2 + 1"
				+ "\n"
				+ "    countMatrixLength = dimension * dimension"
				+ "\n"
				+ "    medianArray[countMatrixLength]"
				+ "\n"
				+ "    arrayCounter = 0"
				+ "\n"
				+ "    for(i = (row - neighbourRange); i <= (row + neighbourRange); i++) {"
				+ "\n"
				+ "        for(j = (column - neighbourRange); j <= (column + neighbourRange); j++) {"
				+ "\n"
				+ "            if(i or j out of srcPicArray) continue"
				+ "\n"
				+ "            medianArray[arrayCounter] = srcPicArray[i][j]"
				+ "\n"
				+ "            arrayCounter++"
				+ "\n"
				+ "        }"
				+ "\n"
				+ "    }"
				+ "\n"
				+ "    medianArray.sort()"
				+ "\n"
				+ "    pivotMedian = (arrayCounter / 2) - 1 + (arrayCounter % 2)"
				+ "\n" + "    destPicture[i][j] = medianArray[pivotMedian]"
				+ "\n";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
	}

	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}
	
	
}