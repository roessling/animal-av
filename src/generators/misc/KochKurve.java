/*
 * KochKurve.java
 * Christian Staab, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.SourceCodeProperties;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.main.Animal;


public class KochKurve implements ValidatingGenerator {
    private Language lang;
    private SourceCodeProperties textProps;
    private int initialLineLength;

    public void init(){
        lang = new AnimalScript("Koch Kurve", "Christian Staab", 800, 600);
    }

    public boolean validateInput(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        initialLineLength = (Integer)primitives.get("initialLineLength");
        if(initialLineLength >= 240 && initialLineLength <= 660) {
        	return true;
        }
        return false;
    }
    
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        textProps = (SourceCodeProperties)props.getPropertiesByName("textProps");
        initialLineLength = (Integer)primitives.get("initialLineLength");
        
        lang.setStepMode(true);
        drawKochCurve(initialLineLength, textProps);
        
        return lang.toString();
    }

    public String getName() {
        return "Koch Kurve";
    }

    public String getAlgorithmName() {
        return "Koch Kurve";
    }

    public String getAnimationAuthor() {
        return "Christian Staab";
    }

    public String getDescription(){
        return "Die Koch Kurve ist eine ueberall stetige aber niegends differenzierbare Kurve."
 +"\n"
 +"Sie war eine der ersten beschriebenen Fraktale."
 +"\n"
 +"Um sie zu konstruieren wird eine Linie in drei Teile geteilt"
 +"\n"
 +"und der mittlere Teil durch ein gleichseitiges Dreieck"
 +"\n"
 +"derselben Laenge ersetzt und anschliessend die mittlere Linie entfernt."
 +"\n"
 +"Dieser Vorgang wird beliebig oft wiederholt.";
    }

    public String getCodeExample(){
        return "1. Teile jede Linie in 3 gleich lange Segmente."
 +"\n"
 +"2. Ersetze das mittlere Segment aus Schritt 1"
 +"\n"
 +"   durch ein gleichseitiges Dreieck."
 +"\n"
 +"3. Entferne das Basis Segment des Dreiecks."
 +"\n"
 +"4. Wiederhole Schritte 1 bis 3.";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    
    
    

    /**
     * Draws the koch curve and snowflake as well as the pseudo code and descriptions
     */
    public void drawKochCurve(int initialLineLength, SourceCodeProperties textProps) {
      
  	// Create header
    	
    	TextProperties headerProps = new TextProperties();
    	Font headerFont = (Font) headerProps.get("font");
    	headerFont = headerFont.deriveFont(1,36);
    	headerProps.set("font", headerFont);
        
    	String headerText = "Koch Kurve";
      Text header = lang.newText(new Coordinates(0, 0), headerText, "header", null, headerProps);
      
     // initial description
      //SourceCodeProperties descrProps = new SourceCodeProperties();
      SourceCode descr = lang.newSourceCode(new Coordinates(300, 20), "descr",
          null, textProps);
      descr.addCodeLine("Die Koch Kurve ist eine ueberall stetige aber niegends differenzierbare Kurve.", null, 0, null);
      descr.addCodeLine("Sie war eine der ersten beschriebenen Fraktale.", null, 0, null);
      descr.addCodeLine("Um sie zu konstruieren wird eine Linie in drei Teile geteilt", null, 0, null);
      descr.addCodeLine("und der mittlere Teil durch ein gleichseitiges Dreieck", null, 0, null);
      descr.addCodeLine("derselben Laenge ersetzt und anschliessend die mittlere Linie entfernt.", null, 0, null);
      descr.addCodeLine("Dieser Vorgang wird beliebig oft wiederholt.", null, 0, null);
      
      
      
  	
      // set the visual properties for the source code
      /*SourceCodeProperties scProps = new SourceCodeProperties();
      scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
      scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
          new Font("Monospaced", Font.PLAIN, 12));

      scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
      scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
*/
      // now, create the source code entity
      SourceCode sc = lang.newSourceCode(new Coordinates(300, 20), "sourceCode",
          null, textProps);

      // Add the lines to the SourceCode object.
      sc.addCodeLine("1. Teile jede Linie in 3 gleich lange Segmente.", null, 0, null);
      sc.addCodeLine("2. Ersetze das mittlere Segment aus Schritt 1 ", null, 0, null);
      sc.addCodeLine("   durch ein gleichseitiges Dreieck.", null, 0, null);
      sc.addCodeLine("3. Entferne das Basis Segment des Dreiecks.", null, 0, null);
      sc.addCodeLine("4. Wiederhole Schritte 1 bis 3.", null, 0, null);
      
      sc.hide();
      
      Text iterationCount = lang.newText(new Offset(20,20, header, AnimalScript.DIRECTION_SW), "", "iterationCount", null);
      
      lang.nextStep("Start");
      
      descr.hide();
      sc.show();
      
      sc.highlight(0);
      
      Coordinates[] initCoords = {new Coordinates(20,300), new Coordinates(20 + initialLineLength,300)};
      Polyline initialLine = lang.newPolyline(initCoords, "initialLine", null);
      
      
      
      int iteration = 0;
      int lineLength = initialLineLength;
      String lms = "F";
      drawOneCurveIteration(lms, lineLength, iteration, iterationCount, true);
      
      sc.unhighlight(0);
      sc.highlight(1);
      sc.highlight(2);
      
      lms = replaceLM(lms);
      iteration += 1;
      drawOneCurveIteration(lms, lineLength, iteration, iterationCount, false);
      initialLine.hide();
      
      sc.unhighlight(1);
      sc.unhighlight(2);
      sc.highlight(3);
      
      drawOneCurveIteration(lms, lineLength, iteration, iterationCount, true);
      
      sc.unhighlight(3);
      sc.highlight(4);
      
      for(int i=0;i<3;i++) {
  	    lms = replaceLM(lms);
  	    iteration += 1;
  	    drawOneCurveIteration(lms, lineLength, iteration, iterationCount, true);
      }
      
      
      sc.hide();
      
      

      iteration = 3;
      lineLength = 200;
      lms = "F--F--F";
      lms = replaceLM(lms);
      lms = replaceLM(lms);
      lms = replaceLM(lms);
      drawSnowflake(lms, lineLength, iteration, iterationCount);
      
      
      //SourceCodeProperties endingProps = new SourceCodeProperties();
      SourceCode ending = lang.newSourceCode(new Coordinates(300, 20), "ending",
          null, textProps);
      ending.addCodeLine("Die Koch Kurve ist nach Konstruktionsvorschrift selbstaehnlich,", null, 0, null);
      ending.addCodeLine("d.h. bei beliebiger Vergroesserung erscheinen immer wieder dieselben", null, 0, null);
      ending.addCodeLine("Strukturen. Ihre Hausdorff-Dimension ist log(4)/log(3).", null, 0, null);
      ending.addCodeLine("Verwendet man als Start Input ein Dreieck statt einer Linie erhaelt man", null, 0, null);
      ending.addCodeLine("eine Kochsche Schneeflocke. Diese ist nicht selbstaehnlich", null, 0, null);
      ending.addCodeLine("und schliesst trotz unendlicher Laenge nur eine endliche Flaeche ein.", null, 0, null);
      ending.addCodeLine("Diese Eigenschaft wird z.B. zum bauen von Fraktalantennen genutzt.", null, 0, null);
      
      
      
      lang.nextStep("Fazit");
      //System.out.println(lms);
      
      //lms = replaceLM(lms);
      
      //System.out.println(lms);
    }

    /**
     * creates a single iteration step for a koch curve
     * @param lms
     * 	The Lindem Mayer System String describing the curve
     * 
     * @param lineLength
     * 	The length of the original line
     * 
     * @param iteration
     * 	The iteration number
     * 
     * @param iterationCount
     * 	The Text element displaying the iteration numeber
     * 
     * @param displayIndex
     * 	boolean, if true display a new entry to the table of contents
     */
    private void drawOneCurveIteration(String lms, int lineLength, int iteration, Text iterationCount, boolean displayIndex) {
  	    String iterationText = "Iteration: " + Integer.toString(iteration);
  	    iterationCount.setText(iterationText, null, null);
  	  
  	    PolylineProperties lineProps = new PolylineProperties();
  	    
  	    int x0 = 20;
  	    int y0 = 300;
  	    int dx = 0;
  	    int dy = 0;
  	    int angle = 0;
  	    double rad = 0.0;
  	    
  	    int max = 1;
  	    for(int i=0;i<iteration;i++) {
  	    	max *= 4;
  	    	lineLength /= 3;
  	    }
  	    max += 1;
  	    
  	    Node[] vertices = new Node[max];
  	    vertices[0] = new Coordinates(x0,y0);
  	    
  	    int position = 0;
  	    int stringLength = lms.length();
  	    for(int i=1; i<max; i++) {
  	    	while(lms.charAt(position) != 'F' && position < stringLength) {
  				  if(lms.charAt(position) == '+') {angle += 60;}
  				  if(lms.charAt(position) == '-') {angle -= 60;}
  				  position += 1;
  			}
  	    	position += 1;
  	    	
  	    	rad = Math.toRadians(angle);
  	    	dx = (int) (lineLength * Math.cos(rad));
  	    	dy = (int) (-lineLength * Math.sin(rad));
  	    	
  	    	x0 += dx;
  	    	y0 += dy;
  	    	vertices[i] = new Coordinates(x0,y0);
  	    }
  	    String name = "kochkurve" + Integer.toString(iteration);
  	    Polyline line = lang.newPolyline(vertices, name, null);
  	    
  	    if(displayIndex) {
  	    	lang.nextStep(iterationText);
  	    }else {
  	    	lang.nextStep();
  	    }
  	    
  	    line.hide();
    }
    
    
    /**
     * creates a single iteration step for a koch snowflake
     * @param lms
     * 	The Lindem Mayer System String describing the curve
     * 
     * @param lineLength
     * 	The length of the original line
     * 
     * @param iteration
     * 	The iteration number
     * 
     * @param iterationCount
     * 	The Text element displaying the iteration numeber
     * 
     */
    private void drawSnowflake(String lms, int lineLength, int iteration, Text iterationCount) {
  	    String iterationText = "Iteration: " + Integer.toString(iteration);
  	    iterationCount.setText(iterationText, null, null);
  	  
  	    PolylineProperties lineProps = new PolylineProperties();
  	    
  	    int x0 = 20;
  	    int y0 = 160;
  	    int dx = 0;
  	    int dy = 0;
  	    int angle = 0;
  	    double rad = 0.0;
  	    
  	    int max = 3;
  	    for(int i=0;i<iteration;i++) {
  	    	max *= 4;
  	    	lineLength /= 3;
  	    }
  	    max += 1;
  	    
  	    Node[] vertices = new Node[max];
  	    vertices[0] = new Coordinates(x0,y0);
  	    
  	    int position = 0;
  	    int stringLength = lms.length();
  	    for(int i=1; i<max; i++) {
  	    	while(lms.charAt(position) != 'F' && position < stringLength) {
  				  if(lms.charAt(position) == '+') {angle += 60;}
  				  if(lms.charAt(position) == '-') {angle -= 60;}
  				  position += 1;
  			}
  	    	position += 1;
  	    	
  	    	rad = Math.toRadians(angle);
  	    	dx = (int) (lineLength * Math.cos(rad));
  	    	dy = (int) (-lineLength * Math.sin(rad));
  	    	
  	    	x0 += dx;
  	    	y0 += dy;
  	    	vertices[i] = new Coordinates(x0,y0);
  	    }
  	    
  	    Polyline flake = lang.newPolyline(vertices, "flake", null);
  	    
  }
    
    /**
     * Executes the production rule of the linden meyer system describing the koch curve or snowflake
     * replaces F -> F+F--F+F
     * 
     * @param lms
     * 	the linden meyer system string of the previous iteration that the new string is based on
     * 
     * @return
     * 	return the new String describing the Linden Mayer System
     */
    private String replaceLM(String lms) {
  	  String newLms = "";
  	  
  	  int length = lms.length();
  	  for(int i=0; i<length; i++) {
  		  if(lms.charAt(i) == 'F') {
  			  newLms = newLms + "F+F--F+F";
  		  }else {
  			  newLms = newLms + lms.charAt(i);
  		  }
  	  }
  	  
  	  return newLms;
    }

    
    /**
     * main method
     * @param args
     * 		args
     */
    public static void main(String[] args) {  
  	  KochKurve s = new KochKurve();
  	  Animal.startGeneratorWindow(s);
    }
    
    
}