package extras.animalsense.test;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import extras.animalsense.evaluate.Question;
import extras.animalsense.serialize.Serializer;
import extras.animalsense.serialize.SerializerException;
import extras.animalsense.serialize.SerializerImpl;
import extras.lifecycle.query.workflow.AbstractBox;
import extras.lifecycle.script.parser.WorkflowGenerator;
import extras.lifecycle.script.parser.WorkflowGeneratorException;


public class CompileAndSerialize {

	
	protected static AbstractBox compile(String script) {
		
		System.out.println("Start compiling...");
		WorkflowGenerator workflowGenerator = new WorkflowGenerator();
		AbstractBox workflow = null;
		try {
			workflow = workflowGenerator.generate(script);
			System.out.println(workflow);
		} catch (WorkflowGeneratorException e) {
			e.printStackTrace();
		}
		System.out.println("Over");
		
		return workflow;	
	}
	
	private static String readFileAsString(String filePath) throws java.io.IOException{
	    byte[] buffer = new byte[(int) new File(filePath).length()];
	    BufferedInputStream f = new BufferedInputStream(new FileInputStream(filePath));
	    f.read(buffer);
	    f.close();
	    return new String(buffer);
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws SerializerException 
	 */
	public static void main(String[] args) throws IOException, SerializerException {
		String fileName = "questionscriptsample.txt";
		String script = readFileAsString(fileName);
		AbstractBox workflow = compile(script);
		
		Question sampleQ = new Question("QText", script, workflow);
		Serializer serializer = new SerializerImpl();
		
		serializer.serializeQuestion(sampleQ, "sampleQ.txt");
		
		Question loadedBox = serializer.deserializeQuestion("sampleQ.txt");
		serializer.serializeQuestion(loadedBox, "sampleQReloaded.txt");
		
		System.out.println("Ready...");
	}

}
