
/*
 * SHA256.java
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
import algoanim.properties.CircleProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.PolylineProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

public class SHA256 implements ValidatingGenerator {
  private CircleProperties mainloopPlusCircleProps;
  private Color arrowHighlightColor;
  private Color inputTextHighlightColor;
  private Color intermediateHighlightColor;
  private Color outputTextHighlightColor;
  private Integer extendWLimit;
  private Integer mainLoopLimit;
  private Language lang;
  private MatrixProperties extendWMatrixProps;
  private MatrixProperties preprocMatrixProps;
  private Polyline generateWArrow;
  private PolylineProperties generateWArrowProps;
  private PolylineProperties mainloopArrowProps;
  private PolylineProperties mainloopPlusSignProps;
  private Rect headlineRect;
  private RectProperties headlineRectProps;
  private RectProperties mainloopChRectProps;
  private RectProperties mainloopE0RectProps;
  private RectProperties mainloopE1RectProps;
  private RectProperties mainloopInputRectProps;
  private RectProperties mainloopMaRectProps;
  private RectProperties mainloopOutputRectProps;
  private SourceCode extendWCode;
  private SourceCode generateMsg;
  private SourceCode generateWCode;
  private SourceCode generateWText;
  private SourceCode generateWTextResult;
  private SourceCode mainLoopCode;
  private SourceCode outerLoopCode;
  private SourceCode preprocCode;
  private SourceCode resultSourceCode;
  private SourceCode variableInitCode;
  private SourceCode variableValues;
  private SourceCodeProperties descriptionProps;
  private SourceCodeProperties sourceCodeProps;
  private String message;
  private String[][] extendWMatrix2;
  private String[][] extendWMatrix;
  private String[][] preprocDisplayMatrix;
  private StringMatrix extendMatrix2;
  private StringMatrix extendMatrix;
  private StringMatrix preprocMatrix;
  private Text comment;
  private Text headlineText;
  private Text inputA;
  private Text inputB;
  private Text inputC;
  private Text inputD;
  private Text inputE;
  private Text inputF;
  private Text inputG;
  private Text inputH;
  private Text outputA;
  private Text outputB;
  private Text outputC;
  private Text outputD;
  private Text outputE;
  private Text outputF;
  private Text outputG;
  private Text outputH;
  private Text preprocDescription;
  private Text preprocDescriptionHex;
  private Text preprocValue;
  private Text preprocValueHex;
  private Text stepTitle;
  private Text valueCh;
  private Text valueE0;
  private Text valueE1;
  private Text valueK;
  private Text valueMa;
  private Text valueW;
  private TextProperties commentProps;
  private TextProperties headlineTextProps;
  private TextProperties mainloopChTextProps;
  private TextProperties mainloopE0TextProps;
  private TextProperties mainloopE1TextProps;
  private TextProperties mainloopInputTextProps;
  private TextProperties mainloopIntermediateTextProps;
  private TextProperties mainloopMaTextProps;
  private TextProperties textProps;
  private TextProperties titleProps;
  private SourceCode rightRotateCode;
  private TextProperties mainloopOutputTextProps;
  private Text valueT;
  private Text valueChH;
  private Polyline arrowA;
  private Polyline arrowB;
  private Polyline arrowC;
  private Polyline arrowD;
  private Polyline arrowSumD;
  private Polyline arrowE;
  private Polyline arrowF;
  private Polyline arrowG;
  private Polyline arrowCh;
  private Polyline arrowSumFirstB;
  private Polyline arrowE1;
  private Polyline arrowMa;
  private Polyline arrowE0;
  private Polyline arrowSumFourth;
  private Polyline arrowSumFirstA;
  private Polyline arrowECh;
  private Polyline arrowFCh;
  private Polyline arrowGCh;
  private Polyline arrowSumSecondD;
  private Polyline arrowE1E;
  private Polyline arrowEMa;
  private Polyline arrowFMa;
  private Polyline arrowGMa;
  private Polyline arrowE0A;
  private Polyline arrowW;
  private Polyline arrowK;
  private Text valueWK;
  private Polyline arrowSumFirst;
  private Text valueTemp1;
  private Text valueTemp2;
  private Polyline arrowSumFifth;
  private Polyline arrowSumSecond;
  private Polyline arrowABase;
  private Polyline arrowEBase1;
  private Polyline arrowEBase2;
  private Polyline arrowFBase1;
  private Polyline arrowGBase1;
  private Polyline arrowFBase2;
  private Polyline arrowEBase3;
  private Polyline arrowGBase2;
  private Polyline arrowSumSecondBase;

  public void init() {
    lang = new AnimalScript("SHA-256 Algorithmus", "Steven Lamarr Reynolds,Simon Bugert",
        1200, 600);

    this.lang.setStepMode(true);
  }

  public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
    this.arrowHighlightColor = (Color)primitives.get("arrowHighlightColor");
    this.commentProps = (TextProperties)props.getPropertiesByName("commentProps");
    this.extendWLimit = (Integer)primitives.get("extendWLimit");
    this.extendWMatrixProps = (MatrixProperties)props.getPropertiesByName("extendWMatrixProps");
    this.generateWArrowProps = (PolylineProperties)props.getPropertiesByName("generateWArrowProps");
    this.headlineRectProps = (RectProperties)props.getPropertiesByName("headlineRectProps");
    this.headlineTextProps = (TextProperties)props.getPropertiesByName("headlineTextProps");
    this.inputTextHighlightColor = (Color)primitives.get("inputTextHighlightColor");
    this.intermediateHighlightColor = (Color)primitives.get("intermediateHighlightColor");
    this.mainloopArrowProps = (PolylineProperties)props.getPropertiesByName("mainloopArrowProps");
    this.mainloopChRectProps = (RectProperties)props.getPropertiesByName("mainloopChRectProps");
    this.mainloopChTextProps = (TextProperties)props.getPropertiesByName("mainloopChTextProps");
    this.mainloopE0RectProps = (RectProperties)props.getPropertiesByName("mainloopE0RectProps");
    this.mainloopE0TextProps = (TextProperties)props.getPropertiesByName("mainloopE0TextProps");
    this.mainloopE1RectProps = (RectProperties)props.getPropertiesByName("mainloopE1RectProps");
    this.mainloopE1TextProps = (TextProperties)props.getPropertiesByName("mainloopE1TextProps");
    this.mainloopInputRectProps = (RectProperties)props.getPropertiesByName("mainloopInputRectProps");
    this.mainloopInputTextProps = (TextProperties)props.getPropertiesByName("mainloopInputTextProps");
    this.mainloopOutputTextProps = (TextProperties)props.getPropertiesByName("mainloopOutputTextProps");
    this.mainloopIntermediateTextProps = (TextProperties)props.getPropertiesByName("mainloopIntermediateTextProps");
    this.mainLoopLimit = (Integer)primitives.get("mainloopLimit");
    this.mainloopMaRectProps = (RectProperties)props.getPropertiesByName("mainloopMaRectProps");
    this.mainloopMaTextProps = (TextProperties)props.getPropertiesByName("mainloopMaTextProps");
    this.mainloopOutputRectProps = (RectProperties)props.getPropertiesByName("mainloopOutputRectProps");
    this.mainloopPlusCircleProps = (CircleProperties)props.getPropertiesByName("mainloopPlusCircleProps");
    this.mainloopPlusSignProps = (PolylineProperties)props.getPropertiesByName("mainloopPlusSignProps");
    this.message = (String)primitives.get("message");
    this.outputTextHighlightColor = (Color)primitives.get("outputTextHighlightColor");
    this.preprocMatrixProps = (MatrixProperties)props.getPropertiesByName("preprocMatrixProps");
    this.sourceCodeProps = (SourceCodeProperties)props.getPropertiesByName("sourceCodeProps");
    this.textProps = (TextProperties)props.getPropertiesByName("textProps");
    this.titleProps = (TextProperties)props.getPropertiesByName("titleProps");
    this.descriptionProps = (SourceCodeProperties)props.getPropertiesByName("descriptionProps");

    showHeadline();
    showIntroduction();

    hash(message);

    return this.lang.toString();
  }

  public void showHeadline() {
    this.headlineText = this.lang.newText(new Coordinates(20, 30),
        "SHA-256-Algorithmus", "headlineText", null, headlineTextProps);
    this.headlineRect = this.lang.newRect(new Offset(-5, -5, "headlineText", "NW"), new Offset(
        5, 5, "headlineText", "SE"), "headlineRect", null, headlineRectProps);
  }

  public void showIntroduction() {
    this.stepTitle = this.lang.newText(new Offset(0, 20, "headlineRect", "SW"),
        "Beschreibung des Algorithmus", "stepTitle", null, titleProps);
    SourceCode desc = this.lang.newSourceCode(new Offset(0, 10, this.stepTitle,"SW"), "desc", null, this.descriptionProps);
    desc.addCodeLine("Der SHA-256 Algorithmus (Secure Hash Algorithm) ist eine kryptologische Hashfunktion,", null, 0,null);
    desc.addCodeLine("und eine Variante von SHA-2 die 2001 von der National Security Agency in den USA veröffentlicht wurde.", null, 0, null);
    desc.addCodeLine("Ziel war es den schwächeren SHA-1 Algorithmus um einen längeren Hash-Resultat", null, 0, null);
    desc.addCodeLine("mit 224, 256, 384 oder 512 bits zu verbessern.", null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine("Daher der Name dieser Variante: Der SHA-256 Algorithmus erzeugt aus einem Eingabestring ", null, 0, null);
    desc.addCodeLine("einen 256-bit (32 byte) langen Hash.", null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine("Die Schritte des Algorithmus:", null, 0, null);
    desc.addCodeLine("1 Preprocessing", null, 1, null);
    desc.addCodeLine("1.1 Hash-Werte und Konstanten initalisieren", null, 2, null);
    desc.addCodeLine("1.2 Nachricht in Byte-Array umwandeln", null, 2, null);
    desc.addCodeLine("1.3 Eins anfügen", null, 2, null);
    desc.addCodeLine("1.4 Mit Nullen auffüllen", null, 2, null);
    desc.addCodeLine("1.5 Länge der Originalnachricht anfügen", null, 2, null);
    desc.addCodeLine("2 Rahmenschleife", null, 1, null);
    desc.addCodeLine("2.1 w[] generieren", null, 2, null);
    desc.addCodeLine("2.2 w[] erweitern", null, 2, null);
    desc.addCodeLine("2.3 Hauptschleife", null, 2, null);
    desc.addCodeLine("2.4 Ergebnis anhängen", null, 2, null);
    desc.addCodeLine("3   Ergebnishash berechnen", null, 2, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine("", null, 0, null);
    desc.addCodeLine("(Aus Platzgründen wird nur der erste 512 bit Block angezeigt.)", null, 0, null);

    this.lang.nextStep("Übersicht");

    desc.hide();
  }

  
  
  public void drawDiagram() {    
    this.lang.newRect(new Offset(0, 30, "stepTitle", "SW"), new Offset(70, 50, 
        "stepTitle", "SW"), "rectInputA", null, mainloopInputRectProps);
    this.lang.newText(new Offset(0, -15, "rectInputA", "N"), "A", "textInputA", null, mainloopInputTextProps);
    this.lang.newRect(new Offset(3, 0, "rectInputA", "NE"), new Offset(73, 0, 
        "rectInputA", "SE"), "rectInputB", null, mainloopInputRectProps);
    this.lang.newText(new Offset(0, -15, "rectInputB", "N"), "B", "textInputB", null, mainloopInputTextProps);
    this.lang.newRect(new Offset(3, 0, "rectInputB", "NE"), new Offset(73, 0, 
        "rectInputB", "SE"), "rectInputC", null, mainloopInputRectProps);
    this.lang.newText(new Offset(0, -15, "rectInputC", "N"), "C", "textInputC", null, mainloopInputTextProps);
    this.lang.newRect(new Offset(3, 0, "rectInputC", "NE"), new Offset(73, 0, 
        "rectInputC", "SE"), "rectInputD", null, mainloopInputRectProps);
    this.lang.newText(new Offset(0, -15, "rectInputD", "N"), "D", "textInputD", null, mainloopInputTextProps);
    this.lang.newRect(new Offset(3, 0, "rectInputD", "NE"), new Offset(73, 0, 
        "rectInputD", "SE"), "rectInputE", null, mainloopInputRectProps);
    this.lang.newText(new Offset(0, -15, "rectInputE", "N"), "E", "textInputE", null, mainloopInputTextProps);
    this.lang.newRect(new Offset(3, 0, "rectInputE", "NE"), new Offset(73, 0, 
        "rectInputE", "SE"), "rectInputF", null, mainloopInputRectProps);
    this.lang.newText(new Offset(0, -15, "rectInputF", "N"), "F", "textInputF", null, mainloopInputTextProps);
    this.lang.newRect(new Offset(3, 0, "rectInputF", "NE"), new Offset(73, 0, 
        "rectInputF", "SE"), "rectInputG", null, mainloopInputRectProps);
    this.lang.newText(new Offset(0, -15, "rectInputG", "N"), "G", "textInputG", null, mainloopInputTextProps);
    this.lang.newRect(new Offset(3, 0, "rectInputG", "NE"), new Offset(73, 0, 
        "rectInputG", "SE"), "rectInputH", null, mainloopInputRectProps);
    this.lang.newText(new Offset(0, -15, "rectInputH", "N"), "H", "textInputH", null, mainloopInputTextProps);
    
    this.lang.newRect(new Offset(0, 300, "rectInputA", "SW"), new Offset(70, 320,
        "rectInputA", "SW"), "rectOutputA", null, mainloopOutputRectProps);
    this.lang.newRect(new Offset(3, 0, "rectOutputA", "NE"), new Offset(73, 0,
        "rectOutputA", "SE"), "rectOutputB", null, mainloopOutputRectProps);
    this.lang.newRect(new Offset(3, 0, "rectOutputB", "NE"), new Offset(73, 0,
        "rectOutputB", "SE"), "rectOutputC", null, mainloopOutputRectProps);
    this.lang.newRect(new Offset(3, 0, "rectOutputC", "NE"), new Offset(73, 0,
        "rectOutputC", "SE"), "rectOutputD", null, mainloopOutputRectProps);
    this.lang.newRect(new Offset(3, 0, "rectOutputD", "NE"), new Offset(73, 0,
        "rectOutputD", "SE"), "rectOutputE", null, mainloopOutputRectProps);
    this.lang.newRect(new Offset(3, 0, "rectOutputE", "NE"), new Offset(73, 0,
        "rectOutputE", "SE"), "rectOutputF", null, mainloopOutputRectProps);
    this.lang.newRect(new Offset(3, 0, "rectOutputF", "NE"), new Offset(73, 0,
        "rectOutputF", "SE"), "rectOutputG", null, mainloopOutputRectProps);
    this.lang.newRect(new Offset(3, 0, "rectOutputG", "NE"), new Offset(73, 0,
        "rectOutputG", "SE"), "rectOutputH", null, mainloopOutputRectProps);
    
    
    // path from A
    this.arrowA = this.lang.newPolyline(new Node[] { new Offset(0, 240, "rectInputA", "S"),
        new Offset(0, 250, "rectInputA", "S"), new Offset(0, 0, "rectOutputB", "N")},
        "arrowA", null, mainloopArrowProps);
    this.arrowE0A = this.lang.newPolyline(new Node[] { new Offset(0, 240, "rectInputA", "S"),
        new Offset(0, 240, "rectInputH", "S") }, "arrowE0A", null, mainloopArrowProps);
    
    // path from B
    this.arrowB = this.lang.newPolyline(new Node[] { new Offset(0, 0, "rectInputB", "S"),
        new Offset(0, 250, "rectInputB", "S"), new Offset(0, 0, "rectOutputC", "N")},
        "arrowB", null, mainloopArrowProps); 
    
    // path from C
    this.arrowC = this.lang.newPolyline(new Node[] { new Offset(0, 0, "rectInputC", "S"),
        new Offset(0, 250, "rectInputC", "S"), new Offset(0, 0, "rectOutputD", "N")},
        "arrowC", null, mainloopArrowProps);     
    
    // path from D
    this.lang.newCircle(new Offset(0, 150, "rectInputD", "S"), 10, "sumD", null, this.mainloopPlusCircleProps);
    this.lang.newPolyline(new Node[] { new Offset(0, 3, "sumD", "N"), 
        new Offset(0, -3, "sumD", "S") }, "p1sumD", null, this.mainloopPlusSignProps);
    this.lang.newPolyline(new Node[] { new Offset(3, 0, "sumD", "W"), 
        new Offset(-3, 0, "sumD", "E") }, "p2sumD", null, this.mainloopPlusSignProps);
    this.arrowD = this.lang.newPolyline(new Node[] { new Offset(0, 0, "rectInputD", "S"), 
        new Offset(0, 0, "sumD", "N") }, "arrowD", null, mainloopArrowProps);
    this.arrowSumD = this.lang.newPolyline(new Node[] { new Offset(0, 0, "sumD", "S"),
        new Offset(0, 250, "rectInputD", "S"), new Offset(0, 0, "rectOutputE", "N")},
        "arrowSumD", null, this.mainloopArrowProps);
        
    // CH
    this.lang.newRect(new Offset(0, 50, "rectInputH", "S"),
        new Offset(30, 90, "rectInputH", "S"), "rectCh", null, this.mainloopChRectProps);
    this.lang.newText(new Offset(5, 2, "rectCh", "NW"), "Ch", "textCh", null, this.mainloopChTextProps);
    
    // E1
    this.lang.newRect(new Offset(0, 100, "rectInputH", "S"),
        new Offset(30, 120, "rectInputH", "S"), "rectE1", null, this.mainloopE1RectProps);
    this.lang.newText(new Offset(5, 2, "rectE1", "NW"), "\u03A3"+"1", "textE1", null, this.mainloopE1TextProps);
    
    // Ma
    this.lang.newRect(new Offset(0, 180, "rectInputH", "S"),
        new Offset(30, 220, "rectInputH", "S"), "rectMa", null, this.mainloopMaRectProps);
    this.lang.newText(new Offset(5, 2, "rectMa", "NW"), "Ma", "textMa", null, this.mainloopMaTextProps);
    
    // E0
    this.lang.newRect(new Offset(0, 230, "rectInputH", "S"),
        new Offset(30, 250, "rectInputH", "S"), "rectE0", null, this.mainloopE0RectProps);
    this.lang.newText(new Offset(5, 2, "rectE0", "NW"), "\u03A3"+"0", "textE0", null, this.mainloopE0TextProps);

    // Sums
    this.lang.newCircle(new Offset(80, 0, "rectCh", "E"), 10, "sumFirstA", null, mainloopPlusCircleProps);
    this.lang.newPolyline(new Node[] { new Offset(0, 3, "sumFirstA", "N"), 
        new Offset(0, -3, "sumFirstA", "S") }, "p1sumFirstA", null, mainloopPlusSignProps);
    this.lang.newPolyline(new Node[] { new Offset(3, 0, "sumFirstA", "W"), 
        new Offset(-3, 0, "sumFirstA", "E") }, "p2sumFirstA", null, mainloopPlusSignProps);
    this.arrowCh = this.lang.newPolyline(new Node[] { new Offset(0, 0, "rectCh", "E"), 
        new Offset(0, 0, "sumFirstA", "W") }, "arrowCh", null, mainloopArrowProps);

    this.lang.newCircle(new Offset(80, 0, "sumFirstA", "E"), 10, "sumFirstB", null, mainloopPlusCircleProps);
    this.lang.newPolyline(new Node[] { new Offset(0, 3, "sumFirstB", "N"), 
        new Offset(0, -3, "sumFirstB", "S") }, "p1sumFirstB", null, mainloopPlusSignProps);
    this.lang.newPolyline(new Node[] { new Offset(3, 0, "sumFirstB", "W"), 
        new Offset(-3, 0, "sumFirstB", "E") }, "p2sumFirstB", null, mainloopPlusSignProps);
    this.arrowSumFirstB = this.lang.newPolyline(new Node[] { new Offset(0, 0, "sumFirstB", "W"), 
        new Offset(0, 0, "sumFirstA", "E") }, "arrowSumFirstB", null, mainloopArrowProps);
    
    this.lang.newCircle(new Offset(80, 0, "rectE1", "E"), 10, "sumSecond", null, mainloopPlusCircleProps);
    this.lang.newPolyline(new Node[] { new Offset(0, 3, "sumSecond", "N"), 
        new Offset(0, -3, "sumSecond", "S") }, "p1sumSecond", null, mainloopPlusSignProps);
    this.lang.newPolyline(new Node[] { new Offset(3, 0, "sumSecond", "W"), 
        new Offset(-3, 0, "sumSecond", "E") }, "p2sumSecond", null, mainloopPlusSignProps);
    this.arrowE1 = this.lang.newPolyline(new Node[] { new Offset(0, 0, "rectE1", "E"), 
        new Offset(0, 0, "sumSecond", "W") }, "arrowE1", null, mainloopArrowProps);
    this.arrowSumFirst = this.lang.newPolyline(new Node[] { new Offset(0, 0, "sumFirstA", "S"), 
        new Offset(0, 0, "sumSecond", "N") }, "arrowSumFirst", null, mainloopArrowProps);
    
    this.lang.newCircle(new Offset(80, 0, "rectE0", "E"), 10, "sumFourth", null, mainloopPlusCircleProps);
    this.lang.newPolyline(new Node[] { new Offset(0, 3, "sumFourth", "N"), 
        new Offset(0, -3, "sumFourth", "S") }, "p1sumFourth", null, mainloopPlusSignProps);
    this.lang.newPolyline(new Node[] { new Offset(3, 0, "sumFourth", "W"), 
        new Offset(-3, 0, "sumFourth", "E") }, "p2sumFourth", null, mainloopPlusSignProps);
    this.arrowE0 = this.lang.newPolyline(new Node[] { new Offset(0, 0, "rectE0", "E"), 
        new Offset(0, 0, "sumFourth", "W") }, "arrowE0", null, mainloopArrowProps);
    
    this.lang.newCircle(new Offset(80, 0, "sumFourth", "E"), 10, "sumFifth", null, mainloopPlusCircleProps);
    this.lang.newPolyline(new Node[] { new Offset(0, 3, "sumFifth", "N"), 
        new Offset(0, -3, "sumFifth", "S") }, "p1sumFifth", null, mainloopPlusSignProps);
    this.lang.newPolyline(new Node[] { new Offset(3, 0, "sumFifth", "W"), 
        new Offset(-3, 0, "sumFifth", "E") }, "p2sumFifth", null, mainloopPlusSignProps);
    
    this.arrowSumFourth = this.lang.newPolyline(new Node[] { new Offset(0, 0, "sumFourth", "E"),
        new Offset(0, 0, "sumFifth", "W") }, "arrowSumFourth", null, mainloopArrowProps);
    this.arrowSumSecond = this.lang.newPolyline(new Node[] { new Offset(75, 150, "rectInputH", "SE"),
        new Offset(165, 150, "rectInputH", "SE"), new Offset(0, 0, "sumFifth", "N") },
        "arrowSumSecond", null, mainloopArrowProps);    
    
    this.arrowSumFifth = this.lang.newPolyline(new Node[] { 
        new Offset(0, 0, "sumFifth", "S"),
        new Offset(0, 25, "sumFifth", "S"),
        new Offset(0, -25, "rectOutputA", "N"),
        new Offset(0, 0, "rectOutputA", "N") },
        "arrowSumFifth", null, mainloopArrowProps);
    
    // path from H
    this.arrowSumFirstA = this.lang.newPolyline(new Node[] { new Offset(0, 0, "rectInputH", "S"),
        new Offset(0, 10, "rectInputH", "S"), new Offset(0, -30, "sumFirstA", "N"),
        new Offset(0, 0, "sumFirstA", "N") }, "arrowSumFirstA", null, mainloopArrowProps);
    
    // arrows
    
    this.arrowMa = this.lang.newPolyline(new Node[] { new Offset(0, 0, "rectMa", "E"), 
        new Offset(80, 0, "rectMa", "E"), new Offset(0, 0, "sumFourth", "N") }, "arrowMa", null, mainloopArrowProps);
 
    this.arrowSumSecondD = this.lang.newPolyline(new Node[] { new Offset(75, 150, "rectInputH", "SE"),
        new Offset(10, 150, "rectInputD", "S") }, "arrowSumSecondD", null, mainloopArrowProps);
    
    this.arrowW = this.lang.newPolyline(new Node[] { new Offset(0, -37, "sumFirstB", "N"), 
        new Offset(0, 0, "sumFirstB", "N") }, "arrowW", null, mainloopArrowProps);
    this.arrowK = this.lang.newPolyline(new Node[] { new Offset(57, 0, "sumFirstB", "W"),  
        new Offset(0, 0, "sumFirstB", "E") }, "arrowK", null, mainloopArrowProps);
    
    // path from E    
    this.arrowE = this.lang.newPolyline(new Node[] { new Offset(0, 190, "rectInputE", "S"),
        new Offset(0, 250, "rectInputE", "S"), new Offset(0, 0, "rectOutputF", "N")},
        "arrowE", null, mainloopArrowProps);
    this.arrowECh = this.lang.newPolyline(new Node[] { new Offset(0, 60, "rectInputE", "S"),
        new Offset(0, 10, "rectCh", "NW") }, "arrowECh", null, mainloopArrowProps);
    this.arrowE1E = this.lang.newPolyline(new Node[] { new Offset(0, 110, "rectInputE", "S"),
        new Offset(0, 110, "rectInputH", "S") }, "arrowE1E", null, mainloopArrowProps);
    this.arrowEMa = this.lang.newPolyline(new Node[] { new Offset(0, 190, "rectInputE", "S"),
        new Offset(0, 10, "rectMa", "NW") }, "arrowEMa", null, mainloopArrowProps);
    
    
    // path from F      
    this.arrowF = this.lang.newPolyline(new Node[] { new Offset(0, 200, "rectInputF", "S"),
        new Offset(0, 250, "rectInputF", "S"), new Offset(0, 0, "rectOutputG", "N")},
        "arrowF", null, mainloopArrowProps);
    this.arrowFCh = this.lang.newPolyline(new Node[] { new Offset(0, 70, "rectInputF", "S"),
        new Offset(0, 20, "rectCh", "NW") }, "arrowFCh", null, mainloopArrowProps);
    this.arrowFMa = this.lang.newPolyline(new Node[] { new Offset(0, 200, "rectInputF", "S"),
        new Offset(0, 20, "rectMa", "NW") }, "arrowFMa", null, mainloopArrowProps);
    
   // path from G      
    this.arrowG = this.lang.newPolyline(new Node[] { new Offset(0, 210, "rectInputG", "S"),
     new Offset(0, 250, "rectInputG", "S"), new Offset(0, 0, "rectOutputH", "N")},
     "arrowG", null, mainloopArrowProps);
   this.arrowGCh = this.lang.newPolyline(new Node[] { new Offset(0, 80, "rectInputG", "S"),
       new Offset(0, 30, "rectCh", "NW") }, "arrowGCh", null, mainloopArrowProps);
   this.arrowGMa = this.lang.newPolyline(new Node[] { new Offset(0, 210, "rectInputG", "S"),
       new Offset(0, 30, "rectMa", "NW") }, "arrowGMa", null, mainloopArrowProps);
   
   // base arrows (no arrow)
   this.mainloopArrowProps.set("fwArrow", false);
   
   this.arrowABase = this.lang.newPolyline(new Node[] { new Offset(0, 0, "rectInputA", "S"),
       new Offset(0, 240, "rectInputA", "S") },
       "arrowABase", null, mainloopArrowProps);
   this.arrowSumSecondBase = this.lang.newPolyline(new Node[] { new Offset(0, 0, "sumSecond", "S"),
       new Offset(75, 150, "rectInputH", "SE") }, "arrowSumSecondD", null, mainloopArrowProps); 
   this.arrowEBase1 = this.lang.newPolyline(new Node[] { new Offset(0, 0, "rectInputE", "S"),
       new Offset(0, 60, "rectInputE", "S") }, "arrowEBase1", null, mainloopArrowProps);
   this.arrowEBase2 = this.lang.newPolyline(new Node[] { new Offset(0, 60, "rectInputE", "S"),
       new Offset(0, 110, "rectInputE", "S") }, "arrowEBase2", null, mainloopArrowProps);
   this.arrowEBase3 = this.lang.newPolyline(new Node[] { new Offset(0, 110, "rectInputE", "S"),
       new Offset(0, 190, "rectInputE", "S") }, "arrowEBase3", null, mainloopArrowProps);
   this.arrowFBase1 = this.lang.newPolyline(new Node[] { new Offset(0, 0, "rectInputF", "S"),
       new Offset(0, 70, "rectInputF", "S") }, "arrowFBase1", null, mainloopArrowProps);
   this.arrowFBase2 = this.lang.newPolyline(new Node[] { new Offset(0, 70, "rectInputF", "S"),
       new Offset(0, 200, "rectInputF", "S") }, "arrowFBase2", null, mainloopArrowProps);
   this.arrowGBase1 = this.lang.newPolyline(new Node[] { new Offset(0, 0, "rectInputG", "S"),
       new Offset(0, 80, "rectInputG", "S") }, "arrowGBase1", null, mainloopArrowProps);
   this.arrowGBase2 = this.lang.newPolyline(new Node[] { new Offset(0, 80, "rectInputG", "S"),
       new Offset(0, 210, "rectInputG", "S") }, "arrowGBase2", null, mainloopArrowProps);
   
   this.mainloopArrowProps.set("fwArrow", true);
    
    // source code
    this.mainLoopCode = this.lang.newSourceCode(new Offset(350, 0, "textInputH", "NE"), "mainLoopCode", null, this.sourceCodeProps);
    this.mainLoopCode.addCodeLine("Hauptschleife:", null, 0, null);
    this.mainLoopCode.addCodeLine("", null, 0, null);
    this.mainLoopCode.addCodeLine("for(int t = 0; t < 64; t++){", null, 1, null);
    this.mainLoopCode.addCodeLine("int s1 = rightrotate(E, 6) ^ rightrotate(E, 11) ^ rightrotate(E, 25);", null, 2, null);
    this.mainLoopCode.addCodeLine("int ch = (E & F) ^ ((~E) & G);", null, 2, null);
    this.mainLoopCode.addCodeLine("int temp1 = H + s1 + ch + K[t] + w[t];", null, 2, null);
    this.mainLoopCode.addCodeLine("int s0 = rightrotate(A, 2) ^ rightrotate(A, 13) ^ rightrotate(A, 22);", null, 2, null);
    this.mainLoopCode.addCodeLine("int maj = (A & B) ^ (A & C) ^ (B & C);", null, 2, null);
    this.mainLoopCode.addCodeLine("int temp2 = s0 + maj;", null, 2, null);
    this.mainLoopCode.addCodeLine("", null, 0, null);
    this.mainLoopCode.addCodeLine("H = G;", null, 2, null);
    this.mainLoopCode.addCodeLine("G = F;", null, 2, null);
    this.mainLoopCode.addCodeLine("F = E;", null, 2, null);
    this.mainLoopCode.addCodeLine("E = D + temp1;", null, 2, null);
    this.mainLoopCode.addCodeLine("D = C;", null, 2, null);
    this.mainLoopCode.addCodeLine("C = B;", null, 2, null);
    this.mainLoopCode.addCodeLine("B = A;", null, 2, null);
    this.mainLoopCode.addCodeLine("A = temp1 + temp2;", null, 2, null);
    this.mainLoopCode.addCodeLine("}", null, 1, null);
    
  }

  public void hash(String message) {
    this.stepTitle.setText("1.1 Preprocessing: Nachricht in Byte-Array umwandeln", null, null); 

    ///////////////////// STEP 1 ///////////////////////////////////
    
    SourceCode initText = this.lang.newSourceCode(new Offset(0, 10, this.stepTitle,"SW"), "initText", null, sourceCodeProps);
    initText.addCodeLine("Die Initialhashs und das Array der Konstanten für jede Runde der Hauptschleife werden initialisiert", null, 0, null);
    initText.addCodeLine("", null, 0, null);
    initText.addCodeLine("int H0 = 0x6a09e667;", null, 0, null);
    initText.addCodeLine("int H1 = 0xbb67ae85;", null, 0, null);
    initText.addCodeLine("int H2 = 0x3c6ef372;", null, 0, null);
    initText.addCodeLine("int H3 = 0xa54ff53a;", null, 0, null);
    initText.addCodeLine("int H4 = 0x510e527f;", null, 0, null);
    initText.addCodeLine("int H5 = 0x9b05688c;", null, 0, null);
    initText.addCodeLine("int H6 = 0x1f83d9ab;", null, 0, null);
    initText.addCodeLine("int H7 = 0x5be0cd19;", null, 0, null);
    initText.addCodeLine("", null, 0, null);
    initText.addCodeLine("", null, 0, null);
    initText.addCodeLine("", null, 0, null);
    initText.addCodeLine("int[] K =", null, 0, null);
    initText.addCodeLine("  {0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,", null, 0, null);
    initText.addCodeLine("   0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174, ", null, 0, null);
    initText.addCodeLine("   0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da, ", null, 0, null);
    initText.addCodeLine("   0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967, ", null, 0, null);
    initText.addCodeLine("   0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85, ", null, 0, null);
    initText.addCodeLine("   0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070, ", null, 0, null);
    initText.addCodeLine("   0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3, ", null, 0, null);
    initText.addCodeLine("   0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2};", null, 0, null);
    initText.addCodeLine("", null, 0, null);
    initText.addCodeLine("", null, 0, null);
    
    this.lang.nextStep("1.1 Nachricht in Byte-Array umwandeln");
    initText.hide();
    
    int H0 = 0x6a09e667;
    int H1 = 0xbb67ae85;
    int H2 = 0x3c6ef372;
    int H3 = 0xa54ff53a;
    int H4 = 0x510e527f;
    int H5 = 0x9b05688c;
    int H6 = 0x1f83d9ab;
    int H7 = 0x5be0cd19;
    byte[] msg = message.getBytes();
    boolean isMoreThanOneBlock = msg.length > 55;
    int original_byte_len = msg.length;
    long original_bit_len = original_byte_len * 8;
    
    int[] K =
      {0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5, 
      0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174, 
      0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da, 
      0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967, 
      0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85, 
      0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070, 
      0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3, 
      0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2};
    
    this.stepTitle.setText("1.2 Preprocessing: Nachricht in Byte-Array umwandeln", null, null);
    
    // Show inputstring at beginning
    this.preprocDescription = this.lang.newText(new Offset(0, 5, this.stepTitle, "SW"), "Eingabetext", "desc1", null, this.textProps);
    this.preprocValue = this.lang.newText(new Offset(140, 5, this.stepTitle,"SW"), message, "val1", null, textProps);
    
    this.preprocDescriptionHex = this.lang.newText(new Offset(0, 20, this.stepTitle, "SW"), "Eingabetext in hex", "desc2", null, textProps);
    this.preprocValueHex = this.lang.newText(new Offset(140, 20, this.stepTitle,"SW"), ("0x" + toHexString(msg)), "val2", null, textProps);
    
    // Cut off if msg is too long
    if(toHexString(msg).length() > 40) {
      preprocValueHex.setText(("0x" + toHexString(msg)).substring(0, 39).concat(" ..."), null, null);
    }
    
    this.lang.nextStep("1.2 Nachricht in Byte-Array umwandeln");
    
    // Create matrix
    this.preprocDisplayMatrix = new String[][] {
        { "XX", "XX", "XX", "XX", "XX", "XX", "XX", "XX" },
        { "XX", "XX", "XX", "XX", "XX", "XX", "XX", "XX" },
        { "XX", "XX", "XX", "XX", "XX", "XX", "XX", "XX" },
        { "XX", "XX", "XX", "XX", "XX", "XX", "XX", "XX" },
        { "XX", "XX", "XX", "XX", "XX", "XX", "XX", "XX" },
        { "XX", "XX", "XX", "XX", "XX", "XX", "XX", "XX" },
        { "XX", "XX", "XX", "XX", "XX", "XX", "XX", "XX" },
        { "XX", "XX", "XX", "XX", "XX", "XX", "XX", "XX" } };
    
    this.preprocMatrix = this.lang.newStringMatrix(new Offset(0, 50, this.stepTitle, "SW"),
        preprocDisplayMatrix, "preprocMatrix", null, preprocMatrixProps);
    
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
    
    this.stepTitle.setText("1.3 Preprocessing: Eins anfügen", null, null);
    
    this.preprocCode.toggleHighlight(0, 1);
    this.lang.nextStep("1.3 Eins anfügen");
    
    // add 1 bit to message
    updateDisplayMatrixByte(msg.length, (byte)0x80);
    msg = add(msg, (byte) 0x80);
    
    this.lang.nextStep();
    this.preprocCode.unhighlight(1);

    ///////////////////// ADD 0's ///////////////////////////////////
    
    this.stepTitle.setText("1.4 Preprocessing: Mit Nullen auffüllen", null, null);
    
    // append 0 <= k < 512 bits '0', so that the resulting message length (in bits)
    // is equal to 448 (mod 512)
    int times = (((56 - (original_byte_len + 1) % 64) + 64) % 64);
    this.preprocCode.highlight(2);
    this.lang.nextStep("1.4 Mit Nullen auffüllen");
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
    
    this.stepTitle.setText("1.5 Preprocessing: Länge der Originalnachricht anfügen", null, null);
    
    // append length of message (before pre-processing), in bits, as 64-bit
    // big-endian integer
    msg = add(msg, original_bit_len);
    this.lang.nextStep("1.5 Länge der Originalnachricht anfügen");
    
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
    
    this.comment = this.lang.newText(new Offset(0, 15, this.stepTitle, "SW"),
        "Es werden immer 512-bit Blocks (64 Byte) für jeden Durchlauf der Rahmenschleife benutzt.", "comment", null, commentProps);
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

      ///////////////////// ARRAY W[] ///////////////////////////////////
      
      this.stepTitle.setText("2.1 Rahmenschleife: w[] generieren", null, null);
      comment.setText("Das Array w[ ] wird aus der Nachricht msg und sich selbst errechnet.", null, null);
      
      this.lang.nextStep("2.1 w[] generieren");
      
      this.generateMsg = this.lang.newSourceCode(new Offset(0, 30, this.stepTitle, "SW"), "msgForW", null, this.sourceCodeProps);
      this.generateWText = this.lang.newSourceCode(new Offset(0, 30, this.stepTitle, "SW"), "textW", null, this.sourceCodeProps);
      this.generateWTextResult = this.lang.newSourceCode(new Offset(0, 30, this.stepTitle, "SW"), "textWresult", null, this.sourceCodeProps);
      this.generateMsg.addCodeLine("msg:", null, 15, null);
      this.generateMsg.addCodeLine("", null, 15, null);
      this.generateWText.addCodeLine("", null, 0, null);
      this.generateWText.addCodeLine("", null, 0, null);
      this.generateWTextResult.addCodeLine("", null, 0, null);
      this.generateWTextResult.addCodeLine("", null, 0, null);
      
      // Setup a string to show how w[] is build
      for(int p = 0; p < 16; p++){
        String num = toHexString((msg[i + (p * 4) + 0] << 24) + (msg[i + (p * 4) + 1] << 16)
                + (msg[i + (p * 4) + 2] << 8) + (msg[i + (p * 4) + 3] & 0xff));
        this.generateMsg.addCodeLine(num, null, 15, null);
        this.generateWText.addCodeLine("w[ " + p + " ] = ", null, 0, null);
      }
      this.generateWText.addCodeLine("...", null, 0, null);
      this.generateWText.hide();

      this.lang.nextStep();
      this.generateWCode = this.lang.newSourceCode(new Offset(0, 360, this.stepTitle, "SW"), "stepThreeCode", null, this.sourceCodeProps);
      this.generateWCode.addCodeLine("int[] w = new int[64];", null, 1, null);
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
      int[] w = new int[64];
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
            new Offset(150, 41+j*16, this.generateWText, "N"),
            new Offset(100, 41+j*16, this.generateWText, "N")},
            "arrowW", null, generateWArrowProps);
        this.generateMsg.highlight(2+j);
        
        this.lang.nextStep();
        this.generateMsg.unhighlight(2+j);
        this.generateWCode.unhighlight(2);
        this.generateWCode.unhighlight(3);
        this.generateWCode.unhighlight(4);
        this.generateWCode.unhighlight(5);
        this.generateWCode.highlight(6);
        this.generateWTextResult.addCodeLine(toHexString(w[j]), null, 5, null);
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
      this.generateMsg.hide();
      this.generateWText.hide();
      this.generateWTextResult.hide();
      this.generateWCode.hide();
      this.generateWArrow.hide();
      
      ///////////////////// EXTEND W[] ///////////////////////////////////
      
      this.stepTitle.setText("2.2 Rahmenschleife: w[] erweitern", null, null);
      comment.setText("Das Array w[ ] wird von sechszehn 32-bit Zahlen zu vierundsechszig 32-bit Zahlen erweitert. Wobei die Funktion rr() rightrotate bedeutet.", null, null);
      
      this.extendWMatrix = new String[][] {
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
          { "", ""} }; // 10
      
      this.extendWMatrix2 = new String[][] {
          { "", ""}, // 0
          { "", ""}};
      
      this.extendMatrix = this.lang.newStringMatrix(new Offset(0, 50, this.stepTitle, "SW"),
          extendWMatrix, "extendMatrix", null, extendWMatrixProps);
      this.extendMatrix2 = this.lang.newStringMatrix(new Offset(440, 50, this.stepTitle, "SW"),
          extendWMatrix2, "extendMatrix2", null, extendWMatrixProps);
      
      this.extendWCode = this.lang.newSourceCode(new Offset(0, 30, this.extendMatrix, "SW"), "stepFiveCode", null, this.sourceCodeProps);
      this.extendWCode.addCodeLine("for (int t = 16; t < 64; t++){", null, 1, null);  
      this.extendWCode.addCodeLine("int s0 = rightrotate(w[t-15], 7) ^ rightrotate(w[t-15], 18) ^ (w[t-15] >>> 3);", null, 2, null);
      this.extendWCode.addCodeLine("int s1 = rightrotate(w[t-2], 17) ^ rightrotate(w[t-2], 19) ^ (w[t-2] >>> 10);", null, 2, null);
      this.extendWCode.addCodeLine("w[t] = w[t-16] + s0 + w[t-7] + s1;", null, 2, null);
      this.extendWCode.addCodeLine("}", null, 1, null);
      
      this.rightRotateCode = this.lang.newSourceCode(new Offset(440, 30, this.extendMatrix, "SW"), "stepFiveCode", null, this.sourceCodeProps);
      this.rightRotateCode.addCodeLine("private final int rr(int x, int distance){", null, 1, null);  
      this.rightRotateCode.addCodeLine("return (((x >>> count)) | (x << (32 - distance)));", null, 2, null);
      this.rightRotateCode.addCodeLine("}", null, 1, null);
      
      this.lang.nextStep("2.1 w[] generieren");
      this.extendWCode.highlight(0);
      this.lang.nextStep();
      this.extendWCode.unhighlight(0);
      this.extendMatrix2.put(0, 0, "s0 = ", null, null);
      this.extendMatrix2.put(1, 0, "s1 = ", null, null);
      
      //  Extend the first 16 words into the remaining
      //  48 words w[16..63] of the message schedule array:
      for (int t = 16; t < 64; t++){
        int s0 = rightrotate(w[t-15], 7) ^ rightrotate(w[t-15], 18) ^ (w[t-15] >>> 3);
        int s1 = rightrotate(w[t-2], 17) ^ rightrotate(w[t-2], 19) ^ (w[t-2] >>> 10);
        w[t] = w[t-16] + s0 + w[t-7] + s1;
        
        if(t < (16 + this.extendWLimit)){
          if((t-16)%10 == 0){
            for (int j = 0; j < 10; j++) {
              this.extendMatrix.put(j, 0, "", null, null);
              this.extendMatrix.put(j, 1, "", null, null);
            }
          }
          
          this.extendMatrix2.put(0, 1, "rr(w[t-15], 7) ^ rr(w[t-15], 18) ^ (w[t-15] >>> 3);", null, null);
          this.extendMatrix2.put(1, 1, "rightrotate(w[t-2], 17) ^ rightrotate(w[t-2], 19) ^ (w[t-2] >>> 10);", null, null);
          this.extendMatrix.put((t-16)%10, 0, "w[ " + t + "] =", null, null);
          this.extendMatrix.put((t-16)%10, 1, "w[t-16] + s0 + w[t-7] + s1;", null, null);
          this.extendWCode.highlight(1);
          this.extendWCode.highlight(2);
          this.lang.nextStep();
          this.extendMatrix2.put(0, 1, "rr("+toHexString(w[t-15])+", 7) ^ rr("+toHexString(w[t-15])+", 18) ^ ("+toHexString(w[t-15])+" >>> 3);", null, null);
          this.extendMatrix2.put(1, 1, "rr("+toHexString(w[t-2])+", 17) ^ rr("+toHexString(w[t-2])+", 19) ^ ("+toHexString(w[t-2])+" >>> 10);", null, null);

          this.lang.nextStep();
          this.extendMatrix2.put(0, 1, toHexString(rightrotate(w[t-15], 7))+" ^ "+toHexString(rightrotate(w[t-15], 18))+" ^ "+ toHexString(w[t-15] >>> 1)+";", null, null);
          this.extendMatrix2.put(1, 1, toHexString(rightrotate(w[t-2], 17))+" ^ "+toHexString(rightrotate(w[t-2], 19))+" ^ "+ toHexString(w[t-2] >>> 10)+";", null, null);
          
          this.lang.nextStep();
          this.extendMatrix2.put(0, 1, toHexString(s0), null, null);
          this.extendMatrix2.put(1, 1, toHexString(s1), null, null);
          this.extendWCode.unhighlight(1);
          this.extendWCode.unhighlight(2);
          this.extendWCode.highlight(3);
          this.extendMatrix.put((t-16)%10, 1, toHexString(w[t-16]) +" + "+ toHexString(s0) +" + "+ toHexString(w[t-7]) +" + "+ toHexString(s1) +";", null, null); 
          
          this.lang.nextStep();
          this.extendMatrix.put((t-16)%10, 1, toHexString(w[t]), null, null); 
          this.extendWCode.unhighlight(3);
          this.lang.nextStep();
        }
      }
      this.extendWCode.hide();
      this.rightRotateCode.hide();
      
      if(16 + this.extendWLimit < 64){
        this.extendMatrix.put(10, 1,"usw. bis 64", null, null);
      }  
      
      this.lang.nextStep();
      this.extendMatrix.hide();
      this.extendMatrix2.hide();
      comment.hide();

      ///////////////////// VARIABLEN INIT ///////////////////////////////////
      
      this.stepTitle.setText("2.3 Rahmenschleife: Hauptschleife", null, null);
      
      int A = H0;
      int B = H1;
      int C = H2;
      int D = H3;
      int E = H4;
      int F = H5;
      int G = H6;
      int H = H7;
      
      this.variableInitCode = this.lang.newSourceCode(new Offset(0, 50, this.stepTitle, "SW"), "stepThreeCode", null, this.sourceCodeProps);
      this.variableInitCode.addCodeLine("Am Anfang des Programms wurden 8 Variablen ( H1, H2, H3, H4, H5, H6, H7 ) mit vorbestimmten Hex Werten definiert.", null, 0, null);
      this.variableInitCode.addCodeLine("Diese werden nun in temporäre Variablen für die Hauptschleife übernommen:", null, 0, null);
      this.variableInitCode.addCodeLine("", null, 0, null);
      this.variableInitCode.addCodeLine("int A = ", null, 1, null);
      this.variableInitCode.addCodeLine("int B = ", null, 1, null);
      this.variableInitCode.addCodeLine("int C = ", null, 1, null);
      this.variableInitCode.addCodeLine("int D = ", null, 1, null);
      this.variableInitCode.addCodeLine("int E = ", null, 1, null);
      this.variableInitCode.addCodeLine("int F = ", null, 1, null);
      this.variableInitCode.addCodeLine("int G = ", null, 1, null);
      this.variableInitCode.addCodeLine("int H = ", null, 1, null);
      this.variableValues = this.lang.newSourceCode(new Offset(0, 50, this.stepTitle, "SW"), "variableValues", null, this.sourceCodeProps);
      this.variableValues.addCodeLine("", null, 0, null);
      this.variableValues.addCodeLine("", null, 0, null);
      this.variableValues.addCodeLine("", null, 0, null);
      this.variableValues.addCodeLine("H0;", null, 5, null);
      this.variableValues.addCodeLine("H1;", null, 5, null);
      this.variableValues.addCodeLine("H2;", null, 5, null);
      this.variableValues.addCodeLine("H3;", null, 5, null);
      this.variableValues.addCodeLine("H4;", null, 5, null);
      this.variableValues.addCodeLine("H5;", null, 5, null);
      this.variableValues.addCodeLine("H6;", null, 5, null);
      this.variableValues.addCodeLine("H7;", null, 5, null);
      
      this.lang.nextStep("2.3 Hauptschleife");
      this.variableValues.hide();
      this.variableValues = this.lang.newSourceCode(new Offset(0, 50, this.stepTitle, "SW"), "variableValues", null, this.sourceCodeProps);
      this.variableValues.addCodeLine("", null, 0, null);
      this.variableValues.addCodeLine("", null, 0, null);
      this.variableValues.addCodeLine("", null, 0, null);
      this.variableValues.addCodeLine("0x" + toHexString(H0)+";", null, 5, null);
      this.variableValues.addCodeLine("0x" + toHexString(H1)+";", null, 5, null);
      this.variableValues.addCodeLine("0x" + toHexString(H2)+";", null, 5, null);
      this.variableValues.addCodeLine("0x" + toHexString(H3)+";", null, 5, null);
      this.variableValues.addCodeLine("0x" + toHexString(H4)+";", null, 5, null);
      this.variableValues.addCodeLine("0x" + toHexString(H5)+";", null, 5, null);
      this.variableValues.addCodeLine("0x" + toHexString(H6)+";", null, 5, null);
      this.variableValues.addCodeLine("0x" + toHexString(H7)+";", null, 5, null);
      
      this.lang.nextStep();
      this.variableInitCode.hide();
      this.variableValues.hide();


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
      this.inputF = this.lang.newText(new Offset(3, 2, "rectInputF", "NW"), toHexString(F), "inputF", null, mainloopInputTextProps);
      this.inputG = this.lang.newText(new Offset(3, 2, "rectInputG", "NW"), toHexString(G), "inputG", null, mainloopInputTextProps);
      this.inputH = this.lang.newText(new Offset(3, 2, "rectInputH", "NW"), toHexString(H), "inputH", null, mainloopInputTextProps);
      
      this.outputA = this.lang.newText(new Offset(3, 2, "rectOutputA", "NW"), "", "outputA", null, this.mainloopOutputTextProps);
      this.outputB = this.lang.newText(new Offset(3, 2, "rectOutputB", "NW"), "", "outputB", null, this.mainloopOutputTextProps);
      this.outputC = this.lang.newText(new Offset(3, 2, "rectOutputC", "NW"), "", "outputC", null, this.mainloopOutputTextProps);
      this.outputD = this.lang.newText(new Offset(3, 2, "rectOutputD", "NW"), "", "outputD", null, this.mainloopOutputTextProps);
      this.outputE = this.lang.newText(new Offset(3, 2, "rectOutputE", "NW"), "", "outputE", null, this.mainloopOutputTextProps);
      this.outputF = this.lang.newText(new Offset(3, 2, "rectOutputF", "NW"), "", "outputF", null, this.mainloopOutputTextProps);
      this.outputG = this.lang.newText(new Offset(3, 2, "rectOutputG", "NW"), "", "outputG", null, this.mainloopOutputTextProps);
      this.outputH = this.lang.newText(new Offset(3, 2, "rectOutputH", "NW"), "", "outputH", null, this.mainloopOutputTextProps);
      
      // intermediate values
      this.valueT = this.lang.newText(new Offset(0, 3, "rectOutputH", "S"), "t = 0", "valueT", null, this.mainloopIntermediateTextProps);
      this.valueW = this.lang.newText(new Offset(-10, -55, "sumFirstB", "N"), "W[t]", "valueW", null, this.mainloopIntermediateTextProps);
      this.valueK = this.lang.newText(new Offset(40, -7, "sumFirstB", "E"), "K[t]", "valueK", null, this.mainloopIntermediateTextProps);
      this.valueWK = this.lang.newText(new Offset(10, -15, "sumFirstA", "NE"), "", "valueWK", null, this.mainloopIntermediateTextProps);
      this.valueCh = this.lang.newText(new Offset(3, 7, "rectCh", "E"), "", "valueCh", null, this.mainloopIntermediateTextProps);
      this.valueChH = this.lang.newText(new Offset(3, 7, "sumFirstA", "E"), "", "valueChH", null, this.mainloopIntermediateTextProps);
      this.valueE1 = this.lang.newText(new Offset(3, 7, "rectE1", "E"), "", "valueE1", null, mainloopIntermediateTextProps);
      this.valueTemp1 = this.lang.newText(new Offset(3, 15, "sumSecond", "SE"), "", "valueTemp1", null, mainloopIntermediateTextProps);
      this.valueMa = this.lang.newText(new Offset(3, 7, "rectMa", "E"), "", "valueMa", null, mainloopIntermediateTextProps);
      this.valueE0 = this.lang.newText(new Offset(3, 7, "rectE0", "E"), "", "valueE0", null, mainloopIntermediateTextProps);
      this.valueTemp2 = this.lang.newText(new Offset(3, 7, "sumFourth", "E"), "", "valueTemp1", null, mainloopIntermediateTextProps);

      this.lang.nextStep();
      
      for(int t = 0; t < 64; t++){
        valueT.setText("t = " + t, null, null);
        
        int s1 = rightrotate(E, 6) ^ rightrotate(E, 11) ^ rightrotate(E, 25);
        int ch = (E & F) ^ ((~E) & G);
        int temp1 = H + s1 + ch + K[t] + w[t];

        int s0 = rightrotate(A, 2) ^ rightrotate(A, 13) ^ rightrotate(A, 22);
        int maj = (A & B) ^ (A & C) ^ (B & C);
        int temp2 = s0 + maj;
        
        if (t < mainLoopLimit) {
          this.mainLoopCode.toggleHighlight(17, 3);
          // highlight arrowEBase1, arrowEBase2, arrowE1E, inputE
          this.arrowE1E.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowEBase1.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowEBase2.changeColor(null, this.arrowHighlightColor, null, null);
          this.inputE.changeColor(null, this.inputTextHighlightColor, null, null);
          this.lang.nextStep();
          this.valueE1.setText(toHexString(s1), null, null);
          // highlight: valueE1
          this.valueE1.changeColor(null, this.intermediateHighlightColor, null, null);
          this.lang.nextStep();
          
          //unhighlight arrowEBase1, arrowEBase2, arrowE1E, inputE, valueE1
          this.arrowE1E.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowEBase1.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowEBase2.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.inputE.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.valueE1.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          // highlight arrowEBase1, arrowFBase1, arrowGBase1, arrowECh, arrowFCh, arrowGCh, inputE, inputF, inputG
          this.arrowEBase1.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowFBase1.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowGBase1.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowECh.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowFCh.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowGCh.changeColor(null, this.arrowHighlightColor, null, null);
          this.inputE.changeColor(null, this.inputTextHighlightColor, null, null);
          this.inputF.changeColor(null, this.inputTextHighlightColor, null, null);
          this.inputG.changeColor(null, this.inputTextHighlightColor, null, null);
          this.mainLoopCode.toggleHighlight(3, 4);
          this.lang.nextStep();
          this.valueCh.setText(toHexString(ch), null, null);
          // highlight: valueE1
          this.valueCh.changeColor(null, this.intermediateHighlightColor, null, null);
          this.lang.nextStep();
          
          // unhighlight arrowEBase1, arrowFBase1, arrowGBase1, arrowECh, arrowFCh, arrowGCh, inputE, inputF, inputG, valueE1
          this.arrowEBase1.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowFBase1.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowGBase1.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowECh.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowFCh.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowGCh.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.inputE.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.inputF.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.inputG.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.valueCh.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          this.valueW.setText("W[t] = " + toHexString(w[t]), null, null);
          this.valueK.setText("K[t] = " + toHexString(K[t]), null, null);
          this.mainLoopCode.toggleHighlight(4, 5);
          // highlight valueW, valueK, arrowW, arrowK
          this.valueW.changeColor(null, this.intermediateHighlightColor, null, null);
          this.valueK.changeColor(null, this.intermediateHighlightColor, null, null);
          this.arrowW.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowK.changeColor(null, this.arrowHighlightColor, null, null);     
          this.lang.nextStep();
          this.valueWK.setText(toHexString(w[t] + K[t]), null, null);
          // highlight valueWK
          this.valueWK.changeColor(null, this.intermediateHighlightColor, null, null);
          this.lang.nextStep();
          
          // unhighlight valueW, valueK, arrowW, arrowK
          this.valueW.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          this.valueK.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          this.arrowW.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowK.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          // highlight inputH, valueCh, arrowSumFirstA, arrowCh, arrowSumFirstB
          this.inputH.changeColor(null, this.inputTextHighlightColor, null, null);
          this.valueCh.changeColor(null, this.intermediateHighlightColor, null, null);
          this.arrowSumFirstA.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowCh.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowSumFirstB.changeColor(null, this.arrowHighlightColor, null, null);
          this.lang.nextStep();
          this.valueChH.setText(toHexString(ch + w[t] + K[t]), null, null);
          // highlight valueChH
          this.valueChH.changeColor(null, this.intermediateHighlightColor, null, null);
          this.lang.nextStep();
          
          // unhighlight inputH, valueCh, valueWK, arrowSumFirstA, arrowCh, arrowSumFirstB
          this.inputH.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.valueCh.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          this.valueWK.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          this.arrowSumFirstA.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowCh.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowSumFirstB.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          // highlight arrowE1, arrowSumFirst, valueE1
          this.arrowE1.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowSumFirst.changeColor(null, this.arrowHighlightColor, null, null);
          this.valueE1.changeColor(null, this.intermediateHighlightColor, null, null);
          this.lang.nextStep();
          valueTemp1.setText(toHexString(s1 + ch + w[t] + K[t]), null, null);
          // highlight valueTemp1
          this.valueTemp1.changeColor(null, this.intermediateHighlightColor, null, null);
          this.lang.nextStep();
          
          // unhighlight arrowE1, arrowSumFirst, valueE1, valueTemp1, valueChH
          this.arrowE1.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowSumFirst.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.valueE1.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          this.valueTemp1.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          this.valueChH.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          // highlight arrowABase, arrowE0A, inputA
          this.arrowE0A.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowABase.changeColor(null, this.arrowHighlightColor, null, null);
          this.inputA.changeColor(null, this.inputTextHighlightColor, null, null);
          this.mainLoopCode.toggleHighlight(5, 6);
          this.lang.nextStep();
          valueE0.setText(toHexString(s0), null, null);
          // highlight valueE0
          this.valueE0.changeColor(null, this.intermediateHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight arrowABase, arrowE0A, inputA, valueE0
          this.arrowE0A.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowABase.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.inputA.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.valueE0.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          // highlight arrowEBase1, arrowEBase2, arrowEBase3, arrowFBase1, arrowFBase2, arrowGBase1, arrowGBase2,
          // arrowEMa, arrowFMa, arrowGMa, inputE, inputF, inputG
          this.arrowEBase1.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowEBase2.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowEBase3.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowFBase1.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowFBase2.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowGBase1.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowGBase2.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowEMa.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowFMa.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowGMa.changeColor(null, this.arrowHighlightColor, null, null);
          this.inputE.changeColor(null, this.inputTextHighlightColor, null, null);
          this.inputF.changeColor(null, this.inputTextHighlightColor, null, null);
          this.inputG.changeColor(null, this.inputTextHighlightColor, null, null);
          this.mainLoopCode.toggleHighlight(6, 7);
          this.lang.nextStep();
          valueMa.setText(toHexString(maj), null, null);
          // highlight valueMa
          this.valueMa.changeColor(null, this.intermediateHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight arrowEBase1, arrowEBase2, arrowEBase3, arrowFBase1, arrowFBase2, arrowGBase1, arrowGBase2,
          // arrowEMa, arrowFMa, arrowGMa, inputE, inputF, inputG
          this.arrowEBase1.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowEBase2.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowEBase3.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowFBase1.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowFBase2.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowGBase1.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowGBase2.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowEMa.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowFMa.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowGMa.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.inputE.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.inputF.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.inputG.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          // highlight valueE0, arrowMa, arrowE0
          this.valueE0.changeColor(null, this.intermediateHighlightColor, null, null);
          this.arrowMa.changeColor(null, this.arrowHighlightColor, null, null);
          this.arrowE0.changeColor(null, this.arrowHighlightColor, null, null);
          this.mainLoopCode.toggleHighlight(7, 8);
          this.lang.nextStep();
          valueTemp2.setText(toHexString(temp2), null, null);
          this.valueTemp2.changeColor(null, this.intermediateHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight valueTemp2, valueMa, valueE0, arrowMa, arrowE0
          this.valueMa.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          this.valueTemp2.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          this.valueE0.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          this.arrowMa.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowE0.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
        }
        
        H = G;
        if (t < mainLoopLimit) {
          this.mainLoopCode.toggleHighlight(8, 10);
          // highlight arrowGBase1, arrowGBase2, arrowG, inputG
          this.inputG.changeColor(null, inputTextHighlightColor, null, null);
          this.arrowGBase1.changeColor(null, arrowHighlightColor, null, null);
          this.arrowGBase2.changeColor(null, arrowHighlightColor, null, null);
          this.arrowG.changeColor(null, arrowHighlightColor, null, null);
          this.lang.nextStep();
          this.outputH.setText(toHexString(H), null, null);
          // highlight ouputH
          this.outputH.changeColor(null, outputTextHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight arrowGBase1, arrowGBase2, arrowG, outputH, inputG
          this.arrowGBase1.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowGBase2.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowG.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.outputH.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.inputG.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
        }
        G = F;
        if (t < mainLoopLimit) {
          this.mainLoopCode.toggleHighlight(10, 11);
          // highlight arrowFBase1, arrowFBase2, arrowF, inputF
          this.inputF.changeColor(null, inputTextHighlightColor, null, null);
          this.arrowFBase1.changeColor(null, arrowHighlightColor, null, null);
          this.arrowFBase2.changeColor(null, arrowHighlightColor, null, null);
          this.arrowF.changeColor(null, arrowHighlightColor, null, null);
          this.lang.nextStep();
          this.outputG.setText(toHexString(G), null, null);
          // highlight ouputG
          this.outputG.changeColor(null, outputTextHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight arrowFBase1, arrowFBase2, arrowF, outputG, inputF
          this.arrowFBase1.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowFBase2.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowF.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.outputG.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.inputF.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
        }
        F = E;
        if (t < mainLoopLimit) {
          this.mainLoopCode.toggleHighlight(11, 12);
          // highlight arrowEBase1, arrowEBase2, arrowEBase3, arrowE, input E
          this.inputE.changeColor(null, inputTextHighlightColor, null, null);
          this.arrowEBase1.changeColor(null, arrowHighlightColor, null, null);
          this.arrowEBase2.changeColor(null, arrowHighlightColor, null, null);
          this.arrowEBase3.changeColor(null, arrowHighlightColor, null, null);
          this.arrowE.changeColor(null, arrowHighlightColor, null, null);
          this.lang.nextStep();
          this.outputF.setText(toHexString(F), null, null);
          // highlight ouputF
          this.outputF.changeColor(null, outputTextHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight arrowEBase1, arrowEBase2, arrowEBase3, arrowE, outputF, inputE
          this.arrowEBase1.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowEBase2.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowEBase3.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowE.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.outputF.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.inputE.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
        }
        E = D + temp1;
        if (t < mainLoopLimit) {
          this.mainLoopCode.toggleHighlight(12, 13);
          // highlight arrowD, arrowSumSecondBase, arrowSumSecondD, arrowSumD, inputD, valueTemp1
          this.inputD.changeColor(null, inputTextHighlightColor, null, null);
          this.arrowD.changeColor(null, arrowHighlightColor, null, null);
          this.arrowSumSecondBase.changeColor(null, arrowHighlightColor, null, null);
          this.arrowSumSecondD.changeColor(null, arrowHighlightColor, null, null);
          this.arrowSumD.changeColor(null, arrowHighlightColor, null, null);
          this.valueTemp1.changeColor(null, this.intermediateHighlightColor, null, null);
          this.lang.nextStep();
          this.outputE.setText(toHexString(E), null, null);
          // highlight ouputF
          this.outputE.changeColor(null, outputTextHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight arrowD, arrowSumSecondBase, arrowSumSecondD, arrowSumD, inputD, valueTemp1, outputE
          this.arrowD.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowSumSecondBase.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowSumSecondD.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowSumD.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.outputE.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.inputD.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.valueTemp1.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
        }
        D = C;
        if (t < mainLoopLimit) {
          this.mainLoopCode.toggleHighlight(13, 14);
          // highlight arrowC, inputC
          this.inputC.changeColor(null, inputTextHighlightColor, null, null);
          this.arrowC.changeColor(null, arrowHighlightColor, null, null);
          this.lang.nextStep();
          this.outputD.setText(toHexString(D), null, null);
          // highlight ouputD
          this.outputD.changeColor(null, outputTextHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight arrowC, outputD, inputC
          this.arrowC.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.outputD.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.inputC.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
        }
        C = B;
        if (t < mainLoopLimit) {
          this.mainLoopCode.toggleHighlight(14, 15);
          // highlight arrowB, inputB
          this.inputB.changeColor(null, inputTextHighlightColor, null, null);
          this.arrowB.changeColor(null, arrowHighlightColor, null, null);
          this.lang.nextStep();
          this.outputC.setText(toHexString(C), null, null);
          // highlight ouputC
          this.outputC.changeColor(null, outputTextHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight arrowB, outputC, inputB
          this.arrowB.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.outputC.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.inputB.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
        }
        B = A;
        if (t < mainLoopLimit) {
          this.mainLoopCode.toggleHighlight(15, 16);
          // highlight arrowABase, arrowA, inputA
          this.inputA.changeColor(null, inputTextHighlightColor, null, null);
          this.arrowA.changeColor(null, arrowHighlightColor, null, null);
          this.arrowABase.changeColor(null, arrowHighlightColor, null, null);
          this.lang.nextStep();
          this.outputB.setText(toHexString(B), null, null);
          // highlight ouputB
          this.outputB.changeColor(null, outputTextHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight arrowA, outputB, inputA
          this.arrowA.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowABase.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.outputB.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.inputA.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
        }
        A = temp1 + temp2;
        if (t < mainLoopLimit) {
          this.mainLoopCode.toggleHighlight(16, 17);
          // highlight valueTemp1, valueTemp2, arrowSumFourth, arrowSumFifth, arrowSumSecondBase, arrowSumSecond
          this.valueTemp1.changeColor(null, this.intermediateHighlightColor, null, null);
          this.valueTemp2.changeColor(null, this.intermediateHighlightColor, null, null);
          this.arrowSumFourth.changeColor(null, arrowHighlightColor, null, null);
          this.arrowSumFifth.changeColor(null, arrowHighlightColor, null, null);
          this.arrowSumSecondBase.changeColor(null, arrowHighlightColor, null, null);
          this.arrowSumSecond.changeColor(null, arrowHighlightColor, null, null);
          this.lang.nextStep();
          this.outputA.setText(toHexString(A), null, null);
          // highlight ouputB
          this.outputA.changeColor(null, outputTextHighlightColor, null, null);
          this.lang.nextStep();
          // unhighlight valueTemp1, valueTemp2, arrowSumFourth, arrowSumFifth, arrowSumSecondBase, arrowSumSecond, ouputB
          this.valueTemp1.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          this.valueTemp2.changeColor(null, (Color) this.mainloopIntermediateTextProps.get("color"), null, null);
          this.arrowSumFourth.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowSumFifth.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowSumSecondBase.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.arrowSumSecond.changeColor(null, (Color) this.mainloopArrowProps.get("color"), null, null);
          this.outputB.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.mainLoopCode.unhighlight(17);
          
          // highlight outputA-H
          this.outputA.changeColor(null, outputTextHighlightColor, null, null);
          this.outputB.changeColor(null, outputTextHighlightColor, null, null);
          this.outputC.changeColor(null, outputTextHighlightColor, null, null);
          this.outputD.changeColor(null, outputTextHighlightColor, null, null);
          this.outputE.changeColor(null, outputTextHighlightColor, null, null);
          this.outputF.changeColor(null, outputTextHighlightColor, null, null);
          this.outputG.changeColor(null, outputTextHighlightColor, null, null);
          this.outputH.changeColor(null, outputTextHighlightColor, null, null);
          
          this.lang.nextStep();
          // unhighlight outputA-H
          this.outputA.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.outputB.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.outputC.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.outputD.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.outputE.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.outputF.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.outputG.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          this.outputH.changeColor(null, (Color) this.mainloopOutputTextProps.get("color"), null, null);
          // highlight inputA-H
          this.inputA.changeColor(null, inputTextHighlightColor, null, null);
          this.inputB.changeColor(null, inputTextHighlightColor, null, null);
          this.inputC.changeColor(null, inputTextHighlightColor, null, null);
          this.inputD.changeColor(null, inputTextHighlightColor, null, null);
          this.inputE.changeColor(null, inputTextHighlightColor, null, null);
          this.inputF.changeColor(null, inputTextHighlightColor, null, null);
          this.inputG.changeColor(null, inputTextHighlightColor, null, null);
          this.inputH.changeColor(null, inputTextHighlightColor, null, null);
          
          this.inputA.setText(toHexString(A), null, null);
          this.inputB.setText(toHexString(B), null, null);
          this.inputC.setText(toHexString(C), null, null);
          this.inputD.setText(toHexString(D), null, null);
          this.inputE.setText(toHexString(E), null, null);
          this.inputF.setText(toHexString(F), null, null);
          this.inputG.setText(toHexString(G), null, null);
          this.inputH.setText(toHexString(H), null, null);
          this.outputA.setText("", null, null);
          this.outputB.setText("", null, null);
          this.outputC.setText("", null, null);
          this.outputD.setText("", null, null);
          this.outputE.setText("", null, null);
          this.outputF.setText("", null, null);
          this.outputG.setText("", null, null);
          this.outputH.setText("", null, null);
          
          this.valueK.setText("K[t]", null, null);
          this.valueW.setText("W[t]", null, null);
          this.valueWK.setText("", null, null);
          this.valueCh.setText("", null, null);
          this.valueChH.setText("", null, null);
          this.valueE1.setText("", null, null);
          this.valueTemp1.setText("", null, null);
          this.valueMa.setText("", null, null);
          this.valueE0.setText("", null, null);
          this.valueTemp2.setText("", null, null);
          this.lang.nextStep();
          //unhighlight inputA-H
          this.inputA.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.inputB.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.inputC.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.inputD.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.inputE.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.inputF.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.inputG.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          this.inputH.changeColor(null, (Color) this.mainloopInputTextProps.get("color"), null, null);
          
          this.lang.nextStep();
        }
      }
      
      this.lang.nextStep();
      
      
      this.lang.addLine("hideAll");
      this.headlineText.show();
      this.headlineRect.show();
      this.stepTitle.show();
      this.stepTitle.setText("2.3 Rahmenschleife: Ergebnis anhängen", null, null);
      
      this.resultSourceCode = this.lang.newSourceCode(new Offset(0, 5, this.stepTitle, "SW"), "resultSourceCode", null, this.sourceCodeProps);
      this.resultSourceCode.addCodeLine("Nach jedem Rahmenschleifen-Durchlauf werden die Zwischenergebnisse", null, 0, null);
      this.resultSourceCode.addCodeLine("für A bis H auf H0 bis H7 aufaddiert.", null, 0, null);
      this.resultSourceCode.addCodeLine("Im nächsten Durchlauf werden dann statt der Initialwerte diese Werte für H0 bis H7 verwendet.", null, 0, null);
      this.resultSourceCode.addCodeLine("", null, 0, null);
      
      this.lang.nextStep("2.3 Ergebnis anhängen");
      this.resultSourceCode.addCodeLine("H0 = H0 + A", null, 0, null);
      this.resultSourceCode.addCodeLine("H1 = H1 + B", null, 0, null);
      this.resultSourceCode.addCodeLine("H2 = H2 + C", null, 0, null);
      this.resultSourceCode.addCodeLine("H3 = H3 + D", null, 0, null);
      this.resultSourceCode.addCodeLine("H4 = H4 + E", null, 0, null);
      this.resultSourceCode.addCodeLine("H5 = H5 + F", null, 0, null);
      this.resultSourceCode.addCodeLine("H6 = H6 + G", null, 0, null);
      this.resultSourceCode.addCodeLine("H7 = H7 + H", null, 0, null);
      
      this.lang.nextStep();
      this.resultSourceCode.hide();
      this.resultSourceCode = this.lang.newSourceCode(new Offset(0, 5, this.stepTitle, "SW"), "resultSourceCode", null, this.sourceCodeProps);
      this.resultSourceCode.addCodeLine("Nach jedem Rahmenschleifen-Durchlauf werden die Zwischenergebnisse", null, 0, null);
      this.resultSourceCode.addCodeLine("für A bis H auf H0 bis H7 aufaddiert.", null, 0, null);
      this.resultSourceCode.addCodeLine("Im nächsten Durchlauf werden dann statt der Initialwerte diese Werte für H0 bis H7 verwendet.", null, 0, null);
      this.resultSourceCode.addCodeLine("", null, 0, null);
      this.resultSourceCode.addCodeLine("H0 = " + toHexString(H0) + " + " + toHexString(A) + " = " + toHexString(H0 + A), null, 0, null);
      this.resultSourceCode.addCodeLine("H1 = " + toHexString(H1) + " + " + toHexString(B) + " = " + toHexString(H1 + B), null, 0, null);
      this.resultSourceCode.addCodeLine("H2 = " + toHexString(H2) + " + " + toHexString(C) + " = " + toHexString(H2 + C), null, 0, null);
      this.resultSourceCode.addCodeLine("H3 = " + toHexString(H3) + " + " + toHexString(D) + " = " + toHexString(H3 + D), null, 0, null);
      this.resultSourceCode.addCodeLine("H4 = " + toHexString(H4) + " + " + toHexString(E) + " = " + toHexString(H4 + E), null, 0, null);
      this.resultSourceCode.addCodeLine("H5 = " + toHexString(H5) + " + " + toHexString(F) + " = " + toHexString(H5 + F), null, 0, null);
      this.resultSourceCode.addCodeLine("H6 = " + toHexString(H6) + " + " + toHexString(G) + " = " + toHexString(H6 + G), null, 0, null);
      this.resultSourceCode.addCodeLine("H7 = " + toHexString(H7) + " + " + toHexString(H) + " = " + toHexString(H7 + H), null, 0, null);

      H0 = H0 + A;
      H1 = H1 + B;
      H2 = H2 + C;
      H3 = H3 + D;
      H4 = H4 + E;
      H5 = H5 + F;
      H6 = H6 + G;
      H7 = H7 + H;
      
      this.lang.nextStep();
    }
    
    this.stepTitle.setText("3 Ergebnishash berechnen", null, null);
    
    // 256 bit number:
    // magic
    String res = "" + toHexString(H0) + toHexString(H1) + toHexString(H2) +
        toHexString(H3) + toHexString(H4) + toHexString(H5) +
        toHexString(H6) + toHexString(H7);
    
    this.resultSourceCode.hide();
    this.resultSourceCode = this.lang.newSourceCode(new Offset(0, 5, this.stepTitle, "SW"), "resultSourceCode", null, this.sourceCodeProps);
    
    if (!isMoreThanOneBlock) {
      this.resultSourceCode.addCodeLine("(Da nur ein Block der Größe 512 Bit berechnet werden musste,", null, 0, null);
      this.resultSourceCode.addCodeLine("kann sofort das Endergebnis erzeugt werden.)", null, 0, null);
      this.resultSourceCode.addCodeLine("", null, 0, null);
    }
    this.resultSourceCode.addCodeLine("Nachdem alle Blöcke abgearbeitet wurden, wird das Ergebnis durch", null, 0, null);
    this.resultSourceCode.addCodeLine("Konkatenation der Variablen H0 bis H7 berechnet.", null, 0, null);
    this.resultSourceCode.addCodeLine("Das Ergebnis ist somit 8 * 32 Bit = 256 Bit lang.", null, 0, null);
    this.resultSourceCode.addCodeLine("", null, 0, null);
    this.resultSourceCode.addCodeLine("result = H0 \u2218 H1 \u2218 H2 \u2218 H3 \u2218 H4 \u2218 H5 \u2218 H6 \u2218 H7", null, 0, null);
    this.resultSourceCode.addCodeLine("(wobei \u2218 die Konkatenation bezeichnet)", null, 1, null);

    this.lang.nextStep("3 Ergebnishash berechnen");
    
    this.resultSourceCode.addCodeLine("", null, 0, null);
    this.resultSourceCode.addCodeLine("", null, 0, null);
    this.resultSourceCode.addCodeLine("H0 = " + toHexString(H0), null, 0, null);
    this.resultSourceCode.addCodeLine("H1 = " + toHexString(H1), null, 0, null);
    this.resultSourceCode.addCodeLine("H2 = " + toHexString(H2), null, 0, null);
    this.resultSourceCode.addCodeLine("H3 = " + toHexString(H3), null, 0, null);
    this.resultSourceCode.addCodeLine("H4 = " + toHexString(H4), null, 0, null);
    this.resultSourceCode.addCodeLine("H5 = " + toHexString(H5), null, 0, null);
    this.resultSourceCode.addCodeLine("H6 = " + toHexString(H6), null, 0, null);
    this.resultSourceCode.addCodeLine("H7 = " + toHexString(H7), null, 0, null);
    this.resultSourceCode.addCodeLine("", null, 0, null);
    this.resultSourceCode.addCodeLine("", null, 0, null);
    this.resultSourceCode.addCodeLine("SHA('" + message +"') = " + res, null, 0, null);
    this.lang.nextStep();
  }

  /*
   * Helper functions
   */
  public final byte[] add(byte[] one, int two){
    byte[] temp = new byte[4];
    temp[0] = (byte) (two >> 24);
    temp[1] = (byte) (two >> 16);
    temp[2] = (byte) (two >> 8);
    temp[3] = (byte) (two);
    return add(one,temp);
  }

  public final byte[] add(byte[] one, byte two){
    byte[] temp = new byte[1];
    temp[0] = two;
    return add(one,temp);
  }

  public final byte[] add(byte[] one, long two){
    byte[] temp = new byte[8];
    temp[0] = (byte) (two >> 56);
    temp[1] = (byte) (two >> 48);
    temp[2] = (byte) (two >> 40);
    temp[3] = (byte) (two >> 32);
    temp[4] = (byte) (two >> 24);
    temp[5] = (byte) (two >> 16);
    temp[6] = (byte) (two >> 8);
    temp[7] = (byte) (two);
    return add(one,temp);
  }

  public final byte[] add(byte[] one, byte[] two){
    byte[] combined = new byte[one.length + two.length];

    System.arraycopy(one,0,combined,0         ,one.length);
    System.arraycopy(two,0,combined,one.length,two.length);

    return combined;
  }

  private final int rightrotate(int x, int count){
    return (((x >>> count)) | (x << (32 - count)));
  }
  
  private String toHexString(int two) {
    byte[] temp = new byte[4];
    temp[0] = (byte) (two >>> 24);
    temp[1] = (byte) (two >>> 16);
    temp[2] = (byte) (two >>> 8);
    temp[3] = (byte) (two);
    return toHexString(temp);
  }

  
  private static String toHexString(byte[] b)
  {
    final String hexChar = "0123456789ABCDEF";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < b.length; i++)
    {
      sb.append(hexChar.charAt((b[i] >> 4) & 0x0f));
      sb.append(hexChar.charAt(b[i] & 0x0f));
    }
    return sb.toString();
  }

  public String getName() {
    return "SHA-256[DE]";
  }

  public String getAlgorithmName() {
    return "SHA-256";
  }

  public String getAnimationAuthor() {
    return "Steven Lamarr Reynolds,Simon Bugert";
  }

  public String getDescription() {
    return "Der SHA-256 Algorithmus (Secure Hash Algorithm) ist eine kryptologische Hashfunktion, "
        + "und eine Variante von SHA-2 die 2001 von der National Security Agency in den USA "
        + "veröffentlicht wurde. Ziel war es den schwächeren SHA-1 Algorithmus um ein längeres "
        + "Hash-Resultat mit 224, 256, 384 bzw 512 bits zu verbessern. Im Jahre 2005 wurden erste "
        + "Angriffe auf SHA-1 gefunden die troz der Ähnlichkeit zu SHA-256 nicht darauf angewandt "
        + "werden konnten.";
  }

  public String getCodeExample() {
    return "Note 1: All variables are 32 bit unsigned integers and addition is calculated modulo 2^32 each round, there is one round constant k[i] and one entry in the message schedule array w[i], 0 <= i <= 63"
        + "\n"
        + "Note 3: The compression function uses 8 working variables, a through h"
        + "\n"
        + "Note 4: Big-endian convention is used when expressing the constants in this pseudocode,"
        + "\n"
        + "    and when parsing message block data from bytes to words, for example,"
        + "\n"
        + "    the first word of the input message \"abc\" after padding is 0x61626380"
        + "\n"
        + ""
        + "\n"
        + "Initialize hash values:"
        + "\n"
        + "(first 32 bits of the fractional parts of the square roots of the first 8 primes 2..19):"
        + "\n"
        + "h0 := 0x6a09e667"
        + "\n"
        + "h1 := 0xbb67ae85"
        + "\n"
        + "h2 := 0x3c6ef372"
        + "\n"
        + "h3 := 0xa54ff53a"
        + "\n"
        + "h4 := 0x510e527f"
        + "\n"
        + "h5 := 0x9b05688c"
        + "\n"
        + "h6 := 0x1f83d9ab"
        + "\n"
        + "h7 := 0x5be0cd19"
        + "\n"
        + ""
        + "\n"
        + "Initialize array of round constants:"
        + "\n"
        + "(first 32 bits of the fractional parts of the cube roots of the first 64 primes 2..311):"
        + "\n"
        + "k[0..63] :="
        + "\n"
        + "   0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,"
        + "\n"
        + "   0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,"
        + "\n"
        + "   0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,"
        + "\n"
        + "   0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,"
        + "\n"
        + "   0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,"
        + "\n"
        + "   0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,"
        + "\n"
        + "   0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,"
        + "\n"
        + "   0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2"
        + "\n"
        + ""
        + "\n"
        + "Pre-processing:"
        + "\n"
        + "append the bit '1' to the message"
        + "\n"
        + "append k bits '0', where k is the minimum number >= 0 such that the resulting message"
        + "\n"
        + "    length (modulo 512 in bits) is 448."
        + "\n"
        + "append length of message (without the '1' bit or padding), in bits, as 64-bit big-endian integer"
        + "\n"
        + "    (this will make the entire post-processed length a multiple of 512 bits)"
        + "\n"
        + ""
        + "\n"
        + "Process the message in successive 512-bit chunks:"
        + "\n"
        + "break message into 512-bit chunks"
        + "\n"
        + "for each chunk"
        + "\n"
        + "    create a 64-entry message schedule array w[0..63] of 32-bit words"
        + "\n"
        + "    (The initial values in w[0..63] don't matter, so many implementations zero them here)"
        + "\n"
        + "    copy chunk into first 16 words w[0..15] of the message schedule array"
        + "\n"
        + ""
        + "\n"
        + "    Extend the first 16 words into the remaining 48 words w[16..63] of the message schedule array:"
        + "\n"
        + "    for i from 16 to 63"
        + "\n"
        + "        s0 := (w[i-15] rightrotate 7) xor (w[i-15] rightrotate 18) xor (w[i-15] rightshift 3)"
        + "\n"
        + "        s1 := (w[i-2] rightrotate 17) xor (w[i-2] rightrotate 19) xor (w[i-2] rightshift 10)"
        + "\n"
        + "        w[i] := w[i-16] + s0 + w[i-7] + s1"
        + "\n"
        + ""
        + "\n"
        + "    Initialize working variables to current hash value:"
        + "\n"        
        + "    a := h0"
        + "\n"
        + "    b := h1"
        + "\n"
        + "    c := h2"
        + "\n"
        + "    d := h3"
        + "\n"
        + "    e := h4"
        + "\n"
        + "    f := h5"
        + "\n"
        + "    g := h6"
        + "\n"
        + "    h := h7"
        + "\n"
        + ""
        + "\n"
        + "    Compression function main loop:"
        + "\n"
        + "    for i from 0 to 63"
        + "\n"
        + "        S1 := (e rightrotate 6) xor (e rightrotate 11) xor (e rightrotate 25)"
        + "\n"
        + "        ch := (e and f) xor ((not e) and g)"
        + "\n"
        + "        temp1 := h + S1 + ch + k[i] + w[i]"
        + "\n"
        + "        S0 := (a rightrotate 2) xor (a rightrotate 13) xor (a rightrotate 22)"
        + "\n"
        + "        maj := (a and b) xor (a and c) xor (b and c)"
        + "\n"
        + "        temp2 := S0 + maj"
        + "\n"
        + " "
        + "\n"
        + "        h := g"
        + "\n"
        + "        g := f"
        + "\n"
        + "        f := e"
        + "\n"
        + "        e := d + temp1"
        + "\n"
        + "        d := c"
        + "\n"
        + "        c := b"
        + "\n"
        + "        b := a"
        + "\n"
        + "        a := temp1 + temp2"
        + "\n"
        + ""
        + "\n"
        + "    Add the compressed chunk to the current hash value:"
        + "\n"
        + "    h0 := h0 + a"
        + "\n"
        + "    h1 := h1 + b"
        + "\n"
        + "    h2 := h2 + c"
        + "\n"
        + "    h3 := h3 + d"
        + "\n"
        + "    h4 := h4 + e"
        + "\n"
        + "    h5 := h5 + f"
        + "\n"
        + "    h6 := h6 + g"
        + "\n"
        + "    h7 := h7 + h"
        + "\n"
        + ""
        + "\n"
        + "Produce the final hash value (big-endian):"
        + "\n"
        +"digest := hash := h0 append h1 append h2 append h3 append h4 append h5 append h6 append h7";
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
        }
      }
      throw new IllegalArgumentException("Error: Message is too long");
    }


}
