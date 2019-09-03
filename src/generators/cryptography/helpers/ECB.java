package generators.cryptography.helpers;

import java.awt.Color;
import java.awt.Font;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;

public class ECB {

  private Language lang;
  private int      rint;
  // private Text header;
  // private Rect headerRect;
  private Rect     inRect;
  private Rect     outRect;
  private Text     encrypt;
  private Text     description;
  private Rect     desRect;
  private Text     codeDescr;
  private Rect     codeRect;
  private Text     m;
  private Text     r;
  private Text     e00;
  private Text     e10;
  // private Text e01;
  // private Text e11;
  // private Text e02;
  // private Text e12;
  private Text     rDecr;
  private Text     e00Decr;
  private Text     e10Decr;
  // private Text e01Decr;
  // private Text e11Decr;
  // private Text e02Decr;
  // private Text e12Decr;
  private Text     e;
  // private Text eDecr;
  private Rect     pseudo;
  private Text     i;
  private Text     i2;
  private Text     m_i;
  private Text     c_i;
  private Text     m_i2;
  private Text     c_i2;
  private Text     split;
  private Text     blocks;
  private Text     split2;
  private Text     blocks2;
  private Text     addZeros1;
  private Text     addZeros2;
  private Text     s0;
  // private Text i_1;
  // private Text i_2;
  // private Text i_3;
  // private Text i_4;
  // private Text i_5;
  // private Text i_6;
  // private Text i_7;
  private Text     m_0;
  // private Text m_1;
  // private Text m_2;
  // private Text m_3;
  // private Text m_4;
  // private Text m_5;
  // private Text m_6;
  // private Text m_7;
  private Text     c_0;
  // private Text c_1;
  // private Text c_2;
  // private Text c_3;
  // private Text c_4;
  // private Text c_5;
  // private Text c_6;
  // private Text c_7;
  private Text     i_02;
  // private Text i_12;
  // private Text i_22;
  // private Text i_32;
  // private Text i_42;
  // private Text i_52;
  // private Text i_62;
  // private Text i_72;
  private Text     m_02;
  // private Text m_12;
  // private Text m_22;
  // private Text m_32;
  // private Text m_42;
  // private Text m_52;
  // private Text m_62;
  // private Text m_72;
  private Text     c_02;
  // private Text c_12;
  // private Text c_22;
  // private Text c_32;
  // private Text c_42;
  // private Text c_52;
  // private Text c_62;
  // private Text c_72;
  private Text     cBlocks;
  private Text     c;
  // private Text cBlocks2;
  // private Text c2;
  private Text     cEnd1;
  private Text     cEnd2;
  private Text     decIntro;
  private Text     CodeDescription;
  private Rect     codeRectDecr;
  private Rect     pseudo_1;
  private Text     cBegin;
  // private Text end;
  private Polyline pl1;
  private Polyline pl12;
  private Polyline pl2;
  private Polyline pl22;
  private Polyline pl3;
  private Polyline pl32;
  private Polyline pr1;
  private Polyline pr12;
  private Polyline pr2;
  private Polyline pr22;
  private Polyline pr3;
  private Polyline pr32;
  private Polyline hline;
  private Polyline hline2;
  private Polyline vline1;
  private Polyline vline2;
  private Polyline vline12;
  private Polyline vline22;
  private Polyline lineMi_i;
  private Polyline lineMi_i2;
  private Polyline lineCi_Mi;
  private Polyline lineCi_Mi2;

  private Text     I;
  private Text     M;
  private Text     C;

  private Text     m_animal;
  private Text     r_animal;
  private Text     c_animal;

  // private Text eText;
  // private Text eText2;

  public ECB(Language l) {
    // Store the language object
    lang = l;
    lang.setStepMode(true);
  }

  private String unterstriche() {
    String z = "";

    for (int i = 0; i < rint; i++) {
      z += "_";
    }
    return z;
  }

  private String[] split(String message, int rint, int anzBlocks) {
    String message2 = message;
    while (message2.length() % rint != 0) {
      message2 = message2 + "0";
    }
    String[] blocks = new String[anzBlocks];
    for (int i = 0; i < blocks.length; i++) {
      blocks[i] = "";
    }

    int block = 0;
    int count = 0;

    for (int i = 0; i < message2.length(); i++) {
      blocks[block] += message2.charAt(i);
      count++;
      if (count == rint) {
        count = 0;
        block++;
      }
    }
    return blocks;
  }

  public void crypt(E efunktion, String message_m, int rint, String rint2,
      TextProperties headerProps, TextProperties mProps,
      TextProperties encryptProps, TextProperties cBlocksProps,
      TextProperties codeDescrProps, TextProperties addZProps,
      TextProperties endProps, TextProperties descriptionProps,
      SourceCodeProperties sdProps, SourceCodeProperties deProps,
      SourceCodeProperties sourceProps, SourceCodeProperties codeProps,
      SourceCodeProperties codeDecrProps) {

    RectProperties rectProps = new RectProperties();
    rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
    rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
    rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

    RectProperties rectPropsZ = new RectProperties();
    rectPropsZ.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 1);

    PolylineProperties p = new PolylineProperties();
    p.set(AnimationPropertiesKeys.FWARROW_PROPERTY, false);

    ArrayProperties arrProps = new ArrayProperties();
    arrProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    arrProps.set(AnimationPropertiesKeys.FILL_PROPERTY,
        new Color(192, 192, 192));
    arrProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    arrProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    arrProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.WHITE);
    arrProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(0,
        191, 255));
    arrProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 14));

    ArrayProperties cArrProps = new ArrayProperties();
    cArrProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
    cArrProps.set(AnimationPropertiesKeys.FILL_PROPERTY, new Color(192, 192,
        192));
    cArrProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
    cArrProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
    cArrProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.GREEN);
    cArrProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, new Color(
        255, 10, 0));
    cArrProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif",
        Font.PLAIN, 12));

    lang.newText(new Coordinates(400, 25), "Electronic Codebook Mode (ECB)",
        "header", null, headerProps);
    lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW),
        new Offset(5, 5, "header", "SE"), "headerRect", null, rectProps);

    TicksTiming t = new TicksTiming(0);

    SourceCode descr = lang.newSourceCode(new Coordinates(10, 100), "descr",
        null, deProps);
    descr
        .addCodeLine(
            "Electronic Codebook Mode (ECB)(http://en.wikipedia.org/wiki/Electronic_codebook#Electronic_codebook_.28ECB.29) is a Blockcipher(http://en.wikipedia.org/wiki/Block_cipher)",
            "descr", 0, t);
    descr.addCodeLine("The message is divided into plaintext blocks.", "descr",
        0, t);
    descr.addCodeLine(
        "The blocks being encrypted to get the ciphertext blocks.", "descr", 0,
        t);
    descr
        .addCodeLine(
            "The ciphertext blocks are decrypted, with the same function to get the ciphertext blocks.",
            "descr", 0, t);
    descr
        .addCodeLine(
            "The disadvantage of this method is that identical plaintext blocks are encrypted into identical ciphertext blocks.",
            "descr", 0, t);
    descr.addCodeLine(
        "It is not recommended for use in cryptographic protocols at all.",
        "descr", 0, t);

    inRect = lang.newRect(new Offset(-10, -2, "descr",
        AnimalScript.DIRECTION_NW), new Offset(10, 2, "descr", "SE"), "inRect",
        null, rectPropsZ);
    outRect = lang.newRect(new Offset(-11, -3, "descr",
        AnimalScript.DIRECTION_NW), new Offset(11, 3, "descr", "SE"),
        "outRect", null, rectPropsZ);

    lang.nextStep();

    // hide
    descr.hide();
    inRect.hide();
    outRect.hide();

    encrypt = lang.newText(new Offset(0, 130, "header", "NW"), "encryption",
        "encrypt", null, encryptProps);

    lang.nextStep();

    encrypt.hide();

    description = lang.newText(new Offset(10, 40, "inRect", "SW"),
        "Description", "description", null, descriptionProps);

    SourceCode sDescr = lang.newSourceCode(new Offset(0, 10, "description",
        "SW"), "sDescr", null, sdProps);
    sDescr.addCodeLine("initalisation", "sDescr", 0, null);
    sDescr.addCodeLine("        - choose a message m to encode", "sDescr", 0,
        null);
    sDescr
        .addCodeLine(
            "        - choose an encryption function E. Usually E is a permutation key",
            "sDescr", 0, null);
    sDescr.addCodeLine("        - choose an r with 1 <= r <= n", "sDescr", 0,
        null);
    sDescr
        .addCodeLine(
            "        - split the given text into blocks of length r. So we get m = m_1 m_2 m_3 ... m_j.",
            "sDescr", 0, null);
    sDescr
        .addCodeLine(
            "          Assuming block m_j has not length of r. If so, we add zeros to the end of m_j",
            "sDescr", 0, null);
    sDescr
        .addCodeLine("          until it has length of r.", "sDescr", 0, null);
    sDescr.addCodeLine("", "sDescr", 0, null);
    sDescr.addCodeLine("iteration", "sDescr", 0, null);
    sDescr.addCodeLine("        - calculate c_i = E(m_i)", "sDescr", 0, null);
    sDescr.addCodeLine("", "sDescr", 0, null);

    desRect = lang.newRect(new Offset(-10, -5, "description", "NW"),
        new Offset(10, 5, "sDescr", "SE"), "desRect", null, rectPropsZ);

    lang.nextStep();

    codeDescr = lang.newText(new Offset(820, 0, "description", "NW"),
        "Pseudocode", "codeDescr", null, codeDescrProps);

    // now, create the source code entity

    SourceCode code = lang.newSourceCode(new Offset(0, 0, "codeDescr", "SW"),
        "code", null, codeProps);
    code.addCodeLine("def ECB(m, r):", "src", 1, null);
    code.addCodeLine("# split text into blocks of length of r", "src", 1, null);
    code.addCodeLine("blocks = splitText(m, r)", "src", 1, null);
    code.addCodeLine("c = Array(blocks.length())", "src", 1, null);
    code.addCodeLine("", "src", 1, null);
    code.addCodeLine("for each block in blocks:", "src", 1, null);
    code.addCodeLine("c[i] = E(m_i)", "src", 2, null);
    code.addCodeLine("return c", "src", 2, null);

    codeRect = lang.newRect(new Offset(-10, -5, "codeDescr", "NW"), new Offset(
        10, 5, "code", "SE"), "codeRect", null, rectPropsZ);

    lang.nextStep("Pseudocode(encrypt)");

    // hide
    desRect.hide();
    description.hide();
    sDescr.hide();
    codeRect.hide();
    codeDescr.hide();
    code.hide();

    // texte
    m_animal = lang.newText(new Coordinates(20, 110), "m=", "m_animal", null,
        mProps);
    m = lang.newText(new Coordinates(40, 110), message_m, "m", null, mProps);

    r_animal = lang.newText(new Offset(0, 20, "m_animal", "SW"), "r=",
        "r_animal", null, mProps);
    r = lang.newText(new Offset(0, 20, "m", "SW"), rint2, "r", null, mProps);

    e = lang
        .newText(new Offset(120, 0, "m", "E"), "E(x) = ", "e", null, mProps);

    Text[] EHideUp = new Text[rint];
    Text[] EHideDown = new Text[rint];

    if (efunktion.isPermutation()) {

      e00 = lang.newText(new Offset(50, 0, "e", "NE"), "1", "e[0][0]", null,
          mProps);

      for (int i = 0; i < rint - 1; i++) {
        Text ex = lang.newText(new Offset(20, 0, "e[0][" + i + "]", "NE"), ""
            + (i + 2), "e[0][" + (i + 1) + "]", null, mProps);
        EHideUp[i] = ex;
      }

      e10 = lang.newText(new Offset(0, 20, "e[0][0]", "S"),
          ((String[]) efunktion.stringRepresentation())[0], "e[1][0]", null,
          mProps);

      for (int i = 0; i < rint - 1; i++) {
        Text ex = lang.newText(new Offset(20, 0, "e[1][" + i + "]", "NE"),
            ((String[]) efunktion.stringRepresentation())[i + 1], "e[1]["
                + (i + 1) + "]", null, mProps);
        EHideDown[i] = ex;
      }

      pl1 = lang.newPolyline(new Node[] { new Offset(-10, -5, "e[0][0]", "NW"),
          new Offset(-10, 5, "e[1][0]", "SW") }, "pl1", null, p);
      pl2 = lang.newPolyline(new Node[] { new Offset(0, 0, "pl1", "NW"),
          new Offset(10, 0, "pl1", "NW") }, "pl2", null, p);
      pl3 = lang.newPolyline(new Node[] { new Offset(0, 0, "pl1", "SW"),
          new Offset(10, 0, "pl1", "SW") }, "pl3", null, p);

      int polylineend = (int) rint - 1;
      pr1 = lang.newPolyline(new Node[] {
          new Offset(10, -5, "e[0][" + polylineend + "]", "NE"),
          new Offset(10, 5, "e[1][" + polylineend + "]", "SE") }, "pr1", null,
          p);
      pr2 = lang.newPolyline(new Node[] { new Offset(0, 0, "pr1", "NE"),
          new Offset(-10, 0, "pr1", "NE") }, "pr2", null, p);
      pr3 = lang.newPolyline(new Node[] { new Offset(0, 0, "pr1", "SE"),
          new Offset(-10, 0, "pr1", "SE") }, "pr3", null, p);

    } else {

      lang.newText(new Offset(40, 0, "e", "E"),
          (String) efunktion.stringRepresentation(), "eText", null, mProps);
    }

    // codegroup "src"
    // now, create the source code entity
    SourceCode source = lang.newSourceCode(new Offset(0, 130, "r", "SW"),
        "src", null, sourceProps);
    source.addCodeLine("def ECB(m, r):", "src", 1, null);
    source.addCodeLine("# split text into blocks of length of r", "src", 1,
        null);
    source.addCodeLine("blocks = splitText(m, r)", "src", 1, null);
    source.addCodeLine("c = Array(blocks.length())", "src", 1, null);
    source.addCodeLine("", "src", 1, null);
    source.addCodeLine("for each block in blocks:", "src", 1, null);
    source.addCodeLine("c[i] = E(m_i)", "src", 2, null);
    source.addCodeLine("", "src", 1, null);
    source.addCodeLine("return c", "src", 2, null);

    pseudo = lang.newRect(new Offset(-10, -2, "src", "NW"), new Offset(10, 2,
        "src", "SE"), "pseudo", null, rectPropsZ);

    source.highlight(0, 0, false);

    i = lang.newText(new Offset(120, 0, "src", "NE"), "i", "i", null, mProps);
    lineMi_i = lang.newPolyline(new Node[] { new Offset(30, -5, "i", "NE"),
        new Offset(30, 297, "i", "NE") }, "lineMi_i", null, p);

    m_i = lang.newText(new Offset(30, 5, "lineMi_i", "NE"), "m_i", "m_i", null,
        mProps);
    lineCi_Mi = lang.newPolyline(new Node[] { new Offset(30, -5, "m_i", "NE"),
        new Offset(30, 297, "m_i", "NE") }, "lineCi_Mi", null, p);

    c_i = lang.newText(new Offset(30, 5, "lineCi_Mi", "NE"), "c_i", "c_i",
        null, mProps);

    hline = lang.newPolyline(new Node[] { new Offset(-35, 5, "i", "SW"),
        new Offset(30, 5, "c_i", "SE") }, "hline", null, p);
    vline1 = lang.newPolyline(new Node[] {
        new Offset(-120, 0, "lineMi_i", "NW"),
        new Offset(-120, 0, "lineMi_i", "SW") }, "vline1", null, p);
    vline2 = lang.newPolyline(new Node[] { new Offset(1, 0, "vline1", "NW"),
        new Offset(1, 0, "vline1", "SW") }, "vline2", null, p);

    lang.nextStep();

    split = lang.newText(new Offset(0, 40, "r", "SW"),
        "split m in blocks of length " + rint + ":", "split", null, mProps);
    blocks = lang.newText(new Offset(0, 40, "split", "SW"), "blocks = ",
        "blocks", null, mProps);

    // definiere input
    int anzBlocks = message_m.length() / rint;
    if (message_m.length() % rint != 0) {
      anzBlocks++;
    }

    String[] s = new String[anzBlocks];

    // String[] s={"___","___","___","___","___","___","___","___"};

    for (int i = 0; i < anzBlocks; i++) {
      s[i] = unterstriche();
    }

    StringArray arr = lang.newStringArray(new Offset(10, 0, "blocks", "NE"), s,
        "arr", null, arrProps);

    source.toggleHighlight(0, 0, false, 2, 0);
    // this statement is equivalent to
    // src.unhighlight(0, 0, false);
    // src.highlight(2, 0, false);

    lang.nextStep("split m(encrypt)");

    String[] m_splitted = split(message_m, rint, anzBlocks);
    for (int i = 0; i < anzBlocks; i++) {
      arr.put(i, m_splitted[i], null, null);
      lang.nextStep();
    }

    addZeros1 = lang.newText(new Offset(15, -25, "arr", "NE"),
        "if the last block hasn't length of r", "addZeros1", null, addZProps);
    addZeros2 = lang.newText(new Offset(0, 1, "addZeros1", "SW"),
        "we add zeros until it has length of  r", "addZeros2", null, addZProps);

    lang.nextStep();

    addZeros1.hide(new TicksTiming(200));
    addZeros2.hide(new TicksTiming(200));

    lang.nextStep();

    source.toggleHighlight(2, 0, false, 3, 0);
    lang.nextStep("Begin encryption");

    source.toggleHighlight(3, 0, false, 5, 0);

    s0 = lang.newText(new Offset(0, 6, "i", "SW"), "0", "0", null, mProps);
    lang.nextStep();

    arr.highlightElem(0, null, null);

    ArrayMarker marker = lang.newArrayMarker(arr, 0, "marker", null);
    m_0 = lang.newText(new Offset(-6, 6, "m_i", "SW"), m_splitted[0], "m_0",
        null, mProps);
    lang.nextStep();

    String[] cipherText = new String[anzBlocks];

    source.toggleHighlight(5, 0, false, 6, 0);
    c_0 = lang.newText(new Offset(-6, 6, "c_i", "SW"),
        efunktion.encrypt(m_splitted[0].toString()), "c_0", null, mProps);
    lang.nextStep();

    cipherText[0] = efunktion.encrypt(m_splitted[0].toString());

    arr.highlightCell(0, null, null);
    lang.nextStep("step_1(encrypt)");

    Text[] IHide = new Text[anzBlocks];
    Text[] MHide = new Text[anzBlocks];
    Text[] CHide = new Text[anzBlocks];
    for (int i = 0; i < anzBlocks - 1; i++) {
      String si = "" + (i + 1);
      source.toggleHighlight(6, 0, false, 5, 0);
      I = lang.newText(new Offset(0, 6, "" + i, "SW"), si, si, null, mProps);
      lang.nextStep();
      IHide[i] = I;

      arr.highlightElem(i + 1, null, null);
      marker.move(i + 1, null, null);
      M = lang.newText(new Offset(0, 6, "m_" + i, "SW"), m_splitted[i + 1],
          "m_" + (i + 1), null, mProps);
      lang.nextStep();
      MHide[i] = M;

      source.toggleHighlight(5, 0, false, 6, 0);
      C = lang.newText(new Offset(0, 6, "c_" + i, "SW"),
          efunktion.encrypt(m_splitted[i + 1]), "c_" + (i + 1), null, mProps);
      lang.nextStep();
      CHide[i] = C;

      cipherText[i + 1] = efunktion.encrypt(m_splitted[i + 1]);

      arr.highlightCell(i + 1, null, null);
      lang.nextStep();
    }

    source.toggleHighlight(6, 0, false, 8, 0);

    lang.nextStep("step_8(last step in encrypt)");

    for (int i = 0; i < anzBlocks - 1; i++) {
      IHide[i].hide();
      MHide[i].hide();
      CHide[i].hide();
    }

    for (int i = 0; i < rint - 1; i++) {
      EHideUp[i].hide();
      EHideDown[i].hide();
    }

    m_animal.hide();
    r_animal.hide();
    s0.hide();
    m_0.hide();
    c_0.hide();
    m.hide();
    r.hide();
    e.hide();
    e00.hide();
    e10.hide();
    pseudo.hide();
    blocks.hide();
    split.hide();
    arr.hide();
    pl1.hide();
    pl2.hide();
    pl3.hide();
    pr1.hide();
    pr2.hide();
    pr3.hide();
    hline.hide();
    vline1.hide();
    vline2.hide();
    lineMi_i.hide();
    lineCi_Mi.hide();
    i.hide();
    c_i.hide();
    m_i.hide();

    source.hide(t);

    cBlocks = lang.newText(new Coordinates(40, 110),
        "The return value is the following array:", "cBlocks", null,
        cBlocksProps);
    c = lang.newText(new Offset(0, 20, "cBlocks", "SW"), "c = ", "c", null,
        cBlocksProps);

    // definiere input
    String[] cS = cipherText;// {"110","001","101","001","011","111","000","010"};

    StringArray cArr = lang.newStringArray(new Offset(8, 0, "c", "NE"), cS,
        "cArr", null, cArrProps);

    String cipherString = "";
    for (int i = 0; i < anzBlocks; i++) {
      cipherString = cipherString + cipherText[i];
    }

    cEnd1 = lang.newText(new Offset(0, 40, "c", "SW"),
        "so we Get the ciphertext c =" + cipherString, "cEnd1", null,
        cBlocksProps);
    cEnd2 = lang.newText(new Offset(0, 5, "cEnd1", "SW"),
        "of the given message m =" + message_m, "cEnd2", null, cBlocksProps);

    lang.nextStep("Begin decryption");

    cBlocks.hide();
    c.hide();
    cEnd1.hide();
    cEnd2.hide();
    cArr.hide();

    // Anfang Decryption

    decIntro = lang.newText(new Offset(0, 130, "header", "NW"), "Decryption",
        "decIntro", null, encryptProps);

    lang.nextStep();

    decIntro.hide();
    CodeDescription = lang.newText(new Offset(0, 130, "header", "NW"),
        "in Pseudocode", "CodeDescription", null, codeDescrProps);

    SourceCode codeDecr = lang.newSourceCode(new Offset(0, 0,
        "CodeDescription", "SW"), "codeDecr", null, codeProps);

    codeDecr.addCodeLine("def ECB(m, r):", "codeDecr", 1, null);
    codeDecr.addCodeLine("# split text into blocks of length of r", "codeDecr",
        1, null);
    codeDecr.addCodeLine("blocks = splitText(m, r)", "codeDecr", 1, null);
    codeDecr.addCodeLine("c = Array(blocks.length())", "codeDecr", 1, null);
    codeDecr.addCodeLine("", "codeDecr", 1, null);
    codeDecr.addCodeLine("for each block in blocks:", "codeDecr", 1, null);
    codeDecr.addCodeLine("c[i] = E(m_i)", "codeDecr", 2, null);
    codeDecr.addCodeLine("return c", "codeDecr", 1, null);

    codeRectDecr = lang.newRect(new Offset(-10, -5, "CodeDescription", "NW"),
        new Offset(10, 5, "codeDecr", "SE"), "codeRectDecr", null, rectPropsZ);

    lang.nextStep();
    CodeDescription.hide();
    codeDecr.hide();
    codeRectDecr.hide();

    c_animal = lang.newText(new Coordinates(20, 110), "c =", "c_animal", null,
        cBlocksProps);
    cBegin = lang.newText(new Coordinates(40, 110), cipherString, "cBegin",
        null, mProps);
    r_animal = lang.newText(new Offset(0, 20, "c_animal", "SW"), "r=",
        "r_animal2", null, mProps);
    rDecr = lang.newText(new Offset(0, 20, "cBegin", "SW"), rint2, "rDecr",
        null, mProps);

    e = lang.newText(new Offset(120, 0, "cBegin", "E"), "E(x) = ", "e2", null,
        mProps);

    if (efunktion.isPermutation()) {

      e00Decr = lang.newText(new Offset(50, 0, "e2", "NE"), "1", "e[0][0]Decr",
          null, mProps);

      for (int i = 0; i < rint - 1; i++) {
        Text ex = lang.newText(new Offset(20, 0, "e[0][" + i + "]Decr", "NE"),
            "" + (i + 2), "e[0][" + (i + 1) + "]Decr", null, mProps);
        EHideUp[i] = ex;
      }

      e10Decr = lang.newText(new Offset(0, 20, "e[0][0]", "S"),
          ((String[]) efunktion.stringRepresentation())[0], "e[1][0]Decr",
          null, mProps);

      for (int i = 0; i < rint - 1; i++) {
        Text ex = lang.newText(new Offset(20, 0, "e[1][" + i + "]Decr", "NE"),
            ((String[]) efunktion.stringRepresentation())[i + 1], "e[1]["
                + (i + 1) + "]Decr", null, mProps);
        EHideDown[i] = ex;
      }

      pl12 = lang.newPolyline(new Node[] {
          new Offset(-10, -5, "e[0][0]", "NW"),
          new Offset(-10, 5, "e[1][0]", "SW") }, "pl12", null, p);
      pl22 = lang.newPolyline(new Node[] { new Offset(0, 0, "pl1", "NW"),
          new Offset(10, 0, "pl1", "NW") }, "pl22", null, p);
      pl32 = lang.newPolyline(new Node[] { new Offset(0, 0, "pl1", "SW"),
          new Offset(10, 0, "pl1", "SW") }, "pl32", null, p);

      int polylineend = (int) rint - 1;
      pr12 = lang.newPolyline(new Node[] {
          new Offset(10, -5, "e[0][" + polylineend + "]", "NE"),
          new Offset(10, 5, "e[1][" + polylineend + "]", "SE") }, "pr12", null,
          p);
      pr22 = lang.newPolyline(new Node[] { new Offset(0, 0, "pr1", "NE"),
          new Offset(-10, 0, "pr1", "NE") }, "pr22", null, p);
      pr32 = lang.newPolyline(new Node[] { new Offset(0, 0, "pr1", "SE"),
          new Offset(-10, 0, "pr1", "SE") }, "pr32", null, p);

    } else {
      lang.newText(new Offset(40, 0, "e", "NE"),
          (String) efunktion.stringRepresentation(), "eText", null, mProps);
    }

    // descr
    SourceCode codeDecr2 = lang.newSourceCode(
        new Offset(0, 130, "rDecr", "SW"), "codeDecr2", null, codeDecrProps);

    codeDecr2.addCodeLine("def ECB(m, r):", "codeDecr2", 1, null);
    codeDecr2.addCodeLine("# split text into blocks of length of r",
        "codeDecr2", 1, null);
    codeDecr2.addCodeLine("blocks = splitText(m, r)", "codeDecr2", 1, null);
    codeDecr2.addCodeLine("c = Array(blocks.length())", "codeDecr2", 1, null);
    codeDecr2.addCodeLine("", "codeDecr2", 1, null);
    codeDecr2.addCodeLine("for each block in blocks:", "codeDecr2", 1, null);
    codeDecr2.addCodeLine("c[i] = E(m_i)", "codeDecr2", 2, null);
    codeDecr2.addCodeLine("", "codeDecr2", 1, null);
    codeDecr2.addCodeLine("return c", "codeDecr2", 1, null);

    pseudo_1 = lang.newRect(new Offset(-10, -2, "codeDecr2", "NW"), new Offset(
        10, 2, "codeDecr2", "SE"), "codeRectDecr2", null, rectPropsZ);

    codeDecr2.highlight(0, 0, false);

    i2 = lang.newText(new Offset(120, 0, "codeDecr2", "NE"), "i", "i2", null,
        mProps);
    lineMi_i2 = lang.newPolyline(new Node[] { new Offset(30, -5, "i2", "NE"),
        new Offset(30, 297, "i2", "NE") }, "lineMi_i2", null, p);

    m_i2 = lang.newText(new Offset(30, 5, "lineMi_i2", "NE"), "m_i", "m_i2",
        null, mProps);
    lineCi_Mi2 = lang.newPolyline(new Node[] {
        new Offset(30, -5, "m_i2", "NE"), new Offset(30, 297, "m_i2", "NE") },
        "lineCi_Mi2", null, p);

    c_i2 = lang.newText(new Offset(30, 5, "lineCi_Mi2", "NE"), "c_i", "c_i2",
        null, mProps);

    hline2 = lang.newPolyline(new Node[] { new Offset(-35, 5, "i2", "SW"),
        new Offset(30, 5, "c_i2", "SE") }, "hline2", null, p);
    vline12 = lang.newPolyline(new Node[] {
        new Offset(-120, 0, "lineMi_i2", "NW"),
        new Offset(-120, 0, "lineMi_i2", "SW") }, "vline12", null, p);
    vline22 = lang.newPolyline(new Node[] { new Offset(1, 0, "vline12", "NW"),
        new Offset(1, 0, "vline12", "SW") }, "vline22", null, p);

    lang.nextStep();

    split2 = lang.newText(new Offset(0, 40, "rDecr", "SW"),
        "split m in blocks of length " + rint + ":", "split2", null, mProps);
    blocks2 = lang.newText(new Offset(0, 40, "split2", "SW"), "blocks = ",
        "blocks2", null, mProps);

    String[] s2 = new String[anzBlocks];

    // String[] s={"___","___","___","___","___","___","___","___"};

    for (int i = 0; i < anzBlocks; i++) {
      s2[i] = unterstriche();
    }
    StringArray arr2 = lang.newStringArray(new Offset(10, 0, "blocks2", "NE"),
        s2, "arr2", null, arrProps);

    codeDecr2.toggleHighlight(0, 0, false, 2, 0);

    lang.nextStep("split c(decrypt)");

    for (int i = 0; i < anzBlocks; i++) {
      arr2.put(i, cipherText[i], null, null);
      lang.nextStep();
    }

    lang.nextStep();

    codeDecr2.toggleHighlight(2, 0, false, 3, 0);

    lang.nextStep();

    codeDecr2.toggleHighlight(3, 0, false, 5, 0);

    i_02 = lang.newText(new Offset(0, 6, "i2", "SW"), "0", "02", null, mProps);
    lang.nextStep();

    arr2.highlightElem(0, null, null);
    ArrayMarker marker2 = lang.newArrayMarker(arr2, 0, "marker2", null);
    m_02 = lang.newText(new Offset(-6, 6, "m_i2", "SW"), cipherText[0], "m_02",
        null, mProps);
    lang.nextStep();

    codeDecr2.toggleHighlight(5, 0, false, 6, 0);
    c_02 = lang.newText(new Offset(-6, 6, "c_i2", "SW"),
        "" + efunktion.encrypt(cipherText[0].toString()), "c_02", null, mProps);
    lang.nextStep();

    arr2.highlightCell(0, null, null);
    lang.nextStep("step_1(decrypt)");

    for (int i = 0; i < anzBlocks - 1; i++) {
      String si = "" + (i + 1);
      codeDecr.toggleHighlight(6, 0, false, 5, 0);
      I = lang.newText(new Offset(0, 6, i + "2", "SW"), si, si + "2", null,
          mProps);
      lang.nextStep();
      IHide[i] = I;

      arr2.highlightElem(i + 1, null, null);
      marker2.move(i + 1, null, null);
      M = lang.newText(new Offset(0, 6, "m_" + i + "2", "SW"),
          cipherText[i + 1], "m_" + (i + 1) + "2", null, mProps);
      lang.nextStep();
      MHide[i] = M;

      codeDecr.toggleHighlight(5, 0, false, 6, 0);
      C = lang.newText(new Offset(0, 6, "c_" + i + "2", "SW"),
          "" + efunktion.encrypt(cipherText[i + 1].toString()), "c_" + (i + 1)
              + "2", null, mProps);
      lang.nextStep();
      CHide[i] = C;

      arr2.highlightCell(i + 1, null, null);
      lang.nextStep();
    }

    for (int i = 0; i < anzBlocks - 1; i++) {
      IHide[i].hide();
      MHide[i].hide();
      CHide[i].hide();
    }

    for (int i = 0; i < rint - 1; i++) {
      EHideUp[i].hide();
      EHideDown[i].hide();
    }
    i_02.hide();
    m_02.hide();
    c_02.hide();
    cBegin.hide();
    c_animal.hide();
    rDecr.hide();
    r_animal.hide();
    e.hide();
    e00Decr.hide();
    e10Decr.hide();
    pseudo_1.hide();
    blocks2.hide();
    split2.hide();
    arr2.hide();
    pl12.hide();
    pl22.hide();
    pl32.hide();
    pr12.hide();
    pr22.hide();
    pr32.hide();
    hline2.hide();
    vline12.hide();
    vline22.hide();
    lineMi_i2.hide();
    lineCi_Mi2.hide();
    i2.hide();
    c_i2.hide();
    m_i2.hide();
    codeDecr2.hide(t);

    lang.newText(new Coordinates(40, 110),
        "The return value is the following array:", "cBlocks2", null,
        cBlocksProps);
    lang.newText(new Offset(0, 20, "cBlocks2", "SW"), "c = ", "c2", null,
        cBlocksProps);

    String[] cS2 = m_splitted;// {"110" ,"001", "011", "001", "101", "111",
                              // "000", "100"};

    lang.newStringArray(new Offset(8, 0, "c2", "NE"), cS2, "cArr2", null,
        cArrProps);
    lang.nextStep("last step in encrypt");

    lang.newText(new Offset(0, 70, "c2", "SW"), "FINISH!!!!", "end", null,
        endProps);

  }
}
