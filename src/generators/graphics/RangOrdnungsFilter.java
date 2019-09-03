/*
 * RangOrdnungsFilter.java
 * Gruppe 40: Faris und Khalid, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;
import java.util.Locale;

//import Algo.Bubblesort;
//import Algo.Filter;
//import Algo.Sort;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.MatrixPrimitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

import java.util.Hashtable;

import javax.swing.JOptionPane;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class RangOrdnungsFilter implements ValidatingGenerator {

	private Language lang;
	private double quantil;
	private int[][] image;
	private int maskSize;
	MatrixProperties maskprops;
	ArrayProperties arrayStringProps;
	int[] position = this.defaultPostions();
	private final int DISTANCE = 25;
	private SourceCode sc;
	private SourceCodeProperties sourceCode;
	TextProperties titelprops;
	int firstTime = 0;
	private ArrayProperties imageProps;
	
	public void init() {
		lang = new AnimalScript("RangOrdnungsFilter", "Faris und Khalid", 800,
				600);

		// lang.setStepMode(true);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		
		
		
		lang.setStepMode(true);
		imageProps = (ArrayProperties) props.getPropertiesByName("imageProps");
		sourceCode = (SourceCodeProperties) props
				.getPropertiesByName("sourceCode");
		TextProperties textProps = (TextProperties) props
				.getPropertiesByName("textProps");
	
		
		Text introduction0 = lang.newText(new Coordinates(10, 25), "Ein Rangordnungsfilter ist eine nichtlineare Filter der Verwendung findet in der digitalen Bildverarbeitung.", 
				"introducton0", null, textProps);
		
		Text introduction1 = lang.newText(new Coordinates(10, 40), "Diese Filter ist geeignet um Bildrauschen speziell Salt and Peper rauschen zu entfernen und Bilder zu glätten. ", 
				"introducton1", null, textProps);
		Text introduction2 = lang.newText(new Coordinates(10, 55), "In jeder Iteration 'bewegt' sich die Maske über das Bild und alle Grauwerte die da drunter liegen werden", 
				"introducton2", null, textProps);
		
		Text introduction3 = lang.newText(new Coordinates(10, 70), "der größe nach sortiert. Mithilfe des Quantil q und der Anzahl der Elemente (Masksize*Masksize) die sortiert wurden," , 
				"introducton3", null, textProps);
		
		Text introduction4 = lang.newText(new Coordinates(10, 85), "wird ein Grauwert innerhalb der sortierten Sequenz ausgewählt. Dieser ausgewählte Wert wird dann durch aktuellen Grauwert ersetzt." , 
				"introducton4", null, textProps);
		
		Text introduction5 = lang.newText(new Coordinates(10, 100), "Für das Quantil q gilt, dass q zwischen null und eins liegt. Falls der q = 0.5 ist entspricht es einen Medianfilter," , 
				"introducton5", null, textProps);
		
		
		Text introduction6 = lang.newText(new Coordinates(10, 115), "für q = 0 ein Minimumfilter , für q= 1 entpricht es ein Maximumsfilter und sonst einen Rangordnungsfilter." , 
				"introducton6", null, textProps);
		
		
		
		lang.nextStep();
		quantil = (double) primitives.get("quantil");
		image = (int[][]) primitives.get("Image");
		maskSize = (int) primitives.get("maskSize");
		image = this.zeroPadding(image);
		int zeroLength = maskSize - 1;
		showSorceCode();
		Text algoName = lang.newText(new Coordinates(5,10), getFilterName(quantil), "Name",null,textProps);
		
		algoName.setFont(new Font("Bold", Font.PLAIN, 17),
				new TicksTiming(0), new TicksTiming(0));
		
		introduction0.hide();
		introduction1.hide();
		introduction2.hide();
		introduction3.hide();
		introduction4.hide();
		introduction5.hide();
		introduction6.hide();
		lang.setStepMode(true);
		
		Text titel = lang.newText(new Coordinates(10, 90), "Masksize is "
				+ maskSize + " x " + maskSize, "next Step", null, textProps);
		titel.setFont(new Font("Bold", Font.PLAIN, 12), new TicksTiming(0),
				new TicksTiming(0));
		titel.hide();
		
		
		
		lang.nextStep();
		
		
		
		
		

		Text imageTitel = lang.newText(new Coordinates(480, 80), "Image",
				"next Step", null, textProps);
		imageTitel.setFont(new Font("Bold", Font.PLAIN, 12),
				new TicksTiming(0), new TicksTiming(0));

		Text imageResultTitel = lang.newText(new Coordinates(480, 320),
				"Result", "next Step", null, textProps);
		
		
		imageResultTitel.setFont(new Font("Bold", Font.PLAIN, 12),
				new TicksTiming(0), new TicksTiming(0));
		
		
		
		sc.highlight(0);
		lang.nextStep("Initialisation");
		MatrixProperties matrixprops = new MatrixProperties();
		matrixprops.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		matrixprops.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.MAGENTA);
		matrixprops.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.RED);
		matrixprops.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.BLUE);

		Rect rect = lang.newRect(new Coordinates(596, 32), new Coordinates(
				596 + 25, 32 + 25), "42", null);
		rect.hide();

		matrixprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		matrixprops.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
		matrixprops.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, 25);
		matrixprops.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, 25);
		matrixprops.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
		IntMatrix matrix = lang.newIntMatrix(new Coordinates(600, 30), image,
				"matrix", null, matrixprops);
		matrix.hide();
		rect.hide();

		maskprops = new MatrixProperties();

		maskprops.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.GREEN);
		maskprops.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		maskprops.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, 25);
		maskprops.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, 25);

		int[][] filterImage = new int[image.length][image[0].length];

		IntMatrix result = lang.newIntMatrix(new Coordinates(600, 400),
				filterImage, "matrix", null, matrixprops);
		result.hide();

		Timing defaultTiming = new TicksTiming(100);
		int width = 0;
		int height = 0;

		Coordinates[][] coord = new Coordinates[maskSize][maskSize];

		IntArray[][] img = new IntArray[image.length][image[0].length];

		IntArray[][] imgResult = new IntArray[image.length][image[0].length];

		for (int i = 0; i < img.length; i++) {
			for (int j = 0; j < img[0].length; j++) {

				img[i][j] = lang.newIntArray(new Coordinates(
						600 + (i * DISTANCE), 80 + (DISTANCE * j)),
						new int[] { image[i][j] }, "Element: (" + i + "," + j
								+ " )", null, imageProps);

				imgResult[i][j] = lang.newIntArray(new Coordinates(
						600 + (i * DISTANCE), 320 + (DISTANCE * j)),
						new int[] { 0 }, "Element: (" + i + "," + j + " )",
						null, imageProps);

				imgResult[i][j].hide();

				if (!(i >= zeroLength / 2 && i < img.length - zeroLength / 2)) {

					imgResult[i][j].hide();
					img[i][j].hide();
				} else if (!(j >= zeroLength / 2 && j < img[0].length
						- zeroLength / 2)) {

					imgResult[i][j].hide();
					img[i][j].hide();

				}
			}
		}
		lang.nextStep();
		sc.toggleHighlight(0, 1);
		for (int i = 0; i < img.length; i++) {
			for (int j = 0; j < img[0].length; j++) {

				if (!(i >= zeroLength / 2 && i < img.length - zeroLength / 2)) {

					continue;
				} else if (!(j >= zeroLength / 2 && j < img[0].length
						- zeroLength / 2)) {

					continue;

				} else
					imgResult[i][j].show();
			}
		}

		lang.nextStep();
		sc.toggleHighlight(1, 2);
		for (int i = 0; i < img.length; i++) {
			for (int j = 0; j < img[0].length; j++) {
				if (!(i >= zeroLength / 2 && i < img.length - zeroLength / 2)) {
					img[i][j].show(defaultTiming);
					img[i][j].highlightCell(0, defaultTiming, defaultTiming);

				} else if (!(j >= zeroLength / 2 && j < img[0].length
						- zeroLength / 2)) {
					img[i][j].show(defaultTiming);
					img[i][j].highlightCell(0, defaultTiming, defaultTiming);

				} else
					continue;
			}
		}
		lang.nextStep();
		sc.toggleHighlight(2, 3);
		titel.show();
		for (int i = 0; i < img.length; i++) {
			for (int j = 0; j < img[0].length; j++) {
				if (!(i >= zeroLength / 2 && i < img.length - zeroLength / 2))

					img[i][j].unhighlightCell(0, defaultTiming, defaultTiming);
				else if (!(j >= zeroLength / 2 && j < img[0].length
						- zeroLength / 2))
					img[i][j].unhighlightCell(0, defaultTiming, defaultTiming);
				else
					continue;
			}
		}

		for (height = 0; height < matrix.getNrRows(); height++) {

			for (width = 0; width < matrix.getNrCols(); width++) {

				if (!(width >= zeroLength / 2 && width < matrix.getNrRows()
						- zeroLength / 2)) {
					filterImage[width][height] = 0;

					result.put(height, width, 0, defaultTiming, defaultTiming);

					// IntArray
					// imgResult[height][width].put(0, 0, defaultTiming,
					// defaultTiming);

				} else if (!(height >= zeroLength / 2 && height < matrix
						.getNrCols() - zeroLength / 2)) {

					result.put(height, width, 0, defaultTiming, defaultTiming);
					// IntArray
					// imgResult[height][width].put(0, 0, defaultTiming,
					// defaultTiming);
				}
				// filterImage[width][height] = 0;
				else {
					// rect.show();
					IntArray value = lang.newIntArray(
							img[width][height].getUpperLeft(),
							new int[] { img[width][height].getData(0) },
							"RESULT", null, imageProps);
					value.hide();

					// if(firstTime == 0){
					// lang.nextStep();
					// sc.highlight("height");
					// firstTime++;
					// }

					if (width == maskSize / 2) {
						lang.nextStep();
						titel.hide();
						sc.unhighlight(3);
						sc.highlight("height");
						// lang.nextStep("Calculation Line " + height);
						lang.nextStep();
						sc.unhighlight("height");
					}
					// sc.toggleHighlight(2,3);
					// sc.toggleHighlight(3,4);
					// sc.toggleHighlight(4,5);
					lang.nextStep();
					sc.toggleHighlight("height");
					sc.toggleHighlight("width");
					lang.nextStep("Calculation image Position: (" + width + ","
							+ height + ")");
					int startPointWidth = width - maskSize / 2;
					int startPointHeight = height - maskSize / 2;

					int[][] partImage = new int[maskSize][maskSize];

					int[][] partImg = this.partImg(width, height);

					int[] lineareUnsortArray = this
							.convertLineareArray(partImg);

					// ArrayProperties arrayProps = new ArrayProperties();
					// arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY,
					// Color.YELLOW);
					// arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY,
					// true);
					// arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
					// Color.GRAY);
					// arrayProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY,
					// 0);

					IntArray unsortArray = lang.newIntArray(new Coordinates(10,
							60), lineareUnsortArray, "unSortArray", null,
							imageProps);

					unsortArray.hide();

					lang.nextStep();
					sc.unhighlight(4);
					sc.toggleHighlight(5, 6);

					img[width][height].highlightElem(0, null, null);

					for (int i = 0; i < partImage.length; i++) {

						for (int j = 0; j < partImage[0].length; j++) {

							img[startPointWidth + i][startPointHeight + j]
									.highlightCell(0, null, null);

							coord[i][j] = (Coordinates) img[startPointWidth + i][startPointHeight
									+ j].getUpperLeft();

							// imgResult[startPointWidth + i][startPointHeight +
							// j]
							// .highlightCell(0, null, null);

							partImage[i][j] = image[startPointWidth + i][startPointHeight
									+ j];

						}

					}

					lang.nextStep();
					sc.toggleHighlight(6, 7);

					for (int i = 0; i < partImage.length; i++) {

						for (int j = 0; j < partImage[0].length; j++) {

							// matrix.highlightCell(startPointHeight + j,
							// startPointWidth + i, null, null);
							// result.highlightCell(startPointHeight + j,
							// startPointWidth + i, null, null);

							// coord[i][j] = (Coordinates) img[startPointWidth +
							// i][startPointHeight
							// + j].getUpperLeft();

							if (startPointWidth + i == width
									&& startPointHeight + j == height) {

								value.moveTo(null, null, new Coordinates(10 + j
										* 13 + 13 * i * maskSize, 60),
										new TicksTiming(85),
										new TicksTiming(85));
							}

							img[startPointWidth + i][startPointHeight + j]
									.moveTo(null, null, new Coordinates(10 + j
											* 13 + 13 * i * maskSize, 60),
											new TicksTiming(85),
											new TicksTiming(85));

							// imgResult[startPointWidth + i][startPointHeight +
							// j]
							// .highlightCell(0, null, null);
							//
							// partImage[i][j] = image[startPointWidth +
							// i][startPointHeight
							// + j];

						}

					}

					unsortArray.show(new TicksTiming(200));
					lang.nextStep();
					sc.toggleHighlight(7, 8);
					matrix.highlightElem(height, width, null, null);
					result.highlightElem(height, width, null, null);

					value.highlightElem(0, null, null);

					int[] sortArray = this.bubbelsort(
							this.convertLineareArray(partImg), unsortArray);

					lang.nextStep();
					sc.toggleHighlight(8, 9);
					ArrayMarker q = lang.newArrayMarker(unsortArray,
							this.getQuantilPosition(sortArray), "q", null);

					int mean = this.getFilterValue(sortArray);
					filterImage[width][height] = mean;

					lang.nextStep();
					sc.toggleHighlight(9, 10);
					value.put(0, mean, null, null);
					value.show();
					result.put(height, width, mean, defaultTiming,
							defaultTiming);
					imgResult[width][height].highlightElem(0, new TicksTiming(
							85), new TicksTiming(85));

					// IntArray
					//
					// IntArray value =
					// lang.newIntArray(img[width][height].getUpperLeft(), new
					// int []{mean},"RESULT",null, imageProps);
					value.moveTo(null, null,
							imgResult[width][height].getUpperLeft(),
							new TicksTiming(85), new TicksTiming(85));
					imgResult[width][height].put(0, mean, new TicksTiming(85),
							new TicksTiming(85));

					unsortArray.hide(new TicksTiming(60));
					// lineareSortArray.hide(new TicksTiming(60));
					value.unhighlightElem(0, null, null);
					value.hide(new TicksTiming(36));
					titel.hide();

					for (int i = 0; i < partImage.length; i++) {

						for (int j = 0; j < partImage[0].length; j++) {

							matrix.unhighlightCell(startPointHeight + j,
									startPointWidth + i, null, null);
							result.unhighlightCell(startPointHeight + j,
									startPointWidth + i, null, null);
							imgResult[startPointWidth + i][startPointHeight + j]
									.unhighlightCell(0, null, null);

							img[startPointWidth + i][startPointHeight + j]
									.moveTo(null, null,
											new Coordinates(coord[i][j].getX(),
													coord[i][j].getY()),
											defaultTiming, defaultTiming);
							img[startPointWidth + i][startPointHeight + j]
									.unhighlightCell(0, null, null);
							//
							// imgResult[startPointWidth + i][startPointHeight +
							// j]
							// .unhighlightCell(0, null, null);
							if (width != 0) {

							}
						}

					}

					matrix.unhighlightElem(height, width, null, null);
					result.unhighlightElem(height, width, null, null);
					img[width][height].unhighlightElem(0, null, null);
					imgResult[width][height].highlightElem(0, null, null);
					;

					lang.nextStep();
					sc.unhighlight(10);

				}

				rect.moveBy(null, 33, 0, defaultTiming, defaultTiming);

				position = this.defaultPostions();
				// lang.nextStep();
				// sc.unhighlight(10);
				// sc.highlight("height");
			}
			rect.moveBy(null, -(33 * image.length), 33, defaultTiming,
					defaultTiming);

		}
		sc.toggleHighlight("FINISH");
		sc.toggleHighlight("END");
		
		imageTitel.hide();
		
		for (int i = 0; i < img.length; i++) {

			for (int j = 0; j < img[0].length; j++) {
				img[i][j].hide();
				}
			}
	
		
		Text end00 = lang.newText(new Coordinates(120,90), "Das ist das Ende des Algorithmus "+ getFilterName(quantil)+". Wenn Sie ihr Wissen vertiefen möchten empfehle ich ihnen den Artikel http://de.wikipedia.org/wiki/Rangordnungsfilter ." , 
				"end00", null, textProps);
		Text end0 = lang.newText(new Coordinates(120,105), "Es gibt noch die Klasse der lineare Filter wie den Boxfilter/Mittelwertfilter und den Gaußfilter die auch das Rauschen unterdrücken!" , 
				"end0", null, textProps);
		Text end1 = lang.newText(new Coordinates(120, 120), "Nähere Informationen zu ihrere funktionweise finden Sie bei Animal unter den Ordner Graphics." , 
				"end1", null, textProps);
		
		return lang.toString();
	}

	private void showSorceCode() {

		sc = lang.newSourceCode(new Coordinates(10, 300), "MySourceCode", null,
				sourceCode);
		sc.addCodeLine("public int [][] rangOrdnungsFilter([] [] image){",
				null, 0, new TicksTiming(30));
		sc.addCodeLine(
				"int [][] resultImage = new [image.length][image[0].length];",
				null, 1, new TicksTiming(30));
		sc.addCodeLine("int [] [] img = zeroPadding(image,masksize);", null, 1,
				new TicksTiming(30));
		sc.addCodeLine("int [][]mask = new int [masksize][masksize];", null, 1,
				new TicksTiming(30));
		sc.addCodeLine("for(int height = 0; height < img.length;height++){",
				"height", 1, new TicksTiming(30));
		sc.addCodeLine("for(int width = 0; width < img[0].length;width++){",
				"width", 2, new TicksTiming(30));
		sc.addCodeLine("mask = calcMaskimg(height,width);", null, 3,
				new TicksTiming(30));
		sc.addCodeLine("int [] lineareSeq = convertLineareSeq(mask);", null, 3,
				new TicksTiming(30));
		sc.addCodeLine("lineareSeq = sort(lineareSeq);", null, 3,
				new TicksTiming(30));
		sc.addCodeLine("int value = getQuantilVale(lineareSeq);", null, 3,
				new TicksTiming(30));
		sc.addCodeLine("resultImage[height][width] = value;", null, 3,
				new TicksTiming(30));
		sc.addCodeLine("}", null, 2, new TicksTiming(30));
		sc.addCodeLine("}", null, 1, new TicksTiming(30));
		sc.addCodeLine("return resultImage;", "FINISH", 1, new TicksTiming(30));
		sc.addCodeLine("}", "END", 0, new TicksTiming(30));

	}

	private int getQuantilPosition(int[] sortArray) {

		if (quantil < 0 || quantil > 1) {
			// Default Configuration is MeanFilter if not(0 <= quantil <= 1)
			return sortArray.length / 2;
		}

		int index = (int) (quantil * sortArray.length);

		if (index == sortArray.length)
			return index - 1;
		else
			return index;

	}

	public int[][] partImg(int width, int height) {

		int startPointWidth = width - maskSize / 2;
		int startPointHeight = height - maskSize / 2;

		int[][] partImage = new int[maskSize][maskSize];

		for (int i = 0; i < partImage.length; i++) {
			for (int j = 0; j < partImage[0].length; j++) {

				partImage[i][j] = image[startPointWidth + i][startPointHeight
						+ j];
				// partimg.put(i, j, image[startPointWidth + i][startPointHeight
				// + j], new TicksTiming(100),new TicksTiming(100));

			}

		}
		return partImage;

	}

	public int[] convertLineareArray(int[][] array) {

		int[] larry = new int[array.length * array[0].length];
		int count = 0;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				larry[count] = array[i][j];
				count++;
			}

		}

		return larry;
	}

	public int getFilterValue(int[] array) {

		array = this.bubbelsort(array);

		int filterValue = (int) (array.length * quantil);

		if (filterValue == array.length)
			filterValue--;

		return array[filterValue];

	}

	public int[][] zeroPadding(int[][] img) {

		int zeroLength = maskSize - 1;

		int[][] extendedImage = new int[zeroLength + img.length][zeroLength
				+ img[0].length];

		for (int i = 0; i < extendedImage.length; i++) {
			for (int j = 0; j < extendedImage[0].length; j++) {
				if (!(i >= zeroLength / 2 && i < extendedImage.length
						- zeroLength / 2))

					extendedImage[i][j] = 0;
				else if (!(j >= zeroLength / 2 && j < extendedImage[0].length
						- zeroLength / 2))
					extendedImage[i][j] = 0;
				else
					extendedImage[i][j] = img[i - zeroLength / 2][j
							- zeroLength / 2];
			}
		}
		return extendedImage;
	}

	public int[] bubbelsort(int[] array) {
		int temp = 0;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length - i - 1; j++) {

				if (array[j] > array[j + 1]) {
					temp = array[j];
					array[j] = array[j + 1];
					array[j + 1] = temp;

				}
			}
		}

		return array;
	}

	public int[] bubbelsort(int[] array, IntArray unsort) {
		int temp = 0;
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array.length - i - 1; j++) {

				if (array[j] > array[j + 1]) {
					temp = array[j];
					array[j] = array[j + 1];
					array[j + 1] = temp;

					// Remember the origin position to fly back correctly
					temp = position[j];
					position[j] = position[j + 1];
					position[j + 1] = temp;

					unsort.swap(j + 1, j, new TicksTiming(100),
							new TicksTiming(100));

				}
			}
		}

		return array;
	}

	public int[] defaultPostions() {

		int[] array = new int[maskSize * maskSize];

		for (int i = 0; i < array.length; i++) {

			array[i] = i;

		}
		return array;

	}

	public String getName() {
		return "RangOrdnungsFilter";
	}

	public String getAlgorithmName() {
		return "RangordnungsFilter";
	}

	public String getAnimationAuthor() {
		return "Gruppe 40: Faris und Khalid";
	}

	public String getDescription() {
		return "Ein Rangordnungsfilter ist eine nichtlineare Filter der Verwendung findet in der digitalen Bildverarbeitung."
				+ "Diese Filter ist geeignet um Bildrauschen speziell Salt and Peper rauschen zu entfernen und Bild zu glätten. "
				+ "In jeder Iteration 'bewegt' sich die Maske über das Bild und alle Grauwerte die da drunter liegen werden"
				+" der größe nach sortiert. Mithilfe des Quantil q und der Anzahl der Elemente (Masksize*Masksize) die sortiert wurden," 
				+" wird ein Grauwert innerhalb der sortierten Sequenz ausgewählt. Dieser ausgewählte Wert wird dann durch aktuellen Grauwert ersetzt."
				+ "Für das Quantil q gilt, dass q zwischen null und eins liegt. Falls der q = 0.5 ist entspricht es einen Medianfilter, "
				+ "für q = 0 ein Minimumfilter , für q= 1 entpricht es ein Maximumsfilter und sonst einen Rangordnungsfilter.";
	}

	public String getCodeExample() {
		return "public int [][] rangOrdnungsFilter([] [] image){\n\t int [][] resultImage = new [image.length][image[0].length];\n\t result = zeroPadding(result,masksize); \n\t int [][]mask;"
				+ "\n\t for(int height = 0; height < image.length;height++){"
				+ "\n\t\t for(int width = 0; width < image[0].length;width++){"
				+ "\n\t\t\t mask = calcMaskimg(height,width);"
				+ "\n\t\t\t int [] lineareSeq = convertLineareSeq(mask); "
				+ "\n\t\t\t lineareSeq = sort(lineareSeq); "
				+ "\n\t\t\t int value = getQuantilVale(lineareSeq); "
				+ "\n\t\t\t resultImage[height][width] = value; "
				+ "\n\t\t }"
				+ "\n\t }" + "\n\t return resultImage;" + "\n } ";
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
	
	public String getFilterName(double quantil){
		
		if(quantil == 0.5)
			return "Medianfilter";
		else if(quantil == 1)
			return "Maximumfilter";
		else if(quantil == 0)
			return "Minimumfilter";
		else
			return "Rangordnungsfilter";
		
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		double q = (double) arg1.get("quantil");
		int m = (int) arg1.get("maskSize");
		int[][] i = (int[][]) arg1.get("Image");
		JOptionPane error = new JOptionPane();
		if (q < 0 || q > 1) {
			JOptionPane.showMessageDialog(error,
					"Quantil muss zwischen 0 und 1 liegen!");
			throw new IllegalArgumentException();
		}
		if (m <= 0) {
			JOptionPane.showMessageDialog(error,
					"Größe der Maske muss größer als 0 sein!");
			throw new IllegalArgumentException();
		}

		if (m % 2 == 0) {
			JOptionPane.showMessageDialog(error,
					"Die Maskengröße muss ungerade sein!");
			throw new IllegalArgumentException();
		}

		if (i.length != i[0].length) {
			JOptionPane.showMessageDialog(error,
					"Das Bild muss eine Quadratische Matrix sein!");
			throw new IllegalArgumentException();
		}
		return true;
	}

}