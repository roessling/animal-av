package algoanim.variables;

public class VariableFactory
{
	public static Variable newVariable(String type)
	{
		if("int".equalsIgnoreCase(type))
			return new IntegerVariable();
//		else if("float".equals(type))
//			return new FloatVariable();
		else if("double".equalsIgnoreCase(type))
			return new DoubleVariable();
		else if("string".equalsIgnoreCase(type))
			return new StringVariable();
		else
			System.err.println("unknown Variable type: " + type);
		
		return null;
	}
}
