/**
 * 
 */
package generators.graph.pagerank;

import java.util.List;
import java.util.Map;

import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.MatrixProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Node;
import algoanim.util.Offset;
import generators.graph.util.FileLoader;

/**
 * @author marc
 *
 */
public class PageRankResultGUI extends PageRankGUI {
	private PageRankAlgorithmGUI pageRankAlgorithm;
	
	private GraphInfoGUI graphInfo;
	
	private Text resultDescriptionHeader;
	private Text resultDescription;
	private List<Text> resultUse;
	
	private StringMatrix resultMatrix;
	
	public PageRankResultGUI(Language lang, Text header, PageRankAlgorithmGUI pageRankAlgorithm, MatrixProperties guiStringMatrixProperties, TextProperties descriptionHeaderProperties, TextProperties normalTextProperties) {
		super(lang, header);
		
		this.pageRankAlgorithm = pageRankAlgorithm;
		this.graphInfo = pageRankAlgorithm.getGraphInfo();
		
		this.initContent(guiStringMatrixProperties, descriptionHeaderProperties, normalTextProperties);
		this.hide();
	}
	
	private void initContent(MatrixProperties guiStringMatrixProperties, TextProperties descriptionHeaderProperties, TextProperties normalTextProperties) {
		this.resultDescriptionHeader = lang.newText(new Offset(50, 0, graphInfo.getGraph(), "NE"), "Endergebnis:", "ResultDescriptionHeader", null, descriptionHeaderProperties);
		this.resultDescription = lang.newText(new Offset(0, 5, resultDescriptionHeader, "SW"), "Das Ergebnis des PageRank-Algorithmus auf dem gegebenen Graphen ist der folgende Vektor mit Knotengewichten:", "ResultDescription", null, normalTextProperties);
		
		String[][] resultMatrixContent = new String[2][graphInfo.numberOfNodes() + 1];
		resultMatrixContent[0][0] = "";
		resultMatrixContent[1][0] = "Knotengewicht:";
		for (int nodeIndex = 0; nodeIndex < graphInfo.numberOfNodes(); nodeIndex++) {
			resultMatrixContent[0][nodeIndex + 1] = graphInfo.getNodeLabel(nodeIndex);
			resultMatrixContent[1][nodeIndex + 1] = "";
		}
		
		this.resultMatrix = lang.newStringMatrix(new Offset(0, 10, this.resultDescription, "SW"), resultMatrixContent, "ResultMatrix", null, guiStringMatrixProperties);
		
		this.resultUse = FileLoader.loadContent("generators/graph/pagerank/resultuse.txt", lang, "Couldn't find result use text", "ResultUseLine", (list) -> list.isEmpty()?this.resultMatrix:list.get(list.size() - 1), normalTextProperties);
	}
	
	public void setKnotenGewichte() {
		Map<Node, Double> nodeWeights = pageRankAlgorithm.getNodeWeights();
		for (int nodeIndex = 0; nodeIndex < graphInfo.numberOfNodes(); nodeIndex++) {
			resultMatrix.put(1, nodeIndex + 1, String.valueOf(nodeWeights.get(graphInfo.getNode(nodeIndex))), null, null);
		}
	}
	
	@Override
	public void hide() {
		this.graphInfo.hide();
		this.resultDescriptionHeader.hide();
		this.resultDescription.hide();
		this.resultMatrix.hide();
		
		for (Text use : this.resultUse) {
			use.hide();
		}
	}

	@Override
	public void show() {
		this.graphInfo.show();
		this.resultDescriptionHeader.show();
		this.resultDescription.show();
		this.resultMatrix.show();
		for (Text use : this.resultUse) {
			use.show();
		}
		
		this.setKnotenGewichte();
	}
}
