package generators.helpers.tsigaridas;

import generators.tree.HeapTree;
import algoanim.animalscript.AnimalScript;
import algoanim.primitives.generators.Language;
import algoanim.properties.ArrayProperties;

public class MainMethod_Heap {

  /**
   * @param args
   */
  public static void main(String[] args) {
    Language l = new AnimalScript("Binary Search Tree Algorithm",
        "Ioannis Tsigaridas", 1024, 768);

    HeapTree t = new HeapTree();

    // 2 9 56 77 12 13 96 17 56 44
    // 15, 24, 3, 8, 2, 0

    // int tree []= {2, 9, 57, 77, 12, 13, 96, 17, 56, 44};
    int tree[] = { 2, 9, 56, 77, 12, 13, 96, 17, 56, 44 };
    // int[] tree ={1,14, 6, 3, 2,4, 5};
    // int tree []= {540, 398, 7int20, 45, 47};
    // int tree []= {50, 120, 70, 80, 60, 105, 30};

    // int tree []= {50, 172, 70, 80, 600, 105, 180, 500};
    // int tree []= {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
    // int tree []= { 2, 9, 56, 77, 12, 13, 96, 17, 56, 44};
    t.heapSort(tree, new ArrayProperties());

    System.out.println(l);
  }

}
