package generators.graphics;

import java.awt.Color;

public class ColorConverter {
	
	/**
	 * Converts an RGB color value to HSL. Conversion formula
	 * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
	 * Assumes r, g, and b are contained in the set [0, 255] and
	 * returns h, s, and l in the set [0, 1].
	 *
	 * @param  	RGBColor (Object containing R, G, B integer values)
	 * @return  HSLColor (Object containing H, S, L double values)
	 */
	public HSLColor rgbToHsl(RGBColor color)
	{
		double r = ((double)color.r) / 255.;
		double g = ((double)color.g) / 255.;
		double b = ((double)color.b) / 255.;

		
		// FIND MIN / MAX
	    double max = Math.max(r, Math.max(g, b)), 
	    		min = Math.min(r, Math.min(g, b));
	    
	    double h = 0, s = 0;
	    // CALCULATE L
	    double l = (max + min) / 2.;

	    // SET H
	    // R = G = B -> h has no relevance, so we define it as 0.
	    if (max == min){
	    	h = 0;
	    } else
	    // MAX = R
	     if (r == max) {
	    	h = 60 * (0 + ((g - b)/(max - min))); // h is now in [-60, 60]
	    } else
	    // MAX = G
	    if (g == max) {
	    	h = 60 * (2 + ((b - r)/(max - min))); // h is now in [60, 180]
	    } else
		// MAX = B
		if (b == max) {
		    h = 60 * (4 + ((r - g)/(max - min))); // h is now in [180, 300]
		}
	    
	    // definition: h is in set of [0, 1] defined
	    if (h < 0) h+=360.0;
	    h = Math.toDegrees(h);
	    
	    // SET S
	    // R = G = B -> s has no relevance, so we define it as 0.
	    if ((max == 0) || (min == 1)){
	    	s = 0;
	    } 
	    else
	    {
	    	s = (max - min) / (1 - Math.abs(max+min-1));
	    }
	    
	    return new HSLColor(h,s,l);
	}
	
	
	/**
	 * Converts an HSL color value to RGB. Conversion formula
	 * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
	 * Assumes h, s, and l are contained in the set [0, 1] and
	 * returns r, g, and b in the set [0, 255].
	 *
	 * @param   HSLColor (Object containing H, S, L double values)
	 * @return  RGBColor (Object containing R, G, B integer values)
	 */
	public RGBColor hslToRgb(HSLColor color)
	{
	    double h = color.h, 
	    		s = color.s, 
	    		l =color.l;
		
	    double r, g, b;

	    if(s == 0)
	    {
	        r = g = b = l; // achromatic = no color
	    }
	      else
	    {
	        double q = l < 0.5 ? l * (1. + s) : l + s - l * s;
	        double p = 2. * l - q;
	        r = hue2rgb(p, q, h + 1./3.);
	        g = hue2rgb(p, q, h);
	        b = hue2rgb(p, q, h - 1./3.);
	    }

	    return new RGBColor((int)(r * 255), (int)(g * 255), (int)(b * 255));
	}

	
	public double hue2rgb(double p, double q, double t)
	{
        if (t < 0.) t += 1.;
        if (t > 1.) t -= 1.;
        if (t < 1./6.) return p + (q - p) * 6. * t;
        if (t < 1./2.) return q;
        if (t < 2./3.) return p + (q - p) * (2./3. - t) * 6.;
        return p;
    }
	
	
	public static class HSLColor
	{
		private double h;
		private double s;
		private double l;
		
		public HSLColor(double h, double s, double l)
		{
			super();
			this.h = h;
			this.s = s;
			this.l = l;
		}
		
		public Color toColor(){
			return new ColorConverter().hslToRgb(this).toColor();
		}
	}
	public static class RGBColor
	{
		private int r;
		private int g;
		private int b;
		
		public RGBColor(int r, int g, int b)
		{
			super();
			this.r = r;
			this.g = g;
			this.b = b;
		}
		
		public Color toColor(){
			return new Color(r,g,b);
		}
	}
	
}
