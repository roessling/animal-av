package animal.exchange.animalscript;

import animal.animator.Animator;

public class InteractionElementExporter extends AnimatorExporter {

  public String getExportString(Animator animator) {
    // the InteractionElement class does everything for us
    return animator.toString();
  }

}
