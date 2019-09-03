package generators.misc.machineLearning;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

public class AprioriML extends AbstractMachineLearning {

  public AprioriML(String resourceName, Locale locale) {
    super(resourceName, locale);
  }

  private double                              minSupport;
  private double                              minConfidence;
  private StringMatrix                        dataSetMatrix;
  private LinkedList<String>                  c;
  private LinkedList<AssociationRule<String>> associationRules;
  private double                              n;
  private SourceCode                          sc;
  private LinkedList<String>                  sk;
  private LinkedList<String>                  confidentAR;

  public void aprioriFirstStep(String[][] data) {

    int k = 1;
    c = new LinkedList<String>();

    associationRules = new LinkedList<AssociationRule<String>>();

    for (int i = 0; i < data[0].length; i++) {
      c.add(data[0][i]);
    }

    // k = 1
    String[][] cTable = createEmptyMatrix(data[0].length + 1, 2);
    cTable[0][0] = "Itemset";
    cTable[0][1] = "Support";

    sc.highlight(0);
    sc.highlight(1);
    setExplanationText(translator.translateMessage("oneElementSet"));

    StringMatrix cMatrix = lang.newStringMatrix(
        new Offset(0, 10, "titleCalc", AnimalScript.DIRECTION_SW), cTable, "c1",
        null, matrixProps);

    lang.nextStep();

    for (int i = 1; i < cTable.length; i++) {
      cMatrix.put(i, 0, c.get(i - 1), null, null);
    }

    lang.nextStep();
    sc.unhighlight(0);
    sc.unhighlight(1);
    sc.highlight(2);
    setExplanationText(translator.translateMessage("calcSuppFormula"));
    sc.highlight(3);
    lang.nextStep();

    for (int i = 1; i < cTable.length; i++) {

      // calc support
      double amount = countFrequency(cMatrix.getElement(i, 0), data);
      double result = round(amount / n, 2);
      setExplanationText("support( {" + c.get(i - 1) + "} )" + " = " + amount
          + "/" + n + " = " + result);

      cMatrix.put(i, 1, Double.toString(result), null, null);
      lang.nextStep();
      unhighlightAll(dataSetMatrix);

    }

    sc.toggleHighlight(3, 4);
    setExplanationText(
        translator.translateMessage("removeItemset") + " " + minSupport);

    String[][] sTable = removeInfrequent(getData(cMatrix), minSupport);
    c.clear();

    for (int i = 1; i < sTable.length; i++) {
      c.add(sTable[i][0]);
    }

    lang.nextStep();

    cMatrix.hide();
    StringMatrix sMatrix = lang.newStringMatrix(
        new Offset(0, 10, "titleCalc", AnimalScript.DIRECTION_SW), sTable,
        "sMatrix", null, matrixProps);
    LinkedList<LinkedList<String>> itemset = new LinkedList<LinkedList<String>>();
    sc.toggleHighlight(4, 5);

    // add s_k to s
    sk = new LinkedList<String>();
    
    StringBuilder s = new StringBuilder();
    s.append("S_1 = {");
    for (int i = 1; i < sTable.length; i++) {
      s.append(sTable[i][0]).append(", ");
    }

    LinkedList<LinkedList<String>> items = getItemset(sTable);

    for (int i = 0; i < items.size(); i++) {
      associationRules.add(new AssociationRule<String>(items.get(i)));
    }

    if(sTable.length > 1) {
    sk.add(s.substring(0, s.length() - 2) + "}");
    setExplanationText(
        translator.translateMessage("addItemset") + sk.get(k - 1));

    lang.nextStep();
    }

    // k > 1
    while (sTable.length > 2) {

      // buildUnions

      if (k == 1) {
        sc.toggleHighlight(5, 6);
        sc.highlight(7);
        itemset = buildFirstUnion(c, k, sMatrix);

      } else {
        itemset = buildUnions(getItemset(sTable), k, sMatrix);
      }

      lang.nextStep();
      unhighlightAll(sMatrix);
      sc.unhighlight(6);
      sc.toggleHighlight(7, 8);
      setExplanationText("k = " + (k + 1));

      cTable = createCTable(itemset);

      lang.nextStep();
      sc.toggleHighlight(8, 3);
      sMatrix.hide();
      cMatrix = lang.newStringMatrix(
          new Offset(0, 10, "titleCalc", AnimalScript.DIRECTION_SW), cTable,
          "c", null, matrixProps);

      lang.nextStep();
      for (int i = 1; i < cMatrix.getNrRows(); i++) {
        // calc support
        double amount = countFrequency(itemset.get(i - 1), data);
        double result = round(amount / n,2);
        setExplanationText("support( {" + cMatrix.getElement(i, 0) + "} )"
            + " = " + amount + "/" + n + " = " + result);
        cMatrix.put(i, 1,
            "" + round((countFrequency(itemset.get(i - 1), data) / n), 2), null,
            null);

        lang.nextStep();
        unhighlightAll(dataSetMatrix);

        cTable[i][1] = cMatrix.getElement(i, 1);
      }

      sc.toggleHighlight(3, 4);
      setExplanationText(
          translator.translateMessage("removeItemset") + " " + minSupport);
      lang.nextStep();

      // remove infrequent
      cMatrix.hide();
      sc.toggleHighlight(4, 5);
      sTable = removeInfrequent(cTable, minSupport);

      // add s_k to s
      s.setLength(0);
      k++;
      s.append("S_" + k + " = {");

      for (int i = 1; i < sTable.length; i++) {
        s.append("{").append(sTable[i][0]).append("}, ");
      }
      

      items = getItemset(sTable);

      for (int i = 0; i < items.size(); i++) {
        associationRules.add(new AssociationRule<String>(items.get(i)));
      }

      sMatrix = lang.newStringMatrix(
          new Offset(0, 10, "titleCalc", AnimalScript.DIRECTION_SW), sTable,
          "sMatrix", null, matrixProps);
      lang.nextStep();
      if(sTable.length > 1) {
      sk.add(s.substring(0, s.length() - 2) + "}");
      setExplanationText(
          translator.translateMessage("addItemset") + sk.get(k - 1));

      lang.nextStep();
      }
      sc.toggleHighlight(5, 6);
      sc.highlight(7);
    }

    setExplanationText(translator.translateMessage("noCombination"));

    lang.nextStep();
    sc.unhighlight(6);
    sc.toggleHighlight(7, 9);
    sc.unhighlight(2);

    String sComplete = "";
    for (int i = 0; i < sk.size(); i++) {
      sComplete = sComplete + sk.get(i) + "\n";
    }
    setExplanationText(sComplete + " ");
    sMatrix.hide();

  }

  public void aprioriSecondStep(String[][] data) {

    sc.toggleHighlight(9, 10);
    
    LinkedList<AssociationRule<String>> items = new LinkedList<AssociationRule<String>>();

    LinkedList<AssociationRule<String>> oneElementSets = new LinkedList<AssociationRule<String>>();

    for (int i = 0; i < associationRules.size(); i++) {
      if (associationRules.get(i).getItemset().size() == 1)
        oneElementSets.add(associationRules.get(i));
    }

    String[][] conf = createEmptyMatrix(oneElementSets.size() + 1, 2);
    StringMatrix confTable = lang.newStringMatrix(
        new Offset(0, 10, "titleCalc", AnimalScript.DIRECTION_SW), conf,
        "sMatrix", null, matrixProps);
    confTable.put(0, 0, "Itemsets", null, null);
    confTable.put(0, 1, "Confidence", null, null);

    setExplanationText(translator.translateMessage("calcConfFormula"));
    lang.nextStep();
    setExplanationText(translator.translateMessage("confOneElement"));

    
    for (int i = 1; i < confTable.getNrRows(); i++) {
      confTable.put(i, 0, oneElementSets.get(i - 1).getItemset().toString(),
          null, null);
      confTable.put(i, 1, "1.0", null, null);
    }

    if(confTable.getNrRows()> 1) {
      for(int i = 1; i < confTable.getNrRows(); i++) {
        confidentAR.add("{ " + confTable.getElement(i, 0) + " }, ");
        }
      }
    

    lang.nextStep();
    confTable.hide();

    for (int x = 2; x <= sk.size(); x++) {

      items = new LinkedList<AssociationRule<String>>();

      for (int i = 0; i < associationRules.size(); i++) {
        if (associationRules.get(i).getItemset().size() == x) {
          items.add(associationRules.get(i));
        }
      }

      // calc number of association rules of itemsets with size k
      int len = 0;
      for (int i = 0; i < items.size(); i++) {
        AssociationRule<String> currentRule = items.get(i);
        currentRule.createAssociationRules();
        len = len + currentRule.getRight().size();
      }

      conf = createEmptyMatrix(len + 1, 2);
      conf[0][0] = "Itemsets";
      conf[0][1] = "Confidence";

      int row = 1;
      for (int i = 0; i < items.size(); i++) {

        AssociationRule<String> currentRule = items.get(i);
        currentRule.createAssociationRules();
        LinkedList<LinkedList<String>> left = new LinkedList<LinkedList<String>>(
            currentRule.getLeft());
        LinkedList<LinkedList<String>> right = new LinkedList<LinkedList<String>>(
            currentRule.getRight());

        for (int j = 0; j < left.size(); j++) {
          String ar = left.get(j).toString() + " - > "
              + right.get(j).toString();
          String res = String
              .valueOf(round(countFrequency(currentRule.getItemset(), data)
                  / countFrequency(left.get(j), data), 2));
          conf[row][0] = ar;
          conf[row][1] = res;
          row++;
        }

      }
      unhighlightAll(dataSetMatrix);

      setExplanationText(
          translator.translateMessage("buildCombination", String.valueOf(x)));
      confTable = lang.newStringMatrix(
          new Offset(0, 10, "titleCalc", AnimalScript.DIRECTION_SW), conf,
          "sMatrix", null, matrixProps);
      lang.nextStep();
      setExplanationText(translator.translateMessage("removeItemsetConf") + " "
          + minConfidence);
      lang.nextStep();

      confTable.hide();
      conf = removeInfrequent(conf, minConfidence);
      confTable = lang.newStringMatrix(
          new Offset(0, 10, "titleCalc", AnimalScript.DIRECTION_SW), conf,
          "sMatrix", null, matrixProps);
      
      
    if(conf.length > 1) {
    for(int i = 1; i < conf.length; i++) {
      confidentAR.add("{ " + conf[i][0] + " }, ");
      }
    }
    
      lang.nextStep();
      confTable.hide();
    }
    

  }

  /**
   * create table for calculation
   * 
   * @param set
   *          set with different sets of attributes
   * @return table with itemsets and empty column for support
   */
  public String[][] createCTable(LinkedList<LinkedList<String>> set) {

    String[][] table = createEmptyMatrix(set.size() + 1, 2);
    table[0][0] = "Itemset";
    table[0][1] = "Support";

    for (int i = 1; i < table.length; i++) {
      String s = set.get(i - 1).toString().replace("[", "").replace("]", "");
      table[i][0] = s;
    }

    return table;

  }

  /**
   * build union for all one-element sets
   * 
   * @param s
   *          set with all attributes
   * @param k
   *          iteration
   * @param m
   *          calculation table
   * @return
   */
  public LinkedList<LinkedList<String>> buildFirstUnion(LinkedList<String> s,
      int k, StringMatrix m) {

    LinkedList<LinkedList<String>> union = new LinkedList<LinkedList<String>>();

    setExplanationText(translator.translateMessage("buildCombination1"));
    String itemsets = "";

    for (int i = 0; i < s.size() - 1; i++) {

      for (int j = i + 1; j < s.size(); j++) {
        lang.nextStep();
        unhighlightAll(m);
        LinkedList<String> temp = new LinkedList<String>();
        temp.add(s.get(i).toString());
        temp.add(s.get(j).toString());
        union.add(temp);

        m.highlightCell(i + 1, 0, null, null);
        m.highlightCell(j + 1, 0, null, null);

        itemsets = itemsets + "{" + m.getElement(i + 1, 0) + ", "
            + m.getElement(j + 1, 0) + "}\n";
        setExplanationText(
            translator.translateMessage("newItemset") + itemsets);
      }
    }

    return union;
  }

  /**
   * get all itemsets
   * 
   * @param data
   *          training data
   * @return set of all itemsets
   */
  public LinkedList<LinkedList<String>> getItemset(String[][] data) {

    LinkedList<LinkedList<String>> l = new LinkedList<LinkedList<String>>();

    for (int i = 1; i < data.length; i++) {
      String[] parts = data[i][0].split(",");
      LinkedList<String> part = new LinkedList<String>();
      for (int j = 0; j < parts.length; j++) {
        part.add(parts[j].replace(" ", ""));
      }
      l.add(part);
    }

    return l;

  }

  /**
   * build unions for all sets with the same first (k-1) attributes
   * 
   * @param s
   *          itemsets
   * @param k
   *          iteration
   * @param m
   *          training data
   * @return united itemsets
   */
  public LinkedList<LinkedList<String>> buildUnions(
      LinkedList<LinkedList<String>> s, int k, StringMatrix m) {

    LinkedList<LinkedList<String>> union = new LinkedList<LinkedList<String>>();
    LinkedList<LinkedList<String>> copy = new LinkedList<LinkedList<String>>();
    LinkedList<LinkedList<String>> prefix = new LinkedList<LinkedList<String>>();
    LinkedList<LinkedList<String>> suffix = new LinkedList<LinkedList<String>>();

    String itemsets = "";

    setExplanationText(translator.translateMessage("buildCombination2",
        String.valueOf((k - 1))));

    for (int i = 0; i < s.size(); i++) {
      String[] tmp = (String[]) s.get(i).toArray(new String[s.get(i).size()]);
      LinkedList<String> x = new LinkedList<String>(Arrays.asList(tmp));
      prefix.add(x);
      LinkedList<String> y = new LinkedList<String>(Arrays.asList(tmp));
      suffix.add(y);
      LinkedList<String> z = new LinkedList<String>(Arrays.asList(tmp));
      copy.add(z);
    }

    // prepare suffixes and prefixes
    for (int i = 0; i < s.size(); i++) {
      prefix.get(i).removeLast();
      for (int j = 0; j < k - 1; j++) {
        suffix.get(i).removeFirst();
      }
    }

    for (int i = 0; i < s.size() - 1; i++) {
      for (int j = i + 1; j < s.size(); j++) {

        // two itemsets have the same first k-1 elements
        if (prefix.get(i).equals(prefix.get(j))) {

          lang.nextStep();
          unhighlightAll(m);

          String[] sa = (String[]) s.get(i)
              .toArray(new String[s.get(i).size()]);
          LinkedList<String> t = new LinkedList<String>(Arrays.asList(sa));
          t.addAll(suffix.get(j));
          union.add(t);
          m.highlightCell(i + 1, 0, null, null);
          m.highlightCell(j + 1, 0, null, null);

          itemsets = itemsets + "{"
              + t.toString().replace("[", "").replace("]", "") + "}\n";
          setExplanationText(
              translator.translateMessage("newItemset") + itemsets);
        }
      }
    }

    return union;
  }

  /**
   * remove all infrequent sets
   * 
   * @param data
   *          calculation table with itemsets and support values
   * @return calculation table without infrequent itemsets
   */
  public String[][] removeInfrequent(String[][] data, double val) {

    LinkedList<String[]> d = new LinkedList<String[]>();

    for (int i = 1; i < data.length; i++) {

      if ((Double.parseDouble(data[i][1]) >= val)) {
        d.add(data[i]);
      }
    }

    String[][] newData = new String[d.size() + 1][2];
    newData[0] = data[0];

    for (int i = 1; i < newData.length; i++) {
      newData[i] = d.get(i - 1);
    }

    return newData;

  }

  /**
   * count frequency of a specific itemset
   * 
   * @param item
   *          itemset
   * @param data
   *          training data
   * @return frequency of itemset in training data
   */
  public double countFrequency(LinkedList<String> item, String[][] data) {

    LinkedList<String> items = (LinkedList<String>) item.clone();
    String[][] d = data;
    int index;
    LinkedList<String[]> filtered = new LinkedList<String[]>();
    String[][] filteredData = null;

    // filter all examples with the attribute values "yes"
    while (items.size() > 0) {

      index = getIndex(data, items.getFirst());
      filtered = new LinkedList<String[]>();
      filtered.add(new String[] { "Itemsets", "Support" });

      for (int i = 1; i < d.length; i++) {

        if (d[i][index].equalsIgnoreCase("yes")) {
          filtered.add(d[i]);
        }
      }

      filteredData = new String[filtered.size()][2];
      for (int i = 0; i < filtered.size(); i++) {
        filteredData[i] = filtered.get(i);
      }

      d = filteredData;

      items.removeFirst();

    }

    // highlight all rows with searched item
    for (int i = 1; i < d.length; i++) {
      for (int j = 1; j < data.length; j++) {
        if (d[i].equals(data[j])) {
          dataSetMatrix.highlightCellColumnRange(j, 0,
              dataSetMatrix.getNrCols() - 2, null, null);
          dataSetMatrix.highlightCell(j,
              dataSetMatrix.getNrCols() - 1, null, null);
        }
      }

    }

    return (double) filteredData.length - 1;

  }

  /**
   * count frequency of a specific attribute in training data
   * 
   * @param s
   *          searched attribute
   * @param data
   *          training data
   * @return frequency of s in training data
   */
  public double countFrequency(String s, String[][] data) {
    int counter = 0;
    int index = 0;

    for (int i = 0; i < data[0].length; i++) {
      if (data[0][i].equalsIgnoreCase(s)) {
        index = i;
      }
    }

    for (int i = 1; i < data.length; i++) {
      if (data[i][index].equalsIgnoreCase("yes")) {

        dataSetMatrix.highlightCellColumnRange(i, 0,
            dataSetMatrix.getNrCols() - 2, null, null);
        dataSetMatrix.highlightCell(i,
            dataSetMatrix.getNrCols() - 1, null, null);
        counter++;
      }
    }

    return (double) counter;
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {

    String[][] trainingDataValidate = (String[][]) primitives
        .get("stringMatrix");

    double support = (double) primitives.get("support");
    double confidence = (double) primitives.get("confidence");

    if (trainingDataValidate == null || trainingDataValidate.length < 2
        || trainingDataValidate[0].length < 2) {
      throw new IllegalArgumentException("Please insert valid training data!");
    }

    for (int i = 1; i < trainingDataValidate.length; i++) {
      for (int j = 0; j < trainingDataValidate[0].length; j++) {

        if (!trainingDataValidate[i][j].equalsIgnoreCase("yes")
            && !trainingDataValidate[i][j].equalsIgnoreCase("no")) {
          throw new IllegalArgumentException(
              "Please specifiy the class of each example by adding 'yes' or 'no' to the class attribute.");
        }

        if (trainingDataValidate[i][j] == null
            || trainingDataValidate[i][j].equals("")) {
          throw new IllegalArgumentException("Missing values are not allowed!");
        }
      }
    }

    if (support < 0.0 || support > 1.0 || confidence < 0.0
        || confidence > 1.0) {
      throw new IllegalArgumentException(
          "Set the support and confidence between 0.0 and 1.0.");
    }

    return true;

  }

  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {

    initProps(props);

    createIntro(translator.translateMessage("algorithmName"),
        translator.translateMessage("description"), new Coordinates(10, 10),
        new Coordinates(300, 60), new Coordinates(60, 15),
        new Coordinates(10, 100));

    lang.nextStep();
    lang.hideAllPrimitives();

    confidentAR = new LinkedList<String>();
    
    MultipleChoiceQuestionModel introQuestion = new MultipleChoiceQuestionModel(
        "introQuestion");
    introQuestion.setPrompt(translator.translateMessage("introQuestion"));
    introQuestion.addAnswer("0",
        translator.translateMessage("introQuestionAnswer1"), 1,
        translator.translateMessage("introQuestionFeedback1"));
    introQuestion.addAnswer("1",
        translator.translateMessage("introQuestionAnswer2"), 0,
        translator.translateMessage("introQuestionFeedback2"));
    introQuestion.addAnswer("2",
        translator.translateMessage("introQuestionAnswer3"), 0,
        translator.translateMessage("introQuestionFeedback3"));
    introQuestion.setNumberOfTries(3);
    lang.addMCQuestion(introQuestion);

    MultipleChoiceQuestionModel outroQuestion = new MultipleChoiceQuestionModel(
        "outroQuestion");
    outroQuestion.setPrompt(translator.translateMessage("outroQuestion"));
    outroQuestion.addAnswer("3",
        translator.translateMessage("outroQuestionAnswer1"), 1,
        translator.translateMessage("outroQuestionFeedback1"));
    outroQuestion.addAnswer("4",
        translator.translateMessage("outroQuestionAnswer2"), 0,
        translator.translateMessage("outroQuestionFeedback2"));
    outroQuestion.addAnswer("5",
        translator.translateMessage("outroQuestionAnswer3"), 0,
        translator.translateMessage("outroQuestionFeedback3"));
    outroQuestion.setNumberOfTries(3);

    String[][] data = (String[][]) primitives.get("stringMatrix");

    minSupport = (double) primitives.get("support");
    minConfidence = (double) primitives.get("confidence");

    // title for training data
    lang.newText(new Coordinates(30, 30),
        translator.translateMessage("dataset"), "titleTrainingData", null,
        titleProps);

    // training data
    dataSetMatrix = lang.newStringMatrix(
        new Offset(0, 10, "titleTrainingData", AnimalScript.DIRECTION_SW), data,
        "dataset", null, matrixProps);

    // title for calculation
    lang.newText(new Offset(100, -30, "dataset", AnimalScript.DIRECTION_NE),
        translator.translateMessage("calculation"), "titleCalc", null,
        titleProps);

    // title for pseudo code
    lang.newText(new Offset(200, 0, "titleCalc", AnimalScript.DIRECTION_NE),
        "Pseuco Code:", "titlePseudoCode", null, titleProps);

    sc = lang.newSourceCode(
        new Offset(0, 30, "titlePseudoCode", AnimalScript.DIRECTION_NW), "sc",
        null, scProps);

    sc.addCodeLine("1.) k = 1", null, 0, null);
    sc.addCodeLine("2.) C_1 = I (all items)", null, 0, null);
    sc.addCodeLine("3.) while C_k != null ", null, 0, null);
    sc.addCodeLine("a) Calculate support", null, 1, null);
    sc.addCodeLine("b) S_k = C_k without all infrequent item sets", null, 1,
        null);
    sc.addCodeLine("c) S = Union(S, S_k)", null, 1, null);
    sc.addCodeLine(
        "d) C_k+1 = all sets with k+1 elements that can be formed by", null, 1,
        null);
    sc.addCodeLine("   uniting of two item sets in S_k", null, 1, null);
    sc.addCodeLine("e) k++", null, 1, null);
    sc.addCodeLine("4.) return S", null, 0, null);
    sc.addCodeLine("5.) Create association rules and calculate confidence",
        null, 0, null);

    lang.newText(new Offset(0, 50, "sc", AnimalScript.DIRECTION_SW),
        translator.translateMessage("explanation"), "titleExplanation", null,
        titleProps);

    n = (double) data.length - 1;

    aprioriFirstStep(data);
    lang.nextStep();
    aprioriSecondStep(data);

   StringBuilder sb = new StringBuilder();
   for(int i = 0; i < confidentAR.size(); i++) {
     sb.append(confidentAR.get(i));
     
     if( (i-4) % 5 == 0)
       sb.append("\n");
     
   }
   
   if(sb.length() == 0) {
     sb.append("{ empty }   ");
   }
    
    setExplanationText(translator.translateMessage("terminates", sb.toString().substring(0, sb.length()-2)));
    lang.addMCQuestion(outroQuestion);
    lang.finalizeGeneration();
    return lang.toString();
  }

  @Override
  public String getCodeExample() {
    return "1.) k = 1" + "\n" + "2.) C_1 = I (all items)" + "\n"
        + "3.) while C_k != null" + "\n" + "       a) Calculate support" + "\n"
        + "       b) S_k = C_k without all infrequent item sets" + "\n"
        + "       c) S = Union(S, S_k)" + "\n"
        + "       d) C_k+1 = all sets with k+1 elements that can be formed by"
        + "\n" + "          uniting of two item sets in S_k" + "\n"
        + "       e) k++" + "\n" + "4.) return S" + "\n"
        + "5.) Create association rules and calculate confidence";
  }

}
