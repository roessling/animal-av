package animal.vhdl.analyzer;

import java.util.ArrayList;

public class VHDLExpressionAnalyzer extends VHDLBaseAnalyzer {
  static ArrayList<String> tempExpressions;

  protected static String[] splitlogicExpressions(
      ArrayList<String> functionDescribeSentences) {
    ArrayList<String> temps = new ArrayList<String>(0);
    tempExpressions = new ArrayList<String>(0);
    String[] subExpressions = null;
    int codeLineNr = Integer.parseInt(functionDescribeSentences.get(0));
    for (int i = 1; i < functionDescribeSentences.size(); i++) {
      functionDescribeSentences.set(i,
          setStandardExpression(functionDescribeSentences.get(i)));
      if (functionDescribeSentences.get(i).toLowerCase().contains("if")
          && !functionDescribeSentences.get(i).toLowerCase().contains("end if")) {
        ArrayList<String> ConditionsubExpressions = new ArrayList<String>();
        ArrayList<String> temp = findExpressions(functionDescribeSentences,
            "if", "end if");
        ConditionsubExpressions = VHDLConditionAnalyzer.conditionAnalyse(temp);
        for (String ConditionsubExpression : ConditionsubExpressions)
          tempExpressions.add(ConditionsubExpression);
        i = i + ConditionsubExpressions.size();
      } else if (functionDescribeSentences.get(i).toLowerCase().contains(
          "process")
          && functionDescribeSentences.get(i).toLowerCase().contains("clk")) {
        ArrayList<String> FFsubExpressions = new ArrayList<String>();
        ArrayList<String> temp = findExpressions(functionDescribeSentences,
            "process", "end process");
        FFsubExpressions = VHDLFlipFlopAnalyzer.FlipFlopAnalyse(temp);
        for (String FFsubExpression : FFsubExpressions)
          tempExpressions.add(FFsubExpression);
        i = i + temp.size();
      } else {
        temps = splitLogicExpression(functionDescribeSentences.get(i),
            codeLineNr + i - 1, codeLineNr + i - 1);
        for (String temp : temps)
          tempExpressions.add(temp);
      }

    }
    subExpressions = new String[tempExpressions.size()];
    tempExpressions.toArray(subExpressions);
    // for (int i=0;i<subExpressions.length;i++)
    // System.out.println(subExpressions[i]);
    return subExpressions;
  }

}
