package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.counter.model.TwoValueCounter;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CounterProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class Karatsuba implements Generator {
	/**
	 * The concrete language object used for creating output
	 */
	private Language lang;
	
//	private static final String DESCRIPTION = "";

	private static final String SOURCE_CODE = "1  public static BigInteger karatsuba (BigInteger x, BigInteger y" +
			"\n2  {" +
			"\n3  \t// Verwende Standard Multiplikation bei kleinen Eingaben" +
			"\n4  \tint n = Math.max (x.bitLength(), y.bitLength());" +
			"\n5  \tif (n <= 8) return x.multiply (y);" +
			"\n6  " +
			"\n7  \t// teile; x = xH * b^n + xL ,   y = yH * b^n + yL" +
			"\n8  \tn = n / 2;" +
			"\n9  \tBigInteger xH = x.shiftRight (n);" +
			"\n10 \tBigInteger xL = x.subtract   (xH.shiftLeft(n));" +
			"\n11 \tBigInteger yH = y.shiftRight (n);" +
			"\n12 \tBigInteger yL = y.subtract   (yH.shiftLeft(n));" +
			"\n13 " +
			"\n14 \t// berechne die Teilresultate" +
			"\n15 \tBigInteger p1 = karatsuba (xH, yH);" +
			"\n16 \tBigInteger p2 = karatsuba (xL, yL);" +
			"\n17 \tBigInteger p3 = karatsuba (xL.add (xH), yL.add (yH));" +
			"\n18" +
			"\n19 \t// Setze die Teile zum Gesamtergebnis zusammen" +
			"\n20 \treturn p1.shiftLeft (2*n).add (p3.subtract (p1).subtract (p2).shiftLeft (n)).add (p2);" +
			"\n21 }";
	
	private SourceCode src ;
	private SourceCodeProperties srcP;
	
	private StringMatrix table;
	private MatrixProperties tableP;
	
	private Rect rect_XxY,rect_p1,rect_p2,rect_p3,rect_X,rect_Y,rect_XH,rect_XL,rect_YH,rect_YL,rect_titel,rect_result;
	private RectProperties rectP;
	
	private Text text_XxY,text_p1,text_p2,text_p3,text_X,text_Y,text_XH,text_XL,text_YH,text_YL,text_titel,text_result;
	private TextProperties textP;
	private TextProperties titleP;
	private String [][] stringMtrix;
	
	private int step;
	private int box_length = 95;
	private int box_width = 40;
	private int table_high = 30 ;
	private int max_bit_lenght = 8;
	
	private String x;
	private String y;

    private BigInteger bigX;
    private BigInteger bigY;
    
	
	private TwoValueCounter counter ;
	private CounterProperties cp ;

    public void init(){
    	lang.setStepMode(true);
    }
    
    public Karatsuba() {
        lang = new AnimalScript("Graph[DE]", "Thanh Tung Do & Hoang Minh Duc", 800, 600);
    }

  
    
 
    public Karatsuba(Language l) {
		// Store the language object
		lang = l;
		// This initializes the step mode. Each pair of subsequent steps has to
		// be divdided by a call of lang.nextStep();
		lang.setStepMode(true);
    }
	
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	// get user define properties
    	srcP = (SourceCodeProperties)props.getPropertiesByName("srcP");
    	rectP = (RectProperties)props.getPropertiesByName("rectP");
		tableP = (MatrixProperties)props.getPropertiesByName("tableP");
        titleP = (TextProperties)props.getPropertiesByName("titleP");		 
        textP = (TextProperties)props.getPropertiesByName("textP");
        
//        get user  define primitives
        x = (String)primitives.get("x");
        y = (String)primitives.get("y");
        
        // convert String to BigInteger
        if(isNummeric(x) && isNummeric(y)){
        	bigX = new BigInteger(x);
        	bigY = new BigInteger(y);

		
        	text_titel = lang.newText(new Coordinates(650, 20), 
        			"Karatsuba", "titel", null, titleP);
        	text_titel.show();
		
		
        	rect_titel = lang.newRect(new Coordinates(640, 10), 
        			new Coordinates(755, 50), "titel_box", null, rectP);
		
        	rect_titel.show();
		

		
        	src = lang.newSourceCode(new Coordinates(1200, 400), 
        			"Karatsuba",null, srcP);
        	src.addCodeLine("1  public static BigInteger karatsuba (BigInteger x, BigInteger y)", null, 0, null);
        	src.addCodeLine("2  {", null, 0, null);
        	src.addCodeLine("3      // Verwende Standard Multiplikation bei kleinen Eingaben", null, 0, null);
        	src.addCodeLine("4      int n = Math.max (x.bitLength(), y.bitLength());", null, 0, null);
        	src.addCodeLine("5      if (n <= 8) return x.multiply (y);", null, 0, null);
        	src.addCodeLine("6  ", null, 0, null);
        	src.addCodeLine("7      // teile; x = xH * b^n + xL ,   y = yH * b^n + yL", null, 0, null);
        	src.addCodeLine("8      n = n / 2;", null, 0, null);
        	src.addCodeLine("9      BigInteger xH = x.shiftRight (n);", null, 0, null);
        	src.addCodeLine("10     BigInteger xL = x.subtract   (xH.shiftLeft(n));", null, 0, null);
        	src.addCodeLine("11     BigInteger yH = y.shiftRight (n);", null, 0, null);
        	src.addCodeLine("12     BigInteger yL = y.subtract   (yH.shiftLeft(n));", null, 0, null);
        	src.addCodeLine("13 ", null, 0, null);
        	src.addCodeLine("14     // berechne die Teilresultate", null, 0, null);
        	src.addCodeLine("15     BigInteger p1 = karatsuba (xH, yH);", null, 0, null);
        	src.addCodeLine("16     BigInteger p2 = karatsuba (xL, yL);", null, 0, null);
        	src.addCodeLine("17     BigInteger p3 = karatsuba (xL.add (xH), yL.add (yH));", null, 0, null);
        	src.addCodeLine("18 ", null, 0, null);
        	src.addCodeLine("19     // Setze die Teile zum Gesamtergebnis zusammen", null, 0, null);
        	src.addCodeLine("20     return p1.shiftLeft (2*n).add (p3.subtract (p1).subtract (p2).shiftLeft (n)).add (p2);", null, 0, null);
        	src.addCodeLine("21 }", null, 0, null);
		
        	lang.nextStep();

        	mult(bigX, bigY);
        	lang.nextStep();
        }
        else{

    		text_titel = lang.newText(new Coordinates(50, 20), "Karatsuba", "titel", null, titleP);
    		text_titel.show();
    		src = lang.newSourceCode(new Coordinates(50, 60), "Karatsuba",
    				null, srcP);
    		src.addCodeLine("Error ....", null, 0, null);
    		if(!isNummeric(x)) 
    			src.addCodeLine("Value X = " + x + " is not digit ", null, 0, null);
    		else 
    			src.addCodeLine("Value Y = " + y + " is not digit ", null, 0, null);
    		src.show();
    		lang.nextStep();
        }
        
        
        return lang.toString();

	}

private void mult(BigInteger x , BigInteger y) {
		BigInteger result = new BigInteger("0");
	
		src.show();
		
		step = 0;
		
		
		text_XxY = lang.newText(new Coordinates(220, 160), "", "XxY", null, textP);
		text_XxY.show();
		rect_XxY = lang.newRect(new Coordinates(210, 150), new Coordinates(210 + 2 * box_length + 50, 150 + box_width), "XxY_box", null, rectP);
		rect_XxY.show();
		
		Coordinates[]  theNodes = new Coordinates[2];
		
		theNodes[0] = new Coordinates(240 + box_length, 150 + box_width);	
		theNodes[1] = new Coordinates(5 + box_length, 250);
		
		Polyline line_XxYtop1 = lang.newPolyline(theNodes, "XxYtop1", null);
		line_XxYtop1.show();
		
		theNodes[1] = new Coordinates(205 + box_length, 250);
		Polyline line_XxYtop2 = lang.newPolyline(theNodes, "XxYtop2", null);
		line_XxYtop2.show();
		
		theNodes[1] = new Coordinates(405 + box_length, 250);
		Polyline line_XxYtop3 = lang.newPolyline(theNodes, "XxYtop3", null);
		line_XxYtop3.show();
		
		
		
		text_p1 = lang.newText(new Coordinates(15, 260), "", "p1", null, textP);
		text_p1.show();
		rect_p1 = lang.newRect(new Coordinates(5, 250), new Coordinates(5 + 2 * box_length, 250 + box_width), "XHxYH_box", null, rectP);
		rect_p1.show();
		
		text_p2 = lang.newText(new Coordinates(215, 260), "", "p2", null, textP);
		text_p2.show();
		rect_p2 = lang.newRect(new Coordinates(205, 250), new Coordinates(205 + 2 * box_length, 250 + box_width), "XHxYH_box", null, rectP);
		rect_p2.show();
		
		text_p3 = lang.newText(new Coordinates(415, 260), "", "p3", null, textP);
		text_p3.show();
		rect_p3 = lang.newRect(new Coordinates(405, 250), new Coordinates(405 + 2 * box_length, 250 + box_width), "XHxYH_box", null, rectP);
		rect_p3.show();
		
		text_X = lang.newText(new Coordinates(665, 160), "", "X", null, textP);
		text_X.show();
		rect_X = lang.newRect(new Coordinates(660, 150), new Coordinates(660 +  box_length, 150 + box_width), "XHxYH_box", null, rectP);
		rect_X.show();
		
		theNodes[0] = new Coordinates(660 + box_length/2, 150 + box_width);	
		theNodes[1] = new Coordinates(610 + box_length/2, 250);
		
		Polyline line_XtoXH = lang.newPolyline(theNodes, "XtoXH", null);
		line_XtoXH.show();
		
		theNodes[1] = new Coordinates(710 + box_length/2, 250);
		Polyline line_XtoXL = lang.newPolyline(theNodes, "XtoXL", null);
		line_XtoXL.show();
		
		text_XH = lang.newText(new Coordinates(615, 260), "", "XH", null, textP);
		text_XH.show();
		rect_XH = lang.newRect(new Coordinates(610, 250), new Coordinates(610 +  box_length, 250 + box_width), "XHxYH_box", null, rectP);
		rect_XH.show();
		
		text_XL = lang.newText(new Coordinates(715, 260), "", "XL", null, textP);
		text_XL.show();
		rect_XL = lang.newRect(new Coordinates(710, 250), new Coordinates(710 +  box_length, 250 + box_width), "XHxYH_box", null, rectP);
		rect_XL.show();
		
		text_Y = lang.newText(new Coordinates(865, 160), "", "XHxYH", null, textP);
		text_Y.show();
		rect_Y = lang.newRect(new Coordinates(860, 150), new Coordinates(860 +  box_length, 150 + box_width), "XHxYH_box", null, rectP);
		rect_Y.show();
		
		theNodes[0] = new Coordinates(860 + box_length/2, 150 + box_width);	
		theNodes[1] = new Coordinates(810 + box_length/2, 250);
		
		Polyline line_YtoYH = lang.newPolyline(theNodes, "YtoYH", null);
		line_YtoYH.show();
		
		theNodes[1] = new Coordinates(910 + box_length/2, 250);
		Polyline line_YtoYL = lang.newPolyline(theNodes, "YtoYL", null);
		line_YtoYL.show();
		
		text_YH = lang.newText(new Coordinates(815, 260), "", "XHxYH", null, textP);
		text_YH.show();
		rect_YH = lang.newRect(new Coordinates(810, 250), new Coordinates(810 +  box_length, 250 + box_width), "XHxYH_box", null, rectP);
		rect_YH.show();
		
		text_YL = lang.newText(new Coordinates(915, 260), "", "XHxYH", null, textP);
		text_YL.show();
		rect_YL = lang.newRect(new Coordinates(910, 250), new Coordinates(910 +  box_length, 250 + box_width), "XHxYH_box", null, rectP);
		rect_YL.show();
		
		
		stringMtrix = new String[table_high][12];
		for (int i = 0; i < table_high; i++) {
			for (int j = 0; j < 12; j++) {
				
				stringMtrix[i][j]= "";
			}
			
		}
		
		stringMtrix[0][0] = "     Step   ";
		stringMtrix[0][1] = "     Deep   ";
		stringMtrix[0][2] = "        x        ";
		stringMtrix[0][3] = "        y        ";
		stringMtrix[0][4] = "   xH       ";
		stringMtrix[0][5] = "   xL       ";
		stringMtrix[0][6] = "   yH       ";
		stringMtrix[0][7] = "   yL       ";
		stringMtrix[0][8] = "   p1       ";
		stringMtrix[0][9] = "   p2       ";
		stringMtrix[0][10] = "   p3      ";
		stringMtrix[0][11] = "     result                       ";

		table = lang.newStringMatrix(new Coordinates(5, 330), stringMtrix, "", null, tableP);
		
		counter = lang.newCounter(table); // Zaehler anlegen
		cp = new CounterProperties(); // Zaehler-Properties anlegen
		cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // gefuellt...
		cp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLUE); // ...mit Blau
		
		// view anlegen, Parameter:
		// 1. Counter
		// 2. linke obere Ecke;
		// 3. CounterProperties;
		// 4. Anzeige Zaehlerwert als Zahl?
		// 5. Anzeige Zaehlerwert als Balken?
		// Alternativ: nur Angabe Counter, Koordinate und Properties
		lang.newCounterView(counter,
				new Coordinates(1200, 200), cp, true, true);
		
		lang.nextStep();
		
		result=karatsuba(x, y,0);
				
		StringBuffer temp = new StringBuffer();
		temp.append("Result = ");
		temp.append(result.toString());
		text_result = lang.newText(new Coordinates(1250, 120), temp.toString(), "result", null, titleP);
		rect_result = lang.newRect(new Coordinates(1140, 110), new Coordinates(1655, 150), "titel_box", null, rectP);
		text_result.show();
		rect_result.show();

	}
	/**
	 *
	 */
	private BigInteger karatsuba (BigInteger x, BigInteger y, int deep)
	{	
		counter.accessInc(1);

			BigInteger result = new BigInteger("0");
			step++;
			int localstep = step ;
			int deep2 = deep;
      int localdeep = ++deep2;
			
			text_XxY.setText("", null, null);
			text_X.setText("", null, null);
			text_Y.setText("", null, null);
			text_XH.setText("", null, null);
			text_XL.setText("", null, null);
			text_YH.setText("", null, null);
			text_YL.setText("", null, null);
			text_p1.setText("", null, null);
			text_p2.setText("", null, null);
			text_p3.setText("", null, null);
			
			lang.nextStep();
			
			table.highlightCellColumnRange(localstep, 0, 11, null, null);
			StringBuffer temp = new StringBuffer();
			temp.append(x.toString());
			temp.append(" x ");
			temp.append(y.toString());
			
			text_XxY.setText(temp.toString(), null, null);
			text_X.setText(x.toString(), null, null);
			text_Y.setText(y.toString(), null, null);
			table.put(localstep, 0, String.valueOf(localstep), null, null);
			table.put(localstep, 1, String.valueOf(deep2), null, null);
			table.put(localstep, 2, x.toString(), null, null);
			table.put(localstep, 3, y.toString(), null, null);
			
			
			
			src.highlight(0);
			
			lang.nextStep();

			src.unhighlight(0);
			src.highlight(3);
			
			lang.nextStep();
			
			src.unhighlight(3);
			src.highlight(4);
			
			lang.nextStep();
	        // Verwende Standard Multiplikation bei kleinen Eingaben
	        int n = Math.max (x.bitLength(), y.bitLength());
	        if (n <= max_bit_lenght) {
	        	result = x.multiply (y);
	        	src.unhighlight(4);
	        	text_XxY.setText(result.toString(), null, null);
	        	table.put(localstep, 11,result.toString() , null, null);
	        	lang.nextStep();
				table.unhighlightCellColumnRange(localstep, 0, 11, null, null);
	        	return result;
	        }
	 
	        // teile; x = xH * b^n + xL ,   y = yH * b^n + yL
	        n = n / 2;
	        src.unhighlight(4);
			src.highlight(7);
			
			lang.nextStep();
	        
	        BigInteger xH = x.shiftRight (n);
	        
	        src.unhighlight(7);
			src.highlight(8);
			text_XH.setText(xH.toString(), null, null);
			table.put(localstep, 4, xH.toString(), null, null);			
			
			lang.nextStep();
			
	        BigInteger xL = x.subtract   (xH.shiftLeft(n));
	        
	        src.unhighlight(8);
			src.highlight(9);
			text_XL.setText(xL.toString(), null, null);
			table.put(localstep, 5, xL.toString(), null, null);			
			
			lang.nextStep();
			
	        BigInteger yH = y.shiftRight (n);
	        
	        src.unhighlight(9);
			src.highlight(10);
			text_YH.setText(yH.toString(), null, null);
			table.put(localstep, 6, yH.toString(), null, null);			
			
			lang.nextStep();
			
	        BigInteger yL = y.subtract   (yH.shiftLeft(n));
	        
	        src.unhighlight(10);
			src.highlight(11);
			text_YL.setText(yL.toString(), null, null);
			table.put(localstep, 7, yL.toString(), null, null);			
			
			lang.nextStep();
			
			temp.delete(0, temp.length());
			temp.append("p1=");
			temp.append(xH.toString());
			temp.append(" x ");
			temp.append(yH.toString());
			text_p1.setText(temp.toString(), null, null);
			lang.nextStep();
			
			temp.delete(0, temp.length());
			temp.append("p2=");
			temp.append(xL.toString());
			temp.append(" x ");
			temp.append(yL.toString());
			text_p2.setText(temp.toString(), null, null);
			lang.nextStep();
			
			temp.delete(0, temp.length());
			temp.append("p3=");
			temp.append(xL.add (xH).toString());
			temp.append(" x ");
			temp.append(yL.add (yH).toString());
			text_p3.setText(temp.toString(), null, null);
			lang.nextStep();
	 
	        // berechne die Teilresultate
			table.unhighlightCellColumnRange(localstep, 0, 11, null, null);
			src.unhighlight(11);
			src.highlight(14);
			lang.nextStep();
			
			src.unhighlight(14);
	        BigInteger p1 = karatsuba (xH, yH,localdeep);
	        
			src.highlight(14);

			temp.delete(0, temp.length());
			temp.append(x.toString());
			temp.append(" x ");
			temp.append(y.toString());
			
			text_XxY.setText(temp.toString(), null, null);
			text_X.setText(x.toString(), null, null);
			text_Y.setText(y.toString(), null, null);
			text_XH.setText(xH.toString(), null, null);
			text_XL.setText(xL.toString(), null, null);
			text_YH.setText(yH.toString(), null, null);
			text_YL.setText(yL.toString(), null, null);
			
			temp.delete(0, temp.length());
			temp.append("p1=");
			temp.append(p1.toString());
			text_p1.setText(temp.toString(), null, null);
			
			temp.delete(0, temp.length());
			temp.append("p2=");
			temp.append(xL.toString());
			temp.append(" x ");
			temp.append(yL.toString());
			text_p2.setText(temp.toString(), null, null);
			
			temp.delete(0, temp.length());
			temp.append("p3=");
			temp.append(xL.add (xH).toString());
			temp.append(" x ");
			temp.append(yL.add (yH).toString());
			text_p3.setText(temp.toString(), null, null);

			table.highlightCellColumnRange(localstep, 0, 11, null, null);
			table.put(localstep, 8, p1.toString(), null, null);	
			lang.nextStep();
			
			table.unhighlightCellColumnRange(localstep, 0, 11, null, null);
			src.unhighlight(14);
			src.highlight(15);
			lang.nextStep();
			
			
			src.unhighlight(15);
	        BigInteger p2 = karatsuba (xL, yL,localdeep);
			src.highlight(15);
			
			temp.delete(0, temp.length());
			temp.append(x.toString());
			temp.append(" x ");
			temp.append(y.toString());
			
			text_XxY.setText(temp.toString(), null, null);
			text_X.setText(x.toString(), null, null);
			text_Y.setText(y.toString(), null, null);
			text_XH.setText(xH.toString(), null, null);
			text_XL.setText(xL.toString(), null, null);
			text_YH.setText(yH.toString(), null, null);
			text_YL.setText(yL.toString(), null, null);
			
			temp.delete(0, temp.length());
			temp.append("p1=");
			temp.append(p1.toString());
			text_p1.setText(temp.toString(), null, null);
			
			temp.delete(0, temp.length());
			temp.append("p2=");
			temp.append(p2.toString());
			text_p2.setText(temp.toString(), null, null);
			
			temp.delete(0, temp.length());
			temp.append("p3=");
			temp.append(xL.add (xH).toString());
			temp.append(" x ");
			temp.append(yL.add (yH).toString());
			text_p3.setText(temp.toString(), null, null);

	        
			table.highlightCellColumnRange(localstep, 0, 11, null, null);
			table.put(localstep, 9, p2.toString(), null, null);	
			lang.nextStep();
			
			table.unhighlightCellColumnRange(localstep, 0, 11, null, null);
			src.unhighlight(15);
			src.highlight(16);
			lang.nextStep();
			
			src.unhighlight(16);
	        BigInteger p3 = karatsuba (xL.add (xH), yL.add (yH),localdeep);
	        src.highlight(16);
	        
	        temp.delete(0, temp.length());
			temp.append(x.toString());
			temp.append(" x ");
			temp.append(y.toString());
			
			text_XxY.setText(temp.toString(), null, null);
			text_X.setText(x.toString(), null, null);
			text_Y.setText(y.toString(), null, null);
			text_XH.setText(xH.toString(), null, null);
			text_XL.setText(xL.toString(), null, null);
			text_YH.setText(yH.toString(), null, null);
			text_YL.setText(yL.toString(), null, null);
			
			temp.delete(0, temp.length());
			temp.append("p1=");
			temp.append(p1.toString());
			text_p1.setText(temp.toString(), null, null);
			
			temp.delete(0, temp.length());
			temp.append("p2=");
			temp.append(p2.toString());
			text_p2.setText(temp.toString(), null, null);
			
			temp.delete(0, temp.length());
			temp.append("p3=");
			temp.append(p3.toString());
			text_p3.setText(temp.toString(), null, null);
	        
			table.highlightCellColumnRange(localstep, 0, 11, null, null);
			table.put(localstep, 10, p3.toString(), null, null);	
			lang.nextStep();
	 
	        // Setze die Teile zum Gesamtergebnis zusammen
			src.unhighlight(16);
			src.highlight(19);
	        result= p1.shiftLeft (2*n).add (p3.subtract (p1).subtract (p2).shiftLeft (n)).add (p2);
	        table.put(localstep, 11, result.toString(), null, null);	
			lang.nextStep();
			
			table.unhighlightCellColumnRange(localstep, 0, 11, null, null);
			src.unhighlight(19);
			lang.nextStep();
	        
	        return result;
	}
	
	private boolean isNummeric(String str){
		for(char c : str.toCharArray()){
			if(!Character.isDigit(c)) return false  ;
		}

		return true;
	}

    public String getName() {
        return "Karatsuba[EN]";
    }

    public String getAlgorithmName() {
        return "Karatsuba";
    }

    public String getAnimationAuthor() {
        return "Hoang Minh Duc, Do Thanh Tung";
    }

    public String getDescription(){
        return "The Karatsuba algorithm is a fast multiplication algorithm. It was invented by Anatolii Alexeevitch Karatsuba in 1960 and published in 1962.[1][2][3] It reduces the multiplication of two n-digit numbers to at most 3 n hoch {log3 of 2 }aquivalent 3 n hoch {1.585} single-digit multiplications in general (and exactly n hoch {log 3 of 2 } when n is a power of 2). It is therefore faster than the classical algorithm, which requires n2 single-digit products. If n = 210 = 1024, in particular, the exact counts are 310 = 59,049 and (210)2 = 1,048,576, respectively. The Toom–Cook algorithm is a faster generalization of this algorithm. For sufficiently large n, the Schönhage–Strassen algorithm is even faster.";
    }

    public String getCodeExample(){
        return SOURCE_CODE;
        }

    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public Locale getContentLocale() {
        return Locale.US;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}