/*
 * Heun.java
 * Felix Sternkopf, Emine Saracoglu, 2015 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.AnswerModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

public class Heun implements Generator {
	boolean question1;
	double reality;
	
	private Language lang;
		
	private double t_0;
    private double t_k;
	private double t_k_inc;
    private double h;
	private double y_0;
    private double y_k;
	private double t_Max;
	private double questionProbability;
  
	private static final String			   	FORMULARHEADER = "Beispielformel zur Anwendung des Verfahrens:";
	private static final String			   	FORMULAR1 = "y'(t) = 2y(t) -e^t";	
	private static final String			   	FORMULAR2 = "--> f(y, t) = 2y - e^t";

	private static final String			   	CODEHEADER = "Codebeispiel als Pseudocode:";
	
	private static String			   		PARAMETERHEADER;
	private static String			   		PARAMETER1;
	private static String			   		PARAMETER2;
	private static String			   		PARAMETER3;
	private static String			   		PARAMETER4;
	
	
	private final TextProperties           	HeaderTP      	= new TextProperties();
	private final TextProperties		   	FormularTP    	= new TextProperties();
	private final TextProperties		   	SmallHeaderTP 	= new TextProperties();
	private final TextProperties			NormalTP		= new TextProperties();
	private final TextProperties			DescriptionTP	= new TextProperties();
	
	private final RectProperties			HeaderRectRP 	= new RectProperties();
	private final RectProperties			FormRectRP		= new RectProperties();
	private final RectProperties			ParamRectRP		= new RectProperties();
	private final RectProperties			CodeRectRP		= new RectProperties();
	
	private Text                           	header;
	private Text                           	description1;
	private Text                           	description2;
	private Text                           	description3;
	private Text                           	description4;
	private Text                           	description5;
	private Text                           	description6;
	private Text                           	description7;
	private Text                           	description8;
	private Text                           	description9;
	private Text                           	description10;
	private Text                           	description11;
	private Text                           	description12;
	private Text                           	description13;
	private Text                           	description14;
	private Text                           	description15;
	
	private Rect						   	headerWindow;
	private Rect							formularWindow;
	private Rect							codeWindow;
	private Rect							paramWindow;
	private Rect 							calcWindow;
	
	private Text						   	formularHeader;
	private Text						   	formular1;
	private Text						   	formular2;
	
	private Text 						   	codeHeader;
	
	private Text						   	parameterHeader;
	private Text						   	parameter1;
	private Text						   	parameter2;
	private Text						   	parameter3;
	private Text						   	parameter4;

	private SourceCode                     	code;
	private SourceCodeProperties     		sourceProps;
	
	private RectProperties					backgroundProps;
	
	private TextProperties					textProps;
	private Color							fontColor;
	
	private Variables 						vars;
	
	private Polyline 						pl;

    public void init(){
        lang = new AnimalScript("Heun [DE]", "Felix Sternkopf, Emine Saracoglu", 800, 600);
    
		question1 = true;
		
		lang.setStepMode(true);
	}

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {

		questionProbability = (double)primitives.get("questionProbability");
		t_k = (double)primitives.get("t_0");
		t_0 = t_k;
        t_Max = (double)primitives.get("t_Max");
        y_0 = (double)primitives.get("y_0");
		y_k = y_0;
        h = (double)primitives.get("h");
		sourceProps = (SourceCodeProperties)(props.getPropertiesByName("SourceCode Properties"));
		
		String HEADER        = "Heun-Verfahren";
		String DESCRIPTION1  = "Beginnen wir mit einem kleinen Rückblick auf Anfangswertprobleme. Ein Anfangswertproblem besteht aus einer ";
		String DESCRIPTION2  = "gewöhnlichen Differentialgleichung und einem Anfangswert, für welchen die Gleichung lösbar sein soll.";
		String DESCRIPTION3  = "Die Differentialgleichung in unserem Beispiel wird die Funktion y'(t) = 2y(t) - e^t mit dem Anfangswert y(" + t_0 + ") = " + y_0 + " sein.";
		String DESCRIPTION4  = "Unser Ziel ist es nun näherungsweise einen Funktionswert für y(" + t_Max + ") zu finden ohne die Differentialgleichung";
		String DESCRIPTION5  = "lösen zu müssen. Hierbei ist y(" + t_Max + ") der Funktionswert zum Zeitpunkt " + t_Max + ".";
		String DESCRIPTION6  = "Nun muss man nur noch eine Schrittweite h wählen. Um eine optimale Schrittweite zu wählen, gibt es";
		String DESCRIPTION7  = "andere Algorithmen.";
		String DESCRIPTION8  = " ";
		String DESCRIPTION9  = "Die Parameter und Funktionen noch einmal im Überblick:";
		String DESCRIPTION10 = "1. y'(t) = 2y(t) - e^t         (Gegebene Differentialgleichung)";
		String DESCRIPTION11 = "2. y(" + t_0 + ") = " + y_0 + "               (Gegebener Anfangswert)";
		String DESCRIPTION12 = "3. t_0 = " + t_0 + "                    (Startzeitpunkt)";
		String DESCRIPTION13 = "4. h = " + h + "                       (Schrittweite h)";
		String DESCRIPTION14 = "5. t_Max = " + t_Max + "               (Zeitpunkt bis zu dem man den Wert annähern möchte)";
		String DESCRIPTION15 = "";
		
		vars = lang.newVariables();
		vars.declare("string", "tkinc");
		vars.set("tkinc", "-");
		vars.declare("string", "k");
		vars.set("k", "-");
		vars.declare("string", "t0");
		vars.set("t0", "-");
		vars.declare("string", "tMax");
		vars.set("tMax", "-");
		vars.declare("string", "h");
		vars.set("h", "-");
		vars.declare("string", "y0");
		vars.set("y0", "-");
		vars.declare("string", "yke");
		vars.set("yke", "-");
		vars.declare("string", "ykinc");
		vars.set("ykinc", "-");
		vars.declare("string", "tk");
		vars.set("tk", "-");
		
		backgroundProps = (RectProperties)(props.getPropertiesByName("Background Properties"));
		Color background1 = (Color)backgroundProps.get(AnimationPropertiesKeys.FILL_PROPERTY);		
		backgroundProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, backgroundProps.get(AnimationPropertiesKeys.FILL_PROPERTY));
		
		FormRectRP.set(AnimationPropertiesKeys.COLOR_PROPERTY, background1);
		FormRectRP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		FormRectRP.set(AnimationPropertiesKeys.FILL_PROPERTY, background1);
		FormRectRP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		ParamRectRP.set(AnimationPropertiesKeys.COLOR_PROPERTY, background1);
		ParamRectRP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		ParamRectRP.set(AnimationPropertiesKeys.FILL_PROPERTY, background1);
		ParamRectRP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		CodeRectRP.set(AnimationPropertiesKeys.COLOR_PROPERTY, background1);
		CodeRectRP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		CodeRectRP.set(AnimationPropertiesKeys.FILL_PROPERTY, background1);
		CodeRectRP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		if(background1.getRed() == 0 && background1.getGreen() == 224 && background1.getBlue() == 190){
			FormRectRP.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(113, 221, 205));
			FormRectRP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			FormRectRP.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(113, 221, 205));
			FormRectRP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
			
			ParamRectRP.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(113, 221, 205));
			ParamRectRP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			ParamRectRP.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(113, 221, 205));
			ParamRectRP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
			
			CodeRectRP.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(175, 233, 223));
			CodeRectRP.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			CodeRectRP.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(175, 233, 223));
			CodeRectRP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		}
		
       	lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		
		textProps = (TextProperties)(props.getPropertiesByName("Text Properties"));
		
		//Gewählte Schriftart erkennen.
		String fontWrong = ((Font)textProps.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFontName();
		String font = "";
		for(int i = 0; i < fontWrong.length(); i++){
			if(fontWrong.charAt(i) != '.'){
				font = font + Character.toString(fontWrong.charAt(i));
			}
			else{
				break;
			}
		}
		
		//Gewählte Schriftfarbe erkennen.
		fontColor = (Color)textProps.get(AnimationPropertiesKeys.COLOR_PROPERTY);
		
		//Eigenschaften an alle Texte übergeben.
		HeaderTP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font, Font.PLAIN, 30));
		FormularTP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font, Font.PLAIN, 20));
		SmallHeaderTP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font, Font.PLAIN, 12));
		NormalTP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font, Font.PLAIN, 14));
		DescriptionTP.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(font, Font.PLAIN, 12));
		
		HeaderTP.set(AnimationPropertiesKeys.COLOR_PROPERTY, fontColor);
		FormularTP.set(AnimationPropertiesKeys.COLOR_PROPERTY, fontColor);
		SmallHeaderTP.set(AnimationPropertiesKeys.COLOR_PROPERTY, fontColor);
		NormalTP.set(AnimationPropertiesKeys.COLOR_PROPERTY, fontColor);
		DescriptionTP.set(AnimationPropertiesKeys.COLOR_PROPERTY, fontColor);
		
		header = lang.newText(new Coordinates(100, 50), HEADER, "Header", null, HeaderTP);
		headerWindow = lang.newRect(new Offset(-5, -5, header, "NW"), new Coordinates(750, 80), "HeaderWindow", null, backgroundProps);
		
		description1 = lang.newText(new Offset(0, 5, header, "SW"), DESCRIPTION1, "Description1", null, DescriptionTP);
		description2 = lang.newText(new Offset(0, 5, description1, "SW"), DESCRIPTION2, "Description2", null, DescriptionTP);
		description3 = lang.newText(new Offset(0, 5, description2, "SW"), DESCRIPTION3, "Description3", null, DescriptionTP);
		description4 = lang.newText(new Offset(0, 5, description3, "SW"), DESCRIPTION4, "Description4", null, DescriptionTP);
		description5 = lang.newText(new Offset(0, 5, description4, "SW"), DESCRIPTION5, "Description5", null, DescriptionTP);
		description6 = lang.newText(new Offset(0, 5, description5, "SW"), DESCRIPTION6, "Description6", null, DescriptionTP);
		description7 = lang.newText(new Offset(0, 5, description6, "SW"), DESCRIPTION7, "Description7", null, DescriptionTP);    
      
		lang.nextStep();
    
		description8 = lang.newText(new Offset(0, 5, description7, "SW"), DESCRIPTION8, "Description8", null, DescriptionTP);
		description9 = lang.newText(new Offset(0, 5, description8, "SW"), DESCRIPTION9, "Description9", null, DescriptionTP);
		description10 = lang.newText(new Offset(0, 5, description9, "SW"), DESCRIPTION10, "Description10", null, DescriptionTP);
		description11 = lang.newText(new Offset(0, 5, description10, "SW"), DESCRIPTION11, "Description11", null, DescriptionTP);
		description12 = lang.newText(new Offset(0, 5, description11, "SW"), DESCRIPTION12, "Description12", null, DescriptionTP);
		description13 = lang.newText(new Offset(0, 5, description12, "SW"), DESCRIPTION13, "Description13", null, DescriptionTP);
		description14 = lang.newText(new Offset(0, 5, description13, "SW"), DESCRIPTION14, "Description14", null, DescriptionTP);
		description15 = lang.newText(new Offset(0, 5, description14, "SW"), DESCRIPTION15, "Description15", null, DescriptionTP); 
    
		lang.nextStep("Erklärung");

		lang.hideAllPrimitives(); 
		
		header.show();
		headerWindow.show();
		
		// Frage1
		double rand1 = Math.random();
		if(rand1 <= questionProbability) {
			MultipleChoiceQuestionModel quest1 = new MultipleChoiceQuestionModel("quest1");
			quest1.setPrompt("Welche Ordnung hat das Heun-Verfahren?");
			quest1.addAnswer(new AnswerModel("an1", "1", 5, "FALSCH"));
			quest1.addAnswer(new AnswerModel("an2", "2", 5, "Sehr gut, das ist korrekt."));
			quest1.addAnswer(new AnswerModel("an3", "3", 5, "FALSCH"));
			quest1.addAnswer(new AnswerModel("an4", "4", 5, "FALSCH"));
			lang.addMCQuestion(quest1);
			lang.nextStep();
		}
		
		heunExample();
		
		lang.finalizeGeneration();
        
        return lang.toString();
    }

    public String getName() {
        return "Heun [DE]";
    }

    public String getAlgorithmName() {
        return "Heun";
    }

    public String getAnimationAuthor() {
        return "Felix Sternkopf, Emine Saracoglu";
    }

    public String getDescription(){
        return 	"Das Heun-Verfahren:"
				+"\n"
				+"Der Algorithmus von Heun ist ein Verfahren aus der numerischen Mathematik. Ziel ist es näherungsweise eine Lösung für ein gegebenes Anfangswertproblem zu finden."
				+"\n"
				+"Das Heun-Verfahren gehört zu den sogenannten Einschrittverfahren. Im Gegensatz zu Mehrschrittverfahren werden hier zur Berechnung keine Daten aus vorherigen Zeitpunkten genommen."
				+"\n"
				+"\n"
				+"Wir haben ein Anfangsproblem gegeben mit y'(t) = f(y(t), t) als gewöhnliche Differentialgleichung und mit y(t_0) = y_0 als einen Anfangswert."
				+"\n"
				+"Nun wählen wir uns eine Diskretisierungsschrittweite, die im Folgenden immer mit der Variable h bezeichnet wird. Diese Schrittweite legt fest wie groß die Schritte sind, die wir in jeder"
				+"\n"
				+"Iteration machen. Je kleiner die Schrittweite h gewählt wird, desto geringer ist der Näherungsfehler des Algorithmus. Zu beachten ist, dass bei einer kleineren Schrittweite die Ausführung"
				+"\n"
				+"länger dauert, da man nun mehr Schritte gehen muss. Deshalb ist es wichtig einen geeigneten Zwischenwert, zwischen guter Ausführung und guter Genauigkeit, zu finden."
				+"\n"
				+"Der globale Fehler der Näherung liegt beim Heun-Verfahren bei h^2 gegen 0,  wobei man hier auch von der Konvergenzordnung 2 spricht."
				+"\n"
				+"\n"
				+"Bei der eigentlichen Berechnung betrachtet man nur die diskreten Zeitpunkte t_(k+1) = t_0 + (h * (k+1)), für ein natürliches k in N. Hierbei ist k die Konstante, die angibt in welcher Iteration wir sind."
				+"\n"
				+"Der Algorithmus ist zweistufig, was bedeutet, dass wir zwei autonome Berechnungen durchführen müssen, um unser Ergebniss zu bekommen."
				+"\n"
				+"\n"
				+"Die erste Stufe ist die Berechnung des expliziten Euler, welchen man als eigenen Algorithmus auch im Animal findet."
				+"\n"
				+"y_(k+1)_e = y_k + h * f(y_k, t_k)"
				+"\n"
				+"Wir benennen unser Ergebnis hier y_(k+1)_e um kenntlich zu machen, dass dies nur ein Zwischenergebnis nach Euler ist und nicht das Endergebnis."
				+"\n"
				+"\n"
				+"In der zweiten Stufe wird dann die eigentliche Heun-Funktion verwendet, die uns aus dem Zwischenergebnis des expliziten Eulers unser Zielergebnis berechnet."
				+"\n"
				+"y_(k+1) = y_k + 0.5 * h(f(y_k, t_k) + f(y_(k+1)_e, t_(k+1))"
				+"\n"
				+"\n"
				+"Das Ergebnis y_i, welches wir in jedem Iterationsschritt bekommen, ist dann der angenäherte Wert, den y an der Stelle t_i annimmt. Zu beachten ist, dass wir keine approximierte Funktion"
				+"\n"
				+"erhalten, die unsere Differentialgleichung löst."
				+"\n"
				+"\n"
				+"Während der gesamten Animation können Fragen zu diesem Algorithmus gestellt werden. Wenn Sie das nicht möchten, können Sie einfach die Wahrscheinlichkeit für Fragen geringer stellen"
				+"\n"
				+"oder komplett ausstellen, indem Sie sie auf 0.0 stellen. 1.0 entspricht einer Wahrscheinlichkeit 100%.";
	}

    public String getCodeExample(){
        return 	"heun{"
				+"\n"
				+" t_k = t_0;"
				+"\n"
				+" y_k = y_0;"
				+"\n"
				+"\n"
				+" while(t_k(+1) <= t_Max){"
				+"\n"
				+"	t_(k+1) = t_0 + (h * (k + 1));"
				+"\n"
				+"\n"
				+"	y_(k+1)_e = y_k + h * f(y_k, t_k);"
				+"\n"
				+"	y_(k+1) = y_k + 0.5 * h(f(y_k, t_k) + f(y_(k+1)_e, t_(k+1)));"
				+"\n"
				+"\n"
				+"	t_k = t_(k+1);"
				+"\n"
				+" }"
				+"\n"
				+"}";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

	private void generateCodeExample() {
		code = lang.newSourceCode(new Offset(0, 10, codeHeader, "SW"), "code", null, sourceProps);
		code.addCodeLine("t_k = t_0;", "", 0, null);
		code.addCodeLine("y_k = y_0;", "", 0, null);
		code.addCodeLine("", "", 0, null);
		code.addCodeLine("while(t_k(+1) <= t_Max){", "", 0, null);
		code.addCodeLine("t_(k+1) = t_0 + (h * (k + 1));", "", 1, null);
		code.addCodeLine("", "", 0, null);
		code.addCodeLine("y_(k+1)_e = y_k + h * f(y_k, t_k);", "", 1, null);
		code.addCodeLine("y_(k+1) = y_k + 0.5 * h(f(y_k, t_k) + f(y_(k+1)_e, t_(k+1)));", "", 1, null);
		code.addCodeLine("", "", 0, null);
		code.addCodeLine("t_k = t_(k+1);", "", 1, null);
		code.addCodeLine("}", "", 0, null);
  }

	private void heunExample(){		
		int k = 1;
		double y_k_e;
	
		//Initialisierung der Beispielformeln
		formularHeader = lang.newText(new Offset(0, 30, header, "SW"), FORMULARHEADER, "formularHeader", null, SmallHeaderTP);
		formular1 = lang.newText(new Offset(0, 10, formularHeader, "SW"), FORMULAR1, "formular1", null, DescriptionTP);
		formular2 = lang.newText(new Offset(0, 10, formular1, "SW"), FORMULAR2, "formular2", null, DescriptionTP);
		
		formularWindow = lang.newRect(new Offset(-5, -5, formularHeader, "NW"), new Coordinates(380, 120), "formularWindow", null, FormRectRP);
		
		lang.nextStep();
	  
		//Initialisierung des Codebeispiels
		codeHeader = lang.newText(new Offset(50, 0, formularHeader, "NE"), CODEHEADER, "codeHeader", null, SmallHeaderTP);
		generateCodeExample();	 

		codeWindow = lang.newRect(new Offset(-5, -5, codeHeader, "NW"), new Coordinates(750, 120), "codeWindow", null, CodeRectRP);
	  
		lang.nextStep();
	  
		//Initialisierung der Startwerte
		PARAMETERHEADER = "Unsere Parameter:";
		PARAMETER1 = "Anfangswert y("+ t_0 +") = " + y_0;
		PARAMETER2 = "Startzeitpunkt t_0 = " + t_0;
		PARAMETER3 = "Endzeitpunkt des Verfahrens t_Max = " + t_Max;
		PARAMETER4 = "Schrittweite h = " + h;
	  
		parameterHeader = lang.newText(new Offset(0, 120, header, "SW"), PARAMETERHEADER, "parameterHeader", null, SmallHeaderTP);
		parameter1 = lang.newText(new Offset(0, 10, parameterHeader, "SW"), PARAMETER1, "parameter1", null, DescriptionTP);
		parameter2 = lang.newText(new Offset(0, 10, parameter1, "SW"), PARAMETER2, "parameter2", null, DescriptionTP);
		parameter3 = lang.newText(new Offset(0, 10, parameter2, "SW"), PARAMETER3, "parameter3", null, DescriptionTP);
		parameter4 = lang.newText(new Offset(0, 10, parameter3, "SW"), PARAMETER4, "parameter4", null, DescriptionTP);
		
		paramWindow = lang.newRect(new Offset(-5, -5, parameterHeader, "NW"), new Coordinates(380, 210), "paramWindow", null, ParamRectRP);
		
		vars.set("k", Integer.toString(k-1));
		vars.set("t0",Double.toString(t_0));
		vars.set("tMax",Double.toString(t_Max));
		vars.set("h",Double.toString(h));
		vars.set("y0",Double.toString(y_k));
	  
		lang.nextStep("Initialisierung der Variablen");
	  
		//Fenster für die Berechnung
		int contentCount = 1;
		
		String k_Text;
		String tk_Text;
		String tk1_Text;
		String s1_Text;
		String s1_Result;
		String s2_Text;
		String s2_Result;
		String yk_Text;
		String iteration_Text;
		String error_Text1;
		String error_Text2;
		
		Text kText;
		Text tkText;
		Text tk1Text;
		Text s1Text;
		Text s1Result;
		Text s2Text;
		Text s2Result;
		Text ykText;
		Text iterationText;
		Text errorText1;
		Text errorText2;
		
		Text newParam1;
		Text newParam2;
		Text newParam3;
		Text newParam4;
		
		calcWindow = lang.newRect(new Offset(-5, 20, parameter4, "SW"), new Coordinates(750, 600), "calcWindow", null, CodeRectRP);
		Node p1 = new Offset(10, 150, calcWindow, "NW");
		Node p2 = new Offset(-10, 150, calcWindow, "NE");
		Node[] nodes = {p1, p2};
		
		//Abfangen des Falles, dass MaxTime über StartTime liegt
		if(t_0 >= t_Max){
			error_Text1 = "Der übergebene Startzeitpunkt liegt über dem Endzeitpunkt des Verfahrens, deshalb kann das Verfahren nicht";
			error_Text2 = "angewendet werden. Die Visualisierung wird mit Standartwerten ausgeführt.";
			errorText1 = lang.newText(new Offset(10, 10, calcWindow, "NW"), error_Text1, "errorText1", null, SmallHeaderTP);
			errorText2 = lang.newText(new Offset(0, 20, errorText1, "NW"), error_Text2, "errorText2", null, SmallHeaderTP);
			errorText1.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			errorText2.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			
			parameter1.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			parameter2.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			parameter3.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			parameter4.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			
			lang.nextStep();
			
			t_0 = 0;
			t_k = t_0;
			y_0 = 2;
			y_k = y_0;
			h = 0.5;
			t_Max = 1;
			
			PARAMETER1 = "Anfangswert y("+ t_0 +") = " + y_0;
			PARAMETER2 = "Startzeitpunkt t_0 = " + t_0;
			PARAMETER3 = "Endzeitpunkt des Verfahrens t_Max = " + t_Max;
			PARAMETER4 = "Schrittweite h = " + h;
			
			newParam1 = lang.newText(new Offset(0, 40, errorText2, "NW"), PARAMETER1, "newParam1", null, NormalTP);
			newParam2 = lang.newText(new Offset(0, 30, newParam1, "NW"), PARAMETER2, "newParam2", null, NormalTP);
			newParam3 = lang.newText(new Offset(0, 30, newParam2, "NW"), PARAMETER3, "newParam3", null, NormalTP);
			newParam4 = lang.newText(new Offset(0, 30, newParam3, "NW"), PARAMETER4, "newParam4", null, NormalTP);
			
			newParam1.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			newParam2.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			newParam3.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			newParam4.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);

			lang.nextStep();
			
			parameter1.hide();
			parameter2.hide();
			parameter3.hide();
			parameter4.hide();
			
			vars.set("k", Integer.toString(k-1));
			vars.set("t0",Double.toString(t_0));
			vars.set("tMax",Double.toString(t_Max));
			vars.set("h",Double.toString(h));
			vars.set("y0",Double.toString(y_k));
			
			parameter1 = lang.newText(new Offset(0, 10, parameterHeader, "SW"), PARAMETER1, "parameter1", null);
			parameter2 = lang.newText(new Offset(0, 10, parameter1, "SW"), PARAMETER2, "parameter2", null);
			parameter3 = lang.newText(new Offset(0, 10, parameter2, "SW"), PARAMETER3, "parameter3", null);
			parameter4 = lang.newText(new Offset(0, 10, parameter3, "SW"), PARAMETER4, "parameter4", null);
			
			parameter1.setFont(new Font("SansSerif", Font.BOLD, 12), null, null);
			parameter2.setFont(new Font("SansSerif", Font.BOLD, 12), null, null);
			parameter3.setFont(new Font("SansSerif", Font.BOLD, 12), null, null);
			parameter4.setFont(new Font("SansSerif", Font.BOLD, 12), null, null);
			
			parameter1.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			parameter2.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			parameter3.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			parameter4.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			
			lang.nextStep();
			
			errorText1.hide();
			errorText2.hide();
			
			newParam1.hide();
			newParam2.hide();
			newParam3.hide();
			newParam4.hide();
			
			parameter1.changeColor(null, fontColor, null, null);
			parameter2.changeColor(null, fontColor, null, null);
			parameter3.changeColor(null, fontColor, null, null);
			parameter4.changeColor(null, fontColor, null, null);
			
			parameter1.setFont(new Font("SansSerif", Font.PLAIN, 12), null, null);
			parameter2.setFont(new Font("SansSerif", Font.PLAIN, 12), null, null);
			parameter3.setFont(new Font("SansSerif", Font.PLAIN, 12), null, null);
			parameter4.setFont(new Font("SansSerif", Font.PLAIN, 12), null, null);
		}	
 	  
		//Beginn des Heun-Verfahrens bzw. der Code-Erstellung
		//Texte initialisieren
		k_Text = "Iteration k = " + (k - 1);
		tk_Text = "Aktuelle Zeit t_k = t_" + (k - 1) + " = " + t_k;
		
		vars.set("tk", Double.toString(t_k));
		kText = lang.newText(new Offset(10, 10, calcWindow, "NW"), k_Text, "kText", null, FormularTP);
		tkText = lang.newText(new Offset(50, 0,  kText,  "NE"), tk_Text, "tkText", null, FormularTP);
		
		lang.nextStep();
		
		//Texte initialisieren
		t_k_inc = ((t_0 + (h * k)) * 100) / 100.0;
		vars.set("tkinc", Double.toString(t_k_inc));
		tk1_Text = "t_(k+1) = t_0 + (h * (k+1))  =  t_" + k + " = " + t_k_inc;

		tk1Text = lang.newText(new Offset(20, 40, kText, "NW"), tk1_Text, "tk1Text", null, FormularTP);
		tk1Text.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
		
		code.highlight(4);
		
		lang.nextStep();
		
		//Vorherige Highlights löschen
		tk1Text.changeColor(null, fontColor, null, null);
		
		code.unhighlight(4);
		
		//Texte initialisieren
		y_k_e = Math.round((y_k + h * (2 * y_k - Math.pow(Math.E, t_k))) * 100) / 100.0; 
		y_k = Math.round((y_k + 0.5 * h * ((2 * y_k - Math.pow(Math.E, t_k)) + (2 * y_k_e - Math.pow(Math.E, t_k_inc)))) * 100) / 100.0;		
		s1_Text = "y_(k+1)_e = y_k + h * f(y_k, t_k) ";	
		s1_Result = "=  " + y_k_e;
		
		vars.set("yke", Double.toString(y_k_e));
		s1Text = lang.newText(new Offset(0, 50, tk1Text, "NW"), s1_Text, "s1Text", null, FormularTP);
		s1Result = lang.newText(new Offset(96, 30, s1Text, "NW"), s1_Result, "s1Result", null, FormularTP);
		
		s1Text.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
		s1Result.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);

		pl = lang.newPolyline(nodes, "pl", null);
		
		code.highlight(6);
		
		lang.nextStep();
		
		//Vorherige Highlights löschen		
		s1Text.changeColor(null, fontColor, null, null);
		s1Result.changeColor(null, fontColor, null, null);
		
		code.unhighlight(6);
		
		//Texte initialisieren
		s2_Text = "y_(k+1) = y_k + 0.5 * h(f(y_k, t_k) + f(y_(k+1)_e, t_(k+1))) ";
		s2_Result = "= " + y_k;
		
		vars.set("ykinc", Double.toString(y_k));
		s2Text = lang.newText(new Offset(-96, 30, s1Result, "NW"), s2_Text, "s2Text", null, FormularTP);
		s2Result = lang.newText(new Offset(74, 30, s2Text, "NW"), s2_Result, "s2Result", null, FormularTP);
		
		s2Text.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
		s2Result.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
		
		code.highlight(7);
	  
		lang.nextStep();

		//Vorherige Highlights löschen
		s2Text.changeColor(null, fontColor, null, null);
		s2Result.changeColor(null, fontColor, null, null);
		
		code.unhighlight(7);
		
		//Texte initialisieren
		yk_Text = "--> y(" + t_k_inc + ") ~ " + y_k;
		
		ykText = lang.newText(new Offset(10, -30, calcWindow, "SW"), yk_Text, "ykText", null, FormularTP); 
		
		ykText.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
	  
		lang.nextStep();
		
		//Vorherige Highlights löschen
		ykText.changeColor(null, fontColor, null, null);
		
		//Texte initialisieren
		vars.set("k", Integer.toString(k));
		iteration_Text = "k  =  k + 1  =  " + k;
		
		iterationText = lang.newText(new Offset(50, 0, ykText, "NE"), iteration_Text, "iterationText", null, FormularTP); 
		
		iterationText.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
		
		code.highlight(9);
		
		k++;
		t_k = t_k_inc;
		
		String content = "Iterationsschritt" + contentCount;
		contentCount++;
		
		lang.nextStep(content);
	  
		while(t_k_inc < t_Max){
			kText.hide();
			tkText.hide();
			tk1Text.hide();
			s1Text.hide();
			s1Result.hide();
			s2Text.hide();
			s2Result.hide();
			ykText.hide();
			iterationText.hide();
			pl.hide();
			
			code.unhighlight(9);
			
			//Texte initialisieren
			k_Text = "Iteration k = " + (k - 1);
			tk_Text = "Aktuelle Zeit t_k = t_" + (k - 1) + " = " + t_k;
			
			vars.set("tk", Double.toString(t_k));
			vars.set("yke", "-");
		
			kText = lang.newText(new Offset(10, 2, calcWindow, "NW"), k_Text, "kText", null, FormularTP);
			tkText = lang.newText(new Offset(50, 0,  kText,  "NE"), tk_Text, "tkText", null, FormularTP);
			
			t_k_inc = ((t_0 + (h * k)) * 100) / 100.0;
			
			double rand2 = Math.random();
			if((rand2 <= questionProbability) && question1) {
				MultipleChoiceQuestionModel quest2 = new MultipleChoiceQuestionModel("quest2");
				quest2.setPrompt("Welchen Wert hat t_(k+1) in dieser Iteration?");
				quest2.addAnswer(new AnswerModel("an1", doubleParser(t_k_inc + (h * 1.5)), 5, "FALSCH"));
				quest2.addAnswer(new AnswerModel("an2", doubleParser(t_k_inc + (h * 2.3)), 5, "FALSCH"));
				quest2.addAnswer(new AnswerModel("an3", doubleParser(t_k_inc), 5, "Sehr gut, das ist korrekt"));
				quest2.addAnswer(new AnswerModel("an4", doubleParser(t_k_inc + (h * 0.8)), 5, "FALSCH"));
				lang.addMCQuestion(quest2);
				question1 = false;
			}
			
			lang.nextStep();
			
			//Texte initialisieren
			vars.set("tkinc", Double.toString(t_k_inc));
			tk1_Text = "t_(k+1) = t_0 + (h * (k+1))  =  t_" + k + " = " + t_k_inc;

			tk1Text = lang.newText(new Offset(20, 40, kText, "NW"), tk1_Text, "tk1Text", null, FormularTP);
			tk1Text.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			
			code.highlight(4);
			
			lang.nextStep();
			
			//Vorherige Highlights löschen
			tk1Text.changeColor(null, fontColor, null, null);
			
			code.unhighlight(4);
			
			//Texte initialisieren
			y_k_e = Math.round((y_k + h * (2 * y_k - Math.pow(Math.E, t_k))) * 100) / 100.0; 
			y_k = Math.round((y_k + 0.5 * h * ((2 * y_k - Math.pow(Math.E, t_k)) + (2 * y_k_e - Math.pow(Math.E, t_k_inc)))) * 100) / 100.0;		
			s1_Text = "y_(k+1)_e = y_k + h * f(y_k, t_k) ";	
			s1_Result = "=  " + y_k_e;

			vars.set("yke", Double.toString(y_k_e));
			s1Text = lang.newText(new Offset(0, 50, tk1Text, "NW"), s1_Text, "s1Text", null, FormularTP);
			s1Result = lang.newText(new Offset(96, 30, s1Text, "NW"), s1_Result, "s1Result", null, FormularTP);
			
			s1Text.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			s1Result.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);

			pl = lang.newPolyline(nodes, "pl", null);
			
			code.highlight(6);
			
			lang.nextStep();
			
			//Vorherige Highlights löschen		
			s1Text.changeColor(null, fontColor, null, null);
			s1Result.changeColor(null, fontColor, null, null);
			
			code.unhighlight(6);
			
			//Texte initialisieren
			vars.set("ykinc", Double.toString(y_k));
			s2_Text = "y_(k+1) = y_k + 0.5 * h(f(y_k, t_k) + f(y_(k+1)_e, t_(k+1))) ";
			s2_Result = "= " + y_k;
			
			s2Text = lang.newText(new Offset(-96, 30, s1Result, "NW"), s2_Text, "s2Text", null, FormularTP);
			s2Result = lang.newText(new Offset(74, 30, s2Text, "NW"), s2_Result, "s2Result", null, FormularTP);
			
			s2Text.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			s2Result.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			
			code.highlight(7);
		  
			lang.nextStep();

			//Vorherige Highlights löschen
			s2Text.changeColor(null, fontColor, null, null);
			s2Result.changeColor(null, fontColor, null, null);
			
			code.unhighlight(7);
			
			//Texte initialisieren
			yk_Text = "--> y(" + t_k_inc + ") ~ " + y_k;
			
			ykText = lang.newText(new Offset(10, -30, calcWindow, "SW"), yk_Text, "ykText", null, FormularTP); 
			
			ykText.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
		  
			lang.nextStep();
			
			//Vorherige Highlights löschen
			ykText.changeColor(null, fontColor, null, null);
			
			//Texte initialisieren
			vars.set("k", Integer.toString(k));
			iteration_Text = "k  =  k + 1  =  " + k;
			
			iterationText = lang.newText(new Offset(50, 0, ykText, "NE"), iteration_Text, "iterationText", null, FormularTP); 
			
			iterationText.changeColor(null, (Color)sourceProps.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), null, null);
			
			code.highlight(9);
			
			k++;
			t_k = t_k_inc;
			
			content = "Iterationsschritt" + contentCount;
			contentCount++;
			
			lang.nextStep(content);
		}
	  
		kText.hide();
		tkText.hide();
		tk1Text.hide();
		s1Text.hide();
		s1Result.hide();
		s2Text.hide();
		s2Result.hide();
		ykText.hide();
		iterationText.hide();
		pl.hide();
		
		code.unhighlight(9);
		
		vars.set("yke", "-");
	    
		String resultString = "Ergebnis nach " + t_Max + " Zeiteinheiten: " + y_k;
		String end = "--> f(" + t_Max + ") ist angenähert " + y_k;
		Text result = lang.newText(new Offset(10, 100, calcWindow, "NW"), resultString, "result", null, FormularTP);
		Text endText = lang.newText(new Offset(5, 25, result, "NW"), end, "endText", null, FormularTP);

		lang.nextStep("Endergebniss");
		
		vars.set("tkinc", "-");
		vars.set("k", "-");
		vars.set("t0", "-");
		vars.set("tMax", "-");
		vars.set("h", "-");
		vars.set("y0", "-");
		vars.set("tk", "-");
	  
		lang.hideAllPrimitives();
	  
		headerWindow.show();
		header.show();
		
		formularWindow = lang.newRect(new Offset(0, 10, headerWindow, "SW"), new Coordinates(750, 140), "formularWindow", null, FormRectRP);
		formularHeader = lang.newText(new Offset(10, 10, formularWindow, "NW"), "Fazit:", "formularHeader", null, FormularTP);
		
		calcWindow = lang.newRect(new Offset(0, 10, formularWindow, "SW"), new Coordinates(750, 600), "calcWindow", null, CodeRectRP);
		
		String fazit1 = "Die Genauigkeit des Verfahrens wird durch den lokalen Fehler und den";
		String fazit2 = "globalen Fehler bestimmt. Der lokale Fehler, ist der Fehler der in";
		String fazit3 = "einem einzelnen Iterationsschritt auftritt und der globale Fehler ergibt";
		String fazit4 = "sich aus der Summe der lokalen Fehler nach allen Iterationsschritten.";
		String fazit5 = "Für die Darstellung werden hier Landau-Symbole verwendet und man";
		String fazit6 = "spricht von der Ordnung des Verfahrens.";
		String fazit7 = "Der maximale Fehler eines Verfahrens der Ordnung p ist eine Funktion";
		String fazit8 = "proportional zu h^p oder mit Landau-Symbolen O(h^p), wobei h die";
		String fazit9 = "gewählte Schrittweite ist. Das Heun Verfahren hat die Ordnung 2 und";
		String fazit10 = "damit liegt der maximale Fehler in O(h^2).";
				
		Text fazitT1 = lang.newText(new Offset(10, 2, calcWindow, "NW"), fazit1, "fazit1", null, FormularTP);
		Text fazitT2 = lang.newText(new Offset(0, 25, fazitT1, "NW"), fazit2, "fazit2", null, FormularTP);
		Text fazitT3 = lang.newText(new Offset(0, 25, fazitT2, "NW"), fazit3, "fazit3", null, FormularTP);
		Text fazitT4 = lang.newText(new Offset(0, 25, fazitT3, "NW"), fazit4, "fazit4", null, FormularTP);
		Text fazitT5 = lang.newText(new Offset(0, 25, fazitT4, "NW"), fazit5, "fazit5", null, FormularTP);
		Text fazitT6 = lang.newText(new Offset(0, 25, fazitT5, "NW"), fazit6, "fazit6", null, FormularTP);
		Text fazitT7 = lang.newText(new Offset(0, 25, fazitT6, "NW"), fazit7, "fazit7", null, FormularTP);
		Text fazitT8 = lang.newText(new Offset(0, 25, fazitT7, "NW"), fazit8, "fazit8", null, FormularTP);
		Text fazitT9 = lang.newText(new Offset(0, 25, fazitT8, "NW"), fazit9, "fazit9", null, FormularTP);
		Text fazitT10 = lang.newText(new Offset(0, 25, fazitT9, "NW"), fazit10, "fazit10", null, FormularTP);
	  
		lang.nextStep();
	}
	
	public String doubleParser(double rand){
		rand = (rand * 100.0) / 100.0;
					
		String number = Double.toString(rand);
		String result = "";
		
		for(int i = 0; i < number.length(); i++){
			if(number.charAt(i) == '.'){
				result = result + ',';
			}
			else{
				result = result + number.charAt(i);
			}
		}
		
		return result;
	}
}