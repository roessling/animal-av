package animalscript.core;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import translator.ResourceLocator;
import animal.animator.Animator;
import animal.graphics.PTGraphicObject;
import animal.main.Animal;
import animal.main.AnimalConfiguration;
import animal.main.AnimalFrame;
import animal.main.Animation;
import animal.main.AnimationListEntry;
import animal.main.AnimationState;
import animal.main.Link;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;

/**
 * This class provides basic parsing functionality needed by AnimalScript
 * parsers. All extension classes should extend this class.
 * 
 * @author <a href="mailto:roessling@acm.org">Guido R&ouml;&szlig;ling</a>
 * @version 1.4 2000-03-21
 */
public class BasicParser implements AnimalScriptInterface {
	// =================================================================
	// CONSTANTS
	// =================================================================

	// ========================= Messages =========================

	/**
	 * This signals the occurence of an error
	 */
	public static final int ERROR = Integer.MAX_VALUE;

	/**
	 * The point of origin
	 */
	public static final Point origin = new Point(0, 0);

	// ========================= attributes =========================

	/**
	 * Animal object for error messages etc.
	 */
	public static Animal animal = Animal.get();

	/**
	 * The step currently being parsed
	 */
	public static int currentStep = 0;

	public static int currentNodeMode = ParseSupport.NODETYPE_ABSOLUTE;

	protected static boolean employsQuestions = false;

	/**
	 * The keywords each client handles; to be set within the client
	 */
	protected Hashtable<String, Object> handledKeywords = new Hashtable<String, Object>();

	private static HashMap<String, String> keyMap;

	/**
	 * The keywords each client handles; to be set within the client
	 */
	protected XProperties rulesHash = new XProperties();

	/**
	 * The StreamTokenizer used to parse the input
	 */
	public static StreamTokenizer stok = null;

	private static Vector<StreamTokenizer> tokenizers = null;

	/**
	 * The XProperties used to store user-defined locations
	 */
	protected static XProperties locations = null;

	/**
	 * The XProperties used to store object IDs
	 */
	private static XProperties objectIDs = null;

	/**
	 * The Hashtable used to store external objects
	 */
	// public static Hashtable externalObjectIDs = null;
	/**
	 * The XProperties used to store object properties
	 */
	protected static XProperties objectProperties = null;

	/**
	 * The XProperties used to store object types
	 */
	private static XProperties objectTypes = null;

	/**
	 * Toggle whether the next line belongs to the same current step
	 */
	public static boolean sameStep = false;
  public static boolean groupedStepMode = false;

	public static AnimationState animState;

	// public static QuickAnimationStep animState;

	/**
	 * The generated animation
	 */
	public static Animation anim = null;

	public static String chosenLanguage = null;

	/**
	 * The current Link we're in
	 */
	public static Link currentLink = null;

	/**
	 * The properties for resolving keyword - handling matches
	 */
	public static Hashtable<String, String> registeredTypes = new Hashtable<String, String>(
			503);

	/**
	 * The properties for resolving keyword - handling matches
	 */
	public static Hashtable<String, AnimalScriptInterface> currentHandlers = new Hashtable<String, AnimalScriptInterface>(
			503);

	/**
	 * The properties for resolving keyword - handling matches
	 */
	public static Hashtable<String, String> currentlyVisible = new Hashtable<String, String>(
			503);

  /**
   * The properties for resolving keyword - handling matches
   */
  public static Hashtable<String, String> dependentObjects = new Hashtable<String, String>(
      503);

	/**
	 * The properties for resolving keyword - handling matches
	 */
	public static Hashtable<String, String> registeredKeywords = new Hashtable<String, String>(
			503);

	/**
	 * The properties for resolving keyword - handling matches
	 */
	public static Hashtable<String, String> registeredRules = new Hashtable<String, String>(
			503);

	/**
	 * The properties for resolving keyword - handling matches
	 */
	public static XProperties compass = new XProperties();

	static {
		compass.put("nw", 0);
		compass.put("northwest", 0);
		compass.put("n", 1);
		compass.put("north", 1);
		compass.put("ne", 2);
		compass.put("northeast", 2);
		compass.put("w", 3);
		compass.put("west", 3);
		compass.put("c", 4);
		compass.put("center", 4);
		compass.put("middle", 4);
		compass.put("e", 5);
		compass.put("east", 5);
		compass.put("sw", 6);
		compass.put("southwest", 6);
		compass.put("s", 7);
		compass.put("south", 7);
		compass.put("se", 8);
		compass.put("southeast", 8);
		parseRuleFile("elementDefinitions");
		if (tokenizers == null)
			tokenizers = new Vector<StreamTokenizer>(20);
	}

	/**
	 * instantiates the key class dispatcher mapping keyword to definition type
	 */
	public BasicParser() {
		// do nothing
	}

	/**
	 * instantiates the key class dispatcher mapping keyword to definition type
	 */
	public BasicParser(StreamTokenizer aStok, int stepNumber, XProperties props) {
		BasicParser.stok = aStok;
		currentStep = stepNumber;
		objectIDs = props;
	}

	public static boolean debugMode = false;

	public static int errorCounter = 0, type = AnimalScriptInterface.OBJECT,
			lastType = AnimalScriptInterface.OBJECT;

	public String getChosenLanguage() {
		return chosenLanguage;
	}

	public static void printDebug(String s) {
		if (debugMode)
			System.err.println(s);
	}

	public static void printExceptionMessage(String errorType, String mode,
			String keyword, boolean showLineNr) {
		StringBuilder sb = new StringBuilder(250);
		sb.append("# !!!").append(errorType).append("!!! ").append(mode);
		sb.append(" '").append(keyword).append("'");
		if (showLineNr)
			sb.append(" in line ").append(stok.lineno());
		sb.append(" ").append(stok.toString());
		MessageDisplay.errorMsg(sb.toString(), MessageDisplay.RUN_ERROR);
		sb = null;
		errorCounter++;
	}

	@SuppressWarnings("unchecked")
	public static AnimalScriptInterface getHandlerForName(String handlerName) {
		if (handlerName == null)
			return null;
		
		Object o = currentHandlers.get(handlerName);
		if (o != null && o instanceof AnimalScriptInterface)
			return (AnimalScriptInterface) o;

		AnimalScriptInterface handler = null;
		try {
			Class<AnimalScriptInterface> targetClass = (Class<AnimalScriptInterface>) Class
					.forName(handlerName);
			handler = targetClass.newInstance();
			currentHandlers.put(handlerName, handler);
		} catch (ClassNotFoundException classNotFound) {
			MessageDisplay.errorMsg("Class '" + handlerName + "' not found!!!",
					MessageDisplay.RUN_ERROR);
			MessageDisplay.errorMsg(classNotFound.getMessage(),
					MessageDisplay.RUN_ERROR);
		} catch (IllegalAccessException illegalAccess) {
			MessageDisplay.errorMsg("Class '" + handlerName
					+ "' has illegal access!!!", MessageDisplay.RUN_ERROR);
			MessageDisplay.errorMsg(illegalAccess.getMessage(),
					MessageDisplay.RUN_ERROR);
		} catch (InstantiationException instantiationException) {
			MessageDisplay
					.errorMsg("Class '" + handlerName
							+ "' has instantiation exception access!!!",
							MessageDisplay.RUN_ERROR);
			MessageDisplay.errorMsg(instantiationException.getMessage(),
					MessageDisplay.RUN_ERROR);
		}
		return handler;
	}

	public AnimalScriptInterface getHandlerForCommand(String currentCommand) {
		// AnimalScriptInterface handler = null;
		String handlerName = registeredKeywords.get(currentCommand);
		return getHandlerForName(handlerName);
	}

	/**
	 * dump the current state of the animation to System.out The output is
	 * identical to the display in the AnimationOverview
	 * 
	 * @see animal.gui.AnimationOverview
	 */
	public static void dumpState(boolean showAnimators, boolean showObjects) {
		if (showAnimators) {
			AnimationListEntry[] info = anim.getAnimatorList();
			for (int j = 0; j < info.length; j++)
				if (info[j].mode == AnimationListEntry.ANIMATOR)
					System.out.println(info[j].animator.toString());
				else if (info[j].mode == AnimationListEntry.STEP)
					System.out.println(info[j].link.toString());
			if (showObjects)
				System.out.println("*****************");
			info = null;
		}
		if (showObjects)
			for (int i = 0; i < anim.getGraphicObjects().size(); i++) {
				System.out.println(anim.getGraphicObjects().elementAt(i) + " NUM: "
						+ anim.getGraphicObjects().elementAt(i).getNum(false));
			}
	}

	public static void addGraphicObject(PTGraphicObject ptgo, Animation animation) {
		animation.insertGraphicObject(ptgo);
		// FIXME do more here...
		if (animState instanceof QuickAnimationStep) {
			((QuickAnimationStep) animState).addGraphicObject(ptgo);
			// System.err.println("Insert clone of " +ptgo);
		}
	}

	public static void addAnimatorToAnimation(Animator animator,
			Animation animation) {
		// FIXME Do more here - if necessary?
		animation.insertAnimator(animator);
	}

	/**
	 * inserts a new link after the current step and steps to the next step.
	 */
	public static void newLink() {
		currentStep++;
		Link l = new Link(currentStep, currentStep + 1);
		anim.insertLink(l);
		currentLink = l;
		l = null;
		//GR HOTPATCH 20190608
    if (animState == null)
      // animState = new AnimationState(anim);
      animState = new QuickAnimationStep(anim);
		BasicParser.animState.setStep(currentStep, false);
    //GR HOTPATCH 20190608 /end
//		System.err.println("set step to" +currentStep);
	}

	/**
	 * auxiliary method for printing out the properties passed to System.out
	 * 
	 * @param props
	 *          the properties to be listed to System.out
	 */
	public static void printReport(XProperties props) {
		System.out.println("Object IDs:");
		props.list(System.out);

		System.out.println("-- end of object IDs  --\nObject Types:");
		// objectTypes.list(System.out);

		System.out.println("-- end of object Types  --\nObject Properties:");
		if (objectProperties.get("Polyline.lastNode") instanceof Point) {
			Point p = (Point) objectProperties.get("Polyline.lastNode");
			if (p != null)
				objectProperties.put("Polyline.lastNode", "(" + p.x + ", " + p.y + ")");
		}
		objectProperties.list(System.out);
		System.out.println("-- end of object Types  --\nDefined Locations:");
		Enumeration<Object> e = locations.keys();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			Point p = locations.getPointProperty(key);
			System.out.println(key + " -> (" + p.x + ", " + p.y + ")");
		}
		System.out.println("-- end of locations  --");
	}

	// ===================================================================
	// Parsing support routines
	// ===================================================================

	/**
	 * should read in file "types.dat" from current Dir or classpath and place
	 * those in the HashTable. No key is really needed, I guess!
	 */
	public static String getTypeIdentifier(String elementType) {
		String s = elementType.toLowerCase();
		if (registeredTypes.contains(s))
			return registeredTypes.get(s);

		registeredTypes.put(s, s);
		MessageDisplay.message("Inserted type " + s
				+ " which was not registered in file 'types.dat'");
		return s;
	}

	public void clearTables() {
		anim = null;
		animState = null;
		if (locations != null)
			locations.clear();
		if (objectIDs != null)
			objectIDs.clear();
		// if (externalObjectIDs != null)
		// externalObjectIDs.clear();
		if (objectProperties != null)
			objectProperties.clear();
		if (objectTypes != null)
			objectTypes.clear();
		if (currentlyVisible != null)
			currentlyVisible.clear();
		if (dependentObjects != null)
		  dependentObjects.clear();
	}

	public void setStep(int targetStepNr, boolean isSameStep) {
	  System.err.println("setStep called for " + targetStepNr +", same? " +isSameStep);
		// int i;
		// Animator a;
		// allObjectClones.removeAllElements();
		// if (animation == null)
		// return;
		// Vector go = animation.getGraphicObjects();
		// allObjectClones.ensureCapacity(go.size());
		// for (i = 0; i < go.size(); i++) {
		// PTGraphicObject origin = (PTGraphicObject) go.elementAt(i);
		// PTGraphicObject clonedObject = (PTGraphicObject) (origin.clone());
		// clonedObject.clonePropertiesFrom(origin.getProperties(), true);
		// allObjectClones.addElement(clonedObject);
		// }
		//
		// step = 0;
		// allObjectClones.trimToSize();
		// nowObjects.removeAllElements();
	}

	/**
	 * Process the current command and perform appropriate actions. Depending on
	 * the keyword found, usually some objects are inserted into the graphic
	 * objects vector, the animator vector, or both.
	 * 
	 * @param aStok
	 *          the StreamTokenizer used for parsing.
	 * @param currentCommand
	 *          the parsed command
	 * @return the properties retrieved from the parse
	 */
	public XProperties process(StreamTokenizer aStok, String currentCommand)
			throws IOException {
		XProperties props = null;
		// insert a new link...
		if (generateNewStep(currentCommand)) {
//			 System.out.println("generate new link@step "+currentStep +"[line "
//			 +aStok.lineno() +"] for command '" +currentCommand +"'");
			 newLink();
		}

		// TODO optimize here for far better performance!!!
		if (animState == null)
			// animState = new AnimationState(anim);
			animState = new QuickAnimationStep(anim);
		// if (animState != null) {
		//    	
		// }
		// else {
		// *********************
		// animState.highPerformanceSetStep((sameStep) ? currentStep-1 :
		// currentStep, false);
		// *********************
//		   System.out.println("set animation state to step " +((sameStep) ?
//		 currentStep-1 : currentStep)
//		 +", sameStep: " +sameStep +" / cStep: "+currentStep);
		animState.setStep((sameStep) ? currentStep - 1 : currentStep, false);
		// }
		// System.err.println("Now at step " +animState.getStep());
		aStok.pushBack(); // push back value, must be re-read by client

		Object localizedHandlerAccess = handledKeywords.get(currentCommand
				.toLowerCase());
		Method targetMethod = null;
		// long timeTaken = System.currentTimeMillis();
		if (localizedHandlerAccess instanceof String) {
			String usedCommand = (String) localizedHandlerAccess;
			try {
				targetMethod = getClass()
						.getDeclaredMethod(usedCommand, (Class[]) null);
				if (targetMethod != null)
					handledKeywords.put(currentCommand.toLowerCase(), targetMethod);
			} catch (NoSuchMethodException noSuchMethod) {
				printExceptionMessage("NoSuchMethodException", "for handling",
						currentCommand, true);
			} catch (SecurityException security) {
				printExceptionMessage("Security exception", "when handling",
						currentCommand, true);
			}
		} else if (localizedHandlerAccess instanceof Method) {
			targetMethod = (Method) localizedHandlerAccess;
		}
		if (targetMethod != null) {
			try {
				props = (XProperties) targetMethod.invoke(this, (Object[]) null);
			} catch (IllegalAccessException illegalAccess) {
				printExceptionMessage("Illegal access", "when handling",
						currentCommand, true);
			} catch (IllegalArgumentException illegalAccess) {
				printExceptionMessage("Illegal argument", "when handling",
						currentCommand, true);
			} catch (InvocationTargetException invocTarget) {
			  System.err.println("---");
			  invocTarget.printStackTrace(); 
			  invocTarget.fillInStackTrace(); 
			  System.err.println("+++");
			  invocTarget.printStackTrace();
				System.err.println("******" + invocTarget.getTargetException() +"..."
						+ invocTarget.getTargetException().getMessage() + " .... "
						+ invocTarget.getMessage() + invocTarget.toString() + ", cause: "
						+ invocTarget.getCause() + " [line=" + aStok.lineno()
						+ "] when handling " + currentCommand);
				printExceptionMessage(invocTarget.getTargetException().getMessage()
						+ " .... " + invocTarget.getMessage(), "[line=" + aStok.lineno()
						+ "] when handling", currentCommand, true);
				invocTarget.printStackTrace();
			} catch (SecurityException security) {
				printExceptionMessage("Security exception", "when handling",
						currentCommand, true);
			}
		}
		// read rest of line...
		ParseSupport.consumeIncludingEOL(aStok, currentCommand + " EOL");

		if (props == null)
			props = new XProperties();

		return props;
	}

	/**
	 * Determine depending on the command passed if a new step is needed Also keep
	 * in mind that we might be in a grouped step using the {...} form. Usually,
	 * every command not inside such a grouped step is contained in a new step.
	 * However, this is not the case for operations without visible effect -
	 * mostly maintenance or declaration entries.
	 * 
	 * Note that this method will return <code>false</code> only if the command is
	 * inside a grouped step!
	 * 
	 * @param command
	 *          the command used for the decision.
	 * @return true if a new step must be generated
	 */
	public boolean generateNewStep(String command) {
		return !sameStep;
	}

	/**
	 * Retrieve the keywords handled by this parser
	 * 
	 * @return an Enumeration of keywords that this parser handles.
	 */
	public Enumeration<String> getHandledKeywords() {
		return handledKeywords.keys();
	}

	public Hashtable<String, Object> getHandledKeywordsProps() {
		return handledKeywords;
	}

	/**
	 * Retrieve the rules handled by this parser
	 * 
	 * @return an enumeration of rules that this parser handles.
	 */
	public Enumeration<Object> getHandledRules() {
		return rulesHash.keys();
	}

	/**
	 * Retrieve the extension handled by this parser
	 * 
	 * @return a String array of keywords that this parser handles. The default
	 *         from animalscript.core.BasicParser is to return <code>null</code>
	 */
	public String[] getRequiredExtensions() {
		return null;
	}

	/**
	 * Retrieve the rule for a given keyword handled by this parser
	 * 
	 * @return a String containing the requested rule, if exists
	 */
	public String getRuleFor(String key) {
		String localKey = key.toLowerCase();
		StringBuilder sb = new StringBuilder(2000);
		if (keyMap.containsKey(localKey))
			// sb.append("# Rule syntax for '").append(localKey).append("':\n").
			sb.append(keyMap.get(localKey));
		if (rulesHash.getProperty(localKey) != null)
			sb.append(rulesHash.getProperty(localKey));
		return sb.toString();
	}

	public static void parseRuleFile(String filename) {
		if (keyMap == null)
			keyMap = new HashMap<String, String>(511);

		keyMap.clear();
		int lineNr = -1;
		try {
			InputStream inStream = ResourceLocator.getResourceLocator()
					.getResourceStream(filename, AnimalFrame.runsInApplet,
							AnimalFrame.baseURL);
			if (inStream == null)
				throw new IOException(filename + " not locatable!");
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(
					inStream));
			StringBuilder contentBuffer = null;
			boolean currentLineIsEmpty = true;
			String currentLine = null;
			String currentKey = null;
			while ((currentLine = inputReader.readLine()) != null) {
				currentLine.trim();
				if (currentLineIsEmpty && currentLine.startsWith("<")
						&& currentLine.endsWith(">:")) {
					currentKey = currentLine.substring(1, currentLine.length() - 2)
							.toLowerCase();
					contentBuffer = new StringBuilder(2048);
				} else if (currentLine.length() == 0) {
					currentLineIsEmpty = true;
					keyMap.put(currentKey, contentBuffer.toString());
				} else
					contentBuffer.append(currentLine).append(MessageDisplay.LINE_FEED);
			}
		} catch (IOException e) {
			if (lineNr > 0)
				MessageDisplay.errorMsg("Sorry, exception in line " + lineNr
						+ " when reading elementDefinitions", MessageDisplay.RUN_ERROR);
			else
				MessageDisplay.errorMsg(e.getMessage(), MessageDisplay.RUN_ERROR);
		}
	}

	protected static Reader underlyingReader;

	/**
	 * Initializes a new StreamTokenizer on the input string. Will
	 * <strong>not</strong> clear or initialize any internal tables!
	 * 
	 * @param input
	 * @param fileMode
	 */
	public void generateStreamTokenizer(String input, boolean fileMode) {
		StreamTokenizer localTokenizer = null;
		if (fileMode && input != null) {
			InputStream inStream = ResourceLocator.getResourceLocator()
					.getResourceStream(
							input,
							null,
							AnimalConfiguration.getDefaultConfiguration()
									.getCurrentDirectory());
			if (inStream != null) {
				underlyingReader = new BufferedReader(new InputStreamReader(inStream));
				// localTokenizer = new StreamTokenizer(br);
			}
		} else {
			underlyingReader = new StringReader(input);
		}
		localTokenizer = new StreamTokenizer(underlyingReader);
		localTokenizer.commentChar('#');
		localTokenizer.eolIsSignificant(true);
		localTokenizer.quoteChar('"');
		if (stok != null)
			tokenizers.addElement(stok);
		stok = localTokenizer;
	}

	public void popTokenizer() {
		if (tokenizers != null && tokenizers.size() > 0)
			if (tokenizers.lastElement() != null)
				stok = tokenizers.remove(tokenizers.size() - 1);
	}

	public boolean canContinueParsing() {
		return stok.ttype != StreamTokenizer.TT_EOF
				|| (tokenizers != null && tokenizers.size() > 0);
	}

	public static Hashtable<String, String> getCurrentlyVisible() {
		return currentlyVisible;
	}

	public static XProperties getLocations() {
		return locations;
	}

	public static XProperties getObjectIDs() {
		return objectIDs;
	}

	public static XProperties getObjectProperties() {
		return objectProperties;
	}

	public static XProperties getObjectTypes() {
		return objectTypes;
	}

	protected void initializeTables() {
		objectIDs = new XProperties();
		locations = new XProperties();
		objectProperties = new XProperties();
		// externalObjectIDs = new Hashtable();
		getObjectProperties().put("Polyline.lastNode", origin);
		objectTypes = new XProperties();
	}
}
