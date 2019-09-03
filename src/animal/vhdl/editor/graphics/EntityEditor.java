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
import animal.vhdl.graphics.PTEntity;
import animal.vhdl.graphics.PTPin;

/**
 * Editor for an entity
 * 
 * @author p_li
 */
public class EntityEditor extends FillablePrimitiveEditor implements ItemListener,
		ActionListener, PropertyChangeListener, KeyListener, MouseListener {

	/**
	 * a JSpinner to choose the amount of input pins
	 */
	private JSpinner inputAmountSpinner;

	/**
	 * a JSpinner to choose the amount of in/out pins
	 */
	private JSpinner inoutAmountSpinner;

	/**
	 * a JSpinner to choose the amount of output pins
	 */
	private JSpinner outputAmountSpinner;

	/**
	 * a JSpinner to choose the amount of control pins
	 */
	private JSpinner controlAmountSpinner;

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
	 * in/out pin
	 */
	private ArrayList<JTextField> inoutNames;
	/**
	 * an array list of JComboBox contains the ComboBoxes for the values of all
	 * in/out pin
	 */
	private ArrayList<JComboBox> inoutValues;

	/**
	 * an array list of JTextField contains the text fields for the names of all
	 * output pin
	 */
	private ArrayList<JTextField> outputNames;
	/**
	 * an array list of JComboBox contains the ComboBoxes for the values of all
	 * output pin
	 */
	private ArrayList<JComboBox> outputValues;

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

	public EntityEditor() {
		super();
	}

	protected void buildGUI() {
		TranslatableGUIElement generator = AnimalTranslator.getGUIBuilder();

		// input, in/out, output and control box
		Box IOBox = new Box(BoxLayout.PAGE_AXIS);
		IOBox.setBorder(new TitledBorder(null, "in,inout,out,control settings",
				TitledBorder.LEADING, TitledBorder.TOP));

		// input box: input amount

		Box inputBox = new Box(BoxLayout.LINE_AXIS);
		JLabel inputAmountLabel = new JLabel("  #input (0-8) : ");
		inputBox.add(inputAmountLabel);

		// if the input amount changed, set a new "inputPin" of the gate.
		ChangeListener inListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				PTEntity entity = (PTEntity) getCurrentObject();
				if (entity != null) {
					int newAmount = ((SpinnerNumberModel) inputAmountSpinner
							.getModel()).getNumber().intValue();
					ArrayList<PTPin> newPins = new ArrayList<PTPin>(newAmount);
					ArrayList<PTPin> pins = entity.getInputPins();
					for (int i = 0; i < newAmount; i++) {
						newPins.add(new PTPin(true));
						// copy the former pins...
						if (i < pins.size() && pins.get(i) != null) {
							newPins.get(i).setPinName(pins.get(i).getPinName());
							newPins.get(i).setPinValue(
									pins.get(i).getPinValue());
						} else
							// or create a new one
							newPins.get(i).setPinName("in" + i);
					}
					entity.setInputPins(newPins);
					repaintNow();
				}
			}
		};
		SpinnerModel inModel = new SpinnerNumberModel(0, 0, 8, 1);
		inModel.addChangeListener(inListener);
		inputAmountSpinner = new JSpinner(inModel);
		inputBox.add(inputAmountSpinner);

		IOBox.add(inputBox);

		// in/out box: in/out amount

		Box inoutBox = new Box(BoxLayout.LINE_AXIS);
		JLabel inoutAmountLabel = new JLabel(" #in-out (0-8) : ");
		inoutBox.add(inoutAmountLabel);

		// if the in/out amount changed, set a new "inoutPin" of the gate.
		ChangeListener inoutListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				PTEntity entity = (PTEntity) getCurrentObject();
				if (entity != null) {
					int newAmount = ((SpinnerNumberModel) inoutAmountSpinner
							.getModel()).getNumber().intValue();
					ArrayList<PTPin> newPins = new ArrayList<PTPin>(newAmount);
					ArrayList<PTPin> pins = entity.getInoutPins();
					for (int i = 0; i < newAmount; i++) {
						newPins.add(new PTPin(true));
						// copy the former pins...
						if (i < pins.size() && pins.get(i) != null) {
							newPins.get(i).setPinName(pins.get(i).getPinName());
							newPins.get(i).setPinValue(
									pins.get(i).getPinValue());
						} else
							// or create a new one
							newPins.get(i).setPinName("io" + i);
					}
					entity.setInoutPins(newPins);
					repaintNow();
				}
			}
		};
		SpinnerModel ioModel = new SpinnerNumberModel(0, 0, 8, 1);
		ioModel.addChangeListener(inoutListener);
		inoutAmountSpinner = new JSpinner(ioModel);
//		inoutAmountSpinn
		inoutBox.add(inoutAmountSpinner);

		IOBox.add(inoutBox);

		// output box: input amount

		Box outputBox = new Box(BoxLayout.LINE_AXIS);
		JLabel outputAmountLabel = new JLabel(" #outout (0-8) : ");
		outputBox.add(outputAmountLabel);

		// if the output amount changed, set a new "outputPin" of the gate.
		ChangeListener outListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				PTEntity entity = (PTEntity) getCurrentObject();
				if (entity != null) {
					int newAmount = ((SpinnerNumberModel) outputAmountSpinner
							.getModel()).getNumber().intValue();
					ArrayList<PTPin> newPins = new ArrayList<PTPin>(newAmount);
					ArrayList<PTPin> pins = entity.getOutputPins();
					for (int i = 0; i < newAmount; i++) {
						newPins.add(new PTPin(false));
						// copy the former pins...
						if (i < pins.size() && pins.get(i) != null) {
							newPins.get(i).setPinName(pins.get(i).getPinName());
							newPins.get(i).setPinValue(
									pins.get(i).getPinValue());
						} else
							// or create a new one
							newPins.get(i).setPinName("out" + i);
					}
					entity.setOutputPins(newPins);
					repaintNow();
				}
			}
		};
		SpinnerModel outModel = new SpinnerNumberModel(0, 0, 8, 1);
		outModel.addChangeListener(outListener);
		outputAmountSpinner = new JSpinner(outModel);
		outputBox.add(outputAmountSpinner);

		IOBox.add(outputBox);

		// control box: input amount

		Box controlBox = new Box(BoxLayout.LINE_AXIS);
		JLabel controlAmountLabel = new JLabel("#conctrol (0-8) : ");
		controlBox.add(controlAmountLabel);

		// if the control amount changed, set a new "controlPin" of the gate.
		ChangeListener conListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				PTEntity entity = (PTEntity) getCurrentObject();
				if (entity != null) {
					int newAmount = ((SpinnerNumberModel) controlAmountSpinner
							.getModel()).getNumber().intValue();
					ArrayList<PTPin> newPins = new ArrayList<PTPin>(newAmount);
					ArrayList<PTPin> pins = entity.getControlPins();
					for (int i = 0; i < newAmount; i++) {
						newPins.add(new PTPin(true));
						// copy the former pins...
						if (i < pins.size() && pins.get(i) != null) {
							newPins.get(i).setPinName(pins.get(i).getPinName());
							newPins.get(i).setPinValue(
									pins.get(i).getPinValue());
						} else
							// or create a new one
							newPins.get(i).setPinName("c" + i);
					}
					entity.setControlPins(newPins);
					repaintNow();
				}
			}
		};
		SpinnerModel conModel = new SpinnerNumberModel(0, 0, 8, 1);
		conModel.addChangeListener(conListener);
		controlAmountSpinner = new JSpinner(conModel);
		controlBox.add(controlAmountSpinner);

		IOBox.add(controlBox);

		JButton editButton = new JButton("Edit");
		editButton.addMouseListener(this);
		inputBox.add(editButton);
		IOBox.add(editButton);

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
		PTEntity not = (PTEntity) getCurrentObject();
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
		PTEntity pg = (PTEntity) go;
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
		PTEntity pg = (PTEntity) go;
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
		colorChooser.setColor(props.getColorProperty(PTEntity.ENTITY_TYPE_LABEL
				+ ".color", Color.black));
		depthBox.setSelectedItem(props.getProperty(PTEntity.ENTITY_TYPE_LABEL
				+ ".depth", "16"));
		fillColorChooser.setColor(props.getColorProperty(PTEntity.ENTITY_TYPE_LABEL
				+ ".color", Color.black));
		filledCB.setSelected(props.getBoolProperty(PTEntity.ENTITY_TYPE_LABEL
				+ ".filled"));
	}

	public void getProperties(XProperties props) {
		props.put(PTEntity.ENTITY_TYPE_LABEL + ".color", colorChooser.getColor());
		props.put(PTEntity.ENTITY_TYPE_LABEL + ".depth", depthBox.getSelectedItem());
		props.put(PTEntity.ENTITY_TYPE_LABEL + ".fillColor", fillColorChooser
				.getColor());
		props.put(PTEntity.ENTITY_TYPE_LABEL + ".filled", filledCB.isSelected());
	}

	public EditableObject createObject() {
		PTEntity pg = new PTEntity();
		storeAttributesInto(pg);
		return pg;
	}

	protected void storeAttributesInto(EditableObject eo) {
		super.storeAttributesInto(eo);
		PTEntity entity = (PTEntity) eo;
		entity.setColor(colorChooser.getColor());
		entity.setFilled(filledCB.isSelected());
		entity.setFillColor(fillColorChooser.getColor());
	}

	protected void extractAttributesFrom(EditableObject eo) {
		super.extractAttributesFrom(eo);
		PTEntity entity = (PTEntity) eo;
		colorChooser.setColor(entity.getColor());
		filledCB.setEnabled(true);
		filledCB.setSelected(entity.isFilled());
		fillColorChooser.setColor(entity.getFillColor());

		inputAmountSpinner.setValue(entity.getInputPins().size());
		inoutAmountSpinner.setValue(entity.getInoutPins().size());
		outputAmountSpinner.setValue(entity.getOutputPins().size());
		controlAmountSpinner.setValue(entity.getControlPins().size());
	}

	public Editor getSecondaryEditor(EditableObject go) {
		EntityEditor result = new EntityEditor();
		// important! result must be of type RectangleEditor (or cast)
		// and the parameter passed must be of type PTRectangle.
		// Otherwise, not all attributes are copied!
		result.extractAttributesFrom(go);
		return result;
	}

	public String getStatusLineMsg() {
		return AnimalTranslator.translateMessage("EntityEditor.statusLine",
				new Object[] { DrawCanvas.translateDrawButton(),
						DrawCanvas.translateFinishButton(),
						DrawCanvas.translateCancelButton() });
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		PTEntity p = (PTEntity) getCurrentObject();

		if (p != null) {
			if (Animation.get() != null)
				Animation.get().doChange();
			repaintNow();
		}
	}

	public void propertyChange(PropertyChangeEvent event) {
		PTEntity poly = (PTEntity) getCurrentObject();
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
		return PTEntity.ENTITY_TYPE_LABEL;
	}

	public void itemStateChanged(ItemEvent e) {
		PTEntity entity = (PTEntity) getCurrentObject();

		if (entity == null)
			return;

		Object es = e.getSource();

		if (es == filledCB) {
			entity.setFilled(filledCB.isSelected());

		}

		// if the value of one pin is changed

		else {
			value: {
				for (JComboBox comb : inputValues) {
					if (es == comb) {
						int index = inputValues.indexOf(comb);
						entity.getInputPins().get(index).setPinValue(
								(Character) comb.getSelectedItem());
						break value;
					}
				}
				for (JComboBox comb : inoutValues) {
					if (es == comb) {
						int index = inoutValues.indexOf(comb);
						entity.getInoutPins().get(index).setPinValue(
								(Character) comb.getSelectedItem());
						break value;
					}
				}
				for (JComboBox comb : outputValues) {
					if (es == comb) {
						int index = outputValues.indexOf(comb);
						entity.getOutputPins().get(index).setPinValue(
								(Character) comb.getSelectedItem());
						break value;
					}
				}
				if (controlValues.contains(es)) {
					int index = controlValues.indexOf(es);
					entity.getControlPins().get(index).setPinValue(
							(Character) ((JComboBox) es).getSelectedItem());
				}
			}
		}

		Animation.get().doChange();
		repaintNow();
	}// itemStateChanged

	public void keyPressed(KeyEvent arg0) {
		// nothing to be done here

	}

	public void keyReleased(KeyEvent ke) {
		PTEntity entity = (PTEntity) getCurrentObject();

		if (entity == null)
			return;

		Object es = ke.getSource();

		if (inputNames.contains(es)) {
			int index = inputNames.indexOf(es);
			PTPin changedPin = entity.getInputPins().get(index);
			String newName = ((JTextField) es).getText();
			changedPin.setPinName(newName);
		} else if (inoutNames.contains(es)) {
			int index = inoutNames.indexOf(es);
			PTPin changedPin = entity.getInoutPins().get(index);
			String newName = ((JTextField) es).getText();
			changedPin.setPinName(newName);
		} else if (outputNames.contains(es)) {
			int index = outputNames.indexOf(es);
			PTPin changedPin = entity.getOutputPins().get(index);
			String newName = ((JTextField) es).getText();
			changedPin.setPinName(newName);
		} else if (controlNames.contains(es)) {
			int index = controlNames.indexOf(es);
			PTPin changedPin = entity.getControlPins().get(index);
			String newName = ((JTextField) es).getText();
			changedPin.setPinName(newName);
		}

		repaintNow();

	}

	public void keyTyped(KeyEvent arg0) {
		// nothing happens here

	}

	public void mouseClicked(MouseEvent me) {
		PTEntity entity = (PTEntity) getCurrentObject();
		if (entity == null)
			return;

		// edit window
		JDialog editWindow = new JDialog();
		editWindow.setTitle("edit");
		editWindow.setLocation(me.getLocationOnScreen());
		editWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		editWindow.setUndecorated(true);
		editWindow.getRootPane().setWindowDecorationStyle(
				JRootPane.PLAIN_DIALOG);
		editWindow.setResizable(false);
		editWindow.setAlwaysOnTop(true);
		// frame content
		Box editBox = new Box(BoxLayout.PAGE_AXIS);

		// input
		if (entity.getInputPins() != null) {

			inputNames = new ArrayList<JTextField>(entity.getInputPins().size());
			inputValues = new ArrayList<JComboBox>(entity.getInputPins().size());
			
			Box inputEditBox = getEditBox(inputNames, inputValues, " input",
					entity.getInputPins());
			editBox.add(inputEditBox);
		}
		// in/out
		if (entity.getInoutPins() != null) {

			inoutNames = new ArrayList<JTextField>(entity.getInoutPins().size());
			inoutValues = new ArrayList<JComboBox>(entity.getInoutPins().size());
			Box inoutEditBox = getEditBox(inoutNames, inoutValues, "in-out",
					entity.getInoutPins());
			editBox.add(inoutEditBox);
		}
		// output
		if (entity.getOutputPins() != null) {
			outputNames = new ArrayList<JTextField>(entity.getOutputPins().size());
			outputValues = new ArrayList<JComboBox>(entity.getOutputPins().size());
			Box outputEditBox = getEditBox(outputNames, outputValues, "output",
					entity.getOutputPins());
			editBox.add(outputEditBox);
		}
		// control
		if (entity.getControlPins() != null) {
			controlNames = new ArrayList<JTextField>(entity.getControlPins().size());
			controlValues = new ArrayList<JComboBox>(entity.getControlPins().size());
			Box controlEditBox = getEditBox(controlNames, controlValues,
					"control", entity.getControlPins());
			editBox.add(controlEditBox);
		}

		editWindow.add(editBox);
		editWindow.pack();
		editWindow.setVisible(true);
	}

	private Box getEditBox(ArrayList<JTextField> names,
			ArrayList<JComboBox> values, String portType, ArrayList<PTPin> pins) {
		Box portEditBox = new Box(BoxLayout.PAGE_AXIS);
		Character[] valueItems = { ' ', '0', '1', 'x', 'z' };
		for (int i = 0; i < pins.size(); i++) {
				
			JLabel portLabel = new JLabel(portType + i + ": ");
			JLabel nameLabel = new JLabel("name ");
			JTextField nameTextField = new JTextField(pins.get(i).getPinName(),
					10);
			nameTextField.addKeyListener(this);
			names.add(nameTextField);

			JLabel valueLabel = new JLabel(" value "); // " value "
			JComboBox valueCombo = new JComboBox(valueItems);// "0/1/x/z"
			valueCombo.setSelectedItem(pins.get(i).getPinValue());
			valueCombo.addItemListener(this);
			values.add(valueCombo);

			Box portBox = new Box(BoxLayout.LINE_AXIS);
			portBox.add(portLabel);
			portBox.add(nameLabel);
			portBox.add(nameTextField);
			portBox.add(valueLabel);
			portBox.add(valueCombo);
			portEditBox.add(portBox);
		}
		return portEditBox;
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

} // EntityEditor

