package algoanim.variables;

public class IntegerVariable extends Variable {

	private static final Integer defaultValue = 0;
	private Integer value;

	public IntegerVariable() {
		this(defaultValue);
	}

	public IntegerVariable(Integer value) {
		super(VariableTypes.INTEGER);
		this.value = value;
	}

	public <T> T getValue(Class<T> type) {
		T result = null;

		if (type == Boolean.class) {
			Boolean b = value != 0;
			result = type.cast(b);
		} else if (type == Byte.class) {
			Byte b = value.byteValue();
			result = type.cast(b);
		} else if (type == Double.class) {
			Double d = value.doubleValue();
			result = type.cast(d);
		} else if (type == Float.class) {
			Float f = value.floatValue();
			result = type.cast(f);
		} else if (type == Integer.class) {
			result = type.cast(value);
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

	public void setValue(Boolean value) {
		if (value)
			setValue(1);
		else
			setValue(0);
	}

	public void setValue(Byte value) {
		setValue(value.intValue());
	}

	public void setValue(Double value) {
		setValue(value.intValue());
	}

	public void setValue(Float value) {
		setValue(value.intValue());
	}

	public void setValue(Integer value) {
		this.value = value;
		this.update();
	}

	public void setValue(Long value) {
		setValue(value.intValue());
	}

	public void setValue(Short value) {
		setValue(value.intValue());
	}

	public void setValue(String value) {
		try {
			setValue(Integer.parseInt(value));
		} catch (NumberFormatException eInt) {
			try {
				setValue(Float.parseFloat(value));
			} catch (NumberFormatException eFloat) {
				try {
					setValue(Double.parseDouble(value));
				} catch (NumberFormatException eDouble) {
					if ("TRUE".equalsIgnoreCase(value)
							|| "FALSE".equalsIgnoreCase(value))
						try {
							setValue(Boolean.parseBoolean(value));
						} catch (NumberFormatException eBool) {
							setError(value);
						}
					else
						setError(value);
				}

			}
		}
	}

	public void setValue(Variable var) {
		setValue(var.getValue(Integer.class));
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
