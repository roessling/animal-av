package generators.misc.helpersSatGenerator;

public class TriangleSAT {
	public Vector2fSAT A;
	public Vector2fSAT B;
	public Vector2fSAT C;
	
	public Vector2fSAT edgeAB;
	public Vector2fSAT edgeAC;
	public Vector2fSAT edgeBC;
	
	public TriangleSAT(Vector2fSAT A, Vector2fSAT B, Vector2fSAT C) {
		this.A = A;
		this.B = B;
		this.C = C;
		
		edgeAB = B.sub(A);
		edgeAC = C.sub(A);
		edgeBC = C.sub(B);
	}
}
