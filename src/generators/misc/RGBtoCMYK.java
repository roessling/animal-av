package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import java.awt.Color;
import java.awt.Font;

import algoanim.primitives.Primitive;
import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.Point;
import algoanim.primitives.Polygon;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PointProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import algoanim.variables.*;

import interactionsupport.models.*;

public class RGBtoCMYK implements Generator {
    private Language lang;
    private int red;
    private int green;
    private int blue;
	
	private static final String HEADER1 = "RGB";
	private static final String HEADER2 = "to";
	private static final String HEADER3 = "CMYK";
	private static final String INPUT = "INPUT";
	private static final String RED = "RED";
	private static final String GREEN = "GREEN";
	private static final String BLUE = "BLUE";
	private static final String OUTPUT = "OUTPUT";
	private static final String CYAN = "CYAN";
	private static final String MAGENTA = "MAGENTA";
	private static final String YELLOW = "YELLOW";
	private static final String KEY = "KEY";
	private static final String algorithm = "algorithm";	
	
	private Text headerTxt1, headerTxt2, headerTxt3;
	private Rect headerRect;
	private final RectProperties HeaderRectPr = new RectProperties();
	private final TextProperties HeaderTxtPr = new TextProperties();
	private Text inputTxt, outputTxt;
	private Rect inputRect, outputRect;
	private final RectProperties UpperRectPr = new RectProperties();
	private final TextProperties UpperTxtPr = new TextProperties();
	private final TextProperties OutputTxtPr = new TextProperties();
	private Text redTxt, redValueTxt;
	private Rect redRect, redValueRect;	
	private final RectProperties RedRectPr = new RectProperties();
	private final TextProperties RedTxtPr = new TextProperties();
	private Text greenTxt, greenValueTxt;
	private Rect greenRect, greenValueRect;
	private final RectProperties GreenRectPr = new RectProperties();
	private final TextProperties GreenTxtPr = new TextProperties();
	private Text blueTxt, blueValueTxt;
	private Rect blueRect, blueValueRect;
	private final RectProperties BlueRectPr = new RectProperties();
	private final TextProperties BlueTxtPr = new TextProperties();
	private Rect rgbRect, cmykRect;
	private final RectProperties LowerRectPr = new RectProperties();
	private Text cyanTxt, cyanValueTxt;
	private Rect cyanRect, cyanValueRect;
	private final RectProperties CyanRectPr = new RectProperties();
	private final TextProperties CyanTxtPr = new TextProperties();
	private Text magentaTxt, magentaValueTxt;
	private Rect magentaRect, magentaValueRect;
	private final RectProperties MagentaRectPr = new RectProperties();
	private final TextProperties MagentaTxtPr = new TextProperties();
	private Text yellowTxt, yellowValueTxt;
	private Rect yellowRect, yellowValueRect;
	private final RectProperties YellowRectPr = new RectProperties();
	private final TextProperties YellowTxtPr = new TextProperties();
	private Text keyTxt, keyValueTxt;
	private Rect keyRect, keyValueRect;
	private final RectProperties KeyRectPr = new RectProperties();
	private final TextProperties KeyTxtPr = new TextProperties();
	private final TextProperties ColorTxtPr = new TextProperties();
	private Timing defaultTiming = new TicksTiming(0);

	private TextProperties tempVariables;
	private final TextProperties ValueTxtPr = new TextProperties();
	private final RectProperties ValueRectPr = new RectProperties();
	private double questProbability;
	
	private Primitive[] hideFehlerFensterArray = null; 

	
	
	private RectProperties AlgoRectPr = new RectProperties();
	private SourceCodeProperties sourceCode;
	private SourceCode sc;	
	
	
	private Variables vars;

    public void init(){
        lang = new AnimalScript("RGB-to-CMYK", "Samil Kurt", 800, 600);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		lang.setStepMode(true);
    }
	
		private double maximum(double a, double b, double c) {
		double max = Math.max(a, b);
		return Math.max(max, c);
	}
	
	
	
	private double round(double a) {
		return (double) (Math.round(a*1000)/1000.0);
	}
	
	
	private void introduction(){
		HeaderRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);			
		HeaderRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(223,223,223));
		headerRect = lang.newRect(new Coordinates(15,15), new Coordinates(625,55), "headerRect", null, HeaderRectPr);
		HeaderTxtPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 27));
		String header = "Umwandlung von RGB- zu CMYK-Farbraum";
		Text intrTxt = lang.newText(new Offset(20,13, headerRect, "NW"), header, "header1", null, HeaderTxtPr);
		
		TextProperties zeilPr = new TextProperties();
		zeilPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 17));
		String zeile1 = "Ein Farbraum besteht immer aus den sogenannten Grundfarben. Diese Grund-";
		String zeile2 = "farben ermöglichen es durch Mischen jede beliebige andere Farbe im ";
		String zeile3 = "Farbraum darzustellen.";
		String zeile33 = "Der RGB-Farbraum besteht aus den Grundfarben Rot, Grün & Blau und der ";
		String zeile4 = "CMYK-Farbraum aus Cyan, Magenta, Yellow & Key, wobei Key für die ";
		String zeile5 = "Abtönung der Farbe zustöndig ist."; 
		String zeile6 = "Da z.B. die Monitore den RGB-Raum und Drucker den CMYK-Raum nutzen ";
		String zeile7 = "ist die Umwandlung von Farbräumen unabdingbar.";
		
		Text z1 = lang.newText(new Offset(12, 25, headerRect, "SW"), zeile1, "z1", null, zeilPr);
		Text z2 = lang.newText(new Offset(0, 10, z1, "SW"), zeile2, "z2", null, zeilPr);
		Text z3 = lang.newText(new Offset(0, 10, z2, "SW"), zeile3, "z3", null, zeilPr);	
		Text z33 = lang.newText(new Offset(0, 10, z3, "SW"), zeile33, "z3", null, zeilPr);
		Text z4 = lang.newText(new Offset(0, 10, z33, "SW"), zeile4, "z4", null, zeilPr);
		Text z5 = lang.newText(new Offset(0, 10, z4, "SW"), zeile5, "z5", null, zeilPr);
		Text z6 = lang.newText(new Offset(0, 10, z5, "SW"), zeile6, "z6", null, zeilPr);
		Text z7 = lang.newText(new Offset(0, 10, z6, "SW"), zeile7, "z7", null, zeilPr);		
		
		lang.nextStep("0. Einführung");
		z1.hide(); 
		z2.hide(); 
		z3.hide(); 
		z33.hide();
		z4.hide();
		z5.hide();
		z6.hide();
		z7.hide();
		intrTxt.hide();		
	}
	
	
	private void fazit(int[] rgb, double[] cmyk){
		headerRect.show();
		HeaderTxtPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		String header = "Fazit";
		Text headerTxt = lang.newText(new Offset(30, 12, headerRect, "NW"), header, "headerTxt", null, HeaderTxtPr);
		
		TextProperties zeilPr = new TextProperties();
		zeilPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 17));
		String zeile1 = "Die errechneten Werte für den CMYK-Raum bilden genau dieselbe Farbe wie";
		String zeile2 = "die Werte vom RGB-Raum ab. D.h. die Umwandlung von Farbräumen";
		String zeile3 = "ändert nicht die Farbe, sondern nur das zugrunde liegende System, dass ";
		String zeile4 = "die Farbe bestimmt.";
		String zeile5 = "In diesem Durchlauf des Algorithmus ist die Farbe, die durch die RGB-Werte";
		String zeile6 = "gebildet wird ";
		Text z1 = lang.newText(new Offset(12, 25, headerRect, "SW"), zeile1, "z1", null, zeilPr);
		Text z2 = lang.newText(new Offset(0, 10, z1, "SW"), zeile2, "z2", null, zeilPr);
		Text z3 = lang.newText(new Offset(0, 10, z2, "SW"), zeile3, "z3", null, zeilPr);	
		Text z4 = lang.newText(new Offset(0, 10, z3, "SW"), zeile4, "z4", null, zeilPr);
		Text z5 = lang.newText(new Offset(0, 10, z4, "SW"), zeile5, "z5", null, zeilPr);
		Text z6 = lang.newText(new Offset(0, 10, z5, "SW"), zeile6, "z6", null, zeilPr);
		
		RectProperties rgbRectPr = new RectProperties();
		rgbRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rgbRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(rgb[0], rgb[1], rgb[2]));
		Rect rgbRect = lang.newRect(new Offset(10, 2, z6, "NE"), new Offset(90, -2, z6, "SE"), "rgbRect", null, rgbRectPr); 
		
		String zeile66 = "& die CMYK-Werte bilden die Farbe";
		Text z66 = lang.newText(new Offset(12, -3, rgbRect, "NE"), zeile66, "z6", null, zeilPr);
		Rect cmykRect = lang.newRect(new Offset(10, 2, z66, "NE"), new Offset(95, -2, z66, "SE"), "cmykRect", null, rgbRectPr); 
		
		String zeile7 = "Hieran kann man erkennen, dass die Umwandlung die Farbe nicht verändert";
		String zeile8 = "& da die Farbfelder die gleiche Farbe anzeigen wurde korrekt umgewandelt.";
		Text z7 = lang.newText(new Offset(0, 10, z6, "SW"), zeile7, "z7", null, zeilPr);
		Text z8 = lang.newText(new Offset(0, 10, z7, "SW"), zeile8, "z8", null, zeilPr);

		lang.nextStep("5. Fazit");	
		
	}
	
	
	private void showSourceCode(){	
		sc = lang.newSourceCode(new Coordinates(30,315), "sourceCode", null, sourceCode);
		sc.addCodeLine("public double[] algorithm(int red, int green, int blue){", null, 0, null);
		sc.addCodeLine("double r,g,b, max, cyan, magenta, yellow, key;", null, 2, null);
		sc.addCodeLine("r = red/255.0;"  , null, 2, null);
		sc.addCodeLine("g = green/255.0;", null, 2, null);
		sc.addCodeLine("b = blue/255.0;", null, 2, null);
		sc.addCodeLine("max = maximum(r, g, b);", null, 2, null);
		sc.addCodeLine("key = 1 - max;", null, 2, null);
		sc.addCodeLine("yellow = (1 - b - key) / (1 - key);", null, 2, null);
		sc.addCodeLine("magenta = (1 - g - key) / (1 - key);", null, 2, null);
		sc.addCodeLine("cyan = (1 - r - key) / (1 - key);", null, 2, null);
		sc.addCodeLine("return new double[] {cyan, magenta, yellow, key};", null, 2, null);
		sc.addCodeLine("}", null, 0, null);
				
		Rect scRect = lang.newRect(new Offset(-15,-5, sc, "NW"), new Offset(15,5, sc, "SE"), "scRect", null, AlgoRectPr);
	}
	
	private void hideFehlerFenster() {
		if (hideFehlerFensterArray!=null)
			for(int i=0; i<hideFehlerFensterArray.length; i++)
				hideFehlerFensterArray[i].hide();
		hideFehlerFensterArray = null;
	}
	
	private int[] fehlerFenster(int red, int green, int blue) {
		int[] neueWerte = new int[]{red, green, blue};
		if(red < 0 || red > 255 || blue < 0 || blue > 255 || green < 0 || green > 255) {
			int fehlerArray = 0;
			Boolean redFalse = false;
			Boolean greenFalse = false;
			Boolean blueFalse = false;
			int falseNumber = 0;
			if (red < 0 || red > 255) {
				if(red>255) neueWerte[0] = 255;
				else if(red<0) neueWerte[0] = 0;
				redFalse = true;
				falseNumber++;
			}
			if (green < 0 || green > 255) {
				if(green>255) neueWerte[1] = 255;
				else if(green<0) neueWerte[1] = 0;
				greenFalse = true;
				falseNumber++;
			}
			if (blue < 0 || blue > 255) {
				if(blue>255) neueWerte[2] = 255;
				else if(blue<0) neueWerte[2] = 0;
				blueFalse = true;
				falseNumber++;
			}
			
			
			TextProperties tPr = new TextProperties();
			tPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));
			tPr.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
			
			String stT0 = "ACHTUNG";
			String stT00 = "FARBWERT";
			String stT000 = "KORREKTUR:";
			String stT1 = "Wenn Sie ein fleißiger Student";
			String stT2 = "wären, hätten Sie in der";
			String stT3 = "Beschreibung gelesen, dass die";
			String stT4 = "Werte immer von 0-255 gehen!";
			String stT5 = "Ihr RED-Wert = " + String.valueOf(red);
			String stT6 = "Ihr GREEN-Wert = " + String.valueOf(green);
			String stT7 = "Ihr BLUE-Wert = " + String.valueOf(blue);
			String stT8 = "Daher werden Defaultwerte";
			String stT88 = "Daher wird der Defaultwert";
			String stT9 = "RED = " + String.valueOf(neueWerte[0]);
			String stT10 = "GREEN = " + String.valueOf(neueWerte[1]);
			String stT11 = "BLUE = " + String.valueOf(neueWerte[2]);
			String stT12 = "gesetzt & der Algorithmus";
			String stT13 = "mit diesen fortgesetzt.";
			String stT133 = "mit diesem forgesetzt.";

			RectProperties fehlerRectPr = new RectProperties();
			RectProperties fehlerHeaderPr =new RectProperties();
			fehlerRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			fehlerRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.white);
			fehlerHeaderPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			fehlerHeaderPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
			Rect fehlerHeaderRect = lang.newRect(new Offset(-440, 50, headerRect, "SE"), new Offset(-180, 75, headerRect, "SE"), "fehlerRect", null, fehlerHeaderPr);
			fehlerArray++;
			Rect fehlerRect;
			if(falseNumber == 3) 
			fehlerRect = lang.newRect(new Offset(0,0, fehlerHeaderRect, "SW"), new Offset(0,325, fehlerHeaderRect, "SE"), "fehlerRect", null, fehlerRectPr);	
			else if(falseNumber == 2) 
			fehlerRect = lang.newRect(new Offset(0,0, fehlerHeaderRect, "SW"), new Offset(0,280, fehlerHeaderRect, "SE"), "fehlerRect", null, fehlerRectPr);
			else fehlerRect = lang.newRect(new Offset(0,0, fehlerHeaderRect, "SW"), new Offset(0,235, fehlerHeaderRect, "SE"), "fehlerRect", null, fehlerRectPr);
			fehlerArray++;

			
			
			Text t0 = lang.newText(new Offset(5,2, fehlerHeaderRect, "NW"), stT0, "t0", null, tPr);
			t0.changeColor(null, Color.RED, defaultTiming, defaultTiming);
			Text t00 = lang.newText(new Offset(5,0, t0, "NE"), stT00, "t00", null, tPr);
			t00.changeColor(null, new Color(34,177,76), defaultTiming, defaultTiming);
			Text t000 = lang.newText(new Offset(0,0, t00, "NE"), stT000, "t000", null, tPr);
			t000.changeColor(null, new Color(98,98,255), defaultTiming, defaultTiming);
			t0.setFont(new Font("SansSerif", Font.BOLD, 14), defaultTiming, defaultTiming);
			fehlerArray++;
			fehlerArray++;
			fehlerArray++;
			Text t1 = lang.newText(new Offset(10, 10, t0, "SW"), stT1, "t1", null, tPr);
			Text t2 = lang.newText(new Offset(0, 5, t1, "SW"), stT2, "t2", null, tPr);
			Text t3 = lang.newText(new Offset(0, 5, t2, "SW"), stT3, "t3", null, tPr);
			Text t4 = lang.newText(new Offset(0, 5, t3, "SW"), stT4, "t4", null, tPr);
			fehlerArray += 4;
			Text t5 = null;
			
			if(redFalse) {
				t5 = lang.newText(new Offset(0, 5, t4, "SW"), stT5, "t5", null, tPr);
			}
			Rect redFRect = null;
			if(t5!=null){
				RectProperties redFRectPr = new RectProperties();
				redFRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				redFRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
				redFRect = lang.newRect(new Offset(-7, 1, t5, "NW"), new Offset(235, 19, t5, "NW"), "redFRect", null, redFRectPr);  
				t5.hide();
				t5 = lang.newText(new Offset(0, 5, t4, "SW"), stT5, "t5", null, tPr);
				fehlerArray += 2;
			}

			Text t6 = null;
			if(greenFalse) {
				if (redFalse) t6 = lang.newText(new Offset(0, 5, t5, "SW"), stT6, "t6", null, tPr);
				else t6 = lang.newText(new Offset(0, 5, t4, "SW"), stT6, "t6", null, tPr);
			}
			
			Rect greenFRect = null;
			if(t6!=null) {
				RectProperties greenFRectPr = new RectProperties();
				greenFRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				greenFRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(34,177,76));
				greenFRect = lang.newRect(new Offset(-7, 1, t6, "NW"), new Offset(235, 19, t6, "NW"), "greenFRect", null, greenFRectPr); 
				t6.hide();
				if (redFalse) t6 = lang.newText(new Offset(0, 5, t5, "SW"), stT6, "t6", null, tPr);
				else t6 = lang.newText(new Offset(0, 5, t4, "SW"), stT6, "t6", null, tPr);
				fehlerArray += 2;
			}
			Text t7=null;
			if(blueFalse) {
				if (redFalse && greenFalse) t7 = lang.newText(new Offset(0, 5, t6, "SW"), stT7, "t7", null, tPr);
				else if (redFalse || greenFalse) t7 = lang.newText(new Offset(0, 5+18+5, t4, "SW"), stT7, "t7", null, tPr);
				else t7 = lang.newText(new Offset(0, 5, t4, "SW"), stT7, "t7", null, tPr);
			}
			Rect blueFRect = null;
			if(t7!=null) {
				RectProperties blueFRectPr = new RectProperties();
				blueFRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				blueFRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(98,98,255));
				blueFRect = lang.newRect(new Offset(-7, 1, t7, "NW"), new Offset(235, 19, t7, "NW"), "blueFRect", null, blueFRectPr); 
				t7.hide();
				if (redFalse && greenFalse) t7 = lang.newText(new Offset(0, 5, t6, "SW"), stT7, "t7", null, tPr);
				else if (redFalse || greenFalse) t7 = lang.newText(new Offset(0, 5+18+5, t4, "SW"), stT7, "t7", null, tPr);
				else t7 = lang.newText(new Offset(0, 5, t4, "SW"), stT7, "t7", null, tPr);
				fehlerArray += 2;
			}
			
			Text t8;	
			if(falseNumber == 3)
				t8 = lang.newText(new Offset(0, (3+1)*5+3*18     + 5, t4, "SW"), stT8, "t8", null, tPr);
			else if(falseNumber > 1)
				t8 = lang.newText(new Offset(0, (2+1)*5+2*18     +5, t4, "SW"), stT8, "t8", null, tPr);
			else t8 = lang.newText(new Offset(0, (1+1)*5+18     +5, t4, "SW"), stT88, "t88", null, tPr);
			fehlerArray++;
			Text t9 = null;
			if(redFalse) t9 = lang.newText(new Offset(0, 5, t8, "SW"), stT9, "t9", null, tPr);
			Rect red2FRect = null;
			if(t9!=null){
				RectProperties red2FRectPr = new RectProperties();
				red2FRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				red2FRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
				red2FRect = lang.newRect(new Offset(-7, 1, t9, "NW"), new Offset(235, 19, t9, "NW"), "red2FRect", null, red2FRectPr); 
				t9.hide();
				if(redFalse) t9 = lang.newText(new Offset(0, 5, t8, "SW"), stT9, "t9", null, tPr);
				fehlerArray += 2;
			}
			Text t10 = null;
			if(greenFalse) {
				if (t9 == null) t10 = lang.newText(new Offset(0, 5, t8, "SW"), stT10, "t10", null, tPr);
				else t10 = lang.newText(new Offset(0, 5+5+18, t8, "SW"), stT10, "t10", null, tPr);
			}
			Rect green2FRect = null;
			if(t10!=null) {
				RectProperties green2FRectPr = new RectProperties();
				green2FRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				green2FRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(34,177,76));
				green2FRect = lang.newRect(new Offset(-7, 1, t10, "NW"), new Offset(235, 19, t10, "NW"), "green2FRect", null, green2FRectPr); 
				t10.hide();				
				if (t9 == null) t10 = lang.newText(new Offset(0, 5, t8, "SW"), stT10, "t10", null, tPr);
				else t10 = lang.newText(new Offset(0, 5+5+18, t8, "SW"), stT10, "t10", null, tPr);
				fehlerArray += 2;
			}
			Text t11 = null;
			if(blueFalse) {
				if (t9 == null && t10 == null) t11 = lang.newText(new Offset(0, 5, t8, "SW"), stT11, "t11", null, tPr);
				else if (t9 == null || t10 == null) t11 = lang.newText(new Offset(0, 5+5+18, t8, "SW"), stT11, "t11", null, tPr);
				else t11 = lang.newText(new Offset(0, 5+5+5+18+18, t8, "SW"), stT11, "t11", null, tPr);	
			}
			Rect blue2FRect = null;
			if(t11!=null) {
				RectProperties blue2FRectPr = new RectProperties();
				blue2FRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				blue2FRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(98,98,255));
				blue2FRect = lang.newRect(new Offset(-7, 1, t11, "NW"), new Offset(235, 19, t11, "NW"), "blue2FRect", null, blue2FRectPr);
				t11.hide();
				if (t9 == null && t10 == null) t11 = lang.newText(new Offset(0, 5, t8, "SW"), stT11, "t11", null, tPr);
				else if (t9 == null || t10 == null) t11 = lang.newText(new Offset(0, 5+5+18, t8, "SW"), stT11, "t11", null, tPr);
				else t11 = lang.newText(new Offset(0, 5+5+5+18+18, t8, "SW"), stT11, "t11", null, tPr);	
				fehlerArray += 2;
			}
			
			Text t12 = lang.newText(new Offset(0 , 2*falseNumber*(5+18)+5+5+18     +5, t4, "SW"), stT12, "t12", null, tPr);
			Text t13 = null;
			if(falseNumber != 1)
				t13 = lang.newText(new Offset(0, 5, t12, "SW"), stT13, "t13", null, tPr);
			else t13 = lang.newText(new Offset(0, 5, t12, "SW"), stT133, "t133", null, tPr);	
			fehlerArray += 2;

			
			int i=0;
			hideFehlerFensterArray = new Primitive[fehlerArray];
			if(fehlerHeaderRect !=null) {
				hideFehlerFensterArray[i] = fehlerHeaderRect;
				i++;
			}
			if(fehlerRect !=null) {
				hideFehlerFensterArray[i] = fehlerRect;
				i++;
			}
			if(t0 !=null) {
				hideFehlerFensterArray[i] = t0;
				i++;
			}
			if(t1 !=null) {
				hideFehlerFensterArray[i] = t1;
				i++;
			}
			if(t2 !=null) {
				hideFehlerFensterArray[i] = t2;
				i++;
			}
			if(t3 !=null) {
				hideFehlerFensterArray[i] = t3;
				i++;
			}
			if(t4 !=null) {
				hideFehlerFensterArray[i] = t4;
				i++;
			}
			if(t5 !=null) {
				hideFehlerFensterArray[i] = t5;
				i++;
			}
			if(t6 !=null) {
				hideFehlerFensterArray[i] = t6;
				i++;
			}
			if(t7 !=null) {
				hideFehlerFensterArray[i] = t7;
				i++;
			}
			if(t8 !=null) {
				hideFehlerFensterArray[i] = t8;
				i++;
			}
			if(t9 !=null) {
				hideFehlerFensterArray[i] = t9;
				i++;
			}
			if(t10 !=null) {
				hideFehlerFensterArray[i] = t10;
				i++;
			}
			if(t11 !=null) {
				hideFehlerFensterArray[i] = t11;
				i++;
			}
			if(t12 !=null) {
				hideFehlerFensterArray[i] = t12;
				i++;
			}
			if(t13 !=null) {
				hideFehlerFensterArray[i] = t13;
				i++;
			}
			if(redFRect !=null) {
				hideFehlerFensterArray[i] = redFRect;
				i++;
			}
			if(red2FRect !=null) {
				hideFehlerFensterArray[i] = red2FRect;
				i++;
			}
			if(blueFRect !=null) {
				hideFehlerFensterArray[i] = blueFRect;
				i++;
			}
			if(blue2FRect !=null) {
				hideFehlerFensterArray[i] = blue2FRect;
				i++;
			}
			if(greenFRect !=null) {
				hideFehlerFensterArray[i] = greenFRect;
				i++;
			}
			if(green2FRect !=null) {
				hideFehlerFensterArray[i] = green2FRect;
				i++;
			}
			if(t00 !=null) {
				hideFehlerFensterArray[i] = t00;
				i++;
			}
			if(t000 !=null) {
				hideFehlerFensterArray[i] = t000;
				i++;
			}	
		}
		return neueWerte;
	}
	
	

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        red = (Integer)primitives.get("red");
        green = (Integer)primitives.get("green");
        blue = (Integer)primitives.get("blue");
		questProbability = (Double)primitives.get("questProbability");
		if(questProbability > 100) questProbability = 100;
		else if(questProbability < 0) questProbability = 0; 	
		tempVariables = (TextProperties)props.getPropertiesByName("tempVariables");
		sourceCode = (SourceCodeProperties)props.getPropertiesByName("sourceCode");		
	
	/*********************************** VARIABLEN DINGS BUMS *********************************************************************************************/
		vars = lang.newVariables();
		vars.declare("String", "red");
		vars.set("red", "xxxxx"); 
		vars.declare("String", "green");
		vars.set("green", "xxxxx");
		vars.declare("String", "blue");
		vars.set("blue", "xxxxx");
		vars.declare("String", "r");
		vars.set("r", "xxxxx");
		vars.declare("String", "g");
		vars.set("g", "xxxxx");
		vars.declare("String", "b");
		vars.set("b", "xxxxx");
		vars.declare("String", "max");
		vars.set("max", "xxxxx");
		vars.declare("String", "cyan");
		vars.set("cyan", "xxxxx");
		vars.declare("String", "magenta");
		vars.set("magenta", "xxxxx");
		vars.declare("String", "yellow");
		vars.set("yellow", "xxxxx");
		vars.declare("String", "key.");
		vars.set("key.", "xxxxx");
		
		
		
		/*********************************** Introduction **************************************************************************************************/
		introduction();
		
		
		
		
		/************************************HEADER STEP**************************************************************************************************/
		// Überschrift 
		HeaderTxtPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 30));
		headerTxt1 = lang.newText(new Offset(32,13, headerRect, "NW"), HEADER1, "header1", null, HeaderTxtPr);
		headerTxt2 = lang.newText(new Offset(175,0, headerTxt1, "NE"), HEADER2, "header2", null, HeaderTxtPr);
		headerTxt3 = lang.newText(new Offset(180,0, headerTxt2, "NE"), HEADER3, "header3", null, HeaderTxtPr);
		
		lang.nextStep();

		
		/***********************************INPUT STEP******************************************************************************************************/
		// INPUT
			// für alle
		ValueTxtPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 15));
		ValueRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		ValueRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		UpperRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		UpperRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(127,127,127));
		UpperTxtPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));
		LowerRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		LowerRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(195,195,195));
		ColorTxtPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
			// Obere Kasten
		inputRect = lang.newRect(new Offset(0, 22, headerRect, "SW"), new Offset(130, 62, headerRect,"SW"), "inputRect", null, UpperRectPr);
		inputTxt = lang.newText(new Offset(-30, -2, inputRect, "N"), INPUT, "input", null, UpperTxtPr);
			// untere kasten
		rgbRect = lang.newRect(new Offset(0, 0, inputRect, "SW"), new Offset(0, 155, inputRect, "SE"), "rgbRect", null, LowerRectPr);
				//RED
		RedRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		RedRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(237,28,36));
		redRect = lang.newRect(new Offset(10, 15, inputRect, "SW"), new Offset(-60, 43, inputRect, "SE"), "redRect", null, RedRectPr);
		redTxt = lang.newText(new Offset(5,-1, redRect, "NW"), RED, "redTxt", null, ColorTxtPr);
		redValueRect = lang.newRect(new Offset(0,0, redRect, "NE"), new Offset(45,0, redRect, "SE"), "redValueRect", null, ValueRectPr);
				//GREEN
		GreenRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		GreenRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(34,177,76));
		greenRect = lang.newRect(new Offset(0, 20, redRect, "SW"), new Offset(0, 48, redRect, "SE"), "greenRect", null, GreenRectPr);
		greenTxt = lang.newText(new Offset(5,5, greenRect, "NW"), GREEN, "greenTxt", null, ColorTxtPr);
		greenValueRect = lang.newRect(new Offset(0,0, greenRect, "NE"), new Offset(45,0, greenRect, "SE"), "greenValueRect", null, ValueRectPr);
				//BLUE
		BlueRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		BlueRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(98,98,255));
		blueRect = lang.newRect(new Offset(0, 20, greenRect, "SW"), new Offset(0, 48, greenRect, "SE"), "BlueRect", null, BlueRectPr);
		blueTxt = lang.newText(new Offset(5,5, blueRect, "NW"), BLUE, "blueTxt", null, ColorTxtPr);
		blueValueRect = lang.newRect(new Offset(0,0, blueRect, "NE"), new Offset(45,0, blueRect, "SE"), "blueValueRect", null, ValueRectPr);

		
		// AlgoRectProperty
		AlgoRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		AlgoRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255, 253, 202));
		AlgoRectPr.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
			
		
		lang.nextStep();
		
		
		// Allgemeine Variablen
			
		//	cookTxtPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
			PointProperties pPr = new PointProperties();
	
			
		
		/******************************************ALGORITHM STEP***********************************************************************************************/
		double r,g,b, max, cyan, magenta, yellow, key; 
		
		
		
		

		showSourceCode();
		
		// Kochtopf
		
			Node[] cookLftAr = new Node[2];
			cookLftAr[0] = new Coordinates(230, 150);
			cookLftAr[1] = new Coordinates(230, 250);
			Polyline cookLft = lang.newPolyline(cookLftAr, "cookLft", null);
			
			Node[] cookRghtAr = new Node[2];
			cookRghtAr[0] = new Offset(150, 0, cookLft, "N");
			cookRghtAr[1] = new Offset(150, 0, cookLft, "S");
			Polyline cookRght = lang.newPolyline(cookRghtAr, "cookRght", null);
			
			Node[] cookUpAr = new Node[2];
			cookUpAr[0] = new Offset(0,0, cookLft, "N");
			cookUpAr[1] = new Offset(0,0, cookRght, "N");
			Polyline cookUp = lang.newPolyline(cookUpAr, "cookUp", null);
			
			Node[] cookDwnAr = new Node[2];
			cookDwnAr[0] = new Offset(0,0, cookLft, "S");
			cookDwnAr[1] = new Offset(0,0, cookRght, "S");
			Polyline cookDwn = lang.newPolyline(cookDwnAr, "cookDwn", null);
					
			Node[] helpLnAr = new Node[2];
			helpLnAr[0] = new Offset(0,0, cookLft, "C");
			helpLnAr[1] = new Offset(0,0, cookRght, "C");
			Polyline helpLn = lang.newPolyline(helpLnAr, "helpLn", null);
			helpLn.hide();
			
			Rect algoRect = lang.newRect(new Offset(0,-30, cookLft, "N"), new Offset(0, 0, cookRght, "N"), "algoRect", null, AlgoRectPr);
			TextProperties algoTxtPr = new TextProperties();
			algoTxtPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
			Text algoTxt = lang.newText(new Offset(-48,8, algoRect, "N"), "ALGORITHM", "algoTxt", null, algoTxtPr);
		
		
			lang.nextStep();
		
			// Frage1
			double rand1 = Math.random()*100;
			if(rand1 <= questProbability) {
				MultipleChoiceQuestionModel quest1 = new MultipleChoiceQuestionModel("quest1");
				quest1.setPrompt("Welche Werte können die Farben im RGB-Farbraum annehmen?");
				quest1.addAnswer(new AnswerModel("an1", "0 - 100", 5, "FALSCH"));
				quest1.addAnswer(new AnswerModel("an2", "0 - 255", 5, "WAHUUUUUU DAS IST KORREEEKT ;)"));
				quest1.addAnswer(new AnswerModel("an3", "0 - 1", 5, "FALSCH"));
				quest1.addAnswer(new AnswerModel("an4", "0 - 125", 5, "FALSCH"));
				lang.addMCQuestion(quest1);
				lang.nextStep();
			}
		
		
		
		/****************************************RGB STEP**************************************************************************************************/
		// RGB-Werte in die Kästen schreiben
			sc.highlight(0);

			redValueTxt = lang.newText(new Offset(-10,4, redValueRect, "N"), String.valueOf((int)red), "redValueTxt", null, ValueTxtPr);
			greenValueTxt = lang.newText(new Offset(-10,4, greenValueRect, "N"), String.valueOf((int)green), "greenValueTxt", null, ValueTxtPr);
			blueValueTxt = lang.newText(new Offset(-10,4, blueValueRect, "N"), String.valueOf((int)blue), "blueValueTxt", null, ValueTxtPr);
					
			vars.set("red", Integer.toString((int) red));
			vars.set("green", Integer.toString((int) green));
			vars.set("blue", Integer.toString((int) blue));


			lang.nextStep("1. Eingabe der RGB-Werten");
		
			
			/**************************************** FEHLER FENSTER *****************************************************************************************/
			int[] newNumber = fehlerFenster(red, green, blue);	
			
			if(hideFehlerFensterArray != null) {
				red = newNumber[0];
				green = newNumber[1];
				blue = newNumber[2];
				
				lang.nextStep();
			
				redValueTxt.setText(Integer.toString(red), defaultTiming, defaultTiming);
				greenValueTxt.setText(Integer.toString(green), defaultTiming, defaultTiming);
				blueValueTxt.setText(Integer.toString(blue), defaultTiming, defaultTiming);

				vars.set("red", Integer.toString((int) red));
				vars.set("green", Integer.toString((int) green));
				vars.set("blue", Double.toString((int) blue));
			
				lang.nextStep();
			}
			
			r = red/255.0;
			g = green/255.0;
			b = blue/255.0;		
	
			max = maximum(r, g, b);
			
			
			
			hideFehlerFenster();

			
		/*******************************************ARROW STEP******************************************************************************************************/	
		// RGB-Pfeile
			// RED-Pfeil
			Node[] redAr = new Node[7];
			Point centerRed = lang.newPoint(new Offset(0,0, redRect, "C"), "centerRed", null, pPr);
			centerRed.hide();
			redAr[0] = new Offset(100, -5, centerRed, "C");
			redAr[1] = new Offset(120, -5, centerRed, "C");
			redAr[2] = new Offset(120, -10, centerRed, "C");
			redAr[3] = new Offset(140, 0, centerRed, "C");
			redAr[4] = new Offset(120, 10, centerRed, "C");
			redAr[5] = new Offset(120, 5, centerRed, "C");
			redAr[6] = new Offset(100, 5, centerRed, "C");
			PolygonProperties RedArrowPr = new PolygonProperties();
			RedArrowPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			RedArrowPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
			try {
				Polygon redArrow = lang.newPolygon(redAr, "redArrow", null, RedArrowPr);
			} catch (NotEnoughNodesException e) {
				e.printStackTrace();
			}		//	redArrow.hide();
			// GREEN-Pfeil
			Node[] greenAr = new Node[7];
			Point centerGreen = lang.newPoint(new Offset(0,0, greenRect, "C"), "centerGreen", null, pPr);
			centerGreen.hide();
			greenAr[0] = new Offset(100, -5, centerGreen, "C");
			greenAr[1] = new Offset(120, -5, centerGreen, "C");
			greenAr[2] = new Offset(120, -10, centerGreen, "C");
			greenAr[3] = new Offset(140, 0, centerGreen, "C");
			greenAr[4] = new Offset(120, 10, centerGreen, "C");
			greenAr[5] = new Offset(120, 5, centerGreen, "C");
			greenAr[6] = new Offset(100, 5, centerGreen, "C");
			PolygonProperties GreenArrowPr = new PolygonProperties();
			GreenArrowPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			GreenArrowPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(34,177,76));
			try {
				Polygon greenArrow = lang.newPolygon(greenAr, "greenArrow", null, GreenArrowPr);
			} catch (NotEnoughNodesException e) {
				e.printStackTrace();
			}
			// BLUE-Pfeil
			Node[] blueAr = new Node[7];
			Point centerBlue = lang.newPoint(new Offset(0,0, blueRect, "C"), "centerBlue", null, pPr);
			centerBlue.hide();
			blueAr[0] = new Offset(100, -5, centerBlue, "C");
			blueAr[1] = new Offset(120, -5, centerBlue, "C");
			blueAr[2] = new Offset(120, -10, centerBlue, "C");
			blueAr[3] = new Offset(140, 0, centerBlue, "C");
			blueAr[4] = new Offset(120, 10, centerBlue, "C");
			blueAr[5] = new Offset(120, 5, centerBlue, "C");
			blueAr[6] = new Offset(100, 5, centerBlue, "C");
			PolygonProperties BlueArrowPr = new PolygonProperties();
			BlueArrowPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			BlueArrowPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(98,98,255));
			try {
				Polygon blueArrow = lang.newPolygon(blueAr, "blueArrow", null, BlueArrowPr);
			} catch (NotEnoughNodesException e) {	
				e.printStackTrace();
			}		
			
			lang.nextStep();
	
			
			
		/**************************************LOCAL VARIABLE STEP****************************************************************************************************/
			// Text im Kochtopf
			tempVariables.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
			sc.toggleHighlight(0,2);
			Text rTxt = lang.newText(new Offset(10,10, cookLft, "N"), "r = " + String.valueOf(round(r)), "rTxt", null, tempVariables);
			rTxt.changeColor(null, (Color) sourceCode.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), defaultTiming, defaultTiming);
			vars.set("r", Double.toString(round(r)));
			lang.nextStep("2.1. Berechnung der r-Hilfsvariable");
			
			sc.toggleHighlight(2,3);
			rTxt.changeColor(null, (Color) tempVariables.get(AnimationPropertiesKeys.COLOR_PROPERTY), defaultTiming, defaultTiming);
			Text gTxt = lang.newText(new Offset(0, 20, rTxt, "NW"), "g = " + String.valueOf(round(g)), "gTxt", null, tempVariables); 
			gTxt.changeColor(null, (Color) sourceCode.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), defaultTiming, defaultTiming);
			vars.set("g", Double.toString(round(g)));
			lang.nextStep("2.2. Berechnung der g-Hilfsvariable");
			
			gTxt.changeColor(null, (Color) tempVariables.get(AnimationPropertiesKeys.COLOR_PROPERTY), defaultTiming, defaultTiming);
			sc.toggleHighlight(3,4);
			Text bTxt = lang.newText(new Offset(0, 20, gTxt, "NW"), "b = " + String.valueOf(round(b)), "bTxt", null, tempVariables); 
			bTxt.changeColor(null, (Color) sourceCode.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), defaultTiming, defaultTiming);
			vars.set("b", Double.toString(round(b)));
			lang.nextStep("2.3. Berechnung der b-Hilfsvariable");
			
			sc.toggleHighlight(4,5);
			bTxt.changeColor(null, (Color) tempVariables.get(AnimationPropertiesKeys.COLOR_PROPERTY), defaultTiming, defaultTiming);
			Text maxTxt = lang.newText(new Offset(0, 20, bTxt, "NW"), "max = " + String.valueOf(round(max)), "maxTxt", null, tempVariables); 
			maxTxt.changeColor(null, (Color) sourceCode.get(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY), defaultTiming, defaultTiming);	
			vars.set("max", Double.toString(round(max)));

			
		lang.nextStep("2.4. Berechnung der max-Hilfsvariable");
		maxTxt.changeColor(null, (Color) tempVariables.get(AnimationPropertiesKeys.COLOR_PROPERTY), defaultTiming, defaultTiming);

		
		/**********************************KEY-STEP*****************************************************************************************************************/
		key = 1 - max;		 
		cyan = (1 - r - key) / (1 - key);
		magenta = (1 - g - key) / (1 - key);
		yellow = (1 - b - key) / (1 - key);
		
		
		// Obere Kasten
				outputRect = lang.newRect(new Offset(-160,22, headerRect, "SE"), new Offset(0,62, headerRect, "SE"), "ouputRect", null, UpperRectPr);
				outputTxt = lang.newText(new Offset(-40, 15, outputRect, "N"), OUTPUT, "output", null, UpperTxtPr);
				outputRect.hide();
				outputTxt.hide();
				// Untere Kasten
				cmykRect = lang.newRect(new Offset(0,0, outputRect, "SW"), new Offset(0,205, outputRect, "SE"), "cmykRect", null, LowerRectPr);
				cmykRect.hide();
					// CYAN
				CyanRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				CyanRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(0,255,255));
				cyanRect = lang.newRect(new Offset(10, 15, outputRect, "SW"), new Offset(-70, 43, outputRect, "SE"), "cyanRect", null, CyanRectPr);
				cyanTxt = lang.newText(new Offset(5,-1, cyanRect, "NW"), CYAN, "cyanTxt", null, ColorTxtPr);
				cyanValueRect = lang.newRect(new Offset(0,0, cyanRect, "NE"), new Offset(55,0, cyanRect, "SE"), "cyanValueRect", null, ValueRectPr);
				cyanValueTxt = lang.newText(new Offset(-18,4, cyanValueRect, "N"), String.valueOf(round(cyan)), "cyanValueTxt", null, ValueTxtPr);
						//CYAN-Pfeil
				Node[] cyanAr = new Node[7];
				Point centerCyan = lang.newPoint(new Offset(0,0, cyanRect, "C"), "centerCyan", null, pPr);	
				centerCyan.hide();
				cyanAr[0] = new Offset(-105,-5, centerCyan, "C");
				cyanAr[1] = new Offset(-85,-5, centerCyan, "C");
				cyanAr[2] = new Offset(-85,-10, centerCyan, "C");
				cyanAr[3] = new Offset(-65, 0, centerCyan, "C");
				cyanAr[4] = new Offset(-85, 10, centerCyan, "C");
				cyanAr[5] = new Offset(-85, 5, centerCyan, "C");
				cyanAr[6] = new Offset(-105, 5, centerCyan, "C");
				PolygonProperties cyanArrowPr = new PolygonProperties();
				cyanArrowPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				cyanArrowPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.CYAN);
				Polygon cyanArrow = null;
				try {
					cyanArrow = lang.newPolygon(cyanAr, "pfeil4", null, cyanArrowPr);
					cyanArrow.hide();
				} catch (NotEnoughNodesException e) {
					e.printStackTrace();
				}				cyanRect.hide();
				cyanTxt.hide();
				cyanValueRect.hide();
				cyanValueTxt.hide();
					// MAGENTA
				MagentaRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				MagentaRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255,0,255));
				magentaRect = lang.newRect(new Offset(0, 20, cyanRect, "SW"), new Offset(0, 48, cyanRect, "SE"), "magentaRect", null, MagentaRectPr);
				magentaTxt = lang.newText(new Offset(5,5, magentaRect, "NW"), MAGENTA, "magentaTxt", null, ColorTxtPr);
				magentaValueRect = lang.newRect(new Offset(0,0, magentaRect, "NE"), new Offset(55,0, magentaRect, "SE"), "magentaValueRect", null, ValueRectPr);
				magentaValueTxt = lang.newText(new Offset(-18,4, magentaValueRect, "N"), String.valueOf(round(magenta)), "magentaValueTxt", null, ValueTxtPr);
						//MAGENTA-Pfeil
				Node[] magentaAr = new Node[7];
				Point centerMagenta = lang.newPoint(new Offset(0,0, magentaRect, "C"), "centerMagenta", null, pPr);		
				centerMagenta.hide();
				magentaAr[0] = new Offset(-105,-5, centerMagenta, "C");
				magentaAr[1] = new Offset(-85,-5, centerMagenta, "C");
				magentaAr[2] = new Offset(-85,-10, centerMagenta, "C");
				magentaAr[3] = new Offset(-65, 0, centerMagenta, "C");
				magentaAr[4] = new Offset(-85, 10, centerMagenta, "C");
				magentaAr[5] = new Offset(-85, 5, centerMagenta, "C");
				magentaAr[6] = new Offset(-105, 5, centerMagenta, "C");
				PolygonProperties magentaArrowPr = new PolygonProperties();
				magentaArrowPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				magentaArrowPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.MAGENTA);
				Polygon magentaArrow = null;
				try {
					magentaArrow = lang.newPolygon(magentaAr, "magentaArrow", null, magentaArrowPr);
					magentaArrow.hide();
				} catch (NotEnoughNodesException e) {
					e.printStackTrace();
				}				magentaRect.hide();
				magentaTxt.hide();
				magentaValueRect.hide();
				magentaValueTxt.hide();
					// YELLOW
				YellowRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				YellowRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255,255,0));
				yellowRect = lang.newRect(new Offset(0, 20, magentaRect, "SW"), new Offset(0, 48, magentaRect, "SE"), "yellowRect", null, YellowRectPr);
				yellowTxt = lang.newText(new Offset(5,5, yellowRect, "NW"), YELLOW, "yellowTxt", null, ColorTxtPr);
				yellowValueRect = lang.newRect(new Offset(0,0, yellowRect, "NE"), new Offset(55,0, yellowRect, "SE"), "yellowValueRect", null, ValueRectPr);
				yellowValueTxt = lang.newText(new Offset(-18,4, yellowValueRect, "N"), String.valueOf(round(yellow)), "yellowValueTxt", null, ValueTxtPr);
						//YELLOW-Pfeil
				Node[] yellowAr = new Node[7];
				Point centerYellow = lang.newPoint(new Offset(0,0, yellowRect, "C"), "centerYellow", null, pPr);
				centerYellow.hide();
				yellowAr[0] = new Offset(-105,-5, centerYellow, "C");
				yellowAr[1] = new Offset(-85,-5, centerYellow, "C");
				yellowAr[2] = new Offset(-85,-10, centerYellow, "C");
				yellowAr[3] = new Offset(-65, 0, centerYellow, "C");
				yellowAr[4] = new Offset(-85, 10, centerYellow, "C");
				yellowAr[5] = new Offset(-85, 5, centerYellow, "C");
				yellowAr[6] = new Offset(-105, 5, centerYellow, "C");
				PolygonProperties yellowArrowPr = new PolygonProperties();
				yellowArrowPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				yellowArrowPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
				Polygon yellowArrow = null;
				try {
					yellowArrow = lang.newPolygon(yellowAr, "yellowArrow", null, yellowArrowPr);
					yellowArrow.hide();
				} catch (NotEnoughNodesException e) {
					e.printStackTrace();
				}				yellowRect.hide();
				yellowTxt.hide();
				yellowValueRect.hide();
				yellowValueTxt.hide();		
					// KEY
				KeyRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				KeyRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(0,0,0));
				keyRect = lang.newRect(new Offset(0, 20, yellowRect, "SW"), new Offset(0, 48, yellowRect, "SE"), "keyRect", null, KeyRectPr);
				KeyTxtPr.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
				KeyTxtPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
				keyTxt = lang.newText(new Offset(5,5, keyRect, "NW"), KEY, "keyTxt", null, KeyTxtPr);
				keyValueRect = lang.newRect(new Offset(0,0, keyRect, "NE"), new Offset(55,0, keyRect, "SE"), "keyValueRect", null, ValueRectPr);
				keyValueTxt = lang.newText(new Offset(-18,4, keyValueRect, "N"), String.valueOf(round(key)), "keyValueTxt", null, ValueTxtPr);
						//KEY-Pfeil
				sc.toggleHighlight(5,6);
				Node[] keyAr = new Node[7];
				Point centerKey = lang.newPoint(new Offset(0,0, keyRect, "C"), "centerKey", null, pPr);			
				centerKey.hide();
				keyAr[0] = new Offset(-105,-5, centerKey, "C");
				keyAr[1] = new Offset(-85,-5, centerKey, "C");
				keyAr[2] = new Offset(-85,-10, centerKey, "C");
				keyAr[3] = new Offset(-65, 0, centerKey, "C");
				keyAr[4] = new Offset(-85, 10, centerKey, "C");
				keyAr[5] = new Offset(-85, 5, centerKey, "C");
				keyAr[6] = new Offset(-105, 5, centerKey, "C");
				PolygonProperties keyArrowPr = new PolygonProperties();
				keyArrowPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				keyArrowPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
				Polygon keyArrow = null;
				try {
					keyArrow = lang.newPolygon(keyAr, "keyArrow", null, keyArrowPr);
				} catch (NotEnoughNodesException e) {
					e.printStackTrace();
				}				
				vars.set("key.", Double.toString(round(key)));
				
				lang.nextStep("3.1. Ermittlung des key-Werts");
				
				
				
				/*************************************** YELLOW-STEP ***********************************************************************************************/
				sc.toggleHighlight(6,7);
				yellowRect.show();
				yellowTxt.show();
				yellowValueRect.show();
				yellowValueTxt.show();
				yellowArrow.show();
				vars.set("yellow", Double.toString(round(yellow)));
				
				lang.nextStep("3.2. Ermittlung des yellow-Werts");
				
				
				/************************************** MAGENTA STEP ***********************************************************************************************/
				sc.toggleHighlight(7,8);
				magentaRect.show();
				magentaTxt.show();
				magentaValueRect.show();
				magentaValueTxt.show();
				magentaArrow.show();
				vars.set("magenta", Double.toString(round(magenta)));

				lang.nextStep("3.3. Ermittlung des magenta-Werts");
				
				
				/*************************************** CYAN STEP **********************************************************************************************/
				sc.toggleHighlight(8,9);
				cyanRect.show();
				cyanTxt.show();
				cyanValueRect.show();
				cyanValueTxt.show();
				cyanArrow.show();
				vars.set("cyan", Double.toString(round(cyan)));

				lang.nextStep("3.4. Ermittlung des cyan-Werts");
				
				
				/********************************** OUTPUT STEP ***********************************************************************************************/
				sc.toggleHighlight(9,10);
				outputRect.show();
				outputTxt.show();
				cmykRect.show();
							
				lang.nextStep("4. Ausgabe der CMYK-Werte");
				
				// Frage 2	
				double rand2 = Math.random()*100;	
				if(rand2 < questProbability) {
					MultipleChoiceQuestionModel quest2 = new MultipleChoiceQuestionModel("quest2");
					quest2.setPrompt("Ändert sich die Farbe durch die Umwandlung des Farbraums?");
					quest2.addAnswer(new AnswerModel("an1", "JA", 5, "FALSCH, genaueres auf der nächsten Seite"));
					quest2.addAnswer(new AnswerModel("an2", "NEIN", 5, "DING DING DING RIICHTIIIIIG"));
					lang.addMCQuestion(quest2);
					lang.nextStep();
				}
				
								
				lang.hideAllPrimitives();
						
				int[] rgb = new int[]{(int) red, (int) green, (int) blue};
				double[] cmyk = new double[]{cyan, magenta, yellow, key};
						
				fazit(rgb, cmyk);
				
			    lang.finalizeGeneration();    
        return lang.toString();
    }

    public String getName() {
        return "RGB-to-CMYK";
    }

    public String getAlgorithmName() {
        return "RGB-to-CMYK";
    }

    public String getAnimationAuthor() {
        return "Samil Kurt";
    }

    public String getDescription(){
        return "Der RGB-Farbraum besteht aus den Grundfarben Rot, Grün und Blau. Dabei kann jedes dieser "
 +"\n"
 +"Farben einen Wert von 0-255 annehmen. "
 +"\n"
 +"Der CMYK-Farbraum hingegen besteht aus den Grundfarben Cyan, Magenta, Yellow und Key. Diese"
 +"\n"
 +"können die Werte zwischen 0 und 1 annehmen. "
 +"\n"
 +"Da sich sowohl die Anazhl der Grundfarben in dem jeweiligen Farbraumsystem, als auch die Werte"
 +"\n"
 +"die, die Farben annehmen können unterscheiden, ist eine korrekte Umwandlung der RGB- in die"
 +"\n"
 +"CMYK-Werte notwendig. "
 +"\n"
 +"Bei der vorliegenden Algorithmenanimation wird die Umwandlung von RGB- in CMYK-Farbraum "
 +"\n"
 +"veranschaulicht.";
    }

    public String getCodeExample(){
        return "public float[ ]  RGBtoCMYKed, float green, float blue) {"
 +"\n"
 +"         float r,g,b, max, cyan,magenta,yellow,key;"
 +"\n"
 +"         r = red / 255;"
 +"\n"
 +"         g = green / 255;"
 +"\n"
 +"         b = blue / 255;"
 +"\n"
 +"         max = maximum (r, g, b);"
 +"\n"
 +"         key = 1 - max;"
 +"\n"
 +"         yellow = (1 - b - key) / (1 - key);"
 +"\n"
 +"         magenta = (1 - g - key) / (1 -key);"
 +"\n"
 +"         cyan = (1 - r - key) / (1 - key);"
 +"\n"
 +"         return new float[ ] {cyan, magenta, yellow, key};"
 +"\n"
 +"} ";
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
        return Generator.JAVA_OUTPUT;
    }

}
