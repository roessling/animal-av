/*
 * Bresenham.java
 * (c) 2010, Dominik Fischer, all rights reserved.
 */
package generators.graphics;

import generators.AnnotatedAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

/**
 * <p>
 * A {@link Generator} that creates an animation displaying the Bresenham line
 * rasterization Algorithm.
 * </p>
 * 
 * @author Dominik Fischer
 */
public class Bresenham extends AnnotatedAlgorithm {

  @Override
  public String generate(AnimationPropertiesContainer properties,
      Hashtable<String, Object> primitives) {
    init();
    final int destinationX = (Integer) primitives.get("X");
    final int destinationY = (Integer) primitives.get("Y");
    lang = new AnimalScript("Bresenham Algorithmus", "Dominik Fischer", 640,
        480);
    if (destinationX < destinationY) {
      lang.newText(
          new Coordinates(10, 10),
          "Diese Variante des Algorithmus kann nur mit Zielpunkten "
              + "umgehen, deren X-Koordinate größer als ihre Y-Koordinate ist.",
          "error", null);
      return lang.toString();
    }
    lang.setStepMode(true);
    final int size = (Integer) primitives.get("Groesse");

    // Setup.
    { // Title.
      final TextProperties textProperties = new TextProperties();
      textProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 24));
      lang.newText(new Coordinates(40, 30), "Bresenham-Rasterisierung",
          "title", null, textProperties);
    }
    { // Title frame.
      final RectProperties rectProperties = new RectProperties();
      rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
      rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.GRAY);
      lang.newRect(new Offset(-5, -5, "title", "NW"), new Offset(5, 5, "title",
          "SE"), "titleFrame", null, rectProperties);
    }
    final SourceCode intro;
    { // Introduction text.
      final SourceCodeProperties scProperties = new SourceCodeProperties();
      scProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
          Font.SANS_SERIF, Font.PLAIN, 18));
      intro = lang.newSourceCode(new Coordinates(20, 75), "intro", null,
          scProperties);
      intro.addCodeLine(
          "Dieser Algorithmus rasterisiert eine Linie vom Nullpunkt zu", "1",
          0, null);
      intro.addCodeLine(
          "einem gegebenen Endpunkt. Dazu wird die Abweichung von der", "2", 0,
          null);
      intro.addCodeLine(
          "Optimallinie in y-Richtung akkumuliert und bei zureichender", "3",
          0, null);
      intro.addCodeLine(
          "Höhe durch einen seitlichen Schritt wieder ausgeglichen.", "4", 0,
          null);
    }
    sourceCode = lang.newSourceCode(new Coordinates(20, 75), "code", null,
        (SourceCodeProperties) properties.getPropertiesByName("Quellcode"));
    parse();
    sourceCode.hide();
    lang.nextStep();

    intro.hide();
    sourceCode.show();
    lang.nextStep();

    { // The optimal line.
      final int halfSize = size / 2;
      lang.newPolyline(new Node[] {
          new Coordinates(300 + halfSize, 200 + halfSize),
          new Coordinates(300 + destinationX * size + halfSize, 200
              + destinationY * size + halfSize) }, "line", null,
          (PolylineProperties) properties.getPropertiesByName("Linie"));
    }
    exec("initdelta");
    lang.nextStep();

    final double delta = (double) destinationY / destinationX;
    lang.newText(new Coordinates(300, 80), "delta = " + delta, "delta", null,
        (TextProperties) properties.getPropertiesByName("Variablen"));
    vars.declare("double", "delta", String.valueOf(delta));
    exec("initerror");
    lang.nextStep();

    double error = 0;
    final Text errorText = lang.newText(new Coordinates(300, 130), "error = 0",
        "error", null,
        (TextProperties) properties.getPropertiesByName("Variablen"));

    exec("initpos");
    lang.nextStep();

    final Rect cursor;
    {
      final RectProperties cursorProperties = new RectProperties();
      cursorProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
      cursorProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY,
          (Color) primitives.get("Cursorfarbe"));
      cursor = lang.newRect(new Coordinates(300, 200), new Coordinates(
          300 + size, 200 + size), "cursor", null, cursorProperties);
    }
    exec("while");
    lang.nextStep();

    int y = 0;
    int x = 0;
    final RectProperties rectProperties = new RectProperties();
    rectProperties.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 3);
    rectProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.WHITE);
    rectProperties.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProperties.set(AnimationPropertiesKeys.FILL_PROPERTY,
        (Color) primitives.get("Zeichenfarbe"));
    while (x <= destinationX) {
      exec("plot");
      lang.nextStep("zeichne " + x + "," + y);

      lang.newRect(new Coordinates(300 + x * size, 200 + y * size),
          new Coordinates(300 + (x + 1) * size, 200 + (y + 1) * size), "Pixel-"
              + x + "," + y, null, rectProperties);
      x++;
      exec("incx");
      lang.nextStep();

      cursor.moveBy(null, size, 0, null, new TicksTiming(50));
      exec("incerror");
      lang.nextStep();

      exec("decide");
      error += delta;
      errorText.setText("error = " + vars.get("error"), null, null);
      lang.nextStep();

      if (error >= 0.5) {
        exec("incy");
        lang.nextStep();

        y++;
        cursor.moveBy(null, 0, size, null, new TicksTiming(50));
        exec("decerror");
        error -= 1;
        lang.nextStep();

        vars.set("error", String.valueOf(error));
        errorText.setText("error = " + vars.get("error"), null, null);
      }
    }

    sourceCode.hide();
    lang.nextStep();
    return lang.toString();
  }

  @Override
  public String getAlgorithmName() {
    return "Bresenham-Algorithmus";
  }

  @Override
  public String getAnimationAuthor() {
    return "Dominik Fischer";
  }

  @Override
  public String getAnnotatedSrc() {
    return "delta = destination.y / destination.x @label(\"initdelta\")\n"
        + "error = 0 @label(\"initerror\") @declare(\"double\", \"error\", \"0\")\n"
        + "(x, y) = (0, 0) @label(\"initpos\")\n"
        + "while (x <= destination.x): @label(\"while\")\n"
        + "  draw(position) @label(\"plot\")\n"
        + "  x++ @label(\"incx\")\n"
        + "  error += delta @label(\"incerror\")\n"
        + "  if x >= 0.5: @label(\"decide\") @eval(\"error\", \"error + delta\")\n"
        + "    y++ @label(\"incy\")\n"
        + "    error -= 1 @label(\"decerror\")\n";
  }

  @Override
  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  @Override
  public String getDescription() {
    return "Dieser Algorithmus rasterisiert eine Linie vom Nullpunkt zu "
        + "einem gegebenen Endpunkt. Dazu wird die Abweichung von der "
        + "Optimallinie in y-Richtung akkumuliert und bei zureichender "
        + "Höhe durch einen seitlichen Schritt wieder ausgeglichen.";
  }

  @Override
  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
  }

  @Override
  public String getName() {
    return "Linienrasterisierung nach Bresenham";
  }

  @Override
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

} // End of class Bresenham.