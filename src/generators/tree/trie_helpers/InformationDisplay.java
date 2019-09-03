package generators.tree.trie_helpers;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import translator.Translator;

public class InformationDisplay
{
	Language lang;
	public Translator translator;

	Text ds_descr_heading;
	Text ins_descr_heading;
	Text ani_descr_heading;
	SourceCode ds_descr;
	SourceCode ins_descr;
	SourceCode ani_descr;
	Rect description_rect;

	RectProperties rp;
	TextProperties tp_heading;
	TextProperties tp_default;
	SourceCodeProperties scp_description;
	SourceCodeProperties scp_source_code;
	ArrayProperties ap;

	SourceCode src_code;
	Rect src_rect;

	Rect info_rect;
	Text current_string;
	Text num_of_strings_in_tree_str;
	Text num_of_nodes_str;
	public Text insertion_array_text;
	StringArray insertion_array;
	StringArray string_array;
	int num_of_nodes;
	int num_of_strings_in_tree;
	String[] insertion_list;

	public InformationDisplay(Language lang_, Translator translator_, String[] insertion_list_, RectProperties rp_, TextProperties tp_heading_, TextProperties tp_default_, SourceCodeProperties scp_description_, SourceCodeProperties scp_source_code_, ArrayProperties ap_)
	{
		lang = lang_;
		translator = translator_;
		insertion_list = insertion_list_;

		rp = rp_;
		tp_heading = tp_heading_;
		tp_default = tp_default_;
		scp_description = scp_description_;
		scp_source_code = scp_source_code_;
		ap = ap_;

		TextProperties heading = new TextProperties();
		heading.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
		heading.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
		heading.set(AnimationPropertiesKeys.COLOR_PROPERTY, new Color(150, 210, 180));

		Text heading_text = lang.newText(new Coordinates(25, 20), translator.translateMessage("heading_text"), "null", null, heading);
		lang.newRect(new Offset(-7, -7, heading_text, AnimalScript.DIRECTION_NW),
				new Offset(7, 7, heading_text, AnimalScript.DIRECTION_SE), "heading_rect", null, rp);

		num_of_nodes = 0;
		num_of_strings_in_tree = 0;
	}

	public void ShowDescription()
	{
		description_rect = lang.newRect(new Offset(0, 10, "heading_rect", AnimalScript.DIRECTION_SW),
				new Offset(640, 500, "heading_rect", AnimalScript.DIRECTION_SW), "description_rect", null, rp);

		ds_descr_heading = lang.newText(new Offset(10, 10, "description_rect", AnimalScript.DIRECTION_NW), translator.translateMessage("ds_heading"), "null", null, tp_heading);
		ds_descr = lang.newSourceCode(new Offset(0, -10, ds_descr_heading, AnimalScript.DIRECTION_SW), "null", null, scp_description);
		ds_descr.addCodeLine(translator.translateMessage("ds_desc01"), null, 0, null);
		ds_descr.addCodeLine(translator.translateMessage("ds_desc02"), null, 0, null);
		ds_descr.addCodeLine(translator.translateMessage("ds_desc03"), null, 0, null);
		ds_descr.addCodeLine(translator.translateMessage("ds_desc04"), null, 0, null);
		ds_descr.addCodeLine(translator.translateMessage("ds_desc05"), null, 0, null);
		ds_descr.addCodeLine(translator.translateMessage("ds_desc06"), null, 0, null);

		lang.nextStep(translator.translateMessage("introduction"));
		ins_descr_heading = lang.newText(new Offset(0, 20, ds_descr, AnimalScript.DIRECTION_SW), translator.translateMessage("ins_heading"), "null", null, tp_heading);
		ins_descr = lang.newSourceCode(new Offset(0, -10, ins_descr_heading, AnimalScript.DIRECTION_SW), "null", null, scp_description);
		ins_descr.addCodeLine(translator.translateMessage("ins_desc01"), null, 0, null);
		ins_descr.addCodeLine(translator.translateMessage("ins_desc02"), null, 0, null);
		ins_descr.addCodeLine(translator.translateMessage("ins_desc03"), null, 0, null);
		ins_descr.addCodeLine(translator.translateMessage("ins_desc04"), null, 0, null);
		ins_descr.addCodeLine(translator.translateMessage("ins_desc05"), null, 0, null);
		ins_descr.addCodeLine(translator.translateMessage("ins_desc06"), null, 0, null);

		lang.nextStep();
		ani_descr_heading = lang.newText(new Offset(0, 20, ins_descr, AnimalScript.DIRECTION_SW), translator.translateMessage("ani_heading"), "null", null, tp_heading);
		ani_descr = lang.newSourceCode(new Offset(0, -10, ani_descr_heading, AnimalScript.DIRECTION_SW), "null", null, scp_description);
		ani_descr.addCodeLine(translator.translateMessage("ani_desc01"), null, 0, null);
		ani_descr.addCodeLine(translator.translateMessage("ani_desc02"), null, 0, null);
		ani_descr.addCodeLine(translator.translateMessage("ani_desc03"), null, 0, null);
		ani_descr.addCodeLine(translator.translateMessage("ani_desc04"), null, 0, null);
		ani_descr.addCodeLine(translator.translateMessage("ani_desc05"), null, 0, null);
	}

	public void HideDescription()
	{
		ds_descr.hide();
		ins_descr.hide();
		ani_descr.hide();
		ds_descr_heading.hide();
		ins_descr_heading.hide();
		ani_descr_heading.hide();
		description_rect.hide();
	}

	public void ShowInformation()
	{
		insertion_array_text = lang.newText(new Offset(5, 30, "heading_rect", AnimalScript.DIRECTION_SW), "stringsToInsert", "null", null, tp_default);
		insertion_array = lang.newStringArray(new Offset(15, -5, insertion_array_text, AnimalScript.DIRECTION_NE), insertion_list, "null", null, ap);
		lang.newRect(new Offset(-5, -10, insertion_array_text, AnimalScript.DIRECTION_NW),
				new Offset(5, 5, insertion_array, AnimalScript.DIRECTION_SE), "insertion_array_rect", null, rp);

		src_code = lang.newSourceCode(new Offset(5, 15, "insertion_array_rect", AnimalScript.DIRECTION_SW), "sourceCode", null, scp_source_code);
		src_code.addCodeLine("void insert(String string) {", null, 0, null);
		src_code.addCodeLine("TrieNode node = root_node;", null, 1, null);
		src_code.addCodeLine("for (int i = 0; i < string.length(); ++i) {", null, 1, null);
		src_code.addCodeLine("char c = string.charAt(i);", null, 2, null);
		src_code.addCodeLine("if (!node.has_child(c)) {", null, 2, null);
		src_code.addCodeLine("node.add_child(c);", null, 3, null);
		src_code.addCodeLine("}", null, 2, null);
		src_code.addCodeLine("node = node.get_child(c);", null, 2, null);
		src_code.addCodeLine("}", null, 1, null);
		src_code.addCodeLine("node.is_end_of_word = true;", null, 1, null);
		src_code.addCodeLine("}", null, 0, null);

		src_rect = lang.newRect(new Offset(-5, -5, src_code, AnimalScript.DIRECTION_NW),
				new Offset(5, 5, src_code, AnimalScript.DIRECTION_SE), "src_rect", null, rp);

		current_string = lang.newText(new Offset(5, 20, "src_rect", AnimalScript.DIRECTION_SW),
				"string = ", "null", null, tp_default);

		num_of_strings_in_tree_str = lang.newText(new Offset(0, 35, current_string, AnimalScript.DIRECTION_SW),
				translator.translateMessage("num_of_inserted_strings") + num_of_strings_in_tree, "null", null, tp_default);
		num_of_nodes_str = lang.newText(new Offset(0, 5, num_of_strings_in_tree_str, AnimalScript.DIRECTION_SW),
				translator.translateMessage("num_of_nodes") + num_of_nodes, "null", null, tp_default);

		info_rect = lang.newRect(new Offset(0, 10, "src_rect", AnimalScript.DIRECTION_SW),
				new Offset(0, 120, "src_rect", AnimalScript.DIRECTION_SE), "info_rect", null, rp);

		String[] data = new String[14];
		for (int i = 0; i != 14; ++i) data[i] = " ";
		string_array = lang.newStringArray(new Offset(0, -5, current_string, AnimalScript.DIRECTION_NE),
				data, "null", null, ap);
	}

	public void HideInformation()
	{
		current_string.hide();
		num_of_strings_in_tree_str.hide();
		num_of_nodes_str.hide();
		string_array.hide();
		src_code.hide();
		info_rect.hide();
		src_rect.hide();
	}

	public void FillString(String str)
	{
		for (int i = 0; i != 12; ++i)
		{
			if (i < str.length())
			{
				string_array.put(i, String.valueOf(str.charAt(i)), null, null);
			}
			else string_array.put(i, " ", null, null);
		}
	}

	public void HighlightNextWord()
	{
		for (int i = 0; i != 12; ++i) string_array.put(i, " ", null, null);
		if (num_of_strings_in_tree != 0) insertion_array.unhighlightElem(num_of_strings_in_tree - 1, null, null);
		if (num_of_strings_in_tree != insertion_list.length) insertion_array.highlightElem(num_of_strings_in_tree, null, null);
	}

	public void IncrNumOfNodes()
	{
		++num_of_nodes;
		num_of_nodes_str.setText(translator.translateMessage("num_of_nodes") + num_of_nodes, null, null);
	}

	public void IncrNumOfInsertedStrings()
	{
		++num_of_strings_in_tree;
		num_of_strings_in_tree_str.setText(translator.translateMessage("num_of_inserted_strings") + num_of_strings_in_tree, null, null);
	}

	public void HighlightChar(int i)
	{
		string_array.highlightElem(i, null, null);
	}

	public void UnhighlightChar(int i)
	{
		string_array.unhighlightElem(i, null, null);
	}

	public void ShowSummary()
	{
		lang.newText(new Offset(5, 5, "src_rect", AnimalScript.DIRECTION_NW),
				translator.translateMessage("summary_heading"), "summary_heading", null, tp_heading);

		lang.newText(new Offset(0, 5, "summary_heading", AnimalScript.DIRECTION_SW),
				num_of_strings_in_tree + translator.translateMessage("summary_1"), "end_text1", null, tp_default);

		int num_of_letters = 0;
		for (String str : insertion_list)
		{
			num_of_letters += str.length();
		}

		lang.newText(new Offset(0, 5, "end_text1", AnimalScript.DIRECTION_SW),
				num_of_nodes + translator.translateMessage("summary_2"), "end_text2", null, tp_default);

		lang.newText(new Offset(0, 5, "end_text2", AnimalScript.DIRECTION_SW),
				translator.translateMessage("summary_3") + num_of_letters + translator.translateMessage("summary_4"), "end_text3", null, tp_default);

		float node_letter_ratio = (float)num_of_nodes / (float)num_of_letters;
		lang.newText(new Offset(0, 5, "end_text3", AnimalScript.DIRECTION_SW),
				translator.translateMessage("summary_5") + String.format("%.2f", node_letter_ratio) + ".", "end_text4", null, tp_default);

		lang.newRect(new Offset(-5, -5, "summary_heading", AnimalScript.DIRECTION_NW),
				new Offset(10, -75, "src_rect", AnimalScript.DIRECTION_SE), "info_rect2", null, rp);
	}
}
