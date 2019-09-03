package algoanim.variables;

public class DoubleVariable extends Variable {
	
	protected static final Double defaultValue = 0.0;
	protected Double value;
	
	public DoubleVariable()
	{
		this(defaultValue);
	}
	
	public DoubleVariable(Double value)
	{
		super(VariableTypes.DOUBLE);
		this.value = value;
	}
	
	@Override
	public void setValue(Byte value) {
		setValue(value.doubleValue());
	}

	@Override
	public void setValue(Long value) {
		setValue(value.doubleValue());
	}

	@Override
	public void setValue(Short value) {
		setValue(value.doubleValue());
	}

	@Override
	public <T> T getValue(Class<T> type) {
		T result = null;

		if (type == Boolean.class) {
			Boolean b = value != 0;
			result = type.cast(b);
		} else if (type == Byte.class) {
			Byte b = value.byteValue();
			result = type.cast(b);
		} else if (type == Double.class) {
			result = type.cast(value);
		} else if (type == Float.class) {
			Float f = value.floatValue();
			result = type.cast(f);
		} else if (type == Integer.class) {
			Integer i = value.intValue();
			result = type.cast(i);
		} else if (type == Long.class) {
			Long l = value.longValue();
			result = type.cast(l);
		} else if (type == Short.class) {
			Short s = value.shortValue();
			result = type.cast(s);
		} else if (type == String.class) {
			String s = value.toString();
			result = type.cast(s);
		} else {
			System.err.println("cannot cast variable to: " + type);
		}

		return result;
	}

	@Override
	public void setValue(Variable var) {
		setValue(var.getValue(Double.class));
	}

	@Override
	public void setValue(String value) {
		try { this.value = Double.parseDouble(value); }
		catch(NumberFormatException e)
		{ setError(value); }
	}

	@Override
	public void setValue(Integer value) {
		setValue(value.doubleValue());
	}

	@Override
	public void setValue(Float value) {
		setValue(value.doubleValue());
	}

	@Override
	public void setValue(Double value) {
		this.value = value;
		update();
	}

	@Override
	public void setValue(Boolean value) {
		if(value)
			setValue(1.0);
		else
			setValue(0.0);
	}

	@Override
	public String toString() {
		return value.toString();
	}

}
