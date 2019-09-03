/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.misc.modelcheckerCTL.kripke;

import java.util.ArrayList;
import java.util.List;

public class KripkeStructure {
    int[][] adjacencyMatrix;
    List<KripkeState> states;
    KripkeState startState;

    public KripkeStructure(int[][] adjacencyMatrix, List<KripkeState> states, KripkeState startState) {
        this.adjacencyMatrix = adjacencyMatrix;
        this.startState = startState;
        this.setStates(states);
    }

    public KripkeState getStartState() {
        return startState;
    }

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public List<KripkeState> getStates() {
        return states;
    }



    public KripkeState getState(int id) {
        return states.get(id);
    }

    private void setStates(List<KripkeState> states) {
        this.states = states;

    }




    public String printStates() {
        String result = "{";
        for (int i = 0; i < states.size(); i++) {
            result += states.get(i).getName();
            if (i != states.size() - 1) result += ",";
        }
        result += "}";
        return result;

    }

    public String printRelations() {
        String result = "{";
        for (int i = 0; i < this.getAdjacencyMatrix().length; i++) {
            for (int j = 0; j < this.getAdjacencyMatrix()[0].length; j++) {
                if (this.getAdjacencyMatrix()[i][j] == 1) {
                    result += "(" + this.getStates().get(i).getName() + "," + this.getStates().get(j).getName() + ")";
                    if (!(i == this.getAdjacencyMatrix().length - 1 && j == this.getAdjacencyMatrix()[0].length - 1))
                        result += ", ";

                }
            }
        }
        result += "}";
        return result;
    }

    public String printTerminals() {
        return this.getStates().toString().replace('[', '{').replace(']', '}');
    }

    public List<KripkeState> getSuccessorsOf(KripkeState state) {
        List<KripkeState> result = new ArrayList<>();
        for (int i = 0; i < adjacencyMatrix[0].length; i++) {
            if (adjacencyMatrix[state.getId()][i] == 1) result.add(states.get(i));
        }
        return result;
    }

    public List<KripkeState> getPredecessorsOf(KripkeState state) {
        List<KripkeState> result = new ArrayList<>();
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            if (adjacencyMatrix[i][state.getId()] == 1) result.add(states.get(i));
        }
//        System.out.println("Vorgänger von "+state.getName()+" = "+Util.printSimpleStates(result));
        return result;
    }

    @Override
    public String toString() {
        return "{\tS=" + printStates() + "\n" +
                "\tI={" + getStartState().getName() + "}\n" +
                "\tR=" + printRelations() + "\n" +
                "\tL=" + printTerminals() + "\n" +
                "}";


    }
}
