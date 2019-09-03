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
public class VigenereEncode implements Generator{
    
    GeneratorGUI gui;
    FormatedTextProperty ueberschrift;
    TextProperty charSet;
    TextProperty dataString;
    TextProperty keyString;
    TextProperty numSteps;
    
    TextProperty lbData;
    TextProperty lbKey;    
    TextProperty lb1, lb2, lb3, lb4, lb5, lb6, lb7;
    ColorProperty clChartZeile, clChartSpalte, clChartRest, clResult;
    FormatedTextProperty formatText;
    EKFontProperty codeGroupFont;
    Property[] properties;
    
    public VigenereEncode() {
       ueberschrift = new FormatedTextProperty("title", "the animation title", 
               "Vigenere Verschluesselung");
       charSet = new TextProperty("algorithm/charset", "The CHARSET property must contain at least all characters used in DATA and KEY.", "ABCDEFGHIJKLMNOPQRSTUVWXYZ ,.");
       dataString = new TextProperty("algorithm/data", "string to be encoded", "INFORMATIK");
       keyString = new TextProperty("algorithm/key", "key to use for encryption", "KEY");
       numSteps = new TextProperty("algorithm/steps", "number of chars to be encoded with animation(0 = all)", "0");
       lb1 = new TextProperty("text/data", "Replacement for \"Text to be encoded: \"" , "Zu kodierender Text: ");
       lb2 = new TextProperty("text/key",  "Replacement for \"Key:                \"", "Schluessel: ");
       lb3 = new TextProperty("text/key char assignment", "Replacement for: \"Put the key string repeatedly under the data string.\"", "Den Schlüssel wiederholt unter den zu kodierenden Text schreiben");
       lb4 = new TextProperty("text/create chart", "Replacement for: \"Create a chart with one column per character\"", "Eine Tabelle mit einer Spalte für jedes Zeichen anlegen");
       lb5 = new TextProperty("text/vertical key", "Replacement for: \"Put the key verticaly into the first column\"", "In die erste Spalte vertikal das Schluesselwort eintragen");
       lb6 = new TextProperty("text/chart content", "Replacement for: \"Fill the rows with the continous charset beginning with the character in the first column.\"", "Die Zeilen mit kontinuierlichem Alphabet auffuellen");
       lb7 = new TextProperty("text/find elements", "Replacement for: \"You can find the encoded characters in the column of the data character and the row of the key character.\"", "Kodierte Zeichen stehen in der Zeile des Schluesselzeichens und in der Spalte des Datenzeichens");
       clChartZeile = new ColorProperty("chart/color first row", "Fill color of the first row", "cyan");
       clChartSpalte = new ColorProperty("chart/color first colum", "Fill color of the first column", "cyan2");
       clChartRest = new ColorProperty("chart/color", "Fill color of the other cells", "white");
       clResult = new ColorProperty("chart/color result", "Background color for the encoded characters", "yellow");
       codeGroupFont = new EKFontProperty("text/font", "Font to use for description", EKColor.BLACK, EKFont.FT_DEFAULT);
       properties = new Property[17];
       properties[0] = ueberschrift;
       properties[1] = dataString;
       properties[2] = keyString;
       properties[3] = lb1;
       properties[4] = lb2;
       properties[5] = lb3;
       properties[6] = lb4;
       properties[7] = lb5;
       properties[8] = lb6;
       properties[9] = lb7;
       properties[10] = clChartZeile;
       properties[11] = clChartSpalte;
       properties[12] = clChartRest;
       properties[13] = clResult;
       properties[14] = codeGroupFont;
       properties[15] = numSteps;
       properties[16] = charSet;
    }
    
    public void generate(Writer out) {
        PrintWriter o = null;
        o = new PrintWriter(out);
        AnimalScriptWriter w = new AnimalScriptWriter(o, properties,
                getClass().getName());

        generateEncode(w);
        //generateDecode(w);
        o.close();        
    }
    
    public void generateEncode(AnimalScriptWriter w) {
        TextBox bb = new TextBox(w, w.abs(100,100),"Test");
        bb.register();
//        if(true)
//        return;
        
        String data = dataString.getValue().toUpperCase(); //"INFORMATIK";
        String key =  keyString.getValue().toUpperCase(); //"KEY";
        String zeichensatz = charSet.getValue();
        
        w.startBlock();
        w.addLabel("Description");
        
        // "Vigenere Verschluesselung"
        Text tx = w.createText(w.abs(10, 40), 
                        ueberschrift.getText());
//        System.out.println("ft: " + ueberschrift.getFont().toString());
        tx.setFont(ueberschrift.getFont());
        tx.setColor(ueberschrift.getColor());
        w.createRectangle(tx.createOffset(0,5,AnimalObject.SW),
                            tx.createOffset(5,6,AnimalObject.SE));
  //      w.createText(w.abs(10,60), "Zu kodierender Text: " + data);
  //      w.createText(w.abs(10,100), "Schluessel:                " + key);
                          
        CodeGroup g = w.createCodeGroup(tx.createOffset(0, 12, AnimalObject.SW));
        g.setFont(codeGroupFont.getFont());
        g.setColor(codeGroupFont.getColor());
        //"Zu kodierender Text: "
        g.addCodeLine(lb1.getValue() + data);
        //"Schluessel: "
        g.addCodeLine(lb2.getValue() + key);
        g.addCodeLine("");
        Chart chart = new Chart(w, w.abs(10,360), data.length(), 2);
        chart.setCharsHorizontaly(0, 0, data);
        chart.register();        
        w.endBlock();
        
        w.addLabel("assign key characters");
        // "Den Schlüssel wiederholt über den zu kodierenden Text schreiben"
        g.addCodeLine(lb3.getValue());
        
        int width = data.length();
        int pos = 0;
        while(pos < width) {
            w.startBlock();
            chart.setCharsHorizontaly(pos,1,key);
            pos+=key.length();
            w.endBlock();
        }
        w.addLabel("create chart");
        //"Eine Tabelle mit einer Spalte für jedes Zeichen anlegen"
        g.addCodeLine(lb4.getValue());      
        w.startBlock();
        int i;                    
        Chart tabelle = new Chart(w, chart.createOffset(50, 200,
        		AnimalObject.S), zeichensatz.length(), key.length() + 1);        
        tabelle.setCharsHorizontaly(0, 0, zeichensatz);
        
        // Füllfarbe für erste Tabellenzeile
        EKColor cl = EKColor.createFromString(clChartZeile.getValue());        
        tabelle.setFillColorRect(0, 0, zeichensatz.length(), 1, cl);
        tabelle.setFillColorRect(0,1,1,key.length(), EKColor.createFromString(clChartSpalte.getValue()));
        tabelle.setFillColorRect(1, 1, zeichensatz.length()-1, key.length(), EKColor.createFromString(clChartRest.getValue()));
        tabelle.register();
        w.endBlock();
        w.addLabel("insert key into the chart");
        //"In die erste Spalte vertikal das Schluesselwort eintragen"
        g.addCodeLine(lb5.getValue());
        w.startBlock();
        tabelle.setCharsVerticaly(0,1, key);        
        w.endBlock();
        w.addLabel("fill the chart");
        //"Die Zeilen mit kontinuirlichem Alphabet auffuellen"
        g.addCodeLine(lb6.getValue());
        for(i = 0; i<key.length(); i++) {
//            int i2;
            w.startBlock();
            char keyChar = key.charAt(i);
            int keyCharPos = zeichensatz.indexOf(""+keyChar);
            String s = zeichensatz.substring(keyCharPos + 1);
            s = s + zeichensatz.substring(0, keyCharPos);
            tabelle.setCharsHorizontaly(1, i+1, s);
            w.endBlock();
        }
        w.addLabel("start encryption");
        //"Kodierte Zeichen stehen in der Zeile des Schluesselzeichens und in der Spalte des Datenzeichens"
        g.addCodeLine(lb7.getValue());
        PolyLine p = null;
        PolyLine p2 = null;
        TextBox lastChar = null;
        int steps = Integer.parseInt(numSteps.getValue()); // Anzahl der Animationsschritte
        if (steps == 0) steps = 1000;
        for(i = 0; (i < data.length()) && (i < steps); i++) {
            w.startBlock();
            w.addLabel("encrypt '" + data.charAt(i) + "'");
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
            int x = zeichensatz.indexOf(data.charAt(i));
            n = tabelle.getElementAt(x, 0).createOffset(0,-30,AnimalObject.N);
            p.addNode(n);
            p.addNode(n.createOffset(0,30));
            p.setArrow(PolyLine.ARROW_FORWARD);
            p.register();            
            
            // Pfeil auf Schlüsselzeile
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
             
             TextBox b = tabelle.getElementAt(x, y);
             TextBox b2 = new TextBox(w, b.getPosition(), b.getText().getValue());
             b2.register();
             b2.getRectangle().setFillColor(EKColor.createFromString(clResult.getValue()));
             
             if(lastChar == null) n = tabelle.createOffset(-30, 30, AnimalObject.SW);
             else n = lastChar.createOffset(5,0,AnimalObject.NE);
             w.endBlock();
             b2.moveTo(n);                      
             lastChar = b2;  
                                      
        }        
        // Die restlichen kodierten Zeichen ohne Animation einblenden
        w.startBlock();
        p.setHidden(true);
        p2.setHidden(true);
        for(; i < data.length(); i++) {
            int x = zeichensatz.indexOf(data.charAt(i));
            int y =  i % key.length() + 1;
            TextBox srcTB = tabelle.getElementAt(x, y);
            EKNode n = lastChar.getRectangle().createOffset(5,0,AnimalObject.SE);
            TextBox b2 = new TextBox(w, n, srcTB.getText().getValue());
            b2.register();
            b2.getRectangle().setFillColor(EKColor.createFromString(clResult.getValue()));            
            lastChar = b2; 
        }
        w.endBlock();
        
    }
    


    /* (non-Javadoc)
     * @see generatorgui.Generator#getGeneratorName()
     */
    public String getGeneratorName() {
        return "Vigenere Encryption";
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
