package generators.misc;


import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.SourceCode;
import algoanim.primitives.StringArray;
import algoanim.primitives.Text;
import algoanim.primitives.Variables;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.Offset;
import algoanim.util.Timing;
import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;
import translator.Translator;

import java.awt.*;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Imed on 6/14/2016.
 */
public class Sorensen implements Generator {
    private static final String AUTHOR = "Imed Ben Ghozi";
    private static final String TITLE = "Sorensen Dice";

    private static final String SOURCE_CODE_0 = "public static double diceCoefficient(String s1, String s2){";
    private static final String SOURCE_CODE_1 = "  Set<String> nx = new HashSet<String>();";
    private static final String SOURCE_CODE_2 = "  Set<String> ny = new HashSet<String>();";
    private static final String SOURCE_CODE_3 = "  for (int i=0; i < s1.length()-1; i++) {";
    private static final String SOURCE_CODE_4 = "   char x1 = s1.charAt(i);";
    private static final String SOURCE_CODE_5 = "   char x2 = s1.charAt(i+1);";
    private static final String SOURCE_CODE_6 = "   String tmp = String.valueOf(x1)+ x2;";
    private static final String SOURCE_CODE_7 = "   nx.add(tmp);";
    private static final String SOURCE_CODE_8 = "  }";
    private static final String SOURCE_CODE_9 = "  for (int j=0; j < s2.length()-1; j++) {";
    private static final String SOURCE_CODE_10 = "   char y1 = s2.charAt(j);";
    private static final String SOURCE_CODE_11 = "   char y2 = s2.charAt(j+1);";
    private static final String SOURCE_CODE_12 = "   String tmp = String.valueOf(y1) + y2;";
    private static final String SOURCE_CODE_13 = "   ny.add(tmp);";
    private static final String SOURCE_CODE_14 = "  }";
    private static final String SOURCE_CODE_15 = "  Set<String> intersection = new HashSet<String>(nx);";
    private static final String SOURCE_CODE_16 = "  intersection.retainAll(ny);";
    private static final String SOURCE_CODE_17 = "  double totcombigrams = intersection.size();";
    private static final String SOURCE_CODE_18 = "  return (2*totcombigrams) / (nx.size()+ny.size());";
    private static final String SOURCE_CODE_19 = "}";

    private String description1;
    private String description2;
    private String description3;
    private String description4;
    private String description5;
    private String description6;
    private String description7;
    private String description8;

    private String conclusion1;
    private String conclusion2;
    private String conclusion3;
    private String conclusion4;
    private String conclusion5;
    private String conclusion6;
    private String conclusion7;
    private String conclusion8;

    private Locale l;
    private Translator trans;
    private Language lang;
    static TextProperties hProps;
    static SourceCodeProperties scProps;
    static ArrayProperties ap;
    static TextProperties dProps;

    public Sorensen(Locale l){
        this.l=l;
        trans=new Translator("generators/misc/SorensenDiceLang/sorensendice", l);

        description1=trans.translateMessage("description1");
        description2=trans.translateMessage("description2");
        description3=trans.translateMessage("description3");
        description4=trans.translateMessage("description4");
        description5=trans.translateMessage("description5");
        description6=trans.translateMessage("description6");
        description7=trans.translateMessage("description7");
        description8=trans.translateMessage("description8");

        conclusion1=trans.translateMessage("conclusion1");
        conclusion2=trans.translateMessage("conclusion2");
        conclusion3=trans.translateMessage("conclusion3");
        conclusion4=trans.translateMessage("conclusion4");
        conclusion5=trans.translateMessage("conclusion5");
        conclusion6=trans.translateMessage("conclusion6");
        conclusion7=trans.translateMessage("conclusion7");
        conclusion8=trans.translateMessage("conclusion8");
    }



    @Override
    public String generate(AnimationPropertiesContainer animationPropertiesContainer, Hashtable<String, Object> hashtable) {
        String s1= (String) hashtable.get("s1");
        String s2=(String)hashtable.get("s2");
        scProps=(SourceCodeProperties) animationPropertiesContainer.getPropertiesByName("scProps");
        ap=(ArrayProperties) animationPropertiesContainer.getPropertiesByName("ap");
        dProps=(TextProperties)animationPropertiesContainer.getPropertiesByName("dProps");
        sorensenDice(s1,s2);

        return lang.toString();
    }
    public  void sorensenDice(String s1, String s2) {
        Variables v = lang.newVariables();
        hProps = new TextProperties();
        hProps.set(AnimationPropertiesKeys.COLOR_PROPERTY, Color.BLACK);
        hProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
        Text h = lang.newText(new Coordinates(10, 10), TITLE, "header", null, hProps);
        h.show();
        lang.nextStep();
        Text d1 = lang.newText(new Offset(0, 100, "header", AnimalScript.DIRECTION_SW), description1, "description1",
                null, dProps);
        Text d2 = lang.newText(new Offset(0, 25, "description1", AnimalScript.DIRECTION_SW), description2,
                "description2", null, dProps);
        Text d3 = lang.newText(new Offset(0, 25, "description2", AnimalScript.DIRECTION_SW), description3,
                "description3", null, dProps);
        Text d4 = lang.newText(new Offset(0, 25, "description3", AnimalScript.DIRECTION_SW), description4, "description4", null, dProps);
        Text d5 = lang.newText(new Offset(0, 25, "description4", AnimalScript.DIRECTION_SW), description5, "description5", null, dProps);
        Text d6 = lang.newText(new Offset(0, 25, "description5", AnimalScript.DIRECTION_SW), description6, "description6", null, dProps);
        Text d7 = lang.newText(new Offset(0, 25, "description6", AnimalScript.DIRECTION_SW), description7, "description7", null, dProps);
        Text d8 = lang.newText(new Offset(0, 25, "description7", AnimalScript.DIRECTION_SW), description8, "description8", null, dProps);
        lang.nextStep("Description");
        lang.hideAllPrimitives();
        h.show();
        SourceCode sC = lang.newSourceCode(new Offset(0, 100, "header", AnimalScript.DIRECTION_SW), "sourceCode", null, scProps);
        sC.addCodeLine(SOURCE_CODE_0, "Sc0", 0, null);
        sC.addCodeLine(SOURCE_CODE_1, "Sc1", 2, null);
        sC.addCodeLine(SOURCE_CODE_2, "Sc2", 2, null);
        sC.addCodeLine(SOURCE_CODE_3, "Sc3", 2, null);
        sC.addCodeLine(SOURCE_CODE_4, "Sc4", 4, null);
        sC.addCodeLine(SOURCE_CODE_5, "Sc5", 4, null);
        sC.addCodeLine(SOURCE_CODE_6, "Sc6", 4, null);
        sC.addCodeLine(SOURCE_CODE_7, "Sc7", 4, null);
        sC.addCodeLine(SOURCE_CODE_8, "Sc8", 2, null);
        sC.addCodeLine(SOURCE_CODE_9, "Sc9", 2, null);
        sC.addCodeLine(SOURCE_CODE_10, "Sc10", 4, null);
        sC.addCodeLine(SOURCE_CODE_11, "Sc11", 4, null);
        sC.addCodeLine(SOURCE_CODE_12, "Sc12", 4, null);
        sC.addCodeLine(SOURCE_CODE_13, "Sc13", 4, null);
        sC.addCodeLine(SOURCE_CODE_14, "Sc14", 2, null);
        sC.addCodeLine(SOURCE_CODE_15, "Sc15", 2, null);
        sC.addCodeLine(SOURCE_CODE_16, "Sc16", 2, null);
        sC.addCodeLine(SOURCE_CODE_17, "Sc17", 2, null);
        sC.addCodeLine(SOURCE_CODE_18, "Sc18", 2, null);
        sC.addCodeLine(SOURCE_CODE_19, "Sc19", 0, null);
        lang.nextStep("Calling SorensenDice");
        String[] sA1 = new String[s1.length()];
        String[] sA2 = new String[s2.length()];
        for (int i = 0; i < s1.length(); i++) {
            sA1[i] = String.valueOf(s1.charAt(i));
        }
        for (int i = 0; i < s2.length(); i++) {
            sA2[i] = String.valueOf(s2.charAt(i));
        }
        sC.highlight(0);
        ap = new ArrayProperties();
        ap.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY, Color.RED);
        ap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY, Color.green);
        ap.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
        StringArray s_1 = lang.newStringArray(new Offset(0, 40, "header", AnimalScript.DIRECTION_SW), sA1, "s1", null, ap);
        StringArray s_2 = lang.newStringArray(new Offset(40, 0, "s1", AnimalScript.DIRECTION_NE), sA2, "s2", null, ap);
        lang.nextStep();
        sC.toggleHighlight(0, 1);
        Set<String> nx = new HashSet<String>();
        StringArray n_x = lang.newStringArray(new Offset(40, 0, "sourceCode", AnimalScript.DIRECTION_NE), new String[]{""}, "xBigrams", null, ap);
        n_x.hide();
        lang.nextStep();
        sC.toggleHighlight(1, 2);
        Set<String> ny = new HashSet<String>();
        lang.nextStep();
        v.declare("int", "i", "0", "STEPPER");
        v.declare("String", "tmp", "null", "TEMPORARY");
        for (int i = 0; i < s1.length() - 1; i++) {
            sC.toggleHighlight(2, 3);
            sC.toggleHighlight(7, 3);
            v.set("i", "" + i);
            s_1.unhighlightElem(i - 1, i, null, null);
            s_1.unhighlightCell(i - 1, i, null, null);
            lang.nextStep("Iteration over s1:" + i);
            char x1 = s1.charAt(i);
            sC.toggleHighlight(3, 4);
            s_1.highlightElem(i, null, null);
            lang.nextStep();
            sC.toggleHighlight(4, 5);
            char x2 = s1.charAt(i + 1);
            s_1.highlightElem(i + 1, null, null);
            lang.nextStep();
            sC.toggleHighlight(5, 6);
            String tmp = "" + x1 + x2;
            v.set("tmp", tmp);
            lang.nextStep();
            sC.toggleHighlight(6, 7);
            //if we sucessufully added the bigram to the set highlight it
            if (nx.add(tmp)) {
                s_1.highlightCell(i, i + 1, null, null);
                String[] xbigrams = nx.toArray(new String[nx.size()]);
                n_x.hide();
                n_x = lang.newStringArray(new Offset(40, 0, sC, AnimalScript.DIRECTION_NE), xbigrams, "xBigrams", null, ap);


            }
            if (i == s1.length() - 2) {
                s_1.unhighlightCell(i, i + 1, Timing.MEDIUM, null);
                s_1.unhighlightElem(i, i + 1, Timing.MEDIUM, null);

            }
            lang.nextStep();
        }

        sC.unhighlight(7);
        v.declare("int", "j", "0", "STEPPER");
        StringArray n_y = lang.newStringArray(new Offset(40, 0, n_x, AnimalScript.DIRECTION_NE), new String[]{""}, "yBigrams", null, ap);
        n_y.hide();
        for (int j = 0; j < s2.length() - 1; j++) {
            sC.toggleHighlight(7, 9);
            sC.toggleHighlight(13,9);
            v.set("j", "" + j);
            s_2.unhighlightElem(j - 1, j, null, null);
            s_2.unhighlightCell(j - 1, j, null, null);
            lang.nextStep("Iteration over s2:" + j);
            char y1 = s2.charAt(j);
            sC.toggleHighlight(9, 10);
            s_2.highlightElem(j, null, null);
            lang.nextStep();
            sC.toggleHighlight(10, 11);
            char y2 = s2.charAt(j + 1);
            s_2.highlightElem(j + 1, null, null);
            lang.nextStep();
            sC.toggleHighlight(11, 12);
            String tmp = "" + y1 + y2;
            v.set("tmp", tmp);
            lang.nextStep();
            sC.toggleHighlight(12, 13);
            if (ny.add(tmp)) {
                s_2.highlightCell(j, j + 1, null, null);
                String[] ybigrams = ny.toArray(new String[ny.size()]);
                n_y.hide();
                n_y = lang.newStringArray(new Offset(100, 0, n_x, AnimalScript.DIRECTION_NE), ybigrams, "yBigrams", null, ap);
            }
            if (j == s2.length() - 2) {
                s_2.unhighlightCell(j, j + 1, Timing.MEDIUM, null);
                s_2.unhighlightElem(j, j + 1, Timing.MEDIUM, null);
            }
            lang.nextStep();
        }
        sC.toggleHighlight(13, 15);
        v.discard("tmp");
        Set<String> intersection = new HashSet<String>(nx);
        n_x.moveTo(AnimalScript.DIRECTION_SW,null,new Offset(300,-40,sC,AnimalScript.DIRECTION_C),null,Timing.MEDIUM);
        lang.nextStep("building Intersection");
        sC.toggleHighlight(15,16);
        intersection.retainAll(ny);
        n_y.moveTo(AnimalScript.DIRECTION_SW,null,new Offset(300,-40,sC,AnimalScript.DIRECTION_C),null,Timing.MEDIUM);
        n_y.hide(Timing.MEDIUM);
        n_x.hide(Timing.MEDIUM);
        String[] inter=intersection.toArray(new String[intersection.size()]);
        StringArray interSection=lang.newStringArray(new Offset(300,-40,sC,AnimalScript.DIRECTION_C),inter,"intersection",null,ap);
        interSection.hide();
        interSection.show(Timing.MEDIUM);
        lang.nextStep();
        sC.toggleHighlight(16,17);
        double totCombigrams = intersection.size();
        v.declare("double","totCombigrams",""+totCombigrams,"CONTAINER");
        lang.nextStep();
        sC.toggleHighlight(17,18);
        int x=nx.size();
        int y=ny.size();
        v.declare("int","nx.size",""+x,"CONTAINER");
        v.declare("int","ny.size",""+y,"CONTAINER");
        double coefficient=(2*totCombigrams) / (nx.size()+ny.size());
        String calculation="DiceCoefficient=(2*"+totCombigrams+")/("+nx.size()+"+"+ny.size()+")="+2*totCombigrams+"/"+(nx.size()+ny.size())+"="+coefficient;
        Text calc=lang.newText(new Offset(40,0,s_2,AnimalScript.DIRECTION_NE),calculation,"calc",null,dProps);
        lang.nextStep("Result");
        lang.hideAllPrimitives();
        interSection.hide();
        h.show();
        Text c1 = lang.newText(new Offset(0, 100, "header", AnimalScript.DIRECTION_SW), conclusion1, "conclusion1",
                null, dProps);
        Text c2 = lang.newText(new Offset(0, 25, c1, AnimalScript.DIRECTION_SW), conclusion2,
                "conclusion2", null, dProps);
        Text c3 = lang.newText(new Offset(0, 25, c2, AnimalScript.DIRECTION_SW), conclusion3,
                "conclusion3", null, dProps);
        Text c4 = lang.newText(new Offset(0, 25, c3, AnimalScript.DIRECTION_SW), conclusion4, "conclusion4", null, dProps);
        Text c5 = lang.newText(new Offset(0, 25, c4, AnimalScript.DIRECTION_SW), conclusion5, "conclusion5", null, dProps);
        Text c6 = lang.newText(new Offset(0, 25, c5, AnimalScript.DIRECTION_SW), conclusion6, "conclusion6", null, dProps);
        Text c7 = lang.newText(new Offset(0, 25, c6, AnimalScript.DIRECTION_SW), conclusion7, "conclusion7", null, dProps);
        Text c8 = lang.newText(new Offset(0, 25, c7, AnimalScript.DIRECTION_SW), conclusion8, "conclusion8", null, dProps);
        lang.nextStep("Conclusion");
    }

    @Override
    public String getAlgorithmName() {
        return TITLE;
    }

    @Override
    public String getAnimationAuthor() {
        return AUTHOR;
    }

    @Override
    public String getCodeExample() {
        return SOURCE_CODE_0 + "\n" + SOURCE_CODE_1 + "\n" + SOURCE_CODE_2 + "\n" + SOURCE_CODE_3 + "\n" + SOURCE_CODE_4 + "\n" + SOURCE_CODE_5 + "\n"
                + SOURCE_CODE_6 + "\n" + SOURCE_CODE_7 + "\n" + SOURCE_CODE_8 + "\n" + SOURCE_CODE_9 + "\n" + SOURCE_CODE_10 + "\n"
                + SOURCE_CODE_11 + "\n" + SOURCE_CODE_12 + "\n" + SOURCE_CODE_13 + "\n" + SOURCE_CODE_14 + "\n" + SOURCE_CODE_15 + "\n" + SOURCE_CODE_16 + "\n" + SOURCE_CODE_17 + "\n" +
                SOURCE_CODE_18 + "\n" + SOURCE_CODE_19 + "\n";
    }

    @Override
    public Locale getContentLocale() {
        return l;
    }

    @Override
    public String getDescription() {
        return description1+"\n"+description2+"\n"+description3+"\n"+description4+"\n"+
                description5+"\n"+description6+"\n"+description7+"\n"+description8+"\n";
    }

    @Override
    public String getFileExtension() {
        return "asu";
    }

    @Override
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_MORE);
    }

    @Override
    public String getName() {
        return TITLE;
    }

    @Override
    public String getOutputLanguage() {
      return Generator.JAVA_OUTPUT;
    }

    @Override
    public void init() {
        lang = new AnimalScript(TITLE, AUTHOR, 1280, 800);
        lang.setStepMode(true);
        lang.setInteractionType(Language.INTERACTION_TYPE_AVINTERACTION);

    }
}
