package htdptl.parser;

import java.io.StringReader;

import jsint.InputPort;

/**
 * The parser of JScheme is used to parse HtDP-TL expressions. The JScheme Parser
 * returns nested lists which are used to create corresponding AST objects
 * 
 */
public class Parser {

  static InputPort    ip;
  static StringReader sr;

  public static Object parse(String sexp) {
    StringReader sr = new StringReader(sexp);
    InputPort ip = new InputPort(sr);
    return ip.read();
  }

  public static void setInput(String definition) {
    sr = new StringReader(definition);
    ip = new InputPort(sr);
  }

  public static Object nextExpression() {
    Object result = ip.read();
    return result;
  }
}
