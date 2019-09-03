package htdptl.visitors;

import htdptl.ast.AST;
import htdptl.ast.CodeGenerationVisitor;

import java.util.ArrayList;
import java.util.Iterator;


public class VisitorUtil {

	public static String toCode(AST ast) {
		CodeGenerationVisitor cgv = new CodeGenerationVisitor();
		if (ast!=null) {
			ast.accept(cgv);
		}
		return cgv.getCode();
	}

	
	public static ArrayList<String> getParameters(AST ast) {
		
		ArrayList<String> result = new ArrayList<String>();
		// special case: (define-struct empty-list ())
		if (ast.getOperator()!=null) {
			
			for (Iterator<AST> iterator = ast.getExpressions().iterator(); iterator.hasNext();) {
				result.add(toCode(iterator.next()));
				
			}
		}
		
		return result;
	}
	
}
