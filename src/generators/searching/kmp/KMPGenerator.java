//This file was created by group of:
// Quoc Hien Dang
// Thanh Tung Dang 1340183 <thanhtung.nov@gmail.com>

package generators.searching.kmp;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Timing;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import java.util.Locale;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;

public class KMPGenerator implements Generator {
  private Language             lang;
  private SourceCodeProperties sourceCodeProperties;
  private String               A;
  private String               B;
  private static Timing        time = new Timing(100) {
                                      @Override
                                      public String getUnit() {
                                        return null;
                                      }
                                    };

  public void init() {
    lang = new AnimalScript("Knutt-Morris-Pratt Algorithm",
        "Thanh Tung Dang, Quoc Hien Dang", 800, 600);
    lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> primitives) {
    sourceCodeProperties = (SourceCodeProperties) props
        .getPropertiesByName("sourceCode");
    A = (String) primitives.get("A");
    B = (String) primitives.get("B");

    // Create string array
    String[] aa = new String[A.length()];
    for (int i = 0; i < A.length(); i++) {
      aa[i] = A.substring(i, i + 1);
    }

    String[] bb = new String[B.length()];
    for (int i = 0; i < B.length(); i++) {
      bb[i] = B.substring(i, i + 1);
    }

    search(aa, bb);
    String result = lang.toString();
    Integer i = new Integer(lang.getStep() + 1);
    return result.replace("DoubleDxxx", i.toString());
  }

  public String getName() {
    return "Knutt-Morris-Pratt String Matching Algorithm[EN]";
  }

  public String getAlgorithmName() {
    return "KMP String Matching Algorithm";
  }

  public String getAnimationAuthor() {
    return "Thanh Tung Dang, Quoc Hien Dang";
  }

  public String getDescription() {
    return "Given a string, and another string called pattern. We have to find where the pattern occurs on the string. "
        + "\n"
        + "The main idea of Knutt-Morris-Pratt Algorithm is to reduce the number of comparing iterations based on prediction: "
        + "\n"
        + "At which position a next match may begin."
        + "\n"
        + "Let's imagine, you're now comparing (i,j)-th character of the text with the j-th character of "
        + "\n"
        + "the pattern, if they're not the same then you start over again, compare the (i,1)-th character "
        + "\n"
        + "with the first character of the pattern. But let's see, now you have a prefix of the pattern on "
        + "\n"
        + "the text. From the i-th to (i , j - 1)-th character of the text (because they're all the same at "
        + "\n"
        + "the previous comparisions). So is it neccessary to start over again? "
        + "\n"
        + "You have this prefix called p of the pattern on the text "
        + "\n"
        + "Knutt-Morris-Pratt Algorithm firstly computes the longest prefix w of p described as below: "
        + "\n"
        + "p = y.w = w.x, in which . means concatenating two strings. So you don't have to start over "
        + "\n"
        + "again, but to shift the pattern length(y) positions, and to compare the first character of x "
        + "\n"
        + "with the (i,j)-th character of the text. If they're different, shift the prefix (now as w) "
        + "\n" + "the same way as before.";
  }

  public String getCodeExample() {
    return "int[] T = new int[b.length() + 1];" + "\n" + "T[0] = -1;" + "\n"
        + "int i = 0; " + "\n" + "int j = T[i];" + "\n"
        + "while (i < b.lenth()) {" + "\n"
        + "	while (j >= 0 && b.charAt(j) != b.charAt(i))" + "\n"
        + "		j = T[j];" + "\n" + "	i++;" + "\n" + "	j++;" + "\n" + "	T[i] = j;"
        + "\n" + "}";
  }

  public String getFileExtension() {
    return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
  }

  public Locale getContentLocale() {
    return Locale.US;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_SEARCH);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }

  public void search(String[] a, String[] b) {

    // Array Properties:
    ArrayProperties ArrayProps = new ArrayProperties("array properties");
    ArrayProps.set("font", new Font(Font.MONOSPACED, Font.PLAIN, 15));
    ArrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    ArrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    ArrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    ArrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    ArrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
    ArrayProps
        .set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.YELLOW);

    // Matrix A :
    StringArray aArray = lang.newStringArray(new Coordinates(70, 120), a,
        "StringArray", null, ArrayProps);

    aArray.hide();
    // Matrix B:
    StringArray bArray = lang.newStringArray(new Coordinates(70, 200), b,
        "StringArray", null, ArrayProps);
    bArray.hide();

    // Array Marker Properties :
    ArrayMarkerProperties amp = new ArrayMarkerProperties();
    amp.set(AnimationPropertiesKeys.SHORT_MARKER_PROPERTY, true);
    amp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    amp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);

    // Array Marker:
    ArrayMarker iMarker = lang.newArrayMarker(bArray, 0, "i", null, amp);
    iMarker.hide();
    amp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "j");
    ArrayMarker jMarker = lang.newArrayMarker(bArray, 0, "j", null, amp);
    jMarker.hide();

    // Source Code Properties:
    // SourceCodeProperties scProps = new SourceCodeProperties();
    // scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
    // scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
    // Font.MONOSPACED, Font.PLAIN, 12));
    // scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
    // scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    // Source Code for calculating T:
    SourceCode calculateT = lang.newSourceCode(new Coordinates(40, 280),
        "calculateT", null, sourceCodeProperties);
    calculateT.addCodeLine("int[] T = new int[b.length() + 1];", null, 0, null);
    calculateT.addCodeLine("T[0] = -1;", null, 0, null);
    calculateT.addCodeLine("int i = 0; int j = T[i];", null, 0, null);
    calculateT.addCodeLine("while (i < b.length()) {", null, 0, null);
    calculateT.addCodeLine("while (j >= 0 && b.charAt(j) != b.charAt(i))",
        null, 1, null);
    calculateT.addCodeLine("j = T[j];", null, 2, null);
    calculateT.addCodeLine("i++;", null, 1, null);
    calculateT.addCodeLine("j++;", null, 1, null);
    calculateT.addCodeLine("T[i] = j", null, 1, null);
    calculateT.addCodeLine("}", null, 0, null);
    calculateT.hide();

    // Source Code for Searching b in a using t:
    SourceCode searching = lang.newSourceCode(new Coordinates(40, 280),
        "searching", null, sourceCodeProperties);
    searching.addCodeLine("int i = 0;", null, 0, null);
    searching.addCodeLine("for (k = 0; k < a.length(); k++) {", null, 0, null);
    searching.addCodeLine("while (i >= 0 && a.charAt(k)!= b.charAt(i))", null,
        1, null);
    searching.addCodeLine("i = T[i];", null, 2, null);
    searching.addCodeLine("i++", null, 1, null);
    searching.addCodeLine("if (i == b.length()) {", null, 1, null);
    searching.addCodeLine("System.out.println(\'Match at : \' + (k - i + 1));",
        null, 2, null);
    searching.addCodeLine("i = T[i];", null, 2, null);
    searching.addCodeLine("}", null, 1, null);
    searching.addCodeLine("}", null, 0, null);
    searching.hide();

    // Introduction of KMP and Description for matrices A, B and T:
    Text t_a = lang.newText(new Coordinates(40, 120), "A:", "a", null);
    Text t_b = lang.newText(new Coordinates(40, 200), "B:", "b", null);
    Text t_t = lang.newText(new Coordinates(40, 240), "T:", "t", null);
    t_b.hide();
    t_a.hide();
    t_t.hide();
    Text title = lang.newText(new Coordinates(0, 0),
        "Knuth-Morris-Prat Algorithm for String Matching.", "des", null);
    title.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15), null, null);

    Text timing = lang.newText(new Coordinates(10, 20),
        "The animation contains: DoubleDxxx steps.", "time", null);
    timing.setFont(new Font(Font.MONOSPACED, Font.ITALIC, 15), null, null);

    lang.nextStep("Description");
    // ///////////////////////////////////
    // Some descriptions added

    String[] intro = {

        "Given a string, and another string called pattern. We have to find where the pattern occurs ",
        "on the string. The very first idea to solve this problem is Brute force. In which we compare ",
        "all substrings of the text. But it takes O(m * n) times, with m as length of the text, and ",
        "n as length of the pattern. If we have a text with milions characters, and a pattern with ",
        "thousands characters. How long will it take? ",
        " ",
        "The main idea of Knutt-Morris-Pratt Algorithm is to reduce the number of comparing iterations ",
        "based on prediction: at which position a next match may begin.",
        " ",
        "Let's imagine, you're now comparing (i,j)-th character of the text with the j-th character of ",
        "the pattern, if they're not the same then you start over again, compare the (i,1)-th character ",
        "with the first character of the pattern. But let's see, now you have a prefix of the pattern on ",
        "the text. From the i-th to (i , j - 1)-th character of the text (because they're all the same at ",
        "the previous comparisions). So is it neccessary to start over again? ",
        " ",
        "You have this prefix called p of the pattern on the text ",
        "Knutt-Morris-Pratt Algorithm firstly computes the longest prefix w of p described as below: ",
        "p = y.w = w.x, in which . means concatenating two strings. So you don't have to start over ",
        "again, but to shift the pattern length(y) positions, and to compare the first character of x ",
        "with the (i,j)-th character of the text. If they're different, shift the prefix (now as w) ",
        "the same way as before." };

    Text[] introArray = new Text[intro.length];
    for (int i = 0; i < intro.length; i++) {
      introArray[i] = lang.newText(new Coordinates(10, 20 * (i + 2)), intro[i],
          "intro" + i, null);
      introArray[i].changeColor(null, Color.blue, null, null);
    }

    // //////////////////////////////////

    // current code line:
    int currentLine = 0;

    // Algorithm:

    lang.nextStep("Pre-calculation");

    // Hide info

    for (int i = 0; i < introArray.length; i++) {
      introArray[i].hide();
    }
    // lang.nextStep();

    Text desc = lang
        .newText(
            new Coordinates(10, 40),
            "Firstly we must calculate the help matrix T. This matrix is used to determine where ",
            "desc1", null);
    Text desc1 = lang
        .newText(
            new Coordinates(10, 65),
            "the next match could begin, thus bypassing re-examination of previously matched characters!",
            "d22", null);

    desc.changeColor(null, Color.blue, null, null);
    desc1.changeColor(null, Color.blue, null, null);

    /*
     * desc.show(); lang.nextStep();
     */

    calculateT.show(); // show source code
    bArray.show(); // show array b
    t_b.show();
    lang.nextStep();

    calculateT.highlight(currentLine);
    // create matrix t:
    int[] t = new int[b.length + 1];
    IntArray T = lang.newIntArray(new Coordinates(70, 240), t, "arrayT", null, // show
        // array
        // T
        ArrayProps); // show Arraymarkers
    jMarker.show();
    t_t.show();
    lang.nextStep();

    calculateT.unhighlight(currentLine); // second code line
    calculateT.highlight(++currentLine);
    t[0] = -1;
    T.highlightCell(0, null, null);
    T.put(0, -1, null, null);
    lang.nextStep();

    T.unhighlightCell(0, null, null);
    calculateT.unhighlight(currentLine); // 3. code line
    currentLine++; //
    calculateT.highlight(currentLine);
    int i = 0;
    iMarker.show();
    int j = t[i];
    lang.nextStep();

    calculateT.unhighlight(currentLine); // 4. code line
    currentLine++; //
    calculateT.highlight(currentLine); //
    lang.nextStep();
    while (i < b.length) { //

      calculateT.unhighlight(currentLine); // 5. code line
      currentLine++;
      calculateT.highlight(currentLine);
      lang.nextStep();
      while (j >= 0 && !b[j].equals(b[i])) { //

        calculateT.unhighlight(currentLine);
        calculateT.highlight(++currentLine);
        T.highlightElem(j, null, null);
        lang.nextStep();

        int tmp = j;
        j = t[j]; // 6. code line
        if (j >= 0 && j < b.length) {
          jMarker.changeColor(null, Color.blue, null, null);
          jMarker.move(j, null, time);
        } else {
          try {
            jMarker.moveTo(AnimalScript.DIRECTION_W, null, new Coordinates(62,
                157), null, time);
          } catch (IllegalDirectionException e) {
            e.printStackTrace();
          }
          jMarker.changeColor(null, Color.red, null, null);
        }
        lang.nextStep();

        T.unhighlightElem(tmp, null, null);
        calculateT.unhighlight(currentLine);
        calculateT.highlight(--currentLine);
        lang.nextStep();
      }

      calculateT.unhighlight(currentLine); // 7. code line
      currentLine += 2;
      calculateT.highlight(currentLine);
      i++;
      if (i < b.length && i >= 0) {
        iMarker.move(i, null, time);
      } else {
        iMarker.changeColor(null, Color.red, null, null);
        iMarker.moveOutside(null, time);
      }
      // 8. code line
      currentLine++;
      calculateT.highlight(currentLine);
      j++;
      jMarker.changeColor(null, Color.BLUE, null, null);
      jMarker.move(j, null, time);
      lang.nextStep();

      calculateT.unhighlight(currentLine - 1);
      calculateT.unhighlight(currentLine); // 9 code line
      currentLine++;
      calculateT.highlight(currentLine);
      T.highlightCell(i, null, null);
      T.put(i, j, null, null);
      t[i] = j;
      lang.nextStep();

      T.unhighlightCell(i, null, null);
      calculateT.unhighlight(currentLine);
      currentLine -= 5;
      calculateT.highlight(currentLine);
      lang.nextStep();
    }
    calculateT.unhighlight(currentLine);
    currentLine += 6;
    calculateT.highlight(currentLine);
    iMarker.hide(); // hide i- and jMarker.
    jMarker.hide();
    desc1.hide();
    desc.hide();
    lang.nextStep();

    calculateT.hide();
    iMarker.hide();
    jMarker.hide();
    desc.hide();
    desc1.hide();
    lang.nextStep("Matching");
    Text t1 = lang.newText(new Coordinates(10, 40),
        "Now we have the help matrix T", "t1", null);
    Text t2 = lang.newText(new Coordinates(10, 65),
        "It will be used to calculate the position of search result", "t2",
        null);
    t2.changeColor(null, Color.blue, null, null);
    t1.changeColor(null, Color.BLUE, null, null);
    t1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13), null, null);
    t2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13), null, null);
    lang.nextStep(1000);

    t_a.show();
    aArray.show();
    searching.show();
    lang.nextStep();
    // iMarker.changeColor(null, Color.BLUE, null, null);
    iMarker.move(0, null, null);
    lang.nextStep(1000);

    iMarker.changeColor(null, Color.blue, null, null);
    iMarker.show();
    amp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "k");
    ArrayMarker kMarker = lang.newArrayMarker(aArray, 0, "kMarker", null, amp);
    kMarker.show();
    currentLine = 0;
    searching.highlight(currentLine);
    i = 0;
    lang.nextStep();

    searching.unhighlight(currentLine);
    searching.highlight(++currentLine);
    // for print out the result
    int tmp = 0;
    int count = 0;
    for (int k = 0; k < aArray.getLength(); k++) { // 2nd code line
      lang.nextStep();

      searching.unhighlight(currentLine);
      searching.highlight(++currentLine);
      lang.nextStep();
      while (i >= 0 && !a[k].equals(b[i])) { // 3rd code line

        searching.unhighlight(currentLine);
        searching.highlight(++currentLine);
        T.highlightCell(i, null, null);
        lang.nextStep();
        count++;
        T.unhighlightCell(i, null, null);
        i = t[i];
        if (i < 0) {
          try {
            iMarker.moveTo(AnimalScript.DIRECTION_W, null, new Coordinates(62,
                157), null, time);
          } catch (IllegalDirectionException e) {
            e.printStackTrace();
          }
          iMarker.changeColor(null, Color.red, null, null);
        } else {
          iMarker.changeColor(null, Color.blue, null, null);
          iMarker.move(i, null, time);
        }
        lang.nextStep();

        searching.unhighlight(currentLine);
        searching.highlight(--currentLine);
        lang.nextStep();
      }
      count++;
      lang.nextStep();

      searching.unhighlight(currentLine);
      currentLine += 2;
      searching.highlight(currentLine);
      i++; // 5th code line
      if (i < bArray.getLength()) {
        iMarker.changeColor(null, Color.blue, null, null);
        iMarker.move(i, null, time);
      } else {
        iMarker.moveOutside(null, time);
        iMarker.changeColor(null, Color.red, null, null);
      }
      lang.nextStep();

      searching.unhighlight(currentLine);
      searching.highlight(++currentLine);
      lang.nextStep();
      if (i == b.length) { // 6th code line
        searching.unhighlight(currentLine);
        searching.highlight(++currentLine);
        Text result = lang.newText(new Coordinates(420, 100 + 20 * tmp),
            ("Match at : " + (k - i + 1)), "tx" + tmp, null);
        result.changeColor(null, Color.GREEN, null, null);
        tmp += 1;
        // print('match at k-i+1 // 7th code line
        lang.nextStep();

        searching.unhighlight(currentLine);
        searching.highlight(++currentLine);
        T.highlightCell(i, null, null);
        lang.nextStep();

        T.unhighlightCell(i, null, null);
        i = t[i]; // 8th code line
        if (i < 0) {
          try {
            iMarker.moveTo(AnimalScript.DIRECTION_W, null, new Coordinates(60,
                59), null, time);
          } catch (IllegalDirectionException e) {
            e.printStackTrace();
          }
          jMarker.changeColor(null, Color.red, null, null);
        } else {
          iMarker.changeColor(null, Color.blue, null, null);
          iMarker.move(i, null, time);
        }
        lang.nextStep();

        searching.unhighlight(currentLine);
        currentLine++;
      } else { // 9th code line
        searching.unhighlight(currentLine);
        currentLine += 3;
      }
      searching.highlight(currentLine);
      lang.nextStep();

      searching.unhighlight(currentLine);
      currentLine -= 7;
      searching.highlight(currentLine);
      if (k == aArray.getLength() - 1) {
        kMarker.moveOutside(null, time);
        kMarker.changeColor(null, Color.red, null, time);
      } else {
        kMarker.move(k + 1, null, time);
      }
    }

    searching.unhighlight(currentLine);
    kMarker.hide();
    lang.nextStep();
    currentLine += 8;
    searching.highlight(currentLine);
    kMarker.hide();
    lang.nextStep("Summary");
    lang.hideAllPrimitives();
    title.show();
    timing.show();
    String[] conclusion = {
        "Complexity :",
        "Knutt-Morris-Pratt Algorithm takes O(m , n) time complexity, with n as length of pattern and m as",
        "length of text. Much better than Brute force! As the example below, the number of comparing iterations",
        "is : " + count + " (include shifting iterations).",
        "But in the worse case, for the text: aaaWbaaaWbaaaWb..., and the pattern : aaaW (W is word of n characters a)",
        "it can be much slower to find the pattern. ",
        "An efficienter algorithm is Boyer Moore.",
        "See http://en.wikipedia.org/wiki/Knuth-Morris-Pratt_algorithm",
        "and http://en.wikipedia.org/wiki/Boyer-Moore_string_search_algorithm",
        "for more informations." };
    Text[] conArray = new Text[conclusion.length];
    for (int k = 0; k < conArray.length; k++) {
      conArray[k] = lang.newText(new Coordinates(10, 20 * (k + 2)),
          conclusion[k], "con" + k, null);
      conArray[k].changeColor(null, Color.blue, null, null);
    }
  }
}
