package animalscript.extensions;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.StreamTokenizer;

import animal.animator.TimedShow;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTPolygon;
import animal.graphics.PTPolyline;
import animal.graphics.PTText;
import animal.main.Animal;
import animalscript.core.AnimalParseSupport;
import animalscript.core.BasicParser;

public class ArrayProducer extends BasicParser {
  public static void makeArray(String arrayName, Point basePoint,
    Color arrayColor, Color fillColor, Color elementColor,
    boolean verticalMode, int arrayLength, String[] elements, int basicDepth,
    int displayDelay, boolean cascadedDisplay, int displayDuration,
    int[] oids, int[] entryIDs, StringBuilder components, Font f, String unit,
    boolean isHidden, StreamTokenizer stok) {
    // now the fun can start... generate the objects!!!
    // We generate arrays by generating a new rectangle with outline and 
    // fillColor at the new coordinates and depth after the given
    // delay and inserting the text immediately afterwards.  Keep in
    // mind to use the last bounding box NE edge for placing the new
    // component!
    int i;
    int textPixelWidth = 0;
    int textPixelHeight = 0;

    //    int fontSize = 16;
    FontMetrics fm = Animal.getConcreteFontMetrics(f);

    //Toolkit.getDefaultToolkit().getFontMetrics(f);
    textPixelHeight = fm.getMaxAscent() + fm.getMaxDescent();

    PTPolygon textBox = null;
    PTText textEntry = null;
    int[] xCoords = { basePoint.x, basePoint.x, 0, 0 };
    int[] yCoords = { basePoint.y, 0, 0, basePoint.y };
    int maxElementLength = -1;
    int oidPosition = 0;
    int displayStep = (displayDuration / arrayLength);
    int currentDelay = displayDelay;
    TimedShow ts = new TimedShow();

    // if in vertical mode, predetermine the width of the largest cell!
    if (verticalMode) {
      for (i = 0; i < arrayLength; i++)
        if (fm.stringWidth(elements[i]) > maxElementLength) {
          maxElementLength = fm.stringWidth(elements[i]);
        }

      xCoords[2] = xCoords[0] + maxElementLength + 5; // fixed with for all elements!
      xCoords[3] = xCoords[2];
    }

    // generate the entries *sigh*
    for (i = 0; i < arrayLength; i++) {
      // 1. determine current entry's width in pixels and adapt coords
      textPixelWidth = fm.stringWidth(elements[i]);

      if (!verticalMode) { // update only for horizontal elements
        xCoords[2] = xCoords[0] + textPixelWidth + 5;
        xCoords[3] = xCoords[2];
      }

      yCoords[1] = yCoords[0] + textPixelHeight + 5;
      yCoords[2] = yCoords[1];

      // 2. generate the array cell
      textBox = new PTPolygon(xCoords, yCoords);
      textBox.setObjectName(arrayName + "[" + i + "].box");
//      textBox.setClosed(true);
      textBox.setFilled(true);
      textBox.setColor(arrayColor);
      textBox.setFillColor(fillColor);
      textBox.setDepth(basicDepth + 1);
//      getObjectTypes().put(arrayName + "[" + i + "].box",
//        getTypeIdentifier("polyline"));

      // 3. generate the text inside the cell
      textEntry = new PTText(elements[i], f);
      textEntry.setObjectName(arrayName + "[" + i + "]");
      textEntry.setDepth(basicDepth);
      textEntry.setColor(elementColor);
      textEntry.setPosition(xCoords[0] + 3, yCoords[1] - 8);
//      textEntry.setLocation(new Point(xCoords[0] + 3, yCoords[1] - 8));
//      getObjectTypes().put(arrayName + "[" + i + "]", getTypeIdentifier("text"));

      // 4. insert the elements into the vector of entries
      BasicParser.addGraphicObject(textBox, anim);
      BasicParser.addGraphicObject(textEntry, anim);
//MessageDisplay.addDebugMessage("added text box & entry for array '" +arrayName
//		+"'@" +i +" with IDs " +textBox.getNum(false) +" /" +textEntry.getNum(false));
      // 5. register for animator && registry
      oids[oidPosition++] = textBox.getNum(false);
      oids[oidPosition++] = textEntry.getNum(false);
      components.append(textBox.getNum(false)).append(' ');
      components.append(textEntry.getNum(false)).append(' ');

      // 6. register for properties
      entryIDs[i] = textEntry.getNum(false);
      entryIDs[arrayLength + i] = textBox.getNum(false);
      getObjectIDs().put(arrayName + "[" + i + "].box", oids[oidPosition - 2]);
      getObjectIDs().put(arrayName + "[" + i + "]", oids[oidPosition - 1]);
      getObjectTypes().put(arrayName + "[" + i + "].box",
        getTypeIdentifier("polyline"));
      getObjectTypes().put(arrayName + "[" + i + "]", getTypeIdentifier("text"));

      // 7. if this uses 'duration', generate the show effect now!
      if (displayDuration != 0 && !isHidden) {
        ts = new TimedShow(currentStep, oids, 0, null, displayDelay >= 0);
        ts.setOffset(currentDelay);
        ts.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));

        BasicParser.addAnimatorToAnimation(ts, anim);
        currentDelay += displayStep;
        oids = new int[2]; // overwrite last entries
        oidPosition = 0;
      }

      // 8. If in horizontal mode, translate x coord, else the y coord
      if (verticalMode) {
        yCoords[0] = yCoords[1];
        yCoords[3] = yCoords[1];
      }
      else {
        xCoords[0] = xCoords[2];
        xCoords[1] = xCoords[2];
      }
    }

    if (displayDuration == 0 && !isHidden) {
      ts = new TimedShow(currentStep, oids, 0, null, displayDelay >= 0);
      ts.setOffset(currentDelay);
      ts.setUnitIsTicks(unit.equalsIgnoreCase("ticks"));
      BasicParser.addAnimatorToAnimation(ts, anim);
    }

    xCoords[0] = basePoint.x;
    yCoords[0] = basePoint.y;
    xCoords[1] = xCoords[0];
    yCoords[3] = yCoords[0];
    textBox = new PTPolygon(xCoords, yCoords);
    textBox.setObjectName(arrayName);
//    textBox.setClosed(true);
    textBox.setFilled(false);
    textBox.setColor(arrayColor);
    textBox.setFillColor(fillColor);
    textBox.setDepth(basicDepth + 1);
    BasicParser.addGraphicObject(textBox, anim);

    getObjectIDs().put(arrayName, entryIDs);

    for (i = 0; i < entryIDs.length; i++)
      getCurrentlyVisible().put(String.valueOf(entryIDs[i]), String.valueOf(!isHidden)); //"true");

    getObjectIDs().put(arrayName + ".bBox", textBox.getNum(false));
    getObjectProperties().put(arrayName + ".orientation",
      (verticalMode) ? "vertical" : "horizontal");

    getObjectProperties().put(arrayName + ".font", f);
    getObjectTypes().put(arrayName, getTypeIdentifier("array"));
  }

  public void arrayMarker(String localType, String markerName,
    String markerLabel, PTText labelText, String basicTag,
    String targetObjectName, int[] targetOIDs, int maxLength, int targetIndex,
    PTPolyline indexMarker, Point[] coords) throws IOException {
    int indexOID = targetOIDs[maxLength + targetIndex];

    // determine the basic object's bounding box
    PTGraphicObject ptgo = animState.getCloneByNum(indexOID);

    // retrieve the bounding box
    Rectangle boundingBox = ptgo.getBoundingBox();

    if (getObjectProperties().getProperty(targetObjectName + ".orientation")
            .equals("horizontal")) {
      coords[1] = new Point(boundingBox.x + (boundingBox.width >>> 1),
          boundingBox.y);
      coords[0] = new Point(coords[1].x, boundingBox.y - 40);
    }
    else {
      coords[1] = new Point(boundingBox.x,
          boundingBox.y + (boundingBox.height >>> 1));
      coords[0] = new Point(boundingBox.x - 40, coords[1].y);
    }

    indexMarker.setObjectName(markerLabel + ".ptr");
    indexMarker.setFWArrow(true);

    if (markerLabel != null) {
      Font f = new Font("Serif", Font.PLAIN, 16);
      labelText = new PTText(markerLabel, f);
      labelText.setObjectName(markerLabel);

      FontMetrics fm = Animal.getConcreteFontMetrics(f);

      //Toolkit.getDefaultToolkit().getFontMetrics(f);
      int textWidth = fm.stringWidth(markerLabel);

      if (getObjectProperties().getProperty(targetObjectName + ".orientation")
              .equals("horizontal")) {
        labelText.setPosition(new Point(coords[0].x - (textWidth >>> 1),
            coords[0].y - 5));
      }
      else
      {
        labelText.setPosition(new Point(coords[0].x - textWidth - 5,
            coords[0].y + 8));
      }

      BasicParser.addGraphicObject(labelText, anim);
      getObjectIDs().put(markerName + ".label", labelText.getNum(false));
    }

    BasicParser.addGraphicObject(indexMarker, anim);

    String markerID = String.valueOf(indexMarker.getNum(false));
    String ids = (labelText == null) ? markerID
                                     : (markerID + " " +
      String.valueOf(labelText.getNum(false)));

    if (getObjectProperties().getProperty(targetObjectName + ".ptrs") != null) {
      getObjectProperties().put(targetObjectName + ".ptrs",
        getObjectProperties().getProperty(targetObjectName + ".ptrs") + " " +
        markerName);
    }
    else
    {
      getObjectProperties().put(targetObjectName + ".ptrs", markerName);
    }

    getObjectIDs().put(markerName, ids);
    getObjectIDs().put(targetObjectName,
      getObjectIDs().getProperty(targetObjectName) + ' ' + ids);
    getObjectProperties().put(markerName + ".target", targetObjectName);
    getObjectProperties().put(markerName + ".pos", targetIndex);
    getObjectTypes().put(markerName, getTypeIdentifier("arraymarker"));
    getCurrentlyVisible().put(String.valueOf(markerID), "true");

    if (labelText != null) {
      getCurrentlyVisible().put(String.valueOf(labelText.getNum(false)), "true");
    }

    AnimalParseSupport.showComponents(stok, ids, localType, true);
  }
}
