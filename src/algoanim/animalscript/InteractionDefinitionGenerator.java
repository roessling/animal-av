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
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.QuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import interactionsupport.models.generators.InteractiveElementGenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;

import animalscript.core.AnimalScriptParser;

/**
 * Creates interaction definition code.
 */
public class InteractionDefinitionGenerator extends AnimalGenerator implements
    InteractiveElementGenerator {

  /** the string builder for the interaction definition code */
  private StringBuilder idc = new StringBuilder(24096);

  /** the filename of the interaction definition file */
  private String        fileName;

  public InteractionDefinitionGenerator(AnimalScript as) {
    this(as, null);
  }

  public InteractionDefinitionGenerator(AnimalScript as, String key) {
    super(as);
    // if no filename was given, create one of the format intDef<timestamp>.txt
    if (key == null) {
      Calendar cal = Calendar.getInstance();
      StringBuilder sb = new StringBuilder(30);
      sb.append("intDef");
      formatTwoCharKey(sb, cal.get(Calendar.YEAR));
      formatTwoCharKey(sb, cal.get(Calendar.MONTH));
      formatTwoCharKey(sb, cal.get(Calendar.DAY_OF_MONTH));
      formatTwoCharKey(sb, cal.get(Calendar.HOUR_OF_DAY));
      formatTwoCharKey(sb, cal.get(Calendar.MINUTE));
      formatTwoCharKey(sb, cal.get(Calendar.SECOND));
      formatTwoCharKey(sb, cal.get(Calendar.MILLISECOND));
      sb.append(".txt");
      fileName = sb.toString();
    } else
      fileName = key;
    // add a reference to the interaction definition file to the given
    // AnimalScript instance
    as.addLine("interactionDefinition \"" + fileName + "\"");
  }

  /**
   * Appends the given value as a two-digit number to the {@link StringBuilder}.
   * 
   * @param sb
   *          the {@link StringBuilder} the given number will be added to
   * @param value
   *          the value that should be added to the {@link StringBuilder}
   */
  private void formatTwoCharKey(StringBuilder sb, int value) {
    if (value < 10)
      sb.append("0");
    sb.append(value);
  }

  /**
   * Creates the interaction definition code for the given
   * {@link InteractionModel}.
   */
  public void createInteractiveElementCode(InteractionModel element) {
    if (element instanceof TrueFalseQuestionModel)
      createTFQuestionCode((TrueFalseQuestionModel) element);
    else if (element instanceof FillInBlanksQuestionModel)
      createFIBQuestionCode((FillInBlanksQuestionModel) element);
    else if (element instanceof MultipleChoiceQuestionModel)
      createMCQuestionCode((MultipleChoiceQuestionModel) element);
    else if (element instanceof MultipleSelectionQuestionModel)
      createMSQuestionCode((MultipleSelectionQuestionModel) element);
    else if (element instanceof HtmlDocumentationModel)
      createDocumentationLinkCode((HtmlDocumentationModel) element);
    else if (element instanceof QuestionGroupModel)
      createQuestionGroupCode((QuestionGroupModel) element);
  }

  /**
   * Appends the code which defines the given {@link HtmlDocumentationModel} to
   * the interaction definition code.
   * 
   * @param docuLink
   *          the {@link HtmlDocumentationModel} that should be defined
   */
  public void createDocumentationLinkCode(HtmlDocumentationModel docuLink) {
    idc.append("documentation \"").append(docuLink.getID()).append("\"\n");
    idc.append("    url \"").append(docuLink.getTargetURI().toString())
        .append("\"\n");
    idc.append("endDocumentation");

    // add a line feed for better readability
    idc.append("\n\n");
  }

  /**
   * Appends the code which defines the given {@link QuestionGroupModel} to the
   * interaction definition code.
   * 
   * @param group
   *          the {@link QuestionGroupModel} that should be defined
   */
  public void createQuestionGroupCode(QuestionGroupModel group) {
    idc.append("questionGroup \"").append(group.getID()).append("\"\n");
    idc.append("    numberOfRepeats ").append(group.getNumberOfRepeats())
        .append("\n");
    idc.append("endQuestionGroup");

    // add a line feed for better readability
    idc.append("\n\n");
  }

  /**
   * Appends a reference to the given {@link InteractionModel} to the
   * AnimalScript code.
   */
  public void createInteraction(InteractionModel interaction) {
    lang.addLine("interaction \"" + interaction.getID() + "\"");
  }

  /**
   * Appends the standard beginning code for the {@link QuestionModel} to the
   * interaction definition code. Includes the question ID, the prompt and the
   * question group.
   * 
   * @param tag
   *          the tag for the given {@link QuestionModel}, e.g.
   *          trueFalseQuestion
   * @param question
   *          the question the code should be generated for
   */
  private void createQuestionHeaderCode(String tag, QuestionModel question) {
    // add the question type and the question ID
    idc.append(tag).append(" \"").append(question.getID()).append("\"\n");

    // add the question prompt
    idc.append("    prompt \"").append(question.getPrompt()).append("\"\n");

    // add question group, if any
    if (question.getGroupID() != null) {
      idc.append("    questionGroup \"");
      idc.append(question.getGroupID()).append("\"\n");
    }

    // add number of tries
    idc.append("    numberOfTries ").append(question.getNumberOfTries())
        .append("\n");
  }

  /**
   * Appends code to the interaction definition which defines the given answers.
   * 
   * @param answers
   *          the answers that should be added to the interaction definition
   *          code
   */
  private void createAnswersCode(Collection<AnswerModel> answers) {
    for (AnswerModel answer : answers) {
      idc.append("    answer \"").append(answer.getID()).append("\"\n");
      idc.append("        text \"").append(answer.getText()).append("\"\n");
      idc.append("        points ").append(answer.getPoints()).append("\n");
      idc.append("        feedback \"").append(answer.getFeedback())
          .append("\"\n");
      idc.append("    endAnswer\n");
    }
  }

  /**
   * Appends the standard ending code for a question: endQuestion and a line
   * feed.
   */
  private void createQuestionFooterCode() {
    idc.append("endQuestion");
    idc.append("\n\n");
  }

  /**
   * Appends the code which defines the given {@link TrueFalseQuestionModel} to
   * the interaction definition code.
   * 
   * @param tfQuestion
   *          the {@link TrueFalseQuestionModel} that should be defined
   */
  public void createTFQuestionCode(TrueFalseQuestionModel tfQuestion) {
    createQuestionHeaderCode("trueFalseQuestion", tfQuestion);

    createAnswersCode(tfQuestion.getAnswers());

    createQuestionFooterCode();
  }

  /**
   * Appends the code which defines the given {@link FillInBlanksQuestionModel}
   * to the interaction definition code.
   * 
   * @param fibQuestion
   *          the {@link FillInBlanksQuestionModel} that should be defined
   */
  public void createFIBQuestionCode(FillInBlanksQuestionModel fibQuestion) {
    createQuestionHeaderCode("fillInBlanksQuestion", fibQuestion);

    createAnswersCode(fibQuestion.getAnswers());

    createQuestionFooterCode();
  }

  /**
   * Appends the code which defines the given
   * {@link MultipleChoiceQuestionModel} to the interaction definition code.
   * 
   * @param mcQuestion
   *          the {@link MultipleChoiceQuestionModel} that should be defined
   */
  public void createMCQuestionCode(MultipleChoiceQuestionModel mcQuestion) {
    createQuestionHeaderCode("multipleChoiceQuestion", mcQuestion);

    createAnswersCode(mcQuestion.getAnswers());

    createQuestionFooterCode();
  }

  /**
   * Appends the code which defines the given
   * {@link MultipleSelectionQuestionModel} to the interaction definition code.
   * 
   * @param msQuestion
   *          the {@link MultipleSelectionQuestionModel} that should be defined
   */
  public void createMSQuestionCode(MultipleSelectionQuestionModel msQuestion) {
    createQuestionHeaderCode("multipleSelectionQuestion", msQuestion);

    createAnswersCode(msQuestion.getAnswers());

    createQuestionFooterCode();
  }

  /**
   * Writes the generated interaction definition code to the interaction
   * definition file.
   */
  public void finalizeInteractiveElements() {
    try {
//      File temp = File.createTempFile(fileName.replace(".txt", ""), ".txt");
      File temp = new File(System.getProperty("java.io.tmpdir")+File.separator+fileName);
      fileHashMap.put(fileName, temp);
      Writer fw = new BufferedWriter(new FileWriter(temp));
      fw.write(idc.toString());
      fw.close();
      //System.err.println("Wrote file " + fileName + ".");
    } catch (Exception ex) {
      System.err.println("Error while writing file:");
      System.err.println("Filename: " + fileName);
      System.err.println(ex.getMessage());
    }
  }

  /**
   * Retrieve the generated interaction definition code.
   * 
   * @return the generated code which defines the interactions
   */
  public String getInteractionDefinitionCode() {
    return idc.toString();
  }
  
  private static HashMap<String, File> fileHashMap = new HashMap<String, File>();
  public static File getQuestionFile(String filename) {
    File f = fileHashMap.get(filename);
    if(f==null) {
      try {
        f = new File(File.createTempFile("temp", "").getAbsolutePath()+File.separator+filename);
        String pathTempFile = File.createTempFile("temp", "").getAbsolutePath();
        String path = pathTempFile.substring(0, pathTempFile.lastIndexOf("\\"));
        if(!f.exists()) {
          f = new File(path+File.separator+filename);
          if(!f.exists()) {
            if(AnimalScriptParser.currentFilename!=null) {
              path = AnimalScriptParser.currentFilename.substring(0, AnimalScriptParser.currentFilename.lastIndexOf("\\"));
              f = new File(path+File.separator+filename);
              if(!f.exists()) {
                f = null;
              }
            } else {
              f = null;
            }
          }
        }
      } catch (Exception ex) {}
    }
    if(f==null) {
      System.err.println("Error while reading Question-File:");
      System.err.println("Filename: " + filename);
    }
    return f;
  }

}