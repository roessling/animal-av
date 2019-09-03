package generators.misc.machineLearning;
import java.awt.Color;
import java.awt.Font;

import algoanim.primitives.Arc;
import algoanim.primitives.Ellipse;
import algoanim.primitives.Polyline;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArcProperties;
import algoanim.properties.EllipseProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

public class CoordinateSystem1D {
  
  private Language lang;
  
  private static int radius = 5;
  
  private double startVal;
  private double endVal;
  
  private int originX;
  private int originY;
  
  private Coordinates origin;
  
  private int xLength;

  private Arc left;
  private Arc right; 
  
  public CoordinateSystem1D(Language l, Coordinates origin, int xLength ,double startVal, double endVal) {
    lang = l;
    originX = origin.getX();
    originY = origin.getY();
    this.origin = origin;
    this.startVal = startVal;
    this.endVal = endVal;
    
    this.xLength = xLength;
    
    //lang.setStepMode(true);
  }
  
  
  public void createCoordinateSystem() {
    
    PolylineProperties polyProps = new PolylineProperties(); 
    polyProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    polyProps.set(AnimationPropertiesKeys.FWARROW_PROPERTY, false);
    
    Polyline c = lang.newPolyline(new Coordinates[]{origin, new Coordinates(origin.getX() + xLength, origin.getY())}, "csXLine", null, polyProps);
        
    labelAxis();
  }
  
  public void labelAxis() {
    PolylineProperties labelLineProps = new PolylineProperties();
    labelLineProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
    
    TextProperties labelTextProps = new TextProperties();
    labelTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    labelTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",Font.PLAIN,10));
    
    double stepSize = Math.abs((startVal - endVal))/10;

    int offset = 0;
    //create y-Labels
    for(int i = 0; i <= 10; i++) {    
      
      offset = i * (xLength/10);
      
      Polyline xLines = lang.newPolyline(new Coordinates[]{new Coordinates(origin.getX() + offset, origin.getY() - 5),
          new Coordinates(origin.getX() + offset, origin.getY() + 5 )}, "xLabelLine" + i,
          null, labelLineProps);
      
      Text xValues = lang.newText(new Coordinates(origin.getX() + offset - 10, origin.getY() + 10), ""+round(startVal + (stepSize * i)), "value"+i, null, labelTextProps);      

    }
   
  }
  
  public void setPoint(double x, String classification) {
    TextProperties labelTextProps = new TextProperties();
    labelTextProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    labelTextProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",Font.PLAIN,10));
    labelTextProps.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        
    int xPosition =  (int) (xLength * ((x- startVal)/Math.abs((startVal-endVal)))) + origin.getX();
            
    EllipseProperties ellipseProps = new EllipseProperties();
   
    if(classification.equalsIgnoreCase("yes")) {
    ellipseProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    ellipseProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    ellipseProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
    Ellipse e = lang.newEllipse(new Coordinates(xPosition, origin.getY()), new Coordinates(radius, radius), "e", null, ellipseProps);
    } else if(classification.equalsIgnoreCase("no")) {
      ellipseProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
      ellipseProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      ellipseProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.RED);
      
      Ellipse e = lang.newEllipse(new Coordinates(xPosition, origin.getY()), new Coordinates(radius, radius), "e", null, ellipseProps);
    }
  }
  
  public void drawSetBoundary(double leftBoundary, double rightBoundary) {
    
    ArcProperties ap = new ArcProperties();

    ap.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    ap.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GREEN);
    ap.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, 90);
    ap.set(AnimationPropertiesKeys.ANGLE_PROPERTY, 180);
    
    int leftX = (int) (xLength * ((leftBoundary - startVal)/Math.abs(startVal - endVal))) + origin.getX();
    left = lang.newArc(new Coordinates(leftX, origin.getY()), new Coordinates(10,20), "left", null, ap);
    
    int rightX = (int) (xLength * ((rightBoundary - startVal)/Math.abs(startVal - endVal))) + origin.getX();
    
    ap.set(AnimationPropertiesKeys.STARTANGLE_PROPERTY, 270);
    right = lang.newArc(new Coordinates(rightX, origin.getY()), new Coordinates(10,20), "right", null, ap);
  }
  
  
  public double round(double val) {
    return (double)Math.round(val * 100.0)/100.0;
 }
  
  public void hideBoundary() {
    if(left !=  null && right != null) {
      left.hide();
      right.hide();
    }
  }

}
