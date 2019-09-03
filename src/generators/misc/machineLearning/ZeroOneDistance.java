package generators.misc.machineLearning;


public class ZeroOneDistance extends AbstractDistanceFunction<String> {

  public double calc(String a, String b) {
    if (a.equals(b)) {
      return 0.0;
    } else
      return 1.0;
  }

  public String getFormulaWithValues(String a, String b) {
    return "Check if " + a + " == " + b;
  }

  public String getName() {
    return "0/1 Distance";
  }

  public String getDescription() {
    return "zeroOneDistanceDescription";
  }

}
