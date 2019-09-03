/**
 * 
 */
package extras.animalsense.ui.edit;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import extras.animalsense.evaluate.Question;

/**
 * @author Mihail Mihaylov
 *
 */
public class QuestionTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Question> questions;
	

	/**
	 * @param questions
	 */
	public QuestionTableModel(List<Question> questions) {
		super();
		setQuestions(questions);
	}

	/**
	 * 
	 */
	public QuestionTableModel() {
		this(new ArrayList<Question>(0));
	
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int row, int column) {
		Object result;
		if (column == 0)
			result = questions.get(row).getQuestionText();
		else //if (column == 1)
			result = questions.get(row);
//		else //if (column == 2)
//			result =  removeBtns.get(row);
		return result;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return questions.size();
	}
	
	
	 /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class<?> getColumnClass(int c) {
    	if (c == 0)
    		return String.class;
    	else 
    		return Question.class;
    }

	/**
	 * @return the questions
	 */
	public List<Question> getQuestions() {
		return questions;
	}

	/**
	 * @param questions the questions to set
	 */
	public void setQuestions(List<Question> questions) {
		this.questions = questions;		
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		if (column == 0)
			return "Question";
		else
			return "-";
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 0)
			return false;
		return true;
	}
	
	
	
	
	
	
	
}
