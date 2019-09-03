package generators.compression.arithmetic;

import generators.compression.helpers.CompressionAlgorithm;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;
import java.util.Vector;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.Primitive;
import algoanim.primitives.Rect;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class ArithmeticDecoding extends CompressionAlgorithm implements Generator {
	
	private static Vector<Range> ranges;
	
	private static Vector<Rect> rects;
	
	private float startSubInterval;
	
	private float endSubInterval;
	
	private static final int inputLimit = 6;
	
	public static class Range {
		
		protected char letter;
		
		protected float start;
		
		protected float end;
		
		public Range(char letterIn, float startIn, float endIn) {
			
			this.letter = letterIn;
			
			this.start = startIn;
			
			this.end = endIn;
		}

		public float getEnd() {
			return end;
		}

		public char getLetter() {
			return letter;
		}

		public float getStart() {
			return start;
		}
	}
	public ArithmeticDecoding() {
	  // Nothing to be done here.
	}

	/**
	 * Decodes the coded text
	 * @param text
	 */
	public void decode(String[] text) {
		
		// trim input to maximum length
//		String eingabe = "";
		String[] t = new String[Math.min(text.length, inputLimit)];
		for (int i=0;i<t.length;i++) {
			t[i] = text[i];
//			eingabe += text[i];
		}
//		text = t;
		
		// Prperties
		RectProperties rctpHighlight = new RectProperties();
		rctpHighlight.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		rctpHighlight.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.YELLOW);
		rctpHighlight.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
		rctpHighlight.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

		// topic
		Text topic = this.lang.newText(new Coordinates(20,50), "Aritmetic Decoding", "Topic", null, tptopic);

      this.lang.newRect(new Offset(-5,-5,topic,"NW"), new Offset(5,5,topic, "SE"), "topicRect", null, rctp);

		// Algo in words
		this.lang.nextStep();
		Text algoinWords = this.lang.newText(new Coordinates(20,100), "Der Algorithmus in Worten", "inWords", null, tpwords);

		// Algo steps
		this.lang.nextStep();
		Text step1 = this.lang.newText(new Offset(0,100,topic,"SW"), "0) Gegeben ist eine Tabelle absoluter Häufigkeiten und eine Kodierung als Gleitkommazahl.", "line1", null, tpsteps);
		this.lang.nextStep();
		Text step2 = this.lang.newText(new Offset(0,30,step1,"SW"), "1) Bilde ein Intervall I in [0,1], welches entsprechend der Häufigkeiten", "line2", null, tpsteps);
		Text step21 = this.lang.newText(new Offset(0,20,step2,"SW"),"       proportional auf die Buchstaben aufgeteilt wird.", "line2", null, tpsteps);
		this.lang.nextStep();
		Text step3 = this.lang.newText(new Offset(0,30,step21,"SW"), "2) Wähle das Teilintervall, in dem sich die Gleitkommzahl befindet und betrachte", "line3", null, tpsteps);
		Text step31 = this.lang.newText(new Offset(0,20,step3,"SW"), "      dieses als neues Intervall. Der Buchstabe, der zu diesem Intervall gehörte", "line31", null, tpsteps);
		Text step32 = this.lang.newText(new Offset(0,20,step31,"SW"), "     wird für die Ausgabe notiert. Das neue Intervall wird wie in 1) unterteilt.", "line31", null, tpsteps);
		this.lang.nextStep();
		Text step4 = this.lang.newText(new Offset(0,30,step32,"SW"), "3) Schritt 2 wird entsprechend der Anzahl der Gesamtbuchstaben, die aus der Tabelle", "line4", null, tpsteps);
		Text step41 = this.lang.newText(new Offset(0,20,step4,"SW"), "      der Häufigkeiten ermittelt werden kann, wiederholt.", "line32", null, tpsteps);
		this.lang.nextStep();
		
		algoinWords.hide();
		step1.hide();
		step2.hide();
		step21.hide();
		step3.hide();
		step31.hide();
		step32.hide();
		step4.hide();
		step41.hide();
		
		tpwords.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",Font.PLAIN,16));

		// extract the input
		String input = "";
		for (int i=0;i<text.length;i++) {
			input += text[i];
		}
		algoinWords.setText("Eingabe:  " + input, null, null);
		algoinWords.show();
		this.lang.nextStep();
		
		Text expl = this.lang.newText(new Offset(0,0,step1,"SW"), "Wir erhalten die Häufigkeiten, sowie die kodierte Zahl der Eingabe:       " + encode(input), "line2", null, tpsteps);
		Text expl2 = this.lang.newText(new Offset(0,20,expl,"SW"), "Durch die Häufigkeiten lässt sich das initiale Intervall herstellen.", "line2", null, tpsteps);
		
		String result = "";
		Text ausgabeLabel = this.lang.newText(new Offset(0,30,expl2,"SW"), "Ausgabe:", "ausgabe", null, tpsteps);
		Text ausgabe = this.lang.newText(new Offset(10,-5,ausgabeLabel,"SE"), result, "ausgabe", null, tpsteps);
		ausgabe.changeColor(null, Color.BLUE, null, null);
		this.lang.nextStep();
		
		// print the initial interval (0 <-----> 1)
		startSubInterval = 0;
		endSubInterval = 1;
		
		rects = new Vector<Rect>();
		Rect actualRect = null;
		for (int i=0;i<ranges.size();i++) {
			if (i==0) actualRect = this.printRange(ranges.elementAt(i), expl2);
			else this.printRange(ranges.elementAt(i), expl2);
			if (i == ranges.size()-1) {
				TextProperties tpend = new TextProperties();
				tpend.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
				tpend.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",Font.PLAIN,10));

          lang.newText(new Offset(685,10,actualRect,"SW"), new Float(this.endSubInterval).toString(), "text", null, tpend);
			}
		}
		
		
		// algorithm
		float n = encode(input);
		
		Vector<Range> urRanges = new Vector<Range>(0,1);
		for (int i=0;i<ranges.size();i++) {
			Range r = new Range(ranges.elementAt(i).getLetter(),ranges.elementAt(i).getStart(),ranges.elementAt(i).getEnd());
			urRanges.add(r);
		}
		
		for (int i=0;i<text.length;i++) {
			// get the next letter. add it to the result, save "j"
			int tmp = -1;
			for (int j=0;j<ranges.size();j++) {
				if (n >= ranges.elementAt(j).getStart() && n < ranges.elementAt(j).getEnd()) {
					result += ranges.elementAt(j).getLetter();
					tmp = j;
					break;
				}
			}
			
			
			
			// highlight the cell
			rects.elementAt(tmp).hide();
			rects.set(tmp, lang.newRect(rects.elementAt(tmp).getUpperLeft(), rects.elementAt(tmp).getLowerRight(), "rect", null, rctpHighlight));
			rects = new Vector<Rect>(0,1);
			
			lang.nextStep();
			
			ausgabe.setText(result, null, null);
			
			lang.nextStep();
			
			// modify ranges
			float start = ranges.elementAt(tmp).getStart();
			float end = ranges.elementAt(tmp).getEnd();
			startSubInterval = start;
			endSubInterval = end;
			
			for (int k=0;k<ranges.size();k++) {
				float urStart = urRanges.elementAt(k).getStart();
				float urEnd = urRanges.elementAt(k).getEnd();
				ranges.elementAt(k).start = start + urStart * (end -start);
				ranges.elementAt(k).end = start + urEnd * (end -start);
			}
			
			// print the actual subinterval
			Rect tmpRect = actualRect;
			for (int k = 0; k < ranges.size(); k++) {
				if (k == 0)	{
					actualRect = this.printRange(ranges.elementAt(k),actualRect);
				}
				else {
					this.printRange(ranges.elementAt(k), tmpRect);
				}
				// print the last Nodevalue on the right side
				if (k == ranges.size() - 1) {

            lang.newText(new Offset(685, 10, actualRect, "SW"),
							new Float(this.endSubInterval).toString(), "text", null, tpsteps);
				}
			}
		}
		
		tpsteps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",Font.PLAIN, 16));
		Text fazit = lang.newText(new Offset(0,90,rects.elementAt(0),"SW"), "Die Ausgabe entspricht genau der erwarteten Eingabe. Die kodierte Zahl stammt aus dem", "Ausgabe", null, tpsteps);
		Text fazit2 = lang.newText(new Offset(0,20,fazit,"SW"), "letzten aufgeführtem Intervall. Dabei ist es nicht wichtig, welche Zahl innerhalb dieses Intervals gewählt", "ausgabe", null, tpsteps);
		Text fazit3 = lang.newText(new Offset(0,20,fazit2,"SW"), "wurde. Für eine gute Kompression sollten allerdings Zahlen gewählt werden, die sich mit", "fazit", null, tpsteps);
      lang.newText(new Offset(0,20,fazit3,"SW"), "möglichst wenigen Bits kodieren lassen.", "fazit", null, tpsteps);
	}

	/**
	 * Encodes the input text.
	 * @param text the input text to encode
	 * @return the encoded floating point number
	 */
	private static float encode(String text) {
    // ##################################################################################
		// set up frequencys
		float[] letters = new float[256];
		for (int i=0; i<text.length();i++) {
			letters[new Integer(text.charAt(i))]++;
		}
		float[] frequency = new float[256];
		for (int i=0;i<letters.length;i++) {
			frequency[i] = letters[i]/text.length();
		}
		// put them sequentially into a vector
		float big = 0;
		int index = 0;
		ranges = new Vector<Range>(0,1);
		
		for (int i=0; i<text.length();i++) {
			for (int j=0;j<frequency.length;j++) {
				if (frequency[j] > big) {
					big = frequency[j];
					index = j;
				}
			}
			if (!ranges.isEmpty() && big > 0) ranges.add(new Range((char)index,ranges.lastElement().getEnd(),ranges.lastElement().getEnd() + big));
			else if (big > 0) ranges.add(new Range((char)index, 0, big));
			frequency[index] = -1;
			big = 0;
			index = 0;
		}
		//##################################################################################
		
		
		// encode
		float foo;
		char tmp;
		float start = 0;
		float end = 1;
		for (int i=0; i<text.length();i++) {
			tmp = text.charAt(i);
			for (int j=0;j<ranges.size();j++) {
				if (tmp == ranges.elementAt(j).getLetter()) {
					foo = start + (end-start)*ranges.elementAt(j).getStart();
					end = foo + (end-start)*(ranges.elementAt(j).getEnd() - ranges.elementAt(j).getStart());
					start = foo;
				}
			}
		}
		return start;
	}
	
	/**
	 * Prints one rectangle of the interval and prints the letter and indexes either.
	 * @param r A given range r the represents one part of the interval
	 * @param prim The primitive under which the Rect is printed.
	 * @return the printed Rectangle
	 */
	private Rect printRange(Range r, Primitive prim) {
		// create the rectangle
		int s = new Float((r.getStart() - startSubInterval)*700/(endSubInterval-startSubInterval)).intValue();
		int e = new Float((r.getEnd() - startSubInterval)*700/(endSubInterval-startSubInterval)).intValue();
		RectProperties recIntervalProp = new RectProperties();
		recIntervalProp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
		Rect rect = lang.newRect(new Offset(s,40, prim, "SW"), new Offset(e,65, prim, "SW"), "rectangle", null, recIntervalProp);
		rects.add(rect);
		// print the letter inside of the rectangle

      lang.newText(new Offset((e-s)/2,20,rect,"M"), "" + r.letter, "text", null, tpsteps);
		tpsteps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",Font.PLAIN,10));
		// print the start value of the interval
 
      lang.newText(new Offset(2, 9, rect, "SW"), new Float(
				r.getStart()).toString(), "startValue", null, tpsteps);
		return rect;
	}
	
	
	public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
	  String[] strArray = (String[]) primitives.get("stringArray");
	  try {
	    decode(strArray);
	  } catch (LineNotExistsException e) {
	    e.printStackTrace();
	  }
	  lang.finalizeGeneration();
	  return lang.getAnimationCode();
	}

	public String getCodeExample() {
	  StringBuilder s = new StringBuilder(1024);
	  s.append("Schematisch funktioniert das Verfahren wie folgt:");
	  s.append("<ol><li>Gegeben ist eine Tabelle absoluter");
	  s.append(" H&auml;ufigkeiten und eine Kodierung als Gleitkommazahl.</li>");
	  s.append("<li>Bilde ein Intervall I in [0,1], das entsprechend ");
	  s.append("der H&auml;ufigkeiten proportional auf die Buchstaben aufgeteilt wird.</li>");
    s.append("<li>W&auml;hle das Teilintervall, in dem sich die Gleitkommzahl befindet.</li>");
    s.append("<li>Betrachte dieses Teilintervall als neues Intervall. Der Buchstabe, der ");
    s.append(" zu diesem Intervall geh&ouml;rte wird f&uuml;r die Ausgabe notiert.</li>");
    s.append("<li>Das neue Intervall wird wie in 2. unterteilt.");
    s.append("<li>Die Schritte 3.-5. werden entsprechend der Anzahl der Gesamtbuchstaben,");
    s.append(" die aus der Tabelle der H&auml;ufigkeiten ermittelt werden kann, wiederholt.</li>");
    s.append("</ol>");
		return s.toString();
	}

	public String getDescription() {
		return "Die arithmetische Dekodierung ist die Umkehrung der gleichnamigen Kodierung."
    + " Durch die arithmetische Kodierung wurde ein String in eine Gleitkommazahl komprimiert."
    + " Um nun diese Gleikommazahl wieder zu dekomprimieren, werden die absoluten H&auml;ufigkeiten"
    + " ben&ouml;tigt, die bei der Kodierung mit ausgeliefert werden.";
	}

	public String getFileExtension() {
		return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
	}

	public GeneratorType getGeneratorType() {
		return new GeneratorType(GeneratorType.GENERATOR_TYPE_COMPRESSION);
	}

	public String getName() {
		return "Arithmetische Dekomprimierung";
	}

	public String getAlgorithmName() {
	  return "Arithmetische Komprimierung";
	}
	
  public String getOutputLanguage() {
    return Generator.PSEUDO_CODE_OUTPUT;
  }

  public void init() {
    lang = new AnimalScript("Arithmetic Decoding", "Florian Lindner", 800,600);
    lang.setStepMode(true);
  }


}
