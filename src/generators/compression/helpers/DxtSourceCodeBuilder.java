package generators.compression.helpers;

import algoanim.primitives.SourceCode;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.util.Coordinates;

import java.awt.Color;
import java.awt.Font;

public class DxtSourceCodeBuilder {
    @SuppressWarnings("unused")
    private Language lang;
    private SourceCode sourceCode;
    private int indentation = 0;

    public DxtSourceCodeBuilder(Language lang, int x, int y, Color textColor, Color highlightColor) {
        this.lang = lang;

        SourceCodeProperties properties = new SourceCodeProperties();
        properties.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        properties.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 14));
        properties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, highlightColor);
        properties.set(AnimationPropertiesKeys.COLOR_PROPERTY, textColor);

        this.sourceCode = lang.newSourceCode(new Coordinates(x, y), "", null, properties);
    }

    public DxtSourceCodeBuilder incIndent() {
        indentation++;
        return this;
    }

    public DxtSourceCodeBuilder decIndent() {
        indentation--;
        return this;
    }

    public DxtSourceCodeBuilder addLine(String line) {
        return this.addLine(line, null);
    }
    
    public DxtSourceCodeBuilder addLine(String line, String name) {
        sourceCode.addCodeLine(line, name, indentation, null);
        return this;
    }

    public SourceCode getSourceCode() {
        return sourceCode;
    }
}
