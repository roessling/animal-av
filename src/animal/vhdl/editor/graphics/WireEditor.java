package animal.vhdl.editor.graphics;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.text.NumberFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import translator.AnimalTranslator;
import animal.editor.Editor;
import animal.editor.graphics.PolylineEditor;
import animal.graphics.PTGraphicObject;
import animal.graphics.PTPoint;
import animal.gui.DrawCanvas;
import animal.main.Animation;
import animal.misc.EditPoint;
import animal.misc.EditableObject;
import animal.misc.MSMath;
import animal.vhdl.graphics.PTWire;

public class WireEditor extends PolylineEditor implements MouseListener,
		KeyListener {

	JFormattedTextField speedLabelText;
	JSlider speedSlider;

	/**
	 * create a and editor
	 */
	public WireEditor() {
		super();
	}

	protected void buildGUI() {

		Box speedBox = new Box(BoxLayout.LINE_AXIS);

		JLabel speedLabel = new JLabel("speed: ");
		speedBox.add(speedLabel);

		speedLabelText = new JFormattedTextField(NumberFormat
				.getIntegerInstance());
		speedLabelText.setValue(100);
		speedLabelText.addKeyListener(this);
		speedLabelText.setEditable(false);
		speedBox.add(speedLabelText);

		ChangeListener listener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (e.getSource() instanceof JSlider) {
					int value = ((JSlider) e.getSource()).getValue();
					speedLabelText.setValue(value);
				}
			}
		};
		speedSlider = new JSlider(0, 500, 100);
		speedSlider.addChangeListener(listener);

		speedBox.add(speedSlider);
		JButton orderButton = new JButton("order");
		orderButton.addMouseListener(this);
		speedBox.add(orderButton);
		addBox(speedBox);
		super.buildGUI();
	}

	public boolean nextPoint(int num, Point p) {
		PTWire w = (PTWire) getCurrentObject();
		w.setNode(num - 1, new PTPoint(p));
		return true;
	}

	public int getMinDist(PTGraphicObject go, Point p) {
		PTWire pg = (PTWire) go;
		// if the polyline is a filled polygon and the point is inside
		// there is not much of distance
		Point a;
		Point b = pg.getNodeAsPoint(0);
		int minDist = Integer.MAX_VALUE;
		int newDist;
		// iterate all edges of the polyline
		for (int i = 1; i < pg.getNodeCount(); i++) {
			a = b;
			b = pg.getNodeAsPoint(i);
			if (!a.equals(b) && (newDist = MSMath.dist(p, a, b)) < minDist)
				minDist = newDist;
		}
		return minDist;
	}

	public EditPoint[] getEditPoints(PTGraphicObject go) {
		PTWire w = (PTWire) go;
		int pSize = w.getNodeCount();
		int size = pSize * 2 - 1;
		int i;
		// add change points(nodes)
		EditPoint[] result = new EditPoint[size];
		for (i = 0; i < pSize; i++)
			result[i] = new EditPoint(i + 1, w.getNodeAsPoint(i));
		// add move points(points halfway the edges
		Point a;
		Point b;
		b = w.getNodeAsPoint(0);
		for (i = 1; i < pSize; i++) {
			a = b;
			b = w.getNodeAsPoint(i);
			result[i + pSize - 1] = new EditPoint(-i, new Point(
					(a.x + b.x) / 2, (a.y + b.y) / 2));
		}
		return result;
	} // getEditPoints

	// public void setProperties(XProperties props) {
	// bwArrowCB.setSelected(props.getBoolProperty(PTWire.POLYLINE_TYPE
	// + ".bwArrow"));
	// colorChooser.setColor(props.getColorProperty(PTPolyline.POLYLINE_TYPE
	// + ".color", Color.black));
	// depthBox.setSelectedItem(props.getProperty(PTPolyline.POLYLINE_TYPE
	// + ".depth", "16"));
	// //
	// depthBox.setSelectedItem(props.getProperty(PTGraphicObject.DEPTH_LABEL,
	// // String.valueOf(16)));
	// fwArrowCB.setSelected(props.getBoolProperty(PTPolyline.POLYLINE_TYPE
	// + ".fwArrow"));
	// }

	// public void getProperties(XProperties props) {
	// props
	// .put(PTPolyline.POLYLINE_TYPE + ".bwArrow", bwArrowCB
	// .isSelected());
	// props.put(PTPolyline.POLYLINE_TYPE + ".color", colorChooser.getColor());
	// props.put(PTPolyline.POLYLINE_TYPE + ".depth", depthBox
	// .getSelectedItem());
	// // props.put(PTGraphicObject.DEPTH_LABEL, depthBox.getSelectedItem());
	// props
	// .put(PTPolyline.POLYLINE_TYPE + ".fwArrow", fwArrowCB
	// .isSelected());
	// }

	/**
	 * enable or disable some CheckBoxes according to whether the polyline is
	 * closed or not
	 */
	// public void itemStateChanged(ItemEvent e) {
	// PTWire p = (PTWire) getCurrentObject();
	//
	// if (e.getSource() == fwArrowCB) {
	// if (p != null)
	// p.setFWArrow(fwArrowCB.isSelected());
	// }
	//
	// if (e.getSource() == bwArrowCB) {
	// if (p != null)
	// p.setBWArrow(bwArrowCB.isSelected());
	// }
	// Animation.get().doChange();
	// repaintNow();
	// } // itemStateChanged
	public EditableObject createObject() {
		PTWire w = new PTWire();
		storeAttributesInto(w);
		return w;
	}

	protected void storeAttributesInto(EditableObject eo) {
		super.storeAttributesInto(eo);
		PTWire w = (PTWire) eo;
		w.setWalkSpeed((Integer) speedLabelText.getValue());
	}

	protected void extractAttributesFrom(EditableObject eo) {
		super.extractAttributesFrom(eo);
		PTWire w = (PTWire) eo;
		speedLabelText.setValue(w.getWalkSpeed());
		speedSlider.setValue(w.getWalkSpeed());
	}

	public Editor getSecondaryEditor(EditableObject go) {
		WireEditor result = new WireEditor();
		// important! result must be of type WireEditor(or casted)
		// and the parameter passed must be of type PTWire.
		// Otherwise, not all attributes are copied!
		result.extractAttributesFrom(go);
		return result;
	}

	public String getStatusLineMsg() {
		return AnimalTranslator.translateMessage("WireEditor.statusLine",
				new Object[] { DrawCanvas.translateDrawButton(),
						DrawCanvas.translateFinishButton(),
						DrawCanvas.translateCancelButton() });
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		PTWire p = (PTWire) getCurrentObject();

		if (p != null) {
			if (Animation.get() != null)
				Animation.get().doChange();
			repaintNow();
		}
	}

	public void propertyChange(PropertyChangeEvent event) {
		PTWire poly = (PTWire) getCurrentObject();
		String eventName = event.getPropertyName();
		if ("color".equals(eventName))
			poly.setColor((Color) event.getNewValue());
		if (!event.getOldValue().equals(event.getNewValue())) {
			repaintNow();
			if (Animation.get() != null)
				Animation.get().doChange();
		}
	}

	public String getBasicType() {
		return PTWire.WIRE_TYPE;
	}

	public void mouseClicked(MouseEvent arg0) {
		PTWire w = (PTWire) getCurrentObject();
		if (w != null) {
			int i;
			if (w.getNodeCount() > 2) {
				for (i = 0; i < w.getNodeCount() - 2; i++) {
					if (Math.abs(w.getNodeAt(i).getX()
							- w.getNodeAt(i + 1).getX()) > Math.abs(w
							.getNodeAt(i).getY()
							- w.getNodeAt(i + 1).getY()))
						w.setNode(i + 1, new PTPoint(w.getNodeAt(i + 1).getX(),
								w.getNodeAt(i).getY()));
					else
						w.setNode(i + 1, new PTPoint(w.getNodeAt(i).getX(), w
								.getNodeAt(i + 1).getY()));
				}
				if (Math.abs(w.getNodeAt(i).getX() - w.getNodeAt(i + 1).getX()) > Math
						.abs(w.getNodeAt(i).getY() - w.getNodeAt(i + 1).getY())) {
					PTPoint NewPoint = new PTPoint(w.getNodeAt(i).getX(), w
							.getNodeAt(i + 1).getY());
					w.setNode(i, NewPoint);
				} else {
					PTPoint NewPoint = new PTPoint(w.getNodeAt(i + 1).getX(), w
							.getNodeAt(i).getY());
					w.setNode(i, NewPoint);
				}
			}

			if (Animation.get() != null) {
				Animation.get().doChange();
			}
			repaintNow();
		}
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

	public void keyPressed(KeyEvent ke) {
    // nothing to be done here
	}

	public void keyReleased(KeyEvent ke) {
    // nothing to be done here
	}

	public void keyTyped(KeyEvent arg0) {
    // nothing to be done here
	}
}
