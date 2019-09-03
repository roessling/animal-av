/*
 * ShamirSecretSharing.java
 * Oliver Käfer, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.cryptography;

import algoanim.animalscript.AnimalScript;
import algoanim.exceptions.NotEnoughNodesException;
import algoanim.primitives.*;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;
import algoanim.util.TicksTiming;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.*;
import java.util.Hashtable;
import java.util.Locale;

public class ShamirSecretSharing implements Generator {
    private Language lang;
    private CircleProperties head;
    private PolygonProperties body;
    private ArrayProperties arrayProps;
    private MatrixProperties matrixProps;
    private int secret;
    private int n;
    private int t;
    private int line;
    private int id_count;
    private SourceCode src;
    private Text aniP;
    private IntMatrix aniShare;
    private Variables v;

    public void init(){
        lang = new AnimalScript("Shamirs Secret Sharing", "Oliver Käfer", 800, 600);
        lang.setStepMode(true);
    }

    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        head = (CircleProperties)props.getPropertiesByName("head");
        arrayProps = (ArrayProperties)props.getPropertiesByName("arrayProps");
        matrixProps = (MatrixProperties)props.getPropertiesByName("matrixProps");
        t = (Integer)primitives.get("t");
        SourceCodeProperties srcProps = (SourceCodeProperties) props.getPropertiesByName("srcProps");
        secret = (Integer)primitives.get("secret");
        body = (PolygonProperties)props.getPropertiesByName("body");
        n = (Integer)primitives.get("n");

        line = 0;
        id_count = 0;

        v = lang.newVariables();

        TextProperties txtProps = new TextProperties();
        txtProps.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font(Font.SANS_SERIF,Font.BOLD,24));
        Text header = lang.newText(new Coordinates(10,20),"Shamir Secret Sharing","header",null,txtProps);
        RectProperties rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
        Rect headRect = lang.newRect(new Offset(-5, -5, "header",
                        AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
                null, rectProps);

        initialExplanation();
        lang.nextStep();
        initShowVariables();
        src = lang.newSourceCode(new Coordinates(5, 150),"src",null, srcProps);
        src.addMultilineCode(getCodeExample(),"code",null);
        lang.nextStep();
        src.highlight(0);
        int[][] shares = createShares();
        visualizeSharing(shares);
        lang.nextStep("Abschluss");
        lang.hideAllPrimitives();
        aniShare.show();
        aniShare.moveBy("Translate",-400,0,null,new TicksTiming(50));
        lang.addItem(lang.newText(new Offset(-400,-20,aniShare,"T"),"Shares:","shareLabel",null));
        header.show();
        headRect.show();
        showFinalExplanation(shares);
        return lang.toString();
    }

    private void showFinalExplanation(int[][] shares) {
        SourceCodeProperties srcProps = new SourceCodeProperties();
        srcProps.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font(Font.SANS_SERIF,Font.BOLD,18));
        SourceCode finalExplanation = lang.newSourceCode(new Coordinates(300,200),"finalExplanation",null,srcProps);
        finalExplanation.addCodeLine("Um das Geheimnis wieder herzustellen ist folgende Rechnung nötig.","finalExplanation",1,null);
        finalExplanation.addCodeLine("Dabei bezeichnet a^(-1) das inverse von a bezüglich p in dem endlichen Körper Z_p.","finalExplanation",1,null);
        finalExplanation.addMultilineCode("Für die Berechnung nimmt man sich t beliebige Punkte her.\nHier wurden die ersten t Punkte genommen.\n Daraus ergibt sich die folgende Allgemeine Formel für t-Punkte (x;y).\nsecret = summe von i = 1 bis t (y_i*[product von j=1 bis t mit i!=j (x_j*(x_j-x_i)^(-1)) ])","finallExplanation",null);
        StringBuilder points = new StringBuilder(100);
        for (int i = 0; i < t; i++) {
            points.append("(").append(shares[i][0]).append(", ").append(shares[i][1]).append("),");
        }
        points.deleteCharAt(points.length()-1);
        finalExplanation.addMultilineCode("Die Benutzten Punkte sind:\n"+points.toString(),"finalExplanation",null);
        StringBuilder formel = new StringBuilder(255);
        formel.append("Secret = ");
        for (int i = 0; i < t; i++) {
            formel.append(shares[i][1]).append("* [");
            for (int j = 0; j < t; j++) {
                if (i != j){
                    formel.append(shares[j][0]).append("*(").append(shares[j][0]).append(" - ").append(shares[i][0]).append(")^(-1)");
                }
            }
            formel.append("]");
            if (i < t-1) {
                formel.append(" + ");
            }
        }
        formel.append(" = ").append(secret);
        finalExplanation.addCodeLine(formel.toString(),"finalExplanation",1,null);
    }

    private void initialExplanation() {
        SourceCode explanation = lang.newSourceCode(new Coordinates(40,50),"explanation",null);
        explanation.addMultilineCode("Bei Shamir's Secret Sharing werden drei Schritte durchgeführt.\n"+
                "1. Bestimmen der Primzahl\n    Durch nextPrim(n+1) wird die nächste Primzahl nach n+1 (inklusive) bestimmt.\n    Dafür wird mit dem kleinen Satz von Fermat überprüft, ob es sich um eine Primzahl handelt."+
                "\n2. Erzeugen des Polynoms\n    Der y-Achsenabschnitt wird auf das secret gesetzt. Dies entspricht dem ersten Eintrag des Arrays.\n    Die anderen Koeffizienten sind frei wählbar. Hier wurden die t letzten Zahlen vor der Primzahl (exklusive) genommen.\n    Durch diese Wahl wird sich das Modulo rechnen gespart, weil sichergestellt ist, dass alle Zahlen kleiner als die Primzahl sind."+
                "\n3. Berechnen der Shares\n    Die x-Werte sind wieder frei wählbar. Hier wurden die ersten n Zahlen ungleich 0 gewählt.\n    Dann wird das Polynom an diesen Stellen ausgewertet und es werden Paare von x und y Werten gebildet.\n    Wichtig ist, dass jeder y-Wert modulo der Primzahl genommen wird.\n    Diese Paare sind die Shares die später an die Entitäten gegeben werden."+
                "\nNach dem Berechnen der Shares müssen diese verteilt werden. Jeder der n Shareholder bekommt einen Share.\nUm das Geheimnis wieder zu bestimmen, müssen nur t der n Shareholder ihre Shares benutzen und mit Hilfe von Interpolationsverfahren das Polynom an der Stelle 0 bestimmen.\nDafür bietet sich die Lagrange-Interpolation an, da diese direkt an einer Stelle bestimmt werden kann.","explanation",null);
        lang.nextStep("Erklärung");
        explanation.hide();
    }

    private void visualizeSharing(int[][] shares) {
        lang.nextStep("Verteilung der Shares");
        createHuman(lang,800, 200,2);
        lang.addItem(lang.newText(new Offset(0,-20,"head0","T"),"Secret: "+String.valueOf(secret),"secLabel",null));
        for (int i = 0; i < shares.length; i++) {
            lang.nextStep();
            creatShareHolder(lang,650+50*i, 300,2,"body0");
            lang.addItem(lang.newText(new Offset(0,20,"body"+(i+1),"B"),String.format("(%d,%d)",shares[i][0],shares[i][1]),"secLabel",null));
        }
    }

    private void initShowVariables() {
        lang.addItem(lang.newText(new Coordinates(20,45),"n: " + String.valueOf(n),"nLabel",null));
        lang.addItem(lang.newText(new Coordinates(20,65),"t: " + String.valueOf(t),"nLabel",null));
        lang.addItem(lang.newText(new Coordinates(20,80),"secret: " + String.valueOf(secret),"nLabel",null));
        aniP = lang.newText(new Coordinates(20,95),"p: ","pValue",null);
    }

    private void nextSrcLine(){
        src.unhighlight(line);
        src.highlight(++line);
    }

    private int[][] createShares(){
        lang.nextStep("Wähle die Primzahl");
        nextSrcLine();
        int p = nextPrim(n+1);
        showP(p);

        lang.nextStep();
        nextSrcLine();
        if (p < secret){
            lang.nextStep();
            nextSrcLine();
            p = nextPrim(secret+1);
            showP(p);
        }
        lang.nextStep();
        src.unhighlight(line);
        line = 4;
        nextSrcLine();
        int[] polynom = new int[t];
        polynom[0] = secret;
        for (int i = 1; i < polynom.length; i++) {
            polynom[i] = p - (t) + i;
        }

        IntArray aniPolynom = lang.newIntArray(new Coordinates(500,100),polynom,"polynom",null,arrayProps);
        aniPolynom.highlightElem(0,polynom.length-1,null,null);
        lang.addItem(lang.newText(new Offset(-70,1,aniPolynom,"L"),"Polynom: ","polyLabel",null));

        lang.nextStep("Das Polynom bestimmen");
        nextSrcLine();
        aniPolynom.unhighlightElem(0,null,null);
        aniPolynom.highlightCell(0,null,null);

        lang.nextStep();
        nextSrcLine();
        v.declare("int","i","1","Counter für die Polynom erstellung");
        for (int i = 1; i < polynom.length; i++) {
            v.set("i",String.valueOf(i));
            lang.nextStep();
            nextSrcLine();
            aniPolynom.unhighlightElem(i,null,null);
            aniPolynom.unhighlightCell(i-1,null,null);
            aniPolynom.highlightCell(i,null,null);

            lang.nextStep();
            src.unhighlight(line);
            line-=2;
            nextSrcLine();
        }
        aniPolynom.unhighlightCell(polynom.length-1,null,null);

        int[][] shares = new int[n][2];
        for (int i = 0; i < shares.length; i++) {
            shares[i][0] = i+1;
            shares[i][1] = mathModulo(polynomAt(polynom,i+1),p);
        }

        lang.nextStep();
        src.unhighlight(line);
        line+=2;
        nextSrcLine();
        aniShare = lang.newIntMatrix(new Coordinates(500,200),shares,"shares",null,matrixProps);
        aniShare.unhighlightCellRowRange(0,shares.length-1,0,null,null);
        aniShare.unhighlightCellRowRange(0,shares.length-1,1,null,null);
        lang.addItem(lang.newText(new Offset(0,-20,aniShare,"T"),"Shares:","shareLabel",null));
        for (int i = 0; i < shares.length; i++) {
            aniShare.highlightElem(i,0,null,null);
            aniShare.highlightElem(i,1,null,null);
        }

        lang.nextStep("Die Shares berechnen");
        nextSrcLine();
        v.declare("int","j","0","Counter für die Share berechnung");
        for (int i = 0; i < shares.length; i++) {
            v.set("j",String.valueOf(i));
            lang.nextStep();
            nextSrcLine();
            aniShare.unhighlightElem(i,0,null,null);
            aniShare.highlightCell(i,0,null,null);
            if (i >= 1)
                aniShare.unhighlightCell(i-1,0,null,null);

            lang.nextStep();
            nextSrcLine();
            aniShare.unhighlightElem(i,1,null,null);
            aniShare.highlightCell(i,1,null,null);
            if (i >= 1)
                aniShare.unhighlightCell(i-1,1,null,null);

            lang.nextStep();
            src.unhighlight(line);
            line -= 3;
            nextSrcLine();
        }
        aniShare.unhighlightCell(shares.length-1,0,null,null);
        aniShare.unhighlightCell(shares.length-1,1,null,null);
        lang.nextStep();
        src.unhighlight(line);
        line+=3;
        nextSrcLine();
        return shares;
    }

    private void showP(int p) {
        aniP.setText("p: " + String.valueOf(p),null,null);
    }

    private int mathModulo(int x, int y){
        if (x >= 0){
            return x % y;
        } else {
            while (x < 0){
                x += y;
            }
            return x;
        }
    }

    private int nextPrim(int n) {
        while (!isPrim(n)) {
            n++;
        }
        return n;
    }

    private boolean isPrim(int n) {
        return (!(n % 2 == 0 || n == 1) && ( mathModulo((int)Math.pow(2, n - 1), n) == 1)) || n == 2; // kleiner Satz von Fermat
    }

    private int polynomAt(int[] polynom, int x) {
        int val = 0;
        for (int i = 0; i < polynom.length; i++) {
            val += polynom[i] * ((int) Math.pow(x,i));
        }
        return val;
    }

    private void creatShareHolder(Language language, int xCor, int yCor, int scale, String id){
        PolylineProperties plp = new PolylineProperties();
        plp.set(AnimationPropertiesKeys.FWARROW_PROPERTY,true);
        plp.set(AnimationPropertiesKeys.DEPTH_PROPERTY,2);
        language.addItem(language.newPolyline(new Node[]{new Offset(0,0,id,"C"),new Coordinates(xCor,yCor)},
                "line" + id_count,null,plp));
        createHuman(language,xCor,yCor+10,scale);
    }

    private void createHuman(Language language, int xCor, int yCor, int scale){
        StringBuilder builder_head = new StringBuilder(10);
        StringBuilder builder_body = new StringBuilder(10);
        builder_body.append("body").append(id_count);
        builder_head.append("head").append(id_count);
        id_count++;
        CircleProperties circleProps = head;
        int radius = 5*scale;
        language.addItem(language.newCircle(new Coordinates(xCor,yCor),radius,builder_head.toString(),null,circleProps));
        PolygonProperties pp = body;
        try {
            language.addItem(language.newPolygon(new Node[]{new Offset(0,0,builder_head.toString(),"C"),
                    new Offset(radius+2,radius+2,builder_head.toString(),"C"),
                    new Offset(radius+2,radius+5,builder_head.toString(),"C"),
                    new Offset(-(radius+2),radius+5,builder_head.toString(),"C"),
                    new Offset(-(radius+2),radius+2,builder_head.toString(),"C"),new Offset(0,0,builder_head.toString(),"C")},builder_body.toString(),null,pp));
        } catch (NotEnoughNodesException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return "Shamir's Secret Sharing";
    }

    public String getAlgorithmName() {
        return "Shamir's Secret Sharing";
    }

    public String getAnimationAuthor() {
        return "Oliver Käfer";
    }

    public String getDescription(){
        return "Shamir's Secret Sharing ist ein Verfahren um ein Geheimnis, z.B. einen Schlüssel für den späteren Gebrauch zu sichern. Dabei wird ähnlich zu Schatzkarten das Geheimnis aufgeteilt und die Teile an verschiedene Entitäten (z.B. Personen, Computer u.ä.) verteilt. Da sich Daten schlecht zerschneiden lassen wie Schatzkarten, benutzt man Polynome. Die Teile sind dann Punkte auf dem Polynom. "
 +"\n"
 +"\n"
 +"Bei Shamir's Secret Sharing wird ein Tupel (n,t) betrachtet, wobei n die Anzahl der Entitäten angibt die ein Geheimnisteil bekommen und t die Mindestzahl bezeichnet die zusammen kommen müssen um das Geheimnis zu rekonstruieren."
 +"\n"
 +"Um dies zu erreichen, wird ein Polynom vom Grad t gebaut, wobei der Schnittpunkt mit der y-Achse das Geheimnis ist. Dann werden n verschiedene Punkte von dem Polynom ausgerechnet. Die Shares, die Geheimnisteile die verteilt werden, bestehen also aus dem x Wert und dem dazugehörigen y Wert."
 +"\n"
 +"\n"
 +"Mit hilfe von Interpolationstechniken, wie Lagrange oder Newton, können t Entitäten das Geheimnis rekonstruieren.";
    }

    public String getCodeExample(){
        return " public int[][] shamirSecretSharing(int secret,int t, int n) {"
 +"\n"
 +"        int p = nextPrim(n+1);"
 +"\n"
 +"        if (p < secret){"
 +"\n"
 +"            p = nextPrim(secret+1);"
 +"\n"
 +"        }"
 +"\n"
 +"        int[] polynom = new int[t];"
 +"\n"
 +"        polynom[0] = secret;"
 +"\n"
 +"        for (int i = 1; i < polynom.length; i++) {"
 +"\n"
 +"            polynom[i] = p - (t-1) + i;"
 +"\n"
 +"        }"
 +"\n"
 +"        int[][] shares = new int[n][2];"
 +"\n"
 +"        for (int j = 0; j < shares.length; j++) {"
 +"\n"
 +"            shares[i][0] = j+1;"
 +"\n"
 +"            shares[i][1] = polynomAt(polynom,j+1) % p;"
 +"\n"
 +"        }"
 +"\n"
 +"        return shares;"
 +"\n"
 +"    }";
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
        return Generator.JAVA_OUTPUT;
    }

}