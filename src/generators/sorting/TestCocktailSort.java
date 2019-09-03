package generators.sorting;

import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;

import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;

public class TestCocktailSort {

	private static Hashtable<String, Object> hs;

	public static int[] cocktailSort(int[] array) {
		Boolean swapped;
		int begin = -1;
		int end = array.length - 2;
		while (true) {
			swapped = false;
			begin++;
			for (int i = begin; i <= end; i++) {
				if (array[i] > array[i + 1]) {
					int safe = array[i];
					array[i] = array[i + 1];
					array[i + 1] = safe;
					// swap(array, i, i+1);
					swapped = true;
				}
			}
			if (swapped == false) {
				break;
			}
			swapped = false;
			end--;
			for (int i = end; i >= begin; i--) {
				if (array[i] > array[i + 1]) {
					int safe = array[i];
					array[i] = array[i + 1];
					array[i + 1] = safe;
					// swap(array, i, i+1);
					swapped = true;
				}
			}
			if (swapped == false) {
				break;
			}

		}
		return array;

	};

	public static void main(String[] args) {
		//int[] array = { 7, 4, 9, 3, 8, 3, 1, 5 };
		//int[] array = { 7, 42, 9, 3, 18, 3, 6, 4, 5,1 };
		int[] array = { 55, 4, 93, 234, 7, 5, 544, 2, 1,3, 238, 1, 35, 6, 77,
				20, 6 };
		// System.out.println(Arrays.toString(array));
		// array = cocktailSort(array);
		// System.out.println(Arrays.toString(array));

		ArrayProperties arrayProp = new ArrayProperties();
		arrayProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProp.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProp.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);   
		arrayProp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, 
			       Color.BLACK);
		arrayProp.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, 
			       Color.WHITE);
		arrayProp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, 
				new Color(58,181,36));
		
		SourceCodeProperties sourceCodeProp = new SourceCodeProperties();
		sourceCodeProp.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		sourceCodeProp.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 
									    Font.PLAIN, 12));
		    
		sourceCodeProp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, 
			    Color.red);   
		sourceCodeProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		CocktailSortDutschka cs = new CocktailSortDutschka();
		cs.init();
		
		arrayProp.setName("arrayProp");;
		sourceCodeProp.setName("sourceCodeProp");
		
		AnimationPropertiesContainer props = new AnimationPropertiesContainer();
		props.add(arrayProp);
		props.add(sourceCodeProp);
		
		hs = new Hashtable<String, Object>();
		hs.put("intArray", array);
		System.out.println(cs.generate(props, hs));
	}

}
