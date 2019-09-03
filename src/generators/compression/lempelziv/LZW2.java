package generators.compression.lempelziv;

import generators.framework.properties.AnimationPropertiesContainer;
import generators.helpers.AnimatedCompressionAlgorithm;

import java.awt.Color;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Primitive;
import algoanim.primitives.Text;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.PolylineProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class LZW2 extends AnimatedCompressionAlgorithm {
  private static final int      MAX_INPUT_LENGTH = 24;
  private HashMap<String, Text> internalMap;

  public LZW2() {
    this("resources/LZW_Java", Locale.US);
  }

  public LZW2(String aResourceName, Locale aLocale) {
    super(aResourceName, aLocale);
    init();
  }

  public void init() {
    super.init();
    // setup the dictionary matrix
    internalMap = new HashMap<String, Text>(2 * MAX_INPUT_LENGTH + 13);
  }

  public void compress(String[] text) throws LineNotExistsException {
    // trim input to maximum length
    // String ein = "";
    String[] t = new String[Math.min(text.length, MAX_INPUT_LENGTH)];
    for (int i = 0; i < t.length; i++) {
      t[i] = text[i];
      // ein += text[i];
    }

    Text algoinWords = lang.newText(new Coordinates(20, 100),
        translator.translateMessage("inWords"), "inWords", null, tpwords);

    lang.nextStep();
    Text step0 = lang.newText(new Offset(0, 100, header, "SW"),
        translator.translateMessage("step00"), "line0", null, tpsteps);
    Text step01 = lang.newText(new Offset(0, 20, step0, "SW"),
        translator.translateMessage("step01"), "line0", null, tpsteps);
    Text step02 = lang.newText(new Offset(0, 20, step01, "SW"),
        translator.translateMessage("step02"), "line0", null, tpsteps);
    lang.nextStep();
    Text step1 = lang.newText(new Offset(0, 40, step02, "SW"),
        translator.translateMessage("step1"), "line1", null, tpsteps);
    lang.nextStep();
    Text step2 = lang.newText(new Offset(0, 40, step1, "SW"),
        translator.translateMessage("step2"), "line2", null, tpsteps);
    lang.nextStep();
    Text step3 = lang.newText(new Offset(0, 40, step2, "SW"),
        translator.translateMessage("step3"), "line3", null, tpsteps);
    Text step31 = lang.newText(new Offset(0, 20, step3, "SW"),
        translator.translateMessage("step31"), "line31", null, tpsteps);
    lang.nextStep();
    Text step4 = lang.newText(new Offset(0, 40, step31, "SW"),
        translator.translateMessage("step4"), "line4", null, tpsteps);
    Text step41 = lang.newText(new Offset(0, 20, step4, "SW"),
        translator.translateMessage("step41"), "line41", null, tpsteps);
    lang.nextStep();
    algoinWords.hide();
    step0.hide();
    step01.hide();
    step02.hide();
    step1.hide();
    step2.hide();
    step3.hide();
    step31.hide();
    step4.hide();
    step41.hide();

    // install StringArray
    array = lang.newStringArray(new Offset(0, 100, header, "SW"), text,
        "stringArray", null, ap);

    // install code
    code = installCodeBlock("code", "code", new Offset(0, 50, array, "SW"));

    lang.nextStep();

    // start the algorithm
    ArrayMarkerProperties amp = new ArrayMarkerProperties();
    amp.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
    amp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    code.highlight("header");

    lang.nextStep();
    code.toggleHighlight("header", "declare");
    String w = "";
    String k = "";
    String result = "";

    lang.nextStep();
    code.toggleHighlight("declare", "cnt=256");

    lang.nextStep();
    code.toggleHighlight("cnt=256", "hashtable");
    int cnt = 256;

    lang.nextStep();
    code.toggleHighlight("hashtable", "for-init");
    code.highlight("initHT");
    code.highlight("end-for-init");
    // setup dictionary
    Hashtable<String, Integer> dict = new Hashtable<String, Integer>();
    for (int i = 0; i < 256; i++) {
      dict.put(String.valueOf((char) i), i);
    }

    String[][] dictData = new String[MAX_INPUT_LENGTH][2];
    for (int i = 0; i < MAX_INPUT_LENGTH; i++) {
      dictData[i][0] = "" + i;
      dictData[i][1] = "1" + i;
    }
    // int matrixCount = 0;
    Text dictText = null, dictValue = null;

    ArrayMarker am = lang.newArrayMarker(array, 0, "arrayMarker", null, amp);

    Text wLabel = lang.newText(new Offset(0, 50, code, "SW"), "w: ", "w", null,
        tpsteps);
    Text kLabel = lang.newText(new Offset(0, 20, wLabel, "SW"), "k: ", "k",
        null, tpsteps);
    Text outputLabel = lang.newText(new Offset(0, 30, kLabel, "SW"),

    translator.translateMessage("output"), "output", null, tpsteps);
    Text output = lang.newText(new Offset(15, -5, outputLabel, "SE"), "",
        "output", null, tpsteps);
    // int highlightCounter = 0;
    output.changeColor(null, Color.BLUE, null, null);

    PolylineProperties ptrP = new PolylineProperties();
    ptrP.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
    code.unhighlight("end-for-init");
    code.unhighlight("initHT");
    code.unhighlight("for-init");

    for (int i = 0; i < text.length; i++) {
      am.move(i, null, null);
      code.highlight("for-i"); // highlight loop
      lang.nextStep();
      code.toggleHighlight("for-i", "set-k");
      k = text[i];
      kLabel.setText("k:  " + k, null, null);
      lang.nextStep();

      code.toggleHighlight("set-k", "contains?");
      if (dict.containsKey(w + k)) {
        Text currentText = internalMap.get(w + k);
        if (currentText != null)
          currentText.changeColor("color", Color.RED, null, null);
        Text currentValue = internalMap.get(w + k + "_v");
        if (currentValue != null)
          currentValue.changeColor("color", Color.RED, null, null);
        lang.nextStep();
        if (currentText != null)
          currentText.changeColor("color", Color.BLACK, null, null);
        if (currentValue != null)
          currentValue.changeColor("color", Color.BLACK, null, null);
        code.toggleHighlight("contains?", "w+k");
        w = w + k;
        wLabel.setText("w: " + w, null, null);
        lang.nextStep();
        code.unhighlight("w+k");
      } else {
        lang.nextStep();
        code.toggleHighlight("contains?", "else");
        lang.nextStep();
        code.highlight("result");
        result += dict.get(w) + " ";
        output.setText(result, null, null);
        lang.nextStep();
        code.toggleHighlight("result", "dict.put");
        String key = w + k;
        dict.put(key, cnt);
        if (dictText == null) {
          dictText = lang.newText(new Offset(40, -200, code, "NE"), key,
              "dictText" + cnt, null, tpsteps);
          dictValue = lang.newText(
              new Offset(70, 0, dictText, "baseline start"),
              String.valueOf(cnt), "dictVal" + cnt, null, tpsteps);
        } else {
          dictText = lang.newText(new Offset(0, 20, dictText, "SW"), key, ""
              + "dictText" + cnt, null, tpsteps);
          dictValue = lang.newText(new Offset(0, 20, dictValue, "SW"),
              String.valueOf(cnt), "dictVal" + cnt, null, tpsteps);
        }
        internalMap.put(key, dictText);
        internalMap.put(key + "_v", dictValue);
        // highlightCounter++;
        // matrixCount++;

        lang.nextStep();
        code.toggleHighlight("dict.put", "cnt++");
        cnt++;
        lang.nextStep();
        code.toggleHighlight("cnt++", "w=k");
        w = k;
        wLabel.setText("w: " + w, null, null);
        lang.nextStep();
        code.unhighlight("w=k");
        code.unhighlight("else");
      }
    }

    Text fazit1 = lang.newText(new Offset(0, 90, outputLabel, "SW"),
        translator.translateMessage("noDict"), "name", null, tpsteps);

    Text fazit2 = lang.newText(new Offset(0, 20, fazit1, "SW"),
        translator.translateMessage("finish"), "fazit", null, tpsteps);
    lang.nextStep();
    fazit1.hide();
    fazit2.hide();
    wLabel.hide();
    outputLabel.hide();
    output.hide();
    kLabel.hide();
  }

  protected void hideNrStepsArrayCode() {
    super.hideNrStepsArrayCode();
    for (Text t : internalMap.values())
      t.hide();
  }

  public String generate(AnimationPropertiesContainer props,
      Hashtable<String, Object> prims) {
    setUpDefaultElements(props, prims, "stringArray", "code", "code", 0, 20);
    String[] strArray = (String[]) primitives.get("stringArray");
    // new Offset(0, 20, array, AnimalScript.DIRECTION_SW));
    try {
      compress(strArray);
    } catch (LineNotExistsException e) {
      e.printStackTrace();
    }
    wrapUpAnimation();
    lang.finalizeGeneration();
    return lang.getAnimationCode();
  }

  @Override
  public Primitive installAdditionalComponents(String arrayKey, String codeKey,
      String codeName, int dx, int dy) {
    // nothing to be done here...?
    return null;
  }

  @Override
  public Locale getContentLocale() {
    return contentLocale;
  }
}
