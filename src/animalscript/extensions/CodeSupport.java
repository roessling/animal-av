package animalscript.extensions;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.IOException;
import java.util.Hashtable;

import animal.animator.ColorChanger;
import animal.animator.TimedShow;
import animal.graphics.PTPoint;
import animal.misc.MessageDisplay;
import animal.misc.ParseSupport;
import animal.misc.XProperties;
import animalscript.core.AnimalParseSupport;
import animalscript.core.AnimalScriptInterface;
import animalscript.core.BasicParser;

/**
 * This class provides an import filter for AnimalScript code commands
 * 
 * @author <a href="mailto:roessling@acm.org">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 2000-03-21
 */
public class CodeSupport extends BasicParser implements AnimalScriptInterface {
	private CodeProducer codeProducer;

	/**
	 * instantiates the key class dispatcher mapping keyword to definition type
	 */
	public CodeSupport() {
		codeProducer = new CodeProducer();
		handledKeywords = new Hashtable<String, Object>();
		rulesHash = new XProperties();

		handledKeywords.put("codegroup", "parseCodeInput");
		// rulesHash.put("codegroup", "codegroup <id> [at] <nodeDefinition> [color
		// <color>] [highlightColor <color>] [contextColor <color>] [<fontInfo>]
		// [depth <n>] [after <n> [ms | ticks]]");

		handledKeywords.put("addcodeelem", "parseCodeInput");
		// rulesHash.put("addcodeelem", "addCodeElem \"text\" [name \"text\"] to
		// <id> [row <n>] [indentation <n>] [after <n> [ms | ticks]]");

		handledKeywords.put("addcodeline", "parseCodeInput");
		// rulesHash.put("addcodeline", "addCodeLine \"text\" [name \"text\"] to
		// <id> [indentation <n>] [after <n> [ms | ticks]]");

		handledKeywords.put("highlightcode", "parseColorChange");
		// rulesHash.put("highlightcode", "highlightCode on <id> line <n> [row <n>]
		// [region | context] [<timing>]");
		handledKeywords.put("unhighlightcode", "parseColorChange");
		// rulesHash.put("unhighlightcode", "unhighlightCode on <id> line <n> [row
		// <n>] [region | context] [<timing>]");

		handledKeywords.put("hidecode", "parseCodeDisplay");
		// rulesHash.put("hidecode", "hideCode <id> [after <n> [ms | ticks]]");
	}

	// ===================================================================
	// interface methods
	// ===================================================================

	/**
	 * Determine depending on the command passed if a new step is needed Also keep
	 * in mind that we might be in a grouped step using the {...} form. Usually,
	 * every command not inside such a grouped step is contained in a new step.
	 * However, this is not the case for operations without visible effect -
	 * mostly maintenance or declaration entries.
	 * 
	 * @param command
	 *          the command used for the decision.
	 * @return true if a new step must be generated
	 */
	public boolean generateNewStep(String command) {
		return (!sameStep && !command.equalsIgnoreCase("addCodeGroup"));
	}

	// ===================================================================
	// Animator parsing routines
	// ===================================================================

	/**
	 * Perform the requested operations for inserting source code
	 */
	public XProperties parseCodeInput() throws IOException {
		String operationType = ParseSupport.parseWord(stok, "code keyword");
		String codeGroupName = null;
		boolean inlineMode = false;

		if (operationType.equalsIgnoreCase("codeGroup")) {
			// define a new code group by giving the upper left corner
			// may also specify element color, highlight color, region color(for
			// context, e.g. loop command when inside the loop body), and depth
			codeGroupName = AnimalParseSupport.parseText(stok, operationType
					+ " name");
			getObjectIDs().put(codeGroupName, "");
			// start with line 0!
			getObjectProperties().put(codeGroupName + ".lineNo", 0);

			// Parse coords for component(ULC)
			ParseSupport.parseOptionalWord(stok, operationType
					+ " location keyword 'at'", "at");
			Point startingPoint = AnimalParseSupport.parseNodeInfo(stok,
					operationType + " upper left corner", null);
			getObjectProperties().put(codeGroupName + ".x0", startingPoint.x);
			getObjectProperties().put(codeGroupName + ".y", startingPoint.y);
			getObjectProperties().put(codeGroupName + ".y0", startingPoint.y);

			// Check for optional components: 'color', 'highlightColor', 'regionColor'
			getObjectProperties().put(
					codeGroupName + ".color",
					AnimalParseSupport.parseAndSetColor(stok, codeGroupName, "color",
							"black"));
			getObjectProperties().put(
					codeGroupName + ".highlightColor",
					AnimalParseSupport.parseAndSetColor(stok, codeGroupName,
							"highlightColor", "red"));
			getObjectProperties().put(
					codeGroupName + ".contextColor",
					AnimalParseSupport.parseAndSetColor(stok, codeGroupName,
							"contextColor", "blue"));

			/*
			 * // FORMER VERSION: Color basicColor =
			 * AnimalParseSupport.parseAndSetColor(stok, codeGroupName, "color",
			 * "black"); Color highlightColor =
			 * AnimalParseSupport.parseAndSetColor(stok, codeGroupName,
			 * "highlightColor", "red"); Color contextColor =
			 * AnimalParseSupport.parseAndSetColor(stok, codeGroupName,
			 * "contextColor", "blue");
			 */

			Font f = AnimalParseSupport.parseFontInfo(stok, codeGroupName);
			getObjectProperties().put(codeGroupName + ".font", f);
			// getObjectProperties().put(codeGroupName +".font", f.getName());
			// getObjectProperties().put(codeGroupName +".fontSize", f.getSize());
			// getObjectProperties().put(codeGroupName +".fontStyle", f.getStyle());

			f = null;

			PTPoint p = new PTPoint(startingPoint);
			AnimalParseSupport.parseAndSetDepth(stok, p, operationType);
			getObjectProperties().put(codeGroupName + ".depth", p.getDepth());
			p = null;
			String unit = "ticks";
			// check for offset keyword 'after'
			if (ParseSupport.parseOptionalWord(stok, operationType
					+ "keyword 'after'", "after")) {
				// parse the offset
				getObjectProperties().put(codeGroupName + ".delay",
						ParseSupport.parseInt(stok, operationType + " delay", 0));

				// parse unit
				unit = ParseSupport.parseWord(stok, operationType + "delay unit");
				if (unit == null)
					unit = "ticks";
				getObjectProperties().put(codeGroupName + ".delayUnit", unit);
			}
		} else if (operationType.equalsIgnoreCase("addCodeLine")
				|| operationType.equalsIgnoreCase("addCodeElem")) {
			XProperties codeProperties = new XProperties();
			boolean fullLine = operationType.equalsIgnoreCase("addCodeLine");

			// add a single line of code to a given code group, indented as given.
			String text = AnimalParseSupport.parseText(stok, operationType + " code",
					null, false, chosenLanguage);
			codeProperties.put("code.text", text);
			String name = null;
			if (ParseSupport.parseOptionalWord(stok, operationType
					+ " keyword 'name'", "name")) {
				name = AnimalParseSupport.parseText(stok, operationType + " name");
			}

			ParseSupport.parseMandatoryWord(stok, operationType + " keyword 'to'",
					"to");
			String baseCodeGroup = AnimalParseSupport.parseText(stok, operationType
					+ "base code group");
			if (getObjectProperties().getProperty(baseCodeGroup + ".x0") == null)
				ParseSupport.formatException("code group '" + baseCodeGroup
						+ " not existent!", stok);
			codeProperties.put("code.baseGroup", baseCodeGroup);

			int lineNr = getObjectProperties().getIntProperty(
					baseCodeGroup + ".lineNo");
			if (name == null && fullLine)
				name = baseCodeGroup + "[" + lineNr + "][0]";

			int colNr = 0;
			if (!fullLine) {
				if ((ParseSupport.parseOptionalWord(stok, operationType
							+ " keyword 'row'", "row")
							|| ParseSupport.parseOptionalWord(stok, operationType
									+ " keyword 'column'", "column"))) {
				colNr = ParseSupport.parseInt(stok, operationType + " column position", 0);
				if (colNr != 0)
					lineNr--;
				if (name == null || colNr != 0)
					name = baseCodeGroup + "[" + lineNr + "][" + colNr + "]";
				}
				else name=baseCodeGroup +"["+lineNr +"][x]";
				if (ParseSupport.parseOptionalWord(stok, operationType + "keyword 'inline'", "inline"))
				  inlineMode = true;
			}
			codeProperties.put("code.lineNr", lineNr);
			codeProperties.put("code.colNr", colNr);
			codeProperties.put("code.name", name);
			codeProperties.put("code.inline", inlineMode);

			// parse optional indentation
			if (ParseSupport.parseOptionalWord(stok, operationType
					+ " keyword 'indentation'", "indentation"))
				codeProperties.put("code.indentation", ParseSupport.parseInt(stok,
						operationType + " indentation", 0));

			int displayDelay = getObjectProperties().getIntProperty(
					baseCodeGroup + ".delay", 0);
			String unit = null;

			// check for offset keyword 'after'
			if (ParseSupport.parseOptionalWord(stok, operationType
					+ "keyword 'after'", "after")) {
				displayDelay += ParseSupport.parseInt(stok, operationType + "delay", 0);
				// parse unit
				unit = ParseSupport.parseWord(stok, operationType + "delay unit");
				if (unit == null)
					unit = "ticks";
				codeProperties.put("code.delayUnit", unit);
			}
			// parse the offset
			codeProperties.put("code.delay", displayDelay);

			codeProducer.insertCode(codeProperties, getObjectProperties());
			return codeProperties;
		}
		return null;
	}

	/**
	 * Create a ColorChange from the description read from the StreamTokenizer.
	 * The description is usually generated by other programs and dumped in a file
	 * or on System.out.
	 */
	public void parseColorChange() throws IOException {
		// parse exact command
		String colorType = ParseSupport.parseWord(stok, "color change operation");
		boolean contextMode = false;
		ColorChanger colChanger = null;
		Color c = null;

		ParseSupport.parseMandatoryWord(stok, colorType + " keyword 'on'", "on");
		String targetCodeGroup = AnimalParseSupport.parseText(stok, colorType
				+ " code group");
		ParseSupport
				.parseMandatoryWord(stok, colorType + " keyword 'line'", "line");
		int targetLine = ParseSupport.parseInt(stok, colorType + " line number", 0,
				getObjectProperties().getIntProperty(targetCodeGroup + ".lineNo"));
		int targetCol = 0;
		if (ParseSupport.parseOptionalWord(stok, colorType + " keyword 'column'",
				"column") || ParseSupport.parseOptionalWord(stok, colorType + " keyword 'row'",
				"row")) {
			targetCol = ParseSupport.parseInt(stok, colorType + " column number", 0);
			if (getObjectIDs().getProperty(
					targetCodeGroup + "[" + targetLine + "][" + targetCol + "]") == null) {
				MessageDisplay.errorMsg("Cannot highlight nonexisting code column "
						+ targetCol + " in input line " + stok.lineno(),
						MessageDisplay.RUN_ERROR);
				targetCol = -1;
			}
		}
		// check for mode
		contextMode = ParseSupport.parseOptionalWord(stok, colorType
				+ " keyword 'region'", "region")
				|| ParseSupport.parseOptionalWord(stok,
						colorType + " keyword 'region'", "context");
		// generate the animator!
		if (colorType.equalsIgnoreCase("unhighlightCode"))
			c = getObjectProperties().getColorProperty(targetCodeGroup + ".color",
					Color.black);
		else
			c = (contextMode) ? getObjectProperties().getColorProperty(
					targetCodeGroup + ".contextColor", Color.blue)
					: getObjectProperties().getColorProperty(
							targetCodeGroup + ".highlightColor", Color.red);

		int[] targetOIDs = { getObjectIDs().getIntProperty(
				targetCodeGroup + "[" + targetLine + "][" + targetCol + "]") };
		// generate a generic color changer move (instant change)
		colChanger = new ColorChanger(currentStep, targetOIDs, 0, "color", c);
		// parse optional timing - is set within the method!
		AnimalParseSupport.parseTiming(stok, colChanger, "Color");

		// insert into list of animators
		BasicParser.addAnimatorToAnimation(colChanger, anim);
	}

	/**
	 * Show or hide the selected object IDs, possibly with delay
	 * 
	 * The description is usually generated by other programs and dumped in a file
	 * or on System.out.
	 */
	public void parseCodeDisplay() throws IOException {
		// Read in the command
		String localType = ParseSupport.parseWord(stok, "show/hide type");
		boolean isShow = localType.equalsIgnoreCase("showcode");
		String codeBaseGroup = AnimalParseSupport.parseText(stok,
				"hideCode base group name");
		int[] ids = getObjectIDs().getIntArrayProperty(codeBaseGroup);
		// check for keyword 'after'
		if (ParseSupport.parseOptionalWord(stok, localType + " 'after' keyword",
				"after")) {
			// push back the token for the parsing method!
			stok.pushBack();

			// generate the TimedShow animator
			TimedShow ts = new TimedShow(currentStep, ids, 0, (isShow) ? "show"
					: "hide", isShow);

			// now parse the timing information
			AnimalParseSupport.parseTiming(stok, ts, "Timed Show");

			// insert the animator
			BasicParser.addAnimatorToAnimation(ts, anim);
		} else {
			// insert a 'normal', i.e. untimed, show
			BasicParser.addAnimatorToAnimation(new TimedShow(currentStep, ids, 0,
					(isShow) ? "show" : "hide", isShow), anim);
		}
	}

}
