package htdptl.test;

import htdptl.facade.Facade;
import htdptl.util.Util;

import java.io.File;
import java.util.ArrayList;

import junit.framework.TestCase;

public class CondTest extends TestCase {

  static File out = new File(OutFile.path);
	static String path = "htdptl/examples/GdI1_WS2009/";
	
	public void testname() throws Exception {
		Facade facade = new Facade();
		File file = new File(path + "T1.41.scm");		
		facade.input(Util.getFileContents(file));
		
		ArrayList<Object> expressions = facade.getExpressions();
		
		ArrayList<Object> temp = new ArrayList<Object>();
		temp.add(expressions.get(5));
		facade.setExpressions(temp);
		
		facade.animate();
		File outFile = new File(out + "/T1.41.asu");
		Util.writeFile(outFile,facade.getScriptCode());		
	}
	
}
