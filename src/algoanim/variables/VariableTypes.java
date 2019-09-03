package algoanim.variables;

public enum VariableTypes {
	BOOLEAN, BYTE, DOUBLE, FLOAT, INTEGER, LONG, SHORT, STRING;
	
	public Class<?> getAssociatedClass()
	{
		if (this == BOOLEAN)
			return Boolean.class;
		else if (this == BYTE)
			return Byte.class;
		else if (this == DOUBLE)
			return Double.class;
		else if (this == FLOAT)
			return Float.class;
		else if (this == INTEGER)
			return Integer.class;
		else if (this == LONG)
			return Long.class;
		else if (this == SHORT)
			return Short.class;
		else
			return String.class;
	}
}
