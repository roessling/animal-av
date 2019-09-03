package animalscript.extensions;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

import animal.graphics.PTLine;
import animal.misc.ParseSupport;
import animal.misc.XProperties;
import animal.vhdl.VHDLAnimalDummy;
import animal.vhdl.graphics.PTDemux;
import animal.vhdl.graphics.PTEntity;
import animal.vhdl.graphics.PTFlipFlop;
import animal.vhdl.graphics.PTMux;
import animal.vhdl.graphics.PTPin;
import animal.vhdl.graphics.PTVHDLElement;
import animalscript.core.AnimalParseSupport;
import animalscript.core.AnimalScriptInterface;
import animalscript.core.BasicParser;

/**
 * This class provides an import filter for program output to Animal.
 * 
 * @author Zheng Lu</a>
 * @version 1.0 1999-06-05
 */
public class VHDLObjectParser extends BasicParser implements
    AnimalScriptInterface {
  private static final int DEFAULT_SPEED = 100;

  /**
   * instantiates the key class dispatcher mapping keyword to definition type
   */
  public VHDLObjectParser() {
    handledKeywords = new Hashtable<String, Object>();
    rulesHash = new XProperties();

    handledKeywords.put("and", "parseGateInput");
    handledKeywords.put("reland", "parseGateInput");
    handledKeywords.put("or", "parseGateInput");
    handledKeywords.put("relor", "parseGateInput");
    handledKeywords.put("nand", "parseGateInput");
    handledKeywords.put("relnand", "parseGateInput");
    handledKeywords.put("nor", "parseGateInput");
    handledKeywords.put("relnor", "parseGateInput");
    handledKeywords.put("xor", "parseGateInput");
    handledKeywords.put("relxor", "parseGateInput");
    handledKeywords.put("xnor", "parseGateInput");
    handledKeywords.put("relxnor", "parseGateInput");
    handledKeywords.put("not", "parseGateInput");
    handledKeywords.put("relnot", "parseGateInput");
    handledKeywords.put("jk", "parseFFInput");
    handledKeywords.put("reljk", "parseFFInput");
    handledKeywords.put("rs", "parseFFInput");
    handledKeywords.put("relrs", "parseFFInput");
    handledKeywords.put("d", "parseFFInput");
    handledKeywords.put("reld", "parseFFInput");
    handledKeywords.put("t", "parseFFInput");
    handledKeywords.put("relt", "parseFFInput");
    handledKeywords.put("entity", "parseEntityInput");
    handledKeywords.put("relentity", "parseEntityInput");
    handledKeywords.put("mux", "parseMuxInput");
    handledKeywords.put("relmux", "parseMuxInput");
    handledKeywords.put("demux", "parseMuxInput");
    handledKeywords.put("reldemux", "parseMuxInput");
    handledKeywords.put("wire", "parseWireInput");

  }

  // ===================================================================
  // interface methods
  // ===================================================================

  /**
   * Determine depending on the command passed if a new step is needed Also keep
   * in mind that we might be in a grouped step using the {...} form. Usually,
   * every command not inside such a grouped step is contained in a new step.
   * However, this is not the case for operations without visible effect -
   * mostly maintenance or declaration entries.
   * 
   * Note that this implementation will return <code>false</code> only if the
   * command is embedded in a grouped step.
   * 
   * @param command
   *          the command used for the decision.
   * @return true if a new step must be generated
   */
  public boolean generateNewStep(String command) {
    return !sameStep && !command.equalsIgnoreCase("supports")
        && !command.equalsIgnoreCase("resource");
  }

  /**
   * Create a PTGate from the description read from the StreamTokenizer.
   */
  @SuppressWarnings("unchecked")
  public void parseGateInput() throws IOException {
    Point firstNode = null, secondNode = null;

    // read in object type
    String localType = ParseSupport.parseWord(stok, "Gate type").toLowerCase();

    // read in OID(object name)
    String s = AnimalParseSupport.parseText(stok, "Gate object name");

    // parse location specification
    firstNode = AnimalParseSupport.parseNodeInfo(stok, localType + "node 1",
        null);
    // read second node
    secondNode = AnimalParseSupport.parseNodeInfo(stok, localType + "node 2",
        null);
    if (localType.toLowerCase().contains("rel"))
      secondNode.translate(firstNode.x, firstNode.y);
    ArrayList<PTPin> inputPin = new ArrayList<PTPin>(0);
    ArrayList<PTPin> outputPin = new ArrayList<PTPin>(0);
    String inoutSymbo = ParseSupport.parseWord(stok, "");
    int token;
    int i = -1;
    String temp = "";
    while (!inoutSymbo.equalsIgnoreCase("color")
        && !inoutSymbo.equalsIgnoreCase("depth")
        && !inoutSymbo.equalsIgnoreCase("filled")
        && !inoutSymbo.equalsIgnoreCase("fillColor")
        && !inoutSymbo.equalsIgnoreCase("after")) {
      if (inoutSymbo.equalsIgnoreCase("input")) {
        inputPin.add(new PTPin(true));
        i++;
        token = stok.nextToken();
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("input") || temp.equalsIgnoreCase("output")) {// next
            // input
            // or
            // output
            // keyword
            inoutSymbo = temp;
            continue;
          } else if (!temp.equalsIgnoreCase("value")) {
            inputPin.get(i).setPinName(temp);
          } else
            stok.pushBack();
        }
        token = stok.nextToken();// value keyword
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("value")) {
            stok.nextToken();
            String sTemp = stok.sval;
            if (token == StreamTokenizer.TT_WORD) {
              if (sTemp != null && sTemp.length() > 0)
                temp = sTemp;
              else
                temp = " ";
            }
            else
              temp = String.valueOf(stok.nval);
//            temp = stok.nval + "";
//            temp = stok.sval;
//            if (temp == null || temp.length() == 0)
//              temp = " ";
//            else
//              temp = temp.toLowerCase();
            System.err.println(sTemp +" - " + stok.sval + "..." + stok.nval +" => '" +temp +"'");
            inputPin.get(i).setPinValue(temp.charAt(0));
          } else {
            inoutSymbo = temp;
            continue;
          }
        }
      }
      if (inoutSymbo.equalsIgnoreCase("output")) {
        outputPin.add(new PTPin(false));
        token = stok.nextToken();
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (!temp.equalsIgnoreCase("value")) {
            outputPin.get(0).setPinName(temp);
          } else
            stok.pushBack();
        } else
          break;
        token = stok.nextToken();// value keyword
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("value")) {
            stok.nextToken();
            temp = stok.nval + "";
            outputPin.get(0).setPinValue(temp.charAt(0));
          } else {
            inoutSymbo = temp;
            continue;
          }
        }
      }

      token = stok.nextToken();
      if (token == StreamTokenizer.TT_WORD) {
        inoutSymbo = stok.sval;
      } else
        break;
    }
    stok.pushBack();
    Class<PTVHDLElement> c;
    PTVHDLElement pe = null;
    String className = isKnownElement(localType.toLowerCase());
    if (className != null)
      try {
        c = (Class<PTVHDLElement>) Class.forName(className);
        pe = c.newInstance();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    pe.setStartNode(firstNode);
    pe.setHeight(secondNode.y - firstNode.y);
    pe.setWidth(secondNode.x - firstNode.x);
    pe.setInputPins(inputPin);
    pe.setOutputPins(outputPin);
    pe.setObjectName(s);

    finishVHDLParsing(pe, localType);
  }

  /**
   * Create a PTEntity from the description read from the StreamTokenizer.
   */
  public void parseEntityInput() throws IOException {
    Point firstNode = null, secondNode = null;

    // read in object type
    String localType = ParseSupport.parseWord(stok, "Entity type")
        .toLowerCase();

    // read in OID(object name)
    String s = AnimalParseSupport.parseText(stok, "Entity object name");

    // parse location specification
    firstNode = AnimalParseSupport.parseNodeInfo(stok, localType + "node 1",
        null);
    // read second node
    secondNode = AnimalParseSupport.parseNodeInfo(stok, localType + "node 2",
        null);
    if (localType.equalsIgnoreCase("relentity"))
      secondNode.translate(firstNode.x, firstNode.y);
    ArrayList<PTPin> inputPin = new ArrayList<PTPin>(0);
    ArrayList<PTPin> outputPin = new ArrayList<PTPin>(0);
    ArrayList<PTPin> inoutputPin = new ArrayList<PTPin>(0);
    ArrayList<PTPin> controlPin = new ArrayList<PTPin>(0);
    String inoutSymbo = ParseSupport.parseWord(stok, "");
    int i = -1, j = -1, k = -1, l = -1, token = 0;
    String temp = "";
    while (!inoutSymbo.equalsIgnoreCase("name")
        && !inoutSymbo.equalsIgnoreCase("color")
        && !inoutSymbo.equalsIgnoreCase("depth")
        && !inoutSymbo.equalsIgnoreCase("filled")
        && !inoutSymbo.equalsIgnoreCase("fillColor")
        && !inoutSymbo.equalsIgnoreCase("after")) {
      if (inoutSymbo.equalsIgnoreCase("input")) {
        inputPin.add(new PTPin(true));
        i++;
        token = stok.nextToken();
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("input") || temp.equalsIgnoreCase("output")
              || temp.equalsIgnoreCase("inoutput")
              || temp.equalsIgnoreCase("control")) {// next input or output
            // keyword
            inoutSymbo = temp;
            continue;
          } else if (!temp.equalsIgnoreCase("value")) {
            inputPin.get(i).setPinName(temp);
          } else
            stok.pushBack();
        }
        token = stok.nextToken();// value keyword
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("value")) {
            stok.nextToken();
            temp = stok.nval + "";
            inputPin.get(i).setPinValue(temp.charAt(0));
          } else {
            inoutSymbo = temp;
            continue;
          }
        }
      }
      if (inoutSymbo.equalsIgnoreCase("output")) {
        outputPin.add(new PTPin(false));
        j++;
        token = stok.nextToken();
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("output")
              || temp.equalsIgnoreCase("inoutput")
              || temp.equalsIgnoreCase("control")) {// next input or
            // output...keyword
            inoutSymbo = temp;
            continue;
          } else if (!temp.equalsIgnoreCase("value")) {
            outputPin.get(j).setPinName(temp);
          } else
            stok.pushBack();
        }
        token = stok.nextToken();// value keyword
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("value")) {
            stok.nextToken();
            temp = stok.nval + "";
            outputPin.get(j).setPinValue(temp.charAt(0));
          } else {
            inoutSymbo = temp;
            continue;
          }
        }
      }
      if (inoutSymbo.equalsIgnoreCase("inoutput")) {
        inoutputPin.add(new PTPin(true));
        k++;
        token = stok.nextToken();
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("inoutput")
              || temp.equalsIgnoreCase("control")) {// next inoutput or control
            // keyword
            inoutSymbo = temp;
            continue;
          } else if (!temp.equalsIgnoreCase("value")) {
            inoutputPin.get(k).setPinName(temp);
          } else
            stok.pushBack();
        }
        token = stok.nextToken();// value keyword
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("value")) {
            stok.nextToken();
            temp = stok.nval + "";
            inoutputPin.get(k).setPinValue(temp.charAt(0));
          } else {
            inoutSymbo = temp;
            continue;
          }
        }
      }
      if (inoutSymbo.equalsIgnoreCase("control")) {
        controlPin.add(new PTPin(false));
        l++;
        token = stok.nextToken();
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("control")) {// next control keyword
            inoutSymbo = temp;
            continue;
          } else if (!temp.equalsIgnoreCase("value")) {
            controlPin.get(l).setPinName(temp);
          } else
            stok.pushBack();
        }
        token = stok.nextToken();// value keyword
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("value")) {
            stok.nextToken();
            temp = stok.nval + "";
            controlPin.get(l).setPinValue(temp.charAt(0));
          } else {
            inoutSymbo = temp;
            continue;
          }
        }
      }
      token = stok.nextToken();
      if (token == StreamTokenizer.TT_WORD) {
        inoutSymbo = stok.sval;
      } else
        break;
    }
    stok.pushBack();
    token = stok.nextToken();
    String name;
    if (token == StreamTokenizer.TT_WORD) {
      name = stok.sval;
      if (name.equalsIgnoreCase("name")) {
        // name=AnimalParseSupport.parseText(stok, "Entity type");
        stok.nextToken();
        name = stok.sval;
      } else {
        stok.pushBack();
        name = "";
      }

    } else
      name = "";
    PTEntity entity = new PTEntity(firstNode.x, firstNode.y, secondNode.x
        - firstNode.x, secondNode.y - firstNode.y, name);
    entity.setInputPins(inputPin);
    entity.setOutputPins(outputPin);
    entity.setControlPins(controlPin);
    entity.setInoutPins(inoutputPin);
    entity.setObjectName(s);

    finishVHDLParsing(entity, localType);
  }

  /**
   * Create a PTFF from the description read from the StreamTokenizer.
   */
  @SuppressWarnings("unchecked")
  public void parseFFInput() throws IOException {
    Point firstNode = null, secondNode = null;

    // read in object type
    String localType = ParseSupport.parseWord(stok, "FF type").toLowerCase();

    // read in OID(object name)
    String s = AnimalParseSupport.parseText(stok, "FF object name");

    // parse location specification
    firstNode = AnimalParseSupport.parseNodeInfo(stok, localType + "node 1",
        null);
    // read second node
    secondNode = AnimalParseSupport.parseNodeInfo(stok, localType + "node 2",
        null);
    if (localType.toLowerCase().contains("rel"))
      secondNode.translate(firstNode.x, firstNode.y);
    ArrayList<PTPin> inputPin = new ArrayList<PTPin>(0);
    ArrayList<PTPin> outputPin = new ArrayList<PTPin>(0);
    ArrayList<PTPin> controlPin = new ArrayList<PTPin>();
    for (int i = 0; i < 4; i++)
      controlPin.add(new PTPin("", true));
    boolean syn = false, rsd = false;
    String inoutSymbo = ParseSupport.parseWord(stok, "");
    int i = -1, j = -1;
    int token = 0;
    // int k=-1,l=-1;
    String temp = "";
    while (!inoutSymbo.equalsIgnoreCase("color")
        && !inoutSymbo.equalsIgnoreCase("depth")
        && !inoutSymbo.equalsIgnoreCase("filled")
        && !inoutSymbo.equalsIgnoreCase("fillColor")
        && !inoutSymbo.equalsIgnoreCase("after")) {
      if (inoutSymbo.equalsIgnoreCase("input")) {
        inputPin.add(new PTPin(true));
        i++;
        token = stok.nextToken();
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("input") || temp.equalsIgnoreCase("output")
              || temp.equalsIgnoreCase("clk") || temp.equalsIgnoreCase("ce")
              || temp.equalsIgnoreCase("sd") || temp.equalsIgnoreCase("rd")) {
            inoutSymbo = temp;
            continue;
          } else if (!temp.equalsIgnoreCase("value")) {
            inputPin.get(i).setPinName(temp);
          } else
            stok.pushBack();
        }
        token = stok.nextToken();// value keyword
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("value")) {
            stok.nextToken();
            temp = stok.nval + "";
            inputPin.get(i).setPinValue(temp.charAt(0));
          } else {
            inoutSymbo = temp;
            continue;
          }
        }
      }
      if (inoutSymbo.equalsIgnoreCase("output")) {
        PTPin currentPin = new PTPin(false);
        outputPin.add(currentPin);
//        outputPin.add(new PTPin(false));
        j++;
        token = stok.nextToken();
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("output") || temp.equalsIgnoreCase("clk")
              || temp.equalsIgnoreCase("ce") || temp.equalsIgnoreCase("sd")
              || temp.equalsIgnoreCase("rd")) {
            inoutSymbo = temp;
            continue;
          } else if (!temp.equalsIgnoreCase("value")) {
//            outputPin.get(j).setPinName(temp);
            currentPin.setPinName(temp);
            token = stok.nextToken();
            if (token == '^') { // argh!
              token = stok.nextToken();
              if (token == StreamTokenizer.TT_WORD)
                currentPin.setPinName(temp + '^' + stok.sval);
              else if (token == StreamTokenizer.TT_NUMBER)
                currentPin.setPinName(temp + '^' + (int)stok.nval);
              else 
                currentPin.setPinName(temp +'^' + token);
            } else
              stok.pushBack();
          } else
            stok.pushBack();
        }
        token = stok.nextToken();// value keyword
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("value")) {
            stok.nextToken();
            temp = stok.nval + "";
            currentPin.setPinValue(temp.charAt(0));
          } else {
            inoutSymbo = temp;
            continue;
          }
        }
      }
      if (inoutSymbo.equalsIgnoreCase("clk")
          || inoutSymbo.equalsIgnoreCase("ce")
          || inoutSymbo.equalsIgnoreCase("rd")
          || inoutSymbo.equalsIgnoreCase("sd")) {
        if (inoutSymbo.equalsIgnoreCase("clk")
            || inoutSymbo.equalsIgnoreCase("ce"))
          syn = true;
        if (inoutSymbo.equalsIgnoreCase("rd")
            || inoutSymbo.equalsIgnoreCase("sd"))
          rsd = true;
        String controlTemp = inoutSymbo;
        token = stok.nextToken();
        String name = "";
        String value = " ";
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("clk") || temp.equalsIgnoreCase("ce")
              || temp.equalsIgnoreCase("rd") || temp.equalsIgnoreCase("sd")) {// next
            // input
            // or
            // output
            // keyword
            inoutSymbo = temp;
            continue;
          } else if (!temp.equalsIgnoreCase("value")
              && !temp.equalsIgnoreCase("color")
              && !temp.equalsIgnoreCase("depth")
              && !temp.equalsIgnoreCase("filled")
              && !temp.equalsIgnoreCase("fillColor")
              && !temp.equalsIgnoreCase("after")) {
            name = temp;
            if (controlTemp.equalsIgnoreCase("sd")) {
              controlPin.get(0).setPinName(name);
            } else if (controlTemp.equalsIgnoreCase("rd")) {
              controlPin.get(1).setPinName(name);
            } else if (controlTemp.equalsIgnoreCase("clk")) {
              controlPin.get(2).setPinName(name);

            } else if (controlTemp.equalsIgnoreCase("ce")) {
              controlPin.get(3).setPinName(name);
            }
          } else
            stok.pushBack();
        }
        token = stok.nextToken();// value keyword
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("value")) {
            stok.nextToken();
            value = stok.nval + "";
            if (controlTemp.equalsIgnoreCase("sd")) {
              controlPin.get(0).setPinValue(value.charAt(0));
            } else if (controlTemp.equalsIgnoreCase("rd")) {
              controlPin.get(1).setPinValue(value.charAt(0));
            } else if (controlTemp.equalsIgnoreCase("clk")) {
              controlPin.get(2).setPinValue(value.charAt(0));

            } else if (controlTemp.equalsIgnoreCase("ce")) {
              controlPin.get(3).setPinValue(value.charAt(0));
            }
          } else {
            inoutSymbo = temp;
            continue;
          }
        }

      }
      token = stok.nextToken();
      if (token == StreamTokenizer.TT_WORD) {
        inoutSymbo = stok.sval;
      } else
        break;
    }
    stok.pushBack();
    Class<PTFlipFlop> c;
    PTFlipFlop ff = null;
    String className = isKnownElement(localType.toLowerCase());
    if (className != null)
      try {
        c = (Class<PTFlipFlop>) Class.forName(className);
        ff = c.newInstance();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
    ff.setStartNode(firstNode);
    ff.setHeight(secondNode.y - firstNode.y);
    ff.setWidth(secondNode.x - firstNode.x);
    ff.setInputPins(inputPin);
    ff.setOutputPins(outputPin);
    ff.setControlPins(controlPin);
    ff.setSynControl(syn);
    ff.setAsynSR(rsd);
    ff.setObjectName(s);
    finishVHDLParsing(ff, localType);
  }

  /**
   * Create a PTMux from the description read from the StreamTokenizer.
   */
  public void parseMuxInput() throws IOException {
    Point firstNode = null, secondNode = null;

    // read in object type
    String localType = ParseSupport.parseWord(stok, "mux type").toLowerCase();

    // read in OID(object name)
    String s = AnimalParseSupport.parseText(stok, "mux object name");

    // parse location specification
    firstNode = AnimalParseSupport.parseNodeInfo(stok, localType + "node 1",
        null);
    // read second node
    secondNode = AnimalParseSupport.parseNodeInfo(stok, localType + "node 2",
        null);
    if (localType.toLowerCase().contains("rel"))
      secondNode.translate(firstNode.x, firstNode.y);
    ArrayList<PTPin> inputPin = new ArrayList<PTPin>(0);
    ArrayList<PTPin> outputPin = new ArrayList<PTPin>(0);
    ArrayList<PTPin> controlPin = new ArrayList<PTPin>(0);
    String inoutSymbo = ParseSupport.parseWord(stok, "");
//    int i = -1, j = -1, cPinNr = -1;
    int token = 0;
    String temp = "";
    PTPin currentPin = null;
    while (!inoutSymbo.equalsIgnoreCase("color")
        && !inoutSymbo.equalsIgnoreCase("depth")
        && !inoutSymbo.equalsIgnoreCase("filled")
        && !inoutSymbo.equalsIgnoreCase("fillColor")
        && !inoutSymbo.equalsIgnoreCase("after")) {
      if (inoutSymbo.equalsIgnoreCase("input")) {
        currentPin = new PTPin(true);
        inputPin.add(currentPin);
//        i++;
        token = stok.nextToken();
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("input") || temp.equalsIgnoreCase("output")
              || temp.equalsIgnoreCase("inoutput")
              || temp.equalsIgnoreCase("control")) {// next input or output
            // keyword
            inoutSymbo = temp;
            continue;
          } else if (!temp.equalsIgnoreCase("value")) {
            currentPin.setPinName(temp);
          } else
            stok.pushBack();
        }
        token = stok.nextToken();// value keyword
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("value")) {
            stok.nextToken();
            temp = stok.nval + "";
            currentPin.setPinValue(temp.charAt(0));
          } else {
            inoutSymbo = temp;
            continue;
          }
        }
      }
      if (inoutSymbo.equalsIgnoreCase("output")) {
        currentPin = new PTPin(false);
        outputPin.add(currentPin);
//        j++;
        token = stok.nextToken();
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("output")
              || temp.equalsIgnoreCase("inoutput")
              || temp.equalsIgnoreCase("control")) {// next input or
            // output...keyword
            inoutSymbo = temp;
            continue;
          } else if (!temp.equalsIgnoreCase("value")) {
            currentPin.setPinName(temp);
          } else
            stok.pushBack();
        }
        token = stok.nextToken();// value keyword
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("value")) {
            stok.nextToken();
            temp = stok.nval + "";
            currentPin.setPinValue(temp.charAt(0));
          } else {
            inoutSymbo = temp;
            continue;
          }
        }
      }
      if (inoutSymbo.equalsIgnoreCase("control")) {
        currentPin = new PTPin(true);
        controlPin.add(currentPin);
        token = stok.nextToken();
//        cPinNr++;
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (!temp.equalsIgnoreCase("value")
              && !temp.equalsIgnoreCase("color")
              && !temp.equalsIgnoreCase("depth")
              && !temp.equalsIgnoreCase("filled")
              && !temp.equalsIgnoreCase("fillColor")
              && !temp.equalsIgnoreCase("after"))
            currentPin.setPinName(temp);
        } else
          stok.pushBack();
        // check if value was found
        token = stok.nextToken();
        if (token == StreamTokenizer.TT_WORD) {
          temp = stok.sval;
          if (temp.equalsIgnoreCase("value")) {
            token = stok.nextToken();
            if (token == StreamTokenizer.TT_NUMBER)
              temp = String.valueOf(stok.nval);
            else
              temp = stok.sval.toLowerCase();
            currentPin.setPinValue(temp.charAt(0));
          } else {
            inoutSymbo = temp;
            continue;
          }
        }

      }
      token = stok.nextToken();
      if (token == StreamTokenizer.TT_WORD) {
        inoutSymbo = stok.sval;
      } else
        break;
    }
    stok.pushBack();
    PTVHDLElement mux = null;
    System.err.println("LOCAL TYPE: " + localType);
    if (localType.equalsIgnoreCase("mux") || localType.equalsIgnoreCase("relmux")) {
      mux = new PTMux(firstNode.x, firstNode.y, secondNode.x - firstNode.x,
          secondNode.y - firstNode.y, 2);
    } else {
      mux = new PTDemux(firstNode.x, firstNode.y, secondNode.x - firstNode.x,
          secondNode.y - firstNode.y, 2);
    }
    mux.setInputPins(inputPin);
    mux.setOutputPins(outputPin);
    mux.setControlPins(controlPin);
    mux.setObjectName(s);

    finishVHDLParsing(mux, localType);
  }

  /**
   * Create a PTWire from the description read from the StreamTokenizer.
   */
  public void parseWireInput() throws IOException {
    Point startNode = null;
    Point endNode = null;
    // read in object type
    String localType = ParseSupport.parseWord(stok, "Wire type").toLowerCase();

    // read in OID(object name)
    String s =
      AnimalParseSupport.parseText(stok, "Wire object name");
    if (ParseSupport.nextTokenIsNodeToken(stok))
      startNode = AnimalParseSupport.parseNodeInfo(stok, localType + "node 1",
          null);
    int i = 2;
    int unit = 10;
    ArrayList<PTLine> ptl = new ArrayList<PTLine>();
    while (ParseSupport.nextTokenIsNodeToken(stok)) {
      endNode = AnimalParseSupport.parseNodeInfo(stok, localType + "node " + i,
          null);
      i++;
      int times = times(startNode, endNode, unit);
      for (int j = 1; j <= times - 1; j++) {
        ptl.add(new PTLine(startNode.x, startNode.y, startNode.x
            + (endNode.x - startNode.x) / times * j, startNode.y
            + (endNode.y - startNode.y) / times * j));
        ptl.get(ptl.size() - 1).setFWArrow(false);
      }
      if (times == 1)
        ptl.add(new PTLine(startNode.x, startNode.y, endNode.x, endNode.y));
      else
        ptl.add(new PTLine(ptl.get(ptl.size() - 1).getLastNode().getX(), ptl
            .get(ptl.size() - 1).getLastNode().getY(), endNode.x, endNode.y));
      ptl.get(ptl.size() - 1).setFWArrow(false);
      startNode = endNode;
    }
    int time = 0, slot = 0;
    int aToken = stok.nextToken();
    if (aToken == StreamTokenizer.TT_WORD) {
      String speed = stok.sval;
      if (speed.equalsIgnoreCase("speed")) {
        aToken = stok.nextToken();
        if (aToken == StreamTokenizer.TT_NUMBER)
          slot = (int) stok.nval;
      } else {
        slot = DEFAULT_SPEED;
        stok.pushBack();
      }

    } else {
      slot = DEFAULT_SPEED;
      stok.pushBack();
    }
    if (slot < 0)
      slot = 0;
    // parse and set the color
    Color c = AnimalParseSupport.parseAndSetColor(stok, localType, "color");
    // check for depth information and set it, if available
    AnimalParseSupport.parseAndSetDepth(stok, ptl.get(0), localType);
    // specific settings - not available for all(sub)types!
    ptl.get(ptl.size() - 1).setFWArrow(
        ParseSupport
            .parseOptionalWord(stok, localType + " fw arrow", "fwArrow"));
    ptl.get(0).setBWArrow(
        ParseSupport
            .parseOptionalWord(stok, localType + " bw arrow", "bwArrow"));

    StringBuffer sb = new StringBuffer(3 * ptl.size());
    for (PTLine l : ptl) {
      l.setColor(c);
      l.setDepth(ptl.get(0).getDepth());
      finishParsing(l, localType, time);
      time = time + (int) slot;
      sb.append(l.getNum(false)).append(' ');
    }
    getObjectIDs().put(s, sb.toString());
    // stok.pushBack();
  }

  /**
   * configure a PTVHDLElement from the description read from the
   * StreamTokenizer.
   * 
   * @param shape
   * @param localType
   * @throws IOException
   */

  private void finishVHDLParsing(PTVHDLElement shape, String localType)
      throws IOException {
    // parse and set the color
    shape.setColor(AnimalParseSupport
        .parseAndSetColor(stok, localType, "color"));

    // check for depth information and set it, if available
    AnimalParseSupport.parseAndSetDepth(stok, shape, localType);

    shape.setFilled(ParseSupport.parseOptionalWord(stok, localType + " filled",
        "filled"));

    if (shape.isFilled())
      shape.setFillColor(AnimalParseSupport.parseAndSetColor(stok, localType,
          "fillColor"));

    // add the object to the list of graphic objects
    BasicParser.addGraphicObject(shape, anim);

    // append the object id to the list
    StringBuilder oids = new StringBuilder();
    oids.append(shape.getNum(false));

    // insert into object list -- necessary for lookups later on!
    getObjectIDs().put(shape.getObjectName(), shape.getNum(false));
    getObjectTypes().put(shape.getObjectName(), getTypeIdentifier("triangle"));
    // display the component, unless marked as 'hidden'
    AnimalParseSupport.showComponents(stok, "" + oids.toString(), localType,
        true);
  }

  /**
   * configure a PTWire from the description read from the StreamTokenizer.
   * 
   * @param shape
   * @param localType
   * @throws IOException
   */

  private void finishParsing(PTLine pt, String localType, int time)
      throws IOException {
    StringBuilder oids = new StringBuilder();

    // add the object to the list of graphic objects
    BasicParser.addGraphicObject(pt, anim);

    // append the object id to the list
    oids.append(pt.getNum(false));

    // insert into object list -- necessary for lookups later on!
    getObjectIDs().put(pt.getObjectName(), pt.getNum(false));
    getObjectTypes().put(pt.getObjectName(), getTypeIdentifier("polyline"));
    StringReader rd = new StringReader("after " + time + " ms");
    StreamTokenizer tempstok = new StreamTokenizer(rd);
    AnimalParseSupport.showComponents(tempstok, "" + oids.toString(), "PTLine",
        true);
  }

  /**
   * find out suited class'name for entity's name
   * 
   * @param entityName
   *          the entity's name
   * @return path and class's name
   */
  public String isKnownElement(String entityName) {
    Properties config = null;
    try {
      VHDLAnimalDummy dummy = new VHDLAnimalDummy();
      ClassLoader cl = dummy.getClass().getClassLoader();
      String path = "VHDLEntityName"; //"/animal/vhdl/VHDLEntityName";
      InputStream in = cl.getResourceAsStream(path);
//      System.err.println("inputStream -- " +path + " /" +in);
//      InputStream in = new FileInputStream("animal" + File.separator + "vhdl"
//          + File.separator + "VHDLEntityName");
      if (in != null) {
        BufferedInputStream bins = new BufferedInputStream(in);
        config = new Properties();
        config.load(bins);
        bins.close();
        in.close();
      }
    } catch (IOException ioex) {
      System.err.println(ioex.getMessage());
    }
    String className = (String) config.get(entityName);
    return className;
  }

  /**
   * compute parts between start node with end node
   * 
   * @param startNode
   * @param endNode
   * @param unit
   * @return times
   */

  private int times(Point startNode, Point endNode, int unit) {
    int times = 0;
    if (startNode.x == endNode.x)
      times = Math.abs(startNode.y - endNode.y) / unit;
    else if (startNode.y == endNode.y)
      times = Math.abs(startNode.x - endNode.x) / unit;
    else
      times = (int) Math.sqrt((startNode.x - endNode.x)
          * (startNode.x - endNode.x) + (startNode.y - endNode.y)
          * (startNode.y - endNode.y))
          / unit;
    if (times == 0)
      times = 1;
    return times;
  }
}
