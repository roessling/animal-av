/*
 * Timsort.java
 * Alessandro Noli,Paul Yousef, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.sorting;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ConceptualStack;
import algoanim.primitives.IntArray;
import algoanim.primitives.IntMatrix;
import algoanim.primitives.Primitive;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.MatrixProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.StackProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.OffsetFromLastPosition;
import algoanim.util.Timing;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;

public class Timsort implements ValidatingGenerator {
    private Language lang;
    private int minrun;
    private ArrayProperties arrayProp;
    private TextProperties textprop;
    private MatrixProperties matrixProp;

    //angeben
    private TextProperties headerprop;

    //changeable
    private List<Primitive> prmlst;
    private Coordinates header;

    //fest
    private IntMatrix intMatrix;
    private ConceptualStack <String>stack;

    //Meine Parameter
    private int[] intArray;

    private int stackSize;
    private int[] baseRun = new int[4];
    private int[] runLen = new int[4];

    private List<String> stacklst = new ArrayList<>();
    private int meineFarbe;

    public void init(){
        lang = new AnimalScript("Timsort", "Alessandro Noli,Paul Youssef", 800, 800);
    }


    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        arrayProp = (ArrayProperties)props.getPropertiesByName("arrayProp");
        textprop = (TextProperties)props.getPropertiesByName("textprop");
        matrixProp = (MatrixProperties)props.getPropertiesByName("matrixProp");
        this.minrun = (int)primitives.get("minrun");
        int[] intArraytmp = (int[]) primitives.get("intArray");

        intArray = intArraytmp.clone();

        start();
        return lang.toString();
    }



    public String getName() {
        return "Timsort";
    }

    public String getAlgorithmName() {
        return "Timsort";
    }

    public String getAnimationAuthor() {
        return "Alessandro Noli,Paul Youssef";
    }

    public String getDescription(){
        return "Timsort ist ein hybrider Soriteralgorithmus, dieser aus Merge- und Insertionsort besteht " +
                "\n" +
                "Der Algorithmus wurde 2002 von Tim Peters entwickelt und ist seit Version 2.3 in Python, sowie seit Java SE 7 und der Android Platform" +
                "\n" +
                "der Standard-Sortieralgorithmus." +
                "\n" +
                "\n" +
                "Die Besonderheit an Timsort ist dessen Schnelligkeit auf großen Arrays sowie bereits sortierten Arrays" +
                "\n" +
                "Timsorts Worst,sowie average Case liegt in (n*log n). Der Best Case ist linear." +
                "\n" +
                "Eigentlich arbeitet der Algorithmus auf sehr großen Arrays, wodurch er seine komplette Schnelligkeit demonstriert. Wir haben es auf ein Array mit 16 Elementen eingestellt" +
                "\n" +
                "und den Minrun auf 4 gesetzt" +
                "\n" +
                "Das Array ist frei änderbar, sowie der Minrun. Allerdings kann die Übersichtlichkeit dadurch leiden." +
                "\n" +
                "";
    }

    public String getCodeExample(){
        return "Code Example";
    }

    public String getFileExtension(){
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
    }

    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

    @Override
    public boolean validateInput(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> hashtable) throws IllegalArgumentException {

        int [] intArraytmp = (int[])hashtable.get("intArray");
        int minruntmp = (int) hashtable.get("minrun");
        if(minruntmp < intArraytmp.length && 1 < intArraytmp.length)
            return true;
        else
            return false;
    }

    private void start() {

        lang.setStepMode(true);

        headerprop = new TextProperties();
        headerprop.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font(Font.SANS_SERIF,Font.BOLD,20));
        header = new Coordinates(20,30);
        prmlst = new ArrayList<>();

        struct();

        run();
        minrun();

        mergePattern();


        timSort(intArray);
        summary();

    }



    private void struct() {

        int y = 20;
        String chapter =  "Timsort - Struktur";

        TextProperties step = new TextProperties();
        step.set(AnimationPropertiesKeys.FONT_PROPERTY,new Font(Font.SANS_SERIF,Font.BOLD,15));

        //lang.hideAllPrimitives();

        lang.newText(header,"Timsort - Struktur",chapter,null, headerprop);
        lang.nextStep();

        lang.newText(new Offset(0,20,header, AnimalScript.DIRECTION_S),"Schritt 1.",chapter,null,step);
        lang.newText(new OffsetFromLastPosition(0,y),"Im ersten Schritt wird erklärt, was ein Run und Minrun ist und wie man diese berechnet",chapter,null,textprop);
        //lang.newText(new OffsetFromLastPosition(0,y),"und wie man diese berechnet",chapter,null,textprop);
        lang.nextStep();
        lang.newText(new OffsetFromLastPosition(0,y),"Schritt 2.",chapter,null,step);
        lang.newText(new OffsetFromLastPosition(0,y),"Im zweiten Schritt wird das Merge Pattern vorgestellt",chapter,null,textprop);
        lang.nextStep();
        lang.newText(new OffsetFromLastPosition(0,y),"Schritt 3.",chapter,null,step);
        lang.newText(new OffsetFromLastPosition(0,y),"Im dritten Schritt wird der Merge Algorithmus erklärt",chapter,null,textprop);
        lang.nextStep();
        lang.newText(new OffsetFromLastPosition(0,y),"Schritt 4.",chapter,null,step);
        lang.newText(new OffsetFromLastPosition(0,y),"Zum Schluss zeigen wir dann noch ein generisches Beispiel um den Algorithmus visuell näher zu bringen",chapter,null,textprop);
        lang.nextStep();

    }


    private void run() {

        int y = 20;
        String chapter =  "Timsort - Run";

        int [] intArray = this.intArray.clone();

        lang.hideAllPrimitives();
        Text headtxt = lang.newText(header,"Timsort - Run",chapter,null, headerprop);
        lang.newText(new Offset(0,20,header, AnimalScript.DIRECTION_S),"Timsort unterteilt das zu sortierende Array in 'Runs' ",chapter,null,textprop);

        //arrayProp.set(AnimationPropertiesKeys.FILL_PROPERTY,Color.white);

        lang.newText(new OffsetFromLastPosition(0,y),"dabei wird in dem zu sortierenden Array nach vorhandenen sortierte Teilsequenzen gesucht, sogenannte 'Natural Runs",chapter,null,textprop);
        //lang.newText(new OffsetFromLastPosition(0,y),"sortierte Teilsequenzen gesucht, sogenannte 'Natural Runs' ",chapter,null,textprop);
        lang.nextStep();

        IntArray tmp = lang.newIntArray(new OffsetFromLastPosition(0,35),intArray,"intArray",null,arrayProp);
        prmlst.add(tmp);
        lang.nextStep();

        boolean reverse = false;

        int i = 0;

        if(i != tmp.getLength() && tmp.getData(i) <= tmp.getData(i+1)){
            tmp.highlightCell(i,null,Timing.MEDIUM);
            i++;
            lang.nextStep();
            while(i < tmp.getLength() && tmp.getData(i) <= tmp.getData(i+1)){
            tmp.highlightCell(i,null,Timing.MEDIUM);
            lang.nextStep();
            i++;
            }
            if(i < tmp.getLength()){
                tmp.highlightCell(i,null,Timing.MEDIUM);
            }
        }
        else{
            tmp.highlightCell(i,null,Timing.MEDIUM);
            i++;
            lang.nextStep();
            while(i < tmp.getLength() && tmp.getData(i) > tmp.getData(i+1)){
                tmp.highlightCell(i,null,Timing.MEDIUM);
                lang.nextStep();
                i++;
            }
            if(i < tmp.getLength()){
                tmp.highlightCell(i,null,Timing.MEDIUM);
            }
            lang.nextStep();
            reverse = true;
        }
        lang.nextStep();

        lang.newText(new OffsetFromLastPosition(0,y + 40),"Diese können entweder absteigend oder aufsteigende sortierte Teilsequenzen sein.",chapter,null,textprop);
        lang.newText(new OffsetFromLastPosition(20,y),"Aufsteigend: element1 <= element2 <= element3 <= elementn",chapter,null,textprop);
        lang.newText(new OffsetFromLastPosition(0,y),"Absteigend: element1 > element2 > element3 > elementn",chapter,null,textprop);

        lang.nextStep();
        lang.newText(new OffsetFromLastPosition(-20,y),"Bei einer absteigenden Sequenz muss der Natural Run anschließend umgedreht werden",chapter,null,textprop);
        lang.nextStep();
        if(reverse)
            reverseRange(intArray,tmp,0,++i);
        lang.nextStep();
        lang.hideAllPrimitives();
        tmp.hide();
    }

    private void minrun() {

        String chapter =  "Timsort - Minrun";

        Text headtxt = lang.newText(header,"Timsort - Minrun",chapter,null, headerprop);

        lang.nextStep();
        lang.newText(new OffsetFromLastPosition(0,20),"Im vorherigen Schritt haben wir ein Natural Run gesucht, der ein Teil von einem Run ist",chapter,null,textprop);
        lang.newText(new OffsetFromLastPosition(0,20),"Ein Run hat eine Mindestlänge. Es wird 'Minrun' genannt",chapter,null,textprop);
        lang.newText(new OffsetFromLastPosition(0,20),"Entspricht der Natural Run nicht gleich dem Minrun so muss der Natural Run erweitert werden",chapter,null,textprop);
        lang.newText(new OffsetFromLastPosition(0,20),"Dazu wählt man ein binary insertion sort, welches den Natural Run erweitert",chapter,null,textprop);
        lang.nextStep();
        lang.hideAllPrimitives();
        headtxt.show();

        lang.newText(new Offset(0,20,headtxt,AnimalScript.DIRECTION_W),"Die Frage ist nun, wie wählt man den Minrun?",chapter,null,textprop);
        lang.nextStep();
        lang.newText(new OffsetFromLastPosition(0,20),"Mit N = größe des Arrays",chapter,null,textprop);
        lang.newText(new OffsetFromLastPosition(0,20),"Ist N < 64 ,  so ist der Minrun gleich N und wir verwenden ein binary insertion sort",chapter,null,textprop);
        lang.newText(new OffsetFromLastPosition(0,20),"um den Rest des Arrays zu sortieren",chapter,null,textprop);
        lang.nextStep();
        lang.newText(new OffsetFromLastPosition(0,20),"Wenn N >= 64 ist verwenden wir folgenden Algorithmus,",chapter,null,textprop);

        // properties for the source code
        SourceCodeProperties scProps = new SourceCodeProperties();
        scProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        scProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("Monospaced", Font.PLAIN, 12));
        scProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.BLUE);
        scProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.RED);

        SourceCode sc = lang.newSourceCode(new OffsetFromLastPosition(0,40), "sourceCode", null, scProps);

        sc.addCodeLine("private int minRunLength(n){", null, 0, null);
        sc.addCodeLine("assert n >= 0", null, 1, null);
        sc.addCodeLine("int r = 0;", null, 1, null);

        sc.addCodeLine("while (n >= 64) {", null, 1, null);
        sc.addCodeLine("r |= (n & 1);", null, 2, null);
        sc.addCodeLine(" n >>= 1", null, 2, null);
        sc.addCodeLine("}", null, 1, null);
        sc.addCodeLine("return n + r;", null, 1, null);
        sc.addCodeLine("}", null, 0, null);
        lang.nextStep();

        //Text text1= lang.newText(new Offset(230,185,sc.getUpperLeft(),AnimalScript.DIRECTION_E),"Hier werden nun die ersten 6 Bits (MSB) von N genommen",chapter,null,textprop);
        Text text1= lang.newText(new Coordinates(230,185),"Hier werden nun die ersten 6 Bits von N genommen",chapter,null,textprop);
        Text text2 = lang.newText(new OffsetFromLastPosition(0,20),"Setze r auf 1, wenn einer der ersten 6 Bits gesetzt ist",chapter,null,textprop);
        Text text3 = lang.newText(new OffsetFromLastPosition(0,20),"Sprich",chapter,null,textprop);
        Text text4 = lang.newText(new OffsetFromLastPosition(0,20),"Sobald n eine ungerade Zahl ist wird r auf 1 gesetzt, bei einer geraden Zahl bleibt r gleich 0",chapter,null,textprop);


        sc.highlight(3);
        sc.highlight(4);
        sc.highlight(5);


        lang.nextStep();

        text1.hide();
        text2.hide();
        text3.hide();
        text4.hide();

        sc.unhighlight(3);
        sc.unhighlight(4);
        sc.unhighlight(5);

        lang.newText(new Offset(230,185,sc.getUpperLeft(),AnimalScript.DIRECTION_SE),"Aufgrund von Performanzgründen ist es am besten",chapter,null,textprop);
        lang.newText(new OffsetFromLastPosition(0,20),"wenn der Minrun eine zweier Potenz ist oder",chapter,null,textprop);
        lang.newText(new OffsetFromLastPosition(0,20),"zumindestens sich in dessen nähe befindet",chapter,null,textprop);
        lang.newText(new OffsetFromLastPosition(0,20),"Man versucht dadurch die kleinstmöglcihe Anzahl von Runs zu erzeugen",chapter,null,textprop);
        lang.newText(new OffsetFromLastPosition(0,20),"um beim mergen weniger Elemente vergleichen zu müssen",chapter,null,textprop);



        lang.nextStep();
    }





    private void mergePattern() {

        lang.hideAllPrimitives();
        String chapter = "Timsort - Merge Pattern";

        LinkedList<String> tmp = new LinkedList<>();

        Text headertxt = lang.newText(header,"Timsort - Merge Pattern",chapter,null, headerprop);
        lang.nextStep();

        lang.newText(new OffsetFromLastPosition(0,30),"Die Position des Runs im Array und dessen Länge wird auf dem Stack gespeichert",chapter,null,textprop);
        lang.newText(new OffsetFromLastPosition(0,30),"Dabei ist es wichtig, das die Längen der Runs auf dem Stack folgenden Bedingungen unterliegen",chapter,null,textprop);
        lang.nextStep();
        lang.newText(new OffsetFromLastPosition(0,30),"A,B und C sind die Längen der Runs",chapter,null,textprop);
        lang.newText(new OffsetFromLastPosition(20,30),"Bedingung 1. A > B + C",chapter,null,textprop);
        lang.newText(new OffsetFromLastPosition(20,30),"Bedingung 2. B > C",chapter,null,textprop);
        lang.nextStep();
        lang.newText(new OffsetFromLastPosition(150,30),"Hier ein Beispiel:",chapter,null,textprop);
        lang.newText(new OffsetFromLastPosition(150,0),"A:100.000    B:80.000    C:120.000 ",chapter,null,textprop);

        tmp.add("A:(posA,100.000)");
        tmp.add("B:(posB,80.000)");
        tmp.add("C:(posC,120.000)");

        StackProperties stackProps = new StackProperties();
        //
        stackProps.set(AnimationPropertiesKeys.DIVIDINGLINE_COLOR_PROPERTY,Color.BLACK);


        lang.nextStep();
        lang.newText(new OffsetFromLastPosition(0,20),"Da hier die zweite Bedingung verletzt wird, muss B und C zu BC gemerged werden",chapter,null,textprop);
        lang.newText(new OffsetFromLastPosition(0,20),"Daraus folgt A:100.000 und BC:200.000",chapter,null,textprop);
        stack = lang.newConceptualStack(new Coordinates(200,240),tmp,"Stack",null,stackProps);
        lang.nextStep();
        stack.pop(null,Timing.MEDIUM);
        stack.pop(null,Timing.MEDIUM);

        stack.push("BC:(posB,200.000)",null,Timing.MEDIUM);
        lang.nextStep();
        lang.newText(new OffsetFromLastPosition(20,20),"Nun ist auch die erste Bedingung und wir machen einen weiteren merge ",chapter,null,textprop);
        lang.newText(new OffsetFromLastPosition(0,20),"Daraus folgt ABC: 300.000",chapter,null,textprop);
        lang.nextStep();
        stack.pop(null,Timing.MEDIUM);
        stack.pop(null,Timing.MEDIUM);
        stack.push("ABC:(posA,300.000)",null,Timing.MEDIUM);
        lang.nextStep();

        lang.newText(new Offset(0,310,headertxt,AnimalScript.DIRECTION_W),"Das Merge Pattern wird aufgrund von Performanze Gründen ausgeführt.",chapter,null,textprop);
       // lang.newText(new OffsetFromLastPosition(0,20),"Das Ziel ist es den Stack möglichst klein zu halten, und kleinere Runs in größere Runs zu mergen",chapter,null,textprop);

        lang.nextStep();

        lang.hideAllPrimitives();
    }

    private void summary() {

        int y = 20;
        String chapter = "Timsort - Zusammenfassung";

        lang.hideAllPrimitives();
        intMatrix.hide();
        intMatrix2.hide();
        intMatrix3.hide();
        lang.newText(header, "Timsort - Zusammenfassung", chapter, null, headerprop);
        lang.nextStep();
        lang.newText(new OffsetFromLastPosition(0, y), "1. Entspricht die größe des Arrays der Mindestrgöße? Sonst verwende ein binary inerstion sort", chapter, null, textprop);
        lang.newText(new OffsetFromLastPosition(0, y), "2. Berechne den Minrun anhand der größe des Arrays", chapter, null, textprop);
        lang.newText(new OffsetFromLastPosition(0, y), "3. Suche nach Natural Runs", chapter, null, textprop);
        lang.newText(new OffsetFromLastPosition(0, y), "4. überprüfe ob Run >= Minrun ist, sonst erweitere ihn", chapter, null, textprop);
        lang.newText(new OffsetFromLastPosition(0, y), "5. Speichere die Anfangsposition und die Länge des Runs auf dem Stack", chapter, null, textprop);
        lang.newText(new OffsetFromLastPosition(0, y), "6. Merge die Runs nach den vorgegebenen Bedingungen", chapter, null, textprop);

        lang.nextStep();


    }

    //Timsort Algorithmus
    public void timSort(int [] intArray) {

        lang.hideAllPrimitives();

        int[][] intMatrixTmp= createMatrix(intArray,-1,-1);

        matrixProp.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,Color.RED);
        matrixProp.set(AnimationPropertiesKeys.GRID_STYLE_PROPERTY,"table");
        matrixProp.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,Color.BLACK);
        matrixProp.set(AnimationPropertiesKeys.FILL_PROPERTY,Color.WHITE);

        Text txt= (lang.newText(header,"Nehmen wir nun als Beispiel dieses Array","Beispiel",null, headerprop));

        prmlst.add(txt);


        lang.nextStep();

        intMatrix = lang.newIntMatrix(new OffsetFromLastPosition(20,100),intMatrixTmp,"intMatrix",null,matrixProp);
        prmlst.add(intMatrix);

        StackProperties stackProps = new StackProperties();
        //
        stackProps.set(AnimationPropertiesKeys.DIVIDINGLINE_COLOR_PROPERTY,Color.BLACK);
        //Stack new Coordinates(500,200)
        stack = lang.newConceptualStack(new Coordinates(500,200),stacklst,"Stack",null,stackProps);
        //stack.hide(); -> Gibt einen Fehler
        System.out.println("Ich bin minrun " + minrun);
        lang.nextStep();
        //intMatrix.show();
        stackSize = 0;
        meineFarbe = 0;
        this.intArray = intArray;
        int low = 0;
        int high = intArray.length;
        int nRemaining = high - low;
        //beginne Timsort
        while(nRemaining != 0 && low < high) {
            //berechne den nächsten MinRun
            intMatrix.show();
            int runlength = countRunLen(intArray, low, high);

            //Ist der Natural Run < als der minrun?
            if (runlength < minrun) {
                //Are enough Elements there for Minrun?
                int remaining = nRemaining < minrun ? nRemaining : minrun;
                //add Elements to the run
                if(remaining != 0)
                binarySort(intArray, low, low + remaining, low + runlength);
                runlength = remaining;
            }
           // lang.newText()
            setRun(low, runlength);

            validateSetRightInvariante();

            low += runlength;
            nRemaining -= runlength;

            //neuer Run, neue Farbe
            meineFarbe++;

        }

        endMerge();

        //geht nicht
        lang.nextStep();
        lang.newText(new Offset(0,40,intMatrix2,AnimalScript.DIRECTION_SW),"Nun ist das Array fertig sortiert, der Stack enthät die Startposition und die länge des einzigen Runs","Fin",null,textprop);
        //System.out.println();
        lang.nextStep();
    }




    private void endMerge() {
        while (stackSize > 1){
            int n = stackSize - 2;
            if(n>0 && runLen[n - 1] < runLen[n+1])
                n--;
            mergeAt(n);
        }

    }

    private void setRun(int base, int runlength) {
       // lang.hideAllPrimitivesExcept(prmlst.get(0));
        intMatrix.hide();
        StringBuilder tmp = new StringBuilder();
        stack.push(tmp.append("( ").append(base).append(",").append(runlength).append(" )").toString());
        //stack.show(); -> wenn du etwas auf den stack.pushst, wird er automatisch gezeigt
        Text text3 = lang.newText(new Offset(100,20,stack.getUpperLeft(),AnimalScript.DIRECTION_E),"Speichere den gefundenen Run auf dem Stack ab","IntrosetRun",null,textprop);
        Text text4 = lang.newText(new OffsetFromLastPosition(0,20),"Dabei ist (x, y) mit x = runBase  und y = runlength","IntrosetRun",null,textprop);
        baseRun[stackSize] = base;
        runLen[stackSize] = runlength;
        lang.nextStep();
        text3.hide();
        text4.hide();
        //increase Stack
        stackSize++;
    }

    private void validateSetRightInvariante() {
        //There are at least 2 Runs!
        Text text1 = lang.newText(new Offset(50, 100, stack.getUpperLeft(), AnimalScript.DIRECTION_SW), "Ist der Stack größer 2?", "Validate", null, textprop);
        while(stackSize > 1) {
            text1.hide();
            text1 = lang.newText(new Offset(50,100,stack.getUpperLeft(),AnimalScript.DIRECTION_SW),"Überprüfung der Bedingungen","Validate",null,textprop);
            System.out.println(text1.getUpperLeft());
            lang.nextStep();
            int n = stackSize - 2;
            //look, if the Conditions are valid

            Text text2 = lang.newText(new OffsetFromLastPosition(0, 20), "1. A > B + C?", "Validate", null, textprop);
            Text text3 = lang.newText(new OffsetFromLastPosition(0, 20), "2. A >  B", "Validate", null, textprop);
            lang.nextStep();
            Text text4;
            if (n > 0 && runLen[n - 1] <= runLen[n] + runLen[n + 1]){

                text3.hide();
                text4 = lang.newText(new OffsetFromLastPosition(0,20),stack.getStack().get(0) + " < " + stack.getStack().get(1) + " + " + stack.getStack().get(2),"Validate",null,textprop);
                lang.nextStep();
                //A < C?
                if (runLen[n - 1] < runLen[n + 1])
                    n--;

                mergeAt(n);
                text2.hide();
                text4.hide();
                //B <= C
            }else if(runLen[n] <= runLen[n+1]){
                text2.hide();
                text4 = lang.newText(new OffsetFromLastPosition(0,20),stack.getStack().get(0) + " <= " + stack.getStack().get(1),"Validate",null,textprop);
                lang.nextStep();

                mergeAt(n);
                text3.hide();
                text4.hide();
            } else{
                text2.hide();
                text3.hide();
                break;
            }
        intMatrix2.hide();
        }
        text1.hide();
        lang.nextStep();
    }


    private IntMatrix intMatrix2;
    private IntMatrix intMatrix3;

    /**
     *
     * @param n, welche Stelle im Stack
     */
    private void mergeAt(int n) {
        int base1 = baseRun[n];
        int runlen1 = runLen[n];

        int base2 = baseRun[n+1];
        int runlen2 = runLen[n+1];

        runLen[n] = runlen1 + runlen2;
        stack.pop();
        stack.pop();
        stack.push("( "+base1 +","+(runlen1+runlen2) + " )",null,null);
        if(n ==  stackSize - 3 ){
            baseRun[n+1] = baseRun[n+2];
            runLen[n+1] = runLen[n+2];
            stack.pop();
            stack.pop();
            stack.push("( "+ baseRun[n+2] +","+runLen[n+2] + " )",null,null);

        }
        stackSize--;

        Text tmpT1;
        Text tmpT2;

        if(runlen1 <= runlen2){
            tmpT1 = lang.newText(new Offset(0,100,intMatrix,AnimalScript.DIRECTION_SW),"run "+ (n+1),"mergeAt",null,textprop);
        //intMatrix2 = lang.newIntMatrix(new Offset(0,100,intMatrix,AnimalScript.DIRECTION_SW),createMatrix(base1,runlen1),"intArray2",null,matrixProp);
            intMatrix2 = lang.newIntMatrix(new OffsetFromLastPosition(0,50),createMatrix(base1,runlen1),"intArray2",null,matrixProp);

            tmpT2 = lang.newText(new OffsetFromLastPosition(0,100),"run"+ (n+2),"mergeAt",null,textprop);
            intMatrix3 = lang.newIntMatrix(new OffsetFromLastPosition(0,50),createMatrix(base2,runlen2),"intArray3",null,matrixProp);
        }
        else{
            tmpT1 = lang.newText(new Offset(0,100,intMatrix,AnimalScript.DIRECTION_SW),"run "+ (n+1),"mergeAt",null,textprop);
            intMatrix2 = lang.newIntMatrix(new OffsetFromLastPosition(0,50),createMatrix(base1,runlen1),"intArray2",null,matrixProp);
            tmpT2 = lang.newText(new OffsetFromLastPosition(0,100),"run "+ (n + 2),"mergeAt",null,textprop);
            intMatrix3 = lang.newIntMatrix(new OffsetFromLastPosition(0,50),createMatrix(base2,runlen2),"intArray3",null,matrixProp);

        }
        lang.nextStep();

        if(runlen1 <= runlen2)
            //links nach rechts
            mergeLow(base1, runlen1, base2, runlen2);
          //  insertionSort(intMatrix3,intMatrix2);
        else
           // insertionSort(intMatrix2,intMatrix3);
            //rechts nach links
            mergeHigh(base1,runlen1,base2,runlen2);
        lang.nextStep();
        intMatrix2.hide();
        intMatrix2 = lang.newIntMatrix(new Offset(0,100,intMatrix,AnimalScript.DIRECTION_SW),createMatrix(base1,runlen1 + runlen2),"intArray2",null,matrixProp);
        intMatrix3.hide();
        tmpT1.hide();
        tmpT2.hide();

        //setColorMatrix(this.intMatrix);

        lang.nextStep();
        intMatrix2.hide();
    }

    /**
     * Geht noch nicht, weiß nicht wieso
     * @param intMatrix
     */
    private void setColorMatrix(IntMatrix intMatrix) {
        meineFarbe = 0;
        for (int i = 0; i < baseRun.length; i++) {

            int base = baseRun[i];
            int length = runLen[i];
            System.out.println("base " + base + " " + "length " + length );
            while(base < length){
                intMatrix.unhighlightCell(0,base,null,null);
                intMatrix.setGridHighlightFillColor(0,base++,getColor(meineFarbe),null,null);
                System.out.println("base " + base);
            }
            meineFarbe++;
        }
    }

    private int[][] createMatrix(int base, int run) {

        return createMatrix(null,base,run);
    }


    private int[][] createMatrix(int[] intArray,int base,int run) {
        int[][] intMatrix2;

        if(base < 0 || intArray != null) {

            intMatrix2 = new int[1][intArray.length];

            System.arraycopy(intArray, 0, intMatrix2[0], 0, intArray.length);
        }else{


            intMatrix2 = new int[1][run];
            int j = 0;

            for (int i = base; i < base+run ; i++) {

                intMatrix2[0][j] = this.intMatrix.getElement(0,i);
                j++;
            }
        }


        return intMatrix2;
    }


    /**
     *
     * @param base1
     * @param runlen1
     * @param base2
     * @param runlen2
     */
    private void merge(int base1, int runlen1, int base2, int runlen2) {


        for (int i = base1; i < base2; i++){
            int pivot = intArray[i];

            int left = base2;
            int right = base2 + runlen2 -1;

            while (left < right){
                int middle = (left + right) >>> 1;

                if (pivot < intArray[middle]){
                    right = middle;
                }else{
                    left = middle+1;
                }
            }

            // left == right
            System.arraycopy(intArray, base1+1, intArray, base1, runlen1-1);
            runlen1--;
            int n = left - base2;
            System.arraycopy(intArray, runlen1, intArray, base2, n);
            intArray[left-1] = pivot;
        }
    }
    private int binsearchForInsertion(int[] array, int target, int left, int right)    {

        int start = left;               // The start of the search region
        int end   = right;              // The end of the search region
        int position = -1;           // Position of the target

        // While there is still something list left to search and
        // the element has not been found
        while (start <= end && position == -1)	{
            int mid = start + (end - start) / 2;
            //	    int mid = (start + end) / 2;  // Location of the middle

            // Determine whether the target is smaller than, greater than,
            // or equal to the middle element
            if (target < array[mid])   {
                // Target is smaller; continue the left half
                end = mid - 1;
            }
            else if (target > array[mid])  {
                // Target is larger, continue the right half
                start = mid + 1;
            }
            else  {
                // Found it!
                position = mid;
            }
        }

        if (position == -1) { // the target is not in, find the position
            // to insert
            position = start;
            while (position < end && array[position] < target)
                position ++;
            position --; // insert after the position
        }

        return position;
    }
    // merge von links nach rechts

    private void mergeLow(int base1, int runlen1, int base2, int runlen2){
        int [] tmp = new int[runlen1];
        // copy first subarray to tmp
        System.arraycopy(intArray,base1,tmp,0,runlen1);
        //versetze Base2 zu Base1;
        int left = base2;
        int right = base2+runlen2-1;

        for (int i = 0; i < runlen1; i++){
            int pivot = tmp[i];

            int index = binsearchForInsertion(intArray, pivot, left, right);

            System.arraycopy(intArray, base2, intArray, base2-1, index +1 - base2);
            arraycopyAnimal(intMatrix,base2,intMatrix,base2-1,index+1 - base2);

            intMatrix.put(0,index,pivot,null,null);
            intArray[index] = pivot;

            base2--;
        }
    }



    private void mergeHigh(int base1, int runlen1, int base2, int runlen2){
        int [] tmp = new int[runlen2];
        // copy second subarray to tmp
        System.arraycopy(intArray,base2,tmp,0,runlen2);
        //versetze Base2 zu Base1;
        int left = base1;
        int right = base1+runlen1-1;

        for (int i = 0; i < runlen2; i++) {
            int pivot = tmp[i];

            int index = binsearchForInsertion(intArray, pivot, left, right);

            int destPos = index + 2;

            int length = base1 + runlen1 - index - 1;

            if (length > 0) {
                System.arraycopy(intArray, index + 1, intArray, destPos, length);
                arraycopyAnimal(intMatrix,index + 1,intMatrix,destPos,length);
            }
            intArray[index+1] = pivot;
            intMatrix.put(0,index+1,pivot,null,null);
            right++;
            runlen1++;
        }
    }

    private int countRunLen(int[] intArray, int low, int high) {
        assert low < high;
        Text tmp = lang.newText(new Offset(0,-20,intMatrix.getName(),AnimalScript.DIRECTION_N),"Suche des nächsten Natural Runs","IntroCountRunLen",null,textprop);
        Text tmp2;
        lang.nextStep();
        //start is always 1
        int runSize = low + 1;
        //erstes Element
     //   intMatrix.setGridColor(0, low, Color.RED, null, null);
     //   intMatrix.setGridFillColor(0,low,Color.BLUE,null,null);
        if(low+1 < high && intArray[low] <= intArray[low+1]){
            tmp2 = lang.newText(new Offset(0,15,intMatrix.getName(),AnimalScript.DIRECTION_S),"Es wird nach aufsteigende Natural Runs geschaut","CountRunLen",null,textprop);
            while(runSize < high && intArray[runSize -1] <= intArray[runSize]) {

                intMatrix.setGridColor(0, runSize-1, Color.RED, null, null);
                intMatrix.setGridFillColor(0,runSize-1,getColor(meineFarbe),null,null);

                lang.nextStep();
                runSize++;
            }
            //Highlighte das letzte Element
            intMatrix.setGridColor(0, runSize-1, Color.RED, null, null);
            intMatrix.setGridFillColor(0,runSize-1,getColor(meineFarbe),null,null);

        }else{
            tmp2 = lang.newText(new Offset(0,15,intMatrix.getName(),AnimalScript.DIRECTION_S),"Es wird nach absteigenden Natural Runs geschaut","CountRunLen",null,textprop);
            while (runSize < high && intArray[runSize -1] >= intArray[runSize]) {
                intMatrix.setGridColor(0, runSize-1, Color.RED, null, null);
                intMatrix.setGridFillColor(0,runSize-1,getColor(meineFarbe),null,null);
                lang.nextStep();
                runSize++;
            }
            //Highlighte das letzte Element
            intMatrix.setGridColor(0, runSize-1, Color.RED, null, null);
            intMatrix.setGridFillColor(0,runSize-1,getColor(meineFarbe),null,null);
            lang.nextStep();
            tmp2.hide();
            tmp2 = lang.newText(new Offset(0,15,intMatrix.getName(),AnimalScript.DIRECTION_S),"Der absteigende Run wird umgedreht","CountRunLen",null,textprop);
            reverseRange(intArray,low,runSize);

        }
        lang.nextStep();
        tmp2.hide();
        tmp.hide();
        return runSize - low;
    }

    private void binarySort(int [] intArray, int low, int high,int start) {
        assert (low <= start && start <= high);
        if (low == start)
            start++;
        Text txtTmp = lang.newText(new Offset(0, 20, intMatrix.getName(), AnimalScript.DIRECTION_S), "Erweitere den Run auf die Größe des Minrun", "binarySortFill", null, textprop);
            for (; start < high; start++) {
                int tmp = intArray[start];
                int left = low;
                int right = start;
                assert left <= right;

                while (left < right) {
                    int mid = (left + right) >>> 1;
                    if (tmp < intArray[mid])
                        right = mid;
                    else
                        left = mid + 1;
                }
                assert left == right;

                int n = start - left;
                lang.nextStep();
                System.arraycopy(intArray, left, intArray, left + 1, n);

                intMatrix.setGridColor(0, left + n, Color.RED, null, null);
                intMatrix.setGridFillColor(0, left + n, getColor(meineFarbe), null, null);
                arrayShift(intMatrix,left, n);
                intArray[left] = tmp;
            }

//        lang.hideAllPrimitivesExcept(prmlst);
        lang.nextStep();
        txtTmp.hide();
    }

    private void arrayShift(IntMatrix intMatrix,int from,int elemente){
        assert from+elemente < intArray.length;
        int start = from + elemente - 1;
        //laufe von hinten nach vorne und tausche
        while(from <= start){

            intMatrix.swap(0,start,0,start+1,null,null);
            start--;
        }

    }

    /**
     * Swap Elements in int [] intArray and swap in IntArray array
     * @param intArray
     * @param array
     * @param lo
     * @param hi
     */
    private void reverseRange(int [] intArray,IntArray array, int lo,int hi){

        hi--;
        while (lo < hi) {
            array.swap(lo,hi,null,null);
            int tmp = intArray[lo];
            intArray[lo] = intArray[hi];
            intArray[hi] = tmp;
            lo++;
            hi--;
        }
    }

    private void reverseRange(int [] intArray,int lo,int hi) {

        hi--;
        while (lo < hi) {
            this.intMatrix.swap(0, lo, 0, hi, null, null);
            int tmp = intArray[lo];
            intArray[lo] = intArray[hi];
            intArray[hi] = tmp;
            lo++;
            hi--;
        }
    }

    private void swap(int [] intArray, int pos1,int pos2){
        int tmp = intArray[pos1];
        intArray[pos1] = intArray[pos2];
        intArray[pos2] = tmp;
    }


    private Color getColor(int i){

        switch (i){

            case 0: return Color.YELLOW;
            case 1: return Color.BLUE;
            case 2: return Color.GREEN;
            case 3: return Color.ORANGE;
            case 4: return Color.LIGHT_GRAY; //Magenta ?  Really fancy
            case 5: return Color.CYAN;
            case 6: return Color.RED;
            default:
                meineFarbe = 0;
                return Color.PINK; //If its Pink, than meineFarbe = 0
        }
    }


    /**
     *
     * @param src
     * @param srcBase
     * @param dest
     * @param destBase
     * @param length
     */
    private void arraycopyAnimal(IntMatrix src, int srcBase,IntMatrix dest, int destBase, int length){

        int [] tmpArray = new int[length];
        ///Möchte Shiften
        for (int t = 0; t < length; t++) {
            tmpArray[t] = src.getElement(0,srcBase + t);

        }
        int j = 0;
        while(j < length){
            dest.put(0,destBase,tmpArray[j],null,null);
            destBase++;
            j++;
        }
    }

}


