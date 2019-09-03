/*
 * Glicko2Generator.java
 * Maxim Kuznetsov, Fritz Beutel, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.List;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.hepersGlicko2.ActuallyUsableMultilineText;
import generators.misc.hepersGlicko2.Glicko2Player;
import generators.misc.hepersGlicko2.Glicko2Table;
import generators.misc.hepersGlicko2.Glicko2VisualRating;
import generators.misc.hepersGlicko2.VariableManager;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.CodeView;
import algoanim.animalscript.addons.InfoBox;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

@SuppressWarnings("unused")
public class Glicko2Generator implements ValidatingGenerator {
    private Language lang;
    private Translator t;
    private Locale locale;
    private ArrayProperties array;
    private Glicko2Player[] players;
    private double[][][] results;
    
    private Text header;
    private Rect hRect;
    private String ltag;
    
    private SourceCode sc;
    private SourceCodeProperties scProps;

    public void init(){
        ltag = getContentLocale().getLanguage() + "_" + getContentLocale().getCountry();
        lang = new AnimalScript("Glicko-2", "Maxim Kuznetsov, Fritz Beutel", 800, 600);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }
    
    public Glicko2Generator(Locale locale) {
        t = new Translator("resources/Glicko2", locale);
        this.locale = locale;
    }
    
    public Glicko2Generator() {
    	this(Locale.US);
    }
    
	@Override
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
    	int rp = (int) primitives.get("ratingPeriod"); 
        String[][] pl = (String[][]) primitives.get("playerStats");
        int[][] rs= (int[][]) primitives.get("roundResults");
       
        /* ########################
         * ### PARAMETER CHECKS ###
         * ######################## */
        
        if (rp < 0) throw new IllegalArgumentException(t.translateMessage("negativeRatingPeriod"));
        else if (rp == 0) throw new IllegalArgumentException(t.translateMessage("zeroRatingPeriod"));
        
        if (pl == null) throw new IllegalArgumentException(t.translateMessage("emptyPlayerStats"));
        else if (pl[0].length != 4) throw new IllegalArgumentException(t.translateMessage("doNotAddColumns"));
        else if (pl.length < 2) throw new IllegalArgumentException(t.translateMessage("atLeast2Players"));
        
        for (int row = 0; row < pl.length; row++) {        	
        	if (Double.parseDouble(pl[row][0]) < 0) 
        		throw new IllegalArgumentException(t.translateMessage("ratingCannotBeNegativeAt") + " (" + row + ",0)");
        	if (Double.parseDouble(pl[row][1]) < 0) 
        		throw new IllegalArgumentException(t.translateMessage("ratingDeviationCannotBeNegativeAt") + " (" + row + ",1)");
        	if (Double.parseDouble(pl[row][2]) < 0) 
        		throw new IllegalArgumentException(t.translateMessage("volatalityCannotBeNegativeAt") + " (" + row + ",2)");
        	if (!Boolean.parseBoolean(pl[row][3])) 
        		throw new IllegalArgumentException(t.translateMessage("mustBeTrueOrFalseAt") + " (" + row + ",3)");
        }
        
        if (rs == null) throw new IllegalArgumentException(t.translateMessage("emptyRoundResults"));
        else if (rs.length == pl.length) throw new IllegalArgumentException(t.translateMessage("columnsMustMatchPlayerCount"));
        else if (rs.length % (pl[0].length - 1) != 0) throw new IllegalArgumentException(t.translateMessage("invalidRowCount"));
        
        //Check for valid result input
        for (int r = 0; r < rs.length; r++) {
        	for (int c = r + 1; c < rs[0].length; c++) {
        		int tmp = rs[r][c];
        		if (tmp != 0 && tmp != 1 && tmp != 2 && tmp != -1) 
        			throw new IllegalArgumentException(t.translateMessage("invalidResultInputAt") + " (" + r + "," + c + ")");
        		// ADD PLAYER DID NOT COMPETE CHECK
        	}
        }
        
        
		
		
		return true;
	}

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	//int ratingPeriod_input = (int) primitives.get("ratingPeriod"); 
        String[][] playerStats_input = (String[][]) primitives.get("playerStats");
        int[][] roundResults_input= (int[][]) primitives.get("roundResults");
        double tau = (double) primitives.get("tau");
        Color chcolor = (Color) primitives.get("codeHighlightColor");
        Color pbcolor = (Color) primitives.get("ratingBarColor");
        boolean codeOnly = (boolean) primitives.get("displayCodeOnly");

        /* #######################
         * ### ALGORITHM SETUP ###
         * ####################### */
        
        int pcount = roundResults_input[0].length;
        int rounds = roundResults_input.length / (pcount - 1);
        
        //System.out.println("Rounds: "+ rounds + " | Players: " + pcount);
        
        results = new double[rounds][pcount][pcount];
        players = new Glicko2Player[playerStats_input.length];
        
        /* 
         * We will be using Animal's StringMatrix only for displaying the elements, 
         * while our internal data is stored inside a double[round][player][opponent] for the round results and a double[][] for player data.
         */
        String[][] roundResults_display = new String[rounds*pcount][pcount + 1];
        for (int rnd = 0; rnd < rounds; rnd++) {
        	roundResults_display[rnd*pcount][0] = "Round " + (rnd + 1);
        	for (int col = 0; col < pcount; col++) {
        		if (col != 0) roundResults_display[rnd*pcount + col][0] = "P" + col;
        		for (int row = 1; row < pcount + 1; row++) {
            		if (col == 0) {
            			roundResults_display[rnd*pcount + col][row] = "";
            			continue;
            		}
        			String tmp = "";
        			switch(roundResults_input[rnd*(pcount-1) + col - 1][row-1]) {
        			case 2: tmp = t.translateMessage("win"); results[rnd][col - 1][row - 1] = 1; break; //mirror now maybe? results[rnd][row - 1][col - 1] = 0; break;
        			case 1: tmp = t.translateMessage("draw"); results[rnd][col - 1][row - 1] = 0.5; break;
        			case 0: tmp = t.translateMessage("loss"); results[rnd][col - 1][row - 1] = 0; break;
        			case -1: tmp = "N/A"; results[rnd][col - 1][row - 1] = -1; break;
        			default: tmp = ""; results[rnd][col - 1][row - 1] = -2; break;
        			}
        			roundResults_display[rnd*pcount + col][row] = tmp;
        		}
        	}
        } 
        for(int r = 1; r < pcount + 1; r++) roundResults_display[0][r] = "P" + r;
        
        /*
         * Mirror data in results for easier access later
         * and make sure to mirror win to loss and vice versa
         * and yes i mixed up rows and collumns... pls ignore
         */
        for (int rnd = 0; rnd < rounds; rnd++) {
        	for (int c = 0; c < results[0].length; c++) {
        		for (int r = 0; r < results[0][0].length; r++) {
        			if (r < c) {
        				if (results[rnd][r][c] == 1) results[rnd][c][r] = 0;
        				else if (results[rnd][r][c] == 0) results[rnd][c][r] = 1;
           				else results[rnd][c][r] = results[rnd][r][c]; 
        			}		
        		}
        	}
        }
        
        /*
         * Set up player stat table for display and fill players-Array for internal calculations
         */
        String[][] playerStats_display = new String[playerStats_input.length + 1][playerStats_input[0].length + 1];
        playerStats_display[0][0] = "";
        playerStats_display[0][1] = t.translateMessage("rating");
        playerStats_display[0][2] = t.translateMessage("rd_short");
        playerStats_display[0][3] = t.translateMessage("volatility");
        playerStats_display[0][4] = t.translateMessage("ratedOrNot");
        for (int c = 1; c < playerStats_input.length + 1; c++) {
        	playerStats_display[c][0] = t.translateMessage("player") + " " + c;
        	Glicko2Player p = new Glicko2Player(Double.parseDouble(playerStats_input[c-1][0]),
        			Double.parseDouble(playerStats_input[c-1][1]),
        			Double.parseDouble(playerStats_input[c-1][2]),
        			!Boolean.parseBoolean(playerStats_input[c-1][3]));
        	players[c-1] = p;
        	for (int r = 1; r <= 4; r++) {
        		playerStats_display[c][r] = playerStats_input[c-1][r-1];
        	}
        }
        
        /* #######################
         * #### DISPLAY SETUP ####
         * ####################### */
        
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",Font.BOLD, 24));
        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
        header = lang.newText(new Coordinates(20, 30), t.translateMessage("title"), "header", null, headerProps);
        
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255, 50, 0));
        rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(255, 190, 0));
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        hRect = lang.newRect(new Offset(-5, -5, "header",
            AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
            null, rectProps);
        
        VariableManager varManager = new VariableManager(lang);
        
        varManager.add("Round", 1);
        varManager.add("Player", 1);
        varManager.add("my", 0.0);
        varManager.add("Phi", 0.0);
        varManager.add("ny", 0.0);
        varManager.add("Delta", 0.0);
        /*
        varManager.add("A", 0.0);
        varManager.add("B", 0.0);
        varManager.add("C", 0.0);
        varManager.add("fA", 0.0);
        varManager.add("fB", 0.0);
        varManager.add("fC", 0.0);
        */
        varManager.add("sigma'", 0.0);
        varManager.add("Phi*", 0.0);
        varManager.add("Phi'", 0.0);
        varManager.add("my'", 0.0);
        
        RectProperties lineProps = new RectProperties();
        lineProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        lineProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        lineProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        Rect varBox = lang.newRect(new Offset(-5, 15, varManager.getLastVar(), "SW"), new Offset(0, 15, "hRect", "SE"), "line1", null, lineProps);
        
        /*Draws the graph to visually show the ratings */
        TextProperties subHeader = new TextProperties();
        subHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",Font.BOLD, 16));
        Text subText = lang.newText(new Offset(0, 15, varBox, "SW"), t.translateMessage("playerRating"), "subHeader", null, subHeader);
        Glicko2VisualRating visGraph = new Glicko2VisualRating(lang, new Coordinates(50, 250), players, pbcolor);
        
        /*
        TextProperties periodProps = new TextProperties();
        periodProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Serif",Font.PLAIN, 14));
        String ratingPeriod_string = (ratingPeriod_input == 1) ? "Rating period: 1 Round" : "Rating period: " + ratingPeriod_input + " Rounds";
        lang.newText(new Offset(-30,30, visGraph.getLowerLeft(), "SW"), ratingPeriod_string, "ratingPeriod", null, periodProps);
        */
        
        MatrixProperties smProps = new MatrixProperties();
        smProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        StringMatrix sm = lang.newStringMatrix(new Offset(-30,30, visGraph.getLowerLeft(), "SW")/*new Offset(0,10, "ratingPeriod", "SW")*/, playerStats_display, "playerStats", null, smProps);

        MatrixProperties imProps = new MatrixProperties();
        imProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
        StringMatrix im = lang.newStringMatrix(new Offset(0,20, "playerStats", "SW"), roundResults_display, "roundResults", null, imProps);
        
        /*Display setup of the middle row (step explanation)*/
        
        TextProperties stepProps = new TextProperties();
        stepProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 20));
        Text step_display = lang.newText(new Offset(50,0,"header", "NE"), t.translateMessage("overview"), "step_display", null, stepProps);
        rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        Rect sRect = lang.newRect(new Offset(-5, -5, "step_display",
                AnimalScript.DIRECTION_NW), new Offset(500, 5, "step_display", "SW"), "cRect",
                null, rectProps);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
        Rect sbigRect = lang.newRect(new Offset(-5, -5, "step_display",
                AnimalScript.DIRECTION_NW), new Offset(500, 600, "step_display", "NW"), "cbigRect",
                null, rectProps);

        InfoBox ib = new InfoBox(lang, new Offset(0,5,"step_display", "SW"), 50, "");
        ib.setText(ActuallyUsableMultilineText.textFromFile("overview." + ltag ));
        if (codeOnly) {
        	sRect.hide();
        	sbigRect.hide();
        	ib.hide();
        }
        
         /*Display setup of the right row (pseudo code) */
        
        scProps = new SourceCodeProperties();
        scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 12));
        scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, chcolor);
        scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        
        sc = lang.newSourceCode(new Offset(50, codeOnly ? 15 : 0, codeOnly ? "header" : "cbigRect", "NE"), "sourceCode", null, scProps);
        sc.addMultilineCode(CodeView.exampleFromFile("resources/glicko2/code.txt"), "", null);
        sc.hide();

        /*END OF SETUP */
        
        /* #########################
         * #### BEGIN ALGORITHM ####
         * ######################### */
        
        
        lang.nextStep();
        
        MultipleSelectionQuestionModel question1 = new MultipleSelectionQuestionModel("question1");
        question1.setPrompt(t.translateMessage("q1_prompt"));
        question1.addAnswer(t.translateMessage("q1_o1"), 100, t.translateMessage("q1_f1"));
        question1.addAnswer(t.translateMessage("q1_o2"), 100, t.translateMessage("q1_f2"));
        question1.addAnswer(t.translateMessage("q1_o3"), 0, t.translateMessage("q1_f3"));
        question1.addAnswer(t.translateMessage("q1_o4"), 100, t.translateMessage("q1_f4"));
        lang.addMSQuestion(question1);
        
        lang.nextStep();
        
        MultipleChoiceQuestionModel question4 = new MultipleChoiceQuestionModel("question4");
        question4.setPrompt(t.translateMessage("q4_prompt"));
        question4.addAnswer(t.translateMessage("q4_o1"), 0, t.translateMessage("q4_f2"));
        question4.addAnswer(t.translateMessage("q4_o2"), 100, t.translateMessage("q4_f2"));
        question4.addAnswer(t.translateMessage("q4_o3"), 0, t.translateMessage("q4_f3"));
        lang.addMCQuestion(question4);
        
        lang.nextStep();
        
        TrueFalseQuestionModel question2 = new TrueFalseQuestionModel("question2");
        question2.setPrompt(t.translateMessage("q2_prompt"));
        question2.setCorrectAnswer(true);
        question2.setPointsPossible(100);
        lang.addTFQuestion(question2);
        

        
        int line = 0;
        
        for (int r = 0; r < results.length; r++) {
	        
        	varManager.update("Round", r+1, true, "####");
     
	        int pNo = 1;
	        for (Glicko2Player p : players) {
	        	
    	/* ### STEP 1 ### */
    	/* Initialization */
		        lang.nextStep();        
		        step_display.setText(t.translateMessage("init_round") + (r+1) + " | " + t.translateMessage("player") + " " + pNo, null, null);
		        
		        varManager.update("Player", pNo, true, "####");
		        sc.show();
	        	
		        ib.setText(ActuallyUsableMultilineText.textFromFile("step1." + ltag));	        
		        
	        	Glicko2Table.highlightRow(sm, pNo);
		        sc.highlight(line);
	        	lang.nextStep("(1) "+ t.translateMessage("init_round") + (r+1) + " | " + t.translateMessage("player") + " " + pNo);
		        
        		Glicko2Table.highlightCell(sm, pNo, 4, Color.YELLOW.darker());
	        	sc.highlight(line+1);	
	        	lang.nextStep();
        		
	        	if (p.isUnrated()) {
	        		Glicko2Table.highlightCell(sm, pNo, 4, Color.GREEN);
	        		Glicko2Table.updateRow(sm, pNo, Color.CYAN, new String[] {null, "1500", "350", "0.06"});
	        		
	        		sc.highlight(line+2);
	        		//sm.put(pNo + 1, 4, "true", null, null); //update when player was rated
	        	}
	        	else {
	        		Glicko2Table.highlightCell(sm, pNo, 4, new Color(200, 40, 40));
	        		sc.unhighlight(line+1);
	        		sc.highlight(line+3);
	        		sc.highlight(line+4);
	        	}
	        	double vol = p.getVotality();
	        	lang.nextStep();
	        	
	        	sc.unhighlight(line);
	        	sc.unhighlight(line+1);
	        	sc.unhighlight(line+2);
	        	sc.unhighlight(line+3);
	        	sc.unhighlight(line+4);
	        	
	        	//Table.unhighlightRow(sm, pNo);
	        	Glicko2Table.highlightRow(sm, pNo, Color.YELLOW);
	        	
    	/* ### STEP 2 ### */
    	/* Conversion to Glicko-Scale */
	        	line = 6;
	        	
	        	ib.setText(ActuallyUsableMultilineText.textFromFile("step2." + ltag));
	        	step_display.setText(t.translateMessage("step2"), null, null);
	        	
	        	sc.highlight(line);
	        	lang.nextStep("(2)" + t.translateMessage("step2"));
	        	
	        	varManager.update("my", p.getConvertedRating(), true);
	        	
	        	sc.highlight(line+1);
	        	lang.nextStep();
	        	
	        	varManager.update("Phi", p.getConvertedRatingDev(), true);
	        	
	        	sc.unhighlight(line+1);
	        	sc.highlight(line+2);
	        	/* END OF STEP 2 */
	        	
	        	lang.nextStep();
	        	
	        	sc.unhighlight(line);
	        	sc.unhighlight(line+2);
	        	
	        	varManager.clearHighlight();
	        	
    	/* ### STEP 3 ### */
    	/* Calculation of estimated variance */
	        	line = 10;
	        	
	        	ib.setText(ActuallyUsableMultilineText.textFromFile("step3." + ltag));
	        	step_display.setText(t.translateMessage("step3") + " (ny)", null, null);
	        	
	        	sc.highlight(line);
	        	lang.nextStep("(3) " + t.translateMessage("step3"));
	        	
	        	//Create array of opponents the player competed against
	        	Glicko2Player[] opponents = new Glicko2Player[pcount-1];
	        	int ip = 0;
	        	for (Glicko2Player o : players) {
	        		if (!p.equals(o) && o.isCompeting()) { // Competing check!
	        			opponents[ip] = o;
	        			ip++;
	        		}
	        	}
	        	
	        	varManager.update("ny", p.estimatedVariance(opponents), true);
	        	sc.highlight(line+3);
	        	
	        	/* END OF STEP 3 */
	        	lang.nextStep();
	        	
	        	sc.unhighlight(line);
	        	sc.unhighlight(line+3);
	        	
	        	varManager.clearHighlight();
	        	
    	/* ### STEP 4 ### */
    	/* Calculation of estimated improvement in rating */
	        	line = 15;
	        	
	        	ib.setText(ActuallyUsableMultilineText.textFromFile("step4." + ltag));
	        	step_display.setText(t.translateMessage("step4") + " (Delta)", null, null);
	        	
	        	sc.highlight(line);
	        	lang.nextStep("(4) "+ t.translateMessage("step4"));
	   
	        	//Create array of results against the opponents
	        	int defeats = 0;
	        	int wins = 0;
	        	int draws = 0;
	        	double[] round_results = new double[pcount-1];
	        	ip = 0;
	        	for (int i = 0; i < results[r][pNo-1].length; i++) {
	        		if (i != pNo - 1 && results[r][pNo-1][i] != -1) {
	        			round_results[ip] = results[r][pNo-1][i];
	        			if (round_results[ip] == 0) defeats++;
	        			else if (round_results[ip] == 0.5) draws++;
	        			else if (round_results[ip] == 1) wins++;
	        			ip++;
	        		}
	        	}
	        	
	        	varManager.update("Delta", p.estimatedImprovement(opponents, round_results), true);
	        	sc.highlight(line+1);
	        	
	        	/* END OF STEP 4 */
	        	lang.nextStep();
	        	
	        	sc.unhighlight(line);
	        	sc.unhighlight(line+1);
	        	
	        	varManager.clearHighlight();
	        	
    	/* ### STEP 5 ### */
    	/* Calculation of new volatility */
	        	line = 18;
	        	
	        	ib.setText(ActuallyUsableMultilineText.textFromFile("step5." + ltag));
	        	step_display.setText(t.translateMessage("step5") +  " (sigma')", null, null);
	        	
	        	p.updateVotality(opponents, round_results, tau);
	        	varManager.update("sigma'", p.getVotality(), true, ".#####");
	        	
	        	sc.highlight(line);
	        	
	        	
	        	/* END OF STEP 5 */
	        	lang.nextStep("(5) " + t.translateMessage("step5"));
	        	
	            TrueFalseQuestionModel question3 = new TrueFalseQuestionModel("question3");
	            question3.setPrompt(t.translateMessage("q3_prompt"));
	            question3.setCorrectAnswer(false);
	            question3.setPointsPossible(100);
	            lang.addTFQuestion(question3);
	            
	        	sc.unhighlight(line);
	        	// unhighlight last line
	        	
	        	varManager.clearHighlight();
	        	
    	/* ### STEP 6 ### */
    	/*  */
	        	line = 46;
	        	
	        	ib.setText(ActuallyUsableMultilineText.textFromFile("step6." + ltag));
	        	step_display.setText(t.translateMessage("step6"), null, null);
	        	
	        	sc.highlight(line);
	        	lang.nextStep("(6) " + t.translateMessage("step6"));
	        	
	        	p.calcRating(opponents, round_results);
	        	
	        	varManager.update("Phi*", p.phi_star, true);
	        	sc.highlight(line+1);
	        	
	        	/* END OF STEP 6 */
	        	lang.nextStep();
	        	
	        	sc.unhighlight(line);
	        	sc.unhighlight(line+1);
	        	
	        	varManager.clearHighlight();
	        	
    	/* ### STEP 7 ### */
    	/*  */
	        	line = 49;
	        	
	        	ib.setText(ActuallyUsableMultilineText.textFromFile("step7." + ltag));
	        	step_display.setText(t.translateMessage("step7"), null, null);
	        	
	        	sc.highlight(line);
	        	lang.nextStep("(7) " + t.translateMessage("step7"));
	        	
	        	varManager.update("Phi'", p.rating_deviation_new, true);
	        	sc.highlight(line+1);
	        	lang.nextStep();
	        	
	        	varManager.update("my'", p.rating_new, true);
	        	sc.unhighlight(line+1);
	        	sc.highlight(line+2);
	        	
	        	/* END OF STEP 7 */
	        	lang.nextStep();
	        	
	        	sc.unhighlight(line);
	        	sc.unhighlight(line+1);
	        	sc.unhighlight(line+2);
	        	
	        	varManager.clearHighlight();
	        	
    	/* ### STEP 8 ### */
    	/* Converting ratings back to Glicko-2 scale */
	        	line = 53;
	        	
	        	ib.setText(ActuallyUsableMultilineText.textFromFile("step8." + ltag));
	        	step_display.setText(t.translateMessage("step8"), null, null);
	        	
	        	sc.highlight(line);
	        	lang.nextStep("(8) " + t.translateMessage("step8"));
	        	
	        	sm.put(pNo, 1, "" + new DecimalFormat("#####").format(173.7178 * p.rating_new + 1500), null, null);
	        	
	        	Glicko2Table.highlightCell(sm, pNo, 1, Color.CYAN);
	        	sc.highlight(line+1);
	        	visGraph.updatePlayer(p); //Update the Graph
	        	lang.nextStep();
	        	
	        	sc.unhighlight(line+1);	        	
	        	sm.put(pNo, 2, "" + new DecimalFormat("#####").format(173.7178 * p.rating_deviation_new), null, null);	        	
	        	Glicko2Table.highlightCell(sm, pNo, 2, Color.CYAN);
	        	sc.highlight(line+2);
	        	visGraph.updatePlayerDev(p); //UpdateTheGraph
	        	lang.nextStep();
	        	
	        	sc.unhighlight(line+2);
	        	sm.put(pNo, 3, "" + new DecimalFormat("#.#####").format(p.getVotality()), null, null);	        	
	        	Glicko2Table.highlightCell(sm, pNo, 3, Color.CYAN);
	        	sc.highlight(line+3);
	        	
	        	lang.nextStep();
	        	List<String> sumup = ActuallyUsableMultilineText.textFromFile("sumup." + ltag);
	        	sumup.add(t.translateMessage("wonMatches") + ": " + wins);
	        	sumup.add(t.translateMessage("drawMatches") + ": " + draws);
	        	sumup.add(t.translateMessage("lostMatches") + ": " + defeats);
	        	sumup.add("");
	        	sumup.add(t.translateMessage("ratingChange") + ": " + 
	        			new DecimalFormat("#####").format(players[pNo-1].getRating())
	        				+ " -> " + 
	        			new DecimalFormat("#####").format(173.7178 * p.rating_new + 1500));
	        	sumup.add(t.translateMessage("rdChange") + ": " + 
	        			new DecimalFormat("#####").format(players[pNo-1].getRatingDev())
	        				+ " -> " + 
	        			new DecimalFormat("#####").format(173.7178 * p.rating_deviation_new));
	        	sumup.add(t.translateMessage("volChange") + ": " + 
	        			new DecimalFormat("##.########").format(vol)
	        				+ " -> " + 
	        			new DecimalFormat("##.########").format(players[pNo-1].getVotality()));
	        	sumup.add("");
	        	
	        	ib.setText(sumup);
	        	step_display.setText(t.translateMessage("sumupOfPlayer") + pNo, null, null);
	        	
	        	sc.unhighlight(line);
	        	sc.unhighlight(line+3);
	        	
	        	lang.nextStep(t.translateMessage("sumupOfPlayer")+ pNo);
	        	
	        	Glicko2Table.unhighlightRow(sm, pNo);
	        	pNo++;
	        }
	        
	        for (Glicko2Player p : players) {
	        	p.updateRating();
	        }
        }
        lang.nextStep();
    	ib.setText(ActuallyUsableMultilineText.textFromFile("wrapup." + ltag));
    	step_display.setText(t.translateMessage("wrapup"), null, null);
    	
    	lang.nextStep(t.translateMessage("wrapup"));
        
    	lang.finalizeGeneration();
        return lang.toString();
    }

    public String getName() {
        return "Glicko-2";
    }

    public String getAlgorithmName() {
        return "Glicko-2";
    }

    public String getAnimationAuthor() {
        return "Maxim Kuznetsov, Fritz Beutel";
    }

    public String getDescription(){
        return t.translateMessage("description");
    }

    public String getCodeExample(){
        return CodeView.exampleFromFile("resources/glicko2/code.txt");
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return locale;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

}