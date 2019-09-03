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
 * @author ek
 */
public class VigenereDecode2 implements Generator {
  private Hashtable<String, Object> propertyMapper = null;

  public VigenereDecode2(Hashtable<String, Object> dataSet) {
    propertyMapper = dataSet;
  }

  private EKColor decodeColor(String key) {
    java.awt.Color readColor = (java.awt.Color) propertyMapper.get(key);
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
    generateDecode(w);
    // o.close();
  }

  public void generateDecode(AnimalScriptWriter w) {
    w.clear();
    String data = ((String) propertyMapper.get("stringToDecode")).toUpperCase();
    String key = ((String) propertyMapper.get("key")).toUpperCase();
    String zeichensatz = (String) propertyMapper.get("charSet");

    w.startBlock();
    w.addLabel("Description");
    // "Vigenere Entschluesselung"
    String title = (String) propertyMapper.get("title");
    Text tx = w.createText(w.abs(10, 40), title);
    tx.setColor(decodeColor("titleColor"));
    tx.setFont(decodeFont("titleFont"));

    w.createRectangle(tx.createOffset(0, 5, AnimalObject.SW), tx.createOffset(
        5, 6, AnimalObject.SE));

    CodeGroup g = w.createCodeGroup(tx.createOffset(0, 12, AnimalObject.SW));
    g.setFont(decodeFont("codeFont"));
    g.setColor(decodeColor("codeColor"));
    // "Zu dekodierender Text: "
    String decodingLabel = (String) propertyMapper.get("decodeLabel");
    g.addCodeLine(decodingLabel + data);

    // "Schluessel: "
    String keyLabel = (String) propertyMapper.get("keyLabel");
    g.addCodeLine(keyLabel + key);
    g.addCodeLine("");
    Chart chart = new Chart(w, w.abs(10, 360), data.length(), 2);
    chart.setCharsHorizontaly(0, 0, data);
    chart.register();
    w.endBlock();

    w.addLabel("Assign Key Chars");
    // "Den Schluessel wiederholt unter den zu dekodierenden Text schreiben"
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

    w.addLabel("Create Chart");
    // "Die gleiche Tabelle wie bei der Verschluesselung verwenden."
    String tableCreateLabel = (String) propertyMapper.get("tableCreateLabel");
    g.addCodeLine(tableCreateLabel);

    w.startBlock();
    int i;
    Chart tabelle = new Chart(w, chart.createOffset(50, 200, AnimalObject.S),
        zeichensatz.length(), key.length() + 1);
    tabelle.setCharsHorizontaly(0, 0, zeichensatz);

    // Fuellfarbe fuer erste Tabellenzeile
    EKColor clRow = decodeColor("tableRowColor");
    EKColor clCol = decodeColor("tableColumnColor");
    EKColor clRest = decodeColor("tableFillColor");
    tabelle.setFillColorRect(0, 0, zeichensatz.length(), 1, clRow);
    tabelle.setFillColorRect(0, 1, 1, key.length(), clCol);
    tabelle.setFillColorRect(1, 1, zeichensatz.length() - 1, key.length(),
        clRest);
    tabelle.register();
    // "In die erste Spalte vertikal das Schluesselwort eintragen"
    // g.addCodeLine(lb5.getValue());
    tabelle.setCharsVerticaly(0, 1, key);
    /*
     * for(i = 0; i < key.length(); i++) {
     * tabelle.getElementAt(0,i+1).setText("" + key.charAt(i));
     * tabelle.getElementAt(0,
     * i+1).getRectangle().setFillColor(Color.createFromString(clChartSpalte.getValue())); }
     */
    for (i = 0; i < key.length(); i++) {

      w.startBlock();
      char keyChar = key.charAt(i);
      int keyCharPos = zeichensatz.indexOf("" + keyChar);
      String s = zeichensatz.substring(keyCharPos + 1);
      s = s + zeichensatz.substring(0, keyCharPos);
      tabelle.setCharsHorizontaly(1, i + 1, s);
    }
    w.endBlock();

    // "Jetzt in der Zeile des Schluesselzeichens die Spalte mit dem
    // Datenzeichen suchen"
    String searchRowLabel = (String) propertyMapper.get("searchRow");
    g.addCodeLine(searchRowLabel);

    w.addLabel("begin decryption");
    PolyLine p = null;
    PolyLine p2 = null;
    TextBox lastChar = null;
    boolean firstChar = true;
    int steps = ((Integer) propertyMapper.get("numberSteps")).intValue();
    if (steps == 0)
      steps = 1000;
    for (i = 0; (i < data.length()) && (i < steps); i++) {
      w.startBlock();
      w.addLabel("decrypt '" + data.charAt(i) + "'");
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
      int x = (zeichensatz.length() + zeichensatz.indexOf(data.charAt(i)) - zeichensatz
          .indexOf(key.charAt(i % key.length())))
          % zeichensatz.length();
      n = tabelle.getElementAt(x, 0).createOffset(0, -30, AnimalObject.N);
      p.addNode(n);
      p.addNode(n.createOffset(0, 30));
      p.setArrow(PolyLine.ARROW_FORWARD);
      p.register();

      // Pfeil auf Schluesselzeile
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

      TextBox b = tabelle.getElementAt(x, 0); // Buchstaben aus der ersten Zeile
      TextBox b2 = new TextBox(w, b.getPosition(), b.getText().getValue());
      TextBox b3 = tabelle.getElementAt(x, y);
      b3.getRectangle().setFillColor(EKColor.ORANGE);
      b2.register();
      b2.getRectangle().setFillColor(decodeColor("resultColor"));

      if (lastChar == null)
        n = tabelle.createOffset(-30, 30, AnimalObject.SW);
      else
        n = lastChar.createOffset(5, 0, AnimalObject.NE);
      w.endBlock();
      b2.moveTo(n);
      w.startBlock();
      // alte Füllfarbe der Tabelle herstellen
      EKColor bkcolor = decodeColor("backgroundColor");
      if (x == 0)
        bkcolor = decodeColor("backgroundColFirstRow");
      b3.getRectangle().setFillColor(bkcolor);
      lastChar = b2;
      if (firstChar) {
        firstChar = false;
        String repeatLabel = (String) propertyMapper.get("codePosLabel");
        g.addCodeLine(repeatLabel);
      }

    }
    // Die restlichen kodierten Zeichen ohne Animation einblenden
    w.startBlock();
    p.setHidden(true);
    p2.setHidden(true);
    for (; i < data.length(); i++) {
      int x = (zeichensatz.length() + zeichensatz.indexOf(data.charAt(i)) - zeichensatz
          .indexOf(key.charAt(i % key.length())))
          % zeichensatz.length();
      int y = 0;
      TextBox srcTB = tabelle.getElementAt(x, y);
      EKNode n = lastChar.getRectangle().createOffset(5, 0, AnimalObject.SE);
      TextBox b2 = new TextBox(w, n, srcTB.getText().getValue());
      b2.register();
      b2.getRectangle().setFillColor(decodeColor("resultColor"));
      lastChar = b2;
    }
    w.endBlock();
    /*
     * for(i = 0; i < width; i++) { chart.getElementAt(i,
     * 0).getRectangle().setFillColor("blue");
     * chart.getElementAt(i,0).getText().setHidden(false); }
     * 
     * PolyLine p = new PolyLine(w);
     * p.addNode(chart.getElementAt(0,1).createOffset(0,0, PolyLine.S));
     * p.addNode(chart.getElementAt(0,1).createOffset(0,100, PolyLine.S));
     * p.addNode(chart.getElementAt(3,1).createOffset(0,100, PolyLine.S));
     * p.addNode(chart.getElementAt(3,1).createOffset(0,0, PolyLine.S));
     * p.setArrow(PolyLine.ARROW_FORWARD); p.register();
     */

  }

  /*
   * (non-Javadoc)
   * 
   * @see generatorgui.Generator#getGeneratorName()
   */
  public String getGeneratorName() {
    return "Vigenere Decryption";
  }

  /*
   * (non-Javadoc)
   * 
   * @see generatorgui.Generator#getProperties()
   */
  public Property[] getProperties() {
    return null;
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