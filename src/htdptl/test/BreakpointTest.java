package htdptl.test;

import htdptl.facade.Facade;
import htdptl.filter.BreakpointFilter;
import htdptl.util.Util;

import java.io.File;

import junit.framework.TestCase;

public class BreakpointTest extends TestCase {

	static File out = new File(OutFile.path+"filter/");
	
	public void test1() throws Exception {
		Facade facade = new Facade();
				
		facade.input("(map + (list 1 2 3) (list 2 5 8) (list 13 21 3))");
	
		facade.addFilter(new BreakpointFilter("map", 1));
		
		facade.animate();
		File outFile = new File(out + "/maptest.asu");
		Util.writeFile(outFile,facade.getScriptCode());		
	}
	
}
