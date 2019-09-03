package extras.animalsense.ui.edit;

//import generators.framework.GeneratorsMainPanel;

import generators.generatorframe.controller.Starter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

public class EditExerciseJFrameBase extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JTabbedPane tabbedPane = null;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JPanel jPanel2 = null;
	private JPanel jPanel3 = null;
	private JPanel jPanel4 = null;
	private JPanel jPanel5 = null;
	private JScrollPane jScrollPane = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private JPanel jPanel51 = null;
	private JButton jButton2 = null;
	private JButton jButton11 = null;
	private JPanel jPanel6 = null;
	private JButton jButton3 = null;
	private JButton jButton4 = null;
	private JPanel jPanel61 = null;
	private JButton jButton31 = null;
	private JButton jButton41 = null;
	private JEditorPane welcomePane = null;
	private JPanel jPanel7 = null;
	private JPanel jPanel8 = null;
	private JPanel jPanel9 = null;
	private JTextArea titleTextArea = null;
	private JPanel jPanel91 = null;
	private JTextArea subTitleTextArea = null;
	private JPanel jPanel911 = null;
	private JTextArea descriptionTextArea = null;
	private JPanel jPanel10 = null;
	private QuestionsTable questionsTable = null;
	private JPanel jPanel611 = null;
	private JButton jButton311 = null;
	private JButton jButton411 = null;
	private JPanel jPanel71 = null;
	private JPanel generatorsMainPanel = null;
	private JPanel jPanel11 = null;
	private JPanel jPanel711 = null;
	private JPanel jPanel6111 = null;
	private JButton jButton3111 = null;
	private JButton jButton4111 = null;
	private VariablesTable variablesTable = null;
	private JScrollPane jScrollPane1 = null;
	private JScrollPane jScrollPane2 = null;
	private JScrollPane jScrollPane3 = null;
	private JPanel jPanel12 = null;
	private JRadioButton radioBtnSave = null;
	private JRadioButton radioBtnSaveAs = null;
//	private JLabel jLabel = null;
	private JButton jButton5 = null;
	private JLabel saveLabel = null;
	private JScrollPane jScrollPane4 = null;
	private JEditorPane finishPane = null;
	/**
	 * This is the default constructor
	 */
	public EditExerciseJFrameBase() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setSize(800, 700);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle("Exercise Editor");
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
//			jContentPane.setLayout(new BoxLayout(getJContentPane(), BoxLayout.Y_AXIS));
//			jContentPane.add(getTabbedPane(), null);
			
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getTabbedPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}
	

	/**
	 * This method initializes tabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	public JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Welcome", null, getJPanel(), null);
			tabbedPane.addTab("Algorithm", null, getJPanel1(), null);
			tabbedPane.addTab("Parameter", null, getJPanel11(), null);
			tabbedPane.addTab("Description", null, getJPanel2(), null);
			tabbedPane.addTab("Questions", null, getJPanel3(), null);
			tabbedPane.addTab("Finish", null, getJPanel4(), "Save and close the wizard");
		}
		return tabbedPane;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(getJPanel(), BoxLayout.Y_AXIS));
			jPanel.add(getJScrollPane(), null);
			jPanel.add(getJPanel5(), null);
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
			jPanel1.setLayout(new BoxLayout(getJPanel1(), BoxLayout.Y_AXIS));
			jPanel1.add(getJPanel7(), null);
			jPanel1.add(getJPanel51(), null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel2	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setLayout(new BoxLayout(getJPanel2(), BoxLayout.Y_AXIS));
			jPanel2.add(getJPanel8(), null);
			jPanel2.add(getJPanel6(), null);
		}
		return jPanel2;
	}

	/**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			jPanel3 = new JPanel();
			jPanel3.setLayout(new BoxLayout(getJPanel3(), BoxLayout.Y_AXIS));
			jPanel3.add(getJPanel10(), null);
			jPanel3.add(getJPanel61(), null);
		}
		return jPanel3;
	}

	/**
	 * This method initializes jPanel4	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel4() {
		if (jPanel4 == null) {
			jPanel4 = new JPanel();
			jPanel4.setLayout(new BoxLayout(getJPanel4(), BoxLayout.Y_AXIS));
			jPanel4.add(getJPanel71(), null);
			jPanel4.add(getJPanel611(), null);
		}
		return jPanel4;
	}

	/**
	 * This method initializes jPanel5	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel5() {
		if (jPanel5 == null) {
			jPanel5 = new JPanel();
			jPanel5.setLayout(new BoxLayout(getJPanel5(), BoxLayout.X_AXIS));
			jPanel5.setPreferredSize(new Dimension(0, 50));
			jPanel5.add(getJButton(), null);
			jPanel5.add(getJButton1(), null);
		}
		return jPanel5;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getWelcomePane());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Close");
			jButton.setActionCommand("close");
			jButton.addActionListener(this);
		}
		return jButton;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("Next");
			jButton1.setActionCommand("nextAlg");
			jButton1.addActionListener(this);
		}
		return jButton1;
	}

	/**
	 * This method initializes jPanel51	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel51() {
		if (jPanel51 == null) {
			jPanel51 = new JPanel();
			jPanel51.setLayout(new BoxLayout(getJPanel51(), BoxLayout.X_AXIS));
			jPanel51.setPreferredSize(new Dimension(0, 50));
			jPanel51.add(getJButton2(), null);
			jPanel51.add(getJButton11(), null);
		}
		return jPanel51;
	}

	/**
	 * This method initializes jButton2	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton2() {
		if (jButton2 == null) {
			jButton2 = new JButton();
			jButton2.setText("Back");
			jButton2.setActionCommand("back");
			jButton2.addActionListener(this);
		}
		return jButton2;
	}

	/**
	 * This method initializes jButton11	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton11() {
		if (jButton11 == null) {
			jButton11 = new JButton();
			jButton11.setText("Next");
			jButton11.setActionCommand("nextVariables");
			jButton11.addActionListener(this);
		}
		return jButton11;
	}

	/**
	 * This method initializes jPanel6	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel6() {
		if (jPanel6 == null) {
			jPanel6 = new JPanel();
			jPanel6.setLayout(new BoxLayout(getJPanel6(), BoxLayout.X_AXIS));
			jPanel6.setPreferredSize(new Dimension(0, 50));
			jPanel6.add(getJButton3(), null);
			jPanel6.add(getJButton4(), null);
		}
		return jPanel6;
	}

	/**
	 * This method initializes jButton3	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton3() {
		if (jButton3 == null) {
			jButton3 = new JButton();
			jButton3.setText("Back");
			jButton3.setActionCommand("back");
			jButton3.addActionListener(this);
		}
		return jButton3;
	}

	/**
	 * This method initializes jButton4	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton4() {
		if (jButton4 == null) {
			jButton4 = new JButton();
			jButton4.setText("Next");
			jButton4.setActionCommand("nextQst");
			jButton4.addActionListener(this);
		}
		return jButton4;
	}

	/**
	 * This method initializes jPanel61	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel61() {
		if (jPanel61 == null) {
			jPanel61 = new JPanel();
			jPanel61.setLayout(new BoxLayout(getJPanel61(), BoxLayout.X_AXIS));
			jPanel61.setPreferredSize(new Dimension(0, 50));
			jPanel61.add(getJButton31(), null);
			jPanel61.add(getJButton41(), null);
		}
		return jPanel61;
	}

	/**
	 * This method initializes jButton31	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton31() {
		if (jButton31 == null) {
			jButton31 = new JButton();
			jButton31.setText("Back");
			jButton31.setActionCommand("back");
			jButton31.addActionListener(this);
		}
		return jButton31;
	}

	/**
	 * This method initializes jButton41	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton41() {
		if (jButton41 == null) {
			jButton41 = new JButton();
			jButton41.setText("Next");
			jButton41.setActionCommand("nextFn");
			jButton41.addActionListener(this);
		}
		return jButton41;
	}

	/**
	 * This method initializes welcomePane	
	 * 	
	 * @return javax.swing.JEditorPane	
	 */
	public JEditorPane getWelcomePane() {
		if (welcomePane == null) {
			welcomePane = new JEditorPane();
			welcomePane.setEditable(false);
		}
		return welcomePane;
	}

	/**
	 * This method initializes jPanel7	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel7() {
		if (jPanel7 == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.weightx = 1.0;
			jPanel7 = new JPanel();
			jPanel7.setLayout(new GridBagLayout());
			jPanel7.add(getGeneratorsMainPanel(), gridBagConstraints1);
		}
		return jPanel7;
	}

	/**
	 * This method initializes jPanel8	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel8() {
		if (jPanel8 == null) {
			jPanel8 = new JPanel();
			jPanel8.setLayout(new BoxLayout(getJPanel8(), BoxLayout.Y_AXIS));
			jPanel8.add(getJPanel9(), null);
			jPanel8.add(getJPanel91(), null);
			jPanel8.add(getJPanel911(), null);
		}
		return jPanel8;
	}

	/**
	 * This method initializes jPanel9	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel9() {
		if (jPanel9 == null) {
			jPanel9 = new JPanel();
			jPanel9.setLayout(new BorderLayout());
			jPanel9.setPreferredSize(new Dimension(37, 50));
			jPanel9.setBorder(BorderFactory.createTitledBorder(null, "Title", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Lucida Grande", Font.PLAIN, 13), Color.black));
			jPanel9.add(getJScrollPane1(), BorderLayout.CENTER);
		}
		return jPanel9;
	}

	/**
	 * This method initializes titleTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	public JTextArea getTitleTextArea() {
		if (titleTextArea == null) {
			titleTextArea = new JTextArea();
			titleTextArea.setLineWrap(true);
			titleTextArea.setWrapStyleWord(true);
		}
		return titleTextArea;
	}

	/**
	 * This method initializes jPanel91	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel91() {
		if (jPanel91 == null) {
			jPanel91 = new JPanel();
			jPanel91.setLayout(new BorderLayout());
			jPanel91.setPreferredSize(new Dimension(37, 50));
			jPanel91.setBorder(BorderFactory.createTitledBorder(null, "Subtitle", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Lucida Grande", Font.PLAIN, 13), Color.black));
			jPanel91.add(getJScrollPane2(), BorderLayout.CENTER);
		}
		return jPanel91;
	}

	/**
	 * This method initializes subTitleTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	public JTextArea getSubTitleTextArea() {
		if (subTitleTextArea == null) {
			subTitleTextArea = new JTextArea();
			subTitleTextArea.setLineWrap(true);
			subTitleTextArea.setWrapStyleWord(true);
		}
		return subTitleTextArea;
	}

	/**
	 * This method initializes jPanel911	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel911() {
		if (jPanel911 == null) {
			jPanel911 = new JPanel();
			jPanel911.setLayout(new BorderLayout());
			jPanel911.setPreferredSize(new Dimension(37, 400));
			jPanel911.setBorder(BorderFactory.createTitledBorder(null, "Description", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Lucida Grande", Font.PLAIN, 13), Color.black));
			jPanel911.add(getJScrollPane3(), BorderLayout.CENTER);
		}
		return jPanel911;
	}

	/**
	 * This method initializes descriptionTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	public JTextArea getDescriptionTextArea() {
		if (descriptionTextArea == null) {
			descriptionTextArea = new JTextArea();
			descriptionTextArea.setTabSize(20);
			descriptionTextArea.setLineWrap(true);
			descriptionTextArea.setWrapStyleWord(true);
			descriptionTextArea.setPreferredSize(new Dimension(0, 400));
		}
		return descriptionTextArea;
	}

	/**
	 * This method initializes jPanel10	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel10() {
		if (jPanel10 == null) {
			jPanel10 = new JPanel();
			jPanel10.setLayout(new BorderLayout());
			jPanel10.add(getQuestionsTable(), BorderLayout.CENTER);
		}
		return jPanel10;
	}

	/**
	 * This method initializes questionsTable
	 * 	
	 * @return extras.animalsense.ui.component.QuestionsTable	
	 */
	public QuestionsTable getQuestionsTable() {
		if (questionsTable == null) {
			questionsTable = new QuestionsTable();
		}
		return questionsTable;
	}

	/**
	 * This method initializes jPanel611	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel611() {
		if (jPanel611 == null) {
			jPanel611 = new JPanel();
			jPanel611.setLayout(new BoxLayout(getJPanel611(), BoxLayout.X_AXIS));
			jPanel611.setPreferredSize(new Dimension(0, 50));
			jPanel611.add(getJButton311(), null);
			jPanel611.add(getJButton411(), null);
		}
		return jPanel611;
	}

	/**
	 * This method initializes jButton311	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton311() {
		if (jButton311 == null) {
			jButton311 = new JButton();
			jButton311.setText("Back");
			jButton311.setActionCommand("back");
			jButton311.addActionListener(this);
		}
		return jButton311;
	}

	/**
	 * This method initializes jButton411	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton411() {
		if (jButton411 == null) {
			jButton411 = new JButton();
			jButton411.setText("Finish");
			jButton411.setActionCommand("save");
			jButton411.addActionListener(this);
		}
		return jButton411;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// nothing to be done here		
	}

	/**
	 * This method initializes jPanel71	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel71() {
		if (jPanel71 == null) {
			jPanel71 = new JPanel(new BorderLayout());
			jPanel71.add(getJScrollPane4(), BorderLayout.CENTER);
			jPanel71.add(getJPanel12(), BorderLayout.SOUTH);
		}
		return jPanel71;
	}

	/**
	 * This method initializes generatorsMainPanel	
	 * 	
	 * @return generator.GeneratorsMainPanel	
	 */
	public JPanel getGeneratorsMainPanel() {
		if (generatorsMainPanel == null) {
		  Starter temp = new Starter(null);
			generatorsMainPanel = (JPanel) temp.getFrame().getContentPane();
		}
		return generatorsMainPanel;
	}

	/**
	 * This method initializes jPanel11	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel11() {
		if (jPanel11 == null) {
			jPanel11 = new JPanel();
			jPanel11.setLayout(new BoxLayout(getJPanel11(), BoxLayout.Y_AXIS));
			jPanel11.add(getJPanel711(), null);
			jPanel11.add(getJPanel6111(), null);
		}
		return jPanel11;
	}

	/**
	 * This method initializes jPanel711	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel711() {
		if (jPanel711 == null) {
			jPanel711 = new JPanel();
			jPanel711.setLayout(new BorderLayout());
			jPanel711.add(getVariablesTable(), BorderLayout.CENTER);
		}
		return jPanel711;
	}

	/**
	 * This method initializes jPanel6111	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel6111() {
		if (jPanel6111 == null) {
			jPanel6111 = new JPanel();
			jPanel6111.setLayout(new BoxLayout(getJPanel6111(), BoxLayout.X_AXIS));
			jPanel6111.setPreferredSize(new Dimension(0, 50));
			jPanel6111.add(getJButton3111(), null);
			jPanel6111.add(getJButton4111(), null);
		}
		return jPanel6111;
	}

	/**
	 * This method initializes jButton3111	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton3111() {
		if (jButton3111 == null) {
			jButton3111 = new JButton();
			jButton3111.setActionCommand("back");
			jButton3111.setText("Back");
			jButton3111.addActionListener(this);
		}
		return jButton3111;
	}

	/**
	 * This method initializes jButton4111	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton4111() {
		if (jButton4111 == null) {
			jButton4111 = new JButton();
			jButton4111.setActionCommand("nextDscr");
			jButton4111.setText("Next");
			jButton4111.addActionListener(this);
		}
		return jButton4111;
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
	 * This method initializes jScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getTitleTextArea());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes jScrollPane2	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane2() {
		if (jScrollPane2 == null) {
			jScrollPane2 = new JScrollPane();
			jScrollPane2.setViewportView(getSubTitleTextArea());
		}
		return jScrollPane2;
	}

	/**
	 * This method initializes jScrollPane3	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane3() {
		if (jScrollPane3 == null) {
			jScrollPane3 = new JScrollPane();
			jScrollPane3.setViewportView(getDescriptionTextArea());
		}
		return jScrollPane3;
	}

	/**
	 * This method initializes jPanel12	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel12() {
		if (jPanel12 == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.gridy = 1;

			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridy = 1;
			gridBagConstraints21.gridx = 2;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridx = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			jPanel12 = new JPanel();
			jPanel12.setLayout(new GridBagLayout());
			jPanel12.setBorder(BorderFactory.createTitledBorder(null, "Save", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Lucida Grande", Font.PLAIN, 13), Color.black));
			jPanel12.setName("jPanel12");
			jPanel12.add(getRadioBtnSave(), gridBagConstraints);
			jPanel12.add(getRadioBtnSaveAs(), gridBagConstraints2);
			jPanel12.add(getJButton5(), gridBagConstraints21);
			jPanel12.add(getSaveLabel(), gridBagConstraints11);
		}
		return jPanel12;
	}
	
	/**
	 * This method initializes jButton5	
	 * 	
	 * @return javax.swing.JButton	
	 */
	public JLabel getSaveLabel() {
		if (saveLabel == null) {
			saveLabel = new JLabel();
			saveLabel.setText("");
		}
		return saveLabel;
	}

	/**
	 * This method initializes radioBtnSave	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	public JRadioButton getRadioBtnSave() {
		if (radioBtnSave == null) {
			radioBtnSave = new JRadioButton();
			radioBtnSave.setText("Save");
			radioBtnSave.setActionCommand("defaultfile");
			radioBtnSave.addActionListener(this);
		}
		return radioBtnSave;
	}

	/**
	 * This method initializes radioBtnSaveAs	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	public JRadioButton getRadioBtnSaveAs() {
		if (radioBtnSaveAs == null) {
			radioBtnSaveAs = new JRadioButton();
			radioBtnSaveAs.setText("Save as");
			radioBtnSaveAs.setActionCommand("saveas");
			radioBtnSaveAs.addActionListener(this);
			
			ButtonGroup bg = new ButtonGroup();
		    bg.add(radioBtnSaveAs);
		    bg.add(getRadioBtnSave());
		}
		return radioBtnSaveAs;
	}

	/**
	 * This method initializes jButton5	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton5() {
		if (jButton5 == null) {
			jButton5 = new JButton();
			jButton5.setText("Browse");
			jButton5.setActionCommand("browse");
			jButton5.addActionListener(this);
		}
		return jButton5;
	}

	/**
	 * This method initializes jScrollPane4	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane4() {
		if (jScrollPane4 == null) {
			jScrollPane4 = new JScrollPane();
			jScrollPane4.setViewportView(getFinishPane());
		}
		return jScrollPane4;
	}

	/**
	 * This method initializes finishPane	
	 * 	
	 * @return javax.swing.JEditorPane	
	 */
	public JEditorPane getFinishPane() {
		if (finishPane == null) {
			finishPane = new JEditorPane();
			finishPane.setEditable(false);
		}
		return finishPane;
	}


}
