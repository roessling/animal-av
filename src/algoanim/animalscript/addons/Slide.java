package algoanim.animalscript.addons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import translator.ResourceLocator;
import algoanim.animalscript.addons.bbcode.BBCode;
import algoanim.animalscript.addons.bbcode.Plain;
import algoanim.animalscript.addons.bbcode.Style;
import algoanim.primitives.Primitive;
import algoanim.primitives.generators.Language;

/**
 * @author Marc Werner <mwerner@rbg.informatik.tu-darmstadt.de>
 * 
 *         Creates a slide animation. This could be used to present larger
 *         blocks of text in a more convenient way. The class uses BBCode to
 *         create text styling and implements a special [block] code to create
 *         animation steps.
 */
public class Slide extends MultiPrimitiveAnim {
  private String b;
  private Style  s;

  /**
   * Create a new slide animation
   * 
   * @param lang
   *          The Language object to add the slide to
   * @param file
   *          The source file of the slide
   * @param baseIDRef
   *          The alignment base
   * @param style
   *          The animation style
   * @param vars
   *          content of variables to be replaced
   */
  public Slide(Language lang, String file, String baseIDRef, Style style,
      Object... vars) {
    super(lang);

    b = baseIDRef;
    s = style;

    // get the content blocks from the file
    // those are the single animation steps later
    String slideContent = getFileContent(file);
    String[] blocks = getBlocks(slideContent);

    // get the formatting for each animation step
    for (int i = 0; i < blocks.length; i++) {
      // replace the variables in each block
      if (vars.length > 0) {
        blocks[i] = replaceVars(blocks[i], vars);
      }

      matchBBCode(blocks[i]);
      l.nextStep();
    }
  }

  /**
   * Get the first [block] of a slide (without replacing BBCode). This is can be
   * used as the animation's description in the Generator tree view.
   * 
   * Note: it is recommend that the [block] and [/block] tags are place in their own line
   * with no other content.
   * 
   * For example:
   * [block]<br />
   *   This is an example text[br]<br />
   *   that contains two lines.<br />
   * [/block]
   * 
   * @param file
   *          The slide's file name and location
   * @return The first block
   */
  public static String getTeaser(String file) {
    // get the content blocks from the file
    // those are the single animation steps later
    String slideContent = getFileContent(file);
    String[] blocks = getBlocks(slideContent);

    return blocks[0].replace("[br]", "\n");
  }

  /**
   * Get the whole file content as one big string.
   * 
   * @param fileName
   *          the name of the file to read
   * @return the file content as String
   */
  private static String getFileContent(String fileName) {
    StringBuilder fileContent = new StringBuilder();
    try {
      InputStream f = ResourceLocator.getResourceLocator().getResourceStream(
          fileName);
      BufferedReader br = new BufferedReader(new InputStreamReader(f));
      String thisLine = null;
      while ((thisLine = br.readLine()) != null) {
        // remove all whitespaces and then append a single space (just to be
        // sure that words are still divided)
        thisLine = thisLine.trim().concat(" ");
        fileContent.append(thisLine);
      }
    } catch (IOException e) {
      System.err
          .println("Slide.getFileContent(): Could nor read resource or file "
              + fileName);
    }

    return fileContent.toString();
  }

  /**
   * Get all animation blocks in a slide.
   * 
   * @param input
   *          The slide's content
   * @return The animation blocks
   */
  private static String[] getBlocks(String input) {
    // split the whole string by the block elements
    // block elements are potentially divided by white spaces
    String[] blocks = Pattern.compile("\\[/block\\]\\s*\\[block\\]",
        Pattern.CASE_INSENSITIVE).split(input);
    // remove leading and tailing block statements
    blocks[0] = blocks[0].replace("[block]", "");
    blocks[blocks.length - 1] = blocks[blocks.length - 1].replace("[/block]",
        "");
    return blocks;
  }

  /**
   * Replace any variables with their content. Variables are created by using {}
   * with a index number between the brackets.
   * 
   * @param text
   *          Text to be replaced
   * @param vars
   *          An array with the contents of the variables
   * @return A String where the variables have been replaced with their
   *         contents.
   */
  private String replaceVars(String text, Object[] vars) {
    String result = new String(text);
    for (int i = 0; i < vars.length; i++) {
      result = result.replace("{" + i + "}", vars[i].toString());
    }

    return result;
  }

  /**
   * Create primitives by matching the BBCode and invoking BBCode classes
   * 
   * @param input
   *          The slide's content as String
   * @return Primitives created from BBCode
   */
  private List<Primitive> matchBBCode(String input) {
    // regular expression to match all BB Code items (regardless of the
    // character case)
    Pattern pattern = Pattern.compile("(.*)\\[(.+)\\](.*)\\[/\\2\\](.*)",
        Pattern.CASE_INSENSITIVE);
    Matcher m = pattern.matcher(input);

    // if the regular expression matches we need to further examine the parts
    if (m.matches()) {
      // examine text before match (if it is not an empty string)
      if (!m.group(1).trim().isEmpty()) {
        p.addAll(matchBBCode(m.group(1)));
      }

      // create new bb code class from match
      String bbCode = m.group(2).substring(0, 1).toUpperCase()
          + m.group(2).substring(1);
      try {
        @SuppressWarnings("rawtypes")
        // first letter of bb code item needs to be upper case
        Class bbClass = Class.forName("algoanim.animalscript.addons.bbcode."
            + bbCode);
        BBCode bb = (BBCode) bbClass.newInstance();
        bb.setLanguage(l);
        bb.setProperties(s);

        // get animal primitives from bb code and add them to the list
        p.addAll(bb.getPrimitives(m.group(3), getBase()));

      } catch (Exception e) {
        System.err.println("Slide.matchBBCode(): No class matching the BBCode "
            + bbCode + " found - algoanim.animalscript.addons.bbcode."+bbCode);
      }

      // examine text after match (if it is not an empty string)
      if (!m.group(4).trim().isEmpty()) {
        p.addAll(matchBBCode(m.group(4)));
      }
    } else {
      // if there were no bb codes in the input we generate a plain text item
      BBCode bb = new Plain();
      bb.setLanguage(l);
      bb.setProperties(s);
      p.addAll(bb.getPrimitives(input, getBase()));
    }

    // return the generated list of primitives
    return p;
  }

  /**
   * Get the base primitive name to align. This can either be the last element
   * of our primitive list or the given base if the list is empty
   * 
   * @return The primitive name
   */
  private String getBase() {
    String base;
    if (p.size() > 0) {
      base = p.get(p.size() - 1).getName();
    } else {
      base = b;
    }
    return base;
  }
}
