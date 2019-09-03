package htdptl.test;

import htdptl.exceptions.NoExpressionsException;
import htdptl.exceptions.TraceTooLargeException;
import htdptl.facade.Facade;
import htdptl.util.Util;

import java.io.File;
import java.util.ArrayList;

import junit.framework.TestCase;

public class SelectedExpressions extends TestCase {

  static File out = new File(OutFile.path+"filter/");
	static String path = "htdptl/examples/GDI 1 (WS 2009-2010)/";
	
	public static void test4() throws NoExpressionsException, TraceTooLargeException {
		File file = new File(path + "T1.33.scm");		
		String contents = Util.getFileContents(file);
		Facade facade = new Facade();
		facade.input(contents);
		ArrayList<Object> expressions = facade.getExpressions();
		for (int i=0; i<8; i++) {
			expressions.remove(2);
		}
		for (int i=0; i<8; i++) {
			expressions.remove(4);
		}
		for (int i=0; i<8; i++) {
			expressions.remove(6);
		}
		for (int i=0; i<8; i++) {
			expressions.remove(8);
		}
		facade.setExpressions(expressions);
		facade.animate();
		Util.write(out+"/T1.33.asu",facade.getScriptCode());		
	}
	
	public static void test3_64_71() throws NoExpressionsException, TraceTooLargeException {
		File file = new File(path + "T3.64-71.scm");		
		String contents = Util.getFileContents(file);
				
		Facade facade = new Facade();
		facade.input(contents);
		ArrayList<Object> expressions = facade.getExpressions();		
		for (int i=0; i<6; i++) { expressions.remove(4); }		
		facade.setExpressions(expressions);
		facade.animate();
		Util.write(out+"/T3.64-71-1.asu",facade.getScriptCode());
		
		facade = new Facade();
		facade.input(contents);
		expressions = facade.getExpressions();		
		ArrayList<Object> e = new ArrayList<Object>();
		e.add(expressions.get(4));
		e.add(expressions.get(5));
		e.add(expressions.get(6));
		e.add(expressions.get(7));
		facade.setExpressions(e);
		facade.animate();
		Util.write(out+"/T3.64-71-2.asu",facade.getScriptCode());	
		
		facade = new Facade();
		facade.input(contents);
		expressions = facade.getExpressions();		
		e = new ArrayList<Object>();
		e.add(expressions.get(8));
		e.add(expressions.get(9));
		facade.setExpressions(e);
		facade.animate();
		Util.write(out+"/T3.64-71-3.asu",facade.getScriptCode());	
	}
	
	
	
	
	
}
