package extras.animalsense.evaluate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import extras.lifecycle.common.Variable;

/**
 * @author Mihail Mihaylov
 */
@XmlRootElement(namespace = "http://www.algoanim.info/Animal2/")
public class Exercise {

	/**
	 */
	private String title;
	/**
	 */
	private String subTitle;
	/**
	 */
	private String description;
	/**
	 */
	private List<Question> questionsList;
	/**
	 * Identifies the specific version of the generator e.g. its language,
	 * specific implementation, etc.
	 */
	private String chainPath;
	/**
	 * 
	 */
	private String generatorName;

	/**
	 * Some initial variables.
	 */
	private List<Variable> initialVariables;

	
	/**
	 * 
	 */
	public Exercise() {
		super();
	}

	public void addInitialVariable(Variable variable) {
		if (initialVariables == null)
			initialVariables = new LinkedList<Variable>();

		initialVariables.add(variable);
	}

	public void addQuestion(Question question) {
		if (questionsList == null)
			questionsList = new LinkedList<Question>();

		questionsList.add(question);
	}

	/**
	 * @return the chainPath
	 */
	public String getChainPath() {
		return chainPath;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the generatorName
	 */
	public String getGeneratorName() {
		return generatorName;
	}

	/**
	 * @return the variables
	 */
	public List<Variable> getInitialVariables() {
		if (initialVariables == null)
			initialVariables = new ArrayList<Variable>();
		return initialVariables;
	}

	/**
	 * @return the questionsList
	 */
	public List<Question> getQuestionsList() {
		if (questionsList == null)
			questionsList = new ArrayList<Question>();

		return questionsList;
	}

	/**
	 * @return the subTitle
	 */
	public String getSubTitle() {
		return subTitle;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param chainPath
	 *            the chainPath to set
	 */
	public void setChainPath(String chainPath) {
		this.chainPath = chainPath;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param generatorName
	 *            the generatorName to set
	 */
	public void setGeneratorName(String generatorName) {
		this.generatorName = generatorName;
	}

	/**
	 * @param initialVariables
	 *            the variables to set
	 */
	public void setInitialVariables(List<Variable> initialVariables) {
		this.initialVariables = initialVariables;
	}

	/**
	 * @param questionsList
	 *            the questionsList to set
	 */
	public void setQuestionsList(List<Question> questionsList) {
		this.questionsList = questionsList;
	}

	/**
	 * @param subTitle
	 *            the subTitle to set
	 */
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void generateDefault() {
		// Define the problem definition
		this.setTitle("Exercise");
		this.setSubTitle("");

		String descr = "Preview";
		this.setDescription(descr);
		this.setInitialVariables(new ArrayList<Variable>(0));
		this.questionsList = null;
	}

}
