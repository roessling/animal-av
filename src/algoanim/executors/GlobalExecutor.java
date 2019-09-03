package algoanim.executors;

import java.util.Vector;

import algoanim.annotations.Annotation;
import algoanim.annotations.Executor;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;

public class GlobalExecutor extends Executor
{
	public GlobalExecutor(Variables vars, SourceCode src) {
		super(vars, src);
	}

	@Override
	public boolean exec(Annotation anno)
	{
		if(anno.getName().equals(Annotation.GLOBAL)
		&& anno.getParameters().size() == 1) // key
		{
			Vector<String> params = anno.getParameters();
			vars.setGlobal(params.firstElement());
			System.out.println("setting global: "+params.firstElement());

			return true;
		}

		return false;
	}

}
