package extras.lifecycle.query.function;

import javax.xml.bind.annotation.XmlRootElement;

import extras.lifecycle.query.JXPathUtils;
import extras.lifecycle.query.Knowledge;
import extras.lifecycle.query.QueryException;
import extras.lifecycle.query.workflow.Function;

/**
 * This function runs a JXPath expression on the current knowledge object.
 * 
 * @author Mihail Mihaylov
 * 
 */
@XmlRootElement(name = "Retrieve")
public class Retrieve extends Function {

	/**
	 * JXPath expression, which will be executed on the knowledge.
	 */
	private String expression;

	/**
	 * Constructor for a JXPath.
	 */
	public Retrieve() {
		super();
	}

	@Override
	public Object calculate(Knowledge knowledge) throws QueryException {
		int argCount = arguments.size();
		if (argCount < 1) {
			knowledge.addError("Compare needs min. 1 argument!");
			return null;
		}
		
		Object first = arguments.get(0);
		expression = first.toString();	// first should be always string
		
		Object contextObject;
		if (argCount > 1) {
			contextObject = arguments.get(1);
		} else {
			contextObject = knowledge;
		}
		Object result = JXPathUtils.JXPathQuery(expression, contextObject, knowledge);
		/*
		JXPathContext context = JXPathContext.newContext(contextObject);
		context.setVariables(new KnowledgeVariablesWrapper(knowledge));
		context.setLenient(true);
		
		Object result = null;
		try {
			// We run the JXPath query
			// result = context.getValue(expression);
			Iterator results = context.iterate(expression);
			List resultsArr = new LinkedList();
			while (results.hasNext()) {
				Object tempResult = results.next();
				resultsArr.add(tempResult);
			}

			if (resultsArr.size() == 0)
				result = null;
			else if (resultsArr.size() == 1)
				result = resultsArr.get(0);
			else 
				result = new ArrayList(resultsArr);//SimpleResultBean(resultsArr);
			
		} catch (JXPathFunctionNotFoundException e) {
			knowledge.addError("Function not found! " + e.getMessage());
		} catch (JXPathInvalidAccessException iae) {
			knowledge.addError("Invalid access!" + iae.getMessage());
		} catch (JXPathInvalidSyntaxException ise) {
			knowledge.addError("Invalid syntax!" + ise.getMessage());
		} catch (JXPathNotFoundException nfe) {
			knowledge.addError("Not found!" + nfe.getMessage());
		} catch (JXPathTypeConversionException tce) {
			knowledge.addError("Type conversation error!" + tce.getMessage());
		} catch (JXPathException pe) {
			// For any other JXPath Exception
			knowledge.addError("Error!" + pe.getMessage());
		} catch (Exception other) {
			// For any other exception
			knowledge.addError("Error!" + other.getMessage());
		}
		 */
		return result;
	}

}
