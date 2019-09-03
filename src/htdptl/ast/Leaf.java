package htdptl.ast;


/**
 * Represents expressions with no children.  
 *
 */
public class Leaf extends AST {

	private Object value;

	public Leaf(Object value) {
		this.value = value;
	}

	@Override
	public AST clone() {
		return new Leaf(value);
	}
	
	@Override
	public AST getOperator() {
		return this;
	}

	@Override
	public void accept(IVisitor visitor) {
		visitor.visit(this);
	}

	public Object getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Leaf other = (Leaf) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public int numChildren() {
		return 0;
	}
	
	
}
