package generators.cryptography.blowfish;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;

import algoanim.animalscript.AnimalScript;

import java.awt.*;
import java.util.Hashtable;
import java.util.Locale;

/**
 * Blowfish Encryption Generator class.
 *
 * @author Dani El-Soufi (dani.el-soufi@stud.tu-darmstadt.de)
 * @author Deniz Can Franz Ertan (deniz_can_franz.ertan@stud.tu-darmstadt.de)
 */
public class BlowfishDecryptionGenerator extends BlowfishAnimator implements ValidatingGenerator {

    private Language lang;

    public void init() {
        lang = new AnimalScript("Blowfish Decryption Generator", "Dani El-Soufi , Deniz Can Franz Ertan", 800, 600);
        lang.setStepMode(true);
        setLanguage(lang);
    }

    public String generate(AnimationPropertiesContainer properties, Hashtable<String, Object> primitives) {
        String secret = (String)primitives.get("Geheimtext");
        String cipher = (String)primitives.get("Ciphertext");

        setSecret(secret);
        setCipher(cipher);

        try {
            Blowfish blowfish = new Blowfish(secret, this);
            String text = blowfish.decrypt(cipher);
            setPlainText(text);
        } catch (BlowfishException e) {
            System.err.println("Error: " + e.getMessage());
        }

        animateDecryption();

        TextProperties ueberschrift = new TextProperties();
        ueberschrift.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        ueberschrift.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 55));
        ueberschrift.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        ueberschrift.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        String Ueberschrift = "                                 BLOWFISH DECRYPTION              ";
        Text text0 = lang.newText(new Coordinates(1200, 40), Ueberschrift, "Beschreibung",null, ueberschrift);

        TextProperties Author = new TextProperties();
        Author.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        Author.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 24));
        Author.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        Author.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        String Dani = "                                 Dani El-Soufi         ";
        String Deniz = "                                Deniz Can Franz Ertan          ";
        Text author1 = lang.newText(new Coordinates(2100, 20), Dani, "Beschreibung",null, Author);
        Text author2 = lang.newText(new Coordinates(2100, 20), Deniz, "Beschreibung",null, Author);


        setAbschlussTextDEC();
        TextProperties abschlussText = new TextProperties();
        abschlussText.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        abschlussText.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 40));
        abschlussText.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        abschlussText.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        Text endText = lang.newText(new Coordinates(1300, 1300), getPlainText(), "entschlüsselung",null, abschlussText);

        lang.nextStep();
        return lang.toString();
    }

    /**
     * Validation function called to check the given primitives. Returns true if the secret is a multiple of 32bit
     * and the plain text is a multiple of 64bit, otherwise throws {@link IllegalArgumentException}.
     *
     * @param properties
     *                                  In Animal set properties
     * @param primitives
     *                                  In Animal set primitives (variables)
     * @return
     *                                  True if the secret is a multiple of 32bit and the plain text is a multiple of
     *                                  64bit, otherwise throws {@link IllegalArgumentException}
     * @throws IllegalArgumentException
     *                                  Throws if the validation criteria is not met
     */
    public boolean validateInput(AnimationPropertiesContainer properties, Hashtable<String, Object> primitives)
            throws IllegalArgumentException {
        String secret = (String)primitives.get("Geheimtext");

        return validate(secret);
    }

    public String getName() {
        return "Blowfish Decryption Generator";
    }

    public String getAlgorithmName() {
        return "Blowfish Decryption Generator ";
    }

    public String getAnimationAuthor() {
        return "Dani El-Soufi , Deniz Can Franz Ertan";
    }

    public String getDescription(){
        return "  Blowfish ist ein symmetrisches Verschlüsselungsverfahren, dass mit einer \n"
                +" Blockverschlüsselung arbeit. Es basiert auf einem Feistelnetzwerk und hat\n"
                +" eine Blocklänge von 64Bit und garantiert die Umkehrbarkeit zwischen Ver-  \n"
                +" und Entschlüsselung. \n"
                +" Funktionsweise: \n"
                +"   				Ein 64Bit langer Klartext wird in zwei 32Bit lange Hälften\n"
                +"					aufgeteilt L1 und R1. Die Hauptverschlüsselung Basiert auf\n"
                +"					16 Runden verschlüsselung des Klartextes. Bevor verschlüsselt\n"
                +"					werden kann, findet eine Initialisierung der Rundenschlüssel, \n"
                +"					mit denen in den 16 Runden verschlüsselt wird statt.\n"
                +"                  Nach der Initialisierung der RundenSchlüssel, wird die Hälfte \n"
                +"					L1 mit dem Rundenschlüssel P1 geXORt und ergibt den Runden- \n"
                +"					schlüssel (Pi+1: i = Anzahl der Runde) P2. Das Ergebnis P2  \n"
                +"					wird anschließend in die Rundenfunktion F eingegeben und mit \n"
                +"					(Ri) R1 geXORt. Danach werden die beiden Hälften vertauscht und \n"
                +"					und dieser ablauf wiederholt sich 16mal, bis am Ende die beiden \n"
                +"					Hälften mit den Rundenschlüsseln P17 und P18 geXORt werden.\n"
                +"					L18 und R18 bilden zusammen den 64Bit langen Schlüsseltext.\n"
                +"                  Die Entschlüsselung verläuft genau Rückwärts, indem DIe PBoxen \n"
                +"                  in umgekehrter reihenfolge L und R in den 16 Runden \"verschlüsseln\" .\n\n\n"
                +"Bitte beachten Sie, dass der Geheimtext vielfaches von 32bit (4 Zeichen) sein muss\n"
                +"und der Ciphertext hexadezimal sein muss.\n";
    }

    public String getCodeExample(){
        return "public void decrypt(int L , int R )"    // 0
                + "\n{"                                                                                // 1
                + "\n  for(int i = 16 ; i > 0 ; i -= 2){"                                                 // 2
                + "\n 	L ^= P[i];"                                                                  // 3
                + "\n  	R ^= f(L);"                                                                  // 4
                + "\n     R ^= P[i+1];"                                                                // 5
                + "\n     L ^= f(R);"                                                                  // 6
                + "\n    }"                                                                            // 7
                + "\n    L ^= P[16];"                                                                  // 8
                + "\n    R ^= P[17];"                                                                  // 9
                + "\n    swap(L, R);"                                                                  // 10
                + "\n   }"                                                                             // 11
                ;
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_CRYPT);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    public void setAbschlussTextDEC(){
        // Encryption
        SourceCodeProperties scProps = new SourceCodeProperties();
        scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
        scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 40));
        scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        // Encrypt
        SourceCode abschlussText = lang.newSourceCode(new Coordinates(250, 300), "sourceCodeENC",null, scProps);

        abschlussText.addCodeLine("Nach der erfolgreichen Entschluesselung des Ciphertext,", null, 1,null); //
        abschlussText.addCodeLine(" " , null, 1, null);
        abschlussText.addCodeLine("erlangt der Anwender fuer einen, zum plaintext korrespondierenden Ciphertext", null, 1, null);
        abschlussText.addCodeLine("" , null, 1, null); // 3
        abschlussText.addCodeLine("Ciphertext(Vielfaches von 16Byte) und einem Key(Vielfaches von 8Byte)", null, 1, null); // 4
        abschlussText.addCodeLine("", null, 1, null); // 5
        abschlussText.addCodeLine("seinen korrespondierenden Plaintext, welcher die halbe Laenge des Ciphertext besitzt.", null, 1, null); // 6
        abschlussText.addCodeLine("", null, 1, null); // 7
        abschlussText.addCodeLine("", null, 1, null); // 8
        abschlussText.addCodeLine("", null, 1, null); // 9


        TextProperties ueberschrift = new TextProperties();
        ueberschrift.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        ueberschrift.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 40));
        ueberschrift.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        ueberschrift.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        String Ueberschrift = "  Plaintext:   ";
        Text text0 = lang.newText(new Coordinates(800, 1300), Ueberschrift, "Beschreibung",null, ueberschrift);
    }

}
