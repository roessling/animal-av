/*
 * AhoCorasick.java
 * Adrian Lumpe, Chi Viet Vu, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

package generators.tree;


import algoanim.counter.model.FourValueCounter;
import algoanim.counter.model.TwoValueCounter;
import algoanim.counter.view.FourValueView;
import algoanim.counter.view.TwoValueView;
import algoanim.primitives.*;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.*;

import algoanim.primitives.generators.Language;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import translator.Translator;

public class AhoCorasick implements Generator {

    public Translator translator;
    private Language lang;
    private Trie trie;
    private int node_horizontal_distance;
    private int circleRadius;
    private String searchString;
    private PolylineProperties failureEdge;
    private PolylineProperties pp;
    private int node_vertical_distance;
    private Color node_highlight;
    private SourceCodeProperties scp_description;
    private ArrayProperties arrayProperties;
    private String[] stringsToSearch;
    private CircleProperties cp;
    private TextProperties textProperties;
    private Color queue_highlight_color;
    private SourceCode descr_1;
    private SourceCode source_code;
    private SourceCode source_code_2;
    private SourceCode source_code_3;
    private SourceCode source_code_4;


    public Text insertion_array_text;
    public StringArray insertion_array;
    public StringArray inputArray;
    public ListBasedQueue<State> queue;
    public QueueProperties qp;


    public AhoCorasick(String path, Locale locale) {
        translator = new Translator(path, locale);
    }

    public void init() {

        lang = new AnimalScript("Aho-Corasick-Algorithmus", "Adrian Lumpe, Chi Viet Vu", 800, 600);
        lang.setStepMode(true);
    }


    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        node_horizontal_distance = (Integer) primitives.get("node_horizontal_distance");
        circleRadius = (Integer) primitives.get("circleRadius");
        searchString = (String) primitives.get("searchString");
        failureEdge = (PolylineProperties) props.getPropertiesByName("failureEdge");
        node_vertical_distance = (Integer) primitives.get("node_vertical_distance");
        node_highlight = (Color) primitives.get("node_highlight");
        scp_description = (SourceCodeProperties) props.getPropertiesByName("scp_description");
        arrayProperties = (ArrayProperties) props.getPropertiesByName("arrayProperties");
        stringsToSearch = (String[]) primitives.get("stringsToSearch");
        queue_highlight_color = (Color) primitives.get("queue_highlight_color");

        textProperties = new TextProperties();
        cp = new CircleProperties();
        pp = new PolylineProperties();
        qp = new QueueProperties();
        pp.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);

        qp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, queue_highlight_color);

        TextProperties heading = new TextProperties();
        TextProperties subheading = new TextProperties();
        TextProperties hint_insert_properties = new TextProperties();
        heading.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
        subheading.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 20));
        Text heading_text = lang.newText(new Coordinates(25, 20), "Aho-Corasick-Algorithmus", "null", null, heading);

        descr_1 = lang.newSourceCode(new Offset(0, -10, heading_text, AnimalScript.DIRECTION_SW), "null", null, scp_description);
        descr_1.addCodeLine(translator.translateMessage("desc01"), null, 0, null);
        descr_1.addCodeLine(translator.translateMessage("desc02"), null, 0, null);
        descr_1.addCodeLine(translator.translateMessage("desc03"), null, 0, null);
        descr_1.addCodeLine(translator.translateMessage("desc04"), null, 0, null);
        descr_1.addCodeLine(translator.translateMessage("desc05"), null, 0, null);
        descr_1.addCodeLine(translator.translateMessage("desc06"), null, 0, null);
        descr_1.addCodeLine(translator.translateMessage("desc07"), null, 0, null);
        descr_1.addCodeLine(translator.translateMessage("desc08"), null, 0, null);
        descr_1.addCodeLine(translator.translateMessage("desc09"), null, 0, null);


        Text subheading_generate = lang.newText(new Offset(0, 10, heading_text, AnimalScript.DIRECTION_SW), translator.translateMessage("subheading_insert"), "null", null, subheading);
        Text hint_insert = lang.newText(new Offset(0, 10, subheading_generate, AnimalScript.DIRECTION_SW), translator.translateMessage("hint_insert"), "null", null, hint_insert_properties);
        insertion_array_text = lang.newText(new Offset(5, 10, hint_insert, AnimalScript.DIRECTION_SW), "stringsToSearch:", "null", null, textProperties);
        insertion_array = lang.newStringArray(new Offset(15, -5, insertion_array_text, AnimalScript.DIRECTION_NE), stringsToSearch, "null", null, arrayProperties);

        source_code = lang.newSourceCode(new Offset(0, 40, insertion_array_text, AnimalScript.DIRECTION_SW), "source", null, scp_description);
        source_code.addCodeLine("void insert(String string) {", null, 0, null); //0
        source_code.addCodeLine("Trie state = root_state;", null, 1, null); //1
        source_code.addCodeLine("for (int i = 0; i < string.length(); ++i) {", null, 1, null); //2
        source_code.addCodeLine("char c = string.charAt(i);", null, 2, null);//3
        source_code.addCodeLine("if (!state.has_child(c)) {", null, 2, null);//4
        source_code.addCodeLine("state.add_state(c);", null, 3, null);//5
        source_code.addCodeLine("}", null, 2, null);//6
        source_code.addCodeLine("state = state.get_child(c);", null, 2, null);//7
        source_code.addCodeLine("}", null, 1, null);//8
        source_code.addCodeLine("}", null, 0, null);//9

        insertion_array_text.hide();
        insertion_array.hide();
        source_code.hide();
        subheading_generate.hide();
        hint_insert.hide();

        lang.nextStep("Trie-Generation");
        descr_1.hide();
        insertion_array_text.show();
        insertion_array.show();
        insertion_array.show();
        source_code.show();
        subheading_generate.show();
        hint_insert.show();
        trie = genTrie(stringsToSearch);
        genNodes(trie);

        lang.nextStep("Construct failure transitions");
        source_code.hide();
        subheading_generate.hide();
        hint_insert.hide();
        Text subheading_connect = lang.newText(new Offset(0, 10, heading_text, AnimalScript.DIRECTION_SW), translator.translateMessage("subheading_connect"), "null", null, subheading);
        source_code_2 = lang.newSourceCode(new Offset(0, 40, insertion_array_text, AnimalScript.DIRECTION_SW), "source2", null, scp_description);

        source_code_2.addCodeLine("private void constructFailureStates() { ", null, 0, null); //0
        source_code_2.addCodeLine("final Queue<State> queue = new LinkedBlockingDeque<>();", null, 1, null); //1
        source_code_2.addCodeLine("final State startState = getRootState();", null, 1, null); //2
        source_code_2.addCodeLine("for (State depthOneState : startState.getStates()) { ", null, 1, null); //3
        source_code_2.addCodeLine("depthOneState.setFailure(startState);", null, 2, null); //4
        source_code_2.addCodeLine("queue.add(depthOneState);", null, 2, null); //5
        source_code_2.addCodeLine("}", null, 1, null); //6
        source_code_2.addCodeLine("while (!queue.isEmpty()) {", null, 1, null); //7
        source_code_2.addCodeLine("final State currentState = queue.remove();", null, 2, null); //8
        source_code_2.addCodeLine("for (final Character transition : currentState.getTransitions()) {", null, 2, null); //9
        source_code_2.addCodeLine("State targetState = currentState.nextState(transition, false);", null, 3, null); //10
        source_code_2.addCodeLine("queue.add(targetState);", null, 3, null); //11
        source_code_2.addCodeLine("while (traceFailureState.nextState(transition,false) == null) {", null, 3, null); //12
        source_code_2.addCodeLine("State traceFailureState = currentState.getFailure();", null, 4, null); //13
        source_code_2.addCodeLine("traceFailureState = traceFailureState.getFailure();", null, 4, null); //14
        source_code_2.addCodeLine("}", null, 3, null); //15
        source_code_2.addCodeLine("final State newFailureState = traceFailureState.nextState(transition, false);", null, 3, null); //16
        source_code_2.addCodeLine("targetState.setFailure(newFailureState);", null, 3, null); //17
        source_code_2.addCodeLine("} ", null, 2, null); //18
        source_code_2.addCodeLine("}", null, 1, null); //19
        source_code_2.addCodeLine("}", null, 0, null); //20

        lang.nextStep();
        trie.constructFailureStates();
        lang.nextStep("Parse text");
        source_code_2.hide();
        subheading_connect.hide();
        queue.hide();

        Text subheading_traverse = lang.newText(new Offset(0, 10, heading_text, AnimalScript.DIRECTION_SW), translator.translateMessage("subheading_traverse"), "null", null, subheading);
        source_code_3 = lang.newSourceCode(new Offset(0, 40, insertion_array_text, AnimalScript.DIRECTION_SW), "source3", null, scp_description);
        source_code_3.addCodeLine("public List<String> parseText( String text) { ", null, 0, null); //0
        source_code_3.addCodeLine("List<String> foundstringsToSearch = new LinkedList<String>", null, 1, null); //1
        source_code_3.addCodeLine(" State currentState = rootState; ", null, 1, null); //2
        source_code_3.addCodeLine("for(Character c :text.toCharArray()) { ", null, 1, null); //3
        source_code_3.addCodeLine("State nextState = currentState.getChildren().get(c);", null, 2, null); //4
        source_code_3.addCodeLine("if( nextState == null) {", null, 2, null); //5
        source_code_3.addCodeLine("nextState = currentState.getFailure();", null, 3, null); //6
        source_code_3.addCodeLine("}", null, 2, null); //7
        source_code_3.addCodeLine("currentState = nextState;", null, 2, null); //8
        source_code_3.addCodeLine("if( currentState.is_word() ) { ", null, 2, null); //9
        source_code_3.addCodeLine("foundStrings.add(currentState.getWord()", null, 3, null); //10
        source_code_3.addCodeLine("if( currentState.getChildren() == null) {", null, 3, null); //11
        source_code_3.addCodeLine("currentState = rootState", null, 4, null); //12
        source_code_3.addCodeLine("}", null, 3, null); //11
        source_code_3.addCodeLine("}", null, 2, null); //11
        source_code_3.addCodeLine("}", null, 1, null); //12
        source_code_3.addCodeLine("return foundStrings;", null, 1, null); //13
        source_code_3.addCodeLine(" }", null, 0, null); //14
        String[] chars = searchString.split("");
        inputArray = lang.newStringArray(new Offset(0, 10, source_code_3, AnimalScript.DIRECTION_SW), chars, "null", null, arrayProperties);
        lang.nextStep();
        trie.parseText();

        lang.nextStep("Summary");
        for (int i = 0; i < stringsToSearch.length; i++) {
            insertion_array.unhighlightCell(i, null, null);
        }
        insertion_array.hide();
        lang.hideAllPrimitivesExcept(heading_text);
        inputArray.hide();
        Text subheading_summary = lang.newText(new Offset(0, 10, heading_text, AnimalScript.DIRECTION_SW), translator.translateMessage("subheading_summary"), "null", null, subheading);
        source_code_4 = lang.newSourceCode(new Offset(0, 40, subheading_summary, AnimalScript.DIRECTION_SW), "source4", null, scp_description);
        source_code_4.addCodeLine(translator.translateMessage("sum01"), null, 0, null); //0
        source_code_4.addCodeLine(translator.translateMessage("sum02"), null, 0, null); //0

        return lang.toString();
    }


    public String getName() {
        return "Aho-Corasick-Algorithmus";
    }

    public String getAlgorithmName() {
        return "Aho-Corasick-Algorithmus";
    }

    public String getAnimationAuthor() {
        return "Adrian Lumpe, Chi Viet Vu";
    }

    public String getDescription() {
        return "The Aho-Corasick-Algorithm is a string matching algorithm by Alfred V. Aho and Margaret J. Corasick."
                + "\n"
                + "The algorithm is able to search multiple strings in a text very efficiently."
                + "\n"
                + "The algorithm is split into two parts:"
                + "\n"
                + "Generation of a Trie"
                + "\n"
                + "A trie is a tree-like data structure which stores the strings which are searched."
                + "\n"
                + "Each node in the tree symbolises a character and shared prefixes are stored once. "
                + "\n"
                + "Inserting a string in the trie is simply following the trie as far as possible and adding new nodes for each new character in the string."
                + "\n"
                + "Generating failure transitions"
                + "\n"
                + "In addition to having the normal trie structure the algorithm generates failure transitions for traversing the trie so that already found prefixes don't have to be followed multiple times."
                + "\n";
    }

    public String getCodeExample() {
        return "void insert(String string) "
                + "\n"
                + "    Trie state = root_state; "
                + "\n"
                + "    for (int i = 0; i < string.length(); ++i) { "
                + "\n"
                + "        char c = string.charAt(i);"
                + "\n"
                + "        if (!state.has_child(c)) {"
                + "\n"
                + "            state.add_state(c);"
                + "\n"
                + "        }"
                + "\n"
                + "        state = state.get_child(c);"
                + "\n"
                + "    }   "
                + "\n"
                + "}"
                + "\n"
                + "\n"
                + "private void constructFailureStates() { "
                + "\n"
                + "    final Queue<State> queue = new LinkedBlockingDeque<>();"
                + "\n"
                + "    final State startState = getRootState();"
                + "\n"
                + "    for (State depthOneState : startState.getStates()) { "
                + "\n"
                + "        depthOneState.setFailure(startState);"
                + "\n"
                + "        queue.add(depthOneState);"
                + "\n"
                + "    }\",  "
                + "\n"
                + "    while (!queue.isEmpty()) {"
                + "\n"
                + "        final State currentState = queue.remove();"
                + "\n"
                + "        for (final Character transition : currentState.getTransitions()) {"
                + "\n"
                + "            State targetState = currentState.nextState(transition, false); "
                + "\n"
                + "            queue.add(targetState); "
                + "\n"
                + "            while (traceFailureState.nextState(transition,false) == null) { "
                + "\n"
                + "                State traceFailureState = currentState.getFailure();"
                + "\n"
                + "                traceFailureState = traceFailureState.getFailure();"
                + "\n"
                + "            } "
                + "\n"
                + "            final State newFailureState = traceFailureState.nextState(transition, false); "
                + "\n"
                + "            targetState.setFailure(newFailureState); "
                + "\n"
                + "        } "
                + "\n"
                + "    }"
                + "\n"
                + "}"
                + "\n"
                + "\n"
                + "public List<String> parseText( String text) { "
                + "\n"
                + "    List<String> foundStrings = new LinkedList<String> "
                + "\n"
                + "    State currentState = rootState; "
                + "\n"
                + "    for(Character c :text.toCharArray()) { "
                + "\n"
                + "        State nextState = currentState.getChildren().get(c); "
                + "\n"
                + "        if( nextState == null) { "
                + "\n"
                + "            nextState = currentState.getFailure();\",  "
                + "\n"
                + "        } "
                + "\n"
                + "        currentState = nextState;"
                + "\n"
                + "        if( currentState.is_word() ) {  "
                + "\n"
                + "            foundStrings.add(currentState.getWord()\",  "
                + "\n"
                +"             if(currentState.getChildren() == null) {"
                + "\n"
                +"                 currentState = rootState;"
                + "\n"
                +"             }"
                + "\n"
                + "        } "
                + "\n"
                + "    }"
                + "\n"
                + "    return foundStrings;"
                + "\n"
                + "}";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    public Trie genTrie(String[] strings) {
        Trie trie = new Trie();
        for (String s : strings) {
            trie.addKey(s);
        }

        return trie;
    }

    public void genNodes(Trie trie) {
        trie.calc_coordinates();
        trie.getRootState().setCircle(lang.newCircle(trie.getRootState().coordinates, circleRadius, trie.rootState.gen_key(), null, cp));
        lang.newText(new Coordinates(trie.getRootState().coordinates.getX() - 12, trie.getRootState().coordinates.getY() - 6), "root", trie.getRootState().gen_key(), null);
        for (State states : trie.getRootState().getStates()) {
            genNode(states, trie.rootState);
        }
    }

    public void genNode(State s, State p) {
        s.setCircle(lang.newCircle(s.coordinates, circleRadius, s.gen_key(), null, cp));
        lang.newText(new Coordinates(s.coordinates.getX() - 5, s.coordinates.getY() - 5), String.valueOf(s.getC()), "str_" + s.gen_key(), null);

        Node[] vertices =
                {
                        new Offset(0, 0, p.gen_key(), AnimalScript.DIRECTION_S),
                        new Offset(0, 0, s.gen_key(), AnimalScript.DIRECTION_N)
                };

        lang.newPolyline(vertices, "pl_" + s.key, null, pp);

        for (State states : s.getStates()) {
            genNode(states, s);
        }
    }

    public static void main(String[] args) {
        Generator gen = new AhoCorasick("resources/AhoCorasick", Locale.US);
        Animal.startGeneratorWindow(gen);
    }

    public class State {

        private State rootState = null;
        private int depth;
        private char c;
        private int index;
        private int x;
        int mod;
        private String key;
        private State parent;
        private Coordinates coordinates;
        private Map<Character, State> success = new HashMap<>();
        private State failure;
        private Circle circle;
        private String word;

        public State() {
            this(0, '-');
        }

        String gen_key() {
            if (key == null) {
                key = ((parent == null) ? " " : (c + parent.gen_key()));
            }
            return key;
        }

        public State(int depth, char c) {
            this.depth = depth;
            this.c = c;
            this.rootState = depth == 0 ? this : null;
        }

        public State addkey(String key) {
            State state = this;

            for (Character c : key.toCharArray()) {
                state = state.addState(c);
            }

            state.setWord(key);
            return state;
        }

        public State addState(Character c) {
            State nextState = nextState(c, true);
            if (nextState == null) {
                State newState = new State(depth + 1, c);
                newState.setParent(this);
                nextState = newState;
                this.success.put(c, nextState);
            }
            return nextState;
        }

        public State nextState(Character c, boolean ignore) {
            State result;
            result = success.get(c);
            if (result == null && !ignore) {
                result = rootState;
            }
            return result;
        }

        public Collection<State> getStates() {
            return this.success.values();
        }

        public Collection<Character> getTransitions() {
            return this.success.keySet();
        }

        public char getC() {
            return c;
        }

        public void setC(char c) {
            this.c = c;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getDepth() {
            return depth;
        }

        public Map<Character, State> getSuccess() {
            return success;
        }

        public void createIndices() {
            int index = 0;
            for (State states : getStates()) {
                states.setIndex(index);
                states.createIndices();
                ++index;
            }
        }

        boolean is_leaf() {
            return (getStates().size() == 0);
        }

        //generation of Trie based on generators.tree.trie_helpers.TrieLayout

        boolean is_left_most() {
            return (index == 0);
        }

        State get_left_most_sibling() {
            return (State) parent.getStates().toArray()[0];
        }

        State get_left_sibling() {
            return (State) parent.getStates().toArray()[index - 1];
        }

        State get_left_most_child() {
            return (State) getStates().toArray()[0];
        }

        State get_right_most_child() {
            return (State) getStates().toArray()[getStates().size() - 1];
        }


        void calc_initial_x() {
            for (State child : getStates()) child.calc_initial_x();

            if (getStates().size() == 0) {
                if (!is_left_most()) x = get_left_sibling().x + circleRadius + node_horizontal_distance;
                else x = 0;
            } else if (getStates().size() == 1) {
                if (is_left_most()) x = get_left_most_child().x;
                else {
                    x = get_left_sibling().x + circleRadius + node_horizontal_distance;
                    mod = x - get_left_most_child().x;
                }
            } else {
                State left_child = get_left_most_child();
                State right_child = get_right_most_child();
                int mid = (left_child.x + right_child.x) / 2;

                if (is_left_most()) x = mid;
                else {
                    x = get_left_sibling().x + circleRadius + node_horizontal_distance;
                    mod = x - mid;
                }
            }

            if (getStates().size() > 0 && !is_left_most()) check_for_conflicts();
        }

        void check_for_conflicts() {
            int min_distance = node_horizontal_distance + circleRadius;
            int shift_value = 0;

            HashMap<Integer, Integer> left_contour = new HashMap<Integer, Integer>();
            get_left_contour(0, left_contour);
            int node_max_level = get_max_key_map(left_contour);

            for (int j = 0; j != index; ++j) {
                State sibling = (State) parent.getStates().toArray()[j];
                HashMap<Integer, Integer> sibling_right_contour = new HashMap<Integer, Integer>();
                sibling.get_right_contour(0, sibling_right_contour);

                int level_limit = Math.min(get_max_key_map(sibling_right_contour), node_max_level);
                for (int level = depth; level <= level_limit; ++level) {
                    int distance = left_contour.get(level) - sibling_right_contour.get(level);
                    shift_value = Math.max(shift_value, min_distance - distance);
                }

                if (shift_value > 0) {
                    x += shift_value;
                    mod += shift_value;
                    center_nodes_between(parent.get_right_most_child());
                }
            }
        }

        void center_nodes_between(State state) {
            int left_index = index;
            int right_index = state.getIndex();
            int num_nodes_between = (right_index - left_index) - 1;

            if (num_nodes_between > 0) {
                float distance_between_nodes = (float) (x - state.getX()) / (float) (num_nodes_between + 1);

                int count = 1;
                for (int j = left_index + 1; j < right_index; ++j) {
                    State middle_node = (State) parent.getStates().toArray()[j];

                    float desired_x = (float) state.getX() + (float) (distance_between_nodes * count);
                    float offset = desired_x - (float) middle_node.x;
                    middle_node.x += (int) offset;
                    middle_node.mod += (int) offset;

                    ++count;
                }
                check_for_conflicts();
            }
        }

        void get_left_contour(int mod_sum, HashMap<Integer, Integer> values) {
            if (!values.containsKey(depth) || (x + mod_sum) < values.get(depth)) values.put(depth, x + mod_sum);
            for (State child : getStates()) child.get_left_contour(mod_sum + mod, values);
        }

        void get_right_contour(int mod_sum, HashMap<Integer, Integer> values) {
            if (!values.containsKey(depth) || (x + mod_sum) > values.get(depth)) values.put(depth, x + mod_sum);
            for (State child : getStates()) child.get_right_contour(mod_sum + mod, values);
        }


        void calc_final_position(int mod_sum) {
            x += mod_sum;
            mod_sum += mod;

            coordinates = new Coordinates(x + 600, depth * node_vertical_distance + 120);

            for (State states : getStates()) {
                states.calc_final_position(mod_sum);
            }
        }

        public State getParent() {
            return parent;
        }

        public void setParent(State parent) {
            this.parent = parent;
        }

        public int getX() {
            return x;
        }

        public Coordinates getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(Coordinates coordinates) {
            this.coordinates = coordinates;
        }

        public void setFailure(State failure) {
            this.failure = failure;

            Node[] vertices =
                    {
                            new Offset(0, 0, this.gen_key(), AnimalScript.DIRECTION_N),
                            new Offset(0, 0, failure.gen_key(), AnimalScript.DIRECTION_S)
                    };

            lang.newPolyline(vertices, "pl_" + this.key, null, failureEdge);

        }

        public State getFailure() {


            return this.failure;
        }

        public Circle getCircle() {
            return circle;
        }

        public void setCircle(Circle circle) {
            this.circle = circle;
        }

        @Override
        public String toString() {
            return new StringBuilder(gen_key()).reverse().toString();
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }
    }

    public class Trie {
        private State rootState;

        public Trie() {
            this.rootState = new State();
            this.rootState.setIndex(0);
            this.rootState.failure = rootState;
        }

        public void addKey(String key) {
            rootState.addkey(key);
        }

        public State getRootState() {
            return rootState;
        }

        public void calc_coordinates() {
            this.rootState.createIndices();
            this.rootState.calc_initial_x();

            rootState.calc_final_position(0);
        }

        private void constructFailureStates() {
            CounterProperties cp = new CounterProperties(); // Zaehler-Properties anlegen
            cp.set(AnimationPropertiesKeys.FILLED_PROPERTY, true); // gefuellt...
            cp.set(AnimationPropertiesKeys.FILL_PROPERTY, queue_highlight_color);
            queue = lang.newListBasedQueue(new Offset(0, 50, source_code_2, AnimalScript.DIRECTION_SW), new LinkedList<State>(), "queue", null, qp);
            FourValueCounter counter = lang.newCounter(queue); // Zaehler anlegen
            counter.activateCounting();
            FourValueView view = lang.newCounterView(counter,
                    new Offset(0, 100, source_code_2, AnimalScript.DIRECTION_SW), cp, true, true);
            final State startState = getRootState();

            // First, set the fail state of all depth 1 states to the root state
            source_code_2.highlight(2);
            startState.getCircle().changeColor("", node_highlight, null, null);

            lang.nextStep();
            source_code_2.unhighlight(2);
            source_code_2.highlight(3);
            for (State depthOneState : startState.getStates()) {
                queue.unhighlightTailCell(null, null);
                source_code_2.unhighlight(3);
                source_code_2.unhighlight(5);
                source_code_2.highlight(4);
                depthOneState.getCircle().changeColor("", node_highlight, null, null);
                depthOneState.setFailure(startState);
                lang.nextStep();
                depthOneState.getCircle().changeColor("", Color.BLACK, null, null);
                source_code_2.unhighlight(4);
                source_code_2.highlight(5);
                queue.enqueue(depthOneState);
                queue.highlightTailCell(null, null);
                lang.nextStep();
            }
            queue.unhighlightTailCell(null, null);
            source_code_2.unhighlight(4);
            source_code_2.unhighlight(5);
            source_code_2.unhighlight(6);
            startState.getCircle().changeColor("", Color.BLACK, null, null);
            source_code_2.highlight(7);

            // Second, determine the fail state for all depth > 1 state
            while (!queue.isEmpty()) {
                source_code_2.unhighlight(9);
                queue.highlightFrontCell(null, null);
                source_code_2.unhighlight(7);
                source_code_2.highlight(8);
                lang.nextStep();
                queue.unhighlightFrontCell(null, null);
                final State currentState = queue.dequeue();
                source_code_2.unhighlight(8);
                source_code_2.highlight(9);
                lang.nextStep();
                for (final Character transition : currentState.getTransitions()) {
                    source_code_2.unhighlight(9);
                    source_code_2.highlight(10);
                    State targetState = currentState.nextState(transition, false);
                    targetState.getCircle().changeColor("", node_highlight, null, null);
                    lang.nextStep();
                    source_code_2.unhighlight(10);
                    source_code_2.highlight(11);
                    lang.nextStep();
                    queue.enqueue(targetState);
                    queue.highlightTailCell(null, null);
                    lang.nextStep();
                    source_code_2.unhighlight(11);
                    source_code_2.highlight(12);
                    queue.unhighlightTailCell(null, null);
                    State traceFailureState = currentState.getFailure();
                    traceFailureState.getCircle().changeColor("", node_highlight, null, null);
                    lang.nextStep();
                    source_code_2.unhighlight(12);
                    source_code_2.highlight(13);
                    lang.nextStep();
                    while (traceFailureState.nextState(transition, false) == null) {
                        source_code_2.unhighlight(13);
                        source_code_2.highlight(14);
                        source_code_2.highlight(15);
                        traceFailureState.getCircle().changeColor("", Color.BLACK, null, null);
                        traceFailureState = traceFailureState.getFailure();
                        traceFailureState.getCircle().changeColor("", node_highlight, null, null);
                        lang.nextStep();
                        source_code_2.unhighlight(14);
                        source_code_2.unhighlight(15);
                        source_code_2.highlight(13);
                        lang.nextStep();
                    }
                    source_code_2.unhighlight(13);
                    source_code_2.highlight(16);
                    source_code_2.highlight(17);
                    final State newFailureState = traceFailureState.nextState(transition, false);
                    targetState.setFailure(newFailureState);
                    lang.nextStep();
                    source_code_2.unhighlight(16);
                    source_code_2.unhighlight(17);
                    newFailureState.getCircle().changeColor("", Color.BLACK, null, null);
                    targetState.getCircle().changeColor("", Color.BLACK, null, null);
                    lang.nextStep();
                }
            }
            source_code_2.unhighlight(9);
            view.hide();
        }


        public void parseText() {
            State currentState = this.rootState;
            currentState.getCircle().changeColor("", node_highlight, null, null);
            for (int i = 0; i < searchString.length(); i++) {
                source_code_3.unhighlight(9);
                source_code_3.unhighlight(11);
                source_code_3.unhighlight(12);
                inputArray.unhighlightCell(i - 1, null, null);
                inputArray.highlightCell(i, null, null);
                source_code_3.highlight(3);
                lang.nextStep();
                source_code_3.unhighlight(3);
                source_code_3.highlight(4);
                lang.nextStep();
                source_code_3.unhighlight(4);
                source_code_3.highlight(5);
                lang.nextStep();
                State nextState = currentState.getSuccess().get(searchString.charAt(i));
                if (nextState == null) {
                    source_code_3.unhighlight(5);
                    source_code_3.highlight(6);
                    nextState = currentState.getFailure();
                    lang.nextStep();
                }
                source_code_3.unhighlight(5);
                source_code_3.unhighlight(6);
                source_code_3.highlight(8);
                currentState.getCircle().changeColor("", Color.BLACK, null, null);
                currentState = nextState;
                currentState.getCircle().changeColor("", node_highlight, null, null);
                lang.nextStep();
                source_code_3.unhighlight(8);
                source_code_3.highlight(9);
                lang.nextStep();
                if (currentState.getWord() != null) {
                    source_code_3.unhighlight(9);
                    source_code_3.highlight(10);
                    for (int j = 0; j < stringsToSearch.length; j++) {
                        if (currentState.getWord().equals(stringsToSearch[j])) {
                            insertion_array.highlightCell(j, null, null);
                        }
                    }
                    lang.nextStep();
                    source_code_3.unhighlight(10);
                    source_code_3.highlight(11);
                    lang.nextStep();
                    if(currentState.getStates().isEmpty()) {
                        currentState.getCircle().changeColor("", Color.BLACK, null, null);
                        currentState = rootState;
                        currentState.getCircle().changeColor("", node_highlight, null, null);
                        source_code_3.unhighlight(11);
                        source_code_3.highlight(12);
                        lang.nextStep();
                    }

                }
            }
            currentState.getCircle().changeColor("", Color.BLACK, null, null);
            inputArray.unhighlightCell(stringsToSearch.length - 1, null, null);
            inputArray.hide();
        }
    }

    static int get_max_key_map(HashMap<Integer, Integer> hmap) {
        int max = 0;
        for (Integer val : hmap.keySet()) if (val > max) max = val;
        return max;
    }

}