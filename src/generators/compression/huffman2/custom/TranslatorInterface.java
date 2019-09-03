package generators.compression.huffman2.custom;

import java.util.Locale;

/**
 * The Class TranslatorInterface.
 */
public class TranslatorInterface {

	/** The translator. */
	// TODO static Translator translator = new Translator("langFiles/files",
	// Locale.ENGLISH);

	/**
	 * Inits the.
	 *
	 * @param language
	 *            the language
	 */
	static void init(Locale language) {
		// TODO translator.setTranslatorLocale(language);
	}

	/**
	 * Translate.
	 *
	 * @param id
	 *            the id
	 * @return the string
	 */
	static String translate(String id) {
		return id;
		// return translator.translateMessage(id);
	}
}
