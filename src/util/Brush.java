package util;

import java.awt.Color;
import java.awt.color.ColorSpace;

@SuppressWarnings("serial")
public class Brush extends Color {
	
	public Brush(Color color) {
		super(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public Brush(int r, int g, int b, int a) {
		super(r, g, b, a);
	}

	public Brush(int r, int g, int b) {
		super(r, g, b);
	}

	public Brush(int rgba, boolean hasalpha) {
		super(rgba, hasalpha);
	}

	public Brush(int rgb) {
		super(rgb);
	}

	public Brush(float r, float g, float b, float a) {
		super(r, g, b, a);
	}

	public Brush(float r, float g, float b) {
		super(r, g, b);
	}

	public Brush(ColorSpace cspace, float[] components, float alpha) {
		super(cspace, components, alpha);
	}
	
	public Brush brighter(double intensity, double skewness) {
		int red = (int) Math.round(getRed() * (1-intensity) + intensity * 255);
		int green = (int) Math.round(getGreen() * (1-intensity) + intensity * 255);
		int blue = (int) Math.round(getBlue() * (1-intensity) + intensity * 255);
		if (getGreen() > getRed() && getGreen() > getBlue()) {
			red = (int) Math.round(red * (1-skewness) + skewness * 255);
		}
		if (getBlue() > getRed() && getBlue() > getGreen()) {
			green = (int) Math.round(green * (1-skewness) + skewness * 255);
		}
		return new Brush(red, green, blue);
	}
	
	public Brush darker(double intensity, double skewness) {
		int red = (int) Math.round(getRed() * (1-intensity));
		int green = (int) Math.round(getGreen() * (1-intensity));
		int blue = (int) Math.round(getBlue() * (1-intensity));
		if (getRed() > getGreen() && getRed() > getBlue()) {
			blue = (int) Math.round(blue * (1-skewness));
			green = (int) Math.round(green * (1-skewness));
		}
		if (getGreen() > getRed() && getGreen() > getBlue()) {
			red = (int) Math.round(red * (1-skewness));
			blue = (int) Math.round(blue * (1-skewness));
		}
		if (getBlue() > getRed() && getBlue() > getGreen()) {
			red = (int) Math.round(red * (1-skewness));
			green = (int) Math.round(green * (1-skewness));
		}
		return new Brush(red, green, blue);
	}

}
