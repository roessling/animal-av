package algoanim.variables;

public class BooleanVariable extends Variable {

	private static final Boolean defaultValue = Boolean.FALSE;
	private boolean value;

	public BooleanVariable() {
		this(defaultValue);
	}

	public BooleanVariable(boolean boolValue) {
		super(VariableTypes.BOOLEAN);
		value = boolValue;
	}

	public <T> T getValue(Class<T> type) {
		T result = null;

		if (type == Boolean.class) {
			result = type.cast(value);
		} else {
			System.err.println("cannot cast variable to: " + type);
		}

		return result;
	}

	public void setValue(Boolean value) {
		setValue(value.toString());
	}

	public void setValue(Byte value) {
		setValue(value.toString());
	}

	public void setValue(Double value) {
		setValue(value.toString());
	}

	public void setValue(Float value) {
		setValue(value.toString());
	}

	public void setValue(Integer value) {
		setValue(value.toString());
	}

	public void setValue(Long value) {
		setValue(value.toString());
	}

	public void setValue(Short value) {
		setValue(value.toString());
	}

	public void setValue(String stringValue) {
		value = ("true".equalsIgnoreCase(stringValue));
	}

	public void setValue(Variable var) {
		setValue(var.getValue(String.class));
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
