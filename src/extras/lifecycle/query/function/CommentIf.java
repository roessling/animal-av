/**
 * 
 */
package extras.lifecycle.query.function;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import extras.lifecycle.query.function.Comment;

import extras.lifecycle.query.JXPathUtils;
import extras.lifecycle.query.Knowledge;

/**
 * @author Mihail Mihaylov
 * 
 */
@XmlRootElement(name = "CommentIf")
public class CommentIf extends Comment {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * extras.lifecycle.query.function.Comment#calculate(extras.lifecycle.query
	 * .Knowledge)
	 */
	@Override
	public Object calculate(Knowledge knowledge) {
		int argCount = arguments.size();
		if (argCount < 2) {
			return null;
		}
		// If a single argument it is an expression
		Object first = arguments.get(0);
		String expression = first.toString(); // first should be always
		// string
		boolean showComment = JXPathUtils.checkCondition(expression, knowledge);

		if (showComment) {

			// We execute the comment function with the rest of the parameters.
			// Here, we remove the first parameter, because it is not needed by
			// the function comment
			List<Object> commentArguments = new ArrayList<Object>(arguments);
			commentArguments.remove(0);

			String comment = generateComment(commentArguments);
			knowledge.addComment(comment);
		}

		return showComment;
	}

}
