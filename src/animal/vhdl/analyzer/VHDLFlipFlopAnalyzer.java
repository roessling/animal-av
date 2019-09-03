package animal.vhdl.analyzer;

import java.util.ArrayList;

public class VHDLFlipFlopAnalyzer extends VHDLBaseAnalyzer {
  public static ArrayList<String> FlipFlopAnalyse(ArrayList<String> expression) {
    String setName = "NIL";
    String resName = "NIL";
    String clkName = "NIL";
    String ceName = "NIL";
    int codeLineNr = Integer.parseInt(expression.get(0));
    ArrayList<String> result = new ArrayList<String>();
    for (int i = 0; i < expression.size(); i++) {
      if (expression.get(i).toLowerCase().contains("set"))
        setName = "SET";
      if (expression.get(i).toLowerCase().contains("res"))
        resName = "RES";
      if (expression.get(i).toLowerCase().contains("event"))
        clkName = "CLK";
      if (expression.get(i).toLowerCase().contains("ce"))
        ceName = "CE";
      if (expression.get(i).contains("<="))
        result = VHDLBaseAnalyzer.splitLogicExpression(expression.get(i),
            codeLineNr + i - 1, codeLineNr + i - 1);
    }
    String temp[] = result.get(result.size() - 1).split(" ");
    String DFF;
    String DFF2;
    if (temp.length == 3) {
      DFF = temp[0] + " = DFF" + temp[2] + " " + setName + " " + resName + " "
          + clkName + " " + ceName;
      result.set(result.size() - 1, DFF);
    }
    if (temp.length == 5) {
      DFF = temp[0] + " = DFF D1" + " " + setName + " " + resName + " "
          + clkName + " " + ceName;
      DFF2 = "D1 = " + temp[2] + " " + temp[3] + " " + temp[4];
      result.set(result.size() - 1, DFF2);
      result.add(DFF);
    }

    return result;
  }
}
