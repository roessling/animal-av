package generators.tree.btree.insert;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.tree.helpers.BTreeInsert;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SquareProperties;

public class BTreeInsertGenerator implements ValidatingGenerator {
  private Language             lang;
  private SourceCodeProperties sourceCode;
  private ArrayProperties      arrayElements;
  private int                  M;
  private SquareProperties     Node;
  private int[]                elements;
  private SourceCodeProperties headerProps;
  private SquareProperties     headerBack;

  public void init() {
    lang = new AnimalScript("B-Baum", "Kristoffer Braun, Philipp Rack", 1500,
        1000);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    sourceCode = (SourceCodeProperties) props.getPropertiesByName("sourceCode");
    arrayElements = (ArrayProperties) props
        .getPropertiesByName("arrayElements");
    M = (Integer) primitives.get("M");
    Node = (SquareProperties) props.getPropertiesByName("Node");
    elements = (int[]) primitives.get("elements");
    headerProps = (SourceCodeProperties) props
        .getPropertiesByName("headerProps");
    headerBack = (SquareProperties) props.getPropertiesByName("headerBack");
    BTreeInsert.generate(lang, sourceCode, arrayElements, M, Node, headerProps,
        headerBack, elements);

    return lang.toString();
  }

  public String getName() {
    return "B-Baum - Einfügen";
  }

  public String getAlgorithmName() {
    return "B-Baum";
  }

  public String getAnimationAuthor() {
    return "Kristoffer Braun, Philipp Rack";
  }

  public String getDescription() {
    return "Ein B-Baum (englisch B-tree) ist in der Informatik eine Daten- oder Indexstruktur, "
        + "\n"
        + "die h&auml;ufig in Datenbanken und Dateisystemen eingesetzt wird. Ein B-Baum ist ein "
        + "\n"
        + "immer vollst&auml;ndig balancierter Baum, der Daten nach Schl&uuml;sseln sortiert speichert. "
        + "\n"
        + "Er kann bin&auml;r sein, ist aber im Allgemeinen kein Bin&auml;rbaum. Das Einf&uuml;gen, Suchen "
        + "\n"
        + "und L&ouml;schen von Daten in B-B&auml;umen ist in amortisiert logarithmischer Zeit m&ouml;glich. "
        + "\n"
        + "B-B&auml;ume wachsen und schrumpfen anders als viele Suchb&auml;ume von den Bl&auml;ttern "
        + "\n"
        + "hin zur Wurzel."
        + "Der Parameter M des B-Baums gibt hierbei die Ordnung an. Ein Knoten enthält maximal 2*M-1 Elemente"
        + "und besitzt maximal 2*M Kinder."
        + " <i> Quelle: https://de.wikipedia.org/wiki/B-Baum (03.09.2013)</i>";
  }

  public String getCodeExample() {
    return "private void bTreeInsert(int value, TreeNode current )  throws LineNotExistsException {"
        + "\n"
        + "      if(current == null) {"
        + "\n"
        + "            rootNode = new TreeNode();"
        + "\n"
        + "            insertIntoLeaf(value, rootNode);"
        + "\n"
        + "       }"
        + "\n"
        + "      else if(current.getCount() == 2*M-1) {"
        + "\n"
        + "            TreeNode newRoot = new TreeNode();"
        + "\n"
        + "            newRoot.setChild(rootNode, 0);"
        + "\n"
        + "            rootNode = newRoot;"
        + "\n"
        + "            splitIntoSiblings(rootNode, 0);"
        + "\n"
        + "            bTreeInsert(value, rootNode);	"
        + "\n"
        + "      }"
        + "\n"
        + "      else if(current.getChild(0) == null) {"
        + "\n"
        + "            insertIntoLeaf(value, current);"
        + "\n"
        + "      }"
        + "\n"
        + "      else {"
        + "\n"
        + "            pointerCounter = pointerCounter + 4;"
        + "\n"
        + "            int index = 0;"
        + "\n"
        + "            while(value > current.getValue(index) && current.getValue(index) != -1)"
        + "\n"
        + "                  index++;"
        + "\n"
        + "            if(current.getChild(index).getCount() == 2*M-1) {"
        + "\n"
        + "                  splitIntoSiblings(current, index);"
        + "\n"
        + "                  bTreeInsert(value, current.getChild(index+1));"
        + "\n"
        + "            }"
        + "\n"
        + "            else {"
        + "\n"
        + "                  current.getChild(index).highlightCell(0, 2*M-2);"
        + "\n"
        + "                  bTreeInsert(value, current.getChild(index));"
        + "\n" + "            }" + "\n" + "      }" + "\n" + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
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
  public boolean validateInput(AnimationPropertiesContainer arg0,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {

    int[] el = (int[]) primitives.get("elements");

    boolean valid = true;

    for (int i = 0; i < el.length; i++) {
      if (el[i] < 0) {
        valid = false;
        throw new IllegalArgumentException(
            "Es sind nur positive Zahlen erlaubt.");
      }
      for (int k = i + 1; k < el.length; k++) {
        if (el[i] == el[k]) {
          valid = false;
          throw new IllegalArgumentException(
              "Es d&uuml;rfen keine Elemente doppelt vorkommen.");
        }
      }
    }

    if ((Integer) primitives.get("M") < 2) {
      valid = false;
      throw new IllegalArgumentException("<b>M</b> muss mindestens 2 sein.");
    }

    return valid;
  }

}