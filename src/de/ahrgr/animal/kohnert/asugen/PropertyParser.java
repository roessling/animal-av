/*
 * Created on 08.02.2005
 */
package de.ahrgr.animal.kohnert.asugen;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;

import de.ahrgr.animal.kohnert.asugen.property.ColorProperty;
import de.ahrgr.animal.kohnert.asugen.property.EKFontProperty;
import de.ahrgr.animal.kohnert.asugen.property.FormatedTextProperty;
import de.ahrgr.animal.kohnert.asugen.property.Property;
import de.ahrgr.animal.kohnert.asugen.property.TextProperty;


/**
 * @author ek
 */
public class PropertyParser {
    
    private static String tokenToString(StreamTokenizer tok) {
        switch(tok.ttype) {
            case '(': return "(";
            case ')': return ")";
            case ',': return ",";
            case StreamTokenizer.TT_EOF: return "end of file";
            case StreamTokenizer.TT_EOL: return "end of line";
            case StreamTokenizer.TT_WORD: return tok.sval;
            case '"': return "\"" + tok.sval + "\"";
        }
        return ""+(char)tok.ttype;
    }
    
    private void parseerror(StreamTokenizer tok, String s) throws IOException {
        throw new IOException("Parse Error in line " + tok.lineno() + " Token " + tokenToString(tok) + ": " + s);
    }
    private void asserttokentype(StreamTokenizer tok, int soll) throws IOException {
        if(tok.ttype != soll) parseerror(tok, "unexpected Token: " + tokenToString(tok));
    }

    /**
     * Liest eine Generator-Konfigurationsdatei aus einem Zeichenstrom
     * und erzeugt daraus ein Generator-Objekt. Die unterst&uuml;tzten
     * Generator-Typen werden durch generatorFactory &uuml;bergeben. 
     * @param src Zeichenstrom mit Konfigurationsdaten
     * @param generatorFactory Erzeuger f&uuml;r das Generator-Objekt
     * @return Generator-Objekt mit den gelesenen Einstellungen oder null
     * falls kein passender Generator gefunden wurde
     */
    public Generator parse(Reader src, GeneratorFactory generatorFactory) throws IOException{
        StreamTokenizer tok = new StreamTokenizer(src);
        tok.ordinaryChar(',');
        tok.ordinaryChar('=');
        tok.ordinaryChar('(');
        tok.ordinaryChar(')');
        tok.eolIsSignificant(true);
        tok.quoteChar('"');
        tok.nextToken();
        asserttokentype(tok, StreamTokenizer.TT_WORD);
        if(!"generator".equals(tok.sval)) parseerror(tok, "generator class definition expected but found: " + tokenToString(tok));
        tok.nextToken();
        asserttokentype(tok, '=');
        tok.nextToken();
        asserttokentype(tok, '"');
        String generatorclass = tok.sval;
        tok.nextToken();
        asserttokentype(tok, StreamTokenizer.TT_EOL);
        Generator g = generatorFactory.getGenerator(generatorclass);
        if(g == null) return null;
        HashMap<String, Property> properties = new HashMap<String, Property>();
        Property[] pa = g.getProperties();
        int i;
        // Hashmap mit den Properties des Generators erzeugen
        for(i = 0; i < pa.length; i++) 
        	properties.put(pa[i].getKey(), pa[i]);
        while(tok.nextToken() != StreamTokenizer.TT_EOF) {
            if(!( (tok.ttype == StreamTokenizer.TT_WORD) ||
                  (tok.ttype == '"') )) {
                parseerror(tok, "Key erwartet");
            }
            String key = tok.sval;
            tok.nextToken();
            asserttokentype(tok, '=');
            tok.nextToken();
            asserttokentype(tok, StreamTokenizer.TT_WORD);
            String dataType = tok.sval;
            tok.nextToken();
            asserttokentype(tok, '(');
            ArrayList<String> params = new ArrayList<String>(3);
            while(tok.nextToken() != ')') {
                if(!( (tok.ttype == StreamTokenizer.TT_WORD) ||
                        (tok.ttype == '"') )) {
                      parseerror(tok, "Parameter erwartet");
                  }
                String param = tok.sval;
                params.add(param);
                tok.nextToken();
                if(tok.ttype == ')') tok.pushBack();
                else asserttokentype(tok, ',');
            }
            tok.nextToken();
            asserttokentype(tok, StreamTokenizer.TT_EOL);
            String[] aparams = new String[params.size()];            
            for(i = 0; i < aparams.length; i++) aparams[i] = 
                    params.get(i);
            parse(properties, key, dataType, aparams);
        }        
        return g;
    }
    
    
    /**
     * parst ein PropertyObjekt aus gegebenem datentyp und Parametern.
     * Wenn erfolgreich, wird der key in der HashMap gesucht und das
     * gefundene Property-Objekt mit den geparsten Werten gesetzt.
     * @param properties
     * @param key
     * @param dataType
     * @param params
     */
    public void parse(HashMap<String, Property> properties, String key, String dataType,
           String[] params) {
        Property p = properties.get(key);
        if(p == null) {
            System.out.println("warning: Key \"" + key +"\" unknown and ignored");
            return;
        }
        if("Text".equals(dataType)) 
            ((TextProperty) p).setValue(params[0]);
        else
        if("Color".equals(dataType)) 
            ((ColorProperty) p).setValue(params[0]);
        else
        if("FormatedText".equals(dataType)) {
            
            String text = params[0];
            String color = params[2];
            EKFont font = EKFont.parseAnimal(params[1]);
            ((FormatedTextProperty) p).setText(text);
            ((FormatedTextProperty) p).setColor(color);
            ((FormatedTextProperty) p).setFont(font);
        }
        else
        if("Font".equals(dataType)) {
            EKFont font = EKFont.parseAnimal(params[0]);
            String color = params[1];
            ((EKFontProperty) p).setFont(font);
            ((EKFontProperty) p).setColor(color);
        } else
        System.out.println("warning: unknown Property Type \"" + dataType +"\" ignored");
    }
}
