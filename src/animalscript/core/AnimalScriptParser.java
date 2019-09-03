package animalscript.core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import translator.AnimalTranslator;
import translator.ResourceLocator;
import animal.main.AnimalConfiguration;
import animal.main.AnimalFrame;
import animal.main.Animation;
import animal.main.AnimationWindow;
import animal.main.Link;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;
import interactionsupport.parser.AnimalscriptParser;

/**
 * This class provides an import filter for program output to Animal. It may be
 * used either via the built-in <code>main</code> method by passing the files
 * to be loaded, or by simply generating a new object via the constructor and
 * then invoking the <code>programImport</code> method.
 * 
 * @author <a href="mailto:roessling@acm.org">Guido R&ouml;&szlig;ling</a>
 * @version v1.2 2000-6-10
 * @see #programImport(java.lang.String, boolean)
 */
public class AnimalScriptParser extends BasicParser implements
		AnimalScriptInterface {
	public static String HANDLERS_FILENAME = "handlers.dat";

	public static String TYPES_FILENAME = "types.dat";

	private static final String ERROR_MESSAGE_STRING = "Animal detected some unknown commands while parsing the file.\n\nThis may be due to the following reasons:\n\n  * mistyped commands(wrong spelling),\n\n  * missing or incorrect entries in the local 'handlers.dat' file\n    All handlers must be entered in this file with their full class name,\n    e.g. animalscript.core.BaseAdminParser\n    Animal looks for the file in the local directory and will then search\n    the CLASSPATH.  Make sure that the correct 'handlers.dat'\n    was loaded; if needed, change the directory for this purpose.\n\n  * missing or invalid extension classes\n    Animal must be able to locate the classes from the CLASSPATH.\n\nIf after checking these hints you still believe Animal is wrong,\n    query the Animal WWW server which has a full listing of all\n    currently registered.  The URL is\n\n  http://www.informatik.uni-siegen.de/~roesslin/Animal/checkKW.pm\n";

	// ========================= attributes =========================

	private boolean isCompressed = false;

	private String author, title;
	public static StringBuilder fileContents = null;

	private long startTime;

	public static String currentFilename;

	public static String[] registeredHandlers = null;

	private Hashtable<String, String> unknownCommands;

	/**
	 * instantiates the key class dispatcher mapping keyword to definition type
	 */
	public AnimalScriptParser() {
		int i;
		handledKeywords = new Hashtable<String, Object>();
		String[] knownElementTypes = readSupportedTypes();
		if (registeredTypes != null)
			registeredTypes.clear();
		for (i = 0; i < knownElementTypes.length; i++) {
			registeredTypes.put(knownElementTypes[i], knownElementTypes[i]);
			knownElementTypes[i] = null;
		}
		knownElementTypes = null;

		registeredHandlers = getRegisteredHandlers();

		if (registeredKeywords != null)
			registeredKeywords.clear();
		else
			registeredKeywords = new Hashtable<String, String>(503);
		if (registeredRules != null)
			registeredRules.clear();
		else
			registeredRules = new Hashtable<String, String>(503);

		for (i = 0; i < registeredHandlers.length; i++)
			if (registeredHandlers[i] != null) {
				addScriptingHandler(registeredHandlers[i]);
			}
		// if (getObjectIDs() != null)
		// getObjectIDs().list(System.out);
	}

	public String[] getRegisteredHandlers() {
		if (registeredHandlers == null)
			registeredHandlers = readHandlersFromFile();
		return registeredHandlers;
	}

	public static void removeScriptingHandler(String handlerName) {
		AnimalScriptInterface asi = currentHandlers.get(handlerName);
		if (asi == null) {
			asi = getHandlerForName(handlerName);
		}

		if (asi != null) {
			// update rule base
			Enumeration<Object> enumerate = asi.getHandledRules();
			String command = null;
			if (enumerate != null)
				while (enumerate.hasMoreElements()) {
					// retrieve the command
					command = ((String)enumerate.nextElement()).toLowerCase();

					// remove from rule base
					registeredRules.remove(command);
				}

			// update keyword base
			Enumeration<String> keywords = asi.getHandledKeywords();
			if (keywords != null)
				while (keywords.hasMoreElements()) {
					// retrieve the keyword
					command = keywords.nextElement().toLowerCase();

					// retrieve from keyword base
					registeredKeywords.remove(command);
				}
		}
	}

	@SuppressWarnings("unchecked")
	public static void addScriptingHandler(String handlerName) {
		try {
			Class<AnimalScriptInterface> loadedClass = 
				(Class<AnimalScriptInterface>)Class.forName(handlerName);
			AnimalScriptInterface o = loadedClass.newInstance();
			String currentRule = null, command = null;
			Enumeration<?> enumerate = o.getHandledRules();
			if (enumerate != null)
				while (enumerate.hasMoreElements()) {
					command = ((String) enumerate.nextElement()).toLowerCase();
					currentRule = o.getRuleFor(command);
					if (currentRule == null)
						System.err.println("*****Rule for '" + command
								+ "' is null *****");
					else if (registeredRules.containsKey(command))
						MessageDisplay.errorMsg("!!! conflict in class " + handlerName
								+ ": '" + command + "' already assigned to "
								+ registeredRules.get(command), MessageDisplay.RUN_ERROR);
					else {
						registeredRules.put(command, currentRule);
					}
				}
			enumerate = o.getHandledKeywords();
			if (enumerate != null)
				while (enumerate.hasMoreElements()) {
					command = ((String) enumerate.nextElement()).toLowerCase();
					if (registeredKeywords.containsKey(command))
						MessageDisplay.errorMsg("!!! conflict in class " + handlerName
								+ ": '" + command + "' already assigned to "
								+ registeredKeywords.get(command), MessageDisplay.RUN_ERROR);
					else {
						registeredKeywords.put(command, handlerName);
					}
				}
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
	}

	public String[] readSupportedTypes() {
		Vector<String> handledTypes = new Vector<String>(20, 10);
		String s;
		try {
			ResourceLocator resLoc = ResourceLocator.getResourceLocator();
			InputStreamReader in;
			// try {
			// in = new InputStreamReader(new FileInputStream(TYPES_FILENAME));
			// }
			// catch(FileNotFoundException notFound)
			// {
			// in = new
			// InputStreamReader(ClassLoader.getSystemResourceAsStream(TYPES_FILENAME));
			// }
			in = new InputStreamReader(resLoc.getResourceStream(TYPES_FILENAME,
					AnimalFrame.runsInApplet, AnimalFrame.baseURL));
			BufferedReader is = new BufferedReader(in);
			while ((s = is.readLine()) != null)
				handledTypes.addElement(s.toLowerCase());
			is.close();
		} catch (FileNotFoundException e) {
			MessageDisplay.errorMsg("File " + e.getMessage() + " not found",
					MessageDisplay.RUN_ERROR);
		} catch (IOException e) {
			MessageDisplay.errorMsg("Error while closing " + e.getMessage(),
					MessageDisplay.PROGRAM_ERROR);
		}
		s = null;
		String[] handledElements = new String[handledTypes.size()];
		handledTypes.copyInto(handledElements);
		handledTypes.removeAllElements();
		handledTypes = null;
		return handledElements;
	}

	public String[] readHandlersFromFile() {
		Properties props = AnimalConfiguration.getDefaultConfiguration()
				.getEntriesForPrefix("animalscript.");
		String[] handlers = new String[props.size()];
		Enumeration<Object> propElems = props.keys();
		int pos = 0;
		while (propElems.hasMoreElements()) {
			handlers[pos++] = props.getProperty((String)propElems.nextElement());
		}
		return handlers;
	}

	public Animation programImport(String filename) {
		return importAnimationFrom(filename, true); // programImport(filename,
		// true);
	}

	public Animation programImport(String filename, boolean useFileMode) {
		if (useFileMode)
			return importAnimationFrom(filename, true); // programImport(filename,
		// true);

		if (stok == null) {
			System.err.println("using String-based mode... creating new stream tokenizer...");
			generateStreamTokenizer(filename, false);
		}
		return importAnimationFrom(new StringReader(filename), true);
	}

	/**
	 * imports all definitions from the file passed, catching any occurring
	 * IOException.
	 * 
	 * Note that the file's first line must be '% Animal' followed by the version
	 * as an integer and a return.
	 * 
	 * @param filename
	 *          the file to be parsed
	 * @return the animation retrieved from the file
	 */
	public Animation importAnimationFrom(String filename,
			boolean createNewAnimation) {
		Animation tmpAnimation = null;
		try {
			InputStream in = null;
			if (filename.startsWith("http:") || filename.startsWith("https:")
					|| filename.startsWith("file:")) {
				URL targetURL = new URL(filename);
//				if (targetURL == null)
//					return null;
				in = targetURL.openStream();
			} else
				in = new FileInputStream(filename);

			if (filename != null && in != null) {
				int index = filename.lastIndexOf(System.getProperty("file.separator"));
				if (index != -1)
					AnimalConfiguration.getDefaultConfiguration().setCurrentDirectory(
							filename.substring(0, index));
			}
			tmpAnimation = importAnimationFrom(in, filename, createNewAnimation);
			in.close();
		} catch (FileNotFoundException fnfe) {
			MessageDisplay.errorMsg("File not found: " + filename,
					MessageDisplay.RUN_ERROR);
		} catch (IOException e) {
			MessageDisplay.errorMsg("IO-Error while reading " + e.getMessage(),
					MessageDisplay.RUN_ERROR);
			e.printStackTrace();
		}
		return tmpAnimation;
	}

	public Animation importAnimationFrom(InputStream in, String filename,
			boolean createNewAnimation) {
		Reader isr = null;
		try { 
			if (in != null) {
				if (isCompressed)
					isr = new InputStreamReader(new GZIPInputStream(in), "UTF-8");
				else
					isr = new InputStreamReader(in, "UTF-8");
			}	else if (filename.startsWith("%Animal"))
				isr = new StringReader(new String(filename.getBytes(), "UTF-8"));

//			isr = new InputStreamReader(in, "utf-8");
		} catch(IOException e) {
			System.err.println("Conversion to utf 8 failed!");
		}
		return importAnimationFrom(isr, filename, createNewAnimation);
	}
	
	public Animation importAnimationFrom(Reader reader, String filename,
			boolean createNewAnimation) {
		employsQuestions = false;
		currentFilename = filename;
		Animation tmpAnimation = null;
		startTime = System.currentTimeMillis();
		BufferedReader br = null;
		try {
//			Reader isr = null;
//			if (in != null) {
//				if (isCompressed)
//					isr = new InputStreamReader(new GZIPInputStream(in));
//				else
//					isr = new InputStreamReader(in);
//			} else if (filename.startsWith("%Animal"))
//				isr = new StringReader(filename);
			br = new BufferedReader(reader);
			// was: StringBuilder fileContents = ...
			fileContents = new StringBuilder(65536);
			String currentLine = null;
			while ((currentLine = br.readLine()) != null)
				fileContents.append(currentLine).append(MessageDisplay.LINE_FEED);
			br.close();
			generateStreamTokenizer(fileContents.toString(), false);
			tmpAnimation = importAnimationFrom(new StringReader(fileContents
					.toString()), createNewAnimation);
			
		} catch (IOException e) {
			MessageDisplay.errorMsg("IO-Error while reading " + e.getMessage(),
					MessageDisplay.RUN_ERROR);
			e.printStackTrace();
			return null;
		}
		employsQuestions = false;
		return tmpAnimation;
	}

	public Animation importAnimationFrom(Reader br, boolean createNewAnimation) {
		Animation tmpAnimation;
		double localWidth = -1, localHeight = -1;
		try {
			initializeTables();
			unknownCommands = new Hashtable<String, String>(43);
			int token = stok.nextToken();

			// parse header, if present!
			// should be "% Animal 1.3" or similar.
			if (token == '%') {
				ParseSupport.parseMandatoryWord(stok,
						"Protocol header '%Animal <version>", "Animal");
				ParseSupport.parseInt(stok, "Animal version"); 
				localWidth = ParseSupport.parseOptionalNumber(stok, "Animation width");
				if (localWidth > 0 && localWidth != Double.MAX_VALUE) {
					ParseSupport.parseMandatoryChar(stok, "anim size separator '*'", '*');
					localHeight = ParseSupport.parseOptionalNumber(stok, "Animation width");				
				}
				ParseSupport.consumeIncludingEOL(stok, "Animal I/O header");
			} else {
//				System.err.println("TT_WORD: " +stok.TT_WORD +", num: " +stok.TT_NUMBER +" eol: " +stok.TT_EOL + " eof: " +stok.TT_EOF +" // " +stok.sval.length() + "," +stok.sval);
				stok.pushBack();
			}
			

			token = stok.nextToken();
			if (token == StreamTokenizer.TT_WORD
					&& stok.sval.equalsIgnoreCase("title")) {
				title = AnimalParseSupport.parseText(stok, "Animation title");
				ParseSupport.consumeIncludingEOL(stok, "EOL after animation title");
			} else
				stok.pushBack();

			token = stok.nextToken();
			if (token == StreamTokenizer.TT_WORD
					&& stok.sval.equalsIgnoreCase("author")) {
				author = AnimalParseSupport.parseText(stok, "Animation Author");
				ParseSupport.consumeIncludingEOL(stok, "EOL after animation author");
			} else
				stok.pushBack();
			tmpAnimation = programImport(createNewAnimation);
			if (localWidth > 0 && localHeight > 0 
					&& localWidth != Double.MAX_VALUE && localHeight != Double.MAX_VALUE) {
				tmpAnimation.setWidth((int)localWidth);
				tmpAnimation.setHeight((int)localHeight);
				AnimationWindow animWin = 
                  AnimalConfiguration.getDefaultConfiguration().getWindowCoordinator().getAnimationWindow(true);
                animWin.getAnimationCanvas().setSize((int)localWidth, (int)localHeight);
                animWin.updatePack();
//                System.err.println("aCanv size should now be " +localWidth +"x" +localHeight);
			}
			tmpAnimation.setAuthor(author);
			tmpAnimation.setTitle(title);
			br.close();
		} catch (IOException e) {
			MessageDisplay.errorMsg("IO-Error while reading " + e.getMessage(),
					MessageDisplay.RUN_ERROR);
			e.printStackTrace();
			return null;
		}

		tmpAnimation.resetChange();
		return tmpAnimation;
	}

	/**
	 * Read in the program output and try to generate a fitting animation.
	 * 
	 * This actually performs the work after the StreamTokenizer has been created.
	 * <em>Do not call it directly from other classes</em>.
	 * 
	 * @return the animation parsed in
	 * @throws IOException
	 *           if there was a parse problem.
	 */
	public Animation programImport(boolean createNewAnimation) throws IOException {
		int token = 0;
		String s;
		AnimalscriptParser.setLastInteractionFileNull();
		Animation tmpStore = anim;
		if (createNewAnimation || tmpStore == null) {
			clearTables();
			anim = new Animation();
			anim.resetNextGraphicObjectNum();
			currentlyVisible = new Hashtable<String, String>(503);
			currentStep = 0;
		} else {
			anim = tmpStore;
			if (currentlyVisible == null)
				currentlyVisible = new Hashtable<String, String>(503);
			// attention: last step in animation currently has "next=Link.END"!
			Link lastLink = anim.getLink(currentStep);
			// System.err.println(lastLink.getNextStep());
			lastLink.setNextStep(currentStep + 1);
			// System.err.println(lastLink.getNextStep());
		}

		while (canContinueParsing()) {
			token = stok.nextToken();
			if (token == StreamTokenizer.TT_EOF) {
				popTokenizer();
			} else {
				while (token == StreamTokenizer.TT_EOL)
					token = stok.nextToken();
				if (token == StreamTokenizer.TT_EOF)
					break; // oops... EOF after EOL...

				// 'switch' over the token read in... word, '{', '}', or error
				if (token != StreamTokenizer.TT_WORD) {
					if (token == '{' || token == '}') {
					  //GR if '{' encountered, mark that we have entered a step;
					  // otherwise (=> we have read '}'), close grouped step mode
					  groupedStepMode = (token == '{');
						sameStep = (token == '{');
						//GR
						if (groupedStepMode)
						  newLink(); // generate in any case, for start OR stop!
						//						if (sameStep)
//							newLink();

						ParseSupport.consumeIncludingEOL(stok, "step begin/end mark");
					} else
						MessageDisplay.errorMsg("Expected Word, read in " + token + "/"
								+ stok.nval + "/" + stok.sval + "/" + stok.ttype
								+ StreamTokenizer.TT_EOL + " in line " + stok.lineno(),
								MessageDisplay.RUN_ERROR);
				} else {
					// extract local type information
					s = stok.sval;
					process(stok, s);
				} // token != StreamTokenizer.TT_WORD
				if (debugMode)
					System.err.print('.');
			} // while
		}
		if (debugMode)
			System.err.println(' ');

		Link lastLink = anim.getLink(currentStep);
		lastLink.setNextStep(Link.END);
		String displayFilename = currentFilename;
		if (displayFilename == null || displayFilename.startsWith("%Animal"))
			displayFilename = "(internal String input)";
		MessageDisplay.message(AnimalTranslator.translateMessage(
				"animalScriptParseFinished", new String[] {
						Integer.toString(errorCounter), displayFilename,
						Long.toString(System.currentTimeMillis() - startTime) }));
		errorCounter = 0;
		anim.setLanguage(chosenLanguage);
		if (unknownCommands.size() != 0) {
			StringBuilder message = new StringBuilder(1000);
			message.append(ERROR_MESSAGE_STRING);
			message.append(MessageDisplay.LINE_FEED).append("\t\tOffending commands:");
			message.append(MessageDisplay.LINE_FEED);
			Enumeration<String> enumerate = unknownCommands.keys();
			while (enumerate.hasMoreElements()) {
				Object entry = enumerate.nextElement();
				message.append(entry).append(" - ");
				message.append(unknownCommands.get(entry));
				message.append(MessageDisplay.LINE_FEED);
			}
		}
		// adjust the color!
		return anim;
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
	 */
	public XProperties process(StreamTokenizer aStok, String currentCommand)
			throws IOException {
		XProperties props = null;
		// determine correct foundry
		try {
			AnimalScriptInterface handler = getHandlerForCommand(currentCommand
					.toLowerCase());
			if (handler != null) {
				props = handler.process(aStok, currentCommand);
			} else {
				errorCounter++;
				if (unknownCommands.containsKey(currentCommand.toLowerCase()))
					unknownCommands.put(currentCommand.toLowerCase(), unknownCommands
							.get(currentCommand.toLowerCase())
							+ ", " + aStok.lineno());
				else
					unknownCommands.put(currentCommand.toLowerCase(), "in line(s) "
							+ aStok.lineno());
				MessageDisplay.errorMsg("Handler is null for " + currentCommand
						+ " in line " + aStok.lineno() + ", skipping rest of line",
						MessageDisplay.RUN_ERROR);
				String unknownLine = ParseSupport.consumeIncludingEOL(aStok,
						"***error line***");
				MessageDisplay.message(currentCommand.toLowerCase() + " [line "
						+ aStok.lineno() + "]: " + unknownLine);
			}
		} catch (IOException e) {
			MessageDisplay.errorMsg("# *** Error parsing file: " + e.getMessage()
					+ " current state: " + aStok.toString(), MessageDisplay.RUN_ERROR);

			errorCounter++;
			if (errorCounter >= 20)
				throw new IOException(
						"Animation contains at least 20 errors, aborting!");
		}
		if (props == null)
			props = new XProperties();
		return props;
	}

	public void setCompressed(boolean compressed) {
		isCompressed = compressed;
	}
}
