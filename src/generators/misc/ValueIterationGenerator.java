package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.helpers.ValueIteration;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.TextProperties;

public class ValueIterationGenerator implements Generator {
  private Language             lang;
  private SourceCodeProperties Pseudocode;
  private MatrixProperties     Valuefunktion;
  private TextProperties       Infotext;
  private GraphProperties      mdpGraphProp;
  private GraphProperties      mdpNGraphProp;
  private double               discountfactor;
  private int[][]              initValuefunktion;
  private boolean              askQuestions;

  public void init() {
    lang = new AnimalScript("Value Iteration[DE]", "Malte Viering", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    Pseudocode = (SourceCodeProperties) props.getPropertiesByName("Pseudocode");
    Valuefunktion = (MatrixProperties) props
        .getPropertiesByName("Valuefunktion");
    Infotext = (TextProperties) props.getPropertiesByName("Infotext");
    discountfactor = (Double) primitives.get("discountfactor");
    initValuefunktion = (int[][]) primitives.get("initiale Valuefunktion");
    mdpGraphProp = (GraphProperties) props.getPropertiesByName("MDP");
    mdpNGraphProp = (GraphProperties) props
        .getPropertiesByName("MDP - Aktueller Zustand");
    askQuestions = (Boolean) primitives.get("Animation mit Fragen?");
    ValueIteration vi = new ValueIteration(lang);
    vi.setpCodeProp(Pseudocode);
    vi.setValueFunctionGridProp(Valuefunktion);
    vi.setInfoTextsProp(Infotext);
    vi.setProperties(initValuefunktion, discountfactor);
    vi.setGraphProp(mdpGraphProp);
    vi.setNGraphProp(mdpNGraphProp);
    vi.setAskQuestions(askQuestions);

    vi.valueIteration();

    return vi.animalScriptOutputHack();
  }

  public String getName() {
    return "Value Iteration[DE]";
  }

  public String getAlgorithmName() {
    return "Value Iteration";
  }

  public String getAnimationAuthor() {
    return "Malte Viering";
  }

  public String getDescription() {
    return "Die Value Iteratoin ein Verfahren, welches beim Bestärkenden Lernen (engl. Reinforcement learning) verwendet wird. "
        + "<br>Value Iteration arbeitet dabei auf dem Markow-Entscheidungsproblem (engl. Markov decision process). "
        + "<br>Die Value Iteration findet für jeden Zustand die maximale Belohnung, die im Markow-Entscheidungsproblem für diesen Zustand erreichbar ist."
        + "<br>"
        + "<br>Das Markow-Entscheidungsproblem ist hier definiert als ein Tupel(S,A,P,R). "
        + "<br>Dabei ist: "
        + "<br>&#x0020;&#x0020;&#x0020;&#x0020;   S die Menge der Zustände,"
        + "<br>&#x0020;&#x0020;&#x0020;&#x0020;   A die Menge der Aktionen, "
        + "<br>&#x0020;&#x0020;&#x0020;&#x0020;   P die Wahrscheinlichkeit, dass bei der Ausführung der Aktion a im Zustand s in den Zustand s_neu gewechselt wird, und"
        + "<br>&#x0020;&#x0020;&#x0020;&#x0020;   R die Belohnungsfunktion, welche einem Zustand s eine Belohnung r zuordnet."
        + "<br>Für mehr Informationen zum Markow-Entscheidungsproblem siehe:"
        + "<br>en.wikipedia.org/wiki/Markov_decision_process. ";
  }

  public String getCodeExample() {
    return "PARAMTER states, actions, valueFuntion"
        + "\n"
        + "RETURN valueFuntion"
        + "\n"
        + "CONSTANTS DiscountFactor"
        + "\n"
        + "DO"
        + "\n"
        + "   SET change TO 0"
        + "\n"
        + "   FORALL states BEGIN"
        + "\n"
        + "      SET neighborStates TO getNeighborStates(state)"
        + "\n"
        + "      FORALL possible actions BEGIN"
        + "\n"
        + "         SET alternativeValue TO 0"
        + "\n"
        + "         FORALL neighborStates BEGIN"
        + "\n"
        + "            INCREMENT alternativeValue BY"
        + "\n"
        + "                           transitionProbability(state, action, neighborState) * valueFunction[neighborState]"
        + "\n"
        + "         END"
        + "\n"
        + "         SET alternativeValue TO alternativeValue * DiscountFactor"
        + "\n"
        + "         IF alternativeValue> valueFunction[neighborState] BEGIN"
        + "\n"
        + "            SET change TO max(change, alternativeValue - valueFunction[state])"
        + "\n" + "            SET valueFunction[state] TO alternativeValue"
        + "\n" + "         END" + "\n" + "      END" + "\n" + "   END" + "\n"
        + "WHILE change > delta";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}