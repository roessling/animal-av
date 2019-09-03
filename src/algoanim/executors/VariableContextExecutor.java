package algoanim.executors;

import algoanim.annotations.Annotation;
import algoanim.annotations.Executor;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;

public class VariableContextExecutor extends Executor
{
	public VariableContextExecutor(Variables vars, SourceCode src) {
		super(vars, src);
	}

	@Override
	public boolean exec(Annotation anno) {
		if(anno.getParameters().size() == 0) // no parameters
		{
			if(anno.getName().equals(Annotation.OPENCONTEXT))
			{
				vars.openContext();
				return true;
			}

			if(anno.getName().equals(Annotation.CLOSECONTEXT))
			{
				vars.closeContext();
				return true;
			}
		}
		return false;
	}

}
