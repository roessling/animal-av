package generators.cryptography;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.StringMatrix;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import java.util.Hashtable;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class Polybios implements Generator {
    private Language lang;
    private SourceCodeProperties pseudoProps;
    private MatrixProperties matrixProps;
    private String Klartext;
    private ArrayProperties arrayProps;
    private TextProperties resultProps;
    private Text[] introText = new Text[4];
    
    public void init(){
        lang = new AnimalScript("Polybios [DE]", "Rene Schubert, Andre Schubert", 800, 600);
    lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        pseudoProps = (SourceCodeProperties)props.getPropertiesByName("Pseudocode");
        matrixProps = (MatrixProperties)props.getPropertiesByName("Matrix");
        Klartext = (String)primitives.get("Klartext");
        arrayProps = (ArrayProperties)props.getPropertiesByName("Eingabe");
        resultProps = (TextProperties)props.getPropertiesByName("Ergebnis");
        
        doPolybios(Klartext);
        
        return lang.toString();
    }

    public String getName() {
        return "Polybios [DE]";
    }

    public String getAlgorithmName() {
        return "Polybios";
    }

    public String getAnimationAuthor() {
        return "Rene Schubert, Andre Schubert";
    }

    public String getDescription(){
        return "Die Polybios-Chiffre ist eine monoalphabetische Substitution."
 +"\n"
 +"Sie uebertraegt Zeichen in Zeichengruppen. Zur Uebersetzung in Zeichenpaare "
 +"\n"
 +"sucht man das gewuenschte Einzelzeichen (Buchstaben) in einer Polybios-Matrix "
 +"\n"
 +"heraus. Aus den Koordinaten des Buchstabens erhaelt man die gesuchte Kodierung."
 +"\n";
    }

    public String getCodeExample(){
        return "Fuer jeden Buchstaben:"
 +"\n"
 +" Suche Buchstaben in Matrix (J=I)"
 +"\n"
 +" Notiere Zeile"
 +"\n"
 +" Notiere Spalte"
 +"\n"
 +"Ende";
    }

    public String getFileExtension(){
        return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public Locale getContentLocale() {
        return Locale.GERMANY;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }
    
    public void doPolybios(String plainString) {
    
      TextProperties textprop = new TextProperties();
      textprop.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("SansSerif", Font.BOLD, 24));
      @SuppressWarnings("unused")
      Text header = lang.newText(new Coordinates(285,12), "Polybios-Chiffre", "header", null, textprop);
  
      RectProperties rectProps = new RectProperties();
      rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
      rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
      rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
      rectProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.LIGHT_GRAY);
      
      lang.newRect(new Coordinates(0,0), new Coordinates(800,35), "headerBackground", null, rectProps);
      
      introText[0] = lang.newText(new Coordinates(20, 90), "Die Polybios-Chiffre ist eine monoalphabetische Substitution.", "intro1", null);
      introText[1] = lang.newText(new Coordinates(20, 120), "Sie uebertraegt Zeichen in Zeichengruppen. Im engeren Sinn ist die Verschluesselung von Einzelzeichen", "intro2", null);
      introText[2] = lang.newText(new Coordinates(20, 140), "als Zeichenpaare gemeint. Zur Uebersetzung in Zeichenpaare sucht man das gewuenschte Einzelzeichen (Buchstaben)", "intro3", null);
      introText[3] = lang.newText(new Coordinates(20, 160), "in einer Polybios-Matrix heraus. Aus den Koordinaten des Buchstabens erhaelt man die gesuchte Kodierung.", "intro4", null);
      

      
      String plain = plainString.toUpperCase().replaceAll("[^A-Z]", "");
      plain = plain.replace('J', 'I');
      String chiffre = "Ergebnis: ";
      
    //  lang.nextStep(); 
      
      String[][] matrixContent = new String[][] { {" ", "1", "2", "3", "4", "5"}, 
                            {"1", "A", "B", "C", "D", "E"}, 
                            {"2", "F", "G", "H", "I", "K"}, 
                            {"3", "L", "M", "N", "O", "P"}, 
                            {"4", "Q", "R", "S", "T", "U"}, 
                            {"5", "V", "W", "X", "Y", "Z"}};
      StringMatrix matrix = lang.newStringMatrix(new Coordinates(50, 120), matrixContent, "poly", null, matrixProps);
      
      for (int i=0; i<introText.length; i++) {
        introText[i].hide();
      }
      
      SourceCode pseudo = lang.newSourceCode(new Coordinates(50, 300), "pseudoCode", null, pseudoProps);
      pseudo.addCodeLine("Fuer jeden Buchstaben:", null, 0, null);
      pseudo.addCodeLine("Suche Buchstaben in Matrix (J=I)", null, 1, null); 
      pseudo.addCodeLine("Notiere Zeile", null, 1, null); 
      pseudo.addCodeLine("Notiere Spalte", null, 1, null); 
      pseudo.addCodeLine("Ende", null, 0, null); 
    
      Text input = lang.newText(new Coordinates(300,150), "", "plain", null);
      input.setText("Zu verschluesselnder Text:" , null, null);
      
      Text result = lang.newText(new Offset(0, 50, input, AnimalScript.DIRECTION_SW), chiffre, "chiffre", null, resultProps);

      String[] inputArray = new String[plain.length()];
      for (int i=0; i<plain.length(); i++) {
        inputArray[i] = plain.substring(i, i+1);
      }

      StringArray sa = lang.newStringArray(new Offset(150, 0, input, AnimalScript.DIRECTION_NE), inputArray, "inputArray", null, arrayProps);
      
      ArrayMarkerProperties arrayMProps = new ArrayMarkerProperties();
      arrayMProps.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");   
      arrayMProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
      ArrayMarker marker = lang.newArrayMarker(sa, 0, "", null, arrayMProps);
      
      int length = plain.length();
      char letter;
      int[] found = new int[2];
      
      for (int i=0; i<length; i++) {
        
        letter = plain.charAt(i);
        marker.move(i, null, null);
        
        for (int row=1; row<=5; row++) {
          for (int column=1; column<=5; column++) {
            if (matrixContent[row][column].charAt(0) == letter) {
              found[0] = row;
              found[1] = column;
            }
          }
        }

        matrix.highlightElem(found[0], found[1], null, null);
        pseudo.unhighlight(2);
        pseudo.unhighlight(3);
        pseudo.highlight(1);
        
        lang.nextStep((i+1) + ". Iteration");   
        matrix.highlightElem(found[0], 0, null, null);
        chiffre += String.valueOf(found[0]);
        result.setText(chiffre, null, null);
        pseudo.unhighlight(1);
        pseudo.unhighlight(3);
        pseudo.highlight(2);
        
        lang.nextStep();
        matrix.highlightElem(0, found[1], null, null);
        chiffre += String.valueOf(found[1]);
        result.setText(chiffre, null, null);
        pseudo.unhighlight(1);
        pseudo.unhighlight(2);
        pseudo.highlight(3);
        
        lang.nextStep();
        matrix.unhighlightElem(found[0], 0, null, null);
        matrix.unhighlightElem(0, found[1], null, null);
        matrix.unhighlightElem(found[0], found[1], null, null);
        pseudo.unhighlight(1);
        pseudo.unhighlight(2);
        pseudo.unhighlight(3);
  
      }
      pseudo.highlight(4);
      lang.nextStep();
      pseudo.hide();
      matrix.hide();
      result.hide();
      input.hide();
      sa.hide();
      marker.hide();
      
      SourceCode outro = lang.newSourceCode(new Coordinates(40, 120), "outro", null);
      outro.addCodeLine("Abschliessende Bemerkungen: ", null, 0, null);
      outro.addCodeLine("", null, 1, null); 
      outro.addCodeLine("Sicherheit: ", null, 1, null); 
      outro.addCodeLine("Es handelt sich bei der Polybius-Chiffre nur um eine monoalphabetische Ersetzung", null, 2, null); 
      outro.addCodeLine("der Buchstaben, die keinen wirklichen Schutz bietet. Sie ist mit einer ", null, 2, null); 
      outro.addCodeLine("Haeufigkeitsanalyse leicht zu knacken.", null, 2, null); 
      outro.addCodeLine("", null, 0, null); 
      outro.addCodeLine("Komplexitaet: ", null, 1, null); 
      outro.addCodeLine("O(n) ", null, 2, null); 
      lang.nextStep("Abschliessende Bemerkungen");
    }
    
    
}