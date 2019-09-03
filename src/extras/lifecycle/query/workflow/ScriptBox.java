package extras.lifecycle.query.workflow;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

import extras.lifecycle.query.workflow.AbstractBox;
import extras.lifecycle.query.workflow.AssignBox;
import extras.lifecycle.query.workflow.Box;
import extras.lifecycle.query.workflow.ConstantValue;
import extras.lifecycle.query.workflow.FunctionBox;

import extras.lifecycle.query.Knowledge;
import extras.lifecycle.query.function.Animate;
import extras.lifecycle.query.function.Check;
import extras.lifecycle.query.function.Comment;
import extras.lifecycle.query.function.CommentIf;
import extras.lifecycle.query.function.Debug;
import extras.lifecycle.query.function.Dump;
import extras.lifecycle.query.function.Max;
import extras.lifecycle.query.function.NumberOfCheckpoints;
import extras.lifecycle.query.function.Ok;

/**
 * This class depicts the root component in the workflow. It (may) has a row of
 * boxes, which should be evaluated sequentially.
 * 
 * @author Mihail Mihaylov
 * 
 */
@XmlRootElement(name = "ScriptBox")
public class ScriptBox extends AbstractBox {

	private static final String NEWLINE = "\n";
	/**
	 * A pointer to the last box in the row of boxes, which should be execeted
	 * sequentially.
	 */
	private ArrayList<AbstractBox> boxes;

	/**
	 * 
	 */
	public ScriptBox() {
		super();

		// At the beginning the ScriptBox itself is the last box
		boxes = new ArrayList<AbstractBox>();
	}

	@Override
	public Box evaluate(Knowledge knowledge) {
		return getNext();
	}

	/**
	 * Appends a box the sequence.
	 * 
	 * @param box
	 */
	public void append(AbstractBox box) {
		// We should not modify the next box of the assign box,
		// because it leads to misleading and causes cycle by
		// XML Marshaling
		
		// No, we should modify them, because otherwise
		// the resulting workflow is not correct
		if (boxes.size() > 0)
			boxes.get(boxes.size() - 1).setNext(box);

		boxes.add(box);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("Script [");
		result.append(NEWLINE);

		for (Box box : boxes) {
			result.append(box);
			result.append(NEWLINE);
		}

		result.append("]");
		return result.toString();
	}

	/**
	 * @return the boxes
	 */
	// @XmlElementWrapper(name = "boxes")
	// @XmlElement(name = "box")
	@XmlElementRefs({ @XmlElementRef(type = AssignBox.class),
			@XmlElementRef(type = ConstantValue.class),
			@XmlElementRef(type = NumberOfCheckpoints.class),
			@XmlElementRef(type = FunctionBox.class), 
			@XmlElementRef(type = Check.class), 
			@XmlElementRef(type = Dump.class),
			@XmlElementRef(type = Debug.class), 
			@XmlElementRef(type = Max.class), 
			@XmlElementRef(type = Ok.class), 
			@XmlElementRef(type = Animate.class),
			@XmlElementRef(type = Comment.class),
			@XmlElementRef(type = CommentIf.class)
	})
	public ArrayList<AbstractBox> getBoxes() {
		return boxes;
	}

	/**
	 * @param boxes
	 *            the boxes to set
	 */
	public void setBoxes(ArrayList<AbstractBox> boxes) {
		this.boxes = boxes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see extras.lifecycle.query.workflow.AbstractBox#getNext()
	 */
	@Override
	public AbstractBox getNext() {
		if ((boxes == null) || (boxes.isEmpty()))
			return null;
		else
			return boxes.get(0);
	}

}
