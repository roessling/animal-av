package generators.framework;

import algoanim.primitives.Primitive;

public abstract class InteractivityJob {
  
  private final Primitive interactivityPrimitive;
  private final int interactivityStep;
  private final String interactivityObjectName;
  private final String interactivityActionName;
  private boolean interactivityOnlyAtAnimationEnd = true;
  private boolean interactivityFromStepUntilLastStep = false;

  /**
   * Create an interactivity job
   * @param interactivityPrimitive the Primitive which BoundingBox is Interactive
   * @param interactivityStep the step in which the interactivity (starts/ends)
   * @param interactivityObjectName the string for the first PopUpMenu (null->interactivityPrimitive.name)
   * @param interactivityActionName the string for the second PopUpMenu
   */
  public InteractivityJob(Primitive interactivityPrimitive, int interactivityStep, String interactivityObjectName, String interactivityActionName) {
    this.interactivityPrimitive = interactivityPrimitive;
    this.interactivityStep = interactivityStep;
    this.interactivityObjectName = interactivityObjectName;
    this.interactivityActionName = interactivityActionName;
  }

  /**
   * Create an interactivity job
   * @param interactiveActionButton the InteractiveActionButton which (BoundingBox) is Interactive
   * @param interactivityStep the step in which the interactivity (starts/ends)
   * @param interactivityObjectName the string for the first PopUpMenu (null->interactivityPrimitive.name)
   * @param interactivityActionName the string for the second PopUpMenu
   */
  public InteractivityJob(InteractiveActionButton interactiveActionButton, int interactivityStep, String interactivityObjectName, String interactivityActionName) {
    this(interactiveActionButton.getRect(), interactivityStep, interactivityObjectName, interactivityActionName);
  }
  
  /**
   * 
   * @return the Primitive which BoundingBox is Interactive
   */
  public Primitive getInteractivityPrimitive() {
    return interactivityPrimitive;
  }

  /**
   * 
   * @return the step in which the interactivity (starts)
   */
  public int getInteractivityStep() {
    return interactivityStep;
  }

  /**
   * 
   * @return the string for the first PopUpMenu
   */
  public String getInteractivityObjectName() {
    return interactivityObjectName;
  }

  /**
   * 
   * @return the string for the second PopUpMenu
   */
  public String getInteractivityActionName() {
    return interactivityActionName;
  }

  /**
   * 
   * @return if the interactivity is only at Animation End
   */
  public boolean isInteractivityOnlyAtAnimationEnd() {
    return interactivityOnlyAtAnimationEnd;
  }

  /**
   * Change if interactivity is only at Animation End
   * @param interactivityOnlyAtAnimationEnd 
   */
  public void setInteractivityOnlyAtAnimationEnd(
      boolean interactivityOnlyAtAnimationEnd) {
    this.interactivityOnlyAtAnimationEnd = interactivityOnlyAtAnimationEnd;
  }

  /**
   * 
   * @return if the interactivity is from start step till last step
   */
  public boolean isInteractivityFromStepUntilLastStep() {
    return interactivityFromStepUntilLastStep;
  }

  /**
   * Change if interactivity is from start step till last step
   * @param interactivityOnlyAtAnimationEnd 
   */
  public void setInteractivityFromStepUntilLastStep(
      boolean interactivityFromStepUntilLastStep) {
    this.interactivityFromStepUntilLastStep = interactivityFromStepUntilLastStep;
  }

  /**
   * implement what should be done if clicked
   */
  public abstract void doAction();

}
