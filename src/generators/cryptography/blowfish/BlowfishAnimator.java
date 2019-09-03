package generators.cryptography.blowfish;

import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

/**
 * Blowfish Animator base class.
 *
 * @author Dani El-Soufi (dani.el-soufi@stud.tu-darmstadt.de)
 * @author Deniz Can Franz Ertan (deniz_can_franz.ertan@stud.tu-darmstadt.de)
 */
public abstract class BlowfishAnimator implements BlowfishAnimatorCallback {

    String laeufer = "i: ";
    String F = "F";
    String plaintextSchrift= "PLAINTEXT";
    String xTimes = "x";
    String repeat = " Times complete Encryption";
    String kleinBit = "32Bit";
    String greatBit = "64Bit";
    String B1 = "P1";
    String B18 = "P18";
    String B2 = "P2";
    String B3 = "P17";
    String B4 = "P18";
    String zwischen = "14 More Iteration";
    String weit = "14 weitere Runden ";
    String weitplus = "16. Runde Ohne L und R tausch ";

    String p71 = "P17";
    String p81 = "P18";
    String p1 = "P1";
    String p0 = "P0";

    String swap = " L und R werden am Ende immer getauscht ";

    String links = "L";
    String rechts = "R";

    String xor= "XOR";
    String add= "ADD";
    Square sq0;
    Square sq1;

    Polyline pol0 ;
    Polyline pol1 ;
    Polyline pol2 ;
    Polyline pol3 ;
    Polyline pol4 ;
    Polyline pol5 ;
    Polyline pol6 ;
    Polyline pol7 ;
    Polyline pol8 ;
    Polyline pol9 ;
    Polyline pol10 ;
    Polyline pol11 ;
    Polyline pol12 ;
    Polyline pol13 ;
    Polyline pol14 ;

    Polyline pofl0 ;
    Polyline pofl1 ;
    Polyline pofl2 ;
    Polyline pofl3 ;
    Polyline pofl4 ;
    Polyline pofl5 ;
    Polyline pofl6 ;
    Polyline pofl7 ;
    Polyline pofl8 ;
    Polyline pofl9 ;
    Polyline pofl10 ;
    Polyline pofl11 ;
    Polyline polx;
    Polyline polc1;
    Polyline polc2;

    Polyline Line0;
    Polyline Line1;
    Polyline Line2;
    Polyline Line3;
    Polyline Line4;
    Polyline Line5;

    Circle circ0;
    Circle circ1;
    Circle circ2;
    Circle circ3;
    Circle circ4;
    Circle circ5;
    Circle circ6;
    Circle circ7;
    Circle circ8;
    Circle circ9;
    Circle circ10;
    Circle circ11;
    Circle circ12;
    Circle circ13;
    Circle circSbox;

    Text FunktionString0;
    Text FunktionString1;

    Text Left;
    Text Right;
    Text SchriftP17;
    Text SchriftP18;

    Text PlaintextSchrift;
    Text Wiederholung;
    Text Schrift1;
    Text SchriftP1;
    Text SchriftP2;
    Text SchriftP3;
    Text SchriftP4;
    Text dreizweiBit;
    Text dreizweiBit1;
    Text dreizweiBit2;
    Text dreizweiBit3;
    Text zwischentext;
    Text I;
    Text ersteF;
    Text weiter;
    Text weiterplus;
    Text xorf1;
    Text add0;
    Text add1;
    Text weiterRED;
    Text weiterplusRED;
    Text swapx;

    Text txf0;
    Text txf1;
    Text txf2;
    Text txf3;
    Text grosBit;

    Text Xors;
    Text Xorsx;
    Text Xorsxx;
    Text Xors1;
    Text Xors2;
    Text Xors3;
    Text Xors4;

    Square sqf1;
    Square sqf2;
    Square sqf3;
    Square sqf4;
    Square sqf5;
    Square sqf6;

    Circle circf1;

    private String secret;
    private String plainText;
    private String cipher;

    private static final String DESCRIPTION     = "  Blowfish ist ein symmetrisches Verschluesselungsverfahren, dass mit einer \n"
            +" Blockverschluesselung arbeit. Es basiert auf einem Feistelnetzwerk und hat\n"
            +" eine Blocklaenge von 64Bit und garantiert die Umkehrbarkeit zwischen Ver-  \n"
            +" und Entschluesselung. \n"
            +" Funktionsweise: \n"
            +"   				Ein 64Bit langer Klartext wird in zwei 32Bit lange Haelften\n"
            +"					aufgeteilt L1 und R1. Die Hauptverschluesselung Basiert auf\n"
            +"					16 Runden verschluesselung des Klartextes. Bevor verschluesselt\n"
            +"					werden kann, findet eine Initialisierung der Rundenschluessel, \n"
            +"					mit denen in den 16 Runden verschluesselt wird statt.\n"
            +"                  Nach der Initialisierung der RundenSchluessel, wird die Haelfte \n"
            +"					L1 mit dem Rundenschluessel P1 geXORt und ergibt den Runden- \n"
            +"					schluessel (Pi+1: i = Anzahl der Runde) P2. Das Ergebnis P2  \n"
            +"					wird anschliessend in die Rundenfunktion F eingegeben und mit \n"
            +"					(Ri) R1 geXORt. Danach werden die beiden Haelften vertauscht und \n"
            +"					und dieser ablauf wiederholt sich 16mal, bis am Ende die beiden \n"
            +"					Haelften mit den Rundenschluesseln P17 und P18 geXORt werden.\n"
            +"					L18 und R18 bilden zusammen den 64Bit langen Schluesseltext.\n"
            ;

    private static final String DESCRIPTIONDEC     = "  Blowfish ist ein symmetrisches Verschluesselungsverfahren, dass mit einer \n"
            +" Blockverschluesselung arbeit. Es basiert auf einem Feistelnetzwerk und hat\n"
            +" eine Blocklaenge von 64Bit und garantiert die Umkehrbarkeit zwischen Ver-  \n"
            +" und Entschluesselung. \n"
            +" Funktionsweise: \n"
            +"   				Ein 64Bit langer Klartext wird in zwei 32Bit lange Haelften\n"
            +"					aufgeteilt L1 und R1. Die Hauptverschluesselung Basiert auf\n"
            +"					16 Runden verschluesselung des Klartextes. Bevor verschluesselt\n"
            +"					werden kann, findet eine Initialisierung der Rundenschluessel, \n"
            +"					mit denen in den 16 Runden verschluesselt wird statt.\n"
            +"                  Nach der Initialisierung der RundenSchluessel, wird die Haelfte \n"
            +"					L1 mit dem Rundenschluessel P1 geXORt und ergibt den Runden- \n"
            +"					schluessel (Pi+1: i = Anzahl der Runde) P2. Das Ergebnis P2  \n"
            +"					wird anschliessend in die Rundenfunktion F eingegeben und mit \n"
            +"					(Ri) R1 geXORt. Danach werden die beiden Haelften vertauscht und \n"
            +"					und dieser ablauf wiederholt sich 16mal, bis am Ende die beiden \n"
            +"					Haelften mit den Rundenschluesseln P17 und P18 geXORt werden.\n"
            +"					L18 und R18 bilden zusammen den 64Bit langen Schluesseltext.\n"
            +"                  Die Entschluesselung verlaeuft genau Rueckwaerts, indem DIe PBoxen \n"
            +"                  in umgekehrter reihenfolge L und R in den 16 Runden \"verschluesseln\" .\n"
            ;

    private static final String SOURCE_CODE_ENCRYPT     = "public void encrypt(int L , int R )"    // 0
            + "\n{"                                                                                // 1
            + "\n  for(int i = 0; i < 16 ; i+=2){"                                                 // 2
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

    private static final String SOURCE_CODE_DECRYPTION     = "public void decrypt(int L , int R )"    // 0
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

    private static final String SOURCE_CODE_F     = "public int f(int x)"    // 0
            + "\n{"                                                                                // 1
            + "\n  int h = S[0][x >> 24] + S[1][x >> 16 && 0xff]; "                                                 // 2
            + "\n   return ( h ^ S[2][x >> 8 && 0xff ] ) + S[3][x && 0xff ];"                                                                  // 3
            + "\n}"
            ;

    /**
     * default duration for swap processes
     */
    public final static Timing defaultDuration = new TicksTiming(36);


    final int[] P = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
    final int[] PD = {16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1};

    private Language lang;




    /**
     * Animate encryption procedure.
     */

    public void animateEncryption() {

        SourceCodeProperties scProps = new SourceCodeProperties();
        scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 40));
        scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        SourceCode vorText = lang.newSourceCode(new Coordinates(400, 300), "sourceCodeENC",null, scProps);

        vorText.addCodeLine("Blowfish ist ein symmetrisches Verschluesselungsverfahren, dass mit einer", null, 1,null); //
        vorText.addCodeLine("Blockverschluesselung arbeit. Es basiert auf einem Feistelnetzwerk und hat" , null, 1, null);
        vorText.addCodeLine("eine Blocklaenge von 64Bit und garantiert die Umkehrbarkeit zwischen Ver-  ", null, 1, null);
        vorText.addCodeLine("und Entschluesselung. " , null, 1, null); // 3
        vorText.addCodeLine("Funktionsweise: ", null, 1, null); // 4
        vorText.addCodeLine("Ein 64Bit langer Klartext wird in zwei 32Bit lange Haelften", null, 5, null); // 5
        vorText.addCodeLine("aufgeteilt L1 und R1. Die Hauptverschluesselung Basiert auf", null, 5, null); // 6
        vorText.addCodeLine("16 Runden verschluesselung des Klartextes. Bevor verschluesselt", null, 5, null); // 7
        vorText.addCodeLine("werden kann, findet eine Initialisierung der Rundenschluessel, ", null, 5, null); // 8
        vorText.addCodeLine("mit denen in den 16 Runden verschluesselt wird statt.", null, 5, null); // 9
        vorText.addCodeLine("Nach der Initialisierung der RundenSchluessel, wird die Haelfte ", null, 5, null); // 10
        vorText.addCodeLine("L1 mit dem Rundenschluessel P1 geXORt und ergibt den Runden- ", null, 5, null); // 11
        vorText.addCodeLine("schluessel (Pi+1: i = Anzahl der Runde) P2. Das Ergebnis P2  ", null, 5, null); // 11
        vorText.addCodeLine("wird anschliessend in die Rundenfunktion F eingegeben und mit ", null, 5, null); // 11
        vorText.addCodeLine("(Ri) R1 geXORt. Danach werden die beiden Haelften vertauscht und ", null, 5, null); // 11
        vorText.addCodeLine("und dieser ablauf wiederholt sich 16mal, bis am Ende die beiden ", null, 5, null); // 11
        vorText.addCodeLine("Haelften mit den Rundenschluesseln P17 und P18 geXORt werden.", null, 5, null); // 11
        vorText.addCodeLine("L18 und R18 bilden zusammen den 64Bit langen Schluesseltext.", null, 5, null); // 11





        TextProperties Author = new TextProperties();
        Author.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        Author.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 24));
        Author.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        Author.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

        TextProperties ueberschrift = new TextProperties();
        ueberschrift.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        ueberschrift.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 55));
        ueberschrift.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        ueberschrift.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

        String Ueberschrift = "                                 BLOWFISH ENCRYPTION              ";
        String Dani = "                                 Dani El-Soufi         ";
        String Deniz = "                                Deniz Can Franz Ertan          ";
        Text text0 = lang.newText(new Coordinates(1200, 20), Ueberschrift, "Beschreibung",null, ueberschrift);
        Text author1 = lang.newText(new Coordinates(2100, 20), Dani, "Beschreibung",null, Author);
        Text author2 = lang.newText(new Coordinates(2100, 20), Deniz, "Beschreibung",null, Author);







        lang.nextStep();
        vorText.hide();
        InitStructure();


        lang.nextStep();

        // Text text0 = lang.newText(new Coordinates(30, 100), DESCRIPTION, "Beschreibung",null, tx);

        // Create Array: coordinates, data, name, display options,
        // default properties
        // start a new step after the array was created

        // Encryption

        SourceCode scE = lang.newSourceCode(new Coordinates(1000, 700), "sourceCodeENC",null, scProps);

        // F
        SourceCode scF = lang.newSourceCode(new Coordinates(1000, 300), "sourceCodeENC",null, scProps);

        scE.addCodeLine("public void public void encrypt(int L , int R )", null, 0,null); //
        scE.addCodeLine("{" , null, 0, null);
        scE.addCodeLine("for(int i = 1; i < 16 ; i+=2){", null, 1, null);
        scE.addCodeLine("L ^= P[i];" , null, 2, null); // 3
        scE.addCodeLine("R ^= f(L);", null, 2, null); // 4
        scE.addCodeLine("R ^= P[i+1];", null, 2, null); // 5
        scE.addCodeLine("L ^= f(R);", null, 2, null); // 6
        scE.addCodeLine("}", null, 1, null); // 7
        scE.addCodeLine("L ^= P[17];", null, 2, null); // 8
        scE.addCodeLine("R ^= P[18];", null, 2, null); // 9
        scE.addCodeLine("swap(L, R);", null, 2, null); // 10
        scE.addCodeLine("}", null, 1, null); // 11

        // F
        scF.addCodeLine("public int f(int x)", null, 0,null); //
        scF.addCodeLine("{" , null, 0, null);
        scF.addCodeLine("int h = S[1][x >> 24] + S[2][x >> 16 && 0xff]; ", null, 1, null);
        scF.addCodeLine("return ( h ^ S[3][x >> 8 && 0xff ] ) + S[4][x && 0xff ];" , null, 1, null); // 3
        scF.addCodeLine("}" , null, 0, null); // 4

        lang.nextStep();

        scE.highlight(0,0,false);





        lang.nextStep();

        ArrayProperties ArrayProps = new ArrayProperties();
        ArrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        ArrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        // arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Size", Font.PLAIN, 5));
        ArrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        ArrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        ArrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        ArrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
                Color.YELLOW);

        // now, create the IntArray object, linked to the properties
        StringArray lefti = lang.newStringArray(new Coordinates(20,110), L, "StringArray",
                null, ArrayProps);
        StringArray righti = lang.newStringArray(new Coordinates(700, 110), R, "StringArray",
                null, ArrayProps);

        /**
         * ungeklaert
         */
        TextProperties laeuferp = new TextProperties();
        laeuferp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        laeuferp.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 24));
        laeuferp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        laeuferp.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

        Text pointer = lang.newText(new Coordinates(1180, 125), laeufer, "Beschreibung",null, laeuferp);


        IntArray pBOXES = lang.newIntArray(new Coordinates(1200, 120), P, "StringArray",
                null, ArrayProps);


//   FRAGE OB DIES NÖTIG IST

        //lang.nextStep();

        // ARRAYS HIGHLIGHTEN
        //   lefti.highlightCell(0, lefti.getLength() - 1, null, null);
        //  righti.highlightCell(0, righti.getLength() - 1, null, null);

        try {
            // Start BlowENC
            BlowENC(lefti, righti, pBOXES, scE, scF);
        } catch (LineNotExistsException e) {
            e.printStackTrace();
        }

        lefti.hide();
        righti.hide();
        pBOXES.hide();
        scE.hide();
        scF.hide();
        lang.nextStep();
    }

    /**
     *
     */
    public void animateDecryption() {


        SourceCodeProperties scProps = new SourceCodeProperties();
        scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 40));
        scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        SourceCode vorText = lang.newSourceCode(new Coordinates(400, 300), "sourceCodeENC",null, scProps);

        vorText.addCodeLine("Blowfish ist ein symmetrisches Verschluesselungsverfahren, dass mit einer", null, 1,null); //
        vorText.addCodeLine("Blockverschluesselung arbeit. Es basiert auf einem Feistelnetzwerk und hat" , null, 1, null);
        vorText.addCodeLine("eine Blocklaenge von 64Bit und garantiert die Umkehrbarkeit zwischen Ver-  ", null, 1, null);
        vorText.addCodeLine("und Entschluesselung. " , null, 1, null); // 3
        vorText.addCodeLine("Naeheres zur Funktionsweise der Verschluesselung wird im Generator : ", null, 1, null); // 4
        vorText.addCodeLine("BlowfishEncryptionGenerator erlaeutert.  ", null, 1, null); // 4
        vorText.addCodeLine("Funktionsweise der Entschluesselung:", null, 1, null); // 4
        vorText.addCodeLine("Die Entschluesselung des BlowfishAlgorithmus laeuft genau", null, 5, null); // 5
        vorText.addCodeLine("gleich ab wie die Verschluesselung des Blowfishs, jedoch gibt es einen ", null, 5, null); // 6
        vorText.addCodeLine("wichtigen unterschied, denn die PBoxen werden in der Reihenfolge", null, 5, null); // 7
        vorText.addCodeLine("vertauscht. Dies garantiert auch die umkehrbarbeit der XOR  ", null, 5, null); // 8
        vorText.addCodeLine("Operation.", null, 5, null); // 9




        TextProperties Author = new TextProperties();
        Author.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        Author.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 24));
        Author.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        Author.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

        TextProperties ueberschrift = new TextProperties();
        ueberschrift.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        ueberschrift.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 55));
        ueberschrift.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        ueberschrift.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        String Ueberschrift = "                                 BLOWFISH DECRYPTION             ";
        String Dani = "                                 Dani El-Soufi         ";
        String Deniz = "                                Deniz Can Franz Ertan          ";
        Text text0 = lang.newText(new Coordinates(1200, 20), Ueberschrift, "Beschreibung",null, ueberschrift);
        Text author1 = lang.newText(new Coordinates(2100, 20), Dani, "Beschreibung",null, Author);
        Text author2 = lang.newText(new Coordinates(2100, 20), Deniz, "Beschreibung",null, Author);


        lang.nextStep();
        vorText.hide();
        InitStructureDEC();


        lang.nextStep();

        SourceCode scD = lang.newSourceCode(new Coordinates(1050, 700), "sourceCodeDEC",null, scProps);

        // F
        SourceCode scF = lang.newSourceCode(new Coordinates(1050,300), "sourceCodeDEC",null, scProps);

        scD.addCodeLine("public void public void encrypt(int L , int R )", null, 0,null); //
        scD.addCodeLine("{" , null, 0, null);
        scD.addCodeLine("for(int i =16 ; i > 1 ; i+=2){", null, 1, null);
        scD.addCodeLine("L ^= P[i];" , null, 2, null); // 3
        scD.addCodeLine("R ^= f(L);", null, 2, null); // 4
        scD.addCodeLine("R ^= P[i+1];", null, 2, null); // 5
        scD.addCodeLine("L ^= f(R);", null, 2, null); // 6
        scD.addCodeLine("}", null, 1, null); // 7
        scD.addCodeLine("L ^= P[1];", null, 2, null); // 8
        scD.addCodeLine("R ^= P[0];", null, 2, null); // 9
        scD.addCodeLine("swap(L, R);", null, 2, null); // 10
        scD.addCodeLine("}", null, 0, null); // 11

        // F
        scF.addCodeLine("public int f(int x)", null, 0,null); //
        scF.addCodeLine("{" , null, 0, null);
        scF.addCodeLine("int h = S[1][x >> 24] + S[2][x >> 16 && 0xff]; ", null, 1, null);
        scF.addCodeLine("return ( h ^ S[3][x >> 8 && 0xff ] ) + S[4][x && 0xff ];" , null, 1, null); // 3
        scF.addCodeLine("}" , null, 0, null); // 4

        lang.nextStep();

        scD.highlight(0,0,false);



        lang.nextStep();

        ArrayProperties ArrayProps = new ArrayProperties();
        ArrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        ArrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        // arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Size", Font.PLAIN, 5));
        ArrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        ArrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        ArrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        ArrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
                Color.YELLOW);

        // now, create the IntArray object, linked to the properties
        StringArray lefti = lang.newStringArray(new Coordinates(20,110), L, "StringArray",
                null, ArrayProps);
        StringArray righti = lang.newStringArray(new Coordinates(700, 110), R, "StringArray",
                null, ArrayProps);

        /**
         * ungeklaert
         *
         */

        TextProperties laeuferp = new TextProperties();
        laeuferp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        laeuferp.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 24));
        laeuferp.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        laeuferp.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);


        Text pointer = lang.newText(new Coordinates(1180, 125), laeufer, "Beschreibung",null, laeuferp);
        IntArray pBOXES = lang.newIntArray(new Coordinates(1200, 120), PD, "StringArray",
                null, ArrayProps);




        try {
            // Start BlowENC
            BlowDEC(lefti, righti, pBOXES, scD, scF);
        } catch (LineNotExistsException e) {
            e.printStackTrace();
        }

        lefti.hide();
        righti.hide();
        pBOXES.hide();
        scD.hide();
        scF.hide();
        lang.nextStep();


    }

    private int Pointer1 = 0;

    private void BlowENC(StringArray L, StringArray R, IntArray P, SourceCode codeENC, SourceCode codeF)throws LineNotExistsException {

        lang.nextStep();
        codeENC.toggleHighlight(0, 0, false, 2, 0);

        ArrayMarkerProperties pMarker = new ArrayMarkerProperties();
        pMarker.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
        pMarker.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        ArrayMarker iMarker = lang.newArrayMarker(P, 0, "i" + Pointer1,
                null, pMarker);

        int i = 0;
        P.highlightCell(0, 0, null, null);
        lang.nextStep();

        //P.hide();  ##########################  PBOXEN noch nicht hiden


        codeENC.toggleHighlight(2, 0, false, 3, 0);

        ArrayProperties ArrayProps = new ArrayProperties();
        ArrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        ArrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        // arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Size", Font.PLAIN, 5));
        ArrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        ArrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        ArrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        ArrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,Color.YELLOW);

        //codeENC.unhighlight(3, 0, false);


        // P[0]

        // ergebnis nach XOR !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //String[] p1x = {"0010 0100 0011 1111 0110 1010 1000 1000"};
        StringArray p1 = lang.newStringArray(new Coordinates(230, 160), pbox0, "StringArray",null, ArrayProps);
        lang.nextStep();

        // High 0
        L.highlightCell(0, L.getLength() - 1, null, null);
        p1.highlightCell(0, p1.getLength() - 1, null, null);
        lang.nextStep();
        //p1.hide();   ############################## DON'T HIGLHIGT pBox[1]
        //L.hide();

        L.unhighlightCell(0, L.getLength() - 1, null, null);
        p1.unhighlightCell(0, p1.getLength() - 1, null, null);
        // ERSTES X FueR F  L nach XOR L^P0
        //String[] L1x = {"1001 1010 0110 1011 0000 1100 0100 0111"};

        // ergebnis nach XOR !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        String[] hi0 = Util.intToStringArray(high[0]);
        StringArray L1 = lang.newStringArray(new Coordinates(20,310), hi0, "StringArray",null, ArrayProps);
        L1.highlightCell(0, L1.getLength() - 1, null, null);
        lang.nextStep();
        L1.unhighlightCell(0, L1.getLength() - 1, null, null);
        lang.nextStep();
        codeENC.toggleHighlight(3, 0, false, 4, 0);
        L1.highlightCell(0, L1.getLength() - 1, null, null);

        lang.nextStep();
        L1.unhighlightCell(0, L1.getLength() - 1, null, null);
        // ALLES HIDEN FueR F

        codeENC.hide();
        L.hide();
        L1.hide();
        p1.hide();
        R.hide();
        //hideBuildingENC();
        hideBuildingENC();
        codeF.highlight(0,0,false);
        codeF.hide();
        //P.hide();






        //############################################################# hier code fuer F Building

        initiateFCodeENC();

        // SOURCE DOCE FueR F EINBLENDEN IN GROss

        SourceCodeProperties scProps0 = new SourceCodeProperties();
        scProps0.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
        scProps0.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 35));
        scProps0.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        scProps0.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        SourceCode scF = lang.newSourceCode(new Coordinates(1300, 300), "sourceCodeENC",null, scProps0);

        scF.addCodeLine("public int f(int x)", null, 0,null); //
        scF.addCodeLine("{" , null, 0, null);
        scF.addCodeLine("int h = S[1][x >> 24] + S[2][x >> 16 && 0xff]; ", null, 1, null);
        scF.addCodeLine("return ( h ^ S[3][x >> 8 && 0xff ] ) + S[4][x && 0xff ];" , null, 1, null); // 3
        scF.addCodeLine("}" , null, 1, null); // 4

        scF.highlight(0,0,false);



        lang.nextStep();
        scF.toggleHighlight(0, 0, false, 2, 0);



        // /#############################################################################################


        // 154 107
        // ERGEBNIS NACH ERSTER + Operation = xx
        // String[] xx = {"sBox1 + sBox2"};
        // StringArray x = lang.newStringArray(new Coordinates(450, 100), xx, "StringArray",null, ArrayProps);
        // 1110 0010 0111 0010 0100 1001
        lang.nextStep();
        // yy Ist das ergebnis Hier REIN
        String[] h0 = Util.intToStringArray((sboxHi[0] + sboxHi[1]));

        //String[] yy = {"0011 1000 1001 1100 1001 0010 1001 1100"};
        StringArray y = lang.newStringArray(new Coordinates(100, 630), h0, "StringArray",null, ArrayProps);
        y.highlightCell(0, y.getLength() - 1, null, null);
        lang.nextStep();
        // x.hide();
        // StringArray yx = lang.newStringArray(new Coordinates(450, 100), h0, "StringArray",null, ArrayProps);
        //yx.highlightCell(0, yx.getLength() - 1, null, null);
        //y.hide();
        y.unhighlightCell(0, y.getLength() - 1, null, null);

        lang.nextStep();
        //  yx.hide();
        scF.toggleHighlight(2, 0, false, 3, 0);

        // String[] aa = {"h ^ sBox3"};
        //StringArray a = lang.newStringArray(new Coordinates(450, 150), aa, "StringArray",null, ArrayProps);
        lang.nextStep();

        // ERGEBNIS aus h^ s3
        String[] ergebnis0 = Util.intToStringArray(((sboxHi[0] +( sboxHi[1]) ^ sboxHi[2])));
        //String[] temp = {"0011 1010 1000 0100 1011 01110 1000 Z001"};
        StringArray tempa = lang.newStringArray(new Coordinates(390,890), ergebnis0, "StringArray",null, ArrayProps);
        tempa.highlightCell(0, tempa.getLength() - 1, null, null);

        lang.nextStep();
        //tempa.hide();
        tempa.unhighlightCell(0, tempa.getLength() - 1, null, null);
        //a.hide();

        //String[] temp3 = { ((sboxHi[0] + sboxHi[1]) ^ sboxHi[2]) + " sBox4"};
        //StringArray tempa3 = lang.newStringArray(new Coordinates(450, 150), temp3, "StringArray",null, ArrayProps);
        lang.nextStep();
        //Ergebnis der ersten F runde

        //Endergebnis aus F

        //String[] temp1 = {"1110 1110 0001 1100 1001 1100 1010 1001"};
        String[] temp1 = Util.intToStringArray(((sboxHi[0] + sboxHi[1]) ^ sboxHi[2])+ sboxHi[3]);
        StringArray tempa1 = lang.newStringArray(new Coordinates(760,1100), temp1, "StringArray",null, ArrayProps);
        tempa1.highlightCell(0, tempa1.getLength() - 1, null, null);
        lang.nextStep();
        tempa1.unhighlightCell(0, tempa1.getLength() - 1, null, null);
        //R.highlightCell(0, R.getLength() - 1, null, null);
        //###################################################################################
        hideF();
        scF.hide();
        showENC();
        tempa1.hide();
        tempa.hide();
        y.hide();

        // building rebuild
        codeENC.show();
        L.show();
        L1.show();
        p1.show();
        R.show();
        codeF.show();
        codeF.toggleHighlight(0,0, true ,0,0);
        //###################################### umschwung

        // alles hide code f und show enc wieder dann weiter
        //------------- erstes F fertig
        lang.nextStep();
        //R.hide();
        // DAS IST R



        //##############POLYLINE
        // output aus f
        //String[] temp2 = {"1110 1110 0001 1100 1001 1100 1010 1001"};
        StringArray tempa2 = lang.newStringArray(new Coordinates(150, 540), temp1, "StringArray",null, ArrayProps);
        tempa2.highlightCell(0, tempa2.getLength() - 1, null, null);
        R.highlightCell(0, R.getLength() - 1, null, null);
        lang.nextStep();

        String[] tempx = Util.intToStringArray(low[0]);
        StringArray tempa3 = lang.newStringArray(new Coordinates(500, 480), tempx, "StringArray",null, ArrayProps);
        tempa3.highlightCell(0, tempa2.getLength() - 1, null, null);

        lang.nextStep();

        tempa3.unhighlightCell(0, tempa2.getLength() - 1, null, null);

        lang.nextStep();
        codeENC.toggleHighlight(4, 0, false, 5, 0);

        // Text
        TextProperties tx0 = new TextProperties();
        tx0.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 33));
        tx0.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
        tx0.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        tx0.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        weiter.hide();
        weiterplus.hide();
        weiterRED = lang.newText(new Coordinates(400,660),weit,"FunktionNameUnten",null,tx0);
        weiterplusRED = lang.newText(new Coordinates(440,730),weitplus,"FunktionNameUnten",null,tx0);



        lang.nextStep();
        codeENC.toggleHighlight(5, 0, false, 6, 0);
        lang.nextStep();
        codeENC.toggleHighlight(6, 0, false, 7, 0);
        lang.nextStep();
        weiterRED.hide();
        weiter.show();
        weiterplus.show();
        weiterplusRED.hide();



        P.unhighlightCell(0, 0, null, null);
        P.highlightCell(15, 15, null, null);
        iMarker.hide();
        ArrayMarker iEndMarker = lang.newArrayMarker(P, 15, "i" + Pointer1,
                null, pMarker);
        /**
         *
         *
         * Jetzt Code Bild aus animal , das der algorithmus nun bis i 16 ist immer das gleiche macht ...
         *
         *
         * Sobald i = 17 ist
         *
         */

        //String[] Lende= {"1111 1111 1111 1111 1111 1111 1111 1111"};
        String[] Lende = Util.intToStringArray(low[2]);
        StringArray Lfin= lang.newStringArray(new Coordinates(15,880), Lende, "StringArray",null, ArrayProps);


        //String[] Rende= {"1111 1111 1111 1111 1111 1111 1111 1111"};
        String[] Rende = Util.intToStringArray(high[2]);
        StringArray Rfin= lang.newStringArray(new Coordinates(630,840), Rende, "StringArray",null, ArrayProps);

        lang.nextStep();
        codeENC.toggleHighlight(7, 0, false, 8, 0);


        Rfin.highlightCell(0, Lfin.getLength() - 1, null, null);

        //String[] P17= {"1001 0010 0001 0110 1101 0101 1101 1001"};
        StringArray Pvorletzt= lang.newStringArray(new Coordinates(410, 890), Util.intToStringArray(pbox16), "StringArray",null, ArrayProps);
        Pvorletzt.highlightCell(0, Pvorletzt.getLength() - 1, null, null);



        lang.nextStep();

        /**
         * XOR VON P17 UND L
         */
        Rfin.unhighlightCell(0, Lfin.getLength() - 1, null, null);
        Pvorletzt.unhighlightCell(0, Pvorletzt.getLength() - 1, null, null);
        String[] finalL = Util.intToStringArray((pbox16 ^ high[2]));
        //String[] finalL= {"Ergebnis aus XOR von P17 und L"};
        StringArray endeL= lang.newStringArray(new Coordinates(650, 1150), finalL, "StringArray",null, ArrayProps);
        endeL.highlightCell(0, endeL.getLength() - 1, null, null);

        lang.nextStep();
        endeL.unhighlightCell(0,endeL.getLength()-1,null,null);
        codeENC.toggleHighlight(8, 0, false, 9, 0);


        Lfin.highlightCell(0, Rfin.getLength() - 1, null, null);

        //String[] P18= {"1000 1001 0111 1001 1111 1011 0001 1011"};
        StringArray Pvorletzt0= lang.newStringArray(new Coordinates(230, 1090), Util.intToStringArray(pbox17), "StringArray",null, ArrayProps);
        Pvorletzt0.highlightCell(0, Pvorletzt0.getLength() - 1, null, null);


        lang.nextStep();
        Lfin.unhighlightCell(0, Rfin.getLength() - 1, null, null);
        /**
         * XOR VON P18 UND R
         */
        Rfin.unhighlightCell(0, Rfin.getLength() - 1, null, null);
        Pvorletzt0.unhighlightCell(0, Pvorletzt0.getLength() - 1, null, null);
        String[] finalR = Util.intToStringArray((pbox17 ^ low[2]));
        //String[] finalR= {"Ergebnis aus XOR von P18 und R"};
        StringArray endeR= lang.newStringArray(new Coordinates(20, 1150), finalR, "StringArray",null, ArrayProps);
        endeR.highlightCell(0, endeR.getLength() - 1, null, null);




        lang.nextStep();
        //Pvorletzt0.hide();
        endeR.unhighlightCell(0, endeR.getLength() - 1, null, null);
        //endeR.hide();
        // Rfin.hide();
        // lang.nextStep();

        lang.nextStep();

        // Text
        TextProperties txx = new TextProperties();
        txx.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 35));
        txx.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        txx.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        txx.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

        swapx = lang.newText(new Coordinates(500, 1220),swap,"swap funktion",null,txx);


        lang.nextStep();
        endeL.hide();
        endeR.hide();
        //fertige1.hide();


        codeENC.toggleHighlight(9, 0, false, 10, 0);

        //String[] fertig1r1= {"Ergebnis aus XOR von P18 und R"};

        StringArray endeRswap= lang.newStringArray(new Coordinates(650, 1150), finalR, "StringArray",null, ArrayProps);
        endeRswap.highlightCell(0, endeRswap.getLength() - 1, null, null);


        //String[] fertig11= {"Ergebnis aus XOR von P17 und L"};
        StringArray endeLSwap= lang.newStringArray(new Coordinates(20, 1150), finalL, "StringArray",null, ArrayProps);
        endeLSwap.highlightCell(0, endeLSwap.getLength() - 1, null, null);


        lang.nextStep();

        lang.hideAllPrimitives();


    }

    private void BlowDEC(StringArray L, StringArray R, IntArray P, SourceCode codeDEC, SourceCode codeF)throws LineNotExistsException {


        lang.nextStep();
        codeDEC.toggleHighlight(0, 0, false, 2, 0);

        ArrayMarkerProperties pMarker = new ArrayMarkerProperties();
        pMarker.set(AnimationPropertiesKeys.LABEL_PROPERTY, "i");
        pMarker.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        ArrayMarker iMarker = lang.newArrayMarker(P, 0, "i" + Pointer1,
                null, pMarker);

        int i = 0;
        P.highlightCell(0, 0, null, null);
        lang.nextStep();

        //P.hide();  ##########################  PBOXEN noch nicht hiden


        codeDEC.toggleHighlight(2, 0, false, 3, 0);

        ArrayProperties ArrayProps = new ArrayProperties();
        ArrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        ArrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        // arrayProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Size", Font.PLAIN, 5));
        ArrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        ArrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        ArrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        ArrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,Color.YELLOW);

        //codeDEC.unhighlight(3, 0, false);


        // P[0]

        // ergebnis nach XOR !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //String[] p1x = {"0010 0100 0011 1111 0110 1010 1000 1000"};
        StringArray p1 = lang.newStringArray(new Coordinates(230, 160), pbox0, "StringArray",null, ArrayProps);
        lang.nextStep();

        // High 0
        L.highlightCell(0, L.getLength() - 1, null, null);
        p1.highlightCell(0, p1.getLength() - 1, null, null);
        lang.nextStep();
        //p1.hide();   ############################## DON'T HIGLHIGT pBox[1]
        //L.hide();

        L.unhighlightCell(0, L.getLength() - 1, null, null);
        p1.unhighlightCell(0, p1.getLength() - 1, null, null);
        // ERSTES X FueR F  L nach XOR L^P0
        //String[] L1x = {"1001 1010 0110 1011 0000 1100 0100 0111"};

        // ergebnis nach XOR !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        String[] hi0 = Util.intToStringArray(high[0]);
        StringArray L1 = lang.newStringArray(new Coordinates(20,310), hi0, "StringArray",null, ArrayProps);
        L1.highlightCell(0, L1.getLength() - 1, null, null);
        lang.nextStep();
        L1.unhighlightCell(0, L1.getLength() - 1, null, null);
        lang.nextStep();
        codeDEC.toggleHighlight(3, 0, false, 4, 0);
        L1.highlightCell(0, L1.getLength() - 1, null, null);

        lang.nextStep();
        L1.unhighlightCell(0, L1.getLength() - 1, null, null);
        // ALLES HIDEN FueR F

        codeDEC.hide();
        L.hide();
        L1.hide();
        p1.hide();
        R.hide();
        //hideBuildingENC();
        hideBuildingENC();
        codeF.highlight(0,0,false);
        codeF.hide();
        //P.hide();






        //############################################################# hier code fuer F Building

        initiateFCodeENC();

        // SOURCE DOCE FueR F EINBLENDEN IN GROss

        SourceCodeProperties scProps0 = new SourceCodeProperties();
        scProps0.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLACK);
        scProps0.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 35));
        scProps0.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        scProps0.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);

        SourceCode scF = lang.newSourceCode(new Coordinates(1300, 300), "sourceCodeENC",null, scProps0);

        scF.addCodeLine("public int f(int x)", null, 0,null); //
        scF.addCodeLine("{" , null, 0, null);
        scF.addCodeLine("int h = S[1][x >> 24] + S[2][x >> 16 && 0xff]; ", null, 1, null);
        scF.addCodeLine("return ( h ^ S[3][x >> 8 && 0xff ] ) + S[4][x && 0xff ];" , null, 1, null); // 3
        scF.addCodeLine("}" , null, 1, null); // 4

        scF.highlight(0,0,false);



        lang.nextStep();
        scF.toggleHighlight(0, 0, false, 2, 0);



        // /#############################################################################################


        // 154 107
        // ERGEBNIS NACH ERSTER + Operation = xx
        // String[] xx = {"sBox1 + sBox2"};
        // StringArray x = lang.newStringArray(new Coordinates(450, 100), xx, "StringArray",null, ArrayProps);
        // 1110 0010 0111 0010 0100 1001
        lang.nextStep();
        // yy Ist das ergebnis Hier REIN
        String[] h0 = Util.intToStringArray((sboxHi[0] + sboxHi[1]));

        //String[] yy = {"0011 1000 1001 1100 1001 0010 1001 1100"};
        StringArray y = lang.newStringArray(new Coordinates(100, 630), h0, "StringArray",null, ArrayProps);
        y.highlightCell(0, y.getLength() - 1, null, null);
        lang.nextStep();
        // x.hide();
        // StringArray yx = lang.newStringArray(new Coordinates(450, 100), h0, "StringArray",null, ArrayProps);
        //yx.highlightCell(0, yx.getLength() - 1, null, null);
        //y.hide();
        y.unhighlightCell(0, y.getLength() - 1, null, null);

        lang.nextStep();
        //  yx.hide();
        scF.toggleHighlight(2, 0, false, 3, 0);

        // String[] aa = {"h ^ sBox3"};
        //StringArray a = lang.newStringArray(new Coordinates(450, 150), aa, "StringArray",null, ArrayProps);
        lang.nextStep();

        // ERGEBNIS aus h^ s3
        String[] ergebnis0 = Util.intToStringArray(((sboxHi[0] +( sboxHi[1]) ^ sboxHi[2])));
        //String[] temp = {"0011 1010 1000 0100 1011 01110 1000 Z001"};
        StringArray tempa = lang.newStringArray(new Coordinates(390,890), ergebnis0, "StringArray",null, ArrayProps);
        tempa.highlightCell(0, tempa.getLength() - 1, null, null);

        lang.nextStep();
        //tempa.hide();
        tempa.unhighlightCell(0, tempa.getLength() - 1, null, null);
        //a.hide();

        //String[] temp3 = { ((sboxHi[0] + sboxHi[1]) ^ sboxHi[2]) + " sBox4"};
        //StringArray tempa3 = lang.newStringArray(new Coordinates(450, 150), temp3, "StringArray",null, ArrayProps);
        lang.nextStep();
        //Ergebnis der ersten F runde

        //Endergebnis aus F

        //String[] temp1 = {"1110 1110 0001 1100 1001 1100 1010 1001"};
        String[] temp1 = Util.intToStringArray(((sboxHi[0] + sboxHi[1]) ^ sboxHi[2])+ sboxHi[3]);
        StringArray tempa1 = lang.newStringArray(new Coordinates(760,1100), temp1, "StringArray",null, ArrayProps);
        tempa1.highlightCell(0, tempa1.getLength() - 1, null, null);
        lang.nextStep();
        tempa1.unhighlightCell(0, tempa1.getLength() - 1, null, null);
        //R.highlightCell(0, R.getLength() - 1, null, null);
        //###################################################################################
        hideF();
        scF.hide();
        showENC();
        tempa1.hide();
        tempa.hide();
        y.hide();

        // building rebuild
        codeDEC.show();
        L.show();
        L1.show();
        p1.show();
        R.show();
        codeF.show();
        codeF.toggleHighlight(0,0, true ,0,0);
        //###################################### umschwung

        // alles hide code f und show enc wieder dann weiter
        //------------- erstes F fertig
        lang.nextStep();
        //R.hide();
        // DAS IST R



        //##############POLYLINE
        // output aus f
        //String[] temp2 = {"1110 1110 0001 1100 1001 1100 1010 1001"};
        StringArray tempa2 = lang.newStringArray(new Coordinates(150, 540), temp1, "StringArray",null, ArrayProps);
        tempa2.highlightCell(0, tempa2.getLength() - 1, null, null);
        R.highlightCell(0, R.getLength() - 1, null, null);
        lang.nextStep();

        String[] tempx = Util.intToStringArray(low[0]);
        StringArray tempa3 = lang.newStringArray(new Coordinates(500, 480), tempx, "StringArray",null, ArrayProps);
        tempa3.highlightCell(0, tempa2.getLength() - 1, null, null);

        lang.nextStep();

        tempa3.unhighlightCell(0, tempa2.getLength() - 1, null, null);

        lang.nextStep();
        codeDEC.toggleHighlight(4, 0, false, 5, 0);

        // Text
        TextProperties tx0 = new TextProperties();
        tx0.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 33));
        tx0.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);
        tx0.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        tx0.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        weiter.hide();
        weiterplus.hide();

        weiterRED = lang.newText(new Coordinates(400,680),weit,"FunktionNameUnten",null,tx0);
        weiterplusRED = lang.newText(new Coordinates(440,710),weitplus,"FunktionNameUnten",null,tx0);


        lang.nextStep();
        codeDEC.toggleHighlight(5, 0, false, 6, 0);
        lang.nextStep();
        codeDEC.toggleHighlight(6, 0, false, 7, 0);
        lang.nextStep();
        weiterRED.hide();
        weiterplus.show();
        weiter.show();
        weiterplusRED.hide();

        P.unhighlightCell(0, 0, null, null);
        P.highlightCell(15, 15, null, null);
        iMarker.hide();
        ArrayMarker iEndMarker = lang.newArrayMarker(P, 15, "i" + Pointer1,
                null, pMarker);
        /**
         *
         *
         * Jetzt Code Bild aus animal , das der algorithmus nun bis i 16 ist immer das gleiche macht ...
         *
         *
         * Sobald i = 17 ist
         *
         */

        //String[] Lende= {"1111 1111 1111 1111 1111 1111 1111 1111"};
        String[] Lende = Util.intToStringArray(low[2]);
        StringArray Lfin= lang.newStringArray(new Coordinates(15,880), Lende, "StringArray",null, ArrayProps);


        //String[] Rende= {"1111 1111 1111 1111 1111 1111 1111 1111"};
        String[] Rende = Util.intToStringArray(high[2]);
        StringArray Rfin= lang.newStringArray(new Coordinates(630,840), Rende, "StringArray",null, ArrayProps);

        lang.nextStep();
        codeDEC.toggleHighlight(7, 0, false, 8, 0);


        Rfin.highlightCell(0, Lfin.getLength() - 1, null, null);

        //String[] P17= {"1001 0010 0001 0110 1101 0101 1101 1001"};
        StringArray Pvorletzt= lang.newStringArray(new Coordinates(410, 890), Util.intToStringArray(pbox16), "StringArray",null, ArrayProps);
        Pvorletzt.highlightCell(0, Pvorletzt.getLength() - 1, null, null);



        lang.nextStep();

        /**
         * XOR VON P17 UND L
         */
        Rfin.unhighlightCell(0, Lfin.getLength() - 1, null, null);
        Pvorletzt.unhighlightCell(0, Pvorletzt.getLength() - 1, null, null);
        String[] finalL = Util.intToStringArray((pbox16 ^ high[2]));
        //String[] finalL= {"Ergebnis aus XOR von P17 und L"};
        StringArray endeL= lang.newStringArray(new Coordinates(650, 1150), finalL, "StringArray",null, ArrayProps);
        endeL.highlightCell(0, endeL.getLength() - 1, null, null);

        lang.nextStep();
        endeL.unhighlightCell(0,endeL.getLength()-1,null,null);
        codeDEC.toggleHighlight(8, 0, false, 9, 0);


        Lfin.highlightCell(0, Rfin.getLength() - 1, null, null);

        //String[] P18= {"1000 1001 0111 1001 1111 1011 0001 1011"};
        StringArray Pvorletzt0= lang.newStringArray(new Coordinates(230, 1090), Util.intToStringArray(pbox17), "StringArray",null, ArrayProps);
        Pvorletzt0.highlightCell(0, Pvorletzt0.getLength() - 1, null, null);


        lang.nextStep();
        Lfin.unhighlightCell(0, Rfin.getLength() - 1, null, null);
        /**
         * XOR VON P18 UND R
         */
        Rfin.unhighlightCell(0, Rfin.getLength() - 1, null, null);
        Pvorletzt0.unhighlightCell(0, Pvorletzt0.getLength() - 1, null, null);
        String[] finalR = Util.intToStringArray((pbox17 ^ low[2]));
        //String[] finalR= {"Ergebnis aus XOR von P18 und R"};
        StringArray endeR= lang.newStringArray(new Coordinates(20, 1150), finalR, "StringArray",null, ArrayProps);
        endeR.highlightCell(0, endeR.getLength() - 1, null, null);




        lang.nextStep();
        //Pvorletzt0.hide();
        endeR.unhighlightCell(0, endeR.getLength() - 1, null, null);
        //endeR.hide();
        // Rfin.hide();
        // lang.nextStep();

        lang.nextStep();

        // Text
        TextProperties txx = new TextProperties();
        txx.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 35));
        txx.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        txx.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        txx.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

        swapx = lang.newText(new Coordinates(500, 1220),swap,"swap funktion",null,txx);


        lang.nextStep();
        endeL.hide();
        endeR.hide();
        //fertige1.hide();


        codeDEC.toggleHighlight(9, 0, false, 10, 0);

        //String[] fertig1r1= {"Ergebnis aus XOR von P18 und R"};

        StringArray endeRswap= lang.newStringArray(new Coordinates(650, 1150), finalR, "StringArray",null, ArrayProps);
        endeRswap.highlightCell(0, endeRswap.getLength() - 1, null, null);


        //String[] fertig11= {"Ergebnis aus XOR von P17 und L"};
        StringArray endeLSwap= lang.newStringArray(new Coordinates(20, 1150), finalL, "StringArray",null, ArrayProps);
        endeLSwap.highlightCell(0, endeLSwap.getLength() - 1, null, null);

        lang.nextStep();

        lang.hideAllPrimitives();


    }
    private boolean ready = false;

    /**
     * Called when encryption/decryption method has been called.
     */
    public void ready() {
        ready = true;
    }

    private String[] L;
    private String[] R;
    private int cText = 0;

    /**
     * Plain text as high and low int values.
     *
     * @param hi 32 higher bits
     * @param lo 32 lower bits
     */
    public void text(int hi, int lo) {
        if (!ready) {
            return;
        }

        if (cText == 1) {
            return;
        }

        L = Util.intToStringArray(hi);
        R = Util.intToStringArray(lo);
        cText++;
    }

    private int cLo = 0;
    private int[] low = new int[4];

    /**
     * Low value.
     *
     * @param lo 32 lower bits
     */
    public void lo(int lo) {
        if (!ready) {
            return;
        }

        if (cLo == low.length) {
            return;
        }

        low[cLo++] = lo;
    }

    private int cHi = 0;
    private int[] high = new int[5];

    /**
     * High value.
     *
     * @param hi 32 higher bits
     */
    public void hi(int hi) {
        if (!ready) {
            return;
        }

        if (cHi == high.length) {
            return;
        }

        high[cHi++] = hi;

    }

    /**
     * Cipher text as high and low int values.
     *
     * @param hi 32 higher bits
     * @param lo 32 lower bits
     */
    public void cipher(int hi, int lo) {
        // is not implemented hier, since it's encryption ;-)
    }

    private String[] pbox0;
    private String[] pbox1;
    private int pbox16;
    private int pbox17;
    private int cPBox = 0;

    /**
     * P-Box value at specific position.
     *
     * @param val Value
     * @param pos Position
     */
    public void pbox(int val, int pos) {
        if (!ready) {
            return;
        }

        if (cPBox == 4) {
            return;
        }

        if (pos == 0) {
            pbox0 = Util.intToStringArray(val);
            cPBox++;
        } else if (pos == 1) {
            pbox1 = Util.intToStringArray(val);
            cPBox++;
        } else if (pos == 16) {
            pbox16 = val;
            cPBox++;
        } else if (pos == 17) {
            pbox17 = val;
            cPBox++;
        }
    }

    private int cF = 0;
    private int[] f = new int[2];

    /**
     * Result of the F-Function of the algorithm for high or low result.
     *
     * @param result Result of the F-Function
     * @param val 0 for low and 1 for high
     */
    public void f(int result, int val) {
        if (!ready) {
            return;
        }

        if (cF == f.length) {
            return;
        }

        f[cF++] = result;

    }

    private int cSBoxLo = 0;
    private int cSBoxHi = 0;
    private int[] sboxLo = new int[4];
    private int[] sboxHi = new int[4];

    /**
     * S-Box value for high or low result.
     *
     * @param result
     *                  Result of sbox
     * @param val
     *                  0 for low and 1 for high
     */
    public void sbox(int result, int val) {
        if (!ready) {
            return;
        }

        if ((cSBoxHi + cSBoxLo) == (sboxHi.length + sboxLo.length)) {
            return;
        }

        if (val == 0) {
            sboxLo[cSBoxLo++] = result;
        } else if (val == 1) {
            sboxHi[cSBoxHi++] = result;
        }
    }

    public void InitStructure(){


        // Polyline
        PolylineProperties polP = new PolylineProperties();
        // ##########################################################################################################
        polP.set(AnimationPropertiesKeys.FWARROW_PROPERTY,true);
        polP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        polP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 16);

        //Circle
        CircleProperties cercp = new CircleProperties();
        cercp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        cercp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 16);

        TextProperties tx2 = new TextProperties();
        tx2.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 22));
        tx2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        tx2.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        tx2.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

        TextProperties txSpecial = new TextProperties();
        txSpecial.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 50));
        txSpecial.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        txSpecial.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        txSpecial.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

        // Text
        TextProperties tx0 = new TextProperties();
        tx0.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 33));
        tx0.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        tx0.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        tx0.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);




        Node[] nA0 = new Node[4];
        Node[] nA1 = new Node[3];
        Node[] nA2 = new Node[2];
        Node[] nA3 = new Node[3];
        Node[] nA4 = new Node[3];
        Node[] nA5 = new Node[2];
        Node[] nA6 = new Node[2];
        Node[] nA7 = new Node[2];
        Node[] nA8= new Node[4];
        Node[] nA9= new Node[4];
        Node[] nA10 = new Node[2];
        Node[] nA11 = new Node[3];
        Node[] nA12 = new Node[3];
        Node[] nA13 = new Node[2];
        Node[] nA14 = new Node[2];
        Node[] nA15 = new Node[2];




        nA0[0] = new Coordinates(410,30);
        nA0[1] = new Coordinates(410,70);
        nA0[2] = new Coordinates(40,70);
        nA0[3] = new Coordinates(40,100);

        nA8[0] = new Coordinates(40,410);
        nA8[1] = new Coordinates(40,590);
        nA8[2] = new Coordinates(760,640);
        nA8[3] = new Coordinates(760,670);

        nA9[0] = new Coordinates(760,500);
        nA9[1] = new Coordinates(760,590);
        nA9[2] = new Coordinates(40,630);
        nA9[3] = new Coordinates(40,670);




        nA2[0] = new Coordinates(40,130);
        nA2[1] = new Coordinates(40,190);


        nA4[0] = new Coordinates(330,170) ;
        nA4[1] = new Coordinates(330,230);
        nA4[2] = new Coordinates(80,230);

        nA3[0] = new Coordinates(410,70) ;
        nA3[1] = new Coordinates(770,70);
        nA3[2] = new Coordinates(770,100);

        nA1[0] = new Coordinates(40,320) ;
        nA1[1] = new Coordinates(40,410);
        nA1[2] = new Coordinates(340,410);

        nA5[0] = new Coordinates(420,500);
        nA5[1] = new Coordinates(420,530);

        nA6[0] = new Coordinates(490,410);
        nA6[1] = new Coordinates(710,410);

        nA7[0] = new Coordinates(770,130);
        nA7[1] = new Coordinates(770,360);


        nA10[0] = new Coordinates(760,850);
        nA10[1] = new Coordinates(760,1000);


        nA11[0] = new Coordinates(330,1080) ;
        nA11[1] = new Coordinates(330,1030);
        nA11[2] = new Coordinates(90,1030);

        nA12[0] = new Coordinates(490,900) ;
        nA12[1] = new Coordinates(490,1050);
        nA12[2] = new Coordinates(710,1050);

        nA13[0] = new Coordinates(50,890);
        nA13[1] = new Coordinates(50,1000);

        nA14[0] = new Coordinates(20, 1150);
        nA14[1] = new Coordinates(730, 1300);

        nA15[0] = new Coordinates(650, 1150);
        nA15[1] = new Coordinates(20, 1300);















        Xors = lang.newText(new Coordinates(768,365),xor,"FunktionNameUnten",null,tx2);
        SchriftP1 = lang.newText(new Coordinates(350, 190),B1,"FunktionNameOben",null,tx2);
        Xors3 = lang.newText(new Coordinates(50,210),xor,"FunktionNameUnten",null,tx2);
        ersteF = lang.newText(new Coordinates(422, 413),F,"FunktionNameUnten",null,txSpecial);
        weiter = lang.newText(new Coordinates(400,660),weit,"FunktionNameUnten",null,tx0);
        weiterplus = lang.newText(new Coordinates(440,730),weitplus,"FunktionNameUnten",null,tx0);
        Xorsx = lang.newText(new Coordinates(50,1020),xor,"FunktionNameUnten",null,tx2);
        Xorsxx = lang.newText(new Coordinates(760,1030),xor,"FunktionNameUnten",null,tx2);

        Left = lang.newText(new Coordinates(60,40),links,"FunktionNameUnten",null,tx2);
        Right = lang.newText(new Coordinates(710, 40),rechts,"FunktionNameUnten",null,tx2);

        SchriftP17 = lang.newText(new Coordinates(440, 910),p71,"FunktionNameUnten",null,tx2);
        SchriftP18 = lang.newText(new Coordinates(250, 1050),p81,"FunktionNameUnten",null,tx2);



        circ2 = lang.newCircle(new Coordinates(50,1050),40,"circ5",null,cercp);
        circ3 = lang.newCircle(new Coordinates(760,1050),40,"circ5",null,cercp);
        circ0 = lang.newCircle(new Coordinates(40,230),40,"circ5",null,cercp);
        circ1 = lang.newCircle(new Coordinates(760, 410),40,"circ5",null,cercp);

        pol3 = lang.newPolyline(nA3,"pol1",null,polP);
        pol5 = lang.newPolyline(nA5,"pol1",null,polP);
        pol2 = lang.newPolyline(nA2,"pol1",null,polP);
        pol0 = lang.newPolyline(nA0,"pol0",null,polP);
        pol1 = lang.newPolyline(nA4,"pol0",null,polP);
        polx = lang.newPolyline(nA1,"pol0",null,polP);
        pol4 = lang.newPolyline(nA6,"pol1",null,polP);
        pol6 = lang.newPolyline(nA7,"pol1",null,polP);
        pol7 = lang.newPolyline(nA8,"pol1",null,polP);
        pol8 = lang.newPolyline(nA9,"pol1",null,polP);
        pol9 = lang.newPolyline(nA10,"pol1",null,polP);
        pol10 = lang.newPolyline(nA11,"pol1",null,polP);
        pol11 = lang.newPolyline(nA12,"pol1",null,polP);
        pol12 = lang.newPolyline(nA13,"pol1",null,polP);








        // SQUARE
        SquareProperties sqp = new SquareProperties();
        sqp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        sqp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
        sq0 = lang.newSquare(new Coordinates(360,370),120,"sq0",null,sqp);


    }

    public void InitStructureDEC() {


        // Polyline
        PolylineProperties polP = new PolylineProperties();
        // ##########################################################################################################
        polP.set(AnimationPropertiesKeys.FWARROW_PROPERTY, true);
        polP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        polP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 16);

        //Circle
        CircleProperties cercp = new CircleProperties();
        cercp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        cercp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 16);

        TextProperties tx2 = new TextProperties();
        tx2.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 22));
        tx2.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        tx2.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        tx2.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

        TextProperties txSpecial = new TextProperties();
        txSpecial.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 50));
        txSpecial.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        txSpecial.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        txSpecial.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);

        // Text
        TextProperties tx0 = new TextProperties();
        tx0.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font("Monospaced", Font.PLAIN, 33));
        tx0.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        tx0.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        tx0.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);


        Node[] nA0 = new Node[4];
        Node[] nA1 = new Node[3];
        Node[] nA2 = new Node[2];
        Node[] nA3 = new Node[3];
        Node[] nA4 = new Node[3];
        Node[] nA5 = new Node[2];
        Node[] nA6 = new Node[2];
        Node[] nA7 = new Node[2];
        Node[] nA8 = new Node[4];
        Node[] nA9 = new Node[4];
        Node[] nA10 = new Node[2];
        Node[] nA11 = new Node[3];
        Node[] nA12 = new Node[3];
        Node[] nA13 = new Node[2];
        Node[] nA14 = new Node[2];
        Node[] nA15 = new Node[2];


        nA0[0] = new Coordinates(410, 30);
        nA0[1] = new Coordinates(410, 70);
        nA0[2] = new Coordinates(40, 70);
        nA0[3] = new Coordinates(40, 100);

        nA8[0] = new Coordinates(40, 410);
        nA8[1] = new Coordinates(40, 590);
        nA8[2] = new Coordinates(760, 640);
        nA8[3] = new Coordinates(760, 670);

        nA9[0] = new Coordinates(760, 500);
        nA9[1] = new Coordinates(760, 590);
        nA9[2] = new Coordinates(40, 630);
        nA9[3] = new Coordinates(40, 670);


        nA2[0] = new Coordinates(40, 130);
        nA2[1] = new Coordinates(40, 190);


        nA4[0] = new Coordinates(330, 170);
        nA4[1] = new Coordinates(330, 230);
        nA4[2] = new Coordinates(80, 230);

        nA3[0] = new Coordinates(410, 70);
        nA3[1] = new Coordinates(770, 70);
        nA3[2] = new Coordinates(770, 100);

        nA1[0] = new Coordinates(40, 320);
        nA1[1] = new Coordinates(40, 410);
        nA1[2] = new Coordinates(340, 410);

        nA5[0] = new Coordinates(420, 500);
        nA5[1] = new Coordinates(420, 530);

        nA6[0] = new Coordinates(490, 410);
        nA6[1] = new Coordinates(710, 410);

        nA7[0] = new Coordinates(770, 130);
        nA7[1] = new Coordinates(770, 360);


        nA10[0] = new Coordinates(760, 850);
        nA10[1] = new Coordinates(760, 1000);


        nA11[0] = new Coordinates(330, 1080);
        nA11[1] = new Coordinates(330, 1030);
        nA11[2] = new Coordinates(90, 1030);

        nA12[0] = new Coordinates(490, 900);
        nA12[1] = new Coordinates(490, 1050);
        nA12[2] = new Coordinates(710, 1050);

        nA13[0] = new Coordinates(50, 890);
        nA13[1] = new Coordinates(50, 1000);

        nA14[0] = new Coordinates(20, 1150);
        nA14[1] = new Coordinates(730, 1300);

        nA15[0] = new Coordinates(650, 1150);
        nA15[1] = new Coordinates(20, 1300);


        Xors = lang.newText(new Coordinates(768,365), xor, "FunktionNameUnten", null, tx2);
        SchriftP1 = lang.newText(new Coordinates(350, 190), B18, "FunktionNameOben", null, tx2);
        Xors3 = lang.newText(new Coordinates(50, 210), xor, "FunktionNameUnten", null, tx2);
        ersteF = lang.newText(new Coordinates(422, 413), F, "FunktionNameUnten", null, txSpecial);
        weiter = lang.newText(new Coordinates(400, 660), weit, "FunktionNameUnten", null, tx0);
        weiterplus = lang.newText(new Coordinates(440, 730), weitplus, "FunktionNameUnten", null, tx0);
        Xorsx = lang.newText(new Coordinates(50, 1020), xor, "FunktionNameUnten", null, tx2);
        Xorsxx = lang.newText(new Coordinates(760, 1030), xor, "FunktionNameUnten", null, tx2);

        Left = lang.newText(new Coordinates(60, 40), links, "FunktionNameUnten", null, tx2);
        Right = lang.newText(new Coordinates(710, 40), rechts, "FunktionNameUnten", null, tx2);

        SchriftP17 = lang.newText(new Coordinates(440, 910), p1, "FunktionNameUnten", null, tx2);
        SchriftP18 = lang.newText(new Coordinates(250, 1050), p0, "FunktionNameUnten", null, tx2);


        circ2 = lang.newCircle(new Coordinates(50, 1050), 40, "circ5", null, cercp);
        circ3 = lang.newCircle(new Coordinates(760, 1050), 40, "circ5", null, cercp);
        circ0 = lang.newCircle(new Coordinates(40, 230), 40, "circ5", null, cercp);
        circ1 = lang.newCircle(new Coordinates(760, 410), 40, "circ5", null, cercp);

        pol3 = lang.newPolyline(nA3, "pol1", null, polP);
        pol5 = lang.newPolyline(nA5, "pol1", null, polP);
        pol2 = lang.newPolyline(nA2, "pol1", null, polP);
        pol0 = lang.newPolyline(nA0, "pol0", null, polP);
        pol1 = lang.newPolyline(nA4, "pol0", null, polP);
        polx = lang.newPolyline(nA1, "pol0", null, polP);
        pol4 = lang.newPolyline(nA6, "pol1", null, polP);
        pol6 = lang.newPolyline(nA7, "pol1", null, polP);
        pol7 = lang.newPolyline(nA8, "pol1", null, polP);
        pol8 = lang.newPolyline(nA9, "pol1", null, polP);
        pol9 = lang.newPolyline(nA10, "pol1", null, polP);
        pol10 = lang.newPolyline(nA11, "pol1", null, polP);
        pol11 = lang.newPolyline(nA12, "pol1", null, polP);
        pol12 = lang.newPolyline(nA13, "pol1", null, polP);


        // SQUARE
        SquareProperties sqp = new SquareProperties();
        sqp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        sqp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);
        sq0 = lang.newSquare(new Coordinates(360, 370), 120, "sq0", null, sqp);

    }




    public void hideBuildingENC() {
        SchriftP17.hide();
        SchriftP18.hide();
        Left.hide();
        Right.hide();

        Xors.hide();
        Xorsx.hide();
        Xorsxx.hide();
        SchriftP1.hide();
        Xors3.hide();
        ersteF.hide();
        weiter.hide();
        weiterplus.hide();

        circ0.hide();
        circ1.hide();
        circ2.hide();
        circ3.hide();

        pol3.hide();
        pol5.hide();
        pol2.hide();
        pol0.hide();
        pol1.hide();
        polx.hide();
        pol4.hide();
        pol6.hide();
        pol7.hide();
        pol8.hide();
        pol9.hide();
        pol10.hide();
        pol11.hide();
        pol12.hide();

        sq0.hide();
    }

    public void initiateFCodeENC() {
        Node[] nA0 = new Node[2];

        nA0[0] = new Coordinates(950,1060);
        nA0[1] = new Coordinates(950,1150);

        Node[] nA11 = new Node[2];

        nA0[0] = new Coordinates(340,560);
        nA0[1] = new Coordinates(340,620);


        Node[] nA12 = new Node[2];

        nA0[0] = new Coordinates(310,590);
        nA0[1] = new Coordinates(370,590);


        Node[] nA13 = new Node[2];

        nA0[0] = new Coordinates(580,780);
        nA0[1] = new Coordinates(580,860);



        Node[] nA14 = new Node[2];

        nA0[0] = new Coordinates(540,820);
        nA0[1] = new Coordinates(620,820);

        Node[] nA15 = new Node[2];

        nA0[0] = new Coordinates(950,1000);
        nA0[1] = new Coordinates(950,1060);




        Node[] nA16 = new Node[2];

        nA0[0] = new Coordinates(920,1030);
        nA0[1] = new Coordinates(980,1030);





        Node[] nA1 = new Node[3];
        nA1[0] = new Coordinates(200,360);
        nA1[1] = new Coordinates(200,600);
        nA1[2] = new Coordinates(310,600);



        Node[] nA2 = new Node[3];
        nA2[0] = new Coordinates(520,460);
        nA2[1] = new Coordinates(520,600);
        nA2[2] = new Coordinates(370,600);


        Node[] nA3 = new Node[3];
        nA3[0] = new Coordinates(340,670);
        nA3[1] = new Coordinates(340,830) ;
        nA3[2] = new Coordinates(540,830);



        Node[] nA4 = new Node[3];
        nA4[0] = new Coordinates(850,550);
        nA4[1] = new Coordinates(850,830);
        nA4[2] = new Coordinates(620,830);



        Node[] nA5 = new Node[3];
        nA5[0] = new Coordinates(580,910);
        nA5[1] = new Coordinates(580,1040);
        nA5[2] = new Coordinates(920,1040);


        Node[] nA6 = new Node[3];
        nA6[0] = new Coordinates(1150,610);
        nA6[1] = new Coordinates(1150,1040);
        nA6[2] = new Coordinates(980,1040);

        String sb1 = "Sbox 1 Entry";
        String sb2 = "Sbox 2 Entry";
        String sb3 = "Sbox 3 Entry";
        String sb4 = "Sbox 4 Entry";

        //Description Code Text
        TextProperties tx = new TextProperties();
        tx.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        tx.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font("Monospaced", Font.PLAIN, 20));
        tx.set(AnimationPropertiesKeys.HIDDEN_PROPERTY, false);
        tx.set(AnimationPropertiesKeys.CENTERED_PROPERTY, true);
        tx.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 16);

        PolylineProperties polP = new PolylineProperties();
        // ##########################################################################################################
        polP.set(AnimationPropertiesKeys.FWARROW_PROPERTY,true);
        polP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        polP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 16);


        PolylineProperties polPOhne = new PolylineProperties();
        // ##########################################################################################################
        polPOhne.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        polPOhne.set(AnimationPropertiesKeys.FWARROW_PROPERTY,false);
        polPOhne.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 16);

        // SQUARE
        SquareProperties sqp = new SquareProperties();
        sqp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        sqp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 10);

        CircleProperties cercp = new CircleProperties();
        cercp.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        cercp.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 16);

        RectProperties rectP = new RectProperties();

        rectP.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        rectP.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 16);

        txf0 = lang.newText(new Coordinates(200,210),sb1,"Sbox 1 Entry",null,tx);
        txf1 = lang.newText(new Coordinates(530,310) ,sb2,"Sbox 2 Entry",null,tx);
        txf2 = lang.newText(new Coordinates(870,400),sb3,"Sbox 3 Entry",null,tx);
        txf3 = lang.newText(new Coordinates(1170,460),sb4,"Sbox 4 Entry",null,tx);

        xorf1 = lang.newText(new Coordinates(600,820),xor,"Sbox 4 Entry",null,tx);
        add0 = lang.newText(new Coordinates(330,570),add,"Sbox 4 Entry",null,tx);
        add1 = lang.newText(new Coordinates(940,1010),add,"Sbox 4 Entry",null,tx);

        sqf1 = lang.newSquare(new Coordinates(120, 150),180,"sq0",null,sqp);
        sqf2 = lang.newSquare(new Coordinates(440, 240),180,"sq0",null,sqp);
        sqf3 = lang.newSquare(new Coordinates(770, 330),180,"sq0",null,sqp);
        sqf4 = lang.newSquare(new Coordinates(1070, 400),180,"sq0",null,sqp);
        sqf5 = lang.newSquare(new Coordinates(310, 560),60,"sq0",null,sqp);
        sqf6 = lang.newSquare(new Coordinates(920, 1000) ,60,"sq0",null,sqp);

        circSbox = lang.newCircle(new Coordinates(585,830),40,"circ0",null,cercp);

        pofl0 = lang.newPolyline(nA1,"pol0",null,polP);
        pofl1 = lang.newPolyline(nA2,"pol1",null,polP);
        pofl2 = lang.newPolyline(nA3,"pol2",null,polP);
        pofl3 = lang.newPolyline(nA4,"pol3",null,polP);
        pofl4 = lang.newPolyline(nA5,"pol4",null,polP);
        pofl5 = lang.newPolyline(nA6,"pol5",null,polP);
    }

    public void hideF(){
        txf0.hide();
        txf1.hide();
        txf2.hide();
        txf3.hide();

        sqf1.hide();
        sqf2.hide();
        sqf3.hide();
        sqf4.hide();
        sqf5.hide();
        sqf6.hide();

        pofl0.hide();
        pofl1.hide();
        pofl2.hide();
        pofl3.hide();
        pofl4.hide();
        pofl5.hide();

        // circ1.hide();
        circSbox.hide();

        xorf1.hide();
        add0.hide();
        add1.hide();
    }

    public void SchowF(){
        txf0.show();
        txf1.show();
        txf2.show();
        txf3.show();

        sqf1.show();
        sqf2.show();
        sqf3.show();
        sqf4.show();
        sqf5.show();
        sqf6.show();

        pofl0.show();
        pofl1.show();
        pofl2.show();
        pofl3.show();
        pofl4.show();
        pofl5.show();
        pofl6.show();
        pofl7.show();
        pofl8.show();
        pofl9.show();
        pofl10.show();
        pofl11.show();

        circ1.show();
    }

    public void showENC(){
        SchriftP17.show();
        SchriftP18.show();
        Left.show();
        Right.show();
        Xors.show();
        SchriftP1.show();
        Xors3.show();
        ersteF.show();
        weiter.show();
        weiterplus.show();
        Xorsx.show();
        Xorsxx.show();

        circ0.show();
        circ1.show();
        circ2.show();
        circ3.show();


        pol3.show();
        pol5.show();
        pol2.show();
        pol0.show();
        pol1.show();
        polx.show();
        pol4.show();
        pol6.show();
        pol7.show();
        pol8.show();
        pol9.show();
        pol10.show();
        pol11.show();
        pol12.show();

        sq0.show();
    }

    /**
     * Copy the given string to clipboard.
     *
     * @param str
     *              String to copy to clipboard
     * @return
     *              True if the string has been successfully copied to clipboard, otherwise false
     */
    protected boolean copyToClipboard(String str) {
        Clipboard clipboard = getClipboard();

        if (clipboard != null) {
            StringSelection clip = new StringSelection(str);

            try {
                clipboard.setContents(clip, clip);
            } catch (IllegalStateException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Get the string from the clipboard.
     *
     * @return
     *          The string returned by the clipboard
     */
    protected String copyFromClipboard() {
        Clipboard clipboard = getClipboard();

        String clipped = null;
        if (clipboard != null) {
            Transferable transfer;
            try {
                transfer = clipboard.getContents(this);
                try {
                    clipped = (String) transfer.getTransferData(DataFlavor.stringFlavor);
                } catch (IOException e) {
                    clipped = null;
                } catch (UnsupportedFlavorException e) {
                    clipped = null;
                }
            } catch (IllegalStateException e) {
                clipped = null;
            }

        }

        return clipped;
    }

    /**
     * Get system's clipboard object.
     *
     * @return
     *          Clipboard object can be null
     */
    private Clipboard getClipboard() {
        Toolkit toolkit;

        try {
            toolkit = Toolkit.getDefaultToolkit();
        } catch (AWTError e) {
            return null;
        }

        Clipboard clipboard;
        try {
            clipboard = toolkit.getSystemClipboard();
        } catch (HeadlessException e) {
            return null;
        }

        return clipboard;
    }

    /**
     * Set language.
     *
     * @param language
     *                  Language object
     */
    protected void setLanguage(final Language language) {
        lang = language;
    }

    /**
     * Set plain text.
     *
     * @param plainText
     *                  Plain text
     */
    protected void setPlainText(final String plainText) {
        this.plainText = plainText;
    }

    /**
     * Get plain text.
     *
     * @return
     *          Plain text
     */
    protected String getPlainText() {
        return plainText;
    }

    /**
     * Set encrypted cipher text.
     *
     * @param cipher
     *                  Cipher text
     */
    protected void setCipher(final String cipher) {
        this.cipher = cipher;
    }

    /**
     * Get cipher text.
     *
     * @return
     *          Cipher text
     */
    protected String getCipher() {
        return cipher;
    }

    /**
     * Set secret text.
     *
     * @param secret
     *                  Secret text
     */
    protected void setSecret(final String secret) {
        this.secret = secret;
    }

    /**
     * Validate Blowfish cryptographic parameters.
     * Secret must be a multiple of 32bit and plain text must be a multiple of 64bit.
     *
     * @param secret
     *                                  Secret
     * @param text
     *                                  Plain text
     * @return
     *                                  True if validation requirements are met
     * @throws IllegalArgumentException
     *                                  Throws if the validation criteria is not met
     */
    protected boolean validate(String secret, String text) throws IllegalArgumentException {
        validate(secret);

        int len = text.length();
        if ((len % 8) != 0) {
            throw new IllegalArgumentException("Error: the given message is " + len * 8 + " bits (" + len + " chars), "
                    + "but expected a multiple of 64bit (8 chars)");
        }

        return true;
    }

    /**
     * Validate if the given secret matches Blowfish's requirement.
     *
     * @param secret
     *                                  Secret
     * @return
     *                                  True if validation requirement is met
     * @throws IllegalArgumentException
     *                                  Throws if the validation criteria is not met
     */
    protected boolean validate(String secret) throws IllegalArgumentException {
        int len = secret.length();
        if ((len % 4) != 0) {
            throw new IllegalArgumentException("Error: the given secret is " + len * 8 + " bits (" + len + " chars), "
                    + "but expected a multiple of 32bit (4 chars)");
        }

        return true;
    }

}
