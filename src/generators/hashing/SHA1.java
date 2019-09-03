
/*
 * SHA1.java
 * Steven, Simon, 2014 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.hashing;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;

import java.awt.Color;
import java.util.Locale;

import algoanim.primitives.Polyline;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.PolylineProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.CircleProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class SHA1 implements ValidatingGenerator {
  private CircleProperties mainloopPlusCircleProps;
  private Color arrowHighlightColor;
  private Color inputTextHighlightColor;
  private Color intermediateHighlightColor;
  private Color outputTextHighlightColor;
  private int extendWLimit;
  private int mainLoopLimit;
  private Language lang;
  private MatrixProperties extendWMatrixProps;
  private MatrixProperties preprocMatrixProps;
  private Polyline arrowA1;
  private Polyline arrowA2;
  private Polyline arrowA;
  private Polyline arrowB1;
  private Polyline arrowB2;
  private Polyline arrowBF;
  private Polyline arrowC;
  private Polyline arrowCF;
  private Polyline arrowD;
  private Polyline arrowDF;
  private Polyline arrowFSum;
  private Polyline arrowKt;
  private Polyline arrowSumA;
  private Polyline arrowSumFirst;
  private Polyline arrowSumFourth;
  private Polyline arrowSumSecond;
  private Polyline arrowSumThird;
  private Polyline arrowWt;
  private Polyline generateWArrow;
  private PolylineProperties generateWArrowProp;
  private PolylineProperties mainloopArrowProps;
  private PolylineProperties mainloopPlusSignProps;
  private Rect headlineRect;
  private RectProperties headlineRectProps;
  private RectProperties mainloopFRectProps;
  private RectProperties mainloopInputRectProps;
  private RectProperties mainloopOutputRectProps;
  private RectProperties mainloopShiftRectProps;
  private SourceCode extendWCode;
  private SourceCode generateMsg;
  private SourceCode generateWCode;
  private SourceCode generateWText;
  private SourceCode generateWTextResult;
  private SourceCode initHashCode;
  private SourceCode introDescription;
  private SourceCode mainLoopCode;
  private SourceCode outerLoopCode;
  private SourceCode preprocCode;
  private SourceCode resultSourceCode;
  private SourceCodeProperties sourceCodeProps;
  private String message;
  private String[][] extendOutputMatrix;
  private String[][] preprocOutputMatrix;
  private StringMatrix extendMatrix;
  private StringMatrix preprocMatrix;
  private Text comment;
  private Text headlineText;
  private Text inputA;
  private Text inputB;
  private Text inputC;
  private Text inputD;
  private Text inputE;
  private Text outputA;
  private Text outputB;
  private Text outputC;
  private Text outputD;
  private Text outputE;
  private Text preprocDescription;
  private Text preprocDescriptionHex;
  private Text preprocValue;
  private Text preprocValueHex;
  private Text stepTitle;
  private Text valueF;
  private Text valueFE;
  private Text valueK;
  private Text valueRot5;
  private Text valueRot5FE;
  private Text valueRot5FEW;
  private Text valueT;
  private Text valueTemp;
  private Text valueW;
  private TextProperties commentProps;
  private TextProperties headlineTextProps;
  private TextProperties mainloopFTextProps;
  private TextProperties mainloopInputTextProps;
  private TextProperties mainloopIntermediateTextProps;
  private TextProperties mainloopOutputTextProps;
  private TextProperties mainloopShiftTextProps;
  private TextProperties textProps;
  private TextProperties titleProps;
  
  public void init() {
    lang = new AnimalScript("SHA-1 Algorithmus", "Steven Lamarr Reynolds,Simon Bugert", 1000, 600);
    this.lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
    this.arrowHighlightColor = (Color)primitives.get("arrowHighlightColor");
    this.commentProps = (TextProperties)props.getPropertiesByName("commentProps");
    this.extendWLimit = (Integer)primitives.get("extendWLimit");
    this.extendWMatrixProps = (MatrixProperties)props.getPropertiesByName("extendWMatrixProps");
    this.generateWArrowProp = (PolylineProperties)props.getPropertiesByName("generateWArrowProp");
    this.headlineRectProps = (RectProperties)props.getPropertiesByName("headlineRectProps");
    this.headlineTextProps = (TextProperties)props.getPropertiesByName("headlineTextProps");
    this.inputTextHighlightColor = (Color)primitives.get("inputTextHighlightColor");
    this.intermediateHighlightColor = (Color)primitives.get("intermediateHighlightColor");
    this.mainloopArrowProps = (PolylineProperties)props.getPropertiesByName("mainloopArrowProps");
    this.mainloopFRectProps = (RectProperties)props.getPropertiesByName("mainloopFRectProps");
    this.mainloopFTextProps = (TextProperties)props.getPropertiesByName("mainloopFTextProps");
    this.mainloopInputRectProps = (RectProperties)props.getPropertiesByName("mainloopInputRectProps");
    this.mainloopInputTextProps = (TextProperties)props.getPropertiesByName("mainloopInputTextProps");
    this.mainloopIntermediateTextProps = (TextProperties)props.getPropertiesByName("mainloopIntermediateTextProps");
    this.mainLoopLimit = (Integer)primitives.get("mainLoopLimit");
    this.mainloopOutputRectProps = (RectProperties)props.getPropertiesByName("mainloopOutputRectProps");
    this.mainloopOutputTextProps = (TextProperties)props.getPropertiesByName("mainloopOutputTextProps");
    this.mainloopPlusCircleProps = (CircleProperties)props.getPropertiesByName("mainloopPlusCircleProps");
    this.mainloopPlusSignProps = (PolylineProperties)props.getPropertiesByName("mainloopPlusSignProps");
    this.mainloopShiftRectProps = (RectProperties)props.getPropertiesByName("mainloopShiftRectProps");
    this.mainloopShiftTextProps = (TextProperties)props.getPropertiesByName("mainloopShiftTextProps");
    this.message = (String)primitives.get("message");
    this.outputTextHighlightColor = (Color)primitives.get("outputTextHighlightColor");
    this.preprocMatrixProps = (MatrixProperties)props.getPropertiesByName("preprocMatrixProps");
    this.sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProps");
    this.textProps = (TextProperties)props.getPropertiesByName("textProps");
    this.titleProps = (TextProperties)props.getPropertiesByName("titleProps");

    showHeadline();
    showIntroduction();
    hash(message);
    
    return this.lang.toString();
  }

  public void showHeadline() {
    this.headlineText = this.lang.newText(new Coordinates(20, 30),
        "SHA-1-Algorithmus", "headlineText", null, headlineTextProps);
    this.headlineRect = this.lang.newRect(new Offset(-5, -5, "headlineText", "NW"), new Offset(
        5, 5, "headlineText", "SE"), "headlineRect", null, headlineRectProps);
  }

  public void showIntroduction() {
    this.stepTitle = this.lang.newText(new Offset(0, 20, "headlineRect", "SW"), "Beschreibung des Algorithmus", "stepTitle", null, this.titleProps);
    this.introDescription = this.lang.newSourceCode(new Offset(0, 10, this.stepTitle, "SW"), "desc", null, this.sourceCodeProps);
    introDescription.addCodeLine("Der SHA-1 Algorithmus (Secure Hash Algorithm) ist eine kryptologische Hashfunktion,", null, 0, null);
    introDescription.addCodeLine("die 1995 von der National Security Agency in den USA entwickelt wurde.", null, 0, null);
    introDescription.addCodeLine("Ziel war es den schwächeren SHA-0 Algorithmus der zwei Jahre zuvor ", null, 0, null);
    introDescription.addCodeLine("veröffentlich wurde, mit einer Shift Operation zu verbessern.", null, 0, null);
    introDescription.addCodeLine("", null, 0, null);
    introDescription.addCodeLine("Algorithmus erzeugt aus einem Eingabestring einen 160-bit (20 byte) langen Hash.", null, 0, null);
    introDescription.addCodeLine("Der Algorithmus erzeugt aus einem Eingabestring einen 128bit langen Hash.", null, 0, null);
    introDescription.addCodeLine("", null, 0, null);
    introDescription.addCodeLine("Die Schritte des Algorithmus:", null, 0, null);
    introDescription.addCodeLine("1 Preprocessing", null, 1, null);
    introDescription.addCodeLine("1.1 Nachricht in Byte-Array umwandeln", null, 2, null);
    introDescription.addCodeLine("1.2 Eins anfügen", null, 2, null);
    introDescription.addCodeLine("1.3 Mit Nullen auffüllen", null, 2, null);
    introDescription.addCodeLine("1.4 Länge der Originalnachricht anfügen", null, 2, null);
    introDescription.addCodeLine("2 Rahmenschleife", null, 1, null);
    introDescription.addCodeLine("2.1 w[] generieren", null, 2, null);
    introDescription.addCodeLine("2.2 Hauptschleife", null, 2, null);
    introDescription.addCodeLine("2.3 Ergebnis anhängen", null, 2, null);
    introDescription.addCodeLine("3 Ergebnishash berechnen", null, 2, null);
    introDescription.addCodeLine("", null, 0, null);
    introDescription.addCodeLine("", null, 0, null);
    introDescription.addCodeLine("(Aus Platzgründen wird nur der erste 512 bit Block angezeigt.)", null, 0, null);

    this.lang.nextStep("Übersicht");

    introDescription.hide();
  }

  
  
  public void drawDiagram() { 
    this.lang.newRect(new Offset(0, 30, "stepTitle", "SW"), new Offset(70, 50, 
            "stepTitle", "SW"), "rectInputA", null, this.mainloopInputRectProps);
        this.lang.newText(new Offset(0, -15, "rectInputA", "N"), "A", "textInputA", null, this.mainloopInputTextProps);
        this.lang.newRect(new Offset(3, 0, "rectInputA", "NE"), new Offset(73, 0, 
            "rectInputA", "SE"), "rectInputB", null, this.mainloopInputRectProps);
        this.lang.newText(new Offset(0, -15, "rectInputB", "N"), "B", "textInputB", null, this.mainloopInputTextProps);
        this.lang.newRect(new Offset(3, 0, "rectInputB", "NE"), new Offset(73, 0, 
            "rectInputB", "SE"), "rectInputC", null, this.mainloopInputRectProps);
        this.lang.newText(new Offset(0, -15, "rectInputC", "N"), "C", "textInputC", null, this.mainloopInputTextProps);
        this.lang.newRect(new Offset(3, 0, "rectInputC", "NE"), new Offset(73, 0, 
            "rectInputC", "SE"), "rectInputD", null, this.mainloopInputRectProps);
        this.lang.newText(new Offset(0, -15, "rectInputD", "N"), "D", "textInputD", null, this.mainloopInputTextProps);
        this.lang.newRect(new Offset(3, 0, "rectInputD", "NE"), new Offset(73, 0, 
            "rectInputD", "SE"), "rectInputE", null, this.mainloopInputRectProps);
        this.lang.newText(new Offset(0, -15, "rectInputE", "N"), "E", "textInputE", null, this.mainloopInputTextProps);
        
        this.lang.newRect(new Offset(0, 250, "rectInputA", "SW"), new Offset(70, 270, 
            "rectInputA", "SW"), "rectOutputA", null, this.mainloopOutputRectProps);
        this.lang.newRect(new Offset(3, 0, "rectOutputA", "NE"), new Offset(73, 0, 
            "rectOutputA", "SE"), "rectOutputB", null, this.mainloopOutputRectProps);
        this.lang.newRect(new Offset(3, 0, "rectOutputB", "NE"), new Offset(73, 0, 
            "rectOutputB", "SE"), "rectOutputC", null, this.mainloopOutputRectProps);
        this.lang.newRect(new Offset(3, 0, "rectOutputC", "NE"), new Offset(73, 0, 
            "rectOutputC", "SE"), "rectOutputD", null, this.mainloopOutputRectProps);
        this.lang.newRect(new Offset(3, 0, "rectOutputD", "NE"), new Offset(73, 0, 
            "rectOutputD", "SE"), "rectOutputE", null, this.mainloopOutputRectProps);
    
    // path from A
    this.arrowA = this.lang.newPolyline(new Node[] {
        new Offset(15, 0, "rectInputA", "SW"),
        new Offset(15, 200, "rectInputA", "SW"),
        new Offset(0, 0, "rectOutputB", "N")},
        "arrowA1", null, mainloopArrowProps);
    this.lang.newRect(new Offset(-5, 90, "rectInputA", "S"),
        new Offset(45, 110, "rectInputA", "S"), "rectRot5", null, mainloopShiftRectProps);
    this.lang.newText(new Offset(3, 2, "rectRot5", "NW"), "<<<\u2085", "rot5", null, mainloopShiftTextProps);
    this.arrowA1 = this.lang.newPolyline(new Node[] { new Offset(-15, 0, "rectInputA", "SE"),
        new Offset(-15, 90, "rectInputA", "SE")}, "arrowA1", null, mainloopArrowProps);
    this.arrowA2 = this.lang.newPolyline(new Node[] { new Offset(0, 0, "rectRot5", "E"),
        new Offset(-10, 100, "rectInputE", "S")}, "arrowA2", null, mainloopArrowProps);
    
    // path from B
    this.lang.newRect(new Offset(-25, 150, "rectInputB", "S"),
        new Offset(25, 170, "rectInputB", "S"), "rectRot30", null, mainloopShiftRectProps);
    this.lang.newText(new Offset(3, 2, "rectRot30", "NW"), "<<<\u2083\u2080", "rot30", null, mainloopShiftTextProps);
    this.arrowB1 = this.lang.newPolyline(new Node[] { new Offset(0, 0, "rectInputB", "S"),
        new Offset(0, 0, "rectRot30", "N")}, "arrowB1", null, mainloopArrowProps);
    this.arrowB2 = this.lang.newPolyline(new Node[] { new Offset(0, 0, "rectRot30", "S"),
        new Offset(0, 200, "rectInputB", "S"), new Offset(0, 0, "rectOutputC", "N")},
        "arrowB2", null, mainloopArrowProps);
    
    // path from C
    this.arrowC = this.lang.newPolyline(new Node[] { new Offset(0, 0, "rectInputC", "S"),
        new Offset(0, 200, "rectInputC", "S"), new Offset(0, 0, "rectOutputD", "N")},
        "arrowC", null, mainloopArrowProps);      
    
    // path from D
    this.arrowD = this.lang.newPolyline(new Node[] { new Offset(-10, 0, "rectInputD", "SE"),
        new Offset(-10, 200, "rectInputD", "SE"), new Offset(0, 0, "rectOutputE", "N")},
        "arrowD", null, mainloopArrowProps);
    
    // path from E      
    this.lang.newCircle(new Offset(0, 50, "rectInputE", "S"), 10, "sumFirst", null, mainloopPlusCircleProps);
    this.lang.newPolyline(new Node[] { new Offset(0, 3, "sumFirst", "N"), 
        new Offset(0, -3, "sumFirst", "S") }, "p1sumFirst", null, mainloopPlusSignProps);
    this.lang.newPolyline(new Node[] { new Offset(3, 0, "sumFirst", "W"), 
        new Offset(-3, 0, "sumFirst", "E") }, "p2sumFirst", null, mainloopPlusSignProps);
    this.arrowSumFirst = this.lang.newPolyline(new Node[] { new Offset(0, 0, "rectInputE", "S"), 
        new Offset(0, 0, "sumFirst", "N") }, "arrowSumFirst", null, mainloopArrowProps);
    
    this.lang.newCircle(new Offset(0, 100, "rectInputE", "S"), 10, "sumSecond", null, mainloopPlusCircleProps);
    this.lang.newPolyline(new Node[] { new Offset(0, 3, "sumSecond", "N"), 
        new Offset(0, -3, "sumSecond", "S") }, "p1sumSecond", null, mainloopPlusSignProps);
    this.lang.newPolyline(new Node[] { new Offset(3, 0, "sumSecond", "W"), 
        new Offset(-3, 0, "sumSecond", "E") }, "p2sumSecond", null, mainloopPlusSignProps);
    this.arrowSumSecond = this.lang.newPolyline(new Node[] { new Offset(0, 0, "sumFirst", "S"), 
        new Offset(0, 0, "sumSecond", "N") }, "arrowSumSecond", null, mainloopArrowProps);
    
    this.lang.newCircle(new Offset(0, 150, "rectInputE", "S"), 10, "sumThird", null, mainloopPlusCircleProps);
    this.lang.newPolyline(new Node[] { new Offset(0, 3, "sumThird", "N"), 
        new Offset(0, -3, "sumThird", "S") }, "p1sumThird", null, mainloopPlusSignProps);
    this.lang.newPolyline(new Node[] { new Offset(3, 0, "sumThird", "W"), 
        new Offset(-3, 0, "sumThird", "E") }, "p2sumThird", null, mainloopPlusSignProps);
    this.arrowSumThird = this.lang.newPolyline(new Node[] { new Offset(0, 0, "sumSecond", "S"), 
        new Offset(0, 0, "sumThird", "N") }, "arrowSumThird", null, mainloopArrowProps);
    this.arrowWt = this.lang.newPolyline(new Node[] { new Offset(33, 0, "sumThird", "E"), 
        new Offset(0, 0, "sumThird", "E") }, "arrowWt", null, mainloopArrowProps);
    
    this.lang.newCircle(new Offset(0, 200, "rectInputE", "S"), 10, "sumFourth", null, mainloopPlusCircleProps);
    this.lang.newPolyline(new Node[] { new Offset(0, 3, "sumFourth", "N"), 
        new Offset(0, -3, "sumFourth", "S") }, "p1sumFourth", null, mainloopPlusSignProps);
    this.lang.newPolyline(new Node[] { new Offset(3, 0, "sumFourth", "W"), 
        new Offset(-3, 0, "sumFourth", "E") }, "p2sumFourth", null, mainloopPlusSignProps);
    this.arrowSumFourth = this.lang.newPolyline(new Node[] { new Offset(0, 0, "sumThird", "S"), 
        new Offset(0, 0, "sumFourth", "N") }, "arrowSumFourth", null, mainloopArrowProps);
    this.arrowKt = this.lang.newPolyline(new Node[] { new Offset(33, 0, "sumFourth", "E"), 
        new Offset(0, 0, "sumFourth", "E") }, "arrowKt", null, mainloopArrowProps);
    
    this.arrowSumA = this.lang.newPolyline(new Node[] { 
        new Offset(0, 0, "sumFourth", "S"),
        new Offset(0, 10, "sumFourth", "S"),
        new Offset(0, -20, "rectOutputA", "N"),
        new Offset(0, 0, "rectOutputA", "N") },
        "arrowSumA", null, mainloopArrowProps);    

    // F path
    this.lang.newRect(new Offset(-15, 40, "rectInputC", "SE"),
        new Offset(25, 60, "rectInputC", "SE"), "rectF", null, this.mainloopFRectProps);
    this.lang.newText(new Offset(15, 2, "rectF", "NW"), "F", "textF", null, this.mainloopFTextProps);
    this.arrowBF = this.lang.newPolyline(new Node[] { 
        new Offset(-5, 0, "rectInputB", "SE"),
        new Offset(-5, 15, "rectInputB", "SE"),
        new Offset(5, -25, "rectF", "NW"),
        new Offset(5, 0, "rectF", "NW") },
        "arrowBF", null, mainloopArrowProps);
    this.arrowCF = this.lang.newPolyline(new Node[] { 
        new Offset(-5, 0, "rectInputC", "SE"),
        new Offset(-5, 15, "rectInputC", "SE"),
        new Offset(0, -25, "rectF", "N"),
        new Offset(0, 0, "rectF", "N") },
        "arrowCF", null, mainloopArrowProps);
    this.arrowDF = this.lang.newPolyline(new Node[] {
        new Offset(-5, -40, "rectF", "NE"),
        new Offset(-5, 0, "rectF", "NE") },
        "arrowDF", null, mainloopArrowProps);
    this.arrowFSum = this.lang.newPolyline(new Node[] {
        new Offset(0, 0, "rectF", "E"),
        new Offset(0, 0, "sumFirst", "W") },
        "arrowFSum", null, mainloopArrowProps);
    
    // source code
    this.mainLoopCode = this.lang.newSourceCode(new Offset(150, -30, "textInputE", "NE"), "mainLoopCode", null, this.sourceCodeProps);
    this.mainLoopCode.addCodeLine("Hauptschleife:", null, 0, null);
    this.mainLoopCode.addCodeLine("", null, 0, null);
    this.mainLoopCode.addCodeLine("for (int t = 0; t < 80; t++) {", null, 1, null);
    this.mainLoopCode.addCodeLine("int K = 0, F = 0;", null, 2, null);
    
    this.mainLoopCode.addCodeLine("if (0 <= t && t <= 19) {", null, 2, null);
    this.mainLoopCode.addCodeLine("F = (B & C) | ((~B) & D);", null, 3, null);
    this.mainLoopCode.addCodeLine("K = 0x5A827999;", null, 3, null);
    
    this.mainLoopCode.addCodeLine("} else if (20 <= t && t <= 39) {", null, 2, null);
    this.mainLoopCode.addCodeLine("F = B ^ C ^ D;", null, 3, null);
    this.mainLoopCode.addCodeLine("K = 0x6ED9EBA1;", null, 3, null);

    this.mainLoopCode.addCodeLine("} else if (40 <= t && t <= 59) {", null, 2, null);
    this.mainLoopCode.addCodeLine("F = (B & C) | (B & D) | (C & D);", null, 3, null);
    this.mainLoopCode.addCodeLine("K = 0x8F1BBCDC;", null, 3, null);
    
    this.mainLoopCode.addCodeLine("} else if (60 <= t && t <= 79) {", null, 2, null);
    this.mainLoopCode.addCodeLine("F = B ^ C ^ D;", null, 3, null);
    this.mainLoopCode.addCodeLine("K = 0xCA62C1D6;", null, 3, null);
    this.mainLoopCode.addCodeLine("}", null, 2, null);
    
    this.mainLoopCode.addCodeLine("int Temp = F + E + leftrotate(A, 5) + w[t] + K;", null, 2, null);
    this.mainLoopCode.addCodeLine("E = D;", null, 2, null);
    this.mainLoopCode.addCodeLine("D = C;", null, 2, null);
    this.mainLoopCode.addCodeLine("C = leftrotate(B, 30);", null, 2, null);
    this.mainLoopCode.addCodeLine("B = A;", null, 2, null);
    this.mainLoopCode.addCodeLine("A = Temp;", null, 2, null);
    this.mainLoopCode.addCodeLine("}", null, 1, null);
  }

  public void hash(String message) {
    this.stepTitle.setText("1.1 Preprocessing: Nachricht in Byte-Array umwandeln", null, null);

    ///////////////////// STEP 1 ///////////////////////////////////
    
    int H0 = 0x67452301;
    int H1 = 0xEFCDAB89;
    int H2 = 0x98BADCFE;
    int H3 = 0x10325476;
    int H4 = 0xC3D2E1F0;
    byte[] msg = message.getBytes();
    boolean isMoreThanOneBlock = msg.length > 55;
    int original_byte_len = msg.length;
    long original_bit_len = original_byte_len * 8;
    
    this.preprocDescription = this.lang.newText(new Offset(0, 5, this.stepTitle, "SW"), "Eingabetext", "desc1", null, textProps);
    this.preprocValue = this.lang.newText(new Offset(140, 5, this.stepTitle,"SW"), message, "val1", null, textProps);
    
    this.preprocDescriptionHex = this.lang.newText(new Offset(0, 20, this.stepTitle, "SW"), "Eingabetext in hex", "desc2", null, textProps);
    this.preprocValueHex = this.lang.newText(new Offset(140, 20, this.stepTitle,"SW"), ("0x" + toHexString(msg)), "val2", null, textProps);
    
    // Cut off if msg is too long
    if(toHexString(msg).length() > 40) {
      preprocValueHex.setText(("0x" + toHexString(msg)).substring(0, 39).concat(" ..."), null, null);
    }
    
    this.lang.nextStep("1.1 Nachricht in Byte-Array umwandeln");
    this.preprocOutputMatrix = new String[][] {
        { "XX", "XX", "XX", "XX", "XX", "XX", "XX", "XX" },
        { "XX", "XX", "XX", "XX", "XX", "XX", "XX", "XX" },
        { "XX", "XX", "XX", "XX", "XX", "XX", "XX", "XX" },
        { "XX", "XX", "XX", "XX", "XX", "XX", "XX", "XX" },
        { "XX", "XX", "XX", "XX", "XX", "XX", "XX", "XX" },
        { "XX", "XX", "XX", "XX", "XX", "XX", "XX", "XX" },
        { "XX", "XX", "XX", "XX", "XX", "XX", "XX", "XX" },
        { "XX", "XX", "XX", "XX", "XX", "XX", "XX", "XX" } };
    
    
    this.preprocMatrix = this.lang.newStringMatrix(new Offset(0, 50, this.stepTitle, "SW"),
        preprocOutputMatrix, "inputMatrix", null, this.preprocMatrixProps);
    
    this.lang.nextStep();
    
    this.preprocCode = this.lang.newSourceCode(new Offset(0, 280, this.stepTitle, "SW"), "stepOneCode", null, this.sourceCodeProps);
    this.preprocCode.addCodeLine("msg = message.getBytes();", null, 1, null);
    this.preprocCode.addCodeLine("msg = add(msg, (byte) 0x80);", null, 1, null);
    this.preprocCode.addCodeLine("for (int i = 0; i < (56 - (original_byte_len + 1) % 64); i++) {", null, 1, null);
    this.preprocCode.addCodeLine("msg = add(msg, (byte) 0x00);",  null, 2, null);
    this.preprocCode.addCodeLine("}", null, 1, null);
    this.preprocCode.addCodeLine("msg = add(msg, original_bit_len);", null, 1, null);
    
    this.lang.nextStep();
    this.preprocCode.highlight(0);
    
    this.lang.nextStep();
    
    for (int i = 0; i < msg.length; i++) {
      updateDisplayMatrixByte(i, (byte) msg[i]);
    }
    
    this.lang.nextStep();

    ///////////////////// ADD 1 ///////////////////////////////////
    
    this.stepTitle.setText("1.2 Preprocessing: Eins anfügen", null, null);
    
    this.preprocCode.toggleHighlight(0, 1);
    this.lang.nextStep("1.2 Eins anfügen");
    
    // add 1 bit to message
    updateDisplayMatrixByte(msg.length, (byte)0x80);
    msg = add(msg, (byte) 0x80);
    
    this.lang.nextStep();
    this.preprocCode.toggleHighlight(1,2);

    ///////////////////// ADD 0's ///////////////////////////////////
    
    this.stepTitle.setText("1.3 Preprocessing: Mit Nullen auffüllen", null, null);
    
    // append 0 <= k < 512 bits '0', so that the resulting message length (in bits)
    // is equal to 448 (mod 512)
    int times = (((56 - (original_byte_len + 1) % 64) + 64) % 64);

    this.lang.nextStep("1.3 Mit Nullen auffüllen");
    this.preprocCode.toggleHighlight(2, 3);
    
    for (int i = 0; i < times; i++) {
      updateDisplayMatrixByte(msg.length, (byte)0x00);
      msg = add(msg, (byte) 0x00);
    }
    
    this.preprocCode.toggleHighlight(2, 3);
    updateDisplayMatrixByte(msg.length, (byte)0x00);
    
    this.lang.nextStep();
    this.preprocCode.toggleHighlight(3, 4);
    
    this.lang.nextStep();
    this.preprocCode.toggleHighlight(4, 5);

    ///////////////////// ADD LENGTH ///////////////////////////////////
    
    this.stepTitle.setText("1.4 Preprocessing: Länge der Originalnachricht anfügen", null, null);
    
    // append length of message (before pre-processing), in bits, as 64-bit big-endian integer
    msg = add(msg, original_bit_len);
    
    this.lang.nextStep("1.4 Länge der Originalnachricht anfügen");
    updateDisplayMatrixByte(56, (byte)(original_bit_len >> 56));
    updateDisplayMatrixByte(57, (byte)(original_bit_len >> 48));
    updateDisplayMatrixByte(58, (byte)(original_bit_len >> 40));
    updateDisplayMatrixByte(59, (byte)(original_bit_len >> 32));
    updateDisplayMatrixByte(60, (byte)(original_bit_len >> 24));
    updateDisplayMatrixByte(61, (byte)(original_bit_len >> 16));
    updateDisplayMatrixByte(62, (byte)(original_bit_len >> 8));
    updateDisplayMatrixByte(63, (byte)(original_bit_len));
    
    this.lang.nextStep();
    this.preprocCode.unhighlight(5);
    
    this.lang.nextStep();
    this.preprocCode.hide();
    this.preprocMatrix.hide();
    preprocDescription.hide();
    preprocDescriptionHex.hide();
    preprocValue.hide();
    preprocValueHex.hide();

    ///////////////////// RAHMENSCHLEIFE ///////////////////////////////////
    
    this.stepTitle.setText("2 Rahmenschleife", null, null);
    
    this.comment = this.lang.newText(new Offset(0, 15, this.stepTitle, "SW"), "Es werden immer 512-bit Blocks (64 Byte) für jeden Durchlauf der Rahmenschleife benutzt.", "comment", null, commentProps);
    
    this.lang.nextStep("2 Rahmenschleife");
    this.outerLoopCode = this.lang.newSourceCode(new Offset(0, 100, this.stepTitle, "SW"), "stepTwoCode", null, this.sourceCodeProps);
    this.outerLoopCode.addCodeLine("for (int i = 0; i < msg.length; i = i + 64) {", null, 1, null);
    this.outerLoopCode.addCodeLine("//...", null, 2, null);
    this.outerLoopCode.addCodeLine("}", null, 1, null);
    this.outerLoopCode.highlight(0);
    
    this.lang.nextStep();
    this.outerLoopCode.toggleHighlight(0, 1);
    
    this.lang.nextStep();
    this.outerLoopCode.hide();
    
    // Process the message in successive 512-bit chunks:
    for (int i = 0; i < msg.length; i = i + 64) {

      ///////////////////// W[] GENERIEREN ///////////////////////////////////
      
      this.stepTitle.setText("2.1 Rahmenschleife: w[] generieren", null, null);
      
      comment.setText("Das Array w[] wird aus der Nachricht msg und sich selbst errechnet.", null, null);
      
      this.lang.nextStep("2.1 w[] generieren");
      
      this.generateWText = this.lang.newSourceCode(new Offset(0, 30, this.stepTitle, "SW"), "generateWText", null, this.sourceCodeProps);
      this.generateWTextResult = this.lang.newSourceCode(new Offset(0, 30, this.stepTitle, "SW"), "generateWTextResult", null, this.sourceCodeProps);
      this.generateMsg = this.lang.newSourceCode(new Offset(0, 30, this.stepTitle, "SW"), "generateMsg", null, this.sourceCodeProps);

      this.generateMsg.addCodeLine("msg:", null, 20, null);
      this.generateMsg.addCodeLine("", null, 20, null);
      this.generateWText.addCodeLine("", null, 0, null);
      this.generateWText.addCodeLine("", null, 0, null);
      this.generateWTextResult.addCodeLine("", null, 0, null);
      this.generateWTextResult.addCodeLine("", null, 0, null);
      
      // Setup a string to show how w[] is build
      for(int p = 0; p < 16; p++){
        String num = toHexString((msg[i + (p * 4) + 0] << 24) + (msg[i + (p * 4) + 1] << 16)
                + (msg[i + (p * 4) + 2] << 8) + (msg[i + (p * 4) + 3] & 0xff));
        this.generateMsg.addCodeLine(num, null, 20, null);
        this.generateWText.addCodeLine("w[ "+p+" ] = ", null, 0, null);
      }
      this.generateWText.addCodeLine("...", null, 0, null);
      this.generateWText.hide();

      this.lang.nextStep();
      this.generateWCode = this.lang.newSourceCode(new Offset(0, 360, this.stepTitle, "SW"), "generateWCode", null, this.sourceCodeProps);
      this.generateWCode.addCodeLine("int[] w = new int[80];", null, 1, null);
      this.generateWCode.addCodeLine("for (int j = 0; j < 16; j++) {", null, 1, null);
      this.generateWCode.addCodeLine("int value1 = (msg [ i + (j * 4) + 0 ] << 24);", null, 2, null);
      this.generateWCode.addCodeLine("int value2 = (msg [ i + (j * 4) + 1 ] << 16);", null, 2, null);
      this.generateWCode.addCodeLine("int value3 = (msg [ i + (j * 4) + 2 ] <<  8);", null, 2, null);
      this.generateWCode.addCodeLine("int value4 = (msg [ i + (j * 4) + 3 ] & 0xff); ", null, 2, null);
      this.generateWCode.addCodeLine("w[ j ] =  value1 + value2 + value3 + value4;", null, 2, null);
      this.generateWCode.addCodeLine("}", null, 1, null);
      
      this.lang.nextStep();
      this.generateWCode.highlight(0);
      this.generateWText.show();

      this.lang.nextStep();
      this.generateWCode.toggleHighlight(0, 1);
      
      this.lang.nextStep();
      this.generateWCode.unhighlight(1);
      
      // Setup w[]
      int[] w = new int[80];
      for (int j = 0; j < 16; j++) {
        int value1 = (msg[i + (j * 4) + 0] << 24);
        int value2 = (msg[i + (j * 4) + 1] << 16);
        int value3 = (msg[i + (j * 4) + 2] <<  8);
        int value4 = (msg[i + (j * 4) + 3] & 0xff); 
        w[j] =  value1 + value2 + value3 + value4;

        this.generateWCode.highlight(2);
        this.generateWCode.highlight(3);
        this.generateWCode.highlight(4);
        this.generateWCode.highlight(5);
        this.generateWArrow = this.lang.newPolyline(new Node[] {
            new Offset(200, 41+j*16, this.generateWText, "N"),
            new Offset(120, 41+j*16, this.generateWText, "N")},
            "arrowW", null, generateWArrowProp);
        this.generateMsg.highlight(2+j);
        
        this.lang.nextStep();
        this.generateMsg.unhighlight(2+j);
        this.generateWCode.unhighlight(2);
        this.generateWCode.unhighlight(3);
        this.generateWCode.unhighlight(4);
        this.generateWCode.unhighlight(5);
        this.generateWCode.highlight(6);
        this.generateWTextResult.addCodeLine(toHexString(w[j]), null, 7, null);
        this.generateWTextResult.highlight(2+j);
        
        this.lang.nextStep();
        this.generateWTextResult.unhighlight(2+j);
        this.generateWCode.unhighlight(6);
        this.generateWArrow.hide();
      }
      this.generateWCode.highlight(7);
      
      this.lang.nextStep();
      this.generateWCode.unhighlight(7);
      
      this.lang.nextStep();
      this.generateWCode.hide();
      this.generateWTextResult.hide();
      this.generateWText.hide();
      this.generateMsg.hide();
      this.generateWArrow.hide();

      ///////////////////// EXTEND W[] ///////////////////////////////////

      this.stepTitle.setText("2.3 Rahmenschleife: Array W[] erweitern", null, null);
      
      this.extendWCode = this.lang.newSourceCode(new Offset(0, 360, this.stepTitle, "SW"), "stepFiveCode", null, this.sourceCodeProps);
      
      this.extendWCode.addCodeLine("for (int t = 16; t < 80; t++) {", null, 1, null);  
      this.extendWCode.addCodeLine("int x = w[ t - 3 ] ^ w[ t - 8 ] ^ w[ t - 14 ] ^ w[ t - 16 ];", null, 2, null);
      this.extendWCode.addCodeLine("w[ t ] = leftrotate( x, 1 );", null, 2, null);
      this.extendWCode.addCodeLine("}", null, 1, null);
      
      comment.setText("Das Array w[ ] wird von sechszehn 32-bit Zahlen zu achtzig 32-bit Zahlen erweitert.", null, null);
      
      this.extendOutputMatrix = new String[][] {
          { "", ""}, // 0
          { "", ""},
          { "", ""},
          { "", ""},
          { "", ""},
          { "", ""},
          { "", ""},
          { "", ""},
          { "", ""},
          { "", ""},
          { "", ""}}; // 10
      
      this.extendMatrix = this.lang.newStringMatrix(new Offset(0, 50, this.stepTitle, "SW"),
          extendOutputMatrix, "extendMatrix", null, extendWMatrixProps);

      this.extendWCode.highlight(0);
      this.lang.nextStep("2.3 Array W[] erweitern");
      this.extendWCode.unhighlight(0);
      
      // Extend the sixteen 32-bit words into eighty 32-bit words:
      for (int t = 16; t < 80; t++) {
        int x = w[t - 3] ^ w[t - 8] ^ w[t - 14] ^ w[t - 16];
        w[t] = leftrotate(x, 1);
        
        if(t < (16+extendWLimit)){
          // If more then 25 is needed: Uncomment the following:
          if((t-16)%10 == 0){
            for (int j = 0; j < 10; j++) {
              this.extendMatrix.put(j, 0, "", null, null);
              this.extendMatrix.put(j, 1, "", null, null);
            }
          }

          this.extendWCode.highlight(1);
          this.extendMatrix.put((t-16)%10, 0, "w[ " + t + "] =", null, null);
          this.extendMatrix.put((t-16)%10, 1, "leftrotate((w[t - 3] ^ w[t - 8] ^ w[t - 14] ^ w[t - 16]), 1);", null, null);
          
          this.lang.nextStep();
          this.extendMatrix.put((t-16)%10, 1, "leftrotate(( "
            + toHexString(w[t - 3]) +" ^ "+ toHexString(w[t - 8]) +" ^ "+ toHexString(w[t - 14])
            + " ^ "+ toHexString(w[t - 16])+" ), 1);", null, null);
          
          this.lang.nextStep();
          this.extendWCode.toggleHighlight(1, 2);
          this.extendMatrix.put((t-16)%10, 1, "leftrotate( " + toHexString(x) + " , 1)", null, null);
          
          this.lang.nextStep();
          this.extendWCode.unhighlight(2);
          this.extendMatrix.put((t-16)%10, 1, toHexString(w[t]), null, null); 
        }
      }
      
      this.lang.nextStep();
      if(extendWLimit+16 != 79){
        this.extendMatrix.put(9, 1,"usw. bis 79", null, null);
      }
      
      this.lang.nextStep();
      this.extendMatrix.hide();
      this.extendWCode.hide();
      comment.hide();
      
      ///////////////////// HAUPTSCHLEIFE ///////////////////////////////////
      
      this.stepTitle.setText("2.3 Rahmenschleife: Hauptschleife", null, null);
      
      int A = H0;
      int B = H1;
      int C = H2;
      int D = H3;
      int E = H4;
      
      this.initHashCode = this.lang.newSourceCode(new Offset(0, 50, this.stepTitle, "SW"), "code", null, this.sourceCodeProps);
      this.initHashCode.addCodeLine("Am Anfang des Programms wurden noch 5 Variablen mit vorbestimmten Hex Werten definiert:", null, 0, null);
      this.initHashCode.addCodeLine("", null, 0, null);
      this.initHashCode.addCodeLine("int H0 = 0x67452301;", null, 1, null);
      this.initHashCode.addCodeLine("int H1 = 0xEFCDAB89;", null, 1, null);
      this.initHashCode.addCodeLine("int H2 = 0x98BADCFE;", null, 1, null);
      this.initHashCode.addCodeLine("int H3 = 0x10325476;", null, 1, null);
      this.initHashCode.addCodeLine("int H4 = 0xC3D2E1F0;", null, 1, null);
      this.initHashCode.addCodeLine("", null, 0, null);
      
      this.lang.nextStep("2.3 Hauptschleife");
      this.initHashCode.addCodeLine("Diese werden nun in temporäre Variablen für die Hauptschleife übernommen:", null, 0, null);
      this.initHashCode.addCodeLine("", null, 0, null);
      this.initHashCode.addCodeLine("int A = H0;", null, 1, null);
      this.initHashCode.addCodeLine("int B = H1;", null, 1, null);
      this.initHashCode.addCodeLine("int C = H2;", null, 1, null);
      this.initHashCode.addCodeLine("int D = H3;", null, 1, null);
      this.initHashCode.addCodeLine("int E = H4;", null, 1, null);
      
      this.lang.nextStep();
      this.initHashCode.hide();
      
      ///////////////////// HAUPTSCHLEIFE ///////////////////////////////////
      
      /*
       * Mainloop
       */
      
      drawDiagram();
      this.inputA = this.lang.newText(new Offset(3, 2, "rectInputA", "NW"), toHexString(A), "inputA", null, mainloopInputTextProps);
      this.inputB = this.lang.newText(new Offset(3, 2, "rectInputB", "NW"), toHexString(B), "inputB", null, mainloopInputTextProps);
      this.inputC = this.lang.newText(new Offset(3, 2, "rectInputC", "NW"), toHexString(C), "inputC", null, mainloopInputTextProps);
      this.inputD = this.lang.newText(new Offset(3, 2, "rectInputD", "NW"), toHexString(D), "inputD", null, mainloopInputTextProps);
      this.inputE = this.lang.newText(new Offset(3, 2, "rectInputE", "NW"), toHexString(E), "inputE", null, mainloopInputTextProps);
      
      this.outputA = this.lang.newText(new Offset(3, 2, "rectOutputA", "NW"), "", "outputA", null, this.mainloopOutputTextProps);
      this.outputB = this.lang.newText(new Offset(3, 2, "rectOutputB", "NW"), "", "outputB", null, mainloopOutputTextProps);
      this.outputC = this.lang.newText(new Offset(3, 2, "rectOutputC", "NW"), "", "outputC", null, mainloopOutputTextProps);
      this.outputD = this.lang.newText(new Offset(3, 2, "rectOutputD", "NW"), "", "outputD", null, mainloopOutputTextProps);
      this.outputE = this.lang.newText(new Offset(3, 2, "rectOutputE", "NW"), "", "outputE", null, mainloopOutputTextProps);
      
      // intermediate values
      this.valueT = this.lang.newText(new Offset(0, 3, "rectOutputE", "S"), "t = 0", "valueT", null, this.mainloopIntermediateTextProps);
      this.valueF = this.lang.newText(new Offset(3, 3, "rectF", "SW"), "", "valueF", null, this.mainloopIntermediateTextProps);
      this.valueW = this.lang.newText(new Offset(35, -7, "sumThird", "E"), "W[t] = ", "valueW", null, mainloopIntermediateTextProps);
      this.valueK = this.lang.newText(new Offset(35, -7, "sumFourth", "E"), "K[t] = ", "valueK", null, mainloopIntermediateTextProps);
      this.valueFE = this.lang.newText(new Offset(13, 0, "arrowSumSecond", "NE"), "", "valueFE", null, mainloopIntermediateTextProps);
      this.valueRot5 = this.lang.newText(new Offset(33, 0, "arrowA2", "SW"), "", "valueRot5", null, mainloopIntermediateTextProps);
      this.valueRot5FE = this.lang.newText(new Offset(13, 0, "arrowSumThird", "NE"), "", "valueRot5FE", null, mainloopIntermediateTextProps);
      this.valueRot5FEW = this.lang.newText(new Offset(13, 0, "arrowSumFourth", "NE"), "", "valueRot5FEW", null, mainloopIntermediateTextProps);
      this.valueTemp = this.lang.newText(new Offset(3, 3, "sumFourth", "SE"), "", "valueTemp", null, mainloopIntermediateTextProps);
      
      this.lang.nextStep();
      for (int t = 0; t < 80; t++) {
        valueT.setText("t = " + t, null, null);
        int K = 0, F = 0;
        if (0 <= t && t <= 19) {
          F = (B & C) | ((~B) & D);
          K = 0x5A827999;
          
          if (t < mainLoopLimit) {
            mainLoopCode.toggleHighlight(6, 5);
            // highlight: arrowBF, arrowCF, arrowDF, inputB, inputC, inputD
            this.arrowBF.changeColor(null, this.arrowHighlightColor, null, null);
            this.arrowCF.changeColor(null, this.arrowHighlightColor, null, null);
            this.arrowDF.changeColor(null, this.arrowHighlightColor, null, null);
            this.inputB.changeColor(null, this.inputTextHighlightColor, null, null);
            this.inputC.changeColor(null, this.inputTextHighlightColor, null, null);
            this.inputD.changeColor(null, this.inputTextHighlightColor, null, null);
            this.lang.nextStep();
            this.valueF.setText(toHexString(F), null, null);
            // highlight: valueF
            this.valueF.changeColor(null, this.intermediateHighlightColor, null, null);
            this.lang.nextStep();
            this.mainLoopCode.toggleHighlight(5, 6);
            // unhighlight arrowBF, arrowCF, arrowDF, inputB, inputC, inputD, valueF
            this.arrowBF.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
            this.arrowCF.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
            this.arrowDF.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
            this.inputB.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
            this.inputC.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
            this.inputD.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
            this.valueF.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
            // highlight valueK
            this.valueK.changeColor(null, this.intermediateHighlightColor, null, null);
            this.lang.nextStep();
            this.valueK.setText("K[t] = " + toHexString(K), null, null);
            this.lang.nextStep();
            // unhighlight valueK
            this.valueK.changeColor(null,  (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
            this.mainLoopCode.unhighlight(6);
          }
          
        } else if (20 <= t && t <= 39) {
          F = B ^ C ^ D;
          K = 0x6ED9EBA1;
          if (t < mainLoopLimit) {
            this.mainLoopCode.unhighlight(5);
            this.mainLoopCode.unhighlight(6);
            this.mainLoopCode.toggleHighlight(9, 8);
            // highlight: arrowBF, arrowCF, arrowDF, inputB, inputC, inputD
            this.arrowBF.changeColor(null, this.arrowHighlightColor, null, null);
            this.arrowCF.changeColor(null, this.arrowHighlightColor, null, null);
            this.arrowDF.changeColor(null, this.arrowHighlightColor, null, null);
            this.inputB.changeColor(null, this.inputTextHighlightColor, null, null);
            this.inputC.changeColor(null, this.inputTextHighlightColor, null, null);
            this.inputD.changeColor(null, this.inputTextHighlightColor, null, null);
            this.lang.nextStep();
            this.valueF.setText(toHexString(F), null, null);  
            this.mainLoopCode.toggleHighlight(8, 9);
            // unhighlight arrowBF, arrowCF, arrowDF, inputB, inputC, inputD, valueF
            this.arrowBF.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
            this.arrowCF.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
            this.arrowDF.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
            this.inputB.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
            this.inputC.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
            this.inputD.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
            this.valueF.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
            // highlight valueK
            this.valueK.changeColor(null, this.intermediateHighlightColor, null, null);
            this.lang.nextStep();
            this.valueK.setText(toHexString(K), null, null);
            this.lang.nextStep();
            // unhighlight valueK
            this.valueK.changeColor(null,  (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
            this.mainLoopCode.unhighlight(9);
          }
        } else if (40 <= t && t <= 59) {
          F = (B & C) | (B & D) | (C & D);
          K = 0x8F1BBCDC;
          if (t < mainLoopLimit) {
            this.mainLoopCode.unhighlight(8);
            this.mainLoopCode.unhighlight(9);
            this.mainLoopCode.toggleHighlight(12, 11);
            // highlight: arrowBF, arrowCF, arrowDF, inputB, inputC, inputD
            this.arrowBF.changeColor(null, this.arrowHighlightColor, null, null);
            this.arrowCF.changeColor(null, this.arrowHighlightColor, null, null);
            this.arrowDF.changeColor(null, this.arrowHighlightColor, null, null);
            this.inputB.changeColor(null, this.inputTextHighlightColor, null, null);
            this.inputC.changeColor(null, this.inputTextHighlightColor, null, null);
            this.inputD.changeColor(null, this.inputTextHighlightColor, null, null);
            this.lang.nextStep();
            this.valueF.setText(toHexString(F), null, null);
            this.mainLoopCode.toggleHighlight(11, 12);
            // unhighlight arrowBF, arrowCF, arrowDF, inputB, inputC, inputD, valueF
            this.arrowBF.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
            this.arrowCF.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
            this.arrowDF.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
            this.inputB.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
            this.inputC.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
            this.inputD.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
            this.valueF.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
            // highlight valueK
            this.valueK.changeColor(null, this.intermediateHighlightColor, null, null);
            this.lang.nextStep();
            this.valueK.setText(toHexString(K), null, null);
            this.lang.nextStep();
            // unhighlight valueK
            this.valueK.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
            this.mainLoopCode.unhighlight(12);
          }
        } else if (60 <= t && t <= 79) {
          F = B ^ C ^ D;
          K = 0xCA62C1D6;
          if (t < mainLoopLimit) {
            this.mainLoopCode.unhighlight(11);
            this.mainLoopCode.unhighlight(12);
            this.mainLoopCode.toggleHighlight(15, 14);
            // highlight: arrowBF, arrowCF, arrowDF, inputB, inputC, inputD
            this.arrowBF.changeColor(null, this.arrowHighlightColor, null, null);
            this.arrowCF.changeColor(null, this.arrowHighlightColor, null, null);
            this.arrowDF.changeColor(null, this.arrowHighlightColor, null, null);
            this.inputB.changeColor(null, this.inputTextHighlightColor, null, null);
            this.inputC.changeColor(null, this.inputTextHighlightColor, null, null);
            this.inputD.changeColor(null, this.inputTextHighlightColor, null, null);
            this.lang.nextStep();
            this.valueF.setText(toHexString(F), null, null);
            this.mainLoopCode.toggleHighlight(14, 15);
            // unhighlight arrowBF, arrowCF, arrowDF, inputB, inputC, inputD, valueF
            this.arrowBF.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
            this.arrowCF.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
            this.arrowDF.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
            this.inputB.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
            this.inputC.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
            this.inputD.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
            this.valueF.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
            // highlight valueK
            this.valueK.changeColor(null, this.intermediateHighlightColor, null, null);
            this.lang.nextStep();
            this.valueK.setText(toHexString(K), null, null);
            this.lang.nextStep();
            // unhighlight valueK
            this.valueK.changeColor(null,  (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
            this.mainLoopCode.unhighlight(15);
          }
        }      
        
        int Temp = F + E + leftrotate(A, 5) + w[t] + K; // 17
        if (t < mainLoopLimit) {
          this.mainLoopCode.toggleHighlight(22, 17);
          // highlight arrowFSum, arrowSumFirst, valueF, inputE
          this.arrowFSum.changeColor(null, arrowHighlightColor, null, null);
          this.arrowSumFirst.changeColor(null, arrowHighlightColor, null, null);
          this.lang.nextStep();
          this.valueFE.setText(toHexString(F + E), null, null);
          // highlight valueFE
          this.valueFE.changeColor(null, intermediateHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight arrowFSum, arrowSumFirst, valueFE, valueF, inputE
          this.arrowFSum.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowSumFirst.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.valueFE.changeColor(null,  (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          this.valueF.changeColor(null,  (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          this.inputE.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          // highlight inputA, arrowA1
          this.arrowA1.changeColor(null, arrowHighlightColor, null, null);
          this.inputA.changeColor(null, this.inputTextHighlightColor, null, null);
          this.lang.nextStep();
          this.valueRot5.setText(toHexString(leftrotate(A, 5)), null, null);
          // highlight valueRot5
          this.valueRot5.changeColor(null, intermediateHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight inputA, arrowA1
          this.inputA.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.arrowA1.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          // highlight arrowA2, arrowSumSecond, valueFE
          this.arrowA2.changeColor(null, arrowHighlightColor, null, null);
          this.arrowSumSecond.changeColor(null, arrowHighlightColor, null, null);
          this.valueFE.changeColor(null, intermediateHighlightColor, null, null);
          this.lang.nextStep();
          this.valueRot5FE.setText(toHexString(F+E+leftrotate(A, 5)), null, null);
          // highlight valueRot5FE
          this.valueRot5FE.changeColor(null, intermediateHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight arrowA2, arrowSumSecond, valueFE, valueRot5
          this.arrowA2.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowSumSecond.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.valueFE.changeColor(null,  (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          this.valueRot5.changeColor(null,  (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          // highlight arrowSumThird, arrowWt, valueW
          this.arrowSumThird.changeColor(null, arrowHighlightColor, null, null);
          this.arrowWt.changeColor(null, arrowHighlightColor, null, null);
          this.valueW.changeColor(null, intermediateHighlightColor, null, null);
          this.valueW.setText("W[t] = " + toHexString(w[t]), null, null);
          this.lang.nextStep();
          this.valueRot5FEW.setText(toHexString(F+E+leftrotate(A, 5)+w[t]), null, null);
          // highlight valueRot5FEW
          this.valueRot5FEW.changeColor(null, intermediateHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight arrowSumThird, arrowWt, valueW, valueRot5FE
          this.arrowSumThird.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowWt.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.valueRot5FE.changeColor(null,  (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          this.valueW.changeColor(null,  (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          // highlight arrowSumFourth, arrowKt, valueK
          this.arrowSumFourth.changeColor(null, arrowHighlightColor, null, null);
          this.arrowKt.changeColor(null, arrowHighlightColor, null, null);
          this.valueK.changeColor(null, intermediateHighlightColor, null, null);
          this.lang.nextStep();
          this.valueTemp.setText(toHexString(Temp), null, null);
          // highlight valueTemp
          this.valueTemp.changeColor(null, intermediateHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight valueTemp, valueRot5FEW, valueK, arrowSumFourth, arrowKt
          this.valueTemp.changeColor(null,  (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          this.valueRot5FEW.changeColor(null,  (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          this.valueK.changeColor(null,  (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          this.arrowSumFourth.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowKt.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
        }
        
        E = D;
        if (t < mainLoopLimit) {
          this.mainLoopCode.toggleHighlight(17, 18);
          // highlight arrowD, input D
          this.inputD.changeColor(null, inputTextHighlightColor, null, null);
          this.arrowD.changeColor(null, arrowHighlightColor, null, null);
          this.lang.nextStep();
          this.outputE.setText(toHexString(D), null, null);
          // highlight ouputE
          this.outputE.changeColor(null, outputTextHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight arrowD, outputE, inputD
          this.arrowD.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.outputE.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.inputD.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
        }
        
        D = C;
        if (t < mainLoopLimit) {
          this.mainLoopCode.toggleHighlight(18, 19);
          // highlight arrowC, inputC
          this.arrowC.changeColor(null, arrowHighlightColor, null, null);
          this.inputC.changeColor(null, inputTextHighlightColor, null, null);
          this.lang.nextStep();
          this.outputD.setText(toHexString(C), null, null);
          // highlight ouputD
          this.outputD.changeColor(null, outputTextHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight arrowC, outputD, inputC
          this.arrowC.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.outputD.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.inputC.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
        }
        
        C = leftrotate(B, 30);
        if (t < mainLoopLimit) {
          this.mainLoopCode.toggleHighlight(19, 20);
          // highlight arrowB1, arrowB2, inputB
          this.arrowB1.changeColor(null, arrowHighlightColor, null, null);
          this.arrowB2.changeColor(null, arrowHighlightColor, null, null);
          this.inputB.changeColor(null, inputTextHighlightColor, null, null);
          this.lang.nextStep();
          // highlight outputC
          this.outputC.setText(toHexString(C), null, null);
          this.outputC.changeColor(null, outputTextHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight arrowB1, arrowB2, inputB, outputC
          this.arrowB1.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowB2.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.outputC.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.inputB.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
        }
        
        B = A;
        if (t < mainLoopLimit) {
          this.mainLoopCode.toggleHighlight(20, 21);
          // highlight arrowA, inputA
          this.arrowA.changeColor(null, arrowHighlightColor, null, null);
          this.inputA.changeColor(null, inputTextHighlightColor, null, null);
          this.lang.nextStep();
          this.outputB.setText(toHexString(A), null, null);
          // highlight ouputB
          this.outputB.changeColor(null, outputTextHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight arrowA, outputB, inputA
          this.arrowA.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.outputB.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.inputA.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
        }
        
        A = Temp;
        if (t < mainLoopLimit) {
          this.mainLoopCode.toggleHighlight(21, 22);
          // highlight arrowSumA, valueTemp
          this.valueTemp.changeColor(null, this.intermediateHighlightColor, null, null);
          this.arrowSumA.changeColor(null, arrowHighlightColor, null, null);
          this.lang.nextStep();
          this.outputA.setText(toHexString(Temp), null, null);
          // highlight outputA
          this.outputA.changeColor(null, outputTextHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight arrowSumA, valueTemp, outputA
          this.arrowSumA.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.outputA.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.valueTemp.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
        
          this.mainLoopCode.unhighlight(22);
          // highlight outputA-E
          this.outputA.changeColor(null, outputTextHighlightColor, null, null);
          this.outputB.changeColor(null, outputTextHighlightColor, null, null);
          this.outputC.changeColor(null, outputTextHighlightColor, null, null);
          this.outputD.changeColor(null, outputTextHighlightColor, null, null);
          this.outputE.changeColor(null, outputTextHighlightColor, null, null);
          
          this.lang.nextStep();
          // unhighlight outputA-E
          this.outputA.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.outputB.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.outputC.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.outputD.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.outputE.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          // highlight inputA-E
          this.inputA.changeColor(null, inputTextHighlightColor, null, null);
          this.inputB.changeColor(null, inputTextHighlightColor, null, null);
          this.inputC.changeColor(null, inputTextHighlightColor, null, null);
          this.inputD.changeColor(null, inputTextHighlightColor, null, null);
          this.inputE.changeColor(null, inputTextHighlightColor, null, null);
          
          this.inputA.setText(toHexString(A), null, null);
          this.inputB.setText(toHexString(B), null, null);
          this.inputC.setText(toHexString(C), null, null);
          this.inputD.setText(toHexString(D), null, null);
          this.inputE.setText(toHexString(E), null, null);
          this.outputA.setText("", null, null);
          this.outputB.setText("", null, null);
          this.outputC.setText("", null, null);
          this.outputD.setText("", null, null);
          this.outputE.setText("", null, null);
          
          this.valueK.setText("K[t] = ", null, null);
          this.valueW.setText("W[t] = ", null, null);
          this.valueF.setText("", null, null);
          this.valueFE.setText("", null, null);
          this.valueRot5.setText("", null, null);
          this.valueRot5FE.setText("", null, null);
          this.valueRot5FEW.setText("", null, null);
          this.valueTemp.setText("", null, null);
          this.lang.nextStep();
          //unhighlight inputA-E
          this.inputA.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.inputB.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.inputC.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.inputD.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.inputE.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          
          this.lang.nextStep();
        }
      }
      
      this.lang.addLine("hideAll");
      this.headlineText.show();
      this.headlineRect.show();
      this.stepTitle.show();
      this.stepTitle.setText("2.4 Rahmenschleife: Ergebnis anhängen", null, null);
      
      this.resultSourceCode = this.lang.newSourceCode(new Offset(0, 5, this.stepTitle, "SW"), "resultSourceCode", null, this.sourceCodeProps);
      this.resultSourceCode.addCodeLine("Nach jedem Rahmenschleifen-Durchlauf werden die Zwischenergebnisse", null, 0, null);
      this.resultSourceCode.addCodeLine("für A bis E auf H0 bis H4 aufaddiert.", null, 0, null);
      this.resultSourceCode.addCodeLine("Im nächsten Durchlauf werden dann statt der Initialwerte diese Werte für H0 bis H4 verwendet.", null, 0, null);
      this.resultSourceCode.addCodeLine("", null, 0, null);
      
      this.lang.nextStep("2.4 Ergebnis anhängen");
      this.resultSourceCode.addCodeLine("H0 = H0 + A", null, 0, null);
      this.resultSourceCode.addCodeLine("H1 = H1 + B", null, 0, null);
      this.resultSourceCode.addCodeLine("H2 = H2 + C", null, 0, null);
      this.resultSourceCode.addCodeLine("H3 = H3 + D", null, 0, null);
      this.resultSourceCode.addCodeLine("H4 = H4 + E", null, 0, null);
      
      this.lang.nextStep();
      this.resultSourceCode.hide();
      this.resultSourceCode = this.lang.newSourceCode(new Offset(0, 5, this.stepTitle, "SW"), "resultSourceCode", null, this.sourceCodeProps);
      this.resultSourceCode.addCodeLine("Nach jedem Rahmenschleifen-Durchlauf werden die Zwischenergebnisse", null, 0, null);
      this.resultSourceCode.addCodeLine("für A bis E auf H0 bis H4 aufaddiert.", null, 0, null);
      this.resultSourceCode.addCodeLine("Im nächsten Durchlauf werden dann statt der Initialwerte diese Werte für H0 bis H4 verwendet.", null, 0, null);
      this.resultSourceCode.addCodeLine("", null, 0, null);
      this.resultSourceCode.addCodeLine("H0 = " + toHexString(H0) + " + " + toHexString(A), null, 0, null);
      this.resultSourceCode.addCodeLine("H1 = " + toHexString(H1) + " + " + toHexString(B), null, 0, null);
      this.resultSourceCode.addCodeLine("H2 = " + toHexString(H2) + " + " + toHexString(C), null, 0, null);
      this.resultSourceCode.addCodeLine("H3 = " + toHexString(H3) + " + " + toHexString(D), null, 0, null);
      this.resultSourceCode.addCodeLine("H4 = " + toHexString(H4) + " + " + toHexString(E), null, 0, null);
      
      this.lang.nextStep();
      this.resultSourceCode.hide();
      this.resultSourceCode = this.lang.newSourceCode(new Offset(0, 5, this.stepTitle, "SW"), "resultSourceCode", null, this.sourceCodeProps);
      this.resultSourceCode.addCodeLine("Nach jedem Rahmenschleifen-Durchlauf werden die Zwischenergebnisse", null, 0, null);
      this.resultSourceCode.addCodeLine("für A bis E auf H0 bis H4 aufaddiert.", null, 0, null);
      this.resultSourceCode.addCodeLine("Im nächsten Durchlauf werden dann statt der Initialwerte diese Werte für H0 bis H4 verwendet.", null, 0, null);
      this.resultSourceCode.addCodeLine("", null, 0, null);
      this.resultSourceCode.addCodeLine("H0 = " + toHexString(H0 + A), null, 0, null);
      this.resultSourceCode.addCodeLine("H1 = " + toHexString(H1 + B), null, 0, null);
      this.resultSourceCode.addCodeLine("H2 = " + toHexString(H2 + C), null, 0, null);
      this.resultSourceCode.addCodeLine("H3 = " + toHexString(H3 + D), null, 0, null);
      this.resultSourceCode.addCodeLine("H4 = " + toHexString(H4 + E), null, 0, null);
      
      H0 = H0 + A;
      H1 = H1 + B;
      H2 = H2 + C;
      H3 = H3 + D;
      H4 = H4 + E;
      
      this.lang.nextStep();
    }

    ///////////////////// ENDERGEBNISHASH ///////////////////////////////////
    
    this.stepTitle.setText("3 Ergebnishash berechnen", null, null);
    
    // 160 bit number:
    // magic
    String res = toHexString(H0) + toHexString(H1) + toHexString(H2) + toHexString(H3) + toHexString(H4);
    
    this.resultSourceCode.hide();
    this.resultSourceCode = this.lang.newSourceCode(new Offset(0, 5, this.stepTitle, "SW"), "resultSourceCode", null, this.sourceCodeProps);
    
    if (!isMoreThanOneBlock) {
      this.resultSourceCode.addCodeLine("Da nur ein Block der Größe 512 Bit berechnet werden musste,", null, 0, null);
      this.resultSourceCode.addCodeLine("kann sofort das Endergebnis erzeugt werden.", null, 0, null);
      this.resultSourceCode.addCodeLine("", null, 0, null);
    }
    this.resultSourceCode.addCodeLine("Nachdem alle Blöcke abgearbeitet wurden, wird das Ergebnis durch", null, 0, null);
    this.resultSourceCode.addCodeLine("Konkatenation der Variablen H0 bis H4 berechnet.", null, 0, null);
    this.resultSourceCode.addCodeLine("Das Ergebnis ist somit 5 * 32 Bit = 160 Bit lang.", null, 0, null);
    this.resultSourceCode.addCodeLine("", null, 0, null);
    this.resultSourceCode.addCodeLine("result = H0 \u2218 H1 \u2218 H2 \u2218 H3 \u2218 H4", null, 0, null);
    this.resultSourceCode.addCodeLine("(wobei \u2218 die Konkatenation bezeichnet)", null, 1, null);

    this.lang.nextStep("3 Ergebnishash berechnen");
    this.resultSourceCode.addCodeLine("", null, 0, null);
    this.resultSourceCode.addCodeLine("", null, 0, null);
    this.resultSourceCode.addCodeLine("H0 = " + toHexString(H0), null, 0, null);
    this.resultSourceCode.addCodeLine("H1 = " + toHexString(H1), null, 0, null);
    this.resultSourceCode.addCodeLine("H2 = " + toHexString(H2), null, 0, null);
    this.resultSourceCode.addCodeLine("H3 = " + toHexString(H3), null, 0, null);
    this.resultSourceCode.addCodeLine("H4 = " + toHexString(H4), null, 0, null);
    this.resultSourceCode.addCodeLine("", null, 0, null);
    this.resultSourceCode.addCodeLine("", null, 0, null);
    this.resultSourceCode.addCodeLine("SHA('" + message +"') = " + res, null, 0, null);
  }

  /*
   * Helper functions
   */
  public final byte[] add(byte[] one, int two) {
    byte[] temp = new byte[4];
    temp[0] = (byte) (two >> 24);
    temp[1] = (byte) (two >> 16);
    temp[2] = (byte) (two >> 8);
    temp[3] = (byte) (two);
    return add(one, temp);
  }

  public final byte[] add(byte[] one, byte two) {
    byte[] temp = new byte[1];
    temp[0] = two;
    return add(one, temp);
  }

  public final byte[] add(byte[] one, long two) {
    byte[] temp = new byte[8];
    temp[0] = (byte) (two >> 56);
    temp[1] = (byte) (two >> 48);
    temp[2] = (byte) (two >> 40);
    temp[3] = (byte) (two >> 32);
    temp[4] = (byte) (two >> 24);
    temp[5] = (byte) (two >> 16);
    temp[6] = (byte) (two >> 8);
    temp[7] = (byte) (two);
    return add(one, temp);
  }

  public final byte[] add(byte[] one, byte[] two) {
    byte[] combined = new byte[one.length + two.length];

    System.arraycopy(one, 0, combined, 0, one.length);
    System.arraycopy(two, 0, combined, one.length, two.length);

    return combined;
  }

  private final int leftrotate(int x, int count) {
    return ((x << count) | (x >>> (32 - count)));
  }

  private String toHexString(int two) {
    byte[] temp = new byte[4];
    temp[0] = (byte) (two >>> 24);
    temp[1] = (byte) (two >>> 16);
    temp[2] = (byte) (two >>> 8);
    temp[3] = (byte) (two);
    return toHexString(temp);
  }

  private static String toHexString(byte[] b) {
    final String hexChar = "0123456789ABCDEF";

    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < b.length; i++) {
      sb.append(hexChar.charAt((b[i] >> 4) & 0x0f));
      sb.append(hexChar.charAt(b[i] & 0x0f));
    }
    return sb.toString();
  }
  
  /*
   *  Animal functions
   */

  public String getName() {
    return "SHA-1 Algorithmus";
  }

  public String getAlgorithmName() {
    return "SHA-1";
  }

  public String getAnimationAuthor() {
    return "Steven Lamarr Reynolds,Simon Bugert";
  }

  public String getDescription() {
    return "Der SHA-1 Algorithmus (Secure Hash Algorithm) ist eine kryptologische Hashfunktion, "
        + "die 1995 von der National Security Agency in den USA entwickelt wurde. Ziel war es den "
        + "schwächeren SHA-0 Algorithmus der zwei Jahre zuvor veröffentlich wurde zu verbessern. Der "
        + "Algorithmus erzeugt aus einem Eingabestring einen 160-bit (20 byte) langen Hash. Im Jahre "
        + "2005 wurden erste Angriffe auf SHA-1 gefunden. Seitdem wird von der Verwendung wird abgeraten.";
  }

  public String getCodeExample() {
    return "Note 1: All variables are unsigned 32 bits and wrap modulo 232 when calculating, except"
        + "\n"
        + "        ml the message length which is 64 bits, and"
        + "\n"
        + "        hh the message digest which is 160 bits."
        + "\n"
        + "Note 2: All constants in this pseudo code are in big endian."
        + "\n"
        + "        Within each word, the most significant byte is stored in the leftmost byte position"
        + "\n"
        + "\n"
        + "Initialize variables:"
        + "\n"
        + "\n"
        + "h0 = 0x67452301"
        + "\n"
        + "h1 = 0xEFCDAB89"
        + "\n"
        + "h2 = 0x98BADCFE"
        + "\n"
        + "h3 = 0x10325476"
        + "\n"
        + "h4 = 0xC3D2E1F0"
        + "\n"
        + "\n"
        + "ml = message length in bits (always a multiple of the number of bits in a character)."
        + "\n"
        + "\n"
        + "Pre-processing:"
        + "\n"
        + "append the bit '1' to the message i.e. by adding 0x80 if characters are 8 bits. "
        + "\n"
        + "append 0 ≤ k < 512 bits '0', thus the resulting message length (in bits)"
        + "\n"
        + "   is congruent to 448 (mod 512)"
        + "\n"
        + "append ml, in a 64-bit big-endian integer. So now the message length is a multiple of 512 bits."
        + "\n"
        + "\n"
        + "Process the message in successive 512-bit chunks:"
        + "\n"
        + "break message into 512-bit chunks"
        + "\n"
        + "for each chunk"
        + "\n"
        + "    break chunk into sixteen 32-bit big-endian words w[i], 0 ≤ i ≤ 15"
        + "\n"
        + "\n"
        + "    Extend the sixteen 32-bit words into eighty 32-bit words:"
        + "\n"
        + "    for i from 16 to 79"
        + "\n"
        + "        w[i] = (w[i-3] xor w[i-8] xor w[i-14] xor w[i-16]) leftrotate 1"
        + "\n"
        + "\n"
        + "    Initialize hash value for this chunk:"
        + "\n"
        + "    a = h0"
        + "\n"
        + "    b = h1"
        + "\n"
        + "    c = h2"
        + "\n"
        + "    d = h3"
        + "\n"
        + "    e = h4"
        + "\n"
        + "\n"
        + "    Main loop:[39]"
        + "\n"
        + "    for i from 0 to 79"
        + "\n"
        + "        if 0 ≤ i ≤ 19 then"
        + "\n"
        + "            f = (b and c) or ((not b) and d)"
        + "\n"
        + "            k = 0x5A827999"
        + "\n"
        + "        else if 20 ≤ i ≤ 39"
        + "\n"
        + "            f = b xor c xor d"
        + "\n"
        + "            k = 0x6ED9EBA1"
        + "\n"
        + "        else if 40 ≤ i ≤ 59"
        + "\n"
        + "            f = (b and c) or (b and d) or (c and d) "
        + "\n"
        + "            k = 0x8F1BBCDC"
        + "\n"
        + "        else if 60 ≤ i ≤ 79"
        + "\n"
        + "            f = b xor c xor d"
        + "\n"
        + "            k = 0xCA62C1D6"
        + "\n"
        + "\n"
        + "        temp = (a leftrotate 5) + f + e + k + w[i]"
        + "\n"
        + "        e = d"
        + "\n"
        + "        d = c"
        + "\n"
        + "        c = b leftrotate 30"
        + "\n"
        + "        b = a"
        + "\n"
        + "        a = temp"
        + "\n"
        + "\n"
        + "    Add this chunk's hash to result so far:"
        + "\n"
        + "    h0 = h0 + a"
        + "\n"
        + "    h1 = h1 + b "
        + "\n"
        + "    h2 = h2 + c"
        + "\n"
        + "    h3 = h3 + d"
        + "\n"
        + "    h4 = h4 + e"
        + "\n"
        + "\n"
        + "Produce the final hash value (big-endian) as a 160 bit number:"
        + "\n"
        + "hh = (h0 leftshift 128) or (h1 leftshift 96) or (h2 leftshift 64) or (h3 leftshift 32) or h4";
  }

  public String getFileExtension() {
    return "asu";
  }

  public Locale getContentLocale() {
    return Locale.GERMANY;
  }

  public GeneratorType getGeneratorType() {
    return new GeneratorType(GeneratorType.GENERATOR_TYPE_HASHING);
  }

  public String getOutputLanguage() {
    return Generator.JAVA_OUTPUT;
  }
  
  private void updateDisplayMatrixByte(int arrayPos, byte newValue)
  {
    this.preprocMatrix.put(arrayPos / 8, arrayPos % 8, 
        String.format("%02X", new Object[] { Byte.valueOf(newValue) }), null, null);
  }

  public boolean validateInput(AnimationPropertiesContainer properties, Hashtable<String, Object> primitives)
      throws IllegalArgumentException
    {
      if (primitives.get("message") != null) {
        String message = (String) primitives.get("message");
        if(message.length() <= 55 && message.length() >= 0){
          return true;
        }else{
          throw new IllegalArgumentException("Error: Message is too long");
        }
      }
      if( (Integer) primitives.get("extendWLimit") < 1){
        throw new IllegalArgumentException("Error: extendWLimit must be at least 1");
      }
      if( (Integer) primitives.get("mainloopLimit") < 1){
        throw new IllegalArgumentException("Error: mainloopLimit must be at least 1");
      }
      return false;
    }
}
