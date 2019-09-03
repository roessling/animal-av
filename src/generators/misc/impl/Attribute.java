package generators.misc.impl;

public class Attribute {

	private String symbol;

    @java.beans.ConstructorProperties({"symbol"})
    public Attribute(String symbol) {
        this.symbol = symbol;
    }

    @Override
	public String toString() {
		return symbol;
	}

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Attribute)) return false;
        final Attribute other = (Attribute) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$symbol = this.getSymbol();
        final Object other$symbol = other.getSymbol();
        if (this$symbol == null ? other$symbol != null : !this$symbol.equals(other$symbol)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $symbol = this.getSymbol();
        result = result * PRIME + ($symbol == null ? 43 : $symbol.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Attribute;
    }
}
