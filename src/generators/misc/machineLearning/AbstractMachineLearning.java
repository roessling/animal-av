package generators.misc.machineLearning;
import java.awt.Color;
import java.awt.Font;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;

public abstract class AbstractMachineLearning implements ValidatingGenerator {
  
  Language lang;
  SourceCode explanation;
  Translator translator;
  SourceCodeProperties expProps;
  SourceCodeProperties scProps;
  TextProperties textProps;
  TextProperties titleProps; 
  MatrixProperties matrixProps;
  
  public AbstractMachineLearning(String resourceName, Locale locale) {
    translator = new Translator(resourceName, locale);
  }
  
  @Override
  public String getAnimationAuthor() {
    return "Stanislaw Kin";
  }
  
  @Override
  public Locale getContentLocale() {
    return translator.getCurrentLocale();
  }

  @Override
  public String getFileExtension() {
    return "asu";
  }
 
  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }
  
  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }
  
  @Override
  public String getAlgorithmName() {
    return translator.translateMessage("algorithmName");
  }

  @Override
  public String getName() {
    return translator.translateMessage("generatorName");
  }
  
  @Override
  public String getDescription() {
    return translator.translateMessage("description");
  }
  
  @Override
  public void init() {
    lang = Language.getLanguageInstance(AnimationType.ANIMALSCRIPT,
        getAlgorithmName(), getAnimationAuthor(), 1000, 1000);
    lang.setStepMode(true);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

   // initPrimitiveProperties();
  }
  
  
  public void initProps(AnimationPropertiesContainer props) {
    titleProps = (TextProperties) props.getPropertiesByName("titleProps");
    titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.BOLD, 18));
    textProps = (TextProperties) props.getPropertiesByName("textProps");
    matrixProps = (MatrixProperties) props.getPropertiesByName("matrixProps");
    scProps = (SourceCodeProperties) props.getPropertiesByName("scProps");
    expProps = (SourceCodeProperties) props.getPropertiesByName("expProps");
  }
  
  /**
   * initialize all properties for different primitives
   */
  public void initPrimitiveProperties() {
    
    // init properties for titles
    titleProps = new TextProperties();
    titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.BOLD, 18));
    titleProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    
    // init properties for additional text
    textProps = new TextProperties();
    textProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.PLAIN, 12));
    textProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    
    //init properties for matrix
    matrixProps = new MatrixProperties();
    matrixProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    matrixProps.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
    matrixProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    matrixProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
        Color.LIGHT_GRAY);
    matrixProps.set(AnimationPropertiesKeys.GRID_BORDER_COLOR_PROPERTY,
        Color.BLACK);
    matrixProps.set(
        AnimationPropertiesKeys.GRID_HIGHLIGHT_BORDER_COLOR_PROPERTY,
        Color.BLACK);
    matrixProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    
    //init properties for source code
    scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("Monospaced", Font.PLAIN, 12));
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    
    //init properties for explanation text
    expProps = new SourceCodeProperties();
    expProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
        new Font("SansSerif", Font.PLAIN, 12));
    expProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  }
  
  /**
   * unhighlight all source code lines
   * @param s source code element
   * @param numOfLines number of lines in the source code
   */
  public void unhighlightAllSourceCodeLines(SourceCode s, int numOfLines) {
    for(int i = 0; i < numOfLines; i++) {
      s.unhighlight(i);
    }
  }
  
  
  /**
   * create an intro as the first slide in the visualization with a framed headline and a description.
   * @param headline name of the algorithm
   * @param description description of the algorithm
   * @param uL upper left coordinates of the frame
   * @param lR lower right coordinates of the frame
   * @param tUL upper left coordinates of the headline text
   * @param dC coordinates of the description
   */
public void createIntro(String headline, String description, Coordinates uL, Coordinates lR, Coordinates tUL, Coordinates dC) {
  
  RectProperties headlinebgProps = new RectProperties();
  RectProperties shadowProps = new RectProperties();
  
  // shadow properties (back rectangle in the background)
  shadowProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  shadowProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.BLACK);
  shadowProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
  shadowProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
  
  // headline rectangle properties
  headlinebgProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
  headlinebgProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);
  headlinebgProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.ORANGE);
  headlinebgProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
  
  // shadow rectangle
  lang.newRect(uL, lR, "headlinebg", null, headlinebgProps);
  Coordinates shadowUpperLeft = new Coordinates(uL.getX() + 10, uL.getY()+10);
  Coordinates shadowLowerRight = new Coordinates(lR.getX() + 10, lR.getY()+10);
  
  // headline rectangle
  lang.newRect(shadowUpperLeft, shadowLowerRight, "shadow", null, shadowProps);
  TextProperties headlineProps = new TextProperties();
  headlineProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 30));
  
  //headline text
  lang.newText(tUL, headline, "headline", null, headlineProps);
  
  SourceCodeProperties descProps = new SourceCodeProperties();
  descProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.PLAIN, 12));

  SourceCode sc = lang.newSourceCode(dC, "description", null, descProps);
  sc.addMultilineCode(description, "desc", null);  
  }

/**
 * set the explanation and delete the previous text
 * @param s explanation
 */
public void setExplanationText(String s) {
  if (explanation != null) {
    explanation.hide();
  }
  explanation = lang.newSourceCode(
      new Offset(0, 0, "titleExplanation", AnimalScript.DIRECTION_SW),
      "explanation", null, expProps);
  explanation.addMultilineCode(s, null, null);
}

/**
 * round up to d decimal digits
 * @param v value
 * @param d number of digits
 * @return rounded value
 */
public double round(double v, int d) {
  return Math.round(v * Math.pow(10.0,d)) / Math.pow(10.0,d);
}

/**
 * unhighlight all cells in a StringMatrix
 * @param m StringMatrix
 */
public void unhighlightAll(StringMatrix m) {
  
  for(int i = 0; i < m.getNrRows(); i++) {
    m.unhighlightCellColumnRange(i, 0, m.getNrCols()-2, null, null);
    m.unhighlightCell(i, m.getNrCols()-1, null, null);
  }
}

/**
 * create a matrix filled with white spaces to avoid getting nulls in StringMatrix
 * @param row number of rows
 * @param col number of columns
 * @return string matrix filled with white spaces
 */
public String[][] createEmptyMatrix(int row, int col){
  
  String[][] m = new String[row][col]; 
  
  for(int i = 0; i < row; i++) {
    for(int j = 0; j < col; j++) {
      m[i][j] = " ";
    }
  }
  
  return m;
}

/**
 * transform a StringMatrix to a String[][]
 * @param s StringMatrix
 * @return data of s
 */
public String[][] getData(StringMatrix s){
  String[][] m = new String[s.getNrRows()][s.getNrCols()];
  
  for(int i = 0; i < s.getNrRows(); i++) {
    for(int j = 0; j < s.getNrCols(); j++) {
      
      m[i][j] = s.getElement(i, j);
      
    }
  }
  
  return m;
}


/**
 * get the amount of examples with a special classification
 * @param examples training data
 * @param classValue classification
 * @return amount of examples
 */
public int countClassValue(String[][] examples, String classValue) {
  int num = 0;

  for (int i = 1; i < examples.length; i++) {

    if (examples[i][examples[0].length - 1].equalsIgnoreCase(classValue)) {
      num++;
    }
  }

  return num;
}

/**
 * get all attribute values of attribute i
 * @param examples training data
 * @param index column of attribute
 * @return
 */
public String[] getValuesForAttribute(StringMatrix examples, int index) {

  HashSet<String> values = new HashSet<String>();

  for (int i = 1; i < examples.getNrRows(); i++) {
    for (int j = 0; j < examples.getNrCols(); j++) {
      if (j == index) {
        values.add(examples.getElement(i, j));
      }
    }
  }

  String[] stringValues = new String[values.size()];
  stringValues = values.toArray(stringValues);

  return stringValues;
}

/**
 * get all attribute values of attribute i
 * @param examples training data
 * @param index column of attribute
 * @return
 */
public String[] getValuesForAttribute(String[][] examples, int index) {

  HashSet<String> values = new HashSet<String>();

  for (int i = 1; i < examples.length; i++) {
    for (int j = 0; j < examples[0].length; j++) {
      if (j == index) {
        values.add(examples[i][j]);
      }
    }
  }

  String[] stringValues = new String[values.size()];
  stringValues = values.toArray(stringValues);

  return stringValues;
}

/**
 * get the class of an example
 * 
 * @param example
 *          example
 * @return classification of example
 */
public String getClassOfExample(String[] example) {
  return example[example.length - 1];
}

/**
 * returns the column index of the attribute
 * @param data
 * @param attribute
 * @return
 */
public int getIndex(String[][] data, String attribute) {
  
  int index = 0; 
  
  for(int i = 0; i < data[0].length; i++) {
    if(data[0][i].equalsIgnoreCase(attribute)) {
      index = i;
    }
  }
  
  return index;
}

/**
 * check whether string is a numerical value
 * @param s string
 * @return true if string is a number
 */
public boolean isNumericAttribute(String s) {
  return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
}

}
