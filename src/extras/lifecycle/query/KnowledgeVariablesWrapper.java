/**
 * 
 */
package extras.lifecycle.query;

import org.apache.commons.jxpath.Variables;

import extras.lifecycle.query.Knowledge;


/**
 * This class wraps over knowledge object and servers as an adaptor
 * to the <code>org.apache.commons.jxpath.Variables</code> interface.
 * @author Mihail Mihaylov
 *
 */
public class KnowledgeVariablesWrapper implements Variables {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Knowledge knowledge;

	public KnowledgeVariablesWrapper(Knowledge knowledge) {
		super();
		this.knowledge = knowledge;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.jxpath.Variables#declareVariable(java.lang.String, java.lang.Object)
	 */
	@Override
	public void declareVariable(String varName, Object value) {
		knowledge.getVariables().set(varName, value);
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.jxpath.Variables#getVariable(java.lang.String)
	 */
	@Override
	public Object getVariable(String varName) {
		return knowledge.getVariables().get(varName);
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.jxpath.Variables#isDeclaredVariable(java.lang.String)
	 */
	@Override
	public boolean isDeclaredVariable(String varName) {
		return knowledge.getVariables().get(varName) != null;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.jxpath.Variables#undeclareVariable(java.lang.String)
	 */
	@Override
	public void undeclareVariable(String varName) {
		knowledge.getVariables().set(varName, null);

	}

	/**
	 * @return the knowledge
	 */
	public Knowledge getKnowledge() {
		return knowledge;
	}

	/**
	 * @param knowledge the knowledge to set
	 */
	public void setKnowledge(Knowledge knowledge) {
		this.knowledge = knowledge;
	}
	
	

}
