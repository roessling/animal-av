package animal.vhdl.analyzer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import animal.vhdl.VHDLAnimalDummy;
import animal.vhdl.graphics.PTPin;

public class VHDLBaseAnalyzer {
  private static int       count = 1;
  private ArrayList<PTPin> inputPinNames;
  private ArrayList<PTPin> outputPinNames;
  private ArrayList<PTPin> inoutputPinNames;
  private static boolean   useQuineMcCluskey;

  public static boolean isUsedQuineMcCluskey() {
    return useQuineMcCluskey;
  }

  public void setUsedQuineMcCluskey(boolean usesOptimization) {
    useQuineMcCluskey = usesOptimization;
  }

  public ArrayList<PTPin> getInputPins() {
    return inputPinNames;
  }

  public ArrayList<PTPin> getOutputPins() {
    return outputPinNames;
  }

  public ArrayList<PTPin> getInoutputPins() {
    return inoutputPinNames;
  }

  protected void setPins(String portInfo) {
    inputPinNames = new ArrayList<PTPin>(0);
    outputPinNames = new ArrayList<PTPin>(0);
    inoutputPinNames = new ArrayList<PTPin>(0);
    int inputcount = 0, outputcount = 0, inoutputcount = 0;
    String[] allportLists = portInfo.split(";");
    for (String portList : allportLists) { // ain,bin,cin : IN STD_LOGIC
      int typeNumber = portList.indexOf(":");
      String portType = portList.substring(typeNumber + 1);
      portType = portType.trim();
      portType = portType.substring(0, 3);// IN
      String temp1 = portList.substring(0, typeNumber).trim();// ain,bin,cin
      String[] port = temp1.split(",");
      if (portType.equalsIgnoreCase("in ")) {
        for (String oneport : port) {
          inputPinNames.add(new PTPin(0, 0, 10, 0,true));
          inputPinNames.get(inputcount).setPinName(oneport.trim());
          inputcount++;
        }
      } else if (portType.equalsIgnoreCase("out")) {
        for (String oneport : port) {
          outputPinNames.add(new PTPin(0, 0, 10, 0,false));
          outputPinNames.get(outputcount).setPinName(oneport.trim());
          outputcount++;
        }
      } else if (portType.equalsIgnoreCase("ino")) {
        for (String oneport : port) {
          inoutputPinNames.add(new PTPin(10, 0, 0, 0,false));
          inoutputPinNames.get(inoutputcount).setPinName(oneport.trim());
          inoutputcount++;
        }
      }
    }
  }

  /**
   * Returns a new array that is a subarray of this array. The subarray begins
   * with the character at "beginString" and extends to the "endString".
   * 
   * @param functionDescribeSentences
   *          object Array
   * @param beginString
   * @param endString
   * @return subarray
   * 
   */
  protected static ArrayList<String> findExpressions(
      ArrayList<String> functionDescribeSentences, String beginString,
      String endString) {
    ArrayList<String> expression = new ArrayList<String>();
    boolean copy = false;
    int count = 0;
    for (String functionDescribeSentence : functionDescribeSentences) {
      if (functionDescribeSentence.trim().toLowerCase().startsWith(beginString)
          && count == 0) {
        copy = true;
        count = Integer.parseInt(functionDescribeSentences.get(0))
            + functionDescribeSentences.indexOf(functionDescribeSentence) - 1;
      }
      if (functionDescribeSentence.trim().toLowerCase().startsWith(endString)) {
        expression.add(functionDescribeSentence);
        break;
      }
      if (copy)
        expression.add(functionDescribeSentence);
    }
    expression.add(0, count + "");
    return expression;
  }

  protected static ArrayList<String> splitLogicExpression(String expression,
      int codeLineNumberBegin, int codeLineNumberEnd) {
    String gateSymbol;
    String workOnThis = expression;
    if (isUsedQuineMcCluskey())
      workOnThis = VHDLAnalyzerOptimize.quineMcCluskey(expression);
    String regEx;
    Pattern p;
    Matcher m;
    boolean rs;
    String temp = "T";
    String subExpression = "";
    ArrayList<String> tempExpressions = new ArrayList<String>();
    workOnThis = setStandardExpression(expression);
    while (workOnThis.toLowerCase().contains("not")) { // check expression have
                                                       // "not"
      gateSymbol = "(not|NOT)";
      regEx = gateSymbol + "\\s+\\w+";
      p = Pattern.compile(regEx);
      m = p.matcher(workOnThis);
      rs = m.find();
      if (rs) {
        subExpression = temp + count + " = ";
        subExpression = subExpression + m.group();
        tempExpressions.add(subExpression);
        // System.out.println(subExpression);
        workOnThis = workOnThis.replaceFirst(regEx, temp + count);
        count++;
      } else
        break;
    }
    gateSymbol = "(or|OR|and|AND|NAND|nand|nor|NOR|xor|XOR|xnor|XNOR)";
    while (workOnThis.contains("(")) { // check expression have "()"

      regEx = "\\(\\s*\\w+\\s+" + gateSymbol + "\\s+\\w+\\s*\\)";
      p = Pattern.compile(regEx);
      m = p.matcher(workOnThis);
      rs = m.find();
      if (rs) {
        subExpression = temp + count + " = ";
        subExpression = subExpression + m.group();
        tempExpressions.add(subExpression);
        // System.out.println(subExpression);
        workOnThis = workOnThis.replaceFirst(regEx, " " + temp + count + " ");
        count++;
      } else
        break;
    }
    while (workOnThis.toLowerCase().contains("not")) { // check not again
      gateSymbol = "(not|NOT)";
      regEx = gateSymbol + "\\s\\w+";
      p = Pattern.compile(regEx);
      m = p.matcher(workOnThis);
      rs = m.find();
      if (rs) {
        subExpression = temp + count + " = ";
        subExpression = subExpression + m.group();
        tempExpressions.add(subExpression);
        // System.out.println(subExpression);
        workOnThis = workOnThis.replaceFirst(regEx, temp + count);
        count++;
      } else
        break;
    }
    while (workOnThis.contains("<=")) { // check expression no ()
      gateSymbol = "(or|OR|and|AND|NAND|nand|nor|NOR|xor|XOR|xnor|XNOR)";
      subExpression = temp + count + " = ";
      regEx = "\\w+\\s+" + gateSymbol + "\\s+\\w+";
      p = Pattern.compile(regEx);
      m = p.matcher(workOnThis);

      rs = m.find();
      if (rs) {
        subExpression = subExpression + m.group();
        tempExpressions.add(subExpression);
        // System.out.println(subExpression);
        workOnThis = workOnThis.replaceFirst(regEx, temp + count);
        count++;
      } else {
        regEx = "\\w+\\s*<=\\s*\\w+";
        p = Pattern.compile(regEx);
        m = p.matcher(workOnThis);
        rs = m.find();
        if (rs) {
          String[] first = m.group(0).split("<=");
          if (tempExpressions.size() != 0) {
            String[] lastExpression = tempExpressions.get(
                tempExpressions.size() - 1).split("=");
            subExpression = first[0] + " =" + lastExpression[1];
            tempExpressions.set(tempExpressions.size() - 1, subExpression);
            // System.out.println(subExpression);
            workOnThis = workOnThis.replaceFirst(regEx, "");
          } else {
            if (first.length == 2)
              tempExpressions.add(first[0] + " = " + first[1]);
            else
              tempExpressions.add(m.group(0));
          }
        } else
          break;
      }
    }
    if (tempExpressions.size() != 0)
      tempExpressions.add(0, codeLineNumberBegin + " " + codeLineNumberEnd);
    return tempExpressions;
  }

  protected static String setStandardExpression(String expression) {
    String workOnThis = expression.trim();
    workOnThis = workOnThis.replaceAll("\\t", "");
    workOnThis = workOnThis.replaceAll("\\( ", "(");
    workOnThis = workOnThis.replaceAll(" \\)", ")");
    workOnThis = workOnThis.replaceAll(" <=", "<=");
    workOnThis = workOnThis.replaceAll("<= ", "<=");
    return workOnThis;
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
//      InputStream in = new FileInputStream("/animal" + File.separator + "vhdl"
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
}
