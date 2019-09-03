package algoanim.primitives.updater;

import java.util.Vector;

import algoanim.primitives.Text;
import algoanim.variables.Variable;
import algoanim.variables.VariableObserver;

public class TextUpdater implements VariableObserver {
	Vector<Object> tokens = new Vector<Object>();
//	String varText = "";
	Text text;
	
	public TextUpdater(Text text)
	{
		this.text = text;
	}
	
	public void addToken(Object o)
	{
		tokens.add(o);
		
		if(o instanceof Variable)
			((Variable)o).addObserver(this);
//		else
//			System.err.println("no variable: "+o.getClass());
	}
	
	private void generateString()
	{
		StringBuilder out = new StringBuilder(256);
		for(Object o : tokens)
			out.append(o.toString());
		
		text.setText(out.toString(), null, null);
	}

	@Override
	public void update() {
		generateString();
	}

}
