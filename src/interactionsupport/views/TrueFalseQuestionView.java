package interactionsupport.views;

import interactionsupport.controllers.InteractionController;
import interactionsupport.models.InteractionModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;


/**
 * View class for true false questions.
 * 
 * @author Gina Haeussge <huge(at)rbg.informatik.tu-darmstadt.de>, Simon
 *         Sprankel <sprankel@rbg.informatik.tu-darmstadt.de>, building upon
 *         work by Laura L. Norton
 */
public class TrueFalseQuestionView extends QuestionView implements ActionListener {

  private static final long serialVersionUID = -6477653165768085409L;

  /** {@link ButtonGroup} grouping the two RadioButtons */
  protected ButtonGroup     answerBoxes;

  /** {@link JRadioButton} for "wrong" */
  protected JRadioButton    falseBox;

  /** {@link JRadioButton} for "right" */
  protected JRadioButton    trueBox;

  public TrueFalseQuestionView(String id, InteractionController interactionModule) {
    super(id);
    setInteractionModule(interactionModule);
  }

  @Override
  public String getTitle() {
    return InteractionController.translateMessage("tfQuestion");
  }

  /**
   * Evaluate the answer, disable the submit button.
   * 
   * @param event
   *          the event that occurred
   */
  public void actionPerformed(ActionEvent event) {
    // if no answer has been submitted, do nothing
    if (answerBoxes == null
        || (!trueBox.isSelected() && !falseBox.isSelected()))
      return;

    // an answer has been submitted, so define the answer ID
    String userAnswerID = TrueFalseQuestionModel.answerIDFalse;
    if (trueBox.isSelected()) {
      userAnswerID = TrueFalseQuestionModel.answerIDTrue;
    }
    // set the answer ID on the question model
    TrueFalseQuestionModel questionModel = this.getModel();
    questionModel.setUserAnswerID(userAnswerID);

    // get the comment from the user's answer, so that it can be shown
    submitButton.setEnabled(false);

    interactionModule.processQuestion(id);
  }

  @Override
  public void makeGUI() {
    JLabel headlineLabel;
    JScrollPane questionScroller;
    Icon TFIcon;
    JRadioButton fakeBox;
    JPanel boxPanel;
    JPanel bottomPanel;
    JPanel buttonPanel;

    // sets the layout manager of the object
    setLayout(new BorderLayout());

    // constructs the headline of the question window
    TFIcon = new ImageIcon("TFGraphic.gif");
    headlineLabel = new JLabel(TFIcon, SwingConstants.CENTER);

    // wraps the question, the answer input field and the submit button
    mainPanel = new JPanel(new GridLayout(4, 1, 5, 5));
    mainPanel.setPreferredSize(new Dimension(200, 400));

    // the question, made scrollable using a JScrollPane
    questionOutput = new JTextArea(this.getModel().getPrompt());
    questionScroller = new JScrollPane(questionOutput,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    questionOutput.setEditable(false);
    questionOutput.setWrapStyleWord(true);
    questionOutput.setLineWrap(true);

    // the true/false answer buttons
    answerBoxes = new ButtonGroup();
    trueBox = new JRadioButton(InteractionController.translateMessage("true"),
        false);
    falseBox = new JRadioButton(InteractionController.translateMessage("false"),
        false);
    fakeBox = new JRadioButton(InteractionController.translateMessage("fake"), true);
    answerBoxes.add(trueBox);
    answerBoxes.add(falseBox);
    answerBoxes.add(fakeBox);
    boxPanel = new JPanel(new BorderLayout());
    boxPanel.add(BorderLayout.NORTH, trueBox);
    boxPanel.add(BorderLayout.SOUTH, falseBox);

    // feedback of the system about the current state
    // of answering the question
    showFeedback(InteractionController.translateMessage("noAnswerYet"));
    setFeedbackRed();

    submitButton = new JButton(InteractionController.translateMessage("submit"));
    submitButton.addActionListener(this);

    // panel that takes care of the submit button
    buttonPanel = new JPanel();
    buttonPanel.add(submitButton);

    // lower part of the main panel, consists of
    // buttons and information labels
    bottomPanel = new JPanel(new GridLayout(2, 1));
    bottomPanel.add(buttonPanel);

    // put together what belongs together
    mainPanel.add(questionScroller);
    mainPanel.add(boxPanel);
    mainPanel.add(bottomPanel);
    mainPanel.add(feedbackScroller);
    add(BorderLayout.NORTH, headlineLabel);
    add(BorderLayout.CENTER, mainPanel);

    numberOfBuilds++;
  }

  @Override
  public void rebuildQuestion() {
    trueBox.setSelected(false);
    falseBox.setSelected(false);
    showFeedback("");
    submitButton.setEnabled(true);
  }

  /**
   * Gets the model object of this true false question.
   * 
   * @see TrueFalseQuestionModel
   */
  @Override
  public TrueFalseQuestionModel getModel() {
    InteractionModel interaction = getInteractionModule()
        .getInteractionModels().get(id);
    if (!(interaction instanceof TrueFalseQuestionModel)) {
      System.err.println("Wrong ID to interaction mapping");
      return null;
    }
    return (TrueFalseQuestionModel) interaction;
  }

}
