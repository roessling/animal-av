package algoanim.annotations;

import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;

public abstract class Executor {
  protected Variables  vars;
  protected SourceCode src;

  public Executor(Variables vars, SourceCode src) {
    this.vars = vars;
    this.src = src;
  }

  public abstract boolean exec(Annotation anno);
}
