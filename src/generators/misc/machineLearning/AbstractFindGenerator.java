package generators.misc.machineLearning;
import java.util.HashSet;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.animalscript.addons.CodeView;
import algoanim.animalscript.addons.Slide;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

public abstract class AbstractFindGenerator extends AbstractMachineLearning {

  public AbstractFindGenerator(String resourceName, Locale locale) {
    super(resourceName, locale);
  }

  /**
   * get all attributes of a column
   * 
   * @param col
   *          string column
   * @return all attributes
   */
  public String[] getAttributeValues(String[] col) {
    HashSet<String> attributeValues = new HashSet<String>();

    for (int i = 1; i < col.length; i++) {
      attributeValues.add(col[i]);
    }
    String[] att = attributeValues.toArray(new String[attributeValues.size()]);
    return att;
  }

  public String[] getAttributeValues(String[][] dataset, int col) {

    String[] column = getCol(dataset, col);
    return getAttributeValues(column);

  }

  /**
   * get column of a string matrix
   * 
   * @param dataset
   *          all examples
   * @param c
   *          index of column
   * @return column on index c
   */
  public String[] getCol(String[][] dataset, int c) {
    String[] col = new String[dataset.length];

    for (int i = 0; i < col.length; i++) {
      col[i] = dataset[i][c];
    }
    return col;
  }
  
  /**
   * check if example is covered by the hypothesis
   * @param example example
   * @param hypo hypothesis
   * @return true if example is covered by hypothesis
   */
  public abstract boolean isExampleCovered(String[] example, String[] hypo);
  
  /**
   * create a hypothesis for an example
   * @param s example
   * @param hypoNum hypothesis number
   * @return
   */
  public abstract String createNewHypothesis(String[] example, int hypoNum);

}
