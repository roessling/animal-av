package generators.backtracking;

import generators.backtracking.helpers.Constraint;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TreeMap;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Circle;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class CSP implements ValidatingGenerator {
    private Language lang;     
    private RectProperties rectProps;
    private RectProperties stepProps;
	private TextProperties headerProps;
	private SourceCodeProperties textProps;
//	private TreeMap<Integer, String> assigns;
	
	// User Inputs
	private int middlepointOffsetX;
	private int middlepointOffsetY;
	private String[] names;
	private String cons;
	private String constraintString;
    private ArrayProperties ArrayProperties;
    private SourceCodeProperties CodeProperties;    
    private CircleProperties SeatsPropertiesAssigned;
    private CircleProperties SeatsPropertiesFailure;
    private CircleProperties SeatsPropertiesSelected;
    private CircleProperties SeatsPropertiesDefault;
    
    // Auxiliary variables
    private int[][] summaryValues; // [0][x] = Backtracking-Values, [1][x] = Forward-Check-Values. [x][0-2] = backtracks, inconsistent values, steps
    private List<Text> indices;
    private int radius;
	private ArrayList<String> outputCode;
	private HashMap<Integer, Circle> places;
	private HashMap<String, Constraint> constraints;
	
	//PrivateStuff for FC
	
	private StringArray arrayPlaetze;
	private ArrayMarker arrMarker;
	private StringArray arrayPersonen;
//	private Integer stepsFC;
	private Rect stepFC;
	private Text currStepFC;
	private SourceCode CodeFC;

	private Circle tmpCircleFC;
	private Text tmpIndiceFC;
	private int currPosFC;
	private Integer stepsF;
	private Integer backtracksF;
	private Integer inconsistentsF;
//	private boolean[] coverageF;
	
	public CSP() {
	  
	}
	
	public String[] getNames() {
		return names;
	}
	
    public void init() 
    {        
    	lang = new AnimalScript("Constraint-Satisfaction-Problem", "Martin Distler & Simon Werner", 1400, 1050);
    	lang.setStepMode(true);
    	constraints = new HashMap<String, Constraint>();
    	
        textProps = new SourceCodeProperties();
			textProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
			textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, 
				new Font("SansSerif", Font.BOLD, 17));		    
			textProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);   
			textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	
		headerProps = new TextProperties();
			headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
			headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, 
				new Font("SansSerif", Font.BOLD, 18));
			headerProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
		
		stepProps = new RectProperties();
			stepProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			stepProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
			stepProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
			
		rectProps = new RectProperties();
			rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
			rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);		
    }

	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) throws IllegalArgumentException {
    constraintString = (String)primitives.get("constraintString");
    names = (String[])primitives.get("names");
    middlepointOffsetX = (Integer)primitives.get("middlepointOffsetX");
    middlepointOffsetY = (Integer)primitives.get("middlepointOffsetY");
		if((names.length >= 3) && (constraintString.length() != 0))
			return true;
		else
			return false;
	}
    
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
      constraintString = (String)primitives.get("constraintString");
      names = (String[])primitives.get("names");
      middlepointOffsetX = (Integer)primitives.get("middlepointOffsetX");
      middlepointOffsetY = (Integer)primitives.get("middlepointOffsetY");
    	  CodeProperties = (SourceCodeProperties)props.getPropertiesByName("CodeProperties");
        ArrayProperties = (ArrayProperties)props.getPropertiesByName("ArrayProperties");
        SeatsPropertiesAssigned = (CircleProperties)props.getPropertiesByName("SeatsPropertiesAssigned");
        SeatsPropertiesFailure = (CircleProperties)props.getPropertiesByName("SeatsPropertiesFailure");
        SeatsPropertiesSelected = (CircleProperties)props.getPropertiesByName("SeatsPropertiesSelected");
        SeatsPropertiesDefault = (CircleProperties)props.getPropertiesByName("SeatsPropertiesDefault");


		try{
			cons = this.generateConstraints(constraintString);
		}
		catch(Exception e) {
			TextProperties error = new TextProperties();
				error.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.red);
				error.set(AnimationPropertiesKeys.FONT_PROPERTY, 
						new Font("SansSerif", Font.BOLD, 21));
				error.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
				
			lang.newText(new Coordinates(50, 50), "ERROR:", "error", null, error);
			SourceCode errorMsg = lang.newSourceCode(new Coordinates(92, 92), "error", null, CodeProperties);
			
			errorMsg.addCodeLine("Beim parsen des Constraint-Strings ist ein Fehler aufgetreten.", "", 0, null);
			errorMsg.addCodeLine("Aus diesem Grund wurde die Animation mit dem Standard-String", "", 0, null);
			errorMsg.addCodeLine("Martin: !Gerrit. Christine: !Anton. Gerrit: !Martin, !Christine, !Simon", "", 1, null);
			errorMsg.addCodeLine("erstellt.", "", 0, null);
			errorMsg.addCodeLine("Falls dies nicht gewuenscht ist muss der Constraint-String neu eingegeben und ueberprueft", "", 0, null);
			errorMsg.addCodeLine("sowie die Animation neu generiert werden.", "", 0, null);
			errorMsg.addCodeLine(" ", "", 0, null);
			errorMsg.addCodeLine("Zur Erinnerung", "erinnerung", 0, null);
			errorMsg.addCodeLine("Es sind nur das Alphabet (A - Z) sowie die festgelegte Zeichenmenge {: , . !} erlaubt.", "", 0, null);
			errorMsg.addCodeLine("Auch darf der String nicht leer sein.", "", 0, null);
			errorMsg.highlight("erinnerung");
			
			names = new String[]{"Martin", "Christine", "Olaf", "Bernd", "Klaus", "Paul", "Anton", "Simon", "Gerrit", "Lars"};
			constraintString = "Martin: !Gerrit. Christine: !Anton. Gerrit: !Martin, !Christine, !Simon";
			
			try {
				cons = this.generateConstraints(constraintString);
			} catch (Exception ex) {
				System.out.println("Watt, das duerfte aber nicht passieren. Simon ist schuld");
			}
			lang.nextStep();
			lang.addLine("hideAll");
		}
		this.Slides();
    	return lang.toString();
    }
    
    public String getName() {
        return "CSP - Backtracking & Forward Check";
    }

    public String getAlgorithmName() {
        return "CSP - Backtracking & Forward Check";
    }

    public String getAnimationAuthor() {
        return "Martin Distler, Simon Werner";
    }

	public String getDescription() {
		return "In der Kuenstlichen Intelligenz gibt es oft Probleme, die unter bestimmten Bedingungen zu loesen sind. Diese Art von Problemen bilden eine eigene Problemklasse die Constraint-Satisfaction-Problems (zu deutsch recht holprig: Bedingungserfuellungsproblem, abgekuerzt CSP) genannt werden."
				+ "\n"
				+ "In diesem Generator wird der naive Ansatz (Backtracking) sowie intuitive Ansatz (Backtracking + Forward Check) anhand eines Beispiels angewendet um den Kerngedanke der maschinellen Problemloesung von CPSs zu skizzieren."
				+ "\n"
				+ "Am Ende des Generators werden die Ergebnisse der beiden Ansaetze miteinander verglichen."
				+ "\n"
				+ "\n"
				+ "Hinweise:"
				+ "\n"
				+ "In diesem Generator koennen Namen, Constraints sowie die Farben des Beispiels geaendert werden."
				+ "\n"
				+ " Constraints werden ueber die Syntax"
				+ "\n"
				+ "		'name': '!nichtErlaubtePerson'"
				+ "\n"
				+ "	erstellt, wobei '!nichtErlaubtePerson' nicht neben 'name' sitzen darf."
				+ "\n"
				+ "Es ist eine beliebige Anzahl an unerwuenschten Personen erlaubt, hingegen darf nur eine Person vor dem Doppelpunkt stehen."
				+ "\n"
				+ "	(2) Die Anzahl der Namen muss kleiner-gleich 10 sein aber mindestens 3 betragen."
				+ "\n\n Die Eingabemoeglichkeit middlepointOffsetX sowie die Y Variante dienen der Verschiebung des Tisches in X oder Y Richtung."
				+ "\n Dadurch kann man eine erhoehte Anzahl Personen eintragen - muss allerdings dafuer den Tisch passend verschieben,"
				+ "andernfalls ist das Layout verschoben und ueberlagert sich zum teil."
				+ "\n\n Das Layout ist fuer bis zu 14 Personen am Tisch ausgelegt und getestet.";
	}

	public String getCodeExample() {
		return "Backtracking in Pseudo-Code"
				+ "\n"
				+ "\n"
				+ "BT(A, U) "
				+ "\n"
				+ "{"
				+ "\n"
				+ "	if (A is complete)"
				+ "\n"
				+ "	     return A"
				+ "\n"
				+ "	Remove a variable X from U"
				+ "\n"
				+ "	for all (values x in D(X)) "
				+ "\n"
				+ "	{"
				+ "\n"
				+ "	   if ('X <= x' is consistent with A according to the constraints) "
				+ "\n" + "	   {" + "\n" + "	         Add 'X <= x' to A" + "\n"
				+ "	         result = BT(A, U)" + "\n" + "	         " + "\n"
				+ "	         if (result != failure)" + "\n"
				+ "	              return result" + "\n" + "	         " + "\n"
				+ "	         Remove 'X <= x' from A" + "\n" + "	   }" + "\n"
				+ "	}" + "\n" + "     return failure" + "\n" + "}";
	}

    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public Locale getContentLocale() {
        return Locale.GERMANY;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_BACKTRACKING);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    
    public void Slides()
    {
    	this.Intro();
    	this.summaryValues = new int[2][3];
    	this.Backtracking();
    	this.Transition();
    	this.ForwardCheck();
    	this.Summary();
    }
    
    /**
	 * Intro-Slides
	 */
	private void Intro() {
		// Page 1
		Text header = lang.newText(new Coordinates(20, 10), "Das Constraint-Satisfaction-Problem - Einleitung", "header", null, headerProps);		
			Offset offsetLeft = new Offset(-5, -5, header, AnimalScript.DIRECTION_NW);
			Offset offsetRight = new Offset(5, 5, header, AnimalScript.DIRECTION_SE);
//		Rect hRect = 
		    lang.newRect(offsetLeft, offsetRight, "header", null, rectProps);		
		SourceCode einleitung = lang.newSourceCode(new Coordinates(20, 40), "sourceCode",	
				null, textProps);

		einleitung.addCodeLine("In der Künstlichen Intelligenz gibt es oft Probleme, die unter bestimmten Bedingungen zu loesen sind.", null, 0, null);
		einleitung.addCodeLine("Diese Art von Problemen bilden eine eigene Problemklasse die Constraint-Satisfaction-Problems", null, 0, null); 
		einleitung.addCodeLine("(zu deutsch recht holprig: Bedingungserfüllungsproblem, abgekürzt CSP) genannt werden.", null, 0, null); 
		einleitung.addCodeLine("", null, 0, null);
		einleitung.addCodeLine("Ein bekanntes Beispiel hierfür ist Sudoku:", null, 0, null); 
		einleitung.addCodeLine("Ziel ist es ein 9x9-Gitter mit den Ziffern 1 bis 9 so zu füllen, sodass jede Ziffer in jeder Spalte,", null, 0, null); 
		einleitung.addCodeLine("in jeder Zeile und in jedem Unterquadrat (3x3-Gitter) genau einmal vorkommt.", null, 0, null); 
		einleitung.addCodeLine("", null, 0, null);
		einleitung.addCodeLine("Das zu erreichende Ziel ist also: komplett ausgefülltes 9x9-Gitter", null, 0, null); 
		einleitung.addCodeLine("Unter den Bedingungen: jede Zahl nur", null, 0, null); 
		einleitung.addCodeLine("1x pro Spalte", null, 1, null); 
		einleitung.addCodeLine("1x pro Zeile", null, 1, null); 
		einleitung.addCodeLine("1x pro Unterquadrat und Zeile sowie im Unterquadrat", null, 1, null); 
		einleitung.addCodeLine("", null, 0, null);
		einleitung.addCodeLine("Im Folgenden wird der naive Ansatz (Backtracking) sowie intuitive Ansatz (Backtracking + Forward Check)", null, 0, null); 
		einleitung.addCodeLine("anhand eines Beispiels angewendet um den Kerngedanke der maschinellen Problemloesung von CPSs zu skizzieren.", null, 0, null); 
		einleitung.addCodeLine("Am Ende werden beide Ansätze miteinander verglichen.", null, 0, null); 
		
		lang.nextStep();
		lang.addLine("hideAll");
		
		// Page 2
		header = lang.newText(new Coordinates(20, 10), "CSP: Anwendung im täglichen Leben", "header", null, headerProps);
//		hRect = 
		    lang.newRect(offsetLeft, offsetRight, "header", null, rectProps);		
		einleitung = lang.newSourceCode(new Coordinates(20, 40), "sourceCode",
				null, textProps);
		
		einleitung.addCodeLine("Ein weiteres Beispiel wäre die Tischbelegung bei groesseren Anlässen, beispielsweise einer Hochzeit oder einer Gala.", null, 0, null);
		einleitung.addCodeLine("Ein solches Szenario wird hier als Beispiel zum illustrieren der Algorithmen verwendet: 10 Personen sollen an einem Tisch mit 10 Plätzen sitzen.", null, 0, null); 
		einleitung.addCodeLine("", null, 0, null);
		
		
		einleitung.addCodeLine("Wie es allerdings immer so ist, kann sich nicht jeder der Personen untereinander leiden, woraus sich einige Bedingungen bilden:", null, 0, null); 
		
		String[] temp = cons.split("\n");
		for (int i = 0; i < temp.length; i++) 
		{
			einleitung.addCodeLine(temp[i],null,1,null);
		}
		
		einleitung.addCodeLine(" ", null, 1, null); 
		einleitung.addCodeLine("(6) Des Weiteren kommt noch die logische Bedingung hinzu das jede Person auf genau einem Platz sitzt.", null, 1, null); 
		
		lang.nextStep();
		lang.addLine("hideAll");
		
		// Page 3
		header = lang.newText(new Coordinates(20, 10), "CSP: Backtracking-Algorithmus", "header", null, headerProps);
//		hRect = 
		    lang.newRect(offsetLeft, offsetRight, "header", null, rectProps);		
		einleitung = lang.newSourceCode(new Coordinates(20, 40), "sourceCode",
				null, textProps);
		
		einleitung.addCodeLine("Als erster Ansatz sei Backtracking betrachtet.", null, 0, null);
		einleitung.addCodeLine("Die Variablennamen bedeuten folgendes: (in Klammern: Bedeutung der Variable im Kontext des Beispiels)", null, 0, null);
		einleitung.addCodeLine("A = Assignments, Zuweisungen (Platz = Person)", null, 1, null);
		einleitung.addCodeLine("U = Unassigned variables, nicht zugewiesene Variablen (Menge der freien Plätze)", null, 1, null);
		einleitung.addCodeLine("D = Domains, Domänen / Wertebereiche (Menge der moeglichen Personen des jeweiligen Platzes)", null, 1, null);
		einleitung.addCodeLine("Hinweis", null, 0, null);
		einleitung.addCodeLine("Eine Domäne beschreibt den Wertebereich der zugehoerigen Variable. Ein Beispiel wäre eine Telefonnummer:", null, 1, null);
		einleitung.addCodeLine("Eine 4 Stellige Telefonnummern hat 4 Variablen (= 4 Domänen) die jeweils nur im Wertebereich 0-9 liegen koennen.", null, 1, null);
		einleitung.addCodeLine("Somit wäre die Domäne für jede Variable D(Stelle) = {0, 1, ..., 9}", null, 1, null);
		einleitung.addCodeLine("Da im Rahmen dieses Beispiels jede Person einem Platz zugewiesen wird, kann sie aufgrund von Bedingung (6) auf keinem anderem Platz mehr sitzen.", null, 1, null);
		einleitung.addCodeLine("Aus diesem Grund teilt sich hier beim Backtracking jeder Sitzplatz eine gemeinsame Domäne - also D(Sitz1) = D(Sitz2) = ... = D(Sitz10)", null, 1, null);
		einleitung.addCodeLine("Daraus folgt: die Domäne verkleinert sich jede Zuweisung um eine Person und bleibt für alle Sitzplätze gleich.", null, 1, null);
		
		SourceCode backtracking = lang.newSourceCode(new Coordinates(20, 300), "sourceCode", null, CodeProperties);
		
		backtracking.addCodeLine("BT(A, U)", null, 0, null);
		backtracking.addCodeLine("{", null, 0, null);
		backtracking.addCodeLine("if (A is complete)	", null, 1, null);		
		backtracking.addCodeLine("return A", null, 2, null);
		backtracking.addCodeLine("Remove a variable X from U", null, 1, null);
		backtracking.addCodeLine("for all (values x in D(X))", null, 1, null);
		backtracking.addCodeLine("{", null, 1, null);
		backtracking.addCodeLine("if ('X <= x' is consistent with A", null, 2, null);
		backtracking.addCodeLine("according to the constraints)", null, 5, null);		
		backtracking.addCodeLine("{", null, 2, null);
		backtracking.addCodeLine("Add 'X <= x' to A", null, 3, null);
		backtracking.addCodeLine("result = BT(A, U)", null, 3, null);
		backtracking.addCodeLine("if (result != failure)", null, 3, null);		
		backtracking.addCodeLine("return result", null, 4, null);
		backtracking.addCodeLine("Remove 'X <= x' from A", null, 3, null);
		backtracking.addCodeLine("}", null, 2, null);
		backtracking.addCodeLine("}", null, 1, null);
		backtracking.addCodeLine("return failure", null, 1, null);		
		backtracking.addCodeLine("}", null, 0, null);
	}
	
	/**
	 * Backtracking-Slides
	 */
	private void Backtracking() {		
		lang.nextStep();				
		lang.addLine("hideAll");
		indices = new ArrayList<Text>();
		places = new HashMap<Integer, Circle>();

		  ////////////////////////
		 // Start Backtracking //
		////////////////////////
		
//		Integer backtracks = 0;
//		Integer inconsistents = 0;
//		Integer steps = 0;
		
		// Header
		Text header = lang.newText(new Coordinates(20, 10), "CSP: Backtracking-Algorithmus", "header", null, headerProps);
		Offset offsetLeft = new Offset(-5, -5, header, AnimalScript.DIRECTION_NW);
		Offset offsetRight = new Offset(5, 5, header, AnimalScript.DIRECTION_SE);
//		Rect hRect = 
		    lang.newRect(offsetLeft, offsetRight, "header", null, rectProps);

		// Render Scene
		this.RenderScene();
		
		// Scource Code Backtracking
		SourceCode backtracking = lang.newSourceCode(new Coordinates(550 + middlepointOffsetX, 360 + radius*10 + middlepointOffsetY), "sourceCode",
				null, CodeProperties);
		
		backtracking.addCodeLine("BT(A, U)", null, 0, null);
		backtracking.addCodeLine("{", null, 0, null);
		backtracking.addCodeLine("if (A is complete)	", null, 1, null);		
		backtracking.addCodeLine("return A", null, 2, null);
		backtracking.addCodeLine("Remove a variable X from U", null, 1, null);
		backtracking.addCodeLine("for all (values x in D(X))", null, 1, null);
		backtracking.addCodeLine("{", null, 1, null);
		backtracking.addCodeLine("if ('X <= x' is consistent with A", null, 2, null);
		backtracking.addCodeLine("according to the constraints)", null, 5, null);	
		backtracking.addCodeLine("{", null, 2, null);
		backtracking.addCodeLine("Add 'X <= x' to A", null, 3, null);
		backtracking.addCodeLine("result = BT(A, U)", null, 3, null);
		backtracking.addCodeLine("if (result != failure)", null, 3, null);		
		backtracking.addCodeLine("return result", null, 4, null);
		backtracking.addCodeLine("Remove 'X <= x' from A", null, 3, null);
		backtracking.addCodeLine("}", null, 2, null);
		backtracking.addCodeLine("}", null, 1, null);
		backtracking.addCodeLine("return failure", null, 1, null);		
		backtracking.addCodeLine("}", null, 0, null);
		
		lang.nextStep();
		
		// Used Variables & Names
		String domainBT[] = names;
		StringArray domain = lang.newStringArray(new Coordinates(1070 + middlepointOffsetX + radius*10, 280 + middlepointOffsetY), domainBT, "domainArr", null, ArrayProperties);		
		StringArray[] arrays = {domain};
		
		// Prepare Variables
		outputCode = new ArrayList<String>();
		ArrayList<Integer> variables = new ArrayList<Integer>();
		for(int i = 0; i < names.length; i++)
			variables.add(i+1);
		
		List<String> domainList = new ArrayList<String>();
		
		for(String st : domainBT)
			domainList.add(st.toLowerCase());
		
		// Start Animation Backtracking & Create Code
//		assigns = 
		    this.Backtracking(new TreeMap<Integer, String>(), variables, domainList);		
		this.createAnimation(false, arrays);
		
		
	}
	
	private void Transition() {
		  ////////////////////
		 // Zwischen Folie //
		////////////////////
		
		 lang.nextStep();
		 lang.addLine("hideAll");
		
		Text header = lang.newText(new Coordinates(20, 10), "CSP: Backtracking-Algorithmus mit Forward Check", "header", null, headerProps);
			Offset offsetLeft = new Offset(-5, -5, header, AnimalScript.DIRECTION_NW);
			Offset offsetRight = new Offset(5, 5, header, AnimalScript.DIRECTION_SE);
//			Rect hRect = 
			    lang.newRect(offsetLeft, offsetRight, "header", null, rectProps);
			
		SourceCode einleitung = lang.newSourceCode(new Coordinates(20, 40), "sourceCode",
					null, textProps);
			
			einleitung.addCodeLine("Als zweiter Ansatz sei Backtracking mit Forward Check betrachtet.", null, 0, null);
			einleitung.addCodeLine("Die Variablennamen bedeuten folgendes: (in Klammern: Bedeutung der Variable im Kontext des Beispiels)", null, 0, null);
			einleitung.addCodeLine("A = Assignments, Zuweisungen (Platz = Person)", null, 1, null);
			einleitung.addCodeLine("U = Unassigned variables, nicht zugewiesene Variablen (Menge der freien Plätze)", null, 1, null);
			einleitung.addCodeLine("D = Domains, Domänen / Wertebereiche (Menge der moeglichen Personen des jeweiligen Platzes)", null, 1, null);
			einleitung.addCodeLine("Hinweis", null, 0, null);
			einleitung.addCodeLine("Im Gegensatz zum schlichten Backtracking Algorithmus verwendet dieser Ansatz für jeden Platz eine eigene Domäne.", null, 1, null);
			
		SourceCode backtracking = lang.newSourceCode(new Coordinates(20, 250), "sourceCode",
					null, CodeProperties);

		backtracking.addCodeLine("BT+FC(A,U,D)", null, 0, null);
		backtracking.addCodeLine("{", null, 0, null);
		backtracking.addCodeLine("if (A is complete)	", null, 1, null);		
		backtracking.addCodeLine("return A", null, 2, null);
		backtracking.addCodeLine("Remove a variable X from U", null, 1, null);
		backtracking.addCodeLine("for all (values x in D(X))", null, 1, null);
		backtracking.addCodeLine("{", null, 1, null);
		backtracking.addCodeLine("if ('X <= x' is consistent with A", null, 2, null);
		backtracking.addCodeLine("according to the constraints)", null, 5, null);	
		backtracking.addCodeLine("{", null, 2, null);
		backtracking.addCodeLine("Add 'X <= x' to A", null, 3, null);
		backtracking.addCodeLine("D' = D (save the current domains)", null, 3, null);
		backtracking.addCodeLine("for all (Y in U)", null, 3, null);
		backtracking.addCodeLine("Remove values for Y from D'(Y) that are inconsistent with A", null, 4, null);
		backtracking.addCodeLine("if (for all (Y in U) D'(Y) is not empty)", null, 3, null);
		backtracking.addCodeLine("{", null, 3, null);
		backtracking.addCodeLine("result = BT+FC(A,U,D')", null, 4, null);
		backtracking.addCodeLine("if (result != failure)", null, 4, null);
		backtracking.addCodeLine("return result", null, 5, null);
		backtracking.addCodeLine("}", null, 3, null);
		backtracking.addCodeLine("Remove 'X <= x' from A", null, 3, null);
		backtracking.addCodeLine("}", null, 2, null);
		backtracking.addCodeLine("}", null, 1, null);
		backtracking.addCodeLine("return failure", null, 1, null);		
		backtracking.addCodeLine("}", null, 0, null);
	}

	private void ForwardCheck() {
		  ////////////////////////
		 // Start Forwardcheck //
		////////////////////////		
	
		lang.nextStep();
		lang.addLine("hideAll");
		Text header = lang.newText(new Coordinates(20, 10), "CSP: Backtracking-Algorithmus mit Forward Check", "header2", null, headerProps);
		Offset offsetLeft = new Offset(-5, -5, header, AnimalScript.DIRECTION_NW);
		Offset offsetRight = new Offset(5, 5, header, AnimalScript.DIRECTION_SE);
//		Rect hRect = 
		lang.newRect(offsetLeft, offsetRight, "header2", null, rectProps);
		
		indices = new ArrayList<Text>();
		
		// Render Scene
		this.RenderScene();
		
		SourceCode backtracking = lang.newSourceCode(new Coordinates(550, 360), "sourceCode",
				null, CodeProperties);
		
		
		backtracking = lang.newSourceCode(new Coordinates(550 + middlepointOffsetX, 360 + radius*10 + middlepointOffsetY), "sourceCode",
				null, CodeProperties);
		
		backtracking.addCodeLine("BT+FC(A,U,D)", null, 0, null);
		backtracking.addCodeLine("{", null, 0, null);
		backtracking.addCodeLine("if (A is complete)	", null, 1, null);		
		backtracking.addCodeLine("return A", null, 2, null);
		backtracking.addCodeLine("Remove a variable X from U", null, 1, null);
		backtracking.addCodeLine("for all (values x in D(X))", null, 1, null);
		backtracking.addCodeLine("{", null, 1, null);
		backtracking.addCodeLine("if ('X <= x' is consistent with A", null, 2, null);
		backtracking.addCodeLine("according to the constraints)", null, 5, null);	
		backtracking.addCodeLine("{", null, 2, null);
		backtracking.addCodeLine("Add 'X <= x' to A", null, 3, null);
		backtracking.addCodeLine("D' = D (save the current domains)", null, 3, null);
		backtracking.addCodeLine("for all (Y in U)", null, 3, null);
		backtracking.addCodeLine("Remove values for Y from D'(Y) that are inconsistent with A", null, 4, null);
		backtracking.addCodeLine("if (for all (Y in U) D'(Y) is not empty)", null, 3, null);
		backtracking.addCodeLine("{", null, 3, null);
		backtracking.addCodeLine("result = BT+FC(A,U,D')", null, 4, null);
		backtracking.addCodeLine("if (result != failure)", null, 4, null);
		backtracking.addCodeLine("return result", null, 5, null);
		backtracking.addCodeLine("}", null, 3, null);
		backtracking.addCodeLine("Remove 'X <= x' from A", null, 3, null);
		backtracking.addCodeLine("}", null, 2, null);
		backtracking.addCodeLine("}", null, 1, null);
		backtracking.addCodeLine("return failure", null, 1, null);		
		backtracking.addCodeLine("}", null, 0, null);
		
		
		String plaetzeFC[] = new String[names.length+1];
		plaetzeFC[0] = "Sitz";
		for(int i = 0; i < names.length; i++) {
			plaetzeFC[i+1] = ""+(i+1);
		}
		arrayPlaetze = lang.newStringArray(new Coordinates(1070 + middlepointOffsetX + radius*10, 280 + middlepointOffsetY), plaetzeFC, "plaetreArr", null, ArrayProperties);
		
		arrMarker = lang.newArrayMarker(arrayPlaetze, 1, "domainMarker", null);
		
		String arrayPersonenFC[] = new String[names.length+1];
		arrayPersonenFC[0] = "Person";
		
		for (int i = 1; i < arrayPersonenFC.length; i++)
		{
			arrayPersonenFC[i] = "";
			for (String initial : getInitials(names)) 
					arrayPersonenFC[i] += initial + " "; 
		}
		
		arrayPersonen = lang.newStringArray(new Coordinates(1120 + middlepointOffsetX + radius*10, 280 + middlepointOffsetY), arrayPersonenFC, "personenArr", null, ArrayProperties);
		
		TextProperties tmpProps = new TextProperties();
		tmpProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		tmpProps.set(AnimationPropertiesKeys.FONT_PROPERTY, 
				new Font("SansSerif", Font.BOLD, 16));
		tmpProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		
		Text cStepFC = lang.newText(new Coordinates(860 + middlepointOffsetX + radius*10, 270 + middlepointOffsetY), "Schritte: ",
				"#s", null, tmpProps);
		
		tmpProps = new TextProperties();
		tmpProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		tmpProps.set(AnimationPropertiesKeys.FONT_PROPERTY, 
				new Font("SansSerif", Font.PLAIN, 12));
		tmpProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		
		stepFC = lang.newRect(new Offset(10, 0, cStepFC, AnimalScript.DIRECTION_NE), 
				new Offset(11, 0, cStepFC, AnimalScript.DIRECTION_SE), "stepRect", null, stepProps);
		currStepFC = lang.newText(new Offset(-5, 5, stepFC,
				AnimalScript.DIRECTION_C), "", "#sn", null, tmpProps);
		
//		stepsFC = 0;
		
		CodeFC = lang.newSourceCode(new Coordinates(20, 30), "sourceCode", null, CodeProperties);

		
		ArrayList<Integer> variables = new ArrayList<Integer>();
		for(int i = 0; i < names.length; i++)
			variables.add(i+1);
		
		List<String> domainList = new ArrayList<String>();
		
		for(String st : names)
			domainList.add(st.toLowerCase());
		
		HashMap<Integer, List<String>> domainsFC = new HashMap<Integer, List<String>>();
		
		for (Integer i : variables)
		{
			domainsFC.put(i, new ArrayList<String>(domainList));
		}
		
		
		currPosFC = 0;
//		Integer stepF = 1;
		stepsF = 0;
		backtracksF = 0;
		inconsistentsF = 0;
//		coverageF = new boolean[names.length];
		
		// Start Animation Backtracking & Create Code
//		assigns = 
		    this.Forward(new TreeMap<Integer, String>(), variables, domainsFC);	
		
		lang.nextStep();
		
		CodeFC.addCodeLine("", null, 0, null);
		CodeFC.addCodeLine("Steps: " + stepsF + ", Inconsistent Assigns: " + inconsistentsF + ", Bachtracks: " + backtracksF, null, 0, null);
		
		summaryValues[1][0] = backtracksF;
		summaryValues[1][1] = inconsistentsF;
		summaryValues[1][2] = stepsF;
		
		
		
	}
	
	private void Summary() {
		  ////////////////////
		 // Abschlussfolie //
		////////////////////		

		lang.nextStep();
		lang.addLine("hideAll");
		Text header = lang.newText(new Coordinates(20, 10), "CSP: Vergleich", "header", null, headerProps);
			Offset offsetLeft = new Offset(-5, -5, header, AnimalScript.DIRECTION_NW);
			Offset offsetRight = new Offset(5, 5, header, AnimalScript.DIRECTION_SE);
//			Rect hRect =
			lang.newRect(offsetLeft, offsetRight, "header", null, rectProps);
			
		SourceCode einleitung = lang.newSourceCode(new Coordinates(20, 40), "sourceCode",
					null, textProps);
			
			einleitung.addCodeLine("Vergleichen wir die beiden Ansätze miteinander, sehen wir", null, 0, null);
			einleitung.addCodeLine("das der Forward-Check den Backtraching-Algorithmus um einiges verbessert.", null, 0, null);
			einleitung.addCodeLine("", null, 1, null);
			einleitung.addCodeLine("Bildlich vorgestellt entspricht ein Problem einem Loesungsgraphen, wobei", null, 0, null);
			einleitung.addCodeLine("der Backtracking-Ansatz jede Moeglichkeit betrachtet und die erstbeste", null, 0, null);
			einleitung.addCodeLine("Zuweisung nimmt. Mit Forward-Check hingegen werden durch das Entfernen der", null, 0, null);
			einleitung.addCodeLine("Belegungsmoeglichkeiten viele Unter- und Teilbäume abgeschnitten, was", null, 0, null);
			einleitung.addCodeLine("die Suche erheblich erleichtert und beschleunigt.", null, 0, null);
			einleitung.addCodeLine("", null, 0, null);
			
			lang.addLine("grid \"vergleich\" (20, 250) lines 3 columns 4 style plain highlightFillColor orange align center");
			lang.addLine("highlightGridCell \"vergleich[1][]\"");
			lang.addLine("setGridValue \"vergleich[0][1]\" \"Backtracks\"");
			lang.addLine("setGridValue \"vergleich[0][2]\" \"Falsche Zuweisungen\"");
			lang.addLine("setGridValue \"vergleich[0][3]\" \"Schritte\"");
			lang.addLine("setGridValue \"vergleich[1][0]\" \"Backtracking\"");
			lang.addLine("setGridValue \"vergleich[1][1]\" \"" + this.summaryValues[0][0] + "\"");
			lang.addLine("setGridValue \"vergleich[1][2]\" \"" + this.summaryValues[0][1] + "\"");
			lang.addLine("setGridValue \"vergleich[1][3]\" \"" + this.summaryValues[0][2] + "\"");
			lang.addLine("setGridValue \"vergleich[2][0]\" \"Backtracking + FC\"");
			lang.addLine("setGridValue \"vergleich[2][1]\" \"" + this.summaryValues[1][0] + "\"");
			lang.addLine("setGridValue \"vergleich[2][2]\" \"" + this.summaryValues[1][1] + "\"");
			lang.addLine("setGridValue \"vergleich[2][3]\" \"" + this.summaryValues[1][2] + "\" refresh");
	}
	
	/**
	 * Sets values to the input array
	 * @param input the String Array
	 * @param i current step
	 * @returns changed input-array
	 */
	public StringArray SetValues(StringArray input, int i) {
		switch (i) {
		case 0:
			input.put(1, "Martin", new TicksTiming(100), null);

			input.put(2, "C O B K P A S L", new TicksTiming(100), null);
			input.put(3, "O B K P A S G L", new TicksTiming(100), null);
			input.put(4, "O B K P A S G L", new TicksTiming(100), null);
			input.put(5, "O B K P A S G L", new TicksTiming(100), null);
			input.put(6, "O B K P A S G L", new TicksTiming(100), null);
			input.put(7, "O B K P A S G L", new TicksTiming(100), null);
			input.put(8, "O B K P A S G L", new TicksTiming(100), null);
			input.put(9, "O B K P A S G L", new TicksTiming(100), null);
			input.put(10, "C O B K P A S L", new TicksTiming(100), null);
			break;

		case 1:

			input.put(2, "Christine", new TicksTiming(100), null);

			input.put(3, "B", new TicksTiming(100), null);
			input.put(4, "O B K P A S G L", new TicksTiming(100), null);
			input.put(5, "O B K P A S G L", new TicksTiming(100), null);
			input.put(6, "O B K P A S G L", new TicksTiming(100), null);
			input.put(7, "O B K P A S G L", new TicksTiming(100), null);
			input.put(8, "O B K P A S G L", new TicksTiming(100), null);
			input.put(9, "O B K P A S G L", new TicksTiming(100), null);
			input.put(10, "O B K P A S L", new TicksTiming(100), null);
			break;

		case 2:

			input.put(3, "Bernd", new TicksTiming(100), null);

			input.put(4, "O K P A S G L", new TicksTiming(100), null);
			input.put(5, "O K P A S G L", new TicksTiming(100), null);
			input.put(6, "O K P A S G L", new TicksTiming(100), null);
			input.put(7, "O K P A S G L", new TicksTiming(100), null);
			input.put(8, "O K P A S G L", new TicksTiming(100), null);
			input.put(9, "O K P A S G L", new TicksTiming(100), null);
			input.put(10, "O K P A S L", new TicksTiming(100), null);
			break;

		case 3:

			input.put(4, "Olaf", new TicksTiming(100), null);
			input.put(5, "K", new TicksTiming(100), null);
			input.put(6, "K P A S G L", new TicksTiming(100), null);
			input.put(7, "K P A S G L", new TicksTiming(100), null);
			input.put(8, "K P A S G L", new TicksTiming(100), null);
			input.put(9, "K P A S G L", new TicksTiming(100), null);
			input.put(10, "K P A S L", new TicksTiming(100), null);
			break;

		case 4:

			input.put(5, "Klaus", new TicksTiming(100), null);

			input.put(6, "P A S G L", new TicksTiming(100), null);
			input.put(7, "P A S G L", new TicksTiming(100), null);
			input.put(8, "P A S G L", new TicksTiming(100), null);
			input.put(9, "P A S G L", new TicksTiming(100), null);
			input.put(10, "P A S L", new TicksTiming(100), null);
			break;

		case 5:

			input.put(6, "Paul", new TicksTiming(100), null);
			input.put(7, "A S G L", new TicksTiming(100), null);
			input.put(8, "A S G L", new TicksTiming(100), null);
			input.put(9, "A S G L", new TicksTiming(100), null);
			input.put(10, "A S L", new TicksTiming(100), null);
			break;

		case 6:

			input.put(7, "Anton", new TicksTiming(100), null);
			input.put(8, "S G L", new TicksTiming(100), null);
			input.put(9, "S G L", new TicksTiming(100), null);
			input.put(10, "S L", new TicksTiming(100), null);
			break;

		case 7:

			input.put(8, "Simon", new TicksTiming(100), null);
			input.put(9, "L", new TicksTiming(100), null);
			input.put(10, "L", new TicksTiming(100), null);
			break;

		case 8:

			input.put(9, "Lars", new TicksTiming(100), null);
			input.put(10, "", new TicksTiming(100), null);
			break;

		case 9:

			input.put(8, "S G L", new TicksTiming(100), null);
			input.put(9, "S G L ", new TicksTiming(100), null);
			input.put(10, "S L", new TicksTiming(100), null);
			break;

		case 10:

			input.put(8, "Gerrit", new TicksTiming(100), null);
			input.put(9, "L ", new TicksTiming(100), null);
			input.put(10, "S L", new TicksTiming(100), null);
			break;

		case 11:

			input.put(9, "Lars", new TicksTiming(100), null);
			input.put(10, "S", new TicksTiming(100), null);
			break;

		case 12:

			input.put(10, "Simon", new TicksTiming(100), null);
			break;

		default:
			break;
		}
		return input;
	}
	
	/**
	 * Renders the Scene
	 */
	public void RenderScene(){
		/*// Tisch
		lang.newRect(new Coordinates(600, 100), new Coordinates(750, 300), "table", null, rectProps);	

		// 2 Oben
		places.put(1, lang.newCircle(new Coordinates(640, 70), 25, "node1", null, SeatsPropertiesDefault));
			indices.add(lang.newText(new Offset(-5, -10, "node1", AnimalScript.DIRECTION_C), "1", "indice", null, headerProps));
		places.put(2, lang.newCircle(new Coordinates(710, 70), 25, "node2", null, SeatsPropertiesDefault));
			indices.add(lang.newText(new Offset(-5, -10, "node2", AnimalScript.DIRECTION_C), "2", "indice", null, headerProps));
			
		// 3 Rechts
		places.put(3, lang.newCircle(new Coordinates(790, 140), 25, "node3", null, SeatsPropertiesDefault));
			indices.add(lang.newText(new Offset(-5, -10, "node3", AnimalScript.DIRECTION_C), "3", "indice", null, headerProps));
		places.put(4, lang.newCircle(new Coordinates(790, 200), 25, "node4", null, SeatsPropertiesDefault));
			indices.add(lang.newText(new Offset(-5, -10, "node4", AnimalScript.DIRECTION_C), "4", "indice", null, headerProps));
		places.put(5, lang.newCircle(new Coordinates(790, 260), 25, "node5", null, SeatsPropertiesDefault));
			indices.add(lang.newText(new Offset(-5, -10, "node5", AnimalScript.DIRECTION_C), "5", "indice", null, headerProps));
			
		// 2 Unten
		places.put(6, lang.newCircle(new Coordinates(710, 330), 25, "node6", null, SeatsPropertiesDefault));
			indices.add(lang.newText(new Offset(-5, -10, "node6", AnimalScript.DIRECTION_C), "6", "indice", null, headerProps));
		places.put(7, lang.newCircle(new Coordinates(640, 330), 25, "node7", null, SeatsPropertiesDefault));
			indices.add(lang.newText(new Offset(-5, -10, "node7", AnimalScript.DIRECTION_C), "7", "indice", null, headerProps));
		
		// 3 Links
		places.put(8, lang.newCircle(new Coordinates(560, 260), 25, "node8", null, SeatsPropertiesDefault));
			indices.add(lang.newText(new Offset(-5, -10, "node8", AnimalScript.DIRECTION_C), "8", "indice", null, headerProps));
		places.put(9, lang.newCircle(new Coordinates(560, 200), 25, "node9", null, SeatsPropertiesDefault));
			indices.add(lang.newText(new Offset(-5, -10, "node9", AnimalScript.DIRECTION_C), "9", "indice", null, headerProps));
		places.put(10, lang.newCircle(new Coordinates(560, 140), 25, "node10", null, SeatsPropertiesDefault));
			indices.add(lang.newText(new Offset(-5, -10, "node10", AnimalScript.DIRECTION_C), "10", "indice", null, headerProps));
			*/
		
		radius = names.length / 3;
		Coordinates middlepoint = new Coordinates(600, 150 + radius * 10);
		List<Coordinates> coords = this.CircleCoords(middlepoint, radius * 40, names.length);
		
		for(int i = 0; i < coords.size(); i++) {
			places.put((i+1), lang.newCircle(coords.get(i), 20, "node"+(i+1), null, SeatsPropertiesDefault));
			indices.add(lang.newText(new Offset(-5, -10, "node"+(i+1), AnimalScript.DIRECTION_C), ""+(i+1), "indice", null, headerProps));
		}
		
		CircleProperties tableProps = new CircleProperties();
		tableProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		tableProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		tableProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		lang.newCircle((Node)middlepoint, radius * 30, "table", null, tableProps);
			
		// Constraints
		SourceCode constraints = lang.newSourceCode(new Coordinates(860 + middlepointOffsetX + radius*10, 50 + middlepointOffsetY), "sourceCode",
					null, CodeProperties);

		String[] temp = cons.split("\n");
		constraints.addCodeLine("Bedingungen", null, 0, null);
		for (int i = 0; i < temp.length; i++) 
		{
			constraints.addCodeLine(temp[i],null,0,null);
		}
		
		RectProperties constraintProps = new RectProperties();			
			constraintProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			constraintProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);		
			constraintProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);			
//		Rect rectConstraints = 
			lang.newRect(new Offset(-5, -5, constraints, AnimalScript.DIRECTION_NW), 
				new Offset(5, 5, constraints, AnimalScript.DIRECTION_SE), "rectConstraints", null, constraintProps);
	}

	/**
	 * Generates the constraints from the given string
	 * also adds transitive items
	 * @param s2 String to check
	 * @throws Exception 
	 * @returns Constraints-String
	 */
	public String generateConstraints(String s2) throws Exception {
	  String s = s2;
		if(!this.validateString(s))
			throw new Exception("wrong input string");
		String sub;
		String name;
		Constraint constraint;
		Constraint tmpConstraint;
		int constraintCount = 0;
		String constString = "";
		
		s = s.replaceAll("\\s+", "");
		s = s.toLowerCase();
		
		if(!s.endsWith("."))
			s = s.concat(".");
		
		while(s.contains("."))
		{
			constraintCount++;
			sub = s.substring(0, s.indexOf("."));
			s = s.substring(s.indexOf(".") + 1);
			
			sub = sub + ",";
			
			name = sub.substring(0, sub.indexOf(":"));
			sub = sub.substring(sub.indexOf(":") + 1);
			
			if(constraints.containsKey(name))
			{
				constraint = constraints.get(name);
				if(constraint.getID() != 42 && constraint.getID() < constraintCount)
					constraint.changeID(constraintCount);
			}
			else
				constraint = new Constraint(name, constraintCount);
			
			if(constString.length() == 0)
				constString = "(1) " + name;
			else
				constString += "\n(" + constraintCount + ") " + name;
			
			while(sub.contains(",")) 
			{
				name = sub.substring(0, sub.indexOf(","));
				sub = sub.substring(sub.indexOf(",") + 1);
				
				if(name.startsWith("!"))
					constraint.addDisallowed(name.substring(1));
				else
					constraint.addAllowed(name);
			}
			
			constraints.put(constraint.getName(), constraint);
			
			// Create Constraints-String
			if(constraint.getAllowed().size() != 0)
			{
				constString += " sitzt neben ";
				for(String sn : constraint.getAllowed())
					constString += sn + ", ";
				if(constraint.getDisallowed().size() != 0)
					constString += "aber nicht neben ";
			}
			else
			{
				if(constraint.getDisallowed().size() != 0)
					constString += " sitzt nicht neben ";

			}
			for(String sn : constraint.getDisallowed())
				constString += sn + ", ";
			

			constString = constString.substring(0, constString.lastIndexOf(","));
			
			// Add transitive allowed-constraints
			for(String allowed : constraint.getAllowed()) {
				if(constraints.containsKey(allowed)) {
					tmpConstraint = constraints.get(allowed);
					tmpConstraint.addAllowed(constraint.getName());
				}
				else {
					tmpConstraint = new Constraint(allowed, constraintCount);
					tmpConstraint.addAllowed(constraint.getName());
				}
				constraints.put(allowed, tmpConstraint);					
			}
			
			// Add transitive disallowed-constraints
			for(String disallowed : constraint.getDisallowed()) {
				if(constraints.containsKey(disallowed)) {
					tmpConstraint = constraints.get(disallowed);
					tmpConstraint.addDisallowed(constraint.getName());
				}
				else {
					tmpConstraint = new Constraint(disallowed, constraintCount);
					tmpConstraint.addDisallowed(constraint.getName());
				}
				constraints.put(disallowed, tmpConstraint);					
			}
			
			// remove , at the end
			//constString = constString.substring(0, constString.lastIndexOf(","));
		}
		
		return constString;
	}
	/*
  

	public TreeMap<Integer, String> Backtracking(TreeMap<Integer, String> assigns, List<Integer> variables, List<String> domainBT) {
		if(variables.size() == 0 || domainBT.size() == 0)
		{
			// Create the "result: {(Position, Name)}"-String
			int tmp = 0;
			String result = "";
			
			result += "result: {";			
			for(Entry<Integer, String> entry : assigns.entrySet())
			{
				result += "(" + entry.getKey() + ", " + entry.getValue() + ")";
				tmp++;
				
				if(entry.equals(assigns.lastEntry()))
					result += "}";
				else
					result +=", ";
				
				if(tmp == 2)
				{
					outputCode.add(result);
					tmp = 0;
					result = "";
				}
			}
			
			return assigns;
		}
		
		int current = variables.get(0);
		outputCode.add("Select " + current);
		
		int check = 42;
		TreeMap<Integer, String> result;		
		ArrayList<String> alternatives = new ArrayList<String>();
		
		// Check for first assigns (wishes)
		for(int i = 0; i < domainBT.size(); i++)
		{			
			// Constraints-check
			if(current == 1)	// Initial assignment (seat 1)
				check = checkConstraints(null, domainBT.get(i), null);
			else if(variables.size() == 1)	// last assignment (seat 10 e.g.)
				check = checkConstraints(assigns.get(assigns.lastKey()), domainBT.get(i), assigns.get(assigns.firstKey()));
			else	// normal assignment				
				check = checkConstraints(assigns.get(assigns.lastKey()), domainBT.get(i), null);
				
			
			switch(check) 
			{			
				case 0: 
					outputCode.add(current + " = " + domainBT.get(i) + " not consistent (" + constraints.get(domainBT.get(i)).getID() +")");
					break;
				
				case 1:
					// Adding assignment
					assigns.put(current, domainBT.get(i));
					
					// previous Strings are not consistent
					for(String s : alternatives)
						outputCode.add(current + " = " + s + " not consistent (" + constraints.get(domainBT.get(i)).getID() +")");
						
					outputCode.add(current + " = " + domainBT.get(i) + " consistent");
					variables.remove(0);
					domainBT.remove(domainBT.get(i));				
				
					// Recursion
					result = Backtracking(assigns, variables, domainBT);
				
					// Failure-Fall
					if(result != null)
						return result;
				
					// Backtrack-Fall
					variables.add(0, assigns.lastKey());
					domainBT.add(i, assigns.get(assigns.lastKey()));
					assigns.remove(current);
					outputCode.add("Backtrack");
					outputCode.add("Select " + current);
					break;
				
				case 2:
					alternatives.add(domainBT.get(i));
					break;
			}
		}
		// Check alternatives
		for(String s : alternatives)
		{
			// Adding assignment
			assigns.put(current, s);
			outputCode.add(current + " = " + s + " consistent");
			variables.remove(0);
			domainBT.remove(s);			
				
			// Recursion
			result = Backtracking(assigns, variables, domainBT);
				
			// Failure-Fall
			if(result != null)
					return result;
				
			// Backtrack-Fall
			variables.add(0, assigns.lastKey());
			domainBT.add(0, assigns.get(assigns.lastKey()));
			assigns.remove(current);
			outputCode.add("Backtrack");
		}
		return null;
	}
	

	 */
	/**
	 * Backtracking-Method
	 * @param assigns that were made from the variables & constraints, initial:_empty
	 * @param variables here: seats, numbered from 1 to names.length
	 * @param domainBT contains the names
	 * @returns the assigns made from the variables & constraints
	 */
	public TreeMap<Integer, String> Backtracking(TreeMap<Integer, String> assigns, List<Integer> variables, List<String> domainBT)
	{
		
		//if (a is complete)
		if(variables.size() == 0 || domainBT.size() == 0)
		{
			// Create the "result: {(Position, Name)}"-String
			int tmp = 0;
			String result = "";
			
			result += "result: {";			
			for(Entry<Integer, String> entry : assigns.entrySet())
			{
				result += "(" + entry.getKey() + ", " + entry.getValue() + ")";
				tmp++;
				
				if(entry.equals(assigns.lastEntry()))
					result += "}";
				else
					result +=", ";
				
				if(tmp == 2)
				{
					outputCode.add(result);
					tmp = 0;
					result = "";
				}
			}
			
			return assigns;
		}
		
		//remove a variable x from U
		int current = variables.get(0);
		outputCode.add("Select " + current);
		
		TreeMap<Integer, String> result;		
		
		int check;
		
		for (String x : domainBT) 
		{
			if(!assigns.containsValue(x))
			{
				if(current == 1)	// Initial assignment (seat 1)
					check = checkConstraints(null, x, null);
				else if(variables.size() == 1)	// last assignment (seat 10 e.g.)
					check = checkConstraints(assigns.get(assigns.lastKey()), x, assigns.get(assigns.firstKey()));
				else	// normal assignment				
					check = checkConstraints(assigns.get(assigns.lastKey()), x, null);
				
				if(check!=0)
				{
					outputCode.add(current + " = " + x + " consistent");
					assigns.put(current, x);
					
					variables.remove(0);			
				
					// Recursion
					result = Backtracking(assigns, variables, domainBT);
				
					// WIN-Fall
					if(result != null)
						return result;
					
					// Backtrack-Fall
					variables.add(0, assigns.lastKey());
					assigns.remove(current);
					outputCode.add("Backtrack");
					outputCode.add("Select " + current);
				}
				else
				{
					outputCode.add(current + " = " + x + " not consistent");
				}
			}
		}
		return null;
	}
	
	/**
	 * Backtracking-Method with ForwardCheck
	 * @param assigns that were made from the variables & constraints, initial:_empty
	 * @param variables here: seats, numbered from 1 to names.length
	 * @param domainsFC contains the names
	 * @returns the assigns made from the variables & constraints
	 */
	public TreeMap<Integer, String> Forward(TreeMap<Integer, String> assigns, List<Integer> variables, 
	    HashMap<Integer,List<String>> domainsFC)
	{
		//if(A is complete)		
		
		if(variables.size() == 0)
		{
			lang.nextStep();
			// Create the "result: {(Position, Name)}"-String

			CodeFC.addCodeLine("", null, 0, null);
			CodeFC.addCodeLine("", null, 0, null);
			
			int tmp = 0;
			int tmp2 = 0;
			String result = "";
			
			result += "result: {";			
			for(Entry<Integer, String> entry : assigns.entrySet())
			{
				result += "(" + entry.getKey() + ", " + entry.getValue() + ")";
				tmp++;
				
				if(entry.equals(assigns.lastEntry()))
				{
					result += "}";
					CodeFC.addCodeLine(result, null, tmp2, null);
					break;
				}
				else
					result +=", ";
				
				if(tmp == 2)
				{
					CodeFC.addCodeLine(result, null, tmp2, null);
					tmp = 0;
					tmp2= 5;
					result = "";
				}
			}
			//return A
			return assigns;
		}
		
		//select n
		
		//remove a variable X from U
		int current = variables.remove(0); 
		
			lang.nextStep();
			CodeFC.addCodeLine("Select " + current, null, current-1, null);
	
		
			tmpCircleFC = places.get(current);
			tmpCircleFC = lang.newCircle(tmpCircleFC.getCenter(), tmpCircleFC.getRadius(), tmpCircleFC.getName(), new TicksTiming(50), SeatsPropertiesSelected);
			places.put(current, tmpCircleFC);
		
		
			arrayPlaetze.unhighlightCell(currPosFC, new TicksTiming(100), null);
			arrayPlaetze.unhighlightElem(currPosFC, new TicksTiming(100), null);
			
			arrMarker.move(current, new TicksTiming(100), null);
		
		TreeMap<Integer, String> result;
		
		List<String> currentDomain = domainsFC.get(current);
			
//		boolean empty = false;
		HashMap<Integer,List<String>> newDomains = new HashMap<Integer,List<String>>();
		
		
		//for all (values x in D(X))
		for (String x : currentDomain)
		{
			
			lang.nextStep();
			stepsF++;
			
			stepFC.moveBy("translate #2", 2, 0, new TicksTiming(100), null);
			currStepFC.setText(stepsF.toString(), new TicksTiming(100), null);
			
			CodeFC.addCodeLine(current + " = " + x + " consistent", null, current-1, null);
			
			tmpCircleFC = places.get(current);
			tmpCircleFC = lang.newCircle(tmpCircleFC.getCenter(), tmpCircleFC.getRadius(), tmpCircleFC.getName(), new TicksTiming(50), SeatsPropertiesAssigned);
			
			arrayPersonen.put(current, x, new TicksTiming(100), null);
			
			places.put(current, tmpCircleFC);
			
			tmpIndiceFC = indices.get(current - 1);
			tmpIndiceFC.setText(x.substring(0, 1).toUpperCase(), null, null);
			
			//add 'X <= x' to A
			assigns.put(current, x);

			//D' = D save the current Domains
			newDomains = new HashMap<Integer,List<String>>(this.CloneMap(domainsFC));
			
			//remove inconsistent Values from D'
			
			for (Integer y : variables)
			{
				//System.out.println("  Cleaning Domain " + key);
				
					//delete used
					newDomains.get(y).remove(x);
					//System.out.println("    Del used " + usedName);
					
					//delete not allowed from neighbours
					if(this.Neigbours(y, current, this.names.length))
					{

						//System.out.println("    Del not allowed from neighbours of " + usedName);
						
						for (String string : this.getDisallowed(x))
						{
							//System.out.println("      Del " + string);
							newDomains.get(y).remove(string);
						}
						
					}
					//delete allowed from others
					/*else
					{

						//System.out.println("    Del allowed from others of " + usedName);
						for (String string : this.getAllowed(x))
						{
							//System.out.println("      Del " + string);
							newDomains.get(y).remove(string);
						}
					}*/
					//System.out.println("  ### Rest of Domain: " + y + ": " + newDomains.get(y));
					//System.out.println("  ### Rest of OldDom: " + y + ": " + domainsFC.get(y));
					
					arrayPersonen.put(y, getInitialsString(newDomains.get(y)), new TicksTiming(100), null);
			}
			
			//if(for all Y in U D'(Y) is not empty)
			if(isNotEmpty(newDomains,variables))
			{
				result = Forward(assigns, variables, newDomains);
				//arrMarker.move(current, new TicksTiming(100), null);	
				
				// Win-Fall
				if(result != null)
					return result;
			}
			else
			{
				CodeFC.addCodeLine("found empty Value at ", null, current-1, null);
				inconsistentsF++;
			}
			assigns.remove(current);
		}
		
		
		lang.nextStep();

		
		arrMarker.move(current-1, new TicksTiming(100), null);
		CodeFC.addCodeLine("Backtrack", null, current-1, null);
		
		tmpCircleFC = places.get(current);
		tmpCircleFC = lang.newCircle(tmpCircleFC.getCenter(), tmpCircleFC.getRadius(), tmpCircleFC.getName(), new TicksTiming(50), SeatsPropertiesDefault);
		
		places.put(current, tmpCircleFC);
		
		tmpIndiceFC.setText(""+current, null, null);
		
		variables.add(0, current);
		
		backtracksF++;
		return null;
	}
	
	
	private boolean isNotEmpty(HashMap<Integer,List<String>> D, List<Integer> U )
	{
		for (Integer Y : U)
		{
			if(D.get(Y).isEmpty())
				return false;
		}
		return true;
	}
	
	/*
	public TreeMap<Integer, String> Forward(TreeMap<Integer, String> assigns, List<Integer> variables, HashMap<Integer,List<String>> domainsFC)
	{
		
		if(variables.size() == 0)
		{
			lang.nextStep();
			// Create the "result: {(Position, Name)}"-String

			CodeFC.addCodeLine("", null, 0, null);
			CodeFC.addCodeLine("", null, 0, null);
			
			int tmp = 0;
			int tmp2 = 0;
			String result = "";
			
			result += "result: {";			
			for(Entry<Integer, String> entry : assigns.entrySet())
			{
				result += "(" + entry.getKey() + ", " + entry.getValue() + ")";
				tmp++;
				
				if(entry.equals(assigns.lastEntry()))
				{
					result += "}";
					CodeFC.addCodeLine(result, null, tmp2, null);
					break;
				}
				else
					result +=", ";
				
				if(tmp == 2)
				{
					CodeFC.addCodeLine(result, null, tmp2, null);
					tmp = 0;
					tmp2= 5;
					result = "";
				}
			}
			
			return assigns;
		}
		//select n
		int current = variables.get(0); 
			lang.nextStep();
			CodeFC.addCodeLine("Select " + current, null, current-1, null);
	
		
			tmpCircleFC = places.get(current);
			tmpCircleFC = lang.newCircle(tmpCircleFC.getCenter(), tmpCircleFC.getRadius(), tmpCircleFC.getName(), new TicksTiming(50), SeatsPropertiesSelected);
			places.put(current, tmpCircleFC);
		
		
			arrayPlaetze.unhighlightCell(currPosFC, new TicksTiming(100), null);
			arrayPlaetze.unhighlightElem(currPosFC, new TicksTiming(100), null);
			
			arrMarker.move(current, new TicksTiming(100), null);
		
		

		
		TreeMap<Integer, String> result;
		
		
		List<String> currentDomain = domainsFC.get(current);
			
		boolean empty = false;
		HashMap<Integer,List<String>> newDomains = new HashMap<Integer,List<String>>();
		String usedName;
		
		for(int i = 0; i < currentDomain.size(); i++)
		{	
			lang.nextStep();
			stepsF++;
			
			stepFC.moveBy("translate #2", 2, 0, new TicksTiming(100), null);
			currStepFC.setText(stepsF.toString(), new TicksTiming(100), null);
			
			
			
			CodeFC.addCodeLine(current + " = " + currentDomain.get(i) + " consistent", null, current-1, null);
			
			usedName = currentDomain.get(i);
			
			
			tmpCircleFC = places.get(current);
			tmpCircleFC = lang.newCircle(tmpCircleFC.getCenter(), tmpCircleFC.getRadius(), tmpCircleFC.getName(), new TicksTiming(50), SeatsPropertiesAssigned);
			
			places.put(current, tmpCircleFC);
			
			tmpIndiceFC = indices.get(current - 1);
			tmpIndiceFC.setText(currentDomain.get(i).substring(0, 1).toUpperCase(), null, null);	
			
						
			
			empty = false;
			newDomains = new HashMap<Integer,List<String>>(this.CloneMap(domainsFC));
			
			//cleaning domains
			
			System.out.println("Seat: " + current);
			
			arrayPersonen.put(current, usedName, new TicksTiming(100), null);
			newDomains.remove(current);
			
			for (Integer key : newDomains.keySet())
			{

				//System.out.println("  Cleaning Domain " + key);
				
					//delete used
					newDomains.get(key).remove(usedName);
					//System.out.println("    Del used " + usedName);
					
					//delete not allowed from neighbours
					if(this.Neigbours(key, current, this.names.length))
					{

						//System.out.println("    Del not allowed from neighbours of " + usedName);
						
						for (String string : this.getDisallowed(usedName))
						{
							//System.out.println("      Del " + string);
							newDomains.get(key).remove(string);
						}
						
					}
					//delete allowed from others
					else
					{

						//System.out.println("    Del allowed from others of " + usedName);
						for (String string : this.getAllowed(usedName))
						{
							//System.out.println("      Del " + string);
							newDomains.get(key).remove(string);
						}
					}
					System.out.println("  ### Rest of Domain: " + key + ": " + newDomains.get(key));
					System.out.println("  ### Rest of OldDom: " + key + ": " + domainsFC.get(key));
					
					arrayPersonen.put(key, getInitialsString(newDomains.get(key)), new TicksTiming(100), null);
					
					if(newDomains.get(key).isEmpty())
					{
						// empty Value found
						CodeFC.addCodeLine("empty Value at " + key, null, current-1, null);
						inconsistentsF++;
						empty = true;
						lang.nextStep();
					}
				
				
				
				
			}
			
		
			if (!empty)
			{
				assigns.put(current, currentDomain.get(i));
				
				List<Integer> newVars = new ArrayList<Integer>(variables);
				newVars.remove(0);
				
				// Recursion
				result = Forward(assigns, newVars, newDomains);
				//arrMarker.move(current, new TicksTiming(100), null);	
				
				// Win-Fall
				if(result != null)
					return result;
				
				
			}
			for (Integer  key : domainsFC.keySet())
			{
				arrayPersonen.put(key, getInitialsString(domainsFC.get(key)), new TicksTiming(100), null);
			}
		
			
			
		}
		
		lang.nextStep();

		
		arrMarker.move(current-1, new TicksTiming(100), null);
		CodeFC.addCodeLine("Backtrack", null, current-1, null);
		
		tmpCircleFC = places.get(current);
		tmpCircleFC = lang.newCircle(tmpCircleFC.getCenter(), tmpCircleFC.getRadius(), tmpCircleFC.getName(), new TicksTiming(50), SeatsPropertiesDefault);
		
		places.put(current, tmpCircleFC);
		
		tmpIndiceFC.setText(""+current, null, null);
		
		
		
		backtracksF++;
		return null;
	}*/
	/**
	 * Checks if a given person can sit next to 2 given persons left and right
	 * @param left person
	 * @param person to check
	 * @param right person
	 * @returns 0 if the person can't sit next to left or right
	 * @returns 1 if the person needs to sit next to either left or right
	 * @returns 2 if the person can sit next to left / right
	 */
	private int checkConstraints(String left, String person, String right) {
		if(checkLeft(left, person) == 0 || checkRight(person, right) == 0)
			return 0;
		if(checkLeft(left, person) == 1 && checkRight(person, right) == 1)
			return 1;
		
		return 2;
	}
	
	
	private HashMap<Integer,List<String>> CloneMap(HashMap<Integer,List<String>> map)
	{
		HashMap<Integer,List<String>> result = new HashMap<Integer,List<String>>();
		List<String> tmp;
		for (Integer key : map.keySet())
		{
			tmp= new ArrayList<String>();
			for (String s : map.get(key)) 
			{
				tmp.add(s);
			}
			result.put(key, tmp);
		}
		
		return result;
	}
	
	private boolean Neigbours(int keyA, int keyB, int Seats)
	{
		if(Math.abs(keyA-keyB)==1 || Math.abs((keyA-Seats)-keyB)==1 || Math.abs(keyA-(keyB-Seats))==1)
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * Getter method
	 * @returns disallowed
	 */
	private ArrayList<String> getDisallowed(String person) {
		Constraint cons;
		if(constraints.containsKey(person))
			cons = constraints.get(person);	
		else 
		{
			cons = new Constraint(person);
			constraints.put(person, cons);
		}
		return cons.getDisallowed();
	}
	
//	/**
//	 * Getter method
//	 * @param person the Person
//	 * @return disallowed
//	 */
//	private ArrayList<String> getAllowed(String person) {
//		Constraint cons;
//		if(constraints.containsKey(person))
//			cons = constraints.get(person);	
//		else 
//		{
//			cons = new Constraint(person);
//			constraints.put(person, cons);
//		}
//		return cons.getAllowed();
//	}
	
	/**
	 * Checks if a given person can sit next to the left person
	 * @see checkConstraints
	 */
	private int checkLeft(String left, String person) {		
		if(left == null)
			return 1;

		Constraint cons;
		if(constraints.containsKey(person))
			cons = constraints.get(person);	
		else 
		{
			cons = new Constraint(person);
			constraints.put(person, cons);
		}
		
		return cons.consistent(left);
	}
	
	/**
	 * Checks if a given person can sit next to the right person
	 * @see checkConstraints
	 */
	private int checkRight(String person, String right) {
		if(right == null)
			return 1;
		
		Constraint cons;
		if(constraints.containsKey(person))
			cons = constraints.get(person);	
		else 
		{
			cons = new Constraint(person);
			constraints.put(person, cons);
		}
		
		return cons.consistent(right);
	}
	
	/**
	 * Creates the animation code for given mode
	 * @param forward true if forward mode, else false
	 */
	private void createAnimation(boolean forward, StringArray[] arrays) {
		  /////////////////////////
		 // Initialize & Create //
		/////////////////////////
		
		SourceCode code = lang.newSourceCode(new Coordinates(20, 30), "sourceCode",
				null, CodeProperties);
		ArrayMarker domainMarker = lang.newArrayMarker(arrays[0], 0, "domainMarker", null);
		
		// Step-Counter & Rectangle
		TextProperties tmpProps = new TextProperties();
			tmpProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
			tmpProps.set(AnimationPropertiesKeys.FONT_PROPERTY, 
					new Font("SansSerif", Font.BOLD, 16));
			tmpProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		
		Text cStep = lang.newText(new Coordinates(860 + middlepointOffsetX + radius*10, 270 + middlepointOffsetY), "Schritte: ",
				"#s", null, tmpProps);
		
		tmpProps = new TextProperties();
			tmpProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
			tmpProps.set(AnimationPropertiesKeys.FONT_PROPERTY, 
					new Font("SansSerif", Font.PLAIN, 12));
			tmpProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		
		Rect stepRect = lang.newRect(new Offset(10, 0, cStep, AnimalScript.DIRECTION_NE), 
				new Offset(11, 0, cStep, AnimalScript.DIRECTION_SE), "stepRect", null, stepProps);
		Text currStep = lang.newText(new Offset(-5, 5, stepRect,
				AnimalScript.DIRECTION_C), "", "#sn", null, tmpProps);
			
		
		Circle tmpCircle;
		Text tmpIndice;
		int currPos = 0;
		Integer step = 1;
		Integer steps = 0;
		Integer backtracks = 0;
		Integer inconsistents = 0;
		boolean[] coverage = new boolean[names.length];
		
		  //////////////////////
		 // Create Animation //
		//////////////////////
		
		// Bewegen des Domainmarkers
		for(String val : outputCode)
		{
			if(val.contains("Select"))
			{
				tmpCircle = places.get(step);
				tmpCircle = lang.newCircle(tmpCircle.getCenter(), tmpCircle.getRadius(), tmpCircle.getName(), new TicksTiming(50), SeatsPropertiesSelected);
				places.put(step, tmpCircle);
				
				arrays[0].unhighlightCell(currPos, new TicksTiming(100), null);
				arrays[0].unhighlightElem(currPos, new TicksTiming(100), null);
			}
			else if(val.contains("not consistent"))
			{
				tmpCircle = places.get(step);
				tmpCircle = lang.newCircle(tmpCircle.getCenter(), tmpCircle.getRadius(), tmpCircle.getName(), new TicksTiming(50), SeatsPropertiesFailure);
				places.put(step, tmpCircle);
				
				coverage[currPos] = false;
				
				currPos++;				
				steps++;
				inconsistents++;
				stepRect.moveBy("translate #2", 2, 0, new TicksTiming(100), null);
				currStep.setText(steps.toString(), new TicksTiming(100), null);
				domainMarker.move(currPos, new TicksTiming(150), null);
			}
			else if(val.contains("consistent"))
			{
				tmpCircle = places.get(step);
				tmpCircle = lang.newCircle(tmpCircle.getCenter(), tmpCircle.getRadius(), tmpCircle.getName(), new TicksTiming(50), SeatsPropertiesAssigned);
				places.put(step, tmpCircle);
				
				tmpIndice = indices.get(step - 1);
				if(val.substring(4, 5).trim().length() != 0)
					tmpIndice.setText(val.substring(4, 5).toUpperCase(), null, null);	
				else
					tmpIndice.setText(val.substring(5, 6).toUpperCase(), null, null);
				
				coverage[currPos] = true;				
				
				arrays[0].highlightCell(currPos, new TicksTiming(100), null);
				arrays[0].highlightElem(currPos, new TicksTiming(100), null);
				
				
				currPos = this.getFreePosition(coverage);
				
				step++;				
				steps++;
				stepRect.moveBy("translate #2", 2, 0, new TicksTiming(100), null);
				currStep.setText(steps.toString(), new TicksTiming(100), null);
				if(currPos > 0)
					domainMarker.move(currPos, new TicksTiming(150), null);
			}
			else if(val.contains("Backtrack"))
			{
				tmpCircle = places.get(step);
				tmpCircle = lang.newCircle(tmpCircle.getCenter(), tmpCircle.getRadius(), tmpCircle.getName(), new TicksTiming(50), SeatsPropertiesDefault);
				places.put(step, tmpCircle);
				
				tmpIndice = indices.get(step - 1);
				tmpIndice.setText(step.toString(), null, null);
				
				arrays[0].unhighlightCell(currPos, new TicksTiming(100), null);
				arrays[0].unhighlightElem(currPos, new TicksTiming(100), null);
				
				currPos--;
				step--;				
				steps++;
				backtracks++;
				stepRect.moveBy("translate #2", 2, 0, new TicksTiming(100), null);
				currStep.setText(steps.toString(), new TicksTiming(100), null);
				domainMarker.move(currPos, new TicksTiming(150), null);
			}
			// nochmal schauen ob das sauberer geht, gerade keinen Nerv
			else if(val.contains("result"))
			{
				code.addCodeLine("", "", 0, null);
				
				step = 1;
			}
			else if(val.contains("(3,"))
				step = 6;
			
			
			code.addCodeLine(val, "", (step - 1), new TicksTiming(50));
			lang.nextStep();
		}
		code.addCodeLine("Steps: " + steps + ", inconsistent Assigns:" + inconsistents + ", Backtracks: " + backtracks, "", 0, new TicksTiming(150));

		this.summaryValues[0][0] = backtracks;
		this.summaryValues[0][1] = inconsistents;
		this.summaryValues[0][2] = steps;
	}
	
	/**
	 * Checks a given boolean Array for the first free position (uncovered place = false-value)
	 * @param array to check
	 * @returns position, or -42 if arrays is fully covered
	 */
	private int getFreePosition(boolean array[]) {
		for(int i = 0; i < array.length; i++)
			if(array[i] == false)
				return i;
			
		return -42;
	}
	
	/**
	 * Gets the Initials off the name-list
	 * @param names to get the initials from
	 * @returns string-array filled with the initials
	 */
	private String[] getInitials(String[] names) {
		String[] initials = new String[names.length];
		
		for(int i = 0; i < names.length; i++)
			initials[i] = names[i].substring(0, 1);
		
		return initials;
	}
	
//	private int maxKey(Set<Integer> keys)
//	{
//		int max=0;
//		for (Integer i : keys)
//		{
//			if (i > max) 
//			{
//				max = i;
//			}
//		}
//		return max;
//	}
	
	private String getInitialsString(List<String> newString) {
		String initials = new String();
		
		for(int i = 0; i < newString.size(); i++)
			initials += newString.get(i).substring(0, 1).toUpperCase() + " ";
		
		return initials;
	}

	private boolean validateString(String s) {
		final char[] chars = (s.replaceAll("\\s+", "")).toCharArray();
		
		for(char c : chars)
		{
			if ((c >= 'a') && (c <= 'z')) continue; // lowercase
			if ((c >= 'A') && (c <= 'Z')) continue; // uppercase
			if ((c == ':') || (c == '.') 
			|| (c == '!') || (c == ',')) continue; // syntax
			
			System.out.println("Found invalid character: " + c);
			return false;
		}
		
		
		return true;
	}
	
    /**
     * Calulates Coordinates on a Circle
     * @param middle of the Circle
     * @param radius of the Circle
     * @param anz of Notes
     * @return a list of circle coordinates
     */
    public List<Coordinates> CircleCoords(Coordinates middle, int radius, int anz)
    {
    	List<Coordinates> result = new ArrayList<Coordinates>(); 
    	
    	double deg = (Math.PI*2) / anz;
    	int x=0,y=0;
    	
    	for (int i = 0; i < anz; i++)
    	{
    		x = (int)Math.round((Math.cos(i*deg)*radius)+middle.getX());
    		y = (int)Math.round((Math.sin(i*deg)*radius)+middle.getY());
    		result.add(new Coordinates(x,y));
		}
    	
    	return result;
    }
}