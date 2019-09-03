/*
 * Created on 08.12.2004
 */

package de.ahrgr.animal.kohnert.generators;

import java.io.PrintWriter;
import java.io.Writer;

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
import de.ahrgr.animal.kohnert.asugen.property.ColorProperty;
import de.ahrgr.animal.kohnert.asugen.property.EKFontProperty;
import de.ahrgr.animal.kohnert.asugen.property.FormatedTextProperty;
import de.ahrgr.animal.kohnert.asugen.property.Property;
import de.ahrgr.animal.kohnert.asugen.property.TextProperty;
import de.ahrgr.animal.kohnert.generatorgui.GeneratorGUI;

/**
 * @author ek
 */
public class VigenereDecodeAPI implements Generator{
    
    GeneratorGUI gui;
    FormatedTextProperty ueberschrift;
    TextProperty dataString;
    TextProperty keyString;
    TextProperty numSteps;
    
    TextProperty lbData;
    TextProperty lbKey;    
    TextProperty charSet;
    TextProperty lb1, lb2, lb3, lb4, lb5, lb6, lb7;
    ColorProperty clChartZeile, clChartSpalte, clChartRest, clResult;
    FormatedTextProperty formatText;
    EKFontProperty codeGroupFont;
    Property[] properties;
    
    public VigenereDecodeAPI() {
        ueberschrift = new FormatedTextProperty("title", "the animation title", 
        "Vigenere Entschl??sselung");
        charSet = new TextProperty("algorithm/charset", "The CHARSET property must be equal to the encryption charset", "ABCDEFGHIJKLMNOPQRSTUVWXYZ ,.");
        dataString = new TextProperty("algorithm/data", "string to be decoded", "SRAYVHKXDU");
        keyString = new TextProperty("algorithm/key", "key to use for encryption", "KEY");
        numSteps = new TextProperty("algorithm/steps", "number of chars to be decoded with animation(0 = all)", "0");
        lb1 = new TextProperty("text/data", "Replacement for \"Text to be decoded: \"" , "Zu kodierender Text: ");
        lb2 = new TextProperty("text/key",  "Replacement for \"Key:                \"", "Schluessel: ");
        lb3 = new TextProperty("text/key char assignment", "Replacement for: \"Put the key string repeatedly under the data string.\"", "Den Schl??ssel wiederholt unter den zu entschl??sselnden Text schreiben");
        lb4 = new TextProperty("text/chart", "Replacement for: \"Use the chart from encryption.\"", "Die gleiche Tabelle wie bei der Verschl??sselung verwenden");        
        lb5 = new TextProperty("text/find elements", "Replacement for: \"You can find the encoded characters in the column of the data character and the row of the key character.\"", "Jetzt in der Zeile des Schluesselzeichens die Spalte mit dem Datenzeichen suchen");
        lb6 = new TextProperty("text/repeat", "Replacement for: \"Repeat this procedure for each character\"", "Wiederhole diesen Vorgang f??r jedes Zeichen");
        clChartZeile = new ColorProperty("chart/color first row", "Fill color of the first row", "cyan");
        clChartSpalte = new ColorProperty("chart/color first colum", "Fill color of the first column", "cyan2");
        clChartRest = new ColorProperty("chart/color", "Fill color of the other cells", "white");
        clResult = new ColorProperty("chart/color result", "Background color for the encoded characters", "yellow");
        codeGroupFont = new EKFontProperty("text/font", "Font to use for description", EKColor.BLACK, EKFont.FT_DEFAULT);
        properties = new Property[16];
        properties[0] = ueberschrift;
        properties[1] = dataString;
        properties[2] = keyString;
        properties[3] = lb1;
        properties[4] = lb2;
        properties[5] = lb3;
        properties[6] = lb4;
        properties[7] = lb5;
        properties[8] = lb6;
        //properties[9] = lb7;
        properties[9] = clChartZeile;
        properties[10] = clChartSpalte;
        properties[11] = clChartRest;
        properties[12] = clResult;
        properties[13] = codeGroupFont;
        properties[14] = numSteps;
        properties[15] = charSet;
    }
    
    public void generate(Writer out) {
        PrintWriter o = null;
        o = new PrintWriter(out);
        AnimalScriptWriter w = new AnimalScriptWriter(o, properties,
                getClass().getName());
        
        generateDecode(w);
        o.close();        
    }
    
    public void generateDecode(AnimalScriptWriter w) {
        w.clear();
        String data = dataString.getValue(); //"INFORMATIK";
        String key =  keyString.getValue(); //"KEY";
        String zeichensatz = charSet.getValue(); //"ABCDEFGHIJKLMNOPQRSTUVWXYZ ,.";
        
        w.startBlock();
        w.addLabel("Description");
        // "Vigenere Entschluesselung"
        Text tx = w.createText(w.abs(10, 40), ueberschrift.getText());

        tx.setColor(ueberschrift.getColor());
        tx.setFont(ueberschrift.getFont());
        w.createRectangle(tx.createOffset(0,5,AnimalObject.SW),
                tx.createOffset(5,6,AnimalObject.SE));
                          
        CodeGroup g = w.createCodeGroup(tx.createOffset(0, 12, AnimalObject.SW));
        g.setFont(codeGroupFont.getFont());
        g.setColor(codeGroupFont.getColor());                
        //"Zu dekodierender Text: "
        g.addCodeLine(lb1.getValue() + data);
        //"Schluessel: "
        g.addCodeLine(lb2.getValue() + key);
        g.addCodeLine("");
        Chart chart = new Chart(w, w.abs(10,360), data.length(), 2);
        chart.setCharsHorizontaly(0, 0, data);
        chart.register();        
        w.endBlock();
        
        w.addLabel("Assign Key Chars");
        // "Den Schl??ssel wiederholt unter den zu dekodierenden Text schreiben"
        g.addCodeLine(lb3.getValue());
        
        int width = data.length();
        int pos = 0;
        while(pos < width) {
            w.startBlock();
            chart.setCharsHorizontaly(pos,1,key);
            pos+=key.length();
            w.endBlock();
        }
        
        w.addLabel("Create Chart");
        //"Die gleiche Tabelle wie bei der Verschl??sselung verwenden."        
        g.addCodeLine(lb4.getValue());        

        w.startBlock();
        int i;                    
        Chart tabelle = new Chart(w, chart.createOffset(50, 200, 
        		AnimalObject.S), zeichensatz.length(), key.length() + 1);        
        tabelle.setCharsHorizontaly(0, 0, zeichensatz);
        
        // F??llfarbe f??r erste Tabellenzeile
        EKColor cl = EKColor.createFromString(clChartZeile.getValue());        
        tabelle.setFillColorRect(0, 0, zeichensatz.length(), 1, cl);
        tabelle.setFillColorRect(0,1,1,key.length(), EKColor.createFromString(clChartSpalte.getValue()));
        tabelle.setFillColorRect(1, 1, zeichensatz.length()-1, key.length(), EKColor.createFromString(clChartRest.getValue()));
        tabelle.register();
        //"In die erste Spalte vertikal das Schluesselwort eintragen"
        //g.addCodeLine(lb5.getValue());        
        tabelle.setCharsVerticaly(0,1, key);        
        /*for(i = 0; i < key.length(); i++) {        
            tabelle.getElementAt(0,i+1).setText("" + key.charAt(i));
            tabelle.getElementAt(0, i+1).getRectangle().setFillColor(Color.createFromString(clChartSpalte.getValue()));
        }*/
        for(i = 0; i<key.length(); i++) {
//            int i2;
            w.startBlock();
            char keyChar = key.charAt(i);
            int keyCharPos = zeichensatz.indexOf(""+keyChar);
            String s = zeichensatz.substring(keyCharPos + 1);
            s = s + zeichensatz.substring(0, keyCharPos);
            tabelle.setCharsHorizontaly(1, i+1, s);           
        }
        w.endBlock();
        
                
        //"Jetzt in der Zeile des Schluesselzeichens die Spalte mit dem Datenzeichen suchen"
        g.addCodeLine(lb5.getValue());
        
        w.addLabel("begin decryption");
        PolyLine p = null;
        PolyLine p2 = null;
        TextBox lastChar = null;
        boolean firstChar = true;
        int steps = Integer.parseInt(numSteps.getValue()); // Anzahl der Animationsschritte
        if (steps == 0) steps = 1000;
        for(i = 0; (i < data.length()) && (i < steps); i++) {
            w.startBlock();
            w.addLabel("decrypt '" + data.charAt(i) + "'");
            if(p!=null)p.setHidden(true);
            if(p2!=null)p2.setHidden(true);
            p = new PolyLine(w);
            EKNode n = chart.getElementAt(i,0).createOffset(0,0,AnimalObject.N);
            p.addNode(n);
            p.addNode(n.createOffset(0, -10));
            n = chart.createOffset(10, -10,AnimalObject.NE);
            p.addNode(n);
            n = chart.createOffset(10, 10, AnimalObject.SE);
            p.addNode(n);
            int x = (zeichensatz.length() + zeichensatz.indexOf(data.charAt(i)) -
                    zeichensatz.indexOf(key.charAt(i % key.length())) )
                    % zeichensatz.length();
            n = tabelle.getElementAt(x, 0).createOffset(0,-30,AnimalObject.N);
            p.addNode(n);
            p.addNode(n.createOffset(0,30));
            p.setArrow(PolyLine.ARROW_FORWARD);
            p.register();            
            
            // Pfeil auf Schl??sselzeile
            p2 = new PolyLine(w);
            n = chart.getElementAt(i,1).createOffset(0,1,AnimalObject.S);
            p2.addNode(n);
            p2.addNode(n.createOffset(0, 10));
             n = tabelle.createOffset(-50, -50,AnimalObject.NW);
             p2.addNode(n);
                          
             int y =  i % key.length() + 1;
             n = tabelle.getElementAt(0, y).createOffset(-50,0,AnimalObject.W);
             p2.addNode(n);
             p2.addNode(n.createOffset(50,0));
             p2.setArrow(PolyLine.ARROW_FORWARD);
             p2.register(); 
                          
             TextBox b = tabelle.getElementAt(x, 0); // Buchstaben aus der ersten Zeile
             TextBox b2 = new TextBox(w, b.getPosition(), b.getText().getValue());
             TextBox b3 = tabelle.getElementAt(x,y);
             b3.getRectangle().setFillColor(EKColor.ORANGE);
             b2.register();
             b2.getRectangle().setFillColor(EKColor.createFromString(clResult.getValue()));
             
             if(lastChar == null) n = tabelle.createOffset(-30, 30, AnimalObject.SW);
             else n = lastChar.createOffset(5,0,AnimalObject.NE);
            w.endBlock();
             b2.moveTo(n);
             w.startBlock();
             // alte F??llfarbe der Tabelle herstellen
             EKColor bkcolor = EKColor.createFromString(clChartRest.getValue());             
             if(x == 0) bkcolor = EKColor.createFromString(clChartSpalte.getValue());
             b3.getRectangle().setFillColor(bkcolor);
             lastChar = b2;  
             if(firstChar) {
                 firstChar = false;
                 g.addCodeLine(lb6.getValue());
             }
                                      
        }
        // Die restlichen kodierten Zeichen ohne Animation einblenden
        w.startBlock();
        p.setHidden(true);
        p2.setHidden(true);
        for(; i < data.length(); i++) {
            int x = (zeichensatz.length() + zeichensatz.indexOf(data.charAt(i)) -
                    zeichensatz.indexOf(key.charAt(i % key.length())) )
                    % zeichensatz.length();
            int y =  0;
            TextBox srcTB = tabelle.getElementAt(x, y);
            EKNode n = lastChar.getRectangle().createOffset(5,0,AnimalObject.SE);
            TextBox b2 = new TextBox(w, n, srcTB.getText().getValue());
            b2.register();
            b2.getRectangle().setFillColor(EKColor.createFromString(clResult.getValue()));            
            lastChar = b2; 
        }
        w.endBlock();        
  /*      for(i = 0; i < width; i++) {
            chart.getElementAt(i, 0).getRectangle().setFillColor("blue");
            chart.getElementAt(i,0).getText().setHidden(false);            
        }
        
        PolyLine p = new PolyLine(w);
        p.addNode(chart.getElementAt(0,1).createOffset(0,0, PolyLine.S));
        p.addNode(chart.getElementAt(0,1).createOffset(0,100, PolyLine.S));
        p.addNode(chart.getElementAt(3,1).createOffset(0,100, PolyLine.S));
        p.addNode(chart.getElementAt(3,1).createOffset(0,0, PolyLine.S));
        p.setArrow(PolyLine.ARROW_FORWARD);
        p.register();*/
        
    }
    
    /* (non-Javadoc)
     * @see generatorgui.Generator#getGeneratorName()
     */
    public String getGeneratorName() {
        return "Vigenere Decryption";
    }

    /* (non-Javadoc)
     * @see generatorgui.Generator#getProperties()
     */
    public Property[] getProperties() {
        return properties;
    }

    /* (non-Javadoc)
     * @see generatorgui.Generator#generateScript(java.io.File)
     */
    public void generateScript(Writer out) {
        generate(out);
        
    }
}
