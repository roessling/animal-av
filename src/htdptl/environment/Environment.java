package htdptl.environment;

import htdptl.ast.AST;

import java.util.HashMap;


/**
 * This class stores variables, procedures and structures as well as their definitions 
 *
 */
public class Environment {

	private HashMap<String, AST> variables = new HashMap<String, AST>();;
	private HashMap<String, AST> procedures = new HashMap<String, AST>();
	private HashMap<String, AST> structures = new HashMap<String, AST>();
	
	public void addVariable(String variable, AST definition) {
		variables.put(variable, definition);
	}
	
	public void addProcedure(String procedure, AST definition) {
		procedures.put(procedure, definition);	
	}
	
	public void addStructure(String structure, AST definition) {
		structures.put(structure, definition);	
	}

	public boolean isVariable(String string) {
		return variables.containsKey(string);
	}

	public boolean isProcedure(String operator) {
		return procedures.containsKey(operator);
	}

	
	
	/**
	 * returns true if the given symbol is a selector on a defined structure.
	 * square-length -> true square? -> false make-square -> false
	 * 
	 * @param symbol
	 * @return true if symbol is a field selector
	 */
	public boolean isStructFieldSelector(String symbol) {
		int i = symbol.indexOf('-');
		if (i == -1) {
			return false;
		} else {
			return structures.containsKey(symbol.substring(0, i));
		}
	}
	
	private String structName(String symbol) {
		if (symbol.startsWith("make-")) {
			return symbol.substring(5);
		}
		if (symbol.indexOf('-') > 0) {
			return symbol.substring(0, symbol.indexOf('-'));
		} else if (symbol.indexOf('?') > 0) {
			return symbol.substring(0, symbol.indexOf('?'));
		} else {
			return symbol;
		}
	}


	public boolean isStructProcedure(String symbol) {
//		String theSymbol = structName(symbol);
		return structures.containsKey(structName(symbol));
	}

	public AST getDefinition(String symbol) {
		if (procedures.containsKey(symbol)) {
			return procedures.get(symbol);
		} else if (variables.containsKey(symbol)) {
			return variables.get(symbol);
		} else if (isStructProcedure(symbol)) {
//			symbol = structName(symbol);
			return structures.get(structName(symbol));
		}
		return null;
	}
	
	

	
}
