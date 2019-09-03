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

public class BinarySearchTreeGenerator implements Generator, ValidatingGenerator {
    private Language lang;
    private SourceCodeProperties sourcecode;
    private CircleProperties circProps;
    private RectProperties headerboxProps;
    private PolylineProperties lineProps;
    private TextProperties textProperties;
    private TextProperties nodeTextProperties;
    private TextProperties headerTextProperties;
    private SourceCodeProperties slideTextProps;
    private TextProperties slideHeaderProps, infoBoxTextProps;
    private Color nodeHighlight1, nodeHighlight2, nodeHighlight3, textHighlight;
    private int[] keys;

    public void init(){
        lang = new AnimalScript("Binärer Suchbaum: Einfügen und Löschen[DE]", "Sebastian Leipe", 800, 600);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        sourcecode = (SourceCodeProperties)props.getPropertiesByName("Sourcecode");
        keys = (int[])primitives.get("Keys");
        circProps = (CircleProperties)props.getPropertiesByName("Knoten");
        headerboxProps = (RectProperties)props.getPropertiesByName("Header-Hintergrund");
        lineProps = (PolylineProperties)props.getPropertiesByName("Knoten-Verbindungen");
        textProperties = (TextProperties)props.getPropertiesByName("Texte");
        headerTextProperties = (TextProperties)props.getPropertiesByName("Header-Text");
        nodeTextProperties = (TextProperties)props.getPropertiesByName("Knoten-Beschriftungen");
        slideTextProps = (SourceCodeProperties)props.getPropertiesByName("Slide Texte");
        slideHeaderProps = (TextProperties)props.getPropertiesByName("Slide Headers");
        infoBoxTextProps = (TextProperties)props.getPropertiesByName("Infobox Texte");
        nodeHighlight1 = (Color)primitives.get("Knoten Highlightfarbe aktueller Vergleich");
        nodeHighlight2 = (Color)primitives.get("Knoten Highlightfarbe Basisknoten");
        nodeHighlight3 = (Color)primitives.get("Knoten Highlightfarbe zweiter Vergleich");
        textHighlight = (Color)primitives.get("Text Highlightfarbe");
		BinaryTreeAPI api = new BinaryTreeAPI(lang, sourcecode,
				headerTextProperties, textProperties, textHighlight,
				nodeTextProperties, circProps, nodeHighlight1, nodeHighlight2,
				nodeHighlight3, headerboxProps, lineProps, slideTextProps,
				slideHeaderProps, infoBoxTextProps, this.getContentLocale());
        api.runNegDel(keys);
        lang.finalizeGeneration();
        //System.out.println(lang);
        return lang.toString();
    }

    public String getName() {
        return "Binärer Suchbaum: Einfügen und Löschen[DE]";
    }

    public String getAlgorithmName() {
        return "Binärer Suchbaum";
    }

    public String getAnimationAuthor() {
        return "Sebastian Leipe";
    }

    public String getDescription(){
        return "Ein bin&auml;rer Suchbaum ist ein Baum, der aus Knoten mit jeweils 0-2 Kindknoten besteht. Jeder Knoten enth&auml;lt einen Schl&uuml;ssel, sowie Verweise auf maximal zwei Kindknoten."
 +"\n"
 +"Die Besonderheit des bin&auml;ren Suchbaums ist es, dass die Wertigkeit der Schl&uuml;ssel aller Knoten im linken Teilbaum eines Knotens K kleiner ist, als der Schl&uuml;ssel von K."
 +"\n"
 +"Im rechten Teilbaum sind die Schl&uuml;ssel entsprechend immer gr&ouml;&szlig;er als der Schl&uuml;ssel von K. Der Schl&uuml;ssel kann hierbei aus einem beliebigen Datentyp bestehen, wobei es eine M&ouml;glichkeit geben muss,"
 +"\n"
 +"die Schl&uuml;ssel nach Wertigkeit zu sortieren."
 +"\n\n"
 +"Hinweis: Um in der Animation Knoten aus dem Baum zu l&ouml;schen, m&uuml;ssen im Wizard negative Schl&uuml;sselwerte eingegeben werden.";
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
 +"      //Getter/Setter werden hier ausgelassen"
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
 +"            //Siehe Animation"
 +"\n"
 +"     }";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMANY;
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
		if(keys.length >= 1)
			return true;
		JOptionPane.showMessageDialog(null, "Es muss mindestens ein Wert eingegeben werden!", "Ungültige Schlüsselanzahl", JOptionPane.ERROR_MESSAGE);
		return false;
	}
}