/**  * Timm Welz, 2018 for the Animal project at TU Darmstadt.  * Copying this file for educational purposes is permitted without further authorization.  */ package generators.misc.modelcheckerCTL.kripke;

import generators.misc.modelcheckerCTL.Util;
import generators.misc.modelcheckerCTL.token.Token;
import generators.misc.modelcheckerCTL.token.TokenComparator;
import generators.misc.modelcheckerCTL.token.Tokenizer;
import generators.misc.modelcheckerCTL.token.Type;

import java.util.ArrayList;
import java.util.List;

public class StructureBuilder {

    public List<KripkeState> buildStates(List<Token> tokens) {
        List<KripkeState> states = new ArrayList<>();
        int idCounter = 0;

        for (Token token : tokens) {
            if (token.getType() == Type.TERMINAL) {
                if (containsState(states, token))
                    throw new IllegalArgumentException("bad state format: state already exists: " + token.toString());
                states.add(new KripkeState(token, idCounter));
                idCounter++;
            }
        }

        return states;
    }

    private boolean containsState(List<KripkeState> states, Token token) {
        boolean result = false;
        for (KripkeState state : states) {
            if (state.getToken().equals(token)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public void buildTerminals(List<KripkeState> states, List<Token> tokens) {
        List<Token> tmpTerminals;
        int tmpStart, tmpEnd;
        for (KripkeState state : states) {
            tmpStart = tokens.indexOf(state.getToken()) + 2;
            if (tmpStart < tokens.size()) {
                tmpEnd = Util.indexOfClosingBracket(tokens.toArray(new Token[tokens.size()]), tmpStart);
                tmpTerminals = getTerminals(tokens, tmpStart, tmpEnd);
                tmpTerminals.sort(new TokenComparator());
                state.setTerminals(tmpTerminals);
            } else throw new IllegalArgumentException("bad state terminal format");
        }
    }

    private List<Token> getTerminals(List<Token> tokens, int start, int end) {
        List<Token> terminals = new ArrayList<>();
        Token tmp;
        for (int i = start; i <= end; i++) {
            tmp = tokens.get(i);
            if (tmp.getType() == Type.TERMINAL) {
                terminals.add(tmp);

            }
        }
        //terminals.sort(new TokenComparator());
        return terminals;
    }

    public int[][] buildStateRelations(List<KripkeState> states, List<Token> tokens) {
//        System.out.println(tokens.toString());
        int[][] adjacencyMatrix = new int[states.size()][states.size()];
        List<Token> tmpTokens = getTerminals(tokens, 0, tokens.size() - 1);
//        System.out.println(tmpTokens.toString());
        KripkeState tmpState = null;
        int row, column;
        if (tmpTokens.size() % 2 == 0) {
            for (int i = 0; i < tmpTokens.size() - 1; i += 2) {
                tmpState = getState(states, tmpTokens.get(i));
                if (tmpState == null)
                    throw new IllegalArgumentException("bad state relation format: unknown state: " + tmpTokens.get(i));
                row = tmpState.getId();

                tmpState = getState(states, tmpTokens.get(i + 1));
                if (tmpState == null)
                    throw new IllegalArgumentException("bad state relation format: unknown state: " + tmpTokens.get(i + 1));
                column = tmpState.getId();
                adjacencyMatrix[row][column] = 1;
            }
        } else throw new IllegalArgumentException("bad state relation format: " + tokens.toString());
        return adjacencyMatrix;
    }

    public KripkeState getState(List<KripkeState> states, Token token) {
        KripkeState result = null;
        for (KripkeState state : states) {
            if (state.getToken().equals(token)) {
                result = state;
            }
        }
        return result;
    }

    public List<Token> removeOuterBrackets(List<Token> tokens) {
        List<Token> result;
        if (tokens.get(0).getType() == Type.BRACKETS_C_OPEN && tokens.get(tokens.size() - 1).getType() == Type.BRACKETS_C_CLOSED) {
            result = tokens.subList(1, tokens.size() - 2);
        } else result = tokens;

        return result;
    }

    public KripkeState buildStartState(List<KripkeState> states, List<Token> tokens) {
        KripkeState result = null;
        for (Token token : tokens) {
            if (token.getType() == Type.TERMINAL) {
                if (result == null) {
                    result = getState(states, token);

                } else throw new IllegalArgumentException("bad start state format:" + tokens.toString());
            }
        }
        if (result == null)throw new IllegalArgumentException("bad start state format:" + tokens.toString());
        return result;
    }

    public KripkeStructure buildStructure(String stateStr, String startSt, String relations, String labels) {
        List<KripkeState> states = buildStates(new Tokenizer(stateStr).getTokens());
        KripkeState startState = buildStartState(states,new Tokenizer(startSt).getTokens());
        int[][] aM = buildStateRelations(states, new Tokenizer(relations).getTokens());

        buildTerminals(states, new Tokenizer(labels).getTokens());

        KripkeStructure structure = new KripkeStructure(aM, states, startState);
        return structure;
    }


    public static void main(String[] args) {
        String string = "{q0,q1,q2,q3}";
        String str2 = "{ ( q0,{a} ),( q1,{b, p, a} ),( q2,{a, p,q} ),( q3,{b} ) }";
        String rel = "{ ( q0,q1),( q0,q3),( q1,q1),( q1,q2),( q2,q2),( q2,q3),( q3,q3) }";
        StructureBuilder builder = new StructureBuilder();
        List<KripkeState> states = builder.buildStates(new Tokenizer(string).getTokens());
        System.out.println(states.toString());
        builder.buildTerminals(states, new Tokenizer(str2).getTokens());
        System.out.println(states.toString());
        int[][] aM = builder.buildStateRelations(states, new Tokenizer(rel).getTokens());
        for (int i = 0; i < aM.length; i++) {
            for (int j = 0; j < aM[0].length; j++) {
                System.out.print(aM[i][j] + " ");
            }
            System.out.println();
        }
    }

}
