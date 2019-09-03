package extras.lifecycle.query.function;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import extras.lifecycle.query.Knowledge;
import extras.lifecycle.query.workflow.Function;

/**
 * This box can produce comments or other information about the current status.
 * 
 * The first argument is a String. This value will be used as comment. If there
 * are more arguments, the first value is formated with
 * <code>String.format</code> and the rest of the arguments are used to produce
 * customizable comments.
 * 
 * @author Mihail Mihaylov
 * 
 */
@XmlRootElement(name = "Comment")
public class Comment extends Function {

	/**
	 * 
	 */
	public Comment() {
		super();
	}

	/**
	 * Creates a <code>CommentBox</code> from given comment and arguments.
	 * 
	 * @param arguments
	 *            addition arguments if we want to customize the comment with
	 *            <code>String.format</code>
	 */
	public Comment(Object... arguments) {
		super(new ArrayList<Object>(arguments.length));
	}

	@Override
	public Object calculate(Knowledge knowledge) {
//		int argCount = arguments.size();
//		// We try to read and change some of the values inside to pass the
//		// format.
//		if (argCount > 0) {
//			List<Object> argValues = new LinkedList<Object>();
//			String comment = arguments.get(0).toString();
//			for (int i = 1; i < argCount; i++) {
//
////				String argVarStr = arguments.get(i).toString();
////				Object value = knowledge.getVariables().get(argVarStr);
//				Object value = arguments.get(i);
//				if (value == null)
//					value = "?";
//				argValues.add(value);
//			}
//
//			String s;
//			try {
//				s = String.format(comment, argValues.toArray());
//			} catch (Exception e) {
//				s = "Error with Comment " + comment + ": "+ e.getMessage();
//			}
//
//			knowledge.addComment(s);
//			return s;
//		}
		String comment = generateComment(arguments);
		knowledge.addComment(comment);
		return null;
	}
	
	protected String generateComment(List<Object> commentArgs) {
		int argCount = commentArgs.size();
		// We try to read and change some of the values inside to pass the
		// format.
		if (argCount > 0) {
			List<Object> argValues = new LinkedList<Object>();
			String comment = commentArgs.get(0).toString();
			for (int i = 1; i < argCount; i++) {

//				String argVarStr = arguments.get(i).toString();
//				Object value = knowledge.getVariables().get(argVarStr);
				Object value = commentArgs.get(i);
				if (value == null)
					value = "?";
				argValues.add(value);
			}

			String s;
			try {
				s = String.format(comment, argValues.toArray());
			} catch (Exception e) {
				s = "Error with Comment " + comment + ": "+ e.getMessage();
			}

			return s;
		}

		return null;
	}

}
