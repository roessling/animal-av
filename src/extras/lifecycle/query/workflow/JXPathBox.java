package extras.lifecycle.query.workflow;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathNotFoundException;

import extras.lifecycle.query.workflow.AbstractBox;
import extras.lifecycle.query.workflow.Box;

import extras.lifecycle.query.Knowledge;

/**
 * This box runs a JXPath expression on the current knowledge object.
 * 
 * @author Mihail Mihaylov
 *
 */
@XmlRootElement(name = "JXPathBox")
public class JXPathBox extends AbstractBox {
	
	/**
	 * 
	 */
//	private static final long serialVersionUID = 8504224012180210559L;

	/**
	 * JXPath expression, which will be executed on the knowledge.
	 */
	private String expression;
	
	/**
	 * The name of the variable, which will get the result. 
	 */
	private String resultVariable;
	
	/**
	 * 
	 */
	public JXPathBox() {
		super();
	}

	/**
	 * Constructor for a JXPath.
	 * @param resultVariable name of the variable, under which the result will be saved
	 * @param expression JXPath which will be executed.
	 */
	public JXPathBox(String resultVariable, String expression) {
		super();
		this.resultVariable = resultVariable;
		this.expression = expression;
	}

	@Override
	public Box evaluate(Knowledge knowledge) {
		JXPathContext context = JXPathContext.newContext(knowledge);
		Object result;
		try {
			result = context.getValue(expression);
		} catch (JXPathNotFoundException e) {
			result = null;
		}
		
		knowledge.getVariables().set(resultVariable, result);
		
		return getNext();
	}

	/**
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * @param expression the expression to set
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * @return the resultVariable
	 */
	public String getResultVariable() {
		return resultVariable;
	}

	/**
	 * @param resultVariable the resultVariable to set
	 */
	public void setResultVariable(String resultVariable) {
		this.resultVariable = resultVariable;
	}

}
