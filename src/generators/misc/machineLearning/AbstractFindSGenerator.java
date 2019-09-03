package generators.misc.machineLearning;
import java.util.Locale;

public abstract class AbstractFindSGenerator extends AbstractFindGenerator{

  public AbstractFindSGenerator(String resourceName, Locale locale) {
    super(resourceName, locale);
  }
  
  public boolean arePreviousNegativeCovered(String[][] trainingdata,
      String[] hypo, int rows) {

    for (int i = 1; i < rows; i++) {
      if (getClassOfExample(trainingdata[i]).equalsIgnoreCase("no")
          && isExampleCovered(trainingdata[i], hypo)) {
        return true;
      }
    }
    return false;
  }
  
  public abstract String[] generalize(String[] example, String[] hypo, int... i);
  
  
}
