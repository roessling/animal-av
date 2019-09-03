package generators.sorting;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.sorting.helpers.TopSortHelp;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Graph;
import algoanim.primitives.generators.Language;

public class TopSort implements Generator {
  private AnimalScript lang;
  private Graph        graph;
  private Color        HighlightColorCode;
  private Color        HighlightColorArrays;
  private Color        HighlightColorGraph;

  public void init() {
    lang = new AnimalScript("TopSort Bottom Up [DE]",
        "Markus Ermuth, Alex Krause", 800, 600);
    lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    graph = (Graph) primitives.get("graph");
    HighlightColorCode = (Color) primitives.get("HighlightColorCode");
    HighlightColorArrays = (Color) primitives.get("HighlightColorArrays");
    HighlightColorGraph = (Color) primitives.get("HighlightColorGraph");
    TopSortHelp helper = new TopSortHelp(lang, graph, HighlightColorCode,
        HighlightColorArrays, HighlightColorGraph);
    try {
      helper.sort();
    } catch (IllegalDirectionException ex) {
      Logger.getLogger(TopSort.class.getName()).log(Level.SEVERE, null, ex);
    }
    // System.out.println(lang.toString());
    lang.finalizeGeneration();
    return lang.toString();
  }

  public String getName() {
    return "TopSort Bottom Up [DE]";
  }

  public String getAlgorithmName() {
    return "TopSort";
  }

  public String getAnimationAuthor() {
    return "Markus Ermuth, Alex Krause";
  }

  public String getDescription() {
    return "Das Topologische Sortieren ist ein Algorithmus, um in einem azyklischen Graphen eine Reihenfolge zu bestimmen."
        + "\n"
        + "Bei der von uns verwendeten Bottom-Up Variante wird daf&uuml;r eine Tiefensuche genutzt, um die Knoten von unten nach"
        + "\n"
        + "oben zu traversieren und auf dem R&uuml;ckweg Besuchsnummern zu vergeben. Das zuerst gefundene Blatt erhält dabei den höchsten"
        + "\n" + "Index zum Einsortieren.";
  }

  public String getCodeExample() {
    return "Gegeben: azyklischer Graph G = (V,E)" + "\n"
        + "Gesucht: topologische Sortierung TS[v] fuer alle v in V" + "\n"
        + "\n" + "markiere alle v in V als unvisited" + "\n" + "z = |V|" + "\n"
        + "FORALL v in V do" + "\n" + "    IF(d-(v) == 0) THEN topSort(v)"
        + "\n" + "\n" + "void topSort(Vertex v) {" + "\n" + "     visit(v)"
        + "\n" + "     FORALL Knoten w in V mit eingehender Kante (v,w) DO"
        + "\n" + "          IF (unvisited(w)) THEN topSort(w)" + "\n"
        + "    TS[z] = v" + "\n" + "    z--" + "\n" + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
  }

  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

}