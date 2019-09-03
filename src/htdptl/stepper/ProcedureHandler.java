package htdptl.stepper;

import htdptl.ast.AST;
import htdptl.exceptions.StepException;
import htdptl.visitors.SubstitutionVisitor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This IHandler evaluates procedures that were defined in the given HtDP-TL
 * program. It substitutes the formal parameters with the arguments.
 * 
 */
public class ProcedureHandler implements IHandler {

  @Override
  public void step(IStepper stepper) throws StepException {

    HashMap<AST, AST> substitution = new HashMap<AST, AST>();

    // (define (f fp1 fp2 ...) ... )
    AST definition = stepper.getCurrentDefinition();
    ArrayList<AST> formalParameter = definition.getChild(0).getChildren();
    ArrayList<AST> arguments = stepper.getRedex().getChildren();

    // 
    if (arguments.size() != formalParameter.size()) {
      throw new StepException("wrong number of given arguments");
    }
    for (int i = 0; i < formalParameter.size(); i++) {
      substitution.put(formalParameter.get(i), arguments.get(i));
    }

    AST procedureBody = definition.getChild(1).clone();
    SubstitutionVisitor sv = new SubstitutionVisitor(substitution);
    procedureBody.accept(sv);

    stepper.setResolvedBody(procedureBody);
    stepper.setSubstitutedExpressions(sv.getSubstitutedExpressions());

    stepper.replaceRedex(procedureBody.clone());

  }

}
