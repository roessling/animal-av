package htdptl.test;

import htdptl.exceptions.NoExpressionsException;
import htdptl.exceptions.TraceTooLargeException;
import htdptl.facade.Facade;
import htdptl.filter.BreakpointFilter;
import htdptl.filter.ProcedureFilter;
import htdptl.util.Util;

import java.io.File;
import java.util.ArrayList;

import junit.framework.TestCase;

public class FilterTest extends TestCase {

  static File out = new File(OutFile.path+"filter/");
	static String path = "htdptl/examples/GDI 1 (WS 2009-2010)/";

	public void test5_108() throws Exception {
		Facade facade = new Facade();
		File file = new File(path + "T5.108.scm");		
		facade.input(Util.getFileContents(file));
		
		facade.addFilter(new BreakpointFilter("sum-of-odd-squares", 2));
				
		facade.animate();
		File outFile = new File(out + "/T5.108.asu");
		Util.writeFile(outFile,facade.getScriptCode());		
	}
	public void test5_117() throws Exception {
		Facade facade = new Facade();
		File file = new File(path + "T5.117.scm");		
		facade.input(Util.getFileContents(file));
	
		facade.addFilter(new BreakpointFilter("map", 2));
		facade.addFilter(new BreakpointFilter("foldl", 2));
		facade.addFilter(new BreakpointFilter("filter", 2));
		facade.addFilter(new BreakpointFilter("enumerate-tree", 2));
		
		facade.animate();
		File outFile = new File(out + "/T5.117.asu");
		Util.writeFile(outFile,facade.getScriptCode());		
	}
	public void test5_118() throws Exception {
		Facade facade = new Facade();
		File file = new File(path + "T5.118.scm");		
		facade.input(Util.getFileContents(file));
		
		ArrayList<Object> e = facade.getExpressions();
		e.remove(4);
		facade.setExpressions(e);
		System.out.println(e);
		
		facade.addFilter(new ProcedureFilter("fib", 1));
		facade.addFilter(new BreakpointFilter("enumerate-interval", 2));
		facade.addFilter(new ProcedureFilter("filter", 1));
		facade.addFilter(new ProcedureFilter("map", 1));
		
		
		facade.animate();
		File outFile = new File(out + "/T5.118.asu");
		Util.writeFile(outFile,facade.getScriptCode());		
	}

	
	
	public void test3_36() throws Exception {
		Facade facade = new Facade();
		File file = new File(path + "T3.36.scm");
		facade.input(Util.getFileContents(file));
		BreakpointFilter bf = new BreakpointFilter("cc", 1);
		facade.addFilter(bf);
		
		ArrayList<Object> e = facade.getExpressions();
		ArrayList<Object> temp = new ArrayList<Object>();
		temp.add(e.get(0));
		temp.add(e.get(2));
		temp.add(e.get(3));
		facade.setExpressions(temp);
		facade.animate();

		File outFile = new File(out + "/T3.36.asu");
		Util.writeFile(outFile,facade.getScriptCode());		
	}

	public static void test3_40_45() throws Exception {
		Facade facade = new Facade();
		File file = new File(path + "T3.40-45.scm");
		facade.input(Util.getFileContents(file));
		BreakpointFilter bf = new BreakpointFilter("blue-eyed-ancestor?", 3);
		facade.addFilter(bf);
		facade.animate();
		File outFile = new File(out + "/T3.40-45.asu");
		Util.writeFile(outFile,facade.getScriptCode());		
	}

	public static void test2_14_29() throws NoExpressionsException, TraceTooLargeException {
		File file = new File(path + "T2.14-29.scm");		
		String contents = Util.getFileContents(file);
		Facade facade = new Facade();
		
		// facade.addFilter(new ProcedureFilter("numer",1));
		facade.addFilter(new ProcedureFilter("denom",2));
		
		facade.input(contents);	
		
		ArrayList<Object> expressions = facade.getExpressions();
		
		ArrayList<Object> temp = new ArrayList<Object>();
//		temp.add(expressions.get(0));
//		temp.add(expressions.get(1));
		temp.add(expressions.get(3));
		temp.add(expressions.get(3));
		temp.add(expressions.get(3));
		// temp.add(expressions.get(5));
//		temp.add(expressions.get(7));
//		temp.add(expressions.get(9));
//		temp.add(expressions.get(11));
//		temp.add(expressions.get(12));
//		temp.add(expressions.get(13));
//		temp.add(expressions.get(14));
//		temp.add(expressions.get(15));
		System.out.println(temp);
		facade.setExpressions(temp);
		
		facade.animate();
		Util.write(out+"/T2.14-29.asu",facade.getScriptCode());		
	}
	
	
	public static void test3_32_33() throws NoExpressionsException, TraceTooLargeException {
		File file = new File(path + "T3.32-33.scm");		
		String contents = Util.getFileContents(file);
		Facade facade = new Facade();
		
		facade.addFilter(new ProcedureFilter("insert",1));
				
			facade.input(contents);	

		ArrayList<Object> expressions = facade.getExpressions();
		
		ArrayList<Object> temp = new ArrayList<Object>();
		temp.add(expressions.get(0));
		temp.add(expressions.get(3));
		temp.add(expressions.get(4));		
		facade.setExpressions(temp);
		
		facade.animate();
		Util.write(out+"/T3.32-33.asu",facade.getScriptCode());		
	}
	
	public static void test5_13() throws NoExpressionsException, TraceTooLargeException {
		File file = new File(path + "T5.13.scm");		
		String contents = Util.getFileContents(file);
		Facade facade = new Facade();
		
		facade.addFilter(new ProcedureFilter("squared>?",2));
				
		facade.input(contents);	
		
		facade.animate();
		Util.write(out+"/T5.13.asu",facade.getScriptCode());		
	}
	
	public static void test5_16_18() throws NoExpressionsException, TraceTooLargeException {
		File file = new File(path + "T5.16-18.scm");		
		String contents = Util.getFileContents(file);
		Facade facade = new Facade();
		
		facade.addFilter(new ProcedureFilter("<ir", 2));
		facade.addFilter(new BreakpointFilter("less-than-ir",2));
		facade.addFilter(new BreakpointFilter("filter1",2));
		facade.input(contents);	
		
		facade.animate();
		Util.write(out+"/T5.16-18.asu",facade.getScriptCode());		
	}
	
	
	public static void test5_57_59() throws NoExpressionsException, TraceTooLargeException {
		File file = new File(path + "T5.57-59.scm");		
		String contents = Util.getFileContents(file);
		Facade facade = new Facade();
		facade.input(contents);
		
		facade.addFilter(new ProcedureFilter("insert", 2));
		
		ArrayList<Object> e = facade.getExpressions();
		
		ArrayList<Object> temp = new ArrayList<Object>();
		temp.add(e.get(0));
		temp.add(e.get(1));
		temp.add(e.get(4));
		temp.add(e.get(5));
		temp.add(e.get(6));
		temp.add(e.get(10));
		temp.add(e.get(11));
		facade.setExpressions(temp);
		
		
		
			
		
		facade.animate();
		Util.write(out+"/T5.57-59.asu",facade.getScriptCode());		
	}
	
	public static void test5_9_12() throws NoExpressionsException, TraceTooLargeException {
		File file = new File(path + "T5.9-12.scm");		
		String contents = Util.getFileContents(file);
		Facade facade = new Facade();
		facade.input(contents);
		
		facade.addFilter(new ProcedureFilter("less-than", 2));
		facade.addFilter(new ProcedureFilter("greater-than",2));
		
		
		
		facade.addFilter(new BreakpointFilter("filter1", 2));
		
		ArrayList<Object> e = facade.getExpressions();
		
		ArrayList<Object> temp = new ArrayList<Object>();
		temp.add(e.get(0));
		temp.add(e.get(4));
		temp.add(e.get(8));
		temp.add(e.get(12));
		temp.add(e.get(16));
		temp.add(e.get(20));
		temp.add(e.get(24));
		facade.setExpressions(temp);
		
		
		
		facade.animate();
		Util.write(out+"/T5.9-12.asu",facade.getScriptCode());		
	}
	
	public static void test6_21_22() throws NoExpressionsException, TraceTooLargeException {
		File file = new File(path + "T6.21-22.scm");		
		String contents = Util.getFileContents(file);
		Facade facade = new Facade();
		facade.input(contents);
		
		facade.addFilter(new ProcedureFilter("less-or-equal", 1));
		facade.addFilter(new ProcedureFilter("greater-than", 1));
		facade.addFilter(new ProcedureFilter("filter1", 1));
		
				
		facade.addFilter(new BreakpointFilter("quicksort2", 2));
		
		
		facade.animate();
		Util.write(out+"/T6.21-22.asu",facade.getScriptCode());		
	}
	
	public static void test3_74_82() throws NoExpressionsException, TraceTooLargeException {
		File file = new File(path + "T3.74-82.scm");		
		String contents = Util.getFileContents(file);
		Facade facade = new Facade();
		facade.input(contents);
		
		
		facade.addFilter(new BreakpointFilter("parse", 1));
		facade.addFilter(new BreakpointFilter("calc", 2));
		facade.addFilter(new BreakpointFilter("swap+*", 2));
		
		facade.animate();
		Util.write(out+"/T3.74-82.asu",facade.getScriptCode());		
	}



}
