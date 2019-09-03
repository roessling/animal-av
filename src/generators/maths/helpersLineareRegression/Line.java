package generators.maths.helpersLineareRegression;

public class Line {
	
	public double calcAverageX(Point[] cloud) {
		double result = 0;
		for(int i = 0; i < cloud.length; i++) result += cloud[i].x;
		return result / cloud.length;
	}
	
	public double calcAverageY(Point[] cloud) {
		double result = 0;
		for(int i = 0; i < cloud.length; i++) result += cloud[i].y;
		return result / cloud.length;
	}
	
	public double calcEmpiricalCovariance(Point[] cloud) {
		double averageX = this.calcAverageX(cloud);
		double averageY = this.calcAverageY(cloud);
		double result = 0;
		
		for(int i = 0; i < cloud.length; i++) {
			result += (cloud[i].x - averageX) * (cloud[i].y - averageY);
		}
		
		return result;
	}
	
	public double calcEmpiricalVarianceOfX(Point[] cloud) {
		double averageX = this.calcAverageX(cloud);
		double result = 0;
		
		for(int i = 0; i < cloud.length; i++) {
			result += Math.pow(cloud[i].x - averageX, 2);
		}
		
		return result;	
	}
	
	public double calcA(Point[] cloud) {
		return this.calcEmpiricalCovariance(cloud) / this.calcEmpiricalVarianceOfX(cloud);
	}
	
	public double calcB(Point[] cloud) {
		double averageX = this.calcAverageX(cloud);
		double averageY = this.calcAverageY(cloud);
		
		return averageY - (this.calcA(cloud) * averageX);
	}
	
	public double getValue(int x, Point[] cloud) {
		//System.out.println(this.calcB(cloud));
		return this.calcA(cloud) * x + this.calcB(cloud);
	}
}
