/*
 * BankierGen.java
 * Aiko Westmeier, Manuel Stork, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.hardware;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import algoanim.animalscript.AnimalScript;
import algoanim.executors.formulaparser.Node;
import algoanim.primitives.IntArray;
import algoanim.primitives.StringArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import algoanim.properties.meta.*;

public class BankierGen implements ValidatingGenerator {
    private static Language lang;
    private int[] BelegteRessourcenC2;
    private int[] BenoetigteRessourcenR1;
    private int[] BelegteRessourcenC3;
    private int[] BenoetigteRessourcenR2;
    private int[] BenoetigteRessourcenR3;
    private int[] BelegteRessourcenC1;
    private int[] BenoetigteRessourcenR4;
    private int[] GesamteRessourcen;
    private int[] BelegteRessourcenC4;
    private static ArrayProperties arrayProperties;
    private static PolylineProperties arrowProperties;
	
	private static ArrayProperties arrayProps;
	private static TextProperties textProps;
	private static TextProperties textProps2;
	private static TextProperties textProps3;
	private static TextProperties textProps4;
	private static TextProperties textProps5;
	private static TextProperties textPropsHeader;
	private static TextProperties textPropsDeadlock;
	private static TextProperties textPropsNoDeadlock;
	private static PolylineProperties polyPropsLine;
	private static PolylineProperties polyPropsArrow;
	private static CircleProperties circleProps;
	private static RectProperties rectProps;
    


    public void init(){
        lang = new AnimalScript("Bankieralgorithmus", "Aiko Westmeier, Manuel Stork", 1200, 1000);
		
		// Activate step control
		lang.setStepMode(true);
				
		// rect properties
		rectProps = new RectProperties();
		rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
		rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		
		// array properties 
		arrayProps = new ArrayProperties();
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLUE);		// color red
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);					// filled
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);		// fill color gray
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
		// highlight cell	
		
		// text properties
		textProps = new TextProperties();
		textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		textProps2 = new TextProperties();
		textProps2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
		textProps3 = new TextProperties();
		textProps3.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		textProps4 = new TextProperties();
		textProps4.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
		textProps5 = new TextProperties();
		textProps5.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		textProps5.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 16));
		
		textPropsHeader = new TextProperties();
		textPropsHeader.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		textPropsHeader.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 30) );
		
		textPropsDeadlock = new TextProperties();
		textPropsDeadlock.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		textPropsDeadlock.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 24) );
		
		textPropsNoDeadlock = new TextProperties();
		textPropsNoDeadlock.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.GREEN);
		textPropsNoDeadlock.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 24) );
		
		// polyline properties
		polyPropsLine = new PolylineProperties();
		polyPropsLine.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		polyPropsArrow = new PolylineProperties();
		polyPropsArrow.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		polyPropsArrow.set(AnimationPropertiesKeys.BWARROW_PROPERTY, true);
			
		// circle properties
		circleProps = new CircleProperties();
		circleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		circleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    }
    
	public static void bankier(int[] E, int[] A, int[] c1, int[] c2, int[] c3, int[] c4, int[] r1, int[] r2, int[] r3,
			int[] r4) {
		int[] arrayTermPro = new int[4];
		int[] arrayProI = new int[1];

		// declare a default duration for swap effects
		Timing defaultTiming = new TicksTiming(15);

		// header
		algoanim.util.Node rect1 = new Coordinates(10, 10);
		algoanim.util.Node rect2 = new Coordinates(380, 50);
		Rect headerRect = lang.newRect(rect1, rect2, "rect", null, rectProps);
		Text textHeader = lang.newText(new Coordinates(25, 25), "Bankier-Algorithmus", "Name", null, textPropsHeader);

		Text textIntro2 = lang.newText(new Coordinates(20, 120),
				"Der Bankieralgorithmus ist ein Algorithmus,der zur Vermeidung von Verklemmungen (Deadlocks)", "Name2",
				null, textProps5);
		Text textIntro3 = lang.newText(new Coordinates(20, 140),
				"bei der Vergabe von Systemressourcen benutzt wird. Dabei sind dem Algorithmus die gesamten", "Name3",
				null, textProps5);
		Text textIntro4 = lang.newText(new Coordinates(20, 160),
				"Ressourcen und die auszuführenden Prozesse bekannt. Über die Prozesse sind folgende Eigenschaften",
				"Name4", null, textProps5);
		Text textIntro5 = lang.newText(new Coordinates(20, 180),
				"bekannt: Ressourcen, die von den Prozessen bereits besetzt sind und Ressourcen die von den", "Name5",
				null, textProps5);
		Text textIntro6 = lang.newText(new Coordinates(20, 200), "Prozessen noch benötigt werden. ", "Name6", null,
				textProps5);
		Text textIntro7 = lang.newText(new Coordinates(20, 220),
				"Aus den gesamten Ressourcen und den belegten Ressourcen der Prozesse errechnen sich die", "Name7",
				null, textProps5);
		Text textIntro1 = lang.newText(new Coordinates(20, 240),
				"verfügbaren Ressourcen. Sofern die Ausführung möglich ist (genügend verfügbare Ressourcen", "Name1",
				null, textProps5);
		Text textIntro8 = lang.newText(new Coordinates(20, 260),
				"vorhanden), werden die Prozesse nacheinander abgearbeitet. Nachdem ein Prozess abgearbeitet", "Name1",
				null, textProps5);
		Text textIntro9 = lang.newText(new Coordinates(20, 280),
				"wurde, werden dessen belegte Ressourcen den verfügbaren Ressourcen hinzugeführt.", "Name1", null,
				textProps5);
		Text textIntro11 = lang.newText(new Coordinates(20, 320),
				"Nach dem Ausführen des Bankieralgorithmus steht fest, ob durch eine bestimmte Reihenfolge", "Name1",
				null, textProps5);
		Text textIntro12 = lang.newText(new Coordinates(20, 340),
				"der Ausführung der Prozesse eine Verklemmung vermeidbar ist.", "Name1", null, textProps5);

		Text textIntro13 = lang.newText(new Coordinates(20, 400),
				"Verklemmung/Deadlock: Ein Deadlock beschreibt in der Informatik einen Zustand eines", "Name1", null,
				textProps5);
		Text textIntro14 = lang.newText(new Coordinates(20, 420),
				"System, bei dem eine zyklische Wartesituation zwischen mehreren Prozessen auftritt,", "Name1", null,
				textProps5);
		Text textIntro15 = lang.newText(new Coordinates(20, 440),
				"wobei kein beteiligter Prozess mehr abgearbeitet werden kann und auf die Freigabe von", "Name1", null,
				textProps5);
		Text textIntro16 = lang.newText(new Coordinates(20, 460),
				"Betriebsmitteln wartet, welche von anderen beteiligten Prozessen belegt sind.", "Name1", null,
				textProps5);

		lang.nextStep("Einleitung");
		textIntro1.hide();
		textIntro2.hide();
		textIntro3.hide();
		textIntro4.hide();
		textIntro5.hide();
		textIntro6.hide();
		textIntro7.hide();
		textIntro8.hide();
		textIntro9.hide();
		textIntro15.hide();
		textIntro11.hide();
		textIntro12.hide();
		textIntro13.hide();
		textIntro14.hide();
		textIntro16.hide();

		// Source Code
		SourceCodeProperties scProps = new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));

		scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		SourceCode sc = lang.newSourceCode(new Coordinates(400, 70), "sourceCode", null, scProps);

		sc.addCodeLine("bankierAlgorithmus(Gesamte Ressourcen 'E', Verfügbare Ressourcen 'A'", null, 0, null); // 0
		sc.addCodeLine("Belegte Ressourcen'C1-C4', Benötigte Ressourcen 'R1-R4')", null, 11, null); // 1
		sc.addCodeLine("{", null, 0, null); // 2
		sc.addCodeLine("kein Deadlock vorhanden", null, 1, null); // 3
		sc.addCodeLine("solange (terminierte Prozesse < vorhandene Prozesse und kein Deadlock vorhanden)", null, 1, null); // 4
		sc.addCodeLine("{", null, 1, null); // 5
		sc.addCodeLine("Setze Deadlock auf true;", null, 2, null); // 6
		sc.addCodeLine("for (i = 1; i < 5; i++)", null, 2, null); // 7
		sc.addCodeLine("{", null, 2, null); // 8
		sc.addCodeLine("falls (Prozess(i) schon in terminierte Prozesse)", null, 3, null); // 9
		sc.addCodeLine("{", null, 4, null); // 10
		sc.addCodeLine("fahre fort", null, 4, null); // 11
		sc.addCodeLine("}", null, 4, null); // 12
		sc.addCodeLine("ansonsten falls (genügend Ressourcen für Prozess (i) vorhanden)", null, 3, null); // 13
		sc.addCodeLine("{", null, 4, null); // 14
		sc.addCodeLine("füge Prozess(i) zu terminierteProzesse", null, 4, null); // 15
		sc.addCodeLine("gebe Ressourcen von Prozess (i) frei", null, 4, null); // 16
		sc.addCodeLine("setze Deadlock auf false", null, 4, null); // 17
		sc.addCodeLine("}", null, 4, null); // 18
		sc.addCodeLine("}", null, 2, null); // 19
		sc.addCodeLine("}", null, 1, null); // 20
		sc.addCodeLine("gebe Zustand des Systems zurück", null, 1, null); // 21
		sc.addCodeLine("}", null, 0, null); // 22
		// ---------------------------------------------------------------------
		// arrows
		algoanim.util.Node[] arrayPolyTotal = new algoanim.util.Node[2];
		;
		arrayPolyTotal[0] = new Coordinates(150, 100);
		arrayPolyTotal[1] = new Coordinates(200, 100);
		Polyline arrowTotal = lang.newPolyline(arrayPolyTotal, "Name", null, polyPropsArrow);
		// ------------------------
		algoanim.util.Node[] arrayPolyC1 = new algoanim.util.Node[2];
		;
		arrayPolyC1[0] = new Coordinates(150, 160);
		arrayPolyC1[1] = new Coordinates(200, 160);
		Polyline arrowC1 = lang.newPolyline(arrayPolyC1, "Name", null, polyPropsArrow);
		// ------------------------
		algoanim.util.Node[] arrayPolyC2 = new algoanim.util.Node[2];
		;
		arrayPolyC2[0] = new Coordinates(150, 200);
		arrayPolyC2[1] = new Coordinates(200, 200);
		Polyline arrowC2 = lang.newPolyline(arrayPolyC2, "Name", null, polyPropsArrow);
		// ------------------------
		algoanim.util.Node[] arrayPolyC3 = new algoanim.util.Node[2];
		;
		arrayPolyC3[0] = new Coordinates(150, 240);
		arrayPolyC3[1] = new Coordinates(200, 240);
		Polyline arrowC3 = lang.newPolyline(arrayPolyC3, "Name", null, polyPropsArrow);
		// ------------------------
		algoanim.util.Node[] arrayPolyC4 = new algoanim.util.Node[2];
		;
		arrayPolyC4[0] = new Coordinates(150, 280);
		arrayPolyC4[1] = new Coordinates(200, 280);
		Polyline arrowC4 = lang.newPolyline(arrayPolyC4, "Name", null, polyPropsArrow);
		// ------------------------
		algoanim.util.Node[] arrayPolyR1 = new algoanim.util.Node[2];
		;
		arrayPolyR1[0] = new Coordinates(150, 340);
		arrayPolyR1[1] = new Coordinates(200, 340);
		Polyline arrowR1 = lang.newPolyline(arrayPolyR1, "Name", null, polyPropsArrow);
		// ------------------------
		algoanim.util.Node[] arrayPolyR2 = new algoanim.util.Node[2];
		;
		arrayPolyR2[0] = new Coordinates(150, 380);
		arrayPolyR2[1] = new Coordinates(200, 380);
		Polyline arrowR2 = lang.newPolyline(arrayPolyR2, "Name", null, polyPropsArrow);
		// ------------------------
		algoanim.util.Node[] arrayPolyR3 = new algoanim.util.Node[2];
		;
		arrayPolyR3[0] = new Coordinates(150, 420);
		arrayPolyR3[1] = new Coordinates(200, 420);
		Polyline arrowR3 = lang.newPolyline(arrayPolyR3, "Name", null, polyPropsArrow);
		// ------------------------
		algoanim.util.Node[] arrayPolyR4 = new algoanim.util.Node[2];
		;
		arrayPolyR4[0] = new Coordinates(150, 460);
		arrayPolyR4[1] = new Coordinates(200, 460);
		Polyline arrowR4 = lang.newPolyline(arrayPolyR4, "Name", null, polyPropsArrow);
		// ------------------------
		algoanim.util.Node[] arrayPolyAvail = new algoanim.util.Node[2];
		;
		arrayPolyAvail[0] = new Coordinates(150, 520);
		arrayPolyAvail[1] = new Coordinates(200, 520);
		Polyline arrowAvail = lang.newPolyline(arrayPolyAvail, "Name", null, polyPropsArrow);
		// ------------------------
		algoanim.util.Node[] arrayPolyDeadlock = new algoanim.util.Node[2];
		;
		arrayPolyDeadlock[0] = new Coordinates(150, 600);
		arrayPolyDeadlock[1] = new Coordinates(200, 600);
		Polyline arrowDeadlock = lang.newPolyline(arrayPolyDeadlock, "Name", null, polyPropsArrow);
		// ------------------------
		algoanim.util.Node[] arrayPolyTerm = new algoanim.util.Node[2];
		;
		arrayPolyTerm[0] = new Coordinates(150, 650);
		arrayPolyTerm[1] = new Coordinates(200, 650);
		Polyline arrowTerm = lang.newPolyline(arrayPolyTerm, "Name", null, polyPropsArrow);
		// ------------------------
		arrowTotal.hide();
		arrowC1.hide();
		arrowC2.hide();
		arrowC3.hide();
		arrowC4.hide();
		arrowR1.hide();
		arrowR2.hide();
		arrowR3.hide();
		arrowR4.hide();
		arrowAvail.hide();
		arrowDeadlock.hide();
		arrowTerm.hide();

		// ------------------------
		algoanim.util.Node[] PolyC1 = new algoanim.util.Node[2];
		;
		PolyC1[0] = new Coordinates(38, 162);
		PolyC1[1] = new Coordinates(110, 162);
		Polyline lineC1 = lang.newPolyline(PolyC1, "Name", null, polyPropsLine);
		// ------------------------
		algoanim.util.Node[] PolyC2 = new algoanim.util.Node[2];
		;
		PolyC2[0] = new Coordinates(38, 202);
		PolyC2[1] = new Coordinates(110, 202);
		Polyline lineC2 = lang.newPolyline(PolyC2, "Name", null, polyPropsLine);
		// ------------------------
		algoanim.util.Node[] PolyC3 = new algoanim.util.Node[2];
		;
		PolyC3[0] = new Coordinates(38, 242);
		PolyC3[1] = new Coordinates(110, 242);
		Polyline lineC3 = lang.newPolyline(PolyC3, "Name", null, polyPropsLine);
		// ------------------------
		algoanim.util.Node[] PolyC4 = new algoanim.util.Node[2];
		;
		PolyC4[0] = new Coordinates(38, 282);
		PolyC4[1] = new Coordinates(110, 282);
		Polyline lineC4 = lang.newPolyline(PolyC4, "Name", null, polyPropsLine);
		// ------------------------
		// ------------------------
		algoanim.util.Node[] PolyR1 = new algoanim.util.Node[2];
		;
		PolyR1[0] = new Coordinates(38, 342);
		PolyR1[1] = new Coordinates(110, 342);
		Polyline lineR1 = lang.newPolyline(PolyR1, "Name", null, polyPropsLine);
		// ------------------------
		algoanim.util.Node[] PolyR2 = new algoanim.util.Node[2];
		;
		PolyR2[0] = new Coordinates(38, 382);
		PolyR2[1] = new Coordinates(110, 382);
		Polyline lineR2 = lang.newPolyline(PolyR2, "Name", null, polyPropsLine);
		// ------------------------
		algoanim.util.Node[] PolyR3 = new algoanim.util.Node[2];
		;
		PolyR3[0] = new Coordinates(38, 422);
		PolyR3[1] = new Coordinates(110, 422);
		Polyline lineR3 = lang.newPolyline(PolyR3, "Name", null, polyPropsLine);
		// ------------------------
		algoanim.util.Node[] PolyR4 = new algoanim.util.Node[2];
		;
		PolyR4[0] = new Coordinates(38, 462);
		PolyR4[1] = new Coordinates(110, 462);
		Polyline lineR4 = lang.newPolyline(PolyR4, "Name", null, polyPropsLine);
		// ------------------------
		lineC1.hide();
		lineC2.hide();
		lineC3.hide();
		lineC4.hide();
		lineR1.hide();
		lineR2.hide();
		lineR3.hide();
		lineR4.hide();
		
		// wrap int[] to IntArray instance
		// total Ressources E
		lang.nextStep();
		Text textTotal = lang.newText(new Coordinates(40, 70), "Gesamte Ressourcen:", "Name", null, textProps);
		Text textE = lang.newText(new Coordinates(20, 90), "E", "Name", null, textProps);
		IntArray arrayTotalRes = lang.newIntArray(new Coordinates(40, 90), E, "array", null, arrayProps);
		arrayTotalRes.showIndices(false, null, null);

		// processes c1-c4
		Text textUsed = lang.newText(new Coordinates(40, 130), "von Prozess i belegte Ressourcen:", "Name", null,
				textProps);
		Text textc1 = lang.newText(new Coordinates(20, 150), "C1", "Name", null, textProps2);
		IntArray used1 = lang.newIntArray(new Coordinates(40, 150), c1, "c1", null, arrayProps);
		used1.showIndices(false, null, null);
		Text textc2 = lang.newText(new Coordinates(20, 190), "C2", "Name", null, textProps2);
		IntArray used2 = lang.newIntArray(new Coordinates(40, 190), c2, "c2", null, arrayProps);
		used2.showIndices(false, null, null);
		Text textc3 = lang.newText(new Coordinates(20, 230), "C3", "Name", null, textProps2);
		IntArray used3 = lang.newIntArray(new Coordinates(40, 230), c3, "c3", null, arrayProps);
		used3.showIndices(false, null, null);
		Text textc4 = lang.newText(new Coordinates(20, 270), "C4", "Name", null, textProps2);
		IntArray used4 = lang.newIntArray(new Coordinates(40, 270), c4, "c4", null, arrayProps);
		used4.showIndices(false, null, null);

		// processes r1-r4
		Text textNeeded = lang.newText(new Coordinates(40, 310), "von Prozess i noch benötigte Ressourcen:", "Name",
				null, textProps);
		Text textr1 = lang.newText(new Coordinates(20, 330), "R1", "Name", null, textProps2);
		IntArray need1 = lang.newIntArray(new Coordinates(40, 330), r1, "r1", null, arrayProps);
		need1.showIndices(false, null, null);
		Text textr2 = lang.newText(new Coordinates(20, 370), "R2", "Name", null, textProps2);
		IntArray need2 = lang.newIntArray(new Coordinates(40, 370), r2, "r2", null, arrayProps);
		need2.showIndices(false, null, null);
		Text textr3 = lang.newText(new Coordinates(20, 410), "R3", "Name", null, textProps2);
		IntArray need3 = lang.newIntArray(new Coordinates(40, 410), r3, "r3", null, arrayProps);
		need3.showIndices(false, null, null);
		Text textr4 = lang.newText(new Coordinates(20, 450), "R4", "Name", null, textProps2);
		IntArray need4 = lang.newIntArray(new Coordinates(40, 450), r4, "r4", null, arrayProps);
		need4.showIndices(false, null, null);

		// available ressources
		lang.nextStep("Start des Algorithmus");
		Text textAvail = lang.newText(new Coordinates(40, 490), "Verfügbare Ressourcen:", "Name", null, textProps);
		Text textA = lang.newText(new Coordinates(20, 510), "A", "Name", null, textProps);
		IntArray availRes = lang.newIntArray(new Coordinates(40, 510), A, "A", null, arrayProps);
		availRes.showIndices(false, null, null);

		Text textRes2 = lang.newText(new Coordinates(200, 490), "Nicht genug Ressourcen vorhanden!", "Name", null,
				textProps3);
		textRes2.hide();
		Text textRes1 = lang.newText(new Coordinates(200, 490), "Genügend Ressourcen vorhanden!", "Name", null,
				textProps4);
		textRes1.hide();

		// determine start ressources
		int[] result = new int[4];
		for (int i = 0; i < 4; i++) {
			lang.nextStep();
			arrayTotalRes.unhighlightCell(i - 1, null, null);
			used1.unhighlightCell(i - 1, null, null);
			used2.unhighlightCell(i - 1, null, null);
			used3.unhighlightCell(i - 1, null, null);
			used4.unhighlightCell(i - 1, null, null);
			availRes.unhighlightCell(i - 1, null, null);
			arrayTotalRes.highlightCell(i, null, null);
			used1.highlightCell(i, null, null);
			used2.highlightCell(i, null, null);
			used3.highlightCell(i, null, null);
			used4.highlightCell(i, null, null);
			availRes.highlightCell(i, null, null);
			result[i] = E[i] - c1[i] - c2[i] - c3[i] - c4[i];
			availRes.put(i, result[i], null, null);
		}
		for (int j = 0; j < 4; j++) {
			A[j] = E[j] - c1[j] - c2[j] - c3[j] - c4[j];
		}

		// polyline
		lang.nextStep();
		arrayTotalRes.unhighlightCell(3, null, null);
		used1.unhighlightCell(3, null, null);
		used2.unhighlightCell(3, null, null);
		used3.unhighlightCell(3, null, null);
		used4.unhighlightCell(3, null, null);
		availRes.unhighlightCell(3, null, null);
		algoanim.util.Node[] arrayPoly3 = new algoanim.util.Node[2];
		;
		arrayPoly3[0] = new Coordinates(20, 550);
		arrayPoly3[1] = new Coordinates(300, 550);
		Polyline polySplit = lang.newPolyline(arrayPoly3, "Name", null, polyPropsLine);

		// deadlock circle
		Text textDeadlock = lang.newText(new Coordinates(40, 570), "Deadlock: ", "Name", null, textProps);
		Circle deadlockCircle = lang.newCircle(new Coordinates(60, 600), 10, "Circle", null, circleProps);

		// terminated processes
		lang.nextStep();
		Text textTermPro = lang.newText(new Coordinates(40, 620), "Terminierte Prozesse:", "Name", null, textProps);
		IntArray termPro = lang.newIntArray(new Coordinates(40, 640), arrayTermPro, "arrayTermPro", null, arrayProps);
		termPro.showIndices(false, null, null);

		// Prozess i
		lang.nextStep();
		Text textI = lang.newText(new Coordinates(40, 680), "Prozess i:", "Name", null, textProps);
		IntArray arrayI = lang.newIntArray(new Coordinates(40, 700), arrayProI, "arrayI", null, arrayProps);
		arrayI.showIndices(false, null, null);

		// start bankier
		lang.nextStep();
		sc.highlight(0); // bankierAlgo (...
		sc.highlight(1); // ... )
		sc.highlight(2); // {
		sc.highlight(22); // }

		// init deadlock = false
		lang.nextStep();
		arrowDeadlock.show();
		sc.unhighlight(0); // bankierAlgo (...
		sc.unhighlight(1); // ... )
		sc.unhighlight(2); // {
		sc.highlight(3); // deadlock = false;
		sc.unhighlight(22); // }
		deadlockCircle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN, null, null);
		textDeadlock.setText("Kein Deadlock vorhanden!", null, null);
		boolean deadlock = false;
		int termProCount = 0;

		while (termProCount < 4 && !deadlock) {
			// while
			lang.nextStep();
			arrowDeadlock.hide();
			arrayI.put(0, 0, null, null);
			arrowTerm.show();
			arrowDeadlock.show();
			need1.unhighlightCell(0, 3, null, null);
			need2.unhighlightCell(0, 3, null, null);
			need3.unhighlightCell(0, 3, null, null);
			need4.unhighlightCell(0, 3, null, null);
			arrowR1.hide();
			arrowR2.hide();
			arrowR3.hide();
			arrowR4.hide();
			arrowAvail.hide();
			availRes.unhighlightCell(0, 3, null, null);
			sc.unhighlight(3); // deadlock = false;
			sc.highlight(4); // while ( )
			sc.highlight(5); // {
			sc.unhighlight(11); // continue
			sc.unhighlight(13); // else if ( )
			sc.unhighlight(14); // {
			sc.unhighlight(17); // deadlock = false
			sc.unhighlight(18); // }
			sc.highlight(20); // }

			// deadlock = true
			lang.nextStep();
			arrowDeadlock.hide();
			arrowDeadlock.show();
			arrowTerm.hide();
			sc.unhighlight(4); // while ( )
			sc.unhighlight(5); // {
			sc.highlight(6); // deadlock = true;
			sc.unhighlight(20); // }
			deadlock = true;
			deadlockCircle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED, null, null);
			textDeadlock.setText("Deadlock vorhanden!", null, null);

			for (int i = 1; i < 5; i++) {
				// for
				lang.nextStep();
				textRes2.hide();
				arrowDeadlock.hide();
				arrayI.highlightCell(0, null, null);
				if (i == 1) {
					arrayI.put(0, 1, null, null);
				}
				if (i == 2) {
					arrayI.put(0, 2, null, null);
				}
				if (i == 3) {
					arrayI.put(0, 3, null, null);
				}
				if (i == 4) {
					arrayI.put(0, 4, null, null);
				}
				need1.unhighlightCell(0, 3, null, null);
				need2.unhighlightCell(0, 3, null, null);
				need3.unhighlightCell(0, 3, null, null);
				need4.unhighlightCell(0, 3, null, null);
				arrowR1.hide();
				arrowR2.hide();
				arrowR3.hide();
				arrowR4.hide();
				arrowAvail.hide();
				availRes.unhighlightCell(0, 3, null, null);
				sc.unhighlight(6); // deadlock = true;
				sc.highlight(7); // for ( )
				sc.highlight(8); // {
				sc.unhighlight(11); // continue
				sc.highlight(19); // }
				sc.unhighlight(13); // else if ( )
				sc.unhighlight(14); // {
				sc.unhighlight(17); // deadlock = false
				sc.unhighlight(18); // }

				// if - check if i in termPro
				lang.nextStep();
				arrayI.unhighlightCell(0, null, null);
				arrowTerm.show();
				if (i == 1) {
					arrowC1.show();
				}
				;
				if (i == 2) {
					arrowC2.show();
				}
				;
				if (i == 3) {
					arrowC3.show();
				}
				;
				if (i == 4) {
					arrowC4.show();
				}
				;
				sc.unhighlight(7); // for ( )
				sc.unhighlight(8); // {
				sc.highlight(9); // if ( )
				sc.highlight(10); // {
				sc.highlight(12); // }
				sc.unhighlight(19); // }
				if (arrayTermPro[0] == i || arrayTermPro[1] == i || arrayTermPro[2] == i || arrayTermPro[3] == i) {
					// continue
					lang.nextStep();
					arrowTerm.hide();
					arrowC1.hide();
					arrowC2.hide();
					arrowC3.hide();
					arrowC4.hide();
					sc.unhighlight(9); // if ( )
					sc.unhighlight(10); // {
					sc.highlight(11); // continue
					sc.unhighlight(12); // }
					continue;
				}

				else
					// else
					lang.nextStep();
				arrowTerm.hide();
				arrowC1.hide();
				arrowC2.hide();
				arrowC3.hide();
				arrowC4.hide();
				sc.unhighlight(9); // if ( )
				sc.unhighlight(10); // {
				sc.unhighlight(12); // }
				sc.highlight(13); // else if ( )
				sc.highlight(14); // {
				sc.highlight(18); // }

				if (i == 1) {
					// cell highlights
					lang.nextStep();
					arrowR1.show();
					arrowAvail.show();
					
					if (r1[0] <= A[0]) {
						lang.nextStep();
						need1.highlightCell(0, null, null);
						availRes.highlightCell(0, null, null);
						if (r1[1] <= A[1]) {
							lang.nextStep();
							need1.highlightCell(1, null, null);
							availRes.highlightCell(1, null, null);
							if (r1[2] <= A[2]) {
								lang.nextStep();
								need1.highlightCell(2, null, null);
								availRes.highlightCell(2, null, null);
								if (r1[3] <= A[3]) {
									lang.nextStep();
									need1.highlightCell(3, null, null);
									availRes.highlightCell(3, null, null);
									textRes1.show();
								}
							}
						}
					}

					if (r1[0] > A[0] || r1[1] > A[1] || r1[2] > A[2] || r1[3] > A[3]) {
						lang.nextStep();
						textRes2.show();
					}

					// genug Ressourcen
					if (r1[0] <= A[0] && r1[1] <= A[1] && r1[2] <= A[2] && r1[3] <= A[3])

					{ // i zu terminierte Prozesse
						lang.nextStep();
						textRes1.hide();
						termProCount++;
						arrowAvail.hide();
						arrowTerm.show();
						arrowR1.hide();
						need1.unhighlightCell(0, 3, null, null);
						availRes.unhighlightCell(0, 3, null, null);
						sc.unhighlight(13); // else if ( )
						sc.unhighlight(14); // {
						sc.highlight(15); // i zu terminierten
						sc.unhighlight(18); // }
						if (arrayTermPro[0] == 0) {
							arrayTermPro[0] = 1;
							termPro.put(0, 1, null, null);
							termPro.highlightCell(0, null, null);
						} else if (arrayTermPro[1] == 0) {
							arrayTermPro[1] = 1;
							termPro.put(1, 1, null, null);
							termPro.highlightCell(1, null, null);
						} else if (arrayTermPro[2] == 0) {
							arrayTermPro[2] = 1;
							termPro.put(2, 1, null, null);
							termPro.highlightCell(2, null, null);
						} else if (arrayTermPro[3] == 0) {
							arrayTermPro[3] = 1;
							termPro.put(3, 1, null, null);
							termPro.highlightCell(3, null, null);
						}
						;

						// Ressourcen freigeben
						lang.nextStep();
						termPro.unhighlightCell(0, 3, null, null);
						arrowTerm.hide();
						arrowAvail.show();
						sc.unhighlight(15); // i zu terminierten
						sc.highlight(16); // Ressourcen freigeben

						lang.nextStep();
						used1.highlightCell(0, null, null);
						availRes.highlightCell(0, null, null);
						lang.nextStep();
						availRes.put(0, availRes.getData(0) + used1.getData(0), null, null);

						lang.nextStep();
						used1.unhighlightCell(0, null, null);
						availRes.unhighlightCell(0, null, null);
						used1.highlightCell(1, null, null);
						availRes.highlightCell(1, null, null);
						lang.nextStep();
						availRes.put(1, availRes.getData(1) + used1.getData(1), null, null);

						lang.nextStep();
						used1.unhighlightCell(1, null, null);
						availRes.unhighlightCell(1, null, null);
						used1.highlightCell(2, null, null);
						availRes.highlightCell(2, null, null);
						lang.nextStep();
						availRes.put(2, availRes.getData(2) + used1.getData(2), null, null);

						lang.nextStep();
						used1.unhighlightCell(2, null, null);
						availRes.unhighlightCell(2, null, null);
						used1.highlightCell(3, null, null);
						availRes.highlightCell(3, null, null);
						lang.nextStep();
						availRes.put(3, availRes.getData(3) + used1.getData(3), null, null);

						A[0] = A[0] + c1[0];
						A[1] = A[1] + c1[1];
						A[2] = A[2] + c1[2];
						A[3] = A[3] + c1[3];

						// deadlock = false
						lang.nextStep();
						lineC1.show();
						lineR1.show();
						used1.unhighlightCell(3, null, null);
						availRes.unhighlightCell(3, null, null);
						arrowAvail.hide();
						arrowC1.hide();
						sc.unhighlight(16); // Ressourcen freigeben
						sc.highlight(17); // deadlock = false
						deadlock = false;
						arrowDeadlock.show();
						deadlockCircle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN, null, null);
						textDeadlock.setText("Kein Deadlock vorhanden!", null, null);
					}
				} else if (i == 2) {
					// cell highlights
					lang.nextStep();
					arrowR2.show();
					arrowAvail.show();
					if (r2[0] <= A[0]) {
						lang.nextStep();
						need2.highlightCell(0, null, null);
						availRes.highlightCell(0, null, null);
						if (r2[1] <= A[1]) {
							lang.nextStep();
							need2.highlightCell(1, null, null);
							availRes.highlightCell(1, null, null);
							if (r2[2] <= A[2]) {
								lang.nextStep();
								need2.highlightCell(2, null, null);
								availRes.highlightCell(2, null, null);
								if (r2[3] <= A[3]) {
									lang.nextStep();
									need2.highlightCell(3, null, null);
									availRes.highlightCell(3, null, null);
									textRes1.show();
								}
							}
						}
					}

					if (r2[0] > A[0] || r2[1] > A[1] || r2[2] > A[2] || r2[3] > A[3]) {
						lang.nextStep();
						textRes2.show();
					}
					// genug Ressourcen

					if (r2[0] <= A[0] && r2[1] <= A[1] && r2[2] <= A[2] && r2[3] <= A[3])

					{ // i zu terminierte Prozesse
						lang.nextStep();
						textRes1.hide();
						termProCount++;
						arrowAvail.hide();
						arrowR2.hide();
						arrowTerm.show();
						need2.unhighlightCell(0, 3, null, null);
						availRes.unhighlightCell(0, 3, null, null);
						sc.unhighlight(13); // else if ( )
						sc.unhighlight(14); // {
						sc.highlight(15); // i zu terminierten
						sc.unhighlight(18); // }
						if (arrayTermPro[0] == 0) {
							arrayTermPro[0] = 2;
							termPro.put(0, 2, null, null);
							termPro.highlightCell(0, null, null);
						} else if (arrayTermPro[1] == 0) {
							arrayTermPro[1] = 2;
							termPro.put(1, 2, null, null);
							termPro.highlightCell(1, null, null);
						} else if (arrayTermPro[2] == 0) {
							arrayTermPro[2] = 2;
							termPro.put(2, 2, null, null);
							termPro.highlightCell(2, null, null);
						} else if (arrayTermPro[3] == 0) {
							arrayTermPro[3] = 2;
							termPro.put(3, 2, null, null);
							termPro.highlightCell(3, null, null);
						}
						;

						// Ressourcen freigeben
						lang.nextStep();
						termPro.unhighlightCell(0, 3, null, null);
						arrowTerm.hide();
						arrowAvail.show();
						sc.unhighlight(15); // i zu terminierten
						sc.highlight(16); // Ressourcen freigeben

						lang.nextStep();
						used2.highlightCell(0, null, null);
						availRes.highlightCell(0, null, null);
						lang.nextStep();
						availRes.put(0, availRes.getData(0) + used2.getData(0), null, null);

						lang.nextStep();
						used2.unhighlightCell(0, null, null);
						availRes.unhighlightCell(0, null, null);
						used2.highlightCell(1, null, null);
						availRes.highlightCell(1, null, null);
						lang.nextStep();
						availRes.put(1, availRes.getData(1) + used2.getData(1), null, null);

						lang.nextStep();
						used2.unhighlightCell(1, null, null);
						availRes.unhighlightCell(1, null, null);
						used2.highlightCell(2, null, null);
						availRes.highlightCell(2, null, null);
						lang.nextStep();
						availRes.put(2, availRes.getData(2) + used2.getData(2), null, null);

						lang.nextStep();
						used2.unhighlightCell(2, null, null);
						availRes.unhighlightCell(2, null, null);
						used2.highlightCell(3, null, null);
						availRes.highlightCell(3, null, null);
						lang.nextStep();
						availRes.put(3, availRes.getData(3) + used2.getData(3), null, null);

						A[0] = A[0] + c2[0];
						A[1] = A[1] + c2[1];
						A[2] = A[2] + c2[2];
						A[3] = A[3] + c2[3];

						// deadlock = false
						lang.nextStep();
						lineC2.show();
						lineR2.show();
						used2.unhighlightCell(3, null, null);
						availRes.unhighlightCell(3, null, null);
						arrowAvail.hide();
						arrowC2.hide();
						sc.unhighlight(16); // Ressourcen freigeben
						sc.highlight(17); // deadlock = false
						deadlock = false;
						arrowDeadlock.show();
						deadlockCircle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN, null, null);
						textDeadlock.setText("Kein Deadlock vorhanden!", null, null);
					}
				} else if (i == 3) {
					// cell highlights
					lang.nextStep();
					arrowR3.show();
					arrowAvail.show();
					if (r3[0] <= A[0]) {
						lang.nextStep();
						need3.highlightCell(0, null, null);
						availRes.highlightCell(0, null, null);
						if (r3[1] <= A[1]) {
							lang.nextStep();
							need3.highlightCell(1, null, null);
							availRes.highlightCell(1, null, null);
							if (r3[2] <= A[2]) {
								lang.nextStep();
								need3.highlightCell(2, null, null);
								availRes.highlightCell(2, null, null);
								if (r3[3] <= A[3]) {
									lang.nextStep();
									need3.highlightCell(3, null, null);
									availRes.highlightCell(3, null, null);
									textRes1.show();
								}
							}
						}
					}

					if (r3[0] > A[0] || r3[1] > A[1] || r3[2] > A[2] || r3[3] > A[3]) {
						lang.nextStep();
						textRes2.show();
					}

					// genug Ressourcen
					if (r3[0] <= A[0] && r3[1] <= A[1] && r3[2] <= A[2] && r3[3] <= A[3])

					{ // i zu terminierte Prozesse
						lang.nextStep();
						textRes1.hide();
						termProCount++;
						arrowR3.hide();
						arrowTerm.show();
						need3.unhighlightCell(0, 3, null, null);
						availRes.unhighlightCell(0, 3, null, null);
						arrowAvail.hide();
						sc.unhighlight(13); // else if ( )
						sc.unhighlight(14); // {
						sc.highlight(15); // i zu terminierten
						sc.unhighlight(18); // }
						if (arrayTermPro[0] == 0) {
							arrayTermPro[0] = 3;
							termPro.put(0, 3, null, null);
							termPro.highlightCell(0, null, null);
						} else if (arrayTermPro[1] == 0) {
							arrayTermPro[1] = 3;
							termPro.put(1, 3, null, null);
							termPro.highlightCell(1, null, null);
						} else if (arrayTermPro[2] == 0) {
							arrayTermPro[2] = 3;
							termPro.put(2, 3, null, null);
							termPro.highlightCell(2, null, null);
						} else if (arrayTermPro[3] == 0) {
							arrayTermPro[3] = 3;
							termPro.put(3, 3, null, null);
							termPro.highlightCell(3, null, null);
						}
						;

						// Ressourcen freigeben
						lang.nextStep();
						termPro.unhighlightCell(0, 3, null, null);
						arrowTerm.hide();
						arrowAvail.show();
						sc.unhighlight(15); // i zu terminierten
						sc.highlight(16); // Ressourcen freigeben

						lang.nextStep();
						used3.highlightCell(0, null, null);
						availRes.highlightCell(0, null, null);
						lang.nextStep();
						availRes.put(0, availRes.getData(0) + used3.getData(0), null, null);

						lang.nextStep();
						used3.unhighlightCell(0, null, null);
						availRes.unhighlightCell(0, null, null);
						used3.highlightCell(1, null, null);
						availRes.highlightCell(1, null, null);
						lang.nextStep();
						availRes.put(1, availRes.getData(1) + used3.getData(1), null, null);

						lang.nextStep();
						used3.unhighlightCell(1, null, null);
						availRes.unhighlightCell(1, null, null);
						used3.highlightCell(2, null, null);
						availRes.highlightCell(2, null, null);
						lang.nextStep();
						availRes.put(2, availRes.getData(2) + used3.getData(2), null, null);

						lang.nextStep();
						used3.unhighlightCell(2, null, null);
						availRes.unhighlightCell(2, null, null);
						used3.highlightCell(3, null, null);
						availRes.highlightCell(3, null, null);
						lang.nextStep();
						availRes.put(3, availRes.getData(3) + used3.getData(3), null, null);

						A[0] = A[0] + c3[0];
						A[1] = A[1] + c3[1];
						A[2] = A[2] + c3[2];
						A[3] = A[3] + c3[3];

						// deadlock = false
						lang.nextStep();
						lineC3.show();
						lineR3.show();
						used3.unhighlightCell(3, null, null);
						availRes.unhighlightCell(3, null, null);
						arrowAvail.hide();
						arrowC3.hide();
						sc.unhighlight(16); // Ressourcen freigeben
						sc.highlight(17); // deadlock = false
						deadlock = false;
						arrowDeadlock.show();
						deadlockCircle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN, null, null);
						textDeadlock.setText("Kein Deadlock vorhanden!", null, null);
					}
				} else if (i == 4) {
					// cell highlights
					lang.nextStep();
					arrowR4.show();
					arrowAvail.show();
					if (r4[0] <= A[0]) {
						lang.nextStep();
						need4.highlightCell(0, null, null);
						availRes.highlightCell(0, null, null);
						if (r4[1] <= A[1]) {
							lang.nextStep();
							need4.highlightCell(1, null, null);
							availRes.highlightCell(1, null, null);
							if (r4[2] <= A[2]) {
								lang.nextStep();
								need4.highlightCell(2, null, null);
								availRes.highlightCell(2, null, null);
								if (r4[3] <= A[3]) {
									lang.nextStep();
									need4.highlightCell(3, null, null);
									availRes.highlightCell(3, null, null);
									textRes1.show();
								}
							}
						}
					}

					if (r4[0] > A[0] || r4[1] > A[1] || r4[2] > A[2] || r4[3] > A[3]) {
						lang.nextStep();
						textRes2.show();
					}

					// genug Ressourcen
					if (r4[0] <= A[0] && r4[1] <= A[1] && r4[2] <= A[2] && r4[3] <= A[3])

					{ // i zu terminierte Prozesse
						lang.nextStep();
						textRes1.hide();
						termProCount++;
						need4.unhighlightCell(0, 3, null, null);
						availRes.unhighlightCell(0, 3, null, null);
						arrowAvail.hide();
						arrowR4.hide();
						arrowTerm.show();
						sc.unhighlight(13); // else if ( )
						sc.unhighlight(14); // {
						sc.highlight(15); // i zu terminierten
						sc.unhighlight(18); // }
						if (arrayTermPro[0] == 0) {
							arrayTermPro[0] = 4;
							termPro.put(0, 4, null, null);
							termPro.highlightCell(0, null, null);
						} else if (arrayTermPro[1] == 0) {
							arrayTermPro[1] = 4;
							termPro.put(1, 4, null, null);
							termPro.highlightCell(1, null, null);
						} else if (arrayTermPro[2] == 0) {
							arrayTermPro[2] = 4;
							termPro.put(2, 4, null, null);
							termPro.highlightCell(2, null, null);
						} else if (arrayTermPro[3] == 0) {
							arrayTermPro[3] = 4;
							termPro.put(3, 4, null, null);
							termPro.highlightCell(3, null, null);
						}
						;

						// Ressourcen freigeben
						lang.nextStep();
						termPro.unhighlightCell(0, 3, null, null);
						arrowTerm.hide();
						arrowAvail.show();
						sc.unhighlight(15); // i zu terminierten
						sc.highlight(16); // Ressourcen freigeben

						lang.nextStep();
						used4.highlightCell(0, null, null);
						availRes.highlightCell(0, null, null);
						lang.nextStep();
						availRes.put(0, availRes.getData(0) + used4.getData(0), null, null);

						lang.nextStep();
						used4.unhighlightCell(0, null, null);
						availRes.unhighlightCell(0, null, null);
						used4.highlightCell(1, null, null);
						availRes.highlightCell(1, null, null);
						lang.nextStep();
						availRes.put(1, availRes.getData(1) + used4.getData(1), null, null);

						lang.nextStep();
						used4.unhighlightCell(1, null, null);
						availRes.unhighlightCell(1, null, null);
						used4.highlightCell(2, null, null);
						availRes.highlightCell(2, null, null);
						lang.nextStep();
						availRes.put(2, availRes.getData(2) + used4.getData(2), null, null);

						lang.nextStep();
						used4.unhighlightCell(2, null, null);
						availRes.unhighlightCell(2, null, null);
						used4.highlightCell(3, null, null);
						availRes.highlightCell(3, null, null);
						lang.nextStep();
						availRes.put(3, availRes.getData(3) + used4.getData(3), null, null);

						A[0] = A[0] + c4[0];
						A[1] = A[1] + c4[1];
						A[2] = A[2] + c4[2];
						A[3] = A[3] + c4[3];

						// deadlock = false
						lang.nextStep();
						lineC4.show();
						lineR4.show();
						used4.unhighlightCell(3, null, null);
						availRes.unhighlightCell(3, null, null);
						arrowAvail.hide();
						arrowC4.hide();
						sc.unhighlight(16); // Ressourcen freigeben
						sc.highlight(17); // deadlock = false
						deadlock = false;
						arrowDeadlock.show();
						deadlockCircle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN, null, null);
						textDeadlock.setText("Kein Deadlock vorhanden!", null, null);
					}
				}
			}
		}
		lang.nextStep();
		// arrayI.put(0, 0, null, null);
		arrowR1.hide();
		textRes2.hide();
		textRes1.hide();
		arrowR2.hide();
		arrowR3.hide();
		arrowR4.hide();
		arrowAvail.hide();
		arrowDeadlock.show();
		sc.unhighlight(0); // bankierAlgo (...
		sc.unhighlight(1); // ... )
		sc.unhighlight(2); // {
		sc.unhighlight(3); // deadlock = false;
		sc.unhighlight(4); // while ( )
		sc.unhighlight(5); // {
		sc.unhighlight(6); // deadlock = true;
		sc.unhighlight(7); // for ( )
		sc.unhighlight(8); // {
		sc.unhighlight(9); // if ( )
		sc.unhighlight(10); // {
		sc.unhighlight(11); // continue
		sc.unhighlight(12); // }
		sc.unhighlight(13); // else if ( )
		sc.unhighlight(14); // {
		sc.unhighlight(15); // i zu terminierten
		sc.unhighlight(16); // Ressourcen freigeben
		sc.unhighlight(17); // deadlock = false
		sc.unhighlight(18); // }
		sc.unhighlight(19); // }
		sc.unhighlight(20); // }
		sc.highlight(21); // return deadlock
		sc.unhighlight(22); // }
		if (deadlock == true) {
			Text textDeadlockTrue = lang.newText(new Coordinates(350, 510), "Deadlock vorhanden!", "Name", null,
					textPropsDeadlock);
		} else if (deadlock == false) {
			Text textDeadlockTrue = lang.newText(new Coordinates(350, 510), "Kein Deadlock vorhanden!", "Name", null,
					textPropsNoDeadlock);
		}

		lang.nextStep("Ende des Algorithmus");
		lang.hideAllPrimitives();
		headerRect.show();
		textHeader.show();
		arrayTotalRes.hide();
		need1.hide();
		need2.hide();
		need3.hide();
		need4.hide();
		used1.hide();
		used1.hide();
		used2.hide();
		used3.hide();
		used4.hide();
		availRes.hide();
		arrayI.hide();
		termPro.hide();
		
		if(deadlock == true){
		Text textIntro20 = lang.newText(new Coordinates(20, 120),
				"Deadlock vorhanden!", "Name2",
				null, textProps5);
		Text textIntro21 = lang.newText(new Coordinates(20, 140),
				"Mit den verfügbaren Ressourcen, können die Prozesse unabhängig der Abarbeitungsreihenfolge", "Name3",
				null, textProps5);
		Text textIntro22 = lang.newText(new Coordinates(20, 160),
				" durch den Bankieralgorithmus nicht abgearbeitet werden. ", "name", null, textProps5);
		Text textIntro23 = lang.newText(new Coordinates(20, 180),
				"Das System müsste mehr Ressourcen zur Verfügung stellen können, um eine verklemmungsfreie", "Name5",
				null, textProps5);
		Text textIntro24 = lang.newText(new Coordinates(20, 200), "Abarbeitung der Prozesse gewährleisten zu können.", "Name6", null,
				textProps5);
		}
		else{
			Text textIntro30 = lang.newText(new Coordinates(20, 120),
					"Kein Deadlock vorhanden!", "Name2",
					null, textProps5);
			Text textIntro31 = lang.newText(new Coordinates(20, 140),
					"Mit den verfügbaren Ressourcen ist die Abarbeitung der Prozesse", "Name3",
					null, textProps5);
			Text textIntro32 = lang.newText(new Coordinates(20, 160),
					"durch den Bankieralgorithmus verklemmungsfrei möglich.", "Name3",
					null, textProps5);
		}
	}
    
    
    

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        BelegteRessourcenC2 = (int[])primitives.get("BelegteRessourcenC2");
        BenoetigteRessourcenR1 = (int[])primitives.get("BenoetigteRessourcenR1");
        BelegteRessourcenC3 = (int[])primitives.get("BelegteRessourcenC3");
        BenoetigteRessourcenR2 = (int[])primitives.get("BenoetigteRessourcenR2");
        BenoetigteRessourcenR3 = (int[])primitives.get("BenoetigteRessourcenR3");
        BelegteRessourcenC1 = (int[])primitives.get("BelegteRessourcenC1");
        BenoetigteRessourcenR4 = (int[])primitives.get("BenoetigteRessourcenR4");
        GesamteRessourcen = (int[])primitives.get("GesamteRessourcen");
        BelegteRessourcenC4 = (int[])primitives.get("BelegteRessourcenC4");
        
 
        arrowProperties = (PolylineProperties)props.getPropertiesByName("arrowProperties");
        arrayProperties = (ArrayProperties)props.getPropertiesByName("arrayProperties");
        
        
        init();
        
        arrayProps = arrayProperties;
        polyPropsArrow = arrowProperties;
        
        int[] E = GesamteRessourcen;
        int[] c1 = BelegteRessourcenC1;
        int[] c2 = BelegteRessourcenC2;
        int[] c3 = BelegteRessourcenC3;
        int[] c4 = BelegteRessourcenC4;
        int[] r1 = BenoetigteRessourcenR1;
        int[] r2 = BenoetigteRessourcenR2;
        int[] r3 = BenoetigteRessourcenR3;
        int[] r4 = BenoetigteRessourcenR4;
        int[] A = new int[]{0,0,0,0};
        
		bankier(E, A, c1, c2, c3, c4, r1, r2, r3, r4);
		
		// System.out.println(lang.toString());
        

        return lang.toString();
    }

    public String getName() {
        return "Bankieralgorithmus";
    }

    public String getAlgorithmName() {
        return "Bankieralgorithmus";
    }

    public String getAnimationAuthor() {
        return "Aiko Westmeier, Manuel Stork";
    }

    public String getDescription(){
        return "Der Bankieralgorithmus ist ein Algorithmus, der zur Vermeidung von Verklemmungen (Deadlocks) bei der Vergabe von"
 +" Systemressourcen benutzt wird."
 +" Dabei sind dem Algorithmus die gesamten Ressourcen und die auszuführenden Prozesse bekannt. Über die Prozesse"
 +" sind folgende Eigenschaften bekannt: Ressourcen, die von den Prozessen bereits besetzt sind und Ressourcen die von"
 +" den Prozessen noch benötigt werden."
 +"\n"
 +"Aus den gesamten Ressourcen und den belegten Ressourcen der Prozesse errechnen sich die verfügbaren Ressourcen. "
 +" Sofern die Ausführung möglich ist (genügend verfügbare Ressourcen vorhanden), werden die Prozesse nacheinander"
 +" abgearbeitet. Nachdem ein Prozess abgearbeitet wurde, werden dessen belegte Ressourcen den verfügbaren Ressourcen "
 +" hinzugeführt."
 +"\n"
 +"Nach dem Ausführen des Bankieralgorithmus steht fest, ob durch eine bestimmte Reihenfolge der Ausführung der Prozesse"
 +" eine Verklemmung vermeidbar ist."
 +"\n"
 +"\n"
 +"Verklemmung/Deadlock: Ein Deadlock beschreibt in der Informatik einen Zustand eines System, bei dem eine zyklische Wartesituation zwischen"
 +" mehreren Prozessen auftritt, wobei kein beteiligter Prozess mehr abgearbeitet werden kann und auf die Freigabe von Betriebsmitteln"
 +" wartet, welche von anderen beteiligten Prozessen belegt sind."
 +"\n"
 +"\n"
 +"Sollten Sie die Primitives selbst wählen, bitte keine negativen Werte eingeben.";
    }

    public String getCodeExample(){
        return 
   
  "public static boolean bankier(int[] E, int[] A, ArrayList<int[]> C, ArrayList<int[]> R){"
 +"\n"
 +" int n = C.size();"
 +"\n"
 +" int m = E.length;"
 +"\n"
 +" ArrayList<int[]> terminatedProcesses = new ArrayList<int[]>();"
 +"\n"
 +" boolean deadlock = false;"
 +"\n"
 +" "
 +"\n"
 +"	while (terminatedProcesses.size() < n && !deadlock){"
 +"\n"
 +"	 deadlock = true;			"
 +"\n"
 +"	 for (int i = 0; i < n; i++){			"
 +"\n"
 +"	  if (terminatedProcesses.contains(C.get(i))) {"
 +"\n"
 +"	   continue;					"
 +"\n"
 +"	  }"
 +"\n"
 +"   else if (checkRessources(R.get(i), A)){ "
 +"\n"
 +"    terminatedProcesses.add(C.get(i));"
 +"\n"
 +"    A = freeRessources(A, C.get(i));"
 +"\n"
 +"    deadlock = false;		"
 +"\n"
 +"   }				"
 +"\n"
 +"  }			"
 +"\n"
 +" }"
 +"\n"
 +" return deadlock;"
 +"\n"
 +"}		"
 +"\n"
 +"\n";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_HARDWARE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
	
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		
        BelegteRessourcenC2 = (int[])primitives.get("BelegteRessourcenC2");
        for(int i = 0; i < 4; i++){
        	if(BelegteRessourcenC2[i] < 0){
        		return false;
        	}
        }
        
        BenoetigteRessourcenR1 = (int[])primitives.get("BenoetigteRessourcenR1");
        for(int i = 0; i < 4; i++){
        	if(BenoetigteRessourcenR1[i] < 0){
        		return false;
        	}
        }
  
        BelegteRessourcenC3 = (int[])primitives.get("BelegteRessourcenC3");
        for(int i = 0; i < 4; i++){
        	if(BelegteRessourcenC3[i] < 0){
        		return false;
        	}
        }
        
        BenoetigteRessourcenR2 = (int[])primitives.get("BenoetigteRessourcenR2");
        for(int i = 0; i < 4; i++){
        	if(BenoetigteRessourcenR2[i] < 0){
        		return false;
        	}
        }
        
        BenoetigteRessourcenR3 = (int[])primitives.get("BenoetigteRessourcenR3");
        for(int i = 0; i < 4; i++){
        	if(BenoetigteRessourcenR3[i] < 0){
        		return false;
        	}
        }
        
        BelegteRessourcenC1 = (int[])primitives.get("BelegteRessourcenC1");
        for(int i = 0; i < 4; i++){
        	if(BelegteRessourcenC1[i] < 0){
        		return false;
        	}
        }
        
        BenoetigteRessourcenR4 = (int[])primitives.get("BenoetigteRessourcenR4");
        for(int i = 0; i < 4; i++){
        	if(BenoetigteRessourcenR4[i] < 0){
        		return false;
        	}
        }
        
        GesamteRessourcen = (int[])primitives.get("GesamteRessourcen");
        for(int i = 0; i < 4; i++){
        	if(GesamteRessourcen[i] < 0){
        		return false;
        	}
        }
        
        BelegteRessourcenC4 = (int[])primitives.get("BelegteRessourcenC4");
        for(int i = 0; i < 4; i++){
        	if(BelegteRessourcenC4[i] < 0){
        		return false;
        	}
        }
		
		
		// TODO Auto-generated method stub
		return true;
	}
    

	

}