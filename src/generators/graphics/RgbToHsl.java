package generators.graphics;
/*
 * Julien Clauter, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */


import translator.Translator;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import algoanim.primitives.Polyline;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import animal.variables.VariableRoles;

import java.awt.Color;
import java.awt.Font;


public class RgbToHsl implements Generator {
	
	public enum Supported_Locale
	{
		RGB2HSL_EN,
		RGB2HSL_DE
	}

	//static String RGB2HSL_RESOURCE_PATH = "res/strings/rgbToHsl";
	static boolean usesVariablesWindow = true;
	
    private Language lang;
    private Color color;
    
    private Locale locale;
    
    private Translator translator;
    
    private int randomNumThreshold;
    
    private Variables variables;


    /**
     * The header text including the headline
     */
    private Header                 header;
    private Header                 subtitle;
    private DescriptionField	   descriptionField;
    
    private GraphBord graphBoard;
    
    private ColorField lightnessColorField;
    private ColorField hueColorField;
    private ColorField endColorField;

    private ColorField rgbColorField;

    
    /***
     * General java source code animation object
     */
    private JavaSourceCodeField sourceCode;
    
    
    public RgbToHsl(Supported_Locale locale, String resourcePath){
    	super();
    	
    	switch(locale){
    	   case RGB2HSL_DE:
    		 this.locale = Locale.GERMANY;
    		 break;
    	   default:
    		 this.locale = Locale.US;
    	}
    	
    	this.translator = new Translator(resourcePath, this.locale);
    }
    
    public RgbToHsl(String resourcePath, Locale locale)
    {
    	super();
    	
    	if (Locale.GERMANY.equals(locale)) {
    		this.locale = locale;
    	}

    	if (Locale.US.equals(locale)) {
    		this.locale = locale;
    	}
    	
    	if (this.locale == null){
    		System.out.println("Used locale " + locale + " is not supported. (supported are GERMANY, US)");
    		this.locale = Locale.US;
    		System.out.println("Default locale is US.");
    	}
    	
    	this.translator = new Translator(resourcePath, this.locale);
    }
    
    public void init()
    {
        lang = new AnimalScript("RGB to HSL conversion [de]", "Julien Clauter", 900, 800);
        lang.setStepMode(true); // Schrittmodus aktivieren
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        //Timing defaultTiming = new TicksTiming(30);
    }
    
    
    /**
     * Converts the given RGB values into a HSL Color.
     * 
     * While calculating the HSL Color it animates and highlights the importants calculation steps.
     * 
     * @param red - int from 0 to 255
     * @param green - int from 0 to 255
     * @param blue - int from 0 to 255
     * @param scenario - the current scenario
     * @param defaultTiming - animation timing
     * @return returns an hsl color object
     */
    public ColorConverter.HSLColor RGB2HSL(int red,  int green,  int blue, Scenario scenario, Timing defaultTiming)
    {
		
    	double r = ((double)red) / 255.f;
		double g = ((double)green)/ 255.f;
		double b = ((double)blue) / 255.f;
		

	    if (usesVariablesWindow)
	    {
	    	String test = String.format("%.2f", r).replace(',', '.');
			this.variables.declare("double", "r", test , animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE) ); // "fixed value"
	        this.variables.declare("double", "g", String.format("%.2f", g).replace(',', '.'), animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE));
	        this.variables.declare("double", "b", String.format("%.2f", b).replace(',', '.'), animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE));
	    }
        
		this.sourceCode.showSourceCode("rgb2hsl", 1, 4, 0, defaultTiming);
		
		String descFormatInitStep_local =  this.translator.translateMessage("descFormatInitStep_local");/* "Als erstes normalisieren wir\ndie Farbwerte:"
				+ "\nRot: %d -> %.2f"
				+ "\nGrün: %d -> %.2f"
				+ "\nBlau: %d -> %.2f";*/
		String initDesc = String.format(descFormatInitStep_local,red,r,green,g,blue,b);
				
		this.subtitle.getHeaderText().setText("R: "+ color.getRed() + " (="+new DecimalFormat("#.##").format(r)+")" + " G: " + color.getGreen() + " (="+new DecimalFormat("#.##").format(g)+")" + " B: " + color.getBlue() + " (="+new DecimalFormat("#.##").format(b)+")", null, null);
    	
		this.descriptionField.setDescriptionText(initDesc , defaultTiming);
    	
		String contentFormatRGBnormalise_local = "RGB-Werte normalisieren.";
		scenario.nextStep(contentFormatRGBnormalise_local, null);
    	
	    
	    double h = 0;
	    double s = 0;
	    
		this.sourceCode.showSourceCode("rgb2hsl", 4, 4, 1, defaultTiming);
		
		String descFormatExtremata_local = this.translator.translateMessage("descFormatExtremata_local"); //"Als nächstes sucht man die Grenzen,\nalso das Minimum und das\nMaximum, die im Verlauf der Berechnung\nbenötigt werden.";
    	this.descriptionField.setDescriptionText(descFormatExtremata_local, defaultTiming);
    	
    	String contentFormatExtremata_local = this.translator.translateMessage("contentFormatExtremata_local"); //"Extrema bestimmen";
    	scenario.nextStep(contentFormatExtremata_local, null);
		
		// FIND MIN / MAX
		double max = Math.max(r, Math.max(g, b));
	    double min = Math.min(r, Math.min(g, b));
	    
	    if (usesVariablesWindow)
	    {
	        this.variables.declare("double", "max", String.format("%.2f", max).replace(',', '.'), animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE));
	        this.variables.declare("double", "min", String.format("%.2f", min).replace(',', '.'), animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE));
	    }


	    
	    String colorNameBlue_local = this.translator.translateMessage("colorNameBlue_local");// "Blau";
	    String colorNameRed_local = this.translator.translateMessage("colorNameRed_local");// "Rot";
	    String colorNameGreen_local = this.translator.translateMessage("colorNameGreen_local");//  "Grün";
	    
	    String maxTitle = colorNameBlue_local;
	    String minTitle = colorNameBlue_local;
	    
	    String midTitle = colorNameBlue_local;
	    	    
	    if (r == max){
	    	maxTitle = colorNameRed_local;
	    } else if(g == max){
	    	maxTitle = colorNameGreen_local;
	    } 
	    if (r == min){
	    	minTitle = colorNameRed_local;
	    	if (g != max){
	    		midTitle = colorNameGreen_local;
	    	}
	    } else if(b != min){
	    	minTitle = colorNameGreen_local;
	    	if (r != max){
	    		midTitle = colorNameRed_local;
	    	}
	    } else {
	    	// blau == min
	    	if (r == max){
	    		midTitle = colorNameGreen_local;
	    	} else {
	    		midTitle = colorNameRed_local;
	    	}
	    }
	    
	    String descFormatHSLspace_local =  this.translator.translateMessage("descFormatHSLspace_local");/* "In diesem Fall ist %s das Maximum (%.2f)"
	    	    + "\nund %s das Minimum (%.2f)."
	    	    + "\nBei Gleichstand wird die Reihenfolge R, G, B\neingehalten."
	    	    + "\nDiese Extremwerte entsprechen dem Y-Wert"
	    	    + "\nder jeweiligen Hoch- bzw. Tiefpunkt der"
	    	    + "\ngefärbten Linien im Graphen."
	    	    + "\nDer Graph zeigt das Verhalten zwischen"
	    	    + "\nRot, Grün und Blau beim Durchlaufen aller "
	    	    + "\nWerte auf dem Rand des HSL Farbraums."
	    	    + "\nIm Gegensatz zu RGB ist der HSL Farbraum "
	    	    + "\nein Zylinder, auf dessen Rand alle knalligen"
	    	    + "\n(grau-losen) Farben liegen."; */
	    
	    String desc = String.format(descFormatHSLspace_local, maxTitle, max, minTitle ,min);
	    
    	this.descriptionField.setDescriptionText(desc, defaultTiming);

		GraphBord gb = this.setupGraphBord(lang, new Coordinates(30, 80), new Size(350, 200), min, max, defaultTiming);
		scenario.addScenarioObject(gb);
		this.graphBoard = gb;

		
	    int randomNum = (int) (Math.random() * 100)+1;
	    if (randomNum >= this.randomNumThreshold){
			this.multipleChoiceQuestionForHUEInterval(r,g,b, scenario.language);
	    }

    	
		String contentFormatHSLspace_local = this.translator.translateMessage("contentFormatHSLspace_local");//  "Der Rand des HSL Raums als Graphik.";
		scenario.nextStep(contentFormatHSLspace_local, null);

	    // CALCULATE L
	    double l = (max + min) / 2.f;
	    
	    if (usesVariablesWindow){
			this.variables.declare("double", "l", String.format("%.2f", l).replace(',', '.'), animal.variables.Variable.getRoleString(VariableRoles.MOST_WANTED_HOLDER));
	    }
		
	    // Show H in Graph
	    this.setupLuminanceLine(scenario.language, gb, l, defaultTiming);
	    this.lightnessColorField = new ColorField(lang, new Coordinates(450,450 ), new Coordinates(500,500), "H=0,\nS=0,\nL="+new DecimalFormat("#.##").format(l), new ColorConverter.HSLColor(0,0,l).toColor());
        
	    String descFormatLuminance_local =  this.translator.translateMessage("descFormatLuminance_local");/*"Die Luminance / Leuchtkraft beträgt %.2f"
	        	+ ",\ndargestellt als orange Linie."
	        	+ "\nDies berechnet sich durch die Formel"
	        	+ "\nL = (max + min) / 2";*/
    	desc = String.format(descFormatLuminance_local, l);
    	this.descriptionField.setDescriptionText(desc, defaultTiming);
    	
		this.sourceCode.showSourceCode("rgb2hsl", 11, 3, 1, defaultTiming);
		
		String contentFormatLuminance_local = this.translator.translateMessage("contentFormatLuminance_local");// "Luminance / Leuchtkraft";
	   
		randomNum = (int) (Math.random() * 100)+1;
	    if (randomNum >= this.randomNumThreshold){
			this.luminanceResponsabilityMutlipleChoice(scenario.language);
	    }

	    scenario.nextStep(contentFormatLuminance_local, null);
		
	    String descFormatGrayCheck_local = this.translator.translateMessage("descFormatGrayCheck_local");/* "Nun prüfen wir, ob es sich"
	    		+ "\nbei der gegebene Farbe um einen Grauton"
	    		+ "\nhandelt. Dies ist genau dann der Fall,"
	    		+ "\nwenn alle Werte identisch sind.";*/
	    desc = descFormatGrayCheck_local; 
    	this.descriptionField.setDescriptionText(desc, defaultTiming);
    	
    	this.sourceCode.showSourceCode("rgb2hsl", 17, 14, 0, defaultTiming);
    	
    	String contentFormatGrayCheck_local = this.translator.translateMessage("contentFormatGrayCheck_local");//"Grauton Prüfung";
    	scenario.nextStep(contentFormatGrayCheck_local, null);
	    
    	
    	// SET H
    	desc = this.getHueDescription(r,g,b, min, max , minTitle, midTitle, maxTitle);
    	
    	colorNameRed_local = this.translator.translateMessage("colorNameRed_local");// "Rot";
    	colorNameGreen_local = this.translator.translateMessage("colorNameGreen_local");//"Grün";
    	colorNameBlue_local =this.translator.translateMessage("colorNameBlue_local");// "Blau";
    	
    	String contentFormatHueCalculation_local = this.translator.translateMessage("contentFormatHueCalculation_local");//"Hue Berechnung";

    	
	    if (max == min)
	    {
	    	this.descriptionField.setDescriptionText(desc, defaultTiming);
	    	h = 0;
	    	this.sourceCode.showSourceCode("rgb2hsl", 17, 14, 1, defaultTiming);
		    if (usesVariablesWindow){
				this.variables.declare("double", "h", String.format("%.2f", h).replace(',', '.'), animal.variables.Variable.getRoleString(VariableRoles.MOST_WANTED_HOLDER));
		    }
	    	String contentFormatHueNotImportant_local = this.translator.translateMessage("contentFormatHueNotImportant_local");//"Hue ohne Auswirkung - Grau";
	    	scenario.nextStep(contentFormatHueNotImportant_local, null);
	    } else {
	    	int originX = 0;
	    	
	    	this.descriptionField.setDescriptionText(desc, defaultTiming);
	    	this.sourceCode.showSourceCode("rgb2hsl", 17, 14, 2, defaultTiming);
	    	//scenario.nextStep(contentFormatHueCalculation_local, null);
			// MAX = R
		    if (r == max)
		    {
		    	if (g > b){
		    		originX = 300;
		    	} else {
		    		originX = 0;
		    	}
		    	//this.setupHighlightingRectForColorField(lang, gb, originX);
		    	
		    	//this.descriptionField.setDescriptionText(desc, defaultTiming);
			    scenario.nextStep(String.format("Max = %s: Hue in [-60, 60]", colorNameRed_local), null);

		    	h = 60.0f * (0.0f + ((g - b)/(max - min))); // h is now in [-60, 60]
			    this.sourceCode.showSourceCode("rgb2hsl", 17, 14, 4, defaultTiming);
		    	
			    if (usesVariablesWindow){
					this.variables.declare("double", "h", String.format("%.2f", h).replace(',', '.'), animal.variables.Variable.getRoleString(VariableRoles.MOST_WANTED_HOLDER));
			    }
			    
			    scenario.nextStep(contentFormatHueCalculation_local, null);
		    
		    } else {
		    	
		    	
		    	//this.sourceCode.showSourceCode("rgb2hsl", 17, 14, 6, defaultTiming);
		    	//scenario.nextStep("", null);
			    // MAX = G
			    if (g == max)
			    {
			    	
			    	if (b > r){
			    		originX = 60;
			    	} else {
			    		originX = 120;
			    	}
			    	//this.setupHighlightingRectForColorField(lang, gb, originX);
			    	
				    scenario.nextStep(String.format("Max = %s: Hue in [60, 180]", colorNameGreen_local), null);
			    	h = 60.0f * (2.0f + ((b - r)/(max - min))); // h is now in [60, 180]
			    	this.sourceCode.showSourceCode("rgb2hsl", 17, 14, 7, defaultTiming);
			    	
				    if (usesVariablesWindow){
						this.variables.declare("double", "h", String.format("%.2f", h).replace(',', '.'), animal.variables.Variable.getRoleString(VariableRoles.MOST_WANTED_HOLDER));
				    }
			    	
			    	scenario.nextStep(contentFormatHueCalculation_local, null);
			    
				// MAX = B -> at this point, this should be always true
			    } else if (b == max) {
			    	
			    	if (b > r){
			    		originX = 180;
			    	} else {
			    		originX = 240;
			    	}
			    	
			    	//this.setupHighlightingRectForColorField(lang, gb, originX);

			    	this.sourceCode.showSourceCode("rgb2hsl", 17, 14, 9, defaultTiming);
			    	scenario.nextStep(String.format("Max = %s: Hue in [180, 300]", colorNameBlue_local), null);
			    	
					
				    h = 60.0f * (4.0f + ((r - g)/(max - min))); // h is now in [180, 300]
				    
				    if (usesVariablesWindow){
						this.variables.declare("double", "h", String.format("%.2f", h).replace(',', '.'), "TEMPORARY");
				    }
				    
			    	this.sourceCode.showSourceCode("rgb2hsl", 17, 14, 10, defaultTiming);
			    	
				    scenario.nextStep(contentFormatHueCalculation_local, null);
			    }
		    }
	    }
	    
    	String descFormatHueValue_local =this.translator.translateMessage("descFormatHueValue_local");// "Hue beträgt %d° . \nWir müssen diesen Wert nur noch\nnormalisieren.";
	    desc = String.format(descFormatHueValue_local, (int)h); 
    	this.descriptionField.setDescriptionText(desc, defaultTiming);
    	
    	this.sourceCode.showSourceCode("rgb2hsl", 37, 5, 100, defaultTiming);
		scenario.nextStep(String.format("Hue = %d °", (int)h), null);
	    // definition: h is in set of [0, 1] defined
	    if (h < 0) 
	    {
	    	h+=360.f;
	    }
	    h = (double) h / 360.0f;
	    
	    if (usesVariablesWindow){
			this.variables.declare("double", "h", String.format("%.2f", h).replace(',', '.'), animal.variables.Variable.getRoleString(VariableRoles.MOST_WANTED_HOLDER));
	    }
	    
	    this.hueColorField = new ColorField(lang, new Coordinates(520, 450), new Coordinates(570, 500), "H="+new DecimalFormat("#.##").format(h)+",\nS=1,\nL="+new DecimalFormat("#.##").format(l), new ColorConverter.HSLColor(h,1,l).toColor());
	    
	    
	    // Show H in Graph
	    String descFormatNormalisedHue_local =this.translator.translateMessage("descFormatNormalisedHue_local");/* "Der normalisierte Wert von H lautet:\n%.2f = %d°"
	    		+ "\nIm Graphen sehen wir, dass Hue"
	    		+ "\nden Winkel eines Farbwertes im"
	    		+ "\nHSL Farbraum repräsentiert.";*/
    	desc = String.format(descFormatNormalisedHue_local, h, (int)(h*360)); 
    	this.descriptionField.setDescriptionText(desc, defaultTiming);
	    
    	this.setupHueLine(scenario.language, gb, h, defaultTiming);
    	
    	String contentFormatNormalisedHue_local = this.translator.translateMessage("contentFormatNormalisedHue_local");//"Der normalisierter Wert von Hue beträgt %.2f°";
	    scenario.nextStep(String.format(contentFormatNormalisedHue_local, h), null);
	    
	    
    	this.sourceCode.showSourceCode("rgb2hsl", 41, 9, 0, defaultTiming);
	    
    	String descFormatSaturationInitial_local =this.translator.translateMessage("descFormatSaturationInitial_local");/*"Nun fehlt nur noch die Saturation / Sättigung."
    			+ "\nDie Saturation beschreibt wie stark"
    			+ "\nder Farbwert von Grau abweicht."
    			+ "\nDemnach ist S abhängig vom Abstand"
    			+ "\ndes min und max Wertes."
    			+ "\n"
    			+ "\nZunächst prüfen wir, ob es sich"
    			+ "\nbei der gegebenen Farbe um Schwarz"
    			+ "\noder Weis handelt.";*/
    	desc = descFormatSaturationInitial_local;
    	this.descriptionField.setDescriptionText(desc, defaultTiming);
    	
    	randomNum = (int) (Math.random() * 100)+1;
	    if (randomNum >= this.randomNumThreshold){
			this.saturationResponsabilityMutlipleChoice(scenario.language);
	    }

    	
    	String contentFormatStaturationInitial_local =this.translator.translateMessage("contentFormatStaturationInitial_local");// "Sättigung / Saturation";
	    scenario.nextStep(contentFormatStaturationInitial_local, null);
	    // SET S
	    // R = G = B = 0 oder 1 -> S hat keine Bedeutung, wir definieren es als 0.
	    if ((max == 0) || (min == 1)){
	    	s = 0;
	    	
		    if (usesVariablesWindow){
				this.variables.declare("double", "s", String.format("%.2f", s).replace(',', '.'), animal.variables.Variable.getRoleString(VariableRoles.MOST_WANTED_HOLDER));
		    }
	    	
	    	String colorNameWhite_local = this.translator.translateMessage("colorNameWhite_local");//"Weis";
	    	String colorNameBlack_local = this.translator.translateMessage("colorNameBlack_local");//"Schwarz";
	    	String upper_local = this.translator.translateMessage("upper_local");//"oberstem";
	    	String lowest_local = this.translator.translateMessage("lowest_local");//"unterstem";
	    	
	    	String colorName = colorNameWhite_local;
	    	String centricDesc = upper_local;
	    	if (max == 0) {
	    		colorName = colorNameBlack_local;
		        centricDesc = lowest_local;
	    	}
	    	
	    	// max, centricDesc, colorName
	    	String descFormatBlackAndWhite_local = this.translator.translateMessage("descFormatBlackAndWhite_local");/*"Da alle Werte gleich %.2f sind,"
	    			+ "\nliegt des HSL-Wert im %s Zentrum des"
	    			+ "\nHSL-Farbraums."
	    			+ "\nEs handelt sich also um %s"
	    			+ "\nDaher setzten wir S = 0.";*/
	    	
	    	desc = String.format(descFormatBlackAndWhite_local, max, centricDesc, colorName ); 
	    	this.descriptionField.setDescriptionText(desc, defaultTiming);
	    	
	    	this.sourceCode.showSourceCode("rgb2hsl", 41, 9, 1, defaultTiming);
	    	
	    	String contentTitleFormatBlackNWhite_local = this.translator.translateMessage("contentTitleFormatBlackNWhite_local");//"Die Farbe %s";
		    scenario.nextStep(String.format(contentTitleFormatBlackNWhite_local, colorName), null);
	    } 
	    else
	    {
	    	String descFormatSaturationCalulation_local = this.translator.translateMessage("descFormatSaturationCalulation_local");/*"Die gegebene Farbe ist weder schwarz"
	    			+ "\nnoch weis."
	    			+ "\nDie Formel zur Berechnung von S lautet:"
	    			+ "\ns = (max - min) / (1 - |max + min - 1|)";*/
	    	desc = descFormatSaturationCalulation_local;
	    			
	    	this.descriptionField.setDescriptionText(desc, defaultTiming);
	    	this.sourceCode.showSourceCode("rgb2hsl", 41, 9, 3, defaultTiming);
		    
	    	String contentTitleFormatSaturationFormal_local = this.translator.translateMessage("contentTitleFormatSaturationFormal_local");// "Saturation Formel";
	    	scenario.nextStep(contentTitleFormatSaturationFormal_local, null);
		    
	    	s = (max - min) / (1.0f - Math.abs(max+min-1.0f));
	    	
		    if (usesVariablesWindow){
				this.variables.declare("double", "s", String.format("%.2f", s).replace(',', '.'), animal.variables.Variable.getRoleString(VariableRoles.MOST_WANTED_HOLDER));
				this.variables.setRole("s", animal.variables.Variable.getRoleString(VariableRoles.MOST_WANTED_HOLDER));
		    }
	    }
	    
	    this.endColorField = new ColorField(lang, new Coordinates(590, 450), new Coordinates(640, 500), "H="+new DecimalFormat("#.##").format(h)+",\nS="+new DecimalFormat("#.##").format(s)+",\nL="+new DecimalFormat("#.##").format(l), new ColorConverter.HSLColor(h,s,l).toColor());
	    
	    String descFormatFinalHSL_local = this.translator.translateMessage("descFormatFinalHSL_local");/*"Die HSL-Farbe hat also die Werte"
    			+ "\nH = %.2f (= %d°),"
    			+ "\nS = %.2f und"
    			+ "\nL = %.2f.";*/
	    
    	desc = String.format(descFormatFinalHSL_local, h, (int)(h*360),s,l);
    	
    	this.descriptionField.setDescriptionText(desc, defaultTiming);
    	
    	//this.sourceCode.showSourceCode("rgb2hsl", 37, 14, 5, defaultTiming);
    	
    	this.sourceCode.showSourceCode("rgb2hsl", 49, 2, 6, defaultTiming);
    	
    	String contentTitleFinalFormat_local = this.translator.translateMessage("contentTitleFinalFormat_local");//"Unser HSL Wert (%.2f; %.2f; %.2f; )";
	    scenario.nextStep(String.format(contentTitleFinalFormat_local, h,s,l), null);
	    
	    return new ColorConverter.HSLColor(h,s,l);
    }

    
    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) 
    {
    	// null all private primitives to avoid dirty caching
    	
    	this.header = null;
    	this.descriptionField = null;
    	this.sourceCode = null;
    	this.subtitle = null;
    	
    	this.lightnessColorField = null;
    	this.endColorField = null;
    	this.hueColorField = null;
    	
    	TextProperties headerTitleProps = (TextProperties) props.getPropertiesByName("Header Title");
    	TextProperties subTitleProps = (TextProperties) props.getPropertiesByName("Subtitle");

    	TextProperties descProps = (TextProperties) props.getPropertiesByName("Description Field");

    	SourceCodeProperties srcProps = (SourceCodeProperties) props.getPropertiesByName("Source Code");

    	
    	color = (Color)primitives.get("RGB Color");
    	Integer randomNumThresholdUserDef = (Integer)primitives.get("Question Threshold");
        
    	if (randomNumThresholdUserDef != null){
    		this.randomNumThreshold = 101 - randomNumThresholdUserDef.intValue();
    		this.randomNumThreshold = Math.max(this.randomNumThreshold, 1);
    		this.randomNumThreshold = Math.min(this.randomNumThreshold, 101);
    	} else {
    		this.randomNumThreshold = 50;
    	}
    	
        if (color == null){
        	color = new Color(224, 160, 215);
        }
        
        Timing defaultTiming = new TicksTiming(30);
        
        
        int initialRow = 0;
        int maxRows = 14;
        int highlightedScope = 0;
        String methodName = "rgb2hsl";
        this.sourceCode = new JavaSourceCodeField(lang, new Coordinates(15, 320), this.getCodeExample() , methodName , initialRow , maxRows, highlightedScope, defaultTiming);
        Scenario scene = new Scenario(lang, this.sourceCode);
    	this.sourceCode.showSourceCode("rgb2hsl", 0, 0, 0, defaultTiming);

    	Color srcHighlightColor = (Color) srcProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY);
    	Color srcTextColor = (Color) srcProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);
    	Font srcFont = (Font) srcProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
    	this.sourceCode.setTextColor(srcTextColor);
    	this.sourceCode.setFont(new Font(srcFont.getName(),Font.PLAIN, Math.max(12, srcFont.getSize())));
        this.sourceCode.setHighlightColor(srcHighlightColor);
    	
		Coordinates headerCoord = new Coordinates(15, 15);

        this.setupHeader(lang, headerCoord, null, null, headerTitleProps);
        
        String title_local = this.translator.translateMessage("title_local"); //"RGB-Farbwert zu HSL-Farbwert";
        this.header.getHeaderText().setText(title_local, null, defaultTiming);
        
        scene.addScenarioObject(this.header);
        
        Coordinates subtitleCoords = new Coordinates(15,30);
        this.setupSubtitle(lang, subtitleCoords , "", null, subTitleProps);
        
        this.subtitle.getHeaderText().setText("R: "+ color.getRed() + " G: " + color.getGreen() + " B: " + color.getBlue(), null, defaultTiming);

        scene.addScenarioObject(this.subtitle);

		String rgbColorFieldText =  "R="+color.getRed()+",\nG="+color.getGreen()+",\nB="+color.getBlue();
		String descFormatInitialSetup_local = this.translator.translateMessage("descFormatInitialSetup_local");/*"Im folgendem Code-Beispiel wird"
				+ "\ndie RGB-Farbe, mit folgenden Werten"
				+ "\nin eine HSL Farbe umgewandelt.";*/
        this.setupDescriptionField(scene.language, new Coordinates(440,50), descFormatInitialSetup_local + "\n" + rgbColorFieldText, defaultTiming, descProps);
        scene.addScenarioObject(this.descriptionField);
        
		Coordinates rgbColorFieldCoord = new Coordinates(headerCoord.getX() , subtitleCoords.getY() + 40);
		this.rgbColorField = new ColorField(lang, rgbColorFieldCoord, new Coordinates(rgbColorFieldCoord.getX()+50,rgbColorFieldCoord.getY()+50), rgbColorFieldText, color);

        String contentSetup_local = this.translator.translateMessage("contentSetup_local");//"Setup";
        lang.nextStep(contentSetup_local);
        
    	if (usesVariablesWindow){
    		this.variables = lang.newVariables();
            this.variables.declare("int", "red", ""+color.getRed(), animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE));
            this.variables.declare("int", "green", ""+color.getGreen(), animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE));
            this.variables.declare("int", "blue", ""+color.getBlue(), animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE));
    	}

        this.rgbColorField.hide();
        
        this.RGB2HSL(color.getRed(), color.getGreen(), color.getBlue(), scene, defaultTiming );
        
        
        // fazit
		this.sourceCode.hide();
		
		String descFormatFazit_local = this.translator.translateMessage("descFormatFazit_local");/* "Im Laufe der Berechnung, wurden"
				+ "\n3 farbige Kästchen erstellt."
				+ "\nDiese zeigen nochmal explizit"
				+ "\ndie Bedeutung der einzelnen Werte"
				+ "\nHue, Saturation und Luminance."
				+ "\n"
				+ "\nIm RGB Farbraum bestimmen alle 3 Werte"
				+ "\nR, G und B die Helligkeit, den Farbverlauf"
				+ "\nund die Sättigung."
				+ "\nIm HSL Farbraum steht allein der Wert"
				+ "\nH für den Farbverlauf, S für die Söttigung"
				+ "\nund L für die Helligkeit / Leuchtkraft."
				+ "\nDie Prioritäten beider Farbräume sind"
				+ "\nalso stark unterschiedlich.";*/
		
		String fazitDesc = descFormatFazit_local;
				    	
		
		int distBetweencolorFields =  (this.hueColorField.getUpperleft().getX() - this.lightnessColorField.getUpperleft().getX());
		this.descriptionField.moveBy("translate", -(this.descriptionField.getUpperleft().getX() - headerCoord.getX()) , 30, null, null);
		this.descriptionField.setDescriptionText(fazitDesc , defaultTiming);

		this.graphBoard.hide();
		
		this.lightnessColorField.moveBy("translate",  -(this.lightnessColorField.getUpperleft().getX() - headerCoord.getX() ) , 0, null, defaultTiming);
		this.hueColorField.moveBy("translate",  -(this.hueColorField.getUpperleft().getX() - headerCoord.getX() ) + distBetweencolorFields,0, null, defaultTiming);

		
		int endColorFieldMoveDist_X = -(this.endColorField.getUpperleft().getX() - headerCoord.getX() ) + (2*distBetweencolorFields);
		int endColorField_X = this.endColorField.getUpperleft().getX() + endColorFieldMoveDist_X;
		this.endColorField.moveBy("translate", endColorFieldMoveDist_X  , 0 , null, defaultTiming);
		
		
	    
		rgbColorFieldCoord = new Coordinates(endColorField_X + distBetweencolorFields + 40 + 100, this.endColorField.getUpperleft().getY());
		this.rgbColorField = new ColorField(lang, rgbColorFieldCoord, new Coordinates(rgbColorFieldCoord.getX()+50,rgbColorFieldCoord.getY()+50), rgbColorFieldText, color);

		
		String contentFormatFazit_local = this.translator.translateMessage("contentFormatFazit_local");//"Fazit";
        scene.nextStep(contentFormatFazit_local, null);
        
        //
        
        lang.finalizeGeneration();
        String languageString = lang.toString();
        // TEST IT
        //System.out.println(languageString);
        
        return languageString;
    }
    
    
    
    
    
    public String getName() 
    {
    	String genName_local = this.translator.translateMessage("genName_local");//"RGB to HSL conversion [de]";
        return genName_local;
    }

    public String getAlgorithmName() 
    {
    	String algoAnimName_local = this.translator.translateMessage("algoAnimName_local");//"RGB to HSL conversion";
        return algoAnimName_local;
    }

    public String getAnimationAuthor() 
    {
        return "Julien Clauter";
    }

    public String getDescription()
    {
    	String generalAlgoDesc_local = this.translator.translateMessage("generalAlgoDesc_local");/*"Algorithmus zur Berechnung eines HSL Farbwertes anhand eines RGB Farbwertes."
        		+ "\n"
        		+ "\nDer HSL-Farbraum ist der Farbraum, bei dem man die Farbe mit Hilfe"
        		+ "\ndes Farbwerts (hue),"
        		+ "\ndas Farbsättigung (saturation) und "
        		+ "\nder Helligkeit (lightness) definiert."
        		+ "\n"
        		+ "\nHue beschreibt dabei den Farbwinkel auf dem Farbkreis (etwa 0° für Rot, 120° für Grün usw.). "
        		+ "Die Sättigung S gibt in Prozent an, wie stark eine Farbe gesättigt ist. Der Wert 0 (%) steht dabei für Neutralgrau und 100 (%) für eine gesättigte, reine Farbe. "
        		+ "Die Lightness L hingegeben beschreibt, wie stark eine Farbe leuchtet. Ist der Wert von L 0 so ist die Farbe schwarz, ist der Wert 1 so ist die Farbe weis. "
        		+ "\n"
        		+ "\nDiese Animation erklärt, wie man vom typischen RGB Farbraum zum HSL Farbraum umrechnet und welche Aufgaben die Bestandteile H, S und L besitzten.";*/
        return generalAlgoDesc_local;
    }

    public String getCodeExample()
    {
        return 
        		"public Color rgb2hsl(int red, int green, int blue){\n"
        		+"\tdouble r = ((double)red)  / 255.f;\n"
        		+"\tdouble g = ((double)green)/ 255.f;\n"
        		+"\tdouble b = ((double)blue) / 255.f;\n"
        		+"\t\n"
        		+"\t// min / max\n"
        		+"\tdouble max = Math.max(r, Math.max(g, b));\n"
        		+"\tdouble min = Math.min(r, Math.min(g, b));\n"
        		+"\t\n"
        		+"\tdouble h = 0;\n"
        		+"\tdouble s = 0;\n"
        		+"\t\n"
        		+"\t// Luminance\n"
        		+"\tdouble l = (max + min) / 2.f;\n"
        		+"\t\n"
        		+"\t// Hue\n"
        		+"\t// R = G = B\n"
        		+"\tif (max == min){\n"
        		+"\t\th = 0;\n"
        		+"\t} else {\n"
        		+"\t\t// MAX = R\n"
        		+"\t\tif (r == max) {\n"
        		+"\t\t\th = 60 * (0 + ((g - b)/(max - min)));\n"
        		+"\t\t} else {\n"
        		+"\t\t\t// MAX = G\n"
        		+"\t\t\tif (g == max) {\n"
        		+"\t\t\t\th = 60 * (2 + ((b - r)/(max - min)));\n"
        		+"\t\t\t} else {\n"
        		+"\t\t\t\t// MAX = B\n"
        		+"\t\t\t\tif (b == max) {\n"
        		+"\t\t\t\t\th = 60 * (4 + ((r - g)/(max - min)));\n"
        		+"\t\t\t\t}\n"
        		+"\t\t\t}\n"
        		+"\t\t}\n"
        		+"\t}\n"
        		+"\t\n"
        		+"\t// h in [0, 1]\n"
        		+"\tif (h < 0) {\n"
        		+"\t\th+=360.f;\n"
        		+"\t}\n"
        		+"\th = (double) h / 360.0f;\n"
        		+"\t\n"
        		+"\t// Saturation\n"
        		+"\t// R = G = B\n"
        		+"\tif ((max == 0) || (min == 1)){\n"
        		+"\t\ts = 0;\n"
        		+"\t} else {\n"
        		+"\t\ts = (max - min) / (1 - Math.abs(max+min-1));\n"
        		+"\t}\n"
        		+"\t\n"
        		+"\treturn new HSLColor(h,s,l);\n"
        		+"}";
    }

    public String getFileExtension()
    {
        return "asu";
    }

    public Locale getContentLocale() 
    {
        return this.locale; // Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() 
    {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
    }

    public String getOutputLanguage() 
    {
        return Generator.JAVA_OUTPUT;
    }
    
    
    /*
     * PRIVATE UI SETUP METHODS
     */
    
    
    /**
     *  Creates a Multi Choice Question.
     *  
     *  User will be asked: in which interval the hue is located.
     * 
     * @param r , double red
     * @param g , double green
     * @param b , double b
     * @param language
     */
    private void multipleChoiceQuestionForHUEInterval(double r, double g, double b, Language language)
    {
    
    	double max = Math.max(g, r);
		max = Math.max(b, max);
		
		String questionHUEString_local = this.translator.translateMessage("questionHUEString_local");// "Im welchem Interval liegt Hue?";
				
		String answerHUEA_local = this.translator.translateMessage("answerHUEA_local");// "Hue ist in [-60 , 60]";
		String answerHUEB_local = this.translator.translateMessage("answerHUEB_local");// "Hue ist in [60 , 180]";
		String answerHUEC_local = this.translator.translateMessage("answerHUEC_local");// "Hue ist in [180 , 300]";
		String answerHUED_local = this.translator.translateMessage("answerHUED_local");//  "Hue ist genau 0";
		
		String[] answers = {answerHUEA_local, answerHUEB_local, answerHUEC_local, answerHUED_local};
		int correctAnswerIndex = 0;
		
		if (r == g && g == b){
			correctAnswerIndex = 3;
		} else if (g == max) {
			correctAnswerIndex = 1;
		} else if (b == max){
			correctAnswerIndex = 2;
		}
		
		String falseFeedbackHUEFormat_local = this.translator.translateMessage("falseFeedbackHUEFormat_local");/*"Falsch. Die Richtige Antwort lautet: %s"
				+"\nUm die richtige Antwort zu finden,"
				+"\nmuss man sich nur die Grafik anschauen."
				+"\nAnhand der 3 Linien für Rot, Grün und Blau, lässt"
				+"\nsich erkennen, wo Hue liegen muss.";*/
		
		String falseFeedback = String.format(falseFeedbackHUEFormat_local, answers[correctAnswerIndex]);
		
		String trueFeedbackHUEFormat_local = this.translator.translateMessage("trueFeedbackHUEFormat_local");//"Richtig! Für Hue gilt: %s";
		String trueFeeback = String.format(trueFeedbackHUEFormat_local, answers[correctAnswerIndex]) ;

		
		MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel("hue_interval_question");
		
		for (int index = 0; index < answers.length; index++)
		{
			
			String answerText = answers[index];
			
			if (index == correctAnswerIndex)
			{
				question.addAnswer(answerText, 1, trueFeeback);
			} else {
				question.addAnswer(answerText, 0, falseFeedback);
			}
		}
		
		question.setPrompt(questionHUEString_local);

		language.addMCQuestion(question);
    }
    
    private void luminanceResponsabilityMutlipleChoice(Language language)
        {
		
		String questionLuminanceString_local = this.translator.translateMessage("questionLuminanceString_local");//"Was repräsentiert die Luminance eines Farbwertes im HSL Farbraum?";
				
		String answerLuminanceA_local = this.translator.translateMessage("answerLuminanceA_local");//"Den Radius";
		String answerLuminanceB_local = this.translator.translateMessage("answerLuminanceB_local");//"Den Winkel";
		String answerLuminanceC_local = this.translator.translateMessage("answerLuminanceC_local");//"Die Höhe";
		
		String[] answers = {answerLuminanceA_local, answerLuminanceB_local, answerLuminanceC_local};
		int correctAnswerIndex = 2;
		
		
		String falseFeedbackLuminanceFormat_local = this.translator.translateMessage("falseFeedbackLuminanceFormat_local");/*"Falsch. Die Richtige Antwort lautet: %s"
				+"\nDer HSL Farbraum ist im Gegensatz zum"
				+ "\nRGB Farbraum ein aufrecht stehender"
				+ "\nZylinder. Im oberen Bereich sind helle"
				+ "\nFarben und im unteren dunkle."
				+ "Luminance steht für die Helligkeit"
				+ "\nund somit die Höhe eines Farbwertes.";*/
		
		String falseFeedback = String.format(falseFeedbackLuminanceFormat_local, answers[correctAnswerIndex]);
		
		String trueFeedbackLuminanceFormat_local = this.translator.translateMessage("trueFeedbackLuminanceFormat_local");//"Richtig! Luminance repräsentiert: %s";
		String trueFeeback = String.format(trueFeedbackLuminanceFormat_local, answers[correctAnswerIndex]) ;

		
		MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel("luminance_question");
		
		for (int index = 0; index < answers.length; index++)
		{			
			String answerText = answers[index];
			
			if (index == correctAnswerIndex)
			{
				question.addAnswer(answerText, 1, trueFeeback);
			} else {
				question.addAnswer(answerText, 0, falseFeedback);
			}
		}
		
		question.setPrompt(questionLuminanceString_local);

		language.addMCQuestion(question);
    }
    
    
    private void saturationResponsabilityMutlipleChoice(Language language)
    {
	
	String questionSaturationString_local = this.translator.translateMessage("questionSaturationString_local");//"Was repräsentiert die Saturation eines Farbwertes im HSL Farbraum?";
			
	String answerSaturationA_local = this.translator.translateMessage("answerSaturationA_local");//"Den Radius";
	String answerSaturationB_local = this.translator.translateMessage("answerSaturationB_local");// "Den Winkel";
	String answerSaturationC_local = this.translator.translateMessage("answerSaturationC_local");//"Die Höhe";
	
	String[] answers = {answerSaturationA_local, answerSaturationB_local, answerSaturationC_local};
	int correctAnswerIndex = 0;
	
	
	String falseFeedbackSaturationFormat_local = this.translator.translateMessage("falseFeedbackSaturationFormat_local");/*"Falsch. Die Richtige Antwort lautet: %s"
			+"\nDer HSL Farbraum ist im Gegensatz zum"
			+ "\nRGB Farbraum ein aufrecht stehender"
			+ "\nZylinder. Auf dem Rand des Zylinders"
			+ "\nliegen die stark gesättigten Farben."
			+ "\nIm Kern des Zylinders sind die"
			+ "\nGrautöne. Somit steht die Saturation"
			+ "\nfür den Radius eines Farbwertes.";*/
	
	String falseFeedback = String.format(falseFeedbackSaturationFormat_local, answers[correctAnswerIndex]);
	
	String trueFeedbackSaturationFormat_local = this.translator.translateMessage("trueFeedbackSaturationFormat_local");//"Richtig! Saturation repräsentiert: %s";
	String trueFeeback = String.format(trueFeedbackSaturationFormat_local, answers[correctAnswerIndex]) ;

	
	MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel("saturation_question");
	
	for (int index = 0; index < answers.length; index++)
	{		
		String answerText = answers[index];
		
		if (index == correctAnswerIndex)
		{
			question.addAnswer(answerText, 1, trueFeeback);
		} else {
			question.addAnswer(answerText, 0, falseFeedback);
		}
	}
	
	question.setPrompt(questionSaturationString_local);

	language.addMCQuestion(question);
}
    
    
    
    /*
     * 
     * @param language - the language
     * @param gb - the graph bord
     * @param originX - x-Coordinate
     */
    /*
    private void setupHighlightingRectForColorField(Language language, GraphBord gb, int originX )
    {
    	Node upperLeft = new Coordinates(gb.getUpperLeft().getX() + originX, gb.getUpperLeft().getY() + gb.getSize().getHeight());
    	Node lowerRight = new Coordinates(gb.getUpperLeft().getX() + originX + 60, gb.getUpperLeft().getY());
    	
    }
    */
    
    /**
     * Calculates the Hue description.
     * 
     * @param r - double from 0 to 1
     * @param g - double from 0 to 1
     * @param b - double from 0 to 1
     * @param min - double from 0 to 1
     * @param max - double from 0 to 1
     * @param minTitle - the color name of the minimal value
     * @param midTitle - the color name of the middle value
     * @param maxTitle - the color name of the maximal value
     * @return
     */
    private String getHueDescription(double r,double g,double b, double min, double max , String minTitle, String midTitle, String maxTitle)
    {
    	String desc = "";
    	if (max == min){
    		String descFormatHueCalculationAllValuesAreEqual_local = this.translator.translateMessage("descFormatHueCalculationAllValuesAreEqual_local");/*"Alle Werte sind identisch!"
	    			+ "\nEs muss sich um ein Grauton handeln."
	    			+ "\nDaher liegt der HSL-Farbton im Zentrum"
	    			+ "\ndes HSL-Farbraums."
	    			+ "\nDer Hue / Radius ist daher bedeutungslos.";*/
	    	desc = descFormatHueCalculationAllValuesAreEqual_local; 
	    } else {
	    	String descFormatGrey_local = this.translator.translateMessage("descFormatGrey_local");/*"Es handelt sich nicht um ein Grauton."
	    			+ "\nDemzufolge muss der HSL-Wert in einem"
	    			+ "\nder folgenden Farbbereichen liegen:"
	    			+ "\nRot, Gelb, Grün, Cyan, Blau, Magenta"
	    			+ "\nDer Bereich hängt stark vom Maximum ab.";*/
	    	desc = descFormatGrey_local; 

	    	// midTitle, minTitle, hueOffset, midTitle, rightVarName, leftVarName, feldIndex
	    	String domininanceDescFormat = this.translator.translateMessage("domininanceDescFormat");/*"Da %s gegenüber %s dominiert, liegt\nHue näher an der %d° Grenze an."
	    			+ "\nWir betrachten uns also nur die"
	    			+ "\n%s gefärbte Linie."
	    			+ "\n \nDie Formel lautet:"
	    			+ "\n60° * (feldIndex + ((right - left) / (max - min)))"
	    			+ "\nright = %s, left = %s"
	    			+ "\nfeldIndex = %d";*/
	    	
	    	// maxTitle, minHue, maxHue, domininanceDesc
	    	String descFormatHueInterval_local = this.translator.translateMessage("descFormatHueInterval_local");// "Da %s das Maximum ist,\nmuss Hue im Interval [%d,%d] liegen.\n%s";
	    	
			// MAX = R
		    if (r == max) {
		    	int minHue = -60;
		    	int maxHue = 60;
		    	int hueOffset = maxHue;
		    	if (b > g) hueOffset = minHue;
		    	
		    	String rightVarName ="g";
		    	int feldIndex = 0;
		    	String leftVarName ="b";
		    	String domininanceDesc = String.format(domininanceDescFormat, midTitle, minTitle, hueOffset, midTitle, rightVarName, leftVarName, (feldIndex / 2));
		    	desc = String.format(descFormatHueInterval_local, maxTitle, minHue, maxHue, domininanceDesc);
		    				    
		    } else {
			    // MAX = G
			    if (g == max) {
			    	int minHue = 60;
			    	int maxHue = 180;
			    	int hueOffset = maxHue;
			    	if (b > r) hueOffset = minHue;
			    	
			    	int feldIndex = 2;
			    	String rightVarName ="b";
			    	String leftVarName ="r";
			    	String domininanceDesc = String.format(domininanceDescFormat, midTitle, minTitle, hueOffset, midTitle, rightVarName, leftVarName, (feldIndex/2));
			    	desc = String.format(descFormatHueInterval_local, maxTitle, minHue, maxHue, domininanceDesc);
			    	
					// MAX = B -> at this point, this should be always true
			    } else {
			    	
			    	int minHue = 180;
			    	int maxHue = 300;
			    	int hueOffset = maxHue;
			    	if (b > r) hueOffset = minHue;

			    	int feldIndex = 4;
			    	String rightVarName ="r";
			    	String leftVarName ="g";
			    	String domininanceDesc = String.format(domininanceDescFormat, midTitle, minTitle, hueOffset, midTitle, rightVarName, leftVarName, (feldIndex/2));
			    	desc = String.format(descFormatHueInterval_local, maxTitle, minHue, maxHue, domininanceDesc);
			    	
			    }
		    }
	    }
    	return desc;
    }

    /**
     * 
     * Sets up the description field.
     * 
     * @param lang - the Language object
     * @param upperLeft - upper left coordinates
     * @param descText - description text
     * @param timing - animation timing
     * @param TextProperties - properties
     */
    private void setupDescriptionField(Language lang, Coordinates upperLeft, String descText, Timing timing, TextProperties properties)
    {
    	if (this.descriptionField == null)
    	{
        	this.descriptionField = new DescriptionField(lang, upperLeft, descText, timing);
        } else {
        	this.descriptionField.setDescriptionText(descText, timing);
        }
    	
    	Color color = (Color)properties.get(AnimationPropertiesKeys.COLOR_PROPERTY);
    	Font selectedFont = (Font)properties.get(AnimationPropertiesKeys.FONT_PROPERTY);
    	
    	Font font = new Font(selectedFont.getFontName(), Font.PLAIN, Math.max(16, selectedFont.getSize()));
    	
    	this.descriptionField.setFont(font);
    	this.descriptionField.setTextColor(color);
    }
    
    /**
     * 
     * Setup the header title.
     * 
     * @param lang - the language factory object
     * @param upperLeft - upper left coordinates
     * @param headerTitle - the title
     * @param timing - animation timing
     * @param TextProperties headerTitleProps
     */
    private void setupHeader(Language lang, Coordinates upperLeft, String headerTitle, Timing timing, TextProperties headerTitleProps)
    {
        if (this.header == null)
        {
        	TextProperties headerProps = new TextProperties();
            headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 15));
        	Text text = lang.newText(upperLeft, headerTitle,
                     "header", timing, headerProps);
        	this.header = new Header(text);
        } else {
        	this.header.getHeaderText().moveBy("translate", upperLeft.getX(), upperLeft.getY(), null, timing);
        	this.header.getHeaderText().setText(headerTitle, null, timing);
        }
        
    	Color color = (Color)headerTitleProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);
    	Font selectedFont = (Font)headerTitleProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
    	
    	Font font = new Font(selectedFont.getFontName(), Font.BOLD, Math.max(24, selectedFont.getSize()));
    	
    	this.header.setFont(font);
    	this.header.setTextColor(color);
    }
    
    /**
     * 
     * Setup the subtitle.
     * 
     * @param lang - the language factory object
     * @param upperLeft - upper left coordinates
     * @param subtitle - the sub title
     * @param timing - animation timing
     * @param TextProperties - subTitleProps
     */
    private void setupSubtitle(Language lang, Coordinates upperLeft, String subtitle, Timing timing, TextProperties subTitleProps)
    {
        if (this.subtitle == null)
        {
        	TextProperties headerProps = new TextProperties();
            headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 16));
        	Text text = lang.newText(upperLeft, subtitle,
                     "subtitle", timing, headerProps);
        	this.subtitle = new Header(text);
        } else {
        	this.subtitle.getHeaderText().moveBy("translate", upperLeft.getX(), upperLeft.getY(), null, timing);
        	this.subtitle.getHeaderText().setText(subtitle, null, timing);
        }
        
    	Color color = (Color)subTitleProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);
    	Font selectedFont = (Font)subTitleProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
    	
    	Font font = new Font(selectedFont.getFontName(), Font.BOLD, Math.max(16, selectedFont.getSize()));
    	
    	this.subtitle.setFont(font);
    	this.subtitle.setTextColor(color);
        
    }
    
    /**
     * 
     * Setup the Graph Bord.
     * 
     * @param language - the language factory object
     * @param upperLeft - the upper left coordinates
     * @param size - the size
     * @param minValue - min value
     * @param maxValue - max value
     * @param timing - animation timing
     * 
     * @return the GraphBord object
     */
    private GraphBord setupGraphBord(Language language, Coordinates upperLeft, Size size , double minValue, double maxValue, Timing timing) 
    {
        ArrayList<Polyline> lines = new ArrayList<Polyline>();

        int xAxisSteps = size.getWidth() / 6;

        int minLimit = (int) (size.getHeight() * minValue);
        int maxLimit = (int) (size.getHeight() * maxValue);
        
        
        // define the nodes and their positions
        Node[] redLineNodes = new Node[7];
        redLineNodes[0] = new Coordinates(upperLeft.getX(), upperLeft.getY() + size.getHeight() - maxLimit);
        redLineNodes[1] = new Coordinates(upperLeft.getX() + xAxisSteps, upperLeft.getY() + size.getHeight() - maxLimit);
        redLineNodes[2] = new Coordinates(upperLeft.getX() + (2*xAxisSteps), upperLeft.getY() + size.getHeight() - minLimit);
        redLineNodes[3] = new Coordinates(upperLeft.getX() + (3*xAxisSteps), upperLeft.getY() + size.getHeight() - minLimit);
        redLineNodes[4] = new Coordinates(upperLeft.getX() + (4*xAxisSteps), upperLeft.getY() + size.getHeight() - minLimit);
        redLineNodes[5] = new Coordinates(upperLeft.getX() + (5*xAxisSteps), upperLeft.getY() + size.getHeight() - maxLimit);
        redLineNodes[6] = new Coordinates(upperLeft.getX() + (6*xAxisSteps), upperLeft.getY() + size.getHeight() - maxLimit);
        Polyline rg = language.newPolyline( redLineNodes, "RED_LINE", timing, this.getLineProperties(Color.red));

        Node[] greenLineNodes = new Node[7];
        greenLineNodes[0] = new Coordinates(upperLeft.getX(), upperLeft.getY() + size.getHeight() - minLimit);
        greenLineNodes[1] = new Coordinates(upperLeft.getX() + xAxisSteps, upperLeft.getY() + size.getHeight() - maxLimit);
        greenLineNodes[2] = new Coordinates(upperLeft.getX() + (2*xAxisSteps), upperLeft.getY() + size.getHeight() - maxLimit);
        greenLineNodes[3] = new Coordinates(upperLeft.getX() + (3*xAxisSteps), upperLeft.getY() + size.getHeight() - maxLimit);
        greenLineNodes[4] = new Coordinates(upperLeft.getX() + (4*xAxisSteps), upperLeft.getY() + size.getHeight() - minLimit);
        greenLineNodes[5] = new Coordinates(upperLeft.getX() + (5*xAxisSteps), upperLeft.getY() + size.getHeight() - minLimit);
        greenLineNodes[6] = new Coordinates(upperLeft.getX() + (6*xAxisSteps), upperLeft.getY() + size.getHeight() - minLimit);
        Polyline gg = language.newPolyline(greenLineNodes, "BLUE_LINE", timing, this.getLineProperties(Color.green));
        
        Node[] blueLineNodes = new Node[7];
        blueLineNodes[0] = new Coordinates(upperLeft.getX(), upperLeft.getY() + size.getHeight() - minLimit);
        blueLineNodes[1] = new Coordinates(upperLeft.getX() + (1*xAxisSteps), upperLeft.getY() + size.getHeight() - minLimit);
        blueLineNodes[2] = new Coordinates(upperLeft.getX() + (2*xAxisSteps), upperLeft.getY() + size.getHeight() - minLimit);
        blueLineNodes[3] = new Coordinates(upperLeft.getX() + (3*xAxisSteps), upperLeft.getY() + size.getHeight() - maxLimit);
        blueLineNodes[4] = new Coordinates(upperLeft.getX() + (4*xAxisSteps), upperLeft.getY() + size.getHeight() - maxLimit);
        blueLineNodes[5] = new Coordinates(upperLeft.getX() + (5*xAxisSteps), upperLeft.getY() + size.getHeight() - maxLimit);
        blueLineNodes[6] = new Coordinates(upperLeft.getX() + (6*xAxisSteps), upperLeft.getY() + size.getHeight() - minLimit);
        Polyline bg = language.newPolyline( blueLineNodes, "GREEN_LINE", timing, this.getLineProperties(Color.blue));
        
        
        lines.add(rg);
        lines.add(gg);
        lines.add(bg);
        
        
        String[] lineDesc = {"","", ""};
 
        String[] xLabels = {"0°","60°","120°","180°","240°","300°","360°"};
        String[] yLabels = {"0","1"};
        
        String xAxisDescription = "H";
        String yAxisDescription = "L";

        return new GraphBord( language, upperLeft, size, xLabels, xAxisDescription, yLabels, yAxisDescription, lines, lineDesc, timing);
    }
    
    /**
     *  Setup the luminance line. This will animate the line from value 0 to the given lum value
     * 
     * @param language - the language factory object
     * @param gb - graphbord in which the line should appear
     * @param lum - double from 0 to 1
     * @param defaultTiming - animation timing
     * @return Polyline - luminance line
     */
    private Polyline setupLuminanceLine(Language language , GraphBord gb, double lum, Timing defaultTiming)
    {
        // LUM
        Node[] lumLineNodes = new Node[2];
        
        int y = (int)(gb.getSize().getHeight() * lum);
        
        lumLineNodes[0] = new Coordinates(gb.getUpperLeft().getX(), gb.getUpperLeft().getY() + gb.getSize().getHeight() );
        lumLineNodes[1] = new Coordinates(gb.getUpperLeft().getX() + gb.getSize().getWidth(), gb.getUpperLeft().getY() + gb.getSize().getHeight() );
        Polyline lumg = language.newPolyline( lumLineNodes, " L= " + new DecimalFormat("#.##").format(lum), null, this.getLineProperties(Color.orange));
        
    	Text lumText = gb.addPolyline(lumg, lumg.getName(), defaultTiming);
        lumg.moveBy("translate", 0, -y, null, defaultTiming);
        lumText.moveBy("translate", 0, -y, null, defaultTiming);
        
        return lumg;
    }
    
   
    /**
     *  Setup the hue line. This will animate the line from value 0 to the given hue value
     * 
     * @param language - the language factory object
     * @param gb - GraphBord object in which the line should appear
     * @param h - double from 0 to 1
     * @param defaultTiming - animation timing
     * @return Polyline - hue line
     */
    private Polyline setupHueLine(Language language , GraphBord gb, double h, Timing defaultTiming)
    {
    	Node[] h_LineNodes = new Node[3];
    	
    	int center 		= gb.getSize().getHeight() / 2;
    	int x = (int) ( (gb.getSize().getWidth() * h));

	    
	    //top
	    h_LineNodes[0] = new Coordinates(gb.getUpperLeft().getX() ,  (int) (gb.getUpperLeft().getY()) + 2);
	    //bottom
	    h_LineNodes[1] = new Coordinates(gb.getUpperLeft().getX() ,  (int) (gb.getUpperLeft().getY() + gb.getSize().getHeight() - 2));
	    //center
	    h_LineNodes[2] = new Coordinates(gb.getUpperLeft().getX() ,  (int) (gb.getUpperLeft().getY() + center));
	    
    	Polyline hg = language.newPolyline( h_LineNodes, " H="+ (int)(h*360)+ "°", null, this.getLineProperties(Color.gray));
    	
    	Text hueText = gb.addPolyline(hg, null, defaultTiming);
        hg.moveBy("translate", x, 0, null, defaultTiming);
        hueText.moveBy("translate", x, 0, null, defaultTiming);
    	
    	return hg;
    }
    
   
    /**
     * Returns the default polyline properties for the given color.
     * 
     * @param color - line color
     * @return PolylineProperties
     */
    private PolylineProperties getLineProperties(Color color)
    {
    	PolylineProperties gp = new PolylineProperties("LineProps");
    	gp.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
    	return gp;
    }
    
    
    
    /*
     *  PRIVATE HELP METHODS / CLASSES
     */
    
    public static class Header extends ScenarioObject{
    	private Text headerText;
    	
        private Font font;
        private Color textColor;
    	
    	public Text getHeaderText()
    	{
    		return headerText;
    	}
    	
    	public Header(Text t)
    	{
    		super();
    		this.headerText = t;
    		this.textColor = (Color) headerText.getProperties().get(AnimationPropertiesKeys.COLOR_PROPERTY);
    		this.font = (Font) headerText.getProperties().get(AnimationPropertiesKeys.FONT_PROPERTY);
    		
    	}
    	
    	public void showPrimitives(Timing t)
    	{
    		headerText.show(t);
    	}
    	public void hidePrimitives(Timing t)
    	{
    		headerText.hide();
    	}
    	public void didRefresh(){
    		
    	}
    	
    	public void setFont(Font f)
    	{
    		if (f != null){
    			this.font = f;
				this.headerText.setFont(f, null, null);
    		}
    	}
    	public Font getFont(){
    		return this.font;
    	}

    	public void setTextColor(Color color)
    	{
    		if (color != null){
				this.headerText.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, color, null, null);
    			this.textColor = color;
    		}
    	}
    	public Color getTextColor(){
    		return this.textColor;
    	}
    	
    	
    }

}