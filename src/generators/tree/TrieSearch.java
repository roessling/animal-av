/*
 * TrieSearch.java
 * Jan-Henrik Kriechel, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.tree;

import java.awt.Font;

import generators.framework.Generator;
import generators.framework.ValidatingGenerator;
import generators.framework.GeneratorType;
import java.util.Locale;

import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import java.awt.Color;
import algoanim.properties.TextProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.ArrayProperties;

import algoanim.properties.AnimationPropertiesKeys;
import generators.tree.trie_helpers.AnimatedTrieSearch;
import generators.tree.trie_helpers.InformationDisplaySearch;
import generators.tree.trie_helpers.Trie;
import generators.tree.trie_helpers.TrieLayout;
import translator.Translator;

public class TrieSearch implements ValidatingGenerator {
    private Language lang;
    private Translator translator;

    // can be initialised like this : TrieSearch("resources/TrieSearch", Locale.US)
    public TrieSearch(String path, Locale locale) {
        translator = new Translator(path, locale);
    }

    public void init(){
        lang = new AnimalScript("Trie : Search", "Jan-Henrik Kriechel", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        TextProperties tp_heading = (TextProperties)props.getPropertiesByName("tp_heading");
        RectProperties rect_properties = (RectProperties)props.getPropertiesByName("rect_properties");
        SourceCodeProperties scp_source_code = (SourceCodeProperties)props.getPropertiesByName("scp_source_code");
        Color node_highlight_color = (Color)primitives.get("node_highlight_color");
        TextProperties tp_default = (TextProperties)props.getPropertiesByName("tp_default");
        SourceCodeProperties scp_description = (SourceCodeProperties)props.getPropertiesByName("scp_description");
        String[] allStrings = (String[])primitives.get("allStrings");
        String[] stringsToSearch = (String[])primitives.get("stringsToSearch");
        Color node_default_color = (Color)primitives.get("node_default_color");
        ArrayProperties array_properties = (ArrayProperties)props.getPropertiesByName("array_properties");
        int circle_radius = (int)primitives.get("circle_radius");
        int horizontal_spacing = (int)primitives.get("horizontal_spacing");
        int vertical_spacing = (int)primitives.get("vertical_spacing");

        tp_heading.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 18));
        tp_default.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 16));
        scp_description.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 15));
        scp_source_code.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 14));
        array_properties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 16));

        // 1. generate the trie
        Trie trie = new Trie();
        for (String str : allStrings)
        {
            trie.insert(str);
        }

        // 2. compute a layout from the nodes in order to display the trie
        int start_x = 600;
        TrieLayout layout = new TrieLayout(trie, circle_radius, horizontal_spacing, vertical_spacing, start_x);

        // 3. display the trie
        InformationDisplaySearch info_display = new InformationDisplaySearch(lang, translator, allStrings, stringsToSearch, rect_properties, tp_heading, tp_default, scp_description, scp_source_code, array_properties);
        info_display.ShowDescription();
        lang.nextStep();
        info_display.HideDescription();

        info_display.ShowInformation();

        Variables vars = lang.newVariables();
        vars.declare("string", "string", "");

        AnimatedTrieSearch animated_trie = new AnimatedTrieSearch(lang, vars, layout, node_default_color, node_highlight_color, info_display);
        animated_trie.init(allStrings);
        lang.nextStep(translator.translateMessage("animation"));

        for (String str : stringsToSearch)
        {
            vars.set("string", str);
            info_display.HighlightNextWord();
            info_display.FillString(str);
            boolean found = animated_trie.search(str);
            if (found) info_display.search_array.highlightCell(info_display.cur_string_idx - 1, null, null);
        }
        info_display.HighlightNextWord();
        vars.set("string", "");
        vars.discard("string");

        info_display.HideInformation();

        info_display.ShowSummary();
        lang.nextStep(translator.translateMessage("summary"));

        return lang.toString();
    }

    public boolean validateInput(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        int circle_radius = (int)primitives.get("circle_radius");
        int horizontal_spacing = (int)primitives.get("horizontal_spacing");
        int vertical_spacing = (int)primitives.get("vertical_spacing");

        if (circle_radius <= 10 || circle_radius >= 50) throw new IllegalArgumentException(translator.translateMessage("radius_error") + "(10, 50)");
        if (horizontal_spacing <= 10 || horizontal_spacing >= 100) throw new IllegalArgumentException(translator.translateMessage("hs_error") + "(10, 100)");
        if (vertical_spacing <= 10 || vertical_spacing >= 200) throw new IllegalArgumentException(translator.translateMessage("vs_error") + "(10, 200)");
        if (vertical_spacing < circle_radius || horizontal_spacing < circle_radius) throw new IllegalArgumentException(translator.translateMessage("sp_error"));

        String[] allStrings = (String[])primitives.get("allStrings");
        String[] stringsToSearch = (String[])primitives.get("stringsToSearch");
        if (allStrings.length == 0) throw new IllegalArgumentException(translator.translateMessage("as_error"));
        if (stringsToSearch.length == 0) throw new IllegalArgumentException(translator.translateMessage("sts_error"));

        for (String str : allStrings) {
            if (str.length() == 0 || str.length() > 14) throw new IllegalArgumentException(translator.translateMessage("str_len_error"));
            for (int i = 0; i != str.length(); ++i)
            {
                char c = str.charAt(i);
                if (c < 'a' || c > 'z') throw new IllegalArgumentException(translator.translateMessage("str_ch_error"));
            }
        }

        for (String str : stringsToSearch) {
            if (str.length() == 0 || str.length() > 14) throw new IllegalArgumentException(translator.translateMessage("str_len_error"));
            for (int i = 0; i != str.length(); ++i)
            {
                char c = str.charAt(i);
                if (c < 'a' || c > 'z') throw new IllegalArgumentException(translator.translateMessage("str_ch_error"));
            }
        }

        return true;
    }

    public String getName() {
        return "Trie : Search";
    }

    public String getAlgorithmName() {
        return "Trie : Search";
    }

    public String getAnimationAuthor() {
        return "Jan-Henrik Kriechel";
    }

    public String getDescription(){
        return "A trie is a tree-like data structure that is used to store strings in such a\n"
            + "way that they can be efficiently retrieved.\n"
            + "Each node in the tree may have one outgoing branch for every character in the\n"
            + "alphabet, so looking up a string is as simple as following its prefix path from\n"
            + "the root node to a specially marked end node.\n"
            + "This also means that shared prefixes are only stored once.\n\n"
            + "To search a string in a trie, follow its prefix path (always take the branch\n"
            + "that corresponds to the string's next character).\n"
            + "If there is no branch matching the next character, the string is not contained.\n"
            + "Otherwise, if the end of the string has been reached, the current node is checked\n"
            + "to see if its endOfString flag is set.\n"
            + "If this is the case, the string is contained; otherwise it is not.\n"
            + "The complexity of search is O(M) where M is the length of the string.";
    }

    public String getCodeExample(){
       return "boolean search(String string) {\n"
          + "  TrieNode node = root_node;\n"
          + "  for (int i = 0; i < string.length(); ++i) {\n"
          + "    char c = string.charAt(i);\n"
          + "    if (!node.has_child(c)) {\n"
          + "      return false;\n"
          + "    }\n"
          + "    node = node.get_child(c);\n"
          + "  }\n"
          + "  return node.is_end_of_word;\n"
          + "}";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}
