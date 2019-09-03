/*
 * Scanline.java
 * Stefan Werner, Malte Limmeroth, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

class edgeData {
	  public final int x; //X Coordinate of Point with lower Y Coordinate
	  public final int ymax; //Y Coordinate of Point with higher Y Coordinate
	  public final int bucket; //lower Y Coordinate
	  public final double xIncrement; // inverse gradient
	  public final int ID;
	  public edgeData(int[]p1, int[]p2, int id){
		this.ID = id;
	    if (p1[1] < p2[1]){
	    	x = p1[0];
	    	ymax = p2[1];
	    	bucket = p1[1];
	    }else{
	    	x = p2[0];
	    	ymax = p1[1];
	    	bucket = p2[1];
	    }
	    double m = ((double)p2[1]-(double)p1[1])/((double)p2[0]-(double)p1[0]);
	    this.xIncrement = 1/m;
	  }
	}

public class Scanline implements ValidatingGenerator {
    private Language lang;
    private Color P1Color;
    private Color P2Color;
    private Color BackgroundColor;
    
    private List<Polyline> p1Lines;
    private List<Polyline> p2Lines;
    private final int xmax = 20;
    private final int ymax = 20;
    private final int pixelSize = 10;
    private final int rasterOriginX = xmax * pixelSize + 250;
    private final int rasterOriginY = ymax * pixelSize + 50 + pixelSize;
    private Rect PixelRaster[][] = new Rect[xmax][ymax];
    private SourceCode etText, aetText, ptText; //, xvalue, scanline;
    
    private SourceCodeProperties sourceCodePorps;
	private SourceCode src;
	private boolean[] polygonTable = {false, false};
    
    //TODO
    private final String[] sourceCode = new String[]{
    		"create edge table",
    		"create empty active edge table",
    		"create polygontable",
    		"",
			"for (every scanline) do",
			"   refresh AET: Store every edge which intersects the scanline;",
			"   for (every pixel on scanline) do",
			"      if(reached edge of AET) then",
			"         if(PT entry = false) then",	
			"            PT entry = true",
			"         else",
			"            PT entry = false",
			"      if(one PT entry = true) then",	
			"         color pixel according polygontable",
			"      if(more than PT entries = true) then",
			"         color pixel of polygon with highest z-value",
			"      if(all PT entries = false) then",
			"         color pixel with background color",
			"   end",
			"end"
    };
	
    
    public void init(){
        lang = new AnimalScript("Scanline Algorithmus", "Stefan Werner, Malte Limmeroth", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	int[][]Polygon1 = (int[][])primitives.get("Polygon1");
    	int[][]Polygon2 = (int[][])primitives.get("Polygon2");
    	
    	sourceCodePorps = new SourceCodeProperties();
		sourceCodePorps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);		
		
        int[][]polygon1Scaled = clone2dArray((int[][])primitives.get("Polygon1"));
        P1Color = (Color)primitives.get("P1Color");
        
        P2Color = (Color)primitives.get("P2Color");
        int[][]polygon2Scaled = clone2dArray((int[][])primitives.get("Polygon2"));
        BackgroundColor = (Color)primitives.get("BackgroundColor");
        
        ArrayList<ArrayList<edgeData>> edgeTable = new ArrayList<ArrayList<edgeData>>(ymax);
        ArrayList<edgeData> activeEdgeTable = new ArrayList<edgeData>();
        
        sourceCodePorps = new SourceCodeProperties();
		sourceCodePorps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
		src = lang.newSourceCode(new Coordinates(25, 50), "src", null, sourceCodePorps);
		for(String s: sourceCode){
			src.addCodeLine(s, null, 0, null);
		}
        
        try{
        	
        //TODO add properties to xml file
        //draw pixelraster
        RectProperties PixelRasterProps = new RectProperties();
        PixelRasterProps.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        PixelRasterProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        PixelRasterProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        PixelRaster = drawPixelRaster(PixelRasterProps, xmax, ymax);
        lang.nextStep();
        
        //draw polygons  
        PolylineProperties p1Properties = new PolylineProperties();
        p1Properties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
        p1Properties.set(AnimationPropertiesKeys.COLOR_PROPERTY, P1Color);
        p1Properties.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
        scaleAndOffset(polygon1Scaled);
        p1Lines = drawPolygonLines(polygon1Scaled, "test1", p1Properties);
        p1Lines.forEach(item -> item.show());
        lang.nextStep();
        
        PolylineProperties p2Properties = new PolylineProperties();
        p2Properties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
        p2Properties.set(AnimationPropertiesKeys.COLOR_PROPERTY, P2Color);
        p2Properties.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, true);
        scaleAndOffset(polygon2Scaled);
        p2Lines = drawPolygonLines(polygon2Scaled, "test2", p2Properties);
        p2Lines.forEach(item -> item.show());
        lang.nextStep();
        
        //init edgetable size with one empty bucket per scanline 
        for(int i = 0; i < ymax; i++){
        edgeTable.add(new ArrayList<edgeData>());
        }    
        //init edgetable buckets
        initEdgetable(edgeTable, Polygon1, 0);
        initEdgetable(edgeTable, Polygon2, 1);
        //sort edgetable buckets from smallest y coordinate in ascending order
        for (ArrayList<edgeData> bucket : edgeTable) {
        	Collections.sort(bucket, new Comparator<edgeData>() {
				@Override
				public int compare(edgeData o1, edgeData o2) {
					return o1.ymax - o2.ymax;
				}
            });
		}
        
        //draw tables
        etText = lang.newSourceCode(new Coordinates(550, rasterOriginY + 150), "ET", null, sourceCodePorps);
		etText.addCodeLine("ET: y, x-Wert, ymax, x-Inkrement", null, 0, null);
		srcHighlightAndStep(etText, src, 0, 0);
		lang.nextStep();
        
        aetText = lang.newSourceCode(new Coordinates(25, rasterOriginY + 150), "AET", null, sourceCodePorps);
		aetText.addCodeLine("AET: y-max, x-Wert, x-Inkrement", null, 0, null);
		aetText.addCodeLine("leer", null, 0, null);
		srcHighlightAndStep(aetText, src, 0, 1);
		lang.nextStep();
		
    	ptText = lang.newSourceCode(new Coordinates(250, rasterOriginY + 150), "PT", null, sourceCodePorps);
    	ptText.addCodeLine("PT:", null, 0, null);
    	ptText.highlight(0);
    	
    	src.highlight(2);
		lang.nextStep();
		drawPolygonTable();
		ptText.highlight(0);
		lang.nextStep();
		src.unhighlight(2);
		ptText.unhighlight(0);
		lang.nextStep();     
		
        //for jede Rasterzeile
        for(int i = 0; i < ymax; i++){
        	//highlight scanline
        	for(int r = 0; r < xmax; r++){
        		PixelRaster[r][i].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW, null, null);
        	}
        	srcHighlightAndStep(src, 4, "Scanline " + Integer.toString(i+1));
        	//refresh AET
        	activeEdgeTable.clear();
        	for(int k = 0; k <= i; k++){
        		ArrayList<edgeData> bucket = edgeTable.get(k);
        		for (edgeData edge : bucket){
        			if(edge.ymax > i){
        				activeEdgeTable.add(edge);
        			}
        		}
        	}
        	//sort edges in AET from smallest current x coordinate (x + xincrement) in ascending order
        	Collections.sort(activeEdgeTable, new Comparator<edgeData>() {
				@Override
				public int compare(edgeData o1, edgeData o2) {
					return Double.compare(o1.x,o2.x); 
				}
        	});
        	drawAET(activeEdgeTable);
        	srcHighlightAndStep(src, aetText, 5, 0);
        	
        	//for jedes Pixel auf Rasterzeile
        	for(int j = 1; j <= xmax; j++){ 
        		//highlight Pixel
            	PixelRaster[j-1][i].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED, null, null);
            	srcHighlightAndStep(src, 6);
        		//aet edge reached?
            	srcHighlightAndStep(src, 7);
	        	for (edgeData edge : activeEdgeTable){
	        		double currentx = (double)edge.x + (double)(i - edge.bucket) * edge.xIncrement;
	        		if((currentx < (double)j) || j==xmax){
        				ptText.highlight(edge.ID + 1);
        				srcHighlightAndStep(src, 8);
	        			if(!polygonTable[edge.ID] && j<xmax){
	        				polygonTable[edge.ID] = true;
	        				drawPolygonTable();
	        				srcHighlightAndStep(ptText, src, edge.ID + 1, 9);
	        			}else{
	        				polygonTable[edge.ID] = false;
	        				drawPolygonTable();
	        				ptText.highlight(edge.ID + 1);
	        				srcHighlightAndStep(src, 10, 11);
	        				ptText.unhighlight(edge.ID + 1);
	        			}
	        		}
	        	}
	        	
	        	if(polygonTable[0] && polygonTable[1]){
	        		srcMultHighlight(src, 14, 15);
	        	}else{
	        		if(polygonTable[0] || polygonTable[1]){
	        			srcMultHighlight(src, 12, 13);
		        		if(polygonTable[0]){
		        			ptText.highlight(1);
			        	}
		        		if(polygonTable[1]){
			        		ptText.highlight(2);
			        	}
	        		}
	        	}
	        	if(!polygonTable[0] && !polygonTable[1]){
	        		srcMultHighlight(src, 16, 17);
	        		srcMultHighlight(ptText, 1, 2);
	        	}
	        	PixelRaster[j-1][i].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, BackgroundColor, null, null);
	        	if(polygonTable[0])PixelRaster[j-1][i].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, P1Color, null, null);
	        	if(polygonTable[1])PixelRaster[j-1][i].changeColor(AnimationPropertiesKeys.FILL_PROPERTY, P2Color, null, null);
	        	lang.nextStep();
	        	srcMultUnHighlight(ptText, 1, 2);
	        	srcMultUnHighlight(src, 12, 17);
	        	
	        	//delete active edges from AET to avoid multiple activation of same edge
	        	Iterator<edgeData> iter = activeEdgeTable.iterator();
	        	while (iter.hasNext()) {
			        edgeData p = iter.next();
			        double currentx = (double)p.x + (double)(i - p.bucket) * p.xIncrement;
			        if (currentx < (double)j){
			        	iter.remove();
			        }
	        	}
	        	//remove Pixel highlighting
	        	PixelRaster[j-1][i].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
        	}
        	//remove scanline highlighting
        	for(int r = 0; r < xmax; r++){
        		PixelRaster[r][i].changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK, null, null);
        	}
        }
        src.highlight(19);
        }catch(Exception e){
        	e.printStackTrace();
        }
        return lang.toString();
    }
    
    private void srcMultHighlight(SourceCode c, int from, int to)
    {
    	for(int i = from; i <= to; i++){
    		c.highlight(i);
    	}
    }
    
    private void srcMultUnHighlight(SourceCode c, int from, int to)
    {
    	for(int i = from; i <= to; i++){
    		c.unhighlight(i);
    	}
    }
    
    private void srcHighlightAndStep(SourceCode c1, SourceCode c2, int i1, int i2){
    	c1.highlight(i1);
    	c2.highlight(i2);
    	lang.nextStep();
    	c1.unhighlight(i1);
    	c2.unhighlight(i2);
    }
    
    private void srcHighlightAndStep(SourceCode c, int i){
    	c.highlight(i);
    	lang.nextStep();
    	c.unhighlight(i);
    }
    
    private void srcHighlightAndStep(SourceCode c, int i, String label){
    	c.highlight(i);
    	lang.nextStep(label);
    	c.unhighlight(i);
    }
    
    private void srcHighlightAndStep(SourceCode c, int i, int j, String label){
    	c.highlight(i);
    	c.highlight(j);
    	lang.nextStep(label);
    	c.unhighlight(i);
    	c.unhighlight(j);
    }
    
    private void srcHighlightAndStep(SourceCode c, int i, int j){
    	srcHighlightAndStep(c, i, j, null);
    }
    
    private void drawPolygonTable() {
    	ptText.hide();
    	ptText = lang.newSourceCode(new Coordinates(250, rasterOriginY + 150), "PT", null, sourceCodePorps);
    	ptText.addCodeLine("PT:", null, 0, null);
    	ptText.addCodeLine("Polygon1: Ebenengleichung, Grün,  " + polygonTable[0], null, 0, null);
        ptText.addCodeLine("Polygon2: Ebenengleichung, Blau,  " + polygonTable[1], null, 0, null);
	}

	private void initEdgetable(ArrayList<ArrayList<edgeData>> edgeTable, int[][] polygon, int id) {
    	for(int i = 0; i < polygon.length - 1; i++) {
			edgeData ed = new edgeData(polygon[i], polygon[i+1], id);
			edgeTable.get(ed.bucket).add(ed);
		}
    	edgeData ed = new edgeData(polygon[polygon.length-1], polygon[0], id);
		edgeTable.get(ed.bucket).add(ed);
	}

    private List<Polyline> drawPolygonLines(int[][] polygon, String name, PolylineProperties pp){
    	List<Polyline> ret = new ArrayList<Polyline>();
    	for(int i = 0; i < polygon.length; i++){
    		Coordinates[] nodes = new Coordinates[2];
        	nodes[0] = new Coordinates(polygon[i][0],polygon[i][1]);    	
        	if(i < polygon.length-1){        		
            	nodes[1] = new Coordinates(polygon[i+1][0], polygon[i+1][1]);
        	}else{
        		nodes[1] = nodes[0];
            	nodes[0] = new Coordinates(polygon[0][0], polygon[0][1]);
        	}
        	ret.add(lang.newPolyline(nodes, name + i, null, pp));
    	}
    	return ret;
    }
    
    private Rect[][] drawPixelRaster(RectProperties rp, int xmax, int ymax){
    	Rect[][] res = new Rect[xmax][ymax];
    	for(int i = 0; i<xmax; i++){
        	for(int j = 0; j<ymax; j++){
        		rp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, j + i + 1);
        		Rect pixel = lang.newRect(	new Coordinates(rasterOriginX + i*pixelSize, rasterOriginY - (j+1)*pixelSize), 
        									new Coordinates(rasterOriginX + (i+1)*pixelSize, rasterOriginY - (j)*pixelSize),
        									Integer.toString(i)+Integer.toString(j), 
        									null,
        									rp);
        		res[i][j] = pixel;
        	}
        }
    	return res;
    }
    
    private void drawAET(List<edgeData> aet){
    	aetText.hide();
    	aetText = lang.newSourceCode(new Coordinates(25, rasterOriginY + 150), "AET", null, sourceCodePorps);
    	aetText.addCodeLine("AET: y-max, x-Wert, x-Inkrement", null, 0, null);
    	for(int i = 0; i < aet.size(); i++){
    		edgeData edge = aet.get(i);
    		aetText.addCodeLine("          " + edge.ymax + "    ,    " + edge.x + "    ,    " + String.format("%.2f", edge.xIncrement), null, 0, null);
    	}
    	if(aet.size() == 0){
    		aetText.addCodeLine("leer", null, 0, null);
    	}
    }
    
    private void scaleAndOffset(int[][] polygon){

    	for(int i = 0; i < polygon.length; i++){
    		polygon[i][0] = rasterOriginX + (pixelSize * polygon[i][0]) + (pixelSize/2);
    		polygon[i][1] = rasterOriginY - (pixelSize * polygon[i][1]) - (pixelSize/2);
    	}
    }
    
    private int[][] clone2dArray(int[][] arr){
    	int[][] res = new int[arr.length][];
		for(int i = 0; i < arr.length; i++){
			res[i] = arr[i].clone();
		}
    	return res;
    }
    
    public String getName() {
        return "Scanline Algorithmus";
    }

    public String getAlgorithmName() {
        return "Scanline";
    }

    public String getAnimationAuthor() {
        return "Stefan Werner, Malte Limmeroth";
    }

    public String getDescription(){
        return "Als Scanline-Algorithmus bzw. Bildzeilenalgorithmus wird in der Computergrafik ein Bildzeile für Bildzeile (englisch scan line) arbeitendes Verfahren zur Verdeckungsberechnung von aus Polygonen aufgebauten 3D-Szenen bezeichnet. Der gesamte Darstellungsprozess wird auch Scanline-Rendering bzw. Bildzeilenrenderung genannt. Scanline-Algorithmen nutzen die Tatsache aus, dass durch die Zeile für Zeile erfolgende Arbeitsweise das Problem der Verdeckungsberechnung von drei auf zwei Dimensionen reduziert wird. Die ersten Scanline-Algorithmen wurden Ende der 1960er Jahre veröffentlicht.";
    }

    public String getCodeExample(){
        return "Kantentabelle (ET) initialisieren"
 +"\n"
 +"Aktive Kantentabelle (AET) initialisieren"
 +"\n"
 +"Polygontabelle (PT) initialisieren"
 +"\n"
 +"for(jede Rasterzeiel)"
 +"\n"
 +"	AET aktualisieren"
 +"\n"
 +"	for(jedes Pixel auf Rasterzeile)"
 +"\n"
 +"		if (Kante aus AET erreicht) then"
 +"\n"
 +"			if (PT-eintrag = false) then"
 +"\n"
 +"				PT-eintrag true setzen"
 +"\n"
 +"			else"
 +"\n"
 +"				PT-eintrag false setzen"
 +"\n"
 +"		if (ein PT-eintrag true) then"
 +"\n"
 +"			zeichne Pixel mit Farbe aus PT"
 +"\n"
 +"		if (mehrere PT-Einträge true) then"
 +"\n"
 +"			zeichne Pixel des Polygons mit höchstem Z-Wert"
 +"\n"
 +"		if (kein PT-eintrag = true) then"
 +"\n"
 +"			zeichne Pixel mit Hintergrundfarbe"
 +"\n"
 +"end"
 +"\n"
 +"\n"
 +"Quelle: http://olli.informatik.uni-oldenburg.de/Grafiti3/grafiti/flow8/page9.html"
 +"\n"
 +"	";
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
        return Generator.PSEUDO_CODE_OUTPUT;
    }

	@Override
	public boolean validateInput(AnimationPropertiesContainer arg0, Hashtable<String, Object> arg1)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return true;
	}

}