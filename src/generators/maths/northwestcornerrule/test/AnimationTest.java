package generators.maths.northwestcornerrule.test;

import generators.maths.northwestcornerrule.Generator;
import generators.maths.northwestcornerrule.io.AsuFileWriter;
import generators.maths.northwestcornerrule.io.Reader;
import generators.maths.northwestcornerrule.io.StaticDataReader;
import generators.maths.northwestcornerrule.io.Writer;

import org.junit.Test;


public class AnimationTest {

	@Test
	public void testAlgorithm(){
		
		int[] exampleSuppliers = {25,25,40};
		int[] exampleDemanders = {10, 30, 15,35};
		
		Reader myReader = new StaticDataReader(exampleSuppliers, exampleDemanders);
		Writer myWriter = new AsuFileWriter("myAnimation");
		
		Generator myGenerator = new Generator(myReader, myWriter);
		myGenerator.generate();
	}
}
