package algoanim.executors;

import java.util.Vector;

import algoanim.annotations.Annotation;
import algoanim.annotations.Executor;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;

public class VariableDeclareExecutor extends Executor {
  public VariableDeclareExecutor(Variables vars, SourceCode src) {
    super(vars, src);
  }

  @Override
  public boolean exec(Annotation anno) {
    if (anno.getName().equals(Annotation.DECLARE)) {
      Vector<String> param = anno.getParameters();

      // type, name, value, role
      if (param.size() == 4)
        vars.declare(param.get(0), param.get(1), param.get(2), param.get(3));
      
      // type, name, value
      else if (param.size() == 3)
        vars.declare(param.get(0), param.get(1), param.get(2));

      // type, name
      else if (param.size() == 2)
        vars.declare(param.get(0), param.get(1));

      // error
      else
        System.err.println("wrong parameter count for @declare");

      return true;
    }

    return false;
  }
}
