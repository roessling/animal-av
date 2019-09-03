package generators.misc.id3_chi_squared;

import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import animal.main.Animal;
import translator.Translator;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ID3Algorithm {

  private double threshold;
  private List<Date> data;
  private List<String> attributes;
  private Language lang;

  //both variables are estimated by calling generate()/expand()
  private int maxDepth;
  private int[] maxWidth;

  private Color color;
  private Translator translator;

  public ID3Algorithm(double threshold, List<Date> data, List<String> attributes, Color color,
                      Language lang, Translator translator) {
    this.translator = translator;
    this.threshold = threshold;
    this.data = data;
    this.lang = lang;
    this.color = color;
    this.attributes = attributes;
    this.maxDepth = 0;
    this.maxWidth = new int[attributes.size()];

    this.lang.setStepMode(true);
  }

  /**
   * generate is called to build the tree (by calling expand) and give information of the depth and
   * width. These informations are used in generateDraw()
   *
   * @return root ID3ChiNode of the generated tree
   **/
  public ID3ChiNode generate() {
    ID3ChiNode root = new ID3ChiNode(null, data, attributes);
    expand(root, 0);
    return root;
  }

  /**
   * expand is called by generate() and creates recursively the final tree
   *
   * @param id3chinode root ID3ChiNode
   * @param depth depth of the current ID3ChiNode, in the beginning it is 0
   */
  private void expand(ID3ChiNode id3chinode, int depth) {

    if (depth > maxDepth) {
      maxDepth = depth;
    }

    int numberOfClasses = id3chinode.getNumberOfClasses();
    int[] numberOfValues = id3chinode.getNumberOfValues();

    List<String>[] orderOfValues = id3chinode.getOrderOfValues();
    List<String> orderOfClasses = id3chinode.getOrderOfClasses();

    double maxGain = -100000;
    int choice = -1;

    double[] rootDistribution = id3chinode.getDistributionOfEachClass();
    double rootEntropy = 0;
    for (int i = 0; i < numberOfClasses; i++) {
      rootEntropy -=
          rootDistribution[i] * /*log base 2*/(Math.log(rootDistribution[i]) / Math.log(2));
    }

    ID3ChiNode[] children = null;// = new ID3ChiNode[numberOfValues.length];
    for (int i = 0; i < numberOfValues.length; i++) {

      ID3ChiNode[] childrenCandidates = new ID3ChiNode[numberOfValues[i]];
      double[][] childrenDistribution = new double[numberOfValues[i]][];
      double[] childrenEntropy = new double[numberOfValues[i]];
      double gain = rootEntropy;

      for (int j = 0; j < numberOfValues[i]; j++) {

        List<Date> newData = cloneDataSet(id3chinode.getData(), i, orderOfValues[i].get(j));
        List<String> newAttributes = cloneAttributes(id3chinode.getAttributes(), i);
        childrenCandidates[j] = new ID3ChiNode(id3chinode, newData, newAttributes);
        childrenDistribution[j] = childrenCandidates[j].getDistributionOfEachClass();
        for (int k = 0; k < childrenDistribution[j].length; k++) {
          childrenEntropy[j] -=
              childrenDistribution[j][k] * /*log base 2*/(Math.log(childrenDistribution[j][k])
                  / Math.log(2));
        }
        gain -= childrenEntropy[j] * childrenCandidates[j].getData().size() / id3chinode.getData().size();
      }

      if (gain > maxGain) {
        maxGain = gain;
        children = childrenCandidates;
        choice = i;
      }
    }

    //Chi_squared_test
    boolean chi_test = true;
    if (choice != -1) {
      Chi chi = new Chi(translator);
      chi_test = chi.squared_test(id3chinode.getData(), numberOfValues[choice], numberOfClasses,
          orderOfValues[choice], orderOfClasses, choice, threshold);
    }
    //end: chi_squared_test

    if (maxGain > 0 && chi_test) {
      maxWidth[depth] += numberOfValues[choice];
      id3chinode.setUpChildren(id3chinode.getNumberOfValues()[choice]);

      for (int i = 0; i < children.length; i++) {
        id3chinode.addChild(children[i], i);
      }

      for (int i = 0; i < numberOfValues[choice]; i++) {
        expand(children[i], depth + 1);
      }
    }
  }

  /**
   * generateDraw uses the informations of generate() and builds + visualizes the tree
   *
   * @return root ID3ChiNode of the generated tree
   */
  public ID3ChiNode generateDraw() {

    int startX = 1950;
    int startY = 100;

    createTitleBar();

    createDescriptionEn();

    PseudoCodeID3.createSourceCodeEn(10, 80, lang,translator);
    ID3ChiNode root = new ID3ChiNode(null, data, attributes, new Coordinates(startX, startY), color, lang, 50);
    root.show();
    lang.nextStep();
    int[] levelCounter = new int[maxDepth+1];
    expandDraw(root, 0, startX, startY, levelCounter, 0);
    PseudoCodeID3.highlightCode(14);

    TextProperties tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 20));
    Text resultText = lang.newText(new Coordinates(600,50),translator.translateMessage("summary"),"null",null,tp);
    lang.nextStep(translator.translateMessage("result"));

    return root;
  }

  /**
   * expandDraw is called by generateDraw() and creates recursively the final tree
   *
   * @param id3chinode root ID3ChiNode
   * @param depth depth of the current ID3ChiNode, in the beginning it is 0
   * @param startX x coordinate of root ID3ChiNode
   * @param startY y coordinate of root ID3ChiNode
   */
  private void expandDraw(ID3ChiNode id3chinode, int depth, int startX, int startY, int[] levelCounter,
      int numberOfID3ChiNodeInLevel) {

    int numberOfClasses = id3chinode.getNumberOfClasses();
    int[] numberOfValues = id3chinode.getNumberOfValues();

    List<String>[] orderOfValues = id3chinode.getOrderOfValues();
    List<String> orderOfClasses = id3chinode.getOrderOfClasses();

    id3chinode.highlight();

    int xCoord = 10;
    int yCoord = 440;

    Coordinates coords = new Coordinates(xCoord, yCoord);
    StringMatrix sm = null;
    Text noData = null;
    boolean hideText = false;

    //draw data set
    if (id3chinode.getData().size() > 0) {
      String[][] dataString = new String[id3chinode.getData().size() + 1][id3chinode.getAttributes().size()
          + 1];
      for (int j = 0; j < id3chinode.getData().size(); j++) {
        for (int i = 0; i < id3chinode.getAttributes().size(); i++) {
          dataString[j + 1][i] = id3chinode.getData().get(j).getLiterals().get(i);
          dataString[0][i] = id3chinode.getAttributes().get(i);
        }
        dataString[j + 1][id3chinode.getAttributes().size()] = id3chinode.getData().get(j).getLabel();
      }
      dataString[0][id3chinode.getAttributes().size()] = translator.translateMessage("class");

      MatrixProperties mp = new MatrixProperties();
      mp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");

      sm = lang.newStringMatrix(coords, dataString, "null", null, mp);
      yCoord += (dataString.length + 1) * 25;
    } else {
      noData = lang.newText(coords, "no Data stored", "noData", null);
      hideText = true;
      yCoord += 30;
    }
    //end: draw data set

    PseudoCodeID3.highlightCode(0);
    String labelText = "";
    if (depth == 0) {
      labelText = "Node (0" + "," + depth + ")";
    } else {
      labelText = "Node (" + numberOfID3ChiNodeInLevel + "," + depth + ")";
    }
    lang.nextStep(labelText);

    //check if there is only one class in dataset
    //if so, the ID3ChiNode can be classified
    boolean onlyOneClass = true;
    for (int i = 0; i < id3chinode.getData().size() - 1; i++) {
      if (!id3chinode.getData().get(i).getLabel().equals(id3chinode.getData().get(i + 1).getLabel())) {
        onlyOneClass = false;
        break;
      }
    }
    if (id3chinode.getData().size() == 0 || id3chinode.getAttributes().size() == 0 || onlyOneClass) {
      classifyID3ChiNode(id3chinode, numberOfClasses, orderOfClasses, noData, sm, hideText, 1);
      return;
    }
    //end: check

    double maxGain = -Double.MIN_VALUE;
    int choice = -1;

    //change coordinates for entropy and gain
    yCoord = 10;
    xCoord = 600;

    //estimate Gain
    double[] rootDistribution = id3chinode.getDistributionOfEachClass();
    double rootEntropy = 0;
    for (int i = 0; i < numberOfClasses; i++) {
      rootEntropy -=
          rootDistribution[i] * /*log base 2*/(Math.log(rootDistribution[i]) / Math.log(2));
    }
    ID3ChiNode[] children = null;//new ID3ChiNode[numberOfValues.length];

    TextProperties tp = new TextProperties();
    tp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 14));
    Text maxGainText = lang
        .newText(new Coordinates(xCoord, yCoord), "Max Gain =      " + translator.translateMessage("best_attribute"), "null", null,
            tp);

    yCoord += 30;

    SourceCodeProperties scProperties = new SourceCodeProperties();
    scProperties
        .set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 14));
    //scProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, new Color(114,140,56));

    SourceCode formula = lang
        .newSourceCode(new Coordinates(xCoord, yCoord), "null", null, scProperties);
    formula.addCodeLine(
        translator.translateMessage("intro5"),
        "null", 0, null);
    formula.addCodeLine(
        translator.translateMessage("intro9"),
        "null", 0, null);

    int tmpX = xCoord, tmpY = yCoord; //used for chi-squared visu
    yCoord += 150;
    xCoord += 250;

    int startLevelCounter = levelCounter[depth];
    for (int i = 0; i < numberOfValues.length; i++) {

      ID3ChiNode[] childrenCandidates = new ID3ChiNode[numberOfValues[i]];
      double[][] childrenDistribution = new double[numberOfValues[i]][];
      double[] childrenEntropy = new double[numberOfValues[i]];
      double gain = rootEntropy;

      int currentLevelCounter = startLevelCounter;

      for (int j = 0; j < numberOfValues[i]; j++) {
        List<Date> newData = cloneDataSet(id3chinode.getData(), i, orderOfValues[i].get(j));
        List<String> newAttributes = cloneAttributes(id3chinode.getAttributes(), i);
        int margin = 400;
        //int margin = 600/(maxWidth[depth] - 1);
        childrenCandidates[j] = new ID3ChiNode(id3chinode, newData, newAttributes, new Coordinates(
            startX - margin + currentLevelCounter * (2 * margin / (maxWidth[depth] - 1)),
            startY + (depth + 1) * 300), color, lang, 50);
        currentLevelCounter++;
        childrenDistribution[j] = childrenCandidates[j].getDistributionOfEachClass();
        for (int k = 0; k < childrenDistribution[j].length; k++) {
          childrenEntropy[j] -=
              childrenDistribution[j][k] * /*log base 2*/(Math.log(childrenDistribution[j][k])
                  / Math.log(2));
        }
        gain -= childrenEntropy[j] * childrenCandidates[j].getData().size() / id3chinode.getData().size();
      }

      visualizeEntropy(sm, id3chinode, childrenCandidates, i, orderOfValues[i], rootDistribution,
          childrenDistribution, orderOfClasses, rootEntropy, childrenEntropy, xCoord, yCoord, gain);

      if (gain > maxGain) {
        maxGain = gain;
        children = childrenCandidates;
        choice = i;
        maxGainText.setText(
            "Max Gain = " + DoubleToString.doubleToString(maxGain) + "  " + translator.translateMessage("best_attribute") + id3chinode
                .getAttributes().get(choice), null, null);
        levelCounter[depth] = currentLevelCounter;
      }
    }
    //end: estimate gain

    PseudoCodeID3.highlightCode(7);
    formula.hide();
    lang.nextStep();
    PseudoCodeID3.highlightCode(8);

    //Chi_squared_test
    xCoord = tmpX;
    yCoord = tmpY;
    highlightAttribute(sm, choice, true);

    Chi chi = new Chi(translator);
    boolean chi_test = chi.squared_test(id3chinode.getData(), numberOfValues[choice], numberOfClasses,
        orderOfValues[choice], orderOfClasses, choice, threshold);
    chi.visualizeTest(orderOfValues[choice], orderOfClasses, choice, xCoord, yCoord, lang);

    highlightAttribute(sm, choice, false);
    //end: chi_squared_test

    maxGainText.hide();

    if (maxGain > 0 && chi_test) {
      PseudoCodeID3.highlightCode(10);
      id3chinode.setAttribut(id3chinode.getAttributes().get(choice));
      id3chinode.highlight();
      id3chinode.setUpChildren(id3chinode.getNumberOfValues()[choice]);

      for (int i = 0; i < children.length; i++) {
        id3chinode.addChild(children[i], i);
        children[i].show();
      }
      id3chinode.drawLineToChildren(choice);
      lang.nextStep();
      PseudoCodeID3.highlightCode(11);
      lang.nextStep();

      for (int i = 0; i < numberOfValues[choice]; i++) {
        if (i > 0) {
          id3chinode.highlight();
          highlightValue(sm, id3chinode, choice, orderOfValues[choice].get(i), true);
          sm.show(new TicksTiming(0));
          PseudoCodeID3.highlightCode(11);
          lang.nextStep();
        }
        highlightValue(sm, id3chinode, choice, orderOfValues[choice].get(i), false);
        id3chinode.unhighlight();
        sm.hide(new TicksTiming(0));
        expandDraw(children[i], depth + 1, startX, startY, levelCounter,
            levelCounter[depth] - numberOfValues[choice] + i);
      }
    } else {
      //PseudoCodeID3.highlightCode(12);
      classifyID3ChiNode(id3chinode, numberOfClasses, orderOfClasses, noData, sm, hideText, 13);
    }
  }

  /**
   * createDescription creates the intro slide
   */
  private void createDescriptionEn() {
    SourceCodeProperties sourceCodeProperties = new SourceCodeProperties();
    sourceCodeProperties
        .set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 25));

    SourceCode description = lang
        .newSourceCode(new Coordinates(10, 80), "null", null, sourceCodeProperties);
    description.addCodeLine(
        translator.translateMessage("intro1"),
        "null", 0, null);
    description.addCodeLine(
            translator.translateMessage("intro2"),
        "null", 0, null);
    description
        .addCodeLine(translator.translateMessage("intro3"), "null", 0,
            null);
    description
        .addCodeLine(translator.translateMessage("intro4"), "null", 0, null);
    description.addCodeLine(
            translator.translateMessage("intro5"),
        "null", 1, null);
    description.addCodeLine(
            translator.translateMessage("intro6"),
        "null", 0, null);
    description.addCodeLine(
            translator.translateMessage("intro7"),
        "null", 0, null);
    description.addCodeLine(
            translator.translateMessage("intro8"),
        "null", 0, null);
    description.addCodeLine(
            translator.translateMessage("intro9"),
        "null", 1, null);
    description
        .addCodeLine(translator.translateMessage("intro10"), "null", 0,
            null);
    description.addCodeLine(
            translator.translateMessage("intro11"),
        "null", 0, null);
    description.addCodeLine(
            translator.translateMessage("intro12"),
        "null", 0, null);
    description.addCodeLine(
            translator.translateMessage("intro13"),
        "null", 0, null);
    description.addCodeLine(translator.translateMessage("intro14"), "null", 0, null);
    description.addCodeLine(
            translator.translateMessage("intro15"),
        "null", 0, null);
    description.addCodeLine(
            translator.translateMessage("intro16"),
        "null", 0, null);
    description.addCodeLine(
            translator.translateMessage("intro17"),
        "null", 0, null);
    description
        .addCodeLine(translator.translateMessage("intro18"), "null", 0, null);
    description.addCodeLine(translator.translateMessage("intro19"), "null", 1,
        null);
    description.addCodeLine(translator.translateMessage("intro20"), "null", 0, null);
    description.addCodeLine(
            translator.translateMessage("intro21"),
        "null", 1, null);
    description
        .addCodeLine(translator.translateMessage("intro22"),
            "null", 0, null);
    description.addCodeLine("", "null", 0, null);
    description.addCodeLine(
        translator.translateMessage("intro23"),
        "null", 0, null);
    description.addCodeLine(translator.translateMessage("intro24"),
        "null", 0, null);
    description.addCodeLine(
            translator.translateMessage("intro25"),
        "null", 0, null);
    description
        .addCodeLine(translator.translateMessage("intro26"), "null", 0, null);
    description.addCodeLine(
            translator.translateMessage("intro27"),
        "null", 0, null);
    description.addCodeLine(
            translator.translateMessage("intro28"),
        "null", 0, null);
    description
        .addCodeLine(translator.translateMessage("intro29"),
            "null", 0, null);
    description
            .addCodeLine(translator.translateMessage("intro30"),
                    "null", 0, null);

    lang.nextStep(translator.translateMessage("introduction"));
    description.hide();
  }
    /*
    ID3 is a simple Data Mining/Machine Learning algorithm to build a decision tree based on a training data set.
    Each example of the training data set contains some features and a lable to show to which class each example belongs. A feature is a tuple containing an attribute/value pair.
    In the beginning the algorithm is called on the root node. First of all it checks which is the best attribute to split the training data.
    Splitting the data means that you create a new data subset for every feature of the attribute and remove the attribute from the data set. Also you create one child node for every subset.
    After that the algorithm is called recursively on every child node based on its data subset.
    To estimate which attribute is the best to split the data set we need to estimate the gain of each attribute. The attribute with the highest gain is the best.
     */

  /**
   * createTitleBar creates the title bar that simply says "ID3"
   */
  private void createTitleBar() {
    TextProperties textProperties = new TextProperties();
    textProperties
        .set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 46));
    textProperties.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
    textProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    RectProperties rectProperties = new RectProperties();
    rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
    rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(203, 250, 100));

    Rect titleRect = lang
        .newRect(new Coordinates(10, 10), new Coordinates(100, 50), "null", null, rectProperties);
    lang.newText(new Offset(0, -30, titleRect, "C"), "ID3", "null", null, textProperties);
  }

  /**
   * visualizeEntropy creates an instancd of EntropyVisual and highlight attribute in data matrix
   *
   * @param sm data matrix
   * @param root current ID3ChiNode
   * @param childrenCandidates children candidates for current attribute
   * @param attribute index of the attribute
   * @param literals values of current attribute
   * @param xCoord xCoordinate where root ID3ChiNode should be visualized
   * @param yCoord yCoordinate where root ID3ChiNode should be visualized
   * @param gain final gain of attribute
   */
  private void visualizeEntropy(StringMatrix sm, ID3ChiNode root, ID3ChiNode[] childrenCandidates,
      int attribute, List<String> literals, double[] rootDistribution,
      double[][] childrenDistribution, List<String> orderOfClasses, double rootEntropy,
      double[] childrenEntropy, int xCoord, int yCoord, double gain) {
    if (attribute != 0) {
      highlightAttribute(sm, attribute, true);
    }
    EntropyVisual ev = new EntropyVisual(root, childrenCandidates, attribute, literals,
        rootDistribution, childrenDistribution, orderOfClasses, rootEntropy, childrenEntropy, lang,
        xCoord, yCoord, gain, color,translator);
    highlightAttribute(sm, attribute, true);
    for (int i = 0; i < childrenCandidates.length; i++) {
      highlightValue(sm, root, attribute, literals.get(i), true);
      ev.drawNextEntropy();
      highlightValue(sm, root, attribute, literals.get(i), false);
    }
    highlightAttribute(sm, attribute, false);
    ev.hide();

  }

  /**
   * highlightAttribute highlights/unhighlights the given attribute in the data matrix
   *
   * @param sm data matrix
   * @param attribute index of current attribute
   * @param highlight if true highlight, else unhighlight
   */
  private void highlightAttribute(StringMatrix sm, int attribute, boolean highlight) {
    if (highlight) {
      sm.highlightCell(0, attribute, null, null);
    } else {
      sm.unhighlightCell(0, attribute, null, null);
    }
  }

  /**
   * highlightValue highlight/unhighlights in a given data matrix for a given attribute every cell
   * which contains a certain value
   *
   * @param sm data matrix
   * @param root current node
   * @param attribute index of current attribute
   * @param value value to be highlighted
   * @param highlight if true highlight, else unhighlight
   */
  private void highlightValue(StringMatrix sm, ID3ChiNode root, int attribute, String value,
      boolean highlight) {
    for (int i = 0; i < root.getData().size(); i++) {
      if (root.getData().get(i).getLiterals().get(attribute).equals(value)) {
        if (highlight) {
          sm.highlightCell(i + 1, attribute, null, null);
        } else {
          sm.unhighlightCell(i + 1, attribute, null, null);
        }
      }
    }
  }

  /**
   * classifyID3ChiNode classify current ID3ChiNode if gain or chi squared is not high enough is not need to be
   * called by expand(), because the generated tree is not further used
   *
   * @param noData Text instance when there is no data
   * @param sm data matrix
   * @param hideText if true hide noData Text, else hide data matrix
   */
  public void classifyID3ChiNode(ID3ChiNode id3chinode, int numberOfClasses, List<String> orderOfClasses, Text noData,
      StringMatrix sm, boolean hideText, int indexOfCode) {
    int[] count = new int[numberOfClasses];
    for (Date d : id3chinode.getData()) {
      for (int i = 0; i < numberOfClasses; i++) {
        if (d.getLabel().equals(orderOfClasses.get(i))) {
          count[i]++;
          break;
        }
      }
    }
    int max = 0;
    int choiceLabel = 0;
    for (int i = 0; i < numberOfClasses; i++) {
      if (max < count[i]) {
        max = count[i];
        choiceLabel = i;
      }
    }
    PseudoCodeID3.highlightCode(indexOfCode);
    lang.nextStep();
    id3chinode.setAttribut(orderOfClasses.get(choiceLabel));

    id3chinode.unhighlight();
    if (hideText) {
      noData.hide();
    } else {
      sm.hide();
    }
  }

  /**
   * cloneDataSet create a new data set for a child. The new data set will not contain a given
   * attribute and only the data which contain a given value on a given attribute
   *
   * @param data the old data set
   * @param choice index of the attribute
   */
  private List<Date> cloneDataSet(List<Date> data, int choice, String value) {
    List<Date> newData = new LinkedList<>();

    for (Date d : data) {
      if (d.getLiterals().get(choice).equals(value)) {
        List<String> literals = new LinkedList<>();
        for (String s : d.getLiterals()) {
          literals.add(s);
        }
        Date newD = new Date(d.getLabel(), literals);
        newD.removeAttribute(choice);
        newData.add(newD);
      }
    }
    return newData;
  }


  /**
   * cloneAttributes clones the list of attributes and cuts out a given attribute
   *
   * @param attributes list of attributes
   * @param choice index of the attribute
   */
  private List<String> cloneAttributes(List<String> attributes, int choice) {
    List<String> newAttributes = new LinkedList<>();

    for (String a : attributes) {
      newAttributes.add(a);
    }
    newAttributes.remove(choice);

    return newAttributes;
  }

  /*
  public static void main(String[] args) {
    // Create a new language object for generating animation code
    // this requires type, name, author, screen width, screen height
    Language l = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT,
        "Quicksort Example", "Guido Rößling", 640, 480);

    Color color = new Color(114, 140, 56);

    List<Date> data = new LinkedList<>();
    List<String> attributes = new LinkedList<>();

    List<String> literals = new LinkedList<>();

/*
        attributes.add("Meme");
        attributes.add("Hello there");
        attributes.add("General Kenobi");
        attributes.add("not just the men");
        literals.add("kenobi");
        literals.add("true");
        literals.add("true");
        literals.add("true");
        data.add(new Date("+",literals));

        literals = new LinkedList<>();
        literals.add("kenobi");
        literals.add("false");
        literals.add("true");
        literals.add("true");
        data.add(new Date("-",literals));

        literals = new LinkedList<>();
        literals.add("kenobi");
        literals.add("false");
        literals.add("false");
        literals.add("true");
        data.add(new Date("-",literals));

        literals = new LinkedList<>();
        literals.add("kenobi");
        literals.add("true");
        literals.add("false");
        literals.add("true");
        data.add(new Date("-",literals));

        literals = new LinkedList<>();
        literals.add("trap");
        literals.add("true");
        literals.add("true");
        literals.add("true");
        data.add(new Date("Its a trap",literals));

        literals = new LinkedList<>();
        literals.add("men");
        literals.add("true");
        literals.add("false");
        literals.add("woman");
        data.add(new Date("but women",literals));

        literals = new LinkedList<>();
        literals.add("men");
        literals.add("true");
        literals.add("false");
        literals.add("children");
        data.add(new Date("and children too",literals));
* /
    createData(attributes, data);

    ID3Algorithm id3 = new ID3Algorithm(0/*11.34* /, data, attributes, color, l);

    id3.generate();
    id3.generateDraw();
    Animal.startAnimationFromAnimalScriptCode(l.toString());
  }
  */

  private static void createData(List<String> attributes, List<Date> data) {
    attributes.add("1");
    attributes.add("2");
    attributes.add("3");
    attributes.add("4");

    String helperLiteral = "000";
    String helperLabel = "0000000";

    List<String> literals = new LinkedList<>();
    for (int i = 0; i < 8; i++) {
      literals.add(i % 2 + helperLiteral);
      literals.add((i >> 1) % 2 + helperLiteral);
      literals.add((i >> 2) % 2 + helperLiteral);
      literals.add((i >> 3) % 2 + helperLiteral);
      data.add(new Date(i + helperLabel, literals));
      literals = new LinkedList<>();
    }
  }
}
