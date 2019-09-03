package generators.helpers;

/**
* Helfer-Datei zur Datei VogelApprox.java. Die Datei definiert eine Klasse, die als Verbund (Record)
* fungiert. Hintergrund ist, dass zu einem gegebenen Wert (int wert) stehts seine Markierung (boolean markiert)
* untrennbar verknueft sein soll. So kann (wie im Algorithmus zur Vogelschen Approximation) auf einfache Weise
* nach Werten sortiert werden, wobei die Markierung quasi "mit sortiert" wird.
* 
*
*/
public class VogelApproxHelper implements Comparable <VogelApproxHelper> {

	int wert;
	public int index;
	public boolean markiert;
	
	public VogelApproxHelper(int a, int b, boolean c) {
		this.wert = a;
		this.index = b;
		this.markiert = c; 
	}

	
	public int compareTo(VogelApproxHelper that) {
		if (this.wert < that.wert) {
			return -1;
			
		} else {
			if (this.wert == that.wert) {
				return 0;
			} else {
				return +1;
			}
		}
	}
	
	

	

	
}