/*
 * ValueDifferenceMetric.java
 * Ilhan Simsiki, Christof Bienkowski, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;
import java.util.Set;

import algoanim.primitives.Graph;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Node;
import algoanim.util.Timing;
import animal.main.Animal;

public class ValueDifferenceMetric implements ValidatingGenerator  {

    private int posy = 80;
	private int posx = 40;
	private int posx2= 450;
	private String [][] finalMatrix;
    private String[][] matrixRow = {{"Attribute","Klassfikation"}};
    private String[][] matrixRowEN = {{"Attributes","Classfikation"}};
	private List<Primitive> notHideTitle;
    
    public Graph graph;
    public Rect r0, r1, r2, r3, r4, r5, r6;
    public Text title0, title1, title2, title3, title4;
    public Text tc1, tc2, tc3, tc4, tc5, tc6, tg1, tg2, tg3, tz0, tz1, tz2, tz3, tz4;
    public Text tf1, tf2, tf3, tf4, tf5, tf6, tf7, tf8, tf9, tf10, tf11, tf12;
    public Polyline form1, form2, form3, form4, form5, calc1, calc2, calc3, xax, yax, average;
    
    public final static Timing  defaultDurationS = new MsTiming(0);
    public final static Timing  defaultDurationE = new MsTiming(1000);
    public final static Timing  defaultDurationEE = new MsTiming(1800);
    
    private Language lang;
	private Locale language;
	private Translator translator;
	private String ALGORITHM_NAME;
	public LinkedList<Text> textList = new LinkedList<Text>();
    public List<String> sumList = new LinkedList<String>(); 

    public ValueDifferenceMetric(Locale l) {
    	language = l;
		translator = new Translator("resources/ValueDifferenceMetric", language);
		ALGORITHM_NAME = translator.translateMessage("algorithm_name");	
    }
   
    public void init(){
        lang = new AnimalScript(ALGORITHM_NAME, "Christof Bienkowski, Ilhan Simsiki", 800, 600);
        lang.setStepMode(true);
    }
    
/**
  * This method will generate the VDM Animiation in Animal
  * 
  * @param props - containiert with all properties
  * @param primitves - hashtable of all primitives
  * 
  */
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	PolylineProperties pPropsPro = (PolylineProperties)props.getPropertiesByName("pPropsPro");
    	String[][] matrixPrim = (String[][])primitives.get("matrixPrim");
        MatrixProperties mPropsPro = (MatrixProperties)props.getPropertiesByName("mPropsPro");
        SourceCodeProperties scPropsPro = (SourceCodeProperties)props.getPropertiesByName("scPropsPro");
        GraphProperties gPropsPro = (GraphProperties)props.getPropertiesByName("gPropsPro");
        String attributPrim = (String)primitives.get("attributPrim");
       
        introduction();    
        start(matrixPrim, matrixPrim.length, attributPrim, scPropsPro, pPropsPro, mPropsPro, gPropsPro);        
        return lang.toString();
    }
    
/**
  * This method validate the the user input and throws appropriate Exceptions
  * 
  * @param props - containiert with all properties
  * @param primitves - hashtable of all primitives
  * 
  */
	public boolean validateInput(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		boolean validation = true;
		String attributPrim = (String)primitives.get("attributPrim");
		String[][] matrixPrim = (String[][])primitives.get("matrixPrim");
		
		for(int i=0; i<matrixPrim.length; i++) {
			if(matrixPrim[i][0].equals(attributPrim)) {
				validation = true;
			} else {
				if(!validation) {
					validation = false;													
					throw new IllegalArgumentException(this.translator.translateMessage("vaild1"));
				}
			}
		}
		
		if(matrixPrim[0].length > 2) {
			validation = false;
			throw new IllegalArgumentException(this.translator.translateMessage("vaild2"));
		}
		
		if(matrixPrim.length == 0) {
			validation = false;
			throw new IllegalArgumentException(this.translator.translateMessage("vaild3"));
		}
		
		for(int i=0; i<matrixPrim.length; i++) {
			if(!(matrixPrim[i][1].equals("no") || matrixPrim[i][1].equals("yes") || matrixPrim[i][1].equals("NO") || matrixPrim[i][1].equals("YES"))) {
				validation = false;
				throw new IllegalArgumentException(this.translator.translateMessage("vaild4"));
			}
		}
		
		for(int i=0; i<matrixPrim.length; i++) {
			
			int total = attributPrim.length() + matrixPrim[i][0].length();
	 		if(total > 16) {
	 			validation = false;
	 			throw new IllegalArgumentException(this.translator.translateMessage("vaild5"));
	 		}
	 	}
		
		return validation;
	}
    
    
/**
  * This method displays the first page of our Animation and includes the Algorithmname, Description 
  * and the Formula.
  * 
  */
  	public void introduction() {      	  
  		RectProperties rProps = new RectProperties();
  		rProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.black);
  		rProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.cyan);
  		rProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
  		r2 = lang.newRect(new Coordinates(10,0), new Coordinates(305,35), "sfd", null, rProps);
  	 
  		TextProperties tProps = new TextProperties();
  		tProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 20));
  		TextProperties tProps1 = new TextProperties();
  		tProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
  		textList.add(lang.newText(new Coordinates(20,10), this.translator.translateMessage("algorithm_name"), "algorithm_name", null, tProps));
  		textList.add(lang.newText(new Coordinates(20,50), this.translator.translateMessage("approach"), "approach", null, tProps1));
  		textList.add(lang.newText(new Coordinates(20,70), this.translator.translateMessage("description"), "description", null));
  		textList.add(lang.newText(new Coordinates(20,90), this.translator.translateMessage("description1"), "description1", null));
  		textList.add(lang.newText(new Coordinates(20,110), this.translator.translateMessage("description2"), "description2", null));
  		textList.add(lang.newText(new Coordinates(20,130), this.translator.translateMessage("description3"), "description3", null));
  		textList.add(lang.newText(new Coordinates(20,150), this.translator.translateMessage("description4"), "description4", null));
  		textList.add(lang.newText(new Coordinates(20,190), this.translator.translateMessage("description5"), "description5", null));
  		textList.add(lang.newText(new Coordinates(40,330), this.translator.translateMessage("description6"), "description6", null, tProps1));
  		textList.add(lang.newText(new Coordinates(40,350), this.translator.translateMessage("description7"), "description7", null, tProps1));
  		textList.add(lang.newText(new Coordinates(95,330), this.translator.translateMessage("description8"), "description8", null));
  		textList.add(lang.newText(new Coordinates(95,350), this.translator.translateMessage("description9"), "description9", null));

  		formula(20, 255, true); 
  		lang.nextStep();
    }
    
/**
  * This method displays the Forumla of our VDM Algorithm.
  * 
  * @param posx - relative position of x
  * @param posy - relative position of y
  * @param show - boolean if true hide content
  * 
  */
     public void formula(int posx, int posy, boolean show) {
       TextProperties tProps = new TextProperties();
       tProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 10));
       Text tf1 = lang.newText(new Coordinates(posx, posy), "vdm(x, y) = ", "tf1", null);
       tf1.setFont(new Font("SansSerif", Font.BOLD, 12), null, null);
       
  	   Node[] np01= {new Coordinates(posx+100,posy-5), new Coordinates(posx+85,posy-5),new Coordinates(posx+95,posy+5), new Coordinates(posx+85,posy+15), new Coordinates(posx+100,posy+15)}; 
  	   form1 = lang.newPolyline(np01, "form1", null);
  	   Node[] np02= {new Coordinates(posx+120,posy-15), new Coordinates(posx+120,posy+30)}; 
  	   form2 = lang.newPolyline(np02, "form2", null);
  	   Node[] np03= {new Coordinates(posx+130,posy+5), new Coordinates(posx+160,posy+5)}; 
	   form3 = lang.newPolyline(np03, "form3", null);
  	   Node[] np05= {new Coordinates(posx+188,posy+5), new Coordinates(posx+218,posy+5)}; 
  	   form4 = lang.newPolyline(np05, "form4", null);
  	   Node[] np06= {new Coordinates(posx+230,posy-15), new Coordinates(posx+230,posy+25)}; 
  	   form5 = lang.newPolyline(np06, "form5", null);
  	   
  	   tf2 = lang.newText(new Coordinates(posx+90, posy-20), "N", "tf2", null, tProps);
  	   tf3 = lang.newText(new Coordinates(posx+85, posy+20), "i=0", "tf3", null, tProps);
  	   tf5 =lang.newText(new Coordinates (posx+142,posy-13), "1,c", "tf5", null, tProps);
  	   tf7 =lang.newText(new Coordinates (posx+148,posy+12), "1", "tf7", null, tProps);
  	   tf8 = lang.newText(new Coordinates (posx+170, posy), "-", "tf8", null, tProps);
  	   tf10 =lang.newText(new Coordinates (posx+200,posy-12), "2,c", "tf10", null, tProps);
  	   tf12 =lang.newText(new Coordinates (posx+203,posy+12), "1", "tf12", null, tProps);
  	   
  	   tProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 16));
  	   tf4 = lang.newText(new Coordinates (posx+132, posy-15), "n", "tf4", null, tProps);
  	   tf6 = lang.newText(new Coordinates (posx+138, posy+10), "n", "tf6", null, tProps);
  	   tf9 = lang.newText(new Coordinates (posx+190, posy-15), "n", "tf9", null, tProps);
  	   tf11 = lang.newText(new Coordinates (posx+193, posy+10), "n", "tf11", null, tProps);

  	   if(show == true) {
  		   lang.nextStep();
  		   notHideTitle = new LinkedList<Primitive>();
  		   notHideTitle.add(textList.get(0));
  		   notHideTitle.add(r2);
  	  	   lang.hideAllPrimitivesExcept(notHideTitle);	  
  	   }
     }
         
/**
  * This method is a part of the Calculation of the VDM Algorithm. It creates the text objects and 
  * highlight the corresponding cell in the matrix.
  * 
  * @param posx - relative position of x
  * @param posy - relative position of y
  * @param matrix - String Matrix
  * @param size - size of StringMatrix
  * 
  */ 
    public void attstart(StringMatrix matrix,int x, int posy, int posymore, int k) {
    	x= x + 475;
    	if(k == 1) {
    	Node[] np12= {new Coordinates(posx+80+x,posy+5+posymore), new Coordinates(posx+110+x,posy+5+posymore)}; 
	 	calc2 = lang.newPolyline(np12, "p12", null);
	 	matrix.highlightCell(0, 1, null, null);
	 	matrix.highlightCell(1, 1, null, null);
	 	tc2 = lang.newText(new Coordinates (posx+92+x, posy-10+posymore), getA(matrix, 1, 1), "text12", null);
	 	lang.nextStep();
	 	
		matrix.highlightCell(2, 1, null, null);
	 	tc3 = lang.newText(new Coordinates (posx+80+x, posy+10+posymore), getA(matrix, 1, 1)+" + "+getA(matrix, 2, 1), "text13", null);
	 	lang.nextStep();
	 	matrix.unhighlightCell(0, 1, null, null);
	 	matrix.unhighlightCell(1, 1, null, null);
	 	matrix.unhighlightCell(2, 1, null, null);
    	} else {
        	Node[] np12= {new Coordinates(posx+80+x,posy+5+posymore), new Coordinates(posx+110+x,posy+5+posymore)}; 
    	 	calc2 = lang.newPolyline(np12, "p12", null);
    	 	matrix.highlightCell(0, 1, null, null);
    	 	matrix.highlightCell(2, 1, null, null);
    	 	tc2 = lang.newText(new Coordinates (posx+92+x, posy-10+posymore), getA(matrix, 2, 1), "text12", null);
    	 	lang.nextStep();

    		matrix.highlightCell(1, 1, null, null);
    	 	tc3 = lang.newText(new Coordinates (posx+80+x, posy+10+posymore), getA(matrix, 1, 1)+" + "+getA(matrix, 2, 1), "text13", null);
    	 	lang.nextStep();
    	 	matrix.unhighlightCell(0, 1, null, null);
    	 	matrix.unhighlightCell(1, 1, null, null);
    	 	matrix.unhighlightCell(2, 1, null, null);
    	}
    }

/**
  * This method is a part of the Calculation of the VDM Algorithm. It calculates the first part of the matrix
  * Part two and four of the Calculation are calculated in method "calculation".
  *
  * @param matrix - String Matrix
  * 
  * @return result - Double 
  */ 
    public double attresult1(StringMatrix matrix) {
    	double n1c = Integer.parseInt(getA(matrix,1,1));
		double n1 = Integer.parseInt(getA(matrix,1,1)) + Integer.parseInt(getA(matrix,2,1));
		double result = n1c/n1;
	
		return result;
    }
    
/**
  * This method is a part of the Calculation of the VDM Algorithm. It calculates the third part of the matrix
  * Part two and four of the Calculation are calculated in method "calculation".
  * 
  * @param matrix - relative position
  * 
  * @return result - Double 
  */ 
    public double attresult2(StringMatrix matrix) {
		double n1c = Integer.parseInt(getA(matrix,2,1));
		double n1 = Integer.parseInt(getA(matrix,1,1)) + Integer.parseInt(getA(matrix,2,1));
		double result = n1c/n1;
		
	 return result;
    }
    
/**
  * This method returns the absolute value of a number(double)
  * 
  * @param number - double number
  * 
  * @return a number of typ double
  * 
  */
    public  double betrag(double number) {
    	if(number < 0.0) {
    		return number*(-1);
    	}
    	return number;
    }
  
/**
  * This method is the main part of the Calculation of the VDM Algorithm. It combines and calculates
  * all parts together. It delivers a list of results
  * 
  * @param posx - relative position of x
  * @param posy - relative position of y
  * @param matrix - StringMatrix
  * @param size - size of StringMatrix
  * @param ma - 2D String matrix
  * @param vdmForAttribute - calculation for this Attribute
  * 
  * @return List<String> - list of results
  * 
  */
    public List<String> calculation(int posx, int posy, StringMatrix matrix, int size, String[][] ma, String vdmForAttribute) {

    	boolean b = true;
    	boolean a = false;
    	int posymore = 0;
    	int p = 0;
    	double resultA =0;
    	double resultB = 0;
    	double resultC = 0;
    	double result = 0;
    	double n1c = 0;
    	double n1 = 0;
    	
    	for(int j=0; j<size-1; j++) {  	
   	 		tc1 = lang.newText(new Coordinates (posx-65, posy+posymore), "vdm("+ vdmForAttribute + ", "+matrix.getElement(0, 2+j)+")"+" = ", "text13", null);
   	 		tc1.setFont(new Font("SansSerif", Font.BOLD, 12), null, null);
   	 		int x = 0;
   	 		int total =vdmForAttribute.length() + matrix.getElement(0, 2+j).length();
   	 		if(total > 8) {
   	 			x = 20;
   	 			if(total > 10)
   	 				x= 30;
   	 					if(total > 14)
   	 						x= 60;
   	 		} 
   	 		b= true;
   	 		for(int k= 1; k<=2; k++) { 
   	 			Node[] np11= {new Coordinates(posx+75+x,posy-5+posymore), new Coordinates(posx+75+x,posy+25+posymore)}; 
   	 			calc1 = lang.newPolyline(np11, "p11", null);
   	 			lang.nextStep();
   	 				if(a) {
   	 					attstart(matrix,x,posy,posymore, k);
   	 					resultA = attresult2(matrix);
   	 					a = false;
   	 				}
	   	 	
   	 				for(int i=1; i <= 2; i++) {
   	 					Node[] np12= {new Coordinates(posx+80+x,posy+5+posymore), new Coordinates(posx+110+x,posy+5+posymore)}; 
   	 					calc2 = lang.newPolyline(np12, "p12", null);
   	 		
   	 					if(b) {
   	 						attstart(matrix,x,posy,posymore, k);
   	 						resultA = attresult1(matrix);
   	 						b = false;
   	 					} else { 
   	 						if(k==2 & i ==1 & j >0 ) {
   	 				
   	 						} else {
   	 							if(j==0 &k==2&i==1) {
   	 						
   	 							}else {
   	 								matrix.highlightCell(0, i+p, null, null);
   	 								matrix.highlightCell(k, i+p, null, null);
   	 								tc2 = lang.newText(new Coordinates (posx+92+x, posy-10+posymore), getA(matrix, k, i+p), "text12", null);
   	 								lang.nextStep();
   	 								matrix.highlightCell(2, i+p, null, null);
   	 								matrix.highlightCell(1, i+p, null, null);
   	 								tc3 = lang.newText(new Coordinates (posx+80+x, posy+10+posymore), getA(matrix, 1, i+p)+" + "+getA(matrix, 2, i+p), "text13", null);
   	 								lang.nextStep();
   	 								matrix.unhighlightCell(0, i+p, null, null);
   	 								matrix.unhighlightCell(1, i+p, null, null);
   	 								matrix.unhighlightCell(2, i+p, null, null);

   	 								n1c = Integer.parseInt(getA(matrix,k,i+p));
   	 								n1 = Integer.parseInt(getA(matrix,1,i+p)) + Integer.parseInt(getA(matrix,2,i+p));
   	 								resultB = n1c/n1;
   	 								resultC = resultA - resultB;
   	 								resultC = betrag(resultC);
   	 							}
   	 						}	
   	 					}
   	 					if(i != 2) {
   	 						tc6 = lang.newText(new Coordinates (posx+123+x, posy+posymore), "-", "text14", null);
   	 						x=x+60;
   	 					} else {
   	 						Node[] np16= {new Coordinates(posx+116+x, posy-5+posymore), new Coordinates(posx+116+x,posy+25+posymore)}; 
   	 						calc3 = lang.newPolyline(np16, "p16", null);
   	 						lang.nextStep();
   	 					
   	 						if(k != 2) {
   	 							tc4 = lang.newText(new Coordinates (posx+123+x, posy+posymore), "+ ", "tc4", null);
   	 							result = result + resultC;
   	 							a = true;
   	 						} else {
   	 						    result = result + resultC;
   	 							tc5 = lang.newText(new Coordinates (posx+123+x, posy+posymore), "=", "tc5", null);
   	 							tc6 = lang.newText(new Coordinates (posx+123+x+20, posy+posymore), roundAndFormat(result, 2), "tc6", null);
   	 							sumList.add(roundAndFormat(result, 1));
   	 							p=p+1;
   	 							result= 0;
   	 						}	 			
   	 					}
   	 					lang.nextStep();
   	 				}
   	 			x=x+60;	
   	 		}
   	 	posymore = posymore + 60;
    	}
   	 return sumList;
   	}   
   
 
/**
 * This method round to n decimal places for formatted output
 * 
 * @param value - double value to be round
 * @param n - round after n decimals
 * 
 * @return String - rounded number
 * 
 */ 
    public String roundAndFormat(final double value, final int n) {
    	final java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
    	nf.setMaximumFractionDigits(n);
    	return nf.format(new BigDecimal(value));
      }

/**
 * This method compute the graph based on the classification from input table, frequency table and result.
 * Each node contains the result of a calculation for an attribute. It creates the coordinate system and the nodes.
 * 
 * @param matrix - 2D String
 * @param posx - relative position of x
 * @param posy - relative position of y
 * 
 */ 
    public void graph(String[][] matrix, int posx, int posy, PolylineProperties ppProps, GraphProperties gProps) {
   	   	PolylineProperties pProps = new PolylineProperties();
   	   	pProps.set(AnimationPropertiesKeys.BWARROW_PROPERTY, true);
   	   	Node[] np17 = {new Coordinates(posx+20,posy), new Coordinates(posx+20,posy+170)}; 
   	   	yax = lang.newPolyline(np17, "yax", null, pProps);
   	   	pProps.set(AnimationPropertiesKeys.BWARROW_PROPERTY, false);
   	   	pProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
   	   	Node[] np18 = {new Coordinates(posx+20,posy+170), new Coordinates(posx+250,posy+170)}; 
   	   	xax = lang.newPolyline(np18, "xax", null, pProps);
   	   	pProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, false);
   	   	Node[] np20 = {new Coordinates(posx+20,posy+170), new Coordinates(posx+230,posy+20)}; 
   	   	average = lang.newPolyline(np20, "average", null, ppProps);
   	
   	   	tg1 = lang.newText(new Coordinates (posx-5,posy-10), "yes", "tex20", null);
   	   	tg2 = lang.newText(new Coordinates (posx+250,posy+180), "no", "tex21", null);
   	   	tg3 = lang.newText(new Coordinates(posx+240,posy+10), "average", "tex22", null);
   	   	tg1.setFont(new Font("SansSerif", Font.BOLD, 12), null, null);
   	   	tg2.setFont(new Font("SansSerif", Font.BOLD, 12), null, null);
   	   	tg3.setFont(new Font("SansSerif", Font.BOLD, 12), null, null);
   	   	lang.nextStep();  	
   	   
   	   	int[][] a = { {0, 1}};
   	   	int x = 0;
   	   	int y= 0;
   	   	int x1 = 0;
   	   	int y1 = 0;
   
   	   	for(int i=2; i<=matrix[0].length-1;i++) {
   	   				if (Integer.parseInt(matrix[1][i]) > Integer.parseInt(matrix[2][i])) {
   	   					Node[] np19= {new Coordinates(posx+90+x,posy+40-y)}; 
   	   				    String [] test = {sumList.get(i-2)};
   	   					graph = lang.newGraph("graph", a, np19, test, null, gProps);
   	   					x=30+x;
   	   					y=30+y;
   	   				} else {
   	   					 if(Integer.parseInt(matrix[1][i]) == Integer.parseInt(matrix[2][i])) {
   	   						 String [] test = {sumList.get(i-2)};
   	   						 Node[] np19= {new Coordinates(posx+130,posy+80)};
   	   						 graph = lang.newGraph("graph", a, np19, test, null, gProps);
   	   					 } else {
   	   						 String [] test = {sumList.get(i-2)};
   	   						 Node[] np19= {new Coordinates(posx+170+x1,posy+100+y1)}; 
   	   						 graph = lang.newGraph("graph", a, np19, test, null, gProps);
   	   						x1=30+x1;
   	   						y1=30+y1;
   	   					 }
   	   				}
   	   			lang.nextStep();
   	   		}
     }

/**
 * This method removes duplicated entries, set attribute to the first position in the frequency
 * table as a String[][]
 * 
 * @param matrix - create frequency table from input Array
 * @param attribute - set attribute on first position on frequency table
 * 
 * @return String[][] matrix
 * 
 */ 
     public String[][] createFrequencyTable(String[][] matrix, String attribute) {
    	boolean a = true;
    	
   	  	Set<String> strings = new HashSet<String>();
   	  	for (int i = 1; i < matrix.length; i++) {
   	  		strings.add(matrix[i][0]);
   	  	}
   	  	
   	  	String[] m = strings.toArray(new String[0]);
   	  	String[][] matrix2 = new String[3][m.length+1];
   	  	
   	    removeElements(m, attribute);
   	    matrix2[0][1]=attribute;
  		for(int j=2;j<m.length+1;j++) {
  		matrix2[0][j] = m[j-2];
  		}
   	  	for(int i=1; i<m.length+1; i++) {
   	  		
   	  		matrix2[1][i] = maxCount(matrix, matrix2[0][i],"yes");
   	  		matrix2[2][i] = maxCount(matrix, matrix2[0][i], "no");
   	  		if(a) {
   	  			matrix2[0][0] = "";
   	  			matrix2[1][0] = "yes";
   	  			matrix2[2][0] = "no";
   	  			a = false;
   	  		}
   	  	}
   	  	a = true;
   	 
     	return matrix2;
     }
     
/**
  * This method removes the content deleteMe in input array
  * 
  * @param input - String array
  * @param deleteMe - String
  * 
  * @return String[] array
  * 
  */ 
     public static String[] removeElements(String[] input, String deleteMe) {
    	    List <String>result = new LinkedList<String>();

    	    for(String item : input)
    	        if(!deleteMe.equals(item))
    	            result.add(item);

    	    return result.toArray(input);
    	}
     
/**
  * This method get the value of column index and row index from the matrix 
  * 
  * @param matrix - String matrix
  * @param indexA - column index of matrix
  * @param indexB - row index of matrix
  * 
  * @return String 
  * 
  */
     public String getA(StringMatrix matrix, int indexA, int indexB) {
    	 return matrix.getElement(indexA, indexB);  
     }
   
/**
  * This method count the attribute based on status(yes or no). Used for frequency table
  * 
  * @param matrix - String matrix
  * @param att - attribute of string array
  * @param status - can be "yes" or "no" 
  * 
  * @return caounts - String
  */
     public String maxCount(String [][] matrix, String att, String status) {
    	 int count= 0;

  	   	for(int i = 1; i<matrix.length; i++) {
  	   		if(matrix[i][0].equals(att) && matrix[i][1].equals(status))
  	   			count = count + 1;	    
  	   	}
  	   	String counts = Integer.toString(count);  
  	   	return counts;   
    }
     
/**
  * This method set the Columns and Rows of the matrix to bold 
  * 
  * @param matrix - String matrix
  * 
  */     
  	 public void setRowAndColBold(StringMatrix matrix) {
  		 for(int i = 1; i< matrix.getNrCols(); i++) {
  			 matrix.setGridFont(0, i, new Font("SansSerif", Font.BOLD, 12), null, null);
  		 }
  		 matrix.setGridFont(1, 0, new Font("SansSerif", Font.BOLD, 12), null, null);
  		 matrix.setGridFont(2, 0, new Font("SansSerif", Font.BOLD, 12), null, null);
  	 }
  	  	
/**
  * This method create the second SourceCodeProperties and displays the SoruceCode and return the SoruceCode
  * 
  * @param size - integer for position of SoruceCode
  * 
  * @return sc - SoruceCode
  */	
  	public SourceCode displaySourceCode1(int size, SourceCodeProperties scProps) { 
  		RectProperties rProps = new RectProperties();
      	rProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.white);
      	rProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.lightGray); 
      	rProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      	r4 = lang.newRect(new Coordinates(10,((size*37)+posy)+25), new Coordinates(400,((size*37)+posy)+190), "r4", null, rProps);
        
  	    SourceCodeProperties scProps1 = new SourceCodeProperties();
        scProps1.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
        SourceCode sc1 = lang.newSourceCode(new Coordinates(20, (size*37)+posy+25), "sourceCode", null, scProps1);
        sc1.addCodeLine(this.translator.translateMessage("line1"), null, 0, null);
      
  		SourceCode sc = lang.newSourceCode(new Coordinates(20, 10+(size*37)+posy+25), "sourceCode", null, scProps);  
  		sc.addCodeLine("", null, 2, null);													
  		sc.addCodeLine(this.translator.translateMessage("line2"), null, 2, null);						
  		sc.addCodeLine(this.translator.translateMessage("line3"), null, 2, null); 						
  		sc.addCodeLine("", null, 1, null);													
  		sc.addCodeLine(this.translator.translateMessage("line4"), null, 2, null); 										
  		sc.addCodeLine(this.translator.translateMessage("line5"), null, 4, null); 								
  		sc.addCodeLine("", null, 1, null);														
  		sc.addCodeLine(this.translator.translateMessage("line6"), null, 2, null); 								
  		lang.nextStep();

  		return sc;   
  	}

/**
  * This method merge two 2d string matrix to one 2d string matrix.
  * 
  * @param matrix - input matrix
  * @param matrixRow - Row content
  * 
  * @return arr3 - 2D Matrix
  */	
  	public String[][] merge(String[][] matrix, String[][] matrixRow) {
  		boolean a = true;
 
	  	int m = matrixRow.length;
	  	int n = matrix.length;
	  	String[][] arr3 = new String[m+n][2];

	  	for(int j=0;j<2;j++) {
	  		for(int i=0;i< m+n;i++) {																																																																																																																																																																																																																																																																																																																																
	  			if(a){
	  				arr3[i][j] = matrixRow[i][j];
	  				a = false;  
         } else
        	 	arr3[i][j] = matrix[i-1][j];
	  		}
	  		a = true;
	  	}
	  	return arr3;
  	}
  	
/**
  * This method will draw a chessboard and the title of this Animation. 
  *         
  */
    public void conclusionTitle() {
      	RectProperties rProps = new RectProperties();
        rProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
        rProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    
        int x= 0;
        int y= 0;
        boolean a = true;
        
        for(int k = 0; k< 93; k++) {
        	y = 0;
               for(int i=0; i<5; i++) {
                     if(a) {
                    	 r0 = lang.newRect(new Coordinates(170+x,70+y), new Coordinates(180+x,80+y), "chessboard", null, rProps);
                    	 y = y + 20;
                    	 if(i == 4)
                    		 a = false;    
                     } else {
                    	 r0 = lang.newRect(new Coordinates(170+x,80+y), new Coordinates(180+x,90+y), "chessboard", null, rProps);
                         y = y + 20;
                         if(i == 4)
                     	 	a = true; 
                     }	 
               } 
               x = x + 10;
        }
        lang.nextStep();
    	rProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
        rProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        r1 = lang.newRect(new Coordinates(390,90), new Coordinates(900,150), "chesstitle", null, rProps);
        
        Text con1 = lang.newText(new Coordinates(430,120), this.translator.translateMessage("conclusion2"), "conclusion2", null);
    	con1.setFont(new Font("SansSerif", Font.BOLD, 32), defaultDurationS, defaultDurationE);
    	con1.changeColor(null, Color.MAGENTA, defaultDurationS, defaultDurationEE);
    }
    
/**
  * This method will run after the introduction method. It contains and displays the the Steps
  * 1: Input & SoruceCode, 2: Frequency Table, 3: Calculation, 4: Graph and the final Conclusion. 
  * 
  * @param matrix - input 2dim matrix 
  * @param size - size of matrix
  * @param vdmForAttribute
  *         
  */
    public void start(String[][] matrix, int size, String vdmForAttribute, SourceCodeProperties scProps, PolylineProperties pProps, MatrixProperties mProps, GraphProperties gProps) {
    	////////////////////////////////// Show: Input-Table
  	  	TextProperties tProps = new TextProperties();
  	  	tProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
  	    textList.add(lang.newText(new Coordinates(20,posy-20), this.translator.translateMessage("step1"), "step1", null, tProps));
  	  	lang.nextStep();
  	  	
  	  	if(language.getLanguage() == "en") {
  	  		finalMatrix = merge(matrix, matrixRowEN);
	  	} else {
	  		finalMatrix = merge(matrix, matrixRow);
	  	}
	  	
  	  	if(finalMatrix.length >= 8) {
	  		size = size - 2;
	  	}
  	 
  	  	StringMatrix im1  = lang.newStringMatrix(new Coordinates(60, posy+5), finalMatrix, "ma", null, mProps);
  	  	im1.setGridFont(0, 0, new Font("SansSerif", Font.BOLD, 12), null, null);
  	  	im1.setGridFont(0, 1, new Font("SansSerif", Font.BOLD, 12), null, null);
  	  	im1.setGridHighlightBorderColor(0, 0, Color.BLACK, null, null);
  	  	im1.setGridFillColor(0, 1, Color.YELLOW, null, null);
  	  	lang.nextStep();
  	
  	    SourceCode sc = displaySourceCode1(size, scProps);	
        lang.nextStep();
           
      	////////////////////////////////////////////// Häufigkeitstabelle - frequency table
  	    textList.add(lang.newText(new Coordinates(450,posy-20), this.translator.translateMessage("step2"), "step2", null, tProps));
      	lang.nextStep(this.translator.translateMessage("label1"));

     	sc.highlight(1, 0, false);
      	sc.highlight(2, 0, false);
      	lang.nextStep();
      	
      	StringMatrix frequencyMatrix  = lang.newStringMatrix(new Coordinates(posx2+40, posy+5), createFrequencyTable(finalMatrix, vdmForAttribute), "ma", null, mProps);
      	setRowAndColBold(frequencyMatrix);
      	lang.nextStep();
      	
      	sc.toggleHighlight(1, 0, false, 4, 0);
      	sc.toggleHighlight(2, 0, false, 5, 0);
      	lang.nextStep();
      	
      	////////////////////////////////////////////// Berechnung - Calculation
      	textList.add(lang.newText(new Coordinates(posx2,110+posy), this.translator.translateMessage("step3"), "step3", null, tProps));
      	lang.nextStep(this.translator.translateMessage("label2"));

      	formula(posx2,240,false);
      	lang.nextStep();

      	calculation(posx2+65,310,frequencyMatrix, createFrequencyTable(matrix, vdmForAttribute)[0].length-1, finalMatrix, vdmForAttribute);
      	lang.nextStep();
      	
      	sc.toggleHighlight(4,0, false, 6,0);
      	sc.toggleHighlight(5, 0, false, 7, 0);
      	lang.nextStep();

      	/////////////////////////////////////////////// Graph 
      	textList.add(lang.newText(new Coordinates(posx2+400,posy-20), this.translator.translateMessage("step4"), "step4", null, tProps));
      	lang.nextStep(this.translator.translateMessage("label3"));
      	
      	graph(createFrequencyTable(matrix, vdmForAttribute), posx2+400, 100, pProps, gProps);			 		
      	lang.nextStep();
  	
      	/////////////////////////////////////////////// Zusammenfassung
      	tProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 14));
      	Text con = lang.newText(new Coordinates(posx2,110+posy), this.translator.translateMessage("conclusion"), "conclusion", null, tProps);
      	con.changeColor(null, Color.MAGENTA, null, null);
      	con.moveBy("translate", -200, 0, defaultDurationS, defaultDurationE);
  	
      	lang.hideAllPrimitivesExcept(con);
    	conclusionTitle();
      	lang.nextStep(this.translator.translateMessage("label4"));
      	
      	formula(posx2, 255, false);
      	tProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
      	textList.add(lang.newText(new Coordinates(posx2-170,210), this.translator.translateMessage("conclusion1"), "conclusion1", null, tProps));
      	lang.nextStep();
      	
    	graph(createFrequencyTable(matrix, vdmForAttribute), posx2-200, 320, pProps, gProps);
    	lang.nextStep();
      	
    	tProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 12));
      	textList.add(lang.newText(new Coordinates(650,350), this.translator.translateMessage("conclusion3"), "conclusion3", null, tProps));
      	lang.nextStep();
      	
      	tProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 12));
      	int y=0;
      	for(int i=1; i<frequencyMatrix.getNrCols()-1;i++) {
      		tz1 = lang.newText(new Coordinates(700,380+y), "vdm("+ vdmForAttribute + ", "+frequencyMatrix.getElement(0, i+1)+")"+" = "+sumList.get(i-1), "tz1", null, tProps);
      		y=y+15;
      		}
      	lang.nextStep();
      	
      	tProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 12));
      	textList.add(lang.newText(new Coordinates(650,440), this.translator.translateMessage("conclusion4"), "conclusion4", null, tProps));
      	textList.add(lang.newText(new Coordinates(650,460), this.translator.translateMessage("conclusion5"), "conclusion5", null, tProps));
      	lang.nextStep(); 	
    }

    public String getName() {
        return "Value Difference Metric";
    }

    public String getAlgorithmName() {
        return "Value Difference Metric";
    }

    public String getAnimationAuthor() {
        return "Ilhan Simsiki, Christof Bienkowski";
    }

    public String getDescription(){
        return this.translator.translateMessage("shortdes1")
    			+"\n"
    			+"\n"
    			+this.translator.translateMessage("shortdes2")
    			+"\n"
    			+"\n"
    			+this.translator.translateMessage("shortdes3")
    			+"\n"
    			+"\n"
    			+"       "+this.translator.translateMessage("shortdes4")
    			+"\n"
    			+"       "+this.translator.translateMessage("shortdes5")
    			+"\n"
    			+"       "+this.translator.translateMessage("shortdes6")
    			+"\n"
    			+"       "+this.translator.translateMessage("shortdes7");
    }

    public String getCodeExample(){
        return this.translator.translateMessage("line1")
        		+"\n"
        		+"\n"
        		+"     while ( i < size)"
        		+"\n"
        		+"           "+this.translator.translateMessage("line2")
        		+"\n"
        		+"           "+this.translator.translateMessage("line3")
        		+"\n"
        		+"     while ( i < size)"
        		+"\n"
        		+"           "+this.translator.translateMessage("line5")
        		+"\n"
        		+this.translator.translateMessage("line6");
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return language;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    
    public void setLanguage ( Locale lang ) {
    	this.language = lang ;
    	this.translator.setTranslatorLocale (this.language) ;
    }

    public static void main(String[] args) {
       Generator generator = new ValueDifferenceMetric(Locale.GERMANY);
       //Generator generator = new ValueDifferenceMetric(Locale.US);
       Animal.startGeneratorWindow(generator);
    }

   
}