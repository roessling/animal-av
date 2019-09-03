package htdptl.test;

import htdptl.exceptions.NoExpressionsException;
import htdptl.exceptions.TraceTooLargeException;
import htdptl.facade.Facade;
import htdptl.filter.ProcedureFilter;
import htdptl.util.Util;

import java.io.File;
import java.util.ArrayList;


public class FilterExample {

	/**
	 * @param args
	 * @throws TraceTooLargeException 
	 * @throws NoExpressionsException 
	 */
	public static void main(String[] args) throws TraceTooLargeException, NoExpressionsException {

			File file = new File("src/htdptl//examples/GDI 1 (WS 2009-2010)/T2.14-29.scm");		
			String contents = Util.getFileContents(file);
			Facade facade = new Facade();
			
			facade.addFilter(new ProcedureFilter("numer",1));
			facade.addFilter(new ProcedureFilter("denom",1));
			
			facade.input(contents);	
			
			ArrayList<Object> expressions = facade.getExpressions();
			
			
			
			ArrayList<Object> temp = new ArrayList<Object>();
			temp.add(expressions.get(5));
			facade.setExpressions(temp);
			
			facade.animate();
					
		

	}

}
