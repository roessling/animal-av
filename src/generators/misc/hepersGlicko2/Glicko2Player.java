package generators.misc.hepersGlicko2;

/**
 * 
 * @author Maxim Kuznetsov
 *
 */
public class Glicko2Player {
	

	private double rating;
	private double rating_deviation;
	private double votality;
	
	private boolean unrated = true;
	private boolean competes = true;
	
	public double phi_star;
	
	public double rating_deviation_new;
	public double rating_new;
	
	public Glicko2Player(double rating, double rating_deviation, double votality) {
		this.rating = rating;
		this.rating_deviation = rating_deviation;
		this.votality = votality;
	}
	
	public Glicko2Player(double rating, double rating_deviation, double votality, boolean rated) {
		this(rating, rating_deviation, votality);
		unrated = rated;
	}
	
	public double getRating() {
		return rating;
	}
	
	public double getRatingDev() {
		return rating_deviation;
	}
	
	public double getVotality() {
		return votality;
	}
	
	public boolean isUnrated() {
		return unrated;
	}
	
	public void competes(boolean competes) {
		this.competes = competes;
	}
	
	public boolean isCompeting() {
		return competes;
	}
	
	public double getConvertedRating() {
		return (rating - 1500.0) / 173.7178;
	}
	
	public double getConvertedRatingDev() {
		return rating_deviation / 173.7178;
	}
	
	public double estimatedVariance(Glicko2Player opponents[]) {
		
		double ny = 0.0;
		
		for (Glicko2Player p : opponents) {
			ny += Math.pow( g(p.getConvertedRatingDev()), 2) 
					* E(p) * (1 - E(p));
		}
		
		return 1.0 / ny;
	}
	
	public double estimatedImprovement(Glicko2Player opponents[], double results[]) {
	
		double delta = 0;
		
		for (int i = 0; i < opponents.length; i++) {
			delta += g(opponents[i].getConvertedRatingDev())
						* (results[i] - E(opponents[i]));
		}
		
		return delta * estimatedVariance(opponents);
	}
	
	private double g(double phi) {
		return 1.0 / (Math.sqrt(1.0 + (3.0 * Math.pow(phi, 2) / Math.pow(Math.PI, 2) )));
	}
	
	private double E(Glicko2Player opponent) {
		return 1.0 / (1.0 + Math.exp( -1.0 * g(opponent.getConvertedRatingDev()) * (getConvertedRating() - opponent.getConvertedRating())));
	}
	
	private double f(double delta, double phi, double ny, double a, double tau, double x) {
		return ( Math.exp(x) * ( Math.pow(delta, 2) - Math.pow(phi, 2) - ny - Math.exp(x) )
				/ ( 2.0 * Math.pow(Math.pow(phi, 2) + ny + Math.exp(x), 2) ))
				- ( (x - a) / Math.pow(tau, 2) );
	}
	
	public void updateVotality(Glicko2Player opponents[], double results[], double tau) {
		if (opponents.length != results.length) throw new IllegalArgumentException("The amount of results must match the opponent count!");
		
		double epsilon = 0.000001; //convergence tolerance
		double a = Math.log(Math.pow(votality, 2.0));
		double ny = estimatedVariance(opponents);
		double delta = estimatedImprovement(opponents, results);
		double phi = getConvertedRatingDev();
		
		double A = a;
		double B = 0.0;
		double C;
		
		if (delta * delta > phi * phi + ny) {
			B = Math.log(delta*delta - phi*phi - ny);
		}
		else {
			double k = 1;
			B = a - k * tau;
			
			while ( f(delta, phi, ny, a, tau, B) < 0) {
				k++;
				B = a - k * tau;
			}
		}
		

		
		double f_A = f(delta, phi, ny, a, tau, A);
		double f_B = f(delta, phi, ny, a, tau, B);
		double f_C = 0;
		
		while (Math.abs(B - A) > epsilon) {
			C = A + ( ((A - B) * f_A) / (f_B - f_A) );
			f_C = f(delta, phi, ny, a, tau, C);
			
			if (f_C * f_B < 0) {
				A = B; 
				f_A = f_B;
			}
			else {
				f_A = f_A / 2.0;
			}
			
			B = C;
			f_B = f_C;
	
		}
		
		votality = Math.exp(A / 2.0);
	}
	
	public void calcRating(Glicko2Player  opponents[], double results[]) {
		phi_star = Math.sqrt(getConvertedRatingDev() * getConvertedRatingDev() + votality * votality);
		
		rating_deviation_new = 1.0 / Math.sqrt((1.0 / Math.pow(phi_star, 2) ) + (1.0 / estimatedVariance(opponents)));
		rating_new = 0;
		
		for(int i = 0; i < opponents.length; i++) {
			rating_new += g(opponents[i].getConvertedRatingDev()) * (results[i] - E(opponents[i]));
		}
		
		rating_new = getConvertedRating() + Math.pow(rating_deviation_new, 2) * rating_new;
	}
	
	public void updateRating() {
		rating = 173.7178 * rating_new + 1500;
		rating_deviation = 173.7178 * rating_deviation_new;
	}
	
	
	
	
}