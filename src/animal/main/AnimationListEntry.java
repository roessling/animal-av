package animal.main;

import animal.animator.Animator;

/**
 * AnimationListEntry is an AnimationOverview's List's entries. It knows what
 * kind of object is in the line(whether Animator or step), what instance it is
 * and how the instance is described.
 * 
 * As this is just an auxiliary class, all values are public.
 * 
 * @author <a href="http://www.ahrgr.de/guido/">Guido
 *         R&ouml;&szlig;ling</a>
 * @version 1.0 18.07.1998
 */
public class AnimationListEntry {
	/**
	 * mode variables
	 */
	public static final int ANIMATOR = 1;

	public static final int STEP = 2;

	public String info;

	public Animator animator = null;

	public Link link = null;

	public int mode = 0;

	/**
	 * this entry is an Animator.
	 */
	public AnimationListEntry(String animatorInfo, Animator theAnimator) {
		info = animatorInfo;
		animator = theAnimator;
		mode = ANIMATOR;
	}

	/**
	 * this entry is a Link.
	 */
	public AnimationListEntry(String linkInfo, Link theLink) {
		info = linkInfo;
		link = theLink;
		mode = STEP;
	}

	public void discard() {
		info = null;
		if (animator != null)
			animator.discard();
		if (link != null)
			link.discard();
	}

} // AnimationListEntry

