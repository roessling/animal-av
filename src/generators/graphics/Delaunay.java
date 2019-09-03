package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Ellipse2D;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Triangle;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.TriangleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class Delaunay implements ValidatingGenerator {
    private Language lang;
    
    private TextProperties Description, Legend_Text, Headline;
    private int[][] PointList;
    private Color Background_Color;
    private TriangleProperties Current_Triangle_Props, Finished_Triangle_Props;
    private CircleProperties Circum, Point1_Props, Point2_Props, Point3_Props, Point4_Props, Other_Points_Props;
    private SourceCodeProperties Source_code_Props;
    
    private RectProperties Headline_Border_Props, Legend_Rectangle_Props, Point_Rectangle_Props;
    
    private Circle[] circleList;
    private java.awt.Point[] absPosPointList;
        
    private SourceCode src;
    
    private int minX, minY, maxX, maxY;
    
    private int startDrawingAreaX = 20, startDrawingAreaY = 70;
    private int startLegendX = 20, startLegendY = 390;
    private int widthDrawingArea = 300, heightDrawingArea = widthDrawingArea;

    public Delaunay(Language language) {
        this.lang = language;
        // This initializes the step mode. Each pair of subsequent steps has to
        // be divdided by a call of lang.nextStep();
        lang.setStepMode(true);
      }

      public Delaunay() {
        init();
        lang.setStepMode(true);
      }
    
    public void init(){
        lang = new AnimalScript("Delaunay-Triangulation", "Ralf Rurainsky", 800, 600);
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

    	Description = (TextProperties)props.getPropertiesByName("Description");
    	Legend_Text = (TextProperties)props.getPropertiesByName("Legend_Text");
    	
        PointList = (int[][]) primitives.get("PointList");
        
        // �berpr�fe, ob in den Eingabedaten ein Punkt mind. doppelt vorkommt
        Set<Integer> removeIndex = new HashSet<Integer>();
        for (int i = 0; i < PointList.length; i++) {
			for (int j = i + 1; j < PointList.length; j++) {
				if(PointList[i][0] == PointList[j][0] && PointList[i][1] == PointList[j][1])
					removeIndex.add(j);					
			}
		}
        int[][] temp = new int[PointList.length - removeIndex.size()][2];
        int counter = 0;
        for (int i = 0; i < PointList.length; i++) {
			if(removeIndex.contains(i))
				continue;
			temp[counter++] = PointList[i];
		}
        PointList = temp;
        
        Background_Color = (Color) primitives.get("Background_Color");
        
        Current_Triangle_Props = (TriangleProperties)props.getPropertiesByName("Current_Triangle");
        Current_Triangle_Props.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
        
        Finished_Triangle_Props = (TriangleProperties)props.getPropertiesByName("Finished_Triangle");
        Finished_Triangle_Props.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
        
        Circum = (CircleProperties)props.getPropertiesByName("Circum");
        Point1_Props = (CircleProperties)props.getPropertiesByName("Point1");
        Point2_Props = (CircleProperties)props.getPropertiesByName("Point2");
        Point3_Props = (CircleProperties)props.getPropertiesByName("Point3");
        Point4_Props = (CircleProperties)props.getPropertiesByName("Point4");
        Other_Points_Props = (CircleProperties)props.getPropertiesByName("Other_Points");
        Source_code_Props = (SourceCodeProperties)props.getPropertiesByName("Source_code");
        
        Headline_Border_Props = (RectProperties)props.getPropertiesByName("Headline_Border");
        Headline_Border_Props.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
        
        Headline = (TextProperties)props.getPropertiesByName("Headline");
        
        Legend_Rectangle_Props = (RectProperties)props.getPropertiesByName("Legend_Rectangle");
        Legend_Rectangle_Props.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
        
        Point_Rectangle_Props = (RectProperties)props.getPropertiesByName("Point_Rectangle");
        Point_Rectangle_Props.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 30);
    	
        circleList = new Circle[PointList.length];
        absPosPointList = new java.awt.Point[PointList.length];
        
        // Berechne Minimum/Maximum der Eingabedaten
        minX = PointList[0][0]; maxX = PointList[0][0];
        minY = PointList[0][1]; maxY = PointList[0][1];
        
        for (int i = 1; i < PointList.length; i++) {
			if(minX > PointList[i][0]) minX = PointList[i][0];
			if(minY > PointList[i][1]) minY = PointList[i][1];
			if(maxX < PointList[i][0]) maxX = PointList[i][0];
			if(maxY < PointList[i][1]) maxY = PointList[i][1];
		}
        
        // erzeuge rechteckige Zeichenfl�che
        if(maxX - minX > maxY - minY)
        	maxY = minY + (maxX - minX);
        else
        	maxX = minX + (maxY - minY);
        
        int diffX = maxX - minX, diffY = maxY - minY;
        
        // Skalierung der Zeichenfl�che in Abh�ngigkeit der Eingabedaten
        widthDrawingArea = (int) (diffX * 300.0 / 170.0);
        heightDrawingArea = (int) (diffY * 300.0 / 170.0);
        
        startLegendY = startDrawingAreaY + 20 + heightDrawingArea;
        
        calculate();
        
        return lang.toString();
    }

//  Eigentlicher Algorithmus
    private void calculate() {
    	
    	lang.setStepMode(true);
    	   	        
        
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.BOLD, 24));
        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Headline.get("color"));
                
        RectProperties drawingProps = new RectProperties();
        drawingProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
                
        RectProperties bugFixProps = new RectProperties();
        bugFixProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        bugFixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Background_Color);
        bugFixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Background_Color);
        bugFixProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 20);
        
        RectProperties bugFixPropsBackDrawingArea = new RectProperties();
        bugFixPropsBackDrawingArea.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        bugFixPropsBackDrawingArea.set(AnimationPropertiesKeys.FILL_PROPERTY, Background_Color);
        bugFixPropsBackDrawingArea.set(AnimationPropertiesKeys.COLOR_PROPERTY, Background_Color);
        bugFixPropsBackDrawingArea.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 31);
        
        
        Rect leftbugFixRect = lang.newRect(new Coordinates(0, 0), new Coordinates(startDrawingAreaX - 6, 999999), "leftbugFixRect", null, bugFixProps);
        Rect rightbugFixRect = lang.newRect(new Coordinates(startDrawingAreaX + widthDrawingArea + 6, 0), new Coordinates(999999, 999999), "rightbugFixRect", null, bugFixProps);
        Rect topbugFixRect = lang.newRect(new Coordinates(startDrawingAreaX - 6, 0), new Coordinates(startDrawingAreaX + widthDrawingArea + 6, startDrawingAreaY - 6), "topbugFixRect", null, bugFixProps);
        Rect buttonbugFixRect = lang.newRect(new Coordinates(startDrawingAreaX - 6, startDrawingAreaY + heightDrawingArea + 6), new Coordinates(startDrawingAreaX + widthDrawingArea + 6, 9999999), "buttonbugFixRect", null, bugFixProps);
        Rect middlebugFixRect = lang.newRect(new Coordinates(startDrawingAreaX - 5, startDrawingAreaY - 5), new Coordinates(startDrawingAreaX + widthDrawingArea + 5, startDrawingAreaY + heightDrawingArea + 5), "middlebugFixRect", null, bugFixPropsBackDrawingArea);
        
        Text header = lang.newText(new Coordinates(20, 30), "Delaunay-Triangulation",
                "header", null, headerProps);
        
        Rect headlineRect = lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
                null, Headline_Border_Props);
        
        PolylineProperties vectorProps = new PolylineProperties();
        vectorProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
        
        Text[] descriptionText = new Text[8];
        descriptionText[0] = lang.newText(new Coordinates(20, 60), "Die Delaunay-Triangulation ist ein gebräuchliches Verfahren,", "description1", null, Description);
//        descriptionText[0] = lang.newText(new Coordinates(20, 70), "Die Delaunay-Triangulation ist ein gebräuchliches Verfahren,", "description1", null, Description);
        descriptionText[1] = lang.newText(new Coordinates(20, 85), "um aus einer Menge an Punkten ein Dreiecksnetz zu erstellen.", "description2", null, Description);
        descriptionText[2] = lang.newText(new Coordinates(20, 115), "Das durch die Triangulation erstellte Dreiecksnetz erfüllt die so", "description3", null, Description);
        descriptionText[3] = lang.newText(new Coordinates(20, 130), "genannte Umkreisbedingung: Der Umkreis eines Dreiecks des", "description4", null, Description);
        descriptionText[4] = lang.newText(new Coordinates(20, 145), "Netzes beinhaltet keine weiteren Punkte der gegebenen Punkt-", "description5", null, Description);
        descriptionText[5] = lang.newText(new Coordinates(20, 160), "menge. Dadurch weisen die Dreiecke des Netzes möglichst große", "description6", null, Description);
        descriptionText[6] = lang.newText(new Coordinates(20, 175), "Innenwinkel auf. Diese Eigenschaft wird vor allem in der", "description7", null, Description);
        descriptionText[7] = lang.newText(new Coordinates(20, 190), "Computergrafik zur Minimierung von Rundungsfehlern benötigt.", "description8", null, Description);
        
        Text[] finallyText = new Text[9];
//      finallyText[0] = lang.newText(new Coordinates(20, 60), "Das hier gezeigte Verfahren zur Berechnung der Delaunay-", "finallyText1", null, Description);
	    finallyText[0] = lang.newText(new Coordinates(20, 70), "Das hier gezeigte Verfahren zur Berechnung der Delaunay-", "finallyText", null, Description);
	    finallyText[1] = lang.newText(new Coordinates(20, 85), "Triangulation befindet sich in der Komplexitätsklasse O(n^4), ", "finallyText2", null, Description);
	    finallyText[2] = lang.newText(new Coordinates(20, 100), "da zuerst alle möglichen Dreiecke konstruiert werden müssen ", "finallyText3", null, Description);
	    finallyText[3] = lang.newText(new Coordinates(20, 115), "( O(n^3) ) und anschließend für alle restlichen Punkte geprüft ", "finallyText4", null, Description);
	    finallyText[4] = lang.newText(new Coordinates(20, 130), "werden muss, ob sich diese im Umkreis des Dreiecks befinden.", "finallyText5", null, Description);
	    finallyText[5] = lang.newText(new Coordinates(20, 160), "Dieses Vorgehen ist eines der trivialsten.", "finallyText6", null, Description);
	    finallyText[6] = lang.newText(new Coordinates(20, 175), "Der Flip-Algorithmus löst das Problem in O(n^2), andere ", "finallyText7", null, Description);
	    finallyText[7] = lang.newText(new Coordinates(20, 190), "Algorithmen wie der Sweep-Algorithmus oder ein Ansatz über ", "finallyText8", null, Description);
	    finallyText[8] = lang.newText(new Coordinates(20, 205), "Divide and conquer finden Lösungen in O(n * log(n)).", "finallyText9", null, Description);
	
	    for (int i = 0; i < finallyText.length; i++) {
	      finallyText[i].hide();
		}
        
	    
        lang.nextStep("Einführung");
        for (int i = 0; i < descriptionText.length; i++) {
			descriptionText[i].hide();
		}
        
        Rect drawingArea = lang.newRect(
        		new Coordinates(startDrawingAreaX, startDrawingAreaY), 
        		new Coordinates(startDrawingAreaX + widthDrawingArea, startDrawingAreaY + heightDrawingArea),
                "drawingArea", 
                null, 
                drawingProps);
        
        Rect drawingRect = lang.newRect(
        		new Offset(-5, -5, "drawingArea", AnimalScript.DIRECTION_NW), 
        		new Offset(5, 5, "drawingArea", "SE"), 
        		"drawingRect", 
        		null, 
        		Point_Rectangle_Props);
        
        // Legende
        Rect legendRect = lang.newRect(
        		new Coordinates(startLegendX, startLegendY), 
        		new Coordinates(startLegendX + 345, startLegendY + 120), 
        		"legendRect", 
        		null, drawingProps);
        
        
        Circum.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 22);        
        
        Rect legendRectBorder = lang.newRect(
        		new Offset(-5, -5, "legendRect", AnimalScript.DIRECTION_NW),
        		new Offset(5, 5, "legendRect", "SE"),
        		"legendRectBorder",
        		null, Legend_Rectangle_Props);
        
        Text point2LegendText = lang.newText(new Offset(10, 15, "legendRect", AnimalScript.DIRECTION_NW), "P2", "randomText2", null, Legend_Text);
        Text point1LegendText = lang.newText(new Offset(10,  0, "legendRect", AnimalScript.DIRECTION_NW), "P1", "randomText1", null, Legend_Text);
        Text point3LegendText = lang.newText(new Offset(10, 30, "legendRect", AnimalScript.DIRECTION_NW), "P3", "randomText3", null, Legend_Text);
    	
        Text pointForLegendText = lang.newText(new Offset(10, 75, "legendRect", AnimalScript.DIRECTION_NW), "actPoint", "randomText4", null, Legend_Text);
        
        Text umkreisLegendText = lang.newText(new Offset(140, 75, "legendRect", AnimalScript.DIRECTION_NW), "Umkreis:", "umkreisText", null, Legend_Text);
        Text umkreisLegendCenterText = lang.newText(new Offset(150, 90, "legendRect", AnimalScript.DIRECTION_NW), "Mittelpunkt:", "umkreisText1", null, Legend_Text);
        Text umkreisLegendRadiusText = lang.newText(new Offset(150, 105, "legendRect", AnimalScript.DIRECTION_NW), "Radius:", "umkreisText2", null, Legend_Text);
        
        Text a1sText = lang.newText(new Offset(140, 0, "legendRect", AnimalScript.DIRECTION_NW), "a1s", "a1s", null, Legend_Text);
        Text b1sText = lang.newText(new Offset(140, 15, "legendRect", AnimalScript.DIRECTION_NW), "b1s", "b1s", null, Legend_Text);
        Text a2sText = lang.newText(new Offset(140, 30, "legendRect", AnimalScript.DIRECTION_NW), "a2s", "a2s", null, Legend_Text);
        Text b2sText = lang.newText(new Offset(140, 45, "legendRect", AnimalScript.DIRECTION_NW), "b2s", "b2s", null, Legend_Text);
        
        
        Circle point1Legend = lang.newCircle(new Offset(3, 8, "legendRect", AnimalScript.DIRECTION_NW), 3, "pL1", null, Point1_Props);
        Circle point2Legend = lang.newCircle(new Offset(3, 23, "legendRect", AnimalScript.DIRECTION_NW), 3, "pL2", null, Point2_Props);
        Circle point3Legend = lang.newCircle(new Offset(3, 38, "legendRect", AnimalScript.DIRECTION_NW), 3, "pL3", null, Point3_Props);
        Circle pointForLegend = lang.newCircle(new Offset(3, 83, "legendRect", AnimalScript.DIRECTION_NW), 3, "pLF", null, Point4_Props);

//      Initialisierung               
    	for (int i = 0; i < PointList.length; i++) {
			int offsetX = (int) (1.0 * (PointList[i][0] - minX) / (maxX - minX) * widthDrawingArea);
			int offsetY = (int) (1.0 * (PointList[i][1] - minY) / (maxY - minY) * heightDrawingArea);
			
			circleList[i] = lang.newCircle(
					new Offset(offsetX, offsetY, "drawingArea", AnimalScript.DIRECTION_NW), 
					3, 
					"point" + i,
					null,
					Other_Points_Props);
			
			absPosPointList[i] = new java.awt.Point(offsetX + startDrawingAreaX, offsetY + startDrawingAreaY);
    	}
    	
        src = lang.newSourceCode(new Coordinates(Math.max(70 + widthDrawingArea, 390), 10), "sourceCode", null, Source_code_Props);
        src.addCodeLine("function Set<int[]> delaunay(int[][] PointList) {", null, 0, null); // 0
        src.addCodeLine("", null, 1, null); // 1
        src.addCodeLine("Set<int[]> results = new HashSet<int[]>();", null, 1, null); // 2
        src.addCodeLine("", null, 1, null); // 3
        src.addCodeLine("for (int firstPoint = 0; firstPoint < PointList.length - 2; firstPoint++) {", null, 1, null); // 4
        src.addCodeLine("for (int secondPoint = firstPoint + 1; secondPoint < PointList.length - 1; secondPoint++) {", null, 2, null); // 5
        src.addCodeLine("for (int thirdPoint = secondPoint + 1; thirdPoint < PointList.length - 0; thirdPoint++) {", null, 3, null); // 6
        src.addCodeLine("", null, 1, null); // 7
        src.addCodeLine("int a1s = PointList[firstPoint][0] - PointList[thirdPoint][0];", null, 4, null); // 8
        src.addCodeLine("int a2s = PointList[firstPoint][1] - PointList[thirdPoint][1];", null, 4, null); // 9
        src.addCodeLine("int b1s = PointList[secondPoint][0] - PointList[thirdPoint][0];", null, 4, null); // 10
        src.addCodeLine("int b2s = PointList[secondPoint][1] - PointList[thirdPoint][1];", null, 4, null); // 11
        src.addCodeLine("", null, 1, null); // 12
        src.addCodeLine("if(a1s * b2s == a2s * b1s) // Überprüfe, ob die drei Punkte auf einer Linie sind", null, 4, null); // 13
        src.addCodeLine("continue;", null, 5, null); // 14
        src.addCodeLine("", null, 1, null); // 15
        src.addCodeLine("double centerX = c1 + b1s / 2.0 + b2s * (Math.pow(a1s, 2) + Math.pow(a2s, 2) - a1s * b1s - a2s * b2s) / (2.0 * (a1s * b2s - a2s * b1s));", null, 4, null); // 16
        src.addCodeLine("double centerY = c2 + b2s / 2.0 - b1s * (Math.pow(a1s, 2) + Math.pow(a2s, 2) - a1s * b1s - a2s * b2s) / (2.0 * (a1s * b2s - a2s * b1s));", null, 4, null); // 17
        src.addCodeLine("double radius = Math.sqrt(Math.pow(a1 - centerX, 2) + Math.pow(a2 - centerY, 2));", null, 4, null); // 18
        src.addCodeLine("Ellipse2D circuit = new Ellipse2D.Double(radius * 2, radius * 2, centerX - radius, centerY - radius);", null, 4, null); // 19
        src.addCodeLine("", null, 4, null); // 20
        src.addCodeLine("boolean isPointinCircle = false;", null, 4, null); // 21
        src.addCodeLine("for (int actPoint = 0; actPoint < PointList.length; actPoint++) {", null, 4, null); // 22
        src.addCodeLine("", null, 4, null); // 23
        src.addCodeLine("if(actPoint == firstPoint || actPoint == secondPoint || actPoint == thirdPoint)", null, 5, null); // 24
        src.addCodeLine("continue;", null, 6, null); // 25
        src.addCodeLine("", null, 4, null); // 26
        src.addCodeLine("if(circuit.contains(PointList[actPoint][0], PointList[actPoint][1])) { // liegt der Punkt im Umkreis?", null, 5, null); // 27
        src.addCodeLine("isPointinCircle = true;", null, 6, null); // 28
        src.addCodeLine("break;", null, 6, null); // 29
        src.addCodeLine("}", null, 5, null); // 30
        src.addCodeLine("", null, 4, null); // 31
        src.addCodeLine("if(!isPointinCircle)", null, 4, null); // 32
        src.addCodeLine("results.add(new int[] {firstPoint, secondPoint, thirdPoint});", null, 5, null); // 33
        src.addCodeLine("", null, 4, null); // 34
        src.addCodeLine("}", null, 3, null); // 35
        src.addCodeLine("}", null, 2, null); // 36
        src.addCodeLine("}", null, 1, null); // 37
        src.addCodeLine("", null, 4, null); // 38
        src.addCodeLine("return results;", null, 1, null); // 39
        src.addCodeLine("}", null, 0, null); // 40
        
        lang.nextStep();
        

        TriangleProperties tProps = new TriangleProperties();
        tProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
        Triangle currentTriangle = null;
        
        Set<int[]> results = new HashSet<int[]>();
        
        for (int firstPoint = 0; firstPoint < PointList.length - 2; firstPoint++) {
        	circleList[firstPoint].hide();
        	circleList[firstPoint] = lang.newCircle(circleList[firstPoint].getCenter(), circleList[firstPoint].getRadius(), "", null, Point1_Props);
        	
        	point1LegendText.setText("P1 (" + PointList[firstPoint][0] + ", " + PointList[firstPoint][1] + ")", null, null);
        	
        	src.highlight(4);        	
        	lang.nextStep();
        	src.unhighlight(4);
        	
        	for (int secondPoint = firstPoint + 1; secondPoint < PointList.length - 1; secondPoint++) {
        		circleList[secondPoint].hide();
            	circleList[secondPoint] = lang.newCircle(circleList[secondPoint].getCenter(), circleList[secondPoint].getRadius(), "", null, Point2_Props);
            	        		
        		point2LegendText.setText("P2 (" + PointList[secondPoint][0] + ", " + PointList[secondPoint][1] + ")", null, null);
            	
        		src.highlight(5);        		
        		lang.nextStep();
        		src.unhighlight(5);
        		
        		for (int thirdPoint = secondPoint + 1; thirdPoint < PointList.length - 0; thirdPoint++) {
        			circleList[thirdPoint].hide();
                	circleList[thirdPoint] = lang.newCircle(circleList[thirdPoint].getCenter(), circleList[thirdPoint].getRadius(), "", null, Point3_Props);
                	
        			point3LegendText.setText("P3 (" + PointList[thirdPoint][0] + ", " + PointList[thirdPoint][1] + ")", null, null);
                	        			
        			src.highlight(6);
        			lang.nextStep();
        			src.unhighlight(6);
        			
        			
//					Berechnung Umkreis
					int a1 = PointList[firstPoint][0], a2 = PointList[firstPoint][1];
					int b1 = PointList[secondPoint][0], b2 = PointList[secondPoint][1];
					int c1 = PointList[thirdPoint][0], c2 = PointList[thirdPoint][1];
					
					int a1s = a1 - c1; a1sText.setText("a1s: " + PointList[firstPoint][0] + " - " + PointList[thirdPoint][0] + " = " + a1s, null, null);
					int b1s = b1 - c1; b1sText.setText("b1s: " + PointList[secondPoint][0] + " - " + PointList[thirdPoint][0] + " = " + b1s, null, null);
					int a2s = a2 - c2; a2sText.setText("a2s: " + PointList[firstPoint][1] + " - " + PointList[thirdPoint][1] + " = " + a2s, null, null);
					int b2s = b2 - c2; b2sText.setText("b2s: " + PointList[secondPoint][1] + " - " + PointList[thirdPoint][1] + " = " + b2s, null, null);
					
					Coordinates[] tempNodes = new Coordinates[2];
					tempNodes[0] = new Coordinates(absPosPointList[thirdPoint].x, absPosPointList[firstPoint].y);
					tempNodes[1] = new Coordinates(absPosPointList[firstPoint].x, absPosPointList[firstPoint].y);
					
					Polyline a1vector = lang.newPolyline(tempNodes, "a1vector", null, vectorProps);
					Text a1text = lang.newText(new Coordinates((absPosPointList[thirdPoint].x + absPosPointList[firstPoint].x) / 2 - 10, absPosPointList[firstPoint].y), "a1s", "a1stext", null);

					tempNodes[0] = new Coordinates(absPosPointList[thirdPoint].x, absPosPointList[firstPoint].y);
					tempNodes[1] = new Coordinates(absPosPointList[thirdPoint].x, absPosPointList[thirdPoint].y);
					
					Polyline b1vector = lang.newPolyline(tempNodes, "a1vector", null, vectorProps);
					Text b1text = lang.newText(new Coordinates(absPosPointList[thirdPoint].x + 4, (absPosPointList[thirdPoint].y + absPosPointList[firstPoint].y) / 2 - 5), "a2s", "b1stext", null);

					
					src.highlight(8); src.highlight(9); 
					lang.nextStep();
					src.unhighlight(8); src.unhighlight(9); 
					
					a1vector.hide(); a1text.hide();
					b1vector.hide(); b1text.hide();
					
					
					tempNodes[0] = new Coordinates(absPosPointList[thirdPoint].x, absPosPointList[secondPoint].y);
					tempNodes[1] = new Coordinates(absPosPointList[secondPoint].x, absPosPointList[secondPoint].y);
					
					a1vector = lang.newPolyline(tempNodes, "a1vector", null, vectorProps);
					a1text = lang.newText(new Coordinates((absPosPointList[thirdPoint].x + absPosPointList[secondPoint].x) / 2 - 10, absPosPointList[secondPoint].y), "b1s", "a1stext", null);

					tempNodes[0] = new Coordinates(absPosPointList[thirdPoint].x, absPosPointList[secondPoint].y);
					tempNodes[1] = new Coordinates(absPosPointList[thirdPoint].x, absPosPointList[thirdPoint].y);
					
					b1vector = lang.newPolyline(tempNodes, "a1vector", null, vectorProps);
					b1text = lang.newText(new Coordinates(absPosPointList[thirdPoint].x + 4, (absPosPointList[thirdPoint].y + absPosPointList[secondPoint].y) / 2 - 5), "b2s", "b1stext", null);

					
					
					
					
					src.highlight(10); src.highlight(11);
					lang.nextStep();					
					src.unhighlight(10); src.unhighlight(11);
					
					a1vector.hide(); a1text.hide();
					b1vector.hide(); b1text.hide();
					
					src.highlight(13);
					lang.nextStep();						
					src.unhighlight(13);
					
					if(a1s * b2s == a2s * b1s) {						
						src.highlight(14);
						lang.nextStep();
						src.unhighlight(14);
						continue;
					}
					
//					Aktuelles Dreieck
					currentTriangle = lang.newTriangle(
							Node.convertToNode(absPosPointList[firstPoint]), 
							Node.convertToNode(absPosPointList[secondPoint]), 
							Node.convertToNode(absPosPointList[thirdPoint]), 
							"triangle", null, Current_Triangle_Props);
					
					double centerX = c1 + b1s / 2.0 + b2s * (Math.pow(a1s, 2) + Math.pow(a2s, 2) - a1s * b1s - a2s * b2s) / (2.0 * (a1s * b2s - a2s * b1s));
					double centerY = c2 + b2s / 2.0 - b1s * (Math.pow(a1s, 2) + Math.pow(a2s, 2) - a1s * b1s - a2s * b2s) / (2.0 * (a1s * b2s - a2s * b1s)); 
				
					double radius = Math.sqrt(Math.pow(a1 - centerX, 2) + Math.pow( a2 - centerY, 2));
					
					umkreisLegendCenterText.setText("Mittelpunkt: (" + Math.round(centerX * 10) / 10.0 + ", " + Math.round(centerY * 10) / 10.0 + ")", null, null);
					umkreisLegendRadiusText.setText("Radius: " + Math.round(radius * 10) / 10.0, null, null);
	            	
					
					Ellipse2D umkreis = new Ellipse2D.Double(centerX - radius, centerY - radius, radius * 2, radius * 2);
					
//					Umkreis
					Circle circumCircle = lang.newCircle(new Offset(
							(int) Math.round(1.0 * (centerX - minX) / (maxX - minX) * widthDrawingArea), 
							(int) Math.round(1.0 * (centerY - minY) / (maxY - minY) * heightDrawingArea), 
							"drawingArea", AnimalScript.DIRECTION_NW), 
						(int) Math.ceil((1.0 * radius / (maxX - minX) * widthDrawingArea) ), "namenkreis", null, Circum);
					
					src.highlight(16); src.highlight(17); src.highlight(18); src.highlight(19);
					lang.nextStep();
					src.unhighlight(16); src.unhighlight(17); src.unhighlight(18); src.unhighlight(19);
										
					boolean isPointinCircle = false;
					
//					Bei allen Punkten testen, ob diese im Umkreis sind
					for (int i = 0; i < PointList.length; i++) {
						
						pointForLegendText.setText("actPoint (" + PointList[i][0] + ", " + PointList[i][1] + ")", null, null);
	                	
						circleList[i].hide();
			        	circleList[i] = lang.newCircle(circleList[i].getCenter(), circleList[i].getRadius(), "", null, Point4_Props);
			        	
						src.highlight(22);
						lang.nextStep();						
						src.unhighlight(22);
						
						
						src.highlight(24);						
						lang.nextStep();
						src.unhighlight(24);
						
						if(i == firstPoint || i == secondPoint || i == thirdPoint) {
							
							src.highlight(25);
							lang.nextStep();
							src.unhighlight(25);
							
							if(i == firstPoint) {
								circleList[i].hide();
					        	circleList[i] = lang.newCircle(circleList[i].getCenter(), circleList[i].getRadius(), "", null, Point1_Props);
					        } else if(i == secondPoint) {
					        	circleList[i].hide();
					        	circleList[i] = lang.newCircle(circleList[i].getCenter(), circleList[i].getRadius(), "", null, Point2_Props);
					        } else {
					        	circleList[i].hide();
					        	circleList[i] = lang.newCircle(circleList[i].getCenter(), circleList[i].getRadius(), "", null, Point3_Props);
					        }
							
							continue;
						}
						
						src.highlight(27);						
						lang.nextStep();						
						src.unhighlight(27);
						
						if(umkreis.contains(PointList[i][0], PointList[i][1])) {
							
							src.highlight(28); src.highlight(29);
							lang.nextStep();
							src.unhighlight(28); src.unhighlight(29);
							
							isPointinCircle = true;
							
							circleList[i].hide();
				        	circleList[i] = lang.newCircle(circleList[i].getCenter(), circleList[i].getRadius(), "", null, Other_Points_Props);
				        	
				        	break;
						}
						
						circleList[i].hide();
			        	circleList[i] = lang.newCircle(circleList[i].getCenter(), circleList[i].getRadius(), "", null, Other_Points_Props);
			        
					}
					
					src.highlight(32);
					lang.nextStep();
					src.unhighlight(32);
					
					circumCircle.hide();
					
					// finished loop - cleanUp
					circleList[thirdPoint].hide();
		        	circleList[thirdPoint] = lang.newCircle(circleList[thirdPoint].getCenter(), circleList[thirdPoint].getRadius(), "", null, Other_Points_Props);
		        	
					if(isPointinCircle) {
						
						lang.nextStep("Dreieck verworfen: (" + PointList[firstPoint][0] + "|" + PointList[firstPoint][1] + "), (" + PointList[secondPoint][0] + "|" + PointList[secondPoint][1] + "), (" + PointList[thirdPoint][0] + "|" + PointList[thirdPoint][1] + ")   ");
						currentTriangle.hide();
					} else {
						currentTriangle.hide();
//						Fertiges Dreieck gefunden
						Triangle finishedTriangle = lang.newTriangle(
								Node.convertToNode(absPosPointList[firstPoint]), 
								Node.convertToNode(absPosPointList[secondPoint]), 
								Node.convertToNode(absPosPointList[thirdPoint]), 
								"trianglefinished", null, Finished_Triangle_Props);
						
						src.highlight(33);
						lang.nextStep("Dreieck gefunden: (" + PointList[firstPoint][0] + "|" + PointList[firstPoint][1] + "), (" + PointList[secondPoint][0] + "|" + PointList[secondPoint][1] + "), (" + PointList[thirdPoint][0] + "|" + PointList[thirdPoint][1] + ")   ");
						src.unhighlight(33);
						
						results.add(new int[] {firstPoint, secondPoint, thirdPoint});
					}
						
				}
        		
        		circleList[secondPoint].hide();
	        	circleList[secondPoint] = lang.newCircle(circleList[secondPoint].getCenter(), circleList[secondPoint].getRadius(), "", null, Other_Points_Props);
	        	
        	}
        	
        	circleList[firstPoint].hide();
        	circleList[firstPoint] = lang.newCircle(circleList[firstPoint].getCenter(), circleList[firstPoint].getRadius(), "", null, Other_Points_Props);
        	
        }
        
        lang.nextStep();
        lang.hideAllPrimitives();
        header.show(); headlineRect.show();

        for (int i = 0; i < finallyText.length; i++) {
        	finallyText[i].show();
		}
        lang.nextStep("Schlussbemerkung");
    }
    
	public String getName() {
        return "Delaunay-Triangulation";
    }

    public String getAlgorithmName() {
        return "Delaunay-Triangulation";
    }

    public String getAnimationAuthor() {
        return "Ralf Rurainsky";
    }

    public String getDescription(){
        return "Die Delaunay-Triangulation ist ein gebr&auml;uchliches Verfahren, um aus einer Punktemenge ein Dreiecksnetz zu erstellen.\n\n"
        			+"Mit dem Verfahren der Delaunay-Triangulation werden Punkte im R^2 so zu Dreiecken vernetzt, dass innerhalb des Kreises, "
        			+ "auf dem die drei Dreieckspunkte liegen, keine anderen Punkte enthalten sind.\n"
        			+"Man verwendet das Verfahren zum Beispiel zur Optimierung von Berechnungsnetzen f&uuml;r die Finite-Elemente-Methode.";
    }

    public String getCodeExample(){
        return "F&uuml;r jedes Dreieck t\n"
			 +"     Berechne den Umkreis u anhand der 3 Eckpunkte des Dreiecks\n\n"
			 +"     F&uuml;r alle gegebenen Punkte p\n"
			 +"          Teste, ob der aktuelle Punkt Teil des Dreiecks t ist\n"
			 +"               Falls ja, &uuml;berspringe diesen Fall\n\n"
			 +"          &uuml;berpr&uuml;fe, ob p sich innerhalb des Umkreises u befindet\n"
			 +"               Falls ja, verwerfe das Dreieck t, da die Bedingungen verletzt werden und fahre mit dem n&auml;chsten Dreieck fort\n\n"
			 +"     Falls kein Punkt innerhalb des Umkreises u gefunden wurde, f&uuml;ge das Dreieck der Ergebnismenge hinzu\n\n";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }
    
	@Override
	public boolean validateInput(AnimationPropertiesContainer primitives,
			Hashtable<String, Object> props) throws IllegalArgumentException {

		PointList = (int[][]) props.get("PointList");
		if(PointList.length < 3)
			throw new IllegalArgumentException("Es müssen mindestens 3 Punkte angegeben werden.\nAngegebene Punkte: " + PointList.length);

		if(PointList[0].length != 2)
			throw new IllegalArgumentException("Die PointList muss aus genau 2 Spalten bestehen (1. Spalte = X-Koordinaten, 2. Spalte = Y-Koordinaten");
		
		return true;
	}
    
}