package htdptl.test;

import htdptl.ast.AST;
import htdptl.ast.Expression;
import htdptl.ast.Leaf;
import htdptl.prettyPrinting.ExpressionPositions;
import htdptl.prettyPrinting.Splitter;

import java.awt.Point;
import java.util.ArrayList;

import jsint.Symbol;
import junit.framework.TestCase;

public class ASTSplitterTest extends TestCase {

	
	public ExpressionPositions buildExpressionPositions() {
		ArrayList<AST> expressions = new ArrayList<AST>();
		expressions.add(new Leaf(Symbol.intern("x")));
		ExpressionPositions e = new ExpressionPositions(expressions, ExpressionPositions.EQUALS);
		e.getPositions(new Leaf(Symbol.intern("x"))).add(new Point(0,0));
		return e;
	}
	
	public static Expression ast1() {
		Expression result = new Expression();
		result.addChild(new Leaf(Symbol.intern("cond")));
		Expression cond = new Expression();
		Expression test = new Expression();
		test.addChild(new Leaf(Symbol.intern(">")));
		test.addChild(new Leaf(Symbol.intern("x")));
		test.addChild(new Leaf(0));
		cond.addChild(test);
		cond.addChild(new Leaf(0));
		result.addChild(cond);
		return result;
	}
	
	public static AST ast2() {
		Expression result = new Expression();
		result.addChild(new Leaf(Symbol.intern("reply")));
		result.addChild(new Leaf(Symbol.intern("s")));
		return result;
	}
	
	public void test01() throws Exception {		
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("[(>");
		expected.add("x");
		expected.add("0) 0]");
		
		Splitter s = new Splitter(buildExpressionPositions());
		
		ArrayList<String> result = s.split(ast1().getChild(0), 0);
		System.out.println(result);
		assertTrue(result.equals(expected));
	}
	
	public void test02() throws Exception {		
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("(cond [(>");
		expected.add("x");
		expected.add("0) 0])");
		
		Splitter s = new Splitter(buildExpressionPositions());
		
		ArrayList<String> result = s.split(ast1(), 0);
		assertTrue(result.equals(expected));
	}
	
	public void test03() throws Exception {		
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("(reply s)");
		
		ArrayList<AST> expressions = new ArrayList<AST>();		
		ExpressionPositions e = new ExpressionPositions(expressions, ExpressionPositions.EQUALS);
		
		Splitter s = new Splitter(e);
		
		ArrayList<String> result = s.split(ast2(), 0);
		assertTrue(result.equals(expected));
	}
	
	public void test04() throws Exception {		
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("(");
		expected.add("reply");		
		expected.add("s");
		expected.add(")");
		
		ArrayList<AST> expressions = new ArrayList<AST>();
		expressions.add(new Leaf(Symbol.intern("reply")));
		expressions.add(new Leaf(Symbol.intern("s")));
		ExpressionPositions e = new ExpressionPositions(expressions, ExpressionPositions.EQUALS);
		
		Splitter s = new Splitter(e);
		
		ArrayList<String> result = s.split(ast2(), 0);
		System.out.println(result);
		assertTrue(result.equals(expected));
	}
	
	
	
}
