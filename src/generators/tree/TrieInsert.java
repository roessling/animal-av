/*
 * TrieInsert.java
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
import generators.tree.trie_helpers.AnimatedTrie;
import generators.tree.trie_helpers.InformationDisplay;
import generators.tree.trie_helpers.Trie;
import generators.tree.trie_helpers.TrieLayout;
import translator.Translator;

public class TrieInsert implements ValidatingGenerator {
    private Language lang;
    private Translator translator;

    // can be initialised like this : TrieInsert("resources/TrieInsert", Locale.US)
    public TrieInsert(String path, Locale locale) {
        translator = new Translator(path, locale);
    }

    public void init() {
        lang = new AnimalScript("Trie : Insert", "Jan-Henrik Kriechel", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        TextProperties tp_heading = (TextProperties)props.getPropertiesByName("tp_heading");
        RectProperties rect_properties = (RectProperties)props.getPropertiesByName("rect_properties");
        SourceCodeProperties scp_source_code = (SourceCodeProperties)props.getPropertiesByName("scp_source_code");
        Color node_highlight_color = (Color)primitives.get("node_highlight_color");
        TextProperties tp_default = (TextProperties)props.getPropertiesByName("tp_default");
        String[] stringsToInsert = (String[])primitives.get("stringsToInsert");
        SourceCodeProperties scp_description = (SourceCodeProperties)props.getPropertiesByName("scp_description");
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
        for (String str : stringsToInsert)
        {
            trie.insert(str);
        }

        // 2. compute a layout from the nodes in order to display the trie
        int start_x = 600;
        TrieLayout layout = new TrieLayout(trie, circle_radius, horizontal_spacing, vertical_spacing, start_x);

        // 3. display the trie
        InformationDisplay info_display = new InformationDisplay(lang, translator, stringsToInsert, rect_properties, tp_heading, tp_default, scp_description, scp_source_code, array_properties);
        info_display.ShowDescription();
        lang.nextStep();
        info_display.HideDescription();

        info_display.ShowInformation();

        Variables vars = lang.newVariables();
        vars.declare("string", "string", "");

        AnimatedTrie animated_trie = new AnimatedTrie(lang, vars, layout, node_default_color, node_highlight_color, info_display);
        lang.nextStep(translator.translateMessage("animation"));

        for (String str : stringsToInsert)
        {
            vars.set("string", str);
            info_display.HighlightNextWord();
            info_display.FillString(str);
            animated_trie.insert(str);
            info_display.IncrNumOfInsertedStrings();
        }
        info_display.HighlightNextWord();
        vars.set("string", "");
        vars.discard("string");

        info_display.HideInformation();

        info_display.insertion_array_text.setText(translator.translateMessage("inserted_strings"), null, null);
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

        String[] stringsToInsert = (String[])primitives.get("stringsToInsert");
        if (stringsToInsert.length == 0) throw new IllegalArgumentException(translator.translateMessage("sti_error"));

        for (String str : stringsToInsert) {
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
        return "Trie : Insert";
    }

    public String getAlgorithmName() {
        return "Trie : Insert";
    }

    public String getAnimationAuthor() {
        return "Jan-Henrik Kriechel";
    }

    public String getDescription(){
        return "A trie is a tree-like data structure that is used to store strings in such a"
 +"\n"
 +"way that they can be efficiently retrieved."
 +"\n"
 +"Each node in the tree may have one outgoing branch for every character in the"
 +"\n"
 +"alphabet, so looking up a string is as simple as following its prefix path from"
 +"\n"
 +"the root node to a specially marked end node."
 +"\n"
 +"This also means that shared prefixes are only stored once."
 +"\n"
 +"\n"
 +"When inserting a string, the prefix path in the trie is followed as far as possible."
 +"\n"
 +"If the end of the new string is reached, the corresponding node gets tagged to"
 +"\n"
 +"indicate the string's end."
 +"\n"
 +"Otherwise, if the end of the path in the trie is reached, new nodes are added"
 +"\n"
 +"for the remaining characters and the last node gets tagged."
 +"\n"
 +"The complexity of insertion is O(M) where M is the length of the string.";
    }

    public String getCodeExample(){
        return "void insert(String string) {"
 +"\n"
 +"  TrieNode node = root_node;"
 +"\n"
 +"  for (int i = 0; i < string.length(); ++i) {"
 +"\n"
 +"    char c = string.charAt(i);"
 +"\n"
 +"    if (!node.has_child(c)) {"
 +"\n"
 +"      node.add_child(c);\", null, 3, null);"
 +"\n"
 +"    }"
 +"\n"
 +"    node = node.get_child(c);"
 +"\n"
 +"  }"
 +"\n"
 +"  node.is_end_of_word = true;"
 +"\n"
 +"}";
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
