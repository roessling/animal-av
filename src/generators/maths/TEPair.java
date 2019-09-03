package generators.maths;

public class TEPair<K, V> {
	private K element0;
    private V element1;

    public static <K, V> TEPair<K, V> createPair(K element0, V element1) {
        return new TEPair<K, V>(element0, element1);
    }

    public TEPair(K element0, V element1) {
        this.element0 = element0;
        this.element1 = element1;
    }

    public K getKey() {
        return element0;
    }

    public V getValue() {
        return element1;
    }
}
