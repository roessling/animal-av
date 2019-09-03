package animal.api;

/**
 * Listener to implement in order to react on Animal Animation Steps.
 * 
 * @author Gina Haeussge <huge@rbg.informatik.tu-darmstadt.de>
 */
public interface AnimalStepListener {

  /**
   * Called upon a made step in the animation.
   *
   * @param step
   *          The number of the step made.
   * @param immediate
   *          Whether the animation will proceed immediately or substeps will be
   *          shown
   */
  void stepSet(int step, boolean immediate);

}
