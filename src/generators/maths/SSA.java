/*
 * Schonhage-Strassen-Algorithmus.java
 * Alexander Ziesing, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.IntArray;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.maths.helperSAA.ComplexNumberSSA;
import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.TwoValueView;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CounterProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import animal.variables.VariableRoles;


@SuppressWarnings("unused")
public class SSA implements ValidatingGenerator {
    private Language lang;
    private Variables vars;
    
    private String VALUE = animal.variables.Variable.getRoleString(VariableRoles.FIXED_VALUE);
    
    
    public ArrayProperties array;
    public SourceCodeProperties header, text;
    
    public int A,B,MaxArray,k,l,K, endErgebnis;
    public int[] AB, BB, ABE, BBE, FolgeA, FolgeB;
    ComplexNumberSSA Einheitswurzel;
	ComplexNumberSSA[] AS, BS,ass;
	ComplexNumberSSA[] bss;
	ComplexNumberSSA[] cs;
    ComplexNumberSSA[] doppelteFourierTransformationErgebnis;
    double[] ergebnisRuecktransformation, gerundetErgebnisRuecktransformation;
    RectProperties rp;
    SourceCodeProperties titelFont2, titelFont3;

    public void init(){
        lang = new AnimalScript("Schonhage-Strassen-Algorithmus", "Alexander Ziesing", 800, 600);
        lang.setStepMode(true);
        vars = lang.newVariables();
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        A = (Integer)primitives.get("A");
        B = (Integer)primitives.get("B");
        array = (ArrayProperties)props.getPropertiesByName("array");
        header = (SourceCodeProperties)props.getPropertiesByName("header");
        text = (SourceCodeProperties)props.getPropertiesByName("header");
        
        
        rp = new RectProperties();
		rp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		rp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		rp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
		
		titelFont2 = new SourceCodeProperties();
		titelFont2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		titelFont2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 17));
		
		titelFont3 = new SourceCodeProperties();
		titelFont3.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		titelFont3.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
        
        AB = berechneBinaerArrays(A, 20,true);
        BB = berechneBinaerArrays(B, 20,true);
        ABE = berechneBinaerArrays(A,32,false);
        BBE = berechneBinaerArrays(B,32,false);
  
        Einheitswurzel = new ComplexNumberSSA(Math.cos((2*Math.PI)/K), Math.sin((2*Math.PI)/K));
        
        einleitung();
        
   
        iteration1();
        iteration2a();
        
        iteration2b();
        iteration3();
        iteration4();
        iteration5();
        
        return lang.toString();
    }
    
    @SuppressWarnings("unused")
	private void einleitung() {
		
		SourceCodeProperties titelFont = new SourceCodeProperties();
		titelFont.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		titelFont.set(AnimationPropertiesKeys.BOLD_PROPERTY, true);
		titelFont.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 27));
		
		
		
    	
    	SourceCode titel = lang.newSourceCode(new Coordinates(0,5), "titel", null, titelFont);
    	titel.addCodeLine("Schonhage-Strassen-Algorithmus", null, 1, null);
    	
    	Rect r = lang.newRect(new Coordinates(0, 10), new Coordinates(550, 100), "Box", null, rp);
    	
    	SourceCode ablauf = lang.newSourceCode(new Coordinates(10,120), "ablauf", null,titelFont2);
    	ablauf.addCodeLine("Um zwei ganze Zahlen a und b zu multiplizieren, wird im Groben folgendes Schema angewandt:", null, 0, null);
    	ablauf.addCodeLine("", null, 0, null);
    	ablauf.addCodeLine("1. Aufspaltung der Zahlen in Binaerdarstellung a und b in Stuecke passender Laenge", null, 1, null);
    	ablauf.addCodeLine("2. Diskrete Fourier-Transformation der beiden Stueckfolgen", null, 1, null);
    	ablauf.addCodeLine("3. Komponentenweise (komplexe) Multiplikation der transformierten Stuecke", null, 1, null);
    	ablauf.addCodeLine("4. Ruecktransformation (inverse Fouriertransformation) der Ergebnisse", null, 1, null);
    	ablauf.addCodeLine("5. Zusammensetzen der Ergebnisstuecke zur Ergebniszahl", null, 1, null);
    	lang.nextStep("Einleitung");
    	
    	lang.hideAllPrimitives();
    	
    }
    
    private void iteration1() {
    	
    	Rect r = lang.newRect(new Coordinates(0, 5), new Coordinates(250, 35), "Box", null, rp);
    	
    	SourceCode titel = lang.newSourceCode(new Coordinates(5, 0), "header", null, titelFont3);
    	titel.addCodeLine("Schonhage-Strassen-Algorithmus", "titel", 0, null);
    	
    	SourceCode ersterSchritt = lang.newSourceCode(new Coordinates(20, 30), "ersterSchritt", null, header);
		ersterSchritt.addCodeLine("1. Dezimal zu binaer umrechnen", "1", 0, null);
		
		
		CounterProperties cp = new CounterProperties();
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE);
		
		lang.nextStep("1. Iteration");
		vars.declare("int", "A",String.valueOf(B), VALUE);
        vars.declare("int","B",String.valueOf(A), VALUE);
		
		SourceCode aZahl = lang.newSourceCode(new Coordinates(50, 80), "A", null, text);
		aZahl.addCodeLine(Integer.toString(A), "A1", 0, null);
		aZahl.addCodeLine("", null, 0, null);
		SourceCode bZahl = lang.newSourceCode(new Coordinates(50, 110), "B", null, text);
		bZahl.addCodeLine(Integer.toString(B), "B1", 0, null);
		
		IntArray intArrayAB = lang.newIntArray(new Coordinates(120, 85), AB,"AB", null, array);
		intArrayAB.showIndices(false, null, null);

		IntArray intArrayBB = lang.newIntArray(new Coordinates(120, 120), BB,"BB", null, array);
		intArrayBB.showIndices(false, null, null);
		
		lang.nextStep();
		TwoValueCounter counterT = lang.newCounter(intArrayAB);
		TwoValueView counter = lang.newCounterView(counterT, new Coordinates(330,100),cp,true,true);
		counter.hideText();
		counter.hideBar();
		lang.nextStep();
		
		vars.declare("String", "A-Binaer", Arrays.toString(AB), VALUE);
        vars.declare("String", "B-Binaer", Arrays.toString(BB), VALUE);
		
		/*
		 * Countervisualisierung
		 */
		for (int i = 0; (i < intArrayBB.getLength() || i < intArrayAB.getLength()); i++) {
			intArrayAB.unhighlightCell(i-1, null, null);
			intArrayBB.unhighlightCell(i-1, null, null);
			
			if(i < intArrayAB.getLength()) {
				counterT.assignmentsInc(1);
				intArrayAB.highlightCell(i, null, null);
			}
			
			if(i < intArrayBB.getLength()) {
				counterT.accessInc(1);
				intArrayBB.highlightCell(i, null, null);
			}
			lang.nextStep();	
		}
		intArrayAB.unhighlightCell(intArrayAB.getLength()-1, null, null);
		intArrayBB.unhighlightCell(intArrayBB.getLength()-1, null, null);
		
		ersterSchritt.unhighlight("1");
		SourceCode zweiterSchritt = lang.newSourceCode(new Coordinates(20, 160), "zweiterSchritt", null, header);
		zweiterSchritt.addCodeLine("2. Parameter bestimmen", "1", 0, null);
		zweiterSchritt.highlight("1");
		lang.nextStep();
		zweiterSchritt.addCodeLine("", null, 0, null);
		zweiterSchritt.addCodeLine("Parameter n:  ", "para1", 1, null);
		
		lang.nextStep();
		vars.discard("A");
        vars.discard("B");
        vars.discard("A-Binaer");
        vars.discard("B-Binaer");
		
		/*
		 * Bestimmen des längsten Bit-Arrays
		 */
		if (intArrayAB.getLength() > intArrayBB.getLength()) {
			zweiterSchritt.addCodeElement(Integer.toString(intArrayAB.getLength()), "zahlCounter", 1, null);
			MaxArray = intArrayAB.getLength();
		} else {
			zweiterSchritt.addCodeElement(Integer.toString(intArrayBB.getLength()), "zahlCounter", 1, null);
			MaxArray = intArrayBB.getLength();
		}
		//FEHLER
		vars.declare("int", "n", String.valueOf(MaxArray),VALUE);
		
		lang.nextStep();
		
		zweiterSchritt.addCodeLine("-> Das laengere wird verwendet!", "hinweis", 1, null);
		zweiterSchritt.highlight("hinweis");
		
		lang.nextStep();
		
		zweiterSchritt.unhighlight("hinweis");
		zweiterSchritt.addCodeLine("Parameter l:   4", "para2", 1, null);
		vars.declare("int", "l", String.valueOf(4),VALUE);
		lang.nextStep();
		
		zweiterSchritt.addCodeLine("-> Wir teilen die Zahlen zu jeweils 4 Bit-Blöcken", "hinweis2", 1, null);
		zweiterSchritt.highlight("hinweis2");

		lang.nextStep();

		zweiterSchritt.unhighlight("hinweis2");
		lang.nextStep();
		zweiterSchritt.addCodeLine("Formel berechnen:  2n <= l * 2^k < 4n", "formel", 1, null);
		lang.nextStep();
		zweiterSchritt.addCodeLine("", "e0", 0, null);
		zweiterSchritt.addCodeElement("-> Einsetzen: 2 *", "e1", 1, null);
		lang.nextStep();
		zweiterSchritt.addCodeElement(Integer.toString(MaxArray), "e2", 1, null);
		zweiterSchritt.highlight("e2");
		zweiterSchritt.highlight("zahlCounter");
		zweiterSchritt.highlight("para1");
		lang.nextStep();
		zweiterSchritt.unhighlight("e2");
		zweiterSchritt.unhighlight("zahlCounter");
		zweiterSchritt.unhighlight("para1");

		zweiterSchritt.addCodeElement(" <= ", "e3", 1, null);
		lang.nextStep();
		zweiterSchritt.addCodeElement("4", "e4", 1, null);
		zweiterSchritt.highlight("e4");
		zweiterSchritt.highlight("para2");
		lang.nextStep();
		zweiterSchritt.unhighlight("e4");
		zweiterSchritt.unhighlight("para2");

		zweiterSchritt.addCodeElement("* 2^k < ", "e5", 1, null);
		lang.nextStep();
		zweiterSchritt.addCodeElement("4 *", "e6", 1, null);
		lang.nextStep();
		zweiterSchritt.addCodeElement(Integer.toString(MaxArray), "e7", 1, null);
		zweiterSchritt.highlight("e7");
		zweiterSchritt.highlight("zahlCounter");
		zweiterSchritt.highlight("para1");
		lang.nextStep();
		zweiterSchritt.unhighlight("e7");
		zweiterSchritt.unhighlight("zahlCounter");
		zweiterSchritt.unhighlight("para1");
		zweiterSchritt.addCodeElement("   | :4", "e8", 1, null);
		zweiterSchritt.highlight("e8");
		lang.nextStep();

		zweiterSchritt.addCodeLine("-> Umformen: ", "u0", 1, null);
		zweiterSchritt.highlight("u0");
		lang.nextStep();
		zweiterSchritt.addCodeElement("1/2 *", "u1", 1, null);
		lang.nextStep();
		zweiterSchritt.addCodeElement(Integer.toString(MaxArray), "u2", 1, null);
		lang.nextStep();
		zweiterSchritt.addCodeElement(" <= ", "u3", 1, null);
		lang.nextStep();
		zweiterSchritt.addCodeElement("2^k", "u4", 1, null);
		lang.nextStep();
		zweiterSchritt.addCodeElement(" < ", "u5", 1, null);
		lang.nextStep();
		zweiterSchritt.addCodeElement(Integer.toString(MaxArray), "u6", 1, null);
		lang.nextStep();
		zweiterSchritt.unhighlight("u0");
		zweiterSchritt.unhighlight("e8");
		zweiterSchritt.addCodeLine("-> Umformen: ", "o0", 1, null);
		zweiterSchritt.highlight("o0");
		lang.nextStep();
		zweiterSchritt.addCodeElement("log2(" + Integer.toString(MaxArray / 2) + ")", "o1", 1, null);
		lang.nextStep();
		zweiterSchritt.addCodeElement(" <= ", "o2", 1, null);
		lang.nextStep();
		zweiterSchritt.addCodeElement("k", "o3", 1, null);
		lang.nextStep();
		zweiterSchritt.addCodeElement(" < ", "o4", 1, null);
		lang.nextStep();
		zweiterSchritt.addCodeElement("log2(" + Integer.toString(MaxArray) + ")", "o5", 1, null);
		lang.nextStep();
		zweiterSchritt.unhighlight("o0");
		zweiterSchritt.addCodeLine("=> Ergebnis: ", "p0", 1, null);
		zweiterSchritt.highlight("p0");
		
		double leftOperand = Math.log(((double)MaxArray/2))/Math.log(2);
		double rightOperand = Math.log((double)MaxArray)/Math.log(2);
		k = (int) Math.round((leftOperand + rightOperand)/2);
		K = (int) Math.pow(2,k);
		
		zweiterSchritt.addCodeElement(Double.toString(round(leftOperand,3)), "p1", 1, null);
		lang.nextStep();
		zweiterSchritt.addCodeElement(" <= ", "p2", 1, null);
		lang.nextStep();
		zweiterSchritt.addCodeElement("k", "p3", 1, null);
		lang.nextStep();
		zweiterSchritt.addCodeElement(" < ", "p4", 1, null);
		lang.nextStep();
		zweiterSchritt.addCodeElement(Double.toString(round(rightOperand,3)), "p5", 1, null);
		lang.nextStep();
		zweiterSchritt.addCodeElement("      k = " + k, "p6", 1, null);

		zweiterSchritt.highlight("p6");
		zweiterSchritt.highlight("p3");
		lang.nextStep();
		zweiterSchritt.unhighlight("p0");
		zweiterSchritt.unhighlight("p6");
		zweiterSchritt.unhighlight("p3");
		lang.nextStep();
		zweiterSchritt.addCodeLine("Parameter K: 2^k = " + K, "p6", 1, null);
		zweiterSchritt.highlight("p6");
		vars.declare("int", "K", String.valueOf(K),VALUE);
		
		lang.nextStep();
		zweiterSchritt.unhighlight("p6");
		zweiterSchritt.unhighlight("1");
		lang.nextStep();

		SourceCode dritterSchritt = lang.newSourceCode(new Coordinates(650, 0), "dritterSchritt", null, header);
		dritterSchritt.addCodeLine("3. Binaedarstellung mit Nullen auffüllen sodass es dann K*l Stellen sind: ", "tabelle", 0, null);
		dritterSchritt.highlight("tabelle");
		vars.discard("n");
		vars.discard("K");
		vars.discard("l");
        

		IntArray intArrayA2 = lang.newIntArray(new Coordinates(650, 55), ABE, "A2-Array", null,array);
		intArrayA2.showIndices(true, null, null);
		lang.nextStep();
		IntArray intArrayB2 = lang.newIntArray(new Coordinates(650, 120), BBE, "B2-Array", null,array);
		intArrayB2.showIndices(true, null, null);
		lang.nextStep();
		SourceCode dritterSchritt4 = lang.newSourceCode(new Coordinates(650, 350), "dritterSchritt", null, text);
		SourceCode dritterSchritt3 = lang.newSourceCode(new Coordinates(650, 330), "dritterSchritt", null, text);
		SourceCode dritterSchritt2 = lang.newSourceCode(new Coordinates(650, 200), "dritterSchritt2", null, text);
		SourceCode dritterSchritt1 = lang.newSourceCode(new Coordinates(650, 160), "dritterSchritt1", null, text);
		dritterSchritt1.addCodeLine(" s:  ", "0", 1, null);
		dritterSchritt2.addCodeLine("As: ", "1", 1, null);
		dritterSchritt2.addCodeLine("Bs: ", "2", 1, null);
		dritterSchritt3.addCodeLine("As: ", "3", 1, null);
		dritterSchritt4.addCodeLine("Bs: ", "4", 1, null);
		dritterSchritt2.addCodeLine("", "2", 1, null);
		dritterSchritt2.addCodeLine("", "2", 1, null);
		dritterSchritt2.addCodeLine("", "2", 1, null);
		dritterSchritt2.addCodeLine("=> Daraus resultiert unsere Folge!", "folge", 1, null);
		dritterSchritt2.addCodeLine("=> Umschreiben in Dezimaldarstellung!", "folge2", 1, null);

		FolgeA = new int[K];
		FolgeB = new int[K];
		
		
		for (int i = 0, c = K - 1; i < K; i++, c--) {
			int tmp4a = 0, tmp4b = 0;
			int[] tmpInta = new int[l];
			int[] tmpIntb = new int[l];
			dritterSchritt1.addCodeElement(Integer.toString(i), "counter", 1, null);
			dritterSchritt1.highlight("counter");
			lang.nextStep();
			
			
			tmpInta = Arrays.copyOfRange(ABE, c * 4, c * 4 + 4);
			tmpIntb = Arrays.copyOfRange(BBE, c * 4, c * 4 + 4);
			IntArray intArrayTMPa = lang.newIntArray(new Coordinates(720 + i * 80, 220), tmpInta, "tmpArrayA", null,array);
			tmp4a += tmpInta[0] * 8 + tmpInta[1] * 4 + tmpInta[2] * 2 + tmpInta[3] * 1;
			FolgeA[i] = tmp4a;
			dritterSchritt3.addCodeElement(Integer.toString(tmp4a), "g1", 1, null);
			dritterSchritt3.highlight("g1");
			tmp4a = 0;

			intArrayTMPa.showIndices(false, null, null);
			IntArray intArrayTMPb = lang.newIntArray(new Coordinates(720 + i * 80, 260), tmpIntb, "tmpArrayB", null,array);
			tmp4b += tmpIntb[0] * 8 + tmpIntb[1] * 4 + tmpIntb[2] * 2 + tmpIntb[3] * 1;
			FolgeB[i] = tmp4b;
			dritterSchritt4.addCodeElement(Integer.toString(tmp4b), "g2", 1, null);
			dritterSchritt4.highlight("g2");
			tmp4b = 0;
			intArrayTMPb.showIndices(false, null, null);
			intArrayTMPa.highlightCell(0,l - 1, null, null);
			intArrayTMPb.highlightCell(0,l - 1, null, null);

			intArrayA2.highlightCell(c * 4, c * 4 + 3, null, null);
			intArrayB2.highlightCell(c * 4, c * 4 + 3, null, null);

			lang.nextStep();
			dritterSchritt1.unhighlight("counter");
			intArrayTMPa.unhighlightCell(0, l- 1, null, null);
			intArrayTMPb.unhighlightCell(0, l - 1, null, null);

			intArrayA2.unhighlightCell(c * 4, c * 4 + 3, null, null);
			intArrayB2.unhighlightCell(c * 4, c * 4 + 3, null, null);

			dritterSchritt3.unhighlight("g1");
			dritterSchritt4.unhighlight("g2");
		}

		lang.nextStep();
		dritterSchritt.unhighlight("tabelle");
		lang.nextStep();
		vars.declare("String", "A-Folge",Arrays.toString(FolgeA), VALUE);
		vars.declare("String", "B-Folge",Arrays.toString(FolgeB), VALUE);
		dritterSchritt4.addCodeLine("=> Jetzt habe wir unsere Folgen fuer die Fouriertransformation!", "fourier", 1,
				null);
		lang.nextStep();
		dritterSchritt4.highlight("fourier");
		lang.nextStep();
		
		
		
		lang.hideAllPrimitives();
	}
    
	private void iteration2a() {
		
		Rect r = lang.newRect(new Coordinates(0, 5), new Coordinates(250, 35), "Box", null, rp);
    	
    	SourceCode titel = lang.newSourceCode(new Coordinates(5, 0), "header", null, titelFont3);
    	titel.addCodeLine("Schonhage-Strassen-Algorithmus", "titel", 0, null);
		
		SourceCode ersterSchritt = lang.newSourceCode(new Coordinates(20, 32), "ersterSchritt", null, header);
		ersterSchritt.addCodeLine("4. Berechnung der primitiven Einheitswurzel w", "ersterSchritt1", 0, null);
		ersterSchritt.highlight("ersterSchritt1");
		lang.nextStep("Fouriertransformierte Input A");
		lang.nextStep();
		
		ersterSchritt.addCodeLine("Erinnerung: K = " + K, "Erinnerung1", 1, null);
		lang.nextStep();
		
		createEinheitswurzel(lang, 40, 90, K);
		lang.nextStep();
		ersterSchritt.unhighlight("ersterSchritt1");
		SourceCode zweiterSchritt = lang.newSourceCode(new Coordinates(20, 140), "zweiterSchritt", null, header);
		zweiterSchritt.addCodeLine("5. Bestimmung der Fouriertransformierten", "zweiterSchritt", 0, null);
		zweiterSchritt.highlight("zweiterSchritt");
		lang.nextStep();
		createSumSymbolA(lang, 70, 180, 0, 0, k, null, Einheitswurzel, true, null, null);
		lang.nextStep();
		SourceCode zweiterSchritt1 = lang.newSourceCode(new Coordinates(20, 240), "zweiterSchritt1", null, header);
		zweiterSchritt1.addCodeLine("Erinnerung Folge A und B:", "zweiterSchritt1", 0, null);
		lang.nextStep();
		IntArray ass = lang.newIntArray(new Coordinates(20, 300), FolgeA, "as", null,array);
		lang.nextStep();
		IntArray bss = lang.newIntArray(new Coordinates(20, 360), FolgeB, "bs", null, array);
		lang.nextStep();
		SourceCode dritterSchritt = lang.newSourceCode(new Coordinates(550, 0), "dritterSchritt", null, header);
		zweiterSchritt.unhighlight("zweiterSchritt");
		dritterSchritt.addCodeLine("6. Gleichungen", "dritterSchritt", 0, null);
		lang.nextStep();
		dritterSchritt.highlight("dritterSchritt");
		
		
		for (int i = 0; i < K; i++) {
			createSumSymbolA(lang, 580, 40 + i * 60, 0, i,k, FolgeA, Einheitswurzel,
					false, ass, bss);
		}
		
		//vars.declare("String", "A-Fourier", Arrays.toString(complexArrayToStringArray(AS)),VALUE);
		lang.nextStep();
		lang.hideAllPrimitives();
	}

	private void iteration2b() {
		
		Rect r = lang.newRect(new Coordinates(0, 5), new Coordinates(250, 35), "Box", null, rp);
    	
    	SourceCode titel = lang.newSourceCode(new Coordinates(5, 0), "header", null, titelFont3);
    	titel.addCodeLine("Schonhage-Strassen-Algorithmus", "titel", 0, null);
		
		SourceCode ersterSchritt = lang.newSourceCode(new Coordinates(20, 32), "ersterSchritt", null, header);
		ersterSchritt.addCodeLine("4. Berechnung der primitiven Einheitswurzel w", "ersterSchritt1", 0, null);
		ersterSchritt.highlight("ersterSchritt1");
		lang.nextStep("Fouriertransformierte Input B");


		ersterSchritt.addCodeLine("Erinnerung: K = " + K, "Erinnerung1", 1, null);

		createEinheitswurzel(lang, 40, 90, K);


		SourceCode zweiterSchritt = lang.newSourceCode(new Coordinates(20, 140), "zweiterSchritt", null, header);
		ersterSchritt.unhighlight("ersterSchritt1");
		zweiterSchritt.addCodeLine("5. Bestimmung der Fouriertransformierten", "zweiterSchritt", 0, null);
		zweiterSchritt.highlight("zweiterSchritt");
		createSumSymbolB(lang, 70, 180, 0, 0, k, null, Einheitswurzel, true, null, null);

		SourceCode zweiterSchritt1 = lang.newSourceCode(new Coordinates(20, 240), "zweiterSchritt1", null, header);
		zweiterSchritt1.addCodeLine("Erinnerung Folge A und B:", "zweiterSchritt1", 0, null);

		IntArray ass = lang.newIntArray(new Coordinates(20, 300), FolgeA, "as", null,array);

		IntArray bss = lang.newIntArray(new Coordinates(20, 360), FolgeB, "bs", null,array);

		zweiterSchritt.unhighlight("zweiterSchritt");
		SourceCode dritterSchritt = lang.newSourceCode(new Coordinates(550, 0), "dritterSchritt", null, header);
		dritterSchritt.addCodeLine("6. Gleichungen", "dritterSchritt", 0, null);
		dritterSchritt.highlight("dritterSchritt");
		lang.nextStep();
		

		for (int i = 0; i < Math.pow(2, k); i++) {
			createSumSymbolB(lang, 580, 40 + i * 60, 0, i, k, FolgeB, Einheitswurzel, false, ass, bss);
		}
		lang.nextStep();
		lang.hideAllPrimitives();
	}
	
	@SuppressWarnings("unused")
	private void iteration3() {
		
		Rect r = lang.newRect(new Coordinates(0, 5), new Coordinates(250, 35), "Box", null, rp);
    	
    	SourceCode titel = lang.newSourceCode(new Coordinates(5, 0), "header", null, titelFont3);
    	titel.addCodeLine("Schonhage-Strassen-Algorithmus", "titel", 0, null);
		
		vars.discard("A-Folge");
		vars.discard("B-Folge");
		SourceCode ersterSchritt = lang.newSourceCode(new Coordinates(20, 30), "1", null, header);
		ersterSchritt.addCodeLine("6. Berechnung der Multiplikation von A und B", "6", 0, null);
		ersterSchritt.highlight("6");
		lang.nextStep("Multiplikation der Fouriertransformierten");
		ass = ComplexNumberSSA.calcComplexNumbers(FolgeA, Einheitswurzel);
		vars.declare("String", "A-Fouriertransformiert", Arrays.toString(complexArrayToStringArray(ass)), VALUE);
		bss = ComplexNumberSSA.calcComplexNumbers(FolgeB, Einheitswurzel);
		vars.declare("String", "B-Fouriertransformiert", Arrays.toString(complexArrayToStringArray(bss)), VALUE);
		cs = new ComplexNumberSSA[ass.length];

		SourceCode aTxt = lang.newSourceCode(new Coordinates(50, 90), "AS", null, text);
		aTxt.addCodeLine("As:", "as1", 0, null);
		lang.nextStep();
		StringArray ass2 = createComplexArray(new Coordinates(100, 90), ass, "as");
		

		SourceCode bTxt = lang.newSourceCode(new Coordinates(50, 150), "BS", null, text);
		bTxt.addCodeLine("Bs:", "bs1", 0, null);
		lang.nextStep();
		StringArray bss2 = createComplexArray(new Coordinates(100, 150), bss, "bs");

		SourceCode cTxt = lang.newSourceCode(new Coordinates(50, 210), "CS", null, text);
		cTxt.addCodeLine("Ergebnis:", "cs1", 0, null);
		cTxt.addCodeLine("", "", 0, null);
		
		for(int i = 0; i<FolgeA.length; i++) {
			cs[i] = ComplexNumberSSA.multiply(ass[i],bss[i]);
			cTxt.addCodeLine("C" + Integer.toString(i) + " = ", "c"+Integer.toString(i), 0, null);
			lang.nextStep();
			ass2.highlightCell(i, null, null);
			cTxt.addCodeElement(ass[i].toString2(), "as"+Integer.toString(i), 0,null);
			lang.nextStep();
			cTxt.addCodeElement(" * ", "*", 0,null);
			lang.nextStep();
			bss2.highlightCell(i, null, null);
			cTxt.addCodeElement(bss[i].toString2(), "bs"+Integer.toString(i), 0,null);
			lang.nextStep();
			cTxt.addCodeElement(" = ", "=", 0, null);
			lang.nextStep();
			
			String mult = ComplexNumberSSA.complexMultVisual(ass[i], bss[i]);
			cTxt.addCodeElement(mult, "mult" + Integer.toString(i), 0, null);
			
			cTxt.addCodeElement(" = ", "=", 0, null);
			
			lang.nextStep();
			ass2.unhighlightCell(i, null, null);
			bss2.unhighlightCell(i, null, null);
			
			cTxt.addCodeElement(cs[i].toString2(), "Ergebnis", 0, null);
			lang.nextStep();
		}
		lang.nextStep();
		SourceCode cTxtErgebnis = lang.newSourceCode(new Coordinates(50, 430), "CS2", null, text);
		cTxtErgebnis.addCodeLine("Cs:", "cs", 0, null);
		StringArray css2 = createComplexArray(new Coordinates(100, 430), cs, "cs");
		ersterSchritt.unhighlight("6");
		vars.declare("String", "C-Multiplikation", Arrays.toString(complexArrayToStringArray(cs)), VALUE);
		lang.nextStep();
		lang.nextStep();
		lang.hideAllPrimitives();

	}
	
	private void iteration4() {
		
		Rect r = lang.newRect(new Coordinates(0, 5), new Coordinates(250, 35), "Box", null, rp);
    	
    	SourceCode titel = lang.newSourceCode(new Coordinates(5, 0), "header", null, titelFont3);
    	titel.addCodeLine("Schonhage-Strassen-Algorithmus", "titel", 0, null);
		
    	
    	
    	SourceCode ersterSchritt = lang.newSourceCode(new Coordinates(20, 30), "eins", null, header);
		ersterSchritt.addCodeLine("7. Ruecktransformation ", "eins1", 0, null);
		vars.discard("A-Fouriertransformiert");
		vars.discard("B-Fouriertransformiert");
		vars.discard("C-Multiplikation");
		
		ersterSchritt.highlight("eins1");
		lang.nextStep("Ruecktransformation");
		ersterSchritt.addCodeLine("", "", 0, null);
		ersterSchritt.unhighlight("eins1");
		ersterSchritt.addCodeLine("a) Doppelte Transformation ausrechnen:", "formel", 1, null);
		ersterSchritt.highlight("formel");
		lang.nextStep();
		SourceCode aTxt = lang.newSourceCode(new Coordinates(50, 100), "CS", null, text);
		aTxt.addCodeLine("Cs:", "cs1", 0, null);
		lang.nextStep();
		
		StringArray ass2 = createComplexArray(new Coordinates(100, 100), cs, "as");
		ass2.getClass();
		
		SourceCode aTxt2 = lang.newSourceCode(new Coordinates(50, 150), "CS2", null, text);
		aTxt2.addCodeLine("CCs:", "cs1", 0, null);
		
		doppelteFourierTransformationErgebnis = ComplexNumberSSA.fourier(cs, Einheitswurzel);
		vars.declare("String","Doppelte-Fouriertransformation", Arrays.toString(complexArrayToStringArray(doppelteFourierTransformationErgebnis)),VALUE);
		lang.nextStep();
		StringArray css = createComplexArray(new Coordinates(100, 170), doppelteFourierTransformationErgebnis, "css");
		css.getClass();
		
		lang.nextStep();
		ersterSchritt.unhighlight("formel");
		SourceCode zweiterSchritt = lang.newSourceCode(new Coordinates(20, 220), "eins", null, header);
		zweiterSchritt.addCodeLine( "b) Ruecktransformationsformel:", "formel", 1, null);
		zweiterSchritt.highlight("formel");
		zweiterSchritt.addCodeLine("", "", 1, null);
		zweiterSchritt.addCodeLine("CCs = Kc(-r) modK", "", 2, null);
		lang.nextStep();
		for(int i = 0; i<doppelteFourierTransformationErgebnis.length;i++) {
			zweiterSchritt.addCodeLine(doppelteFourierTransformationErgebnis[i].toString2() , "trans", 2, null);
			zweiterSchritt.addCodeElement(" = ", "ite", 2, null);
			zweiterSchritt.addCodeElement(Integer.toString(K), "ite",true, 2, null);
			zweiterSchritt.addCodeElement("c(", "ite", 2, null);
			zweiterSchritt.addCodeElement("-"+Integer.toString(i)+")", "ite",true, 2, null);
			zweiterSchritt.addCodeElement(" mod", "ite",true, 2, null);
			zweiterSchritt.addCodeElement(Integer.toString(K), "ite",true, 2, null);
			zweiterSchritt.addCodeElement(" = ", "ite", 2, null);
			int tmp = calcModulo(-i, K);
			zweiterSchritt.addCodeElement(Integer.toString(K) + "c" + Integer.toString(tmp), "ite", 2, null);
			lang.nextStep();
		}
		
		lang.hideAllPrimitives();
		
		Rect r2 = lang.newRect(new Coordinates(0, 5), new Coordinates(250, 35), "Box", null, rp);
    	
    	SourceCode titel2 = lang.newSourceCode(new Coordinates(5, 0), "header", null, titelFont3);
    	titel2.addCodeLine("Schonhage-Strassen-Algorithmus", "titel", 0, null);
		
		
		ergebnisRuecktransformation = new double[doppelteFourierTransformationErgebnis.length];
		
		SourceCode dritterSchritt = lang.newSourceCode(new Coordinates(20, 30), "eins", null, header);
		dritterSchritt.addCodeLine( "c) Berechnungen:", "formel", 1, null);
		dritterSchritt.highlight("formel");
		dritterSchritt.addCodeLine("", "", 1, null);
		lang.nextStep();
		dritterSchritt.addCodeLine("C0" , "c", 2, null);
		dritterSchritt.addCodeElement(" = ", "=", 2, null);
		lang.nextStep();
		dritterSchritt.addCodeElement(doppelteFourierTransformationErgebnis[0].toString2(), "ccs", 2, null);
		dritterSchritt.addCodeElement("/", "/", 2, null);
		dritterSchritt.addCodeElement(Integer.toString(K), "K", 2, null);
		lang.nextStep();
		dritterSchritt.addCodeElement(" = ", "=", 2, null);
		dritterSchritt.addCodeElement(doppelteFourierTransformationErgebnis[0].divide(new ComplexNumberSSA(K,0)).toString2(), "ergebnis", 2, null);
		ergebnisRuecktransformation[0] = doppelteFourierTransformationErgebnis[0].divideToRe(new ComplexNumberSSA(K,0));
		
		lang.nextStep();
		
		for(int i = doppelteFourierTransformationErgebnis.length-1; i>0; i--) {
			dritterSchritt.addCodeLine("C"+Integer.toString(doppelteFourierTransformationErgebnis.length-i) , "c", 2, null);
			dritterSchritt.addCodeElement(" = ", "=", 2, null);
			lang.nextStep();
			dritterSchritt.addCodeElement(doppelteFourierTransformationErgebnis[i].toString2(), "ccs", 2, null);
			dritterSchritt.addCodeElement("/", "/", 2, null);
			dritterSchritt.addCodeElement(Integer.toString(K), "K", 2, null);
			lang.nextStep();
			dritterSchritt.addCodeElement(" = ", "=", 2, null);
			dritterSchritt.addCodeElement(doppelteFourierTransformationErgebnis[i].divide(new ComplexNumberSSA(K,0)).toString2(), "ergebnis", 2, null);
			ergebnisRuecktransformation[doppelteFourierTransformationErgebnis.length-i] = doppelteFourierTransformationErgebnis[i].divideToRe(new ComplexNumberSSA(K,0));
			lang.nextStep();
		}
		
		
		dritterSchritt.unhighlight("formel");
		SourceCode vierterSchritt = lang.newSourceCode(new Coordinates(20, 200), "eins", null, header);
		vierterSchritt.addCodeLine( "d) Dezimalzahl in Binaer umformen:" , "formel", 1, null);
		vierterSchritt.addCodeLine("", "", 1, null);
		vierterSchritt.highlight("formel");
		lang.nextStep();
		
		gerundetErgebnisRuecktransformation = new double[ergebnisRuecktransformation.length];
		for(int i = 0; i<ergebnisRuecktransformation.length;i++) {
			gerundetErgebnisRuecktransformation[i] = round(ergebnisRuecktransformation[i],0);
		}
	
		vars.declare("String","Ruecktransformation-Ergebnis", Arrays.toString(gerundetErgebnisRuecktransformation),VALUE);
		
		int[][] ergebnisBinaer = new int[K][4*K];
		
		for(int i = 0; i<doppelteFourierTransformationErgebnis.length;i++) {
			vierterSchritt.addCodeLine("C" + Integer.toString(i), "C", 2, null);
			vierterSchritt.addCodeElement(" = ", "=", 2, null);
			lang.nextStep();
			vierterSchritt.addCodeElement(Integer.toString((int)Math.round(ergebnisRuecktransformation[i])), "ergebnis", 2, null);
			vierterSchritt.addCodeElement(" = ", "=", 2, null);
			
			int[] tmp = convertBinary((int)Math.round(ergebnisRuecktransformation[i]));
			reverse(tmp);
			lang.nextStep();
			for(int l = 0; l<tmp.length;l++) {
				vierterSchritt.addCodeElement(Integer.toString(tmp[l]), "l"+Integer.toString(l),true,2, null);
				ergebnisBinaer[i][l] = tmp[l];
				
			}
			lang.nextStep();
			
		}
		vierterSchritt.unhighlight("formel");
		SourceCode funfterSchritt = lang.newSourceCode(new Coordinates(450, 0), "eins", null, header);
		funfterSchritt.addCodeLine( "e) Shift nach Indeces:" , "formel", 1, null);
		funfterSchritt.addCodeLine("", "", 1, null);
		funfterSchritt.highlight("formel");
		lang.nextStep();
		vars.discard("Doppelte-Fouriertransformation");
		lang.nextStep();
		vars.discard("Ruecktransformation-Ergebnis");
		
		
		
		for(int i=0; i<ergebnisRuecktransformation.length;i++) {
			funfterSchritt.addCodeLine("C", "c", 2, null);
			funfterSchritt.addCodeElement(Integer.toString(i), "C"+Integer.toString(i), 2, null);
			funfterSchritt.addCodeElement(" = ", "=", 2, null);
			lang.nextStep();
			
			int[] shiftedArray = rotate(ergebnisBinaer[i],32-(i*4));
			ergebnisBinaer[i] = shiftedArray;
			for(int e = 0; e<shiftedArray.length; e++) {
				funfterSchritt.addCodeElement(Integer.toString(ergebnisBinaer[i][e]), "bit"+Integer.toString(i)+ Integer.toString(e), true, 1, null);
			}
			lang.nextStep();
			for(int i2 = 0; i2<ergebnisBinaer.length;i2++) {
				vars.declare("String","C"+Integer.toString(i2), Arrays.toString(ergebnisBinaer[i2]),VALUE);
			}
			lang.nextStep();
		}
		funfterSchritt.unhighlight("formel");
		SourceCode sechsterSchritt = lang.newSourceCode(new Coordinates(450, 200), "eins", null, header);
		sechsterSchritt.addCodeLine( "e) Binaere Addition:" , "formel", 1, null);
		sechsterSchritt.addCodeLine("", "", 1, null);
		sechsterSchritt.highlight("formel");
		sechsterSchritt.addCodeLine("=>", "", 2, null);
		lang.nextStep();
		
		int counter = 0;
		int[] endErgebnisArray = new int[32];
		
		for(int i = 0; i<32;i++) {
			for(int j = 0;j<8;j++) {
				if(ergebnisBinaer[j][31-i] == 1) {
					counter++;
				}
			}
			
			if(counter == 0) {
				backwards(0,i);
				endErgebnisArray[i] = 0;
				lang.nextStep();
			} else if(counter > 1 && counter % 2 == 0) {
				counter = 1;
				backwards(0,i);
				endErgebnisArray[i] = 0;
				lang.nextStep();
			} else if(counter > 1 && counter % 2 == 1) {
				counter = 1;
				backwards(1,i);
				endErgebnisArray[i] = 1;
				lang.nextStep();
			} else {
				backwards(1,i);
				counter = 0;
				endErgebnisArray[i] = 1;
				lang.nextStep();
			}
	
			
		}
		lang.nextStep();
		endErgebnis = 0;
		for(int i = 0; i<endErgebnisArray.length;i++) {
			endErgebnis = (int) (endErgebnis + (Math.pow(2, i) * endErgebnisArray[i]));
		}
		sechsterSchritt.unhighlight("formel");
		sechsterSchritt.addCodeLine("", "", 1, null);
		sechsterSchritt.addCodeLine("=>     "+ A + " * " + B +" = " + endErgebnis, "Ergebnis!!!", 2, null);
		sechsterSchritt.highlight("Ergebnis!!!");
		vars.declare("int","Ergebnis", String.valueOf(endErgebnis),VALUE);
		lang.nextStep("Endergebnis");
		lang.hideAllPrimitives();
	}
	
	private void iteration5() {
		
		Rect r = lang.newRect(new Coordinates(0, 5), new Coordinates(250, 35), "Box", null, rp);
    	
    	SourceCode titel = lang.newSourceCode(new Coordinates(5, 0), "header", null, titelFont3);
    	titel.addCodeLine("Schonhage-Strassen-Algorithmus", "titel", 0, null);
		
		SourceCode ersterSchritt = lang.newSourceCode(new Coordinates(20, 30), "eins", null, header);
		ersterSchritt.addCodeLine( "1. Eingabe:", "eingabe", 1, null);
		ersterSchritt.addCodeLine("", "",0, null);
		ersterSchritt.addCodeLine("Eingabe A: "+Integer.toString(A), "eingabe_A", 1, null);
		ersterSchritt.addCodeLine("Eingabe B: "+Integer.toString(B), "eingabe_B", 1, null);
		
		SourceCode zweiterSchritt = lang.newSourceCode(new Coordinates(20, 110), "zwei", null, header);
		zweiterSchritt.addCodeLine( "2. Folgen fuer Fouriertransformation:", "folge_FT", 1, null);
		zweiterSchritt.addCodeLine("", "",0, null);
		zweiterSchritt.addCodeLine("A-Folge: " + Arrays.toString(FolgeA), "folge_A", 1, null);
		zweiterSchritt.addCodeLine("B-Folge: " + Arrays.toString(FolgeB), "folge_A", 1, null);
		
		SourceCode dritterSchritt = lang.newSourceCode(new Coordinates(20, 190), "drei", null, header);
		dritterSchritt.addCodeLine( "3. Fouriertransformierte:", "FT_folgen", 1, null);
		dritterSchritt.addCodeLine("", "",0, null);
		
		
		SourceCode tmpA = lang.newSourceCode(new Coordinates(30, 235), "Atxt", null, header);
		tmpA.addCodeLine("A-FT: ", "aFT", 0, null);
		StringArray FolgeAFT = createComplexArray(new Coordinates(65,240), ass, "ftA");
		FolgeAFT.showIndices(false, null, null);
		SourceCode tmpB = lang.newSourceCode(new Coordinates(30, 265), "Btxt", null, header);
		tmpB.addCodeLine("B-FT: ", "aFT", 0, null);
		StringArray FolgeBFT = createComplexArray(new Coordinates(65,270), bss, "ftB");
		FolgeBFT.showIndices(false, null, null);
		SourceCode tmpC = lang.newSourceCode(new Coordinates(30, 295), "Ctxt", null, header);
		tmpC.addCodeLine("A * B: ", "abFT", 0, null);
		StringArray FolgeCFT = createComplexArray(new Coordinates(65,300), cs, "ftC");
		FolgeCFT.showIndices(false, null, null);
		SourceCode tmpCC = lang.newSourceCode(new Coordinates(30, 325), "CCtxt", null, header);
		tmpCC.addCodeLine("C-FT: ", "ccFT", 0, null);
		StringArray FolgeCCFT = createComplexArray(new Coordinates(65,330), doppelteFourierTransformationErgebnis, "ftCC");
		FolgeCCFT.showIndices(false, null, null);
		
		SourceCode vierterSchritt = lang.newSourceCode(new Coordinates(300, 0), "vier", null, header);
		vierterSchritt.addCodeLine( "4. Ruecktransformation:", "eingabe", 1, null);
		vierterSchritt.addCodeLine("", "",0, null);
		vierterSchritt.addCodeLine("C-Folge: " + Arrays.toString(gerundetErgebnisRuecktransformation), "AB", 1,null);
		
		SourceCode funfterSchritt = lang.newSourceCode(new Coordinates(300, 80), "funf", null, header);
		funfterSchritt.addCodeLine( "5. Ergebnis:", "eingabe", 1, null);
		funfterSchritt.addCodeLine("", "",0, null);
		funfterSchritt.addCodeLine(Integer.toString(A) + " * " + Integer.toString(B) + " = " +Integer.toString(endErgebnis), "ergebnis", 1,null);
		funfterSchritt.addCodeLine("", "",0, null);
		funfterSchritt.addCodeLine("=> Damit haben wir gezeigt, dass mit Hilfe des Fourierraums" , "text1", 1, null);
		funfterSchritt.addCodeLine("die Multiplikation zweier Zahlen A und B in einer Laufzeit von O(n*log(n)*log(log(n))) möglich ist. ", "text2", 1, null);
	}

	
	
	
	
	public void backwards(int d, int iter) {

			SourceCode tmp = lang.newSourceCode(new Coordinates(727-(iter*7), 230), "eins", null, header);
			tmp.addCodeLine( Integer.toString(d) , "ergebnis", 1, null);
	}
	
	public int[] convertBinary(int num) {
		int binary[] = new int[32];
		int index = 0;
		while (num > 0) {
			binary[index++] = num % 2;
			num = num / 2;
		}
		return binary;
	}
	
	public static int[] rotate(int[] arr, int order) {
		if (arr == null){
		    throw new IllegalArgumentException("Illegal argument!");
		}
	 
		if(order<0) {
			return arr;
		}
		for (int i = 0; i < order; i++) {
			for (int j = arr.length - 1; j > 0; j--) {
				int temp = arr[j];
				arr[j] = arr[j - 1];
				arr[j - 1] = temp;
			}
		}
		
		return arr;
	}

	public ComplexNumberSSA[] swapIndeces(ComplexNumberSSA[] a, int pos1, int pos2) {
		ComplexNumberSSA tmp = a[pos2];
		a[pos2] = a[pos1];
		a[pos1] = tmp;
		
		return a;
	}

	public int calcModulo(int i, int k) {
		if (i == 0) {
			return 0;
		} else {
			return (i%k) + k;
		}
	}
	
	
	
	

	private StringArray createComplexArray(Node node, ComplexNumberSSA[] array, String name) {
		String[] tmp = new String[array.length];
		for(int i = 0; i<array.length; i++) {
			tmp[i] = array[i].toString2();
		}
		
		StringArray result = lang.newStringArray(node, tmp, name, null, this.array);
		return result;
	}	
	
	public void createEinheitswurzel(Language lang, int x, int y, int K) {
		SourceCodeProperties sp = new SourceCodeProperties();
		sp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		sp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 17));

		SourceCodeProperties sp2 = new SourceCodeProperties();
		sp2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		sp2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 12));

		PolylineProperties pp = new PolylineProperties();
		pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		SourceCode sc0 = lang.newSourceCode(new Coordinates(x, y), "0", null, sp);
		sc0.addCodeLine("W", "W", 0, null);
		sc0.addCodeElement("=", "=", 0, null);
		sc0.addCodeElement("e", "e", 0, null);

		if (K > 0) {
			sc0.addCodeElement("  =", "=", 0, null);
			sc0.addCodeElement(Einheitswurzel.toString2(), "Ergebnis", 0, null);
		}

		Node[] p = new Node[] { new Coordinates(x + 50, y + 15), new Coordinates(x + 70, y + 15) };
    Polyline pl = lang.newPolyline(p, "line", null, pp);

		SourceCode sc1 = lang.newSourceCode(new Coordinates(x + 50, y - 10), "potenz", null, sp2);
		sc1.addCodeLine("2*", "2", 0, null);
		sc1.addCodeElement("pi*", "pi", true, 0, null);
		sc1.addCodeElement("i", "i", true, 0, null);

		SourceCode sc2 = lang.newSourceCode(new Coordinates(x + 55, y), "lower", null, sp2);
		sc2.addCodeLine("K", "2", 0, null);

	}
	
	public void createSumSymbolA(Language lang, int x, int y, int counter, int s, int K, int[] array, ComplexNumberSSA w,
			boolean formel, IntArray a, IntArray b) {
		Node[] p = new Node[] { new Coordinates(x + 40, y + 30), new Coordinates(x, y + 30),
				new Coordinates(x + 20, y + 45), new Coordinates(x, y + 60), new Coordinates(x + 40, y + 60) };

		PolylineProperties pp = new PolylineProperties();
		pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		Polyline pl = lang.newPolyline(p, "line", null, pp);
		
		SourceCode sc = lang.newSourceCode(new Coordinates(x + 5, y), "sc", null, text);
		sc.addCodeLine(Integer.toString((int)Math.pow(2, K)) + " - 1", "up", 0, null);

		

		if (formel) {

			SourceCode sc3 = lang.newSourceCode(new Coordinates(x - 30, y + 25), "as", null, text);
			sc3.addCodeLine("as = ", "a", 0, null);

			SourceCode sc4 = lang.newSourceCode(new Coordinates(x + 45, y + 25), "w", null, text);
			sc4.addCodeLine("W", "w", 0, null);

			SourceCode sc5 = lang.newSourceCode(new Coordinates(x + 55, y + 20), "s", null, text);
			sc5.addCodeLine("s", "w", 0, null);

			SourceCode sc6 = lang.newSourceCode(new Coordinates(x + 60, y + 20), "t", null, text);
			sc6.addCodeLine("t", "w", 0, null);

			SourceCode sc7 = lang.newSourceCode(new Coordinates(x + 65, y + 25), "a", null, text);
			sc7.addCodeLine("A", "a", 0, null);

			SourceCode sc8 = lang.newSourceCode(new Coordinates(x + 75, y + 29), "t2", null, text);
			sc8.addCodeLine("t", "t2", 0, null);

		} else {
			SourceCode sc3 = lang.newSourceCode(new Coordinates(x - 30, y + 25), "as", null, text);
			sc3.addCodeLine("a" + Integer.toString(s) + "= ", "a", 0, null);

			SourceCode sc4 = lang.newSourceCode(new Coordinates(x + 45, y + 25), "w", null, text);
			sc4.addCodeLine("W", "w", 0, null);

			SourceCode sc5 = lang.newSourceCode(new Coordinates(x + 55, y + 20), "s", null, text);
			sc5.addCodeLine(Integer.toString(s), "slel", 0, null);

			SourceCode sc6 = lang.newSourceCode(new Coordinates(x + 63, y + 20), "t", null, text);
			sc6.addCodeLine("t", "w", 0, null);

			SourceCode sc7 = lang.newSourceCode(new Coordinates(x + 67, y + 25), "a", null, text);
			sc7.addCodeLine("A  =", "a", 0, null);

			SourceCode sc8 = lang.newSourceCode(new Coordinates(x + 75, y + 29), "t2", null, text);
			sc8.addCodeLine("t", "t2", 0, null);

			lang.nextStep();
			SourceCode sc9 = lang.newSourceCode(new Coordinates(x + 93, y + 25), "rechnung", null, text);
			sc9.addCodeLine(Integer.toString(array[0]), "a0", 0, null);
			sc9.highlight("a0");
			a.highlightCell(0, null, null);
			lang.nextStep();
			a.unhighlightCell(0, null, null);
			sc9.unhighlight("a0");
			
			SourceCode counter2 = lang.newSourceCode(new Coordinates(x + 5, y + 50), "counter2", null, text);
			counter2.addCodeLine("t = 0", "counter2", 0, null);
			for (int i = 1; i < Math.pow(2, K); i++) {
				sc9.addCodeElement("+ W^(", "numbers", 0, null);
				sc9.addCodeElement(Integer.toString(s), "strol", true, 0, null);
				sc9.highlight("strol");
				sc5.highlight("slel");
				lang.nextStep();
				sc9.addCodeElement("*", "*", true, 0, null);
				sc9.unhighlight("strol");
				sc5.unhighlight("slel");
				sc9.addCodeElement(Integer.toString(i), "itrol", true, 0, null);
				sc9.highlight("itrol");
				counter2.highlight("counter2");
				lang.nextStep();
				sc9.unhighlight("itrol");
				counter2.unhighlight("counter2");
				sc9.addCodeElement(")", ")", true, 0, null);
				sc9.addCodeElement(" *", "*1", true, 0, null);
				sc9.addCodeElement(Integer.toString(array[i]), "numbers", true, 0, null);
				sc9.highlight("numbers");
				a.highlightCell(i, null, null);
				lang.nextStep();
				a.unhighlightCell(i, null, null);
				sc9.unhighlight("numbers");
			}

			ComplexNumberSSA result = new ComplexNumberSSA(array[0], 0);
			for (int i = 1; i < Math.pow(2, K); i++) {

				result = ComplexNumberSSA.add(result, ComplexNumberSSA.multiply(new ComplexNumberSSA(array[i], 0), ComplexNumberSSA.pow(w, s * i)));
			}

			ComplexNumberSSA roundResult = new ComplexNumberSSA(result.getReal(), result.getImaginary());
			sc9.addCodeElement(" = " + roundResult.toString2(), "ergebnis", 0, null);
			
		}
	}
	
	public void createSumSymbolB(Language lang, int x, int y, int counter, int s, int K, int[] array, ComplexNumberSSA w, boolean formel, IntArray a, IntArray b) {
		Node[] p = new Node[] { new Coordinates(x + 40, y + 30), new Coordinates(x, y + 30),
				new Coordinates(x + 20, y + 45), new Coordinates(x, y + 60), new Coordinates(x + 40, y + 60) };

		PolylineProperties pp = new PolylineProperties();
		pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

		Polyline pl = lang.newPolyline(p, "line", null, pp);


		SourceCode sc = lang.newSourceCode(new Coordinates(x + 5, y), "sc", null, text);
		sc.addCodeLine(Integer.toString((int)Math.pow(2, K)) + " - 1", "up", 0, null);

		if (formel) {

			SourceCode sc3 = lang.newSourceCode(new Coordinates(x - 30, y + 25), "bs", null, text);
			sc3.addCodeLine("bs = ", "a", 0, null);

			SourceCode sc4 = lang.newSourceCode(new Coordinates(x + 45, y + 25), "w", null, text);
			sc4.addCodeLine("W", "w", 0, null);

			SourceCode sc5 = lang.newSourceCode(new Coordinates(x + 55, y + 20), "s", null, text);
			sc5.addCodeLine("s", "w", 0, null);

			SourceCode sc6 = lang.newSourceCode(new Coordinates(x + 60, y + 20), "t", null, text);
			sc6.addCodeLine("t", "w", 0, null);

			SourceCode sc7 = lang.newSourceCode(new Coordinates(x + 65, y + 25), "b", null, text);
			sc7.addCodeLine("B", "a", 0, null);

			SourceCode sc8 = lang.newSourceCode(new Coordinates(x + 75, y + 29), "t2", null, text);
			sc8.addCodeLine("t", "t2", 0, null);

		} else {
			SourceCode sc3 = lang.newSourceCode(new Coordinates(x - 30, y + 25), "bs", null, text);
			sc3.addCodeLine("b" + Integer.toString(s) + "= ", "b", 0, null);

			SourceCode sc4 = lang.newSourceCode(new Coordinates(x + 45, y + 25), "w", null, text);
			sc4.addCodeLine("W", "w", 0, null);

			SourceCode sc5 = lang.newSourceCode(new Coordinates(x + 55, y + 20), "s", null, text);
			sc5.addCodeLine(Integer.toString(s), "slel", 0, null);

			SourceCode sc6 = lang.newSourceCode(new Coordinates(x + 63, y + 20), "t", null, text);
			sc6.addCodeLine("t", "w", 0, null);

			SourceCode sc7 = lang.newSourceCode(new Coordinates(x + 65, y + 25), "b", null, text);
			sc7.addCodeLine("B  =", "b", 0, null);

			SourceCode sc8 = lang.newSourceCode(new Coordinates(x + 75, y + 29), "t2", null, text);
			sc8.addCodeLine("t", "t2", 0, null);

			lang.nextStep();
			SourceCode sc9 = lang.newSourceCode(new Coordinates(x + 93, y + 25), "rechnung", null, text);
			sc9.addCodeLine(Integer.toString(array[0]), "b0", 0, null);
			sc9.highlight("b0");
			b.highlightCell(0, null, null);
			lang.nextStep();
			b.unhighlightCell(0, null, null);
			sc9.unhighlight("b0");
			
			SourceCode counter2 = lang.newSourceCode(new Coordinates(x + 5, y + 50), "counter2", null, text);
			counter2.addCodeLine("t = 0", "counter2", 0, null);
			for (int i = 1; i < Math.pow(2, K); i++) {
				
				sc9.addCodeElement("+ W^(", "numbers", 0, null);
				sc9.addCodeElement(Integer.toString(s), "strol", true, 0, null);
				sc9.highlight("strol");
				sc5.highlight("slel");
				lang.nextStep();
				sc9.addCodeElement("*", "*", true, 0, null);
				sc9.unhighlight("strol");
				sc5.unhighlight("slel");
				sc9.addCodeElement(Integer.toString(i), "itrol", true, 0, null);
				sc9.highlight("itrol");
				counter2.highlight("counter2");
				lang.nextStep();
				sc9.unhighlight("itrol");
				counter2.unhighlight("counter2");
				sc9.addCodeElement(")", ")", true, 0, null);
				sc9.addCodeElement(" *", "*1", true, 0, null);
				sc9.addCodeElement(Integer.toString(array[i]), "numbers", true, 0, null);
				sc9.highlight("numbers");
				b.highlightCell(i, null, null);
				lang.nextStep();
				b.unhighlightCell(i, null, null);
				sc9.unhighlight("numbers");
			}

			ComplexNumberSSA result = new ComplexNumberSSA(array[0], 0);

			for (int i = 1; i < Math.pow(2, K); i++) {

				result = ComplexNumberSSA.add(result, ComplexNumberSSA.multiply(new ComplexNumberSSA(array[i], 0), ComplexNumberSSA.pow(w, s * i)));
			}

			ComplexNumberSSA roundResult = new ComplexNumberSSA(result.getReal(), result.getImaginary());
			sc9.addCodeElement(" = " + roundResult.toString2(), "ergebnis", 0, null);
		}
	}
	
	private static String[] complexArrayToStringArray(ComplexNumberSSA[] input) {
		String[] result = new String[input.length];
		
		for(int e = 0; e < input.length;e++) {
			result[e] = input[e].toString2();
		}
		return result;
	}


    private int[] berechneBinaerArrays(int input, int length, boolean delete) {
		boolean[] bits = new boolean[length];
		int[] binaer = new int[length];

		for (int i = length - 1; i >= 0; i--) {
			bits[i] = (input & (1 << i)) != 0;
		}

		for (int i = 0; i < bits.length; i++) {
			if (bits[i] == true) {
				binaer[i] = 1;
			} else {
				binaer[i] = 0;
			}
		}

		int[] result;
		
		if (delete) {
			result = deleteLeadingZeros(reverse(binaer));
		} else {
			result = reverse(binaer);
		}
		return result;
	}
	
	private int[] reverse(int[] array) {
		for (int i = 0; i < array.length / 2; i++) {
			int tmp = array[i];
			array[i] = array[array.length - 1 - i];
			array[array.length - 1 - i] = tmp;
		}
		return array;
	}

	private int[] deleteLeadingZeros(int[] array) {
		int[] result;

		for (int i = 0; i < array.length; i++) {

			if (array[i] == 1) {
				result = new int[array.length - i];
				for (int k = 0; k < array.length - i; k++) {
					result[k] = array[i + k];
				}
				return result;
			}
		}
		return null;
	}
    
    
	double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}
    

    public String getName() {
        return "Schonhage-Strassen-Algorithmus";
    }

    public String getAlgorithmName() {
        return "Schonhage-Strassen-Algorithmus";
    }

    public String getAnimationAuthor() {
        return "Alexander Ziesing";
    }

    public String getDescription(){
        return "Der Schonhage-Strassen-Algorithmus ist ein Algorithmus zur Multiplikation zweier n-stelliger ganzer"
 +"\n"
 +"Zahlen. Der Algorithmus basiert auf einer sehr schnellen Variante der diskreten Fourier-Transformation"
 +"\n"
 +"sowie einem geschickten Wechsel zwischen der Restklassen Arithmetik in endlichen Zahlenringen."
 +"\n"
 +"\n"
 +"Der Schonhage-Strassen-Algorithmus terminiert in O(n*log(n)*log(log(n))).";
    }

    public String getCodeExample(){
        return " Um zwei ganze Zahlen a und b zu multiplizieren, wird im Groben folgendes Schema angewandt:"
 +"\n"
 +"\n"
 +"1. Aufspaltung der Zahlen (in Binaerdarstellung) a und b in Stuecke passender Laenge"
 +"\n"
 +"\n"
 +"2. Diskrete Fourier-Transformation der beiden Stueckfolgen"
 +"\n"
 +"\n"
 +"3. Komponentenweise Multiplikation der transformierten Stuecke"
 +"\n"
 +"\n"
 +"4. Ruecktransformation (inverse Fouriertransformation) der Ergebnisse"
 +"\n"
 +"\n"
 +"5. Zusammensetzen der Ergebnisstuecke zur Ergebniszahl";
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


	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		int a = (Integer)arg1.get("A");
		int b = (Integer)arg1.get("B");
		SourceCodeProperties h = (SourceCodeProperties)arg0.getPropertiesByName("header");
		int h1 = (int) h.getItem("size").get();
		
		if(a > 9999 || b > 9999 || a < 1000 || b < 1000) {
			throw new IllegalArgumentException("Eingabeparameter muss zwischen 1000 und 9999 liegen!");
		}
		return true;
	}

}