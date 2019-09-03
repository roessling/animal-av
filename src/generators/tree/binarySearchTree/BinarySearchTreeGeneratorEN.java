package generators.tree.binarySearchTree;

import generators.framework.Generator;
import generators.framework.ValidatingGenerator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import javax.swing.JOptionPane;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.tree.binarySearchTree.api.BinaryTreeAPI;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.CircleProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;

public class BinarySearchTreeGeneratorEN implements Generator, ValidatingGenerator {
    private Language lang;
    private SourceCodeProperties sourcecode;
    private CircleProperties circProps;
    private RectProperties headerboxProps;
    private PolylineProperties lineProps;
    private TextProperties textProperties;
    private TextProperties nodeTextProperties;
    private TextProperties headerTextProperties;
	private TextProperties infoBoxTextProps;
	private SourceCodeProperties slideTextProps;
	private TextProperties slideHeaderProps;
    private Color nodeHighlight1, nodeHighlight2, nodeHighlight3, textHighlight;
    private int[] keys;

    public void init(){
        lang = new AnimalScript(this.getName(), "Sebastian Leipe", 800, 600);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        sourcecode = (SourceCodeProperties)props.getPropertiesByName("Sourcecode");
        keys = (int[])primitives.get("Keys");
        circProps = (CircleProperties)props.getPropertiesByName("Node");
        headerboxProps = (RectProperties)props.getPropertiesByName("Header background");
        lineProps = (PolylineProperties)props.getPropertiesByName("Node connections");
        textProperties = (TextProperties)props.getPropertiesByName("Texts");
        headerTextProperties = (TextProperties)props.getPropertiesByName("Header text");
        nodeTextProperties = (TextProperties)props.getPropertiesByName("Node label");
        slideTextProps = (SourceCodeProperties)props.getPropertiesByName("Slide text");
        slideHeaderProps = (TextProperties)props.getPropertiesByName("Slide headers");
        infoBoxTextProps = (TextProperties)props.getPropertiesByName("Infobox text");
        nodeHighlight1 = (Color)primitives.get("Node highlightcolor current comparison");
        nodeHighlight2 = (Color)primitives.get("Node highlightcolor basenode");
        nodeHighlight3 = (Color)primitives.get("Node highlightcolor second comparison");
        textHighlight = (Color)primitives.get("Text highlightcolor");
        
		BinaryTreeAPI api = new BinaryTreeAPI(lang, sourcecode,
				headerTextProperties, textProperties, textHighlight,
				nodeTextProperties, circProps, nodeHighlight1, nodeHighlight2,
				nodeHighlight3, headerboxProps, lineProps, slideTextProps,
				slideHeaderProps, infoBoxTextProps, this.getContentLocale());
        api.runNegDel(keys);
        lang.finalizeGeneration();
        return lang.toString();
    }

    public String getName() {
        return "Binary Search Tree: insert and delete[EN]";
    }

    public String getAlgorithmName() {
        return "Binärer Suchbaum";
    }

    public String getAnimationAuthor() {
        return "Sebastian Leipe";
    }

    public String getDescription(){
        return "A binary search tree is a tree, that consists of nodes with 0-2 childnodes each. Every node contains a key and references to its children."
        +"\n"
        +"All keys in the left subtree of a node N are less or equal than the key of N. Respectively, all keys in the right subtree of n are greater than the key of N."
        +"The keys can be of any type that can be sorted."
        +"\n\n"
        +"Note: to delete keys from the tree in the animation, enter negative key values.";
    }

    public String getCodeExample(){
        return "public class Node{"
 +"\n"
 +"      private Node left, right;"
 +"\n"
 +"      private int key;"
 +"\n"
 +"      "
 +"\n"
 +"      public Node(int key){"
 +"\n"
 +"            this.key = key;"
 +"\n"
 +"      }"
 +"\n"
 +"      "
 +"\n"
 +"      //Get-/Setmethods are skipped here"
 +"\n"
 +"}"
 +"\n"
 +"\n"
 +"public class BinarySearchTree{"
 +"\n"
 +"      private Node root;"
 +"\n"
 +"      "
 +"\n"
 +"      public void insert(int key){"
 +"\n"
 +"            if(root != null)"
 +"\n"
 +"                  this.insert(key, root);"
 +"\n"
 +"            else //Root-Knoten nicht vorhanden, hier einfügen"
 +"\n"
 +"                  root = new Node(key);"
 +"\n"
 +"      }"
 +"\n"
 +"      public void insert(int key, Node currentNode){"
 +"\n"
 +"            if(key <= currentNode.getKey()){"
 +"\n"
 +"                  if(currentNode.getLeft() != null)"
 +"\n"
 +"                        insert(key, currentNode.getLeft();"
 +"\n"
 +"                  else"
 +"\n"
 +"                        currentNode.setLeft(new Node(key));"
 +"\n"
 +"            } else {"
 +"\n"
 +"                  if(currentNode.getRight() != null)"
 +"\n"
 +"                        insert(key, currentNode.getRight();"
 +"\n"
 +"                  else"
 +"\n"
 +"                        currentNode.setRight(new Node(key));"
 +"\n"
 +"            }"
 +"\n"
 +"      }"
 +"\n"
 +"      public boolean delete(int key){"
 +"\n"
 +"            //See animation"
 +"\n"
 +"     }";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.US;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

	@Override
	public boolean validateInput(AnimationPropertiesContainer props,
			Hashtable<String, Object> primitives) throws IllegalArgumentException {
		int[] keys = (int[])primitives.get("Keys");
		if(keys.length >= 5)
			return true;
		JOptionPane.showMessageDialog(null, "You have to enter at least five values!", "Invalid count of keys", JOptionPane.ERROR_MESSAGE);
		return false;
	}
}