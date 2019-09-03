package algoanim.animalscript;

import java.util.HashMap;

import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.primitives.generators.SourceCodeGenerator;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Timing;

/**
 * @author Stephan Mehlhase
 * 
 */
public class AnimalSourceCodeGenerator extends AnimalGenerator implements
		SourceCodeGenerator {
	private static int count = 1;
	private HashMap<String, Integer> labelsToLineNumbers;
	
	public AnimalSourceCodeGenerator(Language aLang) {
		super(aLang);
		labelsToLineNumbers = new HashMap<String, Integer>(47);
	}

	/**
	 * @see algoanim.primitives.generators.SourceCodeGenerator
	 *      #create(algoanim.primitives.SourceCode)
	 */
	public void create(SourceCode sc) {
		if (this.isNameUsed(sc.getName()) || sc.getName() == "") {
			sc.setName("SourceCode" + AnimalSourceCodeGenerator.count);
			AnimalSourceCodeGenerator.count++;
		}
		lang.addItem(sc);

		StringBuilder def = new StringBuilder(AnimalScript.INITIAL_GENBUFFER_SIZE);
		def.append("codegroup \"").append(sc.getName()).append("\" at ");
		def.append(AnimalGenerator.makeNodeDef(sc.getUpperLeft()));

		SourceCodeProperties props = sc.getProperties();
		addColorOption(props, def);
		addColorOption(props, AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, " highlightColor ", def);
    addColorOption(props, AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, " contextColor ", def);
		addFontOption(props, AnimationPropertiesKeys.FONT_PROPERTY, def);
		addIntOption(props, AnimationPropertiesKeys.DEPTH_PROPERTY, " depth ", def);

		def.append(AnimalGenerator.makeDisplayOptionsDef(sc.getDisplayOptions(), props));
		lang.addLine(def);
	}

	/**
	 * @see algoanim.primitives.generators.SourceCodeGenerator #highlight(
	 *      algoanim.primitives.SourceCode, int, int, boolean,
	 *      algoanim.util.Timing, algoanim.util.Timing)
	 */
	public void highlight(SourceCode code, int line, int row, boolean context,
			Timing delay, Timing duration) {
		StringBuilder sb = new StringBuilder(256);
		sb.append("highlightCode on \"").append(code.getName());
		sb.append("\" line ").append(line);
		if (row >= 0) {
		  sb.append(" row ").append(row);
		}
		if (context) {
		  sb.append(" context ");
		}
		addWithTiming(sb, delay, duration);
	}
	
	/**
	 * @see algoanim.primitives.generators.SourceCodeGenerator#highlight(
	 *      algoanim.primitives.SourceCode, int, int, boolean,
	 *      algoanim.util.Timing, algoanim.util.Timing)
	 */
	public void highlight(SourceCode code, String lineName, int row, boolean context,
			Timing delay, Timing duration) {
		Integer lineNo = labelsToLineNumbers.get(lineName);
		if (lineNo != null) {
			highlight(code, lineNo.intValue(), row, context, delay, duration);
		} else
			System.err.println("@highlight(SC,S,i,b,T,T): argh! lineno is null for " + lineName);
	}

	/**
	 * @see algoanim.primitives.generators.SourceCodeGenerator
	 *      #unhighlight( algoanim.primitives.SourceCode, int, int,
	 *      boolean, algoanim.util.Timing, algoanim.util.Timing)
	 */
	public void unhighlight(SourceCode code, String lineName, int row, boolean context,
			Timing delay, Timing duration) {
		Integer lineNo = labelsToLineNumbers.get(lineName);
		if (lineNo != null) {
			unhighlight(code, lineNo.intValue(), row, context, delay, duration);
		} else
			System.err.println("@unhighlight(SC,S,i,b,T,T): argh -- lineno is null for " + lineName);
	}
	
	/**
	 * @see algoanim.primitives.generators.SourceCodeGenerator
	 *      #unhighlight( algoanim.primitives.SourceCode, int, int,
	 *      boolean, algoanim.util.Timing, algoanim.util.Timing)
	 */
	public void unhighlight(SourceCode code, int line, int row, boolean context,
			Timing delay, Timing duration) {
		StringBuilder sb = new StringBuilder(128);
		sb.append("unhighlightCode on \"").append(code.getName());
		sb.append("\" line ").append(line);
		if (row >= 0) {
		  sb.append(" row ").append(row);
		}
		if (context) {
		  sb.append(" context");
		}
		addWithTiming(sb, delay, duration);
	}

	/**
	 * @see algoanim.primitives.generators.SourceCodeGenerator #hide(
	 *      algoanim.primitives.SourceCode, algoanim.util.Timing)
	 */
	public void hide(SourceCode code, Timing delay) {
	  StringBuilder sb = new StringBuilder(128);
	  sb.append("hideCode \"").append(code.getName()).append("\" ");
	  sb.append(AnimalGenerator.makeOffsetTimingDef(delay));
	  lang.addLine(sb.toString());
	}

	/**
	 * @see algoanim.primitives.generators.SourceCodeGenerator
	 *      #addCodeElement( algoanim.primitives.SourceCode,
	 *      java.lang.String, java.lang.String, int, int,
	 *      algoanim.util.Timing)
	 */
	public void addCodeElement(SourceCode code, String codeline, String name,
      int indentation, int row, Timing t) {
	  addCodeElement(code, codeline, name, indentation, false, row, t);
	}

	/**
	 * @see algoanim.primitives.generators.SourceCodeGenerator
	 *      #addCodeLine( algoanim.primitives.SourceCode, java.lang.String,
	 *      java.lang.String, int, algoanim.util.Timing)
	 */
	public void addCodeLine(SourceCode code, String codeline, String name,
			int indentation, Timing t) {
		// achte hier mal darauf, dass name und t null sein koennen.
		// dann entsprechend behandeln
	  StringBuilder sb = new StringBuilder(128);
		sb.append("addCodeLine \"").append(codeline).append("\" ");
		sb.append("to \"").append(code.getName()).append("\"");
		if (indentation > 0) {
		  sb.append(" indentation ").append(indentation);
		}
		// AnimalGenerator.makeTimingDef checks if t == null.
		sb.append(AnimalGenerator.makeOffsetTimingDef(t));
		lang.addLine(sb.toString());
	}

  @Override
  public void addCodeElement(SourceCode code, String codeline, String name,
      int indentation, boolean noSpace, int row, Timing t) {
    // achte hier mal darauf, dass name und t null sein koennen.
    // dann entsprechend behandeln
    StringBuilder sb = new StringBuilder(128);
    sb.append("addCodeElem \"").append(codeline).append("\" ");
    sb.append("to \"").append(code.getName()).append("\"");
    if (row > 0) {
      sb.append(" row ").append(row);
    }
    if (noSpace)
      sb.append(" inline ");
    if (indentation > 0) {
      sb.append(" indentation ").append(indentation);
    }
    // AnimalGenerator.makeTimingDef checks if t == null.
    sb.append(AnimalGenerator.makeOffsetTimingDef(t));
    lang.addLine(sb.toString());
  }
}
