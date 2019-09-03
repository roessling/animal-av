package algoanim.variables;

public class StringVariable extends Variable {

	private static final String defaultValue = "";
	private String value;

	public StringVariable() {
		this(defaultValue);
	}

	public StringVariable(String value) {
		super(VariableTypes.STRING);
		this.value = value;
	}

	public <T> T getValue(Class<T> type) {
		T result = null;

		if (type == String.class) {
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

	public void setValue(String value) {
		this.value = value;
	}

	public void setValue(Variable var) {
		setValue(var.getValue(String.class));
	}

	@Override
	public String toString() {
		return value;
	}
}
