package htdptl.animation;

import htdptl.traces.TraceManager;
import htdptl.util.Util;
import htdptl.visitors.VisitorUtil;

import java.awt.Color;
import java.util.ArrayList;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

/**
 * This class is used for creating an animation of a HtDP-TL program.
 * The animation begins with an introduction showing all HtDP-TL expression that will be visualized.
 * Then the animations of the HtDP-TL expressions are added.
 * Finally a message "Visualization finished" is added   
 */
public class Animator {

  private Language     lang;
  private TraceManager traceManager;
  private IAnimator    nextAnimator;
  private Text         stepText;
  private Text[]       exp;
  private Text         text;
  private TextProperties bodyProps = new TextProperties();

  public Animator(String title) {
    String myTitle = (title != null) ? title : "generated animation";
    lang = new AnimalScript(myTitle, "HtDP-TL Visualisation Wizard", 640, 480);
    lang.setStepMode(true);

    bodyProps.set(AnimationPropertiesKeys.FONT_PROPERTY, Util.getFont());
  }

  /**
   * For all steps in the manager corresponding animations are added to the animation.
   * @param manager
   * @param expression
   */
  public void animate(TraceManager manager, int expression) {
    this.traceManager = manager;
    traceManager.reset();
    int n = traceManager.getLastStep();
    highlightExpression(expression);

    stepText = lang.newText(new Coordinates(5, 5), "Step 0/" + n, "", null);

    while (!traceManager.done()) {
      addLabel();
      updateStepText(n);
      nextAnimator = AnimationDispatcher.getAnimator(traceManager);
      nextAnimator.animate(lang, traceManager);
    }

    updateStepText(n);
    AnimationDispatcher.getDefaultAnimator().animate(lang, traceManager);
    stepText.hide();
  }

  private void updateStepText(int n) {
    stepText.setText(traceManager.getStep() + "/" + n, null, null);
  }

  private void addLabel() {
    if (lang instanceof AnimalScript
        && traceManager.getCurrentTrace().getDefinition() != null) {
      lang.addLine("label \""
          + VisitorUtil.toCode(traceManager.getCurrentTrace().getRedex()
              .getOperator()) + "\"");
    }

  }

  public String getScriptCode() {
    return lang.toString();
  }

  public void intro(ArrayList<Object> expressions) {
    TextProperties headerProps = new TextProperties();
    headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, Util.getBoldFont());
    text = lang.newText(new Coordinates(20, 20), "Visualized expressions:", "",
        null, headerProps);
//    text.setFont(Util.getBoldFont(), null, null);

    exp = new Text[expressions.size()];
    for (int i = 0; i < expressions.size(); i++) {
      if (i == 0) {
        exp[i] = lang.newText(
            new Offset(0, 20, text, AnimalScript.DIRECTION_SW), Util
                .escape(expressions.get(i).toString()), "", null, bodyProps);
      } else {
        exp[i] = lang.newText(new Offset(0, 5, exp[i - 1],
            AnimalScript.DIRECTION_SW), Util.escape(expressions.get(i)
            .toString()), "", null, bodyProps);
      }
      exp[i].setFont(Util.getFont(), null, null);
    }
    lang.nextStep();
  }

  public void outro() {
    Text text = lang.newText(new Coordinates(20, 20),
        "Visualisation finished.", "", null);
    text.setFont(Util.getFont(), null, null);
  }

  private void highlightExpression(int j) {
    if (j > 0) {
      text.show();
      for (int i = 0; i < exp.length; i++) {
        exp[i].show();
      }
    }
    exp[j].changeColor(AnimalScript.COLORCHANGE_COLOR,
        Util.getHighlightColor(), null, null);
    lang.nextStep();

    text.hide();
    for (int i = 0; i < exp.length; i++) {
      exp[i].hide();
    }
    exp[j].changeColor(AnimalScript.COLORCHANGE_COLOR, Color.black, null, null);

  }

}
