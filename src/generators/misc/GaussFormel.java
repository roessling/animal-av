/*
 * GaussFormel.java
 * Tetiana Rozenvasser, Olga Bayerle, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.main.Animal;
import translator.Translator;


public class GaussFormel implements Generator {
    private Language lang;
    
    //Translator
    private Translator translator;
    private Locale locale;
    
    //Properties Variablen
    private SourceCodeProperties sourceCode;
    private TextProperties formel;
    private SourceCodeProperties commentCode;
    private ArrayProperties weekday;
    private TextProperties finaltext;
    private TextProperties header;
    private TextProperties description;
    private RectProperties headerRect;
    private ArrayMarkerProperties finalResult;
    
    //timing
	public final static Timing  defaultDuration = new TicksTiming(30);
    
    //Variablen zur Berechnung und für das Ergebnis Array
	private int year;
	private String[] weekdayArray = new String[7];
	
    /**
     * Konstruktor
     * @param path path of the language files
     * @param locale locale that's selected
     */
    public GaussFormel(String path, Locale locale) {
     	this.locale = locale;
     	translator = new Translator(path,locale);
    }

    public void init(){
        lang = new AnimalScript((translator.translateMessage("algorithmName")), "Tetiana Rozenvasser, Olga Bayerle", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        formel = (TextProperties)props.getPropertiesByName("formel");
        year = (Integer)primitives.get("year");
        commentCode = (SourceCodeProperties)props.getPropertiesByName("commentCode");
        weekday = (ArrayProperties)props.getPropertiesByName("weekday");
        finaltext = (TextProperties)props.getPropertiesByName("finaltext");
        header = (TextProperties)props.getPropertiesByName("header");
        description = (TextProperties)props.getPropertiesByName("description");
        headerRect = (RectProperties)props.getPropertiesByName("headerRect");
        finalResult = (ArrayMarkerProperties)props.getPropertiesByName("finalResult");
        
        year = (Integer) primitives.get("year");
        //Berechne nun nach Erstellung benötigter Variablen/Properties
        calculate(year);
        return lang.toString();
    }
    
    public void calculate(int year) {
    		Font fontTitle = (Font) header.get("font");
    		fontTitle = fontTitle.deriveFont(1, 36);
    		header.set("font", fontTitle);
    		
	    // Überschrift wird erstellt 	
    	    Text headertxt = this.lang.newText(new Coordinates(20, 30), (translator.translateMessage("generatorName")), "header", null, header);
    	    //Rechteck um die Überschrift
    	    Rect headerRect1 = this.lang.newRect(new Offset(-5, 10, "header", "SW"), new Offset(5, -10, "header", "NE"), "Gaußsche Wochentagsformel", null, headerRect);
    	    // Beschreibung erstellen: pro Zeile neuen Text erstellen ohne /n
		Text description1 = this.lang.newText(new Coordinates(20, 90), (translator.translateMessage("descriptionzu1")), "description1", null, description);
		Text description11 = this.lang.newText(new Coordinates(20, 110), (translator.translateMessage("descriptionzu11")), "description1", null, description);
		Text description12 = this.lang.newText(new Coordinates(20, 130), (translator.translateMessage("descriptionzu12")), "description1", null, description);
		Text description13 = this.lang.newText(new Coordinates(20, 160), (translator.translateMessage("descriptionzu13")), "description1", null, description);
		Text description14 = this.lang.newText(new Coordinates(20, 180), (translator.translateMessage("descriptionzu14")), "description1", null, formel);
		
		lang.nextStep((translator.translateMessage("step1")));
		
		description1.hide();
		description11.hide();
		description12.hide();
		description13.hide();
		description14.hide();
		//Beschreibung ausgeblendet, nun Text was als nächstes passiert
		
		Text description2 = this.lang.newText(new Coordinates(20, 100),
				(translator.translateMessage("descriptionzu2")) + year, "descriprion2", null, description);

		lang.nextStep((translator.translateMessage("step2")));
		
		
		//Alles verstecken und unser Array sowie Formel einblenden
		description2.hide();

		//array das angezeigt werden soll mit Werten füllen 
		weekdayArray[0] = translator.translateMessage("so");
		weekdayArray[1] = translator.translateMessage("mo");
		weekdayArray[2] = translator.translateMessage("di");
		weekdayArray[3] = translator.translateMessage("mi");
		weekdayArray[4] = translator.translateMessage("do");
		weekdayArray[5] = translator.translateMessage("fr");
		weekdayArray[6] = translator.translateMessage("sa");
		
		//array erstellen 
		StringArray stringArray = this.lang.newStringArray(new Coordinates(20,140), weekdayArray, "Ergebnis Array",null, weekday);

		// Neuer Schritt, nachdem Array erstellt wurde
		lang.nextStep((translator.translateMessage("step3")));

		//formel einblenden
		Text w = this.lang.newText(new Coordinates(20,230), "w=", "w", null, formel);
		Text first = this.lang.newText(new Coordinates(70,230), "1+5*(("+(translator.translateMessage("year1"))+"-1) mod4)", "first", null, formel);
		Text plus1 = this.lang.newText(new Coordinates(220,230), " + ", "plus", null, formel);
		Text second = this.lang.newText(new Coordinates(260,230), "4*(("+(translator.translateMessage("year1"))+"-1) mod100)", "second", null, formel);
		Text plus2 = this.lang.newText(new Coordinates(410,230), " + ", "plus", null, formel);
		Text third = this.lang.newText(new Coordinates(450,230), "6*(("+(translator.translateMessage("year1"))+"-1) mod400)", "third", null, formel);
		Text mod = this.lang.newText(new Coordinates(590,230), " )mod7", "mod", null, formel);
		
		lang.nextStep((translator.translateMessage("step4")));


		// sourcecode object erstellen
		SourceCode sc = this.lang.newSourceCode(new Coordinates(20, 250), "SourceCode", null, sourceCode);

		// Add the lines to the SourceCode object
		sc.addCodeLine("int newYear = Integer.parseInt(year);", null, 0, null); // 0
		sc.addCodeLine("int indexW;", null, 0, null); // 1
		sc.addCodeLine("double ersterTeil = (1 + 5 *( (newYear - 1) % 4));", null, 0, null); // 2
		sc.addCodeLine("double zweiterTeil = 4 * ((newYear -1) % 100);", null, 0, null); // 3
		sc.addCodeLine("double dritterTeil = 6 * ((newYear-1) % 400);", null, 0, null); // 4
		sc.addCodeLine("indexW = (int) (ersterTeil+zweiterTeil+dritterTeil) % 7;", null, 0, null); // 5
		sc.addCodeLine("String result = weekday[indexW];", null, 0, null); // 6

		lang.nextStep((translator.translateMessage("step5")));


		// now create the comment code entity
		SourceCode comment = this.lang.newSourceCode(new Coordinates(600, 250), "CommentCode", null, commentCode);
        
		// highlights the first line of the Code
		sc.highlight(0, 0, false);
		
		// create ArrayMarker on the first position
		ArrayMarker arrayMarker = this.lang.newArrayMarker(stringArray, 0, "result", null, finalResult);
		
		// Highlight line 1 and add matching comments
		sc.toggleHighlight(0, 0, false, 1, 0);
		comment.addCodeLine("year = " + year , null, 0, null);
		lang.nextStep();
		
		comment.addCodeLine(" " , null, 0, null);
		lang.nextStep();
		
		sc.toggleHighlight(1, 0, false, 2, 0);
		first.hide();
		Text firstRed = this.lang.newText(new Coordinates(70,230), "1+5*(("+(translator.translateMessage("year1"))+"-1) mod4)", "first", null, finaltext);
		comment.addCodeLine("first = 1+5*(("+year+"-1) mod4)" , null, 0, null);
		int newYear = year;
		double erste = 1 + 5 * ((newYear - 1) % 4);
		lang.nextStep();

		sc.toggleHighlight(2, 0, false, 3, 0);
		firstRed.hide();
		first.show();
		second.hide();
		Text secondRed = this.lang.newText(new Coordinates(260,230), "4*(("+(translator.translateMessage("year1"))+"-1) mod100)", "second", null, finaltext);
		comment.addCodeLine("second = 4*(("+year+"-1) mod100)", null, 0, null);
		double zweite = 4 * ((newYear - 1) % 100);
		lang.nextStep();
		
		sc.toggleHighlight(3, 0, false, 4, 0);
		secondRed.hide();
		second.show();
		third.hide();
		Text thirdRed = this.lang.newText(new Coordinates(450,230), "6*(("+(translator.translateMessage("year1"))+"-1) mod400)", "third", null, finaltext);
		comment.addCodeLine("third = 6*(("+year+"-1) mod400)", null, 0, null);
		double dritte = 6 * ((newYear - 1) % 400);
		lang.nextStep();
		
		sc.toggleHighlight(4, 0, false, 5, 0);
		thirdRed.hide();
		w.hide();
		first.hide();
		plus1.hide();
		second.hide();
		plus2.hide();
		third.hide();
		mod.hide();
		Text finalRed = this.lang.newText(new Coordinates(20,230), "w = (1+5*(("+(translator.translateMessage("year1"))+"-1) mod4) + 4*(("+(translator.translateMessage("year1"))+"-1)mod100) + 6*(("+(translator.translateMessage("year1"))+"-1)mod400)) mod7", "formel", null, finaltext);
		comment.addCodeLine("w = (" + erste + "+" + zweite + "+" + dritte + ") mod 7", null, 0, null);
		int indexW = (int) (erste + zweite + dritte) % 7;
		lang.nextStep((translator.translateMessage("step6")));
		
		sc.toggleHighlight(5, 0, false, 6, 0);
		finalRed.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
		String weekname = stringArray.getData(indexW);
		comment.addCodeLine((translator.translateMessage("ergebnis"))+" "+ weekname, null, 0, null);
		
		// ArrayMarker ist auf das Ergebnisfeld gesetzt
		arrayMarker.move(indexW, null, defaultDuration); 
		
		Text endText = this.lang.newText(new Coordinates(20,200), (translator.translateMessage("schlusstext")) + year +" "+ (translator.translateMessage("ist")), "endText", null, finaltext);
		
		Text endResult = this.lang.newText(new Coordinates(310,200)," " + weekname, "endResult", null, finaltext);
		lang.nextStep((translator.translateMessage("step7")));
		
	}

    public String getName() {
        return (translator.translateMessage("generatorName"));
    }

    public String getAlgorithmName() {
        return (translator.translateMessage("algorithmName"));
    }

    public String getAnimationAuthor() {
        return "Tetiana Rozenvasser, Olga Bayerle";
    }

    public String getDescription(){
        return (translator.translateMessage("description"));
    }

    public String getCodeExample(){
        return (translator.translateMessage("codeexample"));
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
        return Generator.JAVA_OUTPUT;
    }

	public static void main(String[] args) {
		Language l = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT, "Gaußsche Wochentagsformel",
				"Tetiana Rozenvasser, Olga Bayerle", 800, 600);
		Generator generator = new GaussFormel("resources/GaussFormel",Locale.GERMANY);
	//	GaussscheWochentagsformel s = new GaussscheWochentagsformel(l);
		Animal.startGeneratorWindow(generator);
		//String year = "2018";
		//s.calculate(year);
		System.out.println(l);
	}
}