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
import animal.vhdl.graphics.PTDemux;
import animal.vhdl.graphics.PTPin;
import animal.vhdl.logic.LogicVHDL;

/**
 * Editor for a RS flip flop
 * 
 * @author p_li
 */
public class DemuxEditor extends FillablePrimitiveEditor implements ItemListener,
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

	public DemuxEditor() {
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
		JLabel inputAmountLabel = new JLabel("  #output : ");
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
		OutBox.add(new JLabel("  in name : "));
		outText = new JTextField("in", 15);
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
		PTDemux demux = (PTDemux) getCurrentObject();
		if (num == 1)
			demux.setStartNode(p.x, p.y);
		if (num == 2) {
			Point firstNode = demux.getStartNode();
			demux.setWidth(p.x - firstNode.x);
			demux.setHeight(p.y - firstNode.y);
		}
		return true;
	} // nextPoint;

	public int getMinDist(PTGraphicObject go, Point p) {
		PTDemux pg = (PTDemux) go;
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
		PTDemux pg = (PTDemux) go;
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
		colorChooser.setColor(props.getColorProperty(PTDemux.DEMUX_TYPE_LABEL
				+ ".color", Color.black));
		depthBox.setSelectedItem(props.getProperty(PTDemux.DEMUX_TYPE_LABEL + ".depth",
				"16"));
		fillColorChooser.setColor(props.getColorProperty(PTDemux.DEMUX_TYPE_LABEL
				+ ".color", Color.black));
		filledCB.setSelected(props
				.getBoolProperty(PTDemux.DEMUX_TYPE_LABEL + ".filled"));
	}

	public void getProperties(XProperties props) {
		props.put(PTDemux.DEMUX_TYPE_LABEL + ".color", colorChooser.getColor());
		props.put(PTDemux.DEMUX_TYPE_LABEL + ".depth", depthBox.getSelectedItem());
		props.put(PTDemux.DEMUX_TYPE_LABEL + ".fillColor", fillColorChooser.getColor());
		props.put(PTDemux.DEMUX_TYPE_LABEL + ".filled", filledCB.isSelected());
	}

	public EditableObject createObject() {
		PTDemux pg = new PTDemux();
		storeAttributesInto(pg);
		return pg;
	}

	@SuppressWarnings("unchecked")
  public void itemStateChanged(ItemEvent e) {
		PTDemux demux = (PTDemux) getCurrentObject();
		if (demux == null)
			return;
	
		Object es = e.getSource();
		if (es == filledCB) {
			demux.setFilled(filledCB.isSelected());
		} else if (es == inputCombobox) {
			// the number of output is changed
			int newInputAmount = (Integer) inputCombobox.getSelectedItem();
			demux.getElementSymbol().setText(newInputAmount + "-" + demux.getType());
			ArrayList<PTPin> newOutputPin = new ArrayList<PTPin>(newInputAmount);
			for (int i = 0; i < newInputAmount; i++) {
				newOutputPin.add(new PTPin(false));
				// copy the former pins...
				if (i < demux.getOutputPins().size()
						&& demux.getOutputPins().get(i) != null) {
					newOutputPin.set(i, demux.getOutputPins().get(i));
				} else
					// or create a new one
					newOutputPin.get(i).setPinName("out" + i);
			}
			demux.setOutputPins(newOutputPin);
	
			// and the number of selection pins will be also changed
			int newControlAmount = PTDemux.getControlPinAmount(newInputAmount);
			if (newControlAmount != demux.getControlPins().size()) {
				ArrayList<PTPin> newControlPin = new ArrayList<PTPin>(
						newControlAmount);
				for (int i = 0; i < newControlAmount; i++) {
					newControlPin.add(new PTPin(false));
					// copy the former pins...
					if (i < demux.getControlPins().size()
							&& demux.getControlPins().get(i) != null) {
						newControlPin.set(i, demux.getControlPins().get(i));
					} else
						// or create a new one
						newControlPin.get(i).setPinName("s" + i);
				}
				demux.setControlPins(newControlPin);
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
					if (index < demux.getOutputPins().size()) {
						// input changed
						demux.getOutputPins().get(index).setPinValue(newValue);
					} else {
						// selection changed
						index -= demux.getOutputPins().size();
						demux.getControlPins().get(index).setPinValue(newValue);
					}
					calculateOutputValueOf(demux);
					break;
				}
			}
		}
	
		Animation.get().doChange();
		repaintNow();
	}

	protected void storeAttributesInto(EditableObject eo) {
		super.storeAttributesInto(eo);
		PTDemux p = (PTDemux) eo;
		p.setColor(colorChooser.getColor());
		p.setFilled(filledCB.isSelected());
		p.setFillColor(fillColorChooser.getColor());

		p.getInputPins().get(0).setPinName(outText.getText());

	}

	protected void extractAttributesFrom(EditableObject eo) {
		super.extractAttributesFrom(eo);
		PTDemux p = (PTDemux) eo;
		colorChooser.setColor(p.getColor());
		filledCB.setEnabled(true);
		filledCB.setSelected(p.isFilled());
		fillColorChooser.setColor(p.getFillColor());

		outText.setText(p.getInputPins().get(0).getPinName());
		inputCombobox.setSelectedItem(p.getOutputPins().size());

	}

	public Editor getSecondaryEditor(EditableObject go) {
		DemuxEditor result = new DemuxEditor();
		// important! result must be of type RectangleEditor (or cast)
		// and the parameter passed must be of type PTRectangle.
		// Otherwise, not all attributes are copied!
		result.extractAttributesFrom(go);
		return result;
	}

	public String getStatusLineMsg() {
		return AnimalTranslator.translateMessage("DemuxEditor.statusLine",
				new Object[] { DrawCanvas.translateDrawButton(),
						DrawCanvas.translateFinishButton(),
						DrawCanvas.translateCancelButton() });
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		PTDemux p = (PTDemux) getCurrentObject();

		if (p != null) {
			if (Animation.get() != null)
				Animation.get().doChange();
			repaintNow();
		}
	}

	public void propertyChange(PropertyChangeEvent event) {
		PTDemux poly = (PTDemux) getCurrentObject();
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
		return PTDemux.DEMUX_TYPE_LABEL;
	}

	private void calculateOutputValueOf(PTDemux demux) {
		// TODO Auto-generated method stub
		// abc;

	}

	// itemStateChanged

	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
  public void keyReleased(KeyEvent ke) {
		// TODO Auto-generated method stub
		PTDemux demux = (PTDemux) getCurrentObject();
		if (demux != null) {
			Object es = ke.getSource();
			if (es == outText) {
				demux.getInputPins().get(0).setPinName(outText.getText());
			} else {
				ArrayList<JTextField> names = (ArrayList<JTextField>) inputNames
						.clone();
				names.addAll(controlNames);
				for (JTextField tf : names) {
					if (es == tf) {
						int index = names.indexOf(tf);
						String newName = inputNames.get(index).getText();
						if (index < inputNames.size())
							demux.getOutputPins().get(index).setPinName(newName);
						else {
							index -= inputNames.size();
							demux.getControlPins().get(index).setPinName(newName);
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
		// TODO Auto-generated method stub

	}

	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		JDialog inputEditWindow = new JDialog();
		inputEditWindow.setTitle("output & control edit");
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
		PTDemux demux = (PTDemux) getCurrentObject();
		if (demux != null) {
			if (demux.getOutputPins() != null) {
				Box inputEditBox = new Box(BoxLayout.PAGE_AXIS);
				inputNames = new ArrayList<JTextField>(demux.getOutputPins().size());
				inputValues = new ArrayList<JComboBox>(demux.getOutputPins().size());
				for (int i = 0; i < demux.getOutputPins().size(); i++) {
					JLabel newInputLabel = new JLabel("  output " + i + ": "); // "input i: "

					JLabel newInputNameLabel = new JLabel("name "); // "name "

					JTextField newInputName = new JTextField(demux.getOutputPins()
							.get(i).getPinName(), 10);
					newInputName.addKeyListener(this);
					inputNames.add(newInputName);

					JLabel newInputValueLabel = new JLabel(" value "); // " value "

					Character[] inputValue = LogicVHDL.LOGIC_VALUES;
					JComboBox newInputValueCombo = new JComboBox(inputValue);
					newInputValueCombo.setSelectedItem(demux.getOutputPins().get(i)
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
			if (demux.getControlPins() != null) {
				Box controlEditBox = new Box(BoxLayout.PAGE_AXIS);
				controlNames = new ArrayList<JTextField>(demux.getControlPins()
						.size());
				controlValues = new ArrayList<JComboBox>(demux.getControlPins()
						.size());
				for (int i = 0; i < demux.getControlPins().size(); i++) {
					JLabel newControlLabel = new JLabel("  selection " + i
							+ ": "); // "selection i: "

					JLabel newControlNameLabel = new JLabel("name "); // "name "

					JTextField newControlName = new JTextField(demux
							.getControlPins().get(i).getPinName(), 10);
					newControlName.addKeyListener(this);
					controlNames.add(newControlName);

					JLabel newControlValueLabel = new JLabel(" value "); // " value "

					Character[] controlValue = LogicVHDL.LOGIC_VALUES;
					JComboBox newControlValueCombo = new JComboBox(controlValue);// "0/1"
					newControlValueCombo.setSelectedItem(demux.getControlPins()
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
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
} // RectangleEditor

