package generators.misc.apriori;

public class KVPair<Key, Value> {
	private Key k;
	private Value v;
	
	public Key getK() {
		return k;
	}
	public void setK(Key k) {
		this.k = k;
	}
	public Value getV() {
		return v;
	}
	public void setV(Value v) {
		this.v = v;
	}
	public KVPair(Key k, Value v) {
		super();
		this.k = k;
		this.v = v;
	}
	
	
}
