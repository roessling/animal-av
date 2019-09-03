/*
 * Created on 08.12.2004
 */

package de.ahrgr.animal.kohnert.generators;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Hashtable;

import de.ahrgr.animal.kohnert.asugen.AnimalObject;
import de.ahrgr.animal.kohnert.asugen.AnimalScriptWriter;
import de.ahrgr.animal.kohnert.asugen.Chart;
import de.ahrgr.animal.kohnert.asugen.CodeGroup;
import de.ahrgr.animal.kohnert.asugen.EKColor;
import de.ahrgr.animal.kohnert.asugen.EKFont;
import de.ahrgr.animal.kohnert.asugen.Generator;
import de.ahrgr.animal.kohnert.asugen.EKNode;
import de.ahrgr.animal.kohnert.asugen.PolyLine;
import de.ahrgr.animal.kohnert.asugen.Text;
import de.ahrgr.animal.kohnert.asugen.TextBox;
import de.ahrgr.animal.kohnert.asugen.property.Property;

/**
 * Adapter class that encodes a Vigenere encryption for Tobias Ackermann's
 * "GeneratorGUI" framework.
 * 
 * @author Dr. Guido R&ouml;&szlig;ling (roessling@acm.org>
 * @version 1.0 2005-09-28
 */
public class VigenereEncode2 implements Generator {

  private Hashtable<String, Object> propertyMapper = null;

  public VigenereEncode2(Hashtable<String, Object> dataSet) {
    propertyMapper = dataSet;
  }

  private EKColor decodeColor(String key) {
    java.awt.Color readColor = (java.awt.Color) propertyMapper.get(key);
    if (readColor == null)
    	readColor = java.awt.Color.BLACK;
    return new EKColor(readColor);
  }

  private EKFont decodeFont(String key) {
    java.awt.Font readFont = (java.awt.Font) propertyMapper.get(key);
    return new EKFont(readFont);
  }

  public void generate(Writer out) {
    PrintWriter o = null;
    o = new PrintWriter(out);
    AnimalScriptWriter w = new AnimalScriptWriter(o);

    generateEncode(w);
    // generateDecode(w);
  }

  public void generateEncode(AnimalScriptWriter w) {

    String data = ((String) propertyMapper.get("stringToEncode")).toUpperCase();
    String key = ((String) propertyMapper.get("key")).toUpperCase();
    String zeichensatz = (String) propertyMapper.get("charSet");

    w.startBlock();
    w.addLabel("Description");

    String title = (String) propertyMapper.get("title");
    // "Vigenere Verschluesselung"
    Text tx = w.createText(w.abs(10, 40), title);
    tx.setColor(decodeColor("titleColor"));
    tx.setFont(decodeFont("titleFont"));
    // tx.setFont(new Font(Font.SANSSERIF, true, false, 20));
    // tx.setColor(Color.BLACK);
    w.createRectangle(tx.createOffset(0, 5, AnimalObject.SW), tx.createOffset(
        5, 6, AnimalObject.SE));

    CodeGroup g = w.createCodeGroup(tx.createOffset(0, 12, AnimalObject.SW));
    g.setFont(decodeFont("codeFont"));
    g.setColor(decodeColor("codeColor"));
    // g.setFont(Font.FT_SANSSERIF);
    // g.setColor(Color.BLACK);
    // "Zu kodierender Text: "
    String encodingLabel = (String) propertyMapper.get("encodeLabel");
    g.addCodeLine(encodingLabel + data);
    // "Schluessel: "
    String keyLabel = (String) propertyMapper.get("keyLabel");
    g.addCodeLine(keyLabel + key);
    g.addCodeLine("");
    Chart chart = new Chart(w, w.abs(10, 360), data.length(), 2);
    chart.setCharsHorizontaly(0, 0, data);
    chart.register();
    w.endBlock();

    w.addLabel("assign key characters");
    // "Den Schluessel wiederholt ueber den zu kodierenden Text schreiben"
    String repeatedCopyLabel = (String) propertyMapper.get("repeatedCopyLabel");
    g.addCodeLine(repeatedCopyLabel);

    int width = data.length();
    int pos = 0;
    while (pos < width) {
      w.startBlock();
      chart.setCharsHorizontaly(pos, 1, key);
      pos += key.length();
      w.endBlock();
    }
    w.addLabel("create chart");
    // "Eine Tabelle mit einer Spalte fuer jedes Zeichen anlegen"
    String tableCreateLabel = (String) propertyMapper.get("tableCreateLabel");
    g.addCodeLine(tableCreateLabel);
    w.startBlock();
    int i;
    Chart tabelle = new Chart(w, chart.createOffset(50, 200, AnimalObject.S),
        zeichensatz.length(), key.length() + 1);
    tabelle.setCharsHorizontaly(0, 0, zeichensatz);

    // Fuellfarbe fuer erste Tabellenzeile
    // Color clRow = Color.CYAN;
    // Color clCol = Color.CYAN2;
    // Color clRest = Color.WHITE;
    EKColor clRow = decodeColor("tableRowColor");
    EKColor clCol = decodeColor("tableColumnColor");
    EKColor clRest = decodeColor("tableFillColor");
    tabelle.setFillColorRect(0, 0, zeichensatz.length(), 1, clRow);
    tabelle.setFillColorRect(0, 1, 1, key.length(), clCol);
    tabelle.setFillColorRect(1, 1, zeichensatz.length() - 1, key.length(),
        clRest);
    tabelle.register();
    w.endBlock();
    w.addLabel("insert key into the chart");
    // "In die erste Spalte vertikal das Schluesselwort eintragen"
    String firstRowLabel = (String) propertyMapper.get("firstRowLabel");
    g.addCodeLine(firstRowLabel);
    w.startBlock();
    tabelle.setCharsVerticaly(0, 1, key);
    w.endBlock();
    w.addLabel("fill the chart");
    // "Die Zeilen mit kontinuierlichem Alphabet auffuellen"
    String fillContinuousLabel = (String) propertyMapper
        .get("fillContinuousLabel");
    g.addCodeLine(fillContinuousLabel);
    for (i = 0; i < key.length(); i++) {
      w.startBlock();
      char keyChar = key.charAt(i);
      int keyCharPos = zeichensatz.indexOf("" + keyChar);
      String s = zeichensatz.substring(keyCharPos + 1);
      s = s + zeichensatz.substring(0, keyCharPos);
      tabelle.setCharsHorizontaly(1, i + 1, s);
      w.endBlock();
    }
    w.addLabel("start encryption");
    // "Kodierte Zeichen stehen in der Zeile des Schluesselzeichens
    // und in der Spalte des Datenzeichens"
    String codePosLabel = (String) propertyMapper.get("codePosLabel");

    g.addCodeLine(codePosLabel);
    PolyLine p = null;
    PolyLine p2 = null;
    TextBox lastChar = null;
    int steps = ((Integer) propertyMapper.get("numberSteps")).intValue();
    if (steps == 0)
      steps = 1000;
    for (i = 0; (i < data.length()) && (i < steps); i++) {
      w.startBlock();
      w.addLabel("encrypt '" + data.charAt(i) + "'");
      if (p != null)
        p.setHidden(true);
      if (p2 != null)
        p2.setHidden(true);
      p = new PolyLine(w);
      EKNode n = chart.getElementAt(i, 0).createOffset(0, 0, AnimalObject.N);
      p.addNode(n);
      p.addNode(n.createOffset(0, -10));
      n = chart.createOffset(10, -10, AnimalObject.NE);
      p.addNode(n);
      n = chart.createOffset(10, 10, AnimalObject.SE);
      p.addNode(n);
      int x = zeichensatz.indexOf(data.charAt(i));
      n = tabelle.getElementAt(x, 0).createOffset(0, -30, AnimalObject.N);
      p.addNode(n);
      p.addNode(n.createOffset(0, 30));
      p.setArrow(PolyLine.ARROW_FORWARD);
      p.register();

      // Pfeil auf Schlüsselzeile
      p2 = new PolyLine(w);
      n = chart.getElementAt(i, 1).createOffset(0, 1, AnimalObject.S);
      p2.addNode(n);
      p2.addNode(n.createOffset(0, 10));
      n = tabelle.createOffset(-50, -50, AnimalObject.NW);
      p2.addNode(n);

      int y = i % key.length() + 1;
      n = tabelle.getElementAt(0, y).createOffset(-50, 0, AnimalObject.W);
      p2.addNode(n);
      p2.addNode(n.createOffset(50, 0));
      p2.setArrow(PolyLine.ARROW_FORWARD);
      p2.register();

      TextBox b = tabelle.getElementAt(x, y);
      TextBox b2 = new TextBox(w, b.getPosition(), b.getText().getValue());
      b2.register();
      // Color clBackground = Color.YELLOW;
      EKColor clBackground = decodeColor("resultColor");
      b2.getRectangle().setFillColor(clBackground);

      if (lastChar == null)
        n = tabelle.createOffset(-30, 30, AnimalObject.SW);
      else
        n = lastChar.createOffset(5, 0, AnimalObject.NE);
      w.endBlock();
      b2.moveTo(n);
      lastChar = b2;

    }
    // Die restlichen kodierten Zeichen ohne Animation einblenden
    w.startBlock();
    p.setHidden(true);
    p2.setHidden(true);
    for (; i < data.length(); i++) {
      int x = zeichensatz.indexOf(data.charAt(i));
      int y = i % key.length() + 1;
      TextBox srcTB = tabelle.getElementAt(x, y);
      EKNode n = lastChar.getRectangle().createOffset(5, 0, AnimalObject.SE);
      TextBox b2 = new TextBox(w, n, srcTB.getText().getValue());
      b2.register();
      EKColor clBackground = EKColor.YELLOW;
      // Color clBackground = (Color)propertyMapper.get("resultColor");
      b2.getRectangle().setFillColor(clBackground);
      lastChar = b2;
    }
    w.endBlock();
  }

  /*
   * (non-Javadoc)
   * 
   * @see generatorgui.Generator#getGeneratorName()
   */
  public String getGeneratorName() {
    return "Vigenere Encryption";
  }

  /*
   * (non-Javadoc)
   * 
   * @see generatorgui.Generator#getProperties()
   */
  public Property[] getProperties() {
    return null; // properties;
  }

  /*
   * (non-Javadoc)
   * 
   * @see generatorgui.Generator#generateScript(java.io.File)
   */
  public void generateScript(Writer out) {
    generate(out);
  }
}
