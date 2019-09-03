/*
 * Horn.java
 * Victoria Stanilescu, Kristina Raysbikh, 2017 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JOptionPane;
import algoanim.primitives.Point;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.SquareProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import generators.framework.ValidatingGenerator;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;

public class Horn implements ValidatingGenerator{
    private Language lang;
    private Color pixelColor;
    private int radius;

    private static final String SOURCE_CODE     = "d = -radius"							// 0
													+"\n x = radius"						// 1
													+"\n y = 0"						// 2
													+"\n     Wiederhole bis y > x"	// 3
													+"\n     Pixel (x, y) sowie symmetrische Pixel einfaerben"	// 4
													+"\n     d = d + 2*y + 1"		// 5
													+"\n     y = y + 1"				// 6
													+"\n     Wenn d > 0" 			// 7
													+"\n         x = x - 1"			// 8
													+"\n         d = d - 2*x"		// 9
													+"\n";
	  
    /**
	   * default duration for swap processes
	   */
	public final static Timing  defaultDuration = new TicksTiming(30);
	  
    public void init(){
        lang = new AnimalScript("Rasterung von Kreisen", "Victoria Stanilescu, Kristina Raysbikh", 800, 600);
		lang.setStepMode(true);
		lang.setInteractionType(AnimalScript.INTERACTION_TYPE_AVINTERACTION);
    }

    public String getName() {
        return "Rasterung von Kreisen";
    }

    public String getAlgorithmName() {
        return "Methode von Horn";
    }

    public String getAnimationAuthor() {
        return "Victoria Stanilescu, Kristina Raysbikh";
    }

    public String getDescription(){
        return "Das Algorithmus, um Kreise zu rasterisieren.Bei Horns Verfahren befinden sich die einzufaerbenden "
 +"\n"
 +"Pixel innerhalb eines ein Pixel breiten Bereichs um den idealen Kreisbogen."
 +"\n"
 +"\n"
 +"Anmerkung: Fuer eine bessere Darstellung waehlen Sie bitte fuer den Radius einen Wert zwischen 2 und 12";
    }

    public String getCodeExample(){
        return "d = -radius"
 +"\n"
 +"x = radius"
 +"\n"
 +"y = 0"
 +"\n"
 +"while (y <= x)"
 +"\n"
 +"    Pixel (x, y) sowie symmetrische Pixel einfaerben"
 +"\n"
 +"    d = d + 2*y + 1"
 +"\n"
 +"    y = y + 1"
 +"\n"
 +"    if (d > 0)"
 +"\n"
 +"         x = x - 1"
 +"\n"
 +"         d = d - 2*x";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
   
    public void horn(int radius) {
    		int dimension = 15; 
    		int size = dimension*radius;
    		int d = -radius;
    		int x = radius;
    		int y = 0;
    		
    		titel();
    	// Infotext
    	    SourceCodeProperties einleitungProps = new SourceCodeProperties();
    	    einleitungProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 16));
    	    einleitungProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    	    einleitungProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);

    	    SourceCode einleitung = lang.newSourceCode(new Coordinates(180, 150), "einleitung", null, einleitungProps);
    	    einleitung.addCodeLine("Die Horn Methode ist ein einfacher Algorithmus aus der Computergrafik,", null, 0, null);
    	    einleitung.addCodeLine("welcher das Zeichnen (Rastern)eines Kreises auf dem Punktraster einer ", null, 0, null);
    	    einleitung.addCodeLine("Rastergrafik durch Einfaerben entsprechender Pixel ermoeglicht.", null, 0, null);
    	    einleitung.addCodeLine("", null, 0, null);
    		einleitung.addCodeLine("Es wird zuerst der erste Oktand betrachtet und dann auch die symmetrischen", null, 0, null);
    		einleitung.addCodeLine("Punkte eingefaerbt. Man faengt beim Punkt (r,0) an und geht dann entlang", null, 0, null);
    		einleitung.addCodeLine("der Kreislinie bis hoch zu einem Winkel von 45 Grad. Hierbei wird die y-Achse", null, 0, null);
    		einleitung.addCodeLine("inkrementiert, waehrend x ab und zu dekrementiert wird. Die durch den",null, 0, null);
    		einleitung.addCodeLine("Algorithmus berechneten x- und y-Werte werden auf den Kreis Mittelpunkt",null, 0, null);
    		einleitung.addCodeLine("aufaddiert und dann gezeichnet.",null, 0, null);
    	lang.nextStep();
    		einleitung.hide();
        // set the visual properties for the source code
        SourceCodeProperties scProps = new SourceCodeProperties();
        	scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        	scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 15));
        	scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        	scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        // create the source code entity
        SourceCode sc = lang.newSourceCode(new Coordinates(40, 60), "sourceCode", null, scProps);
        // Add the lines to the SourceCode object.
        // Line, name, indentation, display dealy
        	sc.addCodeLine("horn(radius)", null, 0, null); // 0
        	sc.addCodeLine("d = -radius", null, 2, null); // 1
        	sc.addCodeLine("x = radius", null, 2, null); // 2
        	sc.addCodeLine("y = 0", null, 2, null); // 3
        	sc.addCodeLine("while (y <= x)", null, 3, null); // 4
        	sc.addCodeLine("Pixel (x, y) sowie symmetrische Pixel einfaerben", null, 4, null); // 5
        	sc.addCodeLine("d = d + 2*y + 1", null, 4, null); // 6
        	sc.addCodeLine("y = y + 1", null, 4, null); // 7
        	sc.addCodeLine("if (d > 0)", null, 4, null); // 8
        	sc.addCodeLine("x = x - 1", null, 5, null); // 9
        	sc.addCodeLine("d = d - 2*x", null, 5, null); // 10
        lang.nextStep("Initialisierung");
        // create visualisation
    		PointProperties pPr = new PointProperties();
    		pPr.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
    		pPr.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, Boolean.TRUE);
    		Point p = lang.newPoint(new Coordinates(820,275), "point", null, pPr);
    	
    	//set visual property of circle
    		CircleProperties circlePr = new CircleProperties();
    		circlePr.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
    		circlePr.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    		lang.newCircle(new Offset(0, 0, p, "E"), size, "circle", null, circlePr);
    	// lines
    		PolylineProperties axisProps = new PolylineProperties();
    		axisProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    		axisProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    		axisProps.set(AnimationPropertiesKeys.BWARROW_PROPERTY, Boolean.TRUE);
    		PolylineProperties lineProps = new PolylineProperties();
    		lineProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    		lineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    	    Node[] horizontal_Node = {new Offset(size + 3*dimension, 0, p, "N"),new Offset(-size - 3*dimension, 0, p, "N")};
    	    lang.newPolyline(horizontal_Node, "horizontal_axis", null, axisProps);
    		Node[] vertical_Node = {new Offset(0, -size - 3*dimension, p, "N"), new Offset(0, size + 3*dimension, p, "N")};
    		lang.newPolyline(vertical_Node, "vertical_axis", null, axisProps);
    		Node[] diag_Node1 = {new Offset(-7*size/10, 7*size/10, p, "NW"), new Offset(7*size/10, -7*size/10, p, "N")};
    		lang.newPolyline(diag_Node1, "diagonal_1", null, lineProps);
    		Node[] diag_Node2 = {new Offset(-7*size/10, -7*size/10, p, "SW"), new Offset(7*size/10, 7*size/10, p, "SW")};
    		lang.newPolyline(diag_Node2, "diagonal_2", null, lineProps);
    		
    		//numbers
    		lang.newText(new Offset(20, -15,p, "E"), "1", "1", null);
    		lang.newText(new Offset(5, -25, p, "E"), "2", "2", null);
    		lang.newText(new Offset(-10, -25,p, "W"), "3", "3", null);
    		lang.newText(new Offset(-25, -15,p, "W"), "4", "4", null);
    		lang.newText(new Offset(-25, 1,p, "W"), "5", "5", null);
    		lang.newText(new Offset(-10, 10,p, "W"), "6", "6", null);
    		lang.newText(new Offset(5, 10,p, "E"), "7", "7", null);
    		lang.newText(new Offset(20, 1,p, "E"), "8", "8", null);

    		//set the visual properties of pixel
    		SquareProperties pixelProp = new SquareProperties();
    		pixelProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    		pixelProp.set(AnimationPropertiesKeys.FILL_PROPERTY, pixelColor);
    		pixelProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    		pixelProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    		
    		// Variablen
       	 TextProperties infoTextProps = new TextProperties();
       	    infoTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 16));
       	    infoTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
       	    infoTextProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

       	    RectProperties infoRectProps = new RectProperties();
       	    infoRectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
       	    infoRectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
       	    infoRectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
       	    infoRectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

       	    TextProperties infoTitelProps = new TextProperties();
       	    infoTitelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 18));
       	    infoTitelProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
       	    infoTitelProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

           lang.newPoint(new Coordinates(100, 350), "var_mark", null, pPr);
           lang.newRect(new Offset(0, 0, "var_mark", "N"), new Offset(180, 120, "var_mark","N"), "variablenRect", null, infoRectProps);
           lang.newText(new Offset(30, 5, "var_mark", "N"), "Variablen", "variablen", null, infoTitelProps);
           lang.newText(new Offset(10, 30, "var_mark","N"), "radius = "+radius, "var_r", null, infoTextProps);
           Text var_x = lang.newText(new Offset(10, 50, "var_mark","N"), "x = ", "var_x", null, infoTextProps);
           Text var_y = lang.newText(new Offset(10, 70, "var_mark","N"), "y = ", "var_y", null, infoTextProps);
           Text var_d = lang.newText(new Offset(10, 90, "var_mark","N"), "d = ", "var_d", null, infoTextProps);
          
    lang.nextStep();
        //code    	
    		sc.highlight(1);
        	sc.highlight(2);
        	sc.highlight(3);
        	 var_x.setText("x = " + x, null, null);
             var_y.setText("y = " + y, null, null);
             var_d.setText("d = " + d, null, null);
        lang.nextStep();
        	sc.unhighlight(1);
        	sc.unhighlight(2);
        	sc.unhighlight(3);
        	
        lang.nextStep();
        	while(y <= x) {
        	sc.highlight(4);
        	sc.unhighlight(8);
        	sc.unhighlight(10);
        lang.nextStep();	
        	sc.unhighlight(4);
        	sc.highlight(5);
        // 		(x, y), (y, x) einfaerben
        	//(x,y)
    		lang.newSquare(new Offset(x*dimension-dimension/2, y*dimension-dimension/2, p, "NE"), dimension, "pixel_1", null, pixelProp);
    		//(-x,y)
    		lang.newSquare(new Offset(-x*dimension-dimension/2, y*dimension-dimension/2, p, "NW"), dimension, "pixel_2", null, pixelProp);
    		//(y,x)
    		lang.newSquare(new Offset(y*dimension-dimension/2, x*dimension-dimension/2, p, "NE"), dimension, "pixel_3", null, pixelProp);
    		//(-y, x)
    		lang.newSquare(new Offset(-y*dimension-dimension/2, x*dimension-dimension/2, p, "NW"), dimension, "pixel_4", null, pixelProp);
    		//(y, -x)
    		lang.newSquare(new Offset(y*dimension-dimension/2, -x*dimension-dimension/2, p, "SE"), dimension, "pixel_5", null, pixelProp);
    		//(x,-y)
    		lang.newSquare(new Offset(x*dimension-dimension/2, -y*dimension-dimension/2, p, "SE"), dimension, "pixel_6", null, pixelProp);
    		//(-x,-y)
    		lang.newSquare(new Offset(-x*dimension-dimension/2, -y*dimension-dimension/2, p, "SW"), dimension, "pixel_7", null, pixelProp);
    		//(-y,-x)
    		lang.newSquare(new Offset(-y*dimension-dimension/2, -x*dimension-dimension/2, p, "SW"), dimension, "pixel_8", null, pixelProp);
        	
        	lang.nextStep();
        	d = d+2*y+1;
            var_d.setText("d = " + d, null, null);
        	sc.unhighlight(5);
        	sc.highlight(6);
        	
        	lang.nextStep();
    		++y;
    		var_y.setText("y = " + y, null, null);
    	    sc.unhighlight(6);
        	sc.highlight(7);
    		
    		lang.nextStep();
    		sc.unhighlight(7);
    		sc.highlight(8);
        	if(d>0) {
           	
        	lang.nextStep();
    		--x;
    		var_x.setText("x = " + x, null, null);
    	    sc.unhighlight(8);
        	sc.highlight(9);
           	
           	lang.nextStep();
           	d = d-2*x;
           	var_d.setText("d = " + d, null, null);
            sc.unhighlight(9);
           	sc.highlight(10);
           		}
        	}
        	sc.unhighlight(4);
        	sc.unhighlight(10);
        	
        	 // Abschluss
            lang.nextStep("Fazit");
            lang.hideAllPrimitives();
            titel();

            // Infotext
            SourceCode abschluss = lang.newSourceCode(new Coordinates(200, 200), "abschluss", null, einleitungProps);
            abschluss.addCodeLine("Es gibt mehrere Methoden Kreise zu zeichnen. Der hier dargestellte", null, 0, null);
            abschluss.addCodeLine("Code ist einer der Algorithmen zur einfarbigen Rasterung. Die Methode", null, 0, null);
            abschluss.addCodeLine("von Horn ist schneller im Vergleich zu anderen Algorithmen, da sie nur", null, 0, null);
            abschluss.addCodeLine("Additionen und Subtraktionen verwendet, welche deutlich geringere", null, 0, null);
            abschluss.addCodeLine("Ausfuehrungzeiten benoetigen als langsame Multiplikationen (Methode von Metzger).", null, 0, null);

            lang.nextStep("Fragen");
    		// Question 1
    		MultipleChoiceQuestionModel winkel = new MultipleChoiceQuestionModel("Winkel");
    		winkel.setPrompt("Bis zum welchen Winkel wird der Algorithmus berechnet?");
    		winkel.addAnswer("90", 0, "falsch");
    		winkel.addAnswer("45", 1, "richtig");
    		winkel.addAnswer("22.5", 0, "falsch");
    		lang.addMCQuestion(winkel);
    		lang.nextStep();
    		// Question 2
    		TrueFalseQuestionModel algo = new TrueFalseQuestionModel("Algo", false, 1);
    		algo.setPrompt("Die Methode von Horn ist die eizige Methode fuer die Rasterung von Kreisen?");
    		algo.setFeedbackForAnswer(false, "richtig, es gibt mehr als eine Methode");
    		algo.setFeedbackForAnswer(true, "falsch, es gibt mehr als eine Methode. ");
    		lang.addTFQuestion(algo); 
    		
    		lang.nextStep();
    		// Question 3
    		FillInBlanksQuestionModel oktanten = new FillInBlanksQuestionModel("oktanden");
    		oktanten.setPrompt("Wieviele Oktanten gibt es?");
    		oktanten.addAnswer("8", 1, "Ihre Antwort ist richtig");
    		lang.addFIBQuestion(oktanten);
    		lang.nextStep();
    		lang.hideAllPrimitives();
    		Text result1 = lang.newText(new Offset(-250, 100, "titel", "S"), "Wenn Sie Ihren Resultat von Questions ansehen moechten,", "result", null);
    		Text result2 = lang.newText(new Offset(0, 10, result1, "SW"), "dann waehlen Sie in 'Animal Control Center' Help -> Quiz Results ", "result", null);
    		lang.nextStep();
    		result1.hide();
    		result2.hide();
        }  
        
          private void titel() {
    	    TextProperties titelProps = new TextProperties();
    	    titelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 18));
    	    titelProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.gray);
    	    titelProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    	    lang.newText(new Coordinates(300, 10), "Methode von Horn fuer die Rasterung von Kreisen",
    	        "titel", null, titelProps);
    	  }

 
    protected String getAlgorithmCode() {
      return SOURCE_CODE;
    }

    @Override
      public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
      	 pixelColor = (Color)primitives.get("pixelColor");
           radius = (Integer)primitives.get("radius");
           this.horn(radius);
           lang.finalizeGeneration();
           return lang.toString();
      }
      
      @Override
      public boolean validateInput(AnimationPropertiesContainer props,Hashtable<String, Object> primitives)
      		throws IllegalArgumentException {
      	if((int)primitives.get("radius") < 2){
      		JOptionPane.showMessageDialog(null, "The radius is too small.", "Invalid radius", JOptionPane.WARNING_MESSAGE);
      		return false;
      	} 
      	if((int)primitives.get("radius") > 12){
      		JOptionPane.showMessageDialog(null, "The radius is too big.", "Invalid radius", JOptionPane.WARNING_MESSAGE);
      		return false;
      	} 
      	return true;
      }
}