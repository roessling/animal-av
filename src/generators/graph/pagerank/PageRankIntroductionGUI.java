package generators.graph.pagerank;

import java.util.List;

import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.TextProperties;
import algoanim.util.Offset;
import generators.graph.util.FileLoader;

/**
 * 
 * @author marc
 *
 */
public class PageRankIntroductionGUI extends PageRankGUI {
	private Text descriptionHeader;
	
	private List<Text> description;
	
	public PageRankIntroductionGUI(Language lang, Text header, TextProperties descriptionHeaderProperties, TextProperties normalTextProperties) {
		super(lang, header);
		
		this.initContent(descriptionHeaderProperties, normalTextProperties);
		this.hide();
	}

	private void initContent(TextProperties descriptionHeaderProperties, TextProperties normalTextProperties) {
		this.descriptionHeader = lang.newText(new Offset(50, 5, header, "SW"), "Beschreibung:", "DescriptionHeader", null, descriptionHeaderProperties);
		
		this.description = FileLoader.loadContent("generators/graph/pagerank/description.txt", lang, "Couldn't find description text", "DescriptionLine", (list) -> list.isEmpty()?this.descriptionHeader:list.get(list.size() - 1), normalTextProperties);	
	}
	
	@Override
	public void hide() {
		this.descriptionHeader.hide();
		
		for (Text text : this.description) {
			text.hide();
		}
	}
	
	public void show() {
		this.descriptionHeader.show();
		
		for (Text text : this.description) {
			text.show();
		}
	}
}
