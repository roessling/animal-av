package generators.misc;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.GraphProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.TextProperties;

public class PolicyIterationGenerator implements Generator {
	private Language lang;
	private SourceCodeProperties pseudocode;
	private MatrixProperties valuefunktion;
	private TextProperties infotext;
	//private TextProperties SubHeadlineProp;
	private GraphProperties mdpGraphProp;
	private GraphProperties mdpNGraphProp;
	private double discountfactor;
	private int[][] initValuefunktion;
  // private boolean askQuestions;
	private GraphProperties mdpTerinateGraphProp;

	public void init() {

		lang = new AnimalScript("Value Iteration[DE]", "M. Viering", 800, 600);

	}

	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
		pseudocode = (SourceCodeProperties) props.getPropertiesByName("Pseudocode");
		valuefunktion = (MatrixProperties) props.getPropertiesByName("Valuefunktion");
		infotext = (TextProperties) props.getPropertiesByName("Infotext");
		discountfactor = (Double) primitives.get("discountfactor");
		initValuefunktion = (int[][]) primitives.get("Belohnungsfunktion");
		mdpGraphProp = (GraphProperties) props.getPropertiesByName("MDP");
		mdpNGraphProp = (GraphProperties) props.getPropertiesByName("MDP - Aktueller Zustand");
		mdpTerinateGraphProp = (GraphProperties) props.getPropertiesByName("MDP - Terminierungszustand");
		
		PolicyIteration pi = new PolicyIteration(lang);
		pi.setpCodeProp(pseudocode);
		pi.setValueFunctionGridProp(valuefunktion);
		pi.setInfoTextsProp(infotext);
		pi.setProperties(initValuefunktion, discountfactor);
		pi.setGraphProp(mdpGraphProp);
		pi.setNGraphProp(mdpNGraphProp);
		pi.setTerGraphProp(mdpTerinateGraphProp);
		// pi.setAskQuestions(askQuestions);

		pi.policyIteration();

		return pi.animalScriptOutputHack();
	}

	public String getName() {
		return "Policy Iteration";
	}

	public String getAlgorithmName() {
		return "Policy Iteration";
	}

	public String getAnimationAuthor() {
		return "Malte Viering";
	}

	public String getDescription() {
		return "Ist ein Verfahren, welches beim Bestärkenden Lernen (engl. Reinforcement learning) verwendet wird. "
				+ "<br>Policy Iteration arbeitet dabei auf dem Markow-Entscheidungsproblem (engl. Markov decision process). "
				+ "<br>Die Policy Iteration findet für jeden Zustand die beste Aktion. Die beste Aktion ist die Aktion mit der die maximale Belohnung, die im Markow-Entscheidungsproblem für diesen Zustand erreichbar ist, ericht wird."
				+ "<br>Policy (deutsch Strategie) ist die Zuweisung einer Aktion zu einem Zustand." + "<br>" + "Das Markow-Entscheidungsproblem ist hier definiert als ein Tupel(S,A,P,R). "
				+ "<br>Dabei ist: " + "<br>&#x0020;&#x0020;&#x0020;&#x0020;    S die Menge der Zustände," + "<br>&#x0020;&#x0020;&#x0020;&#x0020;    A die Menge der Aktionen, "
				+ "<br>&#x0020;&#x0020;&#x0020;&#x0020;    P die Wahrscheinlichkeit, dass bei der Ausführung der Aktion a im Zustand s in den Zustand s_neu gewechselt wird, und"
				+ "<br>&#x0020;&#x0020;&#x0020;&#x0020;    R die Belohnungsfunktion, welche einem Zustand s eine Belohnung r zuordnet."
				+ "<br> Für mehr Informationen zum Markow-Entscheidungsproblem siehe:" + "<br>en.wikipedia.org/wiki/Markov_decision_process. " + "<br>" + "<br>"
				+ "<br>Die Policy Iteration ist dabei der Value Iteration ähnlich. Bei der Value Iteration wird zu erste die optimale Value Funktion iterative berechnet."
				+ "<br>Wenn die optimale Value Funktion berechnet wurde kann über diese die beste Aktion für jeden Zustand bestimmt werden. Die Policy Iteration funktioniert dagegen wie folgt:"
				+ "<br>1.&#x0020;&#x0020;&#x0020;	Erzeuge eine zufällige Strategie" + "<br>2.&#x0020;&#x0020;&#x0020;	Wiederhole bis Konvergenz erreicht" + "<br>&#x0020;&#x0020;&#x0020;   a.&#x0020;&#x0020;&#x0020;	Berechne die zur Strategie gehörige Value Funktion V"
				+ "<br>&#x0020;&#x0020;&#x0020;   b.&#x0020;&#x0020;&#x0020;	Bestimmt die beste Strategie zu V ";

	}

	public String getCodeExample() {
		return "FUNCTION policyIteration\n" + "PARAMTER states, actions, reward\n" + "RETURN policy\n" + "CONSTANTS DiscountFactor\n" + "VARIABLE ValueFunction\n" + "DO\n" + "   SET change TO false\n" + " "
				+ "  SET ValueFunction To SOLVE_LINEAR_EQUATIONSYSTEM (policy+ rewardForStates)\n" + "\n" + "   FORALL states BEGIN\n" + "      SET rewardBestAction TO bestRewardLastIteration() \n"
				+ "      SET neighborStates TO getNeighborStates(state)\n" + "      FORALL possible actions BEGIN\n" + "         SET rewardAction TO 0\n" + "         FORALL neighborStates BEGIN \n"
				+ "            INCREMENT reward BY\n" + "                           transitionProbability(state+ action+ neighborState) * ValueFunction[neighborState]\n" + "         END\n"
				+ "         SET rewardAction TO rewardAction * DiscountFactor\n" + "         IF rewardAction> rewardBestAction BEGIN\n" + "            SET rewardBestAction TO rewardAction\n"
				+ "            SET policy[state] TO action\n" + "            SET change TO true\n" + "         END\n" + "      END\n" + "   END\n" + "WHILE change\n" + "END FUNCTION";
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