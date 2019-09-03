package generators.maths;
/**
 * Implements Complex Numbers
 * Recommended initialization is through the provided Class functions 
 * @author max
 *
 */
public class ComplexNumber {
	private double absolute;
	private double angle;
	
	public ComplexNumber() {
		this.absolute = 0.0;
		this.angle = 0.0;
	}
	
	public ComplexNumber(double absolute, double angle) {
		this.absolute = absolute;
		this.angle = angle;
	}
	
	public double getAbsolute() {
		return this.absolute;
	}
	
	public double getAngle() {
		return this.angle;
	}
	
	public double getRealPart() {
		return absolute * java.lang.Math.cos(angle);
	}
	
	public double getImaginaryPart() {
		return absolute * java.lang.Math.sin(angle);
	}
	
	/**
	 * Initializes a complex number in Euler form
	 * @param absolute - the absolute value
	 * @param angle - the angle
	 * @return a newl complex number
	 */
	public static ComplexNumber initComplexNumberWithAbsoulteAndAngle(double absolute, double angle) {
		return new ComplexNumber(absolute, angle);
	}
	/**
	 * Initializes a complex number with real and imaginary part.
	 * @param realPart - the real Part of the complex number
	 * @param imaginaryPart - the imaginary part of the complex number
	 * @return returns a new complex number
	 */
	public static ComplexNumber initComplexNumberWithRealAndImaginaryPart(double realPart, double imaginaryPart) {
		
		Double absolute = java.lang.Math.sqrt((realPart * realPart) + (imaginaryPart * imaginaryPart));
		Double angle = java.lang.Math.atan2(imaginaryPart, realPart);
		
		return new ComplexNumber(absolute, angle);
	}
	/**
	 * Adds two complex numbers
	 * @param c1 - the first complex number
	 * @param c2 - the second complex number
	 * @return returns the result of the addition c1 + c2
	 */
	public static ComplexNumber addComplexNumbers(ComplexNumber c1, ComplexNumber c2) {
		
		Double newRealPart = c1.getRealPart() + c2.getRealPart();
		Double newImaginaryPart = c1.getImaginaryPart() + c2.getImaginaryPart();
		
		return ComplexNumber.initComplexNumberWithRealAndImaginaryPart(newRealPart, newImaginaryPart);
	}
	
	/**
	 * Subtracts two complex numbers
	 * @param c1 - the first complex number
	 * @param c2 - the second complex number
	 * @return returns the result of the subtraction c1 - c2
	 */
	public static ComplexNumber subtractComplexNumbers(ComplexNumber c1, ComplexNumber c2) {
		
		Double newRealPart = c1.getRealPart() - c2.getRealPart();
		Double newImaginaryPart = c1.getImaginaryPart() - c2.getImaginaryPart();
		
		return ComplexNumber.initComplexNumberWithRealAndImaginaryPart(newRealPart, newImaginaryPart);
	}
	/**
	 * Multiplies two complex numbers 
	 * @param c1 - the first complex number 
	 * @param c2 - the second complex number
	 * @return returns the product of the two complex numbers
	 */
	public static ComplexNumber multiplyComplexNumbers(ComplexNumber c1, ComplexNumber c2) {
		
		Double newAbsolute = c1.getAbsolute() * c2.getAbsolute();
		Double newAngle = c1.getAngle() + c2.getAngle();
		
		return ComplexNumber.initComplexNumberWithAbsoulteAndAngle(newAbsolute, newAngle);
	}
	
	/**
	 * Transforms a double array to an array of complex numbers with imaginary = 0.0
	 * @param arr - The array to transform 
	 * @return returns the input array converted to complex numbers with
	 */
	public static ComplexNumber[] transFormDoubleArrayToComplexNumberArray(double[] arr) {
		
		ComplexNumber[] complexNumberArr = new ComplexNumber[arr.length];
		
		for (int i = 0; i < arr.length; i++) {
			complexNumberArr[i] = ComplexNumber.initComplexNumberWithRealAndImaginaryPart(arr[i],0.0);
			System.err.println(complexNumberArr[i].getAbsolute() + " " + complexNumberArr[i].getAngle());
		}
		
		return complexNumberArr;
	}
	
}
