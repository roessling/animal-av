package generators.maths.faddejewleverrier;

import java.util.Arrays;
import algoanim.primitives.DoubleArray;
import algoanim.primitives.DoubleMatrix;
import java.lang.Math;

public class MatrixOperations{
	private int n;

  public double[][] mkI(){
  	double[][] d = new double[n][n];
  	for(int i = 0; i < n; i++)
  		d[i][i]=1.0;
  	return d;
  }

  public double det(double c0){
  	return Math.pow(-1,n)*c0;
  }
  
  public boolean notZero(DoubleMatrix d){
  	return notZero(getData(d));
  }
  
  public boolean notZero(double[][] d){
    boolean b = !Arrays.equals(d,fill(new double[n][n], 0));
  	return !b;
  }
  
  public double trace(DoubleMatrix d){
  	return trace(getData(d));
  }
  
  public double trace(double[][] d){
  	double t = 0;
  	for(int i = 0; i < n; i++)
  		t = t + d[i][i];
  	return t;
  }
  
  public double[][] mult(double d, double[][]m){
  	double[][] r = new double[n][n];
  	for(int i = 0; i < n; i++)
  		for(int j = 0; j < n; j++)
  			r[i][j] = m[i][j]*d;
  	return r;
  	
  }
  
  public double[][] mult(double[][] a, double[][] b){
  	double[][] c = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				c[i][j] = 0;
				for (int k = 0; k < n; k++) {
				  c[i][j] += a[i][k] * b[k][j];
				}
			}
		}
		return c;
  }
  
  public double[][] mult(DoubleMatrix A, DoubleMatrix B){
  	return mult(getData(A),getData(B));
  }
  
  
  public double[][] add(DoubleMatrix A, DoubleMatrix B){
  	return add(getData(A),getData(B));
  }
  
  public double[][] add(double[][]a,double[][]b){
  	double[][]d = new double[n][n];
  	for(int i = 0; i < n; i++)
  		for(int j = 0; j < n; j++)
  	    d[i][j]=a[i][j]+b[i][j];
  	return d;
  }
  
  public String[][] doubleToString(double[][]d){
  	String[][] s = new String[n][n];
  	for(int i = 0; i < n; i++)
  		for(int j = 0; j < n; j++)
  			s[i][j]=Double.toString(d[i][j]);
  	return s;
  }
  
  public double[][] stringToDouble(String[][]s){
  	double[][] d = new double[n][n];
  	for(int i = 0; i < n; i++)
  		for(int j = 0; j < n; j++)
  			d[i][j]=Double.valueOf(s[i][j]);
  	return d;
  }
  
  public void fill(DoubleMatrix m, double what){
  	set(m,fill(new double[n][n],what));
  }
  
  public double[][] fill(double[][]d, double what){
  	for(int i = 0; i < n; i++){
  		for(int j = 0; j < n; j++)
  			d[i][j]=what;
  	}
  	return d;
  }
  
  
	public void setN(int n){
		this.n = n;
	}
  
  public void set(DoubleMatrix m, double[][] d){
  	for(int i = 0; i < n; i++)
  		for(int j = 0; j < n; j++)
  			put(m, i, j, d[i][j]);
  }
  public void set(DoubleArray m, double[]d){
  	for(int i = 0; i < d.length; i++)
  		m.put(i, d[i], null, null);
  }
	private int round = 3;
	public void setRound(int round){
		if(round>0)
		  this.round = round;
	}
	public int getRound(){
		return round;
	}
	
	public double rint(double what){
		double r = Math.pow(10, round); 
		r = Math.rint(what*r)/r;
		return r;
	}
  public void put(DoubleArray a, int where, double what){
  	a.put(where, rint(what), null, null);
  }
  
  public void put(DoubleMatrix a, int i, int j, double what){
  	a.put(i, j, rint(what), null, null);
  }
  
  public double getData(DoubleArray a, int i){
  	return a.getData(i);
  }
  
  public double getData(DoubleMatrix M, int i, int j){
  	return M.getElement(i, j);
  }
  
  public double[][]getData(DoubleMatrix M){
	  double[][] d = new double[n][n];
	  for(int i = 0; i < n; i++)
		  for(int j = 0; j < n; j++)
			  d[i][j] = M.getElement(i, j);
  	return d;
  }
  
  public double[] getData(DoubleArray A){
  	double[] d = new double[A.getLength()];
  	for(int i = 0; i < A.getLength();i++)
  		d[i]=A.getData(i);
  	return d;
  }
  
  public void print(DoubleArray A){
  	print(getData(A));
  	System.out.println("   #---#   ");
  }
  
  public void print(DoubleMatrix M){
  	print(getData(M));
  }
  
  public void print(double[][]d){
    for(double[] row : d)
    	print(row);
    System.out.println("   #---#   ");

  }
  
  public void print(double[]d){
  	System.out.print("# ");
    for(double j : d)
  		System.out.print(j+" ");
  	System.out.println("# ");
  }
 
  public double[][] convert(int[][]m){
  	double[][]dA = new double[n][n];
		for(int i = 0; i < n; i++)
			for(int j = 0; j < n; j++)
				dA[i][j] = (double)m[i][j];
		return dA;
  }
  
}
