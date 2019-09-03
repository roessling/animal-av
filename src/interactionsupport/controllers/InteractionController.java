package interactionsupport.controllers;

import interactionsupport.UnknownInteractionException;
import interactionsupport.UnknownParserException;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.HtmlDocumentationModel;
import interactionsupport.models.InteractionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.QuestionModel;
import interactionsupport.models.TrueFalseQuestionModel;
import interactionsupport.models.backend.AnimalEvalBackend;
import interactionsupport.models.backend.BackendInterface;
import interactionsupport.parser.AnimalscriptParser;
import interactionsupport.parser.BadSyntaxException;
import interactionsupport.parser.LanguageParserInterface;
import interactionsupport.parser.Parser;
import interactionsupport.views.FillInBlanksQuestionView;
import interactionsupport.views.HtmlDocumentationView;
import interactionsupport.views.InteractionView;
import interactionsupport.views.MultipleChoiceQuestionView;
import interactionsupport.views.MultipleSelectionQuestionView;
import interactionsupport.views.QuestionView;
import interactionsupport.views.TrueFalseQuestionView;
import interactionsupport.views.WindowWatcher;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import translator.ResourceLocator;
import translator.Translator;

/**
 * Main interaction module, interfaces to the parser and the backend and wraps
 * the whole interaction procedure.
 * 
 * @author Gina Haeussge <huge(at)rbg.informatik.tu-darmstadt.de>, Simon
 *         Sprankel <sprankel@rbg.informatik.tu-darmstadt.de>
 */
public class InteractionController {

  /**
   * the base filename of the file which contains the translated texts used by
   * the {@link InteractionController#translator}
   */
  private static final String                   RESOURCE_NAME      = "interactionRes";

  /** the filename of the parser configuration */
  private static final String                   PARSER_CONFIG_NAME = "parser.config";

  /** the translator used for ensuring I18N measures */
  public static Translator                      translator         = new Translator(
                                                                       RESOURCE_NAME,
                                                                       Locale.US);

  /** the backend to use */
  private BackendInterface                      backend;

  /** stores all {@link InteractionModel} objects */
  private Hashtable<String, InteractionModel>   interactionModels;

  /** stores all the {@link InteractionView} objects */
  private Hashtable<String, InteractionView>    interactionViews;

  /** stores the parser information */
  private Hashtable<String, String>             availableParsers;

  /** stores the {@link QuestionGroupModel} objects */
  private Hashtable<String, QuestionGroupModel> questionGroups;

  /** the one and only window used to display the interaction objects */
  private JFrame                                theFrame;

  /** the listener to the window for closing events */
  private WindowWatcher                         windowListener;

  /**
   * Standard constructor, uses the {@link AnimalEvalBackend} backend.
   * 
   * @throws BadSyntaxException
   *           Thrown if something different than a number was found.
   * @throws IOException
   *           Thrown if something goes wrong with the Tokenizer.
   */
  public InteractionController() throws BadSyntaxException, IOException {
    this(new AnimalEvalBackend());
  }

  /**
   * Usage of this constructor also allows a {@link BackendInterface} object for
   * evaluating the answers.
   * 
   * @param backendObj
   *          The backend object to use
   * 
   * @throws BadSyntaxException
   *           Thrown if something different than a number was found.
   * @throws IOException
   *           Thrown if something goes wrong with the Tokenizer.
   */
  public InteractionController(BackendInterface backendObj)
      throws BadSyntaxException, IOException {
    this(backendObj, false);
  }

  /**
   * Usage of this constructor also allows a {@link BackendInterface} object for
   * evaluating the answers.
   * 
   * @param backendObj
   *          The {@link BackendInterface} object to use
   * @param integratedInAnimal
   *          if true, this component is running inside Animal and will
   *          therefore not have to look for appropriate parsers
   * 
   * @throws BadSyntaxException
   *           Thrown if something different than a number was found.
   * @throws IOException
   *           Thrown if something goes wrong with the Tokenizer.
   */
  public InteractionController(BackendInterface backendObj,
      boolean integratedInAnimal) throws BadSyntaxException, IOException {
    this.backend = backendObj;

    if (integratedInAnimal) {
      setUpListForAnimal();
    } else {
      createParserList();
    }
  }

  /**
   * Allows access to the translator
   * 
   * @return the current Translator instance for I18N purposes
   */
  public static Translator getTranslator() {
    return translator;
  }

  /**
   * Changes the locale for the translator, and thus the messages appearing on
   * the screen.
   * 
   * @param targetLocale
   *          the current locale for I18N purposes
   */
  public static void setTranslatorLocale(Locale targetLocale) {
    if (targetLocale != null) {
      translator = new Translator(RESOURCE_NAME, targetLocale);
    } else {
      translator = new Translator(RESOURCE_NAME, Locale.US);
    }
  }

  /**
   * Translates a given message into the current language, as defined by the
   * current locale.
   * 
   * @param key
   *          the look-up key for the message
   * 
   * @return the translated message
   */
  public static String translateMessage(String key) {
    return translateMessage(key, null);
  }

  /**
   * Translates a given message into the current language, as defined by the
   * current locale.
   * 
   * @param key
   *          the look-up key for the message
   * @param parameters
   *          parameters that are to be incorporated into the message
   * 
   * @return the translated message
   */
  public static String translateMessage(String key, Object[] parameters) {
    return getTranslator().translateMessage(key, parameters);
  }

  /**
   * Retrieve the {@link InteractionModel} objects that store all the
   * interaction data.
   * 
   * @return the {@link InteractionModel} objects
   */
  public Hashtable<String, InteractionModel> getInteractionModels() {
    return interactionModels;
  }

  /**
   * Calling this method will execute the interaction with the given id.
   * 
   * @param interactionID
   *          The id that specifies the interaction
   * 
   * @throws UnknownInteractionException
   *           if there is no known interaction with the given ID. This may also
   *           be due to a parsing problem.
   */
  public void interaction(String interactionID)
      throws UnknownInteractionException {
    if (interactionModels == null) {
      System.err.println("When handling interactionID=\""+interactionID+"\" the interactionModels was null!");
    }
    if (interactionViews == null) {
      System.err.println("When handling interactionID=\""+interactionID+"\" the interactionViews was null!");
    }
    if (interactionModels == null || interactionViews == null) {
      System.err.println("Maybe no intDef File was found!");
      return;
    }
    if (!interactionModels.containsKey(interactionID)
        || !interactionViews.containsKey(interactionID)) {
      throw new UnknownInteractionException(
          InteractionController.translateMessage("unknownInteractionID",
              new String[] { interactionID }));
    }

    if (interactionViews.get(interactionID) instanceof QuestionView) {
      QuestionView aQuestion = (QuestionView) interactionViews
          .get(interactionID);
      showQuestion(aQuestion);
    } else if (interactionViews.get(interactionID) instanceof HtmlDocumentationView) {
      HtmlDocumentationView docView = (HtmlDocumentationView) interactionViews
          .get(interactionID);
      docView.makeGUI();
      docView.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

      theFrame = new JFrame(InteractionController.translateMessage("docu",
          new String[] { docView.getModel().getTargetURI().toString() }));
      theFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

      theFrame.getContentPane().add(docView, BorderLayout.CENTER);

      theFrame.setSize(400, 500);
      theFrame.setVisible(true);
    }
  }

  /**
   * Iterates over all interaction models of this object and creates
   * corresponding view objects.
   */
  private void createViewsFromModels() {
    interactionViews = new Hashtable<String, InteractionView>();
    // iterate over all interaction models
    for (String key : interactionModels.keySet()) {
      InteractionModel interactionModel = interactionModels.get(key);
      String interactionID = interactionModel.getID();
      // create corresponding view objects depending on the type of the
      // interaction
      if (interactionModel instanceof HtmlDocumentationModel) {
        HtmlDocumentationView documentationView = new HtmlDocumentationView(
            interactionID, this);
        interactionViews.put(key, documentationView);
      } else if (interactionModel instanceof FillInBlanksQuestionModel) {
        FillInBlanksQuestionView fibQuestionView = new FillInBlanksQuestionView(
            interactionID, this);
        interactionViews.put(key, fibQuestionView);
      } else if (interactionModel instanceof TrueFalseQuestionModel) {
        TrueFalseQuestionView tfQuestionView = new TrueFalseQuestionView(
            interactionID, this);
        interactionViews.put(key, tfQuestionView);
      } else if (interactionModel instanceof MultipleChoiceQuestionModel) {
        MultipleChoiceQuestionView mcQuestionView = new MultipleChoiceQuestionView(
            interactionID, this);
        interactionViews.put(key, mcQuestionView);
      } else if (interactionModel instanceof MultipleSelectionQuestionModel) {
        MultipleSelectionQuestionView msQuestionView = new MultipleSelectionQuestionView(
            interactionID, this);
        interactionViews.put(key, msQuestionView);
      }
    }
  }

  /**
   * Parses the given interaction definition file with the
   * {@link AnimalscriptParser}.
   * 
   * @param definitionFile
   *          The filename of the interaction definition
   */
  public void interactionDefinition(String definitionFile) {
    AnimalscriptParser parser = new AnimalscriptParser();
    interactionDefinition(definitionFile, parser);
  }

  /**
   * Parses the given interaction definition file with the given parser.
   * 
   * @param definitionFile
   *          the filename of the interaction definition
   * @param parser
   *          the parser that should be used to parse the interaction definition
   */
  public void interactionDefinition(String definitionFile,
      LanguageParserInterface parser) {
    interactionModels = parser.parse(definitionFile);
    // the models are parsed now, so create the interaction views
    createViewsFromModels();
    questionGroups = parser.getQuestionGroups();
  }

  /**
   * Parses the given interaction definition file with the parser referenced by
   * the given String.
   * 
   * @param definitionFile
   *          the filename of the interaction definition
   * @param parserType
   *          the type of parser that should be used, refer to parser.config for
   *          available types.
   * 
   * @throws UnknownParserException
   *           DOCUMENT ME!
   */
  @SuppressWarnings("unchecked")
  public void interactionDefinition(String definitionFile, String parserType)
      throws UnknownParserException {
    String className = "";

    className = availableParsers.get(parserType);

    if (className.equals("")) {
      throw new UnknownParserException(InteractionController.translateMessage(
          "unknownParser", new String[] { parserType }));
    }

    LanguageParserInterface parser;

    try {
      Class<LanguageParserInterface> c = (Class<LanguageParserInterface>) Class
          .forName(className);
      parser = c.newInstance();
    } catch (ClassNotFoundException e) {
      throw new UnknownParserException(InteractionController.translateMessage(
          "parserNotLoaded", new String[] { e.getMessage() }));
    } catch (IllegalAccessException e) {
      throw new UnknownParserException(InteractionController.translateMessage(
          "parserNotLoaded", new String[] { e.getMessage() }));
    } catch (InstantiationException e) {
      throw new UnknownParserException(InteractionController.translateMessage(
          "parserNotLoaded", new String[] { e.getMessage() }));
    }

    interactionModels = parser.parse(definitionFile);
    // the models are parsed now, so create the interaction views
    createViewsFromModels();
    questionGroups = parser.getQuestionGroups();
  }

  /**
   * Closes the window of the interaction with the given ID.
   * 
   * @param interactionID
   */
  public void closeElement(String interactionID) {
    JPanel displayElement = (JPanel) interactionViews.get(interactionID);

    if (displayElement != null) {
      theFrame.remove(displayElement);
    }

    theFrame.removeWindowListener(windowListener);
  }

  /**
   * Create the parser list from the configuration file.
   * 
   * @throws BadSyntaxException
   *           Thrown if the format of the config file is corrupt
   * @throws IOException
   *           if there is some other error
   */
  protected void createParserList() throws BadSyntaxException, IOException {
    StreamTokenizer stok;
    Parser parser;
    String type;
    String parserClass;
    int aChar;

    InputStream inS = ResourceLocator.getResourceLocator().getResourceStream(
        PARSER_CONFIG_NAME);

    // open parser configuration file
    stok = new StreamTokenizer(new BufferedReader(new InputStreamReader(inS)));

    parser = new Parser(stok);
    stok.commentChar('#');

    availableParsers = new Hashtable<String, String>();

    try {
      // retrieve the parser key-class pairs from parser.config
      // and put them into the Hashtable
      while (stok.ttype != StreamTokenizer.TT_EOF) {
        type = "";
        parserClass = "";
        parser.getOptionalWhitespace();

        while (parser.getOptionalEOX()) {
          parser.getOptionalWhitespace();
        }

        type = parser.getQuoted();
        parser.getOptionalWhitespace();
        aChar = parser.getChar();

        if (aChar != '=') {
          throw new BadSyntaxException(InteractionController.translateMessage(
              "lookingForEquals", new String[] { String.valueOf(aChar),
                  PARSER_CONFIG_NAME }));
        }

        parser.getOptionalWhitespace();
        parserClass = parser.getQuoted();
        parser.getOptionalWhitespace();
        parser.getEOX();

        availableParsers.put(type, parserClass);
      }
    } catch (BadSyntaxException e) {
      throw new BadSyntaxException(InteractionController.translateMessage(
          "parserConfigError",
          new String[] { PARSER_CONFIG_NAME, e.getMessage() }));
    }
  }

  /**
   * Processes the question with the given ID. Shows a feedback to the user and
   * executes question group logic.
   * 
   * @param questionID
   */
  public void processQuestion(String questionID) {
    QuestionView questionView = (QuestionView) interactionViews.get(questionID);
    QuestionModel questionModel = (QuestionModel) interactionModels
        .get(questionID);
    boolean displayAnswer = backend.submitAnswer(questionID,
        questionModel.getPointsAchieved(), questionModel.getPointsPossible());

    // If question is part of an existing group and the question was answered
    // correctly, then increment the number of correctly answered questions
    // in this group
    String groupID = questionModel.getGroupID();
    QuestionGroupModel group = null;
    if (groupID != null)
      group = questionGroups.get(groupID);
    if (groupID != null && !groupID.equals("")
        && group != null
        && questionModel.getPointsAchieved() == questionModel
            .getPointsPossible()) {
      group.incrementCorrectlyAnswered();
    }

    questionView.setFeedbackBlack();

    StringBuilder feedbackBuffer = new StringBuilder(256);

    if (displayAnswer) {
      // make the interface display the answer or a comment, if
      // one exists.
      String feedback = questionModel.getFeedback();
      if (!feedback.equals("")) {
        feedbackBuffer.append(feedback);
      } else if (questionModel.getPointsAchieved() == questionModel
          .getPointsPossible()) {
        feedbackBuffer.append(InteractionController
            .translateMessage("answerCorrect"));
      } else if (questionModel.getPointsAchieved() != 0
          && questionModel.getPointsAchieved() < questionModel
              .getPointsPossible()) {
        feedbackBuffer.append(InteractionController
            .translateMessage("answerPartiallyCorrect"));
      } else {
        feedbackBuffer.append(InteractionController
            .translateMessage("answerIncorrect"));
      }
    } else {
      // wait till user closes the frame
      feedbackBuffer.append(InteractionController
          .translateMessage("answerSubmitted"));
    }

    feedbackBuffer.append(InteractionController
        .translateMessage("mayCloseWindow"));
    questionView.showFeedback(feedbackBuffer.toString());
  }

  private void setUpListForAnimal() {
    availableParsers = new Hashtable<String, String>();
    availableParsers.put("text/animalscript",
        "interactionsupport.parser.AnimalscriptParser");
  }

  /**
   * Shows a popup representing the given {@link QuestionView}. If enough
   * questions of the corresponding question group have already been correctly
   * answered, the question is not shown. The question is also not shown if the
   * user exceeds the allowed number of tries.
   * 
   * @param questionView
   */
  private void showQuestion(QuestionView questionView) {
    QuestionModel questionModel = (QuestionModel) interactionModels
        .get(questionView.getID());
    // lookup group infos
    QuestionGroupModel group = null;

    String groupID = questionModel.getGroupID();
    if (groupID != null)
      group = questionGroups.get(groupID);

    // if amount of correct questions of group is already reached,
    // we do not have to continue with this interaction
    // if ((group.processed >= group.repeats) && (group.processed != 0) &&
    // (group.repeats != 0)) {
    if ((group != null) && !groupID.equals("") && (group.getCorrectlyAnswered() >= group.getNumberOfRepeats())
          && (group.getCorrectlyAnswered() != 0 && group.getNumberOfRepeats() != 0))
      return;

    // if the question has already been answered too often, do nothing
    if (questionView.getNumberOfBuilds() == 0) {
      questionView.makeGUI();
    } else if (questionView.getNumberOfBuilds() < questionModel
        .getNumberOfTries()) {
      questionView.rebuildQuestion();
    } else {
      return;
    }

    questionView.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    questionView.setVisible(true);

    theFrame = new JFrame(questionView.getTitle());
    theFrame.getContentPane().add(questionView, BorderLayout.CENTER);

    if (windowListener == null) {
      windowListener = new WindowWatcher();
    }

    windowListener.setInstance(this);
    windowListener.setID(questionView.getID());
    theFrame.addWindowListener(windowListener);
    theFrame.pack();

    Dimension theSize = theFrame.getSize();
    theFrame.setSize(400, theSize.height);
    theFrame.setVisible(true);
  }

  public BackendInterface getBackend() {
    return backend;
  }

  public void setBackend(BackendInterface backend) {
    this.backend = backend;
  }

}
