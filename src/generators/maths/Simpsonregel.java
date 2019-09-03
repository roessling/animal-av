/*
 * Simpsonregel.java
 * Yasin Kaymak, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.maths;
import generators.maths.DoubleFunction;

import generators.framework.Generator;
import generators.framework.GeneratorType;
//import generators.framework.ValidatingGenerator;






import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.text.DecimalFormat;
import java.util.Locale;

import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;



import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class Simpsonregel implements Generator{
	
	public static Language lang;
    public static Simpsonregel s;	

    
    
    //XML parameter
    public static double a;	
    public static double b;	
    public static int N_must_be_Even;
    
    //XML Props
    public static CircleProperties smileyProps;
    
    
    
    
    //Java parametisierbar, nicht XML, da DoubleFunction kein primitiv ist und nicht übernommen werden kann
    
    public static DoubleFunction Funktion = (double x) -> 1 / (x + 3);	
    
    public static String zaehler = "1";				
	public static String nenner = "x+3";				
	
	
	public static String zaehlerabl4 = "24";		
	public static String nennerabl4 = "(x+3)^5";	
	public static Double Betragsmaximum = 0.75;		

	//Parameter -> das wars, der Rest erstellt sich automatisch!
 
	
	public static TextProperties textProps;
	public static Rect hRect;
	public static Text header;
	public static Timing start = new TicksTiming(0);
	public static Timing defaultDuration = new TicksTiming(300);
    
	
    public void init(){
        lang = new AnimalScript("Simpsonregel", "Yasin Kaymak", 800, 600);
        lang.setStepMode(true);
    }

    
    
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        smileyProps = (CircleProperties)props.getPropertiesByName("smileyProps");
        a = (double)primitives.get("a");
        b = (double)primitives.get("b");
        N_must_be_Even = (Integer)primitives.get("N_must_be_Even");
        
        init();
        if(N_must_be_Even > 0 && N_must_be_Even%2==0){
        s = new Simpsonregel();
        
        
        Simpsonregel.Simpsonalgo();
        return lang.toString();
        }
     
        return null;	//eine andere Möglichkeit als ValidatingGenerator
    }

    
    
    
    
    
    //Algo Beginn
    public static void Simpsonalgo() {


		//HEADER
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 30));
		header = lang.newText(new Coordinates(20, 30), "Simpsonregel",
				"header", null, headerProps);
		RectProperties rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
		rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		hRect = lang.newRect(new Offset(-5, -5, "header",
				AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"),
				"hRect", null, rectProps);
		
		
		lang.nextStep();	//Schritt: HEADER anzeigen 

		
		
		
		
		
		
		// Gliederung-HEADER
		Text header2;
		TextProperties header2Props = new TextProperties();
		header2Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 16));
		header2 = lang.newText(
				new Coordinates(((Coordinates) header.getUpperLeft()).getX(),
						((Coordinates) header.getUpperLeft()).getY() + 50),
				"Gliederung", "header", null, header2Props);

		
		
		//Gliederung-TEXT
		SourceCodeProperties structurProps = new SourceCodeProperties();
		structurProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.DIALOG, Font.BOLD, 16));
		SourceCode Structurtext = lang.newSourceCode(new Coordinates(40, 100),
				"InfoText", null, structurProps);

		Structurtext.addCodeLine("", null, 0, null);
		Structurtext.addCodeLine("", null, 0, null);
		Structurtext.addCodeLine("", null, 0, null);
		Structurtext.addCodeLine("", null, 0, null);
		Structurtext.addCodeLine("Folie:    3 -    5:", null, 1, null);
		Structurtext.addCodeLine("", null, 0, null);
		Structurtext.addCodeLine("", null, 0, null);
		Structurtext.addCodeLine("Folie:    6 -    9:", null, 1, null);
		Structurtext.addCodeLine("", null, 0, null);
		Structurtext.addCodeLine("", null, 0, null);
		Structurtext.addCodeLine("Folie:   10 - "+(10+5+3+(7+  (N_must_be_Even/2-1)*6 + (N_must_be_Even/2-1)*5)+6), null, 1, null);
		Structurtext.addCodeLine("", null, 0, null);
		Structurtext.addCodeLine("", null, 0, null);
		Structurtext.addCodeLine("Folie:   "+((10+5+3+(7+  (N_must_be_Even/2-1)*6 + (N_must_be_Even/2-1)*5)+6)+1)+" - "+((10+5+3+(7+  (N_must_be_Even/2-1)*6 + (N_must_be_Even/2-1)*5)+6)+3), null, 1, null);
		Structurtext.addCodeLine("", null, 0, null);
		Structurtext.addCodeLine("", null, 0, null);
		Structurtext.addCodeLine("Folie:   "+((10+5+3+(7+  (N_must_be_Even/2-1)*6 + (N_must_be_Even/2-1)*5)+6)+4), null, 1, null);

		SourceCode Structurtext2 = lang.newSourceCode(
				new Coordinates(240, 100), "InfoText", null, structurProps);

		Structurtext2.addCodeLine("", null, 0, null);
		Structurtext2.addCodeLine("", null, 0, null);
		Structurtext2.addCodeLine("", null, 0, null);
		Structurtext2.addCodeLine("", null, 0, null);
		Structurtext2.addCodeLine("Sinn der Simpsonregel", null, 1, null);
		Structurtext2.addCodeLine("", null, 0, null);
		Structurtext2.addCodeLine("", null, 0, null);
		Structurtext2.addCodeLine("Definition und Formel der Simpsonregel",
				null, 1, null);
		Structurtext2.addCodeLine("", null, 0, null);
		Structurtext2.addCodeLine("", null, 0, null);
		Structurtext2.addCodeLine("Algorithmus anhang Pseudocode", null, 1,
				null);
		Structurtext2.addCodeLine("", null, 0, null);
		Structurtext2.addCodeLine("", null, 0, null);
		Structurtext2.addCodeLine("Abschaetzung des Fehlers", null, 1, null);
		Structurtext2.addCodeLine("", null, 0, null);
		Structurtext2.addCodeLine("", null, 0, null);
		Structurtext2.addCodeLine(
				"Lernziele: Was haben Sie erlernt", null, 1, null);
		
		
		lang.nextStep(); //Schritt: Gliederung zeigen
		
		
		

		
		
		//Sinn-Simpsonregel
		Structurtext.hide();
		Structurtext2.hide();
		header2.setText("Sinn der Simpsonregel?", null, null);	//2nd HEADER

		
		SourceCodeProperties InfoProps = new SourceCodeProperties();
		InfoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.DIALOG, Font.PLAIN, 15));
		SourceCode infoText = lang.newSourceCode(new Coordinates(40, 140),
				"InfoText", null, InfoProps);

		infoText.addCodeLine(
				"Bestimmen sie den Integral fuer die folgende Funktion:", null,
				0, null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine(
				"           e ",
				null, 0, null);
		infoText.addCodeLine("                ln(x) * e  - sin(x) * tan(x ) + tan(e  + cos(x))", null, 0, null);
		infoText.addCodeLine(
				"f(x) =",
				null, 0, null);
		PolylineProperties lineProps = new PolylineProperties();
		
		Polyline Bruchstrich = lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(110, 107)),
				algoanim.util.Node.convertToNode(new Point(380, 107)) }, "inst",
				null, lineProps);
		Bruchstrich.moveTo(null, null, new Coordinates(105, 259), null, null);
		
		//da Animal bestimmte Sonderzeichen nicht unterstütz mussten einige mathematische Symbole irgendwie dargestellt werden
		Polyline Integral1 = lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(355, 90)),
				algoanim.util.Node.convertToNode(new Point(355, 125)) }, "ina",
				null, lineProps);
		Polyline Integral2 =  lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(355, 125)),
				algoanim.util.Node.convertToNode(new Point(350, 125)) }, "inb",
				null, lineProps);
		
		Polyline Integral3 =  lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(355, 90)),
				algoanim.util.Node.convertToNode(new Point(360, 90)) }, "inc",
				null, lineProps);
		
		Integral1.moveTo(null, null, new Coordinates(85, 245), null, null);
		Integral2.moveTo(null, null, new Coordinates(85, 245), null, null);
		Integral3.moveTo(null, null, new Coordinates(80, 280), null, null);//-5 +35
		
		infoText.addCodeLine("                10 + 20x - tan(x ) * x * sin(x) * cos(x) * ln(x) ", null, 0, null);
		infoText.addCodeLine(
				"         -e    ",
				null, 0, null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("", null, 0, null);
		
		//auch Exponenten z.B ^x
		TextProperties expoProps = new TextProperties();
		expoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.DIALOG, Font.PLAIN, 9));
		
		Text expo1 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 135,
						((Coordinates) header.getUpperLeft()).getY() + 191),
				"x", "hochx", null,
				expoProps);
		
		Text expo2 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 292,
						((Coordinates) header.getUpperLeft()).getY() + 199),
				"x", "hochx2", null,
				expoProps);
		
		Text expo3 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 233,
						((Coordinates) header.getUpperLeft()).getY() + 199),
				"7", "hoch7", null,
				expoProps);
		
		Text expo4 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 185,
						((Coordinates) header.getUpperLeft()).getY() + 237),
				"6", "hoch6", null,
				expoProps);
		
		Text expo5 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 73,
						((Coordinates) header.getUpperLeft()).getY() + 177),
				"4", "hoch4-1", null,
				expoProps);
		
		Text expo6 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 73,
						((Coordinates) header.getUpperLeft()).getY() + 255),
				"4", "hoch4-2", null,
				expoProps);
		
		
		
		
		lang.nextStep(); //Schritt: Sinn Simpsonregel-1

		
		
		
		
		
		

		//mein Smiley :)
		CircleProperties eyesProps = new CircleProperties();
		eyesProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		eyesProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		eyesProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		Circle smiley = lang.newCircle(new Coordinates(600, 200), 160,
				"smiley", null, smileyProps);
		Circle eye1 = lang.newCircle(new Coordinates(540, 170), 30, "eye1",
				null, eyesProps);
		Circle eye2 = lang.newCircle(new Coordinates(660, 170), 30, "eye2",
				null, eyesProps);

		PolylineProperties nosemouthProps = new PolylineProperties();
		nosemouthProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		Polyline nose = lang
				.newPolyline(
						new Coordinates[] {
								algoanim.util.Node.convertToNode(new Point(600,
										180)),
								algoanim.util.Node.convertToNode(new Point(600,
										230)) }, "nose", null, nosemouthProps);

		Polyline mouth = lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(520, 280)),
				algoanim.util.Node.convertToNode(new Point(680, 280)) },
				"nose", null, nosemouthProps);



		SourceCodeProperties InfoProps2 = new SourceCodeProperties();
		InfoProps2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.DIALOG, Font.BOLD, 16));
		SourceCode infoText2 = lang.newSourceCode(new Coordinates(420, 85),
				"InfoText", null, InfoProps2);

		infoText2.addCodeLine("                   Viel Spass beim ausrechnen!",
				null, 0, null);
		
		
		
		
		lang.nextStep();	//Schritt: Sinn Simpsonregel-2



		
		
		
		
		//Schlussfolgerungs-Text
		Text header3;
		TextProperties header3Props = new TextProperties();
		header3Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 16));
		header3 = lang
				.newText(
						new Coordinates(((Coordinates) header.getUpperLeft())
								.getX(), ((Coordinates) header.getUpperLeft())
								.getY() + 400),
						"=>  mithilfe der Simpsonregel ist es moeglich solche komplexe Integrale simpel zu loesen",
						"header3", null, header3Props);
		
		
		
		lang.nextStep();	//Schritt: Sinn Simpsonregel-3
		
		lang.hideAllPrimitives();
		header.show();
		hRect.show();
		
		
		
		
		
		
		//Definition Simpsonregel

		header2.setText("Definition der Simpsonregel?", null, null); //2nd HEADER
		header2.show();

		//Definition-Text
		InfoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.DIALOG, Font.PLAIN, 15));
		infoText = lang.newSourceCode(new Coordinates(40, 140), "InfoText",
				null, InfoProps);

		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine(
				"Die Simpsonregel sorgt fuer eine naeherungsweise Berechnung eines Integrals",
				null, 0, null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine(
				"ohne Bestimmung der Stammfunktion, auch genannt numerische Integration",
				null, 0, null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine(
				"Das Loesen schwer integrierender Funktionen wird vereinfacht",
				null, 0, null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine(
				"durch die Annaeherung in eine exakt integrierbare Funktion",
				null, 0, null);
		
		lang.nextStep();// Schritt: Definition Simpsonregel 
		
		
		
		
		
		
		
		//Formel Simpsonregel mit Rechteck umgeben
		infoText.hide();
		header2.setText("Formel der Simpsonregel?", null, null);


		infoText = lang.newSourceCode(new Coordinates(40, 140), "InfoText",
				null, InfoProps);

		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("Hauptformel:", null, 0, null);
		infoText.addCodeLine("", null, 0, null);


		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("", null, 0, null);

		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("", null, 0, null);

		SourceCodeProperties FormelProps = new SourceCodeProperties();
		FormelProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.DIALOG, Font.BOLD, 15));
		SourceCode FormelText = lang.newSourceCode(new Coordinates(40, 140),
				"Formeltext", null, FormelProps);
		FormelText.addCodeLine("", null, 0, null);
		FormelText.addCodeLine("", null, 0, null);
		FormelText.addCodeLine("", null, 0, null);
		FormelText.addCodeLine("          h", null, 1, null);
		FormelText
				.addCodeLine(
						"f(x) =  -   * ( f(x0) + 4 * [ f(x1) + f(x3) + f(x5) + ... ] + 2 * [ f(x2) + f(x4) + ... ] + f(xN) )",
						null, 1, null);
		FormelText.addCodeLine("          3", null, 1, null);
		FormelText.addCodeLine("", null, 0, null);
		FormelText.addCodeLine("", null, 0, null);
		FormelText.addCodeLine("", null, 0, null);

		RectProperties FormelrectProps = new RectProperties();
		FormelrectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		FormelrectProps
				.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
		FormelrectProps
				.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		FormelrectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		Rect FormelRect = lang.newRect(new Offset(-200, -100, "header",
				AnimalScript.DIRECTION_NW),
				new Offset(270, 20, "header", "SE"), "FormelRect", null,
				FormelrectProps);

		FormelRect.moveTo(null, null, new Coordinates(30, 160), null, null);


		lang.nextStep(); //Schritt: Formel Simpsonregel-1

		
		
		
		
		
		//Formel-Tipp
		header3Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 12));
		header3 = lang
				.newText(
						new Coordinates(
								((Coordinates) infoText.getUpperLeft()).getX(),
								((Coordinates) infoText.getUpperLeft()).getY() + 140),
						"=>Die y-Werte der geraden bzw. ungeraden Stuetzstellen mit 2 bzw. 4 multiplizieren (ohne 1. und letzte Stuetzstelle)",
						"header", null, header3Props);

		lang.nextStep(); //Schritt Formel Simpsonregel-2

		
		
		
		
		
		//Simponsregel Zwischenformeln
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("Zwischenformeln:", null, 0, null);
		infoText.addCodeLine("", null, 0, null);


		FormelText.addCodeLine("", null, 0, null);
		FormelText.addCodeLine("", null, 0, null);
		FormelText.addCodeLine("", null, 0, null);
		FormelText.addCodeLine("", null, 0, null);
		FormelText.addCodeLine("        b - a", null, 0, null);
		FormelText.addCodeLine("h =  ", null, 0, null);
		FormelText.addCodeLine("           N", null, 0, null);
		FormelText.addCodeLine("", null, 0, null);
		FormelText.addCodeLine("", null, 0, null);
		FormelText.addCodeLine("x0 = a + 0 * h   <=>    x0 = a", null, 0, null);
		FormelText.addCodeLine("x1 = a + 1 * h", null, 0, null);
		FormelText.addCodeLine("x2 = a + 2 * h", null, 0, null);
		FormelText.addCodeLine(".......      ", null, 0, null);
		FormelText.addCodeLine("xN = a + N * h  <=>    xN = b", null, 0, null);
		
		Polyline Bruchstrich2 = lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(73, 432)),
				algoanim.util.Node.convertToNode(new Point(100, 432)) }, "inst",
				null, lineProps);
		
		
		TextProperties ABProps = new TextProperties();
		ABProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.DIALOG, Font.PLAIN, 10));
		Text header4;
		header4 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 250,
						((Coordinates) header.getUpperLeft()).getY() + 380),
				"a, b := Integrationsgrenzen", "AB", null, ABProps);

		Text header5;

		header5 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 250,
						((Coordinates) header.getUpperLeft()).getY() + 400),
				"N    := Teilintervalle", "AB", null, ABProps);

		lang.nextStep();  //Schritt Formel Simpsonregel-3
		

		
		
		
		//Beispielaufgabe zeigen
		Bruchstrich2.hide();
		FormelText.hide();
		FormelRect.hide();
		header2.setText("Beispielaufgabe", null, null);
		header3.hide();
		header4.hide();
		header5.hide();
		infoText.hide();



		FormelText = lang.newSourceCode(new Coordinates(40, 140), "Formeltext",
				null, FormelProps);
		FormelText.addCodeLine("                   " + (int) b + "           "
				+ zaehler, null, 0, null);
		FormelText.addCodeLine("    f(x)    =               -", null, 0, null);
		FormelText.addCodeLine("                  " + (int) a + "        "
				+ nenner, null, 0, null);
		FormelText.addCodeLine("", null, 1, null); // 4
		FormelText.addCodeLine("", null, 1, null); // 4
		FormelText.addCodeLine("", null, 1, null); // 4
		FormelText.addCodeLine("Teilintervalle: "+N_must_be_Even, null, 1, null); // 4
		
		Integral1 = lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(355, 107)),
				algoanim.util.Node.convertToNode(new Point(355, 123)) }, "ina",
				null, lineProps);
		Integral2 =  lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(355, 123)),
				algoanim.util.Node.convertToNode(new Point(352, 123)) }, "inb",
				null, lineProps);
		
		Integral3 =  lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(355, 90)),
				algoanim.util.Node.convertToNode(new Point(358, 90)) }, "inc",
				null, lineProps);
		
		Integral1.moveTo(null, null, new Coordinates(120, 178), null, null);
		Integral2.moveTo(null, null, new Coordinates(120, 178), null, null);
		Integral3.moveTo(null, null, new Coordinates(117, 194), null, null);//-4 +20
		
		lang.nextStep();

		FormelText.moveTo(null, null, new Coordinates(30, 520), null, null);

		RectProperties FormelrectProps2 = new RectProperties();

		FormelrectProps2.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		FormelrectProps2.set(AnimationPropertiesKeys.FILL_PROPERTY,
				Color.ORANGE);
		FormelrectProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.BLACK);
		FormelrectProps2.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		Rect FormelRect2 = lang.newRect(new Offset(-3, -23, "header",
				AnimalScript.DIRECTION_NW), new Offset(3, 20, "header", "SE"),
				"FormelRect", null, FormelrectProps2);

		FormelRect2.moveTo(null, null, new Coordinates(30, 510), null, null);
		header3Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 10));
		header3 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 23,
						((Coordinates) header.getUpperLeft()).getY() + 472),
				"Aufgabe", "header3", null, header3Props);

		header2.setText("Algorithmus anhang Pseudocode", null, null);

		

		SourceCodeProperties sourcecodeProps = new SourceCodeProperties();
		sourcecodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		sourcecodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.BOLD, 11));

		sourcecodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
		sourcecodeProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);


		SourceCode sc = lang.newSourceCode(new Coordinates(5, 140),
				"sourceCode", null, sourcecodeProps);

		//PseudoCode
		sc.addCodeLine(
				"private static double SimpsonRule(DoubleFunction df, double a, double b, int N){",
				null, 0, null); // 1

		sc.addCodeLine("", null, 1, null); // 4
		sc.addCodeLine("double result, evensum, oddsum = 0.0f;", null, 1, null); // 3
		sc.addCodeLine("double[] x,y = new double [N+1];", null, 1, null); // 4
		sc.addCodeLine("double h = (b - a) / N;", null, 1, null); // 4
		sc.addCodeLine("", null, 1, null); // 4
		sc.addCodeLine("for(int i = 1; i< N; i++){", null, 1, null); // 6
		sc.addCodeLine("x[i] = a + i * h;", null, 2, null); // 9
		sc.addCodeLine("y[i] = df.f(x[i]);", null, 2, null);

		sc.addCodeLine("if(i%2 == 0)", null, 2, null); //
		sc.addCodeLine("evensum = evensum + y[i];", null, 3, null); // 10
		sc.addCodeLine("else", null, 2, null); // 13
		sc.addCodeLine("oddsum = oddsum + y[i];", null, 3, null); // 15
		sc.addCodeLine("}", null, 1, null); // 17
		sc.addCodeLine("x[0] = a + 0 * h;", null, 1, null); // 17
		sc.addCodeLine("y[0] = df.f(x[0]);", null, 1, null);
		sc.addCodeLine("x[N] = a + N * h;", null, 1, null); // 17
		sc.addCodeLine("y[N] = df.f(x[N]);", null, 1, null);
		sc.addCodeLine("", null, 1, null);
		sc.addCodeLine(
				"result =  (h / 3.0) * (y[0] + 4.0 * oddsum + 2.0 * evensum + y[N]);",
				null, 1, null); // 18
		sc.addCodeLine("}", null, 0, null); // 21

		RectProperties screctProps = new RectProperties();
		screctProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		screctProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
		screctProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		screctProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		Rect scRect = lang.newRect(new Offset(-300, -170, "header",
				AnimalScript.DIRECTION_NW),
				new Offset(70, 110, "header", "SE"), "FormelRect", null,
				screctProps);

		scRect.moveTo(null, null, new Coordinates(1, 150), null, null);
		
		Integral1.hide();
		Integral2.hide();
		Integral3.hide();
		Integral1 = lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(355, 107)),
				algoanim.util.Node.convertToNode(new Point(355, 123)) }, "ina",
				null, lineProps);
		Integral2 =  lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(355, 123)),
				algoanim.util.Node.convertToNode(new Point(352, 123)) }, "inb",
				null, lineProps);
		
		Integral3 =  lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(355, 90)),
				algoanim.util.Node.convertToNode(new Point(358, 90)) }, "inc",
				null, lineProps);
		
		Integral1.moveTo(null, null, new Coordinates(110, 541), null, null);
		Integral2.moveTo(null, null, new Coordinates(110, 541), null, null);
		Integral3.moveTo(null, null, new Coordinates(107, 557), null, null);//-3 +16
		
		//Anfang Pseudocode Highlight mit move
		lang.nextStep();
		sc.highlight(0);



		lineProps.set(AnimationPropertiesKeys.BWARROW_PROPERTY, true);
		lineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		Polyline polyline = lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(355, 90)),
				algoanim.util.Node.convertToNode(new Point(355, 125)) }, "a",
				null, lineProps);
		
		Polyline polyline2 = lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(355, 90)),
				algoanim.util.Node.convertToNode(new Point(355, 125)) }, "b",
				null, lineProps);
		
		Polyline polyline3 = lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(355, 90)),
				algoanim.util.Node.convertToNode(new Point(355, 155)) }, "c",
				null, lineProps);
		
		Polyline polyline4 = lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(355, 90)),
				algoanim.util.Node.convertToNode(new Point(355, 155)) }, "d",
				null, lineProps);

		TextProperties polylineProps = new TextProperties();
		polylineProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 12));
		Text poly0text = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 333,
						((Coordinates) header.getUpperLeft()).getY() + 23), "",
				"poly0", null, polylineProps);
		poly0text.setText(zaehler, null, null);

		Text poly01text = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 334,
						((Coordinates) header.getUpperLeft()).getY() + 33), "",
				"poly01", null, polylineProps);
		poly01text.setText("-", null, null);

		Text poly02text = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 326,
						((Coordinates) header.getUpperLeft()).getY() + 43), "",
				"poly02", null, polylineProps);
		poly02text.setText(nenner,null, null); 	//funktion


		ABProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.DIALOG, Font.PLAIN, 10));
		Text header6 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 312,
						((Coordinates) header.getUpperLeft()).getY() + 5), "",
				"Funktion", null, ABProps);
		header6.setText("df    = Funktion", null, null);

		lang.nextStep();


		polyline.moveTo(null, null, new Coordinates(428, 120), start,
				defaultDuration);

		Text poly1text = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 400,
						((Coordinates) header.getUpperLeft()).getY() + 73), "",
				"poly1", null, polylineProps);
		poly1text.setText(String.valueOf((int) a), defaultDuration, null);


		
		lang.nextStep();
		polyline2.moveTo(null, null, new Coordinates(497, 120), start,
				defaultDuration);

		Text poly2text = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 475,
						((Coordinates) header.getUpperLeft()).getY() + 73), "",
				"poly2", null, polylineProps);
		poly2text.setText(String.valueOf((int) b), defaultDuration, null);

		header4 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 387,
						((Coordinates) header.getUpperLeft()).getY() + 50), "",
				"funkgrenzen", null, ABProps);
		header4.setText("a, b = Integrationsgrenzen", defaultDuration,
				null);
		

		
		lang.nextStep();
		polyline3.moveTo(null, null, new Coordinates(548, 90), start,
				defaultDuration);

		Text poly3text = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 524,
						((Coordinates) header.getUpperLeft()).getY() + 45), "",
				"poly2", null, polylineProps);
		poly3text.setText(String.valueOf(N_must_be_Even), defaultDuration, null);
		header5 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 490,
						((Coordinates) header.getUpperLeft()).getY() - 15), "",
				"funkgrenzen", null, ABProps);
		header5.setText("N    = Teilintervalle", defaultDuration, null);

		Text header5zwo = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 490,
						((Coordinates) header.getUpperLeft()).getY() + 15), "",
				"N freiwaehlbar", null, ABProps);
		header5zwo.setText("Parameter ist freiwaehlbar,", defaultDuration,
				null);

		Text header5tri = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 490,
						((Coordinates) header.getUpperLeft()).getY() + 30), "",
				"N freiwaehlbar", null, ABProps);
		header5tri.setText("aber muss gerade sein!", defaultDuration,
				null);

		
		lang.nextStep();


		double result, evensum, oddsum;
		result = 0;
		evensum = 0;
		oddsum = 0;

		sc.unhighlight(0);
		sc.highlight(2);

		InfoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.DIALOG, Font.BOLD, 12));
		infoText = lang.newSourceCode(new Coordinates(250, 460), "InfoText",
				null, InfoProps);
		
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine(
				"=> Initialisierung von result, evensum und oddsum", null, 0,
				null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine(
				"evensum = Summe von allen geraden Stuetzstellen (y-Werte)",
				null, 1, null);
		infoText.addCodeLine("die mit 2 multipliziert werden", null, 1, null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine(
				"oddsum = Summe von allen ungeraden Stuetzstellen (y-Werte),",
				null, 1, null); // 18
		infoText.addCodeLine("die mit 4 multipliziert werden", null, 1, null); // 18

		SourceCodeProperties RESProps = new SourceCodeProperties();
		RESProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.DIALOG, Font.BOLD, 14));
		RESProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				Color.BLUE);
		SourceCode RESText = lang.newSourceCode(new Coordinates(580, 415),
				"result", null, RESProps);
		RESText.addCodeLine("result:       " + result, null, 0, null);

		SourceCode EVENText = lang.newSourceCode(new Coordinates(580, 360),
				"Evensum", null, RESProps);
		EVENText.addCodeLine("Evensum: " + evensum, null, 0, null);

		SourceCode ODDText = lang.newSourceCode(new Coordinates(580, 330),
				"Oddsum", null, RESProps);
		ODDText.addCodeLine("Oddsum:  " + oddsum, null, 0, null);

		RESText.highlight(0);
		EVENText.highlight(0);
		ODDText.highlight(0);

		RectProperties resultProps = new RectProperties();
		resultProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		resultProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
		resultProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		resultProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		Rect resultRect = lang.newRect(new Offset(-40, -150, "header",
				AnimalScript.DIRECTION_NW),
				new Offset(-55, 130, "header", "SE"), "FormelRect5", null,
				resultProps);
		resultRect.moveTo(null, null, new Coordinates(567, 150), null, null);

		lang.nextStep();

		RESText.unhighlight(0);
		EVENText.unhighlight(0);
		ODDText.unhighlight(0);
		infoText.hide();
		sc.unhighlight(2);
		sc.highlight(3);

		double[] x, y;
		x = new double[N_must_be_Even + 1];
		y = new double[N_must_be_Even + 1];

		TextProperties commentProps = new TextProperties();
		commentProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.BOLD, 10));
		commentProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GRAY);

		Text comment1 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 265,
						((Coordinates) header.getUpperLeft()).getY() + 147),
				"//Initialisierung von result, even und oddsum", "AB", null,
				commentProps);

		infoText = lang.newSourceCode(new Coordinates(250, 465), "InfoText",
				null, InfoProps);
		
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("=> Initialisierung von x[] und y[].", null, 0,
				null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("x[] = x-Werte der Stuetzstellen", null, 1, null);
		infoText.addCodeLine("Lenge des Arrays abhaengig von N",
				null, 1, null);
		infoText.addCodeLine("x0,...,xN", null, 1, null);

		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine(
				"y[] = y-Werte der Stuetzstellen mithilfe der x-Werte errechenbar",
				null, 1, null);
		infoText.addCodeLine(
				"Ist relevant fuer die Simpsonformel", null,
				1, null);
		infoText.addCodeLine("f(x0),...,f(xN)", null, 1, null);

		lang.nextStep();

		infoText.hide();
		sc.unhighlight(3);
		sc.highlight(4);

		double h = ((float) (b - a) / N_must_be_Even);

		Text comment2 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 225,
						((Coordinates) header.getUpperLeft()).getY() + 165),
				"// Init. von x0, ..., xN und f(x0), ..., f(xN)", "AB", null,
				commentProps);

		infoText = lang.newSourceCode(new Coordinates(250, 465), "InfoText",
				null, InfoProps);

		infoText.addCodeLine("        b - a", null, 0, null);
		infoText.addCodeLine("h =  	 ------", null, 0, null);
		infoText.addCodeLine("           N", null, 0, null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("Durch einsetzen erhaelt man:", null, 0, null);
		infoText.addCodeLine("        " + (int) b + " - " + (int) a, null, 0,
				null);
		infoText.addCodeLine("h =  	 ------           =  " + ((float) (b - a) / N_must_be_Even), null,
				0, null);
		infoText.addCodeLine("           " + (int) N_must_be_Even, null, 0, null);
		infoText.addCodeLine("", null, 0, null);

		SourceCode hText = lang.newSourceCode(new Coordinates(580, 300), "h",
				null, RESProps);
		hText.addCodeLine("h:              " +((float) h), null, 0, null);
		hText.highlight(0);

		lang.nextStep();
		infoText.hide();
		hText.unhighlight(0);
		sc.unhighlight(4);

		Text comment3 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 165,
						((Coordinates) header.getUpperLeft()).getY() + 178),
				"// h ausrechnen", "AB", null, commentProps);

		infoText = lang.newSourceCode(new Coordinates(250, 465), "InfoText",
				null, InfoProps);

		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine(
				"In der For Schleife werden alle x- und y-Werte der Stuetzstellen berechnet",
				null, 0, null);
		infoText.addCodeLine(
				"bisauf die der 1. und letzten Stuetzstelle",
				null, 0, null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine(
				"daraufhin werden die y-Werte der ungeraden Stuetzstellen unter sum4 (i= 1, 3, ...)",
				null, 0, null);
		infoText.addCodeLine(
				"und die der geraden Stuetzstellen unter sum2 aufaddiert (i= 2, 4, ...)",
				null, 0, null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("Es beginnt mit i=1", null, 0, null);

		SourceCodeProperties ITERProps = new SourceCodeProperties();
		ITERProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.DIALOG, Font.BOLD, 14));
		ITERProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				Color.MAGENTA);

		SourceCode IterationText = lang.newSourceCode(
				new Coordinates(580, 150), "Iteration", null, ITERProps);
		SourceCode xText = lang.newSourceCode(new Coordinates(580, 180), "x",
				null, RESProps);
		SourceCode yText = lang.newSourceCode(new Coordinates(580, 210), "y",
				null, RESProps);
		
		
		
		
		RectProperties IterationProps = new RectProperties();
		IterationProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		IterationProps
				.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		IterationProps
				.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.MAGENTA);
		IterationProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		Rect IterationRect = lang.newRect(new Offset(-30, -140, "header",
				AnimalScript.DIRECTION_NW),
				new Offset(-45, -50, "header", "SE"), "FormelRect", null,
				IterationProps);

		IterationRect.moveTo(null, null, new Coordinates(567, 150), null, null);
		
		
		
		
		
		
		
		
		
//alle Iterationen, aber 1 und 2. Iteration getrennt, da alle CommentTipps eingeblendet werden müssen		
		for (int i = 1; i < N_must_be_Even; i++) {

			
			//1.Iteration
			if (i == 1) {
				sc.highlight(6);
				IterationText.addCodeLine("Iteration:  " + i, null, 0, null);
				IterationText.highlight(0);
				
				
				
				
				
				
			
				
				lang.nextStep();
				
				x[i] = ((float) (a + ((double) i) * h));

				sc.unhighlight(6);
				infoText.hide();
				sc.highlight(7);

				infoText = lang.newSourceCode(new Coordinates(250, 465),
						"InfoText", null, InfoProps);

				infoText.addCodeLine("", null, 0, null);
				infoText.addCodeLine("Den x-Wert zum  " + i
						+ ".Stuetzpunkt ausrechnen", null, 0, null);
				infoText.addCodeLine("", null, 0, null);
				infoText.addCodeLine("x[" + i + "] = a + i * h           =>  "
						+ a + " + " + i + " * " + ((float) h)
						+ "           =       " + ((float) x[i]), null, 0, null);
				infoText.addCodeLine("", null, 0, null);

				xText.addCodeLine("x[" + i + "]:  " + ((float) x[i]), null, 0,
						null);
				xText.highlight(0);
				
				//CommentTipp
				Text comment4 = lang.newText(
						new Coordinates(
								((Coordinates) header.getUpperLeft()).getX() + 195,
								((Coordinates) header.getUpperLeft()).getY() + 208),
						"//x1, ..., xN-1  bzw. f(x1), ..., f(xN-1) ", "for", null, commentProps);
				
				

				lang.nextStep();

				y[i] = ((float) Funktion.f(x[i]));

				infoText.hide();
				xText.unhighlight(0);
				sc.unhighlight(7);
				sc.highlight(8);

				infoText = lang.newSourceCode(new Coordinates(250, 465),
						"InfoText", null, InfoProps);

				infoText.addCodeLine("", null, 0, null);
				infoText.addCodeLine("Den y-Wert zum  " + i
						+ ".Stuetzpunkt ausrechnen", null, 0, null);
				infoText.addCodeLine("", null, 0, null);

				infoText.addCodeLine(
						"                              " + zaehler, null, 0,
						null);
				infoText.addCodeLine("    f( " + x[i]
						+ ")    =         -                      =>  "
						+ ((float) y[i]), null, 0, null);
				infoText.addCodeLine("                            " + nenner,
						null, 0, null);

				yText.addCodeLine("y[" + i + "]:  " + ((float) y[i]), null, 0,
						null);
				yText.highlight(0);
				
				
				Text comment5 = lang.newText(
						new Coordinates(
								((Coordinates) header.getUpperLeft()).getX() + 145,
								((Coordinates) header.getUpperLeft()).getY() + 225),
						"//x1, ..., xN-1  berechnen", "x[]", null, commentProps);
				
				
				
				lang.nextStep();

				sc.unhighlight(8);
				infoText.hide();
				yText.unhighlight(0);
				sc.highlight(9);
				infoText = lang.newSourceCode(new Coordinates(250, 465),
						"InfoText", null, InfoProps);

				infoText.addCodeLine("", null, 0, null);
				infoText.addCodeLine("Die if Abfrage prueft nach", null, 0, null);
				infoText.addCodeLine(
						"ob es sich um eine gerade Stuetzstelle handelt", null,
						0, null);
				infoText.addCodeLine("", null, 0, null);
				infoText.addCodeLine("=> Da es sich bei i = " + i
						+ " um keine gerade Stuetzstelle handelt", null, 0, null);
				infoText.addCodeLine("schlaegt die if Abfrage fehl", null, 0,
						null);
				infoText.addCodeLine("", null, 0, null);
				
				Text comment6 = lang.newText(
						new Coordinates(
								((Coordinates) header.getUpperLeft()).getX() + 145,
								((Coordinates) header.getUpperLeft()).getY() + 242),
						"//f(x1), ..., f(xN-1)  berechnen", "y[]", null, commentProps);
				
				
				
				lang.nextStep();

				sc.unhighlight(9);
				infoText.hide();
				sc.highlight(11);
				infoText = lang.newSourceCode(new Coordinates(250, 465),
						"InfoText", null, InfoProps);
				infoText.addCodeLine("", null, 0, null);
				infoText.addCodeLine("Es handelt sich bei i = " + i
						+ " um eine ungerade Stuetzstelle,", null, 0, null);
				infoText.addCodeLine("daher gelangt es in den else-Block",
						null, 0, null);
				infoText.addCodeLine("", null, 0, null);

				lang.nextStep();

				oddsum += ((float) y[i]);

				sc.unhighlight(11);
				infoText.hide();

				sc.highlight(12);
				ODDText.hide();
				ODDText = lang.newSourceCode(new Coordinates(580, 330),
						"Oddsum", null, RESProps);
				ODDText.addCodeLine("Oddsum:  " + ((float) oddsum), null, 0,
						null);
				ODDText.highlight(0);

				infoText = lang.newSourceCode(new Coordinates(250, 465),
						"InfoText", null, InfoProps);
				infoText.addCodeLine("", null, 0, null);
				infoText.addCodeLine(" Den y-Wert der " + i
						+ ".Stuetzstelle mit oddsum aufaddieren", null, 0, null);

				infoText.addCodeLine("", null, 0, null);
				infoText.addCodeLine("oddsum = oddsum + y[" + i
						+ "]           =>     " + ((float) (oddsum - y[i]))
						+ " + " + ((float) y[i]) + " = " + ((float) oddsum),
						null, 0, null);

				lang.nextStep();
				sc.unhighlight(12);
				ODDText.unhighlight(0);
				infoText.hide();



			}
			
			//2.Iteration
			else if (i == 2) {
				xText.hide();
				yText.hide();
				IterationText.hide();
				infoText.hide();

				IterationText = lang.newSourceCode(new Coordinates(580, 150),
						"Iteration", null, ITERProps);

				sc.highlight(6);
				IterationText.addCodeLine("Iteration:  " + i, null, 0, null);
				IterationText.highlight(0);
				
				
				Text comment7 = lang.newText(
						new Coordinates(
								((Coordinates) header.getUpperLeft()).getX() + 205,
								((Coordinates) header.getUpperLeft()).getY() + 298),
						"//falls ungerade mit oddsum aufaddieren", "oddsum", null, commentProps);
				
				
				lang.nextStep();

				x[i] = ((float) (a + ((double) i) * h));

				sc.unhighlight(6);
				infoText.hide();
				sc.highlight(7);

				infoText = lang.newSourceCode(new Coordinates(250, 465),
						"InfoText", null, InfoProps);

				infoText.addCodeLine("", null, 0, null);
				infoText.addCodeLine("Den x-Wert zum  " + i
						+ ".Stuetzpunkt ausrechnen", null, 0, null);
				infoText.addCodeLine("", null, 0, null);
				infoText.addCodeLine("x[" + i + "] = a + i * h           =>  "
						+ a + " + " + i + " * " + ((float) h)
						+ "           =       " + ((float) x[i]), null, 0, null);
				infoText.addCodeLine("", null, 0, null);

				xText.hide();
				xText = lang.newSourceCode(new Coordinates(580, 180), "x",
						null, RESProps);
				xText.addCodeLine("x[" + i + "]:  " + ((float) x[i]), null, 0,
						null);
				xText.highlight(0);

				lang.nextStep();

				y[i] = ((float) Funktion.f(x[i]));

				infoText.hide();
				xText.unhighlight(0);
				sc.unhighlight(7);
				sc.highlight(8);

				infoText = lang.newSourceCode(new Coordinates(250, 465),
						"InfoText", null, InfoProps);

				infoText.addCodeLine("", null, 0, null);
				infoText.addCodeLine("Den y-Wert zum  " + i
						+ ".Stuetzpunkt ausrechnen", null, 0, null);
				infoText.addCodeLine("", null, 0, null);

				infoText.addCodeLine(
						"                              " + zaehler, null, 0,
						null);
				infoText.addCodeLine("    f( " + x[i]
						+ ")    =         -                      =>  "
						+ ((float) y[i]), null, 0, null);
				infoText.addCodeLine("                            " + nenner,
						null, 0, null);

				yText.hide();
				yText = lang.newSourceCode(new Coordinates(580, 210), "y",
						null, RESProps);
				yText.addCodeLine("y[" + i + "]:  " + ((float) y[i]), null, 0,
						null);
				yText.highlight(0);
				lang.nextStep();

				sc.unhighlight(8);
				infoText.hide();
				yText.unhighlight(0);
				sc.highlight(9);
				infoText = lang.newSourceCode(new Coordinates(250, 465),
						"InfoText", null, InfoProps);

				infoText.addCodeLine("", null, 0, null);
				infoText.addCodeLine("=> Da es sich bei i = " + i
						+ " um eine gerade Stuetzstelle handelt", null, 0, null);

				infoText.addCodeLine("kommt es in den if-Block", null, 0, null);
				infoText.addCodeLine("", null, 0, null);
				lang.nextStep();

				sc.unhighlight(9);
				infoText.hide();

				evensum += ((float) y[i]);

				sc.highlight(10);
				EVENText.hide();
				EVENText = lang.newSourceCode(new Coordinates(580, 360),
						"Evensum", null, RESProps);
				EVENText.addCodeLine("Evensum: " + ((float) evensum), null, 0,
						null);
				EVENText.highlight(0);

				infoText = lang.newSourceCode(new Coordinates(250, 465),
						"InfoText", null, InfoProps);
				infoText.addCodeLine("", null, 0, null);
				infoText.addCodeLine(" Den y-Wert der " + i
						+ ".Stuetzstelle mit evensum aufaddieren", null, 0, null);

				infoText.addCodeLine("", null, 0, null);
				infoText.addCodeLine("evensum = evensum + y[" + i
						+ "]           =>     " + ((float) (evensum - y[i]))
						+ " + " + ((float) y[i]) + " = " + ((float) evensum),
						null, 0, null);

				lang.nextStep();
				sc.unhighlight(10);
				EVENText.unhighlight(0);
				infoText.hide();
				
				Text comment8 = lang.newText(
						new Coordinates(
								((Coordinates) header.getUpperLeft()).getX() + 205,
								((Coordinates) header.getUpperLeft()).getY() + 269),
						"//falls gerade mit evensum aufaddieren", "evensum", null, commentProps);
				
				
			}



			else {
				//gerade Iterationen 2,4,6
				if (i % 2 == 0) {

					xText.hide();
					yText.hide();
					IterationText.hide();
					infoText.hide();

					IterationText = lang.newSourceCode(
							new Coordinates(580, 150), "Iteration", null,
							ITERProps);

					sc.highlight(6);
					IterationText
							.addCodeLine("Iteration:  " + i, null, 0, null);
					IterationText.highlight(0);
					lang.nextStep();

					x[i] = ((float) (a + ((double) i) * h));

					sc.unhighlight(6);
					infoText.hide();
					sc.highlight(7);

					infoText = lang.newSourceCode(new Coordinates(250, 465),
							"InfoText", null, InfoProps);

					infoText.addCodeLine("", null, 0, null);
					infoText.addCodeLine("Den x-Wert zum  " + i
							+ ".Stuetzpunkt ausrechnen", null, 0, null);
					infoText.addCodeLine("", null, 0, null);
					infoText.addCodeLine("x[" + i
							+ "] = a + i * h           =>  " + a + " + " + i
							+ " * " + ((float) h) + "           =       "
							+ ((float) x[i]), null, 0, null);
					infoText.addCodeLine("", null, 0, null);

					xText.hide();
					xText = lang.newSourceCode(new Coordinates(580, 180), "x",
							null, RESProps);
					xText.addCodeLine("x[" + i + "]:  " + ((float) x[i]), null,
							0, null);
					xText.highlight(0);

					lang.nextStep();

					y[i] = ((float) Funktion.f(x[i]));

					infoText.hide();
					xText.unhighlight(0);
					sc.unhighlight(7);
					sc.highlight(8);

					infoText = lang.newSourceCode(new Coordinates(250, 465),
							"InfoText", null, InfoProps);

					infoText.addCodeLine("", null, 0, null);
					infoText.addCodeLine("Den y-Wert zum  " + i
							+ ".Stuetzpunkt ausrechnen", null, 0, null);
					infoText.addCodeLine("", null, 0, null);

					infoText.addCodeLine("                              "
							+ zaehler, null, 0, null);
					infoText.addCodeLine("    f( " + ((float) x[i])
							+ ")    =         -                      =>  "
							+ ((float) y[i]), null, 0, null);
					infoText.addCodeLine("                            "
							+ nenner, null, 0, null);

					yText.hide();
					yText = lang.newSourceCode(new Coordinates(580, 210), "y",
							null, RESProps);
					yText.addCodeLine("y[" + i + "]:  " + ((float) y[i]), null,
							0, null);
					yText.highlight(0);
					lang.nextStep();

					sc.unhighlight(8);
					infoText.hide();
					yText.unhighlight(0);
					sc.highlight(9);
					infoText = lang.newSourceCode(new Coordinates(250, 465),
							"InfoText", null, InfoProps);

					infoText.addCodeLine("", null, 0, null);
					infoText.addCodeLine("=> Da es sich bei i = " + i
							+ " um eine gerade Stuetzstelle handelt", null, 0,
							null);

					infoText.addCodeLine("kommt es in den if-Block", null, 0,
							null);
					infoText.addCodeLine("", null, 0, null);
					lang.nextStep();

					sc.unhighlight(9);
					infoText.hide();

					evensum += ((float) y[i]);

					sc.highlight(10);
					EVENText.hide();
					EVENText = lang.newSourceCode(new Coordinates(580, 360),
							"Evensum", null, RESProps);
					EVENText.addCodeLine("Evensum: " + ((float) evensum), null,
							0, null);
					EVENText.highlight(0);

					infoText = lang.newSourceCode(new Coordinates(250, 465),
							"InfoText", null, InfoProps);
					infoText.addCodeLine("", null, 0, null);
					infoText.addCodeLine(" Den y-Wert der " + i
							+ ".Stuetzstelle mit evensum aufaddieren", null, 0,
							null);

					infoText.addCodeLine("", null, 0, null);
					infoText.addCodeLine(
							"evensum = evensum + y["
									+ i
									+ "]           =>     "
									+ ((float) (evensum - y[i]) + " + " + ((float) y[i]))
									+ " = " + ((float) evensum), null, 0, null);

					lang.nextStep();
					sc.unhighlight(10);
					EVENText.unhighlight(0);
					infoText.hide();

				} else {
					//ungerade Iterationen 3,5,7,..
					xText.hide();
					yText.hide();
					IterationText.hide();
					infoText.hide();

					IterationText = lang.newSourceCode(
							new Coordinates(580, 150), "Iteration", null,
							ITERProps);

					sc.highlight(6);
					IterationText
							.addCodeLine("Iteration:  " + i, null, 0, null);
					IterationText.highlight(0);
					lang.nextStep();

					x[i] = ((float) (a + ((double) i) * h));

					sc.unhighlight(6);
					infoText.hide();
					sc.highlight(7);

					infoText = lang.newSourceCode(new Coordinates(250, 465),
							"InfoText", null, InfoProps);

					infoText.addCodeLine("", null, 0, null);
					infoText.addCodeLine("Den x-Wert zum  " + i
							+ ".Stuetzpunkt ausrechnen", null, 0, null);
					infoText.addCodeLine("", null, 0, null);
					infoText.addCodeLine("x[" + i
							+ "] = a + i * h           =>  " + a + " + " + i
							+ " * " + ((float) h) + "           =       "
							+ ((float) x[i]), null, 0, null);
					infoText.addCodeLine("", null, 0, null);

					xText.hide();
					xText = lang.newSourceCode(new Coordinates(580, 180), "x",
							null, RESProps);
					xText.addCodeLine("x[" + i + "]:  " + ((float) x[i]), null,
							0, null);
					xText.highlight(0);

					lang.nextStep();

					y[i] = ((float) Funktion.f(x[i]));

					infoText.hide();
					xText.unhighlight(0);
					sc.unhighlight(7);
					sc.highlight(8);

					infoText = lang.newSourceCode(new Coordinates(250, 465),
							"InfoText", null, InfoProps);

					infoText.addCodeLine("", null, 0, null);
					infoText.addCodeLine("Den y-Wert zum  " + i
							+ ".Stuetzpunkt ausrechnen", null, 0, null);
					infoText.addCodeLine("", null, 0, null);

					infoText.addCodeLine("                              "
							+ zaehler, null, 0, null);
					infoText.addCodeLine("    f( " + ((float) x[i])
							+ ")    =         -                      =>  "
							+ ((float) y[i]), null, 0, null);
					infoText.addCodeLine("                            "
							+ nenner, null, 0, null);

					yText.hide();
					yText = lang.newSourceCode(new Coordinates(580, 210), "y",
							null, RESProps);
					yText.addCodeLine("y[" + i + "]:  " + ((float) y[i]), null,
							0, null);
					yText.highlight(0);
					lang.nextStep();

					sc.unhighlight(8);
					infoText.hide();
					yText.unhighlight(0);
					sc.highlight(9);
					infoText = lang.newSourceCode(new Coordinates(250, 465),
							"InfoText", null, InfoProps);

				
					infoText.addCodeLine("", null, 0, null);
					infoText.addCodeLine("=> Da es sich bei i = " + i
							+ " um keine gerade Stuetzstelle handelt", null, 0,
							null);
					infoText.addCodeLine("schlaegt die if Abfrage fehl", null,
							0, null);
					infoText.addCodeLine("", null, 0, null);
					lang.nextStep();

					sc.unhighlight(9);
					infoText.hide();
					sc.highlight(11);
					infoText = lang.newSourceCode(new Coordinates(250, 465),
							"InfoText", null, InfoProps);
					infoText.addCodeLine("", null, 0, null);
					infoText.addCodeLine("Es handelt sich bei i = " + i
							+ " um eine ungerade Stuetzstelle,", null, 0, null);
					infoText.addCodeLine("daher gelangt es in den else-Block",
							null, 0, null);
					infoText.addCodeLine("", null, 0, null);

					lang.nextStep();

					oddsum += ((float) y[i]);

					sc.unhighlight(11);
					infoText.hide();

					sc.highlight(12);
					ODDText.hide();
					ODDText = lang.newSourceCode(new Coordinates(580, 330),
							"Oddsum", null, RESProps);
					ODDText.addCodeLine("Oddsum:  " + ((float) oddsum), null,
							0, null);
					ODDText.highlight(0);

					infoText = lang.newSourceCode(new Coordinates(250, 465),
							"InfoText", null, InfoProps);
					infoText.addCodeLine("", null, 0, null);
					infoText.addCodeLine(" Den y-Wert der " + i
							+ ".Stuetzstelle mit oddsum aufaddieren", null, 0,
							null);

					infoText.addCodeLine("", null, 0, null);
					infoText.addCodeLine(
							"oddsum = oddsum + y[" + i + "]           =>     "
									+ ((float) (oddsum - y[i])) + " + "
									+ ((float) y[i]) + " = " + ((float) oddsum),
							null, 0, null);

					lang.nextStep();
					sc.unhighlight(12);
					ODDText.unhighlight(0);
					infoText.hide();

				}
			}

		}// for
		xText.hide();
		yText.hide();
		IterationText.hide();
		sc.highlight(6);
		infoText = lang.newSourceCode(new Coordinates(250, 465), "InfoText",
				null, InfoProps);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("Es endet mit der " + N_must_be_Even
				+ ".Stuetzstelle, da i < N nicht mehr gilt", null, 0, null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("=> somit endet die For Schleife", null, 0, null);
		IterationRect.hide();
		lang.nextStep();

		infoText.hide();
		;
		sc.unhighlight(6);
		sc.highlight(14);

		x[0] = ((float) (a + ((double) 0) * h));

		infoText = lang.newSourceCode(new Coordinates(250, 465), "InfoText",
				null, InfoProps);

		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("Den x-Wert zum  " + 0 + ".Stuetzpunkt ausrechnen",
				null, 0, null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("x[0] = a + i * h           =>  " + a + " + " + 0
				+ " * " + ((float) h) + "           =       " + ((float) x[0]),
				null, 0, null);
		infoText.addCodeLine("", null, 0, null);

		SourceCode x0 = lang.newSourceCode(new Coordinates(580, 270), "x0",
				null, RESProps);
		x0.addCodeLine("X[0]:        " + ((float) x[0]), null, 0, null);

		x0.highlight(0);
		
		

		lang.nextStep();

		
		
		
		
		
		x0.unhighlight(0);
		sc.unhighlight(14);
		sc.highlight(15);
		infoText.hide();

		y[0] = ((float) Funktion.f(x[0]));

		infoText = lang.newSourceCode(new Coordinates(250, 465), "InfoText",
				null, InfoProps);

		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("Den y-Wert zum 0.Stuetzpunkt ausrechnen", null, 0,
				null);
		infoText.addCodeLine("", null, 0, null);

		infoText.addCodeLine("                              " + zaehler, null,
				0, null);
		infoText.addCodeLine("    f( " + ((float) x[0])
				+ ")    =         -                      =>  " + ((float) y[0]),
				null, 0, null);
		infoText.addCodeLine("                            " + nenner, null, 0,
				null);

		SourceCode y0 = lang.newSourceCode(new Coordinates(580, 240), "y0",
				null, RESProps);
		y0.addCodeLine("y[0]:        " + ((float) y[0]), null, 0, null);

		y0.highlight(0);
		
		
		Text comment9 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 140,
						((Coordinates) header.getUpperLeft()).getY() + 328),
				"// x0 berechnen", "x0", null,
				commentProps);

		lang.nextStep();

		y0.unhighlight(0);
		infoText.hide();
		sc.unhighlight(15);
		sc.highlight(16);

		x[N_must_be_Even] = ((float) (a + ((double) N_must_be_Even) * h));

		infoText = lang.newSourceCode(new Coordinates(250, 465), "InfoText",
				null, InfoProps);

		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("Den x-Wert zum " + N_must_be_Even + ".Stuetzpunkt ausrechnen",
				null, 0, null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("x[N] = a + i * h           =>  " + a + " + " + N_must_be_Even
				+ " * " + ((float) h) + "           =       " + ((float) x[N_must_be_Even]),
				null, 0, null);
		infoText.addCodeLine("", null, 0, null);

		SourceCode xN = lang.newSourceCode(new Coordinates(580, 210), "xN",
				null, RESProps);
		xN.addCodeLine("x[N]:        " + ((float) x[N_must_be_Even]), null, 0, null);

		xN.highlight(0);
		
		
		Text comment10 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 140,
						((Coordinates) header.getUpperLeft()).getY() + 342),
				"// y0 berechnen", "y0", null,
				commentProps);

		lang.nextStep();

		xN.unhighlight(0);
		sc.unhighlight(16);
		sc.highlight(17);
		infoText.hide();

		y[N_must_be_Even] = ((float) Funktion.f(x[N_must_be_Even]));

		infoText = lang.newSourceCode(new Coordinates(250, 465), "InfoText",
				null, InfoProps);

		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("Den y-Wert zum " + N_must_be_Even + ".Stuetzpunkt ausrechnen",
				null, 0, null);
		infoText.addCodeLine("", null, 0, null);

		infoText.addCodeLine("                              " + zaehler, null,
				0, null);
		infoText.addCodeLine("    f( " + ((float) x[N_must_be_Even])
				+ ")    =         -                      =>  " + ((float) y[N_must_be_Even]),
				null, 0, null);
		infoText.addCodeLine("                            " + nenner, null, 0,
				null);

		SourceCode yN = lang.newSourceCode(new Coordinates(580, 180), "yN",
				null, RESProps);
		yN.addCodeLine("y[N]:        " + ((float) y[N_must_be_Even]), null, 0, null);

		yN.highlight(0);
		
		
		
		Text comment11 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 140,
						((Coordinates) header.getUpperLeft()).getY() + 357),
				"// xN berechnen", "xN", null,
				commentProps);

		lang.nextStep();
		infoText.hide();
		yN.unhighlight(0);
		sc.unhighlight(17);
		sc.highlight(19);
		
		//Ergebnis
		result = (h / 3.0) * (y[0] + 4.0 * oddsum + 2.0 * evensum + y[N_must_be_Even]);

		infoText = lang.newSourceCode(new Coordinates(250, 465), "InfoText",
				null, InfoProps);
		infoText.addCodeLine("          h", null, 1, null);
		infoText.addCodeLine(
				"f(x) =  -   * ( f(x0) + 4 * [ f(x1) + f(x3) + f(x5) + ... ] + 2 * [ f(x2) + f(x4) + ... ] + f(xN) )",
				null, 1, null);
		infoText.addCodeLine("          3", null, 1, null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("nach einsetzen:", null, 0, null);
		infoText.addCodeLine("", null, 0, null);
		infoText.addCodeLine("          " + ((float) h), null, 1, null);
		infoText.addCodeLine("f(x) =  -   * ( " + ((float) y[0]) + " + 4 * "
				+ ((float) oddsum) + "  + 2 * " + ((float) evensum) + "  + "
				+ ((float) y[N_must_be_Even]) + " )           =   " + (float) result, null,
				1, null);
		infoText.addCodeLine("          3", null, 1, null);

		RESText.hide();
		RESText = lang.newSourceCode(new Coordinates(580, 415), "result", null,
				RESProps);
		RESText.addCodeLine("result:       " + ((float) result), null, 0, null);
		RESText.highlight(0);
		
		Text comment12 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 140,
						((Coordinates) header.getUpperLeft()).getY() + 372),
				"// f(xN) berechnen", "fxN", null,
				commentProps);
		
		lang.nextStep();
		RESText.unhighlight(0);

		infoText.hide();

		PolylineProperties reslineProps = new PolylineProperties();
		reslineProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
		reslineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		Polyline polyresultline = lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(233, 550)),
				algoanim.util.Node.convertToNode(new Point(470, 550)) },
				"result", null, reslineProps);

		TextProperties ResProps = new TextProperties();
		ResProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.DIALOG, Font.BOLD, 30));
		Text resultheader;
		resultheader = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 220,
						((Coordinates) header.getUpperLeft()).getY() + 505),
				"Simpsonregel", "AB", null, ResProps);

		TextProperties Res2Props = new TextProperties();
		Res2Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.DIALOG, Font.BOLD, 20));
		Text result2header;
		result2header = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 270,
						((Coordinates) header.getUpperLeft()).getY() + 510),
				"mit N = " + String.valueOf(N_must_be_Even), "AB", null, Res2Props);

		TextProperties Res3Props = new TextProperties();
		Res3Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.DIALOG, Font.BOLD, 30));
		Text result3header;
		result3header = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 470,
						((Coordinates) header.getUpperLeft()).getY() + 509), ""
						+ (float) result, "AB", null, Res3Props);

		PolylineProperties underlineProps = new PolylineProperties();
		underlineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		Polyline underline = lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(465, 565)),
				algoanim.util.Node.convertToNode(new Point(640, 565)) },
				"result", null, underlineProps);

		Polyline underline1 = lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(465, 570)),
				algoanim.util.Node.convertToNode(new Point(640, 570)) },
				"result", null, underlineProps);


		lang.nextStep();

		lang.hideAllPrimitives();
		header.show();
		hRect.show();
		header2.setText("Fehlerabschaetzung?", null, null);
		header2.show();
		InfoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.DIALOG, Font.BOLD, 20));
		infoText = lang.newSourceCode(new Coordinates(40, 140), "InfoText",
				null, InfoProps);

		RectProperties screctProps2 = new RectProperties();
		screctProps2.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		screctProps2.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
		screctProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		screctProps2.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		Rect scRect2 = lang.newRect(new Offset(-50, -50, "header",
				AnimalScript.DIRECTION_NW),
				new Offset(370, 30, "header", "SE"), "FormelRect5", null,
				screctProps2);
		scRect2.moveTo(null, null, new Coordinates(30, 140), null, null);

		infoText.addCodeLine("                                   (b-a)", null,
				1, null);
		infoText.addCodeLine(
				"Formel:         | E | <=                 [max |f' (x)|];    a <= x <= b  ",
				null, 1, null);
		infoText.addCodeLine("                                 180 * n ",
				null, 1, null);
		
		lineProps.set(AnimationPropertiesKeys.BWARROW_PROPERTY, false);
		Polyline Bruchstrich3 = lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(260, 200)),
				algoanim.util.Node.convertToNode(new Point(325, 200)) }, "inst",
				null, lineProps);
		
		expoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.DIALOG, Font.BOLD, 10));
		expo6 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 407,
						((Coordinates) header.getUpperLeft()).getY() + 145),
				"4", "hoch4-2", null,
				expoProps);
		
		
		lang.nextStep();

		InfoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.DIALOG, Font.PLAIN, 15));
		infoText2 = lang.newSourceCode(new Coordinates(40, 230), "InfoText",
				null, InfoProps2);

		infoText2.addCodeLine("", null, 1, null);
		infoText2.addCodeLine("", null, 1, null);
		infoText2.addCodeLine("", null, 1, null);
		infoText2.addCodeLine(
				"Schritt 1: Bestimme die 4.Ableitung der Funktion", null, 1,
				null);
		infoText2.addCodeLine("und deren Betragsmaximum im Intervall von "
				+ (int) a + " bis " + (int) b, null, 1, null);

		infoText2.addCodeLine("", null, 1, null);
		infoText2.addCodeLine("", null, 1, null);
		infoText2.addCodeLine("", null, 1, null);
		infoText2.addCodeLine("                                              "
				+ zaehlerabl4, null, 20, null);
		infoText2.addCodeLine("4.Ableitung:      f' (x)   =    -", null, 20,
				null);
		infoText2.addCodeLine("                                           "
				+ nennerabl4, null, 20, null);

		infoText2.addCodeLine("", null, 1, null);
		infoText2.addCodeLine("", null, 1, null);
		infoText2.addCodeLine("", null, 1, null);
		infoText2.addCodeLine(" =>  Durch einsetzen von Werte im Intervall von "
				+ (int) a + " bis " + (int) b, null, 1, null);
		infoText2.addCodeLine(
				"        erhaelt man das Betragsmaximum "
						+ String.valueOf(Betragsmaximum), null, 1, null);
		
		
		FormelText = lang.newSourceCode(new Coordinates(40, 140), "Formeltext",
				null, FormelProps);
		FormelText.addCodeLine("                   " + (int) b + "           "
				+ zaehler, null, 0, null);
		FormelText.addCodeLine("    f(x)    =               -", null, 0, null);
		FormelText.addCodeLine("                  " + (int) a + "        "
				+ nenner, null, 0, null);
		
		
		
		
	
		FormelText.moveTo(null, null, new Coordinates(30, 410), null, null);
		RectProperties FormelrectProps3 = new RectProperties();
		FormelrectProps3.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		FormelrectProps3.set(AnimationPropertiesKeys.FILL_PROPERTY,
				Color.ORANGE);
		FormelrectProps3.set(AnimationPropertiesKeys.COLOR_PROPERTY,
				Color.BLACK);
		FormelrectProps3.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		Rect FormelRect3 = lang.newRect(new Offset(-3, -23, "header",
				AnimalScript.DIRECTION_NW), new Offset(3, 20, "header", "SE"),
				"FormelRect4", null, FormelrectProps3);

		FormelRect3.moveTo(null, null, new Coordinates(30, 400), null, null);
		header3Props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 10));
		header3 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 23,
						((Coordinates) header.getUpperLeft()).getY() + 362),
				"Aufgabe", "header3", null, header3Props);

		header4 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 33,
						((Coordinates) header.getUpperLeft()).getY() + 435),
				"n = " + String.valueOf(N_must_be_Even), "header3", null, header3Props);
		
		Integral1.moveTo(null, null, new Coordinates(110, 431), null, null);
		Integral2.moveTo(null, null, new Coordinates(110, 431), null, null);
		Integral3.moveTo(null, null, new Coordinates(107, 447), null, null);//-3 +16
		
		
		Integral1.show();
		Integral2.show();
		Integral3.show();
		
		expoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.DIALOG, Font.PLAIN, 7));
		expo5 = lang.newText(
				new Coordinates(
						((Coordinates) header.getUpperLeft()).getX() + 465,
						((Coordinates) header.getUpperLeft()).getY() + 395),
				"4", "hoch4-1", null,
				expoProps);
		
		lang.nextStep();
		infoText2.hide();
		expo5.hide();
		
		
		Polyline Bruchstrich4 = lang.newPolyline(new Coordinates[] {
				algoanim.util.Node.convertToNode(new Point(510, 436)),
				algoanim.util.Node.convertToNode(new Point(545, 436)) }, "inst",
				null, lineProps);


		infoText2 = lang.newSourceCode(new Coordinates(40, 230), "InfoText",
				null, InfoProps2);

		infoText2.addCodeLine("", null, 1, null);
		infoText2.addCodeLine("", null, 1, null);
		infoText2.addCodeLine("", null, 1, null);
		infoText2.addCodeLine(
				"Schritt 2: Formel ausrechnen mit ausgerechnetem Betragsmaximum = "
						+ String.valueOf(Betragsmaximum), null, 1, null);
		infoText2.addCodeLine("", null, 1, null);
		infoText2.addCodeLine("", null, 1, null);
		infoText2.addCodeLine("", null, 1, null);
		infoText2.addCodeLine("", null, 1, null);
		DecimalFormat number = new DecimalFormat("#.######");
		float Fehler = (float) ((Math.pow((b - a), 5) / (180.0 * Math.pow(N_must_be_Even, 4))) * Betragsmaximum);
		infoText2.addCodeLine(
				"                                   ("
						+ String.valueOf((int) b) + " - "
						+ String.valueOf((int) a) + ")", null, 20, null);
		infoText2
				.addCodeLine(
						"Formel:         | E | <=             *  "
								+ String.valueOf(Betragsmaximum) + " = "
								+ String.valueOf(number.format(Fehler)), null,
						20, null);
		infoText2.addCodeLine("                                 180 * "
				+ String.valueOf(N_must_be_Even) + "", null, 20, null);

		infoText2.addCodeLine("", null, 1, null);
		infoText2.addCodeLine("", null, 1, null);
		infoText2.addCodeLine("", null, 1, null);
		infoText2.addCodeLine(
				" =>  Der Fehler betraegt "
						+ String.valueOf(number.format(Fehler)), null, 1, null);

		lang.nextStep();

		lang.hideAllPrimitives();
		header.show();
		hRect.show();
		header2.setText("Lernziele - Beantwortung Kernfragen", null, null);
		header2.show();
		InfoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.DIALOG, Font.BOLD, 12));

		infoText = lang.newSourceCode(new Coordinates(40, 150), "InfoText",
				null, InfoProps);

		infoText.addCodeLine("", null, 1, null);
		infoText.addCodeLine("Was versteht man unter numerischen Integration?",
				null, 1, null);
		infoText.addCodeLine("", null, 1, null);
		infoText.addCodeLine("", null, 1, null);
		infoText.addCodeLine("Wie funktioniert die Simpsonregel? ", null, 1,
				null);
		infoText.addCodeLine("", null, 1, null);
		infoText.addCodeLine("", null, 1, null);
		infoText.addCodeLine("Wie lautet die Formel zu Simpsonregel?", null, 1,
				null);
		infoText.addCodeLine("", null, 1, null);
		infoText.addCodeLine("", null, 1, null);
		infoText.addCodeLine(
				"Was versteht man unter Fehlerabschaetzung der Simpsonregel?",
				null, 1, null);
		infoText.addCodeLine("", null, 1, null);
		infoText.addCodeLine("", null, 1, null);
		infoText.addCodeLine("Wie lautet die Formel der Fehlerabschaetzung?",
				null, 1, null);
		infoText.addCodeLine("", null, 1, null);
		infoText.addCodeLine("", null, 1, null);
		infoText.addCodeLine(
				"Sind Sie in der Lage den Code des Algorithmus halbwegs darzustellen?",
				null, 1, null);
		infoText.addCodeLine("", null, 1, null);
		infoText.addCodeLine("", null, 1, null);
		infoText.addCodeLine("", null, 1, null);

		infoText.addCodeLine(
				"=> Selbsttest: Koennen Sie diese Fragen beantworten?", null, 1,
				null);
		
		lang.finalizeGeneration();

	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public String getName() {
        return "Simpsonregel";
    }

    public String getAlgorithmName() {
        return "Simpsonregel";
    }

    public String getAnimationAuthor() {
        return "Yasin Kaymak";
    }

    public String getDescription(){
        return "Die Simpsonregel sorgt für eine n&auml;herungsweise Berechnung eines bestimmten Integrals."
 +"\n"
 +"\n"
 +"Oft ist es schwierig, dass Integral einer komplexen Funktion anzugeben."
 +"\n"
 +"In solchen F&auml;llen muss das Integral n&auml;herungsweise berechnet werden."
 +"\n"
 +"Die n&auml;herungsweise Berechnung eines Integrals erfolgt durch die numerische Integration."
 +"\n"
 +"Ein Bestandteil der numerischen Integration ist die Simpsonregel.";
    }

    public String getCodeExample(){
        return "  private static double SimpsonRule(DoubleFunction df, double a, double b, int N){"
 +"\n"
 +"	"
 +"\n"
 +"             double result, evensum, oddsum = 0.0f;"
 +"\n"
 +"             double[] x,y = new double [N+1];"
 +"\n"
 +"             double h = (b - a) / N;"
 +"\n"
 +"\n"
 +"             for(int i = 1; i< N; i++){"
 +"\n"
 +"                       x[i] = a + i * h;						"
 +"\n"
 +"                       y[i] = df.f(x[i]);"
 +"\n"
 +"\n"
 +"                       if(i%2 == 0)"
 +"\n"
 +"                                 evensum = evensum + y[i];"
 +"\n"
 +"\n"
 +"                       else"
 +"\n"
 +"	    oddsum = oddsum + df.f(a + i * h);"
 +"\n"
 +"             }"
 +"\n"
 +"             x[0] = a + 0 * h;"
 +"\n"
 +"             y[0] = df.f(x[0]);"
 +"\n"
 +"             x[N] = a + N * h;"
 +"\n"
 +"             y[N] = df.f(x[N]);"
 +"\n"
 +"\n"
 +"             result =  (h / 3.0) * (y[0] + 4.0 * oddsum + 2.0 * evensum + y[N]);"
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
        return Generator.JAVA_OUTPUT;
    }

    
//   @Override
//	public boolean validateInput(AnimationPropertiesContainer arg0,
//			Hashtable<String, Object> arg1) throws IllegalArgumentException {
//		 
//		// TODO Auto-generated method stub
//		if(N % 2 != 0 && N != 0 ){
//			return false;
//				 
//		}
//		else{
//		return true;
//	}}
    
  
	


}