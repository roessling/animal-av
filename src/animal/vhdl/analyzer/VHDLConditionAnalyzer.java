package animal.vhdl.analyzer;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VHDLConditionAnalyzer extends VHDLBaseAnalyzer {

  public static ArrayList<String> conditionAnalyse(ArrayList<String> expressions) {
    ArrayList<String> result = new ArrayList<String>();
    String regEx;
    Pattern p;
    Matcher m;
    boolean rs;
    regEx = "\\w+='0'|'1'";
    p = Pattern.compile(regEx);
    m = p.matcher(expressions.get(0));
    rs = m.find();
    if (rs) {
      result = muxGenerator(expressions);
    } else
      result = gateGenerator(expressions);
    return result;
  }

  private static ArrayList<String> gateGenerator(ArrayList<String> expression) {
    ArrayList<String> result = new ArrayList<String>();
    int codeLineNumberBegin;
    codeLineNumberBegin = Integer.parseInt(expression.get(0));
    int codeLineNumberEnd = codeLineNumberBegin + expression.size() - 1;
    expression.remove(0);
    while (!expression.get(1).toLowerCase().contains("end if")
        && !expression.get(1).toLowerCase().contains("else")) { // if a then =>
                                                                // if a then
                                                                // n:=b
      expression.set(0, expression.get(0) + " "
          + setStandardExpression(expression.get(1))); // n:=b
      expression.remove(1);
    }

    while (expression.size() > 2) {
      expression.set(1, setStandardExpression(expression.get(1)) + " "
          + setStandardExpression(expression.get(2)));
      expression.remove(2);
    }
    String input1 = null, input2 = null, input3 = null, output = null;
    input1 = expression.get(0).substring(
        expression.get(0).toLowerCase().indexOf("if") + 2,
        expression.get(0).toLowerCase().indexOf("then")).trim();
    if (expression.get(0).toLowerCase().indexOf(":=") != -1) {
      input2 = expression.get(0).substring(
          expression.get(0).toLowerCase().indexOf(":=") + 2,
          expression.get(0).toLowerCase().indexOf(";")).trim();
      output = expression.get(0).substring(
          expression.get(0).toLowerCase().indexOf("then") + 4,
          expression.get(0).toLowerCase().indexOf(":=")).trim();
    } else {
      input2 = expression.get(0).substring(
          expression.get(0).toLowerCase().indexOf("<=") + 2,
          expression.get(0).toLowerCase().indexOf(";")).trim();
      output = expression.get(0).substring(
          expression.get(0).toLowerCase().indexOf("then") + 4,
          expression.get(0).toLowerCase().indexOf("<=")).trim();
    }

    if (expression.get(1).toLowerCase().contains("else")) {
      input3 = expression.get(1).substring(
          expression.get(1).toLowerCase().indexOf(":=") + 2,
          expression.get(1).indexOf(";")).trim();
      if (expression.get(1).toLowerCase().indexOf(":=") == -1)
        input3 = expression.get(1).substring(
            expression.get(1).toLowerCase().indexOf("<=") + 2,
            expression.get(1).indexOf(";")).trim();
      result = VHDLBaseAnalyzer.splitLogicExpression(
          output + "<=(" + input1 + " and " + input2 + ") nand (not " + input1
              + " and " + input3 + ")", codeLineNumberBegin, codeLineNumberEnd);
    } else {
      result.add(output + " = " + input1 + " and " + input2);
      result.add(0, codeLineNumberBegin + " " + codeLineNumberEnd);
    }
    return result;
  }

  private static ArrayList<String> muxGenerator(ArrayList<String> expressions) {
    ArrayList<String> result = new ArrayList<String>();
    ArrayList<String> ifElseExpression = new ArrayList<String>();
    ifElseExpression = canonicallyExpressions(expressions);

    String inputPort1 = "", inputPort2 = "", outputPort = "", controlPort = "";

    for (int i = 0; i < ifElseExpression.size(); i = i + 2) {
      inputPort1 = ifElseExpression.get(i).substring(
          ifElseExpression.get(i).indexOf("<=") + 2,
          ifElseExpression.get(i).indexOf(";")).trim();
      inputPort2 = ifElseExpression.get(i + 1).substring(
          ifElseExpression.get(i + 1).indexOf("<=") + 2,
          ifElseExpression.get(i + 1).indexOf(";")).trim();
      outputPort = ifElseExpression.get(i).substring(
          ifElseExpression.get(i).toLowerCase().indexOf("then") + 4,
          ifElseExpression.get(i).indexOf("<=")).trim();
      controlPort = ifElseExpression.get(i).substring(
          ifElseExpression.get(i).indexOf("(") + 1,
          ifElseExpression.get(i).indexOf("=")).trim();
      result.add(outputPort + " = " + inputPort1 + " mux " + inputPort2 + " "
          + controlPort);
    }

    return result;
  }

  private static ArrayList<String> canonicallyExpressions(
      ArrayList<String> expressions) {
    for (int i = 0; i < expressions.size(); i++) {
      if (!(expressions.get(i).toLowerCase().contains("if")
          || expressions.get(i).toLowerCase().contains("else")
          || expressions.get(i).toLowerCase().contains("elsif") || expressions
          .get(i).toLowerCase().contains("end if"))) {
        expressions.set(i - 1, setStandardExpression(expressions.get(i - 1))
            + " " + setStandardExpression(expressions.get(i)));
        expressions.remove(i);
        i--;
      }
    }
    // change one (if... elsif....end if) expression to many (if... else... end
    // if) expressions;
    ArrayList<String> ifElseExpression = new ArrayList<String>();
    String output = expressions.get(0).substring(
        expressions.get(0).toLowerCase().indexOf("then") + 4,
        expressions.get(0).toLowerCase().indexOf("<=")).trim();
    String temp = "temp";
    int count = 0;
    for (int i = 0; i < expressions.size(); i++) {
      if (expressions.get(i).toLowerCase().contains("elsif")) {
        if (!expressions.get(i - 1).toLowerCase().contains("elsif")) {
          ifElseExpression.add(expressions.get(i - 1));
          ifElseExpression.add("else " + output + " <= " + temp + count
              + "; end if");
        }
        ifElseExpression.add(expressions.get(i).substring(3).replace(output,
            temp + count));
        if (expressions.get(i + 1).toLowerCase().contains("elsif"))
          ifElseExpression.add("else " + temp + count + " <= " + temp + ++count
              + "; end if");
        else {
          String x = expressions.get(i + 1).toLowerCase().replace("else", "");
          x = x.replace(output, temp + count);
          ifElseExpression.add("else " + x + " end if");
        }
      }

    }
    return ifElseExpression;
  }

}
