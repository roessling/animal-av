package generators.maths.trapezoidhelpers;

public class IntegralResult {
    private double value;
    private double[] nodes;

    public IntegralResult(double value, double[] nodes) {
        this.value = value;
        this.nodes = nodes;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double[] getNodes() {
        return nodes;
    }

    public void setNodes(double[] nodes) {
        this.nodes = nodes;
    }
}
