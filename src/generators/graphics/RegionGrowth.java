/*
 * RegionGrowing.java
 * Kevin Kocon, 2016 for the Animal project at TU Darmstadt.
 * Copying this file for educational purposes is permitted without further authorization.
 */
package generators.graphics;

import generators.framework.Generator;
import generators.framework.GeneratorType;

import java.util.ArrayList;
import java.util.Locale;

import algoanim.primitives.IntMatrix;
import algoanim.primitives.Rect;
import algoanim.primitives.SourceCode;
import algoanim.primitives.Text;
import algoanim.primitives.generators.Language;

import java.util.Hashtable;

import generators.framework.properties.AnimationPropertiesContainer;
import algoanim.animalscript.AnimalScript;

import java.awt.Color;
import java.awt.Font;

import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.RectProperties;
import algoanim.properties.SourceCodeProperties;
import algoanim.properties.TextProperties;
import algoanim.properties.MatrixProperties;
import algoanim.util.Coordinates;
import algoanim.util.MsTiming;
import algoanim.util.Offset;
import algoanim.util.Timing;

public class RegionGrowth implements Generator {
    private Language lang;
    private int[] Saatpunkt2;
    private int[] Saatpunkt1;
    private int[] Saatpunkt3;
    private RectProperties ErlaeuterungsKasten;
    private Color Region1;
    private Color Region3;
    private Color Region2;
    private RectProperties Hintergrund;
    private SourceCodeProperties Code;
    private RectProperties CodeKasten;
    private TextProperties Erlaeuterung;
    private MatrixProperties BildEigenschaften;
    private int[][] Grauwertbild;
    
	private SourceCode rg;
	private SourceCode ap;
	private Text rgHeader;
	private Text apHeader;
	private IntMatrix pict;
	private int  tiefenZ;
	private int aufrufeZ;
	private Text tiefe;
	private Text aufrufe;
	public static final Timing animation = new MsTiming(500);
	
	/**
	 * Diese Methode initialisiert alles fuer die Animation
	 */
    public void init(){
        lang = new AnimalScript("Region Growing", "Kevin Kocon", 1050, 900);
        lang.setStepMode(true);
        
    }

    /**
     * Diese Methode generiert die Animation
     */
    public String generate(AnimationPropertiesContainer props,Hashtable<String, Object> primitives) {
        Saatpunkt2 = (int[])primitives.get("Saatpunkt2");
        Saatpunkt1 = (int[])primitives.get("Saatpunkt1");
        Saatpunkt3 = (int[])primitives.get("Saatpunkt3");
        ErlaeuterungsKasten = (RectProperties)props.getPropertiesByName("ErlaeuterungsKasten");
        Region1 = (Color)primitives.get("Region1");
        Region3 = (Color)primitives.get("Region3");
        Region2 = (Color)primitives.get("Region2");
        Hintergrund = (RectProperties)props.getPropertiesByName("Hintergrund");
        Code = (SourceCodeProperties)props.getPropertiesByName("Code");
        CodeKasten = (RectProperties)props.getPropertiesByName("CodeKasten");
        Erlaeuterung = (TextProperties)props.getPropertiesByName("Erlaeuterung");
        BildEigenschaften = (MatrixProperties)props.getPropertiesByName("BildEigenschaften");
        Grauwertbild = (int[][])primitives.get("Grauwertbild");
        
        Erlaeuterung.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 13));
        tiefenZ=0;
        aufrufeZ=0;
        ArrayList<int[]> seeds = new ArrayList<int[]>();
        seeds.add(Saatpunkt1);
        seeds.add(Saatpunkt2);
        seeds.add(Saatpunkt3);
        grow(this.Grauwertbild,seeds);
        
        return lang.toString();
    }

    /**
     * Diese Methode bereitet alle Dinge fuer die Animation vor, die vor bzw. nach dem letzten Schritt des Algos. gebraucht werden und ruft diesen auf.
     * @param picture Grauwertbild
     * @param seedpoints uebergebene saatpunkte als array, wobei 0.Eintrag= x-Koordinate, 1.Eintrag=y-Koordinate, 2.Eintrag=Thershold
     */
    public void grow(int[][] picture, ArrayList<int[]>  seedpoints){
		//Erstelle Titel
		TextProperties headerProps = new TextProperties();
		headerProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 24));
		Text header = lang.newText(new Coordinates(20, 30), "Region Growing","header", null, headerProps);
		    
		//Erstelle Umrandung fuer Titel
		RectProperties rectPropsT = new RectProperties();
		rectPropsT.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectPropsT.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectPropsT.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		Rect headerRect = lang.newRect(new Offset(-5, -5, "header",AnimalScript.DIRECTION_NW), new Offset(5, 5, "header", "SE"), "hRect",null, rectPropsT);
		
		//Erstelle Einleitungstitel
		TextProperties codeTitleProps = new TextProperties();
		codeTitleProps.set(AnimationPropertiesKeys.FONT_PROPERTY, new Font(Font.SANS_SERIF, Font.BOLD, 16));
		Text einleitungsTitel = lang.newText(new Offset(0,20,"header",AnimalScript.DIRECTION_SW), "Einleitung","EinleitungsTitle", null, codeTitleProps);
		
		//Erstelle Umrandung fuer Einleitungstitel
		RectProperties rectPropsCT = new RectProperties();
		rectPropsCT.set(AnimationPropertiesKeys.FILLED_PROPERTY, true);
		rectPropsCT.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		rectPropsCT.set(AnimationPropertiesKeys.DEPTH_PROPERTY, 2);
		Rect EinTitleRect = lang.newRect(new Offset(-5, -5, "EinleitungsTitle",AnimalScript.DIRECTION_NW), new Offset(5, 5, "EinleitungsTitle", "SE"), "ETRect",null, rectPropsCT);
		
		//Erstelle Hintergrund Einleitung/Fazit
		Rect backgroundE = lang.newRect(new Coordinates(0,0),new Coordinates(1100,1000), "EBRect",null, Hintergrund);
		
		//ErlaeuterungsKasten Einleitung/Fazit
		Rect desRectE = lang.newRect(new Offset(0, 0, "ETRect",AnimalScript.DIRECTION_NW), new Offset(-150, -350, "EBRect", "SE"), "DERect",null, ErlaeuterungsKasten);
		
		//Erstelle Erlaeuterung
		Text d1 = lang.newText(new Offset(80,90,"ETRect","NW"), "Beim 'Region Growing' handelt es sich um einen Segmentierungsalgorithmus, welcher häufig in der medizinischen","d1", null, Erlaeuterung);
		Text d2 = lang.newText(new Offset(0,7,"d1","SW"), "Bildverarbeitung verwendet wird.","d2", null, Erlaeuterung);
		Text d3 = lang.newText(new Offset(0,7,"d2","SW"), "Beispielsweise wird dieser Algorithmus zum Segmentieren der Lunge aus Röntgenaufnahmen genutzt, da ihre","d3", null, Erlaeuterung);
		Text d4 = lang.newText(new Offset(0,7,"d3","SW"), "Grauwerte sich stark von den Grauwerten der Umgebung unterscheiden.","d4", null, Erlaeuterung);
		Text d5 = lang.newText(new Offset(0,7,"d4","SW"), "Ausgehend von x verschiedenen Saatpunkten im Bild, werden rekursiv alle umliegenden Punkte zur Region des aktuell","d5", null, Erlaeuterung);
		Text d6 = lang.newText(new Offset(0,7,"d5","SW"), "betrachteten Saatpunktes hinzugefügt, welche das Homogenitätskriterium der Region erfüllen. Der Algorithmus erhält","d6", null, Erlaeuterung);
		Text d7 = lang.newText(new Offset(0,7,"d6","SW"), "also ein Bild (in unserem Fall ein Graubild), die Positionen der Saatpunkte im Bild und eine erlaubte Abweichung","d7", null, Erlaeuterung);
		Text d8 = lang.newText(new Offset(0,7,"d7","SW"), "der Grauwerte zu den Saatpunkten (Threshold). Als Ergebnis bekommen wir ein in Regionen geteiltes Bild.","d8", null, Erlaeuterung);
		Text d9 = lang.newText(new Offset(0,7,"d8","SW"), "In dieser Animation werden wir nun das untenstehende übergebene Bild, mit eingefärbten Saatpunkten, segmentieren.","d9", null, Erlaeuterung);

		//Erstelle zu visualisierendes Grauwertbild
		IntMatrix bild = lang.newIntMatrix(new Offset(50,50,"d9","SW"), picture, "pictureE",null, BildEigenschaften);
		
		//Faerbe Seedpoints in Bild
		bild.setGridFillColor(Saatpunkt1[1], Saatpunkt1[0], Region1, null, null);
		bild.setGridFillColor(Saatpunkt2[1], Saatpunkt2[0], Region2, null, null);
		bild.setGridFillColor(Saatpunkt3[1], Saatpunkt3[0], Region3, null, null);
		
		lang.nextStep("Einleitung");
		
		d1.hide();
		d2.hide();
		d3.hide();
		d4.hide();
		d5.hide();
		d6.hide();
		d7.hide();
		d8.hide();
		d9.hide();
		
		einleitungsTitel.hide();
		EinTitleRect.hide();
		backgroundE.hide();
		desRectE.hide();
		bild.hide();
		
		//Erstelle "Codetitel"
		lang.newText(new Offset(0,20,"header",AnimalScript.DIRECTION_SW), "Code","CodeTitle", null, codeTitleProps);
		  
		//Erstelle Umrandung fuer CodeTitel
		lang.newRect(new Offset(-5, -5, "CodeTitle",AnimalScript.DIRECTION_NW), new Offset(5, 5, "CodeTitle", "SE"), "CTRect",null, rectPropsCT);
		
		TextProperties CodeHeaderProp = new TextProperties();
		CodeHeaderProp.set(AnimationPropertiesKeys.FONT_PROPERTY, Code.get(AnimationPropertiesKeys.FONT_PROPERTY));
		CodeHeaderProp.set(AnimationPropertiesKeys.COLOR_PROPERTY,Code.get(AnimationPropertiesKeys.COLOR_PROPERTY));
		
		rgHeader=lang.newText(new Offset(0,20,"CodeTitle","SW"), "public void RegionGrowing(int[][] picture, ArrayList<int[]>  seedpoints) {...}", "rgHeader", null,CodeHeaderProp);
		apHeader=lang.newText(new Offset(0,25,"rgHeader","SW"), "private void assignPixel(...) {...}", "apHeader", null,CodeHeaderProp);
		
		rg = lang.newSourceCode(new Offset(0,20,"CodeTitle","SW"), "rgCode",null, Code);
		ap = lang.newSourceCode(new Offset(0,45,"CodeTitle","SW"), "apCode",null, Code);
		
		rg.hide();
		ap.hide();
		
		//Erstelle Code
		rg.addCodeLine("public void RegionGrowing(int[][] picture, ArrayList<int[]>  seedpoints) {",null, 0, null);	//0
		rg.addCodeLine("ArrayList<ArrayList<int[]>> regions = new ArrayList<ArrayList<int[]>>();",null, 1, null);	//1
		rg.addCodeLine("int reg = 0;",null, 1, null);																//2
		rg.addCodeLine("for (int[] seedpoint: seedpoints) {",null, 1, null);										//3
		rg.addCodeLine("int[] pixel = {seedpoint[0],seedpoint[1]};",null, 2, null);									//4
		rg.addCodeLine("int grayLevel = picture[seedpoint[1]][seedpoint[0]];",null, 2, null);						//5
		rg.addCodeLine("int min = grayLevel-seedpoint[2];",null, 2, null);											//6
		rg.addCodeLine("int max = grayLevel+seedpoint[2];",null, 2, null);											//7
		rg.addCodeLine("ArrayList<int[]> actRegion = new ArrayList<int[]>();",null, 2, null);						//8
		rg.addCodeLine("regions.add(actRegion);",null, 2, null);													//9
		rg.addCodeLine("assignPixel(picture,regions,pixel,reg,min,max);",null, 2, null);							//10
		rg.addCodeLine("reg++;",null, 2, null);																		//11
		rg.addCodeLine("}",null, 1, null);																			//12
		rg.addCodeLine("}",null, 0, null);																			//13
		
		ap.addCodeLine("private void assignPixel(int[][] picture, ArrayList<ArrayList<int[]>> regions,", null, 0, null);	//0
		ap.addCodeLine("int[] pixel, int region,int min, int max) {", null, 0, null);										//1
		ap.addCodeLine("if(pixel[0]>=picture[0].length||pixel[1]>=picture.length||pixel[0]<0||", null, 1, null);			//2
		ap.addCodeLine("pixel[1]<0)return;", null, 1, null);																//3
		ap.addCodeLine("if(pixelAlreadyAssigned(regions,pixel,region)==region)return;", null, 1, null);						//4
		ap.addCodeLine("int grayLevel = picture[pixel[1]][pixel[0]];", null, 1, null);										//5
		ap.addCodeLine("int pixelAssignedTo=pixelAlreadyAssigned(regions,pixel,-1);", null, 1, null);						//6
		ap.addCodeLine("boolean pixelAlreadyAssigned=(pixelAssignedTo!=-1);", null, 1, null);								//7
		ap.addCodeLine("if (grayLevel>=min && grayLevel<=max) {", null, 1, null);											//8
		ap.addCodeLine("if (pixelAlreadyAssigned) {", null, 2, null);														//9
		ap.addCodeLine("unionRegions(regions,pixelAssignedTo,region);", null, 3, null);										//10
		ap.addCodeLine("}", null, 2, null);																					//11
		ap.addCodeLine("else {", null, 2, null);																			//12
		ap.addCodeLine("regions.get(region).add(pixel);", null, 3, null);													//13
		ap.addCodeLine("}", null, 2, null);																					//14
		ap.addCodeLine("int[] p1 = {pixel[0],pixel[1]-1};", null, 2, null);													//15
		ap.addCodeLine("int[] p2 = {pixel[0]-1,pixel[1]};", null, 2, null);
		ap.addCodeLine("int[] p3 = {pixel[0]+1,pixel[1]};", null, 2, null);
		ap.addCodeLine("int[] p4 = {pixel[0],pixel[1]+1};", null, 2, null);													//18
		ap.addCodeLine("assignPixel(picture,regions,p1,region,min,max);", null, 2, null);									//19
		ap.addCodeLine("assignPixel(picture,regions,p2,region,min,max);", null, 2, null);
		ap.addCodeLine("assignPixel(picture,regions,p3,region,min,max);", null, 2, null);
		ap.addCodeLine("assignPixel(picture,regions,p4,region,min,max);", null, 2, null);
		ap.addCodeLine("}", null, 1, null);																					//23
		ap.addCodeLine("}", null, 0, null);																					//24
		
		//Erstelle Umrandung fuer Code
		lang.newRect(new Offset(0, 0, "CTRect",AnimalScript.DIRECTION_NW), new Offset(5, 5, "apCode", "SE"), "CRect",null, CodeKasten);
		
		pict = lang.newIntMatrix(new Offset(30,0,"CRect","NE"), picture, "picture",null, BildEigenschaften);
		
		
		//Erstelle "Erlaeuterungtitel"
		lang.newText(new Offset(0,30,"picture",AnimalScript.DIRECTION_SW), "Erläuterung","DescriptionTitle", null, codeTitleProps);
		
		//Kasten um Titel
		lang.newRect(new Offset(-5, -5, "DescriptionTitle",AnimalScript.DIRECTION_NW), new Offset(5, 5, "DescriptionTitle", "SE"), "DTRect",null, rectPropsCT);
		
		//Erstelle Properties fuer Erlaeuterungen
		Text big = lang.newText(new Offset(30,60,"DTRect","NW"), "Seedpoint auf und zähle die Anzahl der betrachteten Seedpoints hoch.","big", null, Erlaeuterung);
		big.hide();
		
		//Erstelle Umrandung fuer Erlaeuterung
		lang.newRect(new Offset(0, 0, "DTRect",AnimalScript.DIRECTION_NW), new Offset(50,240, "big", "SE"), "DRect",null,ErlaeuterungsKasten);
		
		//Hintergrund
		lang.newRect(new Coordinates(0,0),new Offset(80,200,"DRect","SE"), "BRect",null, Hintergrund);
		
		//Faerbe Seedpoints in Bild
		pict.setGridFillColor(Saatpunkt1[1], Saatpunkt1[0], Region1, null, null);
		pict.setGridFillColor(Saatpunkt2[1], Saatpunkt2[0], Region2, null, null);
		pict.setGridFillColor(Saatpunkt3[1], Saatpunkt3[0], Region3, null, null);
		
		RegionGrowingAlg(picture,seedpoints);
		
		lang.hideAllPrimitives();
		header.show();
		headerRect.show();
		einleitungsTitel.setText("Fazit", null, null);
		einleitungsTitel.show();
		EinTitleRect.show();
		backgroundE.show();
		desRectE.show();
		
		//Fazit erstellen
		lang.newText(new Offset(80,90,"ETRect","NW"), "An dieser Stelle ist der Algorithmus nun zu Ende. Das Ergebnis ist das untenstehende Bild, welches in die 3 Regionen","f1", null, Erlaeuterung);
		lang.newText(new Offset(0,7,"f1","SW"), "unterteilt wurde.","f2", null, Erlaeuterung);
		lang.newText(new Offset(0,7,"f2","SW"), "Der große Vorteil dieses Algorithmus ist, das dieser sowohl relativ simpel zu verstehen, als auch zu implementieren ist.","f3", null, Erlaeuterung);
		lang.newText(new Offset(0,7,"f3","SW"), "Außerdem ist dieser auch sehr leicht erweiterbar auf 3D (einfach die 2 weiteren in z-Richtung angrenzenden Voxel","f4", null, Erlaeuterung);
		lang.newText(new Offset(0,7,"f4","SW"), "rekursiv zuordnen).","f5", null, Erlaeuterung);
		lang.newText(new Offset(0,7,"f5","SW"), "Ein großes Problem jedoch ist die Wahl der Saatpunkte und Homogenitätskriterien für jede Segmentierung. Eine andere","f6", null, Erlaeuterung);
		lang.newText(new Offset(0,7,"f6","SW"), "Wahl von Saatpunkten kann sogar mit selben Homogenitätskriterien im gleichen Bild zur einer komplett anderen","f7", null, Erlaeuterung);
		lang.newText(new Offset(0,7,"f7","SW"), "Segmentierung führen.","f8", null, Erlaeuterung);
		
		pict.show();
		pict.moveTo("NW", "translate", new Offset(50,50,"f8","SW"), null, null);
		lang.nextStep("Fazit");
	}
	
    
    /**
     * Initialisierung des RegionGrowing 
     * @param picture zu segmentierendes grauwertbild
     * @param seedpoints uebergebene saatpunkte als array, wobei 0.Eintrag= x-Koordinate, 1.Eintrag=y-Koordinate, 2.Eintrag=Thershold
     */
	public void RegionGrowingAlg(int[][] picture, ArrayList<int[]>  seedpoints){
		
		// Einfuerende Animation
		apHeader.moveTo("NW", "translate", new Offset(0,45,"rgCode","SW"), null, animation);
		rgHeader.hide(animation);
		rg.show(animation);
		
		//Erlaeuterung
		Text des11 = lang.newText(new Offset(30,60,"DTRect","NW"), "Erstelle einen beliebigen Datentypen, in dem die einzelnen Regionen","des11", null, Erlaeuterung);
		Text des12 = lang.newText(new Offset(0,7,"des11","SW"), "gespeichert werden sollen und initialisiere einen Zähler für die","des12", null, Erlaeuterung);
		Text des13 = lang.newText(new Offset(0,7,"des12","SW"), "Anzahl der schon betrachteten Regionen.","des13", null, Erlaeuterung);
		
		lang.newText(new Offset(30,-30,"DRect","SW"), "Rekursionstiefe:","t", null, Erlaeuterung);
		lang.newText(new Offset(0,-20,"t","NW"), "Anzahl rekursiver Aufrufe:","a", null, Erlaeuterung);
		lang.newText(new Offset(0,-20,"a","NW"), "Homogenitätskriterium der aktuellen Region: ","h", null, Erlaeuterung);
		
		Text homo = lang.newText(new Offset(7,0,"h","NE"), "","homo", null, Erlaeuterung);
		tiefe = lang.newText(new Offset(7,0,"t","NE"), ""+tiefenZ,"tiefe", null, Erlaeuterung);
		aufrufe = lang.newText(new Offset(7,0,"a","NE"), ""+aufrufeZ,"a", null, Erlaeuterung);
		
		rg.highlight(1);
		rg.highlight(2);
		lang.nextStep();
		des11.hide();
		des12.hide();
		des13.hide();
		rg.unhighlight(1);
		rg.unhighlight(2);
		
		ArrayList<ArrayList<int[]>> regions = new ArrayList<ArrayList<int[]>>();
		int seeds = 0;
		
		for (int[] seedpoint: seedpoints) {
			//Einfuerende Animation
			if(seeds!=0){
			ap.hide();
			apHeader.show();
			apHeader.moveTo("NW", "translate", new Offset(0,45,"rgCode","SW"), null, animation);
			rgHeader.hide(animation);
			rg.show(animation);
			tiefe.setText(""+tiefenZ, null, null);
			}
			
			int[] pixel = {seedpoint[0],seedpoint[1]};
			int grayLevel = picture[seedpoint[1]][seedpoint[0]];
			int min = grayLevel-seedpoint[2];
			int max = grayLevel+seedpoint[2];
			
			homo.setText(min+"-"+max, null, null);
			//Erlaeuterung
			Text des21 = lang.newText(new Offset(30,60,"DTRect","NW"), "Betrachte nun den "+(seeds+1)+". Seedpoint von allen, bestimme","des21", null, Erlaeuterung);
			Text des22 = lang.newText(new Offset(0,7,"des21","SW"), "seine Pixelkoordinaten, seine Graustufe und berechne die","des22", null, Erlaeuterung);
			Text des23 = lang.newText(new Offset(0,7,"des22","SW"), "minimale bzw. maximale Graustufe, welche das Homogenitätskriterium","des23", null, Erlaeuterung);
			Text des24 = lang.newText(new Offset(0,7,"des23","SW"), "des Seedpoints bzw. der Region erfüllt.","des24", null, Erlaeuterung);
			
			rg.highlight(4);
			rg.highlight(5);
			rg.highlight(6);
			rg.highlight(7);
			
			lang.nextStep();
			des21.hide();
			des22.hide();
			des23.hide();
			des24.hide();
			rg.unhighlight(4);
			rg.unhighlight(5);
			rg.unhighlight(6);
			rg.unhighlight(7);
			
			
			//Erlaeuterung
			Text des31 = lang.newText(new Offset(30,60,"DTRect","NW"), "Erstelle einen beliebigen Datentypen, in dem die Pixelkoordinaten","des31", null, Erlaeuterung);
			Text des32 = lang.newText(new Offset(0,7,"des31","SW"), "der aktuellen Region gespeichert werden sollen. Füge diese Region","des32", null, Erlaeuterung);
			Text des33 = lang.newText(new Offset(0,7,"des32","SW"), "in den Datentypen für alle Regionen ein, rufe 'assignPixel' auf dem","des33", null, Erlaeuterung);
			Text des34 = lang.newText(new Offset(0,7,"des33","SW"), "Seedpoint auf und zähle die Anzahl der betrachteten Seedpoints hoch.","des34", null, Erlaeuterung);
			
			rg.highlight(8);
			rg.highlight(9);
			rg.highlight(10);
			rg.highlight(11);
			
			lang.nextStep("Betrachte Region "+(seeds+1));
			des31.hide();
			des32.hide();
			des33.hide();
			des34.hide();
			rg.unhighlight(8);
			rg.unhighlight(9);
			rg.unhighlight(10);
			rg.unhighlight(11);
			
			ArrayList<int[]> actRegion = new ArrayList<int[]>();
			regions.add(actRegion);
			assignPixel(picture,regions,pixel,seeds,min,max,true);
			seeds++;
		}
	}
	
	/**
	 * Diese Methode ordnet einen Pixel zur aktuellen Region zu wenn er das homogenitätskriterium erfuellt
	 * @param picture das grauwertbild
	 * @param regions liste aller regionen 
	 * @param pixel zuzuordnender pixel
	 * @param region aktuell betrachtete region
	 * @param min kleinser grauwert, welcher das homogenitätskriterium erfüllt
	 * @param max groesster grauwert, welcher das homogenitätskriterium erfüllt
	 * @param anim true wenn eine animation stattfinden soll welche den code repraesentiert
	 */
	private void assignPixel(int[][] picture,ArrayList<ArrayList<int[]>> regions ,int[] pixel, int region,int min, int max,boolean anim){
		//Einfuerende Animation und anpassen der Zahlervariablen fuer rekursive aufrufe und tiefe
		aufrufeZ++;
		tiefenZ++;
		aufrufe.setText(""+aufrufeZ, null, null);
		tiefe.setText(""+tiefenZ, null, null);
		if(anim){
		rg.hide();
		rgHeader.show();
		apHeader.moveTo("NW","translate",new Offset(0,25,"rgHeader","SW"), null, animation);
		apHeader.hide(animation);
		ap.show(animation);
		}
		
		//Erlaeuterung
		Text des41 = lang.newText(new Offset(30,60,"DTRect","NW"), "Prüfe ob es den Pixel an der Position ("+pixel[0]+","+pixel[1]+") überhaupt","des41", null, Erlaeuterung);
		Text des42 = lang.newText(new Offset(0,7,"des41","SW"), "gibt und ob dieser noch nicht in die aktuell betrachtete Region","des42", null, Erlaeuterung);
		Text des43 = lang.newText(new Offset(0,7,"des42","SW"), "zugeordnet wurde. (Rekursionsanker)","des43", null, Erlaeuterung);
		
		ap.highlight(2);
		ap.highlight(3);
		ap.highlight(4);
		
		lang.nextStep();
		
		des41.hide();
		des42.hide();
		des43.hide();
		ap.unhighlight(2);
		ap.unhighlight(3);
		ap.unhighlight(4);
		
		//Rekursionsanker
		if(pixel[0]>=picture[0].length||pixel[1]>=picture.length||pixel[0]<0||pixel[1]<0){
			tiefenZ--;
			return;
		}
		if(pixelAlreadyAssigned(regions,pixel,region)==region){
			tiefenZ--;
			return;
		}

		pict.highlightCell(pixel[1], pixel[0], null, null);
		pict.highlightElem(pixel[1], pixel[0], null, null);
		
		//Erlaeuterung
		Text des51 = lang.newText(new Offset(30,60,"DTRect","NW"), "Rekursionsanker hat nicht zugeschlagen, also 'besorge' die","des51", null, Erlaeuterung);
		Text des52 = lang.newText(new Offset(0,7,"des51","SW"), "Graustufe des Pixels und prüfe ob und wenn ja dann in welche Region","des52", null, Erlaeuterung);
		Text des53 = lang.newText(new Offset(0,7,"des52","SW"), "der Pixel schon zugeteilt wurde.","des53", null, Erlaeuterung);
		
		ap.highlight(5);
		ap.highlight(6);
		ap.highlight(7);
		
		int grayLevel = picture[pixel[1]][pixel[0]];
		int pixelAssignedTo=pixelAlreadyAssigned(regions,pixel,-1);
		boolean pixelAlreadyAssigned=(pixelAssignedTo!=-1);
		
		lang.nextStep();
		
		des51.hide();
		des52.hide();
		des53.hide();
		ap.unhighlight(5);
		ap.unhighlight(6);
		ap.unhighlight(7);
		
		//Erlaeuterung
		Text des61 = lang.newText(new Offset(30,60,"DTRect","NW"), "Prüfe ob der Grauwert des aktuell betrachteten Pixels das","des61", null, Erlaeuterung);
		Text des62 = lang.newText(new Offset(0,7,"des61","SW"), "Homogenitätskriterium von Region "+(region+1)+" erfüllt.","des62", null, Erlaeuterung);
		
		ap.highlight(8);
		ap.highlight(23);
		
		lang.nextStep();
		des61.hide();
		des62.hide();
		ap.unhighlight(8);
		ap.unhighlight(23);
		
		//Pruefe homogenitätskriterium
		if (grayLevel>=min && grayLevel<=max) {
			Text des71 = lang.newText(new Offset(30,60,"DTRect","NW"), "Ja, Pixel erfüllt das Homogenitätskriterium, also","des71", null, Erlaeuterung);
			Text des72 = lang.newText(new Offset(0,7,"des71","SW"), "prüfe ob der aktuelle Pixel schon in eine andere","des72", null, Erlaeuterung);
			Text des73 = lang.newText(new Offset(0,7,"des72","SW"), "Region zugeteilt wurde.","des73", null, Erlaeuterung);
			ap.highlight(9);
			ap.highlight(11);
			
			lang.nextStep();
			
			des71.hide();
			des72.hide();
			des73.hide();
			ap.unhighlight(9);
			ap.unhighlight(11);
			
			//Pixel schon zugeordnet?
			if (pixelAlreadyAssigned) {
				Text des81 = lang.newText(new Offset(30,60,"DTRect","NW"), "Auch das ist der Fall, also vereinige Region "+region,"des81", null, Erlaeuterung);
				Text des82 = lang.newText(new Offset(0,7,"des81","SW"), "und Region "+pixelAssignedTo+" miteinander","des82", null, Erlaeuterung);
				ap.highlight(10);
				
				unionRegions(regions,pixelAssignedTo,region);
				
				lang.nextStep();
				des81.hide();
				des82.hide();
				ap.unhighlight(10);
			}
			//Pixel nicht zugeordnet
			else {
				Text des91 = lang.newText(new Offset(30,60,"DTRect","NW"), "Das ist nicht der Fall, also füge den aktuell betrachteten","des91", null, Erlaeuterung);
				Text des92 = lang.newText(new Offset(0,7,"des91","SW"), "Pixel zu Region "+(region+1)+" hinzu.","des92", null, Erlaeuterung);
				ap.highlight(12);
				ap.highlight(13);
				ap.highlight(14);
				
				if(region==0)pict.setGridFillColor(pixel[1], pixel[0], Region1, null, null);
				else if(region==1)pict.setGridFillColor(pixel[1], pixel[0], Region2, null, null);
				else if(region==2)pict.setGridFillColor(pixel[1], pixel[0], Region3, null, null);
				
				//Also fuege in aktuelle Region
				regions.get(region).add(pixel);
				
				lang.nextStep();
				des91.hide();
				des92.hide();
				ap.unhighlight(12);
				ap.unhighlight(13);
				ap.unhighlight(14);
				}
			
			Text des101 = lang.newText(new Offset(30,60,"DTRect","NW"), "Bestimme nun die 4 an den Kanten des aktuellen Pixel sich","des101", null, Erlaeuterung);
			Text des102 = lang.newText(new Offset(0,7,"des101","SW"), "befindenden Pixel und rufe 'assignPixel' auf diese rekursiv auf.","des102", null, Erlaeuterung);
			ap.highlight(15);
			ap.highlight(16);
			ap.highlight(17);
			ap.highlight(18);
			ap.highlight(19);
			ap.highlight(20);
			ap.highlight(21);
			ap.highlight(22);
			
			//8 benachbarte Pixel
			int[] p1 = {pixel[0],pixel[1]-1};
			int[] p2 = {pixel[0]-1,pixel[1]};
			int[] p3 = {pixel[0]+1,pixel[1]};
			int[] p4 = {pixel[0],pixel[1]+1};
			
			lang.nextStep();
			des101.hide();
			des102.hide();
			ap.unhighlight(15);
			ap.unhighlight(16);
			ap.unhighlight(17);
			ap.unhighlight(18);
			ap.unhighlight(19);
			ap.unhighlight(20);
			ap.unhighlight(21);
			ap.unhighlight(22);
			
			pict.unhighlightCell(pixel[1], pixel[0], null, null);
			pict.unhighlightElem(pixel[1], pixel[0], null, null);
			
			//Rekursiver Aufruf auf benachbarte Pixel
			assignPixel(picture,regions,p1,region,min,max,false);
			assignPixel(picture,regions,p2,region,min,max,false);
			assignPixel(picture,regions,p3,region,min,max,false);
			assignPixel(picture,regions,p4,region,min,max,false);
			
		}
		else{
		Text des111 = lang.newText(new Offset(30,60,"DTRect","NW"), "Nein, Pixel erfüllt nicht das Homogenitätskriterium, also","des111", null, Erlaeuterung);
		Text des112 = lang.newText(new Offset(0,7,"des111","SW"), "sind wir an dieser Stelle schon am Ende dieses Aufrufes","des112", null, Erlaeuterung);
		lang.nextStep();
		des111.hide();
		des112.hide();
		pict.unhighlightCell(pixel[1], pixel[0], null, null);
		pict.unhighlightElem(pixel[1], pixel[0], null, null);
		//Ansonsten vereinige nicht regionen und fuege auch nicht in aktuelle ein 
		}
		tiefenZ--;
	}
	
	//-1 gesammt
	/**
	 * Diese Methode prueft ob ein pixel schon in einer bestimmten region ist, wobei mit eingaben -1 geprueft wird ob er überhaupt zugeordnet wurde
	 * @param regions liste aller Regionen 
	 * @param pixel der uebergebene Pixel
	 * @param reg es wird geprueft ob der pixel in dieser region zugeordnet wurde, wobei -1 alle regionen sind
	 * @return true, wenn pixel in region schon zugeteilt ist
	 */
	public int pixelAlreadyAssigned(ArrayList<ArrayList<int[]>> regions,int[] pixel,int reg){
		if(reg==-1){
		int regionNum=0;
		for(ArrayList<int[]> region: regions){
			for(int[] ActPixel: region){
				if(ActPixel[0]==pixel[0]&&ActPixel[1]==pixel[1])return regionNum;
			}
			regionNum++;
		}
		return -1;
		}
		else {
			for(int[] ActPixel: regions.get(reg)){
				if(ActPixel[0]==pixel[0]&&ActPixel[1]==pixel[1])return reg;
				}
			return -1;
			}
		}
		
	
/**
 * Diese Methode vereinigt 2 Regionen
 * @param regions Die liste mit in welchen alle Regionen sind
 * @param region1 wird in region2 gefuegt
 * @param region2 in diese region wird gefuegt
 */
	public void unionRegions(ArrayList<ArrayList<int[]>> regions,int region1,int region2){
		//grafische Umsetzung
		Color nRegion;
		if(region2==1)nRegion=Region2;
		else nRegion=Region3;
		
		for(int[] region: regions.get(region1)){
		pict.setGridFillColor(region[1], region[0], nRegion, null, animation);	
		}
		
		while(regions.get(region1).size()!=0){
			regions.get(region2).add(regions.get(region1).get(0));
			regions.get(region1).remove(0);
		}
	}
	
	/**
	 * Diese Methode gibt den Namen des Generators aus
	 */
    public String getName() {
        return "Region Growing";
    }

    /**
     * Diese Methode gibt den Namen des Algorithmus aus
     */
    public String getAlgorithmName() {
        return "Region Growing";
    }

    /**
     * Diese Methode gibt den Namen des Authors des Algos. aus
     */
    public String getAnimationAuthor() {
        return "Kevin Kocon";
    }

    /**
     * Diese Methode gibt die Beschreibung des Algos. aus
     */
    public String getDescription(){
        return "Beim \"Region Growing\" handelt es sich um einen Segmentierungsalgorithmus, welcher häufig in der medizinischen Bildverarbeitung verwendet wird."
 +"\n"
 +"Beispielsweise wird dieser Algorithmus zum Segmentieren der Lunge aus Röntgenaufnahmen genutzt, da ihre Grauwerte sich stark von den Grauwerten der Umgebung unterscheiden."
 +"\n"
 +"Ausgehend von x verschiedenen Saatpunkten im Bild, werden rekursiv alle umliegenden Punkte zur Region des aktuell betrachteten Saatpunktes hinzugefügt, welche das Homogenitätskriterium der Region erfüllen. Der Algorithmus erhält also ein Bild (in unserem Fall ein Graubild), die Positionen der Saatpunkte im Bild und eine erlaubte Abweichung der Grauwerte zu den Saatpunkten (Threshold). Als Ergebnis bekommen wir ein in Regionen geteiltes Bild."
 +"\n"
 +"\n"
 +"Hinweis: Bei den Einstellungen werden die Saatpunkte als Array der Länge 3 übergeben, wobei Index 0 die x-Koordinate des Saatpunktes ist, Index 1 die y-Koordinate und Index 2 der Threshold. ";
    }

    /**
     * Diese Methode gibt ein Codebeispiel fuer den Algo. aus
     */
    public String getCodeExample(){
        return "public void RegionGrowing(int[][] picture, ArrayList<int[]>  seedpoints) {"
 +"\n"
 +" "
 +"\n"
 +" ArrayList<ArrayList<int[]>> regions = new ArrayList<ArrayList<int[]>>();"
 +"\n"
 +" int reg = 0;"
 +"\n"
 +"		"
 +"\n"
 +" for (int[] seedpoint: seedpoints) {"
 +"\n"
 +"  int[] pixel = {seedpoint[0],seedpoint[1]};"
 +"\n"
 +"  int grayLevel = picture[seedpoint[1]][seedpoint[0]];"
 +"\n"
 +"  int min = grayLevel-seedpoint[2];"
 +"\n"
 +"  int max = grayLevel+seedpoint[2];"
 +"\n"
 +"  ArrayList<int[]> actRegion = new ArrayList<int[]>();"
 +"\n"
 +"  regions.add(actRegion);"
 +"\n"
 +"  assignPixel(picture,regions,pixel,reg,min,max);"
 +"\n"
 +"  reg++;"
 +"\n"
 +" }"
 +"\n"
 +"}"
 +"\n"
 +"\n"
 +"\n"
 +"private void assignPixel(int[][] picture, ArrayList<ArrayList<int[]>> regions,"
 +"\n"
 +"int[] pixel, int region,int min, int max) {"
 +"\n"
 +"		"
 +"\n"
 +" if(pixel[0]>=picture[0].length||pixel[1]>=picture.length||pixel[0]<0||   "
 +"\n"
 +" pixel[1]<0)return;"
 +"\n"
 +" if(pixelAlreadyAssigned(regions,pixel,region)==region)return;"
 +"\n"
 +"\n"
 +" int grayLevel = picture[pixel[1]][pixel[0]];"
 +"\n"
 +" int pixelAssignedTo=pixelAlreadyAssigned(regions,pixel,-1);"
 +"\n"
 +" boolean pixelAlreadyAssigned=(pixelAssignedTo!=-1);"
 +"\n"
 +"\n"
 +" if (grayLevel>=min && grayLevel<=max) {"
 +"\n"
 +"  if (pixelAlreadyAssigned) {"
 +"\n"
 +"   unionRegions(regions,pixelAssignedTo,region);"
 +"\n"
 +"  }"
 +"\n"
 +"  else {"
 +"\n"
 +"   regions.get(region).add(pixel);"
 +"\n"
 +"  }"
 +"\n"
 +"  int[] p1 = {pixel[0],pixel[1]-1};"
 +"\n"
 +"  int[] p2 = {pixel[0]-1,pixel[1]};"
 +"\n"
 +"  int[] p3 = {pixel[0]+1,pixel[1]};"
 +"\n"
 +"  int[] p4 = {pixel[0],pixel[1]+1};"
 +"\n"
 +"			"
 +"\n"
 +"  assignPixel(picture,regions,p1,region,min,max);"
 +"\n"
 +"  assignPixel(picture,regions,p2,region,min,max);"
 +"\n"
 +"  assignPixel(picture,regions,p3,region,min,max);"
 +"\n"
 +"  assignPixel(picture,regions,p4,region,min,max);"
 +"\n"
 +" }"
 +"\n"
 +"}";
    }

    /**
     * Diese Methode gibt den Dateitypen des generierten Generators aus
     */
    public String getFileExtension(){
        return "asu";
    }

    /**
     * Diese Methode gibt die (natuerlicher) Sprache in welcher der Generator ist
     */
    public Locale getContentLocale() {
        return Locale.GERMAN;
    }

    /**
     * Diese Methode gibt den Typen des Generators aus
     */
    public GeneratorType getGeneratorType() {
        return new GeneratorType(GeneratorType.GENERATOR_TYPE_GRAPHICS);
    }

    /**
     * Diese Methode gibt die Programmiersprache aus anhand welcher der Generator den Algo erklaert
     */
    public String getOutputLanguage() {
        return Generator.JAVA_OUTPUT;
    }

}