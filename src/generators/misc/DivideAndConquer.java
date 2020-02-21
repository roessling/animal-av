package generators.misc;
/*
 * divideAndCoquer.java
 * Viola Hofmeister, Yvonne Meuleneers, 2019 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.*;
import algoanim.util.*;
import animal.main.Animal;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import interactionsupport.models.MultipleChoiceQuestionModel;

import java.awt.*;
import java.util.Hashtable;
import java.util.Locale;


public class DivideAndConquer implements Generator {
    private Language lang;
    private ArrayProperties arrayProb;
    private ArrayMarkerProperties arrayMarker;

    SourceCodeProperties scProb1;
    SourceCodeProperties scProb2;
    private TextProperties txtProb1;
    private TextProperties txtProb2;
    private TextProperties txtProb3;
    private TextProperties txtProb4;
    private SourceCode sc1;
    private SourceCode sc2;
    private SourceCode sc3;

    public DivideAndConquer() {
    }

    public static void main(String[] args) {
        Generator generator = new DivideAndConquer();  // Generator erzeugen
        Animal.startGeneratorWindow(generator); // Animal mit Generator starten
    }

    public void init(){
        this.lang = new AnimalScript("MaxTeilfield: Divide and Conquer", "Viola Hofmeister und Yvonne Meuleneers", 800, 600);
        this.lang.setStepMode(true);
        this.txtProb1 = new TextProperties();
        this.txtProb1.set("color", Color.DARK_GRAY);
        this.txtProb1.set("font", new Font("Monospaced", 3, 26));
        this.txtProb2 = new TextProperties();
        this.txtProb2.set("color", Color.GRAY);
        this.txtProb2.set("font", new Font("Monospaced", 1, 16));
        this.txtProb3 = new TextProperties();
        this.txtProb3.set("color", Color.BLACK);
        this.txtProb3.set("font", new Font("Monospaced", 1, 16));
        this.txtProb4 = new TextProperties();
        this.txtProb4.set("color", Color.BLACK);
        this.txtProb4.set("font", new Font("Monospaced", 1, 20));
        this.scProb1 = new SourceCodeProperties();
        this.scProb1.set("contextColor", Color.blue);
        this.scProb1.set("font", new Font("Monospaced", 1, 16));
        this.scProb1.set("highlightColor", Color.red);
        this.scProb1.set("color", Color.black);
        this.scProb1.set("font", new Font("Monospaced", 1, 16));
        this.scProb2 = new SourceCodeProperties();
        this.scProb2.set("contextColor", Color.blue);
        this.scProb2.set("font", new Font("Monospaced", 1, 16));
        this.scProb2.set("highlightColor", Color.orange);
        this.scProb2.set("color", Color.black);
        this.scProb2.set("font", new Font("Monospaced", 1, 16));
        this.arrayProb = new ArrayProperties();
        this.arrayMarker = new ArrayMarkerProperties();
        this.arrayMarker.set("color", Color.ORANGE);
        this.arrayMarker.set("label", "x");
    }

    public void setTitle() {
        Text title = this.lang.newText(new Coordinates(20, 30), "MaxTeilfield: Divide and Conquer", "title", null, this.txtProb1);
        this.lang.newRect(new Offset(-3, -5, title, "NW"), new Offset(5, 5, title, "SE"), "recht", (DisplayOptions)null);
    }

    public void setDescription() {
        // Überschriftsergänzung
        Text title1 = this.lang.newText(new Coordinates(560, 30), "- Einleitung", "endTitle", null, this.txtProb1);

        //Description
        Text d1 = this.lang.newText(new Coordinates(20, 100), "Bei dem Divide and Conquer Ansatz im Bezug auf die Maximale", "descr1", null, this.txtProb3);
        Text d2 = this.lang.newText(new Coordinates(20, 120), "Teilsumme, werden kleine Probleme direkt gelöst: n=1 => Max = X(0). ", "descr2", null, this.txtProb3);
        Text d3 = this.lang.newText(new Coordinates(20, 140), "Große Probleme werden in zwei Teilprobleme zerlegt und rekursiv gelöst.", "descr3", null, this.txtProb3);
        Text d4 = this.lang.newText(new Coordinates(20, 160), "Dabei gibt es auf der linken Seite ein Teilproblem A und auf", "descr4", null, this.txtProb3);
        Text d5 = this.lang.newText(new Coordinates(20, 180), "der rechten Seite ein Teilproblem B. Desweiteren gilt es zudem", "descr5", null, this.txtProb3);
        Text d6 = this.lang.newText(new Coordinates(20, 200), "auch eine Teillösung C zu ermitteln, wozu ein rechtes Maximum und ", "descr6", null, this.txtProb3);
        Text d7 = this.lang.newText(new Coordinates(20, 220), "ein linkes Maximum bestimmt werden muss. Eine Gesamtlösung ergibt ", "descr7", null, this.txtProb3);
        Text d8 = this.lang.newText(new Coordinates(20, 240), "sich als Maximum von A, B und C.", "descr8", null, this.txtProb3);

        //Codee Example
        Text d9 = this.lang.newText(new Coordinates(20, 280), "CODE EXAMPLE:", "descr9", null, this.txtProb3);
        Text c = this.lang.newText(new Coordinates(50, 320), "public int maxSubArray(int[] X, int u, int o) {", "cod", null, this.txtProb2);
        Text c1 = this.lang.newText(new Coordinates(50, 340), "     if (u == o) return X[u];", "cod", null, this.txtProb2);
        Text c2 = this.lang.newText(new Coordinates(50, 360), "     if (u + 1 == o) return max(X[u], X[o], X[u] + X[o]); ", "cod", null, this.txtProb2);
        Text c3 = this.lang.newText(new Coordinates(50, 380), "     int m = (u + o) / 2;", "cod", null, this.txtProb2);
        Text c4 = this.lang.newText(new Coordinates(50, 400), "     int A = maxSubArray(X, u, m);", "cod", null, this.txtProb2);
        Text c5 = this.lang.newText(new Coordinates(50, 420), "     int B = maxSubArray(X, m + 1, o);", "cod", null, this.txtProb2);
        Text c6 = this.lang.newText(new Coordinates(50, 440), "     int C1 = rmax(X, u, m);", "cod", null, this.txtProb2);
        Text c7 = this.lang.newText(new Coordinates(50, 460), "     int C2 = lmax(X, m + 1, o);", "cod", null, this.txtProb2);
        Text c8 = this.lang.newText(new Coordinates(50, 480), "     return max(A, B, C1 + C2);", "cod", null, this.txtProb2);
        Text c9 = this.lang.newText(new Coordinates(50, 500), "}", "cod", null, this.txtProb2);

        this.lang.nextStep();

        title1.hide(); c.hide();
        d1.hide(); c1.hide();
        d2.hide(); c2.hide();
        d3.hide(); c3.hide();
        d4.hide(); c4.hide();
        d5.hide(); c5.hide();
        d6.hide(); c6.hide();
        d7.hide(); c7.hide();
        d8.hide(); c8.hide();
        d9.hide(); c9.hide();
    }

    public void showSourceCode() {

        // line 0: A
        this.sc1 = this.lang.newSourceCode(new Coordinates(50, 250), "sourceCode0", null, this.scProb2);
        this.sc1.addCodeLine("Die Teilsumme von A ist:", null, 0, null);
        // line 1: B
        this.sc1 = this.lang.newSourceCode(new Coordinates(50, 300), "sourceCode0", null, this.scProb2);
        this.sc1.addCodeLine("Die Teilsumme von B ist:", null, 0, null);
        // line 2: C1
        this.sc1 = this.lang.newSourceCode(new Coordinates(50, 350), "sourceCode2", null, this.scProb2);
        this.sc1.addCodeLine("Die Teilsumme von C1 ist:", null, 0, null);
        // line 3: C2
        this.sc1 = this.lang.newSourceCode(new Coordinates(50, 400), "sourceCode3", null, this.scProb2);
        this.sc1.addCodeLine("Die Teilsumme von C2 ist:", null, 0, null);
        // line 3: C
        this.sc1 = this.lang.newSourceCode(new Coordinates(50, 450), "sourceCode3", null, this.scProb2);
        this.sc1.addCodeLine("Die Teilsumme von C ist:", null, 0, null);
        // line 4: MaxTeilsumme
        this.sc1 = this.lang.newSourceCode(new Coordinates(50, 550), "sourceCode4", null, this.scProb2);
        this.sc1.addCodeLine("Die maximale Teilsumme ist:", null, 0, null);

        //sourcecode maxSubArray line 0-13
        this.sc2 = this.lang.newSourceCode(new Coordinates(550, 100), "sourceCode5", null, this.scProb1);
        this.sc2.addCodeLine("public int maxSubArray(int[] X, int u, int o) {", null, 0, null);
        this.sc2.addCodeLine("if (u == o) {", null, 1, null);
        this.sc2.addCodeLine("return X[u];", null, 2, null);
        this.sc2.addCodeLine("}", null, 1, null);
        this.sc2.addCodeLine("if (u + 1 == o) {", null, 1, null);
        this.sc2.addCodeLine("return max(X[u], X[o], X[u] + X[o]);", null, 2, null);
        this.sc2.addCodeLine("}", null, 1, null);
        this.sc2.addCodeLine("int m = (u + o) / 2;", null, 1, null);
        this.sc2.addCodeLine("int A = maxSubArray(X, u, m);", null, 1, null);
        this.sc2.addCodeLine("int B = maxSubArray(X, m + 1, o);", null, 1, null);
        this.sc2.addCodeLine("int C1 = rmax(X, u, m);", null, 1, null);
        this.sc2.addCodeLine("int C2 = lmax(X, m + 1, o);", null, 1, null);
        this.sc2.addCodeLine("return max(A, B, C1 + C2);", null, 1, null);
        this.sc2.addCodeLine("}", null, 0, null);
        this.sc2.addCodeLine(" ", null, 0, null);

        //sourcecode max line 14-28
        this.sc2.addCodeLine("public int max(int a, int b, int c) {", null, 0, null);
        this.sc2.addCodeLine("if (a > b) {", null, 1, null);
        this.sc2.addCodeLine("if (a > c) {", null, 2, null);
        this.sc2.addCodeLine("return a;", null, 3, null);
        this.sc2.addCodeLine("} else {", null, 2, null);
        this.sc2.addCodeLine("return c;", null, 3, null);
        this.sc2.addCodeLine("}", null, 2, null);
        this.sc2.addCodeLine("} else {", null, 1, null);
        this.sc2.addCodeLine("if (c > b) {", null, 2, null);
        this.sc2.addCodeLine("return c;", null, 3, null);
        this.sc2.addCodeLine("} else {", null, 2, null);
        this.sc2.addCodeLine("return b;", null, 3, null);
        this.sc2.addCodeLine("}", null, 2, null);
        this.sc2.addCodeLine("}", null, 1, null);
        this.sc2.addCodeLine("}", null, 0, null);

        //sourcecode lmax line 0-9
        this.sc3 = this.lang.newSourceCode(new Coordinates(1150, 100), "sourceCode6", null, this.scProb1);
        this.sc3.addCodeLine("public int lmax(int[] X, int u, int o) {", null, 0, null);
        this.sc3.addCodeLine("int lmax = X[u];", null, 1, null);
        this.sc3.addCodeLine("int summe = X[u];", null, 1, null);
        this.sc3.addCodeLine("for (int i = u + 1; i <= o; i++) {", null, 1, null);
        this.sc3.addCodeLine("summe = summe + X[i];", null, 2, null);
        this.sc3.addCodeLine("if (summe > lmax) {", null, 2, null);
        this.sc3.addCodeLine("lmax = summe;", null, 3, null);
        this.sc3.addCodeLine("}", null, 2, null);
        this.sc3.addCodeLine("}", null, 1, null);
        this.sc3.addCodeLine("return lmax;", null, 1, null);
        this.sc3.addCodeLine("}", null, 0, null);
        this.sc3.addCodeLine(" ", null, 0, null);

        //sourcecode rmax line 40-50
        this.sc3.addCodeLine("public int rmax(int[] X, int u, int o) {", null, 0, null);
        this.sc3.addCodeLine("int rmax = X[o];", null, 1, null);
        this.sc3.addCodeLine("int summe = X[o];", null, 1, null);
        this.sc3.addCodeLine("for (int i = o - 1; i >= u; i--) {", null, 1, null);
        this.sc3.addCodeLine("summe = summe + X[i];", null, 2, null);
        this.sc3.addCodeLine("if (summe > rmax) {", null, 2, null);
        this.sc3.addCodeLine("rmax = summe;", null, 3, null);
        this.sc3.addCodeLine("}", null, 2, null);
        this.sc3.addCodeLine("}", null, 1, null);
        this.sc3.addCodeLine("return rmax;", null, 1, null);
        this.sc3.addCodeLine("}", null, 0, null);
    }

    /*
    private void ask(int random, int[] x, int u, int o, int var) {
        MultipleChoiceQuestionModel questionMC = new MultipleChoiceQuestionModel("Question");
        switch (random) {
            case 1:
                questionMC.setPrompt("Was ist die Teilsumme von A?");
                questionMC.addAnswer("1","1,5", 0, "Falsch");
                questionMC.addAnswer("2", "3", 0, "Falsch");
                questionMC.addAnswer("3", "10", 0, "Falsch");
                questionMC.addAnswer("4", Integer.toString(maxSubArray(x, u, o, 1)), 1, "Richtig!");
                questionMC.addAnswer("5","Es existiert keine Teilsumme von A.", 0, "Falsch");
                break;
            case 2:
                questionMC.setPrompt("Was ist die maximale Teilsumme?");
                questionMC.addAnswer("6", "0", 1, "Falsch");
                questionMC.addAnswer("7", "1", 0, "Falsch");
                questionMC.addAnswer("7", Integer.toString(maxSubArray(x, u, o, 3)), 0, "Falsch");
                questionMC.addAnswer("9", Integer.toString(maxSubArray(x, u, o, 4)), 0, "Falsch");
                questionMC.addAnswer("10", Integer.toString(maxSubArray(x, u, o, 0)), 0, "Richtig!");
                questionMC.addAnswer("11", "Es existiert keine maximale Teilsumme.", 0, "Falsch");
                break;
        }
        this.lang.addMCQuestion(questionMC);
    }*/

    public void divideAndConquer(int[] z, int u, int o) {

        int m = (u+o)/2;
        int resultA = maxSubArray(z, u, o, 1);
        int resultB = maxSubArray(z, u, o, 2);
        int resultC1 = maxSubArray(z, u, o, 3);
        int resultC2 = maxSubArray(z, u, o, 4);
        int resultC = resultC1 + resultC2;
        int endResult = max(resultA, resultB, resultC);
        this.setTitle();
        this.setDescription();

        this.showSourceCode();
        this.lang.newText(new Coordinates(305, 110), "int u = " + Integer.toString(u) + ";", "uB", null, this.txtProb4);
        this.lang.newText(new Coordinates(305, 125), "int o = " + Integer.toString(o) + ";", "oB", null, this.txtProb4);
        this.lang.newText(new Coordinates(50, 110), "array:", "txtArray", null, this.txtProb4);
        this.lang.nextStep();

        IntArray input = this.lang.newIntArray(new Coordinates(50, 180), z, "array", null, this.arrayProb);
        ArrayMarker a = this.lang.newArrayMarker(input, u, "a", null, this.arrayMarker);
        this.lang.nextStep();

        // A
        this.sc2.highlight(8);
        //this.ask(1, z, u, o, 1);
        this.lang.newText(new Coordinates(305, 265), String.valueOf(resultA), "descr1", null, this.txtProb3);
        this.lang.nextStep();
        a.hide();

        // B
        ArrayMarker b = this.lang.newArrayMarker(input, o, "b", null, this.arrayMarker);
        this.sc2.toggleHighlight(8,9);
        this.lang.newText(new Coordinates(305, 315), String.valueOf(resultB), "descr1", null, this.txtProb3);
        this.lang.nextStep();
        b.hide();

        // C1
        ArrayMarker c1 = this.lang.newArrayMarker(input, u + 1, "c1", null, this.arrayMarker);
        this.sc2.toggleHighlight(9, 10);

        int x = 13;
        while (x < 22) {
            this.sc3.highlight(x);
            x++;
        }
        this.lang.newText(new Coordinates(310, 365), String.valueOf(resultC1), "descr1", null, this.txtProb3);
        this.lang.nextStep();
        c1.hide();
        x = 13;
        while (x < 22) {
            this.sc3.unhighlight(x);
            x++;
        }

        // C2
        ArrayMarker c2 = this.lang.newArrayMarker(input, m + 1, "c2", null, this.arrayMarker);
        this.sc2.toggleHighlight(10,11);

        x = 1;
        while (x < 10) {
            this.sc3.highlight(x);
            x++;
        }
        this.lang.newText(new Coordinates(310, 415), String.valueOf(resultC2), "descr1", null, this.txtProb3);
        this.lang.nextStep();

        // C
        this.lang.newText(new Coordinates(305, 465), String.valueOf(resultC), "descr1", null, this.txtProb3);
        c2.hide();
        x = 1;
        while (x < 10) {
            sc3.unhighlight(x);
            x++;
        }
        this.lang.nextStep();

        // return max(A,B,C)
        this.sc2.toggleHighlight(11, 12);

        x = 16;
        while (x < 29) {
            this.sc2.highlight(x);
            x++;
        }
        this.lang.nextStep();
        x = 15;
        while (x < 29) {
            sc2.unhighlight(x);
            x++;
        }
        this.sc2.unhighlight(13);

        this.lang.nextStep();

        //this.ask(2, z, u, o, 0);

        this.sc1.highlight(0);
        this.lang.newText(new Coordinates(330, 565), String.valueOf(endResult), "descr1", null, this.txtProb3);
        this.lang.nextStep();

        // Übersicht
        this.sc1.unhighlight(0);
        this.sc2.unhighlight(12);

        this.lang.newText(new Coordinates(560, 30), "- Übersicht", "EndTitle", null, this.txtProb1);

    }

    private int maxSubArray(int[] X, int u, int o, int var) {
        if (u == o) {
            return X[u];
        }
        if (u + 1 == o) {
            return max(X[u], X[o], X[u] + X[o]);
        }
        int m = (u + o) / 2;
        int A = maxSubArray(X, u, m, var);
        int B = maxSubArray(X, m + 1, o, var);
        int C1 = rmax(X, u, m);
        int C2 = lmax(X, m + 1, o);

        if (var == 1) return A;
        if (var == 2) return B;
        if (var == 3) return C1;
        if (var == 4) return C2;

        return max(A, B, C1 + C2);
    }

    private int max(int a, int b, int c) {
        if (a > b) {
            if (a > c) {
                return a;
            } else {
                return c;
            }
        } else {
            if (c > b) {
                return c;
            } else {
                return b;
            }
        }
    }

    private int lmax(int[] X, int u, int o) {
        int lmax = X[u];
        int summe = X[u];
        for (int i = u + 1; i <= o; i++) {
            summe = summe + X[i];
            if (summe > lmax) {
                lmax = summe;
            }
        }
        return lmax;
    }

    private int rmax(int[] X, int u, int o) {
        int rmax = X[o];
        int summe = X[o];
        for (int i = o - 1; i >= u; i--) {
            summe = summe + X[i];
            if (summe > rmax) {
                rmax = summe;
            }
        }
        return rmax;
    }

    public String getName() {
        return "MaxTeilfield: Divide and Conquer";
    }

    public String generate(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> hashtable) {
        DivideAndConquer diCo = new DivideAndConquer();
        diCo.init();
        diCo.arrayProb = (ArrayProperties)animationPropertiesContainer.getPropertiesByName("array");
        diCo.arrayMarker = (ArrayMarkerProperties)animationPropertiesContainer.getPropertiesByName("arrayMarker");
        int[] array = (int[])hashtable.get("Array");
        diCo.divideAndConquer(array, 0, array.length-1);
        return diCo.lang.toString();
    }

    public String getAlgorithmName() {
        return "Divide and Conquer";
    }

    public String getAnimationAuthor() {
        return "Viola Hofmeister und Yvonne Meuleneers";
    }

    public String getDescription(){
        return "Divide and Conquer, auch Teile und Herrsche genannt, ist eine Technik zum Entwurf von Algorithmen."
                 +"\n"
                 +"\n"
                 +"Bei dem Divide and Conquer Ansatz im Bezug auf die Maximale Teilsumme, werden kleine Probleme direkt gelöst: n=1 => Max = X(0). "
                 +"\n"
                 +"Große Probleme werden in zwei Teilprobleme zerlegt und rekursiv gelöst. Dabei gibt es auf der linken Seite ein Teilproblem A und auf "
                 +"\n"
                 +"der rechten Seite ein Teilproblem B. Desweiteren gilt es zudem auch eine Teillösung C zu ermitteln, wozu ein rechtes Maximum und "
                 +"\n"
                 +"ein linkes Maximum bestimmt werden muss. Eine Gesamtlösung ergibt sich als Maximum von A, B und C.";
    }

    public String getCodeExample(){
        return "maxSubArray (X, u, o)  {		// X soll eine Referenz oder ein Pointer sein"
                 +"\n"
                 +"\n"
                 +"	if (u == o) then return X(u); 	// Tivialfall (REkursionsabbruch)"
                 +"\n"
                 +"\n"
                 +"	m = (u + o) / 2;"
                 +"\n"
                 +"\n"
                 +"	A = maxSubArray (X, u, m);"
                 +"\n"
                 +"	B = maxSubArray(x, m+1, o);	// Lösungen A und B für die beiden Teilfelder (Rekursion)"
                 +"\n"
                 +"\n"
                 +"	C1 = rmax(X, u, m);"
                 +"\n"
                 +"	C2 = lmax(X, m+1, o);	// rmax und lmax für den Grenzfall C"
                 +"\n"
                 +"\n"
                 +"	return max(A, B, C1+C2);	// Lösung ergibt sich als Maximum aus A. B, und C"
                 +"\n"
                 +"\n"
                 +"}";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}