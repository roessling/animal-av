package generators;


class VerbundWertMarkierung implements Comparable<VerbundWertMarkierung> {

	int wert;
	int index;
	boolean markiert;
	
	public VerbundWertMarkierung (int a, int b, boolean c) {
		this.wert = a;
		this.index = b;
		this.markiert = c; 
	}

	
	public int compareTo (VerbundWertMarkierung that) {
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
