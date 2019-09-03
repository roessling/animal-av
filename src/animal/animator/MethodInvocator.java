package animal.animator;

import java.lang.reflect.Method;
import java.util.Hashtable;

import translator.AnimalTranslator;
import animal.misc.AnimatedAlgorithm;
import animal.misc.MessageDisplay;
import animalscript.core.AnimalScriptParser;

/**
 * Animator that can change some of a GraphicObject's properties.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.1 2000-06-30
 */
public class MethodInvocator extends TimedAnimator {

	// =================================================================
	//                                CONSTANTS
	// =================================================================

	/**
	 * The name of this animator
	 */
	public static final String TYPE_LABEL = "MethodInvocator";

	/**
	 * This constant contains the UID for serialization, <em>do not edit</em>!
	 */
//	private static final long serialVersionUID = 5542833619518931027L;

	// =================================================================
	//                               ATTRIBUTES
	// =================================================================

	private boolean isConstructorInvocation = false;

	private int tag = 0;

	private String targetClassName = null;

	private String targetMethod = null;

	private static Hashtable<String, Object> externalObjectIDs =
		new Hashtable<String, Object>(503);

	// =================================================================
	//                               CONSTRUCTORS
	// =================================================================

	/**
	 * Public(empty) constructor required for serialization.
	 */
	public MethodInvocator() {
		// do nothing; only used for serialization
	} // for serialization

	/**
	 * Construct a new MethodInvocator from the parameters given
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNum
	 *            the number of the object used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this animator
	 * @param method
	 *            the method to be used, e.g. <em>fillColor</em>
	 * @param classOrMethodName
	 *            the name of the class or method to invoke
	 * @param isConstructor
	 *            if true, invokes the constructor rather than a method
	 * @param parameterTag
	 *            the parameter to change
	 */
	public MethodInvocator(int step, int objectNum, int totalTimeOrTicks,
			String method, String classOrMethodName, boolean isConstructor,
			int parameterTag) {
		this(step, new int[] { objectNum }, totalTimeOrTicks, method,
				classOrMethodName, isConstructor, parameterTag);
	}

	/**
	 * Construct a new MethodInvocator from the parameters given
	 * 
	 * @param step
	 *            the step in which the animator is placed
	 * @param objectNums
	 *            the numbers of the objects used in the animator
	 * @param totalTimeOrTicks
	 *            the duration for this animator
	 * @param method
	 *            the method to be used, e.g. <em>fillColor</em>
	 * @param classOrMethodName
	 *            the name of the class or method to invoke
	 * @param isConstructor
	 *            if true, invokes the constructor rather
	 * @param parameterTag
	 *            the parameter to change
	 */
	public MethodInvocator(int step, int[] objectNums, int totalTimeOrTicks,
			String method, String classOrMethodName, boolean isConstructor,
			int parameterTag) {
		super(step, objectNums, totalTimeOrTicks, method);
		if (isConstructor)
			setClassName(classOrMethodName);
		else
			setMethodName(classOrMethodName);
		setIsConstructorInvocation(isConstructor);
		tag = parameterTag;
	}

	// =================================================================
	//                           ANIMATION EXECUTORS
	// =================================================================

	/**
	 * Changes the state of the GraphicObject to its final state after
	 * completely executing the Animator. Must be overwritten and called by
	 * subclasses. The object must have been initialized before but some actions
	 * may have been done meanwhile. Should be called at the end of <code>
	 * action</code>
	 * 
	 * @see #action(long, double)
	 */
	@SuppressWarnings("unchecked")
	public void execute() {
		if (isConstructorInvocation) {
			String accessKey = String.valueOf(getObjectNums()[0]);
			if (externalObjectIDs.get(accessKey) == null) {
				try {
					Class<Object> c = (Class<Object>)Class.forName(targetClassName);
					Object invocationTarget = c.newInstance();
					externalObjectIDs.put(accessKey, invocationTarget);
				} catch (Exception e) {
					MessageDisplay.errorMsg(AnimalTranslator.translateMessage("mthdInvFail",
							new String[] { e.getClass().getName(), e.getMessage()}),
							MessageDisplay.RUN_ERROR);
				}
			} else if (getMethod().equalsIgnoreCase("getAnimationCode")) {
			Object invocationTarget = externalObjectIDs.get(String
					.valueOf(getObjectNums()[0]));
			if (invocationTarget instanceof AnimatedAlgorithm) {
				String animCode = ((AnimatedAlgorithm) invocationTarget)
						.getAnimationCode(tag);
				AnimalScriptParser asp = new AnimalScriptParser();
				asp.programImport(animCode, false);
			}
		} else {
			try {
				Object invocationTarget = externalObjectIDs.get(String
						.valueOf(getObjectNums()[0]));
				Method method = invocationTarget.getClass().getDeclaredMethod(
						targetMethod, (Class[])null);
				method.invoke(invocationTarget, (Object[])null);
			} catch (Exception e) {
				MessageDisplay.errorMsg(AnimalTranslator.translateMessage("mthdInvFail",
						new String[] { e.getClass().getName(), e.getMessage()}),
						MessageDisplay.RUN_ERROR);
			}
		}
		}
		super.execute();
	}

	// =================================================================
	//                                 ATTRIBUTE GET/SET
	// =================================================================

	/**
	 * Returns the name of this animator, used for saving.
	 * 
	 * @return the name of the animator.
	 */
	public String getAnimatorName() {
		return "MethodInvocator";
	}

	public static boolean insertObjectID(String objectID, Object targetObject) {
		if (externalObjectIDs.containsKey(objectID))
			return false;
		externalObjectIDs.put(objectID, targetObject);
		return true;
	}

	public static Object getObject(String objectID) {
		return externalObjectIDs.get(objectID);
	}

	/***************************************************************************
	 * retrieve the file version for this animator It must be incremented
	 * whenever the save format is changed.
	 * 
	 * Versions include:
	 * 
	 * <ol>
	 * <li>original release</li>
	 * </ol>
	 * 
	 * @return the file version for the animator, needed for import/export
	 */
	public int getFileVersion() {
		return 1;
	}

	/**
	 * Returns the property at a certain time. getProperty <em>must</em>
	 * return a property of the "normal" type (i.e. Move must always return a
	 * Point), even if the object is not completely initialized(then return a
	 * dummy!), as TimedAnimatorEditor relies on receiving a property for
	 * querying the possible methods.
	 * 
	 * @param factor
	 *            a value between 0 and 1, indicating how far this animator has
	 *            got(0: start, 1: end)
	 * 
	 * @return the property at this time
	 */
	public Object getProperty( double factor) {
		return new Integer(42); // MUST change this!
	}

	public String getClassName() {
		return targetClassName;
	}

	public String getMethod() {
		return (isConstructorInvocation) ? targetClassName : targetMethod;
	}

	public boolean isConstructorInvocation() {
		return isConstructorInvocation;
	}

	public void setIsConstructorInvocation(boolean isConstructor) {
		isConstructorInvocation = isConstructor;
	}

	public void setClassName(String className) {
		targetClassName = className;
	}

	public void setMethodName(String methodName) {
		targetMethod = methodName;
	}

	public String getType() {
		return TYPE_LABEL;
	}

	/**
	 * Returns the keywords of Animal's ASCII format this animator handles.
	 * 
	 * @return a String array of the keywords handled by this animator.
	 */
	public String[] handledKeywords() {
		return new String[] { "MethodInvocation" };
	}

	// =================================================================
	//                                   I/O
	// =================================================================


	/**
	 * Return a String representation of this object.
	 * 
	 * @return the String representation of this object.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(200);
		sb.append("invoke ");
		sb.append((isConstructorInvocation() ? " constructor " : "method "));
		sb.append(getMethod()).append(" on ").append(super.toString());
		return sb.toString();
	}
}
