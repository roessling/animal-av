package animal.exchange;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import translator.AnimalTranslator;
import animal.misc.MessageDisplay;

public class ExportModeChooser extends JFrame implements ActionListener {
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 4895506508149252074L;

	private JToggleButton fullAnimation, selectedSteps;

	private JToggleButton staticSnapshot, dynamicStep;

	// private JToggleButton normalScaleButton, canvasScaleButton;
	private JComboBox<String> sizeBox, speedBox;

	private JList<String> stepList;

	private double magScale = 1.0, speed = 1.0;

	public static final String[] MAG_SIZES = new String[] { "50%", "71%", "100%",
			"141%", "200%" };

	public static final String[] SPEED_SETTINGS = new String[] { "10%", "25%",
			"50%", "75%", "100%", "150%", "200%", "250%", "500%" };

	public ExportModeChooser(AnimationExporter chosenExporter) {
		super(AnimalTranslator.translateMessage("chooseExMode"));
		getContentPane().setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 2));

		ButtonGroup exportModeGroup = new ButtonGroup();
		boolean canHandleFull = chosenExporter.canExportFullAnimation();

		fullAnimation = AnimalTranslator.getGUIBuilder().generateJToggleButton(
				"fullAnimEx", null, this, true);
		// new JRadioButton("Export full animation");
		fullAnimation.setEnabled(canHandleFull);
		fullAnimation.setSelected(canHandleFull);
		// fullAnimation.addActionListener(this);

		selectedSteps = AnimalTranslator.getGUIBuilder().generateJToggleButton(
				"selectedEx", null, this, true);
		// selectedSteps = new JRadioButton("Export selected step(s)");
		selectedSteps.setSelected(!canHandleFull);
		selectedSteps.setEnabled(chosenExporter.canExportSelectedSteps());
		// selectedSteps.addActionListener(this);

		panel.add(fullAnimation);
		panel.add(selectedSteps);

		exportModeGroup.add(fullAnimation);
		exportModeGroup.add(selectedSteps);

		ButtonGroup exportTypeGroup = new ButtonGroup();
		boolean canHandleDynamicSteps = chosenExporter.canExportDynamicStep();

		dynamicStep = AnimalTranslator.getGUIBuilder().generateJToggleButton(
				"dynamicEx", null, null, true);
		// dynamicStep = new JRadioButton("Dynamic Step");
		dynamicStep.setEnabled(canHandleDynamicSteps);
		dynamicStep.setSelected(canHandleDynamicSteps);

		// staticSnapshot = new JRadioButton("Static Screenshot");
		staticSnapshot = AnimalTranslator.getGUIBuilder().generateJToggleButton(
				"staticEx", null, null, true);
		staticSnapshot.setSelected(!canHandleDynamicSteps);

		panel.add(dynamicStep);
		panel.add(staticSnapshot);

		exportTypeGroup.add(dynamicStep);
		exportTypeGroup.add(staticSnapshot);

		panel.add(AnimalTranslator.getGUIBuilder().generateJLabel("exportMag"));

		boolean canHandleMagnification = chosenExporter.canScaleDisplay();
		JToggleButton defaultSize = AnimalTranslator.getGUIBuilder()
				.generateJToggleButton("normalScale", null, null, true);
		defaultSize.setSelected(true);
		panel.add(defaultSize);

		if (canHandleMagnification) {
			JToggleButton canvasScaleButton = AnimalTranslator.getGUIBuilder()
					.generateJToggleButton("canvasScale", null, null, true);
			panel.add(canvasScaleButton);
			sizeBox = AnimalTranslator.getGUIBuilder().generateJComboBox("exScale",
					null, MAG_SIZES, MAG_SIZES[MAG_SIZES.length >> 1]);
			sizeBox.setEditable(true);
			sizeBox.addActionListener(this);
			panel.add(sizeBox);
			ButtonGroup magnificationGroup = new ButtonGroup();
			magnificationGroup.add(defaultSize);
			magnificationGroup.add(canvasScaleButton);
		}

		panel.add(AnimalTranslator.getGUIBuilder().generateJLabel("exportSpeed"));

		boolean canHandleSpeedSetting = chosenExporter.canAdjustSpeed();
		JToggleButton defaultSpeed = AnimalTranslator.getGUIBuilder()
				.generateJToggleButton("normalSpeed", null, null, true);
		defaultSpeed.setSelected(true);
		panel.add(defaultSpeed);

		if (canHandleSpeedSetting) {
			JToggleButton animSpeedButton = AnimalTranslator.getGUIBuilder()
					.generateJToggleButton("animSpeed", null, null, true);
			panel.add(animSpeedButton);
			speedBox = AnimalTranslator.getGUIBuilder().generateJComboBox("exSpeed",
					null, SPEED_SETTINGS, SPEED_SETTINGS[SPEED_SETTINGS.length >> 1]);
			speedBox.setEditable(true);
			speedBox.addActionListener(this);
			panel.add(speedBox);
			ButtonGroup speedGroup = new ButtonGroup();
			speedGroup.add(defaultSpeed);
			speedGroup.add(animSpeedButton);
		}

		getContentPane().add(panel, BorderLayout.NORTH);

		String[] steps = chosenExporter.getAnimation().getLinkLabels();
		stepList = new JList<String>(steps);
		stepList.setEnabled(!fullAnimation.isSelected());
		getContentPane().add(new JScrollPane(stepList), BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		// JButton okButton = new JButton("OK");
		// okButton.addActionListener(chosenExporter);
		AbstractButton okButton = AnimalTranslator.getGUIBuilder().generateJButton(
				"ok", null, false, chosenExporter);
		buttonPanel.add(okButton);

		AbstractButton cancelButton = AnimalTranslator.getGUIBuilder()
				.generateJButton("cancel", null, false, chosenExporter);
		buttonPanel.add(cancelButton);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		setVisible(true);
		pack();
	}

	public boolean exportFullAnimation() {
		return fullAnimation.isSelected();
	}

	public boolean dynamicStepExport() {
		return dynamicStep.isSelected();
	}

	public int[] getExportSteps() {
		int[] result = null;
		List<String> selected = stepList.getSelectedValuesList(); //getSelectedValues();
		if (selected != null && selected.size() != 0) {
			int nr = selected.size();
			result = new int[nr];
			int pos = 0;
			for (String selectedEntry: selected) {
//			for (int i = 0; i < selected.length; i++) {
				try {
        result[pos++] = Integer.parseInt(selectedEntry.substring(5));
//					result[i] = Integer.parseInt(((String) selected[i]).substring(5));
				} catch (NumberFormatException e) {
					MessageDisplay.message("invalidNumberInput", 
              new String[] { selectedEntry });
//							new String[] { (String)selected[i] });
				}
			}
		}
		return result;
	}

	public double getDisplaySpeed() {
		return speed;
	}

	public double getMagnification() {
		return magScale;
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() instanceof JToggleButton)
			stepList.setEnabled(!fullAnimation.isSelected());
		else if (event.getSource() == sizeBox) {
			String itemBoxValue = (String) sizeBox.getSelectedItem();
			String useThis = itemBoxValue.trim();
			boolean usesPercentage = useThis.endsWith("%");
			if (usesPercentage)
				useThis = useThis.substring(0, useThis.length() - 1);
			try {
				magScale = Double.parseDouble(useThis);
			} catch (NumberFormatException nfe) {
				magScale = 1.0;
				sizeBox.setSelectedItem("100%");
			}
			if (usesPercentage)
				magScale /= 100.0;
		} else if (event.getSource() == speedBox) {
			String itemBoxValue = (String) speedBox.getSelectedItem();
			String useThis = itemBoxValue.trim();
			boolean usesPercentage = useThis.endsWith("%");
			if (usesPercentage)
				useThis = useThis.substring(0, useThis.length() - 1);
			try {
				speed = Double.parseDouble(useThis);
			} catch (NumberFormatException nfe) {
				speed = 1.0;
				speedBox.setSelectedItem("100%");
			}
			if (usesPercentage)
				speed /= 100.0;
		}
	}
}
