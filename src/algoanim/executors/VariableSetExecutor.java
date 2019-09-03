package algoanim.executors;

import java.util.Vector;

import algoanim.annotations.Annotation;
import algoanim.annotations.Executor;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;

public class VariableSetExecutor extends Executor
{
	public VariableSetExecutor(Variables vars, SourceCode src)
	{
		super(vars, src);
	}
	
	@Override
	public boolean exec(Annotation anno)
	{
		if(anno.getName().equals(Annotation.SET))
		{
			Vector<String> param = anno.getParameters();
			vars.set(param.get(0), param.get(1));
			
			return true;
		}
		
		return false;
	}

}
