/*
 * chiMergeGenerator2.java
 * Moritz Lode, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.OffsetFromLastPosition;
import animal.main.Animal;



class EntropySplitAPI {

	private Language lang;
	
	private SourceCodeProperties sourceCodeHighlightColor;
	
	private TextProperties textHighlightColorPositive;
	private TextProperties textHighlightColorNegative;
	  

	  public EntropySplitAPI(Language l, SourceCodeProperties sourceCodeHighlightColor, TextProperties textHighlightColorPositive, TextProperties textHighlightColorNegative) {
	    // Store the language object
	    this.lang = l;
	    
	    this.sourceCodeHighlightColor = sourceCodeHighlightColor;
	    this.textHighlightColorPositive = textHighlightColorPositive;
	    this.textHighlightColorNegative = textHighlightColorNegative;
	    
	    // This initializes the step mode. Each pair of subsequent steps has to be divided by a call of lang.nextStep();
	    lang.setStepMode(true);
	  }

	  
	  private static final String INTRO_DESCRIPTION = "Many classification algorithms require that the training data contains only discrete attributes."
		 +"\n"
		 +"To use such an algorithm when there are numeric attributes, all numeric values must first be converted into discrete values, a process called discretization."
		 +"\n"
		 +"\n"
		 +"This animation describes Entropy-Split, a general, robust algorithm that uses the Entropy or expected information to discretize (quantize) numeric attributes."
		 +"\n"
		 +"Entropy-Split is a supervised discretization method, which also considers the relation between feature values and class values."
		 +"\n"
		 +"Feature values are thereby joined into initially one interval and in a top-down approach successively split until a given threshold of number of intervals, or minimum number values is reached."
		 +"\n";


	  private static final String OUTRO_DESCRIPTION = "We showed how a given table was discretized using entropy statistics in a top-down splitting process.\n"
		      + "Resulting in a discretization in form of intervals for the feature value F and its class value K.\n"
		      +"\n"
		      +"We used Entropy-Split with the stopping criteria number-of-intervals and interval-size. For further information on this matter see:\n"
		      +"Fayyad, U.M. and Irani, K.B. (1993) Multi-Interval Discretization of Continuous-Valued Attributes for Classification Learning.IJCAI-93";

	  
	  private static final String PSEUDO_CODE     = "1. compute weighted entropy (WE) values for each interval split point (SP)"
				 +"\n"
				 +"2. for all intervals while (number of intervals < I and size of intervals > T):"
				 +"\n"
				 +"       3. split interval at the split point with lowest weighted entropy"
				 +"\n"
				 +"4. else STOP";                                                                                            // 3

	  
	  private static final String INIT_PSEUDO_CODE     = "1. sort examples according to feature value F"
				 +"\n"
				 +"2. construct one interval containing all feature values"
				 +"\n"
				 +"3. initial list of possible split points for this interval";                                                           // 1
	  
	  
	  private static final String MATH_FORMULAS     = "Split-Point = arg min(T) ( |S_A<T|/|S| * Entropy(S_A<T) + |S_A>=T|/|S| * Entropy(S_A>=T) )\n"             
		      + "Entropy(S) = -  SUM(p_i * log_2 p_i) | i=1 to i=|K|\n"                                                         
			  + "\n"                                                         
			  + "p_i = probability of class i in the interval S\n"                                                          
			  + "|K| = number of classes\n";                                                          


	  /**
	   * ChiMerge: Visualizes a discretization of feature values into intervals using the class values
	   * 
	   * 
	   * @param arrayF
	   *          the IntArray containing the feature values
	   * @param arrayK
	   *          the IntArray containing the corresponding class values for feature values
	   * @param threshold
	   *          float value for the merging threshold, taken out of a chiSquare table
	 * @param minIntervalSize 
	   *          
	   * @exception IllegalArgumentException if arguments arrayF and arrayK do not have the same length.
	   */
	  protected void discretize(int[] arrayF, int[] arrayK, int maxNumberOfIntervals, int minIntervalSize) throws IllegalArgumentException {
		  
		  if (arrayF.length != arrayK.length) {
			  throw new IllegalArgumentException("The arrayF and arrayK must be the same length!");
		  }
		  
		  // init
		  
		  // sort the arrays
		  Map<Integer, Integer> valueMap = new TreeMap<Integer, Integer>();
		  for(int i=0;i<arrayF.length;i++){
			  valueMap.put(arrayF[i], arrayK[i]);
		  }
		  
		  Collection<Integer> classes_values = valueMap.values();
		  Set<Integer> feature_keys = valueMap.keySet();

		  Integer[] classArray = classes_values.toArray(new Integer[classes_values.size()]);
		  Integer[] featureArray = feature_keys.toArray(new Integer[feature_keys.size()]);
		  
		  List<Integer> unique_classes = new ArrayList<Integer>(new HashSet<Integer>(classes_values));
		  
		  
		    
			Variables vl = lang.newVariables();
			
			vl.declare("int", "numberOfClasses");
			vl.set("numberOfClasses", String.valueOf(unique_classes.size()));
			
			vl.declare("double", "maxNumberOfIntervals");
			vl.set("maxNumberOfIntervals", String.valueOf(maxNumberOfIntervals));
		  
			vl.declare("double", "minIntervalSize");
			vl.set("minIntervalSize", String.valueOf(minIntervalSize));
		  
		  // header
		  TextProperties txtHeaderProps = new TextProperties();
		  txtHeaderProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		  txtHeaderProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

		  Text header = lang.newText(new Coordinates(20, 30), "Entropy-Split", "header", null, txtHeaderProps);

		  
		  RectProperties rectProps = new RectProperties();
		  rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		  rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		  rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);

		  Rect hRect = lang.newRect(new Offset(-5, -5, header, AnimalScript.DIRECTION_NW), new Offset(5, 5, header, AnimalScript.DIRECTION_SE), "hRect", null, rectProps);
		  
		  // end header
		  
		  
		  // first intro text
		  TextProperties txtProps = new TextProperties();
		  txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 16));
		  txtProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		  		  
		  String[] intros = EntropySplitAPI.INTRO_DESCRIPTION.split("\n");
		 
		  Text intro0 = lang.newText(new Coordinates(20, 120), intros[0], "intro0", null, txtProps);
		  Text intro1 = lang.newText(new Coordinates(20, 140), intros[1], "intro1", null, txtProps);
		  Text intro2 = lang.newText(new Coordinates(20, 180), intros[2], "intro2", null, txtProps);
		  Text intro3 = lang.newText(new Coordinates(20, 200), intros[3], "intro3", null, txtProps);
		  Text intro4 = lang.newText(new Coordinates(20, 220), intros[4], "intro4", null, txtProps);
		  Text intro5 = lang.newText(new Coordinates(20, 260), intros[5], "intro5", null, txtProps);

		  // end intro text
		  
		  
		  lang.nextStep("Introduction");

		 
		  intro0.hide();
		  intro1.hide();
		  intro2.hide();
		  intro3.hide();
		  intro4.hide();
		  intro5.hide();

		  
		  TextProperties txtElemProps = new TextProperties();
		  txtElemProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
		  txtElemProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		  
		  Text lblMaxNumberOfIntervals = lang.newText(new Offset(80, -5, header, AnimalScript.DIRECTION_NE), "MaxNumberOfIntervals I=" + maxNumberOfIntervals, "maxNumberOfIntervals", null, txtElemProps);
		  Text lblMinIntervalSize = lang.newText(new Offset(80, 15, header, AnimalScript.DIRECTION_NE), "MinIntervalSize T=" + minIntervalSize, "minIntervalSize", null, txtElemProps);

		  
		  MatrixProperties gridProps = new MatrixProperties();
		  gridProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.lightGray);
		  gridProps.set(AnimationPropertiesKeys.GRID_HIGHLIGHT_BORDER_COLOR_PROPERTY, Color.black);

		  gridProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		  gridProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 12));
		  gridProps.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");

		  
		  
		  // create table header which is fixed
		  String[][] tableHeaderArray = {{"i"}, {"F"}, {"K"}, {"{}"}, {"SP"}, {"WE"}};
		  
		  StringMatrix tableHead = lang.newStringMatrix(new Offset(0, 50, hRect, AnimalScript.DIRECTION_SW), tableHeaderArray, "tableHead", null, gridProps);


		  // now create all interval tables for the given values dynamically
		  
		  List<StringMatrix> valueTables = new ArrayList<StringMatrix>();
			
		  
		  String[] intv_values = new String[featureArray.length];
		  String[] intv_classes = new String[featureArray.length];
		  String[] intv_indize = new String[featureArray.length];
		  String[] intv_intervals = new String[featureArray.length];

		  String[] intv_splitpoints = new String[featureArray.length];
		  String[] intv_entropy = new String[featureArray.length];
		  
		  
		  float intv_end = (float) 0.0;

			for (int i=0; i < featureArray.length; i++) {
				intv_indize[i] = String.valueOf(i);
				
				  if (i + 1 < featureArray.length) {
					  intv_end = (float) (featureArray[i] + ((featureArray[i+1] - featureArray[i]) / 2.0)) ;
					  intv_splitpoints[i] = String.format(java.util.Locale.ROOT, "%.2f", intv_end );
				  } else {
					  intv_splitpoints[i] = "";
				  }
				
				intv_values[i] = String.valueOf(featureArray[i]);
				intv_classes[i] = String.valueOf(classArray[i]);
				intv_entropy[i] = "";

				if (i == featureArray.length/2) {
					intv_intervals[i] = "{" + (Collections.min(Arrays.asList(featureArray)) - 1) + "," + (Collections.max(Arrays.asList(featureArray)) + 1) + "}";
				} else {
					intv_intervals[i] = "";
				}
				
			}
		  		  
			String[][] tableArray = {intv_indize, intv_values, intv_classes, intv_intervals, intv_splitpoints, intv_entropy};
			  
			valueTables.add(lang.newStringMatrix(new Offset(10, 0, tableHead, AnimalScript.DIRECTION_NE), tableArray, "table0", null, gridProps));

		  
		  
		  // intro source code
		  
		// first, set the visual properties for the source code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		
		Font font = (Font) this.sourceCodeHighlightColor.get("font");

		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font.getName(), Font.PLAIN, 16));


	    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, this.sourceCodeHighlightColor.get("highlightColor"));
	    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, this.sourceCodeHighlightColor.get("color"));

	    // now, create the source code entity
	    SourceCode initSc = lang.newSourceCode(new Offset(0, 60, tableHead, AnimalScript.DIRECTION_SW), "initCode", null, scProps);

	    String[] initScs = EntropySplitAPI.INIT_PSEUDO_CODE.split("\n");
	    
		for (int i = 0; i < initScs.length; i++) {	
		    initSc.addCodeLine(initScs[i], null, 0, null);
		}

	    initSc.highlight(0);
	    
	    tableHead.highlightCell(1, 0, null, null);
	    
	    
	    lang.nextStep("Sorting data");
	    
	    
	    initSc.unhighlight(0);
	    initSc.highlight(1);

	    tableHead.unhighlightCell(1, 0, null, null);
	    tableHead.highlightCell(3, 0, null, null);

	    
	    lang.nextStep("Creating initial intervals");
	    
	    
	    initSc.unhighlight(1);
	    initSc.highlight(2);

	    tableHead.unhighlightCell(3, 0, null, null);
	    tableHead.highlightCell(4, 0, null, null);

	    
	    lang.nextStep("Initial split point list");

	    tableHead.unhighlightCell(4, 0, null, null);
	    initSc.hide();

	    SourceCode loopSc = lang.newSourceCode(new Offset(0, 60, tableHead, AnimalScript.DIRECTION_SW), "loopCode", null, scProps);

	    String[] loopScs = EntropySplitAPI.PSEUDO_CODE.split("\n");
	    
		for (int i = 0; i < loopScs.length; i++) {
			int intend = 0;
			if (i==2) intend=1;
			
			loopSc.addCodeLine(loopScs[i], null, intend, null);
		}
		
		loopSc.highlight(0);
	    tableHead.highlightCell(5, 0, null, null);

		
		SourceCodeProperties formulaProps = new SourceCodeProperties();
		formulaProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		formulaProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 16));
	    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, this.textHighlightColorNegative.get("color"));
		formulaProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.gray);
	    

	    // now, create the source code entity
	    SourceCode mathFormula = lang.newSourceCode(new Offset(100, -10, loopSc, AnimalScript.DIRECTION_NE), "chiMath", null, formulaProps);

	    String[] mathFormulas = EntropySplitAPI.MATH_FORMULAS.split("\n");
	    
		for (int i = 0; i < mathFormulas.length; i++) {	
			mathFormula.addCodeLine(mathFormulas[i], null, 0, null);
		}

		int numberOfIntervals = 0;
		vl.declare("int", "numberOfIntervals");
		vl.set("numberOfIntervals", String.valueOf(numberOfIntervals));
		//int intervalSize = 0;
		
		Text lblNumberOfIntervals = lang.newText(new Offset(80, 45, header, AnimalScript.DIRECTION_NE), "CurrentNumberOfIntervals =" + numberOfIntervals, "numberOfIntervals", null, txtElemProps);
		//Text lblIntervalSize = lang.newText(new Offset(80, 55, header, AnimalScript.DIRECTION_NE), "IntervalSize =" + intervalSize, "intervalSize", null, txtElemProps);
		

		// LOOP
		
		boolean stop = false;
		int iterationCounter = 0;
		
		while (!stop) {
			
			// TODO calc entropy values, update them in existing table
			// highlight best PER interval, initial only one
			// split intervals and create new tables

			
			// calcs and shows the entropy values in all tables in valueTables
			calcEntropyValues(valueTables);
			

		    lang.nextStep("Iteration " + iterationCounter);
		    iterationCounter++;
		    
		    
		    mathFormula.hide();
		    
			loopSc.unhighlight(0);
		    tableHead.unhighlightCell(5, 0, null, null);

		    loopSc.highlight(1);
		    
		    numberOfIntervals = valueTables.size();
			vl.set("numberOfIntervals", String.valueOf(numberOfIntervals));
			
		    // show number of intervals and min size of interval as variable somewhere, highlight them
		    lblNumberOfIntervals.setText("CurrentNumberOfIntervals = " + numberOfIntervals, null, null);
		    

		    if (numberOfIntervals < maxNumberOfIntervals) {
			    lblNumberOfIntervals.changeColor(AnimalScript.COLORCHANGE_COLOR, (Color) this.textHighlightColorPositive.get("color"), null, null);
		    } else {
			    lblNumberOfIntervals.changeColor(AnimalScript.COLORCHANGE_COLOR, (Color) this.textHighlightColorNegative.get("color"), null, null);
		    }
		    
			boolean valid = numberOfIntervals < maxNumberOfIntervals; // if both true then proceed else stop

			
		    lang.nextStep();
		    
		    
		    if (valid) {
			    
				loopSc.unhighlight(1);
				loopSc.highlight(2);
				
				List<Float> splitPoints = null;
				

				// highlight the min entropy and splitpoint for all tables
				splitPoints = highlightEntropySplitPoints(valueTables, minIntervalSize);

				
				if (Collections.frequency(splitPoints, Float.NaN) == splitPoints.size()) {
					loopSc.unhighlight(2);
					loopSc.highlight(3);
			    	
					stop = true;
					
				    lang.nextStep("Iteration end");
				    continue;
				}
				
			    lang.nextStep();
			    
				
				unHighlightEntropySplitPoints(valueTables, minIntervalSize);

			    // split table with selected min entropy/splitpoint
				valueTables = splitTables(valueTables, splitPoints, gridProps, tableHead, minIntervalSize);
				
			    numberOfIntervals = valueTables.size();
			    
			    // show number of intervals and min size of interval as variable somewhere, highlight them
			    lblNumberOfIntervals.setText("CurrentNumberOfIntervals = " + numberOfIntervals, null, null);
				
			    lang.nextStep();
				
			    
				loopSc.unhighlight(2);
				loopSc.highlight(0);
				
			    mathFormula.show();
				
		    } else {
		    	
				loopSc.unhighlight(1);
				loopSc.highlight(3);
		    	
				stop = true;
				
			    lang.nextStep("Iteration end");
		    }
		}
		
		
		List<Primitive> prims = new ArrayList<Primitive>(Arrays.asList(new Primitive[]{header, hRect, tableHead}));
		prims.addAll(valueTables);
		
		lang.hideAllPrimitivesExcept(prims);
		
		
		// show text outro
		  String[] outros = EntropySplitAPI.OUTRO_DESCRIPTION.split("\n");
			 
		  Text outro0 = lang.newText(new Coordinates(20, 340), outros[0], "outro0", null, txtProps);
		  Text outro1 = lang.newText(new Coordinates(20, 360), outros[1], "outro1", null, txtProps);
		  Text outro2 = lang.newText(new Coordinates(20, 380), outros[2], "outro2", null, txtProps);
		  Text outro3 = lang.newText(new Coordinates(20, 400), outros[3], "outro3", null, txtProps);
		  Text outro4 = lang.newText(new Coordinates(20, 420), outros[4], "outro4", null, txtProps);

	  }




	private Integer minIntervalSize(List<StringMatrix> valueTables) {

		  List<Integer> sizes = new ArrayList<Integer>();
		  
		  for (int i = 0; i < valueTables.size(); i++) {
				sizes.add(valueTables.get(i).getNrCols());
		  }
		  
		  return Collections.min(sizes);
	  }
	  
	  
	  private void calcEntropyValues(List<StringMatrix> valueTables) {

		  for (int i = 0; i < valueTables.size(); i++) {
			  
				StringMatrix table = valueTables.get(i);
				
				List<Integer> values = new ArrayList<Integer>();
				List<Integer> classes = new ArrayList<Integer>();
				List<Float> splitPoints = new ArrayList<Float>();

				for (int z = 0; z < table.getNrCols(); z++) {

					Integer f = Integer.valueOf(table.getElement(1, z));
					Integer k = Integer.valueOf(table.getElement(2, z));
					
					try {
						Float sp = Float.valueOf(table.getElement(4, z));
						splitPoints.add(sp);
					} catch (NumberFormatException e) {
						// last col of these rows have no value, empty
					}

					values.add(f);
					classes.add(k);
				}
							
				List<Float> entropies = new ArrayList<Float>();

				
				// calc entropy value for every split point
				for (int y = 0; y < splitPoints.size(); y++) {

					// split values and classes lists into smaller and greater-equal splitpoint
					// calc for both entropy value
					// weighted entropy value is sum of both
					
					List<Integer> valuesS = new ArrayList<Integer>();
					List<Integer> classesS = new ArrayList<Integer>();
					
					List<Integer> valuesA = new ArrayList<Integer>();
					List<Integer> classesA = new ArrayList<Integer>();
					
					for (int j = 0; j < values.size(); j++) {
						
						if (values.get(j) < splitPoints.get(y)) {
							valuesS.add(values.get(j));
							classesS.add(classes.get(j));
						} else {
							valuesA.add(values.get(j));
							classesA.add(classes.get(j));
						}
					}
					
					float entropyS = ((float) valuesS.size() / (float) values.size()) * entropy(valuesS, classesS);
					float entropyA = ((float) valuesA.size() / (float) values.size()) * entropy(valuesA, classesA);

					entropies.add(entropyS + entropyA);
				}
				
				// show the entropy values in the table
				for (int j = 0; j < entropies.size(); j++) {
					table.put(5, j, String.format(java.util.Locale.ROOT, "%.2f", entropies.get(j)), null, null);
				}
		  }
	  }
	  
	
	private float entropy(List<Integer> values, List<Integer> classes) {
		
		List<Integer> unique_classes = new ArrayList<Integer>(new HashSet<Integer>(classes));
		
		//System.out.println(unique_classes.size());
		
		float entropy = (float) 0.0;
		
		for (int i=0; i < unique_classes.size(); i++) {
			//System.out.println(unique_classes.get(i) + ", " + Collections.frequency(classes, unique_classes.get(i)));
			
			float pi = (float) Collections.frequency(classes, unique_classes.get(i)) / (float) classes.size();
			
			//System.out.println(pi);
			
			entropy += pi * (Math.log(pi)/Math.log(2));
		}
		
		//System.out.println(-entropy);
		//System.out.println("");
		
		if (entropy == 0.0) {
			return entropy;
		} else {
			return -entropy;
		}
	}

	 
	  private List<Float> highlightEntropySplitPoints(List<StringMatrix> valueTables, int minIntervalSize) {

		  List<Float> tableSplitPoints = new ArrayList<>();
		  
		  for (int i = 0; i < valueTables.size(); i++) {
			  
				StringMatrix table = valueTables.get(i);

				if (table.getNrCols() <= minIntervalSize) {
					
					tableSplitPoints.add(Float.NaN);
					continue;
				}
				
				List<Integer> classes = new ArrayList<Integer>();

				List<Float> entropies = new ArrayList<Float>();
				List<Float> splitPoints = new ArrayList<Float>();

				for (int z = 0; z < table.getNrCols(); z++) {
					
					Integer cls = Integer.valueOf(table.getElement(2, z));
					classes.add(cls);

					try {
						Float sp = Float.valueOf(table.getElement(4, z));
						splitPoints.add(sp);
					} catch (NumberFormatException e) {
						// last col of these rows have no value, empty
					}
					
					try {
						Float ep = Float.valueOf(table.getElement(5, z));
						entropies.add(ep);
					} catch (NumberFormatException e) {
						// last col of these rows have no value, empty
					}
				}


				
				float min_entropy = Collections.min(entropies);

				
				if(Collections.frequency(classes, classes.get(0)) == classes.size() && min_entropy == 0.0) {
					
					tableSplitPoints.add(Float.NaN);
					continue;
				}
				
				int min_index = entropies.indexOf(min_entropy);
				
				tableSplitPoints.add(splitPoints.get(min_index));
				
				table.highlightCell(5, min_index, null, null);
				table.highlightCell(4, min_index, null, null);

		  }
		
		  return tableSplitPoints;
	  }
	  

	  private void unHighlightEntropySplitPoints(List<StringMatrix> valueTables, int minIntervalSize) {
		  
		  for (int i = 0; i < valueTables.size(); i++) {
			  
				StringMatrix table = valueTables.get(i);
				
				if (table.getNrCols() <= minIntervalSize) {
					continue;
				}
				
				List<Float> entropies = new ArrayList<Float>();

				for (int z = 0; z < table.getNrCols(); z++) {
					
					try {
						Float ep = Float.valueOf(table.getElement(5, z));
						entropies.add(ep);
					} catch (NumberFormatException e) {
						// last col of these rows have no value, empty
					}
				}
				
				float min_entropy = Collections.min(entropies);
				int min_index = entropies.indexOf(min_entropy);
				
				if (table.getNrCols() > minIntervalSize) {
					table.unhighlightCell(5, min_index, null, null);
					table.unhighlightCell(4, min_index, null, null);
				}
		  }
		
	}




	private List<StringMatrix> splitTables(List<StringMatrix> valueTables, List<Float> splitPoints, MatrixProperties gridProps, StringMatrix tableHead, int minIntervalSize) {

		
		// for every table, get it splitpoint
		// create two lists at the splitpoint
		// create two new tables from these lists
		// add to table list, delete old table
		
	  List<StringMatrix> copyValueTables = new ArrayList<>();
		
	  for (int i = 0; i < valueTables.size(); i++) {
		  
			StringMatrix table = valueTables.get(i);

			
			if (table.getNrCols() <= minIntervalSize || splitPoints.get(i).isNaN()) {
								
				String [][] data = new String[table.getNrRows()][table.getNrCols()];
				
				for (int j = 0; j < table.getNrRows(); j++) {
					for (int j2 = 0; j2 < table.getNrCols(); j2++) {
						data[j][j2] = table.getElement(j, j2);
					}
				}
				
				table.hide();
				
				Node offsetD;
				
				if (copyValueTables.size()==0) {
					offsetD = new Offset(10, 0, tableHead, AnimalScript.DIRECTION_NE);
				} else {
					offsetD = new Offset(10, 0, copyValueTables.get(copyValueTables.size()-1), AnimalScript.DIRECTION_NE);
				}
				
				copyValueTables.add(lang.newStringMatrix(offsetD, data, table.getName()+"D", null, gridProps));
				
				// bug, move not working properly with offset still
				//table.moveTo(null, null, new Offset(4, 0, copyValueTables.get(copyValueTables.size()-1), AnimalScript.DIRECTION_NE), null, null);
				//copyValueTables.add(table);

				continue;
			}
			
			float splitPoint = splitPoints.get(i);

		
			//copyValueTables.remove(i);
			table.hide();
			
			float intv_start = 0, intv_end = 0;
			
			List<String> indizeS = new ArrayList<>();
			List<String> valuesS = new ArrayList<>();
			List<String> classesS = new ArrayList<>();
			List<String> intvS = new ArrayList<>();
			List<String> splitsS = new ArrayList<>();


			List<String> indizeA = new ArrayList<>();
			List<String> valuesA = new ArrayList<>();
			List<String> classesA = new ArrayList<>();
			List<String> intvA = new ArrayList<>();
			List<String> splitsA = new ArrayList<>();

			for (int z = 0; z < table.getNrCols(); z++) {

				String in = table.getElement(0, z);
				String f = table.getElement(1, z);
				String k = table.getElement(2, z);
				
				String intv = table.getElement(3, z);
				
				intv = intv.replaceAll("\\{", "");
				intv = intv.replaceAll("\\}", "");
				String[] intvs = intv.split(",");
				
				if (intvs[0] != "" && intvs[1] != "") {
					intv_start = Float.valueOf(intvs[0]);
					intv_end = Float.valueOf(intvs[1]);
				}

				
				if (Integer.valueOf(f) < splitPoint) {
					indizeS.add(in);
					valuesS.add(f);
					classesS.add(k);
					intvS.add("");
				} else {
					indizeA.add(in);
					valuesA.add(f);
					classesA.add(k);
					intvA.add("");
				}
			}

			String[] emptyS = new String[indizeS.size()];
			String[] emptyA = new String[indizeA.size()];

			for (int z = 0; z < indizeS.size(); z++) {
				emptyS[z] = "";
				
				if (z == indizeS.size() / 2) {
					intvS.set(z, "{" + intv_start + "," + splitPoint + "}");
				}
				
			}
			
			for (int z = 0; z < indizeA.size(); z++) {
				emptyA[z] = "";
				
				if (z == indizeA.size() / 2) {
					intvA.set(z, "{" + splitPoint + "," + intv_end + "}");
				}
			}
			
			
			for (int k=0; k < valuesS.size(); k++) {
				  if (k + 1 < valuesS.size()) {
					  
					  int sk = Integer.valueOf(valuesS.get(k));
					  int sk1 = Integer.valueOf(valuesS.get(k+1));
					  
					  splitsS.add(String.format(java.util.Locale.ROOT, "%.2f", (float) (sk + ((sk1 - sk) / 2.0)) ));
				  } else {
					  
					  splitsS.add("");
				  }
			}
			
			for (int k=0; k < valuesA.size(); k++) {
				  if (k + 1 < valuesA.size()) {
					  
					  int sk = Integer.valueOf(valuesA.get(k));
					  int sk1 = Integer.valueOf(valuesA.get(k+1));
					  
					  splitsA.add(String.format(java.util.Locale.ROOT, "%.2f", (float) (sk + ((sk1 - sk) / 2.0)) ));
				  } else {
					  
					  splitsA.add("");
				  }
			}
			
			
			String[][] tableArrayS = {indizeS.toArray(new String[indizeS.size()]), valuesS.toArray(new String[valuesS.size()]), classesS.toArray(new String[classesS.size()]), intvS.toArray(new String[intvS.size()]), splitsS.toArray(new String[splitsS.size()]), emptyS};
			String[][] tableArrayA = {indizeA.toArray(new String[indizeA.size()]), valuesA.toArray(new String[valuesA.size()]), classesA.toArray(new String[classesA.size()]), intvA.toArray(new String[intvA.size()]), splitsA.toArray(new String[splitsA.size()]), emptyA};

			Node offsetS;

			if (i==0) {
				offsetS = new Offset(10, 0, tableHead, AnimalScript.DIRECTION_NE);
			} else {
				offsetS = new Offset(10, 0, copyValueTables.get(copyValueTables.size()-1), AnimalScript.DIRECTION_NE);
			}
			
			
			copyValueTables.add(lang.newStringMatrix(offsetS, tableArrayS, table.getName()+"S"+i, null, gridProps));
			copyValueTables.add(lang.newStringMatrix(new Offset(10, 0, copyValueTables.get(copyValueTables.size()-1), AnimalScript.DIRECTION_NE), tableArrayA, table.getName()+"A"+i, null, gridProps));
	  }

	  //System.out.println(copyValueTables.size());
		
	  return copyValueTables;
	}
	  
}



public class EntropySplitGenerator implements ValidatingGenerator {
	
    private Language lang;
    
    private SourceCodeProperties sourceCodeHighlightColor;
    
    private TextProperties textHighlightColorPositive;
    private TextProperties textHighlightColorNegative;
    
    private int maxNumberOfIntervals;
    private int minIntervalSize;

    private int[] featureArray;
    private int[] classArray;

    public void init(){
        lang = new AnimalScript("Entropy-Split Discretization", "Moritz Lode", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	
        sourceCodeHighlightColor = (SourceCodeProperties)props.getPropertiesByName("sourceCodeHighlightColor");
        textHighlightColorPositive = (TextProperties)props.getPropertiesByName("textHighlightColorPositive");
        textHighlightColorNegative = (TextProperties)props.getPropertiesByName("textHighlightColorNegative");
        
        maxNumberOfIntervals = (int)primitives.get("maxNumberOfIntervals");
        minIntervalSize = (int)primitives.get("minIntervalSize");

        featureArray = (int[])primitives.get("featureArray");
        classArray = (int[])primitives.get("classArray");
        
        EntropySplitAPI es = new EntropySplitAPI(lang, sourceCodeHighlightColor, textHighlightColorPositive, textHighlightColorNegative);
				
		es.discretize(featureArray, classArray, maxNumberOfIntervals, minIntervalSize);
		
		return lang.toString();
    }

    
	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        int[] featureArray = (int[])primitives.get("featureArray");
        int[] classArray = (int[])primitives.get("classArray");
        
        return featureArray.length == classArray.length;
	}
    
    public String getName() {
        return "Entropy-Split Discretization";
    }

    public String getAlgorithmName() {
        return "Discretization";
    }

    public String getAnimationAuthor() {
        return "Moritz Lode";
    }

    public String getDescription(){
        return "Many classification algorithms require that the training data contains only discrete attributes."
 +"\n"
 +"To use such an algorithm when there are numeric attributes, all numeric values must first be converted into discrete values, a process called discretization."
 +"\n"
 +"\n"
 +"This animation describes Entropy-Split, a general, robust algorithm that uses the Entropy or expected information to discretize (quantize) numeric attributes."
 +"\n"
 +"Entropy-Split is a supervised discretization method, which also considers the relation between feature values and class values."
 +"\n"
 +"Feature values are thereby joined into initially one interval and in a top-down approach successively split until a given threshold of number of intervals, or minimum number values is reached."
 +"\n";
    }

    public String getCodeExample(){
        return " INIT:"
 +"\n"
 +"1. sort examples according to feature value F"
 +"\n"
 +"2. construct one interval containing all feature values"
 +"\n"
 +"3. initial list of possible split points for this interval"
 +"\n"
 +"\n"
 +"\n"
 +"LOOP:"
 +"\n"
 +"1. compute weighted entropy values for each interval split point"
 +"\n"
 +"2. for all intervals while (number of intervals < I and size of intervals > T):"
 +"\n"
 +"       3. split interval at the split point with lowest weighted entropy"
 +"\n"
 +"4. else STOP";  
    }

    public String getFileExtension(){
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
    
    public static void main(String[] args) {
    	Generator generator = new EntropySplitGenerator(); // Generator erzeugen
    	Animal.startGeneratorWindow(generator); // Animal mit Generator starten
	}

}