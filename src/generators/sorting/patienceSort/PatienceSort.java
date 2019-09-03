/*
 * PatienceSort.java
 * Patience Sort, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.sorting.patienceSort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.VisualStack;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.exceptions.LineNotExistsException;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.StackProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * PatienceSort
 * 
 * @author Yue Hu, Xinyu Liu
 *
 */
public class PatienceSort implements Generator {
	private Language language;
	private ArrayProperties array;
	private int[] intArray;

	/**
	 * Constructor
	 * 
	 * @param l
	 */
	public PatienceSort(Language l) {
		// Store the language object
		language = l;
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		language.setStepMode(true);
	}

	/**
	 * default Constructor
	 */
	public PatienceSort() {
		init();
		language.setStepMode(true);
	}

	private int pointerCounter = 0;


	/**
	 * default duration for swap processes
	 */
	public final static Timing defaultDuration = new TicksTiming(30);

	public void init() {
		language = new AnimalScript("Patience Sort", "Patience Sort", 800, 600);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {
		array = (ArrayProperties) props.getPropertiesByName("array");
		intArray = (int[]) primitives.get("intArray");

		PatienceSort ps = new PatienceSort(language);
		ps.algorithm(intArray);

		language.finalizeGeneration();
		return language.toString();
	}

	/**
	 * Main algorithm
	 * @param input
	 */
	public <E extends Comparable<? super E>> void algorithm(int[] input) {
		// Title
/*		SourceCodeProperties title = new SourceCodeProperties();
		title.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		title.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
				Font.PLAIN, 30));
		title.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		title.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode our_title = language.newSourceCode(new Coordinates(0, 0),
				"sourceCode", null, title);
		our_title.addCodeLine("PatienceSort", null, 0, null);*/
		TextProperties title = new TextProperties();
		title.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
				Font.PLAIN, 30));
		Text our_title = language.newText(new Coordinates(10, 20),
				"PatienceSort", "PatienceSort", null, title);
		//TODO Einleitung
		SourceCodeProperties eineitung = new SourceCodeProperties();
		eineitung.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		eineitung.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",
				Font.PLAIN, 15));
		eineitung.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		eineitung.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode our_eineitung = language.newSourceCode(new Coordinates(10, 80),
				"sourceCode", null, eineitung);
		
		our_eineitung.addCodeLine("Introduction:", null, 0, null);
		our_eineitung.addCodeLine("", null, 0, null);
		our_eineitung.addCodeLine("The algorithm's name derives from a simplified variant of the patience card game.This game begins with a shuffled deck of cards. ", null, 0, null);
		our_eineitung.addCodeLine("These cards are dealt one by one into a sequence of piles on the table, according to the following rules: ", null, 0, null);
		our_eineitung.addCodeLine("", null, 0, null);
		our_eineitung.addCodeLine("1. Initially, there are no piles. The first card dealt forms a new pile consisting of the single card.", null, 0, null);
		our_eineitung.addCodeLine("2. Each subsequent card is placed on the leftmost existing pile whose top card has a value greater than or equal the new card's value, ", null, 0, null);
		our_eineitung.addCodeLine("or to the right of all of the existing piles, thus forming a new pile. ", null, 0, null);
		our_eineitung.addCodeLine("3. When there are no more cards remaining to deal, the game ends. ", null, 0, null);
		our_eineitung.addCodeLine("", null, 0, null);
		our_eineitung.addCodeLine("This card game is turned into a two-phase sorting algorithm, as follows. ", null, 0, null);
		our_eineitung.addCodeLine("Given an array of n elements from some totally ordered domain, consider this array as a collection of cards and simulate the patience sorting game. ", null, 0, null);
		our_eineitung.addCodeLine("When the game is over, recover the sorted sequence by repeatedly picking off the minimum visible card;  ", null, 0, null);
		our_eineitung.addCodeLine("in other words, perform a k-way merge of the p piles, each of which is internally sorted. ", null, 0, null);
		language.nextStep();
		our_eineitung.hide();
		
		// Rect
		RectProperties rect = new RectProperties();
		Rect our_rect = language.newRect(new Coordinates(0, 20),
				new Coordinates(340, 70), "null", null, rect);
		try {
			// Start
			sort(input);
		} catch (LineNotExistsException e) {
			e.printStackTrace();
		}
		language.nextStep();
	}

	/**
	 * Main sort
	 * @param input
	 * @param sc
	 */
	@SuppressWarnings("unchecked")
	public void sort(int[] input) {
		// first, set the visual properties for the source code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 15));
		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		// now, create the source code entity
		SourceCode sc = language.newSourceCode(new Coordinates(20, 100),
				"sourceCode", null, scProps);

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy
		sc.addCodeLine("Start: ", null, 0, null); // 0
		sc.addCodeLine("Get the first element from the unsorted Array.", null, 0,
				null);
		sc.addCodeLine("", null, 0,null);
		sc.addCodeLine("Iteration for Array:", null, 0,
				null);
		sc.addCodeLine(
				"If there is no pile or the element larger than the topmost element",
				null, 0, null);
		sc.addCodeLine("of the rightmost pile, then create a new pile ", null,
				0, null);
		sc.addCodeLine("and push the this element on top of this pile.", null,
				0, null);
		
		sc.addCodeLine("", null, 0,null);
		
		sc.addCodeLine("If the element smaller than topmost one on pile,",
				null, 0, null);
		sc.addCodeLine("push this element on top of this pile.", null, 0, null);
		
		
		
		
		
		List<Pile<Integer>> piles = new ArrayList<Pile<Integer>>();

		// first, set the visual properties (somewhat similar to CSS)
		ArrayProperties arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.YELLOW);

		// now, create the IntArray object, linked to the properties
		IntArray ia = language.newIntArray(new Coordinates(550, 20), input,
				"intArray", null, arrayProps);

		// ========================Aufgabe 5.1===========================
		TwoValueCounter counter = language.newCounter(ia); 
		CounterProperties cp = new CounterProperties(); 
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); 
		cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE); 															
		TwoValueView view = language.newCounterView(counter, new Coordinates(
				80, 360), cp, true, true);
		// ================================================================
		// start a new step after the array was created
		language.nextStep();

		pointerCounter++;
		// Array, current index, name, display options, properties
		ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
		arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
		arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		ArrayMarker iMarker = language.newArrayMarker(ia, 0, "i"
				+ pointerCounter, null, arrayIMProps);

		StackProperties stackProperties = new StackProperties();
		// save all Piles in stacks
		List<VisualStack> stacks = new ArrayList<VisualStack>();

		// used to check Pile,whether the pile exited
		Set<Integer> set = new HashSet<Integer>();
		int iCounter = 0;
		counter.assignmentsInc(1);
		// sort into piles

		// =================== Aufgabe 5 .2 =================
		if (input.length >= 2) {
			int firstElement = input[0];
			int secondElement = input[1];
			language.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
			// lots of code ;-)

			QuestionGroupModel qgm = new QuestionGroupModel("1", 1);

			FillInBlanksQuestionModel ques1 = new FillInBlanksQuestionModel(
					"year");
			ques1.setPrompt("Should we create a new pile for the first element?(please answer yes or no)");
			ques1.addAnswer("yes", 1, "Congratulations! You are right!");
			ques1.setGroupID("1");
			language.addFIBQuestion(ques1);
			if (secondElement > firstElement) {

			}
			FillInBlanksQuestionModel ques2 = new FillInBlanksQuestionModel(
					"year");
			ques2.setPrompt("Should we create a new pile for the second element?(please answer yes or no)");
			if (secondElement > firstElement) {
				ques2.addAnswer("yes", 2, "Congratulations! You are right!");
				ques2.setGroupID("1");
			} else {
				ques2.addAnswer("no", 2, "Congratulations! You are right!");
				ques2.setGroupID("1");
			}

			language.addFIBQuestion(ques2);
			language.addQuestionGroup(qgm);
		}
		// ========================================

		for (int x : input) {
			sc.unhighlight(4);
			sc.unhighlight(5);
			sc.unhighlight(6);
			sc.unhighlight(8);
			sc.unhighlight(9);

			// highlight cell
			ia.highlightCell(iCounter, null, null);
			if (iCounter == 0) {
				sc.highlight(1);
			}
			language.nextStep();
			sc.unhighlight(1);

			Pile<Integer> newPile = new Pile<Integer>();
			newPile.push(x);
			int i = Collections.binarySearch(piles, newPile);
			if (i < 0) {
				i = ~i;
				counter.assignmentsInc(1);
				if (!set.contains(i)) {
					VisualStack vs = language.newConceptualStack(
							new Coordinates(550 + 40 * i, 100), null, "", null,
							stackProperties);
					vs.push(x);
					stacks.add(vs);
					
				}
				set.add(i);
			}
			if (i != piles.size()) {
				piles.get(i).push(x);
				sc.highlight(8);
				sc.highlight(9);

				// animal
				VisualStack vs = stacks.get(i);
				vs.push(x);

			} else {
				// create new pile
				piles.add(newPile);

				sc.highlight(4);
				sc.highlight(5);
				sc.highlight(6);

			}
			// MOVE marker
			language.nextStep();
			iCounter++;
			counter.assignmentsInc(1);
			counter.accessInc(1);
			iMarker.move(iCounter, null, defaultDuration);
			sc.unhighlight(8);
			sc.unhighlight(9);
			sc.unhighlight(4);
			sc.unhighlight(5);
			sc.unhighlight(6);
		}

		// next pop element from piles
		language.nextStep();

		ArrayProperties arrayProps2 = new ArrayProperties();
		arrayProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps2.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps2.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps2.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.BLACK);
		arrayProps2.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.RED);
		arrayProps2.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.YELLOW);

		// now, create the IntArray object, linked to the properties
		String[] result = new String[input.length];
		counter.assignmentsInc(1);
		StringArray ia2 = language.newStringArray(new Coordinates(500, 400),
				result, "result", null, arrayProps2);

		// Stack for output, only for test
		ArrayMarkerProperties arrayIMProps2 = new ArrayMarkerProperties();
		arrayIMProps2.set(AnimationPropertiesKeys.LABEL_PROPERTY, "");
		arrayIMProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		ArrayMarker iMarker2 = language.newArrayMarker(ia2, 0, ""
				+ pointerCounter, null, arrayIMProps2);

		// priority queue allows us to retrieve least pile efficiently
		PriorityQueue<Pile<Integer>> heap = new PriorityQueue<Pile<Integer>>(
				piles);
		counter.assignmentsInc(1);
		// used to save position
		Map<Integer, Integer> map = searchValue(heap, input);
		counter.assignmentsInc(1);

		// Text
		TextProperties textProps = new TextProperties();
		TextProperties textProps2 = new TextProperties();
		TextProperties textProps3 = new TextProperties();
		textProps3.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 20));
		textProps3.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		textProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

		Text text1 = language.newText(new Coordinates(500, 280),
				"After putting all the values in piles", "1", null, textProps);

		Text text2 = language.newText(new Coordinates(500, 300),
				"Priority queue allows us to retrieve least pile efficiently",
				"2", null, textProps);

		Text text3 = language.newText(new Coordinates(500, 320), "", "3", null,
				textProps2);
		
		Text text4 = language.newText(new Coordinates(60, 100), "", "4", null,
				textProps3);
		Text text5 = language.newText(new Coordinates(60, 130), "", "5", null,
				textProps3);
		Text text8 = language.newText(new Coordinates(60, 160), "", "8", null,
				textProps3);
		Text text9 = language.newText(new Coordinates(60, 190), "", "9", null,
				textProps3);
		
		Text text6 = language.newText(new Coordinates(500, 320), "", "6", null,
				textProps2);
		Text text7 = language.newText(new Coordinates(500, 340), "", "7", null,
				textProps2);
		
		//
		language.nextStep();

		for (int c = 0; c < input.length; c++) {
			Pile<Integer> smallPile = heap.poll();
			counter.assignmentsInc(1);
			input[c] = smallPile.pop();
			counter.assignmentsInc(1);
			// animal
			language.nextStep();
			// delete element from queue
			int position = map.get(input[c]);
			counter.assignmentsInc(1);
			text3.setText("",null,null);
			text6.setText("Compare the elements on the topmost position(1st element) of each pile:",  null, defaultDuration);
			text7.setText("We can find that:   "+input[c]+" is the smallest element.", null, defaultDuration);
			language.nextStep();
			text6.setText("",null,null);
			text7.setText("",null,null);
			text3.setText("So retrieve  " + input[c] + "  from pile" + " "
					+ (position + 1), null, defaultDuration);
			language.nextStep();

			stacks.get(position).pop();
			language.nextStep();
			//
			ia2.put(c, "" + input[c], null, defaultDuration);
			ia2.highlightCell(c, null, defaultDuration);
			iMarker2.move(c, null, defaultDuration);

			if (!smallPile.isEmpty())
				heap.offer(smallPile);
		}
		assert (heap.isEmpty());
		// hide
		language.nextStep();
		iMarker2.hide();
		// ia.hide();
		// ia2.hide();
		// iMarker2.hide();
		text1.hide();
		text2.hide();
		text3.hide();
		text6.hide();
		text7.hide();
		sc.hide();
		// Zusammenfassung
		text4.setText("Summary:", null, defaultDuration);
		text5.setText("The array on the bottom is your answer! We can see that there is no piles on above any more!",null, defaultDuration);
		text8.setText("The assignments are: " +counter.getAssigments(), null, defaultDuration);
		text9.setText("The accesses are: " +counter.getAccess(), null, defaultDuration);
	}

	/**
	 * Pile Class
	 * @author Yue Hu, Xinyu Liu
	 *
	 * @param <E>
	 */
	@SuppressWarnings("serial")
	private static class Pile<E extends Comparable<? super E>> extends Stack<E>
			implements Comparable<Pile<E>> {
		public int compareTo(Pile<E> y) {
			return peek().compareTo(y.peek());
		}
	}

	/**
	 * Get the position of Queue
	 * @param heap
	 * @param input
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<Integer, Integer> searchValue(
			PriorityQueue<Pile<Integer>> heap, int[] input) {

		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < input.length; i++) {
			int counter = 0;
			Iterator it = heap.iterator();
			while (it.hasNext()) {
				if (((List<Pile<Integer>>) it.next()).contains(input[i])) {
					map.put(input[i], counter);
				}
				counter++;
			}
		}
		return map;
	}

	// =====================================================================================

	public String getName() {
		return "Patience Sort";
	}

	public String getAlgorithmName() {
		return "Yue Hu, Xinyu Liu";
	}

	public String getAnimationAuthor() {
		return "Patience Sort";
	}

	public String getDescription() {
		return "In computer science, patience sorting is a sorting algorithm inspired by, and named after, the card game patience. A variant of the algorithm efficiently computes the length of a longest increasing subsequence in a given array.";
	}

	public String getCodeExample() {
		return "public class PatienceSort {"
				+ "\n"
				+ "    public static <E extends Comparable<? super E>> void sort (E[] n) {"
				+ "\n"
				+ "        List<Pile<E>> piles = new ArrayList<Pile<E>>();"
				+ "\n"
				+ "        // sort into piles"
				+ "\n"
				+ "        for (E x : n) {"
				+ "\n"
				+ "            Pile<E> newPile = new Pile<E>();"
				+ "\n"
				+ "            newPile.push(x);"
				+ "\n"
				+ "            int i = Collections.binarySearch(piles, newPile);"
				+ "\n"
				+ "            if (i < 0) i = ~i;"
				+ "\n"
				+ "            if (i != piles.size())"
				+ "\n"
				+ "                piles.get(i).push(x);"
				+ "\n"
				+ "            else"
				+ "\n"
				+ "                piles.add(newPile);"
				+ "\n"
				+ "        }"
				+ "\n"
				+ " "
				+ "\n"
				+ "        // priority queue allows us to retrieve least pile efficiently"
				+ "\n"
				+ "        PriorityQueue<Pile<E>> heap = new PriorityQueue<Pile<E>>(piles);"
				+ "\n"
				+ "        for (int c = 0; c < n.length; c++) {"
				+ "\n"
				+ "            Pile<E> smallPile = heap.poll();"
				+ "\n"
				+ "            n[c] = smallPile.pop();"
				+ "\n"
				+ "            if (!smallPile.isEmpty())"
				+ "\n"
				+ "                heap.offer(smallPile);"
				+ "\n"
				+ "        }"
				+ "\n"
				+ "        assert(heap.isEmpty());"
				+ "\n"
				+ "    }"
				+ "\n"
				+ " "
				+ "\n"
				+ "    private static class Pile<E extends Comparable<? super E>> extends Stack<E> implements Comparable<Pile<E>> {"
				+ "\n"
				+ "        public int compareTo(Pile<E> y) { return peek().compareTo(y.peek()); }"
				+ "\n" + "    }";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_BACKTRACKING);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

}