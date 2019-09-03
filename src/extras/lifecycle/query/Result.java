package extras.lifecycle.query;

import extras.lifecycle.query.ShortResult;

/**
 * @author Mihail Mihaylov
 */
public class Result {

	/**
	 */
	private ShortResult shortResult;
	/**
	 */
	private String comment;
	
	/**
	 * Data, which is returned as a result of the query.
	 */
	private Object data;

	/**
	 * Create new answer.
	 * 
	 * @param shortResult
	 *            value
	 * @param comment
	 *            some comment as text
	 */
	public Result(ShortResult shortResult, String comment) {
		super();
		this.shortResult = shortResult;
		this.comment = comment;
	}
	
	public Result(ShortResult shortResult) {
		this(shortResult, "");
	}

	/**
	 * @return the shortAnswer
	 */
	public ShortResult getShortAnswer() {
		return shortResult;
	}

	/**
	 * @param shortResult
	 *            the shortAnswer to set
	 */
	public void setShortAnswer(ShortResult shortResult) {
		this.shortResult = shortResult;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Defines an Answer, which should be returned, if the parameters were not
	 * applicable.
	 * 
	 * @return an answer
	 */
	public static Result notApplicable() {
		return new Result(ShortResult.UNKNOWN,
				"This question can not be asked to this record.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return shortResult + "\n" + comment;
	}

	/**
	 * param the comment
	 */
	public void appendComment(String comment) {
		this.comment += comment;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}
	
	

}
