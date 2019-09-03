/**
 * 
 */
package extras.animalsense.ui.edit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import extras.animalsense.ui.show.ShowExercisePane;

/**
 * @author Mihail Mihaylov
 *
 */
public class QuestionEditJDlgBase extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JPanel descriptionPanel = null;
	private JPanel variablesPanel = null;
	private JPanel controlPanel = null;
	private JButton okButton = null;
	private JButton cancelButton = null;
	private JTextArea scriptTextArea = null;
	private JTextArea questionTextArea = null;
	private JScrollPane jScrollPane = null;
	private JScrollPane scriptScrollPane = null;
	private JPanel jPanel4 = null;
	private ConsolePane consolePreview = null;
	private ShowExercisePane showExercisePane = null;
	private VariablesTable variablesTable = null;
	private JSplitPane jSplitPane2 = null;
	private JSplitPane jSplitPane3 = null;
	private JSplitPane jSplitPane4 = null;
	private JPanel jPanel6 = null;
	private JPanel jPanel7 = null;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JButton compileButton = null;
	private JScrollPane jScrollPane1 = null;
	/**
	 * @param owner
	 */
	public QuestionEditJDlgBase(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(800, 600);
		this.setTitle("Question Editor");
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(600, 600));
		this.setMinimumSize(new Dimension(400, 400));
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJSplitPane2(), BorderLayout.CENTER);
			jContentPane.add(getControlPanel(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}
	

	/**
	 * This method initializes descriptionPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDescriptionPanel() {
		if (descriptionPanel == null) {
			descriptionPanel = new JPanel();
			descriptionPanel.setLayout(new BoxLayout(getDescriptionPanel(), BoxLayout.X_AXIS));
			descriptionPanel.setBorder(BorderFactory.createTitledBorder(null, "Question in a text form", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Lucida Grande", Font.PLAIN, 13), Color.black));
			descriptionPanel.setPreferredSize(new Dimension(400, 80));
			descriptionPanel.add(getJScrollPane(), null);
		}
		return descriptionPanel;
	}

	/**
	 * This method initializes variablesPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getVariablesPanel() {
		if (variablesPanel == null) {
			variablesPanel = new JPanel();
			variablesPanel.setLayout(new BoxLayout(getVariablesPanel(), BoxLayout.Y_AXIS));
			variablesPanel.setBorder(BorderFactory.createTitledBorder(null, "Variables", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Lucida Grande", Font.PLAIN, 13), Color.black));
			variablesPanel.setPreferredSize(new Dimension(466, 160));
			variablesPanel.add(getVariablesTable(), null);
		}
		return variablesPanel;
	}

	/**
	 * This method initializes controlPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getControlPanel() {
		if (controlPanel == null) {
			controlPanel = new JPanel();
			controlPanel.setLayout(new FlowLayout());
			controlPanel.add(getOkButton(), null);
			controlPanel.add(getCancelButton(), null);
		}
		return controlPanel;
	}

	/**
	 * This method initializes okButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("OK");
			okButton.setActionCommand("save");
			okButton.addActionListener(this);
		}
		return okButton;
	}

	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.setActionCommand("close");
			cancelButton.addActionListener(this);
		}
		return cancelButton;
	}

	/**
	 * This method initializes scriptTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	public JTextArea getScriptTextArea() {
		if (scriptTextArea == null) {
			scriptTextArea = new JTextArea();
			scriptTextArea.setRows(20);
		}
		return scriptTextArea;
	}

	/**
	 * This method initializes questionTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	public JTextArea getQuestionTextArea() {
		if (questionTextArea == null) {
			questionTextArea = new JTextArea();
			questionTextArea.setRows(2);
		}
		return questionTextArea;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new Dimension(4, 60));
			jScrollPane.setViewportView(getQuestionTextArea());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes scriptScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScriptScrollPane() {
		if (scriptScrollPane == null) {
			scriptScrollPane = new JScrollPane();
			scriptScrollPane.setBorder(BorderFactory.createTitledBorder(null, "Script", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Lucida Grande", Font.PLAIN, 13), Color.black));
			scriptScrollPane.setPreferredSize(new Dimension(12, 400));
			scriptScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scriptScrollPane.setViewportView(getScriptTextArea());
		}
		return scriptScrollPane;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This method initializes jPanel4	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel4() {
		if (jPanel4 == null) {
			jPanel4 = new JPanel();
			jPanel4.setLayout(new BorderLayout());
			jPanel4.setPreferredSize(new Dimension(400, 16));
			jPanel4.add(getScriptScrollPane(), BorderLayout.CENTER);
			jPanel4.add(getJPanel1(), BorderLayout.SOUTH);
		}
		return jPanel4;
	}

	/**
	 * This method initializes consolePreview	
	 * 	
	 * @return extras.animalsense.ui.edit.ConsolePane	
	 */
	protected ConsolePane getConsolePreview() {
		if (consolePreview == null) {
			consolePreview = new ConsolePane();
		}
		return consolePreview;
	}

	/**
	 * This method initializes showExercisePane	
	 * 	
	 * @return extras.animalsense.ui.textpane.ShowExercisePane	
	 */
	public ShowExercisePane getShowExercisePane() {
		if (showExercisePane == null) {
			showExercisePane = new ShowExercisePane();
		}
		return showExercisePane;
	}

	/**
	 * This method initializes variablesTable	
	 * 	
	 * @return extras.animalsense.ui.edit.VariablesTable	
	 */
	public VariablesTable getVariablesTable() {
		if (variablesTable == null) {
			variablesTable = new VariablesTable();
		}
		return variablesTable;
	}

	/**
	 * This method initializes jSplitPane2	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane2() {
		if (jSplitPane2 == null) {
			jSplitPane2 = new JSplitPane();
			jSplitPane2.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPane2.setBottomComponent(getJSplitPane4());
			jSplitPane2.setTopComponent(getJSplitPane3());
		}
		return jSplitPane2;
	}

	/**
	 * This method initializes jSplitPane3	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane3() {
		if (jSplitPane3 == null) {
			jSplitPane3 = new JSplitPane();
			jSplitPane3.setLeftComponent(getJPanel6());
			jSplitPane3.setRightComponent(getJPanel7());
		}
		return jSplitPane3;
	}

	/**
	 * This method initializes jSplitPane4	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane4() {
		if (jSplitPane4 == null) {
			jSplitPane4 = new JSplitPane();
			jSplitPane4.setLeftComponent(getJPanel4());
			jSplitPane4.setRightComponent(getConsolePreview());
		}
		return jSplitPane4;
	}

	/**
	 * This method initializes jPanel6	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel6() {
		if (jPanel6 == null) {
			jPanel6 = new JPanel();
			jPanel6.setLayout(new BoxLayout(getJPanel6(), BoxLayout.Y_AXIS));
			jPanel6.setPreferredSize(new Dimension(400, 240));
			jPanel6.add(getDescriptionPanel(), null);
			jPanel6.add(getVariablesPanel(), null);
		}
		return jPanel6;
	}

	/**
	 * This method initializes jPanel7	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel7() {
		if (jPanel7 == null) {
			jPanel7 = new JPanel();
			jPanel7.setLayout(new BorderLayout());
			jPanel7.add(getJPanel(), BorderLayout.CENTER);
		}
		return jPanel7;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.setBorder(BorderFactory.createTitledBorder(null, "Preview", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Lucida Grande", Font.PLAIN, 13), Color.black));
			
			jPanel.add(getJScrollPane1(), BorderLayout.CENTER);
		}
		return jPanel;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BoxLayout(getJPanel1(), BoxLayout.X_AXIS));
			jPanel1.add(getCompileButton(), null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes compileButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCompileButton() {
		if (compileButton == null) {
			compileButton = new JButton();
			compileButton.setPreferredSize(new Dimension(120, 29));
			compileButton.setText("Compile");
			compileButton.setActionCommand("compile");
			compileButton.addActionListener(this);
		}
		return compileButton;
	}

	/**
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			jScrollPane1.setViewportView(getShowExercisePane());
		}
		return jScrollPane1;
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"
