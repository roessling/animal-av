package generators.misc.helpers;

/**
 * The Rule of a grammar of the form
 * A -> XY | a
 * @author paskuda, zöller
 *
 */
public class Rule {
	/**
	 * The single terminal symbol this rule can lead to
	 */
	public String terminal;
	/**
	 * The origin of the rule
	 */
	public String father;
	/**
	 * First Rule this Rule can lead to
	 */
	public String leftChild;
	/**
	 * Second Rule this Rule can lead to
	 */
	public String rightChild;
}
