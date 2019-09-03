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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
import animal.vhdl.graphics.PTPin;
import animal.vhdl.graphics.PTXnor;
import animal.vhdl.logic.LogicXnor;

/**
 * Editor for an xnor gate
 * 
 * @author p_li
 */
public class XnorEditor extends FillablePrimitiveEditor implements ItemListener,
		ActionListener, PropertyChangeListener, KeyListener, MouseListener,
		WindowListener {
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
	 * a JSpinner to choose the amount of input pins
	 */
	private JSpinner inputAmountSpinner;

	/**
	 * create a xnor editor
	 */
	public XnorEditor() {
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
		JLabel inputAmountLabel = new JLabel("  #input (2-10) : ");
		inputBox.add(inputAmountLabel);

		// if the input amount changed, set a new "inputPin" of the gate.
		ChangeListener listener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				PTXnor gate = (PTXnor) getCurrentObject();
				if (gate != null) {
					int newInputAmount = ((SpinnerNumberModel) inputAmountSpinner
							.getModel()).getNumber().intValue();
					ArrayList<PTPin> newInputPin = new ArrayList<PTPin>(
							newInputAmount);
					for (int i = 0; i < newInputAmount; i++) {
						// copy the former pins...
						newInputPin.add(new PTPin(true));
						if (i < gate.getInputPins().size()
								&& gate.getInputPins().get(i) != null) {
							newInputPin.get(i).setPinName(
									gate.getInputPins().get(i).getPinName());
							newInputPin.get(i).setPinValue(
									gate.getInputPins().get(i).getPinValue());
						} else
							// or create a new one
							newInputPin.get(i).setPinName("in" + i);
					}
					gate.setInputPins(newInputPin);
					repaintNow();
				}
			}
		};
		SpinnerModel model = new SpinnerNumberModel(2, 2, 10, 1);
		model.addChangeListener(listener);
		inputAmountSpinner = new JSpinner(model);

		inputBox.add(inputAmountSpinner);
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
		PTXnor gate = (PTXnor) getCurrentObject();
		if (num == 1)
			gate.setStartNode(p.x, p.y);
		if (num == 2) {
			Point firstNode = gate.getStartNode();
			gate.setWidth(p.x - firstNode.x);
			gate.setHeight(p.y - firstNode.y);
		}
		return true;
	} // nextPoint;

	public int getMinDist(PTGraphicObject go, Point p) {
		PTXnor gate = (PTXnor) go;
		Point a = new Point(gate.getStartNode().x, gate.getStartNode().y);
		Rectangle boundingBox = gate.getBoundingBox();
		// if point is inside, there is not much of distance ;-)
		if (boundingBox.contains(p.x, p.y))
			return 0;

		// (ULC, URC)
		Point b = new Point(a.x + gate.getWidth(), a.y);
		int minDist = Integer.MAX_VALUE;
		int newDist = MSMath.dist(p, a, b);
		if (newDist < minDist)
			minDist = newDist;

		// (URC, LRC)
		b.translate(0, gate.getHeight());
		newDist = MSMath.dist(p, a, b);
		if (newDist < minDist)
			minDist = newDist;

		// (LRC, LLC)
		a.translate(gate.getWidth(), gate.getHeight());
		newDist = MSMath.dist(p, a, b);
		if (newDist < minDist)
			minDist = newDist;

		newDist = MSMath.dist(p, a, gate.getStartNode());
		if (newDist < minDist)
			minDist = newDist;
		return minDist;
	}

	public EditPoint[] getEditPoints(PTGraphicObject go) {
		PTXnor gate = (PTXnor) go;
		// int pSize = 2;
		int width = gate.getWidth();
		int height = gate.getHeight();
		// int i;
		// add change points(nodes)
		EditPoint[] result = new EditPoint[5];
		Point helper = gate.getStartNode();
		// result[5] = new EditPoint(1, helper);
		helper = new Point(helper.x + width, helper.y + height);
		result[0] = new EditPoint(2, helper);

		int x = gate.getStartNode().x;
		int y = gate.getStartNode().y;
		result[1] = new EditPoint(-2, new Point(x + (width / 2), y));
		result[2] = new EditPoint(-3, new Point(x + width, y + (height / 2)));
		result[3] = new EditPoint(-4, new Point(x + (width / 2), y + height));
		result[4] = new EditPoint(-5, new Point(x, y + (height / 2)));

		return result;
	} // getEditPoints

	public void setProperties(XProperties props) {
		colorChooser.setColor(props.getColorProperty(PTXnor.XNOR_TYPE_LABEL
				+ ".color", Color.black));
		depthBox.setSelectedItem(props.getProperty(
				PTXnor.XNOR_TYPE_LABEL + ".depth", "16"));
		fillColorChooser.setColor(props.getColorProperty(PTXnor.XNOR_TYPE_LABEL
				+ ".color", Color.black));
		filledCB.setSelected(props.getBoolProperty(PTXnor.XNOR_TYPE_LABEL
				+ ".filled"));
		// inputAmountSpinner.setValue(props.getIntProperty(PTXnor.TYPE_LABEL
		// + ".inputAmount", 2));
	}

	public void getProperties(XProperties props) {
		props.put(PTXnor.XNOR_TYPE_LABEL + ".color", colorChooser.getColor());
		props.put(PTXnor.XNOR_TYPE_LABEL + ".depth", depthBox.getSelectedItem());
		props
				.put(PTXnor.XNOR_TYPE_LABEL + ".fillColor", fillColorChooser
						.getColor());
		props.put(PTXnor.XNOR_TYPE_LABEL + ".filled", filledCB.isSelected());
		// props.put(PTXnor.TYPE_LABEL + ".inputAmount", inputAmountSpinner
		// .getValue().toString());
		// props.put(PTXnor.TYPE_LABEL + ".outputName", String.valueOf(outText
		// .getText()));
	}

	public void itemStateChanged(ItemEvent e) {
		PTXnor gate = (PTXnor) getCurrentObject();

		if (e.getSource() == filledCB) {
			if (gate != null)
				gate.setFilled(filledCB.isSelected());

		}

		// if the value of one input is changed
		else {
			for (JComboBox comb : inputValues) {
				if (gate != null)
					if (e.getSource() == comb) {
						int index = inputValues.indexOf(comb);
						gate.getInputPins().get(index).setPinValue(
								(Character) comb.getSelectedItem());
						calculateOutputValueOf(gate);
						break;
					}
			}
		}

		Animation.get().doChange();
		repaintNow();
	} // itemStateChanged

	public EditableObject createObject() {
		PTXnor gate = new PTXnor();
		storeAttributesInto(gate);
		return gate;
	}

	protected void storeAttributesInto(EditableObject eo) {
		super.storeAttributesInto(eo);
		PTXnor gate = (PTXnor) eo;
		gate.setColor(colorChooser.getColor());
		gate.setFilled(filledCB.isSelected());
		gate.setFillColor(fillColorChooser.getColor());
		gate.getOutputPins().get(0).setPinName(outText.getText());
	}

	protected void extractAttributesFrom(EditableObject eo) {
		super.extractAttributesFrom(eo);
		PTXnor gate = (PTXnor) eo;
		colorChooser.setColor(gate.getColor());
		filledCB.setEnabled(true);
		filledCB.setSelected(gate.isFilled());
		fillColorChooser.setColor(gate.getFillColor());
		outText.setText(gate.getOutputPins().get(0).getPinName());
		inputAmountSpinner.setValue(gate.getInputPins().size());

	}

	public Editor getSecondaryEditor(EditableObject go) {

		XnorEditor result = new XnorEditor();
		result.extractAttributesFrom(go);
		return result;
	}

	public String getStatusLineMsg() {
		return AnimalTranslator.translateMessage("XnorEditor.statusLine",
				new Object[] { DrawCanvas.translateDrawButton(),
						DrawCanvas.translateFinishButton(),
						DrawCanvas.translateCancelButton() });
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		PTXnor gate = (PTXnor) getCurrentObject();

		if (gate != null) {
			if (Animation.get() != null) {
				Animation.get().doChange();
			}
			repaintNow();
		}
	}

	public void propertyChange(PropertyChangeEvent event) {
		PTXnor gate = (PTXnor) getCurrentObject();
		String eventName = event.getPropertyName();
		if ("color".equals(eventName))
			gate.setColor((Color) event.getNewValue());
		else if ("fillColor".equals(eventName))
			gate.setFillColor((Color) event.getNewValue());
		if (!event.getOldValue().equals(event.getNewValue())) {
			repaintNow();
			if (Animation.get() != null)
				Animation.get().doChange();
		}
	}

	public String getBasicType() {
		return PTXnor.XNOR_TYPE_LABEL;
	}

	private void calculateOutputValueOf(PTXnor gate) {
		char[] inputList = new char[gate.getInputPins().size()];
		for (int i = 0; i < gate.getInputPins().size(); i++) {
			inputList[i] = gate.getInputPins().get(i).getPinValue();
		}
		LogicXnor lxnor = new LogicXnor(inputList);
		char outputValue = lxnor.getLogicResult();
		gate.getOutputPins().get(0).setPinValue(outputValue);

	}

	public void keyPressed(KeyEvent arg0) {
    // nothing to be done here
	}

	public void keyReleased(KeyEvent ke) {
		PTXnor gate = (PTXnor) getCurrentObject();
		if (gate != null) {
			if (ke.getSource() == outText)
				gate.getOutputPins().get(0).setPinName(outText.getText());
			if (inputNames != null)
				for (JTextField tf : inputNames) {
					if (ke.getSource() == tf) {
						int i = inputNames.indexOf(tf);
						gate.getInputPins().get(i).setPinName(
								inputNames.get(i).getText());
						break;
					}

				}
		}
		repaintNow();

	}

	public void keyTyped(KeyEvent arg0) {
    // nothing to be done here
	}

	public void mouseClicked(MouseEvent me) {
		JDialog inputEditWindow = new JDialog();
		inputEditWindow.setTitle("input edit");
		// inputEditWindow.setModal(true);
		inputEditWindow.setLocation(300, 400);
		inputEditWindow.addWindowListener(this);
		inputEditWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		inputEditWindow.setUndecorated(true);
		inputEditWindow.getRootPane().setWindowDecorationStyle(
				JRootPane.PLAIN_DIALOG);
		inputEditWindow.setResizable(false);
		inputEditWindow.setAlwaysOnTop(true);
		// frame content
		PTXnor gate = (PTXnor) getCurrentObject();
		if (gate != null) {
			if (gate.getInputPins() != null) {
				Box inputEditBox = new Box(BoxLayout.PAGE_AXIS);
				inputNames = new ArrayList<JTextField>(gate.getInputPins()
						.size());
				inputValues = new ArrayList<JComboBox>(gate.getInputPins()
						.size());
				for (int i = 0; i < gate.getInputPins().size(); i++) {
					JLabel newInputLabel = new JLabel("  input " + i + ": "); // "input i: "

					JLabel newInputNameLabel = new JLabel("name "); // "name "

					JTextField newInputName = new JTextField(gate.getInputPins()
							.get(i).getPinName(), 10);
					newInputName.addKeyListener(this);
					inputNames.add(newInputName);

					JLabel newInputValueLabel = new JLabel(" value "); // " value "

					Character[] inputValue = { ' ', '0', '1', 'x', 'z' };
					JComboBox newInputValueCombo = new JComboBox(inputValue);// "0/1/x/z"
					newInputValueCombo.setSelectedItem(gate.getInputPins()
							.get(i).getPinValue());
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
				inputEditWindow.add(inputEditBox);
			}
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

	public void windowActivated(WindowEvent arg0) {
    // nothing to be done here
	}

	public void windowClosed(WindowEvent arg0) {
    // nothing to be done here
	}

	public void windowClosing(WindowEvent arg0) {
    // nothing to be done here
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

} // XnorEditor
