package generators.misc.machineLearning;
import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;

import algoanim.executors.formulaparser.Node;
import algoanim.primitives.Ellipse;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.EllipseProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.*;

public class CoordinateSystem2D {
  
  private Language lang;

  private static int radius = 5;
  private int x0;
  private int x1;
  private int y0;
  private int y1;
  
  private Rect positiveDecisionStump;
  private Rect negativeDecisionStump;
  private Rect boundary;
  private LinkedList<Ellipse> dots;
  private LinkedList<Polyline> lines;
  private LinkedList<Text> text;
  private LinkedList<Rect> rects;
 
  private double startVal;
  private double endVal;
  
  private int yLength;
  private int xLength;
  

  public CoordinateSystem2D(Language l, Coordinates origin, int xLength, int yLength ,double startValue, double endValue) {
    lang = l;
    startVal = startValue;
    endVal = endValue;
    
    this.yLength = yLength;
    this.xLength = xLength;
    
    x0 = origin.getX();
    x1 = origin.getX() + this.xLength;
    y0 = origin.getY();
    y1 = origin.getY() - this.yLength;
    
    dots = new LinkedList<Ellipse>();
    lines = new LinkedList<Polyline>();
    text = new LinkedList<Text>();
    rects = new LinkedList<Rect>();
    
    //lang.setStepMode(true);
  }

  public void hide() {
    hideDecisionStumps();
    hideBoundary();
    
    while(!dots.isEmpty()) {
      dots.getFirst().hide();
      dots.removeFirst();
    }
    
    while(!lines.isEmpty()) {
      lines.getFirst().hide();
      lines.removeFirst();
    }
    
    while(!text.isEmpty()) {
      text.getFirst().hide();
      text.removeFirst();
    }
    
    while(!rects.isEmpty()) {
      rects.getFirst().hide();
      rects.removeFirst();
    }
  }
  
  
  
  public void createCoordinateSystem() {
    
    // create CoordinateSystem
    PolylineProperties polyProps = new PolylineProperties(); 
    polyProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    polyProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, false);
    
    Polyline polyX = lang.newPolyline(new Coordinates[]{new Coordinates(x0,y0), new Coordinates(x1,y0)}, "csXLine", null, polyProps);
    Polyline polyY = lang.newPolyline(new Coordinates[]{new Coordinates(x0, y0), new Coordinates(x0,y1)}, "csYLine", null, polyProps);
        
    lines.add(polyY);
    lines.add(polyX);
    labelAxis();
  }
   
  
  public void labelAxis() {

    PolylineProperties labelLineProps = new PolylineProperties();
    labelLineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
    
    TextProperties labelTextProps = new TextProperties();
    labelTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    labelTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",Font.PLAIN,10));
   
    double stepSize = Math.abs(startVal - endVal)/10;
   
    int offset = 0;
    //create y-Labels
    for(int i = 0; i <= 10; i++) {    
      
      offset = i * (yLength/10);
      
      Polyline yLines = lang.newPolyline(new Coordinates[]{new Coordinates(x0 - 5, y0 - offset),
          new Coordinates(x0 + xLength, y0 - offset)}, "yLabelLine" + i,
          null, labelLineProps);
       
      Text yValues = lang.newText(new Coordinates(x0 - 30, y0-offset), ""+round(startVal + (stepSize * i)), "value"+i, null, labelTextProps);
    
      offset = i * (xLength /10);

      Polyline xLines = lang.newPolyline(new Coordinates[]{new Coordinates(x0 + offset, y0 - xLength),
          new Coordinates(x0 + offset, y0 + 5 )}, "xLabelLine" + i,
          null, labelLineProps);
      
      
      Text xValues = lang.newText(new Coordinates(x0 + offset, y0 + 10), ""+round(startVal + (stepSize * i)), "value"+i, null, labelTextProps);      
///   System.out.println(x0 + offset);
      text.add(xValues);
      lines.add(xLines);
      lines.add(yLines);
      text.add(yValues);
 
    }
}
 
  
  public void drawSetBoundary(double xUpperLeftCorner, double yUpperLeftCorner, double xLowerRightCorner, double yLowerRightCorner) {
    
    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, false);
    rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    
    int xUpperLeftCornerPosition =  (int) (xLength * ((xUpperLeftCorner - startVal)/(Math.abs(startVal - endVal)))) + x0;
    int xLowerRightCornerPosition =  (int) (xLength * ((xLowerRightCorner-startVal)/(Math.abs(startVal - endVal)))) + x0;
    
    int yUpperLeftCornerPosition =  y0 - (int)(yLength*((yUpperLeftCorner-startVal)/(Math.abs(startVal - endVal))));
    int yLowerRightCornerPosition =  y0 - (int)(yLength*((yLowerRightCorner-startVal)/(Math.abs(startVal - endVal))));

    boundary = lang.newRect(new Coordinates(xUpperLeftCornerPosition - radius, yUpperLeftCornerPosition + radius), 
        new Coordinates(xLowerRightCornerPosition + radius, yLowerRightCornerPosition - radius),
         "boundary", null, rectProps);
    rects.add(boundary);
  }
  
  
  public void drawPositiveDecisionStump(int xStart, int xEnd, int yStart, int yEnd) {
    RectProperties decisionStumpProps = new RectProperties();
    decisionStumpProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    decisionStumpProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    decisionStumpProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 16);
    decisionStumpProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(204,255,204));
    
    int xStartCoordinate =  (int) (xLength * ((xStart - startVal)/(Math.abs(startVal - endVal)))) + x0;
    int xEndCoordinate =  (int) (xLength * ((xEnd-startVal)/(Math.abs(startVal - endVal)))) + x0;
    
    int yStartCoordinate =  y0 - (int)(yLength*((yStart-startVal)/(Math.abs(startVal - endVal))));
    int yEndCoordinate =  y0 - (int)(yLength*((yEnd-startVal)/(Math.abs(startVal - endVal))));


    positiveDecisionStump = lang.newRect(new Coordinates(xStartCoordinate, yStartCoordinate), new Coordinates(xEndCoordinate,yEndCoordinate), "positiveDecisionStump",null, decisionStumpProps);
    rects.add(positiveDecisionStump);
  }
  
  
  public void drawNegativeDecisionStump(int xStart, int xEnd, int yStart, int yEnd) {
    RectProperties decisionStumpProps = new RectProperties();
    decisionStumpProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    decisionStumpProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    decisionStumpProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 16);
    decisionStumpProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255,153,153));
    
    int xStartCoordinate =  (int) (xLength * ((xStart - startVal)/(Math.abs(startVal - endVal)))) + x0;
    int xEndCoordinate =  (int) (xLength * ((xEnd-startVal)/(Math.abs(startVal - endVal)))) + x0;
    
    int yStartCoordinate =  y0 - (int)(yLength*((yStart-startVal)/(Math.abs(startVal - endVal))));
    int yEndCoordinate =  y0 - (int)(yLength*((yEnd-startVal)/(Math.abs(startVal - endVal))));



    negativeDecisionStump = lang.newRect(new Coordinates(xStartCoordinate, yStartCoordinate), new Coordinates(xEndCoordinate,yEndCoordinate), "negativeDecisionStump",null, decisionStumpProps);
    rects.add(negativeDecisionStump);
  }
  
  public void hideDecisionStumps() {
    if(negativeDecisionStump != null && positiveDecisionStump != null) {
      negativeDecisionStump.hide();
      positiveDecisionStump.hide();
    }
  }
  
  public void hideBoundary() {
    if(boundary !=  null) {
      boundary.hide();
    }
  }
  
  public double round(double val) {
    return (double)Math.round(val * 100.0)/100.0;
 }
  
  public void setPoint(double x, double y, String classification) {
        
    TextProperties labelTextProps = new TextProperties();
    labelTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    labelTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",Font.PLAIN,10));
    labelTextProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        
    int xPosition =  (int) (xLength * ((x - startVal))/Math.abs((startVal-endVal))) +  x0;
     
    int yPosition =  y0 - (int)(yLength*((y - startVal)/Math.abs(startVal - endVal)));
    
    EllipseProperties ellipseProps = new EllipseProperties();
   
    if(classification.equalsIgnoreCase("yes")) {
    ellipseProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    ellipseProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    ellipseProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
    Ellipse e = lang.newEllipse(new Coordinates(xPosition, yPosition), new Coordinates(radius, radius), "e", null, ellipseProps);
    dots.add(e);
    } else if(classification.equalsIgnoreCase("no")) {
      ellipseProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
      ellipseProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      ellipseProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
      
      Ellipse e = lang.newEllipse(new Coordinates(xPosition, yPosition), new Coordinates(radius, radius), "e", null, ellipseProps);
      dots.add(e);
    }
    
  }
  
  
  
  
  // GETTER AND SETTER
  
  /**
   * 
   * @param v
   */
  public void setx0(int v) {
    x0 = v; 
  }
  
  public void setx1(int v) {
    x1 = v; 
  }
  
  public void sety0(int v) {
    y0 = v; 
  }
  
  public void sety1(int v) {
    y1 = v; 
  }
  
  public int getx0() {
    return x0;
  }
  
  public int getx1() {
    return x1;
  }
  
  public int gety0() {
    return y0;
  }
  
  public int gety1() {
    return y1;
  }
  
  
}
