package generators.maths.helperSAA;

import generators.maths.helperSAA.ComplexNumberSSA;

public class ComplexNumberSSA {
	private double real;
	private double imaginary;

	public ComplexNumberSSA() {
		real = 0.0;
		imaginary = 0.0;
	}

	public ComplexNumberSSA(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	public void set(ComplexNumberSSA n) {
		this.real = n.real;
		this.imaginary = n.imaginary;
	}

	public static ComplexNumberSSA add(ComplexNumberSSA n1, ComplexNumberSSA n2) {
		return new ComplexNumberSSA(n1.real + n2.real, n1.imaginary + n2.imaginary);
	}

	public static ComplexNumberSSA subtract(ComplexNumberSSA n1, ComplexNumberSSA n2) {
		return new ComplexNumberSSA(n1.real - n2.real, n1.imaginary - n2.imaginary);
	}

	public static ComplexNumberSSA multiply(ComplexNumberSSA n1, ComplexNumberSSA n2) {
		double realTmp = n1.real * n2.real - n1.imaginary * n2.imaginary;
		double imaginaryTmp = n1.real * n2.imaginary + n1.imaginary * n2.real;
		return new ComplexNumberSSA(realTmp, imaginaryTmp);
	}

	public static ComplexNumberSSA pow(ComplexNumberSSA z, int power) {
		ComplexNumberSSA output = new ComplexNumberSSA(z.getReal(), z.getImaginary());
		if (power == 0) {
			return new ComplexNumberSSA(1.0, 0.0);
		}

		for (int i = 1; i < power; i++) {
			double _real = output.real * z.real - output.imaginary * z.imaginary;
			double _imaginary = output.real * z.imaginary + output.imaginary * z.real;
			output = new ComplexNumberSSA(_real, _imaginary);
		}
		return output;
	}

	public ComplexNumberSSA divide(ComplexNumberSSA cn) {
		// a+bi / c+di
		double cAndDSquared = (cn.real * cn.real + cn.imaginary * cn.imaginary);
		double re = (this.real * cn.real + this.imaginary * cn.imaginary) / cAndDSquared;
		double im = (this.imaginary * cn.real - this.real * cn.imaginary) / cAndDSquared;
		return new ComplexNumberSSA(re, im);
	}
	
	public double divideToRe(ComplexNumberSSA cn) {
		// a+bi / c+di
		double cAndDSquared = (cn.real * cn.real + cn.imaginary * cn.imaginary);
		double re = (this.real * cn.real + this.imaginary * cn.imaginary) / cAndDSquared;
		return re;
	}
	
	public static ComplexNumberSSA getEuler(int k) {
		double phi = (2*Math.PI)/k;
		return new ComplexNumberSSA(Math.cos(phi), Math.sin(phi));
	}
	
	
	public double complexToDouble() {
		return this.real;
	}


	public String toString2() {
		double re = round(this.getReal(), 3);
		double im = round(this.getImaginary(), 3);
		
		if(-0.1 < im && im < 0.1 && -0.1 < re && re < 0.1) {
			return Integer.toString(0);
		}
		
		if(im > -0.1 && im < 0.1) {
			return Double.toString(re);
		}
		
		if(re > -0.1 && re < 0.1) {
			return Double.toString(im) + "i";
		}
				
		return re + "+" + im +"i";
	}
	
	static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}
	
	public static ComplexNumberSSA[] calcComplexNumbers(int[] a, ComplexNumberSSA w) {
		ComplexNumberSSA[] c = new ComplexNumberSSA[a.length];

		for (int s = 0; s < a.length; s++) {
			ComplexNumberSSA result = new ComplexNumberSSA(a[0], 0);

			for (int i = 1; i < a.length; i++) {

				result = add(result, multiply(new ComplexNumberSSA(a[i], 0), pow(w, s * i)));
			}
			result.setReal(result.getReal());
			result.setImaginary(result.getImaginary());
			c[s] = result;
		}
		
		return c;
	}
	
	public static ComplexNumberSSA[] fourier(ComplexNumberSSA[] array, ComplexNumberSSA w) {
		ComplexNumberSSA[] result = new ComplexNumberSSA[array.length];
		

		for (int s = 0; s < array.length; s++) {
			ComplexNumberSSA tmp = new ComplexNumberSSA(0.0, 0.0);
			for (int i = 0; i < array.length; i++) {

				tmp = add(tmp, multiply(array[i], pow(w, s * i)));
			}
			result[s] = tmp;
		}
		return result;
	}
	
	public ComplexNumberSSA delete(boolean real) {
		if(real == true) {
			return new ComplexNumberSSA(0,this.getImaginary());
		} else {
			return new ComplexNumberSSA(this.getReal(),0);
		}
	}
	
	public static String complexMultVisual(ComplexNumberSSA cn1, ComplexNumberSSA cn2) {
		String real1 = Double.toString(round(cn1.getReal(),3));
		String real2 = Double.toString(round(cn2.getReal(),3));
		String im1 = Double.toString(round(cn1.getImaginary(),3));
		String im2 = Double.toString(round(cn2.getImaginary(),3));
		
		return "(" + real1 +" * "+ real2 + " - " + im1 + " * " + im2 + ")" + " + " + "(" + real1 +" * " + im2 + " + " + im1 + " * " + real2 + ") * i";   
	}

	public double getReal() {
		return real;
	}

	public void setReal(double real) {
		this.real = real;
	}

	public double getImaginary() {
		return imaginary;
	}

	public void setImaginary(double imaginary) {
		this.imaginary = imaginary;
	}

}
