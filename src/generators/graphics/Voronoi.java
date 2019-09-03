package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Set;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.Circle;
import algoanim.primitives.Polygon;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolygonProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.items.ColorPropertyItem;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class Voronoi implements ValidatingGenerator {

	private Language lang;
	
    private PolygonProperties Half_Plane_Current_Point_Props, Voronoi_Region_Current_Point, Voronoi_Region_Finished_Point_Props;
    private TextProperties Description, Legend_Text, Headline;
    private PolylineProperties Perpendicular_Bisector_Props;
    private int[][] PointList;
    private CircleProperties Current_Point_Props;
    private SourceCodeProperties Source_code_Props;
    private RectProperties Headline_Border_Props, Point_Rectangle_Props, Legend_Rectangle_Props;
    private CircleProperties Standard_Point_Props, Second_Point_Props;
    private double Transparency_Rate_Percental;
    private boolean Transparency_Voronoi_Regions;

    private java.awt.Point[] absPosPointList;
    private Circle[] circleList;
    
    private int minX, minY, maxX, maxY;

    private int startDrawingAreaX = 20, startDrawingAreaY = 70;
    private int startLegendX = 20, startLegendY = 390;
    private int widthDrawingArea = 300, heightDrawingArea = 200;
    
    private double leftTopX = startDrawingAreaX, leftTopY = startDrawingAreaY;
	private double leftBottomX = startDrawingAreaX, leftBottomY = startDrawingAreaY + heightDrawingArea;
	private double rightTopX = startDrawingAreaX + widthDrawingArea, rightTopY = startDrawingAreaY;
	private double rightBottomX = startDrawingAreaX + widthDrawingArea, rightBottomY = startDrawingAreaY + heightDrawingArea;
	

    private SourceCode src;
    private SourceCodeProperties sourceCodeProps;
    
	
	@Override
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		
		Description = (TextProperties)props.getPropertiesByName("Description");
        
		PointList = (int[][])primitives.get("PointList");
        
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
                
        Voronoi_Region_Current_Point = (PolygonProperties)props.getPropertiesByName("Calculating_Voronoi_Region");
        Voronoi_Region_Current_Point.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
        
        Voronoi_Region_Finished_Point_Props = (PolygonProperties)props.getPropertiesByName("Finished_Voronoi_Region");
        Voronoi_Region_Finished_Point_Props.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
        
        Current_Point_Props = (CircleProperties)props.getPropertiesByName("Current_Point");
        Second_Point_Props = (CircleProperties)props.getPropertiesByName("Second_Point");
        Standard_Point_Props = (CircleProperties)props.getPropertiesByName("Standard_Point");
        Source_code_Props = (SourceCodeProperties)props.getPropertiesByName("Source_code");
        
        Legend_Rectangle_Props = (RectProperties)props.getPropertiesByName("Legend_Rectangle");
        Legend_Rectangle_Props.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 20);
        
        Legend_Text = (TextProperties)props.getPropertiesByName("Text_in_Legend");
        
        Headline_Border_Props = (RectProperties)props.getPropertiesByName("Headline_Rectangle");
        Headline_Border_Props.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
        
        Headline = (TextProperties)props.getPropertiesByName("Headline");
        
        Point_Rectangle_Props = (RectProperties)props.getPropertiesByName("Drawing_Rectangle");
        Point_Rectangle_Props.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 20);
        
        
		Half_Plane_Current_Point_Props = (PolygonProperties)props.getPropertiesByName("Current_Half_Plane");
		Half_Plane_Current_Point_Props.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
		
		Perpendicular_Bisector_Props = (PolylineProperties)props.getPropertiesByName("Perpendicular_Bisector");
       	
		Transparency_Rate_Percental = (double) primitives.get("Transparency_Rate");
		Transparency_Rate_Percental = Math.sqrt(Transparency_Rate_Percental);
		Transparency_Voronoi_Regions = (boolean) primitives.get("Transparency_Voronoi_Regions");
		
		Color backPolygonColor = (Color) ((ColorPropertyItem) Half_Plane_Current_Point_Props.getItem("fillColor")).get();
		Color frontPolygonColor = (Color) ((ColorPropertyItem) Voronoi_Region_Current_Point.getItem("fillColor")).get();
		Color transparentColor = new Color(
				(int) (backPolygonColor.getRed() * (1.0 - Transparency_Rate_Percental) + frontPolygonColor.getRed() * Transparency_Rate_Percental), 
				(int) (backPolygonColor.getGreen() * (1.0 - Transparency_Rate_Percental) + frontPolygonColor.getGreen() * Transparency_Rate_Percental), 
				(int) (backPolygonColor.getBlue() * (1.0 - Transparency_Rate_Percental) + frontPolygonColor.getBlue() * Transparency_Rate_Percental)
				);

        absPosPointList = new java.awt.Point[PointList.length];
        circleList = new Circle[PointList.length];
		
		minX = PointList[0][0]; maxX = PointList[0][0];
        minY = PointList[0][1]; maxY = PointList[0][1];
        
        for (int i = 1; i < PointList.length; i++) {
			if(minX > PointList[i][0]) minX = PointList[i][0];
			if(minY > PointList[i][1]) minY = PointList[i][1];
			if(maxX < PointList[i][0]) maxX = PointList[i][0];
			if(maxY < PointList[i][1]) maxY = PointList[i][1];
		}
        minX -= (maxX - minX) / 3;
        maxX += (maxX - minX) / 3;
        minY -= (maxY - minY) / 3;
        maxY += (maxY - minY) / 3;
        
        int diffX = maxX - minX, diffY = maxY - minY;

        // Skalierung der Zeichenfl�che in Abh�ngigkeit der Eingabedaten
        widthDrawingArea = (int) (diffX * 300.0 / 280.0);
        heightDrawingArea = (int) (diffY * 200.0 / 245.0);
        
        startLegendY = startDrawingAreaY + 20 + heightDrawingArea;

        leftTopX = startDrawingAreaX; leftTopY = startDrawingAreaY;
    	leftBottomX = startDrawingAreaX; leftBottomY = startDrawingAreaY + heightDrawingArea;
    	rightTopX = startDrawingAreaX + widthDrawingArea; rightTopY = startDrawingAreaY;
    	rightBottomX = startDrawingAreaX + widthDrawingArea; rightBottomY = startDrawingAreaY + heightDrawingArea;
    	
        lang.setStepMode(true);
        
        TextProperties headerProps = new TextProperties();
        headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.SANS_SERIF, Font.BOLD, 24));
        headerProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Headline.get("color"));
        
        sourceCodeProps = new SourceCodeProperties();
        sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
            Font.MONOSPACED, Font.PLAIN, 12));
        sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        
        RectProperties drawingProps = new RectProperties();
        drawingProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
        
        CircleProperties initCircleProps = new CircleProperties();
        initCircleProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        initCircleProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.black);
        
        PolygonProperties voroPolyProps = Voronoi_Region_Current_Point;
        voroPolyProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
              
        Text header = lang.newText(new Coordinates(20, 30), "Voronoi-Diagramme", "header", null, headerProps);
        
        Rect headlineRect = lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
                null, Headline_Border_Props);
        
        Text[] algorithmDescription = new Text[10];
        algorithmDescription[0] = lang.newText(new Coordinates(20, 60), "Als Voronoi-Diagramm wird eine Zerlegung des Raumes in Regioen", "algoDescription1", null, Description);
//        algorithmDescription[0] = lang.newText(new Coordinates(20, 70), "Als Voronoi-Diagramm wird eine Zerlegung des Raumes in Regioen", "algoDescription1", null, Description);
        algorithmDescription[1] = lang.newText(new Coordinates(20, 85), "bezeichnet, die durch eine vorgegebene Menge an Punkten des", "algoDescription2", null, Description);
        algorithmDescription[2] = lang.newText(new Coordinates(20, 100), "Raumes, auch Zentren genannt, bestimmt werden. Jede Region wird", "algoDescription3", null, Description);
        algorithmDescription[3] = lang.newText(new Coordinates(20, 115), "durch genau ein Zentrum bestimmt und umfasst alle Punkte des", "algoDescription4", null, Description);
        algorithmDescription[4] = lang.newText(new Coordinates(20, 130), "Raumes, die in Bezug zur euklidischen Metrik näher an dem", "algoDescription5", null, Description);
        algorithmDescription[5] = lang.newText(new Coordinates(20, 145), "Zentrum der Region liegen als an jedem anderen Zentrum.", "algoDescription6", null, Description);
        algorithmDescription[6] = lang.newText(new Coordinates(20, 175), "Diese Regionen werden auch als Voronoi-Region bezeichnet.", "algoDescription7", null, Description);
        algorithmDescription[7] = lang.newText(new Coordinates(20, 190), "Aus allen Punkten, die mehr als ein nächstgelegenes Zentrum", "algoDescription8", null, Description);
        algorithmDescription[8] = lang.newText(new Coordinates(20, 205), "besitzen und somit die Grenzen der Regionen bilden, ensteht das", "algoDescription9", null, Description);
        algorithmDescription[9] = lang.newText(new Coordinates(20, 220), "Voronoi-Diagramm.", "algoDescription10", null, Description);
		
        Text[] finallyText = new Text[9];
//        finallyText[0] = lang.newText(new Coordinates(20, 60), "Das vorgestellte Verfahren berechnet die Voronoi-Regionen", "finallyText1", null, Description);
        finallyText[0] = lang.newText(new Coordinates(20, 70), "Das vorgestellte Verfahren berechnet die Voronoi-Regionen", "finallyText1", null, Description);
        finallyText[1] = lang.newText(new Coordinates(20, 85), "nach der mathematischen Definition. Die Komplexität liegt bei", "finallyText2", null, Description);
        finallyText[2] = lang.newText(new Coordinates(20, 100), "O(n^2), da für jeden Punkt sämtliche Halbebenen zu allen", "finallyText3", null, Description);
        finallyText[3] = lang.newText(new Coordinates(20, 115), "anderen Punkten berechnet werden müssen.", "finallyText4", null, Description);
        finallyText[4] = lang.newText(new Coordinates(20, 145), "In der Praxis werden allerdings effizientere Algorithmen wie", "finallyText5", null, Description);
        finallyText[5] = lang.newText(new Coordinates(20, 160), "der Fortune's Algorithmus (O(n * log(n)) verwendet. Wenn zu", "finallyText6", null, Description);
        finallyText[6] = lang.newText(new Coordinates(20, 175), "der gegebenen Punktmenge eine Delaunay-Triangulation", "finallyText7", null, Description);
        finallyText[7] = lang.newText(new Coordinates(20, 190), "vorliegt, kann ebenso die Dualität zwischen der Delaunay-", "finallyText8", null, Description);
        finallyText[8] = lang.newText(new Coordinates(20, 205), "Triangulation und des Voronoi-Diagramms ausgenutzt werden.", "finallyText9", null, Description);
        
        for (int i = 0; i < finallyText.length; i++) {
  	      finallyText[i].hide();
  		}
        
		lang.nextStep("Einführung");
		for (int i = 0; i < algorithmDescription.length; i++) {
			algorithmDescription[i].hide();
		}
		
		Rect drawingArea = lang.newRect(
        		new Coordinates(startDrawingAreaX, startDrawingAreaY), 
        		new Coordinates(startDrawingAreaX + widthDrawingArea, startDrawingAreaY + heightDrawingArea),
                "drawingArea", null, Point_Rectangle_Props);
        
//      Legende
        Rect legendRect = lang.newRect(
        		new Coordinates(startLegendX, startLegendY), 
        		new Coordinates(startLegendX + 300, startLegendY + 150), 
        		"legendRect", null, Legend_Rectangle_Props);
		
        
        Circle point1Legend = lang.newCircle(new Offset(10, 9, "legendRect", AnimalScript.DIRECTION_NW), 3, "pL1", null, Current_Point_Props);
        Circle point2Legend = lang.newCircle(new Offset(10, 24, "legendRect", AnimalScript.DIRECTION_NW), 3, "pL2", null, Second_Point_Props);
        Text point1LegendText = lang.newText(new Offset(20,  0, "legendRect", AnimalScript.DIRECTION_NW), "Punkt (i)", "randomText2", null, Legend_Text);
        Text point2LegendText = lang.newText(new Offset(20, 15, "legendRect", AnimalScript.DIRECTION_NW), "Punkt (j)", "randomText1", null, Legend_Text);
                
        Node[] vrcpNodes = new Node[4];
        vrcpNodes[0] = new Coordinates(5 + startLegendX, 33 + startLegendY);
        vrcpNodes[1] = new Coordinates(15 + startLegendX, 33 + startLegendY);
        vrcpNodes[2] = new Coordinates(15 + startLegendX, 43 + startLegendY);
        vrcpNodes[3] = new Coordinates(5 + startLegendX, 43 + startLegendY);
        
        Text voronoiRegionText = lang.newText(new Offset(20, 30, "legendRect", AnimalScript.DIRECTION_NW), "Voronoi-Region (r) des Punktes (i)", "randomText3", null, Legend_Text);
    	        try {
			Polygon vrcpPolygon = lang.newPolygon(vrcpNodes, "vrcpPolygon", null, Voronoi_Region_Current_Point);
		} catch (NotEnoughNodesException e1) { e1.printStackTrace(); }
        
        int legendCountY = 45;
        
        if(!(boolean) Half_Plane_Current_Point_Props.get("hidden")) {
        	
        	Text halfPlaneText = lang.newText(new Offset(20, legendCountY, "legendRect", AnimalScript.DIRECTION_NW), "Halbebene (h)", "randomText4", null, Legend_Text);
                    	
        	Node[] halfPlaneNodes = new Node[4];
            halfPlaneNodes[0] = new Coordinates(5 + startLegendX, legendCountY + 3 + startLegendY);
            halfPlaneNodes[1] = new Coordinates(15 + startLegendX, legendCountY + 3 + startLegendY);
            halfPlaneNodes[2] = new Coordinates(15 + startLegendX, legendCountY + 13 + startLegendY);
            halfPlaneNodes[3] = new Coordinates(5 + startLegendX, legendCountY + 13 + startLegendY);
            try {
    			Polygon halfPlanePolygon = lang.newPolygon(halfPlaneNodes, "halfPlanePolygon", null, Half_Plane_Current_Point_Props);
    		} catch (NotEnoughNodesException e1) { e1.printStackTrace(); }
            
            legendCountY += 15;
        	
        }
        
        
        if(Transparency_Voronoi_Regions) {
        	Text intersectText = lang.newText(new Offset(20, legendCountY, "legendRect", AnimalScript.DIRECTION_NW), "Durchschnitt aus (r) und (h)", "randomText4", null, Legend_Text);
            
	        Node[] intersectionNodes = new Node[4];
	        intersectionNodes[0] = new Coordinates(5 + startLegendX, legendCountY + 3 + startLegendY);
	        intersectionNodes[1] = new Coordinates(15 + startLegendX, legendCountY + 3 + startLegendY);
	        intersectionNodes[2] = new Coordinates(15 + startLegendX, legendCountY + 13 + startLegendY);
	        intersectionNodes[3] = new Coordinates(5 + startLegendX, legendCountY + 13 + startLegendY);
	        try {
				Polygon intersectionPolygon = lang.newPolygon(intersectionNodes, "intersectionPolygon", null, Voronoi_Region_Current_Point);
				intersectionPolygon.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, transparentColor, null, null);
			} catch (NotEnoughNodesException e1) { e1.printStackTrace(); }
	        
	        legendCountY += 15;
        }
        
        if(!(boolean) Voronoi_Region_Finished_Point_Props.get("hidden")) {
        	
        	Text voronoiFinishedText = lang.newText(new Offset(20, legendCountY, "legendRect", AnimalScript.DIRECTION_NW), "fertig berechnete Voronoi-Region", "randomText6", null, Legend_Text);
                    	
        	Node[] vFNodes = new Node[4];
        	vFNodes[0] = new Coordinates(5 + startLegendX, legendCountY + 3 + startLegendY);
        	vFNodes[1] = new Coordinates(15 + startLegendX, legendCountY + 3 + startLegendY);
        	vFNodes[2] = new Coordinates(15 + startLegendX, legendCountY + 13 + startLegendY);
        	vFNodes[3] = new Coordinates(5 + startLegendX, legendCountY + 13 + startLegendY);
            try {
    			Polygon vFPolygon = lang.newPolygon(vFNodes, "halfPlanePolygon", null, Voronoi_Region_Finished_Point_Props);
    		} catch (NotEnoughNodesException e1) { e1.printStackTrace(); }
            
            legendCountY += 15;
        	
        }
        
        
        
		src = lang.newSourceCode(new Coordinates(Math.max(70 + widthDrawingArea, 390), 10), "sourceCode", null, Source_code_Props);
        src.addCodeLine("Berechnung des Voronoi-Diagramms anhand der gegebenen Punkte:", null, 0, null); // 0
        src.addCodeLine("", null, 1, null); // 1
        src.addCodeLine("Für jeden Punkt (i) {", null, 1, null); // 2
        src.addCodeLine("", null, 1, null); // 3
        src.addCodeLine("Initialisierung der Voronoi-Region (r) des Punktes (i) mit der gesamten Ebene", null, 2, null); // 4
        src.addCodeLine("", null, 2, null); // 5
        src.addCodeLine("Für jeden anderen Punkt (j) {", null, 2, null); // 6
        src.addCodeLine("", null, 3, null); // 7
        src.addCodeLine("Berechne die Mittelsenkrechte der Punkte (i) und (j)", null, 3, null); // 8
        src.addCodeLine("Berechne die Halbebene (h) des Punktes (i) anhand der Mittelsenkrechten", null, 3, null); // 9
        src.addCodeLine("", null, 3, null); // 10
        src.addCodeLine("Ermittle den Durchschnitt aus (r) und (h)", null, 3, null); // 11
        src.addCodeLine("und setze das Ergebnis als neue Voronoi-Region (r).", null, 3, null); // 12
        src.addCodeLine("}", null, 2, null); // 13
        src.addCodeLine("füge (r) der Ergebnisliste hinzu", null, 2, null); // 14
        src.addCodeLine("}", null, 1, null); // 15
        
        
        // Zeichne Eingabedaten (= Punkte) in das Zeichenfeld
        for (int i = 0; i < PointList.length; i++) {
			int offsetX = (int) (1.0 * (PointList[i][0] - minX) / (maxX - minX) * widthDrawingArea);
			int offsetY = (int) (1.0 * (PointList[i][1] - minY) / (maxY - minY) * heightDrawingArea);
			
			circleList[i] = lang.newCircle(
					new Offset(offsetX, offsetY, "drawingArea", AnimalScript.DIRECTION_NW), 
					3, 
					"point" + i,
					null, Standard_Point_Props);
			
			absPosPointList[i] = new java.awt.Point(offsetX + startDrawingAreaX, offsetY + startDrawingAreaY);

        }
        
        
        src.highlight(0);
        lang.nextStep();
        src.unhighlight(0);
        
		// absPosPointList[i] ist der Punkt, dessen Voronoi-Region aktuell berechnet werden soll
        for (int i = 0; i < PointList.length; i++) {
        	
        	
        	
        	// Beinhaltet die Kanten, die die Voronoi-Region begrenzen, anfangs die komplette angezeigte Fl�che, im Laufe des Algorithmus wird diese immer weiter eingegrenzt
        	ArrayList<double[]> voronoiLinesResult = new ArrayList<double[]>();
        	
        	// DrawingArea f�r korrekte Behandlung der Regionen am Rand, f�r die in der Delaunay-Triangulation keine Dreiecke existieren
        	voronoiLinesResult.add(new double[] {startDrawingAreaX, startDrawingAreaY, startDrawingAreaX, startDrawingAreaY + heightDrawingArea}); // links
    		voronoiLinesResult.add(new double[] {startDrawingAreaX + widthDrawingArea, startDrawingAreaY, startDrawingAreaX + widthDrawingArea, startDrawingAreaY + heightDrawingArea}); // rechts
    		voronoiLinesResult.add(new double[] {startDrawingAreaX, startDrawingAreaY, startDrawingAreaX + widthDrawingArea, startDrawingAreaY}); // oben
    		voronoiLinesResult.add(new double[] {startDrawingAreaX, startDrawingAreaY + heightDrawingArea, startDrawingAreaX + widthDrawingArea, startDrawingAreaY + heightDrawingArea}); // unten
    		
        				
        	circleList[i].hide();
        	circleList[i] = lang.newCircle(circleList[i].getCenter(), circleList[i].getRadius(), "", null, Current_Point_Props);
        	
        	src.highlight(2);
        	lang.nextStep();
        	src.unhighlight(2);
        	       	
        	// Liste an Punkten, die bei der Berechnung der Voronoi-Region schon erledigt sind
    		ArrayList<java.awt.Point> pointsDone = new ArrayList<java.awt.Point>();
    		
    		
    		Polygon voroRegionPolygon = null;
    		try {
    			voroRegionPolygon = lang.newPolygon(getPolygonPointsinNodes(voronoiLinesResult), "voroRegionPolygon", null, voroPolyProps);
			} catch (NotEnoughNodesException e) {
				e.printStackTrace();
			}
    		
    		src.highlight(4);
    		lang.nextStep();
    		src.unhighlight(4);
    		
    		for (int j = 0; j < PointList.length; j++) {
    			
    			
    			if(j == i)
    				continue;
    			
    			circleList[j].hide();
            	circleList[j] = lang.newCircle(circleList[j].getCenter(), circleList[j].getRadius(), "", null, Second_Point_Props);

    			src.highlight(6);
    			lang.nextStep();
    			src.unhighlight(6);
    			
    			// Die Kanten dieser ArrayList teilen die gezeichnete Fl�che in die Punkte, 
    			// die n�her an absPosPointList[i] liegen, und in jene, die n�her an absPosPointList[j] liegen.
            	ArrayList<double[]> voronoiLines = new ArrayList<double[]>();
            	
            	voronoiLines.add(new double[] {startDrawingAreaX, startDrawingAreaY, startDrawingAreaX, startDrawingAreaY + heightDrawingArea}); // links
        		voronoiLines.add(new double[] {startDrawingAreaX + widthDrawingArea, startDrawingAreaY, startDrawingAreaX + widthDrawingArea, startDrawingAreaY + heightDrawingArea}); // rechts
        		voronoiLines.add(new double[] {startDrawingAreaX, startDrawingAreaY, startDrawingAreaX + widthDrawingArea, startDrawingAreaY}); // oben
        		voronoiLines.add(new double[] {startDrawingAreaX, startDrawingAreaY + heightDrawingArea, startDrawingAreaX + widthDrawingArea, startDrawingAreaY + heightDrawingArea}); // unten
        		
    			pointsDone.add(absPosPointList[j]); 
    			
    			// Berechnung der Grenze der zwei Halbr�ume zwischen absPosPointList[j] und absPosPointList[i]
    			ArrayList<double[]> halfLine = getHalfline(absPosPointList[i].getX(), absPosPointList[i].getY(), absPosPointList[j].getX(), absPosPointList[j].getY());
    			
    			Polyline pl = lang.newPolyline(new Node[] {new Coordinates((int) halfLine.get(0)[0], (int) halfLine.get(0)[1]), new Coordinates((int) halfLine.get(1)[0], (int) halfLine.get(1)[1])}, "polyllinne", null, Perpendicular_Bisector_Props);
    			src.highlight(8);
    			lang.nextStep();
    			src.unhighlight(8);
    			pl.hide();
    			
    			
    			ArrayList<double[]> vLnew = new ArrayList<double[]>();
    			for (int k = 0; k < voronoiLines.size(); k++) {
    				ArrayList<double[]> clipLines = clipLines(voronoiLines.get(k)[0], voronoiLines.get(k)[1], voronoiLines.get(k)[2], voronoiLines.get(k)[3], halfLine.get(0)[0], halfLine.get(0)[1], halfLine.get(1)[0], halfLine.get(1)[1]);
    				
    				if(clipLines.size() != 0)
    					vLnew.addAll(clipLines);
    				else
    					vLnew.add(voronoiLines.get(k));
    			}
    			
    			// doppelte Strecken entfernen
    			for (int k = 0; k < vLnew.size() - 1; k++) {
    				for (int k2 = k + 1; k2 < vLnew.size(); k2++) {
    					if((vLnew.get(k)[0] == vLnew.get(k2)[0] && vLnew.get(k)[1] == vLnew.get(k2)[1] && vLnew.get(k)[2] == vLnew.get(k2)[2] && vLnew.get(k)[3] == vLnew.get(k2)[3]) ||
    							(vLnew.get(k)[0] == vLnew.get(k2)[2] && vLnew.get(k)[1] == vLnew.get(k2)[3] && vLnew.get(k)[2] == vLnew.get(k2)[0] && vLnew.get(k)[3] == vLnew.get(k2)[1])) {
    						vLnew.remove(k2);
    						k2--;
    					}
    				}
    			}

    			voronoiLines = vLnew;
    			
    			// Linien entfernen, deren Distanz zu absPosPointList[i] gr��er als zu absPosPointList[j] ist
    			for (int k = 0; k < voronoiLines.size(); k++) {
    				
    				double distp1a = Math.pow(voronoiLines.get(k)[0] - absPosPointList[j].getX(), 2) + Math.pow(voronoiLines.get(k)[1] - absPosPointList[j].getY(), 2);
    				double distp1b = Math.pow(voronoiLines.get(k)[2] - absPosPointList[j].getX(), 2) + Math.pow(voronoiLines.get(k)[3] - absPosPointList[j].getY(), 2);
    				
    				double distp2a = Math.pow(voronoiLines.get(k)[0] - absPosPointList[i].getX(), 2) + Math.pow(voronoiLines.get(k)[1] - absPosPointList[i].getY(), 2);
    				double distp2b = Math.pow(voronoiLines.get(k)[2] - absPosPointList[i].getX(), 2) + Math.pow(voronoiLines.get(k)[3] - absPosPointList[i].getY(), 2);

    				if(distp1a + distp1b < distp2a + distp2b) {
    					if(Math.abs(distp1a + distp1b - (distp2a + distp2b)) < 1E-4)
    						continue;

    					voronoiLines.remove(k);
    					k--;
    				}
    				
    			}
    			
    			Polygon p = null;
    			try {
					p = lang.newPolygon(getPolygonPointsinNodes(voronoiLines), "tempVoronoiRegion", null, Half_Plane_Current_Point_Props);
				} catch (NotEnoughNodesException e) {
					e.printStackTrace();
				}
    			
    			
//    			Starte Schnittpunktgeneration
    			
    			ArrayList<double[]> tempIntersectList = new ArrayList<double[]>();
    			    			
    			    			
    			tempIntersectList.addAll(voronoiLines);
    			
    			for (int k = 0; k < voronoiLinesResult.size(); k++) {
    				boolean contains = false;
    				for (int k2 = 0; k2 < voronoiLines.size(); k2++) {
    					
        				if(linesEqual(voronoiLinesResult.get(k), voronoiLines.get(k2))) {
        					contains = true;
        					break;
        				}
					}
    				
    				if(!contains)
    					tempIntersectList.add(voronoiLinesResult.get(k));
    				
				}
    			
    			
    			ArrayList<double[]> finalResultList = new ArrayList<double[]>();
	    		
    			while(!tempIntersectList.isEmpty()) {
    			
	    			double[] curLine = tempIntersectList.remove(0);
	    			
	    			for (int k = 0; k < finalResultList.size(); k++) {
						if(linesEqual(curLine, finalResultList.get(k)))
							finalResultList.remove(k);
					}
	    			
	    			
	    			boolean intersects = false;
	    			for (int k = 0; k < tempIntersectList.size(); k++) {
	    				
	    				ArrayList<double[]> clipResult = clipLinesChanges(curLine[0], curLine[1], curLine[2], curLine[3], tempIntersectList.get(k)[0], tempIntersectList.get(k)[1], tempIntersectList.get(k)[2], tempIntersectList.get(k)[3]);
						if(clipResult.size() > 1) {
//							Es entstehen neue Linien
							
							tempIntersectList.remove(k);
							
							for (int l = 0; l < clipResult.size(); l++) {
								boolean found = false;
								
								if(linesEqual(clipResult.get(l), curLine)) {
//									1. Linie ist bereits vorhanden
									
									found = true;
										
									if(clipResult.size() > 2)
										intersects = true;
								} else {
//																		
									for (int l2 = 0; l2 < tempIntersectList.size(); l2++) {
										if(linesEqual(clipResult.get(l), tempIntersectList.get(l2))) {
											found = true;
//											2. Linien ist bereits vorhanden
											break;
										}
									}
								}
								
								
								if(!found) {
//									Noch nicht vorhandene Linie gefunden, muss sp�ter noch ber�cksichtigt werden
									tempIntersectList.add(clipResult.get(l));
									intersects = true;
								}
							}
							
							break;
						} 
					}
	    			
	    			if(!intersects) {
//	    				f�ge curline zur finalResultList hinzu
	    				finalResultList.add(curLine);
	    			} else {
//	    				CurLine muss noch einmal behandelt werden, da Schnitte entstanden sind
	    				tempIntersectList.add(curLine);
	    			}
    			}

//    			Schnitttests fertig    			
    			tempIntersectList = finalResultList;
    			
    			for (int k = 0; k < tempIntersectList.size(); k++) {
    				
    				if(!isPointinPolygon(voronoiLines, tempIntersectList.get(k)[0], tempIntersectList.get(k)[1]) || 
    						!isPointinPolygon(voronoiLines, tempIntersectList.get(k)[2], tempIntersectList.get(k)[3]) || 
    						!isPointinPolygon(voronoiLinesResult, tempIntersectList.get(k)[0], tempIntersectList.get(k)[1]) || 
    						!isPointinPolygon(voronoiLinesResult, tempIntersectList.get(k)[2], tempIntersectList.get(k)[3])) {
//    					die Linie ist nicht Teil der Schnittmenge der Halbebene und der bisher berechneten Voronoi-Region
    					tempIntersectList.remove(k--);
    				}
				}
    			
    			voronoiLinesResult.clear();
    			voronoiLinesResult.addAll(tempIntersectList);
    			
    			Polygon transparencyPolygon = null;
    			
    			if(Transparency_Voronoi_Regions) {
	    			
	    			try {
	    				transparencyPolygon = lang.newPolygon(getPolygonPointsinNodes(voronoiLinesResult), "intersectArea", null, voroPolyProps);
	    				transparencyPolygon.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, transparentColor, null, null);
	    			} catch (NotEnoughNodesException e) {
						e.printStackTrace();
					}
    			}
    			
    			src.highlight(9);
    			lang.nextStep();
    			src.unhighlight(9);
    			p.hide();
    			
    			if(Transparency_Voronoi_Regions)
    				transparencyPolygon.hide();
    			
    			voroRegionPolygon.hide();

    			try {
    				voroRegionPolygon = lang.newPolygon(getPolygonPointsinNodes(voronoiLinesResult), "intersectArea", null, voroPolyProps);

    			} catch (NotEnoughNodesException e) {
					e.printStackTrace();
				}
    			src.highlight(11);
    			src.highlight(12);
    			lang.nextStep();
    			src.unhighlight(11);
    			src.unhighlight(12);
				
    			circleList[j].hide();
            	circleList[j] = lang.newCircle(circleList[j].getCenter(), circleList[j].getRadius(), "", null, Standard_Point_Props);
    			
    		}
    		
//    		Voronoi-Region von Punkt absPosPointList[i] wurde fertig berechnet
        	
    		
    		
    		circleList[i].hide();
        	circleList[i] = lang.newCircle(circleList[i].getCenter(), circleList[i].getRadius(), "", null, Standard_Point_Props);
			
    		Polygon resultPointI = null;
			try {
				resultPointI = lang.newPolygon(getPolygonPointsinNodes(voronoiLinesResult), "intersectArea", null, Voronoi_Region_Finished_Point_Props);
			} catch (NotEnoughNodesException e) {
				e.printStackTrace();
			}

    		src.highlight(14);
			lang.nextStep("Berechnung der Voronoi-Region des Punktes (" + PointList[i][0] + ", " + PointList[i][1] + ") beendet. ");
			src.unhighlight(14);
		}
        
        lang.nextStep(); lang.hideAllPrimitives();
        header.show(); headlineRect.show();
        
        for (int i = 0; i < finallyText.length; i++) {
        	finallyText[i].show();
		}
        
        lang.nextStep("Schlussbemerkung");
        
		return lang.toString();
	}
	
	@Override
	public String getAlgorithmName() {
		return "Voronoi-Diagramm";
	}

	@Override
	public String getAnimationAuthor() {
		return "Ralf Rurainsky";
	}

	@Override
	public String getCodeExample() {
		return "F&uuml;r jeden Punkt (i)\n\n"
				+ "\tInitialisierung der Voronoi-Region (r) des Punktes (i) mit der gesamten Ebene\n\n"
				+ "\tF&uuml;r jeden anderen Punkt (j)\n\n"
				+ "\t\tBerechne die Mittelsenkrechte der Punkte (i) und (j)\n"
				+ "\t\tBerechne die Halbebene (h) des Punktes (i) anhand der Mittelsenkrechten\n\n"
				+ "\t\tErmittle den Durchschnitt aus (r) und (h)\n"
				+ "\t\tund setze das Ergebnis als neue Voronoi-Region (r).\n\n"
				+ "\tf&uuml;ge (r) der Ergebnisliste hinzu";
	}

	@Override
	public Locale getContentLocale() {
		return Locale.GERMAN;
	}

	@Override
	public String getDescription() {
		return "Als Voronoi-Diagramm wird eine Zerlegung des Raumes in Regioen <br>"
				+ "bezeichnet, die durch eine vorgegebene Menge an Punkten des <br>"
				+ "Raumes, auch Zentren genannt, bestimmt werden. Jede Region wird <br>"
				+ "durch genau ein Zentrum bestimmt und umfasst alle Punkte des <br>"
				+ "Raumes, die in Bezug zur euklidischen Metrik n&auml;her an dem <br>"
				+ "Zentrum der Region liegen als an jedem anderen Zentrum.<br>"
				+ "<br>"
				+ "Diese Regionen werden auch als Voronoi-Region bezeichnet. <br>"
				+ "Aus allen Punkten, die mehr als ein n&auml;chstgelegenes Zentrum <br>" 
				+ "besitzen und somit die Grenzen der Regionen bilden, ensteht das <br>"
				+ "Voronoi-Diagramm.";
	}

	@Override
	public String getFileExtension() {
		return "asu";
	}

	@Override
	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
	}

	@Override
	public String getName() {
		return getAlgorithmName();
	}

	@Override
	public String getOutputLanguage() {
		return Generator.PSEUDO_CODE_OUTPUT;
	}

	@Override
	public void init() {
		lang = new AnimalScript("Voronoi-Diagramm", "Ralf Rurainsky", 1000, 600);
	}
	
	public Voronoi() {
		init();
	}
	
	private double[][] getPolygonPoints(ArrayList<double[]> input) {
		double[][] result = new double[input.size()][];
		
		double[] cur = new double[] {input.get(0)[0], input.get(0)[1]};
		
		@SuppressWarnings("unchecked")
		ArrayList<double[]> input2 = (ArrayList<double[]>) input.clone();

		int pointer = 0;
		double[] n = {cur[0], cur[1]};
		
		result[pointer++] = n;
		
		int curIndex = 0;

		for (int i = 0; i < input2.size(); i++) {
			
			if(curIndex == i)
				continue;
			
			if(cur[0] == input2.get(i)[0] && cur[1] == input2.get(i)[1]) {
				curIndex = i;
				cur = new double[] {input2.get(i)[2], input2.get(i)[3]};
				n = new double[] {input2.get(i)[2], input2.get(i)[3]};
				result[pointer++] = n;
				
				i = 0;
			} else if(cur[0] == input2.get(i)[2] && cur[1] == input2.get(i)[3]) {
				curIndex = i;
				cur = new double[] {input2.get(i)[0], input2.get(i)[1]};
				n = new double[] {input2.get(i)[0], input2.get(i)[1]};
				result[pointer++] = n;
				
				i = 0;
			}
			
			if(pointer == input.size())
				break;
		}
		
		if(result.length != pointer)
			throw new RuntimeException("fehlerhafte gr��e des ergebnisarrays");

		return result;
	}
	
	private Node[] getPolygonPointsinNodes(ArrayList<double[]> input) {
		
		double[][] tempResult = getPolygonPoints(input);
		Coordinates[] result = new Coordinates[tempResult.length];
		
		for (int i = 0; i < tempResult.length; i++) {
			result[i] = new Coordinates((int) tempResult[i][0], (int) tempResult[i][1]);
		}

		return result;
	}
	
	private ArrayList<double[]> getHalfline(double x1, double y1, double x2, double y2) {
		
		// Mittelpunkt der Strecke zwischen P1 und P2
		double midPointX = (x1 - x2) / 2.0 + x2;
		double midPointY = (y1 - y2) / 2.0 + y2;
		
		double secPointX, secPointY;
		
		if(y1 == y2) {
			secPointX = midPointX;
			secPointY = midPointY + 1;
		} else {
			double slope = -1.0 * (x2 - x1) / (y2 - y1);
			double t = midPointY - slope * midPointX;
			
			secPointX = midPointX + 1.0 * slope;
			secPointY = slope * secPointX + t;
			
			if(slope == 0) 
				secPointX = midPointX + 1;
			
		}
		
		double[] leftIntersect = getIntersectPoint2(midPointX, midPointY, secPointX, secPointY, leftTopX, leftTopY, leftBottomX, leftBottomY);
		double[] topIntersect = getIntersectPoint2(midPointX, midPointY, secPointX, secPointY, leftTopX, leftTopY, rightTopX, rightTopY);
		double[] rightIntersect = getIntersectPoint2(midPointX, midPointY, secPointX, secPointY, rightTopX, rightTopY, rightBottomX, rightBottomY);
		double[] bottomIntersect = getIntersectPoint2(midPointX, midPointY, secPointX, secPointY, leftBottomX, leftBottomY, rightBottomX, rightBottomY);

		ArrayList<double[]> result = new ArrayList<double[]>();
		
		if(leftIntersect != null && leftIntersect[1] >= leftTopY && leftIntersect[1] <= leftBottomY)
			result.add(leftIntersect);
		if(rightIntersect != null && rightIntersect[1] >= rightTopY && rightIntersect[1] <= rightBottomY)
			result.add(rightIntersect);
		if(topIntersect != null && topIntersect[0] >= leftTopX && topIntersect[0] <= rightTopX)
			result.add(topIntersect);
		if(bottomIntersect != null && bottomIntersect[0] >= leftBottomX && bottomIntersect[0] <= rightBottomX)
			result.add(bottomIntersect);
		
		for (int i = 0; i < result.size() - 1; i++) {
			for (int j = i + 1; j < result.size(); j++) {
				if(result.get(i)[0] == result.get(j)[0] && result.get(i)[1] == result.get(j)[1])
					result.remove(j);
			}
		}
		
		return result;
	
	}

	// Erste Gerade durch Punkte (x1, y1) und (x2, y2)
	// Zweite Gerade durch Punkte (a1, b1) und (a2, b2)
	private double[] getIntersectPoint2(double x1, double y1, double x2, double y2, double a1, double b1, double a2, double b2) {
		
		Line2D line1 = new Line2D.Double(x1, y1, x2, y2);
		Line2D line2 = new Line2D.Double(a1, b1, a2, b2);
				
	    double px = line1.getX1(), py = line1.getY1(), rx = line1.getX2() - px, ry = line1.getY2() - py;
	    double qx = line2.getX1(), qy = line2.getY1(), sx = line2.getX2() - qx, sy = line2.getY2() - qy;
	    double det = sx * ry - sy * rx;
	    
	    if(Math.abs(det) < 1E-4)
	        return null;
	    else {
	    	double z = (sx * (qy - py) + sy * (px - qx)) / det;
	    	
	    	double resX = px + z * rx, resY = py + z * ry;
	    	
	    	// Rechnerungenauigkeit ausgleichen
	    	if(Math.abs(resX - x1) < 1E-4) resX = x1;
	    	else if(Math.abs(resX - x2) < 1E-4) resX = x2;
	    	else if(Math.abs(resX - a1) < 1E-4) resX = a1;
	    	else if(Math.abs(resX - a2) < 1E-4) resX = a2;
	    	
	    	if(Math.abs(resY - y1) < 1E-4) resY = y1;
	    	else if(Math.abs(resY - y2) < 1E-4) resY = y2;
	    	else if(Math.abs(resY - b1) < 1E-4) resY = b1;
	    	else if(Math.abs(resY - b2) < 1E-4) resY = b2;
	    		    		    	
	    	return new double[] { resX, resY};
	    }
	}
	
	private ArrayList<double[]> clipLines(double x1, double y1, double x2, double y2, double a1, double b1, double a2, double b2) {
		
		ArrayList<double[]> resultLines = new ArrayList<double[]>();
		
		
		if((x1 == a1 && y1 == b1 && x2 == a2 && y2 == b2) ||
				(x1 == a2 && y1 == b2 && x2 == a1 && y2 == b1)) {
//			die beiden Linien sind identisch (liegen komplett �bereinander)
			resultLines.add(new double[] {x1, y1, x2, y2});
			return resultLines;
		}


		if(y1 == y2 && y2 == b1 && b1 == b2) {
//			Linien haben alle den selben y-Wert
			
			double xminLine1 = Math.min(x1, x2), xmaxLine1 = Math.max(x1, x2);
			double xminLine2 = Math.min(a1, a2), xmaxLine2 = Math.max(a1, a2);


			if(xminLine2 >= xmaxLine1 || xminLine1 >= xmaxLine2) {
//				Linien schneiden sich nicht oder nur in einem Punkt -> beide Linien unver�ndert zur�ckgeben
				resultLines.add(new double[] {x1, y1, x2, y2});
				resultLines.add(new double[] {a1, b1, a2, b2});
				return resultLines;
			}
			
			if(xminLine1 == xminLine2) {
//				Linien besitzen den gleichen Startpunkt
				
				if(xmaxLine2 > xmaxLine1) {
					resultLines.add(new double[] {xminLine1, y1, xmaxLine1, y2});
					resultLines.add(new double[] {xmaxLine1, b1, xmaxLine2, b2});
					return resultLines;
				}
				if(xmaxLine1 > xmaxLine2) {
					resultLines.add(new double[] {xminLine1, y1, xmaxLine2, y2});
					resultLines.add(new double[] {xmaxLine2, b1, xmaxLine1, b2});
					return resultLines;
				}					
			}
			
			if(xminLine1 < xminLine2 && xminLine2 < xmaxLine1) {
//				Linie 2 startet "in" Linie 1
				
				resultLines.add(new double[] {xminLine1, y1, xminLine2, y1});
				resultLines.add(new double[] {xminLine2, y1, xmaxLine1, y1});
				
				if(xmaxLine1 != xmaxLine2)
					resultLines.add(new double[] {xmaxLine1, y1, xmaxLine2, y1});
				
				return resultLines;				
				
				
			} else if(xminLine2 < xminLine1 && xminLine1 < xmaxLine2) {
//				Linie 1 startet "in" Linie 2
				
				resultLines.add(new double[] {xminLine2, y1, xminLine1, y1});
				resultLines.add(new double[] {xminLine1, y1, xmaxLine2, y1});
				
				if(xmaxLine1 != xmaxLine2)
					resultLines.add(new double[] {xmaxLine1, y1, xmaxLine2, y1});
				
				return resultLines;				
				
			}


		} else if(x1 == x2 && x2 == a1 && a1 == a2) {
//			Linien haben alle den selben x-Wert
			
			double yminLine1 = Math.min(y1, y2), ymaxLine1 = Math.max(y1, y2);
			double yminLine2 = Math.min(b1, b2), ymaxLine2 = Math.max(b1, b2);
			

			if(yminLine2 >= ymaxLine1 || yminLine1 >= ymaxLine2) {
//				Linien schneiden sich nicht oder nur in einem Punkt -> beide Linien unver�ndert zur�ckgeben
				resultLines.add(new double[] {x1, y1, x2, y2});
				resultLines.add(new double[] {a1, b1, a2, b2});
				return resultLines;
			}
			
			if(yminLine1 == yminLine2) {
//				Linien besitzen den gleichen Startpunkt
				
				if(ymaxLine2 > ymaxLine1) {
					resultLines.add(new double[] {x1, yminLine1, x1, ymaxLine1});
					resultLines.add(new double[] {x1, ymaxLine1, x1, ymaxLine2});
					return resultLines;
				}
				if(ymaxLine1 > ymaxLine2) {
					resultLines.add(new double[] {x1, yminLine2, x1, ymaxLine2});
					resultLines.add(new double[] {x1, ymaxLine2, x1, ymaxLine1});
					return resultLines;
				}					
			}


			if(yminLine1 < yminLine2 && yminLine2 < ymaxLine1) {
//				Linie 2 startet "in" Linie 1
				
				resultLines.add(new double[] {x1, yminLine1, x1, yminLine2});
				resultLines.add(new double[] {x1, yminLine2, x1, ymaxLine1});
				
				if(ymaxLine1 != ymaxLine2)
					resultLines.add(new double[] {x1, ymaxLine1, x1, ymaxLine2});
				
				return resultLines;				

			} else if(yminLine2 < yminLine1 && yminLine1 < ymaxLine2) {
//				Linie 1 startet "in" Linie 2
				
				resultLines.add(new double[] {x1, yminLine2, x1, yminLine1});
				resultLines.add(new double[] {x1, yminLine1, x1, ymaxLine2});
				
				if(ymaxLine1 != ymaxLine2)
					resultLines.add(new double[] {x1, ymaxLine1, x1, ymaxLine2});
				
				return resultLines;				
				
			}
			
		}
			
		double[] intersecPoint = getIntersectPoint2(x1, y1, x2, y2, a1, b1, a2, b2);
		
		if(intersecPoint == null || !intersectLines(x1, y1, x2, y2, a1, b1, a2, b2)) {
//			Keine Schnittgeraden, unver�ndert die zwei Geraden zur�ckgeben
			resultLines.add(new double[] {x1, y1, x2, y2});
			resultLines.add(new double[] {a1, b1, a2, b2});
			return resultLines;
		}
		
		if(Math.signum(x1 - x2) == Math.signum(x1 - intersecPoint[0]) && 
				Math.signum(y1 - y2) == Math.signum(y1 - intersecPoint[1]) &&
				Math.signum(x2 - x1) == Math.signum(x2 - intersecPoint[0]) && 
				Math.signum(y2 - y1) == Math.signum(y2 - intersecPoint[1])) {
			
			resultLines.add(new double[] {x1, y1, intersecPoint[0], intersecPoint[1]});
			resultLines.add(new double[] {x2, y2, intersecPoint[0], intersecPoint[1]});
		
		}
		
		
		if(Math.signum(a1 - a2) == Math.signum(a1 - intersecPoint[0]) && 
				Math.signum(b1 - b2) == Math.signum(b1 - intersecPoint[1]) &&
				Math.signum(a2 - a1) == Math.signum(a2 - intersecPoint[0]) && 
				Math.signum(b2 - b1) == Math.signum(b2 - intersecPoint[1])) {

			resultLines.add(new double[] {a1, b1, intersecPoint[0], intersecPoint[1]});

			resultLines.add(new double[] {a2, b2, intersecPoint[0], intersecPoint[1]});
		}
		
		if(intersecPoint[0] == x1 && intersecPoint[1] == y1)
			resultLines.add(new double[] {x2, y2, intersecPoint[0], intersecPoint[1]});
		
		if(intersecPoint[0] == x2 && intersecPoint[1] == y2)
			resultLines.add(new double[] {x1, y1, intersecPoint[0], intersecPoint[1]});
		
		if(intersecPoint[0] == a1 && intersecPoint[1] == b1)
			resultLines.add(new double[] {a2, b2, intersecPoint[0], intersecPoint[1]});
		
		if(intersecPoint[0] == a2 && intersecPoint[1] == b2)
			resultLines.add(new double[] {a1, b1, intersecPoint[0], intersecPoint[1]});
		
		if(resultLines.size() == 0) {
			resultLines.add(new double[] {x1, y1, x2, y2});
			resultLines.add(new double[] {a1, b1, a2, b2});
		}


		return resultLines;
	}
	
	// Schneidet zwei Linien, gibt allerdings nur neu entstandene Linien zur�ck
	private ArrayList<double[]> clipLinesChanges(double x1, double y1, double x2, double y2, double a1, double b1, double a2, double b2) {
		ArrayList<double[]> tempResult = clipLines(x1, y1, x2, y2, a1, b1, a2, b2);
		
		if(tempResult.size() != 2)
			return tempResult;
		
		if((linesEqual(tempResult.get(0), new double[] {x1, y1, x2, y2}) && linesEqual(tempResult.get(1), new double[] {a1, b1, a2, b2})) ||
				(linesEqual(tempResult.get(1), new double[] {x1, y1, x2, y2}) && linesEqual(tempResult.get(0), new double[] {a1, b1, a2, b2}))) {

			return new ArrayList<double[]>();
		}
	
		return tempResult;
	}
	
	private boolean intersectLines(double x1, double y1, double x2, double y2, double a1, double b1, double a2, double b2) {
		boolean res0 = ccw(x1, y1, x2, y2, a1, b1) * ccw(x1, y1, x2, y2, a2, b2) <= 0;
		boolean res1 = ccw(a1, b1, a2, b2, x1, y1) * ccw(a1, b1, a2, b2, x2, y2) <= 0;
		return res0 && res1;
	}
	
	private boolean linesEqual(double[] l1, double[] l2) {
		if(l1.length != 4 || l2.length != 4)
			throw new RuntimeException("Linien m�ssen aus zwei Punkten (= 4 Werten) bestehen");
		
		
		if(l1[0] == l2[0] && l1[1] == l2[1] && l1[2] == l2[2] && l1[3] == l2[3])
			return true;
		
		if(l1[0] == l2[2] && l1[1] == l2[3] && l1[2] == l2[0] && l1[3] == l2[1])
			return true;
				
		double distl1p1l2p1 = Math.pow(l1[0] - l2[0], 2) + Math.pow(l1[1] - l2[1], 2);
		double distl1p2l2p2 = Math.pow(l1[2] - l2[2], 2) + Math.pow(l1[3] - l2[3], 2);
		
		if(distl1p1l2p1 < 1E-4 && distl1p2l2p2 < 1E-4)
			return true;
		
		double distl1p2l2p1 = Math.pow(l1[2] - l2[0], 2) + Math.pow(l1[3] - l2[1], 2);
		double distl1p1l2p2 = Math.pow(l1[0] - l2[2], 2) + Math.pow(l1[1] - l2[3], 2);
		
		if(distl1p2l2p1 < 1E-4 && distl1p1l2p2 < 1E-4)
			return true;
		
		
		return false;
	}
	
	private int ccw(double p0x, double p0y, double p1x, double p1y, double p2x, double p2y) {
		double dx1, dx2, dy1, dy2;
		int ccw = -9;
		
		dx1 = p1x - p0x; dy1 = p1y - p0y;
	    dx2 = p2x - p0x; dy2 = p2y - p0y;
	    
	    if(dx1 * dy2 > dy1 * dx2)
	    	ccw = 1;
	    
	    if(dx1 * dy2 < dy1 * dx2)
	    	ccw = -1;
	    
	    if(dx1 * dy2 == dy1 * dx2) {
	        if((dx1 * dx2 < 0 || dy1 * dy2 < 0))
	        		ccw =-1;
	        else if((dx1 * dx1 + dy1 * dy1 >= dx2 * dx2 + dy2 * dy2))
	        		ccw = 0;
	        else
	        	ccw = 1;
	    }
	    return ccw;
	}
		
	private boolean isPointinPolygon(ArrayList<double[]> polygonLines, double xCoord, double yCoord) {
		
		double[][] polygonNodes = getPolygonPoints(polygonLines);
		
		Path2D path = new Path2D.Double();
		path.moveTo(polygonNodes[0][0], polygonNodes[0][1]);
		for(int i = 1; i < polygonNodes.length; i++) {
			path.lineTo(polygonNodes[i][0], polygonNodes[i][1]);
		}
		path.closePath();
		
		if(path.contains(xCoord, yCoord))
			return true;
		
		for (int i = 0; i < polygonLines.size(); i++) {
			
			if((polygonLines.get(i)[0] == xCoord && polygonLines.get(i)[1] == yCoord) ||
					(polygonLines.get(i)[2] == xCoord && polygonLines.get(i)[3] == yCoord))
				return true;
			
			Line2D line = new Line2D.Double(polygonLines.get(i)[0], polygonLines.get(i)[1], polygonLines.get(i)[2], polygonLines.get(i)[3]);
			
			if(line.ptSegDist(xCoord, yCoord) < 1E-4)
				return true;
						
			}


		return false;
	}

	@Override
	public boolean validateInput(AnimationPropertiesContainer primitives,
			Hashtable<String, Object> props) throws IllegalArgumentException {

		PointList = (int[][]) props.get("PointList");
		if(PointList.length < 3)
			throw new IllegalArgumentException("Es müssen mindestens 3 Punkte angegeben werden.\nAngegebene Punkte: " + PointList.length);

		if(PointList[0].length != 2)
			throw new IllegalArgumentException("Die PointList muss aus genau 2 Spalten bestehen (1. Spalte = X-Koordinaten, 2. Spalte = Y-Koordinaten");
		
		double transparencyRate = (double) props.get("Transparency_Rate");
		if(transparencyRate < 0 || transparencyRate > 1)
			throw new IllegalArgumentException("Die Transparency_Rate ist eine prozentuale Angabe und muss deshalb zwischen 0 (0 %) und 1 (100 %) liegen.");
		
		return true;
	}

}
