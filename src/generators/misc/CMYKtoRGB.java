package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.Point;
import algoanim.primitives.Polygon;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.primitives.Primitive;
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

import interactionsupport.models.*;


public class CMYKtoRGB implements Generator {
    private Language lang;
    private double magenta;
    private double yellow;
    private double cyan;
    private double key;
	private double questProbability;
	private Color highlightColor;
	private TextProperties tempVariables;
	private TextProperties sourceCode;
	
	private static final String HEADER1 = "CMYK";
	private static final String HEADER2 = "to";
	private static final String HEADER3 = "RGB";
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
	private final PointProperties pPr = new PointProperties();

	private final TextProperties ValueTxtPr = new TextProperties();
	private final RectProperties ValueRectPr = new RectProperties();
	
	private PolygonProperties cyanArrowPr = new PolygonProperties();
	private PolygonProperties magentaArrowPr = new PolygonProperties();
	private PolygonProperties yellowArrowPr = new PolygonProperties();
	private PolygonProperties keyArrowPr = new PolygonProperties();
	
	private Primitive[] hideFehlerFensterArray = null; 	
	private Timing defaultTiming = new TicksTiming(0);	
	private Node[] keyAr, cyanAr, magentaAr, yellowAr;
	private Variables vars;
	
	private RectProperties AlgoRectPr = new RectProperties();
	private final SourceCodeProperties sourcePr = new SourceCodeProperties();
	private SourceCode sc;

    public void init(){
        lang = new AnimalScript("CMYK-to-RGB", "Samil Kurt", 800, 600);
		lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
		lang.setStepMode(true);
    }
	
	private double round(double a) {
		return (double) (Math.round(a*1000)/1000.0);
	}
	
	
	private void showSourceCode(){	
	//new Font("Monospaced", Font.PLAIN, 12)
		sourcePr.set(AnimationPropertiesKeys.FONT_PROPERTY, sourceCode.get(AnimationPropertiesKeys.FONT_PROPERTY));
		sourcePr.set(AnimationPropertiesKeys.COLOR_PROPERTY, sourceCode.get(AnimationPropertiesKeys.COLOR_PROPERTY));
		sourcePr.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlightColor);
		sourcePr.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		
		sc = lang.newSourceCode(new Coordinates(30,360), "sourcCode", null, sourcePr);
		sc.addCodeLine("public int[] algorithm(double cyan, double magenta, double yellow, double key){", null, 0, null);
		sc.addCodeLine("double diffkey = 1 - key;", null, 2, null);
		sc.addCodeLine("", null, 2, null);
		sc.addCodeLine("int red = (int) (Math.round (255 * (1 - cyan) * diffKey));"  , null, 2, null);
		sc.addCodeLine("int green = (int) (Math.round (255 * (1 - magenta) * diffKey));", null, 2, null);
		sc.addCodeLine("int blue = (int) (Math.round (255* (1 - yellow) * diffKey));", null, 2, null);
		sc.addCodeLine("", null, 2, null);
		sc.addCodeLine("return new int[] {red, green, blue};", null, 2, null);
		sc.addCodeLine("}", null, 0, null);
				
		Rect scRect = lang.newRect(new Offset(-15,-5, sc, "NW"), new Offset(15,5, sc, "SE"), "scRect", null, AlgoRectPr);
	}
	
	private void introduction(){
		HeaderRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);			
		HeaderRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(223,223,223));
		headerRect = lang.newRect(new Coordinates(15,15), new Coordinates(625,55), "headerRect", null, HeaderRectPr);
		HeaderTxtPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 27));
		String header = "Umwandlung von CMYK- zu RGB-Farbraum";
		Text intrTxt = lang.newText(new Offset(20,14, headerRect, "NW"), header, "header1", null, HeaderTxtPr);
		
		TextProperties zeilPr = new TextProperties();
		zeilPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 17));
		String zeile1 = "Ein Farbraum besteht immer aus den sogenannten Grundfarben. Diese Grund-";
		String zeile2 = "farben ermöglichen es durch Mischen jede beliebige andere Farbe im ";
		String zeile3 = "Farbraum darzustellen.";
		String zeile33 = "Der RGB-Farbraum besteht aus den Grundfarben Rot, Grün & Blau und der ";
		String zeile4 = "CMYK-Farbraum aus Cyan, Magenta, Yellow & Key, wobei Key für die ";
		String zeile5 = "Abtönung der Farbe zuständig ist."; 
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
	
	
	private void fazit(int[] rgb, float[] cmyk){
		headerRect.show();
		HeaderTxtPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		String header = "Fazit";
		Text headerTxt = lang.newText(new Offset(30, 13, headerRect, "NW"), header, "headerTxt", null, HeaderTxtPr);
		
		TextProperties zeilPr = new TextProperties();
		zeilPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 17));
		String zeile1 = "Die errechneten Werte für den RGB-Raum bilden genau dieselbe Farbe wie";
		String zeile2 = "die Werte vom CMYK-Raum ab. D.h. die Umwandlung von Farbräumen";
		String zeile3 = "ändert nicht die Farbe, sondern nur das zugrunde liegende System, dass ";
		String zeile4 = "die Farbe bestimmt.";
		String zeile5 = "In diesem Durchlauf des Algorithmus ist die Farbe, die durch die CMYK-Werte";
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
		
		String zeile66 = "& die RGB-Werte bilden die Farbe";
		Text z66 = lang.newText(new Offset(12, -3, rgbRect, "NE"), zeile66, "z6", null, zeilPr);
		Rect cmykRect = lang.newRect(new Offset(10, 2, z66, "NE"), new Offset(95, -2, z66, "SE"), "cmykRect", null, rgbRectPr); 
		
		String zeile7 = "Hieran kann man erkennen, dass die Umwandlung die Farbe nicht verändert";
		String zeile8 = "& da die Farbfelder die gleiche Farbe anzeigen wurde korrekt umgewandelt.";
		Text z7 = lang.newText(new Offset(0, 10, z6, "SW"), zeile7, "z7", null, zeilPr);
		Text z8 = lang.newText(new Offset(0, 10, z7, "SW"), zeile8, "z8", null, zeilPr);

		lang.nextStep("5. Fazit");	
		
	}
	
	
		private double[] fehlerFenster(double cyan, double magenta, double yellow, double key) {
		double[] neueWerte = new double[]{cyan, magenta, yellow, key};
		if(cyan < 0.0 || cyan > 1.0 || magenta < 0.0 || magenta > 1.0 || yellow < 0.0 || yellow > 1.0 || key < 0.0 || key > 1.0) {
			int fehlerArray = 0;
			Boolean cyanFalse = false;
			Boolean magentaFalse = false;
			Boolean yellowFalse = false;
			Boolean keyFalse = false;
			int falseNumber = 0;
			if (cyan < 0.0 || cyan > 1.0) {
				if(cyan > 1.0) neueWerte[0] = 1.0;
				else if(cyan < 0.0) neueWerte[0] = 0.0;
				cyanFalse = true;
				falseNumber++;
			}
			if (magenta < 0.0 || magenta > 1.0) {
				if(magenta > 1.0) neueWerte[1] = 1.0;
				else if(magenta < 0.0) neueWerte[1] = 0.0;
				magentaFalse = true;
				falseNumber++;
			}
			if (yellow < 0.0 || yellow > 1.0) {
				if(yellow > 1.0) neueWerte[2] = 1.0;
				else if(yellow < 0.0) neueWerte[2] = 0.0;
				yellowFalse = true;
				falseNumber++;
			}
			if (key < 0.0 || key > 1.0) {
				if(key > 1.0) neueWerte[3] = 0.0;
				else if(key < 0.0) neueWerte[3] = 0.0;
				keyFalse = true;
				falseNumber++;
			}
			
			
			TextProperties tPr = new TextProperties();
			tPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));
			tPr.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
			
			String stT0 = "ACHTUNG";
			String stT00 = "FARBWERT";
			String stT000 = "KORREK";
			String stT0000 = "TUR:";
			String stT1 = "Wenn Sie ein fleißiger Student";
			String stT2 = "wären, hätten Sie in der";
			String stT3 = "Beschreibung gelesen, dass die";
			String stT4 = "Werte immer von 0-1 gehen!";
			String stT5 = "Ihr CYAN-Wert = " + String.valueOf(cyan);
			String stT6 = "Ihr MAGENTA-Wert = " + String.valueOf(magenta);
			String stT7 = "Ihr YELLOW-Wert = " + String.valueOf(yellow);
			String stKey1 = "Ihr KEY-Wert = " + String.valueOf(key);
			String stKey2 = "KEY = " + String.valueOf(neueWerte[3]);
			String stT8 = "Daher werden Defaultwerte";
			String stT88 = "Daher wird der Defaultwert";
			String stT9 = "CYAN = " + String.valueOf(neueWerte[0]);
			String stT10 = "MAGENTA = " + String.valueOf(neueWerte[1]);
			String stT11 = "YELLOW = " + String.valueOf(neueWerte[2]);
			String stT12 = "gesetzt & der Algorithmus";
			String stT13 = "mit diesen fortgesetzt.";
			String stT133 = "mit diesem forgesetzt.";

			RectProperties fehlerRectPr = new RectProperties();
			RectProperties fehlerHeaderPr =new RectProperties();
			fehlerRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			fehlerRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
			fehlerHeaderPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
			fehlerHeaderPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
			Rect fehlerHeaderRect = lang.newRect(new Offset(-400, 55, headerRect, "SE"), new Offset(-140, 80, headerRect, "SE"), "fehlerHeaderRect", null, fehlerHeaderPr);
			fehlerArray++;
			Rect fehlerRect;
			if (falseNumber == 4) 
				fehlerRect = lang.newRect(new Offset(0,0, fehlerHeaderRect, "SW"), new Offset(0,370, fehlerHeaderRect, "SE"), "fehlerRect", null, fehlerRectPr);	
			else if(falseNumber == 3) 
				fehlerRect = lang.newRect(new Offset(0,0, fehlerHeaderRect, "SW"), new Offset(0,325, fehlerHeaderRect, "SE"), "fehlerRect", null, fehlerRectPr);	
			else if(falseNumber == 2) 
				fehlerRect = lang.newRect(new Offset(0,0, fehlerHeaderRect, "SW"), new Offset(0,280, fehlerHeaderRect, "SE"), "fehlerRect", null, fehlerRectPr);
			else fehlerRect = lang.newRect(new Offset(0,0, fehlerHeaderRect, "SW"), new Offset(0,235, fehlerHeaderRect, "SE"), "fehlerRect", null, fehlerRectPr);
			fehlerArray++;

			
			
			Text t0 = lang.newText(new Offset(5,2, fehlerHeaderRect, "NW"), stT0, "t0", null, tPr);
			t0.changeColor(null, Color.CYAN, defaultTiming, defaultTiming);
			Text t00 = lang.newText(new Offset(5,0, t0, "NE"), stT00, "t00", null, tPr);
			t00.changeColor(null, Color.MAGENTA, defaultTiming, defaultTiming);
			Text t000 = lang.newText(new Offset(0,0, t00, "NE"), stT000, "t000", null, tPr);
			t000.changeColor(null, Color.YELLOW, defaultTiming, defaultTiming);
			Text t0000 = lang.newText(new Offset(0,0, t000, "NE"), stT0000, "t0000", null, tPr);
			t0.setFont(new Font("SansSerif", Font.BOLD, 14), defaultTiming, defaultTiming);
			fehlerArray += 4;
			Text t1 = lang.newText(new Offset(10, 10, t0, "SW"), stT1, "t1", null, tPr);
			Text t2 = lang.newText(new Offset(0, 5, t1, "SW"), stT2, "t2", null, tPr);
			Text t3 = lang.newText(new Offset(0, 5, t2, "SW"), stT3, "t3", null, tPr);
			Text t4 = lang.newText(new Offset(0, 5, t3, "SW"), stT4, "t4", null, tPr);
			fehlerArray += 4;
			
			Text t5 = null;
			if(cyanFalse) {
				t5 = lang.newText(new Offset(0, 5, t4, "SW"), stT5, "t5", null, tPr);
			}
			Rect cyanFRect = null;
			if(t5!=null){
				RectProperties cyanFRectPr = new RectProperties();
				cyanFRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				cyanFRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.CYAN);
				cyanFRect = lang.newRect(new Offset(-7, 1, t5, "NW"), new Offset(235, 19, t5, "NW"), "redFRect", null, cyanFRectPr);  
				t5.hide();
				t5 = lang.newText(new Offset(0, 5, t4, "SW"), stT5, "t5", null, tPr);
				fehlerArray += 2;
			}

			Text t6 = null;
			if(magentaFalse) {
				if (cyanFalse) t6 = lang.newText(new Offset(0, 5, t5, "SW"), stT6, "t6", null, tPr);
				else t6 = lang.newText(new Offset(0, 5, t4, "SW"), stT6, "t6", null, tPr);
			}
			
			Rect magentaFRect = null;
			if(t6!=null) {
				RectProperties magentaFRectPr = new RectProperties();
				magentaFRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				magentaFRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.MAGENTA);
				magentaFRect = lang.newRect(new Offset(-7, 1, t6, "NW"), new Offset(235, 19, t6, "NW"), "greenFRect", null, magentaFRectPr); 
				t6.hide();
				if (cyanFalse) t6 = lang.newText(new Offset(0, 5, t5, "SW"), stT6, "t6", null, tPr);
				else t6 = lang.newText(new Offset(0, 5, t4, "SW"), stT6, "t6", null, tPr);
				fehlerArray += 2;
			}
			Text t7=null;
			if(yellowFalse) {
				if (cyanFalse && magentaFalse) t7 = lang.newText(new Offset(0, 5, t6, "SW"), stT7, "t7", null, tPr);
				else if (cyanFalse || magentaFalse) t7 = lang.newText(new Offset(0, 5+18+5, t4, "SW"), stT7, "t7", null, tPr);
				else t7 = lang.newText(new Offset(0, 5, t4, "SW"), stT7, "t7", null, tPr);
			}
			Rect yellowFRect = null;
			if(t7!=null) {
				RectProperties yellowFRectPr = new RectProperties();
				yellowFRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				yellowFRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
				yellowFRect = lang.newRect(new Offset(-7, 1, t7, "NW"), new Offset(235, 19, t7, "NW"), "blueFRect", null, yellowFRectPr); 
				t7.hide();
				if (cyanFalse && magentaFalse) t7 = lang.newText(new Offset(0, 5, t6, "SW"), stT7, "t7", null, tPr);
				else if (cyanFalse || magentaFalse) t7 = lang.newText(new Offset(0, 5+18+5, t4, "SW"), stT7, "t7", null, tPr);
				else t7 = lang.newText(new Offset(0, 5, t4, "SW"), stT7, "t7", null, tPr);
				fehlerArray += 2;
			}
			Text tKey1 = null;
			if(keyFalse) {
				if(falseNumber == 4) tKey1 = lang.newText(new Offset(0,5, t7, "SW"), stKey1, "stKey1", null, tPr);
				else if(falseNumber == 3) tKey1 = lang.newText(new Offset(0, (2+1)*5+2*18 , t4, "SW"),  stKey1, "stKey1", null, tPr);
				else if(falseNumber == 2) tKey1 = lang.newText(new Offset(0, 5+18+5, t4, "SW"),  stKey1, "stKey1", null, tPr);	
				else tKey1 = lang.newText(new Offset(0, 5, t4, "SW"),  stKey1, "stKey1", null, tPr);		
			}
			Rect keyFRect = null;
			if(tKey1!=null) {
				RectProperties keyFRectPr = new RectProperties();
				keyFRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				keyFRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
				keyFRect = lang.newRect(new Offset(-7, 1, tKey1, "NW"), new Offset(235, 19, tKey1, "NW"), "keyFRect", null, keyFRectPr); 
				tKey1.hide();
				if(falseNumber == 4) tKey1 = lang.newText(new Offset(0,5, t7, "SW"), stKey1, "stKey1", null, tPr);
				else if(falseNumber == 3) tKey1 = lang.newText(new Offset(0, (2+1)*5+2*18 , t4, "SW"),  stKey1, "stKey1", null, tPr);
				else if(falseNumber == 2) tKey1 = lang.newText(new Offset(0, 5+18+5, t4, "SW"),  stKey1, "stKey1", null, tPr);	
				else tKey1 = lang.newText(new Offset(0, 5, t4, "SW"),  stKey1, "stKey1", null, tPr);		
				tKey1.changeColor(null, Color.WHITE, defaultTiming, defaultTiming);
				fehlerArray += 2;
			}
			
			Text t8;
			if (falseNumber == 4) 
				t8 = lang.newText(new Offset(0, (4+1)*5+4*18     + 5, t4, "SW"), stT8, "t8", null, tPr);
			else if(falseNumber == 3)
				t8 = lang.newText(new Offset(0, (3+1)*5+3*18     + 5, t4, "SW"), stT8, "t8", null, tPr);
			else if(falseNumber > 1)
				t8 = lang.newText(new Offset(0, (2+1)*5+2*18     +5, t4, "SW"), stT8, "t8", null, tPr);
			else t8 = lang.newText(new Offset(0, (1+1)*5+18     +5, t4, "SW"), stT88, "t88", null, tPr);
			fehlerArray++;
			
			Text t9 = null;
			if(cyanFalse) t9 = lang.newText(new Offset(0, 5, t8, "SW"), stT9, "t9", null, tPr);
			Rect cyan2FRect = null;
			if(t9!=null){
				RectProperties cyan2FRectPr = new RectProperties();
				cyan2FRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				cyan2FRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.CYAN);
				cyan2FRect = lang.newRect(new Offset(-7, 1, t9, "NW"), new Offset(235, 19, t9, "NW"), "red2FRect", null, cyan2FRectPr); 
				t9.hide();
				if(cyanFalse) t9 = lang.newText(new Offset(0, 5, t8, "SW"), stT9, "t9", null, tPr);
				fehlerArray += 2;
			}
			Text t10 = null;
			if(magentaFalse) {
				if (t9 == null) t10 = lang.newText(new Offset(0, 5, t8, "SW"), stT10, "t10", null, tPr);
				else t10 = lang.newText(new Offset(0, 5+5+18, t8, "SW"), stT10, "t10", null, tPr);
			}
			Rect magenta2FRect = null;
			if(t10!=null) {
				RectProperties magenta2FRectPr = new RectProperties();
				magenta2FRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				magenta2FRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.MAGENTA);
				magenta2FRect = lang.newRect(new Offset(-7, 1, t10, "NW"), new Offset(235, 19, t10, "NW"), "green2FRect", null, magenta2FRectPr); 
				t10.hide();				
				if (t9 == null) t10 = lang.newText(new Offset(0, 5, t8, "SW"), stT10, "t10", null, tPr);
				else t10 = lang.newText(new Offset(0, 5+5+18, t8, "SW"), stT10, "t10", null, tPr);
				fehlerArray += 2;
			}
			Text t11 = null;
			if(yellowFalse) {
				if (t9 == null && t10 == null) t11 = lang.newText(new Offset(0, 5, t8, "SW"), stT11, "t11", null, tPr);
				else if (t9 == null || t10 == null) t11 = lang.newText(new Offset(0, 5+5+18, t8, "SW"), stT11, "t11", null, tPr);
				else t11 = lang.newText(new Offset(0, 5+5+5+18+18, t8, "SW"), stT11, "t11", null, tPr);	
			}
			Rect yellow2FRect = null;
			if(t11!=null) {
				RectProperties yellow2FRectPr = new RectProperties();
				yellow2FRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				yellow2FRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
				yellow2FRect = lang.newRect(new Offset(-7, 1, t11, "NW"), new Offset(235, 19, t11, "NW"), "blue2FRect", null, yellow2FRectPr);
				t11.hide();
				if (t9 == null && t10 == null) t11 = lang.newText(new Offset(0, 5, t8, "SW"), stT11, "t11", null, tPr);
				else if (t9 == null || t10 == null) t11 = lang.newText(new Offset(0, 5+5+18, t8, "SW"), stT11, "t11", null, tPr);
				else t11 = lang.newText(new Offset(0, 5+5+5+18+18, t8, "SW"), stT11, "t11", null, tPr);	
				fehlerArray += 2;
			}
			Text tKey2 = null;
			if(keyFalse) {
				if (falseNumber == 1) tKey2 = lang.newText(new Offset(0, 5, t8, "SW"), stKey2, "tKey2", null, tPr);
				else if (falseNumber == 2) tKey2 = lang.newText(new Offset(0, 5+5+18, t8, "SW"), stKey2, "tKey2", null, tPr);
				else if (falseNumber == 3) tKey2 = lang.newText(new Offset(0, 5+5+5+18+18, t8, "SW"), stKey2, "tKey2", null, tPr);	
				else tKey2 = lang.newText(new Offset(0, 5+5+5+5+18+18+18, t8, "SW"), stKey2, "tKey2", null, tPr);	
			}
			Rect key2FRect = null;
			if(tKey2!=null) {
				RectProperties key2FRectPr = new RectProperties();
				key2FRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
				key2FRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
				key2FRect = lang.newRect(new Offset(-7, 1, tKey2, "NW"), new Offset(235, 19, tKey2, "NW"), "key2FRect", null, key2FRectPr);
				tKey2.hide();
				if (falseNumber == 1) tKey2 = lang.newText(new Offset(0, 5, t8, "SW"), stKey2, "tKey2", null, tPr);
				else if (falseNumber == 2) tKey2 = lang.newText(new Offset(0, 5+5+18, t8, "SW"), stKey2, "tKey2", null, tPr);
				else if (falseNumber == 3) tKey2 = lang.newText(new Offset(0, 5+5+5+18+18, t8, "SW"), stKey2, "tKey2", null, tPr);	
				else tKey2 = lang.newText(new Offset(0, 5+5+5+5+18+18+18, t8, "SW"), stKey2, "tKey2", null, tPr);
				tKey2.changeColor(null, Color.WHITE, defaultTiming, defaultTiming);
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
			if(cyanFRect !=null) {
				hideFehlerFensterArray[i] = cyanFRect;
				i++;
			}
			if(cyan2FRect !=null) {
				hideFehlerFensterArray[i] = cyan2FRect;
				i++;
			}
			if(yellowFRect !=null) {
				hideFehlerFensterArray[i] = yellowFRect;
				i++;
			}
			if(yellow2FRect !=null) {
				hideFehlerFensterArray[i] = yellow2FRect;
				i++;
			}
			if(magentaFRect !=null) {
				hideFehlerFensterArray[i] = magentaFRect;
				i++;
			}
			if(magenta2FRect !=null) {
				hideFehlerFensterArray[i] = magenta2FRect;
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
			if(t0000 != null) {
				hideFehlerFensterArray[i] = t0000;
				i++;
			}
			if(tKey1 != null) {
				hideFehlerFensterArray[i] = tKey1;
				i++;
			}
			if(tKey2 != null) {
				hideFehlerFensterArray[i] = tKey2;
				i++;
			}
			if(keyFRect != null) {
				hideFehlerFensterArray[i] = keyFRect;
				i++;
			}
			if(key2FRect != null) {
				hideFehlerFensterArray[i] = key2FRect;
				i++;
			}
		}
		return neueWerte;
	}
	
	private void hideFehlerFenster() {
		if (hideFehlerFensterArray!=null)
			for(int i=0; i<hideFehlerFensterArray.length; i++)
				hideFehlerFensterArray[i].hide();
		hideFehlerFensterArray = null;
	}
	
	
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		int red, green, blue;
		magenta = (double)primitives.get("magenta");
        yellow = (double)primitives.get("yellow");
        cyan = (double)primitives.get("cyan");
        key = (double)primitives.get("key");
		highlightColor = (Color)primitives.get("highlightColor");
		questProbability = (double)primitives.get("questProbability");
		if(questProbability > 100) questProbability = 100;
		else if(questProbability < 0) questProbability = 0; 
		tempVariables = (TextProperties)props.getPropertiesByName("tempVariables");
		sourceCode = (TextProperties)props.getPropertiesByName("sourceCode");
		
		
		
		ValueTxtPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 15));
		ValueRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		ValueRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		UpperRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		UpperRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(127,127,127));
		UpperTxtPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 20));
		LowerRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		LowerRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(195,195,195));
		ColorTxtPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
		
		
		
		/*********************************** VARIABLEN DINGS BUMS *********************************************************************************************/
		vars = lang.newVariables();		
		vars.declare("String", "red");
		vars.set("red", "xxxxx");
		vars.declare("String", "green");
		vars.set("green", "xxxxx");
		vars.declare("String", "blue");
		vars.set("blue", "xxxxx");
		vars.declare("String", "diffKey");
		vars.set("diffKey", "xxxxx");
		vars.declare("String", "cyan");
		vars.set("cyan", "xxxxx");
		vars.declare("String", "magenta");
		vars.set("magenta", "xxxxx");
		vars.declare("String", "yellow");
		vars.set("yellow", "xxxxx");
		vars.declare("String", "key.");
		vars.set("key.", "xxxxx");
		
		
		/*********************************** EINFÜHRUNG *****************************************************************************************************/
		introduction();
		
		
		/************************************HEADER STEP**************************************************************************************************/
		// Überschrift 
		HeaderRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		HeaderRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(223,223,223));
		headerRect = lang.newRect(new Coordinates(15,13), new Coordinates(625,55), "headerRect", null, HeaderRectPr);
		HeaderTxtPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 30));
		headerTxt1 = lang.newText(new Offset(32,13, headerRect, "NW"), HEADER1, "header1", null, HeaderTxtPr);
		headerTxt2 = lang.newText(new Offset(175,-2, headerTxt1, "NE"), HEADER2, "header2", null, HeaderTxtPr);
		headerTxt3 = lang.newText(new Offset(180,0, headerTxt2, "NE"), HEADER3, "header3", null, HeaderTxtPr);
		
		lang.nextStep();
		

		/***********************************INPUT ******************************************************************************************************/
		// Obere Kasten
		outputRect = lang.newRect(new Offset(0,22, headerRect, "SW"), new Offset(160,62, headerRect, "SW"), "ouputRect", null, UpperRectPr);
		outputTxt = lang.newText(new Offset(-30, -3, outputRect, "N"), INPUT, "output", null, UpperTxtPr);
		// Untere Kasten
		cmykRect = lang.newRect(new Offset(0,0, outputRect, "SW"), new Offset(0,205, outputRect, "SE"), "cmykRect", null, LowerRectPr);
			// CYAN
		CyanRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		CyanRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(0,255,255));
		cyanRect = lang.newRect(new Offset(10, 15, outputRect, "SW"), new Offset(-70, 43, outputRect, "SE"), "cyanRect", null, CyanRectPr);
		cyanTxt = lang.newText(new Offset(5,-1, cyanRect, "NW"), CYAN, "cyanTxt", null, ColorTxtPr);
		cyanValueRect = lang.newRect(new Offset(0,0, cyanRect, "NE"), new Offset(55,0, cyanRect, "SE"), "cyanValueRect", null, ValueRectPr);
				//CYAN-Pfeil
		cyanAr = new Node[7];
		Point centerCyan = lang.newPoint(new Offset(0,0, cyanRect, "C"), "centerCyan", null, pPr);	
		centerCyan.hide();
		cyanAr[0] = new Offset(125,-5, centerCyan, "C");
		cyanAr[1] = new Offset(145,-5, centerCyan, "C");
		cyanAr[2] = new Offset(145,-10, centerCyan, "C");
		cyanAr[3] = new Offset(165, 0, centerCyan, "C");
		cyanAr[4] = new Offset(145, 10, centerCyan, "C");
		cyanAr[5] = new Offset(145, 5, centerCyan, "C");
		cyanAr[6] = new Offset(125, 5, centerCyan, "C");
		cyanArrowPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		cyanArrowPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.CYAN);
			// MAGENTA
		MagentaRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		MagentaRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255,0,255));
		magentaRect = lang.newRect(new Offset(0, 20, cyanRect, "SW"), new Offset(0, 48, cyanRect, "SE"), "magentaRect", null, MagentaRectPr);
		magentaTxt = lang.newText(new Offset(5,5, magentaRect, "NW"), MAGENTA, "magentaTxt", null, ColorTxtPr);
		magentaValueRect = lang.newRect(new Offset(0,0, magentaRect, "NE"), new Offset(55,0, magentaRect, "SE"), "magentaValueRect", null, ValueRectPr);
				//MAGENTA-Pfeil
		magentaAr = new Node[7];
		Point centerMagenta = lang.newPoint(new Offset(0,0, magentaRect, "C"), "centerMagenta", null, pPr);		
		centerMagenta.hide();
		magentaAr[0] = new Offset(125,-5, centerMagenta, "C");
		magentaAr[1] = new Offset(145,-5, centerMagenta, "C");
		magentaAr[2] = new Offset(145,-10, centerMagenta, "C");
		magentaAr[3] = new Offset(165, 0, centerMagenta, "C");
		magentaAr[4] = new Offset(145, 10, centerMagenta, "C");
		magentaAr[5] = new Offset(145, 5, centerMagenta, "C");
		magentaAr[6] = new Offset(125, 5, centerMagenta, "C");
		magentaArrowPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		magentaArrowPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.MAGENTA);
			// YELLOW
		YellowRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		YellowRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255,255,0));
		yellowRect = lang.newRect(new Offset(0, 20, magentaRect, "SW"), new Offset(0, 48, magentaRect, "SE"), "yellowRect", null, YellowRectPr);
		yellowTxt = lang.newText(new Offset(5,5, yellowRect, "NW"), YELLOW, "yellowTxt", null, ColorTxtPr);
		yellowValueRect = lang.newRect(new Offset(0,0, yellowRect, "NE"), new Offset(55,0, yellowRect, "SE"), "yellowValueRect", null, ValueRectPr);
				//YELLOW-Pfeil
		yellowAr = new Node[7];
		Point centerYellow = lang.newPoint(new Offset(0,0, yellowRect, "C"), "centerYellow", null, pPr);
		centerYellow.hide();
		yellowAr[0] = new Offset(125,-5, centerYellow, "C");
		yellowAr[1] = new Offset(145,-5, centerYellow, "C");
		yellowAr[2] = new Offset(145,-10, centerYellow, "C");
		yellowAr[3] = new Offset(165, 0, centerYellow, "C");
		yellowAr[4] = new Offset(145, 10, centerYellow, "C");
		yellowAr[5] = new Offset(145, 5, centerYellow, "C");
		yellowAr[6] = new Offset(125, 5, centerYellow, "C");
		yellowArrowPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		yellowArrowPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
			// KEY
		KeyRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		KeyRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(0,0,0));
		keyRect = lang.newRect(new Offset(0, 20, yellowRect, "SW"), new Offset(0, 48, yellowRect, "SE"), "keyRect", null, KeyRectPr);
		KeyTxtPr.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
		KeyTxtPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
		keyTxt = lang.newText(new Offset(5,5, keyRect, "NW"), KEY, "keyTxt", null, KeyTxtPr);
		keyValueRect = lang.newRect(new Offset(0,0, keyRect, "NE"), new Offset(55,0, keyRect, "SE"), "keyValueRect", null, ValueRectPr);
				//KEY-Pfeil
		Point centerKey = lang.newPoint(new Offset(0,0, keyRect, "C"), "centerKey", null, pPr);			
		centerKey.hide();
		keyAr = new Node[7];
		keyAr[0] = new Offset(125,-5, centerKey, "C");
		keyAr[1] = new Offset(145,-5, centerKey, "C");
		keyAr[2] = new Offset(145,-10, centerKey, "C");
		keyAr[3] = new Offset(165, 0, centerKey, "C");
		keyAr[4] = new Offset(145, 10, centerKey, "C");
		keyAr[5] = new Offset(145, 5, centerKey, "C");
		keyAr[6] = new Offset(125, 5, centerKey, "C");
		keyArrowPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		keyArrowPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
		

		// AlgoRectProperty
		AlgoRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		AlgoRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255, 253, 202));
		AlgoRectPr.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		
		
		lang.nextStep();
				
			
		// Allgemeine Variablen
		TextProperties cookTxtPr = new TextProperties();
		cookTxtPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
		PointProperties pPr = new PointProperties();
		
		
		/*************************************** SOURCE-CODE *********************************************************************************************/
		showSourceCode();
		Rect cookRect = lang.newRect(new Offset(-50, 100, headerRect, "C"), new Offset(70, 130, headerRect, "C"), "cookRect", null, AlgoRectPr); 
		TextProperties algoTxtPr = new TextProperties();
		algoTxtPr.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
		Text algoTxt = lang.newText(new Offset(-48,9, cookRect, "N"), "ALGORITHM", "algoTxt", null, algoTxtPr);
		Rect algoRect = lang.newRect(new Offset(0, 0, cookRect, "SW"), new Offset(0, 70, cookRect, "SE"), "algoRect", null);

		lang.nextStep();
		
		// Frage1
		double rand1 = Math.random()*100;	
		if(rand1 < questProbability) {
			MultipleChoiceQuestionModel quest1 = new MultipleChoiceQuestionModel("quest1");
			quest1.setPrompt("Welche Werte können die Farben im CMYK-Farbraum annehmen?");
			quest1.addAnswer(new AnswerModel("an1", "0 - 100", 5, "FALSCH"));
			quest1.addAnswer(new AnswerModel("an2", "0 -255", 5, "FALSCH"));
			quest1.addAnswer(new AnswerModel("an3", "0 - 1", 5, "WOW DAS WAR RICHTG GUT GEMACHT :D"));
			quest1.addAnswer(new AnswerModel("an4", "0 - 125", 5, "FALSCH"));
			lang.addMCQuestion(quest1);
			lang.nextStep();
		}
		
		
		/***************************************** CMYK-STEP ************************************************************************************************/
		sc.highlight(0);
		lang.nextStep();
		cyanValueTxt = lang.newText(new Offset(-18,4, cyanValueRect, "N"), String.valueOf(round(cyan)), "cyanValueTxt", null, ValueTxtPr);
		yellowValueTxt = lang.newText(new Offset(-18,4, yellowValueRect, "N"), String.valueOf(round(yellow)), "yellowValueTxt", null, ValueTxtPr);
		magentaValueTxt = lang.newText(new Offset(-18,4, magentaValueRect, "N"), String.valueOf(round(magenta)), "magentaValueTxt", null, ValueTxtPr);
		keyValueTxt = lang.newText(new Offset(-18,4, keyValueRect, "N"), String.valueOf(round(key)), "keyValueTxt", null, ValueTxtPr);
		
		vars.set("cyan", Float.toString((float) round(cyan)));
		vars.set("magenta", Float.toString((float) round(magenta)));
		vars.set("yellow", Float.toString((float) round(yellow)));
		vars.set("key.", Float.toString((float) round(key)));
	
		lang.nextStep("1. Eingabe der CMYK-Werte");
		
		
		double[] newNumber = fehlerFenster(cyan, magenta, yellow, key);	
		
		
		if(hideFehlerFensterArray != null) {
			cyan = newNumber[0];
			magenta = newNumber[1];
			yellow = newNumber[2];
			key = newNumber[3];
				
			lang.nextStep();
		
			cyanValueTxt.setText(Float.toString((float) round(cyan)), defaultTiming, defaultTiming);
			magentaValueTxt.setText(Float.toString((float) round(magenta)), defaultTiming, defaultTiming);
			yellowValueTxt.setText(Float.toString((float) round(yellow)), defaultTiming, defaultTiming);
			keyValueTxt.setText(Float.toString((float) round(key)), defaultTiming, defaultTiming);

			vars.set("cyan", Float.toString((float) round(cyan)));
			vars.set("magenta", Float.toString((float) round(magenta)));
			vars.set("yellow", Float.toString((float) round(yellow)));
			vars.set("key.", Float.toString((float) round(key)));
			
			lang.nextStep();
		}
			
		hideFehlerFenster();
		
				
		double diffKey = 1 - key;
		red = (int) (Math.round((255 * (1 - cyan) * diffKey)));
		green = (int) (Math.round((255 * (1 -magenta) * diffKey)));
		blue = (int) (Math.round((255 * (1 - yellow) * diffKey)));
		
		/****************************************** ARROW-STEP*************************************************************************************/
		Polygon cyanArrow;
		Polygon magentaArrow;
		Polygon yellowArrow;
		Polygon keyArrow;
		try {
			cyanArrow = lang.newPolygon(cyanAr, "pfeil4", null, cyanArrowPr);
			magentaArrow = lang.newPolygon(magentaAr, "magentaArrow", null, magentaArrowPr);
			yellowArrow = lang.newPolygon(yellowAr, "yellowArrow", null, yellowArrowPr);
			keyArrow = lang.newPolygon(keyAr, "keyArrow", null, keyArrowPr);	
		} catch (NotEnoughNodesException e) {
			e.printStackTrace();
		}	
		
		lang.nextStep();
		
		
		/***************************************** HILFSVARIABLE ******************************************************************************/
		sc.toggleHighlight(0,1);
		Text diffKeyTxt = lang.newText(new Offset(10,15, cookRect, "SW"), "diffkey = " + String.valueOf(round((float) diffKey)), "rTxt", null, tempVariables);
		diffKeyTxt.changeColor(null, highlightColor, defaultTiming, defaultTiming);
		vars.set("diffKey", Float.toString((float) (round(diffKey))));
		
		lang.nextStep("2. Berechnen der Hilfsvariable");
		diffKeyTxt.changeColor(null, (Color) tempVariables.get(AnimationPropertiesKeys.COLOR_PROPERTY), defaultTiming, defaultTiming);

		
		/******************************************** RGB-WERTE & -PFEILE ***************************************************************************/
		// Obere Kasten
		inputRect = lang.newRect(new Offset(-140, 22, headerRect, "SE"), new Offset(0, 62, headerRect,"SE"), "inputRect", null, UpperRectPr);
		inputTxt = lang.newText(new Offset(-40, 15, inputRect, "N"), OUTPUT, "input", null, UpperTxtPr);
			// untere kasten
		rgbRect = lang.newRect(new Offset(0, 0, inputRect, "SW"), new Offset(0, 155, inputRect, "SE"), "rgbRect", null, LowerRectPr);
		rgbRect.hide();
		inputRect.hide();
		inputTxt.hide();
		
				//RED
		sc.toggleHighlight(1,3);
		RedRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		RedRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(237,28,36));
		redRect = lang.newRect(new Offset(10, 15, inputRect, "SW"), new Offset(-60, 43, inputRect, "SE"), "redRect", null, RedRectPr);
		redTxt = lang.newText(new Offset(5,-1, redRect, "NW"), RED, "redTxt", null, ColorTxtPr);
		redValueRect = lang.newRect(new Offset(0,0, redRect, "NE"), new Offset(45,0, redRect, "SE"), "redValueRect", null, ValueRectPr);
		redValueTxt = lang.newText(new Offset(-10,4, redValueRect, "N"), String.valueOf((int)red), "redValueTxt", null, ValueTxtPr);
		vars.set("red", Integer.toString(red));
					// RED-Pfeil
		Node[] redAr = new Node[7];
		Point centerRed = lang.newPoint(new Offset(0,0, redRect, "C"), "centerRed", null, pPr);
		centerRed.hide();
		redAr[0] = new Offset(-100, -5, centerRed, "C");
		redAr[1] = new Offset(-80, -5, centerRed, "C");
		redAr[2] = new Offset(-80, -10, centerRed, "C");
		redAr[3] = new Offset(-60, 0, centerRed, "C");
		redAr[4] = new Offset(-80, 10, centerRed, "C");
		redAr[5] = new Offset(-80, 5, centerRed, "C");
		redAr[6] = new Offset(-100, 5, centerRed, "C");
		PolygonProperties RedArrowPr = new PolygonProperties();
		RedArrowPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		RedArrowPr.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
		Polygon redArrow;
		try {
			redArrow = lang.newPolygon(redAr, "redArrow", null, RedArrowPr);
		} catch (NotEnoughNodesException e) {
			e.printStackTrace();
		}		lang.nextStep("3.1. Ermittlung des red-Werts");
		
				//GREEN
		sc.toggleHighlight(3,4);
		GreenRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		GreenRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(34,177,76));
		greenRect = lang.newRect(new Offset(0, 20, redRect, "SW"), new Offset(0, 48, redRect, "SE"), "greenRect", null, GreenRectPr);
		greenTxt = lang.newText(new Offset(5,5, greenRect, "NW"), GREEN, "greenTxt", null, ColorTxtPr);
		greenValueRect = lang.newRect(new Offset(0,0, greenRect, "NE"), new Offset(45,0, greenRect, "SE"), "greenValueRect", null, ValueRectPr);
		greenValueTxt = lang.newText(new Offset(-10,4, greenValueRect, "N"), String.valueOf((int)green), "greenValueTxt", null, ValueTxtPr);
		vars.set("green", Integer.toString(green));
					// GREEN-Pfeil
		Node[] greenAr = new Node[7];
		Point centerGreen = lang.newPoint(new Offset(0,0, greenRect, "C"), "centerGreen", null, pPr);
		centerGreen.hide();
		greenAr[0] = new Offset(-100, -5, centerGreen, "C");
		greenAr[1] = new Offset(-80, -5, centerGreen, "C");
		greenAr[2] = new Offset(-80, -10, centerGreen, "C");
		greenAr[3] = new Offset(-60, 0, centerGreen, "C");
		greenAr[4] = new Offset(-80, 10, centerGreen, "C");
		greenAr[5] = new Offset(-80, 5, centerGreen, "C");
		greenAr[6] = new Offset(-100, 5, centerGreen, "C");
		PolygonProperties GreenArrowPr = new PolygonProperties();
		GreenArrowPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		GreenArrowPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(34,177,76));
		Polygon greenArrow;
		try {
			greenArrow = lang.newPolygon(greenAr, "greenArrow", null, GreenArrowPr);
		} catch (NotEnoughNodesException e) {
			e.printStackTrace();
		}		lang.nextStep("3.2. Ermittlung des green-Werts");
		
				//BLUE
		sc.toggleHighlight(4,5);
		BlueRectPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		BlueRectPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(98,98,255));
		blueRect = lang.newRect(new Offset(0, 20, greenRect, "SW"), new Offset(0, 48, greenRect, "SE"), "BlueRect", null, BlueRectPr);
		blueTxt = lang.newText(new Offset(5,5, blueRect, "NW"), BLUE, "blueTxt", null, ColorTxtPr);
		blueValueRect = lang.newRect(new Offset(0,0, blueRect, "NE"), new Offset(45,0, blueRect, "SE"), "blueValueRect", null, ValueRectPr);
		blueValueTxt = lang.newText(new Offset(-10,4, blueValueRect, "N"), String.valueOf((int)blue), "blueValueTxt", null, ValueTxtPr);
		vars.set("blue", Integer.toString(blue));
					// BLUE-Pfeil
		Node[] blueAr = new Node[7];
		Point centerBlue = lang.newPoint(new Offset(0,0, blueRect, "C"), "centerBlue", null, pPr);
		centerBlue.hide();
		blueAr[0] = new Offset(-100, -5, centerBlue, "C");
		blueAr[1] = new Offset(-80, -5, centerBlue, "C");
		blueAr[2] = new Offset(-80, -10, centerBlue, "C");
		blueAr[3] = new Offset(-60, 0, centerBlue, "C");
		blueAr[4] = new Offset(-80, 10, centerBlue, "C");
		blueAr[5] = new Offset(-80, 5, centerBlue, "C");
		blueAr[6] = new Offset(-100, 5, centerBlue, "C");
		PolygonProperties BlueArrowPr = new PolygonProperties();
		BlueArrowPr.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		BlueArrowPr.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(98,98,255));
		Polygon blueArrow;
		try {
			blueArrow = lang.newPolygon(blueAr, "blueArrow", null, BlueArrowPr);
		} catch (NotEnoughNodesException e) {
			e.printStackTrace();
		}		lang.nextStep("3.3. Ermittlung des blue-Werts");
				// RGB-Kasten zeigen
		sc.toggleHighlight(5,7);
		rgbRect.show();
		inputRect.show();
		inputTxt.show();
		lang.nextStep("4. Ausgabe der RGB-Werte");
		
		// Frage 2
		double rand2 = Math.random()*100;
		if (rand2 < questProbability) {
			MultipleChoiceQuestionModel quest2 = new MultipleChoiceQuestionModel("quest2");
			quest2.setPrompt("Ändert sich die Farbe durch die Umwandlung des Farbraums?");
			quest2.addAnswer(new AnswerModel("an1", "JA", 5, "FALSCH, genaueres auf der nächsten Seite"));
			quest2.addAnswer(new AnswerModel("an2", "NEIN", 5, "DAS WAR EINE MEISTERHAFTE ANTWORT GUTE WAHL :D"));
			lang.addMCQuestion(quest2); 
			lang.nextStep();
		}
				
		
		/********************************************* FAZIT ************************************************************************************/
		lang.hideAllPrimitives();
		int[] rgb = {red, green, blue};
		float[] cmyk = {(float) cyan, (float) magenta, (float) yellow, (float) key};

		fazit(rgb, cmyk);	

	    lang.finalizeGeneration();    
        return lang.toString();
    }

    public String getName() {
        return "CMYK-to-RGB";
    }

    public String getAlgorithmName() {
        return "CMYK-to-RGB";
    }

    public String getAnimationAuthor() {
        return "Samil Kurt";
    }


    public String getDescription(){
        return "Der CMYK-Farbraum besteht aus den Grundfarben Cyan, Magenta, Yellow und Key. Diese"
 +"\n"
 +"können die Werte zwischen 0 und 1 annehmen. "
 +"\n"
 +"Der RGB-Farbraum hingegen besteht aus den Grundfarben Rot, Grün und Blau. Dabei kann jedes dieser "
 +"\n"
 +"Farben einen Wert von 0-255 annehmen. "
 +"\n"
 +"Da sich sowohl die Anazhl der Grundfarben in dem jeweiligen Farbraumsystem, als auch die Werte"
 +"\n"
 +"die, die Farben annehmen können unterscheiden, ist eine korrekte Umwandlung der CMYK- in die"
 +"\n"
 +"RGB-Werte notwendig. "
 +"\n"
 +"Bei der vorliegenden Algorithmenanimation wird die Umwandlung von CMYK- in RGB-Farbraum "
 +"\n"
 +"veranschaulicht.";
    }

    public String getCodeExample(){
        return "public double[ ] algorithm(double cyan, double magenta, double yellow, double key) {"
 +"\n"
 +"         double diffKey = 1 -key;"
 +"\n"
 +"\n"
 +"         int red = (int) (Math.round((255 * (1 - cyan) * diffKey)));"
 +"\n"
 +"         int green = (int) (Math.round((255 * (1 -magenta) * diffKey)));"
 +"\n"
 +"         int blue = (int) (Math.round((255 * (1 - yellow) * diffKey)));"
 +"\n"
 +"\n"
 +"         return new int[ ] {red, green, blue};"
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