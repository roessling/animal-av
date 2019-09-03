package generators.tree.btree.delete;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.tree.helpers.BTreeDelete;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SquareProperties;

public class BTreeDeleteGenerator implements ValidatingGenerator {
  private Language             lang;
  private SourceCodeProperties sourceCode;
  private ArrayProperties      arrayElements;
  private int                  M;
  private SquareProperties     Node;
  private int[]                elements;
  private int[]                deletions;
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
    deletions = (int[]) primitives.get("deletions");
    headerProps = (SourceCodeProperties) props
        .getPropertiesByName("headerProps");
    headerBack = (SquareProperties) props.getPropertiesByName("headerBack");
    BTreeDelete.generate(lang, sourceCode, arrayElements, M, Node, elements,
        deletions, headerProps, headerBack);

    return lang.toString();
  }

  public String getName() {
    return "B-Baum - Löschen";
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
    return "private void mergeTwoSiblings(TreeNode node, int k) {"
        + "\n"
        + "	node.getChild(k-1).setValue(node.getValue(k-1), M-1);"
        + "\n"
        + "	node.getChild(k-1).setChild(node.getChild(k).getChild(0), M-1);"
        + "\n"
        + "	for(int i=1; i < M; i++) {"
        + "\n"
        + "		node.getChild(k-1).setValue(node.getChild(k).getValue(i-1), M+i-1);"
        + "\n"
        + "		node.getChild(k-1).setChild(node.getChild(k).getChild(i), M+i);"
        + "\n" + "	}" + "\n" + "	int end = node.getCount();" + "\n"
        + "	for(int i=k+1; i <= end; i++) {" + "\n"
        + "		node.setValue(node.getValue(i-1), i-2);" + "\n"
        + "		node.setChild(node.getChild(i), i-1);" + "\n" + "	}" + "\n"
        + "	node.setChild(null, node.getCount());" + "\n"
        + "	node.setValue(-1, node.getCount()-1);" + "\n" + "}";
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
    return Generator.PSEUDO_CODE_OUTPUT;
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

    int[] del = (int[]) primitives.get("deletions");

    for (int i = 0; i < del.length; i++) {
      if (del[i] < 0) {
        valid = false;
        throw new IllegalArgumentException(
            "Es sind nur positive Zahlen erlaubt.");
      }
    }

    if ((Integer) primitives.get("M") < 2) {
      valid = false;
      throw new IllegalArgumentException("<b>M</b> muss mindestens 2 sein.");
    }

    return valid;
  }

}