package algoanim.executors;

import java.util.Vector;

import algoanim.annotations.Annotation;
import algoanim.annotations.Executor;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;

public class VariableRoleExecutor extends Executor {
  public VariableRoleExecutor(Variables vars, SourceCode src) {
    super(vars, src);
  }

  @Override
  public boolean exec(Annotation anno) {
    if (anno.getName().equals(Annotation.VARIABLE_ROLE)) {
      Vector<String> param = anno.getParameters();

      // type, name, value
      if (param.size() == 2)
        vars.setRole(param.get(0), param.get(1));

      // error
      else
        System.err.println("wrong parameter count for @varRole");

      return true;
    }

    return false;
  }
}
