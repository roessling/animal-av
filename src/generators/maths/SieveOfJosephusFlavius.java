/*
 * SieveOfJosephusFlavius.java
 * Christoph Niese, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.maths;

import algoanim.primitives.ArrayMarker;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.primitives.ArrayPrimitive;
import algoanim.properties.ArrayProperties;
import algoanim.primitives.generators.AnimationType;
import algoanim.primitives.generators.Language;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import generators.framework.ValidatingGenerator;
import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

public class SieveOfJosephusFlavius implements ValidatingGenerator
{
	public SourceCode createSourceCode()
	{
	    SourceCode sourceCode = lang.newSourceCode(new Coordinates(20, 120), "sourceCode", null, sourceCodeProperties);
	    
	    sourceCode.addCodeLine("LinkedList<Integer> sieveOfJosephusFlavius(int n)", null, 0, null);
	    sourceCode.addCodeLine("{", null, 0, null);
	    sourceCode.addCodeLine("LinkedList<Integer> numbers = createSequence(n);", null, 1, null);
	    sourceCode.addCodeLine("numbers = eliminateEvenNumbers(numbers);", null, 1, null);
	    sourceCode.addCodeLine("", null, 1, null);
	    sourceCode.addCodeLine("for (int i = 1; i < numbers.size(); i++)", null, 1, null);
	    sourceCode.addCodeLine("{", null, 1, null);
	    sourceCode.addCodeLine("int value = numbers.get(i);", null, 2, null);
	    sourceCode.addCodeLine("", null, 2, null);
	    sourceCode.addCodeLine("for (int j = value - 1; j < numbers.size(); j += value)", null, 2, null);
	    sourceCode.addCodeLine("{", null, 2, null);
	    sourceCode.addCodeLine("array.set(j, -1);", null, 3, null);
	    sourceCode.addCodeLine("}", null, 2, null);
	    sourceCode.addCodeLine("", null, 2, null);
	    sourceCode.addCodeLine("cleanupNumbers(numbers, -1);", null, 2, null);
	    sourceCode.addCodeLine("}", null, 1, null);
	    sourceCode.addCodeLine("", null, 1, null);
	    sourceCode.addCodeLine("return numbers;", null, 1, null);
	    sourceCode.addCodeLine("}", null, 0, null);
	    
	    return sourceCode;
	}
	
	private int[] createNumberSequence(int length)
	{
		int[] sequence = new int[length];
		for (int i = 0; i < length; i++)
		{
			sequence[i] = i + 1;
		}
		return sequence;
	}
	
	private LinkedList<Integer> createOddNumberSequence(int length)
	{
		LinkedList<Integer> sequence = new LinkedList<Integer>();
		for (int i = 0; i < length; i += 2)
		{
			sequence.add(i + 1);
		}
		return sequence;
	}
	
	private IntArray createArray()
	{
	    return lang.newIntArray(
	    	new Coordinates(620, 140),
	    	createNumberSequence(n),
	    	"intArray", null, arrayProperties);
	}
	
	private ArrayMarker setupArrayMarker(ArrayPrimitive array)
	{
	    return lang.newArrayMarker(array, 0, "i", null, arrayMarkerProperties);
	}
	
	private void markAsLucky(IntArray a, int index)
	{
		a.setHighlightTextColor(index, elementColor, null, null);
		a.setHighlightFillColor(index, luckyColor, null, null);
		a.highlightElem(index, null, null);
		a.highlightCell(index, null, null);
	}
	
	private void markAsSievedOut(IntArray a, int index)
	{
		a.setHighlightTextColor(index, sievedColor, null, null);
		a.setHighlightFillColor(index, sievedColor, null, null);
		a.highlightElem(index, null, null);
		a.highlightCell(index, null, null);
	}
		
	private void cleanupNumbers(LinkedList<Integer> list)
	{
		list.removeIf(new Predicate<Integer>()
		{  
			public boolean test(Integer i)
			{
				return i.equals(-1);
			}
		});
	}
	
	private void createFrame()
	{
		lang.newRect(new Coordinates(10, 10), new Coordinates(1600, 60), "FrameTitleRect", null);
    	lang.newText(new Coordinates(20, 38), getName(), "FrameTitleText", null)
    	.setFont(new Font("SansSerif", Font.BOLD, 42), null, null);
    	
    	lang.newRect(new Coordinates(10, 70), new Coordinates(600, 800), "FrameLeftRect", null);
    	lang.newRect(new Coordinates(610, 70), new Coordinates(1600, 800), "FrameRightRect", null);
	}
		
	private void createDescription()
	{
		ShowMultilineTextForOneStep(getDescription(), new Coordinates(20, 80), 100, null);
	}
	
	public String createScript()
	{
		createFrame();
		createDescription();
								
		SourceCode sourceCode = createSourceCode();
		IntArray visualizationArray = createArray();
		
		lang.nextStep("Daten");
				
	    ArrayMarker arrayMarker = setupArrayMarker(visualizationArray);	    
		markAsLucky(visualizationArray, 0);
		Text infoText1 = lang.newText(new Coordinates(20, 80), "Es wird eine Sequenz von Zahlen von 1 bis " + n + " erstellt", "infotext1", null);
		infoText1.setFont(new Font("SansSerif", Font.BOLD, 18), null, null);
		Text infoText2 = lang.newText(new Coordinates(20, 100), "Die Zahl 1 ist per Definition 'glücklich'", "infotext2", null);
		infoText2.setFont(new Font("SansSerif", Font.BOLD, 18), null, null);
		
		Variables v = lang.newVariables();		
		v.declare("string", "numbers");
		v.set("numbers", Arrays.toString(createNumberSequence(n)));
		
		sourceCode.highlight(2);
		lang.nextStep("Initialisierung");
		sourceCode.unhighlight(2);
		
		LinkedList<Integer> workingArray = createOddNumberSequence(n);
		v.set("numbers", Arrays.toString(workingArray.toArray()));
		for (int i = 1; i < visualizationArray.getLength(); i += 2)
		{
			markAsSievedOut(visualizationArray, i);
		}
		infoText1.setText("Alle geraden Zahlen werden entfernt", null, null);
		infoText2.setText("", null, null);
		sourceCode.highlight(3);
		lang.nextStep("Gerade Zahlen entfernen");
		sourceCode.unhighlight(3);
		
		v.declare("int", "i");
		
		for (int i = 1; i < workingArray.size(); i++)
		{
			v.set("i", i + "");
			
			int value = workingArray.get(i);
			
			arrayMarker.move(value - 1, null, null);
			infoText1.setText("Die nächste Zahl ist die " + value, null, null);
			markAsLucky(visualizationArray, value - 1);
			sourceCode.highlight(7);
			lang.nextStep(i + ". Iteration");
			sourceCode.unhighlight(7);
			
			for (int j = value - 1; j < workingArray.size(); j += value)
			{
				visualizationArray.highlightCell(workingArray.get(j) - 1, null, null);				
			}
			infoText1.setText("Jede " + value + ". übrige Zahl wird markiert...", null, null);
			infoText2.setText("(ab dem Beginn des gesamten Arrays)", null, null);
			sourceCode.highlight(9);
			sourceCode.highlight(10);
			sourceCode.highlight(11);
			sourceCode.highlight(12);
			lang.nextStep();
			sourceCode.unhighlight(9);
			sourceCode.unhighlight(10);
			sourceCode.unhighlight(11);
			sourceCode.unhighlight(12);
			
			for (int j = value - 1; j < workingArray.size(); j += value)
			{
				markAsSievedOut(visualizationArray, workingArray.get(j) - 1);
				workingArray.set(j, -1);
			}
			
			cleanupNumbers(workingArray);
			
			infoText1.setText("...und entfernt", null, null);
			infoText2.setText("", null, null);
			sourceCode.highlight(14);
			lang.nextStep();
			sourceCode.unhighlight(14);
		}
		
		arrayMarker.hide();
		sourceCode.highlight(17);
		infoText1.setText("FERTIG! Die 'glücklichen' Zahlen sind", null, null);
		infoText2.setText(Arrays.toString(workingArray.toArray()), null, null);
		lang.nextStep("Fazit");
		return lang.toString();
	}
	
	//// GENERATOR /////////////////////////////////////////////////////////////
	
	private Language lang;
	
	private int n;
	
	private SourceCodeProperties sourceCodeProperties;
	private ArrayProperties arrayProperties;
    private ArrayMarkerProperties arrayMarkerProperties;
    
    private Color elementColor;
	private Color luckyColor;
	private Color sievedColor; 
    	
	public SieveOfJosephusFlavius()
	{
		init();
	}
	
    public void init()
    {
    	lang = Language.getLanguageInstance(
    			AnimationType.ANIMALSCRIPT,
    			"Sieb des Josephus Flavius",
    			"Christoph Niese",
    			1600, 800);
    	lang.setStepMode(true);
    }
	
	public boolean validateInput(AnimationPropertiesContainer properties, Hashtable<String, Object> primitives)
		throws IllegalArgumentException
	{
		return (int)primitives.get("n") > 0;
	}
	
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives)
	{
		n = (int)primitives.get("n");
		
		sourceCodeProperties = (SourceCodeProperties)props.getPropertiesByName("sourceCode");
		arrayProperties = (ArrayProperties)props.getPropertiesByName("array");
        arrayMarkerProperties = (ArrayMarkerProperties)props.getPropertiesByName("arrayMarker");
        
        elementColor = (Color)arrayProperties.get(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY);
        luckyColor = (Color)arrayProperties.get(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY);
		sievedColor = new Color(255 - luckyColor.getRed(), 255 - luckyColor.getGreen(), 255 - luckyColor.getBlue());
        
        return createScript();
	}
		
	public String getName()
	{
		return "Sieb des Josephus Flavius";
	}
		
	public String getAlgorithmName()
	{
		return "Sieb des Josephus Flavius";
	}
	
	public String getAnimationAuthor()
	{
		return "Christoph Niese";
	}
	
	public String getDescription()
	{
		return
			"Das Sieb des Josephus Flavius siebt ein Array von natürlichen Zahlen 1 bis n so, " +
			"dass am Ende nur noch die sogenannten 'glücklichen' Zahlen darin enthalten sind. " +
			"Diese werden folgendermaßen bestimmt: Alle geraden Zahlen werden entfernt und die 1 per " +
			"Definition als 'glücklich' deklariert. Die nächste noch vorhandene Zahl ist die 2. " +
			"Es wird jetzt jede 2. Zahl entfernt. Die nächste dann noch verbleibende Zahl ist die 3, " +
			"weshalb nun jede dritte noch vorhandene Zahl entfernt wird. Dieses Vorgehen wird wiederholt, " +
			"bis das gesamte Array abgearbeitet ist und nur noch die 'glücklichen' Zahlen übrig sind.\n" +
			"Es gibt unendlich viele 'glückliche' Zahlen. Sie teilen außerdem einige Eigenschaften mit " +
			"Primzahlen, wie z.B. dem asymptotischen Verhalten.";
    }
	
	public String getCodeExample()
	{
		return
			"public void sieveOfJosephusFlavius(int n)\n"+
			"{\n" +
			"    LinkedList<Integer> numbers = createSequence(n);\n" +
			"    numbers = eliminateEvenNumbers(n);\n" +
			"    \n" +
			"    for (int i = 1; i < numbers.size(); i++)\n" +
			"    {\n" +
			"        int value = numbers.get(i);\n" +
			"        \n" +
			"        for (int j = value - 1; j < numbers.size(); j += value)\n" +
			"        {\n" +
			"            numbers.set(j, -1);\n" +
			"        }\n" +
			"        \n" +
			"        cleanupNumbers(numbers, -1);\n" +
			"    }\n" +
			"    \n" +
			"    return numbers;\n" +
			"}";
	}
	
	public String getFileExtension()
	{
		return "asu";
	}
	
	public Locale getContentLocale()
	{
		return Locale.GERMAN;
	}
	
	public GeneratorType getGeneratorType()
	{
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_MATHS);
	}
	
	public String getOutputLanguage()
	{
		return Generator.JAVA_OUTPUT;
	}
	
    //// UTILITY ///////////////////////////////////////////////////////////////
    
    private void ShowMultilineTextForOneStep(String text, Coordinates coordinates, int maximumLineLength, DisplayOptions displayOptions)
	{
		String[] tokens = text.split("\\s+");
		LinkedList<String> lines = new LinkedList<String>();
		String currentLine = "";
		for (String t : tokens)
		{
			if (currentLine.length() + t.length() < maximumLineLength)
			{
				if (t.charAt(t.length() - 1) == '\n')
				{
					lines.add(currentLine);
					currentLine = "";
				}
				else
				{
					currentLine += t + " ";
				}
			}
			else
			{
				lines.add(currentLine);
				currentLine = t + " ";
			}
		}
		lines.add(currentLine);
		
		int x = coordinates.getX();
		int y = coordinates.getY();
		LinkedList<Text> textPrimitives = new LinkedList<Text>();
		
		for (String l : lines)
		{
			textPrimitives.add(lang.newText(new Coordinates(x, y), l, "DescriptionText" + textPrimitives.size(), displayOptions));
			y += 20;
		}
		
		lang.nextStep("Einleitung");
		
		for (Text t : textPrimitives)
		{
			t.hide();
		}
	}
}