/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.misc.modelcheckerCTL.kripke;

import algoanim.primitives.Circle;
import algoanim.primitives.StringArray;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.CircleProperties;
import algoanim.util.Coordinates;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.modelcheckerCTL.token.Token;

import java.awt.*;

public class AnimalKripkeState extends KripkeState {

    Coordinates terminalsPosition;
    Coordinates circleCenter;
    Circle innerCircle;
    Circle outerCircle;
    StringArray terminalsDisplay;
    boolean circleState = false;


    public AnimalKripkeState(KripkeState state, Coordinates center, Language lang,  AnimationPropertiesContainer props) {
        super(state.getToken(), state.getId());
        this.setTerminals(state.getTerminals());
        this.terminalsPosition = new Coordinates(center.getX(), center.getY() + 20);
        this.circleCenter = new Coordinates(center.getX() + 8, center.getY() + 10);

        innerCircle = lang.newCircle(this.circleCenter, 33, this.getName() + "_circle1", null, (CircleProperties)props.getPropertiesByName("innerCircleProperties"));
        outerCircle = lang.newCircle(this.circleCenter, 37, this.getName() + "_circle2", null, (CircleProperties)props.getPropertiesByName("outerCircleProperties"));
        innerCircle.hide();
        outerCircle.hide();

        String[] tmpValues;
        tmpValues = new String[this.getTerminals().size()];
        for (int i = 0; i < tmpValues.length; i++) {
            tmpValues[i] = this.getTerminals().get(i).getFormula();
        }
        terminalsDisplay = lang.newStringArray(terminalsPosition, tmpValues, this.getName() + "_terminals", null, (ArrayProperties)props.getPropertiesByName("TerminalProperties"));
        terminalsDisplay.showIndices(false, null, null);
    }

    public void markState(Color color) {
        if (!circleState) {
            innerCircle.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, color, null, null);
            innerCircle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, color, null, null);
            innerCircle.show();
            circleState = true;
        } else {
            outerCircle.changeColor(AnimationPropertiesKeys.COLOR_PROPERTY, color, null, null);
            outerCircle.changeColor(AnimationPropertiesKeys.FILL_PROPERTY, color, null, null);
            outerCircle.show();
        }
    }

    public void unMarkState() {
        innerCircle.hide();
        outerCircle.hide();
        circleState = false;
        for (int i = 0; i < this.getTerminals().size(); i++) {
            terminalsDisplay.unhighlightCell(i, null, null);
        }
    }

    public void highlightTerminal(Token token) {
        for (int i = 0; i < this.getTerminals().size(); i++) {
            if (this.getTerminals().get(i).equals(token)) {
                terminalsDisplay.highlightCell(i, null, null);
            }
        }
    }
}
