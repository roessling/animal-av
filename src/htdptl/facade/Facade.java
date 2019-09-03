package htdptl.facade;

import htdptl.animation.AnimationDispatcher;
import htdptl.animation.Animator;
import htdptl.animators.CondAnimator;
import htdptl.animators.DefaultAnimator;
import htdptl.animators.FilteredTraceStepsAnimator;
import htdptl.animators.IfAnimator;
import htdptl.animators.ProcedureAnimator;
import htdptl.animators.StructAnimator;
import htdptl.animators.VariableAnimator;
import htdptl.ast.AST;
import htdptl.ast.ASTFactory;
import htdptl.exceptions.NoExpressionsException;
import htdptl.exceptions.StepException;
import htdptl.exceptions.TraceTooLargeException;
import htdptl.filter.BreakpointFilter;
import htdptl.filter.IFilter;
import htdptl.filter.ProcedureFilter;
import htdptl.parser.Parser;
import htdptl.stepHandlers.CondHandler;
import htdptl.stepHandlers.IfHandler;
import htdptl.stepper.Stepper;
import htdptl.traces.TraceManager;
import htdptl.visitors.VisitorUtil;

import java.util.ArrayList;
import java.util.Iterator;

import jsint.InputPort;
import jsint.Pair;
import jsint.U;

/**
 * This class is used by the HtDPTLWizard. It stores filters, configures the
 * AnimationDispatcher and generates animations using the TraceManager and the
 * class Animation
 * 
 */
public class Facade {

  private Stepper            stepper;
  private Animator           animator;
  private ArrayList<Object>  definitions = new ArrayList<Object>();
  private ArrayList<Object>  structures  = new ArrayList<Object>();
  private ArrayList<Object>  expressions = new ArrayList<Object>();

  private String             title;
  private ArrayList<IFilter> filters     = new ArrayList<IFilter>();

  public Facade() {

  }

  public Facade(String title) {
    this.title = title;
  }

  public void input(String sexp) throws NoExpressionsException {

    if (!(sexp.contains("(") || sexp.contains("["))) {
      throw new NoExpressionsException();
    }

    definitions.clear();
    structures.clear();
    expressions.clear();

    AnimationDispatcher.clean();
    AnimationDispatcher.addAnimator("if", new IfAnimator());
    AnimationDispatcher.addAnimator("map", new ProcedureAnimator());
    AnimationDispatcher.addAnimator("foldl", new ProcedureAnimator());
    AnimationDispatcher.addAnimator("foldr", new ProcedureAnimator());
    AnimationDispatcher.addAnimator("filter", new ProcedureAnimator());
    AnimationDispatcher.addAnimator("cond", new CondAnimator());
    AnimationDispatcher.setDefaultAnimator(new DefaultAnimator());
    AnimationDispatcher
        .setFilteredTraceStepAnimator(new FilteredTraceStepsAnimator());

    try {
      stepper = new Stepper();
      stepper.addHandler("if", new IfHandler());
      stepper.addHandler("cond", new CondHandler());

      Object def_or_expr;
      Parser.setInput(sexp);
      while ((def_or_expr = Parser.nextExpression()) != InputPort.EOF) {

        String operator = U.first(def_or_expr).toString();
        // found a definition
        if (operator.equals("define")) {
          handleDefine(def_or_expr);         
        } 
        // found a structure definition
        else if (operator.equals("define-struct")) {
          structures.add(def_or_expr);
          stepper.defineStruct(def_or_expr);
          addStructAnimators(ASTFactory.build(def_or_expr));
        } 
        // test cases are added to the expressions list
        else if (operator.equals("check-within")
            || operator.equals("check-expect")) {
          def_or_expr = U.second(def_or_expr);
          expressions.add(def_or_expr);
        } 
        // check-error tests are excluded
        else if (!operator.equals("check-error")) {
          stepper.setExpression(def_or_expr);
          if (!stepper.isDone()) {
            expressions.add(def_or_expr);
          }
        }
      }
    } catch (StepException e) {
      e.printStackTrace();
    }

  }

  public void addFilter(IFilter filter) {
    this.filters.add(filter);
  }

  public int animate() throws TraceTooLargeException {
    animator = new Animator(title);
    animator.intro(expressions);

    ArrayList<IFilter> clones = new ArrayList<IFilter>();
    for (Iterator<IFilter> iterator = filters.iterator(); iterator.hasNext();) {
      IFilter filter = iterator.next();
      clones.add(filter.clone());
    }

    TraceManager[] traceManagers = new TraceManager[expressions.size()];
    int steps = 0;

    for (int i = 0; i < expressions.size(); i++) {
      Object exp = expressions.get(i);

      try {

        stepper.setExpression(exp);
        traceManagers[i] = new TraceManager(stepper);
        if (filters.size() > 0) {
          for (Iterator<IFilter> iterator = clones.iterator(); iterator
              .hasNext();) {
            IFilter filter = iterator.next();
            traceManagers[i].addFilter(filter);
          }
        }
        traceManagers[i].buildTrace();

        steps += traceManagers[i].numSteps();
        traceManagers[i].reset();
        stepper.clearObserver();
        if (steps > 300) {
          System.out.println("too large: " + steps);
          throw new TraceTooLargeException("Aborted at step " + steps);
        }

      } catch (StepException e) {
        e.printStackTrace();
      }
    }
    for (int i = 0; i < expressions.size(); i++) {
      animator.animate(traceManagers[i], i);
    }
    animator.outro();
    System.out.println("total: " + steps);
    System.out.println();
    return steps;

  }

  private void handleDefine(Object ASlist) throws StepException {
    Object symbol = U.second(ASlist);
    stepper.defineSymbol(ASlist);
    if (symbol instanceof Pair) {
      String procedureName = U.first(U.first(U.rest(ASlist))).toString();
      AnimationDispatcher.addAnimator(procedureName, new ProcedureAnimator());
      definitions.add(procedureName);
    } else {
      AnimationDispatcher
          .addAnimator(symbol.toString(), new VariableAnimator());
      definitions.add(symbol);
    }
  }

  private void addStructAnimators(AST structDefinition) {
    String structName = VisitorUtil.toCode(structDefinition.getChild(0)
        .getOperator());
    AnimationDispatcher.addAnimator(structName + "?", new StructAnimator());

    ArrayList<String> fields = VisitorUtil.getParameters(structDefinition
        .getChild(1));
    for (Iterator<String> iterator = fields.iterator(); iterator.hasNext();) {
      String field = iterator.next();
      AnimationDispatcher.addAnimator(structName + "-" + field,
          new StructAnimator());
    }
  }

  public String getScriptCode() {
    return animator.getScriptCode();
  }

  private String buildString(ArrayList<Object> list) {
    String result = "";
    for (Iterator<Object> iterator = list.iterator(); iterator.hasNext();) {
      Object exp = iterator.next();

      result += exp + "\n";
    }
    return result;
  }

  public ArrayList<Object> getDefinitions() {
    return definitions;
  }

  public String getStructures() {
    return buildString(structures);
  }

  public ArrayList<Object> getExpressions() {
    return expressions;
  }

  public void setExpressions(ArrayList<Object> expressions) {
    this.expressions = expressions;
  }

  public void setFilters(ArrayList<IFilter> filters) {
    this.filters = filters;
  }

  public IFilter getFilter(int i) {
    return filters.get(i);
  }

  public void addProcedureFilter(String procedure, int times) {
    filters.add(new ProcedureFilter(procedure, times));
  }

  public void addBreakpoint(String procedure, int times) {
    filters.add(new BreakpointFilter(procedure, times));
  }

  public void remove(int index) {
    filters.remove(index);
  }

  public int getNumFilters() {
    return filters.size();
  }

}
