package generators;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.MatrixPrimitive;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

@SuppressWarnings("unused")
public class AnimalDemo implements ValidatingGenerator {

  public static void main(String[] args){
    Generator g = new AnimalDemo();
    g.init();
    Animal.startAnimationFromAnimalScriptCode(g.generate(null, null));
//    Animal.startGeneratorWindow(g);
  }
  
  private Language lang;

  public AnimalDemo() {
    
  }

  @Override
  public void init() {
    lang = new AnimalScript(getName(), getAnimationAuthor(), 800, 600);
    lang.setStepMode(true);
  }
  
  @Override
  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    showMatrix();
    return lang.toString();
  }
  
  
  private void showMatrix() {
    int mSize = 5;
    String[][] sMatrix = new String[mSize][mSize];
    for (int r = 0; r < sMatrix.length; r++) {
      for (int c = 0; c < sMatrix[r].length; c++) {
        sMatrix[r][c] = "R"+r+"C"+c;
      }
    }
    MatrixProperties smp = new MatrixProperties();
    smp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY, "table");
    smp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced",Font.PLAIN,15));
    smp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    smp.set(AnimationPropertiesKeys.GRID_ALIGN_PROPERTY, "center");
    StringMatrix sm = lang.newStringMatrix(new Coordinates(100, 100), sMatrix, "Demo_"+StringMatrix.class.getSimpleName(), null, smp);

    Class<? extends Object> classObject = StringMatrix.class;
    LinkedList<Method> listOfMethods = new LinkedList<Method>();
    while(!classObject.equals(Primitive.class)){
      listOfMethods.addAll(getPublicMethodsFromClass(classObject));
      Class<? extends Object> classObjectNew = StringMatrix.class.getSuperclass();
      if(classObject.equals(classObjectNew)) {
        break;
      } else {
        classObject = classObjectNew;
      }
    }
    listOfMethods.addAll(getPublicMethodsFromClass(Primitive.class));
    sortListOfMethods(listOfMethods);
    showListOfMethods(listOfMethods, sm);
    
  }
  
  private void showListOfMethods(LinkedList<Method> listOfMethods, Primitive dependence) {
    TextProperties tpH = new TextProperties();
    tpH.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.BOLD, 20));
    Text tH = lang.newText(new Offset(0, 75, dependence, AnimalScript.DIRECTION_SW), "Public methods for "+dependence.getClass().getSimpleName(), "PMf"+dependence.getClass().getSimpleName(), null, tpH);
    
    TextProperties tpM = new TextProperties();
    tpM.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 15));
    
    Text tMLast = null;
    for (int i = 0; i < listOfMethods.size(); i++) {
      Offset offset = tMLast==null ? new Offset(50, 20, tH, AnimalScript.DIRECTION_SW) : new Offset(0, 3, tMLast, AnimalScript.DIRECTION_SW); 
      Method m = listOfMethods.get(i);
      Text tM = lang.newText(offset, m.toString(), m.getName(), null, tpM);
      tMLast = tM;
    }
  }
  
  private void sortListOfMethods(LinkedList<Method> listOfMethods) {
    Collections.sort(listOfMethods, new Comparator<Method>() {
      @Override
      public int compare(Method o1, Method o2) {
          return Collator.getInstance().compare(o1.toString(), o2.toString());
      }
    });
  }

  private LinkedList<Method> getPublicMethodsFromClass(Object obj) {
    return getPublicMethodsFromClass(obj.getClass());
  }
  private LinkedList<Method> getPublicMethodsFromClass(Class<? extends Object> classObject) {
    LinkedList<Method> listOfMethods = new LinkedList<Method>();
    Method[] allMethods = classObject.getDeclaredMethods();
    for (Method method : allMethods) {
        if (Modifier.isPublic(method.getModifiers())) {
          listOfMethods.add(method);
        }
    }
    return listOfMethods;
  }
  
  
  @Override
  public String getAlgorithmName() {
    return null;
  }

  @Override
  public String getAnimationAuthor() {
    return null;
  }

  @Override
  public String getCodeExample() {
    return null;
  }

  @Override
  public Locale getContentLocale() {
    return null;
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public String getFileExtension() {
    return null;
  }

  @Override
  public GeneratorType getGeneratorType() {
    return null;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public String getOutputLanguage() {
    return null;
  }

  @Override
  public boolean validateInput(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) throws IllegalArgumentException {
    return true;
  }

}
