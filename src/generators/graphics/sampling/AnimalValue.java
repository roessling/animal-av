/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generators.graphics.sampling;

import algoanim.primitives.generators.Language;
import algoanim.properties.TextProperties;
import algoanim.primitives.Text;
import algoanim.util.Node;

/**
 *
 * @author simon
 */
public class AnimalValue {

    private static int uniqueID = 0;

    private static synchronized String getUniqueID() {
        return String.format("Value%d", uniqueID++);
    }

    private final Language lang;
    private final String ID;
    private Text text;
    private final TextProperties textProp;

    public AnimalValue(Language lang, TextProperties textProp) {
        this.lang = lang;
        this.textProp = textProp;
        this.ID = getUniqueID();
    }

    public void createValue(Node baseNode, String value) {
        text = lang.newText(baseNode, value, ID, null, textProp);
    }

    public void setValue(String value) {
        text.setText(value, null, null);
    }

    public void setValue(int value) {
        text.setText("" + value, null, null);
    }
    
    public void hide(){
        text.hide();
    }

}
