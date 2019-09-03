package extras.animalsense.ui.show;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.PrintJob;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import extras.animalsense.evaluate.Question;
import extras.animalsense.ui.ExerciseView;
import extras.animalsense.ui.ExerciseViewListener;
import extras.lifecycle.common.AbstractObservable;
import extras.lifecycle.common.AnimationStepBean;
import extras.lifecycle.common.Event;

public class ExerciseTextPaneView extends AbstractObservable<ExerciseViewListener> implements ExerciseView, QEventListener{
	
  JFrame form;
	private String subTitle;
	private String description;
	private List<Question> questionsList;
	private ShowExercisePane textPane;
	
	public ExerciseTextPaneView() {
		super(ExerciseViewListener.class);
		form = new JFrame();
	}

	@Override
	public void addListener(ExerciseViewListener evl) {
		super.addListener(evl);
	}

	@Override
	public void hide() {
		form.setVisible(false);
	}
	
	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread.
	 */
	void setUpAndShowGUI() {
		// Set up the window.	
		form.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		// Add content to the window.
		JPanel qPanel = createQPanel();
		form.add(qPanel);

    // GR
    initToolbar(qPanel);
    
		// Display the window.
		form.pack();
		form.setVisible(true);
	}

	public void initToolbar(final JPanel targetPanel) {
//	  JToolBar toolBar = new JToolBar();
	  JButton printMe = new JButton("Print");
	  printMe.addActionListener(
	      new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
//	        System.err.println("Pressed " + e.getActionCommand() + " at " +e.getWhen() +"...");
	        PrintJob printJob = targetPanel.getToolkit().getPrintJob(form, "Printed Exercise", null);
	        if (printJob != null) {
	          Graphics g = printJob.getGraphics();
	          targetPanel.printAll(g);
	          printJob.end();
	        }
	        //(form, "Printed Exercise", new JobAttributes(), new PageAttributes());
	        }
	      });
	  targetPanel.add(BorderLayout.NORTH, printMe);
	}
	@Override
	public void updateView() {
		// Schedule a job for the event dispatching thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				setUpAndShowGUI();
			}
		});
	}

	@Override
	public void onEvent(Event event) {
		System.err.println("ExerciseOnEvent " + event);
	}
	
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return form.getTitle();
	}



	/*
	 * (non-Javadoc)
	 * @see extras.animalsense.ui.ExerciseView#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		form.setTitle(title);
	}

	/**
	 * @return the subTitle
	 */
	public String getSubTitle() {
		return subTitle;
	}

	/*
	 * (non-Javadoc)
	 * @see extras.animalsense.ui.ExerciseView#setSubTitle(java.lang.String)
	 */
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/*
	 * (non-Javadoc)
	 * @see extras.animalsense.ui.ExerciseView#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the questionsList
	 */
	public List<Question> getQuestionsList() {
		return this.questionsList;
	}

	/*
	 * (non-Javadoc)
	 * @see extras.animalsense.ui.ExerciseView#setQuestionsList(java.util.List)
	 */
	public void setQuestionsList(List<Question> questionsList) {
		this.questionsList = questionsList;
	}

	@Override
	public void onSetInputForQuestion(SetUpVariablesEvent qe) {
		fireEvent(qe);
	}

	@Override
	public void setAnswer(Question question, String answer) {
		textPane.setComment(question.getQuestionId(), answer);
	}
	
	private JPanel createQPanel() {
		JPanel qPanel = new JPanel();
		qPanel.setLayout(new BorderLayout());

		// Create a text pane.
		textPane = createTextPane();
		JScrollPane paneScrollPane = new JScrollPane(textPane);
		paneScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		paneScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		paneScrollPane.setPreferredSize(new Dimension(600, 400));
		paneScrollPane.setMinimumSize(new Dimension(10, 10));

		qPanel.add(paneScrollPane);
		
		return qPanel;
	}
	
	private ShowExercisePane createTextPane() {
		ShowExercisePane textPane = new ShowExercisePane(this);
		textPane.setTitle(getTitle());
		textPane.setSubtitle(getSubTitle());
		textPane.setDescription(getDescription());
		textPane.setQuestions(getQuestionsList());
		
		// Pack all
		textPane.pack();

		
		return textPane;
	}

	@Override
	public void visualize(Question question, String scriptContent, List<AnimationStepBean> animationSteps) {
		
	}

}
