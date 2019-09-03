package animal.graphics;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import animal.graphics.meta.ElementLength;
import animal.graphics.meta.PTArray;
import animal.graphics.meta.PointerMode;
import animal.graphics.meta.StartingPoint;

public class PTPointer extends PTLine {
  /**
   * The references object, e.g., a PTArray
   */
  PTGraphicObject reference;
  
  /**
   * the mode for the pointer, e.g., on a horizontally or vertically
   * aligned array, a circular or rectangular shape
   */
  PointerMode mode;
  
  /**
   * The index of the target element, useful for arrays and other
   * indexed structures
   */
  int elementIndex = -1;
  
  /**
   * The basic orientation of the pointer, e.g. from above (TOP) 
   * or below (BOTTOM)
   */
  StartingPoint startingPoint;
  
  /**
   * An (optional!) text label placed next to the pointer
   */
  PTText label;
  
  /**
   * The length of the pointer - short, medium, or long
   */
  ElementLength length;
  
  public PTPointer() {
  }
  
  public PTPointer(PTGraphicObject ptgo) {
    this(ptgo, PointerMode.RECTANGULAR, StartingPoint.TOP,
        null, ElementLength.MEDIUM, -1);
  }
  
  public PTPointer(PTGraphicObject ptgo, PointerMode pMode,
      StartingPoint start) {
    this(ptgo, pMode, start, null, ElementLength.MEDIUM, -1);
  }

  public PTPointer(PTGraphicObject ptgo, PointerMode pMode,
      StartingPoint start, String text) {
    this(ptgo, pMode, start, text, ElementLength.MEDIUM, -1);
  }
  
  public PTPointer(PTGraphicObject ptgo, PointerMode pMode,
      StartingPoint start, String text, ElementLength pointerLength) {
    this(ptgo, pMode, start, text, pointerLength, -1);
 }
  
  public PTPointer(PTGraphicObject ptgo, PointerMode pMode,
      StartingPoint start, String text, ElementLength pointerLength,
      int targetIndex) {
    super();
    reference = ptgo;
    mode = pMode;
    startingPoint = start;
    label = new PTText(text, new Point(10, 10));
    length = pointerLength;
    elementIndex = targetIndex;
    setFWArrow(true);
    update();
  }
  
  protected void update() {
    if (reference == null)
      return;
    Rectangle r = reference.getBoundingBox();
    Rectangle target = null;
    switch(mode) {
      case ARRAY_HORIZONTAL:
      case ARRAY_VERTICAL:
        if (reference instanceof PTArray) {
          PTArray array = (PTArray)reference;
          if (elementIndex >= 0 && elementIndex < array.getSize()) {
            target = ((PTArray)reference).getBoundingBox(elementIndex);      
          }
        }
        break;
      case CIRCULAR:
      case RECTANGULAR:
        //TODO correct this way?
        target = r;
    }
    createLine(target);
  }
  
  private void createLine(Rectangle target) {
    int l = 20;
    if (length == ElementLength.SHORT)
      l -= 10;
    if (length == ElementLength.LONG)
      l += 10;
    
    Point start = null, end = null;
//    end = new Point((int)target.x, (int)target.y);
    int h = target.height, hh = h >> 1, w = target.width, hw = w >> 1;
    // adapt coordinates
    switch(startingPoint) {
      case TOP_LEFT:
        end = new Point(target.x, target.y);
        start = new Point(end.x - l, end.y - l);
        break;
      case TOP_RIGHT:
        end = new Point(target.x + w, target.y);
        start = new Point(end.x + l, end.y - l);
        break;
      case LEFT:
        end = new Point(target.x, target.y + hh);
        start = new Point(end.x - l, end.y);
        break;
      case RIGHT:
        end = new Point(target.x + w, target.y + hh);
        start = new Point(end.x + l, end.y);
        break;
      case BOTTOM_LEFT:
        end = new Point(target.x, target.y + h);
        start = new Point(end.x - l, end.y + l);
        break;
      case BOTTOM:
        end = new Point(target.x + hw, target.y + h);
        start = new Point(end.x, end.y + l);
        break;
      case BOTTOM_RIGHT:
        end = new Point(target.x + w, target.y + h);
        start = new Point(end.x + l, end.y + l);
        break;
      case TOP:
      default:
        end = new Point(target.x + hw, target.y);
        start = new Point(end.x, end.y - l);
        break;
    }
    nodes.set(0, new PTPoint(start.x, start.y));
    nodes.set(1, new PTPoint(end.x, end.y));
  }
  
  public void paint(Graphics g) {
    super.paint(g);
    if (label != null)
      label.paint(g);
  }
    /*
    // adapt x position for all necessary cases
    switch(startingPoint) {
      case TOP:
      case BOTTOM:
        end.translate((int)target.width >> 1, 0);
        break;
      case TOP_RIGHT:
      case BOTTOM_RIGHT:
      case RIGHT:
        end.translate((int)target.width, 0);
        break;
    }

    // adapt y position for all necessary cases
    switch(startingPoint) {
      case LEFT:
      case RIGHT:
        end.translate(0, (int)target.height >> 1);
        break;
      case BOTTOM:
      case BOTTOM_LEFT:
      case BOTTOM_RIGHT:
        end.translate(0, (int)target.height);
        break;
    }
    
    start = new Point(end.x, end.y);
    // adapt start x position
    switch(startingPoint) {
      case TOP_LEFT:
      case LEFT:
      case BOTTOM_LEFT:
        start.translate(-lengthInPixel, 0);
        break;

      case TOP_RIGHT:
      case RIGHT:
      case BOTTOM_RIGHT:
        start.translate(lengthInPixel, 0);
        break;
       
      case LEFT:
      case BOTTOM_LEFT:
        start = new Point(end.x - lengthInPixel, end.y + lengthInPixel);
        break;
     case BOTTOM:
      case BOTTOM_RIGHT:
         start = new Point(end.x - lengthInPixel, end.y + lengthInPixel);
        break;
      case RIGHT:
      case TOP_RIGHT:
        start = new Point(end.x + lengthInPixel, end.y - lengthInPixel);
        break;
    }
  }*/
}
