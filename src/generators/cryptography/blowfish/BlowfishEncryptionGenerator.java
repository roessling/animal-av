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
public class BlowfishEncryptionGenerator extends BlowfishAnimator implements ValidatingGenerator {

    private Language lang;

    public void init() {
        lang = new AnimalScript("Blowfish Encryption Generator", "Dani El-Soufi , Deniz Can Franz Ertan", 800, 600);
        lang.setStepMode(true);
        setLanguage(lang);
    }

    public String generate(AnimationPropertiesContainer properties, Hashtable<String, Object> primitives) {
        String secret = (String)primitives.get("Geheimtext");
        String text = (String)primitives.get("Klartext");

        setSecret(secret);
        setPlainText(text);

        try {
            Blowfish blowfish = new Blowfish(secret, this);
            String cipher = blowfish.encrypt(text);
            setCipher(cipher);
            copyToClipboard(cipher);
        } catch (BlowfishException e) {
            System.err.println("Error: " + e.getMessage());
        }

        animateEncryption();

        TextProperties ueberschrift = new TextProperties();
        ueberschrift.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        ueberschrift.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 55));
        ueberschrift.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        ueberschrift.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        String Ueberschrift = "                                 BLOWFISH ENCRYPTION              ";
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


        setAbschlussText();
        TextProperties abschlussText = new TextProperties();
        abschlussText.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        abschlussText.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 40));
        abschlussText.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        abschlussText.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

        TextProperties infoText = new TextProperties();
        infoText.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
        infoText.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 40));
        infoText.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        infoText.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

        boolean success = copyToClipboard(getCipher());
        if (success) {
            String info = "INFO: Der Ciphertext wurde in der Zwischenablage gespeichert";
            lang.newText(new Coordinates(1300, 1200), info, "info", null, infoText);
        }
        Text endText = lang.newText(new Coordinates(1300, 1300), getCipher(), "ciphertext",null, abschlussText);

        lang.nextStep();

        // hier kommt die beschreibung rein und cipher

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
        String text = (String)primitives.get("Klartext");

        return validate(secret, text);
    }

    public String getName() {
        return "Blowfish Encryption Generator";
    }

    public String getAlgorithmName() {
        return "Blowfish Encryption Generator ";
    }

    public String getAnimationAuthor() {
        return "Dani El-Soufi, Deniz Can Franz Ertan";
    }

    public String getDescription(){
        return "Blowfish ist ein symmetrisches Verschluesselungsverfahren, dass mit einer\n"

                +"Blockverschluesselung arbeit. Es basiert auf einem Feistelnetzwerk und hat\n"

                +"eine Blocklaenge von 64Bit und garantiert die Umkehrbarkeit zwischen Ver-  \n"

                +"und Entschluesselung. \n"

                +"	Funktionsweise: \n "

                +"			Ein 64Bit langer Klartext wird in zwei 32Bit lange Haelften\n"

                +"			aufgeteilt L1 und R1. Die Hauptverschluesselung Basiert auf\n"

                +"			16 Runden verschluesselung des Klartextes. Bevor verschluesselt\n"

                +"			werden kann, findet eine Initialisierung der Rundenschluessel, \n"

                +"			mit denen in den 16 Runden verschluesselt wird statt.\n"

                +"			Nach der Initialisierung der RundenSchluessel, wird die Haelfte\n"

                +"			L1 mit dem Rundenschluessel P1 geXORt und ergibt den Runden- \n"

                +"			schluessel (Pi+1: i = Anzahl der Runde) P2. Das Ergebnis P2 \n "

                +"			wird anschließend in die Rundenfunktion F eingegeben und mit \n"

                +"			(Ri) R1 geXORt. Danach werden die beiden Haelften vertauscht und\n "

                +"			und dieser ablauf wiederholt sich 16mal, bis am Ende die beiden \n"

                +"			Haelften mit den Rundenschluesseln P17 und P18 geXORt werden.\n"

                +"			L18 und R18 bilden zusammen den 64Bit langen Schluesseltext.\n\n\n"

                +"Bitte beachten Sie, dass der Geheimtext vielfaches von 32bit (4 Zeichen) sein muss\n"
                +"und der Klartext vielfaches von 64bit (8 Zeichen) sein muss.\n";


    }

    public String getCodeExample(){
        return "public void encrypt(int L , int R ) {"
                +"\n"
                +"  for (int i = 0; i < 16 ; i += 2) {"
                +"\n"
                +"       L ^= P[i];"
                +"\n"
                +"       R ^= f(L);"
                +"\n"
                +"       R ^= P[i+1];"
                +"\n"
                +"       L ^= f(R);"
                +"\n"
                +"    }"
                +"\n"
                +"    L ^= P[16];"
                +"\n"
                +"    R ^= P[17];"
                +"\n"
                +"    swap(L, R);"
                +"\n"
                +"}"
                +"\n";
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

    public void setAbschlussText(){
        // Encryption
        SourceCodeProperties scProps = new SourceCodeProperties();
        scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
        scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 40));
        scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        // Encrypt
        SourceCode abschlussText = lang.newSourceCode(new Coordinates(400, 300), "sourceCodeENC",null, scProps);

        abschlussText.addCodeLine("Nach der erfolgreichen verschluesselung eines Plaintextes,", null, 1,null); //
        abschlussText.addCodeLine(" " , null, 1, null);
        abschlussText.addCodeLine("erlangt der Anwender fuer eine variable Eingabe von einem", null, 1, null);
        abschlussText.addCodeLine("" , null, 1, null); // 3
        abschlussText.addCodeLine("Plaintext (Vielfaches von 8Byte) und einem Key(Vielfaches von 8Byte)", null, 1, null); // 4
        abschlussText.addCodeLine("", null, 1, null); // 5
        abschlussText.addCodeLine("ein Cipher, welcher die doppelte Laenge des Plaintextes besitzt.", null, 1, null); // 6
        abschlussText.addCodeLine("", null, 1, null); // 7
        abschlussText.addCodeLine("", null, 1, null); // 8
        abschlussText.addCodeLine("", null, 1, null); // 9
        abschlussText.addCodeLine("Bitte kopieren sie den folgenden Ciphertext der unten steht, ", null, 1, null);
        abschlussText.addCodeLine("", null, 1, null);
        abschlussText.addCodeLine("dies ist der korrespondierende cipher zu ihrem eingegebenen ", null, 1, null);
        abschlussText.addCodeLine("", null, 1, null);
        abschlussText.addCodeLine("Plaintext :", null, 1, null);

        TextProperties ueberschrift = new TextProperties();
        ueberschrift.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        ueberschrift.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 40));
        ueberschrift.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        ueberschrift.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

        String Ueberschrift = "  Ciphertext :   ";
        Text text0 = lang.newText(new Coordinates(300, 1300), Ueberschrift, "Beschreibung",null, ueberschrift);
    }

}
