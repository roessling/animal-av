package interactionsupport.views;

import interactionsupport.controllers.InteractionController;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.InteractionModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;


/**
 * View class for fill in blanks questions.
 * 
 * @author Gina Haeussge <huge(at)rbg.informatik.tu-darmstadt.de>, Simon
 *         Sprankel <sprankel@rbg.informatik.tu-darmstadt.de>, building upon
 *         work by Laura L. Norton
 */
public class FillInBlanksQuestionView extends QuestionView implements ActionListener {

  private static final long serialVersionUID = 1158564530027926630L;

  /** the text field for answer input */
  protected JTextField      answerInput;

  public FillInBlanksQuestionView(String id, InteractionController interactionModule) {
    super(id);
    setInteractionModule(interactionModule);
  }

  @Override
  public String getTitle() {
    return InteractionController.translateMessage("fibQuestion");
  }

  /**
   * Evaluate the answer, disable the submit button.
   * 
   * @param event
   *          the event that occurred
   */
  public void actionPerformed(ActionEvent event) {
    // get the user answer and process it
    String answerFromBlank = (answerInput.getText()).trim();
    // if nothing has been entered, do nothing
    if (answerFromBlank.equals("")) {
      return;
    }

    FillInBlanksQuestionModel fibQuestion = this.getModel();
    String userAnswerId = fibQuestion.getAnswerID(answerFromBlank);
    fibQuestion.setUserAnswerID(userAnswerId);

    // disable the submit button
    submitButton.setEnabled(false);
    interactionModule.processQuestion(id);
  }

  @Override
  public void makeGUI() {
    JScrollPane questionScroller;
    Icon fibIcon;
    JLabel headlineLabel;
    JPanel fieldPanel;
    JPanel buttonPanel;
    JPanel bottomPanel;

    // sets the layout manager of the object
    setLayout(new BorderLayout());

    // constructs the headline of the question window
    fibIcon = new ImageIcon("FIBGraphic.gif");
    headlineLabel = new JLabel(fibIcon, SwingConstants.CENTER);

    // wraps the question, the answer input field and the
    // submit button
    mainPanel = new JPanel(new GridLayout(4, 1, 5, 5));
    mainPanel.setPreferredSize(new Dimension(200, 400));

    // the question, made scrollable using a JScrollPane
    String questionText = this.getModel().getPrompt();
    questionOutput = new JTextArea(questionText);
    questionOutput.setEditable(false);
    questionOutput.setWrapStyleWord(true);
    questionOutput.setLineWrap(true);
    questionScroller = new JScrollPane(questionOutput,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    // the input field for the answer, described by a label
    answerInput = new JTextField();
    fieldPanel = new JPanel(new GridLayout(2, 1));
    fieldPanel.add(new JLabel(InteractionController.translateMessage("enterHere")));
    fieldPanel.add(answerInput);

    // add them to our main panel
    mainPanel.add(questionScroller);
    mainPanel.add(fieldPanel);

    submitButton = new JButton(InteractionController.translateMessage("submit"));
    submitButton.addActionListener(this);

    // panel that takes care of the submit button
    buttonPanel = new JPanel();
    buttonPanel.add(submitButton);

    // feedback of the system about the current state
    // of answering the question
    showFeedback(InteractionController.translateMessage("noAnswerYet"));
    setFeedbackRed();

    // lower part of the main panel, consists of
    // buttons and information labels
    bottomPanel = new JPanel(new GridLayout(2, 1));
    bottomPanel.add(buttonPanel);

    // put together what belongs together
    mainPanel.add(bottomPanel);
    mainPanel.add(feedbackScroller);
    add(BorderLayout.NORTH, headlineLabel);
    add(BorderLayout.CENTER, mainPanel);

    numberOfBuilds++;
  }

  @Override
  public void rebuildQuestion() {
    answerInput.setText("");
    showFeedback("");
    submitButton.setEnabled(true);
  }

  /**
   * Gets the model object of this documentation.
   * 
   * @see FillInBlanksQuestionModel
   */
  @Override
  public FillInBlanksQuestionModel getModel() {
    InteractionModel interaction = getInteractionModule()
        .getInteractionModels().get(id);
    if (!(interaction instanceof FillInBlanksQuestionModel)) {
      System.err.println("Wrong ID to interaction mapping");
      return null;
    }
    return (FillInBlanksQuestionModel) interaction;
  }

}
