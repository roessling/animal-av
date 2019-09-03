package interactionsupport.views;

import interactionsupport.controllers.InteractionController;
import interactionsupport.models.AnswerModel;
import interactionsupport.models.InteractionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Vector;

import javax.swing.BoxLayout;
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
 * View class for multiple choice questions.
 * 
 * @author Gina Haeussge <huge(at)rbg.informatik.tu-darmstadt.de>, Simon
 *         Sprankel <sprankel@rbg.informatik.tu-darmstadt.de>, building upon
 *         work by Laura L. Norton
 */
public class MultipleChoiceQuestionView extends QuestionView implements ActionListener {

  private static final long    serialVersionUID = 1254727069355954104L;

  /**
   * a {@link Vector} containing the {@link JRadioButton} objects representing
   * the answer possibilities
   */
  private Vector<JRadioButton> answers;

  /** permutation function */
  private Vector<String>       permutation;

  public MultipleChoiceQuestionView(String id, InteractionController interactionModule) {
    super(id);
    setInteractionModule(interactionModule);
    randomize();
  }

  /**
   * Calling this method permutes the indices of the answers. The internal order
   * of them is not changed by this method, a permutation-lookup-table is
   * created instead to be used later when accessing the answers. The used
   * "algorithm" can be looked up at
   * http://java.sun.com/docs/books/tutorial/collections
   * /interfaces/list.html#shuffle
   */
  public void randomize() {
    if (permutation == null) {
      permutation = new Vector<String>();
      // add every answer ID to the permutation vector
      for (AnswerModel answer : this.getModel().getAnswers()) {
        permutation.add(answer.getID());
      }
    }
    // randomize the order of the possible answers
    int i;
    int j;
    String swap;
    Random randomizer = new Random();

    // permute the order of the indices
    for (i = (permutation.size() - 1); i > 0; i--) {
      // create a new random index
      j = randomizer.nextInt(i);

      // save element at the j'th position
      swap = permutation.elementAt(j);

      // copy the i'th element at the j'th position
      permutation.setElementAt(permutation.elementAt(i), j);

      // insert the saved former j'th element into position i
      permutation.setElementAt(swap, i);
    }
  }

  @Override
  public String getTitle() {
    return InteractionController.translateMessage("mcQuestion");
  }

  /**
   * Evaluate the answer, disable the submit button.
   * 
   * @param event
   *          the event that occurred
   */
  public void actionPerformed(ActionEvent event) {
    MultipleChoiceQuestionModel questionModel = this.getModel();

    String userAnswerID = null;
    for (int i = 0; i < answers.size(); i++) {
      // was this answer selected?
      if (answers.elementAt(i).isSelected()) {
        // set the answer ID in the question model
        userAnswerID = permutation.get(i);
        questionModel.setUserAnswerID(userAnswerID);
      }
    }
    // if nothing has been selected, do nothing
    if (userAnswerID == null) {
      return;
    }

    submitButton.setEnabled(false);
    interactionModule.processQuestion(id);
  }

  @Override
  public void makeGUI() {
    MultipleChoiceQuestionModel questionModel = this.getModel();

    JScrollPane questionScroller;
    Icon mcIcon;
    JLabel headlineLabel;
    JPanel bottomPanel;
    JPanel buttonPanel;
    JPanel answerPanel;
    JPanel questionPanel;
    JTextArea answerArea;
    JScrollPane answerScroller;
    ButtonGroup answerButtons = new ButtonGroup();
    JRadioButton choice;

    int i;

    answers = new Vector<JRadioButton>(permutation.size());

    // sets the layout manager of the object
    setLayout(new BorderLayout());
    // setLayout(new FlowLayout());

    // constructs the headline of the question window
    mcIcon = new ImageIcon("MCGraphic.gif");
    headlineLabel = new JLabel(mcIcon, SwingConstants.CENTER);

    // wraps the question, the answer input field and the submit button
    mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setPreferredSize(new Dimension(200, 400));
    questionPanel = new JPanel();
    questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));

    // the question, made scrollable using a JScrollPane
    questionOutput = new JTextArea(this.getModel().getPrompt());
    questionOutput.setEditable(false);
    questionOutput.setWrapStyleWord(true);
    questionOutput.setLineWrap(true);
    questionScroller = new JScrollPane(questionOutput,
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    mainPanel.add(questionScroller);

    // construct choices
    for (i = 0; i < permutation.size(); i++) {
      choice = new JRadioButton("#" + (new Integer(i + 1)).toString(), false);

      // create radio box
      answers.addElement(choice);
      // add them to the same button group, so that only one button can be
      // selected
      answerButtons.add(choice);

      // create answer text
      answerArea = new JTextArea(questionModel
          .getAnswer(permutation.elementAt(i)).getText().trim());
      answerArea.setWrapStyleWord(true);
      answerArea.setLineWrap(true);
      answerArea.setEditable(false);
      answerScroller = new JScrollPane(answerArea,
          ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
          ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

      // create answer panel
      answerPanel = new JPanel(new BorderLayout());
      answerPanel.add(BorderLayout.WEST, choice);
      answerPanel.add(BorderLayout.CENTER, answerScroller);

      // add answer to main panel
      questionPanel.add(answerPanel);
    }
    mainPanel.add(questionPanel);

    // panel that takes care of the submit button
    buttonPanel = new JPanel();
    submitButton = new JButton(InteractionController.translateMessage("submit"));
    submitButton.addActionListener(this);

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
    mainPanel.add(buttonPanel);
    mainPanel.add(feedbackScroller);
    add(BorderLayout.NORTH, headlineLabel);
    add(BorderLayout.CENTER, mainPanel);

    numberOfBuilds++;
  }

  @Override
  public void rebuildQuestion() {
    randomize();
    int i;

    for (i = 0; i < answers.size(); i++) {
      answers.elementAt(i).setSelected(false);
    }

    showFeedback("");
    submitButton.setEnabled(true);
  }

  /**
   * Gets the model object of this multiple choice question.
   * 
   * @see MultipleChoiceQuestionModel
   */
  @Override
  public MultipleChoiceQuestionModel getModel() {
    InteractionModel interaction = getInteractionModule()
        .getInteractionModels().get(id);
    if (!(interaction instanceof MultipleChoiceQuestionModel)) {
      System.err.println("Wrong ID to interaction mapping");
      return null;
    }
    return (MultipleChoiceQuestionModel) interaction;
  }

}
