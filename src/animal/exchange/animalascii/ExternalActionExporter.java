package animal.exchange.animalascii;

import java.io.PrintWriter;

import animal.animator.Animator;
import animal.animator.ExternalAction;
import animal.animator.PerformableAction;
import animal.animator.QuestionAction;
import animal.misc.MessageDisplay;

/**
 * The exporter for "external actions" (showing documentation or popping up
 * question elements).
 * 
 * @author Guido Roessling (roessling@acm.org>
 * @version 1.0, 29.07.2004
 */
public class ExternalActionExporter extends TimedAnimatorExporter {
  /**
   * Export this object in ASCII format to the PrintWriter passed in.
   * 
   * @param pw
   *          the PrintWriter to write to
   * @param animator
   *          the current ExternalAction object
   */
  public void exportTo(PrintWriter pw, Animator animator) {
    // 1. write the file version
    pw.print(animator.getFileVersion());
    pw.print(" Step ");

    // 2. write the step number
    pw.print(animator.getStep());
    pw.print(" ExternalAction ");

    ExternalAction externalAction = (ExternalAction) animator;
    pw.print(externalAction.getTypeID());

    PerformableAction anAction = ExternalAction.getActionNamed(externalAction
        .getActionKey());

    if (anAction instanceof QuestionAction) {
      MessageDisplay.errorMsg(String.valueOf(((QuestionAction) anAction)
          .getType()), MessageDisplay.DEBUG_MESSAGE);
    }

    // 4. determine type of animator for export
    switch (externalAction.getTypeID()) {
    case ExternalAction.DOCUMENTATION_ACTION:
      MessageDisplay.errorMsg("Documentation!", MessageDisplay.DEBUG_MESSAGE);

      break;

    case ExternalAction.QUESTION_ACTION:
      MessageDisplay.errorMsg("QuestionAction!", MessageDisplay.DEBUG_MESSAGE);

      break;

    default:
      MessageDisplay.errorMsg("export: " + externalAction.toString()
          + externalAction.getTypeID() + " -- " + externalAction.getType(),
          MessageDisplay.DEBUG_MESSAGE);
    }

    pw.println("");
  }
}
