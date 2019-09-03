package extras.lifecycle.script.parser;

import java.util.LinkedList;
import java.util.List;

import extras.lifecycle.script.parser.AbstractHelper;
import extras.lifecycle.script.parser.Expression;
import extras.lifecycle.script.parser.WorkflowGeneratorException;

import extras.lifecycle.query.workflow.Calculator;
import extras.lifecycle.query.workflow.ConstantValue;
import extras.lifecycle.query.workflow.Function;
import extras.lifecycle.query.workflow.FunctionBox;
import extras.lifecycle.query.workflow.ValueOfIdentifier;

/**
 * 
 * @author Mihail Mihaylov
 *
 */
public class FunctionExpression extends AbstractHelper implements Expression {
	
	private static final String FUNCTION_PACKAGE = "extras.lifecycle.query.function";
	private String name;
	private List<Calculator> arguments;
	
	/**
	 * 
	 */
	public FunctionExpression() {
		this(null);
	}

	/**
	 * @param name
	 */
	public FunctionExpression(String name) {
		super();
		this.name = name;
		arguments = new LinkedList<Calculator>();
	}



	/**
	 * @return the expressionCalculator
	 * @throws WorkflowGeneratorException 
	 */
	public FunctionBox generateFunctionBox() throws WorkflowGeneratorException {
		assert name != null;
		assert !name.isEmpty();
		Function function = loadFunction(name);
		FunctionBox calculator = new FunctionBox(function, arguments);

		return calculator;
	}

	
	/* (non-Javadoc)
	 * @see extras.lifecylcle.script.parser.helper.SimpleExpression#putIdentifier(java.lang.String)
	 */
	public void putIdentifier(String identifier) {
		arguments.add(new ValueOfIdentifier(identifier));
	}
	
	/* (non-Javadoc)
	 * @see extras.lifecylcle.script.parser.helper.SimpleExpression#putStringValue(java.lang.String)
	 */
	public void putStringValue(String stringValue) {
		arguments.add(new ConstantValue(stringValue));
	}
	
	/* (non-Javadoc)
	 * @see extras.lifecylcle.script.parser.helper.SimpleExpression#putDecimalIntegerValue(java.lang.String)
	 */
	public void putDecimalIntegerValue(String decimalIntValue) {
		Integer intValue = Integer.decode(decimalIntValue);
		arguments.add(new ConstantValue(intValue));
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the arguments
	 */
	public List<Calculator> getArguments() {
		return arguments;
	}

	/**
	 * @param arguments the arguments to set
	 */
	public void setArguments(List<Calculator> arguments) {
		this.arguments = arguments;
	}
	private static Function loadFunction(String shortClassName) throws WorkflowGeneratorException  {
		return loadFunction(FUNCTION_PACKAGE, shortClassName);
	}
	
	private static Function loadFunction(String packageName, String simpleClassName) throws WorkflowGeneratorException  {
		Object object = null;
		try {
			String className =  packageName + "." + simpleClassName;
			Class<?> genClass = Class.forName(className);
			object = genClass.newInstance();
			if (object instanceof Function)
				return (Function) object;
		//		else		return null;
		} catch (InstantiationException e) {
			// maybe the Class is abstract
			throw new WorkflowGeneratorException("Unable to instantiate the function " + simpleClassName + "! " + e.getMessage());
		} catch (IllegalAccessException e) {
			// we are not allowed to access the Class
			throw new WorkflowGeneratorException("Unable to access the function " + simpleClassName + "! " + e.getMessage());
		} catch (ClassNotFoundException e) {
			// the Class does not exist
			throw new WorkflowGeneratorException("Function " + simpleClassName + " does not exist!");
		} catch (NoClassDefFoundError e) {
			// wrong name
			throw new WorkflowGeneratorException("Invalid function name " + simpleClassName + "! " + e.getMessage());
		}
		throw new WorkflowGeneratorException("Not a valid function " + simpleClassName + "!");
	}

	@Override
	public Calculator generateExpressionCalculator() {
		return generateFunctionBox();
	}

}
