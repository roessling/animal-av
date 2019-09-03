package htdptl.stepper;

import htdptl.ast.AST;
import htdptl.ast.ASTFactory;
import htdptl.ast.Expression;
import htdptl.ast.Leaf;
import htdptl.environment.Environment;
import htdptl.exceptions.StepException;
import htdptl.parser.Parser;
import htdptl.visitors.NormalformVisitor;
import htdptl.visitors.RedexVisitor;
import htdptl.visitors.SubstitutionVisitor;
import htdptl.visitors.VisitorUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import jsint.Evaluator;
import jsint.Pair;
import jsint.Symbol;
import jsint.U;

/**
 * This class is used to stepwise evaluate a HtDP-TL expression. IHandler classes are used to evaluate the redex.  
 *
 */
public class Stepper extends Evaluator implements IStepper {

	private static final long serialVersionUID = 1L;

	
	private AST redex;
	private AST ast;
	private HashMap<String, IHandler> handlers = new HashMap<String, IHandler>();
	private AST evaluatedRedex;
	private ArrayList<AST> substitutedExpressions;
	private AST resolvedBody;
	private ArrayList<IStepObserver> observers = new ArrayList<IStepObserver>();
	private String lastOperator;
	private Environment environment;

	private static final String FOLDR = "(define (foldr f base list) (cond [(empty? list) base] [else (f (first list) (foldr f base (rest list)))]))\n";
	private static final String FOLDL = "(define (foldl f base list) (cond [(empty? list) base] [else (foldl f (f (first list) base) (rest list))]))\n";
	private static final String FILTER = "(define (filter f list) (cond [(empty? list) empty] [(f (first list)) (cons (first list) (filter f (rest list)))] [else (filter f (rest list))]))";

	public Stepper() throws StepException {
		super();

		environment = new Environment();

		defineSymbol(Parser.parse(FOLDL));
		defineSymbol(Parser.parse(FOLDR));
		defineSymbol(Parser.parse(FILTER));

		eval(Parser.parse("(define empty? null?)\n"));
		eval(Parser.parse("(define empty '())"));
		eval(Parser.parse("(define true #t)"));
		eval(Parser.parse("(define false #f)"));
		eval(Parser.parse("(define cons? pair?)"));
		eval(Parser.parse("(define (sqr x) (* x x))"));
		eval(Parser.parse("(define pi 3.14)"));

	}

	public void setExpression(Object expression) throws StepException {
		ast = ASTFactory.build(expression);
		evaluatedRedex = null;
		updateRedex();
	}

	public void updateRedex() throws StepException {
		RedexVisitor redexVisitor = new RedexVisitor(new NormalformVisitor(
				environment));
		ast.accept(redexVisitor);
		redex = redexVisitor.getRedex();
		if (redex instanceof Expression
				&& VisitorUtil.toCode(redex.getOperator()).equals("map")) {
			new MapDefiner(this);
		}
		notifyObservers(StepEvent.REDEX_CHANGED);
	}

	public void step() throws StepException {
		updateRedex();
		evalRedex();
	}

	public void evalRedex() throws StepException {
		String operator = VisitorUtil.toCode(redex.getOperator());
		IHandler handler = handlers.get(operator);
		try {
			if (handler == null) {
				new PrimitiveProcedureHandler().step(this);
			} else {
				handler.step(this);
			}
		} catch (RuntimeException e) {

			System.err.println("Runtime Exception:");
			System.err.println("ast is: " + ast);
			System.err.println("redex is: " + redex);
			System.err.println();
			e.printStackTrace();
			throw new RuntimeException("");

		}
		
	}

	private void notifyObservers(StepEvent stepEvent) {
		for (Iterator<IStepObserver> iterator = observers.iterator(); iterator
				.hasNext();) {
			IStepObserver observer = iterator.next();
			observer.stepPerformed(stepEvent);
		}
	}

	public boolean isDone() {
		NormalformVisitor pv = new NormalformVisitor(environment);
		ast.accept(pv);
		if (pv.isPrimitive()) {
		  notifyObservers(StepEvent.DONE);
		}
		return pv.isPrimitive();
	}

	public AST getAST() {
		return ast;
	}

	public AST getRedex() {
		return redex;
	}

	public void defineSymbol(String symbol, Expression definition) {
		environment.addProcedure(symbol, definition);
	}

	public void defineSymbol(Object definition) throws StepException {
		Object symbol = U.second(definition);
		if (symbol instanceof Pair) {
			String operator = U.first(symbol).toString();
			environment.addProcedure(operator, ASTFactory
					.build(definition));
			handlers.put(operator, new ProcedureHandler());
		} else {
			Object value = U.first(U.rest(U.rest(definition)));
			setExpression(value);
			while (!isDone()) {
				step();
			}
			Expression resolvedDefinition = new Expression();
			resolvedDefinition.addChild(new Leaf(Symbol.intern("define")));
			resolvedDefinition.addChild(new Leaf(symbol));
			resolvedDefinition.addChild(ast.clone());
			environment.addVariable(symbol.toString(), resolvedDefinition);
			handlers.put(symbol.toString(), new VariableHandler());
		}
	}

	public void defineStruct(Object definition) {
		String structName = U.second(definition).toString();

		// get fields
		ArrayList<String> fields = new ArrayList<String>();
		Object args = U.first(U.rest(U.rest(definition)));
		while (args != Pair.EMPTY) {
			fields.add(U.first(args).toString());
			args = U.rest(args);
		}
		StructHandler structHandler = new StructHandler(structName, fields);
		handlers.put(structName + "?", structHandler);
		handlers.put("make-" + structName, structHandler);
		for (Iterator<String> iterator = fields.iterator(); iterator.hasNext();) {
			String field = iterator.next();
			handlers.put(structName + "-" + field, structHandler);
		}
		environment.addStructure(structName, ASTFactory
				.build(definition));

		// add the make procedure as primitive procedure in the interpreter
		primitives.add("make-" + structName);

	}

	public void addHandler(String string, IHandler handler) {
		handlers.put(string, handler);
	}

	public AST getCurrentDefinition() {
		String symbol = VisitorUtil.toCode(redex.getOperator());
		AST result = environment.getDefinition(symbol);
		if (result != null) {
			result = result.clone();
		}
		return result;
	}

	public void replaceRedex(AST replace) {
		if (ast == redex) {
			ast = replace;
			redex = ast;
			if (redex.getParent() != null) {
				redex.getParent().removeChild(redex);
			}
		} else {
			HashMap<AST, AST> substitution = new HashMap<AST, AST>();
			substitution.put(redex, replace);
			SubstitutionVisitor sv = new SubstitutionVisitor(substitution);
			redex.accept(sv);
		}
		evaluatedRedex = replace.clone();
	}

	public AST getEvaluatedRedex() {
		return evaluatedRedex;
	}

	public void addStepObserver(IStepObserver observer) {
		observers.add(observer);
	}

	public void clearObserver() {
		observers.clear();
	}

	public String getLastOperator() {
		return lastOperator;
	}

	Environment getEnvironment() {
		return environment;
	}

	public ArrayList<AST> getSubstitutedExpressions() {
		return substitutedExpressions;
	}

	public void setSubstitutedExpressions(ArrayList<AST> substitutedExpressions) {
		this.substitutedExpressions = substitutedExpressions;
	}

	public NormalformVisitor newPrimitiveVisitor() {
		return new NormalformVisitor(environment);
	}

	public boolean isProcedure(String operator) {
		return environment.isProcedure(operator);
	}

	public boolean isStructFieldSelector(String operator) {
		return environment.isStructFieldSelector(operator);
	}

	public AST getResolvedBody() {
		return resolvedBody;
	}

	public void setResolvedBody(AST resolvedBody) {
		this.resolvedBody = resolvedBody;
	}

}
