/*
 * HanoiVisualisierung.java
 * Marius Faust, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.ArrayBasedStack;
import algoanim.primitives.ConceptualStack;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.StackProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import animal.main.Animal;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.enumeration.ControllerEnum;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;


public class HanoiVisualisierung implements ValidatingGenerator {
    private Language lang;
    private int height;
    
    @Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		return (int)arg1.get("height") > 0;
	}

    public void init(){
        lang = new AnimalScript("RekursivHanoi", "Marius Faust", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        height = (Integer)primitives.get("height");
        start(height);
        return lang.toString();
    }

    public String getName() {
        return "RekursivHanoi";
    }

    public String getAlgorithmName() {
        return "RekursivHanoi";
    }

    public String getAnimationAuthor() {
        return "Marius Faust";
    }

    public String getDescription(){
        return "Recursiv Hanoi returns a set of instructions for a given towers of hanoi game which will lead to the fastest possible Solution."
 +"\n"
 +"\n";
    }

    public String getCodeExample(){
        return "private static void bewege(char x, char y, char z, int n)"
 +"\n"
 +"{"
 +"\n"
 +"   if (n == 1) {"
 +"\n"
 +"      Bewege von Stab x nach Stab z"
 +"\n"
 +"   } else {"
 +"\n"
 +"      bewege(x, z, y, n-1);"
 +"\n"
 +"      bewege(x, y, z, 1);"
 +"\n"
 +"      bewege(y, x, z, n-1);"
 +"\n"
 +"   }"
 +"\n"
 +"}"
 +"\n"
 +"\n"
 +"public static void main (String[] args)"
 +"\n"
 +"{"
 +"\n"
 +"   bewege('a', 'b', 'c', \" + height + \")"
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
    
    static String[] INTRO_TEXT = {"Die 'Tuerme von Hanoi' ist ein Knobelspiel, welches in seiner Urform mit 3 Staeben",
			"und beliebig vielen unterschiedlich groen Scheiben gespielt wird.",
			"Dabei sind zunächst alle Scheiben der Größe nach sortiert auf einen der Staebe aufgesteckt.",
			"Bei jedem Spielzug darf eine Scheibe auf einen anderen Stab verschoben werden,",
			"jedoch immer nur auf eine größere Scheibe, nicht auf eine kleinere.",
			"",
			"Ziel des Spiels ist es alle Scheiben auf einen anderen Stab zu verschieben.",
			"In dieser Visualisierung werden die Stäbe von drei Arrays dargestellt",
			"und die Scheiben durch Zahlen, die auch gleichzeitig die jeweilige Größe der Scheibe angeben.",
			"",
			"Der vorgestellte Algorithmus berechnet die optimalen Züge, um das Spiel zu loesen, mithilfe einer rekursiven Methode.",
			"Dazu wird in der Mitte der Visualisierung ein Stapel mit allen aktuell existenten Instanzen der Methode angezeigt.",
			"Die aktiven werden rot eingefaerbt."};
	
	static String[] OUTRO_TEXT = {"Der Algorithmus hat nun alle Scheiben erfolgreich auf Stab C verschoben.",
			"Dazu wurden alle Instanzen der 'bewegen'-Methode, die der Algorithmus erzeugt,",
			"auf den Stapel gelegt und abgearbeitet.",
			"Zu keinem Zeitpunkt lag eine größere auf einer kleineren Scheibe",
			"und der Algorithmus verschob immer einzelne Scheiben.",
			"Der Algorithmus hat also das Spiel 'Tuerme von Hanoi' erfolgreich abgeschlossen."};
	
	ArrayBasedStack<Integer> stackA, stackB, stackC;
	ConceptualStack<String> methStack;
	SourceCode sc;
	TwoValueCounter counter;
	TwoValueView cv;
	Coordinates origin = new Coordinates(0, 0);
	
	private void start(int height) {
		lang.setStepMode(true);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		
		//header		
		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY,  new Font("Monospaced", Font.PLAIN, 16));
		tp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		lang.newText(new Offset(330, 10, origin, AnimalScript.DIRECTION_SW), "Towers of Hanoi(recursiv)", "header", null, tp);
		
		//visual properties for source-code and intro
	    SourceCodeProperties scProps = new SourceCodeProperties();
	    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
	    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));
	    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
	    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    
		//intro
	    SourceCode intro = lang.newSourceCode(new Offset(20, 50, origin, AnimalScript.DIRECTION_SW), "intro", null, scProps);
		for(int i = 0; i < INTRO_TEXT.length; i++){
			intro.addCodeLine(INTRO_TEXT[i], null, 0, null);
		}
		lang.nextStep();
		
		intro.hide();
		
	
	    //create and fill source-code
	    sc = lang.newSourceCode(new Offset(450, 50, origin, AnimalScript.DIRECTION_SW), "sourceCode", null, scProps);
	    
	    sc.addCodeLine("private static void bewege(char x, char y, char z, int n)", null, 0, null);
	    sc.addCodeLine("{", null, 0, null);
	    sc.addCodeLine("if (n == 1) {", null, 1, null);
	    sc.addCodeLine("Bewege von Stab x nach Stab z", null, 2, null);
	    sc.addCodeLine("} else {", null, 1, null);
	    sc.addCodeLine("bewege(x, z, y, n-1);", null, 2, null);
	    sc.addCodeLine("bewege(x, y, z, 1);", null, 2, null);
	    sc.addCodeLine("bewege(y, x, z, n-1);", null, 2, null);
	    sc.addCodeLine("}", null, 1, null);
	    sc.addCodeLine("}", null, 0, null);
	    sc.addCodeLine("", null, 0, null);
	    sc.addCodeLine("public static void main (String[] args)", null, 0, null);
	    sc.addCodeLine("{", null, 0, null);
	    sc.addCodeLine("bewege('a', 'b', 'c', " + height + ");", null, 1, null);
	    sc.addCodeLine("}", null, 0, null);
		
		//prefill the first rod
		List<Integer> prefill = new ArrayList<Integer>();
		for(int i = 0; i < height; i++) {
			prefill.add(Math.abs(i-height));
		}
		
		//visual properties for the rods
		StackProperties stp = new StackProperties();
		stp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    stp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
	    stp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.YELLOW);
	    stp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

	    
	    //visual properties for the stack
	    StackProperties mstp = new StackProperties();
	    mstp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
	    mstp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
	    mstp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
	    mstp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.RED);

	    //create rods
		stackA = lang.newArrayBasedStack(new Offset(20, 60, origin, AnimalScript.DIRECTION_SW), prefill, "Tower A", null, stp, height);
		stackB = lang.newArrayBasedStack(new Offset(20, 160, origin, AnimalScript.DIRECTION_SW), null, "Tower B", null, stp, height);
		stackC = lang.newArrayBasedStack(new Offset(20, 260, origin, AnimalScript.DIRECTION_SW), null, "Tower C", null, stp, height);
		
		//create method-stack
		//relative Offset didn't work, had to use coordinates
		methStack = lang.newConceptualStack(new Coordinates(170, 95), null, "Stack", null, mstp);
		lang.nextStep();
		//create counter for method-stack
		counter = lang.newCounter(methStack);
		CounterProperties cp = new CounterProperties();
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
		
		cv = lang.newCounterView(counter, new Offset(120, 55, origin, AnimalScript.DIRECTION_SW), cp, true, true);	
		
		lang.nextStep("Algorithm start");
		
		//question
		MultipleChoiceQuestionModel question = new MultipleChoiceQuestionModel("assignments");
		question.setPrompt("Wie oft wird die bewege-Methode aufgerufen?");
		question.addAnswer(Double.toString(Math.pow(height, 2)), 0, "falsch");
		question.addAnswer(Double.toString(Math.pow(2, height)-1), 1, "richtig");
		question.addAnswer(Double.toString(Math.pow(height, height)), 0, "falsch");
		question.addAnswer(Integer.toString(2*height), 0, "falsch");
		
		lang.addMCQuestion(question);

		//start recursive hanoi
		monoHighlight(sc, 11);
		lang.nextStep();
		monoHighlight(sc, 12);
		lang.nextStep();
		methStack.push("bewege("+height+", "+stackA.getName()+", "+stackB.getName()+", "+stackC.getName()+")");
		cv.update(ControllerEnum.assignments, 1);
		monoHighlight(sc, 13);
		lang.nextStep();
		recursiveHanoi(height, stackA, stackC, stackB);
		methStack.pop();
		cv.update(ControllerEnum.assignments, 1);
		monoHighlight(sc, 14);
		lang.nextStep("Algorithm end");
		sc.hide();
		cv.hideBar();
		
		//outro
		SourceCode outro = lang.newSourceCode(new Offset(250, 50, origin, AnimalScript.DIRECTION_SW), "outro", null, scProps);
		for(int i = 0; i < OUTRO_TEXT.length; i++){
			outro.addCodeLine(OUTRO_TEXT[i], null, 0, null);
		}
		//generate script-code
		lang.finalizeGeneration();
	}
	
	private void recursiveHanoi(int disks, ArrayBasedStack<Integer> fromRod, ArrayBasedStack<Integer> toRod, ArrayBasedStack<Integer> auxRod) {
		methStack.highlightTopElem(null, new TicksTiming(10));
		monoHighlight(sc, 0);
		lang.nextStep();
		monoHighlight(sc, 1);
		lang.nextStep();
		monoHighlight(sc, 2);
		lang.nextStep();
		if(disks == 1) {
			monoHighlight(sc, 3);
			toRod.push(fromRod.pop());
		} else {
			monoHighlight(sc, 4);
			lang.nextStep();
			
			methStack.highlightTopCell(null, new TicksTiming(10));
			monoHighlight(sc, 5);
			methStack.push("bewege("+(disks-1)+", "+auxRod.getName()+", "+toRod.getName()+", "+fromRod.getName()+")");
			cv.update(ControllerEnum.assignments, 1);
			lang.nextStep();
			monoHighlight(sc, 6);
			methStack.push("bewege("+1+", "+fromRod.getName()+", "+toRod.getName()+", "+auxRod.getName()+")");
			cv.update(ControllerEnum.assignments, 1);
			lang.nextStep();
			monoHighlight(sc, 7);
			methStack.push("bewege("+(disks-1)+", "+fromRod.getName()+", "+auxRod.getName()+", "+toRod.getName()+")");
			cv.update(ControllerEnum.assignments, 1);
			lang.nextStep();
			
			recursiveHanoi(disks-1, fromRod, auxRod, toRod);
			methStack.pop();
			cv.update(ControllerEnum.assignments, 1);
			lang.nextStep();
			

			recursiveHanoi(1, fromRod, toRod, auxRod);
			methStack.pop();
			cv.update(ControllerEnum.assignments, 1);
			lang.nextStep();
			
			recursiveHanoi(disks-1, auxRod, toRod, fromRod);
			methStack.pop();
			cv.update(ControllerEnum.assignments, 1);
			lang.nextStep();
			
			monoHighlight(sc, 8);
			lang.nextStep();
		}
		monoHighlight(sc, 9);
		lang.nextStep();
	}
	
	private void monoHighlight(SourceCode sourceCode, int line) {
		for(int i = 0; i < sc.length(); i++) {
			sourceCode.unhighlight(i);
		}
		sourceCode.highlight(line);
	}
}