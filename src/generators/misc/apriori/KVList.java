package generators.misc.apriori;

import java.awt.Color;
import algoanim.primitives.generators.Language;
import algoanim.properties.TextProperties;
import algoanim.util.Node;

public class KVList<K,V> extends AList<KVPair<K,V>> {
	
	private final String delimiter = ": ";
	
	public KVList(Language lang, String ident, Node upperElem,
			TextProperties propText, TextProperties propTitle, Color clrHighlight) {
		super(lang, ident, upperElem, propText, propTitle, clrHighlight);
	}
	
	protected String cToString(KVPair<K,V> in) {
		if(in.getV() != "-1")
			return keyToString(in.getK())+delimiter+valueToString(in.getV());
		else
			return keyToString(in.getK());
	}

	protected String keyToString(K k) {
		return k.toString();
	}
	
	protected String valueToString(V v) {
		return v.toString();
	}
	
	public void setValue(int entry, V value) {
		KVPair<K, V> temp = new KVPair<K, V>(entries.get(entry).getK(), value);
		setEntry(entry, temp);
	}
	
	public void addEntry(K k, V v) {
		KVPair<K,V> neuf = new KVPair<K,V>(k,v);
		addEntry(neuf);
	}
}
