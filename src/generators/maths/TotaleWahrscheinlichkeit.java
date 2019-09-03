package generators.maths;

import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.framework.Generator;
import interactionsupport.models.MultipleChoiceQuestionModel;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import algoanim.animalscript.AnimalScript;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JOptionPane;
import algoanim.primitives.Polyline;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;


public class TotaleWahrscheinlichkeit implements ValidatingGenerator{

    private double R1_W;
    private double R3_S_W;
    private double R2_S_W;
    private double R1_S_W;
    private Color HighlightColor;
    private double R2_W;
    private double R3_W;
    

	
	  /**
	   * The concrete language object used for creating output
	   */
	  private Language lang;

	  /**
	   * Default constructor
	   * 
	   * @param l
	   *          the conrete language object used for creating output
	   */
	  public TotaleWahrscheinlichkeit() {
	    // Store the language object
	    // This initializes the step mode. Each pair of subsequent steps has to
	    // be divdided by a call of lang.nextStep();
	  }

                                                                                               // 22


	  
	  /**
	   * default duration for swap processes
	   */
	  public final static Timing  defaultDuration = new TicksTiming(30);




	  protected String getAlgorithmDescription() {
	    return "Hier wird die Berechnung der totalen Wahrscheinlichkeit anhand Baumdiagramme dargestellt, damit man genau nachvollziehen kann man f&uuml;r die Berechnung vorzugehen hat.";
	  }

	  protected String getAlgorithmCode() {
	    return "";
	  }

	  public String getName() {
	        return "Totale Wahrscheinlichkeit";
	  }

	  public String getDescription() {
	        return "Hier wird die Berechnung der totalen Wahrscheinlichkeit anhand Baumdiagramme dargestellt, damit man genau nachvollziehen kann man f&uuml;r die Berechnung vorzugehen hat.";
	    }

	  public String getCodeExample() {
	        return "";
	  }

	  
	  public void render(){

		    TextProperties headerProps = new TextProperties();
		    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		        Font.SANS_SERIF, Font.BOLD, 24));
		    algoanim.primitives.Text header = lang.newText(new Coordinates(20, 30), "Erwartungswert berechnen", "header",
		        null, headerProps);
		    lang.nextStep("Einleitung");
		    
		    TextProperties normalTextProps = new TextProperties();
		    normalTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		    TextProperties boldTextProps = new TextProperties();
		    boldTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));

		    algoanim.primitives.Text text_1 = lang.newText(new Offset(0,30, "header", AnimalScript.DIRECTION_SW),
		    		"Das Gesetz der totalen Wahrscheinlichkeit besagt folgendes: ",
		    		"Text_1", null, normalTextProps);
		    algoanim.primitives.Text text_2 = lang.newText(new Offset(0,10, "Text_1", AnimalScript.DIRECTION_SW), 
		    		"Die marginale Stufe-zwei-Wahrscheinlichkeit eines Ereignisses entspricht der Summe der Wahrscheinlichkeiten aller Szenarios, die zum gesuchten Ereignis führen.",
		    		"Text_2", null, normalTextProps);
		    lang.nextStep();
		    algoanim.primitives.Text text_3 = lang.newText(new Offset(0,10, "Text_2", AnimalScript.DIRECTION_SW), 
		    		"In der Mengennotation lautet das Gesetz der totalen Wahrscheinlichkeit wie folgt:",
		    		"Text_3", null, normalTextProps);
		    lang.nextStep();	
		    
		    algoanim.primitives.Text formel = lang.newText(new Offset(0,10,"Text_3",AnimalScript.DIRECTION_SW), "P(B) = \u03A3( P(A_i) * P(B_i))", 
		    		"Formel", null, boldTextProps);
		    lang.nextStep();

		   		    
		    text_1.hide();
		    text_2.hide();
		    text_3.hide();
		    formel.moveBy(null,0,-80,Timing.FAST,Timing.MEDIUM);
		    lang.nextStep();
		    
		    algoanim.primitives.Text text_4 = lang.newText(new Offset(0,10-80, "Formel", AnimalScript.DIRECTION_SW), 
		    		"Manche haben Schwierigkeiten, die Formel für das Gesetz der totalen Wahrscheinlichkeit zu verstehen.",
		    		"text_4", null, normalTextProps);
		    algoanim.primitives.Text text_5 = lang.newText(new Offset(0,10, "text_4", AnimalScript.DIRECTION_SW), 
		    		"Ein schrittweises Vorgehen kann helfen, indem man einen Baumdiagram aufzeichnet:",
		    		"text_5", null, normalTextProps);		
		    lang.nextStep();	
		    algoanim.primitives.Text schritt_1 = lang.newText(new Offset(0,10, "text_5", AnimalScript.DIRECTION_SW), 
		    		"1. Stellen Sie die Zweige in einem Baumdiagramm dar.",
		    		"text_6", null, normalTextProps);		
		    lang.nextStep();	
		    algoanim.primitives.Text schritt_2 = lang.newText(new Offset(0,10, "text_6", AnimalScript.DIRECTION_SW), 
		    		"2. Ermitteln Sie die Wahrscheinlichkeiten für jeden einzelnen Zweig, dessen Ergebnis zu dem ersuchten Ergebnis gehört.",
		    		"text_7", null, normalTextProps);		
		    lang.nextStep();	
		    algoanim.primitives.Text schritt_3 = lang.newText(new Offset(0,10, "text_7", AnimalScript.DIRECTION_SW), 
		    		"3. Addieren Sie die einzelnen Wahrscheinlichkeiten dieser Zweige. ",
		    		"text_8", null, normalTextProps);		
		    lang.nextStep();
		    text_4.hide();
		    text_5.hide();
		    schritt_1.moveBy(null, 0, -60, Timing.FAST, Timing.MEDIUM);
		    schritt_2.moveBy(null, 0, -60, Timing.FAST, Timing.MEDIUM);
		    schritt_3.moveBy(null, 0, -60, Timing.FAST, Timing.MEDIUM);
		    lang.nextStep();
		    algoanim.primitives.Text text_9 = lang.newText(new Offset(0,10-60, "text_8", AnimalScript.DIRECTION_SW), 
		    		"Wir wollen das anhand eines Beispiels verdeutlichen:",
		    		"text_9", null, normalTextProps);		
		    lang.nextStep("Beispiel");
		    algoanim.primitives.Text bsp_1 = lang.newText(new Offset(0,10, "text_9", AnimalScript.DIRECTION_SW), 
		    		"Ein Kunde kann zwischen drei Restaurants wählen: R1, R2 und R3. Frühere Datenerhebungen haben gezeigt, dass diese Restaurants " + Double.toString(R1_W*100) + " Prozent, ",
		    		"bsp_1", null, normalTextProps);		
		    algoanim.primitives.Text bsp_2 = lang.newText(new Offset(0,10, "bsp_1", AnimalScript.DIRECTION_SW), 
		    		"" + Double.toString(R2_W*100) + "  Prozent bzw. " + Double.toString(R3_W*100) + " Prozent des Umsatzes auf sich ziehen. Sie wissen auch, dass " + Double.toString(R1_S_W*100) + " % der Kunden von Restaurant 1 zufrieden (und " + Double.toString(Math.round((1-R1_S_W)*100)) + " Prozent nicht zufrieden) sind;",
		    		"bsp_2", null, normalTextProps);		
		    algoanim.primitives.Text bsp_3 = lang.newText(new Offset(0,10, "bsp_2", AnimalScript.DIRECTION_SW), 
		    		"bei R2 sind " + Double.toString(R2_S_W*100) + " % der Kunden zufrieden und bei R3 sind " + Double.toString(R3_S_W*100) + " % zufrieden. ",
		    		"bsp_3", null, normalTextProps);		
		    algoanim.primitives.Text bsp_4 = lang.newText(new Offset(0,10, "bsp_3", AnimalScript.DIRECTION_SW), 
		    		"Wie hoch ist die Gesamtwahrscheinlichkeit, dass jemand, der in irgendeinem dieser Restaurants essen geht, zufrieden sein wird?",
		    		"bsp_4", null, boldTextProps);		
		    lang.nextStep("Berechnung");

		    // Font aendern
		    schritt_1.changeColor("color",HighlightColor, null, null);
		    schritt_1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16), Timing.MEDIUM, Timing.FAST);
		    lang.nextStep();
		    
		    // description for drawing graph
		    algoanim.primitives.Text graph_desc_1 = lang.newText(new Offset(710,90, "bsp_4", AnimalScript.DIRECTION_SW), 
		    		"Wir nützen hier das Baumdiagramm, um die mehrstufigen Ereignisse darzustellen. ",
		    		"graph_desc_1", null, normalTextProps);		
		    algoanim.primitives.Text graph_desc_2 = lang.newText(new Offset(0,10, "graph_desc_1", AnimalScript.DIRECTION_SW), 
		    		"In der ersten Stufe werden die verschiedene Ergebnisse  ",
		    		"graph_desc_2", null, normalTextProps);		
		    algoanim.primitives.Text graph_desc_3 = lang.newText(new Offset(0,10, "graph_desc_2", AnimalScript.DIRECTION_SW), 
		    		"der Restaurantauswahl als Zweige dargestellt. ",
		    		"graph_desc_3", null, normalTextProps);		
		    algoanim.primitives.Text graph_desc_4 = lang.newText(new Offset(0,10, "graph_desc_3", AnimalScript.DIRECTION_SW), 
		    		"In der zweiten Stufe werden die verschiedene Ereignisse ",
		    		"graph_desc_4", null, normalTextProps);		
		    algoanim.primitives.Text graph_desc_5 = lang.newText(new Offset(0,10, "graph_desc_4", AnimalScript.DIRECTION_SW), 
		    		"der Zufriedenheit dargestellt d.h. S = Zufrieden und S_C = Unzufrieden. ",
		    		"graph_desc_5", null, normalTextProps);		
		   
		    lang.nextStep();
		    // start drawing the graph
		    // first level lines
		    Node[] lineCoord = new Node[2];
		    lineCoord[0] = new Offset(20, 180, bsp_4, AnimalScript.DIRECTION_SW);
		    lineCoord[1] = new Offset(180,120, bsp_4, AnimalScript.DIRECTION_SW);		    
		    Polyline line1 =  lang.newPolyline(lineCoord, "line1", null);
		    lang.nextStep();
		    
		    lineCoord[0] = new Offset(20, 180, bsp_4, AnimalScript.DIRECTION_SW);
		    lineCoord[1] = new Offset(180,180, bsp_4, AnimalScript.DIRECTION_SW);		    		    
		    Polyline line2 =  lang.newPolyline(lineCoord, "line2", null);
		    lang.nextStep();

		    lineCoord[0] = new Offset(20, 180, bsp_4, AnimalScript.DIRECTION_SW);
		    lineCoord[1] = new Offset(180,240, bsp_4, AnimalScript.DIRECTION_SW);		    		    
		    Polyline line3 =  lang.newPolyline(lineCoord, "line3", null);
		    lang.nextStep();
		    
		    // first level captions
		    algoanim.primitives.Text R1 = lang.newText(new Offset(5,-20, "line1", AnimalScript.DIRECTION_NE), 
		    		"R1",
		    		"R1", null, boldTextProps);		
		    algoanim.primitives.Text R2 = lang.newText(new Offset(5,-15, "line2", AnimalScript.DIRECTION_NE), 
		    		"R2",
		    		"R2", null, boldTextProps);		
		    algoanim.primitives.Text R3 = lang.newText(new Offset(5,0, "line3", AnimalScript.DIRECTION_SE), 
		    		"R3",
		    		"R3", null, boldTextProps);		

		    lang.nextStep();
		    
		    // second level lines
		    lineCoord[0] = new Offset(10, 10, R1, AnimalScript.DIRECTION_NE);
		    lineCoord[1] = new Offset(170,-10, R1, AnimalScript.DIRECTION_NE);		    
		    Polyline line11 =  lang.newPolyline(lineCoord, "line11", null);
		    lang.nextStep();
		    
		    lineCoord[0] = new Offset(10, 10, R1, AnimalScript.DIRECTION_NE);
		    lineCoord[1] = new Offset(170,30, R1, AnimalScript.DIRECTION_NE);		    		    
		    Polyline line12 =  lang.newPolyline(lineCoord, "line12", null);
		    lang.nextStep();

		    lineCoord[0] = new Offset(10, 10, R2, AnimalScript.DIRECTION_NE);
		    lineCoord[1] = new Offset(170,-10, R2, AnimalScript.DIRECTION_NE);		    
		    Polyline line21 =  lang.newPolyline(lineCoord, "line21", null);
		    lang.nextStep();
		    
		    lineCoord[0] = new Offset(10, 10, R2, AnimalScript.DIRECTION_NE);
		    lineCoord[1] = new Offset(170,30, R2, AnimalScript.DIRECTION_NE);		    		    
		    Polyline line22 =  lang.newPolyline(lineCoord, "line22", null);
		    lang.nextStep();

		    lineCoord[0] = new Offset(10, 10, R3, AnimalScript.DIRECTION_NE);
		    lineCoord[1] = new Offset(170,-10, R3, AnimalScript.DIRECTION_NE);		    
		    Polyline line31 =  lang.newPolyline(lineCoord, "line31", null);
		    lang.nextStep();
		    
		    lineCoord[0] = new Offset(10, 10, R3, AnimalScript.DIRECTION_NE);
		    lineCoord[1] = new Offset(170,30, R3, AnimalScript.DIRECTION_NE);		    		    
		    Polyline line32 =  lang.newPolyline(lineCoord, "line32", null);
		    lang.nextStep();

		    // second level captions
		    algoanim.primitives.Text S11 = lang.newText(new Offset(5,-10, "line11", AnimalScript.DIRECTION_NE), 
		    		"S",
		    		"S11", null, boldTextProps);		
		    algoanim.primitives.Text S12 = lang.newText(new Offset(5,-10, "line12", AnimalScript.DIRECTION_SE), 
		    		"S_C",
		    		"S12", null, boldTextProps);		
		    algoanim.primitives.Text S21 = lang.newText(new Offset(5,-10, "line21", AnimalScript.DIRECTION_NE), 
		    		"S",
		    		"S21", null, boldTextProps);		
		    algoanim.primitives.Text S22 = lang.newText(new Offset(5,-10, "line22", AnimalScript.DIRECTION_SE), 
		    		"S_C",
		    		"S22", null, boldTextProps);		
		    algoanim.primitives.Text S31 = lang.newText(new Offset(5,-10, "line31", AnimalScript.DIRECTION_NE), 
		    		"S",
		    		"S31", null, boldTextProps);		
		    algoanim.primitives.Text S32 = lang.newText(new Offset(5,-10, "line32", AnimalScript.DIRECTION_SE), 
		    		"S_C",
		    		"S32", null, boldTextProps);		
		    lang.nextStep();

		    // level titles
		    algoanim.primitives.Text Restaurant = lang.newText(new Offset(-60,-50, "R1", AnimalScript.DIRECTION_NE), 
		    		"Restaurant",
		    		"Restaurant", null, boldTextProps);		
		    algoanim.primitives.Text Zufriedenheit = lang.newText(new Offset(120,0, "Restaurant", AnimalScript.DIRECTION_NE), 
		    		"Zufriedenheit",
		    		"Zufriedenheit", null, boldTextProps);		
		    lang.nextStep();
		    
		    // erste Frage
		    MultipleChoiceQuestionModel mcq1 = new MultipleChoiceQuestionModel("m1");
		    mcq1.setPrompt("Welche Zahlen werden auf der ersten Ebene eingetragen");
		    mcq1.addAnswer(R1_S_W + ";" + R2_S_W + ";" + R3_S_W, 0, "Falsch. In erster Ebene werden die Zahlen für den Umsatz der Restaurants eingetragen.");
		    mcq1.addAnswer(R1_W + ";" + R2_W + ";" + R3_W ,1,"Richtig");
		    lang.addMCQuestion(mcq1);
		    
		    // Wahrscheinlicheiten eintragen
		    schritt_1.changeColor("color", Color.black, null, null);
		    schritt_1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16), Timing.FAST, Timing.FAST);
		    schritt_2.changeColor("color", HighlightColor, null, null);
		    schritt_2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16), Timing.FAST, Timing.FAST);
		    lang.nextStep();

		    algoanim.primitives.Text R1_W_T = lang.newText(new Offset(70,-60, "line1", AnimalScript.DIRECTION_SW), 
		    		Double.toString(R1_W),
		    		"R1_W_T", null, boldTextProps);		
		    algoanim.primitives.Text R2_W_T = lang.newText(new Offset(70,-20, "line2", AnimalScript.DIRECTION_SW), 
		    		Double.toString(R2_W),
		    		"R2_W_T", null, boldTextProps);		
		    algoanim.primitives.Text R3_W_T = lang.newText(new Offset(70,-20, "line3", AnimalScript.DIRECTION_SW), 
		    		Double.toString(R3_W),
		    		"R3_W_T", null, boldTextProps);		
		    lang.nextStep();

		    algoanim.primitives.Text R1_S_W_T = lang.newText(new Offset(70,-10, "line11", AnimalScript.DIRECTION_NW), 
		    		Double.toString(R1_S_W),
		    		"R1_W_T", null, boldTextProps);		
		    algoanim.primitives.Text R1_S_C_W_T = lang.newText(new Offset(70,10, "line12", AnimalScript.DIRECTION_NW), 
		    		Double.toString(Math.round((1 - R1_S_W)*10)/10.0 ),
		    		"R1_W_T", null, boldTextProps);		
		    algoanim.primitives.Text R2_S_W_T = lang.newText(new Offset(70,-10, "line21", AnimalScript.DIRECTION_NW), 
		    		Double.toString(R2_S_W),
		    		"R1_W_T", null, boldTextProps);		
		    algoanim.primitives.Text R2_S_C_W_T = lang.newText(new Offset(70,10, "line22", AnimalScript.DIRECTION_NW), 
		    		Double.toString(Math.round((1 - R2_S_W)*10)/10.0 ),
		    		"R1_W_T", null, boldTextProps);		
		    algoanim.primitives.Text R3_S_W_T = lang.newText(new Offset(70,-10, "line31", AnimalScript.DIRECTION_NW), 
		    		Double.toString(R3_S_W),
		    		"R1_W_T", null, boldTextProps);		
		    algoanim.primitives.Text R3_S_C_W_T = lang.newText(new Offset(70,10, "line32", AnimalScript.DIRECTION_NW), 
		    		Double.toString(Math.round((1 - R3_S_W)*10)/10.0 ),
		    		"R1_W_T", null, boldTextProps);		
		    lang.nextStep();
		    
		    // zweite Frage
		    MultipleChoiceQuestionModel mcq2 = new MultipleChoiceQuestionModel("m2");
		    mcq2.setPrompt("Wie wird der Schnitt der Wahrscheinlichkeiten (P(R1 ∩ S)) der zwei Ebenen berechnet?");
		    mcq2.addAnswer("Die Wahrscheinlichkeiten werden miteinander multipliziert",1,"Richtig");
		    mcq2.addAnswer("Die Wahrscheinlichkeiten werden miteinander addiert", 0, "Falsch. Der Schnitt zweier unabhängige Ereignisse R1 und S ist der Produkt der Ereignisse.");
		    lang.addMCQuestion(mcq2);

		    // Summe ausrechnen
		    // erst Pfeil dann die Summe
		    PolylineProperties    forwardPfeil           = new PolylineProperties();
		    forwardPfeil.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		    lineCoord[0] = new Offset(50, 0, line11, AnimalScript.DIRECTION_NE);
		    lineCoord[1] = new Offset(160,0, line11, AnimalScript.DIRECTION_NE);		    
		    Polyline Pfeil11 =  lang.newPolyline(lineCoord, "Pfeil11", null, forwardPfeil);
		    lang.nextStep();
		    algoanim.primitives.Text W11 = lang.newText(new Offset(5,-10, "Pfeil11", AnimalScript.DIRECTION_NE), 
		    		"P(R1 ∩ S) = " + Double.toString(Math.round(R1_W*R1_S_W*100)/100.0),
		    		"S11", null, boldTextProps);		
		    lang.nextStep();
		    
		    lineCoord[0] = new Offset(50, 0, line12, AnimalScript.DIRECTION_SE);
		    lineCoord[1] = new Offset(160,0, line12, AnimalScript.DIRECTION_SE);		    
		    Polyline Pfeil12 =  lang.newPolyline(lineCoord, "Pfeil12", null, forwardPfeil);
		    lang.nextStep();
		    algoanim.primitives.Text W12 = lang.newText(new Offset(5,-10, "Pfeil12", AnimalScript.DIRECTION_NE), 
		    		"P(R1 ∩ S_C) = " + Double.toString(Math.round(R1_W*(1-R1_S_W)*100)/100.0),
		    		"S12", null, boldTextProps);		
		    lang.nextStep();
		    
		    lineCoord[0] = new Offset(50, 0, line21, AnimalScript.DIRECTION_NE);
		    lineCoord[1] = new Offset(160,0, line21, AnimalScript.DIRECTION_NE);		    
		    Polyline Pfeil21 =  lang.newPolyline(lineCoord, "Pfeil21", null, forwardPfeil);
		    lang.nextStep();
		    algoanim.primitives.Text W21 = lang.newText(new Offset(5,-10, "Pfeil21", AnimalScript.DIRECTION_NE), 
		    		"P(R2 ∩ S) = " + Double.toString(Math.round(R2_W*R2_S_W*100)/100.0),
		    		"S21", null, boldTextProps);
		    lang.nextStep();

		    lineCoord[0] = new Offset(50, 0, line22, AnimalScript.DIRECTION_SE);
		    lineCoord[1] = new Offset(160,0, line22, AnimalScript.DIRECTION_SE);		    
		    Polyline Pfeil22 =  lang.newPolyline(lineCoord, "Pfeil22", null, forwardPfeil);
		    lang.nextStep();
		    algoanim.primitives.Text W22 = lang.newText(new Offset(5,-10, "Pfeil22", AnimalScript.DIRECTION_NE), 
		    		"P(R2 ∩ S_C) = " + Double.toString(Math.round(R2_W*(1-R2_S_W)*100)/100.0),
		    		"S22", null, boldTextProps);		
		    lang.nextStep();

		    lineCoord[0] = new Offset(50, 0, line31, AnimalScript.DIRECTION_NE);
		    lineCoord[1] = new Offset(160,0, line31, AnimalScript.DIRECTION_NE);		    
		    Polyline Pfeil31 =  lang.newPolyline(lineCoord, "Pfeil31", null, forwardPfeil);
		    lang.nextStep();
		    algoanim.primitives.Text W31 = lang.newText(new Offset(5,-10, "Pfeil31", AnimalScript.DIRECTION_NE), 
		    		"P(R3 ∩ S) = " + Double.toString(Math.round(R3_W*R3_S_W*100)/100.0),
		    		"S31", null, boldTextProps);
		    lang.nextStep();

		    lineCoord[0] = new Offset(50, 0, line32, AnimalScript.DIRECTION_SE);
		    lineCoord[1] = new Offset(160,0, line32, AnimalScript.DIRECTION_SE);		    
		    Polyline Pfeil32 =  lang.newPolyline(lineCoord, "Pfeil32", null, forwardPfeil);
		    lang.nextStep();
		    algoanim.primitives.Text W32 = lang.newText(new Offset(5,-10, "Pfeil32", AnimalScript.DIRECTION_NE), 
		    		"P(R3 ∩ S_C) = " + Double.toString(Math.round(R3_W*(1-R3_S_W)*100)/100.0),
		    		"S32", null, boldTextProps);		
		    
		    lang.nextStep();
		    // zweite Frage
		    MultipleChoiceQuestionModel mcq3 = new MultipleChoiceQuestionModel("m3");
		    // Wie hoch ist die Gesamtwahrscheinlichkeit, dass jemand, der in irgendeinem dieser Restaurans essen geht, zufrieden sein wird?
		    mcq3.setPrompt("Wie wird die Gesamtwahrscheinlichkeit, die in der Hauptfrage gefordert wird, ausgerechnet?");
		    mcq3.addAnswer("P(R1 ∩ S_C) + P(R2 ∩ S_C) + P(R3 ∩ S_C)", 0, "Falsch. Die Gesamtwahrscheinlichkeit für S ist die Vereinigung der Zweige für S, da sie kein gemeinsames Element haben d.h. die Summe der Wahscheinlichkeiten");
		    mcq3.addAnswer("P(R1 ∩ S_C) * P(R2 ∩ S_C) * P(R3 ∩ S_C)", 0, "Falsch. Die Gesamtwahrscheinlichkeit für S ist die Vereinigung der Zweige für S, da sie kein gemeinsames Element haben d.h. die Summe der Wahscheinlichkeiten");
		    mcq3.addAnswer("P(R1 ∩ S) + P(R2 ∩ S) + P(R3 ∩ S)",1,"Richtig");
		    mcq3.addAnswer("P(R1 ∩ S) * P(R2 ∩ S) * P(R3 ∩ S)",0,"Falsch. Die Gesamtwahrscheinlichkeit für S ist die Vereinigung der Zweige für S, da sie kein gemeinsames Element haben d.h. die Summe der Wahscheinlichkeiten");
		    lang.addMCQuestion(mcq3);
		    lang.nextStep();
		    
		    schritt_2.changeColor("color", Color.black, null, null);
		    schritt_2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16), Timing.FAST, Timing.FAST);
		    schritt_3.changeColor("color", HighlightColor, null, null);
		    schritt_3.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16), Timing.FAST, Timing.FAST);

		    lang.nextStep();
		    lineCoord[0] = new Offset(0, -2, bsp_4, AnimalScript.DIRECTION_SW);
		    lineCoord[1] = new Offset(960,-2, bsp_4, AnimalScript.DIRECTION_SW);		    		    
		    Polyline bsp_4_underline =  lang.newPolyline(lineCoord, "bsp_4_underline", null);
		    lang.nextStep();

		    algoanim.primitives.Text Gesamt_Text = lang.newText(new Offset(20, 300, bsp_4, AnimalScript.DIRECTION_SW), 
		    		"Der Kunde besucht R1 und ist zufrieden + Der Kunde besucht R2 und ist zufrieden + Der Kunde besucht R3 und ist zufrieden",
		    		"Gesamt_Text", null, boldTextProps);		
		    lang.nextStep();

		    algoanim.primitives.Text Gesamt_Text2 = lang.newText(new Offset(0, 10, Gesamt_Text, AnimalScript.DIRECTION_SW), 
		    		" =  P(R1 ∩ S) + P(R2 ∩ S) + P(R3 ∩ S)",
		    		"Gesamt_Text2", null, boldTextProps);		
		    lang.nextStep();

		    algoanim.primitives.Text Gesamt_Text3 = lang.newText(new Offset(10, 0, Gesamt_Text2, AnimalScript.DIRECTION_NE), 
		    		"= " + Double.toString(Math.round(R1_W*R1_S_W*100)/100.0) + " + "  + Double.toString(Math.round(R2_W*R2_S_W*100)/100.0) + " + " + Double.toString(Math.round(R3_W*R3_S_W*100)/100.0),
		    		"Gesamt_Text3", null, boldTextProps);		
		    lang.nextStep();

		    algoanim.primitives.Text Gesamt_Text4 = lang.newText(new Offset(10, 0, Gesamt_Text3, AnimalScript.DIRECTION_NE), 
		    		"= " + Double.toString(Math.round((Math.round(R1_W*R1_S_W*100)/100.0 + Math.round(R2_W*R2_S_W*100)/100.0 + Math.round(R3_W*R3_S_W*100)/100.0)*100.0)/100.0),
		    		"Gesamt_Text4", null, boldTextProps);		
		    lang.nextStep();
		    
		    schritt_3.changeColor("color", Color.black, null, null);
		    schritt_3.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16), Timing.FAST, Timing.FAST);

		    lang.nextStep();

		    // End result 
		    schritt_1.hide();
		    schritt_2.hide();
		    schritt_3.hide();
		    text_9.hide();
		    bsp_1.moveBy(null, 0, -120, Timing.FAST, Timing.MEDIUM);
		    bsp_2.moveBy(null, 0, -120, Timing.FAST, Timing.MEDIUM);
		    bsp_3.moveBy(null, 0, -120, Timing.FAST, Timing.MEDIUM);
		    bsp_4.moveBy(null, 0, -120, Timing.FAST, Timing.MEDIUM);
		    bsp_4_underline.moveBy(null, 0, -120, Timing.FAST, Timing.MEDIUM);
		    
		    lang.nextStep();
		    algoanim.primitives.Text EndResult = lang.newText(new Offset(130, 180, formel, AnimalScript.DIRECTION_NE), 
		    		Double.toString( Math.round((Math.round(R1_W*R1_S_W*100)/100.0 + Math.round(R2_W*R2_S_W*100)/100.0 + Math.round(R3_W*R3_S_W*100)/100.0)*100)) + "%",
		    		"EndResult", null, boldTextProps);		
		    lang.nextStep();
		    
		    lineCoord[0] = new Offset(0, -2, EndResult, AnimalScript.DIRECTION_SW);
		    lineCoord[1] = new Offset(50,-2, EndResult, AnimalScript.DIRECTION_SW);		    		    
		    Polyline EndResult_underline =  lang.newPolyline(lineCoord, "EndResult_underline", null);
		    lang.nextStep();		    
		    
		    // Summary
		    // first hide all other objects
		    formel.hide();
		    bsp_1.hide();bsp_2.hide();bsp_3.hide();bsp_4.hide();
		    bsp_4_underline.hide();
		    EndResult.hide();
		    EndResult_underline.hide();
		    line1.hide();line2.hide();line3.hide();
		    line11.hide();line12.hide();line21.hide();line22.hide();line31.hide();line32.hide();
		    R1.hide();R2.hide();R3.hide();
		    R1_W_T.hide();R1_S_C_W_T.hide();R1_S_W_T.hide();
		    R2_W_T.hide();R2_S_C_W_T.hide();R2_S_W_T.hide();
		    R3_W_T.hide();R3_S_C_W_T.hide();R3_S_W_T.hide();
		    Pfeil11.hide();Pfeil12.hide();Pfeil21.hide();Pfeil22.hide();Pfeil31.hide();Pfeil32.hide();
		    W11.hide();W12.hide();W22.hide();W21.hide();W31.hide();W32.hide();
		    S11.hide();S12.hide();S21.hide();S22.hide();S31.hide();S32.hide();
		    Restaurant.hide();Zufriedenheit.hide();
		    Gesamt_Text.hide();Gesamt_Text2.hide();Gesamt_Text3.hide();Gesamt_Text4.hide();
		    graph_desc_1.hide();graph_desc_2.hide();graph_desc_3.hide();graph_desc_4.hide();graph_desc_5.hide();
		    header.hide();

		    // now the text
		    lang.newText(new Coordinates(20, 30), "Zusammenfassung", "Sum",
			        null, headerProps);
		    
		    algoanim.primitives.Text sum_1 = lang.newText(new Offset(0,30, "header", AnimalScript.DIRECTION_SW),
		    		"Wir haben gesehen, dass die totale Wahrscheinlichkeit anhand Baumdiagramme berechnet werden kann. Wenn man zum Beispiel",
		    		"sum_1", null, normalTextProps);
		    algoanim.primitives.Text sum_2 = lang.newText(new Offset(0,10, "sum_1", AnimalScript.DIRECTION_SW),
		    		"die totale Wahrscheinlichkeit von B haben willen, addiert man die Wahrscheinlichkeit aller Verzweigungspfade, die zu dem ",
		    		"sum_2", null, normalTextProps);
		    algoanim.primitives.Text sum_3 = lang.newText(new Offset(0,10, "sum_2", AnimalScript.DIRECTION_SW),
		    		"Ereignis B führen. Hier helfen Baumdiagramme besonders zum Verständnis des Problems. Allgemein sind Baumdiagramme am besten ",
		    		"sum_3", null, normalTextProps);
		    algoanim.primitives.Text sum_4 = lang.newText(new Offset(0,10, "sum_3", AnimalScript.DIRECTION_SW),
		    		"für Probleme geeignet, in denen die Wahrscheinlichkeit durch eine Reihe von Schritten oder eine Folge von Ereignisse zustande kommen.",
		    		"sum_4", null, normalTextProps);
		    algoanim.primitives.Text sum_5 = lang.newText(new Offset(0,10, "sum_4", AnimalScript.DIRECTION_SW),
		    		"In solchen Fällen enthalten Probleme normalerweise die Wahrscheinlichkeiten der Stufe-eins-Ereignisse und die bedingten ",
		    		"sum_5", null, normalTextProps);
		    algoanim.primitives.Text sum_6 = lang.newText(new Offset(0,10, "sum_5", AnimalScript.DIRECTION_SW),
		    		"Wahrscheinlichkeiten der Stufe-zwei-Ereignisse unter der Bedingung der Stufe-eins-Ergebnisse. So kann man alle Ergebnisse ",
		    		"sum_6", null, normalTextProps);
		    algoanim.primitives.Text sum_7 = lang.newText(new Offset(0,10, "sum_6", AnimalScript.DIRECTION_SW),
		    		"in einem Baumdiagramm ordnen und darstellen, was hilft, die Wahrscheinlichkeiten jedes Verzweigungspfads oder Kombination ",
		    		"sum_7", null, normalTextProps);
		    algoanim.primitives.Text sum_8 = lang.newText(new Offset(0,10, "sum_7", AnimalScript.DIRECTION_SW),
		    		"von Verzweigungspfaden zu berechnen.",
		    		"sum_8", null, normalTextProps);
		    algoanim.primitives.Text sum_9 = lang.newText(new Offset(0,10, "sum_8", AnimalScript.DIRECTION_SW),
		    		"Baumdiagramme kommen an ihre Grenzen, wenn die Probleme nicht bedingte, sondern Wahrscheinlichkeiten von Durchschnitten betreffen. ",
		    		"sum_9", null, normalTextProps);
		    algoanim.primitives.Text sum_10 = lang.newText(new Offset(0,10, "sum_9", AnimalScript.DIRECTION_SW),
		    		"Sie helfen auch nicht, wenn Sie Ihren Strichprobenraum nicht in eine Reihe von Schritten oder eine Folge von Ereignissen zerlegen können. ",
		    		"sum_10", null, normalTextProps);
		    algoanim.primitives.Text sum_11 = lang.newText(new Offset(0,10, "sum_10", AnimalScript.DIRECTION_SW),
		    		"In dem vorgeführten Beispiel war Baumdiagramm sehr geeignet, da der Stichprobenraum in Schritte zerlegbar war und die totale Wahrscheinlichkeit ",
		    		"sum_11", null, normalTextProps);
		    algoanim.primitives.Text sum_12 = lang.newText(new Offset(0,10, "sum_11", AnimalScript.DIRECTION_SW),
		    		"eines Stufe-zwei-Ereignisses gesucht war (Zufriedenheit der Kunden unabhängig vom Restaurant).",
		    		"sum_12", null, normalTextProps);
		    algoanim.primitives.Text sum_13 = lang.newText(new Offset(0,30, "sum_12", AnimalScript.DIRECTION_SW),
		    		"Quellen: Deborah Rumsey, „Wahrscheinlichkeitsrechnung für DUMMIES‟",
		    		"sum_13", null, normalTextProps);
		    lang.nextStep();

	  }
	  
	  
	


		public void init() {
	        lang = new AnimalScript("Totale Wahrscheinlichkeit", "Mohammad Braei", 800, 600);
		    lang.setStepMode(true);
		    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		}
		
	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        R1_W = (Double)primitives.get("Restaurant1_Umsatz");
        R2_W = (Double)primitives.get("Restaurant2_Umsatz");
        R3_W = (Double)primitives.get("Restaurant3_Umsatz");
        R1_S_W = (Double)primitives.get("Restaurant1_Zufriedenheit");
        R2_S_W = (Double)primitives.get("Restaurant2_Zufriedenheit");
        R3_S_W = (Double)primitives.get("Restaurant3_Zufriedenheit");
        HighlightColor = (Color)primitives.get("HighlightColor");
        render();
        lang.finalizeGeneration();
        return lang.toString();
	}

	public String getAlgorithmName() {
		// TODO Auto-generated method stub
        return "Totale Wahrscheinlichkeit";
	}

	public String getAnimationAuthor() {
        return "Mohammad Braei";
	}

	public Locale getContentLocale() {
        return Locale.GERMAN;
	}

	public String getFileExtension() {
        return "asu";
	}

	public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
	}


	public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable arg1) throws IllegalArgumentException {
        R1_W = (Double)arg1.get("Restaurant1_Umsatz");
        R2_W = (Double)arg1.get("Restaurant2_Umsatz");
        R3_W = (Double)arg1.get("Restaurant3_Umsatz");
        R1_S_W = (Double)arg1.get("Restaurant1_Zufriedenheit");
        R2_S_W = (Double)arg1.get("Restaurant2_Zufriedenheit");
        R3_S_W = (Double)arg1.get("Restaurant3_Zufriedenheit");


        double delta = 0.00000001;
        
        if ((R1_W + R2_W + R3_W) - 1 > delta)
        {
			JOptionPane.showMessageDialog(null, "Die Summe der Umsatzwahrscheinlichkeiten der Restaurants sind nicht 1", "Falsche Umsatzwahrscheinlichkeiten", JOptionPane.WARNING_MESSAGE);
			return false;
		}
        if (R1_S_W > 1)
        {
			JOptionPane.showMessageDialog(null, "Die Zufriedenheitswahrscheinlichkeit des Restaurant 1 darf nicht mehr als 1.0 betragen", "Falsche Zufriedenheitswahrscheinlichkeit", JOptionPane.WARNING_MESSAGE);
			return false;
		}
        if (R2_S_W > 1)
        {
			JOptionPane.showMessageDialog(null, "Die Zufriedenheitswahrscheinlichkeit des Restaurant 2 darf nicht mehr als 1.0 betragen", "Falsche Zufriedenheitswahrscheinlichkeit", JOptionPane.WARNING_MESSAGE);
			return false;
		}
        if (R3_S_W > 1)
        {
			JOptionPane.showMessageDialog(null, "Die Zufriedenheitswahrscheinlichkeit des Restaurant 3 darf nicht mehr als 1.0 betragen", "Falsche Zufriedenheitswahrscheinlichkeit", JOptionPane.WARNING_MESSAGE);
			return false;
		}
        // TODO Auto-generated method stub
		
		return true;
	}
	}


