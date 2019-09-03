package algoanim.annotations;

import java.util.Vector;

import algoanim.executors.EvalExecutor;
import algoanim.executors.GlobalExecutor;
import algoanim.executors.HighlightExecutor;
import algoanim.executors.VariableContextExecutor;
import algoanim.executors.VariableDeclareExecutor;
import algoanim.executors.VariableDecreaseExecutor;
import algoanim.executors.VariableDiscardExecutor;
import algoanim.executors.VariableIncreaseExecutor;
import algoanim.executors.VariableRoleExecutor;
import algoanim.executors.VariableSetExecutor;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;

public class ExecutorManager {
  protected Variables        vars;
  protected SourceCode       src;

  protected Vector<Executor> executors;

  public ExecutorManager(Variables vars, SourceCode src) {
    this.vars = vars;
    this.src = src;

    executors = new Vector<Executor>();
    executors.add(new VariableDeclareExecutor(vars, src));
    executors.add(new VariableRoleExecutor(vars, src));
    executors.add(new VariableSetExecutor(vars, src));
    executors.add(new VariableDiscardExecutor(vars, src));
    executors.add(new VariableDecreaseExecutor(vars, src));
    executors.add(new VariableIncreaseExecutor(vars, src));
    executors.add(new EvalExecutor(vars, src));
    executors.add(new HighlightExecutor(vars, src));
    executors.add(new GlobalExecutor(vars, src));
    executors.add(new VariableContextExecutor(vars, src));
  }

  public boolean exec(Vector<Annotation> annos) {
    boolean executed = true;

    for (Annotation anno : annos) {
      executed = executed && this.exec(anno);
    }

    return executed;
  }

  public boolean exec(Annotation anno) {
    boolean executed = false;

    for (Executor executor : executors) {
      executed = executed || executor.exec(anno);
    }

    if (!executed && !anno.getName().equals("label") // no executor needed, only
                                                     // for parsing
        && !anno.getName().equals("continue")) // no executor needed, only for
                                               // parsing
      System.err.println("no executor found for annotation \"" + anno.getName()
          + "\"");

    return executed;
  }
}
