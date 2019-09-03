package algoanim.executors;

import java.util.Vector;

import algoanim.annotations.Annotation;
import algoanim.annotations.Executor;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Variables;

public class HighlightExecutor extends Executor
{
	public HighlightExecutor(Variables vars, SourceCode src) {
		super(vars, src);
	}

	@Override
	public boolean exec(Annotation anno)
	{
		if(anno.getName().equals(Annotation.HIGHLIGHT)
		&& anno.getParameters().size() == 1) // label
		{
			Vector<String> params = anno.getParameters();
			src.highlight(params.get(0));

			return true;
		}

		return false;
	}

}
