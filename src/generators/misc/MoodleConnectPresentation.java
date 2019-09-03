package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.HtmlDocumentationModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;

import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * Simple presentation just showing all question types and introducing
 * MoodleConnect.
 * 
 * @author Simon Sprankel <sprankel@rbg.informatik.tu-darmstadt.de>
 */
public class MoodleConnectPresentation implements Generator {

  private Language language;

  public MoodleConnectPresentation() {
  }

  public MoodleConnectPresentation(Language language) {
    this.language = language;
    // This initializes the step mode. Each pair of subsequent steps has to
    // be divided by a call of lang.nextStep();
    language.setStepMode(true);
  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    MoodleConnectPresentation presentation = new MoodleConnectPresentation(
        language);
    presentation.showPresentation();
    language.finalizeGeneration();
    return language.toString();
  }

  private void showPresentation() {
    language.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    // set the header text
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.BOLD, 24));
    language.newText(new Coordinates(20, 30),
        "MoodleConnect - die Schnittstelle zu Moodle", "header", null,
        headerProps);

    language.nextStep();
    TextProperties textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
        Font.SANS_SERIF, Font.PLAIN, 16));
    language.newText(new Coordinates(10, 100),
        "In dieser Präsentation wird die Schnittstelle zwischen Moodle ",
        "description1", null, textProps);
    language.newText(new Offset(0, 25, "description1",
        AnimalScript.DIRECTION_NW),
        "und Animal - MoodleConnect - vorgestellt.", "description2", null,
        textProps);
    language.newText(new Offset(0, 25, "description2",
        AnimalScript.DIRECTION_NW),
        "Dabei werden alle Interaktionstypen ausgeführt.", "description3",
        null, textProps);

    QuestionGroupModel groupInfo = new QuestionGroupModel(
        "First question group", 1);
    language.addQuestionGroup(groupInfo);

    // a sample fill in blanks question
    language.nextStep();
    FillInBlanksQuestionModel fibq = new FillInBlanksQuestionModel(
        "fillInBlanksQuestion");
    fibq.setPrompt("To which learning management system does MoodleConnect provide an interface?");
    fibq.addAnswer("Moodle", 10, "This is so easy...");
    fibq.addAnswer("Moodle LMS", 10, "This is so easy...");
    fibq.setGroupID("First question group");
    language.addFIBQuestion(fibq);

    // a sample documentation
    language.nextStep();
    HtmlDocumentationModel doc = new HtmlDocumentationModel("documentation", "http://www.moodle.org/");
    language.addDocumentationLink(doc);

    // a sample multiple choice question
    language.nextStep();
    MultipleChoiceQuestionModel mcq = new MultipleChoiceQuestionModel(
        "multipleChoiceQuestion");
    mcq.setPrompt("Who developed MoodleConnect?");
    mcq.addAnswer("Simon Sprankel", 5, "Absolutely right!");
    mcq.addAnswer("Karsten Weihe", 0, "No, not really.");
    mcq.addAnswer("Guido Rößling", 1, "He was also involved...");
    mcq.setGroupID("First question group");
    language.addMCQuestion(mcq);

    // a sample multiple selection question
    language.nextStep();
    MultipleSelectionQuestionModel msq = new MultipleSelectionQuestionModel(
        "multipleSelectionQuestion");
    msq.setPrompt("Which programming languages does MoodleConnect use?");
    msq.addAnswer("PHP", 1, "PHP is used in the Moodle module...");
    msq.addAnswer("C", -1, "Definitely not...");
    msq.addAnswer("Java", 1, "Animal is written in Java, yes.");
    msq.setGroupID("Second question group");
    language.addMSQuestion(msq);

    language.nextStep();
    TrueFalseQuestionModel tfq = new TrueFalseQuestionModel(
        "trueFalseQuestion", true, 5);
    tfq.setPrompt("Is MoodleConnect cool?");
    tfq.setGroupID("Second question group");
    language.addTFQuestion(tfq);
  }

  @Override
  public String getAlgorithmName() {
    return "MoodleConnect Presentation";
  }

  @Override
  public String getAnimationAuthor() {
    return "Simon Sprankel";
  }

  @Override
  public String getCodeExample() {
    return "The MoodleConnect presentation does not have any code example";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.US;
  }

  @Override
  public String getDescription() {
    return "This presentation should show how MoodleConnect, the interface"
        + "\n" + "between Moodle and Animal, works and how you can use it."
        + "\n";
  }

  @Override
  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  @Override
  public String getName() {
    return "MoodleConnect Presentation";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public void init() {
    language = new AnimalScript("MoodleConnect Presentation", "Simon Sprankel",
        800, 600);
  }

}
