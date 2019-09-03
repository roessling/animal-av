package generators.sorting.shakersort;

import generators.framework.Generator;
import generators.framework.GeneratorType;
import generators.framework.properties.AnimationPropertiesContainer;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Locale;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.ArrayMarker;
import algoanim.primitives.IntArray;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;
import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.ArrayMarkerProperties;
import algoanim.properties.ArrayProperties;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;

public class ShakerSort implements Generator{
    Language lang;
    ArrayProperties ap;
    ArrayMarkerProperties lgp;
    ArrayMarkerProperties rgp;
    TextProperties tpn;
    TextProperties tpz1,tpz2;
    RectProperties rp;
    SourceCodeProperties scp;
    public ShakerSort() {
//  	nothing to be done here
    }
    
    public void init() {
        lang=new AnimalScript("ShakerSort","Eduard Metlewski",300,200);
        lang.setStepMode(true);
        //SourceCode-Properties
            scp=new SourceCodeProperties();
            scp.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.BLUE);
            scp.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,Color.RED);
        // RechteckEigenschaften
            rp=new RectProperties();
            rp.set(AnimationPropertiesKeys.FILLED_PROPERTY,true);
            rp.set(AnimationPropertiesKeys.FILL_PROPERTY,Color.gray);
        //TextEigenschaften
            tpn=new TextProperties();
            tpn.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.CYAN);
            tpn.set(AnimationPropertiesKeys.CENTERED_PROPERTY,true);
        //TextEigenschaften
            tpz1=new TextProperties();
            tpz1.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.RED);
            tpz1.set(AnimationPropertiesKeys.CENTERED_PROPERTY,true);
            //-----------------------------------------------------------
            tpz2=new TextProperties();
            tpz2.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.green);
            tpz2.set(AnimationPropertiesKeys.CENTERED_PROPERTY,true);
        // Array-Eigenschaften
            ap=new ArrayProperties();
            ap.set(AnimationPropertiesKeys.CELLHIGHLIGHT_PROPERTY,Color.GREEN);
            ap.set(AnimationPropertiesKeys.ELEMHIGHLIGHT_PROPERTY,Color.RED);
            ap.set(AnimationPropertiesKeys.ELEMENTCOLOR_PROPERTY,Color.BLACK);
            ap.set(AnimationPropertiesKeys.FILLED_PROPERTY,true);
            ap.set(AnimationPropertiesKeys.FILL_PROPERTY,Color.GRAY);
        //Marker lg -Eigenschaft
            lgp=new ArrayMarkerProperties();
            lgp.set(AnimationPropertiesKeys.LABEL_PROPERTY,"l");
            lgp.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.green);
        //Marker rg -Eigenschaft
            rgp=new ArrayMarkerProperties();
            rgp.set(AnimationPropertiesKeys.LABEL_PROPERTY,"r");
            rgp.set(AnimationPropertiesKeys.COLOR_PROPERTY,Color.green);
        
    }
    public void shakerSort(int[] a){
        
        boolean fertig=false;
        boolean nachlinks=false;
        MsTiming time=new MsTiming(300);
        SourceCode sc=lang.newSourceCode(new Coordinates(20,150),"Code",null,scp);
        sc.addCodeLine("while(lg<rg){",null,0,null);
        sc.addCodeLine("    nach Rechts:",null,0,null);
        sc.addCodeLine("        A[i]>A[i+1]:",null,0,null);
        sc.addCodeLine("            ja--> swap(A[i],A[i+1]);",null,0,null);
        sc.addCodeLine("                      i++;",null,0,null);
        sc.addCodeLine("            nein--> i++;",null,0,null);
        sc.addCodeLine("--------------------------------------------------------",null,0,null);
        sc.addCodeLine("    nach Links:",null,0,null);
        sc.addCodeLine("        A[i-1]>A[i]:",null,0,null);
        sc.addCodeLine("            ja--> swap(A[i-1],A[i]);",null,0,null);
        sc.addCodeLine("                      i--;",null,0,null);
        sc.addCodeLine("            nein--> i--;",null,0,null);
        sc.addCodeLine("}",null,0,null);
        lang.newRect(new Coordinates(0,0),new Coordinates(130,40),"o",null);
        lang.newRect(new Coordinates(5,5),new Coordinates(125,35),"in",null,rp);
        lang.newText(new Coordinates(60,5),"ShakerSort","name",null,tpn);
        Text zustand1=lang.newText(new Coordinates(60,18),"array nicht sortiert","zustand",null,tpz1);
        IntArray array=lang.newIntArray(new Coordinates(40,100),a,"array",null,ap);
        ArrayMarker lg=lang.newArrayMarker(array,0,"lg",null,lgp);
        ArrayMarker rg=lang.newArrayMarker(array,a.length-1,"rg",null,rgp);
        lang.nextStep();
        int temp=lg.getPosition(); //temp-Position vom Marker
        sc.highlight(0);
        sc.highlight(12);
        while(!fertig){
            if(!nachlinks){
                if(lg.getPosition()+1==rg.getPosition()){
                    fertig=true;  
                }
                sc.highlight(1);
                sc.highlight(2);
                if(array.getData(temp)>array.getData(temp+1)){
                    array.highlightElem(temp,null,null);
                    array.highlightElem(temp+1,null,null);
                    lang.nextStep();
                    sc.highlight(3);
                    lang.nextStep();
                    array.swap(temp,temp+1,null,time);
                    lang.nextStep();
                    sc.highlight(4);
                    lang.nextStep();
                    sc.unhighlight(3);
                    sc.unhighlight(4);
                    lang.nextStep();
                    array.unhighlightElem(temp,null,null);
                    array.unhighlightElem(temp+1,null,null);
                    temp++;
                    lang.nextStep();
                    sc.unhighlight(2);
                    lang.nextStep();
                }
                else{
                    array.highlightElem(temp,null,null);
                    array.highlightElem(temp+1,null,null);
                    lang.nextStep();
                    sc.highlight(5);
                    lang.nextStep();
                    array.unhighlightElem(temp,null,null);
                    array.unhighlightElem(temp+1,null,null);
                    sc.unhighlight(5);
                    temp++;
                    lang.nextStep();
                    sc.unhighlight(2);
                    lang.nextStep();
                }
                sc.unhighlight(1);
                if(temp==rg.getPosition()){
                    nachlinks=true;
                    array.highlightCell(temp,null,null);
                    lang.nextStep();
                    temp--;
                    rg.move(temp,null,time);
                    lang.nextStep();
                }
            }
            else{
                if(lg.getPosition()+1==rg.getPosition()){
                    fertig=true;  
                }
                sc.highlight(7);
                sc.highlight(8);
                if(array.getData(temp)<array.getData(temp-1)){
                    array.highlightElem(temp,null,null);
                    array.highlightElem(temp-1,null,null);
                    lang.nextStep();
                    sc.highlight(9);
                    lang.nextStep();
                    array.swap(temp,temp-1,null,time);
                    lang.nextStep();
                    sc.highlight(10);
                    lang.nextStep();
                    array.unhighlightElem(temp,null,null);
                    array.unhighlightElem(temp-1,null,null);
                    lang.nextStep();
                    sc.unhighlight(9);
                    sc.unhighlight(10);
                    temp--;
                    lang.nextStep();
                    sc.unhighlight(8);
                    lang.nextStep();
                }
                else{
                    array.highlightElem(temp,null,null);
                    array.highlightElem(temp-1,null,null);
                    lang.nextStep();
                    sc.highlight(11);
                    lang.nextStep();
                    array.unhighlightElem(temp,null,null);
                    array.unhighlightElem(temp-1,null,null);
                    sc.unhighlight(11);
                    temp--;
                    lang.nextStep();
                    sc.unhighlight(8);
                    lang.nextStep();
                }
                sc.unhighlight(7);
                if(temp==lg.getPosition()){
                    nachlinks=false;
                    array.highlightCell(temp,null,null);
                    lang.nextStep();
                    temp++;
                    sc.unhighlight(8);
                    lang.nextStep();
                    lg.move(temp,null,time);
                    lang.nextStep();
                    
                }
            }
            
        }
        array.highlightCell(lg.getPosition(),null,null);
        lg.hide();
        rg.hide();
        lang.nextStep();
        zustand1.hide();
        lang.newText(new Coordinates(60,15),"array sortiert","zustand",null,tpz2);
        sc.hide();
    }

    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_SORT);
    }

    public String getName() {
        return "Shaker Sort";
    }
    
    public String getDescription() {
        return "ShakerSort ist eine kluge Version von BubbleSort";
    }

    public String getCodeExample() {
        return "while(lg<rg){\n" +
                "nach Recht:\n" +
                "if(A[i]>A[i+1])\n" +
                "ja-->swap(A[i],A[i+1])\ni++\n" +
                "nein-->i++\n" +
                "nach Links:\n" +
                "if(A[i-1]>A[i])\n" +
                "ja-->swap(A[i-1],A[i])\ni--\n" +
                "nein-->i--\n";
    }

    public Locale getContentLocale() {
        return Locale.GERMANY;
    }

    public String getFileExtension() {
      return Generator.ANIMALSCRIPT_FORMAT_EXTENSION;
    }

    public String getOutputLanguage() {
        return Generator.PSEUDO_CODE_OUTPUT;
    }

    public String getAlgorithmName() {
        return "Shaker Sort";
    }

    public String generate(AnimationPropertiesContainer props, Hashtable<String, Object> primitives) {
        /*Set set=primitives.keySet();
        Iterator i=set.iterator();*/
        int[] a=(int[])primitives.get("a");
        this.shakerSort(a);
        return lang.toString();
    }
    public String getAnimationAuthor() {
      return "Eduard Metlewski";
    }

}
