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



class ChiMergeAPI {

	private Language lang;
	
	private SourceCodeProperties sourceCodeHighlightColor;
	
	private TextProperties textHighlightColorPositive;
	private TextProperties textHighlightColorNegative;
	  

	  public ChiMergeAPI(Language l, SourceCodeProperties sourceCodeHighlightColor, TextProperties textHighlightColorPositive, TextProperties textHighlightColorNegative) {
	    // Store the language object
	    this.lang = l;
	    
	    this.sourceCodeHighlightColor = sourceCodeHighlightColor;
	    this.textHighlightColorPositive = textHighlightColorPositive;
	    this.textHighlightColorNegative = textHighlightColorNegative;
	    
	    // This initializes the step mode. Each pair of subsequent steps has to be divided by a call of lang.nextStep();
	    lang.setStepMode(true);
	  }

	  
	  private static final String INTRO_DESCRIPTION = "Many classification algorithms require that the training data contains only discrete attributes.\n"
	      + "To use such an algorithm when there are numeric attributes, all numeric values must first be converted into discrete values, a process called discretization.\n"
	      + "This animation describes Chi-Merge, a general, robust algorithm that uses the X� statistics to discretize (quantize) numeric attributes.\n"
	      + "Chi-Merge is a supervised discretization method, which also considers the relation between feature values and class values.\n"
	      + "Feature values are thereby divided into intervals and in a bottom-up approach successively combined until a given threshold is reached.\n"
	      + "\n"
	      + "Randy Kerber. 1992. ChiMerge: discretization of numeric attributes. In Proceedings of the tenth national conference on Artificial intelligence (AAAI'92). AAAI Press 123-128.";
	  
	  
	  private static final String OUTRO_DESCRIPTION = "We showed how a given table was discretized using X� statistics in a bottom-up merging process.\n"
		      + "Resulting in a discretization in form of intervals for the feature value F and its class value K.\n"
		      + "\n"
		      + "Randy Kerber. 1992. ChiMerge: discretization of numeric attributes. In Proceedings of the tenth national conference on Artificial intelligence (AAAI'92). AAAI Press 123-128.";

	  
	  private static final String PSEUDO_CODE     = "1. compute X� values for each adjacent interval pair\n"              // 0
	      + "2. if(exits X� value < T)\n"                                                                                 // 1
	      + "\t3. merge intervals with the lowest X� values\n"                                                            // 2
	      + "4. else STOP\n";                                                                                             // 3

	  
	  private static final String INIT_PSEUDO_CODE     = "1. sort examples according to feature value F\n"              // 0
		      + "2. construct one interval for each value\n";                                                           // 1
	  
	  
	  private static final String MATH_FORMULAS     = "X� = SUM( SUM( ((A_ij - E_ij)^2) / E_ij) | j=1 to j=|K|) | i=1 to i=2\n"             
		      + "E_ij = N_i * (A_1j + A_2j) / N_1 + N_2\n"                                                         
			  + "N_i = SUM( A_ij) | j=1 to j=|K|\n"                                                         
			  + "\n"                                                         
			  + "A_ij = number of values in i-th interval that are of class j\n"                                                           
			  + "E_ij = expected number of examples in i-th interval that are of class j\n"                                                          
			  + "|K| = number of classes in K\n";                                                          


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
	   *          
	   * @exception IllegalArgumentException if arguments arrayF and arrayK do not have the same length.
	   */
	  protected void discretize(int[] arrayF, int[] arrayK, double threshold) throws IllegalArgumentException {
		  
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
			
			vl.declare("double", "threshold");
			vl.set("threshold", String.valueOf(threshold));
		  
		  
		  // header
		  TextProperties txtHeaderProps = new TextProperties();
		  txtHeaderProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		  txtHeaderProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

		  Text header = lang.newText(new Coordinates(20, 30), "Chi-Merge", "header", null, txtHeaderProps);

		  
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
		  		  
		  String[] intros = ChiMergeAPI.INTRO_DESCRIPTION.split("\n");
		 
		  Text intro0 = lang.newText(new Coordinates(20, 120), intros[0], "intro0", null, txtProps);
		  Text intro1 = lang.newText(new Coordinates(20, 140), intros[1], "intro1", null, txtProps);
		  Text intro2 = lang.newText(new Coordinates(20, 180), intros[2], "intro2", null, txtProps);
		  Text intro3 = lang.newText(new Coordinates(20, 200), intros[3], "intro3", null, txtProps);
		  Text intro4 = lang.newText(new Coordinates(20, 220), intros[4], "intro4", null, txtProps);
		  Text intro5 = lang.newText(new Coordinates(20, 260), intros[5], "intro5", null, txtProps);
		  Text intro6 = lang.newText(new Coordinates(20, 260), intros[6], "intro6", null, txtProps);

		  // end intro text
		  
		  
		  lang.nextStep("Introduction");

		 
		  intro0.hide();
		  intro1.hide();
		  intro2.hide();
		  intro3.hide();
		  intro4.hide();
		  intro5.hide();
		  intro6.hide();

		  
		  TextProperties txtElemProps = new TextProperties();
		  txtElemProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
		  txtElemProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		  
		  Text lblThreshold = lang.newText(new Offset(80, 0, header, AnimalScript.DIRECTION_NE), "T=" + threshold, "threshold", null, txtElemProps);

		  
		  MatrixProperties gridProps = new MatrixProperties();
		  gridProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.gray);
		  gridProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		  gridProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 12));
		  gridProps.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");

		  
		  
		  // create table header which is fixed
		  String[][] tableHeaderArray = {{"i"}, {"F"}, {"K"}, {"{}"}};
		  
		  StringMatrix tableHead = lang.newStringMatrix(new Offset(0, 50, hRect, AnimalScript.DIRECTION_SW), tableHeaderArray, "tableHead", null, gridProps);


		  // now create all interval tables for the given values dynamically
		  
		  List<StringMatrix> valueTables = new ArrayList<StringMatrix>();

		  float intv_start = (float) 0.0;
		  float intv_end = (float) 0.0;
		  
		  for (int i = 0; i < featureArray.length; i++) {
			  Integer f = featureArray[i];
			  Integer k = classArray[i];
			  
			  String sf = String.valueOf(f);
			  String sk = String.valueOf(k);
			  
			  
			  intv_start = intv_end;

			  if (i + 1 < featureArray.length) {
				  intv_end = (float) (featureArray[i] + ((featureArray[i+1] - featureArray[i]) / 2.0)) ;
			  } else {
				  intv_end = featureArray[i] + 1 ;
			  }
			  
			  
			  String[][] tableArray = {{String.valueOf(i)}, {sf}, {sk}, {"{"+intv_start+","+intv_end+"}"}};

			  if (i == 0) {
				  valueTables.add(lang.newStringMatrix(new Offset(4, 0, tableHead, AnimalScript.DIRECTION_NE), tableArray, "table"+i, null, gridProps));
			  } else {
				  valueTables.add(lang.newStringMatrix(new Offset(4, 0, valueTables.get(i-1), AnimalScript.DIRECTION_NE), tableArray, "table"+i, null, gridProps));
			  }
		   }
		  
		  // intro source code
		  
		// first, set the visual properties for the source code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		
		Font font = (Font) this.sourceCodeHighlightColor.get("font");

		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font.getName(), Font.PLAIN, 16));


	    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, this.sourceCodeHighlightColor.get("highlightColor"));
	    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, this.sourceCodeHighlightColor.get("color"));

	    // now, create the source code entity
	    SourceCode initSc = lang.newSourceCode(new Offset(0, 100, tableHead, AnimalScript.DIRECTION_SW), "initCode", null, scProps);

	    String[] initScs = ChiMergeAPI.INIT_PSEUDO_CODE.split("\n");
	    
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

	    tableHead.unhighlightCell(3, 0, null, null);
	    initSc.hide();

	    SourceCode loopSc = lang.newSourceCode(new Offset(0, 100, tableHead, AnimalScript.DIRECTION_SW), "loopCode", null, scProps);

	    String[] loopScs = ChiMergeAPI.PSEUDO_CODE.split("\n");
	    
		for (int i = 0; i < loopScs.length; i++) {
			int intend = 0;
			if (i==2) intend=1;
			
			loopSc.addCodeLine(loopScs[i], null, intend, null);
		}
		
		loopSc.highlight(0);
		
		
		SourceCodeProperties formulaProps = new SourceCodeProperties();
		formulaProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		formulaProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 16));
	    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, this.textHighlightColorNegative.get("color"));
		formulaProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.gray);
	    

	    // now, create the source code entity
	    SourceCode mathFormula = lang.newSourceCode(new Offset(100, -10, loopSc, AnimalScript.DIRECTION_NE), "chiMath", null, formulaProps);

	    String[] mathFormulas = ChiMergeAPI.MATH_FORMULAS.split("\n");
	    
		for (int i = 0; i < mathFormulas.length; i++) {	
			mathFormula.addCodeLine(mathFormulas[i], null, 0, null);
		}
		
		
		String[][] tableHeaderChiArray = {{"X�"}};
		  
		StringMatrix tableChiHead = lang.newStringMatrix(new Offset(0, 35, tableHead, AnimalScript.DIRECTION_SW), tableHeaderChiArray, "tableHead", null, gridProps);

		
		PolylineProperties polylineProps = new PolylineProperties();
		polylineProps.set(AnimationPropertiesKeys.BWARROW_PROPERTY, false);
		polylineProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, false);

		
		vl.declare("double", "minChiSquare");
		
		// LOOP
		
		boolean stop = false;
		int iterationCounter = 0;
		
		while (!stop) {
			
			List<Text> chiValuesTxt = new ArrayList<Text>();
			List<Polyline> chiLines = new ArrayList<Polyline>();
			
			List<Float> chiValues = calcChiValues(valueTables, unique_classes);
			
			
			String min_s = String.format(java.util.Locale.ROOT, "%.2f", Collections.min(chiValues)); // workaround to get the right precision for all parsed values
			float min = Float.valueOf(min_s);
			vl.set("minChiSquare", String.valueOf(min));


			createChiRow(valueTables, chiValues, chiValuesTxt, chiLines, polylineProps, txtElemProps);

			
		    lang.nextStep("Iteration " + iterationCounter);
		    iterationCounter++;
		    
		    mathFormula.hide();
		    
			loopSc.unhighlight(0);
			loopSc.highlight(1);
			
			boolean exists = highlightChiThreshold(chiValuesTxt, threshold);


		    lang.nextStep();
		    
		    
		    if (exists) {
			    
				loopSc.unhighlight(1);
				loopSc.highlight(2);
				
				highlightChiMin(chiValuesTxt, min);


			    lang.nextStep();

			    
			    hideChiRow(chiValuesTxt, chiLines);

				valueTables = mergeTable(valueTables, chiValuesTxt, min, gridProps, tableHead);
				
				
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
		  String[] outros = ChiMergeAPI.OUTRO_DESCRIPTION.split("\n");
			 
		  Text outro0 = lang.newText(new Coordinates(20, 280), outros[0], "outro0", null, txtProps);
		  Text outro1 = lang.newText(new Coordinates(20, 300), outros[1], "outro1", null, txtProps);
		  Text outro2 = lang.newText(new Coordinates(20, 320), outros[2], "outro2", null, txtProps);
		  Text outro3 = lang.newText(new Coordinates(20, 360), outros[3], "outro3", null, txtProps);

	  }

	  
	  private List<Float> calcChiValues(List<StringMatrix> valueTables, List<Integer> classes) {
		  
		  int num_classes = classes.size();
		  
		  List<Float> chiValues = new ArrayList<Float>();
		  
		  for (int y = 1; y < valueTables.size(); y++) {
			  
			// calc chi value of tables y and y-1
			StringMatrix table0 = valueTables.get(y-1);
			StringMatrix table1 = valueTables.get(y);
			
			
			List<Integer> values0 = new ArrayList<Integer>();
			List<Integer> classes0 = new ArrayList<Integer>();
			
			List<Integer> values1 = new ArrayList<Integer>();
			List<Integer> classes1 = new ArrayList<Integer>();
			
			for (int z = 0; z < table0.getNrCols(); z++) {

				Integer f = Integer.valueOf(table0.getElement(1, z));
				Integer k = Integer.valueOf(table0.getElement(2, z));
				
				values0.add(f);
				classes0.add(k);
			}
			
			for (int z = 0; z < table1.getNrCols(); z++) {

				Integer f = Integer.valueOf(table1.getElement(1, z));
				Integer k = Integer.valueOf(table1.getElement(2, z));
				
				values1.add(f);
				classes1.add(k);
			}
			
			
			float[][] A = new float[2][num_classes];
			float[][] E = new float[2][num_classes];
			
			
			for (int j = 0; j < num_classes; j++) {
				// check how many in interval 0
				A[0][j] = Collections.frequency(classes0, classes.get(j));
				
				// check how many in interval 1
				A[1][j] = Collections.frequency(classes1, classes.get(j));
			}
			
			
			float n0 = (float) 0.0;
			for(float i : A[0]) {
				n0 += i;
			}
			
			float n1 = (float) 0.0;
			for(float i : A[1]) {
				n1 += i;
			}		
			
			
			for (int j = 0; j < num_classes; j++) {

				float c_j = A[0][j] + A[1][j];
				
				E[0][j] = n0 * (c_j / (n0 + n1));
				E[1][j] = n1 * (c_j / (n0 + n1));
			}
			

			float chi_square = (float) 0.0;
			
			for (int i = 0; i < 2; i++) {
				
				float sum_1 = (float) 0.0;
				
				for (int j = 0; j < num_classes; j++) {
					
					if (E[i][j] == 0.0) {
						sum_1 += 0.0;
					} else {
						sum_1 += Math.pow((A[i][j] - E[i][j]), 2) / E[i][j];
					}
					
				}
				
				chi_square += sum_1;
			}

			chiValues.add(chi_square);
		  }

		  return chiValues;
	  }
	  
	  
	  private boolean highlightChiThreshold(List<Text> chiValuesTxt, double threshold) {
		boolean smaller_exits = false;
		// hightlight chi values which smaller T
		for (int i = 0; i < chiValuesTxt.size(); i++) {
			
			if (Float.parseFloat(chiValuesTxt.get(i).getText()) < threshold) {
				chiValuesTxt.get(i).changeColor(AnimalScript.COLORCHANGE_COLOR, (Color) this.textHighlightColorPositive.get("color"), null, null);
				smaller_exits = true;
			} else {
				chiValuesTxt.get(i).changeColor(AnimalScript.COLORCHANGE_COLOR, (Color) this.textHighlightColorNegative.get("color"), null, null);
			}
		}
		return smaller_exits;
	  }
	  
	  private void highlightChiMin(List<Text> chiValuesTxt, float min) {
		  
		for (int i = 0; i < chiValuesTxt.size(); i++) {
			
			if (Float.parseFloat(chiValuesTxt.get(i).getText()) == min) {
				chiValuesTxt.get(i).changeColor(AnimalScript.COLORCHANGE_COLOR, (Color) this.textHighlightColorPositive.get("color"), null, null);
			} else {
				chiValuesTxt.get(i).changeColor(AnimalScript.COLORCHANGE_COLOR, (Color) this.textHighlightColorNegative.get("color"), null, null);
			}
		}
	  }
	  
	  private void hideChiRow(List<Text> chiValuesTxt, List<Polyline> chiLines) {
		for (int i = 0; i < chiValuesTxt.size(); i++) {
			chiValuesTxt.get(i).hide();
		}
		
		for (int i = 0; i < chiLines.size(); i++) {
			chiLines.get(i).hide();
		}
	  }
	  
	  private void createChiRow(List<StringMatrix> valueTables, List<Float> chiValues, List<Text> chiValuesTxt, List<Polyline> chiLines, PolylineProperties polylineProps, TextProperties txtElemProps) {
		  
		for (int i = 0; i < valueTables.size(); i++) {
			
			if (i==0) {
				
				Node[] nodes0 = {new Offset(0, 1, valueTables.get(i), AnimalScript.DIRECTION_S), new OffsetFromLastPosition(10, 40)};
				Polyline chiLine0 = lang.newPolyline(nodes0, "chiLine"+i+0, null, polylineProps);
				
				Text chiValue = lang.newText(new Offset(-5, 40, valueTables.get(i), AnimalScript.DIRECTION_SE), String.format(java.util.Locale.ROOT, "%.2f", chiValues.get(i)), "chiValue"+i, null, txtElemProps);
				
				chiValuesTxt.add(chiValue);
				chiLines.add(chiLine0);
				
			} else if (i==valueTables.size()-1) {
				
				Node[] nodes0 = {new Offset(0, 1, valueTables.get(i), AnimalScript.DIRECTION_S), new OffsetFromLastPosition(-10, 40)};
				Polyline chiLine0 = lang.newPolyline(nodes0, "chiLine"+i+0, null, polylineProps);
				
				chiLines.add(chiLine0);
			} else {
				
				Text chiValue = lang.newText(new Offset(-5, 40, valueTables.get(i), AnimalScript.DIRECTION_SE), String.format(java.util.Locale.ROOT, "%.2f", chiValues.get(i)), "chiValue"+i, null, txtElemProps);
				
				Node[] nodes0 = {new Offset(0, 1, valueTables.get(i), AnimalScript.DIRECTION_S), new OffsetFromLastPosition(-10, 40)};
				Node[] nodes1 = {new Offset(0, 1, valueTables.get(i), AnimalScript.DIRECTION_S), new OffsetFromLastPosition(10, 40)};

				Polyline chiLine0 = lang.newPolyline(nodes0, "chiLine"+i+0, null, polylineProps);
				Polyline chiLine1 = lang.newPolyline(nodes1, "chiLine"+i+1, null, polylineProps);
				
				chiValuesTxt.add(chiValue);
				chiLines.add(chiLine0);
				chiLines.add(chiLine1);
			}
		}
	  }

	  
	  private List<StringMatrix> mergeTable(List<StringMatrix> valueTables, List<Text> chiValuesTxt, float min, MatrixProperties gridProps, StringMatrix tableHead) {
		  
		  List<Integer[]> merges = new ArrayList<Integer[]>();
				  
			// merge tables with same, lowest chivalue
			for (int i = 0; i < chiValuesTxt.size(); i++) {
				
				if (Float.parseFloat(chiValuesTxt.get(i).getText()) == min) {
					List<Integer> intv_merge = new ArrayList<Integer>();
					
					int j=i;
					for (; j < chiValuesTxt.size(); j++) {
						if (Float.parseFloat(chiValuesTxt.get(j).getText()) == min) {
							intv_merge.add(j);
						} else {
							break;
						}
					}
					i = j;

					intv_merge.add(Collections.max(intv_merge) + 1);
					merges.add(intv_merge.toArray(new Integer[intv_merge.size()]));
				}
			}
				
			List<StringMatrix> copyValueTables = new ArrayList<>(valueTables);
			
			for (int i = 0; i < merges.size(); i++) {
				
				// get the tables to merge
				Integer[] intv_merge = merges.get(i);
					
				List<String> intv_indize = new ArrayList<String>();
				List<String> intv_values = new ArrayList<String>();
				List<String> intv_classes = new ArrayList<String>();
				
				float i_intv_start = Float.MAX_VALUE;
				float i_intv_end = (float) 0.0;
				
				for (int j = 0; j < intv_merge.length; j++) {
					StringMatrix table = valueTables.get(intv_merge[j]);
					
					for (int z = 0; z < table.getNrCols(); z++) {

						Integer index = Integer.valueOf(table.getElement(0, z));
						Integer f = Integer.valueOf(table.getElement(1, z));
						Integer k = Integer.valueOf(table.getElement(2, z));
						
						intv_indize.add(String.valueOf(index));
						intv_values.add(String.valueOf(f));
						intv_classes.add(String.valueOf(k));

						String intv = table.getElement(3, z);
						
						intv = intv.replaceAll("\\{", "");
						intv = intv.replaceAll("\\}", "");
						String[] intvs = intv.split(",");
						
						
						for(int x = 0; x < intvs.length; x++) {

							if (!intvs[x].isEmpty()) {
								
								Float tmp = Float.valueOf(intvs[x]);
							    
							    if (x==0 & tmp < i_intv_start) {
							    	i_intv_start = tmp;
							    } else if (x==1 & tmp > i_intv_end) {
							    	i_intv_end = tmp;
							    }
							}
						}
					}
					
					table.hide();
				    copyValueTables.set(intv_merge[j].intValue(), null);
				}

				String[] new_intv = new String[intv_indize.size()];
				for(int l = 0; l < new_intv.length; l++) {
					new_intv[l] = "";
				}
				new_intv[new_intv.length/2] = "{"+i_intv_start+","+i_intv_end+"}";
				
				String[][] i_tableArray = {intv_indize.toArray(new String[intv_indize.size()]), intv_values.toArray(new String[intv_values.size()]), intv_classes.toArray(new String[intv_classes.size()]), new_intv};

				int new_offset = intv_merge[0]-1;
				
				Node offset;

				if (intv_merge[0]-1 < 0) {
					offset = new Offset(4, 0, tableHead, AnimalScript.DIRECTION_NE);
				} else {
					offset = new Offset(4, 0, valueTables.get(new_offset), AnimalScript.DIRECTION_NE);
				}
				
				StringBuilder strBuilder = new StringBuilder();
				for (int q = 0; q < intv_merge.length; q++) {
				   strBuilder.append(intv_merge[q]);
				}		
				
				copyValueTables.set(intv_merge[0], lang.newStringMatrix(offset, i_tableArray, "table"+strBuilder.toString(), null, gridProps));
			}
				
			while (copyValueTables.remove(null));
			
			return copyValueTables;
	  }
	  
}



public class ChiMergeGenerator implements ValidatingGenerator {
	
    private Language lang;
    
    private SourceCodeProperties sourceCodeHighlightColor;
    
    private TextProperties textHighlightColorPositive;
    private TextProperties textHighlightColorNegative;
    
    private int[] featureArray;
    private double threshold;
    private int[] classArray;

    public void init(){
        lang = new AnimalScript("Chi-Merge Discretization", "Moritz Lode", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	
        sourceCodeHighlightColor = (SourceCodeProperties)props.getPropertiesByName("sourceCodeHighlightColor");
        textHighlightColorPositive = (TextProperties)props.getPropertiesByName("textHighlightColorPositive");
        textHighlightColorNegative = (TextProperties)props.getPropertiesByName("textHighlightColorNegative");
        
        featureArray = (int[])primitives.get("featureArray");
        threshold = (double)primitives.get("threshold");
        classArray = (int[])primitives.get("classArray");
        
		ChiMergeAPI cm = new ChiMergeAPI(lang, sourceCodeHighlightColor, textHighlightColorPositive, textHighlightColorNegative);
				
		cm.discretize(featureArray, classArray, threshold);
		
		return lang.toString();
    }

    
	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        int[] featureArray = (int[])primitives.get("featureArray");
        int[] classArray = (int[])primitives.get("classArray");
        
        return featureArray.length == classArray.length;
	}
    
    public String getName() {
        return "Chi-Merge Discretization";
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
 +"This animation describes Chi-Merge, a general, robust algorithm that uses the X� statistics to discretize (quantize) numeric attributes."
 +"\n"
 +"Chi-Merge is a supervised discretization method, which also considers the relation between feature values and class values."
 +"\n"
 +"Feature values are thereby divided into intervals and in a bottom-up approach successively combined until a given threshold is reached."
 +"\n"
 +"\n"
 +"\n"
 +"Randy Kerber. 1992. ChiMerge: discretization of numeric attributes. In Proceedings of the tenth national conference on Artificial intelligence (AAAI'92). AAAI Press 123-128.";
    }

    public String getCodeExample(){
        return " INIT:"
 +"\n"
 +"1. sort examples according to feature value F"
 +"\n"
 +"2. construct one interval for each value"
 +"\n"
 +"\n"
 +"\n"
 +"LOOP:"
 +"\n"
 +"1. compute X� values for each adjacent interval pair"
 +"\n"
 +"2. if(exits X� value < T):                                                      "
 +"\n"
 +"       3. merge intervals with the lowest X� values"
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
    	Generator generator = new ChiMergeGenerator(); // Generator erzeugen
    	Animal.startGeneratorWindow(generator); // Animal mit Generator starten
	}

}