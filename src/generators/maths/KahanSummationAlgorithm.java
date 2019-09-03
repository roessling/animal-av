/*
 * TestApi.java
 * Oemer M. Ayar, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Hashtable;
import java.util.Locale;

import de.ahrgr.animal.kohnert.asugen.Rectangle;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.DoubleArray;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import animal.variables.Variable;

public class KahanSummationAlgorithm implements Generator {
    private static Language lang;
    private static KahanSummationAlgorithm s;
	public Rect hRect,iRect;
	public Text header,info_sum,info_c, var_c1,var_c2,var_c3,var_t1,var_t2,var_t3, var_sum,var_y1,var_y2,var_y3, method;
	public Text intro_1,intro_2,intro_3,intro_4,intro_5,intro_6,intro_7,intro_8,intro_9,intro_10,intro_example,intro_11,info_t,end;
	public final  Timing  defaultDuration = new TicksTiming(30);
	private TextProperties headerProps = new TextProperties();
	private TextProperties introProps = new TextProperties();
	private SourceCode sc;
	private static double[] array;
	private ArrayMarker iMarker;
	private SourceCodeProperties sourceCode;
    private double toadd_3;
    private double toadd_1;
    private ArrayProperties arrayProperties;
    private double toadd_2;
    TextProperties infoProps = new TextProperties();
    RectProperties info_rectProps = new RectProperties();
    Variables var;
    DoubleArray da;
    private final String Y = "y";
    private final String SUM = "sum";
    private final String T = "t";
    private final String C_KEY = "c";
	
    /*public KahanSummationAlgorithm() {
        // Store the language object
    	lang = new AnimalScript("Kahan Summation Algorithm", "Oemer M. Ayar", 800, 600);
        // This initializes the step mode. Each pair of subsequent steps has to
        // be divdided by a call of lang.nextStep();
        lang.setStepMode(true);
      }*/
    
    public KahanSummationAlgorithm (Language l) {
    	lang = l;
        lang.setStepMode(true);

        
      }
    
    public KahanSummationAlgorithm() {
    	
    }
	
	
	
	
    public static void main(String[] args) {
        // Create a new language object for generating animation code
        // this requires type, name, author, screen width, screen height
    	lang = new AnimalScript("Kahan Summation Algorithm", "Oemer M. Ayar", 800, 600);
    	s = new KahanSummationAlgorithm(lang);
        array = new double[]{10000.0,3.14159,2.71828};
        
        s.doKahan(array);
        System.out.println(lang);
      }
    
    private void showIntro(){
    	
    	
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.BOLD, 30));
        header = lang.newText(new Coordinates(20, 30), "Kahan Summation Algorithm",
            "header", null, headerProps);
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.decode("#B3EE3A"));
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.decode("#B3EE3A"));
        
        hRect = lang.newRect(new Offset(-5, -5, "header",
                AnimalScript.DIRECTION_NW), new Offset(350, 15 , "header", "SE"), "hRect",
                null, rectProps);
        
        
        
        introProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.LAYOUT_LEFT_TO_RIGHT, 15));
        
        intro_1 = lang.newText(new Coordinates(20, 300), "",
                "intro_1", null, introProps);
        
        intro_2 = lang.newText(new Coordinates(20, 80), "In der numerischen Mathematik verringert der Kahan Summation Algorithm die numerischen Fehler",
                "intro_2", null, introProps);
        intro_3 = lang.newText(new Coordinates(20, 100), "bei der Addition von endlichen Gleitkommazahlen.",
                "intro_3", null, introProps);
        intro_4 = lang.newText(new Coordinates(20, 120), "Ohne diese Technik wird die Präzision einer Addition von Gleitkommazahlen eingeschränkt, da aufgrund von",
                "intro_4", null, introProps);
        intro_5 = lang.newText(new Coordinates(20, 140), "falschen Rundungen der Endwert auf falsche Nachkommastellen gerundet wird.",
                "intro_5", null, introProps);
        
        intro_example = lang.newText(new Coordinates(20, 180), "Kurze 'wichtige' Information zur Animation: ",
                "intro_example", null, introProps);
        
        intro_6 = lang.newText(new Coordinates(20, 200), "Wir werden den Algorithmus mit einem Beispiel durchgehen, wo wir davon ausgehen, dass wir mit 6 Stellen ",
                "intro_6", null, introProps);
        intro_7 = lang.newText(new Coordinates(20, 220), "in Gleitkommazahldarstellung rechnen. Im kommenden Beispiel nehmen wir die Werte 10000.0, 3.15159 und 2.71828.",
                "intro_7", null, introProps);
        intro_8 = lang.newText(new Coordinates(20, 240), "Das exakte Resultat ist 10005.85987, gerundet 10005.9. Bei einer normalen Addition",
                "intro_8", null, introProps);
        intro_9 = lang.newText(new Coordinates(20, 260), "wuerden viele Nachkommastellen verschwinden. Das erste Ergebnis nach dem Runden wäre 10003,1. ",
                "intro_9", null, introProps);
        intro_10 = lang.newText(new Coordinates(20, 280), "Das zweite Resultat waere 10005.81828, und 10005.8 nach Runden. Dies ist nicht korrekt.",
                "intro_10", null, introProps);
        intro_11 = lang.newText(new Coordinates(20, 300), "Im Laufe der Animation wird uns dies nocheinmal verdeutlicht.",
                "intro_11", null, introProps);
        lang.nextStep("Einleitung");
    	
    	
        intro_1.hide();
    	intro_2.hide();
    	intro_3.hide();
    	intro_4.hide();
    	intro_5.hide();
    	intro_6.hide();
    	intro_7.hide();
    	intro_8.hide();
    	intro_9.hide();
    	intro_10.hide();
    	intro_11.hide();
    	intro_example.hide();
    	
    	
    }
    
    
    private void showCode(){
    	
    	
    	SourceCodeProperties scProps = new SourceCodeProperties();
        scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 14));

        scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.decode("#00cd00"));
        scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        sc = lang.newSourceCode(new Coordinates(40, 190), "sourceCode",
            null, scProps);
        method = lang.newText(new Coordinates(40, 188), "public static double doKahan(double[] input)",
                "method", null, introProps);
        method.changeColor(null, Color.decode("#8b0a50"), null, null);
        
        sc.addCodeLine("{", null, 0, null);
        sc.addCodeLine("double sum = 0.0f;", null, 1, null);
        sc.addCodeLine("double c = 0.0f;", null, 1, null); // 3
        sc.addCodeLine("", null, 1, null); // 4
        sc.addCodeLine("for (int i = 0; i < input.length; i++){", null, 1, null); // 5
        sc.addCodeLine("double y = input[i] - c;", null, 3, null); // 6
        sc.addCodeLine("double t = sum + y;", null, 3, null); // 7
        sc.addCodeLine("c = (t - sum) - y;", null, 3, null); // 8
        sc.addCodeLine("sum = t;", null, 3, null); // 9
        sc.addCodeLine("}", null, 1, null); // 10
        sc.addCodeLine("", null, 1, null); // 11
        sc.addCodeLine("return sum;", null, 2, null); // 12
        sc.addCodeLine("}", null, 0, null); // 13
    	
    	
    }
    
    
    private void showArray(){
    	
    	ArrayProperties arrayProps = new ArrayProperties();
        arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);
        da = lang.newDoubleArray(new Coordinates(30, 150), array, "doubleArray",
            null, arrayProps);

        ArrayMarkerProperties arrayIMProps = new ArrayMarkerProperties();
        arrayIMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "input[i]");
        arrayIMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        iMarker = lang.newArrayMarker(da, 0, "input[i]",
            null, arrayIMProps);
        
        
        lang.nextStep();
    }
    
    private void showSideText(){
    	
        
        infoProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.PLAIN, 15));
        infoProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
        
        
         info_sum = lang.newText(new Coordinates(450, 80), "sum holds the sum",
                "info", null, infoProps);
         info_c = lang.newText(new Coordinates(450, 110), "c accumulates the parts not assimilated into sum",
                "info_2", null, infoProps);
        
         
         info_rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
         info_rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.decode("#b0e2ff"));
         info_rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
         info_rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.decode("#b0e2ff"));
         
         
         iRect = lang.newRect(new Offset(-5, -5, "info",
             AnimalScript.DIRECTION_NW), new Offset(10, 10, "info_2", "SE"), "iRect",
             null, info_rectProps);

         
         TextProperties variableProps = new TextProperties();
         variableProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
             Font.SANS_SERIF, Font.PLAIN, 15));
         variableProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
         
         
         var_y1 = lang.newText(new Coordinates(450, 150), "y: N/A ",
                 "var_y1", null, variableProps);
         
         var_y2 = lang.newText(new Coordinates(450, 170), "",
                 "var_y2", null, variableProps);
         
         
         
         var_t1 = lang.newText(new Coordinates(450, 200), "t: N/A ",
                 "var_t1", null, variableProps);
         var_t2 = lang.newText(new Coordinates(450, 220), "",
                 "var_t2", null, variableProps);
         var_t3 = lang.newText(new Coordinates(450, 240), "",
                 "var_t3", null, variableProps);
         
         var_c1 = lang.newText(new Coordinates(450, 270), "c: N/A ",
                 "var_c1", null, variableProps);
         
         var_c2 = lang.newText(new Coordinates(450, 290), "",
                 "var_c2", null, variableProps);
         
         var_c3 = lang.newText(new Coordinates(450, 310), "",
                 "var_c3", null, variableProps);
         
         info_t = lang.newText(new Coordinates(630, 240), "Lost Digits!: "  ,
                 "info_t", null, infoProps);
         
         var_sum = lang.newText(new Coordinates(450, 340), "sum: N/A ",
                 "var", null, variableProps);
         
         
         
         
         
        
    	
    }
    
    public void showOutro(){
    	
    	
    	Text info_outro = lang.newText(new Coordinates(20, 80),  "Outro",
                "info_outro", null, infoProps);
        
    	Rect out = lang.newRect(new Offset(-5, -5, "info_outro",
                AnimalScript.DIRECTION_NW), new Offset(720, 10, "info_outro", "SE"), "out",
                null, info_rectProps);
    	
    	Text end = lang.newText(new Coordinates(20, 120), "Der Kahan Summation Algorithmus ist hilfreich, wenn Rechnungen durchgeführt werden, die auf endliche ",
                 "end", null, infoProps);
    	Text end1 = lang.newText(new Coordinates(20, 140), "Gleitkommazahlen angewendet werden, um eine präzise und 'richtig gerundete' Lösung zu erhalten. ",
                "end1", null, infoProps);
    	Text end2 = lang.newText(new Coordinates(20, 170), "Ich hoffe die Visualisierung hat ihnen bei der Verständnis des Algorithmus geholfen.",
                "end2", null, infoProps);
    	
    	lang.nextStep("Outro");
    	
    }
    
    public void doKahan(double[] input){
    	
    	showIntro();
    	showCode();
    	showArray();
    	showSideText();
        var = lang.newVariables();
       
        var.declare("double", T);
    	var.declare("double", SUM);
    	var.declare("double", Y);
    	var.declare("double", C_KEY);
    	
    	
    	
    	double sum = 0.0;
		double c = 0.0;
    	
		
		
		for (int i = 0; i < input.length; i++){	
			iMarker.move(i, null, defaultDuration);
			
			
			 
			lang.nextStep(i+1 + ". Addition"); 
			
			double y = input[i] - c;
			sc.highlight(5);
			var_y1.setText("y = " + String.valueOf(input[i]) + " - " + getSixDigit(c), null, null);
			var_y2.setText("   = " + getSixDigit(input[i] - c), null, null);
			var.set(Y, String.valueOf(getSixDigit(y)));
			
			
			var_y1.changeColor(null, Color.GREEN, null, null);
			var_y2.changeColor(null, Color.GREEN, null, null);
			
			lang.nextStep();
			
			sc.unhighlight(5);
			var_y1.changeColor(null, Color.BLACK, null, null);
			var_y2.changeColor(null, Color.BLACK, null, null);
			sc.highlight(6);
			
			
			double t = sum + y;
			var_t1.setText("t = " + sum + " + " + getSixDigit(y), null, null);
			var_t2.setText("  = " + (sum + y), null, null);
			
			
			
			String rest = getRest(t);
			DecimalFormat f = new DecimalFormat("#####.#");
			f.setRoundingMode(RoundingMode.HALF_UP);
			
			String t_new = f.format(t);
			t_new = t_new.replace(',', '.');
			t = Double.valueOf(t_new);
			var.set(T, getSixDigit(t));
			
			var_t3.setText("  = " + t, null, null);
			
			info_t.setText("Lost Digits!: " + rest, null, null);
			
			
			var_t1.changeColor(null, Color.GREEN, null, null);
			var_t2.changeColor(null, Color.GREEN, null, null);
			var_t3.changeColor(null, Color.GREEN, null, null);
			
			lang.nextStep();
			
			sc.unhighlight(6);
			sc.highlight(7);
			
			var_t1.changeColor(null, Color.BLACK, null, null);
			var_t2.changeColor(null, Color.BLACK, null, null);
			var_t3.changeColor(null, Color.BLACK, null, null);
			
			
			c = (t - sum) - y;
			double zwischen = t-sum;
			var_c1.setText("c = (" + t + " - " + sum + " )" + " - " + getSixDigit(y) , null, null);
			var_c2.setText("   = " + getSixDigit(zwischen) + " - " + getSixDigit(y) , null, null);
			var_c3.setText("   = " + getSixDigit(c), null, null);
			var.set(C_KEY, getSixDigit(c));
			
			
			var_c1.changeColor(null, Color.GREEN, null, null);
			var_c2.changeColor(null, Color.GREEN, null, null);
			var_c3.changeColor(null, Color.GREEN, null, null);
			
			
			lang.nextStep();
			
			sc.unhighlight(7);
			sc.highlight(8);
			var_c1.changeColor(null, Color.BLACK, null, null);
			var_c2.changeColor(null, Color.BLACK, null, null);
			var_c3.changeColor(null, Color.BLACK, null, null);
			
			
			sum = t;
			var_sum.setText("sum = " + t, null, null);
			var_sum.changeColor(null, Color.GREEN, null, null);
			
			var.set(SUM, getSixDigit(sum));
			
			if (i == input.length-1){
				end = lang.newText(new Coordinates(450, 360), "Korrekte Addition!",
		                 "var", null, infoProps);
				end.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15), null, null);
				lang.nextStep();
				break;
			}
			
			lang.nextStep();
			
			sc.unhighlight(8);
			var_sum.changeColor(null, Color.BLACK, null, null);
		}
		lang.nextStep("Ergebnis");
		
		var_y1.hide();
		var_y2.hide();
		var_t1.hide();
		var_t2.hide();
		var_t3.hide();
		var_c1.hide();
		var_c2.hide();
		var_c3.hide();
		var_sum.hide();
		end.hide();
		info_sum.hide();
		info_c.hide();
		info_t.hide();
		method.hide();
		sc.hide();
		da.hide();
		iRect.hide();
		
		
		showOutro();
		
		
    }
    
    public String getSixDigit(double wert){
    	String res = "";
    	String s_wert;
    	
    	
    	DecimalFormat f = new DecimalFormat("#.#####");
		f.setRoundingMode(RoundingMode.HALF_UP);
		
		String wert_new = f.format(wert);
		wert_new = wert_new.replace(',', '.');
		wert = Double.valueOf(wert_new);
    	
		
		s_wert = String.valueOf(wert);	
    	
  
    	for (int x = 0; x < 7; x++){
    		if (x < s_wert.length())
    		res = res + s_wert.charAt(x);
    		else return res;
    	}
    	
    	return res;
    	
    
    }
    
    public String getRest(double wert){
    	String res = "";
    	String s_wert = String.valueOf(wert);
    	
    	for (int x = 7; x < s_wert.length(); x++){
    		
    		res = res + s_wert.charAt(x);
    	}
    	res = "0.0"+res;
    	return res;
    	
    }
    
    
    public  void init(){
        lang = new AnimalScript("Kahan Summation Algorithm", "Oemer M. Ayar", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        toadd_3 = (double)primitives.get("toadd_3");
        toadd_1 = (double)primitives.get("toadd_1");
        arrayProperties = (ArrayProperties)props.getPropertiesByName("arrayProperties");
        toadd_2 = (double)primitives.get("toadd_2");
        
        array = new double[]{toadd_1,toadd_2,toadd_3};
        
        init();
        s = new KahanSummationAlgorithm(lang); 
        s.doKahan(array);
        
        
        return lang.toString();
    }

    public String getName() {
        return "Kahan Summation Algorithm";
    }

    public String getAlgorithmName() {
        return "Kahan Summation";
    }

    public String getAnimationAuthor() {
        return "Oemer M. Ayar";
    }

    public String getDescription(){
        return "In numerical analysis,"
 +"\n"
 +"the Kahan summation algorithm (also known as compensated summation) significantly reduces "
 +"\n"
 +"the numerical error in the total obtained by adding a sequence of finite precision floating point numbers,"
 +"\n"
 +"compared to the obvious approach. This is done by keeping a separate running compensation (a variable to accumulate small errors). "
 +"\n"
 +"In particular, simply summing n numbers in sequence has a worst-case error that grows proportional to n, "
 +"\n"
 +"and a root mean square error that grows as sqrt N  for random inputs (the roundoff errors form a random walk)."
 +"\n"
 +"With compensated summation, the worst-case error bound is independent of n, so a large number of values can be summed with an error that only depends on the floating-point precision. (Wikipedia)"
 +"\n"
 +""
 +"\n"
 +"";
    }

    public String getCodeExample(){
        return "\n"
 +"public static double doKahan(double[] input){"
 +"\n"
 +"	double sum = 0.0f;"
 +"\n"
 +"	double c = 0.0f;"
 +"\n"
 +"			"
 +"\n"
 +"	for (int i = 0; i < input.length; i++){"
 +"\n"
 +"				"
 +"\n"
 +"		double y = input[i] - c;"
 +"\n"
 +"		double t = sum + y;"
 +"\n"
 +"		c = (t - sum) - y;"
 +"\n"
 +"				 "
 +"\n"
 +"		sum = t;"
 +"\n"
 +"	}"
 +"\n"
 +"		"
 +"\n"
 +"	return sum;"
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

}