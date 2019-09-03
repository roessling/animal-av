package generators.maths.helpersFastInvsqrt;

import java.awt.Color;

import algoanim.primitives.IntArray;

public class FastInverseHelper {

	public FastInverseHelper() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args){
		System.out.println("Test");
		//Color.GRAY = 128,128,128
		//Color.ORANGE = 255,200,0
		Color c = Color.ORANGE;
		System.out.println("White R" +c.getRed() + " G" + c.getGreen() + " B" + c.getBlue());
		testAlgorithm();
		int[] returnval = convertFloatToBitArray(0.1775F);
		for (int i : returnval) {
			System.out.print(i);
		}
		
		
	}
	
	public static void testAlgorithm() {
		//  f(x) = 1/sqrt(x)
		float x = 32.45F;//0.15625F;  //our x
		float threehalves = 1.5F; //????????
		
		float half_x = x * 0.5F; //the half of x
		//get the bit representation of our x as a floating point IEEE 754
		//and save the bits as a integer number
		int i  = Float.floatToIntBits(x);
		i  = 0x5F3759DF - ( i >> 1); //Shift the bits to the right and substract them from our magic number
		float y  = Float.intBitsToFloat(i); //convert the bits back to a floating point number
		
		float prev_y;
		System.out.println("Calculated y before Newton's Approximation: " + y);
		int counter = 0;
		do
		{
			prev_y = y;
			y  = y * ( threehalves - ( half_x * y * y)); //Do one interration of Newton's Method of Approximation
			System.out.println("New y after " + ++counter + " iterrations: " + y);
		} while(Math.abs(prev_y - y) > 0);
		System.out.println("finish...");
		System.out.println("y=" + FastInvSqrt(32.45F).toString());
	}
		
	public static Float FastInvSqrt(float x) //FINAl
	{
		float threehalves = 1.5F;
		float half_x = x * 0.5F; 
		int magicnumber = 0x5F3759DF;

		int i  = Float.floatToIntBits(x);
		i  = magicnumber - ( i >> 1);
		float y  = Float.intBitsToFloat(i);
		
		float prev_y;
		do {
			prev_y = y;
			y  = y * ( threehalves - ( half_x * y * y));
		} while(Math.abs(prev_y - y) > 0);
		return y;
	}
		
		public static int[] convertFloatToBitArray(float value){
			//1bit Sign, 8Bit Exponent, 23Bit Fraction
			int[] bitArray = new int[32];
			int[] tempArray = new int[32];
			int tempArrayCounter = -1;
			
			int i  = Float.floatToIntBits(value);
			while(i > 0)
			{
				//System.out.println(i);
				tempArrayCounter++;
				int new_i = i / 2;
				if( (new_i * 2) != i) //Rest 1
					tempArray[tempArrayCounter] = 1;
				else
					tempArray[tempArrayCounter] = 0;
				i = new_i;
			}
			
			for(int a = 0; a < bitArray.length; a++)
			{
				bitArray[a] = tempArray[tempArray.length - a - 1];
			}

			return bitArray; //correct binary representation for the float value as IEEE 754
		}
		
		public static void applyFloatToAnimalBitIntArray(IntArray intArray, float value) {
			//Sets the bits to the given IntArray
			int[] result = convertFloatToBitArray(value);
			for(int i = 0; i < 32; i++)
			{
				intArray.put(i, result[i], null, null);
			}
		}
		public static void shiftAnimalBitIntArrayByOtherArray(IntArray source, IntArray destination) {
			
			for(int i = 1; i < 32; i++)
				destination.put(i, source.getData(i-1), null, null);
		}
		
		public static void highlightArray(IntArray intArray) {
			for(int i = 0; i < 32; i++)
		    {
				intArray.highlightCell(i, null, null);
		    	if(i <= 8)
		    		intArray.setHighlightFillColor(i, Color.ORANGE, null, null);
		    	else
		    		intArray.setHighlightFillColor(i, new Color(0,153,255), null, null);
		    		
		    }
			intArray.setHighlightFillColor(0, Color.RED, null, null);
			//intArray.showIndices(false, null, null);
		}
		
		
		public static void unhighlightArray(IntArray intArray) {
			for(int i = 0; i < 32; i++)
		    {
				intArray.unhighlightCell(i, null, null);
		    }
		}

}
