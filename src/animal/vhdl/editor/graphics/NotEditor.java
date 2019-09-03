package animal.vhdl.editor.graphics;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import translator.AnimalTranslator;
import translator.TranslatableGUIElement;
import animal.editor.Editor;
import animal.editor.graphics.meta.FillablePrimitiveEditor;
import animal.graphics.PTGraphicObject;
import animal.gui.DrawCanvas;
import animal.main.Animation;
import animal.misc.EditPoint;
import animal.misc.EditableObject;
import animal.misc.MSMath;
import animal.misc.XProperties;
import animal.vhdl.graphics.PTNot;
import animal.vhdl.logic.LogicNot;
import animal.vhdl.logic.LogicVHDL;

/**
 * Editor for a and gate
 * 
 * @author Zheng Lu,Peiqian Li
 * @version 2 2009-4-21
 */
public class NotEditor extends FillablePrimitiveEditor implements ItemListener,
		ActionListener, PropertyChangeListener, KeyListener, MouseListener,
		WindowListener {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -2413776784253970663L;
	private JTextField inText;
	private JTextField outText;
	private JTextField outValue;
	protected ArrayList<JComboBox> inputValues;
	private JComboBox inputCountCombo;
	static boolean fr;

	public NotEditor() {
		super();

	}

	protected void buildGUI() {
		TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();

		// input & output box
		Box IOBox = new Box(BoxLayout.PAGE_AXIS);

		// input box
		Box inputBox = new Box(BoxLayout.LINE_AXIS);
		JLabel inputNameLabel = new JLabel(" input name:");

		inText = new JTextField("In", 5);
		inText.addKeyListener(this);
		JLabel inputValueLabel = new JLabel(" value");
		Character[] inputValue = LogicVHDL.LOGIC_VALUES;
		inputCountCombo = new JComboBox(inputValue);
		inputCountCombo.addItemListener(this);
		inputBox.add(inputNameLabel);
		inputBox.add(inText);
		inputBox.add(inputValueLabel);
		inputBox.add(inputCountCombo);
		IOBox.add(inputBox);

		// output box
		Box OutBox = new Box(BoxLayout.LINE_AXIS);
		outText = new JTextField("Out", 5);
		outText.addKeyListener(this);
		outValue = new JTextField(" ", 1);
		OutBox.add(new JLabel("output name:"));
		OutBox.add(outText);
		IOBox.add(OutBox);

		addBox(IOBox);

		// create shared entries (color, fill color)
		Box contentBox = createCommonElements(generator);

		// create type-specific stuff
		filledCB = generator.generateJCheckBox("GenericEditor.filled", null,
				this);
		filledCB.addItemListener(this);
		contentBox.add(filledCB);

		// finish the boxes
		finishBoxes();
	}

	public int pointsNeeded() {
		return 2;
	}

	public boolean nextPoint(int num, Point p) {
		PTNot not = (PTNot) getCurrentObject();
		if (num == 1)
			not.setStartNode(p.x, p.y);
		if (num == 2) {
			Point firstNode = not.getStartNode();
			not.setWidth(p.x - firstNode.x);
			not.setHeight(p.y - firstNode.y);
		}
		return true;
	} // nextPoint;

	public int getMinDist(PTGraphicObject go, Point p) {
		PTNot pg = (PTNot) go;
		Point a = new Point(pg.getStartNode().x, pg.getStartNode().y);
		Rectangle boundingBox = pg.getBoundingBox();
		// if point is inside, there is not much of distance ;-)
		if (boundingBox.contains(p.x, p.y))
			return 0;

		// (ULC, URC)
		Point b = new Point(a.x + pg.getWidth(), a.y);
		int minDist = Integer.MAX_VALUE;
		int newDist = MSMath.dist(p, a, b);
		if (newDist < minDist)
			minDist = newDist;

		// (URC, LRC)
		b.translate(0, pg.getHeight());
		newDist = MSMath.dist(p, a, b);
		if (newDist < minDist)
			minDist = newDist;

		// (LRC, LLC)
		a.translate(pg.getWidth(), pg.getHeight());
		newDist = MSMath.dist(p, a, b);
		if (newDist < minDist)
			minDist = newDist;

		newDist = MSMath.dist(p, a, pg.getStartNode());
		if (newDist < minDist)
			minDist = newDist;
		return minDist;
	}

	public EditPoint[] getEditPoints(PTGraphicObject go) {
		PTNot pg = (PTNot) go;
		// int pSize = 2;
		int width = pg.getWidth();
		int height = pg.getHeight();
		// int i;
		// add change points(nodes)
		EditPoint[] result = new EditPoint[5];
		Point helper = pg.getStartNode();
		// result[5] = new EditPoint(1, helper);
		helper = new Point(helper.x + width, helper.y + height);
		result[0] = new EditPoint(2, helper);

		int x = pg.getStartNode().x;
		int y = pg.getStartNode().y;
		result[1] = new EditPoint(-2, new Point(x + (width / 2), y));
		result[2] = new EditPoint(-3, new Point(x + width, y + (height / 2)));
		result[3] = new EditPoint(-4, new Point(x + (width / 2), y + height));
		result[4] = new EditPoint(-5, new Point(x, y + (height / 2)));

		return result;
	} // getEditPoints

	public void setProperties(XProperties props) {
		colorChooser.setColor(props.getColorProperty(PTNot.NOT_TYPE_LABEL
				+ ".color", Color.black));
		depthBox.setSelectedItem(props.getProperty(PTNot.NOT_TYPE_LABEL + ".depth",
				"16"));
		fillColorChooser.setColor(props.getColorProperty(PTNot.NOT_TYPE_LABEL
				+ ".color", Color.black));
		filledCB.setSelected(props
				.getBoolProperty(PTNot.NOT_TYPE_LABEL + ".filled"));
	}

	public void getProperties(XProperties props) {
		props.put(PTNot.NOT_TYPE_LABEL + ".color", colorChooser.getColor());
		props.put(PTNot.NOT_TYPE_LABEL + ".depth", depthBox.getSelectedItem());
		props.put(PTNot.NOT_TYPE_LABEL + ".fillColor", fillColorChooser.getColor());
		props.put(PTNot.NOT_TYPE_LABEL + ".filled", filledCB.isSelected());
	}

	/**
	 * enable or disable some CheckBoxes according to whether the polyline is
	 * closed or not
	 */
	public void itemStateChanged(ItemEvent e) {
		PTNot p = (PTNot) getCurrentObject();

		if (e.getSource() == filledCB) {
			if (p != null)
				p.setFilled(filledCB.isSelected());

		}
		//
		else if (e.getSource() == inputCountCombo) {
			if (p != null && inputCountCombo.getSelectedIndex() != -1) {

				p.getInputPins().get(0).setPinValue(
						(Character) inputCountCombo.getSelectedItem());

				char outputValue = getOutputValueOf(p);
				p.getOutputPins().get(0).setPinValue(outputValue);
				outValue.setText(outputValue + "");
			}
		}

		Animation.get().doChange();
		repaintNow();
	} // itemStateChanged

	public EditableObject createObject() {
		PTNot pg = new PTNot();
		storeAttributesInto(pg);
		return pg;
	}

	protected void storeAttributesInto(EditableObject eo) {
		super.storeAttributesInto(eo);
		PTNot p = (PTNot) eo;
		p.setColor(colorChooser.getColor());
		p.setFilled(filledCB.isSelected());
		p.setFillColor(fillColorChooser.getColor());
	}

	protected void extractAttributesFrom(EditableObject eo) {
		super.extractAttributesFrom(eo);
		PTNot p = (PTNot) eo;
		colorChooser.setColor(p.getColor());
		filledCB.setEnabled(true);
		filledCB.setSelected(p.isFilled());
		fillColorChooser.setColor(p.getFillColor());
	}

	public Editor getSecondaryEditor(EditableObject go) {

		NotEditor result = new NotEditor();
		// important! result must be of type AndEditor (or cast)
		// and the parameter passed must be of type PTAnd.
		// Otherwise, not all attributes are copied!
		result.extractAttributesFrom(go);
		return result;
	}

	public String getStatusLineMsg() {
		return AnimalTranslator.translateMessage("NotEditor.statusLine",
				new Object[] { DrawCanvas.translateDrawButton(),
						DrawCanvas.translateFinishButton(),
						DrawCanvas.translateCancelButton() });
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		PTNot p = (PTNot) getCurrentObject();

		if (p != null) {
			if (Animation.get() != null) {
				Animation.get().doChange();
			}
			repaintNow();
		}
	}

	public void propertyChange(PropertyChangeEvent event) {
		PTNot poly = (PTNot) getCurrentObject();
		String eventName = event.getPropertyName();
		if ("color".equals(eventName))
			poly.setColor((Color) event.getNewValue());
		else if ("fillColor".equals(eventName))
			poly.setFillColor((Color) event.getNewValue());
		if (!event.getOldValue().equals(event.getNewValue())) {
			repaintNow();
			if (Animation.get() != null)
				Animation.get().doChange();
		}
	}

	public String getBasicType() {
		return PTNot.NOT_TYPE_LABEL;
	}

	public void keyPressed(KeyEvent arg0) {
    // nothing to be done here
	}

	public void keyReleased(KeyEvent arg0) {
		PTNot p = (PTNot) getCurrentObject();
		if (p != null) {
			p.getInputPins().get(0).setPinName(inText.getText());
			p.getOutputPins().get(0).setPinName(outText.getText());
		}
		repaintNow();

	}

	private char getOutputValueOf(PTNot ptNot) {
		char[] inputList = new char[1];
		inputList[0] = ptNot.getInputPins().get(0).getPinValue();
		return getOutputValueOf(inputList);
	}

	private char getOutputValueOf(char[] inputList) {
		LogicNot l = new LogicNot(inputList);
		return l.getLogicResult();
	}

	public void keyTyped(KeyEvent arg0) {
    // nothing to be done here
	}

	public void mouseEntered(MouseEvent arg0) {
    // nothing to be done here
	}

	public void mouseExited(MouseEvent arg0) {
    // nothing to be done here
	}

	public void mousePressed(MouseEvent arg0) {
    // nothing to be done here
	}

	public void mouseReleased(MouseEvent arg0) {
    // nothing to be done here
	}

	public void windowActivated(WindowEvent arg0) {
    // nothing to be done here
	}

	public void windowClosed(WindowEvent arg0) {
    // nothing to be done here
	}

	public void windowClosing(WindowEvent arg0) {
		fr = false;
	}

	public void windowDeactivated(WindowEvent arg0) {
    // nothing to be done here
	}

	public void windowDeiconified(WindowEvent arg0) {
    // nothing to be done here
	}

	public void windowIconified(WindowEvent arg0) {
    // nothing to be done here
	}

	public void windowOpened(WindowEvent arg0) {
    // nothing to be done here
	}

	public void mouseClicked(MouseEvent arg0) {
    // nothing to be done here
	}
}
