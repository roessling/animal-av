/**
 * 
 */
package extras.animalsense.ui.edit;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import extras.animalsense.evaluate.Exercise;
import extras.animalsense.evaluate.Question;


/**
 * @author Mihail Mihaylov
 *
 */
public class QuestionsTable extends QuestionsTableBase implements ActionListener {

	private Exercise exercise;
	
	private Question question;

	private Frame frame;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6307385098917933334L;

	/**
	 */
	public QuestionsTable() {
		super();
	}

	public void init() {
		ActionOnCellEditor editAction = new ActionOnCellEditor("edit", "Edit", this);
		getQTable().getColumnModel().getColumn(1).setCellEditor(editAction);
		getQTable().getColumnModel().getColumn(1).setCellRenderer(editAction);
		getQTable().getColumnModel().getColumn(1).setPreferredWidth(80);
		getQTable().getColumnModel().getColumn(1).setMinWidth(60);
		getQTable().getColumnModel().getColumn(1).setMaxWidth(100);
		
		ActionOnCellEditor removeAction = new ActionOnCellEditor("remove", "Remove", this);
		getQTable().getColumnModel().getColumn(2).setCellEditor(removeAction);
		getQTable().getColumnModel().getColumn(2).setCellRenderer(removeAction);
		getQTable().getColumnModel().getColumn(2).setPreferredWidth(80);
		getQTable().getColumnModel().getColumn(2).setMinWidth(60);
		getQTable().getColumnModel().getColumn(2).setMaxWidth(100);
		
	}
	
	public void update() {
		this.getQTable().setModel(new QuestionTableModel(exercise.getQuestionsList()));
		init();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object eventSrc = e.getSource();
		if (eventSrc instanceof Question)
			question = (Question) eventSrc;
		
		if ("addNew".equals(e.getActionCommand()))
			addNew();
		else if ("edit".equals(e.getActionCommand()))
			edit();
		else if ("remove".equals(e.getActionCommand()))
			remove();
	}
	private void addNew() {
		if (exercise.getQuestionsList() == null)
			return;
		Question q = new Question();
		q.generateDefault();
		edit(q);
	}	

	/**
	 * @return the frame
	 */
	public Frame getFrame() {
		return frame;
	}

	/**
	 * @param frame the frame to set
	 */
	public void setFrame(Frame frame) {
		this.frame = frame;
	}

	private void remove() {
		//default icon, custom title
		int n = JOptionPane.showConfirmDialog(
		    this,
		    "Are you sure you want to remove this question?",
		    "Remove Question?",
		    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		System.err.println("Remove: " + n);
		if (n == JOptionPane.YES_OPTION) {
			exercise.getQuestionsList().remove(question);
			update();
			
		}
	}
	private void edit() {
		edit(this.question);
	}
	
	private void edit(Question q) {
		QuestionEditDlg questionDlg = new QuestionEditDlg(frame, exercise);
		boolean save = questionDlg.edit(q);
		
		if (save) {
			// Add it if it was a new question and is not already in the list
			if (!exercise.getQuestionsList().contains(q)) 
				exercise.getQuestionsList().add(q);
			
			update();			
		}
	}

	/**
	 * @return the exercise
	 */
	public Exercise getExercise() {
		return exercise;
	}

	/**
	 * @param exercise the exercise to set
	 */
	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}
	
	
}
