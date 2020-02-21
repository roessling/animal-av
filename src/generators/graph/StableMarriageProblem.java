/*
 * StableMarriageProblem.java
 * Aino Schwarte, David Steinmann, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graph;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import java.util.Random;

import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.searching.sss.SSSStar;
import interactionsupport.models.MultipleChoiceQuestionModel;
import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.TextProperties;
import algoanim.properties.MatrixProperties;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import algoanim.primitives.Graph;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Square;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.Timing;
import animal.main.Animal;


public class StableMarriageProblem implements Generator {
    private Language lang;
    private SourceCodeProperties sourceCode;
    private int sizeOfGroup;
    private MatrixProperties offerColor;
    private MatrixProperties engagementColor;
    private int[][] preferences1;
    private String[][] preferences2;
    private MatrixProperties activePersonColor;
    
    private Translator trans;
    private String Sprache;
    
    /**
	 * The header text
	 */
	private Text header;

	/**
	 * The background for the header
	 */
	private Rect headerBackground;

	/**
	 * The standard text properties
	 */
	private TextProperties textProps;

	private SourceCodeProperties srcProps;

	/**
	 * The source code shown next to the animation
	 */
	private SourceCode src;

	private Random rand;
	
	private double singleProb;
	
	private double proposeProb;
	
	private double acceptWhomProb;
	
	private double iterProb;
	
	boolean iterAsked;
	
	private MatrixProperties matProps;
	
	private MatrixProperties memberProps;
	
	private StringMatrix members1vis;
	
	private IntMatrix members2vis;
	
	private IntMatrix group1vis;

	private StringMatrix group2vis;

	private GraphProperties graphProps;
	
	private SquareProperties offerCo;
	
	private SquareProperties engagementCo;
	
	private SquareProperties activeCo;
	
	private Color sourceColor;
	
	private Color offerC;
	
	private Color engagementC;
	
	private Color activeC;

	private Graph graphvis;
	
	//private int cellW;
	
	@SuppressWarnings("unused")
	private Square squ1;
	
	@SuppressWarnings("unused")
	private Square squ2;
	
	@SuppressWarnings("unused")
	private Square squ3;
	
	@SuppressWarnings("unused")
	private StringMatrix lastOfferSentBy;
	
	private StringMatrix lastOfferSentTo;

	// global variables
	private int numOfPersons; // number of people in a group, therefore number of resulting matches

	private int numIteration;
	
	private Text iterationInfo;
	
	private HashMap<Integer, String> idToString = new HashMap<Integer, String>();

	private HashMap<String, Integer> stringToId = new HashMap<String, Integer>();

	// global data structures
	private int[][] group1; // Array of tuples, first element ID of betrothed init -1, second element place
							// in preference list of last person asked
	private int[] group2; // Array, with ID of betrothed, init -1

	private int[][] pref1; // Array of arrays, preferences of Group1, smaller number = more likeable
	private int[][] pref2; // Array of arrays, preferences of Group2

	private String[][] names1;
	private int[][] names2;
	
	private String[][] lastOffers;
    

	public static void main(String[] args) {
        Generator generator = new StableMarriageProblem(Locale.GERMANY);
        Animal.startGeneratorWindow(generator);
    }
	
	
    public void init(){
        lang = new AnimalScript("Stable Marriage Problem", "Aino Schwarte, David Steinmann", 800, 600);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        lang.setStepMode(true);
        }

    public StableMarriageProblem(Locale locale) {
      trans = new Translator("resources/StableMarriageProblem", locale);
/*    	Sprache = "deutsch";   //TODO fuer deutsch "Kommentar" wegmachen
    	
    	if(Sprache == "deutsch") {
    		trans = new Translator(new File("").getAbsolutePath().concat("resources/StableMarriageProblem"), Locale.GERMANY);
    	}
    	else {
    		trans = new Translator(new File("").getAbsolutePath().concat("resources/StableMarriageProblem"), Locale.US);
    	}
		*/
    	
		idToString.put(0, "A");
		idToString.put(1, "B");
		idToString.put(2, "C");
		idToString.put(3, "D");
		idToString.put(4, "E");
		idToString.put(5, "F");
		idToString.put(6, "G");
		idToString.put(7, "H");
		idToString.put(8, "I");
		idToString.put(9, "J");
		idToString.put(10, "K");
		idToString.put(11, "L");
		idToString.put(12, "M");
		idToString.put(13, "N");
		idToString.put(14, "O");
		idToString.put(15, "P");
		idToString.put(16, "Q");
		idToString.put(17, "R");
		idToString.put(18, "S");
		idToString.put(19, "T");
		idToString.put(20, "U");
		idToString.put(21, "V");
		idToString.put(22, "W");
		idToString.put(23, "X");
		idToString.put(24, "Y");
		idToString.put(25, "Z");
		idToString.put(26, "Ä");
		idToString.put(27, "Ö");
		idToString.put(28, "Ü");

		stringToId.put("A", 0);
		stringToId.put("B", 1);
		stringToId.put("C", 2);
		stringToId.put("D", 3);
		stringToId.put("E", 4);
		stringToId.put("F", 5);
		stringToId.put("G", 6);
		stringToId.put("H", 7);
		stringToId.put("I", 8);
		stringToId.put("J", 9);
		stringToId.put("K", 10);
		stringToId.put("L", 11);
		stringToId.put("M", 12);
		stringToId.put("N", 13);
		stringToId.put("O", 14);
		stringToId.put("P", 15);
		stringToId.put("Q", 16);
		stringToId.put("R", 17);
		stringToId.put("S", 18);
		stringToId.put("T", 19);
		stringToId.put("U", 20);
		stringToId.put("V", 21);
		stringToId.put("W", 22);
		stringToId.put("X", 23);
		stringToId.put("Y", 24);
		stringToId.put("Z", 25);
		stringToId.put("Ä", 26);
		stringToId.put("Ö", 27);
		stringToId.put("Ü", 28);

	}
    

	//@SuppressWarnings("unused")
	public void start(int numOfPerson, int[][] pref1, String[][] pref2) { // Personen heissen 0,1,2... und A,B,C,... Das
																			// ist Pflicht
		
		rand = new Random();	
		
		 // initialize the probabilities for the different question types:
		singleProb = 0.5;
		proposeProb = 0.5;
		acceptWhomProb = 0.5;
		iterProb = 0.3;
				
		
		offerC = (Color) offerColor.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY);
		engagementC = (Color) engagementColor.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY);
		activeC = (Color) activePersonColor.get(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY);
		sourceColor = (Color) sourceCode.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY);
		
		//cellW = (int) offerColor.get(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY);
		
		
		
		// Create the header and the background rectangle
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		header = lang.newText(new Coordinates(20, 30), trans.translateMessage("title"), "header", null, headerProps);

		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		headerBackground = lang.newRect(new Offset(-5, -5, "header", "NW"), new Offset(10, 5, "header", "SE"), "hrect",
				null, rectProps);
		lang.nextStep(trans.translateMessage("cont1"));

		// description and pseudo code algorithm
		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));

		lang.newText(new Coordinates(10, 100), trans.translateMessage("desc1"), "desc1", null, textProps); 
		lang.newText(new Offset(0, 25, "desc1", "NW"), trans.translateMessage("desc2"), "desc2", null, textProps);
		lang.newText(new Offset(0, 25, "desc2", "NW"), trans.translateMessage("desc3"), "desc3", null, textProps);
		lang.newText(new Offset(0, 25, "desc3", "NW"), trans.translateMessage("desc4"), "desc4", null, textProps);
		lang.newText(new Offset(0, 25, "desc4", "NW"), trans.translateMessage("desc5"), "desc5", null, textProps);
		lang.newText(new Offset(0, 50, "desc5", "NW"), trans.translateMessage("desc6"), "desc6", null, textProps);
		lang.newText(new Offset(0, 25, "desc6", "NW"), trans.translateMessage("desc7"), "desc7", null, textProps);
		lang.newText(new Offset(0, 25, "desc7", "NW"), trans.translateMessage("desc8"), "desc8", null, textProps);
		lang.newText(new Offset(0, 25, "desc8", "NW"), trans.translateMessage("desc9"), "desc9", null, textProps);
		lang.newText(new Offset(0, 25, "desc9", "NW"), trans.translateMessage("desc10"), "desc10", null, textProps);
		lang.newText(new Offset(0, 50, "desc10", "NW"), trans.translateMessage("desc11"), "desc11", null, textProps);
		lang.newText(new Offset(0, 25, "desc11", "NW"), trans.translateMessage("desc12"), "desc12", null, textProps);
		lang.newText(new Offset(0, 25, "desc12", "NW"), trans.translateMessage("desc13"), "desc13", null, textProps);
		lang.newText(new Offset(0, 50, "desc13", "NW"), trans.translateMessage("desc14"), "desc13", null, textProps);
		
		lang.nextStep();

		lang.hideAllPrimitives();
		header.show();
		headerBackground.show();

		srcProps = new SourceCodeProperties();
		srcProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		srcProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, sourceColor);

		src = lang.newSourceCode(new Coordinates(30, 100), "srcCode", null, srcProps);     
		src.addCodeLine(trans.translateMessage("code1"), null, 0, null);	//0
		src.addCodeLine(trans.translateMessage("code2"), null, 1, null);	//1
		src.addCodeLine(trans.translateMessage("code3"), null, 2, null);    //2
		src.addCodeLine(trans.translateMessage("code4"), null, 1, null);  	//3
		src.addCodeLine(trans.translateMessage("code5"), null, 2, null);	//4
		src.addCodeLine(trans.translateMessage("code6"), null, 3, null);	//5
		src.addCodeLine(trans.translateMessage("code7"), null, 2, null);	//6
		src.addCodeLine(trans.translateMessage("code8"), null, 3, null);	//7
		src.addCodeLine(trans.translateMessage("code9"), null, 4, null);	//8
		src.addCodeLine(trans.translateMessage("code10"), null, 4, null);  	//9
		src.addCodeLine(trans.translateMessage("code11"), null, 2, null);  	//10
		

		lang.nextStep();
		
		//Explanation Colors
		
		lang.newText(new Offset(50, 0, "header", "NE"), trans.translateMessage("leg1"), "legend1", null,  textProps);
		lang.newText(new Offset(20, 0, "legend1", "NE"), trans.translateMessage("leg2"), "legend2", null, textProps);
		lang.newText(new Offset(0, 20, "legend2", "NW"), trans.translateMessage("leg3"), "legend3", null, textProps);
		lang.newText(new Offset(0, 20, "legend3", "NW"), trans.translateMessage("leg4"), "legend4", null, textProps);
		
		offerCo = new SquareProperties();
		offerCo.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		offerCo.set(AnimationPropertiesKeys.FILL_PROPERTY, offerC);
		
		
		engagementCo = new SquareProperties();
		engagementCo.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		engagementCo.set(AnimationPropertiesKeys.FILL_PROPERTY, engagementC);
		
		activeCo = new SquareProperties();
		activeCo.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		activeCo.set(AnimationPropertiesKeys.FILL_PROPERTY, activeC);
		
		
		@SuppressWarnings("unused")
		Square squ3 = lang.newSquare(new Offset(25, 7, "legend4", "NE"), 10, "squ3", null, activeCo);
		@SuppressWarnings("unused")
		Square squ2 = lang.newSquare(new Offset(0, -20, "squ3", "NW"), 10, "squ2", null, engagementCo);
		@SuppressWarnings("unused")
		Square squ1 = lang.newSquare(new Offset(0, -20, "squ2", "NW"), 10, "squ1", null, offerCo);
		
		lang.nextStep(trans.translateMessage("cont2"));
		
		// create matrixes for persons
		this.numOfPersons = numOfPerson;

		group1 = new int[numOfPerson][2];
		group2 = new int[numOfPerson];

		for (int i = 0; i < numOfPerson; i++) {
			group1[i][0] = group2[i] = group1[i][1] = -1;
		}

		setPreferences(pref1, pref2);
		setNames();

		matProps = new MatrixProperties();
		matProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		matProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		matProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, offerC);			
		matProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		matProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, engagementC);
		//matProps.set(AnimationPropertiesKeys.CELL_WIDTH_PROPERTY, 10); 				

		memberProps = new MatrixProperties();
		memberProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		memberProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		memberProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, activeC);
		memberProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
		
		members1vis = lang.newStringMatrix( new Coordinates(480, 120), names1, "Members1", null, memberProps); 
		members2vis = lang.newIntMatrix(new Offset(240, 0, "Members1", "NE"), names2, "Members2", null, memberProps);  
		
		group1vis = lang.newIntMatrix(new Offset(0, 5, "Members1", "SW"), this.pref1, "Matrix1", null, matProps);
		group2vis = lang.newStringMatrix(new Offset(0, 5, "Members2", "SW"), pref2, "Matrix2", null, matProps); 

		graphProps = new GraphProperties();

		graphProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		graphProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.WHITE);
		graphProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.WHITE);
		graphProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.WHITE);
		graphProps.set(AnimationPropertiesKeys.DIRECTED_PROPERTY, true);

		int[][] graphAdjacencyMatrix = new int[numOfPerson*2][numOfPerson*2];

		for (int i = 0; i < graphAdjacencyMatrix.length; i++) {
			for (int j = 0; j < graphAdjacencyMatrix[0].length; j++) {
				graphAdjacencyMatrix[i][j] = 0;
			}
		}
		
		for (int i = 0; i < numOfPerson; i++) {
			for (int j = numOfPerson; j < numOfPerson*2; j++) {
				graphAdjacencyMatrix[i][j] = 1;
			}
		}
		
		Node[] graphNodes = new Node[numOfPerson*2];   
		
		for(int i = 0; i < numOfPerson; i++) {
			graphNodes[i]= new Offset(75, i*60, "Members1", "NE" );
		}
		
		for(int i = 0; i < numOfPerson; i++) {
			graphNodes[i+numOfPerson] = new Offset(155, i*60, "Members1","NE");
		}
		
		
		String[] labels = new String[numOfPerson*2];
		
		for(int i = 0; i < numOfPerson; i++) {
			labels[i] = idToString.get(i);
		}
		
		for(int i = 0; i < numOfPerson; i++) {
			labels[i+numOfPerson] = Integer.toString(i);
		}
		
		graphvis = lang.newGraph("Graph", graphAdjacencyMatrix, graphNodes, labels, null, graphProps);

		for (int i = 0; i < numOfPerson; i++) {
			for (int j = numOfPerson; j < numOfPerson*2; j++) {
				graphvis.highlightEdge(i, j, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			}
		}

		lastOffers = new String[1][numOfPerson];
		
		for(int i=0; i<numOfPerson; i++) {
			lastOffers[0][i]= "   ";
		}
		
		lang.newText(new Offset(0, 20, "Matrix1", "SW"), "Last Offers:", "Offers", null, textProps);
		lastOfferSentBy = lang.newStringMatrix(new Offset(0, 5, "Offers", "SW"), names1, "lastOfferSentBy", null, matProps);
		lastOfferSentTo = lang.newStringMatrix(new Offset(0, 0, "lastOfferSentBy", "SW"), lastOffers, "lastOfferSentTo", null, matProps);
		
		
		lang.nextStep();

		marriage();

	}
	
	public void setNames() {
		this.names1 = new String[1][numOfPersons];
		this.names2 = new int[1][numOfPersons];
		
		for (int i = 0; i < numOfPersons; i++) {
			names1[0][i] = idToString.get(i);
			names2[0][i] = i;
		}
	}

	public void setPreferences(int[][] pref1, String[][] pref2) {
		this.pref1 = pref1;

		this.pref2 = new int[pref2.length][pref2.length];

		for (int i = 0; i < pref2.length; i++) {
			for (int j = 0; j < pref2.length; j++) {
				this.pref2[i][j] = stringToId.get(pref2[i][j]);
			}
		}
	}

	/**
	 * makes stable matches between all members of group 1 and 2, based on the
	 * preferences
	 */
	public void marriage() {					

		src.highlight(0);
		
		//for questions
		int k = 0;
		int l = 0;
		iterAsked = false;
		
		//Counter Iterations
		numIteration = 0;
		
		iterationInfo = lang.newText(new Coordinates(30, 80), createIterationInfo(), "iterationInfo", null, textProps);
		
		
		while (checkPendingMatches()) { // as long as there are single people
			List<List<Integer>> proposals = new ArrayList<List<Integer>>();
			
			numIteration = numIteration + 1;
			
			boolean proposeThisIter = false;
			boolean acceptThisIter = false;
			
			
			if(rand.nextFloat() <= iterProb && !iterAsked && numIteration <= numOfPersons && numIteration > 1) {
				MultipleChoiceQuestionModel q = new MultipleChoiceQuestionModel("Iterations" + numIteration);
				q.setPrompt(trans.translateMessage("q1"));
				q.addAnswer(trans.translateMessage("yes"), 0, trans.translateMessage("a1f"));
				q.addAnswer(trans.translateMessage("no"), 1, trans.translateMessage("a1t"));
				
				lang.addMCQuestion(q);
				iterAsked = true;
				
			}
			if(!iterAsked) {
				iterProb += 0.1;
			}
			
		
			 
			
			iterationInfo.setText(createIterationInfo(), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			lang.nextStep(trans.translateMessage("cont3") + numIteration);
			
			
			
			
			
			for (int i = 0; i < numOfPersons; i++) {
				proposals.add(new ArrayList<Integer>());
			}		
					
			for (int i = 0; i < numOfPersons; i++) {

				src.unhighlight(2);
				members1vis.highlightCell(0, i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				
				
				if (group1[i][0] == -1) { // all unmatched group1 members make offers
					
					src.highlight(1);
					
					
					
					lang.nextStep();
					
					
					src.highlight(2);
					//lang.nextStep();
					
					int nextPref = ++group1[i][1]; // (update who was asked last, to person being asked)
					int idNextOffer = pref1[nextPref][i]; // to next person on preference list, who was not asked yet
					proposals.get(idNextOffer).add(i); // (add offer to list of proposals)
					
					if( numIteration > 1 && rand.nextFloat() <= proposeProb && !proposeThisIter) {		
						
						k++;
						MultipleChoiceQuestionModel q = new MultipleChoiceQuestionModel("propose" + k);
						q.setPrompt(trans.translateMessage("q2"));
						
						for(int j = 0; j < numOfPersons; j++) {
							if(j == idNextOffer ) {
								q.addAnswer(""+j, 1, trans.translateMessage("true"));
							}
							else {
								q.addAnswer(""+j, 0, trans.translateMessage("false"));
							}
						}
						
						proposeProb -= 0.1;
						lang.addMCQuestion(q);
						proposeThisIter = true;
						lang.nextStep();
					}
					
					lastOfferSentTo.put(0, i, String.valueOf(idNextOffer), Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					
					group1vis.highlightCell(nextPref, i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
					graphvis.unhighlightEdge(i, idNextOffer + numOfPersons, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
					lang.nextStep();
					group2vis.highlightCell(getPref2FromId(i, idNextOffer), idNextOffer, Timing.INSTANTEOUS,
							Timing.INSTANTEOUS);
					lang.nextStep();

				}
				members1vis.unhighlightCell(0, i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
			}

			// Offers sent, next accepting
			
			src.unhighlight(1);
			src.unhighlight(2);
			src.highlight(3);
			

			for (int i = 0; i < numOfPersons; i++) { // for all members of group 2
				
				members2vis.highlightCell(0, i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				
				
			
				
				if (proposals.get(i).size() != 0) { // if there are offers for member of group2
					List<Integer> offers = proposals.get(i);
				
					int bestOfferId = getBestOffer(offers, i); // get best offer out of the proposals received this
																// round, ID of Person
					
					if(proposals.get(i).size() >=1 && rand.nextFloat() <= acceptWhomProb && !acceptThisIter) {
						l++;
						MultipleChoiceQuestionModel q = new MultipleChoiceQuestionModel("Accept which offer" + l);
						q.setPrompt(trans.translateMessage("q3"));
					
						
						if(group2[i] == -1) { 
							for(int j = 0; j < proposals.get(i).size(); j++) {
								if(proposals.get(i).get(j) == bestOfferId) {
									q.addAnswer(idToString.get(proposals.get(i).get(j)), 1, trans.translateMessage("a31"));
								}
								else {
									q.addAnswer(idToString.get(proposals.get(i).get(j)), 0, trans.translateMessage("a32"));
								}
							}
							q.addAnswer(trans.translateMessage("a3"), 0, trans.translateMessage("a33"));
						}
						else { 
							if(getPref2FromId(bestOfferId, i) < getPref2FromId(group2[i], i)) {
								for(int j = 0; j < proposals.get(i).size(); j++) {
									if(proposals.get(i).get(j) == bestOfferId) {
										q.addAnswer(idToString.get(proposals.get(i).get(j)), 1, trans.translateMessage("a34"));
									}
									else {
										q.addAnswer(idToString.get(proposals.get(i).get(j)), 0, trans.translateMessage("a35"));
									}
								}
								q.addAnswer(trans.translateMessage("a3"), 0, trans.translateMessage("a36"));
							}
							else { 
								for(int j = 0; j < proposals.get(i).size(); j++) {
									q.addAnswer(idToString.get(proposals.get(i).get(j)), 0, trans.translateMessage("a37"));
								}
								q.addAnswer(trans.translateMessage("a3"), 1, trans.translateMessage("a38"));
							}
						}
						lang.addMCQuestion(q);
						acceptWhomProb -= 0.1;
						acceptThisIter = true;
						lang.nextStep();
				}
					
			
					lang.nextStep();
					src.highlight(4);
					
					if (group2[i] > -1) { // if there already is a valid engagement	
						
						lang.nextStep();
						src.unhighlight(4);
						src.highlight(6);
						lang.nextStep();
						src.highlight(7);
						
						if (getPref2FromId(bestOfferId, i) < getPref2FromId(group2[i], i)) { // Is the best new offer  
																								// better than the
																								// existing engagement?
							int previousPref = getPref2FromId(group2[i], i);	//store old preference, for visual 
							
							group1[bestOfferId][0] = i; // accept new offer, write own ID as betrothed
							group1[group2[i]][0] = -1; // previous engagement has to be nullified, delete oneself as
														// betrothed
							group2[i] = bestOfferId; // store new engagement

							//reject old best offer vis
							
							lang.nextStep();
							src.highlight(8);
							
							group2vis.unhighlightElem(previousPref, i, Timing.INSTANTEOUS,
									Timing.INSTANTEOUS);
							lang.nextStep();
							graphvis.highlightEdge(pref2[previousPref][i], i + numOfPersons, Timing.INSTANTEOUS,
									Timing.INSTANTEOUS);
							lang.nextStep();
							group1vis.unhighlightElem(getPref1FromId(i, pref2[previousPref][i]) , pref2[previousPref][i], Timing.INSTANTEOUS,
									Timing.INSTANTEOUS);
							lang.nextStep();
							
							src.unhighlight(8);

							// accept new best vis
							
							src.highlight(9);
						
							group2vis.unhighlightCell(getPref2FromId(bestOfferId, i), i, Timing.INSTANTEOUS,
									Timing.INSTANTEOUS);
							group2vis.highlightElem(getPref2FromId(bestOfferId, i), i, Timing.INSTANTEOUS,
									Timing.INSTANTEOUS); // highlight at group 2 accepted offer
							lang.nextStep();

							group1vis.unhighlightCell(getPref1FromId(i, bestOfferId), bestOfferId, Timing.INSTANTEOUS,
									Timing.INSTANTEOUS); // Highlighting at Group 1 accepted offer
							group1vis.highlightElem(getPref1FromId(i, bestOfferId), bestOfferId, Timing.INSTANTEOUS,
									Timing.INSTANTEOUS);
							lang.nextStep();
							
							src.unhighlight(9);
							src.unhighlight(7);
							src.unhighlight(6);
							
							
							// REJECT all others vis
							
							src.highlight(10);
							for (int j = 0; j < offers.size(); j++) {
								if (!offers.get(j).equals(bestOfferId)) {
									
									if(j > 0) {
									    lang.nextStep();
									} 
									
									group2vis.unhighlightCell(getPref2FromId(offers.get(j), i), i, Timing.INSTANTEOUS,
											Timing.INSTANTEOUS); // unhighlight rejected offers
									lang.nextStep();
									graphvis.highlightEdge(offers.get(j), i + numOfPersons, Timing.INSTANTEOUS,
											Timing.INSTANTEOUS);
									lang.nextStep();
									group1vis.unhighlightCell(getPref1FromId(i, offers.get(j)), offers.get(j),
											Timing.INSTANTEOUS, Timing.INSTANTEOUS);
									
									
								}
							}
							lang.nextStep();
							src.unhighlight(10);
							
						} else {// no new best offer, reject all
							
							lang.nextStep();
							src.unhighlight(6);
							src.unhighlight(7);
							src.highlight(10);
							
							for (int j = 0; j < offers.size(); j++) {
								
								if (j > 0) {
									lang.nextStep();
								}	
								
									group2vis.unhighlightCell(getPref2FromId(offers.get(j), i), i, Timing.INSTANTEOUS,
											Timing.INSTANTEOUS); // unhighlight rejected offers
									lang.nextStep();
									graphvis.highlightEdge(offers.get(j), i + numOfPersons, Timing.INSTANTEOUS,
											Timing.INSTANTEOUS);
									lang.nextStep();
									group1vis.unhighlightCell(getPref1FromId(i, offers.get(j)), offers.get(j),
											Timing.INSTANTEOUS, Timing.INSTANTEOUS);
									
									
								
							}
							
							lang.nextStep();
							src.unhighlight(10);
							
						}

					} else { // first time any offers are received
					
					
						lang.nextStep();
						src.highlight(5);
						
						group1[bestOfferId][0] = i; // accept offer, write own ID as betrothed
						group2[i] = bestOfferId; // store new engagement

						group2vis.unhighlightCell(getPref2FromId(bestOfferId, i), i, Timing.INSTANTEOUS,
								Timing.INSTANTEOUS);
						group2vis.highlightElem(getPref2FromId(bestOfferId, i), i, Timing.INSTANTEOUS,
								Timing.INSTANTEOUS); // highlight at group 2 accepted offer
						lang.nextStep();

						group1vis.unhighlightCell(getPref1FromId(i, bestOfferId), bestOfferId, Timing.INSTANTEOUS,
								Timing.INSTANTEOUS); // Highlighting at Group 1 accepted offer
						group1vis.highlightElem(getPref1FromId(i, bestOfferId), bestOfferId, Timing.INSTANTEOUS,
								Timing.INSTANTEOUS);
						lang.nextStep();
						
		
						src.unhighlight(4);
						src.unhighlight(5);
						src.highlight(10);
						

						for (int j = 0; j < offers.size(); j++) {
							if (!offers.get(j).equals(bestOfferId)) {
								
								if (j > 0) {
									lang.nextStep();
								}
								
								group2vis.unhighlightCell(getPref2FromId(offers.get(j), i), i, Timing.INSTANTEOUS,
										Timing.INSTANTEOUS); // unhighlight rejected offers
								lang.nextStep();
								graphvis.highlightEdge(offers.get(j), i + numOfPersons, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
								lang.nextStep();
								group1vis.unhighlightCell(getPref1FromId(i, offers.get(j)), offers.get(j),
										Timing.INSTANTEOUS, Timing.INSTANTEOUS);
							
							}

						}
						
						lang.nextStep();
						src.unhighlight(10);
					}

				}
				
				members2vis.unhighlightCell(0, i, Timing.INSTANTEOUS, Timing.INSTANTEOUS);
				

			}
			
			src.unhighlight(3);
			
			if( rand.nextFloat() <= singleProb || (checkPendingMatches() == false && rand.nextFloat() < singleProb+0.3) ) {
				MultipleChoiceQuestionModel q = new MultipleChoiceQuestionModel("single" + numIteration);
				q.setPrompt(trans.translateMessage("q4"));
				
				if(checkPendingMatches() == true) {
					q.addAnswer(trans.translateMessage("yes"), 1, trans.translateMessage("a4t"));
					q.addAnswer(trans.translateMessage("no"), 0, trans.translateMessage("a4f"));
				}
				else {
					q.addAnswer(trans.translateMessage("no"), 1, trans.translateMessage("a4ft"));
					q.addAnswer(trans.translateMessage("yes"), 0, trans.translateMessage("a4ff"));
				}
				lang.addMCQuestion(q);
				singleProb -= 0.2;
				lang.nextStep();
			}
			
		}
		
		src.unhighlight(3);
		lang.nextStep();
		src.unhighlight(0);
		lang.nextStep(trans.translateMessage("cont4"));
		
		finish();
	}

	private void finish() {
		 lang.hideAllPrimitives();
		 iterationInfo.hide();
		
		 header.show();
	     headerBackground.show();
	     
	     lang.newText(new Coordinates(10, 100), trans.translateMessage("end1"), "end1", null, textProps);
	     lang.newText(new Offset(0, 25, "end1", "NW"), trans.translateMessage("end2"), "end2", null, textProps);
	     lang.newText(new Offset(0, 25, "end2", "NW"), trans.translateMessage("end3"), "end3", null, textProps);
	     lang.newText(new Offset(0, 25, "end3", "NW"), trans.translateMessage("end4"), "end4", null, textProps);
	     lang.newText(new Offset(0, 50, "end4", "NW"), trans.translateMessage("end5"), "end5", null, textProps);
	     lang.newText(new Offset(0, 25, "end5", "NW"), trans.translateMessage("end6"), "end6", null, textProps);
	     lang.newText(new Offset(0, 25, "end6", "NW"), trans.translateMessage("end7"), "end7", null, textProps);
	     lang.newText(new Offset(0, 50, "end7", "NW"), trans.translateMessage("end8"), "end8", null, textProps);
	     lang.newText(new Offset(0, 25, "end8", "NW"), trans.translateMessage("end9"), "end9", null, textProps);
	     lang.newText(new Offset(0, 25, "end9", "NW"), trans.translateMessage("end10"), "end10", null, textProps);
	     lang.newText(new Offset(0, 25, "end10", "NW"), trans.translateMessage("end11"), "end11", null, textProps);
	     
	     lang.nextStep(trans.translateMessage("cont5"));
	     lang.finalizeGeneration();
	}
	
	
	
	
	/**
	 * checks if any Person is still in need of a partner. "A single man of a good
	 * fortune must be in want of a wife"
	 * 
	 * @return true, if matches still need to be made; false, if everyone is matched
	 */
	public boolean checkPendingMatches() {
		for (int i = 0; i < numOfPersons; i++) {
			if (group1[i][0] == -1 || group2[i] == -1) // checks, if any Person is single aka betrothed = -1
				return true;
		}
		return false;
	}

	/**
	 * Gets the id of the most preferred Person, out of the Persons, who have made
	 * Offers to 'ownId', a member of group2.
	 * 
	 * @param offers
	 * @param ownId
	 * @return
	 */
	public int getBestOffer(List<Integer> offers, int ownId) {

		int bestOffer = -1;
		int prefValue = numOfPersons - 1;  

		Iterator<Integer> it = offers.iterator();
		int currentOffer;

		while (it.hasNext()) {
			currentOffer = it.next(); // Get next Offer (ID of the Person, who offered
			for (int i = 0; i < numOfPersons; i++) {
				if (pref2[i][ownId] == currentOffer) { // Search each Person in Preference List
					if (i <= prefValue) { // Check, if Offer is better
						prefValue = i; // Update best Offer
						bestOffer = currentOffer; //
					}
					break; // Person found in List
				}
			}
		}

		return bestOffer;
	}

	/**
	 * Gets the rank of Person 'id' in the Preference list of 'ownId', a member of
	 * group2
	 * 
	 * @param id
	 * @param ownId
	 * @return
	 */
	public int getPref2FromId(int id, int ownId) {

		for (int i = 0; i < numOfPersons; i++) {
			if (pref2[i][ownId] == id) // Search Person in Preference List
				return i; // Person found in List, return Preference

		}
		return -1;
	}

	public int getPref1FromId(int id, int ownId) {

		for (int i = 0; i < numOfPersons; i++) {
			if (pref1[i][ownId] == id) // Search Person in Preference List
				return i; // Person found in List, return Preference

		}
		return -1;
	}
	
	private String createIterationInfo() {
		StringBuilder result = new StringBuilder();
		
		result.append("Iteration: ").append(numIteration);
		
		return result.toString();
	}
	
    
    
    
    
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        sizeOfGroup = (Integer)primitives.get("sizeOfGroup");
        offerColor = (MatrixProperties)props.getPropertiesByName("offerColor");
        engagementColor = (MatrixProperties)props.getPropertiesByName("engagementColor");
        preferences1 = (int[][])primitives.get("preferences1");
        preferences2 = (String[][])primitives.get("preferences2");
        activePersonColor = (MatrixProperties)props.getPropertiesByName("activePersonColor");
        
        start(sizeOfGroup, preferences1, preferences2);
        
        return lang.toString();
    }

    public String getName() {
        return "Stable Marriage Problem";
    }

    public String getAlgorithmName() {
        return "Stable Marriage Problem";
    }

    public String getAnimationAuthor() {
        return "Aino Schwarte, David Steinmann";
    }

    public String getDescription(){
    	if ( trans == null) 
            System.out.println("bl");
    	
    		String description = trans.translateMessage("gd1") + "\n"
                + trans.translateMessage("gd2") + "\n"
                + trans.translateMessage("gd3");
         return description;
    }

    public String getCodeExample(){
    	
    	return trans.translateMessage("gc1") + "\n"
                + "     " + trans.translateMessage("gc2")+ "\n"
                + "          " + trans.translateMessage("gc3") + "\n" 
                + "     " + trans.translateMessage("gc4") + "\n"
                + "          " + trans.translateMessage("gc5") + "\n"
                + "               " + trans.translateMessage("gc6") + "\n"
                + "          " + trans.translateMessage("gc7") +"\n"
                + "               " + trans.translateMessage("gc8") + "\n"
                + "                    "  + trans.translateMessage("gc9") + "\n"
                + "                    " + trans.translateMessage("gc10") + "\n"
                + "          " + trans.translateMessage("gc11");
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
    	if(Sprache == "deutsch"){
        	return Locale.GERMAN;
        }
        else {
        	return Locale.ENGLISH;
        }
    	
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}