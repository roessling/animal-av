package animal.editor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;

import translator.AnimalTranslator;
import animal.editor.animators.MoveEditor;
import animal.graphics.PTPolyline;

/**
 * Editor for a Polyline
 * 
 * @author <a href="http://www.algoanim.info/Animal2/">Guido R&ouml;&szlig;ling</a>
 * @version 2.0 2001-03-16
 */
public class NodeSelector extends SpecialSelector 
implements ActionListener  {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
//	private static final long serialVersionUID = -1713805631085640216L;
	JFrame displayFrame;
	JToggleButton[] selectedToggleButton;

	JPanel checkBoxPanel;

	PTPolyline polyline;

	private boolean[] selectedNodes;

//	private int nrSelected = 0;

	public NodeSelector(Editor callingEditor, boolean[] nodeMap,
			String methodBaseName, boolean enableMultipleMode) {
		super(callingEditor, methodBaseName, enableMultipleMode);
		displayFrame = new JFrame(AnimalTranslator.translateMessage("nodeSelectTitle"));
		parentEditor = callingEditor;
		selectedNodes = nodeMap;
		baseMethodName = methodBaseName;
		// supportMultiSelection = enableMultipleMode;

		displayFrame.getContentPane().setLayout(new BorderLayout());
		JTabbedPane tp = new JTabbedPane();
		int nrNodes = nodeMap.length;
		checkBoxPanel = new JPanel(new GridLayout(0, (nrNodes < 3) ? nrNodes
				: nrNodes / 3));
		selectedToggleButton = new JCheckBox[nrNodes];
		selectedNodes = new boolean[nrNodes];

		int i;
		if (enableMultipleMode)
			for (i = 0; i < nrNodes; i++) {
				selectedToggleButton[i] = new JCheckBox(AnimalTranslator
						.translateMessage("node")
						+ " " + (i + 1), nodeMap[i]);
			}
		else
			for (i = 0; i < nrNodes; i++) {
				selectedToggleButton[i] = new JRadioButton(AnimalTranslator
						.translateMessage("node")
						+ " " + (i + 1), nodeMap[i]);
			}

		for (i = 0; i < nrNodes; i++) {
//			if (nodeMap[i])
//				nrSelected++;
			checkBoxPanel.add(selectedToggleButton[i]);
		}
		AnimalTranslator.getGUIBuilder().insertTranslatableTab("nodes",
				checkBoxPanel, tp);
		displayFrame.getContentPane().add(tp, BorderLayout.CENTER);
		finish();
	}

	AbstractButton okButton, applyButton, cancelButton;

	/**
	 * adds the last row, containing buttons for OK, apply, Cancel
	 */
	public void finish() {
		GridLayout gridLayout = new GridLayout(1, 0);

		JPanel p = new JPanel(gridLayout);
		p.add(okButton = AnimalTranslator.getGUIBuilder().generateJButton("GenericEditor.ok",
				null, false, this));
		p.add(applyButton = AnimalTranslator.getGUIBuilder().generateJButton(
				"GenericEditor.apply", null, false, this));
		p.add(cancelButton = AnimalTranslator.getGUIBuilder().generateJButton(
				"GenericEditor.cancel", null, false, this));
		if (displayFrame != null) {
		  displayFrame.getContentPane().add(p, BorderLayout.SOUTH);
		  displayFrame.pack();
		  displayFrame.setVisible(true);
		}
	}

	private String currentMethod() {
		StringBuilder localBuffer = new StringBuilder(50);
		localBuffer.append(baseMethodName);
		int i, nrNodes = selectedNodes.length;
		for (i = 0; i < nrNodes; i++)
			if (selectedToggleButton[i].isSelected())
				localBuffer.append(' ').append(i + 1);
		return localBuffer.toString();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == okButton) {
			if (apply()) {
				close();
			}
		} else if (e.getSource() == applyButton)
			apply();
		else if (e.getSource() == cancelButton)
			close();
	}

	boolean apply() {
		((MoveEditor) parentEditor).setNewMethod(currentMethod());
		return true;
	}

	public void close() {
		displayFrame.setVisible(false);
		displayFrame.dispose();
	}
} // NodeSelector

