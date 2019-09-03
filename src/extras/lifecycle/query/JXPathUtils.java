package extras.lifecycle.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.JXPathFunctionNotFoundException;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.JXPathInvalidSyntaxException;
import org.apache.commons.jxpath.JXPathNotFoundException;
import org.apache.commons.jxpath.JXPathTypeConversionException;

import extras.lifecycle.query.JXPathUtils;
import extras.lifecycle.query.Knowledge;
import extras.lifecycle.query.KnowledgeVariablesWrapper;
import extras.lifecycle.query.QueryException;

public final class JXPathUtils {

	/**
	 * 
	 */
	private JXPathUtils() {
		super();
	}
	
	public static Object JXPathQuery(String expression, Object contextObject, Knowledge knowledge)
			throws QueryException {
				
				JXPathContext context = JXPathContext.newContext(contextObject);
				context.setVariables(new KnowledgeVariablesWrapper(knowledge));
				context.setLenient(true);
				
				Object result = null;
				try {
					// We run the JXPath query
					// result = context.getValue(expression);
					Iterator<?> results = context.iterate(expression);
					List<Object> resultsArr = new LinkedList<Object>();
					while (results.hasNext()) {
						Object tempResult = results.next();
						resultsArr.add(tempResult);
					}
			
					if (resultsArr.size() == 0)
						result = null;
					else if (resultsArr.size() == 1)
						result = resultsArr.get(0);
					else 
						result = new ArrayList<Object>(resultsArr);//new SimpleResultBean(resultsArr);
					
				} catch (JXPathFunctionNotFoundException e) {
					throw new QueryException("Function not found! " + e.getMessage());
				} catch (JXPathInvalidAccessException iae) {
					throw new QueryException("Invalid access!" + iae.getMessage());
				} catch (JXPathInvalidSyntaxException ise) {
					throw new QueryException("Invalid syntax!" + ise.getMessage());
				} catch (JXPathNotFoundException nfe) {
					throw new QueryException("Not found!" + nfe.getMessage());
				} catch (JXPathTypeConversionException tce) {
					throw new QueryException("Type conversation error!" + tce.getMessage());
				} catch (JXPathException pe) {
					// For any other JXPath Exception
					throw new QueryException("Error!" + pe.getMessage());
				} catch (Exception other) {
					// For any other exception
					throw new QueryException("Error!" + other.getMessage());
				}
				
				return result;
			}
	

	public static boolean weakEqual(Object expected, Object actual) {
		if (expected == null)
			return false;

		if (expected.equals(actual))
			return true;

		// Check if 2 Variable are from different types but mean the same.
		Object exptectedValue = normalizeObject(expected);
		Object actualValue = normalizeObject(actual);

		return exptectedValue.equals(actualValue);
	}

	public static Object normalizeObject(Object object) {
		if (object == null)
			return null;
		Object objValue = object;
		if (object instanceof Double)
			objValue = ((Double) object).intValue();

		return objValue.toString().trim();
	}

	public static boolean checkCondition(String expression, Knowledge knowledge) {
		boolean result = false;
		String jxPathExpression = "boolean(" +  expression + ")";
		Object resultObj;
		try {
			resultObj = JXPathUtils.JXPathQuery(jxPathExpression, knowledge, knowledge);
		} catch (QueryException e) {
		//	e.printStackTrace();
			resultObj = null;
		}
		
		if (resultObj instanceof Boolean) {
			result = ((Boolean) resultObj).booleanValue();
		}
		return result;
	}

}
