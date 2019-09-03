/*
 * ID3ChiSquared.java
 * Simon Heinrich, Philipp Nothvogel, 2018 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.misc;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.id3_chi_squared.Date;
import generators.misc.id3_chi_squared.ID3Algorithm;
import translator.Translator;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ID3ChiSquared implements ValidatingGenerator {

  private Language lang;
  private String[][] data;
  private double threshold;
  private Color node_color;
  private Locale locale;
  private Translator translator;
  private static final String defaultResourcePath = "resources/ID3ChiSquared";

  public ID3ChiSquared() {
    locale = new Locale("de","DE");
    translator = new Translator(defaultResourcePath, locale);
  }

  public ID3ChiSquared(Locale locale) {
    this.locale = locale;
    this.translator = new Translator(defaultResourcePath, locale);
  }

  public ID3ChiSquared(String resourcePath, Locale locale) {
    this.locale = locale;
    this.translator = new Translator(resourcePath, locale);
  }

  public void init() {
    lang = new AnimalScript("ID3 with Chi Squared Test", "Simon Heinrich, Philipp Nothvogel", 800,
        600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {

    validateInput(props,primitives);

    this.data = (String[][]) primitives.get("data");
    this.threshold = (double) primitives.get("threshold");
    this.node_color = (Color) primitives.get("node_color");

    List<String> attributes = new LinkedList<>();
    for (int i = 0; i < data[0].length - 1; i++) {
      attributes.add(data[0][i]);
    }

    List<Date> dataList = new LinkedList<>();
    for (int i = 1; i < data.length; i++) {
      List<String> literals = new LinkedList<>();
      for (int j = 0; j < data[i].length - 1; j++) {
        literals.add(data[i][j]);
      }
      dataList.add(new Date(data[i][data[i].length - 1], literals));
    }

    ID3Algorithm id3 = new ID3Algorithm(this.threshold, dataList, attributes, this.node_color, this.lang,this.translator);
    id3.generate();
    id3.generateDraw();
    return lang.toString();
  }

  public String getName() {
    return "ID3 with Chi Squared Test";
  }

  public String getAlgorithmName() {
    return "ID3";
  }

  public String getAnimationAuthor() {
    return "Simon Heinrich, Philipp Nothvogel";
  }

  public String getDescription() {
    return translator.translateMessage("description");
  }

  public String getCodeExample() {
    StringBuilder builder = new StringBuilder();
    for (int i=1; i < 15; i++) {
      builder.append(translator.translateMessage("pseu" + i));
      builder.append("\n");
    }
    return builder.toString();
  }

  public String getFileExtension() {
    return "asu";
  }

  public Locale getContentLocale() {
    return locale;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> hashtable) throws IllegalArgumentException {

    String[][] data = (String[][]) hashtable.get("data");
    double threshold = (double) hashtable.get("threshold");
    //Color node_color = (Color) hashtable.get("node_color");

    if(data == null || data.length < 2 || data[0].length == 0){
      throw new IllegalArgumentException("data set is empty, add some data examples");
    }

    if(data[0].length == 1){
      throw new IllegalArgumentException("data set contains only the class label, add some attributes");
    }

    //less than 9 data examples and less than 5 attributes
    if(data.length > 9){
      throw new IllegalArgumentException("data set contains more than 8 data examples");
    }
    if(data[0].length > 5){
      throw new IllegalArgumentException("data set contains more than 4 attributes");
    }

    //less than 5 features per attribute
    for(int i = 0; i < data[0].length-1; i++){
      int counter = 1;
      for(int j = 2; j < data.length; j++){
        boolean helper = false;
        for(int k = 1; k < j; k++){
          helper = helper || data[k][i].equals(data[j][i]);
        }
        if(!helper){
          counter++;
        }
      }
      if(counter > 4){
        throw new IllegalArgumentException("attribute " + i + " has more than 4 values");
      }
    }

    //Attributes
    for (int i = 0; i < data[0].length; i++) {
      if (data[0][i].length() > 9 || data[0][i].equals("")) {
        throw new IllegalArgumentException("the name of attribute " + i + " has more than 9 letters");
      }
    }
    //Features
    for (int i = 1; i < data.length; i++) {
      for (int j = 0; j < data[i].length-1; j++) {
        if (data[i][j].length() > 4 || data[i][j].equals("")) {
          throw new IllegalArgumentException("value " + j + " on attribute " + i + " has more than 4 letters");
        }
      }
    }
    //Label
    for(int i = 0; i < data.length; i++){
      if(data[i][data[i].length-1].length() > 9 || data[i][data[i].length-1].equals("")){
        throw new IllegalArgumentException("the class label of data example " + i + "has more than 9 letters");
      }
    }

    //data set consistent?
    for (int i = 0; i < data.length; i++) {
      for (int j = i + 1; j < data.length; j++) {

        boolean same = true;
        for (int k = 0; k < data[i].length; k++) {
          if (!data[i][k].equals(data[j][k])) {
            same = false;
            break;
          }
        }
        if (same) {
          throw new IllegalArgumentException("the data set is not consistent, two or more data examples have the same values on every attribute");
        }
      }
    }

    if(threshold < 0.0){
      throw new IllegalArgumentException("the threshold has to be positive");
    }

    return true;
  }
}