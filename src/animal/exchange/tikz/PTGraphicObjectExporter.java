package animal.exchange.tikz;

import java.awt.Point;
import java.io.PrintWriter;

import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;

public abstract class PTGraphicObjectExporter {
  public PTGraphicObjectExporter() {
	  System.err.println("created object of type " +getClass().getName());
  }
  
  public String convertCoordinate(int x, int y) {
	  StringBuilder sb = new StringBuilder(25);
	  sb.append("(").append(((double)x) / 50).append(",");
	  sb.append(((double)y) / 50).append(")");
	  return sb.toString();
  }
  
  public String convertCoordinate(PTPoint p) {
	  return convertCoordinate(p.getX(), p.getY());
  }

  public String convertCoordinate(Point p) {
	  return convertCoordinate(p.x, p.y);
  }
  
  public abstract void exportTo(PrintWriter writer, PTGraphicObject object);
}
