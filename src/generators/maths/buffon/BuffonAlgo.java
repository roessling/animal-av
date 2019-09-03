package generators.maths.buffon;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.Vector;

import util.Brush;
import util.text.FormattedText;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Point;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.properties.CircleProperties;
import algoanim.properties.PointProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.PropertiesBuilder;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class BuffonAlgo {
	public final static String title = "Die Buffon'sche Nadelmethode";
	public final static String author = "Christian Ritter";
	
	private AnimalScript a;
	
	private RectProperties boxRect;
	private RectProperties whiteBoxRect;
	private RectProperties headerRect;
	private RectProperties bar1;
	private RectProperties bar2;
	private RectProperties barError;
	private PolylineProperties gridLine;
	private PolylineProperties needle1Line;
	private PolylineProperties needle2Line;
	private CircleProperties cut;
	private TextProperties largeText;
	private TextProperties boldText;
	private TextProperties normalText;
	private TextProperties coordText;
	
	private int[] throwN = new int[2];
	private int[] cutN = new int[2];
	private int exiter = 0;
	
	private void initProperties(Brush color1, Brush color2, Brush color3) {

		boxRect = PropertiesBuilder.createRectProperties(new Color(180, 180, 180), new Color(245, 245, 245), 100, false);
		whiteBoxRect = PropertiesBuilder.createRectProperties(new Color(200, 200, 200), Color.WHITE, 50, false);
		headerRect = PropertiesBuilder.createRectProperties(new Color(180, 180, 180), new Color(230, 230, 230), 50, false);
		bar1 = PropertiesBuilder.createRectProperties(color1.brighter(0.7, 0.3), color1.brighter(0.7, 0.3), 20, false);
		bar2 = PropertiesBuilder.createRectProperties(color2.brighter(0.7, 0.3), color2.brighter(0.7, 0.3), 20, false);
		barError = PropertiesBuilder.createRectProperties(color3.brighter(0.7, 0.3), color3.brighter(0.7, 0.3), 20, false);
		needle1Line = PropertiesBuilder.createPolylineProperties(color1.darker());
		needle2Line = PropertiesBuilder.createPolylineProperties(color2.darker());
		gridLine = PropertiesBuilder.createPolylineProperties(new Color(220, 220, 220), false, false, 15, false);
		cut = PropertiesBuilder.createCircleProperties(Color.WHITE, new Color(200, 200, 200), 20, false);
		largeText = PropertiesBuilder.createTextProperties(null, new Font("SansSerif", 0, 18));
		boldText = PropertiesBuilder.createTextProperties(null, new Font("SansSerif", Font.BOLD, 14));
		normalText = PropertiesBuilder.createTextProperties(null, new Font("SansSerif", 0, 14));
		coordText = PropertiesBuilder.createTextProperties(null, new Font("SansSerif", 0, 10), true, 0, false);
		
	}
	
	public BuffonAlgo(int maxsteps, double abdiff, int abnum, int[][] needles1, int[][] needles2,
			          Brush color1, Brush color2, Brush color3, boolean withExplanation, boolean stepwise) {
		
		initProperties(color1, color2, color3);
		
		a = new AnimalScript(title, author, 1200, 600);
		a.setStepMode(true);
		
		Point ref = a.newPoint(new Coordinates(0, 0), "reference", null, new PointProperties());
		ref.hide();
		
		boolean correctNeedles = true;
		Vector<Needle> needleSet1 = null, needleSet2 = null;
		try {
			needleSet1 = initNeedles(needles1, color1);
			needleSet2 = initNeedles(needles2, color2);
		}
		catch (Exception ex) {
			correctNeedles = false;
		}
		
		if (maxsteps <= 0 || abdiff <= 0 || abnum <= 0 || !correctNeedles) {
			
			a.newRect(new Offset(-2, -2, "reference", "C"), new Coordinates(1800, 55), "headerBack", null,
				      PropertiesBuilder.createRectProperties(Color.WHITE, new Color(240, 0, 0), 100, false));
			a.newText(new Offset(17, 18, "reference", "NW"), "Fehler beim Erstellen", "header", null,
					  PropertiesBuilder.createTextProperties(Color.WHITE, new Font("SansSerif", 1, 24)));
			a.newRect(new Offset(-2, 57, "reference", "C"), new Coordinates(1800, 1400), "infoBack", null,
			          PropertiesBuilder.createRectProperties(Color.WHITE, new Color(255, 230, 238), 100, false));
			
			FormattedText errorInfo = new FormattedText("errorInfo", a, new Offset(17, 15, "infoBack", "NW"), largeText, false, 0.5, 0.5);
			errorInfo.beginItemize(FormattedText.ItemForm.TRIANGLE, 20, new Color(240, 0, 0));
			errorInfo.addAsNewPar("Beim Erstellen des Graphen anhand der im Generator eingegebenen Daten");
			errorInfo.addInNextLine("ist folgender Fehler entstanden:");
			if (maxsteps <= 0) {
				errorInfo.addAsNewPar("Für die maximale Anzahl der Schritte (");
				errorInfo.add("maxSteps", true);
				errorInfo.add(") wurde eine negative Zahl oder");
				errorInfo.addInNextLine("0 angegeben. Bitte geben Sie eine positive Zahl an.");
			}
			if (abdiff <= 0) {
				errorInfo.addAsNewPar("Für den Fehler der Abbruchbedingung (");
				errorInfo.add("abortDist", true);
				errorInfo.add(") wurde eine negative Zahl oder");
				errorInfo.addInNextLine("0 angegeben. Bitte geben Sie eine positive Zahl an.");
			}
			if (abnum <= 0) {
				errorInfo.addAsNewPar("Für die Anzahl der Fehlerunterschreitungen der Abbruchbedingung (");
				errorInfo.add("abortSteps", true);
				errorInfo.add(")");
				errorInfo.addInNextLine("wurde eine negative Zahl oder 0 angegeben. Bitte geben Sie eine positive Zahl an.");
			}
			if (!correctNeedles) {
				errorInfo.addAsNewPar("Es wurden negative Nadellängen eingegeben. Bitte achten Sie darauf, dass in");
				errorInfo.addInNextLine("needles1", true);
				errorInfo.add("und");
				errorInfo.add("needles2", true);
				errorInfo.add("jeder Eintrag in einer ungeraden Spalte nichtnegativ ist.");
			}
			errorInfo.endItemize();
			
		}
		else {
			
			a.newRect(new Offset(-2, -2, "reference", "C"), new Coordinates(1800, 55), "headerBack", null,
					  PropertiesBuilder.createRectProperties(Color.WHITE, new Color(240, 240, 240), 100, false));
			a.newText(new Offset(17, 20, "reference", "NW"), title, "header", null, PropertiesBuilder.createTextProperties(new Color(60, 60, 60), new Font("SansSerif", 1, 24)));
			a.newRect(new Offset(-2, 57, "reference", "C"), new Coordinates(1800, 1400), "infoBack", null,
					  PropertiesBuilder.createRectProperties(Color.WHITE, new Color(220, 220, 220), 100, false));
			
			FormattedText startInfo = new FormattedText("errorInfo", a, new Offset(20, 15, "infoBack", "NW"), largeText, false, 0.5, 0.5);
			startInfo.beginItemize(FormattedText.ItemForm.CIRCLE, 20, new Color(100, 100, 100));
			startInfo.addAsNewPar("Ein Monte-Carlo-Verfahren zur Bestimmung von π.");
			startInfo.addAsNewPar("Autor: " + author);
			startInfo.addAsNewPar("Mai 2013");
			startInfo.endItemize();
			a.nextStep();

			startInfo.hide();
			a.nextStep();
			
			a.newRect(new Offset(17, 15, "infoBack", "NW"), new Offset(517, 40, "infoBack", "NW"), "headerIntro", null, headerRect);
			Text textIntro = a.newText(new Offset(10, 0, "headerIntro", "NW"), "Einführung", "textIntro", null, boldText);
			Rect rectIntro = a.newRect(new Offset(0, 0, "headerIntro", "SW"), new Offset(500, 515, "headerIntro", "SW"), "rectIntro", null, boxRect);
			
			if (withExplanation) {
				a.addLabel("Einführung: Monte-Carlo-Verfahren");
				FormattedText intro = new FormattedText("intro", a, new Offset(10, 10, "rectIntro", "NW"), normalText, true, 0.2, 0.8);
				intro.beginItemize(null, 15, Color.GRAY, 0.3, 0.8, 0);
				intro.addAsNewPar("Was ist ein Monte-Carlo-Verfahren?", true, null);
				intro.addInNextLine("Unter einem Monte-Carlo-Verfahren versteht man eine Simulation,");
				intro.addInNextLine("die wiederholt ein Zufallsexperiment durchführt. Ziel ist es, den");
				intro.addInNextLine("Erwartungswert der zugrunde liegenden Zufallsvariable zu bestimmen.");
				intro.addInNextLine("Dazu nutzt man einfach das Gesetz der großen Zahlen.");
				intro.addAsNewPar("Welche Probleme lassen sich mit Monte-Carlo-Verfahren", true, null);
				intro.addInNextLine("lösen?", true, null);
				intro.addInNextLine("Besonders gerne werden Monte-Carlo-Verfahren verwendet, um");
				intro.addInNextLine("numerisch Integralwerte zu bestimmen. Man kann nämlich jedes");
				intro.addInNextLine("Integral als Erwartungswert einer Zufallsvariable interpretieren.");
				intro.addAsNewPar("Woher weiß man, wann man nahe genug an der Lösung ist", true, null);
				intro.addInNextLine("und die Simulation beenden kann?", true, null);
				intro.addInNextLine("Den Fehler zwischen der berechneten Lösung und der tatsächlichen");
				intro.addInNextLine("Lösung kann man abschätzen, indem man zwei Monte-Carlo-Simu-");
				intro.addInNextLine("lationen parallel und unabhängig voneinander durchführt. In der");
				intro.addInNextLine("Regel nähern sich die Lösungen aus den beiden Simulationen");
				intro.addInNextLine("immer mehr an. Bleibt die Differenz über mehrere Iterationen unter");
				intro.addInNextLine("der gewünschten Schranke, kann das Monte-Carlo-Verfahren");
				intro.addInNextLine("beendet werden.");
				intro.addAsNewPar("Und wie bestimmen wir nun π?", true, null);
				intro.addInNextLine("Duch Nadelwerfen!");
				intro.endItemize();
				a.nextStep();
				intro.hide();
				a.nextStep();
			}
			
			a.newRect(new Offset(15, 0, "headerIntro", "NE"), new Offset(515, 0, "headerIntro", "SE"), "headerPlain", null, headerRect);
			a.newText(new Offset(10, 3, "headerPlain", "NW"), "Wurffläche", "textPlain", null, boldText);
			a.newRect(new Offset(0, 0, "headerPlain", "SW"), new Offset(500, 515, "headerPlain", "SW"), "rectPlain", null, boxRect);
			a.newRect(new Offset(36, 36, "rectPlain", "NW"), new Offset(-36, -36, "rectPlain", "SE"), "plainBorder", null,
					  PropertiesBuilder.createRectProperties(Color.WHITE, Color.WHITE, 50, false));
			newLine(new Offset(36, 0, "rectPlain", "NW"), new Offset(36, 0, "rectPlain", "SW"), "plainLine1", gridLine);
			newLine(new Offset(179, 0, "rectPlain", "NW"), new Offset(179, 0, "rectPlain", "SW"), "plainLine2", gridLine);
			newLine(new Offset(-179, 0, "rectPlain", "NE"), new Offset(-179, 0, "rectPlain", "SE"), "plainLine3", gridLine);
			newLine(new Offset(-36, 0, "rectPlain", "NE"), new Offset(-36, 0, "rectPlain", "SE"), "plainLine4", gridLine);
			
			if (withExplanation) {
				
				a.addLabel("Einführung: Buffon'sches Nadelwerfen");
				FormattedText given = new FormattedText("given", a, new Offset(10, 10, "rectIntro", "NW"), normalText, true, 0.2, 0.8);
				given.beginItemize(null, 15, Color.GRAY, 0.3, 0.8, 0);
				given.addAsNewPar("Gegeben seien ...", true, null);
				given.beginItemize(FormattedText.ItemForm.TRIANGLE, 15, Color.GRAY, 0.3, 0.2, 0);
				
				given.addAsNewPar("Eine Wurffläche mit parallelen Linien im Abstand", null, 0.3);
				given.add("d", false, true);
				given.add("(siehe rechts)");
				a.nextStep();
				
				PolylineProperties arrowLine = PropertiesBuilder.createPolylineProperties(new Color(180, 180, 180), true, true, 0, false);
				Polyline diffLine = newLine(new Offset(0, 0, "plainLine1", "C"), new Offset(0, 0, "plainLine2", "C"), "diffLine", arrowLine);
				TextProperties diffTextStyle = PropertiesBuilder.createTextProperties(new Color(180, 180, 180), new Font("SansSerif", Font.ITALIC, 14), true, 0, false);
				Text diffText = a.newText(new Offset(0, -22, "diffLine", "C"), "d", "diffText", null, diffTextStyle);
				
				given.addAsNewPar("Eine Nadel der Länge");
				given.add("d", null, true);
				given.add("/2", 0, false);
				a.nextStep();
				
				Polyline needle1 = newLine(new Offset(200, -150, "rectPlain", "W"), new Offset(272, -150, "rectPlain", "W"), "needle1", needle1Line);
				a.nextStep();
				
				Polyline needleLength1 =  newLine(new Offset(0, -30, "needle1", "W"), new Offset(0, 10, "needle1", "W"), "needleLength1", gridLine);
				Polyline needleLength2 =  newLine(new Offset(0, -30, "needle1", "E"), new Offset(0, 10, "needle1", "E"), "needleLength2", gridLine);
				Polyline needleLength3 = newLine(new Offset(0, -20, "needle1", "W"), new Offset(0, -20, "needle1", "E"), "needleLength3", arrowLine);
				Text needleLength4 = a.newText(new Offset(0, -22, "needleLength3", "C"), "d/2", "needleLength4", null, diffTextStyle);
				
				given.addAsNewPar("... die auch geknickt oder verbogen sein kann.");
				given.endItemize();
				a.nextStep();
				
				Node[] needle2Nodes = {new Offset(-13, 25, "plainLine3", "C"), new Offset(13, 0, "plainLine3", "C"), new Offset(-13, -25, "plainLine3", "C")};
				Polyline needle2 = a.newPolyline(needle2Nodes, "needle2", null, needle2Line);
				
				given.addAsNewPar("Wirft man die (gerade) Nadel auf die Fläche, so dass jede", true, null);
				given.addInNextLine("Position gleich wahrscheinlich ist, so beträgt die Wahr-", true, null);
				given.addInNextLine("scheinlichkeit, dass die Nadel eine der parallelen Linien", true, null);
				given.addInNextLine("schneidet, 1/π.", true, null);
				given.addAsNewPar("Für wiederholtes Nadelwerfen bedeutet das: Nach dem", true, null);
				given.addInNextLine("Gesetz der großen Zahlen nähert sich da Verhältnis", true, null);
				given.addInNextLine("Gesamtzahl der Würfe : Würfe mit Schnittpunkt", true, null, null, new Color(180, 30, 30));
				given.add("der Zahl π an.", true, null);
				given.addAsNewPar("Dieses Resultat lässt sich auf geknickte Nadeln übertragen.", true, null);
				a.nextStep();
				
				given.addInNextLine("Zwar ist hier die Wahrscheinlichkeit, dass die Nadel eine Linie");
				given.addInNextLine("schneidet, geringer. Aber auf der anderen Seite kann es sogar meh-");
				given.addInNextLine("rere Schnittpunkte geben (siehe unten). Wenn wir diese entspre-");
				given.addInNextLine("chend mehrfach zählen, gleichen sich die beiden Effekte aus.");
				Circle cut1 = a.newCircle(new Offset(0, 13, "plainLine3", "C"), 4, "cut1", null, cut);
				Circle cut2 = a.newCircle(new Offset(0, -13, "plainLine3", "C"), 4, "cut2", null, cut);
				
				given.addAsNewPar("Unser Algorithmus besteht demnach aus folgenden Schritten:", true, null);
				given.beginEnumerate("1.");
				given.addAsNewPar("Werfe", null, 0.3);
				given.add("n", null, true);
				given.add("Nadeln auf die Wurffläche.");
				given.addAsNewPar("Ermittle die Anzahl");
				given.add("k", null, true);
				given.add("der Schnittpunkte zwischen Nadeln und");
				given.addInNextLine("parallelen Linien.");
				given.addAsNewPar("Schätze π =");
				given.add("n/k", null, true);
				given.endEnumerate();
				given.endItemize();
				a.nextStep();
				
				given.hide();
				diffLine.hide(); diffText.hide();
				needle1.hide(); needle2.hide();
				needleLength1.hide(); needleLength2.hide();
				needleLength3.hide(); needleLength4.hide();
				cut1.hide(); cut2.hide();
				a.nextStep();
			
			}
			
			a.newRect(new Offset(0, 0, "headerIntro", "SW"), new Offset(500, 230, "headerIntro", "SW"), "rectGraph", null, boxRect);
			a.newRect(new Offset(0, 15, "rectGraph", "SW"), new Offset(0, 40, "rectGraph", "SE"), "headerCount", null, headerRect);
			Text textCount = a.newText(new Offset(10, 3, "headerCount", "NW"), "Zähler", "textCount", null, boldText);
			a.newRect(new Offset(0, 0, "headerCount", "SW"), new Offset(500, 245, "headerCount", "SW"), "rectCount", null, boxRect);
			rectIntro.hide();

			textIntro.setText("Schätzung von π im Verlauf", null, null);
			a.nextStep();
			
			a.addLabel("Beginn der Simulation");
			int maxsteps5 = (int) (Math.ceil((double) maxsteps / 5.0) * 5);
			a.newRect(new Offset(-15, 10, "rectGraph", "NE"), new Offset(30, -30, "rectGraph", "SW"), "plainGraph", null, whiteBoxRect);
			newLine(new Offset(91, 0, "plainGraph", "NW"), new Offset(91, 0, "plainGraph", "SW"), "graphLineX1", gridLine);
			newLine(new Offset(182, 0, "plainGraph", "NW"), new Offset(182, 0, "plainGraph", "SW"), "graphLineX2", gridLine);
			newLine(new Offset(-182, 0, "plainGraph", "NE"), new Offset(-182, 0, "plainGraph", "SE"), "graphLineX3", gridLine);
			newLine(new Offset(-91, 0, "plainGraph", "NE"), new Offset(-91, 0, "plainGraph", "SE"), "graphLineX4", gridLine);
			newLine(new Offset(0, 38, "plainGraph", "NW"), new Offset(0, 38, "plainGraph", "NE"), "graphLineY8", gridLine);
			newLine(new Offset(0, 76, "plainGraph", "NW"), new Offset(0, 76, "plainGraph", "NE"), "graphLineY6", gridLine);
			newLine(new Offset(0, -76, "plainGraph", "SW"), new Offset(0, -76, "plainGraph", "SE"), "graphLineY4", gridLine);
			newLine(new Offset(0, -38, "plainGraph", "SW"), new Offset(0, -38, "plainGraph", "SE"), "graphLineY2", gridLine);
			newLine(new Offset(0, -59, "plainGraph", "SW"), new Offset(0, -59, "plainGraph", "SE"), "graphLinePi", new PolylineProperties());
			FormattedText coordsY = new FormattedText("coordsY", a, new Offset(12, 0, "rectGraph", "NW"), coordText, false, 2.3, 2.3);
			coordsY.addAsNewPar("10");
			coordsY.beginItemize(FormattedText.ItemForm.NONE, 6, null);
			coordsY.addAsNewPar("8");
			coordsY.addAsNewPar("6");
			coordsY.addAsNewPar("4");
			coordsY.addAsNewPar("2");
			coordsY.addAsNewPar("0");
			a.newText(new Offset(0, 2, "plainGraph", "SW"), "0", "coordX0", null, coordText);
			a.newText(new Offset(0, 2, "graphLineX1", "S"), ((1*maxsteps5) / 5) + "", "coordX1", null, coordText);
			a.newText(new Offset(0, 2, "graphLineX2", "S"), ((2*maxsteps5) / 5) + "", "coordX2", null, coordText);
			a.newText(new Offset(0, 2, "graphLineX3", "S"), ((3*maxsteps5) / 5) + "", "coordX3", null, coordText);
			a.newText(new Offset(0, 2, "graphLineX4", "S"), ((4*maxsteps5) / 5) + "", "coordX4", null, coordText);
			a.newText(new Offset(0, 2, "plainGraph", "SE"), maxsteps5 + "", "coordX5", null, coordText);
			
			DecimalFormat format = new DecimalFormat("#0.000000");
			Vector<Primitive> boxPrimitives = new Vector<Primitive>();
			newInfoBox("Simulation", "1", "boxSimu1", new Offset(10, 10, "rectCount", "NW"), 85, boxPrimitives);
			Text boxWurf1 = newInfoBox("Würfe (maximal " + maxsteps * needles1.length + ")", "0", "boxWurf1",
					                   new Offset(5, 0, "boxSimu11", "NE"), 390, boxPrimitives);
			Text boxSchnitt1 = newInfoBox("Schnitte", "0", "boxSchnitt1", new Offset(0, 5, "boxWurf12", "SW"), 390, boxPrimitives);
			Text boxPi1 = newInfoBox("Schätzung f. π", "...", "boxPi1", new Offset(0, 5, "boxSimu12", "SW"), 85, boxPrimitives);
			newInfoBox("Simulation", "2", "boxSimu2", new Offset(0, 5, "boxPi12", "SW"), 85, boxPrimitives);
			Text boxWurf2 = newInfoBox("Würfe (maximal " + maxsteps * needles2.length + ")", "0", "boxWurf2",
					                   new Offset(5, 0, "boxSimu21", "NE"), 390, boxPrimitives);
			Text boxSchnitt2 = newInfoBox("Schnitte", "0", "boxSchnitt2", new Offset(0, 5, "boxWurf22", "SW"), 390, boxPrimitives);
			Text boxPi2 = newInfoBox("Schätzung f. π", "...", "boxPi2", new Offset(0, 5, "boxSimu22", "SW"), 85, boxPrimitives);
			Text boxFehler = newInfoBox("Fehler", "...", "boxFehler", new Offset(0, 5, "boxPi22", "SW"), 85, boxPrimitives);
			newInfoBox("Delta", format.format(abdiff), "boxDelta", new Offset(5, 0, "boxFehler1", "NE"), 85, boxPrimitives);
			Text boxFehlerDia = newInfoBox("Unterschreit. von Delta in Folge (Abbruch bei " + abnum +")", "0", "boxFehlerDia",
					                       new Offset(5, 0, "boxDelta1", "NE"), 300, boxPrimitives);
			
			boxPrimitives.add(a.newRect(new Offset(2, 2, "boxSimu11", "NW"), new Offset(-2, -2, "boxSimu11", "SE"), "boxSimBack", null, bar1));
			boxPrimitives.add(a.newRect(new Offset(2, 2, "boxSimu21", "NW"), new Offset(-2, -2, "boxSimu21", "SE"), "boxSimBack", null, bar2));
			boxPrimitives.add(a.newRect(new Offset(2, 2, "boxFehler1", "NW"), new Offset(-2, -2, "boxFehler1", "SE"), "boxSimBack", null, barError));
			a.nextStep();
			
			for (int i = 0; i < maxsteps; i++) {
				
				Vector<Node> cuts1 = new Vector<Node>(), cuts2 = new Vector<Node>();
				Vector<Polyline> needleLine1 = throwNeedles(needleSet1, boxWurf1, cuts1, 0, stepwise);
				a.nextStep();
				Vector<Polyline> needleLine2 = throwNeedles(needleSet2, boxWurf2, cuts2, 1, stepwise);
				a.nextStep();
				
				Vector<Circle> circles1 = showCuts(cuts1, color1, boxSchnitt1, 0);
				a.nextStep();
				Vector<Circle> circles2 = showCuts(cuts2, color2, boxSchnitt2, 1);
				a.nextStep();
				
				Double pi1Old = (double) throwN[0] / (double) cutN[0];
				Double pi2Old = (double) throwN[1] / (double) cutN[1];
	
				setBar("Wurf1", throwN[0], throwN[0] += needles1.length, maxsteps * needles1.length, boxWurf1, bar1);
				setBar("Wurf2", throwN[1], throwN[1] += needles2.length, maxsteps * needles2.length, boxWurf2, bar2);
				setBar("Schnitt1", cutN[0], cutN[0] += cuts1.size(), maxsteps * needles1.length, boxSchnitt1, bar1);
				setBar("Schnitt2", cutN[1], cutN[1] += cuts2.size(), maxsteps * needles2.length, boxSchnitt2, bar2);
				
				Double pi1 = (double) throwN[0] / (double) cutN[0];
				Double pi2 = (double) throwN[1] / (double) cutN[1];
				Double piE = Math.abs(pi1-pi2);
				int exitOld = exiter;
				if (piE <= abdiff) exiter ++;
				else exiter = 0;
				setBar("FehlerDia", exitOld, exiter, abnum, boxFehlerDia, barError);
				
				if (i > 0) {
					addGraphLine(i+1, maxsteps5, pi1Old, pi1, color1);
					addGraphLine(i+1, maxsteps5, pi2Old, pi2, color2);
				}
				
				boxPi1.setText(pi1.isInfinite() ? "unendl." : format.format(pi1), null, null);
				boxPi2.setText(pi2.isInfinite() ? "unendl." : format.format(pi2), null, null);
				boxFehler.setText(piE.isInfinite() || piE.isNaN() ? "unendl." : format.format(piE), null, null);
				hideNeedles(needleLine1, circles1);
				hideNeedles(needleLine2, circles2);
				a.nextStep();
				if (exiter >= abnum) break;
				
			}
			
			a.addLabel("Fazit");
			Double pi1 = (double) throwN[0] / (double) cutN[0];
			Double pi2 = (double) throwN[1] / (double) cutN[1];
			Double pi = (pi1 + pi2) / 2.0;
			setBar("Wurf1", throwN[0], 0, maxsteps * needles1.length, boxWurf1, bar1);
			setBar("Wurf2", throwN[1], 0, maxsteps * needles2.length, boxWurf2, bar2);
			setBar("Schnitt1", cutN[0], 0, maxsteps * needles1.length, boxSchnitt1, bar1);
			setBar("Schnitt2", cutN[1], 0, maxsteps * needles2.length, boxSchnitt2, bar2);
			setBar("FehlerDia", exiter, 0, abnum, boxFehlerDia, barError);
			for (Primitive prim : boxPrimitives) prim.hide();
			textCount.setText("Fazit", null, null);
			FormattedText concl = new FormattedText("concl", a, new Offset(10, 10, "rectCount", "NW"), normalText, true, 0.2, 0.8);
			if (exiter >= abnum) concl.addAsNewPar("Die Abbruchbedingung ist erfüllt.", true, false);
			else concl.addAsNewPar("Die maximale Anzahl der Schritte ist erreicht.", true, false);
			concl.addInNextLine("Die Simulation wurde damit beendet.", true, false);
			concl.addAsNewPar("Als Schätzungen für π erhalten wir " + format.format(pi1) + " und " + format.format(pi2) + ", also");
			concl.addInNextLine("im Mittelwert " + format.format(pi) + ".");
			if (Math.abs(pi-Math.PI) <= abdiff) concl.addAsNewPar("Damit liegt der ermittele Wert innerhalb der gewünschten Fehlergrenze.");
			else {
				concl.addAsNewPar("Damit liegt der ermittele Wert leider außerhalb der gewünschten");
				concl.addInNextLine("Fehlergrenze.");
			}
		}
		
	}
	
	private Vector<Needle> initNeedles(int[][] needles, Brush color) {
		Vector<Needle> needleSet = new Vector<Needle>();
		for (int i = 0; i < needles.length; i++) {
			Needle needle = new Needle(needles[i], color.darker(0.2, 0.2), i+1);
			needleSet.add(needle);
		}
		return needleSet;
	}
	
	private Vector<Polyline> throwNeedles(Vector<Needle> needleSet, Text numberBox, Vector<Node> cuts, int index, boolean stepwise) {
		Vector<Polyline> needleLines = new Vector<Polyline>();
		for (Needle needle : needleSet) {
			Polyline needleLine = needle.throwNeedle(a);
			if (stepwise) a.nextStep();
			needleLines.add(needleLine);
			cuts.addAll(needle.getCuts());
		}
		numberBox.setText(throwN[index] + " + " + needleSet.size(), null, null);
		return needleLines;
	}
	
	private Vector<Circle> showCuts(Vector<Node> cuts, Brush color, Text numberBox, int index) {
		Vector<Circle> circles = new Vector<Circle>();
		for (Node cut : cuts) {
			circles.add(a.newCircle(cut, 4, "cut" + cut.hashCode(), null,
					    PropertiesBuilder.createCircleProperties(Color.WHITE, color.brighter(0.6, 0.3), 20, false)));
		}
		numberBox.setText(cutN[index] + " + " + cuts.size(), null, null);
		return circles;
	}
	
	private void hideNeedles(Vector<Polyline> needles, Vector<Circle> cuts) {
		for (Polyline needle : needles) needle.hide();
		for (Circle cut : cuts) cut.hide();
	}
	
	private void setBar(String keyword, int numberOld, int numberNew, int max, Text numberBox, RectProperties style) {
		String box = "box" + keyword + "1";
		String bar = "bar" + keyword + "_";
		int barLengthOld = (int) Math.round(((double) numberOld/ (double) max) * (keyword == "FehlerDia" ? 296 : 386));
		int barLengthNew = (int) Math.round(((double) numberNew/ (double) max) * (keyword == "FehlerDia" ? 296 : 386));
		if (barLengthOld > 0) a.addLine("hide \"" + bar + barLengthOld + "\"");
		if (a.isNameUsed(bar + barLengthNew)) a.addLine("show \"" + bar + barLengthNew + "\"");
		else if (barLengthNew > 0) a.newRect(new Offset(2, 2, box, "NW"), new Offset(2 + barLengthNew, -2, box, "SW"), bar + barLengthNew, null, style);
		numberBox.setText(numberNew + "", null, null);
	}
	
	private void addGraphLine(int step, int maxsteps, double from, double to, Color color) {
		Offset posFrom = getGraphPos(step - 1, maxsteps, from);
		Offset posTo = getGraphPos(step, maxsteps, to);
		if (posFrom.getY() > -191 || posTo.getY() > -191) {
			if (posFrom.getY() < -191) posFrom = new Offset(posFrom.getX() + (int) Math.round(((double) (posFrom.getY() + 191) / posFrom.getY()) *
				                                           (456.0 / (double) maxsteps)), -191, "plainGraph", "SW");
			if (posTo.getY() < -191) posTo = new Offset(posTo.getX() - (int) Math.round(((double) (posTo.getY() + 191) / posTo.getY()) *
                    (456.0 / (double) maxsteps)), -191, "plainGraph", "SW");
			newLine(posFrom, posTo, "pLine" + "_" + step, PropertiesBuilder.createPolylineProperties(color));
		}
	}
	
	private Offset getGraphPos(double x, double xfactor, double y) {
		return new Offset((int) Math.round((x / xfactor) * 456), (Double.isInfinite(y) ?
			-2000 : (int) Math.round((y / 10.0) * -191)), "plainGraph", "SW");
	}
	
	private Polyline newLine(Node start, Node end, String name, PolylineProperties props) {
		Node[] nodes = {start, end};
		return a.newPolyline(nodes, name, null, props);
	}
	
	private Text newInfoBox(String descript, String content, String name, Offset pos, int width, Vector<Primitive> boxPrimitives) {
		boxPrimitives.add(a.newRect(pos, new Offset(pos.getX() + width, pos.getY() + 25, pos.getBaseID(), pos.getDirection()),
				          name + "1", null, whiteBoxRect));
		boxPrimitives.add(a.newRect(new Offset(0, 0, name + "1", "SW"), new Offset(0, 16, name + "1", "SE"), name + "2", null, boxRect));
		boxPrimitives.add(a.newText(new Offset(0, -11, name + "2", "C"), descript, name + "4", null, coordText));
		boxPrimitives.add(a.newText(new Offset(7, 7, name + "1", "NW"), content, name + "3", null, boldText));
		return (Text) boxPrimitives.lastElement();
	}
	
	@Override
	public String toString() {
		return a.toString();
	}
	
}
