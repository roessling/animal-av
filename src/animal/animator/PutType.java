/*
 * PutType.java
 * Stores the current properties for Put.java
 *
 * Created on 29. November 2005, 20:56
 *
 * @author Michael Schmitt
 * @version 0.1.5
 * @date 2006-01-04
 */

package animal.animator;

import java.awt.Point;

import animal.graphics.PTText;

public class PutType {

	public int idx;

	public PTText newContent;

	public double part;

	public boolean hasFinished;

	public Point positionOld, positionNew;

	public PutType() {
		// do nothing; only used for serialization
	}

	/**
	 * Creates a new instance of PutType
	 */
	public PutType(int i, PTText newVal, double partDone, boolean done) {
		idx = i;
		newContent = newVal;
		part = partDone;
		hasFinished = done;
	}

	public PutType(int i, Point pOld, PTText newVal, Point pNew, double partDone,
			boolean done) {
		this(i, newVal, partDone, done);
		positionOld = pOld;
		positionNew = pNew;
	}

}