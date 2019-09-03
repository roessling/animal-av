package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.misc.helpers.ZBufferAnim;

import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.MatrixProperties;
import algoanim.properties.RectProperties;

public class ZBufferGenerator2 implements Generator {
  private Language         lang;
  private MatrixProperties zbufferProp;
  private MatrixProperties polygon2Prop;
  private int[][]          polygon1;
  private RectProperties   frameBufferBackgroundColor;
  private int[][]          polygon2;
  private MatrixProperties polygon1Prop;

  // private ZBufferAnim zBuffer;

  public void init() {
    lang = new AnimalScript("Z-Buffer", "Dieter Hofmann, Artem Vovk", 800, 600);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    zbufferProp = (MatrixProperties) props.getPropertiesByName("zbufferProp");
    polygon2Prop = (MatrixProperties) props.getPropertiesByName("polygon2Prop");
    polygon1 = (int[][]) primitives.get("polygon1");
    frameBufferBackgroundColor = (RectProperties) props
        .getPropertiesByName("frameBufferBackgroundColor");
    polygon2 = (int[][]) primitives.get("polygon2");
    polygon1Prop = (MatrixProperties) props.getPropertiesByName("polygon1Prop");
    new ZBufferAnim(lang, polygon1Prop, polygon2Prop, zbufferProp, polygon1,
        polygon2, frameBufferBackgroundColor);

    return lang.toString();
  }

  public String getName() {
    return "Z-Buffer";
  }

  public String getAlgorithmName() {
    return "Z-Buffer";
  }

  public String getAnimationAuthor() {
    return "Dieter Hofmann, Artem Vovk";
  }

  public String getDescription() {
    return "In computer graphics, z-buffering is the management of image depth coordinates in three-dimensional (3-D) graphics, usually done in hardware, sometimes in software. It is one solution to the visibility problem, which is the problem of deciding which elements of a rendered scene are visible, and which are hidden. The painter's algorithm is another common solution which, though less efficient, can also handle non-opaque scene elements. Z-buffering is also known as depth buffering.";
  }

  public String getCodeExample() {
    return " z-buffer(x,y)=max depth" + "\n"
        + " COLOR(x,y)=background color    " + "\n" + "\n"
        + "for(each polygon P in the polygon list) do{" + "\n"
        + "        for(each pixel(x,y) that intersects P) do{" + "\n"
        + "            Calculate z-depth of P at (x,y)" + "\n"
        + "             If (z-depth < z-buffer[x,y]) then{" + "\n"
        + "                  z-buffer[x,y]=z-depth;" + "\n"
        + "                  COLOR(x,y)=Intensity of P at(x,y);" + "\n"
        + "            }" + "\n" + "         }" + "\n" + "      }" + "\n"
        + "  display COLOR array.";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}
