/*
 * ChineseRem.java
 * Alessandro Noli, Paul Youssef, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.QuestionGroupModel;

import java.awt.Font;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.Locale;

import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.OffsetFromLastPosition;

public class ChineseRem implements Generator {
    private Language lang;
    private int a1;
    private int a2;
    private int a3;
    private int m1;
    private int m2;
    private int m3;
    private int x; // not a primitive
    private SourceCodeProperties sourceCodeProperties;

    
    public Translator trans;
    private Locale loc;
    
    public ChineseRem(String path, Locale l){
    	loc = l;
    	trans = new Translator(path, l);
    	
    }

    public void init(){
        lang = new AnimalScript("Chinese Remainder Theorem", "Alessandro Noli, Paul Youssef", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        a1 = (Integer)primitives.get("a1");
        a2 = (Integer)primitives.get("a2");
        a3 = (Integer)primitives.get("a3");
        m1 = (Integer)primitives.get("m1");
        m2 = (Integer)primitives.get("m2");
        m3 = (Integer)primitives.get("m3");
        sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProperties");


       lang.setStepMode(true);
        
       //lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		
	
				
			
		//lang.finalizeGeneration();
		introduction();
		problem();
		sourceCode();
		summary();
		references();
        
        return lang.toString();
    }
    

    private void introduction() {
    	
		OffsetFromLastPosition offset1 = new OffsetFromLastPosition(0, 30);
		OffsetFromLastPosition offset2 = new OffsetFromLastPosition(20, 40);
	
		// title
		Coordinates titleCoordinates = new Coordinates(20, 30);
		Coordinates subtitleCoordinates = new Coordinates(40, 150);
	
		TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 20));
		
	
		
		
		lang.newText(titleCoordinates, trans.translateMessage("title"), "title",
				null, titleProps);
	
		lang.nextStep(trans.translateMessage("introduction"));
		// description
		TextProperties descProps = new TextProperties();
		descProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 14));
		
		lang.newText(offset2, trans.translateMessage("desc1"), "desc1", null, descProps);
	
		lang.nextStep();
		
		lang.newText(offset1, trans.translateMessage("desc2"), "desc2", null, descProps);
	
		lang.nextStep();
	
		// Applications in Cryptography
		TextProperties subtitleProps = new TextProperties();
		subtitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 14));
	
		lang.newText(subtitleCoordinates, trans.translateMessage("appsInCrypt"), "appsInCrypto", null,
				subtitleProps);
	
		lang.nextStep();
		lang.newText(offset2, trans.translateMessage("app1"), "app1", null, descProps);
	
		lang.nextStep();
		lang.newText(offset1, trans.translateMessage("app2"), "app2", null, descProps);
	
		lang.nextStep();
		lang.hideAllPrimitives();
	}

	private void problem() {
		
		OffsetFromLastPosition offset1 = new OffsetFromLastPosition(0,30);
		OffsetFromLastPosition offset2 = new OffsetFromLastPosition(20,40);
		OffsetFromLastPosition firstPointOffset = new OffsetFromLastPosition(25,10);
		OffsetFromLastPosition interPointsOffset = new OffsetFromLastPosition(0,10);
		OffsetFromLastPosition lastPointOffset = new OffsetFromLastPosition(-25,20);
		OffsetFromLastPosition textAgainOffset = new OffsetFromLastPosition(-20,40);
		

		// title
		Coordinates titleCoordinates = new Coordinates(20, 30);
	
		TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY,  new Font(Font.SANS_SERIF,Font.BOLD,20));
		
		lang.newText(titleCoordinates, trans.translateMessage("title"), "title", null,  titleProps);
		lang.nextStep( trans.translateMessage("presentationOfProblem"));
		
		
		//problem 
		TextProperties problemProps = new TextProperties();
		problemProps.set(AnimationPropertiesKeys.FONT_PROPERTY,  new Font(Font.SANS_SERIF,Font.PLAIN,14));
		
		
		lang.newText(offset2, trans.translateMessage("problem1"), "problem1", null,  problemProps);
		
		lang.nextStep();
		//System
		TextProperties sysProps = new TextProperties();
		sysProps.set(AnimationPropertiesKeys.FONT_PROPERTY,  new Font(Font.SANS_SERIF,Font.ITALIC + Font.BOLD,16));
		String con1 = "x ≡ a1 mod m1";
		lang.newText(offset2, con1, "con1", null,  sysProps);

		String con2 = "x ≡ a2 mod m2";
		lang.newText(offset1, con2, "con2", null,  sysProps);
		
		lang.newText(firstPointOffset, ".", "point1", null,  sysProps);
		lang.newText(interPointsOffset, ".", "point2", null,  sysProps);
		lang.newText(interPointsOffset, ".", "point3", null,  sysProps);

		String conn = "x ≡ an mod mn";
		lang.newText(lastPointOffset, conn, "conn", null,  sysProps);
		
		
		lang.nextStep();
		
		lang.newText(textAgainOffset, trans.translateMessage("problem2"), "problem2", null,  problemProps);
		
		lang.nextStep();
		// Calculations steps:
		
		//title
		TextProperties subtitleProps = new TextProperties();
		subtitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 14));
		
		lang.newText(new OffsetFromLastPosition(0,40), trans.translateMessage("calSteps"), "calStep", null,  subtitleProps);
		lang.nextStep(trans.translateMessage("calSteps2"));
		
		//steps
		
		lang.newText(offset2, trans.translateMessage("step1"), "step1", null,  problemProps);
		lang.nextStep();
		
		lang.newText(offset1, trans.translateMessage("step2"), "step2", null,  problemProps);
		lang.nextStep();
		
		lang.newText(offset1, trans.translateMessage("step3"), "step3", null,  problemProps);
		lang.nextStep();
		
		lang.newText(offset1, trans.translateMessage("step4"), "step4", null,  problemProps);
		
		lang.nextStep();
		lang.hideAllPrimitives();
	}
	
	private void sourceCode() {
	 
		//title of generator
		Coordinates titleCoordinates = new Coordinates(20, 30);

		TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
					Font.SANS_SERIF, Font.BOLD, 20));
		lang.newText(titleCoordinates, trans.translateMessage("title"), "title", null, titleProps);
	  
		lang.nextStep();
	  
		// the title code
		TextProperties subtitleProps = new TextProperties();
		subtitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 18));
		
		lang.newText(new Coordinates(40, 60), "Code:", "codetitle",null, subtitleProps);
		
		lang.nextStep("Code");
		
		// now, create the source code entity
		SourceCode sc = lang.newSourceCode(new Coordinates(40, 100),
				"sourceCode", null, sourceCodeProperties);
		// addCodeLine(code, label, indentation, delay)
		sc.addCodeLine(
				"public int chineseRemainder(int a1, int m1, int a2, int m2, int a3, int m3){",
				null, 0, null);
		sc.addCodeLine("int M = m1*m2*m3;", null, 1, null);
		sc.addCodeLine(" ", null, 0, null);
		sc.addCodeLine("int M1 = M/m1;", null, 1, null);
		sc.addCodeLine("int M2 = M/m2;", null, 1, null);
		sc.addCodeLine("int M3 = M/m3;", null, 1, null);
		sc.addCodeLine(" ", null, 0, null);
	
		sc.addCodeLine("int y1 = inverse(M1, m1);", null, 1, null);
		sc.addCodeLine("int y2 = inverse(M2, m2);", null, 1, null);
		sc.addCodeLine("int y3 = inverse(M3, m3);", null, 1, null);
		sc.addCodeLine(" ", null, 0, null);
		sc.addCodeLine("int x  = (a1*y1*M1 + a2*y2*M2 + a3*y3*M3) % M;", null,
				1, null);
		sc.addCodeLine(" ", null, 0, null);
		sc.addCodeLine("return x;", null, 1, null);
		sc.addCodeLine("}", null, 0, null);
	
		lang.nextStep();
		chineseRemainder(sc, a1, m1, a2, m2, a3, m3);
		
		//note
		TextProperties noteProps = new TextProperties();
		noteProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 14));
		
		lang.nextStep();
		lang.newText(new Coordinates(40, 420), trans.translateMessage("note"), "note", null,  noteProps);
		
		TextProperties textprops = new TextProperties();
		textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.PLAIN, 14));
		lang.nextStep();
		lang.newText(new OffsetFromLastPosition(20, 30), trans.translateMessage("noteText"), "noteText", null, textprops);
		
		lang.nextStep();
		lang.hideAllPrimitives();
	}
	

private int chineseRemainder(SourceCode sc, int a1, int m1, int a2, int m2,
		int a3, int m3) {
	//the title example
	TextProperties titleProps = new TextProperties();
	titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
			Font.SANS_SERIF, Font.BOLD, 18));
	
	lang.newText(new Coordinates(700, 60), trans.translateMessage("example"), "exampletitle",null, titleProps);
	
	lang.nextStep(trans.translateMessage("example"));
	
	
	OffsetFromLastPosition offset1 = new OffsetFromLastPosition(0, 20);
	OffsetFromLastPosition offset2 = new OffsetFromLastPosition(0, 30);

	sc.highlight(0);

	// line1
	lang.nextStep();
	sc.toggleHighlight(0, 1);
	StringBuilder sb = new StringBuilder();
	sb.append("M = m1*m2*m3 = ");
	sb.append(m1);
	sb.append("*");
	sb.append(m2);
	sb.append("*");
	sb.append(m3);
	sb.append(" = ");

	int M = m1 * m2 * m3;

	sb.append(M);
	TextProperties textprops = new TextProperties();
	textprops.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
			Font.SANS_SERIF, Font.PLAIN, 14));
	Coordinates calcCoordinates = new Coordinates(700, 120);
	lang.newText(calcCoordinates, sb.toString(), "line1", null, textprops);

	// line2
	lang.nextStep();
	sc.toggleHighlight(1, 3);
	sb = new StringBuilder();
	sb.append("M1 = M / m1 = ");
	sb.append(M);
	sb.append(" / ");
	sb.append(m1);
	sb.append(" = ");
	int M1 = M / m1;
	sb.append(M1);

	lang.newText(offset2, sb.toString(), "line2", null, textprops);

	// line3
	lang.nextStep();
	sc.toggleHighlight(3, 4);
	sb = new StringBuilder();
	sb.append("M2 = M / m2 = ");
	sb.append(M);
	sb.append(" / ");
	sb.append(m2);
	sb.append(" = ");
	int M2 = M / m2;
	sb.append(M2);

	lang.newText(offset1, sb.toString(), "line3", null, textprops);

	// line4
	lang.nextStep();
	sc.toggleHighlight(4, 5);
	sb = new StringBuilder();
	sb.append("M3 = M / m3 = ");
	sb.append(M);
	sb.append(" / ");
	sb.append(m3);
	sb.append(" = ");
	int M3 = M / m3;
	sb.append(M3);

	lang.newText(offset1, sb.toString(), "line4", null, textprops);

	int y1 = inverse(M1, m1);


	// line5
	lang.nextStep();
	sc.toggleHighlight(5, 7);
	sb = new StringBuilder();
	sb.append("y1 = (M1)^-1 mod m1 = ");
	sb.append("(");
	sb.append(M1);
	sb.append(")^-1 mod ");
	sb.append(m1);
	sb.append(" = ");
	sb.append(y1);

	lang.newText(offset2, sb.toString(), "line5", null, textprops);

	// line6
	lang.nextStep();
	sc.toggleHighlight(7, 8);
	sb = new StringBuilder();
	int y2 = inverse(M2, m2);
	sb.append("y2 = (M2)^-1 mod m2 = ");
	sb.append("(");
	sb.append(M2);
	sb.append(")^-1 mod ");
	sb.append(m2);
	sb.append(" = ");
	sb.append(y2);

	lang.newText(offset1, sb.toString(), "line6", null, textprops);

	// line7
	lang.nextStep();
	sc.toggleHighlight(8, 9);
	sb = new StringBuilder();
	int y3 = inverse(M3, m3);
	sb.append("y3 = (M3)^-1 mod m3 = ");
	sb.append("(");
	sb.append(M3);
	sb.append(")^-1 mod ");
	sb.append(m3);
	sb.append(" = ");
	sb.append(y3);

	lang.newText(offset1, sb.toString(), "line7", null, textprops);

	// line8
	lang.nextStep();
	sc.toggleHighlight(9, 11);
	sc.highlight(13);
	sb = new StringBuilder();
	int x = (a1 * y1 * M1 + a2 * y2 * M2 + a3 * y3 * M3) % M;

	sb.append("x  = (a1*y1*M1 + a2*y2*M2 + a3*y3*M3) mod M = ");
	sb.append("(");
	sb.append(a1);
	sb.append("*");
	sb.append(y1);
	sb.append("*");
	sb.append(M1);
	sb.append(" + ");

	sb.append(a2);
	sb.append("*");
	sb.append(y2);
	sb.append("*");
	sb.append(M2);
	sb.append(" + ");

	sb.append(a3);
	sb.append("*");
	sb.append(y3);
	sb.append("*");
	sb.append(M3);
	sb.append(")");

	sb.append(" = ");
	sb.append(x);

	lang.newText(offset2, sb.toString(), "line8", null, textprops);

	lang.nextStep();
	
	sc.unhighlight(11);
	sc.unhighlight(13);
	
	// set x for summary
	this.x = x;
	
	return x;
}

	private void summary() {
	//title of generator
	Coordinates titleCoordinates = new Coordinates(20, 30);

	TextProperties titleProps = new TextProperties();
	titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 20));
	lang.newText(titleCoordinates, trans.translateMessage("title"), "title", null, titleProps);

	lang.nextStep();
	
	// the title summary
	TextProperties subtitleProps = new TextProperties();
	subtitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
			Font.SANS_SERIF, Font.BOLD, 18));
	
	lang.newText(new Coordinates(40, 60), trans.translateMessage("summary1"), "summary",null, subtitleProps);
	
	lang.nextStep(trans.translateMessage("summary2"));
	
	// the text summary
	TextProperties summaryProps = new TextProperties();
	summaryProps.set(AnimationPropertiesKeys.FONT_PROPERTY,  new Font(Font.SANS_SERIF,Font.PLAIN,14));
	StringBuilder summary = new StringBuilder();
	
	summary.append(trans.translateMessage("summary3"));
	summary.append(x);
	summary.append(trans.translateMessage("summary4"));
	
	lang.newText( new OffsetFromLastPosition(20, 40), summary.toString(), "summarytext",
			null, summaryProps);
	
	// equations
	
	//equations properties
	
	TextProperties eqsProps = new TextProperties();
	eqsProps.set(AnimationPropertiesKeys.FONT_PROPERTY,  new Font(Font.SANS_SERIF,Font.ITALIC + Font.BOLD,14));
	
	StringBuilder eq1 = new StringBuilder();
	
	eq1.append("x mod m1 = ");
	eq1.append(x);
	eq1.append(" mod ");
	eq1.append(m1);
	eq1.append(" = ");
	eq1.append(a1);
	eq1.append(" = a1");
	
	StringBuilder eq2 = new StringBuilder();
	
	eq2.append("x mod m2 = ");
	eq2.append(x);
	eq2.append(" mod ");
	eq2.append(m2);
	eq2.append(" = ");
	eq2.append(a2);
	eq2.append(" = a2");
	
	StringBuilder eq3 = new StringBuilder();
	
	eq3.append("x mod m3 = ");
	eq3.append(x);
	eq3.append(" mod ");
	eq3.append(m3);
	eq3.append(" = ");
	eq3.append(a3);
	eq3.append(" = a3");
	
	
	lang.newText( new OffsetFromLastPosition(20, 40), eq1.toString(), "eq1",
			null, eqsProps);
	lang.newText( new OffsetFromLastPosition(0, 40), eq2.toString(), "eq2",
			null, eqsProps);
	lang.newText( new OffsetFromLastPosition(0, 40), eq3.toString(), "eq3",
			null, eqsProps);

	lang.nextStep();
	lang.hideAllPrimitives();
	
}

	private void references() {
		// title
		Coordinates titleCoordinates = new Coordinates(20, 30);
	
		TextProperties titleProps = new TextProperties();
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 20));
		lang.newText(titleCoordinates, trans.translateMessage("ref1"), "title",
				null, titleProps);
		
		
		TextProperties refProps = new TextProperties();
		refProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.ITALIC, 14));
		String link1 = "https://en.wikipedia.org/wiki/Chinese_remainder_theorem";
		
		lang.nextStep(trans.translateMessage("ref2"));
		
		lang.newText( new OffsetFromLastPosition(20, 40), link1, "link1",
				null, refProps);
		
		//TODO HTML 
//		HtmlDocumentationModel ref = new HtmlDocumentationModel("reference", link1);
//		
//		lang.addDocumentationLink(ref);
		
		
		
		
		
		
		
	}
	
	private int inverse(int n, int p) {
		BigInteger big = BigInteger.valueOf(n)
				.modInverse(BigInteger.valueOf(p));
		return big.intValue();
	}

    public String getName() {
        return trans.translateMessage("title");
    }

    public String getAlgorithmName() {
        return trans.translateMessage("title");
    }

    public String getAnimationAuthor() {
        return "Alessandro Noli, Paul Youssef";
    }

    public String getDescription(){
    	return trans.translateMessage("getdesc");
	}

    public String getCodeExample(){
    		return "	public int chineseRemainder(int a1, int m1, int a2, int m2, int a3, int m3) {"
    				+ "\n"
    				+ "\n"
    				+ "		int M = m1*m2*m3;"
    				+ "\n"
    				+ "		"
    				+ "\n"
    				+ "		int M1 = M / m1;"
    				+ "\n"
    				+ "		int M2 = M / m2;"
    				+ "\n"
    				+ "		int M3 = M / m3;"
    				+ "\n"
    				+ "		"
    				+ "\n"
    				+ "		int y1 = inverse(M1, m1);"
    				+ "\n"
    				+ "		int y2 = inverse(M2, m2);"
    				+ "\n"
    				+ "		int y3 = inverse(M3, m3);"
    				+ "\n"
    				+ "		"
    				+ "\n"
    				+ "		int x  = (a1*y1*M1 + a2*y2*M2 + a3*y3*M3) % M;"
    				+ "\n"
    				+ "		"
    				+ "\n"
    				+ "		return x;"
    				+ "\n"
    				+ "		"
    				+ "\n"
    				+ "	}"
    				+ "\n" + "	";
    	}

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return loc;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }
    
    public boolean validateInput(AnimationPropertiesContainer arg0,
			Hashtable<String, Object> arg1) throws IllegalArgumentException {

		a1 = (Integer) arg1.get("a1");
		a2 = (Integer) arg1.get("a2");
		a3 = (Integer) arg1.get("a3");
		m1 = (Integer) arg1.get("m1");
		m2 = (Integer) arg1.get("m2");
		m3 = (Integer) arg1.get("m3");
		
		// m's are pairwise coprime
		boolean mscoprime = (gcd(m1, m2) == 1 && gcd(m2, m3) == 1 && gcd(m1, m3) == 1);

		// m's are positive 
		boolean mspositive = m1 > 0 && m2 > 0 && m3 > 0;
		
		return mscoprime && mspositive;
	}
    
	private int gcd(int a, int b) {
		if (a == 0)
			return b;
		if (b == 0)
			return a;
		if (a > b)
			return gcd(b, a % b);
		return gcd(a, b % a);
	}
}