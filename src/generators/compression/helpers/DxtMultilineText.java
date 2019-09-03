package generators.compression.helpers;

import algoanim.primitives.Primitive;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public class DxtMultilineText {
    private Language lang;
    private List<Text> lines = new ArrayList<>();
    private TextProperties properties = new TextProperties();
    private int newLines = 0;
    private int x;
    private int y;

    public DxtMultilineText(Language lang, int x, int y) {
        this.lang = lang;
        this.x = x;
        this.y = y;
    }

    public DxtMultilineText clear() {
        lines.forEach(Primitive::hide);
        lines.clear();
        newLines = 0;
        return this;
    }

    public DxtMultilineText setFont(Font font) {
        properties.set(AnimationPropertiesKeys.FONT_PROPERTY, font);
        return this;
    }
    
    public DxtMultilineText setColor(Color color) {
        properties.set(AnimationPropertiesKeys.COLOR_PROPERTY, color);
        return this;
    }

    public DxtMultilineText addLine(String text) {
        lines.add(lang.newText(new Coordinates(x, y + (lines.size() + newLines) * 14), text, "", null, properties));
        return this;
    }

    public DxtMultilineText addLine() {
        newLines++;
        return this;
    }
}
