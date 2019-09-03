package generators.sorting;

import algoanim.exceptions.LineNotExistsException;
import algoanim.primitives.*;
import algoanim.primitives.generators.AnimationType;
import algoanim.properties.*;
import algoanim.util.*;
import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.awt.*;
import java.util.Locale;

import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.ValidatingGenerator;
import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;
import interactionsupport.models.MultipleChoiceQuestionModel;

@SuppressWarnings("unused")
public class LibGenerator implements ValidatingGenerator {
    Language lang;
    private Color _farbHighlight;
    private int[] a;
    private int epsilon;
    private SourceCode introSrc;
    private SourceCode src;
    private SourceCode srcSf;
    private SourceCode srcR;
    private Polyline pl;


    private SourceCodeProperties sourceCodeProps;
    private ArrayProperties arrayProps;
    private RectProperties rectProps;
    private TextProperties textProps;
    private TextProperties textRoundCounterProps;
    private SourceCodeProperties introSrcProps;
    private Text roundCounter;
    private ArrayMarkerProperties arrayMarkerNextFreeProperties;

    public void init() {
        lang = new AnimalScript("Library Sort", "Jan Rathjens", 800, 600);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);
        lang.setStepMode(true);
    }

    public boolean validateInput(AnimationPropertiesContainer var1, Hashtable<String, Object> var2) {


        int e = (int)var2.get("epsilon");

            if (e < 1) {
                    throw new IllegalArgumentException("Epsilon must be a positive Integer above 0");
                }
        return true;
    }
    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        setParameters(primitives);
        setProperties();

        Text header = lang.newText(new Coordinates(20, 30), "Library Sort", "header", null, textProps);
        displayIntroSrc();
        Rect hRect = lang.newRect(new Offset(-5, -5, "header", AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",
                null, rectProps);
        lang.nextStep("Intro");


        introSrc.hide();
        displayLibrarySortCode();
        displaySearchFreeCode();
        //displayRebalanceCode();
        displayRoundCounter();
        Coordinates a1 = new Coordinates(580, 75);
        Coordinates b1 = new Coordinates(580, 1200);
        Node[] nodes = new Node[2];
        nodes[0] = a1;
        nodes[1] = b1;
        pl = lang.newPolyline(nodes, "trennlinie", null);

        try {
            sort(a, epsilon);
        } catch (LineNotExistsException e) {
            e.printStackTrace();
        }
        pl.hide();
        src.hide();
        srcSf.hide();
        roundCounter.hide();
        lang.nextStep("Conclusion");
        Text fazit = lang.newText(new Coordinates(20, 100), "Conclusion",
                "header", null, textProps);
        SourceCode fazitSrc = lang.newSourceCode(new Coordinates(20, 175), "conclusion",
                null, sourceCodeProps);
        fazitSrc.addCodeLine("Library Sort increases Insertion Sorts's complexity significantly. ", null, 0, null);
        fazitSrc.addCodeLine("However, the storage must be extended, since it needs an extra array of large size.", null,0,null);
        lang.finalizeGeneration();
        return lang.toString();

    }

    void sort(int []a, int epsilon) {
        int n = a.length;
        String [] sa = new String[n];
        for(int i = 0; i<n;i++){
            sa[i] = String.valueOf(a[i]);
        }
        StringArray ia= lang.newStringArray(new Coordinates(600, 100), sa, "intArray",
                null, arrayProps);
        lang.nextStep("Start Sorting");
        src.highlight(0);
        lang.nextStep();
        src.unhighlight(0);
        src.highlight(1);
        String []s = new String[(1+epsilon)*n];
        for(int i = 0; i <s.length;i++){
            s[i] = NONE;
        }
        StringArray is= lang.newStringArray(new Coordinates(600, 200), s, "intArray",
                null, arrayProps);

        libSort(ia, n, is, epsilon);

    }





    void libSort(StringArray a, int N, StringArray s, int epsilon) {

        if(N==0) return;

        int j, k, step;
        int goal = 2;
        int pos = 0;
        int sLen = Math.max((1+epsilon), goal + 1);

        // ------ CONDITION -------
        //What has already been read must be less than the total array size
        boolean isFirstRound = true;
        ArrayMarkerProperties arrayMarkerProperties = new ArrayMarkerProperties();
        arrayMarkerProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.CYAN);
        int round = 0;
        displayMC1();
        lang.nextStep();

        while(pos<N) {
            round++;
            lang.nextStep();
            src.unhighlight(1);
            src.highlight(2);
            incrementRoundCounter();
            src.unhighlight(7);
            src.unhighlight(5);


            // ------ ROUND ------
            //Each round i will end with goal=2^i sorted lements. i starts with 1
            //  ArrayMarker arrayMarker = lang.newArrayMarker(a,pos,"laad",null,arrayMarkerProperties);
            for(j=goal/2;j<goal;j++) {
                if(isFirstRound == true){
                    j--;
                    isFirstRound = false;
                }
                lang.nextStep();
                for(int i = goal/2; i < goal; i++){
                    if(goal == 2){
                        a.highlightCell(0,null,null);
                    }
                    a.highlightCell(i,null,null);
                }
                src.unhighlight(4);
                src.unhighlight(5);
                src.unhighlight(2);
                src.highlight(3);
                src.unhighlight(7);


                lang.nextStep();
                a.setHighlightBorderColor(pos,Color.CYAN,null,null);
                a.setHighlightTextColor(pos,Color.CYAN,null,null);
                a.highlightElem(pos,null,null);
                src.unhighlight(3);
                src.highlight(4);
                srcSf.highlight(0);
                lang.nextStep();
                srcSf.unhighlight(0);
                srcSf.highlight(1);
                //Search where to insert A[pos] (with binary search)
                int insPos = searchFree(Integer.parseInt(a.getData(pos)), s, (1+epsilon)*(int)Math.pow(2.0,(double)round)-1);
                lang.nextStep();
                srcSf.unhighlight(1);
                srcSf.highlight(2);
                ArrayMarker arrayMarkerInsPos;
                lang.nextStep();
                if(insPos == -1){
                    srcSf.highlight(4);
                    srcSf.unhighlight(2);
                    arrayMarkerInsPos = lang.newArrayMarker(s,insPos,"latoad",null,arrayMarkerProperties);
                } else {
                    srcSf.unhighlight(2);
                    srcSf.highlight(3);
                    if(sLen == insPos){
                        arrayMarkerInsPos = lang.newArrayMarker(s,0,"latoad",null,arrayMarkerProperties);
                        arrayMarkerInsPos.moveOutside(null,null);
                    } else {
                        arrayMarkerInsPos = lang.newArrayMarker(s, insPos + 1, "latoad", null, arrayMarkerProperties);
                    }
                }


                lang.nextStep();
                srcSf.unhighlight(3);
                srcSf.unhighlight(4);
                srcSf.highlight(5);

                //Because our binary search returns us the location of an smaller item than the one we search...


                insPos ++;

                boolean animateAtEnd= true;
                ArrayMarker arrayMarkerNextFree;
                if(insPos < s.getLength() &&!IS_EMPTY(s.getData(insPos))) {//There is no place where we wanted to insert that element
                    animateAtEnd = false;
                    srcSf.highlight(5);
                    lang.nextStep();
                    srcSf.highlight(9);
                    srcSf.unhighlight(5);
                    lang.nextStep();
                    int nextFree = insPos + 1;//Search a free space forward
                    arrayMarkerNextFree = lang.newArrayMarker(s,nextFree,"nextFreeMakrer",null,arrayMarkerNextFreeProperties);
                    srcSf.unhighlight(9);
                    srcSf.highlight(10);
                    while(nextFree < sLen && !IS_EMPTY(s.getData(nextFree))){
                        nextFree ++;
                        lang.nextStep();
                        arrayMarkerNextFree.move(nextFree,null,null);
                    }
                    //At 'nextFree' there is a place, translate all elements one position to the right
                    if(nextFree>=sLen) {//Wait! nextFree is out of bounds
                        arrayMarkerNextFree.moveOutside(null,null);
                        lang.nextStep();
                        srcSf.highlight(11);
                        srcSf.unhighlight(10);
                        lang.nextStep();
                        insPos --;
                        srcSf.highlight(12);
                        srcSf.unhighlight(11);
                        arrayMarkerInsPos.move(insPos,null,null);

                        lang.nextStep();
                        srcSf.unhighlight(12);
                        srcSf.highlight(13);
                        arrayMarkerNextFree.move(insPos-1,null,null);
                        if(!IS_EMPTY(s.getData(insPos))) {
                            //Search backward
                            nextFree = insPos - 1;
                            arrayMarkerNextFree.move(nextFree,null,null);
                            arrayMarkerNextFree.show();
                            while(!IS_EMPTY(s.getData(nextFree))){
                                nextFree --;
                                lang.nextStep();
                                arrayMarkerNextFree.move(nextFree,null,null);
                            }
                            //Now we translate all the elements to the left
                            lang.nextStep();
                            srcSf.highlight(14);
                            srcSf.unhighlight(13);
                            for(int i = insPos; i >= nextFree;i--){
                                s.highlightElem(i,null,null);
                            }
                            lang.nextStep();
                            while(nextFree<insPos) {
                                s.put(nextFree,s.getData(nextFree +1),null,null);
                                nextFree ++;
                            }
                            s.put(insPos,NONE,null,null);
                            for(int i = s.getLength()-1; i > -1;i--){
                                s.unhighlightElem(i,null,null);
                            }

                        }
                    } else {
                        //Now we translate all the elements to the right
                        lang.nextStep();
                        srcSf.highlight(11);
                        srcSf.unhighlight(5);
                        srcSf.unhighlight(10);
                        lang.nextStep();
                        srcSf.highlight(15);
                        srcSf.unhighlight(11);
                        for(int i = nextFree; i >= insPos;i--){
                            s.highlightElem(i,null,null);
                        }
                        lang.nextStep();
                        while(nextFree>insPos) {
                            s.put(nextFree,s.getData(nextFree-1),null,null);
                            nextFree --;
                        }
                        s.put(insPos,NONE,null,null);
                        for(int i = s.getLength()-1; i > -1;i--){
                            s.unhighlightElem(i,null,null);
                        }

                    }
                    arrayMarkerNextFree.hide();
                    //Now nextFree is insPos; in other words, insPos is free
                } else if(insPos>=sLen) {//insPos is out of bounds
                    lang.nextStep();
                    srcSf.unhighlight(5);
                    srcSf.highlight(6);

                    //Search a free space backwards
                    insPos --;//This place must be between the limits
                    arrayMarkerInsPos.move(insPos,null,null);
                    lang.nextStep();
                    srcSf.highlight(7);
                    srcSf.unhighlight(6);
                    int nextFree = insPos - 1;
                    arrayMarkerNextFree = lang.newArrayMarker(s,nextFree,"nextFreeMakrer",null,arrayMarkerNextFreeProperties);

                    while(!IS_EMPTY(s.getData(nextFree))) {
                        nextFree --;
                        lang.nextStep();
                        arrayMarkerNextFree.move(nextFree,null,null);
                    }
                    //Now we translate all the elements to the left

                    lang.nextStep();
                    for(int i = insPos; i >= nextFree;i--){
                        s.highlightElem(i,null,null);
                    }
                    lang.nextStep();
                    while(nextFree<insPos) {
                        s.put(nextFree,s.getData(nextFree+1),null,null);
                        nextFree ++;
                    }
                    s.put(insPos,NONE,null,null);
                    lang.nextStep();
                    arrayMarkerNextFree.hide();
                    //Now nextFree is insPos; in other words insPos is free
                }
                lang.nextStep();
                srcSf.unhighlight(5);
                if(animateAtEnd){
                    srcSf.highlight(9);
                    lang.nextStep();
                    srcSf.unhighlight(9);

                }
                srcSf.highlight(16);
                srcSf.unhighlight(15);
                s.setHighlightFillColor(insPos,Color.GREEN,null,null);
                s.highlightCell(insPos,null,null);

                s.put(insPos, a.getData(pos),null,null);//We insert the element and increment our counter
                a.setHighlightFillColor(pos,Color.GREEN,null,null);
                a.unhighlightElem(pos,null,null);
                a.setHighlightBorderColor(pos,Color.BLACK,null,null);
                lang.nextStep();
                arrayMarkerInsPos.hide();
                pos++;

                src.unhighlight(4);
                src.highlight(5);
                srcSf.unhighlight(16);
                for(int i = 0; i<s.getLength();i++) {
                    s.unhighlightCell(i,null,null);
                }

                //   a.setHighlightBorderColor(pos,Color.BLACK,null,null);
                if(pos>=N) {
                    lang.nextStep();
                    src.unhighlight(5);
                    src.highlight(6);
                    lang.nextStep();
                    a.hide();
                    s.hide();
                    return;
                }//That element was the last, return from the function

            }

            lang.nextStep();
           // srcR.highlight(0);
            src.highlight(7);
            src.unhighlight(5);
            // arrayMarker.hide();
            // ----- REBALANCE -----
            //It takes linear time. Tries to spread the elements as much as possible

            for(int i =sLen -1; i>=0;i++){


            }

         /*   for(j=sLen-1, k=Math.min(goal*(2+2*epsilon), (1+epsilon)*N)-1,
                        step=(k+1)/(j+1);j>=0;j--, k-=step) {
                s.put(k,s.getData(j),null,null);
                s.put(j, NONE,null,null);
            }*/
            int gapSize = 0;
            int numberOfInsertedElements = 0;
            int numberOfElementsToBalance = (int)Math.pow(2,round);
            int lastIndex = (2 + 2*epsilon) *(int)Math.pow(2,round) -1;
            int currentLastIndex = lastIndex;
            for(int i = lastIndex; i >= 0; i--){
                if(!IS_EMPTY(s.getData(i))){
                    gapSize = (int)((currentLastIndex + 1 - numberOfElementsToBalance)/(numberOfElementsToBalance+1));


                    s.put(currentLastIndex - gapSize, s.getData(i), null, null);
                    if(i != currentLastIndex - gapSize){
                        s.put(i,NONE,null,null);
                    }
                    currentLastIndex = currentLastIndex - gapSize -1;
                    numberOfElementsToBalance--;
                    numberOfInsertedElements++;
                }
            }


            //In each round insert the double of elements to the sorted array
            // because there will be the double of free spaces after the rebalance
            sLen = Math.min(goal*(2+2*epsilon), N*(1+epsilon));

            goal <<= 1;//We increment i

        }

    }

    int searchFree(int e, StringArray sorted, int last) {

        ArrayMarkerProperties arrayMarkerProperties = new ArrayMarkerProperties();
        arrayMarkerProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLUE);
        //  ArrayMarker arrayMarker = lang.newArrayMarker(sorted,0,"lol123",null,arrayMarkerProperties);
        // arrayMarker.hide();

        while(last > sorted.getLength()-1){
            last--;
        }
        // lang.nextStep();
        //srcSf.unhighlight(0);
        // srcSf.highlight(1);
        for(int i = 0; i<= last;i++){
            //sorted.highlightCell(i,null,null);
        }

        // lang.nextStep();
        // srcSf.unhighlight(1);
        // srcSf.highlight(2);
        int first = 0;
        int middle;
        // sorted.highlightCell(last,null,null);
        while(last>=0 && IS_EMPTY(sorted.getData(last))){
            //sorted.unhighlightCell(last,null,null);
            last --;
            //sorted.highlightCell(last,null,null);
        }
        while(first<=last && IS_EMPTY(sorted.getData(first))){
            //sorted.highlightElem(3,null,null);
            // sorted.unhighlightCell(first,null,null);
            first ++;
        }

        while(first<=last) {
            middle = (first+last)/2;
            //lang.nextStep();
            // arrayMarker.show();
            //  arrayMarker.move(middle,null,null);
            if(IS_EMPTY(sorted.getData(middle))) {
                int tmp = middle + 1;
                //Look to the right
                while(tmp<last && IS_EMPTY(sorted.getData(tmp))) {
                    tmp ++;
                }
                if(Integer.parseInt(sorted.getData(tmp))>=e) {
                    tmp = middle - 1;
                    while(tmp >first && IS_EMPTY(sorted.getData(tmp))) {
                        tmp--;
                    }
                    if(!sorted.getData(tmp).equals(NONE)){
                        if(Integer.parseInt(sorted.getData(tmp))<=e) {
                            //Found intermediate position
                            // lang.nextStep();
                            //      arrayMarker.hide();
                            // System.out.println("wtf" + middle);
                            //sorted.setHighlightFillColor(middle,Color.red,null,null);
                            //sorted.highlightCell(middle,null,null);
                            // lang.nextStep();

                            //  lang.nextStep();

                            // sorted.setHighlightTextColor(tmp,Color.MAGENTA,null,null);
                            //  sorted.setHighlightBorderColor(tmp,Color.MAGENTA,null,null);
                            //  sorted.highlightElem(tmp,null,null);
                            //  srcSf.unhighlight(2);
                            //  srcSf.highlight(3);
                            // lang.nextStep();
                            for(int i = 0; i < sorted.getLength();i++){
                                sorted.unhighlightCell(i,null,null);
                            }
                            return tmp;
                        }
                    }
                    last = tmp - 1;
                } else first = tmp + 1;
            } else if(Integer.parseInt(sorted.getData(middle))<e) {
                first = middle + 1;
               // lang.nextStep();
                for(int i = 0; i <first;i++){
                    // sorted.unhighlightCell(i,null,null);
                }
            } else {
                last = middle - 1;
                // lang.nextStep();
                for(int i = sorted.getLength()-1; i >last;i--){
                    // sorted.unhighlightCell(i,null,null);
                }
            }

        }
        //If no position was found return -1 or if a lower position was found, return that
        if(last>=0 && IS_EMPTY(sorted.getData(last))){
            last --;
        }
        // arrayMarker.hide();
        for(int i = 0; i < sorted.getLength();i++){
            sorted.unhighlightCell(i,null,null);
        }
        // srcSf.highlight(4);
        return last;
    }


    void printArray(StringArray a){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i = 0; i < a.getLength(); i ++){
            sb.append(a.getData(i) + ", ");
        }
        sb.append("]");
        System.out.println(sb.toString());
    }

    String NONE = "   ";


    boolean IS_EMPTY(String i ){
        return i.equals(NONE);
    }


    public String getName() {
        return "Library Sort";
    }

    public String getAlgorithmName() {
        return "Library Sort";
    }

    public String getAnimationAuthor() {
        return "Jan Rathjens";
    }

    public String getDescription() {
        return
                "Library Sort is a sorting algorithm related to Insertion Sort. It sorts an array by inserting elements into a new array of larger size. The size is determined by" +
                        " a parameter e. The new array has gaps between elements, which are used to reduce Insertion Sort's complexity of O(n^2) to O(n*log(n)) \n\n" +
                        "After picking an absolute number for parameter e and initializing a new empty array S of size (1+e)n, where n is the number of elements in an array A which is wished to be" +
                        "sorted, the algorithm proceeds in floor(log2(n)+1) rounds. After each round i 2^i elements are inserted in S via binary search and at the end of " +
                        "each round all elements in S will be rebalanced. The elements are inserted in the first (1+e)*2^i positions. Rebalancing evenly distributes the elements to the first " +
                        "(2+2e)2^i positions.";

    }

    public String getCodeExample() {

       return
       "librarySort(Array A, integer epsilon)\n" +
       "    initialize new empty array S[(1+epsilon)*n]\n" +
       "   for i = 1 to floor(log2(n) + 1)\n" +
       "        for j = 2^(i-1) to 2^i\n" +
       "            findFreeSpaceAndInsert(A[j],S,(1+epsilon)*2.0^i-1)\n" +
        "            if all elements are inserted in S\n"+
       "                return S\n" +
       "        rebalance(S)";
    }

    public String getFileExtension() {
        return "asu";
    }

    public Locale getContentLocale() {
        return Locale.ENGLISH;
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }


    public LibGenerator() {
    }


    public void setProperties(){
        arrayProps = new ArrayProperties();
        arrayProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        arrayProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        arrayProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, Boolean.TRUE);
        arrayProps.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY, Color.BLACK);
        arrayProps.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        arrayProps.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,
                Color.YELLOW);

        arrayMarkerNextFreeProperties = new ArrayMarkerProperties();
        arrayMarkerNextFreeProperties.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.MAGENTA);

        rectProps = new RectProperties();
        rectProps.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
        rectProps.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.LIGHT_GRAY);
        rectProps.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);

        textProps = new TextProperties();
        textProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 24));

        textRoundCounterProps = new TextProperties();
        textRoundCounterProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.BOLD, 20));

        introSrcProps = new SourceCodeProperties();
        introSrcProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 16));
        introSrcProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        introSrcProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        introSrcProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("Monospaced", Font.PLAIN, 12));


        sourceCodeProps = new SourceCodeProperties();
        sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(
                Font.SANS_SERIF, Font.PLAIN, 16));
        sourceCodeProps.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY, Color.RED);
        sourceCodeProps.set(AnimationPropertiesKeys.CONTEXTCOLOR_PROPERTY, Color.BLUE);
        sourceCodeProps.set(AnimationPropertiesKeys.FONT_PROPERTY,
                new Font("Monospaced", Font.PLAIN, 12));
    }

    public void incrementRoundCounter(){
        String s = roundCounter.getText();
        roundCounter.setText("Round: " + (Integer.parseInt(s.substring(7,s.length()))+1),null,null);
    }

    public void setParameters(Hashtable<String, Object> primitives){
        epsilon = (int) primitives.get("epsilon");
        a = (int[]) primitives.get("inputArray");
        _farbHighlight = (Color) primitives.get("farbHighlight");
    }

    public void displayRoundCounter(){
        roundCounter = lang.newText(new Coordinates(600, 40), "Round: 0", "roundCounter", null, textRoundCounterProps);

    }

    public void displayIntroSrc(){
        introSrc = lang.newSourceCode(new Coordinates(20, 75), "Introduction",
                null, introSrcProps);
        introSrc.addCodeLine("Given: An array A wished to be sorted",null, 0,
                null);
        introSrc.addCodeLine("1. Pick an absolute number for parameter e", null, 0,
                null);
        introSrc.addCodeLine("2. Initialize a new empty array S of size (1+e)n",null, 0,
                null);
        introSrc.addCodeLine("3. for i ← 1 to floor(log2(n)+1)",null, 0,
                null);
        introSrc.addCodeLine("3.1 for every element k with index smaller than 2^i which has not been inserted to S yet ",null, 1,
                null);
        introSrc.addCodeLine("3.1.1 find a space for k in S with binary search and by possibly shifting elements in S",null, 2,
                null);
        introSrc.addCodeLine("3.1.2 if all elements have been inserted return S",null, 2,
                null);
        introSrc.addCodeLine("3.2 rebalance S by distributing elements as evenly as possible",null, 1,
                null);


    }
    public void displayMC1(){
        MultipleChoiceQuestionModel effizient = new MultipleChoiceQuestionModel("MC");
        effizient.setPrompt("How many elements will be sorted after the first round?");
        effizient.addAnswer("1 element", 0, "After round 1 2^1 = 2 elements will be sorted");
        effizient.addAnswer("2 elements ", 1, "Correct!");
        effizient.addAnswer("4 elements  ", 0, "After round 1 2^1 = 2 elements will be sorted");
        lang.addMCQuestion(effizient);
    }
    public void displayLibrarySortCode(){
        src = lang.newSourceCode(new Coordinates(20, 100), "sourceCode",
                null, sourceCodeProps);
        src.addCodeLine("librarySort(Array A, integer epsilon)", null, 0, null); // 0
        src.addCodeLine("initialize new empty array S[(1+epsilon)*n]", null, 1, null); //1
        src.addCodeLine("for i = 1 to floor(log2(n) + 1)", null, 1, null);//2
        src.addCodeLine("for j = 2^(i-1) to 2^i-1", null, 2, null);//3
        src.addCodeLine("findFreeSpaceAndInsert(A[j],S,(1+epsilon)*2.0^i-1)",null,3,null);//4
        src.addCodeLine("if all elements are inserted in S",null,3,null);//5
        src.addCodeLine("return S",null,4,null);//6
        src.addCodeLine("rebalance(S)",null,2,null);//7


    }

    public void displaySearchFreeCode(){
        srcSf = lang.newSourceCode(new Coordinates(20,265),"sourceCodeSearchFree",null,sourceCodeProps);
        srcSf.addCodeLine("findFreeSpaceAndInsert(Element e, Array S, Index last)",null,0,null);//0
        srcSf.addCodeLine("find next smallest element to e in S",null,1,null);//1
        srcSf.addCodeLine("if next smallest element k to e in s exists",null,1,null);//2
        srcSf.addCodeLine("set insert position to index of e + 1",null,2,null);//3
        srcSf.addCodeLine("else set insert position to 0",null,1,null);//4
        srcSf.addCodeLine("if insert position is out of bounds",null,1,null);//5
        srcSf.addCodeLine("decrement insert position",null,2,null);//6
        srcSf.addCodeLine("look for next free spot to the left",null,2,null);//7
        srcSf.addCodeLine("shift elements between next free spot and insert position to the left",null,2,null);//8
        srcSf.addCodeLine("else if insert position is not empty",null,1,null);//9
        srcSf.addCodeLine("look to the right for next free spot",null,2,null);//10
        srcSf.addCodeLine("if there is no next free spot ",null,2,null);//11
        srcSf.addCodeLine("decrement insert position",null,3,null);//12
        srcSf.addCodeLine("find next free spot to the left ",null,3,null);//13
        srcSf.addCodeLine("shift elements between next free spot and insert position to the left",null,3,null);//14
        srcSf.addCodeLine("else shift elements between next free spot and insert position to the right",null,2,null);//15
        srcSf.addCodeLine("insert e at insert position",null,1,null);//16





    }

    public void displayRebalanceCode(){
        srcR= lang.newSourceCode(new Coordinates(40,900),"sourceCodeSearchFree",null,sourceCodeProps);
        srcR.addCodeLine("rebalance lol",null,0,null);//0
    }

}