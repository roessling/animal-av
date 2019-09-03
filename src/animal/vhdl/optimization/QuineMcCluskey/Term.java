package animal.vhdl.optimization.QuineMcCluskey;

import java.util.ArrayList;
import java.util.Arrays;

class Term {
  public static final byte wildcard = 2;
  private byte[]           variable;

  public Term(byte[] var) {
    this.variable = var;
  }

  public int getTermLength() {
    return variable.length;
  }

  public String toString() {
    String result = "{";
    for (int i = 0; i < variable.length; i++) {
      if (variable[i] == wildcard)
        result += "X";
      else
        result += variable[i];
      result += " ";
    }
    result += "}";
    return result;
  }

  public Term combine(Term term) {
    int diffVarNum = -1; // The position where they differ
    for (int i = 0; i < variable.length; i++) {
      if (this.variable[i] != term.variable[i]) {
        if (diffVarNum == -1) {
          diffVarNum = i;
        } else {
          // They're different in at least two places
          return null;
        }
      }
    }
    if (diffVarNum == -1) {
      // They're identical
      return null;
    }
    byte[] resultVars = variable.clone();
    resultVars[diffVarNum] = wildcard;
    return new Term(resultVars);
  }

  public int countValues(byte value) {
    int result = 0;
    for (int i = 0; i < variable.length; i++) {
      if (variable[i] == value) {
        result++;
      }
    }
    return result;
  }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    } else if (o == null || !getClass().equals(o.getClass())) {
      return false;
    } else {
      Term rhs = (Term) o;
      return Arrays.equals(this.variable, rhs.variable);
    }
  }

  boolean implies(Term term) {
    for (int i = 0; i < variable.length; i++) {
      if (this.variable[i] != wildcard && this.variable[i] != term.variable[i]) {
        return false;
      }
    }
    return true;
  }

  public static Term read(String reader, ArrayList<String> alphabet) {
    String[] variables = reader.split("\\*");
    ArrayList<Byte> t = new ArrayList<Byte>();
    for (int i = 0; i < alphabet.size(); i++)
      t.add((byte) 2);
    try {
      for (int i = 0; i < variables.length; i++) {
        variables[i] = variables[i].replace("(", "");
        variables[i] = variables[i].replace(")", "");
        variables[i] = variables[i].trim();
        if (variables[i].contains("~")) {
          variables[i] = variables[i].replace("~", "").trim();
          t.set(alphabet.indexOf(variables[i]), (byte) 0);
        } else {
          t.set(alphabet.indexOf(variables[i]), (byte) 1);
        }
      }
    } catch (Exception e) {
      return new Term(new byte[(byte) 3]);
    }

    if (t.size() > 0) {
      byte[] resultBytes = new byte[t.size()];
      for (int i = 0; i < t.size(); i++) {
        resultBytes[i] = (byte) t.get(i);
      }
      return new Term(resultBytes);
    } else {
      return null;
    }
  }

  public byte get(int index) {
    return variable[index];
  }
}
