package generators.network;

import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;

import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;

public class TestBerkeley {

	private static Hashtable<String, Object> ht;

	public static void main(String[] args) {
		//int[] array = { 7, 4, 9, 3, 8, 3, 1, 5 };
		//int[] array = { 7, 42, 9, 3, 18, 3, 6, 4, 5,1 };
		String[] array = { "15:23:56:174","15:23:56:231","15:23:56:120","15:23:56:502","15:23:56:232", "15:23:55:986", "15:23:56:341", "15:23:56:103" };
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
		
		Berkeley b = new Berkeley();
		b.init();
		
		arrayProp.setName("arrayProp");;
		sourceCodeProp.setName("sourceCodeProp");
		
		AnimationPropertiesContainer props = new AnimationPropertiesContainer();
		props.add(arrayProp);
		props.add(sourceCodeProp);
		
		ht = new Hashtable<String, Object>();
		ht.put("stringArray", array);
		ht.put("maxDelta", 100);
		ht.put("negError", -16);
		ht.put("posError", 16);
		ht.put("iterations", 3);
		System.out.println(b.generate(props, ht));
	}

}
