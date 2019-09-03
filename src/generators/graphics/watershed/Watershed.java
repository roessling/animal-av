package generators.graphics.watershed;

import generators.framework.Generator;
import generators.framework.ValidatingGenerator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.graphics.helpers.Tools;
import generators.graphics.helpers.WSTAlgo;
import generators.graphics.helpers.WSTAnim;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

public class Watershed implements ValidatingGenerator {
    private Language lang;
    private boolean showDetailedSourcecode;
    private int[] array;
    private Color waterColor;

    public void init(){
        lang = new AnimalScript("Watershed", "Manuel Weiel, Lucas Rothamel", 800, 600);
        lang.setStepMode(true);
    }

    WSTAnim anim;
	TextProperties textProps = new TextProperties();
	TextProperties titleProps;
	ArrayProperties arrayProps = null;
	SourceCodeProperties scProps = new SourceCodeProperties();
	SourceCode sc;
	Coordinates topLeft = new Coordinates(500,300);
	Text maximum, maximumLabel, underwaterLabel, connectedComponentsLabel;
	Text title;
	IntArray underwaterAnim = null;
	boolean detailedSource;
	
	public void watershed(List<Integer> a, boolean detailedSource) {
		this.detailedSource = detailedSource;
		initProperties();
		
		if (!detailedSource)
			compactWatershed(a);
		else
			detailedWatershed(a);
	}

	private void compactWatershed(List<Integer> a) {
		showIntroSlides();
		
		anim = new WSTAnim(lang, topLeft, a, arrayProps, waterColor);
		lang.nextStep();
		int max = WSTAlgo.arrayMax(anim);
		//anim.setMax(max);
		
		maximumLabel = lang.newText(new Coordinates(topLeft.getX(), topLeft.getY()+90), "maximum:", "maxLabel", null, textProps);
		maximum = lang.newText(new Offset(200, -5, "maxLabel", AnimalScript.DIRECTION_W), max+"", "max", null, textProps);
		List<Integer> underwater = new ArrayList<Integer>();
		underwaterLabel = lang.newText( new Offset(0, 20, "maxLabel", AnimalScript.DIRECTION_SW),
				"underwater:", "underwater", null, textProps);
		List<List<Integer>> connectedComponents = new ArrayList<List<Integer>>();
		connectedComponentsLabel = lang.newText(new Offset(0, 20, underwaterLabel, AnimalScript.DIRECTION_W),
				"regions:", "connectedComponentsLabel", null, textProps);
		sc.highlight("max");
		lang.nextStep();
		sc.unhighlight("max");
		sc.highlight("border");
		initBorder();
		lang.nextStep();
		sc.unhighlight("border");
		
		while (anim.getWaterlevel() < max + 1)
		{
			sc.highlight("while");
			lang.nextStep();
			sc.highlight("waterlevel");
			increaseWaterLevel();
			lang.nextStep("waterlevel " + anim.getWaterlevel());
			sc.unhighlight("waterlevel");
			underwater = WSTAlgo.searchUnderwater(anim);
			updateUnderwater(underwater);
			sc.highlight("underwater");
			lang.nextStep();
			sc.unhighlight("underwater");
			sc.highlight("merge");
			List<Integer> mergePositions = WSTAlgo.searchMergePositions(anim, underwater);
			lang.nextStep();
			sc.unhighlight("merge");
			sc.highlight("foreach");
			lang.nextStep();
			for (Integer mp: mergePositions) {
				anim.setArrElement(mp, max + 1);
				underwater.remove(mp);
				sc.highlight("maxmerge");
				lang.nextStep();
			}
			sc.unhighlight("maxmerge");
			sc.unhighlight("foreach");
			//sc.highlight("updateunderwater");
			//underwater = WST_Algo.searchUnderwater(anim);
			//underwaterAnim = updateUnderwater(underwaterAnim, underwaterLabel,
			//		underwater);
			//lang.nextStep();
			//sc.unhighlight("updateunderwater");
			sc.highlight("cc");
			connectedComponents = WSTAlgo.searchConnectedComponents(underwater);
			updateCCAnim(connectedComponents);
			lang.nextStep();
			sc.unhighlight("cc");
		}
		
		
		anim.hide();
		sc.hide();
		underwaterLabel.hide();
		connectedComponentsLabel.hide();
		maximum.hide();
		maximumLabel.hide();
		for (IntArray ia: connectedComponentsAnim)
			ia.hide();
	    underwaterAnim.hide();
		
		showOutroSlide();
	}
	
	private void detailedWatershed(List<Integer> a) {
		showIntroSlides();
		
		anim = new WSTAnim(lang, topLeft, a, arrayProps, waterColor);
		lang.nextStep();
		maximumLabel = lang.newText(new Coordinates(topLeft.getX(), topLeft.getY()+90), "maximum:", "maxLabel", null, textProps);
		int max = arrayMax();
		
		List<Integer> underwater = new ArrayList<Integer>();
		
		underwaterLabel = lang.newText( new Offset(0, 20, "maxLabel", AnimalScript.DIRECTION_SW),
				"underwater:", "underwater", null, textProps);
		List<List<Integer>> connectedComponents = new ArrayList<List<Integer>>();
		connectedComponentsLabel = lang.newText(new Offset(0, 20, underwaterLabel, AnimalScript.DIRECTION_W),
				"regions:", "connectedComponentsLabel", null, textProps);
		lang.nextStep();
		initBorderDetailed();
		
		while (anim.getWaterlevel() < max + 1)
		{
			sc.highlight("while");
			lang.nextStep();
			sc.highlight("waterlevel");
			increaseWaterLevel();
			lang.nextStep("waterlevel " + anim.getWaterlevel());
			sc.unhighlight("waterlevel");
			underwater = searchUnderwater();
			lang.nextStep();
			doMergePositions(underwater);
			lang.nextStep();
			//sc.highlight("updateunderwater");
			//underwater = WST_Algo.searchUnderwater(anim);
			//underwaterAnim = updateUnderwater(underwaterAnim, underwaterLabel,
			//		underwater);
			//lang.nextStep();
			//sc.unhighlight("updateunderwater");
			connectedComponents = searchConnectedComponents(underwater);
			updateCCAnim(connectedComponents);
			lang.nextStep();
		}
		
		
		anim.hide();
		sc.hide();
		underwaterLabel.hide();
		connectedComponentsLabel.hide();
		maximum.hide();
		maximumLabel.hide();
		for (IntArray ia: connectedComponentsAnim)
			ia.hide();
		underwaterAnim.hide();
		
		showOutroSlide();
	}
	
	public int arrayMax()
	{
		sc.highlight("max1");
		int max = 0;
		maximum = lang.newText(new Offset(200, -5, "maxLabel", AnimalScript.DIRECTION_W), 0+"", "max", null, textProps);
		sc.unhighlight("max1");
		lang.nextStep();
		sc.highlight("max2");
		for (int i = 0; i < anim.getElementCount(); i++)
		{
			int tmp = anim.getArrElement(i);
			sc.highlight("max3");
			anim.getSA().highlightCell(i, null, null);
			lang.nextStep();
			if (tmp > max) {
				sc.highlight("max4");
				max = tmp;
				maximum.setText(max+"", null, null);
				lang.nextStep();
				sc.unhighlight("max4");
			}
			anim.getSA().unhighlightCell(i, null, null);
			sc.unhighlight("max3");
		}
		
		sc.unhighlight("max2");
		return max;
	}
	
	public List<Integer> searchUnderwater() {
		sc.highlight("underwater1");
		List<Integer> underwater = new ArrayList<Integer>();
		updateUnderwater(underwater);
		
		lang.nextStep();
		sc.unhighlight("underwater1");
		sc.highlight("underwater2");
		for (int i = 0; i < anim.getElementCount(); i++)
		{
			sc.highlight("underwater3");
			anim.getSA().highlightCell(i, null, null);
			if (anim.getArrElement(i) < anim.getWaterlevel())
			{
				sc.highlight("underwater4");
				underwater.add(i);
				updateUnderwater(underwater);
				lang.nextStep();
				sc.unhighlight("underwater4");
			}
			lang.nextStep();
			anim.getSA().unhighlightCell(i, null, null);
			sc.unhighlight("underwater3");
		}
		sc.unhighlight("underwater2");
		return underwater;
	}
	
	public void doMergePositions(List<Integer> underwater) {
		int n = anim.getWaterlevel();
		sc.highlight("merge1");
		for (int i = 0; i < underwater.size(); i++)
		{
			Integer elem = underwater.get(i);
			sc.highlight("merge2");
			underwaterAnim.highlightCell(i, null, null);
			lang.nextStep();
			if (anim.getArrElement(elem) == n - 1)
			{
				sc.highlight("merge3");
				sc.highlight("merge4");
				lang.nextStep();
				if (anim.getArrElement(elem-1) < n - 1 && anim.getArrElement(elem+1) < n -1)
				{
					sc.highlight("merge5");
					anim.setArrElement(elem, anim.getMax() + 1);
					underwater.remove(elem);
					updateUnderwater(underwater);
					i--;
					lang.nextStep();
					sc.unhighlight("merge5");
				}
				sc.unhighlight("merge3");
				sc.unhighlight("merge4");
			}
			sc.unhighlight("merge2");
			underwaterAnim.unhighlightCell(i, null, null);
			lang.nextStep();
		}
		lang.nextStep();
		sc.unhighlight("merge1");
	}
	
	public List<List<Integer>> searchConnectedComponents(
			List<Integer> arr) {
		sc.highlight("regions1");
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		List<Integer> first = new ArrayList<Integer>();
		updateCCAnim(result);
		lang.nextStep();
		sc.unhighlight("regions1");
		sc.highlight("regions2");
		result.add(first);
		updateCCAnim(result);
		lang.nextStep();
		sc.unhighlight("regions2");
		sc.highlight("regions3");
		for (int i = 0; i < arr.size(); i++)
		{
			sc.highlight("regions4");
			result.get(result.size()-1).add(arr.get(i));
			updateCCAnim(result);
			lang.nextStep();
			sc.unhighlight("regions4");
			sc.highlight("regions5");
			sc.highlight("regions6");
			if (i < arr.size() - 1)
			{
				if (arr.get(i + 1) != arr.get(i) + 1)
				{
					sc.highlight("regions7");
					result.add(new ArrayList<Integer>());
					updateCCAnim(result);
					lang.nextStep();
					sc.unhighlight("regions7");
				}
			}
			lang.nextStep();
			sc.unhighlight("regions5");
			sc.unhighlight("regions6");
		}
		lang.nextStep();
		sc.unhighlight("regions3");
		return result;
	}

	private void showIntroSlides() {
		/*TextProperties props = new TextProperties();
		props.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				Font.SANS_SERIF, Font.BOLD, 42));*/
		title = lang.newText(new Coordinates(50, 50), "Watershed Algortihm", "title", null, titleProps);
		SourceCode intro = lang.newSourceCode(new Offset(0, 50, title, AnimalScript.DIRECTION_SW), "intro", null, scProps);
		intro.addCodeLine("The watershed algorithm by flooding has been introduced in 1979 by S. Beucher and C. Lantuéjoul.", null, 0, null);
		intro.addCodeLine("The idea behind the algorithm is to find segmentations of a grayscale image.", null, 0, null);
		intro.addCodeLine("A grey-level image may be seen as a topographic relief,", null, 0, null);
		intro.addCodeLine("where the grey level of a pixel is interpreted as its altitude in the relief.", null, 0, null);
		intro.addCodeLine("A drop of water falling on a topographic relief flows along a path to finally reach a local minimum.", null, 0, null);
		intro.addCodeLine("Intuitively, the watershed of a relief correspond to the limits of the adjacent catchment basins of the drops of water.", null, 0, null);
		intro.addCodeLine("Each catchment basin then results in a seperate segment of the image.", null, 0, null);
		intro.addCodeLine("", null, 0, null);
		intro.addCodeLine("Segementation algorithmns are often used in medical imageing to detect certain regions in e.g. a MRI or CT image.", null, 0, null);
		intro.addCodeLine("It is important for this segmentations to be excact and reproducable.", null, 0, null);
		intro.addCodeLine("The watershed algorithm is a very basic algortihm to achieve segmentation of a grayscale image.", null, 0, null);
		if (!detailedSource)
		{
			intro.addCodeLine("", null, 0, null);
			intro.addCodeLine("To keep the algorithm compact and better understandable, the actual animation only contains top level steps,", null, 0, null);
			intro.addCodeLine("that themselves iterate over the data structure.", null, 0, null);
			intro.addCodeLine("On the following slides these steps are outlined in detail.", null, 0, null);
		}
		lang.nextStep("Beginning");
		intro.hide();
		
		if (!detailedSource)
		{
			sc = initializePseudoCode();
			sc.highlight("max");
			SourceCode maxPseudo = lang.newSourceCode(new Offset(100, 0, sc, AnimalScript.DIRECTION_NE), "maxPseudo", null, scProps);
			maxPseudo.addCodeLine("max = 0", null, 0, null);
			maxPseudo.addCodeLine("foreach elem in array", null, 0, null);
			maxPseudo.addCodeLine("if elem > max then", null, 1, null);
			maxPseudo.addCodeLine("max = elem", null, 2, null);
			maxPseudo.addCodeLine("return max", null, 0, null);
			lang.nextStep();
			maxPseudo.hide();
			sc.unhighlight("max");
			
			SourceCode borderPseudo = lang.newSourceCode(new Offset(100, 0, sc, AnimalScript.DIRECTION_NE), "borderPseudo", null, scProps);
			sc.highlight("border");
			borderPseudo.addCodeLine("array[1] = max + 1", null, 0, null);
			borderPseudo.addCodeLine("array[length of array] = max + 1", null, 0, null);
			lang.nextStep();
			borderPseudo.hide();
			sc.unhighlight("border");
			
			SourceCode underwaterPseudo = lang.newSourceCode(new Offset(100, 0, sc, AnimalScript.DIRECTION_NE), "underwaterPseudo", null, scProps);
			sc.highlight("underwater");
			underwaterPseudo.addCodeLine("foreach i in 1..(length of array)", null, 0, null);
			underwaterPseudo.addCodeLine("if array(i) < waterlevel then", null, 1, null);
			underwaterPseudo.addCodeLine("add i to result", null, 2, null);
			underwaterPseudo.addCodeLine("return result", null, 0, null);
			lang.nextStep();
			underwaterPseudo.hide();
			sc.unhighlight("underwater");
			
			SourceCode mergePseudo = lang.newSourceCode(new Offset(100, 0, sc, AnimalScript.DIRECTION_NE), "mergePseudo", null, scProps);
			sc.highlight("merge");
			mergePseudo.addCodeLine("foreach index in underwater", null, 0, null);
			mergePseudo.addCodeLine("if array(index) == waterlevel - 1 then //recently flooded", null, 1, null);
			mergePseudo.addCodeLine("if array(index-1) < waterlevel - 1 ", null, 2, null);
			mergePseudo.addCodeLine("and array(index+1) < waterlevel - 1 then //neighbors are also flooded", null, 2, null);
			mergePseudo.addCodeLine("add index to result", null, 3, null);
			mergePseudo.addCodeLine("return result", null, 0, null);
			lang.nextStep();
			mergePseudo.hide();
			sc.unhighlight("merge");
			
			SourceCode ccPseudo = lang.newSourceCode(new Offset(100, 0, sc, AnimalScript.DIRECTION_NE), "ccPseudo", null, scProps);
			sc.highlight("cc");
			ccPseudo.addCodeLine("add empty list to result", null, 0, null);
			ccPseudo.addCodeLine("foreach i in 1..(length of underwater)", null, 0, null);
			ccPseudo.addCodeLine("add array(i) to the last list in result", null, 1, null);
			ccPseudo.addCodeLine("if i < length of underwater", null, 1, null);
			ccPseudo.addCodeLine("and array(i + 1) != array(i) + 1 then", null, 1, null);
			ccPseudo.addCodeLine("add empty list to result", null, 2, null);
			ccPseudo.addCodeLine("return result", null, 0, null);
			ccPseudo.addCodeLine("", null, 0, null);
			ccPseudo.addCodeLine("// Note that it is sufficient to execute", null, 0, null);
			ccPseudo.addCodeLine("// this step only once at the end.", null, 0, null);
			ccPseudo.addCodeLine("// For easier understanding in this animation", null, 0, null);
			ccPseudo.addCodeLine("// this step is executed each iteration of the loop.", null, 0, null);
			lang.nextStep();
			ccPseudo.hide();
			sc.unhighlight("cc");
		}
		else
		{
			sc = initializeDetailedPseudoCode();
		}
	}

	private void showOutroSlide() {
		SourceCode intro = lang.newSourceCode(new Offset(0, 50, title, AnimalScript.DIRECTION_SW), "intro", null, scProps);
		intro.addCodeLine("This example shows how easy it is to split a 2D-grayscale image into regions", null, 0, null);
		intro.addCodeLine("using the watershed algorithm.", null, 0, null);
		intro.addCodeLine("The computational complexity of this example is in O(n*(m + 1))", null, 0, null);
		intro.addCodeLine("with n beeing the number of pixels ("+anim.getElementCount() + ") and m the maximum grayscale value (" + (anim.getMax())+").", null, 0, null);
		lang.nextStep("Outro");
	}

	private void updateUnderwater(List<Integer> newUnderwater) {
		if (underwaterAnim == null) {
			if (newUnderwater.size() > 0)
				underwaterAnim = initializeIntArray(newUnderwater, new Offset(200, -5, underwaterLabel, AnimalScript.DIRECTION_W));
		}
		else {
			underwaterAnim.hide();
			
			if (newUnderwater.size() > 0) {
				IntArray underwaterAnimtmp = initializeIntArray(newUnderwater, new Offset(200, -5, underwaterLabel, AnimalScript.DIRECTION_W));
				
				underwaterAnim.exchange(underwaterAnimtmp);
			
				underwaterAnim = underwaterAnimtmp;
			}
			else {
				underwaterAnim = null;
			}
		}
	}

	int cccounter = 0;
	List<IntArray> connectedComponentsAnim = new ArrayList<IntArray>();

//  private void updateCCAnimOld(List<List<Integer>> connectedComponents) {
//    int i = 0;
//    IntArray prev = null;
//    List<IntArray> oldConnectedComponentsAnim = (ArrayList<IntArray>) ((ArrayList<IntArray>) connectedComponentsAnim)
//        .clone();
//    for (List<Integer> cc : connectedComponents) {
//      Node n;
//      if (prev == null)
//        n = new Offset(200, -5, "connectedComponentsLabel",
//            AnimalScript.DIRECTION_W);
//      else
//        n = new Offset(0, 20, prev, AnimalScript.DIRECTION_W);
//      IntArray tmp = lang.newIntArray(n, Tools.listToIntArray(cc), "cc"
//          + cccounter++, null, arrayProps);
//      if (oldConnectedComponentsAnim.size() > i) {
//        oldConnectedComponentsAnim.get(i).hide();
//        oldConnectedComponentsAnim.get(i).exchange(tmp);
//        connectedComponentsAnim.set(i++, tmp);
//      } else
//        connectedComponentsAnim.add(tmp);
//      prev = tmp;
//    }
//  }
	
	private void updateCCAnim(List<List<Integer>> connectedComponents) {
		IntArray prev = null;

		for (IntArray ia: connectedComponentsAnim)
		{
			ia.hide();
		}
		connectedComponentsAnim = new ArrayList<IntArray>();
		
		for (List<Integer> cc: connectedComponents)
		{
			if (cc.size() > 0)
			{
				Node n;
				if (prev == null)
					n = new Offset(200, -5, "connectedComponentsLabel", AnimalScript.DIRECTION_W);
				else
					n = new Offset(0, 20, prev, AnimalScript.DIRECTION_W);
				IntArray tmp = lang.newIntArray(n, Tools.listToIntArray(cc), "cc"+cccounter++, null, arrayProps);
				connectedComponentsAnim.add(tmp);
				prev = tmp;
			}
		}
	}
	
	private void initProperties() {
		//scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
		//scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", 
		//							    Font.PLAIN, 12));
		    
		//scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, 
		//	    Color.RED);   
		//scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		
		Font textFont = (Font)textProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
		textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, textFont.deriveFont(16.0f));
		
		//titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
		//		((Font)titleProps.get(AnimationPropertiesKeys.FONT_PROPERTY)).getFontName(), Font.BOLD, 42));
		Font titleFont = (Font)titleProps.get(AnimationPropertiesKeys.FONT_PROPERTY);
		titleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, titleFont.deriveFont(42.0f));
		
		/*arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.YELLOW);
		arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 12));*/
	}

	private void increaseWaterLevel() {
		anim.setWaterlevel(anim.getWaterlevel()+1);
	}

	private void initBorder() {
		anim.setArrElement(0, anim.getMax()+1);
		anim.setArrElement(anim.getElementCount()-1, anim.getMax()+1);
		anim.updateAnim();
	}
	
	private void initBorderDetailed() {
		sc.highlight("border1");
		anim.setArrElement(0, anim.getMax()+1);
		lang.nextStep();
		sc.unhighlight("border1");
		sc.highlight("border2");
		anim.setArrElement(anim.getElementCount()-1, anim.getMax()+1);
		anim.updateAnim();
		lang.nextStep();
		sc.unhighlight("border2");
	}
	
	private IntArray initializeIntArray(List<Integer> intArr, Node c) {
		
		/*arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,
				Color.BLACK);
		arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,
				Color.RED);
		arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
				Color.YELLOW);
		arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
				"Monospaced", Font.PLAIN, 12));*/
		return lang.newIntArray(c, Tools.listToIntArray(intArr),
				"intArray", null, arrayProps);
	}
	
	private SourceCode initializePseudoCode() {
		// Create SourceCode: coordinates, name, display options, 
		// default properties
		    
		// first, set the visual properties for the source code
		
		    
		// now, create the source code entity
		SourceCode sc = lang.newSourceCode(new Coordinates(40, 140), "pseudoCode",
						   null, scProps);
		    
		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy
		sc.addCodeLine("Find maximum level", "max", 0, null);
		sc.addCodeLine("Set border pixels to max + 1", "border", 0, null);
		sc.addCodeLine("while (waterlevel < max + 1)", "while", 0, null);
		sc.addCodeLine("{", null, 0, null);
		sc.addCodeLine("increase water level", "waterlevel", 1, null);
		sc.addCodeLine("search underwater pixels", "underwater", 1, null);
		sc.addCodeLine("search positions, where 2 regions did merge", "merge", 1, null);
		sc.addCodeLine("foreach merge position", "foreach", 1, null);
		sc.addCodeLine("{", null, 1, null);
		sc.addCodeLine("set pixel to max + 1", "maxmerge", 2, null);
		sc.addCodeLine("}", null, 1, null);
		//sc.addCodeLine("update underwater pixels", "updateunderwater", 1, null);
		sc.addCodeLine("determine regions", "cc", 1, null);
		sc.addCodeLine("}", null, 0, null);
		
		return sc;
	}
	
	private SourceCode initializeDetailedPseudoCode() {
		// Create SourceCode: coordinates, name, display options, 
		// default properties
		    
		// first, set the visual properties for the source code
		
		    
		// now, create the source code entity
		SourceCode sc = lang.newSourceCode(new Coordinates(40, 60), "pseudoCode",
						   null, scProps);
		    
		// Add the lines to the SourceCode object.
		// Line, name, indentation, display dealy
		sc.addCodeLine("max = 0", "max1", 0, null);
		sc.addCodeLine("foreach elem in array", "max2", 0, null);
		sc.addCodeLine("if elem > max then", "max3", 1, null);
		sc.addCodeLine("max = elem", "max4", 2, null);
		
		sc.addCodeLine("array[1] = max + 1", "border1", 0, null);
		sc.addCodeLine("array[length of array] = max + 1", "border2", 0, null);		

		sc.addCodeLine("while (waterlevel < max + 1)", "while", 0, null);
		sc.addCodeLine("{", null, 0, null);
		sc.addCodeLine("increase water level", "waterlevel", 1, null);
		
		sc.addCodeLine("empty underwater", "underwater1", 1, null);
		sc.addCodeLine("foreach i in 1..(length of array)", "underwater2", 1, null);
		sc.addCodeLine("if array(i) < waterlevel then", "underwater3", 2, null);
		sc.addCodeLine("add i to underwater", "underwater4", 3, null);
		
		sc.addCodeLine("foreach index in underwater", "merge1", 1, null);
		sc.addCodeLine("if array(index) == waterlevel - 1 then", "merge2", 2, null);
		sc.addCodeLine("if array(index-1) < waterlevel - 1 ", "merge3", 3, null);
		sc.addCodeLine("and array(index+1) < waterlevel - 1 then", "merge4", 3, null);
		sc.addCodeLine("set pixel to max + 1", "merge5", 4, null);
		//sc.addCodeLine("update underwater pixels", "updateunderwater", 1, null);
		
		sc.addCodeLine("clear result", "regions1", 1, null);
		sc.addCodeLine("add empty list to result", "regions2", 1, null);
		sc.addCodeLine("foreach i in 1..(length of underwater)", "regions3", 1, null);
		sc.addCodeLine("add array(i) to the last list in result", "regions4", 2, null);
		sc.addCodeLine("if i < length of underwater", "regions5", 2, null);
		sc.addCodeLine("and array(i + 1) != array(i) + 1 then", "regions6", 2, null);
		sc.addCodeLine("add empty list to result", "regions7", 3, null);
		
		sc.addCodeLine("}", null, 0, null);
		
		return sc;
	}
    
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        scProps = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        arrayProps = (ArrayProperties)props.getPropertiesByName("arrayProperties");
        showDetailedSourcecode = (Boolean)primitives.get("showDetailedSourcecode");
        array = (int[])primitives.get("array");
        waterColor = (Color)primitives.get("watercolor");
        textProps = (TextProperties)props.getPropertiesByName("text");
        titleProps = (TextProperties)props.getPropertiesByName("title");
        
        List<Integer> lst = new ArrayList<Integer>();
		for (int i = 0; i < array.length; i++)
			lst.add(array[i]);
		watershed(lst, showDetailedSourcecode);
        
        return lang.toString();
    }

    public String getName() {
        return "Watershed";
    }

    public String getAlgorithmName() {
        return "Watershed Algorithm";
    }

    public String getAnimationAuthor() {
        return "Manuel Weiel, Lucas Rothamel";
    }

    public String getDescription(){
        return "The watershed algorithm by flooding has been introduced in 1979 by S. Beucher and C. Lantuejoul."
 +"\n"
 +"The idea behind the algorithm is to find segmentations of a grayscale image."
 +"\n"
 +"A grey-level image may be seen as a topographic relief,"
 +"\n"
 +"where the grey level of a pixel is interpreted as its altitude in the relief."
 +"\n"
 +"A drop of water falling on a topographic relief flows along a path to finally reach a local minimum."
 +"\n"
 +"Intuitively, the watershed of a relief correspond to the limits of the adjacent catchment basins of the drops of water.\""
 +"\n"
 +"Each catchment basin then results in a seperate segment of the image."
 +"\n"
 +"\n"
 +"Segementation algorithmns are often used in medical imageing to detect certain regions in e.g. a MRI or CT image."
 +"\n"
 +"It is important for this segmentations to be excact and reproducable."
 +"\n"
 +"The watershed algorithm is a very basic algortihm to achieve segmentation of a grayscale image."
 +"\n"
 +"		";
    }

    public String getCodeExample(){
        return "Find maximum level"
 +"\n"
 +"Set border pixels to max + 1"
 +"\n"
 +"while (waterlevel < max + 1)"
 +"\n"
 +"{"
 +"\n"
 +"	increase water level"
 +"\n"
 +"	search underwater pixels"
 +"\n"
 +"	search positions, where 2 regions did merge"
 +"\n"
 +"	foreach merge position"
 +"\n"
 +"	{"
 +"\n"
 +"		set pixel to max + 1"
 +"\n"
 +"	}"
 +"\n"
 +"	determine regions"
 +"\n"
 +"}";
    }

    public String getFileExtension(){
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
    
    public boolean validateInput(AnimationPropertiesContainer props,Hashtable<String, Object> primitives)
    {
    	scProps = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
        arrayProps = (ArrayProperties)props.getPropertiesByName("arrayProperties");
        showDetailedSourcecode = (Boolean)primitives.get("showDetailedSourcecode");
        array = (int[])primitives.get("array");
        
        if (array.length < 4)
        	throw new IllegalArgumentException("The array has to have at least 4 elements.");
        
        int i = 0;
        for (int a: array)
        {
        	if (a < 0)
        		throw new IllegalArgumentException("The element at index " + i + " of value "+ a +" has to be positive.");
        	if (a >= 1024)
        		throw new IllegalArgumentException("The element at index " + i + " of value " + a + " has to be smaller than 1024.");
        	i++;
        }
    	
    	return true;
    }

}