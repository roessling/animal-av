package generators.graphics;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.*;
import animal.main.Animal;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import graphics.AnimalImageDummy;
import helpers.AnimalReader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;


public class RGBImageBlender implements ValidatingGenerator {

    private enum BlendMode {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIFFERENCE,
        LIGHTEN,
        DARKEN,
        OVERLAY,
    }

    private enum ImageStatus {
        FILE,
        URL,
        DEFAULT,
        FAILURE
    }

    private Language lang;
    private int height = 25;
    private int width = 25;
    private int pixelWidth = 15;
    private String bottomImageURI;
    private String topImageURI;
    private BlendMode blendMode;
    private SourceCodeProperties scrCodeProperties = new SourceCodeProperties();
    private PolylineProperties polylineProperties;
    private TextProperties header = new TextProperties();
    private TextProperties defaultTP = new TextProperties();

    private ImageStatus imgStatus;
    private static final String DESCRIPTION = "Beim Image blending werden zwei Bilder mit einander \"vermischt\".\n" +
            "Diese geschieht, indem die beiden Bilder Pixel für Pixel durchgegangen werden und basierend auf\n" +
            "dem Blend mode den neue RGB-Wert berechnet.";

    private static final String SOURCE_CODE = "public BufferedImage blend(BufferedImage bottom, BufferedImage top){\n" +
            "    BufferedImage result = new BufferedImage(bottom.getWidth(), bottom.getHeight(), BufferedImage.TYPE_INT_ARGB);\n" +
            "    for(int row = 0; row < image.getHeight(); row++){\n" +
            "        for(int column = 0; column < image.getWidth(); column++){\n" +
            "            int a = btmImage.getRGB(column,row);\n" +
            "            int b = topImage.getRGB(column,row);\n" +
            "            int rgb = (b < 128) ? ((2 * a * b) / 255) : (255 - (2 * (255 - a) * (255 - b)) / 255);\n" +
            "            result.setRGB(x,y,rgb)\n" +
            "        }\n" +
            "    }\n" +
            "    return result;\n" +
            "}";

    private static final String ADD = "a + b > 255 ? 255 : a + b";
    private static final String SUBTRACT = "(a + b < 255) ? 0 : (a + b - 255)";
    private static final String MULTIPLY = "(int) ((a * b) / 255.0)";
    private static final String DIFFERENCE = "Math.abs(a - b);";
    private static final String DARKEN = "a > b ? a : b";
    private static final String LIGHTEN = "a < b ? a : b";
    private static final String OVERLAY = "(b < 128) ? ((2 * a * b) / 255) : (255 - (2 * (255 - a) * (255 - b)) / 255)";

    @Override
    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> primitives) throws IllegalArgumentException {
        if ((Integer) primitives.get("height") < 5) {
            JOptionPane.showMessageDialog(null, "height darf nicht kleiner als 5 sein", "Invalid input", JOptionPane.ERROR_MESSAGE); //50*50 bei pixel width = 15 no go
            return false;
        } else if ((Integer) primitives.get("width") < 5) {
            JOptionPane.showMessageDialog(null, "width darf nicht kleiner als 5 sein", "Invalid input", JOptionPane.ERROR_MESSAGE);
            return false;
        } else if ((Integer) primitives.get("height") * (Integer) primitives.get("width") > 2500) {
            JOptionPane.showMessageDialog(null, "Die Maximale Anzahl der Pixel (2500) wurde überschritten. Ihre Anzahl war: " + (Integer) primitives.get("height") * (Integer) primitives.get("width"), "Invalid input", JOptionPane.ERROR_MESSAGE);
            return false;
        } else if ((Integer) primitives.get("pixelWidth") < 10) {
            JOptionPane.showMessageDialog(null, "pixelWidth darf nicht kleiner als 10 sein", "Invalid input", JOptionPane.ERROR_MESSAGE);
            return false;
        } else if ((Integer) primitives.get("pixelWidth") > 60) {
            JOptionPane.showMessageDialog(null, "pixelWidth darf nicht größer als 60 sein", "Invalid input", JOptionPane.ERROR_MESSAGE);
            return false;
        } else if ((Integer) primitives.get("blendmode") < 1) {
            JOptionPane.showMessageDialog(null, "blendmode darf nicht kleiner als 1 sein", "Invalid input", JOptionPane.ERROR_MESSAGE);
            return false;
        } else if ((Integer) primitives.get("blendmode") > 7) {
            JOptionPane.showMessageDialog(null, "blendmode darf nicht größer als 8 sein", "Invalid input", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String generate(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> primitives) {
        this.height = (Integer) primitives.get("height");
        this.width = (Integer) primitives.get("width");
        this.pixelWidth = (Integer) primitives.get("pixelWidth");
        this.bottomImageURI = (String) primitives.get("bottomImage");
        this.topImageURI = (String) primitives.get("topImage");
        this.blendMode = BlendMode.values()[(Integer) primitives.get("blendmode") - 1];
        this.scrCodeProperties = (SourceCodeProperties) animationPropertiesContainer.getPropertiesByName("sourceCode");
        this.polylineProperties = (PolylineProperties) animationPropertiesContainer.getPropertiesByName("polyline");
        this.blend();
        return this.lang.toString();
    }

    @Override
    public String getAlgorithmName() {
        return "RGB Image Blending";
    }

    @Override
    public String getAnimationAuthor() {
        return "Joel Tschesche, Konstantinos Kadoglou";
    }

    @Override
    public String getCodeExample() {
        return SOURCE_CODE;
    }

    @Override
    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getFileExtension() {
        return "asu";
    }

    @Override
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
    }

    @Override
    public String getName() {
        return "Image Blending";
    }

    @Override
    public String getOutputLanguage() {
        return "Java";
    }

    @Override
    public void init() {
        this.lang = new AnimalScript("Blend RGB two Images", "Joel Tschesche, Konstantinos Kadoglou", 800, 600);
        this.lang.setStepMode(true);
    }


    public void blend() {
        // Init Step
        BufferedImage bottomImg = this.setPicture(this.bottomImageURI, this.width, this.height);
        BufferedImage topImg = this.setPicture(this.topImageURI, this.width, this.height);


        this.header.set("font", new Font("Monospaced", 0, 25));
        this.header.set("color", Color.black);

        this.defaultTP.set("font", new Font("Monospaced", 0, 14));
        this.defaultTP.set("color", Color.black);


        if (this.imgStatus == ImageStatus.FAILURE) {
            //Text errorMsg = this.lang.newText(new Coordinates(50, 145), "Leider konnten weder auf Ihre URL/Datei noch auf das Default Bild zugegriffen werden.", "errorMessage", null, tp);
            return;
        }

        // Animation Title
        Text title = this.lang.newText(new Coordinates(20, 30), "Image Blending Algorithm", "title", null, this.header);

        // Description
        Text des1 = this.lang.newText(new Offset(0, 10, title, "SW"), "Beim Image blending werden zwei Bilder mit einander 'vermischt'.", "Description1", null, defaultTP);
        Text des2 = this.lang.newText(new Offset(0, 5, des1, "SW"), "Diese geschieht, indem der Algorithmus die beiden Bilder Pixel für Pixel durchgegangen werden und basierend auf", "Description2", null, defaultTP);
        Text des3 = this.lang.newText(new Offset(0, 5, des2, "SW"), "dem ausgewählten Blend mode den neue RGB-Wert berechnet.", "Description3", null, defaultTP);
        Text des4 = this.lang.newText(new Offset(0, 5, des3, "SW"), "Ausgewählter Blend Mode: " + blendMode + ": " + this.selectImageArithmetic(this.blendMode), "Description4", null, defaultTP);

        // Step 2
        this.lang.nextStep("Introduction");

        Variables variables = this.lang.newVariables();
        variables.declare("String", "rgbBottom", "null", "RGB Wert von Image A");
        variables.declare("String", "rgbTop", "null", "RGB Wert von Image B");
        variables.declare("String", "newColor", "null", "RGB Wert von new Image");
        variables.declare("int", "row", "0", "Aktuelle Zeile");
        variables.declare("int", "column", "0", "Aktuelle Spalte");

        des1.hide();
        des2.hide();
        des3.hide();
        des4.hide();

        this.scrCodeProperties.set("highlightColor", Color.RED);
        this.scrCodeProperties.set("font", new Font("Monospaced", 0, 12));

        SourceCode code = this.getSourceCode();

        SourceCode calulateCode = this.getCalulationSourceCode();

        // Setup Properties
        SquareProperties sp = new SquareProperties();
        sp.set("filled", true);
        sp.set("depth", 16);

        SquareProperties marker = new SquareProperties();
        marker.set("filled", true);
        marker.set("depth", 14);

        this.polylineProperties.set("depth", 12);
        int temp = this.pixelWidth - 1;
        Node[] nodes = new Node[]{
                new Coordinates(0, 0),
                new Coordinates(temp, 0),
                new Coordinates(temp, temp),
                new Coordinates(0, temp),
                new Coordinates(0, 0),
                new Coordinates(temp, temp),
                new Coordinates(temp, 0),
                new Coordinates(0, temp)
        };
        Polyline polylineBottom = this.lang.newPolyline(nodes, "polylineBottom", null, this.polylineProperties);
        polylineBottom.hide();
        Polyline polylineTop = this.lang.newPolyline(nodes, "polylineTop", null, this.polylineProperties);
        polylineTop.hide();

        this.lang.nextStep("Algorithm Start");


        // Setup Images
        Map<Integer, Square> bottom = drawImage(bottomImg, 0, 280, code, sp, false);

        Map<Integer, Square> top = drawImage(topImg, bottomImg.getWidth() * this.pixelWidth + 80, 0, bottom.get(0), sp, false);
        Map<Integer, Square> overlay = drawImage(bottomImg, bottomImg.getWidth() * this.pixelWidth + 80, 0, top.get(0), sp, true);

        //
        Text curPixelBtm = this.lang.newText(new Offset(0, -20, bottom.get(0), "NW"), "", "curPiexelBtm", null, defaultTP);
        Text curPixelTop = this.lang.newText(new Offset(0, -20, top.get(0), "NW"), "", "curPiexelBtm", null, defaultTP);

        Square markerBtm = this.lang.newSquare(new Coordinates(0, 0), this.pixelWidth, "ni", null, marker);
        markerBtm.hide();

        Square markerTop = this.lang.newSquare(new Coordinates(0, 0), this.pixelWidth, "ni", null, marker);
        markerTop.hide();

        Square pixelBtm = this.lang.newSquare(new Offset(0, -150, overlay.get(0), "NW"), 20, "ni", null, sp);
        Text operator = this.lang.newText(new Offset(30, 0, pixelBtm, "NW"), "" + this.blendMode, "operator", null, defaultTP);
        Square pixelTop = this.lang.newSquare(new Offset(80, 0, operator, "NW"), 20, "ni", null, sp);
        Text equalSign = this.lang.newText(new Offset(40, 0, pixelTop, "NW"), "=", "equal", null, defaultTP);
        Square pixelResult = this.lang.newSquare(new Offset(20, 0, equalSign, "NW"), 20, "ni", null, sp);

        pixelBtm.hide();
        operator.hide();
        pixelTop.hide();
        equalSign.hide();
        pixelResult.hide();

        Text equationRed = this.lang.newText(new Offset(0, -100, overlay.get(0), "NW"), "", "equationRed", null, defaultTP);
        Text equationGreen = this.lang.newText(new Offset(0, 15, equationRed, "NW"), "", "equationGreen", null, defaultTP);
        Text equationBlue = this.lang.newText(new Offset(0, 15, equationGreen, "NW"), "", "equationBlue", null, defaultTP);
        Text equationAlpha = this.lang.newText(new Offset(0, 15, equationBlue, "NW"), "", "equationBlue", null, defaultTP);

        this.lang.nextStep();
        code.highlight(1);
        this.lang.nextStep();

        // Start Blending
        int rgbBottom;
        int rgbTop;
        for (int row = 0; row < bottomImg.getWidth(); row++) {
            code.unhighlight(1);
            code.highlight(2);
            variables.set("row", "" + row);
            variables.setRole("row", "Aktuelle Zeile");
            this.lang.nextStep("Row: " + row);

            for (int column = 0; column < bottomImg.getHeight(); column++) {
                variables.set("column", "" + column);
                variables.setRole("column", "Aktuelle Spalte");
                code.toggleHighlight(2, 3);

                code.highlight(4);
                code.highlight(5);
                code.highlight(6); // 6 - 9 only when r,g,b are calculated in the method directly
                code.highlight(7);
                code.highlight(8);
                code.highlight(9);
                calulateCode.highlight(1);

                // Mark current pixel in bottom and top image
                curPixelBtm.setText("Aktuell betrachteter Pixel: (" + row + ", " + column + ")", null, null);
                //Square markerBtm = this.lang.newSquare(bottom.get(row * bottomImg.getHeight() + column).getUpperLeft(), this.pixelWidth, "ni", null, marker);
                markerBtm.moveTo("C", "translate", bottom.get(row * bottomImg.getHeight() + column).getUpperLeft(), null, null);
                markerBtm.show();

                curPixelTop.setText("Aktuell betrachteter Pixel: (" + row + ", " + column + ")", null, null);
                //Square markerTop = this.lang.newSquare(top.get(row * bottomImg.getHeight() + column).getUpperLeft(), this.pixelWidth, "ni", null, marker);
                markerTop.moveTo("C", "translate", top.get(row * bottomImg.getHeight() + column).getUpperLeft(), null, null);
                markerTop.show();

                markerBtm.changeColor("fillcolor", Color.WHITE, null, null);
                markerTop.changeColor("fillcolor", Color.WHITE, null, null);

                polylineBottom.show();
                //polylineBottom.moveTo("NW", null, markerBtm.getUpperLeft(), null, null);
                polylineBottom.moveTo("NW", "translate", bottom.get(row * bottomImg.getHeight() + column).getUpperLeft(), null, null);

                polylineTop.show();
                //polylineTop.moveTo("NW", null, markerTop.getUpperLeft(), null, null);
                polylineTop.moveTo("NW", "translate", top.get(row * bottomImg.getHeight() + column).getUpperLeft(), null, null);

                // Get RGB Value of current Pixel
                rgbBottom = bottomImg.getRGB(column, row);
                rgbTop = topImg.getRGB(column, row);

                Color colorBtm = new Color(rgbBottom, true);
                variables.set("rgbBottom", "(" + colorBtm.getRed() + "," + colorBtm.getGreen() + "," + colorBtm.getBlue() + ")");
                variables.setRole("rgbBottom", "RGB Wert von Image A");

                Color colorTop = new Color(rgbTop, true);
                variables.set("rgbTop", "(" + colorTop.getRed() + "," + colorTop.getGreen() + "," + colorTop.getBlue() + ")");
                variables.setRole("rgbTop", "RGB Wert von Image B");

                // Display piexl o piexl =
                /*Square pixelBtm = this.lang.newSquare(new Offset(0, -150, overlay.get(0), "NW"), 20, "ni", null, sp);
                Text operator = this.lang.newText(new Offset(30, 0, pixelBtm, "NW"), "" + this.blendMode, "operator", null, defaultTP);
                Square pixelTop = this.lang.newSquare(new Offset(80, 0, operator, "NW"), 20, "ni", null, sp);
                Text equalSign = this.lang.newText(new Offset(40, 0, pixelTop, "NW"), "=", "equal", null, defaultTP);*/
                pixelBtm.show();
                operator.show();
                pixelTop.show();
                equalSign.show();

                pixelBtm.changeColor("fillcolor", colorBtm, null, null);
                pixelTop.changeColor("fillcolor", colorTop, null, null);

                // Calculate new color
                Color newColor = this.calculateColor(new Color(rgbBottom, true), new Color(rgbTop, true), this.blendMode); // manipulate bottom image
                variables.set("newColor", "(" + newColor.getRed() + "," + newColor.getGreen() + "," + newColor.getBlue() + ")");
                variables.setRole("newColor", "RGB Wert von Image B");

                // Show equations
               /*Text equationRed = this.lang.newText(new Offset(0, -100, overlay.get(0), "NW"), "R: " + this.getEquation(colorBtm.getRed(), colorTop.getRed()) + " = " + newColor.getRed(), "equationRed", null, defaultTP);
                Text equationGreen = this.lang.newText(new Offset(0, 15, equationRed, "NW"), "G: " + this.getEquation(colorBtm.getGreen(), colorTop.getGreen()) + " = " + newColor.getGreen(), "equationGreen", null, defaultTP);
                Text equationBlue = this.lang.newText(new Offset(0, 15, equationGreen, "NW"), "B: " + this.getEquation(colorBtm.getBlue(), colorTop.getBlue()) + " = " + newColor.getBlue(), "equationBlue", null, defaultTP);
                Text equationAlpha = this.lang.newText(new Offset(0, 15, equationBlue, "NW"), "A: " + this.getEquation(colorBtm.getAlpha(), colorTop.getAlpha()) + " = " + newColor.getAlpha(), "equationBlue", null, defaultTP);*/

                equationRed.setText("R: " + this.getEquation(colorBtm.getRed(), colorTop.getRed()) + " = " + newColor.getRed(), null, null);
                equationGreen.setText("G: " + this.getEquation(colorBtm.getGreen(), colorTop.getGreen()) + " = " + newColor.getGreen(), null, null);
                equationBlue.setText("B: " + this.getEquation(colorBtm.getBlue(), colorTop.getBlue()) + " = " + newColor.getBlue(), null, null);
                equationAlpha.setText("A: " + this.getEquation(colorBtm.getAlpha(), colorTop.getAlpha()) + " = " + newColor.getAlpha(), null, null);

                /*equationRed.show();
                equationGreen.show();
                equationBlue.show();
                equationAlpha.show();*/

                this.lang.nextStep();

                code.unhighlight(3);
                code.unhighlight(4);
                code.unhighlight(5); // 6 - 9 only when r,g,b are calculated in the method directly
                code.unhighlight(6);
                code.unhighlight(7);
                code.unhighlight(8);
                calulateCode.unhighlight(1);
                //code.toggleHighlight(5, 6);
                code.toggleHighlight(9, 10);
                //code.highlight(11);
                //code.highlight(7);

                //Color newColor = this.calculateColor(rgbBottom, rgbTop, this.blendMode); // manipulate bottom image

                /*Square pixelResult = this.lang.newSquare(new Offset(20, 0, equalSign, "NW"), 20, "ni", null, sp);*/
                pixelResult.show();
                pixelResult.changeColor("fillcolor", newColor, null, null);


                this.lang.nextStep();
                //code.toggleHighlight(6, 7);
                code.toggleHighlight(10, 11);

                Square pixel = overlay.get(row * bottomImg.getHeight() + column);
                pixel.changeColor("fillcolor", newColor, null, null);
                pixel.show();

                markerBtm.hide();
                markerTop.hide();
                polylineBottom.hide();
                polylineTop.hide();

                /*equationRed.hide();
                equationGreen.hide();
                equationBlue.hide();
                equationAlpha.hide();*/
                equationRed.setText("", null, null);
                equationGreen.setText("", null, null);
                equationBlue.setText("", null, null);
                equationAlpha.setText("", null, null);
                pixelResult.hide();

                pixelBtm.hide();
                pixelTop.hide();
                equalSign.hide();
                operator.hide();

                this.lang.nextStep();

                code.unhighlight(11);
            }
        }


        // Outro
        this.lang.nextStep("Outro");

        code.hide();
        calulateCode.hide();
        curPixelBtm.hide();
        curPixelTop.hide();
        this.hideImageMap(bottom);
        this.hideImageMap(top);
        this.hideImageMap(overlay);

        Text out1 = this.lang.newText(new Offset(0, 25, title, "SW"), "Das ist das Ergebniss :", "out1", null, defaultTP);
        Text out2 = this.lang.newText(new Offset(0, 10, out1, "SW"), "Einstellungen:", "out2", null, defaultTP);
        Text out3 = this.lang.newText(new Offset(0, 10, out2, "SW"), "height: " + this.height, "out3", null, defaultTP);
        Text out4 = this.lang.newText(new Offset(0, 10, out3, "SW"), "width: " + this.width, "out4", null, defaultTP);
        Text out5 = this.lang.newText(new Offset(0, 10, out4, "SW"), "pixelWidth: " + this.pixelWidth, "out5", null, defaultTP);
        Text out6 = this.lang.newText(new Offset(0, 10, out5, "SW"), "Blendmode: " + blendMode, "out5", null, defaultTP);
        Text out7 = this.lang.newText(new Offset(0, 10, out6, "SW"), "Formel: " + this.selectImageArithmetic(this.blendMode), "out5", null, defaultTP);

        this.drawResult(out1, sp);

    }

    private String getEquation(int a, int b) {
        String str = this.selectImageArithmetic(this.blendMode);
        if (this.blendMode == BlendMode.DIFFERENCE) {
            return "Math.abs(" + "" + a + "-" + "" + b + ")";
        }
        str = str.replace("a", "" + a);
        str = str.replace("b", "" + b);
        return str;
    }

    private String getEquation(String a, String b) {
        String str = this.selectImageArithmetic(this.blendMode);
        if (this.blendMode == BlendMode.DIFFERENCE) {
            return "Math.abs(" + a + "-" + b + ")";
        }
        str = str.replace("a", "" + a);
        str = str.replace("b", "" + b);
        return str;
    }

    private void hideImageMap(Map<Integer, Square> map) {
        for (Square square : map.values()) {
            square.hide();
        }
    }

    private HashMap<Integer, Square> drawResult(Primitive ref, SquareProperties sp) {
        int width = 40;
        int height = 40;
        BufferedImage bottomImg = this.setPicture(this.bottomImageURI, width, height);
        BufferedImage topImg = this.setPicture(this.topImageURI, width, height);
        BufferedImage overlay = new BufferedImage(width, height, 2);
        int rgbBottom = 0;
        int rgbTop = 0;
        for (int row = 0; row < bottomImg.getWidth(); row++) {

            for (int column = 0; column < bottomImg.getHeight(); column++) {
                // Get RGB Value of current Pixel
                rgbBottom = bottomImg.getRGB(column, row);
                rgbTop = topImg.getRGB(column, row);

                Color newColor = this.calculateColor(new Color(rgbBottom, true), new Color(rgbTop, true), this.blendMode); // manipulate bottom image
                overlay.setRGB(column, row, newColor.getRGB());
            }
        }
        return drawImage(overlay, 700, 0, ref, sp, false);
    }

    private SourceCode getSourceCode() {
        SourceCode code = this.lang.newSourceCode(new Coordinates(20, 70), "sourceCode", null, this.scrCodeProperties);
        code.addCodeLine("blend(BufferedImage bottom, BufferedImage top){", null, 0, null);
        code.addCodeLine("BufferedImage result = new BufferedImage(bottom.getWidth(), bottom.getHeight(), 2);", null, 1, null); //line 1
        code.addCodeLine("for (int x = 0; x < bottom.getWidth(); x++) {", null, 1, null); // line 2
        code.addCodeLine("for (int y = 0; y < bottom.getHeight(); y++) {", null, 2, null); // line 3
        /*code.addCodeLine("int a = bottom.getRGB(x, y);", null, 3, null);
        code.addCodeLine("int b = top.getRGB(x, y);", null, 3, null);*/
        code.addCodeLine("Color a = new Color(bottom.getRGB(x, y), true);", null, 3, null); // line 4
        code.addCodeLine("Color b = new Color(top.getRGB(x, y), true);", null, 3, null); // line 5
        //code.addCodeLine("int rgb = " + this.selectImageArithmetic(this.blendMode) + ";", null, 3, null);
        /*code.addCodeLine("int red = " + this.getEquation("a.getRed()", "b.getRed()") + ";", null, 3, null); // line 6
        code.addCodeLine("int green = " + this.getEquation("a.getGreen()", "b.getGreen()") + ";", null, 3, null); // line 7
        code.addCodeLine("int blue = " + this.getEquation("a.getBlue()", "b.getBlue()") + ";", null, 3, null); // line 8
        code.addCodeLine("int alpha = " + this.getEquation("a.getAlpha()", "b.getAlpha()") + ";", null, 3, null); // line 9*/
        code.addCodeLine("int red = calculateColor(a.getRed(), b.getRed());", null, 3, null); // line 6
        code.addCodeLine("int green = calculateColor(a.getGreen(), b.getGreen());", null, 3, null); // line 7
        code.addCodeLine("int blue = calculateColor(a.getBlue(), b.getBlue());", null, 3, null); // line 8
        code.addCodeLine("int alpha = calculateColor(a.getAlpha(), b.getAlpha());", null, 3, null); // line 9
        code.addCodeLine("Color color = new Color(red, green, blue, alpha);", null, 3, null); // line 10
        code.addCodeLine("result.setRGB(x, y, color.getRGB());", null, 3, null); // line 11
        code.addCodeLine("}", null, 2, null);
        code.addCodeLine("}", null, 1, null);
        code.addCodeLine("}", null, 0, null);

        return code;
    }

    private SourceCode getCalulationSourceCode() {
        SourceCode code = this.lang.newSourceCode(new Coordinates(720, 70), "calculation", null, this.scrCodeProperties);
        code.addCodeLine("calculateColor(int a, int b){", null, 0, null);
        code.addCodeLine("return " + this.getEquation("a", "b") + ";", null, 1, null);
        code.addCodeLine("};", null, 0, null);
        return code;
    }

    private Color calculateColor(Color a, Color b, BlendMode blendMode) {
        int red = 0;
        int green = 0;
        int blue = 0;
        int alpha = 0;
        switch (blendMode) {
            case ADD:
                red = a.getRed() + b.getRed() > 255 ? 255 : a.getRed() + b.getRed();
                green = a.getGreen() + b.getGreen() > 255 ? 255 : a.getGreen() + b.getGreen();
                blue = a.getBlue() + b.getBlue() > 255 ? 255 : a.getBlue() + b.getBlue();
                alpha = a.getAlpha() + b.getAlpha() > 255 ? 255 : a.getAlpha() + b.getAlpha();
                break;
            case SUBTRACT:
                red = a.getRed() + b.getRed() < 255 ? 255 : a.getRed() + b.getRed() - 255;
                green = a.getGreen() + b.getGreen() < 255 ? 255 : a.getGreen() + b.getGreen() - 255;
                blue = a.getBlue() + b.getBlue() < 255 ? 255 : a.getBlue() + b.getBlue() - 255;
                alpha = a.getAlpha() + b.getAlpha() < 255 ? 255 : a.getAlpha() + b.getAlpha() - 255;
                break;
            case MULTIPLY:
                red = (a.getRed() * b.getRed()) / 255;
                green = (a.getGreen() * b.getGreen()) / 255;
                blue = (a.getBlue() * b.getBlue()) / 255;
                alpha = (a.getAlpha() * b.getAlpha()) / 255;
                break;
            case OVERLAY:
                red = (b.getRed() < 128) ? ((2 * a.getRed() * b.getRed()) / 255) : (255 - (2 * (255 - a.getRed()) * (255 - b.getRed())) / 255);
                green = (b.getGreen() < 128) ? ((2 * a.getGreen() * b.getGreen()) / 255) : (255 - (2 * (255 - a.getGreen()) * (255 - b.getGreen())) / 255);
                blue = (b.getBlue() < 128) ? ((2 * a.getBlue() * b.getBlue()) / 255) : (255 - (2 * (255 - a.getBlue()) * (255 - b.getBlue())) / 255);
                alpha = (b.getAlpha() < 128) ? ((2 * a.getAlpha() * b.getAlpha()) / 255) : (255 - (2 * (255 - a.getAlpha()) * (255 - b.getAlpha())) / 255);
                break;
            case LIGHTEN:
                red = a.getRed() < b.getRed() ? a.getRed() : b.getRed();
                green = a.getGreen() < b.getGreen() ? a.getGreen() : b.getGreen();
                blue = a.getBlue() < b.getBlue() ? a.getBlue() : b.getBlue();
                alpha = a.getAlpha() < b.getAlpha() ? a.getAlpha() : b.getAlpha();
                break;
            case DARKEN:
                red = a.getRed() > b.getRed() ? a.getRed() : b.getRed();
                green = a.getGreen() > b.getGreen() ? a.getGreen() : b.getGreen();
                blue = a.getBlue() > b.getBlue() ? a.getBlue() : b.getBlue();
                alpha = a.getAlpha() > b.getAlpha() ? a.getAlpha() : b.getAlpha();
                break;
            case DIFFERENCE:
                red = Math.abs(a.getRed() - b.getRed());
                green = Math.abs(a.getGreen() - b.getGreen());
                blue = Math.abs(a.getBlue() - b.getBlue());
                alpha = Math.abs(a.getAlpha() - b.getAlpha());
                break;
            default:
                red = b.getRed();
                green = b.getGreen();
                blue = b.getBlue();
                alpha = b.getAlpha();
                break;
        }
        return new Color(red, green, blue, alpha);
    }

    private HashMap<Integer, Square> drawImage(BufferedImage image, int xOffset, int yOffset, Primitive reference, SquareProperties sp, boolean hidden) {
        HashMap<Integer, Square> imgMap = new HashMap<>();
        int x = xOffset;
        int y = yOffset;
        int rgb;
        for (int row = 0; row < image.getWidth(); row++) {
            for (int column = 0; column < image.getHeight(); column++) {
                Square pixel = this.lang.newSquare(new Offset(x, y, reference, "SN"), this.pixelWidth, "ni", null, sp);
                rgb = image.getRGB(column, row);
                Color col = new Color(rgb, true);
                Color newColor = new Color(col.getRed(), col.getGreen(), col.getBlue());
                pixel.changeColor("fillColor", newColor, null, null);

                if (hidden)
                    pixel.hide();
                imgMap.put(row * image.getHeight() + column, pixel);
                x += this.pixelWidth;
            }

            y += this.pixelWidth;
            x = xOffset;
        }
        return imgMap;
    }

    private String selectImageArithmetic(BlendMode blendMode) {
        switch (blendMode) {
            case SUBTRACT:
                return SUBTRACT;
            case MULTIPLY:
                return MULTIPLY;
            case OVERLAY:
                return OVERLAY;
            case DIFFERENCE:
                return DIFFERENCE;
            case DARKEN:
                return DARKEN;
            case LIGHTEN:
                return LIGHTEN;
            default:
                return ADD;
        }
    }

    private BufferedImage setPicture(String uri, int width, int height) {

        BufferedImage image;
        try {
            File file = new File(uri);
            image = ImageIO.read(file);
            this.imgStatus = ImageStatus.FILE;
        } catch (IOException IOEx) {
            try {
                image = ImageIO.read(new URL(uri));
                this.imgStatus = ImageStatus.URL;
            } catch (IOException ex) {
                try {
                    image = ImageIO.read(AnimalReader.getInputStreamOnLayer(AnimalImageDummy.class, "Animal.jpg"));
                    this.imgStatus = ImageStatus.DEFAULT;
                } catch (IOException e) {
                    this.imgStatus = ImageStatus.FAILURE;
                    return null;
                }

            }
        }

        return createBufferedImage(image, width, height);

    }

    private BufferedImage createBufferedImage(BufferedImage image, int width, int height) {
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        BufferedImage bufferedImage = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bufferedImage.createGraphics();
        bGr.drawImage(scaledImage, 0, 0, null);
        bGr.dispose();
        return bufferedImage;
    }

    public static void main(String[] args) {

        RGBImageBlender blender = new RGBImageBlender();
        Animal.startGeneratorWindow(blender);
    }
}
