/**
 * 
 */
package generators.graph.pagerank;

import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

/**
 * @author marc
 *
 */
public abstract class PageRankGUI {
	protected Language lang;
	
	protected Text header;
	
	protected PageRankGUI(Language lang, Text header) {
		this.lang = lang;
		this.header = header;
	}
	
	public void nextStep() {
		this.lang.nextStep();
	}
	
	public void nextStep(String label) {
		this.lang.nextStep(label);
	}
	
	public abstract void hide();
	
	public abstract void show();
}
