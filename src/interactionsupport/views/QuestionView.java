package interactionsupport.views;

import interactionsupport.controllers.InteractionController;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 * Base class for all question-like interactions.
 * 
 * @author Gina Haeussge, huge(at)rbg.informatik.tu-darmstadt.de, Simon Sprankel
 *         <sprankel@rbg.informatik.tu-darmstadt.de>
 */
public abstract class QuestionView extends InteractionView {

  private static final long serialVersionUID = -1551087083805802638L;

  /** The submit button for submitting the given answer */
  protected JButton         submitButton;

  /**
   * HTMLComponent used for displaying feedback concerning the interactions
   * state to the user
   */
  protected JEditorPane     feedbackView;

  /** used to make the feedbackView scrollable */
  protected JScrollPane     feedbackScroller;

  /** the text area for displaying the question. */
  protected JTextArea       questionOutput;

  /** counts how often this question has been reset */
  protected int             numberOfBuilds   = 0;

  /**
   * Sets up the basic GUI and assigns the question ID.
   */
  public QuestionView(String questionID) {
    super(questionID);
    feedbackView = new JEditorPane("text/html", "");
    feedbackView.setEditable(false);
    feedbackScroller = new JScrollPane(feedbackView,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    feedbackScroller.setPreferredSize(new Dimension(200, 100));
  }

  /**
   * Resets the question.
   */
  public abstract void rebuildQuestion();

  @Override
  public String getTitle() {
    return InteractionController.translateMessage("question");
  }

  /**
   * Sets the text of the feedback label to the given string.
   * 
   * @param feedback
   *          The text that should be displayed.
   */
  public void showFeedback(String feedback) {
    feedbackView.setText(InteractionController.translateMessage("feedbackTemplate",
        new String[] { feedback }));
  }

  /**
   * Sets the text of the feedback label to color black
   */
  public void setFeedbackBlack() {
    feedbackView.setForeground(Color.BLACK);
  }

  /**
   * Sets the text of the feedback label to color red
   */
  public void setFeedbackRed() {
    feedbackView.setForeground(Color.RED);
  }

  /**
   * Retrieve how often GUI has been reseted.
   * 
   * @return how often the GUI has been reseted
   */
  public int getNumberOfBuilds() {
    return numberOfBuilds;
  }

}
