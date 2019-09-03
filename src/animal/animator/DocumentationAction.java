package animal.animator;

import java.net.MalformedURLException;
import java.net.URL;

import translator.AnimalTranslator;
import animal.main.Animal;
import animal.misc.MessageDisplay;

/**
 * This class supports the display of HTML documents as documentation.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling </a>
 * @version 1.1 2000-06-30
 */
public class DocumentationAction implements PerformableAction {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
//	private static final long serialVersionUID = 6001468038296090314L;

	/**
	 * The name of this animator
	 */
	public static final String TYPE_LABEL = "Documentation";

	// =================================================================
	//                             ATTRIBUTES
	// =================================================================

	/**
	 * The URL of the document to display
	 */
	private URL url;

	/**
	 * Remember if the display is in use
	 */
	public boolean used;

	// =================================================================
	//                           CONSTRUCTORS
	// =================================================================

	/**
	 * Public(empty) constructor required for serialization.
	 */
	public DocumentationAction() {
		// do nothing; only used for serialization
	} // empty!

	/**
	 * Constructs the object and sets the target URL to the String provided
	 * 
	 * @param targetURL
	 *            a String representing a(hopefully!) valid URL
	 */
	public DocumentationAction(String targetURL) {
		setURL(targetURL);
	}

	/**
	 * Constructs the object and sets the target URL
	 * 
	 * @param targetURL
	 *            the URL containing the documentation
	 */
	public DocumentationAction(URL targetURL) {
		setURL(targetURL);
	}

	// =================================================================
	//                           ANIMATION EXECUTORS
	// =================================================================

	/**
	 * Perform the action by showing the target URL in an HTML frame
	 */
	public void perform() {
		if (url != null && !used) {
			Animator.getInfoFrame().setHtmlStr(url.toString());
			used = true;
		}
	}

	// =================================================================
	//                        ATTRIBUTE GET/SET
	// =================================================================

	/**
	 * Returns the URL of the documentation
	 * 
	 * @return the URL object containing the address of the documentation
	 */
	public URL getURL() {
		return url;
	}

	/**
	 * returns the type name for this animator
	 * 
	 * @return the type name for this animator
	 */
	public String getType() {
		return TYPE_LABEL;
	}

	private URL generateURLFromString(String urlName) {
		URL targetURL = null;
		String actualURL = urlName;

		try {
			if (!urlName.startsWith("http")) {
				StringBuilder tmpURLBuffer = new StringBuilder(200);
				String defaultWebRoot = Animal.getInteractionHandler()
						.getWebRoot();
				tmpURLBuffer.append(defaultWebRoot);

				if (!defaultWebRoot.endsWith("/")) {
					tmpURLBuffer.append("/");
				}

				tmpURLBuffer.append(urlName);
				actualURL = tmpURLBuffer.toString();
			}

			targetURL = new URL(actualURL);
		} catch (MalformedURLException e) {
			MessageDisplay.errorMsg(AnimalTranslator.translateMessage(
					"malformedURLException", new Object[] { actualURL }),
					MessageDisplay.RUN_ERROR);
		}

		return targetURL;
	}

	/**
	 * Sets the URL to the String provided, which should be a valid URL string
	 * 
	 * @param urlName
	 *            the String representation of the target URL
	 */
	public void setURL(String urlName) {
		setURL(generateURLFromString(urlName));
	}

	/**
	 * Sets the URL to the URL provided
	 * 
	 * @param targetURL
	 *            the target URL
	 */
	public void setURL(URL targetURL) {
		url = targetURL;
	}


	/**
	 * Return the Animator's description to be displayed in the
	 * AnimationOverview.
	 * 
	 * @return the String representation of this object.
	 */
	public String toString() {
		if (url != null) {
			return "documentation URL: '" + url.getFile() + "'";
		}

		return AnimalTranslator.translateMessage("noDocu");
	}
}
