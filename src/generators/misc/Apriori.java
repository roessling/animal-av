/*
 * Apriori.java
 * Matthias Schultheis, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeSet;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;
import java.util.Set;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.apriori.AList;
import generators.misc.apriori.KVList;
import generators.network.helper.ClassName;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.Slide;
import algoanim.animalscript.addons.bbcode.NetworkStyle;
import algoanim.animalscript.addons.bbcode.Style;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * @author Matthias Schultheis
 * 
 *         generates an animation showing the functionality of the apriori
 *         algorithm
 */
public class Apriori implements Generator {

	public Language lang; // TODO private

	private Style style;

	// handles text in different languages
	private ResourceBundle bundle;
	private Locale locale;

	// animation size
	private static final int ANIM_WIDTH = 1024;
	private static final int ANIM_HEIGHT = 768;

	// resource settings
	private static final String BUNDLE_STR_FILE = "generators.misc.apriori.Strings";
	private String RESOURCES_PATH;

	// properties
	private TextProperties propHeader;
	private TextProperties propSubHeader;
	private TextProperties propRemark;
	private TextProperties propLabels;
	private TextProperties propText;
	private SourceCodeProperties propSourceCode;
	MatrixProperties matrixProps;

	private int[][] src;
	private List<List<Set<Integer>>> L;
	private int numItems;
	private String[] itemNames;
	private double minSupport = 0.5;

	private Text status;

	private Color clrHighlight;

	private final int precision = 1000;

	public Apriori(Locale l) {
		RESOURCES_PATH = ClassName.getPackageAsPath(this) + "apriori/";
		this.locale = l;
		
		try {
			bundle = ResourceBundle.getBundle(BUNDLE_STR_FILE, locale);
		} catch (MissingResourceException e) {
			lang.addError("Apriori: Ressource file not found");
		}
	}

	public Apriori() {
		this(new Locale("en"));
	}

	/**
	 * initializes the animation
	 */
	public void init() {
		lang = new AnimalScript("Apriori", "Matthias Schultheis", ANIM_WIDTH,
				ANIM_HEIGHT);
		lang.setStepMode(true);

		style = new NetworkStyle();
		initProperties();

	}

	private void initProperties() {

		// texts
		propText = new TextProperties();
		propText.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 16));

		propLabels = new TextProperties();
		propLabels.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 20));
	}

	/**
	 * starts the animation
	 */
	public void start() {
		L = new LinkedList<List<Set<Integer>>>();

		// draw header
		lang.newText(new Coordinates(20, 30), bundle.getString("header"),
				"header", null, propHeader);

		// draw rectangle around header
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
				new Offset(5, 5, "header", "SE"), "hRect", null, rectProps);

		lang.nextStep(bundle.getString("chapIntro"));

		// show introduction
		Text subtitle = lang.newText(new Offset(0, 20, "header",
				AnimalScript.DIRECTION_SW), bundle.getString("titleIntro"),
				"subtitle", null, propSubHeader);

		Slide titleSlide = null;
		try {
			titleSlide = new Slide(lang, RESOURCES_PATH
					+ bundle.getString("introTextFile"), "subtitle", style);
		} catch (java.lang.NullPointerException e) {
			lang.addError("introduction text file \""
					+ bundle.getString("introTextFile")
					+ "\" could not be found in " + RESOURCES_PATH);
		}
		
		// next step: hide Slide
		subtitle.hide();
		if (titleSlide != null)
			titleSlide.hide();

		// initalize primitives
		List<Set<Integer>> data = convertDataSet(src);
		int numTrans = data.size();

		// draw table with dataset
		String[][] dataSetTab = new String[numTrans + 1][numItems + 1];
		dataSetTab[0][0] = " ";
		for (int i = 0; i < numItems; i++)
			dataSetTab[0][i + 1] = itemNames[i];
		for (int t = 0; t < numTrans; t++)
			dataSetTab[t + 1][0] = "t" + t;
		for (int t = 0; t < numTrans; t++)
			for (int i = 0; i < numItems; i++)
				dataSetTab[t + 1][i + 1] = (src[t][i] > 0) ? "x" : " ";

		StringMatrix dataSet = lang.newStringMatrix(new Offset(30, 0,
				"subtitle", AnimalScript.DIRECTION_SW), dataSetTab, "dataset",
				null, matrixProps);

		// draw minsupport
		Text txtMinSupp = lang.newText(new Offset(0, 10, "dataset",
				AnimalScript.DIRECTION_SW), "min. support: "
				+ cutDouble(minSupport), "txtSupp", null, propLabels);

		// show source code
		SourceCode source = lang.newSourceCode(new Offset(30, 0, "dataset",
				AnimalScript.DIRECTION_NE), "sourceCode", null, propSourceCode);

		source.addCodeLine(bundle.getString("sc2"), null, 0, null);
		source.addCodeLine(bundle.getString("sc3"), null, 0, null);
		source.addCodeLine(bundle.getString("sc3_2"), null, 1, null);
		source.addCodeLine(bundle.getString("sc4"), null, 0, null);
		source.addCodeLine(bundle.getString("sc5"), null, 0, null);
		source.addCodeLine("", null, 0, null);

		source.addCodeLine(bundle.getString("sc6"), "for_k", 0, null);
		source.addCodeLine(bundle.getString("sc7"), "for_k_C_k", 1, null);
		source.addCodeLine(bundle.getString("sc8"), "for_k_for_is", 1, null);
		source.addCodeLine(bundle.getString("sc9"), "for_k_for_is_if", 2, null);
		source.addCodeLine(bundle.getString("sc10"), "for_k_for_is_if_then", 3,
				null);
		source.addCodeLine("", null, 0, null);

		source.addCodeLine(bundle.getString("sc11"), "init_supp", 1, null);
		source.addCodeLine(bundle.getString("sc12"), "for_t", 1, null);
		source.addCodeLine(bundle.getString("sc13"), "for_c", 2, null);
		source.addCodeLine(bundle.getString("sc14"), "if_ss", 3, null);
		source.addCodeLine(bundle.getString("sc15"), "inc_supp", 4, null);
		source.addCodeLine(bundle.getString("sc16"), "norm", 1, null);
		source.addCodeLine(bundle.getString("sc17"), "to_Lk", 1, null);
		source.addCodeLine("", null, 0, null);

		source.addCodeLine(bundle.getString("sc18"), "result", 0, null);

		// statusBox
		status = lang.newText(new Offset(20, 40, "sourceCode",
				AnimalScript.DIRECTION_SW), "", "status", null, propRemark);

		lang.nextStep();

		// first line
		source.highlight(0);

		int[] supports = new int[numItems]; // for each item a counter

		// draw support table

		// list for key-value (elem-support) Strings
		LinkedList<KVList<String, String>> lstSupp = new LinkedList<KVList<String, String>>();
		KVList<String, String> kvlst = new KVList<String, String>(lang,
				"lstSup1", new Offset(10, 0, "sourceCode",
						AnimalScript.DIRECTION_NE), propText, propLabels,
				clrHighlight);
		lstSupp.add(kvlst);

		// title
		kvlst.addEntry("C1", "Supports");

		// add entries to table
		for (int i = 0; i < numItems; i++) {
			kvlst.addEntry("{" + itemNames[i] + "}", "0");
		}

		lang.nextStep("k = 1");

		// compute L0

		source.toggleHighlight(0, 1);

		// for each transaction
		for (int t = 0; t < numTrans; t++) {
			setStatusText("stL0ForTrans", "t" + t);
			dataSet.highlightCellColumnRange(t + 1, 0, numItems, null, null);
			lang.nextStep();

			status.setText("", null, null);
			source.toggleHighlight(1, 2);

			for (int i : data.get(t)) {
				// ...increment support...
				supports[i]++;
			}

			// ...and update table
			for (int i = 0; i < numItems; i++) {
				kvlst.setValue(i + 1, "" + supports[i]);
			}

			lang.nextStep();

			dataSet.unhighlightCellColumnRange(t + 1, 0, numItems, null, null);
			source.toggleHighlight(2, 1);
		}

		setStatusText("stL0ForTransEnd");
		lang.nextStep();

		status.setText("", null, null);
		source.toggleHighlight(1, 3);

		// show normalized supports in table but keep unnormalized values in
		// supports[]
		for (int i = 0; i < numItems; i++) {
			kvlst.setValue(i + 1, "" + supports[i] + "/" + numTrans + " = "
					+ cutDouble((double) supports[i] / numTrans));
		}
		
		lang.nextStep();

		source.toggleHighlight(3, 4);

		// L1 := set of all items with supp > minsupp
		List<Set<Integer>> L1 = new LinkedList<Set<Integer>>();
		L.add(L1);

		// fill L1 with subsets with supp > minsupp
		for (int i = 0; i < numItems; i++) {
			if (supports[i] >= minSupport * numTrans) { // if supp > minsupp
				TreeSet<Integer> set = new TreeSet<Integer>();
				set.add(i);
				L1.add(set);
			} else { // otherwise grey in table
				kvlst.disableEntry(i + 1);
			}
		}

		LinkedList<Text> txtL = new LinkedList<Text>();
		txtL.add(lang.newText(kvlst.getBottom(), "L1 = " + lsiToString(L1),
				"txtL1", null, propText));

		lang.nextStep();

		source.unhighlight(4);
		source.highlight("for_k");
    

		// iterate L_k
		int k = 1;
		for (; !L.get(k - 1).isEmpty() && k < numItems; k++) {
			setStatusText("stForK", "" + (k + 1));
			lang.nextStep("k = " + (k + 1));

			status.setText("", null, null);
			source.toggleHighlight("for_k", "for_k_C_k");

			// Apriori Gen

			Set<Set<Integer>> C_k = new LinkedHashSet<Set<Integer>>();

			List<Set<Integer>> L_old = L.get(k - 1);

			// iterate over all subsets in L_k-1
			for (int a = 0; a < L_old.size(); a++) {
				// iterate over all other subsets not yet pairwise treated
				for (int b = a + 1; b < L_old.size(); b++) {
					// build new set based on the two others
					Set<Integer> newSet = new TreeSet<Integer>(L_old.get(a));
					newSet.addAll(L_old.get(b));

					// take it if the two origin sets have all but one elem in
					// common
					if (newSet.size() == k + 1) {
						C_k.add(newSet);
					}
				}
			}

			List<Set<Integer>> lstC_k = new LinkedList<Set<Integer>>(C_k);
			Set<Set<Integer>> C_kPruned = new LinkedHashSet<Set<Integer>>(C_k);

			// display C_k
			AList<String> alstCk = new AList<String>(lang, "lstCk" + (k + 1),
					new Offset(0, 30, "txtL" + k, AnimalScript.DIRECTION_SW),
					propText, propLabels, clrHighlight);

			// title
			alstCk.addEntry("C_" + (k + 1));
			// add entries to table
			for (Set<Integer> c : lstC_k) {
				alstCk.addEntry(siToString(c));
			}

			if (lstC_k.isEmpty()) {
				alstCk.addEntry(getReplacedBundleText("emptyList"));
				setStatusText("stForKCkEmpty", "" + (k + 1));
			}
			
			lang.nextStep();

			source.toggleHighlight("for_k_C_k", "for_k_for_is");

			// Pruning
			int pos = 1;
			for (Set<Integer> cand : lstC_k) {
				setStatusText("stForK1ItemSets", siToString(cand));
				alstCk.highlight(pos);
				lang.nextStep();

				source.toggleHighlight("for_k_for_is", "for_k_for_is_if");
				boolean toRemove = false;
				Set<Integer> counterexample = null;
				// create all subsets
				for (int itemToRemove : cand) {
					Set<Integer> subset = new TreeSet<Integer>(cand);
					subset.remove(itemToRemove);
					if (!L_old.contains(subset)) {
						toRemove = true;
						counterexample = subset;
						break;
					}
				}
				
				// show removing
				if (toRemove) {
					setStatusText("stExistsSubset", siToString(counterexample),
							"L" + (k + 1));
					lang.nextStep();

					status.setText("", null, null);

					// remove candidate
					source.toggleHighlight("for_k_for_is_if",
							"for_k_for_is_if_then");
					C_kPruned.remove(cand);
					alstCk.disableEntry(pos);

					lang.nextStep();

					source.toggleHighlight("for_k_for_is_if_then",
							"for_k_for_is");
				} else {
					setStatusText("stExistsNoSubset", "" + (k-1), siToString(cand),
							"L" + (k + 1));
					lang.nextStep();

					status.setText("", null, null);
					alstCk.unhighlight(pos);
					source.toggleHighlight("for_k_for_is_if", "for_k_for_is");
				}
				
				pos++;
			}
			
			setStatusText("stForK1ItemSetsEnd", "C" + (k + 1));
			lang.nextStep();

			// hide list
			alstCk.hide();

			source.toggleHighlight("for_k_for_is", "init_supp");
			setStatusText("stEmpty");

			// 2nd part of for
			List<Set<Integer>> L_k = new LinkedList<Set<Integer>>();

			// for each candidate store support in HashMap
			HashMap<Set<Integer>, Integer> cands = new HashMap<Set<Integer>, Integer>();

			// draw support table

			// list for key-value (elem-support) Strings
			kvlst = new KVList<String, String>(lang, "lstSup" + (k + 1),
					new Offset(0, 30, "txtL" + k, AnimalScript.DIRECTION_SW),
					propText, propLabels, clrHighlight);
			lstSupp.add(kvlst);

			// title
			kvlst.addEntry("C" + (k + 1), "Supports");

			// add entries to table and HashMap
			for (Set<Integer> c : C_kPruned) {
				cands.put(c, 0);
				kvlst.addEntry(siToString(c), "0");
			}

			if (C_kPruned.isEmpty()) {
				setStatusText("stForKCkEmpty", "" + (k + 1));
				kvlst.addEntry(getReplacedBundleText("emptyList"), "-1");
			}

			lang.nextStep();

			source.toggleHighlight("init_supp", "for_t");
			// for each transaction
			for (int t = 0; t < numTrans; t++) {
				setStatusText("stForK2ForT", "t" + t);
				dataSet.highlightCellColumnRange(t + 1, 0, numItems, null, null);
				lang.nextStep();

				source.toggleHighlight("for_t", "for_c");

				// List<Set<Integer>> candsOfTrans = new
				// LinkedList<Set<Integer>>();
				int count = 1;
				for (Set<Integer> c : C_kPruned) {
					setStatusText("stForK2ForC", siToString(c));
					lstSupp.get(k).highlight(count);

					lang.nextStep();

					source.toggleHighlight("for_c", "if_ss");

					if (data.get(t).containsAll(c)) {
						// status.setText(siToString(c)+" is subset of "+siToString(data.get(t))+".",
						// null, null);
						setStatusText("stForKForTForCIsSubset", siToString(c),
								siToString(data.get(t)));
						lang.nextStep();

						setStatusText("stEmpty");
						source.toggleHighlight("if_ss", "inc_supp");

						// increment support
						int newSupport = cands.get(c) + 1;
						cands.put(c, newSupport);
						kvlst.setValue(count, "" + newSupport);
						lang.nextStep();

						source.toggleHighlight("inc_supp", "for_c");
					} else {
						setStatusText("stForKForTForCIsNotSubset",
								siToString(c), siToString(data.get(t)));
						lang.nextStep();

						source.toggleHighlight("if_ss", "for_c");
					}

					lstSupp.get(k).unhighlight(count);
					count++;
				}
				setStatusText("stForK2ForCEnd", "" + (k + 1));
				lang.nextStep();

				source.toggleHighlight("for_c", "for_t");
				dataSet.unhighlightCellColumnRange(t + 1, 0, numItems, null,
						null);
			}
			
			setStatusText("stForK2ForTEnd");
			lang.nextStep();

      
			// normalize supports
			int count = 1;
			for (Set<Integer> c : C_kPruned) {
				kvlst.setValue(count, cands.get(c) + "/" + numTrans + " = "
						+ cutDouble((double) cands.get(c) / numTrans));
				count++;
			}
      
      if(count != 1)
        setStatusText("stEmpty");
      else
        setStatusText("stNoSuppNorm");


			source.toggleHighlight("for_t", "norm");
			lang.nextStep();
      
      setStatusText("stEmpty");

			// take only candidates with minsupport
      count = 1;
			for (Set<Integer> c : C_kPruned) {
				int support = cands.get(c);

				if (support >= minSupport * numTrans)
					L_k.add(c);
        else
          kvlst.disableEntry(count);
          
        count++;
			}
      
      
			txtL.add(lang.newText(kvlst.getBottom(), "L" + (k + 1) + " = "
					+ lsiToString(L_k), "txtL" + (k + 1), null, propText));

			source.toggleHighlight("norm", "to_Lk");
			lang.nextStep();

			source.toggleHighlight("to_Lk", "for_k");

			L.add(L_k);
		}
		
		if (k >= numItems)
			setStatusText("stForKEndK", "" + (k + 1));
		else
			setStatusText("stForKEndLEmpty", "" + k);
		lang.nextStep();

		source.toggleHighlight("for_k", "result");
    

		setStatusText("stEmpty");

		List<Set<Integer>> result = new ArrayList<Set<Integer>>();
		for (List<Set<Integer>> s : L) {
			result.addAll(s);
		}

		status.setText("return " + lsiToString(result), null, null);

		lang.nextStep("Result");

		// hide all objects
		dataSet.hide();
		source.hide();
		status.hide();
		txtMinSupp.hide();
		for (KVList<String, String> lst : lstSupp) {
			lst.hide();
		}
		for (Text txt : txtL) {
			txt.hide();
		}

		String conclusionText = "We have finished now. There were " + (k - 1)
				+ " iterations necessary.";
		String conclusionText1 = "The set "+lsiToString(result)+" contains all itemsets that appear sufficiently often.";
		String conclusionText2 = "The complexity is O( #items * #transactions * (#items!) )";

		// show conclusion
		subtitle.show();
		subtitle.setText(bundle.getString("titleConclusion"), null, null);
		lang.newText(new Offset(0, 20, "subtitle", AnimalScript.DIRECTION_SW),
				conclusionText, "conclusion", null, propText);
		lang.newText(
				new Offset(0, 20, "conclusion", AnimalScript.DIRECTION_SW),
				conclusionText1, "conclusion1", null, propText);
		lang.newText(
				new Offset(0, 20, "conclusion1", AnimalScript.DIRECTION_SW),
				conclusionText2, "conclusion2", null, propText);

		// the end
		lang.nextStep();
	}

	public static List<Set<Integer>> convertDataSet(int[][] data) {
		List<Set<Integer>> result = new ArrayList<Set<Integer>>(data.length);
		for (int t = 0; t < data.length; t++) {
			Set<Integer> trans = new LinkedHashSet<Integer>();
			for (int i = 0; i < data[t].length; i++) {
				if (data[t][i] > 0)
					trans.add(i);
			}
			result.add(trans);
		}

		return result;

	}

	private double cutDouble(double num, int precision) {
		return (double) ((int) (num * precision)) / precision;
	}

	private double cutDouble(double num) {
		return cutDouble(num, precision);
	}

	private String lsiToString(List<Set<Integer>> input) {
		if (input.isEmpty())
			return "{}";

		String result = "{ ";
		for (Set<Integer> set : input) {
			result += siToString(set) + ", ";
		}
		result = result.substring(0, result.length() - 2) + " }";

		return result;
	}

	private String siToString(Set<Integer> input) {
		if (input.isEmpty())
			return "{}";

		String result = "{";
		for (int i : input) {
			result = result + itemNames[i] + ", ";
		}

		result = result.substring(0, result.length() - 2) + "}";

		return result;
	}

	/**
	 * @param ident
	 *            the identifier for the bundle
	 * @param vars
	 *            the values for replacement
	 * @return the value of ident in the bundle where every occurence of "$i" is
	 *         replaced
	 */
	private String getReplacedBundleText(String ident, String... vars) {
		String text = bundle.getString(ident);
		for (int i = 0; i < vars.length; i++) {
			text = text.replaceAll("(?<!\\\\)\\$" + i, vars[i]);
		}

		return text;
	}

	/**
	 * sets the status in the status text field
	 * 
	 * @param ident
	 *            the identifier for the bundle
	 * @param vars
	 *            the values for replacement
	 */
	private void setStatusText(String ident, String... vars) {

		status.setText(getReplacedBundleText(ident, vars), null, null);
	}

	public String generate(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) {

		boolean error = false;

		// get primitives and properties
		propSourceCode = (SourceCodeProperties) props
				.getPropertiesByName("sourceCodeProp");
		matrixProps = (MatrixProperties) props
				.getPropertiesByName("transactonItemMatrixProp");
		src = (int[][]) primitives.get("transactionItemMatrix");

		if (src.length == 0 || src[0].length == 0) {
			error = true;
			lang.addError("transactionMatrix may not be empty!");
		} else {
			numItems = src[0].length;
			itemNames = new String[numItems];
			for (int i = 0; i < numItems; i++) {
				itemNames[i] = "i" + i;
			}
		}

		clrHighlight = (Color) primitives.get("supportHighlightColor");
		propHeader = (TextProperties) props.getPropertiesByName("headerProp");
		propSubHeader = (TextProperties) props
				.getPropertiesByName("subHeaderProp");
		propRemark = (TextProperties) props.getPropertiesByName("commentProp");

		// set properties which can not be set in genertor
		propHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 24));

		propSubHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 20));

		propLabels.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 20));

		// fix matrix bug by fixing size of celles
		matrixProps.set(AnimationPropertiesKeys.CELL_HEIGHT_PROPERTY, 20);
		matrixProps.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, 50);

		// texts
		propText = new TextProperties();
		propText.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 16));

		minSupport = (double) primitives.get("minSupport");
		if (minSupport < 0 || minSupport > 1) {
			lang.addError("minSupport must be between 0 and 1!");
			error = true;
		}

		// start algo
		if (!error)
			start();

		return lang.toString();
	}

	public String getName() {
		return "Apriori (Häufige Vorkommen)";
	}

	public String getAlgorithmName() {
		return "Apriori";
	}

	public String getAnimationAuthor() {
		return "Matthias Schultheis";
	}

	public String getDescription() {
		return "Apriori is an algorithm for frequent item set mining and association rule learning over transactional databases."
				+ "\n"
				+ "\n"
				+ "It proceeds by identifying the frequent individual items in the database and extending them to larger and larger item sets as long as those item sets appear sufficiently often in the database."
				+ "\n"
				+ "The frequent item sets determined by Apriori can be used to determine association rules which highlight general trends in the database. The latter part is not included in this visualization."
				+ "\n"
				+ "\n"
				+ "Apriori has applications in domains such as market basket analysis.";
	}

	public String getCodeExample() {
		return "initialize support for each item to be 0"
				+ "\n"
				+ "for each transaction"
				+ "\n"
				+ "	increment support of items in transaction"
				+ "\n"
				+ "	normalize supports by dividing by number of items"
				+ "\n"
				+ "	L_1 = {items | support > minSupport}"
				+ "\n"
				+ "\n"
				+ "for k = 2..#items as long as L_(k-1) is not empty"
				+ "\n"
				+ "	C_k = {a united b | a, b of L_(k-1) and a,b differ in only one elem}"
				+ "\n"
				+ "	for all itemsets c in C_k "
				+ "\n"
				+ "		if there exists a (k-1)-subset of c which is not in L_(k-1)"
				+ "\n" + "			delete c from C_k" + "\n" + "\n"
				+ "	initialize support for each c from C_k to be 0" + "\n"
				+ "	for each transaction t" + "\n"
				+ "		for each candidate c from C_k" + "\n"
				+ "			if c is subset of tail" + "\n"
				+ "				increment c.support" + "\n" + "\n"
				+ "	normalize supports by dividing by number of items" + "\n"
				+ "	L_k = {c from C_k  | c.support > minSupport}" + "\n" + "\n"
				+ "return union(L_k)";
	}

	public String getFileExtension() {
		return "asu";
	}

	public Locale getContentLocale() {
		return Locale.ENGLISH;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
	}

	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}
}