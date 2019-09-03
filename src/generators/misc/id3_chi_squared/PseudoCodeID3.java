package generators.misc.id3_chi_squared;

import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;
import translator.Translator;

import java.awt.*;

/** PseudoCodeID3
 * is needed to create the pseudo code and highlight lines of it
 * is called by ID3, EntropyVisual and Chi
 */
public class PseudoCodeID3 {

    private static SourceCode pseudoCode;
    private static int lastLine = 0;
    private Translator translator;



    /** createSourceCodeEn
     * creates the pseudo code in English
     *
     * @param xCoords
     * @param yCoords
     * @param lang
     */
    public static void createSourceCodeEn(int xCoords, int yCoords, Language lang, Translator translator){
        SourceCodeProperties scProperties = new SourceCodeProperties();
        scProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 18));
        scProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, new Color(114,140,56));

        pseudoCode = lang.newSourceCode(new Coordinates(xCoords,yCoords), "null", null,scProperties);
        pseudoCode.addCodeLine(translator.translateMessage("pseu1"),"null",0,null);
        pseudoCode.addCodeLine(translator.translateMessage("pseu2"),"null",1,null);
        pseudoCode.addCodeLine(translator.translateMessage("pseu3"),"null",0,null);
        pseudoCode.addCodeLine(translator.translateMessage("pseu4"),"null",0,null);
        pseudoCode.addCodeLine(translator.translateMessage("pseu5"),"null",0,null);
        pseudoCode.addCodeLine(translator.translateMessage("pseu6"),"null",0,null);
        pseudoCode.addCodeLine(translator.translateMessage("pseu7"),"null",0,null);
        pseudoCode.addCodeLine(translator.translateMessage("pseu8"),"null",0,null);
        pseudoCode.addCodeLine(translator.translateMessage("pseu9"),"null",0,null);
        pseudoCode.addCodeLine(translator.translateMessage("pseu10"),"null",0,null);
        pseudoCode.addCodeLine(translator.translateMessage("pseu11"),null,1,null);
        pseudoCode.addCodeLine(translator.translateMessage("pseu12"),"null",1,null);
        pseudoCode.addCodeLine(translator.translateMessage("pseu13"),"null",0,null);
        pseudoCode.addCodeLine(translator.translateMessage("pseu14"),"null",1,null);
        pseudoCode.addCodeLine("","null",0,null);
    }

    /** createSourceCodeEn
     * creates the pseudo code in English
     *
     * @param xCoords
     * @param yCoords
     * @param lang
     */
    public static void createSourceCodeDe(int xCoords, int yCoords, Language lang){
        SourceCodeProperties scProperties = new SourceCodeProperties();
        scProperties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 18));
        scProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, new Color(114,140,56));

        pseudoCode = lang.newSourceCode(new Coordinates(xCoords,yCoords), "null", null,scProperties);
        pseudoCode.addCodeLine("Wenn nur eine Klasse sich im Datensatz befindet,","null",0,null);
        pseudoCode.addCodeLine("dann klassifiziere den Knoten und return","null",1,null);
        pseudoCode.addCodeLine("berechne die Entropy des Knotens","null",0,null);
        pseudoCode.addCodeLine("für jedes Attribut:","null",0,null);
        pseudoCode.addCodeLine("|   erstelle einen Kindknoten für jeden Wert des Attributs","null",0,null);
        pseudoCode.addCodeLine("|   berechnne die Entropy für jeden Kindknoten","null",0,null);
        pseudoCode.addCodeLine("|   berechne den Gain","null",0,null);
        pseudoCode.addCodeLine("wähle das Attribut mit dem höchsten Gain und speichere die Gewinner-Kinder","null",0,null);
        pseudoCode.addCodeLine("berechne Chi_Quadrat für das gewählte Attribut","null",0,null);
        pseudoCode.addCodeLine("wenn Chi_Quadrat > Grenzwert","null",0,null);
        pseudoCode.addCodeLine("dann zeichne die Kindknoten",null,1,null);
        pseudoCode.addCodeLine("rufe ID3 auf jeden Kindknoten rekursiv auf","null",1,null);
        pseudoCode.addCodeLine("ansonsten","null",0,null);
        pseudoCode.addCodeLine("klassifiziere den Knoten und return","null",1,null);
        pseudoCode.addCodeLine("","null",0,null);
    }

    /** highlightCode
     * highlights the code line by given index
     *
     * @param i index of code line to be highlighted
     */
    public static void highlightCode(int i){
        pseudoCode.toggleHighlight(lastLine,i);
        lastLine = i;
    }
}
