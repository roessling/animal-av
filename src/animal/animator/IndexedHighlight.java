package animal.animator;

import translator.AnimalTranslator;


public class IndexedHighlight extends TimedAnimator  {
	
	/**
	 * The name of this animator
	 */
	public static final String TYPE_LABEL = "IndexedHighlight";
//TODO toString anpassen. Von Methode einfach nur Substring ab erster pos von " "
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
	 * @return the property encapsulated in an IndexedContentProperty object at this time
	 */
	public Object getProperty(double factor) {
		return new IndexedContentProperty(new Double(factor));
	}

	@Override
	public String getAnimatorName() {
		return "IndexedHighlight";
	}

	@Override
	public String getType() {
		return TYPE_LABEL;
	}

	@Override
	public String[] handledKeywords() {
		return new String[] {"IndexedHighlight"};
	}
	
	/**
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
	 * Return the Animator's description to be displayed in the
	 * AnimationOverview.
	 * 
	 * @return the String representation of this object.
	 */
	public String toString() {
		String[] tokens = this.getMethod().split(" ");
		StringBuilder showMe = new StringBuilder();
		showMe.append(IndexedHighlight.TYPE_LABEL);
		showMe.append(" ").append(AnimalTranslator.translateMessage(tokens[1]));
		showMe.append(" ").append(tokens[2]).append(" of ");
		showMe.append(" ").append(super.toString());
		return showMe.toString();
	}
}
