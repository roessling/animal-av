package htdptl.stepper;

import htdptl.ast.AST;
import htdptl.ast.ASTFactory;
import htdptl.ast.Expression;
import htdptl.ast.Leaf;
import htdptl.parser.Parser;
import htdptl.visitors.VisitorUtil;
import jsint.Pair;
import jsint.Symbol;
import jsint.U;

/**
 * This IHandler evaluates primitive procedures using the JScheme Interpreter.
 * The result, that is returned by JScheme, is converted to an AST object.
 * 
 */
public class PrimitiveProcedureHandler implements IHandler {

  @Override
  public void step(IStepper stepper) {
    AST result;

    Object sexp = Parser.parse(VisitorUtil.toCode(stepper.getRedex()));

    Object value = stepper.eval(sexp);
    if (value instanceof Symbol) {
      // 'barbie is evaluated to symbol barbie
      result = new Leaf("'" + value);
    } else if (value == Pair.EMPTY) {
      result = new Leaf(Symbol.intern("empty"));
    } else if (value instanceof Pair) {
      // (rest (list 1 2 3)) evaluates to (2 3)
      result = handleLists(value);
    } else {
      result = ASTFactory.build(value);
    }

    stepper.replaceRedex(result);

  }

  private Expression handleLists(Object value) {
    Object myValue = value;
    Expression result;
    result = new Expression();
    result.addChild(new Leaf(Symbol.intern("list")));
    while (myValue != Pair.EMPTY) {
      Object element = U.first(myValue);
      // nested list
      if (element instanceof Pair) {
        result.addChild(handleLists(element));
      }
      // list element
      else {
        /*
         * special case if e.g. (first (list (make-xy 2 3) (make-xy 1 3))) is
         * evaluated since (make-xy 2 3) is not evaluated the representation (an
         * Object[]) of the interpreter has to be parsed
         */
        if (element instanceof Object[]) {
          result.addChild(ASTFactory.build(element));
        } else {
          if (element instanceof Symbol) {
            element = "'" + element;
          }
          result.addChild(new Leaf(element));
        }
      }
      myValue = U.rest(myValue);
    }
    return result;
  }

}
