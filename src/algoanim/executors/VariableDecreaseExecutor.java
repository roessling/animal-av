package algoanim.executors;

import java.util.Vector;

import algoanim.annotations.Annotation;
import algoanim.annotations.Executor;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;

public class VariableDecreaseExecutor extends Executor
{
	public VariableDecreaseExecutor(Variables vars, SourceCode src)
	{
		super(vars, src);
	}
	
	@Override
	public boolean exec(Annotation anno)
	{
		if(anno.getName().equals("dec"))
		{
			Vector<String> param = anno.getParameters();
			String var = param.get(0);
			int value;
			if(vars.get(var) == null)
				value = 1;
			else
				value = Integer.parseInt(vars.get(var)) - 1;
			
			vars.set(var, String.valueOf(value));
			
			return true;
		}
		
		return false;
	}

}
