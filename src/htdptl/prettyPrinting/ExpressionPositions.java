package htdptl.prettyPrinting;

import htdptl.ast.AST;
import htdptl.visitors.VisitorUtil;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * This class stores all expressions that can be highlighted and the positions (row and column) in the sourcecode primitive. 
 *
 */
public class ExpressionPositions {

	private ArrayList<AST> expressions;
	private HashMap<AST, ArrayList<Point>> expressionPositions = new HashMap<AST, ArrayList<Point>>();
	protected Point redexPosition;
	public static int EQUALS=0;
	public static int IDENTICAL=1;
	private int mode;

	public ExpressionPositions(ArrayList<AST> expressions, int mode) {
	  ArrayList<AST> exp = (expressions != null) ? expressions : new ArrayList<AST>();		
		this.expressions = exp;
		this.mode = mode;
		for (Iterator<AST> iterator = exp.iterator(); iterator
				.hasNext();) {
			AST expression = iterator.next();
			expressionPositions.put(expression, new ArrayList<Point>());
		}
	}

	public ArrayList<String> split(AST ast, int line) {
		ArrayList<String> split = new Splitter(this).split(ast, line);
		return split;
	}

	public boolean containsExpression(AST expression, Point p) {
		boolean contains = containsExpression(expression);
		if (contains) {
			expressionPositions.get(expression).add(p);
		}
		return contains;
	}
	
	public boolean containsExpression(AST expression) {
		
		if (mode==EQUALS) {
			return expressions.contains(expression);
		}	
		else {
			for (Iterator<AST> iterator = expressions.iterator(); iterator.hasNext();) {
				AST e = iterator.next();
				if (e==expression) {
					return true;
				}			
			}
			return false;
		}
		
		
	}

	public int numexpressions() {
		return expressions.size();
	}

	/*
	 * return the closest match of "search" after given line
	 */
	public int getNextMatch(int line, AST selectedField) {
		ArrayList<Point> points = expressionPositions.get(selectedField);
		Point min = null;
		for (Iterator<Point> iterator = points.iterator(); iterator.hasNext();) {
			Point p = iterator.next();
			if (p.x >= line) {
				if (min == null) {
					min = p;
				} else if (p.x < min.x) {
					min = p;
				}
			}
		}
		return (min == null) ? line : min.x;
	}
	
	@Override
	public String toString() {
		String result = "";
		result += "expressions: " + expressions + "\n";
		result += "expressionPositions: " + expressionPositions + "\n";
		return result;
	}

	public ArrayList<Point> getPositions(AST expression) {
		if (containsExpression(expression)) {
			return expressionPositions.get(expression);
		}
		else {
			return new ArrayList<Point>();
		}
	}

	public void setRedexPosition(int row, int col) {
		redexPosition = new Point(row,col);		
	}
	
	public Point getRedexPosition() {
		return redexPosition;
	}

	public ArrayList<Point> getPoints() {
		ArrayList<Point> result = new ArrayList<Point>();
		for (Iterator<AST> iterator = expressionPositions.keySet().iterator(); iterator.hasNext();) {
			AST key = iterator.next();
			result.addAll(getPositions(key));			
		}
		return result;
	}

	public AST getAST(String string) {
		for (Iterator<AST> iterator = expressions.iterator(); iterator.hasNext();) {
			AST ast = iterator.next();
			if (VisitorUtil.toCode(ast).equals(string)) {
				return ast;
			}			
		}
		return null;
	}
	
}
