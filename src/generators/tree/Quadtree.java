package generators.tree;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.tree.helpers.QuadtreeHelper;
import algoanim.animalscript.AnimalScript;
import java.awt.Color;

public class Quadtree implements Generator {
  private AnimalScript lang;
  private Color        color;
  private int[][]      Coordinates;

  public void init() {
    lang = new AnimalScript("Quadtree[DE]", "Alex Krause, Markus Ermuth", 800,
        600);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    color = (Color) primitives.get("Highlight_Node");
    Coordinates = (int[][]) primitives.get("Coordinates");
    QuadtreeHelper quadtree = new QuadtreeHelper(lang, Coordinates, color,
        props);
    quadtree.buildQuadtree();
    lang.finalizeGeneration();
    return lang.toString();
  }

  public String getName() {
    return "Quadtree[DE]";
  }

  public String getAlgorithmName() {
    return "Quadtree";
  }

  public String getAnimationAuthor() {
    return "Alex Krause, Markus Ermuth";
  }

  public String getDescription() {
    return "Ein Quadtree ist in der Informatik eine spezielle Baum-Struktur, in der jeder innere Knoten genau vier Kinder hat. Das Wort Quadtree leitet sich von der Zahl der Kinder eines inneren Knotens ab (quad (vier) + tree (Baum) = Quadtree). Diese Struktur wird hauptsächlich zur Organisation zweidimensionaler Daten im Bereich der Computergrafik eingesetzt. Die Wurzel des Baumes repräsentiert dabei eine quadratische Fläche. Diese wird rekursiv in je vier gleich große Quadranten zerlegt bis die gewünschte Auflösung erreicht ist und die Rekursion in einem Blatt endet. Durch rekursive Anwendung dieser Zerteilung kann die vom Wurzelknoten repräsentierte Fläche beliebig fein aufgelöst werden. Für dreidimensionale Daten verwendet man gewöhnlich Octrees.Da ein Blatt unter Umständen eine verhältnismäßig große Fläche abdecken kann, ist die Datenstruktur relativ speichersparend und schnell nach einem Blatt, das einen bestimmten Punkt beinhaltet, zu durchsuchen.\n\n"
        + "Quelle: http://de.wikipedia.org/wiki/Quadtree 13.09.2012";
  }

  public String getCodeExample() {
    return "forall points{\n" + "    insert(point, root) \n" + "}\n" + "\n"
        + "insert(point, currentNode){\n" + "    if(currentNode is leaf){\n"
        + "        if(currentNode is empty)\n"
        + "            set point to currentNode\n" + "        else{\n"
        + "            split the Tree\n"
        + "            insert(point, currentNode)\n" + "        }\n"
        + "    } else {\n" + "        child = next quadrant for this point\n"
        + "        insert(point, child)\n" + "    }    \n" + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_TREE);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}