package animalscript.extensions;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;

import animal.animator.TimedShow;
import animal.graphics.PTText;
import animal.main.Animal;
import animal.misc.XProperties;
import animalscript.core.BasicParser;

public class CodeProducer extends BasicParser
{
  public void insertCode(XProperties codeProperties, 
  		XProperties objectProperties)
  {
    // retrieve the name for the base code group
    String baseCodeGroup = codeProperties.getProperty("code.baseGroup");

    // retrieve the text of the code line / row element
    String codeLineValue = codeProperties.getProperty("code.text", "<<error>>");
    //    System.err.println("CODELINEVALUE: " +codeLineValue +" /" +codeProperties.getProperty("code.text"));
    // retrieve the font information
    // String fontName = getObjectProperties().getProperty(baseCodeGroup +".font", "Monospaced");
    // int style = getObjectProperties().getIntProperty(baseCodeGroup +".fontStyle", Font.PLAIN);
    // int fontSizeUsed = getObjectProperties().getIntProperty(baseCodeGroup +".fontSize", 16);
         
    // re-generate the font
    Font font = getObjectProperties().getFontProperty(baseCodeGroup + ".font");
    
    // determine the current x0 and y0 position
    int x = getObjectProperties().getIntProperty(baseCodeGroup +".x0");
    int y = getObjectProperties().getIntProperty(baseCodeGroup +".y0"); // last line used

    // determine further geometric properties: lineNr, rowNr, indentation
    int indentation = codeProperties.getIntProperty("code.indentation", 0);
    int lineNr = codeProperties.getIntProperty("code.lineNr", 0);
    int rowNr = codeProperties.getIntProperty("code.colNr", 0);
    int fontSizeUsed = font.getSize();

    // update x position for indentation
    x += fontSizeUsed * indentation; // * 2;

    // if in line mode, add space for a single line, else determine row position
    if (rowNr == 0)
      y += fontSizeUsed + lineNr* (4 + fontSizeUsed);
    else {
      // keep in same row
      y += fontSizeUsed +(lineNr)* (4 + fontSizeUsed);
      
      // determine end of last entry
      int predID = getObjectIDs().getIntProperty(baseCodeGroup +"[" +lineNr +"]["
                                            +(rowNr-1) +"]");
      // retrieve last entry
      PTText predTextObject = (PTText)animState.getCloneByNum(predID);

      // determine text and, using the font information, the width in pixels
      String predText = predTextObject.getText();

      // retrieve the "metrics" for the current font
      FontMetrics fm = Animal.getConcreteFontMetrics(font);
      //Toolkit.getDefaultToolkit().getFontMetrics(font);
      int indent = 10;
      if (codeProperties.getBoolProperty("code.inline") == true)
        indent = 0;
      
      x = predTextObject.getLocation().x + indent + fm.stringWidth(predText);
    }

    // now that this is done, generate the text entry!
    PTText codeLine = new PTText(codeLineValue, font);
    // set essential properties of the code line: name, location, depth, color
    String name = codeProperties.getProperty("code.name");
    codeLine.setObjectName(name);

    // set location
    codeLine.setLocation(new Point(x, y));

    // set depth
    codeLine.setDepth(getObjectProperties().getIntProperty(baseCodeGroup +".depth"));

    // set color
    codeLine.setColor(getObjectProperties().getColorProperty(baseCodeGroup +".color", 
                                                        Color.black));

    // update object properties: next entry on new line!(will be undone in row mode)
    getObjectProperties().put(baseCodeGroup +".lineNo", lineNr+1);
    
    // add the object to the list of graphic objects
    BasicParser.addGraphicObject(codeLine, anim);
    int objectIDNr = codeLine.getNum(false);
    // insert into object list -- necessary for lookups later on!
    getObjectIDs().put(name, objectIDNr);

    String currentProperty = getObjectIDs().getProperty(baseCodeGroup, null);
    if (currentProperty == null || currentProperty == "")
      getObjectIDs().put(baseCodeGroup, objectIDNr);
    else
      getObjectIDs().put(baseCodeGroup, getObjectIDs().getProperty(baseCodeGroup) +" "
                    +objectIDNr);
    if (getObjectIDs().getProperty(name) == null)
      getObjectIDs().put(name, objectIDNr);

    getObjectTypes().put(name, getTypeIdentifier("code"));
    int[] ids = { objectIDNr };
    
    // generate the TimedShow animator
    TimedShow ts = new TimedShow(currentStep, ids, 0, "show", true);
    int displayDelay = codeProperties.getIntProperty("code.delay", 0);
    if (displayDelay != 0) {
      ts.setOffset(displayDelay);
      ts.setUnitIsTicks(getObjectProperties().getProperty(baseCodeGroup +".delayUnit",
                                                     "ticks").equalsIgnoreCase("ticks"));
    }
    BasicParser.addAnimatorToAnimation(ts, anim);
    currentlyVisible.put(String.valueOf(objectIDNr), "true");
  }
}
