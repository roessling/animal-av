package generators.misc.id3_chi_squared;

import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import translator.Translator;

import java.awt.*;
import java.util.List;

public class EntropyVisual {

  private ID3ChiNode root;
  private ID3ChiNode[] children;
  private int attribute;
  private List<String> literals;
  private double[] rootDistribution;
  private double[][] childrenDistribution;
  private List<String> orderOfClasses;
  private double rootEntropy;
  private double[] childrenEntropy;
  private double gain;
  private Language lang;
  private int xCoords, yCoords;
  private int radius = 50;
  private Text rootText;
  private Text[] childrenText;
  private Text gainText;
  private String gainString;
  private TextProperties tp;
  private int counter; //count, how many children entropies have been visualized
  private Translator translator;

  /**
   * EntropyVisual draw root node and children nodes and visualize entropy estimation of root node
   *
   * @param root root ID3ChiNode
   * @param children children ID3ChiNodes
   * @param attribute index of current attribute
   * @param literals values of current attribute
   * @param xCoords x coordinate where root ID3ChiNode is visualized
   * @param yCoords y coordinate where root ID3ChiNode is visualized
   * @param gain final gain of estimation
   */
  public EntropyVisual(ID3ChiNode root, ID3ChiNode[] children, int attribute, List<String> literals,
                       double[] rootDistribution, double[][] childrenDistribution, List<String> orderOfClasses,
                       double rootEntropy, double[] childrenEntropy, Language lang, int xCoords, int yCoords,
                       double gain, Color color, Translator translator) {

    this.translator = translator;
    this.attribute = attribute;
    this.literals = literals;
    this.rootDistribution = rootDistribution;
    this.childrenDistribution = childrenDistribution;
    this.orderOfClasses = orderOfClasses;
    this.rootEntropy = rootEntropy;
    this.childrenEntropy = childrenEntropy;
    this.gain = gain;
    this.lang = lang;
    this.xCoords = xCoords;
    this.yCoords = yCoords;
    this.childrenText = new Text[children.length];
    this.counter = 0;

    this.root = new ID3ChiNode(null, root.getData(), root.getAttributes(),
        new Coordinates(this.xCoords, this.yCoords), color, lang, radius);
    this.root.setUpChildren(children.length);
    this.root.setAttribut(root.getAttributes().get(attribute));
    this.root.show();

    tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 14));

    //if first attribute, draw entropy of root note
    if (attribute == 0) {
      PseudoCodeID3.highlightCode(2);
      drawEntropy(this.root, this.xCoords - radius, this.yCoords - radius - 20,
          this.root.getNumberOfClasses(), this.root.getOrderOfClasses(), rootDistribution,
          rootEntropy, lang, -1);
    } else {
      rootText = lang
          .newText(new Coordinates(this.xCoords - radius, this.yCoords - radius - 20), "", "null",
              null, tp);
      rootText.setText("Entropy = " + DoubleToString.doubleToString(this.rootEntropy), null, null);
    }

    this.children = new ID3ChiNode[children.length];

    this.xCoords -= 250;
    this.yCoords += 3 * radius;

    int x = this.xCoords, y = this.yCoords;
    //draw Children
    for (int i = 0; i < children.length; i++) {
      this.children[i] = new ID3ChiNode(root, children[i].getData(), children[i].getAttributes(),
          new Coordinates(x, y), color, lang, radius);
      this.children[i].show();
      this.root.addChild(this.children[i], i);
      if (children.length > 1) {
        x += 500 / (children.length - 1);
      }
    }
    this.root.drawLineToChildren(attribute);
    PseudoCodeID3.highlightCode(4);
    lang.nextStep();

    gainString = "Gain = " + DoubleToString.doubleToString(rootEntropy);

    gainText = lang.newText(new Coordinates(this.xCoords - 50,
            this.yCoords + 2 * radius + (1 + childrenDistribution[0].length) * 28), gainString, "null",
        null, tp);
  }


  /**
   * drawNextEntropy visualize estimation of the entropy+gain for the next child by calling
   * drawEntropy() for the next child
   */
  public void drawNextEntropy() {
    PseudoCodeID3.highlightCode(5);
    this.children[counter].highlight();
    drawEntropy(this.children[counter], xCoords - radius, yCoords + radius + 10,
        this.children[counter].getNumberOfClasses(), this.children[counter].getOrderOfClasses(),
        childrenDistribution[counter], childrenEntropy[counter], lang, counter);
    gainString += " - " + DoubleToString.doubleToString(this.childrenEntropy[counter]) + " * ("
        + this.children[counter].getData().size() + "/" + this.root.getData().size() + ")";
    gainText.setText(gainString, null, null);
    counter++;
    if (children.length > 1) {
      xCoords += 500 / (children.length - 1);
    }
    if (counter == children.length) {
      lang.nextStep();
      gainText.setText("Gain = " + DoubleToString.doubleToString(this.gain), null, null);
      PseudoCodeID3.highlightCode(6);
      lang.nextStep();
    }
    this.children[counter - 1].unhighlight();
  }

  /**
   * drawEntropy visualize the estimation of the entropy for a given ID3ChiNode
   *
   * @param root ID3ChiNode which should be visualized
   * @param xCoord x coordinate for Text instance
   * @param yCoord y coordinate for Text instance
   * @param distribution distribution of each class
   * @param entropy result of estimation
   * @param child index of the current child, if it is the root ID3ChiNode index is set to -1
   */
  private void drawEntropy(ID3ChiNode root, int xCoord, int yCoord, int numberOfClasses,
                           List<String> orderOfClasses, double[] distribution, double entropy, Language lang,
                           int child) {
    Text textEntropy = lang.newText(new Coordinates(xCoord, yCoord), "", "null", null, tp);
    lang.nextStep();
    textEntropy.setText(translator.translateMessage("class_distribution") + " = ", null, null);

    MatrixProperties mp = new MatrixProperties();
    mp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
    String[][] distributionString = new String[numberOfClasses + 1][2];
    distributionString[0][0] = translator.translateMessage("class");
    distributionString[0][1] = translator.translateMessage("distribution");
    for (int i = 0; i < numberOfClasses; i++) {
      distributionString[i + 1][0] = orderOfClasses.get(i);
      distributionString[i + 1][1] = "" + DoubleToString.doubleToString(distribution[i]);
    }
    StringMatrix classDistribution = lang
        .newStringMatrix(new Coordinates(xCoord + 150, yCoord), distributionString, "null", null,
            mp);
    lang.nextStep();

    String entropyCalc = "Entropy = ";
    Text entropyEstimation = lang
        .newText(new Coordinates(xCoord + 2 * radius, yCoord + 30 * (numberOfClasses + 1)),
            "Entropy =", "Root", null, tp);
    for (int i = 0; i < numberOfClasses; i++) {
      entropyCalc +=
          "-" + DoubleToString.doubleToString(distribution[i]) + " * log2(" + DoubleToString
              .doubleToString(distribution[i]) + ")";
      entropyEstimation.setText(entropyCalc, null, null);
      //lang.nextStep();
    }
    lang.nextStep();
    entropyEstimation.hide();
    classDistribution.hide();
    textEntropy.setText("Entropy = " + DoubleToString.doubleToString(entropy), null, null);

    lang.nextStep();

    if (child == -1) {
      rootText = textEntropy;
    } else {
      childrenText[child] = textEntropy;
    }

  }

  /**
   * hide hide everything
   */
  public void hide() {
    gainText.hide();
    rootText.hide();
    root.hide();
    for (int i = 0; i < childrenText.length; i++) {
      childrenText[i].hide();
      children[i].hide();
    }
  }
}
