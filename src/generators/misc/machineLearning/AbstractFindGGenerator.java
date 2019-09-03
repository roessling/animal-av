package generators.misc.machineLearning;
import java.util.Locale;

public abstract class AbstractFindGGenerator extends AbstractFindGenerator{

  public AbstractFindGGenerator(String resourceName, Locale locale) {
    super(resourceName, locale);
  }
  
 /**
  * specifies the current hypothesis
  * @param example example that must not be covered
  * @param hypo current hypothesis
  * @param hypoNum hypothesis number
  * @param row row ID in the dataset
  * @return specified hypothesis
  */
  public abstract String[] specify(String[] example, String[] hypo, int hypoNum, int row);
  
  

}
