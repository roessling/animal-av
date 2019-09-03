package algoanim.executors;

import java.util.Vector;

import algoanim.annotations.Annotation;
import algoanim.annotations.Executor;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;

public class VariableDiscardExecutor extends Executor
{
	public VariableDiscardExecutor(Variables vars, SourceCode src)
	{
		super(vars, src);
	}
	
	@Override
	public boolean exec(Annotation anno)
	{
		if(anno.getName().equals(Annotation.DISCARD))
		{
			Vector<String> param = anno.getParameters();
			vars.discard(param.get(0));
			
			return true;
		}
		
		return false;
	}

}
