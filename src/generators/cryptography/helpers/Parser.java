package generators.cryptography.helpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

  // contains all operations
  private Map<String, Operation> operations = new HashMap<String, Operation>();

  // contains all functions
  private Map<String, Function>  functions  = new HashMap<String, Function>();

  /**
   * constructor, adds some standard functions and operations
   */
  public Parser() {
    // add + operation
    addOp("+", new Operation() {
      @Override
      public int getPriority() {
        return 1;
      }

      @Override
      public double calc(double num1, double num2) {
        return num1 + num2;
      }
    });

    // add + function
    addFunc("+", new Function() {
      @Override
      public double calc(double value) {
        return value;
      }
    });

    // add - operation
    addOp("-", new Operation() {
      @Override
      public int getPriority() {
        return 1;
      }

      @Override
      public double calc(double num1, double num2) {
        return num1 - num2;
      }
    });

    // add - function
    addFunc("-", new Function() {
      @Override
      public double calc(double value) {
        return -value;
      }
    });

    // add * operation
    addOp("*", new Operation() {
      @Override
      public int getPriority() {
        return 2;
      }

      @Override
      public double calc(double num1, double num2) {
        return num1 * num2;
      }
    });

    // add mod operation
    addOp("mod", new Operation() {
      @Override
      public int getPriority() {
        return 0;
      }

      @Override
      public double calc(double num1, double num2) {
        return num1 % num2;
      }
    });
  }

  /**
   * parses the given formula
   * 
   * @param formula
   *          given formula as a string
   * 
   * @return solution
   */
  public double parse(String formula) {
    // tokenize the formula
    Tokenizer t = new Tokenizer();
    List<Token> tokens = t.tokenize(formula, operations.keySet(),
        functions.keySet());

    // check if syntax is correct
    int count = 0;
    for (Token token : tokens) {
      if (token.isOpen())
        count++;
      if (token.isClose())
        count--;

      if (count < 0)
        throw new IllegalArgumentException("there is a missing bracket");
    }

    if (count > 0)
      throw new IllegalArgumentException("closing bracket is missing");

    if (count < 0)
      throw new IllegalArgumentException("open bracket is missing");

    // parse expression
    int size = tokens.size();
    while (size > 1) {
      findInnerExpression(tokens, 0);
      if (tokens.size() >= size) {
        throw new IllegalArgumentException("there is a problem");
      }
      size = tokens.size();
    }

    if (size != 1 || !(tokens.get(0).isNum()))
      throw new IllegalArgumentException("there is a problem");

    return tokens.get(0).getNum();
  }

  /**
   * finds an expression to solute
   * 
   * @param formula
   *          given formula as token list
   * @param start
   *          start index
   */
  private void findInnerExpression(List<Token> formula, int start) {
    // if formula[start] is an open bracket remove it
    if (formula.get(start).isOpen()) {
      formula.remove(start);
    }

    int index = start;
    int length = 0;
    boolean done = false;

    while (index + length < formula.size()) {
      // if this index is another open bracket, start parsing from this
      // position again
      if (formula.get(index + length).isOpen()) {
        findInnerExpression(formula, index + length);
      }
      // if a close bracket is found, solute the inner expression
      else if (formula.get(index + length).isClose()) {
        // if length is zero, there is a problem
        if (length == 0)
          break;

        // parse this expression and remove it
        parseExpression(formula, index, length);
        formula.remove(index + 1);
        length = 0;

        if (formula.get(index + length).isClose()) {
          done = true;
          break;
        }

        index++;
      } else
        length++;
    }

    // there are no more brackets, solute last expression
    if (!done && index + length == formula.size()) {
      parseExpression(formula, index, length);
      index++;
      length = 0;
    }
  }

  /**
   * parses a given Part of the formula and replaces it with the result
   * 
   * @param formula
   *          given Formula as a token list
   * @param start
   *          start index in formula
   * @param length
   *          length of the expression to solute
   */
  private void parseExpression(List<Token> formula, int start, int length) {
    // get all functions and calc them
    int length2 = length;
    for (int i = start + length2 - 2; i >= start; i--) {
      Function func = functions.get(formula.get(i).getSign());

      if (func != null) {
        // the sign is a function if there is no left number
        if (i == start || !formula.get(i - 1).isNum()) {
          double val = 0;
          if (formula.get(i + 1).isNum())
            val = formula.get(i + 1).getNum();
          else
            throw new IllegalArgumentException("no parameter  given");

          // replace the function with there solution
          formula.remove(i + 1);
          formula.set(i, new Token(String.valueOf(func.calc(val)), true));
          length2--;
        }
      }
    }

    // check for operations
    while (length2 > 1) {
      int curr = length2;

      // get highest Priority
      int prio = -1;
      for (int i = start; i < start + length2; i++) {
        if (formula.get(i).isSign()) {
          prio = Math.max(prio, operations.get(formula.get(i).getSign())
              .getPriority());
        }
      }

      // search operation with this priority
      for (int i = start; i < start + length2 - 1; i++) {
        if (formula.get(i).isSign()) {
          // check if it has highest priority
          Operation op = operations.get(formula.get(i).getSign());
          if (prio == op.getPriority()) {
            // get arguments
            double left = -1;
            double right = -1;
            if (formula.get(i - 1).isNum())
              left = formula.get(i - 1).getNum();
            else
              throw new IllegalArgumentException("wrong parameters");
            if (formula.get(i + 1).isNum())
              right = formula.get(i + 1).getNum();
            else
              throw new IllegalArgumentException("wrong parameters");

            // calc result and replace it with operation
            formula.set(i,
                new Token(String.valueOf(op.calc(left, right)), true));
            formula.remove(i + 1);
            formula.remove(i - 1);
            i--;
            length2 -= 2;
          }
        }
      }

      if (length2 == curr)
        throw new IllegalArgumentException("there is a problem");
    }
  }

  private void addOp(String name, Operation operation) {
    operations.put(name, operation);
  }

  private void addFunc(String name, Function function) {
    functions.put(name, function);
  }

  /**
   * checks if one of the numbers is a double
   * 
   * @param formula
   *          given formula as string
   * 
   * @return true if at least one number is a double, else false
   */
  public boolean containsInvalidNumber(String formula) {
    Tokenizer t = new Tokenizer();
    List<Token> tokens = t.tokenize(formula, operations.keySet(),
        functions.keySet());

    for (int i = 0; i < tokens.size(); i++) {
      if (tokens.get(i).isDouble())
        return true;
    }

    return false;
  }
}