package generators.maths;

//import FibonacciRec;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class FiboRecursiv implements Generator {
    private Language lang;
    private SourceCodeProperties sourceCode;
    private Color HighlightColor;
    private int n;
    private boolean showRedundance;
    private boolean[] computedFib;
    private Color redundanceColor;
    
	private int counter;
	private SourceCode codeSupport;

    public void init(){
        lang = new AnimalScript("Fibonacci Rekursiv[DE]", "Lars Schulte, Marius Diebel", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	lang.setStepMode(true);
        sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        TextProperties p = (TextProperties)props.getPropertiesByName("HighlightColor");
        HighlightColor = (Color) p.get(AnimationPropertiesKeys.COLOR_PROPERTY);
        n = (Integer)primitives.get("n");
        showRedundance = (Boolean) primitives.get("zeigeRedundanz");
        redundanceColor = (Color)primitives.get("RedundanzFarbe");
        
		int length = (int) (50 * Math.pow(2, n - 1));
		
		computedFib = new boolean[n+1];

		start(n, length);

		
        return lang.toString();
    }

    public String getName() {
        return "Fibonacci Rekursiv[DE]";
    }

    public String getAlgorithmName() {
        return "Fibonacci";
    }

    public String getAnimationAuthor() {
        return "Lars Schulte, Marius Diebel";
    }

    public String getDescription(){
        return "Die Fibonaccifolge ist eine rekursiv definierte Funktion, da jede Fibonacci Zahl die Summe"
 +"\n"
 +"der beiden vorhergehenden Fibonacci Zahlen ist. Die einzigen festgelegten Zahlen sind die"
 +"\n"
 +" F(0) = 0 und F(1) = 1. Entsprechend sucht der Algorithmus so lange bis er eine dieser Zahlen findet"
 +"\n"
 +"und gibt diese dann zurueck, wodurch die naechste Zahl berechnet werden kann";
    }

    public String getCodeExample(){
        return "Fibonacci (int n)"
 +"\n"
 +"	if n = 1 return 1"
 +"\n"
 +"	if n = 0 return 0"
 +"\n"
 +"	return Fibonacci (n-1) + Fibonacci(n-2)";
    }

    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public Locale getContentLocale() {
        return Locale.GERMANY;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    
	private void start(int n, int length) {

		TextProperties tp = new TextProperties();
		tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 24));
		tp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		Text title = lang.newText(new Coordinates(20, 20), "Fibonacci", "title", null, tp);
		
		
		RectProperties rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		lang.newRect(new Offset(-5, -5, title, "NW"), new Offset(5, 5, title, "SE"), "titlebox", null, rp);
		
		
		lang.nextStep("Einleitung");
		
		String[] desc = getDescription().split("\n");
		int z = 0;
		int x = 200;
		int y = 0;
		
		Text[] description = new Text[5];
		
		//pseudo text just to align the others
		lang.newText(new Coordinates(x, y), "", "desc-1", null);
		
		for (String s : desc){
			Node node = new Offset(0, 10, "desc" +(z-1), "SW"); 
			Text text = lang.newText(node, s, "desc"+z, null);
			description[z] = text;
			z++;
			y += 20;
		}
		Node node = new Offset(0, 10, "desc" +(z-1), "SW"); 
		Text text = lang.newText(node, "", "desc"+z, null);
		description[4] = text;

		
		lang.nextStep("Source Code");
		
		
		// create the source code entity
		SourceCode sc = lang.newSourceCode(new Coordinates(10, 150),
				"sourceCode", null, sourceCode);

		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy
		sc.addCodeLine("Fibonacci(int n)", null, 0, null); // 0
		sc.addCodeLine("if n = 1 return 1", null, 1, null); // 1
		sc.addCodeLine("if n = 0 return 0", null, 1, null); // 2
		sc.addCodeLine("return (Fibonacci(n-1) + Fobonacci(n - 2))", null, 1,
				null); // 3
		sc.addCodeLine("end", null, 0, null); // 4

		codeSupport = sc;

		lang.nextStep("Initialisierung");

		fibonacci(n, new Coordinates(length, 250), length, false,
				null);
		sc.toggleHighlight(3, 4);
		
		lang.nextStep("Ende");
		
		/* 
		"Wie man sieht ist der rekursive Ansatz nicht unbedingt der performanteste"
		"da manche Zahlen, wie hier F(2) mehrfach berechnet werden"
		"Die Komplexitaet liegt hier bei O(2^n)"
		"Zum Vergleich: die Funktion F(n) wurde insgesamt 9 mal aufgerufen"
		"davon waren 4 Berechnungen redundant. Dies Entspricht 44% der Aufrufe"
}
		 */
		int zuViel = counter - (n + 1);
		float prozent = (float)(zuViel * 100) / counter;
		
		description[0].setText("Wie man sieht ist der rekursive Ansatz nicht unbedingt der performanteste", null, null);
		description[1].setText("da manche Zahlen, wie hier beispielsweise F(2) mehrfach berechnet werden", null, null);
		description[2].setText("Die Komplexitaet liegt hier bei O(2^n)", null, null);
		description[3].setText("Zum Vergleich: die Funktion F(n) wurde insgesamt " +counter+ " mal aufgerufen", null, null);
		description[4].setText("davon waren " +zuViel+" Berechnungen redundant. Dies Entspricht " +prozent+"% der Aufrufe", null, null);
		
		
	}

	/*
	 * length = x length of line to previous fib number child = if this is a
	 * child, therefore it has a parent and needs to get a connection to him
	 */
	private int fibonacci(int n, Coordinates pos, int length, boolean child,
			String parent) {
		counter++;
		String name = "node" + counter;
		Text text = lang.newText(pos, "F(" + n + ")", name, null);
		text.changeColor("color", HighlightColor, null, null);

		
//		Node firstNode = new Offset(0,0, "node1", "W");
//		Node secondNode = new Offset(0,0, "node" + counter, "E");
//		Node[] allNodes = {firstNode, secondNode};
//		Polyline p = lang.newPolyline(allNodes, "ob", null);
		
		codeSupport.toggleHighlight(3, 0);

		
		if (child) {
			Node parentNode = new Offset(0, 0, parent, "S");
			Node myNode = new Offset(0, 0, "node" + counter, "N");
			Node[] nodes = { parentNode, myNode };
			lang.newPolyline(nodes, "pl" + counter, null);
		}
		lang.nextStep("Iteration "+counter);
		
		Polyline reduntPol = null;
		Text reduntText = null;
		
		if(showRedundance && computedFib[n] == true)
		{
			// Node Poition errechnen
			Node redundanceNode = new Offset(0, 0, "node"+ ((this.n - n) + 1), "E");
			//Linie zeichnen
			Node myRNode = new Offset(0, 0, "node" + counter, "W");
			Node[] rNodes = {redundanceNode,myRNode};
			reduntPol = lang.newPolyline(rNodes, "redundanc" + counter, null);
			reduntPol.changeColor("Color", redundanceColor, null, null);
			
			reduntText = lang.newText(new Offset(0, 0, "redundanc" + counter, "S"), "redundant", "rText" + counter, null);
			reduntText.changeColor("Color", redundanceColor, null, null);
		}
		

		if (n == 1) {
			codeSupport.toggleHighlight(0, 1);
			text.setText("F(" + n + ") = 1", null, null);

			lang.nextStep();
			
			if(showRedundance && computedFib[n] == true){
			reduntPol.hide();
			reduntText.hide();
			}
			
			text.changeColor(null, Color.BLACK, null, null);
			computedFib[n] = true;
			return 1;
		}
		if (n == 0) {
			codeSupport.toggleHighlight(0, 2);
			text.setText("F(" + n + ") = 0", null, null);

			lang.nextStep();
			
			if(showRedundance && computedFib[n] == true){
			reduntPol.hide();
			reduntText.hide();
			}

			text.changeColor(null, Color.BLACK, null, null);
			computedFib[n] = true;
			return 0;
		}

		// step
		codeSupport.toggleHighlight(0, 3);
		text.setText("F(" + n + ") = F(" + (n - 1) + ") + F(" + (n - 2) + ")",
				null, null);

		lang.nextStep();
		
		if(showRedundance && computedFib[n] == true){
		reduntPol.hide();
		reduntText.hide();
		}
		
		Coordinates posLeft = new Coordinates(pos.getX() - length / 2,
				pos.getY() + 50);
		Coordinates posRight = new Coordinates(pos.getX() + length / 2,
				pos.getY() + 50);

		text.changeColor(null, Color.BLACK, null, null);
		int a = fibonacci(n - 1, posLeft, length / 2, true, name);
		// step when first number gets back
		codeSupport.unhighlight(1);
		codeSupport.unhighlight(2);
		codeSupport.highlight(3);
		text.changeColor("color", HighlightColor, null, null);
		text.setText("F(" + n + ") = " + a + " + F(" + (n - 2) + ")", null,
				null);

		lang.nextStep();

		text.changeColor(null, Color.BLACK, null, null);
		int b = fibonacci(n - 2, posRight, length / 2, true, name);
		// step when second number gets back
		int result = a + b;

		codeSupport.unhighlight(1);
		codeSupport.unhighlight(2);
		codeSupport.highlight(3);
		text.changeColor("color", HighlightColor, null, null);
		text.setText("F(" + n + ") = " + a + " + " + b + " = " + result, null,
				null);

		lang.nextStep();

		text.changeColor(null, Color.BLACK, null, null);
		computedFib[n] = true;
		return (result);
	}

}