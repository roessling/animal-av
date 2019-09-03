package animalscript.extensions;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import animal.graphics.PTText;
import animal.main.Animal;
import animal.misc.ParseSupport;
import animalscript.core.AnimalParseSupport;
import animalscript.core.AnimalScriptInterface;
import animalscript.core.BasicParser;

/**
 * This class provides an import filter for program output to Animal.
 * 
 * @author <a href="mailto:roessling@acm.org">Guido R&ouml;&szlig;ling</a>
 * @version 1.0 1999-06-05
 */
public class InternationalTextSupport extends BasicParser implements
		AnimalScriptInterface {
	// ========================= attributes =========================

	private String[] registeredLanguages = null;

	/**
	 * instantiates the key class dispatcher mapping keyword to definition type
	 */
	public InternationalTextSupport() {
		handledKeywords = new Hashtable<String, Object>();
		handledKeywords.put("itext", "parseTextInput");
		handledKeywords.put("supports", "parseLanguageSupport");
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
	 * Note that this implementation will return <code>false</code> only if the
	 * command is embedded in a grouped step.
	 * 
	 * @param command
	 *          the command used for the decision.
	 * @return true if a new step must be generated
	 */
	public boolean generateNewStep(String command) {
		return !(command.equalsIgnoreCase("iText") || sameStep);
	}

	// ===================================================================
	// Object parsing routines
	// ===================================================================

	/**
	 * Create a PTArc from the description read from the StreamTokenizer. The
	 * description is usually generated by other programs and dumped in a file or
	 * on System.out.
	 */
	public void parseLanguageSupport() throws IOException {
		ParseSupport.parseWord(stok, "Arc type");
		registeredLanguages = ParseSupport.parseOIDs(stok, null, false);
		/*
		 * for (int i=0; i<registeredLanguages.length; i++) System.err.println("#"
		 * +i +": " +registeredLanguages[i]);
		 */
		chooseLanguage();
	}

	public void parseTextInput() throws IOException {
//		String colorName, fontName = "Serif";
//		int fontStyle = 0, fontSize = 16;
//		int token = 0;
//		boolean bold = false, italic = false;
		boolean isCentered = false;
		PTText pt = new PTText();
		StringBuilder oids = new StringBuilder();

		// check for type information
		ParseSupport.parseMandatoryWord(stok, "Text type", "itext");

		// read in OID(object name)
		String s = AnimalParseSupport.parseText(stok, "Text object name");
		pt.setObjectName(s);
		/*
		 * // parse the text component ParseSupport.parseMandatoryChar(stok, "iText
		 * open parenthesis '('", '('); while (!ParseSupport.parseOptionalChar(stok,
		 * "iText closing parenthesis ')'", ')')) { stok.pushBack(); String
		 * languageTag = ParseSupport.parseWord(stok, "language token");
		 * ParseSupport.parseMandatoryChar(stok, "iText key color ':'", ':'); String
		 * text =AnimalParseSupport.parseText(stok, "Text text component"); if
		 * (languageTag.equalsIgnoreCase(chosenLanguage)) pt.setText(text); }
		 */
		pt.setText(AnimalParseSupport.parseText(stok, "Text component", "text",
				false, chosenLanguage));
		// parse location specification
		ParseSupport.parseOptionalWord(stok, "Text command 'at'(deprecated)", "at");
		Point basePoint = AnimalParseSupport.parseNodeInfo(stok, "text baseline",
				null);

		// set the location
		pt.setLocation(basePoint);

		// check if centered horizontally
		isCentered = ParseSupport.parseOptionalWord(stok,
				"'centered' tag for Text", "centered");

		// parse and set the color
		pt.setColor(AnimalParseSupport.parseAndSetColor(stok, "text", "color"));

		// check for depth information and set it, if available
		AnimalParseSupport.parseAndSetDepth(stok, pt, "text");

		// set the font
		Font f = AnimalParseSupport.parseFontInfo(stok, "text");
		pt.setFont(f);

		// if object is centered, figure out translation
		if (isCentered) {
			FontMetrics fm = Animal.getConcreteFontMetrics(f);
			// Toolkit.getDefaultToolkit().getFontMetrics(f);
			// Animal.message("Font: " +f + "\t\ttext: '" +pt.getText() +"'\n\twidth:
			// " +fm.stringWidth(pt.getText()));
			// Animal.message("Current position: " +pt.toString());
			int translationWidth = fm.stringWidth(pt.getText());
			pt.translate(-(translationWidth >>> 1), 0);
			// Animal.message("After translating: " +pt.toString());
		}

		// add the object to the list of graphic objects
		BasicParser.addGraphicObject(pt, anim);

		// append object ID to list
		oids.append(pt.getNum(false));

		// insert into object list -- necessary for lookups later on!
		getObjectIDs().put(s, pt.getNum(false));
		getObjectTypes().put(s, getTypeIdentifier("text"));

		// display the component, unless marked as 'hidden'
		AnimalParseSupport.showComponents(stok, oids.toString(), "text", true);

	}

	public void chooseLanguage() {
		/*
		 * int chosenLang = JOptionPane.showInternalOptionDialog(null, "Choose a
		 * Language:", "Language Picker", JOptionPane.YES_NO_OPTION,
		 * JOptionPane.QUESTION_MESSAGE, null, registeredLanguages,
		 * registeredLanguages[0]);
		 */
		chosenLanguage = (String) JOptionPane.showInputDialog(null,
				"Choose a language", "Language Choice", JOptionPane.QUESTION_MESSAGE,
				null, registeredLanguages, registeredLanguages[0]);
		// chosenLanguage = registeredLanguages[chosenLang];
		if (chosenLanguage == null)
			chosenLanguage = registeredLanguages[0];
		System.err.println("chosen: " + chosenLanguage);
	}
}