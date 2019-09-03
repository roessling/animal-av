package extras.animalsense.evaluate;

import extras.lifecycle.query.workflow.AbstractBox;
import extras.lifecycle.script.parser.WorkflowGenerator;
import extras.lifecycle.script.parser.WorkflowGeneratorException;

public class QuestionCompiler {

	private String script;
	private String output;
	private AbstractBox workflow;

	/**
	 * 
	 */
	public QuestionCompiler() {
		super();
		reset();
	}

	/**
	 * @return the script
	 */
	public String getScript() {
		return script;
	}

	/**
	 * @param script
	 *            the script to set
	 */
	public void setScript(String script) {
		this.script = script;
	}

	private void reset() {
		output = "Not compiled";
		workflow = null;
	}

	public boolean compile() {
		reset();
		WorkflowGenerator workflowGenerator = new WorkflowGenerator();

		try {
			workflow = workflowGenerator.generate(this.script);
		} catch (WorkflowGeneratorException e) {
			output = e.getMessage();
			return false;
		}
		
		boolean ok = workflow != null;
		
		if (ok)
			output = workflow.toString();

		return ok;
	}

	public String getOutput() {
		return output;
	}

	/**
	 * @return the workflow
	 */
	public AbstractBox getWorkflow() {
		return workflow;
	}

}
