package autoGraph;
/**
 * 
 * @author Marian Hieke
 *
 */
public class VectorComp {

	private double x;
	private double y;
	
	
	public VectorComp(){
		
	}
	
	public VectorComp(double x, double y){
		this.x = this.round(x);
		this.y = this.round(y);
	}
	
	/**
	 * 
	 * @return the x value of the Vector
	 */
	public double getX(){
		return this.x;
	}
	/**
	 * 
	 * @return the y value of the Vector
	 */
	public double getY(){
		return this.y;
	}
	
	/**
	 * sets the x value of the Vector to the given value
	 * @param x
	 */
	public void setX(double x){
		this.x = this.round(x);
	}
	
	/**
	 * sets the y value of the Vector to the given value
	 * @param y
	 */
	public void setY(double y){
		this.y = this.round(y);
	}
	/**
	 * 
	 * @param pos
	 * @return a new Vector that is this vector minus the given Vector
	 */
	public VectorComp sub(VectorComp pos){
		VectorComp sub = new VectorComp();
		sub.setX(this.x-pos.getX());
		sub.setY(this.y-pos.getY());
		return sub;
	}
	/**
	 * 
	 * @param pos
	 * @return a new vector that is this victor plus the given vector
	 */
	public VectorComp add(VectorComp pos){
		VectorComp add = new VectorComp();
		add.setX(this.x + pos.getX());
		add.setY(this.y + pos.getY());
		return add;
	}
	public VectorComp mul(double factor){
		VectorComp mul = new VectorComp();
		mul.setX(this.x * factor);
		mul.setY(this.y * factor);
		return mul;
	}
	
	public VectorComp div(double divisor){
		VectorComp div = new VectorComp();
		if(divisor != 0){
			div.setX(this.x / divisor);
			div.setY(this.y / divisor);
		}else{
			div.setX(this.x);
			div.setY(this.y );
		}
		
		return div;
	}
	
	/**
	 * 
	 * @return returns the 2 norm of this Vector
	 */
	public double norm(){
		double norm = 0;
		double xSqr = this.x * this.x;
		double ySqr = this.y * this.y;
		if(xSqr != 0 || ySqr != 0){
		//System.out.println("x "+this.x+" xSqr : " + xSqr);
		//System.out.println("y "+this.y+" ySqr : " + ySqr);
			norm = Math.sqrt(xSqr + ySqr);
		}
		return norm;
	}
	
	public String toString(){
		return "x : "+ this.x + " y: "+ this.y;
	}
	
	public double round(double toRound){
		double rounded = 0;
		double tmp = toRound * 10000;
		
		int z = (int)Math.round(tmp) ;
		
		rounded = z;
		rounded = rounded/ 10000;
		return rounded;
	}
	
	public boolean isBetween(VectorComp a, VectorComp b, double radius){
		boolean isBetw = false;
		if(this.x  <= max(a.getX(), b.getX())&& this.x >= min(a.getX(), b.getX()) && this.y  <= max(a.getY(),b.getY())&& this.y >= min(a.getY(), b.getY()) ){
			isBetw = true;
		}else{
			
		}
		
		return isBetw;
	}
	
	public double min(double a, double b){
			if(a < b){
				return a;
			}else{
				return b;
			}
		}
		public double max(double a, double b){
			if(a > b){
				return a;
			}else{
				return b;
			}
		}	
}
