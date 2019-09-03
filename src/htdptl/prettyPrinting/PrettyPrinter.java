package htdptl.prettyPrinting;

import htdptl.ast.AST;
import htdptl.util.Util;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

/**
 * The PrettyPrinter is used to add AST objects to an animation. It creates a
 * sourcecode primitive and computes the positions of linebreaks using the
 * LineBreaker class
 * 
 */
public class PrettyPrinter {

  protected SourceCode           sourceCode;
  protected SourceCodeProperties scProps;
  protected Language             lang;
  protected Node                 target;
  private LineBreaker            prettyPrinter;
  private ExpressionPositions    expressionPositions;
  private Highlighter            highlighter;

  public PrettyPrinter(Language lang) {

    scProps = new SourceCodeProperties();
    scProps.set(AnimationPropertiesKeys.FONT_PROPERTY, Util.getFont());
    scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Util
        .getHighlightColor());
    scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

    this.lang = lang;
    this.target = defaultTarget();
  }

  public Node defaultTarget() {
    return new Coordinates(20, 20);
  }

  public void print(AST ast, AST redex, String redexValue,
      ArrayList<AST> expressions) {
    print(ast, redex, redexValue, expressions, ExpressionPositions.IDENTICAL);
  }

  public void print(AST ast, AST redex, String redexValue,
      ArrayList<AST> expressions, int mode) {
    if (sourceCode != null) {
      hide();
    }
    sourceCode = lang.newSourceCode(target, "", null, scProps);
    expressionPositions = new ExpressionPositions(expressions, mode);
    prettyPrinter = new LineBreaker(ast, redex, redexValue, expressionPositions);
    highlighter = new Highlighter(prettyPrinter, expressionPositions);
    draw();
  }

  private void draw() {
    ArrayList<ArrayList<String>> lines = prettyPrinter.getLines();
    for (int i = 0; i < lines.size(); i++) {
      ArrayList<String> line = lines.get(i);
      int indentation = prettyPrinter.getIndentations().get(i);
      sourceCode.addCodeLine(Util.escape(line.get(0)), null, indentation, null);
      if (line.size() > 1) {
        for (int j = 1; j < line.size(); j++) {
          boolean inline = false;
          if (j > 0) {
            inline = line.get(j - 1).endsWith("(")
                || line.get(j - 1).endsWith("[") || line.get(j).startsWith(")")
                || line.get(j).startsWith("]");
          }
          sourceCode.addCodeElement(Util.escape(line.get(j)), "", inline,
              indentation, null);
        }
      }
    }
  }

  public void hide() {
    sourceCode.hide();
  }

  public Primitive getSourceCode() {
    return sourceCode;
  }

  public void setTarget(Node target) {
    this.target = target;
  }

  public void highlight(ArrayList<AST> elements, boolean highlight) {
    highlighter.highlight(sourceCode, elements, highlight);
  }

  public void highlight(AST ast, boolean highlight) {
    ArrayList<AST> elements = new ArrayList<AST>();
    elements.add(ast);
    highlight(elements, highlight);
  }

  public void highlightRedex() {
    highlighter.highlightRedex(sourceCode);
  }

  public void highlight(int row, int col) {
    sourceCode.highlight(row, col, false);
  }

  public void unhighlight(int row, int col) {
    sourceCode.unhighlight(row, col, false);
  }

  public int getRedexPosition() {
    return prettyPrinter.getRedexPosition();
  }

  public void highlight(int i) {
    sourceCode.highlight(i);
  }

  public void highlightBlock(int line, int block) {
    highlighter.highlightBlock(line, block, sourceCode);
  }

  public ArrayList<Point> getExpressionPositions() {
    return expressionPositions.getPoints();
  }

}
