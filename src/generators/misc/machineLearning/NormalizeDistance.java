package generators.misc.machineLearning;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import algoanim.primitives.StringMatrix;

public class NormalizeDistance extends AbstractDistanceFunction<Double> {

  StringMatrix data;
  int          currentIndex;

  public double calc(Double a, Double b) {
    double difference = getMaxDistance(data, currentIndex);
    if(difference == 0) {
      difference = Math.abs(a - b);
    }
    double result = round(((double) Math.abs(a - b) / difference), 2);
    return result;
  }

  public NormalizeDistance(StringMatrix m) {
    data = m;
  }

  @Override
  public String getDescription() {
    return "normalizeDistanceDescription";
  }

  public String getFormulaWithValues(double a, double b) {
    StringBuilder sb = new StringBuilder();
    sb.append("( ").append(a).append(" - ").append(b).append(" / ");
    sb.append("abs(max() - min())");
    return sb.toString();
  }

  public double getMaxDistance(StringMatrix examples, int index) {

    double smallest = Integer.MAX_VALUE;
    double biggest = Integer.MIN_VALUE;

    for (int i = 1; i < examples.getNrRows(); i++) {

      if (!examples.getElement(i, index).equals("X")) {

        if (Double.parseDouble(examples.getElement(i, index)) > biggest) {
          biggest = Double.parseDouble(examples.getElement(i, index));
        }

        if (Double.parseDouble(examples.getElement(i, index)) < smallest) {
          smallest = Double.parseDouble(examples.getElement(i, index));
        }
      }
    }

    return Math.abs(biggest - smallest);

  }

  @Override
  public String getFormulaWithValues(Double a, Double b) {
    // TODO Auto-generated method stub
    return null;
  }

  public void setData(StringMatrix d) {
    data = d;
  }

  public void setCurrentIndex(int i) {
    currentIndex = i;
  }

  @Override
  public String getName() {
    return "Normalize Distance";
  }


}
