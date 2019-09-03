package generators.misc.arithconvert;

import algoanim.primitives.Graph;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.GraphProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import generators.misc.arithconvert.sugar.*;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jannis Weil, Hendrik Wuerz
 */
public class ArithConverter {

    /**
     * The concrete language object used for creating output
     */
    private Language lang;

    /**
     * Default constructor
     *
     * @param l the conrete language object used for creating output
     */
    public ArithConverter(Language l) {
        // Store the language object
        lang = l;
        // This initializes the step mode. Each pair of subsequent steps has to
        // be divided by a call of lang.nextStep();
        lang.setStepMode(true);
    }

    // TODO: Write more
    public static final String DESCRIPTION = 
    		"In dieser Animation wird die Konvertierung eines arithmetischen Ausdrucks in Infixnotation\n"
    		+ "zu einer LR-Postorder Darstellung visualisiert. Bei der LR-Postorder Darstellung werden\n"
    		+ "die Operationen von links nach rechts bearbeitet und so der gesamte arithmetische Ausdruck\n"
    		+ "ausgewertet. Die LR-Postorder Darstellung kann dabei komplett auf Klammerung verzichten, da\n"
    		+ "die Reihenfolge der Operanden und Operatoren selbst bereits die Ausführung eindeutig spezifiziert.\n"
    		+ " \n"
    		+ "Um Zwischenergebnisse zu speichern, wird dazu allerdings auch ein Stack benötigt. In diesem\n"
    		+ "können Ergebnisse gespeichert (push " + Expression.push + ") und jeweils der zuletzt gespeicherte Eintrag\n"
    		+ "entnommen (pop " + Expression.pop + ") werden.\n"
    		;
    
    public static final String LR_DESCRIPTION = 
    		"Bei der Konvertierung eines Ausdrucks von Infixnotation in LR-Postorder Darstellung geht man wie folgt vor:\n \n"
    		+ "Zunächst wird der gesamte Ausdruck betrachtet. Ist er ein Literal, so wird das Literal in den Buffer\n"
    		+ "geschrieben und der Algorithmus ist fertig. Andernfalls liegt eine binäre Operation vor (z.B. 1 + 2).\n"
    		+ "Diese hat eine linke und eine rechte Seite (1 bzw. 2). In diesem Fall wird zunächst der linke Ausdruck\n"
    		+ "der binären Operation betrachtet. Dieser wird konvertiert. Danach wird der rechte Ausdruck betrachtet.\n"
    		+ "Ist er ein Literal, so kann er ebenfalls in den Buffer geschrieben werden.\n"
    		+ "Danach wird der Operator geschrieben.\n"
    		+ " \n		Beispiel: 1 2 + steht für: 1 + 2\n \n"
    		+ "Ist der rechte Ausdruck jedoch kein Literal (z.B. (3 + 4) bei ((1 + 2) - (3 + 4)), so muss der linke Teilausdruck\n"
    		+ "zunächst zwischengespeichert werden. Dazu wird er in den Stack geschrieben (push " + Expression.push + ").\n"
    		+ " \n		Beispiel: 1 2 + " + Expression.push + " steht für: (1 + 2) wird in den Stack geschrieben.\n \n"
    		+ "Dann wird der rechte Teilausdruck konvertiert und entsprechend der Buffer verändert. Ist die Konvertierung des\n"
    		+ "rechten Ausdrucks abgeschlossen, so muss der linke Teilausdruck aus dem Stack entnommen werden (pop "+ Expression.pop + ").\n"
    		+ " \n		Beispiel: 1 2 + " + Expression.push + "3 4 + " + Expression.pop + " bedeutet: (1 + 2) wird im letzten Schritt dem Stack entnommen.\n \n"
    		+ "Nachdem der Wert entnommen wurde, kann die Operation zwischen linkem und rechten Teilausdruck durchgeführt werden.\n"
    		+ " \n		Beispiel: 1 2 + " + Expression.push + "3 4 + " + Expression.pop + "- : Subtraktion (1 + 2) - (3 + 4) wird durchgeführt.\n \n"
    		+ "Zu bemerken ist hierbei, dass die Operation nach einem pop umgekehrt durchgeführt werden muss.\n"
    		+ "Das bedeutet, dass zwar 1 2 * = 1 * 2, aber (res) " + Expression.pop + " * = (pop) * (res), wenn (res) die aktuelle Auswertung\n"
    		+ "und (pop) der Ausdruck ist, der aktuell auf dem Stack liegt und durch pop entfernt wurde. * steht hierbei für\n"
    		+ "eine beliebige Operation.\n \n"
    		+ "Diese Animation visualisiert den Ausdruck als binären Baum und zugleich den Fokus auf dem Ausdruck selbst. Ebenso ist\n"
    		+ "ein Pseudocode für die Vorgehensweise der Konvertierung eingeblendet. Der Stack in der Visualisiserung wächst von oben\n"
    		+ "nach unten. Das bedeutet, dass immer nur der unterste Eintrag entnommen werden kann.\n"
    		;
    
    private static final String DESCRIPTION_SPECIFIC_START = 
    		"In diesem konkreten Beispiel wird die Konvertierung von dem Ausdruck\n"
    		;
    
    private static final String DESCRIPTION_SPECIFIC_END = 
    		"behandelt.\n"
    		;
    
    private static final String SUMMARY = 
    		"Hier werden neben dem Ergebnis auch nochmal Statistiken zu der Konvertierung bereitgestellt.\n"
    		+ " \n";
    
    private static final String SUMMARY_RL = 
    		" \nNeben der LR-Postorder Darstellung existiert auch die RL-Postorder Darstellung.\n"
    		+ "Die Funktionsweise dieser ist nahezu identisch zu der hier präsentierten Konvertierung,\n"
    		+ "allerdings wird hier immer zuerst der rechte Ausdruck betrachtet, anstelle dem Linken.\n"
    		+ "Dies spiegelt sich auch in dem Ergebnis der Konvertierung wieder, weshalb sich dieses z.B.\n"
    		+ "in der Anzahl an Stackoperationen von der LR-Postorder Darstellung unterscheiden kann.\n"
    		+ "Der Vollständigkeit halber sind im Folgenden zusätzlich die Ergebnisse der Konvertierung in\n"
    		+ "einen RL-Postorder Ausdruck dargestellt:\n \n";
    
    public static final String INPUT_DESCRIPTION = 
    		"Bitte achte bei der Eingabe darauf, dass sie das richtige Format hat.\n"
    		+ "Jede Operation muss korrekt geklammert sein, zulässig als Zeichen für\n"
    		+ "Literale sind alle Zeichen bis auf Klammerung und die Operatoren selbst.\n"
    		+ " \n"
    		+ "Beispiele für valide Ausdrücke: \n"
    		+ "1, (1 + 2), (3 * (4 + 2)), ((1 + 2) + (3 + 4))\n";
    
    private static final String GENERIC_ERROR = 
    		INPUT_DESCRIPTION
    		+ " \n"
    		+ "Bitte behebe den Fehler und versuche es erneut, um die Animation der\n"
    		+ "Umwandlung deines arithmetischen Ausdrucks sehen zu können.\n"
    		;
    
    private static final String TITLE_LR = "Konvertierung von Infixnotation zu LR-Postorder Darstellung";

    
    // LR binary expression
    public static final int LR_B_START = 0;
    public static final String LR_B_START_DESCRIPTION = 
    		"Der binäre Ausdruck\n"
    		+ "wird konvertiert.";
    public static final int LR_B_LEFT = 1;
    public static final String LR_B_LEFT_DESCRIPTION = 
    		"Der linke Teilausdruck\n"
    		+ "wird betrachtet.";
    public static final int LR_B_IF = 2;
    public static final String LR_B_IF_DESCRIPTION = 
    		"Der rechte Teilausdruck\n"
    		+ "ist ein Literal.";
    // the right is a literal
    public static final int LR_B_T_RIGHT = 3;
    public static final String LR_B_T_RIGHT_DESCRIPTION = 
    		"Das Literal wird betrachtet.";
    public static final int LR_B_T_APPLY = 4;
    public static final String LR_B_T_APPLY_DESCRIPTION = 
    		"Der Operator wird angewandt.";

    public static final int LR_B_ELSE = 5;
    public static final String LR_B_ELSE_DESCRIPTION = 
    		"Der rechte Teilausdruck\n"
    		+ "ist kein Literal.";
    public static final int LR_B_F_PUSH = 6;
    public static final String LR_B_F_PUSH_DESCRIPTION = 
    		"Der linke Teilausdruck ist bereits\n"
    		+ "konvertiert und wird auf den \n"
    		+ "Stack geschoben.";
    public static final int LR_B_F_RIGHT = 7;
    public static final String LR_B_F_RIGHT_DESCRIPTION = 
    		"Der rechte Teilausdruck\n"
    		+ "wird betrachtet.";
    public static final int LR_B_F_POP = 8;
    public static final String LR_B_F_POP_DESCRIPTION = 
    		"Der linke Teilausdruck wird\n"
    		+ "dem Stack entnommen.";
    public static final int LR_B_F_APPLY = 9;
    public static final String LR_B_F_APPLY_DESCRIPTION = 
    		"Der Operator wird in umgekehrter\n"
    		+ "Reihenfolge angewandt.";
    
    // LR literal
    public static final int LR_L_START = 13;
    public static final String LR_L_START_DESCRIPTION = 
    		"Das Literal wird konvertiert.";
    public static final int LR_L_HANDLE = 14;
    public static final String LR_L_HANDLE_DESCRIPTION = 
    		"Das Literal wird in den\n"
    		+ "Buffer geschrieben.";
    
    public static final int LR_LAST_LINE = 15;
    
    private static SourceCode sourceCodeLR;
    public static final String SOURCE_CODE = 
    	"convertToLR(BinaryExpression e) {\n" // 0
    	+ "	convertToLR(e.left)\n" 
    	+ "	if (e.right instanceof Literal) {\n"
    	+ "		convertToLR(e.right)\n" // 3
    	+ "		apply(e.op)\n"
    	+ "	} else {\n" // 5
    	+ "		push the current term\n"
    	+ "		convertToLR(e.right)\n" // 7
    	+ "		pop (old left term)\n"
    	+ "		applyInverted(e.op)\n"
    	+ "	}\n" // 10
    	+ "}\n"
    	+ " \n"
    	+ "convertToLR(Literal e) {\n" // 13
    	+ "	write e to the buffer\n"
    	+ "}"; // 15

    private static Node codeDescriptionPosition;
    private static SourceCode codeDescription;
    private static Rect codeDescriptionRect;
    private static int codeDescriptionCounter = 0;
    private static SourceCodeProperties codeDescriptionStyle;
    
    
    /**
     * How often questions to specific lines were asked
     */
    static Map<Integer, Integer> questions;
    static int questionId = 0;
    public static boolean enableQuestions = true;
    
    public static void updateCodeDescription(Language lang, String newDescription) {
    	if(codeDescription != null){
    		codeDescription.hide();
    		codeDescriptionCounter++;
    	}
    	String label = "SourceCode_description_" + codeDescriptionCounter;
    	codeDescription = lang.newSourceCode(codeDescriptionPosition, label, null, codeDescriptionStyle);
    	codeDescription.addMultilineCode(newDescription, label + "_line", null);
    	
    	if(codeDescriptionRect != null){
    		codeDescriptionRect.hide();
    	}
    	
    	String rectLabel = "SourceCode_description_Rect_" + codeDescriptionCounter;
    	codeDescriptionRect = lang.newRect(new Offset(-5, -5, codeDescription, "NW"), new Offset(5, 5, codeDescription, "SE"), rectLabel, null);
    }
   
    public static void akMCQuestion(Language lang, String title, String[] text, Integer[] points, String[] feedback){
    	if(!(text.length == points.length && points.length == feedback.length && feedback.length > 0)){
    			throw new IllegalArgumentException(
    					"You must use at least one answer and the length of each array must be the same!");
    	}
    	
    	List<String> textList = Arrays.asList(text);
    	List<Integer> pointsList = Arrays.asList(points);
    	List<String> feedbackList = Arrays.asList(feedback);
    	
    	// shuffle the questions (shuffle all the same)
    	long seed = System.nanoTime();
    	Collections.shuffle(textList, new Random(seed));
    	Collections.shuffle(pointsList, new Random(seed));
    	Collections.shuffle(feedbackList, new Random(seed));
    	
    	
    	MultipleChoiceQuestionModel mod = new MultipleChoiceQuestionModel("question_mc_" + questionId);
    	questionId++;
    	mod.setPrompt(title);
    	// add the answers
    	for(int i = 0; i < textList.size(); i++) {
    		mod.addAnswer(textList.get(i), pointsList.get(i), feedbackList.get(i));
    	}
    	mod.setNumberOfTries(1);
    	lang.addMCQuestion(mod);
    	lang.nextStep();
    }

    public static boolean questionRequest(int lineNo) {
    	if(!questions.containsKey(lineNo)) {
    		questions.put(lineNo, 1);
    		// we always ask a question for the first time something happens
    		return true;
    	}
    	
    	// amount of times this question was already asked
    	Integer tries = questions.get(lineNo);
    	// randval is a double value from 0 to (tries + 1) (exclusive)
    	double randVal = Math.random() * (tries + 1);
    	// ask this question again if randVal <= 1
    	// example: question was already asked 1 time -> tries + 1 = 2
    	// -> randVal \in [0, 2) -> 50% possibility for another question here
    	if(randVal <= 1) {
    		questions.put(lineNo, tries + 1);
    		return true;
    	}
    	// do not ask this question
    	return false;
    }
        
    /**
     * Ask questions regarding the given line of code (when there exists a question for that line)
     * @param lang the language used
     * @param lineNo the line of the code
     */
    public static void askQuestions(Language lang, int lineNo){
    	if(lineNo == LR_B_F_PUSH && questionRequest(LR_B_F_PUSH)) {
    		String[] text = {LR_B_F_PUSH_DESCRIPTION, LR_B_F_RIGHT_DESCRIPTION, LR_B_LEFT_DESCRIPTION};
    		Integer[] points = {1, 0, 0};
    		String[] feedback = {
    				"Richtig!", 
    				"Falsch! Richtig wäre: " + LR_B_F_PUSH_DESCRIPTION, 
    				"Falsch! Richtig wäre: " + LR_B_F_PUSH_DESCRIPTION};
    		akMCQuestion(lang, "Was ist der nächste Schritt?", text, points, feedback);
    	}
    	else if(lineNo == LR_B_F_POP && questionRequest(LR_B_F_POP)) {
    		String[] text = {LR_B_F_POP_DESCRIPTION, LR_B_F_PUSH_DESCRIPTION, LR_B_F_APPLY_DESCRIPTION};
    		Integer[] points = {1, 0, 0};
    		String[] feedback = {
    				"Richtig!", 
    				"Falsch! Richtig wäre: " + LR_B_F_POP_DESCRIPTION, 
    				"Falsch! Richtig wäre: " + LR_B_F_POP_DESCRIPTION};
    		akMCQuestion(lang, "Was ist der nächste Schritt?", text, points, feedback);
    	}
    	else if(lineNo == LR_B_T_APPLY && questionRequest(LR_B_T_APPLY)) {
    		String[] text = {LR_B_T_APPLY_DESCRIPTION, LR_B_F_PUSH_DESCRIPTION, LR_B_F_APPLY_DESCRIPTION};
    		Integer[] points = {1, 0, 0};
    		String[] feedback = {
    				"Richtig!", 
    				"Falsch! Richtig wäre: " + LR_B_T_APPLY_DESCRIPTION, 
    				"Falsch! Richtig wäre: " + LR_B_T_APPLY_DESCRIPTION};
    		akMCQuestion(lang, "Was ist der nächste Schritt?", text, points, feedback);
    	}
    	else if(lineNo == LR_B_F_APPLY && questionRequest(LR_B_F_APPLY)) {
    		String[] text = {LR_B_F_APPLY_DESCRIPTION, LR_B_F_PUSH_DESCRIPTION, LR_B_T_APPLY_DESCRIPTION};
    		Integer[] points = {1, 0, 0};
    		String[] feedback = {
    				"Richtig!", 
    				"Falsch! Richtig wäre: " + LR_B_F_APPLY_DESCRIPTION, 
    				"Falsch! Richtig wäre: " + LR_B_F_APPLY_DESCRIPTION};
    		akMCQuestion(lang, "Was ist der nächste Schritt?", text, points, feedback);
    	}
    }
    
    /**
     * Visualize a step in the code. Can also ask questions about that line.
     * @param lang the language to use to update the code description and ask questions
     * @param lineNo the new line number for the next step
     */
    public static void doCodeStep(Language lang, int lineNo){
    	// ask questions regarding specific lines
    	if(enableQuestions) {
    		askQuestions(lang, lineNo);
    	}
    	
    	// highlight the next line
    	highlightLR(lineNo);
    	// update the description for that line
    	switch(lineNo){
    		case LR_B_START: updateCodeDescription(lang, LR_B_START_DESCRIPTION); break;
    		case LR_B_LEFT: updateCodeDescription(lang, LR_B_LEFT_DESCRIPTION); break;
    		case LR_B_IF: updateCodeDescription(lang, LR_B_IF_DESCRIPTION); break;
    		case LR_B_T_RIGHT: updateCodeDescription(lang, LR_B_T_RIGHT_DESCRIPTION); break;
    		case LR_B_T_APPLY: updateCodeDescription(lang, LR_B_T_APPLY_DESCRIPTION); break;
    		case LR_B_ELSE: updateCodeDescription(lang, LR_B_ELSE_DESCRIPTION); break;
    		case LR_B_F_PUSH: updateCodeDescription(lang, LR_B_F_PUSH_DESCRIPTION); break;
    		case LR_B_F_RIGHT: updateCodeDescription(lang, LR_B_F_RIGHT_DESCRIPTION); break;
    		case LR_B_F_POP: updateCodeDescription(lang, LR_B_F_POP_DESCRIPTION); break;
    		case LR_B_F_APPLY: updateCodeDescription(lang, LR_B_F_APPLY_DESCRIPTION); break;
    		case LR_L_START: updateCodeDescription(lang, LR_L_START_DESCRIPTION); break;
    		case LR_L_HANDLE: updateCodeDescription(lang, LR_L_HANDLE_DESCRIPTION); break;
    		default: System.out.println("Keine Beschreibung für Zeilennummer verfügbar."); break;
    	}
    }
    
    /**
     * Highlight a specific line in the LR code
     * @param lineNo the line number
     */
    public static void highlightLR(int lineNo){
    	sourceCodeLR.highlight(lineNo);
    }
    
    /**
     * Unhighlight a specific line in the LR code
     * @param lineNo the line number
     */
    public static void unHighlightLR(int lineNo){
    	sourceCodeLR.unhighlight(lineNo);
    }
    
    /**
     * Unhighlight all lines in the LR code
     */
    public static void unHighlightLR(){
    	for(int i = 0; i <= LR_LAST_LINE; i++){
        	sourceCodeLR.unhighlight(i);
    	}
    }
    
    /**
     * default duration for swap processes
     */
    public final static Timing defaultDuration = new TicksTiming(30);
    
    /**
     * Finalizes the animation creation, if this script was created by hand
     * @param language the language which should be written
     */
    private static void finalize (Language language){
    	String code = language.toString();
        System.out.println(code);
                
        try {
            Files.write(Paths.get("arithconvert.asu"), code.getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Parse a string and convert it to an expression object
     * @param text the string which should be parsed to an expression
     * @return the parsed expression
     * @throws IllegalArgumentException whether there occured an error during the conversion
     */
    public static Expression parse(String text) throws IllegalArgumentException {
    	text = text.trim(); // remove spaces
    	
    	//System.out.println(text);
    	if(text.length() == 0){
    		throw new IllegalArgumentException("Die Expression darf nicht leer sein!");
    	}
    	
    	// check for a unary pattern (an operand)
    	String unaryRegex = "^[^\\(\\)\\+\\*\\-\\/]+$";
    	Pattern unaryPattern = Pattern.compile(unaryRegex);
    	Matcher unaryMatcher = unaryPattern.matcher(text);
    	
    	if(unaryMatcher.matches()){
    		//System.out.println("literal! " + text);
    		return new Literal(text);
    	}
    	
    	// else (no operand) : check whether we have a binary expression
    	
    	if(text.charAt(0) == '('){
    		int parenthesisCount = 1;
    		int mode = 1;
    		// 1 - search for first operand
    		// 2 - search for second operand
    		String firstOperand = "";
    		Operator op = null;
    		String secondOperand = "";
    		
    		for(int i = 1; i < text.length(); i++){
    			char c = text.charAt(i);
    			
    			if(c == '('){
    				parenthesisCount++;
    			}
    			else if(c == ')'){
    				parenthesisCount--;
    			}
    			
    			if(mode == 1 && parenthesisCount == 1){
    				// we are on the top-level and see an operator -> switch the mode!
    				// when we see a duplicate operator, these will trigger an error in the
    				// next iteration when parsing the second operand
	    			if(c == '+'){
	    				op = Operator.Add;
	    				mode = 2;
	    				continue;
	    			}
	    			else if(c == '-'){
	    				op = Operator.Sub;
	    				mode = 2;
	    				continue;
	    			}
	    			else if(c == '*'){
	    				op = Operator.Mul;
	    				mode = 2;
	    				continue;
	    			}
	    			else if(c == '/'){
	    				op = Operator.Div;
	    				mode = 2;
	    				continue;
	    			}
    			}
    			
    			if(parenthesisCount > 0){
    				if(mode == 1){
            			firstOperand += c;
    				}
    				if(mode == 2){
            			secondOperand += c;
    				}
    			} else {
    				// == 0 only at the end
    				if(i != text.length() - 1){
    	    	    	throw new IllegalArgumentException("Die Klammerung von \"" + text + "\" ist fehlerhaft! Die letzte Klammer wurde vor dem Ende gefunden.");
    				}
    			}
    		}
    		
    		if(parenthesisCount != 0){
    			throw new IllegalArgumentException("Die Klammerung von \"" + text + "\" ist fehlerhaft! Übrige Klammern: " + parenthesisCount + ".");
    		}
    		
    		firstOperand = firstOperand.trim();
    		secondOperand = secondOperand.trim();
    		
    		// left and right operand should be != "" and we should have seen an operand
    		if(firstOperand.equals("")){
    	    	throw new IllegalArgumentException("Der Ausdruck \"" + text + "\" enthält keinen ersten Operanden.");
    		}
    		if(op == null){
    	    	throw new IllegalArgumentException("Der Ausdruck \"" + text + "\" enthält keinen Operator.");
    		}
    		if(secondOperand.equals("")){
    	    	throw new IllegalArgumentException("Der Ausdruck \"" + text + "\" enthält keinen zweiten Operanden.");
    		}
    		
    		// parse the sub-expressions
    		return new BinaryExpression(parse(firstOperand), parse(secondOperand), op);
    	}
    	
    	// we are neither binary nor unary
    	throw new IllegalArgumentException("Der Ausdruck \"" + text + "\" ist keine valide (sub-)expression. Sie muss entweder ein Literal sein, oder mit ( beginnen.");
    }
    
    /**
     * Animate the conversion of the expression inside of the sting parameter.
     * @param lang the language which is used as an output for the conversion.
     * @param expression the expression in string-form.
     */
    public static void animateExpression(Language lang, String expression){
    	lang.setStepMode(true);
    	
    	// new questions hashmap
    	questions = new HashMap<>();
    	questionId = 0;
    	
        Font fontHeadline = new Font(Font.SERIF, Font.BOLD, 18);
        TextProperties propHeadline = new TextProperties();
        propHeadline.set("font", fontHeadline);
        // draw headline
        Text headline = lang.newText(new Coordinates(10, 10), TITLE_LR, "headline", null, propHeadline);
        
    	Expression e;
        try{
        	e = parse(expression);
        }
        catch(IllegalArgumentException err){
        	Text error0 = lang.newText(new Offset(0, 5, headline, "SW"), "Es ist ein Fehler bei dem Parsen von >" + expression + "< aufgetreten:", "parse_error0", null);
        	Text error1 = lang.newText(new Offset(0, 5, error0, "SW"), err.getMessage(), "parse_error1", null);
        	SourceCode errorSourceCode = lang.newSourceCode(new Offset(0, 5, error1, "SW"), "parse_error_generic", null);
        	errorSourceCode.addMultilineCode(GENERIC_ERROR, "generror_", null);
            lang.nextStep("Eingabefehler");
        	return;
        }
    	
    	SourceCode descriptionSourceCode = lang.newSourceCode(new Offset(0, 5, headline, "SW"), "description", null);
        //Text description = language.newText(new Offset(0, 10, headline, "SW"), DESCRIPTION, "description", null);
        String introduction = DESCRIPTION + " \n" + DESCRIPTION_SPECIFIC_START + " \n" + e + " \n \n" +  DESCRIPTION_SPECIFIC_END;
        descriptionSourceCode.addMultilineCode(introduction, "desc_", null);
        lang.nextStep("Einführung");
        descriptionSourceCode.hide();
                
        SourceCode description2SourceCode = lang.newSourceCode(new Offset(0, 5, headline, "SW"), "description2", null);
        description2SourceCode.addMultilineCode(LR_DESCRIPTION, "lr_desc_", null);
        lang.nextStep("Erklärung des Ablaufs");
        description2SourceCode.hide();
        
        // properties (colors etc)
        Font fontArith = new Font(Font.SERIF, Font.PLAIN, 14);
        TextProperties propArith = new TextProperties();
        propArith.set("font", fontArith);
        
        GraphProperties graphProps = new GraphProperties();
        graphProps.set("fillColor", Color.white);
        graphProps.set("highlightColor", Color.orange);
        
        RectProperties rp = new RectProperties();
        rp.set("color", Color.orange);
        
        RectProperties rpOperator = new RectProperties();
        rpOperator.set("color", Color.green);
        
        RectProperties rpNoLiteral = new RectProperties();
        rpNoLiteral.set("color", Color.red);
        
        RectProperties rpPop = new RectProperties();
        rpPop.set("color", new Color(100, 149, 237));
        
        Text infixText = lang.newText(new Coordinates(10, 45), "Infix: ", "infixText", null);

        // draw expression text
        Text lastExpressionText = e.createTexts(lang, propArith, new Offset(10, 0, infixText, "NE"));
        
        // draw the source code below the text expression
        SourceCodeProperties scp = new SourceCodeProperties();
        scp.set("highlightColor", new Color(255, 100, 150));
        sourceCodeLR = lang.newSourceCode(new Coordinates(10, 45 + 20), "SourceCode", null, scp);
        sourceCodeLR.addMultilineCode(SOURCE_CODE, "SourceCode_line", null);
        
        codeDescriptionPosition = new Offset(0, 10, sourceCodeLR, "SW");
        
        codeDescriptionStyle = new SourceCodeProperties();
        Font codeDescriptionFont = new Font(Font.SERIF, Font.BOLD, 11);
        codeDescriptionStyle.set("font", codeDescriptionFont);
               
        // draw expression tree (manually move to left enough)
        int relPos = Integer.MAX_VALUE;
        Coordinates leftLeaf = e.getLeftLeafCoordinates(new Coordinates(relPos, 0));
        int leftTreeSize = relPos - leftLeaf.getX();
        // 240 = width of source code. warning: must change when changing the fontsize!
        Graph graph = e.createGraph(lang, graphProps, new Coordinates(240 + leftTreeSize, 45 + 20 + 10 + Expression.nodeRadius));//new Offset(100, 0, sourceCodeLR, "NE"));
        
        Text stackText = lang.newText(new Offset(5, 10, graph, "SW"), "Stack: ", "stackText", null);
        
        Text postfixText = lang.newText(new Offset(20, 0, lastExpressionText, "NE"), "Postorder: ", "postfixText", null);
        
        e.convertToLRPostfix(lang, new Offset(10, 0, postfixText, "NE"), new Offset(10, 5, stackText, "SW"), propArith, rp, rpOperator, rpNoLiteral, rpPop);
        updateCodeDescription(lang, "Fertig.");
        lang.nextStep("Ergebnis");
        
        // hide everything
        lang.hideAllPrimitives();
        // show the title
        headline.show();
        
        SourceCode summarySourceCode =lang.newSourceCode(new Offset(0, 5, headline, "SW"), "description", null);
        //Text description = language.newText(new Offset(0, 10, headline, "SW"), DESCRIPTION, "description", null);
        ConvertInformation inf = e.convertToPostfix(true);
        String fullSummary = SUMMARY
        		+ "Statistiken zu der LR-Konvertierung von \n"
        		+ "\t " + e + "\n"
        		+ "in den LR-Postorder Ausdruck\n"
        		+ "\t " + inf.postOrder + "\n"
        		+ " \n"
        		+ "- Konvertierte Literale: " + inf.numberOfLiterals + "\n"
        		+ "- Konvertierte binäre Ausdrücke: " + inf.numberOfExpressions + "\n"
        		+ "- Anzahl an Stackoperationen für Auswertung: " + inf.stackOperations + "\n"
				+ "- Maximale Stackgröße bei Auswertung: " + inf.maxStackSize + "\n";

        ConvertInformation infRev = e.convertToPostfix(false);
        
        fullSummary += SUMMARY_RL;
        
        fullSummary += 
        		"Statistiken für RL-Konvertierung von\n"
        		+ "\t " + e + "\n"
        		+ "in den RL-Postorder Ausdruck\n"
        		+ "\t " + infRev.postOrder + "\n"
        		+ " \n"
        		+ "- Konvertierte Literale: " + infRev.numberOfLiterals + "\n"
        		+ "- Konvertierte binäre Ausdrücke: " + infRev.numberOfExpressions + "\n"
        		+ "- Anzahl an Stackoperationen für Auswertung: " + infRev.stackOperations + "\n"
				+ "- Maximale Stackgröße bei Auswertung: " + infRev.maxStackSize + "\n";
        
        summarySourceCode.addMultilineCode(fullSummary, "desc_", null);
        lang.nextStep("Zusammenfassung");  
        
        lang.finalizeGeneration();
    }

    /**
     * Contains sample usages of this class
     * @param args
     */
    @SuppressWarnings("unused")
	public static void main(String[] args) {
        // Create a new language object for generating animation code
        // this requires type, name, author, screen width, screen height
        Language language = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT,
                "ArithParser", "Jannis Weil, Hendrik Wuerz", 640, 480);
        language.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
                
        // define/load expressions
        Expression e0 = new Literal(1);
        Expression e1 = new Add(new Literal(1), new Literal(2));
        Expression e2 = new Add(new Literal(1), new Add(new Literal(2), new Add(new Literal(3), new Literal(4))));
        Expression e3 = new Add(new Add (new Add(new Literal(1), new Add(new Literal("a"), new Literal("b"))), new Literal("c")), new Add(new Literal(1), new Add(new Literal(1), new Add(new Literal(2), new Literal(3)))));
        
        //String input = "((((((((1 + 1) + (1 + 1)) + 1) + (1 + 1)) + 1) + (1 + 1)) + 1) + (1 + 9))";
        String input = "(1 + (2 + 3))";
        //String input = "((1 + 2) * (a - (b / c)))";
        animateExpression(language, input);
        finalize(language);
    }
}
