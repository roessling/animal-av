package animal.vhdl.editor.graphics;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

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
import animal.vhdl.graphics.PTMux;
import animal.vhdl.graphics.PTPin;
import animal.vhdl.logic.LogicVHDL;

/**
 * Editor for a RS flip flop
 * 
 * @author p_li
 */
public class MuxEditor extends FillablePrimitiveEditor implements ItemListener,
		MouseListener, KeyListener ,PropertyChangeListener{

	/**
	 * a JComboBox to choose the amount of input pins
	 */
	private JComboBox inputCombobox;

	/**
	 * the text field for the output pin name
	 */
	private JTextField outText;

	/**
	 * an array list of JTextField contains the text fields for the names of all
	 * input pin
	 */
	private ArrayList<JTextField> inputNames;
	/**
	 * an array list of JComboBox contains the ComboBoxes for the values of all
	 * input pin
	 */
	private ArrayList<JComboBox> inputValues;

	/**
	 * an array list of JTextField contains the text fields for the names of all
	 * control pin
	 */
	private ArrayList<JTextField> controlNames;
	/**
	 * an array list of JComboBox contains the ComboBoxes for the values of all
	 * control pin
	 */
	private ArrayList<JComboBox> controlValues;

	public MuxEditor() {
		super();
	}

	protected void buildGUI() {
		TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();

		// input & output box
		Box IOBox = new Box(BoxLayout.PAGE_AXIS);
		IOBox.setBorder(new TitledBorder(null, "in/output settings",
				TitledBorder.LEADING, TitledBorder.TOP));

		// input box: input amount
		Box inputBox = new Box(BoxLayout.LINE_AXIS);
		JLabel inputAmountLabel = new JLabel("  #input : ");
		inputBox.add(inputAmountLabel);

		inputCombobox = new JComboBox(new Integer[] { 2, 4, 8, 16 });
		inputCombobox.addItemListener(this);
		inputBox.add(inputCombobox);

		JButton inputEditButton = new JButton("Edit");
		inputEditButton.addMouseListener(this);
		inputBox.add(inputEditButton);

		IOBox.add(inputBox);

		// output box
		Box OutBox = new Box(BoxLayout.LINE_AXIS);
		OutBox.add(new JLabel("  output name : "));
		outText = new JTextField("out", 15);
		outText.addKeyListener(this);
		OutBox.add(outText);
		IOBox.add(OutBox);

		// io box is finish
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
		PTMux mux = (PTMux) getCurrentObject();
		if (num == 1)
			mux.setStartNode(p.x, p.y);
		if (num == 2) {
			Point firstNode = mux.getStartNode();
			mux.setWidth(p.x - firstNode.x);
			mux.setHeight(p.y - firstNode.y);
		}
		return true;
	} // nextPoint;

	public int getMinDist(PTGraphicObject go, Point p) {
		PTMux pg = (PTMux) go;
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
		PTMux pg = (PTMux) go;
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
		colorChooser.setColor(props.getColorProperty(PTMux.MUX_TYPE_LABEL
				+ ".color", Color.black));
		depthBox.setSelectedItem(props.getProperty(PTMux.MUX_TYPE_LABEL + ".depth",
				"16"));
		fillColorChooser.setColor(props.getColorProperty(PTMux.MUX_TYPE_LABEL
				+ ".color", Color.black));
		filledCB.setSelected(props
				.getBoolProperty(PTMux.MUX_TYPE_LABEL + ".filled"));
	}

	public void getProperties(XProperties props) {
		props.put(PTMux.MUX_TYPE_LABEL + ".color", colorChooser.getColor());
		props.put(PTMux.MUX_TYPE_LABEL + ".depth", depthBox.getSelectedItem());
		props.put(PTMux.MUX_TYPE_LABEL + ".fillColor", fillColorChooser.getColor());
		props.put(PTMux.MUX_TYPE_LABEL + ".filled", filledCB.isSelected());
	}

	public EditableObject createObject() {
		PTMux pg = new PTMux();
		storeAttributesInto(pg);
		return pg;
	}

	@SuppressWarnings("unchecked")
  public void itemStateChanged(ItemEvent e) {
		PTMux mux = (PTMux) getCurrentObject();
		if (mux == null)
			return;
	
		Object es = e.getSource();
		if (es == filledCB) {
			mux.setFilled(filledCB.isSelected());
		} else if (es == inputCombobox) {
			// the number of input is changed
			mux.getElementSymbol().setText(inputCombobox.getSelectedItem() + "-" + mux.getType());
			int newInputAmount = (Integer) inputCombobox.getSelectedItem();
			ArrayList<PTPin> newInputPin = new ArrayList<PTPin>(newInputAmount);
			for (int i = 0; i < newInputAmount; i++) {
				newInputPin.add(new PTPin(true));
				// copy the former pins...
				if (i < mux.getInputPins().size()
						&& mux.getInputPins().get(i) != null) {
					newInputPin.set(i, mux.getInputPins().get(i));
				} else
					// or create a new one
					newInputPin.get(i).setPinName("in" + i);
			}
			mux.setInputPins(newInputPin);
	
			// and the number of selection pins will be also changed
			int newControlAmount = PTMux.getControlPinAmount(newInputAmount);
			if (newControlAmount != mux.getControlPins().size()) {
				ArrayList<PTPin> newControlPin = new ArrayList<PTPin>(
						newControlAmount);
				for (int i = 0; i < newControlAmount; i++) {
					newControlPin.add(new PTPin(true));
					// copy the former pins...
					if (i < mux.getControlPins().size()
							&& mux.getControlPins().get(i) != null) {
						newControlPin.set(i, mux.getControlPins().get(i));
					} else
						// or create a new one
						newControlPin.get(i).setPinName("s" + i);
				}
				mux.setControlPins(newControlPin);
			}
		}
	
		// if the value of one input or selection is changed
		else {
			ArrayList<JComboBox> values = (ArrayList<JComboBox>) inputValues
					.clone();
			values.addAll(controlValues);
			for (JComboBox comb : values) {
				if (es == comb) {
					int index = values.indexOf(comb);
					char newValue = (Character) comb.getSelectedItem();
					if (index < mux.getInputPins().size()) {
						// input changed
						mux.getInputPins().get(index).setPinValue(newValue);
					} else {
						// selection changed
						index -= mux.getInputPins().size();
						mux.getControlPins().get(index).setPinValue(newValue);
					}
					calculateOutputValueOf(mux);
					break;
				}
			}
		}
	
		Animation.get().doChange();
		repaintNow();
	}

	protected void storeAttributesInto(EditableObject eo) {
		super.storeAttributesInto(eo);
		PTMux p = (PTMux) eo;
		p.setColor(colorChooser.getColor());
		p.setFilled(filledCB.isSelected());
		p.setFillColor(fillColorChooser.getColor());

		p.getOutputPins().get(0).setPinName(outText.getText());

	}

	protected void extractAttributesFrom(EditableObject eo) {
		super.extractAttributesFrom(eo);
		PTMux p = (PTMux) eo;
		colorChooser.setColor(p.getColor());
		filledCB.setEnabled(true);
		filledCB.setSelected(p.isFilled());
		fillColorChooser.setColor(p.getFillColor());

		outText.setText(p.getOutputPins().get(0).getPinName());
		inputCombobox.setSelectedItem(p.getInputPins().size());

	}

	public Editor getSecondaryEditor(EditableObject go) {
		MuxEditor result = new MuxEditor();
		// important! result must be of type RectangleEditor (or cast)
		// and the parameter passed must be of type PTRectangle.
		// Otherwise, not all attributes are copied!
		result.extractAttributesFrom(go);
		return result;
	}

	public String getStatusLineMsg() {
		return AnimalTranslator.translateMessage("MuxEditor.statusLine",
				new Object[] { DrawCanvas.translateDrawButton(),
						DrawCanvas.translateFinishButton(),
						DrawCanvas.translateCancelButton() });
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		PTMux p = (PTMux) getCurrentObject();

		if (p != null) {
			if (Animation.get() != null)
				Animation.get().doChange();
			repaintNow();
		}
	}

	public void propertyChange(PropertyChangeEvent event) {
		PTMux poly = (PTMux) getCurrentObject();
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
		return PTMux.MUX_TYPE_LABEL;
	}

	private void calculateOutputValueOf(PTMux mux) {
   // nothing to be done here
	}

	public void keyPressed(KeyEvent arg0) {
    // nothing to be done here
	}

	@SuppressWarnings("unchecked")
  public void keyReleased(KeyEvent ke) {
		PTMux mux = (PTMux) getCurrentObject();
		if (mux != null) {
			Object es = ke.getSource();
			if (es == outText) {
				mux.getOutputPins().get(0).setPinName(outText.getText());
			} else {
				ArrayList<JTextField> names = (ArrayList<JTextField>) inputNames
						.clone();
				names.addAll(controlNames);
				for (JTextField tf : names) {
					if (es == tf) {
						int index = names.indexOf(tf);
						String newName = inputNames.get(index).getText();
						if (index < inputNames.size())
							mux.getInputPins().get(index).setPinName(newName);
						else {
							index -= inputNames.size();
							mux.getControlPins().get(index).setPinName(newName);
						}
						break;
					}
				}
			}
		} else
			return;
		repaintNow();

	}

	public void keyTyped(KeyEvent arg0) {
		//    // nothing to be done here
	}

	public void mouseClicked(MouseEvent arg0) {
		JDialog inputEditWindow = new JDialog();
		inputEditWindow.setTitle("input & control edit");
		// inputEditWindow.setModal(true);
		inputEditWindow.setLocation(300, 400);
		// inputEditWindow.addWindowListener(this);
		inputEditWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		inputEditWindow.setUndecorated(true);
		inputEditWindow.getRootPane().setWindowDecorationStyle(
				JRootPane.PLAIN_DIALOG);
		inputEditWindow.setResizable(false);
		inputEditWindow.setAlwaysOnTop(true);
		Box editBox = new Box(BoxLayout.PAGE_AXIS);
		// frame content
		PTMux mux = (PTMux) getCurrentObject();
		if (mux != null) {
			if (mux.getInputPins() != null) {
				Box inputEditBox = new Box(BoxLayout.PAGE_AXIS);
				inputNames = new ArrayList<JTextField>(mux.getInputPins().size());
				inputValues = new ArrayList<JComboBox>(mux.getInputPins().size());
				for (int i = 0; i < mux.getInputPins().size(); i++) {
					JLabel newInputLabel = new JLabel("  input " + i + ": "); // "input i: "

					JLabel newInputNameLabel = new JLabel("name "); // "name "

					JTextField newInputName = new JTextField(mux.getInputPins()
							.get(i).getPinName(), 10);
					newInputName.addKeyListener(this);
					inputNames.add(newInputName);

					JLabel newInputValueLabel = new JLabel(" value "); // " value "

					Character[] inputValue = LogicVHDL.LOGIC_VALUES ;
					JComboBox newInputValueCombo = new JComboBox(inputValue);// "0/1/x/z"
					newInputValueCombo.setSelectedItem(mux.getInputPins().get(i)
							.getPinValue());
					newInputValueCombo.addItemListener(this);
					inputValues.add(newInputValueCombo);

					Box newinputBox = new Box(BoxLayout.LINE_AXIS);
					newinputBox.add(newInputLabel);
					newinputBox.add(newInputNameLabel);
					newinputBox.add(newInputName);
					newinputBox.add(newInputValueLabel);
					newinputBox.add(newInputValueCombo);
					inputEditBox.add(newinputBox);
				}
				editBox.add(inputEditBox);
			}
			if (mux.getControlPins() != null) {
				Box controlEditBox = new Box(BoxLayout.PAGE_AXIS);
				controlNames = new ArrayList<JTextField>(mux.getControlPins()
						.size());
				controlValues = new ArrayList<JComboBox>(mux.getControlPins()
						.size());
				for (int i = 0; i < mux.getControlPins().size(); i++) {
					JLabel newControlLabel = new JLabel("  selection " + i
							+ ": "); // "selection i: "

					JLabel newControlNameLabel = new JLabel("name "); // "name "

					JTextField newControlName = new JTextField(mux
							.getControlPins().get(i).getPinName(), 10);
					newControlName.addKeyListener(this);
					controlNames.add(newControlName);

					JLabel newControlValueLabel = new JLabel(" value "); // " value "

					Character[] controlValue = LogicVHDL.LOGIC_VALUES ;
					JComboBox newControlValueCombo = new JComboBox(controlValue);// "0/1"
					newControlValueCombo.setSelectedItem(mux.getControlPins()
							.get(i).getPinValue());
					newControlValueCombo.addItemListener(this);
					controlValues.add(newControlValueCombo);

					Box newControlBox = new Box(BoxLayout.LINE_AXIS);
					newControlBox.add(newControlLabel);
					newControlBox.add(newControlNameLabel);
					newControlBox.add(newControlName);
					newControlBox.add(newControlValueLabel);
					newControlBox.add(newControlValueCombo);
					controlEditBox.add(newControlBox);
				}
				editBox.add(controlEditBox);

			}
			inputEditWindow.add(editBox);
		}
		inputEditWindow.pack();

		inputEditWindow.setVisible(true);

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
} // MuxEditor

