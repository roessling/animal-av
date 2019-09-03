package animal.vhdl.optimization.QuineMcCluskey;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Formula {
  private List<Term>               termList;
  private List<Term>               originalTermList;
  private static ArrayList<String> alphabet;
  private static String            px;

  public static ArrayList<String> getAlphabet() {
    return alphabet;
  }

  public static void setAlphabet(String logicExpression) {
    StringTokenizer stok = null;
    alphabet = new ArrayList<String>(0);
    stok = new StringTokenizer(logicExpression);
    while (stok.hasMoreTokens()) {
      String temp = stok.nextToken();
      temp = temp.replace("~", "");
      temp = temp.replace("(", "");
      temp = temp.replace(")", "");
      temp = temp.trim();
      if (!alphabet.contains(temp) && !temp.equals("+") && !temp.equals("*")
          && !temp.equals(""))
        alphabet.add(temp);
    }

  }

  public Formula(List<Term> aTermList) {
    termList = aTermList;
  }

  public String toString() {
    String result = "";
    result += termList.size() + " terms, " + termList.get(0).getTermLength()
        + " variables\n";
    for (int i = 0; i < termList.size(); i++) {
      result += termList.get(i) + "\n";
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  public void reduceToPrimeImplicants() {
    originalTermList = new ArrayList<Term>(termList);
    int numVars = termList.get(0).getTermLength();
    ArrayList<Term>[][] table = new ArrayList[numVars + 1][numVars + 1];
    for (int dontKnows = 0; dontKnows <= numVars; dontKnows++) {
      for (int ones = 0; ones <= numVars; ones++) {
        table[dontKnows][ones] = new ArrayList<Term>();
      }
    }
    for (int i = 0; i < termList.size(); i++) {
      int dontCares = termList.get(i).countValues(Term.wildcard);
      int ones = termList.get(i).countValues((byte) 1);
      table[dontCares][ones].add(termList.get(i));
    }

    for (int dontKnows = 0; dontKnows <= numVars - 1; dontKnows++) {
      for (int ones = 0; ones <= numVars - 1; ones++) {
        ArrayList<Term> left = table[dontKnows][ones];
        ArrayList<Term> right = table[dontKnows][ones + 1];
        ArrayList<Term> out = table[dontKnows + 1][ones];
        for (int leftIdx = 0; leftIdx < left.size(); leftIdx++) {
          for (int rightIdx = 0; rightIdx < right.size(); rightIdx++) {
            Term combined = left.get(leftIdx).combine(right.get(rightIdx));
            if (combined != null) {
              if (!out.contains(combined)) {
                out.add(combined);
              }
              termList.remove(left.get(leftIdx));
              termList.remove(right.get(rightIdx));
              if (!termList.contains(combined)) {
                termList.add(combined);
              }

            }
          }
        }
      }
    }

  }

  public void reducePrimeImplicantsToSubset() {
    int numPrimeImplicants = termList.size();
    int numOriginalTerms = originalTermList.size();
    boolean[][] table = new boolean[numPrimeImplicants][numOriginalTerms];
    for (int impl = 0; impl < numPrimeImplicants; impl++) {
      for (int term = 0; term < numOriginalTerms; term++) {
        table[impl][term] = termList.get(impl).implies(
            originalTermList.get(term));
      }
    }

    ArrayList<Term> newTermList = new ArrayList<Term>();
    boolean done = false;
    int impl;
    while (!done) {
      impl = extractEssentialImplicant(table);
      if (impl != -1) {
        newTermList.add(termList.get(impl));
      } else {
        impl = extractLargestImplicant(table);
        if (impl != -1) {
          newTermList.add(termList.get(impl));
        } else {
          done = true;
        }
      }
    }
    termList = newTermList;

    originalTermList = null;
  }

  public static Formula read(String symbolExpression) {
    String[] termExp = symbolExpression.split("\\+");
    setAlphabet(symbolExpression);
    ArrayList<String> alp = getAlphabet();
    ArrayList<Term> terms = new ArrayList<Term>(0);
    Term term;
    for (int i = 0; i < termExp.length; i++) {
      term = Term.read(termExp[i], alp);
      if (term.get(0) == 3)
        return null;
      if (term != null)
        terms.add(term);
    }

    return new Formula(terms);
  }

  private int extractEssentialImplicant(boolean[][] table) {
    for (int term = 0; term < table[0].length; term++) {
      int lastImplFound = -1;
      for (int impl = 0; impl < table.length; impl++) {
        if (table[impl][term]) {
          if (lastImplFound == -1) {
            lastImplFound = impl;
          } else {
            // This term has multiple implications
            lastImplFound = -1;
            break;
          }
        }
      }
      if (lastImplFound != -1) {
        extractImplicant(table, lastImplFound);
        return lastImplFound;
      }
    }
    return -1;
  }

  private void extractImplicant(boolean[][] table, int impl) {
    for (int term = 0; term < table[0].length; term++) {
      if (table[impl][term]) {
        for (int impl2 = 0; impl2 < table.length; impl2++) {
          table[impl2][term] = false;
        }
      }
    }
  }

  private int extractLargestImplicant(boolean[][] table) {
    int maxNumTerms = 0;
    int maxNumTermsImpl = -1;
    for (int impl = 0; impl < table.length; impl++) {
      int numTerms = 0;
      for (int term = 0; term < table[0].length; term++) {
        if (table[impl][term]) {
          numTerms++;
        }
      }
      if (numTerms > maxNumTerms) {
        maxNumTerms = numTerms;
        maxNumTermsImpl = impl;
      }
    }
    if (maxNumTermsImpl != -1) {
      extractImplicant(table, maxNumTermsImpl);
      return maxNumTermsImpl;
    }
    return -1;
  }

  public static String expressionTranslate(String logicExpression) {
    if (logicExpression.contains("<=") || logicExpression.contains(":=")) {
      if (logicExpression.toLowerCase().contains("nand")
          || logicExpression.toLowerCase().contains("nor")
          || logicExpression.toLowerCase().contains("xor")
          || logicExpression.toLowerCase().contains("xnor")) {
        return "Error 0";
      }
      String symbolExpression = null;
      symbolExpression = logicExpression.replaceAll("and", "*");
      symbolExpression = symbolExpression.replaceAll("And", "*");
      symbolExpression = symbolExpression.replaceAll("AND", "*");
      symbolExpression = symbolExpression.replaceAll("or", "+");
      symbolExpression = symbolExpression.replaceAll("Or", "+");
      symbolExpression = symbolExpression.replaceAll("OR", "+");
      symbolExpression = symbolExpression.replaceAll("NOT", "~");
      symbolExpression = symbolExpression.replaceAll("not", "~");
      symbolExpression = symbolExpression.replaceAll("Not", "~");
      if (symbolExpression.contains("=")) {
        px = symbolExpression.substring(0, symbolExpression.indexOf("=") + 1);
        symbolExpression = symbolExpression.substring(symbolExpression
            .indexOf("=") + 1);

      }
      return symbolExpression;
    } else
      return null;
  }

  public String resultTranslate() {
    String result = "";
    for (int i = 0; i < termList.size(); i++) {
      for (int j = 0; j < termList.get(i).getTermLength(); j++) {
        if (termList.get(i).get(j) == 0)
          result += "NOT " + alphabet.get(j) + " AND ";
        else if ((termList.get(i).get(j) == 1))
          result += alphabet.get(j) + " AND ";
      }
      result = result.substring(0, result.length() - 5);
      result += " OR ";
    }
    return px + result.substring(0, result.length() - 4);
  }

  public String resultSymbolTranslate() {
    String result = "";
    char[] valName = { 'a', 'b', 'c' };
    for (int i = 0; i < termList.size(); i++) {
      for (int j = 0; j < termList.get(i).getTermLength(); j++) {
        if (termList.get(i).get(j) == 0)
          result += "~" + valName[j] + "*";
        else if ((termList.get(i).get(j) == 1))
          result += valName[j] + "*";
      }
      result = result.substring(0, result.length() - 1);
      result += "+";
    }
    return px + result.substring(0, result.length() - 1);
  }

//  public static void main(String[] args) {
//    // String
//    // test=Formula.expressionTranslate("not a and not b and not c or not a and b and not c or not a and b and c  or a and b and not c  or a and not b and c  or a and b and c");
//    String test = Formula
//        .expressionTranslate("y <=a and b or b and not c or b and c");
//    if (test != null) {
//      Formula f = Formula.read(test);
//
//      f.reduceToPrimeImplicants();
//      System.out.println(f);
//      f.reducePrimeImplicantsToSubset();
//      System.out.println(f.resultTranslate());
//      System.out.println(f.resultSymbolTranslate());
//    }
//  }

}
