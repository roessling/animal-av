/*
 * Created on 16.11.2007 by Guido Roessling <roessling@acm.org>
 */
package algoanim.animalscript;

import interactionsupport.models.AnswerModel;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.HtmlDocumentationModel;
import interactionsupport.models.InteractionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import interactionsupport.models.generators.InteractiveElementGenerator;

public class AnimalJHAVETextInteractionGenerator extends AnimalGenerator
    implements InteractiveElementGenerator {

  private boolean startQuestionsPlaced = false;

  public AnimalJHAVETextInteractionGenerator(AnimalScript as) {
    super(as);
  }

  @Override
  public void createInteraction(InteractionModel interaction) {
    if (interaction instanceof TrueFalseQuestionModel)
      createTFQuestionCode((TrueFalseQuestionModel) interaction);
    else if (interaction instanceof MultipleChoiceQuestionModel)
      createMCQuestionCode((MultipleChoiceQuestionModel) interaction);
    else if (interaction instanceof MultipleSelectionQuestionModel)
      createMSQuestionCode((MultipleSelectionQuestionModel) interaction);
    else if (interaction instanceof FillInBlanksQuestionModel)
      createFIBQuestionCode((FillInBlanksQuestionModel) interaction);
  }

  public void createTFQuestion(TrueFalseQuestionModel q) {
    StringBuilder sb = new StringBuilder(127);
    sb.append("TFQUESTION \"").append(q.getID()).append("\"");
    lang.addLine(sb.toString());
  }

  public void createFIBQuestion(FillInBlanksQuestionModel q) {
    StringBuilder sb = new StringBuilder(127);
    sb.append("FIBQUESTION \"").append(q.getID()).append("\"");
    lang.addLine(sb.toString());
  }

  public void createInteractiveElementCode(InteractionModel element) {
    if (!startQuestionsPlaced) {
      lang.addLine("STARTQUESTIONS");
      startQuestionsPlaced = true;
    }
    if (element instanceof TrueFalseQuestionModel)
      createTFQuestionCode((TrueFalseQuestionModel) element);
    else if (element instanceof MultipleChoiceQuestionModel)
      createMCQuestionCode((MultipleChoiceQuestionModel) element);
    else if (element instanceof MultipleSelectionQuestionModel)
      createMSQuestionCode((MultipleSelectionQuestionModel) element);
    else if (element instanceof FillInBlanksQuestionModel)
      createFIBQuestionCode((FillInBlanksQuestionModel) element);
    // no need to do something for DocuLink!
  }

  public void createTFQuestionCode(TrueFalseQuestionModel tfQuestion) {
    StringBuilder sb = new StringBuilder(255);
    sb.append("TFQUESTION \"").append(tfQuestion.getID());
    sb.append("\"\n\"").append(tfQuestion.getPrompt());
    sb.append("\"\nENDTEXT\nANSWER\n");
    sb.append((tfQuestion.getCorrectAnswer()) ? "T" : "F");
    sb.append("\nENDANSWER");
    lang.addLine(sb);
  }

  public void createFIBQuestionCode(FillInBlanksQuestionModel fibQuestion) {
    StringBuilder sb = new StringBuilder(255);
    sb.append("FIBQUESTION \"").append(fibQuestion.getID());
    sb.append("\"\n\"").append(fibQuestion.getPrompt());
    sb.append("\"\nENDTEXT\nANSWER\n");
    // TODO does JHAVE support more than one correct answer?
    String answerID = "";
    for (AnswerModel answer : fibQuestion.getAnswers()) {
      if (answer.getPoints() > 0) {
        answerID = answer.getID();
      }
    }
    sb.append(answerID);
    sb.append("\nENDANSWER");
    lang.addLine(sb);
  }

  public void createMCQuestionCode(MultipleChoiceQuestionModel mcQuestion) {
    StringBuilder sb = new StringBuilder(255);
    sb.append("MCQUESTION \"").append(mcQuestion.getID());
    sb.append("\"\n\"").append(mcQuestion.getPrompt());
    sb.append("\"\nENDTEXT\n");
    String correctAnswerID = "";
    for (AnswerModel answer : mcQuestion.getAnswers()) {
      if (answer.getPoints() > 0) {
        correctAnswerID = answer.getID();
      }
      String prompt = answer.getText();
      if (prompt != null) {
        sb.append("\"").append(prompt).append("\"\nENDCHOICE\n");
      }
    }
    sb.append("ANSWER\n").append(correctAnswerID);
    sb.append("\nENDANSWER");
    lang.addLine(sb);
  }

  public void createMSQuestionCode(MultipleSelectionQuestionModel msQuestion) {
    StringBuilder sb = new StringBuilder(255);
    sb.append("MSQUESTION \"").append(msQuestion.getID());
    sb.append("\"\n\"").append(msQuestion.getPrompt());
    sb.append("\"\nENDTEXT\n");
    for (AnswerModel answer : msQuestion.getAnswers()) {
      String prompt = answer.getText();
      if (prompt != null) {
        sb.append("\"").append(prompt).append("\"\nENDCHOICE\n");
        boolean correct = answer.getPoints() > 0;
        sb.append("ANSWER\n").append(correct).append("\n");
      }
    }
    sb.append("ENDANSWER");
    lang.addLine(sb);
  }

  public void createDocumentationLink(HtmlDocumentationModel docuLink) {
    StringBuilder sb = new StringBuilder(127);
    sb.append("DOCUMENTATION \"").append(docuLink.getID()).append("\"");
    lang.addLine(sb.toString());
  }

  public void createMCQuestion(MultipleChoiceQuestionModel mcQuestion) {
    StringBuilder sb = new StringBuilder(127);
    sb.append("MCQUESTION \"").append(mcQuestion.getID()).append("\"");
    lang.addLine(sb.toString());
  }

  public void createMSQuestion(MultipleSelectionQuestionModel msQuestion) {
    StringBuilder sb = new StringBuilder(127);
    sb.append("MSQUESTION \"").append(msQuestion.getID()).append("\"");
    lang.addLine(sb.toString());
  }

  public void finalizeInteractiveElements() {
    // nothing to be done here :-)
  }

}