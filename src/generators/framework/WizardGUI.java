/*
 * Created on 08.04.2005 by T. Ackermann
 */
package generators.framework;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import translator.Translator;

/**
 * WizardGUI is a class that allows to easily write Wizard-like GUIs. When the
 * class is used, it displays a frame with custom Panels. A listener
 * (WizardGUIListener) is provided that can be used to react on user input like
 * "next", "previous" or "exit". The labels of the buttons can be get/set
 * because the members (btnBack, btnNext) are public.
 *
 * @author T. Ackermann
 */
public class WizardGUI extends WindowAdapter implements ActionListener {
	
	private JFrame frame;
	
//	/** we store this because WizardGUI is serializable */
//	private static final long serialVersionUID = 3690754016438989111L;

	/** stores the JPanel for the Wizard-Panels */
	private JPanel panelContent = new JPanel();

	/** stores the Back Button */
	public JButton btnBack;

	/** stores the Next Button */
	public AbstractButton btnNext;

	/** stores the Exit Button */
	private JButton btnExit;
	
	/** stores the Label that provides the helping text */
	private JLabel lblHelp;
	
	/** stores the index of the current step */
	private int currentStep = 0;
	
	/** stores the number of steps */
	private int numberOfSteps = 0;
	
	/** stores the Help Strings */
	private String[] strHelp;
	
	/** stores the content Components */
	private JComponent[] content;
	
	/** stores the WizardGUIListener */
	private WizardGUIListener listener;
	
	private Translator translator;
	
	/**
	 * Constructor
	 * creates a new WizardGUI-Object.
	 * @param newNumberOfSteps The number of steps for the Wizard.
	 */
	public WizardGUI(int newNumberOfSteps) {
		super();
		int theStepNr = newNumberOfSteps;
		if (theStepNr < 1)
		  theStepNr = 1;
		numberOfSteps = newNumberOfSteps;
		strHelp = new String[theStepNr];
		content = new JComponent[theStepNr];
		listener = null;
		frame = new JFrame();
	}
	
	/**
	 * displayWizard creates and sets up the JFrame and shows the Wizard,
	 * with the first step open.
	 */
	public void displayWizard() {
	/*
		try {
	        UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) { 
	    	// nothing done here
	    }
	    */
//		System.err.println(UIManager.getSystemLookAndFeelClassName());
	  
		
		
		// create and set up the window
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		BorderLayout borderLayout = new BorderLayout(0, 1);
		frame.getContentPane().setLayout(borderLayout);
		frame.getContentPane().setBackground(Color.black);

		JPanel panelHelp = new JPanel();
		panelHelp.setPreferredSize(new Dimension(600, 60));
		panelHelp.setBackground(Color.white);
		
		lblHelp = new JLabel(strHelp[0]);
		lblHelp.setFont(new Font("SansSerif", Font.PLAIN, 12));
		panelHelp.add(lblHelp);

		frame.getContentPane().add(panelHelp, BorderLayout.PAGE_START);

		panelContent.setPreferredSize(new Dimension(600, 300));

		// we use a card layout to switch between the Panels
		panelContent.setLayout(new CardLayout(8, 8));
		frame.getContentPane().add(panelContent, BorderLayout.CENTER);

		// create the Content Components
		for (int i = 0; i < numberOfSteps; i++) {
			if (content[i] == null)
				throw new IllegalArgumentException(translator.translateMessage("illegalPanel",
						new String[]{Integer.toString(i)}));
			panelContent.add(content[i], Integer.toString(i));
		}
		
		JPanel panelButtons = new JPanel();
		panelButtons.setLayout(new FlowLayout(FlowLayout.TRAILING, 8, 2));
		btnBack = new JButton(translator.translateMessage("backButton"));
		btnBack.setActionCommand("back");
		btnBack.setMnemonic('b');
		btnBack.setEnabled(false);
		panelButtons.add(btnBack);
		btnBack.addActionListener(this);

		btnNext = translator.getGenerator().generateJButton("confirm", null,
				false, this);
//		btnNext = new JButton(translator.translateMessage("nextButton")); //was: nextButton
		btnNext.setActionCommand("next");
//		btnNext.setMnemonic('c');
		frame.getRootPane().setDefaultButton((JButton)btnNext);
		btnNext.setEnabled(true);
		panelButtons.add(btnNext);
//		btnNext.addActionListener(this);

		btnExit = new JButton(translator.translateMessage("exit"));
		btnExit.setActionCommand("exit");
		btnExit.setMnemonic('a');
		panelButtons.add(btnExit);
		btnExit.addActionListener(this);

		frame.getContentPane().add(panelButtons, BorderLayout.PAGE_END);

		// size all components
		frame.setResizable(true);
		frame.getRootPane().setMinimumSize(new Dimension(600, 400));
		frame.setSize(600, 440);

		// center the Frame
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		frame.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);

		// show first step
		showStep(0);
		// display the window
		frame.setVisible(true);
	}
	
	
	/**
	 * showStep shows the step with the number index (zero-based).
	 * @param index the index of the step that should be shown (zero-based).
	 */
	public void showStep(int index) {
		if (index < 0 || index >= numberOfSteps) return;
		
		lblHelp.setText(strHelp[index]);
		
		CardLayout cl = (CardLayout) (panelContent.getLayout());
		cl.show(panelContent, Integer.toString(index));

		btnBack.setEnabled(index > 0);
		//btnNext.setEnabled(index > 0);

//		btnNext.setText(translator.translateMessage("step" + index));
//		System.err.print("btnNext: " +btnNext.getText());
////		 show the correct buttons
//		if (index < (numberOfSteps - 1)) {
//			btnNext.setText(translator.translateMessage("nextButton"));
//			btnNext.setMnemonic('n');
//		} else {
////			btnNext.setText(translator.translateMessage("finish"));
//			btnNext.setMnemonic('f');
//		}
//		btnNext.revalidate();
//		System.err.println(", now: " +btnNext.getText());

		currentStep = index;
		
		// call the listener
		if (listener != null)
			listener.afterShowStep(index);
	}
	

//	 ****************************************************************************
//	  BELOW ARE THE LISTENERS
//****************************************************************************
	
	/**
	 * setListener sets the listener that is informed, when the step is changed
	 * or when the buttons are pressed.
	 * @param newListener The new Listener.
	 */
	public void setListener(WizardGUIListener newListener) {	
		listener = newListener;
	}
	
	/**
	 * removeListener removes an existing Listener.
	 */
	public void removeListener() {
		listener = null;
	}
	
	/** (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e == null)
			return;
		
		if (e.getActionCommand().equalsIgnoreCase("exit"))
			frame.dispose();
		
		if (e.getActionCommand().equalsIgnoreCase("back")) {
			// call the listener
			boolean doit = true;
			if (listener != null)
				doit = listener.backPressed();
			if (!doit) return;
			
			// show the next step
			if (currentStep > 0)
				showStep(currentStep - 1);
			return;
		}

		if (e.getActionCommand().equalsIgnoreCase("next")) {
			// call the listener
			boolean doit = true;
			if (listener != null)
				doit = listener.nextPressed();
			if (!doit) return;
			
			// show the previous step
			if (currentStep < numberOfSteps - 1)
				showStep(currentStep + 1);
			return;
		}
	}
	
	public void windowClosing(WindowEvent closingEvent) {
		frame.setVisible(false);
		frame.dispose();
	}
	
	protected void setVisible(boolean isVisible) {
		frame.setVisible(isVisible);
	}
	
	public JFrame getWizardFrame() {
		return frame;
	}

// ****************************************************************************
//	  BELOW ARE THE GETTERS AND SETTERS
// ****************************************************************************
	
	/**
	 * getCurrentStep
	 * returns the index of the currently displayed step.
	 * @return The index of the currently displayed step.
	 */
	public int getCurrentStep() {
		return currentStep;
	}
	
	/**
	 * getContent
	 * returns the JComponent for the given step-index or null if the index
	 * is greater than the last index.
	 * @param index The index of the step.
	 * @return The JComponent for the given step-index.
	 */
	public JComponent getContent(int index) {
		if (index < 0 || index >= numberOfSteps) return null;
		if (content ==  null) return null;
		return content[index];
	}
	
	/**
	 * setContent
	 * sets the JComponent for the given step-index.
	 * @param index The index of the step.
	 * @param newContent The new JComponent for the given step-index.
	 */
	public void setContent(int index, JComponent newContent) {
		if (index < 0 || index >= numberOfSteps) return;
		if (content ==  null) return;
		content[index] = newContent;
	}
	
	/**
	 * getNumberOfSteps
	 * returns the number of steps in the Wizard.
	 * @return The number of steps in the Wizard.
	 */
	public int getNumberOfSteps() {
		return numberOfSteps;
	}
	
	
	/**
	 * getHelpString
	 * returns the Help-String for the given step-index or null if the index is
	 * greater than the last index.
	 * @param index The index of the step.
	 * @return The Help-String for the given step-index.
	 */
	public String getHelpString(int index) {
		if (index < 0 || index >= numberOfSteps) return null;
		if (strHelp == null) return null;
		return strHelp[index];
	}
	
	/**
	 * setHelpString
	 * sets the Help-String for the given step-index.
	 * @param index The index of the step.
	 * @param newHelpString The new Help-String for the given step-index.
	 */
	public void setHelpString(int index, String newHelpString) {
		if (index < 0 || index >= numberOfSteps) return;
		if (newHelpString == null) return;
		if (strHelp == null) return;
		strHelp[index] = newHelpString;
	}
	
	public void setTranslator(Translator trans) {
		translator = trans;
	}
}