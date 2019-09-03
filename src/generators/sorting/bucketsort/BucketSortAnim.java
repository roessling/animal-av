package generators.sorting.bucketsort;

import extras.lifecycle.common.Variable;
import extras.lifecycle.monitor.CheckpointUtils;
import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.updater.ArrayMarkerUpdater;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class BucketSortAnim extends AnnotatedAlgorithm {

	private int[] arrayToSort;

	private int numberOfBuckets = 16;

	private int lowerBound, upperBound;

	private IntArray intArray;
	private Rect[] bucketRects;
	private Text[] bucketRectsLabels;
	private StringArray sortedListArray;
	private TextProperties elementInBucketProperties;
	private RectProperties bucketRectangleProperties;
	
	private ArrayMarkerUpdater amu;

	private void initGraphicalElements(AnimationPropertiesContainer properties) {
		bucketRectangleProperties = (RectProperties) properties.getPropertiesByName("Rectangles");
		intArray = lang.newIntArray(new Coordinates(20, 100), arrayToSort, "array", null, (ArrayProperties) properties.getPropertiesByName("Array Properties"));
		amu = new ArrayMarkerUpdater(lang.newArrayMarker(intArray, 0, "arrayMarker", null), null, null, intArray.getLength() - 1);
		
		sourceCode = lang.newSourceCode(new Offset(0, 50, intArray, AnimalScript.DIRECTION_SW), "source", null, (SourceCodeProperties) properties.getPropertiesByName("Source Code Properties"));

		TextProperties titleProperties = new TextProperties();
		titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 15));
		lang.newText(new Coordinates(30, 30), "BucketSort - numberOfElementsToSort: " + arrayToSort.length, "title", null, titleProperties);

		bucketRects = new Rect[16];
		for (int i = 0; i < 8; i++)
			bucketRects[i] = lang.newRect(new Coordinates(450 + i * 60, 25), new Coordinates(510 + i * 60, 85), "bucket" + i, null);
		for (int i = 0; i < 8; i++)
			bucketRects[i + 8] = lang.newRect(new Coordinates(450 + i * 60, 110), new Coordinates(510 + i * 60, 170), "bucket" + (i + 8), null);

		for (Rect r : bucketRects) {
			r.changeColor(null, (Color) bucketRectangleProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
		}
		TextProperties bucketTitleProperties = new TextProperties();
		titleProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 10));
		titleProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, bucketRectangleProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY));
		bucketRectsLabels = new Text[16];
		for (int i = 0; i < bucketRects.length; i++) {
			// for some reason, the first label is not exactly at (0,-16) NW
			// to bucketRects[0]
			Text label = lang.newText(new Offset(0, -16, bucketRects[i], AnimalScript.DIRECTION_NW), "bucket " + i, "bucketLabel" + i, null, bucketTitleProperties);
			bucketRectsLabels[i] = label;
			label.hide();
			bucketRects[i].hide();
		}
		// add an invisible sortedList
		String[] initial = new String[arrayToSort.length];
		Arrays.fill(initial, "   ");
		sortedListArray = lang.newStringArray(new Coordinates(20, 130), initial, "sortedList", null, (ArrayProperties) properties.getPropertiesByName("Array Properties"));
		sortedListArray.hide();
		lang.nextStep();

		// set properties for later
		elementInBucketProperties = (TextProperties) properties.getPropertiesByName("Elements in buckets properties");
		elementInBucketProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 15));
	}

	private void bucketSort() {
		int numberOfElementsToSort = intArray.getLength();
		// build buckets
		exec("Create-Bucket-List");
		lang.nextStep();
		List<List<Integer>> buckets = new ArrayList<List<Integer>>(numberOfBuckets);
		for (int i = 0; i < numberOfBuckets; i++) {
			buckets.add(new LinkedList<Integer>());
			// show the buckets
			bucketRects[i].show();
			bucketRectsLabels[i].show();
		}
		lang.nextStep();
		// fill buckets
		exec("For-Init-Put-Into-Bucket");
		amu.setVariable(vars.getVariable("i"));
		exec("For-Comp-Put-Into-Bucket");
		intArray.highlightCell(0, null, null);
		lang.nextStep();
		for (int i = 0; i < numberOfElementsToSort; i++) {
			int bucketIndex = putIntoBucket(intArray.getData(i), buckets);
			showElementInBucket(intArray.getData(i), bucketRects[bucketIndex], buckets.get(bucketIndex).size() - 1);
			exec("Put-Into-Bucket");
			lang.nextStep();
			exec("For-Comp-Put-Into-Bucket");
			lang.nextStep();
			if (i + 1 != numberOfElementsToSort) {
				intArray.highlightCell(i + 1, null, null);
				exec("For-Inc-Put-Into-Bucket");
				lang.nextStep();
			}
		}
		// sort buckets and concatenate them
		exec("New-Sorted-List");
		sortedListArray.show();
		lang.nextStep();
		int currentSortedListSize = 0;
		for (int i = 0; i < buckets.size(); i++) {
			exec("For-Bucket");
			bucketRects[i].changeColor(null, (Color) bucketRectangleProperties.get(AnimationPropertiesKeys.FILL_PROPERTY), null, null);
			lang.nextStep();
			List<Integer> bucket = buckets.get(i);
			exec("Sort-The-Bucket");
			Collections.sort(bucket); // any sort algorithm can be used

			for (int j = 0; j < bucket.size(); j++) {
				showElementInBucket(bucket.get(j), bucketRects[i], j);
			}

			lang.nextStep();
			exec("Add-Bucket-To-SortedList");
			lang.nextStep();
			bucketRects[i].changeColor(null, (Color) bucketRectangleProperties.get(AnimationPropertiesKeys.COLOR_PROPERTY), null, null);
			// move the contents of bucket to the end of sortedList here
			for (int j = 0; j < bucket.size(); j++) {
				// for some reason, multiple arrayPuts in one step breaks
				// the alignment of the array
				// even single puts break the alignment
				sortedListArray.put(currentSortedListSize, Integer.toString(bucket.get(j)), null, null);
				currentSortedListSize++;
				lang.nextStep();
			}

		}
	}

	private void showElementInBucket(int element, Rect bucketRect,
			int elementsAlreadyInBucket) {
		Offset textPos = new Offset(2 + (elementsAlreadyInBucket % 3) * 20, 2 + ((int) (elementsAlreadyInBucket / 3)) * 20, bucketRect, AnimalScript.DIRECTION_NW);
		Offset textPosLowerRight = new Offset(18 + (elementsAlreadyInBucket % 3) * 20, 19 + ((int) (elementsAlreadyInBucket / 3)) * 20, bucketRect, AnimalScript.DIRECTION_NW);
		RectProperties rectProp = new RectProperties();
		rectProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
		rectProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		lang.newRect(textPos, textPosLowerRight, "rectToHideOldNumberUnderneathOf" + element, null, rectProp);
		lang.newText(textPos, "" + element, "elementInBucketLabel" + element, null, elementInBucketProperties);
	}

	private int putIntoBucket(Integer elementToSort, List<List<Integer>> buckets) {
		int interval = upperBound - lowerBound;
		int bucketIndex = (elementToSort - lowerBound) * numberOfBuckets / interval;
		if (bucketIndex >= buckets.size()) // fix the mapping at this interval's end
			bucketIndex = buckets.size() - 1;
		buckets.get(bucketIndex).add(elementToSort);
		 CheckpointUtils.checkpointEvent(this, "AddToBucket", 
		     new Variable("bucketsIndex", bucketIndex),		     
		     new Variable("bucketAtIndex", buckets.get(bucketIndex)),
		     new Variable("buckSizeAtIndex", buckets.get(bucketIndex).size()),
		     new Variable("valueAdded", elementToSort));
		return bucketIndex;
	}

	@Override
	public String generate(AnimationPropertiesContainer myProperties, Hashtable<String, Object> primitives) {
		init();

		if (primitives != null) {
			arrayToSort = (int[]) primitives.get("elements to sort");
			numberOfBuckets = (Integer) primitives.get("number of buckets");
		} else {
			arrayToSort = new int[] { 81, 0, 99, 34, 31, 31, 33, 57, 98, 98, 91, 98, 39, 72, 82, 74, 68, 69 };
			numberOfBuckets = 16;
		}

		computeBounds();
		AnimationPropertiesContainer properties = myProperties;
		if (properties == null) {
			properties = new AnimationPropertiesContainer();

			RectProperties rectProp = new RectProperties("Rectangles");
			rectProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
			rectProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
			properties.add(rectProp);

			ArrayProperties arrayProp = new ArrayProperties("Array Properties");
			arrayProp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLUE);
			arrayProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
			arrayProp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.ORANGE);
			arrayProp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
			properties.add(arrayProp);

			SourceCodeProperties scProp = new SourceCodeProperties("Source Code Properties");
			scProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
			scProp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.ORANGE);
			scProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
			properties.add(scProp);

			TextProperties textProp = new TextProperties("Elements in buckets properties");
			textProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
			properties.add(textProp);

		}

		initGraphicalElements(properties);
		
		parse();

		bucketSort();

		return lang.toString();
	}

	private void computeBounds() {
		lowerBound = Integer.MAX_VALUE;
		upperBound = 0;
		for (int i : arrayToSort) {
			if (i < lowerBound) {
				lowerBound = i;
			} else if (i > upperBound) {
				upperBound = i;
			}
		}
	}

	@Override
	public String getAlgorithmName() {
		return "Bucket Sort";
	}

	@Override
	public String getAnimationAuthor() {
		return "Thomas Pilot, Tobias Freudenreich";
	}

	@Override
	public String getCodeExample() {
		StringBuilder builder = new StringBuilder();
		builder.append("List&lt;List&lt;Integer&gt;&gt; buckets = createBuckets(numberOfElementsToSort)\n");
		builder.append("for (int i = 0; i &lt; numberOfElementsToSort; i++)\n");
		builder.append("\tputIntoBucket(array[i], buckets)\n");
		builder.append("\t /* Put the element into the corresponding bucket\n");
		builder.append("\t * Many different algorithms can be used for that\n");
		builder.append("\t * In this case, linear scaling is used\n");
		builder.append("\t * E.g. If the numbers range from 0 to 99 and we have 10 buckets,  \n");
		builder.append("\t * 80 is put into the 8th bucket (with index 7)\n\t */\n");
		builder.append("List&lt;Integer&gt; sortedList = new ArrayList&lt;Integer&gt;()\n");
		builder.append("for (List&lt;Integer&gt; bucket : buckets) {\n");
		builder.append("\tCollections.sort(bucket); // arbitrary algorithm\n");
		builder.append("\tsortedList.addAll(bucket);\n");
		builder.append("}");
		return builder.toString();

	}

	@Override
	public Locale getContentLocale() {
		return Locale.US;
	}

	@Override
	public String getDescription() {
		return "Visualizes how the bucket sort works by first showing how elements are put into buckets and then how the sorted array is constructed";
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
		return "Bucket Sort [version with annotations]";
	}

	@Override
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	@Override
	public String getAnnotatedSrc() {
		return "List<List<Integer>> buckets = createBuckets(numberOfElementsToSort) @label(\"Create-Bucket-List\")\n"
		+ "for (int i = 0;                                                          @label(\"For-Init-Put-Into-Bucket\") @declare(\"int\", \"i\", \"0\")\n"
		+ "     i < numberOfElementsToSort;                                         @label(\"For-Comp-Put-Into-Bucket\") @continue\n"
		+ "     i++)                                                                @label(\"For-Inc-Put-Into-Bucket\") @continue @inc(\"i\")\n"
		+ "   /* Many different algorithms can be used for putIntoBucket()          @label(\"Comment-1\")\n"
		+ "    * In this case, linear scaling is used                               @label(\"Comment-2\")\n"
		+ "    * E.g. If the numbers range from 0 to 99 and we have 10 buckets,     @label(\"Comment-3\")\n"
		+ "    * 80 is put into the 8th bucket (with index 7)                       @label(\"Comment-4\")\n"
		+ "    */                                                                   @label(\"Comment-5\")\n"
		+ "   putIntoBucket(array[i], buckets)                                      @label(\"Put-Into-Bucket\")\n"
		+ "List<Integer> sortedList = new ArrayList<Integer>()                      @label(\"New-Sorted-List\")\n"
		+ "for (List<Integer> bucket : buckets) {                                   @label(\"For-Bucket\")\n"
		+ "   Collections.sort(bucket); // arbitrary algorithm                      @label(\"Sort-The-Bucket\")\n"
		+ "   sortedList.addAll(bucket);                                            @label(\"Add-Bucket-To-SortedList\")\n"
		+ "}                                                                        @label(\"end-for\")\n";
	}

}
