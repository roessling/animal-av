/*
 * SutherlandHodgman.java
 * Malte Limmeroth, Stefan Werner, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

public class SutherlandHodgman implements ValidatingGenerator {
    /*TODO: - properties for: 	default line color
     * 							line highlight color
     *  	- orientation of Graph/Polygon
     */
	
	private Color clipColor = Color.BLACK;
	private Color clipHighlightColor = Color.RED;
	
	private Color subjectColor = Color.BLACK;
	private Color subjectHighlightColor = Color.RED;
	
	private int pointRadius = 3;
	
	///////////
	private Language lang;
    private SourceCode src;
    private List<Polyline> clipLines;
    private List<Polyline> subjectLines;
    private List<Circle> subjectPoints;
    private SourceCode outputListText, inputListText;
    private SourceCodeProperties sourceCodePorps;
    
    //TODO: change to properties?
    //		max length of animation grid?
    //magic numbers
    private final int xOffset = 250;
    private final int yOffset = 100;
    private final int scale = 40;
    
    private final String[] sourceCode = new String[]{
			"List outputList = subjectPolygon;",
			"for (Edge clipEdge in clipPolygon) do",
			"   List inputList = outputList;",
			"   outputList.clear();",
			"   Point S = inputList.last;",
			"   for (Point E in inputList) do",
			"      if (E inside clipEdge) then",
			"         if (S not inside clipEdge) then",
			"            outputList.add(ComputeIntersection(S,E,clipEdge));",
			"         end if",
			"         outputList.add(E);",
			"      else if (S inside clipEdge) then",
			"         outputList.add(ComputeIntersection(S,E,clipEdge));",
			"      end if",
			"      S = E;",
			"   done",
			"done",
			"draw(outputList)"
	};

    public void init(){
        lang = new AnimalScript("Sutherland-Hodgman Algorithmus", "Malte Limmeroth, Stefan Werner", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
    	try{
    		//cloning - otherwise entered values get changed
    		int[][] subjectPolygon = clone2dArray((int[][])primitives.get("SubjectPolygon"));
    		int[][] clipPolygon = clone2dArray((int[][])primitives.get("ClipPolygon"));
    		
    		//Line -1: initial drawing
    		TextProperties header = new TextProperties();
    		header.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
    		lang.newText(new Coordinates(25, 25), "Sutherland-Hodgman Algorithmus", "header", null, header);
    		
    		sourceCodePorps = new SourceCodeProperties();
    		sourceCodePorps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    		src = lang.newSourceCode(new Coordinates(25, 50), "src", null, sourceCodePorps);
    		for(String s: sourceCode){
    			src.addCodeLine(s, null, 0, null);
    		}    		
    		
    		scaleAndOffset(clipPolygon);
    		scaleAndOffset(subjectPolygon);
    		
    		clipLines = drawPolygonLines(clipPolygon, "clipLine");
    		subjectLines = drawPolygonLines(subjectPolygon, "subjectLine");
    		subjectPoints = drawPoints(subjectPolygon, "subjectPoint");
    		
    		outputListText = lang.newSourceCode(new Coordinates(25, 350), "outputList", null, sourceCodePorps);
    		outputListText.addCodeLine("OutputList:", null, 0, null);
    		
        	inputListText = lang.newSourceCode(new Coordinates(150, 350), "inputList", null, sourceCodePorps);
        	inputListText.addCodeLine("InputList:", null, 0, null);
    		
    		lang.nextStep();
    		
    		//Line 0: fill outputList    		
        	List<int[]> clipList, outputList;
        	outputList = new ArrayList<int[]>(Arrays.asList(subjectPolygon));
    		
        	drawOutputList(outputList);
        	List<Circle> highlightSubjectPolygon = new ArrayList<Circle>();
        	outputList.forEach(item -> highlightSubjectPolygon.add(getHighlightCircle(item[0], item[1])));

        	srcHighlightAndStep(0);
        	highlightSubjectPolygon.forEach(item -> item.hide());
        	
        	clipList = new ArrayList<int[]>(Arrays.asList(clipPolygon));
        	
        	Circle hideLater = null;
            int clipLen = clipList.size();
            Circle shl = null;
            for (int i = 0; i < clipLen; i++) {
            	int outLen = outputList.size();
            	
            	//Line 1: select edge
                //current clip edge
                int[] e1 = clipList.get((i + clipLen - 1) % clipLen);
                int[] e2 = clipList.get(i);
                
            	clipLines.get((i + clipLen - 1) % clipLen).changeColor(null, clipHighlightColor, null, null);
            	srcHighlightAndStep(1,16);
                

            	//Line 2: fill inputList
                List<int[]> inputList = new ArrayList<int[]>(outputList);
                
            	drawInputList(inputList);
            	srcHighlightAndStep(2);
            	
            	//Line 3: clear outputList
            	outputList.clear();
            	
            	drawOutputList(outputList);
            	srcHighlightAndStep(3);
            	
            	//Line 4: get Point s
                int[] s = inputList.get(inputList.size()-1);
                
                shl = getHighlightCircle(s[0], s[1]);
                inputListText.highlight(inputList.size());
                srcHighlightAndStep(4);
            	
                for (int j = 0; j < outLen; j++) {
                	int edgeIndex = (j + outLen - 1) % outLen;

                    //Line 5: get Point e
                    int[] e = inputList.get(j);
                    
                    Circle ehl = getHighlightCircle(e[0], e[1]);
                    inputListText.highlight((j+1)%(outLen+1));
                    //highlight current subject edge
                    subjectLines.get(edgeIndex).changeColor(null, subjectHighlightColor, null, null);
                    srcHighlightAndStep(5, 15);
                         
                    //Line 6: check e inside
                	srcHighlightAndStep(6,13);
                    if (isInside(e, e1, e2)) {

                    	//Line 7: check s not inside
                    	srcHighlightAndStep(7,9);
                        if (!isInside(s, e1, e2)){
                        	//Line 8: add intersection
                        	outputList.add(intersection(s, e, e1, e2));
                        	
                        	drawOutputList(outputList);
                        	int[] newPoint = outputList.get(outputList.size()-1);
                        	Coordinates newCoords = new Coordinates(newPoint[0], newPoint[1]);

                    		//do not hide the last point on first iteration
                    		//otherwise the last edge will have only one point
                        	if(j > 0){
                        		subjectPoints.get(edgeIndex).hide();
                        	}else{
                        		hideLater = subjectPoints.get(edgeIndex);
                        	}
                        	subjectPoints.set(edgeIndex, lang.newCircle(newCoords, pointRadius, "", null));
                        	
                        	shl.hide();
                        	shl = getHighlightCircle(newCoords.getX(), newCoords.getY());
                        	
                        	shortenHighlightedSubjectLine(edgeIndex,
                        			new Coordinates[]{newCoords, new Coordinates(e[0],  e[1])});
                        	srcHighlightAndStep(8);
                        }
                        
                        //Line 10: add e since it's inside anyway
                        outputList.add(e);
                        
                        drawOutputList(outputList);
                        srcHighlightAndStep(10);
                    } else if (isInside(s, e1, e2)){
                    	//Line 11: check s inside
                    	srcHighlightAndStep(11,13);
                    	
                    	//Line 12: add intersection
                    	outputList.add(intersection(s, e, e1, e2));
                    	
                    	drawOutputList(outputList);
                		int[] prevPoint = s;
                    	int[] newPoint = outputList.get(outputList.size()-1);
                    	Coordinates p1 = new Coordinates(prevPoint[0],  prevPoint[1]);
                    	Coordinates p2 = new Coordinates(newPoint[0], newPoint[1]);
                    	
                    	subjectPoints.add(lang.newCircle(p2, pointRadius, "", null));
                    	
                    	ehl.hide();
                    	ehl = getHighlightCircle(p2.getX(), p2.getY());
                    	
                    	shortenHighlightedSubjectLine(edgeIndex, new Coordinates[]{p1, p2});
                    	
                        if(j == outLen-1 && hideLater != null){
                        	//now we can hide the last point
                        	hideLater.hide();
                        	hideLater = null;
                        }
                    	
                    	srcHighlightAndStep(12);
                    }else{
                    	//both points out of clipPolygon
                    	srcHighlightAndStep(11,13);
                    	ehl.hide();
                    	shl.hide();
                    	subjectPoints.get(edgeIndex).hide();
                    	subjectLines.get(edgeIndex).hide();
                    }
                    //Line 14: switch s and e
                    s = e;
                    
                    subjectLines.get(edgeIndex).changeColor(null, subjectColor, null, null);
                    inputListText.unhighlight(edgeIndex+1);
                    ehl.hide();
                    shl.hide();
                    
                    if(j != outLen-1){
                        //TODO: to skip or not to skip, that is the question
                        shl = getHighlightCircle(s[0], s[1]);
                        srcHighlightAndStep(14);
                    }
                }
                //restore not highlighted state
                inputListText.unhighlight(outLen);
                clipLines.get((i + clipLen - 1) % clipLen).changeColor(null, clipColor, null, null);
                subjectLines.forEach(item -> item.hide());
                subjectLines = drawPolygonLines(outputList.toArray(new int[outputList.size()][2]), "subjectLine");
                subjectPoints.forEach(item -> item.hide());
                subjectPoints = drawPoints(outputList.toArray(new int[outputList.size()][2]), "subjectPoint");
            }
            
            subjectPoints.forEach(item -> item.changeColor(null, subjectHighlightColor, null, null));
            subjectLines.forEach(item -> item.changeColor(null, subjectHighlightColor, null, null));
            drawInputList(new ArrayList<>());
            srcHighlightAndStep(17);
            
    	}catch(Exception e){
    		e.printStackTrace();
    	}

        return lang.toString();
    }

    private int[] intersection(int[] p, int[] q, int[] a, int[] b){
    	Vector2D s = new Vector2D(a[0], a[1]);
    	Vector2D e = new Vector2D(b[0], b[1]);
    	Vector2D c1 = new Vector2D(p[0], p[1]);
    	Vector2D c2 = new Vector2D(q[0], q[1]);
    	
    	Vector2D de = c1.subtract(c2);
    	Vector2D dp = s.subtract(e);
    	double n1 = c1.getX() * c2.getY() - c1.getY() * c2.getX();
    	double n2 = s.getX() * e.getY() - s.getY() * e.getX();
    	double n3 = 1 / (de.getX() * dp.getY() - de.getY() * dp.getX());
    	
        return new int[]{(int) ((n1*dp.getX()-n2*de.getX())*n3), (int) ((n1*dp.getY()-n2*de.getY())*n3)};
    }
    
    private boolean isInside(int[] p, int[] a, int[] b) {
        return (a[0] - p[0]) * (b[1] - p[1]) > (a[1] - p[1]) * (b[0] - p[0]);
    }
    
    private int[][] clone2dArray(int[][] arr){
    	int[][] res = new int[arr.length][];
		for(int i = 0; i < arr.length; i++){
			res[i] = arr[i].clone();
		}
    	return res;
    }
    
    private void srcHighlightAndStep(int i){
    	src.highlight(i);
    	lang.nextStep();
    	src.unhighlight(i);
    }
    
    private void srcHighlightAndStep(int i, int j){
    	src.highlight(i);
    	src.highlight(j);
    	lang.nextStep();
    	src.unhighlight(i);
    	src.unhighlight(j);
    }
    
    private void drawOutputList(List<int[]> outputList){
    	outputListText.hide();
    	outputListText = lang.newSourceCode(new Coordinates(25, 350), "outputList", null, sourceCodePorps);
    	outputListText.addCodeLine("OutputList:", null, 0, null);
    	for(int i = 0; i < outputList.size(); i++){
    		int[] coords = removeOffsetAndScale(outputList.get(i));
    		outputListText.addCodeLine("(" +  coords[0] + "," + coords[1] + ")", null, 0, null);
    	}
    }
    
    private void drawInputList(List<int[]> inputList){
    	inputListText.hide();
    	inputListText = lang.newSourceCode(new Coordinates(150, 350), "inputList", null, sourceCodePorps);
    	inputListText.addCodeLine("InputList:", null, 0, null);
    	for(int i = 0; i < inputList.size(); i++){
    		int[] coords = removeOffsetAndScale(inputList.get(i));
    		inputListText.addCodeLine("(" +  coords[0] + "," + coords[1] + ")", null, 0, null);
    	}
    }
    
    private Circle getHighlightCircle(int x, int y){
    	Circle ret = lang.newCircle(new Coordinates(x, y), pointRadius, "", null);
        ret.changeColor(null, subjectHighlightColor, null, null);
        return ret;
    }
    
    private void scaleAndOffset(int[][] polygon){
    	for(int i = 0; i < polygon.length; i++){
    		polygon[i][0] = polygon[i][0] * scale + xOffset*2;
    		polygon[i][1] = polygon[i][1] * scale + yOffset*2;
    	}
    }
    
    private int[] removeOffsetAndScale(int[] p){
    	int[] ret = new int[2];
    	ret[0] = (p[0]-xOffset*2)/scale;
    	ret[1] = (p[1]-yOffset*2)/scale;
    	return ret;
    }
    
    private List<Circle> drawPoints(int[][] polygon, String name){
    	List<Circle> ret = new ArrayList<Circle>();
    	for(int i = 0; i < polygon.length; i++){
    		ret.add(lang.newCircle(new Coordinates(polygon[i][0], polygon[i][1]), pointRadius, name+i, null));
    	}
    	return ret;
    }

    private List<Polyline> drawPolygonLines(int[][] polygon, String name){
    	List<Polyline> ret = new ArrayList<Polyline>();
    	for(int i = 0; i < polygon.length; i++){
    		Coordinates[] nodes = new Coordinates[2];
        	nodes[0] = new Coordinates(polygon[i][0], polygon[i][1]);
        	
        	if(i < polygon.length-1){        		
            	nodes[1] = new Coordinates(polygon[i+1][0], polygon[i+1][1]);
        	}else{
        		nodes[1] = nodes[0];
            	nodes[0] = new Coordinates(polygon[0][0], polygon[0][1]);
        	}
        	ret.add(lang.newPolyline(nodes, name + i, null));
    	}
    	return ret;
    }
    
    private void shortenHighlightedSubjectLine(int i, Coordinates[] newCoords){
    	subjectLines.get(i).hide();
        subjectLines.set(i, lang.newPolyline(newCoords, "subjectLine", null));
        subjectLines.get(i).changeColor(null, subjectHighlightColor, null, null);
    }

    public String getName() {
        return "Sutherland-Hodgman Algorithmus";
    }

    public String getAlgorithmName() {
        return "Sutherland-Hodgman algorithm";
    }

    public String getAnimationAuthor() {
        return "Malte Limmeroth, Stefan Werner";
    }

    public String getDescription(){
        return "Mit dem Sutherland-Hodgman-Algorithmus kann man mit jedem konvexen Polygon jedes andere Polygon (konvex oder konkav) clippen. Für jede Fensterkante wird die Begrenzungsstrecke zu einer Gerade erweitert, an der sämtliche (relevanten) Polygonkanten gekürzt werden.";
    }

    public String getCodeExample(){
    	String ret = "";
    	for(String s: sourceCode){
    		ret = ret + s + "\n";
    	}
        return ret;
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
    
    private boolean isConvex(int[][] arr){
    	//TODO: implement
    	return true;
    }

	@Override
	public boolean validateInput(AnimationPropertiesContainer props,Hashtable<String, Object> primitives)
			throws IllegalArgumentException {
		
		int[][] subject = (int[][])primitives.get("SubjectPolygon");
		int[][] clip = (int[][])primitives.get("ClipPolygon");
		
		//TODO: more checks? check if clip is counterclockwise? 
		if(subject.length < 3 || clip.length < 3){
			//not enough points for a polygon
		}else if(subject[0].length != 2 || clip[0].length != 2){
			//not two columns
		}else if(!isConvex(clip)){
			//not convex
		}else{
			return true;
		}
		
		return false;
	}

}