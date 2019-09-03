package generators.compression.runlength;

import generators.compression.helpers.CompressionAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class RLE extends CompressionAlgorithm implements Generator {

	private static final int inputLimit = 15;
	
	SourceCode sc;
	
	private static final String DESCRIPTION =
	  "Die Laufl&auml;ngendkodierung ist ein verlustfreies Kompressionsverfahren f&uuml;r Texte. Das"
			+ " Verfahren ist f&uuml;r Texte geeignet, bei dem einzelne Zeichen oft aufeinander folgen."
			+ " So wird beispielsweise die Zeichenkette AAAA zu 4A zusammengefasst. Einzelne"
			+ " Zeichen werden hingegen nicht ver&auml;ndert und unkodiert &uuml;bernommen.";

	private static final String SOURCE_CODE = "Der Algorithmus wird in einer Animation demonstriert. Um die grafische Animation in voller Größe darstellen"
		+ " zu können, wird die Eingabe auf 15 Buchstaben begrenzt.\n\n"
		+ "public void rle(char[] array) {" // 0
			+ "\n  int count = 1;" // 1
			+ "\n  char tmp;" // 2
			+ "\n  String result = \"\"" // 3
			+ "\n  for (int i = 0; i < array.length; i++) {" // 4
			+ "\n  	 tmp = array[i];" // 5
			+ "\n  	 if (i == array.length-1) {" // 6
			+ "\n  		result += tmp;" // 7
			+ "\n  		break;" // 8
			+ "\n  	 }" // 9
			+ "\n  	 while (array[i + 1] == tmp) {" // 10
			+ "\n  		i++;" // 11
			+ "\n  		count++;" // 12
			+ "\n  	 }" // 13
			+ "\n  	 if (count == 1) result += tmp;" // 14
			+ "\n  	 else result = result + (String)count + tmp;" // 15
			+ "\n  	 count = 1;" // 16
			+ "\n  }" // 17
			+ "\n}"; // 18


	public RLE() {
		// nothing to be done
	}

	public void compress(String[] text) throws LineNotExistsException {
		// trim input to maximum length
//		String ein = "";
		String[] t = new String[Math.min(text.length, inputLimit)];
		for (int i=0;i<t.length;i++) {
			t[i] = text[i];
//			ein += text[i];
		}
//		text = t;
		
		// topic
		Text topic = lang.newText(new Coordinates(20, 50),"Lauflängenkodierung", "Topic", null, tptopic);

      lang.newRect(new Offset(-5, -5, topic, "NW"), 
          new Offset(5, 5, topic, "SE"), "topicRect", null, rctp);

		Text algoinWords = lang.newText(new Coordinates(20, 100),
				"Der Algorithmus in Worten", "inWords", null, tpwords);

		lang.nextStep();

		Text step1 = lang.newText(new Offset(0,100,topic,"SW"),"1) Lese die Eingabe iterativ buchstabenweise ein.",
				"line1", null,		tpsteps);
		lang.nextStep();
		Text step2 = lang.newText(new Offset(0,40,step1,"SW"),"2) Zähle dabei die aufeinanderfolgenden gleichen Buchstaben bis zur aktuellen Iterationsstelle im Text.",
						"line2", null, tpsteps);
		lang.nextStep();
		Text step3 = lang.newText(new Offset(0,40,step2,"SW"),"3) Für das n-fache aufeinanderfolgende Auftreten eines Buchstaben X, kodiere nX.",
						"line3", null, tpsteps);
		lang.nextStep();
		Text step4 = lang.newText(new Offset(0,40,step3,"SW"),"4) Für das einfache Auftreten eines Buchstabens Y kodiere nur Y.",
				"line4", null, tpsteps);
		lang.nextStep();

		algoinWords.hide();
		step1.hide();
		step2.hide();
		step3.hide();
		step4.hide();
		
		StringArray strArray = lang.newStringArray(new Offset(0, 100, topic,"SW"), t, "stringArray", null, ap);

		sc = lang.newSourceCode(new Offset(0, 50, strArray, "SW"), "codeName",
				null, scp);
		sc.addCodeLine("public void rle(char[] array) {", null, 0, null);
		sc.addCodeLine("int count = 1;", null, 1, null);
		sc.addCodeLine("char tmp; ", null, 1, null);
		sc.addCodeLine("String result;", null, 1, null);
		sc.addCodeLine("for (int i = 0; i < array.length; i++) {", null, 1, null);
		sc.addCodeLine("tmp = array[i];", null, 2, null);
		sc.addCodeLine("if (i == array.length - 1) {", null, 3, null);
		sc.addCodeLine("result += tmp;", null, 4, null);
		sc.addCodeLine("break;", null, 4, null);
		sc.addCodeLine("}", null, 3, null);
		sc.addCodeLine("while (array[i + 1] == tmp) {", null, 2, null);
		sc.addCodeLine("i++;", null, 3, null);
		sc.addCodeLine("count++;", null, 3, null);
		sc.addCodeLine("}", null, 2, null);
		sc.addCodeLine("if (count == 1) result += tmp;", null, 2, null);
		sc.addCodeLine("else result = result + (String)count + tmp;", null, 2,null);
		sc.addCodeLine("count = 1;", null, 2, null);
		sc.addCodeLine("}", null, 1, null);
		sc.addCodeLine("}", null, 0, null);

		// initialize the matrix that shows tmp and count
		String[][] varData = new String[2][2];
		varData[0][0] = "count";
		varData[1][0] = "tmp";
		varData[0][1] = "   ";
		varData[1][1] = "   ";
		StringMatrix varMatrix = lang.newStringMatrix(new Offset(75,0,sc,"NE"), varData, "varMatrix", null, mp);
		
		Text resultLabel = lang.newText(new Offset(0,50,sc,"SW"), "Ausgabe:   ", "resultLabel", null, tpsteps);
		Text resultText = lang.newText(new Offset(10,-5,resultLabel,"SE"), "   ", "result", null, tpsteps);
		resultText.changeColor(null, Color.BLUE, null, null);
		lang.nextStep();
		
		sc.highlight(0, 0, false);
		lang.nextStep();

		sc.toggleHighlight(0, 0, false, 1, 0);
		varMatrix.put(0, 1, " 1 ", null, null);
		lang.nextStep();

		sc.toggleHighlight(1, 0, false, 2, 0);
		lang.nextStep();

		sc.toggleHighlight(2, 0, false, 3, 0);
		lang.nextStep();

		sc.toggleHighlight(3, 0, false, 4, 0);
		lang.nextStep();
		sc.unhighlight(4, 0, false);
		ArrayMarker am = lang.newArrayMarker(strArray, 0, "arrayMarker", null,amp);

		int marked = 0;

		int count = 1;
		String tmp;
		 String result = "";

		for (int i = 0; i < t.length; i++) {
			am.move(i, null, null);
			for (int m = 0; m <= marked; m++) {
				strArray.unhighlightCell(i - m, null, null);
			}
			marked = 0;
			strArray.highlightCell(i, null, null);
			marked++;
			if (i > 0)
				strArray.unhighlightCell(i - 1, null, null);
			sc.unhighlight(4, 0, false);
			sc.highlight(5, 0, false);
			tmp = t[i];
			
			varMatrix.put(1, 1, " " + tmp + " ", null, null);
			lang.nextStep();

			sc.toggleHighlight(5, 0, false, 6, 0);
			if (i == t.length - 1) {
				lang.nextStep();
				sc.toggleHighlight(6, 0, false, 7, 0);
				result += tmp;
				
				resultText.setText(result, null, null);

				lang.nextStep();
				sc.toggleHighlight(7, 0, false, 8, 0);
				lang.nextStep();
				break;
			} 
			// else
			lang.nextStep();
			sc.unhighlight(6, 0, false);

			sc.unhighlight(8, 0, false);
			sc.highlight(10, 0, false);
			while (t[i + 1].equals(tmp)) {
				lang.nextStep();
				sc.unhighlight(10, 0, false);
				sc.unhighlight(12, 0, false);
				sc.highlight(11, 0, false);
				i++;
				am.move(i, null, null);
				strArray.highlightCell(i, null, null);
				marked++;
				lang.nextStep();
				sc.toggleHighlight(11, 0, false, 12, 0);
				count++;
				
				varMatrix.put(0, 1, " " + count + " ", null, null);
				if (i==t.length-1) break;
			}
			lang.nextStep();
			sc.unhighlight(12, 0, false);
			sc.unhighlight(10, 0, false);
			sc.highlight(14, 0, false);
			if (count == 1) {
				lang.nextStep();
				sc.toggleHighlight(14, 0, false, 16, 0);
				result += tmp;
				resultText.setText(result, null, null);
			} else {
				lang.nextStep();
				result = result + (new Integer(count)).toString() + tmp;
				sc.toggleHighlight(14, 0, false, 15, 0);
				lang.nextStep();
				sc.toggleHighlight(15, 0, false, 16, 0);
				resultText.setText(result, null, null);
			}
			lang.nextStep();
			count = 1;
			varMatrix.put(0, 1, " " + count + " ", null, null);
			sc.toggleHighlight(16, 0, false, 4, 0);
			lang.nextStep();
		}

		// mark the end
		sc.unhighlight(8, 0, false);
		sc.highlight(18, 0, false);
		
		lang.nextStep();
		
		Text info = lang.newText(new Offset(0,35,resultLabel,"SW"), "Die Lauflängenkodierung eignet sich besonders gut für Texte, bei denen", "info", null, tpsteps);

      lang.newText(new Offset(0,20,info,"SW"), 
          "sich einzelne Zeichen oft aufeinanderfolgend wiederholen.", 
          "info", null, tpsteps);
	}

	public static String getSOURCE_CODE() {
		return SOURCE_CODE;
	}

	public String getCodeExample() {
		return SOURCE_CODE;
	}

	public String getDescription() {
		return DESCRIPTION;
	}

	public String getName() {
		return "Laufl\u00e4ngenkodierung";
	}

	public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
		String[] strArray = (String[]) primitives.get("stringArray");
		try {
			compress(strArray);
		} catch (LineNotExistsException e) {
			e.printStackTrace();
		}
    lang.finalizeGeneration();
    return lang.getAnimationCode();
	}

	public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_COMPRESSION);
	}

  @Override
  public String getAlgorithmName() {
    return "RLE (Laufl\u00e4ngencodierung)";
  }
  
  @Override
  public void init() {
    lang = new AnimalScript("Lauflängenkodierung", "Florian Lindner", 800, 600);
    lang.setStepMode(true);
  }
	
}
