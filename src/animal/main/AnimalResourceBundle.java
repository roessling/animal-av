package animal.main;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.Vector;

import translator.AnimalTranslator;
import translator.ResourceLocator;
import animal.misc.MessageDisplay;

/**
 * A special type of PropertyResourceBundle support
 * 
 * @version 1.1 2000-01-11
 * @author Guido R&ouml;&szlig;ling (<a href="mailto:roessling@acm.org">
 *         roessling@acm.org</a>)
 */
public class AnimalResourceBundle {
	/**
	 * The property resource bundle used for storing the current resources
	 */
	PropertyResourceBundle bundle = null;

	/**
	 * Default constructor
	 */
	public AnimalResourceBundle() {
		// do nothing
	}

	/**
	 * Generate a new instance by invoking this(Locale, String)
	 * 
	 * @param locale
	 *          the target locale
	 */
	public AnimalResourceBundle(Locale locale) {
		this(locale, "AnimalResources");
	}

	/**
	 * Generate a new instance by retrieving the appropriate resource
	 * 
	 * @param locale
	 *          the target locale
	 * @param filename
	 *          the base name of the file for the resources. Extended by adding
	 *          '.' and the result of locale.getCountry()
	 * 
	 * @see java.util.Locale#getCountry()
	 */
	public AnimalResourceBundle(Locale locale, String filename) {
	  Locale currentLocale = locale;
	  String actualFilename = filename;
		if (currentLocale == null)
		  currentLocale = Locale.US;

		if (locale.getCountry() != null)
		  actualFilename += '.' + locale.getCountry();
		InputStream in = null;
		in = getClass().getResourceAsStream(actualFilename);
		if (in == null)
			in = ClassLoader.getSystemResourceAsStream(actualFilename);
    if (in == null)
      in = ResourceLocator.getResourceLocator().getResourceStream(filename);
		if (in == null)
			MessageDisplay.errorMsg("resourceNotFound", actualFilename,
					MessageDisplay.INFO);
		try {
			if (in != null)
				bundle = new PropertyResourceBundle(new BufferedInputStream(in));
			// printProperties();
		} catch (IOException ioException) {
			MessageDisplay.errorMsg("resourceIOExc",
					new String[] {actualFilename, ioException.getMessage() },
					MessageDisplay.INFO);
		}
	}

	/**
	 * Generate a new bundle from the given name and language code
	 * 
	 * @param baseName
	 *          the base file name
	 * @param languageCode
	 *          the extension of the file name, resulting name will be baseName
	 *          +"." +languageCode
	 */
	public AnimalResourceBundle(String baseName, String languageCode) {
		String filename = baseName + '.' + languageCode;
		InputStream in = null;
		try {
			in = new FileInputStream(filename);
		} catch (IOException e) {
			MessageDisplay.errorMsg("resourceNotInDir", filename,
					MessageDisplay.RUN_ERROR);
		}
		if (in == null)
			in = getClass().getResourceAsStream(filename);
		if (in == null)
			in = ClassLoader.getSystemResourceAsStream(filename);
    if (in == null)
      in = ResourceLocator.getResourceLocator().getResourceStream(filename);
		if (in == null)
			MessageDisplay.errorMsg("resourceNotFound", filename,
					MessageDisplay.INFO);
		try {
			if (in != null)
				bundle = new PropertyResourceBundle(new BufferedInputStream(in));
		} catch (IOException ioException) {
			MessageDisplay.errorMsg("resourceIOExc",
					new String[] { filename, ioException.getMessage() },
					MessageDisplay.INFO);
		}
	}

	// ======================================================================
	// Attribute access
	// ======================================================================

	/**
	 * Retrieve the keys of this resource
	 * 
	 * @return a String[] of all keys in this resource
	 */
	public String[] getKeys() {
		Vector<String> v = new Vector<String>(128);
		Enumeration<String> e = bundle.getKeys();
		while (e.hasMoreElements())
			v.addElement(bundle.getString(e.nextElement()));
		String[] result = new String[v.size()];
		for (int i = 0; i < result.length; i++)
			result[i] = v.elementAt(i);
		return result;
	}

	/**
	 * Convenvience wrapper for retrieving the message for key 'key' Internally
	 * invokes getMessage(key, true)
	 * 
	 * @param key
	 *          the key of the message to retrieve
	 * @return the retrieved message or null, if no message was found
	 * @see #getMessage(String, boolean)
	 */
	public String getMessage(String key) {
		return getMessage(key, true);
	}

	/**
	 * Method for retrieving the message for key 'key'
	 * 
	 * @param key
	 *          the key of the message to retrieve
	 * @param warnOnError
	 *          if true, display a warning on System.err if no appropriate
	 *          resource was found.
	 * @return the retrieved message or null, if no message was found
	 */
	public String getMessage(String key, boolean warnOnError) {
		try {
			return bundle.getString(key);
		} catch (MissingResourceException nullPointerException) {
			if (warnOnError)
				MessageDisplay.errorMsg("resourceMissing", key,
						MessageDisplay.CONFIG_ERROR);
		}
		return null;
	}

	/**
	 * Print the properties stored in the bundle to System.out
	 */
	public String printProperties() {
		if (bundle == null)
			return "";

		StringBuilder sbuf = new StringBuilder(2048);
		Enumeration<String> e = bundle.getKeys();
		while (e.hasMoreElements()) {
			String key = e.nextElement();
			sbuf.append(AnimalTranslator.translateMessage("propPrint", 
					new Object[] {key, bundle.getObject(key) }));
			sbuf.append(MessageDisplay.LINE_FEED);
		}
		return sbuf.toString();
	}
}
