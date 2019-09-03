package htdptl.traces;

import htdptl.ast.AST;
import htdptl.visitors.NormalformVisitor;
import htdptl.visitors.RedexVisitor;
import htdptl.visitors.VisitorUtil;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * The Trace is represented as a list of TraceSteps. A TraceStep contains the
 * current expression the redex and additional information that is later used
 * when creating the animation.
 * 
 */
public class TraceStep {

  private AST               ast;
  private AST               redex;
  private AST               redexValue;
  private AST               definition;
  private AST               resolvedBody;
  private HashMap<AST, AST> currentSubstitution;
  private ArrayList<AST>    substitutedExpressions;

  public TraceStep(AST ast, NormalformVisitor pv) {
    /*
     * Since a clone of the current AST is stored, a new reference to the redex
     * has to be computed
     */
    AST clone = ast.clone();
    RedexVisitor rv = new RedexVisitor(pv);
    clone.accept(rv);
    AST redex = rv.getRedex();

    if (redexValue != null) {
      redexValue = redexValue.clone();
    }

    this.ast = clone;
    this.redex = redex;
  }

  public void setRedexValue(AST redexValue) {
    this.redexValue = redexValue.clone();
  }

  public AST getAst() {
    return ast;
  }

  public AST getRedex() {
    return redex;
  }

  public String getRedexValue() {
    return VisitorUtil.toCode(redexValue);
  }

  @Override
  public String toString() {
    String result = "";
    result += VisitorUtil.toCode(ast) + "\n";
    result += VisitorUtil.toCode(redex) + "\n";
    result += VisitorUtil.toCode(redexValue) + "\n";
    result += VisitorUtil.toCode(definition) + "\n";
    result += VisitorUtil.toCode(resolvedBody) + "\n";
    result += currentSubstitution + "\n";
    return result;
  }

  public void setDefinition(AST definition) {
    this.definition = definition;
  }

  public AST getDefinition() {
    return definition;
  }

  public void setResolvedBody(AST resolvedBody) {
    this.resolvedBody = resolvedBody;
  }

  public AST getResolvedBody() {
    return resolvedBody;
  }

  public void setSubstitution(HashMap<AST, AST> hashMap) {
    this.currentSubstitution = hashMap;
  }

  public HashMap<AST, AST> getSubstitution() {
    return currentSubstitution;
  }

  public void setSubstitutedExpressions(ArrayList<AST> substitutedExpressions) {
    this.substitutedExpressions = substitutedExpressions;
  }

  public ArrayList<AST> getSubstitutedExpressions() {
    return substitutedExpressions;
  }

}
