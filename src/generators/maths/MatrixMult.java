package generators.maths;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;
import java.util.StringTokenizer;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Group;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Polyline;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class MatrixMult implements Generator{
	private Language lang;
	private IntMatrix mA , mB;
	private StringMatrix mC;
	private int[][]a,b,axb;
	private SourceCode sc;
	private ArrayList<Text> sc2;
	private boolean flag1,flag2 ;
	private MatrixProperties matrixPropAB, matrixPropC;
	private PolylineProperties lineProps;
	private TextProperties textProp;
	private RectProperties rectProp;
	private SourceCodeProperties sCProp;
	public MatrixMult(){
		lang= new AnimalScript("Matrix multiplication","Tijani, Niko, Arif", 640,480);
		this.flag1 = true;
		this.flag2 = true;
		lang.setStepMode(true);

	}
	
	public void init(int[][]a,int[][] b){
		description();
		check(a,b);
		if(flag1 && flag2){
			this.a = a;
			this.b = b;
			this.axb = mult(a,b);
			lang.nextStep();
			showSourceCode1();
			sc.highlight(0);
			this.mA = lang.newIntMatrix(new Offset(200,0,sc,AnimalScript.DIRECTION_NE), a, "a", null,matrixPropAB);
			lang.newText(new Offset(-30,0,mA,AnimalScript.DIRECTION_W),"A = ", "textA", null,textProp);
			drawBracket(mA);
			this.mB = lang.newIntMatrix(new Offset(100,0,mA,AnimalScript.DIRECTION_NE), b, "b", null,matrixPropAB);
			lang.newText(new Offset(-30,0,mB,AnimalScript.DIRECTION_W),"B = ", "textB", null,textProp);
			drawBracket(mB);
			lang.nextStep();
			sc.unhighlight(0);
			lang.nextStep();
			String label = "Zur Berechnung der Produktmatrix A x B = C ist es sinnvoll, die Matrizen höhenversetzt nebeneinander zu schreiben (Falksches Schema) ";
			Text labelAB = lang.newText(new Offset(0,50,sc,AnimalScript.DIRECTION_SW),label ,"text1",new TicksTiming(5), textProp);
			lang.nextStep();
			//*********************************************************//
			mA = lang.newIntMatrix(new Offset(50,50,labelAB,AnimalScript.DIRECTION_SW), a, "mA", null,matrixPropAB);
			mB = lang.newIntMatrix(new Offset(50,0,mA,AnimalScript.DIRECTION_NE), b, "mB", null,matrixPropAB);
			Offset [] nodes = new Offset[] {new Offset(0,0,mB,AnimalScript.DIRECTION_NW),new Offset(0,20,mB,AnimalScript.DIRECTION_SW)};
			Polyline viaLine = lang.newPolyline(nodes, "viaLine", null); 
			viaLine.hide();
			lang.nextStep();
			try {
				mA.moveVia(AnimalScript.DIRECTION_N, null, viaLine, new TicksTiming(20), new TicksTiming(20));
			} catch (IllegalDirectionException e) {
				e.printStackTrace();
			}
			lang.nextStep();
			//**********************************************************//
			Text AxB = lang.newText(new Offset(50,50,labelAB,AnimalScript.DIRECTION_SW),"A x B" ,"text2",new TicksTiming(15), textProp);
			Offset [] nodes1 = new Offset[] {new Offset(2,0,AxB,AnimalScript.DIRECTION_SW),new Offset(2,40,AxB,AnimalScript.DIRECTION_SW)};
			Offset [] nodes2 = new Offset[] {new Offset(2,0,AxB,AnimalScript.DIRECTION_E),new Offset(42,0,AxB,AnimalScript.DIRECTION_E)};
			lineProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
			drawLines(nodes1, nodes2,"l1","l2",false);
			
			nodes1 = new Offset[] {new Offset(-25,0,mB,AnimalScript.DIRECTION_NW),new Offset(-25,getY(a.length,b.length),mB,AnimalScript.DIRECTION_NW)};
			nodes2 = getNodes(a,b,mA,mB);
			lineProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, false);
			drawLines(nodes1, nodes2,"l3","l4",true);
			lang.nextStep(); 
			//**********************************************************//
			Offset offsetC = new Offset(0,15,mB,AnimalScript.DIRECTION_SW);
			mC = drawMatrixC(new String[a.length][b[0].length],offsetC,matrixPropC);
			lang.nextStep();
			sc.unhighlight(1);
			lang.nextStep();
			sc2 = showSourceCode2();
		}
	}
	
	private void check(int[][] matrixA, int[][] matrixB) {
		if(flag2){
			TextProperties textProp = new TextProperties();
			textProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
			textProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Bold", Font.PLAIN, 18));
			if(matrixA[0].length != matrixB.length){
				flag1 = false;
				lang.newText(new Coordinates(50,200), "Die Multiplikation kann nicht ausgefuehrt werden, da die Anzahl der Spalten von Matrix A ungleich die Anzahl der Zeilen in Matrix B", "text", null,textProp);
			}
		}
	}
	
	public void inciteMult(){
		//*********************************************************//
		if(flag1&&flag2){
			int index = 0; 
			String newText;
			Text oldText = null;
			for (int i = 0; i < axb.length; i++) {
				highlightLine(2,i,false,false);
				for (int j = 0; j < axb[0].length; j++) {
					highlightLine(3,j,false,true);
					highlightLine(4,0,true,false);
					mC.highlightElem(i,j,new TicksTiming(20), new TicksTiming(20));
					lang.nextStep();
					sc2.get(index).show();
					lang.nextStep();
					sc.highlight(5);
					lang.nextStep();
					newText = Integer.toString(mA.getElement(i,0))+"*"+Integer.toString(mB.getElement(0,j));
					oldText = highlight(i,j,0,sc2.get(index),textProp,newText);
					sc.unhighlight(5);
					lang.nextStep();
					for (int k = 1; k < b.length; k++) {
						highlightLine(4,k,true,false);
						sc.highlight(5);
						lang.nextStep();
						newText ="+"+Integer.toString(mA.getElement(i,k))+"*"+Integer.toString(mB.getElement(k,j));
						oldText = highlight(i,j,k,oldText,textProp,newText);
						sc.unhighlight(5);
						lang.nextStep();
					}
					newText = "="+Integer.toString(axb[i][j]);
					oldText = lang.newText(new Offset(0,0,oldText,AnimalScript.DIRECTION_NE),newText,"=", null,textProp);
					mC.unhighlightElem(i,j,new TicksTiming(20), new TicksTiming(20));
					index++;
					lang.nextStep();
				}
			}
			sc.highlight(9);
			lang.nextStep();
			transform();
			sc.unhighlight(9);
			lang.nextStep();
			lang.newRect(new Offset(-10,-10,sc2.get(0),AnimalScript.DIRECTION_NW), new Offset(10,10,oldText,AnimalScript.DIRECTION_SE), "rect", null,rectProp);
			lang.newRect(new Offset(-8,-8,sc2.get(0),AnimalScript.DIRECTION_NW), new Offset(12,12,oldText,AnimalScript.DIRECTION_SE), "rect", null,rectProp);
			lang.newRect(new Offset(-6,-6,sc2.get(0),AnimalScript.DIRECTION_NW), new Offset(14,14,oldText,AnimalScript.DIRECTION_SE), "rect", null,rectProp);
		//*********************************************************//
		}
		
	}
	
	private void transform() {
		for(int i = 0; i <axb.length; i++){
			for(int j = 0; j < axb[0].length; j++){
				mC.put(i, j, Integer.toString(axb[i][j]),new TicksTiming(20), new TicksTiming(20));
			}
		}
	}

	private void showSourceCode1() {
		// now, create the source code entity
		sc= lang.newSourceCode(new Coordinates(40,40), "sourceCode",null, sCProp);
		sc.addCodeLine("public  int[][] mult(int[][] a, int[][] b){", null, 0, null);
		sc.addCodeLine("int[][] c = new int[a.length][b[0].length];", null, 1, null);
		sc.addCodeLine("for (int i = 0; i < a.length; i++) {", null, 1, null);
		sc.addCodeLine("for (int j = 0; j < b[0].length; j++) {", null, 2, null);
		sc.addCodeLine("for (int k = 0; k < b.length; k++) {", null, 3, null);
		sc.addCodeLine("c[i][j]+=a[i][k]*b[k][j]", null, 4, null);
		sc.addCodeLine("}", null, 3, null);
		sc.addCodeLine("}", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("return c;", null, 1, null);
		sc.addCodeLine("}", null, 0, null);
		//***********************************
		lang.newRect(new Offset(-10,-10,sc,AnimalScript.DIRECTION_NW), new Offset(10,10,sc,AnimalScript.DIRECTION_SE), "rect", null,rectProp);
		lang.newRect(new Offset(-8,-8,sc,AnimalScript.DIRECTION_NW), new Offset(12,12,sc,AnimalScript.DIRECTION_SE), "rect", null,rectProp);
		lang.newRect(new Offset(-6,-6,sc,AnimalScript.DIRECTION_NW), new Offset(14,14,sc,AnimalScript.DIRECTION_SE), "rect", null,rectProp);
		lang.nextStep();
	}

	public ArrayList<Text>  showSourceCode2(){
		textProp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
		// now, create the source code entity
		String s = null;
		String name = null;
		ArrayList<Text> sc2 = new  ArrayList<Text>();
		int y = 0;
		for(int i = 1; i <=a.length; i++){
			for(int j =1; j <=b[0].length; j++){
				s ="C"+Integer.toString(i)+Integer.toString(j)+"= ";
				name =Integer.toString(i)+Integer.toString(j);
				Text t = lang.newText(new Offset(200,y,mB,AnimalScript.DIRECTION_NE),s,name,null, textProp);
				sc2.add(t);
				y+=20;
			}
			
		}
		textProp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY,false);
		return sc2;
	}
	//#############################################################################//
	private void highlightLine(int i,int index,boolean b1,boolean b2){
		sc.highlight(i);
		lang.nextStep();
		if(!b1 && !b2){
			mA.highlightCellColumnRange(index, 0, mA.getNrCols()-1, new TicksTiming(20),new  TicksTiming(20));
			lang.nextStep();
			mA.unhighlightCellColumnRange(index, 0, mA.getNrCols()-1, new TicksTiming(20),new  TicksTiming(20));
		}
		else{
			if(!b1 && b2){
				mB.highlightCellRowRange(0, mB.getNrRows()-1, index,new TicksTiming(20),new  TicksTiming(20));
				lang.nextStep();
				mB.unhighlightCellRowRange(0, mB.getNrRows()-1, index,new TicksTiming(20),new  TicksTiming(20));
			}
		}
		sc.unhighlight(i);
		lang.nextStep();
	}
	//########################################################################//
	public Text highlight(int i, int j, int k,Text oldText,TextProperties scProps, String newText){
		
		mA.highlightElem(i,k,new TicksTiming(40), new TicksTiming(20));
		mB.highlightElem(k,j,new TicksTiming(40), new TicksTiming(20));
		lang.nextStep();
		
		Text anOldText = lang.newText(new Offset(0,0,oldText,AnimalScript.DIRECTION_NE),newText,Integer.toString(k), null,scProps);
		lang.nextStep();
		
		mA.unhighlightElem(i,k,new TicksTiming(20), new TicksTiming(20));
		mB.unhighlightElem(k,j,new TicksTiming(20), new TicksTiming(20));
		lang.nextStep();
		return anOldText;
	}

	private Offset[] getNodes(int[][] a, int[][] b, IntMatrix ma, IntMatrix mb) {
		int x = ((2*a[0].length-1)+(2*b[0].length-1))*20 + 50;
		if(b.length < a.length)
			return new Offset []{new Offset(0,0,ma,AnimalScript.DIRECTION_SW),new Offset(x,0,ma,AnimalScript.DIRECTION_SW)};
		else
			return new Offset []{new Offset(20,0,mb,AnimalScript.DIRECTION_SE),new Offset(-x,0,mb,AnimalScript.DIRECTION_SE)};
	}

	private int getY(int l1, int l2) {
		return ((2*l1-1)+(2*l2-1))*20;
	}

	private StringMatrix drawMatrixC(String[][] cString, Offset offsetC, MatrixProperties propForMc2) {
		sc.highlight(1);
		for(int i = 1; i <=cString.length; i++){
			for(int j = 1; j<= cString[0].length; j++){
				cString[i-1][j-1] = "C"+Integer.toString(i)+Integer.toString(j);
			}
		}
		return lang.newStringMatrix(offsetC, cString, "C",null, matrixPropC);
	}

	private void drawLines(Offset[] ns1, Offset[] ns2,String name1, String name2,boolean b) {
		Polyline l1 = lang.newPolyline(ns1,name1,null,lineProps);
		Polyline l2 = lang.newPolyline(ns2,name2, null,lineProps);
		if(b){
			Offset[] offsets1 = new Offset[] {new Offset(-2,0,l1,AnimalScript.DIRECTION_NE),new Offset(-2,0,l1,AnimalScript.DIRECTION_SE)};
			Offset[] offsets2 = new Offset[] {new Offset(0,-2,l2,AnimalScript.DIRECTION_NE),new Offset(0,-2,l2,AnimalScript.DIRECTION_NW)};
			lang.newPolyline(offsets1,"l1_2",null,lineProps);
			lang.newPolyline(offsets2,"l2_2",null,lineProps);
		}
	}

	private void drawBracket(IntMatrix m) {
		Offset [] nodes;
		nodes =new Offset[] {new Offset(0,0,m,AnimalScript.DIRECTION_NW),new Offset(0,10,m,AnimalScript.DIRECTION_SW)};
		Polyline lLeft = lang.newPolyline(nodes,"line1",null,lineProps);
		nodes = new Offset[] {new Offset(-5,0,m,AnimalScript.DIRECTION_NE),new Offset(-5,10,m,AnimalScript.DIRECTION_SE)};
		Polyline lRight = lang.newPolyline(nodes,"line2",null,lineProps);
		nodes = new Offset[]{new Offset(0,0,lLeft,AnimalScript.DIRECTION_NW),new Offset(3,0,lLeft,AnimalScript.DIRECTION_NW)};
		lang.newPolyline(nodes,"line1",null,lineProps);
		nodes = new Offset[]{new Offset(0,0,lRight,AnimalScript.DIRECTION_NW),new Offset(-3,0,lRight,AnimalScript.DIRECTION_NW)};
		lang.newPolyline(nodes,"line1",null,lineProps);
		nodes = new Offset[]{new Offset(0,0,lLeft,AnimalScript.DIRECTION_SW),new Offset(3,0,lLeft,AnimalScript.DIRECTION_SW)};
		lang.newPolyline(nodes,"line1",null,lineProps);
		nodes = new Offset[]{new Offset(0,0,lRight,AnimalScript.DIRECTION_SW),new Offset(-3,0,lRight,AnimalScript.DIRECTION_SW)};
		lang.newPolyline(nodes,"line1",null,lineProps);
	}

	public  int[][] mult(int[][] a, int[][] b){
		int[][] c = new int[a.length][b[0].length];
	
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				for (int k = 0; k < b.length; k++) {
					c[i][j]+=a[i][k]*b[k][j];
				}
			}
		}
		return c;
	}
	

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		int zA = (Integer)primitives.get("row_number_Of _MatrixA"); // zA: zeilen von A
		int sA = (Integer)primitives.get("col_number_Of _MatrixA"); // sA: spalten von A
		int zB = (Integer)primitives.get("row_number_Of _MatrixB");
		int sB = (Integer)primitives.get("col_number_Of _MatrixB");
		int[][] a = parse((String)primitives.get("MatrixA"),zA,sA);
		int[][] b = parse((String)primitives.get("MatrixB"),zB,sB);
		
		matrixPropAB = (MatrixProperties)     props.get(0);
		matrixPropC  = (MatrixProperties)     props.get(1);
		textProp     = (TextProperties)       props.get(2);
		lineProps	 = (PolylineProperties)   props.get(3);
		rectProp	 = (RectProperties)		  props.get(4);
		sCProp		 = (SourceCodeProperties) props.get(5);
		
		init(a, b);
		inciteMult();
		return lang.toString();
	}

	private void description() {
		TextProperties textProp = new TextProperties();
		textProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
		textProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Bold", Font.BOLD, 18));
		lang.newText(new Coordinates(400,10), "Multiplikation zweier Matrizen", "titel", null,textProp);
		lang.nextStep();
		SourceCodeProperties scProps= new SourceCodeProperties();
		scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 18));
		lang.nextStep();
		scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		SourceCode sc = lang.newSourceCode(new Coordinates(10,40), "discription", null,scProps);
		sc.addCodeLine("Das Produkt zweier Matrizen A und B ist nur dann definiert, wenn die Anzahl der Spalten der ersten Matrix",null,0,null);
		lang.nextStep();
		sc.addCodeLine("gleich der Anzahl der Zeilen der zweiten Matrix ist.",null,0,null);
		lang.nextStep();
		sc.addCodeLine("D.h., wenn A eine n x m-Matrix ist, so muss B eine m x k-Matrix sein. ",null,0,null);
		lang.nextStep();
		sc.addCodeLine("Die Produktmatrix C=A.B ist dann eine n x k-Matrix.",null,0,null);
		lang.nextStep();
		sc.addCodeLine("Zur Berechnung des Elements Cij der Produktmatrix wird die i-te Zeile der ersten Matrix mit der j-ten Spalte",null,0,null);
		lang.nextStep();
		sc.addCodeLine("der zweiten Matrix multipliziert (im Sinne eines Skalarprodukts):",null,0,null);
		lang.nextStep();
		blub(sc);
		lang.nextStep();
		sc.hide();
//		titel.hide();
		
	}

	private void blub(SourceCode sc) {
		LinkedList<Primitive> list = new LinkedList<Primitive>();
		TextProperties textProp = new TextProperties();
		textProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		PolylineProperties pl = new PolylineProperties();
		pl.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		list.add(lang.newText(new Offset(-30,45,sc,AnimalScript.DIRECTION_S), "Cij =", "t1", null,textProp));
		Offset [] nodes = new Offset []{new Offset(0,50,sc,AnimalScript.DIRECTION_S),new Offset(20,50,sc,AnimalScript.DIRECTION_S)};
		list.add(lang.newPolyline(nodes, "l1", null,pl));
		nodes = new Offset []{new Offset(0,50,sc,AnimalScript.DIRECTION_S),new Offset(10,60,sc,AnimalScript.DIRECTION_S)};
		list.add(lang.newPolyline(nodes, "l2", null,pl));
		nodes = new Offset []{new Offset(10,60,sc,AnimalScript.DIRECTION_S),new Offset(0,70,sc,AnimalScript.DIRECTION_S)};
		list.add(lang.newPolyline(nodes, "l3", null,pl));
		nodes = new Offset []{new Offset(0,70,sc,AnimalScript.DIRECTION_S),new Offset(25,70,sc,AnimalScript.DIRECTION_S)};
		list.add(lang.newPolyline(nodes, "l4", null,pl));
		list.add(lang.newText(new Offset(-5,-20,list.get(1),AnimalScript.DIRECTION_N), "m", "t2", null,textProp));
		list.add(lang.newText(new Offset(-5,-20,list.get(1),AnimalScript.DIRECTION_N), "m", "t2", null,textProp));
		list.add(lang.newText(new Offset(-7,0,list.get(4),AnimalScript.DIRECTION_S), "s=1", "t3", null,textProp));
		list.add(lang.newText(new Offset(40,49,sc,AnimalScript.DIRECTION_S), "Ais*Bsj", "t4", null,textProp));
		Rect rect = lang.newRect(new Offset(-5,-30,list.get(0),AnimalScript.DIRECTION_NW), new Offset(5,30,list.getLast(),AnimalScript.DIRECTION_SE), "rect", null);
		
		list.add(rect);
		Group g =lang.newGroup(list, "list");
		lang.nextStep();
		g.hide();
		lang.nextStep();
	}

	private  int[][] parse(String m,int z,int s) {
		boolean b = true;
		int [][] matrix = new int[z][s];
		StringTokenizer st = new StringTokenizer(m,"; ");
		int counter = 0;
		while(st.hasMoreTokens()&& b && counter < z){
			if(!flag2)
				break;
			try {
				b = add(matrix[counter],new StringTokenizer(st.nextToken(),"{}, "));
			} catch (Exception e) {
				String text = "Die Elemente der Matrizen sollen alle von typ Integer sein ;-)";
				TextProperties textProp = new TextProperties();
				textProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
				textProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Bold", Font.PLAIN, 18));
				lang.newText(new Coordinates(50,200), text, "text1", null,textProp);
				flag2 = false;
				break;
			}
			counter++;
		}
		return matrix;
	}
	
	private boolean add(int[] elem, StringTokenizer sT) {
		boolean b = true;
		try {
			if(elem.length < sT.countTokens())
				throw new Exception();
		} catch (Exception e) {
			b = false;
			TextProperties textProp = new TextProperties();
			textProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
			textProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Bold", Font.PLAIN, 18));
			String text ="Die Zeilen der Matrix haben nicht die selbe Länge";
			lang.newText(new Coordinates(50,200), text, "text2", null,textProp);
			flag2 = false;
			lang.nextStep();
		}
		if(b){
			for(int i= 0; i < elem.length&& sT.hasMoreTokens(); i++)
				elem[i]=Integer.parseInt(sT.nextToken());
		}
		return b;
	}
	
	
	public String getAlgorithmName() {
		return "Matrizenmultiplikation";
	}

	
	public String getAnimationAuthor() {
		return "Tijani Ahmedou, Arif Sami, Nikola Dyundev";
	}

	
	public String getCodeExample() {
		return "public  int[][] mult(int[][] a, int[][] b){\n"+
				"   int[][] c = new int[a.length][b[0].length];\n"+
				"   for (int i = 0; i < a.length; i++) {\n"+
				"      for (int j = 0; j < b[0].length; j++) {\n"+
				"         for (int k = 0; k < b.length; k++) {\n"+
				"            c[i][j]+=a[i][k]*b[k][j];\n"+
				"         }\n"+	
				"      }\n"+
				"   }\n"+	
				"   return c;\n"+
				"}\n";
	}

	
	public Locale getContentLocale() {
		return Locale.GERMANY;
	}

	
	public String getDescription() {
		return"Animates matrix multiplication with Source Code + Highlighting";
	}

	
	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}

	
	public String getName() {
		return "matrix multiplication";
	}

	
	public String getOutputLanguage() {
		return Generator.JAVA_OUTPUT;
	}

	
	public void init() {
		// nothing to be done here
		
	}
}