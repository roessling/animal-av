/*
 * Swap.java
 * Animator that swaps two elements of an array in different styles.
 *
 * Created on 8. August 2005, 19:46
 *
 * @author Michael Schmitt
 * @version 0.5.5b
 * @date 2006-02-24
 */

package animal.animator;

import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;

import translator.AnimalTranslator;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTOpenEllipseSegment;
import animal.graphics.PTPoint;
import animal.graphics.PTPolyline;
import animal.graphics.meta.PTArray;
import animal.main.Animation;
import animal.main.AnimationState;
import animal.misc.MessageDisplay;
import animal.misc.XProperties;

public class Swap extends TimedAnimator implements
		GraphicObjectSpecificAnimation {

	// =================================================================
	// CONSTANTS
	// =================================================================

	/**
	 * The name of this animator
	 */
	public static final String TYPE_LABEL = "Swap";

	/**
	 * This constant contains the UID for serialization, <em>do not edit</em>!
	 */
//	private static final long serialVersionUID = 6013542892925447747L;

	/**
	 * Translate the type type of the swap path depending on the type chosen in
	 * the swap editor
	 */
	public static final int POLY = 0;

	public static final int ARC = 1;

	// =================================================================
	// TRANSIENTS
	// =================================================================

	/**
	 * Store the IDs of the animated objects (PTString- or PTIntArrays)
	 */
	private int[] animatedObjectNums;

	/**
	 * Which PTGraphicObject shall be used when animating the swap? Currently
	 * PTPolyline and PTArc are available, which correspond to '0' and '1'.
	 */
	private int animationType = POLY;

	/**
	 * The paths along which the moves are executed when choosing polyline as move
	 * path
	 */
	private PTPolyline[] p1, p2, p3;

	/**
	 * When arc is chosen as move path these variables will be used Two arrays are
	 * enough, because the 'inner' cell translation is better realized using
	 * polyline p3 as declared above.
	 */
	private PTOpenEllipseSegment[] a1, a2;

	/**
	 * Store the IDs of the used array cells (and move paths) to restore them when
	 * this animation is reloaded from a file
	 */
	private int[] usedIDs;

	/**
	 * The index of the first (lower) cell to be swapped
	 */
	private int first = -1;

	/**
	 * The index of the second (higher) cell to be swapped
	 */
	private int second = -1;

	/**
	 * The moves that are needed to animate the related array componenents while
	 * the swap is executed.
	 */
	private Move[] upperPath, lowerPath, translationPath;

	// =================================================================
	// CONSTRUCTORS
	// =================================================================
	/**
	 * Public(empty) constructor required for serialization.
	 */
	public Swap() { // required for serialization
	}

	/**
	 * Construct a new Swap animator for a given step and the given object ID.
	 * 
	 * @param step
	 *          the step in which the animator is placed
	 * @param objectNum
	 *          the number of the object used in the animator
	 * @param totalTimeOrTicks
	 *          the duration for this effect
	 * @param firstIndex
	 *          the index of the first cell that shall be swapped
	 * @param secondIndex
	 *          the index of the second cell that shall be swapped
	 */
	public Swap(int step, int objectNum, int totalTimeOrTicks, 
			int firstIndex,int secondIndex) {
		this(step, new int[] { objectNum }, "swap", totalTimeOrTicks, 
				firstIndex, secondIndex);
	}

	/**
	 * Construct a new Swap animator for a given step and the given object IDs.
	 * 
	 * @param firstIndex
	 *          the index of the first cell that shall be swapped
	 * @param secondIndex
	 *          the index of the second cell that shall be swapped
	 * @param step
	 *          the step in which the animator is placed
	 * @param objectNums
	 *          the numbers of the objects used in the animator
	 * @param totalTimeOrTicks
	 *          the duration for this effect
	 */
	public Swap(int step, int[] objectNums, int totalTimeOrTicks, int firstIndex,
			int secondIndex) {
		this(step, objectNums, "swap", totalTimeOrTicks, firstIndex, secondIndex);
	}

	/**
	 * Construct a new Swap animator for a given step and the given object ID.
	 * 
	 * @param step
	 *          the step in which the animator is placed
	 * @param objectNum
	 *          the number of the object used in the animator
	 * @param method
	 *          the name of the method used
	 * @param totalTimeOrTicks
	 *          the duration for this effect
	 * @param firstIndex
	 *          the index of the first cell that shall be swapped
	 * @param secondIndex
	 *          the index of the second cell that shall be swapped
	 */
	public Swap(int step, int objectNum, String method, int totalTimeOrTicks,
			int firstIndex, int secondIndex) {
		this(step, new int[] { objectNum }, method, totalTimeOrTicks, 
				firstIndex, secondIndex);
	}

	/**
	 * Construct a new Swap animator for a given step and the given object IDs.
	 * 
	 * @param step
	 *          the step in which the animator is placed
	 * @param objectNums
	 *          the numbers of the objects used in the animator
	 * @param method
	 *          the name of the method used
	 * @param totalTimeOrTicks
	 *          the duration for this effect
	 * @param firstIndex
	 *          the index of the first cell that shall be swapped
	 * @param secondIndex
	 *          the index of the second cell that shall be swapped
	 */
	public Swap(int step, int[] objectNums, String method, int totalTimeOrTicks,
			 int firstIndex, 
			 int secondIndex) {
		super(step, objectNums, totalTimeOrTicks, method);
		setObjects(objectNums);
		setSwapElements(firstIndex,secondIndex);
	}

	/**
	 * Construct a new Swap animator using XProperties.
	 * 
	 * @param props
	 *          the properties file that is used
	 */
	public Swap(XProperties props) {
		setProperties(props);
	}

	// =================================================================
	// INITIALIZATION
	// =================================================================

	/**
	 * prepares the Animator for use by the AnimationState and initializes it. If
	 * any actions have to be done <em>before</em>
	 * <code>action</code> is
	 * called, it has to be done here.
	 * 
	 * @param animationState
	 *          the animation state in which the animator is placed
	 * @param time
	 *          the duration of this animator in ms
	 * @param ticks
	 *          the duration of this animator in ticks
	 * @see Scale#init(AnimationState, long, double)
	 */
	public void init(AnimationState animationState, long time, double ticks) {
		super.init(animationState, time, ticks);
		startTimeOrTicks = (isUnitIsTicks() ? Math.round(ticks) : time);

		if ((animatedObjectNums != null) && (animatedObjectNums.length > 0)) {
			for (int num = 0; num < animatedObjectNums.length; num++) {

				Rectangle bBox1 = new Rectangle(0, 0, 0, 0);
				Rectangle bBox2 = new Rectangle(0, 0, 0, 0);

				// is this necessary??? Maybe replace this...
				// PTArray ao = (PTArray) animationState.getCloneByNum
				// (animatedObjectNums [num]);

				PTGraphicObject ao = animationState
						.getCloneByNum(animatedObjectNums[num]);
				if (ao instanceof PTArray) {
					bBox1 = ((PTArray) ao).getBoundingBox(first);
					bBox2 = ((PTArray) ao).getBoundingBox(second);
				}

				switch (animationType) {
				case POLY:
					// Update paths from first to second cell
					p1[num].setNode(0, new PTPoint(bBox1.x + bBox1.width, bBox1.y));
					p1[num].setNode(1, new PTPoint(bBox1.x + bBox1.width, bBox1.y
							+ bBox1.height + 5));
					p1[num].setNode(2, new PTPoint(bBox2.x + bBox2.width, bBox2.y
							+ bBox2.height + 5));
					p1[num].setNode(3, new PTPoint(bBox2.x + bBox2.width, bBox2.y));

					// Update paths from second to first cell
					p2[num].setNode(0, new PTPoint(bBox2.x, bBox2.y + bBox2.height));
					p2[num].setNode(1, new PTPoint(bBox2.x, bBox2.y - 5));
					p2[num].setNode(2, new PTPoint(bBox1.x, bBox1.y - 5));
					p2[num].setNode(3, new PTPoint(bBox1.x, bBox1.y + bBox1.height));
					break;
				case ARC:
					// Update radius for arc from first to second cell
					Point radius = a1[num].getRadius();
					int yRadius = radius.y;
					a1[num].setLocation(new Point(bBox1.x + bBox1.width, bBox1.y
							+ bBox1.height - yRadius));
					a1[num].setRadius(new Point((bBox2.x - bBox1.x + bBox2.width - bBox1.width) / 2, yRadius));
//							.setXRadius((bBox2.x - bBox1.x + bBox2.width - bBox1.width) / 2);

					radius = a2[num].getRadius();
					yRadius = radius.y;
					// Update radius for arc from second to first cell
					a2[num].setLocation(new Point(bBox1.x, bBox1.y - yRadius));
//							.setLocation(new Point(bBox1.x, bBox1.y - a2[num].getYRadius()));
					a2[num].setRadius(new Point((bBox2.x - bBox1.x) / 2, yRadius));
					break;
				}
				// Update move-path for cells between those that are swapped
				p3[num].setNode(0, new PTPoint(bBox1.x + bBox1.width, bBox1.y));
				p3[num].setNode(1, new PTPoint(bBox1.x + bBox2.width, bBox1.y));

				upperPath[num].init(animationState, time, ticks);
				lowerPath[num].init(animationState, time, ticks);
				translationPath[num].init(animationState, time, ticks);
			}
		}
	}

	// =================================================================
	// ATTRIBUTE GET/SET
	// =================================================================

	/**
	 * Restore the IDs of the PTText objects that were inserted into the animation
	 * to correctly execute the swap. This has to be done because otherwise
	 * objects in the animation might be overwritten by the swapImporter.
	 * 
	 * @param ID
	 *          the IDs of the first and second swap cells of each animated array,
	 *          arranged alternately, e.g. {first1, second1, first2, second2, ...}
	 * 
	 * !!! THIS METHOD IS NOT FOR PUBLIC USE, ONLY REQUIRED BY THE SwapImporter!!!
	 */
	public void restoreIDs(int[] ID) {
		usedIDs = ID;
		// the entrys set in usedIDs will be assigned on runtimee
		// when <code>createMoves ()</code> is executed, not
		// immediately when they've been stored by the <code>SwapImporter</code>.
	}

	public int[] exportIDs() {
		int al = animatedObjectNums.length;

		// for each animated object store:
		// - the indices of the first and second swap cell
		// - the index of the upper path used (polyline/arc)
		// - the index of the lower path used (polyline/arc)
		// - the index of the translation path used (polyline)
		int[] IDs = new int[5 * al];

		// primary export the numbers of the animated arrays, because they also
		// will also be restored first, when an animation is read from a file
		for (int i = 0; i < al; i++) {
			PTGraphicObject ao = Animation.get().getGraphicObject(
					animatedObjectNums[i]);
			if (ao instanceof PTArray) {
				IDs[2 * i] = ((PTArray) ao).getEntry(first).getNum(false);
				IDs[2 * i + 1] = ((PTArray) ao).getEntry(second).getNum(false);
			}
		}

		// now store the IDs of the used polylines or arcs
		switch (animationType) {
		case POLY:
			for (int i = 0; i < p1.length; i++) {
				IDs[2 * al + 3 * i] = p1[i].getNum(false);
				IDs[2 * al + 3 * i + 1] = p2[i].getNum(false);
				IDs[2 * al + 3 * i + 2] = p3[i].getNum(false);
			}
			break;
		case ARC:
			for (int i = 0; i < a1.length; i++) {
				IDs[2 * al + 3 * i] = a1[i].getNum(false);
				IDs[2 * al + 3 * i + 1] = a2[i].getNum(false);
				IDs[2 * al + 3 * i + 2] = p3[i].getNum(false);
			}
			break;
		}
		return IDs;
	}

	/**
	 * Set the indices of the cells that shall be swapped
	 * 
	 * @param a
	 *          the index of the first cell to be swapped
	 * @param b
	 *          the index of the first cell to be swapped
	 */
	public void setSwapElements(int a, int b) {
	  int theA = a, theB = b;
		if ((theA != theB) && (theA >= 0) && (theB >= 0)) {
			if (b < theA) {
				int tmp = theA;
				theA = theB;
				theB = tmp;
			}
			if ((animatedObjectNums == null) || (animatedObjectNums.length == 0)) {
				MessageDisplay.errorMsg(AnimalTranslator.translateMessage("noSwapObjectsSet"),
						MessageDisplay.RUN_ERROR);
			} else {
				for (int num = 0; num < animatedObjectNums.length; num++) {
					PTGraphicObject ao = Animation.get().getGraphicObject(
							animatedObjectNums[num]);
					if (ao instanceof PTArray) {
						modifyAnimatedObjects((PTArray) ao, first, theA);
						modifyAnimatedObjects((PTArray) ao, second, theB);
					}
				}
			}
			first = theA;
			second = theB;
			createMoves();
		}
	}

	/**
	 * Modify the graphic objects that are needed by the current swap animator. If
	 * the choice of the cells to be swapped has changed, the old graphic objects
	 * in the current animation are deleted and the new necessary objects are
	 * inserted into the current animation.
	 * 
	 * @param go the related PTArray
	 * @param old the index of the old swap cell
	 * @param now the updated index of the swap cell
	 */
	private void modifyAnimatedObjects(PTArray go, int old, int now) {
		if (usedIDs != null && usedIDs.length > 0 && now >= 0) {
			// Restore the ID of the swap cells to avoid
			// overwriting of graphic objects in the animation!
			go.setNum(now, usedIDs[0]);
			deleteUsedID();
		}
		if (old < 0) {
			if (now >= 0) {
				Animation.get().insertGraphicObject(go.getEntry(now));
			}
		} else {
			if (old != now) {
				// must be checked if a a new PTStringArray was selected
				// that has less entries than the array(s) that were
				// selected before to prevent access of a null pointer
				if (go.getEntry(old) != null) {
					Animation.get().deleteGraphicObject(go.getEntry(old).getNum(false));
				}
				if (now >= 0) {
					Animation.get().insertGraphicObject(go.getEntry(now));
				}
			}
		}
	}

	private void deleteUsedID() {
		if (usedIDs != null && usedIDs.length > 0) {
			int[] temp = new int[usedIDs.length - 1];
			for (int i = 0; i < temp.length; i++) {
				temp[i] = usedIDs[i + 1];
			}
			usedIDs = temp;
		} else if (usedIDs != null) {
			usedIDs = null;
		}
	}

	/**
	 * Returns the elements chosen for the swap
	 * 
	 * @return [first index, second index]
	 */
	public int[] getSwapElements() {
		return new int[] { first, second };
	}

	/**
	 * Returns the name of this animator, used for saving.
	 * 
	 * @return the name of the animator.
	 */
	public String getAnimatorName() {
		return "Swap";
	}

	/**
	 * Store the number of the currently animated object. This method is only
	 * called if the objectSB in <code>Swapeditor</code> is configured for
	 * single selection.
	 * 
	 * @param obj
	 *          the object number to set
	 */
	public void setObject(int obj) {
		setObjects(new int[] { obj });
	}

	/**
	 * Store the number of the currently animated objects. This is the default
	 * method that is called if the objectSB in <code>SwapEditor</code> is
	 * configured for multiple selection.
	 * 
	 * @param obj
	 *          the object numbers to set
	 */
	public void setObjects(int[] obj) {
		if (animatedObjectNums != null && animatedObjectNums.length > 0
				&& animatedObjectNums != obj) {
			for (int num = 0; num < animatedObjectNums.length; num++) {
				PTGraphicObject ao = Animation.get().getGraphicObject(
						animatedObjectNums[num]);
				if (ao instanceof PTArray) {
					Animation.get().deleteGraphicObject(
							((PTArray) ao).getEntry(first).getNum(false));
					Animation.get().deleteGraphicObject(
							((PTArray) ao).getEntry(second).getNum(false));
				}
			}
			animatedObjectNums = obj;
			if (animatedObjectNums != null && animatedObjectNums.length > 0) {
				for (int num = 0; num < animatedObjectNums.length; num++) {
					PTGraphicObject ao = Animation.get().getGraphicObject(
							animatedObjectNums[num]);
					if (ao instanceof PTArray) {
						if (((PTArray) ao).getEntry(first) != null) {
							Animation.get().insertGraphicObject(
									((PTArray) ao).getEntry(first));
						}
						if (((PTArray) ao).getEntry(second) != null) {
							Animation.get().insertGraphicObject(
									((PTArray) ao).getEntry(second));
						}
					}
				}
			}
		} else {
			animatedObjectNums = obj;
			if (animatedObjectNums != null && animatedObjectNums.length != 0) {
				for (int num = 0; num < animatedObjectNums.length; num++) {
					PTGraphicObject ao = Animation.get().getGraphicObject(
							animatedObjectNums[num]);
					if (ao instanceof PTArray) {
						if (((PTArray) ao).getEntry(first) != null) {
							Animation.get().insertGraphicObject(
									((PTArray) ao).getEntry(first));
						}
						if (((PTArray) ao).getEntry(second) != null) {
							Animation.get().insertGraphicObject(
									((PTArray) ao).getEntry(second));
						}
					}
				}
			}
		}
	}

	/**
	 * Create the move paths and the initial move animations that are required
	 * while the swap is executed.
	 */
	public void createMoves() {
		if ((animatedObjectNums != null) && (animatedObjectNums.length != 0)) {
			Rectangle bBox1, bBox2;

			switch (animationType) {
			case POLY:
				// update the polyline arrays
				if ((p1 == null) || (p1.length != animatedObjectNums.length)) {
					if (p1 != null) {
						for (int num = 0; num < p1.length; num++) {
							if (Animation.get().getGraphicObjects().contains(p1[num]))
								Animation.get().deleteGraphicObject(p1[num].getNum(false));
							if (Animation.get().getGraphicObjects().contains(p2[num]))
								Animation.get().deleteGraphicObject(p2[num].getNum(false));
							if (Animation.get().getGraphicObjects().contains(p3[num]))
								Animation.get().deleteGraphicObject(p3[num].getNum(false));
						}
					}
					p1 = new PTPolyline[animatedObjectNums.length];
					p2 = new PTPolyline[animatedObjectNums.length];
					p3 = new PTPolyline[animatedObjectNums.length];
					upperPath = new Move[animatedObjectNums.length];
					lowerPath = new Move[animatedObjectNums.length];
					translationPath = new Move[animatedObjectNums.length];
				}
				break;
			case ARC:
				// update the arc arrays otherwise
				if ((a1 == null) || (a1.length != animatedObjectNums.length)) {
					if (a1 != null) {
						for (int num = 0; num < a1.length; num++) {
							if (Animation.get().getGraphicObjects().contains(a1[num]))
								Animation.get().deleteGraphicObject(a1[num].getNum(false));
							if (Animation.get().getGraphicObjects().contains(a2[num]))
								Animation.get().deleteGraphicObject(a2[num].getNum(false));
							if (Animation.get().getGraphicObjects().contains(p3[num]))
								Animation.get().deleteGraphicObject(p3[num].getNum(false));
						}
					}
					a1 = new PTOpenEllipseSegment[animatedObjectNums.length];
					a2 = new PTOpenEllipseSegment[animatedObjectNums.length];
					p3 = new PTPolyline[animatedObjectNums.length];
					upperPath = new Move[animatedObjectNums.length];
					lowerPath = new Move[animatedObjectNums.length];
					translationPath = new Move[animatedObjectNums.length];
				}
				break;
			}

			for (int num = 0; num < animatedObjectNums.length; num++) {
				PTGraphicObject ao = Animation.get().getGraphicObject(
						animatedObjectNums[num]);

				// Store the object numbers for the latter animation
//				int elem1, elem2;
				if (ao instanceof PTArray) {
					bBox1 = ((PTArray) ao).getBoundingBox(first);
					bBox2 = ((PTArray) ao).getBoundingBox(second);
				} else {
					MessageDisplay.errorMsg(AnimalTranslator.translateMessage("wrongTypeForSwap"),
							MessageDisplay.RUN_ERROR);
					return;
				}

				switch (animationType) {
				case POLY:
					// These are the paths, along which the cell entries shall move
					// Paths from first to second cell
					if (p1[num] == null) {
						p1[num] = new PTPolyline(new int[] { 0, 0, 0, 0 }, new int[] { 0,
								0, 0, 0 });
					} else {
						p1[num].setNode(0, new PTPoint(bBox1.x + bBox1.width, bBox1.y));
						p1[num].setNode(1, new PTPoint(bBox1.x + bBox1.width, bBox1.y
								+ bBox1.height + 5));
						p1[num].setNode(2, new PTPoint(bBox2.x + bBox2.width, bBox2.y
								+ bBox2.height + 5));
						p1[num].setNode(3, new PTPoint(bBox2.x + bBox2.width, bBox2.y));
					}
					if (usedIDs != null && usedIDs.length >= 3) {
						p1[num].setNum(usedIDs[0]);
						deleteUsedID();
					} else {
						p1[num].setNum(p1[num].getNum(true));
					}

					// Paths from second to first cell
					if (p2[num] == null) {
						p2[num] = new PTPolyline(new int[] { 0, 0, 0, 0 }, new int[] { 0,
								0, 0, 0 });
					} else {
						p2[num].setNode(0, new PTPoint(bBox2.x, bBox2.y + bBox2.height));
						p2[num].setNode(1, new PTPoint(bBox2.x, bBox2.y - 5));
						p2[num].setNode(2, new PTPoint(bBox1.x, bBox1.y - 5));
						p2[num].setNode(3, new PTPoint(bBox1.x, bBox1.y + bBox1.height));
					}
					if (usedIDs != null && usedIDs.length >= 2) {
						p2[num].setNum(usedIDs[0]);
						deleteUsedID();
					} else {
						p2[num].setNum(p2[num].getNum(true));
					}

					// Move-paths for cells between those that are swapped
					if (p3[num] == null) {
						p3[num] = new PTPolyline(new int[] { 0, 0 }, new int[] { 0, 0 });
					} else {
						p3[num].setNode(0, new PTPoint(bBox1.x + bBox1.width, bBox1.y));
						p3[num].setNode(1, new PTPoint(bBox1.x + bBox2.width, bBox1.y));
					}
					if (usedIDs != null && usedIDs.length >= 1) {
						p3[num].setNum(usedIDs[0]);
						deleteUsedID();
					} else {
						p3[num].setNum(p3[num].getNum(true));
					}

					if (upperPath[num] == null) {
						upperPath[num] = new Move(getStep(), animatedObjectNums[num],
								getDuration(), getMethod(), p2[num].getNum(false));
					} else {
						upperPath[num].setDuration(getDuration());
						upperPath[num].setMethod(getMethod());
						upperPath[num].setMoveBaseNum(p2[num].getNum(false));
					}

					if (lowerPath[num] == null) {
						lowerPath[num] = new Move(getStep(), animatedObjectNums[num],
								getDuration(), getMethod(), p1[num].getNum(false));
					} else {
						lowerPath[num].setDuration(getDuration());
						lowerPath[num].setMethod(getMethod());
						lowerPath[num].setMoveBaseNum(p1[num].getNum(false));
					}

					if (translationPath[num] == null) {
						translationPath[num] = new Move(getStep(), animatedObjectNums[num],
								getDuration(), getMethod(), p3[num].getNum(false));
					} else {
						translationPath[num].setDuration(getDuration());
						translationPath[num].setMethod(getMethod());
						translationPath[num].setMoveBaseNum(p3[num].getNum(false));
					}
					if (!Animation.get().getGraphicObjects().contains(p1[num]))
						Animation.get().insertGraphicObject(p1[num]);
					if (!Animation.get().getGraphicObjects().contains(p2[num]))
						Animation.get().insertGraphicObject(p2[num]);
					if (!Animation.get().getGraphicObjects().contains(p3[num]))
						Animation.get().insertGraphicObject(p3[num]);
					break;
				case ARC:
					// These are the paths, along which the cell entries shall move
					// Paths from first to second cell
					if (a1[num] == null) {
						a1[num] = new PTOpenEllipseSegment();
						a1[num].setObjectSelectable(false);
						a1[num].setStartAngle(180);
						a1[num].setTotalAngle(180);
						a1[num].setBWArrow(false);
//						a1[num].setCircle(false);
						a1[num].setClockwise(false);
//						a1[num].setClosed(false);
//						a1[num].setFilled(false);
						a1[num].setRadius(new Point(0, bBox1.height * 3));
//						System.err.println("a1@createMoves: " +a1[num]);
//						a1[num].setYRadius(bBox1.height * 3);
					} else {
						// !!!
						// This could lead to increasing errors in the position of the cell
						// text
						// when combining several swaps that have a radius that is rounded.
						// Then the final text position might differ 1 pixel from the
						// correct position after each swap!!!
						Point radius = a1[num].getRadius();
						int yRadius = radius.y;
						a1[num].setLocation(new Point(bBox1.x + bBox1.width, bBox1.y
								+ bBox1.height - yRadius));
						a1[num].setRadius((bBox2.x - bBox1.x + bBox2.width - bBox1.width) / 2,
								yRadius);
//						System.err.println("a1+else@createMoves: " +a1[num]);
					}
					if (usedIDs != null && usedIDs.length >= 3) {
						a1[num].setNum(usedIDs[0]);
						deleteUsedID();
					} else {
						a1[num].setNum(a1[num].getNum(true));
					}

					// Paths from second to first cell
					if (a2[num] == null) {
						a2[num] = new PTOpenEllipseSegment();
						a2[num].setObjectSelectable(false);
						a2[num].setStartAngle(0);
						a2[num].setTotalAngle(180);
						a2[num].setBWArrow(false);
//						a2[num].setCircle(false);
						a2[num].setClockwise(false);
//						a2[num].setClosed(false);
//						a2[num].setFilled(false);
//						a2[num].setYRadius(bBox2.height * 3);
						a2[num].setRadius(0, bBox2.height * 3);
//						System.err.println("a2@createMoves: " +a1[num]);
					} else {
						// !!! Might lead to display errors (s.a.)
//						System.err.println("@a2.createMove2: " +a2[num]);
						Point radius = a2[num].getRadius();
						int yRadius = radius.y;
						a2[num].setLocation(new Point(bBox1.x, bBox1.y
								- yRadius));
						a2[num].setRadius((bBox2.x - bBox1.x) / 2, yRadius);
//						System.err.println("@a2.createMove2: " +a2[num]);
					}
					if (usedIDs != null && usedIDs.length >= 2) {
						a2[num].setNum(usedIDs[0]);
						deleteUsedID();
					} else {
						a2[num].setNum(a2[num].getNum(true));
					}

					// Move-paths for cells between those that are swapped
					if (p3[num] == null) {
						p3[num] = new PTPolyline(new int[] { 0, 0 }, new int[] { 0, 0 });
					} else {
						p3[num].setNode(0, new PTPoint(bBox1.x + bBox1.width, bBox1.y));
						p3[num].setNode(1, new PTPoint(bBox1.x + bBox2.width, bBox1.y));
					}
					if (usedIDs != null && usedIDs.length >= 1) {
						p3[num].setNum(usedIDs[0]);
						deleteUsedID();
					} else {
						p3[num].setNum(p3[num].getNum(true));
					}

					if (upperPath[num] == null) {
						upperPath[num] = new Move(getStep(), animatedObjectNums[num],
								getDuration(), getMethod(), a2[num].getNum(false));
					} else {
						upperPath[num].setDuration(getDuration());
						upperPath[num].setMethod(getMethod());
						upperPath[num].setMoveBaseNum(a2[num].getNum(false));
					}

					if (lowerPath[num] == null) {
						lowerPath[num] = new Move(getStep(), animatedObjectNums[num],
								getDuration(), getMethod(), a1[num].getNum(false));
					} else {
						lowerPath[num].setDuration(getDuration());
						lowerPath[num].setMethod(getMethod());
						lowerPath[num].setMoveBaseNum(a1[num].getNum(false));
					}

					if (translationPath[num] == null) {
						translationPath[num] = new Move(getStep(), animatedObjectNums[num],
								getDuration(), getMethod(), p3[num].getNum(false));
					} else {
						translationPath[num].setDuration(getDuration());
						translationPath[num].setMethod(getMethod());
						translationPath[num].setMoveBaseNum(p3[num].getNum(false));
					}
					if (!Animation.get().getGraphicObjects().contains(a1[num]))
						Animation.get().insertGraphicObject(a1[num]);
					if (!Animation.get().getGraphicObjects().contains(a2[num]))
						Animation.get().insertGraphicObject(a2[num]);
					if (!Animation.get().getGraphicObjects().contains(p3[num]))
						Animation.get().insertGraphicObject(p3[num]);
					break;
				}
			}
		}
	}

	/**
	 * Retrieve the file version for this animator. It must be incremented
	 * whenever the save format is changed.
	 * 
	 * Versions include:
	 * 
	 * <ol>
	 * <li>1. original release</li>
	 * <li>2. annimation type 'arc' added</li>
	 * </ol>
	 * 
	 * @return the file version for the animator, needed for import/export
	 */
	public int getFileVersion() {
		return 2;
	}

	/**
	 * Returns the property at a certain time. getProperty <em>must</em> return
	 * a property of the "normal" type (i.e. Move must always return a Point),
	 * even if the object is not completely initialized (then return a dummy!), as
	 * TimedAnimatorEditor relies on receiving a property for querying the
	 * possible methods.
	 * 
	 * @param factor
	 *          a value between 0 and 1, indicating how far this animator has
	 *          got(0: start, 1: end)
	 * @return the object at the given time.
	 * @see animal.editor.animators.TimedAnimatorEditor
	 */
	public Object getProperty(double factor) {
		if ((animatedObjectNums == null) || (animatedObjectNums.length == 0)) {
			return new SwapType[] { new SwapType(-1, -1, (byte) (factor * 100)) };
		} else if ((lowerPath == null) || (upperPath == null)
				|| (translationPath == null)) {
			MessageDisplay.errorMsg(AnimalTranslator.translateMessage("swapNoMovePath"),
					MessageDisplay.RUN_ERROR);
			return new SwapType[] { new SwapType(-1, -1, (byte) (factor * 100)) };
		} else {
			SwapType[] st = new SwapType[animatedObjectNums.length];
			for (int num = 0; num < animatedObjectNums.length; num++) {
				if ((lowerPath[num] == null) || (upperPath[num] == null)
						|| (translationPath[num] == null) || (lowerPath.length == 0)
						|| (upperPath.length == 0) || (translationPath.length == 0)) {
					st[num] = new SwapType(new int[] { -1, -1 }, new Point(0, 0),
							new Point(0, 0), new Point(0, 0), hasFinished());
				} else {
					st[num] = new SwapType(getSwapElements(), ((Point) (lowerPath[num]
							.getProperty(factor))), ((Point) (upperPath[num]
							.getProperty(factor))), ((Point) (translationPath[num]
							.getProperty(factor))), hasFinished());
				}
			}
			return st;
		}
	}

	/**
	 * Returns the temporary objects of this Animator. Temporary object are
	 * required for animation, but are not animated themselves: for example, move
	 * paths. These are passed as an <code>int[]</code> and not as
	 * PTGraphicObject[], as resetting the numbers of the temporary objects in the
	 * Animators doesn't change the objects but only the numbers. The objects are
	 * not changed until the animator is reinitialized.
	 * 
	 * @return an array containing the numerics IDs of the temporary objects;
	 *         <code>null</code> if no emporary objects are used.
	 */
	public int[] getTemporaryObjects() {
		if (p3 == null) {
			return null;
		} 
		int objCounter = p3.length;
		switch (animationType) {
		case POLY:
			objCounter += p1.length + p2.length;
			break;
		case ARC:
			objCounter += a1.length + a2.length;
			break;
		}
		int[] temp = new int[objCounter];
		switch (animationType) {
		case POLY:
			for (int x = 0; x < p1.length; x++) {
				temp[x] = p1[x].getNum(false);
			}
			objCounter = p1.length;
			for (int x = 0; x < p2.length; x++) {
				temp[objCounter + x] = p2[x].getNum(false);
			}
			objCounter += p2.length;
			break;
		case ARC:
			for (int x = 0; x < a1.length; x++) {
				temp[x] = a1[x].getNum(false);
			}
			objCounter = a1.length;
			for (int x = 0; x < a2.length; x++) {
				temp[objCounter + x] = a2[x].getNum(false);
			}
			objCounter += a2.length;
			break;
		}
		for (int x = 0; x < p3.length; x++) {
			temp[objCounter + x] = p3[x].getNum(false);
		}
		return temp;
		
	}

	/**
	 * Return the currenly used animation type. At the moment ARC and POLY are
	 * available.
	 */
	public int getAnimationType() {
		return animationType;
	}

	public void setAnimationType(int type) {
		if (animationType != type) {
			switch (animationType) {
			case POLY:
				if ((p1 != null) && (p1.length > 0)) {
					for (int num = 0; num < p1.length; num++) {
						if (Animation.get().getGraphicObjects().contains(p1[num]))
							Animation.get().deleteGraphicObject(p1[num].getNum(false));
						if (Animation.get().getGraphicObjects().contains(p2[num]))
							Animation.get().deleteGraphicObject(p2[num].getNum(false));
						if (Animation.get().getGraphicObjects().contains(p3[num]))
							Animation.get().deleteGraphicObject(p3[num].getNum(false));
					}
				}
				p1 = null;
				p2 = null;
				p3 = null;
				break;
			case ARC:
				if ((a1 != null) && (a1.length > 0)) {
					for (int num = 0; num < a1.length; num++) {
						if (Animation.get().getGraphicObjects().contains(a1[num]))
							Animation.get().deleteGraphicObject(a1[num].getNum(false));
						if (Animation.get().getGraphicObjects().contains(a2[num]))
							Animation.get().deleteGraphicObject(a2[num].getNum(false));
						if (Animation.get().getGraphicObjects().contains(p3[num]))
							Animation.get().deleteGraphicObject(p3[num].getNum(false));
					}
				}
				a1 = null;
				a2 = null;
				p3 = null;
				break;
			}
		}
		animationType = type;
	}

	/**
	 * Get the type of this animator as "Swap"
	 * 
	 * @return the name of this animator
	 */
	public String getType() {
		return TYPE_LABEL;
	}

	/**
	 * Returns the keywords of Animal's ASCII format this animator handles.
	 * 
	 * @return a String array of the keywords handled by this animator.
	 */
	public String[] handledKeywords() {
		return new String[] { "Swap" };
	}

	// =================================================================
	// I/O
	// =================================================================

	/**
	 * Sets the object to the state it has after a certain time has passed. Can
	 * rely on <code>init</code> begin called before. Should call <code> execute</code>
	 * when finished(e.g. because time is over). If <code> hasFinished()</code>
	 * is true, it should return immediately
	 * 
	 * @param time
	 *          the current time [ms], used for determining what action is to be
	 *          taken
	 * @param ticks
	 *          the current time in ticks, used for determining what action is to
	 *          be taken
	 * @see animal.animator.Animator#hasFinished()
	 */
	public void action(long time, double ticks) {
		double factor;
		if (hasFinished())
			return;
		double elapsed = (isUnitIsTicks() ? ticks : time) - startTimeOrTicks
				- getOffset();
		factor = elapsed / getDuration();
		if ((getDuration() == 0 && elapsed >= 0) || (factor >= 1)) {
			execute();
		} else if (factor < 0) {
			return;
		} else {
			Object newProperty = getProperty(factor);

			for (int a = 0; a < objects.length; a++) {
				if (objects[a] != null) {
					objects[a].propertyChange(new PropertyChangeEvent(this, getMethod(),
							((SwapType[]) oldProperty)[a], ((SwapType[]) newProperty)[a]));
				}
			}
			oldProperty = newProperty;
		}
	}

	/**
	 * Changes the state of the GraphicObject to its final state after completely
	 * executing the Animator. Must be overwritten and called by subclasses. The
	 * object must have been initialized before but some actions may have been
	 * done meanwhile. Should be called at the end of <code> action</code>
	 * 
	 * @see #action(long, double)
	 */
	public void execute() {
		setFinished(true);
		if (oldProperty == null)
			oldProperty = getProperty(0);
		Object newProperty = getProperty(1);
		if (objects != null && objects.length > 0) {
			for (int a = 0; a < objects.length; a++)
				if (objects[a] != null)
					objects[a].propertyChange(new PropertyChangeEvent(this, getMethod(),
							((SwapType[]) oldProperty)[a], ((SwapType[]) newProperty)[a]));
		} else {
			MessageDisplay.errorMsg(AnimalTranslator.translateMessage("noObjectsSetException"),
					new Object[] {String.valueOf(getStep()), 
						getAnimatorName() + " -- " + toString() });
		}
	}

	/**
	 * Reset the attributes for this animator for a "clean memory" state.
	 */
	public void discard() {
		for (int num = 0; num < animatedObjectNums.length; num++) {
			switch (animationType) {
			case POLY:
				p1[num].discard();
				p2[num].discard();
				break;
			case ARC:
				a1[num].discard();
				a2[num].discard();
				break;
			}
			p3[num].discard();
			upperPath[num].discard();
			lowerPath[num].discard();
			translationPath[num].discard();
		}
		a1 = null;
		a2 = null;
		p1 = null;
		p2 = null;
		p3 = null;
		upperPath = null;
		lowerPath = null;
		translationPath = null;
		usedIDs = null;
		super.discard();
	}

	/**
	 * Return the Animator's description to be displayed in the AnimationOverview.
	 * 
	 * @return the String representation of this object.
	 */
	public String toString() {
		if ((first < 0) || (second < 0)) {
			return "Invalid parameters; swap cannot be executed";
		} 
		StringBuilder sb = new StringBuilder(256);
		sb.append("Swap cells ").append(first).append(" and ");
		sb.append(second).append(" of ").append(super.toString());
		sb.append(" using PT");
		sb.append((animationType == POLY ? "Polyline " : "Arc "));
		return sb.toString();
	}

	/**
	 * Get the graphic object types on which the animator works.
	 * 
	 * @return an array of the supported types
	 */
	public String[] getSupportedTypes() {
		return new String[] { animal.graphics.PTStringArray.STRING_ARRAY_TYPE,
				animal.graphics.PTIntArray.INT_ARRAY_TYPE };
	}
}