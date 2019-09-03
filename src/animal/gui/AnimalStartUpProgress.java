package animal.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import animal.misc.MessageDisplay;
import translator.ResourceLocator;

/*
 * Created on 12.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Guido
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class AnimalStartUpProgress extends JPanel // implements ActionListener
{

  /**
   * Comment for <code>serialVersionUID</code>
   */
  private static final long                                         serialVersionUID                           = 1L;
  private JProgressBar                                              startUpBar                                 = null;
  private JTextArea                                                 taskOutput;
  private JFrame                                                    progressFrame;
  private double                                                    percentage                                 = 0.0;
  private JImagePanel                                               animalImage                                = new JImagePanel();

  private static boolean                                            SHOW_IMAGES_FOR_PROGRESS                   = false;
  private static HashMap<String, Image>                             HASHMAP_Images                             = new HashMap<String, Image>();

  public static final String                                        PROGRESS_LABEL_CreatingnewAnimalinstance   = "Creating new Animal instance";
  public static final String                                        PROGRESS_LABEL_LoadGenerators              = "Load Generators";
  public static final String                                        PROGRESS_LABEL_LoadingAnimalproperties     = "Loading Animal properties";
  public static final String                                        PROGRESS_LABEL_Initializingdefaultlocale   = "Initializing default locale";
  public static final String                                        PROGRESS_LABEL_InitializingAnimal          = "Initializing Animal";
  public static final String                                        PROGRESS_LABEL_InstantiateAnimalmainwindow = "Instantiate Animal main window";
  public static final String                                        PROGRESS_LABEL_InitializeAllEditors        = "Initialize All Editors";
  public static final String                                        PROGRESS_LABEL_InitializePrimitives        = "Initialize Primitives";
  public static final String                                        PROGRESS_LABEL_InitializeAnimators         = "Initialize Animators";
  public static final String                                        PROGRESS_LABEL_InitializeHandlerExtensions = "Initialize HandlerExtensions";
  public static final String                                        PROGRESS_LABEL_InitializeAnimationStep     = "Initialize AnimationStep";
  public static final String                                        PROGRESS_LABEL_ParseArguments              = "Parse Arguments";
  public static final String                                        PROGRESS_LABEL_FinalStart                  = "Final Start";
  public static final String                                        PROGRESS_LABEL_HideProgressDisplay         = "Hide ProgressDisplay";

  // public static final String PROGRESS_LABEL_RetrievingAnimalcomponents =
  // "Retrieving Animal components";
  // public static final String PROGRESS_LABEL_Reloadinganimationifrequested =
  // "Reloading animation, if requested";
  // public static final String PROGRESS_LABEL_Checkingcommandlinearguments =
  // "Checking command-line arguments";
  // public static final String PROGRESS_LABEL_Checkingforcommandlinelocale =
  // "Checking for command-line locale";
  // public static final String PROGRESS_LABEL_Loadingcommandlineanimation =
  // "Loading command-line animation";

  private static HashMap<String, AnimalStartUpProgressPercentRange> HASHMAP_STARTUP_PROGRESS_LABELS            = new HashMap<String, AnimalStartUpProgressPercentRange>();

  static {
    HASHMAP_STARTUP_PROGRESS_LABELS.put(
        PROGRESS_LABEL_CreatingnewAnimalinstance,
        new AnimalStartUpProgressPercentRange(1, 10));
    HASHMAP_STARTUP_PROGRESS_LABELS.put(PROGRESS_LABEL_LoadGenerators,
        new AnimalStartUpProgressPercentRange(2, 9));
    HASHMAP_STARTUP_PROGRESS_LABELS.put(PROGRESS_LABEL_LoadingAnimalproperties,
        new AnimalStartUpProgressPercentRange(11, 12));
    HASHMAP_STARTUP_PROGRESS_LABELS.put(
        PROGRESS_LABEL_Initializingdefaultlocale,
        new AnimalStartUpProgressPercentRange(12, 14));
    HASHMAP_STARTUP_PROGRESS_LABELS.put(PROGRESS_LABEL_InitializingAnimal,
        new AnimalStartUpProgressPercentRange(14, 16));
    HASHMAP_STARTUP_PROGRESS_LABELS.put(
        PROGRESS_LABEL_InstantiateAnimalmainwindow,
        new AnimalStartUpProgressPercentRange(16, 20));
    HASHMAP_STARTUP_PROGRESS_LABELS.put(PROGRESS_LABEL_InitializeAllEditors,
        new AnimalStartUpProgressPercentRange(20, 85));
    HASHMAP_STARTUP_PROGRESS_LABELS.put(PROGRESS_LABEL_InitializePrimitives,
        new AnimalStartUpProgressPercentRange(20, 70));
    HASHMAP_STARTUP_PROGRESS_LABELS.put(PROGRESS_LABEL_InitializeAnimators,
        new AnimalStartUpProgressPercentRange(70, 75));
    HASHMAP_STARTUP_PROGRESS_LABELS.put(
        PROGRESS_LABEL_InitializeHandlerExtensions,
        new AnimalStartUpProgressPercentRange(75, 78));
    HASHMAP_STARTUP_PROGRESS_LABELS.put(PROGRESS_LABEL_InitializeAnimationStep,
        new AnimalStartUpProgressPercentRange(78, 85));
    HASHMAP_STARTUP_PROGRESS_LABELS.put(PROGRESS_LABEL_ParseArguments,
        new AnimalStartUpProgressPercentRange(85, 98));
    HASHMAP_STARTUP_PROGRESS_LABELS.put(PROGRESS_LABEL_FinalStart,
        new AnimalStartUpProgressPercentRange(99, 100));
    HASHMAP_STARTUP_PROGRESS_LABELS.put(PROGRESS_LABEL_HideProgressDisplay,
        new AnimalStartUpProgressPercentRange(100, 100));
  }

  private static String[] IMAGES_FOR_PROGRESS = { "Animal-large.jpg",
      "Animal-large_ProgressImageBuilder.jpg",
      "AnimalStartUpProgressImageBuilder_ExerciseBuilder.png",
      "AnimalStartUpProgressImageBuilder_DrawingWindow.png",
      "AnimalStartUpProgressImageBuilder_GeneratorBuilder.png" };

  public AnimalStartUpProgress(String title) {
    super();
    setLayout(new BorderLayout());
    progressFrame = new JFrame(title);
    progressFrame.getContentPane().add(this);

    loadImageInHashMap("Animal-large.jpg");
    for (String s : IMAGES_FOR_PROGRESS) {
      loadImageInHashMap(s);
    }

    Image image = HASHMAP_Images.get("Animal-large_ProgressImageBuilder.jpg");

    animalImage.setSize(new Dimension(750, 450));
    animalImage.setPreferredSize(new Dimension(750, 450));
    animalImage.setMinimumSize(new Dimension(750, 450));
    animalImage.setMaximumSize(new Dimension(750, 450));
    setNewImageBG(image);

    startUpBar = new JProgressBar(0, 100);
    startUpBar.setValue(0);
    percentage = 0.0;
    // startUpBar.setIndeterminate(true);
    startUpBar.setStringPainted(true);

    startUpBar.setSize(new Dimension(-1, 50));
    startUpBar.setPreferredSize(new Dimension(-1, 50));
    startUpBar.setMinimumSize(new Dimension(-1, 50));
    startUpBar.setMaximumSize(new Dimension(-1, 50));

    taskOutput = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(taskOutput);
    scrollPane.setSize(new Dimension(250, 200));
    scrollPane.setPreferredSize(new Dimension(250, 200));
    scrollPane.setMinimumSize(new Dimension(250, 200));
    scrollPane.setMaximumSize(new Dimension(250, 200));
    scrollPane.setHorizontalScrollBar(null);

    add(BorderLayout.CENTER, animalImage);
    add(BorderLayout.SOUTH, startUpBar);
    add(BorderLayout.EAST, scrollPane);

    // progressFrame.setUndecorated(true);
    // progressFrame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
    if (progressFrame.isUndecorated()) {
      progressFrame.setSize(1000, 500);
    } else {
      progressFrame.setSize(1000, 535);
    }

    image = HASHMAP_Images.get("Animal-large.jpg");
    progressFrame.setIconImage(image);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    progressFrame.setLocation(dim.width / 2 - progressFrame.getSize().width / 2,
        dim.height / 2 - progressFrame.getSize().height / 2);
    progressFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    progressFrame.setResizable(false);

    this.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));

    progressFrame.setVisible(true);
  }

  private void loadImageInHashMap(String path) {
    HASHMAP_Images.put(path, getImage(path));
  }

  public Image getImage(String path) {
    Image image = null;
    ImageIcon icon = ResourceLocator.getResourceLocator().getImageIcon(path);
    if (icon != null)
      image = icon.getImage();
    return image;
  }

  public void setNewImageBG(Image image) {
    animalImage.setImage(image.getScaledInstance(animalImage.getWidth(),
        animalImage.getHeight(), Image.SCALE_DEFAULT));
  }

  public AnimalStartUpProgressPercentRange getTextPercentRange(String text) {
    return HASHMAP_STARTUP_PROGRESS_LABELS.get(text);
  }

  public void addTextWithPercent(String text, double percent) {
    addText(text);
    setPercentage(percent);
  }

  public void addTextWithPercentStart(String text) {
    addText(text);
    setPercentage(getTextPercentRange(text).getPERCENT_START());
  }

  public void addTextWithPercentEnd(String text) {
    setPercentage(getTextPercentRange(text).getPERCENT_END());
  }

  public void setInvisible() {
    progressFrame.setVisible(false);
    progressFrame.dispose();

  }

  public void setPercentage(double percent) {
    if (percent < 0 || percent > 100)
      return;
    if (SHOW_IMAGES_FOR_PROGRESS && percent == 10) {
      int max = IMAGES_FOR_PROGRESS.length - 1;
      int min = 0;
      int randomNum = new Random().nextInt((max - min) + 1) + min;
      setNewImageBG(HASHMAP_Images.get(IMAGES_FOR_PROGRESS[randomNum]));
    }
    percentage = percent;
    startUpBar.setValue((int) Math.round(percentage));
  }

  public double getCurrentPercentage() {
    return startUpBar.getValue();
  }

  public void addText(String text) {
    taskOutput.append(text);
    taskOutput.append(MessageDisplay.LINE_FEED);
    taskOutput.setCaretPosition(taskOutput.getDocument().getLength());
  }

}
