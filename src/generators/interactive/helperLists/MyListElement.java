package generators.interactive.helperLists;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Circle;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.framework.InteractivityJob;

@SuppressWarnings("unused")
public class MyListElement {
  
  private final Language lang;
  
  private Text text;
  private PolylineProperties pp;
  private TextProperties textProp;
  private Polyline pointerLastArrow;
  private Polyline pointerNextArrow;
  private Rect textRect;
  private Circle pointerLastCircle;
  private Circle pointerNextCircle;
  
  private String item;

  private MyListElement nextElement = null;
  private MyListElement lastElement = null;
  
  private final int distanceToCorner = 12;
  private final int distanceToRec = 30;

  public MyListElement(Node c, String item, Language lang) {
    this.lang = lang;
    
    this.item = item;
    
    setTextTo(c);
  }
  
  public String getItem() {
    return item;
  }
  
  public void setItem(String s) {
    item = s;
    setTextTo(oldCoordinates);
    if(nextElement!=null) {
      nextElement.setLastElementTo(this);
    }
  }
  
  public Rect getTextRect() {
    return textRect;
  }
  
  private Node oldCoordinates = null;
  public void setTextTo(Node c) {
    if(oldCoordinates!=null) {
      text.hide();
      textRect.hide();
//      pointerBeforeArrow.hide();
//      pointerNextArrow.hide();
      pointerLastCircle.hide();
      pointerNextCircle.hide();
    }
    
    oldCoordinates = c;

    textProp = new TextProperties();
    textProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 17));
    textProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
    text = lang.newText(c, item, null, null, textProp);
    
    RectProperties textRecProp = new RectProperties();
    textRecProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    textRecProp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    textRect = lang.newRect(new Offset(-10, -10, text, AnimalScript.DIRECTION_NW), new Offset(10, 10, text, AnimalScript.DIRECTION_SE), null, null, textRecProp);    
    CircleProperties cpB = new CircleProperties();
    cpB.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cpB.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(153,51,255));
    cpB.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
    CircleProperties cpN = new CircleProperties();
    cpN.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    cpN.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(255,0,255));
    cpN.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
    pointerLastCircle = lang.newCircle(new Offset(0, -distanceToCorner, textRect, AnimalScript.DIRECTION_SW), 3, null, null, cpB);
    pointerNextCircle = lang.newCircle(new Offset(0, distanceToCorner, textRect, AnimalScript.DIRECTION_NE), 3, null, null, cpN);
    
    pp = new PolylineProperties();
    pp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    pp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
    
    updatePointers();
  }
  
  private Text lastElementNullText = null;
  public void setLastElementTo(MyListElement beforeElement) {
    if(pointerLastArrow!=null) {
      pointerLastArrow.hide();
    }
    if(lastElementNullText!=null) {
      lastElementNullText.hide();
    }
    
    if(beforeElement!=null) {
      pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
      pointerLastArrow = lang.newPolyline(new Node[]{new Offset(0, -distanceToCorner, textRect, AnimalScript.DIRECTION_SW), new Offset(0, -distanceToCorner, beforeElement.getTextRect(), AnimalScript.DIRECTION_SE)}, null, null, pp);
      lastElementNullText = null;
    } else {
      pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
      pointerLastArrow = lang.newPolyline(new Node[]{new Offset(0, -distanceToCorner, textRect, AnimalScript.DIRECTION_SW), new Offset(-distanceToRec, -distanceToCorner, textRect, AnimalScript.DIRECTION_SW)}, null, null, pp);
      lastElementNullText = lang.newText(new Offset(-32, -12, pointerLastArrow, AnimalScript.DIRECTION_W), "null", null, null, textProp);
    }
    
    this.lastElement = beforeElement;
  }

  private Text nextElementNullText = null;
  public void setNextElementTo(MyListElement nextElement) {
    if(pointerNextArrow!=null) {
      pointerNextArrow.hide();
    }
    if(nextElementNullText!=null) {
      nextElementNullText.hide();
    }
    
    if(nextElement!=null) {
      pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
      pointerNextArrow = lang.newPolyline(new Node[]{new Offset(0, distanceToCorner, textRect, AnimalScript.DIRECTION_NE), new Offset(0, distanceToCorner, nextElement.getTextRect(), AnimalScript.DIRECTION_NW)}, null, null, pp);
      nextElementNullText = null;
    } else {
      pp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
      pointerNextArrow = lang.newPolyline(new Node[]{new Offset(0, distanceToCorner, textRect, AnimalScript.DIRECTION_NE), new Offset(distanceToRec, distanceToCorner, textRect, AnimalScript.DIRECTION_NE)}, null, null, pp);
      nextElementNullText = lang.newText(new Offset(5, -12, pointerNextArrow, AnimalScript.DIRECTION_E), "null", null, null, textProp);
    }
    
    this.nextElement = nextElement;
  }
  
  public void updatePointers() {
    setLastElementTo(lastElement);
    setNextElementTo(nextElement);
  }

  public void hide() {
    text.hide();
    textRect.hide();
    pointerLastCircle.hide();
    pointerNextCircle.hide();
    pointerNextArrow.hide();
    pointerLastArrow.hide();
    if(nextElementNullText!=null) {
      nextElementNullText.hide();
    }
    if(lastElementNullText!=null) {
      lastElementNullText.hide();
    }
  }

}
