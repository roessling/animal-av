package generators.framework;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;

import java.awt.Component;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import animal.gui.AnimalMainWindow;
import animal.main.Animal;
import generators.generatorframe.store.GetInfos;

/**
 * makes an Generator Interactive
 * 
 * @author Christian Dreger
 * @version 1.0
 */
public abstract class InteractiveGenerator {

  private LinkedList<InteractivityJob> interactivityJobList = null;
  private LinkedList<Runnable> taskList = null;
  private Runnable startTask = null;
  
  /**
   * Get the Player Component!
   * Needed for example for JOptionPane.showInputDialog(getParentComponent(),"","")
   * @return the player component
   */
  protected Component getParentComponent() {
    return AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false).getAnimationWindowView();
  }

  /**
   * Should return the AnimationScriptInstance Variable which is used to generate the AnimalScriptCode in this Generator
   * @return AnimationScriptInstance Variable of this Generator
   */
  public abstract Language getAnimalScriptInstance();
  
  /**
   * It will reset(clear) the interactivityJobList and the taskList. <br>
   * After that it set the startTask and execute it.<br>
   * Remind to set the (AnimalScript-)Language in the start task and all other global variables!
   * @param startTask the first task which can not be undo.
   * It should contain all start parameters from generate(...) and start declarations.
   */
  public final void startInteractivity(Runnable startTask) {
    interactivityJobList = new LinkedList<InteractivityJob>();
    taskList = new LinkedList<Runnable>();
    this.startTask = startTask;
    runTask(startTask);
  }
  
  private final void msgIfNotInteractive() {
    if(!isInteractive()) {
      System.err.println("Error: You must call startInteractivity(...) first, to make a Generator Interactive!");
    }
  }
  
  /**
   * 
   * @return if this Generator is interactive
   */
  public final boolean isInteractive() {
    return interactivityJobList!=null && taskList!=null && startTask!=null;
  }
  
  /**
   * 
   * @return a copy of the interactivityJobList
   */
  public final LinkedList<InteractivityJob> getInteractivityJobList() {
    msgIfNotInteractive();
    LinkedList<InteractivityJob> cloneList = new LinkedList<InteractivityJob>();
    for(InteractivityJob iJob : interactivityJobList) {
      cloneList.add(iJob);
    }
    return cloneList;
  }
  
  /**
   * Adds an InteractivityJob to the InteractivityList
   * @param iJob InteractivityJob to add
   */
  public final void addInteractivityJob(InteractivityJob iJob) {
    msgIfNotInteractive();
    interactivityJobList.add(iJob);
  }
  
  /**
   * Remove an InteractivityJob from the InteractivityList
   * @param iJob InteractivityJob to remove
   */
  public final void removeInteractivityJobList(InteractivityJob iJob) {
    msgIfNotInteractive();
    interactivityJobList.remove(iJob);
  }
  
  /**
   * Run all tasks which run before and the new one!
   * New task is inserted to the taskList
   * First task will be startTask and can not be removed!
   * @param task
   */
  public final void runTask(Runnable task) {
    msgIfNotInteractive();
    taskList.add(task);
    executeAllTasksInList();
  }
  
  /**
   * Remove the last run(inserted) task.
   * First task will be startTask and can not be removed!
   * 
   * After removed, all tasks which are still in the taskList will be running.
   * 
   * @return true if an item was removed, false if not(no items in list or only startTask)
   */
  public final boolean removeLastTask() {
    msgIfNotInteractive();
    if(taskList.size()==0)
      return false;
    taskList.removeLast();
    executeAllTasksInList();
    return true;
  }
  
  /**
   * Execute all tasks in the taskList from first to last task and update the Animation with the new AnimalScriptCode
   */
  private final void executeAllTasksInList() {
    Language lang = getAnimalScriptInstance();
    if(lang==null) {
      System.err.println("Error: AnimalScriptLanguage is null - You must implement \"getAnimalScriptLanguage()\" correctly!");
    } else {
      int currentStep = getAnimalScriptInstance().getStep();
      interactivityJobList = new LinkedList<InteractivityJob>();
      for(Runnable t : taskList) {
        t.run();
      }
      Generator generator = GetInfos.getInstance().getLatestStartetGenerator();
      InteractiveGenerator iGenerator = null;
      if(generator!=null && generator instanceof InteractiveGenerator) {
        iGenerator = (InteractiveGenerator) generator;
        if(this.equals(iGenerator)) {
          Animal.get().getStarter().setAnimation(getAnimalScriptInstance().toString());
          int newLastStep = AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false).getAnimationState().getLastStep();
          int stepToSet = currentStep>newLastStep ? newLastStep : currentStep;
          AnimalMainWindow.getWindowCoordinator().getAnimationWindow(false).setStep(stepToSet, true);
        }
      }
    }
  }
  

  private Rect titleIRec;
  private SourceCode sc = null;
  /**
   * create an intro as the first slide in the visualization with a framed headline and a description.
   * @param headline name of the algorithm
   * @param description description of the algorithm
   */
  protected final void createIntro(String headline, String description, boolean showIteractivityInTitle) {
    msgIfNotInteractive();
    Language lang = getAnimalScriptInstance();
    if(lang==null) {
      System.err.println("Error: AnimalScriptLanguage is null - You must implement \"getAnimalScriptLanguage()\" correctly!");
    } else {
      Coordinates headlineTextPosition = new Coordinates(40, 30);
      Coordinates descriptionPosition = new Coordinates(50, 120);
      
      //headline text
      TextProperties headlineProps = new TextProperties();
      headlineProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 30));
      headlineProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 0);
      Text textHeadline = lang.newText(headlineTextPosition, headline, "headline", null, headlineProps);

      // headline rectangle
      RectProperties headlinebgProps = new RectProperties();
      headlinebgProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
      headlinebgProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
      headlinebgProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(135,206,235));
      headlinebgProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      headlinebgProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
      Rect rectHeadlinebg = lang.newRect(new Offset(-20, -10, textHeadline, AnimalScript.DIRECTION_NW), new Offset(20, 10, textHeadline, AnimalScript.DIRECTION_SE), "rectHeadlinebg", null, headlinebgProps);

      // shadow (back rectangle in the background)
      RectProperties shadowProps = new RectProperties();
      shadowProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
      shadowProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
      shadowProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
      shadowProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      shadowProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
      @SuppressWarnings("unused")
      Rect rectHeadlineshadow = lang.newRect(new Offset(10, 10, rectHeadlinebg, AnimalScript.DIRECTION_NW), new Offset(10, 10, rectHeadlinebg, AnimalScript.DIRECTION_SE), "rectHeadlineshadow", null, shadowProps);

      if(showIteractivityInTitle) {
        RectProperties titleIRecProps = new RectProperties();
        titleIRecProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        titleIRecProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
        titleIRecProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
        titleIRecProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        titleIRec = lang.newRect(new Offset(0, 0, rectHeadlinebg, AnimalScript.DIRECTION_NW), new Offset(16, 16, rectHeadlinebg, AnimalScript.DIRECTION_NW), "titleIRec"+System.currentTimeMillis(), null, titleIRecProps);
        
        TextProperties titleIProps = new TextProperties();
        titleIProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 15));
        @SuppressWarnings("unused")
        Text titleI = lang.newText(new Offset(6, -2, rectHeadlinebg, AnimalScript.DIRECTION_NW), "i", "titleI", null, titleIProps);
      }
      
      SourceCodeProperties descProps = new SourceCodeProperties();
      descProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 14));
      sc = lang.newSourceCode(descriptionPosition, "description", null, descProps);
      sc.addMultilineCode(description, "desc", null);  
    }
  }
  
  /**
   * Create an InteractiveActionButton, which you can pass in an InteractivityJob as Primitive.
   * @param coordinates coordinate of the button
   * @param buttonText text in Button
   * @param size size of the Text
   * @param colorBG background color of the rectangle for the button
   * @return an InteractiveActionButton
   */
  protected final InteractiveActionButton createInteractiveActionButton(Coordinates coordinates, String buttonText, int size, Color colorBG) {
    msgIfNotInteractive();
    Language lang = getAnimalScriptInstance();
    if(lang==null) {
      System.err.println("Error: AnimalScriptLanguage is null - You must implement \"getAnimalScriptLanguage()\" correctly!");
      return null;
    } else {
      if(colorBG==null) {
        colorBG = Color.CYAN;
      }
      return new InteractiveActionButton(lang, coordinates, buttonText, size, colorBG);
    }
  }

  /**
   * Hide IntroDescription if set
   */
  protected final void hideIntroDescription() {
    msgIfNotInteractive();
    if(sc!=null)
      sc.hide(new TicksTiming(0));
  }

  /**
   * Get InteractiveRectName.
   */
  public final String getIRectName() {
    msgIfNotInteractive();
    return titleIRec==null ? null : titleIRec.getName();
  }

  /**
   * 
   * @return if lastTask can be remove(more than startTask is in list)
   */
  public final boolean canRemoveLastTask() {
    msgIfNotInteractive();
    return taskList!=null && taskList.size()>1;
  }
}
