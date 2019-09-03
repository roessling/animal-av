package generators.maths.vogelApprox.test;

import generators.maths.vogelApprox.Generator;
import generators.maths.vogelApprox.io.AsuFileWriter;
import generators.maths.vogelApprox.io.Reader;
import generators.maths.vogelApprox.io.StaticDataReader;
import generators.maths.vogelApprox.io.Writer;

import org.junit.Test;

public class AnimationTest {


	@Test
	public void testAlgorithm(){
		
		int[] exampleSupply = {35,50,40 };
		int[] exampleDemand = { 45,20,30,30 };
		int[][] exampleCost = {{8,6,10,9},{9,12,13,7},{14,9,16,5}};
		
		Reader myReader = new StaticDataReader(exampleSupply, exampleDemand, exampleCost);
		Writer myWriter = new AsuFileWriter("myAnimation");
		
		Generator myGenerator = new Generator(myReader, myWriter);
		myGenerator.generate();
	}
	

}
