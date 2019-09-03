/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.misc.modelcheckerCTL.kripke;

import algoanim.primitives.Graph;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.util.Coordinates;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalKripkeStructure extends KripkeStructure {

    Graph graph = null;

    Language lang;

    public AnimalKripkeStructure(List<KripkeState> states, KripkeState startState, int[][] adjacencyMatrix, Coordinates[] pattern, Language lang, AnimationPropertiesContainer props) {
        super(adjacencyMatrix, states, startState);
        this.lang = lang;
        buildGraph(pattern, (GraphProperties) props.getPropertiesByName("GraphProperties"));
//        buildTerminalTexts();

        List<KripkeState> tmp = new ArrayList<>();
        for (KripkeState state : states) {
            tmp.add(new AnimalKripkeState(state, this.getCoordinatesOfState(state), lang, props));
        }
        this.states = tmp;
    }

    public AnimalKripkeStructure(KripkeStructure structure, Coordinates[] pattern, Language lang, AnimationPropertiesContainer props) {
        this(structure.getStates(), structure.getStartState(), structure.getAdjacencyMatrix(), pattern, lang, props);
    }

    private void buildGraph(Coordinates[] pattern, GraphProperties graphProperties) {
        String[] labels = new String[this.getStates().size()];
        for (int i = 0; i < labels.length; i++) {
            labels[i] = this.getState(i).getName();
        }

        this.graph = lang.newGraph("graph", this.getAdjacencyMatrix(), pattern, labels, null, graphProperties);
        for (int i = 0; i < labels.length; i++) {
            this.graph.setNodeHighlightFillColor(i, (Color) graphProperties.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY), null, null);
            this.graph.setNodeHighlightTextColor(i, Color.BLACK, null,null);
            this.graph.setNodeRadius(i, 30, null, null);
        }
    }


    public void highlightState(int i, Color color) {
//        this.getGraph().setNodeHighlightFillColor(i, color, null, null);
        this.getGraph().highlightNode(i, null, null);
    }

    public void unHighlightState(int i) {
        this.getGraph().unhighlightNode(i, null, null);
    }

    public void highlightEdge(int from, int to, Color color) {
//        this.getGraph().setEdgeHighlightPolyColor(from, to, color, null, null);
        this.getGraph().highlightEdge(from, to, null, null);
    }

    public void unHighlightEdge(int from, int to) {
        this.getGraph().unhighlightEdge(from, to, null, null);
    }

    public void unHighlightAll() {

        for (KripkeState state : this.getStates()) {
            this.unHighlightState(state.getId());
            for (KripkeState state2 : this.getSuccessorsOf(state)) {
                this.unHighlightEdge(state.getId(), state2.getId());
            }
        }
    }

    private Coordinates getCoordinatesOfState(KripkeState state) {
        return getCoordinatesOfState(state.getId());
    }

    private Coordinates getCoordinatesOfState(int i) {
        return (Coordinates) (this.getGraph().getNodeForIndex(i));
    }

    public Graph getGraph() {
        return graph;
    }


    public Language getLang() {
        return lang;
    }

}
