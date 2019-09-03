package htdptl.visitors;

import htdptl.ast.AST;
import htdptl.ast.Expression;
import htdptl.ast.IVisitor;
import htdptl.ast.Leaf;
import htdptl.environment.Environment;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * This visitor computes whether the AST is in normal form, that is the AST was
 * completely reduced to a primitive expression
 * 
 */
public class NormalformVisitor implements IVisitor {

  private boolean           isPrimitive;

  private ArrayList<String> primitiveSymbols;
  private Environment       environment;

  public NormalformVisitor(Environment environment) {
    this.environment = environment;
    primitiveSymbols = new ArrayList<String>();
    primitiveSymbols.add("true");
    primitiveSymbols.add("false");
    primitiveSymbols.add("else");
    primitiveSymbols.add("empty");

  }

  public boolean isPrimitive() {
    return isPrimitive;
  }

  @Override
  public void visit(Expression expression) {
    String operator = VisitorUtil.toCode(expression.getOperator());

    if (environment.isProcedure(operator)) {
      isPrimitive = false;
    }

    // lists, vectors and structures are primitive if all children are
    // primitive
    else if (operator.equals("list") || operator.equals("vector")
        || operator.startsWith("make-")
        && environment.isStructProcedure(operator)
        || environment.isProcedure(operator)) {
      for (Iterator<AST> iterator = expression.getExpressions().iterator(); iterator
          .hasNext();) {
        AST child = iterator.next();
        NormalformVisitor pv = new NormalformVisitor(environment);
        child.accept(pv);
        if (!pv.isPrimitive) {
          isPrimitive = false;
          return;
        }
      }
      isPrimitive = true;
    }
    // quotes are primitive. List abbreviations are resolved while building
    // the AST
    else if (operator.equals("quote") || primitiveSymbols.contains(operator)) {
      isPrimitive = true;
    } else {
      isPrimitive = false;
    }

  }

  @Override
  public void visit(Leaf leaf) {
    if (environment.isVariable(VisitorUtil.toCode(leaf))) {
      isPrimitive = false;
    } else {
      isPrimitive = true;
    }

  }

}
